package br.gov.go.tj.projudi.dt;

public class PeticionamentoDt extends MovimentacaoProcessoDt {

	/**
     * 
     */
    private static final long serialVersionUID = -9001186153877545366L;

    public static final int CodigoPermissao = 102;
	
	private boolean pedidoUrgencia = false;
	
	private boolean pedidoSegredoJustica = false;

	public boolean isPedidoUrgencia() {
		return pedidoUrgencia;
	}

	public void setPedidoUrgencia(boolean pedidoUrgencia) {
		this.pedidoUrgencia = pedidoUrgencia;
	}
	
	public boolean isSegredoJustica() {
		return pedidoSegredoJustica;
	}

	public void setSegredoJustica(boolean pedidoSegredoJustica) {
		this.pedidoSegredoJustica = pedidoSegredoJustica;
	}
	
	public void copiar(PeticionamentoDt objeto){
		super.copiar(objeto);
		pedidoUrgencia = objeto.isPedidoUrgencia();
		pedidoSegredoJustica = objeto.isSegredoJustica();
	}
	

}
