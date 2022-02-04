package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.OficialSSGDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class OficialSSGPs extends Persistencia {
	
	private static final long serialVersionUID = 1754237543728934L;
	
	public OficialSSGPs(Connection conexao){
		Conexao = conexao;
	}
	
	/**
	 * Método para consultar oficiais.
	 * @return List<OficialSSGDt>
	 * @throws Exception
	 */
	public List consultarOficiais() throws Exception {
		List listaOficiaisSSGDt = null;
		
		String sql = "SELECT NOME_OFICIAL, CODG_OFICIAL FROM SSGU-OFICIAIS ORDER BY NOME_OFICIAL";
		
		ResultSetTJGO rs1 = null;
		
		try{
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
//			rs = conn.createStatement().executeQuery(sql);
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				listaOficiaisSSGDt = new ArrayList();
				while(rs1.next()) {
					
					if( rs1.getString("NOME_OFICIAL") != null 
						&& rs1.getString("NOME_OFICIAL").length() > 1 //Maior que um pq tem nomes cadastrados no SPG que é só um "ponto"
						&& rs1.getString("CODG_OFICIAL") != null
						&& rs1.getString("CODG_OFICIAL").length() > 0 ) {
						
						OficialSSGDt oficialSSGDt = new OficialSSGDt();
						
						oficialSSGDt.setNomeOficial(	rs1.getString("NOME_OFICIAL"));
						oficialSSGDt.setCodigoOficial(	rs1.getString("CODG_OFICIAL"));
						
						listaOficiaisSSGDt.add(oficialSSGDt);
					}
				}
			}
			
		
		}
		finally{
			//Fecha conexão e resultset
			if( rs1 != null ) {
				rs1.close();
			}
		}
		
		return listaOficiaisSSGDt;
	}
	
	/**
	 * Método para consultar o oficial através do codigo.
	 * @param String codigoOficial
	 * @return OficialSSGDt
	 * @throws Exception
	 */
	public OficialSSGDt consultaOficial(String codigoOficial) throws Exception{
		OficialSSGDt oficialSSGDt = new OficialSSGDt();
				
		String sql = "SELECT NOME_OFICIAL, CODG_OFICIAL FROM SSGU-OFICIAIS WHERE CODG_OFICIAL = ?";
				
//		PreparedStatement prest = conn.prepareStatement(sql);
//		prest.setLong(1, Funcoes.StringToLong(codigoOficial));
		
		ResultSetTJGO rs1 = null;
		
		try{
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			ps.adicionarLong(Funcoes.StringToLong(codigoOficial));
			
			//rs = conn.createStatement().executeQuery(sql);
//			rs = prest.executeQuery();
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					
					oficialSSGDt.setNomeOficial(	rs1.getString("NOME_OFICIAL"));
					oficialSSGDt.setCodigoOficial(	rs1.getString("CODG_OFICIAL"));
						
					}
				}
			
		
		}
		finally{
			//Fecha conexão e resultset
			if( rs1 != null ) {
				rs1.close();
			}
		}
		
		return oficialSSGDt;
	}

}
