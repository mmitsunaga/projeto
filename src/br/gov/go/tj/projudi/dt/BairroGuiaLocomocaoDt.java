package br.gov.go.tj.projudi.dt;

public class BairroGuiaLocomocaoDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8321954144566220202L;
	
	private BairroDt Bairro;
	private int Quantidade;
	private String Id_Finalidade;
	private String Finalidade = "";
	private boolean penhora;
	private boolean intimacao;
	private boolean oficialCompanheiro;
	
	private OficialSPGDt oficialSPGDt_Principal;
	private OficialSPGDt oficialSPGDt_Companheiro;
	private String idMandadoJudicial;

	public BairroDt getBairroDt() {
		return Bairro;
	}

	public void setBairroDt(BairroDt bairro) {
		if (bairro != null) {
			this.Bairro = bairro;
		}
	}

	public int getQuantidade() {
		return this.Quantidade;
	}

	public void setQuantidade(int Quantidade) {
		this.Quantidade = Quantidade;
	}
	
	public String getId_Finalidade() {		
		if( Id_Finalidade == null )
			Id_Finalidade = "";
		return Id_Finalidade;
	}

	public void setId_Finalidade(String id_finalidade) {
		Id_Finalidade = id_finalidade;
	}

	public String getFinalidade() {
		return Finalidade;
	}

	public void setFinalidade(String finalidade) {
		Finalidade = finalidade;
	}
	
	public boolean isPenhora() {
		return penhora;
	}
	
	public void setPenhora(boolean penhora) {
		this.penhora = penhora;
	}
	
	public void setPenhora(String penhora) {
		if (penhora != null && !penhora.trim().equalsIgnoreCase("null") && penhora.trim().length() > 0)
			this.penhora = penhora.trim().equalsIgnoreCase(GuiaLocomocaoDt.VALOR_SIM);
	}

	public boolean isIntimacao() {
		return intimacao;
	}

	public void setIntimacao(boolean intimacao) {
		this.intimacao = intimacao;
	}
	
	public void setIntimacao(String intimacao) {
		if (intimacao != null && !intimacao.trim().equalsIgnoreCase("null") && intimacao.trim().length() > 0)
			this.intimacao = intimacao.trim().equalsIgnoreCase(GuiaLocomocaoDt.VALOR_SIM);
	}
	
	public boolean isOficialCompanheiro() {
		return oficialCompanheiro;
	}

	public void setOficialCompanheiro(boolean oficialCompanheiro) {
		this.oficialCompanheiro = oficialCompanheiro;
	}
	
	public void setOficialCompanheiro(String oficialCompanheiro) {
		if (oficialCompanheiro != null && !oficialCompanheiro.trim().equalsIgnoreCase("null") && oficialCompanheiro.trim().length() > 0)
			this.oficialCompanheiro = oficialCompanheiro.trim().equalsIgnoreCase(GuiaLocomocaoDt.VALOR_SIM);
	}
	
	public OficialSPGDt getOficialSPGDt_Principal() {
		return oficialSPGDt_Principal;
	}

	public void setOficialSPGDt_Principal(OficialSPGDt oficialSPGDt_Principal) {
		this.oficialSPGDt_Principal = oficialSPGDt_Principal;
	}

	public OficialSPGDt getOficialSPGDt_Companheiro() {
		return oficialSPGDt_Companheiro;
	}

	public void setOficialSPGDt_Companheiro(OficialSPGDt oficialSPGDt_Companheiro) {
		this.oficialSPGDt_Companheiro = oficialSPGDt_Companheiro;
	}

	public String getIdMandadoJudicial() {
		return idMandadoJudicial;
	}

	public void setIdMandadoJudicial(String idMandadoJudicial) {
		this.idMandadoJudicial = idMandadoJudicial;
	}

	public void copiar(BairroGuiaLocomocaoDt objeto){
		if (objeto==null) return;		
		Bairro = objeto.getBairroDt();
		Quantidade = objeto.getQuantidade();
		Id_Finalidade = objeto.getId_Finalidade();
		Finalidade = objeto.getFinalidade();
		this.penhora = objeto.isPenhora();
		this.intimacao = objeto.isIntimacao();
		this.oficialCompanheiro = objeto.isOficialCompanheiro();
		this.oficialSPGDt_Principal = objeto.getOficialSPGDt_Principal();
		this.oficialSPGDt_Companheiro = objeto.getOficialSPGDt_Companheiro();
		this.idMandadoJudicial = objeto.getIdMandadoJudicial();
	}

	public void limpar(){
		super.limpar();
		Bairro = null;
		Quantidade = 0;		
	}

	@Override
	public void setId(String id) {
				
	}

	@Override
	public String getId() {
		if (this.Bairro == null || this.Bairro.getId() == null) return "";
		return this.Bairro.getId();
	}
}