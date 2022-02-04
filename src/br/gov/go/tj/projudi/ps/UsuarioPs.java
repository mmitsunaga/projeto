package br.gov.go.tj.projudi.ps;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioUltimoLoginDt;
import br.gov.go.tj.projudi.dt.relatorios.DetalhesUsuarioDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;

//---------------------------------------------------------
public class UsuarioPs extends UsuarioPsGen implements Serializable{

	private static final long serialVersionUID = 8788040468975170038L;
	
	public UsuarioPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Consulta Sub-Menu
	 * @param id_usuarioserventia
	 * @param id_permissaopai
	 * @param id_grupo
	 * @return
	 * @throws Exception
	 */
	private String getSubMenu(String id_usuarioserventia, int id_permissaopai, String id_grupo) throws Exception{
		String stMenu = "";
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{
			stSql = "(SELECT * ";
			stSql += "FROM PROJUDI.VIEW_SUB_MENU_COMPOSTO m ";
			stSql += "INNER JOIN PROJUDI.VIEW_USU_PERM_GERAL_SUB_MENU upg ";
			stSql += "on m.ID_PERM = upg.ID_PERM ";
			stSql += "WHERE upg.ID_USU_SERV = ? AND m.ID_PERM_PAI= ? ";			ps.adicionarLong(id_usuarioserventia);			ps.adicionarLong(id_permissaopai);
			stSql += " AND (upg.ID_GRUPO=-100 OR upg.ID_GRUPO= ?))";			ps.adicionarLong(id_grupo);
			stSql += " UNION ";
			stSql += " (SELECT * ";
			stSql += "FROM PROJUDI.VIEW_SUB_MENU_SIMPLES m ";
			stSql += "INNER JOIN PROJUDI.VIEW_USU_PERM_GERAL_SUB_MENU upg ";
			stSql += "on m.ID_PERM = upg.ID_PERM ";
			stSql += "WHERE upg.ID_USU_SERV = ? AND m.ID_PERM_PAI= ? ";			ps.adicionarLong(id_usuarioserventia);			ps.adicionarLong(id_permissaopai);
			stSql += " AND (upg.ID_GRUPO=-100 OR upg.ID_GRUPO=?))";				ps.adicionarLong(id_grupo);
			stSql += " ORDER BY m.ORDENACAO";

			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				if (rs1.getString("MENU").indexOf("{SubMenu}") != 0) stMenu += rs1.getString("MENU").replace("{SubMenu}", getSubMenu(id_usuarioserventia, rs1.getInt("ID_PERM"), id_grupo));
				else stMenu += rs1.getString("MENU");
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stMenu;

	}

	/**
	 * Consulta Menu
	 * @param id_usuarioserventia
	 * @param id_grupo
	 * @return
	 * @throws Exception
	 */
	public String getMenu(String id_usuarioserventia, String id_grupo) throws Exception {
		String stMenu = "";
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{
			stSql = "SELECT * ";
			stSql += "FROM PROJUDI.VIEW_MENU_SIMPLES m ";
			stSql += "INNER JOIN PROJUDI.VIEW_USU_PERM_GERAL upg ";
			stSql += "on m.ID_PERM = upg.ID_PERM ";
			stSql += "WHERE upg.ID_PERM_ESPECIAL IS NULL AND upg.ID_USU_SERV = ? AND (upg.ID_GRUPO=-100 OR upg.ID_GRUPO = ?)";			
			//ps.adicionarLong(id_usuarioserventia);
			//ps.adicionarLong(id_grupo);
			stSql += " UNION ";
			stSql = " SELECT * ";
			stSql += "FROM PROJUDI.VIEW_MENU_COMPOSTO m ";
			stSql += "INNER JOIN PROJUDI.VIEW_USU_PERM_GRUPO_GERAL upg ";
			stSql += "on m.ID_PERM = upg.ID_PERM ";
			stSql += "WHERE upg.ID_PERM_ESPECIAL IS NULL AND upg.ID_USU_SERV = ? AND (upg.ID_GRUPO=-100 OR upg.ID_GRUPO = ?)";
			ps.adicionarLong(id_usuarioserventia);
			ps.adicionarLong(id_grupo);

			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				stMenu += rs1.getString("MENU").replace("{SubMenu}", getSubMenu(id_usuarioserventia, rs1.getInt("ID_PERM"), id_grupo));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stMenu;

	}

	/**
	 * Consulta o Menu Especial de acordo com o tipo passado
	 * (permissaoEspecialCodigo) e com o usuário logado e seu respectivo grupo
	 */
	public String getMenuEspecial(String id_usuarioserventia, String id_grupo, int permissaoEspecialCodigo) throws Exception {
		String stMenu = "";
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{
			stSql = "SELECT * ";
			stSql += "FROM PROJUDI.VIEW_MENU_SIMPLES m ";
			stSql += "INNER JOIN PROJUDI.VIEW_USU_PERM_GERAL upg on m.ID_PERM = upg.ID_PERM ";
			stSql += "INNER JOIN PROJUDI.PERM_ESPECIAL pe on upg.ID_PERM_ESPECIAL = pe.ID_PERM_ESPECIAL AND pe.PERM_ESPECIAL_CODIGO = ? ";
			ps.adicionarLong(permissaoEspecialCodigo);
			stSql += " WHERE upg.ID_USU_SERV = ? AND (upg.ID_GRUPO=-100 OR upg.ID_GRUPO=?)";
			ps.adicionarLong(id_usuarioserventia);
			ps.adicionarLong(id_grupo);
			stSql += " UNION ";
			stSql += "SELECT * ";
			stSql += "FROM PROJUDI.VIEW_MENU_COMPOSTO m ";
			stSql += "INNER JOIN PROJUDI.VIEW_USU_PERM_GRUPO_GERAL upg on m.ID_PERM = upg.ID_PERM ";
			stSql += "INNER JOIN PROJUDI.PERM_ESPECIAL pe on upg.ID_PERM_ESPECIAL = pe.ID_PERM_ESPECIAL AND pe.PERM_ESPECIAL_CODIGO = ? ";
			ps.adicionarLong(permissaoEspecialCodigo);
			stSql += " WHERE upg.ID_USU_SERV = ? AND (upg.ID_GRUPO=-100 OR upg.ID_GRUPO=?)";
			ps.adicionarLong(id_usuarioserventia);
			ps.adicionarLong(id_grupo);

			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				stMenu += rs1.getString("MENU").replace("{SubMenu}", getSubMenu(id_usuarioserventia, rs1.getInt("ID_PERM"), id_grupo));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return stMenu;

	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stSqlFrom ="";
		String stSqlOrder ="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_USU AS ID, USU AS DESCRICAO1";
		stSqlFrom = " FROM PROJUDI.VIEW_USU";
		stSqlFrom += " WHERE USU LIKE ?";
		stSqlOrder = " ORDER BY USU ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql + stSqlFrom + stSqlOrder, ps, posicao);

			stSql= "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(stSql + stSqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;  
	}
	
	public BitSet ConsultaUsuarioPermissoes(String id_usuarioserventia, String id_grupo) throws Exception {
		BitSet Permissoes = new BitSet(8000);
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{
			         		
			stSql = " SELECT PERM_CODIGO FROM PROJUDI.VIEW_USU_PERM_GRUPO_GERAL  WHERE ID_USU_SERV = ? and id_grupo = ? "; 					ps.adicionarLong(id_usuarioserventia); 			ps.adicionarLong(id_grupo);
			stSql += " UNION ";
			stSql += " Select Perm_Codigo  From Grupo_Perm G Inner Join Perm P  On P.Id_Perm =G.Id_Perm 	 where g.id_grupo = ?"; 		ps.adicionarLong(GrupoDt.ID_GRUPO_PUBLICO);

			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				Permissoes.set(rs1.getInt("PERM_CODIGO"), true);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Permissoes;

	}

	/**
	 * Método para consultar um advogado de acordo com a oab e tipo da serventia
	 * passada
	 * 
	 * @param oabNumero, número da OAB
	 * @param oabComplemento, complemento da OAB
	 * @param oabEstado, estado da OAB
	 * @param serventiaTipo, define o tipo do advogado a ser consultado
	 */
	public List consultarAdvogadoOab(String oabNumero, String oabComplemento, String oabEstado, int serventiaTipo) throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = " SELECT us.ID_USU_SERV, u.NOME, uo.OAB_NUMERO, uo.OAB_COMPLEMENTO, e.UF, s.SERV FROM PROJUDI.USU_SERV us";
		sql += " JOIN PROJUDI.USU u on u.ID_USU=us.ID_USU ";
		sql += " JOIN PROJUDI.SERV s on s.ID_SERV=us.ID_SERV";
		sql += " JOIN PROJUDI.ESTADO e on e.ID_ESTADO=s.ID_ESTADO_REPRESENTACAO";
		sql += " JOIN PROJUDI.SERV_TIPO st on st.ID_SERV_TIPO=s.ID_SERV_TIPO";
		sql += " LEFT JOIN PROJUDI.USU_SERV_OAB uo on uo.ID_USU_SERV=us.ID_USU_SERV";
		sql += " WHERE uo.OAB_NUMERO = ? ";					ps.adicionarLong(oabNumero);
		sql += " AND uo.OAB_COMPLEMENTO= ? ";				ps.adicionarString(oabComplemento);
		sql += " AND e.UF= ? ";								ps.adicionarString(oabEstado);
		sql += " AND st.SERV_TIPO_CODIGO= ? ";				ps.adicionarLong(serventiaTipo);
		sql += " AND u.ATIVO = 1 AND us.ATIVO = 1 ";		

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				UsuarioDt usuarioDt = new UsuarioDt();
				usuarioDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				usuarioDt.setServentia(rs1.getString("SERV"));
				usuarioDt.setNome(rs1.getString("NOME"));
				usuarioDt.setOabNumero(rs1.getString("OAB_NUMERO"));
				usuarioDt.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				usuarioDt.setOabEstado(rs1.getString("UF"));
	
				liTemp.add(usuarioDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Método para consultar um advogado de acordo com a oab e tipo da serventia
	 * passada
	 * 
	 * @param oabNumero, número da OAB
	 * @param oabComplemento, complemento da OAB
	 * @param oabEstado, estado da OAB
	 */
	public List consultarAdvogadoOab(String oabNumero, String oabComplemento, String oabEstado) throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = " SELECT us.ID_USU_SERV, u.NOME, uo.OAB_NUMERO, uo.OAB_COMPLEMENTO, e.UF, s.SERV FROM PROJUDI.USU_SERV us";
		sql += " JOIN PROJUDI.USU u on u.ID_USU=us.ID_USU ";
		sql += " JOIN PROJUDI.SERV s on s.ID_SERV=us.ID_SERV";
		sql += " JOIN PROJUDI.ESTADO e on e.ID_ESTADO=s.ID_ESTADO_REPRESENTACAO";
		sql += " JOIN PROJUDI.SERV_TIPO st on st.ID_SERV_TIPO=s.ID_SERV_TIPO";
		sql += " LEFT JOIN PROJUDI.USU_SERV_OAB uo on uo.ID_USU_SERV=us.ID_USU_SERV";
		sql += " WHERE uo.OAB_NUMERO = ? ";
		ps.adicionarLong(oabNumero);
		sql += " AND uo.OAB_COMPLEMENTO= ? ";		
		ps.adicionarString(oabComplemento);
		sql += " AND e.UF= ? ";		
		ps.adicionarString(oabEstado);
		sql += " AND u.ATIVO = 1 AND us.ATIVO = 1 ";		

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				UsuarioDt usuarioDt = new UsuarioDt();
				usuarioDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				usuarioDt.setServentia(rs1.getString("SERV"));
				usuarioDt.setNome(rs1.getString("NOME"));
				usuarioDt.setOabNumero(rs1.getString("OAB_NUMERO"));
				usuarioDt.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				usuarioDt.setOabEstado(rs1.getString("UF"));
	
				liTemp.add(usuarioDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Método para consultar um promotor (Substituto processual) de acordo com serventia passada
	 * 
	 * @param id_Serv, identificador da serventia
	 * 
	 */
	public List consultarPromotorSubstitutoProcessual(String id_Serv) throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = " SELECT us.ID_USU_SERV, u.NOME, s.SERV FROM PROJUDI.USU_SERV us";
		sql += " JOIN PROJUDI.USU u on u.ID_USU=us.ID_USU ";
		sql += " JOIN PROJUDI.SERV s on s.ID_SERV=us.ID_SERV";
		sql += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		sql += " JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		sql += " WHERE s.ID_SERV = ? and g.GRUPO_CODIGO = ? ";
		ps.adicionarLong(id_Serv); ps.adicionarLong(GrupoDt.MINISTERIO_PUBLICO);
		sql += " AND u.ATIVO = 1 AND us.ATIVO = 1 ";		

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				UsuarioDt usuarioDt = new UsuarioDt();
				usuarioDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				usuarioDt.setServentia(rs1.getString("SERV"));
				usuarioDt.setNome(rs1.getString("NOME"));
				usuarioDt.setOabNumero("MP");
				usuarioDt.setOabEstado("GO");
				usuarioDt.setOabComplemento("GO");
	
				liTemp.add(usuarioDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Retorna usuários administradores
	 * @return
	 * @throws Exception
	 */
	public List getAdministradores() throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_USU WHERE ";
		Sql += " AND  USU_TIPO_CODIGO = ? ";
		ps.adicionarLong(br.gov.go.tj.projudi.dt.GrupoDt.ADMINISTRADORES);
		Sql += " AND  ATIVO = 1 ";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta todos usuários cadastrados no sistema de acordo com os filtros
	 * passados
	 * 
	 * @param grupo, grupo a ser filtrado
	 * @param descricao, nome do usuário a ser pesquisado
	 */
	public List consultarUsuarios(String grupo, String serventia, String descricao) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT DISTINCT u.ID_USU,u.NOME, u.USU, u.MATRICULA_TJGO,u.EMAIL,u.TELEFONE, u.RG, u.CPF, u.ATIVO,";
		Sql += " g.ID_GRUPO,g.GRUPO_CODIGO,g.GRUPO, usoab.OAB_NUMERO, usoab.OAB_COMPLEMENTO ";
		Sql += " FROM  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  LEFT JOIN PROJUDI.USU_SERV_OAB usoab on us.ID_USU_SERV = usoab.ID_USU_SERV  ";
		Sql += " WHERE u.NOME LIKE ? ";				ps.adicionarString( descricao +"%");
		if (grupo != null && grupo.length() > 0){
			Sql += "  AND g.ID_GRUPO  = ? ";
			ps.adicionarLong(grupo);
		}
		if (serventia != null && serventia.length() > 0){
			Sql += "  AND us.ID_SERV  = ? ";
			ps.adicionarLong(serventia);
		}
		Sql += " AND us.ATIVO = 1 AND sg.ATIVO = 1";
		Sql += " ORDER BY u.NOME  ";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				DetalhesUsuarioDt obTemp = new DetalhesUsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setEmail(rs1.getString("EMAIL"));
				obTemp.setTelefone(rs1.getString("TELEFONE"));
				obTemp.setGrupo(rs1.getString("GRUPO"));

				String grupoCodigo = rs1.getString("GRUPO_CODIGO");
				if (grupoCodigo.equalsIgnoreCase(String.valueOf(GrupoDt.ADVOGADO_PARTICULAR))
						|| grupoCodigo.equalsIgnoreCase(String.valueOf(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO))
						|| grupoCodigo.equalsIgnoreCase(String.valueOf(GrupoDt.ADVOGADO_PUBLICO))
						|| grupoCodigo.equalsIgnoreCase(String.valueOf(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL))
						|| grupoCodigo.equalsIgnoreCase(String.valueOf(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL))
						|| grupoCodigo.equalsIgnoreCase(String.valueOf(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO))
						|| grupoCodigo.equalsIgnoreCase(String.valueOf(GrupoDt.ADVOGADO_PUBLICO_UNIAO))) {
					obTemp.setDoc(rs1.getString("OAB_NUMERO") + " - " + rs1.getString("OAB_COMPLEMENTO"));
				} else {
					if (rs1.getString("MATRICULA_TJGO") != null) obTemp.setDoc(rs1.getString("MATRICULA_TJGO"));
					else obTemp.setDoc(rs1.getString("CPF"));
				}
				if (rs1.getString("ATIVO").equalsIgnoreCase("1")) obTemp.setSituacao("A");
				else obTemp.setSituacao("I");
				liTemp.add(obTemp);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Ativa ou Desativa um usuário
	 * 
	 * @param usuarioDt, dt de usuário
	 * 
	 * @author msapaula
	 */
	public void alterarStatusUsuario(String id_Usuario, int novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU SET ATIVO = ? ";
		ps.adicionarLong(novoStatus);
		Sql += " WHERE ID_USU = ? ";
		ps.adicionarLong(id_Usuario);

		
		executarUpdateDelete(Sql, ps);

	}

	/**
	 * Método que verifica se existe um usuário ativo de acordo com login e
	 * senha passados
	 * 
	 * @param usuario, usuário digitado
	 * @param senha, senha digitada
	 */
	public UsuarioDt logarUsuarioSenha(String usuario, String senha) throws Exception {
		String stSql;
		ResultSetTJGO rs1 = null;
		UsuarioDt tempdtUsuario = new UsuarioDt();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSql = "SELECT * FROM PROJUDI.VIEW_USU u  WHERE u.USU = ? ";				ps.adicionarString(usuario);
		stSql += " AND u.SENHA = ? ";												ps.adicionarString(Funcoes.SenhaMd5(senha));
		stSql += " AND u.ATIVO = 1";

		rs1 = consultar(stSql, ps);

		if (rs1.next()) {
			tempdtUsuario.setId(rs1.getString("ID_USU"));
			tempdtUsuario.setUsuario(rs1.getString("USU"));
			tempdtUsuario.setSenha(rs1.getString("SENHA"));
			tempdtUsuario.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			if (rs1.next()) throw new MensagemException("Erro no Logon, mais de um logon encontrado");
		}
		
		
		return tempdtUsuario;

	}

	/**
	 * Método que verifica se existe um usuário ativo/inativo de acordo com login e
	 * senha passados, não deve ser utilizado para o login
	 * 
	 * @param usuario, usuário digitado
	 * @param senha, senha digitada
	 * jrcorrea
	 * 2508/2016
	 */
	public UsuarioDt verificarUsuarioSenha(String usuario, String senha) throws Exception {
		String stSql;
		ResultSetTJGO rs1 = null;
		UsuarioDt tempdtUsuario = new UsuarioDt();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSql = "SELECT * FROM PROJUDI.VIEW_USU u  WHERE u.USU = ? ";
		ps.adicionarString(usuario);
		stSql += " AND u.SENHA = ? ";
		ps.adicionarString(Funcoes.SenhaMd5(senha));		

		rs1 = consultar(stSql, ps);

		if (rs1.next()) {
			tempdtUsuario.setId(rs1.getString("ID_USU"));
			tempdtUsuario.setUsuario(rs1.getString("USU"));
			tempdtUsuario.setSenha(rs1.getString("SENHA"));
			tempdtUsuario.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			if (rs1.next()) throw new MensagemException("Erro no Logon, mais de um logon encontrado");
		}
		
		
		return tempdtUsuario;

	}
	/**
	 * Sobrescrevendo método inserir
	 */
	public void inserir(UsuarioDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		dados.setCodigoTemp(String.valueOf(Math.round(Math.random() * 100000)));

		SqlCampos = "INSERT INTO PROJUDI.USU (";
		SqlValores = " Values (";
		if (!(dados.getId_CtpsUf().length() == 0)){
			SqlCampos += ",ID_CTPS_UF";
			SqlValores+= "?";
			ps.adicionarLong(dados.getId_CtpsUf());
		}
		if (!(dados.getNome().length() == 0)){
			SqlCampos += ",NOME";
			SqlValores+= ",?";
			ps.adicionarString(dados.getNome());
		}
		if (!(dados.getSexo().length() == 0)){
			SqlCampos += ",SEXO";
			SqlValores+= ",?";
			ps.adicionarString(dados.getSexo());
		}
		if (!(dados.getDataNascimento().length() == 0)){
			SqlCampos += ",DATA_NASCIMENTO";
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataNascimento());
		}
		if (!(dados.getId_Naturalidade().length() == 0)){
			SqlCampos += ",ID_NATURALIDADE";
			SqlValores+= ",?";
			ps.adicionarLong(dados.getId_Naturalidade());
		}
		if (!(dados.getTelefone().length() == 0)){
			SqlCampos += ",TELEFONE";
			SqlValores+= ",?";
			ps.adicionarString(dados.getTelefone());
		}
		if (!(dados.getCelular().length() == 0)){
			SqlCampos += ",CELULAR";
			SqlValores+= ",?";
			ps.adicionarString(dados.getCelular());
		}
		if (!(dados.getEMail().length() == 0)){
			SqlCampos += ",EMAIL";
			SqlValores+= ",?";
			ps.adicionarString(dados.getEMail());
		}
		if (!(dados.getId_Endereco().length() == 0)){
			SqlCampos += ",ID_ENDERECO";
			SqlValores+= ",?";
			ps.adicionarLong(dados.getId_Endereco());
		}
		if (!(dados.getRg().length() == 0)){
			SqlCampos += ",RG";
			SqlValores+= ",?";
			ps.adicionarString(dados.getRg());
		}
		if (!(dados.getId_RgOrgaoExpedidor().length() == 0)){
			SqlCampos += ",ID_RG_ORGAO_EXP";
			SqlValores+= ",?";
			ps.adicionarLong(dados.getId_RgOrgaoExpedidor());
		}
		if (!(dados.getRgDataExpedicao().length() == 0)){
			SqlCampos += ",RG_DATA_EXPEDICAO";
			SqlValores+= ",?";
			ps.adicionarDate(dados.getRgDataExpedicao());
		}
		if (!(dados.getCpf().length() == 0)){
			SqlCampos += ",CPF";
			SqlValores+= ",?";
			ps.adicionarString(dados.getCpf());
		}
		if (!(dados.getTituloEleitor().length() == 0)){
			SqlCampos += ",TITULO_ELEITOR";
			SqlValores+= ",?";
			ps.adicionarString(dados.getTituloEleitor());
		}
		if (!(dados.getTituloEleitorZona().length() == 0)){
			SqlCampos += ",TITULO_ELEITOR_ZONA";
			SqlValores+= ",?";
			ps.adicionarString(dados.getTituloEleitorZona());
		}
		if (!(dados.getTituloEleitorSecao().length() == 0)){
			SqlCampos += ",TITULO_ELEITOR_SECAO";
			SqlValores+= ",?";
			ps.adicionarString(dados.getTituloEleitorSecao());
		}
		if (!(dados.getCtps().length() == 0)){
			SqlCampos += ",CTPS";
			SqlValores+= ",?";
			ps.adicionarLong(dados.getCtps());
		}
		if (!(dados.getCtpsSerie().length() == 0)){
			SqlCampos += ",CTPS_SERIE";
			SqlValores+= ",?";
			ps.adicionarLong(dados.getCtpsSerie());
		}
		if (!(dados.getPis().length() == 0)){
			SqlCampos += ",PIS";
			SqlValores+= ",?";
			ps.adicionarLong(dados.getPis());
		}
		if (!(dados.getMatriculaTjGo().length() == 0)){
			SqlCampos += ",MATRICULA_TJGO";
			SqlValores+= ",?";
			ps.adicionarLong(dados.getMatriculaTjGo());
		}
		if (!(dados.getNumeroConciliador().length() == 0)){
			SqlCampos += ",NUMERO_CONCILIADOR";
			SqlValores+= ",?";
			ps.adicionarLong(dados.getNumeroConciliador());
		}
		if (!(dados.getUsuario().length() == 0)){
			SqlCampos += ",USU";
			SqlValores+= ",?";
			ps.adicionarString(dados.getUsuario());
		}
		if (!(dados.getSenha().length() == 0)){
			SqlCampos += ",SENHA";
			SqlValores+= ",?";
			ps.adicionarString(Funcoes.SenhaMd5(dados.getSenha()));
		}
		if (!(dados.getDataAtivo().length() == 0)){
			SqlCampos += ",DATA_ATIVO";
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataAtivo());
		}
		if (!(dados.getDataExpiracao().length() == 0)){
			SqlCampos += ",DATA_EXPIRACAO";
			SqlValores+= ",?";
			ps.adicionarDate(dados.getDataExpiracao());
		}
		if (!(dados.getAtivo().length() == 0)){
			SqlCampos += ",ATIVO";
			SqlValores+= ",?";
			ps.adicionarBoolean(dados.getAtivo());
		}
		SqlCampos += ",DATA_CADASTRO";
		SqlValores+= ",?";
		ps.adicionarDateTime(new Date());
		SqlCampos += ",CODIGO_TEMP";
		SqlValores+= ",?";
		ps.adicionarLong(dados.getCodigoTemp());
		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");
		
		dados.setId(executarInsert(Sql, "ID_USU", ps));


	}

	/**
	 * Sobrescrevendo método alterar
	 */
	public void alterar(UsuarioDt dados) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU SET  ";
		Sql += ",ID_CTPS_UF = ? ";
		ps.adicionarLong(dados.getId_CtpsUf());
		Sql += ",NOME = ? ";
		ps.adicionarString(dados.getNome());
		Sql += ",SEXO = ? ";
		ps.adicionarString(dados.getSexo());
		Sql += ",DATA_NASCIMENTO = ? ";
		ps.adicionarDate(dados.getDataNascimento());
		Sql += ",ID_NATURALIDADE = ? ";
		ps.adicionarLong(dados.getId_Naturalidade());
		Sql += ",TELEFONE = ? ";
		ps.adicionarString(dados.getTelefone());
		Sql += ",CELULAR = ? ";
		ps.adicionarString(dados.getCelular());
		Sql += ",ID_ENDERECO = ? ";
		ps.adicionarLong(dados.getId_Endereco());
		Sql += ",EMAIL = ? ";
		ps.adicionarString(dados.getEMail());
		Sql += ",RG = ? ";
		ps.adicionarString(dados.getRg());
		Sql += ",ID_RG_ORGAO_EXP = ? ";
		ps.adicionarLong(dados.getId_RgOrgaoExpedidor());
		Sql += ",RG_DATA_EXPEDICAO = ? ";
		ps.adicionarDate(dados.getRgDataExpedicao());
		Sql += ",CPF = ? ";
		ps.adicionarString(dados.getCpf());
		Sql += ",TITULO_ELEITOR = ? ";
		ps.adicionarString(dados.getTituloEleitor());
		Sql += ",TITULO_ELEITOR_ZONA = ? ";
		ps.adicionarLong(dados.getTituloEleitorZona());
		Sql += ",TITULO_ELEITOR_SECAO = ? ";
		ps.adicionarLong(dados.getTituloEleitorSecao());
		Sql += ",CTPS = ? ";
		ps.adicionarLong(dados.getCtps());
		Sql += ",CTPS_SERIE = ? ";
		ps.adicionarLong(dados.getCtpsSerie());
		Sql += ",PIS = ? ";
		ps.adicionarLong(dados.getPis());
		Sql += ",MATRICULA_TJGO = ? ";
		ps.adicionarLong(dados.getMatriculaTjGo());
		Sql += ",NUMERO_CONCILIADOR = ? ";
		ps.adicionarLong(dados.getNumeroConciliador());
		Sql += ",USU = ? ";
		ps.adicionarString(dados.getUsuario());
		Sql += ",DATA_ATIVO = ? ";
		ps.adicionarDate(dados.getDataAtivo());
		Sql += ",DATA_EXPIRACAO = ? ";
		ps.adicionarDate(dados.getDataExpiracao());
		Sql += ",ATIVO = ? ";
		ps.adicionarBoolean(dados.getAtivo());
		Sql = Sql + ",CODIGO_TEMP = null";
		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_USU = ? ";
		ps.adicionarLong(dados.getId());
		
		executarUpdateDelete(Sql, ps);
		
	}

	/**
	 * Consultar os dados completos de um usuário para possibilitar alteração
	 * dos mesmos
	 * 
	 * @param id_usuario,
	 *            identificação do usuário
	 * 
	 * @author msapaula
	 */
	public UsuarioDt consultarUsuarioCompleto(String id_usuario) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.*, e.LOGRADOURO, e.NUMERO, e.COMPLEMENTO, e.ID_BAIRRO, e.BAIRRO, e.CIDADE, e.UF, e.CEP, ";
		Sql += " g.GRUPO, g.GRUPO_CODIGO ";
		Sql += " FROM PROJUDI.VIEW_USU u  ";
		Sql += " JOIN PROJUDI.VIEW_ENDERECO e ON u.ID_ENDERECO = e.ID_ENDERECO";
		Sql += " JOIN PROJUDI.USU_SERV us on u.ID_USU = us.ID_USU";
		Sql += " JOIN PROJUDI.USU_SERV_GRUPO ug on us.ID_USU_SERV=ug.ID_USU_SERV";
		Sql += " JOIN PROJUDI.GRUPO g on g.ID_GRUPO = ug.ID_GRUPO";
		Sql += " WHERE u.ID_USU = ? ";
		ps.adicionarLong(id_usuario);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setNaturalidade(rs1.getString("NATURALIDADE"));
				Dados.setEndereco(rs1.getString("ENDERECO"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP_SIGLA"));
				Dados.setSenha(rs1.getString("SENHA"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setRg(rs1.getString("RG"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				Dados.setDataAtivo(Funcoes.FormatarData(rs1.getDateTime("DATA_ATIVO")));
				Dados.setDataExpiracao(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.setGrupo(rs1.getString("GRUPO"));
				Dados.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				Dados.getEnderecoUsuario().setLogradouro(rs1.getString("LOGRADOURO"));
				Dados.getEnderecoUsuario().setNumero(rs1.getString("NUMERO"));
				Dados.getEnderecoUsuario().setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.getEnderecoUsuario().setId_Bairro(rs1.getString("ID_BAIRRO"));
				Dados.getEnderecoUsuario().setBairro(rs1.getString("BAIRRO"));
				Dados.getEnderecoUsuario().setCidade(rs1.getString("CIDADE"));
				Dados.getEnderecoUsuario().setUf(rs1.getString("UF"));
				Dados.getEnderecoUsuario().setCep(rs1.getString("CEP"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}
	
	public UsuarioDt consultarUsuarioCompletoPJD(String id_usuario) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.*, e.LOGRADOURO, e.NUMERO, e.COMPLEMENTO, e.ID_BAIRRO, e.BAIRRO, e.CIDADE, e.UF, e.CEP, us.*,  ";
		Sql += " g.GRUPO, g.GRUPO_CODIGO ";
		Sql += " FROM PROJUDI.VIEW_USU u  ";
		Sql += " JOIN PROJUDI.VIEW_ENDERECO e ON u.ID_ENDERECO = e.ID_ENDERECO";
		Sql += " JOIN PROJUDI.USU_SERV us on u.ID_USU = us.ID_USU";
		Sql += " JOIN PROJUDI.USU_SERV_GRUPO ug on us.ID_USU_SERV=ug.ID_USU_SERV";
		Sql += " JOIN PROJUDI.GRUPO g on g.ID_GRUPO = ug.ID_GRUPO";
		Sql += " WHERE u.ID_USU = ? ";
		ps.adicionarLong(id_usuario);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));;
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setNaturalidade(rs1.getString("NATURALIDADE"));
				Dados.setEndereco(rs1.getString("ENDERECO"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP_SIGLA"));
				Dados.setSenha(rs1.getString("SENHA"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setRg(rs1.getString("RG"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				Dados.setDataAtivo(Funcoes.FormatarData(rs1.getDateTime("DATA_ATIVO")));
				Dados.setDataExpiracao(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.setGrupo(rs1.getString("GRUPO"));
				Dados.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				Dados.getEnderecoUsuario().setLogradouro(rs1.getString("LOGRADOURO"));
				Dados.getEnderecoUsuario().setNumero(rs1.getString("NUMERO"));
				Dados.getEnderecoUsuario().setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.getEnderecoUsuario().setId_Bairro(rs1.getString("ID_BAIRRO"));
				Dados.getEnderecoUsuario().setBairro(rs1.getString("BAIRRO"));
				Dados.getEnderecoUsuario().setCidade(rs1.getString("CIDADE"));
				Dados.getEnderecoUsuario().setUf(rs1.getString("UF"));
				Dados.getEnderecoUsuario().setCep(rs1.getString("CEP"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	/**
	 * Consultar os dados completos de um advogado, inclusive endereço
	 * 
	 * @param id_usuario,
	 *            identificação do advogado
	 */
	public UsuarioDt consultarAdvogadoId(String id_usuario) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.*, e.LOGRADOURO, e.NUMERO, e.COMPLEMENTO, e.ID_BAIRRO, e.BAIRRO, e.CIDADE, e.UF, e.CEP ";
		Sql += " FROM PROJUDI.VIEW_USU u  ";
		Sql += " JOIN PROJUDI.VIEW_ENDERECO e ON u.ID_ENDERECO = e.ID_ENDERECO";
		Sql += " WHERE u.ID_USU = ? ";
		ps.adicionarLong(id_usuario);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setNaturalidade(rs1.getString("NATURALIDADE"));
				Dados.setEndereco(rs1.getString("ENDERECO"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP_SIGLA"));
				Dados.setSenha(rs1.getString("SENHA"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setRg(rs1.getString("RG"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));

				Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				Dados.setDataAtivo(Funcoes.FormatarData(rs1.getDateTime("DATA_ATIVO")));
				Dados.setDataExpiracao(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.getEnderecoUsuario().setLogradouro(rs1.getString("LOGRADOURO"));
				Dados.getEnderecoUsuario().setNumero(rs1.getString("NUMERO"));
				Dados.getEnderecoUsuario().setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.getEnderecoUsuario().setId_Bairro(rs1.getString("ID_BAIRRO"));
				Dados.getEnderecoUsuario().setBairro(rs1.getString("BAIRRO"));
				Dados.getEnderecoUsuario().setCidade(rs1.getString("CIDADE"));
				Dados.getEnderecoUsuario().setUf(rs1.getString("UF"));
				Dados.getEnderecoUsuario().setCep(rs1.getString("CEP"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}
	
	/**
	 * Consulta os dados do Desembargador baseado no Id da Serventia informado. 
	 * @param idServentia - ID da Serventia
	 * @return Id do usuário desembargador
	 */
	public String consultarIdServentiaCargoDesembargadorServentia(String idServentia) throws Exception {
		String Sql;
		String idServentiaCargo = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT sc.ID_SERV_CARGO FROM PROJUDI.SERV_CARGO sc " +
			" INNER JOIN PROJUDI.USU_SERV_GRUPO usg ON usg.ID_USU_SERV_GRUPO = sc.ID_USU_SERV_GRUPO " +
			" INNER JOIN PROJUDI.USU_SERV us ON us.ID_USU_SERV=usg.ID_USU_SERV " +
			" WHERE us.ID_SERV = ? AND usg.ID_GRUPO = ? ";
		ps.adicionarLong(idServentia);
		ps.adicionarLong(GrupoDt.DESEMBARGADOR);
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				idServentiaCargo = rs1.getString("ID_SERV_CARGO");
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return idServentiaCargo;
	}
	
	
	/**
	 * Consulta um usuário para envio de e-mail baseado no id_UsuarioServentia passado.
	 * O objeto UsuarioDt retornado está incompleto.
	 * 
	 * @param id_UsuarioServentia
	 */
	public UsuarioDt consultarUsuarioServentiaIdParaEnvioDeEmail(String id_UsuarioServentia) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.ID_USU, u.USU, u.NOME, u.EMAIL, us.ID_USU_SERV, us.ID_SERV, us.ATIVO as USU_SERV_ATIVO ";
		Sql += " FROM  PROJUDI.USU u  INNER JOIN PROJUDI.USU_SERV us ON u.ID_USU = us.ID_USU ";
		Sql += " WHERE us.ID_USU_SERV = ? ";
		ps.adicionarLong(id_UsuarioServentia);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_ATIVO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setId_Serventia(rs1.getString("ID_SERV"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}
	
	/**
	 * Consulta um Advogado baseado no id_UsuarioServentia passado
	 * 
	 * @param id_UsuarioServentia
	 */
	public UsuarioDt consultarAdvogadoServentiaId(String id_UsuarioServentia) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.*, us.ID_USU_SERV, s.ID_SERV, s.SERV, us.ATIVO as USU_SERV_ATIVO,  ";
		Sql += " e.LOGRADOURO, e.NUMERO, e.COMPLEMENTO,e.ID_BAIRRO, e.BAIRRO, e.CIDADE, e.UF, e.CEP,";
		Sql += " es.UF as UF_SERV, usoab.ID_USU_SERV_OAB, usoab.OAB_NUMERO, usoab.OAB_COMPLEMENTO, usoab.USU_MASTER, sg.ID_USU_SERV_GRUPO   ";
		Sql += " FROM  PROJUDI.VIEW_USU u  ";
		Sql += "  JOIN PROJUDI.VIEW_ENDERECO e ON u.ID_ENDERECO = e.ID_ENDERECO";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";		
		Sql += "  JOIN PROJUDI.USU_SERV_OAB usoab on us.ID_USU_SERV = usoab.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.ESTADO es on es.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += "  LEFT JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += " WHERE us.ID_USU_SERV = ? ";
		ps.adicionarLong(id_UsuarioServentia);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				Dados.setUsuarioServentiaAtivo(rs1.getString("USU_SERV_ATIVO"));
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setNaturalidade(rs1.getString("NATURALIDADE"));
				Dados.setEndereco(rs1.getString("ENDERECO"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP_SIGLA"));
				Dados.setSenha(rs1.getString("SENHA"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setRg(rs1.getString("RG"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));
				Dados.getUsuarioServentiaOab().setId(rs1.getString("ID_USU_SERV_OAB"));
				Dados.getUsuarioServentiaOab().setOabNumero(rs1.getString("OAB_NUMERO"));
				Dados.getUsuarioServentiaOab().setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				Dados.setDataAtivo(Funcoes.FormatarData(rs1.getDateTime("DATA_ATIVO")));
				Dados.setDataExpiracao(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setId_Serventia(rs1.getString("ID_SERV"));
				Dados.setServentia(rs1.getString("SERV"));
				Dados.setServentiaUf(rs1.getString("UF_SERV"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.getEnderecoUsuario().setLogradouro(rs1.getString("LOGRADOURO"));
				Dados.getEnderecoUsuario().setNumero(rs1.getString("NUMERO"));
				Dados.getEnderecoUsuario().setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.getEnderecoUsuario().setId_Bairro(rs1.getString("ID_BAIRRO"));
				Dados.getEnderecoUsuario().setBairro(rs1.getString("BAIRRO"));
				Dados.getEnderecoUsuario().setCidade(rs1.getString("CIDADE"));
				Dados.getEnderecoUsuario().setUf(rs1.getString("UF"));
				Dados.getEnderecoUsuario().setCep(rs1.getString("CEP"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				Dados.setId_UsuarioServentiaGrupo(rs1.getString("ID_USU_SERV_GRUPO"));
				Dados.setUsu_Serventia_Oab_Master(Funcoes.FormatarLogico(rs1.getString("USU_MASTER")));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	
	/**
	 * Consulta um Advogado baseado no id_UsuarioServentia passado
	 * 
	 * @param id_UsuarioServentia
	 */
	public UsuarioDt consultarUsuarioServentiaId(String id_UsuarioServentia) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.*, us.ID_USU_SERV, s.ID_SERV, s.SERV, us.ATIVO as USU_SERV_ATIVO, s.ID_AREA,  ";
		Sql += " e.LOGRADOURO, e.NUMERO, e.COMPLEMENTO,e.ID_BAIRRO, e.BAIRRO, e.CIDADE, e.UF, e.CEP,";
		Sql += " es.UF as UF_SERV, usoab.ID_USU_SERV_OAB, usoab.OAB_NUMERO, usoab.OAB_COMPLEMENTO  ";
		Sql += " FROM  PROJUDI.VIEW_USU u  ";
		Sql += "  JOIN PROJUDI.VIEW_ENDERECO e ON u.ID_ENDERECO = e.ID_ENDERECO";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_OAB usoab on us.ID_USU_SERV = usoab.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.ESTADO es on es.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE us.ID_USU_SERV = ? ";
		ps.adicionarLong(id_UsuarioServentia);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setNaturalidade(rs1.getString("NATURALIDADE"));
				Dados.setEndereco(rs1.getString("ENDERECO"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP_SIGLA"));
				Dados.setSenha(rs1.getString("SENHA"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setRg(rs1.getString("RG"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));
				Dados.getUsuarioServentiaOab().setId(rs1.getString("ID_USU_SERV_OAB"));
				Dados.getUsuarioServentiaOab().setOabNumero(rs1.getString("OAB_NUMERO"));
				Dados.getUsuarioServentiaOab().setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				Dados.setDataAtivo(Funcoes.FormatarData(rs1.getDateTime("DATA_ATIVO")));
				Dados.setDataExpiracao(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setId_Serventia(rs1.getString("ID_SERV"));
				Dados.setServentiaIdArea(rs1.getString("ID_AREA"));
				Dados.setServentia(rs1.getString("SERV"));
				Dados.setServentiaUf(rs1.getString("UF_SERV"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.getEnderecoUsuario().setLogradouro(rs1.getString("LOGRADOURO"));
				Dados.getEnderecoUsuario().setNumero(rs1.getString("NUMERO"));
				Dados.getEnderecoUsuario().setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.getEnderecoUsuario().setId_Bairro(rs1.getString("ID_BAIRRO"));
				Dados.getEnderecoUsuario().setBairro(rs1.getString("BAIRRO"));
				Dados.getEnderecoUsuario().setCidade(rs1.getString("CIDADE"));
				Dados.getEnderecoUsuario().setUf(rs1.getString("UF"));
				Dados.getEnderecoUsuario().setCep(rs1.getString("CEP"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}	
	
	
	/**
	 * Consulta Advogados Ativos ou Inativos
	 * 
	 * @param nome, filtro para nome do usuário
	 * @param usuario, filtro para login do usuário
	 * @param posicao, parametro para paginação
	 */
	public List consultarDescricaoAdvogado(String nome, String usuario, String oab, String rg, String cpf, String posicao) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT DISTINCT u.ID_USU,u.NOME, u.USU, uso.OAB_NUMERO, uso.OAB_COMPLEMENTO, est.UF, u.RG, u.CPF, u.ATIVO ";
		Sql += " FROM  PROJUDI.USU u  ";
		Sql += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += " JOIN PROJUDI.USU_SERV_OAB uso  on uso.ID_USU_SERV = us.ID_USU_SERV  ";
		Sql += " JOIN PROJUDI.SERV serv on serv.ID_SERV = us.ID_SERV ";
		Sql += " JOIN PROJUDI.ESTADO est on est.ID_ESTADO = serv.ID_ESTADO_REPRESENTACAO ";
		Sql += " JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += " WHERE g.GRUPO_CODIGO  IN (?, ?, ?, ?, ? ,?, ?) ";
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		if (nome != null && !nome.equals("")) {
			Sql += " AND u.NOME LIKE ? ";								ps.adicionarString( nome +"%");
		}
		if (usuario != null && !usuario.equals("")) {
			Sql += " AND u.USU LIKE ? ";
			ps.adicionarString( usuario +"%");
		}
		if (oab != null && !oab.equals("")) {
			Sql += " AND uso.OAB_NUMERO = ? ";
			ps.adicionarLong(oab);
		}
		if (rg != null && !rg.equals("")) {
			Sql += " AND u.RG LIKE ? ";
			ps.adicionarString( rg +"%");
		}
		if (cpf != null && !cpf.equals("")) {
			Sql += " AND u.CPF LIKE ? ";
			ps.adicionarString( cpf +"%");
		}
		Sql += " ORDER BY u.USU  ";		

		try{
			rs1 = consultarPaginacao(Sql, ps, posicao);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setOabNumero(rs1.getString("OAB_NUMERO") + "/" + rs1.getString("OAB_COMPLEMENTO") + "/" + rs1.getString("UF"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				liTemp.add(obTemp);
			}
			//rs1.close();

			Sql = " SELECT COUNT(*) AS QUANTIDADE ";
			Sql += " FROM  (";
			Sql += " 	SELECT DISTINCT u.ID_USU FROM  PROJUDI.USU u  ";
			Sql += "    JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
			Sql += "    JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
			Sql += "    JOIN PROJUDI.USU_SERV_OAB uso  on uso.ID_USU_SERV = us.ID_USU_SERV  ";
			Sql += "    JOIN PROJUDI.SERV serv on serv.ID_SERV = us.ID_SERV ";
			Sql += "    JOIN PROJUDI.ESTADO est on est.ID_ESTADO = serv.ID_ESTADO_REPRESENTACAO ";
			Sql += "  	JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
			Sql += " 	WHERE g.GRUPO_CODIGO  IN (?, ?, ?, ?, ?, ?, ?) ";
			if (nome != null && !nome.equals("")) {
				Sql += " AND u.NOME LIKE ? ";
			}
			if (usuario != null && !usuario.equals("")) {
				Sql += " AND u.USU LIKE ? ";
			}
			if (oab != null && !oab.equals("")) {
				Sql += " AND uso.OAB_NUMERO = ? ";
			}
			if (rg != null && !rg.equals("")) {
				Sql += " AND u.RG LIKE ? ";
			}
			if (cpf != null && !cpf.equals("")) {
				Sql += " AND u.CPF LIKE ? ";
			}
			Sql += ") Total";
			rs2 = consultar(Sql, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));
			
			// rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta Habilitações de um Advogado em Serventias, e suas respectivas
	 * OAB's
	 * 
	 * @param id_Usuario,
	 *            identificação do Advogado
	 */
	public List consultarServentiaOabAdvogado(String id_Usuario) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.ID_USU, u.NOME, u.USU, u.RG, u.CPF, u.ATIVO,  ";
		Sql += " us.ID_USU_SERV, s.SERV, s.ID_SERV, us.ATIVO as USU_SERV_ATIVO,  ";
		Sql += " e.UF, usoab.ID_USU_SERV_OAB, usoab.OAB_NUMERO, usoab.OAB_COMPLEMENTO, sg.ID_USU_SERV_GRUPO  ";
		Sql += " FROM  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.USU_SERV_OAB usoab on us.ID_USU_SERV = usoab.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.ESTADO e on e.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE g.GRUPO_CODIGO  IN (?, ?, ?, ?, ? ,?, ?) ";
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);		
		Sql += "  AND u.ID_USU  = ? ";
		ps.adicionarLong(id_Usuario);
		Sql += " ORDER BY u.USU  ";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setServentiaUf(rs1.getString("UF"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.getUsuarioServentiaOab().setOabNumero(rs1.getString("OAB_NUMERO"));
				obTemp.getUsuarioServentiaOab().setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				obTemp.setUsuarioServentiaAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_ATIVO")));
				obTemp.setId_UsuarioServentiaGrupo(rs1.getString("ID_USU_SERV_GRUPO"));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Consulta Habilitações de um Advogado em Serventias, e suas respectivas
	 * OAB's
	 * 
	 * @param id_Usuario,
	 *            identificação do Advogado
	 */
	public List consultarServentiaOabAdvogadoHabilitacao(String id_Usuario, String id_Serventia) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.ID_USU, u.NOME, u.USU, u.RG, u.CPF, u.ATIVO,  ";
		Sql += " us.ID_USU_SERV, s.SERV, s.ID_SERV, us.ATIVO as USU_SERV_ATIVO,  ";
		Sql += " e.UF, usoab.ID_USU_SERV_OAB, usoab.OAB_NUMERO, usoab.OAB_COMPLEMENTO  ";
		Sql += " FROM  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.USU_SERV_OAB usoab on us.ID_USU_SERV = usoab.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.ESTADO e on e.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE g.GRUPO_CODIGO IN (?, ?, ?, ?, ? ,?, ?) ";
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);		
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		Sql += " AND s.ID_SERV = ?";
		ps.adicionarLong(id_Serventia);
		Sql += "  AND u.ID_USU  = ? ";
		ps.adicionarLong(id_Usuario);
		Sql += " ORDER BY u.USU  ";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setServentiaUf(rs1.getString("UF"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.getUsuarioServentiaOab().setOabNumero(rs1.getString("OAB_NUMERO"));
				obTemp.getUsuarioServentiaOab().setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				obTemp.setUsuarioServentiaAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_ATIVO")));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * CONSULTA SERVIDOR JUDICIARIO (Usuario) POR ID Usuario
	 */
	public UsuarioDt consultarServidorJudiciarioId(String id_usuario) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT ";
		Sql += " u.*,  ";
		Sql += " ENDERECO.LOGRADOURO, ENDERECO.NUMERO, ENDERECO.COMPLEMENTO,ENDERECO.ID_BAIRRO, ENDERECO.BAIRRO, ENDERECO.CIDADE, ENDERECO.UF, ENDERECO.CEP ";
		Sql += " FROM  ";
		Sql += "  PROJUDI.VIEW_USU u  ";
		Sql += "    JOIN PROJUDI.VIEW_ENDERECO ENDERECO ON u.ID_ENDERECO = ENDERECO.ID_ENDERECO";
		Sql += " WHERE u.ID_USU = ? ";
		ps.adicionarLong(id_usuario);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setNaturalidade(rs1.getString("NATURALIDADE"));
				Dados.setEndereco(rs1.getString("ENDERECO"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP_SIGLA"));
				Dados.setSenha(rs1.getString("SENHA"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setRg(rs1.getString("RG"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));
				Dados.setTituloEleitor(rs1.getString("TITULO_ELEITOR"));
				Dados.setTituloEleitorZona(rs1.getString("TITULO_ELEITOR_ZONA"));
				Dados.setTituloEleitorSecao(rs1.getString("TITULO_ELEITOR_SECAO"));
				Dados.setCtps(rs1.getString("CTPS"));
				Dados.setCtpsSerie(rs1.getString("CTPS_SERIE"));
				Dados.setPis(rs1.getString("PIS"));
				Dados.setMatriculaTjGo(rs1.getString("MATRICULA_TJGO"));
				Dados.setNumeroConciliador(rs1.getString("NUMERO_CONCILIADOR"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				Dados.setDataAtivo(Funcoes.FormatarData(rs1.getDateTime("DATA_ATIVO")));
				Dados.setDataExpiracao(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.getEnderecoUsuario().setLogradouro(rs1.getString("LOGRADOURO"));
				Dados.getEnderecoUsuario().setNumero(rs1.getString("NUMERO"));
				Dados.getEnderecoUsuario().setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.getEnderecoUsuario().setId_Bairro(rs1.getString("ID_BAIRRO"));
				Dados.getEnderecoUsuario().setBairro(rs1.getString("BAIRRO"));
				Dados.getEnderecoUsuario().setCidade(rs1.getString("CIDADE"));
				Dados.getEnderecoUsuario().setUf(rs1.getString("UF"));
				Dados.getEnderecoUsuario().setCep(rs1.getString("CEP"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	/**
	 * Consulta um Usuário de acordo com Id_UsuarioServentiaGrupo passado
	 * 
	 * @param id_UsuarioServentiaGrupo,
	 *            identificação do usuário na serventia e grupo
	 */
	public UsuarioDt consultarUsuarioServentiaGrupoId(String id_UsuarioServentiaGrupo) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.*, us.ID_USU_SERV, s.ID_SERV, s.SERV, s.ID_COMARCA, us.ATIVO as USU_SERV_ATIVO,  ";
		Sql += " e.LOGRADOURO, e.NUMERO, e.COMPLEMENTO,e.ID_BAIRRO, e.BAIRRO, e.CIDADE, e.UF, e.CEP,";
		Sql += " es.UF as UfServentia, sg.ID_USU_SERV_GRUPO, sg.ID_GRUPO, g.GRUPO_CODIGO, g.GRUPO   ";
		Sql += " FROM  PROJUDI.VIEW_USU u  ";
		Sql += "  JOIN PROJUDI.VIEW_ENDERECO e ON u.ID_ENDERECO = e.ID_ENDERECO";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.ESTADO es on es.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE sg.ID_USU_SERV_GRUPO = ? ";
		ps.adicionarLong(id_UsuarioServentiaGrupo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				Dados.setId_UsuarioServentiaGrupo(rs1.getString("ID_USU_SERV_GRUPO"));
				Dados.setId_Grupo(rs1.getString("ID_GRUPO"));
				Dados.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				Dados.setGrupo(rs1.getString("GRUPO"));
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setNaturalidade(rs1.getString("NATURALIDADE"));
				Dados.setEndereco(rs1.getString("ENDERECO"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP_SIGLA"));
				Dados.setSenha(rs1.getString("SENHA"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setRg(rs1.getString("RG"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));
				Dados.setTituloEleitor(rs1.getString("TITULO_ELEITOR"));
				Dados.setTituloEleitorZona(rs1.getString("TITULO_ELEITOR_ZONA"));
				Dados.setTituloEleitorSecao(rs1.getString("TITULO_ELEITOR_SECAO"));
				Dados.setCtps(rs1.getString("CTPS"));
				Dados.setCtpsSerie(rs1.getString("CTPS_SERIE"));
				Dados.setPis(rs1.getString("PIS"));
				Dados.setMatriculaTjGo(rs1.getString("MATRICULA_TJGO"));
				Dados.setNumeroConciliador(rs1.getString("NUMERO_CONCILIADOR"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				Dados.setDataAtivo(Funcoes.FormatarData(rs1.getDateTime("DATA_ATIVO")));
				Dados.setDataExpiracao(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setId_Serventia(rs1.getString("ID_SERV"));
				Dados.setId_Comarca(rs1.getString("ID_COMARCA"));
				Dados.setServentia(rs1.getString("SERV"));
				Dados.setServentiaUf(rs1.getString("UfServentia"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.getEnderecoUsuario().setLogradouro(rs1.getString("LOGRADOURO"));
				Dados.getEnderecoUsuario().setNumero(rs1.getString("NUMERO"));
				Dados.getEnderecoUsuario().setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.getEnderecoUsuario().setId_Bairro(rs1.getString("ID_BAIRRO"));
				Dados.getEnderecoUsuario().setBairro(rs1.getString("BAIRRO"));
				Dados.getEnderecoUsuario().setCidade(rs1.getString("CIDADE"));
				Dados.getEnderecoUsuario().setUf(rs1.getString("UF"));
				Dados.getEnderecoUsuario().setCep(rs1.getString("CEP"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	/**
	 * Consulta Servidor Judicários Ativos ou Inativos
	 * 
	 * @param nome, filtro para nome do usuário
	 * @param usuario, filtro para login do usuário
	 * @param posicao, parametro para paginação
	 */
	public List consultarDescricaoServidorJudiciario(String nome, String usuario, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_USU,NOME,USU,RG,CPF,ATIVO";
		SqlFrom = " FROM PROJUDI.USU ";
		
		if(usuario != null && !usuario.equalsIgnoreCase("")) {
			SqlFrom += " WHERE USU = ? ";
			ps.adicionarString(usuario);
		} else {
			SqlFrom += " WHERE USU LIKE ? ";
			ps.adicionarString( usuario +"%");
			SqlFrom += " AND MATRICULA_TJGO IS NOT NULL";
		}
		
		SqlFrom += " AND NOME LIKE ? ";									ps.adicionarString( nome +"%");
		
		SqlOrder = " ORDER BY USU";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";			
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta dados de um Servidor Judiciário, e seus respectivos grupos e
	 * serventias onde está habilitado. Retorna ATIVOS ou INATIVOS
	 * 
	 * @param id_Usuario,
	 *            identificação do usuário para o qual serão consultados
	 *            serventias e grupos
	 * 
	 */
	public List consultarServentiasGruposUsuario(String id_Usuario) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.ID_USU, u.NOME, u.USU, u.RG, u.CPF, u.ATIVO,  ";
		Sql += " us.ID_USU_SERV, s.SERV, us.ATIVO as USU_SERV_ATIVO,  ";
		Sql += " e.UF, sg.ID_USU_SERV_GRUPO, sg.ID_GRUPO, g.GRUPO_CODIGO, g.GRUPO   ";
		Sql += " FROM  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.ESTADO e on e.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE g.GRUPO_CODIGO <> ? ";		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		Sql += " AND g.GRUPO_CODIGO <> ? ";			ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		Sql += " AND g.GRUPO_CODIGO <> ? ";			ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		Sql += " AND g.GRUPO_CODIGO <> ? ";			ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		Sql += " AND g.GRUPO_CODIGO <> ? ";			ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		Sql += " AND g.GRUPO_CODIGO <> ? ";			ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		Sql += " AND g.GRUPO_CODIGO <> ? ";			ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		Sql += "  AND u.ID_USU  = ? ";
		ps.adicionarLong(id_Usuario);
		Sql += " ORDER BY  u.USU  ";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setId_UsuarioServentiaGrupo(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
				obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setServentiaUf(rs1.getString("UF"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				obTemp.setUsuarioServentiaAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_ATIVO")));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * CONSULTA SERVIDOR JUDICIARIO (UsuarioServentia) ATIVOS OU INATIVOS POR ID Serventia
	 */
	public List consultarDescricaoServidorJudiciario(String id_Serventia) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT ";
		Sql += " u.ID_USU, u.NOME, u.USU, u.RG, u.CPF, u.ATIVO,  ";
		Sql += " us.ID_USU_SERV, s.SERV, us.ATIVO as USU_SERV_ATIVO,  ";
		Sql += " e.UF, sg.ID_USU_SERV_GRUPO, sg.ID_GRUPO, g.GRUPO_CODIGO, g.GRUPO   ";
		Sql += " FROM  ";
		Sql += "  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.ESTADO e on e.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE  ";
		Sql += "  g.GRUPO_CODIGO <> ? ";		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		Sql += " AND g.GRUPO_CODIGO <> ? ";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		Sql += " AND g.GRUPO_CODIGO <> ? ";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		Sql += " AND g.GRUPO_CODIGO <> ? ";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		Sql += " AND g.GRUPO_CODIGO <> ? ";		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		Sql += " AND g.GRUPO_CODIGO <> ? ";		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		Sql += " AND g.GRUPO_CODIGO <> ? ";		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		Sql += "  AND s.ID_SERV  = ? ";
		ps.adicionarLong(id_Serventia);
		Sql += " ORDER BY  ";
		Sql += "  u.USU  ";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setId_UsuarioServentiaGrupo(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
				obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setServentiaUf(rs1.getString("UF"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				obTemp.setUsuarioServentiaAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_ATIVO")));
				liTemp.add(obTemp);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Limpar Senha do usuário
	 * @param id_Usuario
	 * @throws Exception
	 */
	public void limparSenha(String id_Usuario) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU SET  ";
		Sql = Sql + ",SENHA = ? ";
		ps.adicionarString(Funcoes.SenhaMd5("12345"));
		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_USU = ? ";
		ps.adicionarLong(id_Usuario);

		executarUpdateDelete(Sql, ps);	
	}

	/**
	 * Altera Senha do usuário
	 * @param id_Usuario
	 * @param senhaAtual
	 * @param senhaNova
	 * @throws Exception
	 */
	public void alterarSenha(String id_Usuario, String senhaAtual, String senhaNova) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU SET  ";
		Sql = Sql + ",SENHA = ? ";
		ps.adicionarString(Funcoes.SenhaMd5(senhaNova));
		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_USU = ? ";			ps.adicionarLong(id_Usuario);
		if(senhaAtual!=null) {
			Sql = Sql + " AND SENHA = ? ";				ps.adicionarString(Funcoes.SenhaMd5(senhaAtual));
		}

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Altera Senha do usuário sem verificar a senha atual. ATENÇÃO: Utilizar com cuidado, método destinado
	 * à funcionalidade de recuperar senha através de link de confirmação enviado para o e-mail do usuário.
	 * @param id_Usuario
	 * @param senhaNova
	 * @throws Exception
	 * @author hrrosa
	 * @since 02/05/2017
	 */
	public void alterarSenhaSemVerificarAtual(String id_Usuario, String senhaNova) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU SET  ";
		Sql = Sql + ",SENHA = ? ";
		ps.adicionarString(Funcoes.SenhaMd5(senhaNova));
		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_USU = ? ";
		ps.adicionarLong(id_Usuario);

		executarUpdateDelete(Sql, ps);
	}

	/**
	 * CONSULATA ASSISTENTE (Usuario) POR ID Usuario
	 */
	public UsuarioDt consultarAssistenteId(String id_usuario) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Sql = " SELECT ";
		Sql += " u.*,  ";
		Sql += " ENDERECO.LOGRADOURO, ENDERECO.NUMERO, ENDERECO.COMPLEMENTO,ENDERECO.ID_BAIRRO, ENDERECO.BAIRRO, ENDERECO.CIDADE, ENDERECO.UF, ENDERECO.CEP ";
		Sql += " FROM  ";
		Sql += "  PROJUDI.VIEW_USU u  ";
		Sql += "    JOIN PROJUDI.VIEW_ENDERECO ENDERECO ON u.ID_ENDERECO = ENDERECO.ID_ENDERECO";
		Sql += " WHERE u.ID_USU = ? ";
		ps.adicionarLong(id_usuario);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setNaturalidade(rs1.getString("NATURALIDADE"));
				Dados.setEndereco(rs1.getString("ENDERECO"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP_SIGLA"));
				Dados.setSenha(rs1.getString("SENHA"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setRg(rs1.getString("RG"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				Dados.setDataAtivo(Funcoes.FormatarData(rs1.getDateTime("DATA_ATIVO")));
				Dados.setDataExpiracao(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.getEnderecoUsuario().setLogradouro(rs1.getString("LOGRADOURO"));
				Dados.getEnderecoUsuario().setNumero(rs1.getString("NUMERO"));
				Dados.getEnderecoUsuario().setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.getEnderecoUsuario().setId_Bairro(rs1.getString("ID_BAIRRO"));
				Dados.getEnderecoUsuario().setBairro(rs1.getString("BAIRRO"));
				Dados.getEnderecoUsuario().setCidade(rs1.getString("CIDADE"));
				Dados.getEnderecoUsuario().setUf(rs1.getString("UF"));
				Dados.getEnderecoUsuario().setCep(rs1.getString("CEP"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	/**
	 *  CONSULTA ASSISTENTE (UsuarioServentiaGrupo) POR ID UsuarioServentiaGrupo
	 * @return
	 */
	public UsuarioDt consultarAssistenteServentiaGrupoId(String id_UsuarioServentiaGrupo) throws Exception {
		String Sql;
		UsuarioDt Dados = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT ";
		Sql += " u.*,  ";
		Sql += " us.ID_USU_SERV, s.ID_SERV, s.SERV, us.ATIVO as USU_SERV_ATIVO,  ";
		Sql += " ENDERECO.LOGRADOURO, ENDERECO.NUMERO, ENDERECO.COMPLEMENTO,ENDERECO.ID_BAIRRO, ENDERECO.BAIRRO, ENDERECO.CIDADE, ENDERECO.UF, ENDERECO.CEP,";
		Sql += " e.UF as UF_SERV, sg.ID_USU_SERV_GRUPO, sg.ID_GRUPO, g.GRUPO_CODIGO, g.GRUPO   ";
		Sql += " FROM  ";
		Sql += "  PROJUDI.VIEW_USU u  ";
		Sql += "    JOIN PROJUDI.VIEW_ENDERECO ENDERECO ON u.ID_ENDERECO = ENDERECO.ID_ENDERECO";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.ESTADO e on e.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE sg.ID_USU_SERV_GRUPO = ? ";
		ps.adicionarLong(id_UsuarioServentiaGrupo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				Dados.setId_UsuarioServentiaGrupo(rs1.getString("ID_USU_SERV_GRUPO"));
				Dados.setId_Grupo(rs1.getString("ID_GRUPO"));
				Dados.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				Dados.setGrupo(rs1.getString("GRUPO"));
				Dados.setId_Naturalidade(rs1.getString("ID_NATURALIDADE"));
				Dados.setNaturalidade(rs1.getString("NATURALIDADE"));
				Dados.setEndereco(rs1.getString("ENDERECO"));
				Dados.setId_RgOrgaoExpedidor(rs1.getString("ID_RG_ORGAO_EXP"));
				Dados.setRgOrgaoExpedidor(rs1.getString("RG_ORGAO_EXP_SIGLA"));
				Dados.setSenha(rs1.getString("SENHA"));
				Dados.setNome(rs1.getString("NOME"));
				Dados.setSexo(rs1.getString("SEXO"));
				Dados.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				Dados.setRg(rs1.getString("RG"));
				Dados.setRgDataExpedicao(Funcoes.FormatarData(rs1.getDateTime("RG_DATA_EXPEDICAO")));
				Dados.setCpf(rs1.getString("CPF"));
				Dados.setDataCadastro(Funcoes.FormatarData(rs1.getDateTime("DATA_CADASTRO")));
				Dados.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				Dados.setDataAtivo(Funcoes.FormatarData(rs1.getDateTime("DATA_ATIVO")));
				Dados.setDataExpiracao(Funcoes.FormatarData(rs1.getDateTime("DATA_EXPIRACAO")));
				Dados.setEMail(rs1.getString("EMAIL"));
				Dados.setTelefone(rs1.getString("TELEFONE"));
				Dados.setCelular(rs1.getString("CELULAR"));
				Dados.setId_Serventia(rs1.getString("ID_SERV"));
				Dados.setServentia(rs1.getString("SERV"));
				Dados.setServentiaUf(rs1.getString("UF_SERV"));
				Dados.setId_Endereco(rs1.getString("ID_ENDERECO"));
				Dados.getEnderecoUsuario().setLogradouro(rs1.getString("LOGRADOURO"));
				Dados.getEnderecoUsuario().setNumero(rs1.getString("NUMERO"));
				Dados.getEnderecoUsuario().setComplemento(rs1.getString("COMPLEMENTO"));
				Dados.getEnderecoUsuario().setId_Bairro(rs1.getString("ID_BAIRRO"));
				Dados.getEnderecoUsuario().setBairro(rs1.getString("BAIRRO"));
				Dados.getEnderecoUsuario().setCidade(rs1.getString("CIDADE"));
				Dados.getEnderecoUsuario().setUf(rs1.getString("UF"));
				Dados.getEnderecoUsuario().setCep(rs1.getString("CEP"));
				Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	/**
	 * Consulta todos os usuários cadastrados no sistema
	 *
	 * @param descricao, filtro para nome do usuário
	 * @param posicao, parâmetro para paginação
	 * 
	 * @author msapaula, lsbernardes
	 */
	public List consultarTodosUsuarios(String nome, String usuario, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT ID_USU,NOME,USU,RG,CPF,ATIVO";
		SqlFrom = " FROM PROJUDI.USU";
		SqlFrom += " WHERE NOME LIKE ? ";								ps.adicionarString( nome +"%");
		if (usuario != null && usuario.length() > 0){
			SqlFrom += " AND USU LIKE ? ";
			ps.adicionarString( usuario +"%");			
		}
		SqlOrder = " ORDER BY NOME";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Consulta todos os promotores cadastrados no sistema
	 *
	 * @param descricao, filtro para nome do usuário
	 * @param posicao, parâmetro para paginação
	 * 
	 * @author msapaula, lsbernardes
	 */
	public List consultarTodosPromotores(String nome, String usuario, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT distinct u.ID_USU,u.NOME,u.USU,u.RG,u.CPF,u.ATIVO";
		SqlFrom = " FROM PROJUDI.USU u  ";
		SqlFrom += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		SqlFrom += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		SqlFrom += " JOIN PROJUDI.Grupo g on sg.ID_GRUPO = g.ID_GRUPO ";
		SqlFrom += " WHERE u.NOME LIKE ? ";													ps.adicionarString( nome +"%");
		SqlFrom += " and g.GRUPO_CODIGO in (?,?,?) ";										ps.adicionarLong(GrupoDt.MINISTERIO_PUBLICO);	ps.adicionarLong(GrupoDt.MP_TCE);	ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		if (usuario != null && usuario.length() > 0){
			SqlFrom += " AND u.USU LIKE ? ";												ps.adicionarString( usuario +"%");			
		}
		SqlOrder = " ORDER BY u.NOME";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	public String consultarTodosPromotoresJSON(String nome, String usuario, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT distinct u.ID_USU as id,u.NOME as descricao1,u.USU as descricao2,u.RG as descricao3,u.CPF as descricao4,u.ATIVO as descricao5";
		SqlFrom = " FROM PROJUDI.USU u  ";
		SqlFrom += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		SqlFrom += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		SqlFrom += " JOIN PROJUDI.Grupo g on sg.ID_GRUPO = g.ID_GRUPO ";
		SqlFrom += " WHERE u.NOME LIKE ? and g.GRUPO_CODIGO in (?,?,?) ";
		ps.adicionarString( nome +"%");
		ps.adicionarLong(GrupoDt.MINISTERIO_PUBLICO);
		ps.adicionarLong(GrupoDt.MP_TCE);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		if (usuario != null && usuario.length() > 0){
			SqlFrom += " AND u.USU LIKE ? ";
			ps.adicionarString( usuario +"%");			
		}
		SqlOrder = " ORDER BY u.NOME";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);	
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}
	
	/**
	 * Consulta todos os advogados cadastrados no sistema
	 *
	 * @param descricao, filtro para nome do usuário
	 * @param posicao, parâmetro para paginação
	 * 
	 * @author msapaula, lsbernardes
	 */
	public List consultarTodosAdvogados(String nome, String usuario, String id_Serventia, String posicao) throws Exception {

		String sql, sqlFrom, sqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT distinct u.ID_USU,u.NOME,u.USU,u.RG,u.CPF,u.ATIVO,us.ID_USU_SERV,uso.OAB_NUMERO,uso.OAB_COMPLEMENTO, us.ATIVO as USU_SERV_ATIVO ";
		sqlFrom = " FROM PROJUDI.USU u  ";
		sqlFrom += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		sqlFrom += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		sqlFrom += "  JOIN PROJUDI.Grupo g on sg.ID_GRUPO = g.ID_GRUPO ";
		sqlFrom += "  JOIN USU_SERV_OAB uso ON uso.ID_USU_SERV=us.ID_USU_SERV ";
		sqlFrom += " WHERE u.NOME LIKE ? and  g.GRUPO_CODIGO IN (?, ?, ?, ?, ? ,?, ?)  and us.ID_SERV = ? ";
		ps.adicionarString( nome + "%");
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		ps.adicionarLong(id_Serventia);
		if (usuario != null && usuario.length() > 0){
			sqlFrom += " AND u.USU LIKE ? ";
			ps.adicionarString( usuario +"%");			
		}
		sqlOrder = " ORDER BY u.NOME";		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setOabNumero(rs1.getString("OAB_NUMERO"));
				obTemp.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				obTemp.setUsuarioServentiaAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_ATIVO")));
				liTemp.add(obTemp);
			}
			sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(sql + sqlFrom + sqlOrder, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	public String consultarTodosAdvogadosJSON(String nome, String usuario, String id_Serventia, String posicao) throws Exception {

		String sql, sqlFrom, sqlOrder;
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		sql = "SELECT distinct u.ID_USU as id,u.NOME as descricao1,u.USU as descricao2,u.RG as descricao3,u.CPF as descricao4,u.ATIVO as descricao5 ";
		sqlFrom = " FROM PROJUDI.USU u  ";
		sqlFrom += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		sqlFrom += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		sqlFrom += "  JOIN PROJUDI.Grupo g on sg.ID_GRUPO = g.ID_GRUPO ";
		sqlFrom += "  JOIN USU_SERV_OAB uso ON uso.ID_USU_SERV=us.ID_USU_SERV ";
		sqlFrom += " WHERE u.NOME LIKE ? and  g.GRUPO_CODIGO IN (?, ?, ?, ?, ? ,?, ?)  and us.ID_SERV = ? ";
		ps.adicionarString( nome + "%");
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		ps.adicionarLong(id_Serventia);
		if (usuario != null && usuario.length() > 0){
			sqlFrom += " AND u.USU LIKE ? ";
			ps.adicionarString( usuario +"%");			
		}
		sqlOrder = " ORDER BY u.NOME";		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);


			sql= "SELECT Count(*) as Quantidade " + sqlFrom + sqlOrder;
			rs2 = consultar(sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}

	/**
	 * CONSULTA ASSISTENTE (UsuarioServentia) ATIVOS OU INATIVOS POR ID UsuarioServentia
	 */
	public List consultarDescricaoAssistenteServentia(String id_Usuario, String id_UsuarioServentia, String id_Serventia) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT ";
		Sql += " u.ID_USU, u.NOME, u.USU, u.RG, u.CPF, u.ATIVO,  ";
		Sql += " us.ID_USU_SERV, s.SERV, us.ATIVO as USU_SERV_ATIVO, sg.ATIVO as USU_SERV_GRUPO_ATIVO,  ";
		Sql += " e.UF, sg.ID_USU_SERV_GRUPO, sg.ID_GRUPO, g.GRUPO_CODIGO, g.GRUPO, us.PODE_GUARDAR_ASSINAR_USUCHEFE   ";
		Sql += " FROM  ";
		Sql += "  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.ESTADO e on e.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE  ";
		if (!id_Usuario.equalsIgnoreCase("")) {
			Sql += "  us.ID_USU  = ? AND ";
			ps.adicionarLong(id_Usuario);
		}
		Sql += "  us.ID_USU_SERV_CHEFE  = ? ";
		ps.adicionarLong(id_UsuarioServentia);
		Sql += "  AND us.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		Sql += " ORDER BY u.NOME  ";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setId_UsuarioServentiaGrupo(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
				obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setServentiaUf(rs1.getString("UF"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				obTemp.setUsuarioServentiaAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_ATIVO")));
				obTemp.setUsuarioServentiaGrupoAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_GRUPO_ATIVO")));
				obTemp.setPodeGuardarAssinarUsuarioServentiaChefe(Funcoes.FormatarLogico(rs1.getString("PODE_GUARDAR_ASSINAR_USUCHEFE")));
				liTemp.add(obTemp);
			}
			//rs1.close();

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * CONSULTA Advogados/procurados de um serventia
	 */
	public List consultarDescricaoAdvogadosProcuradoresServentia(String id_Serventia) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT ";
		Sql += " u.ID_USU, u.NOME, u.USU, u.RG, u.CPF, u.ATIVO,  ";
		Sql += " us.ID_USU_SERV, s.SERV, us.ATIVO as USU_SERV_ATIVO, sg.ATIVO as USU_SERV_GRUPO_ATIVO,  ";
		Sql += " e.UF, sg.ID_USU_SERV_GRUPO, sg.ID_GRUPO, g.GRUPO_CODIGO, g.GRUPO, oab.OAB_NUMERO, oab.OAB_COMPLEMENTO, oab.USU_MASTER ";
		Sql += " FROM  ";
		Sql += "  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.USU_SERV_OAB oab  on us.ID_USU_SERV = oab.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.ESTADO e on e.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE  ";
		Sql += "  us.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		Sql += " AND g.GRUPO_CODIGO NOT IN (?,?,?,?,?,? ) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA); ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA); ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL); ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		Sql += " ORDER BY u.NOME  ";

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setId_UsuarioServentiaGrupo(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
				obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setServentiaUf(rs1.getString("UF"));
				obTemp.setOabNumero(rs1.getString("OAB_NUMERO"));
				obTemp.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				obTemp.setOabEstado(rs1.getString("UF"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				obTemp.setUsuarioServentiaAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_ATIVO")));
				obTemp.setUsuarioServentiaGrupoAtivo(Funcoes.FormatarLogico(rs1.getString("USU_SERV_GRUPO_ATIVO")));
				obTemp.setUsu_Serventia_Oab_Master(Funcoes.FormatarLogico(rs1.getString("USU_MASTER")));
				liTemp.add(obTemp);
			}
			//rs1.close();

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Realiza a consulta por um Usuário baseado no cpf passado
	 */
	public UsuarioDt consultarUsuarioCpf(String cpf) throws Exception {
		String Sql;
		UsuarioDt obTemp = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT u.ID_USU, u.NOME, u.USU, u.RG, u.CPF, u.ATIVO FROM PROJUDI.USU u  ";
		Sql += " WHERE u.CPF = ? ";
		ps.adicionarString(cpf);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return obTemp;
	}

	/**
	 * Efetua a consulta por um Usuário baseado no login passado
	 * 
	 * @param loginUsuario:
	 *            login do Usuario que se deseja pesquisar
	 */
	public UsuarioDt consultarUsuarioLogin(String loginUsuario) throws Exception {
		UsuarioDt obTemp = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String Sql = " SELECT u.ID_USU, u.NOME, u.USU, u.RG, u.CPF, u.ATIVO FROM PROJUDI.USU u  ";
		Sql += " WHERE  u.USU = ? ";
		ps.adicionarString(loginUsuario);

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
			}
			//rs1.close();
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return obTemp;
	}
	
	public String consultarAdvogadoPublicoJSON(String oab, String PosicaoPaginaAtual) throws Exception {

		String Sql2;
		String Sql1;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql1 = " SELECT DISTINCT u.ID_USU AS ID, u.NOME AS DESCRICAO1, uso.OAB_NUMERO AS DESCRICAO2, uso.OAB_COMPLEMENTO AS DESCRICAO3, est.UF AS DESCRICAO4";
		Sql2 = " FROM  PROJUDI.USU u  ";
		Sql2 += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql2 += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql2 += " JOIN PROJUDI.USU_SERV_OAB uso  on uso.ID_USU_SERV = us.ID_USU_SERV  ";
		Sql2 += " JOIN PROJUDI.SERV serv on serv.ID_SERV = us.ID_SERV ";
		Sql2 += " JOIN PROJUDI.ESTADO est on est.ID_ESTADO = serv.ID_ESTADO_REPRESENTACAO ";
		Sql2 += " JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql2 += " WHERE g.GRUPO_CODIGO IN (?, ?, ?, ?, ?) ";					
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);	
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);	
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO); 
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO); 
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
							
		Sql2 += " AND uso.OAB_NUMERO = ? ";		ps.adicionarLong(oab);		
				
		Sql2 += " ORDER BY u.NOME  ";		

		try{
			rs1 = consultarPaginacao(Sql1 + Sql2, ps, PosicaoPaginaAtual);

			Sql1 = " SELECT COUNT(*) as QUANTIDADE ";
			
			rs2 = consultar(Sql1 + Sql2, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), PosicaoPaginaAtual, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public List consultarAdvogadoPublicoServentiaJSON(String id_serv) throws Exception {
		List liTemp = new ArrayList();
		
		String Sql2;
		String Sql1;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql1 = " SELECT DISTINCT u.ID_USU AS ID, u.NOME, uso.OAB_NUMERO, uso.OAB_COMPLEMENTO, est.UF, us.ID_USU_SERV, us.ATIVO";
		Sql2 = " FROM  PROJUDI.USU u  ";
		Sql2 += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql2 += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql2 += " JOIN PROJUDI.USU_SERV_OAB uso  on uso.ID_USU_SERV = us.ID_USU_SERV  ";
		Sql2 += " JOIN PROJUDI.SERV serv on serv.ID_SERV = us.ID_SERV ";
		Sql2 += " JOIN PROJUDI.ESTADO est on est.ID_ESTADO = serv.ID_ESTADO_REPRESENTACAO ";
		Sql2 += " JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql2 += " WHERE g.GRUPO_CODIGO IN (?, ?, ?, ?, ?) ";					
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);	
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO); 
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
							
		Sql2 += " AND us.ID_SERV = ? ";			ps.adicionarLong(id_serv);
				
		Sql2 += " ORDER BY u.NOME  ";		

		try{
			rs1 = consultar(Sql1 + Sql2, ps);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setOabNumero(rs1.getString("OAB_NUMERO"));
				obTemp.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				obTemp.setServentiaUf(rs1.getString("UF"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setAtivo(rs1.getString("ATIVO"));
				liTemp.add(obTemp);
			}
			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return liTemp;
	}
	
	public String consultarDescricaoAdvogadoJSON(String nome, String usuario, String oab, String rg, String cpf, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {

		if(ORDENACAO_PADRAO.equals(ordenacao))
			ordenacao = " u.NOME ";
		
		String Sql2;
		String Sql1;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 7;

		Sql1 = " SELECT DISTINCT u.ID_USU AS ID, u.NOME AS DESCRICAO1, u.USU AS DESCRICAO2, uso.OAB_NUMERO AS DESCRICAO3, uso.OAB_COMPLEMENTO AS DESCRICAO4, est.UF AS DESCRICAO5, u.RG AS DESCRICAO6, u.CPF AS DESCRICAO7, u.ATIVO ";
		Sql2 = " FROM  PROJUDI.USU u  ";
		Sql2 += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql2 += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql2 += " JOIN PROJUDI.USU_SERV_OAB uso  on uso.ID_USU_SERV = us.ID_USU_SERV  ";
		Sql2 += " JOIN PROJUDI.SERV serv on serv.ID_SERV = us.ID_SERV ";
		Sql2 += " JOIN PROJUDI.ESTADO est on est.ID_ESTADO = serv.ID_ESTADO_REPRESENTACAO ";
		Sql2 += " JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql2 += " WHERE g.GRUPO_CODIGO IN (?, ?, ?, ?, ? ,?, ?) ";		
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);		
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);		
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);		
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);	
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);		
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);		
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		
		if (usuario!=null && usuario.length()>0){
			Sql2 += " AND u.USU = ? ";					ps.adicionarString( usuario );
		}else if (oab!=null && oab.length()>0){
			Sql2 += " AND uso.OAB_NUMERO = ? ";			ps.adicionarLong(oab);
		}else if (rg!=null && rg.length()>0){
			Sql2 += " AND u.RG = ? ";					ps.adicionarString( rg);			
		}else if (cpf!=null && cpf.length()>0){
			Sql2 += " AND u.CPF = ? ";					ps.adicionarString( cpf );
		}else if (nome!=null && nome.length()>0){
			Sql2 += " AND u.NOME LIKE ? ";				ps.adicionarString( nome +"%"); 						
		}
				
		Sql2 += " ORDER BY " + ordenacao;		

		try{
			rs1 = consultarPaginacao(Sql1 + Sql2, ps, posicao, quantidadeRegistros);

			Sql1 = " SELECT COUNT(*) as QUANTIDADE ";			
			rs2 = consultar(Sql1 + Sql2, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarTodosUsuariosJSON(String nome, String usuario, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT ID_USU AS ID, NOME AS DESCRICAO1, USU AS DESCRICAO2,RG as descricao3,CPF as descricao4";
		SqlFrom = " FROM PROJUDI.USU";
		SqlFrom += " WHERE NOME LIKE ? ";						ps.adicionarString( nome +"%");
		if (usuario != null && usuario.length() > 0){
			SqlFrom += " AND USU LIKE ? ";
			ps.adicionarString( usuario +"%");			
		}
		SqlOrder = " ORDER BY NOME";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{
				if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarDescricaoServidorJudiciarioJSON(String nome, String usuario, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {

		if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
			ordenacao =  " USU ";
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT ID_USU AS ID, NOME AS DESCRICAO1, USU AS DESCRICAO2, RG AS DESCRICAO3, CPF AS DESCRICAO4, ATIVO";
		SqlFrom = " FROM PROJUDI.USU ";
		if(nome != null && !nome.equalsIgnoreCase("")) {
			SqlFrom += " WHERE NOME LIKE ? ";						
			ps.adicionarString( nome +"%");
		}
		if(usuario != null && !usuario.equalsIgnoreCase("")) {
			if(nome != null && !nome.equalsIgnoreCase("")) {
				SqlFrom += " AND ";
			} else {
				SqlFrom += " WHERE ";
			}
			SqlFrom += " USU = ? ";
			ps.adicionarString(usuario);
		} 
		SqlOrder = " ORDER BY " + ordenacao;	
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao, quantidadeRegistros);
			Sql = "SELECT COUNT(*) AS QUANTIDADE";			
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	public String consultarAdvogadoOAB(String codigoComarca, String oab, String complemento, String uf) throws Exception {
		String stTemp = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_DADOS_COMP_DIST";
		sql += " WHERE OAB_NUMERO = ? ";
		ps.adicionarString(oab);
		sql += "AND OAB_COMPLEMENTO = ?";
		ps.adicionarString(complemento);
		sql += "AND OAB_UF = ?";
		ps.adicionarString(uf);
		sql += "AND COD_COMARCA = ?";
		ps.adicionarLong(codigoComarca);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				stTemp += rs1.getString("NUMR_PROC")+";";
				stTemp += rs1.getString("DIGITO_VERIFICADOR")+";";
				stTemp += rs1.getString("ANO")+";";
				stTemp += rs1.getString("FIXO")+";";
				stTemp += rs1.getString("UNIDADE_ORIGEM")+";";
				stTemp += rs1.getString("SERV")+";";
				stTemp += rs1.getString("NATUREZA")+";";
				stTemp += rs1.getString("DATA_DISTRIBUICAO")+";";
				stTemp += rs1.getString("TIPO_DISTRIBUICAO")+";";
				stTemp += rs1.getString("POSICAO_JUIZ")+";";
				stTemp += rs1.getString("VALOR_ACAO")+";";
				stTemp += rs1.getString("VALOR_CUSTAS")+";";
				stTemp += rs1.getString("DATA_BAIXA_PROC")+";";
				stTemp += rs1.getString("OAB")+";";
				stTemp += rs1.getString("NOME_ADVOGADO")+";";
				stTemp += rs1.getString("TIPO_PARTE")+";";
				stTemp += rs1.getString("TIPO_PESSOA")+";";
				stTemp += rs1.getString("NOME")+";";
				stTemp += rs1.getString("CPF_CGC")+";";
				stTemp += rs1.getString("SEXO")+";";
				stTemp += rs1.getString("IDENTIDADE")+";";
				stTemp += rs1.getString("ORGAO_EXPEDIDOR")+";";
				stTemp += rs1.getString("DATA_BAIXA_PARTE")+";";
				stTemp += rs1.getString("COD_COMARCA")+";";
				stTemp += rs1.getString("NOME_SIMPLIFICADO")+";";				
				stTemp += "\n";
			}
		
		}
		finally{
			try{if( rs1 != null ) rs1.close(); }catch(Exception e){}
		}

		return stTemp;
	}
	
	/**
	 * Método que verifica se existe um usuário ativo de acordo com login e
	 * senha passados
	 * 
	 * @param usuario, usuário digitado
	 * @param senha, senha digitada
	 */
	public UsuarioDt consultaUsuarioSenhaMD5(String usuario, String senha) throws Exception {
		String stSql;
		ResultSetTJGO rs1 = null;
		UsuarioDt tempdtUsuario = new UsuarioDt();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSql = "SELECT * FROM PROJUDI.VIEW_USU u  WHERE u.USU = ? ";
		ps.adicionarString(usuario);
		stSql += " AND u.SENHA = ? ";
		ps.adicionarString(senha);
		stSql += " AND u.ATIVO = 1";

		rs1 = consultar(stSql, ps);

		if (rs1.next()) {
			tempdtUsuario.setId(rs1.getString("ID_USU"));
			tempdtUsuario.setUsuario(rs1.getString("USU"));
			tempdtUsuario.setSenha(rs1.getString("SENHA"));
			tempdtUsuario.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			if (rs1.next()) throw new MensagemException("Erro no Logon, mais de um logon encontrado");
		}

		return tempdtUsuario;

	}
	
	/**
	 * Método que consulta o nome do usuário pelo serventia cargo dele.
	 * @param idServCargo - ID do Serventia Cargo
	 * @return nome do usuário
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarNomeUsuarioServentiaCargo(String idServCargo) throws Exception {
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = " select u.nome AS NOME from PROJUDI.USU u "
				+ " inner join PROJUDI.USU_SERV us on us.ID_USU = u.ID_USU "
				+ " inner join PROJUDI.SERV s ON s.ID_SERV = us.ID_SERV "
				+ " inner join PROJUDI.USU_SERV_GRUPO usg on usg.ID_USU_SERV = us.ID_USU_SERV "
				+ " inner join PROJUDI.GRUPO g on usg.ID_GRUPO = g.ID_GRUPO "
				+ " inner join PROJUDI.SERV_CARGO sc on sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO "
				+ " where sc.ID_SERV_CARGO = ? ";
		ps.adicionarString(idServCargo);

		rs1 = consultar(stSql, ps);

		if (rs1.next()) {
			return rs1.getString("NOME");
		}

		return null;

	}
	
	/**
	 * Método que consulta o nome do usuário pelo serventia cargo dele.
	 * @param idServCargo - ID do Serventia Cargo
	 * @return nome do usuário
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarNomeCargoUsuarioServentiaCargo(String idServCargo) throws Exception {
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = " select u.nome AS NOME, sc.SERV_CARGO from PROJUDI.USU u "
				+ " inner join PROJUDI.USU_SERV us on us.ID_USU = u.ID_USU "
				+ " inner join PROJUDI.SERV s ON s.ID_SERV = us.ID_SERV "
				+ " inner join PROJUDI.USU_SERV_GRUPO usg on usg.ID_USU_SERV = us.ID_USU_SERV "
				+ " inner join PROJUDI.GRUPO g on usg.ID_GRUPO = g.ID_GRUPO "
				+ " inner join PROJUDI.SERV_CARGO sc on sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO "
				+ " where sc.ID_SERV_CARGO = ? ";
		ps.adicionarString(idServCargo);

		rs1 = consultar(stSql, ps);

		if (rs1.next()) {
			return rs1.getString("SERV_CARGO") +" - "+rs1.getString("NOME");
		}

		return null;

	}
	
	/**
	 * Método que consulta o último login do usuário.
	 * @param idUsuario - ID do Usuário.
	 * @return UsuarioUltimoLoginDt
	 * @throws Exception
	 * @author mmgomes
	 */
	public UsuarioUltimoLoginDt consultarUsuarioUltimoLogin(String idUsuario) throws Exception {
		UsuarioUltimoLoginDt obTemp = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String Sql = " SELECT ID_USU, ID_SERV, GRUPO_CODIGO, ID_SERV_CARGO, ID_SERV_CARGO_USU_CHEFE, ID_USU_SERV_CHEFE, MODO_CONTRASTE, LAYOUT_CAPA_PROCESSO FROM PROJUDI.USU_ULTIMO_LOGIN ";
		Sql += " WHERE ID_USU = ? ";
		ps.adicionarLong(idUsuario);

		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {
				obTemp = new UsuarioUltimoLoginDt();
				obTemp.setId_Usuario(rs1.getString("ID_USU"));
				
				if (rs1.getString("ID_SERV") == null) obTemp.setId_Serventia("");
				else obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				
				if (rs1.getString("GRUPO_CODIGO") == null) obTemp.setGrupoCodigo("");
				else obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				
				if (rs1.getString("ID_SERV_CARGO") == null) obTemp.setId_ServentiaCargo("");
				else obTemp.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
				
				if (rs1.getString("ID_SERV_CARGO_USU_CHEFE") == null) obTemp.setId_ServentiaCargoUsuarioChefe("");
				else obTemp.setId_ServentiaCargoUsuarioChefe(rs1.getString("ID_SERV_CARGO_USU_CHEFE"));
				
				if (rs1.getString("ID_USU_SERV_CHEFE") == null) obTemp.setId_UsuarioServentiaChefe("");
				else obTemp.setId_UsuarioServentiaChefe(rs1.getString("ID_USU_SERV_CHEFE"));

				obTemp.setModo_Contraste(rs1.getString("MODO_CONTRASTE"));
				obTemp.setLayoutCapaProcesso(rs1.getString("LAYOUT_CAPA_PROCESSO"));
				
			}
			//rs1.close();
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return obTemp;
	}
	
	/**
	 * Método que grava o último login do usuário.
	 * @param dados - UsuarioUltimoLoginDt.
	 * @throws Exception
	 * @author mmgomes
	 */	
	public void inserirUsuarioUltimoLogin(UsuarioUltimoLoginDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "DELETE FROM PROJUDI.USU_ULTIMO_LOGIN WHERE ID_USU = ?";
		ps.adicionarLong(dados.getId_Usuario());
		executarUpdateDelete(Sql, ps);
		
		Sql = "INSERT INTO PROJUDI.USU_ULTIMO_LOGIN (ID_USU, ID_SERV, GRUPO_CODIGO, ID_SERV_CARGO, ID_SERV_CARGO_USU_CHEFE, ID_USU_SERV_CHEFE)";
		Sql += " Values (?, ?, ?, ?, ?, ?)";	
		ps.adicionarString(dados.getId_Serventia());
		ps.adicionarString(dados.getGrupoCodigo());
		ps.adicionarString(dados.getId_ServentiaCargo());
		ps.adicionarString(dados.getId_ServentiaCargoUsuarioChefe());
		ps.adicionarString(dados.getId_UsuarioServentiaChefe());
		
		executarUpdateDelete(Sql, ps);
	}

	//desaqui: hataraku
	public void gravarTipoMenuMovimentacao(UsuarioUltimoLoginDt dados) throws Exception
	{
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "UPDATE PROJUDI.USU_ULTIMO_LOGIN SET LAYOUT_CAPA_PROCESSO = ? ";
		ps.adicionarString(dados.getLayoutCapaProcesso());
		Sql += " WHERE ID_USU = ? ";
		ps.adicionarLong(dados.getId_Usuario());
		
		executarUpdateDelete(Sql, ps);
	}
	
	public BitSet consultarPermissoesGrupo(String id_grupo) throws Exception {
		BitSet Permissoes = new BitSet(8000);
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{
		
			stSql = "  SELECT * FROM PROJUDI.VIEW_GRUPO_PERM WHERE ID_GRUPO in ( ?, ? ) "; 	ps.adicionarLong(id_grupo); ps.adicionarLong(GrupoDt.ID_GRUPO_PUBLICO);

			rs1 = consultar(stSql, ps);

			while (rs1.next()) {
				Permissoes.set(rs1.getInt("PERM_CODIGO"), true);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Permissoes;
	}

	//Código abaixo alterado pelo jelves 08/09/16, pois não estava fazendo a paginação
	public String consultarDescricaoAssistenteServentiaJSON(String id_Usuario, String id_UsuarioServentia, String id_Serventia,String qntRegistro,String posicao, String tipoOrdenacao,String pesquisa) throws Exception {
		String stTemp = "";
		String Sql = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 8;
	
		Sql = " SELECT  ";
		Sql += " u.ID_USU as Id, "
				+ "u.NOME as descricao1, "
				+ "u.USU as descricao2, "
				+ "u.RG as descricao3, "
				+ "u.CPF as descricao4 , "
				+ "u.ATIVO ,  ";
		Sql += " us.ID_USU_SERV ,"
				+ " s.SERV , "
				+ "us.ATIVO as descricao5 , "
				+ "sg.ATIVO as descricao6 ,  ";
		Sql += " e.UF , "
				+ "sg.ID_USU_SERV_GRUPO descricao7 , "
				+ "sg.ID_GRUPO , "
				+ "g.GRUPO_CODIGO , g.GRUPO   , ";
		Sql += "us.PODE_GUARDAR_ASSINAR_USUCHEFE as descricao8";		
		Sql += " FROM  ";
		Sql += "  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.ESTADO e on e.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
		Sql += " WHERE  ";
		if (!id_Usuario.equalsIgnoreCase("")) {
			Sql += "  us.ID_USU  = ? AND ";
			ps.adicionarLong(id_Usuario);
		}
		if(!pesquisa.equalsIgnoreCase("")){
			Sql += " u.CPF LIKE ? AND ";
			ps.adicionarString("%"+pesquisa+"%");
		}
		Sql += "  us.ID_USU_SERV_CHEFE  = ? ";
		ps.adicionarLong(id_UsuarioServentia);
		Sql += "  AND us.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		Sql += " ORDER BY "+tipoOrdenacao;

		try {
			rs1 = consultarPaginacao(Sql, ps, posicao, qntRegistro);
			
			
			Sql = " SELECT ";
			Sql += " COUNT(u.ID_USU) as QUANTIDADE ";
			Sql += " FROM  ";
			Sql += "  PROJUDI.USU u  ";
			Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
			Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
			Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
			Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
			Sql += "  JOIN PROJUDI.ESTADO e on e.ID_ESTADO= s.ID_ESTADO_REPRESENTACAO  ";
			Sql += " WHERE  ";
			if (!id_Usuario.equalsIgnoreCase("")) {
				Sql += "  us.ID_USU  = ? AND ";
			}
			if(!pesquisa.equalsIgnoreCase("")){
				Sql += " u.CPF LIKE ? AND ";
			}
			Sql += "  us.ID_USU_SERV_CHEFE  = ? ";
			Sql += "  AND us.ID_SERV = ? ";
			rs2 = consultar(Sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try {if (rs1 != null)rs1.close();} catch (Exception e) {}
			try {if (rs2 != null)rs2.close();} catch (Exception e) {}
		}
		return stTemp;
	}
	
	public UsuarioDt  consultarAssistenteServentiaCpfJSON(String id_UsuarioServentia,String id_Serventia,String cpf) throws Exception{
		
		String Sql;
		UsuarioDt usu = new UsuarioDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "Select * ";
		Sql += "FROM VIEW_USU_COMPLETO uc ";
		Sql += "WHERE uc.CPF LIKE ? ";
		ps.adicionarString(cpf);

		try {
			rs1 = consultar(Sql, ps);
			if(rs1.next()){
				usu.setId(rs1.getString("ID_USU"));
				usu.setNome(rs1.getString("NOME"));
				usu.setUsuario(rs1.getString("USU"));
				usu.setRg(rs1.getString("RG"));
				usu.setCpf(rs1.getString("CPF"));
				usu.setAtivo(rs1.getString("ATIVO"));
			}
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {
			}
			
		}
		return usu;
		
	}
	
	
	/*
	 * Retorna o email do usuario.
	 * 
	 * @param String idUsuario
	 * @author hrrosa
	 * @since 22/02/2017
	 */
	public String consultarEmailUsuario(String idUsuario) throws Exception{
		String sql;
		String email = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		
		sql = "SELECT EMAIL FROM PROJUDI.USU WHERE ID_USU = ?";
		ps.adicionarLong(idUsuario);
		
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				email = rs1.getString("EMAIL");
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		
		return email;
	}
	
	/**
	 * Consulta os usuários que estão nos grupos DESEMBARGADOR E JUIZ_VARA
	 * @param descricao
	 * @param posicao
	 * @return
	 */
	public String consultarUsuarioServentiaGrupoMagistrados(String descricao, String posicao) throws Exception {
		
		String stTemp="";
		int qtdeColunas = 2;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		StringBuilder sql = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		StringBuilder sqlSelect = new StringBuilder();
		sqlSelect.append(" SELECT DISTINCT u.id_usu AS id, u.nome AS descricao1");
		sqlSelect.append(" , 'Magistrado' descricao2");
		
		StringBuilder sqlFrom = new StringBuilder();
		sqlFrom.append(" FROM projudi.usu_serv_grupo usg");
		sqlFrom.append(" JOIN projudi.grupo g ON usg.id_grupo = g.id_grupo ");
		sqlFrom.append(" JOIN projudi.grupo_tipo gt ON g.id_grupo_tipo = gt.id_grupo_tipo ");
		sqlFrom.append(" AND gt.grupo_tipo_codigo IN (?,?,?)  "); 								ps.adicionarLong(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU); ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA); ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU); 
		sqlFrom.append(" JOIN projudi.usu_serv us ON us.id_usu_serv = usg.id_usu_serv");
		sqlFrom.append(" JOIN projudi.usu u ON us.id_usu = u.id_usu");
		
		if (ValidacaoUtil.isNaoVazio(descricao)) {
			sqlFrom.append(" WHERE u.nome LIKE ? ");
			ps.adicionarString("%" + descricao + "%");
		}
		
		StringBuilder sqlOrder = new StringBuilder();
		sqlOrder.append(" ORDER BY descricao2, descricao1 ASC");
		
		try {
			sql = new StringBuilder();
			sql.append(sqlSelect).append(sqlFrom).append(sqlOrder);
			rs1 = consultarPaginacao(sql.toString(), ps, posicao);			
			sql = new StringBuilder();
			sql.append("SELECT Count(*) as QUANTIDADE FROM ( ").append(sqlSelect).append(sqlFrom).append(")");
			rs2 = consultar(sql.toString(), ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
	
	/**
	 * Altera Status Master de um advogado/procurador
	 * @param id_Usu_Serv_OAB, ID do usuário serventia oab que terá o status master alterado
	 * @param statusMasterNovo, Novo status Master 
	 * @throws Exception
	 */
	public void alterarStatusAdvovogadoMaster(String id_Usu_Serv_OAB, int statusMasterNovo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_SERV_OAB oab SET oab.USU_MASTER = ? where oab.ID_USU_SERV_OAB = ?  ";
		ps.adicionarLong(statusMasterNovo);
		ps.adicionarString(id_Usu_Serv_OAB);
		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método para consultar um advogado de acordo com dados da oab e cpf
	 * 
	 * @param oabNumero, número da OAB
	 * @param oabComplemento, complemento da OAB
	 * @param oabEstado, estado da OAB
	 * @param cpf, cpf do usuário
	 */
	public UsuarioDt consultarAdvogadoOabCPF(String oabNumero, String oabComplemento, String oabEstado, String cpf) throws Exception {
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		UsuarioDt usuarioDt = null;

		sql = " SELECT u.ID_USU, u.NOME, u.CPF, uo.OAB_NUMERO, uo.OAB_COMPLEMENTO, e.UF, s.SERV FROM PROJUDI.USU_SERV us";
		sql += " JOIN PROJUDI.USU u on u.ID_USU=us.ID_USU ";
		sql += " JOIN PROJUDI.SERV s on s.ID_SERV=us.ID_SERV";
		sql += " JOIN PROJUDI.ESTADO e on e.ID_ESTADO=s.ID_ESTADO_REPRESENTACAO";
		sql += " JOIN PROJUDI.SERV_TIPO st on st.ID_SERV_TIPO=s.ID_SERV_TIPO";
		sql += " JOIN PROJUDI.USU_SERV_OAB uo on uo.ID_USU_SERV=us.ID_USU_SERV";
		sql += " WHERE uo.OAB_NUMERO = ? ";
		ps.adicionarLong(oabNumero);
		sql += " AND uo.OAB_COMPLEMENTO= ? ";		
		ps.adicionarString(oabComplemento);
		sql += " AND e.UF= ? ";		
		ps.adicionarString(oabEstado);
		sql += " AND u.CPF = ? ";
		ps.adicionarLong(cpf);
		
		//sql += " AND u.ATIVO = 1 AND us.ATIVO = 1 ";		

		try{
			rs1 = consultar(sql, ps);
			if(rs1.next()) {
				usuarioDt = new UsuarioDt();
				usuarioDt.setId(rs1.getString("ID_USU"));
				usuarioDt.setCpf(rs1.getString("CPF"));
				usuarioDt.setServentia(rs1.getString("SERV"));
				usuarioDt.setNome(rs1.getString("NOME"));
				usuarioDt.setOabNumero(rs1.getString("OAB_NUMERO"));
				usuarioDt.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				usuarioDt.setOabEstado(rs1.getString("UF"));
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return usuarioDt;
	}
	
	/**
	 * Método que consulta o nome do usuário pelo identiticação do usuário serventia dele.
	 * 
	 * @param id_Usu_Serv - ID do usuário serventia
	 * @return nome do usuário
	 * @throws Exception
	 * @author lsbernardes
	 */
	public String consultarNomeUsuario(String id_Usu_Serv) throws Exception {
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = " select u.nome AS NOME from PROJUDI.USU u "
				+ " inner join PROJUDI.USU_SERV us on us.ID_USU = u.ID_USU "
				+ " where us.ID_USU_SERV = ? ";
		
		ps.adicionarString(id_Usu_Serv);

		rs1 = consultar(stSql, ps);

		if (rs1.next()) {
			return rs1.getString("NOME");
		}

		return null;
	}

	public List consultarTodosPoliciais(String nome, String usuario, String posicao) throws Exception {
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "SELECT distinct u.ID_USU,u.NOME,u.USU,u.RG,u.CPF,u.ATIVO";
		SqlFrom = " FROM PROJUDI.USU u  ";
		SqlFrom += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		SqlFrom += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		SqlFrom += " JOIN PROJUDI.Grupo g on sg.ID_GRUPO = g.ID_GRUPO ";
		SqlFrom += " WHERE u.NOME LIKE ? ";											ps.adicionarString( nome +"%"); 
		SqlFrom += " and g.GRUPO_CODIGO in (?,?) ";									ps.adicionarLong(GrupoDt.AUTORIDADES_POLICIAIS);		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_SSP);
		
		if (usuario != null && usuario.length() > 0){
			SqlFrom += " AND u.USU LIKE ? ";										ps.adicionarString( usuario +"%");			
		}
		
		SqlOrder = " ORDER BY u.NOME";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				liTemp.add(obTemp);
			}
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	public String consultarTodosPoliciaisJSON(String nome, String usuario, String posicao) throws Exception {

		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;

		Sql = "SELECT distinct u.ID_USU as id,u.NOME as descricao1,u.USU as descricao2,u.RG as descricao3,u.CPF as descricao4,u.ATIVO as descricao5";
		SqlFrom = " FROM PROJUDI.USU u  ";
		SqlFrom += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		SqlFrom += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		SqlFrom += " JOIN PROJUDI.Grupo g on sg.ID_GRUPO = g.ID_GRUPO ";
		SqlFrom += " WHERE u.NOME LIKE ? ";											ps.adicionarString( nome +"%"); 
		SqlFrom += " and g.GRUPO_CODIGO in (?,?) ";									ps.adicionarLong(GrupoDt.AUTORIDADES_POLICIAIS);		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_SSP);
		
		if (usuario != null && usuario.length() > 0){
			SqlFrom += " AND u.USU LIKE ? ";										ps.adicionarString( usuario +"%");			
		}
		
		SqlOrder = " ORDER BY u.NOME";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);

			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);	
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}
	
	public String consultarOficialJusticaJSON(String nome, String posicao, String idServ) throws Exception {

		String sql, sqlFrom, sqlOrder;
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 4;
		sql = "SELECT u.ID_USU as id, u.NOME as descricao1, u.USU as descricao2, u.RG as descricao3, u.CPF as descricao4";
		sqlFrom = " FROM PROJUDI.USU_SERV_GRUPO usg  ";
		sqlFrom += " INNER JOIN PROJUDI.GRUPO g ON g.ID_GRUPO = usg.ID_GRUPO";
		sqlFrom += " INNER JOIN PROJUDI.USU_SERV us ON us.ID_USU_SERV = usg.ID_USU_SERV  ";
		sqlFrom += " INNER JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV ";
		sqlFrom += " INNER JOIN USU u ON u.ID_USU = us.ID_USU";
		sqlFrom += " WHERE u.NOME LIKE ? AND  g.GRUPO_CODIGO IN (?)  AND s.ID_SERV_TIPO = ? AND us.ATIVO = ?";
		ps.adicionarString( nome + "%");
		ps.adicionarLong(GrupoDt.OFICIAL_JUSTICA);
		ps.adicionarLong(ServentiaTipoDt.CENTRAL_MANDADOS);	
		ps.adicionarLong(UsuarioServentiaDt.ATIVO);		
		if(!idServ.equalsIgnoreCase("")) {
			sqlFrom += " AND s.id_serv = ?";
			ps.adicionarLong(idServ);			
		}		
		sqlOrder = " ORDER BY u.NOME";			
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			sql= "SELECT Count(*) as Quantidade " + sqlFrom + sqlOrder;
			rs2 = consultar(sql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return stTemp;
	}	
}

