package br.gov.go.tj.projudi.dt;

//---------------------------------------------------------
public class UsuarioServentiaOabDt extends UsuarioServentiaOabDtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2486789171874510233L;
	public static final int CodigoPermissao = 503;
	
	// Variáveis para controlar se um advogado é Master
	public static final int NOT_MASTER = 0;
	public static final int MASTER = 1;

	private String NomeUsuario;
	private String serventia;
	private String grupoCodigo;
	private boolean promotor;
	private boolean isInativo;

	private String idServentia;

	public String getNomeUsuario() {
		return NomeUsuario;
	}

	public void setNomeUsuario(String valor) {
		if (valor != null) NomeUsuario = valor;
	}
	
	public void setPromotor(boolean promotor) {
		this.promotor = promotor;
	}

	public boolean isMp(){
		return promotor;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (serventia != null) this.serventia = serventia;
	}
	

	public String getIdServentia() {
		return idServentia;
	}

	
	public void setIdServentia(String idServentia) {
		this.idServentia = idServentia;
	}

	public boolean isInativo() {
		return isInativo;
	}
	
	public void setInativo(boolean isInativo) {
		this.isInativo = isInativo;
	}

	public String getGrupoCodigo() {
		return grupoCodigo;
	}

	public void setGrupoCodigo(String grupoCodigo) {
		this.grupoCodigo = grupoCodigo;
	}
	
}
