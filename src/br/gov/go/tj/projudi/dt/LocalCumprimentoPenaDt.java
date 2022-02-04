package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class LocalCumprimentoPenaDt extends LocalCumprimentoPenaDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = 2686416178768135453L;
    public static final int CodigoPermissao=398;
    
    private EnderecoDt enderecoLocal;
    //
    public void limpar() {
		super.limpar();
		enderecoLocal = new EnderecoDt();
	}
	public EnderecoDt getEnderecoLocal() {
		return enderecoLocal;
	}

	public void setEnderecoLocal(EnderecoDt enderecoLocal) {
		this.enderecoLocal = enderecoLocal;
	}

}
