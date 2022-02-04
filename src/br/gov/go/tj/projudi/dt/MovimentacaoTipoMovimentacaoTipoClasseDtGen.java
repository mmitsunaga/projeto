package br.gov.go.tj.projudi.dt;

public class MovimentacaoTipoMovimentacaoTipoClasseDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5177612803352878861L;
	private String Id_MoviTipoMoviTipoClasse;
	private String MovimentacaoTipoMovimentacaoTipoClasse;


	private String Id_MovimentacaoTipo;
	private String MovimentacaoTipo;
	private String Id_MovimentacaoTipoClasse;
	private String MoviTipoClasse;
	private String CodigoTemp;

//---------------------------------------------------------
	public MovimentacaoTipoMovimentacaoTipoClasseDtGen() {

		limpar();

	}

	public String getId()  {return Id_MoviTipoMoviTipoClasse;}
	public void setId(String valor ) {if(valor!=null) Id_MoviTipoMoviTipoClasse = valor;}
	public String getMovimentacaoTipoMovimentacaoTipoClasse()  {return MovimentacaoTipoMovimentacaoTipoClasse;}
	public void setMovimentacaoTipoMovimentacaoTipoClasse(String valor ) {if (valor!=null) MovimentacaoTipoMovimentacaoTipoClasse = valor;}
	public String getId_MovimentacaoTipo()  {return Id_MovimentacaoTipo;}
	public void setId_MovimentacaoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_MovimentacaoTipo = ""; MovimentacaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_MovimentacaoTipo = valor;}
	public String getMovimentacaoTipo()  {return MovimentacaoTipo;}
	public void setMovimentacaoTipo(String valor ) {if (valor!=null) MovimentacaoTipo = valor;}
	public String getId_MovimentacaoTipoClasse()  {return Id_MovimentacaoTipoClasse;}
	public void setId_MovimentacaoTipoClasse(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_MovimentacaoTipoClasse = ""; MoviTipoClasse = "";}else if (!valor.equalsIgnoreCase("")) Id_MovimentacaoTipoClasse = valor;}
	public String getMoviTipoClasse()  {return MoviTipoClasse;}
	public void setMoviTipoClasse(String valor ) {if (valor!=null) MoviTipoClasse = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_MovimentacaoTipo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getMovimentacaoTipo() + "</strong>  </li>  ";
		return stTemp;
	}

	public void copiar(MovimentacaoTipoMovimentacaoTipoClasseDt objeto){
		 if (objeto==null) return;
		Id_MoviTipoMoviTipoClasse = objeto.getId();
		MovimentacaoTipoMovimentacaoTipoClasse = objeto.getMovimentacaoTipoMovimentacaoTipoClasse();
		Id_MovimentacaoTipo = objeto.getId_MovimentacaoTipo();
		MovimentacaoTipo = objeto.getMovimentacaoTipo();
		Id_MovimentacaoTipoClasse = objeto.getId_MovimentacaoTipoClasse();
		MoviTipoClasse = objeto.getMoviTipoClasse();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MoviTipoMoviTipoClasse="";
		MovimentacaoTipoMovimentacaoTipoClasse="";
		Id_MovimentacaoTipo="";
		MovimentacaoTipo="";
		Id_MovimentacaoTipoClasse="";
		MoviTipoClasse="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MoviTipoMoviTipoClasse:" + Id_MoviTipoMoviTipoClasse + ";MovimentacaoTipoMovimentacaoTipoClasse:" + MovimentacaoTipoMovimentacaoTipoClasse + ";Id_MovimentacaoTipo:" + Id_MovimentacaoTipo + ";MovimentacaoTipo:" + MovimentacaoTipo + ";Id_MovimentacaoTipoClasse:" + Id_MovimentacaoTipoClasse + ";MoviTipoClasse:" + MoviTipoClasse + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
