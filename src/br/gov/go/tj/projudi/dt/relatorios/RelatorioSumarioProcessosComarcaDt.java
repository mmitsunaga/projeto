package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na listagem de maiores promoventes e promovidos
 */
public class RelatorioSumarioProcessosComarcaDt extends Dados {

	private static final long serialVersionUID = 2081250722582753056L;

	public static final int CodigoPermissao = 587;

	public static final int ATIVO = 0;
	public static final int DISTRIBUIDO = 1;
	public static final int ARQUIVADO = 2;
	
	// vari�veis da tela
	private String limiteConsulta;
	private String tipoProcesso;

	// vari�veis do relat�rio
	private int qtdeProcessosRelatorio;
	private String nomeComarcaRelatorio;

	/**
	 * Construtor. Inicializa todas as vari�veis
	 */
	public RelatorioSumarioProcessosComarcaDt() {
		limpar();
	}

	public void limpar() {
		limiteConsulta = "";
		tipoProcesso = "";
		nomeComarcaRelatorio = "";
		qtdeProcessosRelatorio = 0;
	}

	/**
	 * M�todo que limpa os campos de consulta da tela e relat�rio.
	 */
	public void limparCamposConsulta() {
		limiteConsulta = "";
		tipoProcesso = "";
		nomeComarcaRelatorio = "";
		qtdeProcessosRelatorio = 0;
	}

	public String getId() {
		return null;
	}

	public void setId(String id) {

	}

	public String getLimiteConsulta() {
		return limiteConsulta;
	}

	public void setLimiteConsulta(String limiteConsulta) {
		this.limiteConsulta = limiteConsulta;
	}

	public String getTipoProcesso() {
		return tipoProcesso;
	}

	public void setTipoProcesso(String tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public int getQtdeProcessosRelatorio() {
		return qtdeProcessosRelatorio;
	}

	public void setQtdeProcessosRelatorio(int qtdeProcessosRelatorio) {
		this.qtdeProcessosRelatorio = qtdeProcessosRelatorio;
	}

	public String getNomeComarcaRelatorio() {
		return nomeComarcaRelatorio;
	}

	public void setNomeComarcaRelatorio(String nomeComarcaRelatorio) {
		this.nomeComarcaRelatorio = nomeComarcaRelatorio;
	}

}