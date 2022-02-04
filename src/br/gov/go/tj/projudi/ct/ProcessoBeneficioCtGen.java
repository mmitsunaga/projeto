package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoBeneficioDt;
import br.gov.go.tj.projudi.ne.ProcessoBeneficioNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoBeneficioCtGen extends Controle {

	private static final long serialVersionUID = -8126969345775643341L;

	public int Permissao() {
		return ProcessoBeneficioDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoBeneficioDt ProcessoBeneficiodt;
		ProcessoBeneficioNe ProcessoBeneficione;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoBeneficio.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------

		ProcessoBeneficione = (ProcessoBeneficioNe) request.getSession().getAttribute("ProcessoBeneficione");
		if (ProcessoBeneficione == null) ProcessoBeneficione = new ProcessoBeneficioNe();

		ProcessoBeneficiodt = (ProcessoBeneficioDt) request.getSession().getAttribute("ProcessoBeneficiodt");
		if (ProcessoBeneficiodt == null) ProcessoBeneficiodt = new ProcessoBeneficioDt();

		ProcessoBeneficiodt.setProcessoBeneficio(request.getParameter("ProcessoBeneficio"));
		ProcessoBeneficiodt.setPrazo(request.getParameter("Prazo"));
		ProcessoBeneficiodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoBeneficiodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("tempPrograma", "ProcessoBeneficio");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				ProcessoBeneficione.excluir(ProcessoBeneficiodt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("ProcessoBeneficio");
					descricao.add("ProcessoBeneficio");
					request.setAttribute("tempBuscaId", "Id_ProcessoBeneficio");
					request.setAttribute("tempBuscaDescricao", "ProcessoBeneficio");
					request.setAttribute("tempBuscaPrograma", "ProcessoBeneficio");
					request.setAttribute("tempRetorno", "ProcessoBeneficio");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "ProcessoBeneficio");
					stTemp = ProcessoBeneficione.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo:
				ProcessoBeneficiodt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = ProcessoBeneficione.Verificar(ProcessoBeneficiodt);
				if (Mensagem.length() == 0) {
					ProcessoBeneficione.salvar(ProcessoBeneficiodt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			default:
				stId = request.getParameter("Id_ProcessoBeneficio");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoBeneficiodt.getId())) {
						ProcessoBeneficiodt.limpar();
						ProcessoBeneficiodt = ProcessoBeneficione.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoBeneficiodt", ProcessoBeneficiodt);
		request.getSession().setAttribute("ProcessoBeneficione", ProcessoBeneficione);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
