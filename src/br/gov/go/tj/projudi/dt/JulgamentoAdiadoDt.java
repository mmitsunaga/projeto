package br.gov.go.tj.projudi.dt;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class JulgamentoAdiadoDt {
	private String classe;
	private String processoNumero;
	private String relator;
	private String advogadoSolicitante;
	private String dataHoraSolicitacao;
	private String parte;
	private List<JulgamentoAdiadoDt> advsPedidoSOdeferido;
	
	public String getProcessoNumero() {
		return processoNumero;
	}
	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}
	public String getRelator() {
		return relator;
	}
	public void setRelator(String relator) {
		this.relator = relator;
	}
	public String getAdvogadoSolicitante() {
		return advogadoSolicitante;
	}
	public void setAdvogadoSolicitante(String advogadoSolicitante) {
		this.advogadoSolicitante = advogadoSolicitante;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public String getDataHoraSolicitacao() {
		return dataHoraSolicitacao;
	}
	public void setDataHoraSolicitacao(String dataHoraSolicitacao) {
		this.dataHoraSolicitacao = dataHoraSolicitacao;
	}
	public String getParte() {
		return parte;
	}
	public void setParte(String parte) {
		this.parte = StringUtils.defaultIfEmpty(parte, "");
	}
	public List<JulgamentoAdiadoDt> getAdvsPedidoSOdeferido() {
		return advsPedidoSOdeferido;
	}
	public void setAdvsPedidoSOdeferido(List<JulgamentoAdiadoDt> advsPedidoSOdeferido) {
		this.advsPedidoSOdeferido = advsPedidoSOdeferido;
	}
	
}
