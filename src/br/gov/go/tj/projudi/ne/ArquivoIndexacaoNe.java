package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.ps.ArquivoIndexacaoPs;
import br.gov.go.tj.projudi.ps.ArquivoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.ValidacaoUtil;

public class ArquivoIndexacaoNe extends Negocio {
	
	private static final long serialVersionUID = 1974260298292534340L;

	public ArquivoIndexacaoNe() {
		
	}
	
	/**
	 * Lista os arquivos que ainda n�o foram indexados para o Elasticsearch.
	 * Verifica se a data de indexa��o � nula e se o id � maior que o �ltimo arquivo indexado na migra��o (id=71945162).
	 * Esse m�todo ser� utilizado pelo projeto de execu��o autom�tica.
	 * @return Lista de identificadores
	 */
	public List<Integer> listarIdsArquivosParaIndexacaoNoElasticSearch(int offset, int limit) throws Exception {
		List<Integer> lista = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ArquivoIndexacaoPs obPersistencia = new  ArquivoIndexacaoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarIdsArquivosParaIndexacaoNoElasticSearch(offset, limit);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void alterarStatusArquivoTemporario (String id, String status, FabricaConexao conexao) throws Exception {		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = ValidacaoUtil.isNulo(conexao) ? new FabricaConexao(FabricaConexao.PERSISTENCIA) : conexao;
			ArquivoIndexacaoPs obPersistencia = new ArquivoIndexacaoPs(obFabricaConexao.getConexao());			
			obPersistencia.atualizarStatus(id, status);
		} finally{
			if (ValidacaoUtil.isNulo(conexao)) obFabricaConexao.fecharConexao();
		}		
	}
	
	/**
	 * Altera a data de indexa��o do arquivo
	 * Utilizado pela execu��o autom�tica - indexa��o de arquivos no elasticsearch
	 * Esse m�todo foi colocado nessa classe de neg�cio para evitar que seja copiado 
	 * a lib aws-java-sdk que � requerido pelo ArquivoNe.
	 * @param obFabricaConexao
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int alterarDataIndexacao (String id, FabricaConexao conexao) throws Exception {
		int rowsAfetados = 0;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = ValidacaoUtil.isNulo(conexao) ? new FabricaConexao(FabricaConexao.PERSISTENCIA) : conexao;
			ArquivoPs obPersistencia = new ArquivoPs(obFabricaConexao.getConexao());
			rowsAfetados = obPersistencia.alterarDataIndexacao(id);			
		} finally{
			if (ValidacaoUtil.isNulo(conexao)) obFabricaConexao.fecharConexao();
		}
		return rowsAfetados;
	}

}
