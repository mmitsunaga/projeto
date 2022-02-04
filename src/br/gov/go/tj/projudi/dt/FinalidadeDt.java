package br.gov.go.tj.projudi.dt;

public class FinalidadeDt extends Dados {
	
	private static final long serialVersionUID = 8345769670533343316L;
	
	private String			id;
	private String			finalidade;
	private String			finalidadeCodigo;
	private String			ativo;
	private String			codigoTemp;
	public static final int   CodigoPermissao  = -99;
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}
	
	public String getFinalidade() {
		return finalidade;
	}
	
	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}
	
	public String getFinalidadeCodigo() {
		return finalidadeCodigo;
	}
	
	public void setFinalidadeCodigo(String finalidadeCodigo) {
		this.finalidadeCodigo = finalidadeCodigo;
	}
	
	public String getAtivo() {
		return ativo;
	}
	
	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}
	
	public String getCodigoTemp() {
		return codigoTemp;
	}
	
	public void setCodigoTemp(String codigoTemp) {
		this.codigoTemp = codigoTemp;
	}
	
}
