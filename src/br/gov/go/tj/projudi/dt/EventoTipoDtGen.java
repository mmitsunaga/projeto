package br.gov.go.tj.projudi.dt;

public class EventoTipoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7319255374773820714L;
	protected String Id_EventoTipo;
	protected String EventoTipo;


	protected String CodigoTemp;

//---------------------------------------------------------
	public EventoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_EventoTipo;}
	public void setId(String valor ) { if(valor!=null) Id_EventoTipo = valor;}
	public String getEventoTipo()  {return EventoTipo;}
	public void setEventoTipo(String valor ) { if (valor!=null) EventoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(EventoTipoDt objeto){
		 if (objeto==null) return;
		Id_EventoTipo = objeto.getId();
		EventoTipo = objeto.getEventoTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_EventoTipo="";
		EventoTipo="";
		CodigoTemp="";
	}

	public String toJson(){
		return "{\"Id\":\"" + getId() + "\",\"Id_EventoTipo\":\"" + getId() + "\",\"EventoTipo\":\"" + getEventoTipo() + "\",\"CodigoTemp\":\"" + getCodigoTemp() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_EventoTipo:" + Id_EventoTipo + ";EventoTipo:" + EventoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
