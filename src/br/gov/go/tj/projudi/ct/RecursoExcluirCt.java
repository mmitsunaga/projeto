package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoExcluirDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.ne.RecursoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

/**
 * Servlet que controla a exclusão de recursos.
 *  
 * @author mmgomes
 *
 */
public class RecursoExcluirCt extends Controle {

	/**
     * 
     */
    private static final long serialVersionUID = 2378840777156802255L;

    public int Permissao() {
		return RecursoParteDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RecursoDt Recursodt;
		RecursoExcluirDt RecursoExcluirdt;
		RecursoParteNe RecursoPartene;

		String stAcao = "/WEB-INF/jsptjgo/RecursoExcluir.jsp";
		request.setAttribute("tempPrograma", "RecursoExcluir");
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		RecursoPartene = (RecursoParteNe) request.getSession().getAttribute("RecursoPartene");
		if (RecursoPartene == null) RecursoPartene = new RecursoParteNe();

		Recursodt = (RecursoDt) request.getSession().getAttribute("Recursodt");
		if (Recursodt == null) Recursodt = new RecursoDt();
		
		RecursoExcluirdt = (RecursoExcluirDt) request.getSession().getAttribute("RecursoExcluirdt");
		if (RecursoExcluirdt == null) RecursoExcluirdt = new RecursoExcluirDt();

		ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt != null && processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) {
			Recursodt = processoDt.getRecursoDt();
		}

		//Captura a classe selecionada
		RecursoExcluirdt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		RecursoExcluirdt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		RecursoExcluirdt.setProcessoTipoCodigo(request.getParameter("ProcessoTipoCodigo"));
		RecursoExcluirdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		RecursoExcluirdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			//Inicializa dados
			case Configuracao.Novo:
				
				//Se foi passado Id_Recurso
				if (Recursodt != null && Recursodt.getId().length() > 0) {
					//Recupera dados de recurso que está no objeto Processo da sessão
					Recursodt = processoDt.getRecursoDt();					
					List<ProcessoTipoDt> dados = RecursoPartene.consultarProcessoTipoServentiaRecurso(stNomeBusca1, Recursodt.getId(), false);	
					if (dados == null || dados.size() <= 1) {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=Opção Disponível somente quando existir mais de um Recurso.");
						return;
					}					
					RecursoExcluirdt.Limpar();
				} else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=Opção Disponível somente para Recurso.");
					return;
				}
				break;

			case Configuracao.Salvar:
				request.setAttribute("Mensagem", "Clique para confirmar a exclusão do recurso");
				break;

			case Configuracao.SalvarResultado:
				String mensagem = RecursoPartene.verificarRemoverRecursoSegundoGrau(Recursodt, RecursoExcluirdt.getId_ProcessoTipo());
				if (mensagem.trim().length() == 0) {
					RecursoPartene.removerRecursoSegundoGrau(Recursodt, RecursoExcluirdt, UsuarioSessao.getUsuarioDt());
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Recurso excluído com sucesso.");
					return;
				} else {
					request.setAttribute("MensagemErro", mensagem);	
				}
				break;
				
			// Consultar tipos de processo do recurso
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Classe"};
					String[] lisDescricao = {"Classe", "Código"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String stPermissao = String.valueOf((ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					atribuirJSON(request, "Id_ProcessoTipo", "ProcessoTipo", "Classe", "RecursoExcluir", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
				} else {
					String stTemp = "";
					stTemp = RecursoPartene.consultarProcessoTipoServentiaRecursoJSON(stNomeBusca1, Recursodt.getId(), true, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);
					return;
				}			
			break;

		}

		request.getSession().setAttribute("Recursodt", Recursodt);
		request.getSession().setAttribute("RecursoExcluirdt", RecursoExcluirdt);
		request.getSession().setAttribute("RecursoPartene", RecursoPartene);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
