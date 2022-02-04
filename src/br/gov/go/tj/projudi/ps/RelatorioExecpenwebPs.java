package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioExecpenwebDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class RelatorioExecpenwebPs extends Persistencia{
	
		
	/**
     * 
     */
    private static final long serialVersionUID = 2353619284798743358L;

    public RelatorioExecpenwebPs(Connection conexao){
    	Conexao = conexao;
	}
	
	/**
	 * Total de processos sem cálculo (nunca foi feito cálculo de liquidação de pena), agrupado e ordenado por serventia.
	 * return lista com os sentenciados
	 * throws Exception
	 */
	public List consultarTotalProcessoCalculoAtrasoSemCalculoServentia(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select count (distinct p.id_proc) as quantidade, s.serv");
		sql.append(" from projudi.proc_exe pe ");
		sql.append(" inner join projudi.proc p on pe.id_proc_exe_penal = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on ps.id_proc_status = p.id_proc_status");
		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
		sql.append(" left join projudi.calculo_liquidacao c on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.view_situacao_atual_exe vur on vur.id_proc = p.id_proc");
		sql.append(" where ps.proc_status_codigo in (?,?)");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		sql.append(" and (c.data_calculo is null or data_calculo < sysdate-365)");//cálculo atraso e sem cálculo
		sql.append(" and (vur.id_regime_exe is null or vur.id_regime_exe <> ?)"); //quando o sentenciado esta em LC, não tem mais cálculo.
		ps.adicionarLong(RegimeExecucaoDt.LIVRAMENTO_CONDICIONAL);
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		} 
		sql.append(" group by s.serv");
		sql.append(" order by quantidade desc");
		
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setQuantidade(rs.getInt("QUANTIDADE"));
				rel.setServentia(rs.getString("SERV"));
				lista.add(rel);
			}
			
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
//	Lista de processos com cálculo em atraso e sem cálculo, agrupados por serventia e ordenado por serventia e data do cálculo, respectivamente.
//    ob.: Considera-se cálculo em atraso, os processos, onde o sentenciado não está em Livramento condicional, com período de cálculo maior que 1 ano, à partir da data do relatório.
//    Dados do relatório: Número do processo, dados do último cálculo (data do cálculo, data da provável progressão, data do provável livramento, data de homologação do cálculo, data de validade do mandado de prisão, data do término da pena) e último regime
	public List consultarProcessoCalculoAtrasoSemCalculo(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select distinct (p.id_proc), p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo, p.id_serv, s.serv, vur.regime_exe,");
		sql.append(" c.ID_CALCULO_LIQUIDACAO, c.data_calculo, c.data_req_temporal_livramento, c.data_req_temporal_progressao, c.data_termino_pena, c.data_validade_mand_prisao, c.DATA_HOMOLOGACAO");
		sql.append(" from projudi.proc_exe pe ");
		sql.append(" inner join projudi.proc p on pe.id_proc_exe_penal = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on ps.id_proc_status = p.id_proc_status");
		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
		sql.append(" left join projudi.calculo_liquidacao c on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.view_situacao_atual_exe vur on vur.id_proc = p.id_proc");
		sql.append(" where ps.proc_status_codigo in (?,?) ");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		sql.append(" and (c.data_calculo is null or c.data_calculo < sysdate-365)");//cálculo atraso e sem cálculo
		sql.append(" and (vur.id_regime_exe is null or vur.id_regime_exe <> ?)"); //quando o sentenciado esta em LC, não tem mais cálculo.
		ps.adicionarLong(RegimeExecucaoDt.LIVRAMENTO_CONDICIONAL);
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		} 
		sql.append(" order by s.serv, c.data_calculo");
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setServentia(rs.getString("serv"));
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				if (rs.getString("regime_exe") != null) rel.setRegime(rs.getString("regime_exe"));
				else rel.setRegime("--");
				if (rs.getDateTime("data_calculo") != null) rel.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("data_calculo")));
				else rel.setDataCalculo("--");
				if (rs.getDateTime("data_req_temporal_livramento") != null) rel.setDataLivramento(Funcoes.FormatarData(rs.getDateTime("data_req_temporal_livramento")));
				else rel.setDataLivramento("--");
				if (rs.getDateTime("data_req_temporal_progressao") != null)rel.setDataProgressao(Funcoes.FormatarData(rs.getDateTime("data_req_temporal_progressao")));
				else rel.setDataProgressao("--");
				if (rs.getDateTime("data_termino_pena") != null) rel.setDataTerminoPena(Funcoes.FormatarData(rs.getDateTime("data_termino_pena")));
				else rel.setDataTerminoPena("--");
				if (rs.getDateTime("data_validade_mand_prisao") != null) rel.setDataValidadeMandadoPrisao(Funcoes.FormatarData(rs.getDateTime("data_validade_mand_prisao")));
				else rel.setDataValidadeMandadoPrisao("--");
				if (rs.getDateTime("DATA_HOMOLOGACAO") != null) rel.setDataHomologacaoCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_HOMOLOGACAO")));
				else rel.setDataHomologacaoCalculo("--");
				lista.add(rel);
			}
			
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	//Lista de processos com data de término da pena anterior à data atual, agrupados por serventia
	public List consultarProcessoTerminoPenaAtraso(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select distinct p.id_proc, p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo, p.id_serv, s.serv, c.data_termino_pena, sit.regime_exe, c.data_calculo, pp.nome");
		sql.append(" from projudi.calculo_liquidacao c ");
		sql.append(" inner join projudi.view_situacao_atual_exe sit on sit.id_proc = c.id_proc");
		sql.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on ps.id_proc_status = p.id_proc_status");
		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
		sql.append(" inner join projudi.proc_parte pp on pp.id_proc = p.id_proc");
		sql.append(" where ps.proc_status_codigo in (?,?)");
		sql.append(" and sit.id_evento_exe_status = ?"); //cumprindo pena
		sql.append(" and c.data_termino_pena < sysdate"); //calculo atualizado
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoStatusDt.CUMPRINDO_PENA);
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		}
		sql.append(" order by s.serv, c.data_termino_pena");
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setServentia(rs.getString("serv"));
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				rel.setRegime(rs.getString("regime_exe"));
				rel.setSentenciado(rs.getString("nome").toUpperCase());
				if (rs.getDateTime("data_calculo") != null) rel.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("data_calculo")));
				if (rs.getDateTime("data_termino_pena") != null) rel.setDataTerminoPena(Funcoes.FormatarData(rs.getDateTime("data_termino_pena")));
				lista.add(rel);
			}
			
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	//Total de processos de execução penal por serventia.
	public List consultarTotalProcessoServentia(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select count(distinct p.ID_PROC) AS QUANTIDADE, s.serv");
		sql.append(" FROM PROJUDI.PROC_EXE pe");
		sql.append(" INNER JOIN PROJUDI.PROC p on pe.ID_PROC_EXE_PENAL = p.ID_PROC");
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
		sql.append(" where ps.proc_status_codigo in (?,?)");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		} 
		sql.append(" group by s.serv");
		sql.append(" order by s.serv");	
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setQuantidade(rs.getInt("QUANTIDADE"));
				rel.setServentia(rs.getString("SERV"));
				lista.add(rel);
			}
			
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	//Total de processos com cálculo, no período informado, por serventia
	public List consultarTotalProcessoComCalculoPeriodo(String dataInicio, String dataFim, String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE, s.serv");
		sql.append(" FROM PROJUDI.CALCULO_LIQUIDACAO cl");
		sql.append(" INNER JOIN PROJUDI.PROC p ON p.ID_PROC = cl.ID_PROC");
		sql.append(" INNER JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS");
		sql.append(" INNER JOIN PROJUDI.SERV s ON p.ID_SERV = s.ID_SERV");
		sql.append(" WHERE ps.PROC_STATUS_CODIGO IN (?,?)");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		sql.append(" AND cl.data_calculo > to_date('" + dataInicio + "', 'dd/mm/yy')");
		if (dataFim.length() > 0) {
			sql.append("and cl.data_calculo < to_date(' "+ dataFim + "', 'dd/mm/yy')");
		}
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		} 
		sql.append(" GROUP BY s.serv");
		sql.append(" ORDER BY s.serv");
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setQuantidade(rs.getInt("QUANTIDADE"));
				rel.setServentia(rs.getString("SERV"));
				lista.add(rel);
			}
			
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	//Lista dos processos que não receberam a Progressão de Regime, agrupados por serventia.
	//Ob.: o sentenciado tem data provável de Progressão de Regime anterior à data atual, o cálculo está atualizado, mas não recebeu a Progressão.
	public List consultarProcessoProgressaoAtraso(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select c.data_calculo, c.data_req_temporal_progressao, c.data_termino_pena, p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo, s.serv, ur.regime_exe");
		sql.append(" from projudi.calculo_liquidacao c");
		sql.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
		sql.append(" inner join projudi.view_situacao_atual_exe ur on ur.id_proc = c.id_proc");
		sql.append(" where ps.proc_status_codigo in (?,?)");
		sql.append(" and c.data_req_temporal_progressao is not null"); //tem data provável da PR
		sql.append(" and (c.data_homologacao is null or c.data_homologacao < data_calculo)"); //cálculo não foi homologado
		sql.append(" and c.data_req_temporal_progressao < sysdate"); //data provável menor que a data atual
		sql.append(" and (");
		sql.append("   p.id_proc not in ("); //não tem evento de PR com data início >= à data do provável benefício
		sql.append("                       select id_proc_exe_penal"); 
		sql.append("                       from projudi.proc_evento_exe"); 
		sql.append("                       where id_evento_exe = ? ");
		sql.append("                       and c.data_req_temporal_progressao < data_inicio");
		sql.append("                     ) ");
		sql.append(" )");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoDt.PROGRESSAO_REGIME);
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		} 
		sql.append(" order by s.serv, c.data_req_temporal_progressao");
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setServentia(rs.getString("serv"));
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				rel.setRegime(rs.getString("regime_exe"));
				if (rs.getDateTime("data_calculo") != null) rel.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("data_calculo")));
				if (rs.getDateTime("data_req_temporal_progressao") != null) rel.setDataProgressao(Funcoes.FormatarData(rs.getDateTime("data_req_temporal_progressao")));
				if (rs.getDateTime("data_termino_pena") != null) rel.setDataTerminoPena(Funcoes.FormatarData(rs.getDateTime("data_termino_pena")));
				lista.add(rel);
			}
			
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	//Lista dos processos que não receberam o benefício do Livramento Condicional, agrupados por serventia.
	//Ob.: o sentenciado têm data provável de Livramento Condicional anterior à data atual, o cálculo está atualizado, mas não recebeu o benefício.
	public List consultarProcessoLivramentoAtraso(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select c.data_calculo, c.data_req_temporal_livramento, c.data_termino_pena, p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo, s.serv, ur.regime_exe");
		sql.append(" from projudi.calculo_liquidacao c");
		sql.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
		sql.append(" inner join projudi.view_situacao_atual_exe ur on ur.id_proc = c.id_proc");
		sql.append(" where ps.proc_status_codigo in (?,?)");
		sql.append(" and c.data_req_temporal_livramento is not null"); //tem data provável da LC
		sql.append(" and (c.data_homologacao is null or c.data_homologacao < data_calculo)"); //cálculo não foi homologado
		sql.append(" and c.data_req_temporal_livramento < sysdate"); //data provável menor que a data atual
		sql.append(" and (");
		sql.append("   p.id_proc not in ("); //não tem evento de concessão do LC com data início >= à data do provável benefício
		sql.append("                       select id_proc_exe_penal"); 
		sql.append("                       from projudi.proc_evento_exe"); 
		sql.append("                       where id_evento_exe = ?");
		sql.append("                       and c.data_req_temporal_livramento < data_inicio");
		sql.append("                     ) ");
		sql.append(" )");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL);		
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		} 
		sql.append(" order by s.serv, c.data_req_temporal_livramento");
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setServentia(rs.getString("serv"));
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				rel.setRegime(rs.getString("regime_exe"));
				if (rs.getDateTime("data_calculo") != null) rel.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("data_calculo")));
				if (rs.getDateTime("data_req_temporal_livramento") != null) rel.setDataProgressao(Funcoes.FormatarData(rs.getDateTime("data_req_temporal_livramento"))); //seta a dataProgressao para utilizar no relatório
				if (rs.getDateTime("data_termino_pena") != null) rel.setDataTerminoPena(Funcoes.FormatarData(rs.getDateTime("data_termino_pena")));
				lista.add(rel);
			}
			
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	//Lista dos processos com data de validade do mandado de prisão vencido e status foragido.
	//Ob.: o sentenciado têm data de validade do mandado de prisão anterior à data atual, o cálculo está atualizado, mas o sentenciado está foragido.
	public List consultarProcessoMandadoAtraso(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select c.data_calculo, c.data_validade_mand_prisao, c.data_termino_pena, p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo, s.serv, regime_exe");
		sql.append(" from projudi.calculo_liquidacao c");
		sql.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
		sql.append(" inner join projudi.view_situacao_atual_exe vus ON vus.id_proc = p.id_proc");
		sql.append(" where ps.proc_status_codigo in (?,?)"); //processo ativo
		sql.append(" and c.data_validade_mand_prisao is not null"); //tem data de validade do mandado de prisão
		sql.append(" and c.data_validade_mand_prisao < sysdate"); //data de validade do mandado de prisão anterior à data atual
		sql.append(" and vus.id_evento_exe_status = ?"); //status foragido
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoStatusDt.FORAGIDO);
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		} 
		sql.append(" order by s.serv, c.data_validade_mand_prisao");
		
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setServentia(rs.getString("serv"));
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				rel.setRegime(rs.getString("REGIME_EXE"));
				if (rs.getDateTime("data_calculo") != null) rel.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("data_calculo")));
				if (rs.getDateTime("data_validade_mand_prisao") != null) rel.setDataProgressao(Funcoes.FormatarData(rs.getDateTime("data_validade_mand_prisao"))); //seta a dataProgressao para utilizar no relatório
				if (rs.getDateTime("data_termino_pena") != null) rel.setDataTerminoPena(Funcoes.FormatarData(rs.getDateTime("data_termino_pena")));
				lista.add(rel);
			}
			
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	
	//Total de processos agrupados por regime, status e serventia, para as pena Privativas de Liberdade.
//	public List consultarProcessoRegime_old(String idServentia){
//		StringBuffer sql = new StringBuffer();
//		List listaRetorno = new ArrayList();
//		ResultSetTJGO rs = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		
//		sql.append(" select s.serv, s.id_serv, p.id_proc, pee.data_inicio, pee.id_evento_exe, ee.evento_exe, re.id_regime_exe, re.regime_exe, ");
//		sql.append(" pet.id_pena_exe_tipo, pet.pena_exe_tipo, vs.id_evento_exe_status, vs.evento_exe_status");
//		sql.append(" from proc_evento_exe pee");
//		sql.append(" inner join evento_exe ee on pee.id_evento_exe = ee.id_evento_exe");
//		sql.append(" inner join proc p on p.id_proc = pee.id_proc_exe_penal");
//		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
//		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
//		sql.append(" left join evento_regime er on pee.id_proc_evento_exe = er.id_proc_evento_exe");
//		sql.append(" left join regime_exe re on er.id_regime_exe = re.id_regime_exe");
//		sql.append(" left join pena_exe_tipo pet on pet.id_pena_exe_tipo = re.id_pena_exe_tipo");
//		sql.append(" left join projudi.view_situacao_atual_exe vs on vs.id_proc = p.id_proc");
//		sql.append(" where ps.proc_status_codigo in (?,?)"); //processo ativo
//		ps.adicionarLong(ProcessoStatusDt.ATIVO);
//		ps.adicionarLong(ProcessoStatusDt.CALCULO);
//		if (idServentia.length() > 0){
//			sql.append(" AND s.id_serv = ?");
//			ps.adicionarLong(idServentia);
//		} 
//		sql.append(" order by s.serv, pee.id_proc_exe_penal, pee.data_inicio desc");
//		
//			rs = consultar(sql.toString(), ps);
//			
//			List listaRel = new ArrayList();
//				while (rs.next()){
//					RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
//					rel.setServentia(rs.getString("serv"));
//					rel.setIdServentia(rs.getString("id_serv"));
//					rel.setIdProcesso(rs.getString("id_proc"));
//					rel.setDataInicio(rs.getString("data_inicio"));
//					rel.setIdEvento(rs.getString("id_evento_exe"));
//					rel.setEvento(rs.getString("evento_exe"));
//					if (rs.getString("id_regime_exe") == null){
//						rel.setIdRegime("");
//						rel.setRegime("");
//					} else {
//						rel.setIdRegime(rs.getString("id_regime_exe"));
//						rel.setRegime(rs.getString("regime_exe"));	
//					}
//					if (rs.getString("id_pena_exe_tipo") == null){
//						rel.setIdTipoPena("");
//						rel.setTipoPena("");
//					} else {
//						rel.setIdTipoPena(rs.getString("id_pena_exe_tipo"));
//						rel.setTipoPena(rs.getString("pena_exe_tipo"));	
//					}
//					if (rs.getString("id_evento_exe_status") == null){
//						rel.setIdStatus(String.valueOf(EventoExecucaoStatusDt.NAO_APLICA));
//						rel.setStatus("Não aplica");
//					} else {
//						rel.setIdStatus(rs.getString("id_evento_exe_status"));
//						rel.setStatus(rs.getString("evento_exe_status"));	
//					}
//					listaRel.add(rel);
//				}
//
//			
//			idServentia = "";
//			rs.close();
//			
//			String serventia = "";
//			String idProcesso = "";
//			int posicaoProcesso = 0;
//			//pena privativa de liberdade
//		    int fechadoForagido = 0;
//		    int fechadoCumprindo = 0;
//		    int semiAbertoForagido = 0;
//		    int semiAbertoCumprindo = 0;
//		    int abertoForagido = 0;
//		    int abertoCumprindo = 0;
//		    int abertoDomiciliarForagido = 0;
//		    int abertoDomiciliarCumprindo = 0;
//		    int lcForagido = 0;
//		    int lcCumprindo = 0;
//		    int abertoPscForagido = 0;
//		    int abertoPscCumprindo = 0;
//		    int prisaoDomiciliarForagido = 0;
//		    int prisaoDomiciliarCumprindo = 0;
//		    
//		    //pena restritiva de direito
//		    int prestPecunCumprindo = 0;
//		    int prestPecunForagido = 0;
//			int perdaBensValoresCumprindo = 0;
//			int perdaBensValoresForagido = 0;
//			int pscCumprindo = 0;
//			int pscForagido = 0;
//			int intTempDireitoCumprindo = 0;
//			int intTempDireitoForagido = 0;
//			int lfsCumprindo = 0;
//			int lfsForagido = 0;
//		    
//		    //medida de segurança
//			int tratAmbulatorialCumprindo = 0;
//			int tratAmbulatorialForagido = 0;
//			int internacaoCumprindo = 0;
//			int internacaoForagido = 0;
//			int tratPsiquiatricoCumprindo = 0;
//			int tratPsiquiatricoForagido = 0;
//		    
//			for (int i = 0; i < listaRel.size(); i++) {
//				RelatorioExecpenwebDt relI = (RelatorioExecpenwebDt)listaRel.get(i);
//				RelatorioExecpenwebDt rel = null;
//				
//				if (!idServentia.equals(relI.getIdServentia()) || i == listaRel.size()-1){//mudou a serventia
//					if (i != 0){
//						if (abertoCumprindo > 0){
//							rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Aberto");
//							rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(abertoCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//						}
//					    if (abertoForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Aberto");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(abertoForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (abertoPscCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Aberto com PSC");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(abertoPscCumprindo);
//							listaRetorno.add(rel);	
//							rel = null;
//					    }
//					    if (abertoPscForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Aberto com PSC");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(abertoPscForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (abertoDomiciliarCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Aberto Domiciliar");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(abertoDomiciliarCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (abertoDomiciliarForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Aberto Domiciliar");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(abertoDomiciliarForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (fechadoCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Fechado");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(fechadoCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (fechadoForagido > 0){
//							rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Fechado");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(fechadoForagido);
//							listaRetorno.add(rel);
//							rel = null;
//						}
//					    if (lcCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Livramento Condicional");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(lcCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (lcForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Livramento Condicional");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(lcForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (prisaoDomiciliarCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Prisão Domiciliar");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(prisaoDomiciliarCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }	
//					    if (prisaoDomiciliarForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Prisão Domiciliar");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(prisaoDomiciliarForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (semiAbertoCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Semiaberto");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(semiAbertoCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (semiAbertoForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Semiaberto");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(semiAbertoForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (internacaoCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Internação");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(internacaoCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (internacaoForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Internação");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(internacaoForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (tratAmbulatorialCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Tratamento Ambulatorial");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(tratAmbulatorialCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (tratAmbulatorialForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Tratamento Ambulatorial");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(tratAmbulatorialForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (tratPsiquiatricoCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Tratamento Psiquiátrico");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(tratPsiquiatricoCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (tratPsiquiatricoForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Tratamento Psiquiátrico");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(tratPsiquiatricoForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (prestPecunCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Prestacao Pecuniária");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(prestPecunCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (prestPecunForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Prestacao Pecuniária");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(prestPecunForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (perdaBensValoresCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Perda de Bens e Valores");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(perdaBensValoresCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (perdaBensValoresForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Perda de Bens e Valores");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(perdaBensValoresForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (pscCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Prestação de Serviço à Comunidade");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(pscCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (pscForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Prestação de Serviço à Comunidade");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(pscForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (intTempDireitoCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Interdição Temporária de Direitos");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(intTempDireitoCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (intTempDireitoForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Interdição Temporária de Direitos");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(intTempDireitoForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (lfsCumprindo > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Limitação de Fim de Semana");
//						    rel.setStatus("Cumprindo Pena");
//							rel.setQuantidade(lfsCumprindo);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					    if (lfsForagido > 0){
//					    	rel = new RelatorioExecpenwebDt();
//							rel.setServentia(serventia);	
//							rel.setRegime("Limitação de Fim de Semana");
//						    rel.setStatus("Foragido");
//							rel.setQuantidade(lfsForagido);
//							listaRetorno.add(rel);
//							rel = null;
//					    }
//					}
//					
//					serventia = relI.getServentia();
//					idServentia = relI.getIdServentia();
//					//pena privativa de liberdade
//				    fechadoForagido = 0;
//				    fechadoCumprindo = 0;
//				    semiAbertoForagido = 0;
//				    semiAbertoCumprindo = 0;
//				    abertoForagido = 0;
//				    abertoCumprindo = 0;
//				    abertoDomiciliarForagido = 0;
//				    abertoDomiciliarCumprindo = 0;
//				    lcForagido = 0;
//				    lcCumprindo = 0;
//				    abertoPscForagido = 0;
//				    abertoPscCumprindo = 0;
//				    prisaoDomiciliarForagido = 0;
//				    prisaoDomiciliarCumprindo = 0;
//				    //pena restritiva de direito
//				    prestPecunCumprindo = 0;
//				    prestPecunForagido = 0;
//					perdaBensValoresCumprindo = 0;
//					perdaBensValoresForagido = 0;
//					pscCumprindo = 0;
//					pscForagido = 0;
//					intTempDireitoCumprindo = 0;
//					intTempDireitoForagido = 0;
//					lfsCumprindo = 0;
//					lfsForagido = 0;
//				    //medida de segurança
//					tratAmbulatorialCumprindo = 0;
//					tratAmbulatorialForagido = 0;
//					internacaoCumprindo = 0;
//					internacaoForagido = 0;
//					tratPsiquiatricoCumprindo = 0;
//					tratPsiquiatricoForagido = 0;
//			} //fim if mudou serventia
//				
//			idProcesso = relI.getIdProcesso(); //primeiro processo da serventia
//			posicaoProcesso = i; 
//			String tipoPena = "";
//			
//			//percorre a lista até o fim do mesmo processo
//			for (int w=posicaoProcesso; w<listaRel.size(); w++){
//				RelatorioExecpenwebDt relW = (RelatorioExecpenwebDt)listaRel.get(w);
//				
//				if (idProcesso.equals(relW.getIdProcesso())){
//					
//					//verifica se é pena privativa de liberdade
//					if (tipoPena.length() == 0){
//						if (relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_MEDIDA_SEGURANCA)) 
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PENA_RESTRITIVA_DIREITO))
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO))
//								){
//							tipoPena = "prd"; //pena restritiva de direito
//						} else if (relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PRIVATIVA_LIBERDADE))
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PRIVATIVA_LIBERDADE_CAUTELAR))
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.PRIMEIRO_REGIME))
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.PROGRESSAO_REGIME))
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.REGRESSAO_REGIME))
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL))
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))
//								){
//							tipoPena = "ppl"; //pena privativa de liberdade
//						} else if (relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.TRANSITO_JULGADO)) 
//									&& relW.getIdTipoPena() != null
//									&& relW.getIdTipoPena().equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA))){
//							tipoPena = "ms"; //medida de segurança
//						} else if (relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.EXTINCAO_CUMPRIMENTO_PENA))
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.EXTINCAO_OBITO))
//								|| relW.getIdEvento().equals(String.valueOf(EventoExecucaoDt.EXTINCAO_PRESCRICAO))){
//							tipoPena = "extinto";
//						}
//					}
//				} else {
//					i = w-1; //posição do próximo processo
//					break;
//				}
//				idProcesso = relW.getIdProcesso();
//			} //fim processo
//				
//			//se for pena restritiva de direito....
//			if (tipoPena.equals("ppl") || tipoPena.length() == 0){
//				for (int p=posicaoProcesso; p<listaRel.size(); p++){
//					RelatorioExecpenwebDt relP = (RelatorioExecpenwebDt)listaRel.get(p);
//					if (relP.getIdRegime() != null && relP.getIdRegime().length() > 0){
//						String idStatus = "";
//						if (relP.getIdStatus() != null && relP.getIdStatus().length() > 0) idStatus = relP.getIdStatus();
//						switch(Funcoes.StringToInt(relP.getIdRegime())){
//							case RegimeExecucaoDt.ABERTO:
//							case RegimeExecucaoDt.ABERTO_SUBSTITUICAO_PENA:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) abertoCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) abertoForagido++;
//								break;
//							case RegimeExecucaoDt.ABERTO_COM_PSC:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) abertoPscCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) abertoPscForagido++;
//								break;
//							case RegimeExecucaoDt.ABERTO_DOMICILIAR:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) abertoDomiciliarCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) abertoDomiciliarForagido++;
//								break;
//							case RegimeExecucaoDt.FECHADO:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) fechadoCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) fechadoForagido++;
//								break;
//							case RegimeExecucaoDt.LIVRAMENTO_CONDICIONAL:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) lcCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) lcForagido++;
//								break;
//							case RegimeExecucaoDt.PRISAO_DOMICILIAR:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) prisaoDomiciliarCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) prisaoDomiciliarForagido++;
//								break;
//							case RegimeExecucaoDt.SEMI_ABERTO:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) semiAbertoCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) semiAbertoForagido++;
//								break;
//						}
//						break;
//					}
//				}
//			} //fim ppl
//			//se for pena restritiva de direito.....
//			else if (tipoPena.equals("prd")){
//				for (int p=posicaoProcesso; p<listaRel.size(); p++){
//					RelatorioExecpenwebDt relP = (RelatorioExecpenwebDt)listaRel.get(p);
//					if (!idProcesso.equals(relP.getIdProcesso()) 
//							&& (relP.getIdEvento().equals(String.valueOf(EventoExecucaoDt.INICIO_PENA_RESTRITIVA_DIREITO))
//									|| relP.getIdEvento().equals(String.valueOf(EventoExecucaoDt.CONVERSAO_PENA_RESTRITIVA_DIREITO)))	
//						){
//						break;
//					}
//					if (relP.getIdRegime() != null && relP.getIdRegime().length() > 0){
//						String idStatus = "";
//						if (relP.getIdStatus() != null && relP.getIdStatus().length() > 0) idStatus = relP.getIdStatus();
//						switch(Funcoes.StringToInt(relP.getIdRegime())){
//							//Caso esse switch seja descomentado, é necesário incluir o regime de Cestas básicas que foi criado posteriormente.
//							case RegimeExecucaoDt.PRESTACAO_PECUNIARIA:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) prestPecunCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) prestPecunForagido++;
//								break;
//							case RegimeExecucaoDt.PERDA_BENS_VALORES:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) perdaBensValoresCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) perdaBensValoresForagido++;
//								break;
//							case RegimeExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) pscCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) pscForagido++;
//								break;
//							case RegimeExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) intTempDireitoCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) intTempDireitoForagido++;
//								break;
//							case RegimeExecucaoDt.LIMITACAO_FIM_SEMANA:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) lfsCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) lfsForagido++;
//								break;
//						}
//					}
//				}
//			} //fim prd
//			//se for medida de segurança....
//			else if (tipoPena.equals("ms")){
//				for (int p=posicaoProcesso; p<listaRel.size(); p++){
//					RelatorioExecpenwebDt relP = (RelatorioExecpenwebDt)listaRel.get(p);
//					if (relP.getIdRegime() != null && relP.getIdRegime().length() > 0){
//						String idStatus = "";
//						if (relP.getIdStatus() != null && relP.getIdStatus().length() > 0) idStatus = relP.getIdStatus();
//						switch(Funcoes.StringToInt(relP.getIdRegime())){
//							case RegimeExecucaoDt.TRATAMENTO_PSIQUIATRICO:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) tratPsiquiatricoCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) tratPsiquiatricoForagido++;
//								break;
//							case RegimeExecucaoDt.INTERNACAO:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) internacaoCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) internacaoForagido++;
//								break;
//							case RegimeExecucaoDt.TRATAMENTO_AMBULATORIAL:
//								if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.CUMPRINDO_PENA))) tratAmbulatorialCumprindo++;
//								else if (idStatus.equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))) tratAmbulatorialForagido++;
//								break;
//						}
//						break;
//					}
//				}
//			} //fim ms
//		}
//		listaRel = null;
//			
//		return listaRetorno;
//	}
//	
	//lista os processos de execução penal ativos da serventia.
	public List consultarProcessoRegime(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select count(*) as qtde, s.id_serv, s.serv, vs.id_regime_exe, vs.regime_exe, vs.id_evento_exe_status, vs.evento_exe_status, vs.id_modalidade, vs.modalidade ");
		sql.append(" from view_proc_exe pe ");
		sql.append(" inner join projudi.serv s on pe.id_serv = s.id_serv "); 
		sql.append(" inner join projudi.proc_status ps on pe.id_proc_status = ps.id_proc_status "); 
		sql.append(" left join projudi.view_situacao_atual_exe vs on vs.id_proc = pe.id_proc_exe_penal ");
		sql.append(" where ps.proc_status_codigo in (?,?)  ");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		}
		sql.append(" group by s.id_serv, s.serv, vs.id_regime_exe, vs.regime_exe, vs.id_evento_exe_status, vs.evento_exe_status, vs.id_modalidade, vs.modalidade ");
		sql.append(" order by s.serv, vs.regime_exe, vs.evento_exe_status, vs.modalidade ");
		
		try{
			rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setServentia(rs.getString("serv"));
				rel.setRegime(rs.getString("regime_exe"));
				rel.setStatus(rs.getString("EVENTO_EXE_STATUS"));
				rel.setQuantidade(rs.getInt("qtde"));
				rel.setModalidade(rs.getString("modalidade"));
				lista.add(rel);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	public String consultarDescricaoServentiaJSON(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_SERV AS ID, SERV AS DESCRICAO1, ESTADO_REPRESENTACAO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_SERV ";
		SqlFrom += "WHERE SERV LIKE ? ";
		ps.adicionarString("%"+ descricao +"%");
//		Sql += " AND CODIGO_TEMP = ?";
//		ps.adicionarLong(ServentiaDt.ATIVO);
		SqlFrom += " AND SERV_SUBTIPO_CODIGO in (?,";			ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL);
		SqlFrom += "?,";										ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL_ALTERNATIVA);
		SqlFrom += "?)";										ps.adicionarLong(ServentiaSubtipoDt.EXECPENWEB);
		
		
		SqlOrder = " ORDER BY SERV";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	//lista os processos de execução penal ativos da serventia.
	public List listarProcessoAtivoServentia(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select distinct p.id_proc, p.PROC_NUMERO, p.ANO, p.DIGITO_VERIFICADOR, p.forum_codigo, c.comarca, s.serv, pp.nome,");
		sql.append(" sit.id_regime_exe, sit.regime_exe, sit.id_evento_exe_status, sit.EVENTO_EXE_STATUS, cl.data_calculo, cl.data_termino_pena,");
		sql.append(" cl.DATA_REQ_TEMPORAL_LIVRAMENTO, cl.DATA_REQ_TEMPORAL_PROGRESSAO, cl.DATA_VALIDADE_MAND_PRISAO, cl.DATA_HOMOLOGACAO ");
		sql.append(" FROM PROJUDI.PROC_EXE pe  ");
		sql.append(" INNER JOIN PROJUDI.PROC p on pe.ID_PROC_EXE_PENAL = p.ID_PROC "); 
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status "); 
		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv ");
		sql.append(" inner join projudi.comarca c on s.id_comarca = c.id_comarca ");
		sql.append(" INNER JOIN PROJUDI.PROC_PARTE pp on p.ID_PROC = pp.ID_PROC  ");
		sql.append(" LEFT JOIN PROJUDI.calculo_liquidacao cl on pe.id_proc_exe_penal = cl.id_proc ");
		sql.append(" left join projudi.view_situacao_atual_exe sit on sit.id_proc = p.id_proc ");
		sql.append(" WHERE ps.PROC_STATUS_CODIGO IN (?,?) ");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		}
		sql.append(" ORDER BY c.comarca, s.serv ");
		
		try{
			rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				rel.setServentia(rs.getString("serv"));
				rel.setComarca(rs.getString("comarca"));
				rel.setSentenciado(rs.getString("nome"));
				rel.setRegime(rs.getString("regime_exe"));
				rel.setStatus(rs.getString("EVENTO_EXE_STATUS"));
				if (rs.getDateTime("data_calculo") != null) rel.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("data_calculo")));
				if (rs.getDateTime("data_termino_pena") != null) rel.setDataTerminoPena(Funcoes.FormatarData(rs.getDateTime("data_termino_pena")));
				if (rs.getDateTime("DATA_REQ_TEMPORAL_LIVRAMENTO") != null) rel.setDataLivramento(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_LIVRAMENTO")));
				if (rs.getDateTime("DATA_REQ_TEMPORAL_PROGRESSAO") != null) rel.setDataProgressao(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_PROGRESSAO")));
				if (rs.getDateTime("DATA_VALIDADE_MAND_PRISAO") != null) rel.setDataValidadeMandadoPrisao(Funcoes.FormatarData(rs.getDateTime("DATA_VALIDADE_MAND_PRISAO")));
				if (rs.getDateTime("DATA_HOMOLOGACAO") != null) rel.setDataHomologacaoCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_HOMOLOGACAO")));
				lista.add(rel);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	
	//lista os processos de execução penal ativos da serventia.
	public List listarProcessoEletronicoAtivoServentia(String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		
		sql.append(" SELECT ");
		sql.append(" c.comarca AS COMARCA, ");
		sql.append(" s.serv AS SERVENTIA, ");
		sql.append(" sc.serv_cargo, ");
		sql.append(" pr.PROC_NUMERO, pr.ANO, pr.DIGITO_VERIFICADOR, pr.forum_codigo, ");
		sql.append(" pr.data_recebimento AS DATA_PROTOCOLO, ");
		sql.append(" mov.data_realizacao AS DATA_FASE, ");
		sql.append(" mt.movi_tipo AS FASE, ");
		sql.append(" pt.proc_tipo AS NATUREZA ");
		sql.append(" FROM ( ");
		sql.append("   SELECT s.id_comarca, p.id_serv, ");
		sql.append("       (SELECT max(m.id_movi) FROM projudi.movi m ");
		sql.append("           WHERE m.id_proc = p.id_proc) AS ID_ULTIMA_MOVIMENTACAO, ");
		sql.append("       p.id_proc, p.id_proc_tipo ");
		sql.append("       FROM projudi.proc p ");
		sql.append("       INNER JOIN projudi.serv s ON p.id_serv = s.id_serv ");
		sql.append("   WHERE p.data_arquivamento is null ");
		sql.append("       AND p.id_proc_tipo = 305  "); //-- processos do execpen
		if (idServentia.length() > 0){
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		}		
		sql.append(" )  tab ");
		sql.append(" INNER JOIN projudi.comarca c ON tab.id_comarca = c.id_comarca ");
		sql.append(" INNER JOIN projudi.serv s ON tab.id_serv = s.id_serv ");
		sql.append(" INNER JOIN projudi.movi mov ON tab.id_ultima_movimentacao = mov.id_movi ");
		sql.append(" INNER JOIN projudi.movi_tipo mt ON mov.id_movi_tipo = mt.id_movi_tipo ");
		sql.append(" INNER JOIN projudi.proc pr ON tab.id_proc = pr.id_proc ");
		sql.append(" INNER JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = pr.id_proc_tipo ");
		sql.append(" INNER JOIN projudi.proc_resp pre ON pre.id_proc = pr.id_proc and pre.id_cargo_tipo = 5  "); //-- 5 = juiz
		sql.append(" INNER JOIN projudi.serv_cargo sc ON sc.id_serv_cargo = pre.id_serv_cargo ");
		sql.append(" ORDER BY COMARCA, SERVENTIA, PROC_NUMERO, DATA_PROTOCOLO ");
 
		try{
			rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				rel.setServentia(rs.getString("serventia"));
				rel.setComarca(rs.getString("comarca"));
				if (rs.getString("DATA_PROTOCOLO") != null) rel.setDataProtocolo(Funcoes.DataHora(rs.getDateTime("DATA_PROTOCOLO")));
				if (rs.getString("DATA_FASE") != null) rel.setDataProtocolo(Funcoes.DataHora(rs.getDateTime("DATA_FASE")));
				rel.setFase(rs.getString("FASE"));
				rel.setNatureza(rs.getString("NATUREZA"));
				rel.setServentiaCargo(rs.getString("serv_cargo"));
				lista.add(rel);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	public List listarEventos(String id_processo) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		
		sql.append("SELECT pev.*, e.*");
		sql.append("FROM PROJUDI.VIEW_PROC_EVENTO_EXE pev ");
		sql.append("INNER JOIN PROJUDI.EVENTO_EXE e ON pev.ID_EVENTO_EXE = e.ID_EVENTO_EXE ");
		sql.append("INNER JOIN PROJUDI.PROC p on p.ID_PROC = pev.id_proc ");
		sql.append("LEFT JOIN PROJUDI.PROC_EXE pe ON pev.ID_PROC_EXE = pe.ID_PROC_EXE ");
		sql.append("WHERE (pe.CODIGO_TEMP = ? OR pe.CODIGO_TEMP is NULL) ");
		ps.adicionarLong(1);
		//consulta o processo pai e os processos filho (para atender os casos de processo apenso)
		if (id_processo.length()>0){
			sql.append(" AND (p.ID_PROC = ? OR p.ID_PROC_DEPENDENTE = ?) ");
			ps.adicionarLong(id_processo);
			ps.adicionarLong(id_processo);
		}
		sql.append(" ORDER BY DATA_INICIO, pev.ID_MOVI, pev.ID_PROC_EVENTO_EXE");
		
		try{
			rs1 = consultar(sql.toString(), ps);

			while (rs1.next()){
				ProcessoEventoExecucaoDt dados = new ProcessoEventoExecucaoDt();
				dados.setId_EventoExecucao( rs1.getString("ID_EVENTO_EXE"));
				lista.add(dados);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return lista;
	}
}