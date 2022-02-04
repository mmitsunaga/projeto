package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.InterrupcaoTipoDt;
import br.gov.go.tj.projudi.ne.InterrupcaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class InterrupcaoTipoCt extends ProcessoTipoCtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8361388343813310469L;

	public int Permissao() {
		return InterrupcaoTipoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		InterrupcaoTipoDt interrupcaoTipoDt;
		InterrupcaoTipoNe interrupcaoTipoNe;
		String stNomeBusca1 = "";
		
		List tempList = null;
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/InterrupcaoTipo.jsp";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempPrograma", "InterrupcaoTipo");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		interrupcaoTipoNe = (InterrupcaoTipoNe) request.getSession().getAttribute("InterrupcaoTipone");
		if (interrupcaoTipoNe == null)	interrupcaoTipoNe = new InterrupcaoTipoNe();

		interrupcaoTipoDt = (InterrupcaoTipoDt) request.getSession().getAttribute("InterrupcaoTipodt");
		if (interrupcaoTipoDt == null)	interrupcaoTipoDt = new InterrupcaoTipoDt();

		interrupcaoTipoDt.setInterrupcaoTipo(request.getParameter("InterrupcaoTipo"));
		if (request.getParameter("InterrupcaoTotal") != null)
			interrupcaoTipoDt.setInterrupcaoTotal(request.getParameter("InterrupcaoTotal")); 
		else interrupcaoTipoDt.setInterrupcaoTotal("false");
		interrupcaoTipoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		interrupcaoTipoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado:
				Mensagem = interrupcaoTipoNe.VerificarExclusao(interrupcaoTipoDt);
				if (Mensagem.length() == 0) {
					interrupcaoTipoNe.excluir(interrupcaoTipoDt);
					request.setAttribute("MensagemOk", "Dados Excluidos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;				
	
			case Configuracao.Localizar:
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Tipo de Interrupção"};
					String[] lisDescricao = {"Tipo de Interrupção"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_InterrupcaoTipo");
					request.setAttribute("tempBuscaDescricao", "InterrupcaoTipo");
					request.setAttribute("tempBuscaPrograma", "InterrupcaoTipo");
					request.setAttribute("tempRetorno", "InterrupcaoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = interrupcaoTipoNe.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);
					return;
				}
				break;
			case Configuracao.Novo:
				interrupcaoTipoDt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = interrupcaoTipoNe.Verificar(interrupcaoTipoDt);
				if (Mensagem.length() == 0) {
					interrupcaoTipoNe.salvar(interrupcaoTipoDt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			default: {
				stId = request.getParameter("Id_InterrupcaoTipo");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(interrupcaoTipoDt.getId())) {
						interrupcaoTipoDt.limpar();
						interrupcaoTipoDt = interrupcaoTipoNe.consultarId(stId);
					}
				}
				break;
			}
		}

		request.getSession().setAttribute("InterrupcaoTipodt", interrupcaoTipoDt);
		request.getSession().setAttribute("InterrupcaoTipone", interrupcaoTipoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}