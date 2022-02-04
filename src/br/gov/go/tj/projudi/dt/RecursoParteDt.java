package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class RecursoParteDt extends RecursoParteDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = 3165261180361751022L;

    public static final int CodigoPermissao = 439;

	private String ProcessoParteTipoCodigo;
	private ProcessoParteDt processoParteDt;
	private String descricaoPoloAtivo;
	private String descricaoPoloPassivo;
	private String ordemParte;

	public void limpar() {
		super.limpar();
		ProcessoParteTipoCodigo = "";
		processoParteDt = null;
		descricaoPoloAtivo = "";
		descricaoPoloPassivo = "";
	}

	public ProcessoParteDt getProcessoParteDt() {
		return processoParteDt;
	}

	public void setProcessoParteDt(ProcessoParteDt processoParteDt) {
		this.processoParteDt = processoParteDt;
	}

	public String getProcessoParteTipoCodigo() {
		return ProcessoParteTipoCodigo;
	}

	public void setProcessoParteTipoCodigo(String valor) {
		if (valor != null) ProcessoParteTipoCodigo = valor;
	}
	
	public String getDescricaoPoloAtivo() {
		if (descricaoPoloAtivo == null || descricaoPoloAtivo.trim().length() == 0) return "Recorrente";
		return descricaoPoloAtivo;
	}

	public void setDescricaoPoloAtivo(String descricaoPoloAtivo) {
		this.descricaoPoloAtivo = descricaoPoloAtivo;
	}

	public String getDescricaoPoloPassivo() {
		if (descricaoPoloPassivo == null || descricaoPoloPassivo.trim().length() == 0) return "Recorrido";
		return descricaoPoloPassivo;
	}

	public void setDescricaoPoloPassivo(String descricaoPoloPassivo) {
		this.descricaoPoloPassivo = descricaoPoloPassivo;
	}

	public String getOrdemParte() {
		return ordemParte;
	}

	public void setOrdemParte(String ordemParte) {
		this.ordemParte = ordemParte;
	}
}
