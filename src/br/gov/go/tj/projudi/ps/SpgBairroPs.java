package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.SpgBairroDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class SpgBairroPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3206631831995757085L;

	public SpgBairroPs(Connection conexao){
		Conexao = conexao;
	}

	public SpgBairroDt consultarBairro(String bairro) throws Exception {
		String stSql="";
		
		SpgBairroDt stDt= null;
		ResultSetTJGO rs1 = null;
		try{			
			
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
					
			stSql = "SELECT NOME_BAIRRO,CODG_BAIRRO FROM V_SPGUBAIRROS WHERE NOME_BAIRRO = ?"; ps.adicionarString( bairro);				
					
			rs1 = consultar(stSql, ps);

			if(rs1.next()) {
				stDt = new SpgBairroDt();
				stDt.setId( Funcoes.completarZeros(rs1.getString("CODG_BAIRRO"),5));
				stDt.setBairro(rs1.getString("NOME_BAIRRO"));					
			}
					
		
	    } finally{
	         try{if (rs1 != null) rs1.close();} catch(Exception e1) {}	         
	    }
		
		return stDt;
	}

}
