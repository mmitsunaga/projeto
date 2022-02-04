package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.ps.GrupoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class GrupoTipoNe extends GrupoTipoNeGen {

	private static final long serialVersionUID = -5468327100194072273L;

	public String Verificar(GrupoTipoDt dados) {

		String stRetorno = "";
		if (dados.getGrupoTipo().length() == 0)
			stRetorno += "O Campo GrupoTipo é obrigatório.";
		if (dados.getGrupoTipoCodigo().length() == 0)
			stRetorno += "O Campo GrupoTipoCodigo é obrigatório.";
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GrupoTipoPs obPersistencia = new GrupoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
}
