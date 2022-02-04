package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.ne.ForumNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ForumCt extends ForumCtGen {

	private static final long serialVersionUID = 8602919802717971040L;

	public int Permissao() {
		return ForumDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ForumDt Forumdt;
		ForumNe Forumne;

		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		List tempList = null;
		String mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/Forum.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "Forum");
		request.setAttribute("tempRetorno", "Forum");
		
		Forumne = (ForumNe) request.getSession().getAttribute("Forumne");
		if (Forumne == null) Forumne = new ForumNe();

		Forumdt = (ForumDt) request.getSession().getAttribute("Forumdt");
		if (Forumdt == null) Forumdt = new ForumDt();

		Forumdt.setForum(request.getParameter("Forum"));
		Forumdt.setForumCodigo(request.getParameter("ForumCodigo"));
		Forumdt.setId_Comarca(request.getParameter("Id_Comarca"));
		Forumdt.setComarca(request.getParameter("Comarca"));
		Forumdt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));
		Forumdt.getEnderecoForum().setId(request.getParameter("Id_Endereco"));
		Forumdt.getEnderecoForum().setLogradouro(request.getParameter("Logradouro"));
		Forumdt.getEnderecoForum().setNumero(request.getParameter("Numero"));
		Forumdt.getEnderecoForum().setComplemento(request.getParameter("Complemento"));
		Forumdt.getEnderecoForum().setId_Bairro(request.getParameter("Id_Bairro"));
		Forumdt.getEnderecoForum().setBairro(request.getParameter("Bairro"));
		Forumdt.getEnderecoForum().setId_Cidade(request.getParameter("BairroId_Cidade"));
		Forumdt.getEnderecoForum().setCidade(request.getParameter("BairroCidade"));
		Forumdt.getEnderecoForum().setUf(request.getParameter("BairroUf"));
		Forumdt.getEnderecoForum().setCep(request.getParameter("Cep"));
		Forumdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Forumdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				Forumne.excluir(Forumdt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Forum"};
					String[] lisDescricao = {"Forum"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Forum");
					request.setAttribute("tempBuscaDescricao","Forum");
					request.setAttribute("tempBuscaPrograma","Forum");			
					request.setAttribute("tempRetorno","Forum");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Forumne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			case Configuracao.Novo:
				Forumdt.limpar();
				break;
			case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Bairro","Cidade","UF"};
					String[] lisDescricao = {"Bairro","Cidade","UF"};
					String[] camposHidden = {"BairroCidade","BairroUf"};
					request.setAttribute("camposHidden",camposHidden);
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "Forum");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else{
					String stTemp = "";
					stTemp = Forumne.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case Configuracao.SalvarResultado:
				mensagem = Forumne.Verificar(Forumdt);
				if (mensagem == null || mensagem.length() == 0) {
					Forumne.salvar(Forumdt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", mensagem);
				break;
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","Forum");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Forumne.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
			break;
			case (EnderecoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				request.setAttribute("tempBuscaId_Endereco", "Id_Endereco");
				request.setAttribute("tempBuscaEndereco", "Endereco");
				request.setAttribute("tempRetorno", "Forum");
				stAcao = "/WEB-INF/jsptjgo/EnderecoLocalizar.jsp";
				tempList = Forumne.consultarDescricaoEndereco(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size() > 0) {
					request.setAttribute("ListaEndereco", tempList);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Forumne.getQuantidadePaginas());
				} else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			default:
				stId = request.getParameter("Id_Forum");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Forumdt.getId())) {
						Forumdt.limpar();
						Forumdt = Forumne.consultarId(stId);
					}
				}
				break;
		}

		request.getSession().setAttribute("Forumdt", Forumdt);
		request.getSession().setAttribute("Forumne", Forumne);
		request.getSession().setAttribute("Enderecodt", Forumdt.getEnderecoForum());

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}