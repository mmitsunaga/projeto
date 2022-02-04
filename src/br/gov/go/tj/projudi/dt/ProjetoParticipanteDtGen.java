package br.gov.go.tj.projudi.dt;

public class ProjetoParticipanteDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 2071663642151472173L;
    private String Id_ProjetoParticipante;
	private String Projeto;

	private String Id_Projeto;

	private String Id_ServentiaCargo;
	private String ServentiaCargo;
	private String CodigoTemp;
	private String Nome;

//---------------------------------------------------------
	public ProjetoParticipanteDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProjetoParticipante;}
	public void setId(String valor ) {if(valor!=null) Id_ProjetoParticipante = valor;}
	public String getProjeto()  {return Projeto;}
	public void setProjeto(String valor ) {if (valor!=null) Projeto = valor;}
	public String getId_Projeto()  {return Id_Projeto;}
	public void setId_Projeto(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Projeto = ""; Projeto = "";}else if (!valor.equalsIgnoreCase("")) Id_Projeto = valor;}
	public String getId_ServentiaCargo()  {return Id_ServentiaCargo;}
	public void setId_ServentiaCargo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaCargo = ""; ServentiaCargo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo = valor;}
	public String getServentiaCargo()  {return ServentiaCargo;}
	public void setServentiaCargo(String valor ) {if (valor!=null) ServentiaCargo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_ServentiaCargo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getServentiaCargo() + "</strong>  </li>  ";
		return stTemp;
	}

	public void copiar(ProjetoParticipanteDt objeto){
		 if (objeto==null) return;
		Id_ProjetoParticipante = objeto.getId();
		Id_Projeto = objeto.getId_Projeto();
		Projeto = objeto.getProjeto();
		Id_ServentiaCargo = objeto.getId_ServentiaCargo();
		ServentiaCargo = objeto.getServentiaCargo();
		CodigoTemp = objeto.getCodigoTemp();
		Nome = objeto.getNome();
	}

	public void limpar(){
		Id_ProjetoParticipante="";
		Id_Projeto="";
		Projeto="";
		Id_ServentiaCargo="";
		ServentiaCargo="";
		CodigoTemp="";
		Nome="";
	}


	public String getPropriedades(){
		return "[Id_ProjetoParticipante:" + Id_ProjetoParticipante + ";Id_Projeto:" + Id_Projeto + ";Projeto:" + Projeto + ";Id_ServentiaCargo:" + Id_ServentiaCargo + ";ServentiaCargo:" + ServentiaCargo + ";CodigoTemp:" + CodigoTemp + ";Nome:" + Nome + "]";
	}


} 
