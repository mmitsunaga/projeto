package br.gov.go.tj.projudi.dt;

public class RecursoSecundarioParteDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4498427792648000545L;
	private String Id_RecursoSecundarioParte;
	private String Id_ProcessoParte;
	private String Id_ProcessoParteTipo;
	private String ProcessoParteTipo;
	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String Id_AudienciaProcesso;
	private String Id_ProcessoTipoRecursoSecundario;
	private String ProcessoTipoRecursoSecundario;
	private String OrdemParte;
	private String descricaoPoloAtivo;
	private String descricaoPoloPassivo;


	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		Id_RecursoSecundarioParte = id;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return Id_RecursoSecundarioParte;
	}

	public String getId_ProcessoParte() {
		return Id_ProcessoParte;
	}

	public void setId_ProcessoParte(String id_ProcessoParte) {
		Id_ProcessoParte = id_ProcessoParte;
	}

	public String getId_ProcessoParteTipo() {
		return Id_ProcessoParteTipo;
	}

	public void setId_ProcessoParteTipo(String id_ProcessoParteTipo) {
		Id_ProcessoParteTipo = id_ProcessoParteTipo;
	}

	public String getProcessoParteTipo() {
		return ProcessoParteTipo;
	}

	public void setProcessoParteTipo(String processoParteTipo) {
		ProcessoParteTipo = processoParteTipo;
	}

	public String getId_ProcessoTipo() {
		return Id_ProcessoTipo;
	}

	public void setId_ProcessoTipo(String id_ProcessoTipo) {
		Id_ProcessoTipo = id_ProcessoTipo;
	}

	public String getId_AudienciaProcesso() {
		return Id_AudienciaProcesso;
	}

	public void setId_AudienciaProcesso(String id_AudienciaProcesso) {
		Id_AudienciaProcesso = id_AudienciaProcesso;
	}

	public String getProcessoTipo() {
		return ProcessoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		ProcessoTipo = processoTipo;
	}

	public String getProcessoTipoRecursoSecundario() {
		return ProcessoTipoRecursoSecundario;
	}

	public void setProcessoTipoRecursoSecundario(String processoTipoRecursoSecundario) {
		ProcessoTipoRecursoSecundario = processoTipoRecursoSecundario;
	}

	public String getOrdemParte() {
		return OrdemParte;
	}

	public void setOrdemParte(String ordemParte) {
		OrdemParte = ordemParte;
	}

	public String getId_ProcessoTipoRecursoSecundario() {
		return Id_ProcessoTipoRecursoSecundario;
	}

	public void setId_ProcessoTipoRecursoSecundario(String id_ProcessoTipoRecursoSecundario) {
		Id_ProcessoTipoRecursoSecundario = id_ProcessoTipoRecursoSecundario;
	}

	public String getDescricaoPoloAtivo() {
		return descricaoPoloAtivo;
	}

	public void setDescricaoPoloAtivo(String descricaoPoloAtivo) {
		this.descricaoPoloAtivo = descricaoPoloAtivo;
	}

	public String getDescricaoPoloPassivo() {
		return descricaoPoloPassivo;
	}

	public void setDescricaoPoloPassivo(String descricaoPoloPassivo) {
		this.descricaoPoloPassivo = descricaoPoloPassivo;
	}
	

}
