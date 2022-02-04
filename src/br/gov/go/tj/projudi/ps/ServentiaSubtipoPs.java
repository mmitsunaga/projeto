package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ServentiaSubtipoPs extends ServentiaSubtipoPsGen {

	private static final long serialVersionUID = -1011130474141244125L;

	public ServentiaSubtipoPs(Connection conexao){
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

		stSql = "SELECT ID_SERV_SUBTIPO AS ID, SERV_SUBTIPO AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_SERV_SUBTIPO";
		stSqlFrom += " WHERE SERV_SUBTIPO LIKE ?";
		stSqlOrder = " ORDER BY SERV_SUBTIPO ";
		ps.adicionarString( descricao +"%");

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close(); }catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Método que realiza a consulta de serventia subtipo
	 * 
	 * @param id_AreaDistribuicao
	 *            , id da Area Distribuicao
	 * @return String
	 *            , id da Serventia SubTipo
	 * 
	 * @author lsbernardes
	 */	
	public String consultarServentiaSubTipoAreaDistribuicao(String id_AreaDistribuicao )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String  id_ServentiaSubTipo = null;

		stSql= "SELECT ad.ID_SERV_SUBTIPO FROM PROJUDI.AREA_DIST ad WHERE ad.ID_AREA_DIST = ?";		ps.adicionarLong(id_AreaDistribuicao); 


		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				id_ServentiaSubTipo = rs1.getString("ID_SERV_SUBTIPO");
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return id_ServentiaSubTipo; 
	}
	
	/**
	 * Método que realiza a consulta de serventia subtipo
	 * 
	 * @param id_Serventia
	 *            , id da Serventia
	 * @return String
	 *            , id da Serventia SubTipo
	 * 
	 * @author lsbernardes
	 */	
	public String consultarServentiaSubTipoServentia(String id_Serventia )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String  id_ServentiaSubTipo = null;

		stSql= "SELECT s.ID_SERV_SUBTIPO FROM PROJUDI.SERV s WHERE s.ID_SERV = ?";		ps.adicionarLong(id_Serventia); 


		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				id_ServentiaSubTipo = rs1.getString("ID_SERV_SUBTIPO");
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return id_ServentiaSubTipo; 
	}
	
	/**
	 * 
	 * @param descricao
	 * @return
	 * @throws Exception
	 */
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		
		Sql = "SELECT ID_SERV_SUBTIPO, SERV_SUBTIPO FROM PROJUDI.VIEW_SERV_SUBTIPO";
		if (valorFiltro != null) {
			Sql += " WHERE SERV_SUBTIPO LIKE ?"; 
			ps.adicionarString(valorFiltro);	
		}		 
		Sql += " ORDER BY SERV_SUBTIPO";		
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_SERV_SUBTIPO"));
				obTemp.setDescricao(rs1.getString("ID_SERV_SUBTIPO") + " - " + rs1.getString("SERV_SUBTIPO"));				
				listaTemp.add(obTemp);
			}			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;
	}
	
	/**
	 * Método que realiza a consulta de serventia subtipo da serventia origem de um recurso
	 * 
	 * @param id_Reurso
	 *            , id do recurso
	  * @return String
	 *            , codigo do subtipo da serventia 
	 * 
	 * @author lsbernardes
	 */	
	public String consultarServentiaSubTipoServentiaOrigemRecurso(String id_Recurso )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String  serventiaSubTipoCodigo = null;

		stSql= "SELECT ss.SERV_SUBTIPO_CODIGO ";		
		stSql += " FROM ";
		stSql += " PROJUDI.RECURSO r "; 
		stSql += " INNER JOIN PROJUDI.SERV s on s.ID_SERV = r.ID_SERV_ORIGEM ";
		stSql += " INNER JOIN PROJUDI.SERV_SUBTIPO ss on s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
		stSql += " WHERE "; 
		stSql += "	r.ID_RECURSO = ? ";	ps.adicionarLong(id_Recurso); 


		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				serventiaSubTipoCodigo = rs1.getString("SERV_SUBTIPO_CODIGO");
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return serventiaSubTipoCodigo; 
	}
	
}
