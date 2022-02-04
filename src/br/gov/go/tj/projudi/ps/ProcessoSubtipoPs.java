package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProcessoSubtipoPs extends ProcessoSubtipoPsGen {

	private static final long serialVersionUID = -5896477566925933025L;

	public ProcessoSubtipoPs(Connection conexao){
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

		stSql = "SELECT ID_PROC_SUBTIPO AS ID, PROC_SUBTIPO AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_PROC_SUBTIPO";
		stSqlFrom += " WHERE PROC_SUBTIPO LIKE ?";
		stSqlOrder = " ORDER BY PROC_SUBTIPO ";
		ps.adicionarString("%"+ descricao +"%");

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null)
					rs2.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}
}
