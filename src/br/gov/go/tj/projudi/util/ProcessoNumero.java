package br.gov.go.tj.projudi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.softwareag.common.resourceutilities.message.MessageException;

import br.gov.go.tj.projudi.ne.ProcessoNumeroNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 *  Classe que controla numera��o de processos.
 *  Iternamente h� um algoritmo para gera��o dos n�meros dos processos, 
 *  incluindo a presen�a de um d�gito verificador
 * 
 * 	Singleton design pattern � utilizado em raz�o de a entidade oferecer o
 * 	n�mero de um novo processo
 * 
 */
public class ProcessoNumero {

	private static ProcessoNumero processonumero = null;	

	/**
	 * M�todo de captura do singleton
	 * 
	 * @return O singleton
	 */
	public static ProcessoNumero getInstance(){
		if (processonumero == null) processonumero = new ProcessoNumero();
		return ProcessoNumero.processonumero;
	}

	/**
	 * Construtor que captura o �ltimo n�mero de processo gerado.
	 * Se tratar de um novo ano, a numera��o � zerada.
	 * Armazena nas vari�veis *NumeroProcesso: o pr�ximo numero a ser usado
	 * 						  *Ano: ano do �ltimo processo
	 */
	private ProcessoNumero(){
		
	}

	/**
	 * Devolve a pr�xima numera��o de processo no seguinte formato: AnoNumeroDigito
	 * e atualiza var�avel Numero com a pr�xima numera��o. 
	 * 39 - c�digo da comarca - substring(0,2)
	 * 2008 - ano atual - substring (2,6)
	 * 002775 - numero processo - substring (6,12)
	 * 6 - d�gito verificador - substring (12,13)
	 */
	public String getProcessoNumero(String codigoForum) throws Exception {
		
		long loForumCodigo = Funcoes.StringToLong(codigoForum,-9999);
		
		if(loForumCodigo==-9999) {
			throw new MessageException("N�o foi poss�vel determinar o C�digo do Forum. Favor entrar em contato com o suporte e informa��o a Comarca e �rea de Distribui��o.");			
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
