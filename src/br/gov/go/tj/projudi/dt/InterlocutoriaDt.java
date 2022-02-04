package br.gov.go.tj.projudi.dt;

public class InterlocutoriaDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 942962738717773305L;

	public InterlocutoriaDt(String numero, String dataProtocolo, String tipo,
			String fase) {
		super();
		this.numero = numero;
		this.dataProtocolo = dataProtocolo;
		this.tipo = tipo;
		this.fase = fase;
	}
	public InterlocutoriaDt(){}
	
	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDataProtocolo() {
		return dataProtocolo;
	}

	public void setDataProtocolo(String dataProtocolo) {
		this.dataProtocolo = dataProtocolo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFase() {
		return fase;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	private String numero;
	private String dataProtocolo;
	private String tipo;
	private String fase;

	@Override
	public void setId(String id) {
		

	}

	@Override
	public String getId() {
		
		return null;
	}

}
