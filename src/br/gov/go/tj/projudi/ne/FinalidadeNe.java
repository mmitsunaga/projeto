package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.FinalidadeDt;
import br.gov.go.tj.projudi.ps.FinalidadePs;
import br.gov.go.tj.utils.FabricaConexao;

public class FinalidadeNe extends Negocio {

	private static final long serialVersionUID = 4098977235474256242L;
		
	protected FinalidadeDt obDados;

	public FinalidadeNe() {
		
		obLog = new LogNe(); 
		obDados = new FinalidadeDt(); 
	}
	
	/**
	 * Método para consultar lista de finalidades pela descrição.
	 * @param String descricao
	 * @return List
	 * @throws Exception
	 */
	public List consultarDescricao(String descricao) throws Exception {
		List listaFinalidadeDt = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			FinalidadePs obPersistencia = new FinalidadePs(obFabricaConexao.getConexao());
			listaFinalidadeDt = obPersistencia.consultarDescricao(descricao);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		return listaFinalidadeDt;
	}
	
	/**
	 * Método para consultar toda a lista de finalidades.
	 * @return List
	 * @throws Exception
	 */
	public List consultarFinalidades() throws Exception {
		List listaFinalidadeDt = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			FinalidadePs obPersistencia = new FinalidadePs(obFabricaConexao.getConexao());
			listaFinalidadeDt = obPersistencia.consultarFinalidades();
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		return listaFinalidadeDt;
	}
	
	/**
	 * Método para consultar finalidade pelo id.
	 * @return List
	 * @throws Exception
	 */
	public FinalidadeDt consultarId(String id_finalidade) throws Exception {
		FinalidadeDt finalidadeDt = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			FinalidadePs obPersistencia = new FinalidadePs(obFabricaConexao.getConexao());
			finalidadeDt = obPersistencia.consultarId(id_finalidade);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		return finalidadeDt;
	}
	
	public String consultarDescricaoFinalidadesJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			FinalidadePs obPersistencia = new FinalidadePs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoFinalidadesJSON(tempNomeBusca, PosicaoPaginaAtual);
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
}
