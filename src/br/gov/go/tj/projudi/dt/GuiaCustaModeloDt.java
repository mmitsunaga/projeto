package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class GuiaCustaModeloDt extends GuiaCustaModeloDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = -5889248515785332140L;
    public static final int CodigoPermissao=540;
//
    
    private GuiaModeloDt guiaModeloDt;
    private CustaDt custaDt;
    private String custa;
    
    public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_Custa() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getCodigoRegimento() + " - " + getCusta() + "</strong>  </li>  ";
		return stTemp;
	}
    
    public GuiaModeloDt getGuiaModeloDt() {
    	return guiaModeloDt;
    }
    
    public void setGuiaModeloDt(GuiaModeloDt guiaModeloDt) {
    	this.guiaModeloDt = guiaModeloDt;
    }
    
    public CustaDt getCustaDt() {
    	return custaDt;
    }
    
    public void setCustaDt(CustaDt custaDt) {
    	this.custaDt = custaDt;
    }
    
    public String getCusta() {
    	return custa;
    }
    
    public void setCusta(String custa) {
    	this.custa = custa;
    }

}
