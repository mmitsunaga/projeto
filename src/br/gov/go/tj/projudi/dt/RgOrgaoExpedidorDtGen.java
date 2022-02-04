package br.gov.go.tj.projudi.dt;

public class RgOrgaoExpedidorDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -364782893139583409L;
    private String Id_RgOrgaoExpedidor;
	private String RgOrgaoExpedidor;
	
	


	private String Sigla;
	private String Id_Estado;
	private String Estado;
	
	private String EstadoCodigo;
	private String Uf;

//---------------------------------------------------------
	public RgOrgaoExpedidorDtGen() {

		limpar();

	}

	public String getId()  {return Id_RgOrgaoExpedidor;}
	public void setId(String valor ) {if(valor!=null) Id_RgOrgaoExpedidor = valor;}
	public String getRgOrgaoExpedidor()  {return RgOrgaoExpedidor;}
	public void setRgOrgaoExpedidor(String valor ) {if (valor!=null) RgOrgaoExpedidor = valor;}
	public String getSigla()  {return Sigla;}
	public void setSigla(String valor ) {if (valor!=null) Sigla = valor;}
	public String getId_Estado()  {return Id_Estado;}
	public void setId_Estado(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Estado = ""; Estado = "";}else if (!valor.equalsIgnoreCase("")) Id_Estado = valor;}
	public String getEstado()  {return Estado;}
	public void setEstado(String valor ) {if (valor!=null) Estado = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getEstadoCodigo()  {return EstadoCodigo;}
	public void setEstadoCodigo(String valor ) {if (valor!=null) EstadoCodigo = valor;}
	public String getUf()  {return Uf;}
	public void setUf(String valor ) {if (valor!=null) Uf = valor;}
	
	
	
	


	public void copiar(RgOrgaoExpedidorDt objeto){
		Id_RgOrgaoExpedidor = objeto.getId();
		RgOrgaoExpedidor = objeto.getRgOrgaoExpedidor();
		Sigla = objeto.getSigla();
		Id_Estado = objeto.getId_Estado();
		Estado = objeto.getEstado();
		CodigoTemp = objeto.getCodigoTemp();
		EstadoCodigo = objeto.getEstadoCodigo();
		Uf = objeto.getUf();
	}

	public void limpar(){
		Id_RgOrgaoExpedidor="";
		RgOrgaoExpedidor="";
		Sigla="";
		Id_Estado="";
		Estado="";
		CodigoTemp="";
		EstadoCodigo="";
		Uf="";
	}


	public String getPropriedades(){
		return "[Id_RgOrgaoExpedidor:" + Id_RgOrgaoExpedidor + ";RgOrgaoExpedidor:" + RgOrgaoExpedidor + ";Sigla:" + Sigla + ";Id_Estado:" + Id_Estado + ";Estado:" + Estado + ";CodigoTemp:" + CodigoTemp + ";EstadoCodigo:" + EstadoCodigo + ";Uf:" + Uf + "]";
	}


} 
