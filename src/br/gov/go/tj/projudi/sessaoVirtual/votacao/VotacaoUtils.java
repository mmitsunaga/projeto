package br.gov.go.tj.projudi.sessaoVirtual.votacao;

import java.util.List;

import br.gov.go.tj.projudi.dt.ResultadoVotacaoSessao;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.sessaoVirtual.voto.FiltroVotoReal;

public class VotacaoUtils {	
	private static ResultadoVotacaoSessao geraResultado(int soma, int total) {
		if(soma == total) {
			return ResultadoVotacaoSessao.UNANIMIDADE;
		}
		return soma >= 0 ? ResultadoVotacaoSessao.MAIORIA_RELATOR : ResultadoVotacaoSessao.MAIORIA_DIVERGE;
	}
	
	public static ResultadoVotacaoSessao calculaResultado(List<VotoDt> votos) {		
		List<VotoDt> votosReais = filtraVotosReais(votos);
		
		return geraResultado(votosReais.stream().mapToInt(VotoDt::getValorVoto).sum(), votosReais.size());
	}
	
	public static List<VotoDt> filtraVotosReais(List<VotoDt> votos){
		return new FiltroVotoReal().filtrar(votos);
	}
}
