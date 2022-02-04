package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.ne.ProcessoStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoStatusCtGen extends Controle {

	private static final long serialVersionUID = 4185539055360664394L;

	public int Permissao() {
		return ProcessoStatusDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoStatusDt ProcessoStatusdt;
		ProcessoStatusNe ProcessoStatusne;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";
		String stAcao = "/WEB-INF/jsptjgo/ProcessoStatus.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------
		request.setAttribute("tempPrograma", "ProcessoStatus");

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		ProcessoStatusne = (ProcessoStatusNe) request.getSession().getAttribute("ProcessoStatusne");
		if (ProcessoStatusne == null) ProcessoStatusne = new ProcessoStatusNe();

		ProcessoStatusdt = (ProcessoStatusDt) request.getSession().getAttribute("ProcessoStatusdt");
		if (ProcessoStatusdt == null) ProcessoStatusdt = new ProcessoStatusDt();

		ProcessoStatusdt.setProcessoStatus(request.getParameter("ProcessoStatus"));
		ProcessoStatusdt.setProcessoStatusCodigo(request.getParameter("ProcessoStatusCodigo"));

		ProcessoStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoStatusdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				ProcessoStatusne.excluir(ProcessoStatusdt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("ProcessoStatus");
					descricao.add("ProcessoStatus");
					request.setAttribute("tempBuscaId", "Id_ProcessoStatus");
					request.setAttribute("tempBuscaDescricao", "ProcessoStatus");
					request.setAttribute("tempBuscaPrograma", "ProcessoStatus");
					request.setAttribute("tempRetorno", "ProcessoStatus");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "ProcessoStatus");
					stTemp = ProcessoStatusne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo:
				ProcessoStatusdt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = ProcessoStatusne.Verificar(ProcessoStatusdt);
				if (Mensagem.length() == 0) {
					ProcessoStatusne.salvar(ProcessoStatusdt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			// --------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcessoStatus");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoStatusdt.getId())) {
						ProcessoStatusdt.limpar();
						ProcessoStatusdt = ProcessoStatusne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoStatusdt", ProcessoStatusdt);
		request.getSession().setAttribute("ProcessoStatusne", ProcessoStatusne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
