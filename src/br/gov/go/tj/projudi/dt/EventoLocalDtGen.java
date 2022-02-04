package br.gov.go.tj.projudi.dt;

public class EventoLocalDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -5295813294575323099L;
    private String Id_EventoLocal;
	private String LocalCumprimentoPena;

	private String Id_ProcessoEventoExecucao;
	private String EventoExecucao;
	private String Id_LocalCumprimentoPena;

	private String CodigoTemp;

//---------------------------------------------------------
	public EventoLocalDtGen() {

		limpar();

	}

	public String getId()  {return Id_EventoLocal;}
	public void setId(String valor ) {if(valor!=null) Id_EventoLocal = valor;}
	public String getLocalCumprimentoPena()  {return LocalCumprimentoPena;}
	public void setLocalCumprimentoPena(String valor ) {if (valor!=null) LocalCumprimentoPena = valor;}
	public String getId_ProcessoEventoExecucao()  {return Id_ProcessoEventoExecucao;}
	public void setId_ProcessoEventoExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoEventoExecucao = ""; EventoExecucao = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoEventoExecucao = valor;}
	public String getEventoExecucao()  {return EventoExecucao;}
	public void setEventoExecucao(String valor ) {if (valor!=null) EventoExecucao = valor;}
	public String getId_LocalCumprimentoPena()  {return Id_LocalCumprimentoPena;}
	public void setId_LocalCumprimentoPena(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_LocalCumprimentoPena = ""; LocalCumprimentoPena = "";}else if (!valor.equalsIgnoreCase("")) Id_LocalCumprimentoPena = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(EventoLocalDt objeto){
		Id_EventoLocal = objeto.getId();
		Id_ProcessoEventoExecucao = objeto.getId_ProcessoEventoExecucao();
		EventoExecucao = objeto.getEventoExecucao();
		Id_LocalCumprimentoPena = objeto.getId_LocalCumprimentoPena();
		LocalCumprimentoPena = objeto.getLocalCumprimentoPena();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_EventoLocal="";
		Id_ProcessoEventoExecucao="";
		EventoExecucao="";
		Id_LocalCumprimentoPena="";
		LocalCumprimentoPena="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_EventoLocal:" + Id_EventoLocal + ";Id_ProcessoEventoExecucao:" + Id_ProcessoEventoExecucao + ";EventoExecucao:" + EventoExecucao + ";Id_LocalCumprimentoPena:" + Id_LocalCumprimentoPena + ";LocalCumprimentoPena:" + LocalCumprimentoPena + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
