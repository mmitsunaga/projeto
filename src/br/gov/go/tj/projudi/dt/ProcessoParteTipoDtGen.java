package br.gov.go.tj.projudi.dt;

public class ProcessoParteTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 5339651411496091665L;
    private String Id_ProcessoParteTipo;
	private String ProcessoParteTipo;


	private String ProcessoParteTipoCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoParteTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteTipo;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParteTipo = valor;}
	public String getProcessoParteTipo()  {return ProcessoParteTipo;}
	public void setProcessoParteTipo(String valor ) {if (valor!=null) ProcessoParteTipo = valor;}
	public String getProcessoParteTipoCodigo()  {return ProcessoParteTipoCodigo;}
	public void setProcessoParteTipoCodigo(String valor ) {if (valor!=null) ProcessoParteTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoParteTipoDt objeto){
		Id_ProcessoParteTipo = objeto.getId();
		ProcessoParteTipo = objeto.getProcessoParteTipo();
		ProcessoParteTipoCodigo = objeto.getProcessoParteTipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoParteTipo="";
		ProcessoParteTipo="";
		ProcessoParteTipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoParteTipo:" + Id_ProcessoParteTipo + ";ProcessoParteTipo:" + ProcessoParteTipo + ";ProcessoParteTipoCodigo:" + ProcessoParteTipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
