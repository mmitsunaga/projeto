package br.gov.go.tj.projudi.dt;

public class ProcessoEncaminhamentoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4080754874079367296L;
	protected String Id_ProcEncaminhamento;
	protected String ProcNumero;

	protected String Id_Proc;

	protected String Id_ServOrigem;
	protected String ServOrigem;
	protected String Id_ServEncaminhamento;
	protected String ServEncaminhamento;
	protected String Id_UsuServEncaminhamento;
	protected String UsuEncaminhamento;
	protected String Id_UsuServRetorno;
	protected String UsuRetorno;
	protected String DataEncaminhamento;
	protected String DataRetorno;
	protected String CodigoTemp;

//---------------------------------------------------------
	public ProcessoEncaminhamentoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcEncaminhamento;}
	public void setId(String valor ) { if(valor!=null) Id_ProcEncaminhamento = valor;}
	public String getProcNumero()  {return ProcNumero;}
	public void setProcNumero(String valor ) { if (valor!=null) ProcNumero = valor;}
	public String getId_Proc()  {return Id_Proc;}
	public void setId_Proc(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_Proc = ""; ProcNumero = "";}else if (!valor.equalsIgnoreCase("")) Id_Proc = valor;}
	public String getId_ServOrigem()  {return Id_ServOrigem;}
	public void setId_ServOrigem(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ServOrigem = ""; ServOrigem = "";}else if (!valor.equalsIgnoreCase("")) Id_ServOrigem = valor;}
	public String getServOrigem()  {return ServOrigem;}
	public void setServOrigem(String valor ) { if (valor!=null) ServOrigem = valor;}
	public String getId_ServEncaminhamento()  {return Id_ServEncaminhamento;}
	public void setId_ServEncaminhamento(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ServEncaminhamento = ""; ServEncaminhamento = "";}else if (!valor.equalsIgnoreCase("")) Id_ServEncaminhamento = valor;}
	public String getServEncaminhamento()  {return ServEncaminhamento;}
	public void setServEncaminhamento(String valor ) { if (valor!=null) ServEncaminhamento = valor;}
	public String getId_UsuServEncaminhamento()  {return Id_UsuServEncaminhamento;}
	public void setId_UsuServEncaminhamento(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuServEncaminhamento = ""; UsuEncaminhamento = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuServEncaminhamento = valor;}
	public String getUsuEncaminhamento()  {return UsuEncaminhamento;}
	public void setUsuEncaminhamento(String valor ) { if (valor!=null) UsuEncaminhamento = valor;}
	public String getId_UsuServRetorno()  {return Id_UsuServRetorno;}
	public void setId_UsuServRetorno(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_UsuServRetorno = ""; UsuRetorno = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuServRetorno = valor;}
	public String getUsuRetorno()  {return UsuRetorno;}
	public void setUsuRetorno(String valor ) { if (valor!=null) UsuRetorno = valor;}
	public String getDataEncaminhamento()  {return DataEncaminhamento;}
	public void setDataEncaminhamento(String valor ) { if (valor!=null) DataEncaminhamento = valor;}
	public String getDataRetorno()  {return DataRetorno;}
	public void setDataRetorno(String valor ) { if (valor!=null) DataRetorno = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoEncaminhamentoDt objeto){
		 if (objeto==null) return;
		Id_ProcEncaminhamento = objeto.getId();
		Id_Proc = objeto.getId_Proc();
		ProcNumero = objeto.getProcNumero();
		Id_ServOrigem = objeto.getId_ServOrigem();
		ServOrigem = objeto.getServOrigem();
		Id_ServEncaminhamento = objeto.getId_ServEncaminhamento();
		ServEncaminhamento = objeto.getServEncaminhamento();
		Id_UsuServEncaminhamento = objeto.getId_UsuServEncaminhamento();
		UsuEncaminhamento = objeto.getUsuEncaminhamento();
		Id_UsuServRetorno = objeto.getId_UsuServRetorno();
		UsuRetorno = objeto.getUsuRetorno();
		DataEncaminhamento = objeto.getDataEncaminhamento();
		DataRetorno = objeto.getDataRetorno();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcEncaminhamento="";
		Id_Proc="";
		ProcNumero="";
		Id_ServOrigem="";
		ServOrigem="";
		Id_ServEncaminhamento="";
		ServEncaminhamento="";
		Id_UsuServEncaminhamento="";
		UsuEncaminhamento="";
		Id_UsuServRetorno="";
		UsuRetorno="";
		DataEncaminhamento="";
		DataRetorno="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcEncaminhamento:" + Id_ProcEncaminhamento + ";Id_Proc:" + Id_Proc + ";ProcNumero:" + ProcNumero + ";Id_ServOrigem:" + Id_ServOrigem + ";ServOrigem:" + ServOrigem + ";Id_ServEncaminhamento:" + Id_ServEncaminhamento + ";ServEncaminhamento:" + ServEncaminhamento + ";Id_UsuServEncaminhamento:" + Id_UsuServEncaminhamento + ";UsuEncaminhamento:" + UsuEncaminhamento + ";Id_UsuServRetorno:" + Id_UsuServRetorno + ";UsuRetorno:" + UsuRetorno + ";DataEncaminhamento:" + DataEncaminhamento + ";DataRetorno:" + DataRetorno + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
