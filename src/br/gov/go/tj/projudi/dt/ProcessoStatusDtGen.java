package br.gov.go.tj.projudi.dt;

public class ProcessoStatusDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -1965768831683368332L;
    private String Id_ProcessoStatus;
	private String ProcessoStatus;


	private String ProcessoStatusCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoStatusDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoStatus;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoStatus = valor;}
	public String getProcessoStatus()  {return ProcessoStatus;}
	public void setProcessoStatus(String valor ) {if (valor!=null) ProcessoStatus = valor;}
	public String getProcessoStatusCodigo()  {return ProcessoStatusCodigo;}
	public void setProcessoStatusCodigo(String valor ) {if (valor!=null) ProcessoStatusCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoStatusDt objeto){
		Id_ProcessoStatus = objeto.getId();
		ProcessoStatus = objeto.getProcessoStatus();
		ProcessoStatusCodigo = objeto.getProcessoStatusCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoStatus="";
		ProcessoStatus="";
		ProcessoStatusCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoStatus:" + Id_ProcessoStatus + ";ProcessoStatus:" + ProcessoStatus + ";ProcessoStatusCodigo:" + ProcessoStatusCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
