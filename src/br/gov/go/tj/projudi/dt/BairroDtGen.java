package br.gov.go.tj.projudi.dt;

public class BairroDtGen extends Dados{

	private static final long serialVersionUID = 2717285373795822341L;
	private String Id_Bairro;
	private String Bairro;


	private String BairroCodigo;
	private String CodigoSPG;
	private String Id_Cidade;
	private String Cidade;
	private String CodigoTemp;
	private String Uf;

	public BairroDtGen() {
		limpar();
	}

	public String getId()  {return Id_Bairro;}
	public void setId(String valor ) {if(valor!=null) Id_Bairro = valor;}
	public String getBairro()  {return Bairro;}
	public void setBairro(String valor ) {if (valor!=null) Bairro = valor;}
	public String getBairroCodigo()  {return BairroCodigo;}
	public void setBairroCodigo(String valor ) {if (valor!=null) BairroCodigo = valor;}
	public String getCodigoSPG()  {return CodigoSPG;}
	public void setCodigoSPG(String valor ) {if (valor!=null) CodigoSPG = valor;}
	public String getId_Cidade()  {return Id_Cidade;}
	public void setId_Cidade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Cidade = ""; Cidade = "";}else if (!valor.equalsIgnoreCase("")) Id_Cidade = valor;}
	public String getCidade()  {return Cidade;}
	public void setCidade(String valor ) {if (valor!=null) Cidade = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getUf()  {return Uf;}
	public void setUf(String valor ) {if (valor!=null) Uf = valor;}


	public void copiar(BairroDt objeto){
		 if (objeto==null) return;
		Id_Bairro = objeto.getId();
		Bairro = objeto.getBairro();
		BairroCodigo = objeto.getBairroCodigo();
		CodigoSPG = objeto.getCodigoSPG();
		Id_Cidade = objeto.getId_Cidade();
		Cidade = objeto.getCidade();
		CodigoTemp = objeto.getCodigoTemp();
		Uf = objeto.getUf();
	}

	public void limpar(){
		Id_Bairro="";
		Bairro="";
		BairroCodigo="";
		CodigoSPG="";
		Id_Cidade="";
		Cidade="";
		CodigoTemp="";
		Uf="";
	}


	public String getPropriedades(){
		return "[Id_Bairro:" + Id_Bairro + ";Bairro:" + Bairro + ";BairroCodigo:" + BairroCodigo + ";CodigoSPG:" + CodigoSPG + ";Id_Cidade:" + Id_Cidade + ";Cidade:" + Cidade + ";CodigoTemp:" + CodigoTemp + ";Uf:" + Uf + "]";
	}


} 
