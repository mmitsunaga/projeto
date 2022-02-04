package br.gov.go.tj.projudi.dt;

import java.util.List;

public class AssinarVotoSessaoDt {
	String nomeArquivos;
	String conteudoArquivos;
	List<VotoSessaoLocalizarDt> votos;
	
	public AssinarVotoSessaoDt() {
		nomeArquivos = "";
		conteudoArquivos = "";
	}
	
	public String getNomeArquivos() {
		return nomeArquivos;
	}
	public void setNomeArquivos(String nomeArquivos) {
		this.nomeArquivos = nomeArquivos;
	}
	public String getConteudoArquivos() {
		return conteudoArquivos;
	}
	public void setConteudoArquivos(String conteudoArquivos) {
		this.conteudoArquivos = conteudoArquivos;
	}
	public List<VotoSessaoLocalizarDt> getVotos() {
		return votos;
	}
	public void setVotos(List<VotoSessaoLocalizarDt> votos) {
		this.votos = votos;
	}
	
	

}
