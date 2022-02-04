package br.gov.go.tj.projudi.dt;

public class EventoExecucaoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 6631862800386565286L;
    private String Id_EventoExecucaoTipo;
	private String EventoExecucaoTipo;


	private String EventoExecucaoTipoCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public EventoExecucaoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_EventoExecucaoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_EventoExecucaoTipo = valor;}
	public String getEventoExecucaoTipo()  {return EventoExecucaoTipo;}
	public void setEventoExecucaoTipo(String valor ) {if (valor!=null) EventoExecucaoTipo = valor;}
	public String getEventoExecucaoTipoCodigo()  {return EventoExecucaoTipoCodigo;}
	public void setEventoExecucaoTipoCodigo(String valor ) {if (valor!=null) EventoExecucaoTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(EventoExecucaoTipoDt objeto){
		Id_EventoExecucaoTipo = objeto.getId();
		EventoExecucaoTipo = objeto.getEventoExecucaoTipo();
		EventoExecucaoTipoCodigo = objeto.getEventoExecucaoTipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_EventoExecucaoTipo="";
		EventoExecucaoTipo="";
		EventoExecucaoTipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_EventoExecucaoTipo:" + Id_EventoExecucaoTipo + ";EventoExecucaoTipo:" + EventoExecucaoTipo + ";EventoExecucaoTipoCodigo:" + EventoExecucaoTipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
