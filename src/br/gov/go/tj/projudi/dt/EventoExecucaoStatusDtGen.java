package br.gov.go.tj.projudi.dt;

public class EventoExecucaoStatusDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 6499490171521947504L;
    private String Id_EventoExecucaoStatus;
	private String EventoExecucaoStatus;


	private String EventoExecucaoStatusCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public EventoExecucaoStatusDtGen() {

		limpar();

	}

	public String getId()  {return Id_EventoExecucaoStatus;}
	public void setId(String valor ) {if(valor!=null) Id_EventoExecucaoStatus = valor;}
	public String getEventoExecucaoStatus()  {return EventoExecucaoStatus;}
	public void setEventoExecucaoStatus(String valor ) {if (valor!=null) EventoExecucaoStatus = valor;}
	public String getEventoExecucaoStatusCodigo()  {return EventoExecucaoStatusCodigo;}
	public void setEventoExecucaoStatusCodigo(String valor ) {if (valor!=null) EventoExecucaoStatusCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(EventoExecucaoStatusDt objeto){
		Id_EventoExecucaoStatus = objeto.getId();
		EventoExecucaoStatus = objeto.getEventoExecucaoStatus();
		EventoExecucaoStatusCodigo = objeto.getEventoExecucaoStatusCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_EventoExecucaoStatus="";
		EventoExecucaoStatus="";
		EventoExecucaoStatusCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_EventoExecucaoStatus:" + Id_EventoExecucaoStatus + ";EventoExecucaoStatus:" + EventoExecucaoStatus + ";EventoExecucaoStatusCodigo:" + EventoExecucaoStatusCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
