package br.gov.go.tj.projudi.dt;

public class AgenciaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 1136981656475253987L;
    private String Id_Agencia;
	private String Agencia;


	private String AgenciaCodigo;
	private String Id_Banco;
	private String Banco;
	private String CodigoTemp;
	private String BancoCodigo;

//---------------------------------------------------------
	public AgenciaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Agencia;}
	public void setId(String valor ) {if(valor!=null) Id_Agencia = valor;}
	public String getAgencia()  {return Agencia;}
	public void setAgencia(String valor ) {if (valor!=null) Agencia = valor;}
	public String getAgenciaCodigo()  {return AgenciaCodigo;}
	public void setAgenciaCodigo(String valor ) {if (valor!=null) AgenciaCodigo = valor;}
	public String getId_Banco()  {return Id_Banco;}
	public void setId_Banco(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Banco = ""; Banco = "";}else if (!valor.equalsIgnoreCase("")) Id_Banco = valor;}
	public String getBanco()  {return Banco;}
	public void setBanco(String valor ) {if (valor!=null) Banco = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getBancoCodigo()  {return BancoCodigo;}
	public void setBancoCodigo(String valor ) {if (valor!=null) BancoCodigo = valor;}


	public void copiar(AgenciaDt objeto){
		Id_Agencia = objeto.getId();
		Agencia = objeto.getAgencia();
		AgenciaCodigo = objeto.getAgenciaCodigo();
		Id_Banco = objeto.getId_Banco();
		Banco = objeto.getBanco();
		CodigoTemp = objeto.getCodigoTemp();
		BancoCodigo = objeto.getBancoCodigo();
	}

	public void limpar(){
		Id_Agencia="";
		Agencia="";
		AgenciaCodigo="";
		Id_Banco="";
		Banco="";
		CodigoTemp="";
		BancoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_Agencia:" + Id_Agencia + ";Agencia:" + Agencia + ";AgenciaCodigo:" + AgenciaCodigo + ";Id_Banco:" + Id_Banco + ";Banco:" + Banco + ";CodigoTemp:" + CodigoTemp + ";BancoCodigo:" + BancoCodigo + "]";
	}


} 
