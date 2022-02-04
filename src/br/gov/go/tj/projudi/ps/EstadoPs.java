package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ProjudiConstantes;
import br.gov.go.tj.utils.ResultSetTJGO;

public class EstadoPs extends EstadoPsGen {

    private static final long serialVersionUID = 908874187212898263L;
    
    public EstadoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consulta geral de Estados
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_ESTADO,ESTADO,UF";
		SqlFrom = " FROM PROJUDI.VIEW_ESTADO";
		SqlFrom += " WHERE ESTADO LIKE ?";
		SqlOrder = " ORDER BY ESTADO";
		ps.adicionarString("%"+ descricao +"%");		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				EstadoDt obTemp = new EstadoDt();
				obTemp.setId(rs1.getString("ID_ESTADO"));
				obTemp.setEstado(rs1.getString("ESTADO"));
				obTemp.setUf(rs1.getString("UF"));
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
	 * Método responsável em buscar um Estado baseado no Uf passado
	 * @throws Exception 
	 */
	public EstadoDt buscaEstado(String ufEstado) throws Exception {

		EstadoDt estadoDt = null;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{

			sql = "SELECT * FROM PROJUDI.VIEW_ESTADO e WHERE e.UF = ?";
			ps.adicionarString(ufEstado);

			 rs1 = consultar(sql, ps);
			if (rs1.next()) {
				estadoDt = new EstadoDt();
				estadoDt.setId(rs1.getString("ID_ESTADO"));
				estadoDt.setEstado(rs1.getString("ESTADO"));
				estadoDt.setUf(rs1.getString("UF"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return estadoDt;
	}

	public String consultarDescricaoJSON(String descricao,  String posicao) throws Exception {
		
		String Sql="";
		String SqlFrom ="";
		String SqlOrder ="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql = "SELECT ID_ESTADO AS ID , ESTADO AS DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_ESTADO";
		SqlFrom += " WHERE ESTADO LIKE ?";
		SqlOrder = " ORDER BY ESTADO";
		ps.adicionarString("%"+ descricao +"%");		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
					
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);						
													
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarDescricao2JSON(String descricao,  String posicao) throws Exception {
		
		String Sql="";
		String SqlFrom ="";
		String SqlOrder ="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		Sql = "SELECT ID_ESTADO AS ID , ESTADO AS DESCRICAO2 , UF AS DESCRICAO1";
		SqlFrom = " FROM PROJUDI.VIEW_ESTADO";
		SqlFrom += " WHERE ESTADO LIKE ?";
		SqlOrder = " ORDER BY ESTADO";
		ps.adicionarString("%"+ descricao +"%");		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
					
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);						
													
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarDescricaoUfJSON(String descricao,  String posicao) throws Exception {
		
		String Sql="";
		String SqlFrom ="";
		String SqlOrder ="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		Sql = "SELECT ID_ESTADO AS ID , UF AS DESCRICAO1, ESTADO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_ESTADO";
		SqlFrom += " WHERE ESTADO LIKE ?";
		SqlOrder = " ORDER BY ESTADO";
		ps.adicionarString("%"+ descricao +"%");		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
					
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);						
													
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarDescricaoUfJSON (String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		
		String Sql="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		Sql = "SELECT ID_ESTADO AS ID , UF AS DESCRICAO1, ESTADO AS DESCRICAO2, PAIS AS DESCRICAO3 FROM PROJUDI.VIEW_ESTADO WHERE (ESTADO LIKE ? OR UF LIKE ?)";
		Sql += " ORDER BY " + ordenacao;
		ps.adicionarString("%" + descricao + "%");
		ps.adicionarString("%" + descricao + "%");
		try {
			rs1 = consultarPaginacao(Sql, ps, posicao, quantidadeRegistros);
			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_ESTADO WHERE (ESTADO LIKE ? OR UF LIKE ?)";
			
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
             try {if (rs2 != null) rs2.close();} catch (Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarUfJSON(String descricao,  String posicao) throws Exception {
		String Sql="";
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		
		Sql = "SELECT UF AS ID, ESTADO AS DESCRICAO1 FROM PROJUDI.VIEW_ESTADO WHERE ESTADO LIKE ? OR UF LIKE ?";
		Sql += " ORDER BY ESTADO";
		ps.adicionarString("%" + descricao + "%");
		ps.adicionarString("%" + descricao + "%");
		try {
			rs1 = consultarPaginacao(Sql, ps, posicao);
					
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE  FROM PROJUDI.VIEW_ESTADO WHERE ESTADO LIKE ? OR UF LIKE ?";
			
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);						
													
			
			//rs1.close();
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
             try {if (rs2 != null) rs2.close();} catch (Exception e1) {}
        }
		return stTemp;
	}
	
	
	public List consultarIdPais(String idPais) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_ESTADO,ESTADO,UF FROM PROJUDI.VIEW_ESTADO WHERE ID_PAIS = ?";
		Sql += " ORDER BY ESTADO";
		ps.adicionarString(idPais);		
		try{
			rs = consultar(Sql, ps);
			while (rs.next()) {
				EstadoDt obTemp = new EstadoDt();
				obTemp.setId(rs.getString("ID_ESTADO"));
				obTemp.setEstado(rs.getString("ESTADO"));
				obTemp.setUf(rs.getString("UF"));
				liTemp.add(obTemp);
			}
		
        } finally{
             try{if (rs != null) rs.close();} catch(Exception e1) {}
        }
		return liTemp;
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
		
		Sql = "SELECT * FROM PROJUDI.ESTADO";
		if (valorFiltro != null) {
			Sql += " WHERE ESTADO LIKE ?"; 
			ps.adicionarString(valorFiltro);	
		}		 
		Sql += " ORDER BY ESTADO";		
		
		try {
			rs1 = consultar(Sql, ps);
			while (rs1.next() && listaTemp.size() <= ProjudiConstantes.QUANTIDADE_OBJETOS_COMBO) {
				DescritorComboDt obTemp = new DescritorComboDt();
				obTemp.setId(rs1.getString("ID_ESTADO"));
				obTemp.setDescricao(rs1.getString("ESTADO"));				
				listaTemp.add(obTemp);
			}			
		} finally{
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
        }
		return listaTemp;
		
	}
	
	public EstadoDt consultarEstadoCodigo(String codigoEstado )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		EstadoDt Dados=null;

		stSql = "SELECT * ";
		stSql += "FROM PROJUDI.VIEW_ESTADO ";
		stSql += "WHERE ESTADO_CODIGO = ? ";
		ps.adicionarLong(codigoEstado); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new EstadoDt();
				associarDt(Dados, rs1);
			}
		}finally{
				try{
					if (rs1 != null) rs1.close();
				} catch(Exception e) {}
		}
		return Dados; 
	}	
}