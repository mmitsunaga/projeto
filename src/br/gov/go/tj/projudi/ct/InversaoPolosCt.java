package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.InversaoPolosDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.ne.InversaoPolosNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

/**
 * Servlet que controla a inversão de pólos de um processo / recurso.
 *  
 * @author Márcio Gomes
 *
 */
public class InversaoPolosCt extends Controle {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6286215942679861643L;

	public int Permissao() {
		return InversaoPolosDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		InversaoPolosDt InversaoPolosdt;
		InversaoPolosNe InversaoPolosne;

		String Mensagem = "";
		String stAcao = super.obtenhaAcaoJSP("InversaoPolos");
		request.setAttribute("tempPrograma", "InversaoPolos");

		InversaoPolosne = (InversaoPolosNe) request.getSession().getAttribute("InversaoPolosne");
		if (InversaoPolosne == null) InversaoPolosne = new InversaoPolosNe();

		InversaoPolosdt = (InversaoPolosDt) request.getSession().getAttribute("InversaoPolosdt");	
		if (InversaoPolosdt == null) InversaoPolosdt = new InversaoPolosDt();		
		
		InversaoPolosdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		InversaoPolosdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		//Captura partes selecionadas
		InversaoPolosdt.setListaRecorrentesInversaoPolos(getPartesRecorrentes(request, InversaoPolosdt));
		InversaoPolosdt.setListaRecorridosInversaoPolos(getPartesRecorridas(request, InversaoPolosdt));
		InversaoPolosdt.setListaPromoventesInversaoPolos(getPartesPromoventes(request, InversaoPolosdt));
		InversaoPolosdt.setListaPromovidosInversaoPolos(getPartesPromovidos(request, InversaoPolosdt));

		request.setAttribute("PaginaAnterior", paginaatual);
		super.limpeMensagens(request);		
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {			
			case Configuracao.Novo:
				InversaoPolosdt.limpar();
				InversaoPolosdt.setProcessoCompletoDt((ProcessoDt) request.getSession().getAttribute("processoDt"));
				InversaoPolosdt.setMovimentacaoProcessoDt((MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt"));
				break;

			case Configuracao.Salvar:
				super.exibaMensagemConfirmacao(request, "Clique para confirmar a inversão de pólos das partes selecionadas.");				
				break;

			case Configuracao.SalvarResultado:				
				Mensagem = InversaoPolosne.Verificar(InversaoPolosdt);
				if (Mensagem == null || Mensagem.length()==0) {		
					InversaoPolosne.InverterPolos(InversaoPolosdt, UsuarioSessao.getUsuarioDt());
					this.redirecionePaginaCapaProcesso(response, InversaoPolosdt.getProcessoCompletoDt().getId(), "Inversão de Pólos realizada com sucesso.");					
					InversaoPolosdt.limpar();
					request.getSession().removeAttribute("InversaoPolosdt");
					request.getSession().removeAttribute("InversaoPolosne");
					return;
				} 				
				request.setAttribute("MensagemErro", Mensagem);				
				break;
		}

		request.getSession().setAttribute("InversaoPolosdt", InversaoPolosdt);
		request.getSession().setAttribute("InversaoPolosne", InversaoPolosne);
		
		super.redirecione(stAcao, request, response);	
	}
	
	protected void redirecionePaginaCapaProcesso(HttpServletResponse response, String id_Processo, String mensagem) throws IOException
	{
		redireciona(response, "BuscaProcesso?Id_Processo=" + id_Processo + "&MensagemOk=" + mensagem);
	}

	/**
	 * Captura as partes recorrentes selecionadas pelo usuário
	 */
	protected List getPartesRecorrentes(HttpServletRequest request, InversaoPolosDt inversaoPolosdt) {		
		String[] recorrentes = request.getParameterValues("Recorrente");
		if (recorrentes != null && inversaoPolosdt.isRecurso()) 
			return montaListaRecursos(recorrentes, inversaoPolosdt.getRecursoDt().getListaRecorrentes());		
		return null;

	}

	/**
	 * Captura as partes recorridas selecionadas pelo usuário
	 */
	protected List getPartesRecorridas(HttpServletRequest request, InversaoPolosDt inversaoPolosdt) {		
		String[] recorridos = request.getParameterValues("Recorrido");
		if (recorridos != null && inversaoPolosdt.isRecurso()) 
			return montaListaRecursos(recorridos, inversaoPolosdt.getRecursoDt().getListaRecorridos());
		return null;
	}
	
	/**
	 * Monta a lista de partes do recurso selecionados
	 * 
	 */
	protected List montaListaRecursos(String[] recursosParte, List listaRecursosParte) {
		List partesSelecionadas = new ArrayList();
		
		for (int i = 0; i < recursosParte.length; i++) {			
			for (int j = 0; j < listaRecursosParte.size(); j++) {
				RecursoParteDt parteDt = (RecursoParteDt) listaRecursosParte.get(j);
				if (parteDt.getId().equals(recursosParte[i])) {					
					partesSelecionadas.add(parteDt);					
					break;
				}
			}
		}
		return partesSelecionadas;
	}
	
	/**
	 * Captura as partes promoventes selecionadas pelo usuário
	 */
	protected List getPartesPromoventes(HttpServletRequest request, InversaoPolosDt inversaoPolosdt) {		
		String[] promoventes = request.getParameterValues("Promovente");
		if (promoventes != null) 
			return montaListaPartes(promoventes, inversaoPolosdt.getProcessoCompletoDt().getListaPolosAtivos());		
		return null;

	}

	/**
	 * Captura as partes promovidas selecionadas pelo usuário
	 */
	protected List getPartesPromovidos(HttpServletRequest request, InversaoPolosDt inversaoPolosdt) {		
		String[] promovidos = request.getParameterValues("Promovido");
		if (promovidos != null) 
			return montaListaPartes(promovidos, inversaoPolosdt.getProcessoCompletoDt().getListaPolosPassivos());
		return null;
	}
	
	/**
	 * Monta a lista de partes do processo selecionados
	 * 
	 */
	private List montaListaPartes(String[] processosParte, List listaProcessosParte) {
		List partesSelecionadas = new ArrayList();
		
		for (int i = 0; i < processosParte.length; i++) {			
			for (int j = 0; j < listaProcessosParte.size(); j++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) listaProcessosParte.get(j);
				if (parteDt.getId().equals(processosParte[i])) {					
					partesSelecionadas.add(parteDt);					
					break;
				}
			}
		}
		return partesSelecionadas;
	}
}
