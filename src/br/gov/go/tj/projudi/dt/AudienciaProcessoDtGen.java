package br.gov.go.tj.projudi.dt;

public class AudienciaProcessoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -4842891061974394563L;
    private String Id_AudienciaProcesso;
	private String AudienciaTipo;

	private String Id_Audiencia;

	private String Id_AudienciaProcessoStatus;
	private String AudienciaProcessoStatus;
	private String Id_ServentiaCargo;
	private String ServentiaCargo;
	private String Id_Processo;
	private String ProcessoNumero;
	private String DataMovimentacao;
	private String CodigoTemp;
	private String AudienciaTipoCodigo;
	private String AudienciaProcessoStatusCodigo;

//---------------------------------------------------------
	public AudienciaProcessoDtGen() {

		limpar();

	}

	public String getId()  {return Id_AudienciaProcesso;}
	public void setId(String valor ) {if(valor!=null) Id_AudienciaProcesso = valor;}
	public String getAudienciaTipo()  {return AudienciaTipo;}
	public void setAudienciaTipo(String valor ) {if (valor!=null) AudienciaTipo = valor;}
	public String getId_Audiencia()  {return Id_Audiencia;}
	public void setId_Audiencia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Audiencia = ""; AudienciaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_Audiencia = valor;}
	public String getId_AudienciaProcessoStatus()  {return Id_AudienciaProcessoStatus;}
	public void setId_AudienciaProcessoStatus(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AudienciaProcessoStatus = ""; AudienciaProcessoStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_AudienciaProcessoStatus = valor;}
	public String getAudienciaProcessoStatus()  {return AudienciaProcessoStatus;}
	public void setAudienciaProcessoStatus(String valor ) {if (valor!=null) AudienciaProcessoStatus = valor;}
	public String getId_ServentiaCargo()  {return Id_ServentiaCargo;}
	public void setId_ServentiaCargo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaCargo = ""; ServentiaCargo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo = valor;}
	public String getServentiaCargo()  {return ServentiaCargo;}
	public void setServentiaCargo(String valor ) {if (valor!=null) ServentiaCargo = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getDataMovimentacao()  {return DataMovimentacao;}
	public void setDataMovimentacao(String valor ) {if (valor!=null) DataMovimentacao = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getAudienciaTipoCodigo()  {return AudienciaTipoCodigo;}
	public void setAudienciaTipoCodigo(String valor ) {if (valor!=null) AudienciaTipoCodigo = valor;}
	public String getAudienciaProcessoStatusCodigo()  {return AudienciaProcessoStatusCodigo;}
	public void setAudienciaProcessoStatusCodigo(String valor ) {if (valor!=null) AudienciaProcessoStatusCodigo = valor;}


	public void copiar(AudienciaProcessoDt objeto){
		Id_AudienciaProcesso = objeto.getId();
		Id_Audiencia = objeto.getId_Audiencia();
		AudienciaTipo = objeto.getAudienciaTipo();
		Id_AudienciaProcessoStatus = objeto.getId_AudienciaProcessoStatus();
		AudienciaProcessoStatus = objeto.getAudienciaProcessoStatus();
		Id_ServentiaCargo = objeto.getId_ServentiaCargo();
		ServentiaCargo = objeto.getServentiaCargo();
		Id_Processo = objeto.getId_Processo();
		ProcessoNumero = objeto.getProcessoNumero();
		DataMovimentacao = objeto.getDataMovimentacao();
		CodigoTemp = objeto.getCodigoTemp();
		AudienciaTipoCodigo = objeto.getAudienciaTipoCodigo();
		AudienciaProcessoStatusCodigo = objeto.getAudienciaProcessoStatusCodigo();
	}

	public void limpar(){
		Id_AudienciaProcesso="";
		Id_Audiencia="";
		AudienciaTipo="";
		Id_AudienciaProcessoStatus="";
		AudienciaProcessoStatus="";
		Id_ServentiaCargo="";
		ServentiaCargo="";
		Id_Processo="";
		ProcessoNumero="";
		DataMovimentacao="";
		CodigoTemp="";
		AudienciaTipoCodigo="";
		AudienciaProcessoStatusCodigo="";
	}


	public String getPropriedades(){
		return "[Id_AudienciaProcesso:" + Id_AudienciaProcesso + ";Id_Audiencia:" + Id_Audiencia + ";AudienciaTipo:" + AudienciaTipo + ";Id_AudienciaProcessoStatus:" + Id_AudienciaProcessoStatus + ";AudienciaProcessoStatus:" + AudienciaProcessoStatus + ";Id_ServentiaCargo:" + Id_ServentiaCargo + ";ServentiaCargo:" + ServentiaCargo + ";Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";DataMovimentacao:" + DataMovimentacao + ";CodigoTemp:" + CodigoTemp + ";AudienciaTipoCodigo:" + AudienciaTipoCodigo + ";AudienciaProcessoStatusCodigo:" + AudienciaProcessoStatusCodigo + "]";
	}


} 
