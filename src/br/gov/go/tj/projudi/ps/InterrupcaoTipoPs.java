package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.InterrupcaoTipoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class InterrupcaoTipoPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6408837076932568497L;

	public InterrupcaoTipoPs(Connection conexao){
    	Conexao = conexao;
	}
	
	public void inserir(InterrupcaoTipoDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.INTERRUPCAO_TIPO ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getInterrupcaoTipo().length()>0)) {
			 stSqlCampos+=   stVirgula + "INTERRUPCAO_TIPO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getInterrupcaoTipo());  

			stVirgula=",";
		}
		if ((dados.getInterrupcaoTotal().length()>0)) {
			 stSqlCampos +=  stVirgula + "INTERRUPCAO_TOTAL " ;
			 stSqlValores += stVirgula + "? " ;
			 ps.adicionarBoolean(dados.getInterrupcaoTotal());  

			stVirgula=",";
		}
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 
		
		dados.setId(executarInsert(stSql,"ID_INTERRUPCAO_TIPO",ps));
	} 

	public void alterar(InterrupcaoTipoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.INTERRUPCAO_TIPO SET  ";
		
		stSql+= "INTERRUPCAO_TIPO = ?";		 ps.adicionarString(dados.getInterrupcaoTipo());  

		stSql+= ",INTERRUPCAO_TOTAL = ?";		 ps.adicionarBoolean(dados.getInterrupcaoTotal());  

		stSql += " WHERE ID_INTERRUPCAO_TIPO  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps); 
	
	} 

	public void excluir(String chave) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.INTERRUPCAO_TIPO";
		stSql += " WHERE ID_INTERRUPCAO_TIPO = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	} 

	public InterrupcaoTipoDt consultarId(String chave )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		InterrupcaoTipoDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_INTERRUPCAO_TIPO WHERE ID_INTERRUPCAO_TIPO = ?";		ps.adicionarLong(chave); 

		try {
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new InterrupcaoTipoDt();
				associarDt(Dados, rs1);
			}			
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	protected void associarDt(InterrupcaoTipoDt Dados, ResultSetTJGO rs )  throws Exception {

		Dados.setId(rs.getString("ID_INTERRUPCAO_TIPO"));
		Dados.setInterrupcaoTipo(rs.getString("INTERRUPCAO_TIPO"));
		Dados.setInterrupcaoTotal(Funcoes.FormatarLogico(rs.getString("INTERRUPCAO_TOTAL")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));		
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		Sql= "SELECT ID_INTERRUPCAO_TIPO AS ID, INTERRUPCAO_TIPO DESCRICAO1, INTERRUPCAO_TOTAL FROM PROJUDI.VIEW_INTERRUPCAO_TIPO WHERE INTERRUPCAO_TIPO LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		Sql+= " ORDER BY INTERRUPCAO_TIPO ";
		try{

			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql= "SELECT COUNT(1) as QUANTIDADE  FROM PROJUDI.VIEW_INTERRUPCAO_TIPO WHERE INTERRUPCAO_TIPO LIKE ?";
			rs2 = consultar(Sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp; 
	}
} 
