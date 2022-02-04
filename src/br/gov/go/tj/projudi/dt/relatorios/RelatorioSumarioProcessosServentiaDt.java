package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na listagem de maiores promoventes e promovidos
 */
public class RelatorioSumarioProcessosServentiaDt extends Dados {

	private static final long serialVersionUID = 5262979837738398222L;

	public static final int CodigoPermissao = 588;

	public static final int ATIVO = 0;
	public static final int DISTRIBUIDO = 1;
	public static final int ARQUIVADO = 2;

	// variáveis da tela
	private String limiteConsulta;
	private String tipoProcesso;

	// variáveis do relatório
	private int qtdeProcessosRelatorio;
	private String nomeServentiaRelatorio;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioSumarioProcessosServentiaDt() {
		limpar();
	}

	public void limpar() {
		limiteConsulta = "";
		tipoProcesso = "";
		nomeServentiaRelatorio = "";
		qtdeProcessosRelatorio = 0;
	}

	/**
	 * Método que limpa os campos de consulta da tela e relatório.
	 */
	public void limparCamposConsulta() {
		limiteConsulta = "";
		tipoProcesso = "";
		nomeServentiaRelatorio = "";
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

	public String getNomeServentiaRelatorio() {
		return nomeServentiaRelatorio;
	}

	public void setNomeServentiaRelatorio(String nomeServentiaRelatorio) {
		this.nomeServentiaRelatorio = nomeServentiaRelatorio;
	}
	
}