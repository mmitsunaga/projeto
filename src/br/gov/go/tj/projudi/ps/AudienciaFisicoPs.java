package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.AudienciaFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.RetornoAudienciaMarcada;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AudienciaFisicoPs extends AudienciaPs {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1038282211811033465L;

	public AudienciaFisicoPs(Connection conexao){
		super(conexao);		
	}
	
	public RetornoAudienciaMarcada obtenhaAudienciaTipoAgendamentoProcessoFisicoASerRealizada(String numeroProcessoCompleto) throws Exception {
        String sql = "";
        ResultSetTJGO rs1 = null;
        RetornoAudienciaMarcada audienciaMarcada = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
        sql = " SELECT AUDI_TIPO_CODIGO, DATA_AGENDADA, SERV ";
        sql += " FROM AUDI A INNER JOIN AUDI_TIPO AT ON AT.ID_AUDI_TIPO = A.ID_AUDI_TIPO ";
        sql += " INNER JOIN AUDI_PROC AP ON AP.ID_AUDI = A.ID_AUDI ";
        sql += " INNER JOIN AUDI_PROC_FISICO APF ON APF.ID_AUDI_PROC = AP.ID_AUDI_PROC ";
        sql += " INNER JOIN SERV S ON S.ID_SERV = A.ID_SERV ";
        sql += " WHERE PROC_NUMERO_COMPLETO = ? "; ps.adicionarLong(Funcoes.obtenhaSomenteNumeros(numeroProcessoCompleto));
        sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
        
        try{
            rs1 = consultar(sql, ps);
            if (rs1.next()) {
            	audienciaMarcada = new RetornoAudienciaMarcada();
            	audienciaMarcada.setTipoAudiencia(rs1.getString("AUDI_TIPO_CODIGO"));
            	audienciaMarcada.setDataAudiencia(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
            	audienciaMarcada.setServentia(rs1.getString("SERV"));
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return audienciaMarcada;
    }
	
	public AudienciaFisicoDt getUltimaAudienciaMarcadaAgendamentoAutomatico(String audienciaTipoCodigo, ProcessoFisicoDt processoDt) throws Exception {
        String sql = "";
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
        sql = "SELECT ID_AUDI, ID_AUDI_TIPO, AUDI_TIPO_CODIGO, AUDI_TIPO, DATA_AGENDADA, DATA_MOVI_AUDI, RESERVADA, ID_AUDI_PROC, ID_AUDI_PROC_STATUS, ID_SERV_CARGO, SERV_CARGO, ID_PROC FROM PROJUDI.VIEW_AUDI_COMPLETA ap";
		sql += " WHERE ap.ID_PROC IS NULL AND ap.CODIGO_TEMP_AUDI_PROC = ? AND AUDI_TIPO_CODIGO = ? AND ap.DATA_MOVI_AUDI_PROC IS NULL ";
		ps.adicionarLong(Funcoes.obtenhaSomenteNumeros(processoDt.getNumeroProcesso()));
		ps.adicionarLong(audienciaTipoCodigo);
		sql += " AND ap.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql += " ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC, ap.ID_AUDI_PROC";
        try{	        	       	
        	
            rs1 = consultar(sql, ps);
            if (rs1.next()) {
                return prepararAudienciaLivreAgendamento(rs1);
            }
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return null;
    }
	
	private AudienciaFisicoDt prepararAudienciaLivreAgendamento(ResultSetTJGO rs1) throws Exception{
        // PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIADT"
		AudienciaFisicoDt audienciaDt = new AudienciaFisicoDt();
        audienciaDt.setId(rs1.getString("ID_AUDI"));
        audienciaDt.setId_AudienciaTipo(rs1.getString("ID_AUDI_TIPO"));
        audienciaDt.setAudienciaTipoCodigo(rs1.getString("AUDI_TIPO_CODIGO"));
        audienciaDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
        audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
        audienciaDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI")));
        audienciaDt.setReservada(Funcoes.FormatarLogico(rs1.getString("RESERVADA")));

        // PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIAPROCESSODT"
        AudienciaProcessoFisicoDt audienciaProcessoDt = new AudienciaProcessoFisicoDt();
        audienciaProcessoDt.setId(rs1.getString("ID_AUDI_PROC"));
        audienciaProcessoDt.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
        audienciaProcessoDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
        audienciaProcessoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));

        // Adicionando objeto do tipo "AudiênciaProcessoDt" à uma lista do
        // objeto do
        // tipo "AudiênciaDt"
        audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
        audienciaDt.getAudienciaProcessoDt().setId_Processo(rs1.getString("ID_PROC"));

        // RETORNO
        return audienciaDt;
    }
	
	public void inserirVinculoProcessoFisico(AudienciaFisicoDt audienciaDt, ProcessoFisicoDt processoFisicoDt) throws Exception {
    	String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO PROJUDI.AUDI_PROC_FISICO (";
		SqlValores = " VALUES (";
		 // Audiência processo
		if (!(audienciaDt.getAudienciaProcessoDt().getId().length() == 0)){
			SqlCampos+= "ID_AUDI_PROC ";
			SqlValores += ",?";
			ps.adicionarLong(audienciaDt.getAudienciaProcessoDt().getId());
		}        
		// Número processo
        if (!(processoFisicoDt.getNumeroProcesso().length() == 0)){
        	SqlCampos += ",PROC_NUMERO_COMPLETO ";
        	SqlValores+= ",?";
        	ps.adicionarLong(Funcoes.obtenhaSomenteNumeros(processoFisicoDt.getNumeroProcesso()));
        }
        // Promovente
        if (!(processoFisicoDt.getPromovente().length() == 0)){
        	SqlCampos += ",PROMOVENTE ";
        	SqlValores+= ",?";
        	ps.adicionarString(processoFisicoDt.getPromovente());
        }
        // Promovido
        if (!(processoFisicoDt.getPromovido().length() == 0)){
        	SqlCampos += ",PROMOVIDO ";
        	SqlValores+= ",?";
        	ps.adicionarString(processoFisicoDt.getPromovido());
        }
        // Serventia
        if (!(processoFisicoDt.getEscrivania().length() == 0)){
        	SqlCampos += ",SERVENTIA ";
        	SqlValores+= ",?";
        	ps.adicionarString(processoFisicoDt.getEscrivania());
        }
        SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");
 		
        // Executar SQL            
        audienciaDt.setId_AudienciaProcessoFisico(executarInsert(Sql, "ID_AUDI_PROC_FISICO", ps));
                
    }
	
	 public AudienciaFisicoDt consultarAudienciaLivreCompleta(String id_Audiencia) throws Exception {
		AudienciaFisicoDt audienciaCompleta = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		// SQL
		String sql = null;
		sql = "SELECT ID_AUDI, AUDI_TIPO, ID_AUDI_TIPO, ID_SERV, SERV, DATA_AGENDADA, DATA_MOVI_AUDI, RESERVADA, ";
		sql += " CODIGO_TEMP, AUDI_TIPO_CODIGO, ID_AUDI_PROC, ID_AUDI_PROC_STATUS, AUDI_PROC_STATUS, ID_SERV_CARGO, ";
		sql += " SERV_CARGO, ID_PROC, PROC_NUMERO, DATA_MOVI_AUDI_PROC, AUDI_PROC_STATUS_CODIGO, ID_ARQ_FINALIZACAO ";
		sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE vac.ID_AUDI = ? ";
		ps.adicionarLong(id_Audiencia);
		
		// RESULTSET
		ResultSetTJGO rs1 = null;
		try{
		    rs1 = consultar(sql, ps);
		
		    if (rs1.next()) {
		        audienciaCompleta = prepararAudienciaLivreCompleta(rs1);
		    }
		} finally{
		    try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		return audienciaCompleta;
    }
	 
	 private AudienciaFisicoDt prepararAudienciaLivreCompleta(ResultSetTJGO rs1) throws Exception{
        // PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIADT"
		AudienciaFisicoDt audienciaDt = new AudienciaFisicoDt();
        associarAudienciaDt(audienciaDt, rs1);

        // PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIAPROCESSODT"
        AudienciaProcessoFisicoDt audienciaProcessoDt = new AudienciaProcessoFisicoDt();
        AudienciaProcessoFisicoPs audienciaProcessoPs = new AudienciaProcessoFisicoPs();
        audienciaProcessoPs.associarAudienciaProcessoDt(audienciaProcessoDt, rs1);

        // LISTA CONTENDO OBJETO(S) DO TIPO "AUDIÊNCIAPROCESSODT"
        // Adicionando objeto do tipo "AudiênciaProcessoDt" à uma lista do
        // objeto do
        // tipo "AudiênciaDt"
        audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);

        // RETORNO
        return audienciaDt;
    }
}
