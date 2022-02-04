package br.gov.go.tj.projudi.dt;

public class ProcessoCriminalDtGen extends Dados{

	private static final long serialVersionUID = -8076854966835119771L;
	private String Id_ProcessoCriminal;
	private String ProcessoNumero;
	private String Id_Processo;
	private String ReuPreso;
	private String Inquerito;
	private String DataPrisao;
	private String DataOferecimentoDenuncia;
	private String DataRecebimentoDenuncia;
	private String DataTransacaoPenal;
	private String DataSuspensaoPenal;
	private String DataFato;
	private String DataPrescricao;
	private String DataBaixa;

//---------------------------------------------------------
	public ProcessoCriminalDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoCriminal;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoCriminal = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getReuPreso()  {return ReuPreso;}
	public void setReuPreso(String valor ) {if (valor!=null) ReuPreso = valor;}
	public String getInquerito()  {return Inquerito;}
	public void setInquerito(String valor ) {if (valor!=null) Inquerito = valor;}
	public String getDataPrisao()  {return DataPrisao;}
	public void setDataPrisao(String valor ) {if (valor!=null) DataPrisao = valor;}
	public String getDataOferecimentoDenuncia()  {return DataOferecimentoDenuncia;}
	public void setDataOferecimentoDenuncia(String valor ) {if (valor!=null) DataOferecimentoDenuncia = valor;}
	public String getDataRecebimentoDenuncia()  {return DataRecebimentoDenuncia;}
	public void setDataRecebimentoDenuncia(String valor ) {if (valor!=null) DataRecebimentoDenuncia = valor;}
	public String getDataTransacaoPenal()  {return DataTransacaoPenal;}
	public void setDataTransacaoPenal(String valor ) {if (valor!=null) DataTransacaoPenal = valor;}
	public String getDataSuspensaoPenal()  {return DataSuspensaoPenal;}
	public void setDataSuspensaoPenal(String valor ) {if (valor!=null) DataSuspensaoPenal = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getDataFato() {return DataFato;}
	public void setDataFato(String dataFato) {DataFato = dataFato;}
	public String getDataPrescricao() {return DataPrescricao;}
	public void setDataPrescricao(String dataPrescricao) {DataPrescricao = dataPrescricao;}
	public String getDataBaixa() {return DataBaixa;}
	public void setDataBaixa(String dataBaixa) {DataBaixa = dataBaixa;}

	public void copiar(ProcessoCriminalDt objeto){
		 if (objeto==null) return;
		Id_ProcessoCriminal = objeto.getId();
		Id_Processo = objeto.getId_Processo();
		ProcessoNumero = objeto.getProcessoNumero();
		ReuPreso = objeto.getReuPreso();
		Inquerito = objeto.getInquerito();
		DataPrisao = objeto.getDataPrisao();
		DataOferecimentoDenuncia = objeto.getDataOferecimentoDenuncia();
		DataRecebimentoDenuncia = objeto.getDataRecebimentoDenuncia();
		DataTransacaoPenal = objeto.getDataTransacaoPenal();
		DataSuspensaoPenal = objeto.getDataSuspensaoPenal();
		CodigoTemp = objeto.getCodigoTemp();
		DataFato = objeto.getDataFato();
		DataPrescricao = objeto.getDataPrescricao();
		DataBaixa = objeto.getDataBaixa();
	}

	public void limpar(){
		Id_ProcessoCriminal="";
		Id_Processo="";
		ProcessoNumero="";
		ReuPreso="";
		Inquerito="";
		DataPrisao="";
		DataOferecimentoDenuncia="";
		DataRecebimentoDenuncia="";
		DataTransacaoPenal="";
		DataSuspensaoPenal="";
		DataFato = "";
		CodigoTemp="";
		DataPrescricao = "";
		DataBaixa = "";
	}

	public String getPropriedades(){
		return "[Id_ProcessoCriminal:" + Id_ProcessoCriminal + ";Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";ReuPreso:" + ReuPreso + ";Inquerito:" + Inquerito + ";DataPrisao:" + DataPrisao + ";DataOferecimentoDenuncia:" + DataOferecimentoDenuncia + ";DataRecebimentoDenuncia:" + DataRecebimentoDenuncia + ";DataTransacaoPenal:" + DataTransacaoPenal + ";DataSuspensaoPenal:" + DataSuspensaoPenal + ";DataFato:" + DataFato + ";DataPrescricao:" + DataPrescricao + ";DataBaixa:" + DataBaixa + ";CodigoTemp:" + CodigoTemp + "]";
	}

} 
