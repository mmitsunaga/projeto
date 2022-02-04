package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * @author lsbernardes
 * 10/06/2013 09:24:24
 */
public class BuscaProcessoInternaCodigoAcessoCt  extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 64713257283622314L;
	public static final int CodigoPermissao = 807;


	public int Permissao() {
		return BuscaProcessoInternaCodigoAcessoCt.CodigoPermissao;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaAtual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		String stId = "";
		String processoNumero = "";
		String codigoAcesso = "";
		ProcessoDt processoDt;
		String stMensagem = "";
		int passoBusca = 0;
		ProcessoNe Processone;
		int passoEditar = -1;
		
		request.setAttribute("tempPrograma", "Busca Processo por Código");
		request.setAttribute("tempRetorno", "BuscaProcessoInternaCodigoAcesso");
		String stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaInternaCodigoAcesso.jsp";


		Processone = (ProcessoNe) request.getSession().getAttribute("BuscaProcessoneCodigoAcesso");
		if (Processone == null) Processone = new ProcessoNe();

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt == null) processoDt = new ProcessoDt();

		if (request.getParameter("PaginaAtual") != null) paginaAtual = Funcoes.StringToInt(request.getParameter("PaginaAtual"));

		if (request.getParameter("ProcessoNumero") == null) processoNumero = "";
		else processoNumero = request.getParameter("ProcessoNumero").replaceAll("[-]", "").trim();

		if (request.getParameter("CodigoAcesso") == null) {
			codigoAcesso = "";
			if (request.getSession().getAttribute("CodigoAcesso") != null) codigoAcesso = (String)(request.getSession().getAttribute("CodigoAcesso"));
		}
		else {
			codigoAcesso = request.getParameter("CodigoAcesso");
			request.getSession().setAttribute("CodigoAcesso",codigoAcesso);
		}

		if (request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));

		request.setAttribute("PaginaAnterior", paginaAtual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaAtual) {

			case Configuracao.Novo:
				processoNumero = "";
				codigoAcesso = "";
				stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaInternaCodigoAcesso.jsp";
				break;

			//Consulta processo pelo código de acesso informado 
			case Configuracao.Localizar:

				if (processoNumero == null || processoNumero.length() == 0) stMensagem += "Número do Processo é campo obrigatório. \n";
				else if (processoNumero.indexOf(".") < 0) stMensagem += "Número do Processo no formato incorreto. \n";

				if (codigoAcesso == null || codigoAcesso.length() == 0) stMensagem += "Código de Acesso é campo obrigatório. \n";

				if (stMensagem.length() == 0) {

					//Verifica o código passado pelo usuário, se for válido retorna o Id do Processo
					stId = Processone.verificarCodigoAcessoProcesso(codigoAcesso, processoNumero, Controle.getIpCliente(request), true);
					
					//Define código rash para acesso ao link "e outros"
					armazeneCodigoRashEOutros(request,UsuarioSessao,stId);
					if (stId != null && !stId.equalsIgnoreCase("")) {
						processoDt = Processone.consultarDadosProcessoAcessoExterno(stId, UsuarioSessao.getUsuarioDt(), true, (request.getParameter("CodigoVerificacao") != null && request.getParameter("CodigoVerificacao").equals("true")), UsuarioSessao.getNivelAcesso());
						request.getSession().setAttribute("processoDt", processoDt);

						if (processoDt.getId_Recurso().length() > 0) {
							// Quando se tratar de recurso inominado os dados do recurso serão mostrados
							RecursoDt recursoDt = Processone.consultarDadosRecurso(processoDt.getId_Recurso(), true);
							processoDt.setRecursoDt(recursoDt);
						}
						stAcao = "/WEB-INF/jsptjgo/DadosProcessoInternoCodigoAcesso.jsp";
						
					} else{
						stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaInternaCodigoAcesso.jsp";
						request.setAttribute("MensagemErro", "Processo não encontrado! Confira os dados Digitados.");
					}
						

				} else request.setAttribute("MensagemErro", stMensagem);
				break;

			// Efetua download de arquivos
			case Configuracao.Curinga6:
				String id_MovimentacaoArquivo = request.getParameter("Id_MovimentacaoArquivo");
				if (UsuarioSessao.VerificarCodigoHash(id_MovimentacaoArquivo+processoDt.getId_Processo(), request.getParameter("hash"))) {
					boolean recibo = false;
					if (request.getParameter("recibo") != null && request.getParameter("recibo").equals("true")) recibo = true;

					//Informações do usuário e IP para gerar log do download
					String id_Usuario = UsuarioSessao.getId_Usuario();
					String ipComputador = UsuarioSessao.getUsuarioDt().getIpComputadorLog();

					if (request.getParameter("CodigoVerificacao") != null && request.getParameter("CodigoVerificacao").equals("true")){
						String stIdArquivo = Processone.consultarIdArquivo(id_MovimentacaoArquivo); 
						if (stIdArquivo != null && stIdArquivo.length() > 0) {
							if (!stIdArquivo.equalsIgnoreCase("")) {
															
								// gerar pdf como arquivos da publicação
								byte[] arquivoPDF = Processone.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() ,stIdArquivo, id_MovimentacaoArquivo, processoDt,  UsuarioSessao.getUsuarioDt(), new LogDt(id_Usuario, ipComputador));																																								
								enviarPDF(response, arquivoPDF,"Publicacao");																	
								return;
								//**********************************************************************************************

							}
						} 
						else {
							request.setAttribute("Mensagem", "Erro ao efetuar consulta do arquivo.");
							RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
							dis.include(request, response);
							return;
						}
					} else {
						if (!Processone.baixarArquivoMovimentacao(id_MovimentacaoArquivo, processoDt, UsuarioSessao.getUsuarioDt(), recibo, response, new LogDt(id_Usuario, ipComputador), true)){
							//se baixou mandar para a jsp de não permitido baixar o arquivo
							stAcao = "/WEB-INF/jsptjgo/Padroes/ArquivoNaoPermitido.jsp";
							RequestDispatcher dis = request.getRequestDispatcher(stAcao);
							dis.include(request, response);
						}
					}
					
				} else exibaMensagemInconsistenciaErro(request, "Acesso negado.");
				return;

				//Visualizar Situação do Processo
			case Configuracao.Imprimir:
				this.consultarSituacaoProcesso(request, processoDt, Processone);
				stAcao = "/WEB-INF/jsptjgo/ProcessoSituacao.jsp";
				break;

			//Consultar Responsáveis pelo Processo
			case Configuracao.LocalizarDWR:
				this.consultarResponsaveisProcesso(request, processoDt, UsuarioSessao, Processone);
				stAcao = "/WEB-INF/jsptjgo/ProcessoResponsaveis.jsp";
				break;

			default:
				stAcao = "/WEB-INF/jsptjgo/DadosProcessoInternoCodigoAcesso.jsp";
					break;
		}

		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("PassoBusca", passoBusca);
		request.setAttribute("ProcessoNumero", processoNumero);
		request.setAttribute("CodigoAcesso", codigoAcesso);
		request.getSession().setAttribute("BuscaProcessoneCodigoAcesso", Processone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}


	/**
	 * Método responsável em consultar a situação completa do processo.
	 * Retorna dados referentes a pendências, conclusões e audiências em aberto no processo.
	 * 
	 * @param request
	 * @param processoDt
	 * @param processoNe
	 * @throws Exception
	 */
	private void consultarSituacaoProcesso(HttpServletRequest request, ProcessoDt processoDt, ProcessoNe processoNe) throws Exception{
		//Consultar pendências em aberto ou não vistadas
		List[] listaPendencias = processoNe.consultarPendenciasProcesso(processoDt.getId(), true);
		request.setAttribute("ListaPendencias", listaPendencias);

		//Consultar conclusões em aberto
		List conclusoesPendentes = processoNe.consultarConclusoesPendentesProcessoPublico(processoDt.getId(), true);
		request.setAttribute("ConclusaoPendente", conclusoesPendentes);

		//Consultar audiências em aberto
		List<String[]> audienciaPendente = processoNe.consultarAudienciasPendentes(processoDt.getId(), true);
		request.setAttribute("AudienciaPendente", audienciaPendente);
	}

	/**
	 * Método responsável em consultar os responsáveis pelo processo
	 * @throws Exception 
	 */
	private void consultarResponsaveisProcesso(HttpServletRequest request, ProcessoDt processoDt, UsuarioNe usuarioNe, ProcessoNe processoNe) throws Exception{
		//Responsáveis Pendência
		List listaResponsaveis = processoNe.consultarResponsaveisProcesso(processoDt.getId(), processoDt.getId_Serventia(), usuarioNe.getUsuarioDt().getGrupoCodigo());
		request.setAttribute("ListaResponsaveis", listaResponsaveis);

		List listaAdvogados = processoNe.consultarAdvogadosProcesso(processoDt.getId());
		request.setAttribute("ListaAdvogados", listaAdvogados);
	}
	
	private final String idCodigoHashEOutros = "CodigoHashEOutros";
	
	/**
	 * Armazena o código Rash gerado para validação futura durante o acesso ao link e outros, evitando uso de robô
     *      
     * @param HttpServletRequest request 
     *
	 */
	private void armazeneCodigoRashEOutros(HttpServletRequest request, UsuarioNe usuarioNe, String idProcesso) throws Exception{
		request.setAttribute(idCodigoHashEOutros, usuarioNe.getCodigoHash(idProcesso));
	}

}
