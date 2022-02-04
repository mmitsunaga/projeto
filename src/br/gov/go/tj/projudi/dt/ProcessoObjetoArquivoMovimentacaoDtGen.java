package br.gov.go.tj.projudi.dt;

public class ProcessoObjetoArquivoMovimentacaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9058471776433236475L;
	
	protected String Id_ProcessoObjetoArquivoMovimentacao;
	protected String ProcessoObjetoArquivoMovimentacao;


	protected String Id_ProcessoObjetoArquivo;
	protected String ProcessoObjetoArquivo;
	protected String DataMovimentacao;
	protected String DataRetorno;
	protected String CodigoTemp;

//---------------------------------------------------------
	public ProcessoObjetoArquivoMovimentacaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoObjetoArquivoMovimentacao;}
	public void setId(String valor ) { if(valor!=null) Id_ProcessoObjetoArquivoMovimentacao = valor;}
	public String getProcessoObjetoArquivoMovimentacao()  {return ProcessoObjetoArquivoMovimentacao;}
	public void setProcessoObjetoArquivoMovimentacao(String valor ) { if (valor!=null) ProcessoObjetoArquivoMovimentacao = valor;}
	public String getId_ProcessoObjetoArquivo()  {return Id_ProcessoObjetoArquivo;}
	public void setId_ProcessoObjetoArquivo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ProcessoObjetoArquivo = ""; ProcessoObjetoArquivo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoObjetoArquivo = valor;}
	public String getProcessoObjetoArquivo()  {return ProcessoObjetoArquivo;}
	public void setProcessoObjetoArquivo(String valor ) { if (valor!=null) ProcessoObjetoArquivo = valor;}
	public String getDataMovimentacao()  {return DataMovimentacao;}
	public void setDataMovimentacao(String valor ) { if (valor!=null) DataMovimentacao = valor;}
	public String getDataRetorno()  {return DataRetorno;}
	public void setDataRetorno(String valor ) { if (valor!=null) DataRetorno = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoObjetoArquivoMovimentacaoDt objeto){
		 if (objeto==null) return;
		Id_ProcessoObjetoArquivoMovimentacao = objeto.getId();
		ProcessoObjetoArquivoMovimentacao = objeto.getProcessoObjetoArquivoMovimentacao();
		Id_ProcessoObjetoArquivo = objeto.getId_ProcessoObjetoArquivo();
		ProcessoObjetoArquivo = objeto.getProcessoObjetoArquivo();
		DataMovimentacao = objeto.getDataMovimentacao();
		DataRetorno = objeto.getDataRetorno();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoObjetoArquivoMovimentacao="";
		ProcessoObjetoArquivoMovimentacao="";
		Id_ProcessoObjetoArquivo="";
		ProcessoObjetoArquivo="";
		DataMovimentacao="";
		DataRetorno="";
		CodigoTemp="";
	}

	public String toJson(){
		return "{\"Id\":\"" + getId() + "\",\"ProcessoObjetoArquivoMovimentacao\":\"" + getProcessoObjetoArquivoMovimentacao() + "\",\"Id_ProcessoObjetoArquivo\":\"" + getId_ProcessoObjetoArquivo() + "\",\"ProcessoObjetoArquivo\":\"" + getProcessoObjetoArquivo() + "\",\"DataMovimentacao\":\"" + getDataMovimentacao() + "\",\"DataRetorno\":\"" + getDataRetorno() + "\",\"CodigoTemp\":\"" + getCodigoTemp() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_ProcessoObjetoArquivoMovimentacao:" + Id_ProcessoObjetoArquivoMovimentacao + ";ProcessoObjetoArquivoMovimentacao:" + ProcessoObjetoArquivoMovimentacao + ";Id_ProcessoObjetoArquivo:" + Id_ProcessoObjetoArquivo + ";ProcessoObjetoArquivo:" + ProcessoObjetoArquivo + ";DataMovimentacao:" + DataMovimentacao + ";DataRetorno:" + DataRetorno + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
