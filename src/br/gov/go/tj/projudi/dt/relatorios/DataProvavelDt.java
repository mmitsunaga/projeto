package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

public class DataProvavelDt  extends Dados {

	private static final long serialVersionUID = -546780101453458701L;
	public static final int CodigoPermissao = 579;
	
	private String idProcesso;
	private boolean temProcessoApenso;
	private String idProcessoPrincipal;
	private String numeroProcesso;
	private String dataInicialPeriodo;
	private String dataFinalPeriodo;
	private String dataProvavelProgressao;
	private String dataProvavelLivramento;
	private String dataValidadeMandadoPrisao;	
	private String dataCalculo;
	private String informacaoSentenciado;
	private String tipoConsulta;
	private String dataProvavelTerminoPena;
	private String processoTipoCodigo;
	private String processoTipo;
	private String idUsuarioServentia;
	private String nomeUsuario;
	private String dataFuga;
	private String idCalculo;
	private byte[] relatorioByte; //ultimo atestado de pena a cumprir salvo pelo usuário (.pdf)
	private String dataHomologacao;
	private String regime;
	private String descricaoModalidade;
	
	private String horaRestantePSC;
	private String dataTerminoLFS;
	private String dataTerminoITD;
	private String valorDevidoPEC;
	private String qtdDevidaPCB;
	private String terminoSURSIS;

	public DataProvavelDt() {
		idProcesso = "";
		temProcessoApenso = false;
		idProcessoPrincipal = "";
		numeroProcesso = "";
		dataInicialPeriodo = "";
		dataFinalPeriodo = "";
		dataProvavelProgressao = "";
		dataProvavelLivramento = "";
		dataValidadeMandadoPrisao = "";
		dataCalculo = "";
		informacaoSentenciado = "";
		tipoConsulta = "";
		dataProvavelTerminoPena = "";
		processoTipoCodigo = "";
		processoTipo = "";
		idUsuarioServentia = "";
		nomeUsuario = "";
		dataFuga = "";
		relatorioByte = null;
		idCalculo = "";
		dataHomologacao = "";
		regime = "";
		descricaoModalidade = "";
		
		horaRestantePSC = "";
		dataTerminoITD = "";
		dataTerminoLFS = "";
		valorDevidoPEC = "";
		qtdDevidaPCB = "";
		terminoSURSIS = "";
	}
	
	public String getIdProcesso() {
		return idProcesso;
	}
	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}
	
	public boolean isTemProcessoApenso() {
		return temProcessoApenso;
	}

	public void setTemProcessoApenso(boolean temProcessoApenso) {
		this.temProcessoApenso = temProcessoApenso;
	}

	public String getIdProcessoPrincipal() {
		return idProcessoPrincipal;
	}

	public void setIdProcessoPrincipal(String idProcessoPrincipal) {
		this.idProcessoPrincipal = idProcessoPrincipal;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}
	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	
	public String getDataInicialPeriodo() {
		return dataInicialPeriodo;
	}
	public void setDataInicialPeriodo(String dataInicialPeriodo) {
		this.dataInicialPeriodo = dataInicialPeriodo;
	}
	
	public String getDataFinalPeriodo() {
		return dataFinalPeriodo;
	}
	public void setDataFinalPeriodo(String dataFinalPeriodo) {
		this.dataFinalPeriodo = dataFinalPeriodo;
	}
	
	public String getDataProvavelProgressao() {
		return dataProvavelProgressao;
	}
	public void setDataProvavelProgressao(String dataProvavelProgressao) {
		this.dataProvavelProgressao = dataProvavelProgressao;
	}
	
	public String getDataProvavelLivramento() {
		return dataProvavelLivramento;
	}
	public void setDataProvavelLivramento(String dataProvavelLivramento) {
		this.dataProvavelLivramento = dataProvavelLivramento;
	}
	
	public String getDataValidadeMandadoPrisao() {
		return dataValidadeMandadoPrisao;
	}
	public void setDataValidadeMandadoPrisao(String dataValidadeMandadoPrisao) {
		this.dataValidadeMandadoPrisao = dataValidadeMandadoPrisao;
	}
	
	public String getInformacaoSentenciado() {
		return informacaoSentenciado;
	}
	public void setInformacaoSentenciado(String informacaoSentenciado) {
		this.informacaoSentenciado = informacaoSentenciado;
	}

	public String getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public String getDataCalculo() {
		return dataCalculo;
	}

	public void setDataCalculo(String dataCalculo) {
		this.dataCalculo = dataCalculo;
	}

	public String getDataProvavelTerminoPena() {
		return dataProvavelTerminoPena;
	}

	public void setDataProvavelTerminoPena(String dataProvavelTerminoPena) {
		this.dataProvavelTerminoPena = dataProvavelTerminoPena;
	}

	public String getProcessoTipoCodigo() {
		return processoTipoCodigo;
	}

	public void setProcessoTipoCodigo(String ProcessoTipoCodigo) {
		this.processoTipoCodigo = ProcessoTipoCodigo;
	}

	public String getProcessoTipo() {
		return processoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		this.processoTipo = processoTipo;
	}

	public String getIdUsuarioServentia() {
		return idUsuarioServentia;
	}

	public void setIdUsuarioServentia(String idUsuarioServentia) {
		this.idUsuarioServentia = idUsuarioServentia;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}

	public String getDataFuga() {
		return dataFuga;
	}

	public void setDataFuga(String dataFuga) {
		this.dataFuga = dataFuga;
	}

	public byte[] getRelatorioByte() {
		return this.relatorioByte;	
	}

	public void setRelatorioByte(byte[] bytes) {
		this.relatorioByte = bytes;
	}

	public String getIdCalculo() {
		return idCalculo;
	}

	public void setIdCalculo(String idCalculo) {
		this.idCalculo = idCalculo;
	}

	public String getDataHomologacao() {
		return dataHomologacao;
	}

	public void setDataHomologacao(String dataHomologacao) {
		this.dataHomologacao = dataHomologacao;
	}

	public String getRegime() {
		return regime;
	}

	public void setRegime(String regime) {
		this.regime = regime;
	}

	public String getDescricaoModalidade() {
		return descricaoModalidade;
	}

	public void setDescricaoModalidade(String descricaoModalidade) {
		if (descricaoModalidade != null && !descricaoModalidade.equals("")) this.descricaoModalidade = descricaoModalidade;
	}
	
	public String getHoraRestantePSC() {
		return horaRestantePSC;
	}

	public void setHoraRestantePSC(String horaRestantePSC) {
		this.horaRestantePSC = horaRestantePSC;
	}

	public String getDataTerminoLFS() {
		return dataTerminoLFS;
	}

	public void setDataTerminoLFS(String dataTerminoLFS) {
		this.dataTerminoLFS = dataTerminoLFS;
	}

	public String getDataTerminoITD() {
		return dataTerminoITD;
	}

	public void setDataTerminoITD(String dataTerminoITD) {
		this.dataTerminoITD = dataTerminoITD;
	}

	public String getValorDevidoPEC() {
		return valorDevidoPEC;
	}

	public void setValorDevidoPEC(String valorDevidoPEC) {
		this.valorDevidoPEC = valorDevidoPEC;
	}

	public String getQtdDevidaPCB() {
		return qtdDevidaPCB;
	}

	public void setQtdDevidaPCB(String qtdDevidaPCB) {
		this.qtdDevidaPCB = qtdDevidaPCB;
	}

	public String getTerminoSURSIS() {
		return terminoSURSIS;
	}

	public void setTerminoSURSIS(String terminoSURSIS) {
		this.terminoSURSIS = terminoSURSIS;
	}

	public void copiar(DataProvavelDt objeto){
		idCalculo = objeto.getId();
		idProcesso = objeto.getIdProcesso();
		dataCalculo = objeto.getDataCalculo();
		dataProvavelProgressao = objeto.getDataProvavelProgressao();
		dataProvavelLivramento = objeto.getDataProvavelLivramento();
		dataProvavelTerminoPena = objeto.getDataProvavelTerminoPena();
		dataValidadeMandadoPrisao = objeto.getDataValidadeMandadoPrisao();
		idUsuarioServentia = objeto.getIdUsuarioServentia();
		relatorioByte = objeto.getRelatorioByte();
		informacaoSentenciado = objeto.getInformacaoSentenciado();
		dataHomologacao = objeto.getDataHomologacao();
		horaRestantePSC = objeto.getHoraRestantePSC();
		dataTerminoITD = objeto.getDataTerminoITD();
		dataTerminoLFS = objeto.getDataTerminoLFS();
		terminoSURSIS = objeto.getTerminoSURSIS();
		valorDevidoPEC = objeto.getValorDevidoPEC();
		qtdDevidaPCB = objeto.getQtdDevidaPCB();
	}
	
	@Override
	public String getId() {
		return idCalculo;
	}

	@Override
	public void setId(String id) {
		idCalculo = id;
	}
}
