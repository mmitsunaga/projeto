package br.gov.go.tj.projudi.ps;

import java.util.Date;

import br.gov.go.tj.projudi.dt.UfrValorDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class UfrValorPs extends Persistencia {

	private static final long serialVersionUID = -2796556948617001825L;

	public UfrValorPs(Connection conexao){
		Conexao = conexao;
	}

//	/**
//	 * Método para consultar o ultimo valor da UFR da taxa judiciária. 
//	 * @return Double
//	 * @throws Exception
//	 */
//	public Double obterUltimoValorUFRTaxaJudiciaria() throws Exception {
//		String Sql;
//		ResultSetTJGO rs1 = null;
//		Double retorno = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//
//		Sql= "SELECT VALOR_TAXA_JUDICIARIA FROM projudi.UFR_VALOR WHERE ID_UFR_VALOR = (SELECT MAX(ID_UFR_VALOR) FROM projudi.UFR_VALOR)";
//
//		try{
//			rs1 = consultar(Sql, ps);
//			if( rs1 != null ) {
//				if (rs1.next()) {
//					retorno = rs1.getDouble("VALOR_TAXA_JUDICIARIA");
//				}
//			}
//		} finally {
//			 rs1.close();
//		}
//		
//		return retorno;
//	}
//	
//	/**
//	 * Método para consultar o ultimo valor da UFR. 
//	 * @return Double
//	 * @throws Exception
//	 */
//	public Double obterUltimoValorUFR() throws Exception {
//		String Sql;
//		ResultSetTJGO rs1 = null;
//		Double retorno = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//
//		Sql= "SELECT VALOR FROM projudi.UFR_VALOR WHERE ID_UFR_VALOR = (SELECT MAX(ID_UFR_VALOR) FROM projudi.UFR_VALOR)";
//
//		try{
//			rs1 = consultar(Sql, ps);
//			if( rs1 != null ) {
//				if (rs1.next()) {
//					retorno = rs1.getDouble("VALOR");
//				}
//			}
//		} finally {
//			 rs1.close();
//		}
//		
//		return retorno;
//	}
//	
//	/**
//	 * Método para obter o valor da taxa judiciária pela data de protocolo do processo.
//	 * @param Date
//	 * @return Double
//	 * @throws Exception
//	 */
//	public Double obterValorUFRTaxaJudiciaria(Date date) throws Exception {
//		String Sql;
//		ResultSetTJGO rs1 = null;
//		Double retorno = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//
//		Sql= "SELECT VALOR_TAXA_JUDICIARIA FROM projudi.UFR_VALOR WHERE MES_INICIO <= ? AND MES_FINAL >= ?";
//		ps.adicionarDate(date);
//		ps.adicionarDate(date);
//		
//		try{
//			rs1 = consultar(Sql, ps);
//			if( rs1 != null ) {
//				if (rs1.next()) {
//					retorno = rs1.getDouble("VALOR_TAXA_JUDICIARIA");
//				}
//			}
//		} finally {
//			 rs1.close();
//		}
//		
//		return retorno;
//	}
	
	public UfrValorDt consultarId(String id_UFR_Valor)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UfrValorDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_UFR_VALOR WHERE ID_UFR_VALOR = ?";		ps.adicionarLong(id_UFR_Valor); 

		try {
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UfrValorDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	public UfrValorDt consultarData(Date date)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		UfrValorDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_UFR_VALOR WHERE MES_INICIO <= ? AND MES_FINAL >= ?";
		ps.adicionarDate(date);
		ps.adicionarDate(date);

		try {
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new UfrValorDt();
				associarDt(Dados, rs1);
			} else {
				stSql= "SELECT * FROM PROJUDI.VIEW_UFR_VALOR WHERE MES_INICIO = (SELECT MAX(MES_INICIO) FROM PROJUDI.VIEW_UFR_VALOR WHERE MES_FINAL < ?)";
				ps.limpar();
				ps.adicionarDate(date);	
				
				rs1 = consultar(stSql,ps);
				if (rs1.next()) {
					Dados= new UfrValorDt();
					associarDt(Dados, rs1);
				}	
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	protected void associarDt(UfrValorDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_UFR_VALOR"));
		Dados.setValorUFR(Funcoes.FormatarDecimal(rs.getString("VALOR")));
		Dados.setValorTaxaJudiciaria(Funcoes.FormatarDecimal(rs.getString("VALOR_TAXA_JUDICIARIA")));
		Dados.setDataInicio(Funcoes.FormatarDataHora(rs.getDateTime("MES_INICIO")));
		Dados.setDataFinal(Funcoes.FormatarDataHora(rs.getDateTime("MES_FINAL")));
		Dados.setDataAtualizacao(Funcoes.FormatarDataHora(rs.getDateTime("DATA_ATUALIZACAO")));		
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
	}
}
