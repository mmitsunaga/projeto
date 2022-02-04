package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class VincularGuiaComplementarProcessoDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8329160820955574485L;
	
	private String NumeroGuiaCompleto;
	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	
	public VincularGuiaComplementarProcessoDt(){
		this.NumeroGuiaCompleto = "";
		this.Id_ProcessoTipo = "";
		this.ProcessoTipo = "";
	}
	
	public String getNumeroGuiaCompleto()  {return NumeroGuiaCompleto;}
	
	public void setNumeroGuiaCompleto(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) { NumeroGuiaCompleto = ""; }else if (!valor.equalsIgnoreCase("")) NumeroGuiaCompleto = valor;}
	
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	
	public String getProcessoTipo()  {return ProcessoTipo;}
	
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
}
