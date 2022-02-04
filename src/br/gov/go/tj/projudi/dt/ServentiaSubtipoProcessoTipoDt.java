package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class ServentiaSubtipoProcessoTipoDt extends ServentiaSubtipoProcessoTipoDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = 6356745360262864107L;
    public static final int CodigoPermissao=194;
//
    
	public String getListaLiCheckBox() {
				
		String stTemp = "<li><input class='formEdicaoCheckBox' id='chkEditar" + getId_ProcessoTipo() + "' name='chkEditar" + getId_ProcessoTipo() + "' type='checkbox' value='" + getId_ProcessoTipo() + "' onchange='alterarServentiaSubtipoProcessoTipo(\"" + getId() + "\",\"" + getId_ProcessoTipo() + "\",\"" + getId_ServentiaSubtipo() + "\")'";
		if (getId().length() > 0)
			stTemp += " checked ";
		if (getIdCnjClasse() != null && !getIdCnjClasse().equals("")) {
			stTemp += "><strong> " + getIdCnjClasse() + " - "+ getProcessoTipo() + "</strong>  </li>";
		} else {
			stTemp += "><strong> " + getProcessoTipo() + "</strong>  </li>";
		}
		
		return stTemp;
	}

}
