package br.gov.go.tj.projudi.dt;

public class GuiaCustaModeloDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -301180193691900418L;
    private String Id_GuiaCustaModelo;
	private String GuiaCustaModelo;


	private String Id_GuiaModelo;
	private String GuiaModelo;
	private String Id_Custa;
	private String CodigoRegimento;
	private String CodigoTemp;

//---------------------------------------------------------
	public GuiaCustaModeloDtGen() {

		limpar();

	}

	public String getId()  {return Id_GuiaCustaModelo;}
	public void setId(String valor ) {if(valor!=null) Id_GuiaCustaModelo = valor;}
	public String getGuiaCustaModelo()  {return GuiaCustaModelo;}
	public void setGuiaCustaModelo(String valor ) {if (valor!=null) GuiaCustaModelo = valor;}
	public String getId_GuiaModelo()  {return Id_GuiaModelo;}
	public void setId_GuiaModelo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_GuiaModelo = ""; GuiaModelo = "";}else if (!valor.equalsIgnoreCase("")) Id_GuiaModelo = valor;}
	public String getGuiaModelo()  {return GuiaModelo;}
	public void setGuiaModelo(String valor ) {if (valor!=null) GuiaModelo = valor;}
	public String getId_Custa()  {return Id_Custa;}
	public void setId_Custa(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Custa = ""; CodigoRegimento = "";}else if (!valor.equalsIgnoreCase("")) Id_Custa = valor;}
	public String getCodigoRegimento()  {return CodigoRegimento;}
	public void setCodigoRegimento(String valor ) {if (valor!=null) CodigoRegimento = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_Custa() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getCodigoRegimento() + "</strong>  </li>  ";
		return stTemp;
	}

	public void copiar(GuiaCustaModeloDt objeto){
		 if (objeto==null) return;
		Id_GuiaCustaModelo = objeto.getId();
		GuiaCustaModelo = objeto.getGuiaCustaModelo();
		Id_GuiaModelo = objeto.getId_GuiaModelo();
		GuiaModelo = objeto.getGuiaModelo();
		Id_Custa = objeto.getId_Custa();
		CodigoRegimento = objeto.getCodigoRegimento();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_GuiaCustaModelo="";
		GuiaCustaModelo="";
		Id_GuiaModelo="";
		GuiaModelo="";
		Id_Custa="";
		CodigoRegimento="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_GuiaCustaModelo:" + Id_GuiaCustaModelo + ";GuiaCustaModelo:" + GuiaCustaModelo + ";Id_GuiaModelo:" + Id_GuiaModelo + ";GuiaModelo:" + GuiaModelo + ";Id_Custa:" + Id_Custa + ";CodigoRegimento:" + CodigoRegimento + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
