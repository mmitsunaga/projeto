package br.gov.go.tj.projudi.dt;

public class ProcessoPrioridadeDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -9121579576075184395L;
    private String Id_ProcessoPrioridade;
	private String ProcessoPrioridade;
	
	


	private String ProcessoPrioridadeCodigo;
	

//---------------------------------------------------------
	public ProcessoPrioridadeDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoPrioridade;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoPrioridade = valor;}
	public String getProcessoPrioridade()  {return ProcessoPrioridade;}
	public void setProcessoPrioridade(String valor ) {if (valor!=null) ProcessoPrioridade = valor;}
	public String getProcessoPrioridadeCodigo()  {return ProcessoPrioridadeCodigo;}
	public void setProcessoPrioridadeCodigo(String valor ) {if (valor!=null) ProcessoPrioridadeCodigo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(ProcessoPrioridadeDt objeto){
		Id_ProcessoPrioridade = objeto.getId();
		ProcessoPrioridade = objeto.getProcessoPrioridade();
		ProcessoPrioridadeCodigo = objeto.getProcessoPrioridadeCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoPrioridade="";
		ProcessoPrioridade="";
		ProcessoPrioridadeCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoPrioridade:" + Id_ProcessoPrioridade + ";ProcessoPrioridade:" + ProcessoPrioridade + ";ProcessoPrioridadeCodigo:" + ProcessoPrioridadeCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
