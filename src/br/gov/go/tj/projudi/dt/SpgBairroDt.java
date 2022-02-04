package br.gov.go.tj.projudi.dt;

public class SpgBairroDt  extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 365576956179706473L;

	String Bairro ="";
	String BairroCodigo = "";
	
	
	public void setId(String id) {
		BairroCodigo = id;
		
	}
	
	public String getBairro() {
		return Bairro;
	}

	public void setBairro(String bairro) {
		Bairro = bairro;
	}

	public String getBairroCodigo() {
		return BairroCodigo;
	}

	public void setBairroCodigo(String bairroCodigo) {
		BairroCodigo = bairroCodigo;
	}

	public String getId() {
		return BairroCodigo;
	}
	

}
