package br.gov.go.tj.projudi.dt;

public class MandadoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -7514553995298509369L;
    private String Id_MandadoTipo;
	private String MandadoTipo;


	private String MandadoTipoCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public MandadoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_MandadoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_MandadoTipo = valor;}
	public String getMandadoTipo()  {return MandadoTipo;}
	public void setMandadoTipo(String valor ) {if (valor!=null) MandadoTipo = valor;}
	public String getMandadoTipoCodigo()  {return MandadoTipoCodigo;}
	public void setMandadoTipoCodigo(String valor ) {if (valor!=null) MandadoTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(MandadoTipoDt objeto){
		Id_MandadoTipo = objeto.getId();
		MandadoTipo = objeto.getMandadoTipo();
		MandadoTipoCodigo = objeto.getMandadoTipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MandadoTipo="";
		MandadoTipo="";
		MandadoTipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MandadoTipo:" + Id_MandadoTipo + ";MandadoTipo:" + MandadoTipo + ";MandadoTipoCodigo:" + MandadoTipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
