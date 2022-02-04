package br.gov.go.tj.projudi.sessaoVirtual.voto;

import java.util.List;

import br.gov.go.tj.projudi.dt.VotoDt;

public interface FiltroVoto {
	List<VotoDt> filtrar(List<VotoDt> votos);
}
