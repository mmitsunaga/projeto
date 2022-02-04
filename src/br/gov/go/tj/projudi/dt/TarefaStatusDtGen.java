package br.gov.go.tj.projudi.dt;

public class TarefaStatusDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -1025389225668823433L;
    private String Id_TarefaStatus;
	private String TarefaStatus;
	private String TarefaStatusCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public TarefaStatusDtGen() {

		limpar();

	}

	public String getId()  {return Id_TarefaStatus;}
	public void setId(String valor ) {if(valor!=null) Id_TarefaStatus = valor;}
	public String getTarefaStatus()  {return TarefaStatus;}
	public void setTarefaStatus(String valor ) {if (valor!=null) TarefaStatus = valor;}
	public String getTarefaStatusCodigo()  {return TarefaStatusCodigo;}
	public void setTarefaStatusCodigo(String valor ) {if (valor!=null) TarefaStatusCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(TarefaStatusDt objeto){
		 if (objeto==null) return;
		Id_TarefaStatus = objeto.getId();
		TarefaStatus = objeto.getTarefaStatus();
		TarefaStatusCodigo = objeto.getTarefaStatusCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_TarefaStatus="";
		TarefaStatus="";
		TarefaStatusCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_TarefaStatus:" + Id_TarefaStatus + ";TarefaStatus:" + TarefaStatus + ";TarefaStatusCodigo:" + TarefaStatusCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
