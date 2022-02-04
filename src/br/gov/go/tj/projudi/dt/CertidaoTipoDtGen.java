package br.gov.go.tj.projudi.dt;

public class CertidaoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 6855394011442965954L;
    private String Id_CertidaoTipo;
	private String CertidaoTipo;

	private String CertidaoTipoCodigo;

	private String CodigoTemp;

//---------------------------------------------------------
	public CertidaoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_CertidaoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_CertidaoTipo = valor;}
	public String getCertidaoTipo()  {return CertidaoTipo;}
	public void setCertidaoTipo(String valor ) {if (valor!=null) CertidaoTipo = valor;}
	public String getCertidaoTipoCodigo()  {return CertidaoTipoCodigo;}
	public void setCertidaoTipoCodigo(String valor ) {if (valor!=null) CertidaoTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(CertidaoTipoDt objeto){
		Id_CertidaoTipo = objeto.getId();
		CertidaoTipoCodigo = objeto.getCertidaoTipoCodigo();
		CertidaoTipo = objeto.getCertidaoTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_CertidaoTipo="";
		CertidaoTipoCodigo="";
		CertidaoTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_CertidaoTipo:" + Id_CertidaoTipo + ";CertidaoTipoCodigo:" + CertidaoTipoCodigo + ";CertidaoTipo:" + CertidaoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
