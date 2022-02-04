package br.gov.go.tj.projudi.dt;

public class CertidaoTipoProcessoTipoDt extends CertidaoTipoProcessoTipoDtGen{

    private static final long serialVersionUID = -3179302895830970265L;
    public static final int CodigoPermissao=459;
    
    private String idProcessoTipoCNJClasse;
    
    public String getIdProcessoTipoCNJClasse()  {return idProcessoTipoCNJClasse;}
    public void setIdProcessoTipoCNJClasse(String valor ) {if (valor!=null) idProcessoTipoCNJClasse = valor;}
    
    
    public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_ProcessoTipo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getIdProcessoTipoCNJClasse() + " - " + getProcessoTipo() + "</strong>  </li>  ";
		return stTemp;
	}
}
