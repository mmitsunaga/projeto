package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.ps.CargoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class CargoTipoNe extends CargoTipoNeGen {

	private static final long serialVersionUID = -959515813014089423L;

	public String Verificar(CargoTipoDt dados) {

		String stRetorno = "";

		if (dados.getCargoTipo().length() == 0)
			stRetorno += "O Campo Descrição é obrigatório.";
		if (dados.getCargoTipoCodigo().length() == 0)
			stRetorno += "O Campo Código é obrigatório.";
		if (dados.getGrupo().length() == 0)
			stRetorno += "O Campo Grupo é obrigatório.";
		return stRetorno;

	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CargoTipoPs obPersistencia = new  CargoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarDescricaoGrupoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		GrupoNe Grupone = new GrupoNe();
		stTemp = Grupone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

	/**
	 * Método que retonar o ID do Cargo Tipo consultando através do código do mesmo.
	 * @param cargoTipoCodigo - código do cargo tipo
	 * @return ID do cargo tipo
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarIdCargoTipoCodigo(int cargoTipoCodigo) throws Exception {
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CargoTipoPs obPersistencia = new CargoTipoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarIdCargoTipoCodigo(cargoTipoCodigo); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public CargoTipoDt consultarCargoTipoCodigo(String cargoTipoCodigo) throws Exception {

		CargoTipoDt retorno = null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CargoTipoPs obPersistencia = new CargoTipoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarId(cargoTipoCodigo); 
			obDados.copiar(retorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
}
