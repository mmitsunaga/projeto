package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ControleImportacaoDados;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet que controla o cadastramento de um processo criminal
 * 
 * @author msapaula
 * 
 */
public class ImportarDadosProcessoCt extends Controle {

	private static final long serialVersionUID = -6852168440039056750L;

	public int Permissao() {
		return 259;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoCadastroDt processoDt;
		ProcessoNe Processone;
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		int passoEditar = -1;
		int paginaAnterior = 0;
		String stAcao = "/WEB-INF/jsptjgo/ImportarProcesso.jsp";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempPrograma", "Processo");
		request.setAttribute("tempRetorno", "ImportarDadosProcesso");

		Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null) Processone = new ProcessoNe();

		processoDt = (ProcessoCadastroDt) request.getSession().getAttribute("ProcessoCadastroDt");
		if (processoDt == null)	processoDt = new ProcessoCadastroDt();

		if (request.getParameter("nomeArquivo") != null)
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
			request.setAttribute("nomeArquivo", "");

		processoDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		processoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		processoDt.setId_Modelo(request.getParameter("Id_Modelo"));
		processoDt.setModelo(request.getParameter("Modelo"));
		processoDt.setTextoEditor(request.getParameter("TextoEditor"));
		processoDt.setMarcarAudiencia(request.getParameter("NaoMarcarAudiencia"));
		processoDt.setMandarConcluso(request.getParameter("NaoMandarConcluso"));
		processoDt.setMarcarAudienciaConciliacao(request.getParameter("AudienciaConciliacao"));
		processoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		List listaArquivosInseridos = getListaArquivos(request); // Captura arquivos inseridos

		if (request.getAttribute("PassoEditar") != null)
			passoEditar = Funcoes.StringToInt((String) request.getAttribute("PassoEditar"));
		else if (request.getParameter("PassoEditar") != null)
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getAttribute("PaginaAnterior") != null)
			paginaAnterior = Funcoes.StringToInt((String) request.getAttribute("PaginaAnterior"));
		
		setParametrosAuxiliares(processoDt, paginaatual, request, paginaAnterior, Processone, UsuarioSessao);

		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Novo:
				processoDt.limpar();
				passoEditar = -1;
				limparListaArquivos(request);
				break;
			case Configuracao.SalvarResultado:
				getPartesIntimadasDelegacia(request, processoDt);
				Processone.cadastrarProcessoImportacao(processoDt, UsuarioSessao.getUsuarioDt());
				switch (Funcoes.StringToInt(processoDt.getCodigoInstituicao())) {
					case 1:
						stAcao = "/WEB-INF/jsptjgo/ProcessoCriminalCadastrado.jsp";
						break;
					default:
						stAcao = "/WEB-INF/jsptjgo/ProcessoCivelCadastrado.jsp";
						break;
				}
	
				request.setAttribute("ProcessoCadastroDt", processoDt);  // Joga processo no request e limpa da sessão
				processoDt = new ProcessoCadastroDt();
				limparListaArquivos(request);
				break;
				
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao", "ArquivoTipo");
					request.setAttribute("tempBuscaPrograma", "ArquivoTipo");
					request.setAttribute("tempRetorno", "ImportarDadosProcesso");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 0;
				} else {
					String stTemp = "";
					stTemp = Processone.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;
				
		case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"Modelo"};
				String[] lisDescricao = {"Modelo","Serventia","ArquivoTipo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Modelo");
				request.setAttribute("tempBuscaDescricao", "Modelo");
				request.setAttribute("tempBuscaPrograma", "Modelo");
				request.setAttribute("tempRetorno", "ImportarDadosProcesso");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				passoEditar = 0;
			} else {
				String stTemp = "";
				stTemp = Processone.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), processoDt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;
			
		default:
			switch (passoEditar) {
				case 0:
					request.setAttribute("tempRetorno", "ImportarDadosProcesso");
					processoDt.setPasso3("");
					stAcao = "/WEB-INF/jsptjgo/ImportarProcessoPeticionar.jsp";
					break;
				case 1:
					String conteudoArquivo = getConteudoArquivo(request);
					if (conteudoArquivo != null && conteudoArquivo.length() > 0) {
						LogDt logDt = new LogDt(processoDt.getId_UsuarioLog(), processoDt.getIpComputadorLog());
						ControleImportacaoDados controleImportacaoDados = new ControleImportacaoDados(conteudoArquivo, logDt);
						if (controleImportacaoDados.getMensagem().length() == 0) {
							processoDt = controleImportacaoDados.getProcessoDt();
							// Se não há erros, passa para passo de inserção de arquivos
							processoDt.setPasso1("Passo 1 OK");
							processoDt.setPasso2("Passo 2");
							stAcao = "/WEB-INF/jsptjgo/ImportarProcessoPeticionar.jsp";
						} else
							request.setAttribute("MensagemErro", controleImportacaoDados.getMensagem());
					} 
					else
						request.setAttribute("MensagemErro", "Selecione um Arquivo para prosseguir.");
					break;
				// Redireciona para tela de confirmação
				case 2:
					stAcao = "/WEB-INF/jsptjgo/ImportarProcessoVisualizar.jsp";
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
					break;
				case 3: // Resgata lista de arquivos inseridos e adiciona ao processo. Redireciona para confirmação cadastro processo
					if (listaArquivosInseridos != null && listaArquivosInseridos.size() > 0) {
						processoDt.setListaArquivos(listaArquivosInseridos);
						processoDt.setPasso1("Passo 1 OK");
						processoDt.setPasso2("Passo 2 OK");
						processoDt.setPasso3("Passo 3");
		
						stAcao = "/WEB-INF/jsptjgo/ImportarProcessoVisualizar.jsp";
						processoDt.setId_ArquivoTipo("");
						processoDt.setArquivoTipo("");
						processoDt.setId_Modelo("");
						processoDt.setModelo("");
						processoDt.setTextoEditor("");
		
						// Gera código do pedido, assim o submit so pode ser executado uma unica vez
						request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
					} else {
						request.setAttribute("MensagemErro", "É necessário inserir um arquivo para prosseguir.");
						stAcao = "/WEB-INF/jsptjgo/ImportarProcessoPeticionar.jsp";
					}
					break;
				default:
					processoDt.setPasso2("");
					processoDt.setPasso3("");
					break;
			}
	}

	request.setAttribute("PassoEditar", passoEditar);
	request.getSession().setAttribute("ProcessoCadastroDt", processoDt);
	request.getSession().setAttribute("Processone", Processone);

	RequestDispatcher dis = request.getRequestDispatcher(stAcao);
	dis.include(request, response);
}

	/**
	 * Limpa lista DWR e zera contador Arquivos
	 * 
	 * @param request
	 */
	protected void limparListaArquivos(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
	}

	/**
	 * Resgata lista de arquivos inseridos no passo 2 usando DWR Converte de Map
	 * para List
	 */
	protected List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		return Funcoes.converterMapParaList(mapArquivos);
	}

	/**
	 * Método que faz tratamentos necessários com parâmetros auxiliares no
	 * cadastro de processo
	 * @throws Exception 
	 */
	private void setParametrosAuxiliares(ProcessoCadastroDt processoDt, int paginaatual, HttpServletRequest request, int paginaAnterior, ProcessoNe Processone, UsuarioNe UsuarioSessao) throws Exception{

		// Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo do arquivo
		if (!processoDt.getId_Modelo().equals("") && request.getParameter("PaginaAtual") != null) {
			ModeloDt modeloDt = Processone.consultarModeloId(processoDt.getId_Modelo(), processoDt, UsuarioSessao.getUsuarioDt());
			processoDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			processoDt.setArquivoTipo(modeloDt.getArquivoTipo());
			processoDt.setTextoEditor(modeloDt.getTexto());
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Id_ArquivoTipo", processoDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", processoDt.getArquivoTipo());
		request.setAttribute("Modelo", processoDt.getModelo());
		request.setAttribute("TextoEditor", processoDt.getTextoEditor());
		request.setAttribute("PaginaAtual", Configuracao.Editar);

	}

	// Captura partes selecionadas pelo usuario
	protected void getPartesIntimadasDelegacia(HttpServletRequest request, ProcessoCadastroDt processoCriminaldt) {
		String[] partesIntimadas = request.getParameterValues("partesIntimadas");
		if (partesIntimadas != null) {
			List partesProcesso = processoCriminaldt.getPartesAtivasPassivas();
			for (int i = 0; i < partesProcesso.size(); i++) {
				ProcessoParteDt parte = (ProcessoParteDt) partesProcesso.get(i);
				for (int j = 0; j < partesIntimadas.length; j++) {
					if (partesIntimadas[j].equals(parte.getNome()))
						processoCriminaldt.addListaPartesIntimadas(parte);
				}
			}
		}
	}

}
