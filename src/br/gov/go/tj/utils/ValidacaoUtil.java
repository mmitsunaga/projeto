package br.gov.go.tj.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Classe Utilitária que faz a validação de um Object
 * @author mmitsunaga
 *
 */
public final class ValidacaoUtil {

	private ValidacaoUtil() {
	}

	public static boolean isNaoNulo(Object objeto) {
		return (!isNulo(objeto));
	}

	public static boolean isNulo(Object objeto) {
		return objeto == null || objeto.toString().equals("nulo") || objeto.toString().equals("undefined");
	}

	public static boolean isNaoVazio(Object objeto) {
		return (!isVazio(objeto));
	}

	@SuppressWarnings("rawtypes")
	public static boolean isVazio(Object objeto) {
		if (isNulo(objeto)) {
			return Boolean.TRUE;
		} else if (objeto instanceof Collection) {
			return (((Collection) objeto).isEmpty());
		} else if (objeto instanceof Object[]){
			return (((Object[]) objeto).length == 0);
		} else if (objeto instanceof Map){
			return (((Map) objeto).isEmpty());
		} else if (objeto instanceof StringBuilder){
			return  (((StringBuilder) objeto).length() == 0);
		} else {
			return objeto.toString().trim().isEmpty();
		}		
	}

	public static boolean isPeloMenosUmVerdadeiro(boolean... valores) {
		boolean isPeloMenosUmVerdadeiro = false;
		for (boolean valor : valores) {
			if (valor) {
				isPeloMenosUmVerdadeiro = true;
				break;
			}
		}
		return isPeloMenosUmVerdadeiro;
	}

	public static boolean isTodosVerdadeiros(boolean... valores) {
		boolean isTodosVerdadeiros = true;
		for (boolean valor : valores) {
			if (!valor) {
				isTodosVerdadeiros = false;
				break;
			}
		}
		return isTodosVerdadeiros;
	}

	public static boolean isPeloMenosUmFalso(boolean... valores) {
		return !isTodosVerdadeiros(valores);
	}

	public static boolean isTodosFalsos(boolean... valores) {
		return !isPeloMenosUmVerdadeiro(valores);
	}
	
	public static boolean isValorZerado(Long valor){
		return isNaoNulo(valor) && valor.equals(Long.valueOf(0));
	}
	
	public static boolean isValorZerado(Integer valor){
		return isNaoNulo(valor) && valor.equals(Integer.valueOf(0));
	}
}
