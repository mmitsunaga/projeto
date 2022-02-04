package br.gov.go.tj.projudi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.softwareag.common.resourceutilities.message.MessageException;

import br.gov.go.tj.projudi.ne.ProcessoNumeroNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 *  Classe que controla numeração de processos.
 *  Iternamente há um algoritmo para geração dos números dos processos, 
 *  incluindo a presença de um dígito verificador
 * 
 * 	Singleton design pattern é utilizado em razão de a entidade oferecer o
 * 	número de um novo processo
 * 
 */
public class ProcessoNumero {

	private static ProcessoNumero processonumero = null;	

	/**
	 * Método de captura do singleton
	 * 
	 * @return O singleton
	 */
	public static ProcessoNumero getInstance(){
		if (processonumero == null) processonumero = new ProcessoNumero();
		return ProcessoNumero.processonumero;
	}

	/**
	 * Construtor que captura o último número de processo gerado.
	 * Se tratar de um novo ano, a numeração é zerada.
	 * Armazena nas variáveis *NumeroProcesso: o próximo numero a ser usado
	 * 						  *Ano: ano do último processo
	 */
	private ProcessoNumero(){
		
	}

	/**
	 * Devolve a próxima numeração de processo no seguinte formato: AnoNumeroDigito
	 * e atualiza varíavel Numero com a próxima numeração. 
	 * 39 - código da comarca - substring(0,2)
	 * 2008 - ano atual - substring (2,6)
	 * 002775 - numero processo - substring (6,12)
	 * 6 - dígito verificador - substring (12,13)
	 */
	public String getProcessoNumero(String codigoForum) throws Exception {
		
		long loForumCodigo = Funcoes.StringToLong(codigoForum,-9999);
		
		if(loForumCodigo==-9999) {
			throw new MessageException("Não foi possível determinar o Código do Forum. Favor entrar em contato com o suporte e informação a Comarca e Área de Distribuição.");			
		}
		
	    ProcessoNumeroNe processoNumeroNe = new ProcessoNumeroNe();
	    
		String NumeroProcesso = processoNumeroNe.gerarNumero();
		
		SimpleDateFormat FormatoData = new SimpleDateFormat("yyyy");
		int Ano = Funcoes.StringToInt(FormatoData.format(new Date()));
			
		
		String stDigito = Funcoes.calcula_mod97(Funcoes.StringToLong(NumeroProcesso), Ano, Funcoes.StringToLong(Configuracao.JTR.replace(".", "")),  loForumCodigo );
		NumeroProcesso = String.valueOf(Ano) + Funcoes.completarZeros(String.valueOf(NumeroProcesso),7) + stDigito;
				
		
		return NumeroProcesso;
	}


    public boolean isValidoNumeroProcesso(String numero, String digito, int ano, long codigoForum , String jtr) {
    	if (jtr==null) {
    		jtr = Configuracao.JTR.replace(".", ""); 
    	}
    	try {
    		String stDigito = Funcoes.calcula_mod97(Funcoes.StringToLong(numero), ano, Funcoes.StringToLong(jtr),  codigoForum );
    		if (stDigito.equals(digito)) {
    			return true;
    		}else {
    			return false;
    		}
    		
    	}catch(Exception e){
    		return false;
    	}    	    
 	   
    }

}
