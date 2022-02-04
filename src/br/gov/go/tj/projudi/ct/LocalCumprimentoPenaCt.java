package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt;
import br.gov.go.tj.projudi.ne.LocalCumprimentoPenaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class LocalCumprimentoPenaCt extends LocalCumprimentoPenaCtGen {

	private static final long serialVersionUID = -6615730835453771266L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		LocalCumprimentoPenaDt LocalCumprimentoPenadt;
		LocalCumprimentoPenaNe LocalCumprimentoPenane;
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");		
		
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/LocalCumprimentoPena.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "LocalCumprimentoPena");

		LocalCumprimentoPenane = (LocalCumprimentoPenaNe) request.getSession().getAttribute("LocalCumprimentoPenane");
		if (LocalCumprimentoPenane == null)	LocalCumprimentoPenane = new LocalCumprimentoPenaNe();

		LocalCumprimentoPenadt = (LocalCumprimentoPenaDt) request.getSession().getAttribute("LocalCumprimentoPenadt");
		if (LocalCumprimentoPenadt == null)	LocalCumprimentoPenadt = new LocalCumprimentoPenaDt();

		LocalCumprimentoPenadt.setLocalCumprimentoPena(request.getParameter("LocalCumprimentoPena"));
		LocalCumprimentoPenadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		LocalCumprimentoPenadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		// Endereço do local
		LocalCumprimentoPenadt.getEnderecoLocal().setLogradouro(request.getParameter("Logradouro"));
		LocalCumprimentoPenadt.getEnderecoLocal().setNumero(request.getParameter("Numero"));
		LocalCumprimentoPenadt.getEnderecoLocal().setComplemento(request.getParameter("Complemento"));
		LocalCumprimentoPenadt.getEnderecoLocal().setId_Bairro(request.getParameter("Id_Bairro"));
		LocalCumprimentoPenadt.getEnderecoLocal().setBairro(request.getParameter("Bairro"));
		LocalCumprimentoPenadt.getEnderecoLocal().setId_Cidade(request.getParameter("BairroId_Cidade"));
		LocalCumprimentoPenadt.getEnderecoLocal().setCidade(request.getParameter("BairroCidade"));
		LocalCumprimentoPenadt.getEnderecoLocal().setUf(request.getParameter("BairroUf"));
		LocalCumprimentoPenadt.getEnderecoLocal().setCep(request.getParameter("Cep"));
		LocalCumprimentoPenadt.getEnderecoLocal().setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		LocalCumprimentoPenadt.getEnderecoLocal().setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());		

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		// é a página padrão
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				LocalCumprimentoPenane.excluir(LocalCumprimentoPenadt);
				request.setAttribute("MensagemOk", "Os dados foram excluídos.");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Local de Cumprimento da Pena"};
					String[] lisDescricao = {"Local de Cumprimento da Pena"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_LocalCumprimentoPena");
					request.setAttribute("tempBuscaDescricao", "LocalCumprimentoPena");
					request.setAttribute("tempBuscaPrograma", "LocalCumprimentoPena");
					request.setAttribute("tempRetorno", "LocalCumprimentoPena");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = LocalCumprimentoPenane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case Configuracao.Novo:
				LocalCumprimentoPenadt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = LocalCumprimentoPenane.Verificar(LocalCumprimentoPenadt);
				if (Mensagem.length() == 0) {
					LocalCumprimentoPenane.salvar(LocalCumprimentoPenadt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			// Consulta de Bairro - Usado no endereço da parte
			case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","Uf"};
					String[] lisDescricao = {"Bairro","Cidade","Uf"};
					String[] camposHidden = {"BairroCidade","BairroUf"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "LocalCumprimentoPena");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else{
					String stTemp = "";
					stTemp = LocalCumprimentoPenane.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			default:
				stId = request.getParameter("Id_LocalCumprimentoPena");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(LocalCumprimentoPenadt.getId())) {
						LocalCumprimentoPenadt.limpar();
						LocalCumprimentoPenadt = LocalCumprimentoPenane.consultarId(stId);
					}
				}
				break;
		}

		request.getSession().setAttribute("LocalCumprimentoPenadt", LocalCumprimentoPenadt);
		request.getSession().setAttribute("LocalCumprimentoPenane", LocalCumprimentoPenane);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	//

}
