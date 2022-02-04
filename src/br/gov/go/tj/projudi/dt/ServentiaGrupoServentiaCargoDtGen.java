package br.gov.go.tj.projudi.dt;

public class ServentiaGrupoServentiaCargoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -996410293228806582L;
	private String Id_ServentiaCargoServentiaGrupo;
	private String ServentiaCargoServentiaGrupo;


	private String Id_ServentiaCargo;
	private String ServentiaCargo;
	private String Id_ServentiaGrupo;
	private String ServentiaGrupo;
	private String CodigoTemp;

//---------------------------------------------------------
	public ServentiaGrupoServentiaCargoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaCargoServentiaGrupo;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaCargoServentiaGrupo = valor;}
	public String getServentiaCargoServentiaGrupo()  {return ServentiaCargoServentiaGrupo;}
	public void setServentiaCargoServentiaGrupo(String valor ) {if (valor!=null) ServentiaCargoServentiaGrupo = valor;}
	public String getId_ServentiaCargo()  {return Id_ServentiaCargo;}
	public void setId_ServentiaCargo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaCargo = ""; ServentiaCargo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo = valor;}
	public String getServentiaCargo()  {return ServentiaCargo;}
	public void setServentiaCargo(String valor ) {if (valor!=null) ServentiaCargo = valor;}
	public String getId_ServentiaGrupo()  {return Id_ServentiaGrupo;}
	public void setId_ServentiaGrupo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaGrupo = ""; ServentiaGrupo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaGrupo = valor;}
	public String getServentiaGrupo()  {return ServentiaGrupo;}
	public void setServentiaGrupo(String valor ) {if (valor!=null) ServentiaGrupo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_ServentiaCargo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getServentiaCargo() + "</strong>  </li>  ";
		return stTemp;
	}

	public void copiar(ServentiaGrupoServentiaCargoDt objeto){
		 if (objeto==null) return;
		Id_ServentiaCargoServentiaGrupo = objeto.getId();
		ServentiaCargoServentiaGrupo = objeto.getServentiaCargoServentiaGrupo();
		Id_ServentiaCargo = objeto.getId_ServentiaCargo();
		ServentiaCargo = objeto.getServentiaCargo();
		Id_ServentiaGrupo = objeto.getId_ServentiaGrupo();
		ServentiaGrupo = objeto.getServentiaGrupo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ServentiaCargoServentiaGrupo="";
		ServentiaCargoServentiaGrupo="";
		Id_ServentiaCargo="";
		ServentiaCargo="";
		Id_ServentiaGrupo="";
		ServentiaGrupo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ServentiaCargoServentiaGrupo:" + Id_ServentiaCargoServentiaGrupo + ";ServentiaCargoServentiaGrupo:" + ServentiaCargoServentiaGrupo + ";Id_ServentiaCargo:" + Id_ServentiaCargo + ";ServentiaCargo:" + ServentiaCargo + ";Id_ServentiaGrupo:" + Id_ServentiaGrupo + ";ServentiaGrupo:" + ServentiaGrupo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
