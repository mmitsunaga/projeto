package br.gov.go.tj.projudi.dt;

import java.util.List;

public class AudienciaProcessoVotacaoDt {
	private AudienciaProcessoDt audienciaProcesso;
	private List<VotoDt> votos;
	private PendenciaArquivoDt votoRelator;
	private PendenciaArquivoDt ementaRelator;
	private String nomeRelator;

	public AudienciaProcessoDt getAudienciaProcesso() {
		return audienciaProcesso;
	}

	public void setAudienciaProcesso(AudienciaProcessoDt audienciaProcesso) {
		this.audienciaProcesso = audienciaProcesso;
	}

	public List<VotoDt> getVotos() {
		return votos;
	}

	public void setVotos(List<VotoDt> votos) {
		this.votos = votos;
	}

	public PendenciaArquivoDt getEmentaRelator() {
		return ementaRelator;
	}

	public void setEmentaRelator(PendenciaArquivoDt ementaRelator) {
		this.ementaRelator = ementaRelator;
	}

	public PendenciaArquivoDt getVotoRelator() {
		return votoRelator;
	}

	public void setVotoRelator(PendenciaArquivoDt votoRelator) {
		this.votoRelator = votoRelator;
	}

	public String getNomeRelator() {
		return nomeRelator;
	}

	public void setNomeRelator(String nomeRelator) {
		this.nomeRelator = nomeRelator;
	}

}
