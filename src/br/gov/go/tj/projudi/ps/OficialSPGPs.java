package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.OficialSPGDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class OficialSPGPs extends Persistencia {
	
	private static final long serialVersionUID = -2430701725417986125L;
	
	public OficialSPGPs(Connection conexao){
		Conexao = conexao;
	}
	
	/**
	 * Método para extrair o código da comarca no código do oficial.
	 * @param String codigoOficial
	 * @return String
	 * @throws Exception
	 */
	private String getCodigoComarca(String codigoOficial){
		String retorno = "";
				
		if( codigoOficial != null ) {
			int indice = 6 - codigoOficial.length();
			retorno = codigoOficial.substring(0, 3 - indice);
		}		
		
		return retorno;
	}
	
	/**
	 * Método para consultar os oficiais da comarca.
	 * @param String comarcaCodigo
	 * @return List<OficialSPGDt>
	 * @throws Exception
	 */
	public List consultarOficiaisComarca(String comarcaCodigo) throws Exception {
		List listaOficiaisSPGDt = new ArrayList();
		
		//Consulta códigos oficiais que não estejam como afastados(código 4)
		String sql = "SELECT NOME_OFICIAL, CODG_OFICIAL, STAT_AFASTAMENTO FROM V_SPGUOFICIAIS " +
				" WHERE STAT_AFASTAMENTO <> 4 AND (CODG_OFICIAL >= "+comarcaCodigo+"000 AND CODG_OFICIAL <= "+comarcaCodigo+"999) ORDER BY NOME_OFICIAL";
		
		ResultSetTJGO rs1 = null;
		
		try{
			PreparedStatementTJGO ps = new PreparedStatementTJGO();

			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					
					if( rs1.getString("NOME_OFICIAL") != null 
						&& rs1.getString("NOME_OFICIAL").length() > 1 //Maior que um pq tem nomes cadastrados no SPG que ï¿½ sï¿½ um "ponto"
						&& rs1.getString("CODG_OFICIAL") != null
						&& rs1.getString("CODG_OFICIAL").length() > 0 ) {
						
						OficialSPGDt oficialSPGDt = new OficialSPGDt();
						
						oficialSPGDt.setNomeOficial(	rs1.getString("NOME_OFICIAL"));
						oficialSPGDt.setCodigoOficial(	rs1.getString("CODG_OFICIAL"));
						oficialSPGDt.setAtivo(			rs1.getString("STAT_AFASTAMENTO"));
						oficialSPGDt.setCodigoComarca(	this.getCodigoComarca(rs1.getString("CODG_OFICIAL")));
						
						listaOficiaisSPGDt.add(oficialSPGDt);
					}
				}
			}
			
		
		}
		finally{
			//Fecha conexão e resultset
			if( rs1 != null ) {
				rs1.close();
			}
		}
		
		return listaOficiaisSPGDt;
	}
	
	/**
	 * Mï¿½todo para consultar o oficial através do codigo.
	 * @param String codigoOficial
	 * @return OficialSPGDt
	 * @throws Exception
	 */
	public OficialSPGDt consultaOficial(String codigoOficial) throws Exception {
		OficialSPGDt oficialSPGDt = new OficialSPGDt();
				
		String sql = "SELECT NOME_OFICIAL, CODG_OFICIAL, STAT_AFASTAMENTO FROM V_SPGUOFICIAIS WHERE CODG_OFICIAL = ?";
				
		ResultSetTJGO rs1 = null;
		
		try{
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			ps.adicionarLong(Funcoes.StringToLong(codigoOficial));
			
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					
						oficialSPGDt.setNomeOficial(	rs1.getString("NOME_OFICIAL"));
						oficialSPGDt.setCodigoOficial(	rs1.getString("CODG_OFICIAL"));
						oficialSPGDt.setAtivo(			rs1.getString("STAT_AFASTAMENTO"));
						oficialSPGDt.setCodigoComarca(	this.getCodigoComarca(rs1.getString("CODG_OFICIAL")));
						
					}
				}
			
		
		}
		finally{
			//Fecha conexão e resultset
			if( rs1 != null ) {
				rs1.close();
			}
		}
		
		return oficialSPGDt;
	}
	
	public String consultarOficiaisComarcaJSON(String descricao, String comarcaCodigo, String posicao) throws Exception {
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		// Consulta códigos oficiais que não estejam como afastados(código 4)
		String sql = "SELECT NOME_OFICIAL as descricao1, CODG_OFICIAL as id, STAT_AFASTAMENTO as descricao2 FROM V_SPGUOFICIAIS " + " WHERE STAT_AFASTAMENTO <> 4 AND (CODG_OFICIAL >= " + comarcaCodigo + "000 AND CODG_OFICIAL <= " + comarcaCodigo + "999)";
		if (descricao != null && descricao.length() > 0) {
			sql = sql + " AND NOME_OFICIAL LIKE ?";
			ps.adicionarString("%" + descricao + "%");
		}
		sql += " ORDER BY NOME_OFICIAL";
		
		rs1 = consultar(sql, ps);
		
		sql = "SELECT Count(*) as QUANTIDADE FROM V_SPGUOFICIAIS " + " WHERE STAT_AFASTAMENTO <> 4 AND (CODG_OFICIAL >= " + comarcaCodigo + "000 AND CODG_OFICIAL <= " + comarcaCodigo + "999)";
		if (descricao != null && descricao.length() > 0) {
			sql = sql + " AND NOME_OFICIAL LIKE ?";
		}
		sql += " ORDER BY NOME_OFICIAL";
		
		rs2 = consultar(sql, ps);
		rs2.next();
		stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, 2);
		try {
			
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
	
	/**
	 * Método para consultar o oficial através pelo cpf.
	 * @param String cpf
	 * @return OficialSPGDt
	 * @throws Exception
	 * @author fasoares
	 */
	public OficialSPGDt consultaOficialCpf(String cpf) throws Exception {
		OficialSPGDt oficialSPGDt = null;
				
		String sql = "SELECT NOME_OFICIAL, CODG_OFICIAL, STAT_AFASTAMENTO FROM V_SPGUOFICIAIS WHERE NUMR_CPF_CNPJ = ?";
				
		ResultSetTJGO rs1 = null;
		
		try{
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			ps.adicionarString(cpf);
			
			rs1 = consultar(sql, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					oficialSPGDt = new OficialSPGDt();
					
					oficialSPGDt.setNomeOficial(	rs1.getString("NOME_OFICIAL"));
					oficialSPGDt.setCodigoOficial(	rs1.getString("CODG_OFICIAL"));
					oficialSPGDt.setAtivo(			rs1.getString("STAT_AFASTAMENTO"));
					oficialSPGDt.setCodigoComarca(	this.getCodigoComarca(rs1.getString("CODG_OFICIAL")));
				}
			}
		}
		finally{
			if( rs1 != null ) {
				rs1.close();
			}
		}
		
		return oficialSPGDt;
	}
}
