package br.gov.go.tj.projudi;

import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class Teste {

	public static void main(String[] args) {
   		// TODO Auto-generated method stub
    	String NumeroProcesso = "286877";
    	
    	int Ano = 2015;
    	long forum =175;
    	
    	System.out.println( forum);
    	
    	String stDigito = Funcoes.calcula_mod97(Funcoes.StringToLong(NumeroProcesso), Ano, Funcoes.StringToLong(Configuracao.JTR.replace(".", "")), forum);
    	NumeroProcesso =  Funcoes.completarZeros(String.valueOf(NumeroProcesso),7)  + "." + stDigito + "." + String.valueOf(Ano) + "." + Configuracao.JTR + "." + forum;
    					
    			
    	System.out.println( NumeroProcesso);
    	
////		String stCaracteres = "AaBbCc@@DdEeFfGgHh$$IiJjKkLl##MmNn&&OoPqRrSsTtUuVvXxWwYyZz";
////		int loNumero = (int) Math.round((Math.random()*9999));
////		int loCaracter = (int) Math.round((Math.random()*stCaracteres.length()));
////		String codigo= stCaracteres.substring(loCaracter,loCaracter+1) + loNumero;
////		
////		System.out.println( codigo);
//    	System.out.println(Funcoes.converteNomeSimplificado("ROGERIO PINTO MESQUITA"));
//    	ProcessoNe processoNe = new ProcessoNe();
//    			
//    			try {
//					System.out.println( processoNe.gerarCodigoAcessoProcesso("2833948","5124906","6772874"));
//					System.out.println("5124906.16");
////					System.out.println( processoNe.gerarCodigoAcessoProcesso("733785","5304681","7150936"));
////					System.out.println("118655.42");
////					System.out.println( processoNe.gerarCodigoAcessoProcesso("3964050","118655","9935606"));
////					System.out.println("384606.28");
////					System.out.println( processoNe.gerarCodigoAcessoProcesso("3964546","384606","9936844"));
////					System.out.println("384609.80");
////					System.out.println( processoNe.gerarCodigoAcessoProcesso("3953923","384609","9908547"));
////					System.out.println("52033.37");
////					System.out.println( processoNe.gerarCodigoAcessoProcesso("3733452","52033","9321512"));
////					System.out.println("5468177.98");
////					System.out.println( processoNe.gerarCodigoAcessoProcesso("3836997","5468177","9596913"));
//					
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
    			
//    	texto = texto.replaceAll("[$][{][\\s]*"+PROCESSO_NUMERO+"[\\s]*[}]", format(processo.getProcessoNumeroCompleto()));
//		texto = texto.replaceAll("[$][{][\\s]*" + PROCESSO_CODIGO_ACESSO + "[\\s]*[}]",);
    	
    }

}
