package br.gov.go.tj.projudi.dt.relatorios;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.DiaHoraEventos;
import br.gov.go.tj.utils.Funcoes;

//atributos declarados com inicial maiúscula para adaptar à interface "InterfaceJasper.java 
public class SaidaTemporariaDt {

	private String idProcesso;
	private String NumeroProcesso;
	private String NomeSentenciado;
	private String NomeMae;
	private String DataNascimento;
	private String Regime;
	private String Status;
	private String BloqueioSaidas;
	private String BloqueioTrabalho;
	private String Hediondo;
	private String TempoTotalCondenacao;
	private String TempoCondenacao16dias;
	private String TempoCondenacao16anos;
	private String TempoCondenacao14dias;
	private String TempoCondenacao14anos;
	private String TempoCumpridoDias;
	private String TempoCumpridoAnos;
	private String TotalSaidasTemporarias;
	private String Ano;
	private List ListaSaidasTemporarias;
//	private List ListaTipoReincidencia;
	private String descricaoReincidencia;
	private String textoSaidaTemporaria;
	private String dataUltimoCalculo;
	
	public SaidaTemporariaDt() {
		idProcesso = "";
		NumeroProcesso = "";
		NomeSentenciado = "";
		NomeMae = "";
		DataNascimento = "";
		Regime = "";
		Status = "";
		BloqueioSaidas = "";
		BloqueioTrabalho = "";
		Hediondo = "";
		TempoTotalCondenacao = "";
		TempoCondenacao16dias = "";
		TempoCondenacao16anos = "";
		TempoCondenacao14dias = "";
		TempoCondenacao14anos = "";
		TempoCumpridoDias = "";
		TempoCumpridoAnos = "";
		TotalSaidasTemporarias = "";
		Ano = "";
		ListaSaidasTemporarias = null;
//		ListaTipoReincidencia = null;
		descricaoReincidencia = "";
		textoSaidaTemporaria = "";
		dataUltimoCalculo = "";
	}

	public String getIdProcesso() {return idProcesso;}
	public void setIdProcesso(String idProcesso) {this.idProcesso = idProcesso;}
	
	public String getNumeroProcesso() {return NumeroProcesso;}
	public void setNumeroProcesso(String numeroProcesso) {this.NumeroProcesso = numeroProcesso;}

	public String getNomeSentenciado() {return NomeSentenciado;}
	public void setNomeSentenciado(String nomeSentenciado) {this.NomeSentenciado = nomeSentenciado;}

	public String getNomeMae() {return NomeMae;}
	public void setNomeMae(String nomeMae) {this.NomeMae = nomeMae;	}

	public String getDataNascimento() {return DataNascimento;	}
	public void setDataNascimento(String dataNascimento) {	this.DataNascimento = dataNascimento;	}

	public String getRegime() {	return Regime;	}
	public void setRegime(String regime) {	this.Regime = regime;	}

	public String getStatus() {	return Status;	}
	public void setStatus(String status) {	this.Status = status; }

	public String getBloqueioSaidas() {	return BloqueioSaidas; }
	public void setBloqueioSaidas(String bloqueioSaidas) {	this.BloqueioSaidas = bloqueioSaidas; }

	public String getBloqueioTrabalho() {	return BloqueioTrabalho; }
	public void setBloqueioTrabalho(String bloqueioTrabalho) {	this.BloqueioTrabalho = bloqueioTrabalho; }

	public String getHediondo() {	return Hediondo;	}
	public void setHediondo(String hediondo) {	this.Hediondo = hediondo; }

	public String getTempoTotalCondenacao() {	return TempoTotalCondenacao;	}
	public void setTempoTotalCondenacao(String tempoTotalCondenacao) {	this.TempoTotalCondenacao = tempoTotalCondenacao;	}

	public String getTempoCondenacao16dias() {	return TempoCondenacao16dias;	}
	public void setTempoCondenacao16dias(String tempoDias) {
		this.TempoCondenacao16dias = tempoDias;	
		setTempoCondenacao16anos(tempoDias);
	}

	public String getTempoCondenacao14dias() {	return TempoCondenacao14dias;	}
	public void setTempoCondenacao14dias(String tempoDias) {	
		this.TempoCondenacao14dias = tempoDias;
		setTempoCondenacao14anos(tempoDias);
	}

	public String getTotalSaidasTemporarias() {	return TotalSaidasTemporarias;	}
	public void setTotalSaidasTemporarias(String totalSaidasTemporarias) {	this.TotalSaidasTemporarias = totalSaidasTemporarias;	}

	public List getListaSaidasTemporarias() {	return ListaSaidasTemporarias;	}
	public void setListaSaidasTemporarias(List listaSaidasTemporarias) {	this.ListaSaidasTemporarias = listaSaidasTemporarias;	}
	public void addListaSaidasTemporarias(String dt) {
		if (ListaSaidasTemporarias == null) ListaSaidasTemporarias = new ArrayList();
		this.ListaSaidasTemporarias.add(dt);
	}
	
//	public List getListaTipoReincidencia() {	return ListaTipoReincidencia;	}
//	public void setListaTipoReincidencia(List listaTipoReincidencia) {	this.ListaTipoReincidencia = listaTipoReincidencia;	}
//	public void addListaTipoReincidencia(String dt) {
//		if (ListaTipoReincidencia == null) ListaTipoReincidencia = new ArrayList();
//		this.ListaTipoReincidencia.add(dt);
//	}

	public String getAno() {return Ano; }
	public String getDescricaoReincidencia() {
		return descricaoReincidencia;
	}

	public void setDescricaoReincidencia(String descricaoReincidencia) {
		this.descricaoReincidencia = descricaoReincidencia;
	}

	public void setAno(String ano) {if (ano != null) Ano = ano; }

	public String getTempoCondenacao14anos() {	return TempoCondenacao14anos; }
	public void setTempoCondenacao14anos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.TempoCondenacao14anos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.TempoCondenacao14anos = "";
	}

	public String getTempoCondenacao16anos() {return TempoCondenacao16anos;	}
	public void setTempoCondenacao16anos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.TempoCondenacao16anos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.TempoCondenacao16anos = "";
	}
	
	public String getTempoCumpridoDias() {	return TempoCumpridoDias;	}
	public void setTempoCumpridoDias(String tempoDias) {	
		this.TempoCumpridoDias = tempoDias;
		setTempoCumpridoAnos(tempoDias);
	}
	
	public String getTempoCumpridoAnos() {	return TempoCumpridoAnos; }
	public void setTempoCumpridoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.TempoCumpridoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.TempoCumpridoAnos = "";
	}

	public String getDataConsulta() {
		return new DiaHoraEventos().diaHoraSegundos();
	}

	public String getTextoSaidaTemporaria() {
		return textoSaidaTemporaria;
	}

	public void setTextoSaidaTemporaria(String textoSaidaTemporaria) {
		this.textoSaidaTemporaria = textoSaidaTemporaria;
	}

	public String getDataUltimoCalculo() {
		return dataUltimoCalculo;
	}

	public void setDataUltimoCalculo(String dataUltimoCalculo) {
		this.dataUltimoCalculo = dataUltimoCalculo;
	}	
	
}
