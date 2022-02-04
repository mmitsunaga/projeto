package br.gov.go.tj.projudi.dt;

public class EscalaMgDtGen extends Dados {

	private static final long serialVersionUID = 6952527689973829464L;

	protected String idEscalaMg;
	protected String idUsuario;
	protected String idEscalaTipoMg;
	protected String dataInicio;
	protected String dataFim;
	protected String codigoEscalaMg;

	public EscalaMgDtGen() {
		limpar();
	}

	public String getId() {
		return idEscalaMg;
	}

	public void setId(String idEscalaMg) {
		this.idEscalaMg = idEscalaMg;
	}

	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getIdEscalaTipoMg() {
		return idEscalaTipoMg;
	}

	public void setIdEscalaTipoMg(String idEscalaTipoMg) {
		this.idEscalaTipoMg = idEscalaTipoMg;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}
	
	
	public String getCodigoEscalaMg() {
		return codigoEscalaMg;
	}

	public void setCodigoEscalaMg(String codigoEscalaMg) {
		this.codigoEscalaMg = codigoEscalaMg;
	}

	public void copiar(EscalaMgDt objeto){
		 if (objeto==null) return;
		idEscalaMg = objeto.getId();
		idUsuario = objeto.getIdUsuario();
		idEscalaTipoMg = objeto.getIdEscalaTipoMg();
		dataInicio = objeto.getDataInicio();
		dataFim = objeto.getDataFim();
		codigoEscalaMg = objeto.getCodigoEscalaMg();
		CodigoTemp = objeto.getCodigoTemp();
	}


	public String getPropriedades() {

		return "[idEscalaMg:" + idEscalaMg + ";idUsuario:" + idUsuario + ";idEscalaTipoMg:" + idEscalaTipoMg + ";dataInicio:"
				+ dataInicio + ";dataFim:" + dataFim + "]";
	}

}
