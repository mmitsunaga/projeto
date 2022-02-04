package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na listagem de maiores promoventes e promovidos
 */
public class RelatorioMaioresPromoventesPromovidosDt extends Dados {

	private static final long serialVersionUID = -4321430352557100077L;

	public static final int CodigoPermissao = 583;

	public static final int TIPO_PROMOVENTE = 1;
	public static final int TIPO_PROMOVIDO = 0;

	// variáveis da tela
	private String limiteConsulta;
	private String tipoParte;

	// variáveis do relatório
	private int qtdeProcessosRelatorio;
	private String nomeParteRelatorio;
	private String cpfCnpjParteRelatorio;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioMaioresPromoventesPromovidosDt() {
		limpar();
	}

	public void limpar() {
		limiteConsulta = "";
		tipoParte = "";
		nomeParteRelatorio = "";
		cpfCnpjParteRelatorio = "";
		qtdeProcessosRelatorio = 0;
	}

	/**
	 * Método que limpa os campos de consulta da tela e relatório.
	 */
	public void limparCamposConsulta() {
		limiteConsulta = "";
		tipoParte = "";
		nomeParteRelatorio = "";
		cpfCnpjParteRelatorio = "";
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

	public String getTipoParte() {
		return tipoParte;
	}

	public void setTipoParte(String tipoParte) {
		this.tipoParte = tipoParte;
	}

	public int getQtdeProcessosRelatorio() {
		return qtdeProcessosRelatorio;
	}

	public void setQtdeProcessosRelatorio(int qtdeProcessosRelatorio) {
		this.qtdeProcessosRelatorio = qtdeProcessosRelatorio;
	}

	public String getNomeParteRelatorio() {
		return nomeParteRelatorio;
	}

	public void setNomeParteRelatorio(String nomeParteRelatorio) {
		this.nomeParteRelatorio = nomeParteRelatorio;
	}

	public String getCpfCnpjParteRelatorio() {
		return cpfCnpjParteRelatorio;
	}

	public void setCpfCnpjParteRelatorio(String cpfCnpjParteRelatorio) {
		this.cpfCnpjParteRelatorio = cpfCnpjParteRelatorio;
	}
}