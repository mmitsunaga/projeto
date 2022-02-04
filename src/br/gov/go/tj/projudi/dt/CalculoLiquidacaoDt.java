package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

/**
 * Este objeto será utilizado no cálculo de liquidação de penas.
 * @author wcsilva
 */
public class CalculoLiquidacaoDt extends Dados {
	/**
	 * 
	 */
	private static final long serialVersionUID = 484190728802747163L;
	
	private String idCalculoLiquidacao;
	private String idProcesso;
	private String idUsuarioServentia;
	private byte[] relatorioByte;
	
	//variáveis utilizadas em qualquer cálculo de liquidação de penas
	private String tempoTotalCondenacaoDias;
	private String tempoTotalCondenacaoAnos;
	
	private String tempoTotalCondenacaoRemanescenteDias;
	private String tempoTotalCondenacaoRemanescenteAnos;
	
	private String tempoTotalComutacaoDias;
	private String tempoTotalComutacaoAnos;
	
	private String tempoCumpridoDataAtualDias;
	private String tempoCumpridoDataAtualAnos;
	private String tempoCumpridoUltimoEventoDias;
	private String tempoCumpridoUltimoEventoAnos;
	
	private String tempoRestanteDataAtualDias;
	private String tempoRestanteDataAtualAnos;
	private String tempoRestanteUltimoEventoDias;
	private String tempoRestanteUltimoEventoAnos;
	
	private String dataTerminoPena;
	private String dataTerminoPenaUnificada;
	private String dataCalculo;
	
	private String informacaoAdicional; //informações adicionais do cálculo
	private String dataValidadeMandadoPrisaoUnificada; //deixei aqui por causa do relatório em pdf
	private String dataValidadeMandadoPrisaoIndividual;
	private String artigoPrescricaoPunitiva;
	private String artigoPrescricaoExecutoria;
	private String artigoPrescricaoExecutoriaUnificada;
	private String tempoPrescricaoUnificadaAnos;
	private String dataUltimaFuga;
	private String formaCumprimento; //utilizado no relatório em pdf
	
	private String visualizaPenaUnificada;
	private String visualizaRestantePenaUltimoEvento;
	private boolean iniciouCumprimentoPena;
	private boolean mulherComMenores;
	private boolean saldoDevedorCrimeHediondo;
	private boolean considerarDetracao;
	private String calcularSomente;
	
	private List ListaTipoCalculo;
	
	//remição
	private String totalDiasTrabalhados;
	private String totalHorasEstudo;
	private String tempoTotalRemicaoTrabalhoDias;
	private String tempoTotalRemicaoTrabalhoAnos;
	private String tempoTotalRemicaoEstudoDias;
	private String tempoTotalRemicaoEstudoAnos;
	private String tipoRemicao;
	private String qtdeTempoHorasEstudo;
	private String tempoTotalRemicaoLeituraDias;
	private String tempoTotalRemicaoLeituraAnos;

	//variável utilizada no cálculo de Progressão de Regime e Livramento Condicional
	private CalculoLiquidacaoProgressaoLivramentoDt calculoProgressaoLivramentoDt;
	
	//variável utilizada no cálculo de comutação
	private CalculoLiquidacaoComutacaoDt calculoComutacaoDt;
	private List listaComutacao;
	private List listaComutacaoUnificada;
	
	//variáveis utilizadas no cálculo de indulto
	private CalculoLiquidacaoIndultoDt calculoIndultoDt;
	
	//variáveis utilizadas no cálculo da prescrição
	private List listaPrescricaoPunitiva;
	private List listaPrescricaoExecutoria;
	
	private CalculoLiquidacaoPenaRestritivaDt calculoPenaRestritivaDt;
	private List listaSursis;
	
	public CalculoLiquidacaoDt(){
		this.limpar();
	}
	
	public void limpar(){
		limparDadosEspecificos(true, true, true, true, true);
		informacaoAdicional = "";
		calculoPenaRestritivaDt = null;
		listaSursis = null;
		
	}
	
	public void limparDadosEspecificos(boolean limparDadosGerais, boolean limparProgressaoLivramento, 
			boolean limparComutacao, boolean limparIndulto, boolean limparPrescricao){
		
		if (limparDadosGerais){
			idCalculoLiquidacao = "";
			idProcesso = "";
			idUsuarioServentia = "";
			relatorioByte = null;
			tempoTotalCondenacaoDias = "";
			tempoTotalCondenacaoAnos = "";
			tempoTotalCondenacaoRemanescenteDias = "";
			tempoTotalCondenacaoRemanescenteAnos = "";
			tempoTotalComutacaoDias = "";
			tempoTotalComutacaoAnos = "";
			tempoCumpridoDataAtualDias = "";
			tempoCumpridoDataAtualAnos = "";
			tempoCumpridoUltimoEventoDias = "";
			tempoCumpridoUltimoEventoAnos = "";
			tempoRestanteDataAtualDias = "";
			tempoRestanteDataAtualAnos = "";
			tempoRestanteUltimoEventoDias = "";
			tempoRestanteUltimoEventoAnos = "";
			dataTerminoPena = "";
			dataTerminoPenaUnificada = "";
			dataCalculo = "";
			totalDiasTrabalhados = "";
			totalHorasEstudo = "";
			tempoTotalRemicaoTrabalhoDias = "";
			tempoTotalRemicaoTrabalhoAnos = "";
			tempoTotalRemicaoEstudoAnos = "";
			tempoTotalRemicaoEstudoDias = "";
			tempoTotalRemicaoLeituraDias = "";
			tempoTotalRemicaoLeituraAnos = "";
			tipoRemicao = "";
			visualizaPenaUnificada = "";
			visualizaRestantePenaUltimoEvento = "";
			qtdeTempoHorasEstudo = "12";
			iniciouCumprimentoPena = true;
			mulherComMenores = false;
			saldoDevedorCrimeHediondo = false;
			considerarDetracao = true;
			
			ListaTipoCalculo = null;
			calcularSomente = "";
		}
		if (limparProgressaoLivramento)	calculoProgressaoLivramentoDt = null;
		if (limparComutacao) {
			calculoComutacaoDt = null;
			listaComutacao = null;
			listaComutacaoUnificada = null;
		}
		if (limparPrescricao) {
			dataValidadeMandadoPrisaoUnificada = "";
			dataValidadeMandadoPrisaoIndividual = "";
			listaPrescricaoExecutoria = null;
			listaPrescricaoPunitiva = null;
			artigoPrescricaoPunitiva = "";
			artigoPrescricaoExecutoria = "";
			artigoPrescricaoExecutoriaUnificada = "";
			tempoPrescricaoUnificadaAnos = "";	
			dataUltimaFuga = "";
		}
		if (limparIndulto) {
			calculoIndultoDt = null;
		}
	}

	public String getDataTerminoPena() {
		return this.dataTerminoPena;
	}

	public void setDataTerminoPena(String dataTerminoPena) {
		this.dataTerminoPena = dataTerminoPena;
	}
	
	public String getDataTerminoPenaUnificada() {
		return this.dataTerminoPenaUnificada;
	}

	public void setDataTerminoPenaUnificada(String dataTerminoPenaUnificada) {
		this.dataTerminoPenaUnificada = dataTerminoPenaUnificada;
	}

	public String getDataCalculo() {
		return this.dataCalculo;
	}

	public void setDataCalculo(String dataCalculo) {
		this.dataCalculo = dataCalculo;
	}

	public List getListaTipoCalculo() {
		return ListaTipoCalculo;
	}

	public void setListaTipoCalculo(List listaTipoCalculo) {
		if (ListaTipoCalculo == null) ListaTipoCalculo = new ArrayList();
		this.ListaTipoCalculo = listaTipoCalculo;
	}
	
	public void addListaTipoCalculo(String lista) {
		if (ListaTipoCalculo == null) ListaTipoCalculo = new ArrayList();
		this.ListaTipoCalculo.add(lista);
	}

	public String getTempoTotalCondenacaoDias() {
		return this.tempoTotalCondenacaoDias;
	}

	public void setTempoTotalCondenacaoDias(String tempoTotalCondenacaoDias) {
		this.tempoTotalCondenacaoDias = tempoTotalCondenacaoDias;
		setTempoTotalCondenacaoAnos(tempoTotalCondenacaoDias);
	}

	public String getTempoTotalCondenacaoAnos() {
		return this.tempoTotalCondenacaoAnos;
	}

	public void setTempoTotalCondenacaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoTotalCondenacaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoTotalCondenacaoAnos = "";
	}
	
	public void setTempoTotalCondenacaoAnosTempoEmAno(String tempoEmAnos) {
		this.tempoTotalCondenacaoAnos = tempoEmAnos;
	}
	
	public String getTempoTotalCondenacaoRemanescenteDias() {
		return this.tempoTotalCondenacaoRemanescenteDias;
	}

	public void setTempoTotalCondenacaoRemanescenteDias(String tempoTotalCondenacaoRemanescenteDias) {
		this.tempoTotalCondenacaoRemanescenteDias = tempoTotalCondenacaoRemanescenteDias;
		setTempoTotalCondenacaoRemanescenteAnos(tempoTotalCondenacaoRemanescenteDias);
	}

	public String getTempoTotalCondenacaoRemanescenteAnos() {
		return this.tempoTotalCondenacaoRemanescenteAnos;
	}

	public void setTempoTotalCondenacaoRemanescenteAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoTotalCondenacaoRemanescenteAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoTotalCondenacaoRemanescenteAnos = "";
	}
	
	
	public String getTempoTotalComutacaoDias() {
		return this.tempoTotalComutacaoDias;
	}

	public void setTempoTotalComutacaoDias(String tempoTotalComutacaoDias) {
		this.tempoTotalComutacaoDias = tempoTotalComutacaoDias;
		setTempoTotalComutacaoAnos(tempoTotalComutacaoDias);
	}

	public String getTempoTotalComutacaoAnos() {
		return this.tempoTotalComutacaoAnos;
	}

	public void setTempoTotalComutacaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoTotalComutacaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoTotalComutacaoAnos = "";
	}
	
	public String getTempoCumpridoDataAtualDias() {
		return this.tempoCumpridoDataAtualDias;
	}

	public void setTempoCumpridoDataAtualDias(String tempoCumpridoDataAtualDias) {
		this.tempoCumpridoDataAtualDias = tempoCumpridoDataAtualDias;
		setTempoCumpridoDataAtualAnos(tempoCumpridoDataAtualDias);
	}

	public String getTempoCumpridoDataAtualAnos() {
		return this.tempoCumpridoDataAtualAnos;
	}

	public void setTempoCumpridoDataAtualAnos(String tempoEmDias) {
		this.tempoCumpridoDataAtualAnos = getTempoNegativoFormatado(tempoEmDias);
	}

	public String getTempoCumpridoUltimoEventoDias() {
		return this.tempoCumpridoUltimoEventoDias;
	}

	public void setTempoCumpridoUltimoEventoDias(String tempoCumpridoUltimoEventoDias) {
		this.tempoCumpridoUltimoEventoDias = tempoCumpridoUltimoEventoDias;
		setTempoCumpridoUltimoEventoAnos(tempoCumpridoUltimoEventoDias);
	}

	public String getTempoCumpridoUltimoEventoAnos() {
		return this.tempoCumpridoUltimoEventoAnos;
	}

	public void setTempoCumpridoUltimoEventoAnos(String tempoEmDias) {
		this.tempoCumpridoUltimoEventoAnos = getTempoNegativoFormatado(tempoEmDias);
	}

	public String getTempoRestanteUltimoEventoDias() {
		return this.tempoRestanteUltimoEventoDias;
	}

	public void setTempoRestanteUltimoEventoDias(String tempoRestanteUltimoEventoDias) {
		this.tempoRestanteUltimoEventoDias = tempoRestanteUltimoEventoDias;
		setTempoRestanteUltimoEventoAnos(tempoRestanteUltimoEventoDias);
	}

	public String getTempoRestanteUltimoEventoAnos() {
		return this.tempoRestanteUltimoEventoAnos;
	}

	public void setTempoRestanteUltimoEventoAnos(String tempoEmDias) {
		this.tempoRestanteUltimoEventoAnos = getTempoNegativoFormatado(tempoEmDias);
	}

	public String getTempoRestanteDataAtualDias() {
		return this.tempoRestanteDataAtualDias;
	}

	public void setTempoRestanteDataAtualDias(String tempoRestanteDataAtualDias) {
		this.tempoRestanteDataAtualDias = tempoRestanteDataAtualDias;
		setTempoRestanteDataAtualAnos(tempoRestanteDataAtualDias);
	}

	public String getTempoRestanteDataAtualAnos() {
		return this.tempoRestanteDataAtualAnos;
	}

	public void setTempoRestanteDataAtualAnos(String tempoEmDias) {
		this.tempoRestanteDataAtualAnos = getTempoNegativoFormatado(tempoEmDias);
	}

	public String getTotalDiasTrabalhados() {
		if (this.totalDiasTrabalhados.length() == 0) this.totalDiasTrabalhados = "00";
		return this.totalDiasTrabalhados;
	}

	public void setTotalDiasTrabalhados(String totalDiasTrabalhados) {
		this.totalDiasTrabalhados = totalDiasTrabalhados;
	}

	public String getTotalHorasEstudo() {
		if (this.totalHorasEstudo.length() == 0) this.totalHorasEstudo = "00";
		return this.totalHorasEstudo;
	}

	public void setTotalHorasEstudo(String totalHorasEstudo) {
		this.totalHorasEstudo = totalHorasEstudo;
	}

	public String getTempoTotalRemicaoTrabalhoDias() {
		return this.tempoTotalRemicaoTrabalhoDias;
	}

	public void setTempoTotalRemicaoTrabalhoDias(String tempoEmDias) {
		this.tempoTotalRemicaoTrabalhoDias = tempoEmDias;
		setTempoTotalRemicaoTrabalhoAnos(tempoEmDias);
	}

	public String getTempoTotalRemicaoTrabalhoAnos() {
		return this.tempoTotalRemicaoTrabalhoAnos;
	}

	public void setTempoTotalRemicaoTrabalhoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoTotalRemicaoTrabalhoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoTotalRemicaoTrabalhoAnos = "";
	}

	public String getTempoTotalRemicaoEstudoDias() {
		return this.tempoTotalRemicaoEstudoDias;
	}

	public void setTempoTotalRemicaoEstudoDias(String tempoEmDias) {
		this.tempoTotalRemicaoEstudoDias = tempoEmDias;
		setTempoTotalRemicaoEstudoAnos(tempoEmDias);
	}

	public String getTempoTotalRemicaoEstudoAnos() {
		return this.tempoTotalRemicaoEstudoAnos;
	}

	public void setTempoTotalRemicaoEstudoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoTotalRemicaoEstudoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoTotalRemicaoEstudoAnos = "";
	}

	public String getTempoTotalRemicaoLeituraDias() {
		return this.tempoTotalRemicaoLeituraDias;
	}
	
	public void setTempoTotalRemicaoLeituraDias(String tempoEmDias) {
		this.tempoTotalRemicaoLeituraDias = tempoEmDias;
		setTempoTotalRemicaoLeituraAnos(tempoEmDias);
	}

	public String getTempoTotalRemicaoLeituraAnos() {
		return this.tempoTotalRemicaoLeituraAnos;
	}

	public void setTempoTotalRemicaoLeituraAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoTotalRemicaoLeituraAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoTotalRemicaoLeituraAnos = "";
	}
	
	public String getInformacaoAdicional() {
		return informacaoAdicional;
	}

	public void setInformacaoAdicional(String informacaoAdicional) {
		if (informacaoAdicional != null) if (informacaoAdicional.equalsIgnoreCase("null")) this.informacaoAdicional = "";
		else this.informacaoAdicional = informacaoAdicional;
	}

	public String getDataValidadeMandadoPrisaoUnificada() {
		return dataValidadeMandadoPrisaoUnificada;
	}

	public void setDataValidadeMandadoPrisaoUnificada(String dataValidadeMandadoPrisao) {
		this.dataValidadeMandadoPrisaoUnificada = dataValidadeMandadoPrisao;
	}
	
	public String getDataValidadeMandadoPrisaoIndividual() {
		return dataValidadeMandadoPrisaoIndividual;
	}

	public void setDataValidadeMandadoPrisaoIndividual(
			String dataValidadeMandadoPrisaoIndividual) {
		this.dataValidadeMandadoPrisaoIndividual = dataValidadeMandadoPrisaoIndividual;
	}

	public String getId() {
		return idCalculoLiquidacao;
	}

	public void setId(String idCalculoLiquidacao) {
		this.idCalculoLiquidacao = idCalculoLiquidacao;
	}
	
	public String getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}
	
	public String getIdUsuarioServentia() {
		return idUsuarioServentia;
	}

	public void setIdUsuarioServentia(String idUsuarioServentia) {
		this.idUsuarioServentia = idUsuarioServentia;
	}
	
	public byte[] getRelatorioByte() {
		return this.relatorioByte;	
	}

	public void setRelatorioByte(byte[] bytes) {
		this.relatorioByte = bytes;
	}

	public List getListaPrescricaoExecutoria() {
		return this.listaPrescricaoExecutoria;
	}

	public void setListaPrescricaoExecutoria(List lista) {
		this.listaPrescricaoExecutoria = lista;
	}

	public void addListaPrescricaoExecutoria(CalculoLiquidacaoPrescricaoDt calculoPrescricaoDt) {
		if (this.listaPrescricaoExecutoria == null) this.listaPrescricaoExecutoria = new ArrayList();
		this.listaPrescricaoExecutoria.add(calculoPrescricaoDt);
	}
	
	public List getListaPrescricaoPunitiva() {
		return this.listaPrescricaoPunitiva;
	}

	public void setListaPrescricaoPunitiva(List lista) {
		this.listaPrescricaoPunitiva = lista;
	}

	public void addListaPrescricaoPunitiva(CalculoLiquidacaoPrescricaoDt calculoPrescricaoDt) {
		if (this.listaPrescricaoPunitiva == null) this.listaPrescricaoPunitiva = new ArrayList();
		this.listaPrescricaoPunitiva.add(calculoPrescricaoDt);
	}
	
	public String getArtigoPrescricaoPunitiva() {
		return artigoPrescricaoPunitiva;
	}

	public void setArtigoPrescricaoPunitiva(String textoPrescricaoPunitiva) {
		this.artigoPrescricaoPunitiva = textoPrescricaoPunitiva;
	}
	
	public void addArtigoPrescricaoPunitiva(String texto){
		this.artigoPrescricaoPunitiva += texto;
	}

	public String getArtigoPrescricaoExecutoria() {
		return artigoPrescricaoExecutoria;
	}

	public void setArtigoPrescricaoExecutoria(String textoPrescricaoExecutoria) {
		this.artigoPrescricaoExecutoria = textoPrescricaoExecutoria;
	}
	
	public void addArtigoPrescricaoExecutoria(String texto){
		this.artigoPrescricaoExecutoria += texto;
	}
	
	public String getArtigoPrescricaoExecutoriaUnificada() {
		return artigoPrescricaoExecutoriaUnificada;
	}

	public void setArtigoPrescricaoExecutoriaUnificada(String textoPrescricaoExecutoria) {
		this.artigoPrescricaoExecutoriaUnificada = textoPrescricaoExecutoria;
	}
	
	public void addArtigoPrescricaoExecutoriaUnificada(String texto){
		this.artigoPrescricaoExecutoriaUnificada += texto;
	}
	
	public String getTempoPrescricaoUnificadaAnos() {
		return tempoPrescricaoUnificadaAnos;
	}

	public void setTempoPrescricaoUnificadaAnos(String tempoPrescricaoUnificadaAnos) {
		this.tempoPrescricaoUnificadaAnos = tempoPrescricaoUnificadaAnos;
	}

	public String getDataUltimaFuga() {
		return dataUltimaFuga;
	}

	public void setDataUltimaFuga(String dataUltimaFuga) {
		this.dataUltimaFuga = dataUltimaFuga;
	}

	public void copiar(CalculoLiquidacaoDt objeto){
		idCalculoLiquidacao = objeto.getId();
		idProcesso = objeto.getIdProcesso();
		dataCalculo = objeto.getDataCalculo();
		if (this.getCalculoProgressaoLivramentoDt() != null) {
			this.getCalculoProgressaoLivramentoDt().setDataRequisitoTemporalLivramento(objeto.getCalculoProgressaoLivramentoDt().getDataRequisitoTemporalLivramento());
			this.getCalculoProgressaoLivramentoDt().setDataRequisitoTemporalProgressao(objeto.getCalculoProgressaoLivramentoDt().getDataRequisitoTemporalProgressao());
		}
		dataTerminoPena = objeto.getDataTerminoPena();
		dataValidadeMandadoPrisaoUnificada = objeto.getDataValidadeMandadoPrisaoUnificada();
		artigoPrescricaoPunitiva = objeto.getArtigoPrescricaoPunitiva();
		artigoPrescricaoExecutoria = objeto.getArtigoPrescricaoExecutoria();
		informacaoAdicional = objeto.getInformacaoAdicional();
		CodigoTemp = objeto.getCodigoTemp();
		idUsuarioServentia = objeto.getIdUsuarioServentia();
		relatorioByte = objeto.getRelatorioByte();
	}

	public String getPropriedades(){
		String dataRequisitoProgressao = "";
		String dataRequisitoLivramento = "";
		if (this.getCalculoProgressaoLivramentoDt() != null){
			dataRequisitoProgressao = this.getCalculoProgressaoLivramentoDt().getDataRequisitoTemporalProgressao();
			dataRequisitoLivramento = this.getCalculoProgressaoLivramentoDt().getDataRequisitoTemporalLivramento();
		}
		return "[idCalculoLiquidacao:" + idCalculoLiquidacao +
				";idProcesso:" + idProcesso +
				";dataCalculo:" + dataCalculo +
				";dataRequisitoProgressao:" + dataRequisitoProgressao +
				";dataRequisitoLivramento:" + dataRequisitoLivramento + 
				";dataTerminoPena:" + dataTerminoPena + 
				";dataValidadeMandadoPrisao:" + dataValidadeMandadoPrisaoUnificada +
				";artigoPrescricaoPunitiva:" + artigoPrescricaoPunitiva +
				";artigoPrescricaoExecutoria:" + artigoPrescricaoExecutoria +
				";idUsuarioServentia:" + idUsuarioServentia +
				";informacaoAdicional:" + informacaoAdicional + "]";
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

	public CalculoLiquidacaoProgressaoLivramentoDt getCalculoProgressaoLivramentoDt() {
		return calculoProgressaoLivramentoDt;
	}

	public void setCalculoProgressaoLivramentoDt(CalculoLiquidacaoProgressaoLivramentoDt calculoDt) {
		this.calculoProgressaoLivramentoDt = calculoDt;
	}
	
	public void newCalculoProgressaoLivramentoDt(){
		if(this.calculoProgressaoLivramentoDt == null) this.calculoProgressaoLivramentoDt = new CalculoLiquidacaoProgressaoLivramentoDt();
	}

	public CalculoLiquidacaoComutacaoDt getCalculoComutacaoDt() {
		return calculoComutacaoDt;
	}

	public void setCalculoComutacaoDt(
			CalculoLiquidacaoComutacaoDt calculoComutacaoDt) {
		this.calculoComutacaoDt = calculoComutacaoDt;
	}
	
	public void newCalculoComutacaoDt(){
		if(this.calculoComutacaoDt == null) this.calculoComutacaoDt = new CalculoLiquidacaoComutacaoDt();
		if(this.listaComutacao == null) this.listaComutacao = new ArrayList();
		if(this.listaComutacaoUnificada == null) this.listaComutacaoUnificada = new ArrayList();
	}

	public List getListaComutacao() {
		return this.listaComutacao;
	}

	public void setListaComutacao(List lista) {
		this.listaComutacao = lista;
	}

	public void addListaComutacao(CalculoLiquidacaoComutacaoDt calculoComutacaoDt) {
		if (this.listaComutacao == null) this.listaComutacao = new ArrayList();
		this.listaComutacao.add(calculoComutacaoDt);
	}
	
	public List getListaComutacaoUnificada() {
		return this.listaComutacaoUnificada;
	}

	public void setListaComutacaoUnificada(List lista) {
		this.listaComutacaoUnificada = lista;
	}

	public void addListaComutacaoUnificada(CalculoLiquidacaoComutacaoDt calculoComutacaoDt) {
		if (this.listaComutacaoUnificada == null) this.listaComutacaoUnificada = new ArrayList();
		this.listaComutacaoUnificada.add(calculoComutacaoDt);
	}
	
	public void newCalculoPrescricaoDt(){
		if(this.listaPrescricaoExecutoria == null) this.listaPrescricaoExecutoria = new ArrayList();
		if(this.listaPrescricaoPunitiva == null) this.listaPrescricaoPunitiva = new ArrayList();
	}

	public CalculoLiquidacaoIndultoDt getCalculoIndultoDt() {
		return calculoIndultoDt;
	}

	public void setCalculoIndultoDt(CalculoLiquidacaoIndultoDt calculoIndultoDt) {
		this.calculoIndultoDt = calculoIndultoDt;
	}
	
	public void newCalculoIndultoDt(){
		if(this.calculoIndultoDt == null) this.calculoIndultoDt = new CalculoLiquidacaoIndultoDt();
	}

	public CalculoLiquidacaoPenaRestritivaDt getCalculoPenaRestritivaDt() {
		return calculoPenaRestritivaDt;
	}

	public void setCalculoPenaRestritivaDt(
			CalculoLiquidacaoPenaRestritivaDt calculoPenaRestritiva) {
		this.calculoPenaRestritivaDt = calculoPenaRestritiva;
	}
	
	public void newCalculoPenaRestritivaDt(){
		if(this.calculoPenaRestritivaDt == null) this.calculoPenaRestritivaDt = new CalculoLiquidacaoPenaRestritivaDt();
	}

	public List getListaSursis() {
		return this.listaSursis;
	}

	public void setListaSursis(List lista) {
		this.listaSursis = lista;
	}

	public void addListaSursis(CalculoLiquidacaoSursisDt calculoSursisDt) {
		if (this.listaSursis == null) this.listaSursis = new ArrayList();
		this.listaSursis.add(calculoSursisDt);
	}
	
	public String getTipoRemicao() {
		return tipoRemicao;
	}

	public void setTipoRemicao(String tipoRemicao) {
		this.tipoRemicao = tipoRemicao;
	}

	public String getVisualizaPenaUnificada() {
		return visualizaPenaUnificada;
	}

	public void setVisualizaPenaUnificada(String visualizaPenaUnificada) {
		this.visualizaPenaUnificada = visualizaPenaUnificada;
	}

	public String getVisualizaRestantePenaUltimoEvento() {
		return visualizaRestantePenaUltimoEvento;
	}

	public void setVisualizaRestantePenaUltimoEvento(
			String visualizaRestantePenaUltimoEvento) {
		this.visualizaRestantePenaUltimoEvento = visualizaRestantePenaUltimoEvento;
	}

	public String getQtdeTempoHorasEstudo() {
		return qtdeTempoHorasEstudo;
	}

	public void setQtdeTempoHorasEstudo(String tempoHorasEstudo) {
		if (tempoHorasEstudo.length() == 0) tempoHorasEstudo = "12"; //valor padrão
		else this.qtdeTempoHorasEstudo = tempoHorasEstudo;
	}

	public boolean isIniciouCumprimentoPena() {
		return iniciouCumprimentoPena;
	}

	public void setIniciouCumprimentoPena(boolean iniciouCumprimentoPena) {
		this.iniciouCumprimentoPena = iniciouCumprimentoPena;
	}
	
	public boolean isMulherComMenores() {
		return mulherComMenores;
	}

	public void setMulherComMenores(boolean mulherComMenores) {
		this.mulherComMenores = mulherComMenores;
	}

	public boolean isSaldoDevedorCrimeHediondo() {
		return saldoDevedorCrimeHediondo;
	}

	public void setSaldoDevedorCrimeHediondo(boolean saldoDevedorCrimeHediondo) {
		this.saldoDevedorCrimeHediondo = saldoDevedorCrimeHediondo;
	}

	public boolean isConsiderarDetracao() {
		return considerarDetracao;
	}

	public void setConsiderarDetracao(boolean considerarDetracao) {
		this.considerarDetracao = considerarDetracao;
	}

	public String getCalcularSomente() {
		return calcularSomente;
	}

	public void setCalcularSomente(String calcularSomente) {
		this.calcularSomente = calcularSomente;
	}

	public String getFormaCumprimento() {
		return formaCumprimento;
	}

	public void setFormaCumprimento(String formaCumprimento) {
		this.formaCumprimento = formaCumprimento;
	}
}
