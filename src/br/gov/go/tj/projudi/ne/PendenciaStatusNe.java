package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.PendenciaPs;
import br.gov.go.tj.projudi.ps.PendenciaStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

public class PendenciaStatusNe extends PendenciaStatusNeGen {

	private static final long serialVersionUID = -6238245505995052739L;

	public String Verificar(PendenciaStatusDt dados) {

		String stRetorno = "";

		if (dados.getPendenciaStatus().length() == 0)
			stRetorno += "O Campo PendenciaStatus é obrigatório.";
		if (dados.getPendenciaStatusCodigo().length() == 0)
			stRetorno += "O Campo PendenciaStatusCodigo é obrigatório.";
		return stRetorno;

	}

	/**
	 * Método que obtém registro de PendenciaStatusDt referente ao código
	 * passado
	 */
	public PendenciaStatusDt consultarPendenciaStatusCodigo(int pendenciaStatusCodigo) throws Exception {
		PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaStatusPs obPersistencia = new  PendenciaStatusPs(obFabricaConexao.getConexao());
			pendenciaStatusDt = obPersistencia.consultarPendenciaStatusCodigo(pendenciaStatusCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return pendenciaStatusDt;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaStatusPs obPersistencia = new  PendenciaStatusPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
}
