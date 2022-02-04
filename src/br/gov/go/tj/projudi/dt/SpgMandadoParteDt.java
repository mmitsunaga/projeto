package br.gov.go.tj.projudi.dt;

public class SpgMandadoParteDt extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5055150052297847477L;
	
	public final static String  PARTE_TIPO_ADVOGADO = "A";
	public final static String  PARTE_TIPO_TESTEMUNHA = "T";
	public final static String  PARTE_TIPO_PESSOA = "P";
	public final static String  COMPLEMENTAR_SIM = "S";
	public final static String  COMPLEMENTAR_NAO = "N";

	String NomeParte="";
	String Rua="";
	String Numero="";
	String Lote="";
	String Quadra ="";
	String BairroCodigo="";
	String MunicipioCodigo="";
	String Complemento="";
	String Cep="";
	String Complementar="";
	String ParteTipo="";
	String NumeroOab ="";
	String NumeroProcessoProjudi = "";
	String MandadoNumero="";

	public String getBairroCodigo() {
		return BairroCodigo;
	}


	public void setBairroCodigo(String bairroCodigo) {
		BairroCodigo = bairroCodigo;
	}
	
	public String getNomeParte() {
		return NomeParte;
	}


	public void setNomeParte(String nomeParte) {
		this.NomeParte = nomeParte;
	}


	public String getRua() {
		return Rua;
	}


	public void setRua(String rua) {
		Rua = rua;
	}


	public String getNumero() {
		return Numero;
	}


	public void setNumero(String numero) {
		Numero = numero;
	}


	public String getLote() {
		return Lote;
	}


	public void setLote(String lote) {
		Lote = lote;
	}


	public String getQuadra() {
		return Quadra;
	}


	public void setQuadra(String quadra) {
		Quadra = quadra;
	}


	public String getMunicipioCodigo() {
		return MunicipioCodigo;
	}


	public void setMunicipioCodigo(String municipioCodigo) {
		MunicipioCodigo = municipioCodigo;
	}


	public String getComplemento() {
		return Complemento;
	}


	public void setComplemento(String complemento) {
		Complemento = complemento;
	}


	public String getCep() {
		return Cep;
	}


	public void setCep(String cep) {
		Cep = cep;
	}


	public String getComplementar() {
		return Complementar;
	}


	public void setComplementar(String complementar) {
		Complementar = complementar;
	}


	public String getParteTipo() {
		return ParteTipo;
	}


	public void setParteTipo(String tipoParte) {
		ParteTipo = tipoParte;
	}


	public String getNumeroOab() {
		return NumeroOab;
	}


	public void setNumeroOab(String numeroOab) {
		NumeroOab = numeroOab;
	}


	public String getNumeroProcessoProjudi() {
		return NumeroProcessoProjudi;
	}


	public void setNumeroProcessoProjudi(String numeroProcessoProjudi) {
		NumeroProcessoProjudi = numeroProcessoProjudi;
	}


	public String getMandadoNumero() {
		return MandadoNumero;
	}


	public void setMandadoNumero(String numeroMandado) {
		MandadoNumero = numeroMandado;
	}

	
	public void setId(String id) {
		MandadoNumero = id;
		
	}

	
	public String getId() {

		return MandadoNumero;
	}


	public String getPropriedades() {		 
		return "[Parte:" + NomeParte +";Rua:" +Rua +";Numero:"+Numero +";Lote:"+Lote +";Quadra:"+Quadra +";BairroCodigo:"+BairroCodigo +";MunicipioCodigo:"+MunicipioCodigo +";Complemento:"+Complemento +";Cep:"+Cep +";Complementar:"+Complementar +";ParteTipo:"+ ParteTipo+";NumeroOab :"+NumeroOab +";NumeroProcessoProjudi:"+NumeroProcessoProjudi +";MandadoNumero:"+MandadoNumero +"]";
	}


	public void copiar(SpgMandadoParteDt dados) {
		 NomeParte= dados.getNomeParte();
		 Rua= dados.getRua();
		 Numero= dados.getNumero();
		 Lote= dados.getLote();
		 Quadra = dados.getQuadra();
		 BairroCodigo= dados.getBairroCodigo();
		 MunicipioCodigo= dados.getMunicipioCodigo();
		 Complemento= dados.getComplemento();
		 Cep= dados.getCep();
		 Complementar= dados.getComplementar();
		 ParteTipo= dados.getParteTipo();
		 NumeroOab = dados.getNumeroOab();
		 NumeroProcessoProjudi = dados.getNumeroProcessoProjudi();
		 MandadoNumero= dados.getMandadoNumero();
		
	}

}

