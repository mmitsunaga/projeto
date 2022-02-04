package br.gov.go.tj.projudi.dt;

public class PendenciaStatusDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -6454934348575233606L;
    private String Id_PendenciaStatus;
	private String PendenciaStatus;


	private String PendenciaStatusCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public PendenciaStatusDtGen() {

		limpar();

	}

	public String getId()  {return Id_PendenciaStatus;}
	public void setId(String valor ) {if(valor!=null) Id_PendenciaStatus = valor;}
	public String getPendenciaStatus()  {return PendenciaStatus;}
	public void setPendenciaStatus(String valor ) {if (valor!=null) PendenciaStatus = valor;}
	public String getPendenciaStatusCodigo()  {return PendenciaStatusCodigo;}
	public void setPendenciaStatusCodigo(String valor ) {if (valor!=null) PendenciaStatusCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(PendenciaStatusDt objeto){
		Id_PendenciaStatus = objeto.getId();
		PendenciaStatus = objeto.getPendenciaStatus();
		PendenciaStatusCodigo = objeto.getPendenciaStatusCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PendenciaStatus="";
		PendenciaStatus="";
		PendenciaStatusCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PendenciaStatus:" + Id_PendenciaStatus + ";PendenciaStatus:" + PendenciaStatus + ";PendenciaStatusCodigo:" + PendenciaStatusCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
