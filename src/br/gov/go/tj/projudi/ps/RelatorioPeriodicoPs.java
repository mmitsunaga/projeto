package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RelatorioPeriodicoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RelatorioPeriodicoPs extends RelatorioPeriodicoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -7296995814816029477L;

    public RelatorioPeriodicoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método que gera o Relatório Periódico baseado nos valores informados em tela.
	 * 
	 * @param relatorioPeriodicoDt - relatório periódico preenchido em tela
	 * @return ResultSetTJGO com o resultado da consulta
	 * @throws Exception
	 * @author hmgodinho
	 */
	public ResultSetTJGO gerarRelatorioPeriodico(String sql) throws Exception {
		ResultSetTJGO rs = null;
		try{
			rs = consultarSemParametros(sql);

		
		} finally{
			// Não pode fechar este ResultSetTJGO pois ele será retornado para o método da classe de negócio.
			// try{
			// if (rs != null)
			// rs.close();
			// } catch(Exception e) {
			// }
		}
		return rs;
	}

	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 1;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_RELATORIO_PERIODICO as id, RELATORIO_PERIODICO as descricao1";
		stSqlFrom = " FROM PROJUDI.VIEW_RELATORIO_PERIODICO";
		stSqlFrom += " WHERE RELATORIO_PERIODICO LIKE ?";
		stSqlOrder = " ORDER BY RELATORIO_PERIODICO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
}
