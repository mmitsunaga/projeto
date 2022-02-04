package br.gov.go.tj.projudi.dt;

//lrcampos 12/07/2019 * Criado Dt para mapear objetos SUSTENTACAO_ORAL
public class SustentacaoOralDt {
	
	private String id;
	private AudienciaProcessoDt audienciaProcessoDt;
	private ProcessoParteAdvogadoDt processoParteAdvogadoDt;
	private UsuarioServentiaDt usuarioServentiaDt;
	private PendenciaDt pendenciaDt;
	private ProcessoParteDt processoParteDt;
	
	
	public SustentacaoOralDt() {
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public AudienciaProcessoDt getAudienciaProcessoDt() {
		return audienciaProcessoDt;
	}


	public void setAudienciaProcessoDt(AudienciaProcessoDt audienciaProcessoDt) {
		this.audienciaProcessoDt = audienciaProcessoDt;
	}


	public ProcessoParteAdvogadoDt getProcessoParteAdvogadoDt() {
		return processoParteAdvogadoDt;
	}


	public void setProcessoParteAdvogadoDt(ProcessoParteAdvogadoDt processoParteAdvogadoDt) {
		this.processoParteAdvogadoDt = processoParteAdvogadoDt;
	}


	public UsuarioServentiaDt getUsuarioServentiaDt() {
		return usuarioServentiaDt;
	}


	public void setUsuarioServentiaDt(UsuarioServentiaDt usuarioServentiaDt) {
		this.usuarioServentiaDt = usuarioServentiaDt;
	}


	public PendenciaDt getPendenciaDt() {
		return pendenciaDt;
	}


	public void setPendenciaDt(PendenciaDt pendenciaDt) {
		this.pendenciaDt = pendenciaDt;
	}


	public ProcessoParteDt getProcessoParteDt() {
		return processoParteDt;
	}


	public void setProcessoParteDt(ProcessoParteDt processoParteDt) {
		this.processoParteDt = processoParteDt;
	}

}
