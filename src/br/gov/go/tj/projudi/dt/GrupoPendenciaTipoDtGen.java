package br.gov.go.tj.projudi.dt;

public class GrupoPendenciaTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -6622191274164622029L;
    private String Id_GrupoPendenciaTipo;
	private String Grupo;
	
	

	private String Id_Grupo;

	private String Id_PendenciaTipo;
	private String PendenciaTipo;
	
	private String GrupoCodigo;
	private String PendenciaTipoCodigo;

//---------------------------------------------------------
	public GrupoPendenciaTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_GrupoPendenciaTipo;}
	public void setId(String valor ) {if(valor!=null) Id_GrupoPendenciaTipo = valor;}
	public String getGrupo()  {return Grupo;}
	public void setGrupo(String valor ) {if (valor!=null) Grupo = valor;}
	public String getId_Grupo()  {return Id_Grupo;}
	public void setId_Grupo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Grupo = ""; Grupo = "";}else if (!valor.equalsIgnoreCase("")) Id_Grupo = valor;}
	public String getId_PendenciaTipo()  {return Id_PendenciaTipo;}
	public void setId_PendenciaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaTipo = ""; PendenciaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_PendenciaTipo = valor;}
	public String getPendenciaTipo()  {return PendenciaTipo;}
	public void setPendenciaTipo(String valor ) {if (valor!=null) PendenciaTipo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getGrupoCodigo()  {return GrupoCodigo;}
	public void setGrupoCodigo(String valor ) {if (valor!=null) GrupoCodigo = valor;}
	public String getPendenciaTipoCodigo()  {return PendenciaTipoCodigo;}
	public void setPendenciaTipoCodigo(String valor ) {if (valor!=null) PendenciaTipoCodigo = valor;}
	
	
	
	

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_PendenciaTipo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getPendenciaTipo() + "</strong>  </li>  ";
		return stTemp;
	}

	public void copiar(GrupoPendenciaTipoDt objeto){
		Id_GrupoPendenciaTipo = objeto.getId();
		Id_Grupo = objeto.getId_Grupo();
		Grupo = objeto.getGrupo();
		Id_PendenciaTipo = objeto.getId_PendenciaTipo();
		PendenciaTipo = objeto.getPendenciaTipo();
		CodigoTemp = objeto.getCodigoTemp();
		GrupoCodigo = objeto.getGrupoCodigo();
		PendenciaTipoCodigo = objeto.getPendenciaTipoCodigo();
	}

	public void limpar(){
		Id_GrupoPendenciaTipo="";
		Id_Grupo="";
		Grupo="";
		Id_PendenciaTipo="";
		PendenciaTipo="";
		CodigoTemp="";
		GrupoCodigo="";
		PendenciaTipoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_GrupoPendenciaTipo:" + Id_GrupoPendenciaTipo + ";Id_Grupo:" + Id_Grupo + ";Grupo:" + Grupo + ";Id_PendenciaTipo:" + Id_PendenciaTipo + ";PendenciaTipo:" + PendenciaTipo + ";CodigoTemp:" + CodigoTemp + ";GrupoCodigo:" + GrupoCodigo + ";PendenciaTipoCodigo:" + PendenciaTipoCodigo + "]";
	}


} 
