package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoBancoDt;
import br.gov.go.tj.projudi.ps.ArquivoBancoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ArquivoBancoNe extends Negocio {

	private static final long serialVersionUID = 8381361519144228138L;
	
	/**
	 * Construtor
	 */
	public ArquivoBancoNe() {
		
		obLog = new LogNe();
	}
	
	/**
	 * M�todo para salvar arquivo lido no banco.
	 * @param String nomeArquivo
	 * @param int idBanco
	 * @param int statusLeitura
	 * @param Strring mensagem
	 * @param String conteudo
	 * @throws Exception
	 */
	public void salvarArquivoLido(String nomeArquivo, int idBanco, int statusLeitura, String mensagem, String conteudo) throws Exception{
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoBancoPs obPersistencia = new ArquivoBancoPs(obFabricaConexao.getConexao());
			
			obPersistencia.salvarArquivoLido(nomeArquivo, idBanco, statusLeitura, mensagem, conteudo);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Consulta se arquivo j� foi lido.
	 * Retorna false caso n�o foi lido e true se sim.
	 * 
	 * @param String nomeArquivo
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isArquivoJaLido(String nomeArquivo) throws Exception{
		boolean retorno = false;
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoBancoPs obPersistencia = new ArquivoBancoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.isArquivoJaLido(nomeArquivo);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Consulta se arquivo j� foi lido.
	 * 
	 * @param String idArquivoBanco
	 * @return String
	 * @throws Exception
	 */
	public String consulteArquivoLido(String idArquivoBanco) throws Exception{
		String retorno = "";
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoBancoPs obPersistencia = new ArquivoBancoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consulteArquivoLido(idArquivoBanco);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Consulta os arquivos j� lidos e n�o reprocessados.
	 * 
	 * @return List<ArquivoBancoDt>
	 * @throws Exception
	 */
	public List<ArquivoBancoDt> consulteArquivosLidosParaReprocessamento() throws Exception{
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoBancoPs obPersistencia = new ArquivoBancoPs(obFabricaConexao.getConexao());			
			return obPersistencia.consulteArquivosLidosParaReprocessamento();		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void atualizaArquivoReprocessado(String idArquivoBanco) throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoBancoPs obPersistencia = new ArquivoBancoPs(obFabricaConexao.getConexao());			
			obPersistencia.atualizarArquivoReprocessado(idArquivoBanco);		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * M�todo que consulta as ultimas 5 ocorrencias de arquivos recebido da Caixa economina.
	 * Tamb�m conhecido como arquivos de "Rajada".
	 * 
	 * @return List<ArquivoBancoDt>
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public List<ArquivoBancoDt> consulta5UltimosArquivosProcessados() throws Exception {
		FabricaConexao obFabricaConexao = null;
		
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ArquivoBancoPs obPersistencia = new ArquivoBancoPs(obFabricaConexao.getConexao());			
			return obPersistencia.consulta5UltimosArquivosProcessados();		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	}
}
