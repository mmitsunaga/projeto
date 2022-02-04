package br.gov.go.tj.projudi.dt;

public class GrupoArquivoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 5673664257948889451L;
    private String Id_GrupoArquivoTipo;
	private String Grupo;
	
	

	private String Id_Grupo;

	private String Id_ArquivoTipo;
	private String ArquivoTipo;
	
	private String GrupoCodigo;
	private String ArquivoTipoCodigo;

//---------------------------------------------------------
	public GrupoArquivoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_GrupoArquivoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_GrupoArquivoTipo = valor;}
	public String getGrupo()  {return Grupo;}
	public void setGrupo(String valor ) {if (valor!=null) Grupo = valor;}
	public String getId_Grupo()  {return Id_Grupo;}
	public void setId_Grupo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Grupo = ""; Grupo = "";}else if (!valor.equalsIgnoreCase("")) Id_Grupo = valor;}
	public String getId_ArquivoTipo()  {return Id_ArquivoTipo;}
	public void setId_ArquivoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ArquivoTipo = ""; ArquivoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ArquivoTipo = valor;}
	public String getArquivoTipo()  {return ArquivoTipo;}
	public void setArquivoTipo(String valor ) {if (valor!=null) ArquivoTipo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getGrupoCodigo()  {return GrupoCodigo;}
	public void setGrupoCodigo(String valor ) {if (valor!=null) GrupoCodigo = valor;}
	public String getArquivoTipoCodigo()  {return ArquivoTipoCodigo;}
	public void setArquivoTipoCodigo(String valor ) {if (valor!=null) ArquivoTipoCodigo = valor;}
	
	
	
	

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_ArquivoTipo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getArquivoTipo() + "</strong>  </li>  ";
		return stTemp;
	}

	public void copiar(GrupoArquivoTipoDt objeto){
		Id_GrupoArquivoTipo = objeto.getId();
		Id_Grupo = objeto.getId_Grupo();
		Grupo = objeto.getGrupo();
		Id_ArquivoTipo = objeto.getId_ArquivoTipo();
		ArquivoTipo = objeto.getArquivoTipo();
		CodigoTemp = objeto.getCodigoTemp();
		GrupoCodigo = objeto.getGrupoCodigo();
		ArquivoTipoCodigo = objeto.getArquivoTipoCodigo();
	}

	public void limpar(){
		Id_GrupoArquivoTipo="";
		Id_Grupo="";
		Grupo="";
		Id_ArquivoTipo="";
		ArquivoTipo="";
		CodigoTemp="";
		GrupoCodigo="";
		ArquivoTipoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_GrupoArquivoTipo:" + Id_GrupoArquivoTipo + ";Id_Grupo:" + Id_Grupo + ";Grupo:" + Grupo + ";Id_ArquivoTipo:" + Id_ArquivoTipo + ";ArquivoTipo:" + ArquivoTipo + ";CodigoTemp:" + CodigoTemp + ";GrupoCodigo:" + GrupoCodigo + ";ArquivoTipoCodigo:" + ArquivoTipoCodigo + "]";
	}


} 
