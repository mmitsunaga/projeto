package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.ServentiaGrupoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaGrupoCt extends ServentiaGrupoCtGen {

	private static final long serialVersionUID = -3581848836385402942L;
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaGrupoDt ServentiaGrupodt;
		ServentiaGrupoNe ServentiaGrupone;

		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) {
			stNomeBusca1 = request.getParameter("nomeBusca1");
		}
		
		if(request.getParameter("nomeBusca2") != null) {
			stNomeBusca2 = request.getParameter("nomeBusca2");
		}
		
		//-fim controle de buscas ajax
		
		String stAcao = "/WEB-INF/jsptjgo/ServentiaGrupo.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "ServentiaGrupo");

		ServentiaGrupone = (ServentiaGrupoNe) request.getSession().getAttribute("ServentiaGrupone");
		if (ServentiaGrupone == null)
			ServentiaGrupone = new ServentiaGrupoNe();

		ServentiaGrupodt = (ServentiaGrupoDt) request.getSession().getAttribute("ServentiaGrupodt");
		if (ServentiaGrupodt == null)
			ServentiaGrupodt = new ServentiaGrupoDt();

		ServentiaGrupodt.setServentiaGrupo(request.getParameter("ServentiaGrupo"));
		ServentiaGrupodt.setAtividade(request.getParameter("Atividade"));
		ServentiaGrupodt.setId_Serventia(request.getParameter("Id_Serventia"));
		ServentiaGrupodt.setServentia(request.getParameter("Serventia"));
		ServentiaGrupodt.setId_ServentiaGrupoProximo(request.getParameter("Id_ServentiaGrupoProximo"));
		ServentiaGrupodt.setServentiaGrupoProximo(request.getParameter("ServentiaGrupoProximo"));
		ServentiaGrupodt.setAtividadeProxima(request.getParameter("AtividadeProxima"));
		ServentiaGrupodt.setEnviaMagistrado(new Boolean(request.getParameter("EnviaDesembargador")));

		ServentiaGrupodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaGrupodt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar: 
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Unidade de Trabalho", "Serventia"};
					String[] lisDescricao = {"Serventia", "Unidade de Trabalho"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaGrupo");
					request.setAttribute("tempBuscaDescricao", "ServentiaGrupo");
					request.setAttribute("tempBuscaPrograma", "ServentiaGrupo");
					request.setAttribute("tempRetorno", "ServentiaGrupo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ServentiaGrupone.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, UsuarioSessao.getGrupoCodigoToInt(), PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia", "Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "ServentiaGrupo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ServentiaGrupone.consultarDescricaoServentiaUnidaTrabalhoJSON(stNomeBusca1, UsuarioSessao.getGrupoCodigoToInt(), PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
																		
					return;
				}
				break;
			case (Configuracao.LocalizarDWR):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Unidade de Trabalho"};
					String[] lisDescricao = {"ServentiaGrupo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaGrupoProximo");
					request.setAttribute("tempBuscaDescricao", "ServentiaGrupoProximo");
					request.setAttribute("tempBuscaPrograma", "Próxima Função");
					request.setAttribute("tempRetorno", "ServentiaGrupo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.LocalizarDWR));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ServentiaGrupone.consultarDescricaoServentiaGrupoProximaFuncaoJSON(stNomeBusca1, PosicaoPaginaAtual, ServentiaGrupodt.getId(), ServentiaGrupodt.getId_Serventia());					
					enviarJSON(response, stTemp);						
					return;
				}
				break;
			default: 
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("ServentiaGrupodt", ServentiaGrupodt);
		request.getSession().setAttribute("ServentiaGrupone", ServentiaGrupone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
