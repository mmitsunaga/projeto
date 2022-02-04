package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.ps.RgOrgaoExpedidorPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class RgOrgaoExpedidorNe extends RgOrgaoExpedidorNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8935721394060779366L;

	public String Verificar(RgOrgaoExpedidorDt dados) {

		String stRetorno = "";

		if (dados.getRgOrgaoExpedidor().length() == 0) stRetorno += "O Campo RgOrgaoExpedidor é obrigatório.";
		if (dados.getSigla().length() == 0) stRetorno += "O Campo Sigla é obrigatório.";
		if (dados.getEstado().length() == 0) stRetorno += "O Campo Estado é obrigatório.";
		//		if (dados.getEstadoCodigo().length()==0)
		//			stRetorno += "O Campo EstadoCodigo é obrigatório.";
		//		if (dados.getUf().length()==0)
		//			stRetorno += "O Campo Uf é obrigatório.";
		return stRetorno;

	}

	/**
	 * Método responsável em consultar um Órgão Expedidor, baseado na descrição e Uf passados
	 */
	public RgOrgaoExpedidorDt buscaOrgaoExpedidor(String descricao, String Uf) throws Exception {
		RgOrgaoExpedidorDt expedidorDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			expedidorDt = obPersistencia.buscaOrgaoExpedidor(descricao, Uf);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return expedidorDt;
	}

	/**
	 * Consulta geral de Órgãos Expedidores
	 */
	public List consultarDescricao(String descricao, String sigla, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricao(descricao, sigla, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarDescricaoJSON(String sigla, String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(sigla, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoSiglaJSON(String sigla, String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RgOrgaoExpedidorPs obPersistencia = new RgOrgaoExpedidorPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoSiglaJSON(sigla, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoEstadoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		EstadoNe Estadone = new EstadoNe(); 
		stTemp = Estadone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

}
