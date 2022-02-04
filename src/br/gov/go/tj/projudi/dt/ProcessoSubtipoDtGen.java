package br.gov.go.tj.projudi.dt;

public class ProcessoSubtipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 3205862664391247222L;
    private String Id_ProcessoSubtipo;
	private String ProcessoSubtipo;


	private String ProcessoSubtipoCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoSubtipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoSubtipo;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoSubtipo = valor;}
	public String getProcessoSubtipo()  {return ProcessoSubtipo;}
	public void setProcessoSubtipo(String valor ) {if (valor!=null) ProcessoSubtipo = valor;}
	public String getProcessoSubtipoCodigo()  {return ProcessoSubtipoCodigo;}
	public void setProcessoSubtipoCodigo(String valor ) {if (valor!=null) ProcessoSubtipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoSubtipoDt objeto){
		 if (objeto==null) return;
		Id_ProcessoSubtipo = objeto.getId();
		ProcessoSubtipo = objeto.getProcessoSubtipo();
		ProcessoSubtipoCodigo = objeto.getProcessoSubtipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoSubtipo="";
		ProcessoSubtipo="";
		ProcessoSubtipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoSubtipo:" + Id_ProcessoSubtipo + ";ProcessoSubtipo:" + ProcessoSubtipo + ";ProcessoSubtipoCodigo:" + ProcessoSubtipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
