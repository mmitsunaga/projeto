package br.gov.go.tj.projudi.dt;

public class AudienciaProcessoPendenciaDt extends Dados {
	private String id_AudienciaProcesso_Pend;
	private String id_AudienciaProcesso;
	private String id_Pend;
	
	
	public AudienciaProcessoPendenciaDt(String idPend, String idAudiProc) {
		setIdPend(idPend);
		setIdAudienciaProcesso(idAudiProc);
	}

	@Override
	public void setId(String id) {
		id_AudienciaProcesso_Pend = id;
	}

	@Override
	public String getId() {
		return id_AudienciaProcesso_Pend;
	}

	public String getIdAudienciaProcesso() {
		return id_AudienciaProcesso;
	}

	public void setIdAudienciaProcesso(String id_AudienciaProcesso) {
		this.id_AudienciaProcesso = id_AudienciaProcesso;
	}

	public String getIdPend() {
		return id_Pend;
	}

	public void setIdPend(String id_Pend) {
		this.id_Pend = id_Pend;
	}

}
