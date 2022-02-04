package br.gov.go.tj.projudi.dt;

public class HistoricoDt extends Dados {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1468470599364463885L;
	private String data;
	private String hora;
	private String fase;
	private String descricaoFase;
	public HistoricoDt(String data, String hora, String fase,
			String descricaoFase) {
		super();
		this.data = data;
		this.hora = hora;
		this.fase = fase;
		this.descricaoFase = descricaoFase;
	}
	public HistoricoDt() {
		this.data = "";
		this.hora = "";
		this.fase = "";
		this.descricaoFase = "";
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getFase() {
		return fase;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	public String getDescricaoFase() {
		return descricaoFase;
	}

	public void setDescricaoFase(String descricaoFase) {
		this.descricaoFase = descricaoFase;
	}

	@Override
	public void setId(String id) {
		

	}

	@Override
	public String getId() {
		
		return null;
	}
	public void setMensagem(String value) {
	}

}
