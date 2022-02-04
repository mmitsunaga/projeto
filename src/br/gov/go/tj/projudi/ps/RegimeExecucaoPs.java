package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RegimeExecucaoPs extends RegimeExecucaoPsGen{

    private static final long serialVersionUID = 6562377618553068601L;

    public RegimeExecucaoPs(Connection conexao){
    	Conexao = conexao;
    }

	/**
	 * Consulta os regimes disponíveis a partir do tipo de pena
	 * 
	 * @param id_PenaExecucaoTipo, tipo de pena do regime em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 */
	public List consultarDescricao(String descricao, String id_PenaExecucaoTipo, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT ID_REGIME_EXE, REGIME_EXE, PENA_EXE_TIPO"; 
		SqlFrom = " FROM PROJUDI.VIEW_REGIME_EXE";
		SqlFrom += " WHERE REGIME_EXE LIKE ? ";
		ps.adicionarString("%"+ descricao +"%");
		if (id_PenaExecucaoTipo.length() > 0){
			SqlFrom += " AND ID_PENA_EXE_TIPO = ? ";
			ps.adicionarLong(id_PenaExecucaoTipo);
		}
		SqlOrder = " ORDER BY REGIME_EXE ";		
		try{
			if (posicao.length() > 0)
				rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			else 
				rs1 = consultar(Sql + SqlFrom + SqlOrder, ps);

			while (rs1.next()) {
				RegimeExecucaoDt obTemp = new RegimeExecucaoDt();
				obTemp.setId(rs1.getString("ID_REGIME_EXE"));
				obTemp.setRegimeExecucao(rs1.getString("REGIME_EXE"));
				obTemp.setPenaExecucaoTipo(rs1.getString("PENA_EXE_TIPO"));
				liTemp.add(obTemp);
			}
			if (posicao.length() > 0){
				Sql= "SELECT COUNT(*) as QUANTIDADE";
				rs2 = consultar(Sql + SqlFrom,ps);
				if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));	
			}
			
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp="";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		stSql= "SELECT ID_REGIME_EXE as ID, REGIME_EXE as DESCRICAO1, PENA_EXE_TIPO as DESCRICAO2 FROM PROJUDI.VIEW_REGIME_EXE WHERE REGIME_EXE LIKE ?";
		stSql+= " ORDER BY REGIME_EXE ";
		ps.adicionarString("%"+descricao+"%");		
		
		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_REGIME_EXE WHERE REGIME_EXE LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	
	/**
	 * Consulta o regime informando o id
	 * @param id_RegimeExecucao, identificação do regime
	 * @return RegimeExecucaoDt
	 * @author wcsilva
	 */
	public RegimeExecucaoDt consultarRegime(String id_RegimeExecucao) throws Exception {

		StringBuffer sql = new StringBuffer();
		RegimeExecucaoDt dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql.append("SELECT * FROM PROJUDI.VIEW_REGIME_EXE");
		sql.append(" WHERE ID_REGIME_EXE = ? ");
		ps.adicionarLong(id_RegimeExecucao);
//		sql.append(" AND ID_PENA_EXE_TIPO = " + String.valueOf(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE));
		
		try{
			rs1 = consultar(sql.toString(), ps);
			rs1.next();
			dados = new RegimeExecucaoDt();
			associarDt(dados, rs1);
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return dados; 
	}
}
