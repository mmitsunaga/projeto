package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaFinalidadeModeloCustaDt;
import br.gov.go.tj.projudi.ps.GuiaFinalidadeModeloCustaPs;
import br.gov.go.tj.utils.FabricaConexao;

public class GuiaFinalidadeModeloCustaNe extends Negocio {

	private static final long serialVersionUID = 7115085445814629675L;
		
	protected GuiaFinalidadeModeloCustaDt obDados;
	
	public GuiaFinalidadeModeloCustaNe() {
			 
		obLog = new LogNe(); 
		obDados = new GuiaFinalidadeModeloCustaDt(); 
	}
	
	/**
	 * Método que consulta o cálculo e seus itens trazendo o código de arrecadação e sua descrição para a apresentação na guia.
	 * @param String idGuiaTipo
	 * @param String idFinalidade
	 * @return List
	 * @throws Exception
	 */
	public List consultarItensGuia(String idGuiaTipo, String idFinalidade) throws Exception {
		List listaGuiaCustaModelo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			GuiaFinalidadeModeloCustaPs obPersistencia = new GuiaFinalidadeModeloCustaPs(obFabricaConexao.getConexao());
			
			listaGuiaCustaModelo = obPersistencia.consultarItensGuia(idGuiaTipo, idFinalidade);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaGuiaCustaModelo;
	}
}
