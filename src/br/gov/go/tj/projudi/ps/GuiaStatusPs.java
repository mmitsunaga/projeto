package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class GuiaStatusPs extends Persistencia {

	private static final long serialVersionUID = -1014576903009321944L;
	
	public GuiaStatusPs(Connection conexao){
		Conexao = conexao;
	}
	
	public List consultarDescricao() throws Exception {

		String Sql;
		List liTemp = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT ID_GUIA_STATUS, GUIA_STATUS FROM projudi.GUIA_STATUS WHERE ATIVO = ? ORDER BY GUIA_STATUS";
		ps.adicionarLong(GuiaTipoDt.ATIVO);
	
		try{

			rs = consultar(Sql, ps);

			while (rs.next()) {
				if( liTemp == null ) {
					liTemp = new ArrayList();
				}
				GuiaStatusDt obTemp = new GuiaStatusDt();
				obTemp.setId(rs.getString("ID_GUIA_STATUS"));
				obTemp.setGuiaStatus(rs.getString("GUIA_STATUS"));
				
				liTemp.add(obTemp);
			}
		
		}
		finally{
			 rs.close();
		}
		
		return liTemp; 
	}
}
