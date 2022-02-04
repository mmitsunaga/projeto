package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.ImpedimentoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ImpedimentoTipoPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8314727465254120383L;
	public ImpedimentoTipoPs(Connection conexao){
    	Conexao = conexao;
	}
	
	public ImpedimentoTipoDt consultarProcessoParteTipoCodigo(String votanteTipoCodigo) throws Exception {
		String Sql;
		ImpedimentoTipoDt Dados = new ImpedimentoTipoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.IMPEDIMENTO_TIPO WHERE IMPEDIMENTO_TIPO_CODIGO = ?";
		ps.adicionarLong(votanteTipoCodigo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_IMPEDIMENTO_TIPO"));
				Dados.setImpedimentoTipo(rs1.getString("IMPEDIMENTO_TIPO"));
				Dados.setImpedimentoTipoCodigo(rs1.getString("IMPEDIMENTO_TIPO_CODIGO"));				
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	
	public void alterarImpedimentoTipo(String idServentiaCargo, String idAudienciaProcesso, int novoTipo) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.AUDI_PROC_VOTANTES SET  ";
		stSql+= " ID_IMPEDIMENTO_TIPO = (SELECT ID_IMPEDIMENTO_TIPO FROM PROJUDI.IMPEDIMENTO_TIPO WHERE IMPEDIMENTO_TIPO_CODIGO = ?) ";
		ps.adicionarLong(novoTipo);

		stSql += " WHERE ID_AUDI_PROC =  ?"; ps.adicionarLong(idAudienciaProcesso);
		stSql += " AND ID_SERV_CARGO = ? "; ps.adicionarLong(idServentiaCargo);

		executarUpdateDelete(stSql,ps);
	
	}
}
