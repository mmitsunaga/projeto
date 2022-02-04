package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na listagem de maiores promoventes e promovidos
 */
public class RelatorioAdvogadosProcessosOutrosEstadosDt extends Dados {

	private static final long serialVersionUID = -4321430352557100077L;

	public static final int CodigoPermissao = 808;

	// variáveis do relatório
	private int quantidadeProcessos;
	private String nomeAdvogado;
	private String estadoAdvogado;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioAdvogadosProcessosOutrosEstadosDt() {
		limpar();
	}

	public void limpar() {
		nomeAdvogado = "";
		estadoAdvogado = "";
		quantidadeProcessos = 0;
	}

	/**
	 * Método que limpa os campos de consulta do relatório.
	 */
	public void limparCamposConsulta() {
		nomeAdvogado = "";
		estadoAdvogado = "";
		quantidadeProcessos = 0;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

	public int getQuantidadeProcessos() {
		return quantidadeProcessos;
	}

	public void setQuantidadeProcessos(int quantidadeProcessos) {
		this.quantidadeProcessos = quantidadeProcessos;
	}

	public String getNomeAdvogado() {
		return nomeAdvogado;
	}

	public void setNomeAdvogado(String nomeAdvogado) {
		this.nomeAdvogado = nomeAdvogado;
	}

	public String getEstadoAdvogado() {
		return estadoAdvogado;
	}

	public void setEstadoAdvogado(String estadoAdvogado) {
		this.estadoAdvogado = estadoAdvogado;
	}

}