package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.AlcunhaDt;
import br.gov.go.tj.projudi.ps.AlcunhaPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;

public class AlcunhaNe extends AlcunhaNeGen {

	private static final long serialVersionUID = -525138706823226733L;

	public String Verificar(AlcunhaDt dados) {

		String stRetorno = "";

		if (dados.getAlcunha().length() == 0)
			stRetorno += "O Campo Alcunha é obrigatório.";
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception{
		return consultarDescricaoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AlcunhaPs  obPersistencia = new  AlcunhaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

}