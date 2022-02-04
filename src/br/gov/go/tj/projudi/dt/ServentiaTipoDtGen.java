package br.gov.go.tj.projudi.dt;

public class ServentiaTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -7363147555176012590L;
    private String Id_ServentiaTipo;
	private String ServentiaTipo;


	private String ServentiaTipoCodigo;
	private String Externa;
	private String CodigoTemp;

//---------------------------------------------------------
	public ServentiaTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaTipo;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaTipo = valor;}
	public String getServentiaTipo()  {return ServentiaTipo;}
	public void setServentiaTipo(String valor ) {if (valor!=null) ServentiaTipo = valor;}
	public String getServentiaTipoCodigo()  {return ServentiaTipoCodigo;}
	public void setServentiaTipoCodigo(String valor ) {if (valor!=null) ServentiaTipoCodigo = valor;}
	public String getExterna()  {return Externa;}
	public void setExterna(String valor ) {if (valor!=null) Externa = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ServentiaTipoDt objeto){
		Id_ServentiaTipo = objeto.getId();
		ServentiaTipo = objeto.getServentiaTipo();
		ServentiaTipoCodigo = objeto.getServentiaTipoCodigo();
		Externa = objeto.getExterna();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ServentiaTipo="";
		ServentiaTipo="";
		ServentiaTipoCodigo="";
		Externa="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ServentiaTipo:" + Id_ServentiaTipo + ";ServentiaTipo:" + ServentiaTipo + ";ServentiaTipoCodigo:" + ServentiaTipoCodigo + ";Externa:" + Externa + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
