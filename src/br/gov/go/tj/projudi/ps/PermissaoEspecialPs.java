package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PermissaoEspecialPs extends PermissaoEspecialPsGen {

	public PermissaoEspecialPs(Connection conexao){
		Conexao = conexao;
	}

	private static final long serialVersionUID = -7809862768265594700L;

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_PERM_ESPECIAL AS ID, PERM_ESPECIAL AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_PERM_ESPECIAL";
		stSqlFrom += " WHERE PERM_ESPECIAL LIKE ?";
		stSqlOrder = " ORDER BY ID_PERM_ESPECIAL ";
		ps.adicionarString( descricao +"%");
		int qtdeColunas = 1;

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
}
