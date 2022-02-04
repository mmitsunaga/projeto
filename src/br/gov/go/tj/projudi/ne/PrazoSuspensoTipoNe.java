package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.PrazoSuspensoTipoDt;
import br.gov.go.tj.projudi.ps.PrazoSuspensoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class PrazoSuspensoTipoNe extends PrazoSuspensoTipoNeGen {

	private static final long serialVersionUID = -8503309915337943569L;

	public String Verificar(PrazoSuspensoTipoDt dados) {

		String stRetorno = "";
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PrazoSuspensoTipoPs obPersistencia = new  PrazoSuspensoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

}
