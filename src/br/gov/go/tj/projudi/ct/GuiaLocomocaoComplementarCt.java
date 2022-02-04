package br.gov.go.tj.projudi.ct;

import br.gov.go.tj.projudi.dt.GuiaLocomocaoComplementarDt;

public class GuiaLocomocaoComplementarCt extends GuiaLocomocaoCt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5053012739469132350L;

	@Override
	public int Permissao() {
		return GuiaLocomocaoComplementarDt.CodigoPermissao;
	}
	
	@Override
	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaLocomocaoComplementar";
	} 
	
	@Override
	protected boolean exibeLocomocoesNaoUtilizadasParaEmissaoDeComplementar() {
		return true;
	}
	
	@Override
	protected String obtenhaTituloPagina() {
		return "Guia Locomoção Complementar";
	}
}
