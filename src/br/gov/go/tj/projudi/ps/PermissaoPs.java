package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PermissaoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PermissaoPs extends PermissaoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 7673963076840697753L;

    public PermissaoPs(Connection conexao){
    	Conexao = conexao;
	}

	public List consultarPermissoes() throws Exception {
		String sql;
		List listaPermissoes = new ArrayList();

		sql = "SELECT ID_PERM,PERM FROM PROJUDI.VIEW_PERM ";
		sql += " ORDER BY PERM";
		ResultSetTJGO rs1 = consultarSemParametros(sql);
		while (rs1.next()) {
			PermissaoDt permissaoDt = new PermissaoDt();
			permissaoDt.setId(rs1.getString("ID_PERM"));
			permissaoDt.setPermissao(rs1.getString("PERM"));
			listaPermissoes.add(permissaoDt);
		}

		return listaPermissoes;
	}

	public List consultarTodasPermissoes(String id_grupo) throws Exception {
		String sql;
		List listaPermissoes = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT p.*, gp.ID_GRUPO, gp.id_grupo_perm FROM PROJUDI.VIEW_PERM p LEFT JOIN GRUPO_PERM gp ON p.ID_PERM=gp.ID_PERM AND gp.ID_GRUPO = ?";
		ps.adicionarLong(id_grupo);
		sql += " ORDER BY ORDENACAO, PERM";

		ResultSetTJGO rs1 = consultar(sql,ps);
		while (rs1.next()) {
			PermissaoDt permissaoDt = new PermissaoDt();
			permissaoDt.setId(rs1.getString("ID_PERM"));
			permissaoDt.setPermissao(rs1.getString("PERM"));
			permissaoDt.setPermissaoCodigo(rs1.getString("PERM_CODIGO"));
			permissaoDt.setId_PermissaoPai(rs1.getString("ID_PERM_PAI"));
			permissaoDt.setPermissaoCodigoPai(rs1.getString("PERM_CODIGO_PAI"));
			permissaoDt.setPermissaoPai(rs1.getString("PERM_PAI"));
			permissaoDt.setEMenu(rs1.getString("E_MENU"));
			permissaoDt.setLink(rs1.getString("LINK"));
			permissaoDt.setTitulo(rs1.getString("TITULO"));
			permissaoDt.setIrPara(rs1.getString("IR_PARA"));
			permissaoDt.setOrdenacao(rs1.getString("ORDENACAO"));
			permissaoDt.setCodigoTemp(rs1.getString("ID_GRUPO"));			
			permissaoDt.setId_Grupo(id_grupo);
			permissaoDt.setId_GrupoPermissao(rs1.getString("id_grupo_perm"));

			listaPermissoes.add(permissaoDt);
		}
		rs1.close();
		return listaPermissoes;
	}

	public List consultarMenusUsuarioGrupo(String id_usuarioserventia, String id_grupo) throws Exception {
		String sql;
		List listaPermissoes = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

//		sql = "SELECT * FROM (SELECT p.*, gp.ID_GRUPO FROM PROJUDI.VIEW_PERM p INNER JOIN PROJUDI.VIEW_USU_PERM_GERAL gp";
//		sql += " ON p.ID_PERM=gp.ID_PERM ";
//		sql += " WHERE p.E_MENU = ? ";
//		ps.adicionarLong(1);
//		sql += " AND ( gp.ID_GRUPO = ? OR gp.ID_GRUPO=?)";
//		ps.adicionarLong(id_grupo);
//		ps.adicionarLong(-100);
//		sql += " AND gp.ID_PERM_ESPECIAL IS NULL";
//		sql += " AND gp.ID_USU_SERV = ?";
//		ps.adicionarLong(id_grupo); 
//		sql += " UNION ";
		sql = "SELECT p.*, gp.ID_GRUPO FROM PROJUDI.VIEW_PERM p INNER JOIN PROJUDI.VIEW_USU_PERM_GRUPO_GERAL gp";
		sql += " ON p.ID_PERM=gp.ID_PERM ";
		sql += " WHERE p.E_MENU = ? ";										ps.adicionarLong(1);
		sql += " AND gp.ID_GRUPO = ? ";                                     ps.adicionarLong(id_grupo);
		sql += " AND gp.ID_PERM_ESPECIAL IS NULL";
		sql += " AND gp.ID_USU_SERV = ? ";									ps.adicionarLong(id_usuarioserventia);

		sql += " ORDER BY ORDENACAO, PERM";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				PermissaoDt permissaoDt = new PermissaoDt();
				permissaoDt.setId(rs1.getString("ID_PERM"));
				permissaoDt.setPermissao(rs1.getString("PERM"));
				permissaoDt.setPermissaoCodigo(rs1.getString("PERM_CODIGO"));
				permissaoDt.setId_PermissaoPai(rs1.getString("ID_PERM_PAI"));
				permissaoDt.setPermissaoCodigoPai(rs1.getString("PERM_CODIGO_PAI"));
				permissaoDt.setPermissaoPai(rs1.getString("PERM_PAI"));
				permissaoDt.setEMenu(rs1.getString("E_MENU"));
				permissaoDt.setLink(rs1.getString("LINK"));
				permissaoDt.setTitulo(rs1.getString("TITULO"));
				permissaoDt.setIrPara(rs1.getString("IR_PARA"));
				permissaoDt.setOrdenacao(rs1.getString("ORDENACAO"));
				permissaoDt.setCodigoTemp(rs1.getString("ID_GRUPO"));
				listaPermissoes.add(permissaoDt);
			}
			

		} finally {
			try{if(rs1!=null) rs1.close(); } catch(Exception e) {}
		}
		
		return listaPermissoes;
	}

	public List consultarMenusEspecialUsuarioGrupo(String id_usuarioserventia, String id_grupo, int permissaoEspecialCodigo) throws Exception {
		String stSql;
		List listaPermissoes = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
//		stSql = "SELECT * FROM (";
//		stSql += "SELECT p.*, gp.ID_GRUPO FROM PROJUDI.VIEW_PERM p";
//		stSql += " INNER JOIN PROJUDI.VIEW_USU_PERM_GERAL gp ON p.ID_PERM=gp.ID_PERM ";
//		stSql += " LEFT JOIN PROJUDI.PERM_ESPECIAL pe on p.ID_PERM_ESPECIAL = pe.ID_PERM_ESPECIAL ";
//		stSql += " WHERE p.E_MENU = ? ";																	ps.adicionarLong(1);
//		stSql += " AND (gp.ID_GRUPO = ? OR gp.ID_GRUPO=? )";												ps.adicionarLong(id_grupo);		ps.adicionarLong(-100);
//		stSql += " AND (pe.PERM_ESPECIAL_CODIGO = ? OR p.ID_PERM_PAI is not null)";							ps.adicionarLong(permissaoEspecialCodigo);
//		stSql += " AND gp.ID_USU_SERV = ?";																	ps.adicionarLong(id_usuarioserventia);
//		stSql += " UNION ";
		stSql = "SELECT p.*, gp.ID_GRUPO FROM PROJUDI.VIEW_PERM p";
		stSql += " INNER JOIN PROJUDI.VIEW_USU_PERM_GRUPO_GERAL gp ON p.ID_PERM=gp.ID_PERM ";
		stSql += " LEFT JOIN PROJUDI.PERM_ESPECIAL pe on p.ID_PERM_ESPECIAL = pe.ID_PERM_ESPECIAL ";
		stSql += " WHERE p.E_MENU = ? ";																	ps.adicionarLong(1);
		stSql += " AND (gp.ID_GRUPO = ? OR gp.ID_GRUPO=? )";												ps.adicionarLong(id_grupo);		ps.adicionarLong(-100);
		stSql += " AND (pe.PERM_ESPECIAL_CODIGO = ? OR p.ID_PERM_PAI is not null)";							ps.adicionarLong(permissaoEspecialCodigo);
		stSql += " AND gp.ID_USU_SERV = ?";																	ps.adicionarLong(id_usuarioserventia);
		
		stSql += " ORDER BY ORDENACAO, PERM";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(stSql,ps);
			while (rs1.next()) {
				PermissaoDt permissaoDt = new PermissaoDt();
				permissaoDt.setId(rs1.getString("ID_PERM"));
				permissaoDt.setPermissao(rs1.getString("PERM"));
				permissaoDt.setPermissaoCodigo(rs1.getString("PERM_CODIGO"));
				permissaoDt.setId_PermissaoPai(rs1.getString("ID_PERM_PAI"));
				permissaoDt.setPermissaoCodigoPai(rs1.getString("PERM_CODIGO_PAI"));
				permissaoDt.setPermissaoPai(rs1.getString("PERM_PAI"));
				permissaoDt.setEMenu(rs1.getString("E_MENU"));
				permissaoDt.setLink(rs1.getString("LINK"));
				permissaoDt.setTitulo(rs1.getString("TITULO"));
				permissaoDt.setIrPara(rs1.getString("IR_PARA"));
				permissaoDt.setOrdenacao(rs1.getString("ORDENACAO"));
				permissaoDt.setCodigoTemp(rs1.getString("ID_GRUPO"));
				listaPermissoes.add(permissaoDt);
			}

		} finally{
			try{if(rs1!=null) rs1.close(); } catch(Exception e) {}
		}
		
		return listaPermissoes;
	}

	@SuppressWarnings("unchecked")
	
	public List consultarDescricao(String descricao, String posicao) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		List listaPermissoes = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT *";
		sqlFrom = " FROM PROJUDI.VIEW_PERM";
		sqlFrom += " WHERE PERM LIKE ?";
		ps.adicionarString( descricao +"%");
		sqlOrder = " ORDER BY PERM_CODIGO_PAI, PERM, E_MENU";
		
		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs12 = null;
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			while (rs1.next()) {
				PermissaoDt permissaoDt = new PermissaoDt();
				permissaoDt.setId(rs1.getString("ID_PERM"));
				permissaoDt.setPermissao(rs1.getString("PERM"));
				permissaoDt.setPermissaoCodigo(rs1.getString("PERM_CODIGO"));
				permissaoDt.setId_PermissaoPai(rs1.getString("ID_PERM_PAI"));
				permissaoDt.setPermissaoCodigoPai(rs1.getString("PERM_CODIGO_PAI"));
				permissaoDt.setPermissaoPai(rs1.getString("PERM_PAI"));
				permissaoDt.setEMenu(rs1.getString("E_MENU"));
				permissaoDt.setLink(rs1.getString("LINK"));
				permissaoDt.setTitulo(rs1.getString("TITULO"));
				permissaoDt.setIrPara(rs1.getString("IR_PARA"));
				permissaoDt.setOrdenacao(rs1.getString("ORDENACAO"));
				permissaoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				listaPermissoes.add(permissaoDt);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs12 = consultar(sql + sqlFrom, ps);
			if (rs12.next()) 
				listaPermissoes.add(rs12.getLong("QUANTIDADE"));
			
			//rs12.close();
		} finally{
			try{if(rs1 != null) rs1.close();} catch(Exception e) {}
			try{if(rs12 != null) rs12.close();} catch(Exception e) {}
		}
		return listaPermissoes;
	}

	/**
	 * Método que realiza consulta de Permissões por código ou descrição.
	 * @param codigo - Código da permissão (não obrigatório).
	 * @param descricao - Descrição da permissão (não obrigatório).
	 * @param posicao - posição da página (não obrigatório).
	 * @return Lista de permissões
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List consultarCodigoDescricao(String codigo, String descricao, String posicao) throws Exception {
		StringBuffer sqlPrincipal = new StringBuffer();
		StringBuffer sqlSecundario = new StringBuffer();
		String sqlQtde = null;
		List listaPermissoes = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sqlPrincipal.append("SELECT * FROM ");
		
		sqlSecundario.append(" PROJUDI.VIEW_PERM ");
		
		boolean clausulaAnd = false;
		if((codigo != null && codigo.length() > 0) || (descricao != null && descricao.length() > 0)) {
			sqlSecundario.append(" WHERE ");
		}
		
		if(codigo != null && codigo.length() > 0) {
			if(clausulaAnd) {
				sqlSecundario.append(" AND ");
			}
			sqlSecundario.append(" PERM_CODIGO = ?");
			ps.adicionarLong(codigo);
			clausulaAnd = true;
		}
		
		if(descricao != null && descricao.length() > 0) {
			if(clausulaAnd) {
				sqlSecundario.append(" AND ");
			}
			sqlSecundario.append(" PERM LIKE ?");
			ps.adicionarString( descricao +"%");
			clausulaAnd = true;
		}
		sqlQtde = "SELECT COUNT(*) AS QUANTIDADE FROM " + sqlSecundario.toString();
		sqlSecundario.append(" ORDER BY PERM_CODIGO_PAI, PERM, E_MENU");
		
		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs12 = null;
		try{
			rs1 = consultarPaginacao(sqlPrincipal.toString() + sqlSecundario.toString(),ps,posicao);
			while (rs1.next()) {
				PermissaoDt permissaoDt = new PermissaoDt();
				permissaoDt.setId(rs1.getString("ID_PERM"));
				permissaoDt.setPermissao(rs1.getString("PERM"));
				permissaoDt.setPermissaoCodigo(rs1.getString("PERM_CODIGO"));
				permissaoDt.setId_PermissaoPai(rs1.getString("ID_PERM_PAI"));
				permissaoDt.setPermissaoCodigoPai(rs1.getString("PERM_CODIGO_PAI"));
				permissaoDt.setPermissaoPai(rs1.getString("PERM_PAI"));
				permissaoDt.setEMenu(rs1.getString("E_MENU"));
				permissaoDt.setLink(rs1.getString("LINK"));
				permissaoDt.setTitulo(rs1.getString("TITULO"));
				permissaoDt.setIrPara(rs1.getString("IR_PARA"));
				permissaoDt.setOrdenacao(rs1.getString("ORDENACAO"));
				permissaoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				listaPermissoes.add(permissaoDt);
			}
			//rs1.close();
			
			rs12 = consultar(sqlQtde.toString(),ps);
			if (rs12.next()) 
				listaPermissoes.add(rs12.getLong("QUANTIDADE"));
			
			//rs12.close();
			
		} finally{
			try{if(rs1 != null) rs1.close();} catch(Exception e) {}
			try{if(rs12 != null) rs12.close();} catch(Exception e) {}
		}
		return listaPermissoes;
	}

	public PermissaoDt consultarId(String idPermissao) throws Exception {
		String sql;
		PermissaoDt permissaoDt = new PermissaoDt();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT * FROM PROJUDI.VIEW_PERM WHERE ID_PERM = ?";
		ps.adicionarLong(idPermissao);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				permissaoDt.setId(rs1.getString("ID_PERM"));
				permissaoDt.setPermissao(rs1.getString("PERM"));
				permissaoDt.setPermissaoCodigo(rs1.getString("PERM_CODIGO"));
				permissaoDt.setId_PermissaoPai(rs1.getString("ID_PERM_PAI"));
				permissaoDt.setPermissaoPai(rs1.getString("PERM_PAI"));
				permissaoDt.setId_PermissaoEspecial(rs1.getString("ID_PERM_ESPECIAL"));
				permissaoDt.setPermissaoEspecial(rs1.getString("PERM_ESPECIAL"));
				permissaoDt.setPermissaoCodigoPai(rs1.getString("PERM_CODIGO_PAI"));
				permissaoDt.setEMenu(Funcoes.FormatarLogico(rs1.getString("E_MENU")));
				permissaoDt.setLink(rs1.getString("LINK"));
				permissaoDt.setTitulo(rs1.getString("TITULO"));
				permissaoDt.setIrPara(rs1.getString("IR_PARA"));
				permissaoDt.setOrdenacao(rs1.getString("ORDENACAO"));
				permissaoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
		} finally{
			try{if(rs1!=null) rs1.close(); } catch(Exception e) {}
		}
		return permissaoDt;
	}

	/**
	 * Método responsável por consultar as permissões do tipo função
	 * pertencentes à permissão pai passada como parâmetro
	 * 
	 * @author Keila Sousa Silva
	 * @param id_PermissaoPai
	 * @param permissaoCodigoPai
	 * @return listaPermissoesTipoFuncao = permissões do tipo função
	 *         pertencentes à permissão pai passada como parâmetro
	 * @throws Exception
	 */
	public List consultarFuncoesPermissao(String id_PermissaoPai, String permissaoPaiCodigo) throws Exception {
		String sql;
		List listaPermissoesTipoFuncao = new ArrayList();
		int permissaoCodigoInicial = Funcoes.StringToInt(permissaoPaiCodigo) * 10 + 0;
		int permissaoCodigoFinal = Funcoes.StringToInt(permissaoPaiCodigo) * 10 + 9;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		
		sql = "SELECT * FROM PROJUDI.VIEW_PERM WHERE ID_PERM_PAI = ?";
		ps.adicionarLong(id_PermissaoPai);
		sql += " AND PERM_CODIGO >= ?";
		ps.adicionarLong(permissaoCodigoInicial);
		sql += " AND PERM_CODIGO <= ?";
		ps.adicionarLong(permissaoCodigoFinal);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				PermissaoDt permissaoDt = new PermissaoDt();
				permissaoDt.setId(rs1.getString("ID_PERM"));
				permissaoDt.setPermissao(rs1.getString("PERM"));
				permissaoDt.setPermissaoCodigo(rs1.getString("PERM_CODIGO"));
				permissaoDt.setId_PermissaoPai(rs1.getString("ID_PERM_PAI"));
				permissaoDt.setPermissaoPai(rs1.getString("PERM_PAI"));
				permissaoDt.setPermissaoCodigoPai(rs1.getString("PERM_CODIGO_PAI"));
				permissaoDt.setEMenu(Funcoes.FormatarLogico(rs1.getString("E_MENU")));
				permissaoDt.setLink(rs1.getString("LINK"));
				permissaoDt.setTitulo(rs1.getString("TITULO"));
				permissaoDt.setIrPara(rs1.getString("IR_PARA"));
				permissaoDt.setOrdenacao(rs1.getString("ORDENACAO"));
				permissaoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
				listaPermissoesTipoFuncao.add(permissaoDt);
			}
		
		}
		finally{
			try{if(rs1!=null) rs1.close(); } catch(Exception e) {}
		}
		return listaPermissoesTipoFuncao;
	}

	/**
	 * Método responsável por buscar e retornar o maior código (PermissaoCodigo)
	 * das permissões não funções existentes no banco de dados na tabela
	 * "PERM"
	 * 
	 * @author Keila Sousa Silva
	 * @author Jesus Rodrigo 03/05/2019 - traser o menor codigo ainda não utilizado
	 * @return permissaoCodigoMaior = maior código das permissões não funções
	 *         ("Excluir", "Imprimir", "Localizar", "LocalizarDWR", "Novo",
	 *         "Salvar") existentes no banco de dados na tabela "PERM"
	 * @throws Exception
	 */
	public String consultarProximaPermissaoCodigo() throws Exception {
		String permissaoCodigoMaior = "";
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql = "Select (Perm_Codigo+1) AS PERM_CODIGO_MAIOR From Perm P Where Perm_Codigo>=? and Perm_Codigo<=? and not exists ( select 1 from Perm P1 Where p1.Perm_Codigo= (p.Perm_Codigo+1)) and rownum=1";
		
		//sql = "SELECT MAX(PERM_CODIGO) AS PERM_CODIGO_MAIOR FROM PROJUDI.VIEW_PERM WHERE PERM_CODIGO >= ? AND PERM_CODIGO <= ?";
		ps.adicionarLong(100);
		ps.adicionarLong(999);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				permissaoCodigoMaior = rs1.getString("PERM_CODIGO_MAIOR");
			}
		
		}
		finally{
			try{if(rs1!=null) rs1.close(); } catch(Exception e) {}
		}
		return permissaoCodigoMaior;
	}

	/**
	 * Método responsável por consultar uma permissão no banco de dados, usando
	 * como parâmetro o código da permissão (PermissaoCodigo) desejada
	 * 
	 * @author Keila Sousa Silva
	 * @param permissaoCodigo
	 * @return permissaoDt = objeto permissão
	 * @throws Exception
	 */
	public PermissaoDt consultarPermissaoCodigo(String permissaoCodigo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		sql = "SELECT * FROM PROJUDI.VIEW_PERM WHERE PERM_CODIGO = ?";
		ps.adicionarLong(permissaoCodigo);
		ResultSetTJGO rs1 = null;
		PermissaoDt permissaoDt = null;
		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				permissaoDt = new PermissaoDt();
				permissaoDt.setId(rs1.getString("ID_PERM"));
				permissaoDt.setPermissao(rs1.getString("PERM"));
				permissaoDt.setPermissaoCodigo(rs1.getString("PERM_CODIGO"));
				permissaoDt.setId_PermissaoPai(rs1.getString("ID_PERM_PAI"));
				permissaoDt.setPermissaoPai(rs1.getString("PERM_PAI"));
				permissaoDt.setPermissaoCodigoPai(rs1.getString("PERM_CODIGO_PAI"));
				permissaoDt.setEMenu(Funcoes.FormatarLogico(rs1.getString("E_MENU")));
				permissaoDt.setLink(rs1.getString("LINK"));
				permissaoDt.setTitulo(rs1.getString("TITULO"));
				permissaoDt.setIrPara(rs1.getString("IR_PARA"));
				permissaoDt.setOrdenacao(rs1.getString("ORDENACAO"));
				permissaoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
		
		}
		finally{
			try{if(rs1!=null) rs1.close(); } catch(Exception e) {}
		}
		return permissaoDt;
	}
	
	public PermissaoDt[] consultaFuncoesPermissao(String permissaoPaiCodigo) throws Exception {
		String sql;
		PermissaoDt[] listaPermissoesTipoFuncao = new PermissaoDt[10];
		int permissaoCodigoInicial = Funcoes.StringToInt(permissaoPaiCodigo) * 10 + 0;
		int permissaoCodigoFinal = Funcoes.StringToInt(permissaoPaiCodigo) * 10 + 9;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		boolean listaVazia = true;
		int indice;
		
		sql = "SELECT * FROM PROJUDI.VIEW_PERM WHERE";
		sql += " PERM_CODIGO >= ?";
		ps.adicionarLong(permissaoCodigoInicial);
		sql += " AND PERM_CODIGO <= ?";
		ps.adicionarLong(permissaoCodigoFinal);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql,ps);
			while (rs1.next()) {
				PermissaoDt permissaoDt = new PermissaoDt();
				permissaoDt.setId(rs1.getString("ID_PERM"));
				permissaoDt.setPermissaoCodigo(rs1.getString("PERM_CODIGO"));
				permissaoDt.setPermissao(rs1.getString("PERM"));
				permissaoDt.setTitulo(rs1.getString("TITULO"));
				permissaoDt.setId_PermissaoPai(rs1.getString("ID_PERM_PAI"));
				permissaoDt.setPermissaoPai(rs1.getString("PERM_PAI"));
				permissaoDt.setPermissaoCodigoPai(rs1.getString("PERM_CODIGO_PAI"));
				listaVazia = false;
				indice = Funcoes.StringToInt(permissaoDt.getPermissaoCodigo().substring(permissaoDt.getPermissaoCodigo().length()-1));
				listaPermissoesTipoFuncao[indice] = permissaoDt;
			}
		
		}
		finally{
			try{if(rs1!=null) rs1.close(); } catch(Exception e) {}
		}
		if(listaVazia) listaPermissoesTipoFuncao = null;
		return listaPermissoesTipoFuncao;
	}
	
	public void excluirFuncoes( String permissaoCodigoPai) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		stSql= "DELETE FROM PROJUDI.PERM";
		stSql += " WHERE ID_PERM_PAI = ?";		ps.adicionarLong(permissaoCodigoPai); 
			executarUpdateDelete(stSql,ps);

		

	} 
	
	public String consultarDescricaoJSON(String codigo, String descricao, String posicao) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		String stTemp = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = "SELECT ID_PERM AS ID, PERM_CODIGO AS DESCRICAO2, PERM AS DESCRICAO1, PERM_CODIGO_PAI AS DESCRICAO3, PERM_PAI AS DESCRICAO4, E_MENU AS DESCRICAO5, ORDENACAO AS DESCRICAO6";
		sqlFrom = " FROM PROJUDI.VIEW_PERM";
		sqlFrom += " WHERE PERM LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		if(codigo != null && !codigo.equals("")) {
			sqlFrom += " AND PERM_CODIGO = ?" ;
			ps.adicionarLong(codigo);
		}
		sqlOrder = " ORDER BY PERM_CODIGO";
		int qtdeColunas = 6;
		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			
			sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(sql + sqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
		} finally{
			try{if(rs1 != null) rs1.close();} catch(Exception e) {}
			try{if(rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp;
	}
}
