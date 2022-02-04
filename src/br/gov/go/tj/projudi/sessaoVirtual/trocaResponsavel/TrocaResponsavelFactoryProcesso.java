package br.gov.go.tj.projudi.sessaoVirtual.trocaResponsavel;

public class TrocaResponsavelFactoryProcesso implements TrocaResponsavelFactory{

	@Override
	public TrocaResponsavel criarTrocaResponsavel(TrocaResponsavelParam params) {
		TrocaResponsavelProcesso retorno = new TrocaResponsavelProcesso();
		retorno.preencheParametros(params);
		return retorno;
	}

}
