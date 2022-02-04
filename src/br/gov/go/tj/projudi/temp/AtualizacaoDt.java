package br.gov.go.tj.projudi.temp;

import br.gov.go.tj.projudi.dt.ArquivoDt;

//---------------------------------------------------------
public class AtualizacaoDt  extends ArquivoDt {

	/**
     * 
     */
    private static final long serialVersionUID = -222011769678943556L;

    public static final int CodigoPermissao = 143;
	
	private String id_Movimentacao;
	private String processoNumeroCompleto;
	private String tabelaOrigem;
	
	public AtualizacaoDt(){
		limpar();
	}
	
	public void limpar(){
		id_Movimentacao = "";
		processoNumeroCompleto = "";
		super.limpar();
	}
	
	public String getId_Movimentacao() {
		return id_Movimentacao;
	}
	
	public void setId_Movimentacao(String id_Movimentacao) {
		if (id_Movimentacao != null)
			this.id_Movimentacao = id_Movimentacao;
	}
	
	public String getProcessoNumeroCompleto() {
		return processoNumeroCompleto;
	}
	
	public void setProcessoNumeroCompleto(String processoNumeroCompleto) {
		if (processoNumeroCompleto != null)
			this.processoNumeroCompleto = processoNumeroCompleto;
	}

	
	public String getTabelaOrigem() {
		return tabelaOrigem;
	}
	

	public void setTabelaOrigem(String tabelaOrigem) {
		if (tabelaOrigem != null)
		this.tabelaOrigem = tabelaOrigem;
	}

}
