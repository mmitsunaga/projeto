package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.ne.ProcessoFaseNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoFaseCtGen extends Controle {

	private static final long serialVersionUID = -6805026989594345488L;

	public int Permissao() {
		return ProcessoFaseDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoFaseDt ProcessoFasedt;
		ProcessoFaseNe ProcessoFasene;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoFase.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		
		ProcessoFasene = (ProcessoFaseNe) request.getSession().getAttribute("ProcessoFasene");
		if (ProcessoFasene == null) ProcessoFasene = new ProcessoFaseNe();

		ProcessoFasedt = (ProcessoFaseDt) request.getSession().getAttribute("ProcessoFasedt");
		if (ProcessoFasedt == null) ProcessoFasedt = new ProcessoFaseDt();

		ProcessoFasedt.setProcessoFase(request.getParameter("ProcessoFase"));
		ProcessoFasedt.setProcessoFaseCodigo(request.getParameter("ProcessoFaseCodigo"));

		ProcessoFasedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoFasedt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("tempPrograma", "ProcessoFase");
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				ProcessoFasene.excluir(ProcessoFasedt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("ProcessoFase");
					descricao.add("ProcessoFase");
					request.setAttribute("tempBuscaId", "Id_ProcessoFase");
					request.setAttribute("tempBuscaDescricao", "ProcessoFase");
					request.setAttribute("tempBuscaPrograma", "ProcessoFase");
					request.setAttribute("tempRetorno", "ProcessoFase");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "ProcessoFase");
					stTemp = ProcessoFasene.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo:
				ProcessoFasedt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = ProcessoFasene.Verificar(ProcessoFasedt);
				if (Mensagem.length() == 0) {
					ProcessoFasene.salvar(ProcessoFasedt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			default:
				stId = request.getParameter("Id_ProcessoFase");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoFasedt.getId())) {
						ProcessoFasedt.limpar();
						ProcessoFasedt = ProcessoFasene.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoFasedt", ProcessoFasedt);
		request.getSession().setAttribute("ProcessoFasene", ProcessoFasene);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
