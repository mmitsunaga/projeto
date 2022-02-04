package br.gov.go.tj.projudi.dt;

public class GuiaItemDisponivelDt extends Dados {

	private static final long serialVersionUID = -7236706939190252141L;
	
	private String idGuiaItemDisponivel;
	private GuiaItemDt guiaItemDt;
	private PendenciaDt pendenciaDt;
	private MovimentacaoDt movimentacaoDt;
	private String dataPendencia;
	private String dataMovimentacao;

	@Override
	public void setId(String id) {
		idGuiaItemDisponivel = id;
	}

	@Override
	public String getId() {
		return idGuiaItemDisponivel;
	}

	public GuiaItemDt getGuiaItemDt() {
		return guiaItemDt;
	}

	public void setGuiaItemDt(GuiaItemDt guiaItemDt) {
		this.guiaItemDt = guiaItemDt;
	}

	public PendenciaDt getPendenciaDt() {
		return pendenciaDt;
	}

	public void setPendenciaDt(PendenciaDt pendenciaDt) {
		this.pendenciaDt = pendenciaDt;
	}

	public MovimentacaoDt getMovimentacaoDt() {
		return movimentacaoDt;
	}

	public void setMovimentacaoDt(MovimentacaoDt movimentacaoDt) {
		this.movimentacaoDt = movimentacaoDt;
	}

	public String getDataPendencia() {
		return dataPendencia;
	}

	public void setDataPendencia(String dataPendencia) {
		this.dataPendencia = dataPendencia;
	}
	
	public String getDataMovimentacao() {
		return dataMovimentacao;
	}

	public void setDataMovimentacao(String dataMovimentacao) {
		this.dataMovimentacao = dataMovimentacao;
	}
	
	public void copiar(GuiaItemDisponivelDt objeto){
		if (objeto==null) return;
		idGuiaItemDisponivel = objeto.getId();
		guiaItemDt = objeto.getGuiaItemDt();
		pendenciaDt = objeto.getPendenciaDt();
		movimentacaoDt = objeto.getMovimentacaoDt();
		dataPendencia = objeto.getDataPendencia();	
		dataMovimentacao = objeto.getDataMovimentacao();
	}
	
	@Override
	public String toString() {
		String propriedades = "[idGuiaItemDisponivel=" + idGuiaItemDisponivel + ", idGuiaItem=" + guiaItemDt.getId()
				+ ", dataPendencia=" + dataPendencia + ", dataMovimentacao=" + dataMovimentacao;

		if( pendenciaDt != null && pendenciaDt.getId() != null ) {
			propriedades += ", idPendencia=" + pendenciaDt.getId();
		}
		if( movimentacaoDt != null && movimentacaoDt.getId() != null ) {
			propriedades += ", idMovimentacao=" + movimentacaoDt.getId();
		}
		
		propriedades += "]";
		
		return propriedades;
	}
	
}
