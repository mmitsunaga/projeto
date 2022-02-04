package br.gov.go.tj.utils;

/**
 * Classe do tipo enumeration contendo os dias da semana.
 * 
 * @author Keila Sousa Silva
 * 
 */
public enum DiaDaSemana {
    SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO, DOMINGO;

    public static final DiaDaSemana getDiaDaSemanaDescricao(int diaDaSemanaNumerico) {
	switch (diaDaSemanaNumerico) {
	case 1: {
	    return DiaDaSemana.SEGUNDA;
	}
	case 2: {
	    return DiaDaSemana.TERCA;
	}
	case 3: {
	    return DiaDaSemana.QUARTA;
	}
	case 4: {
	    return DiaDaSemana.QUINTA;
	}
	case 5: {
	    return DiaDaSemana.SEXTA;
	}
	case 6: {
	    return DiaDaSemana.SABADO;
	}
	case 7: {
	    return DiaDaSemana.DOMINGO;
	}
	}
	return null;
    }
}
