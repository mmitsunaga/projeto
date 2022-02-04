package br.gov.go.tj.projudi.dt;

public class GovernoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -3404806445435574533L;
    private String Id_GovernoTipo;
	private String GovernoTipo;

	private String GovernoTipoCodigo;

	private String CodigoTemp;

//---------------------------------------------------------
	public GovernoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_GovernoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_GovernoTipo = valor;}
	public String getGovernoTipo()  {return GovernoTipo;}
	public void setGovernoTipo(String valor ) {if (valor!=null) GovernoTipo = valor;}
	public String getGovernoTipoCodigo()  {return GovernoTipoCodigo;}
	public void setGovernoTipoCodigo(String valor ) {if (valor!=null) GovernoTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(GovernoTipoDt objeto){
		 if (objeto==null) return;
		Id_GovernoTipo = objeto.getId();
		GovernoTipoCodigo = objeto.getGovernoTipoCodigo();
		GovernoTipo = objeto.getGovernoTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_GovernoTipo="";
		GovernoTipoCodigo="";
		GovernoTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_GovernoTipo:" + Id_GovernoTipo + ";GovernoTipoCodigo:" + GovernoTipoCodigo + ";GovernoTipo:" + GovernoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
