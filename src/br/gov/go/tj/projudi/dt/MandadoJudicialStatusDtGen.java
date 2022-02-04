package br.gov.go.tj.projudi.dt;

public class MandadoJudicialStatusDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -1589539182235101447L;
    private String Id_MandadoJudicialStatus;
	private String MandadoJudicialStatus;


	private String MandadoJudicialStatusCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public MandadoJudicialStatusDtGen() {

		limpar();

	}

	public String getId()  {return Id_MandadoJudicialStatus;}
	public void setId(String valor ) {if(valor!=null) Id_MandadoJudicialStatus = valor;}
	public String getMandadoJudicialStatus()  {return MandadoJudicialStatus;}
	public void setMandadoJudicialStatus(String valor ) {if (valor!=null) MandadoJudicialStatus = valor;}
	public String getMandadoJudicialStatusCodigo()  {return MandadoJudicialStatusCodigo;}
	public void setMandadoJudicialStatusCodigo(String valor ) {if (valor!=null) MandadoJudicialStatusCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(MandadoJudicialStatusDt objeto){
		Id_MandadoJudicialStatus = objeto.getId();
		MandadoJudicialStatus = objeto.getMandadoJudicialStatus();
		MandadoJudicialStatusCodigo = objeto.getMandadoJudicialStatusCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MandadoJudicialStatus="";
		MandadoJudicialStatus="";
		MandadoJudicialStatusCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MandadoJudicialStatus:" + Id_MandadoJudicialStatus + ";MandadoJudicialStatus:" + MandadoJudicialStatus + ";MandadoJudicialStatusCodigo:" + MandadoJudicialStatusCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
