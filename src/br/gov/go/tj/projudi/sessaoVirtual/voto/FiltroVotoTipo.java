package br.gov.go.tj.projudi.sessaoVirtual.voto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import br.gov.go.tj.projudi.dt.VotoDt;

public class FiltroVotoTipo implements FiltroVoto {

	private int[] tiposCodigo;

	public FiltroVotoTipo(int votoTipoCodigo) {
		tiposCodigo = new int[] {votoTipoCodigo};
	}
	
	public FiltroVotoTipo(int[] votoTiposCodigo) {
		tiposCodigo = votoTiposCodigo;
	}

	@Override
	public List<VotoDt> filtrar(List<VotoDt> votos) {
		return Optional.ofNullable(votos).orElse(Collections.emptyList())
				.stream()
				.filter(x -> 
					IntStream
						.of(tiposCodigo)
						.anyMatch(cod -> cod == x.getVotoCodigoInt())
				).collect(Collectors.toList());
	}

}
