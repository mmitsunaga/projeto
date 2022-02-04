package br.gov.go.tj.projudi.dt.relatorios;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioSituacaoGabineteDt extends Dados {

	private static final long serialVersionUID = -7713458163806937661L;

	public static final int CodigoPermissaoSituacaoGabinete = 703; //669;
	
	private String qtdeProcessosDistribuidos;
	private String qtdeConclusosFinalizados;
	private String qtdeConclusosPendentes;
	private String qtdeRevisao;
	private String idServentiaCargoResponsavel;
	private String idUsuarioServentiaResponsavel;
	private String nomeResponsavel;
	private String mesInicial;
	private String anoInicial;
	private String mesFinal;
	private String anoFinal;
	private String idServentia;
	private String serventia;
	private List listaMovimentacaoTipoClasse;
	private String IdMovimentacaoTipoClasse;
	private String movimentacaoTipoClasse;
	
	
	//variável genérica que será usada para o segundo nível de consulta, onde serão apresentados a lista de itens consultados
	private List listaItens;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioSituacaoGabineteDt() {
		limpar();
	}

	public void limpar() {
		qtdeProcessosDistribuidos = "";
		qtdeConclusosFinalizados = "";
		qtdeConclusosPendentes = "";
		qtdeRevisao = "";
		idServentiaCargoResponsavel = "";
		idUsuarioServentiaResponsavel = "";
		nomeResponsavel = "";
		mesInicial = "";
		anoInicial = "";
		mesFinal = "";
		anoFinal = "";
		idServentia = "";
		serventia = "";
		listaItens = new ArrayList();
		listaMovimentacaoTipoClasse = new ArrayList();
		IdMovimentacaoTipoClasse = "";
		movimentacaoTipoClasse = "";
	}

	/**
	 * Método que limpa os campos de consulta da tela.
	 */
	public void limparCamposConsulta() {
		qtdeProcessosDistribuidos = "";
		qtdeConclusosFinalizados = "";
		qtdeConclusosPendentes = "";
		qtdeRevisao = "";
		idServentiaCargoResponsavel = "";
		idUsuarioServentiaResponsavel = "";
		nomeResponsavel = "";
		mesInicial = "";
		anoInicial = "";
		mesFinal = "";
		anoFinal = "";
		idServentia = "";
		serventia = "";
		listaItens = new ArrayList();
		listaMovimentacaoTipoClasse = new ArrayList();
		IdMovimentacaoTipoClasse = "";
		movimentacaoTipoClasse = "";
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getMesInicial() {
		return mesInicial;
	}

	public void setMesInicial(String mesInicial) {
		this.mesInicial = mesInicial;
	}

	public String getAnoInicial() {
		return anoInicial;
	}

	public void setAnoInicial(String anoInicial) {
		this.anoInicial = anoInicial;
	}

	public String getMesFinal() {
		return mesFinal;
	}

	public void setMesFinal(String mesFinal) {
		this.mesFinal = mesFinal;
	}

	public String getAnoFinal() {
		return anoFinal;
	}

	public void setAnoFinal(String anoFinal) {
		this.anoFinal = anoFinal;
	}

	public String getIdServentia() {
		return idServentia;
	}

	public void setIdServentia(String idServentia) {
		this.idServentia = idServentia;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

	public String getQtdeProcessosDistribuidos() {
		return qtdeProcessosDistribuidos;
	}

	public void setQtdeProcessosDistribuidos(String qtdeProcessosDistribuidos) {
		this.qtdeProcessosDistribuidos = qtdeProcessosDistribuidos;
	}

	public String getQtdeConclusosFinalizados() {
		return qtdeConclusosFinalizados;
	}

	public void setQtdeConclusosFinalizados(String qtdeConclusosFinalizados) {
		this.qtdeConclusosFinalizados = qtdeConclusosFinalizados;
	}

	public String getQtdeConclusosPendentes() {
		return qtdeConclusosPendentes;
	}

	public void setQtdeConclusosPendentes(String qtdeConclusosPendentes) {
		this.qtdeConclusosPendentes = qtdeConclusosPendentes;
	}

	public String getQtdeRevisao() {
		return qtdeRevisao;
	}

	public void setQtdeRevisao(String qtdeRevisao) {
		this.qtdeRevisao = qtdeRevisao;
	}

	public List getListaItens() {
		return listaItens;
	}

	public void setListaItens(List listaItens) {
		this.listaItens = listaItens;
	}

	public List getListaMovimentacaoTipoClasse() {
		return listaMovimentacaoTipoClasse;
	}

	public void setListaMovimentacaoTipoClasse(List listaMovimentacaoTipoClasse) {
		this.listaMovimentacaoTipoClasse = listaMovimentacaoTipoClasse;
	}

	public String getIdMovimentacaoTipoClasse() {
		return IdMovimentacaoTipoClasse;
	}

	public void setIdMovimentacaoTipoClasse(String idMovimentacaoTipoClasse) {
		IdMovimentacaoTipoClasse = idMovimentacaoTipoClasse;
	}

	public String getMovimentacaoTipoClasse() {
		return movimentacaoTipoClasse;
	}

	public void setMovimentacaoTipoClasse(String movimentacaoTipoClasse) {
		this.movimentacaoTipoClasse = movimentacaoTipoClasse;
	}

	public String getIdServentiaCargoResponsavel() {
		return idServentiaCargoResponsavel;
	}

	public void setIdServentiaCargoResponsavel(String idServentiaCargoResponsavel) {
		this.idServentiaCargoResponsavel = idServentiaCargoResponsavel;
	}

	public String getIdUsuarioServentiaResponsavel() {
		return idUsuarioServentiaResponsavel;
	}

	public void setIdUsuarioServentiaResponsavel(String idUsuarioServentiaResponsavel) {
		this.idUsuarioServentiaResponsavel = idUsuarioServentiaResponsavel;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}	
	
}