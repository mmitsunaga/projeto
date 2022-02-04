package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.projudi.ps.ProcessoSubtipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ProcessoSubtipoNe extends ProcessoSubtipoNeGen {

	private static final long serialVersionUID = 3925318617859926912L;

	public String Verificar(ProcessoSubtipoDt dados) {

		String stRetorno = "";

		if (dados.getProcessoSubtipo().length() == 0)
			stRetorno += "O Campo ProcessoSubtipo é obrigatório.";
		if (dados.getProcessoSubtipoCodigo().length() == 0)
			stRetorno += "O Campo ProcessoSubtipoCodigo é obrigatório.";

		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoSubtipoPs obPersistencia = new ProcessoSubtipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

}
