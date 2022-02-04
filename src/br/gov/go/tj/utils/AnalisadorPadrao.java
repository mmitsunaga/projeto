package br.gov.go.tj.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class AnalisadorPadrao {
	
	private static final String ESPACO_BRANCO = " ";
	
	/**
	 * Faz a avaliação de uma consulta informada pelo usuario e faz a troca de operadores 
	 * @param query
	 */
	public static String avaliar (String consulta) throws Exception {
		
		// Verifica se foi digitado uma consulta
		if (ValidacaoUtil.isNaoVazio(consulta)) {
			
			// Limpa a string se houver um "			
			String textoSemAspasDuplas = consulta.replaceAll("\"", "");
			
			// troca o caracterer $ por *
			String texto = textoSemAspasDuplas.replaceAll("\\$", "*");
									
			// Separa os operandos e operadores pelo espaço em branco
			StringTokenizer tokenizer = new StringTokenizer(texto, ESPACO_BRANCO);
			
			// Cria uma lista com os termos encontrados
			List<String> tokens = new ArrayList<>();
			while (tokenizer.hasMoreTokens()){
				tokens.add(tokenizer.nextToken());
			}
			
			// Percorre a lista de tokens fazendo a troca dos operadores
			for (int i = 0; i < tokens.size(); i++){
				
				String token = tokens.get(i);
				
				if (token.trim().equals("E")) tokens.set(i, "+");
				
				if (token.trim().equals("OU")) tokens.set(i, "|");
				
				if (token.trim().equals("NAO")) tokens.set(i, "-");
				
				if (token.trim().equals("$")) tokens.set(i, "*");
												
				 // ObtÃ©m o termo Ã  esquerda e Ã  direita 
				 // Se ambos existirem e forem diferente de vazio
				 //   Faz a troca de posiÃ§Ã£o com o prÃ³ximo termo
				 //   Coloca aspas duplas no final termo atual \"
				 //   Coloca aspas duplas no inicio do termo atual \"
				 // Ex: moto ADJ2 carro -> "moto carro"~2				 
				if (token.trim().startsWith("ADJ")) {
					String rightToken = getProximoToken(tokens, i);
					String leftToken = getTokenAnterior(tokens, i);
					if (!rightToken.isEmpty() && !leftToken.isEmpty()){
						if (!isOperador(rightToken) && !isOperador(leftToken)){
							tokens.set(i, rightToken + "\\\"");
							tokens.set(i+1, token.replace("ADJ", "~"));
							tokens.set(i-1, "\\\"" + leftToken);							
						}
					}
				}
				
				// Mesmo processo do ADJ, mas fazendo a junÃ§Ã£o dos termos
				// trocando a ordem e somando os resultados com o operador OU
				// Ex: moto PROX2 carro -> ("moto carro"~2 | "carro moto"~2)			
				if (token.trim().startsWith("PROX")) {
					String rightToken = getProximoToken(tokens, i);
					String leftToken = getTokenAnterior(tokens, i);
					if (!rightToken.isEmpty() && !leftToken.isEmpty()){
						if (!isOperador(rightToken) && !isOperador(leftToken)){							
							tokens.set(i-1, "(\\\"" + leftToken + " " + rightToken + "\\\"" +  token.replace("PROX", "~"));
							tokens.set(i, "|");
							tokens.set(i+1, "\\\"" + rightToken + " " + leftToken + "\\\"" +  token.replace("PROX", "~") + ")");
						}
					}
				}
				
			}
			
			return toString(tokens);
			
		} else return "";
	}
	
	/**
	 * Obtém o próximo elemento
	 * @param tokens
	 * @param indexAtual
	 * @return
	 */
	private static String getProximoToken(List<String> tokens, int index) {
		return (index + 1 < tokens.size()) ? tokens.get(index + 1) : "";
	}
		
	/**
	 * Obtém o elemento anterior
	 * @param tokens
	 * @param index
	 * @return
	 */
	private static String getTokenAnterior(List<String> tokens, int index) {
		return (index - 1 >= 0) ? tokens.get(index -1) : ""; 
	}
	
	/**
	 * Verifica se o token é operador lógico e não é palavra
	 * @param value
	 * @return
	 */
	private static boolean isOperador(String value) {		
		return value.equals("+") || value.equals("-") || value.equals("|") || value.equals("(") || value.equals(")") || value.startsWith("~") || value.startsWith("ADJ") || value.startsWith("PROX"); 
	}
	
	
	/**
	 * Verifica se entre dois termos, deve existir espaço em branco
	 * @param current
	 * @param next
	 * @return
	 */
	private static boolean isDeveTerEspacoEmBranco(String atual, String proximo) {
		boolean ambosTokensNaoVazios = ValidacaoUtil.isNaoVazio(atual) && ValidacaoUtil.isNaoVazio(proximo); 
		return ambosTokensNaoVazios && (!isOperador(atual) && (!isOperador(proximo) || proximo.equals("-"))); 
	}
	
	
	/**
	 * Converte a lista de tokens para string, incluindo espaço em branco entre as palavras e operadores, exceto o Não (-) e ~
	 * @param tokens
	 * @return
	 */
	private static String toString(List<String> tokens){
		StringBuilder query = new StringBuilder();
		for (int i = 0; i < tokens.size(); i++) {
			String currentToken = tokens.get(i);
			String nextToken = getProximoToken(tokens, i);
			query.append(isDeveTerEspacoEmBranco(currentToken, nextToken) ? currentToken + ESPACO_BRANCO : currentToken);
		}
		return query.toString().trim();
	}
	
}
