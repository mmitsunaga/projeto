package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoMovimentacaoTipoClasseDt;
import br.gov.go.tj.projudi.ne.MovimentacaoTipoMovimentacaoTipoClasseNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class MovimentacaoTipoMovimentacaoTipoClasseCt extends MovimentacaoTipoMovimentacaoTipoClasseCtGen {

	private static final long serialVersionUID = -1727963863872060650L;

	public int Permissao() {
		return MovimentacaoTipoMovimentacaoTipoClasseDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		MovimentacaoTipoMovimentacaoTipoClasseDt MovimentacaoTipoMovimentacaoTipoClassedt;
		MovimentacaoTipoMovimentacaoTipoClasseNe MovimentacaoTipoMovimentacaoTipoClassene;
		
		String Mensagem = "";
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		String stAcao = "/WEB-INF/jsptjgo/MovimentacaoTipoMovimentacaoTipoClasse.jsp";

		request.setAttribute("tempPrograma", "MovimentacaoTipoMovimentacaoTipoClasse");
		request.setAttribute("ListaUlLiMovimentacaoTipoMovimentacaoTipoClasse", "");

		MovimentacaoTipoMovimentacaoTipoClassene = (MovimentacaoTipoMovimentacaoTipoClasseNe) request.getSession().getAttribute("MovimentacaoTipoMovimentacaoTipoClassene");
		if (MovimentacaoTipoMovimentacaoTipoClassene == null) MovimentacaoTipoMovimentacaoTipoClassene = new MovimentacaoTipoMovimentacaoTipoClasseNe();

		MovimentacaoTipoMovimentacaoTipoClassedt = (MovimentacaoTipoMovimentacaoTipoClasseDt) request.getSession().getAttribute("MovimentacaoTipoMovimentacaoTipoClassedt");
		if (MovimentacaoTipoMovimentacaoTipoClassedt == null) MovimentacaoTipoMovimentacaoTipoClassedt = new MovimentacaoTipoMovimentacaoTipoClasseDt();

		MovimentacaoTipoMovimentacaoTipoClassedt.setMovimentacaoTipoMovimentacaoTipoClasse(request.getParameter("MovimentacaoTipoMovimentacaoTipoClasse"));
		MovimentacaoTipoMovimentacaoTipoClassedt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));
		MovimentacaoTipoMovimentacaoTipoClassedt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		MovimentacaoTipoMovimentacaoTipoClassedt.setId_MovimentacaoTipoClasse(request.getParameter("Id_MovimentacaoTipoClasse"));
		MovimentacaoTipoMovimentacaoTipoClassedt.setMoviTipoClasse(request.getParameter("MovimentacaoTipoClasse"));

		MovimentacaoTipoMovimentacaoTipoClassedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		MovimentacaoTipoMovimentacaoTipoClassedt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		// é a página padrão
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		switch (paginaatual) {
		case Configuracao.Localizar: // localizar
			localizar(request, MovimentacaoTipoMovimentacaoTipoClassedt.getId_MovimentacaoTipoClasse(), MovimentacaoTipoMovimentacaoTipoClassene, UsuarioSessao);
			break;
		case Configuracao.Novo:
			MovimentacaoTipoMovimentacaoTipoClassedt.limpar();
			request.setAttribute("ListaUlLiMovimentacaoTipoMovimentacaoTipoClasse", "");
			break;
		case Configuracao.SalvarResultado:
			String[] idsDados = request.getParameterValues("chkEditar");
			Mensagem = MovimentacaoTipoMovimentacaoTipoClassene.Verificar(MovimentacaoTipoMovimentacaoTipoClassedt);
			if (Mensagem.length() == 0) {
				MovimentacaoTipoMovimentacaoTipoClassene.salvarMultiplo(MovimentacaoTipoMovimentacaoTipoClassedt, idsDados);
				localizar(request, MovimentacaoTipoMovimentacaoTipoClassedt.getId_MovimentacaoTipoClasse(), MovimentacaoTipoMovimentacaoTipoClassene, UsuarioSessao);
				request.setAttribute("ListaUlLiMovimentacaoTipoMovimentacaoTipoClasse", "");
				request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
			} else
				request.setAttribute("MensagemErro", Mensagem);
			break;
		case (MovimentacaoTipoClasseDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				//lisNomeBusca = new ArrayList();
				//lisDescricao = new ArrayList();
				String[] lisNomeBusca = {"MovimentacaoTipoClasse"};
				String[] lisDescricao = {"MovimentacaoTipoClasse"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_MovimentacaoTipoClasse");
				request.setAttribute("tempBuscaDescricao", "MovimentacaoTipoClasse");
				request.setAttribute("tempBuscaPrograma", "MovimentacaoTipoClasse");
				request.setAttribute("tempRetorno", "MovimentacaoTipoMovimentacaoTipoClasse");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
				request.setAttribute("PaginaAtual", (MovimentacaoTipoClasseDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = MovimentacaoTipoMovimentacaoTipoClassene.consultarDescricaoMovimentacaoTipoClasseJSON(stNomeBusca1, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
		break;
			default:
				break;
		}
		request.getSession().setAttribute("MovimentacaoTipoMovimentacaoTipoClassedt", MovimentacaoTipoMovimentacaoTipoClassedt);
		request.getSession().setAttribute("MovimentacaoTipoMovimentacaoTipoClassene", MovimentacaoTipoMovimentacaoTipoClassene);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, MovimentacaoTipoMovimentacaoTipoClasseNe objNe, UsuarioNe UsuarioSessao) throws Exception{
		String tempDados = objNe.consultarMovimentacaoTipoMovimentacaoTipoClasseUlLiCheckBox(id);
		if (tempDados.length() > 0) {
			request.setAttribute("ListaUlLiMovimentacaoTipoMovimentacaoTipoClasse", tempDados);
			// é gerado o código do pedido, assim o submit so pode ser executado
			// uma unica vez
			request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
		} else {
			request.setAttribute("MensagemErro", "Dados Não Localizados");
			request.setAttribute("PaginaAtual", Configuracao.Editar);
		}
	}

}
