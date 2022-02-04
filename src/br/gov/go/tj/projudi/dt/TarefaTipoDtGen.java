package br.gov.go.tj.projudi.dt;

public class TarefaTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 4900683175519484212L;
    private String Id_TarefaTipo;
	private String TarefaTipo;


	private String CodigoTemp;

//---------------------------------------------------------
	public TarefaTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_TarefaTipo;}
	public void setId(String valor ) {if(valor!=null) Id_TarefaTipo = valor;}
	public String getTarefaTipo()  {return TarefaTipo;}
	public void setTarefaTipo(String valor ) {if (valor!=null) TarefaTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(TarefaTipoDt objeto){
		 if (objeto==null) return;
		Id_TarefaTipo = objeto.getId();
		TarefaTipo = objeto.getTarefaTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_TarefaTipo="";
		TarefaTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_TarefaTipo:" + Id_TarefaTipo + ";TarefaTipo:" + TarefaTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
