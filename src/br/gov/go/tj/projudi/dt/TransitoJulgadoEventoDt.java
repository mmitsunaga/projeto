package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;



//---------------------------------------------------------
public class TransitoJulgadoEventoDt extends Dados {

	 /**
	 * 
	 */
	private static final long serialVersionUID = 8961850561072119319L;
	
	private String id_TransitoJulgadoEvento;
	private String id_Evento;
	private String id_TransitoJulgado;
	private String id_ProcessoExecucao;
	private String fracao;
	private String dataInicioTransito;
	private String dataInicioEvento;
	private String id_ProcessoExecucaoPenal;
	private String id_ProcessoDependente;
	private String tempoPenaTJDias;
	private String tempoPenaTJAnos;
	private String tempoPenaRemanescenteTJDias;
	private String tempoPenaRemanescenteTJAnos;
	private String tempoComutacaoDias;
	private String tempoComutacaoAnos;
	private String id_CondenacaoExecucao;
	
	public TransitoJulgadoEventoDt() {
		limpar();
	}

	public void limpar() {
		id_Evento = "";
		id_TransitoJulgadoEvento = "";
		id_TransitoJulgado = "";
		fracao = "";
		id_ProcessoExecucao = "";
		tempoPenaTJDias = "";
		tempoComutacaoDias = "";
		tempoPenaRemanescenteTJDias = "";
		tempoPenaTJAnos = "";
		tempoComutacaoAnos = "";
		tempoPenaRemanescenteTJAnos = "";
		dataInicioTransito = "";
		dataInicioEvento = "";
		id_ProcessoExecucaoPenal = "";
		id_ProcessoDependente = "";
		id_CondenacaoExecucao = "";
	}

	public String getId_TransitoJulgadoEvento() {
		return id_TransitoJulgadoEvento;
	}

	public void setId_TransitoJulgadoEvento(String id_TransitoJulgadoEvento) {
		this.id_TransitoJulgadoEvento = id_TransitoJulgadoEvento;
	}

	public String getId_Evento() {
		return id_Evento;
	}

	public void setId_Evento(String id_Evento) {
		this.id_Evento = id_Evento;
	}

	public String getId_TransitoJulgado() {
		return id_TransitoJulgado;
	}

	public void setId_TransitoJulgado(String id_TransitoJulgado) {
		this.id_TransitoJulgado = id_TransitoJulgado;
	}

	public String getFracao() {
		return fracao;
	}

	public void setFracao(String fracao) {
		this.fracao = fracao;
	}

	@Override
	public String getId() {
		return this.id_TransitoJulgadoEvento;
	}

	@Override
	public void setId(String id) {
		this.id_TransitoJulgadoEvento = id;
		
	}

	public String getId_ProcessoExecucao() {
		return id_ProcessoExecucao;
	}

	public void setId_ProcessoExecucao(String id_ProcessoExecucao) {
		this.id_ProcessoExecucao = id_ProcessoExecucao;
	}
	
	public String getTempoPenaTJDias() {
		return tempoPenaTJDias;
	}

	public void setTempoPenaTJDias(String tempoPenaTJDias) {
		this.tempoPenaTJDias = tempoPenaTJDias;
		setTempoPenaTJAnos(tempoPenaTJDias);
	}
	
	public String getTempoPenaTJAnos() {
		return tempoPenaTJAnos;
	}

	public void setTempoPenaTJAnos(String tempoEmDias) {
		if (tempoEmDias.length() == 0) this.tempoPenaTJAnos = "";
		else this.tempoPenaTJAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
	}

	public String getTempoPenaRemanescenteTJDias() {
		return tempoPenaRemanescenteTJDias;
	}

	public void setTempoPenaRemanescenteTJDias(String tempoPenaRemanescenteTJDias) {
		this.tempoPenaRemanescenteTJDias = tempoPenaRemanescenteTJDias;
		setTempoPenaRemanescenteTJAnos(tempoPenaRemanescenteTJDias);
	}

	public String getTempoPenaRemanescenteTJAnos() {
		return tempoPenaRemanescenteTJAnos;
	}

	public void setTempoPenaRemanescenteTJAnos(String tempoEmDias) {
		if (tempoEmDias.length() == 0) this.tempoPenaRemanescenteTJAnos = "";
		else this.tempoPenaRemanescenteTJAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
	}

	public String getTempoComutacaoDias() {
		return tempoComutacaoDias;
	}

	public void setTempoComutacaoDias(String tempoComutacaoDias) {
		this.tempoComutacaoDias = tempoComutacaoDias;
		setTempoComutacaoAnos(tempoComutacaoDias);
	}

	public String getTempoComutacaoAnos() {
		return tempoComutacaoAnos;
	}

	public void setTempoComutacaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() == 0) this.tempoComutacaoAnos = ""; 
		else this.tempoComutacaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
	}

	public String getDataInicioTransito() {
		return dataInicioTransito;
	}

	public void setDataInicioTransito(String dataInicioTransito) {
		this.dataInicioTransito = dataInicioTransito;
	}

	public String getDataInicioEvento() {
		return dataInicioEvento;
	}

	public void setDataInicioEvento(String dataInicioEvento) {
		this.dataInicioEvento = dataInicioEvento;
	}

	public String getId_ProcessoExecucaoPenal() {
		return id_ProcessoExecucaoPenal;
	}

	public void setId_ProcessoExecucaoPenal(String id_ProcessoExecucaoPenal) {
		this.id_ProcessoExecucaoPenal = id_ProcessoExecucaoPenal;
	}

	public String getId_ProcessoDependente() {
		return id_ProcessoDependente;
	}

	public void setId_ProcessoDependente(String id_ProcessoDependente) {
		this.id_ProcessoDependente = id_ProcessoDependente;
	}

	public String getId_CondenacaoExecucao() {
		return id_CondenacaoExecucao;
	}

	public void setId_CondenacaoExecucao(String id_CondenacaoExecucao) {
		this.id_CondenacaoExecucao = id_CondenacaoExecucao;
	}

	public void copiar(TransitoJulgadoEventoDt objeto){
		id_TransitoJulgadoEvento = objeto.getId();
		id_Evento = objeto.getId_Evento();
		id_TransitoJulgado = objeto.getId_TransitoJulgado();
		fracao = objeto.getFracao();
	}
	
	public String getPropriedades(){
		return "[Id_TransitoJulgadoEvento:" + id_TransitoJulgadoEvento + ";Id_Evento:" + id_Evento + ";Id_TransitoJulgado:" + id_TransitoJulgado+ ";Fracao:" + fracao + "]";
	}
	
}
