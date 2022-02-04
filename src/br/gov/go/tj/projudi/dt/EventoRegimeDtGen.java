package br.gov.go.tj.projudi.dt;

public class EventoRegimeDtGen extends Dados{

	private String Id_EventoRegime;
    private static final long serialVersionUID = -7864736976956547055L;
	private String RegimeExecucao;

	private String Id_ProcessoEventoExecucao;
	private String Id_RegimeExecucao;
	private String CodigoTemp;

	private String Id_ProximoRegimeExecucao;
	private String Id_PenaExecucaoTipo;
	private String PenaExecucaoTipo;
	private String EventoExecucao;

//---------------------------------------------------------
	public EventoRegimeDtGen() {

		limpar();

	}

	public String getId()  {return Id_EventoRegime;}
	public void setId(String valor ) {if(valor!=null) Id_EventoRegime = valor;}
	public String getRegimeExecucao()  {return RegimeExecucao;}
	public void setRegimeExecucao(String valor ) {if (valor!=null) RegimeExecucao = valor;}
	public String getId_ProcessoEventoExecucao()  {return Id_ProcessoEventoExecucao;}
	public void setId_ProcessoEventoExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoEventoExecucao = ""; Id_RegimeExecucao = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoEventoExecucao = valor;}
	public String getId_RegimeExecucao()  {return Id_RegimeExecucao;}
	public void setId_RegimeExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_RegimeExecucao = ""; CodigoTemp = "";}else Id_RegimeExecucao = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getId_ProximoRegimeExecucao()  {return Id_ProximoRegimeExecucao;}
	public void setId_ProximoRegimeExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProximoRegimeExecucao = ""; Id_PenaExecucaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProximoRegimeExecucao = valor;}
	public String getId_PenaExecucaoTipo()  {return Id_PenaExecucaoTipo;}
	public void setId_PenaExecucaoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PenaExecucaoTipo = ""; PenaExecucaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_PenaExecucaoTipo = valor;}
	public String getPenaExecucaoTipo()  {return PenaExecucaoTipo;}
	public void setPenaExecucaoTipo(String valor ) {if (valor!=null) PenaExecucaoTipo = valor;}
	public String getEventoExecucao()  {return EventoExecucao;}
	public void setEventoExecucao(String valor ) {if (valor!=null) EventoExecucao = valor;}


	public void copiar(EventoRegimeDt objeto){
		 if (objeto==null) return;
		Id_EventoRegime = objeto.getId();
		Id_ProcessoEventoExecucao = objeto.getId_ProcessoEventoExecucao();
		Id_RegimeExecucao = objeto.getId_RegimeExecucao();
		CodigoTemp = objeto.getCodigoTemp();
		RegimeExecucao = objeto.getRegimeExecucao();
		Id_ProximoRegimeExecucao = objeto.getId_ProximoRegimeExecucao();
		Id_PenaExecucaoTipo = objeto.getId_PenaExecucaoTipo();
		PenaExecucaoTipo = objeto.getPenaExecucaoTipo();
		EventoExecucao = objeto.getEventoExecucao();
	}

	public void limpar(){
		Id_EventoRegime="";
		Id_ProcessoEventoExecucao="";
		Id_RegimeExecucao="";
		CodigoTemp="";
		RegimeExecucao="";
		Id_ProximoRegimeExecucao="";
		Id_PenaExecucaoTipo="";
		PenaExecucaoTipo="";
		EventoExecucao="";
	}


	public String getPropriedades(){
		return "[Id_EventoRegime:" + Id_EventoRegime + ";Id_ProcessoEventoExecucao:" + Id_ProcessoEventoExecucao + ";Id_RegimeExecucao:" + Id_RegimeExecucao + ";CodigoTemp:" + CodigoTemp + ";RegimeExecucao:" + RegimeExecucao + ";Id_ProximoRegimeExecucao:" + Id_ProximoRegimeExecucao + ";Id_PenaExecucaoTipo:" + Id_PenaExecucaoTipo + ";PenaExecucaoTipo:" + PenaExecucaoTipo + ";EventoExecucao:" + EventoExecucao + "]";
	}


} 
