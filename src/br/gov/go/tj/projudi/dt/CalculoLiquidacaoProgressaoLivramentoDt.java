package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

/**
 * Este objeto será utilizado no cálculo de liquidação de penas. 
 * Armazena as variáveis necessárias para o cálculo de progressão de regime e livramento condicional
 * @author wcsilva
 */
public class CalculoLiquidacaoProgressaoLivramentoDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5860426701708247547L;
	
	//variáveis utilizadas no cálculo de Progressão de Regime
	private String tempoCondenacaoComumProgressaoDias;
	private String tempoCondenacaoComumProgressaoAnos;
	private String tempoCondenacaoHediondoProgressaoDias;
	private String tempoCondenacaoHediondoProgressaoAnos;
	private String tempoCondenacaoHediondoReincidenteProgressaoDias;
	private String tempoCondenacaoHediondoReincidenteProgressaoAnos;
	private String tempoACumprirProgressaoDias;
	private String tempoACumprirProgressaoAnos;
	private String dataBaseProgressao;
	private String dataRequisitoTemporalProgressao;
	private String novoRegimeProgressao;
	private String tempoInterrupcaoAposDataBaseDias;
	private String tempoInterrupcaoAposDataBaseAnos;
	private String tempoCumpridoDataBaseDias;
	private String tempoCumpridoDataBaseAnos;
	private String tempoRestanteDataBaseDias;
	private String tempoRestanteDataBaseAnos;
	private String reincidenteHediondoPR;
	
	//variáveis utilizadas no cálculo de Livramento Condicional
	private String tempoComumLivramentoDias;
	private String tempoComumLivramentoAnos;
	private String tempoComumReincidenteLivramentoDias;
	private String tempoComumReincidenteLivramentoAnos;
	private String tempoHediondoLivramentoDias;
	private String tempoHediondoLivramentoAnos;
	private String tempoHediondoReincidenteLivramentoDias;
	private String tempoHediondoReincidenteLivramentoAnos;
	private String tempoACumprirLivramentoDias;
	private String tempoACumprirLivramentoAnos;
	private String dataBaseLivramento;
	private String dataRequisitoTemporalLivramento;
	private String tempoInterrupcaoTotalDias;
	private String tempoInterrupcaoTotalAnos;
	private String reincidenteEspecificoLC;
	private String forcarCalculoLC;
	
	private String tempoCumpridoDataBaseLivramentoDias;
	private String tempoCumpridoDataBaseLivramentoAnos;
	private String tempoRestanteDataBaseLivramentoDias;
	private String tempoRestanteDataBaseLivramentoAnos;
	private String tempoInterrupcaoAposDataBaseLivramentoDias;
	private String tempoInterrupcaoAposDataBaseLivramentoAnos;
	
	private String tempoTotalRemicaoDias;
	private String tempoTotalRemicaoAnos;
	private String tempoTotalCondenacaoAnos; //utilizado no relatório pdf
	private String mensagemProgressao;
	private String mensagemLivramento;
	
	public CalculoLiquidacaoProgressaoLivramentoDt(){
		this.limpar();
	}
	
	public void limpar(){
		tempoCondenacaoComumProgressaoDias = "";
		tempoCondenacaoComumProgressaoAnos = "";
		tempoCondenacaoHediondoProgressaoDias = "";
		tempoCondenacaoHediondoProgressaoAnos = "";
		tempoCondenacaoHediondoReincidenteProgressaoDias = "";
		tempoCondenacaoHediondoReincidenteProgressaoAnos = "";
		tempoACumprirProgressaoDias = "";
		tempoACumprirProgressaoAnos = "";
		dataBaseProgressao = "";
		dataRequisitoTemporalProgressao = "";
		novoRegimeProgressao = "";
		tempoInterrupcaoAposDataBaseDias = "";
		tempoInterrupcaoAposDataBaseAnos = "";
		tempoCumpridoDataBaseDias = "";
		tempoCumpridoDataBaseAnos = "";
		tempoRestanteDataBaseDias = "";
		tempoRestanteDataBaseAnos = "";
		
		tempoComumLivramentoDias = "";
		tempoComumLivramentoAnos = "";
		tempoComumReincidenteLivramentoDias = "";
		tempoComumReincidenteLivramentoAnos = "";
		tempoHediondoLivramentoDias = "";
		tempoHediondoLivramentoAnos = "";
		tempoHediondoReincidenteLivramentoDias = "";
		tempoHediondoReincidenteLivramentoAnos = "";
		tempoACumprirLivramentoDias = "";
		tempoACumprirLivramentoAnos = "";
		dataBaseLivramento = "";
		dataRequisitoTemporalLivramento = "";
		tempoInterrupcaoTotalDias = "";
		tempoInterrupcaoTotalAnos = "";
		
		tempoCumpridoDataBaseLivramentoDias = "";
		tempoCumpridoDataBaseLivramentoAnos = "";
		tempoRestanteDataBaseLivramentoDias = "";
		tempoRestanteDataBaseLivramentoAnos = "";
		tempoInterrupcaoAposDataBaseLivramentoDias = "";
		tempoInterrupcaoAposDataBaseLivramentoAnos = "";
		
		tempoTotalRemicaoDias = "";
		tempoTotalRemicaoAnos = "";
		reincidenteEspecificoLC = "";
		forcarCalculoLC = "";
		reincidenteHediondoPR = "";
		mensagemLivramento = "";
		mensagemProgressao = "";
	}

	public String getDataBaseProgressao() {
		return this.dataBaseProgressao;
	}

	public void setDataBaseProgressao(String dataBaseProgressao) {
		this.dataBaseProgressao = dataBaseProgressao;
	}

	public String getDataRequisitoTemporalProgressao() {
		return this.dataRequisitoTemporalProgressao;
	}

	public void setDataRequisitoTemporalProgressao(String dataRequisitoTemporalProgressao) {
		this.dataRequisitoTemporalProgressao = dataRequisitoTemporalProgressao;
	}

	public String getNovoRegimeProgressao() {
		return this.novoRegimeProgressao;
	}

	public void setNovoRegimeProgressao(String novoRegimeProgressao) {
		this.novoRegimeProgressao = novoRegimeProgressao;
	}

	public String getTempoCumpridoDataBaseDias() {
		return this.tempoCumpridoDataBaseDias;
	}

	public void setTempoCumpridoDataBaseDias(String tempoCumpridoDataBaseDias) {
		this.tempoCumpridoDataBaseDias = tempoCumpridoDataBaseDias;
		setTempoCumpridoDataBaseAnos(tempoCumpridoDataBaseDias);
	}

	public String getTempoCumpridoDataBaseAnos() {
		return this.tempoCumpridoDataBaseAnos;
	}

	public void setTempoCumpridoDataBaseAnos(String tempoEmDias) {
		this.tempoCumpridoDataBaseAnos = getTempoNegativoFormatado(tempoEmDias);
	}

	public String getTempoRestanteDataBaseDias() {
		return this.tempoRestanteDataBaseDias;
	}

	public void setTempoRestanteDataBaseDias(String tempoRestanteDataBaseDias) {
		this.tempoRestanteDataBaseDias = tempoRestanteDataBaseDias;
		setTempoRestanteDataBaseAnos(tempoRestanteDataBaseDias);
	}

	public String getTempoRestanteDataBaseAnos() {
		return this.tempoRestanteDataBaseAnos;
	}

	public void setTempoRestanteDataBaseAnos(String tempoEmDias) {
		this.tempoRestanteDataBaseAnos = getTempoNegativoFormatado(tempoEmDias);
	}

	public String getTempoCondenacaoComumProgressaoDias() {
		return this.tempoCondenacaoComumProgressaoDias;
	}

	public void setTempoCondenacaoComumProgressaoDias(String tempoCondenacaoComumProgressaoDias) {
		this.tempoCondenacaoComumProgressaoDias = tempoCondenacaoComumProgressaoDias;
		setTempoCondenacaoComumProgressaoAnos(tempoCondenacaoComumProgressaoDias);
	}

	public String getTempoCondenacaoComumProgressaoAnos() {
		return this.tempoCondenacaoComumProgressaoAnos;
	}

	public void setTempoCondenacaoComumProgressaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCondenacaoComumProgressaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCondenacaoComumProgressaoAnos = "";
	}

	public String getTempoCondenacaoHediondoProgressaoDias() {
		return this.tempoCondenacaoHediondoProgressaoDias;
	}

	public void setTempoCondenacaoHediondoProgressaoDias(String tempoCondenacaoHediondoProgressaoDias) {
		this.tempoCondenacaoHediondoProgressaoDias = tempoCondenacaoHediondoProgressaoDias;
		setTempoCondenacaoHediondoProgressaoAnos(tempoCondenacaoHediondoProgressaoDias);
	}

	public String getTempoCondenacaoHediondoProgressaoAnos() {
		return this.tempoCondenacaoHediondoProgressaoAnos;
	}

	public void setTempoCondenacaoHediondoProgressaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCondenacaoHediondoProgressaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCondenacaoHediondoProgressaoAnos = "";
	}

	public String getTempoCondenacaoHediondoReincidenteProgressaoDias() {
		return this.tempoCondenacaoHediondoReincidenteProgressaoDias;
	}

	public void setTempoCondenacaoHediondoReincidenteProgressaoDias(String tempoCondenacaoHediondoReincidenteProgressaoDias) {
		this.tempoCondenacaoHediondoReincidenteProgressaoDias = tempoCondenacaoHediondoReincidenteProgressaoDias;
		setTempoCondenacaoHediondoReincidenteProgressaoAnos(tempoCondenacaoHediondoReincidenteProgressaoDias);
	}

	public String getTempoCondenacaoHediondoReincidenteProgressaoAnos() {
		return this.tempoCondenacaoHediondoReincidenteProgressaoAnos;
	}

	public void setTempoCondenacaoHediondoReincidenteProgressaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCondenacaoHediondoReincidenteProgressaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCondenacaoHediondoReincidenteProgressaoAnos = "";
	}

	public String getTempoACumprirProgressaoDias() {
		return this.tempoACumprirProgressaoDias;
	}

	public void setTempoACumprirProgressaoDias(String tempoACumprirProgressaoDias) {
		this.tempoACumprirProgressaoDias = tempoACumprirProgressaoDias;
		setTempoACumprirProgressaoAnos(tempoACumprirProgressaoDias);
	}

	public String getTempoACumprirProgressaoAnos() {
		return this.tempoACumprirProgressaoAnos;
	}

	public void setTempoACumprirProgressaoAnos(String tempoEmDias) {
		this.tempoACumprirProgressaoAnos = getTempoNegativoFormatado(tempoEmDias);
	}

	public String getTempoInterrupcaoAposDataBaseDias() {
		return tempoInterrupcaoAposDataBaseDias;
	}

	public void setTempoInterrupcaoAposDataBaseDias(String tempoEmDias) {
		this.tempoInterrupcaoAposDataBaseDias = tempoEmDias;
		setTempoInterrupcaoAposDataBaseAnos(tempoEmDias);
	}

	public String getTempoInterrupcaoAposDataBaseAnos() {
		return tempoInterrupcaoAposDataBaseAnos;
	}

	public void setTempoInterrupcaoAposDataBaseAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoInterrupcaoAposDataBaseAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoInterrupcaoAposDataBaseAnos = "";
	}

	public String getDataBaseLivramento() {
		return this.dataBaseLivramento;
	}

	public void setDataBaseLivramento(String dataBaseLivramento) {
		this.dataBaseLivramento = dataBaseLivramento;
	}

	public String getDataRequisitoTemporalLivramento() {
		return this.dataRequisitoTemporalLivramento;
	}

	public void setDataRequisitoTemporalLivramento(String dataRequisitoTemporalLivramento) {
		this.dataRequisitoTemporalLivramento = dataRequisitoTemporalLivramento;
	}
	
	public String getTempoComumLivramentoDias() {
		return this.tempoComumLivramentoDias;
	}

	public void setTempoComumLivramentoDias(String tempoComumLivramentoDias) {
		this.tempoComumLivramentoDias = tempoComumLivramentoDias;
		setTempoComumLivramentoAnos(tempoComumLivramentoDias);
	}

	public String getTempoComumLivramentoAnos() {
		return this.tempoComumLivramentoAnos;
	}

	public void setTempoComumLivramentoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoComumLivramentoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoComumLivramentoAnos = "";
	}

	public String getTempoComumReincidenteLivramentoDias() {
		return this.tempoComumReincidenteLivramentoDias;
	}

	public void setTempoComumReincidenteLivramentoDias(String tempoComumReincidenteLivramentoDias) {
		this.tempoComumReincidenteLivramentoDias = tempoComumReincidenteLivramentoDias;
		setTempoComumReincidenteLivramentoAnos(tempoComumReincidenteLivramentoDias);
	}

	public String getTempoComumReincidenteLivramentoAnos() {
		return this.tempoComumReincidenteLivramentoAnos;
	}

	public void setTempoComumReincidenteLivramentoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoComumReincidenteLivramentoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoComumReincidenteLivramentoAnos = "";
	}

	public String getTempoHediondoLivramentoDias() {
		return this.tempoHediondoLivramentoDias;
	}

	public void setTempoHediondoLivramentoDias(String tempoHediondoLivramentoDias) {
		this.tempoHediondoLivramentoDias = tempoHediondoLivramentoDias;
		setTempoHediondoLivramentoAnos(tempoHediondoLivramentoDias);
	}

	public String getTempoHediondoLivramentoAnos() {
		return this.tempoHediondoLivramentoAnos;
	}

	public void setTempoHediondoLivramentoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoHediondoLivramentoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoHediondoLivramentoAnos = "";
	}

	public String getTempoHediondoReincidenteLivramentoDias() {
		return this.tempoHediondoReincidenteLivramentoDias;
	}

	public void setTempoHediondoReincidenteLivramentoDias(String tempoHediondoReincidenteLivramentoDias) {
		this.tempoHediondoReincidenteLivramentoDias = tempoHediondoReincidenteLivramentoDias;
		setTempoHediondoReincidenteLivramentoAnos(tempoHediondoReincidenteLivramentoDias);
	}

	public String getTempoHediondoReincidenteLivramentoAnos() {
		return this.tempoHediondoReincidenteLivramentoAnos;
	}

	public void setTempoHediondoReincidenteLivramentoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoHediondoReincidenteLivramentoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoHediondoReincidenteLivramentoAnos = "";
	}

	public String getTempoACumprirLivramentoDias() {
		return this.tempoACumprirLivramentoDias;
	}

	public void setTempoACumprirLivramentoDias(String tempoACumprirLivramentoDias) {
		this.tempoACumprirLivramentoDias = tempoACumprirLivramentoDias;
		setTempoACumprirLivramentoAnos(tempoACumprirLivramentoDias);
	}

	public String getTempoACumprirLivramentoAnos() {
		return this.tempoACumprirLivramentoAnos;
	}

	public void setTempoACumprirLivramentoAnos(String tempoEmDias) {
		this.tempoACumprirLivramentoAnos = getTempoNegativoFormatado(tempoEmDias);
	}
	
	public String getTempoInterrupcaoTotalDias() {
		return tempoInterrupcaoTotalDias;
	}

	public void setTempoInterrupcaoTotalDias(String tempoEmDias) {
		this.tempoInterrupcaoTotalDias = tempoEmDias;
		setTempoInterrupcaoTotalAnos(tempoEmDias);
	}

	public String getTempoInterrupcaoTotalAnos() {
		return tempoInterrupcaoTotalAnos;
	}

	public void setTempoInterrupcaoTotalAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoInterrupcaoTotalAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoInterrupcaoTotalAnos = "";
	}
	
	public String getTempoNegativoFormatado(String tempoEmDias){
		String tempoFormatado = "";
		if (tempoEmDias.length() > 0){
			if (Funcoes.StringToInt(tempoEmDias) < 0){
				tempoFormatado = "(" + Funcoes.converterParaAnoMesDia(Math.abs(Funcoes.StringToInt(tempoEmDias))) + ")";
			} else tempoFormatado = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		}
		return tempoFormatado;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}
	
	public String getTempoTotalRemicaoDias() {
		return this.tempoTotalRemicaoDias;
	}

	public void setTempoTotalRemicaoDias(String tempoEmDias) {
		this.tempoTotalRemicaoDias = tempoEmDias;
		setTempoTotalRemicaoAnos(tempoEmDias);
	}

	public String getTempoTotalRemicaoAnos() {
		return this.tempoTotalRemicaoAnos;
	}

	public void setTempoTotalRemicaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoTotalRemicaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoTotalRemicaoAnos = "";
	}

	public String getTempoTotalCondenacaoAnos() {
		return tempoTotalCondenacaoAnos;
	}

	public void setTempoTotalCondenacaoAnos(String tempoTotalCondenacaoAnos) {
		this.tempoTotalCondenacaoAnos = tempoTotalCondenacaoAnos;
	}

	public String getReincidenteEspecificoLC() {
		return reincidenteEspecificoLC;
	}

	public void setReincidenteEspecificoLC(String reincidenteEspecificoLC) {
		this.reincidenteEspecificoLC = reincidenteEspecificoLC;
	}

	public String getReincidenteHediondoPR() {
		return reincidenteHediondoPR;
	}

	public void setReincidenteHediondoPR(String reincidenteHediondoPR) {
		this.reincidenteHediondoPR = reincidenteHediondoPR;
	}

	public String getForcarCalculoLC() {
		return forcarCalculoLC;
	}

	public void setForcarCalculoLC(String forcarCalculoLC) {
		this.forcarCalculoLC = forcarCalculoLC;
	}

	public String getMensagemProgressao() {
		return mensagemProgressao;
	}

	public void setMensagemProgressao(String mensagemProgressao) {
		this.mensagemProgressao = mensagemProgressao;
	}

	public String getMensagemLivramento() {
		return mensagemLivramento;
	}

	public void setMensagemLivramento(String mensagemLivramento) {
		this.mensagemLivramento = mensagemLivramento;
	}
	
	public String getTempoInterrupcaoAposDataBaseLivramentoDias() {
		return tempoInterrupcaoAposDataBaseLivramentoDias;
	}

	public void setTempoInterrupcaoAposDataBaseLivramentoDias(String tempoEmDias) {
		this.tempoInterrupcaoAposDataBaseLivramentoDias = tempoEmDias;
		setTempoInterrupcaoAposDataBaseLivramentoAnos(tempoEmDias);
	}

	public String getTempoInterrupcaoAposDataBaseLivramentoAnos() {
		return tempoInterrupcaoAposDataBaseLivramentoAnos;
	}

	public void setTempoInterrupcaoAposDataBaseLivramentoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoInterrupcaoAposDataBaseLivramentoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoInterrupcaoAposDataBaseLivramentoAnos = "";
	}
	
	public String getTempoCumpridoDataBaseLivramentoDias() {
		return this.tempoCumpridoDataBaseLivramentoDias;
	}

	public void setTempoCumpridoDataBaseLivramentoDias(String tempoCumpridoDataBaseDias) {
		this.tempoCumpridoDataBaseLivramentoDias = tempoCumpridoDataBaseDias;
		setTempoCumpridoDataBaseLivramentoAnos(tempoCumpridoDataBaseDias);
	}

	public String getTempoCumpridoDataBaseLivramentoAnos() {
		return this.tempoCumpridoDataBaseLivramentoAnos;
	}

	public void setTempoCumpridoDataBaseLivramentoAnos(String tempoEmDias) {
		this.tempoCumpridoDataBaseLivramentoAnos = getTempoNegativoFormatado(tempoEmDias);
	}

	public String getTempoRestanteDataBaseLivramentoDias() {
		return this.tempoRestanteDataBaseLivramentoDias;
	}

	public void setTempoRestanteDataBaseLivramentoDias(String tempoRestanteDataBaseDias) {
		this.tempoRestanteDataBaseLivramentoDias = tempoRestanteDataBaseDias;
		setTempoRestanteDataBaseLivramentoAnos(tempoRestanteDataBaseDias);
	}

	public String getTempoRestanteDataBaseLivramentoAnos() {
		return this.tempoRestanteDataBaseLivramentoAnos;
	}

	public void setTempoRestanteDataBaseLivramentoAnos(String tempoEmDias) {
		this.tempoRestanteDataBaseLivramentoAnos = getTempoNegativoFormatado(tempoEmDias);
	}
	
}
