package br.gov.go.tj.projudi.dt;

public class ProcessoExecucaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6358045896261649442L;
	private String Id_ProcessoExecucao;
	private String ProcessoExecucaoPenalNumero;

	private String Id_ProcessoExecucaoPenal;

	private String ProcessoExecucaoPenalDigitoVerificador;
	private String Id_ProcessoDependente;
	private String Id_Serventia;
	private String Id_ProcessoAcaoPenal;
	private String ProcessoAcaoPenalNumero;
	private String Id_CidadeOrigem;
	private String CidadeOrigem;
	private String EstadoOrigem;
	private String UfOrigem;
	private String DataAcordao;
	private String DataDistribuicao;
	private String DataPronuncia;
	private String DataSentenca;
	private String DataTransitoJulgado;
	private String DataTransitoJulgadoMP;
	private String DataDenuncia;
	private String DataAdmonitoria;
	private String DataInicioCumprimentoPena;
	private String NumeroAcaoPenal;
	private String VaraOrigem;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoExecucaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoExecucao;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoExecucao = valor;}
	public String getProcessoExecucaoPenalNumero()  {return ProcessoExecucaoPenalNumero;}
	public void setProcessoExecucaoPenalNumero(String valor ) {if (valor!=null) ProcessoExecucaoPenalNumero = valor;}
	public String getId_ProcessoExecucaoPenal()  {return Id_ProcessoExecucaoPenal;}
	public void setId_ProcessoExecucaoPenal(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoExecucaoPenal = ""; ProcessoExecucaoPenalNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoExecucaoPenal = valor;}
	public String getProcessoExecucaoPenalDigitoVerificador()  {return ProcessoExecucaoPenalDigitoVerificador;}
	public void setProcessoExecucaoPenalDigitoVerificador(String valor ) {if (valor!=null) ProcessoExecucaoPenalDigitoVerificador = valor;}
	public String getId_ProcessoDependente()  {return Id_ProcessoDependente;}
	public void setId_ProcessoDependente(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoDependente = ""; Id_Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoDependente = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Id_ProcessoAcaoPenal = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getId_ProcessoAcaoPenal()  {return Id_ProcessoAcaoPenal;}
	public void setId_ProcessoAcaoPenal(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoAcaoPenal = ""; ProcessoAcaoPenalNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoAcaoPenal = valor;}
	public String getProcessoAcaoPenalNumero()  {return ProcessoAcaoPenalNumero;}
	public void setProcessoAcaoPenalNumero(String valor ) {if (valor!=null) ProcessoAcaoPenalNumero = valor;}
	public String getId_CidadeOrigem()  {return Id_CidadeOrigem;}
	public void setId_CidadeOrigem(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CidadeOrigem = ""; CidadeOrigem = "";}else if (!valor.equalsIgnoreCase("")) Id_CidadeOrigem = valor;}
	public String getCidadeOrigem()  {return CidadeOrigem;}
	public void setCidadeOrigem(String valor ) {if (valor!=null) CidadeOrigem = valor;}
	public String getEstadoOrigem()  {return EstadoOrigem;}
	public void setEstadoOrigem(String valor ) {if (valor!=null) EstadoOrigem = valor;}
	public String getUfOrigem()  {return UfOrigem;}
	public void setUfOrigem(String valor ) {if (valor!=null) UfOrigem = valor;}
	public String getDataAcordao()  {return DataAcordao;}
	public void setDataAcordao(String valor ) {if (valor!=null) DataAcordao = valor;}
	public String getDataDistribuicao()  {return DataDistribuicao;}
	public void setDataDistribuicao(String valor ) {if (valor!=null) DataDistribuicao = valor;}
	public String getDataPronuncia()  {return DataPronuncia;}
	public void setDataPronuncia(String valor ) {if (valor!=null) DataPronuncia = valor;}
	public String getDataSentenca()  {return DataSentenca;}
	public void setDataSentenca(String valor ) {if (valor!=null) DataSentenca = valor;}
	public String getDataTransitoJulgado()  {return DataTransitoJulgado;}
	public void setDataTransitoJulgado(String valor ) {if (valor!=null) DataTransitoJulgado = valor;}
	public String getDataTransitoJulgadoMP()  {return DataTransitoJulgadoMP;}
	public void setDataTransitoJulgadoMP(String valor ) {if (valor!=null) DataTransitoJulgadoMP = valor;}
	public String getDataDenuncia()  {return DataDenuncia;}
	public void setDataDenuncia(String valor ) {if (valor!=null) DataDenuncia = valor;}
	public String getDataAdmonitoria()  {return DataAdmonitoria;}
	public void setDataAdmonitoria(String valor ) {if (valor!=null) DataAdmonitoria = valor;}
	public String getDataInicioCumprimentoPena()  {return DataInicioCumprimentoPena;}
	public void setDataInicioCumprimentoPena(String valor ) {if (valor!=null) DataInicioCumprimentoPena = valor;}
	public String getNumeroAcaoPenal()  {return NumeroAcaoPenal;}
	public void setNumeroAcaoPenal(String valor ) {if (valor!=null) NumeroAcaoPenal = valor;}
	public String getVaraOrigem()  {return VaraOrigem;}
	public void setVaraOrigem(String valor ) {if (valor!=null) VaraOrigem = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoExecucaoDt objeto){
		 if (objeto==null) return;
		Id_ProcessoExecucao = objeto.getId();
		Id_ProcessoExecucaoPenal = objeto.getId_ProcessoExecucaoPenal();
		ProcessoExecucaoPenalNumero = objeto.getProcessoExecucaoPenalNumero();
		ProcessoExecucaoPenalDigitoVerificador = objeto.getProcessoExecucaoPenalDigitoVerificador();
		Id_ProcessoDependente = objeto.getId_ProcessoDependente();
		Id_Serventia = objeto.getId_Serventia();
		Id_ProcessoAcaoPenal = objeto.getId_ProcessoAcaoPenal();
		ProcessoAcaoPenalNumero = objeto.getProcessoAcaoPenalNumero();
		Id_CidadeOrigem = objeto.getId_CidadeOrigem();
		CidadeOrigem = objeto.getCidadeOrigem();
		EstadoOrigem = objeto.getEstadoOrigem();
		UfOrigem = objeto.getUfOrigem();
		DataAcordao = objeto.getDataAcordao();
		DataDistribuicao = objeto.getDataDistribuicao();
		DataPronuncia = objeto.getDataPronuncia();
		DataSentenca = objeto.getDataSentenca();
		DataTransitoJulgado = objeto.getDataTransitoJulgado();
		DataTransitoJulgadoMP = objeto.getDataTransitoJulgadoMP();
		DataDenuncia = objeto.getDataDenuncia();
		DataAdmonitoria = objeto.getDataAdmonitoria();
		DataInicioCumprimentoPena = objeto.getDataInicioCumprimentoPena();
		NumeroAcaoPenal = objeto.getNumeroAcaoPenal();
		VaraOrigem = objeto.getVaraOrigem();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoExecucao="";
		Id_ProcessoExecucaoPenal="";
		ProcessoExecucaoPenalNumero="";
		ProcessoExecucaoPenalDigitoVerificador="";
		Id_ProcessoDependente="";
		Id_Serventia="";
		Id_ProcessoAcaoPenal="";
		ProcessoAcaoPenalNumero="";
		Id_CidadeOrigem="";
		CidadeOrigem="";
		EstadoOrigem="";
		UfOrigem="";
		DataAcordao="";
		DataDistribuicao="";
		DataPronuncia="";
		DataSentenca="";
		DataTransitoJulgado="";
		DataTransitoJulgadoMP="";
		DataDenuncia="";
		DataAdmonitoria="";
		DataInicioCumprimentoPena="";
		NumeroAcaoPenal="";
		VaraOrigem="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoExecucao:" + Id_ProcessoExecucao + ";Id_ProcessoExecucaoPenal:" + Id_ProcessoExecucaoPenal + ";ProcessoExecucaoPenalNumero:" + ProcessoExecucaoPenalNumero + ";ProcessoExecucaoPenalDigitoVerificador:" + ProcessoExecucaoPenalDigitoVerificador + ";Id_ProcessoDependente:" + Id_ProcessoDependente + ";Id_Serventia:" + Id_Serventia + ";Id_ProcessoAcaoPenal:" + Id_ProcessoAcaoPenal + ";ProcessoAcaoPenalNumero:" + ProcessoAcaoPenalNumero + ";Id_CidadeOrigem:" + Id_CidadeOrigem + ";CidadeOrigem:" + CidadeOrigem + ";EstadoOrigem:" + EstadoOrigem + ";UfOrigem:" + UfOrigem + ";DataAcordao:" + DataAcordao + ";DataDistribuicao:" + DataDistribuicao + ";DataPronuncia:" + DataPronuncia + ";DataSentenca:" + DataSentenca + ";DataTransitoJulgado:" + DataTransitoJulgado + ";DataTransitoJulgadoMP:" + DataTransitoJulgadoMP + ";DataDenuncia:" + DataDenuncia + ";DataAdmonitoria:" + DataAdmonitoria + ";DataInicioCumprimentoPena:" + DataInicioCumprimentoPena + ";NumeroAcaoPenal:" + NumeroAcaoPenal + ";VaraOrigem:" + VaraOrigem + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
