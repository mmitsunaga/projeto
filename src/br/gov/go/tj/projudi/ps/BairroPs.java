package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class BairroPs extends BairroPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -6140917493230767460L;

    public BairroPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consulta de bairros com paginação
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_BAIRRO,BAIRRO,ID_CIDADE,CIDADE,UF,ZONA,REGIAO";
		SqlFrom = " FROM PROJUDI.VIEW_BAIRRO";
		SqlFrom += " WHERE BAIRRO LIKE ?";
		ps.adicionarString("%"+ descricao +"%");		
		SqlOrder = " ORDER BY BAIRRO";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				BairroDt obTemp = new BairroDt();
				obTemp.setId(rs1.getString("ID_BAIRRO"));
				obTemp.setBairro(rs1.getString("BAIRRO"));
				obTemp.setId_Cidade(rs1.getString("ID_CIDADE"));
				obTemp.setCidade(rs1.getString("CIDADE"));
				obTemp.setUf(rs1.getString("UF"));
				obTemp.setZona(rs1.getString("ZONA"));
				obTemp.setRegiao(rs1.getString("REGIAO"));
				liTemp.add(obTemp);
			}
			
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";

			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
        } finally{
        	 rs1.close();
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }  
		return liTemp;
	}

	/**
	 * Consulta de bairros com filtros por cidade e estado
	 * @param descricao, filtro para descrição do bairro
	 * @param cidade, filtro para descrição da cidade
	 * @param uf, filtro para estado
	 * @param posicao, parametro para paginação
	 */
	public List consultarDescricao(String descricao, String cidade, String uf, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_BAIRRO,BAIRRO,ID_CIDADE,CIDADE,UF,ZONA,REGIAO";
		SqlFrom = " FROM PROJUDI.VIEW_BAIRRO ";
		SqlFrom += " WHERE BAIRRO LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		SqlFrom += " AND CIDADE LIKE ?";
		ps.adicionarString( cidade +"%");
		SqlFrom += " AND UF LIKE ?";
		ps.adicionarString( uf +"%");
		SqlOrder = " ORDER BY BAIRRO";
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				BairroDt obTemp = new BairroDt();
				obTemp.setId(rs1.getString("ID_BAIRRO"));
				obTemp.setBairro(rs1.getString("BAIRRO"));
				obTemp.setId_Cidade(rs1.getString("ID_CIDADE"));
				obTemp.setCidade(rs1.getString("CIDADE"));
				obTemp.setUf(rs1.getString("UF"));
				obTemp.setZona(rs1.getString("ZONA"));
				obTemp.setRegiao(rs1.getString("REGIAO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }  
		return liTemp;
	}

	/**
	 * Consulta um bairro de acordo com descrição, cidade e estado passados
	 * @param descricao, filtro para descrição do bairro
	 * @param cidade, filtro para descrição da cidade
	 * @param uf, filtro para estado
	 */
	public BairroDt consultarBairro(String descricao, String cidade, String uf) throws Exception {

		String Sql;
		BairroDt bairroDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_BAIRRO,BAIRRO,ID_CIDADE,CIDADE,UF,CODIGO_SPG,ZONA,REGIAO,CODIGO_CIDADE_SPG FROM PROJUDI.VIEW_BAIRRO ";
		Sql += " WHERE BAIRRO LIKE ?";
		ps.adicionarString(descricao);
		Sql += " AND CIDADE LIKE ?";
		ps.adicionarString(cidade);
		Sql += " AND UF LIKE ?";
		ps.adicionarString(uf);
		Sql += " ORDER BY BAIRRO";
		try{
			rs1 = consultar(Sql, ps);

			if (rs1.next()) {
				bairroDt = new BairroDt();
				bairroDt.setId(rs1.getString("ID_BAIRRO"));
				bairroDt.setBairro(rs1.getString("BAIRRO"));
				bairroDt.setId_Cidade(rs1.getString("ID_CIDADE"));
				bairroDt.setCidade(rs1.getString("CIDADE"));
				bairroDt.setUf(rs1.getString("UF"));
				bairroDt.setCodigoSPG(rs1.getString("CODIGO_SPG"));
				bairroDt.setZona(rs1.getString("ZONA"));
				bairroDt.setRegiao(rs1.getString("REGIAO"));
				bairroDt.setCodigoCidadeSPG(rs1.getString("CODIGO_CIDADE_SPG"));
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return bairroDt;
	}
	
	/**
	 * Consulta um bairro de acordo com descrição, cidade e estado passados
	 * @param descricao, filtro para descrição do bairro
	 * @param cidade, filtro para descrição da cidade
	 * @param uf, filtro para estado
	 */
	public String consultarId(String descricao, String cidade, String uf) throws Exception {

		String sql;
		String id = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT ID_BAIRRO FROM PROJUDI.VIEW_BAIRRO ";
		sql += " WHERE BAIRRO LIKE ?";
		sql += " AND CIDADE LIKE ?";
		sql += " AND UF LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		ps.adicionarString("%"+ cidade +"%");
		ps.adicionarString("%"+ uf +"%");
		try{
			rs1 = consultar(sql, ps);

			if (rs1.next()) {
				id = rs1.getString("ID_BAIRRO");
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return id;
	}
	
	public String consultarDescricaoJSON(String descricao, String cidade, String uf, String zona, String posicao) throws Exception {

		String Sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 5;

		Sql = "SELECT ID_BAIRRO AS ID, BAIRRO AS DESCRICAO1, CIDADE AS DESCRICAO2, UF AS DESCRICAO3, ZONA AS DESCRICAO4, REGIAO AS DESCRICAO5";
		sqlFrom = " FROM PROJUDI.VIEW_BAIRRO  WHERE  BAIRRO LIKE ?";
		ps.adicionarString( "%" + descricao +"%");
		
		sqlFrom += " AND CIDADE LIKE ?";
		ps.adicionarString( cidade +"%");
		
		sqlFrom += " AND UF LIKE ?";
		ps.adicionarString( uf +"%");
		
		if (zona != null && zona.trim().length() > 0) {
			sqlFrom += " AND ZONA LIKE ?";
			ps.adicionarString( zona +"%");	
		}
		
		sqlOrder = " ORDER BY BAIRRO, UF, CIDADE";
		
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
	
	public String consultarDescricaoBairrosGeralJSON(String descricao, String cidade, String uf, String zona, String posicao) throws Exception {

		String Sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 5;

		Sql = "SELECT ID_BAIRRO AS ID, BAIRRO AS DESCRICAO1, CIDADE AS DESCRICAO2, UF AS DESCRICAO3, ZONA AS DESCRICAO4, REGIAO AS DESCRICAO5";
		sqlFrom = " FROM PROJUDI.VIEW_BAIRRO  WHERE BAIRRO LIKE ?";
		ps.adicionarString( "%" + descricao +"%");
		
		sqlFrom += " AND CIDADE LIKE ?";
		ps.adicionarString( cidade +"%");
		
		sqlFrom += " AND UF LIKE ?";
		ps.adicionarString( uf +"%");
		
		if (zona != null && zona.trim().length() > 0) {
			sqlFrom += " AND ZONA LIKE ?";
			ps.adicionarString( zona +"%");	
		}
		
		sqlOrder = " ORDER BY BAIRRO, UF, CIDADE";
		
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
	
	
	public String consultarDescricaoLocomocaoJSON(String descricao, String cidade, String uf, String posicao) throws Exception {

		String Sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql = "SELECT ID_BAIRRO AS ID, BAIRRO AS DESCRICAO1, CIDADE AS DESCRICAO2, UF AS DESCRICAO3";
		sqlFrom = " FROM PROJUDI.VIEW_BAIRRO WHERE BAIRRO LIKE ?";
		ps.adicionarString( "%" + descricao +"%");
		
		sqlFrom += " AND CIDADE LIKE ?";
		ps.adicionarString( cidade +"%");
		
		sqlFrom += " AND UF LIKE ?";
		ps.adicionarString( uf +"%");
		
		sqlFrom += " AND ZONA IS NOT NULL";
		
		sqlOrder = " ORDER BY BAIRRO, UF, CIDADE";
		
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
	
	public String consultarDescricaoJSON(String descricao, String cidade, String uf, String posicao) throws Exception {

		String Sql, sqlFrom, sqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT ID_BAIRRO AS ID, BAIRRO AS DESCRICAO1, CIDADE AS DESCRICAO2, UF AS DESCRICAO3, ID_CIDADE AS DESCRICAO4";
		
		sqlFrom = "  FROM PROJUDI.VIEW_BAIRRO WHERE BAIRRO LIKE ?";
		ps.adicionarString( "%" + descricao +"%");
		
		sqlFrom += " AND CIDADE LIKE ?";
		ps.adicionarString( cidade +"%");
		
		sqlFrom += " AND UF LIKE ?";
		ps.adicionarString( uf +"%");
		
		sqlOrder = " ORDER BY BAIRRO";
		
		try {
			rs1 = consultarPaginacao(Sql + sqlFrom + sqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE ";
			
			rs2 = consultar(Sql + sqlFrom, ps);			
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} catch (Exception e) {
			throw new Exception("<{Erro.}> Local Exception: " + this.getClass().getName() + ".consultarDescricaoJSON(): " + e.getMessage(), e);
        } finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
             try {if (rs2 != null) rs2.close();} catch (Exception e1) {}
        }  
		return stTemp;
	}
	
	public List consultarIdCidade(String idCidade) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_BAIRRO,BAIRRO,ID_CIDADE,CIDADE,UF,ZONA,REGIAO FROM PROJUDI.VIEW_BAIRRO ";
		Sql += " WHERE ID_CIDADE = ?";
		ps.adicionarLong(idCidade);		
		Sql += " ORDER BY BAIRRO";		
		try{

			rs = consultar(Sql, ps);
			while (rs.next()) {
				BairroDt obTemp = new BairroDt();
				obTemp.setId(rs.getString("ID_BAIRRO"));
				obTemp.setBairro(rs.getString("BAIRRO"));
				obTemp.setId_Cidade(rs.getString("ID_CIDADE"));
				obTemp.setCidade(rs.getString("CIDADE"));
				obTemp.setUf(rs.getString("UF"));
				obTemp.setZona(rs.getString("ZONA"));
				obTemp.setRegiao(rs.getString("REGIAO"));
				liTemp.add(obTemp);
			}
			
		
        } finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }  
		return liTemp;
	}
	
	public BairroDt consultarId(String id_bairro )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		BairroDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_BAIRRO WHERE ID_BAIRRO = ?";		ps.adicionarLong(id_bairro); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new BairroDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	protected void associarDt( BairroDt Dados, ResultSetTJGO rs1 )  throws Exception {
		Dados.setId(rs1.getString("ID_BAIRRO"));
		Dados.setBairro(rs1.getString("BAIRRO"));
		Dados.setBairroCodigo( rs1.getString("BAIRRO_CODIGO"));
		Dados.setId_Cidade( rs1.getString("ID_CIDADE"));
		Dados.setCidade( rs1.getString("CIDADE"));
		Dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		Dados.setUf( rs1.getString("UF"));
		Dados.setCodigoSPG(rs1.getString("CODIGO_SPG"));
		Dados.setZona(rs1.getString("ZONA"));
		Dados.setRegiao(rs1.getString("REGIAO"));
		Dados.setCodigoCidadeSPG(rs1.getString("CODIGO_CIDADE_SPG"));
	}
	
	public BairroDt consultarBairroCodigoSPG(String codigoMunicipioSPG, String codigoBairroSPG) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		BairroDt Dados=null;
		
		String codigoMunicipioEBairro = codigoMunicipioSPG + Funcoes.completarZeros(codigoBairroSPG, 5);

		stSql = "SELECT * FROM PROJUDI.VIEW_BAIRRO WHERE CODIGO_SPG = ?";		ps.adicionarLong(codigoMunicipioEBairro);		

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new BairroDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return Dados; 
	}
	
	/**
	 * Método para consultar bairros que tem as seguintes condições:
	 * CODIGO_SPG preenchido
	 * e 
	 * BAIRRO que esteja zoneado
	 * 
	 * Consulta feita para sincronizar dados dos bairros com os bairros do SPG.
	 * 
	 * @param String idCidade
	 * @return List<BairroDt>
	 * @throws Exception
	 */
	public List<BairroDt> consultarBairrosZoneados(String idCidade) throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<BairroDt> listaBairroDt = null;
		
		stSql = "SELECT * FROM PROJUDI.BAIRRO B ";
		stSql += "INNER JOIN PROJUDI.ZONA_BAIRRO_REGIAO ZB on (B.ID_BAIRRO = ZB.ID_BAIRRO) ";
		stSql += "WHERE B.CODIGO_SPG IS NOT NULL ";
		if( idCidade != null && !idCidade.isEmpty() ) {
			stSql += "AND B.ID_CIDADE = ?";
			ps.adicionarLong(idCidade);
		}

		try {
			rs1 = consultar(stSql,ps);
			if ( rs1 != null ) {
				
				listaBairroDt = new ArrayList<BairroDt>();
				
				while (rs1.next()) {
					BairroDt dados= new BairroDt();
					associarDt(dados, rs1);
					
					listaBairroDt.add(dados);
				}
			}
		}
		finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return listaBairroDt; 
	}
	
	public BairroDt consultarBairroCodigo(String bairroCodigo) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		BairroDt Dados = null;

		stSql = "SELECT * ";
		stSql += "FROM PROJUDI.VIEW_CARGO_TIPO ";
		stSql += "WHERE CARGO_TIPO_CODIGO = ? ";
		ps.adicionarLong(bairroCodigo); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new BairroDt();
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
	
	public BairroDt consultarBairro(String descricao, String idCidade) throws Exception {

		String Sql;
		BairroDt bairroDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_BAIRRO,BAIRRO,ID_CIDADE,CIDADE,UF,CODIGO_SPG,ZONA,REGIAO,CODIGO_CIDADE_SPG FROM PROJUDI.VIEW_BAIRRO ";
		Sql += " WHERE BAIRRO LIKE ?";
		ps.adicionarString(descricao);
		Sql += " AND ID_CIDADE = ?";
		ps.adicionarLong(idCidade);
		Sql += " ORDER BY BAIRRO";
		try{
			rs1 = consultar(Sql, ps);

			if (rs1.next()) {
				bairroDt = new BairroDt();
				bairroDt.setId(rs1.getString("ID_BAIRRO"));
				bairroDt.setBairro(rs1.getString("BAIRRO"));
				bairroDt.setId_Cidade(rs1.getString("ID_CIDADE"));
				bairroDt.setCidade(rs1.getString("CIDADE"));
				bairroDt.setUf(rs1.getString("UF"));
				bairroDt.setCodigoSPG(rs1.getString("CODIGO_SPG"));
				bairroDt.setZona(rs1.getString("ZONA"));
				bairroDt.setRegiao(rs1.getString("REGIAO"));
				bairroDt.setCodigoCidadeSPG(rs1.getString("CODIGO_CIDADE_SPG"));
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return bairroDt;
	}
}
