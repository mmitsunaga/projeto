package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ProcessoDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoDebitoStatusPs extends ProcessoDebitoStatusPsGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8527905759557144236L;

	public ProcessoDebitoStatusPs(Connection conexao){
		Conexao = conexao;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql = "SELECT ID_PROC_DEBITO_STATUS AS ID, PROC_DEBITO_STATUS AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_PROC_DEBITO_STATUS";
		stSqlFrom += " WHERE PROC_DEBITO_STATUS LIKE ?";
		stSqlOrder = " ORDER BY PROC_DEBITO_STATUS ";
		ps.adicionarString( descricao +"%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public List consultarDescricao(String descricao) throws Exception {
		String stSql="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_PROC_DEBITO_STATUS, PROC_DEBITO_STATUS FROM PROJUDI.VIEW_PROC_DEBITO_STATUS WHERE PROC_DEBITO_STATUS LIKE ?";
		stSql+= " ORDER BY PROC_DEBITO_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultar(stSql, ps);
			
			while (rs1.next()) {
				ProcessoDebitoStatusDt obTemp = new ProcessoDebitoStatusDt();
				obTemp.setId(rs1.getString("ID_PROC_DEBITO_STATUS"));
				obTemp.setProcessoDebitoStatus(rs1.getString("PROC_DEBITO_STATUS"));
				liTemp.add(obTemp);
			}		
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}			
		}
		
		return liTemp; 
	}
}
