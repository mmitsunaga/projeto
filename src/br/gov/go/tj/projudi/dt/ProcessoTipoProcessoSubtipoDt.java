package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class ProcessoTipoProcessoSubtipoDt extends ProcessoTipoProcessoSubtipoDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = -6619713813007454469L;
    public static final int CodigoPermissao=613;
    
    public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_ProcessoTipo() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getProcessoTipo() + "</strong>  </li>  ";
		return stTemp;
	}
//

}
