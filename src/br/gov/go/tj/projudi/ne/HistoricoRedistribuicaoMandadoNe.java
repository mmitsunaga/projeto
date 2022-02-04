package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.HistoricoRedistribuicaoMandadosDt;
import br.gov.go.tj.projudi.ps.HistoricoRedistribuicaoMandadoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class HistoricoRedistribuicaoMandadoNe {
	  

	public void cadastraHistoricoRedistribuicao(String idUsuServAnt, String idUsuServAtual, String idEscAnterior, String idEscAtual, String idMandJud, 
			     String motivo, String idMandTipoRedist, FabricaConexao obFabricaConexao) throws Exception {
		
		HistoricoRedistribuicaoMandadosDt objDt = new HistoricoRedistribuicaoMandadosDt();		
		HistoricoRedistribuicaoMandadoPs obPersistencia = new HistoricoRedistribuicaoMandadoPs(obFabricaConexao.getConexao());
		
		objDt.setIdMandJud(idMandJud);
		
		objDt.setIdUsuServAnt(idUsuServAnt);
		objDt.setIdUsuServAtual(idUsuServAtual);
		objDt.setMotivo(motivo);
		objDt.setIdEscAnterior(idEscAnterior);
		objDt.setIdEscAtual(idEscAtual);
		objDt.setIdMandTipoRedist(idMandTipoRedist);
		obPersistencia.cadastraHistoricoRedistribuicao(objDt);
	}
}
