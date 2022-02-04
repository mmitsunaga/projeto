package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;


//---------------------------------------------------------
public class ProcessoEventoExecucaoDt extends ProcessoEventoExecucaoDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = -4244735876506622717L;

    public static final int CodigoPermissao = 414;

	private EventoLocalDt eventoLocalDt;
	private EventoRegimeDt eventoRegimeDt;
	private EventoExecucaoDt eventoExecucaoDt;

	private String condenacaoAnos;
	private String condenacaoDias;

	private String tempoCumpridoAnos;
	private String tempoCumpridoDias;
	
	private String movimentacaoDataRealizacaoTipo;
	//ConsiderarLivramentoCondicional:
	//0: Não (fato novo) - 1/1, zera o tempo cumprido do livramento condicional
	//1: Sim (Crime anterior ao LC) - fração, considera o tempo cumprido do livramento condicional
	//2: Não (por descumprimento) - 1/1, considera o tempo cumprido do livramento condicional
	
	private boolean boManterAcaoPenal;
	
	private String dataInicioCumpirmentoPena; //utilizado para montagem da lista de eventos
	private String dataPrisaoRevogacaoLC;
	
	//Lista de HashMap com as chaves: Id_ProcessoExecucao, DataTransitoJulgado, Id_CondenacaoExecucaoSituacao, 
	// TempoNaoExtintoDias, TempoExtintoDias, TempoCondenacao, Checked, Fracao, Id_TransitoJulgadoEvento, 
	// TempoTotalDias, TempoTotalAnos, TempoExtintoAnos, TempoNaoExtintoAnos
	private List listaTJ;
	
	private String DataInicioLivramentoCondicional;
	private String usuarioAlteracao;
	private String ObservacaoAux;
	private String dataDecretoComutacao;
	private String observacaoVisualizada;
	private String dataInicioSursis;
	private String idEventoPai;

	private boolean isUsouTempoCumprido; //utilizado no cálculo para verificar se já utilizou o tempo cumprido.
	
	private String tempoAno;
	private String tempoMes;
	private String tempoDia;
	
	// EXECPEN
	//variáveis auxiliares para cálculo do cumprimento de pena
	//Autor: Leonardo
	private int tempoCumpridoCalculadoDias;
	
	public ProcessoEventoExecucaoDt() {
		limpar();
	}

	public void limpar() {
		super.limpar();
		condenacaoAnos = "";
		condenacaoDias = "";
		tempoCumpridoAnos = "";
		tempoCumpridoDias = "";
		movimentacaoDataRealizacaoTipo = "";
		eventoLocalDt = new EventoLocalDt();
		eventoRegimeDt = new EventoRegimeDt();
		eventoExecucaoDt = new EventoExecucaoDt();
		boManterAcaoPenal = false;
		dataInicioCumpirmentoPena = "";
		dataPrisaoRevogacaoLC = "";
		listaTJ = null;
		usuarioAlteracao = "";
		ObservacaoAux = "";
		dataDecretoComutacao = "";
		observacaoVisualizada = "";
		isUsouTempoCumprido = false;
		dataInicioSursis = "";
		idEventoPai = "";
		
		tempoAno = "";
		tempoMes = "";
		tempoDia = "";
		tempoCumpridoCalculadoDias = 0;
	}

	public EventoLocalDt getEventoLocalDt() {
		return eventoLocalDt;
	}

	public void setEventoLocalDt(EventoLocalDt eventoLocalDt) {
		this.eventoLocalDt = eventoLocalDt;
	}

	public EventoRegimeDt getEventoRegimeDt() {
		return eventoRegimeDt;
	}

	public void setEventoRegimeDt(EventoRegimeDt eventoRegimeDt) {
		this.eventoRegimeDt = eventoRegimeDt;
	}

	public String getEventoExecucaoStatus(){
		String stTemp="";
		if (eventoExecucaoDt!=null){
			stTemp = eventoExecucaoDt.getEventoExecucaoStatus();
		}
		return  stTemp;
	}
	
	public String getId_EventoExecucaoStatus(){
		String stTemp="";
		if (eventoExecucaoDt!=null){
			stTemp = eventoExecucaoDt.getId_EventoExecucaoStatus();
		}
		return  stTemp;
	}
	public EventoExecucaoDt getEventoExecucaoDt() {
		return eventoExecucaoDt;
	}

	public void setEventoExecucaoDt(EventoExecucaoDt eventoExecucaoDt) {
		this.eventoExecucaoDt = eventoExecucaoDt;
	}

	public String getCondenacaoAnos() {
		return condenacaoAnos;
	}

	public void setCondenacaoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.condenacaoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.condenacaoAnos = "";
	}
	public void setCondenacaoAnosComutacao(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.condenacaoAnos = "(" + Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias)) + ")";
		else this.condenacaoAnos = "";
	}

	public String getCondenacaoDias() {
		return condenacaoDias;
	}

	public void setCondenacaoDias(String tempoEmDias) {
		this.condenacaoDias = tempoEmDias;
		setCondenacaoAnos(tempoEmDias);
	}

	public String getTempoCumpridoAnos() {
		return tempoCumpridoAnos;
	}

	public void setTempoCumpridoAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoCumpridoAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoCumpridoAnos = "";
	}
	
	public void setTempoCumpridoAnos_TempoEmAnos(String tempoEmAnos) {
		if (tempoEmAnos.length() > 0) this.tempoCumpridoAnos = tempoEmAnos;
		else this.tempoCumpridoAnos = "";
	}
	
	public String getTempoCumpridoDias() {
		return tempoCumpridoDias;
	}

	public void setTempoCumpridoDias(String tempoEmDias) {
		this.tempoCumpridoDias = tempoEmDias;
		setTempoCumpridoAnos(tempoEmDias);
	}
	
	public String getMovimentacaoDataRealizacaoTipo() {
		return movimentacaoDataRealizacaoTipo;
	}

	public void setMovimentacaoDataRealizacaoTipo(String movimentacaoDataRealizacaoTipo) {
		if (movimentacaoDataRealizacaoTipo!=null) this.movimentacaoDataRealizacaoTipo = movimentacaoDataRealizacaoTipo;
	}
	
    public void setConsiderarTempoLivramentoCondicional(String valor) {
    	if (valor != null) 
    		if (valor.equalsIgnoreCase("null")) {
    			super.setConsiderarTempoLivramentoCondicional("0");
    	} else{
    		super.setConsiderarTempoLivramentoCondicional(valor);
    	}
    }

    public boolean isManterAcaoPenal() {
		return boManterAcaoPenal;
	}

    public void setManterAcaoPenal(String valor) {
    	if (valor != null) 
    		if (valor.equalsIgnoreCase("null")) {
    			boManterAcaoPenal = false;
    	} else{
            if (valor.equalsIgnoreCase("true") || valor.equalsIgnoreCase("1"))
                boManterAcaoPenal = true;
            else
                boManterAcaoPenal = false;
    	}
    }
    
	public String getDataInicioCumpirmentoPena() {
		return dataInicioCumpirmentoPena;
	}

	public void setDataInicioCumpirmentoPena(String dataInicioCumpirmentoPena) {
		this.dataInicioCumpirmentoPena = dataInicioCumpirmentoPena;
	}
	
	public List getListaTJ() {
		return listaTJ;
	}

	public void setListaTJ(List listaTJ) {
		this.listaTJ = listaTJ;
	}
	
	public void addListaTJ(HashMap map) {
		if (listaTJ == null) listaTJ = new ArrayList();
		this.listaTJ.add(map);
	}

	public String getQuantidadeAnos() {
		if (getQuantidade().length() > 0) 
			return Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(getQuantidade()));
		else return "";
	}

	public String getDataInicioLivramentoCondicional()  {return DataInicioLivramentoCondicional;}
	public void setDataInicioLivramentoCondicional(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {DataInicioLivramentoCondicional = "";} else if (!valor.equalsIgnoreCase("")) DataInicioLivramentoCondicional = valor;}

	public String getDataDecretoComutacao() {
		return dataDecretoComutacao;
	}

	public void setDataDecretoComutacao(String dataDecretoComutacao) {
		if (dataDecretoComutacao!=null) this.dataDecretoComutacao = dataDecretoComutacao;
	}

	public String getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(String usuarioAlteracao) {
		if (usuarioAlteracao != null) this.usuarioAlteracao = usuarioAlteracao;
	}

	public String getObservacaoAux() {
		return ObservacaoAux;
	}

	public void setObservacaoAux(String observacaoAux) {
		ObservacaoAux = observacaoAux;
	}
	
	public String getDataPrisaoRevogacaoLC() {
		return dataPrisaoRevogacaoLC;
	}

	public void setDataPrisaoRevogacaoLC(String dataPrisaoRevogacaoLC) {
		if (dataPrisaoRevogacaoLC!=null) this.dataPrisaoRevogacaoLC = dataPrisaoRevogacaoLC;
	}

	public String getObservacaoVisualizada() {
		return observacaoVisualizada;
	}

	public void setObservacaoVisualizada(String observacaoVisualizada) {
		this.observacaoVisualizada = observacaoVisualizada;
	}

	public boolean isUsouTempoCumprido() {
		return isUsouTempoCumprido;
	}

	public void setUsouTempoCumprido(boolean isUsouTempoCumprido) {
		this.isUsouTempoCumprido = isUsouTempoCumprido;
	}

	public String getDataInicioSursis() {
		return dataInicioSursis;
	}

	public void setDataInicioSursis(String dataInicioSursis) {
		this.dataInicioSursis = dataInicioSursis;
	}

	public String getIdEventoPai() {
		return idEventoPai;
	}

	public void setIdEventoPai(String idEventoPai) {
		if (idEventoPai == null || idEventoPai.equalsIgnoreCase("null")) this.idEventoPai = "";
		else this.idEventoPai = idEventoPai;
	}

	public String getTempoAno() {
		return tempoAno;
	}

	public void setTempoAno(String tempoAno) {
		if (tempoAno != null)
			this.tempoAno = tempoAno;
	}

	public String getTempoMes() {
		return tempoMes;
	}

	public void setTempoMes(String tempoMes) {
		if (tempoMes != null)
			this.tempoMes = tempoMes;
	}

	public String getTempoDia() {
		return tempoDia;
	}

	public void setTempoDia(String tempoDia) {
		if (tempoDia != null)
			this.tempoDia = tempoDia;
	}

	public void setQuantidade(String valor ) {
		if (valor!=null) super.setQuantidade(valor);
		setTempoPenaEmAnos(valor);
	}
	
	public void setTempoPenaEmAnos(String tempoEmDias) {
		String tempoEmAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		String[] tempoAnos = tempoEmAnos.split(" - ");
        this.setTempoAno(tempoAnos[0]);
        this.setTempoMes(tempoAnos[1]);
        this.setTempoDia(tempoAnos[2]);
	}

	public int getTempoCumpridoCalculadoDias() {
		return tempoCumpridoCalculadoDias;
	}

	public void setTempoCumpridoCalculadoDias(int tempoCumpridoCalculadoDias) {
		this.tempoCumpridoCalculadoDias = tempoCumpridoCalculadoDias;
	}
}
