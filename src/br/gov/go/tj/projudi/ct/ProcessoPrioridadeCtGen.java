package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.ne.ProcessoPrioridadeNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoPrioridadeCtGen extends Controle {

	private static final long serialVersionUID = 1802933942113711447L;

	public int Permissao() {
		return ProcessoPrioridadeDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoPrioridadeDt ProcessoPrioridadedt;
		ProcessoPrioridadeNe ProcessoPrioridadene;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/ProcessoPrioridade.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		ProcessoPrioridadene = (ProcessoPrioridadeNe) request.getSession().getAttribute("ProcessoPrioridadene");
		if (ProcessoPrioridadene == null) ProcessoPrioridadene = new ProcessoPrioridadeNe();

		ProcessoPrioridadedt = (ProcessoPrioridadeDt) request.getSession().getAttribute("ProcessoPrioridadedt");
		if (ProcessoPrioridadedt == null) ProcessoPrioridadedt = new ProcessoPrioridadeDt();

		ProcessoPrioridadedt.setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
		ProcessoPrioridadedt.setProcessoPrioridadeCodigo(request.getParameter("ProcessoPrioridadeCodigo"));
		ProcessoPrioridadedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoPrioridadedt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("tempPrograma", "ProcessoPrioridade");
		request.setAttribute("tempBuscaId_ProcessoPrioridade", "Id_ProcessoPrioridade");
		request.setAttribute("tempBuscaProcessoPrioridade", "ProcessoPrioridade");
		request.setAttribute("tempRetorno", "ProcessoPrioridade");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ProcessoPrioridadene.excluir(ProcessoPrioridadedt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("ProcessoPrioridade");
					descricao.add("ProcessoPrioridade");
					request.setAttribute("tempBuscaId", "Id_ProcessoPrioridade");
					request.setAttribute("tempBuscaDescricao", "ProcessoPrioridade");
					request.setAttribute("tempBuscaPrograma", "ProcessoPrioridade");
					request.setAttribute("tempRetorno", "ProcessoPrioridade");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "ProcessoPrioridade");
					stTemp = ProcessoPrioridadene.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo:
				ProcessoPrioridadedt.limpar();
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem = ProcessoPrioridadene.Verificar(ProcessoPrioridadedt);
				if (Mensagem.length() == 0) {
					ProcessoPrioridadene.salvar(ProcessoPrioridadedt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_ProcessoPrioridade");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoPrioridadedt.getId())) {
						ProcessoPrioridadedt.limpar();
						ProcessoPrioridadedt = ProcessoPrioridadene.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoPrioridadedt", ProcessoPrioridadedt);
		request.getSession().setAttribute("ProcessoPrioridadene", ProcessoPrioridadene);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
