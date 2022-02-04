package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class BancoPs extends BancoPsGen{

    private static final long serialVersionUID = -7893526487955056494L;

    public BancoPs(Connection conexao){
    	Conexao = conexao;
	}

	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros ) throws Exception {
		
		if(ORDENACAO_PADRAO.equals(ordenacao)) //ordenação PROJUDI
			ordenacao = " BANCO ";

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		stSql= "SELECT ID_BANCO AS ID, BANCO_CODIGO AS DESCRICAO1, BANCO AS DESCRICAO2";
		stSqlFrom = " FROM PROJUDI.VIEW_BANCO";
		stSqlFrom += " WHERE BANCO LIKE ?";
		stSqlOrder = " ORDER BY " + ordenacao;
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);
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