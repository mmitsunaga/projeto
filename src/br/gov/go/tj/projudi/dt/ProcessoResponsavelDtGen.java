package br.gov.go.tj.projudi.dt;

public class ProcessoResponsavelDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -1179455422778059884L;
    private String Id_ProcessoResponsavel;
	private String ServentiaCargo;

	private String Id_ServentiaCargo;

	private String Id_Processo;
	private String ProcessoNumero;
	private String Id_CargoTipo;
	private String CargoTipo;
	private String CodigoTemp;
	private String Id_Grupo;
	private String GrupoCodigo;
	private String CargoTipoCodigo;
	
	//Para procuradores
	private String Id_UsuarioServentia;
	private String UsuarioServentia;

	//---------------------------------------------------------
	public ProcessoResponsavelDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoResponsavel;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoResponsavel = valor;}
	public String getServentiaCargo()  {return ServentiaCargo;}
	public void setServentiaCargo(String valor ) {if (valor!=null) ServentiaCargo = valor;}
	public String getId_ServentiaCargo()  {return Id_ServentiaCargo;}
	public void setId_ServentiaCargo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaCargo = ""; ServentiaCargo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getId_CargoTipo()  {return Id_CargoTipo;}
	public void setId_CargoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CargoTipo = ""; CargoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_CargoTipo = valor;}
	public String getCargoTipo()  {return CargoTipo;}
	public void setCargoTipo(String valor ) {if (valor!=null) CargoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getId_Grupo()  {return Id_Grupo;}
	public void setId_Grupo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Grupo = ""; GrupoCodigo = "";}else if (!valor.equalsIgnoreCase("")) Id_Grupo = valor;}
	public String getGrupoCodigo()  {return GrupoCodigo;}
	public void setGrupoCodigo(String valor ) {if (valor!=null) GrupoCodigo = valor;}
	public String getCargoTipoCodigo()  {return CargoTipoCodigo;}
	public void setCargoTipoCodigo(String valor ) {if (valor!=null) CargoTipoCodigo = valor;}

	public String getId_UsuarioServentia() {
		return Id_UsuarioServentia;
	}

	public void setId_UsuarioServentia(String idUsuarioServentia) {
		if(idUsuarioServentia != null)
			Id_UsuarioServentia = idUsuarioServentia;
	}

	public String getUsuarioServentia() {
		return UsuarioServentia;
	}

	public void setUsuarioServentia(String usuarioServentia) {
		if(usuarioServentia != null)
			UsuarioServentia = usuarioServentia;
	}

	public void copiar(ProcessoResponsavelDt objeto){
		 if (objeto==null) return;
		Id_ProcessoResponsavel = objeto.getId();
		Id_ServentiaCargo = objeto.getId_ServentiaCargo();
		ServentiaCargo = objeto.getServentiaCargo();
		Id_Processo = objeto.getId_Processo();
		ProcessoNumero = objeto.getProcessoNumero();
		Id_CargoTipo = objeto.getId_CargoTipo();
		CargoTipo = objeto.getCargoTipo();
		CodigoTemp = objeto.getCodigoTemp();
		Id_Grupo = objeto.getId_Grupo();
		GrupoCodigo = objeto.getGrupoCodigo();
		CargoTipoCodigo = objeto.getCargoTipoCodigo();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		UsuarioServentia = objeto.getUsuarioServentia();
	}

	public void limpar(){
		Id_ProcessoResponsavel="";
		Id_ServentiaCargo="";
		ServentiaCargo="";
		Id_Processo="";
		ProcessoNumero="";
		Id_CargoTipo="";
		CargoTipo="";
		CodigoTemp="";
		Id_Grupo="";
		GrupoCodigo="";
		CargoTipoCodigo="";
		Id_UsuarioServentia="";
		UsuarioServentia="";
	}


	public String getPropriedades(){
	return "[Id_ProcessoResponsavel:" + Id_ProcessoResponsavel + ";Id_ServentiaCargo:" + Id_ServentiaCargo + ";ServentiaCargo:" + ServentiaCargo + ";Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";Id_CargoTipo:" + Id_CargoTipo + ";CargoTipo:" + CargoTipo + ";CodigoTemp:" + CodigoTemp + ";Id_Grupo:" + Id_Grupo + ";GrupoCodigo:" + GrupoCodigo + ";CargoTipoCodigo:" + CargoTipoCodigo + ";Id_UsuarioServentia:"+Id_UsuarioServentia+";UsuarioServentia:"+UsuarioServentia+"]";
	}


} 
