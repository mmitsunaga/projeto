package br.gov.go.tj.projudi.dt;

public class RecursoDtGen extends Dados{

	private static final long serialVersionUID = -601741362859851756L;
	private String Id_Recurso;
	private String ProcessoNumero;

	private String Id_Processo;

	private String DigitoVerificador;
	private String ProcessoNumeroCompleto;
	private String Id_ServentiaOrigem;
	private String Id_AreaDistribuicaoOrigem;
	private String ServentiaOrigem;
	private String Id_ServentiaRecurso;
	private String ServentiaRecurso;
	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String DataEnvio;
	private String DataRecebimento;
	private String DataRetorno;
	private String ProcessoTipoCodigo;

	public RecursoDtGen() {
		limpar();
	}

	public String getId()  {return Id_Recurso;}
	public void setId(String valor ) {if(valor!=null) Id_Recurso = valor;}
	public String getProcessoNumero()  {return ProcessoNumero;}
	public void setProcessoNumero(String valor ) {if (valor!=null) ProcessoNumero = valor;}
	public String getId_Processo()  {return Id_Processo;}
	public void setId_Processo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Processo = ""; ProcessoNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Processo = valor;}
	public String getDigitoVerificador()  {return DigitoVerificador;}
	public void setDigitoVerificador(String valor ) {if (valor!=null) DigitoVerificador = valor;}
	public String getProcessoNumeroCompleto()  {return ProcessoNumeroCompleto;}
	public void setProcessoNumeroCompleto(String valor ) {if (valor!=null) ProcessoNumeroCompleto = valor;}
	public String getId_ServentiaOrigem()  {return Id_ServentiaOrigem;}
	public void setId_ServentiaOrigem(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaOrigem = ""; ServentiaOrigem = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaOrigem = valor;}
	public String getServentiaOrigem()  {return ServentiaOrigem;}
	public String getId_AreaDistribuicaoOrigem()  {return Id_AreaDistribuicaoOrigem;}
	public void setId_AreaDistribuicaoOrigem(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AreaDistribuicaoOrigem = "";}else if (!valor.equalsIgnoreCase("")) Id_AreaDistribuicaoOrigem = valor;}
	public void setServentiaOrigem(String valor ) {if (valor!=null) ServentiaOrigem = valor;}
	public String getId_ServentiaRecurso()  {return Id_ServentiaRecurso;}
	public void setId_ServentiaRecurso(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaRecurso = ""; ServentiaRecurso = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaRecurso = valor;}
	public String getServentiaRecurso()  {return ServentiaRecurso;}
	public void setServentiaRecurso(String valor ) {if (valor!=null) ServentiaRecurso = valor;}
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getDataEnvio()  {return DataEnvio;}
	public void setDataEnvio(String valor ) {if (valor!=null) DataEnvio = valor;}
	public String getDataRecebimento()  {return DataRecebimento;}
	public void setDataRecebimento(String valor ) {if (valor!=null) DataRecebimento = valor;}
	public String getDataRetorno()  {return DataRetorno;}
	public void setDataRetorno(String valor ) {if (valor!=null) DataRetorno = valor;}
	public String getProcessoTipoCodigo()  {return ProcessoTipoCodigo;}
	public void setProcessoTipoCodigo(String valor ) {if (valor!=null) ProcessoTipoCodigo = valor;}


	public void copiar(RecursoDt objeto){
		 if (objeto==null) return;
		Id_Recurso = objeto.getId();
		Id_Processo = objeto.getId_Processo();
		ProcessoNumero = objeto.getProcessoNumero();
		DigitoVerificador = objeto.getDigitoVerificador();
		ProcessoNumeroCompleto = objeto.getProcessoNumeroCompleto();
		Id_ServentiaOrigem = objeto.getId_ServentiaOrigem();
		Id_AreaDistribuicaoOrigem = objeto.getId_AreaDistribuicaoOrigem();
		ServentiaOrigem = objeto.getServentiaOrigem();
		Id_ServentiaRecurso = objeto.getId_ServentiaRecurso();
		ServentiaRecurso = objeto.getServentiaRecurso();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		ProcessoTipo = objeto.getProcessoTipo();
		DataEnvio = objeto.getDataEnvio();
		DataRecebimento = objeto.getDataRecebimento();
		DataRetorno = objeto.getDataRetorno();
		ProcessoTipoCodigo = objeto.getProcessoTipoCodigo();
	}

	public void limpar(){
		Id_Recurso="";
		Id_Processo="";
		ProcessoNumero="";
		DigitoVerificador="";
		ProcessoNumeroCompleto="";
		Id_ServentiaOrigem="";
		Id_AreaDistribuicaoOrigem = "";
		ServentiaOrigem="";
		Id_ServentiaRecurso="";
		ServentiaRecurso="";
		Id_ProcessoTipo="";
		ProcessoTipo="";
		DataEnvio="";
		DataRecebimento="";
		DataRetorno="";
		ProcessoTipoCodigo="";
	}

	public String getPropriedades(){
		return "[Id_Recurso:" + Id_Recurso + ";Id_Processo:" + Id_Processo + ";ProcessoNumero:" + ProcessoNumero + ";DigitoVerificador:" + DigitoVerificador + ";ProcessoNumeroCompleto:" + ProcessoNumeroCompleto + ";Id_ServentiaOrigem:" + Id_ServentiaOrigem + ";ServentiaOrigem:" + ServentiaOrigem + ";Id_AreaDistribuicaoOrigem:" + Id_AreaDistribuicaoOrigem + ";Id_ServentiaRecurso:" + Id_ServentiaRecurso + ";ServentiaRecurso:" + ServentiaRecurso + ";Id_ProcessoTipo:" + Id_ProcessoTipo + ";ProcessoTipo:" + ProcessoTipo + ";DataEnvio:" + DataEnvio + ";DataRecebimento:" + DataRecebimento + ";DataRetorno:" + DataRetorno + ";ProcessoTipoCodigo:" + ProcessoTipoCodigo + "]";
	}

} 
