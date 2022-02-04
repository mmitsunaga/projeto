package br.gov.go.tj.projudi.ps;

import java.sql.Connection;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class CargoTipoPs extends CargoTipoPsGen {

	private static final long serialVersionUID = 1562696715729532561L;

	public CargoTipoPs(Connection conexao){
		Conexao = conexao;
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

		stSql = "SELECT ID_CARGO_TIPO AS ID, CARGO_TIPO AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_CARGO_TIPO";
		stSqlFrom += " WHERE CARGO_TIPO LIKE ?";
		stSqlOrder = " ORDER BY CARGO_TIPO ";
		ps.adicionarString("%"+ descricao +"%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		
		} finally{
			try{
				if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Método que retonar o ID do Cargo Tipo consultando através do código do mesmo.
	 * @param cargoTipoCodigo - código do cargo tipo
	 * @return ID do cargo tipo
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarIdCargoTipoCodigo(int cargoTipoCodigo) throws Exception {
		String stSql = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_CARGO_TIPO AS ID FROM PROJUDI.CARGO_TIPO WHERE CARGO_TIPO_CODIGO = ?";
		ps.adicionarLong(cargoTipoCodigo);

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				return rs1.getString("ID");
			}
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}
		return stTemp;
	}
	
	public CargoTipoDt consultarCargoTipoCodigo(String cargoTipoCodigo) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		CargoTipoDt Dados = null;

		stSql = "SELECT * ";
		stSql += "FROM PROJUDI.VIEW_CARGO_TIPO ";
		stSql += "WHERE CARGO_TIPO_CODIGO = ? ";
		ps.adicionarLong(cargoTipoCodigo); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new CargoTipoDt();
				associarDt(Dados, rs1);
			}
		}
		finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {}
		}
		return Dados; 
	}
}