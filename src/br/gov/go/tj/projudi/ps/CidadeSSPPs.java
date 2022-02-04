package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.CidadeSSPDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class CidadeSSPPs extends CidadeSSPPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -4411668413053732342L;

    public CidadeSSPPs(Connection conexao){
    	Conexao = conexao;
	}

	public CidadeSSPDt buscaCidadeTJ(String id_CidadeSSP) throws Exception {
		CidadeSSPDt cidadeSSPDt = null;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{

			sql = "SELECT * FROM PROJUDI.VIEW_CIDADE_SSP c WHERE c.ID_CIDADE_SSP = ?";
			ps.adicionarLong(id_CidadeSSP);

			 rs1 = consultar(sql,ps);
			if (rs1.next()) {
				cidadeSSPDt = new CidadeSSPDt();
				cidadeSSPDt.setId(rs1.getString("ID_CIDADE_SSP"));
				cidadeSSPDt.setCidade(rs1.getString("CIDADE"));
				cidadeSSPDt.setId_CidadeTJ(rs1.getString("ID_CIDADE_TJ"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return cidadeSSPDt;
	}

}
