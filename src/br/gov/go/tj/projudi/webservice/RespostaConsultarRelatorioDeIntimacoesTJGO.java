package br.gov.go.tj.projudi.webservice;

import java.io.UnsupportedEncodingException;

import br.gov.go.tj.projudi.dt.relatorios.ResultadoRelatorioDt;
import br.gov.go.tj.utils.Funcoes;

public class RespostaConsultarRelatorioDeIntimacoesTJGO extends RespostaTJGO {

	private static final long serialVersionUID = -4387612647624522466L;

    private String nomeArquivo;
	private String conteudoArquivo;
	private long quantidadeDeRegistros;
	
	public RespostaConsultarRelatorioDeIntimacoesTJGO() {
		this.nomeArquivo = "";
		this.conteudoArquivo = "";
		this.setQuantidadeDeRegistros(0);
	}
	
	public RespostaConsultarRelatorioDeIntimacoesTJGO(ResultadoRelatorioDt resultado) throws UnsupportedEncodingException {
		this();
		setRelatorio(resultado);
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	public String getConteudoArquivo() {
		return conteudoArquivo;
	}
	public void setConteudoArquivo(String conteudoArquivo) {
		this.conteudoArquivo = conteudoArquivo;
	}
	
	public long getQuantidadeDeRegistros() {
		return quantidadeDeRegistros;
	}

	public void setQuantidadeDeRegistros(long quantidadeDeRegistros) {
		this.quantidadeDeRegistros = quantidadeDeRegistros;
	}

	public void setRelatorio(ResultadoRelatorioDt resultado) throws UnsupportedEncodingException {
		if (resultado == null) return ;
		if (resultado.getNomeArquivo() != null) this.nomeArquivo = resultado.getNomeArquivo();
		if (resultado.getConteudoArquivo() != null && resultado.getConteudoArquivo().length > 0) 
			this.conteudoArquivo = new String(resultado.getConteudoArquivo(), "Cp1252"); 
		if (resultado.getQuantidadeRegistros() > 0) this.setQuantidadeDeRegistros(resultado.getQuantidadeRegistros());
	}	
}
