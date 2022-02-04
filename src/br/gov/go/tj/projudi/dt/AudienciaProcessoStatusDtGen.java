package br.gov.go.tj.projudi.dt;

public class AudienciaProcessoStatusDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 363939614873961495L;
	private String Id_AudienciaProcessoStatus;
	private String AudienciaProcessoStatus;


	private String AudienciaProcessoStatusCodigo;
	private String Id_ServentiaTipo;
	private String ServentiaTipo;
	private String CodigoTemp;
	private String ServentiaTipoCodigo;

//---------------------------------------------------------
	public AudienciaProcessoStatusDtGen() {

		limpar();

	}

	public String getId()  {return Id_AudienciaProcessoStatus;}
	public void setId(String valor ) {if(valor!=null) Id_AudienciaProcessoStatus = valor;}
	public String getAudienciaProcessoStatus()  {return AudienciaProcessoStatus;}
	public void setAudienciaProcessoStatus(String valor ) {if (valor!=null) AudienciaProcessoStatus = valor;}
	public String getAudienciaProcessoStatusCodigo()  {return AudienciaProcessoStatusCodigo;}
	public void setAudienciaProcessoStatusCodigo(String valor ) {if (valor!=null) AudienciaProcessoStatusCodigo = valor;}
	public String getId_ServentiaTipo()  {return Id_ServentiaTipo;}
	public void setId_ServentiaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaTipo = ""; ServentiaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaTipo = valor;}
	public String getServentiaTipo()  {return ServentiaTipo;}
	public void setServentiaTipo(String valor ) {if (valor!=null) ServentiaTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getServentiaTipoCodigo()  {return ServentiaTipoCodigo;}
	public void setServentiaTipoCodigo(String valor ) {if (valor!=null) ServentiaTipoCodigo = valor;}


	public void copiar(AudienciaProcessoStatusDt objeto){
		 if (objeto==null) return;
		Id_AudienciaProcessoStatus = objeto.getId();
		AudienciaProcessoStatus = objeto.getAudienciaProcessoStatus();
		AudienciaProcessoStatusCodigo = objeto.getAudienciaProcessoStatusCodigo();
		Id_ServentiaTipo = objeto.getId_ServentiaTipo();
		ServentiaTipo = objeto.getServentiaTipo();
		CodigoTemp = objeto.getCodigoTemp();
		ServentiaTipoCodigo = objeto.getServentiaTipoCodigo();
	}

	public void limpar(){
		Id_AudienciaProcessoStatus="";
		AudienciaProcessoStatus="";
		AudienciaProcessoStatusCodigo="";
		Id_ServentiaTipo="";
		ServentiaTipo="";
		CodigoTemp="";
		ServentiaTipoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_AudienciaProcessoStatus:" + Id_AudienciaProcessoStatus + ";AudienciaProcessoStatus:" + AudienciaProcessoStatus + ";AudienciaProcessoStatusCodigo:" + AudienciaProcessoStatusCodigo + ";Id_ServentiaTipo:" + Id_ServentiaTipo + ";ServentiaTipo:" + ServentiaTipo + ";CodigoTemp:" + CodigoTemp + ";ServentiaTipoCodigo:" + ServentiaTipoCodigo + "]";
	}


} 
