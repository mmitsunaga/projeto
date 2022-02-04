package br.gov.go.tj.projudi.dt.relatorios;

import java.io.Serializable;

public class EstatisticaConciliadorCejuscDt implements Serializable {

	private static final long serialVersionUID = 1667778145079939235L;
	
	String cpf;
	String nome;
	String pis;
	String agencia;
	String numeroConta;
	String dadosBancarios;
	String qtdConciliacaoCejusc;
	String qtdMediacaoCejusc;
	String qtdTotal;
	String codigoBanco;
	
	public String getcpf() {
		return cpf;
	}
	public void setcpf(String cpf) {
		this.cpf = cpf;
	}
	public String getnome() {
		return nome;
	}
	public void setnome(String nome) {
		this.nome = nome;
	}
	public String getdadosBancarios() {
		return dadosBancarios;
	}
	public void setdadosBancarios(String dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}
	public String getqtdConciliacaoCejusc() {
		return qtdConciliacaoCejusc;
	}
	public void setqtdConciliacaoCejusc(String qtdConciliacaoCejusc) {
		this.qtdConciliacaoCejusc = qtdConciliacaoCejusc;
	}
	public String getqtdMediacaoCejusc() {
		return qtdMediacaoCejusc;
	}
	public void setqtdMediacaoCejusc(String qtdMediacaoCejusc) {
		this.qtdMediacaoCejusc = qtdMediacaoCejusc;
	}
	public String getqtdTotal() {
		return qtdTotal;
	}
	public void setqtdTotal(String qtdTotal) {
		this.qtdTotal = qtdTotal;
	}
	public String getpis() {
		return pis;
	}
	public void setpis(String pis) {
		this.pis = pis;
	}
	public String getagencia() {
		return agencia;
	}
	public void setagencia(String agencia) {
		this.agencia = agencia;
	}
	public String getnumeroConta() {
		return numeroConta;
	}
	public void setnumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}
	public String getcodigoBanco() {
		return codigoBanco;
	}
	public void setcodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

}
