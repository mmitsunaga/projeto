package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaFinalidadeModeloDt;
import br.gov.go.tj.projudi.ps.GuiaFinalidadeModeloPs;
import br.gov.go.tj.utils.FabricaConexao;

public class GuiaFinalidadeModeloNe extends Negocio {

	private static final long serialVersionUID = 6898941924537058347L;
	
	
	protected GuiaFinalidadeModeloDt obDados;


	public GuiaFinalidadeModeloNe() {

		obLog = new LogNe(); 

		obDados = new GuiaFinalidadeModeloDt(); 

	}
	
	/**
	 * Método para consultar lista de GuiaFinalidadeModelo pelo idGuiaTipo.
	 * @param String idGuiaTipo
	 * @return List
	 * @throws Exception
	 */
	public List consultarGuiaFinalidadeModelo(String idGuiaTipo) throws Exception {
		List listaGuiaModelo = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaFinalidadeModeloPs obPersistencia = new GuiaFinalidadeModeloPs(obFabricaConexao.getConexao());
			listaGuiaModelo = obPersistencia.consultarGuiaFinalidadeModelo(idGuiaTipo); 
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaModelo;
	}
	
	/**
	 * Método para consultar GuiaFinalidadeModelo pelo idGuiaTipo e idFinalidade.
	 * @param String idGuiaTipo
	 * @param String idFinalidade
	 * @return GuiaModeloDt
	 * @throws Exception
	 */
	public GuiaFinalidadeModeloDt consultarGuiaFinalidadeModelo(String idGuiaTipo, String idFinalidade) throws Exception {
		GuiaFinalidadeModeloDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaFinalidadeModeloPs obPersistencia = new GuiaFinalidadeModeloPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarGuiaFinalidadeModelo(idGuiaTipo, idFinalidade); 
			obDados.copiar(dtRetorno);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return dtRetorno;
	}
}
