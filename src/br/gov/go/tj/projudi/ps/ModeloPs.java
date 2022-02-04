package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ModeloPs extends ModeloPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -3547890900853528505L;


    @SuppressWarnings("unused")
	private ModeloPs( ) {}
    
    public ModeloPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consulta geral de modelos. 
	 * 
	 * @descricao parâmetro para consulta
	 * @posicao parametro para paginação
	 * @param id_ArquivoTipo tipo de arquivo para que sejam filtrados os modelos
	 * @param id_Serventia identificação da serventia, define que devem ser retornados os modelos da serventia
	 * @param generico booleano que define se devem ser retornados os modelos genéricos
	 * 
	 * 
	 * @author msapaula
	 */
	public List consultarModelos(String descricao, String posicao, String id_ArquivoTipo, String id_serventia, boolean generico) throws Exception {
		String sql;
		String sqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sqlComum = " FROM PROJUDI.VIEW_MODELO WHERE MODELO LIKE ?";
		ps.adicionarString("%" + descricao + "%");

		if (id_ArquivoTipo != null && !id_ArquivoTipo.trim().equals("")) {
			sqlComum += " AND ID_ARQ_TIPO = ?";
			ps.adicionarLong(id_ArquivoTipo);
		}
		if (id_serventia != null && id_serventia.length() > 0) {
			sqlComum += " AND (ID_SERV = ?";
			ps.adicionarLong(id_serventia);
			if (generico) {
				sqlComum += " OR ID_SERV IS NULL AND ID_USU_SERV IS NULL)";
			}
		} else if (generico)
			sqlComum += " AND ID_SERV IS NULL AND ID_USU_SERV IS NULL";
		
		sql = "SELECT ID_MODELO, MODELO, ID_SERV, SERV, USU_SERV, ARQ_TIPO" + sqlComum;
		sql += " ORDER BY ARQ_TIPO, MODELO";

		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			while (rs1.next()) {
				ModeloDt obTemp = new ModeloDt();
				obTemp.setId(rs1.getString("ID_MODELO"));
				obTemp.setModelo(rs1.getString("MODELO"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setUsuarioServentia(rs1.getString("USU_SERV"));
				obTemp.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	
	/**
	 * Consulta geral de modelos. 
	 * 
	 * @descricao parâmetro para consulta
	 * @posicao parametro para paginação
	 * @param id_ArquivoTipo tipo de arquivo para que sejam filtrados os modelos
	 * @param id_Serventia identificação da serventia, define que devem ser retornados os modelos da serventia
	 * @param generico booleano que define se devem ser retornados os modelos genéricos
	 * 
	 * 
	 * @author msapaula
	 * @author jrcorrea 01/10/2014
	 */
	
	
	public String consultarModelosJSON(String descricao,  String posicao, String id_ArquivoTipo, String id_serventia, boolean generico) throws Exception {
		String sql;
		String sqlComum;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		sqlComum = " FROM PROJUDI.VIEW_MODELO WHERE MODELO LIKE ? ";	ps.adicionarString("%" + descricao + "%");
		
		if (id_ArquivoTipo != null && !id_ArquivoTipo.trim().equals("")) {
			sqlComum += " AND ID_ARQ_TIPO = ?";
			ps.adicionarLong(id_ArquivoTipo);
		}
		
		//Serviroes da equipe de gerenciamento consultam apenas modelos genéricos na tela de cadastro de modelos.
		//TODO: Passar esta regra para um NE e refatorar esta solução.
		if (id_serventia != null && id_serventia.length() > 0 && !id_serventia.equalsIgnoreCase(ServentiaDt.GERENCIAMENTO_SISTEMA_PRODUDI)) {
			
    		sqlComum += " AND (ID_SERV = ?";
	    	ps.adicionarLong(id_serventia);
		    if (generico) {
			    sqlComum += " OR (ID_SERV IS NULL AND ID_USU_SERV IS NULL))";
		    } else {
	    		sqlComum += " )";
		    }
			
		} else if (generico)
			sqlComum += " AND ID_SERV IS NULL AND ID_USU_SERV IS NULL";
		
		sql = "SELECT ID_MODELO AS ID, MODELO AS DESCRICAO1, SERV AS DESCRICAO2, ARQ_TIPO AS DESCRICAO3" + sqlComum;
		sql += " ORDER BY MODELO, SERV, ARQ_TIPO";
		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(sql, ps);

			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}

	
	/**
	 * Consulta modelos de um usuário
	 * 
	 * @param descricao parâmetro para consulta
	 * @param posicao parametro para paginação
	 * @param id_ArquivoTipo tipo do arquivo para filtrar os modelos
	 * @param id_UsuarioServentia identificação do usuario serventia
	 * 
	 * @author msapaula
	 */
	public List consultarModelosUsuario(String descricao, String posicao, String id_ArquivoTipo, String id_UsuarioServentia) throws Exception {
		String sql;
		String sqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sqlComum = " FROM PROJUDI.VIEW_MODELO WHERE MODELO LIKE ?";
			ps.adicionarString("%" + descricao + "%");
		if (id_ArquivoTipo != null && !id_ArquivoTipo.trim().equals("")){
			sqlComum += " AND ID_ARQ_TIPO = ?";
			ps.adicionarLong(id_ArquivoTipo);
		}
		sqlComum += " AND ID_USU_SERV = ?";
		ps.adicionarLong(id_UsuarioServentia);

		sql = "SELECT ID_MODELO,MODELO,ID_SERV,SERV,USU_SERV, ARQ_TIPO" + sqlComum;
		sql += " ORDER BY MODELO, ARQ_TIPO";
		
		
		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			while (rs1.next()) {
				ModeloDt obTemp = new ModeloDt();
				obTemp.setId(rs1.getString("ID_MODELO"));
				obTemp.setModelo(rs1.getString("MODELO"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setUsuarioServentia(rs1.getString("USU_SERV"));
				obTemp.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(sql,ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	public String consultarModelosUsuarioJSON(String descricao, String posicao, String id_ArquivoTipo, String id_UsuarioServentia, String ordenacao, String quantidadeRegistros) throws Exception {
		
		if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
			ordenacao =  " SERV, ARQ_TIPO, MODELO ";
		
		String sql;
		String sqlComum;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		sqlComum = " FROM PROJUDI.VIEW_MODELO WHERE MODELO LIKE ? "; ps.adicionarString("%" + descricao + "%");
			
		if (id_ArquivoTipo != null && !id_ArquivoTipo.trim().equals("")){
			sqlComum += " AND ID_ARQ_TIPO = ?";
			ps.adicionarLong(id_ArquivoTipo);
		}
		sqlComum += " AND ID_USU_SERV = ?";
		ps.adicionarLong(id_UsuarioServentia);

		sql = "SELECT ID_MODELO AS ID, MODELO AS DESCRICAO1, SERV AS DESCRICAO2, ARQ_TIPO AS DESCRICAO3" + sqlComum;
		sql += " ORDER BY "+ordenacao;
				
		try{
			rs1 = consultarPaginacao(sql, ps, posicao, quantidadeRegistros);
			sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	
	/**
	 * Consulta modelos de um grupo específicio, usado até agora para o caso de juizes, onde
	 * esses podem ver os seus modelos e os genéricos. 
	 * 
	 * @param descricao parâmetro para consulta
	 * @param posicao parametro para paginação
	 * @param id_ArquivoTipo tipo do arquivo para filtrar os modelos
	 * @param id_UsuarioServentia identificação do usuario serventia
	 * 
	 * @author msapaula
	 */
	public List consultarModelosGrupo(String descricao, String posicao, String id_ArquivoTipo, String id_UsuarioServentia, String id_Serventia)
			throws Exception {
		String sql;
		String sqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sqlComum = " FROM PROJUDI.VIEW_MODELO WHERE MODELO LIKE ?";
		ps.adicionarString("%" + descricao + "%");
		if (id_ArquivoTipo != null && !id_ArquivoTipo.trim().equals("")) {
			sqlComum += " AND ID_ARQ_TIPO = ?";
			ps.adicionarLong(id_ArquivoTipo);
		}
			sqlComum += " AND ((ID_USU_SERV = ?"; 
			ps.adicionarLong(id_UsuarioServentia);

		if (id_Serventia != null) {
			sqlComum += " OR ID_SERV = ? )";
			 ps.adicionarLong(id_Serventia); 
		} else
			sqlComum += " )";

		sqlComum += " OR (ID_USU_SERV IS NULL AND ID_SERV IS NULL) )";

		sql = "SELECT ID_MODELO,MODELO,ID_SERV,SERV, USU_SERV, ARQ_TIPO" + sqlComum;
		sql += " ORDER BY ARQ_TIPO, MODELO";
		
		try{
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			while (rs1.next()) {
				ModeloDt obTemp = new ModeloDt();
				obTemp.setId(rs1.getString("ID_MODELO"));
				obTemp.setModelo(rs1.getString("MODELO"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setUsuarioServentia(rs1.getString("USU_SERV"));
				obTemp.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	public String consultarModelosGrupoJSON(String descricao,  String posicao, String id_ArquivoTipo, String id_UsuarioServentia, String id_Serventia)	throws Exception {
		String sql;
		String sqlComum;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		sqlComum = " FROM PROJUDI.VIEW_MODELO WHERE MODELO LIKE ?";		ps.adicionarString("%" + descricao + "%");
		
		if (id_ArquivoTipo != null && !id_ArquivoTipo.trim().equals("")) {
			sqlComum += " AND ID_ARQ_TIPO = ?";
			ps.adicionarLong(id_ArquivoTipo);
		}
		//MEUS MODELOS
		sqlComum += " AND ((ID_USU_SERV = ? AND ID_SERV = ? )"; 
		ps.adicionarLong(id_UsuarioServentia);
		ps.adicionarLong(id_Serventia);
		
		//MODELOS DA SERVENTIA
		sqlComum += " OR (ID_USU_SERV IS NULL AND ID_SERV = ? )"; 
		ps.adicionarLong(id_Serventia);
		
		//MODELOS GENÉRICOS
		sqlComum += " OR (ID_USU_SERV IS NULL AND ID_SERV IS NULL) )";
		
		sql = "SELECT ID_MODELO AS ID, MODELO AS DESCRICAO1, SERV AS DESCRICAO2, ARQ_TIPO AS DESCRICAO3" + sqlComum;
		sql += " ORDER BY MODELO, SERV, ARQ_TIPO";
		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
		} finally{
		     try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		     try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
		}
		return stTemp;
	}
	
	/**
	* Realiza chamada ao objeto que efetuará a consulta modelo para código de acesso
	*/

	public ModeloDt consultarModeloCodigoAcesso(String id_ArquivoTipo) throws Exception {
		String sql;
		ModeloDt modeloDt = new ModeloDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT ID_MODELO,MODELO,ID_SERV,SERV,USU_SERV, ARQ_TIPO FROM PROJUDI.VIEW_MODELO  MD ";
		sql += " WHERE MD.ID_ARQ_TIPO = ?";
		ps.adicionarLong(id_ArquivoTipo);
		sql += " AND ID_SERV IS NULL ";
		sql += " AND ID_USU_SERV IS NULL ";		
		sql += " AND ROWNUM = ? ";	
		ps.adicionarLong(1);
		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				modeloDt.setId(rs1.getString("ID_MODELO"));
				modeloDt.setModelo(rs1.getString("MODELO"));
				modeloDt.setServentia(rs1.getString("SERV"));
				modeloDt.setId_Serventia(rs1.getString("ID_SERV"));
				modeloDt.setUsuarioServentia(rs1.getString("USU_SERV"));
				modeloDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
		return modeloDt;
	}

	
	/**
	 * @author jpcpresa
	* Realiza chamada ao objeto que efetuará a consulta modelo para código de acesso através do 
	* código do modelo.
	*/
	
	
	public ModeloDt consultarModeloCodigo(String modeloCodigo) throws Exception {
		String sql;
		ModeloDt modeloDt = new ModeloDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT ID_MODELO,MODELO, TEXTO, MODELO_CODIGO, ID_SERV,SERV,USU_SERV, ARQ_TIPO FROM PROJUDI.VIEW_MODELO md WHERE md.MODELO_CODIGO = ?";
		ps.adicionarLong(modeloCodigo);
		sql += " AND ROWNUM = ? ";
		ps.adicionarLong(1);
		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				modeloDt.setId(rs1.getString("ID_MODELO"));
				modeloDt.setModelo(rs1.getString("MODELO"));
				modeloDt.setTexto(rs1.getString("TEXTO"));
				modeloDt.setServentia(rs1.getString("SERV"));
				modeloDt.setId_Serventia(rs1.getString("ID_SERV"));
				modeloDt.setUsuarioServentia(rs1.getString("USU_SERV"));
				modeloDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
			}
			//rs1.close();
		
			
        } finally{
             try{
            	 if (rs1 != null) 
            		 rs1.close();
            	 } 
             catch(Exception e1) {}
        }
		return modeloDt;
	}
	
	/**
	 * Método que exclui modelos levando em consideração o perfil do usuário que está realizando a ação.
	 * Motivo: somente o usuário ADMINISTRADOR pode excluir modelos que não tem ID_SERV (próprios).
	 * @param idModelo - Id do modelo 
	 * @param isUsuarioAdministrador - se o grupo do usuário logado é Administrador
	 * @throws Exception
	 */
	public void excluir(String idModelo, boolean isUsuarioAdministrador) throws Exception {
		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.MODELO";
		stSql += " WHERE ID_MODELO = ?";		ps.adicionarLong(idModelo); 
		if(!isUsuarioAdministrador){
			stSql += " AND ID_SERV IS NOT NULL";
		}

		executarUpdateDelete(stSql,ps);

	} 
	
	/**
	 * Retonar a quantidade de locomoção vinculada ao modelo
	 * @param idModelo
	 * @return String 
	 * @throws Exception
	 */
	public String consultarQtdLocomocao(String idModelo) throws Exception {
		String sql;
		String qtdLocomocao = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT QTD_LOCOMOCAO FROM PROJUDI.MODELO WHERE ID_MODELO = ?";
		ps.adicionarLong(idModelo);
		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				qtdLocomocao = rs1.getString("QTD_LOCOMOCAO");
			}
        } finally{
             try{
            	 if (rs1 != null) 
            		 rs1.close();
            	 } 
             catch(Exception e1) {}
        }
		return qtdLocomocao;
	}
}
