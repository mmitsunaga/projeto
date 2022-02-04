package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;

import java.sql.Connection;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

// ---------------------------------------------------------
public class UsuarioServentiaGrupoPs extends UsuarioServentiaGrupoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -131106180270080812L;

    public UsuarioServentiaGrupoPs(Connection conexao){
    	Conexao = conexao;
	}


	public List consultarDescricao(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT ID_USU_SERV_GRUPO,USU_SERV_GRUPO, GRUPO, SERV";
		SqlFrom = " FROM PROJUDI.VIEW_USU_SERV_GRUPO";
		SqlFrom += " WHERE USU_SERV_GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlOrder = " ORDER BY USU_SERV_GRUPO";
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			while (rs1.next()) {
				UsuarioServentiaGrupoDt obTemp = new UsuarioServentiaGrupoDt();
				obTemp.setId(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setUsuarioServentiaGrupo(rs1.getString("USU_SERV_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setServentia(rs1.getString("SERV"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			Sql = "SELECT COUNT(*) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) 
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	/**
	 * Busca serventias e grupos vinculados a um usuário no momento após o logon, para que usuário
	 * possa definir em qual serventia e grupo irá trabalhar
	 * 
	 * @param id_Usuario, identificação do usuário que está logando
	 * @return lista de Usuários com todos os dados de serventia e grupo setados
	 */
	@SuppressWarnings("unchecked")
	public List consultarServentiasGrupos(String id_usuario) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = " SELECT u.ID_USU, u.USU, u.NOME, u.CPF, u.RG, u.SIGLA_ORGAO_EXPEDIDOR, u.ORGAO_EXPEDIDOR_SIGLA_ESTADO, ";
		Sql += " u.DATA_NASCIMENTO, u.EMAIL, u.CIDADE as USUARIO_CIDADE, u.UF as USU_ESTADO, u.SENHA, u.MATRICULA_TJGO, ";
		Sql += " us.ID_SERV, us.SERV, us.SERV_CODIGO, us.ID_USU_SERV, us.ID_COMARCA, us.COMARCA_CODIGO, ";
		Sql += " us.ID_CIDADE_SERV,g.ID_GRUPO, g.GRUPO, ser.ID_AUDI_TIPO,";
		Sql += " st.ID_SERV_TIPO, st.SERV_TIPO, st.SERV_TIPO_CODIGO, st.EXTERNA, sc.ID_SERV_CARGO, ct.CARGO_TIPO_CODIGO, ";
		Sql += " ct.CARGO_TIPO, g.GRUPO_CODIGO, gt.ID_GRUPO_TIPO, gt.GRUPO_TIPO, gt.GRUPO_TIPO_CODIGO, ss.ID_SERV_SUBTIPO, ss.SERV_SUBTIPO_CODIGO, e.UF as ESTADO_REPRESENTACAO, uo.OAB_NUMERO, uo.OAB_COMPLEMENTO, ";
		Sql += " us.ID_USU_SERV_CHEFE, us.USU_SERV_CHEFE, sc1.ID_SERV_CARGO as ID_SERV_CARGO_USU_CHEFE, sc1.SERV_CARGO as SERV_CARGO_USU_CHEFE,";
		Sql += " ct1.CARGO_TIPO_CODIGO as CARGO_TIPO_CODIGO_USU_CHEFE, ct1.CARGO_TIPO as CARGO_TIPO_USU_CHEFE, g1.GRUPO_CODIGO as GRUPO_USU_CHEFE, ser.ID_AREA, sc.SERV_CARGO ";
		Sql += " FROM PROJUDI.VIEW_USU_COMPLETO u ";
		Sql += "	INNER JOIN PROJUDI.VIEW_USU_SERV us on u.ID_USU=us.ID_USU";
		Sql += "    INNER JOIN PROJUDI.SERV ser on ser.ID_SERV= us.ID_SERV";
		Sql += "    INNER JOIN PROJUDI.SERV_TIPO st on ser.ID_SERV_TIPO=st.ID_SERV_TIPO";
		Sql += "    INNER JOIN PROJUDI.USU_SERV_GRUPO ug on us.ID_USU_SERV=ug.ID_USU_SERV";
		Sql += "	LEFT JOIN PROJUDI.SERV_CARGO sc on (ug.ID_USU_SERV_GRUPO=sc.ID_USU_SERV_GRUPO AND ser.ID_SERV=sc.ID_SERV)";
		Sql += "	LEFT JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		Sql += "    INNER JOIN PROJUDI.GRUPO g on ug.ID_GRUPO= g.ID_GRUPO";
		Sql += "	LEFT JOIN PROJUDI.GRUPO_TIPO gt on g.ID_GRUPO_TIPO = gt.ID_GRUPO_TIPO";
		Sql += "    LEFT JOIN PROJUDI.SERV_SUBTIPO ss on ss.ID_SERV_SUBTIPO= ser.ID_SERV_SUBTIPO";
		Sql += "    LEFT JOIN PROJUDI.USU_SERV_OAB uo on uo.ID_USU_SERV= us.ID_USU_SERV";
		Sql += "    LEFT JOIN PROJUDI.ESTADO e on e.ID_ESTADO = ser.ID_ESTADO_REPRESENTACAO ";
		Sql += " 	LEFT JOIN PROJUDI.USU_SERV_GRUPO ug1 on ug1.ID_USU_SERV=us.ID_USU_SERV_CHEFE and ug1.Ativo= ?";			ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
		Sql += "	LEFT JOIN PROJUDI.SERV_CARGO sc1 on ug1.ID_USU_SERV_GRUPO=sc1.ID_USU_SERV_GRUPO";
		Sql += "	LEFT JOIN PROJUDI.CARGO_TIPO ct1 on sc1.ID_CARGO_TIPO=ct1.ID_CARGO_TIPO";
		Sql += "    LEFT JOIN PROJUDI.GRUPO g1 on ug1.ID_GRUPO= g1.ID_GRUPO";
		Sql += " WHERE u.ID_USU= ?";																						ps.adicionarLong(id_usuario); 
		Sql += "    AND ug.ATIVO= ?";																						ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
		Sql += " 	AND us.ATIVO= ?";																						ps.adicionarLong(UsuarioServentiaDt.ATIVO);		
		Sql += "    ORDER BY ser.SERV,ser.ID_SERV, g.GRUPO";

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setSenha(rs1.getString("SENHA"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setMatriculaTjGo(rs1.getString("MATRICULA_TJGO"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setRgOrgaoExpedidor(rs1.getString("SIGLA_ORGAO_EXPEDIDOR"));
				obTemp.setRgOrgaoExpedidorUf(rs1.getString("ORGAO_EXPEDIDOR_SIGLA_ESTADO"));
				obTemp.setDataNascimento(rs1.getString("DATA_NASCIMENTO"));
				obTemp.setEMail(rs1.getString("EMAIL"));
				obTemp.setUsuarioCidade(rs1.getString("USUARIO_CIDADE"));
				obTemp.setUsuarioEstado(rs1.getString("USU_ESTADO"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setServentiaCodigo(rs1.getString("SERV_CODIGO"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setId_Comarca(rs1.getString("ID_COMARCA"));
				obTemp.setComarcaCodigo(rs1.getString("COMARCA_CODIGO"));
				obTemp.setId_Cidade(rs1.getString("ID_CIDADE_SERV"));
				obTemp.setId_AudienciaTipo("ID_AUDI_TIPO");
				obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				obTemp.setId_GrupoTipo(rs1.getString("ID_GRUPO_TIPO"));
				obTemp.setGrupoTipo(rs1.getString("GRUPO_TIPO"));
				obTemp.setGrupoTipoCodigo(rs1.getString("GRUPO_TIPO_CODIGO"));
				obTemp.setId_ServentiaTipo(rs1.getString("ID_SERV_TIPO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				obTemp.setServentiaIdArea(rs1.getString("ID_AREA"));
				obTemp.setServentiaTipoExterna(Funcoes.FormatarLogico(rs1.getString("EXTERNA")));
				obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				obTemp.setServentiaSubtipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				obTemp.setEstadoRepresentacao(rs1.getString("ESTADO_REPRESENTACAO"));
				obTemp.setOabNumero(rs1.getString("OAB_NUMERO"));
				obTemp.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				obTemp.setOabEstado(rs1.getString("ESTADO_REPRESENTACAO"));
				obTemp.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
				obTemp.setServentiaCargo(rs1.getString("SERV_CARGO"));
				obTemp.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				obTemp.setCargoTipo(rs1.getString("CARGO_TIPO"));
				
				obTemp.setGrupoUsuarioChefe(rs1.getString("GRUPO_USU_CHEFE"));
				obTemp.setId_UsuarioServentiaChefe(rs1.getString("ID_USU_SERV_CHEFE"));
				obTemp.setUsuarioServentiaChefe(rs1.getString("USU_SERV_CHEFE"));
				obTemp.setId_ServentiaCargoUsuarioChefe(rs1.getString("ID_SERV_CARGO_USU_CHEFE"));
				obTemp.setCargoTipoCodigoUsuarioChefe(rs1.getString("CARGO_TIPO_CODIGO_USU_CHEFE"));
				obTemp.setCargoTipoUsuarioChefe(rs1.getString("CARGO_TIPO_USU_CHEFE"));
				obTemp.setServentiaCargoUsuarioChefe(rs1.getString("SERV_CARGO_USU_CHEFE"));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}
	
	public List consultarServentiasGrupos(String id_usuario,
										  String id_usuarioServentia, 
										  String grupoCodigo,
										  String id_serventiaCargo,
									      String id_serventiaCargoUsuarioChefe,
									      String id_usuarioServentiaChefe) throws Exception {
		String Sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = " SELECT u.ID_USU, u.USU, u.NOME, u.CPF, u.RG, u.SIGLA_ORGAO_EXPEDIDOR, u.ORGAO_EXPEDIDOR_SIGLA_ESTADO, ";
		Sql += " u.DATA_NASCIMENTO, u.EMAIL, u.CIDADE as USUARIO_CIDADE, u.UF as USU_ESTADO, u.SENHA, u.MATRICULA_TJGO, ";
		Sql += " us.ID_SERV, us.SERV, us.SERV_CODIGO, us.ID_USU_SERV, us.ID_COMARCA, us.COMARCA_CODIGO, ";
		Sql += " us.ID_CIDADE_SERV,g.ID_GRUPO, g.GRUPO, ser.ID_AUDI_TIPO,";
		Sql += " st.ID_SERV_TIPO, st.SERV_TIPO, st.SERV_TIPO_CODIGO, st.EXTERNA, sc.ID_SERV_CARGO, ct.CARGO_TIPO_CODIGO, ";
		Sql += " ct.CARGO_TIPO, g.GRUPO_CODIGO, gt.ID_GRUPO_TIPO, gt.GRUPO_TIPO, gt.GRUPO_TIPO_CODIGO, ss.ID_SERV_SUBTIPO, ss.SERV_SUBTIPO_CODIGO, e.UF as ESTADO_REPRESENTACAO, uo.OAB_NUMERO, uo.OAB_COMPLEMENTO, ";
		Sql += " us.ID_USU_SERV_CHEFE, us.USU_SERV_CHEFE, sc1.ID_SERV_CARGO as ID_SERV_CARGO_USU_CHEFE, sc1.SERV_CARGO as SERV_CARGO_USU_CHEFE,";
		Sql += " ct1.CARGO_TIPO_CODIGO as CARGO_TIPO_CODIGO_USU_CHEFE, ct1.CARGO_TIPO as CARGO_TIPO_USU_CHEFE, g1.GRUPO_CODIGO as GRUPO_USU_CHEFE, ser.ID_AREA, sc.SERV_CARGO ";
		Sql += " FROM PROJUDI.VIEW_USU_COMPLETO u ";
		Sql += "	INNER JOIN PROJUDI.VIEW_USU_SERV us on u.ID_USU=us.ID_USU";
		Sql += "    INNER JOIN PROJUDI.SERV ser on ser.ID_SERV= us.ID_SERV";
		Sql += "    INNER JOIN PROJUDI.SERV_TIPO st on ser.ID_SERV_TIPO=st.ID_SERV_TIPO";
		Sql += "    INNER JOIN PROJUDI.USU_SERV_GRUPO ug on us.ID_USU_SERV=ug.ID_USU_SERV";
		Sql += "	LEFT JOIN PROJUDI.SERV_CARGO sc on (ug.ID_USU_SERV_GRUPO=sc.ID_USU_SERV_GRUPO AND ser.ID_SERV=sc.ID_SERV)";
		Sql += "	LEFT JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		Sql += "    INNER JOIN PROJUDI.GRUPO g on ug.ID_GRUPO= g.ID_GRUPO";
		Sql += "	LEFT JOIN PROJUDI.GRUPO_TIPO gt on g.ID_GRUPO_TIPO = gt.ID_GRUPO_TIPO";
		Sql += "    LEFT JOIN PROJUDI.SERV_SUBTIPO ss on ss.ID_SERV_SUBTIPO= ser.ID_SERV_SUBTIPO";
		Sql += "    LEFT JOIN PROJUDI.USU_SERV_OAB uo on uo.ID_USU_SERV= us.ID_USU_SERV";
		Sql += "    LEFT JOIN PROJUDI.ESTADO e on e.ID_ESTADO = ser.ID_ESTADO_REPRESENTACAO ";
		Sql += " 	LEFT JOIN PROJUDI.USU_SERV_GRUPO ug1 on ug1.ID_USU_SERV=us.ID_USU_SERV_CHEFE and ug1.Ativo= ?";			ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
		Sql += "	LEFT JOIN PROJUDI.SERV_CARGO sc1 on ug1.ID_USU_SERV_GRUPO=sc1.ID_USU_SERV_GRUPO";
		Sql += "	LEFT JOIN PROJUDI.CARGO_TIPO ct1 on sc1.ID_CARGO_TIPO=ct1.ID_CARGO_TIPO";
		Sql += "    LEFT JOIN PROJUDI.GRUPO g1 on ug1.ID_GRUPO= g1.ID_GRUPO";
		Sql += " WHERE u.ID_USU= ?";																						ps.adicionarLong(id_usuario); 
		Sql += "    AND ug.ATIVO= ?";																						ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
		Sql += " 	AND us.ATIVO= ?";																						ps.adicionarLong(UsuarioServentiaDt.ATIVO);		
		if (id_usuarioServentia != null && id_usuarioServentia.trim().length() > 0) {
			//ID_USU_SERV
			Sql += " 	AND us.ID_USU_SERV= ?";	ps.adicionarLong(id_usuarioServentia);	
		}
		if (grupoCodigo != null && grupoCodigo.trim().length() > 0) {
			//GRUPO_CODIGO	
			Sql += " 	AND g.GRUPO_CODIGO= ?";	ps.adicionarLong(grupoCodigo);	
		}
		if (id_serventiaCargo != null && id_serventiaCargo.trim().length() > 0) {
			//ID_SERV_CARGO
			Sql += " 	AND sc.ID_SERV_CARGO= ?";	ps.adicionarLong(id_serventiaCargo);	
		}
		if (id_serventiaCargoUsuarioChefe != null && id_serventiaCargoUsuarioChefe.trim().length() > 0) {
			//ID_SERV_CARGO_USU_CHEFE
			Sql += " 	AND sc1.ID_SERV_CARGO= ?";	ps.adicionarLong(id_serventiaCargoUsuarioChefe);	
		}
		if (id_usuarioServentiaChefe != null && id_usuarioServentiaChefe.trim().length() > 0) {
			//ID_USU_SERV_CHEFE
			Sql += " 	us.ID_USU_SERV_CHEFE= ?";	ps.adicionarLong(id_usuarioServentiaChefe);	
		}
		
		Sql += "    ORDER BY ser.SERV,ser.ID_SERV, g.GRUPO";

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				UsuarioDt obTemp = new UsuarioDt();
				obTemp.setId(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setSenha(rs1.getString("SENHA"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setCpf(rs1.getString("CPF"));
				obTemp.setMatriculaTjGo(rs1.getString("MATRICULA_TJGO"));
				obTemp.setRg(rs1.getString("RG"));
				obTemp.setRgOrgaoExpedidor(rs1.getString("SIGLA_ORGAO_EXPEDIDOR"));
				obTemp.setRgOrgaoExpedidorUf(rs1.getString("ORGAO_EXPEDIDOR_SIGLA_ESTADO"));
				obTemp.setDataNascimento(rs1.getString("DATA_NASCIMENTO"));
				obTemp.setEMail(rs1.getString("EMAIL"));
				obTemp.setUsuarioCidade(rs1.getString("USUARIO_CIDADE"));
				obTemp.setUsuarioEstado(rs1.getString("USU_ESTADO"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setServentiaCodigo(rs1.getString("SERV_CODIGO"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setId_Comarca(rs1.getString("ID_COMARCA"));
				obTemp.setComarcaCodigo(rs1.getString("COMARCA_CODIGO"));
				obTemp.setId_Cidade(rs1.getString("ID_CIDADE_SERV"));
				obTemp.setId_AudienciaTipo("ID_AUDI_TIPO");
				obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				obTemp.setId_GrupoTipo(rs1.getString("ID_GRUPO_TIPO"));
				obTemp.setGrupoTipo(rs1.getString("GRUPO_TIPO"));
				obTemp.setGrupoTipoCodigo(rs1.getString("GRUPO_TIPO_CODIGO"));
				obTemp.setId_ServentiaTipo(rs1.getString("ID_SERV_TIPO"));
				obTemp.setServentiaTipo(rs1.getString("SERV_TIPO"));
				obTemp.setServentiaIdArea(rs1.getString("ID_AREA"));
				obTemp.setServentiaTipoExterna(Funcoes.FormatarLogico(rs1.getString("EXTERNA")));
				obTemp.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				obTemp.setServentiaSubtipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
				obTemp.setId_ServentiaSubtipo(rs1.getString("ID_SERV_SUBTIPO"));
				obTemp.setEstadoRepresentacao(rs1.getString("ESTADO_REPRESENTACAO"));
				obTemp.setOabNumero(rs1.getString("OAB_NUMERO"));
				obTemp.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				obTemp.setOabEstado(rs1.getString("ESTADO_REPRESENTACAO"));
				obTemp.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
				obTemp.setServentiaCargo(rs1.getString("SERV_CARGO"));
				obTemp.setCargoTipoCodigo(rs1.getString("CARGO_TIPO_CODIGO"));
				obTemp.setCargoTipo(rs1.getString("CARGO_TIPO"));
				
				obTemp.setGrupoUsuarioChefe(rs1.getString("GRUPO_USU_CHEFE"));
				obTemp.setId_UsuarioServentiaChefe(rs1.getString("ID_USU_SERV_CHEFE"));
				obTemp.setUsuarioServentiaChefe(rs1.getString("USU_SERV_CHEFE"));
				obTemp.setId_ServentiaCargoUsuarioChefe(rs1.getString("ID_SERV_CARGO_USU_CHEFE"));
				obTemp.setCargoTipoCodigoUsuarioChefe(rs1.getString("CARGO_TIPO_CODIGO_USU_CHEFE"));
				obTemp.setCargoTipoUsuarioChefe(rs1.getString("CARGO_TIPO_USU_CHEFE"));
				obTemp.setServentiaCargoUsuarioChefe(rs1.getString("SERV_CARGO_USU_CHEFE"));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}

	/**
	 * Consulta que retorna os usuários de um determinado tipo de cargo em uma serventia
	 * 
	 * @param id_CargoTipo, identificação do tipo de cargo para filtrar os usuários
	 * @param id_Serventia, serventia para filtrar os usuários
	 * @descricao, filtro para usuário
	 * @posicao, página a ser exibida
	 * 
	 * @author msapaula
	 * @since 09/02/2009 08:53
	 */
	public List consultarUsuarioServentiaCargo(String id_CargoTipo, String id_Serventia, String descricao, String posicao) throws Exception {
		String sql;
		String sqlComum;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sqlComum = " FROM PROJUDI.VIEW_USU_SERV_GRUPO us JOIN PROJUDI.CARGO_TIPO ct on us.ID_GRUPO=ct.ID_GRUPO ";
		sqlComum += " JOIN PROJUDI.USU_SERV u on us.ID_USU_SERV = u.ID_USU_SERV";
		sqlComum += " WHERE NOME LIKE ?";									ps.adicionarString( descricao +"%");
		sqlComum += " AND u.ATIVO = ? AND us.ATIVO = ? ";
		ps.adicionarLong(1);
		ps.adicionarLong(1);
		sqlComum += " AND ct.ID_CARGO_TIPO = ?";
		ps.adicionarLong(id_CargoTipo);
		sqlComum += " AND us.ID_SERV = ?";
		ps.adicionarLong(id_Serventia);

		sql = "SELECT us.ID_USU_SERV_GRUPO,us.USU_SERV_GRUPO, us.GRUPO, us.SERV, us.NOME " + sqlComum;
		sql += " ORDER BY USU_SERV_GRUPO";
		
		
		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
			while (rs1.next()) {
				UsuarioServentiaGrupoDt obTemp = new UsuarioServentiaGrupoDt();
				obTemp.setId(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setUsuario(rs1.getString("USU_SERV_GRUPO"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setGrupo(rs1.getString("GRUPO"));
				obTemp.setServentia(rs1.getString("SERV"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) AS QUANTIDADE " + sqlComum;
			rs2 = consultar(sql,ps);
			if (rs2.next())
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return liTemp;
	}

	/**
	 * Método responsável em verificar se um determinado advogado já está habilitado em uma Serventia
	 * 
	 * @param usuarioServentiaGrupoDt, objeto com dados do usuário, serventia 
	 * 
	 * @author Jrcorrea
	 * @throws Exception
	 * 25/08/2016
	 */
	public boolean verificarHabilitacaoAdvogado(UsuarioServentiaGrupoDt usuarioServentiaGrupoDt) throws Exception {
		boolean boRetorno = false;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = " SELECT 1 FROM PROJUDI.USU_SERV_GRUPO ug";
		sql += " INNER JOIN PROJUDI.USU_SERV us on us.ID_USU_SERV = ug.ID_USU_SERV";
		sql += " INNER JOIN PROJUDI.Grupo g on ug.ID_GRUPO = g.ID_GRUPO";
		sql += " WHERE us.ID_SERV = ?";													ps.adicionarLong(usuarioServentiaGrupoDt.getId_Serventia());
		sql += " AND us.ID_USU = ?";													ps.adicionarLong(usuarioServentiaGrupoDt.getId_Usuario());
		sql += " AND g.GRUPO_CODIGO NOT IN (?,?,?,?,?,? ) "; 
		ps.adicionarLong(GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA); ps.adicionarLong(GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA); ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL); ps.adicionarLong(GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL);
		
		//Ocorrencia 2020/10686 - Não estava verificando se estava ativo
		sql += " AND UG.ATIVO = ? ";
		ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);
		
		try{
			rs1 = consultar(sql,ps);
			if (rs1.next())
				boRetorno = true;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return boRetorno;
	}
	
	/**
	 * Método responsável em verificar se um determinado usuário já está habilitado em uma Serventia
	 * e Grupo passados
	 * 
	 * @param usuarioServentiaGrupoDt, objeto com dados do usuário, serventia e grupo a serem
	 *        verificados
	 * 
	 * @author msapaula
	 * @throws Exception
	 */
	public boolean verificarHabilitacaoUsuario(UsuarioServentiaGrupoDt usuarioServentiaGrupoDt) throws Exception {
		boolean boRetorno = false;
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = " SELECT 1 FROM PROJUDI.USU_SERV_GRUPO ug";
		sql += " INNER JOIN PROJUDI.USU_SERV us on us.ID_USU_SERV = ug.ID_USU_SERV";
		sql += " WHERE ug.ID_GRUPO = ?";												ps.adicionarLong(usuarioServentiaGrupoDt.getId_Grupo());
		sql += " AND us.ID_SERV = ?";													ps.adicionarLong(usuarioServentiaGrupoDt.getId_Serventia());
		sql += " AND us.ID_USU = ?";													ps.adicionarLong(usuarioServentiaGrupoDt.getId_Usuario());

		try{
			rs1 = consultar(sql,ps);
			if (rs1.next())
				boRetorno = true;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return boRetorno;
	}
	
	/**
	 * Método responsável em verificar se um determinado usuário já está habilitado em uma Serventia
	 * e Grupo passados
	 * 
	 * @param usuarioServentiaGrupoDt, objeto com dados do usuário, serventia e grupo a serem
	 *        verificados
	 * 
	 * @author asrocha
	 * @throws Exception
	 */
	public String  consultarIdUsuarioServentiaGrupo(String idGrupo, String idServentia, String idUsuario) throws Exception {
		String stRetorno = "";
		String sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = " SELECT * FROM PROJUDI.USU_SERV_GRUPO ug";
		sql += " INNER JOIN PROJUDI.USU_SERV us on us.ID_USU_SERV = ug.ID_USU_SERV";
		sql += " WHERE ug.Id_Grupo = ?";
		ps.adicionarLong(idGrupo);
		sql += " AND us.ID_SERV = ?";
		ps.adicionarLong(idServentia);
		sql += " AND us.ID_USU = ?";
		ps.adicionarLong(idUsuario);

		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				stRetorno = rs1.getString("ID_USU_SERV_GRUPO");
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return stRetorno;
	}

	/**
	 * Altera o status de um UsuarioServentiaGrupo passado, desativando ou ativando.
	 * 
	 * @param id_UsuarioServentiaGrupo, identificação do UsuarioServentiaGrupo
	 * @param novoStatus, novo status
	 * 
	 * @author msapaula
	 */
	public void alterarStatusUsuarioServentiaGrupo(String id_UsuarioServentiaGrupo, int novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Sql = "UPDATE PROJUDI.USU_SERV_GRUPO SET ATIVO =?";
		ps.adicionarBoolean(novoStatus == 0 ? false : true);
		Sql += " WHERE ID_USU_SERV_GRUPO =?";;
		ps.adicionarLong(id_UsuarioServentiaGrupo);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Método responsável em alterar status de todos registros em UsuarioServentiaGrupo de um usuário
	 * 
	 * @param id_Usuario, identificação do usuário
	 * @param novoStatus, novo status
	 * @author msapaula
	 */
	public void alterarStatusTodosUsuarioServentiaGrupo(String id_Usuario, int novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = " UPDATE (SELECT usg.ATIVO, us.ID_USU FROM PROJUDI.USU_SERV_GRUPO usg";
		Sql += " INNER JOIN PROJUDI.USU_SERV us on usg.ID_USU_SERV = us.ID_USU_SERV) TAB";
		Sql += " set TAB.ATIVO = ?";
		ps.adicionarBoolean(novoStatus == 0 ? false: true);
		Sql += " WHERE TAB.ID_USU = ?";
		ps.adicionarLong(id_Usuario);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Consulta os grupos e serventias onde um Servidor Judiciário está habilitado. Retorna ATIVOS ou INATIVOS
	 * Retorna somente os grupos e serventias que não sejam de Advogado
	 * 
	 * @param id_Usuario, identificação do usuário para o qual serão consultados serventias e grupos
	 * 
	 * @author msapaula
	 * @return lista de UsuarioServentiaGrupos
	 */
	public List consultarServentiasGruposServidorJudiciario(String id_Usuario) throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String servTipo;

		sql = " SELECT usg.ID_USU_SERV_GRUPO, usg.ID_USU_SERV, u.ID_USU, u.USU,";
		sql += " usg.ID_GRUPO, g.GRUPO, g.GRUPO_CODIGO, g.SERV_TIPO, u.NOME, us.ID_SERV, s.SERV,  usg.ATIVO, uc.nome AS NOME_USU_CHEFE";
		sql += " FROM PROJUDI.USU_SERV_GRUPO usg";
		sql += " INNER JOIN PROJUDI.USU_SERV us on usg.ID_USU_SERV = us.ID_USU_SERV";
		sql += " INNER JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV";
		sql += " INNER JOIN PROJUDI.USU u on us.ID_USU = u.ID_USU";
		sql += " INNER JOIN PROJUDI.VIEW_GRUPO g on usg.ID_GRUPO = g.ID_GRUPO";
		sql += " LEFT JOIN PROJUDI.USU_SERV usc ON usc.id_usu_serv = us.id_usu_serv_chefe ";
		sql += " LEFT JOIN PROJUDI.USU uc ON uc.id_usu = usc.id_usu";
		sql += " WHERE g.GRUPO_CODIGO NOT IN (?, ?, ?, ?, ? ,?, ?) ";
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_ESTADUAL);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PUBLICO_UNIAO);
		ps.adicionarLong(GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO);
		ps.adicionarLong(GrupoDt.ADVOGADO_DEFENSOR_PUBLICO);
		sql += " AND u.ID_USU = ?"; 
		ps.adicionarLong(id_Usuario);
		sql += " ORDER BY u.USU, s.SERV, g.GRUPO";

		try{
			rs1 = consultar(sql,ps);

			while (rs1.next()) {
				UsuarioServentiaGrupoDt obTemp = new UsuarioServentiaGrupoDt();
				obTemp.setId(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
				servTipo = rs1.getString("SERV_TIPO");
				
				if (servTipo == null)
				  obTemp.setGrupo(rs1.getString("GRUPO"));
				else if (rs1.getString("NOME_USU_CHEFE") != null && !rs1.getString("NOME_USU_CHEFE").isEmpty()){
					//Se houver um chefe, apresentar para facilitar os cadastradores identificarem os usuários assessores de juiz
					obTemp.setGrupo(rs1.getString("GRUPO") + "-" + servTipo + "- Chefe: " + rs1.getString("NOME_USU_CHEFE"));
				} else {
					obTemp.setGrupo(rs1.getString("GRUPO") + "-" + servTipo);
				}

				obTemp.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				obTemp.setId_Usuario(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}
	
	/**
	 * Consulta os grupos e serventias onde um  usuário está habilitado como promotor. Retorna ATIVOS ou INATIVOS
	 * 
	 * @param id_Usuario, identificação do usuário para o qual serão consultados serventias e grupos
	 * 
	 * @author lsbernardes
	 * @return lista de UsuarioServentiaGrupos
	 */
	public List consultarServentiasGruposPromotores(String id_Usuario) throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = " SELECT usg.ID_USU_SERV_GRUPO, usg.ID_USU_SERV, u.ID_USU, u.USU,";
		sql += " usg.ID_GRUPO, g.GRUPO, g.GRUPO_CODIGO, g.SERV_TIPO, u.NOME, us.ID_SERV, s.SERV,  usg.ATIVO";
		sql += " FROM PROJUDI.USU_SERV_GRUPO usg";
		sql += " INNER JOIN PROJUDI.USU_SERV us on usg.ID_USU_SERV = us.ID_USU_SERV";
		sql += " INNER JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV";
		sql += " INNER JOIN PROJUDI.USU u on us.ID_USU = u.ID_USU";
		sql += " INNER JOIN PROJUDI.VIEW_GRUPO g on usg.ID_GRUPO = g.ID_GRUPO";
		sql += " WHERE g.GRUPO_CODIGO  in ( ?,?,? )";
		ps.adicionarLong(GrupoDt.MINISTERIO_PUBLICO);
		ps.adicionarLong(GrupoDt.MP_TCE);
		ps.adicionarLong(GrupoDt.COORDENADOR_PROMOTORIA);
		sql += " AND u.ID_USU = ?"; 
		ps.adicionarLong(id_Usuario);
		sql += " ORDER BY u.USU, s.SERV, g.GRUPO";

		try{
			rs1 = consultar(sql,ps);

			while (rs1.next()) {
				UsuarioServentiaGrupoDt obTemp = new UsuarioServentiaGrupoDt();
				obTemp.setId(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO") + "-" + rs1.getString("SERV_TIPO"));
				obTemp.setId_Usuario(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}
	
	/**
	 * Retorna usuarios da serventia
	 * @since 25/11/2009
	 * 
	 * @param usuarioNe, usuário logado para permitir consulta e geração de código Hash 
	 * @return List
	 * @throws Exception
	 */
	public List consultarTodosUsuarioServentiaGrupo(UsuarioNe usuarioNe) throws Exception {
		List lista = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT DISTINCT u.ID_USU, us.ID_USU_SERV, sg.ID_USU_SERV_GRUPO, u.NOME, u.USU, g.GRUPO";
		Sql += " FROM  PROJUDI.USU u  ";
		Sql += "  JOIN PROJUDI.USU_SERV us  on u.ID_USU = us.ID_USU  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.GRUPO g on sg.ID_GRUPO = g.ID_GRUPO  ";
		Sql += " WHERE ID_SERV = ? AND u.ATIVO = ? AND us.ATIVO = ? AND sg.ATIVO = ?";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(1);
		ps.adicionarLong(1);
		ps.adicionarLong(1);

		try{
			rs1 = consultar(Sql,ps);

			while (rs1.next()) {
				if (lista == null) lista = new ArrayList();
				UsuarioServentiaGrupoDt usuario = new UsuarioServentiaGrupoDt();
				usuario.setId_Usuario(rs1.getString("ID_USU"));
				usuario.setId(rs1.getString("ID_USU_SERV_GRUPO"));
				usuario.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				usuario.setNome(rs1.getString("NOME"));
				usuario.setUsuario(rs1.getString("USU"));
				usuario.setGrupo(rs1.getString("GRUPO"));
				usuario.setHash(usuarioNe.getCodigoHash(usuario.getId()));
				lista.add(usuario);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	public String consultarUsuarioServentiaCargoJSON(String id_CargoTipo, String id_Serventia, String descricao, String posicao) throws Exception {
		String sql;
		String sqlComum;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sqlComum = " FROM PROJUDI.VIEW_USU_SERV_GRUPO us ";
		sqlComum += " JOIN PROJUDI.USU_SERV u on us.ID_USU_SERV = u.ID_USU_SERV";
		
		if (Funcoes.StringToInt(id_CargoTipo) == CargoTipoDt.JUIZ_UPJ){
			sqlComum += " JOIN PROJUDI.GRUPO g ON us.ID_GRUPO = g.ID_GRUPO  AND g.GRUPO_CODIGO in (?,?) ";
			ps.adicionarLong(GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU); ps.adicionarLong(GrupoDt.MAGISTRADO_UPJ_SEGUNDO_GRAU);
		} else {
			sqlComum += " JOIN PROJUDI.CARGO_TIPO ct on us.ID_GRUPO=ct.ID_GRUPO AND ct.ID_CARGO_TIPO = ? ";
			ps.adicionarLong(id_CargoTipo);
		}
		
		sqlComum += " WHERE us.USU_SERV_GRUPO LIKE ?";			ps.adicionarString( descricao +"%");
		sqlComum += " AND u.ATIVO = ? AND us.ATIVO = ? ";		ps.adicionarLong(1); ps.adicionarLong(1);
		sqlComum += " AND us.ID_SERV = ?";						ps.adicionarLong(id_Serventia);
		int qtdeColunas = 4;

		sql = "SELECT us.ID_USU_SERV_GRUPO AS ID, us.USU_SERV_GRUPO AS DESCRICAO1 , us.NOME AS DESCRICAO2, us.GRUPO AS DESCRICAO3, us.SERV AS DESCRICAO4 " + sqlComum;
		sql += " ORDER BY USU_SERV_GRUPO";
		
		try{
			rs1 = consultarPaginacao(sql, ps, posicao);
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

	public void AtivarAdvogado(String id_usuarioServentia) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Sql = "UPDATE PROJUDI.USU_SERV_GRUPO SET ATIVO =?";		ps.adicionarBoolean(true);
		Sql += " WHERE ID_USU_SERV =?";	ps.adicionarLong(id_usuarioServentia);

			executarUpdateDelete(Sql, ps);
		
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		
		String Sql;
		String SqlFrom;
		String SqlOrder;
		String stTemp="";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		int qtdeColunas = 3;
		
		Sql = "SELECT ID_USU_SERV_GRUPO as ID ,USU_SERV_GRUPO  AS DESCRICAO1  , GRUPO  AS DESCRICAO2 , SERV  AS DESCRICAO3";
		SqlFrom = " FROM PROJUDI.VIEW_USU_SERV_GRUPO";
		SqlFrom += " WHERE USU_SERV_GRUPO LIKE ?";
		ps.adicionarString( descricao +"%");
		SqlOrder = " ORDER BY USU_SERV_GRUPO";
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
	
	public void alteraGrupoAdvogados(String idServentia, String id_Grupo) throws Exception {
		String Sql1;
		String Sql2;
		String Sql3;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs1;
		ResultSetTJGO rs2;
		String id_usuarioServentia;
		String id_usuarioServentiaGrupo;
		
		Sql1 = "SELECT ID_USU_SERV FROM PROJUDI.VIEW_USU_SERV WHERE ID_SERV =?";
		ps.adicionarString(idServentia);
		rs1 = consultar(Sql1, ps);
		
		while (rs1.next()) {
			id_usuarioServentia = rs1.getString("ID_USU_SERV");
			Sql2 = "SELECT ID_USU_SERV_GRUPO FROM PROJUDI.VIEW_USU_SERV_GRUPO WHERE ID_USU_SERV =?";
			ps.limpar();
		    ps.adicionarString(id_usuarioServentia);
		    rs2 = consultar(Sql2, ps);
			
		    while (rs2.next()) {
     		  id_usuarioServentiaGrupo = rs2.getString("ID_USU_SERV_GRUPO");
			  Sql3 = "UPDATE PROJUDI.USU_SERV_GRUPO SET ID_GRUPO =?";
			  ps.limpar();
			  ps.adicionarString(id_Grupo);
			  Sql3 += " WHERE ID_USU_SERV_GRUPO =?";
			  ps.adicionarString(id_usuarioServentiaGrupo);
	          executarUpdateDelete(Sql3, ps);
		    }
		}
	}


	public List consultarServentiasGruposPoliciais(String id_usuario) throws Exception {
		String sql;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = " SELECT usg.ID_USU_SERV_GRUPO, usg.ID_USU_SERV, u.ID_USU, u.USU,";
		sql += " usg.ID_GRUPO, g.GRUPO, g.GRUPO_CODIGO, g.SERV_TIPO, u.NOME, us.ID_SERV, s.SERV,  usg.ATIVO";
		sql += " FROM PROJUDI.USU_SERV_GRUPO usg";
		sql += " INNER JOIN PROJUDI.USU_SERV us on usg.ID_USU_SERV = us.ID_USU_SERV";
		sql += " INNER JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV";
		sql += " INNER JOIN PROJUDI.USU u on us.ID_USU = u.ID_USU";
		sql += " INNER JOIN PROJUDI.VIEW_GRUPO g on usg.ID_GRUPO = g.ID_GRUPO";
		sql += " WHERE g.GRUPO_CODIGO  in ( ?,? )";										ps.adicionarLong(GrupoDt.AUTORIDADES_POLICIAIS);		ps.adicionarLong(GrupoDt.COORDENADOR_HABILITACAO_SSP);
		sql += " AND u.ID_USU = ?";														ps.adicionarLong(id_usuario);
		sql += " ORDER BY u.USU, s.SERV, g.GRUPO";

		try{
			rs1 = consultar(sql,ps);

			while (rs1.next()) {
				UsuarioServentiaGrupoDt obTemp = new UsuarioServentiaGrupoDt();
				obTemp.setId(rs1.getString("ID_USU_SERV_GRUPO"));
				obTemp.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				obTemp.setId_Grupo(rs1.getString("ID_GRUPO"));
				obTemp.setGrupo(rs1.getString("GRUPO") + "-" + rs1.getString("SERV_TIPO"));
				obTemp.setId_Usuario(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setNome(rs1.getString("NOME"));
				obTemp.setId_Serventia(rs1.getString("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setAtivo(Funcoes.FormatarLogico(rs1.getString("ATIVO")));
				liTemp.add(obTemp);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}

}
