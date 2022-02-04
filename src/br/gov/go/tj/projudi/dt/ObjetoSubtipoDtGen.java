package br.gov.go.tj.projudi.dt;

public class ObjetoSubtipoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9200227906727192806L;
	protected String Id_ObjetoSubtipo;
	protected String ObjetoSubtipo;


	protected String Id_ObjetoTipo;
	protected String ObjetoTipo;
	protected String CodigoTemp;

//---------------------------------------------------------
	public ObjetoSubtipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ObjetoSubtipo;}
	public void setId(String valor ) { if(valor!=null) Id_ObjetoSubtipo = valor;}
	public String getObjetoSubtipo()  {return ObjetoSubtipo;}
	public void setObjetoSubtipo(String valor ) { if (valor!=null) ObjetoSubtipo = valor;}
	public String getId_ObjetoTipo()  {return Id_ObjetoTipo;}
	public void setId_ObjetoTipo(String valor ) { if (valor!=null) if (valor.equalsIgnoreCase("null")) { Id_ObjetoTipo = ""; ObjetoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ObjetoTipo = valor;}
	public String getObjetoTipo()  {return ObjetoTipo;}
	public void setObjetoTipo(String valor ) { if (valor!=null) ObjetoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(ObjetoSubtipoDt objeto){
		 if (objeto==null) return;
		Id_ObjetoSubtipo = objeto.getId();
		ObjetoSubtipo = objeto.getObjetoSubtipo();
		Id_ObjetoTipo = objeto.getId_ObjetoTipo();
		ObjetoTipo = objeto.getObjetoTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ObjetoSubtipo="";
		ObjetoSubtipo="";
		Id_ObjetoTipo="";
		ObjetoTipo="";
		CodigoTemp="";
	}

	public String toJson(){
		return "{\"Id\":\"" + getId() +  "\",\"ObjetoSubtipo\":\"" + getObjetoSubtipo() + "\",\"Id_ObjetoTipo\":\"" + getId_ObjetoTipo() + "\",\"ObjetoTipo\":\"" + getObjetoTipo() + "\",\"CodigoTemp\":\"" + getCodigoTemp() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_ObjetoSubtipo:" + Id_ObjetoSubtipo + ";ObjetoSubtipo:" + ObjetoSubtipo + ";Id_ObjetoTipo:" + Id_ObjetoTipo + ";ObjetoTipo:" + ObjetoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
