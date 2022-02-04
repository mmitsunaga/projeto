package br.gov.go.tj.projudi.ps;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.relatorios.DetalhesMovimentacaoDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoDistribuidoPorServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAcompanhamentoPonteiroDistribuicaoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAdvogadosProcessosOutrosEstadosDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAnaliticoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAudienciasDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioConclusoesPrimeiroGrauDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioConclusoesSegundoGrauDt;
//import br.gov.go.tj.projudi.dt.relatorios.RelatorioEstProImpressaoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioEstatisticaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioMaioresPromoventesPromovidosDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioPonteiroDistribuicaoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoSegundoGrauDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioRecursoRepetitivoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSituacaoGabineteDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioAudienciasComarcaDiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioAudienciasComarcaDiaServDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioAudienciasComarcaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioProcessosComarcaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioProcessosServentiaDt;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class RelatorioEstatisticaPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7174212793985002841L;

	public RelatorioEstatisticaPs(Connection conexao) {
		Conexao = conexao;
	}

	public RelatorioEstatisticaDt consultarDadosRelatorioEstatisticaServentia(RelatorioEstatisticaDt tipoRelatorio)
			throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		String id = tipoRelatorio.getId_Serventia();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT DISTINCT ";
		Sql += " 	 vme.ID_MOVI_TIPO, vme.MOVI_TIPO,  ";
		Sql += " 	(SELECT COUNT(*) ";
		Sql += " 		FROM projudi.VIEW_MOV_ESTATISTICA me ";
		Sql += " 		WHERE  me.ID_MOVI_TIPO = vme.ID_MOVI_TIPO AND me.ID_SERV = ?";
		ps.adicionarLong(id);
		if (!tipoRelatorio.getDataInicial().equalsIgnoreCase("")) {
			Sql += " AND me.DATA_REALIZACAO  >= ?";
			ps.adicionarDate(tipoRelatorio.getDataInicial());
		}
		if (!tipoRelatorio.getDataFinal().equalsIgnoreCase("")) {
			Sql += " AND me.DATA_REALIZACAO  < ?";
			ps.adicionarDate(tipoRelatorio.getDataFinal());
		}
		Sql += "	) AS QUANTIDADE_TOTAL ";
		Sql += " FROM ";
		Sql += "	VIEW_MOV_ESTATISTICA vme ";
		Sql += " WHERE ";
		Sql += "	vme.ID_SERV = ?";
		ps.adicionarLong(id);
		if (!tipoRelatorio.getDataInicial().equalsIgnoreCase("")) {
			Sql += " AND vme.DATA_REALIZACAO >= ?";
			ps.adicionarDate(tipoRelatorio.getDataInicial());
		}
		if (!tipoRelatorio.getDataFinal().equalsIgnoreCase("")) {
			Sql += " AND vme.DATA_REALIZACAO < ?";
			ps.adicionarDate(tipoRelatorio.getDataFinal());
		}
		Sql += " ORDER BY vme.MOVI_TIPO";
		try {
			rs1 = consultar(Sql, ps);
			tipoRelatorio.setListaDetalhesMovimentacao(null);
			while (rs1.next()) {
				DetalhesMovimentacaoDt detalhesMovimentacao = new DetalhesMovimentacaoDt();
				detalhesMovimentacao.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				detalhesMovimentacao.setQtdTotal(rs1.getString("QUANTIDADE_TOTAL"));

				tipoRelatorio.getListaDetalhesMovimentacao().add(detalhesMovimentacao);
			}
			// rs1.close();

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}
		return tipoRelatorio;
	}

	public RelatorioEstatisticaDt consultarDadosRelatorioEstatisticaUsuario(RelatorioEstatisticaDt tipoRelatorio)
			throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		String id = tipoRelatorio.getUsuario().getId();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = " SELECT DISTINCT ";
		Sql += " 	 vme.ID_MOVI_TIPO, vme.MOVI_TIPO,  ";
		Sql += " 	(SELECT COUNT(*) ";
		Sql += " 		FROM projudi.VIEW_MOV_ESTATISTICA me ";
		Sql += " 		WHERE  me.ID_MOVI_TIPO = vme.ID_MOVI_TIPO AND me.ID_USU = ?";
		ps.adicionarLong(id);
		if (!tipoRelatorio.getDataInicial().equalsIgnoreCase("")) {
			Sql += " AND me.DATA_REALIZACAO  >= ?";
			ps.adicionarDate(tipoRelatorio.getDataInicial());
		}
		if (!tipoRelatorio.getDataFinal().equalsIgnoreCase("")) {
			Sql += " AND me.DATA_REALIZACAO  < ?";
			ps.adicionarDate(tipoRelatorio.getDataFinal());
		}
		Sql += " ) AS QUANTIDADE_TOTAL ";
		Sql += " FROM ";
		Sql += "	projudi.VIEW_MOV_ESTATISTICA vme ";
		Sql += " WHERE ";
		Sql += "	vme.ID_USU = ?";
		ps.adicionarLong(id);
		if (!tipoRelatorio.getDataInicial().equalsIgnoreCase("")) {
			Sql += " AND vme.DATA_REALIZACAO  >= ?";
			ps.adicionarDate(tipoRelatorio.getDataInicial());
		}
		if (!tipoRelatorio.getDataFinal().equalsIgnoreCase("")) {
			Sql += " AND vme.DATA_REALIZACAO  < ?";
			ps.adicionarDate(tipoRelatorio.getDataFinal());
		}
		Sql += " ORDER BY vme.MOVI_TIPO";

		try {
			rs1 = consultar(Sql, ps);
			tipoRelatorio.setListaDetalhesMovimentacao(null);
			while (rs1.next()) {
				DetalhesMovimentacaoDt detalhesMovimentacao = new DetalhesMovimentacaoDt();
				detalhesMovimentacao.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				detalhesMovimentacao.setQtdTotal(rs1.getString("QUANTIDADE_TOTAL"));
				tipoRelatorio.getListaDetalhesMovimentacao().add(detalhesMovimentacao);
			}
			// rs1.close();

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}
		return tipoRelatorio;
	}

	public RelatorioEstatisticaDt consultarDadosRelatorioEstatisticaUsuarioServentia(
			RelatorioEstatisticaDt tipoRelatorio) throws Exception {

		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String id = tipoRelatorio.getUsuario().getId_UsuarioServentia();

		Sql = " SELECT DISTINCT ";
		Sql += " 	 vme.ID_MOVI_TIPO, vme.MOVI_TIPO,  ";
		Sql += " 	(SELECT COUNT(*) ";
		Sql += " 		FROM projudi.VIEW_MOV_ESTATISTICA me ";
		Sql += " 		WHERE  me.ID_MOVI_TIPO = vme.ID_MOVI_TIPO AND me.ID_USU_REALIZADOR = ?";
		ps.adicionarLong(id);
		if (!tipoRelatorio.getDataInicial().equalsIgnoreCase("")) {
			Sql += " AND me.DATA_REALIZACAO  >= ?";
			ps.adicionarDate(tipoRelatorio.getDataInicial());
		}
		if (!tipoRelatorio.getDataFinal().equalsIgnoreCase("")) {
			Sql += " AND me.DATA_REALIZACAO  < ?";
			ps.adicionarDate(tipoRelatorio.getDataFinal());
		}
		Sql += "	) AS QUANTIDADE_TOTAL ";
		Sql += " FROM ";
		Sql += "	projudi.VIEW_MOV_ESTATISTICA vme ";
		Sql += " WHERE ";
		Sql += "	vme.ID_USU_REALIZADOR = ?";
		ps.adicionarLong(id);
		if (!tipoRelatorio.getDataInicial().equalsIgnoreCase("")) {
			Sql += " AND vme.DATA_REALIZACAO  >= ?";
			ps.adicionarDate(tipoRelatorio.getDataInicial());
		}
		if (!tipoRelatorio.getDataFinal().equalsIgnoreCase("")) {
			Sql += " AND vme.DATA_REALIZACAO  < ?";
			ps.adicionarDate(tipoRelatorio.getDataFinal());
		}
		Sql += " ORDER BY vme.MOVI_TIPO";
		try {
			rs1 = consultar(Sql, ps);
			tipoRelatorio.setListaDetalhesMovimentacao(null);
			while (rs1.next()) {
				DetalhesMovimentacaoDt detalhesMovimentacao = new DetalhesMovimentacaoDt();
				detalhesMovimentacao.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
				detalhesMovimentacao.setQtdTotal(rs1.getString("QUANTIDADE_TOTAL"));
				tipoRelatorio.getListaDetalhesMovimentacao().add(detalhesMovimentacao);
			}
			// rs1.close();

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}

		return tipoRelatorio;
	}

	/**
	 * Método para geração do Relatório Sumário de Processos.
	 * 
	 * param anoInicial - ano inicial selecionado param mesInicial - mes inicial
	 * selecionado param anoFinal - ano final selecionado param mesFinal - mes final
	 * selecionado param idComarca - ID da Comarca selecionada param idServentia -
	 * ID da Serventia selecionada param tipoProcesso - tipo de processo selecionado
	 * param agrupamento - tipo de agrupamento selecionado param tipoArquivo - tipo
	 * de arquivo solicitado pelo usuário return lista de processos throws Exception
	 * author hmgodinho
	 */
	public List relSumarioProcessos(String anoInicial, String mesInicial, String anoFinal, String mesFinal,
			String idComarca, String idServentia, String tipoProcesso, String agrupamento, String tipoArquivo)
			throws Exception {
		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		int agrupamentoRelatorio = Funcoes.StringToInt(agrupamento);
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" SELECT vep.ANO, vep.MES, vep.COMARCA");

		if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA) {
			sql.append(", vep.SERV");
		}

		sql.append(", vep.PROC_TIPO, SUM(vep.QUANTIDADE) AS QUANTIDADE FROM projudi.VIEW_EST_PROC vep WHERE");

		// Há diferença na cláusula WHERE da consulta se os anos INICIAL e FINAL forem
		// diferentes
		if (anoInicial.equals(anoFinal)) {
			sql.append("(vep.ANO = ? AND vep.MES >= ? AND vep.MES <= ?)");
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(mesFinal);
		} else {
			sql.append("((vep.ANO = ? AND vep.MES >= ?) OR (vep.ANO = ? AND vep.MES <= ?))");
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(anoFinal);
			ps.adicionarLong(mesFinal);
		}

		if (idComarca != null && !idComarca.equals("") && (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA)) {
			sql.append(" AND ");
			sql.append("vep.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
		}

		if (idServentia != null && !idServentia.equals("")
				&& (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA)) {
			sql.append(" AND ");
			sql.append("vep.ID_SERV = ?");
			ps.adicionarLong(idServentia);
		}

		if (tipoProcesso != null && !tipoProcesso.equals("")) {
			sql.append(" AND ");
			sql.append(" vep.ID_PROC_TIPO = ?");
			ps.adicionarLong(tipoProcesso);
		}

		sql.append(" GROUP BY vep.ANO, vep.MES, vep.COMARCA");
		if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA) {
			sql.append(", vep.SERV");
		}
		sql.append(", vep.PROC_TIPO");

		sql.append(" ORDER BY vep.ANO, vep.MES, vep.COMARCA");
		if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA) {
			sql.append(", vep.SERV");
		}
		sql.append(", vep.PROC_TIPO");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioSumarioDt obTemp = new RelatorioSumarioDt();
				obTemp.setAno(rs.getInt("ANO"));
				// Se o tipo de Arquivo for PDF (tipo 1), deve retornar o nome
				// do Mês. Se for TXT (tipo 2) ou qualquer outro tipo,
				// de arquivo ou não informar tipo, deve retornar o número
				// equivalente do mês.
				if (tipoArquivo != null && !tipoArquivo.equals("") && tipoArquivo.equals("1")) {
					obTemp.setMes(Funcoes.identificarNomeMes(rs.getInt("MES")));
				} else {
					obTemp.setMes(rs.getString("MES"));
				}

				if (agrupamento.equals(String.valueOf(RelatorioSumarioDt.COMARCA_SERVENTIA))) {
					obTemp.setServentiaRelatorio(rs.getString("SERV"));
				}

				obTemp.setComarca(rs.getString("COMARCA"));
				obTemp.setProcessoRelatorio(rs.getString("PROC_TIPO"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método para geração do Relatório Sumário de Produtividade para mês fechado selecionado.
	 * 
	 * @param anoInicial - ano inicial selecionado 
	 * @param mesInicial - mes inicial selecionado 
	 * @param anoFinal - ano final selecionado 
	 * @param mesFinal - mes final selecionado 
	 * @param idComarca - ID da Comarca selecionada 
	 * @param idServentia - ID da Serventia selecionada 
	 * @param idGrupo - ID do grupo de usuários selecionado 
	 * @param idUsuario - ID do usuário selecionado 
	 * @param tipoPendencia - tipo de pendência selecionada 
	 * @param agrupamento - agrupamento selecionado
	 * @param tipoArquivo - tipo de arquivo solicitado pelo usuário return lista de produtividade 
	 * @throws Exception 
	 * @author hmgodinho
	 */
	public List relSumarioProdutividadeMesFechado(String anoInicial, String mesInicial, String anoFinal, String mesFinal,
			String idComarca, String idServentia, String idGrupo, String idUsuario, String tipoPendencia,
			String agrupamento, String tipoArquivo) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		int agrupamentoRelatorio = Funcoes.StringToInt(agrupamento);
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" SELECT vep.ANO, vep.MES, vep.SISTEMA, vep.COMARCA");

		if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", vep.SERV");
		}

		if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", vep.NOME");
		}

		sql.append(", vep.EST_PROD_ITEM, SUM(vep.QUANTIDADE) AS QUANTIDADE FROM projudi.VIEW_EST_PROD vep ");

		sql.append(" WHERE ");
		// ------------------------------------------------------------------------------------------
		/*
		 * Há diferença na cláusula WHERE da consulta se os anos INICIAL e FINAL forem
		 * diferentes
		 */
		if (anoInicial.equals(anoFinal)) {
			sql.append("(vep.ANO = ? AND vep.MES >= ? AND vep.MES <= ?)");
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(mesFinal);
		} else {
			sql.append("((vep.ANO = ? AND vep.MES >= ?) OR (vep.ANO = ? AND vep.MES <= ?))");
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(anoFinal);
			ps.adicionarLong(mesFinal);
		}

		if (idComarca != null && !idComarca.equals("")
				&& (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(" AND ");
			sql.append("vep.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
		}

		if (idServentia != null && !idServentia.equals("")
				&& (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(" AND ");
			sql.append("vep.ID_SERV = ?");
			ps.adicionarLong(idServentia);
		}

		if (idUsuario != null && !idUsuario.equals("")
				&& agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(" AND ");
			sql.append(" vep.ID_USU = ?  AND vep.ID_USU IN (SELECT us.ID_USU FROM projudi.USU_SERV_GRUPO usg "
					+ " INNER JOIN projudi.USU_SERV us on us.ID_USU_SERV = usg.ID_USU_SERV "
					+ " WHERE us.ID_SERV = ?)");
			ps.adicionarLong(idUsuario);
			ps.adicionarLong(idServentia);
		}

		if (tipoPendencia != null && !tipoPendencia.equals("")
				&& (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(" AND ");
			sql.append(" vep.ID_EST_PROD_ITEM = ?");
			ps.adicionarString(tipoPendencia);
		}

		// ------------------------------------------------------------------------------------------
		sql.append(" GROUP BY vep.ANO, vep.MES, vep.SISTEMA, vep.COMARCA");

		if (idServentia != null && (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(", vep.SERV");
		}

		if (idUsuario != null && agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", vep.NOME");
		}
		sql.append(", vep.EST_PROD_ITEM");

		// ------------------------------------------------------------------------------------------
		sql.append(" ORDER BY vep.ANO, vep.MES, vep.COMARCA");

		if (idServentia != null && (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(", vep.SERV");
		}

		if (idUsuario != null && agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", vep.NOME");
		}
		sql.append(", vep.EST_PROD_ITEM");

		// ------------------------------------------------------------------------------------------

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioSumarioDt obTemp = new RelatorioSumarioDt();
				obTemp.setAno(rs.getInt("ANO"));
				// Se o tipo de Arquivo for PDF (tipo 1), deve retornar o nome
				// do Mês. Se for TXT (tipo 2) ou qualquer outro tipo,
				// de arquivo ou não informar tipo, deve retornar o número
				// equivalente do mês.
				if (tipoArquivo != null && !tipoArquivo.equals("") && tipoArquivo.equals("1")) {
					obTemp.setMes(Funcoes.identificarNomeMes(rs.getInt("MES")));
				} else {
					obTemp.setMes(rs.getString("MES"));
				}

				obTemp.setComarca(rs.getString("COMARCA"));

				switch (Funcoes.StringToInt(agrupamento)) {
				case RelatorioSumarioDt.COMARCA:
					break;
				case RelatorioSumarioDt.COMARCA_SERVENTIA:
					obTemp.setServentiaRelatorio(rs.getString("SERV"));
					break;
				case RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO:
					obTemp.setServentiaRelatorio(rs.getString("SERV"));
					obTemp.setUsuarioRelatorio(rs.getString("NOME"));
					break;
				}

				obTemp.setItemEstatisticaRelatorio(rs.getString("EST_PROD_ITEM"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}
	
	/**
	 * Método para geração do Relatório Sumário de Produtividade com datas inicial e final informadas.
	 * 
	 * @param dataInicial - data inicial selecionado 
	 * @param dataFinal - data final selecionado 
	 * @param idComarca - ID da Comarca selecionada 
	 * @param idServentia - ID da Serventia selecionada 
	 * @param idGrupo - ID do grupo de usuários selecionado 
	 * @param idUsuario - ID do usuário selecionado 
	 * @param tipoPendencia - tipo de pendência selecionada 
	 * @param agrupamento - agrupamento selecionado
	 * @param tipoArquivo - tipo de arquivo solicitado pelo usuário return lista de produtividade 
	 * @throws Exception 
	 * @author hmgodinho
	 */
	public List relSumarioProdutividadeDataInicialFinal(String dataInicial, String dataFinal, String idComarca, String idServentia, String idGrupo, String idUsuario, String tipoPendencia, String agrupamento, String tipoArquivo) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		int agrupamentoRelatorio = Funcoes.StringToInt(agrupamento);
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append(" SELECT c.comarca ");

		if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", s.serv");
		}

		if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", u.NOME");
		}

		sql.append(", mt.movi_tipo AS EST_PROD_ITEM, COUNT(1) AS QUANTIDADE "
				+ "		FROM ( ");
		sql.append("		SELECT s.id_serv, u.id_usu, m.id_movi_tipo "
				+ "				FROM projudi.movi M "
				+ "			INNER JOIN projudi.usu_serv us ON m.id_usu_realizador = us.id_usu_serv "
				+ "			INNER JOIN projudi.usu u ON u.id_usu = us.id_usu "
				+ "			INNER JOIN projudi.serv s ON s.id_serv = us.id_serv "
				+ "				WHERE m.data_realizacao BETWEEN ? AND ? ");
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
			      
		if (idComarca != null && !idComarca.equals("")
				&& (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(" 			AND s.id_comarca = ?");
			ps.adicionarLong(idComarca);
		}
		
		if (idServentia != null && !idServentia.equals("")
				&& (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(" 			AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		}

		if (idUsuario != null && !idUsuario.equals("")
				&& agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(" 			AND us.id_usu = ?");
			ps.adicionarLong(idUsuario);
		}

		if (tipoPendencia != null && !tipoPendencia.equals("")) {
			sql.append(" 			AND m.id_movi_tipo = ?");
			ps.adicionarString(tipoPendencia);
		}
		
		sql.append(" 		) tab "
				+ " INNER JOIN projudi.movi_tipo mt ON mt.id_movi_tipo = tab.id_movi_tipo "
				+ " INNER JOIN projudi.serv s ON s.id_serv = tab.id_serv "
				+ " INNER JOIN projudi.comarca c ON c.id_comarca = s.id_comarca "
				+ " INNER JOIN projudi.usu u ON u.id_usu = tab.id_usu ");
	

		sql.append(" GROUP BY comarca ");
		
		if (idServentia != null && (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(", serv");
		}

		if (idUsuario != null && agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", nome");
		}
		sql.append(", movi_tipo");

		// ------------------------------------------------------------------------------------------
		sql.append(" ORDER BY comarca");

		if (idServentia != null && (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(", serv");
		}

		if (idUsuario != null && agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", nome");
		}
		sql.append(", movi_tipo");

		// ------------------------------------------------------------------------------------------

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioSumarioDt obTemp = new RelatorioSumarioDt();
				obTemp.setComarca(rs.getString("COMARCA"));

				switch (Funcoes.StringToInt(agrupamento)) {
				case RelatorioSumarioDt.COMARCA:
					break;
				case RelatorioSumarioDt.COMARCA_SERVENTIA:
					obTemp.setServentiaRelatorio(rs.getString("SERV"));
					break;
				case RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO:
					obTemp.setServentiaRelatorio(rs.getString("SERV"));
					obTemp.setUsuarioRelatorio(rs.getString("NOME"));
					break;
				}

				obTemp.setItemEstatisticaRelatorio(rs.getString("EST_PROD_ITEM"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Gera a estatística de produtividade dos assessores COM FLUXO de magistrados.
	 * @param dataInicial - data inicial da consulta
	 * @param dataFinal - data final da consulta
	 * @param idServentia - id da serventia/gabinete do magistrado
	 * @return lista de produtividade dos assistentes
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List relSumarioProdutividadeAssessoresFluxo(String dataInicial, String dataFinal, String idServentia)
			throws Exception {

		List listaProdutividade = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" SELECT u.nome, pt.pend_tipo, COUNT(*) AS quantidade " +
					"  FROM ( " +
						//Esse select interno serve para agrupar as id_pend. Motivo: pode acontecer de um o usuário trabalhar 
						//duas ou mais vezes na mesma pendência e, nestes casos, só será considerado como um trabalho.
					"    SELECT prh.id_serv_cargo, prh.id_pend " +
					"       FROM projudi.pend_resp_hist_final prh " +
					"       INNER JOIN projudi.serv_cargo sc ON sc.id_serv_cargo = prh.id_serv_cargo " +
					"    	WHERE sc.id_serv = ? ");
		ps.adicionarLong(idServentia);
		sql.append(" 			AND prh.data_fim BETWEEN ? AND ? ");
		ps.adicionarDateTime(Funcoes.calculePrimeraHora(Funcoes.StringToDate(dataInicial)));
		ps.adicionarDateTime(Funcoes.calculeUltimaHora(Funcoes.StringToDate(dataFinal)));

		sql.append(" 		GROUP BY prh.id_serv_cargo, prh.id_pend ) tab " +
					" 	INNER JOIN projudi.serv_cargo sc ON sc.id_serv_cargo = tab.id_serv_cargo " +
					" 	LEFT JOIN projudi.USU_SERV_GRUPO usg ON sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO" +
					" 	LEFT JOIN projudi.GRUPO g ON usg.ID_GRUPO = g.ID_GRUPO " +
					" 	LEFT JOIN projudi.USU_SERV us ON usg.ID_USU_SERV = us.ID_USU_SERV " +
					" 	LEFT JOIN projudi.USU u ON us.id_usu = u.id_usu " +
					" 	INNER JOIN projudi.pend_final pe ON pe.id_pend = tab.id_pend " +
					" 	INNER JOIN projudi.pend_tipo pt ON pt.id_pend_tipo = pe.id_pend_tipo " +
					" GROUP BY u.nome, pt.pend_tipo " +
					" ORDER BY u.nome, pt.pend_tipo");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioSumarioDt obTemp = new RelatorioSumarioDt();
				obTemp.setUsuarioRelatorio(rs.getString("NOME"));
				obTemp.setItemEstatisticaRelatorio(rs.getString("PEND_TIPO"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProdutividade.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProdutividade;
	}
	
	/**
	 * Gera a estatística de produtividade dos assessores de magistrados.
	 * @param dataInicial - data inicial da consulta
	 * @param dataFinal - data final da consulta
	 * @param idServentia - id da serventia/gabinete do magistrado
	 * @param grupoCodigo - id do grupo ao qual pertence o magistrado
	 * @param idUsuarioServentiaChefe - id do usuário serventia chefe dos assessores
	 * @return lista de produtividade dos assessores
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List relSumarioProdutividadeAssessores(String dataInicial, String dataFinal, String idServentia, int grupoCodigo, String idUsuarioServentiaChefe) throws Exception {

		List listaProdutividade = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" SELECT u.nome, pt.pend_tipo, count(*) AS quantidade FROM ( "
				+ "		SELECT p.id_pend, p.id_pend_tipo, u.id_usu "
				+ "			FROM projudi.pend_final p "
				+ "			INNER JOIN projudi.pend_final_resp pr ON pr.id_pend = p.id_pend "
				+ "			INNER JOIN projudi.usu_serv us ON us.id_usu_serv = pr.id_usu_resp "
				+ "			INNER JOIN projudi.usu_serv_grupo usg ON usg.id_usu_serv = us.id_usu_serv "
				+ "			INNER JOIN projudi.grupo g ON g.id_grupo = usg.id_grupo "
				+ "			INNER JOIN projudi.usu u ON u.id_usu = us.id_usu "
				+ "			INNER JOIN projudi.pend_tipo pt ON pt.id_pend_tipo = p.id_pend_tipo ");

		sql.append(" 			WHERE p.data_fim BETWEEN ? AND ? ");
		ps.adicionarDateTime(Funcoes.calculePrimeraHora(Funcoes.StringToDate(dataInicial)));
		ps.adicionarDateTime(Funcoes.calculeUltimaHora(Funcoes.StringToDate(dataFinal)));

		sql.append(" 				AND us.ID_SERV = ? ");
		ps.adicionarLong(idServentia);

		sql.append(" 				AND g.grupo_codigo = ? ");
		ps.adicionarLong(grupoCodigo);
		
		sql.append(" 				AND us.id_usu_serv_chefe = ? ");
		ps.adicionarLong(idUsuarioServentiaChefe);
		
		sql.append(" 				AND p.id_pend_status <> ? ");
		ps.adicionarLong(PendenciaStatusDt.ID_DESCARTADA);
		
		//Pendência de ementa não deve ser contabilizada, pois é usada no segundo grau apenas como atributo para funcionamento do sistema
		sql.append(" 				AND pt.pend_tipo_codigo <> ? ");
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		
		sql.append(" 	) tab "
				+ " INNER JOIN projudi.usu u ON u.id_usu = tab.id_usu "
				+ " INNER JOIN projudi.pend_final pf ON pf.id_pend = tab.id_pend "
				+ " INNER JOIN projudi.pend_tipo pt ON pf.id_pend_tipo = pt.id_pend_tipo " 
				+ " GROUP BY u.nome, pt.pend_tipo " 
				+ " ORDER BY u.nome, pt.pend_tipo");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioSumarioDt obTemp = new RelatorioSumarioDt();
				obTemp.setUsuarioRelatorio(rs.getString("NOME"));
				obTemp.setItemEstatisticaRelatorio(rs.getString("PEND_TIPO"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProdutividade.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProdutividade;
	}
	
	
	/**
	 * Gera a estatística de produtividade dos assessores de magistrados.
	 * @param dataInicial - data inicial da consulta
	 * @param dataFinal - data final da consulta
	 * @param idServentia - id da serventia/gabinete do magistrado
	 * @param grupoCodigo - id do grupo ao qual pertence o magistrado
	 * @return lista de produtividade dos assistentes de gabinete
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List relSumarioProdutividadeAssistenteGabinete(String dataInicial, String dataFinal, String idServentia, int grupoCodigo) throws Exception {

		List listaProdutividade = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append(" SELECT u.nome, pt.pend_tipo, count(*) AS quantidade FROM ( "
				+ "		SELECT p.id_pend, p.id_pend_tipo, u.id_usu "
				+ "			FROM projudi.pend_final p "
				+ "			INNER JOIN projudi.pend_final_resp pr ON pr.id_pend = p.id_pend "
				+ "			INNER JOIN projudi.serv_cargo sc ON sc.id_serv_cargo = pr.id_serv_cargo "		
				+ "			INNER JOIN projudi.usu_serv_grupo usg ON usg.id_usu_serv_grupo = sc.id_usu_serv_grupo "
				+ "			INNER JOIN projudi.usu_serv us ON us.id_usu_serv = usg.id_usu_serv "
				+ "			INNER JOIN projudi.grupo g ON g.id_grupo = usg.id_grupo "
				+ "			INNER JOIN projudi.usu u ON u.id_usu = us.id_usu "
				+ "			INNER JOIN projudi.pend_tipo pt ON pt.id_pend_tipo = p.id_pend_tipo ");

		sql.append(" 			WHERE p.data_fim BETWEEN ? AND ? ");
		ps.adicionarDateTime(Funcoes.calculePrimeraHora(Funcoes.StringToDate(dataInicial)));
		ps.adicionarDateTime(Funcoes.calculeUltimaHora(Funcoes.StringToDate(dataFinal)));

		sql.append(" 				AND us.ID_SERV = ? ");
		ps.adicionarLong(idServentia);

		sql.append(" 				AND g.grupo_codigo = ? ");
		ps.adicionarLong(grupoCodigo);
		
		sql.append(" 				AND p.id_pend_status <> ? ");
		ps.adicionarLong(PendenciaStatusDt.ID_DESCARTADA);
		
		//Pendência de ementa não deve ser contabilizada, pois é usada no segundo grau apenas como atributo para funcionamento do sistema
		sql.append(" 				AND pt.pend_tipo_codigo <> ? ");
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		
		sql.append(" 	) tab "
				+ " INNER JOIN projudi.usu u ON u.id_usu = tab.id_usu "
				+ " INNER JOIN projudi.pend_final pf ON pf.id_pend = tab.id_pend "
				+ " INNER JOIN projudi.pend_tipo pt ON pf.id_pend_tipo = pt.id_pend_tipo " 
				+ " GROUP BY u.nome, pt.pend_tipo " 
				+ " ORDER BY u.nome, pt.pend_tipo");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioSumarioDt obTemp = new RelatorioSumarioDt();
				obTemp.setUsuarioRelatorio(rs.getString("NOME"));
				obTemp.setItemEstatisticaRelatorio(rs.getString("PEND_TIPO"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProdutividade.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProdutividade;
	}
	

	/**
	 * Método para geração do Relatório Sumário de Pendências.
	 * 
	 * param anoInicial - ano inicial selecionado param mesInicial - mes inicial
	 * selecionado param anoFinal - ano final selecionado param mesFinal - mes final
	 * selecionado param idServentia - ID da Serventia selecionada param idUsuario -
	 * ID do usuário selecionado param pendenciaTipo - tipo de pendência selecionada
	 * return lista de pendências throws Exception author asrocha
	 */
	public List relSumarioPendencia(String anoInicial, String mesInicial, String anoFinal, String mesFinal,
			String idServentia, String idUsuario, String idComarca, int agrupamentoRelatorio, String idSistema,
			String tipoArquivo, String idPendenciaTipo) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		// ------------------------------------------------------------------------------------------
		sql.append(" SELECT ANO, MES, SISTEMA, COMARCA");

		if (idServentia != null && (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(", SERV");
		}

		if (idUsuario != null && agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", NOME");
		}

		sql.append(", PEND_TIPO, SUM(QUANTIDADE) AS QUANTIDADE FROM projudi.VIEW_EST_PEND WHERE ");

		// ------------------------------------------------------------------------------------------
		/*
		 * Há diferença na cláusula WHERE da consulta se os anos INICIAL e FINAL forem
		 * diferentes
		 */
		if (anoInicial.equals(anoFinal)) {
			sql.append("(ANO = ? AND MES >= ? AND MES <= ?)");
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(mesFinal);
		} else {
			sql.append("((ANO = ? AND MES >= ?) OR (ANO = ? AND MES <= ?))");
			ps.adicionarLong(anoInicial);
			ps.adicionarLong(mesInicial);
			ps.adicionarLong(anoFinal);
			ps.adicionarLong(mesFinal);
		}

		if (idSistema != null && !idSistema.equals("")) {
			sql.append(" AND ");
			sql.append("ID_SISTEMA = ?");
			ps.adicionarLong(idSistema);
		}

		if (idComarca != null && !idComarca.equals("")
				&& (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(" AND ");
			sql.append("ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
		}

		if (idServentia != null && !idServentia.equals("")
				&& (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(" AND ");
			sql.append("ID_SERV = ?");
			ps.adicionarLong(idServentia);
		}

		if (idUsuario != null && !idUsuario.equals("")
				&& agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(" AND ");
			sql.append("ID_USU = ?");
			ps.adicionarLong(idUsuario);
		}

		if (idPendenciaTipo != null && !idPendenciaTipo.equals("")) {
			sql.append(" AND ");
			sql.append(" ID_PEND_TIPO = ?");
			ps.adicionarLong(idPendenciaTipo);
		}

		// ------------------------------------------------------------------------------------------
		sql.append(" GROUP BY ANO, MES, SISTEMA, COMARCA");

		if (idServentia != null && (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(", SERV");
		}

		if (idUsuario != null && agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", NOME");
		}
		sql.append(", PEND_TIPO");

		// ------------------------------------------------------------------------------------------
		sql.append(" ORDER BY ANO, MES, SISTEMA, COMARCA");

		if (idServentia != null && (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
				|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)) {
			sql.append(", SERV");
		}

		if (idUsuario != null && agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO) {
			sql.append(", NOME");
		}
		sql.append(", PEND_TIPO");

		// ------------------------------------------------------------------------------------------
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioSumarioDt obTemp = new RelatorioSumarioDt();
				obTemp.setAno(rs.getInt("ANO"));
				/*
				 * Se o tipo de Arquivo for PDF (tipo 1), deve retornar o nome do Mês. Se for
				 * TXT (tipo 2) ou qualquer outro tipo, de arquivo ou não informar tipo, deve
				 * retornar o número equivalente do mês.
				 */
				if (tipoArquivo != null && !tipoArquivo.equals("") && tipoArquivo.equals("1")) {
					obTemp.setMes(Funcoes.identificarNomeMes(rs.getInt("MES")));
				} else {
					obTemp.setMes(rs.getString("MES"));
				}
				obTemp.setSistema(rs.getString("SISTEMA"));
				obTemp.setComarca(rs.getString("COMARCA"));
				if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA
						|| agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)
					obTemp.setServentiaRelatorio(rs.getString("SERV"));
				if (agrupamentoRelatorio == RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO)
					obTemp.setUsuarioRelatorio(rs.getString("NOME"));
				obTemp.setPendenciaRelatorio(rs.getString("PEND_TIPO"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que realiza a consulta dos RelatorioEstPro cadastrados.
	 * 
	 * param anoInicial - ano inicial selecionado param mesInicial - mes inicial
	 * selecionado param anoFinal - ano final selecionado param mesFinal - mes final
	 * selecionado param idComarca - ID da Comarca selecionada param idServentia -
	 * ID da Serventia selecionada param idGrupo - ID do grupo de usuários
	 * selecionado param idUsuario - ID do usuário selecionado param
	 * listaEstatisticas - lista de Estatísticas de Produtividade selecionadas
	 * dentro do RelatorioEstPro param agrupamento - agrupamento selecionado param
	 * tipoArquivo - tipo de arquivo solicitado pelo usuário return lista de
	 * produtividade throws Exception author hmgodinho
	 */
	//
	// public List imprimirRelatorioEstPro(String anoInicial, String mesInicial,
	// String anoFinal, String mesFinal, String idComarca, String idServentia,
	// String idGrupo, String idUsuario, List listaEstatisticas, String
	// agrupamento,String tipoArquivo) throws Exception {
	//
	// List listaProcessos = new ArrayList();
	// StringBuffer sql = new StringBuffer();
	// String sqlGroupBy = "";
	// String sqlOrderBy = "";
	// ResultSetTJGO rs = null;
	// PreparedStatementTJGO ps = new PreparedStatementTJGO();
	//
	// switch (Funcoes.StringToInt(agrupamento)) {
	// case RelatorioSumarioDt.COMARCA:
	// sql.append(" SELECT vep.ANO, vep.MES, vep.COMARCA, vep.EST_PROD_ITEM,
	// SUM(vep.QUANTIDADE) AS QUANTIDADE " +
	// "FROM projudi.VIEW_EST_PROD vep " +
	// "LEFT JOIN projudi.USU_SERV us on vep.ID_SERV = us.ID_SERV AND vep.ID_USU =
	// us.ID_USU " +
	// "LEFT JOIN projudi.USU_SERV_GRUPO usg on us.ID_USU_SERV = usg.ID_USU_SERV " +
	// "LEFT JOIN projudi.GRUPO g on usg.ID_GRUPO = g.ID_GRUPO");
	// sqlGroupBy = " GROUP BY ANO, MES, COMARCA, EST_PROD_ITEM ";
	// sqlOrderBy = " ORDER BY ANO, MES, COMARCA, EST_PROD_ITEM";
	// break;
	// case RelatorioSumarioDt.COMARCA_SERVENTIA:
	// sql.append(" SELECT vep.ANO, vep.MES, vep.COMARCA, vep.SERV,
	// vep.EST_PROD_ITEM, SUM(vep.QUANTIDADE) AS QUANTIDADE " +
	// "FROM projudi.VIEW_EST_PROD vep LEFT JOIN projudi.USU_SERV us on vep.ID_SERV
	// = us.ID_SERV AND vep.ID_USU = us.ID_USU " +
	// "LEFT JOIN projudi.USU_SERV_GRUPO usg on us.ID_USU_SERV = usg.ID_USU_SERV " +
	// "LEFT JOIN projudi.GRUPO g on usg.ID_GRUPO = g.ID_GRUPO");
	// sqlGroupBy = " GROUP BY vep.ANO, vep.MES, vep.COMARCA, vep.ID_SERV,
	// vep.EST_PROD_ITEM, vep.COMARCA, vep.SERV";
	// sqlOrderBy = " ORDER BY vep.ANO, vep.MES, vep.COMARCA, vep.SERV,
	// vep.EST_PROD_ITEM";
	// break;
	// case RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO:
	// sql.append(" SELECT vep.ANO, vep.MES, vep.COMARCA, vep.SERV, g.GRUPO,
	// vep.NOME, vep.EST_PROD_ITEM, SUM(vep.QUANTIDADE) AS QUANTIDADE ");
	// sqlGroupBy = " GROUP BY vep.ANO, vep.MES, vep.COMARCA, vep.ID_SERV,
	// vep.ID_USU, vep.EST_PROD_ITEM, vep.COMARCA, vep.SERV, g.GRUPO, vep.NOME";
	// sqlOrderBy = " ORDER BY vep.ANO, vep.MES, vep.COMARCA, vep.SERV, vep.NOME,
	// vep.EST_PROD_ITEM";
	// sql.append(" FROM projudi.VIEW_EST_PROD vep " +
	// "LEFT JOIN projudi.USU_SERV us on vep.ID_SERV = us.ID_SERV AND vep.ID_USU =
	// us.ID_USU " +
	// "LEFT JOIN projudi.USU_SERV_GRUPO usg on us.ID_USU_SERV = usg.ID_USU_SERV " +
	// "LEFT JOIN projudi.GRUPO g on usg.ID_GRUPO = g.ID_GRUPO");
	// break;
	// }
	//
	// boolean clausulaWhere = true;
	// boolean clausulaAnd = false;
	// if (anoInicial != null && !anoInicial.equals("") && mesInicial != null &&
	// !mesInicial.equals("") && anoFinal != null && !anoFinal.equals("") &&
	// mesFinal != null && !mesFinal.equals("")) {
	// if (clausulaWhere) {
	// sql.append(" WHERE ");
	// clausulaWhere = false;
	// }
	// // Há diferença na cláusula WHERE da consulta se os anos INICIAL e FINAL
	// forem diferentes.
	// if (anoInicial.equals(anoFinal)) {
	// sql.append("(vep.ANO = ? AND vep.MES >= ? AND vep.MES <= ?)");
	// ps.adicionarLong(anoInicial);
	// ps.adicionarLong(mesInicial);
	// ps.adicionarLong(mesFinal);
	// } else {
	// sql.append("((vep.ANO = ? AND vep.MES >= ? ) OR (vep.ANO = ? AND vep.MES <=
	// ?))");
	// ps.adicionarLong(anoInicial);
	// ps.adicionarLong(mesInicial);
	// ps.adicionarLong(anoFinal);
	// ps.adicionarLong(mesFinal);
	// }
	//
	// clausulaAnd = true;
	// }
	//
	// if (idServentia != null && !idServentia.equals("")) {
	// if (clausulaWhere) {
	// sql.append(" WHERE ");
	// clausulaWhere = false;
	// }
	// if (clausulaAnd) {
	// sql.append(" AND ");
	// }
	// sql.append(" vep.ID_SERV = ?");
	// ps.adicionarLong(idServentia);
	// clausulaAnd = true;
	// }
	// if (idComarca != null && !idComarca.equals("")) {
	// if (clausulaWhere) {
	// sql.append(" WHERE ");
	// clausulaWhere = false;
	// }
	// if (clausulaAnd) {
	// sql.append(" AND ");
	// }
	// sql.append(" vep.ID_COMARCA = ?");
	// ps.adicionarLong(idComarca);
	// clausulaAnd = true;
	// }
	// if (idGrupo != null && !idGrupo.equals("")) {
	// if (clausulaWhere) {
	// sql.append(" WHERE ");
	// clausulaWhere = false;
	// }
	// if (clausulaAnd) {
	// sql.append(" AND ");
	// }
	// sql.append(" g.ID_GRUPO = ?");
	// ps.adicionarLong(idGrupo);
	// clausulaAnd = true;
	// }
	// if (idUsuario != null && !idUsuario.equals("") && idServentia != null &&
	// !idServentia.equals("")) {
	// if (clausulaWhere) {
	// sql.append(" WHERE ");
	// clausulaWhere = false;
	// }
	// if (clausulaAnd) {
	// sql.append(" AND ");
	// }
	// sql.append(" vep.ID_USU = ? AND vep.ID_USU IN (SELECT us.ID_USU FROM
	// projudi.USU_SERV_GRUPO usg " +
	// " INNER JOIN projudi.USU_SERV us on us.ID_USU_SERV = usg.ID_USU_SERV " +
	// " WHERE us.ID_SERV = ?)");
	// ps.adicionarLong(idUsuario);
	// ps.adicionarLong(idServentia);
	// clausulaAnd = true;
	// }
	//
	// if (!listaEstatisticas.isEmpty()) {
	// boolean parentesesFinal = false;
	// if (clausulaWhere) {
	// sql.append(" WHERE ");
	// clausulaWhere = false;
	// }
	// if (clausulaAnd) {
	// sql.append(" AND (");
	// parentesesFinal = true;
	// }
	// for (int i = 0; i < listaEstatisticas.size(); i++) {
	// if(i > 0) {
	// sql.append(" OR ");
	// }
	// sql.append(" vep.ID_EST_PROD_ITEM = ?");
	// ps.adicionarLong(String.valueOf(listaEstatisticas.get(i)));
	// }
	// if (parentesesFinal) {
	// sql.append(") ");
	// }
	// }
	//
	// try{
	// rs = consultar(sql.toString() + sqlGroupBy + sqlOrderBy, ps);
	// while (rs.next()) {
	// RelatorioEstProImpressaoDt obTemp = new RelatorioEstProImpressaoDt();
	// obTemp.setAno(rs.getString("ANO"));
	//
	// if (rs.getInt("MES") < 10) {
	// obTemp.setMes("0" + rs.getString("MES"));
	// } else {
	// obTemp.setMes(rs.getString("MES"));
	// }
	//
	// if(rs.getString("COMARCA") != null) {
	// obTemp.setComarcaRelatorio(rs.getString("COMARCA"));
	// } else {
	// obTemp.setComarcaRelatorio("");
	// }
	//
	// switch (Funcoes.StringToInt(agrupamento)) {
	// case RelatorioSumarioDt.COMARCA:
	// break;
	// case RelatorioSumarioDt.COMARCA_SERVENTIA:
	// if(rs.getString("SERV") != null) {
	// obTemp.setServentiaRelatorio(rs.getString("SERV"));
	// } else {
	// obTemp.setServentiaRelatorio("");
	// }
	// break;
	// case RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO:
	// if(rs.getString("SERV") != null) {
	// obTemp.setServentiaRelatorio(rs.getString("SERV"));
	// } else {
	// obTemp.setServentiaRelatorio("");
	// }
	// if(rs.getString("GRUPO") != null) {
	// obTemp.setGrupoUsuarioRelatorio(rs.getString("GRUPO"));
	// } else {
	// obTemp.setGrupoUsuarioRelatorio("");
	// }
	// if(rs.getString("NOME") != null) {
	// obTemp.setUsuarioRelatorio(rs.getString("NOME"));
	// } else {
	// obTemp.setUsuarioRelatorio("");
	// }
	// break;
	// }
	//
	// if(rs.getString("EST_PROD_ITEM") != null) {
	// obTemp.setItemEstatisticaRelatorio(rs.getString("EST_PROD_ITEM"));
	// } else {
	// obTemp.setItemEstatisticaRelatorio("");
	// }
	// obTemp.setQuantidade(rs.getDouble("QUANTIDADE"));
	// listaProcessos.add(obTemp);
	// }
	//
	//
	// } finally{
	// try{
	// if (rs != null)
	// rs.close();
	// } catch(Exception e) {
	// }
	// }
	// return listaProcessos;
	// }
	//
	/**
	 * Método que realiza a consulta do relatório analítico de produtividade. param
	 * ano - ano informado na tela param mes - mês informado na tela param idComarca
	 * - ID da Comarca selecionada param idServentia - ID da Serventia selecionada
	 * param idUsuario - ID do usuário selecionado param idMovimentacaoTipo - tipo
	 * de movimentação informada param posicaoPaginaAtual - página indicada na
	 * paginação return lista de produtividade throws Exception author hmgodinho
	 */
	public List relAnaliticoProdutividade(String ano, String mes, String idComarca, String idServentia,
			String idUsuario, String idMovimentacaoTipo, String posicaoPaginaAtual) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		String sqlOrderBy = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sqlSelect = "SELECT distinct vepa.ANO, vepa.MES, vepa.ID_PROC, vepa.ID_MOVI,vepa.PROC_NUMERO, vepa.COMARCA, vepa.NOME, vepa.EST_PROD_ITEM ";
		sql.append("  FROM projudi.VIEW_EST_PROD_ANA vepa ");
		sqlOrderBy = " ORDER BY vepa.ANO, vepa.MES, vepa.PROC_NUMERO, vepa.NOME, vepa.EST_PROD_ITEM";

		boolean clausulaWhere = true;
		boolean clausulaAnd = false;
		if (ano != null && !ano.equals("") && mes != null && !mes.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			sql.append("vepa.ANO = ? AND vepa.MES = ?");
			ps.adicionarLong(ano);
			ps.adicionarLong(mes);
			clausulaAnd = true;
		}

		if (idComarca != null && !idComarca.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
			clausulaAnd = true;
		}
		if (idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_SERV = ?");
			ps.adicionarLong(idServentia);
			clausulaAnd = true;
		}
		if (idUsuario != null && !idUsuario.equals("") && idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_USU = ?");
			ps.adicionarLong(idUsuario);
			clausulaAnd = true;
		}
		if (idMovimentacaoTipo != null && !idMovimentacaoTipo.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_EST_PROD_ITEM = ?");
			ps.adicionarLong(idMovimentacaoTipo);
		}

		try {
			rs = consultarPaginacao(sqlSelect + sql.toString() + sqlOrderBy, ps, posicaoPaginaAtual);
			while (rs.next()) {
				RelatorioAnaliticoDt obTemp = new RelatorioAnaliticoDt();
				obTemp.setAnoRelatorio(rs.getInt("ANO"));
				obTemp.setMesRelatorio(rs.getString("MES"));
				obTemp.setIdProcesso(rs.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs.getString("PROC_NUMERO"));
				obTemp.setComarca(rs.getString("COMARCA"));
				obTemp.setUsuarioRelatorio(rs.getString("NOME"));
				obTemp.setItemEstatisticaRelatorio(rs.getString("EST_PROD_ITEM"));
				listaProcessos.add(obTemp);
			}

			String sqlQuantidade = "SELECT COUNT(1) AS QUANTIDADE FROM (" + sqlSelect + sql.toString() + ")";
			rs = consultar(sqlQuantidade, ps);
			if (rs.next())
				listaProcessos.add(rs.getLong("QUANTIDADE"));

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que realiza a consulta do relatório analítico de processo. param ano -
	 * ano informado na tela param mes - mês informado na tela param idComarca - ID
	 * da Comarca selecionada param idServentia - ID da Serventia selecionada param
	 * idUsuario - ID do usuário selecionado param idProcessoTipo - tipo de processo
	 * informado param posicaoPaginaAtual - página indicada na paginação return
	 * lista de processos throws Exception author hmgodinho
	 */
	public List relAnaliticoProcesso(String ano, String mes, String idComarca, String idServentia,
			String idProcessoTipo, String posicaoPaginaAtual) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		String sqlOrderBy = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sqlSelect = "SELECT * ";
		sql.append("  FROM projudi.VIEW_EST_PROC_ANA vepa ");
		sqlOrderBy = " ORDER BY vepa.ANO, vepa.MES, vepa.PROC_NUMERO, vepa.PROC_TIPO";

		boolean clausulaWhere = true;
		boolean clausulaAnd = false;
		if (ano != null && !ano.equals("") && mes != null && !mes.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			sql.append("vepa.ANO = ? AND vepa.MES = ?");
			ps.adicionarLong(ano);
			ps.adicionarLong(mes);
			clausulaAnd = true;
		}

		if (idComarca != null && !idComarca.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
			clausulaAnd = true;
		}
		if (idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_SERV = ?");
			ps.adicionarLong(idServentia);
			clausulaAnd = true;
		}
		if (idProcessoTipo != null && !idProcessoTipo.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_PROC_TIPO = ?");
			clausulaAnd = true;
			ps.adicionarLong(idProcessoTipo);
		}

		try {
			rs = consultarPaginacao(sqlSelect + sql.toString() + sqlOrderBy, ps, posicaoPaginaAtual);
			while (rs.next()) {
				RelatorioAnaliticoDt obTemp = new RelatorioAnaliticoDt();
				obTemp.setAnoRelatorio(rs.getInt("ANO"));
				obTemp.setMesRelatorio(rs.getString("MES"));
				obTemp.setIdProcesso(rs.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs.getString("PROC_NUMERO"));
				obTemp.setProcessoRelatorio(rs.getString("PROC_TIPO"));
				listaProcessos.add(obTemp);
			}

			String sqlQuantidade = "SELECT COUNT(ID_PROC) AS QUANTIDADE " + sql.toString();
			rs = consultar(sqlQuantidade, ps);
			if (rs.next())
				listaProcessos.add(rs.getLong("QUANTIDADE"));

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que realiza a consulta do relatório analítico de pendência. param ano
	 * - ano informado na tela param mes - mês informado na tela param idComarca -
	 * ID da Comarca selecionada param idServentia - ID da Serventia selecionada
	 * param idUsuario - ID do usuário selecionado param idPendenciaTipo - tipo de
	 * pendência informada param posicaoPaginaAtual - página indicada na paginação
	 * return lista de processos com pendências throws Exception author hmgodinho
	 */
	public List relAnaliticoPendencia(String ano, String mes, String idComarca, String idServentia, String idUsuario,
			String idPendenciaTipo, String posicaoPaginaAtual) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		String sqlOrderBy = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sqlSelect = "SELECT * ";
		sql.append("  FROM projudi.VIEW_EST_PEND_ANA vepa ");
		sqlOrderBy = " ORDER BY vepa.ANO, vepa.MES, vepa.PROC_NUMERO, vepa.NOME, vepa.PEND_TIPO";

		boolean clausulaWhere = true;
		boolean clausulaAnd = false;
		if (ano != null && !ano.equals("") && mes != null && !mes.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			sql.append("vepa.ANO = ? AND vepa.MES = ?");
			ps.adicionarLong(ano);
			ps.adicionarLong(mes);
			clausulaAnd = true;
		}

		if (idComarca != null && !idComarca.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
			clausulaAnd = true;
		}
		if (idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_SERV = ?");
			ps.adicionarLong(idServentia);
			clausulaAnd = true;
		}
		if (idUsuario != null && !idUsuario.equals("") && idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_USU = ?");
			ps.adicionarLong(idUsuario);
			clausulaAnd = true;
		}
		if (idPendenciaTipo != null && !idPendenciaTipo.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_PEND_TIPO = ?");
			ps.adicionarLong(idPendenciaTipo);
		}

		try {
			rs = consultarPaginacao(sqlSelect + sql.toString() + sqlOrderBy, ps, posicaoPaginaAtual);
			while (rs.next()) {
				RelatorioAnaliticoDt obTemp = new RelatorioAnaliticoDt();
				obTemp.setAnoRelatorio(rs.getInt("ANO"));
				obTemp.setMesRelatorio(rs.getString("MES"));
				obTemp.setIdProcesso(rs.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs.getString("PROC_NUMERO"));
				obTemp.setUsuarioRelatorio(rs.getString("NOME"));
				obTemp.setPendenciaRelatorio(rs.getString("PEND_TIPO"));
				listaProcessos.add(obTemp);
			}

			String sqlQuantidade = "SELECT COUNT(ID_PROC) AS QUANTIDADE " + sql.toString();
			rs = consultar(sqlQuantidade, ps);
			if (rs.next())
				listaProcessos.add(rs.getLong("QUANTIDADE"));

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método responsável pela listagem e geração do relatório PDF de maiores
	 * promoventes e promovidos.
	 * 
	 * param tipoParte - tipo de parte selecionada (promovente ou promovido) param
	 * limiteConsulta - quantidade de registros máxima da consulta return lista de
	 * maiores promoventes e promovidos throws Exception author hmgodinho
	 */
	public List relMaioresPromoventesPromovidos(String tipoParte, String limiteConsulta) throws Exception {
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		long limite = Funcoes.StringToLong(limiteConsulta);
		long contador = 1;

		String sql = "SELECT pp.NOME AS NOME , pp.CPF AS CPF_CNPJ, COUNT(pp.ID_PROC) AS QUANTIDADE FROM projudi.PROC_PARTE pp "
				+ "INNER JOIN projudi.PROC_PARTE_TIPO ppt ON pp.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO "
				+ " WHERE pp.DATA_BAIXA IS NULL AND ppt.PROC_PARTE_TIPO_CODIGO = ? "
				+ " AND pp.CPF IS NOT NULL AND pp.CPF NOT IN (?,?) GROUP BY pp.CPF, pp.NOME "
				+ " UNION ( SELECT pp.NOME AS NOME, pp.CNPJ AS CPF_CNPJ, COUNT(pp.ID_PROC) AS QUANTIDADE FROM projudi.PROC_PARTE pp "
				+ " INNER JOIN projudi.PROC_PARTE_TIPO ppt ON pp.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO WHERE pp.DATA_BAIXA IS NULL "
				+ " AND ppt.PROC_PARTE_TIPO_CODIGO = ? AND pp.CNPJ IS NOT NULL GROUP BY pp.CNPJ, pp.NOME) ";

		sql = "SELECT * FROM (" + sql + ") ORDER BY QUANTIDADE DESC";

		ps.adicionarLong(tipoParte);
		ps.adicionarLong(11111111111l);
		ps.adicionarLong(99999999999l);
		ps.adicionarLong(tipoParte);

		try {
			rs = consultar(sql, ps);
			while (rs.next() && contador <= limite) {
				RelatorioMaioresPromoventesPromovidosDt obTemp = new RelatorioMaioresPromoventesPromovidosDt();
				obTemp.setNomeParteRelatorio(rs.getString("NOME"));
				if (rs.getString("CPF_CNPJ").length() > 11) {
					obTemp.setCpfCnpjParteRelatorio(Funcoes.formataCNPJ(rs.getString("CPF_CNPJ")));
				} else {
					obTemp.setCpfCnpjParteRelatorio(Funcoes.formataCPF(rs.getString("CPF_CNPJ")));
				}
				obTemp.setQtdeProcessosRelatorio(rs.getInt("QUANTIDADE"));
				listaProcessos.add(obTemp);
				contador += 1;
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que gera o relatório sumário de serventias com mais processos. param
	 * tipoProcesso - tipo do processo informado param limiteConsulta - limite de
	 * registros da consulta return lista de serventias throws Exception author
	 * hmgodinho
	 */
	public List relSumarioMaisProcessosServentia(String tipoProcesso, String limiteConsulta) throws Exception {
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs = null;
		String sql = "";
		long limite = Funcoes.StringToLong(limiteConsulta);
		long contador = 1;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		if (tipoProcesso.equals(String.valueOf(RelatorioSumarioProcessosServentiaDt.ATIVO))) {
			sql = "SELECT s.SERV AS SERV, COUNT(p.ID_PROC) AS QUANTIDADE FROM projudi.PROC p "
					+ " INNER JOIN projudi.SERV s ON p.ID_SERV = s.ID_SERV "
					+ " INNER JOIN projudi.PROC_STATUS ps ON p.ID_PROC_STATUS = ps.ID_PROC_STATUS "
					+ " WHERE ps.PROC_STATUS_CODIGO = ? GROUP BY s.ID_SERV, s.SERV ORDER BY QUANTIDADE DESC";
			ps.adicionarLong(0);
		} else if (tipoProcesso.equals(String.valueOf(RelatorioSumarioProcessosServentiaDt.ARQUIVADO))) {
			sql = "SELECT s.SERV AS SERV, COUNT(p.ID_PROC) AS QUANTIDADE FROM projudi.PROC p "
					+ " INNER JOIN projudi.SERV s ON p.ID_SERV = s.ID_SERV "
					+ " WHERE p.DATA_ARQUIVAMENTO IS NOT NULL GROUP BY s.ID_SERV, s.SERV ORDER BY QUANTIDADE DESC";
		} else if (tipoProcesso.equals(String.valueOf(RelatorioSumarioProcessosServentiaDt.DISTRIBUIDO))) {
			sql = "SELECT s.SERV AS SERV, COUNT(*) AS QUANTIDADE FROM projudi.PROC p "
					+ " INNER JOIN projudi.SERV s ON p.ID_SERV = s.ID_SERV "
					+ " GROUP BY p.ID_SERV, s.SERV ORDER BY QUANTIDADE DESC";
		}
		try {
			rs = consultar(sql, ps);
			while (rs.next() && contador <= limite) {
				RelatorioSumarioProcessosServentiaDt obTemp = new RelatorioSumarioProcessosServentiaDt();
				obTemp.setNomeServentiaRelatorio(rs.getString("SERV"));
				obTemp.setQtdeProcessosRelatorio(rs.getInt("QUANTIDADE"));
				listaProcessos.add(obTemp);
				contador += 1;
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que gera o relatório sumário de comarcas com mais processos. param
	 * tipoProcesso - tipo do processo informado param limiteConsulta - limite de
	 * registros da consulta return lista de comarcas throws Exception author
	 * hmgodinho
	 */
	public List relSumarioMaisProcessosComarca(String tipoProcesso, String limiteConsulta) throws Exception {
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs = null;
		String sql = "";
		long limite = Funcoes.StringToLong(limiteConsulta);
		long contador = 1;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		if (tipoProcesso.equals(String.valueOf(RelatorioSumarioProcessosComarcaDt.ATIVO))) {
			sql = "SELECT c.COMARCA AS COMARCA, COUNT(p.ID_PROC) AS QUANTIDADE FROM projudi.PROC p "
					+ " INNER JOIN projudi.SERV s ON p.ID_SERV = s.ID_SERV INNER JOIN COMARCA c ON s.ID_COMARCA = c.ID_COMARCA "
					+ " INNER JOIN projudi.PROC_STATUS ps ON p.ID_PROC_STATUS = ps.ID_PROC_STATUS WHERE ps.PROC_STATUS_CODIGO = ? "
					+ " GROUP BY c.ID_COMARCA, c.COMARCA ORDER BY QUANTIDADE DESC";
			ps.adicionarLong(0);
		} else if (tipoProcesso.equals(String.valueOf(RelatorioSumarioProcessosComarcaDt.ARQUIVADO))) {
			sql = "SELECT c.COMARCA AS COMARCA, COUNT(p.ID_PROC) AS QUANTIDADE FROM projudi.PROC p "
					+ " INNER JOIN projudi.SERV s ON p.ID_SERV = s.ID_SERV INNER JOIN projudi.COMARCA c ON s.ID_COMARCA = c.ID_COMARCA "
					+ " WHERE p.DATA_ARQUIVAMENTO IS NOT NULL GROUP BY c.ID_COMARCA, c.COMARCA ORDER BY QUANTIDADE DESC";
		} else if (tipoProcesso.equals(String.valueOf(RelatorioSumarioProcessosComarcaDt.DISTRIBUIDO))) {
			sql = "SELECT c.COMARCA AS COMARCA, COUNT(*) AS QUANTIDADE FROM projudi.PROC p "
					+ " INNER JOIN projudi.SERV s ON p.ID_SERV = s.ID_SERV INNER JOIN projudi.COMARCA c ON s.ID_COMARCA = c.ID_COMARCA "
					+ " GROUP BY c.ID_COMARCA, c.COMARCA ORDER BY QUANTIDADE DESC";
		}
		try {
			rs = consultar(sql, ps);
			while (rs.next() && contador <= limite) {
				RelatorioSumarioProcessosComarcaDt obTemp = new RelatorioSumarioProcessosComarcaDt();
				obTemp.setNomeComarcaRelatorio(rs.getString("COMARCA"));
				obTemp.setQtdeProcessosRelatorio(rs.getInt("QUANTIDADE"));
				listaProcessos.add(obTemp);
				contador += 1;
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que gera o relatório quantitativo de processos distribuídos no 2º
	 * grau, segundo os parâmetros informados. param ano - ano da distribuição param
	 * idAreaDistribuicao - ID da área de distribuição return lista de processos
	 * distribuídos throws Exception author hmgodinho
	 */
	public List imprimirRelatorioProcessoSegundoGrauArea(String ano, String idAreaDistribuicao) throws Exception {

		List listaProcessos = new ArrayList();
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "	SELECT s.SERV AS SERV, pt.PROC_TIPO AS PROC_TIPO, tab.Qtd AS QUANTIDADE ";
		sql += "		FROM ( SELECT p.ID_SERV, p.ID_PROC_TIPO, COUNT(*) AS Qtd ";
		sql += "				FROM  projudi.SERV_AREA_DIST sad ";
		sql += "					INNER JOIN projudi.SERV s ON sad.ID_SERV = s.ID_SERV ";
		sql += "                    INNER JOIN projudi.PROC p ON s.ID_SERV = p.ID_SERV ";
		sql += "                    LEFT JOIN projudi.RECURSO r ON r.ID_PROC = p.ID_PROC ";
		sql += "				WHERE sad.ID_AREA_DIST = ?";
		sql += " 					AND (p.ANO = ? OR TO_NUMBER(TO_CHAR(r.DATA_RECEBIMENTO,'YYYY')) = ?)";
		sql += " 				GROUP BY p.ID_SERV, p.ID_PROC_TIPO) tab ";
		sql += " 			INNER JOIN projudi.SERV s ON tab.ID_SERV = s.ID_SERV ";
		sql += "			INNER JOIN projudi.PROC_TIPO pt ON tab.ID_PROC_TIPO = pt.ID_PROC_TIPO ";
		sql += "		ORDER BY pt.PROC_TIPO , s.SERV ";

		ps.adicionarLong(idAreaDistribuicao);
		ps.adicionarLong(ano);
		ps.adicionarLong(ano);

		try {
			rs = consultar(sql, ps);
			while (rs.next()) {
				RelatorioProcessoSegundoGrauDt obTemp = new RelatorioProcessoSegundoGrauDt();
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setProcessoTipo(rs.getString("PROC_TIPO"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que gera o relatório quantitativo de processos distribuídos no 1º grau
	 * por Juizes da Serventia do tipo Turma, segundo os parâmetros informados.
	 * param anoConsulta - ano da distribuição param idServentia - ID da Serventia
	 * selecionada return lista de processos distribuídos author hmgodinho
	 */
	public List imprimirRelatorioProcessoMagistradoResponsavel(String ano, String idServentia)
			throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		
		sql.append("select pt.proc_tipo as PROC_TIPO, s.serv as SERV, sc.serv_cargo as NOME, tab.total as QUANTIDADE  from ( ");
		sql.append("		select pl.id_serv, pl.id_area_dist, pt.id_proc_tipo, pl.id_serv_cargo , sum(pl.qtd) total "); 
		sql.append("		    from ponteiro_log pl ");
		sql.append("		        inner join proc p on pl.id_proc = p.id_proc ");
		sql.append("		        inner join proc_tipo pt on pt.id_proc_tipo = p.id_proc_tipo ");
		sql.append("		where pl.id_ponteiro_log_tipo=1 and pl.id_serv = ? and data BETWEEN ? AND ? ");		ps.adicionarLong(idServentia); 
																												ps.adicionarDateTime(Funcoes.getPrimeiroDiaHoraMinutoSegundoAno(ano));
																												ps.adicionarDateTime(Funcoes.getUltimoDiaHoraMinutoSegundoMes("12", ano));
		sql.append("		group by pl.id_serv, pl.id_area_dist, pt.id_proc_tipo, pl.id_serv_cargo ");
		sql.append("		) tab inner join serv s on tab.id_Serv = s.id_serv ");
		sql.append(" 			  inner join serv_cargo sc on tab.id_serv_cargo = sc.id_serv_cargo ");
		sql.append("		      inner join proc_tipo pt on tab.id_proc_tipo = pt.id_proc_tipo ");      
		sql.append("		      order by  pt.PROC_TIPO, sc.serv_Cargo ");
				      
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioProcessoSegundoGrauDt obTemp = new RelatorioProcessoSegundoGrauDt();
				obTemp.setProcessoTipo(rs.getString("PROC_TIPO"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setNomeResponsavel(rs.getString("NOME"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que gera o relatório quantitativo de processos distribuídos no 2º grau
	 * por Desembargadores da Serventia, segundo os parâmetros informados. param
	 * anoConsulta - ano da distribuição param idServentia - ID da Serventia
	 * selecionada return lista de processos distribuídos author hmgodinho
	 */
	public List imprimirRelatorioProcessoSegundoGrauDesembargadoresServentia2Grau(String ano, String idServentia)
			throws Exception {
		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" SELECT pt.proc_tipo as PROC_TIPO, s1.serv as SERV, u.nome as NOME, COUNT(1) as QUANTIDADE FROM "
				+ "		(SELECT p.id_proc_tipo, sc.id_serv, sc.id_serv_cargo  " + "			FROM projudi.serv s "
				+ "			INNER JOIN projudi.serv_relacionada sr on s.id_serv = sr.id_serv_princ "
				+ "			INNER JOIN projudi.serv_cargo sc on sr.id_serv_rel = sc.id_serv "
				+ "			INNER JOIN projudi.cargo_tipo ct on sc.id_cargo_tipo = ct.id_cargo_tipo "
				+ "			INNER JOIN projudi.grupo g on ct.id_grupo = g.id_grupo "
				+ "			INNER JOIN projudi.grupo_tipo gt on gt.id_grupo_tipo = g.id_grupo_tipo "
				+ "			INNER JOIN projudi.proc_resp pr on pr.id_serv_cargo = sc.id_serv_cargo "
				+ "			INNER JOIN projudi.proc p on pr.id_proc = p.id_proc " + "			WHERE s.id_serv = ?");
		ps.adicionarLong(idServentia);
		sql.append("				AND gt.grupo_tipo_codigo = ? ");
		ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
		sql.append("				AND sr.recebe_proc = ? ");
		ps.adicionarLong(1);
		sql.append("				AND pr.redator = ? ");
		ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
		sql.append("				AND (p.data_recebimento BETWEEN ? AND ? ");
		ps.adicionarDateTime(Funcoes.getPrimeiroDiaHoraMinutoSegundoAno(ano));
		ps.adicionarDateTime(Funcoes.getUltimoDiaHoraMinutoSegundoMes("12", ano));
		sql.append(
				"					OR ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ");
		ps.adicionarDateTime(Funcoes.getPrimeiroDiaHoraMinutoSegundoAno(ano));
		sql.append(
				"						AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ))");
		ps.adicionarDateTime(Funcoes.getUltimoDiaHoraMinutoSegundoMes("12", ano));
		sql.append("		) tab " + "	INNER JOIN projudi.serv s1 ON tab.id_serv = s1.id_serv "
				+ "	INNER JOIN projudi.proc_tipo pt ON tab.id_proc_tipo = pt.id_proc_tipo "
				+ "	INNER JOIN projudi.serv_cargo sc ON tab.id_serv_cargo = sc.id_serv_cargo "
				+ "	LEFT JOIN projudi.usu_serv_grupo usg ON usg.id_usu_serv_grupo = sc.id_usu_serv_grupo "
				+ "	LEFT JOIN projudi.usu_serv us ON us.id_usu_serv = usg.id_usu_serv "
				+ "	LEFT JOIN projudi.usu u ON us.id_usu = u.id_usu " + "	GROUP BY pt.proc_tipo, s1.serv, u.nome "
				+ "	ORDER BY pt.proc_tipo, u.nome");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioProcessoSegundoGrauDt obTemp = new RelatorioProcessoSegundoGrauDt();
				obTemp.setProcessoTipo(rs.getString("PROC_TIPO"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setNomeResponsavel(rs.getString("NOME"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que gera o relatório quantitativo de processos distribuídos no 1º grau
	 * por Juízes da Serventia, segundo os parâmetros informados. param anoConsulta
	 * - ano da distribuição param idServentia - ID da Serventia selecionada return
	 * lista de processos distribuídos author hmgodinho
	 */
	public List imprimirRelatorioProcessoPrimeiroGrauJuizes(String ano, String idServentia) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("SELECT s.SERV, pt.PROC_TIPO, u.NOME, ct.CARGO_TIPO, tab.QUANTIDADE FROM ( "
				+ "			SELECT p.ID_SERV, p.ID_PROC_TIPO, us.ID_USU, ct.ID_CARGO_TIPO, COUNT(*) AS QUANTIDADE FROM projudi.PROC p "
				+ "				INNER JOIN projudi.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC "
				+ "				INNER JOIN projudi.SERV_CARGO sc ON sc.ID_SERV_CARGO = pr.ID_SERV_CARGO "
				+ "				INNER JOIN projudi.USU_SERV_GRUPO usg ON usg.ID_USU_SERV_GRUPO = sc.ID_USU_SERV_GRUPO "
				+ "				INNER JOIN projudi.USU_SERV us ON usg.ID_USU_SERV = us.ID_USU_SERV "
				+ "				INNER JOIN projudi.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO "
				+ "				WHERE p.ANO = ? " + "					AND p.ID_SERV = ? "
				+ "					AND ct.CARGO_TIPO_CODIGO = ? " + "					AND pr.CODIGO_TEMP = ? "
				+ "				GROUP BY p.ID_SERV, p.ID_PROC_TIPO, us.ID_USU, ct.ID_CARGO_TIPO) tab "
				+ "		INNER JOIN projudi.SERV s ON s.ID_SERV = tab.ID_SERV "
				+ "		INNER JOIN projudi.USU u ON u.ID_USU = tab.ID_USU "
				+ "		INNER JOIN projudi.PROC_TIPO pt ON tab.ID_PROC_TIPO = pt.ID_PROC_TIPO "
				+ "		INNER JOIN projudi.CARGO_TIPO ct ON ct.ID_CARGO_TIPO = tab.ID_CARGO_TIPO "
				+ "		ORDER BY pt.PROC_TIPO, u.NOME");
		ps.adicionarLong(ano);
		ps.adicionarLong(idServentia);
		ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU);
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioProcessoSegundoGrauDt obTemp = new RelatorioProcessoSegundoGrauDt();
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setProcessoTipo(rs.getString("PROC_TIPO"));
				obTemp.setNomeResponsavel(rs.getString("NOME"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que realiza a consulta de conclusões pendentes no segundo grau. param
	 * idServentia - id da serventia a ser consultada. return lista de conclusões
	 * pendentes throws Exception author hmgodinho
	 */
	@SuppressWarnings("unchecked")
	public List imprimirRelatorioConclusoesPendentesSegundoGrau(String idServentia) throws Exception {
		List listaConclusoes = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "	SELECT ct.CARGO_TIPO, u.NOME, pt.PEND_TIPO, tab.DATA_INICIO AS DATA_INI, TO_CHAR(tab.DATA_INICIO, 'DD/MM/YYYY HH24:MI:SS') as DATA_INICIO, tab.PROC_NUMERO, tab.DIGITO_VERIFICADOR FROM "
				+ "		(SELECT pr.ID_SERV_CARGO, pro.PROC_NUMERO, pro.DIGITO_VERIFICADOR, p.ID_PEND_TIPO, p.DATA_INICIO FROM projudi.SERV_CARGO sc "
				+ "			INNER JOIN projudi.PEND_RESP pr ON sc.ID_SERV_CARGO = pr.ID_SERV_CARGO "
				+ "			INNER JOIN projudi.PEND p ON pr.ID_PEND = p.ID_PEND "
				+ "			INNER JOIN projudi.PROC pro ON p.ID_PROC = pro.ID_PROC "
				+ "			WHERE sc.ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		sql += "				AND p.DATA_FIM IS NULL " + "				AND pro.DATA_ARQUIVAMENTO IS NULL "
				+ "		) tab " + "		INNER JOIN projudi.SERV_CARGO sc ON tab.ID_SERV_CARGO = sc.ID_SERV_CARGO "
				+ "		INNER JOIN projudi.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO "
				+ "		INNER JOIN projudi.USU_SERV_GRUPO usg ON sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO "
				+ "		INNER JOIN projudi.USU_SERV us ON us.ID_USU_SERV = usg.ID_USU_SERV "
				+ "		INNER JOIN projudi.USU u ON us.ID_USU = u.ID_USU "
				+ "		INNER JOIN projudi.PEND_TIPO pt ON pt.ID_PEND_TIPO = tab.ID_PEND_TIPO "
				+ "		WHERE PEND_TIPO_CODIGO IN (";
		List<Integer> conclusoes = PendenciaTipoDt.Conclusoes;
		for (int i = 0; i < conclusoes.size(); i++) 
		{ 
			if (conclusoes.get(i) != PendenciaTipoDt.CONCLUSO_VOTO &&
				conclusoes.get(i) != PendenciaTipoDt.CONCLUSO_EMENTA) {				
				if (i != 0) {
					sql += ",";
				}
				sql += "?";
				ps.adicionarLong(conclusoes.get(i));
			}			
		}
		sql +=  ")";
		sql +=  " 		ORDER BY u.NOME, pt.PEND_TIPO, DATA_INI, PROC_NUMERO, DIGITO_VERIFICADOR ";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioConclusoesSegundoGrauDt obTemp = new RelatorioConclusoesSegundoGrauDt();
				obTemp.setCargoTipo(rs.getString("CARGO_TIPO"));
				obTemp.setNomeResponsavel(rs.getString("NOME"));
				obTemp.setProcessoNumero(rs.getString("PROC_NUMERO") + "- " + rs.getString("DIGITO_VERIFICADOR"));
				obTemp.setPendenciaTipo(rs.getString("PEND_TIPO"));
				obTemp.setDataFim(rs.getString("DATA_INICIO"));
				listaConclusoes.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaConclusoes;
	}

	/**
	 * Método responsável pela consulta de conclusões realizadas em determinado
	 * período no segundo grau param dataInicial - data inicial param dataFinal -
	 * data final param idServentia - id da serventia return lista de conclusões
	 * realizadas throws Exception author hmgodinho
	 */
	public List imprimirRelatorioConclusoesRealizadasSegundoGrau(Date dataInicial, Date dataFinal, String idServentia)
			throws Exception {
		List listaConclusoes = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "	SELECT ct.CARGO_TIPO, u.NOME, pt.PEND_TIPO, tab.DATA_FIM AS DATA_FINAL, TO_CHAR(tab.DATA_FIM, 'DD/MM/YYYY HH24:MI:SS') as DATA_FIM, tab.PROC_NUMERO, tab.DIGITO_VERIFICADOR FROM "
				+ "		(SELECT pr.ID_SERV_CARGO, pro.PROC_NUMERO, pro.DIGITO_VERIFICADOR, p.ID_PEND_TIPO, p.DATA_FIM FROM projudi.SERV_CARGO sc "
				+ "			INNER JOIN projudi.PEND_RESP pr ON sc.ID_SERV_CARGO = pr.ID_SERV_CARGO "
				+ "			INNER JOIN projudi.PEND p ON pr.ID_PEND = p.ID_PEND "
				+ "			INNER JOIN projudi.PROC pro ON p.ID_PROC = pro.ID_PROC "
				+ "			WHERE sc.ID_SERV = ? ";						ps.adicionarLong(idServentia);
		sql += "				AND p.DATA_FIM BETWEEN ? AND ?"; ps.adicionarDateTime(dataInicial); ps.adicionarDateTime(dataFinal);
		sql += "			) TAB " + "		INNER JOIN projudi.SERV_CARGO sc ON tab.ID_SERV_CARGO = sc.ID_SERV_CARGO "
				+ "		INNER JOIN projudi.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO "
				+ "		INNER JOIN projudi.USU_SERV_GRUPO usg ON sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO "
				+ "		INNER JOIN projudi.USU_SERV us ON us.ID_USU_SERV = usg.ID_USU_SERV "
				+ "		INNER JOIN projudi.USU u ON us.ID_USU = u.ID_USU "
				+ "		INNER JOIN projudi.PEND_TIPO pt ON pt.ID_PEND_TIPO = tab.ID_PEND_TIPO "
				+ "		ORDER BY u.NOME, pt.PEND_TIPO, DATA_FINAL, PROC_NUMERO, DIGITO_VERIFICADOR";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioConclusoesSegundoGrauDt obTemp = new RelatorioConclusoesSegundoGrauDt();
				obTemp.setCargoTipo(rs.getString("CARGO_TIPO"));
				obTemp.setNomeResponsavel(rs.getString("NOME"));
				obTemp.setProcessoNumero(rs.getString("PROC_NUMERO") + "-" + rs.getString("DIGITO_VERIFICADOR"));
				obTemp.setPendenciaTipo(rs.getString("PEND_TIPO"));
				obTemp.setDataFim(rs.getString("DATA_FIM"));
				listaConclusoes.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaConclusoes;
	}

	/**
	 * Método responsável pela consulta de conclusões recebidas em determinado
	 * período no segundo grau param dataInicial - data inicial param dataFinal -
	 * data final param idServentia - id da serventia return lista de conclusões
	 * recebidas throws Exception author hmgodinho
	 */
	public List imprimirRelatorioConclusoesRecebidasSegundoGrau(Date dataInicial, Date dataFinal, String idServentia)
			throws Exception {
		List listaConclusoes = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "	SELECT  ct.CARGO_TIPO, u.NOME, pt.PEND_TIPO, tab.DATA_INICIO AS DATA_INI, TO_CHAR(tab.DATA_INICIO, 'DD/MM/YYYY HH24:MI:SS') as DATA_INICIO, tab.PROC_NUMERO, tab.DIGITO_VERIFICADOR FROM "
				+ "		(SELECT pr.ID_SERV_CARGO, pro.PROC_NUMERO, pro.DIGITO_VERIFICADOR, p.ID_PEND_TIPO, p.DATA_INICIO FROM projudi.SERV_CARGO sc "
				+ "			INNER JOIN projudi.PEND_RESP pr ON sc.ID_SERV_CARGO = pr.ID_SERV_CARGO "
				+ "			INNER JOIN projudi.PEND p ON pr.ID_PEND = p.ID_PEND "
				+ "			INNER JOIN projudi.PROC pro ON p.ID_PROC = pro.ID_PROC "
				+ "				WHERE sc.ID_SERV = ? ";					ps.adicionarLong(idServentia);
		sql += "				AND p.DATA_INICIO BETWEEN ? AND ?"; ps.adicionarDateTime(dataInicial); ps.adicionarDateTime(dataFinal);
		sql += "			) TAB " + "		INNER JOIN projudi.SERV_CARGO sc ON tab.ID_SERV_CARGO = sc.ID_SERV_CARGO "
				+ "		INNER JOIN projudi.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO "
				+ "		INNER JOIN projudi.USU_SERV_GRUPO usg ON sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO "
				+ "		INNER JOIN projudi.USU_SERV us ON us.ID_USU_SERV = usg.ID_USU_SERV "
				+ "		INNER JOIN projudi.USU u ON us.ID_USU = u.ID_USU "
				+ "		INNER JOIN projudi.PEND_TIPO pt ON pt.ID_PEND_TIPO = tab.ID_PEND_TIPO "
				+ "		ORDER BY u.NOME, pt.PEND_TIPO, DATA_INI, PROC_NUMERO, DIGITO_VERIFICADOR";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioConclusoesSegundoGrauDt obTemp = new RelatorioConclusoesSegundoGrauDt();
				obTemp.setCargoTipo(rs.getString("CARGO_TIPO"));
				obTemp.setNomeResponsavel(rs.getString("NOME"));
				obTemp.setPendenciaTipo(rs.getString("PEND_TIPO"));
				obTemp.setProcessoNumero(rs.getString("PROC_NUMERO") + "-" + rs.getString("DIGITO_VERIFICADOR"));
				obTemp.setDataFim(rs.getString("DATA_INICIO"));
				listaConclusoes.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaConclusoes;
	}

	/**
	 * Método que realiza a consulta de conclusões pendentes no primeiro grau. param
	 * idServentia - id da serventia a ser consultada. return lista de conclusões
	 * pendentes throws Exception author hmgodinho
	 */
	public List imprimirRelatorioConclusoesPendentesPrimeiroGrau(String idServentia) throws Exception {
		List listaConclusoes = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "	SELECT ct.CARGO_TIPO, sc.SERV_CARGO, u.NOME, pt.PEND_TIPO, tab.DATA_INICIO AS DATA_INI, TO_CHAR(tab.DATA_INICIO, 'DD/MM/YYYY HH24:MI:SS') as DATA_INICIO, tab.PROC_NUMERO, tab.DIGITO_VERIFICADOR FROM "
				+ "		(SELECT pr.ID_SERV_CARGO, p.ID_PEND_TIPO, COUNT(1) as Qtd  FROM projudi.SERV_CARGO sc "
				+ "			INNER JOIN projudi.PEND_RESP pr ON sc.ID_SERV_CARGO = pr.ID_SERV_CARGO "
				+ "			INNER JOIN projudi.PEND p ON pr.ID_PEND = p.ID_PEND "
				+ "			INNER JOIN projudi.PROC pro ON p.ID_PROC = pro.ID_PROC "
				+ "			WHERE sc.ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		sql += "				AND p.DATA_FIM IS NULL " + "				AND pro.DATA_ARQUIVAMENTO IS NULL "
				+ "			GROUP BY pr.ID_SERV_CARGO, p.ID_PEND_TIPO " + "		) AS tab "
				+ "		INNER JOIN projudi.SERV_CARGO sc ON tab.ID_SERV_CARGO = sc.ID_SERV_CARGO "
				+ "		INNER JOIN projudi.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO "
				+ "		INNER JOIN projudi.USU_SERV_GRUPO usg ON sc.ID_USU_SERV_GRUPO = usg.ID_USU_SERV_GRUPO "
				+ "		INNER JOIN projudi.USU_SERV us ON us.ID_USU_SERV = usg.ID_USU_SERV "
				+ "		INNER JOIN projudi.USU u ON us.ID_USU = u.ID_USU "
				+ "		INNER JOIN projudi.PEND_TIPO pt ON pt.ID_PEND_TIPO = tab.ID_PEND_TIPO";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioConclusoesSegundoGrauDt obTemp = new RelatorioConclusoesSegundoGrauDt();
				obTemp.setCargoTipo(rs.getString("CARGO_TIPO"));
				obTemp.setNomeResponsavel(rs.getString("NOME"));
				obTemp.setPendenciaTipo(rs.getString("PEND_TIPO"));
				obTemp.setQuantidade(rs.getLong("Qtd"));
				listaConclusoes.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaConclusoes;
	}

	/**
	 * Método responsável pela consulta de conclusões realizadas em determinado
	 * período no Primeiro grau param dataInicial - data inicial param dataFinal -
	 * data final param idServentia - id da serventia return lista de conclusões
	 * realizadas throws Exception author hmgodinho
	 */
	public List imprimirRelatorioConclusoesRealizadasPrimeiroGrau(Date dataInicial, Date dataFinal, String idServentia)
			throws Exception {
		List listaConclusoes = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "	SELECT  ct.CargoTipo, sc.ServentiaCargo, u.Nome, pt.PendenciaTipo, tab.* FROM "
				+ "		(SELECT pr.Id_ServentiaCargo, p.Id_PendenciaTipo, COUNT(1) AS Qtd  FROM projudi.ServentiaCargo sc "
				+ "			INNER JOIN projudi.PendenciaResponsavel pr ON sc.Id_ServentiaCargo = pr.Id_ServentiaCargo "
				+ "			INNER JOIN projudi.Pendencia p ON pr.Id_Pendencia = p.Id_Pendencia "
				+ "			INNER JOIN projudi.Processo pro ON p.Id_Processo = pro.Id_Processo "
				+ "			WHERE sc.Id_Serventia = ? ";			ps.adicionarLong(idServentia);
		sql += "				AND p.DataFim >= ? ";				ps.adicionarDate(dataInicial);
		sql += "				AND p.DataFim <= ? ";				ps.adicionarDate(dataFinal);
		sql += "			GROUP BY pr.Id_ServentiaCargo, p.Id_PendenciaTipo) AS tab "
				+ "		INNER JOIN projudi.ServentiaCargo sc ON tab.Id_ServentiaCargo = sc.Id_ServentiaCargo "
				+ "		INNER JOIN projudi.CargoTipo ct ON sc.Id_CargoTipo = ct.Id_CargoTipo "
				+ "		INNER JOIN projudi.UsuarioServentiaGrupo usg ON sc.Id_UsuarioServentiaGrupo = usg.Id_UsuarioServentiaGrupo "
				+ "		INNER JOIN projudi.UsuarioServentia us ON us.Id_UsuarioServentia = usg.Id_UsuarioServentia "
				+ "		INNER JOIN projudi.Usuario u ON us.Id_Usuario = u.Id_Usuario "
				+ "		INNER JOIN projudi.PendenciaTipo pt ON pt.Id_PendenciaTipo = tab.Id_PendenciaTipo ";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioConclusoesPrimeiroGrauDt obTemp = new RelatorioConclusoesPrimeiroGrauDt();
				obTemp.setCargoTipo(rs.getString("CargoTipo"));
				obTemp.setNomeResponsavel(rs.getString("Nome"));
				obTemp.setPendenciaTipo(rs.getString("PendenciaTipo"));
				obTemp.setQuantidade(rs.getLong("Qtd"));
				listaConclusoes.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaConclusoes;
	}

	/**
	 * Método responsável pela consulta de conclusões recebidas em determinado
	 * período no Primeiro grau param dataInicial - data inicial param dataFinal -
	 * data final param idServentia - id da serventia return lista de conclusões
	 * recebidas throws Exception author hmgodinho
	 */
	public List imprimirRelatorioConclusoesRecebidasPrimeiroGrau(Date dataInicial, Date dataFinal, String idServentia)
			throws Exception {
		List listaConclusoes = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "	SELECT  ct.CargoTipo,sc.ServentiaCargo, u.Nome, pt.PendenciaTipo, tab.* FROM "
				+ "		(SELECT pr.Id_ServentiaCargo, p.Id_PendenciaTipo, COUNT(1) AS Qtd  FROM projudi.ServentiaCargo sc "
				+ "			INNER JOIN projudi.PendenciaResponsavel pr ON sc.Id_ServentiaCargo = pr.Id_ServentiaCargo "
				+ "			INNER JOIN projudi.Pendencia p ON pr.Id_Pendencia = p.Id_Pendencia "
				+ "			INNER JOIN projudi.Processo pro ON p.Id_Processo = pro.Id_Processo "
				+ "				WHERE sc.Id_Serventia = ? ";			ps.adicionarLong(idServentia);
		sql += "				AND p.DataInicio >= ? ";				ps.adicionarDate(dataInicial);
		sql += "				AND p.DataInicio <= ?  ";				ps.adicionarDate(dataFinal);
		sql += "			GROUP BY pr.Id_ServentiaCargo, p.Id_PendenciaTipo) AS tab "
				+ "		INNER JOIN projudi.ServentiaCargo sc ON tab.Id_ServentiaCargo = sc.Id_ServentiaCargo "
				+ "		INNER JOIN projudi.CargoTipo ct ON sc.Id_CargoTipo = ct.Id_CargoTipo "
				+ "		INNER JOIN projudi.UsuarioServentiaGrupo usg ON sc.Id_UsuarioServentiaGrupo = usg.Id_UsuarioServentiaGrupo "
				+ "		INNER JOIN projudi.UsuarioServentia us ON us.Id_UsuarioServentia = usg.Id_UsuarioServentia "
				+ "		INNER JOIN projudi.Usuario u ON us.Id_Usuario = u.Id_Usuario "
				+ "		INNER JOIN projudi.PendenciaTipo pt ON pt.Id_PendenciaTipo = tab.Id_PendenciaTipo ";
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioConclusoesPrimeiroGrauDt obTemp = new RelatorioConclusoesPrimeiroGrauDt();
				obTemp.setCargoTipo(rs.getString("CargoTipo"));
				obTemp.setNomeResponsavel(rs.getString("Nome"));
				obTemp.setPendenciaTipo(rs.getString("PendenciaTipo"));
				obTemp.setQuantidade(rs.getLong("Qtd"));
				listaConclusoes.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaConclusoes;
	}

	/**
	 * Método responsável por imprimir a capa do relatório de situação do gabinete.
	 * 
	 * @param mesInicial
	 *            - mês inicial do relatório
	 * @param anoInicial
	 *            - ano inicial do relatório
	 * @param mesFinal
	 *            - mês final do relatório
	 * @param anoFinal
	 *            - ano final do relatório
	 * @param idServentiaCargoResponsavel
	 *            - ID Serventia Cargo do responsável do relatório
	 * @return o relatorioSituacaoGabinete preenchido com a lista
	 * @throws Exception
	 * @author hmgodinho
	 */
	public RelatorioSituacaoGabineteDt imprimirCapaRelatorioSituacaoGabinte(String mesInicial, String anoInicial,
			String mesFinal, String anoFinal, String idServentiaCargoResponsavel) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RelatorioSituacaoGabineteDt relatorioSituacaoGabineteDt = null;

		String sql = "	SELECT * FROM " + "			(SELECT COUNT(*) AS QTDE_DISTRIBUIDOS FROM PROJUDI.PROC p "
				+ "				LEFT JOIN PROJUDI.RECURSO r ON r.ID_PROC = p.ID_PROC "
				+ "				INNER JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC "
				+ "			WHERE (p.DATA_RECEBIMENTO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy') ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "					or r.DATA_RECEBIMENTO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy')) ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND pr.ID_SERV_CARGO = ? ";
		ps.adicionarLong(idServentiaCargoResponsavel);
		sql += "				AND pr.CODIGO_TEMP = ? ";
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		sql += "				AND pr.ID_CARGO_TIPO = ? ) QTDE_DISTRIBUIDOS , ";
		ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		sql += "			(SELECT COUNT(*) AS QTDE_CONCLUSOS_FINALIZADOS FROM PROJUDI.PROC p "
				+ "				INNER JOIN PROJUDI.PEND pe ON pe.ID_PROC = p.ID_PROC "
				+ "				INNER JOIN PROJUDI.PEND_RESP per ON per.ID_PEND = pe.ID_PEND "
				+ "			WHERE (pe.DATA_INICIO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy')) ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND pe.DATA_FIM IS NOT NULL "
				+ "				AND per.ID_SERV_CARGO = ?) QTDE_CONCLUSOS_FINALIZADOS , ";
		ps.adicionarLong(idServentiaCargoResponsavel);
		sql += "			(SELECT COUNT(*) AS QTDE_CONCLUSOS_PENDENTES FROM PROJUDI.PROC p "
				+ "				INNER JOIN PROJUDI.PEND pe ON pe.ID_PROC = p.ID_PROC "
				+ "				INNER JOIN PROJUDI.PEND_RESP per ON per.ID_PEND = pe.ID_PEND "
				+ "			WHERE (pe.DATA_INICIO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy')) ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND pe.DATA_FIM IS NULL "
				+ "				AND per.ID_SERV_CARGO = ?) QTDE_CONCLUSOS_PENDENTES , ";
		ps.adicionarLong(idServentiaCargoResponsavel);
		sql += "			(SELECT COUNT(*) AS QTDE_REVISAO FROM PROJUDI.PROC p "
				+ "				INNER JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC "
				+ "				INNER JOIN PROJUDI.PEND pe ON pe.ID_PROC = p.ID_PROC "
				+ "				INNER JOIN PROJUDI.PEND_TIPO pt ON pt.ID_PEND_TIPO = pe.ID_PEND_TIPO "
				+ "			WHERE (pe.DATA_INICIO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy')) ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND pt.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		sql += "				AND pr.ID_SERV_CARGO = ? ";
		ps.adicionarLong(idServentiaCargoResponsavel);
		sql += "			AND pr.ID_CARGO_TIPO = ? ) QTDE_REVISAO ";
		ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				relatorioSituacaoGabineteDt = new RelatorioSituacaoGabineteDt();
				relatorioSituacaoGabineteDt.setQtdeProcessosDistribuidos(rs.getString("QTDE_DISTRIBUIDOS"));
				relatorioSituacaoGabineteDt.setQtdeConclusosFinalizados(rs.getString("QTDE_CONCLUSOS_FINALIZADOS"));
				relatorioSituacaoGabineteDt.setQtdeConclusosPendentes(rs.getString("QTDE_CONCLUSOS_PENDENTES"));
				relatorioSituacaoGabineteDt.setQtdeRevisao(rs.getString("QTDE_REVISAO"));
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return relatorioSituacaoGabineteDt;
	}

	/**
	 * Método responsável por imprimir a capa do relatório de situação do gabinete.
	 * 
	 * @param idServentiaCargoResponsavel
	 *            - ID Serventia Cargo do responsável do relatório
	 * @return o relatorioSituacaoGabinete preenchido com a lista
	 * @throws Exception
	 * @author hmgodinho
	 */

	/**
	 * Método faz a consulta genérica para preenchimento da capa do relatório de
	 * situação do gabinete
	 * 
	 * @param mesInicial
	 *            - mês inicial do relatório
	 * @param anoInicial
	 *            - ano inicial do relatório
	 * @param mesFinal
	 *            - mês final do relatório
	 * @param anoFinal
	 *            - ano final do relatório
	 * @param movimentoTipoClasseCodigo
	 *            - código do movimentação tipo classe que será consultado
	 * @param idUsuarioServentiaCargo
	 *            - ID do Usuario Serventia Cargo responsavel
	 * @return quantidade de movimentos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String imprimirCapaRelatorioSituacaoGabinteGenericos(String mesInicial, String anoInicial, String mesFinal,
			String anoFinal, String movimentoTipoClasseCodigo, String idUsuarioServentiaCargo) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String retorno = "";

		String sql = "	SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.MOVI m "
				+ "			INNER JOIN PROJUDI.MOVI_TIPO mt ON mt.ID_MOVI_TIPO = m.ID_MOVI_TIPO "
				+ "			INNER JOIN PROJUDI.PROC p ON m.ID_PROC = p.ID_PROC "
				+ "			INNER JOIN PROJUDI.MOVI_TIPO_MOVI_TIPO_CLASSE mtmtc ON mtmtc.ID_MOVI_TIPO = mt.ID_MOVI_TIPO "
				+ "			INNER JOIN PROJUDI.MOVI_TIPO_CLASSE mtc ON mtc.ID_MOVI_TIPO_CLASSE = mtmtc.ID_MOVI_TIPO_CLASSE "
				+ "			WHERE m.DATA_REALIZACAO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy') ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND mtc.MOVI_TIPO_CLASSE_CODIGO = ?";
		ps.adicionarLong(movimentoTipoClasseCodigo);
		sql += "				AND m.ID_USU_REALIZADOR = ? ";
		ps.adicionarLong(idUsuarioServentiaCargo);

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				retorno = rs.getString("QUANTIDADE");
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return retorno;
	}

	/**
	 * Consulta a lista de processos distribuidos no gabinete
	 * 
	 * @param mesInicial
	 *            - mês inicial do relatório
	 * @param anoInicial
	 *            - ano inicial do relatório
	 * @param mesFinal
	 *            - mês final do relatório
	 * @param anoFinal
	 *            - ano final do relatório
	 * @param idServentiaCargoResponsavel
	 *            - ID Serventia Cargo do responsável do relatório
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	@SuppressWarnings("unchecked")
	public List consultarProcessosDistribuidosGabinete(String mesInicial, String anoInicial, String mesFinal,
			String anoFinal, String idServentiaCargoResponsavel) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaProcessos = new ArrayList();
		ProcessoDt processoDt = null;

		String sql = " SELECT p.ID_PROC AS ID_PROC, p.PROC_NUMERO as PROC_NUMERO, p.DIGITO_VERIFICADOR AS DIGITO_VERIFICADOR, "
				+ "		pt.PROC_TIPO AS PROC_TIPO, TO_CHAR(p.DATA_RECEBIMENTO, 'DD/MM/YYYY HH24:MI:SS') AS DATA_RECEBIMENTO FROM PROJUDI.PROC p "
				+ "				LEFT JOIN PROJUDI.RECURSO r ON r.ID_PROC = p.ID_PROC "
				+ "				INNER JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC "
				+ "				INNER JOIN PROJUDI.PROC_TIPO pt ON pt.ID_PROC_TIPO = p.ID_PROC_TIPO "
				+ "			WHERE (p.DATA_RECEBIMENTO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy') ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "					or r.DATA_RECEBIMENTO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy')) ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND pr.ID_SERV_CARGO = ? ";
		ps.adicionarLong(idServentiaCargoResponsavel);
		sql += "				AND pr.CODIGO_TEMP = ? ";
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		sql += "				AND pr.ID_CARGO_TIPO = ? ";
		ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		sql += "		ORDER BY p.PROC_NUMERO, p.DIGITO_VERIFICADOR ";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				processoDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				processoDt.setProcessoTipo(rs.getString("PROC_TIPO"));
				processoDt.setDataRecebimento(rs.getString("DATA_RECEBIMENTO"));
				listaProcessos.add(processoDt);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Consulta a lista de processos pendentes no gabinete
	 * 
	 * @param mesInicial
	 *            - mês inicial do relatório
	 * @param anoInicial
	 *            - ano inicial do relatório
	 * @param mesFinal
	 *            - mês final do relatório
	 * @param anoFinal
	 *            - ano final do relatório
	 * @param idServentiaCargoResponsavel
	 *            - ID Serventia Cargo do responsável do relatório
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	@SuppressWarnings("unchecked")
	public List consultarProcessosConclusosPendentesGabinete(String mesInicial, String anoInicial, String mesFinal,
			String anoFinal, String idServentiaCargoResponsavel) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaPendencias = new ArrayList();
		PendenciaDt pendenciaDt = null;

		String sql = " SELECT p.ID_PROC AS ID_PROC, p.PROC_NUMERO AS PROC_NUMERO, p.DIGITO_VERIFICADOR AS DIGITO_VERIFICADOR, pt.PEND_TIPO AS PEND_TIPO, "
				+ "		TO_CHAR(pe.DATA_INICIO, 'DD/MM/YYYY HH24:MI:SS') AS DATA_INICIO FROM PROJUDI.PROC p "
				+ "			INNER JOIN PROJUDI.PEND pe ON pe.ID_PROC = p.ID_PROC "
				+ "			INNER JOIN PROJUDI.PEND_TIPO pt ON pt.ID_PEND_TIPO = pe.ID_PEND_TIPO "
				+ "			INNER JOIN PROJUDI.PEND_RESP per ON per.ID_PEND = pe.ID_PEND "
				+ "		WHERE (pe.DATA_INICIO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy')) ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND pe.DATA_FIM IS NULL " + "				AND per.ID_SERV_CARGO = ?";
		ps.adicionarLong(idServentiaCargoResponsavel);
		sql += "		ORDER BY p.PROC_NUMERO, p.DIGITO_VERIFICADOR ";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				pendenciaDt = new PendenciaDt();
				pendenciaDt.setId_Processo(rs.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				pendenciaDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				pendenciaDt.setPendenciaTipo(rs.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(rs.getString("DATA_INICIO"));
				listaPendencias.add(pendenciaDt);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaPendencias;
	}

	/**
	 * Consulta a lista de processos conclusos finalizados no gabinete
	 * 
	 * @param mesInicial
	 *            - mês inicial do relatório
	 * @param anoInicial
	 *            - ano inicial do relatório
	 * @param mesFinal
	 *            - mês final do relatório
	 * @param anoFinal
	 *            - ano final do relatório
	 * @param idServentiaCargoResponsavel
	 *            - ID Serventia Cargo do responsável do relatório
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	@SuppressWarnings("unchecked")
	public List consultarProcessosConclusosFinalizadosGabinete(String mesInicial, String anoInicial, String mesFinal,
			String anoFinal, String idServentiaCargoResponsavel) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaPendencias = new ArrayList();
		PendenciaDt pendenciaDt = null;

		String sql = " SELECT p.ID_PROC AS ID_PROC, p.PROC_NUMERO AS PROC_NUMERO, p.DIGITO_VERIFICADOR AS DIGITO_VERIFICADOR, pt.PEND_TIPO AS PEND_TIPO, "
				+ "		TO_CHAR(pe.DATA_INICIO, 'DD/MM/YYYY HH24:MI:SS') AS DATA_INICIO, "
				+ "		TO_CHAR(pe.DATA_FIM, 'DD/MM/YYYY HH24:MI:SS') AS DATA_FIM FROM PROJUDI.PROC p "
				+ "			INNER JOIN PROJUDI.PEND pe ON pe.ID_PROC = p.ID_PROC "
				+ "			INNER JOIN PROJUDI.PEND_TIPO pt ON pt.ID_PEND_TIPO = pe.ID_PEND_TIPO "
				+ "			INNER JOIN PROJUDI.PEND_RESP per ON per.ID_PEND = pe.ID_PEND "
				+ "		WHERE (pe.DATA_INICIO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy')) ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND pe.DATA_FIM IS NOT NULL " + "				AND per.ID_SERV_CARGO = ?";
		ps.adicionarLong(idServentiaCargoResponsavel);
		sql += "		ORDER BY p.PROC_NUMERO, p.DIGITO_VERIFICADOR ";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				pendenciaDt = new PendenciaDt();
				pendenciaDt.setId_Processo(rs.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				pendenciaDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				pendenciaDt.setPendenciaTipo(rs.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(rs.getString("DATA_INICIO"));
				pendenciaDt.setDataFim(rs.getString("DATA_FIM"));
				listaPendencias.add(pendenciaDt);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaPendencias;
	}

	/**
	 * Consulta a lista de processos em revisão no gabinete
	 * 
	 * @param mesInicial
	 *            - mês inicial do relatório
	 * @param anoInicial
	 *            - ano inicial do relatório
	 * @param mesFinal
	 *            - mês final do relatório
	 * @param anoFinal
	 *            - ano final do relatório
	 * @param idServentiaCargoResponsavel
	 *            - ID Serventia Cargo do responsável do relatório
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	@SuppressWarnings("unchecked")
	public List consultarProcessosRevisaoGabinete(String mesInicial, String anoInicial, String mesFinal,
			String anoFinal, String idServentiaCargoResponsavel) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaPendencias = new ArrayList();
		PendenciaDt pendenciaDt = null;

		String sql = " SELECT p.ID_PROC AS ID_PROC, p.PROC_NUMERO AS PROC_NUMERO, p.DIGITO_VERIFICADOR AS DIGITO_VERIFICADOR, pt.PEND_TIPO AS PEND_TIPO, "
				+ "			TO_CHAR(pe.DATA_INICIO, 'DD/MM/YYYY HH24:MI:SS') AS DATA_INICIO FROM PROJUDI.PROC p "
				+ "			INNER JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC "
				+ "			INNER JOIN PROJUDI.PEND pe ON pe.ID_PROC = p.ID_PROC "
				+ "			INNER JOIN PROJUDI.PEND_TIPO pt ON pt.ID_PEND_TIPO = pe.ID_PEND_TIPO "
				+ "		WHERE (pe.DATA_INICIO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy')) ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND pt.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		sql += "				AND pr.ID_SERV_CARGO = ? ";
		ps.adicionarLong(idServentiaCargoResponsavel);
		sql += "				AND pr.CODIGO_TEMP = ? ";
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		sql += "			AND pr.ID_CARGO_TIPO = ? ";
		ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
		sql += "		ORDER BY p.PROC_NUMERO, p.DIGITO_VERIFICADOR ";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				pendenciaDt = new PendenciaDt();
				pendenciaDt.setId_Processo(rs.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				pendenciaDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				pendenciaDt.setPendenciaTipo(rs.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(rs.getString("DATA_INICIO"));
				listaPendencias.add(pendenciaDt);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaPendencias;
	}

	/**
	 * Método que consulta a estatística genérica (baseada na
	 * MovimentacaoTipoClasse) do gabinete
	 * 
	 * @param mesInicial
	 *            - mês inicial do relatório
	 * @param anoInicial
	 *            - ano inicial do relatório
	 * @param mesFinal
	 *            - mês final do relatório
	 * @param anoFinal
	 *            - ano final do relatório
	 * @param idUsuarioServentiaResponsavel
	 *            - ID do Usuario Serventia Responsavel
	 * @param idMovimentacaoTipoClasse
	 *            - ID da MovimentacaoTipoClasse que será consultada
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	@SuppressWarnings("unchecked")
	public List consultarEstatisticaGenericaGabinete(String mesInicial, String anoInicial, String mesFinal,
			String anoFinal, String idUsuarioServentiaResponsavel, String idMovimentacaoTipoClasse) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaPendencias = new ArrayList();
		PendenciaDt pendenciaDt = null;

		String sql = " SELECT p.ID_PROC AS ID_PROC, p.PROC_NUMERO AS PROC_NUMERO, p.DIGITO_VERIFICADOR AS DIGITO_VERIFICADOR, mt.MOVI_TIPO as MOVI_TIPO, "
				+ "				TO_CHAR(m.DATA_REALIZACAO, 'DD/MM/YYYY HH24:MI:SS') AS DATA_REALIZACAO FROM PROJUDI.MOVI m "
				+ "			INNER JOIN PROJUDI.MOVI_TIPO mt ON mt.ID_MOVI_TIPO = m.ID_MOVI_TIPO "
				+ "			INNER JOIN PROJUDI.PROC p ON m.ID_PROC = p.ID_PROC "
				+ "			INNER JOIN PROJUDI.MOVI_TIPO_MOVI_TIPO_CLASSE mtmtc ON mtmtc.ID_MOVI_TIPO = mt.ID_MOVI_TIPO "
				+ "			INNER JOIN PROJUDI.MOVI_TIPO_CLASSE mtc ON mtc.ID_MOVI_TIPO_CLASSE = mtmtc.ID_MOVI_TIPO_CLASSE "
				+ "		WHERE (m.DATA_REALIZACAO BETWEEN to_date(?, 'mm/yyyy') and to_date(?, 'mm/yyyy')) ";
		ps.adicionarString(mesInicial + "/" + anoInicial);
		ps.adicionarString(mesFinal + "/" + anoFinal);
		sql += "				AND mtc.MOVI_TIPO_CLASSE_CODIGO = ? ";
		ps.adicionarLong(idMovimentacaoTipoClasse);
		sql += "				AND m.ID_USU_REALIZADOR = ? ";
		ps.adicionarLong(idUsuarioServentiaResponsavel);
		sql += "		ORDER BY p.PROC_NUMERO, p.DIGITO_VERIFICADOR, m.DATA_REALIZACAO ";

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				pendenciaDt = new PendenciaDt();
				pendenciaDt.setId_Processo(rs.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				pendenciaDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				pendenciaDt.setPendenciaTipo(rs.getString("MOVI_TIPO"));
				pendenciaDt.setDataInicio(rs.getString("DATA_REALIZACAO"));
				listaPendencias.add(pendenciaDt);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaPendencias;
	}

	/**
	 * Método responsável por consultar a situação do ponteiro de distribuição.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da Área de Distribuição
	 * @param dataVerificacao
	 *            - data de início da verificação
	 * @return lista com os índices da situação do ponteiro.
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List verificarSituacaoPonteiroDistribuicaoAreaTurma(String idAreaDistribuicao, Date dataInicial,
			Date dataFinal) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" SELECT s1.serv, tab2.Qtd FROM " + "		(SELECT tab.id_serv, COUNT(1) as qtd FROM "
				+ "			(SELECT s.id_serv " + "				FROM projudi.proc p "
				+ "				INNER JOIN projudi.serv s ON p.id_serv = s.id_serv "
				+ "				INNER JOIN projudi.serv_area_dist sad ON s.id_serv = sad.id_serv "
				+ "				INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
				+ "				INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
				+ "				INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
				+ "				INNER JOIN PROJUDI.grupo g ON ct.id_grupo = g.id_grupo "
				+ "				INNER JOIN PROJUDI.grupo_tipo gt ON gt.id_grupo_tipo = g.id_grupo_tipo "
				+ "				WHERE sad.id_area_dist = ?");
		ps.adicionarLong(idAreaDistribuicao);
		sql.append(" 				AND p.id_proc_fase <> ? ");
		ps.adicionarLong(ProcessoFaseDt.RECURSO);
		sql.append("				AND pr.redator = ?");
		ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
		sql.append("				AND gt.grupo_tipo_codigo = ? ");
		ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
		sql.append("				AND p.data_recebimento BETWEEN ? AND ? ");
		ps.adicionarDateTime(dataInicial);
		ps.adicionarDateTime(dataFinal);
		sql.append("UNION ALL " + "		SELECT s.id_serv " + "			FROM projudi.recurso r "
				+ "			INNER JOIN projudi.proc p ON p.id_proc = r.id_proc "
				+ "			INNER JOIN projudi.serv s ON r.id_serv_recurso = s.id_serv "
				+ "			INNER JOIN projudi.serv_area_dist sad on s.id_serv = sad.id_serv "
				+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
				+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
				+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
				+ "			INNER JOIN projudi.grupo g on ct.id_grupo = g.id_grupo "
				+ "			INNER JOIN projudi.grupo_tipo gt on gt.id_grupo_tipo = g.id_grupo_tipo "
				+ "			WHERE sad.id_area_dist = ? ");
		ps.adicionarLong(idAreaDistribuicao);
		sql.append(
				"				AND ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
		ps.adicionarDateTime(dataInicial);
		sql.append(
				"					AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc))");
		ps.adicionarDateTime(dataFinal);
		sql.append(
				" 			AND r.id_recurso = (SELECT MAX(r.id_recurso) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
		sql.append("				AND pr.redator = ?");
		ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
		sql.append("				AND gt.grupo_tipo_codigo = ? ");
		ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
		sql.append("UNION ALL " + "		SELECT s.id_serv " + "			FROM projudi.recurso r "
				+ "			INNER JOIN projudi.proc p ON p.id_proc = r.id_proc "
				+ "			INNER JOIN projudi.serv s ON r.id_serv_origem = s.id_serv "
				+ "			INNER JOIN projudi.serv_area_dist sad on s.id_serv = sad.id_serv "
				+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
				+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
				+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
				+ "			INNER JOIN projudi.grupo g on ct.id_grupo = g.id_grupo "
				+ "			INNER JOIN projudi.grupo_tipo gt on gt.id_grupo_tipo = g.id_grupo_tipo "
				+ "			WHERE sad.id_area_dist = ? ");
		ps.adicionarLong(idAreaDistribuicao);
		sql.append(
				"				AND ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
		ps.adicionarDateTime(dataInicial);
		sql.append(
				"					AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc))");
		ps.adicionarDateTime(dataFinal);
		sql.append(
				" 			AND r.id_recurso = (SELECT MAX(r.id_recurso) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
		sql.append("				AND pr.redator = ?");
		ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
		sql.append("				AND gt.grupo_tipo_codigo = ? ");
		ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
		sql.append("		) tab  " + "		GROUP BY tab.id_serv " + "		) tab2 "
				+ "	INNER JOIN projudi.serv s1 on tab2.id_serv = s1.id_serv " + "	ORDER BY tab2.id_serv ");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioPonteiroDistribuicaoDt obTemp = new RelatorioPonteiroDistribuicaoDt();
				obTemp.setNomeServentia(rs.getString("SERV"));
				obTemp.setQuantidade(rs.getLong("QTD"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Relatório que informa a situação do ponteiro de distribuição de processos na
	 * área de distribuição com serventias consideradas do tipo 1º grau.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param dataVerificacao
	 *            - data final do ponteiro de verificação calculada
	 * @return - lista de registos do ponteiro
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List verificarSituacaoPonteiroDistribuicaoAreaDistribuicao(String idAreaDistribuicao, Date dataVerificacao)
			throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" SELECT s.serv, tab.quantidade " + "		"
				+ "		FROM ( "
				+ "				SELECT pl.id_serv, SUM(qtd) AS quantidade " 
				+ "					FROM ponteiro_log pl "
				+ "				 	WHERE pl.id_area_dist = ? ");
		ps.adicionarLong(idAreaDistribuicao);
		sql.append("					AND pl.data <= ? ");
		ps.adicionarDateTime(dataVerificacao);
		sql.append("					AND pl.id_serv IN (SELECT id_serv FROM serv_area_dist where id_area_dist = ?)");
		ps.adicionarLong(idAreaDistribuicao);
		sql.append("				GROUP BY pl.id_serv " + "			) tab "
				+ "		INNER JOIN serv s ON s.id_serv = tab.id_serv " 
				+ "		ORDER BY s.serv ");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioPonteiroDistribuicaoDt obTemp = new RelatorioPonteiroDistribuicaoDt();
				obTemp.setNomeServentia(rs.getString("SERV"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Relatório que lista os processos distribuídos pelo ponteiro de distribuição
	 * na área de distribuição com serventias consideradas do tipo 1º grau.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param dataInicial
	 *            - data inicial do ponteiro de verificação calculada
	 * @param dataFinal
	 *            - data final do ponteiro de verificação calculada
	 * @return - lista de registos do ponteiro
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List listarProcessosPonteiroDistribuicaoAreaDistribuicao(String idAreaDistribuicao, Date dataInicial,
			Date dataFinal) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(
				" SELECT tab.id_proc, TO_CHAR(tab.data, 'DD/MM/YYYY HH24:mi:ss') AS DATA_RECEBIMENTO, s.serv AS SERV, p.proc_numero AS PROC_NUMERO, p.digito_verificador AS DIGITO_VERIFICADOR, tab.qtd AS QUANTIDADE, plt.ponteiro_log_tipo AS TIPO "
						+ "		FROM ( "
						+ "				SELECT pl.id_proc, pl.id_serv, pl.id_ponteiro_log_tipo, pl.data, pl.qtd "
						+ "					FROM ponteiro_log pl " + "					WHERE  pl.id_area_dist = ? ");
		ps.adicionarLong(idAreaDistribuicao);
		sql.append("        			AND pl.data BETWEEN ? AND ? ");
		ps.adicionarDateTime(dataInicial);
		ps.adicionarDateTime(dataFinal);
		sql.append("	) tab " + "	LEFT JOIN proc p ON p.id_proc = tab.id_proc "
				+ "	INNER JOIN serv s ON s.id_serv = tab.id_serv "
				+ "	INNER JOIN ponteiro_log_tipo plt ON plt.id_ponteiro_log_tipo = tab.id_ponteiro_log_tipo "
				+ "	ORDER BY tab.data asc ");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioPonteiroDistribuicaoDt obTemp = new RelatorioPonteiroDistribuicaoDt();
				obTemp.setDataRecebimento(rs.getString("DATA_RECEBIMENTO"));
				if (rs.getString("PROC_NUMERO") != null) {
					obTemp.setNumeroProcesso(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR"));
				} else {
					obTemp.setNumeroProcesso(rs.getString("QUANTIDADE"));
				}
				obTemp.setNomeServentia(rs.getString("SERV"));
				obTemp.setDistribuicaoTipo(rs.getString("TIPO"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
				e.getMessage();
			}
		}
		return listaProcessos;
	}

	/**
	 * Relatório que informa a situação do ponteiro de distribuição de processos na
	 * serventia considerada do tipo 2º grau.
	 * 
	 * @param idServentia
	 *            - ID da Serventia
	 * @param dataReferencia
	 *            - data de referência informada
	 * @return - lista de registos do ponteiro
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List verificarSituacaoPonteiroDistribuicaoResponsavel(String idServentia, String idAreaDistribuicao,
			Date dataReferencia) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" SELECT s.serv AS SERV, u.nome AS NOME, sc.serv_cargo AS SERV_CARGO, tab.Qtd AS QUANTIDADE ");
		sql.append("		FROM (SELECT pl.id_serv, pl.id_serv_cargo, SUM(qtd) AS qtd ");
		sql.append("				FROM ponteiro_log pl 			WHERE pl.id_serv = ?");							ps.adicionarLong(idServentia);
		sql.append("				AND pl.id_area_dist = ? ");														ps.adicionarLong(idAreaDistribuicao);
		sql.append("				AND pl.data <= ? ");															ps.adicionarDateTime(dataReferencia);
		sql.append("			GROUP BY pl.id_serv, pl.id_serv_cargo 		) tab ");
		sql.append("	INNER JOIN serv s on tab.id_serv = s.id_serv ");
		sql.append("    INNER JOIN SERV_RELACIONADA SR ON SR.ID_SERV_PRINC = S.ID_SERV AND SR.RECEBE_PROC= ? "); 	ps.adicionarLong(1);
		sql.append("	INNER JOIN serv_cargo sc on tab.id_serv_cargo = sc.id_serv_cargo and sc.id_Serv = sr.id_serv_rel ");
		sql.append("	INNER JOIN usu_serv_grupo usg on usg.id_usu_serv_grupo = sc.id_usu_serv_grupo ");
		sql.append("	INNER JOIN usu_serv us on us.id_usu_serv = usg.id_usu_serv ");
		sql.append("	INNER JOIN usu u on us.id_usu = u.id_usu where sc.quantidade_dist > ? ");					ps.adicionarLong(0);
		sql.append("	ORDER BY nome");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioPonteiroDistribuicaoDt obTemp = new RelatorioPonteiroDistribuicaoDt();
				obTemp.setNomeServentia(rs.getString("SERV"));
				obTemp.setNomeResponsavel(rs.getString("NOME"));
				obTemp.setServCargoResponsavel(rs.getString("SERV_CARGO"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaProcessos.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Relatório que lista os processos distribuídos pelo ponteiro de distribuição
	 * na serventia considerada do tipo 2º grau.
	 * 
	 * @param idServentia
	 *            - ID da Serventia
	 * @param dataPonteiro
	 *            - data do ponteiro de verificação calculada
	 * @return - lista de registos do ponteiro
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List listarProcessosPonteiroDistribuicaoResponsavel(String idAreaDistribuicao, String idServentia,
			Date dataInicial, Date dataFinal) throws Exception {

		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(
				"SELECT TO_CHAR(tab.data, 'DD/MM/YYYY HH24:mi:ss') AS DATA_RECEBIMENTO, u.nome AS NOME, sc.serv_cargo AS SERV_CARGO,  p.proc_numero AS PROC_NUMERO, p.digito_verificador AS DIGITO_VERIFICADOR, "
						+ "		CASE tab.id_ponteiro_log_tipo WHEN 1 THEN 'Distribuição' WHEN 2 THEN 'Redistribuição' WHEN 4 THEN 'Ganho de Responsabilidade' WHEN 5 THEN 'Perda de Responsabilidade' END AS TIPO "
						+ "		FROM (SELECT pl.data, pl.id_serv_cargo, pl.id_proc, pl.id_ponteiro_log_tipo "
						+ "				FROM ponteiro_log pl "
						+ "				WHERE pl.id_ponteiro_log_tipo IN (?,?,?,?) ");
		ps.adicionarLong(PonteiroLogTipoDt.DISTRIBUICAO);
		ps.adicionarLong(PonteiroLogTipoDt.REDISTRIBUICAO);
		ps.adicionarLong(PonteiroLogTipoDt.GANHO_RESPONSABILIDADE);
		ps.adicionarLong(PonteiroLogTipoDt.PERDA_RESPONSABILIDADE);
		sql.append("        		AND pl.id_proc IS NOT NULL 					AND pl.id_area_dist = ?");				ps.adicionarLong(idAreaDistribuicao);
		sql.append("				AND pl.id_serv = ? ");																ps.adicionarLong(idServentia);
		sql.append("				AND pl.data BETWEEN ? AND ? ");														ps.adicionarDateTime(dataInicial);		ps.adicionarDateTime(dataFinal);
		sql.append("			ORDER BY pl.data asc 			) tab "
				+ "	INNER JOIN proc p ON p.id_proc = tab.id_proc "
				+ "	INNER JOIN serv_cargo sc on tab.id_serv_cargo = sc.id_serv_cargo "
				+ "	INNER JOIN usu_serv_grupo usg on usg.id_usu_serv_grupo = sc.id_usu_serv_grupo "
				+ " 	INNER JOIN usu_serv us on us.id_usu_serv = usg.id_usu_serv "
				+ "	INNER JOIN usu u on us.id_usu = u.id_usu " + "	ORDER BY tab.data asc");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioPonteiroDistribuicaoDt obTemp = new RelatorioPonteiroDistribuicaoDt();
				obTemp.setDataRecebimento(rs.getString("DATA_RECEBIMENTO"));
				obTemp.setNomeResponsavel(rs.getString("NOME"));
				obTemp.setServCargoResponsavel(rs.getString("SERV_CARGO"));
				obTemp.setNumeroProcesso(rs.getString("PROC_NUMERO"));
				obTemp.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				obTemp.setDistribuicaoTipo(rs.getString("TIPO"));
				listaProcessos.add(obTemp);
			}
		} catch (Exception e) {
			e.getMessage();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método para acompanhar a distribuição de processos dentro da área de
	 * distribuição (para serventias de 1º Grau) nos dias informados, sendo a
	 * consulta realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param datas
	 *            - lista com os dias que serão consultados.
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public Collection acompanharProcessosDistribuidosAreaDistribuicao(String idAreaDistribuicao, List datas)
			throws Exception {

		HashMap mapRelatorio = new HashMap();

		ResultSetTJGO rs = null;
		StringBuffer sql = null;
		PreparedStatementTJGO ps = null;

		Date dataInicial, dataFinal;
		for (int i = 0; i < datas.size(); i++) {
			sql = new StringBuffer();
			ps = new PreparedStatementTJGO();
			dataInicial = Funcoes.calculePrimeraHora((Date) datas.get(i));
			dataFinal = Funcoes.calculeUltimaHora((Date) datas.get(i));

			sql.append(" SELECT s.serv AS SERV, " + "		(NVL((SELECT COUNT(1) "
					+ "				FROM ponteiro_log pl " + "				WHERE pl.id_ponteiro_log_tipo = ? ");
			ps.adicionarLong(PonteiroLogTipoDt.DISTRIBUICAO);
			sql.append("				AND pl.id_proc IS NOT NULL " + "					AND s.id_serv = pl.id_serv "
					+ "					AND pl.id_area_dist = ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append(" 				AND pl.data BETWEEN ? AND ? ");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
			sql.append("        ),0) " + "		) as Distribuicao, " + "		(NVL((SELECT SUM(pl.qtd) "
					+ "				FROM ponteiro_log pl " + "				WHERE pl.id_ponteiro_log_tipo = ? ");
			ps.adicionarLong(PonteiroLogTipoDt.REDISTRIBUICAO);
			sql.append("				AND pl.id_proc IS NOT NULL " + "					AND s.id_serv = pl.id_serv "
					+ "					AND pl.id_area_dist = ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append(" 				AND pl.data BETWEEN ? AND ? ");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
			sql.append("        ),0) " + "		) as Redistribuicao, " + "		(NVL((SELECT SUM(pl.qtd) "
					+ "				FROM ponteiro_log pl " + "				WHERE pl.id_ponteiro_log_tipo = ? ");
			ps.adicionarLong(PonteiroLogTipoDt.CADASTRO);
			sql.append("				AND s.id_serv = pl.id_serv " + "					AND pl.id_area_dist = ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append(" 				AND pl.data BETWEEN ? AND ? ");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
			sql.append("        ),0) " + "		) as Cadastro " + "		FROM serv s "
					+ " INNER JOIN serv_area_dist sad on s.id_serv = sad.id_serv " + " WHERE sad.id_area_dist = ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append(" ORDER BY s.serv");

			try {
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {

					if (mapRelatorio.containsKey(rs.getString("SERV"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("SERV"));
						obTemp.setRedistribuicao(i, rs.getLong("REDISTRIBUICAO"));
						obTemp.setDistribuicao(i, rs.getLong("DISTRIBUICAO"));
						obTemp.setCadastro(i, rs.getLong("CADASTRO"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("SERV"));
						obTemp.setRedistribuicao(i, rs.getLong("REDISTRIBUICAO"));
						obTemp.setDistribuicao(i, rs.getLong("DISTRIBUICAO"));
						obTemp.setCadastro(i, rs.getLong("CADASTRO"));
						mapRelatorio.put(rs.getString("SERV"), obTemp);
					}
				}
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
				}
			}
		}
		return mapRelatorio.values();
	}

	public Collection acompanharProcessosDistribuidosResponsavel(String idAreaDistribuicao, String idServentia,
			List datas) throws Exception {

		HashMap mapRelatorio = new HashMap();

		ResultSetTJGO rs = null;
		StringBuffer sql = null;
		PreparedStatementTJGO ps = null;

		Date dataInicial, dataFinal;
		for (int i = 0; i < datas.size(); i++) {
			sql = new StringBuffer();
			ps = new PreparedStatementTJGO();
			dataInicial = Funcoes.calculePrimeraHora((Date) datas.get(i));
			dataFinal = Funcoes.calculeUltimaHora((Date) datas.get(i));

			sql.append(
					" SELECT u.nome AS NOME, sc.SERV_CARGO AS SERV_CARGO, tab.distribuicao, tab.redistribuicao, tab.ganho_responsabilidade, tab.perda_responsabilidade, tab.cadastro "
							+ "		FROM (SELECT DISTINCT pl.id_serv_cargo, "
							+ "					(NVL((SELECT COUNT(1) "
							+ "							FROM ponteiro_log pl "
							+ "							WHERE pl.id_ponteiro_log_tipo = ? ");
			ps.adicionarLong(PonteiroLogTipoDt.DISTRIBUICAO);
			sql.append("							AND pl.id_proc IS NOT NULL "
					+ "								AND pl.id_area_dist = ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append("							AND pl.id_serv = ? ");
			ps.adicionarLong(idServentia);
			sql.append("							AND pl.data BETWEEN ? AND ? ");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
			sql.append("							AND sc.id_serv_cargo = pl.id_serv_cargo "
					+ "						),0) " + "					) as DISTRIBUICAO, "
					+ "					(NVL((SELECT COUNT(1) " + "							FROM ponteiro_log pl "
					+ "							WHERE pl.id_ponteiro_log_tipo = ? ");
			ps.adicionarLong(PonteiroLogTipoDt.REDISTRIBUICAO);
			sql.append("							AND pl.id_proc IS NOT NULL "
					+ "								AND pl.id_area_dist= ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append("							AND pl.id_serv = ? ");
			ps.adicionarLong(idServentia);
			sql.append("							AND pl.data BETWEEN ? AND ? ");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
			sql.append("							AND sc.id_serv_cargo = pl.id_serv_cargo "
					+ "						),0) " + "					) as REDISTRIBUICAO, "
					+ "					(NVL((SELECT COUNT(1) " + "							FROM ponteiro_log pl "
					+ "							WHERE pl.id_ponteiro_log_tipo = ? ");
			ps.adicionarLong(PonteiroLogTipoDt.GANHO_RESPONSABILIDADE);
			sql.append("							AND pl.id_proc IS NOT NULL "
					+ "								AND pl.id_area_dist = ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append("							AND pl.id_serv = ? ");
			ps.adicionarLong(idServentia);
			sql.append("							AND pl.data BETWEEN ? AND ? ");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
			sql.append("							AND sc.id_serv_cargo = pl.id_serv_cargo "
					+ "						),0) " + "					) as GANHO_RESPONSABILIDADE, "
					+ "					(NVL((SELECT COUNT(1) " + "							FROM ponteiro_log pl "
					+ "							WHERE pl.id_ponteiro_log_tipo = ? ");
			ps.adicionarLong(PonteiroLogTipoDt.PERDA_RESPONSABILIDADE);
			sql.append("							AND pl.id_proc IS NOT NULL "
					+ "								AND pl.id_area_dist = ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append("							AND pl.id_serv = ? ");
			ps.adicionarLong(idServentia);
			sql.append("							AND pl.data BETWEEN ? AND ? ");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
			sql.append("							AND sc.id_serv_cargo = pl.id_serv_cargo "
					+ "						),0) " + "					) as PERDA_RESPONSABILIDADE, "
					+ "					(NVL((SELECT SUM(pl.qtd) " + "							FROM ponteiro_log pl "
					+ "							WHERE pl.id_ponteiro_log_tipo = ? ");
			ps.adicionarLong(PonteiroLogTipoDt.CADASTRO);
			sql.append("							AND pl.id_proc IS NULL "
					+ "								AND pl.id_area_dist = ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append("							AND pl.id_serv = ? ");
			ps.adicionarLong(idServentia);
			sql.append("							AND pl.data BETWEEN ? AND ? ");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
			sql.append("							AND sc.id_serv_cargo = pl.id_serv_cargo "
					+ "						),0) " + "					) as CADASTRO "
					+ "				FROM ponteiro_log pl "
					+ "				INNER JOIN serv_cargo sc ON sc.id_serv_cargo = pl.id_serv_cargo "
					+ "				WHERE sc.quantidade_dist > ? ");
			ps.adicionarLong(0);
			sql.append("					AND pl.id_serv = ? ");
			ps.adicionarLong(idServentia);
			sql.append("					AND pl.data BETWEEN ? AND ? ");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
			sql.append("					AND pl.id_area_dist = ? ");
			ps.adicionarLong(idAreaDistribuicao);
			sql.append("		) tab" + "		INNER JOIN serv_cargo sc on sc.id_serv_cargo = tab.id_serv_cargo "
					+ "		INNER JOIN usu_serv_grupo usg on usg.id_usu_serv_grupo = sc.id_usu_serv_grupo "
					+ "		INNER JOIN usu_serv us on us.id_usu_serv = usg.id_usu_serv "
					+ "		INNER JOIN usu u on us.id_usu = u.id_usu " + "	ORDER BY nome");

			try {
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {
					if (mapRelatorio.containsKey(rs.getString("NOME") + " - " + rs.getString("SERV_CARGO"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("NOME") + " - " + rs.getString("SERV_CARGO"));
						obTemp.setRedistribuicao(i, rs.getLong("REDISTRIBUICAO"));
						obTemp.setDistribuicao(i, rs.getLong("DISTRIBUICAO"));
						obTemp.setGanhoResponsabilidade(i, rs.getLong("GANHO_RESPONSABILIDADE"));
						obTemp.setPerdaResponsabilidade(i, rs.getLong("PERDA_RESPONSABILIDADE"));
						obTemp.setCadastro(i, rs.getLong("CADASTRO"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("NOME") + " - " + rs.getString("SERV_CARGO"));
						obTemp.setRedistribuicao(i, rs.getLong("REDISTRIBUICAO"));
						obTemp.setDistribuicao(i, rs.getLong("DISTRIBUICAO"));
						obTemp.setGanhoResponsabilidade(i, rs.getLong("GANHO_RESPONSABILIDADE"));
						obTemp.setPerdaResponsabilidade(i, rs.getLong("PERDA_RESPONSABILIDADE"));
						obTemp.setCadastro(i, rs.getLong("CADASTRO"));
						mapRelatorio.put(rs.getString("NOME") + " - " + rs.getString("SERV_CARGO"), obTemp);
					}
				}
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
				}
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método para acompanhar a situação do ponteiro de distribuição dentro da área
	 * de distribuição (para serventias de 1º grau) nos dias informados, sendo a
	 * consulta realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param listaDiasIniciais
	 *            - lista de data inicial do ponteiro
	 * @param listaDiasFinais
	 *            - lista de data final do ponteiro
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public Collection acompanharSituacaoPonteiroDistribuicaoAreaDistribuicao(String idAreaDistribuicao, List listaDiasFinais) throws Exception {

		HashMap mapRelatorio = new HashMap();
		StringBuffer sql = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = null;

		for (int j = 0; j < listaDiasFinais.size(); j++) {
			sql = new StringBuffer();
			ps = new PreparedStatementTJGO();

			sql.append(" SELECT s.serv, tab.quantidade " + "		FROM ( "
					+ "				SELECT pl.id_serv, SUM(qtd) AS quantidade "
					+ "					FROM ponteiro_log pl " + "			 		WHERE pl.id_area_dist = ? ");	ps.adicionarLong(idAreaDistribuicao);
			sql.append("					AND data <= ? ");														ps.adicionarDateTime((Date) listaDiasFinais.get(j));
			sql.append("				GROUP BY pl.id_serv " + "			) tab "
					+ "		INNER JOIN serv s ON s.id_serv = tab.id_serv " + "		ORDER BY s.serv ");

			try {
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {
					if (mapRelatorio.containsKey(rs.getString("SERV"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("SERV"));
						obTemp.setQuantidade(j, rs.getLong("QUANTIDADE"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("SERV"));
						obTemp.setQuantidade(j, rs.getLong("QUANTIDADE"));
						mapRelatorio.put(rs.getString("SERV"), obTemp);
					}
				}

			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
				}
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método para acompanhar a situação do ponteiro de distribuição dentro da área
	 * de distribuição (para serventias de 1º grau) nos dias informados, sendo a
	 * consulta realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param listaDiasIniciais
	 *            - lista de data inicial do ponteiro
	 * @param listaDiasFinais
	 *            - lista de data final do ponteiro
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public Collection acompanharSituacaoPonteiroDistribuicaoResponsavel(String idAreaDistribuicao, String idServentia, List listaDiasFinais) throws Exception {

		HashMap mapRelatorio = new HashMap();
		StringBuffer sql = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = null;

		for (int j = 0; j < listaDiasFinais.size(); j++) {
			sql = new StringBuffer();
			ps = new PreparedStatementTJGO();

			sql.append(" SELECT u.nome AS NOME, sc.serv_cargo AS SERV_CARGO, tab.quantidade AS QUANTIDADE ");
			sql.append("		FROM (SELECT pl.id_serv, pl.id_serv_cargo, SUM(qtd) AS quantidade ");
			sql.append("				FROM ponteiro_log pl 				WHERE pl.id_serv = ?");							ps.adicionarLong(idServentia);
			sql.append("				AND pl.id_area_dist = ? ");															ps.adicionarLong(idAreaDistribuicao);
			sql.append("				AND pl.data <= ? ");																ps.adicionarDateTime((Date) listaDiasFinais.get(j));
			sql.append("			GROUP BY pl.id_serv, pl.id_serv_cargo 			) tab ");
			sql.append("	INNER JOIN serv s on tab.id_serv = s.id_serv ");
			sql.append("    INNER JOIN SERV_RELACIONADA SR ON SR.ID_SERV_PRINC = S.ID_SERV AND SR.RECEBE_PROC= ? "); 		ps.adicionarLong(1);
			sql.append("	INNER JOIN serv_cargo sc on tab.id_serv_cargo = sc.id_serv_cargo and sc.id_Serv = sr.id_serv_rel ");			
			sql.append("	INNER JOIN usu_serv_grupo usg on usg.id_usu_serv_grupo = sc.id_usu_serv_grupo ");
			sql.append("	INNER JOIN usu_serv us on us.id_usu_serv = usg.id_usu_serv ");
			sql.append("	INNER JOIN usu u on us.id_usu = u.id_usu 	WHERE sc.quantidade_dist > ? ");				ps.adicionarLong(0);
			sql.append("	ORDER BY nome");

			try {
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {
					if (mapRelatorio.containsKey(rs.getString("NOME") + " - " + rs.getString("SERV_CARGO"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("NOME") + " - " + rs.getString("SERV_CARGO"));
						obTemp.setQuantidade(j, rs.getLong("QUANTIDADE"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("NOME") + " - " + rs.getString("SERV_CARGO"));
						obTemp.setQuantidade(j, rs.getLong("QUANTIDADE"));
						mapRelatorio.put(rs.getString("NOME") + " - " + rs.getString("SERV_CARGO"), obTemp);
					}
				}

			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (Exception e) {
				}
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método responsável pela consulta de advogados de outros estados que tem mais
	 * processos que o permitido pela OAB-GO.
	 * 
	 * @return lista de advogados com excesso de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarAdvogadosProcessosOutrosEstados() throws Exception {

		List listaAdvogados = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Calendar cal = Calendar.getInstance();
		String anoAtual = String.valueOf(cal.get(Calendar.YEAR));

		sql.append(
				" SELECT u.nome AS NOME, uso.oab_numero || '/' || uso.oab_complemento || ' - ' || e.UF AS SERV, tab.qtde AS QTDE "
						+ "	FROM ( "
						+ "	SELECT u.id_usu AS ID_USU, s.id_estado_representacao AS ID_ESTADO, uso.id_usu_serv_oab AS ID_USU_SERV_OAB, count(p.id_proc) AS QTDE "
						+ "		FROM projudi.proc p "
						+ "		INNER JOIN projudi.proc_parte pp ON pp.id_proc = p.id_proc "
						+ "		INNER JOIN projudi.proc_parte_advogado ppa ON ppa.id_proc_parte = pp.id_proc_parte "
						+ "		INNER JOIN projudi.usu_serv us ON us.id_usu_serv = ppa.id_usu_serv "
						+ "		INNER JOIN projudi.usu u ON u.id_usu = us.id_usu "
						+ "		INNER JOIN projudi.serv s ON s.id_serv = us.id_serv "
						+ "		INNER JOIN projudi.usu_serv_oab uso ON us.id_usu_serv = uso.id_usu_serv"
						+ "		WHERE p.data_recebimento BETWEEN ? AND ? ");
		ps.adicionarDateTime(Funcoes.getPrimeiroDiaHoraMinutoSegundoAno(anoAtual));
		ps.adicionarDateTime(Funcoes.getUltimoDiaHoraMinutoSegundoMes("12", anoAtual));

		sql.append(
				" 		AND s.serv_codigo IN (709,710,711,712,713,714,715,716,718,719,720,721,722,723,724,725,726,727,728,729,730,731,732,733,734,735)	"
						+ "		GROUP BY u.id_usu, s.id_estado_representacao, uso.id_usu_serv_oab " + "	) TAB "
						+ "	INNER JOIN projudi.usu u ON u.id_usu = tab.id_usu"
						+ "	INNER JOIN projudi.estado e ON e.id_estado = tab.id_estado "
						+ "	INNER JOIN projudi.usu_serv_oab uso ON uso.id_usu_serv_oab = tab.id_usu_serv_oab "
						+ "	WHERE tab.qtde > ?");
		ps.adicionarLong(Configuracao.QUANTIDADE_MAXIMA_PROCESSOS_ADVOGADOS_OUTRAS_OAB);
		sql.append(" ORDER BY tab.qtde desc ");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioAdvogadosProcessosOutrosEstadosDt obTemp = new RelatorioAdvogadosProcessosOutrosEstadosDt();
				obTemp.setNomeAdvogado(rs.getString("NOME"));
				obTemp.setEstadoAdvogado(rs.getString("SERV"));
				obTemp.setQuantidadeProcessos(rs.getInt("QTDE"));
				listaAdvogados.add(obTemp);
			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaAdvogados;
	}

	/**
	 * Método de geração do relatório sumário de recursos repetitivos.
	 * 
	 * @param dataInicial
	 *            - data inicial de sobrestamento
	 * @param dataFinal
	 *            - data final de sobrestamento
	 * @param idTema
	 *            - ID do tema
	 * @return lista de recursos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List relSumarioRecursoRepetitivo(String dataInicial, String dataFinal, String idTema) throws Exception {
		List listaRecursos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(
				" SELECT t.titulo AS RECURSO, t.ques_direito AS QUESTAO_DIREITO, t.tema_codigo || '/' || teo.tema_origem AS TEMA_CODIGO, tab.QTDE AS QUANTIDADE "
						+ "		FROM ( SELECT pt.id_tema AS ID_TEMA, COUNT(pt.id_proc_tema) AS QTDE "
						+ "  					FROM projudi.proc_tema pt "
						+ "					WHERE pt.data_sobrestado_final is null AND pt.data_sobrestado BETWEEN ? AND ? ");
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		if (idTema != null && !idTema.equals("")) {
			sql.append(" 				AND pt.id_tema = ?");
			ps.adicionarLong(idTema);
		}
		sql.append("			GROUP BY pt.id_tema " + "				)TAB"
				+ "  		INNER JOIN projudi.tema t ON tab.id_tema = t.id_tema"
				+ "		INNER JOIN projudi.tema_origem teo ON teo.id_tema_origem = t.id_tema_origem ");
		sql.append(" ORDER BY t.tema_codigo");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioRecursoRepetitivoDt obTemp = new RelatorioRecursoRepetitivoDt();
				obTemp.setRecurso(rs.getString("RECURSO"));
				obTemp.setQuestaoDireito(rs.getString("QUESTAO_DIREITO"));
				obTemp.setTemaCodigo(rs.getString("TEMA_CODIGO"));
				obTemp.setQuantidade(rs.getLong("QUANTIDADE"));
				listaRecursos.add(obTemp);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaRecursos;
	}

	/**
	 * Método de geração do relatório analítico de recursos repetitivos.
	 * 
	 * @param dataInicial
	 *            - data inicial de sobrestamento
	 * @param dataFinal
	 *            - data final de sobrestamento
	 * @param idTema
	 *            - ID do tema
	 * @return lista de recursos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List relAnaliticoRecursoRepetitivo(String dataInicial, String dataFinal, String idTema) throws Exception {
		List listaRecursos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" SELECT");
		sql.append(" t.titulo AS RECURSO");
		sql.append(", p.proc_numero || '.' || p.digito_verificador AS NUMERO_PROCESSO");
		sql.append(", tt.tema_tipo AS TIPO_RECURSO");
		sql.append(", to_char(pt.data_sobrestado, 'dd/mm/yyyy') AS DATA_SOBRESTADO");
		sql.append(", t.tema_codigo || '/' || teo.tema_origem AS TEMA_CODIGO");
		sql.append(" FROM projudi.tema t");
		sql.append(" INNER JOIN projudi.tema_origem teo ON teo.id_tema_origem = t.id_tema_origem");
		sql.append(" INNER JOIN projudi.proc_tema pt ON pt.id_tema = t.id_tema");
		sql.append(" INNER JOIN projudi.tema_tipo tt ON tt.id_tema_tipo = t.id_tema_tipo");
		sql.append(" INNER JOIN projudi.proc p ON p.id_proc = pt.id_proc");
		sql.append(" WHERE pt.data_sobrestado_final is null");
		sql.append(" AND pt.data_sobrestado BETWEEN ? AND ?");
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);

		if (idTema != null && !idTema.equals("")) {
			sql.append(" AND pt.id_tema = ?");
			ps.adicionarLong(idTema);
		}
		sql.append(" ORDER BY T.TEMA_CODIGO, DATA_SOBRESTADO");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioRecursoRepetitivoDt obTemp = new RelatorioRecursoRepetitivoDt();
				String recurso = rs.getString("RECURSO").length() > 75
						? Funcoes.limitarString(rs.getString("RECURSO"), 75) + ".."
						: rs.getString("RECURSO");
				obTemp.setRecurso(recurso);
				obTemp.setNumeroProcesso(rs.getString("NUMERO_PROCESSO"));
				obTemp.setTipoRecurso(rs.getString("TIPO_RECURSO"));
				obTemp.setDataSobrestado(rs.getString("DATA_SOBRESTADO"));
				obTemp.setTemaCodigo(rs.getString("TEMA_CODIGO"));
				listaRecursos.add(obTemp);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaRecursos;
	}

	/**
	 * Método para acompanhar a distribuição de processos dentro da área de
	 * distribuição (para turmas recursais) nos dias informados, sendo a consulta
	 * realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param datas
	 *            - lista com os dias que serão consultados.
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public Collection acompanharProcessosDistribuidosAreaTurma(String idAreaDistribuicao, List datas) throws Exception {

		HashMap mapRelatorio = new HashMap();

		ResultSetTJGO rs = null;
		StringBuffer sql = null;
		PreparedStatementTJGO ps = null;

		Date dataInicial, dataFinal;
		try {
			for (int i = 0; i < datas.size(); i++) {
				sql = new StringBuffer();
				ps = new PreparedStatementTJGO();
				dataInicial = Funcoes.calculePrimeraHora((Date) datas.get(i));
				dataFinal = Funcoes.calculeUltimaHora((Date) datas.get(i));

				sql.append(" SELECT s1.serv AS SERV, COUNT(*) AS QUANTIDADE FROM " + "		(SELECT p.id_serv "
						+ "			FROM projudi.proc p "
						+ "			INNER JOIN projudi.serv s ON p.id_serv = s.id_serv "
						+ "			INNER JOIN projudi.serv_area_dist sad ON s.id_serv = sad.id_serv "
						+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
						+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
						+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
						+ "			INNER JOIN PROJUDI.grupo g ON ct.id_grupo = g.id_grupo "
						+ "			INNER JOIN PROJUDI.grupo_tipo gt ON gt.id_grupo_tipo = g.id_grupo_tipo "
						+ "			WHERE sad.id_area_dist = ?");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append(" 			AND p.id_proc_fase <> ? ");
				ps.adicionarLong(ProcessoFaseDt.RECURSO);
				sql.append("			AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("			AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
				sql.append("				AND p.data_recebimento BETWEEN ? AND ? ");
				ps.adicionarDateTime(dataInicial);
				ps.adicionarDateTime(dataFinal);
				sql.append("UNION ALL " + "		SELECT r.id_serv_recurso " + "			FROM projudi.recurso r "
						+ "			INNER JOIN projudi.proc p ON p.id_proc = r.id_proc "
						+ "			INNER JOIN projudi.serv s ON r.id_serv_recurso = s.id_serv "
						+ "			INNER JOIN projudi.serv_area_dist sad on s.id_serv = sad.id_serv "
						+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
						+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
						+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
						+ "			INNER JOIN projudi.grupo g on ct.id_grupo = g.id_grupo "
						+ "			INNER JOIN projudi.grupo_tipo gt on gt.id_grupo_tipo = g.id_grupo_tipo "
						+ "			WHERE sad.id_area_dist = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append(
						"				AND ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"					AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc))");
				ps.adicionarDateTime(dataFinal);
				sql.append(
						" 			AND r.id_recurso = (SELECT MAX(r.id_recurso) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
				sql.append("UNION ALL " + "		SELECT r.id_serv_recurso " + "			FROM projudi.recurso r "
						+ "			INNER JOIN projudi.proc p ON p.id_proc = r.id_proc "
						+ "			INNER JOIN projudi.serv s ON r.id_serv_origem = s.id_serv "
						+ "			INNER JOIN projudi.serv_area_dist sad on s.id_serv = sad.id_serv "
						+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
						+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
						+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
						+ "			INNER JOIN projudi.grupo g on ct.id_grupo = g.id_grupo "
						+ "			INNER JOIN projudi.grupo_tipo gt on gt.id_grupo_tipo = g.id_grupo_tipo "
						+ "			WHERE sad.id_area_dist = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append(
						"				AND ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"					AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc))");
				ps.adicionarDateTime(dataFinal);
				sql.append(
						" 			AND r.id_recurso = (SELECT MAX(r.id_recurso) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
				sql.append("	) tab " + "	INNER JOIN projudi.serv s1 on tab.id_serv = s1.id_serv "
						+ "   GROUP BY serv " + "	ORDER BY serv ");

				rs = consultar(sql.toString(), ps);
				while (rs.next()) {

					if (mapRelatorio.containsKey(rs.getString("SERV"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("SERV"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("SERV"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
						mapRelatorio.put(rs.getString("SERV"), obTemp);
					}
				}
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método para acompanhar a distribuição de processos dentro da área de
	 * distribuição (para turmas recursais) nos dias informados, sendo a consulta
	 * realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param datas
	 *            - lista com os dias que serão consultados.
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public Collection acompanharProcessosDistribuidosAreaTurmaAntigo(String idAreaDistribuicao, List datas)
			throws Exception {

		HashMap mapRelatorio = new HashMap();

		ResultSetTJGO rs = null;
		StringBuffer sql = null;
		PreparedStatementTJGO ps = null;

		Date dataInicial, dataFinal;
		try {
			for (int i = 0; i < datas.size(); i++) {
				sql = new StringBuffer();
				ps = new PreparedStatementTJGO();
				dataInicial = Funcoes.calculePrimeraHora((Date) datas.get(i));
				dataFinal = Funcoes.calculeUltimaHora((Date) datas.get(i));

				sql.append(
						" SELECT pt.id_proc_tipo AS ID_PROC_TIPO, pt.proc_tipo AS PROC_TIPO, s1.serv AS SERV, COUNT(*) AS QUANTIDADE FROM "
								+ "		(SELECT p.id_proc_tipo, p.id_serv " + "			FROM projudi.proc p "
								+ "			INNER JOIN projudi.serv s ON p.id_serv = s.id_serv "
								+ "			INNER JOIN projudi.serv_area_dist sad ON s.id_serv = sad.id_serv "
								+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
								+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
								+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
								+ "			INNER JOIN PROJUDI.grupo g ON ct.id_grupo = g.id_grupo "
								+ "			INNER JOIN PROJUDI.grupo_tipo gt ON gt.id_grupo_tipo = g.id_grupo_tipo "
								+ "			WHERE sad.id_area_dist = ?");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append(" 			AND p.id_proc_fase <> ? ");
				ps.adicionarLong(ProcessoFaseDt.RECURSO);
				sql.append("			AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("			AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
				sql.append("				AND p.data_recebimento BETWEEN ? AND ? ");
				ps.adicionarDateTime(dataInicial);
				ps.adicionarDateTime(dataFinal);
				sql.append("UNION ALL " + "		SELECT p.id_proc_tipo, r.id_serv_recurso "
						+ "			FROM projudi.recurso r "
						+ "			INNER JOIN projudi.proc p ON p.id_proc = r.id_proc "
						+ "			INNER JOIN projudi.serv s ON r.id_serv_recurso = s.id_serv "
						+ "			INNER JOIN projudi.serv_area_dist sad on s.id_serv = sad.id_serv "
						+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
						+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
						+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
						+ "			INNER JOIN projudi.grupo g on ct.id_grupo = g.id_grupo "
						+ "			INNER JOIN projudi.grupo_tipo gt on gt.id_grupo_tipo = g.id_grupo_tipo "
						+ "			WHERE sad.id_area_dist = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append(
						"				AND ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"					AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc))");
				ps.adicionarDateTime(dataFinal);
				sql.append(
						" 			AND r.id_recurso = (SELECT MAX(r.id_recurso) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
				sql.append("UNION ALL " + "		SELECT p.id_proc_tipo, r.id_serv_recurso "
						+ "			FROM projudi.recurso r "
						+ "			INNER JOIN projudi.proc p ON p.id_proc = r.id_proc "
						+ "			INNER JOIN projudi.serv s ON r.id_serv_origem = s.id_serv "
						+ "			INNER JOIN projudi.serv_area_dist sad on s.id_serv = sad.id_serv "
						+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
						+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
						+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
						+ "			INNER JOIN projudi.grupo g on ct.id_grupo = g.id_grupo "
						+ "			INNER JOIN projudi.grupo_tipo gt on gt.id_grupo_tipo = g.id_grupo_tipo "
						+ "			WHERE sad.id_area_dist = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append(
						"				AND ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"					AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc))");
				ps.adicionarDateTime(dataFinal);
				sql.append(
						" 			AND r.id_recurso = (SELECT MAX(r.id_recurso) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
				sql.append("	) tab " + "	INNER JOIN projudi.serv s1 on tab.id_serv = s1.id_serv "
						+ "	INNER JOIN projudi.proc_tipo pt on tab.id_proc_tipo = pt.id_proc_tipo "
						+ "   GROUP BY pt.id_proc_tipo, proc_tipo, serv " + "	ORDER BY proc_tipo, serv ");
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {

					if (mapRelatorio.containsKey(rs.getString("ID_PROC_TIPO") + rs.getString("SERV"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("ID_PROC_TIPO") + rs.getString("SERV"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("SERV"));
						obTemp.setIdTipoProcesso(rs.getString("ID_PROC_TIPO"));
						obTemp.setTipoProcesso(rs.getString("PROC_TIPO"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
						mapRelatorio.put(rs.getString("ID_PROC_TIPO") + rs.getString("SERV"), obTemp);
					}
				}
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método para acompanhar a distribuição de processos dentro da área de
	 * distribuição (para serventias de 2º grau) nos dias informados, sendo a
	 * consulta realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param datas
	 *            - lista com os dias que serão consultados.
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public Collection acompanharProcessosDistribuidosArea2Grau(String idAreaDistribuicao, List datas) throws Exception {

		HashMap mapRelatorio = new HashMap();

		ResultSetTJGO rs = null;
		StringBuffer sql = null;
		PreparedStatementTJGO ps = null;

		Date dataInicial, dataFinal;
		try {
			for (int i = 0; i < datas.size(); i++) {
				sql = new StringBuffer();
				ps = new PreparedStatementTJGO();
				dataInicial = Funcoes.calculePrimeraHora((Date) datas.get(i));
				dataFinal = Funcoes.calculeUltimaHora((Date) datas.get(i));

				sql.append(" SELECT s1.serv AS SERV, tab2.Qtd AS QUANTIDADE FROM "
						+ "		(SELECT tab.id_serv, COUNT(1) as qtd FROM " + "			(SELECT s.id_serv "
						+ "				FROM PROJUDI.SERV_AREA_DIST sad "
						+ "				INNER JOIN PROJUDI.SERV s ON sad.id_serv = s.id_serv "
						+ "				INNER JOIN PROJUDI.SERV_RELACIONADA sr ON s.ID_SERV = sr.ID_SERV_PRINC "
						+ "				INNER JOIN PROJUDI.SERV_CARGO sc ON sr.ID_SERV_REL = sc.ID_SERV "
						+ "				INNER JOIN PROJUDI.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO "
						+ "				INNER JOIN PROJUDI.GRUPO g ON ct.ID_GRUPO= g.ID_GRUPO "
						+ "				INNER JOIN PROJUDI.GRUPO_TIPO gt ON gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO "
						+ "				INNER JOIN PROJUDI.PROC_RESP pr ON pr.ID_SERV_CARGO = sc.ID_SERV_CARGO "
						+ "				INNER JOIN PROJUDI.PROC p ON pr.ID_PROC = p.ID_PROC "
						+ "				WHERE sad.ID_AREA_DIST = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append("				AND sc.quantidade_dist > ? ");
				ps.adicionarLong(0);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
				sql.append("				AND sr.recebe_proc = ? ");
				ps.adicionarLong(new Long(1));
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND (p.data_recebimento BETWEEN ? AND ? ");
				ps.adicionarDateTime(dataInicial);
				ps.adicionarDateTime(dataFinal);
				sql.append(
						"					OR ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"						AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ))");
				ps.adicionarDateTime(dataFinal);
				sql.append("		) tab " + "		GROUP BY tab.id_serv " + "		) tab2 "
						+ "	INNER JOIN projudi.serv s1 on tab2.id_serv = s1.id_serv " + "	ORDER BY s1.serv");
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {
					if (mapRelatorio.containsKey(rs.getString("SERV"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("SERV"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("SERV"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
						mapRelatorio.put(rs.getString("SERV"), obTemp);
					}
				}
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método para acompanhar a distribuição de processos dentro da área de
	 * distribuição (para serventias de 2º grau) nos dias informados, sendo a
	 * consulta realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param datas
	 *            - lista com os dias que serão consultados.
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	// método foi substituído pelo método acima (sem o nome "Antigo") por conta da
	// nova distribuição implantada em outubro de 2013
	public Collection acompanharProcessosDistribuidosArea2GrauAntigo(String idAreaDistribuicao, List datas)
			throws Exception {

		HashMap mapRelatorio = new HashMap();

		ResultSetTJGO rs = null;
		StringBuffer sql = null;
		PreparedStatementTJGO ps = null;

		Date dataInicial, dataFinal;
		try {
			for (int i = 0; i < datas.size(); i++) {
				sql = new StringBuffer();
				ps = new PreparedStatementTJGO();
				dataInicial = Funcoes.calculePrimeraHora((Date) datas.get(i));
				dataFinal = Funcoes.calculeUltimaHora((Date) datas.get(i));

				sql.append(
						" SELECT pt.id_proc_tipo AS ID_PROC_TIPO, pt.proc_tipo AS PROC_TIPO, s1.serv AS SERV, s2.serv AS SERV_CARGO, tab2.Qtd AS QUANTIDADE FROM "
								+ "		(SELECT tab.id_proc_tipo, tab.id_serv, tab.id_serventia_cargo, COUNT(1) as qtd FROM "
								+ "			(SELECT p.id_proc_tipo, s.id_serv, sc.id_serv as id_serventia_cargo "
								+ "				FROM PROJUDI.SERV_AREA_DIST sad "
								+ "				INNER JOIN PROJUDI.SERV s ON sad.id_serv = s.id_serv "
								+ "				INNER JOIN PROJUDI.SERV_RELACIONADA sr ON s.ID_SERV = sr.ID_SERV_PRINC "
								+ "				INNER JOIN PROJUDI.SERV_CARGO sc ON sr.ID_SERV_REL = sc.ID_SERV "
								+ "				INNER JOIN PROJUDI.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO "
								+ "				INNER JOIN PROJUDI.GRUPO g ON ct.ID_GRUPO= g.ID_GRUPO "
								+ "				INNER JOIN PROJUDI.GRUPO_TIPO gt ON gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO "
								+ "				INNER JOIN PROJUDI.PROC_RESP pr ON pr.ID_SERV_CARGO = sc.ID_SERV_CARGO "
								+ "				INNER JOIN PROJUDI.PROC p ON pr.ID_PROC = p.ID_PROC "
								+ "				WHERE sad.ID_AREA_DIST = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append("				AND sc.quantidade_dist > ? ");
				ps.adicionarLong(0);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
				sql.append("				AND sr.recebe_proc = ? ");
				ps.adicionarLong(new Long(1));
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND (p.data_recebimento BETWEEN ? AND ? ");
				ps.adicionarDateTime(dataInicial);
				ps.adicionarDateTime(dataFinal);
				sql.append(
						"					OR ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"						AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ))");
				ps.adicionarDateTime(dataFinal);
				sql.append("		) tab " + "		GROUP BY tab.id_proc_tipo, tab.id_serv, tab.id_serventia_cargo "
						+ "		) tab2 " + "	INNER JOIN projudi.serv s1 on tab2.id_serv = s1.id_serv "
						+ " 	INNER JOIN projudi.serv s2 on tab2.id_serventia_cargo = s2.id_serv "
						+ "	INNER JOIN projudi.proc_tipo pt on tab2.id_proc_tipo = pt.id_proc_tipo "
						+ "	ORDER BY pt.proc_tipo, s1.serv");
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {
					if (mapRelatorio.containsKey(rs.getString("ID_PROC_TIPO") + rs.getString("SERV_CARGO"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("ID_PROC_TIPO") + rs.getString("SERV_CARGO"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("SERV") + " - " + rs.getString("SERV_CARGO"));
						obTemp.setIdTipoProcesso(rs.getString("ID_PROC_TIPO"));
						obTemp.setTipoProcesso(rs.getString("PROC_TIPO"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
						mapRelatorio.put(rs.getString("ID_PROC_TIPO") + rs.getString("SERV_CARGO"), obTemp);
					}
				}
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método para acompanhar a situação do ponteiro de distribuição dentro da área
	 * de distribuição (para turmas recursais) nos dias informados, sendo a consulta
	 * realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param datas
	 *            - lista com os dias que serão consultados.
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public Collection acompanharSituacaoPonteiroDistribuicaoAreaTurmaAntigo(String idAreaDistribuicao,
			List listaDiasIniciais, List listaDiasFinais) throws Exception {

		HashMap mapRelatorio = new HashMap();

		ResultSetTJGO rs = null;
		StringBuffer sql = null;
		PreparedStatementTJGO ps = null;

		Date dataInicial, dataFinal;
		try {
			for (int i = 0; i < listaDiasIniciais.size(); i++) {
				sql = new StringBuffer();
				ps = new PreparedStatementTJGO();
				dataInicial = (Date) listaDiasIniciais.get(i);
				dataFinal = (Date) listaDiasFinais.get(i);

				sql.append(" SELECT pt.id_proc_tipo, pt.proc_tipo, s1.serv, tab2.Qtd AS QUANTIDADE FROM "
						+ "		(SELECT tab.id_proc_tipo, tab.id_serv, COUNT(1) as qtd FROM "
						+ "			(SELECT p.id_proc_tipo, s.id_serv " + "				FROM projudi.proc p "
						+ "				INNER JOIN projudi.serv s ON p.id_serv = s.id_serv "
						+ "				INNER JOIN projudi.serv_area_dist sad ON s.id_serv = sad.id_serv "
						+ "				INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
						+ "				INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
						+ "				INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
						+ "				INNER JOIN PROJUDI.grupo g ON ct.id_grupo = g.id_grupo "
						+ "				INNER JOIN PROJUDI.grupo_tipo gt ON gt.id_grupo_tipo = g.id_grupo_tipo "
						+ "				WHERE sad.id_area_dist = ?");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append(" 				AND p.id_proc_fase <> ? ");
				ps.adicionarLong(ProcessoFaseDt.RECURSO);
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
				sql.append("				AND p.data_recebimento BETWEEN ? AND ? ");
				ps.adicionarDateTime(dataInicial);
				ps.adicionarDateTime(dataFinal);
				sql.append("UNION ALL " + "		SELECT p.id_proc_tipo, s.id_serv " + "			FROM projudi.recurso r "
						+ "			INNER JOIN projudi.proc p ON p.id_proc = r.id_proc "
						+ "			INNER JOIN projudi.serv s ON r.id_serv_recurso = s.id_serv "
						+ "			INNER JOIN projudi.serv_area_dist sad on s.id_serv = sad.id_serv "
						+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
						+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
						+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
						+ "			INNER JOIN projudi.grupo g on ct.id_grupo = g.id_grupo "
						+ "			INNER JOIN projudi.grupo_tipo gt on gt.id_grupo_tipo = g.id_grupo_tipo "
						+ "			WHERE sad.id_area_dist = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append(
						"				AND ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"					AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc))");
				ps.adicionarDateTime(dataFinal);
				sql.append(
						" 			AND r.id_recurso = (SELECT MAX(r.id_recurso) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
				sql.append("UNION ALL " + "		SELECT p.id_proc_tipo, s.id_serv " + "			FROM projudi.recurso r "
						+ "			INNER JOIN projudi.proc p ON p.id_proc = r.id_proc "
						+ "			INNER JOIN projudi.serv s ON r.id_serv_origem = s.id_serv "
						+ "			INNER JOIN projudi.serv_area_dist sad on s.id_serv = sad.id_serv "
						+ "			INNER JOIN projudi.proc_resp pr ON p.id_proc = pr.id_proc "
						+ "			INNER JOIN projudi.serv_cargo sc ON pr.id_serv_cargo = sc.id_serv_cargo "
						+ "			INNER JOIN projudi.cargo_tipo ct ON pr.id_cargo_tipo = ct.id_cargo_tipo "
						+ "			INNER JOIN projudi.grupo g on ct.id_grupo = g.id_grupo "
						+ "			INNER JOIN projudi.grupo_tipo gt on gt.id_grupo_tipo = g.id_grupo_tipo "
						+ "			WHERE sad.id_area_dist = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append(
						"				AND ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"					AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc))");
				ps.adicionarDateTime(dataFinal);
				sql.append(
						" 			AND r.id_recurso = (SELECT MAX(r.id_recurso) FROM projudi.recurso r WHERE r.id_proc = p.id_proc) ");
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.JUIZ_TURMA);
				sql.append("		) tab  " + "		GROUP BY tab.id_proc_tipo, tab.id_serv " + "		) tab2 "
						+ "	INNER JOIN projudi.serv s1 on tab2.id_serv = s1.id_serv "
						+ "	INNER JOIN projudi.proc_tipo pt on tab2.id_proc_tipo =pt.id_proc_tipo "
						+ "	ORDER BY pt.proc_tipo, tab2.id_serv ");
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {

					if (mapRelatorio.containsKey(rs.getString("ID_PROC_TIPO") + rs.getString("SERV"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("ID_PROC_TIPO") + rs.getString("SERV"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("SERV"));
						obTemp.setIdTipoProcesso(rs.getString("ID_PROC_TIPO"));
						obTemp.setTipoProcesso(rs.getString("PROC_TIPO"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
						mapRelatorio.put(rs.getString("ID_PROC_TIPO") + rs.getString("SERV"), obTemp);
					}
				}
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método para acompanhar a situação do ponteiro de distribuição dentro da área
	 * de distribuição (para serventias de 2º grau) nos dias informados, sendo a
	 * consulta realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param datas
	 *            - lista com os dias que serão consultados.
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public Collection acompanharSituacaoPonteiroDistribuicaoArea2Grau(String idAreaDistribuicao, List listaDiasIniciais,
			List listaDiasFinais) throws Exception {

		HashMap mapRelatorio = new HashMap();

		ResultSetTJGO rs = null;
		StringBuffer sql = null;
		PreparedStatementTJGO ps = null;

		Date dataInicial, dataFinal;
		try {
			for (int i = 0; i < listaDiasIniciais.size(); i++) {
				sql = new StringBuffer();
				ps = new PreparedStatementTJGO();
				dataInicial = (Date) listaDiasIniciais.get(i);
				dataFinal = (Date) listaDiasFinais.get(i);

				sql.append(" SELECT s1.serv AS SERV, tab2.Qtd AS QUANTIDADE FROM "
						+ "		(SELECT tab.id_serv, COUNT(1) as qtd FROM " + "			(SELECT s.id_serv "
						+ "				FROM PROJUDI.SERV_AREA_DIST sad "
						+ "				INNER JOIN PROJUDI.SERV s ON sad.id_serv = s.id_serv "
						+ "				INNER JOIN PROJUDI.SERV_RELACIONADA sr ON s.ID_SERV = sr.ID_SERV_PRINC "
						+ "				INNER JOIN PROJUDI.SERV_CARGO sc ON sr.ID_SERV_REL = sc.ID_SERV "
						+ "				INNER JOIN PROJUDI.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO "
						+ "				INNER JOIN PROJUDI.GRUPO g ON ct.ID_GRUPO= g.ID_GRUPO "
						+ "				INNER JOIN PROJUDI.GRUPO_TIPO gt ON gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO "
						+ "				INNER JOIN PROJUDI.PROC_RESP pr ON pr.ID_SERV_CARGO = sc.ID_SERV_CARGO "
						+ "				INNER JOIN PROJUDI.PROC p ON pr.ID_PROC = p.ID_PROC "
						+ "				WHERE sad.ID_AREA_DIST = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append("				AND sc.quantidade_dist > ? ");
				ps.adicionarLong(0);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
				sql.append("				AND sr.recebe_proc = ? ");
				ps.adicionarLong(new Long(1));
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND (p.data_recebimento BETWEEN ? AND ? ");
				ps.adicionarDateTime(dataInicial);
				ps.adicionarDateTime(dataFinal);
				sql.append(
						"					OR ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"						AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ))");
				ps.adicionarDateTime(dataFinal);
				sql.append("		) tab " + "		GROUP BY tab.id_serv " + "		) tab2 "
						+ "	INNER JOIN projudi.serv s1 on tab2.id_serv = s1.id_serv " + "	ORDER BY s1.serv");
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {
					if (mapRelatorio.containsKey(rs.getString("SERV"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("SERV"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("SERV"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
						mapRelatorio.put(rs.getString("SERV"), obTemp);
					}
				}
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método para acompanhar a situação do ponteiro de distribuição dentro da área
	 * de distribuição (para serventias de 2º grau) nos dias informados, sendo a
	 * consulta realizada dia a dia.
	 * 
	 * @param idAreaDistribuicao
	 *            - ID da área de distribuição
	 * @param datas
	 *            - lista com os dias que serão consultados.
	 * @return lista de distribuição nos dias informados
	 * @throws Exception
	 * @author hmgodinho
	 */
	// método foi substituído pelo método acima (sem o nome "Antigo") por conta da
	// nova distribuição implantada em outubro de 2013
	public Collection acompanharSituacaoPonteiroDistribuicaoArea2GrauAntigo(String idAreaDistribuicao,
			List listaDiasIniciais, List listaDiasFinais) throws Exception {

		HashMap mapRelatorio = new HashMap();

		ResultSetTJGO rs = null;
		StringBuffer sql = null;
		PreparedStatementTJGO ps = null;

		Date dataInicial, dataFinal;
		try {
			for (int i = 0; i < listaDiasIniciais.size(); i++) {
				sql = new StringBuffer();
				ps = new PreparedStatementTJGO();
				dataInicial = (Date) listaDiasIniciais.get(i);
				dataFinal = (Date) listaDiasFinais.get(i);

				sql.append(
						" SELECT pt.id_proc_tipo AS ID_PROC_TIPO, pt.proc_tipo AS PROC_TIPO, s1.serv AS SERV, s2.serv AS SERV_CARGO, tab2.Qtd AS QUANTIDADE FROM "
								+ "		(SELECT tab.id_proc_tipo, tab.id_serv, tab.id_serventia_cargo, COUNT(1) as qtd FROM "
								+ "			(SELECT p.id_proc_tipo, s.id_serv, sc.id_serv as id_serventia_cargo "
								+ "				FROM PROJUDI.SERV_AREA_DIST sad "
								+ "				INNER JOIN PROJUDI.SERV s ON sad.id_serv = s.id_serv "
								+ "				INNER JOIN PROJUDI.SERV_RELACIONADA sr ON s.ID_SERV = sr.ID_SERV_PRINC "
								+ "				INNER JOIN PROJUDI.SERV_CARGO sc ON sr.ID_SERV_REL = sc.ID_SERV "
								+ "				INNER JOIN PROJUDI.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO "
								+ "				INNER JOIN PROJUDI.GRUPO g ON ct.ID_GRUPO= g.ID_GRUPO "
								+ "				INNER JOIN PROJUDI.GRUPO_TIPO gt ON gt.ID_GRUPO_TIPO=g.ID_GRUPO_TIPO "
								+ "				INNER JOIN PROJUDI.PROC_RESP pr ON pr.ID_SERV_CARGO = sc.ID_SERV_CARGO "
								+ "				INNER JOIN PROJUDI.PROC p ON pr.ID_PROC = p.ID_PROC "
								+ "				WHERE sad.ID_AREA_DIST = ? ");
				ps.adicionarLong(idAreaDistribuicao);
				sql.append("				AND sc.quantidade_dist > ? ");
				ps.adicionarLong(0);
				sql.append("				AND gt.grupo_tipo_codigo = ? ");
				ps.adicionarLong(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU);
				sql.append("				AND sr.recebe_proc = ? ");
				ps.adicionarLong(new Long(1));
				sql.append("				AND pr.redator = ?");
				ps.adicionarLong(ProcessoResponsavelDt.PROCESSO_RESPONSAVEL_REDATOR_ATIVO);
				sql.append("				AND (p.data_recebimento BETWEEN ? AND ? ");
				ps.adicionarDateTime(dataInicial);
				ps.adicionarDateTime(dataFinal);
				sql.append(
						"					OR ( ? <= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ");
				ps.adicionarDateTime(dataInicial);
				sql.append(
						"						AND ? >= (SELECT MAX(r.data_recebimento) FROM projudi.recurso r WHERE r.id_proc = p.id_proc AND r.data_retorno IS NULL) ))");
				ps.adicionarDateTime(dataFinal);
				sql.append("		) tab " + "		GROUP BY tab.id_proc_tipo, tab.id_serv, tab.id_serventia_cargo "
						+ "		) tab2 " + "	INNER JOIN projudi.serv s1 on tab2.id_serv = s1.id_serv "
						+ " 	INNER JOIN projudi.serv s2 on tab2.id_serventia_cargo = s2.id_serv "
						+ "	INNER JOIN projudi.proc_tipo pt on tab2.id_proc_tipo = pt.id_proc_tipo "
						+ "	ORDER BY pt.proc_tipo, s1.serv");
				rs = consultar(sql.toString(), ps);
				while (rs.next()) {
					if (mapRelatorio.containsKey(rs.getString("ID_PROC_TIPO") + rs.getString("SERV_CARGO"))) {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = (RelatorioAcompanhamentoPonteiroDistribuicaoDt) mapRelatorio
								.get(rs.getString("ID_PROC_TIPO") + rs.getString("SERV_CARGO"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
					} else {
						RelatorioAcompanhamentoPonteiroDistribuicaoDt obTemp = new RelatorioAcompanhamentoPonteiroDistribuicaoDt();
						obTemp.setNomeServentia(rs.getString("SERV") + " - " + rs.getString("SERV_CARGO"));
						obTemp.setIdTipoProcesso(rs.getString("ID_PROC_TIPO"));
						obTemp.setTipoProcesso(rs.getString("PROC_TIPO"));
						obTemp.setQuantidade(i, rs.getLong("QUANTIDADE"));
						mapRelatorio.put(rs.getString("ID_PROC_TIPO") + rs.getString("SERV_CARGO"), obTemp);
					}
				}
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return mapRelatorio.values();
	}

	/**
	 * Método que realiza a consulta do relatório analítico de produtividade. param
	 * ano - ano informado na tela param mes - mês informado na tela param idComarca
	 * - ID da Comarca selecionada param idServentia - ID da Serventia selecionada
	 * param idUsuario - ID do usuário selecionado param idMovimentacaoTipo - tipo
	 * de movimentação informada param posicaoPaginaAtual - página indicada na
	 * paginação return json do relatorio throws Exception author gschiquini
	 */
	public String relAnaliticoProdutividadeJSON(String ano, String mes, String idComarca, String idServentia,
			String idUsuario, String idMovimentacaoTipo, String posicaoPaginaAtual) throws Exception {

		String stTemp;
		StringBuffer sql = new StringBuffer();
		String sqlOrderBy = "";
		ResultSetTJGO rs = null, rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sqlSelect = "SELECT ID_PROC AS id, PROC_NUMERO AS DESCRICAO1, NOME AS DESCRICAO2, EST_PROD_ITEM AS DESCRICAO3, MES AS DESCRICAO4, ANO AS DESCRICAO5 ";
		sql.append("  FROM projudi.VIEW_EST_PROD_ANA vepa ");
		sqlOrderBy = " ORDER BY vepa.ANO, vepa.MES, vepa.PROC_NUMERO, vepa.NOME, vepa.EST_PROD_ITEM";

		boolean clausulaWhere = true;
		boolean clausulaAnd = false;
		if (ano != null && !ano.equals("") && mes != null && !mes.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			sql.append("vepa.ANO = ? AND vepa.MES = ?");
			ps.adicionarLong(ano);
			ps.adicionarLong(mes);
			clausulaAnd = true;
		}

		if (idComarca != null && !idComarca.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
			clausulaAnd = true;
		}
		if (idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_SERV = ?");
			ps.adicionarLong(idServentia);
			clausulaAnd = true;
		}
		if (idUsuario != null && !idUsuario.equals("") && idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_USU = ?");
			ps.adicionarLong(idUsuario);
			clausulaAnd = true;
		}
		if (idMovimentacaoTipo != null && !idMovimentacaoTipo.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_EST_PROD_ITEM = ?");
			ps.adicionarLong(idMovimentacaoTipo);
		}

		try {
			rs = consultarPaginacao(sqlSelect + sql.toString() + sqlOrderBy, ps, posicaoPaginaAtual);
			String sqlQuantidade = "SELECT COUNT(ID_PROC) AS QUANTIDADE " + sql.toString();
			rs2 = consultar(sqlQuantidade, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs, 5);

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return stTemp;
	}

	/**
	 * Método que realiza a consulta do relatório analítico de processo. param ano -
	 * ano informado na tela param mes - mês informado na tela param idComarca - ID
	 * da Comarca selecionada param idServentia - ID da Serventia selecionada param
	 * idUsuario - ID do usuário selecionado param idProcessoTipo - tipo de processo
	 * informado param posicaoPaginaAtual - página indicada na paginação return json
	 * da lista de processos throws Exception author gschiquini
	 */
	public String relAnaliticoProcessoJSON(String ano, String mes, String idComarca, String idServentia,
			String idProcessoTipo, String posicaoPaginaAtual) throws Exception {

		String stTemp;
		StringBuffer sql = new StringBuffer();
		String sqlOrderBy = "";
		ResultSetTJGO rs = null, rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sqlSelect = "SELECT ID_PROC AS id, PROC_NUMERO AS DESCRICAO1, PROC_TIPO AS DESCRICAO2, MES AS DESCRICAO3, ANO AS DESCRICAO4 ";
		sql.append("  FROM projudi.VIEW_EST_PROC_ANA vepa ");
		sqlOrderBy = " ORDER BY vepa.ANO, vepa.MES, vepa.PROC_NUMERO, vepa.PROC_TIPO";

		boolean clausulaWhere = true;
		boolean clausulaAnd = false;
		if (ano != null && !ano.equals("") && mes != null && !mes.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			sql.append("vepa.ANO = ? AND vepa.MES = ?");
			ps.adicionarLong(ano);
			ps.adicionarLong(mes);
			clausulaAnd = true;
		}

		if (idComarca != null && !idComarca.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
			clausulaAnd = true;
		}
		if (idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_SERV = ?");
			ps.adicionarLong(idServentia);
			clausulaAnd = true;
		}
		if (idProcessoTipo != null && !idProcessoTipo.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_PROC_TIPO = ?");
			clausulaAnd = true;
			ps.adicionarLong(idProcessoTipo);
		}

		try {
			rs = consultarPaginacao(sqlSelect + sql.toString() + sqlOrderBy, ps, posicaoPaginaAtual);
			String sqlQuantidade = "SELECT COUNT(*) AS QUANTIDADE " + sql.toString();
			rs2 = consultar(sqlQuantidade, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs, 4);

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return stTemp;
	}

	/**
	 * Método que realiza a consulta do relatório analítico de pendência. param ano
	 * - ano informado na tela param mes - mês informado na tela param idComarca -
	 * ID da Comarca selecionada param idServentia - ID da Serventia selecionada
	 * param idUsuario - ID do usuário selecionado param idPendenciaTipo - tipo de
	 * pendência informada param posicaoPaginaAtual - página indicada na paginação
	 * return JSON da lista de processos com pendências throws Exception author
	 * gschiquini
	 */
	public String relAnaliticoPendenciaJSON(String ano, String mes, String idComarca, String idServentia,
			String idUsuario, String idPendenciaTipo, String posicaoPaginaAtual) throws Exception {

		String stTemp;
		StringBuffer sql = new StringBuffer();
		String sqlOrderBy = "";
		ResultSetTJGO rs = null, rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sqlSelect = "SELECT ID_PROC AS ID, PROC_NUMERO AS DESCRICAO1, NOME AS DESCRICAO2, PEND_TIPO AS DESCRICAO3, MES AS DESCRICAO4, ANO AS DESCRICAO5";
		sql.append("  FROM projudi.VIEW_EST_PEND_ANA vepa ");
		sqlOrderBy = " ORDER BY vepa.ANO, vepa.MES, vepa.PROC_NUMERO, vepa.NOME, vepa.PEND_TIPO";

		boolean clausulaWhere = true;
		boolean clausulaAnd = false;
		if (ano != null && !ano.equals("") && mes != null && !mes.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			sql.append("vepa.ANO = ? AND vepa.MES = ?");
			ps.adicionarLong(ano);
			ps.adicionarLong(mes);
			clausulaAnd = true;
		}

		if (idComarca != null && !idComarca.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_COMARCA = ?");
			ps.adicionarLong(idComarca);
			clausulaAnd = true;
		}
		if (idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_SERV = ?");
			ps.adicionarLong(idServentia);
			clausulaAnd = true;
		}
		if (idUsuario != null && !idUsuario.equals("") && idServentia != null && !idServentia.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_USU = ?");
			ps.adicionarLong(idUsuario);
			clausulaAnd = true;
		}
		if (idPendenciaTipo != null && !idPendenciaTipo.equals("")) {
			if (clausulaWhere) {
				sql.append(" WHERE ");
				clausulaWhere = false;
			}
			if (clausulaAnd) {
				sql.append(" AND ");
			}
			sql.append(" vepa.ID_PEND_TIPO = ?");
			ps.adicionarLong(idPendenciaTipo);
		}

		try {
			rs = consultarPaginacao(sqlSelect + sql.toString() + sqlOrderBy, ps, posicaoPaginaAtual);
			String sqlQuantidade = "SELECT COUNT(*) AS QUANTIDADE " + sql.toString();
			rs2 = consultar(sqlQuantidade, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs, 5);

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return stTemp;
	}
	

	/**
	 * Relatório destinado ao NUPEMEC.
	 * 
	 * @param idComarca
	 * @param dataInicial
	 * @param dataFinal
	 * @author hrrosa
	 * @return
	 * @throws Exception
	 */
	public List<RelatorioSumarioAudienciasComarcaDt> relSumarioAudienciasComarca(String idComarca, String dataInicial,
			String dataFinal) throws Exception {

		HashMap<String, RelatorioSumarioAudienciasComarcaDt> mapRelSumarioAudienciasComarcaDt = new HashMap<String, RelatorioSumarioAudienciasComarcaDt>();
		String sql = "";
		String designadas = "";
		String realizadas = "";
		String acordos = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RelatorioSumarioAudienciasComarcaDt relSumarioAudienciasComarcaDt = null;

		sql = " select";
		sql += " SERV, ano, mes, sum(DESIGNADAS) as DESIGNADAS,  sum(REALIZADAS) as REALIZADAS, sum(ACORDOS) as ACORDOS, sum(VALOR_ACORDOS) as VALOR_ACORDOS from (";
		// DESIGNADAS
		sql += " (";
		sql += " 	select serv, to_char(a.data_agendada, 'yyyy') as ano, to_char(a.data_agendada, 'mm') as mes, count(*) as DESIGNADAS, 0 as REALIZADAS, 0 as ACORDOS, 0 as VALOR_ACORDOS  from AUDI a";
		sql += " 	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI";
		sql += " 	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS";
		sql += " 	join serv s on a.id_serv = s.id_serv";
		sql += " 	where a.id_serv in (select id_serv from serv where id_comarca = ?)";
		ps.adicionarLong(idComarca);
		sql += " 	and data_agendada >= ?";
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += " 	and data_agendada <= ?";
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += " and aps.AUDI_PROC_STATUS_CODIGO in(";
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.NAO_REALIZADA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.NEGATIVADA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REMARCADA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.HOMOLOGADO_DESISTENCIA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
		sql += " 					?  ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_ACORDO);
		sql += "					)";
		sql += " 	group by serv, to_char(a.data_agendada, 'yyyy'), to_char(a.data_agendada, 'mm')";
		sql += " ) UNION ALL";

		// REALIZADAS
		sql += " (";
		sql += " 	select serv, to_char(a.data_agendada, 'yyyy') as ano, to_char(a.data_agendada, 'mm') as mes, 0 as DESIGNADAS, count(*) as REALIZADAS, 0 AS ACORDOS, 0 AS VALOR_ACORDOS from AUDI a";
		sql += " 	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI";
		sql += " 	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS";
		sql += "	join serv s on a.id_serv = s.id_serv";
		sql += "	where a.id_serv in (select id_serv from serv where id_comarca = ?)";
		ps.adicionarLong(idComarca);
		sql += " 	and data_agendada >= ?";
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += " 	and data_agendada <= ?";
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += " and aps.AUDI_PROC_STATUS_CODIGO in(";
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.HOMOLOGADO_DESISTENCIA);
		sql += " 					?, ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
		sql += " 					?  ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_ACORDO);
		sql += "					)";
		sql += "	group by serv, to_char(a.data_agendada, 'yyyy'), to_char(a.data_agendada, 'mm')";
		sql += " ) UNION ALL";

		// ACORDOS
		sql += " (";
		sql += "	select serv, to_char(a.data_agendada, 'yyyy') as ano, to_char(a.data_agendada, 'mm') as mes, 0 AS DESIGNADAS, 0 AS REALIZADAS, count(*) as ACORDOS, 0 AS VALOR_ACORDOS from AUDI a";
		sql += " 	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI";
		sql += "	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS";
		sql += "	join serv s on a.id_serv = s.id_serv";
		sql += "	where a.id_serv in (select id_serv from serv where id_comarca = ?)";
		ps.adicionarLong(idComarca);
		sql += " 	and data_agendada >= ?";
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += " 	and data_agendada <= ?";
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += " and aps.AUDI_PROC_STATUS_CODIGO in(";
		sql += " 					? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
		sql += "					)";
		sql += "	group by serv, to_char(a.data_agendada, 'yyyy'), to_char(a.data_agendada, 'mm')";
		sql += " ) UNION ALL";

		// VALOR_ACORDOS
		sql += " (";
		sql += "	select serv, to_char(a.data_agendada, 'yyyy') as ano, to_char(a.data_agendada, 'mm') as mes, 0 AS DESIGNADAS, 0 AS REALIZADAS, 0 AS ACORDOS, nvl(sum(ap.valor_acordo),0) as VALOR_ACORDOS from AUDI a";
		sql += "	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI";
		sql += "	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS";
		sql += "	join serv s on a.id_serv = s.id_serv";
		sql += "	where a.id_serv in (select id_serv from serv where id_comarca = ?)";
		ps.adicionarLong(idComarca);
		sql += " 	and data_agendada >= ?";
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += " 	and data_agendada <= ?";
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += " and aps.AUDI_PROC_STATUS_CODIGO in(";
		sql += " 					? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
		sql += "					)";
		sql += "	group by serv, to_char(a.data_agendada, 'yyyy'), to_char(a.data_agendada, 'mm')";
		sql += " )";

		sql += " )";
		sql += " group by serv, ano, mes";
		sql += " order by serv, ano, mes";

		try {
			rs = consultar(sql, ps);
			while (rs.next()) {

				relSumarioAudienciasComarcaDt = mapRelSumarioAudienciasComarcaDt.get(rs.getString("SERV"));

				if (relSumarioAudienciasComarcaDt == null) {
					relSumarioAudienciasComarcaDt = new RelatorioSumarioAudienciasComarcaDt();
				}

				relSumarioAudienciasComarcaDt.setServentia(rs.getString("SERV"));
				relSumarioAudienciasComarcaDt.setAno(rs.getString("ANO"));

				switch (rs.getString("MES")) {
				case "01":
					relSumarioAudienciasComarcaDt.setMes("Janeiro");
					relSumarioAudienciasComarcaDt.setJanDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setJanRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setJanAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setJanValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "02":
					relSumarioAudienciasComarcaDt.setMes("Fevereiro");
					relSumarioAudienciasComarcaDt.setFevDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setFevRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setFevAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setFevValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "03":
					relSumarioAudienciasComarcaDt.setMes("Março");
					relSumarioAudienciasComarcaDt.setMarDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setMarRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setMarAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setMarValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "04":
					relSumarioAudienciasComarcaDt.setMes("Abril");
					relSumarioAudienciasComarcaDt.setAbrDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setAbrRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setAbrAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setAbrValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "05":
					relSumarioAudienciasComarcaDt.setMes("Maio");
					relSumarioAudienciasComarcaDt.setMaiDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setMaiRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setMaiAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setMaiValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "06":
					relSumarioAudienciasComarcaDt.setMes("Junho");
					relSumarioAudienciasComarcaDt.setJunDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setJunRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setJunAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setJunValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "07":
					relSumarioAudienciasComarcaDt.setMes("Julho");
					relSumarioAudienciasComarcaDt.setJulDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setJulRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setJulAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setJulValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "08":
					relSumarioAudienciasComarcaDt.setMes("Agosto");
					relSumarioAudienciasComarcaDt.setAgoDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setAgoRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setAgoAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setAgoValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "09":
					relSumarioAudienciasComarcaDt.setMes("Setembro");
					relSumarioAudienciasComarcaDt.setSetDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setSetRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setSetAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setSetValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "10":
					relSumarioAudienciasComarcaDt.setMes("Outubro");
					relSumarioAudienciasComarcaDt.setOutDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setOutRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setOutAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setOutValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "11":
					relSumarioAudienciasComarcaDt.setMes("Novembro");
					relSumarioAudienciasComarcaDt.setNovDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setNovRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setNovAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setNovValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;

				case "12":
					relSumarioAudienciasComarcaDt.setMes("Dezembro");
					relSumarioAudienciasComarcaDt.setDezDesignadas(rs.getString("DESIGNADAS"));
					relSumarioAudienciasComarcaDt.setDezRealizadas(rs.getString("REALIZADAS"));
					relSumarioAudienciasComarcaDt.setDezAcordos(rs.getString("ACORDOS"));
					relSumarioAudienciasComarcaDt.setDezValorAcordos(rs.getString("VALOR_ACORDOS"));
					break;
				}

				relSumarioAudienciasComarcaDt.setDataInicial(dataInicial);
				relSumarioAudienciasComarcaDt.setDataFinal(dataFinal);
				mapRelSumarioAudienciasComarcaDt.put(rs.getString("SERV"), relSumarioAudienciasComarcaDt);

			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		return new ArrayList<RelatorioSumarioAudienciasComarcaDt>(mapRelSumarioAudienciasComarcaDt.values());

	}

	/**
	 * Relatório destinado ao NUPEMEC.
	 * 
	 * @param idComarca
	 * @param dataInicial
	 * @param dataFinal
	 * @author hrrosa
	 * @return
	 * @throws Exception
	 */
	public List relSumarioAudienciasComarcaDia(String idComarca, String dataInicial, String dataFinal) throws Exception {

		HashMap<String, RelatorioSumarioAudienciasComarcaDiaDt> mapRelSumarioAudienciasComarcaDiaDt = new HashMap<String, RelatorioSumarioAudienciasComarcaDiaDt>();
		String sql = "";
		String designadas = "";
		String realizadas = "";
		String acordos = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RelatorioSumarioAudienciasComarcaDiaDt relSumarioAudienciasComarcaDiaDt = null;
		RelatorioSumarioAudienciasComarcaDiaServDt relSumarioAudienciasComarcaDiaServDt;
		List listaProcessos = new ArrayList();

		// dataFinal = dataInicial;

		sql = " select";
		sql += " PROC_TIPO, comarca, SERV, sum(DESIGNADAS) as DESIGNADAS,  sum(REALIZADAS) as REALIZADAS, sum(ACORDOS) as ACORDOS, sum(VALOR_ACORDOS) as VALOR_ACORDOS from (";

		// DESIGNADAS
		sql += " (";
		sql += " 	select com.comarca,";
		sql += "	CASE WHEN pt.PROC_TIPO_CODIGO IN (?,?,?) THEN 'Pré-processual' ELSE 'Processual' END AS PROC_TIPO,";
		ps.adicionarLong(ProcessoTipoDt.RECLAMACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.PEDIDO_MEDIACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.HOMOLOGACAO_TRANSACAO_EXTRAJUDICIAL);
		sql += "	serv, count(*) as DESIGNADAS, 0 as REALIZADAS, 0 as ACORDOS, 0 as VALOR_ACORDOS  from AUDI a";
		sql += " 	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI";
		sql += " 	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS";
		sql += " 	join serv s on a.id_serv = s.id_serv";
		sql += " 	join proc p on ap.id_proc = p.id_proc";
		sql += " 	join proc_tipo pt on p.ID_PROC_TIPO = pt.ID_PROC_TIPO";
		sql += " 	join comarca com on com.ID_COMARCA = s.ID_COMARCA";
		sql += " 	and data_agendada >= ?";
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += " 	and data_agendada <= ?";
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += " 	and aps.AUDI_PROC_STATUS_CODIGO in( ?,?,?,?,?,?,?,?,?,?,?,?)";
		ps.adicionarLong(AudienciaProcessoStatusDt.NAO_REALIZADA);
		ps.adicionarLong(AudienciaProcessoStatusDt.NEGATIVADA);
		ps.adicionarLong(AudienciaProcessoStatusDt.REMARCADA);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_ACORDO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
		ps.adicionarLong(AudienciaProcessoStatusDt.DESMARCAR_PAUTA);
		sql += " 	group by com.comarca, PROC_TIPO_CODIGO, serv";
		sql += " ) UNION ALL";

		// REALIZADAS
		sql += " (";
		sql += " 	select com.comarca,";
		sql += "	CASE WHEN pt.PROC_TIPO_CODIGO IN (?,?,?) THEN 'Pré-processual' ELSE 'Processual' END AS PROC_TIPO,";
		ps.adicionarLong(ProcessoTipoDt.RECLAMACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.PEDIDO_MEDIACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.HOMOLOGACAO_TRANSACAO_EXTRAJUDICIAL);
		sql += "	serv, 0 as DESIGNADAS, count(*) as REALIZADAS, 0 AS ACORDOS, 0 AS VALOR_ACORDOS from AUDI a";
		sql += " 	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI";
		sql += " 	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS";
		sql += "	join serv s on a.id_serv = s.id_serv";
		sql += " 	join proc p on ap.id_proc = p.id_proc";
		sql += " 	join proc_tipo pt on p.ID_PROC_TIPO = pt.ID_PROC_TIPO";
		sql += "	join comarca com on com.ID_COMARCA = s.ID_COMARCA";
		sql += " 	and data_agendada >= ?";
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += " 	and data_agendada <= ?";
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += " 	and aps.AUDI_PROC_STATUS_CODIGO in( ?,?,?,?,?,?,?,?)";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_ACORDO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
		sql += "	group by com.comarca, PROC_TIPO_CODIGO, serv";
		sql += " ) UNION ALL";

		// ACORDOS
		sql += " (";
		sql += "	select com.comarca,";
		sql += "	CASE WHEN pt.PROC_TIPO_CODIGO IN (?,?,?) THEN 'Pré-processual' ELSE 'Processual' END AS PROC_TIPO,";
		ps.adicionarLong(ProcessoTipoDt.RECLAMACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.PEDIDO_MEDIACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.HOMOLOGACAO_TRANSACAO_EXTRAJUDICIAL);
		sql += "	serv, 0 AS DESIGNADAS, 0 AS REALIZADAS, count(*) as ACORDOS, 0 AS VALOR_ACORDOS from AUDI a";
		sql += " 	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI";
		sql += "	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS";
		sql += "	join serv s on a.id_serv = s.id_serv";
		sql += " 	join proc p on ap.id_proc = p.id_proc";
		sql += " 	join proc_tipo pt on p.ID_PROC_TIPO = pt.ID_PROC_TIPO";
		sql += "	join comarca com on com.ID_COMARCA = s.ID_COMARCA";
		sql += " 	and data_agendada >= ?";
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += " 	and data_agendada <= ?";
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += "	and aps.AUDI_PROC_STATUS_CODIGO in(?,?)";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO);
		sql += "	group by com.comarca, PROC_TIPO_CODIGO, serv";
		sql += " ) UNION ALL";

		// VALOR_ACORDOS
		sql += " (";
		sql += "	select com.comarca,";
		sql += "	CASE WHEN pt.PROC_TIPO_CODIGO IN (?,?,?) THEN 'Pré-processual' ELSE 'Processual' END AS PROC_TIPO,";
		ps.adicionarLong(ProcessoTipoDt.RECLAMACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.PEDIDO_MEDIACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.HOMOLOGACAO_TRANSACAO_EXTRAJUDICIAL);
		sql += "	serv, 0 AS DESIGNADAS, 0 AS REALIZADAS, 0 AS ACORDOS, nvl(sum(ap.valor_acordo),0) as VALOR_ACORDOS from AUDI a";
		sql += "	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI";
		sql += "	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS";
		sql += "	join serv s on a.id_serv = s.id_serv";
		sql += " 	join proc p on ap.id_proc = p.id_proc";
		sql += " 	join proc_tipo pt on p.ID_PROC_TIPO = pt.ID_PROC_TIPO";
		sql += "	join comarca com on com.ID_COMARCA = s.ID_COMARCA";
		sql += " 	and data_agendada >= ?";
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += " 	and data_agendada <= ?";
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += "	and aps.AUDI_PROC_STATUS_CODIGO in(?,?)";
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
		ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO);
		sql += "	group by com.comarca, PROC_TIPO_CODIGO, serv";
		sql += " )";

		sql += " )";
		sql += " group by comarca, PROC_TIPO, serv";
		sql += " order by PROC_TIPO, comarca, serv";

		try {
			String servAnteriorNome = null;
			String comarcaAnteriorNome = null;
			RelatorioSumarioAudienciasComarcaDiaDt obTemp = null;
			RelatorioSumarioAudienciasComarcaDiaDt obTempComplemento = null;
			rs = consultar(sql.toString(), ps);
			boolean flagDesignadas = false;
			boolean flagRealizadas = false;
			boolean flagAcordos = false;

			while (rs.next()) {

				obTemp = new RelatorioSumarioAudienciasComarcaDiaDt();

				obTemp.setComarca(rs.getString("COMARCA"));
				obTemp.setServentiaRelatorio(rs.getString("SERV"));

				obTemp.setQtdDesignadas(rs.getLong("DESIGNADAS"));
				obTemp.setQtdRealizadas(rs.getLong("REALIZADAS"));
				obTemp.setQtdAcordos(rs.getLong("ACORDOS"));
				obTemp.setValorAcordos(rs.getLong("VALOR_ACORDOS"));

				obTemp.setServSubtipo(rs.getString("PROC_TIPO"));

				listaProcessos.add(obTemp);

			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		if (listaProcessos.isEmpty()) {
			throw new MensagemException("Não foi encontrada nenhuma audiência para a data especificada.");
		}

		return listaProcessos;

	}

	
	/**
	 * Relatório destinado ao NUPEMEC.
	 * 
	 * @param idComarca
	 * @param dataInicial
	 * @param dataFinal
	 * @author hrrosa
	 * @return
	 * @throws Exception
	 */
	public List relAnaliticoAudiencias(String idComarca, String idServentia, String dataInicial, String dataFinal) throws Exception {
		
		HashMap<String, RelatorioAudienciasDt> mapRelAudienciasDt = new HashMap<String, RelatorioAudienciasDt>();
		String sql = "";
		String designadas = "";
		String realizadas = "";
		String acordos = "";
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaProcessos = new ArrayList();

		sql += "( ";
		sql += "		select com.comarca, upper(SERV) as SERV, ";
		sql += "		CASE WHEN pt.PROC_TIPO_CODIGO IN (?,?,?) THEN 'Pré-processual' ELSE 'Processual' END AS procTipo,";
		ps.adicionarLong(ProcessoTipoDt.RECLAMACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.PEDIDO_MEDIACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.HOMOLOGACAO_TRANSACAO_EXTRAJUDICIAL);
		sql += "		P.PROC_NUMERO || '.' || P.DIGITO_VERIFICADOR AS numeroProcesso, to_char(A.DATA_AGENDADA, 'dd/mm/yyyy') AS dataAudiencia, ";
		sql += "	 'DESIGNADA' as STATUS, aps.AUDI_PROC_STATUS as statusDetalhado, AP.VALOR_ACORDO as valorAcordo ";
		sql += "		FROM AUDI a ";
		sql += "	 	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI ";
		sql += "	 	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS ";
		sql += "	 	join serv s on a.id_serv = s.id_serv ";
		sql += "	 	join proc p on ap.id_proc = p.id_proc ";
		sql += "	 	join proc_tipo pt on p.ID_PROC_TIPO = pt.ID_PROC_TIPO ";
		sql += "	 	join comarca com on com.ID_COMARCA = s.ID_COMARCA ";
		sql += "	 	and data_agendada >= ? ";
			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += "	 	and data_agendada <= ? ";
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += "	 	and aps.AUDI_PROC_STATUS_CODIGO in( ?,?,?,?,?,?,?,?,?,?,?,?) ";
			ps.adicionarLong(AudienciaProcessoStatusDt.NAO_REALIZADA);
			ps.adicionarLong(AudienciaProcessoStatusDt.NEGATIVADA);
			ps.adicionarLong(AudienciaProcessoStatusDt.REMARCADA);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_ACORDO);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
			ps.adicionarLong(AudienciaProcessoStatusDt.DESMARCAR_PAUTA);
			
		sql += " 		AND s.ID_COMARCA = ? ";
			ps.adicionarLong(idComarca);
		sql += " 		AND s.ID_SERV = ? ";
			ps.adicionarLong(idServentia);
		sql += "	 ) UNION ALL ";

			// REALIZADAS
		sql += "	 ( ";
		sql += "		select com.comarca, upper(SERV) as SERV, ";
		sql += "		CASE WHEN pt.PROC_TIPO_CODIGO IN (?,?,?) THEN 'Pré-processual' ELSE 'Processual' END AS procTipo,";
		ps.adicionarLong(ProcessoTipoDt.RECLAMACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.PEDIDO_MEDIACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.HOMOLOGACAO_TRANSACAO_EXTRAJUDICIAL);
		sql += "		P.PROC_NUMERO || '.' || P.DIGITO_VERIFICADOR AS numeroProcesso, to_char(A.DATA_AGENDADA, 'dd/mm/yyyy') AS dataAudiencia, ";
		sql += "	 'REALIZADA' as STATUS, aps.AUDI_PROC_STATUS as statusDetalhado, AP.VALOR_ACORDO as valorAcordo ";
		sql += "		FROM AUDI a ";
		sql += "	 	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI ";
		sql += "	 	join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS ";
		sql += "		join serv s on a.id_serv = s.id_serv ";
		sql += "	 	join proc p on ap.id_proc = p.id_proc ";
		sql += "	 	join proc_tipo pt on p.ID_PROC_TIPO = pt.ID_PROC_TIPO ";
		sql += "		join comarca com on com.ID_COMARCA = s.ID_COMARCA ";
		sql += "	 	and data_agendada >= ? ";
			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += "	 	and data_agendada <= ? ";
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += "	 	and aps.AUDI_PROC_STATUS_CODIGO in( ?,?,?,?,?,?,?,?) ";
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_SENTENCA);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_SENTENCA);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_COM_MERITO);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_SEM_MERITO);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SEM_ACORDO);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
		sql += " 		AND s.ID_COMARCA = ? ";
			ps.adicionarLong(idComarca);
		sql += " 		AND s.ID_SERV = ? ";
			ps.adicionarLong(idServentia);
		sql += "	 ) UNION ALL ";

			// ACORDOS
		sql += "	 ( ";
		sql += "		select com.comarca, upper(SERV) as SERV, ";
		sql += "		CASE WHEN pt.PROC_TIPO_CODIGO IN (?,?,?) THEN 'Pré-processual' ELSE 'Processual' END AS procTipo,";
		ps.adicionarLong(ProcessoTipoDt.RECLAMACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.PEDIDO_MEDIACAO_PRE_PROCESSUAL);
		ps.adicionarLong(ProcessoTipoDt.HOMOLOGACAO_TRANSACAO_EXTRAJUDICIAL);
		sql += "		P.PROC_NUMERO || '.' || P.DIGITO_VERIFICADOR AS numeroProcesso, to_char(A.DATA_AGENDADA, 'dd/mm/yyyy') AS dataAudiencia, ";
		sql += "	 	'ACORDO' as STATUS, aps.AUDI_PROC_STATUS as statusDetalhado, AP.VALOR_ACORDO as valorAcordo ";
		sql += "		FROM AUDI a ";
		sql += "	 	join audi_proc ap on a.ID_AUDI = ap.ID_AUDI ";
		sql += "		join AUDI_PROC_STATUS aps on ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS ";
		sql += "		join serv s on a.id_serv = s.id_serv ";
		sql += "	 	join proc p on ap.id_proc = p.id_proc ";
		sql += "	 	join proc_tipo pt on p.ID_PROC_TIPO = pt.ID_PROC_TIPO ";
		sql += "		join comarca com on com.ID_COMARCA = s.ID_COMARCA ";
		sql += "	 	and data_agendada >= ? ";
			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		sql += "	 	and data_agendada <= ? ";
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		sql += "		and aps.AUDI_PROC_STATUS_CODIGO in(?,?) ";
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_COM_ACORDO);
			ps.adicionarLong(AudienciaProcessoStatusDt.REALIZADA_SENTENCA_HOMOLOGACAO);
		sql += " 		AND s.ID_COMARCA = ? ";
			ps.adicionarLong(idComarca);
		sql += " 		AND s.ID_SERV = ? ";
			ps.adicionarLong(idServentia);
		sql += "	 ) ";

		sql += " ORDER BY comarca, serv, procTipo, status, dataAudiencia ";
		
		try {
			String servAnteriorNome = null;
			String comarcaAnteriorNome = null;
			RelatorioAudienciasDt relatorioAudienciasDt = null;
			RelatorioAudienciasDt relatorioAudienciasDtComplemento = null;
			rs = consultar(sql.toString(), ps);
			boolean flagDesignadas = false;
			boolean flagRealizadas = false;
			boolean flagAcordos = false;

			while (rs.next()) {

				relatorioAudienciasDt = new RelatorioAudienciasDt();

				relatorioAudienciasDt.setComarca(rs.getString("COMARCA"));
				relatorioAudienciasDt.setServ(rs.getString("SERV"));

				relatorioAudienciasDt.setProcTipo(rs.getString("procTipo"));
				relatorioAudienciasDt.setNumeroProcesso(rs.getString("numeroProcesso"));
				relatorioAudienciasDt.setDataAudiencia(rs.getString("dataAudiencia"));
				relatorioAudienciasDt.setStatus(rs.getString("STATUS"));
				relatorioAudienciasDt.setStatusDetalhado(rs.getString("statusDetalhado"));
				
				relatorioAudienciasDt.setValorAcordo(BigDecimal.valueOf(rs.getDouble("valorAcordo")));
				if(rs.getString("valorAcordo") == null || rs.getString("valorAcordo").isEmpty()){
					relatorioAudienciasDt.setValorAcordo(null);
				}

				listaProcessos.add(relatorioAudienciasDt);

			}

		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}

		if (listaProcessos.isEmpty()) {
			throw new MensagemException("Não foi encontrada nenhuma audiência para a comarca e data escolhidas.");
		}

		return listaProcessos;

	}
	

	/**
	 * Método de geração do relatório estatistico de movimentação de recursos.
	 * 
	 * @param dataInicial
	 *            - data inicial da movimentação
	 * @return lista de recursos
	 * @throws Exception
	 * @author fvmeireles
	 */
	public List relatorioEstatisticoNugep(String dataInicial, String dataFinal) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		RelatorioRecursoRepetitivoDt obTemp = null;
		List listTemp = new ArrayList<>();

		//
		// busca movimentacao de recursos(RESP,IRDR,IAC) (STJ).
		//

		ps = new PreparedStatementTJGO();
		sql = new StringBuffer();

		sql.append(" SELECT mt.movi_tipo_codigo, COUNT(mo.id_movi) AS quantidade FROM projudi.movi mo"
				+ " INNER JOIN projudi.movi_tipo mt ON mo.id_movi_tipo = mt.id_movi_tipo"
				+ " AND mt.movi_tipo_codigo IN (?, ?, ?, ?, ?, ?, ?)"
				+ " WHERE mo.data_realizacao BETWEEN ? AND ? GROUP BY mt.movi_tipo_codigo ORDER BY mt.movi_tipo_codigo");

		ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_ADMITIDO);
		ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_NAO_ADMITIDO);
		ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_REPETITIVO_NAO_ADMITIDO_CONSONANCIA);
		ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_REPETITIVO_ENCAMINHADO_JUIZO_RETRATACAO);
		ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_ADMITIDO_IRDR);
		ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_REPRESENTATIVO_CONTROVERSIA);
		ps.adicionarLong(MovimentacaoTipoDt.ADMITIDO_IAC);

		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);

		try {

			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				obTemp = new RelatorioRecursoRepetitivoDt();
				obTemp.setTipoMovimentoCodigo(rs.getString("movi_tipo_codigo"));
				obTemp.setQuantidade(rs.getLong("quantidade"));
				listTemp.add(obTemp);
			}

			//
			// busca recurso especial sem decisão de admissibilidade.
			//

			ps = new PreparedStatementTJGO();
			sql = new StringBuffer();

			sql.append("SELECT COUNT(1) AS quantidade FROM ("
					+ " SELECT p.id_proc, p.proc_numero, pt.proc_tipo as procTipo,  MIN(rp.data_inclusao)"
					+ " FROM projudi.recurso_parte rp" + " INNER JOIN projudi.recurso r ON r.id_recurso = rp.id_recurso"
					+ " INNER JOIN projudi.proc p ON p.id_proc = r.id_proc"
					+ " INNER JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = rp.id_proc_tipo"
					+ " INNER JOIN projudi.serv s ON p.id_serv = s.id_serv"
					+ " INNER JOIN projudi.proc_status ps ON ps.id_proc_status = p.id_proc_status"
					+ " WHERE s.serv_codigo = ? AND ps.proc_status_codigo <> ? AND ps.proc_status_codigo <> ?"
					+ " AND ps.proc_status_codigo <> ? AND pt.proc_tipo_codigo in (?)" + " AND NOT EXISTS ("
					+ " SELECT m.id_movi FROM projudi.movi m"
					+ " INNER JOIN PROJUDI.movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo"
					+ " WHERE m.id_proc = p.id_proc AND" + " M.data_realizacao <= ? AND "
					+ " mt.movi_tipo_codigo IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))"
					+ " GROUP BY p.id_proc, p.proc_numero, pt.proc_tipo)");

			ps.adicionarLong(RelatorioEstatisticaDt.ASSESSORIA_RECURSOS_CONSTITUCIONAIS);

			ps.adicionarLong(ProcessoStatusDt.ARQUIVADO);
			ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO);
			ps.adicionarLong(ProcessoStatusDt.ARQUIVADO_PROVISORIAMENTE);

			ps.adicionarLong(ProcessoTipoDt.RECURSO_ESPECIAL);

			ps.adicionarDateTimeUltimaHoraDia(dataFinal);

			ps.adicionarLong(MovimentacaoTipoDt.PROCESSO_SUSPENSO_RECURSO_ESPECIAL_REPETITIVO);
			ps.adicionarLong(MovimentacaoTipoDt.PROCESSO_SUSPENSO);
			ps.adicionarLong(MovimentacaoTipoDt.SUSPENSÃO_SOBRESTAMENTO);
			ps.adicionarLong(MovimentacaoTipoDt.DECISÃO_NEGADO_SEGUIMENTO_RECURSO);
			ps.adicionarLong(MovimentacaoTipoDt.PROCESSO_SUSPENSO_RECURSO_EXTRAORDINARIO_REPERCUSSAO_GERAL);
			ps.adicionarLong(MovimentacaoTipoDt.DECISAO_DESPACHO_HOMOLOGACAO);
			ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_ADMITIDO);
			ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_NAO_ADMITIDO);
			ps.adicionarLong(MovimentacaoTipoDt.EXTINTO_DESISTENCIA);
			ps.adicionarLong(MovimentacaoTipoDt.TRANSITADO_JULGADO);
			ps.adicionarLong(MovimentacaoTipoDt.DECISAO_SUSPENSO_SOBRESTADO_DECISAO_JUDICIAL);
			ps.adicionarLong(MovimentacaoTipoDt.HOMOLOGADO_DESISTENCIA);
			ps.adicionarLong(MovimentacaoTipoDt.DESPACHO_DETERMINANDO_ARQUIVAMENTO);
			ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_REPETITIVO_NAO_ADMITIDO_CONSONANCIA);
			ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_REPETITIVO_ENCAMINHADO_JUIZO_RETRATACAO);
			ps.adicionarLong(MovimentacaoTipoDt.RECURSO_ESPECIAL_ADMITIDO_IRDR);

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				obTemp = new RelatorioRecursoRepetitivoDt();
				obTemp.setTipoMovimentoCodigo("Recurso Especial aguardando juízo de admissibilidade");
				obTemp.setQuantidade(rs.getLong("quantidade"));
				listTemp.add(obTemp);
			}
			//
			// busca recursos autuados.
			//
			ps = new PreparedStatementTJGO();
			sql = new StringBuffer();

			sql.append("SELECT procTipoCodigo, COUNT(1) AS quantidade FROM");
			sql.append(" (SELECT p.proc_numero, p.digito_verificador, pt.proc_tipo_codigo AS procTipoCodigo,  MIN(rp.data_inclusao)");
			sql.append(" FROM projudi.recurso_parte rp");
			sql.append(" INNER JOIN projudi.recurso r ON r.id_recurso = rp.id_recurso");
			sql.append(" INNER JOIN projudi.proc p ON p.id_proc = r.id_proc");
			sql.append(" INNER JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = rp.id_proc_tipo");
			sql.append(" WHERE pt.proc_tipo_codigo in (?,?, ?) AND rp.data_inclusao IS NOT NULL");
			sql.append(" AND TO_DATE(rp.data_inclusao) BETWEEN ? AND ? GROUP BY p.proc_numero, p.digito_verificador, pt.proc_tipo_codigo)");
			sql.append(" GROUP BY procTipoCodigo");

			ps.adicionarLong(ProcessoTipoDt.AGRAVO_STJ);
			ps.adicionarLong(ProcessoTipoDt.AGRAVO_INTERNO_DECISOES_APLICACAO_STJ);
			ps.adicionarLong(ProcessoTipoDt.RECURSO_ESPECIAL);

			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);

			rs = consultar(sql.toString(), ps);

			while (rs.next()) {
				obTemp = new RelatorioRecursoRepetitivoDt();
				obTemp.setTipoMovimentoCodigo(rs.getString("procTipoCodigo"));
				obTemp.setQuantidade(rs.getLong("quantidade"));						
				listTemp.add(obTemp);
			}
			//
			// total recursos suspensos(STJ).
			//
			ps = new PreparedStatementTJGO();
			sql = new StringBuffer();

			sql.append("SELECT COUNT (pt.id_proc_tema) AS quantidade" + " FROM projudi.proc_tema pt"
					+ " INNER JOIN projudi.tema t ON t.id_tema = pt.id_tema"
					+ " INNER JOIN projudi.tema_origem teo ON teo.id_tema_origem = t.id_tema_origem"
					+ " WHERE  pt.data_sobrestado_final is null AND "
					+ " teo.tema_origem = 'STJ' AND pt.data_sobrestado BETWEEN ? AND ?");

			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);

			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				obTemp = new RelatorioRecursoRepetitivoDt();
				obTemp.setTipoMovimentoCodigo("Total de Processos Sobrestados");
				obTemp.setQuantidade(rs.getLong("quantidade"));
				listTemp.add(obTemp);
			}
			//
			// total recursos suspensos por temas repetitivos(STJ).
			//
			ps = new PreparedStatementTJGO();
			sql = new StringBuffer();

			sql.append("SELECT t.tema_codigo || '/' || teo.tema_origem AS codigo, COUNT (pt.id_proc_tema) AS quantidade"
					+ " FROM projudi.proc_tema pt" + " INNER JOIN projudi.tema t ON t.id_tema = pt.id_tema"
					+ " INNER JOIN projudi.tema_origem teo ON teo.id_tema_origem = t.id_tema_origem"
					+ " WHERE  pt.data_sobrestado_final is null AND "
					+ " teo.tema_origem = 'STJ' AND pt.data_sobrestado BETWEEN ? AND ?"
					+ " GROUP BY t.tema_codigo, teo.tema_origem ORDER BY t.tema_codigo");

			ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
			ps.adicionarDateTimeUltimaHoraDia(dataFinal);

			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				obTemp = new RelatorioRecursoRepetitivoDt();
				obTemp.setTipoMovimentoCodigo("      " + rs.getString("codigo"));
				obTemp.setQuantidade(rs.getLong("quantidade"));
				listTemp.add(obTemp);
			}

		} finally

		{
			try {
				if (rs != null)
					rs.close();
			} catch (

			Exception e) {
			}
		}
		return listTemp;
	}

	/**
	 * Método de geração do relatório analítico de recursos repetitivos por
	 * serventia
	 * 
	 * @param dataInicial
	 *            - data inicial de sobrestamento
	 * @param dataFinal
	 *            - data final de sobrestamento
	 * @param idTema
	 *            - ID do tema
	 * @return lista de recursos
	 * @throws Exception
	 * @author fvmeireles
	 */
	public List relAnaliticoRecursoRepetitivoPorServentia(String dataInicial, String dataFinal, String idTema)
			throws Exception {
		List listaRecursos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int status_processo = 0;

		sql.append(
				" SELECT s.serv AS nomeServentia, p.proc_numero || '.' || p.digito_verificador AS numeroProcesso, tt.tema_tipo as tipoRecurso,");
		sql.append(" ps.proc_status_codigo AS statusProcesso,");
		sql.append(
				" TO_CHAR (pt.data_sobrestado, 'dd/mm/yyyy') AS dataSobrestado, t.tema_codigo || '/' || teo.tema_origem AS temaCodigo");
		sql.append(" FROM projudi.proc_tema pt");
		sql.append(" INNER JOIN projudi.tema t ON t.id_tema = pt.id_tema");
		sql.append(" INNER JOIN projudi.tema_origem teo ON teo.id_tema_origem = t.id_tema_origem");
		sql.append(" INNER JOIN projudi.tema_tipo tt ON tt.id_tema_tipo = t.id_tema_tipo");
		sql.append(" INNER JOIN projudi.proc p ON p.id_proc = pt.id_proc");
		sql.append(" INNER JOIN projudi.serv s ON s.id_serv = p.id_serv");
		sql.append(" INNER JOIN projudi.proc_status ps ON ps.id_proc_status = p.id_proc_status");
		sql.append(" WHERE pt.data_sobrestado_final is null ");
		sql.append(" AND pt.data_sobrestado BETWEEN ? AND ?");

		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);

		if (idTema != null && !idTema.equals("")) {
			sql.append(" AND pt.id_tema = ?");
			ps.adicionarLong(idTema);
		}
		sql.append(" ORDER BY s.serv, dataSobrestado");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				RelatorioRecursoRepetitivoDt obTemp = new RelatorioRecursoRepetitivoDt();
				obTemp.setNomeServentia(rs.getString("nomeServentia"));
				obTemp.setNumeroProcesso(rs.getString("numeroProcesso"));
				obTemp.setTipoRecurso(rs.getString("tipoRecurso"));
				obTemp.setDataSobrestado(rs.getString("dataSobrestado"));
				obTemp.setTemaCodigo(rs.getString("temaCodigo"));
				obTemp.setStatusProcesso(Integer.toString(rs.getInt("statusProcesso")));
				if (obTemp.getStatusProcesso().equalsIgnoreCase("2")) // marcar processos com status diferente de
																		// suspenso
					obTemp.setStatusProcesso("");
				else
					obTemp.setStatusProcesso("*");
				listaRecursos.add(obTemp);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaRecursos;
	}
	/**
	 * Método de processos distribuidos por serventia em um periodo.
	 * 
	 * @param area distribuicao - serventia - usuario - data inicio distribuicao - data final distribuicao.
	 *           
	 * @return lista de processos por serventia, magistrado e tipo.
	 * 
	 * @throws Exception
	 * 
	 * @author fvmeireles
	 */	
	public List processoDistribuidoPorServentiaAnalitico(String idAreaDistribuicao, String idServentia,
			String idUsuario, String dataInicial, String dataFinal, String tipoSaida, String idServentiaSessao)
			throws Exception {
		List listaProcessos = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append("SELECT tab.*,");
		sql.append(" (SELECT   a.assunto FROM projudi.proc_assunto pa");
		sql.append(" INNER JOIN projudi.assunto a ON a.id_assunto = pa.id_assunto");
		sql.append(" WHERE  pa.id_proc_assunto in");
		sql.append(" (SELECT MAX(pa.id_proc_assunto) FROM projudi.proc_assunto  pa");
		sql.append(" INNER JOIN projudi.assunto a ON a.id_assunto = pa.id_assunto");
		sql.append(" WHERE pa.id_proc = tab.id_proc");
		sql.append(" )) AS nomeAssunto FROM (");
		sql.append(" SELECT  p.id_proc, s.serv AS nomeServentia, u.nome AS nomeUsuario,");
		sql.append(" p.proc_numero || '-' ||  p.digito_verificador AS numeroProcesso,");
		sql.append(" pt.proc_tipo AS nomeClasse, TO_CHAR (pl.data, 'dd/mm/yyyy') AS dataRecebimento,");
		sql.append(" plt.ponteiro_log_tipo AS distribuicaoTipo");
		sql.append(" FROM projudi.ponteiro_log pl");
		sql.append(" INNER JOIN projudi.ponteiro_log_tipo plt ON");
		sql.append(" plt.id_ponteiro_log_tipo = pl.id_ponteiro_log_tipo");
		sql.append(" INNER JOIN projudi.area_dist ad ON pl.id_area_dist = ad.id_area_dist");
		sql.append(" INNER JOIN projudi.proc p ON p.id_proc = pl.id_proc");
		sql.append(" INNER JOIN projudi.serv s ON s.id_serv = pl.id_serv");
		sql.append(" INNER JOIN projudi.serv_cargo sc ON sc.id_serv_cargo = pl.id_serv_cargo");
		sql.append(" INNER JOIN projudi.usu_serv_grupo usg ON");
		sql.append(" usg.id_usu_serv_grupo = sc.id_usu_serv_grupo");
		sql.append(" INNER JOIN projudi.usu_serv us ON us.id_usu_serv = usg.id_usu_serv");
		sql.append(" INNER JOIN projudi.usu u ON u.id_usu = us.id_usu");
		sql.append(" INNER JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = p.id_proc_tipo");
		sql.append(" WHERE  plt.id_ponteiro_log_tipo IN (?,?,?,?,?,?)");
		sql.append(" AND pl.data BETWEEN ? AND ?  AND ad.id_area_dist = ?");

		ps.adicionarLong(PonteiroLogTipoDt.DISTRIBUICAO);
		ps.adicionarLong(PonteiroLogTipoDt.REDISTRIBUICAO);
		ps.adicionarLong(PonteiroLogTipoDt.GANHO_RESPONSABILIDADE);
		ps.adicionarLong(PonteiroLogTipoDt.PERDA_RESPONSABILIDADE);
		ps.adicionarLong(PonteiroLogTipoDt.COMPENSACAO);
		ps.adicionarLong(PonteiroLogTipoDt.CORRECAO);

		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);
		ps.adicionarDateTimeUltimaHoraDia(dataFinal);
		ps.adicionarLong(idAreaDistribuicao);

		if (tipoSaida.equalsIgnoreCase("serventiaSelecionada")) {
			sql.append(" AND s.id_serv = ?");
			ps.adicionarLong(idServentia);
		} else {
			if (tipoSaida.equalsIgnoreCase("serventiaSessao")) {
				sql.append(" AND s.id_serv = ?");
				ps.adicionarLong(idServentiaSessao);
			}
		}

		if (idUsuario != null && !idUsuario.equals("")) {
			sql.append(" AND u.id_usu = ?");
			ps.adicionarLong(idUsuario);
		}

		sql.append(" ORDER BY nomeServentia, nomeUsuario, plt.ponteiro_log_tipo, pl.data) tab");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				ProcessoDistribuidoPorServentiaDt obTemp = new ProcessoDistribuidoPorServentiaDt();
				obTemp.setDistribuicaoTipo(rs.getString("distribuicaoTipo"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setNomeServentia(rs.getString("nomeServentia"));
				obTemp.setNumeroProcesso(rs.getString("numeroProcesso"));
				obTemp.setNomeClasse(rs.getString("nomeClasse"));
				obTemp.setDataRecebimento(rs.getString("dataRecebimento"));
				obTemp.setNomeAssunto(rs.getString("nomeAssunto"));
				listaProcessos.add(obTemp);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaProcessos;
	}
	/**
	 * Método de totais de processos distribuidos por serventia em um periodo.
	 * 
	 * @param area distribuicao - serventia - usuario - data inicio distribuicao - data final distribuicao.
	 *           
	 * @return lista de totais de processos por serventia, magistrado e tipo.
	 * 
	 * @throws Exception
	 * 
	 * @author fvmeireles
	 */	
	public List processoDistribuidoPorServentiaSintetico(String idAreaDistribuicao, String idServentia, String idUsuario,
			String dataInicial, String dataFinal, String tipoSaida, String  idServentiaSessao) throws Exception {
		List listaTotais = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();	 
		
		sql.append("SELECT nomeServentia, nomeUsuario,"
				+ " sum(decode (tipo, ?, total, 0)) as distribuicaoProcesso,"
				+ " sum(decode (tipo, ?, total, 0)) as redistribuicaoProcesso,"
				+ " sum(decode (tipo, ?, total, 0)) as ganhoResponsabilidade,"
				+ " sum(decode (tipo, ?, total, 0)) as perdaResponsabilidade,"
				+ " sum( case when tipo = ? and indicador = 1 then total else 0 end) as ganhoCompensacao,"
				+ " sum( case when tipo = ? and indicador = -1 then total else 0 end) as perdaCompensacao,"
				+ " sum( case when tipo = ? and indicador = 1 then total else 0 end) as ganhoCorrecao,"
				+ " sum( case when tipo = ? and indicador = -1 then total else 0 end) as perdaCorrecao"
				+ "	FROM ( (SELECT s.serv AS nomeServentia, u.nome AS nomeUsuario,"
				+ " plt.id_ponteiro_log_tipo AS tipo, pl.qtd as indicador, COUNT(1) AS total"
				+ " FROM projudi.ponteiro_log pl"
				+ " INNER JOIN projudi.ponteiro_log_tipo plt ON plt.id_ponteiro_log_tipo = pl.id_ponteiro_log_tipo"
				+ " INNER JOIN projudi.area_dist ad ON pl.id_area_dist = ad.id_area_dist"
				+ " INNER JOIN projudi.serv s ON s.id_serv = pl.id_serv"
				+ " INNER JOIN projudi.serv_cargo sc ON sc.id_serv_cargo = pl.id_serv_cargo"
				+ " INNER JOIN projudi.usu_serv_grupo usg ON usg.id_usu_serv_grupo = sc.id_usu_serv_grupo"
				+ " INNER JOIN projudi.usu_serv us ON us.id_usu_serv = usg.id_usu_serv"
				+ " INNER JOIN projudi.usu u ON u.id_usu = us.id_usu" 			
				+ " WHERE  plt.id_ponteiro_log_tipo in (?, ?, ?, ?, ?, ?) AND pl.data BETWEEN ? AND ? AND ad.id_area_dist = ?");		
		
		
		ps.adicionarLong(PonteiroLogTipoDt.DISTRIBUICAO);
		ps.adicionarLong(PonteiroLogTipoDt.REDISTRIBUICAO);
		ps.adicionarLong(PonteiroLogTipoDt.GANHO_RESPONSABILIDADE);
		ps.adicionarLong(PonteiroLogTipoDt.PERDA_RESPONSABILIDADE);
		ps.adicionarLong(PonteiroLogTipoDt.COMPENSACAO);
		ps.adicionarLong(PonteiroLogTipoDt.COMPENSACAO);
		ps.adicionarLong(PonteiroLogTipoDt.CORRECAO);	
		ps.adicionarLong(PonteiroLogTipoDt.CORRECAO);				
		
		ps.adicionarLong(PonteiroLogTipoDt.DISTRIBUICAO);
		ps.adicionarLong(PonteiroLogTipoDt.REDISTRIBUICAO);
		ps.adicionarLong(PonteiroLogTipoDt.GANHO_RESPONSABILIDADE);
		ps.adicionarLong(PonteiroLogTipoDt.PERDA_RESPONSABILIDADE);
		ps.adicionarLong(PonteiroLogTipoDt.COMPENSACAO);
		ps.adicionarLong(PonteiroLogTipoDt.CORRECAO);	
		
		ps.adicionarDateTimePrimeiraHoraDia(dataInicial);

		ps.adicionarDateTimeUltimaHoraDia(dataFinal);

		ps.adicionarLong(idAreaDistribuicao);
		
		if (tipoSaida.equalsIgnoreCase("serventiaSelecionada")) {
			sql.append(" AND s.id_serv = ?");    
		    ps.adicionarLong(idServentia);
		} else {
			if (tipoSaida.equalsIgnoreCase("serventiaSessao")) {
				sql.append(" AND s.id_serv = ?");    
			    ps.adicionarLong(idServentiaSessao);
		    }
		}	
		
		if (idUsuario != null && !idUsuario.equals("")) { //
			sql.append(" AND u.id_usu = ?");
			ps.adicionarLong(idUsuario);
		}
		
		sql.append(" GROUP BY s.serv, u.nome, plt.id_ponteiro_log_tipo, pl.qtd))"
	         	+ "	GROUP BY  nomeServentia, nomeUsuario"
	        	+ " ORDER BY nomeServentia, nomeUsuario");		

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				ProcessoDistribuidoPorServentiaDt obTemp = new ProcessoDistribuidoPorServentiaDt();
				obTemp.setNomeServentia(rs.getString("nomeServentia"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario"));
				obTemp.setDistribuicao(rs.getInt("distribuicaoProcesso"));
				obTemp.setRedistribuicao(rs.getInt("redistribuicaoProcesso"));
				obTemp.setGanhoResponsabilidade(rs.getInt("ganhoResponsabilidade"));
				obTemp.setPerdaResponsabilidade(rs.getInt("perdaResponsabilidade"));
				obTemp.setGanhoCompensacao(rs.getInt("ganhoCompensacao"));
				obTemp.setPerdaCompensacao(rs.getInt("perdaCompensacao"));
				obTemp.setGanhoCorrecao(rs.getInt("ganhoCorrecao"));
				obTemp.setPerdaCorrecao(rs.getInt("perdaCorrecao"));
				listaTotais.add(obTemp);
			}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
		return listaTotais;
	}
}
