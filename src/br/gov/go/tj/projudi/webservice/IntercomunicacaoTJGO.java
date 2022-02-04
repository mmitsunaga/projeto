package br.gov.go.tj.projudi.webservice;

import br.jus.cnj.intercomunicacao.servico.Intercomunicacao;

@javax.jws.WebService(name="servico-intercomunicacao-2.2.2",
targetNamespace="http://www.cnj.jus.br/servico-intercomunicacao-2.2.2/")
@javax.jws.soap.SOAPBinding(parameterStyle=javax.jws.soap.SOAPBinding.ParameterStyle.BARE)
public interface IntercomunicacaoTJGO extends Intercomunicacao {

	@javax.jws.WebMethod(action="http://www.cnj.jus.br/servico-intercomunicacao-2.2.2/consultarRelatorioDeIntimacoesTJGO")
	@javax.jws.WebResult(name="consultarRelatorioDeIntimacoesTJGOResposta",
                         targetNamespace="http://www.cnj.jus.br/servico-intercomunicacao-2.2.2/",
                         partName="parameters")	
	RespostaConsultarRelatorioDeIntimacoesTJGO consultarRelatorioDeIntimacoesTJGO(RequisicaoCredenciaisTJGO requisicao);
}
