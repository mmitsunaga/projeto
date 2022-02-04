package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EscolaridadeDt;
import br.gov.go.tj.projudi.ne.EscolaridadeNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class EscolaridadeCtGen extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5202982563732872452L;

	public int Permissao() {
		return EscolaridadeDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EscolaridadeDt Escolaridadedt;
		EscolaridadeNe Escolaridadene;

		List nomeBusca = null;
		List descricao = null;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/Escolaridade.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "Escolaridade");

		Escolaridadene = (EscolaridadeNe) request.getSession().getAttribute("Escolaridadene");
		if (Escolaridadene == null)
			Escolaridadene = new EscolaridadeNe();

		Escolaridadedt = (EscolaridadeDt) request.getSession().getAttribute("Escolaridadedt");
		if (Escolaridadedt == null)
			Escolaridadedt = new EscolaridadeDt();

		Escolaridadedt.setEscolaridade(request.getParameter("Escolaridade"));
		Escolaridadedt.setEscolaridadeCodigo(request.getParameter("EscolaridadeCodigo"));

		Escolaridadedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Escolaridadedt.setIpComputadorLog(request.getRemoteAddr());

		if (request.getParameter("nomeBusca1") != null)	stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		// é a página padrão
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		// --------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				Escolaridadene.excluir(Escolaridadedt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo") == null) {
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("Escolaridade");
					descricao.add("Escolaridade");
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Escolaridade");
					request.setAttribute("tempBuscaDescricao", "Escolaridade");
					request.setAttribute("tempBuscaPrograma", "Escolaridade");
					request.setAttribute("tempRetorno", "Escolaridade");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp = "";
					request.setAttribute("tempRetorno", "Escolaridade");
					stTemp = Escolaridadene.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					} catch(Exception e) {
					}
					return;
				}
				break;
			case Configuracao.Novo:
				Escolaridadedt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = Escolaridadene.Verificar(Escolaridadedt);
				if (Mensagem.length() == 0) {
					Escolaridadene.salvar(Escolaridadedt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			// --------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Escolaridade");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Escolaridadedt.getId())) {
						Escolaridadedt.limpar();
						Escolaridadedt = Escolaridadene.consultarId(stId);
					}
				}
				break;
		}

		request.getSession().setAttribute("Escolaridadedt", Escolaridadedt);
		request.getSession().setAttribute("Escolaridadene", Escolaridadene);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
