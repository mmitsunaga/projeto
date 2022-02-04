package br.gov.go.tj.projudi.dt;

public class ProjetoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 8549649801757956441L;
    private String Id_Projeto;
	private String Projeto;


	private String CodigoTemp;

//---------------------------------------------------------
	public ProjetoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Projeto;}
	public void setId(String valor ) {if(valor!=null) Id_Projeto = valor;}
	public String getProjeto()  {return Projeto;}
	public void setProjeto(String valor ) {if (valor!=null) Projeto = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProjetoDt objeto){
		 if (objeto==null) return;
		Id_Projeto = objeto.getId();
		Projeto = objeto.getProjeto();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Projeto="";
		Projeto="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Projeto:" + Id_Projeto + ";Projeto:" + Projeto + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
