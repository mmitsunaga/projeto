package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

/**
 * Dt criado para manipular os dados necessários na análise ou pré-análise de conclusões
 * @author msapaula
 *
 */
public class AnaliseConclusaoDt extends AnalisePendenciaDt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6110426964489069033L;

	public static final int CodigoPermissao = 271;

	//Varíaveis para auxiliar na análise de conclusões
	private String id_Classificador;
	private String classificador;

	private List listaPendenciasGerar; //Pendências que serão geradas
	private List listaPendenciaTipos; //Tipos de pendências que poderão ser geradas

	//Variáveis para guardar o arquivo de configuração da pré-analise
	private PendenciaArquivoDt arquivoConfiguracaoPreAnalise;

	private String passo3;
	
	private boolean pendenteAssinatura;
	
	private List listaTiposMovimentacaoConfigurado;
	
	private boolean arquivoTipoSomenteLeitura;
	
	private boolean arquivoTipoEmentaSomenteLeitura;
	
	private String processoTipoSessao;
	private String id_ProcessoTipoSessao;
	
	private String audienciaStatus;
	private String audienciaStatusCodigo;
	
	private boolean naoGerarVeficarProcesso = false;
	
	private String pedidoAssistencia;
	
	private boolean visualizarPedidoAssistencia;
	private boolean iniciarVotacao = false;
	private boolean adiarVotacao = false;
	private boolean aguardarIniciarVotacao = false;
	
	private boolean virtual; // jvosantos - 14/10/2019 13:22 - Flag para verificar se é virtual

	public AnaliseConclusaoDt() {
		limpar();
	}

	public void limpar() {
		//fluxo = -10; //valor para não interferir nos valores que Configuração pode assumir
		id_Classificador = "";
		classificador = "";
		listaPendenciasGerar = null;
		listaPendenciaTipos = null;
		passo3 = "";
		pendenteAssinatura= false;
		listaTiposMovimentacaoConfigurado = new ArrayList();
		arquivoTipoSomenteLeitura = false;
		arquivoTipoEmentaSomenteLeitura = false;
		naoGerarVeficarProcesso = false;
		visualizarPedidoAssistencia = false;
		pedidoAssistencia = "";
		setProcessoTipoSessao("");
		setId_ProcessoTipoSessao("");
		audienciaStatusCodigo = "";
		audienciaStatus = "";
		super.limpar();
	}

	public void copiar(AnaliseConclusaoDt objeto) {
		//fluxo = objeto.getFluxo();
		id_Classificador = objeto.getId_Classificador();
		classificador = objeto.getClassificador();
		listaPendenciasGerar = objeto.getListaPendenciasGerar();
		listaPendenciaTipos = objeto.getListaPendenciaTipos();
		processoTipoSessao = objeto.getProcessoTipoSessao();
		id_ProcessoTipoSessao = objeto.getId_ProcessoTipoSessao();
		naoGerarVeficarProcesso = objeto.isNaoGerarVeficarProcesso();
		visualizarPedidoAssistencia = objeto.isVisualizarPedidoAssistencia();
		pedidoAssistencia = objeto.getPedidoAssistencia();
		super.copiar(objeto);
	}

	public List getListaPendenciaTipos() {
		return listaPendenciaTipos;
	}

	public void setListaPendenciaTipos(List listaPendenciaTipos) {
		this.listaPendenciaTipos = listaPendenciaTipos;
	}

	public List getListaPendenciasGerar() {
		return listaPendenciasGerar;
	}

	public void setListaPendenciasGerar(List listaPendenciasGerar) {
		this.listaPendenciasGerar = listaPendenciasGerar;
	}

	public void addPendenciasGerar(PendenciaDt pendencia) {
		if (listaPendenciasGerar == null) listaPendenciasGerar = new ArrayList();
		this.listaPendenciasGerar.add(pendencia);
	}

	public PendenciaArquivoDt getArquivoConfiguracaoPreAnalise() {
		return arquivoConfiguracaoPreAnalise;
	}

	public void setArquivoConfiguracaoPreAnalise(PendenciaArquivoDt arquivoConfiguracaoPreAnalise) {
		this.arquivoConfiguracaoPreAnalise = arquivoConfiguracaoPreAnalise;
	}

	public String getPasso3() {
		return passo3;
	}

	public void setPasso3(String passo3) {
		if (passo3 != null) this.passo3 = passo3;
	}

	public String getId_Classificador() {
		return this.id_Classificador;
	}

	public void setId_Classificador(String id_Classificador) {
		if (id_Classificador != null) if (id_Classificador.equalsIgnoreCase("null")) {
			this.id_Classificador = "";
			this.classificador = "";
		} else if (!id_Classificador.equalsIgnoreCase("")) this.id_Classificador = id_Classificador;
	}

	public String getClassificador() {
		return classificador;
	}

	public void setClassificador(String classificador) {
		if (classificador != null) if (classificador.equalsIgnoreCase("null")) this.classificador = "";
		else if (!classificador.equalsIgnoreCase("")) this.classificador = classificador;
	}

	public void setPendenteAssinatura(boolean pendenteAssinatura) {
		this.pendenteAssinatura = pendenteAssinatura;
	}

	public boolean isPendenteAssinatura() {
		return pendenteAssinatura;
	}
	
	public void setListaTiposMovimentacaoConfigurado(List listaTiposMovimentacaoConfigurado) {		
		this.listaTiposMovimentacaoConfigurado = listaTiposMovimentacaoConfigurado;
	}
	
	public List getListaTiposMovimentacaoConfigurado(){		
		return this.listaTiposMovimentacaoConfigurado;
	}
	
	public void addListaTiposMovimentacaoConfigurado(UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt) {	
		this.listaTiposMovimentacaoConfigurado.add(usuarioMovimentacaoTipoDt);
	}

	public void setArquivoTipoSomenteLeitura(boolean arquivoTipoSomenteLeitura) {
		this.arquivoTipoSomenteLeitura = arquivoTipoSomenteLeitura;		
	}
	
	public boolean isArquivoTipoSomenteLeitura() {
		return this.arquivoTipoSomenteLeitura;		
	}
	
	public void setArquivoTipoEmentaSomenteLeitura(boolean arquivoTipoEmentaSomenteLeitura) {
		this.arquivoTipoEmentaSomenteLeitura = arquivoTipoEmentaSomenteLeitura;		
	}
	
	public boolean isArquivoTipoEmentaSomenteLeitura() {
		return this.arquivoTipoEmentaSomenteLeitura;		
	}

	public String getProcessoTipoSessao() {
		return processoTipoSessao;
	}

	public void setProcessoTipoSessao(String processoTipoSessao) {
		if (processoTipoSessao != null) this.processoTipoSessao = processoTipoSessao;
	}

	public String getId_ProcessoTipoSessao() {
		return id_ProcessoTipoSessao;
	}

	public void setId_ProcessoTipoSessao(String id_ProcessoTipoSessao) {
		if (id_ProcessoTipoSessao != null) this.id_ProcessoTipoSessao = id_ProcessoTipoSessao;
	}

	public boolean isNaoGerarVeficarProcesso() {
		return naoGerarVeficarProcesso;
	}

	public void setNaoGerarVeficarProcesso(boolean naoGerarVeficarProcesso) {
		this.naoGerarVeficarProcesso = naoGerarVeficarProcesso;
	}
	
	public boolean isVisualizarPedidoAssistencia() {
		return visualizarPedidoAssistencia;
	}

	public void setVisualizarPedidoAssistencia(boolean visualizarPedidoAssistencia) {
		this.visualizarPedidoAssistencia = visualizarPedidoAssistencia;
	}
	
	public String getPedidoAssistencia() {
		return pedidoAssistencia;
	}

	public void setPedidoAssistencia(String pedidoAssistencia) {
		this.pedidoAssistencia = pedidoAssistencia;
	}

	public boolean isIniciarVotacao() {
		return iniciarVotacao;
	}

	public void setIniciarVotacao(boolean iniciarVotacao) {
		this.iniciarVotacao = iniciarVotacao;
	}

	public boolean isAdiarVotacao() {
		return adiarVotacao;
	}

	public void setAdiarVotacao(boolean adiarVotacao) {
		this.adiarVotacao = adiarVotacao;
	}

	public String getAudienciaStatus() {
		return audienciaStatus;
	}

	public void setAudienciaStatus(String audienciaStatus) {
		if(audienciaStatus != null)	this.audienciaStatus = audienciaStatus;
	}

	public String getAudienciaStatusCodigo() {
		return audienciaStatusCodigo;
	}

	public void setAudienciaStatusCodigo(String codigo) {
		if(codigo != null)
			this.audienciaStatusCodigo = codigo;
	}

	public boolean isAguardarIniciarVotacao() {
		return aguardarIniciarVotacao;
	}

	public void setAguardarIniciarVotacao(boolean aguardarIniciarVotacao) {
		this.aguardarIniciarVotacao = aguardarIniciarVotacao;
	}

	// jvosantos - 14/10/2019 13:22 - Flag para verificar se é virtual
	public boolean isVirtual() {
		return virtual;
	}

	// jvosantos - 14/10/2019 13:22 - Flag para verificar se é virtual
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}
	
	public String getId_Pendencia_Ementa(){
		if (this.getArquivoPreAnaliseEmenta() != null && this.getArquivoPreAnaliseEmenta().getId_Pendencia() != null && this.getArquivoPreAnaliseEmenta().getId_Pendencia().length() > 0){
			return this.getArquivoPreAnaliseEmenta().getId_Pendencia();
		} else{
			return "";
		}
	}
	
}
