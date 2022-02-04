package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;

import java.sql.Connection;

import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PendenciaStatusPs extends PendenciaStatusPsGen {

	private static final long serialVersionUID = -650003050468166748L;

	@SuppressWarnings("unused")
	private PendenciaStatusPs() {
	}

	public PendenciaStatusPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Método que retorna o registro de PendenciaStatusDt equivalente ao código
	 * passado
	 */
	public PendenciaStatusDt consultarPendenciaStatusCodigo(int pendenciaStatusCodigo) throws Exception {

		PendenciaStatusDt Dados = new PendenciaStatusDt();
		ResultSetTJGO rs1 = null;
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try{

			sql = "SELECT * FROM PROJUDI.VIEW_PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?";
			ps.adicionarLong(pendenciaStatusCodigo);

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_PEND_STATUS"));
				Dados.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				Dados.setPendenciaStatusCodigo(rs1.getString("PEND_STATUS_CODIGO"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			// rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return Dados;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {

		String stSql = "";
		String stSqlFrom = "";
		String stSqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql = "SELECT ID_PEND_STATUS AS ID, PEND_STATUS AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_PEND_STATUS";
		stSqlFrom += " WHERE PEND_STATUS LIKE ?";
		stSqlOrder = " ORDER BY PEND_STATUS ";
		ps.adicionarString("%"+ descricao +"%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
}
