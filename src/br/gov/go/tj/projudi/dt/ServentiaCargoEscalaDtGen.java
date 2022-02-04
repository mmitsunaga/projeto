package br.gov.go.tj.projudi.dt;

public class ServentiaCargoEscalaDtGen extends Dados{

    private static final long serialVersionUID = 1467548751794794081L;
    private String Id_ServentiaCargoEscala;
	private String ServentiaCargoEscala;
	private String Id_ServentiaCargo;
	private String ServentiaCargo;
	private String Id_Escala;
	private String Escala;
	private String Ativo;
	
	public ServentiaCargoEscalaDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaCargoEscala;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaCargoEscala = valor;}
	public String getServentiaCargoEscala()  {return ServentiaCargoEscala;}
	public void setServentiaCargoEscala(String valor ) {if (valor!=null) ServentiaCargoEscala = valor;}
	public String getId_ServentiaCargo()  {return Id_ServentiaCargo;}
	public void setId_ServentiaCargo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaCargo = ""; ServentiaCargo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaCargo = valor;}
	public String getServentiaCargo()  {return ServentiaCargo;}
	public void setServentiaCargo(String valor ) {if (valor!=null) ServentiaCargo = valor;}
	public String getId_Escala()  {return Id_Escala;}
	public void setId_Escala(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Escala = ""; Escala = "";}else if (!valor.equalsIgnoreCase("")) Id_Escala = valor;}
	public String getEscala()  {return Escala;}
	public void setEscala(String valor ) {if (valor!=null) Escala = valor;}
	public String getAtivo()  {return Ativo;}
	public void setAtivo(String valor ) {if (valor!=null) Ativo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_Escala() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getEscala() + "</strong>  </li>  ";
		return stTemp;
	}

	public void copiar(ServentiaCargoEscalaDt objeto){
		Id_ServentiaCargoEscala = objeto.getId();
		ServentiaCargoEscala = objeto.getServentiaCargoEscala();
		Id_ServentiaCargo = objeto.getId_ServentiaCargo();
		ServentiaCargo = objeto.getServentiaCargo();
		Id_Escala = objeto.getId_Escala();
		Escala = objeto.getEscala();
		Ativo = objeto.getAtivo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ServentiaCargoEscala="";
		ServentiaCargoEscala="";
		Id_ServentiaCargo="";
		ServentiaCargo="";
		Id_Escala="";
		Escala="";
		Ativo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ServentiaCargoEscala:" + Id_ServentiaCargoEscala + ";ServentiaCargoEscala:" + ServentiaCargoEscala + ";Id_ServentiaCargo:" + Id_ServentiaCargo + ";UsuarioServentia:" + ServentiaCargo + ";Id_Escala:" + Id_Escala + ";Escala:" + Escala + ";Ativo:" + Ativo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
