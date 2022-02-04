package br.gov.go.tj.projudi.ne.strategy.geracao_voto;

import br.gov.go.tj.projudi.dt.VotoTipoDt;

public class ProclamacaoVotoStrategy extends FinalizacaoVotoStrategy {
	public ProclamacaoVotoStrategy() { tipo = VotoTipoDt.PROCLAMACAO_DECISAO; }
}