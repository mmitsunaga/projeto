package br.gov.go.tj.projudi.dt; 

public class HistoricoRedistribuicaoMandadosDt {

 
	private String id;
	private String dataRedist;
	private String idMandJud;
	private String idUsuServAnt;
	private String idUsuServAtual;
	private String motivo;
	private String compensado;
	private String idEscAnterior;
	private String idEscAtual;
	private String idMandTipoRedist;
	
	public HistoricoRedistribuicaoMandadosDt() {
		limpar();
	}
	
	public void limpar() {
		id = "";
		dataRedist = "";
		idMandJud = "";
		idUsuServAnt = "";
		idUsuServAtual = "";
		motivo = "";
		compensado = "";	 
		idEscAnterior = "";
		idEscAtual = "";
		idMandTipoRedist = "";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDataRedist() {
		return dataRedist;
	}

	public void setDataRedist(String dataRedist) {
		this.dataRedist = dataRedist;
	}

	public String getIdMandJud() {
		return idMandJud;
	}

	public void setIdMandJud(String idMandJud) {
		this.idMandJud = idMandJud;
	}

	public String getIdUsuServAnt() {
		return idUsuServAnt;
	}

	public void setIdUsuServAnt(String idUsuServAnt) {
		this.idUsuServAnt = idUsuServAnt;
	}

	public String getIdUsuServAtual() {
		return idUsuServAtual;
	}

	public void setIdUsuServAtual(String idUsuServAtual) {
		this.idUsuServAtual = idUsuServAtual;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getCompensado() {
		return compensado;
	}

	public void setCompensado(String compensado) {
		this.compensado = compensado;
	}
 

	public String getIdEscAnterior() {
		return idEscAnterior;
	}

	public void setIdEscAnterior(String idEscAnterior) {
		this.idEscAnterior = idEscAnterior;
	}

	public String getIdEscAtual() {
		return idEscAtual;
	}

	public void setIdEscAtual(String idEscAtual) {
		this.idEscAtual = idEscAtual;
	}

	public String getIdMandTipoRedist() {
		return idMandTipoRedist;
	}

	public void setIdMandTipoRedist(String idMandTipoRedist) {
		this.idMandTipoRedist = idMandTipoRedist;
	} 

}