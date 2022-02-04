package br.gov.go.tj.projudi.dt;

public class PermissaoEspecialDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -3127432531068739258L;
    private String Id_PermissaoEspecial;
	private String PermissaoEspecial;


	private String PermissaoEspecialCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public PermissaoEspecialDtGen() {

		limpar();

	}

	public String getId()  {return Id_PermissaoEspecial;}
	public void setId(String valor ) {if(valor!=null) Id_PermissaoEspecial = valor;}
	public String getPermissaoEspecial()  {return PermissaoEspecial;}
	public void setPermissaoEspecial(String valor ) {if (valor!=null) PermissaoEspecial = valor;}
	public String getPermissaoEspecialCodigo()  {return PermissaoEspecialCodigo;}
	public void setPermissaoEspecialCodigo(String valor ) {if (valor!=null) PermissaoEspecialCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(PermissaoEspecialDt objeto){
		Id_PermissaoEspecial = objeto.getId();
		PermissaoEspecial = objeto.getPermissaoEspecial();
		PermissaoEspecialCodigo = objeto.getPermissaoEspecialCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PermissaoEspecial="";
		PermissaoEspecial="";
		PermissaoEspecialCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PermissaoEspecial:" + Id_PermissaoEspecial + ";PermissaoEspecial:" + PermissaoEspecial + ";PermissaoEspecialCodigo:" + PermissaoEspecialCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
