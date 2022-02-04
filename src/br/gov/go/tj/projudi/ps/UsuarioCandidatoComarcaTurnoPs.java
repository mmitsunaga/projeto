package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioCandidatoComarcaDt;
import br.gov.go.tj.projudi.dt.UsuarioCandidatoComarcaTurnoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class UsuarioCandidatoComarcaTurnoPs extends Persistencia {
	
	public UsuarioCandidatoComarcaTurnoPs(Connection conexao){
		Conexao = conexao;
	}

	/**
	 * Método para inserir UsuarioCandidatoComarcaTurnoDt
	 * @param UsuarioCandidatoComarcaTurnoDt usuarioCandidatoComarcaTurnoDt
	 * @throws Exception
	 */
	public void inserir(UsuarioCandidatoComarcaTurnoDt usuarioCandidatoComarcaTurnoDt) throws Exception {
		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO PROJUDI.USU_CAND_COMARCA_TURNO (";
		SqlValores = " Values (";
		if (!(usuarioCandidatoComarcaTurnoDt.getUsuarioCandidatoComarcaDt().getId().length() == 0)){
			SqlCampos += ",ID_USU_CAND_COMARCA";
			SqlValores+= ",?";
			ps.adicionarLong(usuarioCandidatoComarcaTurnoDt.getUsuarioCandidatoComarcaDt().getId());
		}
		if (!(usuarioCandidatoComarcaTurnoDt.getCodigoDia().length() == 0)){
			SqlCampos += ",CODIGO_DIA";
			SqlValores+= ",?";
			ps.adicionarLong(usuarioCandidatoComarcaTurnoDt.getCodigoDia());
		}
		if (!(usuarioCandidatoComarcaTurnoDt.getCodigoTurnoMatutino().length() == 0)){
			SqlCampos += ",CODIGO_TURNO_MATUTINO";
			SqlValores+= ",?";
			ps.adicionarLong(usuarioCandidatoComarcaTurnoDt.getCodigoTurnoMatutino());
		}
		if (!(usuarioCandidatoComarcaTurnoDt.getCodigoTurnoVespertino().length() == 0)){
			SqlCampos += ",CODIGO_TURNO_VESPERTINO";
			SqlValores+= ",?";
			ps.adicionarLong(usuarioCandidatoComarcaTurnoDt.getCodigoTurnoVespertino());
		}
		
		SqlCampos+=")";
 		SqlValores+=")";

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");

 		usuarioCandidatoComarcaTurnoDt.setId(executarInsert(Sql, "ID_USU_CAND_COMARCA_TURNO", ps));
	}
	
	/**
	 * Método para alterar UsuarioCandidatoComarcaTurnoDt
	 * @param UsuarioCandidatoComarcaTurnoDt dados
	 * @throws Exception
	 */
	public void alterar(UsuarioCandidatoComarcaTurnoDt dados) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_CAND_COMARCA_TURNO SET  ";
		
		Sql += ",ID_USU_CAND_COMARCA = ? ";
		ps.adicionarLong(dados.getUsuarioCandidatoComarcaDt().getId());
		Sql += ",CODIGO_DIA = ? ";
		ps.adicionarLong(dados.getCodigoDia());
		Sql += ",CODIGO_TURNO_MATUTINO = ? ";
		ps.adicionarDate(dados.getCodigoTurnoMatutino());
		Sql += ",CODIGO_TURNO_VESPERTINO = ? ";
		ps.adicionarLong(dados.getCodigoTurnoVespertino());
		
		Sql = Sql + " WHERE ID_USU_CAND_COMARCA_TURNO = ? ";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método para consultar UsuarioCandidatoComarcaTurnoDt por ID
	 * @param String idUsuarioCandidatoComarcaTurno
	 * @return UsuarioCandidatoComarcaTurnoDt
	 * @throws Exception
	 */
	public UsuarioCandidatoComarcaTurnoDt consultarId(String idUsuarioCandidatoComarcaTurno) throws Exception {
		UsuarioCandidatoComarcaTurnoDt usuarioCandidatoComarcaTurnoDt = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.USU_CAND_COMARCA_TURNO WHERE ID_USU_CAND_COMARCA_TURNO = ?";
		
		ps.adicionarLong(idUsuarioCandidatoComarcaTurno);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				
				while(rs1.next()) {
					
					if( usuarioCandidatoComarcaTurnoDt == null ) {
						usuarioCandidatoComarcaTurnoDt = new UsuarioCandidatoComarcaTurnoDt();
					}
					
					usuarioCandidatoComarcaTurnoDt.setId(rs1.getString("ID_USU_CAND_COMARCA_TURNO"));
					usuarioCandidatoComarcaTurnoDt.setCodigoDia(rs1.getString("CODIGO_DIA"));
					usuarioCandidatoComarcaTurnoDt.setCodigoTurnoMatutino(rs1.getString("CODIGO_TURNO_MATUTINO"));
					usuarioCandidatoComarcaTurnoDt.setCodigoTurnoVespertino(rs1.getString("CODIGO_TURNO_VESPERTINO"));
					
					UsuarioCandidatoComarcaDt usuarioCandidatoComarcaDt = new UsuarioCandidatoComarcaDt();
					usuarioCandidatoComarcaDt.setId(rs1.getString("ID_USU_CAND_COMARCA"));
					usuarioCandidatoComarcaTurnoDt.setUsuarioCandidatoComarcaDt(usuarioCandidatoComarcaDt);
				}
			}
		}
		finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return usuarioCandidatoComarcaTurnoDt;
	}
	
	public List<UsuarioCandidatoComarcaTurnoDt> consultarPorId_UsuarioCandidatoComarca(String idUsuarioCandidatoComarca) throws Exception {
		List<UsuarioCandidatoComarcaTurnoDt> listaUsuarioCandidatoComarcaTurnoDt = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.USU_CAND_COMARCA_TURNO WHERE ID_USU_CAND_COMARCA = ?";
		
		ps.adicionarLong(idUsuarioCandidatoComarca);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				
				while(rs1.next()) {
					
					if( listaUsuarioCandidatoComarcaTurnoDt == null ) {
						listaUsuarioCandidatoComarcaTurnoDt = new ArrayList<UsuarioCandidatoComarcaTurnoDt>();
					}
					
					UsuarioCandidatoComarcaTurnoDt usuarioCandidatoComarcaTurnoDt = new UsuarioCandidatoComarcaTurnoDt();
					
					usuarioCandidatoComarcaTurnoDt.setId(rs1.getString("ID_USU_CAND_COMARCA_TURNO"));
					usuarioCandidatoComarcaTurnoDt.setCodigoDia(rs1.getString("CODIGO_DIA"));
					usuarioCandidatoComarcaTurnoDt.setCodigoTurnoMatutino(rs1.getString("CODIGO_TURNO_MATUTINO"));
					usuarioCandidatoComarcaTurnoDt.setCodigoTurnoVespertino(rs1.getString("CODIGO_TURNO_VESPERTINO"));
					
					UsuarioCandidatoComarcaDt usuarioCandidatoComarcaDt = new UsuarioCandidatoComarcaDt();
					usuarioCandidatoComarcaDt.setId(rs1.getString("ID_USU_CAND_COMARCA"));
					usuarioCandidatoComarcaTurnoDt.setUsuarioCandidatoComarcaDt(usuarioCandidatoComarcaDt);
					
					listaUsuarioCandidatoComarcaTurnoDt.add(usuarioCandidatoComarcaTurnoDt);
				}
			}
		}
		finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return listaUsuarioCandidatoComarcaTurnoDt;
	}
}
