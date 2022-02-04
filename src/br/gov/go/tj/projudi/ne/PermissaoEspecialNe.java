package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.PermissaoEspecialDt;
import br.gov.go.tj.projudi.ps.PermissaoEspecialPs;
import br.gov.go.tj.utils.FabricaConexao;

public class PermissaoEspecialNe extends PermissaoEspecialNeGen {

	private static final long serialVersionUID = -3684639644150899498L;

	public String Verificar(PermissaoEspecialDt dados) {

		String stRetorno = "";

		if (dados.getPermissaoEspecial().length() == 0)
			stRetorno += "O Campo PermissaoEspecial é obrigatório.";
		if (dados.getPermissaoEspecialCodigo().length() == 0)
			stRetorno += "O Campo PermissaoEspecialCodigo é obrigatório.";
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoEspecialPs obPersistencia = new  PermissaoEspecialPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

}
