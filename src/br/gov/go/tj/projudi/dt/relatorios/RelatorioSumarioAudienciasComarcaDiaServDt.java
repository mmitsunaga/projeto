package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class RelatorioSumarioAudienciasComarcaDiaServDt extends Dados {

	private static final long serialVersionUID = 1460190577970884015L;

	public static final int CodigoPermissaoSumarioAudiencia = 904;
	
	
	private String id_Serventia;
	private String serventia;
	private String designadas;
	private String realizadas;
	private String acordos;
	private String total;
	private String valorAcordos;

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public RelatorioSumarioAudienciasComarcaDiaServDt() {
		limpar();
	}


	public void limpar() {
	}

	/**
	 * Método que limpa os campos de consulta da tela, mas não limpa o Tipo de
	 * Relatório que está sendo gerado.
	 */
	public void limparCamposConsulta() {
	}


	public String getId_Serventia() {
		return id_Serventia;
	}


	public void setId_Serventia(String id_Serventia) {
		this.id_Serventia = id_Serventia;
	}


	public String getServentia() {
		return serventia;
	}


	public void setServentia(String serventia) {
		this.serventia = serventia;
	}


	public String getDesignadas() {
		return designadas;
	}


	public void setDesignadas(String designadas) {
		this.designadas = designadas;
	}


	public String getRealizadas() {
		return realizadas;
	}


	public void setRealizadas(String realizadas) {
		this.realizadas = realizadas;
	}


	public String getAcordos() {
		return acordos;
	}


	public void setAcordos(String acordos) {
		this.acordos = acordos;
	}


	public String getTotal() {
		return total;
	}


	public void setTotal(String total) {
		this.total = total;
	}


	public String getValorAcordos() {
		return valorAcordos;
	}


	public void setValorAcordos(String valorAcordos) {
		this.valorAcordos = valorAcordos;
	}


	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
	
}