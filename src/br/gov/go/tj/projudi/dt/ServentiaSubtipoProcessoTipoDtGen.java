package br.gov.go.tj.projudi.dt;

public class ServentiaSubtipoProcessoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -6311785851090262694L;
    private String Id_ServentiaSubtipoProcessoTipo;
	private String ServentiaSubtipo;

	private String Id_ServentiaSubtipo;

	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String CodigoTemp;
	private String ServentiaSubtipoCodigo;
	private String ProcessoTipoCodigo;
	
	private String idCnjClasse;

//---------------------------------------------------------
	public ServentiaSubtipoProcessoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServentiaSubtipoProcessoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_ServentiaSubtipoProcessoTipo = valor;}
	public String getServentiaSubtipo()  {return ServentiaSubtipo;}
	public void setServentiaSubtipo(String valor ) {if (valor!=null) ServentiaSubtipo = valor;}
	public String getId_ServentiaSubtipo()  {return Id_ServentiaSubtipo;}
	public void setId_ServentiaSubtipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ServentiaSubtipo = ""; ServentiaSubtipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ServentiaSubtipo = valor;}
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getServentiaSubtipoCodigo()  {return ServentiaSubtipoCodigo;}
	public void setServentiaSubtipoCodigo(String valor ) {if (valor!=null) ServentiaSubtipoCodigo = valor;}
	public String getProcessoTipoCodigo()  {return ProcessoTipoCodigo;}
	public void setProcessoTipoCodigo(String valor ) {if (valor!=null) ProcessoTipoCodigo = valor;}
	
	public String getIdCnjClasse() {
		return idCnjClasse;
	}

	public void setIdCnjClasse(String idCnjClasse) {
		this.idCnjClasse = idCnjClasse;
	}

	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_ProcessoTipo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		if (getIdCnjClasse() != null && !getIdCnjClasse().equals("")) {
			stTemp += "><strong> " + getIdCnjClasse() + " - "+ getProcessoTipo() + "</strong>  </li>";
		} else {
			stTemp += "><strong> " + getProcessoTipo() + "</strong>  </li>";
		}
		
		return stTemp;
	}

	public void copiar(ServentiaSubtipoProcessoTipoDt objeto){
		Id_ServentiaSubtipoProcessoTipo = objeto.getId();
		Id_ServentiaSubtipo = objeto.getId_ServentiaSubtipo();
		ServentiaSubtipo = objeto.getServentiaSubtipo();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		ProcessoTipo = objeto.getProcessoTipo();
		CodigoTemp = objeto.getCodigoTemp();
		ServentiaSubtipoCodigo = objeto.getServentiaSubtipoCodigo();
		ProcessoTipoCodigo = objeto.getProcessoTipoCodigo();
		idCnjClasse = objeto.getIdCnjClasse();
	}

	public void limpar(){
		Id_ServentiaSubtipoProcessoTipo="";
		Id_ServentiaSubtipo="";
		ServentiaSubtipo="";
		Id_ProcessoTipo="";
		ProcessoTipo="";
		CodigoTemp="";
		ServentiaSubtipoCodigo="";
		ProcessoTipoCodigo="";
		idCnjClasse = "";
	}


	public String getPropriedades(){
		return "[Id_ServentiaSubtipoProcessoTipo:" + Id_ServentiaSubtipoProcessoTipo + ";Id_ServentiaSubtipo:" + Id_ServentiaSubtipo + ";ServentiaSubtipo:" + ServentiaSubtipo + ";Id_ProcessoTipo:" + Id_ProcessoTipo + ";ProcessoTipo:" + ProcessoTipo + ";CodigoTemp:" + CodigoTemp + ";ServentiaSubtipoCodigo:" + ServentiaSubtipoCodigo + ";ProcessoTipoCodigo:" + ProcessoTipoCodigo + ";IdCnjClasse:" + idCnjClasse +"]";
	}


} 
