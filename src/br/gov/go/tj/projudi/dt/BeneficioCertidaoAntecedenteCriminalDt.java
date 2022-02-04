package br.gov.go.tj.projudi.dt;

public class BeneficioCertidaoAntecedenteCriminalDt extends Dados {
	/**
	 * 
	 */
	private static final long serialVersionUID = 23858220222778837L;
	private String data_beneficio_inicio;
	private String beneficio;
	private String data_beneficio_fim;
	private String condicional;
	private String condicoes;
	
	
	
	public BeneficioCertidaoAntecedenteCriminalDt(String data_beneficio_inicio,
			String beneficio, String data_beneficio_fim) {
		super();
		this.data_beneficio_inicio = data_beneficio_inicio;
		this.beneficio = beneficio;
		this.data_beneficio_fim = data_beneficio_fim;
	}

	public BeneficioCertidaoAntecedenteCriminalDt(){}
	
	public void setBeneficio(String formatarData) {
		this.beneficio = formatarData;
	}

	public String getBeneficio() {
		return beneficio;
	}
	public String getData_beneficio_inicio() {
		return data_beneficio_inicio;
	}

	public void setData_beneficio_inicio(String data_beneficio_inicio) {
		this.data_beneficio_inicio = data_beneficio_inicio;
	}

	public String getData_beneficio_fim() {
		return data_beneficio_fim;
	}

	public void setData_beneficio_fim(String data_beneficio_fim) {
		this.data_beneficio_fim = data_beneficio_fim;
	}

	@Override
	public void setId(String id) {
		

	}

	@Override
	public String getId() {
		
		return null;
	}

	public void setCondicional(String condicional) {
		this.condicional = condicional;
	}

	public void setCondicoes(String condicoes) {
		this.condicoes = condicoes;
	}

	public String getCondicional() {
		return condicional;
	}

	public String getCondicoes() {
		return condicoes;
	}

}
