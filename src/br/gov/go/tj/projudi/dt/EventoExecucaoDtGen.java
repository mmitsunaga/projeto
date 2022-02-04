package br.gov.go.tj.projudi.dt;

public class EventoExecucaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 5330924148100975456L;
    private String Id_EventoExecucao;
	private String EventoExecucao;


	private String AlteraLocal;
	private String AlteraRegime;
	private String ValorNegativo; // 1 - true ; 0 - false
	private String EventoExecucaoCodigo;
	private String Id_EventoExecucaoTipo;
	private String EventoExecucaoTipo;
	private String Id_EventoExecucaoStatus;
	private String EventoExecucaoStatus;
	private String CodigoTemp;
	private String Observacao;

//---------------------------------------------------------
	public EventoExecucaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_EventoExecucao;}
	public void setId(String valor ) {if(valor!=null) Id_EventoExecucao = valor;}
	public String getEventoExecucao()  {return EventoExecucao;}
	public void setEventoExecucao(String valor ) {if (valor!=null) EventoExecucao = valor;}
	public String getAlteraLocal()  {return AlteraLocal;}
	public void setAlteraLocal(String valor ) {if (valor!=null) AlteraLocal = valor;}
	public String getAlteraRegime()  {return AlteraRegime;}
	public void setAlteraRegime(String valor ) {if (valor!=null) AlteraRegime = valor;}
	public String getValorNegativo()  {return ValorNegativo;}
	public void setValorNegativo(String valor ) {if (valor!=null) ValorNegativo = valor;}
	public String getEventoExecucaoCodigo()  {return EventoExecucaoCodigo;}
	public void setEventoExecucaoCodigo(String valor ) {if (valor!=null) EventoExecucaoCodigo = valor;}
	public String getId_EventoExecucaoTipo()  {return Id_EventoExecucaoTipo;}
	public void setId_EventoExecucaoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_EventoExecucaoTipo = ""; EventoExecucaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_EventoExecucaoTipo = valor;}
	public String getEventoExecucaoTipo()  {return EventoExecucaoTipo;}
	public void setEventoExecucaoTipo(String valor ) {if (valor!=null) EventoExecucaoTipo = valor;}
	public String getId_EventoExecucaoStatus()  {return Id_EventoExecucaoStatus;}
	public void setId_EventoExecucaoStatus(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_EventoExecucaoStatus = ""; EventoExecucaoStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_EventoExecucaoStatus = valor;}
	public String getEventoExecucaoStatus()  {return EventoExecucaoStatus;}
	public void setEventoExecucaoStatus(String valor ) {if (valor!=null) EventoExecucaoStatus = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getObservacao()  {return Observacao;}
	public void setObservacao(String valor ) {if (valor!=null) Observacao = valor;}


	public void copiar(EventoExecucaoDt objeto){
		Id_EventoExecucao = objeto.getId();
		EventoExecucao = objeto.getEventoExecucao();
		AlteraLocal = objeto.getAlteraLocal();
		AlteraRegime = objeto.getAlteraRegime();
		ValorNegativo = objeto.getValorNegativo();
		EventoExecucaoCodigo = objeto.getEventoExecucaoCodigo();
		Id_EventoExecucaoTipo = objeto.getId_EventoExecucaoTipo();
		EventoExecucaoTipo = objeto.getEventoExecucaoTipo();
		Id_EventoExecucaoStatus = objeto.getId_EventoExecucaoStatus();
		EventoExecucaoStatus = objeto.getEventoExecucaoStatus();
		CodigoTemp = objeto.getCodigoTemp();
		Observacao = objeto.getObservacao();
	}

	public void limpar(){
		Id_EventoExecucao="";
		EventoExecucao="";
		AlteraLocal="";
		AlteraRegime="";
		ValorNegativo="";
		EventoExecucaoCodigo="";
		Id_EventoExecucaoTipo="";
		EventoExecucaoTipo="";
		Id_EventoExecucaoStatus="";
		EventoExecucaoStatus="";
		CodigoTemp="";
		Observacao = "";
	}


	public String getPropriedades(){
		return "[Id_EventoExecucao:" + Id_EventoExecucao + ";EventoExecucao:" + EventoExecucao + ";AlteraLocal:" + AlteraLocal + ";AlteraRegime:" + AlteraRegime + ";ValorNegativo:" + ValorNegativo + ";EventoExecucaoCodigo:" + EventoExecucaoCodigo + ";Id_EventoExecucaoTipo:" + Id_EventoExecucaoTipo + ";EventoExecucaoTipo:" + EventoExecucaoTipo + ";Id_EventoExecucaoStatus:" + Id_EventoExecucaoStatus + ";EventoExecucaoStatus:" + EventoExecucaoStatus + ";CodigoTemp:" + CodigoTemp + ";Observacao:" + Observacao +"]";
	}


} 
