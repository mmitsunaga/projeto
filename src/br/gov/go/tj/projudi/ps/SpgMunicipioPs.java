package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.SpgMunicipioDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class SpgMunicipioPs extends Persistencia {

	static final String TABELA = "V_SPGUMUNICIPIOS";
	
	public SpgMunicipioPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8356210944062961060L;

	public SpgMunicipioDt consultarMunicipio(String municipio) throws Exception {
		String stSql="";
		
		SpgMunicipioDt stDt= null;
		ResultSetTJGO rs1 = null;
		try{			
			
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
					
			stSql = "SELECT NOME_MUNIC, CODG_MUNICIPIO FROM "+ TABELA + " WHERE NOME_MUNIC = ?";	ps.adicionarString( municipio );
			
			rs1 = consultar(stSql, ps);

			if(rs1.next()) {
				stDt = new SpgMunicipioDt();					
				stDt.setId(rs1.getString("CODG_MUNICIPIO"));
				stDt.setMunicipio(rs1.getString("NOME_MUNIC"));					
			}
					
		
	    } finally{
	         try{if (rs1 != null) rs1.close();} catch(Exception e1) {}	         
	    }
		
		return stDt;
	}
		
	

}
