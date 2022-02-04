package br.gov.go.tj.projudi.dt;

public class LocalCumprimentoPenaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 6706766482086583168L;
    private String Id_LocalCumprimentoPena;
	private String LocalCumprimentoPena;


	private String Id_Endereco;
	private String Endereco;
	private String CodigoTemp;

//---------------------------------------------------------
	public LocalCumprimentoPenaDtGen() {

		limpar();

	}

	public String getId()  {return Id_LocalCumprimentoPena;}
	public void setId(String valor ) {if(valor!=null) Id_LocalCumprimentoPena = valor;}
	public String getLocalCumprimentoPena()  {return LocalCumprimentoPena;}
	public void setLocalCumprimentoPena(String valor ) {if (valor!=null) LocalCumprimentoPena = valor;}
	public String getId_Endereco()  {return Id_Endereco;}
	public void setId_Endereco(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Endereco = ""; Endereco = "";}else if (!valor.equalsIgnoreCase("")) Id_Endereco = valor;}
	public String getEndereco()  {return Endereco;}
	public void setEndereco(String valor ) {if (valor!=null) Endereco = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(LocalCumprimentoPenaDt objeto){
		Id_LocalCumprimentoPena = objeto.getId();
		LocalCumprimentoPena = objeto.getLocalCumprimentoPena();
		Id_Endereco = objeto.getId_Endereco();
		Endereco = objeto.getEndereco();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_LocalCumprimentoPena="";
		LocalCumprimentoPena="";
		Id_Endereco="";
		Endereco="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_LocalCumprimentoPena:" + Id_LocalCumprimentoPena + ";LocalCumprimentoPena:" + LocalCumprimentoPena + ";Id_Endereco:" + Id_Endereco + ";Endereco:" + Endereco + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
