package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class EnderecoDt extends EnderecoDtGen{

	/**
     * 
     */
    private static final long serialVersionUID = 1901873748132932481L;

    public static final int CodigoPermissao=186;
//
	
	private String estado;
	
	//private int  qtdLocomocao;

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

//	public int getQtdLocomocao() {
//		return qtdLocomocao;
//	}
//
//	public void setQtdLocomocao(int qtdLocomocao) {
//		this.qtdLocomocao = qtdLocomocao;
//	}
	
	
	public void limpar(){
		super.limpar();
		estado = "";
//		qtdLocomocao = 0;
	}
	

}
