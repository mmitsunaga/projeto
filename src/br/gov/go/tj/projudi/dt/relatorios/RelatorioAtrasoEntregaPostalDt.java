package br.gov.go.tj.projudi.dt.relatorios;

import java.io.Serializable;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * Encapsula os dados para a Consulta de Atraso de Entrega de Postagem do e-cartas
 * @author mmitsunaga
 *
 */
public class RelatorioAtrasoEntregaPostalDt extends Dados implements Serializable {
	
	private static final long serialVersionUID = 6148809523876645418L;
	
	public static final int CodigoPermissao = 419;
	
	private String idComarca;
	
	private String comarca;
	
	private String idServentia;
	
	private String serventia;
	
	private String codigoRastreamento;
	
	private String dataPostagem;
	
	private String dataEntrega;
	
	private ProcessoDt processoDt;
	
	private String maoPropria;
	
	private String diasEspera;
	
	private String nomeParte;
	
	private String idPendTipo;
	
	private String pendTipo;
	
	
	public String getIdComarca() {
		return idComarca;
	}

	public void setIdComarca(String idComarca) {
		this.idComarca = idComarca;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}

	public String getIdServentia() {
		return idServentia;
	}

	public void setIdServentia(String idServentia) {
		this.idServentia = idServentia;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getCodigoRastreamento() {
		return codigoRastreamento;
	}

	public void setCodigoRastreamento(String codigoRastreamento) {
		this.codigoRastreamento = codigoRastreamento;
	}

	public String getDataPostagem() {
		return dataPostagem;
	}

	public void setDataPostagem(String dataPostagem) {
		this.dataPostagem = dataPostagem;
	}

	public String getDataEntrega() {
		return dataEntrega;
	}

	public void setDataEntrega(String dataEntrega) {
		this.dataEntrega = dataEntrega;
	}

	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public String getMaoPropria() {
		return maoPropria;
	}

	public void setMaoPropria(String maoPropria) {
		this.maoPropria = maoPropria;
	}

	public String getDiasEspera() {
		return diasEspera;
	}

	public void setDiasEspera(String diasEspera) {
		this.diasEspera = diasEspera;
	}
	
	public String getNomeParte() {
		return ValidacaoUtil.isNaoVazio(nomeParte) ? nomeParte.trim().toUpperCase() : nomeParte;
	}

	public void setNomeParte(String nomeParte) {
		this.nomeParte = nomeParte;
	}

	public String getIdPendTipo() {
		return idPendTipo;
	}

	public void setIdPendTipo(String idPendTipo) {
		this.idPendTipo = idPendTipo;
	}

	public String getPendTipo() {
		return pendTipo;
	}

	public void setPendTipo(String pendTipo) {
		this.pendTipo = pendTipo;
	}

	public RelatorioAtrasoEntregaPostalDt(){
		limpar();
	}
	
	public RelatorioAtrasoEntregaPostalDt(String[] cols){
		limpar();				
		setCodigoRastreamento(cols[1]);
		setDataPostagem(cols[2]);
		setDataEntrega(cols[3]);		
		setDiasEspera(cols[4]);
		setMaoPropria(cols[5]);		
		processoDt = new ProcessoDt();
		processoDt.setId(cols[6]);
		processoDt.setProcessoNumero(cols[7]);
		processoDt.setDigitoVerificador(cols[8]);
		processoDt.setAno(cols[9]);
		processoDt.setForumCodigo(cols[10]);
		setNomeParte(cols[11]);
		setIdPendTipo(cols[12]);
		setPendTipo(cols[13]);		
		setIdServentia(cols[14]);
		setServentia(cols[15]);		
	}
	
	public void limpar(){
		this.idComarca = "";
		this.comarca = "";
		this.idServentia = "";
		this.serventia = "";		
		this.codigoRastreamento = "";
		this.dataPostagem = "";
		this.dataEntrega = "";			
		this.maoPropria = "";
		this.diasEspera = "10";
		this.nomeParte = "";
		this.idPendTipo = "";
		this.pendTipo = "";
	}
	
	@Override
	public void setId(String id) {}

	@Override
	public String getId() {
		return null;
	}

}
