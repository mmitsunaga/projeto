package br.gov.go.tj.projudi.dt;

public class ProcessoFaseDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 3779951349370350131L;
    private String Id_ProcessoFase;
	private String ProcessoFase;


	private String ProcessoFaseCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoFaseDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoFase;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoFase = valor;}
	public String getProcessoFase()  {return ProcessoFase;}
	public void setProcessoFase(String valor ) {if (valor!=null) ProcessoFase = valor;}
	public String getProcessoFaseCodigo()  {return ProcessoFaseCodigo;}
	public void setProcessoFaseCodigo(String valor ) {if (valor!=null) ProcessoFaseCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoFaseDt objeto){
		Id_ProcessoFase = objeto.getId();
		ProcessoFase = objeto.getProcessoFase();
		ProcessoFaseCodigo = objeto.getProcessoFaseCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoFase="";
		ProcessoFase="";
		ProcessoFaseCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoFase:" + Id_ProcessoFase + ";ProcessoFase:" + ProcessoFase + ";ProcessoFaseCodigo:" + ProcessoFaseCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
