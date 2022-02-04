package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaCorreiosDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class PendenciaCorreiosPs extends PendenciaCorreiosPsGen {

	private static final long serialVersionUID = 4901067207287906306L;

	private PendenciaCorreiosPs() {
	}

	public PendenciaCorreiosPs(Connection conexao) {
		Conexao = conexao;
	}
	
	public PendenciaCorreiosDt consultarIdPendencia(String id_Pendencia) throws Exception {
		
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaCorreiosDt Dados = null;
		
		stSql = "SELECT * FROM PEND_CORREIOS WHERE ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);
		
		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new PendenciaCorreiosDt();
				associarDt(Dados, rs1);
			}
			
		} finally {
			try {if (rs1 != null) rs1.close();} finally {}
		}
		return Dados;
	}
	
	public PendenciaCorreiosDt consultarIdPendenciaStatus(String id_Pendencia, String idPendenciaStatus) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaCorreiosDt Dados = null;

		stSql = "SELECT PC.* FROM PEND_CORREIOS PC INNER JOIN PEND P ON PC.ID_PEND = P.ID_PEND WHERE P.ID_PEND = ? ";
		ps.adicionarLong(id_Pendencia);
		
		if (idPendenciaStatus != null && !idPendenciaStatus.equalsIgnoreCase("")){
			stSql += " AND P.ID_PEND_STATUS = ? ";
			ps.adicionarLong(idPendenciaStatus);
		}

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new PendenciaCorreiosDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {if (rs1 != null) rs1.close();} finally {}
		}
		return Dados;
	}
	
	public String consultarIdPendenciaRastreamento(String codigoRastreamento, String idPendenciaStatus) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String id_Pendencia = "";

		stSql = "SELECT P.ID_PEND FROM PEND_CORREIOS PC INNER JOIN PEND P ON PC.ID_PEND=P.ID_PEND WHERE PC.COD_RASTREAMENTO = ? ";
		ps.adicionarString(codigoRastreamento);
		
		if (idPendenciaStatus != null && !idPendenciaStatus.equalsIgnoreCase("")){
			stSql += " AND P.ID_PEND_STATUS = ? ";
			ps.adicionarLong(idPendenciaStatus);
		}

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				id_Pendencia = rs1.getString("ID_PEND");
			}
		} finally {
			try {if (rs1 != null) rs1.close();} finally {}
		}
		return id_Pendencia;
	}
	
	public void reverterEnvioLote(String lote) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";
		
		stSql = "UPDATE PEND_CORREIOS SET  ";

		stSql += "MATRIZ = ?";
		ps.adicionarLongNull();

		stSql += ",LOTE = ?";
		ps.adicionarLongNull();
		
		stSql += ",DATA_EXPEDICAO = ?";
		ps.adicionarDateTimeNull();

		stSql += " WHERE LOTE  = ? ";
		ps.adicionarLong(lote);

		executarUpdateDelete(stSql, ps);

	}
	
	public List consultarPendenciasLote(String lote) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List tempList = new ArrayList();

		stSql = "SELECT P.ID_PEND FROM PEND_CORREIOS PC INNER JOIN PEND P ON P.ID_PEND=PC.ID_PEND WHERE LOTE = ? AND ID_PEND_STATUS = ?";
		ps.adicionarLong(lote);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS);

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				tempList.add(rs1.getString("ID_PEND"));
			}

		} finally {
			try {if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		return tempList;
	}
	
	/**
	 * Método que consuta a quantidade de OS aberta
	 * 
	 * @param String idProcesso
	 * @return String
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public String consultaQuantidadePendenciaCorreiosOrdemServicoAbertas(String idProcesso) throws Exception {

		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String retorno = null;

		String stSql = "SELECT COUNT(PC.ID_PEND) AS QUANTIDADE FROM PROJUDI.PEND_CORREIOS PC "+
				"INNER JOIN PROJUDI.PEND PEND ON PEND.ID_PEND = PC.ID_PEND "+
				"WHERE PC.ORDEM_SERVICO = 1 "+
				"AND PEND.ID_PROC = ?";
		ps.adicionarLong(idProcesso);

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				retorno = rs1.getString("QUANTIDADE");
			}

		} finally {
			try {if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		return retorno;
	}
	
	/**
	 * Método que consuta a quantidade de OS fechadas
	 * 
	 * @param String idProcesso
	 * @return String
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public String consultaQuantidadePendenciaFinalCorreiosOrdemServicoFechada(String idProcesso) throws Exception {

		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String retorno = null;

		String stSql = "SELECT COUNT(PFC.ID_PEND) AS QUANTIDADE FROM PROJUDI.PEND_FINAL_CORREIOS PFC "+
				"INNER JOIN PROJUDI.PEND PEND ON PEND.ID_PEND = PFC.ID_PEND "+
				"WHERE PFC.ORDEM_SERVICO = 1 "+
				"AND PEND.ID_PROC = ?";
		ps.adicionarLong(idProcesso);

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				retorno = rs1.getString("QUANTIDADE");
			}

		} finally {
			try {if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		return retorno;
	}
	
	public PendenciaCorreiosDt consultarFinalizadaIdPendencia(String id_Pendencia) throws Exception {
		
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaCorreiosDt Dados = null;
		
		stSql = "SELECT * FROM PEND_FINAL_CORREIOS WHERE ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);
		
		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new PendenciaCorreiosDt();
				associarDt(Dados, rs1);
			}
			
		} finally {
			try {if (rs1 != null) rs1.close();} finally {}
		}
		return Dados;
	}
	
	public String consultarIdPendenciaCorreios(String id_Pendencia) throws Exception {
		
		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String idPendCorreios = "";
		
		stSql = "SELECT ID_PEND_CORREIOS FROM PEND_CORREIOS WHERE ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);
		
		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				idPendCorreios = rs1.getString("ID_PEND_CORREIOS");
			}
		} finally {
			try {if (rs1 != null) rs1.close();} finally {}
		}
		return idPendCorreios;
	}
	
	public PendenciaCorreiosDt consultarIdFinal(String id_pendcorreios) throws Exception {

		String stSql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaCorreiosDt Dados = null;

		stSql = "SELECT * FROM PEND_FINAL_CORREIOS WHERE ID_PEND_CORREIOS = ?";
		ps.adicionarLong(id_pendcorreios);

		try {
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				Dados = new PendenciaCorreiosDt();
				associarDt(Dados, rs1);
			}
		} finally {
			try {
				if (rs1 != null) rs1.close();
			} catch (Exception e) {}
		}
		return Dados;
	}

}