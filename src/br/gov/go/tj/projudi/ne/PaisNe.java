package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.PaisDt;
import br.gov.go.tj.projudi.ps.PaisPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;

public class PaisNe extends PaisNeGen {

	private static final long serialVersionUID = -5347178871914177340L;

	public String Verificar(PaisDt dados) {

		String stRetorno = "";

		if (dados.getPais().equalsIgnoreCase("")) {
			stRetorno += "Descrição é é obrigatório.";
		}
		if (dados.getPaisCodigo().equalsIgnoreCase("")) {
			stRetorno += "Código é é obrigatório.";
		}
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		return consultarDescricaoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}

	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PaisPs obPersistencia = new  PaisPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public List consultarDescricao(String descricao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PaisPs obPersistencia = new  PaisPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricao(descricao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
}
