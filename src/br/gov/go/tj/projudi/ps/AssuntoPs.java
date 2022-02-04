package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AssuntoPs extends AssuntoPsGen {

	public AssuntoPs(Connection conexao){
		Conexao = conexao;
	}

	private static final long serialVersionUID = 7181312361131975457L;

	public ProcessoAssuntoDt consultarID(String id_assunto) throws Exception {

		String stSql = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ProcessoAssuntoDt Dados = null;

		stSql = "SELECT * FROM PROJUDI.VIEW_ASSUNTO WHERE ID_ASSUNTO = ?";
		ps.adicionarLong(id_assunto);

		try{
			rs = consultar(stSql, ps);
			if (rs.next()) {
				Dados = new ProcessoAssuntoDt();
				Dados.setId_Assunto(rs.getString("ID_ASSUNTO"));
				Dados.setAssunto(rs.getString("ASSUNTO"));
			}
		
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return Dados;
	}
	
	public String consultarDescricaoAssuntoServentiaJSON(String descricao, String idServentia, String posicao) throws Exception {

		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql = "SELECT DISTINCT ss.ID_ASSUNTO AS ID, ss.ASSUNTO AS DESCRICAO1, ss.ASSUNTO_PAI AS DESCRICAO2, ss.DISPOSITIVO_LEGAL AS DESCRICAO3 FROM projudi.VIEW_SERV_SUBTIPO_ASSUNTO ss";
		Sql += " LEFT JOIN projudi.AREA_DIST a on a.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
		Sql += " LEFT JOIN projudi.SERV_AREA_DIST sad on sad.ID_AREA_DIST = a.ID_AREA_DIST ";
		Sql += " LEFT JOIN projudi.SERV s on s.ID_SERV = sad.ID_SERV ";
		Sql += " WHERE ss.ASSUNTO like ?";
		ps.adicionarString("%"+ descricao +"%");		
		if (idServentia != null && idServentia.length() > 0){
			Sql += " AND s.ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}
		Sql += " ORDER BY ASSUNTO";
		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);
			Sql = "SELECT Count(*) as QUANTIDADE FROM projudi.VIEW_SERV_SUBTIPO_ASSUNTO ss";
			Sql += " LEFT JOIN projudi.AREA_DIST a on a.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
			Sql += " LEFT JOIN projudi.SERV_AREA_DIST sad on sad.ID_AREA_DIST = a.ID_AREA_DIST ";
			Sql += " LEFT JOIN projudi.SERV s on s.ID_SERV = sad.ID_SERV ";
			Sql += " WHERE ss.ASSUNTO like ?";
			if (idServentia != null && idServentia.length() > 0){
				Sql += " AND s.ID_SERV = ?";
			}

			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarDescricaoAssuntoJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql = "SELECT ss.ID_ASSUNTO AS ID, ss.ASSUNTO AS DESCRICAO1, ss.ASSUNTO_PAI AS DESCRICAO2, ss.DISPOSITIVO_LEGAL AS DESCRICAO3";
		SqlFrom = " FROM projudi.VIEW_ASSUNTO ss";
		SqlFrom += " WHERE ss.ASSUNTO like ?";
		ps.adicionarString("%"+ descricao +"%");		
		SqlOrder = " ORDER BY ASSUNTO";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT Count(*) as QUANTIDADE";
			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public List<DescritorComboDt> consultarDescricaoCombo(String descricao, List<String> idsAreasDistribuicoes) throws Exception {
		
		String Sql;
		List<DescritorComboDt> listaTemp = new ArrayList<DescritorComboDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		String valorFiltro = super.obtenhaValorFiltroConsultarDescricaoCombo(descricao);
		String filtroWhereAnd = "WHERE";
		
		if (idsAreasDistribuicoes == null || idsAreasDistribuicoes.size() == 0) {
			Sql = "SELECT a.ID_ASSUNTO, a.ASSUNTO";
			Sql += " FROM ASSUNTO a ";
		} else {
			Sql = "SELECT ss.ID_ASSUNTO, ss.ASSUNTO";
			Sql += " FROM VIEW_SERV_SUBTIPO_ASSUNTO ss ";
			Sql += " LEFT JOIN AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";		
			Sql += filtroWhereAnd + " ( ";
			filtroWhereAnd = "AND";
			for (int i = 0; i < idsAreasDistribuicoes.size(); i++) {
				Sql += " a.ID_AREA_DIST = ?";
				ps.adicionarLong(idsAreasDistribuicoes.get(i));
				
				if (idsAreasDistribuicoes.size() == (i+1)){
					Sql += " ) ";
				} else {
					Sql += " OR ";
				}
			}
		}
		if (valorFiltro != null) {
			Sql += filtroWhereAnd + " ASSUNTO LIKE ? "; 
			ps.adicionarString(valorFiltro);
			filtroWhereAnd = "AND";
		}
		Sql += " ORDER BY ASSUNTO";
		
		try {
			rs1 = consultar(Sql, ps);

			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_ASSUNTO"));
				obTemp.setDescricao(rs1.getString("ASSUNTO"));				
				listaTemp.add(obTemp);
			}			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;		
	}
	
	public String consultarDescricaoJSON(String descricao, String codigoCNJ, String posicao ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_ASSUNTO as id, ASSUNTO as descricao1, ASSUNTO_CODIGO as descricao2 ";
		stSqlFrom= " FROM PROJUDI.VIEW_assunto WHERE ASSUNTO LIKE ? ";
		ps.adicionarString("%"+descricao+"%");
		if (Funcoes.StringToInt(codigoCNJ) > 0) {
			stSqlFrom += " AND ASSUNTO_CODIGO = ? ";
			ps.adicionarLong(codigoCNJ); 
		}
		stSqlOrder= " ORDER BY ASSUNTO ";
		
		try {
			rs1 = consultarPaginacao(stSql+stSqlFrom+stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql+stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
		return stTemp; 
	}
	
	public String consultarDescricaoArtigoJSON(String descricao, String codigoCNJ, String posicao ) throws Exception {

		String stSql, stSqlFrom, stSqlOrder;
		String stTemp="";
		int qtdeColunas = 3;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT ID_ASSUNTO as id, ASSUNTO as descricao1, DISPOSITIVO_LEGAL as descricao2, ARTIGO  as descricao3";
		stSqlFrom= " FROM PROJUDI.VIEW_assunto WHERE ASSUNTO LIKE ? ";							ps.adicionarString("%"+descricao+"%"); 
		if (Funcoes.StringToInt(codigoCNJ) > 0) {
			stSqlFrom += " OR ASSUNTO_CODIGO = ? ";												ps.adicionarLong(codigoCNJ); 
		}
		stSqlOrder= " ORDER BY ASSUNTO ";
		
		try {
			rs1 = consultarPaginacao(stSql+stSqlFrom+stSqlOrder, ps, posicao);
			stSql= "SELECT Count(*) as Quantidade";
			rs2 = consultar(stSql+stSqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
			try {if (rs2 != null) rs2.close();} catch (Exception e) {}
		}
		return stTemp; 
	}
}
