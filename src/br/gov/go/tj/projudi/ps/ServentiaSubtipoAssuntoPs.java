package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoAssuntoDt;
import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ServentiaSubtipoAssuntoPs extends ServentiaSubtipoAssuntoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -1471873766053693376L;


    public ServentiaSubtipoAssuntoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consulta os assuntos disponíveis a partir da área de distribuição, chegando assim ao ServentiaSubTipo em questão.
	 * 
	 * @param id_AreaDistribuicao, area de distribuição da serventia em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 * 
	 * @author msapaula
	 */
	public List consultarAssuntosAreaDistribuicao(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ss.ID_ASSUNTO, ss.ASSUNTO, ss.DISPOSITIVO_LEGAL, ss.ASSUNTO_PAI, ss.ASSUNTO_CODIGO" ;
		SqlFrom = " FROM VIEW_SERV_SUBTIPO_ASSUNTO ss";
		SqlFrom += " LEFT JOIN AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		SqlFrom += " WHERE SS.IS_ATIVO <> '0' AND ss.ASSUNTO like ?";
		ps.adicionarString( descricao +"%");		
		if (id_AreaDistribuicao.length() > 0){
			SqlFrom += " AND a.ID_AREA_DIST = ?";
			ps.adicionarLong(id_AreaDistribuicao);
		}
		SqlOrder = " ORDER BY ASSUNTO";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				AssuntoDt obTemp = new AssuntoDt();
				obTemp.setId(rs1.getString("ID_ASSUNTO"));
				obTemp.setAssunto(rs1.getString("ASSUNTO"));
				obTemp.setDispositivoLegal(rs1.getString("DISPOSITIVO_LEGAL"));
				obTemp.setAssuntoPai(rs1.getString("ASSUNTO_PAI"));
				obTemp.setAssuntoCodigo(rs1.getString("ASSUNTO_CODIGO"));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) AS QUANTIDADE";

			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Consulta os assuntos disponíveis a partir da área de distribuição, chegando assim ao ServentiaSubTipo em questão.
	 * 
	 * @param id_AreaDistribuicao, area de distribuição da serventia em questão
	 * @param descricao, filtro de descrição
	 * @param posicao, parametro usado na paginação
	 * 
	 * @author msapaula
	 */
	public List consultarAssuntosAreasDistribuicoes(List areasDistribuicoes, String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ss.ID_ASSUNTO, ss.ASSUNTO, ss.DISPOSITIVO_LEGAL, ss.ASSUNTO_PAI";
		SqlFrom = " FROM projudi.VIEW_SERV_SUBTIPO_ASSUNTO ss";
		SqlFrom += " LEFT JOIN projudi.AREA_DIST a on a.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
		SqlFrom += " WHERE ss.ASSUNTO like ?";
		ps.adicionarString( descricao +"%");		
		if (areasDistribuicoes != null && areasDistribuicoes.size() > 0){
			SqlFrom += " AND ( ";
			for (int i = 0; i < areasDistribuicoes.size(); i++) {
				AreaDistribuicaoDt areaDistribuicaoDt = (AreaDistribuicaoDt) areasDistribuicoes.get(i);
				SqlFrom += " a.ID_AREA_DIST = ?";
				ps.adicionarLong(areaDistribuicaoDt.getId());
				
				if (areasDistribuicoes.size() == (i+1)){
					SqlFrom += " ) ";
				} else {
					SqlFrom += " OR ";
				}
			}
		}
		SqlOrder = " ORDER BY ASSUNTO";
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				AssuntoDt obTemp = new AssuntoDt();
				obTemp.setId(rs1.getString("ID_ASSUNTO"));
				obTemp.setAssunto(rs1.getString("ASSUNTO"));
				obTemp.setDispositivoLegal(rs1.getString("DISPOSITIVO_LEGAL"));
				obTemp.setAssuntoPai(rs1.getString("ASSUNTO_PAI"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT Count(*) as QUANTIDADE";

			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	

    /*
     Este método foi criado com o intuito de retornar uma lista ordenada pelo atributo Assunto     
	 @Autor Márcio Gomes
	 @Data 27/08/2010
    */
	public List consultarAssuntoServentiaSubtipoGeral(String id_serventiasubtipo ) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT t2.ID_SERV_SUBTIPO_ASSUNTO, t1.ID_ASSUNTO, t1.ASSUNTO, t3.ID_SERV_SUBTIPO, t3.SERV_SUBTIPO, pai.ASSUNTO AS ASSUNTO_PAI, t1.DISPOSITIVO_LEGAL, t1.artigo, t1.ASSUNTO_CODIGO";
		Sql+= " FROM PROJUDI.ASSUNTO t1 ";
		Sql+= " LEFT JOIN PROJUDI.SERV_SUBTIPO_ASSUNTO t2 ON t1.ID_ASSUNTO = t2.ID_ASSUNTO AND t2.ID_SERV_SUBTIPO = ?";
		ps.adicionarLong(id_serventiasubtipo);
		Sql+= " LEFT JOIN PROJUDI.SERV_SUBTIPO t3 ON t3.ID_SERV_SUBTIPO = t2.ID_SERV_SUBTIPO";
		Sql+= " LEFT JOIN PROJUDI.ASSUNTO pai ON t1.ID_ASSUNTO_PAI = pai.ID_ASSUNTO";
		Sql+= " where t1.is_ativo <> '0' ";
		Sql+= " ORDER BY t1.ASSUNTO";
		try{

			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				ServentiaSubtipoAssuntoDt obTemp = new ServentiaSubtipoAssuntoDt();
				obTemp.setId(rs1.getString("ID_SERV_SUBTIPO_ASSUNTO"));
				obTemp.setServentiaSubtipo(rs1.getString("SERV_SUBTIPO"));
				obTemp.setId_Assunto (rs1.getString("ID_ASSUNTO"));
				obTemp.setAssunto(rs1.getString("ASSUNTO"));
				obTemp.setId_ServentiaSubtipo (id_serventiasubtipo);
				obTemp.setServentiaSubtipo(rs1.getString("SERV_SUBTIPO"));
				obTemp.setAssuntoPai(rs1.getString("ASSUNTO_PAI"));
				obTemp.setDispositivo_Legal(rs1.getString("DISPOSITIVO_LEGAL") + ' ' +  rs1.getString("ARTIGO"));
				obTemp.setAssuntoCodigo(rs1.getString("ASSUNTO_CODIGO"));
				liTemp.add(obTemp);
			}			
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp; 
	}
	
	public String consultarAssuntosAreasDistribuicoesJSON(List areasDistribuicoes, String descricao, String codigoCNJ, String posicao) throws Exception {

		String Sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT DISTINCT ss.ID_ASSUNTO AS ID, ss.ASSUNTO AS DESCRICAO1, ss.DISPOSITIVO_LEGAL || ' ' || SS.ARTIGO AS DESCRICAO3, ss.ASSUNTO_PAI AS DESCRICAO2, ss.ASSUNTO_CODIGO AS DESCRICAO4 ";
		sqlFrom = " FROM projudi.VIEW_SERV_SUBTIPO_ASSUNTO ss LEFT JOIN projudi.AREA_DIST a on a.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
		sqlFrom += " WHERE SS.IS_ATIVO <> '0' AND ss.ASSUNTO like ?";										ps.adicionarString("%" + descricao +"%");
		if (Funcoes.StringToLong(codigoCNJ) > 0) {
			sqlFrom += " AND ss.ASSUNTO_CODIGO = ? ";										ps.adicionarLong(codigoCNJ);
		}
		if (areasDistribuicoes != null && areasDistribuicoes.size() > 0){
			sqlFrom += " AND ( ";
			for (int i = 0; i < areasDistribuicoes.size(); i++) {
				AreaDistribuicaoDt areaDistribuicaoDt = (AreaDistribuicaoDt) areasDistribuicoes.get(i);
				sqlFrom += " a.ID_AREA_DIST = ?";									ps.adicionarLong(areaDistribuicaoDt.getId());
				
				if (areasDistribuicoes.size() == (i+1)){
					sqlFrom += " ) ";
				} else {
					sqlFrom += " OR ";
				}
			}
		}
		sqlOrder = " ORDER BY ASSUNTO";
		try{
			rs1 = consultarPaginacao(Sql + sqlFrom + sqlOrder, ps, posicao);
			Sql = "SELECT Count(*) as QUANTIDADE ";

			rs2 = consultar(Sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarAssuntosAreaDistribuicaoJSON(String id_AreaDistribuicao, String descricao, String codigoCNJ, String posicao) throws Exception {

		String Sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT ss.ID_ASSUNTO AS ID, ss.ASSUNTO AS DESCRICAO1, ss.DISPOSITIVO_LEGAL || ' ' || SS.ARTIGO AS DESCRICAO3, ss.ASSUNTO_PAI AS DESCRICAO2, ss.ASSUNTO_CODIGO AS DESCRICAO4 ";
		sqlFrom = " FROM VIEW_SERV_SUBTIPO_ASSUNTO ss LEFT JOIN AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		sqlFrom += " WHERE  SS.IS_ATIVO <> '0' AND ss.ASSUNTO like ?";										ps.adicionarString("%" + descricao +"%");		
		if (id_AreaDistribuicao.length() > 0){
			sqlFrom += " AND a.ID_AREA_DIST = ? ";									ps.adicionarLong(id_AreaDistribuicao);
		}
		if (Funcoes.StringToLong(codigoCNJ) > 0) {
			sqlFrom += " AND ss.ASSUNTO_CODIGO = ? ";                               ps.adicionarLong(codigoCNJ);
		}
		sqlOrder = " ORDER BY ASSUNTO";
		try{
			rs1 = consultarPaginacao(Sql + sqlFrom + sqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE ";

			rs2 = consultar(Sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarAssuntosServentiaJSON(String descricao, String idServentia, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql = "SELECT DISTINCT ss.ID_ASSUNTO AS ID, ss.ASSUNTO AS DESCRICAO1, ss.ASSUNTO_PAI AS DESCRICAO2, ss.DISPOSITIVO_LEGAL || ' ' || SS.ARTIGO AS DESCRICAO3";
		SqlFrom = " FROM projudi.VIEW_SERV_SUBTIPO_ASSUNTO ss";
		SqlFrom += " INNER JOIN projudi.SERV s on s.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
		SqlFrom += " WHERE SS.IS_ATIVO <> '0' AND ss.ASSUNTO like ?";
		ps.adicionarString("%" + descricao + "%");		
		if (idServentia != null && idServentia.length() > 0){
			SqlFrom += " AND s.ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}
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
	
	public String consultarAssuntosAreaDistribuicaoPjdJSON(String id_AreaDistribuicao, String descricao, String posicao) throws Exception {

		String Sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT ss.ID_ASSUNTO AS ID, ss.ASSUNTO AS DESCRICAO1, ss.DISPOSITIVO_LEGAL  || ' ' || SS.ARTIGO AS DESCRICAO3, ss.ASSUNTO_PAI AS DESCRICAO2, ss.ASSUNTO_CODIGO AS DESCRICAO4 ";
		sqlFrom = " FROM VIEW_SERV_SUBTIPO_ASSUNTO ss LEFT JOIN AREA_DIST a on a.ID_SERV_SUBTIPO=ss.ID_SERV_SUBTIPO ";
		sqlFrom += " WHERE  SS.IS_ATIVO <> '0' AND (ss.ASSUNTO like ?";										ps.adicionarString("%" + descricao +"%");
		sqlFrom += " or ss.ASSUNTO_CODIGO = ? )";                               ps.adicionarLong(descricao);
		if (id_AreaDistribuicao.length() > 0){
			sqlFrom += " AND a.ID_AREA_DIST = ? ";									ps.adicionarLong(id_AreaDistribuicao);
		}		
		sqlOrder = " ORDER BY ASSUNTO";
		try{
			rs1 = consultarPaginacao(Sql + sqlFrom + sqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE ";

			rs2 = consultar(Sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarAssuntosAreasDistribuicoesPjdJSON(List areasDistribuicoes, String descricao, String posicao) throws Exception {

		String Sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT DISTINCT ss.ID_ASSUNTO AS ID, ss.ASSUNTO AS DESCRICAO1, ss.DISPOSITIVO_LEGAL || ' ' || SS.ARTIGO AS DESCRICAO3, ss.ASSUNTO_PAI AS DESCRICAO2, ss.ASSUNTO_CODIGO AS DESCRICAO4 ";
		sqlFrom = " FROM projudi.VIEW_SERV_SUBTIPO_ASSUNTO ss LEFT JOIN projudi.AREA_DIST a on a.ID_SERV_SUBTIPO = ss.ID_SERV_SUBTIPO ";
		sqlFrom += " WHERE  SS.IS_ATIVO <> '0' AND (ss.ASSUNTO like ?";										ps.adicionarString("%" + descricao +"%");
		sqlFrom += " or ss.ASSUNTO_CODIGO = ?) ";										ps.adicionarLong(descricao);		
		if (areasDistribuicoes != null && areasDistribuicoes.size() > 0){
			sqlFrom += " AND ( ";
			for (int i = 0; i < areasDistribuicoes.size(); i++) {
				AreaDistribuicaoDt areaDistribuicaoDt = (AreaDistribuicaoDt) areasDistribuicoes.get(i);
				sqlFrom += " a.ID_AREA_DIST = ?";									ps.adicionarLong(areaDistribuicaoDt.getId());
				
				if (areasDistribuicoes.size() == (i+1)){
					sqlFrom += " ) ";
				} else {
					sqlFrom += " OR ";
				}
			}
		}
		sqlOrder = " ORDER BY ASSUNTO";
		try{
			rs1 = consultarPaginacao(Sql + sqlFrom + sqlOrder, ps, posicao);
			Sql = "SELECT Count(*) as QUANTIDADE ";

			rs2 = consultar(Sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}

}
