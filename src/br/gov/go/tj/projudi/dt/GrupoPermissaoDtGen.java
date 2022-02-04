package br.gov.go.tj.projudi.dt;

public class GrupoPermissaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -6996406177008586721L;
    private String Id_GrupoPermissao;
	private String Grupo;
	
	

	private String Id_Grupo;

	private String GrupoCodigo;
	private String Id_Permissao;
	private String Permissao;
	private String PermissaoCodigo;
	

//---------------------------------------------------------
	public GrupoPermissaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_GrupoPermissao;}
	public void setId(String valor ) {if(valor!=null) Id_GrupoPermissao = valor;}
	public String getGrupo()  {return Grupo;}
	public void setGrupo(String valor ) {if (valor!=null) Grupo = valor;}
	public String getId_Grupo()  {return Id_Grupo;}
	public void setId_Grupo(String valor ) {if (valor!=null) Id_Grupo = valor;}
	public String getGrupoCodigo()  {return GrupoCodigo;}
	public void setGrupoCodigo(String valor ) {if (valor!=null) GrupoCodigo = valor;}
	public String getId_Permissao()  {return Id_Permissao;}
	public void setId_Permissao(String valor ) {if (valor!=null) Id_Permissao = valor;}
	public String getPermissao()  {return Permissao;}
	public void setPermissao(String valor ) {if (valor!=null) Permissao = valor;}
	public String getPermissaoCodigo()  {return PermissaoCodigo;}
	public void setPermissaoCodigo(String valor ) {if (valor!=null) PermissaoCodigo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	
	
	
	

	public void copiar(GrupoPermissaoDt objeto){
		Id_GrupoPermissao = objeto.getId();
		Id_Grupo = objeto.getId_Grupo();
		Grupo = objeto.getGrupo();
		GrupoCodigo = objeto.getGrupoCodigo();
		Id_Permissao = objeto.getId_Permissao();
		Permissao = objeto.getPermissao();
		PermissaoCodigo = objeto.getPermissaoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_GrupoPermissao="";
		Id_Grupo="";
		Grupo="";
		GrupoCodigo="";
		Id_Permissao="";
		Permissao="";
		PermissaoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_GrupoPermissao:" + Id_GrupoPermissao + ";Id_Grupo:" + Id_Grupo + ";Grupo:" + Grupo + ";GrupoCodigo:" + GrupoCodigo + ";Id_Permissao:" + Id_Permissao + ";Permissao:" + Permissao + ";PermissaoCodigo:" + PermissaoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
