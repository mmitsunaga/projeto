package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoDebitoDt;
import br.gov.go.tj.projudi.ps.ProcessoDebitoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoDebitoNe extends ProcessoDebitoNeGen {

	private static final long serialVersionUID = -8773478860533665382L;

	public String Verificar(ProcessoDebitoDt dados) {

		String stRetorno = "";

		if (dados.getProcessoDebito().length() == 0)
			stRetorno += "O Campo ProcessoDebito é obrigatório.";
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoDebitoPs obPersistencia = new  ProcessoDebitoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoProcessoDebitoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoDebitoPs obPersistencia = new  ProcessoDebitoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public List consultarProcessoDebito(String descricao) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoDebitoPs obPersistencia = new  ProcessoDebitoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricao(descricao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

}
