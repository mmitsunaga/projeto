package br.gov.tj.teste;

import br.jus.cnj.intercomunicacao.beans.ObjectFactory;
import br.jus.cnj.intercomunicacao.beans.RespostaConsultaAvisosPendentes;

public class TesteWebServiceCNJ {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		br.jus.cnj.intercomunicacao.servico.Intercomunicacao intercomunicacao;
		
		br.jus.cnj.intercomunicacao.servico.ObjectFactory fabrica = new br.jus.cnj.intercomunicacao.servico.ObjectFactory();
		
		fabrica.createConsultarAlteracao(null);
		
		ObjectFactory fabricaObjetos = new ObjectFactory();
		
		RespostaConsultaAvisosPendentes resposta = fabricaObjetos.createRespostaConsultaAvisosPendentes();	
		
	}
}