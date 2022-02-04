package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ObjetoPedidoDt;
import br.gov.go.tj.projudi.ps.ObjetoPedidoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ObjetoPedidoNe extends ObjetoPedidoNeGen {

	private static final long serialVersionUID = 1794432982130803800L;

	public String Verificar(ObjetoPedidoDt dados) {
		String stRetorno = "";
		if (dados.getObjetoPedido().equalsIgnoreCase("")) {
			stRetorno += "O Campo ObjetoPedido é obrigatório.";
		}
		if (dados.getObjetoPedidoCodigo().equalsIgnoreCase("")) {
			stRetorno += "O Campo ObjetoPedidoCodigo é obrigatório.";
		}
		return stRetorno;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ObjetoPedidoPs obPersistencia = new  ObjetoPedidoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public ObjetoPedidoDt consultarObjetoPedidoCodigo(String objetoPedidoCodigo) throws Exception {
		
		ObjetoPedidoDt retorno = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ObjetoPedidoPs obPersistencia = new  ObjetoPedidoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarObjetoPedidoCodigo(objetoPedidoCodigo);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;   
	}

}
