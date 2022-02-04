package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AlcunhaPs extends AlcunhaPsGen{

    private static final long serialVersionUID = 2506435711004263492L;
    
    @SuppressWarnings("unused")
	private AlcunhaPs(){}
    
    public AlcunhaPs(Connection conexao){
    	Conexao = conexao;
	}

	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao do PROJUDI
			ordenacao = " ALCUNHA ";

		String stSql="";
		String stSqlFrom="";
		String stSqlOrder="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_ALCUNHA AS ID, ALCUNHA AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_ALCUNHA";
		stSqlFrom += " WHERE ALCUNHA LIKE ?";
		stSqlOrder = " ORDER BY " + ordenacao;
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);
			stSql= "SELECT COUNT(*) AS QUANTIDADE";
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
