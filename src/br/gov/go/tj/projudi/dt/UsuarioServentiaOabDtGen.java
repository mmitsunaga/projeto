package br.gov.go.tj.projudi.dt;

public class UsuarioServentiaOabDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -2081071474161079403L;
    private String Id_UsuarioServentiaOab;
	private String OabNumero;
	
	


	private String Id_UsuarioServentia;
	private String OabComplemento;
	

//---------------------------------------------------------
	public UsuarioServentiaOabDtGen() {

		limpar();

	}

	public String getId()  {return Id_UsuarioServentiaOab;}
	public void setId(String valor ) {if(valor!=null) Id_UsuarioServentiaOab = valor;}
	public String getOabNumero()  {return OabNumero;}
	public void setOabNumero(String valor ) {if (valor!=null) OabNumero = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia = ""; OabComplemento = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getOabComplemento()  {return OabComplemento;}
	public void setOabComplemento(String valor ) {if (valor!=null) OabComplemento = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(UsuarioServentiaOabDt objeto){
		Id_UsuarioServentiaOab = objeto.getId();
		OabNumero = objeto.getOabNumero();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		OabComplemento = objeto.getOabComplemento();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_UsuarioServentiaOab="";
		OabNumero="";
		Id_UsuarioServentia="";
		OabComplemento="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_UsuarioServentiaOab:" + Id_UsuarioServentiaOab + ";OabNumero:" + OabNumero + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";OabComplemento:" + OabComplemento + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
