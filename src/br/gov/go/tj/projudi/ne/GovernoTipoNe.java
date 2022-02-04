package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.GovernoTipoDt;
import br.gov.go.tj.projudi.ps.GovernoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class GovernoTipoNe extends GovernoTipoNeGen {

	private static final long serialVersionUID = -808018568836946174L;

	public String Verificar(GovernoTipoDt dados) {

		String stRetorno = "";
		//System.out.println("..neGovernoTipoVerificar()");
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GovernoTipoPs obPersistencia = new GovernoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

}
