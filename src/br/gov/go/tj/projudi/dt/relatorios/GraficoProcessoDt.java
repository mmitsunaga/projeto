package br.gov.go.tj.projudi.dt.relatorios;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.EstatisticaProdutividadeItemDt;

/**
 * Objeto para auxiliar na criação do gráfico de processos
 */
public class GraficoProcessoDt extends Dados {

    private static final long serialVersionUID = -642322353301860473L;
    private String mesInicial;
	private String anoInicial;
	private String mesFinal;
	private String anoFinal;
	private String id_Serventia;
	private String serventia;
	private String id_Comarca;
	private String comarca;
	private String id_EstatisticaProdutividadeItem;
	private String estatisticaProdutividadeItem;
	private List listaEstatisticaProdutividadeItem;
	private String mes;
	private int ano;
	private long quantidade;
	public static final String RECEBIDO = "50000";
	public static final String ARQUIVADO = "50001";
	public static final String ATIVO = "50002";

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public GraficoProcessoDt() {
		limpar();
	}

	public void limpar() {
		setMesInicial("");
		setAnoInicial("");
		setMesFinal("");
		setAnoFinal("");
		setId_Serventia("");
		setServentia("");
		setId_Comarca("");
		setComarca("");
		setAno(0);
		setMes("");
		setId_EstatisticaProdutividadeItem("");
		setEstatisticaProdutividadeItem("");
		setListaEstatisticaProdutividadeItem(new ArrayList());
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

	public String getId_Serventia() {
		return id_Serventia;
	}

	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null) this.id_Serventia = id_Serventia;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (serventia != null) this.serventia = serventia;
	}

	public String getId_Comarca() {
		return id_Comarca;
	}

	public void setId_Comarca(String idComarca) {
		this.id_Comarca = idComarca;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getId_EstatisticaProdutividadeItem() {
		return id_EstatisticaProdutividadeItem;
	}

	public void setId_EstatisticaProdutividadeItem(String idEstatisticaProdutividadeItem) {
		id_EstatisticaProdutividadeItem = idEstatisticaProdutividadeItem;
	}

	public String getEstatisticaProdutividadeItem() {
		return estatisticaProdutividadeItem;
	}

	public void setEstatisticaProdutividadeItem(String estatisticaProdutividadeItem) {
		this.estatisticaProdutividadeItem = estatisticaProdutividadeItem;
	}

	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}

	public List getListaEstatisticaProdutividadeItem() {
		return listaEstatisticaProdutividadeItem;
	}

	public void setListaEstatisticaProdutividadeItem(List listaEstatisticaProdutividadeItem) {
		this.listaEstatisticaProdutividadeItem = listaEstatisticaProdutividadeItem;
	}
	
	public boolean listaVazia() {
		return this.listaEstatisticaProdutividadeItem.isEmpty();
	}
	
	public void addListaEstatisticaProdutividadeItem(EstatisticaProdutividadeItemDt dt) {
		if (this.listaEstatisticaProdutividadeItem == null) this.listaEstatisticaProdutividadeItem = new ArrayList();
		this.listaEstatisticaProdutividadeItem.add(dt);
	}

	public String getId() {
		return null;
	}

	public void setId(String id) {

	}
}
