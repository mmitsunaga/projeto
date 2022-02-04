package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.ne.RegiaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class RegiaoCt extends RegiaoCtGen {

	private static final long serialVersionUID = -2165204501947540659L;
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RegiaoDt regiaoDt;
		RegiaoNe regiaoNe;
		String mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/Regiao.jsp";
		
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------

		regiaoNe = (RegiaoNe) request.getSession().getAttribute("regiaoNe");
		if (regiaoNe == null)
			regiaoNe = new RegiaoNe();

		regiaoDt = (RegiaoDt) request.getSession().getAttribute("regiaoDt");
		if (regiaoDt == null)
			regiaoDt = new RegiaoDt();


		regiaoDt.setRegiao(request.getParameter("Regiao"));
		regiaoDt.setRegiaoCodigo(request.getParameter("RegiaoCodigo"));
		regiaoDt.setId_Comarca(request.getParameter("Id_Comarca"));
		regiaoDt.setComarca(request.getParameter("Comarca"));
		regiaoDt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));
		regiaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		regiaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("tempPrograma", "Regiao");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
		
		case Configuracao.ExcluirResultado: //conferido
			if(regiaoDt.getId() != null && regiaoDt.getId().length() > 0 ) {
				regiaoNe.excluir(regiaoDt);
				request.getSession().removeAttribute("regiaoDt");
				request.setAttribute("MensagemOk", "Região excluída com sucesso.");
			} else  {
				request.setAttribute("MensagemErro", "Para excluir uma região é necessário consultá-la antes" );
			}
			break;

		case Configuracao.Localizar: // conferido
			if (request.getParameter("Passo") == null) {
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"Região", "Comarca"};
				String[] lisDescricao = {"Região", "Comarca"};
				request.setAttribute("tempBuscaId", "Id_Regiao");
				request.setAttribute("tempBuscaDescricao", "Região");
				request.setAttribute("tempBuscaPrograma", "Região");
				request.setAttribute("tempRetorno", "Regiao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = regiaoNe.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
		case Configuracao.Novo: //conferido
			request.getSession().removeAttribute("regiaoDt");
			regiaoDt.limpar();
			break;
			
		case Configuracao.SalvarResultado: //conferido
			mensagem = regiaoNe.Verificar(regiaoDt);
			if (mensagem.length() == 0) mensagem = regiaoNe.VerificarCodigo(regiaoDt);
			if (mensagem.length() == 0) {
				regiaoNe.salvar(regiaoDt);
				request.setAttribute("MensagemOk", "Dados Salvos com sucesso");				
			} else {
				request.setAttribute("MensagemErro", mensagem);
			}
			break;
		
		// Consulta de comarcas
		case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): //conferido
			if (request.getParameter("Passo") == null) {
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String[] lisNomeBusca = {"Comarca" };
				String[] lisDescricao = {"Comarca" };
				request.setAttribute("tempBuscaId", "Id_Comarca");
				request.setAttribute("tempBuscaDescricao", "Comarca");
				request.setAttribute("tempBuscaPrograma", "Comarca");
				request.setAttribute("tempRetorno", "Regiao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = regiaoNe.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
									
				enviarJSON(response, stTemp);
								
				return;
			}
			break;
		
		default:
			stId = request.getParameter("Id_Regiao");
			if (stId != null && !stId.isEmpty())
				if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(regiaoDt.getId())) {
					regiaoDt.limpar();
					regiaoDt = regiaoNe.consultarId(stId);
				}
			break;
		}

		request.getSession().setAttribute("regiaoDt", regiaoDt);
		request.getSession().setAttribute("regiaoNe", regiaoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
