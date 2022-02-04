package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

public class AdiarJulgamentoDt {
	private List<AudienciaProcessoDt> audiProcs;
	private ArquivoTipoDt arquivoTipo;
	private String tipoVoto;
	
	public AdiarJulgamentoDt() {
		audiProcs =  new ArrayList<>();
	}
	
	
	public ArquivoTipoDt getArquivoTipo() {
		return arquivoTipo;
	}
	public void setArquivoTipo(ArquivoTipoDt arquivoTipo) {
		this.arquivoTipo = arquivoTipo;
	}
	public List<AudienciaProcessoDt> getAudiProcs() {
		return audiProcs;
	}
	public void setProcessos(List<AudienciaProcessoDt> audiProcs) {
		this.audiProcs = audiProcs;
	}


	public String getTipoVoto() {
		return tipoVoto;
	}


	public void setTipoVoto(String tipoVoto) {
		this.tipoVoto = tipoVoto;
	}
}
