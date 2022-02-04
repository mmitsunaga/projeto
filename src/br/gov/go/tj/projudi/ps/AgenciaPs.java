package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AgenciaPs extends AgenciaPsGen{

    private static final long serialVersionUID = 6733586447859692858L;

    @SuppressWarnings("unused")
	private AgenciaPs(){}
    
    public AgenciaPs(Connection conexao){
    	Conexao = conexao;
	}
    
    public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom="";
		String stSqlOrder="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_AGENCIA AS ID, AGENCIA AS DESCRICAO1, AGENCIA_CODIGO AS DESCRICAO2,  BANCO AS DESCRICAO3";
		stSqlFrom = " FROM PROJUDI.VIEW_AGENCIA";
		stSqlFrom += " WHERE AGENCIA LIKE ?";
		stSqlOrder = " ORDER BY DESCRICAO1 ";
		ps.adicionarString("%"+descricao+"%"); 
		int qtdeColunas = 3;
		 
		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
    
    //Para o PJD: necessários mais dois parâmetros -> String ordenacao, String quantidadeRegistros
    public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
    	
    	int qtdeColunas;
		String stSql="";
		String stSqlFrom="";
		String stSqlOrder="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		//Para o PJD: necessários mais dois campos -> AGENCIA_CODIGO AS DESCRICAO2, BANCO AS DESCRICAO3
		stSql= "SELECT ID_AGENCIA AS ID, AGENCIA AS DESCRICAO1, AGENCIA_CODIGO AS DESCRICAO2, BANCO AS DESCRICAO3";
		stSqlFrom = " FROM PROJUDI.VIEW_AGENCIA";
		
    	stSqlFrom += "WHERE (AGENCIA LIKE ? OR AGENCIA_CODIGO LIKE ? OR BANCO LIKE ?) ";
		stSqlOrder = "ORDER BY " + ordenacao;
		ps.adicionarString("%" + descricao + "%");
		ps.adicionarString("%" + descricao + "%");
		ps.adicionarString("%" + descricao + "%");
		qtdeColunas = 3;
    	try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);

			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

}
