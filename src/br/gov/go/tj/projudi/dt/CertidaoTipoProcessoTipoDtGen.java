package br.gov.go.tj.projudi.dt;

public class CertidaoTipoProcessoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -1045126161970839606L;
    private String Id_CertidaoTipoProcessoTipo;
	private String CertidaoTipo;

	private String Id_CertidaoTipo;

	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String CodigoTemp;

//---------------------------------------------------------
	public CertidaoTipoProcessoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_CertidaoTipoProcessoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_CertidaoTipoProcessoTipo = valor;}
	public String getCertidaoTipo()  {return CertidaoTipo;}
	public void setCertidaoTipo(String valor ) {if (valor!=null) CertidaoTipo = valor;}
	public String getId_CertidaoTipo()  {return Id_CertidaoTipo;}
	public void setId_CertidaoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_CertidaoTipo = ""; CertidaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_CertidaoTipo = valor;}
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}

	public void copiar(CertidaoTipoProcessoTipoDt objeto){
		Id_CertidaoTipoProcessoTipo = objeto.getId();
		Id_CertidaoTipo = objeto.getId_CertidaoTipo();
		CertidaoTipo = objeto.getCertidaoTipo();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		ProcessoTipo = objeto.getProcessoTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_CertidaoTipoProcessoTipo="";
		Id_CertidaoTipo="";
		CertidaoTipo="";
		Id_ProcessoTipo="";
		ProcessoTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_CertidaoTipoProcessoTipo:" + Id_CertidaoTipoProcessoTipo + ";Id_CertidaoTipo:" + Id_CertidaoTipo + ";CertidaoTipo:" + CertidaoTipo + ";Id_ProcessoTipo:" + Id_ProcessoTipo + ";ProcessoTipo:" + ProcessoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
