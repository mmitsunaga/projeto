package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.ps.ProcessoPrioridadePs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoPrioridadeNe extends ProcessoPrioridadeNeGen {

	private static final long serialVersionUID = 3716072498074263146L;

	public String Verificar(ProcessoPrioridadeDt dados) {

		String stRetorno = "";

		if (dados.getProcessoPrioridade().equalsIgnoreCase(""))
			stRetorno += "O Campo ProcessoPrioridade é obrigatório.";
		if (dados.getProcessoPrioridadeCodigo().equalsIgnoreCase(""))
			stRetorno += "O Campo ProcessoPrioridadeCodigo é obrigatório.";
		return stRetorno;

	}

	/**
	 * Consultar registro ProcessoPrioridade de acordo com o
	 * ProcessoPrioridadeCodigo passado
	 */
	public ProcessoPrioridadeDt consultarProcessoPrioridadeCodigo(String processoPrioridadeCodigo) throws Exception {

		ProcessoPrioridadeDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPrioridadePs obPersistencia = new  ProcessoPrioridadePs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarProcessoPrioridadeCodigo(processoPrioridadeCodigo);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoPrioridadePs obPersistencia = new  ProcessoPrioridadePs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

}
