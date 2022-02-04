package br.gov.go.tj.projudi.dt;

public class ComarcaCidadeDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 6103133898595606294L;
    
    private String Id_ComarcaCidade;
	private String Comarca;

	private String Id_Comarca;

	private String Id_Cidade;
	private String Cidade;
	private String CodigoTemp;

//---------------------------------------------------------
	public ComarcaCidadeDtGen() {

		limpar();

	}

	public String getId()  {return Id_ComarcaCidade;}
	public void setId(String valor ) {if(valor!=null) Id_ComarcaCidade = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getId_Cidade()  {return Id_Cidade;}
	public void setId_Cidade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Cidade = ""; Cidade = "";}else if (!valor.equalsIgnoreCase("")) Id_Cidade = valor;}
	public String getCidade()  {return Cidade;}
	public void setCidade(String valor ) {if (valor!=null) Cidade = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_Cidade() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getCidade() + "</strong>  </li>  ";
		return stTemp;
	}

	public void copiar(ComarcaCidadeDt objeto){
		 if (objeto==null) return;
		Id_ComarcaCidade = objeto.getId();
		Id_Comarca = objeto.getId_Comarca();
		Comarca = objeto.getComarca();
		Id_Cidade = objeto.getId_Cidade();
		Cidade = objeto.getCidade();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ComarcaCidade="";
		Id_Comarca="";
		Comarca="";
		Id_Cidade="";
		Cidade="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ComarcaCidade:" + Id_ComarcaCidade + ";Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";Id_Cidade:" + Id_Cidade + ";Cidade:" + Cidade + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
