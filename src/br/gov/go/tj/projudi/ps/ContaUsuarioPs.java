package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ContaUsuarioPs extends ContaUsuarioPsGen{

    public ContaUsuarioPs(Connection conexao){
    	Conexao = conexao;
	}

    private static final long serialVersionUID = -1950843965480278862L;

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 8;

		stSql= "SELECT ID_CONTA_USU AS ID, NOME AS DESCRICAO1, BANCO AS DESCRICAO2, AGENCIA_CODIGO AS DESCRICAO3, AGENCIA AS DESCRICAO4,"
				+ " CONTA_USU_OPERACAO AS DESCRICAO5, CONTA_USU AS DESCRICAO6, CONTA_USU_DV AS DESCRICAO7, ATIVA AS DESCRICAO8";
		stSqlFrom = " FROM PROJUDI.VIEW_CONTA_USU";
		stSqlFrom += " WHERE NOME LIKE ?";
		stSqlOrder = " ORDER BY NOME ";
		ps.adicionarString(descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
}
