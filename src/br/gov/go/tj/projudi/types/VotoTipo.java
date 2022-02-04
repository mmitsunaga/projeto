package br.gov.go.tj.projudi.types;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum VotoTipo {
	ACOMPANHA_RELATOR(1), ACOMPANHA_RELATOR_RESSALVA(2), DIVERGE(3), ACOMPANHA_DIVERGENCIA(4), PEDIDO_VISTA(5),
	JULGAMENTO_ADIADO(6), OBSERVACAO(7), PRAZO_EXPIRADO(8), IMPEDIDO(9), PROCLAMACAO_DECISAO(10),
	VERIFICAR_RESULTADO_VOTACAO(11), AGUARDANDO_PEDIDO_VISTA(12), SUSPEICAO(13);

	private final int tipo;

	private VotoTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getValue() {
		return tipo;
	}

	public static List<Integer> getAllValues() {
		return Stream.of(VotoTipo.values()).map(VotoTipo::getValue).collect(Collectors.toList());
	}

}