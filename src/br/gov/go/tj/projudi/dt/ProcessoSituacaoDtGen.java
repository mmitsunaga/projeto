package br.gov.go.tj.projudi.dt;

public class ProcessoSituacaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -4554825091961388124L;
    private String Id_ProcessoSituacao;
	private String ProcessoSituacao;
	
	


	private String ProcessoSituacaoCodigo;
	

//---------------------------------------------------------
	public ProcessoSituacaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoSituacao;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoSituacao = valor;}
	public String getProcessoSituacao()  {return ProcessoSituacao;}
	public void setProcessoSituacao(String valor ) {if (valor!=null) ProcessoSituacao = valor;}
	public String getProcessoSituacaoCodigo()  {return ProcessoSituacaoCodigo;}
	public void setProcessoSituacaoCodigo(String valor ) {if (valor!=null) ProcessoSituacaoCodigo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(ProcessoSituacaoDt objeto){
		Id_ProcessoSituacao = objeto.getId();
		ProcessoSituacao = objeto.getProcessoSituacao();
		ProcessoSituacaoCodigo = objeto.getProcessoSituacaoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoSituacao="";
		ProcessoSituacao="";
		ProcessoSituacaoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoSituacao:" + Id_ProcessoSituacao + ";ProcessoSituacao:" + ProcessoSituacao + ";ProcessoSituacaoCodigo:" + ProcessoSituacaoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
