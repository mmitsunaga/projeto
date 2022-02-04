package br.gov.go.tj.projudi.ne.strategy.geracao_voto;

class FinalizacaoVotoStrategy extends GeracaoVotoStrategy {
	@Override
	protected void before() throws Exception {
		super.before();

		votoDt.setIdAudienciaProcessoStatusVencido(audienciaProcessoNe.consultarStatusAudienciaTemp(idAudienciaProcesso, fabrica).getId());
	}
}
