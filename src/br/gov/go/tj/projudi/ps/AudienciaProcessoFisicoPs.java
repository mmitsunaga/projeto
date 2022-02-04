package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoFisicoDt;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;

public class AudienciaProcessoFisicoPs extends AudienciaProcessoPs {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8492319971379193288L;
	
	public AudienciaProcessoFisicoPs( ) {}
	
	public AudienciaProcessoFisicoPs(Connection conexao){
		super(conexao);
	}
	
	public boolean agendarAudienciaProcessoAutomaticoServentiaCargo(String id_ServentiaCargo, String audienciaTipoCodigo, ProcessoFisicoDt processoFisicoDt) throws Exception{
    	return agendarAudienciaProcesso(null, audienciaTipoCodigo, processoFisicoDt, "", id_ServentiaCargo);
    }
	
	private boolean agendarAudienciaProcesso(AudienciaProcessoFisicoDt audienciaProcessoDt, String audienciaTipoCodigo, ProcessoFisicoDt processoFisicoDt, String id_Serventia, String id_ServentiaCargo) throws Exception{		
		if (audienciaProcessoDt != null) {
			return alterarAudienciaProcessoAgendamento(audienciaProcessoDt);
		} else {
			return agendarAudienciaProcessoAutomatico(audienciaTipoCodigo, processoFisicoDt, id_Serventia, id_ServentiaCargo);
		}		
	}
	
	private boolean agendarAudienciaProcessoAutomatico(String audienciaTipoCodigo, ProcessoFisicoDt processoFisicoDt, String id_Serventia, String id_ServentiaCargo) throws Exception{
		if (processoFisicoDt == null || processoFisicoDt.getNumeroProcesso() == null || processoFisicoDt.getNumeroProcesso().length() == 0){
			throw new MensagemException("O processo não foi informado.");
		}
		
		String sNomeView = "VIEW_PROX_AUDI_LIVRE_AUTO";
		int prazo = 0;
		if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo()) {
			sNomeView = "VIEW_PROX_AUDI_LIVREAUTOCEJUSC";
			prazo = ProjudiPropriedades.getInstance().getQuantidadeDiasMarcarAudienciaAutomaticaCejusc();
		} else if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo()) {
			sNomeView = "VIEW_PROX_AUDI_LIVREAUTOCEJUSC";
			prazo = ProjudiPropriedades.getInstance().getQuantidadeDiasMarcarAudienciaDPVATAutomaticaCejusc();
		} else if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo()) {
			sNomeView = "VIEW_PROX_AUDI_LIVREAUTOCEJUSC";
			prazo = ProjudiPropriedades.getInstance().getQuantidadeDiasMarcarMediacaoAutomaticaCejusc();
		} 
		
		String sql = "";
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
    	sql = " UPDATE PROJUDI.AUDI_PROC ap SET ap.CODIGO_TEMP = ?";
    	ps.adicionarLong(Funcoes.obtenhaSomenteNumeros(processoFisicoDt.getNumeroProcesso()));	
    	
        sql += " , ap.ID_AUDI_PROC_STATUS = (SELECT ID_AUDI_PROC_STATUS FROM PROJUDI.AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?)";
    	ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
    	sql += " WHERE ap.ID_AUDI_PROC = (";
    	
    	//Resgata próxima audiência livre e já dá update
    	sql += " SELECT ap.ID_AUDI_PROC FROM PROJUDI.AUDI_PROC ap";
    	sql += " INNER JOIN (SELECT ID_AUDI_PROC FROM PROJUDI." + sNomeView;
    	sql += " 			  WHERE AUDI_TIPO_CODIGO = ?";
        ps.adicionarLong(audienciaTipoCodigo);
        if (prazo > 0) {
        	sql += " AND DATA_AGENDADA >= (SYSDATE + ?) ";
        	ps.adicionarLong(prazo);
        }
        if (id_Serventia != null && id_Serventia.trim().length() > 0){
        	//independente do Cargo
        	sql += " 		  AND ID_SERV = ?";
        	ps.adicionarLong(id_Serventia);
        }
        else if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
        	//sql o cargo
        	sql += " 		  AND ID_SERV_CARGO = ?";
        	ps.adicionarLong(id_ServentiaCargo);
        }
        sql += " 			  AND ROWNUM = ?) pa";
        ps.adicionarLong(1);
        sql += " 	ON ap.ID_AUDI_PROC = pa.ID_AUDI_PROC";        
        sql += " WHERE ap.ID_AUDI_PROC_STATUS = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.LIVRE);
		sql += " AND ap.ID_PROC IS NULL";
		sql += " AND ap.CODIGO_TEMP IS NULL"; 
		sql +=")";
        	
        return executarUpdateDelete(sql, ps) > 0;		 
    }
	
	public boolean alterarAudienciaProcessoAgendamento(AudienciaProcessoFisicoDt audienciaProcessoDt) throws Exception{
		
		if (audienciaProcessoDt.getProcessoFisicoDt() == null || audienciaProcessoDt.getProcessoFisicoDt().getNumeroProcesso() == null || audienciaProcessoDt.getId() == null || audienciaProcessoDt.getProcessoFisicoDt().getNumeroProcesso().length() == 0){
			throw new MensagemException("O processo não foi informado.");
		}
		
		String sql;
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
			
		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " CODIGO_TEMP = ?"; ps2.adicionarLong(Funcoes.obtenhaSomenteNumeros(audienciaProcessoDt.getProcessoFisicoDt().getNumeroProcesso()));						
		
		if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo() != null) {
			sql += ", ID_AUDI_PROC_STATUS = (SELECT ID_AUDI_PROC_STATUS FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?)";			
			ps2.adicionarLong(audienciaProcessoDt.getAudienciaProcessoStatusCodigo());
		} 
		
		sql += " WHERE ID_AUDI_PROC = ?"; ps2.adicionarLong(audienciaProcessoDt.getId());			
		sql += " AND ID_PROC IS NULL ";
		sql += " AND CODIGO_TEMP IS NULL ";
		sql += " AND ID_AUDI_PROC_STATUS = ?"; ps2.adicionarLong(AudienciaProcessoStatusDt.LIVRE);
		
		return executarUpdateDelete(sql, ps2) > 0;
	}
	
	public boolean agendarAudienciaProcessoAutomatico(String audienciaTipoCodigo, ProcessoFisicoDt processoFisicoDt, String id_Serventia) throws Exception{
        return agendarAudienciaProcesso(null, audienciaTipoCodigo, processoFisicoDt, id_Serventia, "");
    }
	
	public boolean agendarAudienciaProcessoManual(AudienciaProcessoFisicoDt audienciaProcessoDt) throws Exception{
		return agendarAudienciaProcesso(audienciaProcessoDt, "", null, "", "");
	}
	
	public void incluirAudienciaProcessoAgendamentoProcessoFisico(AudienciaProcessoFisicoDt audienciaProcessoDt) throws Exception {		
		if (audienciaProcessoDt.getProcessoFisicoDt() == null || audienciaProcessoDt.getProcessoFisicoDt().getNumeroProcesso() == null || audienciaProcessoDt.getId() == null || audienciaProcessoDt.getProcessoFisicoDt().getNumeroProcesso().length() == 0){
			throw new MensagemException("O processo não foi informado.");
		}
		
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "INSERT INTO PROJUDI.AUDI_PROC_FISICO (ID_AUDI_PROC, PROC_NUMERO_COMPLETO, PROMOVENTE, PROMOVIDO, SERVENTIA, SERVENTIA_CODIGO) VALUES ";
		sql += "(?, ?, ?, ?, ?, ?)";
		ps.adicionarLong(audienciaProcessoDt.getId());
		ps.adicionarString(Funcoes.obtenhaSomenteNumeros(audienciaProcessoDt.getProcessoFisicoDt().getNumeroProcesso()));
		ps.adicionarString(audienciaProcessoDt.getProcessoFisicoDt().getPromovente());
		ps.adicionarString(audienciaProcessoDt.getProcessoFisicoDt().getPromovido());
		ps.adicionarString(audienciaProcessoDt.getProcessoFisicoDt().getEscrivania());
		ps.adicionarString(audienciaProcessoDt.getProcessoFisicoDt().getEscrivaniaCodigo());
		
		audienciaProcessoDt.setIdAudienciaProcessoFisico(executarInsert(sql, "ID_AUDI_PROC_FISICO", ps));
	}
	
	public boolean retirarAudienciaProcesso(String id_AudienciaProcesso) throws Exception{		
		String sql;
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
			
		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " CODIGO_TEMP = NULL";
		sql += ", ID_AUDI_PROC_STATUS = ? "; 
		ps2.adicionarLong(AudienciaProcessoStatusDt.LIVRE);		
		sql += " WHERE ID_AUDI_PROC = ?"; ps2.adicionarLong(id_AudienciaProcesso);			
		sql += " AND ID_PROC IS NULL ";		
		
		return executarUpdateDelete(sql, ps2) > 0;
	}
	
	public void retirarAudienciaProcessoAgendamentoProcessoFisico(AudienciaProcessoFisicoDt audienciaProcessoDt) throws Exception {		
		if (audienciaProcessoDt.getProcessoFisicoDt() == null || audienciaProcessoDt.getProcessoFisicoDt().getNumeroProcesso() == null || audienciaProcessoDt.getId() == null || audienciaProcessoDt.getProcessoFisicoDt().getNumeroProcesso().length() == 0){
			throw new MensagemException("O processo não foi informado.");
		}
		
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "DELETE FROM PROJUDI.AUDI_PROC_FISICO WHERE ID_AUDI_PROC_FISICO = ? ";
		ps.adicionarLong(audienciaProcessoDt.getIdAudienciaProcessoFisico());
		
		executarUpdateDelete(sql, ps);
	}
}
