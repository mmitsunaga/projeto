package br.gov.go.tj.projudi.dt;

public class TarefaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -1562821623048994502L;
    private String Id_Tarefa;
	private String Tarefa;


	private String Descricao;
	private String Resposta;
	private String DataInicio;
	private String Previsao;
	private String DataFim;
	private String Id_TarefaPai;
	private String TarefaPai;
	private String PontosApf;
	private String PontosApg;
	private String Id_TarefaPrioridade;
	private String TarefaPrioridade;
	private String Id_TarefaTipo;
	private String TarefaTipo;
	private String Id_TarefaStatus;
	private String TarefaStatus;
	private String TarefaStatusCodigo;
	private String Id_Projeto;
	private String Projeto;
	private String Id_ProjetoParticipanteResponsavel;
	private String ProjetoParticipanteResponsavel;
	private String Id_UsuarioCriador;
	private String UsuarioCriador;
	private String Id_UsuarioFinalizador;
	private String UsuarioFinalizador;
	private String CodigoTemp;

//---------------------------------------------------------
	public TarefaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Tarefa;}
	public void setId(String valor ) {if(valor!=null) Id_Tarefa = valor;}
	public String getTarefa()  {return Tarefa;}
	public void setTarefa(String valor ) {if (valor!=null) Tarefa = valor;}
	public String getDescricao()  {return Descricao;}
	public void setDescricao(String valor ) {if (valor!=null) Descricao = valor;}
	public String getResposta()  {return Resposta;}
	public void setResposta(String valor ) {if (valor!=null) Resposta = valor;}
	public String getDataInicio()  {return DataInicio;}
	public void setDataInicio(String valor ) {if (valor!=null) DataInicio = valor;}
	public String getPrevisao()  {return Previsao;}
	public void setPrevisao(String valor ) {if (valor!=null) Previsao = valor;}
	public String getDataFim()  {return DataFim;}
	public void setDataFim(String valor ) {if (valor!=null) DataFim = valor;}
	public String getId_TarefaPai()  {return Id_TarefaPai;}
	public void setId_TarefaPai(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_TarefaPai = ""; TarefaPai = "";}else if (!valor.equalsIgnoreCase("")) Id_TarefaPai = valor;}
	public String getTarefaPai()  {return TarefaPai;}
	public void setTarefaPai(String valor ) {if (valor!=null) TarefaPai = valor;}
	public String getPontosApf()  {return PontosApf;}
	public void setPontosApf(String valor ) {if (valor!=null) PontosApf = valor;}
	public String getPontosApg()  {return PontosApg;}
	public void setPontosApg(String valor ) {if (valor!=null) PontosApg = valor;}
	
	public String getId_TarefaPrioridade()  {return Id_TarefaPrioridade;}
	public void setId_TarefaPrioridade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_TarefaPrioridade = ""; TarefaPrioridade = "";}else if (!valor.equalsIgnoreCase("")) Id_TarefaPrioridade = valor;}
	
	public String getId_TarefaStatus()  {return Id_TarefaStatus;}
	public void setId_TarefaStatus(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_TarefaStatus = ""; TarefaStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_TarefaStatus = valor;}
	
	public String getTarefaPrioridade()  {return TarefaPrioridade;}
	public void setTarefaPrioridade(String valor ) {if (valor!=null) TarefaPrioridade = valor;}

	public String getTarefaStatus()  {return TarefaStatus;}
	public void setTarefaStatus(String valor ) {if (valor!=null) TarefaStatus = valor;}

	public String getTarefaStatusCodigo()  {return TarefaStatusCodigo;}
	public void setTarefaStatusCodigo(String valor ) {if (valor!=null) TarefaStatusCodigo = valor;}

	public String getId_TarefaTipo()  {return Id_TarefaTipo;}
	public void setId_TarefaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_TarefaTipo = ""; TarefaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_TarefaTipo = valor;}
	public String getTarefaTipo()  {return TarefaTipo;}
	public void setTarefaTipo(String valor ) {if (valor!=null) TarefaTipo = valor;}
	public String getId_Projeto()  {return Id_Projeto;}
	public void setId_Projeto(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Projeto = ""; Projeto = "";}else if (!valor.equalsIgnoreCase("")) Id_Projeto = valor;}
	public String getProjeto()  {return Projeto;}
	public void setProjeto(String valor ) {if (valor!=null) Projeto = valor;}
	public String getId_ProjetoParticipanteResponsavel()  {return Id_ProjetoParticipanteResponsavel;}
	public void setId_ProjetoParticipanteResponsavel(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProjetoParticipanteResponsavel = ""; ProjetoParticipanteResponsavel = "";}else if (!valor.equalsIgnoreCase("")) Id_ProjetoParticipanteResponsavel = valor;}
	public String getProjetoParticipanteResponsavel()  {return ProjetoParticipanteResponsavel;}
	public void setProjetoParticipanteResponsavel(String valor ) {if (valor!=null) ProjetoParticipanteResponsavel = valor;}
	public String getId_UsuarioCriador()  {return Id_UsuarioCriador;}
	public void setId_UsuarioCriador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioCriador = ""; UsuarioCriador = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioCriador = valor;}
	public String getUsuarioCriador()  {return UsuarioCriador;}
	public void setUsuarioCriador(String valor ) {if (valor!=null) UsuarioCriador = valor;}
	public String getId_UsuarioFinalizador()  {return Id_UsuarioFinalizador;}
	public void setId_UsuarioFinalizador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioFinalizador = ""; UsuarioFinalizador = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioFinalizador = valor;}
	public String getUsuarioFinalizador()  {return UsuarioFinalizador;}
	public void setUsuarioFinalizador(String valor ) {if (valor!=null) UsuarioFinalizador = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(TarefaDt objeto){
		 if (objeto==null) return;
		Id_Tarefa = objeto.getId();
		Tarefa = objeto.getTarefa();
		Descricao = objeto.getDescricao();
		Resposta = objeto.getResposta();
		DataInicio = objeto.getDataInicio();
		Previsao = objeto.getPrevisao();
		DataFim = objeto.getDataFim();
		Id_TarefaPai = objeto.getId_TarefaPai();
		TarefaPai = objeto.getTarefaPai();
		PontosApf = objeto.getPontosApf();
		PontosApg = objeto.getPontosApg();
		Id_TarefaPrioridade = objeto.getId_TarefaPrioridade();
		TarefaPrioridade = objeto.getTarefaPrioridade();

		Id_TarefaStatus = objeto.getId_TarefaStatus();
		TarefaStatus = objeto.getTarefaStatus();
		TarefaStatusCodigo = objeto.getTarefaStatusCodigo();
		
		Id_TarefaTipo = objeto.getId_TarefaTipo();
		TarefaTipo = objeto.getTarefaTipo();
		Id_Projeto = objeto.getId_Projeto();
		Projeto = objeto.getProjeto();
		Id_ProjetoParticipanteResponsavel = objeto.getId_ProjetoParticipanteResponsavel();
		ProjetoParticipanteResponsavel = objeto.getProjetoParticipanteResponsavel();
		Id_UsuarioCriador = objeto.getId_UsuarioCriador();
		UsuarioCriador = objeto.getUsuarioCriador();
		Id_UsuarioFinalizador = objeto.getId_UsuarioFinalizador();
		UsuarioFinalizador = objeto.getUsuarioFinalizador();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Tarefa="";
		Tarefa="";
		Descricao="";
		Resposta="";
		DataInicio="";
		Previsao="";
		DataFim="";
		Id_TarefaPai="";
		TarefaPai="";
		PontosApf="";
		PontosApg="";
		Id_TarefaPrioridade="";
		TarefaPrioridade="";

		Id_TarefaStatus="";
		TarefaStatus="";
		TarefaStatusCodigo="";
		
		Id_TarefaTipo="";
		TarefaTipo="";
		Id_Projeto="";
		Projeto="";
		Id_ProjetoParticipanteResponsavel="";
		ProjetoParticipanteResponsavel="";
		Id_UsuarioCriador="";
		UsuarioCriador="";
		Id_UsuarioFinalizador="";
		UsuarioFinalizador="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Tarefa:" + Id_Tarefa + ";Tarefa:" + Tarefa + ";Descricao:" + Descricao + ";Resposta:" + Resposta + ";DataInicio:" + DataInicio + ";Previsao:" + Previsao + ";DataFim:" + DataFim + ";Id_TarefaPai:" + Id_TarefaPai + ";TarefaPai:" + TarefaPai + ";PontosApf:" + PontosApf + ";PontosApg:" + PontosApg + ";Id_TarefaPrioridade:" + Id_TarefaPrioridade + ";TarefaPrioridade:" + TarefaPrioridade + ";Id_TarefaStatus:" + Id_TarefaStatus + ";TarefaStatusCodigo:" + TarefaStatusCodigo +";TarefaStatus:" + TarefaStatus + ";Id_TarefaTipo:" + Id_TarefaTipo + ";TarefaTipo:" + TarefaTipo + ";Id_Projeto:" + Id_Projeto + ";Projeto:" + Projeto + ";Id_ProjetoParticipanteResponsavel:" + Id_ProjetoParticipanteResponsavel + ";ProjetoParticipanteResponsavel:" + ProjetoParticipanteResponsavel + ";Id_UsuarioCriador:" + Id_UsuarioCriador + ";UsuarioCriador:" + UsuarioCriador + ";Id_UsuarioFinalizador:" + Id_UsuarioFinalizador + ";UsuarioFinalizador:" + UsuarioFinalizador + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
