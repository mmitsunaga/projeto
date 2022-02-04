package br.gov.go.tj.projudi.dt;

public class GrupoMovimentacaoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -3258858249428383738L;
    private String Id_GrupoMovimentacaoTipo;
	private String Grupo;
	
	

	private String Id_Grupo;

	private String Id_MovimentacaoTipo;
	private String MovimentacaoTipo;
	
	private String GrupoCodigo;
	private String MovimentacaoTipoCodigo;

//---------------------------------------------------------
	public GrupoMovimentacaoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_GrupoMovimentacaoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_GrupoMovimentacaoTipo = valor;}
	public String getGrupo()  {return Grupo;}
	public void setGrupo(String valor ) {if (valor!=null) Grupo = valor;}
	public String getId_Grupo()  {return Id_Grupo;}
	public void setId_Grupo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Grupo = ""; Grupo = "";}else if (!valor.equalsIgnoreCase("")) Id_Grupo = valor;}
	public String getId_MovimentacaoTipo()  {return Id_MovimentacaoTipo;}
	public void setId_MovimentacaoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_MovimentacaoTipo = ""; MovimentacaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_MovimentacaoTipo = valor;}
	public String getMovimentacaoTipo()  {return MovimentacaoTipo;}
	public void setMovimentacaoTipo(String valor ) {if (valor!=null) MovimentacaoTipo = valor;}
	
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getGrupoCodigo()  {return GrupoCodigo;}
	public void setGrupoCodigo(String valor ) {if (valor!=null) GrupoCodigo = valor;}
	public String getMovimentacaoTipoCodigo()  {return MovimentacaoTipoCodigo;}
	public void setMovimentacaoTipoCodigo(String valor ) {if (valor!=null) MovimentacaoTipoCodigo = valor;}
	
	
	
	

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_MovimentacaoTipo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getMovimentacaoTipo() + "</strong><label style='color:red;'> -> ID CNJ:</label> <strong>"+ getCodigoTemp() + "</strong></label></li>  ";
		return stTemp;
	}

	public void copiar(GrupoMovimentacaoTipoDt objeto){
		Id_GrupoMovimentacaoTipo = objeto.getId();
		Id_Grupo = objeto.getId_Grupo();
		Grupo = objeto.getGrupo();
		Id_MovimentacaoTipo = objeto.getId_MovimentacaoTipo();
		MovimentacaoTipo = objeto.getMovimentacaoTipo();
		CodigoTemp = objeto.getCodigoTemp();
		GrupoCodigo = objeto.getGrupoCodigo();
		MovimentacaoTipoCodigo = objeto.getMovimentacaoTipoCodigo();
	}

	public void limpar(){
		Id_GrupoMovimentacaoTipo="";
		Id_Grupo="";
		Grupo="";
		Id_MovimentacaoTipo="";
		MovimentacaoTipo="";
		CodigoTemp="";
		GrupoCodigo="";
		MovimentacaoTipoCodigo="";
	}


	public String getPropriedades(){
		return "[Id_GrupoMovimentacaoTipo:" + Id_GrupoMovimentacaoTipo + ";Id_Grupo:" + Id_Grupo + ";Grupo:" + Grupo + ";Id_MovimentacaoTipo:" + Id_MovimentacaoTipo + ";MovimentacaoTipo:" + MovimentacaoTipo + ";CodigoTemp:" + CodigoTemp + ";GrupoCodigo:" + GrupoCodigo + ";MovimentacaoTipoCodigo:" + MovimentacaoTipoCodigo + "]";
	}


} 
