package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import java.sql.Connection;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AudienciaProcessoStatusPs extends AudienciaProcessoStatusPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 3722815540638009299L;

    public AudienciaProcessoStatusPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Método que contém a SQL responsável por consultar um objeto do tipo "AUDI_PROC_STATUS" dado o código do
	 * status da audiência de processo.
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaProcessoStatusCodigo
	 * @return AudienciaProcessoStatusDt audienciaProcessoStatusDt
	 * @throws Exception
	 */
	public AudienciaProcessoStatusDt consultarIdAudienciaProcessoStatusCodigo(int audienciaProcessoStatusCodigo) throws Exception {
		String sql;
		AudienciaProcessoStatusDt audienciaProcessoStatusDt = new AudienciaProcessoStatusDt();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT aps.ID_AUDI_PROC_STATUS, aps.AUDI_PROC_STATUS, aps.AUDI_PROC_STATUS_CODIGO, aps.CODIGO_TEMP FROM PROJUDI.VIEW_AUDI_PROC_STATUS aps WHERE aps.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(audienciaProcessoStatusCodigo);
		
		ResultSetTJGO rs1 = consultar(sql,ps);
		if (rs1.next()) {
			audienciaProcessoStatusDt.setId(rs1.getString("ID_AUDI_PROC_STATUS"));
			audienciaProcessoStatusDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
			audienciaProcessoStatusDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
			audienciaProcessoStatusDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
		}
		rs1.close();
		
		return audienciaProcessoStatusDt;
	}

	/**
	 * Consulta os Status de Audiência disponíveis para o caso de movimentação de Audiências.
	 * 
	 * @param serventiaTipoCodigo, tipo da serventia para o qual devem ser retornados os status 
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAudienciaProcessoStatusMovimentacao(String serventiaTipoCodigo) throws Exception {
		AudienciaProcessoStatusDt audienciaProcessoStatusDt = null;
		String sql;
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT * FROM PROJUDI.VIEW_AUDI_PROC_STATUS aps ";
		sql += " WHERE aps.AUDI_PROC_STATUS_CODIGO <> ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql += " AND aps.AUDI_PROC_STATUS_CODIGO <> ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.LIVRE);
		sql += " AND (aps.SERV_TIPO_CODIGO = ? OR aps.SERV_TIPO_CODIGO IS NULL)";
		ps.adicionarLong(serventiaTipoCodigo);
		sql += " ORDER BY AUDI_PROC_STATUS";
		
		ResultSetTJGO rs1 = consultar(sql,ps);
		while (rs1.next()) {
			audienciaProcessoStatusDt = new AudienciaProcessoStatusDt();
			this.associarDt(audienciaProcessoStatusDt, rs1);
			liTemp.add(audienciaProcessoStatusDt);
		}
		rs1.close();

		return liTemp;
	}

	/**
	 * Consulta os Status de Audiência disponíveis de acordo com o tipo da Serventia
	 * 
	 * @param descricao
	 * @param serventiaTipoCodigo
	 * @param posicao
	 */
	public List consultarDescricao(String descricao, String serventiaTipoCodigo, String posicao) throws Exception {
		
		String sql;
		String sqlFrom;
		String sqlOrder;
		List liTemp = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		

		sql = "SELECT ID_AUDI_PROC_STATUS, AUDI_PROC_STATUS";
		sqlFrom = " FROM PROJUDI.VIEW_AUDI_PROC_STATUS aps ";
		sqlFrom += " WHERE AUDI_PROC_STATUS LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlFrom += " AND (aps.SERV_TIPO_CODIGO = ? OR aps.SERV_TIPO_CODIGO IS NULL)";
		ps.adicionarLong(serventiaTipoCodigo);
		sqlOrder = " ORDER BY AUDI_PROC_STATUS ";
		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);

			while (rs1.next()) {
				AudienciaProcessoStatusDt obTemp = new AudienciaProcessoStatusDt();
				obTemp.setId(rs1.getString("ID_AUDI_PROC_STATUS"));
				obTemp.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
				liTemp.add(obTemp);
			}
			//rs1.close();
			sql = "SELECT COUNT(*) as QUANTIDADE";
			
			rs2 = consultar(sql + sqlFrom,ps);
			if (rs2.next())
				liTemp.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }  
		return liTemp;
	}
	
	public String consultarDescricaoJSON(String descricao, String serventiaTipoCodigo, String posicao) throws Exception {
		
		String sql = "";
		String sqlFrom = "";
		String sqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
		

		sql = "SELECT ID_AUDI_PROC_STATUS AS ID, AUDI_PROC_STATUS AS DESCRICAO1";
		sqlFrom = " FROM PROJUDI.VIEW_AUDI_PROC_STATUS aps";
		sqlFrom += " WHERE AUDI_PROC_STATUS LIKE ?";
		ps.adicionarString("%"+ descricao +"%");
		sqlFrom += " AND (aps.SERV_TIPO_CODIGO = ? OR aps.SERV_TIPO_CODIGO IS NULL)";
		ps.adicionarLong(serventiaTipoCodigo);
		sqlOrder = " ORDER BY AUDI_PROC_STATUS";
		
		try{
			rs1 = consultarPaginacao(sql + sqlFrom + sqlOrder, ps, posicao);
			sql = "SELECT COUNT(*) as QUANTIDADE";
			rs2 = consultar(sql + sqlFrom,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }  
		return stTemp;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {

		String stSql="";
		String stTemp = "";
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;

		stSql= "SELECT ID_AUDI_PROC_STATUS AS ID, AUDI_PROC_STATUS AS DESCRICAO1 FROM PROJUDI.VIEW_AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS LIKE ?";
		stSql+= " ORDER BY AUDI_PROC_STATUS ";
		ps.adicionarString("%"+descricao+"%"); 

		try{
			rs1 = consultarPaginacao(stSql, ps, posicao);
			stSql= "SELECT COUNT(*) as QUANTIDADE  FROM PROJUDI.VIEW_AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS LIKE ?";
			rs2 = consultar(stSql,ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
				try{if (rs2 != null) rs2.close();} catch(Exception e) {}
			}
			return stTemp; 
	}
	
	/**
	 * Consulta os Status de Audiência disponíveis para o caso de movimentação de Audiências.
	 * 
	 * @param serventiaTipoCodigo, tipo da serventia para o qual devem ser retornados os status 
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public AudienciaProcessoStatusDt consultarAudienciaProcessoStatusCodigoMovimentacao(String serventiaTipoCodigo, String AudienciaProcessoStatusCodigo) throws Exception {
		AudienciaProcessoStatusDt audienciaProcessoStatusDt = null;
		String sql;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT * FROM PROJUDI.VIEW_AUDI_PROC_STATUS aps ";
		sql += " WHERE aps.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusCodigo);		
		sql += " AND (aps.SERV_TIPO_CODIGO = ? OR aps.SERV_TIPO_CODIGO IS NULL)";
		ps.adicionarLong(serventiaTipoCodigo);		
		
		ResultSetTJGO rs1 = consultar(sql,ps);
		if (rs1.next()) {
			audienciaProcessoStatusDt = new AudienciaProcessoStatusDt();
			this.associarDt(audienciaProcessoStatusDt, rs1);				
		}
		rs1.close();
				
		return audienciaProcessoStatusDt;
	}
	
	/**
	 * Método que contém a SQL responsável por consultar um objeto do tipo "AUDI_PROC_STATUS" dado o id do
	 * status da audiência de processo.
	 * 
	 * @author hrrosa
	 * @param audienciaProcessoStatusId
	 * @return AudienciaProcessoStatusDt audienciaProcessoStatusDt
	 * @throws Exception
	 */
	public AudienciaProcessoStatusDt consultarAudienciaProcessoStatusCodigo(int audienciaProcessoStatusId) throws Exception {
		String sql;
		AudienciaProcessoStatusDt audienciaProcessoStatusDt = new AudienciaProcessoStatusDt();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT aps.ID_AUDI_PROC_STATUS, aps.AUDI_PROC_STATUS, aps.AUDI_PROC_STATUS_CODIGO, aps.CODIGO_TEMP FROM PROJUDI.VIEW_AUDI_PROC_STATUS aps WHERE aps.ID_AUDI_PROC_STATUS = ?";
		ps.adicionarLong(audienciaProcessoStatusId);
		ResultSetTJGO rs1 = null;
		try {
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				audienciaProcessoStatusDt.setId(rs1.getString("ID_AUDI_PROC_STATUS"));
				audienciaProcessoStatusDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
				audienciaProcessoStatusDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
				audienciaProcessoStatusDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			rs1.close();
		} finally{
            try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
		}
		return audienciaProcessoStatusDt;
	}

	// jvosantos - 04/06/2019 10:34 - Consulta o status da audiencia pelo seu codigo
	public AudienciaProcessoStatusDt consultarAudienciaProcessoStatusPorCodigo(String codigo) throws Exception {
		String sql;
		AudienciaProcessoStatusDt audienciaProcessoStatusDt = new AudienciaProcessoStatusDt();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT aps.ID_AUDI_PROC_STATUS, aps.AUDI_PROC_STATUS, aps.AUDI_PROC_STATUS_CODIGO, aps.CODIGO_TEMP FROM PROJUDI.VIEW_AUDI_PROC_STATUS aps WHERE aps.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(codigo);
		ResultSetTJGO rs1 = null;
		try {
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				audienciaProcessoStatusDt.setId(rs1.getString("ID_AUDI_PROC_STATUS"));
				audienciaProcessoStatusDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
				audienciaProcessoStatusDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
				audienciaProcessoStatusDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
			rs1.close();
		} finally{
            try {if (rs1 != null) rs1.close();} catch (Exception e1) {}             
		}
		return audienciaProcessoStatusDt;
	}

}
