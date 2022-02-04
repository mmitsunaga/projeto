package br.gov.go.tj.projudi.sessaoVirtual.votante;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;

public class FiltraVotoObservacao implements FiltroVoto {
    @Override
	public  List<VotoDt> filtrar(List<VotoDt> votos) {
		List<VotoDt> observacoes = votos != null ? votos.stream().filter(x -> StringUtils.equals(x.getVotoTipoCodigo(), String.valueOf(VotoTipoDt.OBSERVACAO))).collect(Collectors.toList()) : null;
		return observacoes;
	}

}
