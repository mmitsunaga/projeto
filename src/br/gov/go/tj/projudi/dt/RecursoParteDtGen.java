package br.gov.go.tj.projudi.dt;

public class RecursoParteDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4237440752654300571L;
	private String Id_RecursoParte;
	private String ProcessoParteTipo;

	private String Id_Recurso;
	private String DataEnvio;
	private String Id_ProcessoParte;
	private String Nome;
	private String Id_ProcessoParteTipo;

	private String DataBaixa;
	private String ProcessoParteTipoCodigo;
	private String Id_Processo;
	private String DataRetorno;
	
	private String Id_ProcessoTipo;
	private String ProcessoTipo;

//---------------------------------------------------------
	public RecursoParteDtGen() {

		limpar();

	}

	public String getId()  {return Id_RecursoParte;}
	public void setId(String valor ) {if(valor!=null) Id_RecursoParte = valor;}
	public String getProcessoParteTipo()  {return ProcessoParteTipo;}
	public void setProcessoParteTipo(String valor ) {if (valor!=null) ProcessoParteTipo = valor;}
	public String getId_Recurso()  {return Id_Recurso;}
	public void setId_Recurso(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Recurso = ""; DataEnvio = "";}else if (!valor.equalsIgnoreCase("")) Id_Recurso = valor;}
	public String getDataEnvio()  {return DataEnvio;}
	public void setDataEnvio(String valor ) {if (valor!=null) DataEnvio = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getId_ProcessoParteTipo()  {return Id_ProcessoParteTipo;}
	public void setId_ProcessoParteTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParteTipo = ""; ProcessoParteTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParteTipo = valor;}
	public String getDataBaixa()  {return DataBaixa;}
	public void setDataBaixa(String valor ) {if (valor!=null) DataBaixa = valor;}
	public String getProcessoParteTipoCodigo()  {return ProcessoParteTipoCodigo;}
	public void setProcessoParteTipoCodigo(String valor ) {if (valor!=null) ProcessoParteTipoCodigo = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; DataRetorno = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getDataRetorno()  {return DataRetorno;}
	public void setDataRetorno(String valor ) {if (valor!=null) DataRetorno = valor;}

	public String getId_ProcessoTipo()  {
		return Id_ProcessoTipo;
	}
	
	public void setId_ProcessoTipo(String valor ) {
		if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;
	}
	
	public String getProcessoTipo()  {
		return ProcessoTipo;
	}
	
	public void setProcessoTipo(String valor ) {
		if (valor!=null) ProcessoTipo = valor;
	}

	public void copiar(RecursoParteDt objeto){
		if (objeto==null) return;
		Id_RecursoParte = objeto.getId();
		Id_Recurso = objeto.getId_Recurso();
		DataEnvio = objeto.getDataEnvio();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		Nome = objeto.getNome();
		Id_ProcessoParteTipo = objeto.getId_ProcessoParteTipo();
		ProcessoParteTipo = objeto.getProcessoParteTipo();
		DataBaixa = objeto.getDataBaixa();
		ProcessoParteTipoCodigo = objeto.getProcessoParteTipoCodigo();
		Id_Processo = objeto.getId_Processo();
		DataRetorno = objeto.getDataRetorno();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		ProcessoTipo = objeto.getProcessoTipo();
	}

	public void limpar(){
		Id_RecursoParte="";
		Id_Recurso="";
		DataEnvio="";
		Id_ProcessoParte="";
		Nome="";
		Id_ProcessoParteTipo="";
		ProcessoParteTipo="";
		DataBaixa="";
		ProcessoParteTipoCodigo="";
		Id_Processo="";
		DataRetorno="";
		Id_ProcessoTipo = "";
		ProcessoTipo = "";
	}


	public String getPropriedades(){
		return "[Id_RecursoParte:" + Id_RecursoParte + ";Id_Recurso:" + Id_Recurso + ";DataEnvio:" + DataEnvio + ";Id_ProcessoParte:" + Id_ProcessoParte + ";Nome:" + Nome + ";Id_ProcessoParteTipo:" + Id_ProcessoParteTipo + ";ProcessoParteTipo:" + ProcessoParteTipo + ";DataBaixa:" + DataBaixa + ";ProcessoParteTipoCodigo:" + ProcessoParteTipoCodigo + ";Id_Processo:" + Id_Processo + ";DataRetorno:" + DataRetorno + ";Id_ProcessoTipo:" + Id_ProcessoTipo + "]";
	}


} 
