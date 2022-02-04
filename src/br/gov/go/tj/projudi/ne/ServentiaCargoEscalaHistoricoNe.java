package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.ps.ServentiaCargoEscalaStatusHistoricoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ServentiaCargoEscalaHistoricoNe extends Negocio {

	private static final long serialVersionUID = -2963869012518017017L;
	
	public void inserir(ServentiaCargoEscalaDt serventiaCargoEscalaDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoEscalaStatusHistoricoPs obPersistencia = new ServentiaCargoEscalaStatusHistoricoPs(obFabricaConexao.getConexao());
			
			obPersistencia.inserir(serventiaCargoEscalaDt);
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
	}
}