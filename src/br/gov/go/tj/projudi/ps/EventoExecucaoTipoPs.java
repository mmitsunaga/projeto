package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class EventoExecucaoTipoPs extends EventoExecucaoTipoPsGen{

    private static final long serialVersionUID = -4232218383261361731L;

    public EventoExecucaoTipoPs(Connection conexao){
    	Conexao = conexao;
	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		stSql= "SELECT ID_EVENTO_EXE_TIPO as id, EVENTO_EXE_TIPO as descricao1";
		stSqlFrom = " FROM PROJUDI.VIEW_EVENTO_EXE_TIPO";
		stSqlFrom += " WHERE EVENTO_EXE_TIPO LIKE ?";
		stSqlOrder = " ORDER BY EVENTO_EXE_TIPO ";
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
