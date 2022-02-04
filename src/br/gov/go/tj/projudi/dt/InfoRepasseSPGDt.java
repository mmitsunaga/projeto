package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.TJDataHora;

public class InfoRepasseSPGDt extends Dados {
	
	private static final long serialVersionUID = 21364102751790620L;
	
	private String isn;
	private String codgEscrivania;
	private String dataRepasse;
	private TJDataHora dataRepasseTJDataHora;
	private String percRepasse;

	@Override
	public void setId(String id) {
		this.isn = id;
	}

	@Override
	public String getId() {
		return this.isn;
	}

	public String getIsn() {
		return isn;
	}

	public void setIsn(String isn) {
		this.isn = isn;
	}

	public String getCodgEscrivania() {
		return codgEscrivania;
	}

	public void setCodgEscrivania(String codgEscrivania) {
		this.codgEscrivania = codgEscrivania;
	}

	public String getDataRepasse() {
		return dataRepasse;
	}

	public void setDataRepasse(String dataRepasse) {
		this.dataRepasse = dataRepasse;
	}

	public TJDataHora getDataRepasseTJDataHora() {
		return dataRepasseTJDataHora;
	}

	public void setDataRepasseTJDataHora(TJDataHora dataRepasseTJDataHora) {
		this.dataRepasseTJDataHora = dataRepasseTJDataHora;
	}

	public String getPercRepasse() {
		return percRepasse;
	}

	public void setPercRepasse(String percRepasse) {
		this.percRepasse = percRepasse;
	}

}
