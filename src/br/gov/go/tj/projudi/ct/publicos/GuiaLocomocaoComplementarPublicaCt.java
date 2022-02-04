package br.gov.go.tj.projudi.ct.publicos;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.utils.Configuracao;

public class GuiaLocomocaoComplementarPublicaCt extends GuiaLocomocaoPublicaCt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -344004520098753383L;

	@Override
	protected boolean exibeLocomocoesNaoUtilizadasParaEmissaoDeComplementar() {
		return true;
	}
	
	@Override
	protected String obtenhaServletDeRetornoPesquisa() {
		return "GuiaLocomocaoComplementarPublica";
	} 
	
	@Override
	protected String obtenhaServletBuscaProcesso() {
		return "BuscaProcessoPublica";
	}
	
	@Override
	protected String obtenhaTituloPagina() {
		return "Guia Locomoção Complementar [Acesso Público]";
	}
	
	@Override
	protected String obtenhaAcaoMensagemOk(ProcessoDt processoDt, String mensagem) {
		return "GuiaLocomocaoComplementarPublica?PaginaAtual="+Configuracao.Novo+"&MensagemOk="+mensagem;
	}
	
	@Override
	protected String obtenhaAcaoMensagemErro(ProcessoDt processoDt, String mensagem) {
		return "GuiaLocomocaoComplementarPublica?PaginaAtual="+Configuracao.Novo+"&MensagemErro="+mensagem;
	}
}