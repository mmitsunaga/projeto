package br.gov.go.tj.projudi.dt;

public class OficialSPGDt extends Dados {

	private static final long serialVersionUID = -6591973931639329889L;
	
	public static final String ATIVO 	= "1";
	public static final String INATIVO 	= "2";
	
	public static final String CODIGO_OFICIAL_TRIBUNAL_JUSTICA = "999999";
	public static final String NOME_OFICIAL_TRIBUNAL_JUSTICA = "Oficial de Justiça/Tribunal(Estado)"; 
	
	public String nomeOficial;
	public String codigoOficial;
	public String codigoComarca;
	public String ativo;

	public String getId() {
		return null;
	}
	
	public void setId(String id) {
	}

	public String getNomeOficial() {
		return nomeOficial;
	}

	public void setNomeOficial(String nomeOficial) {
		this.nomeOficial = nomeOficial;
	}

	public String getCodigoOficial() {
		return codigoOficial;
	}

	public void setCodigoOficial(String codigoOficial) {
		this.codigoOficial = codigoOficial;
	}
	
	public String getCodigoComarca() {
		return codigoComarca;
	}

	public void setCodigoComarca(String codigoComarca) {
		this.codigoComarca = codigoComarca;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}	
}