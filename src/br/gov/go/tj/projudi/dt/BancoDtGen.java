package br.gov.go.tj.projudi.dt;

public class BancoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 3434185570509431804L;
    private String Id_Banco;
	private String Banco;

	private String BancoCodigo;

	private String CodigoTemp;

//---------------------------------------------------------
	public BancoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Banco;}
	public void setId(String valor ) {if(valor!=null) Id_Banco = valor;}
	public String getBanco()  {return Banco;}
	public void setBanco(String valor ) {if (valor!=null) Banco = valor;}
	public String getBancoCodigo()  {return BancoCodigo;}
	public void setBancoCodigo(String valor ) {if (valor!=null) BancoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(BancoDt objeto){
		Id_Banco = objeto.getId();
		BancoCodigo = objeto.getBancoCodigo();
		Banco = objeto.getBanco();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Banco="";
		BancoCodigo="";
		Banco="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Banco:" + Id_Banco + ";BancoCodigo:" + BancoCodigo + ";Banco:" + Banco + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
