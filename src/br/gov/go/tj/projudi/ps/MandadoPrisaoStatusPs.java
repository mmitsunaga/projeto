package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class MandadoPrisaoStatusPs extends MandadoPrisaoStatusPsGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1110765718946628362L;

	@SuppressWarnings("unused")
	private MandadoPrisaoStatusPs( ) {}
	
	public MandadoPrisaoStatusPs(Connection conexao){
		Conexao = conexao;
	}

	public List consultarDescricao(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		List liTemp = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT *";
		stSqlFrom = " FROM PROJUDI.VIEW_MANDADO_PRISAO_STATUS";
		stSqlFrom += " WHERE MANDADO_PRISAO_STATUS LIKE ?";
		stSqlOrder = " ORDER BY MANDADO_PRISAO_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			while (rs1.next()) {
				MandadoPrisaoStatusDt obTemp = new MandadoPrisaoStatusDt();
				associarDt(obTemp, rs1);
				liTemp.add(obTemp);
			}
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql + stSqlFrom, ps);

			while (rs2.next()) {
				liTemp.add(rs2.getLong("Quantidade"));
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp; 
	}

}
