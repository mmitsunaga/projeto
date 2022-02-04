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
	 * Lista os arquivos que ainda não foram indexados para o Elasticsearch.
	 * Verifica se a data de indexação é nula e se o id é maior que o último arquivo indexado na migração (id=71945162).
	 * Esse método será utilizado pelo projeto de execução automática.
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
	 * Altera a data de indexação do arquivo
	 * Utilizado pela execução automática - indexação de arquivos no elasticsearch
	 * Esse método foi colocado nessa classe de negócio para evitar que seja copiado 
	 * a lib aws-java-sdk que é requerido pelo ArquivoNe.
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
