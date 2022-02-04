package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessaoVirtualPublicaDt {
	private AudienciaProcessoDt audienciaProcesso;
	private VotanteDt mp;
	private VotanteDt relator;
	private List<String> votos;

	public SessaoVirtualPublicaDt() {
		audienciaProcesso = new AudienciaProcessoDt();
		mp = new VotanteDt();
		relator = new VotanteDt();
		votos = new ArrayList<>();
	}
	
	public List<String> getVotos() {
		return votos;
	}
	
	public ProcessoDt getProcessoDt() {
		return Optional.ofNullable(audienciaProcesso).map(AudienciaProcessoDt::getProcessoDt).orElse(null);
	}
	
	public String getIdAudienciaProcesso() {
		return Optional.ofNullable(audienciaProcesso).map(AudienciaProcessoDt::getId).orElse("");
	}
	
	public void setVotos(List<String> votos) {
		this.votos = votos;
	}
	public AudienciaProcessoDt getAudienciaProcesso() {
		return audienciaProcesso;
	}
	public void setAudienciaProcesso(AudienciaProcessoDt audienciaProcesso) {
		this.audienciaProcesso = audienciaProcesso;
	}

	public VotanteDt getMp() {
		return mp;
	}

	public void setMp(VotanteDt mp) {
		if(mp != null) {
			this.mp = mp;
		}
	}

	public VotanteDt getRelator() {
		return relator;
	}

	public void setRelator(VotanteDt relator) {
		if(relator != null) {
			this.relator = relator;
		}
	}
	
}
