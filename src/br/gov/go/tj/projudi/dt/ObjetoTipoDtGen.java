package br.gov.go.tj.projudi.dt;

public class ObjetoTipoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8176944468105193723L;
	protected String Id_ObjetoTipo;
	protected String ObjetoTipo;


	protected String Codigo;

//---------------------------------------------------------
	public ObjetoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ObjetoTipo;}
	public void setId(String valor ) { if(valor!=null) Id_ObjetoTipo = valor;}
	public String getObjetoTipo()  {return ObjetoTipo;}
	public void setObjetoTipo(String valor ) { if (valor!=null) ObjetoTipo = valor;}
	public String getCodigo()  {return Codigo;}
	public void setCodigo(String valor ) { if (valor!=null) Codigo = valor;}


	public void copiar(ObjetoTipoDt objeto){
		 if (objeto==null) return;
		Id_ObjetoTipo = objeto.getId();
		ObjetoTipo = objeto.getObjetoTipo();
		Codigo = objeto.getCodigo();
	}

	public void limpar(){
		Id_ObjetoTipo="";
		ObjetoTipo="";
		Codigo="";
	}

	public String toJson(){
		return "{\"Id\":\"" + getId() +  "\",\"ObjetoTipo\":\"" + getObjetoTipo() + "\",\"Codigo\":\"" + getCodigo() + "\"}";
	}

	public String getPropriedades(){
		return "[Id_ObjetoTipo:" + Id_ObjetoTipo + ";ObjetoTipo:" + ObjetoTipo + ";Codigo:" + Codigo + "]";
	}


} 
