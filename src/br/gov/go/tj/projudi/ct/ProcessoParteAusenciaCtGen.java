package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoParteAusenciaDt;
import br.gov.go.tj.projudi.ne.ProcessoParteAusenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoParteAusenciaCtGen extends Controle {

	private static final long serialVersionUID = 1624443197822585033L;

	public int Permissao() {
		return ProcessoParteAusenciaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteAusenciaDt ProcessoParteAusenciadt;
		ProcessoParteAusenciaNe ProcessoParteAusenciane;
		List nomeBusca;
		List descricao;
		String stNomeBusca1 = "";
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoParteAusencia.jsp";

		// --------------------------------------------------------------------------
		// Faz o mapeamento do id e da descrição das buscas externas com as
		// variaveis locais.
		// Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		// cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e
		// outra
		// tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		// --------------------------------------------------------------------------

		ProcessoParteAusenciane = (ProcessoParteAusenciaNe) request.getSession().getAttribute("ProcessoParteAusenciane");
		if (ProcessoParteAusenciane == null) ProcessoParteAusenciane = new ProcessoParteAusenciaNe();

		ProcessoParteAusenciadt = (ProcessoParteAusenciaDt) request.getSession().getAttribute("ProcessoParteAusenciadt");
		if (ProcessoParteAusenciadt == null) ProcessoParteAusenciadt = new ProcessoParteAusenciaDt();

		ProcessoParteAusenciadt.setProcessoParteAusencia(request.getParameter("ProcessoParteAusencia"));
		ProcessoParteAusenciadt.setProcessoParteAusenciaCodigo(request.getParameter("ProcessoParteAusenciaCodigo"));

		ProcessoParteAusenciadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteAusenciadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("tempPrograma", "ProcessoParteAusencia");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: // Excluir
				ProcessoParteAusenciane.excluir(ProcessoParteAusenciadt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
	
			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: // localizar
				if (request.getParameter("Passo")==null){
					nomeBusca = new ArrayList();
					descricao = new ArrayList();
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					nomeBusca.add("ProcessoParteAusencia");
					descricao.add("ProcessoParteAusencia");
					request.setAttribute("tempBuscaId", "Id_ProcessoParteAusencia");
					request.setAttribute("tempBuscaDescricao", "ProcessoParteAusencia");
					request.setAttribute("tempBuscaPrograma", "ProcessoParteAusencia");
					request.setAttribute("tempRetorno", "ProcessoParteAusencia");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("nomeBusca", nomeBusca);
					request.setAttribute("descricao", descricao);
				}else{
					String stTemp = "";
					request.setAttribute("tempRetorno", "ProcessoParteAusencia");
					stTemp = ProcessoParteAusenciane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					try{
						enviarJSON(response, stTemp);
						
					}
					catch(Exception e) {}
					return;
				}
				break;
			case Configuracao.Novo:
				ProcessoParteAusenciadt.limpar();
				break;
			case Configuracao.SalvarResultado:
				Mensagem = ProcessoParteAusenciane.Verificar(ProcessoParteAusenciadt);
				if (Mensagem.length() == 0) {
					ProcessoParteAusenciane.salvar(ProcessoParteAusenciadt);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
				} else
					request.setAttribute("MensagemErro", Mensagem);
				break;
			default:
				stId = request.getParameter("Id_ProcessoParteAusencia");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoParteAusenciadt.getId())) {
						ProcessoParteAusenciadt.limpar();
						ProcessoParteAusenciadt = ProcessoParteAusenciane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoParteAusenciadt", ProcessoParteAusenciadt);
		request.getSession().setAttribute("ProcessoParteAusenciane", ProcessoParteAusenciane);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
