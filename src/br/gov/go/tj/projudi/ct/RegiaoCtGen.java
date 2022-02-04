package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.ne.RegiaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class RegiaoCtGen extends Controle {

	private static final long serialVersionUID = -8678610034865393977L;

	public int Permissao() {
		return RegiaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RegiaoDt Regiaodt;
		RegiaoNe Regiaone;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/Regiao.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------

		Regiaone = (RegiaoNe) request.getSession().getAttribute("Regiaone");
		if (Regiaone == null)
			Regiaone = new RegiaoNe();

		Regiaodt = (RegiaoDt) request.getSession().getAttribute("Regiaodt");
		if (Regiaodt == null)
			Regiaodt = new RegiaoDt();

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");

		Regiaodt.setRegiao(request.getParameter("Regiao"));
		Regiaodt.setRegiaoCodigo(request.getParameter("RegiaoCodigo"));
		Regiaodt.setId_Comarca(request.getParameter("Id_Comarca"));
		Regiaodt.setComarca(request.getParameter("Comarca"));
		Regiaodt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));
		Regiaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Regiaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("tempPrograma", "Regiao");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
		case Configuracao.ExcluirResultado: // Excluir
			Regiaone.excluir(Regiaodt);
			request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
			break;

		case Configuracao.Imprimir:
			break;
		case Configuracao.Localizar: // localizar
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
				stTemp = Regiaone.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
				
				try{
					enviarJSON(response, stTemp);
					
				} catch(Exception e) {}
				return;
			}
			break;
		case Configuracao.Novo:
			Regiaodt.limpar();
			break;
		case Configuracao.SalvarResultado:
			Mensagem = Regiaone.Verificar(Regiaodt);
			if (Mensagem.length() == 0) {
				Regiaone.salvar(Regiaodt);
				request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
			} else
				request.setAttribute("MensagemErro", Mensagem);
			break;
		case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				nomeBusca = new ArrayList();
				descricao = new ArrayList();
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				nomeBusca.add("Comarca");
				descricao.add("Comarca");
				request.setAttribute("tempBuscaId", "Id_Comarca");
				request.setAttribute("tempBuscaDescricao", "Comarca");
				request.setAttribute("tempBuscaPrograma", "Comarca");
				request.setAttribute("tempRetorno", "Regiao");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("nomeBusca", nomeBusca);
				request.setAttribute("descricao", descricao);
			} else {
				String stTemp = "";
				request.setAttribute("tempRetorno", "Regiao");
				stTemp = Regiaone.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
				
				try{
					enviarJSON(response, stTemp);
					
				} catch(Exception e) {}
				return;
			}
			break;
		default:
			stId = request.getParameter("Id_Regiao");
			if (stId != null && !stId.isEmpty())
				if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Regiaodt.getId())) {
					Regiaodt.limpar();
					Regiaodt = Regiaone.consultarId(stId);
				}
			break;
		}

		request.getSession().setAttribute("Regiaodt", Regiaodt);
		request.getSession().setAttribute("Regiaone", Regiaone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
