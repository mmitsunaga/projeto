package br.gov.go.tj.projudi.dt;

import java.util.List;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.ServentiaDt;

public class UsuarioCandidatoComarcaDt extends Dados {
	
	private String id;
	private ComarcaDt comarcaDt;
	private ServentiaDt serventiaDt;
	private String dataInscricao;
	private UsuarioCejuscDt usuarioCejuscDt;
	private String codigoEscolha;
	
	//Variaveis auxiliares
	private String perfilEscolhidoConciliador;
	private String perfilEscolhidoMediador;
	private List<UsuarioCandidatoComarcaTurnoDt> listaUsuarioCandidatoComarcaTurnoDt;
	private String timemilisParaExcluir = null;
	
	public UsuarioCandidatoComarcaDt() {
		comarcaDt = new ComarcaDt();
		serventiaDt = new ServentiaDt();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ComarcaDt getComarcaDt() {
		return comarcaDt;
	}
	public void setComarcaDt(ComarcaDt comarcaDt) {
		this.comarcaDt = comarcaDt;
	}
	public ServentiaDt getServentiaDt() {
		return serventiaDt;
	}
	public void setServentiaDt(ServentiaDt serventiaDt) {
		this.serventiaDt = serventiaDt;
	}
	public String getDataInscricao() {
		return dataInscricao;
	}
	public void setDataInscricao(String dataInscricao) {
		this.dataInscricao = dataInscricao;
	}
	public UsuarioCejuscDt getUsuarioCandidatoDt() {
		return usuarioCejuscDt;
	}
	public void setUsuarioCandidatoDt(UsuarioCejuscDt usuarioCejuscDt) {
		this.usuarioCejuscDt = usuarioCejuscDt;
	}
	public String getPerfilEscolhidoConciliador() {
		return perfilEscolhidoConciliador;
	}
	public void setPerfilEscolhidoConciliador(String perfilEscolhidoConciliador) {
		this.perfilEscolhidoConciliador = perfilEscolhidoConciliador;
	}
	public String getPerfilEscolhidoMediador() {
		return perfilEscolhidoMediador;
	}
	public void setPerfilEscolhidoMediador(String perfilEscolhidoMediador) {
		this.perfilEscolhidoMediador = perfilEscolhidoMediador;
	}
	public String getCodigoEscolha() {
		return codigoEscolha;
	}
	public void setCodigoEscolha(String codigoEscolha) {
		this.codigoEscolha = codigoEscolha;
	}
	
	public List<UsuarioCandidatoComarcaTurnoDt> getListaUsuarioCandidatoComarcaTurnoDt() {
		return listaUsuarioCandidatoComarcaTurnoDt;
	}
	public void setListaUsuarioCandidatoComarcaTurnoDt(
			List<UsuarioCandidatoComarcaTurnoDt> listaUsuarioCandidatoComarcaTurnoDt) {
		this.listaUsuarioCandidatoComarcaTurnoDt = listaUsuarioCandidatoComarcaTurnoDt;
	}
	public String getTimemilisParaExcluir() {
		return timemilisParaExcluir;
	}

	public void setTimemilisParaExcluir(String timemilisParaExcluir) {
		this.timemilisParaExcluir = timemilisParaExcluir;
	}

	public String getPropriedades() {
		return "UsuarioCandidatoComarcaDt [id=" + id + ", comarcaDt=" + comarcaDt + ", serventiaDt=" + serventiaDt
				+ ", dataInscricao=" + dataInscricao + ", usuarioCandidatoDt=" + usuarioCejuscDt + ", codigoEscolha="
				+ codigoEscolha + ", perfilEscolhidoConciliador=" + perfilEscolhidoConciliador
				+ ", perfilEscolhidoMediador=" + perfilEscolhidoMediador + "]";
	}
}
