package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.ForumDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ForumPs extends ForumPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = 7782514719936488881L;

    public ForumPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método criado para associar um ResultSetTJGO a um Dt de Fórum. Chama o método da super classe e
	 * associa os dados de endereço.
	 */
	public void associarDt( ForumDt Dados, ResultSetTJGO rs1 )  throws Exception {
		
		super.associarDt(Dados, rs1);
		Dados.getEnderecoForum().setLogradouro(rs1.getString("LOGRADOURO"));
		Dados.getEnderecoForum().setNumero(rs1.getString("NUMERO"));
		Dados.getEnderecoForum().setComplemento(rs1.getString("COMPLEMENTO"));
		Dados.getEnderecoForum().setId_Bairro(rs1.getString("ID_BAIRRO"));
		Dados.getEnderecoForum().setBairro(rs1.getString("BAIRRO"));
		Dados.getEnderecoForum().setCidade(rs1.getString("CIDADE"));
		Dados.getEnderecoForum().setUf(rs1.getString("UF"));
		Dados.getEnderecoForum().setCep(rs1.getString("CEP"));

	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom="";
		String stSqlOrder="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_FORUM AS ID, FORUM AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_FORUM";
		stSqlFrom += " WHERE FORUM LIKE ?";
		stSqlOrder = " ORDER BY FORUM ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);		
		}
			finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
	public ForumDt consultarForumCodigo(String forumCodigo)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ForumDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_FORUM WHERE FORUM_CODIGO = ?";
		ps.adicionarLong(forumCodigo); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new ForumDt();
				associarDt(Dados, rs1);
			}
		
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}
}
