package br.gov.go.tj.projudi.dt;

public class PrazoSuspensoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -8944823374979767737L;
    private String Id_PrazoSuspenso;
	private String Motivo;
	
	


	private String Id_PrazoSuspensoTipo;
	private String PrazoSuspensoTipo;
	private String Id_Comarca;
	private String Comarca;
	private String Id_Serventia;
	private String Serventia;
	private String Id_Cidade;
	private String Cidade;
	private String Data;
	
	private String PrazoSuspensoTipoCodigo;
	private String ComarcaCodigo;
	private String ServentiaCodigo;
	private String CidadeCodigo;

//---------------------------------------------------------
	public PrazoSuspensoDtGen() {

		limpar();

	}

	public String getId()  {return Id_PrazoSuspenso;}
	public void setId(String valor ) {if(valor!=null) Id_PrazoSuspenso = valor;}
	public String getMotivo()  {return Motivo;}
	public void setMotivo(String valor ) {if (valor!=null) Motivo = valor;}
	public String getId_PrazoSuspensoTipo()  {return Id_PrazoSuspensoTipo;}
	public void setId_PrazoSuspensoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PrazoSuspensoTipo = ""; PrazoSuspensoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_PrazoSuspensoTipo = valor;}
	public String getPrazoSuspensoTipo()  {return PrazoSuspensoTipo;}
	public void setPrazoSuspensoTipo(String valor ) {if (valor!=null) PrazoSuspensoTipo = valor;}
	public String getId_Comarca()  {return Id_Comarca;}
	public void setId_Comarca(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Comarca = ""; Comarca = "";}else if (!valor.equalsIgnoreCase("")) Id_Comarca = valor;}
	public String getComarca()  {return Comarca;}
	public void setComarca(String valor ) {if (valor!=null) Comarca = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_Cidade()  {return Id_Cidade;}
	public void setId_Cidade(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Cidade = ""; Cidade = "";}else if (!valor.equalsIgnoreCase("")) Id_Cidade = valor;}
	public String getCidade()  {return Cidade;}
	public void setCidade(String valor ) {if (valor!=null) Cidade = valor;}
	public String getData()  {return Data;}
	public void setData(String valor ) {if (valor!=null) Data = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getPrazoSuspensoTipoCodigo()  {return PrazoSuspensoTipoCodigo;}
	public void setPrazoSuspensoTipoCodigo(String valor ) {if (valor!=null) PrazoSuspensoTipoCodigo = valor;}
	public String getComarcaCodigo()  {return ComarcaCodigo;}
	public void setComarcaCodigo(String valor ) {if (valor!=null) ComarcaCodigo = valor;}
	public String getServentiaCodigo()  {return ServentiaCodigo;}
	public void setServentiaCodigo(String valor ) {if (valor!=null) ServentiaCodigo = valor;}
	public String getCidadeCodigo()  {return CidadeCodigo;}
	public void setCidadeCodigo(String valor ) {if (valor!=null) CidadeCodigo = valor;}
	
	
	
	


	public void copiar(PrazoSuspensoDt objeto){
		Id_PrazoSuspenso = objeto.getId();
		Motivo = objeto.getMotivo();
		Id_PrazoSuspensoTipo = objeto.getId_PrazoSuspensoTipo();
		PrazoSuspensoTipo = objeto.getPrazoSuspensoTipo();
		Id_Comarca = objeto.getId_Comarca();
		Comarca = objeto.getComarca();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
		Id_Cidade = objeto.getId_Cidade();
		Cidade = objeto.getCidade();
		Data = objeto.getData();
		CodigoTemp = objeto.getCodigoTemp();
		PrazoSuspensoTipoCodigo = objeto.getPrazoSuspensoTipoCodigo();
		ComarcaCodigo = objeto.getComarcaCodigo();
		ServentiaCodigo = objeto.getServentiaCodigo();
		CidadeCodigo = objeto.getCidadeCodigo();
	}

	public void limpar(){
		Id_PrazoSuspenso="";
		Motivo="";
		Id_PrazoSuspensoTipo="";
		PrazoSuspensoTipo="";
		Id_Comarca="";
		Comarca="";
		Id_Serventia="";
		Serventia="";
		Id_Cidade="";
		Cidade="";
		Data="";
		CodigoTemp="";
		PrazoSuspensoTipoCodigo="";
		ComarcaCodigo="";
		ServentiaCodigo="";
		CidadeCodigo="";
	}


	public String getPropriedades(){
		return "[Id_PrazoSuspenso:" + Id_PrazoSuspenso + ";Motivo:" + Motivo + ";Id_PrazoSuspensoTipo:" + Id_PrazoSuspensoTipo + ";PrazoSuspensoTipo:" + PrazoSuspensoTipo + ";Id_Comarca:" + Id_Comarca + ";Comarca:" + Comarca + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + ";Id_Cidade:" + Id_Cidade + ";Cidade:" + Cidade + ";Data:" + Data + ";CodigoTemp:" + CodigoTemp + ";PrazoSuspensoTipoCodigo:" + PrazoSuspensoTipoCodigo + ";ComarcaCodigo:" + ComarcaCodigo + ";ServentiaCodigo:" + ServentiaCodigo + ";CidadeCodigo:" + CidadeCodigo;
	}


} 
