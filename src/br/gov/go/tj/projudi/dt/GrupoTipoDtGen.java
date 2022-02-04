package br.gov.go.tj.projudi.dt;

public class GrupoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -8448808618471889340L;
    private String Id_GrupoTipo;
	private String GrupoTipo;


	private String GrupoTipoCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public GrupoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_GrupoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_GrupoTipo = valor;}
	public String getGrupoTipo()  {return GrupoTipo;}
	public void setGrupoTipo(String valor ) {if (valor!=null) GrupoTipo = valor;}
	public String getGrupoTipoCodigo()  {return GrupoTipoCodigo;}
	public void setGrupoTipoCodigo(String valor ) {if (valor!=null) GrupoTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(GrupoTipoDt objeto){
		 if (objeto==null) return;
		Id_GrupoTipo = objeto.getId();
		GrupoTipo = objeto.getGrupoTipo();
		GrupoTipoCodigo = objeto.getGrupoTipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_GrupoTipo="";
		GrupoTipo="";
		GrupoTipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_GrupoTipo:" + Id_GrupoTipo + ";GrupoTipo:" + GrupoTipo + ";GrupoTipoCodigo:" + GrupoTipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
