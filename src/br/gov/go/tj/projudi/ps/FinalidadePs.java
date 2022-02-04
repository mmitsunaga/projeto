package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.FinalidadeDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class FinalidadePs extends Persistencia {

	private static final long serialVersionUID = -2871005874624034803L;
	
	public FinalidadePs(Connection conexao){
		Conexao = conexao;
	}

	protected void associarDt( FinalidadeDt Dados, ResultSetTJGO rs )  throws Exception {
		 
		Dados.setId(rs.getString("ID_FINALIDADE"));
		Dados.setFinalidade(rs.getString("FINALIDADE"));
		Dados.setFinalidadeCodigo( rs.getString("FINALIDADE_CODIGO"));
		Dados.setAtivo(rs.getString("ATIVO"));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}

	/**
	 * Método para consultar lista de finalidades pela descrição.
	 * @param String descricao
	 * @return List
	 * @throws Exception
	 */
	public List consultarDescricao(String descricao) throws Exception {
		List listaFinalidadeDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			String sql = "SELECT * FROM projudi.VIEW_FINALIDADE ";
			if( descricao != null && descricao.length() > 0 ) {
				sql = sql + "WHERE FINALIDADE LIKE ?";
				ps.adicionarString("%"+ descricao +"%");
			}

			rs1 = consultar(sql,ps);
			if (rs1 != null) {
				while(rs1.next()) {
					if( listaFinalidadeDt == null ) {
						listaFinalidadeDt = new ArrayList();
					}
					FinalidadeDt finalidadeDt = new FinalidadeDt();  
					finalidadeDt.setId(rs1.getString("ID_FINALIDADE"));
					finalidadeDt.setFinalidade(rs1.getString("FINALIDADE"));
					finalidadeDt.setAtivo(rs1.getString("ATIVO"));
					finalidadeDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
					finalidadeDt.setFinalidadeCodigo(rs1.getString("FINALIDADE_CODIGO"));
					
					listaFinalidadeDt.add(finalidadeDt);
				}
			}
		
		}finally{
			 rs1.close();
		}
		
		return listaFinalidadeDt;
	}
	
	/**
	 * Método para consultar toda a lista de finalidades.
	 * @return List
	 * @throws Exception
	 */
	public List consultarFinalidades() throws Exception {
		List listaFinalidadeDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try{
			String sql = "SELECT * FROM projudi.VIEW_FINALIDADE";

			rs1 = consultar(sql,ps);
			if (rs1 != null) {
				while(rs1.next()) {
					if( listaFinalidadeDt == null ) {
						listaFinalidadeDt = new ArrayList();
					}
					FinalidadeDt finalidadeDt = new FinalidadeDt();  
					finalidadeDt.setId(rs1.getString("ID_FINALIDADE"));
					finalidadeDt.setFinalidade(rs1.getString("FINALIDADE"));
					finalidadeDt.setAtivo(rs1.getString("ATIVO"));
					finalidadeDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
					finalidadeDt.setFinalidadeCodigo(rs1.getString("FINALIDADE_CODIGO"));
					
					listaFinalidadeDt.add(finalidadeDt);
				}
			}
		
		}
		finally{
			 rs1.close();
		}
		
		return listaFinalidadeDt;
	}
	
	/**
	 * Método para consultar FinalidadeDt pelo Id.
	 * @param String descricao
	 * @return List
	 * @throws Exception
	 */
	public FinalidadeDt consultarId(String id_finalidade )  throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		FinalidadeDt Dados=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql= "SELECT * FROM projudi.VIEW_FINALIDADE WHERE ";
		Sql+="ID_FINALIDADE = ?";
		ps.adicionarLong(id_finalidade);

		try{
			rs1 = consultar(Sql,ps);
			if (rs1.next()) {
				Dados= new FinalidadeDt();
				this.associarDt(Dados, rs1);
			}
		} finally {
			if (rs1 != null) rs1.close();
		}
		return Dados; 
	}
	
	/**
	 * Método para consultar toda a lista de finalidades.
	 * 
	 * @return List
	 * @throws Exception
	 */
	public String consultarDescricaoFinalidadesJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		try {
			String sql = "SELECT ID_FINALIDADE as id, FINALIDADE as descricao1, FINALIDADE_CODIGO as descricao2, ATIVO as descricao3 FROM projudi.VIEW_FINALIDADE ";
			if (descricao != null && descricao.length() > 0) {
				sql = sql + "WHERE FINALIDADE LIKE ?";
				ps.adicionarString("%" + descricao + "%");
			}
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			sql = "SELECT Count(*) as Quantidade FROM projudi.VIEW_FINALIDADE ";
			if (descricao != null && descricao.length() > 0) {
				sql = sql + "WHERE FINALIDADE LIKE ?";
			}
			
			rs2 = consultar(sql, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, 2);
			
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
			try {
				if (rs2 != null)
					rs2.close();
			} catch (Exception e) {
			}
		}
		
		return stTemp;
	}
}
