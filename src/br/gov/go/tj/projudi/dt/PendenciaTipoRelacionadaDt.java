package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class PendenciaTipoRelacionadaDt extends PendenciaTipoRelacionadaDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = 5435034814771809001L;
    public static final int CodigoPermissao=499;
//
    
    public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_PendenciaTipoRelacao() + "'";
		if (getId().length() > 0)
			stTemp += " checked ";
		stTemp += "><strong> " + getPendenciaTipoRelacao() + "</strong>  </li>  ";
		return stTemp;
	}

}
