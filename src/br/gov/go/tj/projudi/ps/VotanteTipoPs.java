package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class VotanteTipoPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1716100378167904241L;
	
	public VotanteTipoPs(Connection conexao){
    	Conexao = conexao;
	}
	
	public VotanteTipoDt consultarProcessoParteTipoCodigo(String votanteTipoCodigo) throws Exception {
		String Sql;
		VotanteTipoDt Dados = new VotanteTipoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VOTANTE_TIPO WHERE VOTANTE_TIPO_CODIGO = ?";
		ps.adicionarLong(votanteTipoCodigo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_VOTANTE_TIPO"));
				Dados.setVotanteTipo(rs1.getString("VOTANTE_TIPO"));
				Dados.setVotanteTipoCodigo(rs1.getString("VOTANTE_TIPO_CODIGO"));				
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}
	//lrcampos 10/07/2019 * consulta codigo do votante passando o id
	public Integer  consultarVotanteTipoId(String votanteTipoId) throws Exception {
		String Sql;
		Integer codigoVotante = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VOTANTE_TIPO WHERE ID_VOTANTE_TIPO = ?";
		ps.adicionarLong(votanteTipoId);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				codigoVotante = rs1.getInt("VOTANTE_TIPO_CODIGO");				
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return codigoVotante;
	}

}
