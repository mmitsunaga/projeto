package br.gov.go.tj.projudi.dt;

public class GrupoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -1848018348582802552L;
    private String Id_Grupo;
	private String Grupo;


	private String GrupoCodigo;
	private String Id_ServentiaTipo;
	private String ServentiaTipo;
	private String Id_GrupoTipo;
	private String GrupoTipo;
	private String CodigoTemp;
	private String ServentiaTipoCodigo;
	private String GrupoTipoCodigo;

//---------------------------------------------------------
	public GrupoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Grupo;}
	public void setId(String valor ) {if(valor!=null) Id_Grupo = valor;}
	public String getGrupo()  {return Grupo;}
	public void setGrupo(String valor ) {if (valor!=null) Grupo = valor;}
	public String getGrupoCodigo()  {return GrupoCodigo;}
	public void setGrupoCodigo(String valor ) {if (valor!=null) GrupoCodigo = valor;}
	public String getId_ServentiaTipo()  {return Id_ServentiaTipo;}
	public void setId_ServentiaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaTipo = ""; ServentiaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaTipo = valor;}
	public String getServentiaTipo()  {return ServentiaTipo;}
	public void setServentiaTipo(String valor ) {if (valor!=null) ServentiaTipo = valor;}
	public String getId_GrupoTipo()  {return Id_GrupoTipo;}
	public void setId_GrupoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GrupoTipo = ""; GrupoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_GrupoTipo = valor;}
	public String getGrupoTipo()  {return GrupoTipo;}
	public void setGrupoTipo(String valor ) {if (valor!=null) GrupoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getServentiaTipoCodigo()  {return ServentiaTipoCodigo;}
	public void setServentiaTipoCodigo(String valor ) {if (valor!=null) ServentiaTipoCodigo = valor;}
	public String getGrupoTipoCodigo()  {return GrupoTipoCodigo;}
	public void setGrupoTipoCodigo(String valor ) {if (valor!=null) GrupoTipoCodigo = valor;}


	public void copiar(GrupoDt objeto){
		 if (objeto==null) return;
		Id_Grupo = objeto.getId();
		Grupo = objeto.getGrupo();
		GrupoCodigo = objeto.getGrupoCodigo();
		Id_ServentiaTipo = objeto.getId_ServentiaTipo();
		ServentiaTipo = objeto.getServentiaTipo();
		Id_GrupoTipo = objeto.getId_GrupoTipo();
		GrupoTipo = objeto.getGrupoTipo();
		CodigoTemp = objeto.getCodigoTemp();
		ServentiaTipoCodigo = objeto.getServentiaTipoCodigo();
		GrupoTipoCodigo = objeto.getGrupoTipoCodigo();
	}

	public void limpar(){
		Id_Grupo="";
		Grupo="";
		GrupoCodigo="";
		Id_ServentiaTipo="";
		ServentiaTipo="";
		Id_GrupoTipo="";
		GrupoTipo="";
		CodigoTemp="";
		ServentiaTipoCodigo="";
		GrupoTipoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_Grupo:" + Id_Grupo + ";Grupo:" + Grupo + ";GrupoCodigo:" + GrupoCodigo + ";Id_ServentiaTipo:" + Id_ServentiaTipo + ";ServentiaTipo:" + ServentiaTipo + ";Id_GrupoTipo:" + Id_GrupoTipo + ";GrupoTipo:" + GrupoTipo + ";CodigoTemp:" + CodigoTemp + ";ServentiaTipoCodigo:" + ServentiaTipoCodigo + ";GrupoTipoCodigo:" + GrupoTipoCodigo + "]";
	}


} 
