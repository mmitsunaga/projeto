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
import br.gov.go.tj.projudi.dt.UsuarioArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.UsuarioArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que gerencia os documentos de usuários
 * @author msapaula
 *
 */
public class UsuarioArquivoCt extends UsuarioArquivoCtGen {

    private static final long serialVersionUID = 1895834701437853260L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca,
			String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioArquivoDt UsuarioArquivodt;
		UsuarioArquivoNe UsuarioArquivone;
		

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
				
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/UsuarioArquivo.jsp";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma", "UsuarioArquivo");
		request.setAttribute("tempRetorno", "UsuarioArquivo");

		UsuarioArquivone = (UsuarioArquivoNe) request.getSession().getAttribute("UsuarioArquivone");
		if (UsuarioArquivone == null) UsuarioArquivone = new UsuarioArquivoNe();

		UsuarioArquivodt = (UsuarioArquivoDt) request.getSession().getAttribute("UsuarioArquivodt");
		if (UsuarioArquivodt == null) UsuarioArquivodt = new UsuarioArquivoDt();

		UsuarioArquivodt.setId_Usuario(request.getParameter("Id_Usuario"));
		UsuarioArquivodt.setUsuario(request.getParameter("Usuario"));
		UsuarioArquivodt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		UsuarioArquivodt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		UsuarioArquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		UsuarioArquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Novo:
				UsuarioArquivodt.limpar();
				UsuarioDt usuarioDt = (UsuarioDt) request.getSession().getAttribute("usuarioDt");
				if (usuarioDt != null && usuarioDt.getId().length() > 0) {
					UsuarioArquivodt.setUsuarioDt(usuarioDt);
					List arquivos = UsuarioArquivone.consultarArquivosUsuario(usuarioDt.getId(), UsuarioSessao);
					usuarioDt.setListaArquivosUsuario(arquivos);
					
					limparListaArquivos(request);
				} else request.setAttribute("MensagemErro", "Não foi possível obter Usuário selecionado");
				break;
	
			case Configuracao.Salvar:
				// Resgata lista de documentos inseridos
				List listaDocumentosInseridos = getListaArquivos(request);
				UsuarioArquivodt.setArquivosInseridos(listaDocumentosInseridos);
				request.setAttribute("Mensagem", "Clique para Confirmar a Inserção dos Documentos do Usuário.");
				break;
	
			//Salva documentos do usuário
			case Configuracao.SalvarResultado:
				Mensagem = UsuarioArquivone.Verificar(UsuarioArquivodt);
				if (Mensagem.length() == 0) {
					UsuarioArquivone.salvarDocumentosUsuario(UsuarioArquivodt);
					request.setAttribute("MensagemOk", "Documentos Salvos com Sucesso.");
					//Atualiza lista de documentos do usuário
					List arquivos = UsuarioArquivone.consultarArquivosUsuario(UsuarioArquivodt.getUsuarioDt().getId(), UsuarioSessao);
					UsuarioArquivodt.getUsuarioDt().setListaArquivosUsuario(arquivos);
					
					limparListaArquivos(request);
				} else request.setAttribute("MensagemErro", Mensagem);
				break;
	
			case Configuracao.Excluir:
				stId = request.getParameter("Id_UsuarioArquivo");
				String nomeArquivo = request.getParameter("NomeArquivo");
				if (stId != null && stId.length() > 0) {
					UsuarioArquivodt.setId(stId);
					request.setAttribute("Mensagem", "Clique para confirmar a exclusão do documento " + nomeArquivo
							+ ". Essa exclusão será registrada por motivos de segurança.");
				}
				break;
	
			case Configuracao.ExcluirResultado:
				if (UsuarioArquivodt.getId().length() > 0) {
					LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
					UsuarioArquivone.excluirDocumentoUsuario(UsuarioArquivodt.getId(), logDt);
					request.setAttribute("MensagemOk", "Documento Excluído com Sucesso.");
					//Atualiza lista de documentos do usuário
					List arquivos = UsuarioArquivone.consultarArquivosUsuario(UsuarioArquivodt.getUsuarioDt().getId(), UsuarioSessao);
					UsuarioArquivodt.getUsuarioDt().setListaArquivosUsuario(arquivos);
				}
				break;
	
			//Efetua download de arquivos
			case Configuracao.Curinga6:
				String id_UsuarioArquivo = request.getParameter("Id_UsuarioArquivo");
				if (UsuarioSessao.VerificarCodigoHash(id_UsuarioArquivo, request.getParameter("hash"))) {
					UsuarioArquivone.baixarArquivoUsuario(id_UsuarioArquivo, UsuarioSessao.getUsuarioDt(), response, new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()));
				} else throw new MensagemException("Acesso negado. executar()");
				return;
	
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"UsuarioArquivo"};
					String[] lisDescricao = {"UsuarioArquivo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_UsuarioArquivo");
					request.setAttribute("tempBuscaDescricao", "UsuarioArquivo");
					request.setAttribute("tempBuscaPrograma", "UsuarioArquivo");
					request.setAttribute("tempRetorno", "UsuarioArquivo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = UsuarioArquivone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
	
			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao", "ArquivoTipo");
					request.setAttribute("tempBuscaPrograma", "ArquivoTipo");
					request.setAttribute("tempRetorno", "UsuarioArquivo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = UsuarioArquivone.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default:
				stId = request.getParameter("Id_UsuarioArquivo");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(UsuarioArquivodt.getId())) {
					UsuarioArquivodt.limpar();
					UsuarioArquivodt = UsuarioArquivone.consultarId(stId);
				}
				break;
		}

		request.getSession().setAttribute("UsuarioArquivodt", UsuarioArquivodt);
		request.getSession().setAttribute("UsuarioArquivone", UsuarioArquivone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Limpa lista de Arquivos (DWR) e zera contador
	 * @param request
	 */
	private void limparListaArquivos(HttpServletRequest request) {
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
	}

	/**
	 * Resgata lista de documentos inseridos usando DWR
	 * Converte de Map para List
	 */
	private List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		return Funcoes.converterMapParaList(mapArquivos);
	}

}
