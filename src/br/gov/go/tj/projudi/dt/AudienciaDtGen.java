package br.gov.go.tj.projudi.dt;

public class AudienciaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 7839176778274107586L;
    private String Id_Audiencia;
	private String AudienciaTipo;

	private String Id_AudienciaTipo;

	private String Id_Serventia;
	private String Serventia;
	private String DataAgendada;
	private String DataMovimentacao;
	private String Reservada;
	private String CodigoTemp;
	private String AudienciaTipoCodigo;

//---------------------------------------------------------
	public AudienciaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Audiencia;}
	public void setId(String valor ) {if(valor!=null) Id_Audiencia = valor;}
	public String getAudienciaTipo()  {return AudienciaTipo;}
	public void setAudienciaTipo(String valor ) {if (valor!=null) AudienciaTipo = valor;}
	public String getId_AudienciaTipo()  {return Id_AudienciaTipo;}
	public void setId_AudienciaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AudienciaTipo = ""; AudienciaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_AudienciaTipo = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getDataAgendada()  {return DataAgendada;}
	public void setDataAgendada(String valor ) {if (valor!=null) DataAgendada = valor;}
	public String getDataMovimentacao()  {return DataMovimentacao;}
	public void setDataMovimentacao(String valor ) {if (valor!=null) DataMovimentacao = valor;}
	public String getReservada()  {return Reservada;}
	public void setReservada(String valor ) {if (valor!=null) Reservada = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getAudienciaTipoCodigo()  {return AudienciaTipoCodigo;}
	public void setAudienciaTipoCodigo(String valor ) {if (valor!=null) AudienciaTipoCodigo = valor;}


	public void copiar(AudienciaDt objeto){
		Id_Audiencia = objeto.getId();
		Id_AudienciaTipo = objeto.getId_AudienciaTipo();
		AudienciaTipo = objeto.getAudienciaTipo();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		DataAgendada = objeto.getDataAgendada();
		DataMovimentacao = objeto.getDataMovimentacao();
		Reservada = objeto.getReservada();
		CodigoTemp = objeto.getCodigoTemp();
		AudienciaTipoCodigo = objeto.getAudienciaTipoCodigo();
	}

	public void limpar(){
		Id_Audiencia="";
		Id_AudienciaTipo="";
		AudienciaTipo="";
		Id_Serventia="";
		Serventia="";
		DataAgendada="";
		DataMovimentacao="";
		Reservada="";
		CodigoTemp="";
		AudienciaTipoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_Audiencia:" + Id_Audiencia + ";Id_AudienciaTipo:" + Id_AudienciaTipo + ";AudienciaTipo:" + AudienciaTipo + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";DataAgendada:" + DataAgendada + ";DataMovimentacao:" + DataMovimentacao + ";Reservada:" + Reservada + ";CodigoTemp:" + CodigoTemp + ";AudienciaTipoCodigo:" + AudienciaTipoCodigo + "]";
	}


} 
