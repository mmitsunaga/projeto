package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.dt.ContaUsuarioDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RelatoriosMandadoPs extends Persistencia {

	private static final long serialVersionUID = -2548786806822055395L;

	public RelatoriosMandadoPs(Connection conexao) {
		Conexao = conexao;
	}

	public int buscaSequenciaTextoBanco() throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int sequencia = 0;

		sql.append(" select seq_texto_banco.nextval as sequencia from dual");
		try {
			rs = consultar(sql.toString(), ps);
			rs.next();
			sequencia = rs.getInt("sequencia");

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return sequencia;
	}

	public List listaOrdemPagamentoAutorizada(String dataInicial, String dataFinal, int idUsuarioSessao,
			int idServentiaSessao) throws Exception {

		List listaOficiais = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try {
			sql.append("SELECT tab.*,"
					//
					+ " (SELECT u.nome FROM projudi.mand_jud mj"
					+ " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_2"
					+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ " WHERE mj.id_mand_jud = tab.idMandJud)  AS nomeCompanheiro FROM"
					//
					+ "	(SELECT u.id_usu AS idUsuario, u.nome AS nomeUsuario, u.cpf AS cpfUsuario,"
					+ " mj.id_mand_jud_pagamento_status AS idMandJudPagamentoStatus, mj.data_retorno, s.serv AS nomeServentia,"
					+ " TO_CHAR(mj.data_pagamento_status,'dd/mm/yyyy') AS dataPagamentoStatus,"
					+ " p.proc_numero || '-' || p.digito_verificador as numrProcesso,"
					+ " mj.id_mand_jud AS idMandJud,mjps.mand_jud_pagamento_status AS statusPagamento"
					+ " FROM projudi.mand_jud mj"
					+ "	INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1"
					+ "	INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
					+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ " INNER JOIN projudi.proc_parte pp ON pp.id_proc_parte = mj.id_proc_parte"
					+ " INNER JOIN projudi.proc p ON p.id_proc = pp.id_proc"
					+ " INNER JOIN projudi.mand_jud_pagamento_status mjps  ON mjps.id_mand_jud_pagamento_status = mj.id_mand_jud_pagamento_status"
					+ "	WHERE  mj.data_pagamento_status between ? AND ? AND mj.id_mand_jud_pagamento_status in(?,?)"
					+ " AND mj.assistencia = ?");

			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);
			ps.adicionarString(MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO);
			ps.adicionarString(MandadoJudicialDt.ID_PAGAMENTO_ENVIADO);
			ps.adicionarLong(MandadoJudicialDt.NAO_ASSISTENCIA);

			if (idUsuarioSessao != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuarioSessao);
			}

			if (idServentiaSessao != 0) {
				sql.append(" AND us.id_serv = ?");
				ps.adicionarLong(idServentiaSessao);
			}

			sql.append(") tab	ORDER BY nomeUsuario");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setIdUsuario(rs.getInt("idUsuario"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setCpfUsuario(rs.getString("cpfUsuario"));
				obTemp.setIdMandJud(rs.getString("idMandJud"));
				obTemp.setProcessoNumero(rs.getString("numrProcesso"));
				obTemp.setNomeServentia(rs.getString("nomeServentia"));
				obTemp.setNomeCompanheiro(rs.getString("nomeCompanheiro"));
				obTemp.setDataPagamentoStatus(rs.getString("dataPagamentoStatus"));
				if ((rs.getString("idMandJudPagamentoStatus").equalsIgnoreCase(MandadoJudicialDt.ID_PAGAMENTO_ENVIADO)))
					obTemp.setStatusPagamento(rs.getString("statusPagamento"));
				listaOficiais.add(obTemp);
			}

		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaOficiais;
	}

	public List RelatorioFinanceiroMandadoComCustas(String dataInicial, String dataFinal, int tipoArquivo,
			int idUsuario, int idServentia) throws Exception {

		List listaOficiais = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String array[] = new String[5];
		try {

			sql.append("SELECT vw.idUsuario, vw.nomeUsuario, vw.cpfUsuario, vw.nomeComarca,"
					+ " vw.infoConta, sum (vw.locomocao) AS valorLocomocao from"
					//
					+ "	(SELECT tab.*,"
					+ "	(SELECT CASE WHEN (tab.companheiro is null or tab.companheiro = 0) THEN SUM(gi.valor_calculado)"
					+ "	ELSE SUM(gi.valor_calculado/2) END FROM projudi.locomocao l"
					+ "	INNER JOIN projudi.guia_item gi ON gi.id_guia_item = l.id_guia_item"
					+ "	WHERE l.id_mand_jud = tab.idMandJud AND l.codigo_oficial_spg IS NULL) AS locomocao,"
					//
					+ "	(SELECT b.banco_codigo||'#'||a.agencia_codigo||'#'||cu.conta_usu_operacao||'#'||"
					+ "	cu.conta_usu||'#'||cu.conta_usu_dv FROM projudi.conta_usu cu"
					+ " INNER JOIN projudi.agencia a ON a.id_agencia = cu.id_agencia"
					+ "	INNER JOIN projudi.banco b ON b.id_banco = a.id_banco WHERE ROWNUM = 1 and cu.ativa = ?"
					+ " AND cu.id_usu = tab.idUsuario) AS infoConta from"
					//
					+ "	(SELECT u.id_usu AS idUsuario, u.nome AS nomeUsuario, u.cpf AS cpfUsuario, c.comarca AS nomeComarca,"
					+ "	mj.id_mand_Jud AS idMandJud, mj.id_usu_serv_2 as companheiro FROM projudi.mand_jud mj"
					//
					+ "	INNER JOIN projudi.usu_serv us ON (us.id_usu_serv = mj.id_usu_serv_1 or  us.id_usu_serv = mj.id_usu_serv_2)"
					//
					+ " INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ " INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
					+ " INNER JOIN projudi.comarca c ON c.id_comarca = s.id_comarca"
					+ "	WHERE  mj.data_pagamento_status between ? AND ?"
					+ "	AND mj.id_mand_jud_pagamento_status in (?,?) AND mj.assistencia = ?");

			ps.adicionarLong(ContaUsuarioDt.ATIVO);
			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);
			ps.adicionarString(MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO);
			ps.adicionarString(MandadoJudicialDt.ID_PAGAMENTO_ENVIADO);
			ps.adicionarLong(MandadoJudicialDt.NAO_ASSISTENCIA);

			if (idUsuario != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND us.id_serv = ?");
				ps.adicionarLong(idServentia);
			}

			sql.append(" )tab ORDER BY nomeComarca, nomeUsuario) vw  GROUP BY vw.idUsuario, vw.nomeUsuario,"
					+ "	vw.cpfUsuario, vw.nomeComarca, vw.infoConta");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setIdUsuario(rs.getInt("idUsuario"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setCpfUsuario(rs.getString("cpfUsuario"));
				obTemp.setNomeComarca(rs.getString("nomeComarca"));
				obTemp.setInfoConta(rs.getString("infoConta"));
				obTemp.setValorReceber(rs.getDouble("valorLocomocao"));

				if (obTemp.getInfoConta() != null) {
					array = obTemp.getInfoConta().split("#");
					obTemp.setBanco(Integer.parseInt(array[0]));
					if (obTemp.getBanco() == BancoDt.CODIGO_CAIXA_ECONOMICA_FEDERAL)
						obTemp.setNomeBanco("CAIXA");
					else
						obTemp.setNomeBanco("OUTROS BANCOS");
					obTemp.setAgencia(Integer.parseInt(array[1]));
					obTemp.setContaOperacao(Integer.parseInt(array[2]));
					obTemp.setConta(Integer.parseInt(array[3]));
					obTemp.setContaDv(array[04]);
					listaOficiais.add(obTemp);
				} else if (tipoArquivo == 1) // sair sem banco so no relatorio
					listaOficiais.add(obTemp);
			}

		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaOficiais;
	}

	public List listaMandadosGratuitos(String anoMes, int tipoArquivo, int idUsuario, int idServentia)
			throws Exception {

		List listaMandado = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try {

			sql.append(
					"SELECT u.id_usu AS idUsu, u.nome AS nome , c.comarca AS comarca, et.escala_tipo_mg AS escalaTipoMg, mj.resolutivo AS resolutivo,"
							+ "	u.cpf AS cpf, b.banco_codigo AS bancoCodigo, a.agencia_codigo AS agenciaCodigo, mj.id_mand_jud AS idMandJud,"
							+ " conta_usu_operacao AS contaUsuOperacao, cu.conta_usu AS contaUsu, cu.conta_usu_dv AS contaUsuDv,"
							+ "	TO_CHAR(mj.data_dist,'yyyymmdd') AS dataDist, to_char(mj.data_dist, 'hh24miss') AS horaDist FROM projudi.MAND_JUD mj"
							+ "	INNER JOIN projudi.esc e ON e.id_esc = mj.id_esc"
							+ "	INNER JOIN projudi.usu_serv us on us.id_usu_serv = mj.id_usu_serv_1"
							+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
							+ "	INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
							+ "	INNER JOIN projudi.comarca c ON c.id_comarca = s.id_comarca"
							+ "	LEFT JOIN projudi.escala_mg em ON em.id_usuario = us.id_usu AND em.data_fim is null"
							+ "	LEFT JOIN projudi.escala_tipo_mg et ON et.id_escala_tipo_mg = em.id_escala_tipo_mg"
							+ "	LEFT JOIN projudi.conta_usu cu ON cu.id_usu = u.id_usu AND ROWNUM = 1 AND cu.ativa = ?"
							+ "	LEFT JOIN projudi.agencia a ON a.id_agencia = cu.id_agencia"
							+ "	LEFT JOIN projudi.banco b ON b.id_banco = a.id_banco"
							+ "	WHERE TO_CHAR(mj.data_dist,'yyyymm') IN (?)"
							+ "	AND mj.assistencia = ? AND e.tipo_especial != ?");

			ps.adicionarLong(ContaUsuarioDt.ATIVO);
			ps.adicionarString(anoMes);
			ps.adicionarLong(MandadoJudicialDt.SIM_ASSISTENCIA);
			ps.adicionarLong(EscalaDt.TIPO_ESPECIAL_ADHOC);

			if (idUsuario != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND us.id_serv = ?");
				ps.adicionarLong(idServentia);
			}

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setIdUsuario(rs.getInt("idUsu"));
				obTemp.setNomeUsuario(rs.getString("nome"));
				obTemp.setCpfUsuario(rs.getString("cpf"));
				obTemp.setNomeComarca(rs.getString("comarca"));
				obTemp.setResolutivo(rs.getInt("resolutivo"));
				obTemp.setIdMandJud(rs.getString("idMandJud"));
				obTemp.setBanco(rs.getInt("bancoCodigo"));
				obTemp.setAgencia(rs.getInt("agenciaCodigo"));
				obTemp.setContaOperacao(rs.getInt("contaUsuOperacao"));
				obTemp.setConta(rs.getInt("contaUsu"));
				obTemp.setContaDv(rs.getString("contaUsuDv"));
				obTemp.setDataString(rs.getString("dataDist"));
				obTemp.setHoraString(rs.getString("horaDist"));
				if (rs.getString("escalaTipoMg") != null)
					obTemp.setEscalaTipo(rs.getString("escalaTipoMg"));
				else
					obTemp.setEscalaTipo("Normal");

				if (obTemp.getBanco() != 0) {
					if (obTemp.getBanco() == BancoDt.CODIGO_CAIXA_ECONOMICA_FEDERAL)
						obTemp.setNomeBanco("CAIXA");
					else
						obTemp.setNomeBanco("OUTROS BANCOS");
					listaMandado.add(obTemp);

				} else if (tipoArquivo == 1) // sair sem conta bancaria so no relatorio. no texto para o banco não pode
												// sair.
					listaMandado.add(obTemp);
			}

		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaMandado;
	}
	
	public List listaMandadosDistribuidosAnalitico(String dataInicial, String dataFinal, int idUsuario,
			String assistencia, int idServentia) throws Exception {

		List listaMandados = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try {
			sql.append(
					"SELECT u.id_usu AS idUsuario, u.nome AS nomeUsuario, u.cpf AS cpfUsuario, et.escala_tipo_mg AS escalaTipoMg,"
							+ "	mjp.mand_jud_pagamento_status AS statusPagamento, TO_CHAR(mj.data_retorno,'dd/mm/yyyy') AS dataRetorno,"
							+ " TO_CHAR(pe.data_limite,'dd/mm/yyyy') AS dataLimite,"
							+ "	TO_CHAR(mj.data_dist,'dd/mm/yyyy') AS dataDist, mt.mand_tipo AS mandTipo, mj.resolutivo AS resolutivo, "
							+ "	p.proc_numero || '-' || p.digito_verificador AS numrProcesso, s1.serv as nomeServentia,"
							+ "	CASE WHEN p.id_area = 2 THEN 'Criminal' ELSE 'Civel' END AS area,"
							+ "	CASE WHEN mj.assistencia = 0 THEN 'COM CUSTAS' ELSE 'SEM CUSTAS'"
							+ "	END AS assistencia,	mj.id_mand_jud AS idMandJud, mjs.mand_jud_status AS statusMandJud"
							+ "	FROM projudi.mand_jud mj"
							+ "	INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1"
							+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
							+ "	INNER JOIN projudi.proc_parte pp ON pp.id_proc_parte = mj.id_proc_parte"
							+ "	INNER JOIN projudi.proc p ON p.id_proc = pp.id_proc"
							+ "	INNER JOIN projudi.serv s1 ON s1.id_serv = us.id_serv"
							+ " INNER JOIN projudi.mand_jud_status mjs ON mjs.id_mand_jud_status = mj.id_mand_jud_status"
							+ "	INNER JOIN projudi.mand_tipo mt ON mt.id_mand_tipo = mj.id_mand_tipo"
							+ "	INNER JOIN projudi.mand_jud_pagamento_status mjp ON mjp.id_mand_jud_pagamento_status = mj.id_mand_jud_pagamento_status"
							+ "	INNER JOIN projudi.esc e  ON e.id_esc = mj.id_esc"
							+ "	LEFT JOIN projudi.pend pe ON pe.id_pend = mj.id_pend"
							+ "	LEFT JOIN projudi.escala_mg e ON e.id_usuario = us.id_usu AND e.data_fim is null"
							+ " LEFT JOIN projudi.escala_tipo_mg et ON et.id_escala_tipo_mg = e.id_escala_tipo_mg"
							+ "	WHERE (mj.data_dist  BETWEEN ? AND ?) AND mj.assistencia = ?");

			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);

			if (assistencia.equalsIgnoreCase("sim")) {
				ps.adicionarLong(1);
				sql.append(" AND mj.id_mand_tipo <> 8");
			} else {
				ps.adicionarLong(0);
			}

			if (idUsuario != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND s1.id_serv = ?");
				ps.adicionarLong(idServentia);
			}

			sql.append(" ORDER BY nomeUsuario, 	mj.id_mand_jud");

			// sql.append(" ORDER BY nomeUsuario, TO_CHAR(mj.data_dist,'yyyymmdd')");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setIdUsuario(rs.getInt("idUsuario"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setCpfUsuario(rs.getString("cpfUsuario"));
				obTemp.setIdMandJud(rs.getString("idMandJud"));
				obTemp.setProcessoNumero(rs.getString("numrProcesso"));
				obTemp.setAreaProcesso(rs.getString("area"));
				obTemp.setMandTipo(rs.getString("mandTipo"));
				obTemp.setAssistencia(rs.getString("assistencia"));
				obTemp.setStatusPagamento(rs.getString("statusPagamento"));
				obTemp.setDataRetorno(rs.getString("dataRetorno"));
				obTemp.setDataDist(rs.getString("dataDist"));
				obTemp.setStatusMandJud(rs.getString("statusMandJud"));
				obTemp.setNomeServentia(rs.getString("nomeServentia"));
				obTemp.setEscalaTipo(rs.getString("escalaTipoMg"));
				obTemp.setDataLimite(rs.getString("dataLimite"));
				if (rs.getString("assistencia").equalsIgnoreCase("SEM CUSTAS")) {
					if (rs.getString("resolutivo") == null)
						obTemp.setAnalise("Não Analis.");
					else if (rs.getString("resolutivo").equalsIgnoreCase("1"))
						obTemp.setAnalise("Resolut.");
					else if (rs.getString("resolutivo").equalsIgnoreCase("2"))
						obTemp.setAnalise("Não Resol.");
				}
				listaMandados.add(obTemp);
			}

		} finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaMandados;
	}

	public List relatorioMandadosRedistribuidos(String dataInicial, String dataFinal, int idUsuario, int idServentia)
			throws Exception {

		List listaMandados = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try {
			sql.append(
					"SELECT TO_CHAR(hrm.data_redist,'dd/mm/yyyy') AS dataRedist, hrm.id_mand_jud AS idMandJud, hrm.motivo AS motivo,"
							+ " u1.nome AS oficialAnt, u2.nome As oficialAtual, hrm.id_esc_ant AS idEscAnt,"
							+ " hrm.id_esc_atual AS idEscAtual, mtr.mand_tipo_redist AS mandTipoRedist"
							+ " FROM projudi.hist_redist_mand hrm"
							+ "	INNER JOIN projudi.usu_serv us1 ON us1.id_usu_serv = hrm.id_usu_serv_ant"
							+ "	INNER JOIN projudi.usu_serv us2 ON us2.id_usu_serv = hrm.id_usu_serv_atual"
							+ " INNER JOIN projudi.usu u1 ON u1.id_usu = us1.id_usu"
							+ "	INNER JOIN projudi.usu u2 ON u2.id_usu = us2.id_usu"
							+ "	INNER JOIN projudi.mand_tipo_redist mtr ON mtr.id_mand_tipo_redist = hrm.id_mand_tipo_redist"
							+ " WHERE (hrm.data_redist  BETWEEN ? AND ?)");

			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);

			if (idUsuario != 0) {
				sql.append(" AND us1.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND us1.id_serv = ?");
				ps.adicionarLong(idServentia);
			}

			sql.append(" ORDER BY oficialAnt, 	TO_CHAR(hrm.data_redist,'yyyymmdd')");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setDataDist(rs.getString("dataRedist"));
				obTemp.setIdMandJud(rs.getString("idMandJud"));
				obTemp.setMotivo(rs.getString("motivo"));
				obTemp.setNomeAnt(rs.getString("oficialAnt"));
				obTemp.setNomeAtual(rs.getString("oficialAtual"));
				obTemp.setIdEscAnt(rs.getString("idEscAnt"));
				obTemp.setIdEscAtual(rs.getString("idEscAtual"));
				obTemp.setMandTipoRedist(rs.getString("mandTipoRedist"));
				listaMandados.add(obTemp);
			}
		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaMandados;
	}

	public List listaMandadosDistribuidosSintetico(String dataInicial, String dataFinal, int idUsuario,
			String assistencia, int idServentia) throws Exception {

		List listaMandados = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try {

			sql.append("SELECT u.nome AS nomeUsuario, s1.serv AS nomeServentia,"
					+ " CASE WHEN mj.assistencia = 0 THEN 'COM CUSTAS' ELSE 'SEM CUSTAS' END AS assistencia,"
					+ " COUNT(*) as quantidade FROM projudi.mand_jud mj"
					+ " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1"
					+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ "	INNER JOIN projudi.serv s1 ON s1.id_serv = us.id_serv"
					+ "	INNER JOIN projudi.esc e  ON e.id_esc = mj.id_esc"
					+ "	WHERE mj.data_dist  BETWEEN ? AND ? AND mj.assistencia = ?");

			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);

			if (assistencia.equalsIgnoreCase("sim")) {
				ps.adicionarLong(1);
				sql.append(" AND mj.id_mand_tipo <> 8");
			} else
				ps.adicionarLong(0);

			if (idUsuario != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND s1.id_serv = ?");
				ps.adicionarLong(idServentia);
			}

			sql.append(" GROUP BY s1.serv, u.nome, mj.assistencia  ORDER BY nomeUsuario, nomeServentia");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setQuantidade(rs.getInt("quantidade"));
				obTemp.setNomeServentia(rs.getString("nomeServentia"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setAssistencia(rs.getString("assistencia"));
				listaMandados.add(obTemp);
			}
		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaMandados;
	}

	public List listaMandadosPorOficial(String dataInicial, String dataFinal, int idUsuario, int idServentia,
			int tipoOpcao) throws Exception {

		List listaMandados = new ArrayList();

		int i = 0;

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try {
			sql.append("SELECT u.nome AS nomeUsuario, TO_CHAR(mj.data_retorno,'dd/mm/yyyy') AS dataRetorno,"
					+ " TO_CHAR(mj.data_dist,'dd/mm/yyyy') AS dataDist, mt.mand_tipo AS mandTipo,"
					+ "	p.proc_numero || '-' || p.digito_verificador AS numrProcesso,"
					+ " s.serv as nomeServentia, pe.data_limite AS dataLimite,"
					+ " CASE WHEN p.id_area = 2 THEN 'Criminal' ELSE 'Civel' END AS area,"
					+ "	mj.id_mand_jud AS idMandJud, mjs.mand_jud_status AS statusMandJud"
					+ "	FROM projudi.mand_jud mj"
					+ "	INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1"
					+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ "	INNER JOIN projudi.proc_parte pp ON pp.id_proc_parte = mj.id_proc_parte"
					+ "	INNER JOIN projudi.proc p ON p.id_proc = pp.id_proc"
					+ "	INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
					+ "	INNER JOIN projudi.esc e ON e.id_esc = mj.id_esc"
					+ " LEFT JOIN projudi.pend pe ON  pe.id_pend = mj.id_pend"
					+ "	INNER JOIN projudi.mand_jud_status mjs ON mjs.id_mand_jud_status = mj.id_mand_jud_status"
					+ "	INNER JOIN projudi.mand_tipo mt ON mt.id_mand_tipo = mj.id_mand_tipo");

			switch (tipoOpcao) {

			case 1: // concluidos
				sql.append(" WHERE mj.data_retorno  BETWEEN ? AND ?");
				ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
				ps.adicionarDateTimeUltimaHoraDia(dataFinal);
				break;

			case 2: // com o oficial
				sql.append(" WHERE mj.data_retorno is null");
				break;

			case 3: // atrasados
				sql.append(" WHERE pe.id_pend_status = ? AND pe.data_limite < SYSDATE");
				ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_RETORNO);
				break;

			case 4: // ad hoc
				sql.append(" WHERE mj.data_dist  BETWEEN ? AND ? AND e.tipo_especial = ?");
				ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
				ps.adicionarDateTimeUltimaHoraDia(dataFinal);
				ps.adicionarLong(EscalaDt.TIPO_ESPECIAL_ADHOC);
				break;
			}

			if (idUsuario != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND s.id_serv = ?");
				ps.adicionarLong(idServentia);
			}

			switch (tipoOpcao) {

			case 1:
				sql.append(" ORDER BY nomeUsuario, TO_CHAR(mj.data_retorno,'yyyymmdd')");
				break;

			case 2:
				sql.append(" ORDER BY nomeUsuario, TO_CHAR(mj.data_dist,'yyyymmdd')");
				break;

			case 3:
				sql.append(" ORDER BY nomeUsuario, TO_CHAR(pe.data_limite,'yyyymmdd')");
				break;

			case 4:
				sql.append(" ORDER BY nomeUsuario, TO_CHAR(mj.data_dist,'yyyymmdd')");
				break;
			}

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setIdMandJud(rs.getString("idMandJud"));
				obTemp.setProcessoNumero(rs.getString("numrProcesso"));
				obTemp.setAreaProcesso(rs.getString("area"));
				obTemp.setMandTipo(rs.getString("mandTipo"));
				obTemp.setDataRetorno(rs.getString("dataRetorno"));
				obTemp.setDataDist(rs.getString("dataDist"));
				obTemp.setStatusMandJud(rs.getString("statusMandJud"));
				obTemp.setNomeServentia(rs.getString("nomeServentia"));
				obTemp.setDataLimite(Funcoes.FormatarDataHora(rs.getString("dataLimite")));
				listaMandados.add(obTemp);
			}
		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaMandados;
	}

	public List listaMandadosPorOficial(String dataInicial, String dataFinal, int idUsuario, int idServentia)
			throws Exception {

		List listaMandados = new ArrayList();

		int i = 0;

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try {

			sql.append("SELECT u.nome AS nomeUsuario, TO_CHAR(mj.data_retorno,'dd/mm/yyyy') AS dataRetorno,"
					+ " TO_CHAR(mj.data_dist,'dd/mm/yyyy') AS dataDist, mt.mand_tipo AS mandTipo,"
					+ "	p.proc_numero || '-' || p.digito_verificador AS numrProcesso,"
					+ " s.serv as nomeServentia, pe.data_limite AS dataLimite,"
					+ " CASE WHEN p.id_area = 2 THEN 'Criminal' ELSE 'Civel' END AS area,"
					+ "	mj.id_mand_jud AS idMandJud, mjs.mand_jud_status AS statusMandJud"
					+ "	FROM projudi.hist_redist_mand hrm"
					+ " INNER JOIN projudi.mand_jud mj ON mj.id_mand_jud = hrm.id_mand_jud"
					+ "	INNER JOIN projudi.usu_serv us ON us.id_usu_serv = hrm.id_usu_serv_ant"
					+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ "	INNER JOIN projudi.proc_parte pp ON pp.id_proc_parte = mj.id_proc_parte"
					+ "	INNER JOIN projudi.proc p ON p.id_proc = pp.id_proc"
					+ "	INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
					+ "	INNER JOIN projudi.esc e ON e.id_esc = mj.id_esc"
					+ " LEFT JOIN projudi.pend pe ON  pe.id_pend = mj.id_pend"
					+ "	INNER JOIN projudi.mand_jud_status mjs ON mjs.id_mand_jud_status = mj.id_mand_jud_status"
					+ "	INNER JOIN projudi.mand_tipo mt ON mt.id_mand_tipo = mj.id_mand_tipo"
					+ "	WHERE  hrm.data_redist  BETWEEN ? AND ? AND hrm.motivo LIKE 'Devolvido%'");

			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);

			if (idUsuario != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND s.id_serv = ?");
				ps.adicionarLong(idServentia);
			}

			sql.append(" ORDER BY nomeUsuario, TO_CHAR(hrm.data_redist,'yyyymmdd')");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setIdMandJud(rs.getString("idMandJud"));
				obTemp.setProcessoNumero(rs.getString("numrProcesso"));
				obTemp.setAreaProcesso(rs.getString("area"));
				obTemp.setMandTipo(rs.getString("mandTipo"));
				obTemp.setDataRetorno(rs.getString("dataRetorno"));
				obTemp.setDataDist(rs.getString("dataDist"));
				obTemp.setStatusMandJud(rs.getString("statusMandJud"));
				obTemp.setNomeServentia(rs.getString("nomeServentia"));
				obTemp.setDataLimite(Funcoes.FormatarDataHora(rs.getString("dataLimite")));
				listaMandados.add(obTemp);
			}
		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaMandados;
	}

	public List RelatorioFinanceiroMandadoAdHoc(String anoMes, int idUsuario, int idServentia) throws Exception {

		List listaOficiais = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String array[] = new String[5];
		try {

			sql.append(
					"SELECT vw.idUsuario, vw.nomeUsuario, vw.cpfUsuario, vw.nomeComarca, vw.infoConta, sum (vw.valorReceber) AS valorReceber from"
							//
							+ " (SELECT tab.*,"
							//
							+ "	(SELECT c.comarca FROM projudi.usu_serv us "
							+ " INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
							+ " INNER JOIN projudi.serv_tipo st ON st.id_serv_tipo = s.id_serv_tipo"
							+ "	AND st.serv_tipo_codigo = ? INNER JOIN projudi.comarca c"
							+ "	ON c.id_comarca = s.id_comarca WHERE ROWNUM = 1 AND us.ativo = ? AND Us.id_usu = tab.idUsuario) AS nomeComarca,"
							//
							+ " (SELECT mfv.valor from projudi.mandado_faixa_valor mfv where mfv.tipo_valor = 'VALOR AD HOC' and"
							+ "  MFV.DATA_VIGENCIA_FIM IS NULL) AS valorReceber,"
							//
							+ " (SELECT b.banco_codigo||'#'||a.agencia_codigo||'#'||cu.conta_usu_operacao||'#'||"
							+ "	cu.conta_usu||'#'||cu.conta_usu_dv FROM projudi.conta_usu cu"
							+ "	INNER JOIN projudi.agencia a ON a.id_agencia = cu.id_agencia"
							+ "	INNER JOIN projudi.banco b ON b.id_banco = a.id_banco"
							+ "	WHERE rownum = 1 and cu.ativa = ? AND cu.id_usu = tab.idUsuario) as infoConta from"
							//
							+ "	(SELECT u.id_usu AS idUsuario, u.nome AS nomeUsuario, u.cpf AS cpfUsuario, mj.id_mand_Jud AS idMandJud"
							+ "	FROM projudi.mand_jud mj" + " INNER JOIN projudi.esc e on e.id_esc = mj.id_esc "
							+ " INNER JOIN projudi.usu_serv us ON (us.id_usu_serv = mj.id_usu_serv_1 or  us.id_usu_serv = mj.id_usu_serv_2)"
							+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
							+ " WHERE  TO_CHAR(mj.data_dist,'yyyymm') IN (?) AND e.id_esc = mj.id_esc"
							+ "	AND e.tipo_especial = ?");

			ps.adicionarLong(ServentiaTipoDt.CENTRAL_MANDADOS);
			ps.adicionarLong(UsuarioServentiaDt.ATIVO);
			ps.adicionarLong(ContaUsuarioDt.ATIVO);
			ps.adicionarString(anoMes);
			ps.adicionarLong(EscalaDt.TIPO_ESPECIAL_ADHOC);

			if (idUsuario != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND us.id_serv = ?");
				ps.adicionarLong(idServentia);
			}

			sql.append(" )tab ORDER BY nomeComarca, nomeUsuario) vw  GROUP BY vw.idUsuario, vw.nomeUsuario,"
					+ "	vw.cpfUsuario, vw.nomeComarca, vw.infoConta");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setIdUsuario(rs.getInt("idUsuario"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setCpfUsuario(rs.getString("cpfUsuario"));
				obTemp.setNomeComarca(rs.getString("nomeComarca"));
				obTemp.setInfoConta(rs.getString("infoConta"));
				obTemp.setValorReceber(rs.getDouble("valorReceber"));
				array = obTemp.getInfoConta().split("#");
				obTemp.setBanco(Integer.parseInt(array[0]));
				obTemp.setAgencia(Integer.parseInt(array[1]));
				obTemp.setContaOperacao(Integer.parseInt(array[2]));
				obTemp.setConta(Integer.parseInt(array[3]));
				obTemp.setContaDv(array[04]);
				listaOficiais.add(obTemp);
			}
		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaOficiais;
	}

	public List RelatorioFinanceiroGuiaVinculada(String dataInicial, String dataFinal, int idUsuario, int idServentia)
			throws Exception {

		List listaGuias = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try {
			sql.append("SELECT v.nomeUsuario, v.cpfUsuario, v.dataEmis, v.statusPagamento,  v.dataRetorno, v.dataDist,"
					+ " v.numrProcesso, v.nomeServentia, v.idMandJud, v.statusMandJud, v.numeroGuiaCompleto,"
					+ " sum( v.quantidade) AS quant, sum (v.valorCalculado) AS valor FROM ("
					+ " SELECT u.nome AS nomeUsuario, u.cpf AS cpfUsuario, TO_CHAR(ge.data_emis,'dd/mm/yyyy') AS dataEmis,"
					+ " mjp.mand_jud_pagamento_status AS statusPagamento,"
					+ " TO_CHAR(mj.data_retorno,'dd/mm/yyyy') AS dataRetorno,"
					+ " TO_CHAR(mj.data_dist,'dd/mm/yyyy') AS dataDist,"
					+ " p.proc_numero || '-' || p.digito_verificador AS numrProcesso, s.serv as nomeServentia,"
					+ " ge.numero_guia_completo AS numeroGuiaCompleto, mj.id_mand_jud AS idMandJud, mjs.mand_jud_status AS statusMandJud,"
					+ " gi.quantidade AS quantidade, gi.valor_calculado AS valorCalculado"
					+ "	FROM projudi.guia_emis ge"
					+ "	INNER JOIN projudi.guia_item gi ON gi.id_guia_emis = ge.id_guia_emis"
					+ "	INNER JOIN projudi.locomocao l  ON l.id_guia_item = gi.id_guia_item"
					+ "	INNER JOIN projudi.mand_jud  mj ON mj.id_mand_jud = l.id_mand_jud"
					+ "	INNER JOIN projudi.mand_jud_status mjs ON mjs.id_mand_jud_status = mj.id_mand_jud_status"
					+ "	INNER JOIN projudi.mand_jud_pagamento_status mjp ON mjp.id_mand_jud_pagamento_status = mj.id_mand_jud_pagamento_status"
					+ "	INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1"
					+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ "	INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
					+ "	INNER JOIN projudi.proc p ON p.id_proc = ge.id_proc"
					+ "	WHERE l.codigo_oficial_spg IS NOT NULL AND l.id_mand_jud IS NOT NULL AND ge.data_emis BETWEEN ? AND ?");

			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);

			if (idUsuario != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND us.id_serv = ?");
				ps.adicionarLong(idServentia);
			}
			sql.append(" ) v GROUP BY v.nomeUsuario, v.cpfUsuario, v.dataEmis, v.statusPagamento,  v.dataRetorno,"
					+ " v.dataDist, v.numrProcesso, v.nomeServentia, v.idMandJud, v.statusMandJud, v.numeroGuiaCompleto"
					+ " ORDEr BY v.nomeUsuario");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setCpfUsuario(rs.getString("cpfUsuario"));
				obTemp.setIdMandJud(rs.getString("idMandJud"));
				obTemp.setProcessoNumero(rs.getString("numrProcesso"));
				obTemp.setNumeroGuiaCompleto(Funcoes.FormatarNumeroSerieGuia(rs.getString("numeroGuiaCompleto")));
				obTemp.setDataEmis(rs.getString("dataEmis"));
				// obTemp.setStatusPagamento(rs.getString("statusPagamento"));
				obTemp.setStatusPagamento("");
				obTemp.setDataRetorno(rs.getString("dataRetorno"));
				obTemp.setDataDist(rs.getString("dataDist"));
				obTemp.setStatusMandJud(rs.getString("statusMandJud"));
				obTemp.setNomeServentia(rs.getString("nomeServentia"));
				obTemp.setQuantidadeLocomocao(rs.getString("quant"));
				obTemp.setValorLocomocao(rs.getDouble("valor"));
				listaGuias.add(obTemp);
			}
		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaGuias;
	}
	
	////////////////////////tirar
	public List relatorioFinanceiroMandadoGratuito(String arrayAnoMes[], int tipoArquivo, int idUsuario,
			int idServentia) throws Exception {

		List listaMandado = new ArrayList();

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String array[] = new String[5];
		try {

			sql.append("SELECT tab.*," + " (SELECT c.comarca FROM projudi.usu_serv us"
					+ " INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
					+ " INNER JOIN projudi.serv_tipo st ON st.id_serv_tipo = s.id_serv_tipo AND st.serv_tipo_codigo = ?"
					+ " INNER JOIN projudi.comarca c ON c.id_comarca = s.id_comarca"
					+ " WHERE ROWNUM = 1 AND us.ativo = ? AND  us.id_usu = tab.idUsuario) AS nomeComarca,"
					//
					+ " (SELECT b.banco_codigo||'#'||a.agencia_codigo||'#'||cu.conta_usu_operacao||'#'||cu.conta_usu||'#'||cu.conta_usu_dv FROM projudi.conta_usu cu"
					+ " INNER JOIN projudi.agencia a ON a.id_agencia = cu.id_agencia"
					+ " INNER JOIN projudi.banco b ON b.id_banco = a.id_banco WHERE rownum = 1  AND cu.ativa = ? "
					+ " AND cu.id_usu = tab.idUsuario) as infoConta FROM ("
					//
					+ " SELECT idUsuario, nomeUsuario, cpfUsuario, escalaTipoMg,"
					+ " SUM(DECODE (mesAnoMandado,?,quantMandado, 0))  AS quantMandadoMesAnt1,"
					+ " SUM(DECODE (mesAnoMandado,?,quantMandado, 0))  AS quantMandadoMesAnt2,"
					+ " SUM(DECODE (mesAnoMandado,?,quantMandado, 0))  AS quantMandadoMesAnt3,"
					+ " SUM(DECODE (mesAnoMandado,?,quantMandado, 0))  AS quantMandadoMesAnt4,"
					+ " SUM(DECODE (mesAnoMandado,?,quantMandado, 0))  AS quantMandadoMesAnt5,"
					+ " SUM(DECODE (mesAnoMandado,?,quantMandado, 0))  AS quantMandadoMesAnt6,"
					+ " SUM(DECODE (mesAnoMandado,?,quantMandado, 0))  AS quantMandadoMesAnt7"
					//
					+ " FROM ((SELECT vw.idUsuario, vw.nomeUsuario, vw.cpfUsuario, vw.escalaTipoMg, vw.mesAnoMandado , COUNT(1) AS quantMandado"
					//
					+ " FROM (SELECT u.id_usu AS idUsuario, u.nome AS nomeUsuario, u.cpf AS cpfUsuario,"
					+ " et.escala_tipo_mg as escalaTipoMg, TO_CHAR(mj.data_dist,'yyyymm') AS mesAnoMandado"
					+ " FROM projudi.mand_jud mj"
					+ " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = mj.id_usu_serv_1"
					+ " INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
					+ " INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
					+ "	LEFT JOIN projudi.escala_mg e ON e.id_usuario = us.id_usu AND e.data_fim is null "
					+ " LEFT JOIN projudi.escala_tipo_mg et ON et.id_escala_tipo_mg = e.id_escala_tipo_mg"
					+ " INNER JOIN projudi.esc e ON e.id_esc = mj.id_esc"
					+ "	WHERE  TO_CHAR(mj.data_dist,'yyyymm')  IN (?,?,?,?,?,?,?) AND (mj.assistencia = ?)"
					+ " AND e.tipo_especial <> ? AND mj.id_mand_tipo <> ?");

			ps.adicionarLong(ServentiaTipoDt.CENTRAL_MANDADOS);
			ps.adicionarLong(UsuarioServentiaDt.ATIVO);
			ps.adicionarLong(ContaUsuarioDt.ATIVO);
			ps.adicionarString(arrayAnoMes[0]);
			ps.adicionarString(arrayAnoMes[1]);
			ps.adicionarString(arrayAnoMes[2]);
			ps.adicionarString(arrayAnoMes[3]);
			ps.adicionarString(arrayAnoMes[4]);
			ps.adicionarString(arrayAnoMes[5]);
			ps.adicionarString(arrayAnoMes[6]);

			ps.adicionarString(arrayAnoMes[0]);
			ps.adicionarString(arrayAnoMes[1]);
			ps.adicionarString(arrayAnoMes[2]);
			ps.adicionarString(arrayAnoMes[3]);
			ps.adicionarString(arrayAnoMes[4]);
			ps.adicionarString(arrayAnoMes[5]);
			ps.adicionarString(arrayAnoMes[6]);

			ps.adicionarLong(MandadoJudicialDt.SIM_ASSISTENCIA);
			ps.adicionarLong(EscalaDt.TIPO_ESPECIAL_ADHOC);
			ps.adicionarLong(MandadoTipoDt.EXECUCAO_FISCAL);

			if (idUsuario != 0) {
				sql.append(" AND u.id_usu = ?");
				ps.adicionarLong(idUsuario);
			}

			if (idServentia != 0) {
				sql.append(" AND us.id_serv = ?");
				ps.adicionarLong(idServentia);
			}

			sql.append(" ORDER BY u.nome, mesAnoMandado) vw"
					+ " GROUP BY vw.idUsuario, vw.nomeUsuario, vw.cpfUsuario, vw.escalaTipoMg, vw.mesAnoMandado))"
					+ " GROUP BY idUsuario, nomeUsuario, cpfUsuario, escalaTipoMg) tab"
					+ "	ORDER BY nomeComarca, nomeUsuario");

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setIdUsuario(rs.getInt("idUsuario"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setCpfUsuario(rs.getString("cpfUsuario"));
				obTemp.setNomeComarca(rs.getString("nomeComarca"));
				obTemp.setInfoConta(rs.getString("infoConta"));
				obTemp.setQuantMandadoMesAnt1(rs.getInt("quantMandadoMesAnt1"));
				obTemp.setQuantMandadoMesAnt2(rs.getInt("quantMandadoMesAnt2"));
				obTemp.setQuantMandadoMesAnt3(rs.getInt("quantMandadoMesAnt3"));
				obTemp.setQuantMandadoMesAnt4(rs.getInt("quantMandadoMesAnt4"));
				obTemp.setQuantMandadoMesAnt5(rs.getInt("quantMandadoMesAnt5"));
				obTemp.setQuantMandadoMesAnt6(rs.getInt("quantMandadoMesAnt6"));
				obTemp.setQuantMandadoMesAnt7(rs.getInt("quantMandadoMesAnt7"));
				if (rs.getString("escalaTipoMg") != null)
					obTemp.setEscalaTipo(rs.getString("escalaTipoMg"));
				else
					obTemp.setEscalaTipo("Normal");
				if (obTemp.getInfoConta() != null) {
					array = obTemp.getInfoConta().split("#");
					obTemp.setBanco(Integer.parseInt(array[0]));
					if (obTemp.getBanco() == BancoDt.CODIGO_CAIXA_ECONOMICA_FEDERAL)
						obTemp.setNomeBanco("CAIXA");
					else
						obTemp.setNomeBanco("OUTROS BANCOS");
					obTemp.setAgencia(Integer.parseInt(array[1]));
					obTemp.setContaOperacao(Integer.parseInt(array[2]));
					obTemp.setConta(Integer.parseInt(array[3]));
					obTemp.setContaDv(array[04]);
					listaMandado.add(obTemp);

					 System.out.println(rs.getString("nomeComarca") + "..." +
					 rs.getString("nomeUsuario") + "..."
					 + rs.getString("quantMandadoMesAnt1") + "..." +
					 rs.getString("quantMandadoMesAnt2") + "..."
					 + rs.getString("quantMandadoMesAnt3") + "..." +
					 rs.getString("quantMandadoMesAnt4") + "..."
					 + rs.getString("quantMandadoMesAnt5") + "..." +
					 rs.getString("quantMandadoMesAnt6") + "..."
					 + rs.getString("quantMandadoMesAnt7") + "...");

				} else if (tipoArquivo == 1) // sair sem conta bancaria so no relatorio. no texto para o banco não pode
												// sair.
					listaMandado.add(obTemp);
			}

		}

		finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return listaMandado;
	}
	
	////// fim tirar

}
