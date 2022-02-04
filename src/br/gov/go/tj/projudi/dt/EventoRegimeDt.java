package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class EventoRegimeDt extends EventoRegimeDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = 5199297607326838078L;
    public static final int CodigoPermissao=2248;
//

    private RegimeExecucaoDt regimeExecucaoDt;
    
	public EventoRegimeDt() {
		limpar();
		regimeExecucaoDt = new RegimeExecucaoDt();
	}

	public EventoRegimeDt(String idEventoRegime, String idRegimeExecucao, String idProcessoEventoExecucao){
		this.setId_RegimeExecucao(idRegimeExecucao);
		this.setId(idEventoRegime);
		this.setId_ProcessoEventoExecucao(idProcessoEventoExecucao);
	}

	public RegimeExecucaoDt getRegimeExecucaoDt() {
		return regimeExecucaoDt;
	}

	public void setRegimeExecucaoDt(RegimeExecucaoDt regimeExecucaoDt) {
		this.regimeExecucaoDt = regimeExecucaoDt;
	}
	
}
