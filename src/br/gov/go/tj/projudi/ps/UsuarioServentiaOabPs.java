package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class UsuarioServentiaOabPs extends UsuarioServentiaOabPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -949120109651546850L;

    public UsuarioServentiaOabPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Consultar um advogado de acordo com OAB, complemento e estado passados
	 * 
	 * @param oabNumero
	 *            número da oab
	 * @param oabComplemento
	 *            complemento da oab
	 * @param oabUf
	 *            estado de representação da serventia OAB
	 * 
	 * @author msapaula
	 */
	public String consultarAdvogado(String oabNumero, String oabComplemento, String oabUf, int codigoInstituicao) throws Exception {
		String id_UsuarioServentia = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT us.ID_USU_SERV FROM PROJUDI.USU_SERV_OAB o";
		Sql += " INNER JOIN PROJUDI.USU_SERV us on o.ID_USU_SERV = us.ID_USU_SERV";
		Sql += " INNER JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV";
		Sql += " INNER JOIN PROJUDI.ESTADO e on e.ID_ESTADO = s.ID_ESTADO_REPRESENTACAO";
		Sql += " INNER JOIN PROJUDI.SERV_TIPO st on st.ID_SERV_TIPO = s.ID_SERV_TIPO";
		Sql += " WHERE o.OAB_NUMERO = ?"; 				ps.adicionarLong(oabNumero);
		Sql += " AND o.OAB_COMPLEMENTO = ?";			ps.adicionarString(oabComplemento);
		Sql += " AND e.UF = ?"; 						ps.adicionarString(oabUf);
		Sql += " AND us.ATIVO = ?"; 					ps.adicionarLong(UsuarioDt.ATIVO);
		if(codigoInstituicao != 0) {
			Sql += " AND st.SERV_TIPO_CODIGO = ?";
			ps.adicionarLong(codigoInstituicao);
		}
		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) id_UsuarioServentia = rs1.getString("ID_USU_SERV");

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return id_UsuarioServentia;
	}

	/**
	 * Consultar um advogado de acordo com OAB, complemento e estado passados
	 * 
	 * @param oabNumero
	 *            número da oab
	 * @param oabComplemento
	 *            complemento da oab
	 * @param oabUf
	 *            estado de representação da serventia OAB
	 * 
	 * @author msapaula
	 */
	public UsuarioServentiaOabDt consultarUsuarioServentiaOab(String oabNumero, String oabComplemento, String oabUf) throws Exception {
		UsuarioServentiaOabDt usuarioServentiaOabDt = null;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT us.ID_USU_SERV, u.NOME FROM PROJUDI.USU_SERV_OAB o";
		Sql += " INNER JOIN PROJUDI.USU_SERV us on o.ID_USU_SERV = us.ID_USU_SERV";
		Sql += " INNER JOIN PROJUDI.USU u on us.ID_USU = u.ID_USU";
		Sql += " INNER JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV";
		Sql += " INNER JOIN PROJUDI.ESTADO e on e.ID_ESTADO = s.ID_ESTADO_REPRESENTACAO";
		Sql += " WHERE o.OAB_NUMERO = ?";			ps.adicionarLong(oabNumero);
		Sql += " AND o.OAB_COMPLEMENTO = ?";		ps.adicionarString(oabComplemento);
		Sql += " AND e.UF = ?";						ps.adicionarString(oabUf);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				usuarioServentiaOabDt = new UsuarioServentiaOabDt();
				usuarioServentiaOabDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				usuarioServentiaOabDt.setNomeUsuario(rs1.getString("NOME"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return usuarioServentiaOabDt;
	}
	
	/**
	 * Consultar todos os advogados de um serventia
	 * 
	 * @param id_serventia
	 *           
	 * 
	 * @author jesus, hmgodinho
	 * @18/11/2014
	 */
	
	public List consultarUsuariosServentiaOab(String id_serventia) throws Exception {
		String Sql;
		String Sql2;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
		List liTemp = new ArrayList();
		
		//Primeiro tentar buscar o Master
		Sql = "SELECT us.ID_USU_SERV, u.NOME, o.OAB_COMPLEMENTO, o.OAB_NUMERO, s.SERV  FROM PROJUDI.USU_SERV_OAB o";
		Sql += " INNER JOIN PROJUDI.USU_SERV us on o.ID_USU_SERV = us.ID_USU_SERV";
		Sql += " INNER JOIN PROJUDI.USU u on us.ID_USU = u.ID_USU";
		Sql += " INNER JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV";
		Sql += " INNER JOIN PROJUDI.ESTADO e on e.ID_ESTADO = s.ID_ESTADO_REPRESENTACAO";
		Sql += " WHERE s.id_serv  = ?";			ps.adicionarLong(id_serventia);		
		//localizar somente usuários que estejam ativos na serventia
		Sql += " 	AND us.ativo  = ?";			ps.adicionarLong(UsuarioServentiaDt.ATIVO);
		Sql += " 	AND o.USU_MASTER  = ?";			ps.adicionarLong(UsuarioServentiaOabDt.MASTER);
		
		//Se não tiver um master definido, poderá habilitar qualquer advogado/procurador
		Sql2 = "SELECT us.ID_USU_SERV, u.NOME, o.OAB_COMPLEMENTO, o.OAB_NUMERO, s.SERV  FROM PROJUDI.USU_SERV_OAB o";
		Sql2 += " INNER JOIN PROJUDI.USU_SERV us on o.ID_USU_SERV = us.ID_USU_SERV";
		Sql2 += " INNER JOIN PROJUDI.USU u on us.ID_USU = u.ID_USU";
		Sql2 += " INNER JOIN PROJUDI.SERV s on s.ID_SERV = us.ID_SERV";
		Sql2 += " INNER JOIN PROJUDI.ESTADO e on e.ID_ESTADO = s.ID_ESTADO_REPRESENTACAO";
		Sql2 += " WHERE s.id_serv  = ?";			ps2.adicionarLong(id_serventia);		
		//localizar somente usuários que estejam ativos na serventia
		Sql2 += " 	AND us.ativo  = ?";			ps2.adicionarLong(UsuarioServentiaDt.ATIVO);
		

		try{
			rs1 = consultar(Sql,ps);
			while (rs1.next()) {
				UsuarioDt usuarioDt = new UsuarioDt();
				usuarioDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				usuarioDt.setNome(rs1.getString("NOME"));
				usuarioDt.setOabNumero(rs1.getString("OAB_NUMERO"));
				usuarioDt.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				usuarioDt.setServentia(rs1.getString("SERV"));
				liTemp.add(usuarioDt);
			}
			
			if (liTemp == null || liTemp.size()==0){
				rs2 = consultar(Sql2,ps2);
				while (rs2.next()) {
					UsuarioDt usuarioDt = new UsuarioDt();
					usuarioDt.setId_UsuarioServentia(rs2.getString("ID_USU_SERV"));
					usuarioDt.setNome(rs2.getString("NOME"));
					usuarioDt.setOabNumero(rs2.getString("OAB_NUMERO"));
					usuarioDt.setOabComplemento(rs2.getString("OAB_COMPLEMENTO"));
					usuarioDt.setServentia(rs2.getString("SERV"));
					liTemp.add(usuarioDt);
				}
			}
			
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {	}
			try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
		}

		return liTemp;
	}

	public List<UsuarioServentiaOabDt> consultarUsuarioServentiaOab(String id_Usuario) throws Exception {
		List<UsuarioServentiaOabDt> lista = new ArrayList<UsuarioServentiaOabDt>();
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT uso.ID_USU_SERV_OAB,us.ID_USU_SERV, us.ID_SERV, us.ATIVO, uso.OAB_COMPLEMENTO, uso.OAB_NUMERO, us.SERV, g.grupo_codigo FROM PROJUDI.VIEW_USU_SERV us";
		Sql += " INNER JOIN PROJUDI.USU_SERV_OAB uso on us.ID_USU_SERV = uso.ID_USU_SERV";
		Sql += " INNER JOIN PROJUDI.USU_SERV_GRUPO usg on usg.ID_USU_SERV = us.ID_USU_SERV";
		Sql += " INNER JOIN PROJUDI.GRUPO g on usg.ID_GRUPO = g.ID_GRUPO";
		Sql += " WHERE us.ID_USU = ?";
		ps.adicionarLong(id_Usuario);

		try{
			rs1 = consultar(Sql,ps);
			while (rs1.next()) {
				UsuarioServentiaOabDt usuarioServentiaOabDt = new UsuarioServentiaOabDt();
				usuarioServentiaOabDt.setId(rs1.getString("ID_USU_SERV_OAB"));
				usuarioServentiaOabDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				usuarioServentiaOabDt.setIdServentia(rs1.getString("ID_SERV"));
				usuarioServentiaOabDt.setInativo(rs1.getInt("ATIVO") == UsuarioServentiaDt.INATIVO);
				usuarioServentiaOabDt.setOabComplemento(rs1.getString("OAB_COMPLEMENTO"));
				usuarioServentiaOabDt.setOabNumero(rs1.getString("OAB_NUMERO"));
				usuarioServentiaOabDt.setServentia(rs1.getString("SERV"));
				usuarioServentiaOabDt.setGrupoCodigo(rs1.getString("GRUPO_CODIGO"));
				lista.add(usuarioServentiaOabDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	/**
     * Verifica se o usuário advogado/procurador/defensor que será intimado ou citado é master ou não.
     * 
     * @author lsbernardes
     
     * @param id_Usu_Serv,
     *            id do usuário serventia
     *           
     * @return boolean,
     * 			se o usuário é master ou não 
     *             
     * @throws Exception
     */
	public boolean isUsuarioServentiaOabMaster(String id_Usu_Serv) throws Exception {
		boolean retorno = false;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = "SELECT us.ID_USU_SERV  FROM PROJUDI.USU_SERV_OAB us";
		Sql += " WHERE us.ID_USU_SERV = ? AND us.USU_MASTER = ?";			
		ps.adicionarLong(id_Usu_Serv);
		ps.adicionarLong(UsuarioServentiaOabDt.MASTER);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				retorno = true;			
			}
		
		} finally{
			try{
				if (rs1 != null) 
					rs1.close();
				
			} catch(Exception e) {
			}
		}
		return retorno;
	}
	
	/**
     * Consulta possíveis usuários (advogado/procurador/defensor) master na serventia do usuário (advogado/procurador/defensor) que será intimado ou citado.
     * 
     * @author lsbernardes
     
     * @param id_Serv,
     *            id da serventia
     
     * @return List,
     *           lista contendo objetos do tipo PendenciaResponsavelDt para compor a lista de responsáveis pela intimação ou citação
    
     * @throws Exception
     */
	public List<PendenciaResponsavelDt> consultarUsuarioServentiaOabMaster(String id_Serv) throws Exception { 
		List<PendenciaResponsavelDt> lista = new ArrayList<PendenciaResponsavelDt>();
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		Sql = " SELECT ";
		Sql += " us.ID_USU_SERV ";
		Sql += " FROM  ";
		Sql += "  PROJUDI.USU_SERV us";
		Sql += "  JOIN PROJUDI.USU u on us.ID_USU = u.ID_USU ";
		Sql += "  JOIN PROJUDI.USU_SERV_OAB oab  on us.ID_USU_SERV = oab.ID_USU_SERV  ";
		Sql += "  JOIN PROJUDI.SERV s on us.ID_SERV = s.ID_SERV  ";
		Sql += "  JOIN PROJUDI.USU_SERV_GRUPO sg on us.ID_USU_SERV = sg.ID_USU_SERV  ";
		Sql += " WHERE  ";
		Sql += "  us.ID_SERV = ? AND oab.USU_MASTER = ? and u.ATIVO = ? and us.ATIVO = ? and sg.ATIVO = ? ";
		ps.adicionarLong(id_Serv);
		ps.adicionarLong(UsuarioServentiaOabDt.MASTER);
		ps.adicionarLong(UsuarioDt.ATIVO);
		ps.adicionarLong(UsuarioServentiaDt.ATIVO);
		ps.adicionarLong(UsuarioServentiaGrupoDt.ATIVO);

		try{
			rs1 = consultar(Sql,ps);
			while (rs1.next()) {
				PendenciaResponsavelDt pendenciaResponsavelDt = new PendenciaResponsavelDt();
				pendenciaResponsavelDt.setId_UsuarioResponsavel(rs1.getString("ID_USU_SERV"));
				lista.add(pendenciaResponsavelDt);
			}
		
		} finally{
			try{
				if (rs1 != null) 
					rs1.close();
				
			} catch(Exception e) {
			}
		}
		return lista;
	}

}
