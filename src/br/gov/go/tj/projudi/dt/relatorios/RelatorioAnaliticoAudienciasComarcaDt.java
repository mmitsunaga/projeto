package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.UsuarioDt;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioAnaliticoAudienciasComarcaDt extends Dados {

	private static final long serialVersionUID = 21459405251817542L;

	public static final int CodigoPermissaoSumarioAudiencia = 904;
	
	public static final int COMARCA = 1;
	public static final int COMARCA_SERVENTIA = 2;
	public static final int COMARCA_SERVENTIA_USUARIO = 3;

	private String dataInicial;
	private String dataFinal;
	private String qtdDesignadas;
	private String qtdRealizadas;
	private String qtdAcordos;
	private String valorAcordos;
	
	// variáveis das telas
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
	//As duas próximas variáveis possuem nomenclatura genérica para
	//atender a várias telas de relatórios que geradas.
	private String itemEstatistica;
	private String idItemEstatistica;
	private String id_PendenciaTipo;
	private String pendenciaTipo;

	// variáveis dos relatórios
//	private int ano;
//	private String mes;
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
	
	
	
	//TODO: HELLENO - Remover variáveis não utilizadas.
	String serv;
	String ano;
	String mes;
	
	String janDesignadas;
	String janRealizadas;
	String janAcordos;
	String janValorAcordos;
	
	String fevDesignadas;
	String fevRealizadas;
	String fevAcordos;
	String fevValorAcordos;
	
	
	String marDesignadas;
	String marRealizadas;
	String marAcordos;
	String marValorAcordos;
	
	String abrDesignadas;
	String abrRealizadas;
	String abrAcordos;
	String abrValorAcordos;
	
	
	String maiDesignadas;
	String maiRealizadas;
	String maiAcordos;
	String maiValorAcordos;
	
	String junDesignadas;
	String junRealizadas;
	String junAcordos;
	String junValorAcordos;
	
	String julDesignadas;
	String julRealizadas;
	String julAcordos;
	String julValorAcordos;
	
	String agoDesignadas;
	String agoRealizadas;
	String agoAcordos;
	String agoValorAcordos;
	
	String setDesignadas;
	String setRealizadas;
	String setAcordos;
	String setValorAcordos;
	
	String outDesignadas;
	String outRealizadas;
	String outAcordos;
	String outValorAcordos;
	
	String novDesignadas;
	String novRealizadas;
	String novAcordos;
	String novValorAcordos;
	
	String dezDesignadas;
	String dezRealizadas;
	String dezAcordos;
	String dezValorAcordos;
	
	
	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioAnaliticoAudienciasComarcaDt() {
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
		id_CargoTipo = "";
		cargoTipo = "";
		pendenciaRelatorio = "";
		usuarioRelatorio = "";
		id_PendenciaTipo = "";
		pendenciaTipo = "";
		dataInicial = "";
		dataFinal = "";
		
		//TODO: HELLENO - Remover variáveis não utilizadas.
		serv = "";
		ano = "";
		String mes;
		
		janDesignadas 	= "0";
		janRealizadas 	= "0";
		janAcordos 		= "0";
		janValorAcordos = "0";
		
		fevDesignadas 	= "0";
		fevRealizadas 	= "0";
		fevAcordos 		= "0";
		fevValorAcordos = "0";
		
		
		marDesignadas 	= "0";
		marRealizadas 	= "0";
		marAcordos 		= "0";
		marValorAcordos = "0";
		
		abrDesignadas 	= "0";
		abrRealizadas 	= "0";
		abrAcordos 		= "0";
		abrValorAcordos = "0";
		
		
		maiDesignadas 	= "0";
		maiRealizadas 	= "0";
		maiAcordos 		= "0";
		maiValorAcordos = "0";
		
		junDesignadas 	= "0";
		junRealizadas 	= "0";
		junAcordos 		= "0";
		junValorAcordos = "0";
		
		julDesignadas 	= "0";
		julRealizadas 	= "0";
		julAcordos 		= "0";
		julValorAcordos = "0";
		
		agoDesignadas 	= "0";
		agoRealizadas 	= "0";
		agoAcordos 		= "0";
		agoValorAcordos = "0";
		
		setDesignadas 	= "0";
		setRealizadas 	= "0";
		setAcordos 		= "0";
		setValorAcordos = "0";
		
		outDesignadas 	= "0";
		outRealizadas 	= "0";
		outAcordos 		= "0";
		outValorAcordos = "0";
		
		novDesignadas 	= "0";
		novRealizadas 	= "0";
		novAcordos 		= "0";
		novValorAcordos = "0";
		
		dezDesignadas 	= "0";
		dezRealizadas 	= "0";
		dezAcordos 		= "0";
		dezValorAcordos = "0";
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
	
	

	public String getjanDesignadas() {
		return janDesignadas;
	}


	public void setJanDesignadas(String janDesignadas) {
		this.janDesignadas = janDesignadas;
	}


	public String getjanRealizadas() {
		return janRealizadas;
	}


	public void setJanRealizadas(String janRealizadas) {
		this.janRealizadas = janRealizadas;
	}


	public String getjanAcordos() {
		return janAcordos;
	}


	public void setJanAcordos(String janAcordos) {
		this.janAcordos = janAcordos;
	}


	public String getjanValorAcordos() {
		return janValorAcordos;
	}


	public void setJanValorAcordos(String janValorAcordos) {
		this.janValorAcordos = janValorAcordos;
	}


	public String getFevDesignadas() {
		return fevDesignadas;
	}


	public void setFevDesignadas(String fevDesignadas) {
		this.fevDesignadas = fevDesignadas;
	}


	public String getFevRealizadas() {
		return fevRealizadas;
	}


	public void setFevRealizadas(String fevRealizadas) {
		this.fevRealizadas = fevRealizadas;
	}


	public String getFevAcordos() {
		return fevAcordos;
	}


	public void setFevAcordos(String fevAcordos) {
		this.fevAcordos = fevAcordos;
	}


	public String getFevValorAcordos() {
		return fevValorAcordos;
	}


	public void setFevValorAcordos(String fevValorAcordos) {
		this.fevValorAcordos = fevValorAcordos;
	}


	public String getMarDesignadas() {
		return marDesignadas;
	}


	public void setMarDesignadas(String marDesignadas) {
		this.marDesignadas = marDesignadas;
	}


	public String getMarRealizadas() {
		return marRealizadas;
	}


	public void setMarRealizadas(String marRealizadas) {
		this.marRealizadas = marRealizadas;
	}


	public String getMarAcordos() {
		return marAcordos;
	}


	public void setMarAcordos(String marAcordos) {
		this.marAcordos = marAcordos;
	}


	public String getMarValorAcordos() {
		return marValorAcordos;
	}


	public void setMarValorAcordos(String marValorAcordos) {
		this.marValorAcordos = marValorAcordos;
	}


	public String getAbrDesignadas() {
		return abrDesignadas;
	}


	public void setAbrDesignadas(String abrDesignadas) {
		this.abrDesignadas = abrDesignadas;
	}


	public String getAbrRealizadas() {
		return abrRealizadas;
	}


	public void setAbrRealizadas(String abrRealizadas) {
		this.abrRealizadas = abrRealizadas;
	}


	public String getAbrAcordos() {
		return abrAcordos;
	}


	public void setAbrAcordos(String abrAcordos) {
		this.abrAcordos = abrAcordos;
	}


	public String getAbrValorAcordos() {
		return abrValorAcordos;
	}


	public void setAbrValorAcordos(String abrValorAcordos) {
		this.abrValorAcordos = abrValorAcordos;
	}


	public String getMaiDesignadas() {
		return maiDesignadas;
	}


	public void setMaiDesignadas(String maiDesignadas) {
		this.maiDesignadas = maiDesignadas;
	}


	public String getMaiRealizadas() {
		return maiRealizadas;
	}


	public void setMaiRealizadas(String maiRealizadas) {
		this.maiRealizadas = maiRealizadas;
	}


	public String getMaiAcordos() {
		return maiAcordos;
	}


	public void setMaiAcordos(String maiAcordos) {
		this.maiAcordos = maiAcordos;
	}


	public String getMaiValorAcordos() {
		return maiValorAcordos;
	}


	public void setMaiValorAcordos(String maiValorAcordos) {
		this.maiValorAcordos = maiValorAcordos;
	}


	public String getJunDesignadas() {
		return junDesignadas;
	}


	public void setJunDesignadas(String junDesignadas) {
		this.junDesignadas = junDesignadas;
	}


	public String getJunRealizadas() {
		return junRealizadas;
	}


	public void setJunRealizadas(String junRealizadas) {
		this.junRealizadas = junRealizadas;
	}


	public String getJunAcordos() {
		return junAcordos;
	}


	public void setJunAcordos(String junAcordos) {
		this.junAcordos = junAcordos;
	}


	public String getJunValorAcordos() {
		return junValorAcordos;
	}


	public void setJunValorAcordos(String junValorAcordos) {
		this.junValorAcordos = junValorAcordos;
	}


	public String getJulDesignadas() {
		return julDesignadas;
	}


	public void setJulDesignadas(String julDesignadas) {
		this.julDesignadas = julDesignadas;
	}


	public String getJulRealizadas() {
		return julRealizadas;
	}


	public void setJulRealizadas(String julRealizadas) {
		this.julRealizadas = julRealizadas;
	}


	public String getJulAcordos() {
		return julAcordos;
	}


	public void setJulAcordos(String julAcordos) {
		this.julAcordos = julAcordos;
	}


	public String getJulValorAcordos() {
		return julValorAcordos;
	}


	public void setJulValorAcordos(String julValorAcordos) {
		this.julValorAcordos = julValorAcordos;
	}


	public String getAgoDesignadas() {
		return agoDesignadas;
	}


	public void setAgoDesignadas(String agoDesignadas) {
		this.agoDesignadas = agoDesignadas;
	}


	public String getAgoRealizadas() {
		return agoRealizadas;
	}


	public void setAgoRealizadas(String agoRealizadas) {
		this.agoRealizadas = agoRealizadas;
	}


	public String getAgoAcordos() {
		return agoAcordos;
	}


	public void setAgoAcordos(String agoAcordos) {
		this.agoAcordos = agoAcordos;
	}


	public String getAgoValorAcordos() {
		return agoValorAcordos;
	}


	public void setAgoValorAcordos(String agoValorAcordos) {
		this.agoValorAcordos = agoValorAcordos;
	}

	
	public String getSetDesignadas() {
		return setDesignadas;
	}


	public void setSetDesignadas(String setDesignadas) {
		this.setDesignadas = setDesignadas;
	}


	public String getSetRealizadas() {
		return setRealizadas;
	}


	public void setSetRealizadas(String setRealizadas) {
		this.setRealizadas = setRealizadas;
	}


	public String getSetAcordos() {
		return setAcordos;
	}


	public void setSetAcordos(String setAcordos) {
		this.setAcordos = setAcordos;
	}


	public String getSetValorAcordos() {
		return setValorAcordos;
	}


	public void setSetValorAcordos(String setValorAcordos) {
		this.setValorAcordos = setValorAcordos;
	}


	public String getOutDesignadas() {
		return outDesignadas;
	}


	public void setOutDesignadas(String outDesignadas) {
		this.outDesignadas = outDesignadas;
	}


	public String getOutRealizadas() {
		return outRealizadas;
	}


	public void setOutRealizadas(String outRealizadas) {
		this.outRealizadas = outRealizadas;
	}


	public String getOutAcordos() {
		return outAcordos;
	}


	public void setOutAcordos(String outAcordos) {
		this.outAcordos = outAcordos;
	}


	public String getOutValorAcordos() {
		return outValorAcordos;
	}


	public void setOutValorAcordos(String outValorAcordos) {
		this.outValorAcordos = outValorAcordos;
	}


	public String getNovDesignadas() {
		return novDesignadas;
	}


	public void setNovDesignadas(String novDesignadas) {
		this.novDesignadas = novDesignadas;
	}


	public String getNovRealizadas() {
		return novRealizadas;
	}


	public void setNovRealizadas(String novRealizadas) {
		this.novRealizadas = novRealizadas;
	}


	public String getNovAcordos() {
		return novAcordos;
	}


	public void setNovAcordos(String novAcordos) {
		this.novAcordos = novAcordos;
	}


	public String getNovValorAcordos() {
		return novValorAcordos;
	}


	public void setNovValorAcordos(String novValorAcordos) {
		this.novValorAcordos = novValorAcordos;
	}


	public String getDezDesignadas() {
		return dezDesignadas;
	}


	public void setDezDesignadas(String dezDesignadas) {
		this.dezDesignadas = dezDesignadas;
	}


	public String getDezRealizadas() {
		return dezRealizadas;
	}


	public void setDezRealizadas(String dezRealizadas) {
		this.dezRealizadas = dezRealizadas;
	}


	public String getDezAcordos() {
		return dezAcordos;
	}


	public void setDezAcordos(String dezAcordos) {
		this.dezAcordos = dezAcordos;
	}


	public String getDezValorAcordos() {
		return dezValorAcordos;
	}


	public void setDezValorAcordos(String dezValorAcordos) {
		this.dezValorAcordos = dezValorAcordos;
	}


	public String getValorAcordos() {
		return valorAcordos;
	}

	public void setValorAcordos(String valorAcordos) {
		this.valorAcordos = valorAcordos;
	}

	public String getQtdDesignadas() {
		return qtdDesignadas;
	}

	public void setQtdDesignadas(String qtdDesignadas) {
		this.qtdDesignadas = qtdDesignadas;
	}

	public String getQtdRealizadas() {
		return qtdRealizadas;
	}

	public void setQtdRealizadas(String qtdRealizadas) {
		this.qtdRealizadas = qtdRealizadas;
	}

	public String getQtdAcordos() {
		return qtdAcordos;
	}

	public void setQtdAcordos(String qtdAcordos) {
		this.qtdAcordos = qtdAcordos;
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
	
	public String getId_Serventia() {
		return id_Serventia;
	}

	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null)
			this.id_Serventia = id_Serventia;
	}

	public String getserventia() {
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

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
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
	
}