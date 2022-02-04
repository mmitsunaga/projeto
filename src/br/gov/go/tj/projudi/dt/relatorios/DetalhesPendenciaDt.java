package br.gov.go.tj.projudi.dt.relatorios;


public class DetalhesPendenciaDt {
	
	private String pendenciaTipo;
	private String qtdTotal;
	private String qtdFinalizada;
	private String qtdPendente;
	private String percFinalizada;
	private String tempMedioFinalizar;
		
	public DetalhesPendenciaDt(){
		pendenciaTipo = "";
		qtdTotal = "";
		qtdFinalizada = "";
		qtdPendente = "";
		percFinalizada = "";
		tempMedioFinalizar = "";
	}
	
	public String getPendenciaTipo() {
		return pendenciaTipo;
	}
	
	public void setPendenciaTipo(String pendenteTipo) {
		this.pendenciaTipo = pendenteTipo;
	}
	
	public String getQtdTotal() {
		return qtdTotal;
	}
	
	public void setQtdTotal(String qtdTotal) {
		this.qtdTotal = qtdTotal;
	}
	
	public String getQtdFinalizada() {
		return qtdFinalizada;
	}
	
	public void setQtdFinalizada(String qtdFinalizada) {
		this.qtdFinalizada = qtdFinalizada;
	}
	
	public String getQtdPendente() {
		return qtdPendente;
	}
	
	public void setQtdPendente(String qtdPendente) {
		this.qtdPendente = qtdPendente;
	}
	
	public String getTempMedioFinalizar() {
		return tempMedioFinalizar;
	}
	
	public void setTempMedioFinalizar(String tempMedioFinalizar) {
		this.tempMedioFinalizar = tempMedioFinalizar;
	}

	public String getPercFinalizada() {
		return percFinalizada;
	}

	public void setPercFinalizada(String percFinalizada) {
		this.percFinalizada = percFinalizada;
	}	

}
