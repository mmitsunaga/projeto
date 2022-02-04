package br.gov.go.tj.projudi.jurisprudencia;

public enum Ambiente {
	PRODUCAO("http://192.168.200.82/tamino/jurisprudencia"), HOMOLOGACAO("http://10.0.10.215/tamino/jurisprudencia");
	
	private String ambiente;
	
	private Ambiente(String ambiente) {
		this.ambiente = ambiente;
	}
	
	public String getUrl() {
		return this.ambiente;
	}		
	
}
