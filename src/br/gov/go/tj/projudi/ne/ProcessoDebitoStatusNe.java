package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.ps.ProcessoDebitoPs;
import br.gov.go.tj.projudi.ps.ProcessoDebitoStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoDebitoStatusNe extends ProcessoDebitoStatusNeGen {

	private static final long serialVersionUID = -8773478860533665382L;

	public String Verificar(ProcessoDebitoStatusDt dados) {

		String stRetorno = "";

		if (dados.getProcessoDebitoStatus().length() == 0)
			stRetorno += "O Campo Processo Débito Status é um é obrigatório.";
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoDebitoStatusPs obPersistencia = new  ProcessoDebitoStatusPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public List consultarProcessoDebitoStatus(String descricao) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoDebitoStatusPs obPersistencia = new  ProcessoDebitoStatusPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricao(descricao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

}
