package br.gov.go.tj.projudi.dt;

public class ProcessoBeneficioDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 3550614791650529228L;
    private String Id_ProcessoBeneficio;
	private String ProcessoBeneficio;


	private String Prazo;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoBeneficioDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoBeneficio;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoBeneficio = valor;}
	public String getProcessoBeneficio()  {return ProcessoBeneficio;}
	public void setProcessoBeneficio(String valor ) {if (valor!=null) ProcessoBeneficio = valor;}
	public String getPrazo()  {return Prazo;}
	public void setPrazo(String valor ) {if (valor!=null) Prazo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoBeneficioDt objeto){
		Id_ProcessoBeneficio = objeto.getId();
		ProcessoBeneficio = objeto.getProcessoBeneficio();
		Prazo = objeto.getPrazo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoBeneficio="";
		ProcessoBeneficio="";
		Prazo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoBeneficio:" + Id_ProcessoBeneficio + ";ProcessoBeneficio:" + ProcessoBeneficio + ";Prazo:" + Prazo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
