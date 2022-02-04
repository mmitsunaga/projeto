package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class SessaoSegundoGrauProcessoDt implements Serializable {	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6726265881234298159L;
	
	private String id_UsuarioLog;
	private String ipComputadorLog;
	
	private AudienciaProcessoDt audienciaProcessoDt;
	private PendenciaArquivoDt pendenciaArquivoDtRelatorioEVoto;
	private PendenciaArquivoDt pendenciaArquivoDtEmenta;
	
	public SessaoSegundoGrauProcessoDt() {
		limpar();
	}
	
	public void limpar() {
		audienciaProcessoDt = null;		
	}
	
	public String getId_UsuarioLog() {
		return id_UsuarioLog;
	}

	public void setId_UsuarioLog(String id_UsuarioLog) {
		if (id_UsuarioLog != null) this.id_UsuarioLog = id_UsuarioLog;
	}

	public String getIpComputadorLog() {
		return ipComputadorLog;
	}

	public void setIpComputadorLog(String ipComputadorLog) {
		if (ipComputadorLog != null) this.ipComputadorLog = ipComputadorLog;
	}

	public AudienciaProcessoDt getAudienciaProcessoDt() {
		return audienciaProcessoDt;
	}

	public void setAudienciaProcessoDt(AudienciaProcessoDt audienciaProcessoDt) {
		this.audienciaProcessoDt = audienciaProcessoDt;
	}

	public PendenciaArquivoDt getPendenciaArquivoDtRelatorioEVoto() {
		return pendenciaArquivoDtRelatorioEVoto;
	}

	public void setPendenciaArquivoDtRelatorioEVoto(PendenciaArquivoDt pendenciaArquivoDtRelatorioEVoto) {
		this.pendenciaArquivoDtRelatorioEVoto = pendenciaArquivoDtRelatorioEVoto;
	}

	public PendenciaArquivoDt getPendenciaArquivoDtEmenta() {
		return pendenciaArquivoDtEmenta;
	}

	public void setPendenciaArquivoDtEmenta(PendenciaArquivoDt pendenciaArquivoDtEmenta) {
		this.pendenciaArquivoDtEmenta = pendenciaArquivoDtEmenta;
	}
	
	public boolean isSessaoVirtual() throws Exception {
		return this.audienciaProcessoDt != null && this.audienciaProcessoDt.isSessaoVirtual();
	}
}