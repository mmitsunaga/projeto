package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class EventoLocalDt extends EventoLocalDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = -8646431922842498851L;
    public static final int CodigoPermissao=2490;
//
	public EventoLocalDt(){
		this.limpar();
	}

	public EventoLocalDt(String idEventoLocal, String idLocalCumprimentoPena, String idProcessoEventoExecucao){
		this.setId_LocalCumprimentoPena(idLocalCumprimentoPena);
		this.setId(idEventoLocal);
		this.setId_ProcessoEventoExecucao(idProcessoEventoExecucao);
	}
}
