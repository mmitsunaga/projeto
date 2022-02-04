package br.gov.go.tj.projudi.dt;

public class AudienciaFisicoMovimentacaoDt extends AudienciaMovimentacaoDt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8521551227501551585L;	
	
	public String getProcessoNumero() {
		return getAudienciaProcessoFisicoDt().getProcessoNumero();
	}

	public AudienciaProcessoFisicoDt getAudienciaProcessoFisicoDt() {
		if (super.getAudienciaDt() != null && super.getAudienciaDt().getAudienciaProcessoDt() instanceof AudienciaProcessoFisicoDt) {
			AudienciaProcessoFisicoDt audienciaProcesso = (AudienciaProcessoFisicoDt)super.getAudienciaDt().getAudienciaProcessoDt();
			if (audienciaProcesso != null) return audienciaProcesso;
		}			
		return new AudienciaProcessoFisicoDt();
	}
}