package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.SinalDt;
import br.gov.go.tj.projudi.ne.SinalNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class SinalCtGen extends Controle {

	private static final long serialVersionUID = 254584980090049590L;

	public int Permissao() {
		return SinalDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		SinalDt Sinaldt;
		SinalNe Sinalne;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/Sinal.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "Sinal");
		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");

		Sinalne = (SinalNe) request.getSession().getAttribute("Sinalne");
		if (Sinalne == null)
			Sinalne = new SinalNe();

		Sinaldt = (SinalDt) request.getSession().getAttribute("Sinaldt");
		if (Sinaldt == null)
			Sinaldt = new SinalDt();

		Sinaldt.setSinal(request.getParameter("Sinal"));

		Sinaldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Sinaldt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		// é a página padrão
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		// --------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				Sinalne.excluir(Sinaldt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo") == null) {
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("Sinal");
					descricao.add("Sinal");
					request.setAttribute("tempBuscaId", "Id_Sinal");
					request.setAttribute("tempBuscaDescricao", "Sinal");
					request.setAttribute("tempBuscaPrograma", "Sinal");
					request.setAttribute("tempRetorno", "Sinal");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp = "";
					request.setAttribute("tempRetorno", "Serventia");
					stTemp = Sinalne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					} catch(Exception e) {
					}
					return;
				}
				break;
			case Configuracao.Novo:
				Sinaldt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = Sinalne.Verificar(Sinaldt);
				if (Mensagem.length() == 0) {
					Sinalne.salvar(Sinaldt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			// --------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Sinal");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Sinaldt.getId())) {
						Sinaldt.limpar();
						Sinaldt = Sinalne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Sinaldt", Sinaldt);
		request.getSession().setAttribute("Sinalne", Sinalne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
