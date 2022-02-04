package br.gov.go.tj.projudi.sessaoVirtual.trocaResponsavel;

public class TrocaResponsavelFactoryPendencia implements TrocaResponsavelFactory{

	@Override
	public TrocaResponsavel criarTrocaResponsavel(TrocaResponsavelParam params) {
		TrocaResponsavelPendencia retorno;
		if(params.isDistribuicao() == true) {
			 retorno = new TrocaResponsavelPendenciaComDistribuidor();
		}else {
			retorno = new TrocaResponsavelPendenciaSemDistribuidor();
		}
		retorno.preencheParametros(params);
		return retorno;
	}
}
