package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.ps.ProcessoFasePs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoFaseNe extends ProcessoFaseNeGen {

	private static final long serialVersionUID = -7254604500953044848L;

	public String Verificar(ProcessoFaseDt dados) {

		String stRetorno = "";

		if (dados.getProcessoFase().equalsIgnoreCase(""))
			stRetorno += "O Campo ProcessoFase é obrigatório.";
		if (dados.getProcessoFaseCodigo().equalsIgnoreCase(""))
			stRetorno += "O Campo ProcessoFaseCodigo é obrigatório.";
		return stRetorno;

	}

	/**
	 * Método que obtém o objeto ProcessoFase corresponde ao código passado
	 */
	public ProcessoFaseDt consultarProcessoFaseCodigo(int processoFaseCodigo) throws Exception {
		ProcessoFaseDt processoFaseDt = new ProcessoFaseDt();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoFasePs obPersistencia = new  ProcessoFasePs(obFabricaConexao.getConexao());
			processoFaseDt = obPersistencia.consultarProcessoFaseCodigo(processoFaseCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return processoFaseDt;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoFasePs obPersistencia = new  ProcessoFasePs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
}
