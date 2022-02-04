package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

// ---------------------------------------------------------
public class GrupoPs extends GrupoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -5770342412016086549L;

    @SuppressWarnings("unused")
	private GrupoPs( ) {}
    
    public GrupoPs(Connection conexao){
    	Conexao = conexao;
	}

	public List consultarTodos() throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;

		Sql = "SELECT ID_GRUPO,GRUPO FROM PROJUDI.VIEW_GRUPO ORDER BY GRUPO";
		try{
			rs1 = consultarSemParametros(Sql);

			while (rs1.next()) {
				GrupoDt obTemp = new GrupoDt();
				obTemp.setId(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}

	public String consultarCodigo(int grupoCodigo) throws Exception {
		String Sql;
		String idGrupo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		

		Sql = "SELECT ID_GRUPO FROM PROJUDI.VIEW_GRUPO WHERE GRUPO_CODIGO = ?";
		ps.adicionarLong(grupoCodigo);

		try{
			 rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				idGrupo = rs1.getString("ID_GRUPO");
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return idGrupo;
	}

	/**
	 * Consultar determinado grupo de acordo com Código (GrupoCodigo)
	 */
	public GrupoDt consultarGrupoCodigo(String grupoCodigo) throws Exception {
		String Sql;
		GrupoDt Dados = new GrupoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_GRUPO WHERE GRUPO_CODIGO = ?";
		ps.adicionarLong(grupoCodigo);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				super.associarDt(Dados, rs1);				
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	/**
	 * Consulta todos os grupod cadastrados. Substituindo método do Gen para retornar Tipo da
	 * Serventia.
	 * 
	 * @param descricao
	 * @param posicao
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_GRUPO, GRUPO, SERV_TIPO, SERV_TIPO_CODIGO";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO";
		SqlFrom += " WHERE GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlOrder = " ORDER BY GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				GrupoDt obTemp = new GrupoDt();
				obTemp.setId(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom,ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
		
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	/**
	 * Consulta os grupos que poderão ser utilizados na habilitação de Servidor. Até o momento são
	 * retornados todos, menos o administrador, advogado e assistentes.
	 * 
	 * @param descricao
	 * @param posicao
	 * 
	 * @author msapaula
	 */
	public List consultarGruposHabilitacaoServidores(String descricao, String posicao) throws Exception {

		String Sql, sqlFrom, sqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_GRUPO, GRUPO, SERV_TIPO_CODIGO, SERV_TIPO ";
		sqlFrom = " FROM PROJUDI.VIEW_GRUPO g WHERE g.GRUPO LIKE ?";															ps.adicionarString("%" + descricao +"%");
		sqlFrom += " AND g.GRUPO_CODIGO <> ?"; 		ps.adicionarLong(GrupoDt.ADMINISTRADORES);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_ADVOGADOS);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_MP);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_VARA);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.JUIZ_LEIGO);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU);
		sqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.CADASTRADORES_TABELA);
		
		sqlOrder = " ORDER BY g.GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + sqlFrom + sqlOrder, ps, posicao);
			while (rs1.next()) {
				GrupoDt obTemp = new GrupoDt();
				obTemp.setId(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			
			Sql = "SELECT COUNT(*) as QUANTIDADE ";
			
			rs2 = consultar(Sql + sqlFrom, ps);
			
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	/**
	 * Consulta os grupos que poderão ser utilizados na habilitação de um promotor ou coordenador de promotoria. Até o momento são
	 * 
	 * @param descricao
	 * @param posicao
	 * 
	 * @author lsbernardes
	 */
	public List consultarGruposHabilitacaoPromotores(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_GRUPO, GRUPO, SERV_TIPO_CODIGO, SERV_TIPO";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO g";
		SqlFrom += " WHERE g.GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND (g.GRUPO_CODIGO in (?,?)";	ps.adicionarLong(GrupoDt.MINISTERIO_PUBLICO ); ps.adicionarLong(GrupoDt.MP_TCE);
		SqlFrom += " OR g.GRUPO_CODIGO = ? )";
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		SqlOrder = " ORDER BY g.GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				GrupoDt obTemp = new GrupoDt();
				obTemp.setId(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	public List consultarGruposListaUsuarios(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_GRUPO, GRUPO, SERV_TIPO_CODIGO, SERV_TIPO";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO g";
		SqlFrom += " WHERE g.GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";
		ps.adicionarLong(GrupoDt.ADMINISTRADORES);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";
		ps.adicionarLong(GrupoDt.ASSESSOR);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";				ps.adicionarLong(GrupoDt.ASSESSOR_ADVOGADOS);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";				ps.adicionarLong(GrupoDt.ASSESSOR_MP);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";				ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_VARA);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";				ps.adicionarLong(GrupoDt.JUIZ_LEIGO);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";
		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU);
		SqlOrder = " ORDER BY g.GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				GrupoDt obTemp = new GrupoDt();
				obTemp.setId(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	public String consultarGruposListaUsuariosJSON(String descricao, String posicao ) throws Exception {
		
		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;
		
		stSql = "SELECT ID_GRUPO AS ID, GRUPO AS DESCRICAO1, SERV_TIPO_CODIGO AS DESCRICAO2, SERV_TIPO AS DESCRICAO3";
		stSqlFrom = " FROM PROJUDI.VIEW_GRUPO g";
		stSqlFrom += " WHERE g.GRUPO LIKE ?";
		ps.adicionarString("%"+descricao+"%"); 
		stSqlFrom += " AND g.GRUPO_CODIGO <> ?";
		ps.adicionarLong(GrupoDt.ADMINISTRADORES);
		stSqlFrom += " AND g.GRUPO_CODIGO <> ?";
		ps.adicionarLong(GrupoDt.ASSESSOR);
		stSqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_ADVOGADOS);
		stSqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_MP);
		stSqlFrom += " AND g.GRUPO_CODIGO <> ?";        ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_VARA);
		stSqlFrom += " AND g.GRUPO_CODIGO <> ?";        ps.adicionarLong(GrupoDt.JUIZ_LEIGO);
		stSqlFrom += " AND g.GRUPO_CODIGO <> ?";
		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU);
		stSqlOrder = " ORDER BY g.GRUPO ";
		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);
			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		stSql= "SELECT ID_GRUPO AS ID, GRUPO AS DESCRICAO1, GRUPO_CODIGO AS DESCRICAO2, SERV_TIPO AS DESCRICAO3";
		stSqlFrom = " FROM PROJUDI.VIEW_GRUPO";
		stSqlFrom += " WHERE GRUPO LIKE ?";
		stSqlOrder = " ORDER BY GRUPO ";
		ps.adicionarString("%"+descricao+"%"); 

		try{

			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			stSql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
	public String consultarDescricaoServentiaJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 3;

		Sql = "SELECT ID_GRUPO AS ID, GRUPO AS DESCRICAO1, SERV_TIPO AS DESCRICAO2, SERV_TIPO_CODIGO AS DESCRICAO3";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO";
		SqlFrom += " WHERE GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlOrder = " ORDER BY GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	/**
	 * Método que permite a localização de grupos para usuários cadastradores. Impede que cadastradores cadastrem usuários com determinados
	 * perfis por questão de segurança.
	 * @param descricao - descrição do grupo
	 * @param posicao - posição
	 * @return lista de grupos localizados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarDescricaoGrupoCadastradoresJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_GRUPO AS ID, GRUPO AS DESCRICAO1, SERV_TIPO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO";
		SqlFrom += " WHERE GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND GRUPO_CODIGO NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		ps.adicionarLong(GrupoDt.ADMINISTRADORES);
		ps.adicionarLong(GrupoDt.ADMINISTRADOR_LOG);
		ps.adicionarLong(GrupoDt.CADASTRADORES);
		ps.adicionarLong(GrupoDt.CADASTRADORES_TABELA);
		ps.adicionarLong(GrupoDt.CADASTRADOR_MASTER);
		ps.adicionarLong(GrupoDt.GERENCIAMENTO_TABELAS);
		ps.adicionarLong(GrupoDt.ESTATISTICA);
		ps.adicionarLong(GrupoDt.PUBLICO);
		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_VARA);
		ps.adicionarLong(GrupoDt.JUIZ_LEIGO);
		ps.adicionarLong(GrupoDt.ASSESSOR);
		ps.adicionarLong(GrupoDt.ASSESSOR_ADVOGADOS);
		ps.adicionarLong(GrupoDt.ASSESSOR_MP);
		ps.adicionarLong(GrupoDt.ASSESSOR_DESEMBARGADOR);
		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU);
		ps.adicionarLong(GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU);
		ps.adicionarLong(GrupoDt.CADASTRADOR_INTELIGENCIA);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N0);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N1);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N2);
		SqlOrder = " ORDER BY GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE"; 
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	/**
	 * Método que permite a localização de grupos para usuários Cadastrador Master. Impede que Cadastrador Master cadastre usuários com determinados
	 * perfis por questão de segurança.
	 * @param descricao - descrição do grupo
	 * @param posicao - posição
	 * @return lista de grupos localizados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarDescricaoGrupoCadastradorMasterJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		//seleciono os campos que vou mostrar
		Sql = "SELECT ID_GRUPO AS ID, GRUPO AS DESCRICAO1, SERV_TIPO AS DESCRICAO2 ";
		//uso essa string para a consulta que localiza os dados, tambem para a contagem dos registros
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO";
		SqlFrom += " WHERE GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND GRUPO_CODIGO NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		ps.adicionarLong(GrupoDt.ADMINISTRADORES);
		ps.adicionarLong(GrupoDt.PUBLICO);
		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_VARA);
		ps.adicionarLong(GrupoDt.JUIZ_LEIGO);
		ps.adicionarLong(GrupoDt.ASSESSOR);
		ps.adicionarLong(GrupoDt.ASSESSOR_ADVOGADOS);
		ps.adicionarLong(GrupoDt.ASSESSOR_MP);
		ps.adicionarLong(GrupoDt.ASSESSOR_DESEMBARGADOR);
		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU);
		ps.adicionarLong(GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N0);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N1);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N2);
		SqlOrder = " ORDER BY GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql  = "SELECT COUNT(1) as QUANTIDADE"; 
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarDescricaoGrupoCadastradorInteligenciaJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;
		
		//seleciono os campos que vou mostrar
		Sql = "SELECT ID_GRUPO AS ID, GRUPO AS DESCRICAO1, SERV_TIPO AS DESCRICAO2 ";
		//uso essa string para a consulta que localiza os dados, tambem para a contagem dos registros
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO";
		SqlFrom += " WHERE GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND GRUPO_CODIGO IN (?,?,?,?,?) ";
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N0);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N1);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N2);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N3);
		ps.adicionarLong(GrupoDt.INTELIGENCIA_N4);
		SqlOrder = " ORDER BY GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql  = "SELECT COUNT(1) as QUANTIDADE"; 
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarGruposHabilitacaoPromotoresJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_GRUPO AS ID, GRUPO AS DESCRICAO1, SERV_TIPO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO g";
		SqlFrom += " WHERE g.GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND (g.GRUPO_CODIGO in (?,?)"; ps.adicionarLong(GrupoDt.MP_TCE); ps.adicionarLong(GrupoDt.MINISTERIO_PUBLICO);
		SqlFrom += " OR g.GRUPO_CODIGO = ? )";
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		SqlOrder = " ORDER BY g.GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public String consultarGruposHabilitacaoServidoresJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_GRUPO AS ID, GRUPO AS DESCRICAO1, SERV_TIPO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO g";
		SqlFrom += " WHERE g.GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlFrom += " AND g.GRUPO_CODIGO <> ?"; 		ps.adicionarLong(GrupoDt.ADMINISTRADORES);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);		
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_DESEMBARGADOR);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_ADVOGADOS);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_MP);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_VARA);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.JUIZ_LEIGO);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.CADASTRADORES_TABELA);
		SqlFrom += " AND g.GRUPO_CODIGO <> ?";		ps.adicionarLong(GrupoDt.PUBLICO);
		SqlOrder = " ORDER BY g.GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}

	public String consultarGruposHabilitacaoSspJSON(String descricao, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_GRUPO AS ID, GRUPO AS DESCRICAO1, SERV_TIPO AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO g";
		SqlFrom += " WHERE g.GRUPO LIKE ?";				ps.adicionarString( descricao +"%");
		SqlFrom += " AND g.GRUPO_CODIGO in (?,?)"; 	ps.adicionarLong(GrupoDt.AUTORIDADES_POLICIAIS); ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_SSP);
		
		SqlOrder = " ORDER BY g.GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}

	public List consultarGruposHabilitacaoPoliciais(String descricao, String posicao) throws Exception {
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_GRUPO, GRUPO, SERV_TIPO_CODIGO, SERV_TIPO";
		SqlFrom = " FROM PROJUDI.VIEW_GRUPO g";
		SqlFrom += " WHERE g.GRUPO LIKE ?";						ps.adicionarString( descricao +"%");
		SqlFrom += " AND g.GRUPO_CODIGO in (?,?)";				ps.adicionarLong(GrupoDt.AUTORIDADES_POLICIAIS ); ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_SSP);
		
		SqlOrder = " ORDER BY g.GRUPO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				GrupoDt obTemp = new GrupoDt();
				obTemp.setId(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}
	
	public List consultarGruposUsuarioServentia(String idUsuarioServentia) throws Exception {
		String Sql;
		List tempList = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT G.GRUPO_CODIGO FROM USU_SERV_GRUPO USG INNER JOIN GRUPO G ON G.ID_GRUPO = USG.ID_GRUPO WHERE USG.ID_USU_SERV = ?";
		ps.adicionarLong(idUsuarioServentia);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				tempList.add(rs1.getString("GRUPO_CODIGO"));		
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} finally {}
        } 
		return tempList;
	}
}
