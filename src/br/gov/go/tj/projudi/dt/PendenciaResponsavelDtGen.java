package br.gov.go.tj.projudi.dt;

public class PendenciaResponsavelDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 2055129738720492637L;
    private String Id_PendenciaResponsavel;
	private String Pendencia;
	
	

	private String Id_Pendencia;

	private String Id_Serventia;
	private String Serventia;
	private String Id_ServentiaTipo;
	private String ServentiaTipo;
	private String Id_UsuarioResponsavel;
	private String UsuarioResponsavel;
	private String Id_ServentiaCargo;
	private String ServentiaCargo;
	

//---------------------------------------------------------
	public PendenciaResponsavelDtGen() {

		limpar();

	}

	public String getId()  {return Id_PendenciaResponsavel;}
	public void setId(String valor ) {if(valor!=null) Id_PendenciaResponsavel = valor;}
	public String getPendencia()  {return Pendencia;}
	public void setPendencia(String valor ) {if (valor!=null) Pendencia = valor;}
	public String getId_Pendencia()  {return Id_Pendencia;}
	public void setId_Pendencia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Pendencia = ""; Pendencia = "";}else if (!valor.equalsIgnoreCase("")) Id_Pendencia = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_ServentiaTipo()  {return Id_ServentiaTipo;}
	public void setId_ServentiaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaTipo = ""; ServentiaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaTipo = valor;}
	public String getServentiaTipo()  {return ServentiaTipo;}
	public void setServentiaTipo(String valor ) {if (valor!=null) ServentiaTipo = valor;}
	public String getId_UsuarioResponsavel()  {return Id_UsuarioResponsavel;}
	public void setId_UsuarioResponsavel(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioResponsavel = ""; UsuarioResponsavel = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioResponsavel = valor;}
	public String getUsuarioResponsavel()  {return UsuarioResponsavel;}
	public void setUsuarioResponsavel(String valor ) {if (valor!=null) UsuarioResponsavel = valor;}
	public String getId_ServentiaCargo()  {return Id_ServentiaCargo;}
	public void setId_ServentiaCargo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaCargo = ""; ServentiaCargo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo = valor;}
	public String getServentiaCargo()  {return ServentiaCargo;}
	public void setServentiaCargo(String valor ) {if (valor!=null) ServentiaCargo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(PendenciaResponsavelDt objeto){
		Id_PendenciaResponsavel = objeto.getId();
		Id_Pendencia = objeto.getId_Pendencia();
		Pendencia = objeto.getPendencia();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		Id_ServentiaTipo = objeto.getId_ServentiaTipo();
		ServentiaTipo = objeto.getServentiaTipo();
		Id_UsuarioResponsavel = objeto.getId_UsuarioResponsavel();
		UsuarioResponsavel = objeto.getUsuarioResponsavel();
		Id_ServentiaCargo = objeto.getId_ServentiaCargo();
		ServentiaCargo = objeto.getServentiaCargo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar() {
		Id_PendenciaResponsavel="";
		Id_Pendencia="";
		Pendencia="";
		Id_Serventia="";
		Serventia="";
		Id_ServentiaTipo="";
		ServentiaTipo="";
		Id_UsuarioResponsavel="";
		UsuarioResponsavel="";
		Id_ServentiaCargo="";
		ServentiaCargo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PendenciaResponsavel:" + Id_PendenciaResponsavel + ";Id_Pendencia:" + Id_Pendencia + ";Pendencia:" + Pendencia + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";Id_ServentiaTipo:" + Id_ServentiaTipo + ";ServentiaTipo:" + ServentiaTipo + ";Id_UsuarioResponsavel:" + Id_UsuarioResponsavel + ";UsuarioResponsavel:" + UsuarioResponsavel + ";Id_ServentiaCargo:" + Id_ServentiaCargo + ";ServentiaCargo:" + ServentiaCargo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
