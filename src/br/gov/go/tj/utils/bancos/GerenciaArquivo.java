package br.gov.go.tj.utils.bancos;

import java.util.List;

import br.gov.go.tj.utils.FabricaConexao;


public abstract class GerenciaArquivo {
	
	//Listas
	private List valorPago;
	private List numeroGuia;
	private List dataHoraRecebimento;
	protected FabricaConexao obFabricaConexao;
	protected FabricaConexao obFabricaConexaoAdabas;
	
	public GerenciaArquivo(FabricaConexao obFabricaConexao) {
		this.obFabricaConexao = obFabricaConexao;
		try {
			this.obFabricaConexaoAdabas = new FabricaConexao(FabricaConexao.ADABAS);
		} catch (Exception e) {	
			System.out.print(e);
		}
	}
	
	protected void finalizarConexaoAdabas() throws Exception {
		if (this.obFabricaConexaoAdabas != null) {
			this.obFabricaConexaoAdabas.fecharConexao();
		}
	} 
	
	public List getValorPago() {
		return valorPago;
	}

	public void setValorPago(List valorPago) {
		this.valorPago = valorPago;
	}

	public List getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(List numeroGuia) {
		this.numeroGuia = numeroGuia;
	}

	public List getDataHoraRecebimento() {
		return dataHoraRecebimento;
	}

	public void setDataHoraRecebimento(List dataHoraRecebimento) {
		this.dataHoraRecebimento = dataHoraRecebimento;
	}

	/**
	 * Método para ler conteúdo do arquivo.
	 * @param String conteudo
	 * @throws Exception
	 */
	public abstract int lerDados(String conteudo) throws Exception;
	
	/**
	 * Método para limpas as listas de dados.
	 * @throws Exception
	 */
	public void limparListas() throws Exception {
		valorPago 				= null;
		numeroGuia 				= null;
		dataHoraRecebimento 	= null;
	}
	
}
