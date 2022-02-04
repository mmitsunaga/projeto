package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GrupoTipoPs extends GrupoTipoPsGen {

	private static final long serialVersionUID = -7488745197043524780L;

	@SuppressWarnings("unused")
	private GrupoTipoPs( ) {}
	
	public GrupoTipoPs(Connection conexao){
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

		stSql = "SELECT ID_GRUPO_TIPO as id, GRUPO_TIPO as descricao1";
		stSqlFrom = " FROM PROJUDI.VIEW_GRUPO_TIPO";
		stSqlFrom += " WHERE GRUPO_TIPO LIKE ?";
		stSqlOrder = " ORDER BY GRUPO_TIPO ";
		ps.adicionarString( descricao +"%");

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		
		} finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e) {}
			try{if (rs2 != null)rs2.close();} catch(Exception e) {
			}
		}
		return stTemp;
	}
}
