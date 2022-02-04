package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

public class RecursoExcluirDt extends Dados implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8404697630936642799L;
	
	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String ProcessoTipoCodigo;
	
	public String getId_ProcessoTipo()  {return Id_ProcessoTipo;}
	public void setId_ProcessoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = ""; ProcessoTipoCodigo = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;}
	public String getProcessoTipo()  {return ProcessoTipo;}
	public void setProcessoTipo(String valor ) {if (valor!=null) ProcessoTipo = valor;}
	public String getProcessoTipoCodigo() {
		return ProcessoTipoCodigo;
	}
	public void setProcessoTipoCodigo(String valor) {
		if (valor!=null) ProcessoTipoCodigo = valor;				
	}
	
	public void Limpar() {
		this.Id_ProcessoTipo = "";
		this.ProcessoTipo = "";
		this.ProcessoTipoCodigo = "";
	}
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
