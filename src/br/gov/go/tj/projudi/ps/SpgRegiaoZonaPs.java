package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.SpgRegiaoZonaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class SpgRegiaoZonaPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3206631831995757085L;

	public SpgRegiaoZonaPs(Connection conexao){
		Conexao = conexao;
	}

	public SpgRegiaoZonaDt consultaRegiaoZonaCivel(String id_MuncipioId_Bairro) throws Exception {
		String stSql="";
		
		SpgRegiaoZonaDt stDt= null;
		ResultSetTJGO rs1 = null;
		try {
			
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
					
			stSql = "SELECT * FROM V_SPGUREGBAIRROS WHERE CODG_MUNIC_BAIRRO = ?";
			ps.adicionarString(id_MuncipioId_Bairro);	
					
			rs1 = consultar(stSql, ps);

			if(rs1.next()) {
				stDt = new SpgRegiaoZonaDt();					
				stDt.setId(rs1.getString("ISN_SPGU_REG_BAIRROS"));
				stDt.setRegiaoCodigo(rs1.getString("CODG_REGIAO_1"));
				stDt.setZonaCodigo(rs1.getString("CODG_ZONA_1"));
			}
					
		
	    }
		finally {
	         try{if (rs1 != null) rs1.close();} catch(Exception e1) {}	         
	    }
		
		return stDt;
	}
	
	public SpgRegiaoZonaDt consultaRegiaoZonaCriminal(String id_MuncipioId_Bairro) throws Exception {
		String stSql="";
		
		SpgRegiaoZonaDt stDt= null;
		ResultSetTJGO rs1 = null;
		try {
			
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
					
			stSql = "SELECT * FROM V_SPGUREGBAIRROS WHERE CODG_MUNIC_BAIRRO = ?";
			ps.adicionarString(id_MuncipioId_Bairro);	
					
			rs1 = consultar(stSql, ps);

			if(rs1.next()) {
				stDt = new SpgRegiaoZonaDt();					
				stDt.setId(rs1.getString("ISN_SPGU_REG_BAIRROS"));
				stDt.setRegiaoCodigo(rs1.getString("CODG_REGIAO_2"));
				stDt.setZonaCodigo(rs1.getString("CODG_ZONA_2"));
			}
					
		
	    }
		finally {
	         try{if (rs1 != null) rs1.close();} catch(Exception e1) {}	         
	    }
		
		return stDt;
	}
	
	/**
	 * Método que atualiza o ID_PROJUDI na view V_SPGUREGBAIRROS.
	 * @param String codigoMunicipioBairro
	 * @param String idBairro
	 * @param boolean capital
	 * @return boolean 
	 * @throws Exception
	 */
	public boolean atualizarIdProjudi(String codigoMunicipioBairro, String idBairro, boolean capital) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String ambiente = "_REM";
		if( capital ) {
			ambiente = "";
		}
		
		String sqlUpdate= "UPDATE V_SPGUREGBAIRROS"+ ambiente +" SET ID_PROJUDI = " + idBairro + " WHERE ";
		sqlUpdate += " ID_PROJUDI IS NULL ";
		sqlUpdate += " AND CODG_MUNIC_BAIRRO = " + codigoMunicipioBairro;
		
		return executarUpdateDelete(sqlUpdate, ps) > 0;
	}

}
