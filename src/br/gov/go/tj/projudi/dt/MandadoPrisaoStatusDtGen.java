package br.gov.go.tj.projudi.dt;

public class MandadoPrisaoStatusDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3582309109807723566L;
	private String Id_MandadoPrisaoStatus;
	private String MandadoPrisaoStatus;

	private String MandadoPrisaoStatusCodigo;

	private String CodigoTemp;

//---------------------------------------------------------
	public MandadoPrisaoStatusDtGen() {

		limpar();

	}

	public String getId()  {return Id_MandadoPrisaoStatus;}
	public void setId(String valor ) {if(valor!=null) Id_MandadoPrisaoStatus = valor;}
	public String getMandadoPrisaoStatus()  {return MandadoPrisaoStatus;}
	public void setMandadoPrisaoStatus(String valor ) {if (valor!=null) MandadoPrisaoStatus = valor;}
	public String getMandadoPrisaoStatusCodigo()  {return MandadoPrisaoStatusCodigo;}
	public void setMandadoPrisaoStatusCodigo(String valor ) {if (valor!=null) MandadoPrisaoStatusCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(MandadoPrisaoStatusDt objeto){
		 if (objeto==null) return;
		Id_MandadoPrisaoStatus = objeto.getId();
		MandadoPrisaoStatusCodigo = objeto.getMandadoPrisaoStatusCodigo();
		MandadoPrisaoStatus = objeto.getMandadoPrisaoStatus();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MandadoPrisaoStatus="";
		MandadoPrisaoStatusCodigo="";
		MandadoPrisaoStatus="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MandadoPrisaoStatus:" + Id_MandadoPrisaoStatus + ";MandadoPrisaoStatusCodigo:" + MandadoPrisaoStatusCodigo + ";MandadoPrisaoStatus:" + MandadoPrisaoStatus + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
