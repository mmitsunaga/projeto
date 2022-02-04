package br.gov.go.tj.utils.Certificado;


public enum CadeiaDeCertificado {
	ICP_BR("acs-icpbr"), TJGO("ac-tjgo"), CAIXA_WEBSERVICE("ac-caixa"),  CORREIOS_WEBSERVICE("ac-correios");
	 
	private String pasta;

	private CadeiaDeCertificado(String pasta) {
		this.pasta = pasta;
	}
	
	public String getPasta() {
		return pasta;
	}
}
