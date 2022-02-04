package br.gov.go.tj.projudi.ne.strategy.geracao_voto;

import br.gov.go.tj.projudi.dt.VotoTipoDt;

public class AnaliseErroMaterialVotoStrategy extends FinalizacaoVotoStrategy {
	public AnaliseErroMaterialVotoStrategy() { tipo = VotoTipoDt.ANALISE_ERRO_MATERIAL; }
}