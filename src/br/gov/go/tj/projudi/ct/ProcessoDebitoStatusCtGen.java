package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.ne.ProcessoDebitoStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoDebitoStatusCtGen extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2480176841181606885L;

	public int Permissao() {
		return ProcessoDebitoStatusDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDebitoStatusDt ProcessoDebitoStatusdt;
		ProcessoDebitoStatusNe ProcessoDebitoStatusne;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoDebitoStatus.jsp";

		ProcessoDebitoStatusne = (ProcessoDebitoStatusNe) request.getSession().getAttribute("ProcessoDebitoStatusne");
		if (ProcessoDebitoStatusne == null) ProcessoDebitoStatusne = new ProcessoDebitoStatusNe();

		ProcessoDebitoStatusdt = (ProcessoDebitoStatusDt) request.getSession().getAttribute("ProcessoDebitoStatusdt");
		if (ProcessoDebitoStatusdt == null) ProcessoDebitoStatusdt = new ProcessoDebitoStatusDt();

		ProcessoDebitoStatusdt.setProcessoDebitoStatus(request.getParameter("ProcessoDebitoStatus"));

		ProcessoDebitoStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoDebitoStatusdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("tempPrograma", "ProcessoDebitoStatus");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				ProcessoDebitoStatusne.excluir(ProcessoDebitoStatusdt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("ProcessoDebitoStatus");
					descricao.add("ProcessoDebitoStatus");
					request.setAttribute("tempBuscaId", "Id_ProcessoDebitoStatus");
					request.setAttribute("tempBuscaDescricao", "ProcessoDebitoStatus");
					request.setAttribute("tempBuscaPrograma", "ProcessoDebitoStatus");
					request.setAttribute("tempRetorno", "ProcessoDebitoStatus");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "ProcessoDebitoStatus");
					stTemp = ProcessoDebitoStatusne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo:
				ProcessoDebitoStatusdt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = ProcessoDebitoStatusne.Verificar(ProcessoDebitoStatusdt);
				if (Mensagem.length() == 0) {
					ProcessoDebitoStatusne.salvar(ProcessoDebitoStatusdt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			default:
				stId = request.getParameter("Id_ProcessoDebitoStatus");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoDebitoStatusdt.getId())) {
						ProcessoDebitoStatusdt.limpar();
						ProcessoDebitoStatusdt = ProcessoDebitoStatusne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoDebitoStatusdt", ProcessoDebitoStatusdt);
		request.getSession().setAttribute("ProcessoDebitoStatusne", ProcessoDebitoStatusne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
