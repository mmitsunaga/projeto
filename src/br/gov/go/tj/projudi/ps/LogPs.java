package br.gov.go.tj.projudi.ps;

import java.io.Serializable;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTabelaDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

//---------------------------------------------------------
public class LogPs extends LogPsGen implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1072944789692429511L;

	public LogPs(Connection conexao){
		Conexao = conexao;
	}

	public void inserir(LogDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		dados.setCodigoTemp(String.valueOf(Math.round(Math.random() * 100000)));				
		 
		////System.out.println("....psLoginserir()");
		SqlCampos = "INSERT INTO PROJUDI.LOG (";
		SqlValores = " Values (";
		if (dados.getTabela().length() != 0){
			SqlCampos += "TABELA";
			SqlValores += "?";
			if (dados.getTabela().trim().length() > 60)
				ps.adicionarString(dados.getTabela().trim().substring(0, 59));
			else
				ps.adicionarString(dados.getTabela().trim());
		}
		if (dados.getId_Tabela().length() != 0){
			SqlCampos += ",ID_TABELA";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Tabela());
		}
		if (dados.getId_LogTipo().length() != 0){
			SqlCampos += ",ID_LOG_TIPO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_LogTipo());
		} else if (dados.getLogTipoCodigo() != null && dados.getLogTipoCodigo().length() != 0) {
			SqlCampos += ",ID_LOG_TIPO";
			SqlValores += ", (SELECT MAX(ID_LOG_TIPO) FROM PROJUDI.LOG_TIPO WHERE LOG_TIPO_CODIGO = ?)";
			ps.adicionarLong(dados.getLogTipoCodigo());
		}
		SqlCampos += ",DATA";
		SqlValores += ",?";
		if (dados.getData() != null &&  dados.getData().trim().length() != 0){
			ps.adicionarDate(dados.getData());
		} else {
			ps.adicionarDate(new Date());	
		}		
		SqlCampos += ",HORA";
		SqlValores += ",?";
		ps.adicionarDateTime(new Date());
		//ps.adicionarString("+00 09:03:33.000000");
		//SimpleDateFormat FormatoData = new SimpleDateFormat("0 HH:mm:ss.000000");			
		//ps.adicionarString(FormatoData.format(new Date()));
		if (dados.getId_Usuario().length() != 0){
			SqlCampos += ",ID_USU";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Usuario());
		}
		if (dados.getIpComputador().length() != 0){
			SqlCampos += ",IP_COMPUTADOR";
			SqlValores += ",?";
			ps.adicionarString(dados.getIpComputador());
		}
		if (dados.getValorAtual().length() != 0){
			SqlCampos += ",VALOR_ATUAL";
			SqlValores += ",?";
			ps.adicionarClob(dados.getValorAtual());
		}
		if (dados.getValorNovo().length() != 0){
			SqlCampos += ",VALOR_NOVO";
			SqlValores += ",?";		
			ps.adicionarClob(dados.getValorNovo());
		}
		if (dados.getHash().length() != 0){
			SqlCampos += ",HASH";
			SqlValores += ",?";		ps.adicionarString(dados.getHash());
		}
		if (dados.getQtdErrosDia()!= 0){
			SqlCampos += ",QTD_ERROS_DIA";
			SqlValores += ",?";		ps.adicionarLong(dados.getQtdErrosDia());
		}
		
		SqlCampos += ",CODIGO_TEMP) ";
		SqlValores += ",?)";
		ps.adicionarLong(dados.getCodigoTemp());
		
		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
		

		Sql = Sql.replace("(,", "(");
		
		dados.setId(executarInsert(Sql, "ID_LOG", ps));		

	}	

	/**
	 * Consulta logs de acordo com parâmetros passados
	 * 
	 * @param tabela
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_LogTipo
	 * @param posicao
	 * 
	 * @author jrcorre
	 * @throws Exception 
	 * @since 03/12/2013
	 */
	public String consultarLogJSON(String codigo, String tabela, String dataInicial, String dataFinal, String id_LogTipo, String posicao, String id_Tabela) throws Exception{
		List listaTabelas = null;
		
		//não tem id
		if (codigo==null || codigo.length()==0){
			String stTabela = obtenhaNomesTabelasLogHistoricaConsulta(dataInicial, dataFinal);
			
			return consultarLogTabelaHistoricaJSON( tabela, dataInicial, dataFinal, id_LogTipo, posicao, id_Tabela, stTabela);
		}else{
			listaTabelas = new ArrayList(getMapTabelasLog().values());
			return consultarLogTabelaAtualJSON(codigo,  listaTabelas);
		}							
					
	}
	
	
	/**
	 * 
	 * 
	 * @param codigo
	 * @param tabela
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_LogTipo
	 * @param posicao
	 * @param id_Tabela
	 * @return
	 * @throws Exception
	 */
	private List consultarLogTabelaAtual(String codigo, String tabela, String dataInicial, String dataFinal, String id_LogTipo, String posicao, String id_Tabela, String logTipoCodigo) throws Exception{
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuffer sqlComum = new StringBuffer();
		String sql = "";		
		
		monteSqlComumConsultarLog(codigo, tabela, dataInicial, dataFinal, id_LogTipo, posicao, id_Tabela, "PROJUDI.VIEW_LOG", logTipoCodigo, sqlComum, ps);				
		
		sql = sqlComum.toString() + " ORDER BY DATA, HORA, LOG_TIPO, TABELA ";
		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				LogDt obTemp = new LogDt();
				obTemp.setId(rs1.getString("ID_LOG"));
				obTemp.setTabela(rs1.getString("TABELA"));
				obTemp.setId_Tabela(rs1.getString("ID_TABELA"));
				obTemp.setData(Funcoes.FormatarData(rs1.getDateTime("DATA")));
				obTemp.setHora(rs1.getString("HORA"));
				obTemp.setLogTipo(rs1.getString("LOG_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(1) as QUANTIDADE FROM (" + sqlComum.toString() + " )";
			rs2 = consultar(sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	/**
	 * 
	 * 
	 * @param codigo
	 * @param tabela
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_LogTipo
	 * @param posicao
	 * @param id_Tabela
	 * @return
	 * @throws Exception
	 */
	private String consultarLogTabelaAtualJSON(String codigo, List tabelas) throws Exception{
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		StringBuilder sqlSelect =  new StringBuilder();
		StringBuilder sqlFrom = new StringBuilder();
							
		try{
						
			sqlSelect.append("SELECT l.ID_LOG as Id, LT.LOG_TIPO as Descricao1, l.DATA as Descricao2, l.HORA as Descricao3,  l.TABELA as Descricao4,  l.ID_TABELA as Descricao5 ");
						
			sqlFrom.append( " FROM PROJUDI.LOG L JOIN LOG_TIPO LT ON (L.ID_LOG_TIPO = LT.ID_LOG_TIPO) ");
			sqlFrom.append( " JOIN USU U ON (L.ID_USU = U.ID_USU) ");
										
			sqlFrom.append("  WHERE l.ID_LOG = ? ");	ps.adicionarLong(codigo);
			
			rs1 = consultar(sqlSelect.toString() + sqlFrom  , ps);	
			//vejo se o registro esta na tabela atual
			if (rs1.next()){
				LogDt logdt = new LogDt();
				logdt.setId(rs1.getString("Id"));
				logdt.setLogTipo(rs1.getString("Descricao1"));
				logdt.setData(rs1.getString("Descricao2"));
				logdt.setHora(rs1.getString("Descricao3"));
				logdt.setTabela(rs1.getString("Descricao4"));
				logdt.setId_Tabela(rs1.getString("Descricao5"));
								
				stTemp=logdt.gerarJSON();				

			}else {			
				//se não estiver procuro nos anos anteriores
				for (int i=0; i< tabelas.size(); i++){
					ps.limpar();
					sqlFrom = new StringBuilder();
					
					String stTabela = (String)tabelas.get(i);				
					sqlFrom.append( " FROM PROJUDI." + stTabela + " L JOIN LOG_TIPO LT ON (L.ID_LOG_TIPO = LT.ID_LOG_TIPO) ");
					sqlFrom.append( " JOIN USU U ON (L.ID_USU = U.ID_USU) ");					
										
					sqlFrom.append("  WHERE l.ID_LOG = ? ");	ps.adicionarLong(codigo);
					
					rs1 = consultar(sqlSelect.toString() + sqlFrom  , ps);	
					
					if (rs1.next()){
						
						LogDt logdt = new LogDt();
						logdt.setId(rs1.getString("Id"));
						logdt.setLogTipo(rs1.getString("Descricao1"));
						logdt.setData(rs1.getString("Descricao2"));
						logdt.setHora(rs1.getString("Descricao3"));
						logdt.setTabela(rs1.getString("Descricao4"));
						logdt.setId_Tabela(rs1.getString("Descricao5"));
										
						stTemp=logdt.gerarJSON();
						break;
					}														
				}	
			}
												
							
								
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
        
		return stTemp;
	}
	
	/**
	 * 
	 * 
	 * @param codigo
	 * @param tabela
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_LogTipo
	 * @param posicao
	 * @param id_Tabela
	 * @param listaTabelas
	 * @return
	 * @throws Exception
	 * @author jrcorrea
	 * @since 03/12/2013
	 */
	private String consultarLogTabelaHistoricaJSON( String tabela, String dataInicial, String dataFinal, String id_LogTipo, String posicao, String id_Tabela, String stTabelas) throws Exception{
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		StringBuilder sqlSelect =  new StringBuilder();
		StringBuilder sqlFrom = new StringBuilder();
							
		try{
			int qtdeColunas = 5;
			
			sqlSelect.append("SELECT ID_LOG as Id, LOG_TIPO as Descricao1, DATA as Descricao2, HORA as Descricao3,  TABELA as Descricao4,  ID_TABELA as Descricao5 ");
			
			sqlFrom.append( " FROM PROJUDI." + stTabelas + " L JOIN LOG_TIPO LT ON (L.ID_LOG_TIPO = LT.ID_LOG_TIPO) ");
			sqlFrom.append( " JOIN USU U ON (L.ID_USU = U.ID_USU) ");
			
			
			boolean termoAnd = false;
			
			if ( (tabela != null && tabela.length() > 0) || (dataInicial != null && dataInicial.length() > 0) || (dataFinal != null && dataFinal.length() > 0) || (id_LogTipo != null && id_LogTipo.length() > 0) || (id_Tabela != null && id_Tabela.length() > 0)) {
				sqlFrom.append(" WHERE ");
			
				if (tabela != null && tabela.length() > 0) {
					sqlFrom.append(" TABELA LIKE ? ");
					ps.adicionarString( tabela +"%");
					termoAnd = true;
				}
				if (dataInicial != null && dataInicial.length() > 0) {
					if (termoAnd) sqlFrom.append(" AND ");
					sqlFrom.append(" l.DATA >= ? ");
					ps.adicionarDate(dataInicial);			
					termoAnd = true;
				}
				if (dataFinal != null && dataFinal.length() > 0) {
					if (termoAnd) sqlFrom.append(" AND ");
					sqlFrom.append(" l.DATA <= ? ");
					ps.adicionarDate(dataFinal);
					termoAnd = true;
				}
				if (id_LogTipo != null && id_LogTipo.length() > 0) {
					if (termoAnd) sqlFrom.append(" AND ");
					sqlFrom.append(" l.ID_LOG_TIPO = ? ");
					ps.adicionarLong(id_LogTipo);
					termoAnd = true;
				}
				if (id_Tabela != null && id_Tabela.length() > 0) {
					if (termoAnd) sqlFrom.append(" AND ");
					sqlFrom.append(" l.ID_TABELA = ? ");
					ps.adicionarLong(id_Tabela);
				}
			}
																											
			rs1 = consultarPaginacao(sqlSelect.toString() + sqlFrom +  " ORDER BY DATA, HORA, LOG_TIPO, TABELA ", ps, posicao);
			
			rs2 = consultar("SELECT COUNT(1) as QUANTIDADE  " + sqlFrom.toString()  , ps);
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);				
								
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
        
		return stTemp;
	}
	
	/**
	 * 
	 * 
	 * @param codigo
	 * @param tabela
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_LogTipo
	 * @param posicao
	 * @param id_Tabela
	 * @param listaTabelas
	 * @return
	 * @throws Exception
	 */
	private List consultarLogTabelaHistorica(String codigo, String tabela, String dataInicial, String dataFinal, String id_LogTipo, String posicao, String id_Tabela, String nomeTabela, String logTipoCodigo) throws Exception{
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuffer sqlComum = new StringBuffer();
		String sql = "";
		Long quantidadeRegistros = 0l;
		
		try{
			
			sqlComum.setLength(0);
			ps.limpar();
		
			monteSqlComumConsultarLog(codigo, id_Tabela, dataInicial, dataFinal, id_LogTipo, posicao, id_Tabela, obtenhaConsultaBaseTabelaHistoricaLog(nomeTabela), logTipoCodigo, sqlComum, ps);
			
			sql = sqlComum.toString() + " ORDER BY DATA, HORA, LOG_TIPO, TABELA ";
			rs1 = consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				LogDt obTemp = new LogDt();
				obTemp.setId(rs1.getString("ID_LOG"));
				obTemp.setTabela(rs1.getString("TABELA"));
				obTemp.setId_Tabela(rs1.getString("ID_TABELA"));
				obTemp.setData(Funcoes.FormatarData(rs1.getDateTime("DATA")));
				obTemp.setHora(rs1.getString("HORA"));
				obTemp.setLogTipo(rs1.getString("LOG_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(1) as QUANTIDADE FROM (" + sqlComum.toString() + " )";
			rs2 = consultar(sql, ps);
			if (rs2.next()) quantidadeRegistros += rs2.getLong("QUANTIDADE");			
			//rs1.close();		
			
			liTemp.add(quantidadeRegistros);
		
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
        
		return liTemp;
	}
	
	private String obtenhaNomesTabelasLogHistoricaConsulta(String dataInicial, String dataFinal) throws Exception	{
		if (dataInicial == null || dataFinal == null) return null;
		
		String stTabela = "";
		
		Set<Integer> chaveAno = this.getMapTabelasLog().keySet();
		int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
		
		if (anoAtual==(new TJDataHora(dataInicial.trim()).getAno()) || anoAtual==(new TJDataHora(dataFinal.trim()).getAno()) ){
			stTabela = "LOG";
		}else{		
			for (Integer ano : chaveAno){	
				if (ano > 0) {				
					if (dataInicial != null && dataInicial.trim().length() > 0 && dataFinal != null && dataFinal.trim().length() > 0){
						
						// Informou data inicial e data final...
						if ((new TJDataHora(dataInicial.trim()).getAno() == ano) && (new TJDataHora(dataFinal.trim()).getAno() == ano))
							stTabela = (String) this.getMapTabelasLog().get(ano);				
					} else {
						// Não informou data...
						throw new MensagemException("Erro data inicial ou final não informadas.");
					}
				}			
			}
		}
		return stTabela;
	}
	
	private void monteSqlComumConsultarLog(String codigo, String tabela, String dataInicial, String dataFinal, String id_LogTipo, String posicao, String id_Tabela, String nomeTabela, String logTipoCodigo, StringBuffer sqlComum, PreparedStatementTJGO ps) throws Exception
	{
		sqlComum.append("SELECT ID_LOG, TABELA, DATA, HORA, LOG_TIPO, ID_TABELA ");
		sqlComum.append(" FROM " + nomeTabela + " l ");
		boolean termoAnd = false;
		
		if ((codigo != null && codigo.length() > 0) || (tabela != null && tabela.length() > 0) || (dataInicial != null && dataInicial.length() > 0) || (dataFinal != null && dataFinal.length() > 0) || (id_LogTipo != null && id_LogTipo.length() > 0) || (id_Tabela != null && id_Tabela.length() > 0)) {
			sqlComum.append(" WHERE ");
		}
		if (tabela != null && tabela.length() > 0) {
			sqlComum.append(" TABELA LIKE ? ");
			ps.adicionarString( tabela +"%");
			termoAnd = true;
		}
		if (codigo != null && codigo.length() > 0) {
			if (termoAnd) sqlComum.append(" AND ");			
			sqlComum.append(" l.ID_LOG = ? ");
			ps.adicionarLong(codigo);
			termoAnd = true;						
		}
		if (dataInicial != null && dataInicial.length() > 0) {
			if (termoAnd) sqlComum.append(" AND ");
			sqlComum.append(" l.DATA >= ? ");
			ps.adicionarDate(dataInicial);			
			termoAnd = true;
		}
		if (dataFinal != null && dataFinal.length() > 0) {
			if (termoAnd) sqlComum.append(" AND ");
			sqlComum.append(" l.DATA <= ? ");
			ps.adicionarDate(dataFinal);
			termoAnd = true;
		}
		if (id_LogTipo != null && id_LogTipo.length() > 0) {
			if (termoAnd) sqlComum.append(" AND ");
			sqlComum.append(" l.ID_LOG_TIPO = ? ");
			ps.adicionarLong(id_LogTipo);
			termoAnd = true;
		}
		else if (logTipoCodigo != null && logTipoCodigo.length() > 0)
		{
			if (termoAnd) sqlComum.append(" AND ");
			sqlComum.append(" l.LOG_TIPO_CODIGO = ? ");
			ps.adicionarLong(logTipoCodigo);
			termoAnd = true;
		}
		if (id_Tabela != null && id_Tabela.length() > 0) {
			if (termoAnd) sqlComum.append(" AND ");
			sqlComum.append(" l.ID_TABELA = ? ");
			ps.adicionarLong(id_Tabela);
		}
	}
	
	/**
	 * Consulta logs de algumas tabelas para um determinado processo.
	 * 
	 * @param id_Processo
	 * 
	 * @author mmgomes
	 */
	public List consultarLogProcesso(String id_Processo, String ano) throws Exception {
		
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String consultaIdentificacaoTabelaLogProcesso;
		
		if (id_Processo == null || id_Processo.trim().equalsIgnoreCase("")) return liTemp;
		
		consultaIdentificacaoTabelaLogProcesso = obtenhaConsultaIdentificacaoTabelaLogProcesso(ano);
		
		sql = " SELECT l.ID_LOG, l.TABELA, l.ID_LOG_TIPO, l.LOG_TIPO, l.DATA, l.HORA, l.ID_USU, l.USU, l.IP_COMPUTADOR, l.VALOR_ATUAL, l.VALOR_NOVO, l.CODIGO_TEMP, l.ID_TABELA, l.LOG_TIPO_CODIGO, u.NOME ";
		sql += " FROM " + consultaIdentificacaoTabelaLogProcesso + " l ";
		sql += " INNER JOIN PROJUDI.VIEW_USU u ON u.ID_USU = l.ID_USU ";
		sql += " WHERE l.TABELA = ? "; ps.adicionarString(LogTabelaDt.Processo);
		sql += " AND l.ID_TABELA = ? "; ps.adicionarLong(id_Processo);	
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Erro);		
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Acesso);	
				
		sql += " UNION ALL ";
		
		sql += " SELECT l.ID_LOG, l.TABELA, l.ID_LOG_TIPO, l.LOG_TIPO, l.DATA, l.HORA, l.ID_USU, l.USU, l.IP_COMPUTADOR, l.VALOR_ATUAL, l.VALOR_NOVO, l.CODIGO_TEMP, l.ID_TABELA, l.LOG_TIPO_CODIGO, u.NOME ";
		sql += " FROM " + consultaIdentificacaoTabelaLogProcesso + " l ";
		sql += " INNER JOIN PROJUDI.VIEW_USU u ON u.ID_USU = l.ID_USU ";
		sql += " WHERE l.TABELA = ? "; ps.adicionarString(LogTabelaDt.ProcessoParte);
		sql += " AND EXISTS (SELECT 1 FROM PROC_PARTE WHERE ID_PROC_PARTE = l.ID_TABELA AND ID_PROC = ?) "; ps.adicionarLong(id_Processo);	
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Erro);
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Acesso);
		
		
		sql += " UNION ALL ";
		
		sql += " SELECT l.ID_LOG, l.TABELA, l.ID_LOG_TIPO, l.LOG_TIPO, l.DATA, l.HORA, l.ID_USU, l.USU, l.IP_COMPUTADOR, l.VALOR_ATUAL, l.VALOR_NOVO, l.CODIGO_TEMP, l.ID_TABELA, l.LOG_TIPO_CODIGO, u.NOME ";
		sql += " FROM " + consultaIdentificacaoTabelaLogProcesso + " l ";
		sql += " INNER JOIN PROJUDI.VIEW_USU u ON u.ID_USU = l.ID_USU ";
		sql += " WHERE l.TABELA = ? "; ps.adicionarString(LogTabelaDt.ProcessoParteAdvogado);		
		sql += " AND EXISTS (SELECT 1 FROM PROC_PARTE WHERE ID_PROC_PARTE = l.ID_TABELA AND ID_PROC = ?) "; ps.adicionarLong(id_Processo);	
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Erro);
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Acesso);
		
		sql += " UNION ALL ";
		
		sql += " SELECT l.ID_LOG, l.TABELA, l.ID_LOG_TIPO, l.LOG_TIPO, l.DATA, l.HORA, l.ID_USU, l.USU, l.IP_COMPUTADOR, l.VALOR_ATUAL, l.VALOR_NOVO, l.CODIGO_TEMP, l.ID_TABELA, l.LOG_TIPO_CODIGO, u.NOME ";
		sql += " FROM " + consultaIdentificacaoTabelaLogProcesso + " l ";
		sql += " INNER JOIN PROJUDI.VIEW_USU u ON u.ID_USU = l.ID_USU ";
		sql += " WHERE l.TABELA = ? "; ps.adicionarString(LogTabelaDt.ProcessoResponsavel);
		sql += " AND EXISTS (SELECT 1 FROM PROC_RESP WHERE ID_PROC_RESP = l.ID_TABELA AND ID_PROC = ?) "; ps.adicionarLong(id_Processo);	
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Erro);
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Acesso);	
		
		sql += " UNION ALL ";
		
		sql += " SELECT l.ID_LOG, l.TABELA, l.ID_LOG_TIPO, l.LOG_TIPO, l.DATA, l.HORA, l.ID_USU, l.USU, l.IP_COMPUTADOR, l.VALOR_ATUAL, l.VALOR_NOVO, l.CODIGO_TEMP, l.ID_TABELA, l.LOG_TIPO_CODIGO, u.NOME ";
		sql += " FROM " + consultaIdentificacaoTabelaLogProcesso + " l ";
		sql += " INNER JOIN PROJUDI.VIEW_USU u ON u.ID_USU = l.ID_USU ";
		sql += " WHERE l.TABELA = ? "; ps.adicionarString(LogTabelaDt.PendenciaResponsavel);
		sql += " AND EXISTS (SELECT 1 FROM PEND WHERE ID_PEND = l.ID_TABELA AND ID_PROC = ?) "; ps.adicionarLong(id_Processo);	
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Erro);
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Acesso);	
		
		sql += " UNION ALL ";
		
		sql += " SELECT l.ID_LOG, l.TABELA, l.ID_LOG_TIPO, l.LOG_TIPO, l.DATA, l.HORA, l.ID_USU, l.USU, l.IP_COMPUTADOR, l.VALOR_ATUAL, l.VALOR_NOVO, l.CODIGO_TEMP, l.ID_TABELA, l.LOG_TIPO_CODIGO, u.NOME ";
		sql += " FROM " + consultaIdentificacaoTabelaLogProcesso + " l ";
		sql += " INNER JOIN PROJUDI.VIEW_USU u ON u.ID_USU = l.ID_USU ";
		sql += " WHERE l.TABELA = ? "; ps.adicionarString(LogTabelaDt.Endereco);
		sql += " AND EXISTS (SELECT 1 FROM PROC_PARTE WHERE ID_ENDERECO = l.ID_TABELA AND ID_PROC = ?) "; ps.adicionarLong(id_Processo);	
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Erro);
		sql += " AND l.LOG_TIPO_CODIGO <> ? "; ps.adicionarLong(LogTipoDt.Acesso);		
		
		//sql =  "SELECT * FROM (" + sql + ") ORDER BY ID_TABELA, DATA DESC, HORA DESC"; // 30/11/2018 - A pedido do usuário, foi alterado o log para que, no momento da ordenação, somente a data fosse levada em conta.
		sql =  "SELECT * FROM (" + sql + ") ORDER BY DATA DESC, HORA DESC";
		
		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				LogDt obTemp = new LogDt();
				obTemp.setId(rs1.getString("ID_LOG"));
				obTemp.setTabela(rs1.getString("TABELA"));
				obTemp.setId_LogTipo(rs1.getString("ID_LOG_TIPO"));
				obTemp.setLogTipo(rs1.getString("LOG_TIPO"));
				obTemp.setLogTipoCodigo(rs1.getString("LOG_TIPO_CODIGO"));
				obTemp.setData(Funcoes.FormatarData(rs1.getDateTime("DATA")));
				obTemp.setHora(Funcoes.FormatarHora(rs1.getString("HORA")));
				obTemp.setId_Usuario(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setNomeUsuario(rs1.getString("NOME"));
				obTemp.setIpComputador(rs1.getString("IP_COMPUTADOR"));
				obTemp.setValorAtual(rs1.getString("VALOR_ATUAL"));
				obTemp.setValorNovo(rs1.getString("VALOR_NOVO"));
				obTemp.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				obTemp.setId_Tabela(rs1.getString("ID_TABELA"));				
				
				liTemp.add(obTemp);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return liTemp;
	}
	
	//Metodo utilizado para consultar o log da serventia
	public List consultarLogServentia(String id_Serventia, String ano) throws Exception {
		
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anoAtual = Integer.parseInt(sdf.format(new Date()));
		
		if (id_Serventia == null || id_Serventia.trim().equalsIgnoreCase("")) {
			return liTemp;
		}
			
		sql = " SELECT l.ID_LOG, l.TABELA, l.ID_LOG_TIPO,  l.DATA, l.HORA, l.ID_USU,  u.NOME, l.IP_COMPUTADOR, l.VALOR_ATUAL, l.VALOR_NOVO, l.CODIGO_TEMP, l.ID_TABELA, l.HASH, l.QTD_ERROS_DIA, lt.LOG_TIPO, u.USU, lt.LOG_TIPO_CODIGO ";
		if(Integer.parseInt(ano) == anoAtual){
			sql += " FROM PROJUDI.VIEW_LOG l ";
		} else {
			sql += " FROM PROJUDI.LOG_" + ano + " l ";
		}
		sql += " INNER JOIN VIEW_USU u ON l.ID_USU = u.ID_USU ";
		sql += " INNER JOIN PROJUDI.VIEW_LOG_TIPO lt ON l.ID_LOG_TIPO = lt.ID_LOG_TIPO ";			
		sql += " WHERE l.TABELA LIKE ? ";	ps.adicionarString(LogTabelaDt.Serventia);
		sql += " AND l.ID_TABELA = ? ";	ps.adicionarLong(id_Serventia);	
		sql += " AND lt.LOG_TIPO_CODIGO <> ? ";	ps.adicionarLong(LogTipoDt.Erro);		
		sql += " AND lt.LOG_TIPO_CODIGO <> ? ";	ps.adicionarLong(LogTipoDt.Acesso);		
		sql += " ORDER BY l.DATA DESC, l.HORA DESC";
		
		
		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				
				LogDt obTemp = new LogDt();
				
				obTemp.setId(rs1.getString("ID_LOG"));
				obTemp.setTabela(rs1.getString("TABELA"));
				obTemp.setId_LogTipo(rs1.getString("ID_LOG_TIPO"));
				obTemp.setLogTipo(rs1.getString("LOG_TIPO"));
				obTemp.setData(Funcoes.FormatarData(rs1.getDateTime("DATA")));
				obTemp.setHora(Funcoes.FormatarHora(rs1.getString("HORA")));
				obTemp.setId_Usuario(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setNomeUsuario(rs1.getString("NOME"));
				obTemp.setIpComputador(rs1.getString("IP_COMPUTADOR"));
				obTemp.setValorAtual(rs1.getString("VALOR_ATUAL"));
				obTemp.setValorNovo(rs1.getString("VALOR_NOVO"));
				obTemp.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				obTemp.setId_Tabela(rs1.getString("ID_TABELA"));
				obTemp.setHash(rs1.getString("HASH"));
				obTemp.setQtdErrosDia(rs1.getLong("QTD_ERROS_DIA"));
				obTemp.setLogTipoCodigo(rs1.getString("LOG_TIPO_CODIGO"));
				
				liTemp.add(obTemp);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return liTemp;
	}
	
	private String obtenhaConsultaIdentificacaoTabelaLogProcesso(String ano) throws Exception{
		
		String nomeTabela = (String) getMapTabelasLog().get(Funcoes.StringToInt(ano));
		
		if (nomeTabela != null && nomeTabela.trim().length() > 0) {
			return obtenhaConsultaBaseTabelaHistoricaLog(nomeTabela);
		}
		
		return "PROJUDI.VIEW_LOG";
	}

	private String obtenhaConsultaBaseTabelaHistoricaLog(String nomeTabela)	{
		
		String sql = " ( ";
		sql += " SELECT L.ID_LOG AS ID_LOG, L.TABELA AS TABELA, L.ID_LOG_TIPO AS ID_LOG_TIPO, LT.LOG_TIPO AS LOG_TIPO, L.DATA AS DATA, L.HORA AS HORA, ";
		sql += " L.ID_USU AS ID_USU, U.USU AS USU, L.IP_COMPUTADOR AS IP_COMPUTADOR, L.VALOR_ATUAL AS VALOR_ATUAL, L.VALOR_NOVO AS VALOR_NOVO, ";
		sql += " L.CODIGO_TEMP AS CODIGO_TEMP, L.ID_TABELA AS ID_TABELA, LT.LOG_TIPO_CODIGO AS LOG_TIPO_CODIGO ";
		sql += " FROM PROJUDI." + nomeTabela + " L JOIN LOG_TIPO LT ON (L.ID_LOG_TIPO = LT.ID_LOG_TIPO) ";
		sql += " JOIN USU U ON (L.ID_USU = U.ID_USU) ";
		sql += " ) ";
		return sql;
	}	
	
	public LogDt consultarId(String id_log )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LogDt Dados=null;
		
		stSql+= "SELECT id_log, TABELA, ID_LOG_TIPO, LOG_TIPO, DATA, HORA, ID_USU, USU, IP_COMPUTADOR, VALOR_ATUAL, VALOR_NOVO, CODIGO_TEMP, ID_TABELA, HASH, QTD_ERROS_DIA, LOG_TIPO_CODIGO FROM PROJUDI.VIEW_LOG l WHERE l.ID_LOG = ?";		ps.adicionarLong(id_log);
		try{			
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new LogDt();
				associarDt(Dados, rs1);
			}			
			if (Dados == null)	{
												
				List listaTabelas = new ArrayList(getMapTabelasLog().values());		
				for(int i=0; i<listaTabelas.size(); i++){					
					stSql = "SELECT * FROM " + obtenhaConsultaBaseTabelaHistoricaLog((String) listaTabelas.get(i)) + " l WHERE l.ID_LOG = ?";
					rs1 = consultar(stSql,ps);
					if (rs1.next()) {
						Dados= new LogDt();
						associarDt(Dados, rs1);
						break;
					}
				}
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}
	
	private static Map mapTabelasLog = new HashMap();
	private static Date dataHoraUltimaAtualizacao = null;
	
	private Map getMapTabelasLog() throws Exception
	{		
		if (!ehParaConsultarNomesDeTabelas()) return mapTabelasLog;
				
		mapTabelasLog.clear();
		List liTabelasLog;
		
		liTabelasLog = getTabelasTodasTabelasLog();			
		for (int i = 0; i < liTabelasLog.size(); i++) {
			String nomeTabelaLog = (String) liTabelasLog.get(i);
			String anoTabelaLog = getAnoTabelaLog(nomeTabelaLog);
			if (anoTabelaLog != null) mapTabelasLog.put(Funcoes.StringToInt(anoTabelaLog), nomeTabelaLog);
		}
		
		return mapTabelasLog;
	}
	
	private boolean ehParaConsultarNomesDeTabelas()
	{
		if (dataHoraUltimaAtualizacao == null || Funcoes.diferencaEmHoras(dataHoraUltimaAtualizacao, new Date()) > 1){
			dataHoraUltimaAtualizacao = new Date();
			return true;
		}		
		return false;
	}
	
	private String getAnoTabelaLog(String nomeTabelaLog)
	{
		String ano = null;
		String vetorTemp[] = nomeTabelaLog.split("_");
		
		if (vetorTemp != null && vetorTemp.length == 2) ano = vetorTemp[1];
		
		return ano;
	}
	
	private List getTabelasTodasTabelasLog() throws Exception{
		String Sql;
        List liTemp = new ArrayList();
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        ResultSetTJGO rs1 = null;

        Sql = "SELECT TABLE_NAME ";
        Sql += " FROM ALL_TABLES ";
        Sql += " WHERE TABLESPACE_NAME = ? "; ps.adicionarString("PROJUDI_DATA");
        Sql += " AND TABLE_NAME LIKE ? "; ps.adicionarString("LOG_2%");  
        Sql += " ORDER BY TABLE_NAME desc";
        try{
            rs1 = consultar(Sql, ps);
            while (rs1.next()) {                              
                liTemp.add(rs1.getString("TABLE_NAME"));
            }
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}            
        }
        return liTemp; 
	}

	public LogDt consultarLogHash(String stHash) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LogDt Dados=null;
		
		stSql = "SELECT * FROM PROJUDI.VIEW_LOG_ERRO l WHERE l.hash = ?"; ps.adicionarString(stHash);
		stSql += " and l.data >= ? "; ps.adicionarDate(Funcoes.dateToStringSoData(new Date()));
		try{			
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new LogDt();
				associarDtErro(Dados, rs1);
			}			
								
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}
	/***
	 * 
	 * @param LogDt
	 * @throws Exception
	 */
	public void incluirErroDia(LogDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.LOG_ERRO SET  ";
		stSql+= "QTD_ERROS_DIA = ?";		 	ps.adicionarLong(dados.getQtdErrosDia());  		
		stSql += " WHERE ID_LOG_ERRO  = ? "; 	ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);

	
	} 
	
	/**
	 * Listagem de logs gerados em data específica.
	 * @param data - data do log
	 * @return lista de logs
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List listarLogErros(String data, String posicao) throws Exception {
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql, sqlComum = "";	
		Long quantidadeRegistros = 0l;
		
		sql = "SELECT l.id_log_erro, lt.log_tipo, l.tabela, l.id_tabela, l.data, l.hora, l.id_usu, l.ip_computador, l.valor_novo, l.qtd_erros_dia FROM ";
		
		sqlComum = " projudi.log_erro l " +
				" INNER JOIN projudi.log_tipo lt ON lt.id_log_tipo = l.id_log_tipo " +
				" WHERE l.data = ? "; 													ps.adicionarDate(data);
		sqlComum += " 	AND lt.log_tipo_codigo = ? ";									ps.adicionarLong(LogTipoDt.Erro);
		
		sql += sqlComum + " ORDER BY NVL(l.qtd_erros_dia,0) desc";
		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				LogDt obTemp = new LogDt();
				obTemp.setId(rs1.getString("id_log_erro"));
				obTemp.setLogTipo(rs1.getString("log_tipo"));
				obTemp.setTabela(rs1.getString("tabela"));
				obTemp.setId_Tabela(rs1.getString("id_tabela"));
				obTemp.setData(rs1.getString("data"));
				obTemp.setHora(rs1.getString("hora"));
				obTemp.setId_Usuario(rs1.getString("id_usu"));
				obTemp.setIpComputador(rs1.getString("ip_computador"));
				obTemp.setValorNovo(rs1.getString("valor_novo"));
				obTemp.setQtdErrosDia(rs1.getLong("qtd_erros_dia"));
				liTemp.add(obTemp);
			}
			sql = "SELECT COUNT(1) as QUANTIDADE FROM " + sqlComum.toString();
			rs2 = consultar(sql, ps);
			if (rs2.next()) quantidadeRegistros += rs2.getLong("QUANTIDADE");			
		
		liTemp.add(quantidadeRegistros);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Consulta logs de acordo com parâmetros passados
	 * 
	 * @param tabela
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_LogTipo
	 * @param posicao
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarLog(String codigo, String tabela, String dataInicial, String dataFinal, String id_LogTipo, String logTipoCodigo, String posicao, String id_Tabela) throws Exception{
		String stNomeTabela = obtenhaNomesTabelasLogHistoricaConsulta(dataInicial, dataFinal);
		if (stNomeTabela != null && stNomeTabela.trim().length() > 0) return consultarLogTabelaHistorica(codigo, tabela, dataInicial, dataFinal, id_LogTipo, logTipoCodigo, posicao, id_Tabela, stNomeTabela);
		else return consultarLogTabelaAtual(codigo, tabela, dataInicial, dataFinal, id_LogTipo, posicao, id_Tabela, logTipoCodigo);
	}
	
	public LogDt consultarUltimoLog(String logTipoCodigo)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LogDt Dados=null;
		
		stSql = "SELECT * FROM PROJUDI.VIEW_LOG l WHERE l.ID_LOG = (SELECT MAX(ID_LOG) FROM PROJUDI.VIEW_LOG l2 WHERE l2.LOG_TIPO_CODIGO = ?)";		ps.adicionarLong(logTipoCodigo);
		try{			
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new LogDt();
				associarDt(Dados, rs1);
			}

		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}
	
	public List consultarListaPendencias(String idProcesso, String idServCargo)  throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = new ArrayList();
		
		stSql = " SELECT P.ID_PEND, P.PEND_TIPO ";
		stSql += " FROM PROJUDI.VIEW_PEND_FINAL P ";
		stSql += " INNER JOIN PROJUDI.VIEW_PEND_FINAL_RESP PR ";
		stSql += " ON PR.ID_PEND = P.ID_PEND ";
		stSql += " WHERE P.ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		stSql += " AND P.ID_PEND_TIPO IN (13,14,15,22,26,27,28,29,87,95,96) ";
		stSql += " AND PR.ID_SERV_CARGO = ? ";
		ps.adicionarLong(idServCargo);
		stSql += " ORDER BY P.ID_PEND ";		
		
		try{			
			
			rs1 = consultar(stSql, ps);
			
			while(rs1.next()) {
				
				lista.add(rs1.getString("ID_PEND") + ":" + rs1.getString("PEND_TIPO"));
				
			}
		}finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {}
		}
		return lista; 
	}	
	
	public List consultarListaArquivos(String idPendencia)  throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = new ArrayList();
		
		stSql = "SELECT ID_ARQ ";
		stSql += "FROM PROJUDI.PEND_FINAL_ARQ ";
		stSql += "WHERE ID_PEND = ? ";
		ps.adicionarLong(idPendencia);
		stSql += "AND RESPOSTA = 1 ";
		stSql += "ORDER BY ID_ARQ ";		
		
		try{			
			rs1 = consultar(stSql,ps);
			while(rs1.next()) {
				
				lista.add(rs1.getString("ID_ARQ"));
				
			}
		}finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {}
		}
		return lista; 
	}
	
	public List consultarLogPendencias(String idArquivo, String idPendencia, String pendenciaTipo)  throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = new ArrayList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int ano = Integer.parseInt(sdf.format(new Date()));
		
		for(int i = ano; i >= 2010; i--){
			
			stSql = "SELECT L.ID_TABELA, L.ID_LOG, LT.LOG_TIPO, L.DATA, L.HORA, L.ID_USU, L.IP_COMPUTADOR, U.NOME ";

			if(i == ano){
				stSql += "FROM PROJUDI.VIEW_LOG L ";
			}
			else {
				stSql += "FROM PROJUDI.LOG_" + i + " L ";
			}	

			stSql += "INNER JOIN PROJUDI.USU U ON U.ID_USU = L.ID_USU ";
			stSql += "INNER JOIN PROJUDI.LOG_TIPO LT ON L.ID_LOG_TIPO = LT.ID_LOG_TIPO ";
			stSql += "WHERE L.TABELA ='Arquivo' ";
			stSql += "AND L.ID_LOG_TIPO NOT IN (9) ";
			stSql += "AND L.ID_TABELA = ? ";
			ps.adicionarLong(idArquivo);			
			stSql += "ORDER BY HORA DESC ";

			try{			
				
				rs1 = consultar(stSql,ps);
				
				while(rs1.next()) {
					
					LogDt obTemp = new LogDt();
					
					obTemp.setId_Pendencia(idPendencia);
					obTemp.setPendenciaTipo(pendenciaTipo);
					obTemp.setId_Tabela(rs1.getString("ID_TABELA"));
					obTemp.setId(rs1.getString("ID_LOG"));
					obTemp.setLogTipo(rs1.getString("LOG_TIPO"));
					obTemp.setData(Funcoes.FormatarData(rs1.getString("DATA")));
					obTemp.setHora(Funcoes.FormatarHora(rs1.getString("HORA")));
					obTemp.setIpComputador(rs1.getString("IP_COMPUTADOR"));
					obTemp.setId_Usuario(rs1.getString("ID_USU"));
					obTemp.setNomeUsuario(rs1.getString("NOME"));
					
					lista.add(obTemp);
					
				}
			}finally{
				try{
					if (rs1 != null) rs1.close();
				} catch(Exception e) {}
			}	
		
			//Limpando o result set e o ps para o próximo loop
			rs1 = null;
			ps = new PreparedStatementTJGO();
			
		}//fim do for
		
		return lista; 
	}
	
	public List consultarLogUsuario(String id, String ano, String tabela) throws Exception {
		
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anoAtual = Integer.parseInt(sdf.format(new Date()));
		
		if (id == null || id.trim().equalsIgnoreCase("")) {
			return liTemp;
		}
			
		sql = " SELECT l.ID_LOG, l.TABELA, l.ID_LOG_TIPO, lt.LOG_TIPO, l.DATA, l.HORA, l.ID_USU, u.USU, u.NOME, l.IP_COMPUTADOR, l.VALOR_ATUAL, l.VALOR_NOVO, l.ID_TABELA";		
		if(Integer.parseInt(ano) == anoAtual){
			sql += " FROM PROJUDI.VIEW_LOG l ";
		} else {
			sql += " FROM PROJUDI.LOG_" + ano + " l ";
		}		
		sql += " INNER JOIN PROJUDI.VIEW_USU u ON l.ID_USU = u.ID_USU ";
		sql += " INNER JOIN PROJUDI.VIEW_LOG_TIPO lt ON l.ID_LOG_TIPO = lt.ID_LOG_TIPO ";
		sql += " WHERE lt.LOG_TIPO_CODIGO <> ? ";	ps.adicionarLong(LogTipoDt.Erro);
		sql += " AND lt.LOG_TIPO_CODIGO <> ? ";	ps.adicionarLong(LogTipoDt.Acesso);
		sql += " AND l.TABELA = ? "; ps.adicionarString(tabela);
		sql += " AND l.ID_TABELA IN (" + id + ")";
		sql += " ORDER BY l.TABELA, l.DATA DESC, l.HORA DESC, lt.LOG_TIPO ";
				
		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				
				LogDt obTemp = new LogDt();
				
				obTemp.setId(rs1.getString("ID_LOG"));
				obTemp.setTabela(rs1.getString("TABELA"));
				obTemp.setId_LogTipo(rs1.getString("ID_LOG_TIPO"));
				obTemp.setLogTipo(rs1.getString("LOG_TIPO"));
				obTemp.setData(Funcoes.FormatarData(rs1.getDateTime("DATA")));
				obTemp.setHora(Funcoes.FormatarHora(rs1.getString("HORA")));
				obTemp.setId_Usuario(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setNomeUsuario(rs1.getString("NOME"));
				obTemp.setIpComputador(rs1.getString("IP_COMPUTADOR"));
				obTemp.setValorAtual(rs1.getString("VALOR_ATUAL"));
				obTemp.setValorNovo(rs1.getString("VALOR_NOVO"));
				obTemp.setId_Tabela(rs1.getString("ID_TABELA"));
				
				liTemp.add(obTemp);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return liTemp;
	}
	
	public List consultarLogAdvogado(String id_Advogado, String ano) throws Exception {
		
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		int anoAtual = Integer.parseInt(sdf.format(new Date()));
		
		if (id_Advogado == null || id_Advogado.trim().equalsIgnoreCase("")) {
			return liTemp;
		}
			
		sql = " SELECT l.ID_LOG, l.TABELA, l.ID_LOG_TIPO, lt.LOG_TIPO, l.DATA, l.HORA, l.ID_USU, u.USU, u.NOME, l.IP_COMPUTADOR, l.VALOR_ATUAL, l.VALOR_NOVO, l.ID_TABELA";		
		if(Integer.parseInt(ano) == anoAtual){
			sql += " FROM PROJUDI.VIEW_LOG l ";
		} else {
			sql += " FROM PROJUDI.LOG_" + ano + " l ";
		}		
		sql += " INNER JOIN VIEW_USU u ON l.ID_USU = u.ID_USU ";
		sql += " INNER JOIN PROJUDI.VIEW_LOG_TIPO lt ON l.ID_LOG_TIPO = lt.ID_LOG_TIPO ";
		sql += " WHERE l.ID_TABELA = ? "; ps.adicionarLong(id_Advogado);				
		sql += " AND lt.LOG_TIPO_CODIGO <> ? ";	ps.adicionarLong(LogTipoDt.Erro);
		sql += " AND lt.LOG_TIPO_CODIGO <> ? ";	ps.adicionarLong(LogTipoDt.Acesso);		
		sql += " AND (l.TABELA LIKE 'Usuario' ";
		sql += " OR l.TABELA LIKE 'UsuarioServentia' ";
		sql += " OR l.TABELA LIKE 'UsuarioServentiaOab' ";
		sql += " OR l.TABELA LIKE 'UsuarioServentiaGrupo') ";
		sql += " ORDER BY l.TABELA, l.DATA DESC, l.HORA DESC, lt.LOG_TIPO ";
				
		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				
				LogDt obTemp = new LogDt();
				
				obTemp.setId(rs1.getString("ID_LOG"));
				obTemp.setTabela(rs1.getString("TABELA"));
				obTemp.setId_LogTipo(rs1.getString("ID_LOG_TIPO"));
				obTemp.setLogTipo(rs1.getString("LOG_TIPO"));
				obTemp.setData(Funcoes.FormatarData(rs1.getDateTime("DATA")));
				obTemp.setHora(Funcoes.FormatarHora(rs1.getString("HORA")));
				obTemp.setId_Usuario(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setNomeUsuario(rs1.getString("NOME"));
				obTemp.setIpComputador(rs1.getString("IP_COMPUTADOR"));
				obTemp.setValorAtual(rs1.getString("VALOR_ATUAL"));
				obTemp.setValorNovo(rs1.getString("VALOR_NOVO"));
				obTemp.setId_Tabela(rs1.getString("ID_TABELA"));
				
				liTemp.add(obTemp);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return liTemp;
	}

	public void inserirErro(LogDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		dados.setCodigoTemp(String.valueOf(Math.round(Math.random() * 100000)));				
		 
		////System.out.println("....psLoginserir()");
		SqlCampos = "INSERT INTO PROJUDI.LOG_ERRO (";
		SqlValores = " Values (";
		if (dados.getTabela().length() != 0){
			SqlCampos += "TABELA";
			SqlValores += "?";
			if (dados.getTabela().trim().length() > 60)
				ps.adicionarString(dados.getTabela().trim().substring(0, 59));
			else
				ps.adicionarString(dados.getTabela().trim());
		}
		if (dados.getId_Tabela().length() != 0){
			SqlCampos += ",ID_TABELA";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Tabela());
		}
		if (dados.getId_LogTipo().length() != 0){
			SqlCampos += ",ID_LOG_TIPO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_LogTipo());
		} else if (dados.getLogTipoCodigo() != null && dados.getLogTipoCodigo().length() != 0) {
			SqlCampos += ",ID_LOG_TIPO";
			SqlValores += ", (SELECT MAX(ID_LOG_TIPO) FROM PROJUDI.LOG_TIPO WHERE LOG_TIPO_CODIGO = ?)";
			ps.adicionarLong(dados.getLogTipoCodigo());
		}
		SqlCampos += ",DATA";
		SqlValores += ",?";
		if (dados.getData() != null &&  dados.getData().trim().length() != 0){
			ps.adicionarDate(dados.getData());
		} else {
			ps.adicionarDate(new Date());	
		}		
		SqlCampos += ",HORA";
		SqlValores += ",?";
		ps.adicionarDateTime(new Date());
		//ps.adicionarString("+00 09:03:33.000000");
		//SimpleDateFormat FormatoData = new SimpleDateFormat("0 HH:mm:ss.000000");			
		//ps.adicionarString(FormatoData.format(new Date()));
		if (dados.getId_Usuario().length() != 0){
			SqlCampos += ",ID_USU";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Usuario());
		}
		if (dados.getIpComputador().length() != 0){
			SqlCampos += ",IP_COMPUTADOR";
			SqlValores += ",?";
			ps.adicionarString(dados.getIpComputador());
		}
		if (dados.getValorAtual().length() != 0){
			SqlCampos += ",VALOR_ATUAL";
			SqlValores += ",?";
			ps.adicionarClob(dados.getValorAtual());
		}
		if (dados.getValorNovo().length() != 0){
			SqlCampos += ",VALOR_NOVO";
			SqlValores += ",?";		
			ps.adicionarClob(dados.getValorNovo());
		}
		if (dados.getHash().length() != 0){
			SqlCampos += ",HASH";
			SqlValores += ",?";		ps.adicionarString(dados.getHash());
		}
		if (dados.getQtdErrosDia()!= 0){
			SqlCampos += ",QTD_ERROS_DIA";
			SqlValores += ",?";		ps.adicionarLong(dados.getQtdErrosDia());
		}
		
		SqlCampos += ",CODIGO_TEMP) ";
		SqlValores += ",?)";
		ps.adicionarLong(dados.getCodigoTemp());
		
		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
		

		Sql = Sql.replace("(,", "(");
		
		dados.setId(executarInsert(Sql, "ID_LOG_ERRO", ps));		

	}	
	
	protected void associarDtErro( LogDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_LOG_ERRO"));
		Dados.setTabela(rs.getString("TABELA"));
		Dados.setId_LogTipo( rs.getString("ID_LOG_TIPO"));
		Dados.setLogTipo( rs.getString("LOG_TIPO"));
		Dados.setData( Funcoes.FormatarData(rs.getDateTime("DATA")));
		Dados.setHora( rs.getString("HORA"));
		Dados.setId_Usuario( rs.getString("ID_USU"));
		Dados.setUsuario( rs.getString("USU"));
		Dados.setIpComputador( rs.getString("IP_COMPUTADOR"));
		Dados.setValorAtual( rs.getString("VALOR_ATUAL"));
		Dados.setValorNovo( rs.getString("VALOR_NOVO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		Dados.setId_Tabela( rs.getString("ID_TABELA"));
		Dados.setLogTipoCodigo( rs.getString("LOG_TIPO_CODIGO"));
		Dados.setHash(rs.getString("HASH"));
		Dados.setQtdErrosDia(rs.getLong("QTD_ERROS_DIA"));		
}

	public String consultarLogErroJSON(String codigo) throws Exception {
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		StringBuilder sqlSelect =  new StringBuilder();
		StringBuilder sqlFrom = new StringBuilder();
							
		try{
						
			sqlSelect.append("SELECT l.ID_LOG_ERRO as Id, LT.LOG_TIPO as Descricao1, l.DATA as Descricao2, l.HORA as Descricao3,  l.TABELA as Descricao4,  l.ID_TABELA as Descricao5 ");
						
			sqlFrom.append( " FROM PROJUDI.LOG_ERRO L JOIN LOG_TIPO LT ON (L.ID_LOG_TIPO = LT.ID_LOG_TIPO) ");
			sqlFrom.append( " JOIN USU U ON (L.ID_USU = U.ID_USU) ");
										
			sqlFrom.append("  WHERE l.ID_LOG_ERRO = ? ");	ps.adicionarLong(codigo);
			
			rs1 = consultar(sqlSelect.toString() + sqlFrom  , ps);	
			//vejo se o registro esta na tabela atual
			if (rs1.next()){
				LogDt logdt = new LogDt();
				logdt.setId(rs1.getString("Id"));
				logdt.setLogTipo(rs1.getString("Descricao1"));
				logdt.setData(rs1.getString("Descricao2"));
				logdt.setHora(rs1.getString("Descricao3"));
				logdt.setTabela(rs1.getString("Descricao4"));
				logdt.setId_Tabela(rs1.getString("Descricao5"));
								
				stTemp=logdt.gerarJSON();				

			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
        
		return stTemp;
	}

	public LogDt consultarIdLogErro(String stId) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LogDt Dados=null;
		
		stSql = "SELECT ID_LOG_ERRO as id_log, TABELA, ID_LOG_TIPO, LOG_TIPO, DATA, HORA, ID_USU, USU, IP_COMPUTADOR, VALOR_ATUAL, VALOR_NOVO, CODIGO_TEMP, ID_TABELA, HASH, QTD_ERROS_DIA, LOG_TIPO_CODIGO FROM PROJUDI.VIEW_LOG_ERRO l WHERE l.ID_LOG_ERRO = ?";		ps.adicionarLong(stId);
		
		try{			
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new LogDt();
				associarDt(Dados, rs1);
			}			

		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}
	
	/**
	 * Método para consultar o histórico nos logs da guia.
	 * @param String idGuiaProjudi
	 * @param String idISNSPG
	 * @param String numeroGuiaCompleto
	 * 
	 * @return List<LogDt>
	 * 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public List<LogDt> consultarLogsGuia(String idGuiaProjudi, String idISNSPG, String numeroGuiaCompleto) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<LogDt> listaLogDt = null;
		
		String campos = "ID_LOG, TABELA, ID_LOG_TIPO, LOG_TIPO, DATA, HORA, ID_USU, USU, IP_COMPUTADOR, VALOR_ATUAL, VALOR_NOVO, CODIGO_TEMP, ID_TABELA, HASH, QTD_ERROS_DIA, LOG_TIPO_CODIGO";
		
//		String sql2012 = " select " + campos + " from projudi.view_log_2012 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2013 = " select " + campos + " from projudi.view_log_2013 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2014 = " select " + campos + " from projudi.view_log_2014 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2015 = " select " + campos + " from projudi.view_log_2015 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2016 = " select " + campos + " from projudi.view_log_2016 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2017 = " select " + campos + " from projudi.view_log_2017 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2018 = " select " + campos + " from projudi.view_log_2018 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2019 = " select " + campos + " from projudi.view_log_2019 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
		
		String sql2020 = " select " + campos + " from projudi.view_log where id_tabela in (?, ?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
		ps.adicionarLong(idGuiaProjudi);
		ps.adicionarLong(idISNSPG);
		ps.adicionarLong(numeroGuiaCompleto);
		
		try {
//			String sql = sql2012 + " union all " + sql2013 + " union all " + sql2014 + " union all " + sql2015 + " union all " + sql2016 + " union all " + sql2017 + " union all " + sql2018 + " union all " + sql2019 + " union all " + sql2020;
			String sql = sql2020;
			rs1 = consultar(sql + " order by id_log", ps);
			
			while(rs1.next()) {
				if( listaLogDt == null ) {
					listaLogDt = new ArrayList<LogDt>();
				}
				
				LogDt dados= new LogDt();
				associarDt(dados, rs1);
				
				listaLogDt.add(dados);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return listaLogDt; 
	}
	
//	/**
//	 * Método para consultar o histórico nos logs da guia.
//	 * @param String idGuiaProjudi
//	 * @param String idISNSPG
//	 * @return List<LogDt>
//	 * @throws Exception
//	 */
//	public List<LogDt> consultarLogsGuia(String idGuiaProjudi, String idISNSPG) throws Exception {
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		List<LogDt> listaLogDt = null;
//		
//		String campos = "ID_LOG TABELA, ID_LOG_TIPO, LOG_TIPO, DATA, HORA, ID_USU, USU, IP_COMPUTADOR, VALOR_ATUAL, VALOR_NOVO, CODIGO_TEMP, ID_TABELA, HASH, QTD_ERROS_DIA, LOG_TIPO_CODIGO";
//		
//		String sql2012 = " select " + campos + " from projudi.view_log_2012 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2013 = " select " + campos + " from projudi.view_log_2013 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2014 = " select " + campos + " from projudi.view_log_2014 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2015 = " select " + campos + " from projudi.view_log_2015 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2016 = " select " + campos + " from projudi.view_log_2016 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2017 = " select " + campos + " from projudi.view_log_2017 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2018 = " select " + campos + " from projudi.view_log_2018 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2019 = " select " + campos + " from projudi.view_log_2019 where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		String sql2020 = " select " + campos + " from projudi.view_log where id_tabela in (?, ?) and upper(tabela) like '%GUIAEMISSAO%' ";
//		ps.adicionarLong(idGuiaProjudi);
//		ps.adicionarLong(idISNSPG);
//		
//		try {
//			String sql = sql2012 + " union all " + sql2013 + " union all " + sql2014 + " union all " + sql2015 + " union all " + sql2016 + " union all " + sql2017 + " union all " + sql2018 + " union all " + sql2019 + " union all " + sql2020;
//			rs1 = consultar(sql + " order by id_log", ps);
//			
//			while(rs1.next()) {
//				if( listaLogDt == null ) {
//					listaLogDt = new ArrayList<LogDt>();
//				}
//				
//				LogDt dados= new LogDt();
//				associarDt(dados, rs1);
//	
//				listaLogDt.add(dados);
//			}
//
//		} finally {
//			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
//		}
//		return listaLogDt; 
//	}
	
	/**
	 * Método que consulta o último log de boleto emitido hoje.
	 * 
	 * @return LogDt
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public LogDt consultaLogUltimoBoletoEmitidoHoje() throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		LogDt logDt = null;
		
		String sql = "SELECT * FROM PROJUDI.VIEW_LOG WHERE ID_LOG IN ( SELECT MAX(ID_LOG) FROM PROJUDI.LOG WHERE upper(TABELA) = 'GUIAEMISSAO' AND ID_LOG_TIPO IN (?,?) AND DATA >= ? AND ID_TABELA IN ( SELECT ID_GUIA_EMIS FROM PROJUDI.GUIA_EMIS WHERE DATA_EMIS >= ? AND DATA_EMISSAO_BOLETO >= ? AND CODIGO_BARRAS_BOLETO IS NOT NULL ))";
		
		ps.adicionarLong(LogTipoDt.BoletoEmitido);
		ps.adicionarLong(LogTipoDt.BoletoReemitido);
		Date hoje = new Date();
		ps.adicionarDate(hoje);
		ps.adicionarDate(hoje);
		ps.adicionarDate(hoje);
		
		try {
			rs1 = consultar(sql, ps);
			
			while(rs1.next()) {
				logDt = new LogDt();
				
				associarDt(logDt, rs1);
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		return logDt;
	}

	public Date getDataHoraBD() throws Exception {
		ResultSetTJGO rs1 = null;
		Date tempDate = null;
		
		String sql = "select sysdate as data from dual";		
				
		try {
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
			rs1 = consultar(sql, ps);
			
			if(rs1.next()) {
				tempDate = rs1.getDateTime("data");								
			}

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		return tempDate;
	}
	
	//Metodo utilizado para consultar o log de pedido de liberação de acesso do processo
	public List<String> consultarLogLiberacaoAcesso(String id_Processo,Date data_Inicial,Date data_Final) throws Exception {
		
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List<String> listaLiberacaoAcesso = new ArrayList<String>();
			
		sql  = " SELECT DATA_INICIO, DATA_FIM, NOME, CPF, OAB_NUMERO ";
		sql += " FROM VIEW_LIBERACAO_ACESSO_PROCESSO ";
		sql += " WHERE ID_PROC = ? " ; ps.adicionarLong(id_Processo);
		
		sql += " AND DATA_INICIO BETWEEN ? "; ps.adicionarDateTime(data_Inicial);
		sql += " AND ? "; ps.adicionarDateTime(data_Final);
		
		sql += " ORDER BY DATA_INICIO DESC ";
		
		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				
				listaLiberacaoAcesso.add(
				rs1.getString("DATA_INICIO") + ";" +
				rs1.getString("DATA_FIM") + ";" +
				rs1.getString("NOME") + ";" +
				rs1.getString("CPF") + ";" +
				rs1.getString("OAB_NUMERO")
				);
				
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return listaLiberacaoAcesso;
	}
	
	//Metodo utilizado para consultar o log de pedido de liberação de acesso do processo
	public List<String> consultarLogAcessoArquivo(String id_Processo, Date data_Inicial, Date data_Final, int ano) throws Exception {
		
		String sql;
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List<String> listaAcessoArquivo = new ArrayList<String>();
								
		int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
		
		sql = " select distinct u.nome Usuario, u.cpf, a.nome_arq Arquivo, mt.movi_tipo Movimentacao, m.DATA_REALIZACAO Data_movimentacao, l.data Data_acesso,  l.IP_COMPUTADOR IP_ACESSO ";
		sql += " from  movi m "; 
		sql += " inner join movi_arq ma on m.id_movi = ma.id_movi ";
		sql += " inner join arq a on a.id_arq = ma.id_arq ";		
		sql += " inner join movi_tipo mt on mt.id_movi_tipo = m.id_movi_tipo ";
		
		if(ano == anoAtual){
			sql += " inner join log l on ma.id_arq = l.id_tabela ";
		} else {
			sql += " inner join log_" + ano + "  l on ma.id_arq = l.id_tabela ";
		}
		
		sql += " inner join usu u on u.id_usu = l.id_usu ";	
		
		sql += " WHERE m.ID_PROC = ? and l.id_log_tipo = 9 " ; ps.adicionarLong(id_Processo);			
		sql += " AND l.DATA BETWEEN ? "; ps.adicionarDateTime(data_Inicial);
		sql += " AND ? "; ps.adicionarDateTime(data_Final);
		
		sql += " ORDER BY Data_acesso DESC ";
		
		try{
			rs1 = consultar(sql, ps);

			while (rs1.next()) {
				
				listaAcessoArquivo.add(
				rs1.getString("Usuario") + ";" +
				rs1.getString("cpf") + ";" +
				rs1.getString("Arquivo") + ";" +
				rs1.getString("Movimentacao") + ";" +
				rs1.getString("Data_movimentacao") + ";" +
				rs1.getString("Data_acesso") + ";" +					
				rs1.getString("IP_ACESSO")
				);
				
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        }
		return listaAcessoArquivo;
	}
}