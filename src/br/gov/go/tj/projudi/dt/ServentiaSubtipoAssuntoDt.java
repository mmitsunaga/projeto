package br.gov.go.tj.projudi.dt;

public class ServentiaSubtipoAssuntoDt extends ServentiaSubtipoAssuntoDtGen {

	private static final long serialVersionUID = -5985629237448472730L;
	public static final int CodigoPermissao = 441;
	
	public String getListaLiCheckBox() {
		String stTemp = "<li><input class='formEdicaoCheckBox' id='chkEditar" + getId_Assunto() + "' name='chkEditar" + getId_Assunto() + "' type='checkbox' value='" + getId_Assunto() + "' onchange='alterarServentiaSubtipoAssunto(\"" + getId() + "\",\"" + getId_Assunto() + "\",\"" + getId_ServentiaSubtipo() + "\")'";
		if (getId().length() > 0) stTemp += " checked ";
		stTemp += "><strong>" + getAssuntoCodigo() + " - " + getAssunto() + " - " + "<label style='color:red;'>Pai:</label> "+ getAssuntoPai() + " - " + "<label style='color:red;'>Dispositivo Legal:</label> "+ getDispositivo_Legal() +"</strong></li>";
		return stTemp;
	}
	
}
