package br.gov.go.tj.projudi.dt.relatorios;

import java.io.Serializable;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.ProcessoDt;

/**
 * @author mmitsunaga
 *
 */
public class RelatorioPostagemECartasDt extends Dados implements Serializable {

	private static final long serialVersionUID = -2892226961579756191L;
	
	public static final int CodigoPermissao = 935;
	
	private String idComarca;
	
	private String comarca;
	
	private String idServentia;
	
	private String serventia;
	
	private String codigoRastreamento;
	
	private String dataPostagem;
	
	private ProcessoDt processoDt;
	
	private String cep;
	
	private String maoPropria;
	
	private String dataInicial;
	
	private String dataFinal;
	
	private String nomeParte;
	
	private String qtdeFolhas;

	public RelatorioPostagemECartasDt() {
		limpar();
	}
	
	
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

	public String getProcesso() {
		return processoDt.getProcessoNumeroCompleto();
	}

	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getMaoPropria() {
		return maoPropria;
	}
	
	public String getMaoPropriaTela(){
		return maoPropria.equals("1") ? "Sim" : "Não";
	}

	public void setMaoPropria(String maoPropria) {
		this.maoPropria = maoPropria;
	}

	public String getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
	
	public String getNomeParte() {
		return nomeParte;
	}
	
	public void setNomeParte(String nomeParte) {
		this.nomeParte = nomeParte;
	}
	
	public String getQtdeFolhas() {
		return qtdeFolhas;
	}

	public void setQtdeFolhas(String qtdeFolhas) {
		this.qtdeFolhas = qtdeFolhas;
	}

	public void limpar(){
		this.idComarca = "";
		this.comarca = "";
		this.idServentia = "";
		this.serventia = "";
		this.dataInicial = "";
		this.dataFinal = "";
		this.codigoRastreamento = "";
		this.dataPostagem = "";
		this.maoPropria = "";
		this.cep = "";
		this.nomeParte = "";
		this.qtdeFolhas = "1";
	}

	@Override
	public void setId(String id) {}

	@Override
	public String getId() {		
		return null;
	}
	
}
