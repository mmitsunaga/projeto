package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class LigacaoDt implements Serializable {

	public String getProcessoDesmembradoAtual() {
		return ProcessoDesmembradoAtual;
	}

	public void setProcessoDesmembradoAtual(String processoDesmembradoAtual) {
		ProcessoDesmembradoAtual = processoDesmembradoAtual;
	}

	public String getListaProcessosDesmembrados() {
		return ListaProcessosDesmembrados;
	}

	public void setListaProcessosDesmembrados(String listaProcessosDesmembrados) {
		ListaProcessosDesmembrados = listaProcessosDesmembrados;
	}

	public String getDataApensamento() {
		return DataApensamento;
	}

	public void setDataApensamento(String dataApensamento) {
		DataApensamento = dataApensamento;
	}

	public String getNumeroProcessoApensado() {
		return NumeroProcessoApensado;
	}

	public void setNumeroProcessoApensado(String numeroProcessoApensado) {
		NumeroProcessoApensado = numeroProcessoApensado;
	}

	public String getListaProcessosApensados() {
		return ListaProcessosApensados;
	}

	public void setListaProcessosApensados(String listaProcessosApensados) {
		ListaProcessosApensados = listaProcessosApensados;
	}

	public String getProcessoAndamento() {
		return ProcessoAndamento;
	}

	public void setProcessoAndamento(String processoAndamento) {
		ProcessoAndamento = processoAndamento;
	}

	public String getFase() {
		return Fase;
	}

	public void setFase(String fase) {
		Fase = fase;
	}

	public String getNumeroRecurso() {
		return NumeroRecurso;
	}

	public void setNumeroRecurso(String numeroRecurso) {
		NumeroRecurso = numeroRecurso;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4301757230609934525L;
	
	private String DataDesmebramento;
	private String ProcessoDesmembradoAtual;
	private String ListaProcessosDesmembrados;
	private String DataApensamento;
	private String NumeroProcessoApensado;
	private String ListaProcessosApensados;
	private String ProcessoAndamento;
	private String Fase;
	private String NumeroRecurso;

	public LigacaoDt(String dataDesmebramento, String processoDesmembradoAtual, String listaProcessosDesmembrados, String dataApensamento, String numeroProcessoApensado, String listaProcessosApensados, String processoAndamento, String fase, String numeroRecurso) {
		setDataDesmebramento(dataDesmebramento);
		ProcessoDesmembradoAtual = processoDesmembradoAtual;
		ListaProcessosDesmembrados =listaProcessosDesmembrados;
		DataApensamento =dataApensamento;
		NumeroProcessoApensado =numeroProcessoApensado;
		ListaProcessosApensados = listaProcessosApensados;
		ProcessoAndamento = processoAndamento;
		Fase= fase;
		NumeroRecurso = numeroRecurso;		
	}

	public String getDataDesmebramento() {
		return DataDesmebramento;
	}

	public void setDataDesmebramento(String dataDesmebramento) {
		DataDesmebramento = dataDesmebramento;
	}



}
