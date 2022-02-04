package br.gov.go.tj.projudi.ps;

import br.gov.go.tj.projudi.dt.RegiaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RegiaoPs extends RegiaoPsGen {

	public RegiaoPs(Connection conexao){
		Conexao = conexao;
	}

	private static final long serialVersionUID = -6677249465070982375L;

	public String consultarDescricaoJSON(String descricao, String comarca, String posicao) throws Exception {

		String stSql = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		stSql = "SELECT ID_REGIAO AS ID, REGIAO AS DESCRICAO1, COMARCA AS DESCRICAO2 FROM PROJUDI.VIEW_REGIAO WHERE REGIAO LIKE ?";
		ps.adicionarString( descricao +"%");
		if(comarca != null && !comarca.equalsIgnoreCase("")) {
			stSql += " AND COMARCA LIKE ?";
			ps.adicionarString( comarca +"%");
		}
		stSql += " ORDER BY REGIAO ";

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_REGIAO WHERE REGIAO LIKE ?";
			if(comarca != null && !comarca.equalsIgnoreCase("")) {
				stSql += " AND COMARCA LIKE ?";
			}
			rs2 = consultar(stSql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
		
	}
	
	/**
	 * Consulta região pelo nome exato da comarca.
	 * @param regiao - nome da região
	 * @param comarca - nome da comarca
	 * @param posicao - posição da consulta
	 * @return lista de regiões consultadas
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarDescricaoPorComarcaJSON(String regiao, String idcomarca, String posicao) throws Exception {

		String stSql = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		stSql = "SELECT ID_REGIAO AS ID, REGIAO AS DESCRICAO1, COMARCA AS DESCRICAO2 FROM PROJUDI.VIEW_REGIAO WHERE REGIAO LIKE ?";
		ps.adicionarString( regiao +"%");
		if(idcomarca != null && !idcomarca.equalsIgnoreCase("")) {
			stSql += " AND ID_COMARCA = ?";
			ps.adicionarLong(idcomarca);
		}
		stSql += " ORDER BY REGIAO ";

		try {
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_REGIAO WHERE REGIAO LIKE ?";
			if(idcomarca != null && !idcomarca.equalsIgnoreCase("")) {
				stSql += " AND ID_COMARCA = ?";
			}
			rs2 = consultar(stSql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
		return stTemp;
		
	}
	
	public RegiaoDt consultarCodigo(String codigoRegiao)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RegiaoDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_REGIAO WHERE REGIAO_CODIGO = ?";		ps.adicionarLong(codigoRegiao); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new RegiaoDt();
				associarDt(Dados, rs1);
			}			
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	/**
	 * Recebe o id da comarca e retorna o id da região de menor código dela.
	 * Utilizado para preencher automaticamente o campo de região no caso das
	 * escalas que forem de plantão pois, nestes casos, este campo será preenchido
	 * apenas para evitar problema com a restrição do banco de dados.
	 * @param idComarca
	 * @return idRegiao
	 * @throws Exception
	 */
	public String consultarIdPrimeraRegiaoComarca(String idComarca) throws Exception {
		String stSql = "";
		String idRegiao = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT ID_REGIAO FROM PROJUDI.REGIAO WHERE ID_COMARCA = ? ORDER BY REGIAO_CODIGO ASC";
		ps.adicionarLong(idComarca);
		
		try {
			rs1 = consultar(stSql, ps);
			if(rs1.next()) {
				idRegiao = rs1.getString("ID_REGIAO");
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return idRegiao;
	}
}
