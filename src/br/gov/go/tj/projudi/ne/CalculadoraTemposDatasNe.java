package br.gov.go.tj.projudi.ne;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.utils.Funcoes;

import org.json.JSONArray;
import org.json.JSONObject;

//---------------------------------------------------------
public class CalculadoraTemposDatasNe extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -123203463692870448L;

	/**
	 * Soma uma data à uma quantidade de dias
	 * @param data: data a ser somada
	 * @param qtdeDias: qtde de dias a ser somado à data
	 * @return: retorna a data final.
	 * @throws Exception
	 */
	public String somaDias(String data, String qtdeDias) throws Exception{
		String retorno = "";
			retorno = Funcoes.somaData(data, Funcoes.StringToInt(qtdeDias.trim()));
		
		return retorno;
	}
	
	public int converterParaDias(String anoMesDia) throws Exception{
		anoMesDia = anoMesDia.trim();
		return Funcoes.StringToInt(converterParaDias(anoMesDia.substring(0,2), anoMesDia.substring(5,7), anoMesDia.substring(10,12)));
	}
	
	/**
	 * Converte em dias
	 * @param qtdeAno
	 * @param qtdeMes
	 * @param qtdeDias
	 * @return: retorna a quantidade total de dias
	 * @throws Exception
	 */
	public String converterParaDias(String qtdeAno, String qtdeMes, String qtdeDias) throws Exception{
		String dias = "";
			dias = Funcoes.converterParaDias(qtdeAno.trim(), qtdeMes.trim(), qtdeDias.trim());
		
		return dias;
	}
	
	/**
	 * Converte em ano-mês-dia
	 * @param qtdeDias: qtde de dias a ser convertida
	 * @return: retorna o tempo em ano-mês-dia
	 * @throws Exception
	 */
	public String converterParaAnoMesDia(String qtdeDias) throws Exception{
		String anos = "";
		
		if (qtdeDias == null) qtdeDias = "0";
		anos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(qtdeDias.trim()));
		
		return anos;
	}
	/**
	 * subtrai duas datas e retorna a quantidade de dias
	 * @param dataFim: data informada (data1)
	 * @param dataInicio: data informada a ser subtraída (data2)
	 * @return
	 * @throws Exception
	 */
	public String subtrairDatas(String dataFim, String dataInicio) throws Exception{
		String qtdeDias = "";
			qtdeDias = String.valueOf(Funcoes.calculaDiferencaEntreDatas(dataInicio, dataFim));
		
		return qtdeDias;
	}
	
	/**
	 * Calcula o Tempo a ser Comutado
	 * @param tempoTotal
	 * @param fracao
	 * @return
	 * @throws Exception
    */
	public String calcularTempoComutacao(String tempoTotal, String fracao) throws Exception{
		int tempoComutacao = 0;
		
	   if (fracao.length()>0 && tempoTotal.length() > 0){
		   fracao.trim();
		   tempoComutacao = Funcoes.StringToInt(tempoTotal.trim()) * Funcoes.StringToInt(fracao.substring(0,1)) / Funcoes.StringToInt(fracao.substring(2));
	   }
		
	   return String.valueOf(tempoComutacao);
	}
	
	/**
	 * Calcula o tempo de condenação, restante da pena e tempo cumprido até data (em dias e anos)
	 * @return: array 
	 * @param data: data referência para o cálculo (data do decreto selecionado).
	 * @param idProcesso: identificação do processo
	 * @param listaTJ: lista dos TJ selecionados.
	 */
	public ArrayList calcularTempoAteData(String idProcesso, String data, List listaTJ) throws Exception{
		ArrayList array = new ArrayList();
		
		if (idProcesso.length() > 0 && data.length() > 0){
			ProcessoEventoExecucaoNe peeNe = new ProcessoEventoExecucaoNe();
			
			//-----monta a lista de eventos------------
			List listaEvento = peeNe.listarEventos(idProcesso,"");
			
			//separa a lista de eventos e a lista de histórico de cumprimento de pena restritiva de direito (armazena no HashMap)
			HashMap maplista_Evento_Historico = peeNe.separarLista_Evento_HistoricoPRD(listaEvento, null);
			listaEvento = (List)maplista_Evento_Historico.get("listaEventos");

			peeNe.calcularDataFim(listaEvento);
			peeNe.montarListaEventos(listaEvento, null, null, idProcesso, 12); //preenche a lista de condenações extintas
			
			// Prepara Lista para o cálculo, setando o atributo temporário com o valor cumprido de cada evento
			for (int i = 0; i < listaEvento.size(); i++) {
				ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
				evento.setTempoCumpridoCalculadoDias(Funcoes.StringToInt(evento.getTempoCumpridoDias()));				
			}
			
			//----------------------------------------
			
			//lista as condenações não extinta e calcula a pena remanescente
			List listaCondenacaoNaoExtinta = peeNe.calcularPenaRemanescente(idProcesso, listaEvento);
			
			List listaCondenacaoSelecionada = new ArrayList();
			
			//calcula o tempo total de condenação dos TJ's selecionados
			int tempoCondenacaoDias = 0;
			for (CondenacaoExecucaoDt condenacaoNaoExtinta: (List<CondenacaoExecucaoDt>)listaCondenacaoNaoExtinta) {
				for (HashMap condenacaoMap : (List<HashMap>)listaTJ) {
					if (condenacaoNaoExtinta.getId_ProcessoExecucao().equals(condenacaoMap.get("Id_ProcessoExecucao"))
							&& condenacaoMap.get("Checked").equals("1")){
						tempoCondenacaoDias += Funcoes.StringToInt(condenacaoNaoExtinta.getTempoPenaRemanescenteEmDias());
						listaCondenacaoSelecionada.add(condenacaoNaoExtinta);
					}
				}
			}
			String tempoCondenacaoAnos = Funcoes.converterParaAnoMesDia(tempoCondenacaoDias);
			
			//calcula o restante da pena dos TJ's selecionados até data do decreto
			List listaCondenacaoOrdenada = peeNe.ordenarCrimeMaisGrave_ateDataBase(listaCondenacaoSelecionada, listaEvento, data, true); //ordena os crimes dos TJ selecionados
			int tempoCumpridoAteDataDias = peeNe.calcularTempoCumpridoAteDataInicioEvento(listaEvento, data); //calcula o tempo cumprido total até data do decreto
			int tempoCondenacaoDiasCrimesNaoHediondos = peeNe.calcularTempoCondenacaoCrimesNaoHediondos(listaCondenacaoSelecionada);//Calcula o tempo das condenações dos crimes não hediondos
			int tempoCumpridoAteDataDiasCrimesNaoHediondos = peeNe.calcularTempoCumpridoCrimesNaoHediondos(listaEvento, listaCondenacaoSelecionada, data); //calcula o tempo cumprido total até data do decreto
			int tempoRestanteAteDataDiasCrimesNaoHediondos = tempoCondenacaoDiasCrimesNaoHediondos-tempoCumpridoAteDataDiasCrimesNaoHediondos;
			String tempoRestanteAnosCrimesNaoHediondos = Funcoes.converterParaAnoMesDia(tempoRestanteAteDataDiasCrimesNaoHediondos);
			int tempoRestanteTotalDias = 0;
			
			if (listaCondenacaoOrdenada != null && listaCondenacaoOrdenada.size() > 0 && tempoCumpridoAteDataDias > 0){
				for (int i=0; i < listaCondenacaoOrdenada.size(); i++) {
	                listaCondenacaoOrdenada.get(i);

	                HashMap map = peeNe.calcularRestantePenaCrime((CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(i), data, listaEvento, tempoCumpridoAteDataDias, i, "1"); 
	                int restantePenaCrime = Funcoes.StringToInt(map.get("restantePenaCrime").toString());
	                tempoCumpridoAteDataDias = Funcoes.StringToInt(map.get("tempoCumpridoTotal").toString());

	                tempoRestanteTotalDias += restantePenaCrime;
	            }

			} else {
				tempoRestanteTotalDias = tempoCondenacaoDias;
			}
			String restantePenaAnos = Funcoes.converterParaAnoMesDia(tempoRestanteTotalDias);
			
			//calcula o o tempo cumprido dos TJ's selecionados até data do decreto
			int tempoCumpridoDias = tempoCondenacaoDias - tempoRestanteTotalDias;
			String tempoCumpridoAnos = Funcoes.converterParaAnoMesDia(tempoCumpridoDias);
			
			array.add(String.valueOf(tempoCondenacaoDias));
			array.add(tempoCondenacaoAnos);
			array.add(String.valueOf(tempoCumpridoDias));
			array.add(tempoCumpridoAnos);
			array.add(String.valueOf(tempoRestanteTotalDias));
			array.add(restantePenaAnos);
			array.add(String.valueOf(tempoRestanteAteDataDiasCrimesNaoHediondos));
			array.add(String.valueOf(tempoRestanteAnosCrimesNaoHediondos));
			
		}
		
		
		return array;
	}

	public String calcularTempoAteDataJSON(String idProcesso, String data, List listaTJ) throws Exception{
		JSONObject oJson = new JSONObject();
		
		if (idProcesso.length() > 0 && data.length() > 0){
			ProcessoEventoExecucaoNe peeNe = new ProcessoEventoExecucaoNe();
			
			//-----monta a lista de eventos------------
			List listaEvento = peeNe.listarEventos(idProcesso,"");
			
			//separa a lista de eventos e a lista de histórico de cumprimento de pena restritiva de direito (armazena no HashMap)
			HashMap maplista_Evento_Historico = peeNe.separarLista_Evento_HistoricoPRD(listaEvento, null);
			listaEvento = (List)maplista_Evento_Historico.get("listaEventos");

			peeNe.calcularDataFim(listaEvento);
			peeNe.montarListaEventos(listaEvento, null, null, idProcesso, 12); //preenche a lista de condenações extintas
			
			// Prepara Lista para o cálculo, setando o atributo temporário com o valor cumprido de cada evento
			for (int i = 0; i < listaEvento.size(); i++) {
				ProcessoEventoExecucaoDt evento = (ProcessoEventoExecucaoDt) listaEvento.get(i);
				evento.setTempoCumpridoCalculadoDias(Funcoes.StringToInt(evento.getTempoCumpridoDias()));				
			}
			
			//----------------------------------------
			
			//lista as condenações não extinta e calcula a pena remanescente
			List listaCondenacaoNaoExtinta = peeNe.calcularPenaRemanescente(idProcesso, listaEvento);
			
			List listaCondenacaoSelecionada = new ArrayList();
			
			//calcula o tempo total de condenação dos TJ's selecionados
			int tempoCondenacaoDias = 0;
			for (CondenacaoExecucaoDt condenacaoNaoExtinta: (List<CondenacaoExecucaoDt>)listaCondenacaoNaoExtinta) {
				for (HashMap condenacaoMap : (List<HashMap>)listaTJ) {
					if (condenacaoNaoExtinta.getId_ProcessoExecucao().equals(condenacaoMap.get("Id_ProcessoExecucao"))
							&& condenacaoMap.get("Checked").equals("1")){
						tempoCondenacaoDias += Funcoes.StringToInt(condenacaoNaoExtinta.getTempoPenaRemanescenteEmDias());
						listaCondenacaoSelecionada.add(condenacaoNaoExtinta);
					}
				}
			}
			String tempoCondenacaoAnos = Funcoes.converterParaAnoMesDia(tempoCondenacaoDias);
			
			//calcula o restante da pena dos TJ's selecionados até data do decreto
			List listaCondenacaoOrdenada = peeNe.ordenarCrimeMaisGrave_ateDataBase(listaCondenacaoSelecionada, listaEvento, data, true); //ordena os crimes dos TJ selecionados
			int tempoCumpridoAteDataDias = peeNe.calcularTempoCumpridoAteDataInicioEvento(listaEvento, data); //calcula o tempo cumprido total até data do decreto
			int tempoCondenacaoDiasCrimesNaoHediondos = peeNe.calcularTempoCondenacaoCrimesNaoHediondos(listaCondenacaoSelecionada);//Calcula o tempo das condenações dos crimes não hediondos
			int tempoCumpridoAteDataDiasCrimesNaoHediondos = peeNe.calcularTempoCumpridoCrimesNaoHediondos(listaEvento, listaCondenacaoOrdenada, data); //calcula o tempo cumprido total até data do decreto
			int tempoRestanteAteDataDiasCrimesNaoHediondos = tempoCondenacaoDiasCrimesNaoHediondos-tempoCumpridoAteDataDiasCrimesNaoHediondos;
			String tempoRestanteAnosCrimesNaoHediondos = Funcoes.converterParaAnoMesDia(tempoRestanteAteDataDiasCrimesNaoHediondos);
			int tempoRestanteTotalDias = 0;
			
			if (listaCondenacaoOrdenada != null && listaCondenacaoOrdenada.size() > 0 && tempoCumpridoAteDataDias > 0){
				for (int i=0; i < listaCondenacaoOrdenada.size(); i++) {
	                listaCondenacaoOrdenada.get(i);

	                HashMap map = peeNe.calcularRestantePenaCrime((CondenacaoExecucaoDt) listaCondenacaoOrdenada.get(i), data, listaEvento, tempoCumpridoAteDataDias, i, "1"); 
	                int restantePenaCrime = Funcoes.StringToInt(map.get("restantePenaCrime").toString());
	                tempoCumpridoAteDataDias = Funcoes.StringToInt(map.get("tempoCumpridoTotal").toString());

	                tempoRestanteTotalDias += restantePenaCrime;
	            }

			} else {
				tempoRestanteTotalDias = tempoCondenacaoDias;
			}
			String restantePenaAnos = Funcoes.converterParaAnoMesDia(tempoRestanteTotalDias);
			
			//calcula o o tempo cumprido dos TJ's selecionados até data do decreto
			int tempoCumpridoDias = tempoCondenacaoDias - tempoRestanteTotalDias;
			String tempoCumpridoAnos = Funcoes.converterParaAnoMesDia(tempoCumpridoDias);
			
		    oJson.put("tempoCondenacaoDias", String.valueOf(tempoCondenacaoDias));
		    oJson.put("tempoCondenacaoAnos", tempoCondenacaoAnos);
		    oJson.put("tempoCumpridoDias", String.valueOf(tempoCumpridoDias));
		    oJson.put("tempoCumpridoAnos", tempoCumpridoAnos);
		    oJson.put("tempoRestanteTotalDias", String.valueOf(tempoRestanteTotalDias));
		    oJson.put("restantePenaAnos", restantePenaAnos);
		    oJson.put("tempoRestanteAteDataDiasCrimesNaoHediondos", String.valueOf(tempoRestanteAteDataDiasCrimesNaoHediondos));
		    oJson.put("tempoRestanteAnosCrimesNaoHediondos", String.valueOf(tempoRestanteAnosCrimesNaoHediondos));
			
		}
		
		
		return oJson.toString();
	}
}
