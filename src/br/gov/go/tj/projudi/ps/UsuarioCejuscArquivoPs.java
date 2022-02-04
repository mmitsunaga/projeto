package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.UsuarioCejuscArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class UsuarioCejuscArquivoPs extends Persistencia {

	public UsuarioCejuscArquivoPs(Connection conexao){
		Conexao = conexao;
	}
	
	/**
	 * Método para inserir UsuarioCandidatoArquivoDt
	 * @param UsuarioCejuscArquivoDt usuarioCandidatoArquivoDt
	 * @throws Exception
	 */
	public void inserir(UsuarioCejuscArquivoDt usuarioCejuscArquivoDt) throws Exception {
		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO PROJUDI.USU_CEJUSC_ARQ (";
		SqlValores = " Values (";
		
		if (!(usuarioCejuscArquivoDt.getNomeArquivo() != null && usuarioCejuscArquivoDt.getNomeArquivo().length() == 0)){
			SqlCampos += ",NOME_ARQ";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscArquivoDt.getNomeArquivo());
		}
		
//		if (!(usuarioCandidatoArquivoDt.getBytes() != null )){
			SqlCampos += ",CONTEUDO_ARQ";
			SqlValores+= ",?";
			ps.adicionarByte(usuarioCejuscArquivoDt.getBytes());
//		}
			
		if (!(usuarioCejuscArquivoDt.getContentType() != null && usuarioCejuscArquivoDt.getContentType().length() == 0)){
			SqlCampos += ",CONTENT_TYPE";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscArquivoDt.getContentType());
		}
		if (!(usuarioCejuscArquivoDt.getDataInsercao() != null && usuarioCejuscArquivoDt.getDataInsercao().length() == 0)){
			SqlCampos += ",DATA_INSERCAO";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscArquivoDt.getDataInsercao());
		}
		if (!(usuarioCejuscArquivoDt.getUsuarioCejuscDt() != null && usuarioCejuscArquivoDt.getUsuarioCejuscDt().getId() != null && usuarioCejuscArquivoDt.getUsuarioCejuscDt().getId().length() == 0)){
			SqlCampos += ",ID_USU_CEJUSC";
			SqlValores+= ",?";
			ps.adicionarString(usuarioCejuscArquivoDt.getUsuarioCejuscDt().getId());
		}
		
		SqlCampos+=")";
 		SqlValores+=")";

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");

 		usuarioCejuscArquivoDt.setId(executarInsert(Sql, "ID_USU_CEJUSC_ARQ", ps));
	}
	
	/**
	 * Método para alterar UsuarioCandidatoArquivoDt
	 * @param UsuarioCejuscArquivoDt dados
	 * @throws Exception
	 */
	public void alterar(UsuarioCejuscArquivoDt dados) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.USU_CEJUSC_ARQ SET ";
		
		Sql += ",NOME_ARQ = ? ";
		ps.adicionarString(dados.getNomeArquivo());
		
		Sql += ",CONTEUDO_ARQ = ? ";
		ps.adicionarByte(dados.getBytes());
		
		Sql += ",CONTENT_TYPE = ? ";
		ps.adicionarString(dados.getContentType());
		
		Sql += ",DATA_INSERCAO = ? ";
		ps.adicionarString(dados.getDataInsercao());
		
		Sql += ",ID_USU_CAND = ? ";
		ps.adicionarString(dados.getUsuarioCejuscDt().getId());
		
		Sql = Sql + " WHERE ID_USU_CEJUSC_ARQ = ? ";
		ps.adicionarLong(dados.getId());

		Sql = Sql.replace("SET ,", "SET ");
		
		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método para consultar UsuarioCandidatoArquivoDt por ID
	 * @param String id
	 * @return UsuarioCandidatoArquivoDt
	 * @throws Exception
	 */
	public UsuarioCejuscArquivoDt consultarId(String id) throws Exception {
		UsuarioCejuscArquivoDt usuarioCejuscArquivoDt = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.USU_CEJUSC_ARQ WHERE ID_USU_CEJUSC_ARQ = ?";
		
		ps.adicionarLong(id);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				
				while(rs1.next()) {
					
					if( usuarioCejuscArquivoDt == null ) {
						usuarioCejuscArquivoDt = new UsuarioCejuscArquivoDt();
					}
					
					this.associarDt(usuarioCejuscArquivoDt, rs1);
				}
			}
		}
		finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return usuarioCejuscArquivoDt;
	}
	
	/**
	 * Método para consultar lista de arquivos.
	 * @param String idUsuarioCandidato
	 * @return List<UsuarioCandidatoArquivoDt>
	 * @throws Exception
	 */
	public List<UsuarioCejuscArquivoDt> consultaListaUsuarioCejuscArquivoDt(String id) throws Exception {
		List<UsuarioCejuscArquivoDt> listaUsuarioCejuscArquivoDt = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT ID_USU_CEJUSC_ARQ, NOME_ARQ, ID_USU_CEJUSC FROM PROJUDI.USU_CEJUSC_ARQ WHERE ID_USU_CEJUSC = ?";
		
		ps.adicionarLong(id);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			if( rs1 != null ) {
				
				while(rs1.next()) {
					
					if( listaUsuarioCejuscArquivoDt == null ) {
						listaUsuarioCejuscArquivoDt = new ArrayList<UsuarioCejuscArquivoDt>();
					}
					
					UsuarioCejuscArquivoDt usuarioCejuscArquivoDt = new UsuarioCejuscArquivoDt();
					
					usuarioCejuscArquivoDt.setId(rs1.getString("ID_USU_CEJUSC_ARQ"));
					usuarioCejuscArquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
					
					UsuarioCejuscDt usuarioCejuscDt = new UsuarioCejuscDt();
					usuarioCejuscDt.setId(rs1.getString("ID_USU_CEJUSC"));
					usuarioCejuscArquivoDt.setUsuarioCejuscDt(usuarioCejuscDt);
					
					listaUsuarioCejuscArquivoDt.add(usuarioCejuscArquivoDt);
				}
			}
		}
		finally {
			 try {if (rs1 != null) rs1.close();} catch (Exception e1) {}
		}
		
		return listaUsuarioCejuscArquivoDt;
	}
	
	/**
	 * Método para associarDt da consulta do ResultSet.
	 * @param UsuarioCejuscArquivoDt usuarioCejuscArquivoDt
	 * @param ResultSetTJGO rs
	 * @throws Exception
	 */
	protected void associarDt(UsuarioCejuscArquivoDt usuarioCejuscArquivoDt, ResultSetTJGO rs) throws Exception {
		
		usuarioCejuscArquivoDt.setId(rs.getString("ID_USU_CEJUSC_ARQ"));
		usuarioCejuscArquivoDt.setNomeArquivo(rs.getString("NOME_ARQ"));
		usuarioCejuscArquivoDt.setBytes(rs.getBytes("CONTEUDO_ARQ"));
		usuarioCejuscArquivoDt.setContentType(rs.getString("CONTENT_TYPE"));
		usuarioCejuscArquivoDt.setDataInsercao(rs.getString("DATA_INSERCAO"));
		
		UsuarioCejuscDt usuarioCejuscDt = new UsuarioCejuscDt();
		usuarioCejuscDt.setId(rs.getString("ID_USU_CEJUSC"));
		usuarioCejuscArquivoDt.setUsuarioCejuscDt(usuarioCejuscDt);
		
	}
}
