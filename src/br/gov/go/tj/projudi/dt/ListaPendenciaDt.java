package br.gov.go.tj.projudi.dt;

import java.util.List;

/**
 * Lista de pendencias
 *  - Inicialmente criado para contemplar a pagina inicial
 * @author Ronneesley Moura Teles
 * @since 02/12/2008 16:16
 */
public class ListaPendenciaDt extends Dados {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4574957290406931036L;
	/**
	 * Lista de pendencias reservadas
	 */
	private List reservadas;
	/**
	 * Lista de pendencias intimacoes, tela inicial advogado
	 */
	private List pendenciasAndamento;

	/**
	 * Quantidade de pendencias nao analisadas
	 */
	private int qtdeNaoAnalisadas;
	
	/**
	 * Quantidade de pendencias pre-analisadas
	 */
	private int qtdePreAnalisadas;
	
	/**
	 * Quantidade de pendencias pre-analisadas da Serventia
	 */
	private int qtdePreAnalisadasServentia;
	
	/**
	 * Quantidade de pendencias reservadas
	 */
	private int qtdeReservadas;
	
	/**
	 * Titulo da lista de pendencia
	 */
	private String titulo;
	
	/**
	 * Id do tipo da pendencia
	 */
	private String idTipo;
	
	public String getUrlRetorno() {
		return urlRetorno;
	}

	public void setUrlRetorno(String urlRetorno) {
		this.urlRetorno = urlRetorno;
	}

	/**
	 * Quantidade total de Pendencias
	 */
	private int quantidadePendencias;
	
	/**
	 * Quantidade de pendencias pré-analisadas pendentes de assinatura
	 */
	private int qtdePreAnalisadasPendentesAssinatura;
	
	private String urlRetorno;

	public ListaPendenciaDt() {
//		qtdeNaoAnalisadas=0;
//		qtdePreAnalisadas=0;
//		qtdeReservadas=0;
	}

	public List getReservadas() {
		return reservadas;
	}

	public void setReservadas(List reservadas) {
		this.reservadas = reservadas;
	}
	
	public List getPendenciasAndamento() {
		return pendenciasAndamento;
	}

	public void setPendenciasAndamento(List pendenciasAndamento) {
		this.pendenciasAndamento = pendenciasAndamento;
	}

	public int getQtdeNaoAnalisadas() {
		return qtdeNaoAnalisadas;
	}

	public void setQtdeNaoAnalisadas(int qtdeNaoAnalisadas) {
		this.qtdeNaoAnalisadas = qtdeNaoAnalisadas;
	}

	public int getQtdePreAnalisadas() {
		return qtdePreAnalisadas;
	}

	public void setQtdePreAnalisadas(int qtdePreAnalisadas) {
		this.qtdePreAnalisadas = qtdePreAnalisadas;
	}

	public int getQtdePreAnalisadasServentia() {
		return qtdePreAnalisadasServentia;
	}

	public void setQtdePreAnalisadasServentia(int qtdePreAnalisadasServentia) {
		this.qtdePreAnalisadasServentia = qtdePreAnalisadasServentia;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public int getQuantidadePendencias() {
		return quantidadePendencias;
	}

	public void setQuantidadePendencias(int quantidadePendencias) {
		this.quantidadePendencias = quantidadePendencias;
	}

	public String getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(String idTipo) {
		this.idTipo = idTipo;
	}

	public int getQtdeReservadas() {
		return qtdeReservadas;
	}

	public void setQtdeReservadas(int qtdeReservadas) {
		this.qtdeReservadas = qtdeReservadas;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}
	
	public void setQtdePreAnalisadasPendentesAssinatura(int qtdePreAnalisadasPendentesAssinatura) {
		this.qtdePreAnalisadasPendentesAssinatura = qtdePreAnalisadasPendentesAssinatura;
	}

	public int getQtdePreAnalisadasPendentesAssinatura() {
		return qtdePreAnalisadasPendentesAssinatura;
	}
	
	
}
