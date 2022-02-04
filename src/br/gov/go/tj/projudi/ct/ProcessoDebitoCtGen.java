package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDebitoDt;
import br.gov.go.tj.projudi.ne.ProcessoDebitoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoDebitoCtGen extends Controle {

	private static final long serialVersionUID = 8846827153456205939L;

	public int Permissao() {
		return ProcessoDebitoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDebitoDt ProcessoDebitodt;
		ProcessoDebitoNe ProcessoDebitone;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoDebito.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------

		ProcessoDebitone = (ProcessoDebitoNe) request.getSession().getAttribute("ProcessoDebitone");
		if (ProcessoDebitone == null) ProcessoDebitone = new ProcessoDebitoNe();

		ProcessoDebitodt = (ProcessoDebitoDt) request.getSession().getAttribute("ProcessoDebitodt");
		if (ProcessoDebitodt == null) ProcessoDebitodt = new ProcessoDebitoDt();

		ProcessoDebitodt.setProcessoDebito(request.getParameter("ProcessoDebito"));

		ProcessoDebitodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoDebitodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("tempPrograma", "ProcessoDebito");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				ProcessoDebitone.excluir(ProcessoDebitodt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("ProcessoDebito");
					descricao.add("ProcessoDebito");
					request.setAttribute("tempBuscaId", "Id_ProcessoDebito");
					request.setAttribute("tempBuscaDescricao", "ProcessoDebito");
					request.setAttribute("tempBuscaPrograma", "ProcessoDebito");
					request.setAttribute("tempRetorno", "ProcessoDebito");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "ProcessoDebito");
					stTemp = ProcessoDebitone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo:
				ProcessoDebitodt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = ProcessoDebitone.Verificar(ProcessoDebitodt);
				if (Mensagem.length() == 0) {
					ProcessoDebitone.salvar(ProcessoDebitodt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			default:
				stId = request.getParameter("Id_ProcessoDebito");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoDebitodt.getId())) {
						ProcessoDebitodt.limpar();
						ProcessoDebitodt = ProcessoDebitone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoDebitodt", ProcessoDebitodt);
		request.getSession().setAttribute("ProcessoDebitone", ProcessoDebitone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
