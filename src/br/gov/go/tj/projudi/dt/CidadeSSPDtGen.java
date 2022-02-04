package br.gov.go.tj.projudi.dt;

public class CidadeSSPDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 2531395017909725069L;
    private String Id_CidadeSSP;
	private String Cidade;
	
	


	private String Id_CidadeTJ;
	private String CidadeTJ;
	private String Estado;

//---------------------------------------------------------
	public CidadeSSPDtGen() {

		limpar();

	}

	public String getId()  {return Id_CidadeSSP;}
	public void setId(String valor ) {if(valor!=null) Id_CidadeSSP = valor;}
	public String getCidade()  {return Cidade;}
	public void setCidade(String valor ) {if (valor!=null) Cidade = valor;}
	public String getId_CidadeTJ()  {return Id_CidadeTJ;}
	public void setId_CidadeTJ(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CidadeTJ = ""; CidadeTJ = "";}else if (!valor.equalsIgnoreCase("")) Id_CidadeTJ = valor;}
	public String getCidadeTJ()  {return CidadeTJ;}
	public void setCidadeTJ(String valor ) {if (valor!=null) CidadeTJ = valor;}
	public String getEstado()  {return Estado;}
	public void setEstado(String valor ) {if (valor!=null) Estado = valor;}
	
	
	
	


	public void copiar(CidadeSSPDt objeto){
		Id_CidadeSSP = objeto.getId();
		Cidade = objeto.getCidade();
		Id_CidadeTJ = objeto.getId_CidadeTJ();
		CidadeTJ = objeto.getCidadeTJ();
		Estado = objeto.getEstado();
	}

	public void limpar(){
		Id_CidadeSSP="";
		Cidade="";
		Id_CidadeTJ="";
		CidadeTJ="";
		Estado="";
	}


	public String getPropriedades(){
		return "[Id_CidadeSSP:" + Id_CidadeSSP + ";Cidade:" + Cidade + ";Id_CidadeTJ:" + Id_CidadeTJ + ";CidadeTJ:" + CidadeTJ + ";Estado:" + Estado + "]";
	}


} 
