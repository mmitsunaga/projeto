package br.gov.go.tj.projudi.dt;

public class ProcessoDebitoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -6480815490373372280L;
    private String Id_ProcessoDebito;
	private String ProcessoDebito;


	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoDebitoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoDebito;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoDebito = valor;}
	public String getProcessoDebito()  {return ProcessoDebito;}
	public void setProcessoDebito(String valor ) {if (valor!=null) ProcessoDebito = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoDebitoDt objeto){
		Id_ProcessoDebito = objeto.getId();
		ProcessoDebito = objeto.getProcessoDebito();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoDebito="";
		ProcessoDebito="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoDebito:" + Id_ProcessoDebito + ";ProcessoDebito:" + ProcessoDebito + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
