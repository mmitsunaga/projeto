package br.gov.go.tj.projudi.dt;

import java.util.List;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ProcessoParteDebitoDt extends ProcessoParteDebitoDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = -4259865093296708192L;

    public static final int CodigoPermissao = 411;
    public static final int CodigoPermissaoFinanceiro = 923;

	/**
	 * Foram criadas essas variáveis para auxiliar na manipulação de dados referentes ao processo, pois não compensava ter um atributo 
	 * do tipo ProcessoDt, pois a maioria dos seus atributos não seriam utilizados
	 */
	private String id_Processo;
	private String processoNumero;
	private String processoNumeroCompleto;
	private String id_Serventia;
	private String serventia;
	private String id_GuiaEmissao;
	private String numeroGuiaEmissao;
	private String dataVencimentoGuiaEmissao;
	private String id_GuiaTipo;
	private String guiaTipo;
	private String id_GuiaStatus;
	private String guiaStatus;
	private String valorTotalGuia;
	private String processoNumeroPROAD;
	private String id_ProcessoDebitoStatus;
	private String processoDebitoStatus;
	private String dataBaixa;
	private List listaPartesPromoventes;
	private List listaPartesPromovidas;
	private List listaPartesComDebito;
	private List listaGuiasProcesso;
	private List listaProcessoDebito;
	private List listaProcessoDebitoStatus;
	private String enderecoPartePROAD;
	private String dataDebitoPROAD;
	private String valorCreditoPROAD;	
	private String tipoParte;
	private String dividaSolidaria;
	private String observacaoProcessoDebitoStatus;

	//Variável para auxiliar na consulta de débitos
	private String cpfParte;

	public void limpar() {
		this.limparParcial();
		id_Processo = "";
		processoNumero = "";
		processoNumeroCompleto = "";
		listaPartesPromoventes = null;
		listaPartesPromovidas = null;
		listaPartesComDebito = null;
		listaGuiasProcesso = null;
		listaProcessoDebito = null;
		listaProcessoDebitoStatus = null;
	}
	
	public void limparParcial() {
		super.limpar();
		cpfParte = "";
		id_Serventia = "";
		serventia = "";
		id_GuiaEmissao = "";
		numeroGuiaEmissao = "";
		dataVencimentoGuiaEmissao = "";
		id_GuiaTipo = "";
		guiaTipo = "";
		id_GuiaStatus = "";
		guiaStatus = "";	
		valorTotalGuia = "";
		setId_ProcessoDebitoStatus("");
		processoDebitoStatus = "";
		dataBaixa = "";
		processoNumeroPROAD = "";
		enderecoPartePROAD = "";
		dataDebitoPROAD = "";
		valorCreditoPROAD = "";		
		tipoParte = "";
		dividaSolidaria = "";		
		observacaoProcessoDebitoStatus = "";		
	}
	
	public void copiar(ProcessoParteDebitoDt objeto) {
		super.copiar(objeto);
		id_Processo = objeto.getId_Processo();
		processoNumero = objeto.getProcessoNumero();
		processoNumeroCompleto = objeto.getProcessoNumeroCompleto();
		cpfParte = objeto.getCpfParte();
		id_Serventia = objeto.getId_Serventia();
		serventia = objeto.getServentia();
		id_GuiaEmissao = objeto.getId_GuiaEmissao();
		numeroGuiaEmissao = objeto.getNumeroGuiaEmissao();
		dataVencimentoGuiaEmissao = objeto.getDataVencimentoGuiaEmissao();
		id_GuiaTipo = objeto.getId_GuiaTipo();
		guiaTipo = objeto.getGuiaTipo();
		guiaStatus = objeto.getGuiaStatus();
		valorTotalGuia = objeto.getValorTotalGuia();
		id_ProcessoDebitoStatus = objeto.getId_ProcessoDebitoStatus();
		processoDebitoStatus = objeto.getProcessoDebitoStatus();
		dataBaixa = objeto.getDataBaixa();
		processoNumeroPROAD = objeto.getProcessoNumeroPROAD();
		enderecoPartePROAD = objeto.getEnderecoPartePROAD();
		dataDebitoPROAD = objeto.getDataDebitoPROAD();
		valorCreditoPROAD = objeto.getValorCreditoPROAD();		
		tipoParte = objeto.getTipoParte();
		dividaSolidaria = objeto.getDividaSolidaria();
		observacaoProcessoDebitoStatus = objeto.getObservacaoProcessoDebitoStatus();
	}

	public String getId_Processo() {
		return id_Processo;
	}

	public void setId_Processo(String id_Processo) {
		if (id_Processo != null) this.id_Processo = id_Processo;
	}

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String numeroProcesso) {
		if (numeroProcesso != null) this.processoNumero = numeroProcesso;
	}
	
	public String getProcessoNumeroCompleto() {
		return processoNumeroCompleto;
	}

	public void setProcessoNumeroCompleto(String numeroProcessoCompleto) {
		if (numeroProcessoCompleto != null) this.processoNumeroCompleto = numeroProcessoCompleto;
	}

	public String getCpfParte() {
		return cpfParte;
	}

	public void setCpfParte(String cpfParte) {
		if (cpfParte != null) this.cpfParte = cpfParte;
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
	
	public String getId_GuiaEmissao() {
		return id_GuiaEmissao;
	}

	public void setId_GuiaEmissao(String id_GuiaEmissao) {
		if (id_GuiaEmissao != null) this.id_GuiaEmissao = id_GuiaEmissao;
	}
	
	public String getNumeroGuiaEmissao() {
		return numeroGuiaEmissao;
	}

	public void setNumeroGuiaEmissao(String numeroGuiaEmissao) {
		if (numeroGuiaEmissao != null) this.numeroGuiaEmissao = numeroGuiaEmissao;
	}
	
	public String getId_GuiaTipo() {
		return id_GuiaTipo;
	}

	public void setId_GuiaTipo(String id_GuiaTipo) {
		if (id_GuiaTipo != null) this.id_GuiaTipo = id_GuiaTipo;
	}
	
	public String getGuiaTipo() {
		return guiaTipo;
	}

	public void setGuiaTipo(String guiaTipo) {
		if (guiaTipo != null) this.guiaTipo = guiaTipo;
	}
	
	public String getId_GuiaStatus() {
		return id_GuiaStatus;
	}

	public void setId_GuiaStatus(String id_GuiaStatus) {
		if (id_GuiaStatus != null) this.id_GuiaStatus = id_GuiaStatus;
	}
	
	public String getGuiaStatus() {
		return guiaStatus;
	}

	public void setGuiaStatus(String guiaStatus) {
		if (guiaStatus != null) this.guiaStatus = guiaStatus;
	}
	
	public String getValorTotalGuia() {
		return valorTotalGuia;
	}

	public void setValorTotalGuia(String valorTotalGuia) {
		if (valorTotalGuia != null) this.valorTotalGuia = valorTotalGuia;
	}
	
	public List getListaPartesPromoventes() {
		return listaPartesPromoventes;
	}

	public void setListaPartesPromoventes(List listaPartesPromoventes) {
		this.listaPartesPromoventes = listaPartesPromoventes;
	}
	
	public List getListaPartesPromovidas() {
		return listaPartesPromovidas;
	}

	public void setListaPartesPromovidas(List listaPartesPromovidas) {
		this.listaPartesPromovidas = listaPartesPromovidas;
	}
	
	public List getListaPartesComDebito() {
		return listaPartesComDebito;
	}

	public void setListaPartesComDebito(List listaPartesComDebito) {
		this.listaPartesComDebito = listaPartesComDebito;
	}
	
	public List getListaGuiasProcesso() {
		return listaGuiasProcesso;
	}

	public void setListaGuiasProcesso(List listaGuiasProcesso) {
		this.listaGuiasProcesso = listaGuiasProcesso;
	}
	
	public String getProcessoNumeroPROAD() {
		return this.processoNumeroPROAD;
	}
	
	public void setProcessoNumeroPROAD(String processoNumeroPROAD) {
		if (processoNumeroPROAD != null) this.processoNumeroPROAD = processoNumeroPROAD;
	}
	
	public String getId_ProcessoDebitoStatus() {
		return id_ProcessoDebitoStatus;
	}

	public void setId_ProcessoDebitoStatus(String id_ProcessoDebitoStatus) {
		if (id_ProcessoDebitoStatus != null) this.id_ProcessoDebitoStatus = id_ProcessoDebitoStatus;
	}
	
	public String getProcessoDebitoStatus() {
		return this.processoDebitoStatus;
	}
	
	public void setProcessoDebitoStatus(String processoDebitoStatus) {
		if (processoDebitoStatus != null) this.processoDebitoStatus = processoDebitoStatus;
	}
	
	public boolean isEmAberto() {
		return !isBaixado();
	}
	
	public boolean isBaixado() {
		return Funcoes.StringToLong(this.id_ProcessoDebitoStatus) == ProcessoDebitoStatusDt.BAIXADO;
	}
	
	public String getDataBaixa() {
		return this.dataBaixa;
	}
	
	public void setDataBaixa(String dataBaixa) {
		if (dataBaixa != null) this.dataBaixa = dataBaixa;
	}

	public String getEnderecoPartePROAD() {
		return enderecoPartePROAD;
	}

	public void setEnderecoPartePROAD(String enderecoPartePROAD) {
		if (enderecoPartePROAD != null) this.enderecoPartePROAD = enderecoPartePROAD;
	}

	public String getDataDebitoPROAD() {
		return dataDebitoPROAD;
	}

	public void setDataDebitoPROAD(String dataDebitoPROAD) {
		if (dataDebitoPROAD != null) this.dataDebitoPROAD = dataDebitoPROAD;
	}

	public String getValorCreditoPROAD() {
		return valorCreditoPROAD;
	}

	public void setValorCreditoPROAD(String valorCreditoPROAD) {
		if (valorCreditoPROAD != null) this.valorCreditoPROAD = valorCreditoPROAD;
	}

	public boolean isLiberadoEnvioCadin() {
		return Funcoes.StringToInt(getId_ProcessoDebitoStatus()) == ProcessoDebitoStatusDt.LIBERADO_PARA_ENVIO_CADIN;
	}
	
	public boolean isEnviadoCadin() {
		return Funcoes.StringToInt(getId_ProcessoDebitoStatus()) == ProcessoDebitoStatusDt.ENVIADO_CADIN;
	}

	public List getListaProcessoDebito() {
		return listaProcessoDebito;
	}

	public void setListaProcessoDebito(List listaProcessoDebito) {
		this.listaProcessoDebito = listaProcessoDebito;
	}
	
	public List getListaProcessoDebitoStatus() {
		return listaProcessoDebitoStatus;
	}

	public void setListaProcessoDebitoStatus(List listaProcessoDebitoStatus) {
		this.listaProcessoDebitoStatus = listaProcessoDebitoStatus;
	}

	public String getTipoParte() {
		return tipoParte;
	}

	public void setTipoParte(String tipoParte) {
		if (tipoParte != null) this.tipoParte = tipoParte;		
	}

	public String getDividaSolidaria() {
		return dividaSolidaria;
	}

	public void setDividaSolidaria(String dividaSolidaria) {
		if (dividaSolidaria != null) this.dividaSolidaria = dividaSolidaria;		
	}
	
	public boolean isDividaSolidaria() {
		return this.dividaSolidaria != null && this.dividaSolidaria.trim().equals("true");
	}
	
	public boolean NotIsDividaSolidaria() {
		return this.dividaSolidaria != null && this.dividaSolidaria.trim().length() > 0  && !isDividaSolidaria();
	}

	public String getObservacaoProcessoDebitoStatus() {
		return observacaoProcessoDebitoStatus;
	}

	public void setObservacaoProcessoDebitoStatus(String observacaoProcessoDebitoStatus) {
		if (observacaoProcessoDebitoStatus != null) {
			this.observacaoProcessoDebitoStatus = observacaoProcessoDebitoStatus;	
		}		
	}

	public String getDataVencimentoGuiaEmissao() {
		return dataVencimentoGuiaEmissao;
	}

	public void setDataVencimentoGuiaEmissao(String dataVencimentoGuiaEmissao) {
		if (dataVencimentoGuiaEmissao != null) {
			this.dataVencimentoGuiaEmissao = dataVencimentoGuiaEmissao;	
		}		
	}
}
