package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.ne.CalculadoraTemposDatasNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CalculadoraTemposDatasCt extends Controle {


    /**
	 * 
	 */
	private static final long serialVersionUID = -4184993348622772610L;

	public  CalculadoraTemposDatasCt() {

	} 
		public int Permissao(){
			return ProcessoExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		String stAcao="/WEB-INF/jsptjgo/CalculadoraTemposDatas.jsp";

		switch (paginaatual) {
		  case Configuracao.LocalizarDWR: 
            
       	  int inFluxo = Funcoes.StringToInt( request.getParameter("fluxo").toString());
       	  String qtdeDias;
       	  String data;

       	  switch (inFluxo) {
            case 1:
            	data = request.getParameter("Data").toString();
            	qtdeDias = request.getParameter("QtdeDias").toString();
            	somaDias(response, data, qtdeDias);
                break;
            case 2:
            	String qtdeAno = request.getParameter("QtdeAno").toString();
            	String qtdeMes = request.getParameter("QtdeMes").toString();
            	qtdeDias = request.getParameter("QtdeDias").toString();
            	converterParaDias(response, qtdeAno, qtdeMes, qtdeDias);
                break;
            case 3:
            	qtdeDias = request.getParameter("QtdeDias").toString();
            	converterParaAnoMesDia(response, qtdeDias);
                break;
            case 4:
            	String data1 = request.getParameter("Data1").toString();
            	String data2 = request.getParameter("Data2").toString();
            	subtrairDatas(response, data1, data2);
                break;
            case 5:
            	String tempoTotal = request.getParameter("TempoTotal").toString();
            	String fracao = request.getParameter("Fracao").toString();
            	calcularTempoComutacao(response, tempoTotal, fracao);
                break;
            case 6:
            	String idProcesso = request.getParameter("IdProcesso").toString();
            	data = request.getParameter("Data").toString();
            	String tjSelecionado = request.getParameter("TjSelecionado").toString();
            	calcularTempoAteData(request, response, idProcesso, data, tjSelecionado);
                break;
          }		                	               	            
		  return;
		}
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	public void somaDias(HttpServletResponse response, String data, String qtdeDias) throws Exception{
		JSONObject oJson = new JSONObject();
		CalculadoraTemposDatasNe obNegocio = null;
		if (obNegocio == null )  obNegocio = new CalculadoraTemposDatasNe();
	    oJson.put("data", obNegocio.somaDias(data, qtdeDias));
                          
        enviarJSON(response, oJson.toString());
        
	}
	
	public void converterParaDias(HttpServletResponse response, String qtdeAno, String qtdeMes, String qtdeDias) throws Exception{
		String tempoDias = null; 
		CalculadoraTemposDatasNe obNegocio = null;
		if (obNegocio == null )  obNegocio = new CalculadoraTemposDatasNe();
		tempoDias = obNegocio.converterParaDias(qtdeAno, qtdeMes, qtdeDias);
        
		enviarJSON(response,tempoDias);
        
	}
	
	public void converterParaAnoMesDia(HttpServletResponse response, String qtdeDias) throws Exception{
		JSONObject oJson = new JSONObject();
		CalculadoraTemposDatasNe obNegocio = null;
		if (obNegocio == null )  obNegocio = new CalculadoraTemposDatasNe();
	    oJson.put("data", obNegocio.converterParaAnoMesDia(qtdeDias));
        
	    enviarJSON(response,oJson.toString());
        
	}
	
	public void subtrairDatas(HttpServletResponse response, String data1, String data2) throws Exception{
		String retorno = null; 
		CalculadoraTemposDatasNe obNegocio = null;
		if (obNegocio == null )  obNegocio = new CalculadoraTemposDatasNe();
		retorno = obNegocio.subtrairDatas(data1, data2);
	    
		enviarJSON(response,retorno);
        
    }
	
	public void calcularTempoComutacao(HttpServletResponse response, String tempoTotal, String fracao) throws Exception{
		JSONObject oJson = new JSONObject();

		CalculadoraTemposDatasNe obNegocio = null;
		if (obNegocio == null )  obNegocio = new CalculadoraTemposDatasNe();
		String dias = obNegocio.calcularTempoComutacao(tempoTotal, fracao);
		String anos = obNegocio.converterParaAnoMesDia(dias); 
	    oJson.put("dias", dias);
	    oJson.put("anos", anos);
        
	    enviarJSON(response,oJson.toString());
        
	}
	
	public void calcularTempoAteData(HttpServletRequest request, HttpServletResponse response, String idProcesso, String data, String tjSelecionado) throws Exception{
		String retorno = "";
		CalculadoraTemposDatasNe obNegocio = null;
		ProcessoEventoExecucaoDt dados = (ProcessoEventoExecucaoDt)request.getSession().getAttribute("ProcessoEventoExecucaodt");
		String[] listaTJ = tjSelecionado.split("-");
		int w = 0;
		//atualiza, no objeto, a lista de TJ selecionado na tela (request)
		for (int i=0; i<dados.getListaTJ().size(); i++){
			if (w < listaTJ.length){
				//verifica se a posição do TJ do objeto é a mesma posição contida na lista de TJ do request (TJ selecionados)
				if (i == Funcoes.StringToInt(listaTJ[w])){
					((HashMap)dados.getListaTJ().get(i)).put("Checked", "1");
					w++;
				} else {
					((HashMap)dados.getListaTJ().get(i)).put("Checked", "0");
				}
			} else {
				((HashMap)dados.getListaTJ().get(i)).put("Checked", "0");
			}
		}
		
		if (obNegocio == null )  obNegocio = new CalculadoraTemposDatasNe();
		retorno = obNegocio.calcularTempoAteDataJSON(idProcesso, data, dados.getListaTJ());
        
		enviarJSON(response,retorno);
        
	}
}
