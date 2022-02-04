package br.gov.go.tj.projudi.temp;

import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.utils.FabricaConexao;


public class ExportacaoNe {

	protected LogNe obLog;

	public ExportacaoNe() {		
		obLog = new LogNe();
	}

	/**
	 * Recupera o id INICIAL do próximo aquivo a 
	 * ser exportado.
	 * 
	 * @return long
	 * @throws Exception
	 */
	public Long buscarIdInicial() throws Exception {
		long loTemp;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ExportacaoPs obPersistencia = new ExportacaoPs(obFabricaConexao.getConexao());
			loTemp = obPersistencia.buscarIdInicial();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return loTemp;
	}

	/**
	 * Recupera a lista de id's dos arquivos que serão importados de acordo com
	 * idInicio e um limite de registros impostos.
	 * 
	 * @param idArquivo - Long
	 * @param limite - int
	 * @return List
	 * @throws Exception
	 */
	public List<String> buscarIdsExportacao(Long idArquivo, long limite) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ExportacaoPs obPersistencia = new ExportacaoPs(obFabricaConexao.getConexao());
			return obPersistencia.buscarIdsExportacao(idArquivo, limite);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Retorna a quantidade de registros pendentes de exportação.
	 * @return Long
	 * @throws Exception
	 */
	public Long contarIdsRegistroExportacao() throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ExportacaoPs obPersistencia = new ExportacaoPs(obFabricaConexao.getConexao());
			return obPersistencia.contarIdsRegistroExportacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Recupera o arquivo que será exportado
	 * @param idArquivo
	 * 
	 * @return ArquivoDt
	 * @throws Exception
	 */
	public ArquivoDt getArquivoById(String idArquivo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ExportacaoPs obPersistencia = new ExportacaoPs(obFabricaConexao.getConexao());
			return obPersistencia.getArquivoById(idArquivo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Atualiza o caminho do arquivo exportado.
	 * 
	 * @param idArquivo
	 * @param path
	 * @throws Exception
	 */
	public void atualizarCaminhoArquivo(String idArquivo, String path) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ExportacaoPs obPersistencia = new ExportacaoPs(obFabricaConexao.getConexao());
			obPersistencia.atualizarCaminhoArquivo(idArquivo, path);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Recupera a quantidade total de arquivos existentes 
	 * para exportação.
	 * 
	 * @return long
	 * @throws Exception
	 */
	public long buscarQtdTotalArquivos() throws Exception {
		long loTemp;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ExportacaoPs obPersistencia = new ExportacaoPs(obFabricaConexao.getConexao());
			loTemp = obPersistencia.buscarQtdTotalArquivos();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return loTemp;
	}
}
