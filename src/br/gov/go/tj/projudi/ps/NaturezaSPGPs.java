package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.NaturezaSPGDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class NaturezaSPGPs extends Persistencia {
	
	private static final long serialVersionUID = -620833502989724109L;

	public NaturezaSPGPs(Connection conexao){
    	Conexao = conexao;
	}
	
	/**
	 * Método para consultar NaturezaSPGDt pelo id.
	 * @param String id
	 * @return NaturezaSPGDt
	 * @throws Exception
	 */
	public NaturezaSPGDt consultarId(String id)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		NaturezaSPGDt naturezaSPGDt = null;
		
		stSql= "SELECT * FROM PROJUDI.VIEW_NATUREZA_SPG WHERE ID_NATUREZA_SPG = ?";
		ps.adicionarLong(id); 
		
		try {
			rs1 = consultar(stSql,ps);
			
			if (rs1.next()) {
				naturezaSPGDt = new NaturezaSPGDt();
				associarDt(naturezaSPGDt, rs1);
			}
		}
		finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		return naturezaSPGDt; 
	}
	
	/**
	 * Método para consultar JSON da página de localizar.
	 * 
	 * @param String descricao
	 * @param String posicao
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoNaturezaSPGJSON(String descricao, String codigoSPG,  String posicao ) throws Exception {
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql= "SELECT ID_NATUREZA_SPG as id, NATUREZA_SPG as descricao1, NATUREZA_SPG_CODIGO as descricao2";
		stSqlFrom = " FROM PROJUDI.VIEW_NATUREZA_SPG";
		stSqlFrom += " WHERE NATUREZA_SPG LIKE ?";
		ps.adicionarString("%"+descricao+"%");
		
		if (Funcoes.StringToInt(codigoSPG) > 0) {
			stSqlFrom += " AND NATUREZA_SPG_CODIGO = ?";
			ps.adicionarLong(codigoSPG);	
		}		
		stSqlOrder = " ORDER BY NATUREZA_SPG ";		
		
		try {
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";			
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}
		finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		
		return stTemp; 
	}
	
	/**
	 * Método para associar dt de NaturezaSPGDt.
	 * @param NaturezaSPG naturezaSPGDt
	 * @param ResultSetTJGO rs
	 * @throws Exception
	 */
	protected void associarDt(NaturezaSPGDt naturezaSPGDt, ResultSetTJGO rs)  throws Exception {
		
		naturezaSPGDt.setId(rs.getString("ID_NATUREZA_SPG"));
		naturezaSPGDt.setNaturezaSPG(rs.getString("NATUREZA_SPG"));
		naturezaSPGDt.setNaturezaSPGCodigo( rs.getString("NATUREZA_SPG_CODIGO"));
		naturezaSPGDt.setCodigoTemp(rs.getString("CODIGO_TEMP"));
	}
	
	/**
	 * Método para inserir a NaturezaSPGDt no banco de dados.
	 * @param NaturezaSPGDt dados
	 * @throws Exception
	 */
	public void inserir(NaturezaSPGDt dados) throws Exception { 

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSqlCampos= "INSERT INTO PROJUDI.NATUREZA_SPG ("; 

		stSqlValores +=  " Values (";
		
		if (dados.getNaturezaSPG() != null && dados.getNaturezaSPG().length()>0) {
			 stSqlCampos+=   stVirgula + "NATUREZA_SPG " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarString(dados.getNaturezaSPG());  

			stVirgula=",";
		}
		
		if (dados.getNaturezaSPGCodigo() != null && dados.getNaturezaSPGCodigo().length()>0) {
			 stSqlCampos+=   stVirgula + "NATUREZA_SPG_CODIGO " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getNaturezaSPGCodigo());  

			stVirgula=",";
		}
		
		if (dados.getCodigoTemp() != null && dados.getCodigoTemp().length()>0) {
			 stSqlCampos+=   stVirgula + "CODIGO_TEMP " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarBigDecimal(dados.getCodigoTemp());  

			stVirgula=",";
		}
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_NATUREZA_SPG",ps)); 
	} 
	
	/**
	 * Método para alterar a NaturezaSPGDt no banco de dados.
	 * @param NaturezaSPGDt dados
	 * @throws Exception
	 */
	public void alterar(NaturezaSPGDt dados) throws Exception{ 

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";
		String stVirgula="";

		stSql= "UPDATE PROJUDI.NATUREZA_SPG SET  ";
		
		if ((dados.getNaturezaSPG() != null && dados.getNaturezaSPG().length()>0)) {
			stSql+= " NATUREZA_SPG = ?";		 
			ps.adicionarString(dados.getNaturezaSPG());
			
			stVirgula=",";
		}
		
		if ((dados.getNaturezaSPGCodigo() != null && dados.getNaturezaSPGCodigo().length()>0)) {
			stSql+= stVirgula + " NATUREZA_SPG_CODIGO = ?";		 
			ps.adicionarLong(dados.getNaturezaSPGCodigo());
			
			stVirgula=",";
		}
		
		if ((dados.getCodigoTemp() != null && dados.getCodigoTemp().length()>0)) {
			stSql+= stVirgula + " CODIGO_TEMP = ?";		 
			ps.adicionarBigDecimal(dados.getCodigoTemp());
			
			stVirgula=",";
		}
		
		stSql += " WHERE ID_NATUREZA_SPG  = ? "; 		
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);	
	} 
	
	/**
	 * Método para excluir a NaturezaSPGDt no banco de dados.
	 * @param NaturezaSPGDt dados
	 * @throws Exception
	 */
	public void excluir( String chave) throws Exception { 

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.NATUREZA_SPG";
		stSql += " WHERE ID_NATUREZA_SPG = ?";		ps.adicionarLong(chave); 

		executarUpdateDelete(stSql,ps);
	}
	
	/**
	 * Método para consultar NaturezaSPGDt pelo código.
	 * @param String codigo
	 * @return NaturezaSPGDt
	 * @throws Exception
	 */
	public NaturezaSPGDt consultarCodigo(String codigo)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		NaturezaSPGDt naturezaSPGDt = null;
		
		stSql= "SELECT * FROM PROJUDI.VIEW_NATUREZA_SPG WHERE NATUREZA_SPG_CODIGO = ?";
		ps.adicionarLong(codigo); 
		
		try {
			rs1 = consultar(stSql,ps);
			
			if (rs1.next()) {
				naturezaSPGDt = new NaturezaSPGDt();
				associarDt(naturezaSPGDt, rs1);
			}
		}
		finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		return naturezaSPGDt; 
	}
}
