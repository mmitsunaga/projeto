package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ArrecadacaoCustaPs extends ArrecadacaoCustaPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3408976855283017101L;

	public ArrecadacaoCustaPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Consultar por Descrição.
	 * @param String Descricao
	 * @param String posicao
	 * @return List
	 * @throws Exception
	 */
	public List consultarPorDescricao(String descricao, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT ID_ARRECADACAO_CUSTA, ARRECADACAO_CUSTA, CODIGO_ARRECADACAO";
		SqlFrom = " FROM projudi.ARRECADACAO_CUSTA ";
		SqlFrom += " WHERE ARRECADACAO_CUSTA LIKE  ?";
		ps.adicionarString(descricao+"%");
		SqlOrder = " ORDER BY CODIGO_ARRECADACAO, ARRECADACAO_CUSTA ";
		try{

			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs.next()) {
				ArrecadacaoCustaDt obTemp = new ArrecadacaoCustaDt();
				obTemp.setId(rs.getString("ID_ARRECADACAO_CUSTA"));
				obTemp.setArrecadacaoCusta(rs.getString("ARRECADACAO_CUSTA"));
				obTemp.setCodigoArrecadacao(rs.getString("CODIGO_ARRECADACAO"));
				liTemp.add(obTemp);
			}
			Sql= "SELECT COUNT(*) as QUANTIDADE";
			
			rs = consultar(Sql + SqlFrom, ps);
			if (rs.next()) liTemp.add(rs.getLong("QUANTIDADE"));
			
		} finally {
			if (rs != null) rs.close();
		}
		return liTemp; 
	}

	/**
	 * Método para consultar a lista de Arrecadacao Custa.
	 * @param String descricao
	 * @return List ArrecadacaoCustaDt
	 * @throws Exception
	 */
	public List consultarPorDescricao(String descricao) throws Exception {
		String Sql;
		List liTemp = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT ID_ARRECADACAO_CUSTA, ARRECADACAO_CUSTA, CODIGO_ARRECADACAO FROM projudi.ARRECADACAO_CUSTA " +
				"WHERE CODIGO_ARRECADACAO <> 0 AND ARRECADACAO_CUSTA LIKE  ?";
		if( descricao == null ) {
			descricao = "";
			ps.adicionarString(descricao+"%");
		}
		else {
			ps.adicionarString(descricao+"%");
		}
		
		Sql+= " ORDER BY CODIGO_ARRECADACAO, ARRECADACAO_CUSTA ";
		
		try{

			rs = consultar(Sql, ps);
			
			if( rs != null ) {
				liTemp = new ArrayList();

				while (rs.next()) {
					ArrecadacaoCustaDt obTemp = new ArrecadacaoCustaDt();
					obTemp.setId(rs.getString("ID_ARRECADACAO_CUSTA"));
					obTemp.setArrecadacaoCusta(rs.getString("ARRECADACAO_CUSTA"));
					obTemp.setCodigoArrecadacao(rs.getString("CODIGO_ARRECADACAO"));
					liTemp.add(obTemp);
				}
			}
			
		} finally {
			if (rs != null) rs.close();
		}
		return liTemp; 
	}
	
	public String consultarPorDescricaoJSON(String descricao, String posicao ) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		Sql= "SELECT ID_ARRECADACAO_CUSTA AS ID, ARRECADACAO_CUSTA AS DESCRICAO1";
		SqlFrom = " FROM projudi.ARRECADACAO_CUSTA";
		SqlFrom += " WHERE ARRECADACAO_CUSTA LIKE ?";
		ps.adicionarString(descricao+"%");
		SqlOrder = " ORDER BY CODIGO_ARRECADACAO, ARRECADACAO_CUSTA ";
		try{

			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			Sql= "SELECT COUNT(*) as QUANTIDADE";
			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		} finally {
			if (rs1 != null) rs1.close();
			if (rs2 != null) rs2.close();
		}
		return stTemp; 
	}
}
