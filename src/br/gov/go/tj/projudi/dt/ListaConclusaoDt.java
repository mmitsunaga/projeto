package br.gov.go.tj.projudi.dt;

import java.util.List;

/**
 * Lista de conclusões
 *  - Inicialmente criado para contemplar a pagina inicial dos juízes
 * @author Ronneesley Moura Teles
 * @since 02/12/2008 16:16
 */
public class ListaConclusaoDt extends Dados {

	private static final long serialVersionUID = -511080914364237614L;

	/**
	 * Quantidade de conclusões nao analisadas
	 */
	private int qtdeNaoAnalisadas;

	/**
	 * Quantidade de conclusões pré-analisadas
	 */
	private int qtdePreAnalisadas;

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
	
	/**
	 * Id Serventia Grupo (Unidade de Trabalho)
	 */
	private String idServentiaGrupo;

	/**
	 * Quantidade total de Pendencias
	 */
	private int quantidadePendencias;
	
	/**
	 * Lista de conclusões, tela inicial distribuidor de gabinete
	 */
	private List conclusoesAndamento;
	
	/**
	 * Quantidade de conclusões pré-analisadas pendentes de assinatura
	 */
	private int qtdePreAnalisadasPendentesAssinatura;

	/**
	 * lrcampos 18/10/2019 10:17 - Quantidade de conclusões nao analisadas e pré analisadas virtual
	 */
	private int qtdeNaoAnalisadasVirtualNaoInicilizada;
	private int qtdePreAnalisadasVirtualNaoInicializada;
	private int qtdePreAnalisadasPresencialNaoInicilizada;
	private int qtdeTotalBoxPresencialSessaoVirtual;
	
	public int getQtdeNaoAnalisadas() {
		return qtdeNaoAnalisadas;
	}

	public void setQtdeNaoAnalisadas(int qtdeNaoAnalisadas) {
		this.qtdeNaoAnalisadas = qtdeNaoAnalisadas;
	}

	public int getQtdePreAnalisadasVirtualNaoInicilizada() {
		return qtdePreAnalisadasVirtualNaoInicializada;
	}

	public int getQtdePreAnalisadasPresencialNaoInicilizada() {
		return qtdePreAnalisadasPresencialNaoInicilizada;
	}
	
	public void setQtdePreAnalisadasVirtualNaoInicializada(int qtdePreAnalisadasVirtualNaoInicializada) {
		this.qtdePreAnalisadasVirtualNaoInicializada = qtdePreAnalisadasVirtualNaoInicializada;
	}
	
	public int getQtdeNaoAnalisadasVirtualNaoInicilizada() {
		return qtdeNaoAnalisadasVirtualNaoInicilizada;
	}

	public void setQtdeNaoAnalisadasVirtualNaoInicilizada(int qtdeNaoAnalisadasVirtualNaoInicilizada) {
		this.qtdeNaoAnalisadasVirtualNaoInicilizada = qtdeNaoAnalisadasVirtualNaoInicilizada;
	}
	
	public void setQtdePreAnalisadasPresencialNaoInicilizada(int qtdePreAnalisadasPresencialNaoInicilizada) {
		this.qtdePreAnalisadasPresencialNaoInicilizada = qtdePreAnalisadasPresencialNaoInicilizada;
	}
	
	public int getQtdeTotalBoxPresencialSessaoVirtual() {
		return qtdeTotalBoxPresencialSessaoVirtual;
	}
	
	public void setQtdeTotalBoxPresencialSessaoVirtual(int qtdeTotalBoxPresencialSessaoVirtual) {
		this.qtdeTotalBoxPresencialSessaoVirtual = qtdeTotalBoxPresencialSessaoVirtual;
	}
	
	
	public int getQtdePreAnalisadas() {
		return qtdePreAnalisadas;
	}

	public void setQtdePreAnalisadas(int qtdePreAnalisadas) {
		this.qtdePreAnalisadas = qtdePreAnalisadas;
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
	
	public String getIdServentiaGrupo() {
		return idServentiaGrupo;
	}

	public void setIdServentiaGrupo(String idServentiaGrupo) {
		this.idServentiaGrupo = idServentiaGrupo;
	}

	public int getQtdeReservadas() {
		return qtdeReservadas;
	}

	public void setQtdeReservadas(int qtdeReservadas) {
		this.qtdeReservadas = qtdeReservadas;
	}

	public List getConclusoesAndamento() {
		return conclusoesAndamento;
	}

	public void setConclusoesAndamento(List conclusoesAndamento) {
		this.conclusoesAndamento = conclusoesAndamento;
	}

	public void setQtdePreAnalisadasPendentesAssinatura(int qtdePreAnalisadasPendentesAssinatura) {
		this.qtdePreAnalisadasPendentesAssinatura = qtdePreAnalisadasPendentesAssinatura;
	}

	public int getQtdePreAnalisadasPendentesAssinatura() {
		return qtdePreAnalisadasPendentesAssinatura;
	}

	public String getId() {
		return null;
	}
	
	public void setId(String id) {
	}
}
