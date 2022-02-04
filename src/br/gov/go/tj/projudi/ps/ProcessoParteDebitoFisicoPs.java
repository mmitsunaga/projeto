package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt;
import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoParteDebitoFisicoPs extends ProcessoParteDebitoFisicoPsGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6286502646025228976L;

	public ProcessoParteDebitoFisicoPs(Connection conexao){
    	Conexao = conexao;
	}

	public List consultarDebitosProcessoParte(String nomeParte, String cpfCnpjParte, String processoNumero, String posicao) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT * ";
		sqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_DEBITO_FISICO b ";
		sqlFrom += " WHERE b.NOME LIKE ? ";
		ps.adicionarString( nomeParte +"%");
		
		if (cpfCnpjParte != null && cpfCnpjParte.length() > 0) {
			sqlFrom += " AND b.CPF_CNPJ_PARTE = ?";
			ps.adicionarLong(cpfCnpjParte);
		}
		if(processoNumero != null && processoNumero.length() > 0){
			sqlFrom += " AND b.PROC_NUMERO LIKE ? ";
			ps.adicionarString(processoNumero);
		}
		
		sqlOrder = " ORDER BY PROC_NUMERO ";
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			while (rs1.next()) {
				ProcessoParteDebitoFisicoDt obTemp = new ProcessoParteDebitoFisicoDt();
				this.associarDt(obTemp, rs1);
				liTemp.add(obTemp);
			}
			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			if (rs2.next())	liTemp.add(rs2.getLong("QUANTIDADE"));
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	public String consultarDebitosProcessoParteJSON(String nomeParte, String cpfCnpjParte, String processoNumero, String serventia, String numeroGuia, String posicao) throws Exception {

		String sql = ""; 
		String sqlFrom = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 5;

		sql = "SELECT b.ID_PROC_PARTE_DEBITO_FISICO AS ID, b.PROC_NUMERO AS DESCRICAO1, b.PROC_DEBITO AS DESCRICAO2, b.NOME_PARTE AS DESCRICAO3, b.DESC_ESCRIVANIA AS DESCRICAO4, b.NUMERO_GUIA_COMPLETO AS DESCRICAO5 ";
		sqlFrom = " FROM PROJUDI.VIEW_PROC_PARTE_DEBITO_FISICO b ";
		sqlFrom += " WHERE"; 
		
		if(nomeParte != null && nomeParte.trim().length() > 0) {
			String nomeSimplificadoParte = Funcoes.converteNomeSimplificado(nomeParte);
			sqlFrom += " AND (b.NOME_SIMPLIFICADO = ? OR b.NOME_PARTE LIKE ?)";
			ps.adicionarString(nomeSimplificadoParte);
			ps.adicionarString("%" + nomeParte + "%");
		}
		if (cpfCnpjParte != null && cpfCnpjParte.length() > 0) {			
			sqlFrom += " AND b.CPF_CNPJ_PARTE = ?";
			ps.adicionarLong(cpfCnpjParte);
		}
		if(processoNumero != null && processoNumero.length() > 0){			
			sqlFrom += " AND b.PROC_NUMERO = ?";
			ps.adicionarString(processoNumero);
		}
		if (serventia != null && serventia.length() > 0) {
			sqlFrom += " AND b.DESC_ESCRIVANIA LIKE ?";
			ps.adicionarString("%" + serventia + "%");
		}
		if (numeroGuia != null && numeroGuia.length() > 0) {			
			sqlFrom += " AND b.NUMERO_GUIA_COMPLETO = ? ";
			ps.adicionarLong(numeroGuia);
		}
		
		sqlFrom += " ORDER BY b.PROC_NUMERO ";
		
		sqlFrom = sqlFrom.replace("WHERE AND", "WHERE");
		sqlFrom = sqlFrom.replace("WHERE ORDER", "ORDER");
		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom, ps, posicao);
			sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public List consultarPartesComDebitos(String numeroProcesso ) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO_FISICO WHERE PROC_NUMERO = ? ";
		ps.adicionarLong(Funcoes.obtenhaSomenteNumeros(numeroProcesso));
		stSql+= " ORDER BY NOME_PARTE ";		
		try{
			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				ProcessoParteDebitoFisicoDt processoParteDebitoDt = new ProcessoParteDebitoFisicoDt();
				associarDt(processoParteDebitoDt, rs1);
				liTemp.add(processoParteDebitoDt);
			}
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}

	public ProcessoParteDebitoFisicoDt consultarProcessoNumeroProad(String numeroProcessoProad) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		ProcessoParteDebitoFisicoDt Dados=null;
		ResultSetTJGO rs1 = null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO_FISICO WHERE NUMERO_PROAD = ? "; ps.adicionarLong(numeroProcessoProad);

		try{
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados= new ProcessoParteDebitoFisicoDt();
				associarDt(Dados, rs1);
			}
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}
	
	public List<ProcessoParteDebitoFisicoDt> consultarListaLiberadoCadinAindaNaoEnvido() throws Exception {
		String sql;
		List<ProcessoParteDebitoFisicoDt> listaProcessoParteDebitoDt = new ArrayList<ProcessoParteDebitoFisicoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		sql = "SELECT * FROM PROJUDI.VIEW_PROC_PARTE_DEBITO_FISICO PPDF ";
		sql += " WHERE ID_PROC_DEBITO_STATUS = ? "; ps.adicionarLong(ProcessoDebitoStatusDt.LIBERADO_PARA_ENVIO_CADIN);
		sql += " AND NOT EXISTS (SELECT 1 ";
		sql += "                   FROM PROJUDI.PROC_PARTE_DEBITO_FISICO_CADIN PPDFC ";
		sql += "                  WHERE PPDF.ID_PROC_PARTE_DEBITO_FISICO = PPDFC.ID_PROC_PARTE_DEBITO_FISICO)";
		
		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				ProcessoParteDebitoFisicoDt processoParteDebitoDt = new ProcessoParteDebitoFisicoDt();
				associarDt(processoParteDebitoDt, rs1);
				listaProcessoParteDebitoDt.add(processoParteDebitoDt);
			}		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return listaProcessoParteDebitoDt;
	}
}
