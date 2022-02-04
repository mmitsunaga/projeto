package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ClassificadorDt;
import java.sql.Connection;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ClassificadorPs extends ClassificadorPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 3280904621375696529L;

    public ClassificadorPs(Connection conexao){
    	Conexao = conexao;
	}

	@SuppressWarnings("unchecked")
	public List consultarDescricao(String descricao, String posicao) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		List listaClassificadores = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT ID_CLASSIFICADOR,CLASSIFICADOR, ID_SERV, SERV, PRIORI ";
		sqlFrom = " FROM PROJUDI.VIEW_CLASSIFICADOR";
		sqlFrom += " WHERE CLASSIFICADOR LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlOrder = " ORDER BY SERV, CLASSIFICADOR";
		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			while (rs1.next()) {
				ClassificadorDt classificadorDt = new ClassificadorDt();
				classificadorDt.setId(rs1.getString("ID_CLASSIFICADOR"));
				classificadorDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				classificadorDt.setId_Serventia(rs1.getString("ID_SERV"));
				classificadorDt.setServentia(rs1.getString("SERV"));
				classificadorDt.setPrioridade(rs1.getString("PRIORI"));
				listaClassificadores.add(classificadorDt);
			}
			rs1.close();
			sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(sql + sqlFrom, ps);
			
			if (rs2.next())	listaClassificadores.add(rs2.getLong("QUANTIDADE"));
			
			rs1.close();
		
		} finally{
			 rs1.close();
			 rs2.close();
		}
		return listaClassificadores;
	}

	/**
	 * Efetua consulta por classificadores de uma serventia,
	 * com suporte a paginação
	 */
	public List consultarClassificadorServentia(String descricao, String posicao, String id_Serventia) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_CLASSIFICADOR,CLASSIFICADOR,SERV,PRIORI";
		SqlFrom = " FROM PROJUDI.VIEW_CLASSIFICADOR";
		SqlFrom += " WHERE CLASSIFICADOR LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND ID_SERV = ?";
		ps.adicionarLong(id_Serventia);
		SqlOrder = " ORDER BY PRIORI, CLASSIFICADOR";
		
		try{
			rs = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs.next()) {
				ClassificadorDt obTemp = new ClassificadorDt();
				obTemp.setId(rs.getString("ID_CLASSIFICADOR"));
				obTemp.setClassificador(rs.getString("CLASSIFICADOR"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setPrioridade(rs.getString("PRIORI"));
				liTemp.add(obTemp);
			}
			//rs.close();

			Sql = "SELECT COUNT(*) AS QUANTIDADE";			
			rs2 = consultar(Sql + SqlFrom,ps);
			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs.close();
		
		} finally{
			rs.close();
			rs2.close();					
		}
		return liTemp;
	}

	/**
	 * Método que consulta o classificador de um processo
	 * 
	 * @param id_Processo, identificação do processo
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */

	public ClassificadorDt consultarClassificadorProcesso(String id_Processo) throws Exception {
		String Sql;
		ClassificadorDt classificadorDt = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT p.ID_CLASSIFICADOR, p.CLASSIFICADOR FROM PROJUDI.VIEW_PROC p";
		Sql += " WHERE p.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		try{
			rs = consultar(Sql,ps);
			if (rs.next()) {
				classificadorDt = new ClassificadorDt();
				classificadorDt.setId(rs.getString("ID_CLASSIFICADOR"));
				classificadorDt.setClassificador(rs.getString("CLASSIFICADOR"));
			}
		
		} finally{
			rs.close();
		}
		return classificadorDt;
	}
	
	//Código abaixo inserido pelo jelves 14/07/15
	public String consultarDescricaoJSON(String descricao, String posicao, String idServentia, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenação PROJUDI
			ordenacao = " CLASSIFICADOR ";
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();


		stSql= "SELECT ID_CLASSIFICADOR as id, CLASSIFICADOR as descricao1, PRIORI as descricao2";
		stSqlFrom = " FROM PROJUDI.VIEW_CLASSIFICADOR";
		stSqlFrom += " WHERE CLASSIFICADOR LIKE ?";
		ps.adicionarString("%"+descricao+"%");
		if( idServentia != null ) {
			stSqlFrom += " AND ID_SERV = ?  ";
			ps.adicionarLong(idServentia);
		}
		stSqlOrder += " ORDER BY " + ordenacao;

		try{


			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao, quantidadeRegistros);
			stSql= "SELECT Count(*) as Quantidade";			
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}
	
	public String consultarClassificadorServentiaJSON(String descricao, String posicao, String id_Serventia) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql = "SELECT ID_CLASSIFICADOR AS ID, CLASSIFICADOR AS DESCRICAO1, PRIORI AS DESCRICAO2, SERV AS DESCRICAO3";
		SqlFrom = " FROM PROJUDI.VIEW_CLASSIFICADOR";
		SqlFrom += " WHERE CLASSIFICADOR LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		if (id_Serventia != null && id_Serventia.trim().length() > 0) {
			SqlFrom += " AND ID_SERV = ?";
			ps.adicionarLong(id_Serventia);	
		}		
		SqlOrder = " ORDER BY PRIORI, CLASSIFICADOR";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			Sql = "SELECT COUNT(*) AS QUANTIDADE";			
			rs2 = consultar(Sql + SqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}					
		}
		return stTemp;
	}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		
		Sql = "SELECT * FROM PROJUDI.CLASSIFICADOR";
		if (valorFiltro != null) {
			Sql += " WHERE CLASSIFICADOR LIKE ?"; 
			ps.adicionarString(valorFiltro);	
		}		 
		Sql += " ORDER BY CLASSIFICADOR";		
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_CLASSIFICADOR"));
				obTemp.setDescricao(rs1.getString("CLASSIFICADOR"));				
				listaTemp.add(obTemp);
			}			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;
	}
	
}
