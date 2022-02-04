package br.gov.go.tj.projudi.dt;

public class TarefaPrioridadeDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 6080354994898880808L;
    private String Id_TarefaPrioridade;
	private String TarefaPrioridade;


	private String CodigoTemp;

//---------------------------------------------------------
	public TarefaPrioridadeDtGen() {

		limpar();

	}

	public String getId()  {return Id_TarefaPrioridade;}
	public void setId(String valor ) {if(valor!=null) Id_TarefaPrioridade = valor;}
	public String getTarefaPrioridade()  {return TarefaPrioridade;}
	public void setTarefaPrioridade(String valor ) {if (valor!=null) TarefaPrioridade = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(TarefaPrioridadeDt objeto){
		 if (objeto==null) return;
		Id_TarefaPrioridade = objeto.getId();
		TarefaPrioridade = objeto.getTarefaPrioridade();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_TarefaPrioridade="";
		TarefaPrioridade="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_TarefaPrioridade:" + Id_TarefaPrioridade + ";TarefaPrioridade:" + TarefaPrioridade + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
