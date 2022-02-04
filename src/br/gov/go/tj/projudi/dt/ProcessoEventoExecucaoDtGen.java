package br.gov.go.tj.projudi.dt;

public class ProcessoEventoExecucaoDtGen extends Dados{

	private String Id_ProcessoEventoExecucao;
    private static final long serialVersionUID = -2724372653736623725L;
	private String EventoExecucao;

	private String DataInicio;
	private String DataFim;
	private String Quantidade;
	private String Id_Movimentacao;
	private String Id_EventoExecucao;
	private String Id_ProcessoExecucao;
	private String Observacao;
	private String Id_LivramentoCondicional;
	private String ConsiderarTempoLivramentoCondicional;
	private String CodigoTemp;
	private String MovimentacaoTipo;
	private String DataRealizacao;
	private String Id_Processo;
	private String Id_UsuarioServentia;
	private String DataAlteracao;
	private String idProcessoExecucaoPenal;

//---------------------------------------------------------
	public ProcessoEventoExecucaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoEventoExecucao;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoEventoExecucao = valor;}
	public String getEventoExecucao()  {return EventoExecucao;}
	public void setEventoExecucao(String valor ) {if (valor!=null) EventoExecucao = valor;}
	public String getDataInicio()  {return DataInicio;}
	public void setDataInicio(String valor ) {if (valor!=null) DataInicio = valor;}
	public String getDataFim()  {return DataFim;}
	public void setDataFim(String valor ) {if (valor!=null) DataFim = valor;}
	public String getQuantidade()  {return Quantidade;}
	public void setQuantidade(String valor ) {if (valor!=null) Quantidade = valor;}
	public String getId_Movimentacao()  {return Id_Movimentacao;}
	public void setId_Movimentacao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Movimentacao = ""; Id_EventoExecucao = "";}else if (!valor.equalsIgnoreCase("")) Id_Movimentacao = valor;}
	public String getId_EventoExecucao()  {return Id_EventoExecucao;}
	public void setId_EventoExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_EventoExecucao = ""; Id_ProcessoExecucao = "";}else if (!valor.equalsIgnoreCase("")) Id_EventoExecucao = valor;}
	public String getId_ProcessoExecucao()  {return Id_ProcessoExecucao;}
	public void setId_ProcessoExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoExecucao = ""; Observacao = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoExecucao = valor;}
	public String getObservacao()  {return Observacao;}
	public void setObservacao(String valor ) {if (valor!=null) Observacao = valor;}
	public String getId_LivramentoCondicional()  {return Id_LivramentoCondicional;}
	public void setId_LivramentoCondicional(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_LivramentoCondicional = ""; ConsiderarTempoLivramentoCondicional = "";}else if (!valor.equalsIgnoreCase("")) Id_LivramentoCondicional = valor;}
	public String getConsiderarTempoLivramentoCondicional()  {return ConsiderarTempoLivramentoCondicional;}
	public void setConsiderarTempoLivramentoCondicional(String valor ) {if (valor!=null) ConsiderarTempoLivramentoCondicional = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getMovimentacaoTipo()  {return MovimentacaoTipo;}
	public void setMovimentacaoTipo(String valor ) {if (valor!=null) MovimentacaoTipo = valor;}
	public String getDataRealizacao()  {return DataRealizacao;}
	public void setDataRealizacao(String valor ) {if (valor!=null) DataRealizacao = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; EventoExecucao = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getId_UsuarioServentia() {return Id_UsuarioServentia; }
	public void setId_UsuarioServentia(String id_UsuarioServentia) {Id_UsuarioServentia = id_UsuarioServentia; }
	public String getDataAlteracao() {return DataAlteracao; }
	public void setDataAlteracao(String dataAlteracao) {DataAlteracao = dataAlteracao; }
	public String getIdProcessoExecucaoPenal() {return idProcessoExecucaoPenal;}
	public void setIdProcessoExecucaoPenal(String idProcessoExecucaoPenal) {if (idProcessoExecucaoPenal!=null) this.idProcessoExecucaoPenal = idProcessoExecucaoPenal;	}

	public void copiar(ProcessoEventoExecucaoDt objeto){
		 if (objeto==null) return;
		Id_ProcessoEventoExecucao = objeto.getId();
		DataInicio = objeto.getDataInicio();
		DataFim = objeto.getDataFim();
		Quantidade = objeto.getQuantidade();
		Id_Movimentacao = objeto.getId_Movimentacao();
		Id_EventoExecucao = objeto.getId_EventoExecucao();
		Id_ProcessoExecucao = objeto.getId_ProcessoExecucao();
		Observacao = objeto.getObservacao();
		Id_LivramentoCondicional = objeto.getId_LivramentoCondicional();
		ConsiderarTempoLivramentoCondicional = objeto.getConsiderarTempoLivramentoCondicional();
		CodigoTemp = objeto.getCodigoTemp();
		MovimentacaoTipo = objeto.getMovimentacaoTipo();
		DataRealizacao = objeto.getDataRealizacao();
		Id_Processo = objeto.getId_Processo();
		EventoExecucao = objeto.getEventoExecucao();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		DataAlteracao = objeto.getDataAlteracao();
		idProcessoExecucaoPenal = objeto.getIdProcessoExecucaoPenal();
	}

	public void limpar(){
		Id_ProcessoEventoExecucao="";
		DataInicio="";
		DataFim="";
		Quantidade="";
		Id_Movimentacao="";
		Id_EventoExecucao="";
		Id_ProcessoExecucao="";
		Observacao="";
		Id_LivramentoCondicional="";
		ConsiderarTempoLivramentoCondicional="";
		CodigoTemp="";
		MovimentacaoTipo="";
		DataRealizacao="";
		Id_Processo="";
		EventoExecucao="";
		Id_UsuarioServentia = "";
		DataAlteracao = "";
		idProcessoExecucaoPenal = "";
	}


	public String getPropriedades(){
		return "[Id_ProcessoEventoExecucao:" + Id_ProcessoEventoExecucao + ";DataInicio:" + DataInicio + ";DataFim:" + DataFim + ";Quantidade:" + Quantidade + ";Id_Movimentacao:" + Id_Movimentacao + ";Id_EventoExecucao:" + Id_EventoExecucao + ";Id_ProcessoExecucao:" + Id_ProcessoExecucao + ";Observacao:" + Observacao + ";Id_LivramentoCondicional:" + Id_LivramentoCondicional + ";ConsiderarTempoLivramentoCondicional:" + ConsiderarTempoLivramentoCondicional + ";CodigoTemp:" + CodigoTemp + ";MovimentacaoTipo:" + MovimentacaoTipo + ";DataRealizacao:" + DataRealizacao + ";Id_Processo:" + Id_Processo + ";EventoExecucao:" + EventoExecucao + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";Data_Alteração:" + DataAlteracao + ";IdProcessoExecucaoPenal:" + idProcessoExecucaoPenal + "]";
	}


} 
