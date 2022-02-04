package br.gov.go.tj.projudi.dt;

public class UsuarioServentiaGrupoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -5342306078342587377L;
    private String Id_UsuarioServentiaGrupo;
	private String UsuarioServentiaGrupo;


	private String Id_UsuarioServentia;
	private String UsuarioServentia;
	private String Id_Grupo;
	private String Grupo;
	private String Ativo;
	private String CodigoTemp;
	private String GrupoCodigo;
	private String Nome;
	private String Id_Serventia;
	private String Serventia;

//---------------------------------------------------------
	public UsuarioServentiaGrupoDtGen() {

		limpar();

	}

	public String getId()  {return Id_UsuarioServentiaGrupo;}
	public void setId(String valor ) {if(valor!=null) Id_UsuarioServentiaGrupo = valor;}
	public String getUsuarioServentiaGrupo()  {return UsuarioServentiaGrupo;}
	public void setUsuarioServentiaGrupo(String valor ) {if (valor!=null) UsuarioServentiaGrupo = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia = ""; UsuarioServentia = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getUsuarioServentia()  {return UsuarioServentia;}
	public void setUsuarioServentia(String valor ) {if (valor!=null) UsuarioServentia = valor;}
	public String getId_Grupo()  {return Id_Grupo;}
	public void setId_Grupo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Grupo = ""; Grupo = "";}else if (!valor.equalsIgnoreCase("")) Id_Grupo = valor;}
	public String getGrupo()  {return Grupo;}
	public void setGrupo(String valor ) {if (valor!=null) Grupo = valor;}
	public String getAtivo()  {return Ativo;}
	public void setAtivo(String valor ) {if (valor!=null) Ativo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getGrupoCodigo()  {return GrupoCodigo;}
	public void setGrupoCodigo(String valor ) {if (valor!=null) GrupoCodigo = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getId_Serventia()  {return Id_Serventia;}
	public void setId_Serventia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServentia()  {return Serventia;}
	public void setServentia(String valor ) {if (valor!=null) Serventia = valor;}


	public void copiar(UsuarioServentiaGrupoDt objeto){
		Id_UsuarioServentiaGrupo = objeto.getId();
		UsuarioServentiaGrupo = objeto.getUsuarioServentiaGrupo();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		UsuarioServentia = objeto.getUsuarioServentia();
		Id_Grupo = objeto.getId_Grupo();
		Grupo = objeto.getGrupo();
		Ativo = objeto.getAtivo();
		CodigoTemp = objeto.getCodigoTemp();
		GrupoCodigo = objeto.getGrupoCodigo();
		Nome = objeto.getNome();
		Id_Serventia = objeto.getId_Serventia();
		Serventia = objeto.getServentia();
	}

	public void limpar(){
		Id_UsuarioServentiaGrupo="";
		UsuarioServentiaGrupo="";
		Id_UsuarioServentia="";
		UsuarioServentia="";
		Id_Grupo="";
		Grupo="";
		Ativo="";
		CodigoTemp="";
		GrupoCodigo="";
		Nome="";
		Id_Serventia="";
		Serventia="";
	}


	public String getPropriedades(){
		return "[Id_UsuarioServentiaGrupo:" + Id_UsuarioServentiaGrupo + ";UsuarioServentiaGrupo:" + UsuarioServentiaGrupo + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";UsuarioServentia:" + UsuarioServentia + ";Id_Grupo:" + Id_Grupo + ";Grupo:" + Grupo + ";Ativo:" + Ativo + ";CodigoTemp:" + CodigoTemp + ";GrupoCodigo:" + GrupoCodigo + ";Nome:" + Nome + ";Id_Serventia:" + Id_Serventia + ";Serventia:" + Serventia + "]";
	}


} 
