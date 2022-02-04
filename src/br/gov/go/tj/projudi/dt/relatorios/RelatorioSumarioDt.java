package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.UsuarioDt;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioSumarioDt extends Dados {

	private static final long serialVersionUID = -4321430352557100077L;

	public static final int CodigoPermissaoSumarioProdutividade = 579;
	public static final int CodigoPermissaoSumarioProcesso = 581;
	public static final int CodigoPermissaoSumarioPendencia = 580;

	// variáveis das telas
	private String dataInicial;
	private String dataFinal;
	private String mesInicial;
	private String anoInicial;
	private String mesFinal;
	private String anoFinal;
	private String id_Serventia;
	private String serventia;
	private String id_CargoTipo;
	private String cargoTipo;
	private UsuarioDt usuario;
	private String agrupamentoRelatorio;
	private String opcaoRelatorio;
	//As duas próximas variáveis possuem nomenclatura genérica para
	//atender a várias telas de relatórios que geradas.
	private String itemEstatistica;
	private String idItemEstatistica;
	private String id_PendenciaTipo;
	private String pendenciaTipo;

	// variáveis dos relatórios
	private int ano;
	private String mes;
	private String usuarioRelatorio;
	private String grupoUsuarioRelatorio;
	private String comarca;
	private String id_Comarca;
	private String serventiaRelatorio;
	private String itemEstatisticaRelatorio;
	private String processoRelatorio;
	private String pendenciaRelatorio;
	private String tipoAcao;
	private String sistema;
	private long quantidade;
	
	public static final int COMARCA = 1;
	public static final int COMARCA_SERVENTIA = 2;
	public static final int COMARCA_SERVENTIA_USUARIO = 3;
	
	public static final int MES_FECHADO = 1;
	public static final int DATA_INICIAL_FINAL = 2;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioSumarioDt() {
		limpar();
	}

	public void limpar() {
		id_Serventia = "";
		serventia = "";
		usuario = new UsuarioDt();
		mesInicial = "";
		anoInicial = "";
		anoFinal = "";
		mesFinal = "";
		comarca = "";
		id_Comarca = "";
		serventiaRelatorio = "";
		itemEstatistica = "";
		idItemEstatistica = "";
		agrupamentoRelatorio = "";
		opcaoRelatorio = "1";
		id_CargoTipo = "";
		cargoTipo = "";
		pendenciaRelatorio = "";
		usuarioRelatorio = "";
		id_PendenciaTipo = "";
		pendenciaTipo = "";
		dataInicial = "";
		dataFinal = "";
		
	}

	/**
	 * Método que limpa os campos de consulta da tela, mas não limpa o Tipo de
	 * Relatório que está sendo gerado.
	 */
	public void limparCamposConsulta() {
		id_Serventia = "";
		serventia = "";
		usuario = new UsuarioDt();
		mesInicial = "";
		anoInicial = "";
		anoFinal = "";
		mesFinal = "";
		comarca = "";
		id_Comarca = "";
		agrupamentoRelatorio = "";
		opcaoRelatorio = "1";
		id_CargoTipo = "";
		cargoTipo = "";
		itemEstatistica = "";
		idItemEstatistica = "";
		pendenciaRelatorio = "";
		usuarioRelatorio = "";
		id_PendenciaTipo = "";
		pendenciaTipo = "";
		dataInicial = "";
		dataFinal = "";
	}

	public String getId_Serventia() {
		return id_Serventia;
	}

	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null)
			this.id_Serventia = id_Serventia;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (serventia != null)
			this.serventia = serventia;
	}

	public UsuarioDt getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDt usuario) {
		if (usuario != null)
			this.usuario = usuario;
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

	public String getServentiaRelatorio() {
		return serventiaRelatorio;
	}

	public void setServentiaRelatorio(String serventiaRelatorio) {
		this.serventiaRelatorio = serventiaRelatorio;
	}

	public String getPendenciaRelatorio() {
		return pendenciaRelatorio;
	}

	public void setPendenciaRelatorio(String pendenciaRelatorio) {
		this.pendenciaRelatorio = pendenciaRelatorio;
	}

	public String getTipoAcao() {
		return tipoAcao;
	}

	public void setTipoAcao(String tipoAcao) {
		this.tipoAcao = tipoAcao;
	}

	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}

//	public String getCodigoTipo() {
//		return codigoTipo;
//	}
//
//	public void setCodigoTipo(String codigoTipo) {
//		this.codigoTipo = codigoTipo;
//	}

	public String getUsuarioRelatorio() {
		return usuarioRelatorio;
	}

	public void setUsuarioRelatorio(String usuarioRelatorio) {
		this.usuarioRelatorio = usuarioRelatorio;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}
	
	public String getProcessoRelatorio() {
		return processoRelatorio;
	}

	public void setProcessoRelatorio(String processoRelatorio) {
		this.processoRelatorio = processoRelatorio;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public String getId() {
		return null;
	}

	public void setId(String id) {
		
	}
	
	public String getId_Comarca() {
		return id_Comarca;
	}

	public void setId_Comarca(String idComarca) {
		this.id_Comarca = idComarca;
	}
	
	public String getAgrupamentoRelatorio() {
		return agrupamentoRelatorio;
	}

	public void setAgrupamentoRelatorio(String agrupamentoRelatorio) {
		this.agrupamentoRelatorio = agrupamentoRelatorio;
	}

	public String getId_CargoTipo() {
		return id_CargoTipo;
	}

	public void setId_CargoTipo(String id_CargoTipo) {
		this.id_CargoTipo = id_CargoTipo;
	}

	public String getCargoTipo() {
		return cargoTipo;
	}

	public void setCargoTipo(String cargoTipo) {
		this.cargoTipo = cargoTipo;
	}

	public String getGrupoUsuarioRelatorio() {
		return grupoUsuarioRelatorio;
	}

	public void setGrupoUsuarioRelatorio(String grupoUsuarioRelatorio) {
		this.grupoUsuarioRelatorio = grupoUsuarioRelatorio;
	}

	public String getItemEstatisticaRelatorio() {
		return itemEstatisticaRelatorio;
	}

	public void setItemEstatisticaRelatorio(String itemEstatisticaRelatorio) {
		this.itemEstatisticaRelatorio = itemEstatisticaRelatorio;
	}

	public String getItemEstatistica() {
		return itemEstatistica;
	}

	public void setItemEstatistica(String itemEstatistica) {
		this.itemEstatistica = itemEstatistica;
	}

	public String getIdItemEstatistica() {
		return idItemEstatistica;
	}

	public void setIdItemEstatistica(String idItemEstatistica) {
		this.idItemEstatistica = idItemEstatistica;
	}

	public String getId_PendenciaTipo() {
		return id_PendenciaTipo;
	}

	public void setId_PendenciaTipo(String id_PendenciaTipo) {
		this.id_PendenciaTipo = id_PendenciaTipo;
	}

	public String getPendenciaTipo() {
		return pendenciaTipo;
	}

	public void setPendenciaTipo(String pendenciaTipo) {
		this.pendenciaTipo = pendenciaTipo;
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

	public String getOpcaoRelatorio() {
		return opcaoRelatorio;
	}

	public void setOpcaoRelatorio(String opcaoRelatorio) {
		this.opcaoRelatorio = opcaoRelatorio;
	}
	
}