package br.gov.go.tj.projudi.dt;

public class EnderecoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 5195583919857264319L;
    private String Id_Endereco;
	private String Logradouro;
	
	


	private String Numero;
	private String Complemento;
	private String Cep;
	private String Id_Bairro;
	private String Bairro;
	
	private String BairroCodigo;
	private String Id_Cidade;
	private String Cidade;
	private String CidadeCodigo;
	private String EstadoCodigo;
	private String Uf;

//---------------------------------------------------------
	public EnderecoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Endereco;}
	public void setId(String valor ) {if(valor!=null) Id_Endereco = valor;}
	public String getLogradouro()  {return Logradouro;}
	public void setLogradouro(String valor ) {if (valor!=null) Logradouro = valor;}
	public String getNumero()  {return Numero;}
	public void setNumero(String valor ) {if (valor!=null) Numero = valor;}
	public String getComplemento()  {return Complemento;}
	public void setComplemento(String valor ) {if (valor!=null) Complemento = valor;}
	public String getCep()  {return Cep;}
	public void setCep(String valor ) {if (valor!=null) Cep = valor;}
	public String getId_Bairro()  {return Id_Bairro;}
	public void setId_Bairro(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Bairro = ""; Bairro = "";}else if (!valor.equalsIgnoreCase("")) Id_Bairro = valor;}
	public String getBairro()  {return Bairro;}
	public void setBairro(String valor ) {if (valor!=null) Bairro = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getBairroCodigo()  {return BairroCodigo;}
	public void setBairroCodigo(String valor ) {if (valor!=null) BairroCodigo = valor;}
	public String getId_Cidade()  {return Id_Cidade;}
	public void setId_Cidade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Cidade = ""; Cidade = "";}else if (!valor.equalsIgnoreCase("")) Id_Cidade = valor;}
	public String getCidade()  {return Cidade;}
	public void setCidade(String valor ) {if (valor!=null) Cidade = valor;}
	public String getCidadeCodigo()  {return CidadeCodigo;}
	public void setCidadeCodigo(String valor ) {if (valor!=null) CidadeCodigo = valor;}
	public String getEstadoCodigo()  {return EstadoCodigo;}
	public void setEstadoCodigo(String valor ) {if (valor!=null) EstadoCodigo = valor;}
	public String getUf()  {return Uf;}
	public void setUf(String valor ) {if (valor!=null) Uf = valor;}
	
	
	
	


	public void copiar(EnderecoDt objeto){
		Id_Endereco = objeto.getId();
		Logradouro = objeto.getLogradouro();
		Numero = objeto.getNumero();
		Complemento = objeto.getComplemento();
		Cep = objeto.getCep();
		Id_Bairro = objeto.getId_Bairro();
		Bairro = objeto.getBairro();
		CodigoTemp = objeto.getCodigoTemp();
		BairroCodigo = objeto.getBairroCodigo();
		Id_Cidade = objeto.getId_Cidade();
		Cidade = objeto.getCidade();
		CidadeCodigo = objeto.getCidadeCodigo();
		EstadoCodigo = objeto.getEstadoCodigo();
		Uf = objeto.getUf();
	}

	public void limpar(){
		Id_Endereco="";
		Logradouro="";
		Numero="";
		Complemento="";
		Cep="";
		Id_Bairro="";
		Bairro="";
		CodigoTemp="";
		BairroCodigo="";
		Id_Cidade="";
		Cidade="";
		CidadeCodigo="";
		EstadoCodigo="";
		Uf="";
	}


	public String getPropriedades(){
		return "[Id_Endereco:" + Id_Endereco + ";Logradouro:" + Logradouro + ";Numero:" + Numero + ";Complemento:" + Complemento + ";Cep:" + Cep + ";Id_Bairro:" + Id_Bairro + ";Bairro:" + Bairro + ";CodigoTemp:" + CodigoTemp + ";BairroCodigo:" + BairroCodigo + ";Id_Cidade:" + Id_Cidade + ";Cidade:" + Cidade + ";CidadeCodigo:" + CidadeCodigo + ";EstadoCodigo:" + EstadoCodigo + ";Uf:" + Uf + "]";
	}


} 
