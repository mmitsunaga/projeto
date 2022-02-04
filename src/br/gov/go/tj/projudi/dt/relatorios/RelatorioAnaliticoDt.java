package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.UsuarioDt;

/**
 * Objeto para auxiliar na criação dos relatórios analíticos
 */
public class RelatorioAnaliticoDt extends Dados {

	private static final long serialVersionUID = -4321430352557100077L;

	public static final int CodigoPermissaoAnaliticoProdutividade = 564;
	public static final int CodigoPermissaoAnaliticoProcesso = 569;
	public static final int CodigoPermissaoAnaliticoPendencia = 572;

	// variáveis das telas
	private String mes;
	private String ano;
	private String id_Comarca;
	private String comarca;
	private String id_Serventia;
	private String serventia;
	private String id_CargoTipo;
	private String cargoTipo;
	private String id_MovimentacaoTipo;
	private String movimentacaoTipo;
	private UsuarioDt usuario;
	private String id_ProcessoTipo;
	private String processoTipo;
	private String id_PendenciaTipo;
	private String pendenciaTipo;

	// variáveis dos relatórios
	private int anoRelatorio;
	private String mesRelatorio;
	private String usuarioRelatorio;
	private String grupoUsuarioRelatorio;
	private String idProcesso;
	private String numeroProcesso;
	private String itemEstatisticaRelatorio;
	private String processoRelatorio;
	private String pendenciaRelatorio;
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioAnaliticoDt() {
		limpar();
	}

	public void limpar() {
		ano = "";
		mes = "";
		id_Serventia = "";
		serventia = "";
		usuario = new UsuarioDt();
		comarca = "";
		id_Comarca = "";
		id_CargoTipo = "";
		cargoTipo = "";
		pendenciaRelatorio = "";
		usuarioRelatorio = "";
		idProcesso= "";
		numeroProcesso= "";
		id_MovimentacaoTipo = "";
		movimentacaoTipo = "";
		id_ProcessoTipo = "";
		processoTipo = "";
		id_PendenciaTipo = "";
		pendenciaTipo = "";
	}

	/**
	 * Método que limpa os campos de consulta da tela, mas não limpa o Tipo de
	 * Relatório que está sendo gerado.
	 */
	public void limparCamposConsulta() {
		mes = "";
		ano = "";
		id_Comarca = "";
		comarca = "";
		id_Serventia = "";
		serventia = "";
		id_CargoTipo = "";
		cargoTipo = "";
		usuario = new UsuarioDt();
		pendenciaRelatorio = "";
		usuarioRelatorio = "";
		idProcesso= "";
		numeroProcesso = "";
		id_MovimentacaoTipo = "";
		movimentacaoTipo = "";
		id_ProcessoTipo = "";
		processoTipo = "";
		id_PendenciaTipo = "";
		pendenciaTipo = "";
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

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public int getAnoRelatorio() {
		return anoRelatorio;
	}

	public void setAnoRelatorio(int anoRelatorio) {
		this.anoRelatorio = anoRelatorio;
	}

	public String getMesRelatorio() {
		return mesRelatorio;
	}

	public void setMesRelatorio(String mesRelatorio) {
		this.mesRelatorio = mesRelatorio;
	}

	public String getPendenciaRelatorio() {
		return pendenciaRelatorio;
	}

	public void setPendenciaRelatorio(String pendenciaRelatorio) {
		this.pendenciaRelatorio = pendenciaRelatorio;
	}

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

	public String getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getId_MovimentacaoTipo() {
		return id_MovimentacaoTipo;
	}

	public void setId_MovimentacaoTipo(String id_MovimentacaoTipo) {
		this.id_MovimentacaoTipo = id_MovimentacaoTipo;
	}

	public String getMovimentacaoTipo() {
		return movimentacaoTipo;
	}

	public void setMovimentacaoTipo(String movimentacaoTipo) {
		this.movimentacaoTipo = movimentacaoTipo;
	}

	public String getId_ProcessoTipo() {
		return id_ProcessoTipo;
	}

	public void setId_ProcessoTipo(String id_ProcessoTipo) {
		this.id_ProcessoTipo = id_ProcessoTipo;
	}

	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		this.processoTipo = processoTipo;
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
	

}