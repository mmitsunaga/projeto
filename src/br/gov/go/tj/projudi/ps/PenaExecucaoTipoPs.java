package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class PenaExecucaoTipoPs extends PenaExecucaoTipoPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = -3283032619009400883L;

    public PenaExecucaoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	public List consultarIds(String id_opcoes) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String[] vetorIds_Opcoes =  id_opcoes.split(",");
		
		Sql= "SELECT ID_PENA_EXE_TIPO, PENA_EXE_TIPO FROM PROJUDI.VIEW_VIEWPENA_EXECUCAO_TIPO WHERE ID_PENA_EXE_TIPO IN (";
		boolean ehPrimeiroItem = true;
		for (String id : vetorIds_Opcoes)
		{
			if (!ehPrimeiroItem) Sql+= ",";
			Sql+= "?";
			ps.adicionarLong(id);
			ehPrimeiroItem = false;
		}		
		Sql+= ") ORDER BY PENA_EXE_TIPO ";	
		
		try{
			rs = consultar(Sql, ps);
			while (rs.next()) {
				PenaExecucaoTipoDt obTemp = new PenaExecucaoTipoDt();
				obTemp.setId(rs.getString("ID_PENA_EXE_TIPO"));
				obTemp.setPenaExecucaoTipo(rs.getString("PENA_EXE_TIPO"));
				liTemp.add(obTemp);
			}
		} finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
    
	 public String consultarIdsJSON(String id_opcoes) throws Exception {
	
		String stSql="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		String[] vetorIds_Opcoes =  id_opcoes.split(",");
		
		stSql= "SELECT ID_PENA_EXE_TIPO as id, PENA_EXE_TIPO as DESCRICAO1 FROM PROJUDI.VIEW_VIEWPENA_EXECUCAO_TIPO WHERE ID_PENA_EXE_TIPO IN (";
		boolean ehPrimeiroItem = true;
		for (String id : vetorIds_Opcoes)
		{
			if (!ehPrimeiroItem) stSql+= ",";
			stSql+= "?";
			ps.adicionarLong(id);
			ehPrimeiroItem = false;
		}		
		stSql+= ") ORDER BY PENA_EXE_TIPO ";	

		try{
			rs1 = consultar(stSql, ps);
			stTemp = gerarJSON(2, "0", rs1, qtdeColunas);
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	 
    public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		stSql= "SELECT ID_PENA_EXE_TIPO AS ID, PENA_EXE_TIPO AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_VIEWPENA_EXECUCAO_TIPO";
		stSqlFrom += " WHERE PENA_EXE_TIPO LIKE ?";
		stSqlOrder = " ORDER BY PENA_EXE_TIPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
}
