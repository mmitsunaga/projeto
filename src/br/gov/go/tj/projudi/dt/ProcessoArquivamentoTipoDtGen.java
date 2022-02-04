package br.gov.go.tj.projudi.dt;

public class ProcessoArquivamentoTipoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2102254616302624088L;
	protected String Id_PrococessoArquivamentoTipo;
	protected String ProcessoArquivamentoTipo;


	protected String CodigoTemp;

//---------------------------------------------------------
	public ProcessoArquivamentoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_PrococessoArquivamentoTipo;}
	public void setId(String valor ) { if(valor!=null) Id_PrococessoArquivamentoTipo = valor;}
	public String getProcessoArquivamentoTipo()  {return ProcessoArquivamentoTipo;}
	public void setProcessoArquivamentoTipo(String valor ) { if (valor!=null) ProcessoArquivamentoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoArquivamentoTipoDt objeto){
		 if (objeto==null) return;
		Id_PrococessoArquivamentoTipo = objeto.getId();
		ProcessoArquivamentoTipo = objeto.getProcessoArquivamentoTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PrococessoArquivamentoTipo="";
		ProcessoArquivamentoTipo="";
		CodigoTemp="";
	}

	public String toJson(){
		return "{\"Id\":\"" + getId() + "\",\"Id_PrococessoArquivamentoTipo\":\"" + getId() + "\",\"ProcessoArquivamentoTipo\":\"" + getProcessoArquivamentoTipo() + "\",\"CodigoTemp\":\"" + getCodigoTemp() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_PrococessoArquivamentoTipo:" + Id_PrococessoArquivamentoTipo + ";ProcessoArquivamentoTipo:" + ProcessoArquivamentoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
