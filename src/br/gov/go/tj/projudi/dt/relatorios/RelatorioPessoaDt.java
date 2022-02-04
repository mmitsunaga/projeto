package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para armazenar relatório
 */
public class RelatorioPessoaDt extends Dados{

//	public static final int CodigoPermissaoSumario = 460;

	private String vinculo;
	private String nomePessoaVinculada;
	private String dataCadastro;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioPessoaDt() {
		limpar();
	}

	/**
	 * Define os valores de todas as variáveis como nulo
	 */
	public void limpar() {
		vinculo = "";
		nomePessoaVinculada = "";
		dataCadastro = "";
	}

	/**
	 * Limpa alguns parâmetros do objeto
	 */
	public void limparParametrosConsulta() {
		vinculo = "";
		nomePessoaVinculada = "";
		dataCadastro = "";
	}
	
	public String getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(String dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getVinculo() {
		return vinculo;
	}

	public void setVinculo(String vinculo) {
		this.vinculo = vinculo;
	}

	public String getNomePessoaVinculada() {
		return nomePessoaVinculada;
	}

	public void setNomePessoaVinculada(String nomePessoaVinculada) {
		this.nomePessoaVinculada = nomePessoaVinculada;
	}

	public void setId(String id) {
		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}