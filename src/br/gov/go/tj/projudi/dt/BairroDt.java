package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class BairroDt extends BairroDtGen {

	private static final long serialVersionUID = -1896290991088292980L;

	public static final int CodigoPermissao = 174;

	private String uf;
	private String bairroCompleto;
	private String zona;
	private String regiao;
	private String codigoCidadeSPG;

	public String getUf() {
		return uf;
	}

	public void setUf(String valor) {
		if (valor != null) {
			uf = valor;
		}
	}

	public String getBairroCompleto() {
		return bairroCompleto;
	}

	public void setBairroCompleto(String bairroCompleto) {
		this.bairroCompleto = bairroCompleto;
	}
	
	public String getZona() {
		return zona;
	}
	
	public void setZona(String zona) {
		if(zona != null)
			this.zona = zona;
	}

	public String getRegiao() {
		return regiao;
	}

	public void setRegiao(String regiao) {
		if(regiao != null)
			this.regiao = regiao;
	}
	
	public String getCodigoCidadeSPG()  {return codigoCidadeSPG;}
	
	public void setCodigoCidadeSPG(String valor) { 
		if (valor!=null) {
			codigoCidadeSPG = valor;
		}
	}
	
	public void copiar(BairroDt objeto){
		if (objeto==null) {
			return;
		}
		super.copiar(objeto);
		uf = objeto.uf;
		bairroCompleto = objeto.bairroCompleto;
		zona = objeto.getZona();
		regiao = objeto.getRegiao();
		codigoCidadeSPG = objeto.getCodigoCidadeSPG();
	}

	public void limpar(){
		super.limpar();
		uf = "";
		bairroCompleto = "";
		zona = "";
		regiao = "";
		codigoCidadeSPG = "";
	}
	
	public int ObtenhaSomenteCodigoDoBairroSPGSemCodigoMunicipio() {
		if (this.getCodigoSPG() == null || this.getCodigoSPG().trim().length() == 0) {
			return 0;
		}
		
		String codigoDoBairroEMunicipioFormatado = Funcoes.completarZeros(this.getCodigoSPG(), 12);
		
		return Funcoes.StringToInt(codigoDoBairroEMunicipioFormatado.substring(7, 12));		
	}
}