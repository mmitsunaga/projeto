package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioCandidatoComarcaDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class UsuarioCandidatoComarcaPs extends Persistencia {
	
	public UsuarioCandidatoComarcaPs(Connection conexao){
		Conexao = conexao;
	}
	
	/**
	 * Método para inserir UsuarioCandidatoComarcaDt
	 * @param UsuarioCandidatoComarcaDt usuarioCandidatoComarcaDt
	 * @throws Exception
	 */
	public void inserir(UsuarioCandidatoComarcaDt usuarioCandidatoComarcaDt) throws Exception {
		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO PROJUDI.USU_CAND_COMARCA (";
		SqlValores = " Values (";
		if (!(usuarioCandidatoComarcaDt.getComarcaDt().getId().length() == 0)){
			SqlCampos += ",ID_COMARCA";
			SqlValores+= ",?";
			ps.adicionarLong(usuarioCandidatoComarcaDt.getComarcaDt().getId());
		}
		if (!(usuarioCandidatoComarcaDt.getUsuarioCandidatoDt().getId().length() == 0)){
			SqlCampos += ",ID_USU_CAND";
			SqlValores+= ",?";
			ps.adicionarLong(usuarioCandidatoComarcaDt.getUsuarioCandidatoDt().getId());
		}
		if (!(usuarioCandidatoComarcaDt.getCodigoEscolha().length() == 0)){
			SqlCampos += ",CODIGO_ESCOLHA";
			SqlValores+= ",?";
			ps.adicionarLong(usuarioCandidatoComarcaDt.getCodigoEscolha());
		}
		if (!(usuarioCandidatoComarcaDt.getDataInscricao().length() == 0)){
			SqlCampos += ",DATA_INSCRICAO";
			SqlValores+= ",?";
			ps.adicionarDateTime(usuarioCandidatoComarcaDt.getDataInscricao());
		}
		if (!(usuarioCandidatoComarcaDt.getServentiaDt().getId().length() == 0)){
			SqlCampos += ",ID_SERVENTIA";
			SqlValores+= ",?";
			ps.adicionarLong(usuarioCandidatoComarcaDt.getServentiaDt().getId());
		}
		
		SqlCampos+=")";
 		SqlValores+=")";

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");

 		usuarioCandidatoComarcaDt.setId(executarInsert(Sql, "ID_USU_CAND_COMARCA", ps));
	}
	
	/**
	 * Método para alterar UsuarioCandidatoComarcaDt
	 * @param UsuarioCandidatoComarcaDt dados
	 * @throws Exception
	 */
	public void alterar(UsuarioCandidatoComarcaDt dados) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_CAND_COMARCA SET  ";
		
		Sql += ",ID_COMARCA = ? ";
		ps.adicionarLong(dados.getComarcaDt().getId());
		Sql += ",ID_USU_CAND = ? ";
		ps.adicionarLong(dados.getUsuarioCandidatoDt().getId());
		Sql += ",CODIGO_ESCOLHA = ? ";
		ps.adicionarDate(dados.getCodigoEscolha());
		Sql += ",DATA_INSCRICAO = ? ";
		ps.adicionarLong(dados.getDataInscricao());
		Sql += ",ID_SERVENTIA = ? ";
		ps.adicionarLong(dados.getServentiaDt().getId());
		
		Sql = Sql + " WHERE ID_USU_CAND_COMARCA = ? ";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método para consultar UsuarioCandidatoComarcaDt por ID
	 * @param String idUsuarioCandidatoComarca
	 * @return UsuarioCandidatoComarcaDt
	 * @throws Exception
	 */
	public UsuarioCandidatoComarcaDt consultarId(String idUsuarioCandidatoComarca) throws Exception {
		UsuarioCandidatoComarcaDt usuarioCandidatoComarcaDt = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.USU_CAND_COMARCA WHERE ID_USU_CAND_COMARCA = ?";
		
		ps.adicionarLong(idUsuarioCandidatoComarca);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				
				while(rs1.next()) {
					
					if( usuarioCandidatoComarcaDt == null ) {
						usuarioCandidatoComarcaDt = new UsuarioCandidatoComarcaDt();
					}
					
					usuarioCandidatoComarcaDt.setId(rs1.getString("ID_USU_CAND_COMARCA"));
					usuarioCandidatoComarcaDt.setDataInscricao(rs1.getString("DATA_INSCRICAO"));
					usuarioCandidatoComarcaDt.setCodigoEscolha(rs1.getString("CODIGO_ESCOLHA"));
					
					UsuarioCejuscDt usuarioCejuscDt = new UsuarioCejuscDt();
					usuarioCejuscDt.setId(rs1.getString("ID_USU_CAND"));
					usuarioCandidatoComarcaDt.setUsuarioCandidatoDt(usuarioCejuscDt);
					
					ComarcaDt comarcaDt = new ComarcaDt();
					comarcaDt.setId(rs1.getString("ID_COMARCA"));
					usuarioCandidatoComarcaDt.setComarcaDt(comarcaDt);
					
					ServentiaDt serventiaDt = new ServentiaDt();
					serventiaDt.setId(rs1.getString("ID_SERVENTIA"));
					usuarioCandidatoComarcaDt.setServentiaDt(serventiaDt);
				}
			}
		}
		finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return usuarioCandidatoComarcaDt;
	}
	
	public List<UsuarioCandidatoComarcaDt> consultarPorId_UsuarioCandidato(String idUsuarioCandidato) throws Exception {
		List<UsuarioCandidatoComarcaDt> listaUsuarioCandidatoComarcaDt = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.USU_CAND_COMARCA WHERE ID_USU_CAND = ?";
		
		ps.adicionarLong(idUsuarioCandidato);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				
				while(rs1.next()) {
					
					if( listaUsuarioCandidatoComarcaDt == null ) {
						listaUsuarioCandidatoComarcaDt = new ArrayList<UsuarioCandidatoComarcaDt>();
					}
					
					UsuarioCandidatoComarcaDt usuarioCandidatoComarcaDt = new UsuarioCandidatoComarcaDt();
					
					usuarioCandidatoComarcaDt.setId(rs1.getString("ID_USU_CAND_COMARCA"));
					usuarioCandidatoComarcaDt.setDataInscricao(rs1.getString("DATA_INSCRICAO"));
					usuarioCandidatoComarcaDt.setCodigoEscolha(rs1.getString("CODIGO_ESCOLHA"));
					
					UsuarioCejuscDt usuarioCejuscDt = new UsuarioCejuscDt();
					usuarioCejuscDt.setId(rs1.getString("ID_USU_CAND"));
					usuarioCandidatoComarcaDt.setUsuarioCandidatoDt(usuarioCejuscDt);
					
					ComarcaDt comarcaDt = new ComarcaDt();
					comarcaDt.setId(rs1.getString("ID_COMARCA"));
					usuarioCandidatoComarcaDt.setComarcaDt(comarcaDt);
					
					ServentiaDt serventiaDt = new ServentiaDt();
					serventiaDt.setId(rs1.getString("ID_SERVENTIA"));
					usuarioCandidatoComarcaDt.setServentiaDt(serventiaDt);
					
					listaUsuarioCandidatoComarcaDt.add(usuarioCandidatoComarcaDt);
				}
			}
		}
		finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return listaUsuarioCandidatoComarcaDt;
	}
}
