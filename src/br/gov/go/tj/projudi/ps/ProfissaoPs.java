package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.ProfissaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ProfissaoPs extends ProfissaoPsGen {
	
	/**
     * 
     */
    private static final long serialVersionUID = -7299238183549573396L;

    public ProfissaoPs(Connection conexao){
    	Conexao = conexao;
	}

	public ProfissaoDt consultarCodigo(String codigo_profissao)  throws Exception {
		String Sql;
		ProfissaoDt Dados=null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_PROFISSAO WHERE PROFISSAO_CODIGO = ?";
		ps.adicionarLong(codigo_profissao);
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados= new ProfissaoDt();
				associarDt(Dados, rs1);
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}
    
    public String consultarIdProfissao(String descricao)  throws Exception {
		String sql;
		String id = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql= "SELECT ID_PROFISSAO FROM PROJUDI.VIEW_PROFISSAO WHERE PROFISSAO like ?";
		ps.adicionarString("%"+ descricao +"%");
		
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				id = rs1.getString("ID_PROFISSAO");
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return id; 
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

		stSql= "SELECT ID_PROFISSAO AS ID, PROFISSAO AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_PROFISSAO";
		stSqlFrom += " WHERE PROFISSAO LIKE ?";
		stSqlOrder = " ORDER BY PROFISSAO ";
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
