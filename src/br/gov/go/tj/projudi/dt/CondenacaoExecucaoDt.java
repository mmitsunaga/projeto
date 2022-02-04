package br.gov.go.tj.projudi.dt;

import java.util.HashMap;
import java.util.Map;

import br.gov.go.tj.utils.Funcoes;


//---------------------------------------------------------
public class CondenacaoExecucaoDt extends CondenacaoExecucaoDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = -1124886170437516370L;

    public static final int CodigoPermissao=522;
	
    
	private String tempoPenaEmDias; 
	private String tempoPenaEmAnos;
	private boolean hediondoProgressao;
	private boolean hediondoLivramento;
	private boolean equiparaHediondoLivramento;
	private boolean boReincidente;
	private String dataTransitoJulgado;
	private String dataInicioCumprimentoPena;
	private String tempoPenaRemanescenteEmDias;
	private String tempoPenaRemanescenteEmAnos;
	private String tempoCumpridoExtintoDias;
	
	//variáveis auxiliares na edição da condenação
	private String qtdeAno;
	private String qtdeMes;
	private String qtdeDias;
	
	//variavel auxiliar para calculo de prescricao
	private Map<String, Boolean> isEmFuga = new HashMap<String, Boolean>();
	
	//sobrescrevendo a variável do gen, pois para utilizar a interface para geração do pdf, o atributo deve iniciar com letra minúscula
	//private String condenacaoExecucaoSituacao;
	//private String crimeExecucao;
	//private String dataFato;
	
	public CondenacaoExecucaoDt(){
		this.limpar();
	}
	
	public void limpar(){
		super.limpar();
		tempoPenaEmAnos = "";
		tempoPenaEmDias = "";
		hediondoLivramento = false;
		hediondoProgressao = false;
		equiparaHediondoLivramento = false;
		boReincidente = false;
		dataTransitoJulgado = "";
		dataInicioCumprimentoPena = "";
		//condenacaoExecucaoSituacao = super.getCondenacaoExecucaoSituacao();
		//crimeExecucao = super.getCrimeExecucao();
		//dataFato = super.getDataFato();
		tempoPenaRemanescenteEmDias = "";
		tempoPenaRemanescenteEmAnos = "";
		qtdeAno = "";
		qtdeMes = "";
		qtdeDias = "";
		tempoCumpridoExtintoDias = "";
		isEmFuga = new HashMap<String, Boolean>();
	}

	public String getTempoPenaEmDias() {
		return tempoPenaEmDias;
	}

	public void setTempoPenaEmDias(String tempoPenaEmDias) {
		if (tempoPenaEmDias != null) if (tempoPenaEmDias.equalsIgnoreCase("null")) this.tempoPenaEmDias = "";
		else if (!tempoPenaEmDias.equalsIgnoreCase("")) this.tempoPenaEmDias = tempoPenaEmDias;
		setTempoPenaEmAnos(this.tempoPenaEmDias);
		setTempoPena(tempoPenaEmDias);
	}

	public String getTempoPenaEmAnos() {
		return tempoPenaEmAnos;
	}

	public void setTempoPenaEmAnos(String tempoEmDias) {
		if (tempoEmDias.length() > 0) this.tempoPenaEmAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoEmDias));
		else this.tempoPenaEmAnos = "";
		String[] tempoAnos = this.tempoPenaEmAnos.split(" - ");
        this.setQtdeAno(tempoAnos[0]);
        this.setQtdeMes(tempoAnos[1]);
        this.setQtdeDias(tempoAnos[2]);
	}

	public String getTempoPenaRemanescenteEmDias() {
		return tempoPenaRemanescenteEmDias;
	}

	public void setTempoPenaRemanescenteEmDias(String tempoPenaRemanescenteEmDias) {
		this.tempoPenaRemanescenteEmDias = tempoPenaRemanescenteEmDias;
		setTempoPenaRemanescenteEmAnos(tempoPenaRemanescenteEmDias);
	}

	public String getTempoPenaRemanescenteEmAnos() {
		return tempoPenaRemanescenteEmAnos;
	}

	public void setTempoPenaRemanescenteEmAnos(String tempoPenaRemanescenteEmDias) {
		if (tempoPenaRemanescenteEmDias.length() > 0) this.tempoPenaRemanescenteEmAnos = Funcoes.converterParaAnoMesDia(Funcoes.StringToInt(tempoPenaRemanescenteEmDias));
		else this.tempoPenaRemanescenteEmAnos = "";
	}

	public boolean isHediondoProgressao() {
		return hediondoProgressao;
	}

	public void setHediondoProgressao(boolean hediondoProgressao) {
		this.hediondoProgressao = hediondoProgressao;
	}

	public boolean isHediondoLivramento() {
		return hediondoLivramento;
	}

	public void setHediondoLivramento(boolean hediondoLivramento) {
		this.hediondoLivramento = hediondoLivramento;
	}

	public boolean isEquiparaHediondoLivramento() {
		return equiparaHediondoLivramento;
	}

	public void setEquiparaHediondoLivramento(boolean equiparaHediondoLivramento) {
		this.equiparaHediondoLivramento = equiparaHediondoLivramento;
	}
	
	public boolean isReincidente() {
		return boReincidente;
	}

    public void setReincidente(String valor) {
    	if (valor != null) 
    		if (valor.equalsIgnoreCase("null")) {
    			super.setReincidente("false");
    			boReincidente = false;
    	} else{
    		super.setReincidente(valor);
            if (valor.equalsIgnoreCase("true"))
                boReincidente = true;
            else
                boReincidente = false;
    	}
    }

	public String getDataTransitoJulgado() {
		return dataTransitoJulgado;
	}

	public void setDataTransitoJulgado(String dataTransitoJulgado) {
		this.dataTransitoJulgado = dataTransitoJulgado;
	}

	public String getCondenacaoExecucaoSituacao() {
		return super.getCondenacaoExecucaoSituacao();
	}

	public void setCondenacaoExecucaoSituacao(String condenacaoExecucaoSituacao) {
		super.setCondenacaoExecucaoSituacao(condenacaoExecucaoSituacao);
	}

	public String getCrimeExecucao() {
		return super.getCrimeExecucao();
	}

	public void setCrimeExecucao(String crimeExecucao) {
		super.setCrimeExecucao(crimeExecucao);
	}

	public String getDataFato() {
		return super.getDataFato();
	}

	public void setDataFato(String dataFato) {
		super.setDataFato(dataFato);
	}

	public String getQtdeAno() {
		return qtdeAno;
	}

	public void setQtdeAno(String qtde) {
		if (qtde != null) if (qtde.equalsIgnoreCase("null")) this.qtdeAno = "";
		else this.qtdeAno = qtde.trim();
	}
	
	public String getQtdeMes() {
		return qtdeMes;
	}

	public void setQtdeMes(String qtde) {
		if (qtde != null) if (qtde.equalsIgnoreCase("null")) this.qtdeMes = "";
		else this.qtdeMes = qtde.trim();
	}

	public String getQtdeDias() {
		return qtdeDias;
	}

	public void setQtdeDias(String qtde) {
		if (qtde != null) if (qtde.equalsIgnoreCase("null")) this.qtdeDias = "";
		else this.qtdeDias = qtde.trim();
	}

	public String getDataInicioCumprimentoPena() {
		return dataInicioCumprimentoPena;
	}

	public void setDataInicioCumprimentoPena(String dataInicioCumprimentoPena) {
		this.dataInicioCumprimentoPena = dataInicioCumprimentoPena;
	}

	public String getTempoCumpridoExtintoDias() {
		return tempoCumpridoExtintoDias;
	}

	public void setTempoCumpridoExtintoDias(String tempoCumpridoExtintoDias) {
		if (tempoCumpridoExtintoDias != null) if (tempoCumpridoExtintoDias.equalsIgnoreCase("null")) this.tempoCumpridoExtintoDias = "";
		else this.tempoCumpridoExtintoDias = tempoCumpridoExtintoDias.trim();
	}

	public boolean isEmFuga(ProcessoEventoExecucaoDt eventoFuga) {
		if(eventoFuga == null || !isEmFuga.containsKey(eventoFuga.getId()))
			return false;
	    return isEmFuga.get(eventoFuga.getId());
	}

	public void setEmFuga(ProcessoEventoExecucaoDt eventoFuga, boolean isEmFuga) {
		if(eventoFuga == null || eventoFuga.getId() == null)
			throw new IllegalArgumentException("Evento Fuga inconsistente");
	    this.isEmFuga.put(eventoFuga.getId(), isEmFuga);
	}
	
}

