package br.gov.go.tj.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import br.gov.go.tj.utils.Certificado.Base64Utils;

/**
*
* @author  Jesus Rodrigo Corrêa
* @version 2.0.0
* @since 10/09/2009
* @Organizacao Tribunal de Justiça do Estado de Goiás
* @Equipe Projudi
*/

public class Cifrar {

	public static final String[] representacaoBinarioDecimal={"0000","0001","0010","0011","0100","0101","0110","0111","1000","1001"};
	public static final String[] representacaoBinarioBase32={"00000","00001","00010","00011","00100","00101","00110","00111","01000","01001",
															 "01010","01011","01100","01101","01110","01111","10000","10001","10010","10011",
															 "10100","10101","10110","10111","11000","11001","11010","11011","11100","11101","11110","11111"};
    public static final String[] representacaoBase32={"j","b","w","c","d","@","e","x","f","2","a","h","z","p","k","3","*","4","m","u","n","5","7","6","q","r","8","s","t","9","v","g"};
	
    /**
     * Descriptografa de base32 para decimal
     * @param Valor base 32
     * @author jrcorrea
     */        
    public static String converterBase32Decimal(String valor) throws Exception{
        StringBuilder stTemp = new StringBuilder();        
        valor = valor.trim();
         
        //converto para binario
        for (int i=0; i<(valor.length());i++){               
            int indice = indiceBase32(valor.substring(i, i+1));
            stTemp.append(representacaoBinarioBase32[indice]);
        }
        String base32 = binarioDecimal(stTemp.toString());
        //sempre que for multiplo de 4 restará um zero no final, retiro esse.
        if ((valor.length()%4)==0) 
        	if (base32.substring(base32.length()-1, base32.length()).equalsIgnoreCase("0"))
        		base32 = base32.substring(0, base32.length()-1); 
        return base32;      
    }
    /**
     * Criptografar de base32 para decimal
     * @param Valor decimal
     * @author jrcorrea
     */        
    public  static String converterDecimalBase32(String valor)throws Exception{
        StringBuilder stTemp = new StringBuilder();
        valor = valor.trim();
        //converto para binario
        //System.out.println(valor);
        for (int i=0; i<valor.length();i++){
            int indice = Funcoes.StringToInt(valor.substring(i, i+1));
            stTemp.append(representacaoBinarioDecimal[indice]);
        }
        String base32 = binarioBase32(stTemp.toString());
        return base32;            
    }
    
    
     private static int indiceBase32(String valor)throws Exception{
        for (int i=0;i<representacaoBase32.length;i++)
            if (valor.equalsIgnoreCase(representacaoBase32[i]))
                return i;
        throw new Exception("<{Erro ao localizar indice.}> cifrar.indiceBase32(): ");
    }     
     
    private static  int indiceBinario(String valor)throws Exception{
        for (int i=0;i<representacaoBinarioDecimal.length;i++)
            if (valor.equalsIgnoreCase(representacaoBinarioDecimal[i]))
                return i;
        throw new Exception("<{Erro ao localizar indice.}> cifrar.indiceBinario(): ");
    }
     
    private  static int indiceBinarioBase32(String valor)throws Exception{
        for (int i=0;i<representacaoBinarioBase32.length;i++)
            if (valor.equalsIgnoreCase(representacaoBinarioBase32[i]))
                return i;
        throw new Exception("<{Erro ao localizar indice.}> cifrar.indiceBinarioBase32(): ");
    }
          
    private static String binarioDecimal(String valor)throws Exception{
        StringBuilder stTemp = new StringBuilder();	
        int tamanho = valor.length();
        while ((tamanho % 4)!=0 ){
            valor=valor.substring(0, tamanho-1);
            tamanho = valor.length();
        }
        for (int i=0;i<valor.length();i+=4){               
            stTemp.append(indiceBinario(valor.substring(i,i+4)));
        }
        
        return stTemp.toString();
    }
    
    private  static String binarioBase32(String valor)throws Exception{
        StringBuilder stTemp = new StringBuilder();	
        int tamanho = valor.length();
        while ((tamanho % 5)!=0 ){
            valor+="0";
            tamanho = valor.length();
        }
        for (int i=0;i<valor.length();i+=5){               
            stTemp.append(representacaoBase32[indiceBinarioBase32(valor.substring(i,i+5))]);
        }
        
        return stTemp.toString();
    } 
    
	public static String DigitoVerificadorMod11(String numero, int qtdDigitos) {
		String stDigito = "";
		String stTemp = numero;
		for (int j = 0; j < qtdDigitos; j++) {
			long loSoma = 0;
			long inFator = 2;
			for (int i = stTemp.length(); i > 0; i--)
				loSoma += (Funcoes.StringToInt(stTemp.substring(i - 1, i)) * inFator++);
			long loDigito = (loSoma * 10) % 11;
			if (loDigito == 10) loDigito = 0;
			stDigito += String.valueOf(loDigito);
			stTemp += String.valueOf(loDigito);
		}
		return stDigito;
	}

	static char[] chRepalce = {'b', '1', 'e', 'a', '4', 'd', '3', 'c', '0', '2' };
	static char[][] chTabela = {{'a', 'b', 'c', 'd', 'e', '0', '1', '2', '3', '4' }, // 0
			{'b', 'c', 'd', 'e', '0', '1', '2', '3', '4', 'a' }, // 1
			{'c', 'd', 'e', '0', '1', '2', '3', '4', 'a', 'b' }, // 2
			{'d', 'e', '0', '1', '2', '3', '4', 'a', 'b', 'c' }, // 3
			{'e', '0', '1', '2', '3', '4', 'a', 'b', 'c', 'd' }, // 4
			{'0', '1', '2', '3', '4', 'a', 'b', 'c', 'd', 'e' }, // 5
			{'1', '2', '3', '4', 'a', 'b', 'c', 'd', 'e', '0' }, // 6
			{'2', '3', '4', 'a', 'b', 'c', 'd', 'e', '0', '1' }, // 7
			{'3', '4', 'a', 'b', 'c', 'd', 'e', '0', '1', '2' }, // 8
			{'4', 'a', 'b', 'c', 'd', 'e', '0', '1', '2', '3' }, // 9
			{'b', 'c', 'd', 'e', '0', '1', '2', '3', '4', 'a' }, // 10
			{'a', 'b', 'c', 'd', 'e', '0', '1', '2', '3', '4' }, // 11
			{'1', '2', '3', '4', 'a', 'b', 'c', 'd', 'e', '0' }, // 12
			{'d', 'e', '0', '1', '2', '3', '4', 'a', 'b', 'c' }, // 13
			{'e', '0', '1', '2', '3', '4', 'a', 'b', 'c', 'd' }, // 14
			{'3', '4', 'a', 'b', 'c', 'd', 'e', '0', '1', '2' }, // 15
			{'c', 'd', 'e', '0', '1', '2', '3', '4', 'a', 'b' }, // 16
			{'0', '1', '2', '3', '4', 'a', 'b', 'c', 'd', 'e' }, // 17
			{'4', 'a', 'b', 'c', 'd', 'e', '0', '1', '2', '3' }, // 18
			{'2', '3', '4', 'a', 'b', 'c', 'd', 'e', '0', '1' }, // 19
	};

	private static String codificarMatriz(String dados) {
		StringBuffer stTemp = new StringBuffer();

		for (int i = 0; i < dados.length(); i++) {
			for (int j = 0; j < 10; j++)
				if (chTabela[i][j] == dados.charAt(i)) {
					stTemp.append(j);
					break;
				}
		}
		return stTemp.toString();
	}

	private static String decodificarMatriz(String dados) {
		StringBuffer stTemp = new StringBuffer();

		for (int i = 0; i < dados.length(); i++)
			stTemp.append(chTabela[i][Funcoes.StringToInt(dados.substring(i, i + 1))]);

		return stTemp.toString();
	}

	private static String codificarVetor(String dados) {
		StringBuffer stTemp = new StringBuffer();
		//primeiro troco os caracteres
		for (int i = 0; i < dados.length(); i++)
			stTemp.append(chRepalce[Funcoes.StringToInt(dados.substring(i, i + 1))]);

		return stTemp.toString();
	}

	private static String decodificarVetor(String dados) {
		StringBuffer stTemp = new StringBuffer();
		//primeiro troco os caracteres
		for (int i = 0; i < dados.length(); i++)
			for (int j = 0; j < chRepalce.length; j++)
				if (chRepalce[j] == dados.charAt(i)) stTemp.append(j);

		return stTemp.toString();
	}
	
	public static String codificarId_certidao(String dados) {
		StringBuffer stTemp = new StringBuffer();
		String stTemp01 = "";

		//Completo com zeros a direita
		stTemp01 = Funcoes.completarZeros(dados, 10);

		//codifico o vetor
		stTemp01 = codificarVetor(stTemp01);

		//codifico a matriz
		stTemp = new StringBuffer(codificarMatriz(stTemp01));

		//gero o digito verificador
		String stDigito = DigitoVerificadorMod11(stTemp.toString(), 2);

		//incluo o digito no numero
		stTemp.insert(3, stDigito.substring(0, 1));
		stTemp.insert(7, stDigito.substring(1, 2));

		return stTemp.toString();
	}
	
	
	public static String decodificarId_Certidao(String dados) {
		if (dados.length() != 12) return "-1";

		String stDigito = dados.substring(3, 4) + dados.substring(7, 8);
		String stTemp = dados.substring(0, 3) + dados.substring(4, 7) + dados.substring(8);
		String stDigito01 = DigitoVerificadorMod11(stTemp, 2);
		if (stDigito.equalsIgnoreCase(stDigito01)) {
			stTemp = decodificarMatriz(stTemp);
			stTemp = decodificarVetor(stTemp);
			return stTemp;

		} else return "-1";
	}

	public static String codificar(String dados, int permissao) {
		StringBuffer stTemp = new StringBuffer();
		String stTemp01 = "";

		//Completo com zeros a direita
		stTemp01 = Funcoes.completarZeros(String.valueOf(permissao), 5)  + Funcoes.completarZeros(dados, 10);

		//codifico o vetor
		stTemp01 = codificarVetor(stTemp01);

		//codifico a matriz
		stTemp = new StringBuffer(codificarMatriz(stTemp01));

		//gero o digito verificador
		String stDigito = DigitoVerificadorMod11(stTemp.toString(), 2);

		//incluo o digito no numero
		stTemp.insert(3, stDigito.substring(0, 1));
		stTemp.insert(7, stDigito.substring(1, 2));

		return stTemp.toString();
	}
	
	public static String decodificar(String dados, int permissao) {
		if (dados.length() != 17) return "-1";
		
		String stDigito = dados.substring(3, 4) + dados.substring(7, 8);
		String stTemp = dados.substring(0, 3) + dados.substring(4, 7) + dados.substring(8);
		String stDigito01 = DigitoVerificadorMod11(stTemp, 2);
		if (stDigito.equalsIgnoreCase(stDigito01)) {
			stTemp = decodificarMatriz(stTemp);
			stTemp = decodificarVetor(stTemp);
			String stPermessao=stTemp.substring(0,5);
			if (stPermessao.equals( Funcoes.completarZeros(String.valueOf(permissao),5))){
				return stTemp.substring(5,stTemp.length());
			}else{
				return "-1";
			}

		} else return "-1";
	}
	
	public static String hashMd5(String dado) throws Exception {
		String tempSenha = null;
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(dado.getBytes());
			tempSenha= Base64Utils.base64Encode(md.digest());
			//BigInteger hash = new BigInteger(1, );
			//tempSenha = hash.toString(30);
			//tempSenha = md.digest().toString();
			////System.out.println(tempSenha);
		} catch(NoSuchAlgorithmException ns) {
			throw ns;
		}
		return tempSenha;
	}

}
