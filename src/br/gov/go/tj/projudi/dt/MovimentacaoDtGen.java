package br.gov.go.tj.projudi.dt;

public class MovimentacaoDtGen extends Dados{

	private String Id_Movimentacao;
    private static final long serialVersionUID = -9192520744477135345L;
	private String Movimentacao;


	private String Id_MovimentacaoTipo;
	private String MovimentacaoTipo;
	private String Id_Processo;
	private String ProcessoNumero;
	private String Id_UsuarioRealizador;
	private String UsuarioRealizador;
	private String Complemento;
	private String Id_ProcessoPrioridade;
	private String ProcessoPrioridade;
	private String DataRealizacao;
	private String PalavraChave;
	private String CodigoTemp;
	private String MovimentacaoTipoCodigo;
	private String ProcessoPrioridadeCodigo;
	private String DigitoVerificador;

//---------------------------------------------------------
	public MovimentacaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Movimentacao;}
	public void setId(String valor ) {if(valor!=null) Id_Movimentacao = valor;}
	public String getMovimentacao()  {return Movimentacao;}
	public void setMovimentacao(String valor ) {if (valor!=null) Movimentacao = valor;}
	public String getId_MovimentacaoTipo()  {return Id_MovimentacaoTipo;}
	public void setId_MovimentacaoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_MovimentacaoTipo = ""; MovimentacaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_MovimentacaoTipo = valor;}
	public String getMovimentacaoTipo()  {return MovimentacaoTipo;}
	public void setMovimentacaoTipo(String valor ) {if (valor!=null) MovimentacaoTipo = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getId_UsuarioRealizador()  {return Id_UsuarioRealizador;}
	public void setId_UsuarioRealizador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioRealizador = ""; UsuarioRealizador = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioRealizador = valor;}
	public String getUsuarioRealizador()  {return UsuarioRealizador;}
	public void setUsuarioRealizador(String valor ) {if (valor!=null) UsuarioRealizador = valor;}
	public String getComplemento()  {return Complemento;}
	public void setComplemento(String valor ) {if (valor!=null) Complemento = valor;}
	public String getId_ProcessoPrioridade()  {return Id_ProcessoPrioridade;}
	public void setId_ProcessoPrioridade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoPrioridade = ""; ProcessoPrioridade = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoPrioridade = valor;}
	public String getProcessoPrioridade()  {return ProcessoPrioridade;}
	public void setProcessoPrioridade(String valor ) {if (valor!=null) ProcessoPrioridade = valor;}
	public String getDataRealizacao()  {return DataRealizacao;}
	public void setDataRealizacao(String valor ) {if (valor!=null) DataRealizacao = valor;}
	public String getPalavraChave()  {return PalavraChave;}
	public void setPalavraChave(String valor ) {if (valor!=null) PalavraChave = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getMovimentacaoTipoCodigo()  {return MovimentacaoTipoCodigo;}
	public void setMovimentacaoTipoCodigo(String valor ) {if (valor!=null) MovimentacaoTipoCodigo = valor;}
	public String getProcessoPrioridadeCodigo()  {return ProcessoPrioridadeCodigo;}
	public void setProcessoPrioridadeCodigo(String valor ) {if (valor!=null) ProcessoPrioridadeCodigo = valor;}
	public String getDigitoVerificador()  {return DigitoVerificador;}
	public void setDigitoVerificador(String valor ) {if (valor!=null) DigitoVerificador = valor;}


	public void copiar(MovimentacaoDt objeto){
		 if (objeto==null) return;
		Id_Movimentacao = objeto.getId();
		Movimentacao = objeto.getMovimentacao();
		Id_MovimentacaoTipo = objeto.getId_MovimentacaoTipo();
		MovimentacaoTipo = objeto.getMovimentacaoTipo();
		Id_Processo = objeto.getId_Processo();
		ProcessoNumero = objeto.getProcessoNumero();
		Id_UsuarioRealizador = objeto.getId_UsuarioRealizador();
		UsuarioRealizador = objeto.getUsuarioRealizador();
		Complemento = objeto.getComplemento();
		Id_ProcessoPrioridade = objeto.getId_ProcessoPrioridade();
		ProcessoPrioridade = objeto.getProcessoPrioridade();
		DataRealizacao = objeto.getDataRealizacao();
		PalavraChave = objeto.getPalavraChave();
		CodigoTemp = objeto.getCodigoTemp();
		MovimentacaoTipoCodigo = objeto.getMovimentacaoTipoCodigo();
		ProcessoPrioridadeCodigo = objeto.getProcessoPrioridadeCodigo();
		DigitoVerificador = objeto.getDigitoVerificador();
	}

	public void limpar(){
		Id_Movimentacao="";
		Movimentacao="";
		Id_MovimentacaoTipo="";
		MovimentacaoTipo="";
		Id_Processo="";
		ProcessoNumero="";
		Id_UsuarioRealizador="";
		UsuarioRealizador="";
		Complemento="";
		Id_ProcessoPrioridade="";
		ProcessoPrioridade="";
		DataRealizacao="";
		PalavraChave="";
		CodigoTemp="";
		MovimentacaoTipoCodigo="";
		ProcessoPrioridadeCodigo="";
		DigitoVerificador="";
	}


	public String getPropriedades(){
		return "[Id_Movimentacao:" + Id_Movimentacao + ";Movimentacao:" + Movimentacao + ";Id_MovimentacaoTipo:" + Id_MovimentacaoTipo + ";MovimentacaoTipo:" + MovimentacaoTipo + ";Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";Id_UsuarioRealizador:" + Id_UsuarioRealizador + ";UsuarioRealizador:" + UsuarioRealizador + ";Complemento:" + Complemento + ";Id_ProcessoPrioridade:" + Id_ProcessoPrioridade + ";ProcessoPrioridade:" + ProcessoPrioridade + ";DataRealizacao:" + DataRealizacao + ";PalavraChave:" + PalavraChave + ";CodigoTemp:" + CodigoTemp + ";MovimentacaoTipoCodigo:" + MovimentacaoTipoCodigo + ";ProcessoPrioridadeCodigo:" + ProcessoPrioridadeCodigo + ";DigitoVerificador:" + DigitoVerificador + "]";
	}


} 
