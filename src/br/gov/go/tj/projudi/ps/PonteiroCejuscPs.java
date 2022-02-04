package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroCejuscDt;
import br.gov.go.tj.projudi.dt.PonteiroCejuscStatuDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.EstatisticaConciliadorCejuscDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class PonteiroCejuscPs extends PonteiroCejuscPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7736810582854333676L;
	
	public PonteiroCejuscPs(Connection conexao){
		Conexao = conexao;
	}
	
	
	public String distribuir(String data, String id_audiencia_tipo, String id_serventia, int diaSemana, int periodo) throws Exception {
	
        
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        String stPonteiro="";
        
        String sql = null;
                
        sql = " SELECT CD.ID_usu_cejusc, DBMS_RANDOM.value(0, 1) as SORTEIO ";   
        sql += "  FROM PROJUDI.CEJUSC_DISPONIBILIDADE CD ";
        
        sql += " INNER JOIN USU_CEJUSC UC ON UC.ID_USU_CEJUSC = CD.ID_USU_CEJUSC ";
        sql += " INNER JOIN USU U ON U.ID_USU = UC.ID_USU ";
        
        sql += " WHERE CD.ID_SERV = ?";   ps.adicionarLong(id_serventia);
        sql += "    AND ID_AUDI_TIPO = ? "; ps.adicionarLong(id_audiencia_tipo);
        
        switch (diaSemana) {
			case PonteiroCejuscDt.DOMINGO:
				sql += "    AND CD.DOMINGO "; 	
				break;
			case PonteiroCejuscDt.SEGUNDA:
				sql += "    AND CD.SEGUNDA "; 	
				break;
			case PonteiroCejuscDt.TERCA:
				sql += "    AND CD.TERCA "; 	
				break;
			case PonteiroCejuscDt.QUARTA:
				sql += "    AND CD.QUARTA ";	
				break;
			case PonteiroCejuscDt.QUINTA:
				sql += "    AND CD.QUINTA "; 	
				break;
			case PonteiroCejuscDt.SEXTA:
				sql += "    AND CD.SEXTA ";	
				break;	
			case PonteiroCejuscDt.SABADO:
				sql += "    AND CD.SABADO "; 	
				break;			
		}      
        
    	sql += " IN (?,?) "; 		ps.adicionarLong(periodo); ps.adicionarLong(PonteiroCejuscDt.AMBOS_PERIODOS);
    	
        sql += " AND UC.CODIGO_STATUS_ATUAL = ? "; ps.adicionarLong(UsuarioCejuscDt.CODIGO_STATUS_APROVADO);
        sql += " AND U.ATIVO = ? "; ps.adicionarLong(UsuarioDt.ATIVO);

    	
        String stPeriodo = "%VESPERTINO%";
        if (periodo == PonteiroCejuscDt.PERIODO_MATUTINO ){
            stPeriodo = "%MATUTINO%";
        }
        
        
        
        sql += " AND UC.ID_USU_CEJUSC NOT IN (";
        sql += "				SELECT PC.ID_USU_CEJUSC FROM Ponteiro_Cejusc pc ";
        sql += "				INNER JOIN SERV_CARGO sc on pc.ID_SERV_CARGO_BANCA = sc.id_Serv_cargo ";
        sql += "				WHERE PC.ID_USU_CEJUSC = CD.ID_usu_cejusc ";
        sql += "				AND DATA = ? "; 	ps.adicionarDate(data);
        sql += "				AND PC.ID_PONTEIRO_CEJUSC_STATUS IN (?,?) "; ps.adicionarLong(PonteiroCejuscStatuDt.CONFIRMADO); ps.adicionarLong(PonteiroCejuscStatuDt.AVISADO);
        sql += "				AND upper(sc.serv_cargo) like ? ) "; 	ps.adicionarString(stPeriodo);
        
        
        
        sql += " ORDER BY SORTEIO ";
        
        ResultSetTJGO rs1 = null;
        try{
            rs1 = consultar(sql, ps);

            if (rs1.next()) {
            	stPonteiro = rs1.getString("ID_USU_CEJUSC");
            	
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return stPonteiro;
	}
	
/*
 * TODO: Depois da mudança do dia 12/04/2018, que não sorteia o conciliador caso ele já tenha sido sorteado no mesmo dia e período,
 * talvez este método não seja mais necessário pois, teoricamente, a substituição não irá mais repetir o conciliador. Verificar e
 * refatorar se for o caso.
 * 
 * @author hrrosa
 * 	
 */
public String distribuirComExcecao(String data, String id_audiencia_tipo, String id_serventia, int diaSemana, int periodo, String id_usu_excecao) throws Exception {
        
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        String stPonteiro = null;
        
        String sql = null;
                
        sql = " SELECT CD.ID_usu_cejusc, DBMS_RANDOM.value(0, 1) as SORTEIO, nvl((SELECT  count(1) FROM ponteiro_cejusc pl  WHERE pl.ID_SERV = ? and pl.ID_usu_cejusc = CD.ID_usu_cejusc AND ID_AUDI_TIPO = ?  ),0) as qtd "; ps.adicionarLong(id_serventia); ps.adicionarLong(id_audiencia_tipo);       
        sql += "  FROM PROJUDI.CEJUSC_DISPONIBILIDADE CD ";
        
        
        sql += " INNER JOIN USU_CEJUSC UC ON UC.ID_USU_CEJUSC = CD.ID_USU_CEJUSC ";
        sql += " INNER JOIN USU U ON U.ID_USU = UC.ID_USU ";
        
        sql += " WHERE CD.ID_SERV = ?" ;   ps.adicionarLong(id_serventia);
        sql += "    AND ID_AUDI_TIPO = ? "; ps.adicionarLong(id_audiencia_tipo);
        sql += "    AND CD.ID_USU_CEJUSC != ? "; ps.adicionarLong(id_usu_excecao);
        
        switch (diaSemana) {
			case PonteiroCejuscDt.DOMINGO:
				sql += "    AND CD.DOMINGO "; 	
				break;
			case PonteiroCejuscDt.SEGUNDA:
				sql += "    AND CD.SEGUNDA "; 	
				break;
			case PonteiroCejuscDt.TERCA:
				sql += "    AND CD.TERCA "; 	
				break;
			case PonteiroCejuscDt.QUARTA:
				sql += "    AND CD.QUARTA ";	
				break;
			case PonteiroCejuscDt.QUINTA:
				sql += "    AND CD.QUINTA "; 	
				break;
			case PonteiroCejuscDt.SEXTA:
				sql += "    AND CD.SEXTA ";	
				break;	
			case PonteiroCejuscDt.SABADO:
				sql += "    AND CD.SABADO "; 	
				break;			
		}      
        
        sql += " IN (?,?) "; 		ps.adicionarLong(periodo); ps.adicionarLong(PonteiroCejuscDt.AMBOS_PERIODOS);
        
        sql += " AND UC.CODIGO_STATUS_ATUAL = ? "; ps.adicionarLong(UsuarioCejuscDt.CODIGO_STATUS_APROVADO);
        sql += " AND U.ATIVO = ? "; ps.adicionarLong(UsuarioDt.ATIVO);
        
        String stPeriodo = "%VESPERTINO%";
        if (periodo == PonteiroCejuscDt.PERIODO_MATUTINO ){
            stPeriodo = "%MATUTINO%";
        }
        sql += " AND UC.ID_USU_CEJUSC NOT IN (";
        sql += "				SELECT PC.ID_USU_CEJUSC FROM Ponteiro_Cejusc pc ";
        sql += "				INNER JOIN SERV_CARGO sc on pc.ID_SERV_CARGO_BANCA = sc.id_Serv_cargo ";
        sql += "				WHERE PC.ID_USU_CEJUSC = CD.ID_usu_cejusc ";
        sql += "				AND DATA = ? "; 	ps.adicionarDate(data);
        sql += "				AND PC.ID_PONTEIRO_CEJUSC_STATUS IN (?,?) "; ps.adicionarLong(PonteiroCejuscStatuDt.CONFIRMADO); ps.adicionarLong(PonteiroCejuscStatuDt.AVISADO);
        sql += "				AND upper(sc.serv_cargo) like ? ) "; 	ps.adicionarString(stPeriodo);
        
        
        
        
        sql += " ORDER BY SORTEIO ";
        
        ResultSetTJGO rs1 = null;
        try{
            rs1 = consultar(sql, ps);

            if (rs1.next()) {
            	stPonteiro = rs1.getString("ID_usu_cejusc");
            	
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return stPonteiro;
	}

	public List< Map<String,String> > consultaPorData(String data, String id_serventia) throws Exception {
        
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        Map<String, String> map = new HashMap<String, String>();
        List< Map<String,String> > lisPonteiroCejusc = new ArrayList< Map<String,String> >();
        
        String sql = null;
                
        sql = " SELECT  PC.ID_PONTEIRO_CEJUSC, PC.ID_USU_CEJUSC, PC.ID_USU_SERV_CONFIRMOU, PC.ID_USU_SERV_COMPARECEU, PC.ID_AUDI_TIPO ";       
        sql += "  FROM PROJUDI.PONTEIRO_CEJUSC PC ";
        sql += " WHERE PC.DATA = ? ";   ps.adicionarDate(data);
        sql += "    AND ID_SERV = ? "; ps.adicionarLong(id_serventia);
        
        
        sql += " ORDER BY DATA ";
        
        ResultSetTJGO rs1 = null;
        try{
        	
        	rs1 = consultar(sql, ps);

            while (rs1.next()) {
            	
            	map = new HashMap<String, String>();
            	map.put( "ID_PONTEIRO_CEJUSC", rs1.getString("ID_PONTEIRO_CEJUSC"));
            	map.put( "ID_USU_CEJUSC", rs1.getString("ID_USU_CEJUSC"));
            	map.put( "ID_USU_SERV_CONFIRMOU", rs1.getString("ID_USU_SERV_CONFIRMOU"));
            	map.put( "ID_USU_SERV_COMPARECEU", rs1.getString("ID_USU_SERV_COMPARECEU"));
            	map.put( "ID_AUDI_TIPO", rs1.getString("ID_AUDI_TIPO"));
            	lisPonteiroCejusc.add(map);
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return lisPonteiroCejusc;
	}
	
	public String consultaPorDataJSON(String data, String id_serventia, String posicaoPaginaAtual) throws Exception {
     
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
        String sqlComum = "";
        String sqlSelect = "";
        String sqlOrderBy = "";
        String retorno = "";
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        int qtdeColunas = 4;
                
        sqlSelect = " SELECT  PC.ID_PONTEIRO_CEJUSC ID, TO_CHAR( PC.DATA, 'dd/mm/yyyy') DESCRICAO1, SC.SERV_CARGO DESCRICAO2, USU.NOME DESCRICAO3, PCS.PONTEIRO_CEJUSC_STATUS DESCRICAO4 ";       
        sqlComum += "  FROM PROJUDI.PONTEIRO_CEJUSC PC, PROJUDI.PONTEIRO_CEJUSC_STATUS PCS, PROJUDI.USU_CEJUSC USU_CEJUSC, PROJUDI.USU USU, PROJUDI.SERV_CARGO SC ";
        sqlComum += " WHERE PC.DATA = ? ";   ps.adicionarDate(data);
        sqlComum += "    AND PC.ID_SERV = ? "; ps.adicionarLong(id_serventia);
        sqlComum += "    AND USU.ID_USU = USU_CEJUSC.ID_USU ";
        sqlComum += "    AND PC.ID_USU_CEJUSC = USU_CEJUSC.ID_USU_CEJUSC ";
        sqlComum += "    AND PC.ID_PONTEIRO_CEJUSC_STATUS = PCS.ID_PONTEIRO_CEJUSC_STATUS ";
        sqlComum += "    AND SC.ID_SERV_CARGO = PC.ID_SERV_CARGO_BANCA ";
        sqlOrderBy += " ORDER BY DATA ";
        
        
		
        try{
        	 rs1 = consultarPaginacao(sqlSelect+sqlComum+sqlOrderBy, ps, posicaoPaginaAtual);

             sqlSelect = "SELECT COUNT(1) AS QUANTIDADE ";
             rs2 = consultar(sqlSelect+sqlComum, ps);
             rs2.next();
             
             retorno = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e) {}
        }
        return retorno;
    }   
	
	public String consultaPonteiroCejuscDataStatusJSON(String data, String id_serventia, int status, String posicaoPaginaAtual) throws Exception {
	     
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
        String sqlComum = "";
        String sqlSelect = "";
        String sqlOrderBy = "";
        String retorno = "";
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        int qtdeColunas = 4;
                
        sqlSelect = " SELECT  PC.ID_PONTEIRO_CEJUSC ID, TO_CHAR( PC.DATA, 'dd/mm/yyyy') DESCRICAO1, SC.SERV_CARGO DESCRICAO2, USU.NOME DESCRICAO3, PCS.PONTEIRO_CEJUSC_STATUS DESCRICAO4 ";       
        sqlComum += "  FROM PROJUDI.PONTEIRO_CEJUSC PC, PROJUDI.PONTEIRO_CEJUSC_STATUS PCS, PROJUDI.USU_CEJUSC USU_CEJUSC, PROJUDI.USU USU, PROJUDI.SERV_CARGO SC ";
        sqlComum += " WHERE PC.DATA = ? ";   ps.adicionarDate(data);
        sqlComum += "    AND PC.ID_SERV = ? "; ps.adicionarLong(id_serventia);
        sqlComum += "    AND PC.ID_PONTEIRO_CEJUSC_STATUS = ? "; ps.adicionarLong(status);
        sqlComum += "    AND USU.ID_USU = USU_CEJUSC.ID_USU ";
        sqlComum += "    AND PC.ID_USU_CEJUSC = USU_CEJUSC.ID_USU_CEJUSC ";
        sqlComum += "    AND PC.ID_PONTEIRO_CEJUSC_STATUS = PCS.ID_PONTEIRO_CEJUSC_STATUS ";
        sqlComum += "    AND SC.ID_SERV_CARGO = PC.ID_SERV_CARGO_BANCA ";
        sqlOrderBy += " ORDER BY DATA ";
        
        
		
        try{
        	 rs1 = consultarPaginacao(sqlSelect+sqlComum+sqlOrderBy, ps, posicaoPaginaAtual);

             sqlSelect = "SELECT COUNT(1) AS QUANTIDADE ";
             rs2 = consultar(sqlSelect+sqlComum, ps);
             rs2.next();
             
             retorno = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e) {}
        }
        return retorno;
    }   
	
	public boolean isBancaOcupada(String id_serventia, String idCargo, String data) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql;
		boolean isOcupada = true;
		
		sql = " SELECT PC.ID_PONTEIRO_CEJUSC ";       
        sql += "  FROM PROJUDI.PONTEIRO_CEJUSC PC ";
        sql += " WHERE PC.DATA = ? ";   ps.adicionarDate(data);
        sql += "    AND ID_SERV = ? "; ps.adicionarLong(id_serventia);
        sql += "    AND ID_SERV_CARGO_BANCA = ? "; ps.adicionarLong(idCargo);
        
        ResultSetTJGO rs1 = null;
        try{
        	
        	rs1 = consultar(sql, ps);

            if (rs1.next()) {
            	
            	isOcupada = true;
            } else {
            	
            	isOcupada = false;
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return isOcupada;
		
	}

	
	/**
	 * Traz um relatório de quantidade de audiências realizadas por conciliador/mediador.
	 * 
	 * @param data
	 * @param id_serventia
	 * @return
	 * @throws Exception
	 */
	
	public String consultaRelatorioAudienciaConciliadorJSON(String dataInicial, String dataFinal, String posicaoPaginaAtual) throws Exception {
        
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        //List lisPonteiroCejusc = null;
        String retorno = null;
        String sqlComum = null;
        String sqlSelect = null;
        String sqlOrderBy = null;
        
        int qtdeColunas = 2;
                
//        sql = " SELECT  PC.ID_PONTEIRO_CEJUSC, PC.ID_USU_CEJUSC, PC.ID_USU_SERV_CONFIRMOU, PC.ID_USU_SERV_COMPARECEU, PC.ID_AUDI_TIPO ";       
//        sql += "  FROM PROJUDI.PONTEIRO_CEJUSC PC ";
//        sql += " WHERE PC.DATA = ? ";   ps.adicionarDate(data);
//        sql += "    AND ID_SERV = ? "; ps.adicionarLong(id_serventia);
//        
//        
//        sql += " ORDER BY DATA ";
        
        
        
        
        sqlSelect = "SELECT Id, descricao1, count(*) descricao2 ";
        
        sqlComum =  " FROM ( ";
        sqlComum += " SELECT PC.ID_USU_CEJUSC Id, USU.NOME descricao1, PC.DATA ";
        sqlComum += " FROM USU, USU_CEJUSC UC, PONTEIRO_CEJUSC PC  ";
        sqlComum += "	INNER JOIN AUDI_PROC AP ON PC.ID_SERV_CARGO_BANCA = AP.ID_SERV_CARGO ";
        sqlComum += "	INNER JOIN AUDI A ON AP.ID_AUDI = A.ID_AUDI ";
        sqlComum += " WHERE ";
        sqlComum += "	A.DATA_AGENDADA >= PC.DATA ";
        sqlComum += "	AND A.DATA_AGENDADA < (PC.DATA + 1) ";
        //sqlComum += "	AND PC.ID_USU_CEJUSC = ? "; ps.adicionarLong(3);
        sqlComum += "   AND UC.ID_USU_CEJUSC = PC.ID_USU_CEJUSC ";
        sqlComum += "   AND USU.ID_USU = UC.ID_USU ";
        sqlComum += "	AND AP.ID_AUDI_PROC_STATUS <> ? "; ps.adicionarLong(1);
        //sqlComum += "	AND PC.DATA = to_date('06/07/2016 00:00:00','dd/mm/yyyy hh24:mi:ss')";
        
        //sqlComum += "	AND PC.DATA = ?"; ps.adicionarDate("06/07/2016");
        sqlComum += "	AND PC.DATA >= ? "; ps.adicionarDate(dataInicial);
        sqlComum += "	AND PC.DATA <= ? "; ps.adicionarDate(dataFinal);
        
        sqlComum += "	) ";
        
        sqlComum += "	GROUP BY Id, descricao1 ";
        
        sqlOrderBy = " ORDER BY descricao2 DESC ";
        
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        
        try{
	       	 rs1 = consultarPaginacao(sqlSelect+sqlComum+sqlOrderBy, ps, posicaoPaginaAtual);
	       	 
	            //sqlSelect = "SELECT COUNT(1) AS QUANTIDADE ";
	            // O jeito convencional de fazer o count não funciona por conta do GROUP BY
	            rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE FROM (" + sqlSelect+sqlComum + ")", ps);
	            rs2.next();
	            retorno = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
       
       } finally{
           try{if (rs1 != null) rs1.close();} catch(Exception e) {}
           try{if (rs2 != null) rs2.close();} catch(Exception e) {}
       }

        return retorno;
	}
	
	/**
	 * Traz um relatório de quantidade de audiências realizadas por conciliador/mediador.
	 * 
	 * @param data
	 * @param id_serventia
	 * @return
	 * @throws Exception
	 */
	
	public List consultaRelatorioAudienciaConciliador(String dataInicial, String dataFinal) throws Exception {
        
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        List lista = new ArrayList<>();
        String sqlComum = null;
        String sqlSelect = null;
        String sqlOrderBy = null;
        HashMap<String, EstatisticaConciliadorCejuscDt> listaHash = new HashMap<String, EstatisticaConciliadorCejuscDt>();
        EstatisticaConciliadorCejuscDt estatisticaDt = null;
        
        sqlSelect = "SELECT Id, descricao1, descricao2, descricao3, descricao5, descricao6, descricao7, descricao8, count(*) descricao4 ";
        
        sqlComum =  " FROM ( ";
        sqlComum += " SELECT  PC.ID_USU_CEJUSC Id, USU.CPF descricao2, USU.NOME descricao1, PC.DATA, ATIPO.AUDI_TIPO_CODIGO descricao3, USU.PIS descricao5, UC.NUMERO_AGENCIA descricao6, UC.NUMERO_CONTA descricao7, UC.CODIGO_BANCO descricao8 ";
        sqlComum += " FROM AUDI_TIPO ATIPO, USU, USU_CEJUSC UC, PONTEIRO_CEJUSC PC  ";
        sqlComum += "	INNER JOIN AUDI_PROC AP ON PC.ID_SERV_CARGO_BANCA = AP.ID_SERV_CARGO ";
        sqlComum += "	INNER JOIN AUDI A ON AP.ID_AUDI = A.ID_AUDI ";
        sqlComum += " WHERE ";
        sqlComum += "	A.DATA_AGENDADA >= PC.DATA ";
        sqlComum += "	AND A.ID_AUDI_TIPO = ATIPO.ID_AUDI_TIPO ";
        sqlComum += "	AND ATIPO.AUDI_TIPO_CODIGO IN (?,?,?) ";
        	ps.adicionarLong( String.valueOf( AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ) );
        	ps.adicionarLong( String.valueOf( AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ) );
        	ps.adicionarLong( String.valueOf( AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo() ) );
        sqlComum += "	AND A.DATA_AGENDADA < (PC.DATA + 1) ";
        sqlComum += "   AND UC.ID_USU_CEJUSC = PC.ID_USU_CEJUSC ";
        sqlComum += "   AND USU.ID_USU = UC.ID_USU ";
        sqlComum += "	AND AP.ID_AUDI_PROC_STATUS <> ? "; ps.adicionarLong(1);
        sqlComum += "	AND PC.DATA >= ? "; ps.adicionarDate(dataInicial);
        sqlComum += "	AND PC.DATA <= ? "; ps.adicionarDate(dataFinal);
        
        sqlComum += "	) ";
        
        sqlComum += "	GROUP BY Id, descricao1, descricao2, descricao3, descricao5, descricao6, descricao7, descricao8 ";
        
        sqlOrderBy = " ORDER BY descricao1 DESC ";
        
        ResultSetTJGO rs1 = null;
        
        try{
            rs1 = consultar(sqlSelect+sqlComum+sqlOrderBy, ps);
            while(rs1.next()) {
            	//TODO: tratar aqui o processamento do resultset para gerar a lista correta
            	
            	estatisticaDt = listaHash.get(rs1.getString("descricao2"));
            	
            	if(  estatisticaDt == null) {
            	
	            	estatisticaDt = new EstatisticaConciliadorCejuscDt();
	            	
	            	if( rs1.getString("descricao2") != null )
	            		estatisticaDt.setcpf(rs1.getString("descricao2"));
	            	else
	            		estatisticaDt.setcpf("");
	            	
	            	if( rs1.getString("descricao1") != null )
	            		estatisticaDt.setnome(rs1.getString("descricao1"));
	            	else
	            		estatisticaDt.setnome("");
	            	
	            	if( rs1.getString("descricao7") != null )
	            		estatisticaDt.setnumeroConta(rs1.getString("descricao7"));
	            	else
	            		estatisticaDt.setnumeroConta("");
	            	
	            	if( rs1.getString("descricao8") != null )
	            		estatisticaDt.setcodigoBanco(rs1.getString("descricao8"));
	            	else
	            		estatisticaDt.setcodigoBanco("");
	            	
	            	if( rs1.getString("descricao5") != null )
	            		estatisticaDt.setpis(rs1.getString("descricao5"));
	            	else
	            		estatisticaDt.setpis("");
	            	
	            	if( rs1.getString("descricao6") != null )
	            		estatisticaDt.setagencia(rs1.getString("descricao6"));
	            	else
	            		estatisticaDt.setagencia("");
	            	
	            	if( rs1.getString("descricao3").equals(String.valueOf(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo()))  || 
	            		rs1.getString("descricao3").equals(String.valueOf(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo())) ){
	            		
	            		estatisticaDt.setqtdConciliacaoCejusc( rs1.getString("descricao4") );
	            		estatisticaDt.setqtdMediacaoCejusc("0");
	            	}
	            	else {
	            		if( rs1.getString("descricao3").equals( String.valueOf(AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo()) ) ) {
	            			estatisticaDt.setqtdMediacaoCejusc( rs1.getString("descricao4") );
	            			estatisticaDt.setqtdConciliacaoCejusc("0");
	            		}
	            	}
	            	
	            	estatisticaDt.setqtdTotal( String.valueOf( Integer.parseInt(estatisticaDt.getqtdConciliacaoCejusc()) + Integer.parseInt(estatisticaDt.getqtdMediacaoCejusc()) ) ); 
	            	listaHash.put(rs1.getString("descricao2"), estatisticaDt);
	            	
            	}
            	else {
            		
            		if( rs1.getString("descricao3").equals(String.valueOf(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo()))  || 
    	            		rs1.getString("descricao3").equals(String.valueOf(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo())) ){
    	            		
            				if(estatisticaDt.getqtdConciliacaoCejusc() != null && estatisticaDt.getqtdConciliacaoCejusc() != "") {
            					estatisticaDt.setqtdConciliacaoCejusc( String.valueOf( (Integer.parseInt(estatisticaDt.getqtdConciliacaoCejusc()) + Integer.parseInt(rs1.getString("descricao4"))) ) );
            				}
    	            	}
    	            	else {
    	            		if( rs1.getString("descricao3").equals( String.valueOf(AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo()) ) ) {
            					
    	            			if(estatisticaDt.getqtdMediacaoCejusc() != null && estatisticaDt.getqtdMediacaoCejusc() != "") {
    	            				estatisticaDt.setqtdMediacaoCejusc( String.valueOf( (Integer.parseInt(estatisticaDt.getqtdMediacaoCejusc()) + Integer.parseInt(rs1.getString("descricao4"))) ) );
    	            			}

    	            		}
    	            	}
            		
	            	estatisticaDt.setqtdTotal( String.valueOf( Integer.parseInt(estatisticaDt.getqtdConciliacaoCejusc()) + Integer.parseInt(estatisticaDt.getqtdMediacaoCejusc()) ) ); 
            		listaHash.replace(rs1.getString("descricao2"), estatisticaDt);
            		
            	}
            	
            }
            
       } finally{
           try{if (rs1 != null) rs1.close();} catch(Exception e) {}
       }

        lista = new ArrayList<EstatisticaConciliadorCejuscDt>(listaHash.values());
        
        return lista;
	}
	
}
