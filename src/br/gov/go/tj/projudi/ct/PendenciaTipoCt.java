package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.ne.PendenciaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PendenciaTipoCt extends PendenciaTipoCtGen {

	private static final long serialVersionUID = -2648173648420305273L;

	public int Permissao() {
		return PendenciaTipoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PendenciaTipoDt pendenciaTipoDt;
		PendenciaTipoNe pendenciaTipoNe;

		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		List tempList = null;
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/PendenciaTipo.jsp";
		
		request.setAttribute("tempPrograma", "PendenciaTipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		pendenciaTipoNe = (PendenciaTipoNe) request.getSession().getAttribute("PendenciaTipone");
		if (pendenciaTipoNe == null) pendenciaTipoNe = new PendenciaTipoNe();

		pendenciaTipoDt = (PendenciaTipoDt) request.getSession().getAttribute("PendenciaTipodt");
		if (pendenciaTipoDt == null) pendenciaTipoDt = new PendenciaTipoDt();

		pendenciaTipoDt.setPendenciaTipo(request.getParameter("PendenciaTipo"));
		pendenciaTipoDt.setPendenciaTipoCodigo(request.getParameter("PendenciaTipoCodigo"));
		pendenciaTipoDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		pendenciaTipoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		pendenciaTipoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		pendenciaTipoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				pendenciaTipoNe.excluir(pendenciaTipoDt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				Integer tipoArquivo = new Integer(request.getParameter("tipoArquivo"));
				byte[] byTemp = pendenciaTipoNe.relPendenciaTipo(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , pendenciaTipoDt.getPendenciaTipo(), tipoArquivo, UsuarioSessao.getUsuarioDt().getNome());
				// Se o parâmetro tipo_Arquivo for setado e igual a 2, significa que o relatório deve ser um arquivo TXT. Algumas telas não tem esse parâmetro setado no request, logo é gerado um PDF.
				if (tipoArquivo != null && tipoArquivo.equals(2)) {								
					enviarTXT(response, byTemp, "Relatorio");					
				} else {					
					enviarPDF(response, byTemp, "Relatorio");
				}
																	
				return;
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PendenciaTipo"};
					String[] lisDescricao = {"PendenciaTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_PendenciaTipo");
					request.setAttribute("tempBuscaDescricao", "PendenciaTipo");
					request.setAttribute("tempBuscaPrograma", "PendenciaTipo");
					request.setAttribute("tempRetorno", "PendenciaTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					pendenciaTipoDt.limpar();
				}else{
					String stTemp = "";
					stTemp = pendenciaTipoNe.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case Configuracao.Novo:
				pendenciaTipoDt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = pendenciaTipoNe.Verificar(pendenciaTipoDt);
				if (Mensagem.length() == 0) {
					pendenciaTipoNe.salvar(pendenciaTipoDt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");
					request.setAttribute("tempRetorno","PendenciaTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = pendenciaTipoNe.consultarArquivoTipoDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case Configuracao.Curinga6:
				// Curinga de acesso à tela de listagem de PendenciaTipoDt
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				request.setAttribute("tempPrograma", "Listagem de Tipos de Pendência");
				stAcao = "WEB-INF/jsptjgo/ListagemPendenciaTipo.jsp";
				request.getSession().setAttribute("stAcaoRetorno", stAcao);
				pendenciaTipoDt.limpar();
				break;
			case Configuracao.Curinga7:
				// Curinga destinado à listagem de PendenciaTipodt
				if (request.getParameter("PendenciaTipo") != null) {
					if (request.getParameter("PendenciaTipo").equals("null")) {
						pendenciaTipoDt.setPendenciaTipo("");
					} else {
						pendenciaTipoDt.setPendenciaTipo(request.getParameter("PendenciaTipo"));
					}
				}
				stAcao = "/WEB-INF/jsptjgo/ListagemPendenciaTipo.jsp";
				tempList = pendenciaTipoNe.consultarDescricao(pendenciaTipoDt.getPendenciaTipo(), PosicaoPaginaAtual);
				if (tempList.size() > 0) {
					request.setAttribute("listaPendenciaTipo", tempList);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", pendenciaTipoNe.getQuantidadePaginas());
					request.setAttribute("PaginaAtual", Configuracao.Curinga7);
				} else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
				}
				request.setAttribute("tempPrograma", "Listagem de Tipos de Pendência");
				break;
			default:
				stId = request.getParameter("Id_PendenciaTipo");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(pendenciaTipoDt.getId())) {
						pendenciaTipoDt.limpar();
						pendenciaTipoDt = pendenciaTipoNe.consultarId(stId);
					}
				}
				break;
		}

		request.getSession().setAttribute("PendenciaTipodt", pendenciaTipoDt);
		request.getSession().setAttribute("PendenciaTipone", pendenciaTipoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
