package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PonteiroLogCompensarDt;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PonteiroLogCompensarPs extends PonteiroLogCompensarPsGen{

	private static final long serialVersionUID = 4599293785813841709L;
	private PonteiroLogCompensarPs( ) {}
	public PonteiroLogCompensarPs(Connection conexao) {
		Conexao = conexao;
	}

	public List consultarPonteiroCompensar(String id_AreaDistribuicao, String id_Serv, String id_ServentiaCargo) throws Exception {

		String stSql, stSqlFrom;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * ";
		stSqlFrom= " FROM PROJUDI.VIEW_PONTEIRO_LOG_COMPENSAR "; 
		stSqlFrom+= " WHERE Id_Area_Dist_O = ? ";							ps.adicionarLong(id_AreaDistribuicao); 
		stSqlFrom+= " and Id_Serv_O = ? ";									ps.adicionarLong(id_Serv);
		stSqlFrom+= " and Id_Serv_Cargo_O = ? ";							ps.adicionarLong(id_ServentiaCargo);
		stSqlFrom+= " and Data_Inicio <= ? ";								ps.adicionarDate(new Date());
		stSqlFrom+= " and Data_Final IS NULL ";			
				
		try{

			rs1 = consultar(stSql + stSqlFrom , ps);


			while (rs1.next()) {
				PonteiroLogCompensarDt Dados= new PonteiroLogCompensarDt();
				associarDt(Dados, rs1);
								
				liTemp.add(Dados);
			}
					

		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}			
		}
			return liTemp; 
			
	}
	
	public void finalizarPonteiro(String idPonteiroLogCompensacao, String idUsusarioFinalizador) throws Exception {
		ResultSetTJGO rs1=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String stSql = "UPDATE PONTEIRO_LOG_COMPENSAR "; 
		stSql += " SET DATA_FINAL = ? ";					ps.adicionarDateTime(new Date());
		stSql += " , ID_USU_SERV_F = ? ";					ps.adicionarLong(idUsusarioFinalizador);
		stSql += " WHERE ID_PONTEIRO_LOG_COMPENSAR = ? ";	ps.adicionarLong(idPonteiroLogCompensacao); 
				
		try{
			executarUpdateDelete(stSql, ps);
		}catch (Exception e) {
			throw new MensagemException("PonteiroLogCompensarPs.finalizarPonteiro() - Não foi possível finalizar o ponteiro de ID " + idPonteiroLogCompensacao + ". Favor contatar o suporte. " + e.getMessage());
		}finally{
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}			
		}
	}

}
