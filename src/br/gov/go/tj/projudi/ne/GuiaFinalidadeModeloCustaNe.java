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
	 * M�todo que consulta o c�lculo e seus itens trazendo o c�digo de arrecada��o e sua descri��o para a apresenta��o na guia.
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
