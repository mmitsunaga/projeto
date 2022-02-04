package br.gov.go.tj.projudi.dt;

public class CondenacaoExecucaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2713242575496684589L;
	private String Id_CondenacaoExecucao;
	private String CrimeExecucao;

	private String TempoPena;
	private String Reincidente;
	private String DataFato;
	private String Id_ProcessoExecucao;
	private String Id_CondenacaoExecucaoSituacao;
	private String CondenacaoExecucaoSituacao;
	private String ProcessoNumero;
	private String Id_CrimeExecucao;

	private String CrimeExecucaoCodigo;
	private String Artigo;
	private String Inciso;
	private String Lei;
	private String Paragrafo;
	private String CodigoTemp;
	private String observacao;

//---------------------------------------------------------
	public CondenacaoExecucaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_CondenacaoExecucao;}
	public void setId(String valor ) {if(valor!=null) Id_CondenacaoExecucao = valor;}
	public String getCrimeExecucao()  {return CrimeExecucao;}
	public void setCrimeExecucao(String valor ) {if (valor!=null) CrimeExecucao = valor;}
	public String getTempoPena()  {return TempoPena;}
	public void setTempoPena(String valor ) {if (valor!=null) TempoPena = valor;}
	public String getReincidente()  {return Reincidente;}
	public void setReincidente(String valor ) {if (valor!=null) Reincidente = valor;}
	public String getDataFato()  {return DataFato;}
	public void setDataFato(String valor ) {if (valor!=null) DataFato = valor;}
	public String getId_ProcessoExecucao()  {return Id_ProcessoExecucao;}
	public void setId_ProcessoExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoExecucao = ""; Id_CondenacaoExecucaoSituacao = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoExecucao = valor;}
	public String getId_CondenacaoExecucaoSituacao()  {return Id_CondenacaoExecucaoSituacao;}
	public void setId_CondenacaoExecucaoSituacao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CondenacaoExecucaoSituacao = ""; CondenacaoExecucaoSituacao = "";}else if (!valor.equalsIgnoreCase("")) Id_CondenacaoExecucaoSituacao = valor;}
	public String getCondenacaoExecucaoSituacao()  {return CondenacaoExecucaoSituacao;}
	public void setCondenacaoExecucaoSituacao(String valor ) {if (valor!=null) CondenacaoExecucaoSituacao = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getId_CrimeExecucao()  {return Id_CrimeExecucao;}
	public void setId_CrimeExecucao(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CrimeExecucao = ""; CrimeExecucao = "";}else if (!valor.equalsIgnoreCase("")) Id_CrimeExecucao = valor;}
	public String getCrimeExecucaoCodigo()  {return CrimeExecucaoCodigo;}
	public void setCrimeExecucaoCodigo(String valor ) {if (valor!=null) CrimeExecucaoCodigo = valor;}
	public String getArtigo()  {return Artigo;}
	public void setArtigo(String valor ) {if (valor!=null) Artigo = valor;}
	public String getInciso()  {return Inciso;}
	public void setInciso(String valor ) {if (valor!=null) Inciso = valor;}
	public String getLei()  {return Lei;}
	public void setLei(String valor ) {if (valor!=null) Lei = valor;}
	public String getParagrafo()  {return Paragrafo;}
	public void setParagrafo(String valor ) {if (valor!=null) Paragrafo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		if (observacao!=null) this.observacao = observacao;
	}

	public void copiar(CondenacaoExecucaoDt objeto){
		 if (objeto==null) return;
		Id_CondenacaoExecucao = objeto.getId();
		TempoPena = objeto.getTempoPena();
		Reincidente = objeto.getReincidente();
		DataFato = objeto.getDataFato();
		Id_ProcessoExecucao = objeto.getId_ProcessoExecucao();
		Id_CondenacaoExecucaoSituacao = objeto.getId_CondenacaoExecucaoSituacao();
		CondenacaoExecucaoSituacao = objeto.getCondenacaoExecucaoSituacao();
		ProcessoNumero = objeto.getProcessoNumero();
		Id_CrimeExecucao = objeto.getId_CrimeExecucao();
		CrimeExecucao = objeto.getCrimeExecucao();
		CrimeExecucaoCodigo = objeto.getCrimeExecucaoCodigo();
		Artigo = objeto.getArtigo();
		Inciso = objeto.getInciso();
		Lei = objeto.getLei();
		Paragrafo = objeto.getParagrafo();
		CodigoTemp = objeto.getCodigoTemp();
		observacao = objeto.getObservacao();
	}

	public void limpar(){
		Id_CondenacaoExecucao="";
		TempoPena="";
		Reincidente="";
		DataFato="";
		Id_ProcessoExecucao="";
		Id_CondenacaoExecucaoSituacao="";
		CondenacaoExecucaoSituacao="";
		ProcessoNumero="";
		Id_CrimeExecucao="";
		CrimeExecucao="";
		CrimeExecucaoCodigo="";
		Artigo="";
		Inciso="";
		Lei="";
		Paragrafo="";
		CodigoTemp="";
		observacao = "";
	}


	public String getPropriedades(){
		return "[Id_CondenacaoExecucao:" + Id_CondenacaoExecucao + ";TempoPena:" + TempoPena + ";Reincidente:" + Reincidente + ";DataFato:" + DataFato + ";Id_ProcessoExecucao:" + Id_ProcessoExecucao + ";Id_CondenacaoExecucaoSituacao:" + Id_CondenacaoExecucaoSituacao + ";CondenacaoExecucaoSituacao:" + CondenacaoExecucaoSituacao + ";ProcessoNumero:" + ProcessoNumero + ";Id_CrimeExecucao:" + Id_CrimeExecucao + ";CrimeExecucao:" + CrimeExecucao + ";CrimeExecucaoCodigo:" + CrimeExecucaoCodigo + ";Artigo:" + Artigo + ";Inciso:" + Inciso + ";Lei:" + Lei + ";Paragrafo:" + Paragrafo + ";CodigoTemp:" + CodigoTemp + ";Observação:" + observacao + "]";
	}


} 
