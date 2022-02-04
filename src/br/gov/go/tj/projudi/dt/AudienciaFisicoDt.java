package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;

import br.gov.go.tj.utils.Funcoes;

public class AudienciaFisicoDt extends AudienciaDt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1497821500684350497L;
	
	private String Id_AudienciaProcessoFisico;
	private String ProcessoNumero;
	private String TipoAgendamento;
	
	/**
	 * Encapsulamento para retornar primeiro registro de AudienciaProcessoDt vinculado a Audiencia,
	 * pois na maioria dos casos uma audiencia estará vinculado somente a um processo, exceto no caso de sessões do 2º grau
	 * 
	 * @author mmgomes
	 * @return AudienciaProcessoDt
	 */
	public AudienciaProcessoFisicoDt getAudienciaProcessoDt() {
		if (super.listaAudienciaProcessoDt == null) {
			super.listaAudienciaProcessoDt = new ArrayList();
			super.listaAudienciaProcessoDt.add(new AudienciaProcessoFisicoDt());
		} else if (super.listaAudienciaProcessoDt.size() == 0) super.listaAudienciaProcessoDt.add(new AudienciaProcessoFisicoDt());

		return (AudienciaProcessoFisicoDt) super.listaAudienciaProcessoDt.get(0);
	}

	public String getId_AudienciaProcessoFisico() {
		return Id_AudienciaProcessoFisico;
	}

	public void setId_AudienciaProcessoFisico(String valor) {
		if (valor!=null) {
			if (valor.equalsIgnoreCase("null")) {
				Id_AudienciaProcessoFisico = "";			
			} else if (!valor.equalsIgnoreCase("")) {
				Id_AudienciaProcessoFisico = valor;
			}	
		}		
	}
	
	public String getPropriedades(){
		String propriedades = super.getPropriedades();
		return "[Id_AudienciaProcessoFisico:" + this.getId_AudienciaProcessoFisico() + ";" + propriedades;		
	}

	public String getProcessoNumero() {
		return ProcessoNumero;
	}

	public void setProcessoNumero(String valor) {
		if (valor!=null) {
			if (valor.equalsIgnoreCase("null")) {
				ProcessoNumero = "";			
			} else if (!valor .equalsIgnoreCase("")) {
				ProcessoNumero = valor;
			}	
		}		
	}
	
	public String getTipoAgendamento() {
		return TipoAgendamento;
	}

	public void setTipoAgendamento(String valor) {
		if (valor!=null) {
			if (valor.equalsIgnoreCase("null")) {
				TipoAgendamento = "";			
			} else if (!valor .equalsIgnoreCase("")) {
				TipoAgendamento = valor;
			}	
		}		
	}
	
	public boolean isAgendamentoManual() {
		return this.TipoAgendamento != null && this.TipoAgendamento.equalsIgnoreCase("1");
	}
	
	public boolean isAgendamentoAutomatico() {
		return this.TipoAgendamento != null && this.TipoAgendamento.equalsIgnoreCase("2");
	}
	
	public void limpar() {
		super.limpar();
		Id_AudienciaProcessoFisico = "";
		ProcessoNumero = "";	
		TipoAgendamento = "";		
	}
	
	public boolean isTipoConciliacaoMediacaoCEJUSC() {
		int tipoCodigo = Funcoes.StringToInt(getAudienciaTipoCodigo());
		if (tipoCodigo == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() || 
				tipoCodigo == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() || 
				tipoCodigo == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo()){
			return true;
		}

		return false;
	}
}
