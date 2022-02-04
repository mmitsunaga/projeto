package br.gov.go.tj.projudi.dt;

public class PonteiroCejuscStatuDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -941465834188092663L;
	protected String Id_PonteiroCejuscStatus;
	protected String PonteiroCejuscStatus;


	protected String CodigoTemp;

//---------------------------------------------------------
	public PonteiroCejuscStatuDtGen() {

		limpar();

	}

	public String getId()  {return Id_PonteiroCejuscStatus;}
	public void setId(String valor ) { if(valor!=null) Id_PonteiroCejuscStatus = valor;}
	public String getPonteiroCejuscStatus()  {return PonteiroCejuscStatus;}
	public void setPonteiroCejuscStatus(String valor ) { if (valor!=null) PonteiroCejuscStatus = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(PonteiroCejuscStatuDt objeto){
		 if (objeto==null) return;
		Id_PonteiroCejuscStatus = objeto.getId();
		PonteiroCejuscStatus = objeto.getPonteiroCejuscStatus();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PonteiroCejuscStatus="";
		PonteiroCejuscStatus="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PonteiroCejuscStatus:" + Id_PonteiroCejuscStatus + ";PonteiroCejuscStatus:" + PonteiroCejuscStatus + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
