package br.gov.go.tj.projudi.dt;

public class ModeloDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -838471912622805552L;
	private String Id_Modelo;
	private String Modelo;
	private String ModeloCodigo;
	private String Texto;
	private String ArquivoTipo;
	private String Id_Serventia;
	private String Id_UsuarioServentia;
	private String CodigoTemp;
	private String Serventia;
	private String Id_ArquivoTipo;
	private String UsuarioServentia;
	private String ServentiaCodigo;

//---------------------------------------------------------
	public ModeloDtGen() {

		limpar();

	}

	public String getId()  {return Id_Modelo;}
	public void setId(String valor ) {if(valor!=null) Id_Modelo = valor;}
	public String getModelo()  {return Modelo;}
	public void setModelo(String valor ) {if (valor!=null) Modelo = valor;}
	public String getModeloCodigo()  {return ModeloCodigo;}
	public void setModeloCodigo(String valor ) {if (valor!=null) ModeloCodigo = valor;}
	public String getTexto()  {return Texto;}
	public void setTexto(String valor ) {if (valor!=null) Texto = valor;}
	public String getArquivoTipo()  {return ArquivoTipo;}
	public void setArquivoTipo(String valor ) {if (valor!=null) ArquivoTipo = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Id_UsuarioServentia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia = ""; CodigoTemp = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_ArquivoTipo()  {return Id_ArquivoTipo;}
	public void setId_ArquivoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ArquivoTipo = ""; UsuarioServentia = "";}else if (!valor.equalsIgnoreCase("")) Id_ArquivoTipo = valor;}
	public String getUsuarioServentia()  {return UsuarioServentia;}
	public void setUsuarioServentia(String valor ) {if (valor!=null) UsuarioServentia = valor;}
	public String getServentiaCodigo()  {return ServentiaCodigo;}
	public void setServentiaCodigo(String valor ) {if (valor!=null) ServentiaCodigo = valor;}


	public void copiar(ModeloDt objeto){
		 if (objeto==null) return;
		Id_Modelo = objeto.getId();
		ModeloCodigo = objeto.getModeloCodigo();
		Modelo = objeto.getModelo();
		Texto = objeto.getTexto();
		ArquivoTipo = objeto.getArquivoTipo();
		Id_Serventia = objeto.getId_Serventia();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		CodigoTemp = objeto.getCodigoTemp();
		Serventia = objeto.getServentia();
		Id_ArquivoTipo = objeto.getId_ArquivoTipo();
		UsuarioServentia = objeto.getUsuarioServentia();
		ServentiaCodigo = objeto.getServentiaCodigo();
	}

	public void limpar(){
		Id_Modelo="";
		ModeloCodigo="";
		Modelo="";
		Texto="";
		ArquivoTipo="";
		Id_Serventia="";
		Id_UsuarioServentia="";
		CodigoTemp="";
		Serventia="";
		Id_ArquivoTipo="";
		UsuarioServentia="";
		ServentiaCodigo="";
	}


	public String getPropriedades(){
		return "[Id_Modelo:" + Id_Modelo + ";ModeloCodigo:" + ModeloCodigo + ";Modelo:" + Modelo + ";Texto:" + Texto + ";ArquivoTipo:" + ArquivoTipo + ";Id_Serventia:" + Id_Serventia + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";CodigoTemp:" + CodigoTemp + ";Serventia:" + Serventia + ";Id_ArquivoTipo:" + Id_ArquivoTipo + ";UsuarioServentia:" + UsuarioServentia + ";ServentiaCodigo:" + ServentiaCodigo + "]";
	}


} 
