package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.relatorios.EstatisticaPublicaDt;
import br.gov.go.tj.projudi.ps.EstatisticaPublicaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class EstatisticaPublicaNe extends Negocio {

	private static final long serialVersionUID = -8498344060230586980L;

	
	protected static Date ultimaExecucao = null;
	
	protected static List listaProcessosGeralComarca = new ArrayList();
	protected static List listaProcessosServentia = new ArrayList();
	
	public EstatisticaPublicaNe() {		
		obLog = new LogNe();
	}
	
	/**
	 * Limpa as vari�veis desta classe de neg�cio
	 */
	private void limparValoresVariaveisEstatisticaPublicaNe() {
		listaProcessosGeralComarca = new ArrayList();
		listaProcessosServentia = new ArrayList();
	}

	/**
	 * M�todo respons�vel por calcular os valores da estat�stica p�blica a cada hora. Sempre
	 * que a consulta a ser realizada tiver de aguardar uma hora para executar novamente,
	 * o m�todo deve ser chamado a partir deste.
	 * 
	 * @return EstatisticaPublicaDt preenchido com os valores atuais do sistema
	 * @throws Exception
	 */
	public EstatisticaPublicaDt consultarEstatisticasPublicasProjudi() throws Exception {
		EstatisticaPublicaDt estatisticaPublicaDt = new EstatisticaPublicaDt();
		Date dataAtual = new Date();
		boolean primeiraExecucao = false;

		if (ultimaExecucao == null) {
			ultimaExecucao = new Date();
			primeiraExecucao = true;
		}

		double diferencaMinutos = Funcoes.diferencaEmMinutos(ultimaExecucao, dataAtual);

		// valida se a ultima execu��o dos c�lculos foi realizada h� mais 60
		// minutos: - se true realiza os c�lculos novamente
		// se false apenas seta os valores e devolve pra tela sem recalcular
		if (primeiraExecucao || diferencaMinutos >= 60) {
			FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			EstatisticaPublicaPs obPersistencia = new  EstatisticaPublicaPs(obFabricaConexao.getConexao());
			try{
				// limpando as vari�veis da classe de neg�cio para evitar
				// duplica��o de registros
				this.limparValoresVariaveisEstatisticaPublicaNe();

				// preenchendo as vari�veis da classe de neg�cio para
				// montagem da tela
				listaProcessosGeralComarca.addAll(obPersistencia.calcularQuantitativoProcessosComarca());
				
				// setando valores no Dt para montar para a tela
				estatisticaPublicaDt = this.setarValoresEstatisticaPublicaDt(estatisticaPublicaDt);

				// Setando o hor�rio da �ltima execu��o destes c�lculos para
				// verifica��o futura
				ultimaExecucao = new Date();
			
			} finally{
				obFabricaConexao.fecharConexao();
			}

		} else {
			// Se n�o precisar consultar os dados novamente, vai apenas setar os
			// dados atuais e retornar
			estatisticaPublicaDt = this.setarValoresEstatisticaPublicaDt(estatisticaPublicaDt);
		}

		return estatisticaPublicaDt;
	}
	
	/**
	 * M�todo respons�vel pela estat�stica de processos Ativos, Arquivados e Recebidos 
	 * por Serventias de uma Comarca espec�fica
	 * @param idComarca - ID da Comarca
	 * @return listagem de processos das serventias da comarca
	 * @throws Exception
	 * @author hmgodinho
	 */
	public EstatisticaPublicaDt consultarEstatisticaProcessosServentiasComarca(String idComarca) throws Exception {
		EstatisticaPublicaDt estatisticaPublicaDt = new EstatisticaPublicaDt();
		FabricaConexao obFabricaConexao = null;
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			EstatisticaPublicaPs obPersistencia = new  EstatisticaPublicaPs(obFabricaConexao.getConexao());
			
			//limpando a vari�vel da classe de neg�cio para 
			//evitar duplicidade de registros
			listaProcessosServentia = new ArrayList();
			
			listaProcessosServentia.addAll(obPersistencia.calcularQuantitativoProcessosServentia(idComarca));
			
			// setando valores no Dt para montar para a tela
			estatisticaPublicaDt = this.setarValoresEstatisticaPublicaDt(estatisticaPublicaDt);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return estatisticaPublicaDt;
	}
	
	/**
	 * Quantitativo dos tipos de serventia com projudi instalado no estado.
	 * @return lista de Serventias
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarEstatisticaServentiasEstado() throws Exception {
		FabricaConexao obFabricaConexao = null;
		List listaServentiasEstado = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
		try{
			EstatisticaPublicaPs obPersistencia = new  EstatisticaPublicaPs(obFabricaConexao.getConexao());			
			listaServentiasEstado = obPersistencia.consultarEstatisticaServentiasEstado();
		
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaServentiasEstado;
	}
	

	/**
	 * Atualiza a vari�vel estatisticaPublicaDt, setando os valores das
	 * respectivas vari�veis desta classe de neg�cio.
	 * 
	 * @param estatisticaPublicaDt
	 *            - Dt a ser atualizado
	 * @return estatisticaPublicaDt atualizado com os valores mantidos nesta
	 *         classe de neg�cio
	 */
	private EstatisticaPublicaDt setarValoresEstatisticaPublicaDt(EstatisticaPublicaDt estatisticaPublicaDt) {
		estatisticaPublicaDt.limpar();
		
		estatisticaPublicaDt.setListaProcessosGeralComarca(listaProcessosGeralComarca);
		estatisticaPublicaDt.setListaProcessosServentia(listaProcessosServentia);

		return estatisticaPublicaDt;
	}

	

}
