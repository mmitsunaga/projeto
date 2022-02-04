package br.gov.go.tj.projudi.dt.relatorios;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.UsuarioDt;

/**
 * Objeto para armazenar relat�rio
 */
public class RelatorioEstatisticaDt extends Dados{

	private static final long serialVersionUID = -8370376157198508580L;
	
	public static final int CodigoPermissaoAnalitico = 461;
	public static final int CodigoPermissaoSumario = 460;
	
	public static final int[] AUDIENCIAS_DESIGNADAS = {
			AudienciaProcessoStatusDt.NAO_REALIZADA,
			AudienciaProcessoStatusDt.NEGATIVADA,
			AudienciaProcessoStatusDt.REMARCADA,
			AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA,
			AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA,
			AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO,
			AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO,
			AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO,
			AudienciaProcessoStatusDt.REALIZADA,
			AudienciaProcessoStatusDt.REALIZADA_SEM_ACORDO,
			AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO,
	};
	
	public static final int[] AUDIENCIAS_REALIZADAS = {
			AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA,
			AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA,
			AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO,
			AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO,
			AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO,
			AudienciaProcessoStatusDt.REALIZADA,
			AudienciaProcessoStatusDt.REALIZADA_SEM_ACORDO,
			AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO,
	};
	
	public static final int[] AUDIENCIAS_ACORDOS = {
			AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO
	};
	
	
	// Relatorio estatistico nugep
	public static final int ASSESSORIA_RECURSOS_CONSTITUCIONAIS = 1836;

	private String dataInicial;
	private String dataFinal;
	private String id_Serventia;
	private String serventia;
	private UsuarioDt usuario;
	private List detalhesMovimentacao;

	/**
	 * Construtor. Inicializa todas as vari�veis
	 */
	public RelatorioEstatisticaDt() {
		limpar();
	}

	/**
	 * Define os valores de todas as vari�veis como nulo
	 */
	public void limpar() {

		id_Serventia = "";
		serventia = "";
		usuario = new UsuarioDt();
		detalhesMovimentacao = null;
		dataInicial = "";
		dataFinal = "";

	}

	/**
	 * Limpa alguns par�metros do objeto
	 */
	public void limparParametrosConsulta() {
		id_Serventia = "";
		serventia = "";
		usuario = new UsuarioDt();
		detalhesMovimentacao = null;
	}

	/**
	 * Obt�m o c�digo da serventia
	 * 
	 * @return um <code>String</code> com o c�digo
	 */
	public String getId_Serventia() {
		return id_Serventia;
	}

	/**
	 * Define o valor do c�digo da serventia
	 * 
	 * @param id_Serventia
	 *            c�digo da serventia
	 */
	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null)
			this.id_Serventia = id_Serventia;
	}

	/**
	 * Obt�m o nome da serventia
	 * 
	 * @return um <code>String</code> com o nome
	 */
	public String getServentia() {
		return serventia;
	}

	/**
	 * Define o nome da serventia
	 * 
	 * @param serventia
	 *            nome da serventia
	 */
	public void setServentia(String serventia) {
		if (serventia != null)
			this.serventia = serventia;
	}

	/**
	 * Obt�m o objeto usu�rio
	 * 
	 * @return
	 */
	public UsuarioDt getUsuario() {
		return usuario;
	}

	/**
	 * Define o objeto usu�rio
	 * 
	 * @param usuario
	 *            objeto usu�rio
	 */
	public void setUsuario(UsuarioDt usuario) {
		if (usuario != null)
			this.usuario = usuario;
	}

	/**
	 * 
	 * @return
	 */
	public List getListaDetalhesMovimentacao() {
		if (detalhesMovimentacao == null) {
			detalhesMovimentacao = new ArrayList();
		}
		return detalhesMovimentacao;
	}

	/**
	 * 
	 * 
	 * @param detalhesMovimentacao
	 */
	public void setListaDetalhesMovimentacao(List detalhesMovimentacao) {
		this.detalhesMovimentacao = detalhesMovimentacao;
	}

	/**
	 * Obt�m a data inicial da consulta
	 * 
	 * @return um <code>String</code> com a data inicial
	 */
	public String getDataInicial() {
		return dataInicial;
	}

	/**
	 * Define a data inicial para consulta
	 * 
	 * @param dataInicial
	 *            data inicial da consulta
	 */
	public void setDataInicial(String dataInicial) {
		if (dataInicial != null)
			this.dataInicial = dataInicial;
	}

	/**
	 * Obt�m a data final da consulta
	 * 
	 * @return um <code>String</code> com a data final
	 */
	public String getDataFinal() {
		return dataFinal;
	}

	/**
	 * Define a data final para consulta
	 * 
	 * @param dataFinal
	 *            data final para consulta
	 */
	public void setDataFinal(String dataFinal) {
		if (dataFinal != null)
			this.dataFinal = dataFinal;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}

}