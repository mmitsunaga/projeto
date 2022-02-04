package br.gov.go.tj.projudi.dt;

public class ProcessoParteAusenciaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -5463821988544574747L;
    private String Id_ProcessoParteAusencia;
	private String ProcessoParteAusencia;


	private String ProcessoParteAusenciaCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoParteAusenciaDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteAusencia;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParteAusencia = valor;}
	public String getProcessoParteAusencia()  {return ProcessoParteAusencia;}
	public void setProcessoParteAusencia(String valor ) {if (valor!=null) ProcessoParteAusencia = valor;}
	public String getProcessoParteAusenciaCodigo()  {return ProcessoParteAusenciaCodigo;}
	public void setProcessoParteAusenciaCodigo(String valor ) {if (valor!=null) ProcessoParteAusenciaCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoParteAusenciaDt objeto){
		Id_ProcessoParteAusencia = objeto.getId();
		ProcessoParteAusencia = objeto.getProcessoParteAusencia();
		ProcessoParteAusenciaCodigo = objeto.getProcessoParteAusenciaCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoParteAusencia="";
		ProcessoParteAusencia="";
		ProcessoParteAusenciaCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoParteAusencia:" + Id_ProcessoParteAusencia + ";ProcessoParteAusencia:" + ProcessoParteAusencia + ";ProcessoParteAusenciaCodigo:" + ProcessoParteAusenciaCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
