package br.gov.go.tj.projudi.dt;

public class PendenciaDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4027822071971634651L;
	private String Id_Pendencia;
	private String Pendencia;


	private String Id_PendenciaTipo;
	private String PendenciaTipo;
	private String Id_Movimentacao;
	private String Movimentacao;
	private String Id_Processo;
	private String ProcessoNumero;
	private String DigitoVerificador;
	private String ProcessoNumeroCompleto;
	private String Id_ProcessoPrioridade;
	private String ProcessoPrioridade;
	private String PendenciaPrioridadeCodigo;
	private String Id_ProcessoParte;
	private String NomeParte;
	private String Id_PendenciaStatus;
	private String PendenciaStatus;
	private String Id_UsuarioCadastrador;
	private String UsuarioCadastrador;
	private String Id_UsuarioFinalizador;
	private String UsuarioFinalizador;
	private String Id_ServentiaFinalizador;
	private String ServentiaFinalizador;
	private String Id_ServentiaCadastrador;
	private String ServentiaCadastrador;
	private String DataInicio;
	private String DataFim;
	private String DataLimite;
	private String DataDistribuicao;
	private String Prazo;
	private String DataTemp;
	private String Id_PendenciaPai;
	private String DataVisto;
	private String CodigoTemp;
	private String PendenciaTipoCodigo;
	private String PendenciaStatusCodigo;
	private String NomeUsuarioCadastrador;
	private String NomeUsuarioFinalizador;
	private String Id_Classificador;
	private String Classificador;

//---------------------------------------------------------
	public PendenciaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Pendencia;}
	public void setId(String valor ) {if(valor!=null) Id_Pendencia = valor;}
	public String getPendencia()  {return Pendencia;}
	public void setPendencia(String valor ) {if (valor!=null) Pendencia = valor;}
	public String getId_PendenciaTipo()  {return Id_PendenciaTipo;}
	public void setId_PendenciaTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaTipo = ""; PendenciaTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_PendenciaTipo = valor;}
	public String getPendenciaTipo()  {return PendenciaTipo;}
	public void setPendenciaTipo(String valor ) {if (valor!=null) PendenciaTipo = valor;}
	public String getId_Movimentacao()  {return Id_Movimentacao;}
	public void setId_Movimentacao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Movimentacao = ""; Movimentacao = "";}else if (!valor.equalsIgnoreCase("")) Id_Movimentacao = valor;}
	public String getMovimentacao()  {return Movimentacao;}
	public void setMovimentacao(String valor ) {if (valor!=null) Movimentacao = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getDigitoVerificador()  {return DigitoVerificador;}
	public void setDigitoVerificador(String valor ) {if (valor!=null) DigitoVerificador = valor;}
	public String getProcessoNumeroCompleto()  {return ProcessoNumeroCompleto;}
	public void setProcessoNumeroCompleto(String valor ) {if (valor!=null) ProcessoNumeroCompleto = valor;}
	public String getId_ProcessoPrioridade()  {return Id_ProcessoPrioridade;}
	public void setId_ProcessoPrioridade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoPrioridade = ""; ProcessoPrioridade = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoPrioridade = valor;}
	public String getProcessoPrioridade()  {return ProcessoPrioridade;}
	public void setProcessoPrioridade(String valor ) {if (valor!=null) ProcessoPrioridade = valor;}
	public String getId_Classificador()  {return Id_Classificador;}
	public void setId_Classificador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Classificador = ""; Classificador = "";}else if (!valor.equalsIgnoreCase("")) Id_Classificador = valor;}
	public String getClassificador()  {return Classificador;}
	public void setClassificador(String valor ) {if (valor!=null) Classificador = valor;}
	public String getPendenciaPrioridadeCodigo()  {return PendenciaPrioridadeCodigo;}
	public void setPendenciaPrioridadeCodigo(String valor ) {if (valor!=null) PendenciaPrioridadeCodigo = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; NomeParte = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getNomeParte()  {return NomeParte;}
	public void setNomeParte(String valor ) {if (valor!=null) NomeParte = valor;}
	public String getId_PendenciaStatus()  {return Id_PendenciaStatus;}
	public void setId_PendenciaStatus(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaStatus = ""; PendenciaStatus = "";}else if (!valor.equalsIgnoreCase("")) Id_PendenciaStatus = valor;}
	public String getPendenciaStatus()  {return PendenciaStatus;}
	public void setPendenciaStatus(String valor ) {if (valor!=null) PendenciaStatus = valor;}
	public String getId_UsuarioCadastrador()  {return Id_UsuarioCadastrador;}
	public void setId_UsuarioCadastrador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioCadastrador = ""; UsuarioCadastrador = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioCadastrador = valor;}
	public String getUsuarioCadastrador()  {return UsuarioCadastrador;}
	public void setUsuarioCadastrador(String valor ) {if (valor!=null) UsuarioCadastrador = valor;}
	public String getId_UsuarioFinalizador()  {return Id_UsuarioFinalizador;}
	public void setId_UsuarioFinalizador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioFinalizador = ""; UsuarioFinalizador = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioFinalizador = valor;}
	public String getUsuarioFinalizador()  {return UsuarioFinalizador;}
	public void setUsuarioFinalizador(String valor ) {if (valor!=null) UsuarioFinalizador = valor;}
	public String getId_ServentiaFinalizador()  {return Id_ServentiaFinalizador;}
	public void setId_ServentiaFinalizador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaFinalizador = ""; ServentiaFinalizador = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaFinalizador = valor;}
	public String getServentiaFinalizador()  {return ServentiaFinalizador;}
	public void setServentiaFinalizador(String valor ) {if (valor!=null) ServentiaFinalizador = valor;}
	public String getId_ServentiaCadastrador()  {return Id_ServentiaCadastrador;}
	public void setId_ServentiaCadastrador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaCadastrador = ""; ServentiaCadastrador = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCadastrador = valor;}
	public String getServentiaCadastrador()  {return ServentiaCadastrador;}
	public void setServentiaCadastrador(String valor ) {if (valor!=null) ServentiaCadastrador = valor;}
	public String getDataInicio()  {return DataInicio;}
	public void setDataInicio(String valor ) {if (valor!=null) DataInicio = valor;}
	public String getDataFim()  {return DataFim;}
	public void setDataFim(String valor ) {if (valor!=null) DataFim = valor;}
	public String getDataLimite()  {return DataLimite;}
	public void setDataLimite(String valor ) {if (valor!=null) DataLimite = valor;}
	public String getDataDistribuicao()  {return DataDistribuicao;}
	public void setDataDistribuicao(String valor ) {if (valor!=null) DataDistribuicao = valor;}
	public String getPrazo()  {return Prazo;}
	public void setPrazo(String valor ) {if (valor!=null) Prazo = valor;}
	public String getDataTemp()  {return DataTemp;}
	public void setDataTemp(String valor ) {if (valor!=null) DataTemp = valor;}
	public String getId_PendenciaPai()  {return Id_PendenciaPai;}
	public void setId_PendenciaPai(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaPai = ""; DataVisto = "";}else if (!valor.equalsIgnoreCase("")) Id_PendenciaPai = valor;}
	public String getDataVisto()  {return DataVisto;}
	public void setDataVisto(String valor ) {if (valor!=null) DataVisto = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getPendenciaTipoCodigo()  {return PendenciaTipoCodigo;}
	public void setPendenciaTipoCodigo(String valor ) {if (valor!=null) PendenciaTipoCodigo = valor;}
	public String getPendenciaStatusCodigo()  {return PendenciaStatusCodigo;}
	public void setPendenciaStatusCodigo(String valor ) {if (valor!=null) PendenciaStatusCodigo = valor;}
	public String getNomeUsuarioCadastrador()  {return NomeUsuarioCadastrador;}
	public void setNomeUsuarioCadastrador(String valor ) {if (valor!=null) NomeUsuarioCadastrador = valor;}
	public String getNomeUsuarioFinalizador()  {return NomeUsuarioFinalizador;}
	public void setNomeUsuarioFinalizador(String valor ) {if (valor!=null) NomeUsuarioFinalizador = valor;}


	public void copiar(PendenciaDt objeto){
		 if (objeto==null) return;
		Id_Pendencia = objeto.getId();
		Pendencia = objeto.getPendencia();
		Id_PendenciaTipo = objeto.getId_PendenciaTipo();
		PendenciaTipo = objeto.getPendenciaTipo();
		Id_Movimentacao = objeto.getId_Movimentacao();
		Movimentacao = objeto.getMovimentacao();
		Id_Processo = objeto.getId_Processo();
		ProcessoNumero = objeto.getProcessoNumero();
		DigitoVerificador = objeto.getDigitoVerificador();
		ProcessoNumeroCompleto = objeto.getProcessoNumeroCompleto();
		Id_ProcessoPrioridade = objeto.getId_ProcessoPrioridade();
		ProcessoPrioridade = objeto.getProcessoPrioridade();
		PendenciaPrioridadeCodigo = objeto.getPendenciaPrioridadeCodigo();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		NomeParte = objeto.getNomeParte();
		Id_PendenciaStatus = objeto.getId_PendenciaStatus();
		PendenciaStatus = objeto.getPendenciaStatus();
		Id_UsuarioCadastrador = objeto.getId_UsuarioCadastrador();
		UsuarioCadastrador = objeto.getUsuarioCadastrador();
		Id_UsuarioFinalizador = objeto.getId_UsuarioFinalizador();
		UsuarioFinalizador = objeto.getUsuarioFinalizador();
		Id_ServentiaFinalizador = objeto.getId_ServentiaFinalizador();
		ServentiaFinalizador = objeto.getServentiaFinalizador();
		Id_ServentiaCadastrador = objeto.getId_ServentiaCadastrador();
		ServentiaCadastrador = objeto.getServentiaCadastrador();
		DataInicio = objeto.getDataInicio();
		DataFim = objeto.getDataFim();
		DataLimite = objeto.getDataLimite();
		DataDistribuicao = objeto.getDataDistribuicao();
		Prazo = objeto.getPrazo();
		DataTemp = objeto.getDataTemp();
		Id_PendenciaPai = objeto.getId_PendenciaPai();
		DataVisto = objeto.getDataVisto();
		CodigoTemp = objeto.getCodigoTemp();
		PendenciaTipoCodigo = objeto.getPendenciaTipoCodigo();
		PendenciaStatusCodigo = objeto.getPendenciaStatusCodigo();
		NomeUsuarioCadastrador = objeto.getNomeUsuarioCadastrador();
		NomeUsuarioFinalizador = objeto.getNomeUsuarioFinalizador();
		Id_Classificador = objeto.getId_Classificador();
		Classificador = objeto.getClassificador();
	}

	public void limpar(){
		Id_Pendencia="";
		Pendencia="";
		Id_PendenciaTipo="";
		PendenciaTipo="";
		Id_Movimentacao="";
		Movimentacao="";
		Id_Processo="";
		ProcessoNumero="";
		DigitoVerificador="";
		ProcessoNumeroCompleto="";
		Id_ProcessoPrioridade="";
		ProcessoPrioridade="";
		Id_Classificador="";
		Classificador="";
		PendenciaPrioridadeCodigo="";
		Id_ProcessoParte="";
		NomeParte="";
		Id_PendenciaStatus="";
		PendenciaStatus="";
		Id_UsuarioCadastrador="";
		UsuarioCadastrador="";
		Id_UsuarioFinalizador="";
		UsuarioFinalizador="";
		Id_ServentiaFinalizador="";
		ServentiaFinalizador="";
		Id_ServentiaCadastrador="";
		ServentiaCadastrador="";
		DataInicio="";
		DataFim="";
		DataLimite="";
		DataDistribuicao="";
		Prazo="";
		DataTemp="";
		Id_PendenciaPai="";
		DataVisto="";
		CodigoTemp="";
		PendenciaTipoCodigo="";
		PendenciaStatusCodigo="";
		NomeUsuarioCadastrador="";
		NomeUsuarioFinalizador="";
	}


	public String getPropriedades(){
		return "[Id_Pendencia:" + Id_Pendencia + ";Pendencia:" + Pendencia + ";Id_PendenciaTipo:" + Id_PendenciaTipo + ";PendenciaTipo:" + PendenciaTipo + ";Id_Movimentacao:" + Id_Movimentacao + ";Movimentacao:" + Movimentacao + ";Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";DigitoVerificador:" + DigitoVerificador + ";ProcessoNumeroCompleto:" + ProcessoNumeroCompleto + ";Id_ProcessoPrioridade:" + Id_ProcessoPrioridade + ";ProcessoPrioridade:" + ProcessoPrioridade + ";PendenciaPrioridadeCodigo:" + PendenciaPrioridadeCodigo + ";Id_ProcessoParte:" + Id_ProcessoParte + ";NomeParte:" + NomeParte + ";Id_PendenciaStatus:" + Id_PendenciaStatus + ";PendenciaStatus:" + PendenciaStatus + ";Id_UsuarioCadastrador:" + Id_UsuarioCadastrador + ";UsuarioCadastrador:" + UsuarioCadastrador + ";Id_UsuarioFinalizador:" + Id_UsuarioFinalizador + ";UsuarioFinalizador:" + UsuarioFinalizador + ";Id_ServentiaFinalizador:" + Id_ServentiaFinalizador + ";ServentiaFinalizador:" + ServentiaFinalizador + ";Id_ServentiaCadastrador:" + Id_ServentiaCadastrador + ";ServentiaCadastrador:" + ServentiaCadastrador + ";DataInicio:" + DataInicio + ";DataFim:" + DataFim + ";DataLimite:" + DataLimite + ";DataDistribuicao:" + DataDistribuicao + ";Prazo:" + Prazo + ";DataTemp:" + DataTemp + ";Id_PendenciaPai:" + Id_PendenciaPai + ";DataVisto:" + DataVisto + ";CodigoTemp:" + CodigoTemp + ";PendenciaTipoCodigo:" + PendenciaTipoCodigo + ";PendenciaStatusCodigo:" + PendenciaStatusCodigo + ";NomeUsuarioCadastrador:" + NomeUsuarioCadastrador + ";NomeUsuarioFinalizador:" + NomeUsuarioFinalizador + "]";
	}


} 
