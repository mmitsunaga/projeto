package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet que controla a movimentação de audiências. 
 * No momento somente as Sessões de 2º grau poderão ser movimentadas em um momento posterior ao julgamento dos processos vinculados a essa.
 * 
 * @author msapaula
 * 
 */
public class AudienciaMovimentacaoCt extends AudienciaCtGen {

	private static final long serialVersionUID = -5964601547815786867L;

    public int Permissao() {
		return AudienciaMovimentacaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca,
			String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AudienciaMovimentacaoDt audienciaMovimentacaoDt;
		AudienciaNe audienciaNe;
    	//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";
		int paginaAnterior = 0;
		String stAcao = "/WEB-INF/jsptjgo/AudienciaMovimentacao.jsp";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma", "AudienciaMovimentacao");
		request.setAttribute("tempRetorno", "AudienciaMovimentacao");
		request.setAttribute("TituloPagina", "Concluir Audiência");

		audienciaNe = (AudienciaNe) request.getSession().getAttribute("Audienciane");
		if (audienciaNe == null) audienciaNe = new AudienciaNe();

		audienciaMovimentacaoDt = (AudienciaMovimentacaoDt) request.getSession().getAttribute("AudienciaMovimentacaoDt");
		if (audienciaMovimentacaoDt == null) audienciaMovimentacaoDt = new AudienciaMovimentacaoDt();

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		
		audienciaMovimentacaoDt.setAudienciaStatusCodigo(request.getParameter("AudienciaStatusCodigo"));
		audienciaMovimentacaoDt.setAudienciaStatus(request.getParameter("AudienciaStatus"));
		audienciaMovimentacaoDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		audienciaMovimentacaoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		audienciaMovimentacaoDt.setId_Modelo(request.getParameter("Id_Modelo"));
		audienciaMovimentacaoDt.setModelo(request.getParameter("Modelo"));
		audienciaMovimentacaoDt.setNomeArquivo(request.getParameter("nomeArquivo"));
		audienciaMovimentacaoDt.setTextoEditor(request.getParameter("TextoEditor"));
		audienciaMovimentacaoDt.setId_NovaSessao(request.getParameter("Id_NovaSessao"));
		audienciaMovimentacaoDt.setDataNovaSessao(request.getParameter("DataNovaSessao"));
		audienciaMovimentacaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		audienciaMovimentacaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		setParametrosAuxiliares(audienciaMovimentacaoDt, paginaAnterior, paginaatual, request, audienciaNe, UsuarioSessao);

		// -----------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {

			// Inicializa movimentação
			case Configuracao.Novo:
				audienciaMovimentacaoDt.limpar();
				limparListas(request);
				stId = request.getParameter("Id_Audiencia");
				if (stId != null && !stId.equals("")) {
					//Verifica se a sessão selecionada pode ser finalizada
					Mensagem = audienciaNe.podeFinalizarSessao(stId, UsuarioSessao.getUsuarioDt());
					if (Mensagem.length() == 0) {
						AudienciaDt audienciaDt = audienciaNe.consultarId(stId);
						audienciaMovimentacaoDt.setAudienciaDt(audienciaDt);
						audienciaMovimentacaoDt.setTextoEditor(audienciaNe.obtenhaTextoTodosExtratosAtaJulgamentoFinalizarSessao(stId, UsuarioSessao.getUsuarioDt(), true));
						request.setAttribute("TextoEditor", audienciaMovimentacaoDt.getTextoEditor());
					} else {
						redireciona(response, "AudienciaSegundoGrau?MensagemErro=" + Mensagem);
						return;
					}
				}
				break;

			//Confirmação de Finalização da Sessão
			case Configuracao.Salvar:
				// Captura lista de arquivos inseridos
				audienciaMovimentacaoDt.setListaArquivos(getListaArquivos(request));
				request.setAttribute("Mensagem", "Clique para confirmar a Finalização da Sessão.");
				break;

			// Salva finalização de Sessão
			case Configuracao.SalvarResultado:
				Mensagem = audienciaNe.verificarMovimentacaoAudiencia(audienciaMovimentacaoDt);
				if (Mensagem.length() == 0) {
					audienciaNe.salvarMovimentacaoAudienciaFinalizacaoSessaoSegundoGrau(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());

					redireciona(response, "AudienciaSegundoGrau?MensagemOk=Sessão Finalizada com Sucesso.");
					audienciaMovimentacaoDt.limpar();
					limparListas(request);
					return;
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","AudienciaMovimentacao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = audienciaNe.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;		

			// Consultar Modelos do Usuário
			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (consultarModelo(request, tempNomeBusca, PosicaoPaginaAtual, audienciaNe, audienciaMovimentacaoDt, paginaatual, UsuarioSessao)) {
					stAcao = "/WEB-INF/jsptjgo/ModeloArquivoTipoLocalizar.jsp";
				}
				break;

			default:
				stId = request.getParameter("Id_Audiencia");
				if (stId != null && !stId.isEmpty()) if (!stId.equalsIgnoreCase("")) {
					this.executar(request, response, UsuarioSessao, Configuracao.Novo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}

		}

		request.getSession().setAttribute("AudienciaMovimentacaoDt", audienciaMovimentacaoDt);
		request.getSession().setAttribute("audienciaNe", audienciaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Tratamentos necessários ao realizar uma movimentação
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(AudienciaMovimentacaoDt audienciaMovimentacaoDt, int paginaAnterior, int paginaatual,
			HttpServletRequest request, AudienciaNe audienciaNe, UsuarioNe usuarioNe) throws Exception{

		// Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo do arquivo
		if (!audienciaMovimentacaoDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = audienciaNe.consultarModeloId(audienciaMovimentacaoDt.getId_Modelo(), (ProcessoDt) audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt(), usuarioNe.getUsuarioDt());
			audienciaMovimentacaoDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			audienciaMovimentacaoDt.setArquivoTipo(modeloDt.getArquivoTipo());
			audienciaMovimentacaoDt.setTextoEditor(modeloDt.getTexto());
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("TextoEditor", audienciaMovimentacaoDt.getTextoEditor());
		request.setAttribute("Id_ArquivoTipo", audienciaMovimentacaoDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", audienciaMovimentacaoDt.getArquivoTipo());
		request.setAttribute("Modelo", audienciaMovimentacaoDt.getModelo());
		request.setAttribute("nomeArquivo", audienciaMovimentacaoDt.getNomeArquivo());
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");
	}

	/**
	 * Resgata lista de arquivos inseridos Converte de Map para List
	 */
	protected List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		List lista = Funcoes.converterMapParaList(mapArquivos);

		return lista;
	}

	/**
	 * Consulta de modelos Se não encontrar nenhum modelo retorna false
	 * @throws Exception 
	 */
	protected boolean consultarModelo(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, AudienciaNe audienciaNe,
			AudienciaMovimentacaoDt audienciaMovimentacaoDt, int paginaatual, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;

		List tempList = audienciaNe.consultarModelo(usuarioSessao.getUsuarioDt(), audienciaMovimentacaoDt.getId_ArquivoTipo(), tempNomeBusca, posicaoPaginaAtual);
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaModelo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", audienciaNe.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_Modelo", "Id_Modelo");
			request.setAttribute("tempBuscaModelo", "Modelo");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Modelo foi localizado.");
		return boRetorno;
	}

	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
	}

}
