package br.gov.go.tj.projudi.dt;

public class UsuarioServentiaAudienciaProcessoDt extends Dados{

	/**
	 * Objeto para vincular audiências aos conciliadores/mediadores CEJUSC.
	 */
	
	private static final long serialVersionUID = -7981367447438369707L;
    
	public static final int CodigoPermissao = 844;
	
	String idUsuarioServentia;
	String idAudienciaProcessoDt;
	Boolean isManual;
	
	public String getIdUsuarioServentia() {
		return idUsuarioServentia;
	}

	public void setIdUsuarioServentia(String idUsuarioServentia) {
		this.idUsuarioServentia = idUsuarioServentia;
	}

	public String getIdAudienciaProcessoDt() {
		return idAudienciaProcessoDt;
	}

	public void setIdAudienciaProcessoDt(String idAudienciaProcessoDt) {
		this.idAudienciaProcessoDt = idAudienciaProcessoDt;
	}

	public Boolean getIsManual() {
		return isManual;
	}

	public void setIsManual(Boolean isManual) {
		this.isManual = isManual;
	}

	@Override
	public void setId(String id) {
	}

	@Override
	public String getId() {
		return null;
	}

}
