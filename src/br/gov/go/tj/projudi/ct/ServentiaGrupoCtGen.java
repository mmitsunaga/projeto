package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.ServentiaGrupoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaGrupoCtGen extends Controle {

	private static final long serialVersionUID = -4166517000992555829L;

	public int Permissao() {
		return ServentiaGrupoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaGrupoDt ServentiaGrupodt;
		ServentiaGrupoNe ServentiaGrupone;

		List nomeBusca = null;
		List descricao = null;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

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

		if (request.getParameter("nomeBusca1") != null)	stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: 
				ServentiaGrupone.excluir(ServentiaGrupodt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: 
				if (request.getParameter("Passo") == null) {
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					nomeBusca.add("ServentiaGrupo");
					descricao.add("ServentiaGrupo");
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaGrupo");
					request.setAttribute("tempBuscaDescricao", "ServentiaGrupo");
					request.setAttribute("tempBuscaPrograma", "ServentiaGrupo");
					request.setAttribute("tempRetorno", "ServentiaGrupo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				} else {
					String stTemp = "";
					stTemp = ServentiaGrupone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {
					}
					return;
				}
				break;
			case Configuracao.Novo:
				ServentiaGrupodt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = ServentiaGrupone.Verificar(ServentiaGrupodt);
				if (Mensagem.length() == 0) {
					ServentiaGrupone.salvar(ServentiaGrupodt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			default:
				stId = request.getParameter("Id_ServentiaGrupo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ServentiaGrupodt.getId())) {
						ServentiaGrupodt.limpar();
						ServentiaGrupodt = ServentiaGrupone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ServentiaGrupodt", ServentiaGrupodt);
		request.getSession().setAttribute("ServentiaGrupone", ServentiaGrupone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
