package br.gov.go.tj.projudi.dt;

import java.util.Date;

public class SpguSentencaDt extends Dados {

	private static final long serialVersionUID = 519018662208536505L;
	
	private String id;
	private String isnSpguSentenca;
	private String numeroProcesso;
	private Date dataSentenca;
	private Date dataTransJulg;
	private String tipoSentenca;
	private String regime;
	private String numeroSequencia;
	private String codigoJuiz;
	private String statusSentenca;
	private String infoMerito;
	private String pena;
	private String multa;
	private Date dataRegistroSentenca;
	private String infoSentLiquida;
	
	private boolean rem;
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsnSpguSentenca() {
		return isnSpguSentenca;
	}

	public void setIsnSpguSentenca(String isnSpguSentenca) {
		this.isnSpguSentenca = isnSpguSentenca;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public Date getDataSentenca() {
		return dataSentenca;
	}

	public void setDataSentenca(Date dataSentenca) {
		this.dataSentenca = dataSentenca;
	}

	public Date getDataTransJulg() {
		return dataTransJulg;
	}

	public void setDataTransJulg(Date dataTransJulg) {
		this.dataTransJulg = dataTransJulg;
	}

	public String getTipoSentenca() {
		return tipoSentenca;
	}

	public void setTipoSentenca(String tipoSentenca) {
		this.tipoSentenca = tipoSentenca;
	}

	public String getRegime() {
		return regime;
	}

	public void setRegime(String regime) {
		this.regime = regime;
	}

	public String getNumeroSequencia() {
		return numeroSequencia;
	}

	public void setNumeroSequencia(String numeroSequencia) {
		this.numeroSequencia = numeroSequencia;
	}

	public String getCodigoJuiz() {
		return codigoJuiz;
	}

	public void setCodigoJuiz(String codigoJuiz) {
		this.codigoJuiz = codigoJuiz;
	}

	public String getStatusSentenca() {
		return statusSentenca;
	}

	public void setStatusSentenca(String statusSentenca) {
		this.statusSentenca = statusSentenca;
	}

	public String getInfoMerito() {
		return infoMerito;
	}

	public void setInfoMerito(String infoMerito) {
		this.infoMerito = infoMerito;
	}

	public String getPena() {
		return pena;
	}

	public void setPena(String pena) {
		this.pena = pena;
	}

	public String getMulta() {
		return multa;
	}

	public void setMulta(String multa) {
		this.multa = multa;
	}

	public Date getDataRegistroSentenca() {
		return dataRegistroSentenca;
	}

	public void setDataRegistroSentenca(Date dataRegistroSentenca) {
		this.dataRegistroSentenca = dataRegistroSentenca;
	}

	public String getInfoSentLiquida() {
		return infoSentLiquida;
	}

	public void setInfoSentLiquida(String infoSentLiquida) {
		this.infoSentLiquida = infoSentLiquida;
	}

	public boolean isRem() {
		return rem;
	}

	public void setRem(boolean rem) {
		this.rem = rem;
	}
}