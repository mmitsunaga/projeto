package br.gov.go.tj.projudi.ps;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;

import br.gov.go.tj.projudi.dt.relatorios.EstatisticaProcessoServentiaDt;
import br.gov.go.tj.utils.DiaHoraEventos;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class EstatisticaProcessoServentiaPs extends Persistencia {
	/**
     * 
     */
    private static final long serialVersionUID = -3990604722572708280L;
    //private static final long UM_DIAS_EM_SEGUNDOS = 60L * 60  * 24;


public EstatisticaProcessoServentiaPs(Connection conexao){
		Conexao = conexao;
	}

	//---------------------------------------------------------
	//CONTA OS PROCESSOS ATIVOS POR SERVENTIA (VARA) E PROCESSO TIPO (TIPO ACAO)
	public int consultaQtdProcessosAtivos(String idServentia, String idProcessoTipo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		Integer qtdProcessosAtivos = 0;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		////System.out.println("..ps-consultaQtdProcessosAtivos()");

		Sql= "SELECT COUNT(*) as QTD_PROCESSOS_ATIVOS  FROM PROJUDI.PROC " +
				"WHERE ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		if (!(idProcessoTipo.length()==0)){ 
			Sql+= " AND ID_PROC_TIPO = ? ";
			ps.adicionarLong(idProcessoTipo);
		}
		Sql +="  AND ID_PROC_STATUS <> ? "; //1 = ARQUIVADO
		ps.adicionarLong(1);
		
		try{
			rs1 = consultar(Sql,ps);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				qtdProcessosAtivos = rs1.getInt("QTD_PROCESSOS_ATIVOS");
			}
			//rs1.close();			
			////System.out.println("..ps-consultaQtdProcessosAtivos Operação realizada com sucesso");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return qtdProcessosAtivos;
	}
	
	//	CONTABILIZA OS PROCESSOS DE 1º GRAU NUMA DETERMINADA SITUAÇÃO
	//	DE ACORDO COM OS PARAMETROS: PRIORIDADE PROCESSO, SEGREDO DE JUSTIÇA, FASE PROCESSO,
	//  PROCESSOS DITRIBUÍDOS HOJE, SEMANA, MÊS, ANO; PROCESSOS ARQUIVADOS HOJE, SEMANA, MÊS, ANO;  
	//	QUE ESTÃO NO OBJETO EstatisticaProcessoServentiaDt.
	public int consultaQtdProcessosEmSituacao(EstatisticaProcessoServentiaDt estatisticaProcessoServentia) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Integer qtdProcessosAtivos = 0;
		DiaHoraEventos dataFimDistribuicao = null;
		DiaHoraEventos dataFimArquivamento = null;
		
		if(estatisticaProcessoServentia.getDataFinalDistribuicao() != null) 
			dataFimDistribuicao = estatisticaProcessoServentia.getDataFinalDistribuicao().getDataZeroHoraPosterior();
		if(estatisticaProcessoServentia.getDataFinalArquivamento() != null) 
			dataFimArquivamento = estatisticaProcessoServentia.getDataFinalArquivamento().getDataZeroHoraPosterior();
		
		
		////System.out.println("..ps-consultaQtdProcessosEmSituacao()");

		Sql= "SELECT COUNT(*) as QTD_PROCESSOS_ATIVOS  FROM PROJUDI.PROC " +
				"WHERE ID_SERV = ? ";
		ps.adicionarLong(estatisticaProcessoServentia.getId_Serventia());
		if (!(estatisticaProcessoServentia.getId_ProcessoTipo().length()==0)){
			Sql += " AND ID_PROC_TIPO = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getId_ProcessoTipo());
		}			
		if (estatisticaProcessoServentia.getDataInicioDistribuicao() != null){
			Sql +=(" AND DATA_RECEBIMENTO >= ? ");
			ps.adicionarDateTime(estatisticaProcessoServentia.getDataInicioDistribuicao().getDataFormatoDiaMesAnoHoraMinuntoSegundo());
		}
		if (dataFimDistribuicao != null){
			Sql +=(" AND DATA_RECEBIMENTO < ? ");
			ps.adicionarDateTime(dataFimDistribuicao.getDataFormatoDiaMesAnoHoraMinuntoSegundo());
		}		
		if (estatisticaProcessoServentia.getDataInicioArquivamento() != null){
			Sql +=(" AND DATA_ARQUIVAMENTO >= ? ");
			ps.adicionarDateTime(estatisticaProcessoServentia.getDataInicioArquivamento().getDataFormatoDiaMesAnoHoraMinuntoSegundo());
		}
		if (dataFimArquivamento != null){
			Sql +=(" AND DATA_ARQUIVAMENTO < ? ");
			ps.adicionarDateTime(dataFimArquivamento.getDataFormatoDiaMesAnoHoraMinuntoSegundo());
		}
		if (!(estatisticaProcessoServentia.getPrioridadeProcesso().length()==0)){ 
			Sql += " AND ID_PROC_PRIOR = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getPrioridadeProcesso());
		}
		if (!(estatisticaProcessoServentia.getSegredoJustica().length()==0)){ 
			Sql += " AND SEGREDO_JUSTICA = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getSegredoJustica());
		}
		if (!(estatisticaProcessoServentia.getFaseProcesso().length()==0)){ 
			Sql += " AND ID_PROC_FASE = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getFaseProcesso());
		}
		if (!(estatisticaProcessoServentia.getProcessoStatus().length() == 0)){
			Sql +="  AND ID_PROC_STATUS = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getProcessoStatus());
		}
		
		try{
			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				qtdProcessosAtivos = rs1.getInt("QTD_PROCESSOS_ATIVOS");
			}
			//rs1.close();			
			////System.out.println("..ps-consultaQtdProcessosEmSituacao Operação realizada com sucesso");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return qtdProcessosAtivos;
	}
	
	//	MÉTODO QUE CALCULA  A MÉDIA DE DIAS EM TRAMITAÇÃO DE PROCESSOS
	//	DE ACORDO COM OS PARAMETROS: PRIORIDADE PROCESSO, SEGREDO DE JUSTIÇA, FASE PROCESSO,
	//  PROCESSOS DITRIBUÍDOS HOJE, SEMANA, MÊS, ANO; PROCESSOS ARQUIVADOS HOJE, SEMANA, MÊS, ANO;  
	//	QUE ESTÃO NO OBJETO EstatisticaProcessoServentiaDt.
	public String mediaDiasEmTramitacao(EstatisticaProcessoServentiaDt estatisticaProcessoServentia) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		double mediaDias = 0.00;
		DiaHoraEventos dataFimDistribuicao = null;
		DiaHoraEventos dataFimArquivamento = null;
		
		if(estatisticaProcessoServentia.getDataFinalDistribuicao() != null) 
			dataFimDistribuicao = estatisticaProcessoServentia.getDataFinalDistribuicao().getDataZeroHoraPosterior();
		if(estatisticaProcessoServentia.getDataFinalArquivamento() != null) 
			dataFimArquivamento = estatisticaProcessoServentia.getDataFinalArquivamento().getDataZeroHoraPosterior();
		
		////System.out.println("..ps-mediaDiasEmTramitacao()");

		Sql= "Select AVG(DATA_ARQUIVAMENTO - DATA_RECEBIMENTO) as MEDIA_DIAS  FROM PROJUDI.PROC " +
				"WHERE ID_SERV = ? ";
		ps.adicionarLong(estatisticaProcessoServentia.getId_Serventia());
		if (!(estatisticaProcessoServentia.getId_ProcessoTipo().length()==0)){ 
			Sql += " AND ID_PROC_TIPO = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getId_ProcessoTipo());
		}
		if (estatisticaProcessoServentia.getDataInicioDistribuicao() != null){
			Sql +=(" AND DATA_RECEBIMENTO >= ? ");
			ps.adicionarDateTime(estatisticaProcessoServentia.getDataInicioDistribuicao().getDataFormatoDiaMesAnoHoraMinuntoSegundo());
		}
		if (dataFimDistribuicao != null){
			Sql +=(" AND DATA_RECEBIMENTO < ? ");
			ps.adicionarDateTime(dataFimDistribuicao.getDataFormatoDiaMesAnoHoraMinuntoSegundo());
		}
		if (estatisticaProcessoServentia.getDataInicioArquivamento() != null){
			Sql +=(" AND DATA_ARQUIVAMENTO >= ? ");
			ps.adicionarDateTime(estatisticaProcessoServentia.getDataInicioArquivamento().getDataFormatoDiaMesAnoHoraMinuntoSegundo());
		}
		if (dataFimArquivamento != null){
			Sql +=(" AND DATA_ARQUIVAMENTO < ? ");
			ps.adicionarDateTime(dataFimArquivamento.getDataFormatoDiaMesAnoHoraMinuntoSegundo());
		}
		if (!(estatisticaProcessoServentia.getPrioridadeProcesso().length()==0)){ 
			Sql += " AND ID_PROC_PRIOR = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getPrioridadeProcesso());
		}
		if (!(estatisticaProcessoServentia.getSegredoJustica().length()==0)) {
			Sql += " AND SEGREDO_JUSTICA = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getSegredoJustica());
		}
		if (!(estatisticaProcessoServentia.getFaseProcesso().length()==0)){ 
			Sql += " AND ID_PROC_FASE = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getFaseProcesso());
		}
		if (!(estatisticaProcessoServentia.getProcessoStatus().length() == 0)){
			Sql +="  AND ID_PROC_STATUS = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getProcessoStatus());
		}
		
		try{
			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				mediaDias = rs1.getDouble("MEDIA_DIAS");
			}
			//rs1.close();			
			////System.out.println("..ps-mediaDiasEmTramitacao Operação realizada com sucesso");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		NumberFormat formatadorDeNumeros = NumberFormat.getInstance();
		formatadorDeNumeros.setMaximumFractionDigits(2);
		return formatadorDeNumeros.format(mediaDias);
	}
	
	//CONTA OS PROCESSOS SUSPENSOS COM PRAZO 
	public int consultaQtdProcessosSuspensos(EstatisticaProcessoServentiaDt estatisticaProcessoServentia) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Integer qtdProcessos = 0;
		////System.out.println("..ps-consultaQtdProcessosSuspensos()");

		Sql= "SELECT COUNT(*) as QTD_PROCESSOS FROM PROC p " +
				"WHERE " +
				" p.ID_PROC" +
				"	IN (SELECT DISTINCT pd.ID_PROC " +
				"		FROM PEND pd " +
				"		WHERE pd.ID_PEND_TIPO = ? ";
		ps.adicionarLong(25);
				if (!(estatisticaProcessoServentia.getSuspensoPrazo().length() == 0))
					if (estatisticaProcessoServentia.getSuspensoPrazo().equalsIgnoreCase("0")) // 0 - SEM PRAZO
						Sql += " AND DATA_FIM IS NULL ";
					else if (estatisticaProcessoServentia.getSuspensoPrazo().equalsIgnoreCase("1")) //1 - COM PRAZO
						Sql += " AND DATA_FIM IS NOT NULL ";				
						
		Sql += ") AND p.ID_SERV = ? ";
		ps.adicionarLong(estatisticaProcessoServentia.getId_Serventia());
		if (!(estatisticaProcessoServentia.getId_ProcessoTipo().length()==0)){
			Sql+= " AND p.ID_PROC_TIPO = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getId_ProcessoTipo());
		}
			
		Sql +="  AND p.ID_PROC_STATUS = ? "; //3 = SUSPENSO
		ps.adicionarLong(3);
		
		try{
			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				qtdProcessos = rs1.getInt("QTD_PROCESSOS");
			}
			//rs1.close();			
			////System.out.println("..ps-consultaQtdProcessosSuspensos Operação realizada com sucesso");
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return qtdProcessos;
	}
	
	//CONTA PROCESSOS PARALISADOS POR MAIS DE 20 DIAS OU MAIS DE 30 DIAS
	public int consultaQtdProcessosProcessosParalisados(EstatisticaProcessoServentiaDt estatisticaProcessoServentia) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Integer qtdProcessosParalisados = 0;
		
		Calendar calCalendario = Calendar.getInstance();
        calCalendario.setTime(new Date());
        calCalendario.add(Calendar.DAY_OF_YEAR, (-1*Funcoes.StringToInt(estatisticaProcessoServentia.getDiasParalisados())));        
        Date tempDate2 = calCalendario.getTime();
		
		////System.out.println("..ps-consultaQtdProcessosProcessosParalisados()");
		Sql=" SELECT COUNT(*)as QTD_PROC_PARALISADOS  FROM PROC " +
			" WHERE" +
			"	ID_PROC " +
			"	IN (SELECT TABELA.ID_PROC " +
			"		FROM " +
			"			(SELECT MAX(DATA_REALIZACAO)" +
			"				AS ULTIMA_DATA,ID_PROC" +
			"		 	FROM MOVI " +
			"		 	GROUP BY ID_PROC) TABELA " +
			"		WHERE TABELA.ULTIMA_DATA > ? " + ")";
		ps.adicionarDate(tempDate2);		
		Sql += " AND ID_SERV = ? ";
		ps.adicionarLong(estatisticaProcessoServentia.getId_Serventia());
		if (!(estatisticaProcessoServentia.getId_ProcessoTipo().length()==0)){ 
			Sql+= " AND ID_PROC_TIPO = ? ";
			ps.adicionarLong(estatisticaProcessoServentia.getId_ProcessoTipo());
		}
		Sql +="  AND ID_PROC_STATUS = ? "; //2 = ATIVO
		ps.adicionarLong(2);
		
		try{
			rs1 = consultar(Sql, ps);
			////System.out.println("....Execução Query OK"  );

			while (rs1.next()) {
				qtdProcessosParalisados = rs1.getInt("QTD_PROC_PARALISADOS");
			}
			//rs1.close();			
			////System.out.println("..ps-consultaQtdProcessosProcessosParalisados Operação realizada com sucesso");
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return qtdProcessosParalisados;
	}
	
	
} 
