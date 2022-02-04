package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;

import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class UsuarioServentiaPs extends UsuarioServentiaPsGen {

	public UsuarioServentiaPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Retorna usuarios da serventia
	 * @author lsbernardes
	 * @param String id_serventia, id da serventia 
	 * @return List
	 * @throws Exception
	 */
	public List consultarUsuariosServentia(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		
		List lista = new ArrayList();
		String Sql;
		String SqlFrom;
		String SqlOrder;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT DISTINCT u.ID_USU,u.NOME, u.USU, us.ID_USU_SERV, s.SERV, us.PODE_GUARDAR_ASSINAR_USUCHEFE ";
		SqlFrom = " FROM  PROJUDI.USU u  ";
		SqlFrom += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		SqlFrom += " JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV ";
		SqlFrom += " JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		SqlFrom += " JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		SqlFrom += " WHERE  NOME LIKE ? AND us.ID_SERV = ? AND GRUPO_CODIGO IN (?, ?, ?, ?, ? , ?, ?) AND u.ATIVO = ? AND us.ATIVO = ? ";
		ps.adicionarString( nomeBusca + "%");
		ps.adicionarLong(id_Serventia);
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		ps.adicionarLong(1);
		ps.adicionarLong(1);
		SqlOrder = " ORDER BY NOME ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicaoPaginaAtual);

			while (rs1.next()) {				
				UsuarioServentiaDt usuarioServentia = new UsuarioServentiaDt();
				usuarioServentia.setId(rs1.getString("ID_USU_SERV"));
				usuarioServentia.setUsuarioServentia(rs1.getString("NOME"));
				usuarioServentia.setServentia(rs1.getString("SERV"));
				usuarioServentia.setPodeGuardarAssinarUsuarioServentiaChefe(Funcoes.FormatarLogico(rs1.getString("PODE_GUARDAR_ASSINAR_USUCHEFE")));

				lista.add(usuarioServentia);
			}
			
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())  lista.add(rs2.getLong("QUANTIDADE"));			
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	/**
	 * Método que consulta uma lista de usuários serventia filtrados pelo grupo e serventia deles.
	 * @param nomeUsuario - nome do usuário
	 * @param idServentia - ID da serventia dos usuários
	 * @param grupoCodigo - código do grupo dos usuários
	 * @return lista de usuários
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarGrupoEspecificoServentiaJSON(String nomeUsuario, String idServentia, String grupoCodigo, String posicaoPaginaAtual) throws Exception {
		
		String stTemp = "";
		String Sql;
		String SqlFrom;
		String SqlOrder;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = " SELECT DISTINCT u.NOME AS DESCRICAO1, u.USU, us.ID_USU_SERV AS ID, s.SERV AS DESCRICAO2 ";
		SqlFrom = " FROM  PROJUDI.USU u  ";
		SqlFrom += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		SqlFrom += "  JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV ";
		SqlFrom += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		SqlFrom += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		SqlFrom += " WHERE  NOME LIKE ? AND us.ID_SERV = ? AND GRUPO_CODIGO = ? AND u.ATIVO = ? AND us.ATIVO = ? ";
		
		ps.adicionarString( nomeUsuario +"%");
		ps.adicionarLong(idServentia);
		ps.adicionarLong(grupoCodigo);
		ps.adicionarLong(UsuarioDt.ATIVO);
		ps.adicionarLong(UsuarioServentiaDt.ATIVO);
		SqlOrder = " ORDER BY NOME ";
		
		try {
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicaoPaginaAtual);			
			
			Sql = "SELECT COUNT(1) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);			
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
		} finally {
             try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
             try {if (rs2 != null) rs2.close();} catch (Exception e1) {}
        } 
		return stTemp;
	}
	
	/**
	 * Retorna usuarios da serventia dos grupos de advogados
	 * @author mmgomes
	 * @param String id_serventia, id da serventia 
	 * @return List
	 * @throws Exception
	 */
	public String consultarUsuariosServentiaAdvogadosJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		//testeana -> PGE Execução Fiscal - GO
		String stTemp = "";
		String Sql, SqlFrom, SqlOrder;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = " SELECT DISTINCT u.ID_USU, u.NOME AS DESCRICAO1, u.USU, us.ID_USU_SERV AS ID, s.SERV AS DESCRICAO2 ";
		SqlFrom = " FROM  PROJUDI.USU u  ";
		SqlFrom += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		SqlFrom += "  JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV ";
		SqlFrom += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		SqlFrom += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		SqlFrom += " WHERE  NOME LIKE ? AND us.ID_SERV = ? ";							ps.adicionarString( nomeBusca +"%");	ps.adicionarLong(id_Serventia);
		SqlFrom += " AND GRUPO_CODIGO IN (?,?,?,?,?,?,?) ";								ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);	ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL); ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);  ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL); ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO); ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO); ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		SqlFrom += " AND u.ATIVO = ? AND us.ATIVO = ? AND sg.ATIVO = ? ";				ps.adicionarLong(1); ps.adicionarLong(1); ps.adicionarLong(1);
		SqlOrder = " ORDER BY NOME ";
		
		try{
			rs1 = consultarPaginacao(Sql+ SqlFrom + SqlOrder, ps, posicaoPaginaAtual);			
			
			Sql = "SELECT COUNT(1) as QUANTIDADE " + SqlFrom;
			
			rs2 = consultar(Sql, ps);			
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        } 
		return stTemp;
	}
	
	/**
	 * Retorna usuarios da serventia de todos grupos
	 * @author mmgomes
	 * @param String id_serventia, id da serventia 
	 * @return List
	 * @throws Exception
	 */
	public String consultarUsuariosServentiaTodosJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		//testeana -> PGE Execução Fiscal - GO
		String stTemp = "";
		String Sql, SqlFrom, SqlOrder;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = " SELECT DISTINCT u.ID_USU, u.NOME AS DESCRICAO1, u.USU, us.ID_USU_SERV AS ID, s.SERV AS DESCRICAO2 ";
		SqlFrom = " FROM  PROJUDI.USU u  ";
		SqlFrom += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		SqlFrom += "  JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV ";
		SqlFrom += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		SqlFrom += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		SqlFrom += " WHERE  NOME LIKE ? AND us.ID_SERV = ? ";							ps.adicionarString( nomeBusca +"%");	ps.adicionarLong(id_Serventia);
		SqlFrom += " AND u.ATIVO = ? AND us.ATIVO = ? AND sg.ATIVO = ? ";				ps.adicionarLong(1); ps.adicionarLong(1); ps.adicionarLong(1);
		SqlOrder = " ORDER BY NOME ";
		
		try{
			rs1 = consultarPaginacao(Sql+ SqlFrom + SqlOrder, ps, posicaoPaginaAtual);			
			
			Sql = "SELECT COUNT(1) as QUANTIDADE " + SqlFrom;
			
			rs2 = consultar(Sql, ps);			
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        } 
		return stTemp;
	}
	
	public String consultarUsuariosServentiaGrupoJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String grupo) throws Exception {
		String stTemp = "";
		String Sql;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = " SELECT DISTINCT u.ID_USU, u.NOME AS DESCRICAO1, u.USU, us.ID_USU_SERV AS ID, s.SERV AS DESCRICAO2 ";
		Sql += " FROM  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += "  JOIN PROJUDI.GRUPO_TIPO gt on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += " WHERE  NOME LIKE ? AND us.ID_SERV = ? AND g.GRUPO_CODIGO = ? AND u.ATIVO = ? AND us.ATIVO = ? ";
		ps.adicionarString("%" + nomeBusca + "%");
		ps.adicionarLong(id_Serventia);
		ps.adicionarLong(grupo);
		ps.adicionarLong(1);
		ps.adicionarLong(1);
		Sql += " ORDER BY NOME ";
		
		try{
			rs1 = consultarPaginacao(Sql, ps, posicaoPaginaAtual);			
			
			Sql = "SELECT COUNT(1) as QUANTIDADE ";
			Sql += " FROM  PROJUDI.USU u  ";
			Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
			Sql += "  JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV ";
			Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
			Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
			Sql += " WHERE  NOME LIKE ? AND us.ID_SERV = ? AND g.GRUPO_CODIGO = ? AND u.ATIVO = ? AND us.ATIVO = ? ";
			rs2 = consultar(Sql, ps);			
			rs2.next();
			
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        } 
		return stTemp;
	}
	
	/**
     * 
     */
    private static final long serialVersionUID = -7705956723613781070L;

//    /**
//	 * Retorna uma lista com os servidores da serventia utilizando alguns filtros
//	 * @author asrocha
//	 * @param nome nome do servidor
//	 * @param usuario usuario utilizado pelo servidor
//	 * @param posicao numero da pagina para o mapeamento do dwr
//	 * @param idServentia id da serventia
//	 * @return
//	 * @throws Exception
//	 */
//	 
//	public List consultarDescricaoServidorJudiciarioServentia(String nome, String usuario, String posicao, String idServentia) throws Exception {
//		String Sql;
//        List liTemp = new ArrayList();
//        ResultSetTJGO rs1 = null;
//        ResultSetTJGO rs2 = null;
//        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//        
//        Sql  = " SELECT DISTINCT u.ID_USU,NOME,USU,RG,CPF ";
//        Sql += " FROM PROJUDI.USU u";
//		Sql += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
//		Sql += " WHERE USU LIKE ?";								ps.adicionarString( usuario +"%"); 		
//		Sql += " AND NOME LIKE ?";								ps.adicionarString( nome +"%");
//		Sql += " AND ID_SERV = ?";								ps.adicionarLong(idServentia);
//		Sql += " AND MATRICULA_TJGO IS NOT NULL";
//        Sql += " ORDER BY NOME";
//                
//
//        try{
//            rs1 = consultarPaginacao(Sql, ps, posicao);
//            while (rs1.next()) {
//                UsuarioDt obTemp = new UsuarioDt();
//                obTemp.setId(rs1.getString("ID_USU"));
//                obTemp.setNome(rs1.getString("NOME"));
//                obTemp.setUsuario(rs1.getString("USU"));
//                obTemp.setRg(rs1.getString("RG"));
//                obTemp.setCpf(rs1.getString("CPF"));
//                liTemp.add(obTemp);
//            }
//            //rs1.close();
//            Sql  = " SELECT COUNT(*) AS Quantidade FROM (SELECT DISTINCT u.ID_USU,NOME,USU,RG,CPF ";
//            Sql += " FROM PROJUDI.USU u";
//    		Sql += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
//    		Sql += " WHERE USU LIKE ?"; 
//    		Sql += " AND NOME LIKE ?";
//    		Sql += " AND ID_SERV = ?";
//    		Sql += " AND MATRICULA_TJGO IS NOT NULL) TAB";
//            
//            rs2 = consultar(Sql, ps);
//            if (rs2.next()) liTemp.add(rs2.getLong("QUANTIDADE"));            
//            //rs1.close();
//        
//        } finally{
//             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
//             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
//        }
//        return liTemp;
//	}
	
	/**
	 * Busca serventias vinculadas a um usuário
	 */
	public List consultarServentiasUsuario(String usuario) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_USU_SERV,SERV, ID_SERV, SERV_CODIGO, ID_COMARCA, ID_CIDADE, PODE_GUARDAR_ASSINAR_USUCHEFE FROM PROJUDI.VIEW_USU_SERV ";
		Sql += " WHERE USU LIKE ? AND ATIVO = ?";
		ps.adicionarString( usuario +"%");
		ps.adicionarLong(1);
		Sql += " ORDER BY USU";
		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				UsuarioServentiaDt obTemp = new UsuarioServentiaDt();
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setServentiaCodigo(rs1.getString("SERV_CODIGO"));
				obTemp.setId(rs1.getString("ID_USU_SERV"));
				obTemp.setId_Comarca(rs1.getString("ID_COMARCA"));
				obTemp.setId_Cidade(rs1.getString("ID_CIDADE"));
				obTemp.setPodeGuardarAssinarUsuarioServentiaChefe(Funcoes.FormatarLogico(rs1.getString("PODE_GUARDAR_ASSINAR_USUCHEFE")));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}

	public void alterar(UsuarioServentiaDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_SERV SET  ";
		if (!(dados.getId_Serventia().length() == 0)){
			Sql += ",ID_SERV = ?";
			ps.adicionarLong(dados.getId_Serventia());
		}
		if (!(dados.getId_Usuario().length() == 0)){
			Sql += ",ID_USU = ?";
			ps.adicionarLong(dados.getId_Usuario());
		}
		/*if (!(dados.getId_UsuarioTipo().length() == 0)) Sql += ",Id_UsuarioTipo = " + Funcoes.BancoInteiro(dados.getId_UsuarioTipo());*/
		if (!(dados.getAtivo().length() == 0)){
			Sql += ",ATIVO = ?";
			ps.adicionarBoolean(dados.getAtivo());
		}
		if (!(dados.getPodeGuardarAssinarUsuarioServentiaChefe().length() == 0)){
			Sql += ",PODE_GUARDAR_ASSINAR_USUCHEFE = ?";
			ps.adicionarBoolean(dados.getPodeGuardarAssinarUsuarioServentiaChefe());
		}
		Sql = Sql + ",CODIGO_TEMP = null";
		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_USU_SERV = ?";
		ps.adicionarLong(dados.getId());

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Obter registro em UsuarioServentia de acordo com usuário e serventia passados
	 */
	public UsuarioServentiaDt buscaUsuarioServentiaId(String id_Usuario, String id_Serventia) throws Exception {

		String Sql;
		UsuarioServentiaDt Dados = new UsuarioServentiaDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_USU_SERV ";
		Sql += " WHERE ID_USU = ?";						ps.adicionarLong(id_Usuario);
		Sql += " AND ID_SERV = ?";						ps.adicionarLong(id_Serventia);
//		Sql += " AND ATIVO = ? ";						ps.adicionarLong(1);
		Sql += " order by id_usu_Serv ";
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU_SERV"));
				Dados.setId_Serventia(rs1.getString("ID_SERV"));
				Dados.setServentia(rs1.getString("SERV"));
				Dados.setServentiaCodigo(rs1.getString("SERV_CODIGO"));
				Dados.setId_Usuario(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setId_UsuarioServentiaChefe(rs1.getString("ID_USU_SERV_CHEFE"));
				Dados.setPodeGuardarAssinarUsuarioServentiaChefe(Funcoes.FormatarLogico(rs1.getString("PODE_GUARDAR_ASSINAR_USUCHEFE")));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}
	
	/**
	 * Obter registro em UsuarioServentia de acordo com usuário e serventia passados
	 */
	public UsuarioServentiaDt buscaUsuarioServentiaId(String id_Usuario, String id_Serventia, String id_UsuarioServentiaChefe) throws Exception {

		String Sql;
		UsuarioServentiaDt Dados = new UsuarioServentiaDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_USU_SERV ";
		Sql += " WHERE ID_USU = ?";
		ps.adicionarLong(id_Usuario);
		Sql += " AND ID_SERV = ?";
		ps.adicionarLong(id_Serventia);
		Sql += " AND ATIVO = ? ";
		ps.adicionarLong(1);
		Sql += " AND ID_USU_SERV_CHEFE = ?";
		ps.adicionarLong(id_UsuarioServentiaChefe);
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU_SERV"));
				Dados.setId_Serventia(rs1.getString("ID_SERV"));
				Dados.setServentia(rs1.getString("SERV"));
				Dados.setServentiaCodigo(rs1.getString("SERV_CODIGO"));
				Dados.setId_Usuario(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setPodeGuardarAssinarUsuarioServentiaChefe(Funcoes.FormatarLogico(rs1.getString("PODE_GUARDAR_ASSINAR_USUCHEFE")));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	public List consultarDescricao(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_USU_SERV, USU_SERV, SERV";
		SqlFrom = " FROM PROJUDI.VIEW_USU_SERV";
		SqlFrom += " WHERE USU_SERV LIKE ?";
		ps.adicionarString( descricao +"%");		
		SqlOrder = " ORDER BY USU_SERV";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				UsuarioServentiaDt obTemp = new UsuarioServentiaDt();
				obTemp.setId(rs1.getString("ID_USU_SERV"));
				obTemp.setUsuarioServentia(rs1.getString("USU_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			while (rs2.next()) {
				liTemp.add(rs2.getLong("QUANTIDADE"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	//CONSULTA (UsuarioServentia) ATIVOS OU INATIVOS POR ID Usuario
	public List consultarDescricaoUsuarioServentia(String id_Usuario) throws Exception {

		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

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
		Sql += " WHERE u.ID_USU = ?";
		ps.adicionarLong(id_Usuario);
		Sql += " ORDER BY u.USU";

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
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}

	/**
	 * Consulta o Id_UsuarioServentia correspondente a um parte, baseado
	 * no cpf passado 
	 * @param String cpf, cpf da parte
	 */
	public UsuarioServentiaDt consultarUsuarioServentiaParte(String cpf) throws Exception {
		UsuarioServentiaDt usuarioServentiaDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "Select us.* FROM PROJUDI.USU u";
		sql += " INNER JOIN PROJUDI.VIEW_USU_SERV us on u.ID_USU=us.ID_USU";
		sql += " WHERE u.CPF = ?";
		ps.adicionarString(cpf);
		try{
			rs1 = this.consultar(sql, ps);
			//Se possui proximo registro
			if (rs1.next()) {
				usuarioServentiaDt = new UsuarioServentiaDt();
				super.associarDt(usuarioServentiaDt, rs1);
			}

			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return usuarioServentiaDt;
	}

	/**
	 * Altera o status de um UsuarioServentia passado, desativando ou ativando.
	 * 
	 * @param id_UsuarioServentia, identificação do UsuarioServentia
	 * @param novoStatus, novo status
	 * 
	 * @author msapaula
	 */
	public void alterarStatusUsuarioServentia(String id_UsuarioServentia, int novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_SERV SET ATIVO = ?";
		ps.adicionarLong(novoStatus);
		Sql += " WHERE ID_USU_SERV = ?";
		ps.adicionarLong(id_UsuarioServentia);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Método responsável em alterar status de todos os registros em UsuarioServentia de um usuário
	 * 
	 * @param id_Usuario, identificação do usuário
	 * @param novoStatus, novo status
	 * @author msapaula
	 */
	public void alterarStatusId_Usuario(String id_Usuario, int novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_SERV SET ATIVO = ?";		ps.adicionarLong(novoStatus);
		Sql += " WHERE ID_USU = ?";							ps.adicionarLong(id_Usuario);

			executarUpdateDelete(Sql, ps);
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 2;

		Sql = "SELECT ID_USU_SERV AS ID, USU_SERV AS DESCRICAO1, SERV AS DESCRICAO2";
		SqlFrom = " FROM PROJUDI.VIEW_USU_SERV";
		SqlFrom += " WHERE USU_SERV LIKE ?";
		ps.adicionarString( descricao +"%");	
		SqlOrder = " ORDER BY USU_SERV";		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
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
	 * Método para verificar se usuário é contador.
	 * 
	 * @param String idUsurio
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isUsuarioContador(String idUsurio) throws Exception {
		boolean retorno = false;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try{
			sql = "SELECT US.ID_USU_SERV FROM PROJUDI.USU_SERV US, PROJUDI.SERV S, PROJUDI.SERV_TIPO ST WHERE US.ID_USU = ? AND S.ID_SERV = US.ID_SERV AND S.ID_SERV_TIPO = ST.ID_SERV_TIPO AND US.ATIVO = 1 AND ST.ID_SERV_TIPO = ?";
			
			ps.adicionarLong(idUsurio);
			ps.adicionarLong(ServentiaTipoDt.CONTADORIA);
			
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				if (rs1.next()) {
					retorno = true;
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se usuário é advogado.
	 * 
	 * @param String idUsurio
	 * @param String idServentia
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean isUsuarioAdvogado(String idUsuario, String idServentia) throws Exception {
		boolean retorno = false;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try{
			sql = "SELECT US.ID_USU_SERV FROM PROJUDI.USU_SERV US, PROJUDI.SERV S, PROJUDI.SERV_TIPO ST WHERE US.ID_USU = ? AND S.ID_SERV = ? AND S.ID_SERV = US.ID_SERV AND S.ID_SERV_TIPO = ST.ID_SERV_TIPO AND US.ATIVO = 1 AND ST.ID_SERV_TIPO = ?";
			
			ps.adicionarLong(idUsuario);
			ps.adicionarLong(idServentia);
			ps.adicionarLong(ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
			
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				if (rs1.next()) {
					retorno = true;
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se usuário é Gerenciamento Projudi.
	 * 
	 * @param String idUsurio
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isUsuarioGerenciamentoProjudi(String idUsurio) throws Exception {
		boolean retorno = false;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try{
			sql = "SELECT US.ID_USU_SERV FROM PROJUDI.USU_SERV US, PROJUDI.SERV S, PROJUDI.SERV_TIPO ST WHERE US.ID_USU = ? AND S.ID_SERV = US.ID_SERV AND S.ID_SERV_TIPO = ST.ID_SERV_TIPO AND US.ATIVO = 1 AND ST.ID_SERV_TIPO = ?";
			
			ps.adicionarLong(idUsurio);
			ps.adicionarLong(ServentiaTipoDt.GERENCIAMENTO_SISTEMA_PROJUDI);
			
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				if (rs1.next()) {
					retorno = true;
				}
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return retorno;
	}
	
	/**
	 * Retorna usuarios da serventia que podem analisar pendências em uma determinada serventia.
	 * @author mmgomes
	 * @param String id da serventia 
	 * @return List
	 * @throws Exception
	 */
	public List consultarTodosUsuariosAnalisamPendencias(String id_Serventia) throws Exception {
		List lista = new ArrayList();
		String Sql;
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT DISTINCT u.ID_USU,u.NOME, u.USU, us.ID_USU_SERV, s.SERV, u.email ";
		Sql += " FROM PROJUDI.SERV_CARGO sc  ";
		Sql += "  INNER JOIN PROJUDI.USU_SERV_GRUPO usg on usg.ID_USU_SERV_GRUPO = sc.ID_USU_SERV_GRUPO  ";
		Sql += "  INNER JOIN PROJUDI.USU_SERV us on us.ID_USU_SERV = usg.ID_USU_SERV ";
		Sql += "  INNER JOIN PROJUDI.USU u  on us.ID_USU = u.ID_USU  ";
		Sql += "  INNER JOIN PROJUDI.SERV s on s.ID_SERV = sc.ID_SERV ";
		Sql += "  INNER JOIN PROJUDI.GRUPO g on usg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += " WHERE us.ID_SERV = ? AND g.GRUPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?) AND u.ATIVO = ? AND us.ATIVO = ? ";		
		ps.adicionarLong(id_Serventia);
		ps.adicionarLong(GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL);
		ps.adicionarLong(GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL);
		ps.adicionarLong(GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL);
		ps.adicionarLong(GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL);
		ps.adicionarLong(GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL);
		ps.adicionarLong(GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL);
		ps.adicionarLong(GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL);
		ps.adicionarLong(GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL);
		ps.adicionarLong(GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL);
		ps.adicionarLong(GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL);
		ps.adicionarLong(GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL);
		ps.adicionarLong(GrupoDt.CONTADORES_VARA);
		ps.adicionarLong(GrupoDt.ANALISTA_FORENSE);
		ps.adicionarLong(GrupoDt.ANALISTA_FORENSE_2_GRAU);
		ps.adicionarLong(1);
		ps.adicionarLong(1);
		Sql += " ORDER BY NOME ";
		
		try{
			rs1 = consultar(Sql, ps);

			while (rs1.next()) {				
				UsuarioServentiaDt usuarioServentia = new UsuarioServentiaDt();
				usuarioServentia.setId(rs1.getString("ID_USU_SERV"));
				usuarioServentia.setUsuarioServentia(rs1.getString("NOME"));
				usuarioServentia.setServentia(rs1.getString("SERV"));
				usuarioServentia.setEmail(rs1.getString("email"));

				lista.add(usuarioServentia);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}             
        } 
		return lista;
	}
	
	/**
	 * Método para verificar se foi um magistrado que gerou determinada movimentação
	 * @param id_Serventia
	 * @return boolean
	 * @throws Exception
	 */
	public boolean consultarGrupoUsuarioServentia(String id_usuarioserventia )  throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		boolean retorno = false;

		stSql= "SELECT g.GRUPO_CODIGO FROM PROJUDI.USU_SERV us "
				+ " INNER JOIN PROJUDI.USU_SERV_GRUPO usg ON usg.id_usu_serv = us.id_usu_serv   "
				+ " INNER JOIN PROJUDI.GRUPO g ON g.id_grupo = usg.id_grupo "
				+ " WHERE us.ID_USU_SERV = ? "		 
				+  " AND g.GRUPO_CODIGO IN (?,?,?,?,?,?) ";
				ps.adicionarLong(id_usuarioserventia);
				ps.adicionarLong(GrupoDt.JUIZES_VARA);
				ps.adicionarLong(GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU);
				ps.adicionarLong(GrupoDt.JUIZES_TURMA_RECURSAL);
				ps.adicionarLong(GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL);
				ps.adicionarLong(GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL);
				ps.adicionarLong(GrupoDt.DESEMBARGADOR);

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				retorno = true;
			}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return retorno; 
	}
	
	public List consultarProcuradoresServentia(String posicaoPaginaAtual, String id_Serventia) throws Exception {
		
		List lista = new ArrayList();
		String Sql;
		String SqlFrom;
		String SqlOrder;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = " SELECT DISTINCT u.ID_USU,u.NOME, u.USU, us.ID_USU_SERV, s.SERV, us.PODE_GUARDAR_ASSINAR_USUCHEFE ";
		SqlFrom = " FROM  PROJUDI.USU u  ";
		SqlFrom += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		SqlFrom += "  JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV ";
		SqlFrom += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		SqlFrom += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		SqlFrom += " WHERE us.ID_SERV = ? AND GRUPO_CODIGO IN (?,?,?,?,?,?,?) AND u.ATIVO = ? AND us.ATIVO = ? ";
		ps.adicionarLong(id_Serventia);
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		ps.adicionarLong(1);
		ps.adicionarLong(1);
		SqlOrder = " ORDER BY NOME ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicaoPaginaAtual);

			while (rs1.next()) {				
				UsuarioServentiaDt usuarioServentia = new UsuarioServentiaDt();
				usuarioServentia.setId(rs1.getString("ID_USU_SERV"));
				usuarioServentia.setNome(rs1.getString("NOME"));
				usuarioServentia.setUsuario(rs1.getString("USU"));
				usuarioServentia.setPodeGuardarAssinarUsuarioServentiaChefe(Funcoes.FormatarLogico(rs1.getString("PODE_GUARDAR_ASSINAR_USUCHEFE")));

				lista.add(usuarioServentia);
			}
			
			Sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next())  lista.add(rs2.getLong("QUANTIDADE"));			
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	public String consultarDescricaoServidorJudiciarioServentiaJSON(String nome, String usuario, String posicao, String idServentia) throws Exception {
		String Sql;
        String stTemp = "";
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        int qtdeColunas = 4;
        
        Sql  = " SELECT DISTINCT u.ID_USU AS ID, NOME AS DESCRICAO1, USU AS DESCRICAO2, RG AS DESCRICAO3, CPF AS DESCRICAO4 ";
        Sql += " FROM PROJUDI.USU u";
		Sql += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += " WHERE USU LIKE ?";		ps.adicionarString( usuario +"%"); 
		Sql += " AND NOME LIKE ?";		ps.adicionarString( nome +"%");
		Sql += " AND ID_SERV = ?";		ps.adicionarLong(idServentia);
		Sql += " AND MATRICULA_TJGO IS NOT NULL";
        Sql += " ORDER BY NOME";
                

        try{
            rs1 = consultarPaginacao(Sql, ps, posicao);
            Sql  = " SELECT COUNT(*) AS Quantidade FROM (SELECT DISTINCT u.ID_USU,NOME,USU,RG,CPF ";
            Sql += " FROM PROJUDI.USU u";
    		Sql += " JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
    		Sql += " WHERE USU LIKE ?"; 
    		Sql += " AND NOME LIKE ?";
    		Sql += " AND ID_SERV = ?";
    		Sql += " AND MATRICULA_TJGO IS NOT NULL) TAB";
            
            rs2 = consultar(Sql, ps);
            rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);            
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
        return stTemp;
	}
		
	//metodo que retorna o nome e o grupo do responsável pela pendência, quando houver alteração. Busca com base em id_ServentiaCargo.
	public String consultarNomePendenciaResponsavel(String idServentiaCargo)  throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String retorno = "";
		
		stSql = "SELECT u.NOME, g.GRUPO FROM PROJUDI.USU u ";
		stSql += "INNER JOIN PROJUDI.USU_SERV us ON us.ID_USU = u.ID_USU ";
		stSql += "INNER JOIN PROJUDI.USU_SERV_GRUPO usg ON usg.ID_USU_SERV = us.ID_USU_SERV ";
		stSql += "INNER JOIN PROJUDI.GRUPO g ON usg.ID_GRUPO = g.ID_GRUPO ";
		stSql += "INNER JOIN PROJUDI.SERV_CARGO sc ON sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO ";
		stSql += "WHERE sc.ID_SERV_CARGO = ? ";		
		
		ps.adicionarLong(idServentiaCargo);
		
		try{			
			rs1 = consultar(stSql,ps);
			while(rs1.next()) {				
				
				retorno += rs1.getString("NOME");
				retorno += " - ";
				retorno += rs1.getString("GRUPO");
				
			}
		}finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {}
		}
		return retorno; 
	}
	
	/**
	 * Método responsável em alterar status do UsuarioServentia a partir de um usuario serventia grupo
	 * 
	 * @param id_UsuServGrupo, identificação do usuário serventia grupo
	 * @author asrocha
	 */
	public void ativarUsuarioServentia(String id_UsuServGrupo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_SERV SET ATIVO = ?";		ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
		Sql += " WHERE ID_USU_SERV = (SELECT ID_USU_SERV FROM PROJUDI.USU_SERV_GRUPO WHERE ID_USU_SERV_GRUPO = ?)";		ps.adicionarLong(id_UsuServGrupo);

		executarUpdateDelete(Sql, ps);
	}

	// jvosantos - 26/11/2019 14:21 - Método para buscar o ID_USU_SERV usando o ID_SERV_CARGO
	public String consultarIdUsuarioServentiaPorIdServentiaCargo(String idServentiaCargo) throws Exception {
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		stSql = "SELECT us.ID_USU_SERV FROM PROJUDI.USU_SERV us INNER JOIN PROJUDI.USU_SERV_GRUPO usg ON usg.ID_USU_SERV = us.ID_USU_SERV INNER JOIN PROJUDI.GRUPO g ON usg.ID_GRUPO = g.ID_GRUPO INNER JOIN PROJUDI.SERV_CARGO sc ON sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO WHERE sc.ID_SERV_CARGO = ?";		
		
		ps.adicionarLong(idServentiaCargo);
		
		try{			
			rs1 = consultar(stSql,ps);
			if(rs1.next()) {
				return rs1.getString("ID_USU_SERV");
			}
		}finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {}
		}
		return null; 
	}
	
	/**
	 * Obter registro em UsuarioServentia de acordo com usuário e serventia passados
	 */
	public UsuarioServentiaDt consultaUsuarioServentiaId(String id_Usuario, String id_Serventia) throws Exception {

		String Sql;
		UsuarioServentiaDt Dados = new UsuarioServentiaDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_USU_SERV ";
		Sql += " WHERE ID_USU = ?";
		ps.adicionarLong(id_Usuario);
		Sql += " AND ID_SERV = ?";
		ps.adicionarLong(id_Serventia);
		Sql += " AND ATIVO = ? ";
		ps.adicionarLong(1);
		Sql += " AND ID_USU_SERV_CHEFE IS NULL";
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU_SERV"));
				Dados.setId_Serventia(rs1.getString("ID_SERV"));
				Dados.setServentia(rs1.getString("SERV"));
				Dados.setServentiaCodigo(rs1.getString("SERV_CODIGO"));
				Dados.setId_Usuario(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
				Dados.setPodeGuardarAssinarUsuarioServentiaChefe(Funcoes.FormatarLogico(rs1.getString("PODE_GUARDAR_ASSINAR_USUCHEFE")));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}
	
	/**
	 * Obter registro em UsuarioServentia de acordo com id_usu_serv
	 */
	public UsuarioServentiaDt buscaUsuarioServentiaIdUsuServ(String id_UsuServ) throws Exception {

		String Sql;
		UsuarioServentiaDt Dados = new UsuarioServentiaDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_USU_SERV ";
		Sql += " WHERE ID_USU_SERV = ?";
		ps.adicionarLong(id_UsuServ);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados.setId(rs1.getString("ID_USU_SERV"));
				Dados.setId_Serventia(rs1.getString("ID_SERV"));
				Dados.setServentia(rs1.getString("SERV"));
				Dados.setServentiaCodigo(rs1.getString("SERV_CODIGO"));
				Dados.setId_Usuario(rs1.getString("ID_USU"));
				Dados.setUsuario(rs1.getString("USU"));
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados;
	}

	
	
	
}
