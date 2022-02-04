package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class EstatisticaPublicaPs extends Persistencia {

	private static final long serialVersionUID = 5308271275047123747L;

	
	public EstatisticaPublicaPs(Connection conexao){
		Conexao = conexao;
	}


	/**
	 * Método que calcula o quantitativo de processos para uma determinada Comarca.
	 * @return lista de processos da comarca
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List calcularQuantitativoProcessosComarca() throws Exception {
		
		List listaProcessosEstado = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;

		String sql = " SELECT c.ID_COMARCA, c.COMARCA, " +
						" (SELECT COUNT(*) FROM PROJUDI.PROC p0 " +
						" 	INNER JOIN PROJUDI.SERV s0 ON p0.ID_SERV = s0.ID_SERV " +
						"  	INNER JOIN PROJUDI.COMARCA c0 ON s0.ID_COMARCA = c0.ID_COMARCA " +
						"		WHERE p0.DATA_ARQUIVAMENTO IS NULL " +
						"			AND c0.ID_COMARCA = c.ID_COMARCA " +
						"			AND p0.ID_AREA = ?) AS CIVEL_ATIVO, ";
		ps.adicionarLong(AreaDt.CIVEL);

		sql += 		" (SELECT COUNT(*) FROM PROJUDI.PROC p1 " +
						"	INNER JOIN PROJUDI.SERV s1 ON p1.ID_SERV = s1.ID_SERV " +
						"	INNER JOIN PROJUDI.COMARCA c1 ON s1.ID_COMARCA = c1.ID_COMARCA " +
						"		WHERE p1.DATA_ARQUIVAMENTO IS NOT NULL " +
						"			AND c1.ID_COMARCA = c.ID_COMARCA " +
						"			AND p1.ID_AREA = ?) AS CIVEL_ARQUIVADO, ";
		ps.adicionarLong(AreaDt.CIVEL);

		sql += 		" (SELECT COUNT(*) FROM PROJUDI.PROC p2 " +
						" 	INNER JOIN PROJUDI.SERV s2 ON p2.ID_SERV = s2.ID_SERV " +
						"	INNER JOIN PROJUDI.COMARCA c2 ON s2.ID_COMARCA = c2.ID_COMARCA " +
						"		WHERE p2.DATA_RECEBIMENTO >=  ?";
		ps.adicionarDate(Funcoes.getPrimeiroDiaMes());
		sql += 		"			AND c2.ID_COMARCA = c.ID_COMARCA " +
						"			AND p2.ID_AREA = ?) AS CIVEL_RECEBIDO, ";
		ps.adicionarLong(AreaDt.CIVEL);

		sql += 		" (SELECT COUNT(*) FROM PROJUDI.PROC p3 " +
						"	INNER JOIN PROJUDI.SERV s3 ON p3.ID_SERV = s3.ID_SERV " +
						"	INNER JOIN PROJUDI.COMARCA c3 ON s3.ID_COMARCA = c3.ID_COMARCA " +
						"		WHERE p3.DATA_ARQUIVAMENTO IS NULL " +
						"			AND c3.ID_COMARCA = c.ID_COMARCA " +
						"			AND p3.ID_AREA = ?) as CRIMINAL_ATIVO , ";
		ps.adicionarLong(AreaDt.CRIMINAL);

		sql += 		" (SELECT COUNT(*) FROM PROJUDI.PROC p4 " +
						"	INNER JOIN PROJUDI.SERV s4 ON p4.ID_SERV = s4.ID_SERV " +
						"	INNER JOIN PROJUDI.COMARCA c4 ON s4.ID_COMARCA = c4.ID_COMARCA " +
						"		WHERE p4.DATA_ARQUIVAMENTO IS NOT NULL " +
						"			AND c4.ID_COMARCA = c.ID_COMARCA " +
						"			AND p4.ID_AREA = ?) as CRIMINAL_ARQUIVADO,	";
		ps.adicionarLong(AreaDt.CRIMINAL);

		sql += 		" (SELECT COUNT(*) FROM PROJUDI.PROC p5 " +
						"	INNER JOIN PROJUDI.SERV s5 ON p5.ID_SERV = s5.ID_SERV " +
						"	INNER JOIN PROJUDI.COMARCA c5 ON s5.ID_COMARCA = c5.ID_COMARCA " +
						"		WHERE p5.DATA_RECEBIMENTO >=  ?";
		ps.adicionarDate(Funcoes.getPrimeiroDiaMes());
		sql += 		"			AND c5.ID_COMARCA = c.ID_COMARCA " +
						"			AND p5.ID_AREA = ?) AS CRIMINAL_RECEBIDO ";
		ps.adicionarLong(AreaDt.CRIMINAL);

		sql += 		" FROM PROJUDI.COMARCA c ";
		sql += 		" ORDER BY c.COMARCA ";
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				Map obTemp = new HashMap();
				obTemp.put("Id_Comarca", rs.getLong("ID_COMARCA"));
				obTemp.put("Comarca", rs.getString("COMARCA"));
				obTemp.put("CivelAtivo", rs.getLong("CIVEL_ATIVO"));
				obTemp.put("CivelArquivado", rs.getLong("CIVEL_ARQUIVADO"));
				obTemp.put("CivelRecebido", rs.getLong("CIVEL_RECEBIDO"));
				obTemp.put("CriminalAtivo", rs.getLong("CRIMINAL_ATIVO"));
				obTemp.put("CriminalArquivado", rs.getLong("CRIMINAL_ARQUIVADO"));
				obTemp.put("CriminalRecebido", rs.getLong("CRIMINAL_RECEBIDO"));
				listaProcessosEstado.add(obTemp);
			}

		
		} finally{
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}
		return listaProcessosEstado;
	}
	
	/**
	 * Método que calcula o quantitativo de processos para as Serventias de uma determinada Comarca.
	 * @param idComarca - ID da Cormarca
	 * @return lista de processos das serventias
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List calcularQuantitativoProcessosServentia(String idComarca) throws Exception {
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		List listaProcessosEstado = new ArrayList();

		ResultSetTJGO rs = null;

		String sql = "SELECT s.SERV AS SERV, " +
					"	(SELECT COUNT(*) FROM PROJUDI.PROC p0 " +
					"		INNER JOIN PROJUDI.SERV s0 ON p0.ID_SERV = s0.ID_SERV " +
					"			WHERE p0.DATA_ARQUIVAMENTO IS NULL " +
					"				AND s0.ID_SERV = s.ID_SERV " +
					"				AND p0.ID_AREA = ?) AS CIVEL_ATIVO, ";
		ps.adicionarLong(AreaDt.CIVEL);

		sql += 		"	(SELECT COUNT(*) FROM PROJUDI.PROC p1 " +
					"		INNER JOIN PROJUDI.SERV s1 ON p1.ID_SERV = s1.ID_SERV " +
					"			WHERE p1.DATA_ARQUIVAMENTO IS NOT NULL " +
					"				AND s1.ID_SERV = s.ID_SERV " +
					"				AND p1.ID_AREA = ?) AS CIVEL_ARQUIVADO, ";
		ps.adicionarLong(AreaDt.CIVEL);

		sql += 		"	(SELECT COUNT(*) FROM PROJUDI.PROC p2 " +
					"		INNER JOIN PROJUDI.SERV s2 ON p2.ID_SERV = s2.ID_SERV " +
					"			WHERE p2.DATA_RECEBIMENTO >= ?";
		ps.adicionarDate(Funcoes.getPrimeiroDiaMes());
		sql += 		"				AND s2.ID_SERV = s.ID_SERV " +
					"				AND p2.ID_AREA = ?) AS CIVEL_RECEBIDO, ";
		ps.adicionarLong(AreaDt.CIVEL);

		sql += 		"	(SELECT COUNT(*) FROM PROJUDI.PROC p3 " +
					"		INNER JOIN PROJUDI.SERV s3 ON p3.ID_SERV = s3.ID_SERV " +
					"			WHERE p3.DATA_ARQUIVAMENTO IS NULL " +
					"				AND s3.ID_SERV = s.ID_SERV " +
					"				AND p3.ID_AREA = ?) AS CRIMINAL_ATIVO,	";
		ps.adicionarLong(AreaDt.CRIMINAL);

		sql += 		"	(SELECT COUNT(*) FROM PROJUDI.PROC p4 " +
					"		INNER JOIN PROJUDI.SERV s4 ON p4.ID_SERV = s4.ID_SERV " +
					"			WHERE p4.DATA_ARQUIVAMENTO IS NOT NULL " +
					"				AND s4.ID_SERV = s.ID_SERV " +
					" 				AND p4.ID_AREA = ?) AS CRIMINAL_ARQUIVADO, ";
		ps.adicionarLong(AreaDt.CRIMINAL);

		sql += 		"	(SELECT COUNT(*) FROM PROJUDI.PROC p5 " +
					"		INNER JOIN PROJUDI.SERV s5 ON p5.ID_SERV = s5.ID_SERV " +
					"			WHERE p5.DATA_RECEBIMENTO >= ?";
		ps.adicionarDate(Funcoes.getPrimeiroDiaMes());
		sql += 		"				AND s5.ID_SERV = s.ID_SERV " +
					"				AND p5.ID_AREA = ?) AS CRIMINAL_RECEBIDO " ;
		ps.adicionarLong(AreaDt.CRIMINAL);

		sql += 		" FROM PROJUDI.SERV s " +
					"	INNER JOIN PROJUDI.COMARCA c on s.ID_COMARCA = c.ID_COMARCA " +
					"		WHERE c.ID_COMARCA = ? "; 
		ps.adicionarLong(Funcoes.StringToLong(idComarca));
		
		sql += 		"			AND s.ID_SERV_TIPO IN (?,?) ";
		ps.adicionarLong(ServentiaTipoDt.VARA);
		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		sql += 		" ORDER BY s.SERV ";
				
		try{
			rs = consultar(sql.toString(),ps);
			while (rs.next()) {
				Map obTemp = new HashMap();
				obTemp.put("Serventia", rs.getString("SERV"));
				obTemp.put("CivelAtivo", rs.getLong("CIVEL_ATIVO"));
				obTemp.put("CivelArquivado", rs.getLong("CIVEL_ARQUIVADO"));
				obTemp.put("CivelRecebido", rs.getLong("CIVEL_RECEBIDO"));
				obTemp.put("CriminalAtivo", rs.getLong("CRIMINAL_ATIVO"));
				obTemp.put("CriminalArquivado", rs.getLong("CRIMINAL_ARQUIVADO"));
				obTemp.put("CriminalRecebido", rs.getLong("CRIMINAL_RECEBIDO"));
				listaProcessosEstado.add(obTemp);
			}

		
		} finally{
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}
		return listaProcessosEstado;
	}
	
	/**
	 * Método que consulta a quantidade de processos ativos por Tipo de Processo
	 * e Comarca.
	 * 
	 * @return lista de comarcas, tipos de processos e quantidade de ativos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List calcularQuantidadeProcessosAtivosProcessoTipoComarca() throws Exception {

		List listaProcessosAtivos = new ArrayList();

		ResultSetTJGO rs = null;

		String sql = "SELECT c.COMARCA, pt.PROC_TIPO AS PROC_TIPO, COUNT(*) AS QUANTIDADE " + 
					" 	FROM PROJUDI.PROC p " + 
					" 		INNER JOIN PROJUDI.SERV s ON p.ID_SERV = s.ID_SERV " + 
					" 		INNER JOIN PROJUDI.COMARCA c ON s.ID_COMARCA = c.ID_COMARCA " + 
					" 		INNER JOIN PROJUDI.PROC_TIPO pt ON p.ID_PROC_TIPO = pt.ID_PROC_TIPO " +
					" 		WHERE p.DATA_ARQUIVAMENTO IS NULL " + 
					" 		GROUP BY c.COMARCA, pt.ID_PROC_TIPO, pt.PROC_TIPO ";
		try{
			rs = consultarSemParametros(sql.toString());
			while (rs.next()) {
				Map obTemp = new HashMap();
				obTemp.put("Comarca", rs.getString("COMARCA"));
				obTemp.put("ProcessoTipo", rs.getString("PROC_TIPO"));
				obTemp.put("Quantidade", rs.getLong("QUANTIDADE"));
				listaProcessosAtivos.add(obTemp);
			}

		
		} finally{
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}
		return listaProcessosAtivos;
	}
	
	/**
	 * Quantitativo dos tipos de serventia com projudi instalado no estado.
	 * @return lista de Serventias
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarEstatisticaServentiasEstado() throws Exception {
		List listaServentiasEstado = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;

		String sql = "SELECT st.SERV_TIPO, tb1.QTD as QUANTIDADE " +
					"	FROM ( " +
					"		SELECT tb.ID_SERV_TIPO, COUNT(*) AS QTD " +
					"			FROM ( " +
					"				SELECT ID_SERV_TIPO, 1 AS QTD " +
					"					FROM PROJUDI.SERV s " +
					"					WHERE s.ID_SERV_TIPO IN (?,?) ";
					ps.adicionarLong(ServentiaTipoDt.VARA);
					ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		sql +=		"					GROUP BY s.SERV_CODIGO_EXTERNO, s.ID_SERV_TIPO " +
					"				) tb GROUP BY ID_SERV_TIPO " +
					"			UNION (" +
					"				SELECT ID_SERV_TIPO, COUNT(*) AS QTD " +
					"					FROM PROJUDI.SERV s " +
					"					WHERE s.ID_SERV_TIPO NOT IN (?,?) ";
					ps.adicionarLong(ServentiaTipoDt.VARA);
					ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		sql +=		"					GROUP BY s.ID_SERV_TIPO " +
					"				) " +
					"	)  tb1 " +
					"	INNER JOIN PROJUDI.SERV_TIPO st ON tb1.ID_SERV_TIPO = st.ID_SERV_TIPO " +
					"	ORDER BY st.SERV_TIPO";
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				Map obTemp = new HashMap();
				obTemp.put("ServentiaTipo", rs.getString("SERV_TIPO"));
				obTemp.put("Quantidade", rs.getLong("QUANTIDADE"));
				listaServentiasEstado.add(obTemp);
			}

		
		} finally{
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}
		return listaServentiasEstado;
	}
	
}