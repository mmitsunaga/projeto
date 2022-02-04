package br.gov.go.tj.projudi.ne;

import br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoFisicoDt;
import br.gov.go.tj.projudi.ps.AudienciaProcessoFisicoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class AudienciaProcessoFisicoNe extends AudienciaProcessoNe {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1787730558528154555L;
	
	public boolean agendarAudienciaProcessoAutomaticoServentiaCargo(String id_ServentiaCargo, String audienciaTipoCodigo, ProcessoFisicoDt processoFisicoDt, FabricaConexao obFabricaConexao) throws Exception {
    	// SET CONEXÃO
		AudienciaProcessoFisicoPs obPersistencia = new  AudienciaProcessoFisicoPs(obFabricaConexao.getConexao());			
		
		return obPersistencia.agendarAudienciaProcessoAutomaticoServentiaCargo(id_ServentiaCargo, audienciaTipoCodigo, processoFisicoDt);			
    }
	
	public boolean agendarAudienciaProcessoAutomatico(String audienciaTipoCodigo, ProcessoFisicoDt processoFisicoDt, String id_Serventia, FabricaConexao obFabricaConexao) throws Exception {		
		
		// SET CONEXÃO
		AudienciaProcessoFisicoPs obPersistencia = new  AudienciaProcessoFisicoPs(obFabricaConexao.getConexao());			
		
		return obPersistencia.agendarAudienciaProcessoAutomatico(audienciaTipoCodigo, processoFisicoDt, id_Serventia);
    }   
	
	public boolean agendarAudienciaProcessoManual(AudienciaProcessoFisicoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
		boolean resultado;

		// SET CONEXÃO
		AudienciaProcessoFisicoPs obPersistencia = new  AudienciaProcessoFisicoPs(obFabricaConexao.getConexao());			
		
		resultado = obPersistencia.agendarAudienciaProcessoManual(audienciaProcessoDt);
		
		if (resultado) {
			LogDt logDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), audienciaProcessoDt.getPropriedades());
			// COPIAR OBJETO DO TIPO "AUDIENCIAPROCESSODT" ATUALIZADO
			obDados.copiar(audienciaProcessoDt);

			// SALVAR LOG DA INSERÇÃO OU ATUALIZAÇÃO DO OBJETO DO TIPO "AUDIENCIAPROCESSODT"
			obLog.salvar(logDt, obFabricaConexao);	
		}
		
		return resultado;		
	}
	
	public void incluirAudienciaProcessoAgendamentoProcessoFisico(AudienciaProcessoFisicoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
		// SET CONEXÃO
		AudienciaProcessoFisicoPs obPersistencia = new  AudienciaProcessoFisicoPs(obFabricaConexao.getConexao());			
		
		obPersistencia.incluirAudienciaProcessoAgendamentoProcessoFisico(audienciaProcessoDt);
		
		LogDt logDt = new LogDt("AudienciaProcessoFisico", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), audienciaProcessoDt.getPropriedades(), "");
		// COPIAR OBJETO DO TIPO "AUDIENCIAPROCESSODT" ATUALIZADO
		obDados.copiar(audienciaProcessoDt);

		// SALVAR LOG DA INSERÇÃO OU ATUALIZAÇÃO DO OBJETO DO TIPO "AUDIENCIAPROCESSODT"
		obLog.salvar(logDt, obFabricaConexao);
				
	}
	
	public void retirarAudienciaProcesso(AudienciaProcessoFisicoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
		// SET CONEXÃO
		AudienciaProcessoFisicoPs obPersistencia = new  AudienciaProcessoFisicoPs(obFabricaConexao.getConexao());			
		
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo(String.valueOf(AudienciaProcessoStatusDt.LIVRE));
		audienciaProcessoDt.setCodigoTemp("");
		obPersistencia.retirarAudienciaProcesso(audienciaProcessoDt.getId());
		
		LogDt logDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), audienciaProcessoDt.getPropriedades());
		// COPIAR OBJETO DO TIPO "AUDIENCIAPROCESSODT" ATUALIZADO
		obDados.copiar(audienciaProcessoDt);

		// SALVAR LOG DA INSERÇÃO OU ATUALIZAÇÃO DO OBJETO DO TIPO "AUDIENCIAPROCESSODT"
		obLog.salvar(logDt, obFabricaConexao);	
	}
	
	public void retirarAudienciaProcessoFisico(AudienciaProcessoFisicoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {				
		// SET CONEXÃO
		AudienciaProcessoFisicoPs obPersistencia = new  AudienciaProcessoFisicoPs(obFabricaConexao.getConexao());			
		
		obPersistencia.retirarAudienciaProcessoAgendamentoProcessoFisico(audienciaProcessoDt);
		
		LogDt logDt = new LogDt("AudienciaProcessoFisico", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), "", audienciaProcessoDt.getPropriedades());
		// COPIAR OBJETO DO TIPO "AUDIENCIAPROCESSODT" ATUALIZADO
		obDados.copiar(audienciaProcessoDt);

		// SALVAR LOG DA INSERÇÃO OU ATUALIZAÇÃO DO OBJETO DO TIPO "AUDIENCIAPROCESSODT"
		obLog.salvar(logDt, obFabricaConexao);	
	}
}
