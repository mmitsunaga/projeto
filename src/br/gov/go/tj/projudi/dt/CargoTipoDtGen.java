package br.gov.go.tj.projudi.dt;

public class CargoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -8175119714294428852L;
    private String Id_CargoTipo;
	private String CargoTipo;


	private String CargoTipoCodigo;
	private String Id_Grupo;
	private String Grupo;
	private String CodigoTemp;
	private String GrupoCodigo;

//---------------------------------------------------------
	public CargoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_CargoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_CargoTipo = valor;}
	public String getCargoTipo()  {return CargoTipo;}
	public void setCargoTipo(String valor ) {if (valor!=null) CargoTipo = valor;}
	public String getCargoTipoCodigo()  {return CargoTipoCodigo;}
	public void setCargoTipoCodigo(String valor ) {if (valor!=null) CargoTipoCodigo = valor;}
	public String getId_Grupo()  {return Id_Grupo;}
	public void setId_Grupo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Grupo = ""; Grupo = "";}else if (!valor.equalsIgnoreCase("")) Id_Grupo = valor;}
	public String getGrupo()  {return Grupo;}
	public void setGrupo(String valor ) {if (valor!=null) Grupo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getGrupoCodigo()  {return GrupoCodigo;}
	public void setGrupoCodigo(String valor ) {if (valor!=null) GrupoCodigo = valor;}


	public void copiar(CargoTipoDt objeto){
		Id_CargoTipo = objeto.getId();
		CargoTipo = objeto.getCargoTipo();
		CargoTipoCodigo = objeto.getCargoTipoCodigo();
		Id_Grupo = objeto.getId_Grupo();
		Grupo = objeto.getGrupo();
		CodigoTemp = objeto.getCodigoTemp();
		GrupoCodigo = objeto.getGrupoCodigo();
	}

	public void limpar(){
		Id_CargoTipo="";
		CargoTipo="";
		CargoTipoCodigo="";
		Id_Grupo="";
		Grupo="";
		CodigoTemp="";
		GrupoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_CargoTipo:" + Id_CargoTipo + ";CargoTipo:" + CargoTipo + ";CargoTipoCodigo:" + CargoTipoCodigo + ";Id_Grupo:" + Id_Grupo + ";Grupo:" + Grupo + ";CodigoTemp:" + CodigoTemp + ";GrupoCodigo:" + GrupoCodigo + "]";
	}


} 
