package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet para controlar a emissão de logs do processo.
 * 
 * @author mmgomes
 */
public class ProcessoLogCt extends Controle {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2606004289556897554L;

	public int Permissao() {
		return 747;
	}

	public ProcessoLogCt() {
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDt processoDt;
		LogNe Logne;
		String ano = "";				
		String stAcao = "";// = "/WEB-INF/jsptjgo/ProcessoLog.jsp";
		List listaLogDt = null;
		List listaDePendenciasLogDt = null;
		
		List listaLiberacaoAcesso = null;
		List listaAcessoArquivo = null;
		
		Date data_Inicial = null;
		Date data_Final = null;
		
		request.setAttribute("tempPrograma", "Votos / Ementas");
		request.setAttribute("tempRetorno", "PreAnalisarVotoEmenta");
		request.setAttribute("PaginaAtual", paginaatual);
		
		Logne = (LogNe) request.getSession().getAttribute("Logne");
		
		if (Logne == null){
			Logne = new LogNe();
		}
				
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");			
		
		if (processoDt == null){
			throw new MensagemException("Não foi possível obter o ProcessoDt da sessão.");
		}
		
		SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy");
		
		request.setAttribute("data_Inicial", "");
		if (request.getParameter("data_Inicial") != null){
			data_Inicial = Funcoes.StringToDateTime(request.getParameter("data_Inicial"));
			request.setAttribute("data_Inicial", request.getParameter("data_Inicial"));
		}
		
		request.setAttribute("data_Final", "");
		if (request.getParameter("data_Final") != null){
			data_Final = Funcoes.StringToDateTime(request.getParameter("data_Final"));
			request.setAttribute("data_Final", request.getParameter("data_Final"));
		}
		
		switch (paginaatual) {
		
		case Configuracao.Curinga6:			
			
			stAcao = "/WEB-INF/jsptjgo/ProcessoLog.jsp";
				
			listaLogDt = (List<LogDt>) Logne.obterListaLog(processoDt.getId(), ano, "Processo");			
			
			if( (UsuarioSessao.getUsuarioDt().getId_ServentiaCargo() != "") && !(UsuarioSessao.getUsuarioDt().getId_ServentiaCargo().equalsIgnoreCase("null")) ){
				listaDePendenciasLogDt = (List<LogDt>) Logne.consultarLogPendencias(processoDt.getId(), UsuarioSessao.getUsuarioDt().getId_ServentiaCargo());			
			}
			
			break;
			
		case Configuracao.Curinga7:
			
			stAcao = "/WEB-INF/jsptjgo/ProcessoLogAcessoArquivo.jsp";
			
			if(data_Inicial != null && data_Final != null) {
								
				listaLiberacaoAcesso = Logne.consultarLogLiberacaoAcesso(processoDt.getId(), data_Inicial, data_Final);
				
				listaAcessoArquivo = Logne.consultarLogAcessoArquivo(processoDt.getId(), data_Inicial, data_Final);			
			}			
			break;			
		}
		
		request.setAttribute("listaLogDt", listaLogDt);		
		request.setAttribute("listaDePendenciasLogDt", listaDePendenciasLogDt);				
		request.setAttribute("AnoBuscaSelecionado", ano);
		request.setAttribute("listaLiberacaoAcesso", listaLiberacaoAcesso);
		request.setAttribute("listaAcessoArquivo", listaAcessoArquivo);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}	
	
	protected Date getPrimeiroDiaAno(String ano){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.valueOf(ano));
		calendar.set(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	protected Date getUltimoDiaAno(String ano){
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.valueOf(ano));
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
	    return calendar.getTime();
	}
}