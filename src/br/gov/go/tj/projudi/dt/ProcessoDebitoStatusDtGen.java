package br.gov.go.tj.projudi.dt;

public class ProcessoDebitoStatusDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -6480815490373372280L;
    private String Id_ProcessoDebitoStatus;
	private String ProcessoDebitoStatus;


	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoDebitoStatusDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoDebitoStatus;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoDebitoStatus = valor;}
	public String getProcessoDebitoStatus()  {return ProcessoDebitoStatus;}
	public void setProcessoDebitoStatus(String valor ) {if (valor!=null) ProcessoDebitoStatus = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoDebitoStatusDt objeto){
		Id_ProcessoDebitoStatus = objeto.getId();
		ProcessoDebitoStatus = objeto.getProcessoDebitoStatus();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoDebitoStatus="";
		ProcessoDebitoStatus="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoDebitoStatus:" + Id_ProcessoDebitoStatus + ";ProcessoDebitoStatus:" + ProcessoDebitoStatus + ";CodigoTemp:" + CodigoTemp + "]";
	}
} 
