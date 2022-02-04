package br.gov.go.tj.projudi.sessaoVirtual.voto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.ListUtils;

import br.gov.go.tj.projudi.dt.VotoDt;

public class FiltroVotoReal implements FiltroVoto {
    
	@Override
	public  List<VotoDt> filtrar(List<VotoDt> votos) {
		return Optional.ofNullable(votos).orElse(Collections.emptyList()).stream().filter(VotoDt::isVoto).collect(Collectors.toList());
	}
}
