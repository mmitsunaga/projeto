package br.gov.go.tj.projudi.dt;

public class EstadoCivilDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 8511832412752725285L;
    private String Id_EstadoCivil;
	private String EstadoCivil;
	
	


	

//---------------------------------------------------------
	public EstadoCivilDtGen() {

		limpar();

	}

	public String getId()  {return Id_EstadoCivil;}
	public void setId(String valor ) {if(valor!=null) Id_EstadoCivil = valor;}
	public String getEstadoCivil()  {return EstadoCivil;}
	public void setEstadoCivil(String valor ) {if (valor!=null) EstadoCivil = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	


	public void copiar(EstadoCivilDt objeto){
		Id_EstadoCivil = objeto.getId();
		EstadoCivil = objeto.getEstadoCivil();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_EstadoCivil="";
		EstadoCivil="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_EstadoCivil:" + Id_EstadoCivil + ";EstadoCivil:" + EstadoCivil + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
