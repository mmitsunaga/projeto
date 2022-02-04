package br.gov.go.tj.projudi.dt;

public class RacaDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8565778763789943850L;
	protected String Id_Raca;
	protected String Raca;


	protected String CodigoTemp;

//---------------------------------------------------------
	public RacaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Raca;}
	public void setId(String valor ) {if(valor!=null) Id_Raca = valor;}
	public String getRaca()  {return Raca;}
	public void setRaca(String valor ) {if (valor!=null) Raca = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(RacaDt objeto){
		 if (objeto==null) return;
		Id_Raca = objeto.getId();
		Raca = objeto.getRaca();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Raca="";
		Raca="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Raca:" + Id_Raca + ";Raca:" + Raca + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
