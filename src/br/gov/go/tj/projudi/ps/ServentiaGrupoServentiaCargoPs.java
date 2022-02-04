package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
//---------------------------------------------------------
public class ServentiaGrupoServentiaCargoPs extends ServentiaGrupoServentiaCargoPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3066080186487255251L;
	public ServentiaGrupoServentiaCargoPs(Connection conexao){
		Conexao = conexao;
	}

	public String consultarServentiaCargoServentiaGrupoGeralJson(String id_serventiagrupo, String id_Serventia ) throws Exception {

		int qtdeColunas;
		String stSql="";
		String stSqlFrom="";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		String stTemp = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT t2.ID_SERV_CARGO_SERV_GRUPO as id, t1.ID_SERV_CARGO as DESCRICAO1, t1.SERV_CARGO as DESCRICAO2, t3.ID_SERV_GRUPO as DESCRICAO3, t3.SERV_GRUPO as DESCRICAO4";
		stSqlFrom= " FROM PROJUDI.Serv_Cargo  t1";
		stSqlFrom+= " LEFT JOIN PROJUDI.VIEW_SERV_CARGO_SERV_GRUPO  t2 " +
					"ON t1.ID_SERV_CARGO = t2.ID_SERV_CARGO AND t2.ID_SERV_GRUPO = ? ";ps.adicionarLong( id_serventiagrupo);	
		stSqlFrom+= " LEFT JOIN PROJUDI.Serv_Grupo  t3 ON t3.ID_SERV_GRUPO = t2.ID_SERV_GRUPO";
		stSqlFrom += " where t1.id_serv = ? "; ps.adicionarLong( id_Serventia);	
		
		qtdeColunas = 4;
		try{
			rs1 = consultar(stSql + stSqlFrom,ps);
			
			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), "0", rs1, qtdeColunas);
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	

}
