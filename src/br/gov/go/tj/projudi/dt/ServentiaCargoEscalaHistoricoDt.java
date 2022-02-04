package br.gov.go.tj.projudi.dt;

public class ServentiaCargoEscalaHistoricoDt extends Dados {

	private static final long serialVersionUID = -7640419704294722575L;
	
	public String idServentiaCargoEscalaHistorico;
	public String serventiaCargoEscalaHistorico;
	public String serventiaCargoEscalaHistoricoCodigo;
	public String dataInicio;
	public String codigoTemp;
	public ServentiaCargoEscalaDt serventiaCargoEscalaDt;
	public ServentiaCargoEscalaStatusDt serventiaCargoEscalaStatusDt;
	
	public String getIdServentiaCargoEscalaHistorico() {
		return this.idServentiaCargoEscalaHistorico;
	}

	public void setIdUsuarioServentiaEscalaStatusHistorico(String id) {
		this.idServentiaCargoEscalaHistorico = id;
	}

	public String getUsuarioServentiaEscalaStatusHistorico() {
		return serventiaCargoEscalaHistorico;
	}

	public void setUsuarioServentiaEscalaStatusHistorico(String usuarioServentiaEscalaStatusHistorico) {
		this.serventiaCargoEscalaHistorico = usuarioServentiaEscalaStatusHistorico;
	}

	public String getUsuarioServentiaEscalaStatusHistoricoCodigo() {
		return serventiaCargoEscalaHistoricoCodigo;
	}

	public void setUsuarioServentiaEscalaStatusHistoricoCodigo(String usuarioServentiaEscalaStatusHistoricoCodigo) {
		this.serventiaCargoEscalaHistoricoCodigo = usuarioServentiaEscalaStatusHistoricoCodigo;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getCodigoTemp() {
		return codigoTemp;
	}

	public void setCodigoTemp(String codigoTemp) {
		this.codigoTemp = codigoTemp;
	}

	public ServentiaCargoEscalaDt getUsuarioServentiaEscalaDt() {
		return serventiaCargoEscalaDt;
	}

	public void setUsuarioServentiaEscalaDt(ServentiaCargoEscalaDt usuarioServentiaEscalaDt) {
		this.serventiaCargoEscalaDt = usuarioServentiaEscalaDt;
	}
	
	public ServentiaCargoEscalaStatusDt getUsuarioServentiaEscalaStatusDt() {
		return this.serventiaCargoEscalaStatusDt;
	}
	
	public void setServentiaCargoEscalaStatusDt(ServentiaCargoEscalaStatusDt serventiaCargoEscalaStatusDt) {
		this.serventiaCargoEscalaStatusDt = serventiaCargoEscalaStatusDt;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}

}
