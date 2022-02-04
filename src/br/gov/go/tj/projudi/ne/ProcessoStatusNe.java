package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.ps.ProcessoStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoStatusNe extends ProcessoStatusNeGen {

	private static final long serialVersionUID = 372029791239832914L;

	public String Verificar(ProcessoStatusDt dados) {
		String stRetorno = "";

		if (dados.getProcessoStatus().trim().equalsIgnoreCase("")) {
			return stRetorno += "O campo 'Status de Processo' é obrigatório!";
		}
		return stRetorno;
	}

	/**
	 * Método responsável por abrir uma conexão com o banco de dados para buscar
	 * os status de processo existentes
	 * 
	 * @author Keila
	 * @return processoStatus: lista contendo os status de processo existentes
	 * @throws Exception
	 */

	public List getProcessoStatus() throws Exception {

		List listaProcessoStatus = new ArrayList();
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoStatusPs obPersistencia = new  ProcessoStatusPs(obFabricaConexao.getConexao());
			listaProcessoStatus = obPersistencia.getProcessoStatus();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaProcessoStatus;
	}

	/**
	 * Método que obtém o objeto ProcessoStatus corresponde ao código passado
	 */
	public ProcessoStatusDt consultarProcessoStatusCodigo(int processoStatusCodigo) throws Exception {
		ProcessoStatusDt processoStatusDt = new ProcessoStatusDt();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoStatusPs obPersistencia = new  ProcessoStatusPs(obFabricaConexao.getConexao());
			processoStatusDt = obPersistencia.consultarProcessoStatusCodigo(processoStatusCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return processoStatusDt;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoStatusPs obPersistencia = new  ProcessoStatusPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoStatusPs obPersistencia = new  ProcessoStatusPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
}
