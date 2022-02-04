package br.gov.go.tj.projudi.dt;

public class ProjetoParticipanteDt extends ProjetoParticipanteDtGen{

    private static final long serialVersionUID = 39571483984753625L;
    public static final int CodigoPermissao=624;
    
    public String getListaLiCheckBox(String id_projeto) {
		String stTemp = "<li><input class='formEdicaoCheckBox' name='chkEditar' type='checkbox' value='" + getId_ServentiaCargo() + "'";
		if (getId_Projeto().equals(id_projeto))
			stTemp += " checked ";
		stTemp += "><strong> " + getServentiaCargo() + "</strong>  </li>  ";
		return stTemp;
	}

}
