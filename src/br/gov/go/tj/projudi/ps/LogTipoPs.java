package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class LogTipoPs extends LogTipoPsGen{

	public LogTipoPs(Connection conexao){
    	Conexao = conexao;
	}

    private static final long serialVersionUID = 8758302291049930090L;

	public String consultarDescricaoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {		

		String stSql="";		
		String stSqlFrom ="";
		String stSqlOrder ="";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		String stTemp="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_LOG_TIPO as id, LOG_TIPO as descricao1";
		stSqlFrom = " FROM PROJUDI.VIEW_LOG_TIPO";
		stSqlFrom += " WHERE LOG_TIPO LIKE ?";
		ps.adicionarString("%"+tempNomeBusca+"%"); 
		stSqlOrder = " ORDER BY LOG_TIPO ";		

		int qtdeColunas=1;
		
		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicaoPaginaAtual);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
}