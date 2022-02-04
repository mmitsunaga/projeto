package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import br.gov.go.tj.projudi.dt.ImportaDadosSPGDt;
import br.gov.go.tj.projudi.dt.ImportaEscalaOficiaisDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatoriosMandadoDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class ImportaDadosSPGPs extends Persistencia {

	private static final long serialVersionUID = -2548786806822055395L;

	public ImportaDadosSPGPs(Connection conexao) {
		Conexao = conexao;
	}

	public List relatorioMensalMandadoGratuitoSpg(String anoMesCompetencia, String nomeComarca, String cpfUsuario)
			throws Exception {

		List listaMandado = new ArrayList();
		String oficialTipo = "";

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append(
				"SELECT m.info_cpf as cpfUsuario, m.nome_oficial as nomeUsuario, m.info_qtd_mes1 as quantMandadoMesAnt1,"
						+ " m.info_qtd_mes2 as quantMandadoMesAnt2,"
						+ " m.info_qtd_mes3 as quantMandadoMesAnt3,"
						+ " m.info_qtd_mes4 as quantMandadoMesAnt4,"
						+ " m.info_qtd_mes5 as quantMandadoMesAnt5,"
						+ " m.info_qtd_mes6 as quantMandadoMesAnt6,"
						+ " m.info_qtd_mes7 as quantMandadoMesAnt7,"
						
						+ " m.codg_banco as codgBanco, m.numr_agencia as numrAgencia, m.numr_conta_operacao as numrContaOperacao,"
						+ " CASE WHEN m.codg_banco = 104 THEN 'CAIXA' ELSE 'OUTROS BANCOS' END AS nomeBanco,"
						+ " m.numr_conta as numrConta, m.numr_conta_dv as numrContaDv, m.nome_comarca as nomeComarca,"
						+ " m.info_ano_mes_competencia as infoAnoMesCompetencia FROM v_spgamapa m WHERE m.info_ano_mes_competencia = ?");

		ps.adicionarString(anoMesCompetencia);

		if (!nomeComarca.equalsIgnoreCase("")) {
			sql.append(" AND m.nome_comarca = ?");
			ps.adicionarString(nomeComarca);
		}

		if (!cpfUsuario.equalsIgnoreCase("")) {
			sql.append(" AND m.info_cpf = ?");
			ps.adicionarLong(cpfUsuario);
		}

		sql.append(" ORDER BY nomeComarca,nomeUsuario");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {

				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setCpfUsuario(rs.getString("cpfUsuario"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario").substring(0, 59));
				oficialTipo = rs.getString("nomeUsuario").substring(59, 60);
				if (oficialTipo.equalsIgnoreCase("C"))
					obTemp.setEscalaTipo("Cenops");
				else if (oficialTipo.equalsIgnoreCase("P"))
					obTemp.setEscalaTipo("Plantão");
				else
					obTemp.setEscalaTipo("Normal");
				obTemp.setQuantFaixaReceber(rs.getInt("quantMandadoMesAnt1"));
				obTemp.setQuantResolutivoReceber(rs.getInt("quantMandadoMesAnt2"));
				obTemp.setNomeComarca(rs.getString("nomeComarca"));
				obTemp.setBanco(rs.getInt("codgBanco"));
				obTemp.setAgencia(rs.getInt("numrAgencia"));
				obTemp.setContaOperacao(rs.getInt("numrContaOperacao"));
				obTemp.setConta(rs.getInt("numrConta"));
				obTemp.setContaDv(rs.getString("numrContaDv"));
				obTemp.setMesAnoCompetencia(rs.getInt("infoAnoMesCompetencia"));
				obTemp.setNomeBanco(rs.getString("nomeBanco"));

				System.out.println(rs.getString("nomeComarca") + "..." + rs.getString("nomeUsuario") + "..."
						+ rs.getInt("infoAnoMesCompetencia") + "..." + rs.getString("quantMandadoMesAnt1") + "..."
						+ rs.getString("quantMandadoMesAnt2") + "..."
				+ rs.getString("quantMandadoMesAnt3") + "..."
				+ rs.getString("quantMandadoMesAnt4") + "..."
				+ rs.getString("quantMandadoMesAnt5") + "..."
				+ rs.getString("quantMandadoMesAnt6") + "..."
						+ rs.getString("quantMandadoMesAnt7") + "...");

				listaMandado.add(obTemp);
			}
			
			
			
	//		RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
		//	obTemp.setCpfUsuario("01480972606");
	//		obTemp.setNomeUsuario("PEDRO MARQUE MONTEIRO");
//			obTemp.setEscalaTipo("Normal");
//			obTemp.setQuantFaixaReceber(90);
//			obTemp.setQuantResolutivoReceber(10);
//			obTemp.setNomeComarca("SENADOR CANEDO");
//			obTemp.setBanco(341);
//			obTemp.setAgencia(4391);		 
//			obTemp.setConta(11817);
//			obTemp.setContaDv("3");
//			obTemp.setMesAnoCompetencia(202011);
//			obTemp.setNomeBanco("OUTROS BANCOS");

//			listaMandado.add(obTemp);		
			

		}
		
		finally {
			if (rs != null)
				rs.close();
		}
		return listaMandado;
	}

	//
	// atualiza locomocao spg
	//
	public List listaMandadosParaLocomocaoSpg(String dataInicial, String dataFinal, String idServentia)
			throws Exception {

		List<ImportaDadosSPGDt> listaMandados = new ArrayList();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("SELECT tab.*,"
				//
				+ " (SELECT CASE WHEN (tab.companheiro is null or tab.companheiro = 0)"
				+ "	THEN SUM(gi.valor_calculado) else SUM(gi.valor_calculado/2) END" + "	FROM projudi.locomocao l"
				+ "	INNER JOIN projudi.guia_item gi ON gi.id_guia_item = l.id_guia_item"
				+ "	WHERE l.id_mand_jud = tab.numrMandado) AS valorLocomocao FROM"
				//
				+ "	(SELECT mj.id_mand_jud AS numrMandado, c.comarca_codigo AS codgComarca,"
				+ " TO_CHAR(mj.data_pagamento_status,'dd/mm/yyyy') AS dataValidacao,"
				+ "	mj.id_usu_pagamento_status AS matrValidacao, u.cpf As numrCpfOficial, "
				+ " mj.id_usu_serv_2 as companheiro FROM projudi.mand_jud mj"
				+ "	INNER JOIN projudi.usu_serv us ON (us.id_usu_serv = mj.id_usu_serv_1 or  us.id_usu_serv = mj.id_usu_serv_2)"
				+ "	INNER JOIN projudi.usu u ON u.id_usu = us.id_usu"
				+ "	INNER JOIN projudi.serv s ON s.id_serv = us.id_serv"
				+ "	INNER JOIN projudi.comarca c ON c.id_comarca = s.id_comarca"
				+ "	WHERE  mj.data_pagamento_status BETWEEN ? AND ?"
				+ "	AND mj.id_mand_jud_pagamento_status = ? AND mj.assistencia = ?" + "	AND us.id_serv = ?) tab");

		ps.adicionarString(dataInicial);
		ps.adicionarString(dataFinal);
		ps.adicionarString(MandadoJudicialDt.ID_PAGAMENTO_AUTORIZADO);
		ps.adicionarLong(MandadoJudicialDt.NAO_ASSISTENCIA);
		ps.adicionarString(idServentia);

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				ImportaDadosSPGDt obTemp = new ImportaDadosSPGDt();
				obTemp.setNumrMandado(rs.getString("numrMandado"));
				obTemp.setValorLocomocao(rs.getDouble("valorLocomocao"));
				obTemp.setCodgComarca(rs.getInt("codgComarca"));
				obTemp.setDataValidacao(rs.getString("dataValidacao"));
				obTemp.setMatricValidacao(rs.getString("matrValidacao"));
				obTemp.setNumrCpfOficial(rs.getString("numrCpfOficial"));
				listaMandados.add(obTemp);
			}
		}

		finally {
			if (rs != null)
				rs.close();
		}
		return listaMandados;
	}

	public boolean consultaLocomocaoSpgPorMandadoCapital(String numrMandado, String numrCpfOficial) throws Exception {

		boolean existe = false;

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;

		try {

			List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
			PreparedStatementTJGO ps = new PreparedStatementTJGO();

			sql.append("select numr_mandado from V_SPGALOCOMOCOES where numr_mandado = ? AND numr_cpf_oficial = ?");

			ps.adicionarLong(numrMandado);
			ps.adicionarLong(numrCpfOficial);

			rs = consultar(sql.toString(), ps);

			if (rs.next()) {
				existe = true;
			}
		}

		finally {
			if (rs != null)
				rs.close();
		}
		return existe;
	}

	public boolean consultaLocomocaoSpgPorMandadoInterior(String numrMandado, String numrCpfOficial) throws Exception {

		boolean existe = false;

		try {
			StringBuffer sql = new StringBuffer();
			ResultSetTJGO rs = null;
			List<ImportaEscalaOficiaisDt> lista = new ArrayList<>();
			PreparedStatementTJGO ps = new PreparedStatementTJGO();

			sql.append("select numr_mandado from V_SPGALOCOMOCOES_REM where numr_mandado = ? AND numr_cpf_oficial = ?");

			ps.adicionarLong(numrMandado);
			ps.adicionarLong(numrCpfOficial);

			rs = consultar(sql.toString(), ps);

			if (rs.next()) {
				existe = true;
			}
		}

		finally {
		}
		return existe;
	}

	public void incluiLocomocaoSpgCapital(ImportaDadosSPGDt objDt) throws Exception {

		try {

			String sql = "";

			sql = "INSERT INTO v_spgalocomocoes (stat_pagamento, numr_mandado, valr_locomocao, codg_comarca, data_validacao,"
					+ " matric_validacao, numr_cpf_oficial) VALUES (";

			sql += objDt.getStatPagamento() + ",";
			sql += objDt.getNumrMandado() + ",";
			sql += objDt.getValorLocomocao() + ",";
			sql += objDt.getCodgComarca() + ",";
			sql += Funcoes.BancoData(objDt.getDataValidacao()) + ",";
			sql += objDt.getMatricValidacao() + ",";
			sql += objDt.getNumrCpfOficial() + ")";

			executarComando(sql);
		}

		finally {
		}
	}

	public void incluiLocomocaoSpgInterior(ImportaDadosSPGDt objDt) throws Exception {

		try {

			String sql = "";

			sql = "INSERT INTO v_spgalocomocoes_rem (stat_pagamento, numr_mandado, valr_locomocao, codg_comarca, data_validacao,"
					+ " matric_validacao, numr_cpf_oficial) VALUES (";

			sql += objDt.getStatPagamento() + ",";
			sql += objDt.getNumrMandado() + ",";
			sql += objDt.getValorLocomocao() + ",";
			sql += objDt.getCodgComarca() + ",";
			sql += Funcoes.BancoData(objDt.getDataValidacao()) + ",";
			sql += objDt.getMatricValidacao() + ",";
			sql += objDt.getNumrCpfOficial() + ")";

			executarComando(sql);
		}

		finally {
		}
	}

	public void alteraPagamentoStatus(String numrMandado, String usuarioSessao) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "UPDATE projudi.mand_jud SET id_mand_jud_pagamento_status = ?," + " id_usu_pagamento_envio = ?,"
				+ " data_pagamento_envio = ?" + " WHERE id_mand_jud = ?";

		ps.adicionarString(MandadoJudicialDt.ID_PAGAMENTO_ENVIADO);
		ps.adicionarString(usuarioSessao);
		ps.adicionarDateTime(Funcoes.getDataHoraAtual());
		ps.adicionarString(numrMandado);
		this.executarUpdateDelete(sql, ps);
	}

	//
	// controle guia saldo
	//

	public String[] consultaProcNumero(int idProc) throws Exception {

		StringBuffer sql = new StringBuffer();
		String array[] = new String[3];
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append("SELECT proc_numero, digito_verificador, ano FROM projudi.proc WHERE id_proc = ?");
		ps.adicionarLong(idProc);

		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			array[0] = rs.getString("proc_numero");
			array[1] = rs.getString("digito_verificador");
			if (array[1].length() == 1)
				array[1] = "0" + array[1];
			array[2] = rs.getString("ano");
		}
		return array;
	}

	public ImportaDadosSPGDt consultaGuiaSaldoSpg(String numrProcesso) throws Exception {

		StringBuffer sql = new StringBuffer();
		ImportaDadosSPGDt objDt = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(
				"SELECT tipo_guia, valr_acao, numr_guia  FROM v_spgaguias WHERE numr_projudi = ? AND tipo_guia IN (?,?)");
		ps.adicionarLong(numrProcesso);
		ps.adicionarLong(ImportaDadosSPGDt.GUIA_SALDO_STATUS_LIBERADA);
		ps.adicionarLong(ImportaDadosSPGDt.GUIA_SALDO_STATUS_NAO_LIBERADA);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			objDt = new ImportaDadosSPGDt();
			objDt.setGuiaSaldoStatus(rs.getString("tipo_guia"));
			objDt.setGuiaSaldoValorAtualizado(rs.getDouble("valr_acao"));
			objDt.setNumeroGuiaSaldo(rs.getString("numr_guia"));
		}
		return objDt;
	}

	public ImportaDadosSPGDt consultaGuiaSaldoProjudi(int idProc) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		ImportaDadosSPGDt objDt = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append(
				"SELECT id_guia_emis, guia_saldo_valor_atualizado, guia_saldo_status, numero_guia_completo FROM projudi.guia_emis WHERE id_proc = ? AND guia_emis = ?");
		ps.adicionarLong(idProc);
		ps.adicionarString("Guia Saldo");
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			objDt = new ImportaDadosSPGDt();
			objDt.setIdGuiaSaldo(rs.getInt("id_guia_emis"));
			objDt.setGuiaSaldoValorAtualizado(rs.getDouble("guia_saldo_valor_atualizado"));
			objDt.setGuiaSaldoStatus(rs.getString("guia_saldo_status"));
			objDt.setNumeroGuiaSaldo(rs.getString("numero_guia_completo"));
		}
		return objDt;
	}

	public void atualizaGuiaSaldoProjudi(int idGuiaSaldo, String guiaSaldoStatus, double valorSaldoAtualizado)
			throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		sql = " UPDATE projudi.guia_emis SET guia_saldo_valor_atualizado = ?, guia_saldo_status = ?  WHERE id_guia_emis = ?";
		ps.adicionarDouble(valorSaldoAtualizado);
		ps.adicionarDouble(guiaSaldoStatus);
		ps.adicionarLong(idGuiaSaldo);
		executarUpdateDelete(sql, ps);
	}

	public void atualizaGuiaSaldoSPG(String numeroGuiaSaldo, double valorSaldoAtualizado) throws Exception {

		String sql = " UPDATE  v_spgaguias SET valr_acao = " + valorSaldoAtualizado + "  WHERE numr_guia = "
				+ numeroGuiaSaldo;
		executarComando(sql);
	}

	public ImportaDadosSPGDt gravaGuiaSaldoProjudi(ImportaDadosSPGDt objDtAdabas, int idProc) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "";

		sql = "INSERT INTO projudi.guia_emis (id_proc, valor_acao, data_recebimento, data_emis, numero_guia_completo,"
				+ " guia_saldo_status, id_usu, data_vencimento, guia_emis, guia_saldo_valor_atualizado) VALUES (?, ?, ?, ?, ?, ?, ?,?,?,?)";
		ps.adicionarLong(idProc);
		ps.adicionarDouble(objDtAdabas.getGuiaSaldoValorAtualizado());
		ps.adicionarDateTime(new Date());
		ps.adicionarDateTime(new Date());
		ps.adicionarString(objDtAdabas.getNumeroGuiaSaldo());
		ps.adicionarString(objDtAdabas.getGuiaSaldoStatus());
		ps.adicionarString("1");
		ps.adicionarDateTime(new Date());
		ps.adicionarString("Guia Saldo");
		ps.adicionarDouble(objDtAdabas.getGuiaSaldoValorAtualizado());
		objDtAdabas.setIdGuiaSaldo(Integer.parseInt(executarInsert(sql, "ID_GUIA_EMIS", ps)));
		return objDtAdabas;
	}

	public List consultaGuiaVinculadaSaldoSpg(String numeroProcesso) throws Exception {
		ImportaDadosSPGDt objDt;
		List<ImportaDadosSPGDt> listaGuiasVinculadasSaldo = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append("SELECT numr_guia FROM v_spgacontrolegrs  WHERE numr_proc_projudi = ?");
		ps.adicionarLong(numeroProcesso);
		rs = consultar(sql.toString(), ps);
		while (rs.next()) {
			objDt = new ImportaDadosSPGDt();
			objDt.setNumeroGuia(rs.getString("numr_guia"));
			listaGuiasVinculadasSaldo.add(objDt);
		}
		return listaGuiasVinculadasSaldo;
	}

	public void vinculaGuiaNaGuiaSaldoProjudi(String numeroGuia, String numeroGuiaSaldo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		sql = " UPDATE projudi.guia_emis SET guia_saldo_status = ?, guia_saldo_numero = ? WHERE numero_guia_completo = ?";
		ps.adicionarLong(ImportaDadosSPGDt.GUIA_SALDO_STATUS_NAO_LIBERADA);
		ps.adicionarLong(numeroGuiaSaldo);
		ps.adicionarLong(numeroGuia);
		executarUpdateDelete(sql, ps);
	}

	public void gravaLogGuiaSaldoSpg(ImportaDadosSPGDt objDt) throws Exception {

		String sql = "INSERT INTO v_spgaloggrsaldo (numr_guia_log, info_sistema_origem, nome_programa_origem, valr_saldo_old,"
				+ " valr_saldo_new, data_gravacao, hora_gravacao) VALUES (";

		sql += objDt.getNumeroGuiaSaldo() + ",";
		sql += "'PROJUDI'" + ",";
		sql += "'PROJUDI'" + ",";
		sql += objDt.getGuiaSaldoValorAnterior() + ",";
		sql += objDt.getGuiaSaldoValorAtualizado() + ",";
		sql += Funcoes.BancoData(new Date()) + ",";
		sql += Funcoes.BancoHora(new Date()) + ")";

		executarComando(sql);
	}

	public List listaGuiasLocomocoesUsadasProjudi(String idMandJud) throws Exception {
		ImportaDadosSPGDt objDt;
		List lista = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append("SELECT DISTINCT ge.numero_guia_completo AS numeroGuia, l.id_mand_jud from projudi.locomocao l"
				+ " INNER JOIN projudi.guia_item gi ON gi.id_guia_item = l.id_guia_item"
				+ " INNER JOIN projudi.guia_emis ge ON ge.id_guia_emis = gi.id_guia_emis" + " WHERE l.id_mand_jud = ?");
		ps.adicionarLong(idMandJud);
		rs = consultar(sql.toString(), ps);
		while (rs.next()) {
			objDt = new ImportaDadosSPGDt();
			objDt.setNumeroGuia(rs.getString("numeroGuia"));
			lista.add(objDt);
		}
		return lista;
	}

	public boolean consultaControleSpg(String numeroGuia) throws Exception {
		boolean retorno = false;
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append("SELECT numr_guia FROM v_spgacontrolegrs WHERE numr_guia = ?");
		ps.adicionarLong(numeroGuia);
		rs = consultar(sql.toString(), ps);
		if (rs.next()) {
			retorno = true;
		}
		return retorno;
	}

	public void gravaControleSpg(ImportaDadosSPGDt objDt) throws Exception {

		String sql = "";

		sql = "INSERT INTO v_spgacontrolegrs (numr_guia, data_gravacao, hora_gravacao, info_sistema_origem,"
				+ " nome_programa_origem, numr_proc_projudi) VALUES (";

		sql += objDt.getNumeroGuia() + ",";
		sql += Funcoes.BancoData(new Date()) + ",";
		sql += Funcoes.BancoHora(new Date()) + ",";
		sql += "'PROJUDI'" + ",";
		sql += "'PROJUDI'" + ",";
		sql += objDt.getProcNumero() + ")";

		executarComando(sql);
	}

	public List consultaLocomocaoVinculadaGuiaSaldo(String idMandJud) throws Exception {
		List listaLocomocaoVinculada = new ArrayList();
		ImportaDadosSPGDt objDt = new ImportaDadosSPGDt();
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append("SELECT l.id_guia_emis AS idGuiaEmis, l.numero_guia_completo AS numeroGuiaSaldo,"
				+ " l.id_proc AS idProc, l.valor_calculado AS valorCalculado, l.id_locomocao AS idLocomocao, l.id_guia_item AS idGuiaItem"
				+ " from projudi.view_locomocao l"
				+ " WHERE l.id_mand_jud = ? AND l.guia_emis = 'Guia Saldo' AND l.id_guia_emis_comp IS NULL");
		ps.adicionarLong(idMandJud);
		rs = consultar(sql.toString(), ps);
		while (rs.next()) {
			objDt = new ImportaDadosSPGDt();
			objDt.setNumeroGuiaSaldo(rs.getString("numeroGuiaSaldo"));
			objDt.setLocomocaoValorUnitario(rs.getDouble("valorCalculado"));
			objDt.setIdGuiaSaldo(rs.getInt("idGuiaEmis"));
			objDt.setIdProc(rs.getString("idProc"));
			objDt.setIdLocomocao(rs.getInt("idLocomocao"));
			objDt.setIdGuiaItem(rs.getInt("idGuiaItem"));
			listaLocomocaoVinculada.add(objDt);
		}
		return listaLocomocaoVinculada;
	}

	public int liberaLocomocaoGuiaSaldo(int idLocomocao, int idGuiaItem) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		int quantidade = 0;
		sql = "DELETE projudi.locomocao WHERE id_locomocao = ?";
		ps.adicionarLong(idLocomocao);
		quantidade = executarUpdateDelete(sql, ps);

		if (quantidade > 0) {
			ps = new PreparedStatementTJGO();
			sql = "DELETE projudi.guia_item WHERE id_guia_item = ?";
			ps.adicionarLong(idGuiaItem);
			quantidade = executarUpdateDelete(sql, ps);

		}

		return quantidade;
	}

	public void atualizaGuiaSaldoProjudiLocomocaoLiberada(String numeroGuiaSaldo, double valorAtual) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		sql = " UPDATE projudi.guia_emis SET guia_saldo_valor_atualizado = ? WHERE numero_guia_completo = ?";
		ps.adicionarDouble(valorAtual);
		ps.adicionarLong(numeroGuiaSaldo);
		executarUpdateDelete(sql, ps);
	}
	////////////////TIRAR
	

	public List mapaFinanceiroMandadoGratuitoSpg(String anoMesCompetencia, String nomeComarca, String cpfUsuario)
			throws Exception {

		List listaMandado = new ArrayList();
		String oficialTipo = "";

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql.append(
				"SELECT m.info_cpf as cpfUsuario, m.nome_oficial as nomeUsuario, m.info_qtd_mes1 as quantMandadoMesAnt1,"
						+ " m.info_qtd_mes2 as quantMandadoMesAnt2, m.info_qtd_mes3 as quantMandadoMesAnt3, m.info_qtd_mes4 as quantMandadoMesAnt4,"
						+ " m.info_qtd_mes5 as quantMandadoMesAnt5, m.info_qtd_mes6 as quantMandadoMesAnt6, m.info_qtd_mes7 as quantMandadoMesAnt7,"
						+ " m.codg_banco as codgBanco, m.numr_agencia as numrAgencia, m.numr_conta_operacao as numrContaOperacao,"
						+ " CASE WHEN m.codg_banco = 104 THEN 'CAIXA' ELSE 'OUTROS BANCOS' END AS nomeBanco,"
						+ " m.numr_conta as numrConta, m.numr_conta_dv as numrContaDv, m.nome_comarca as nomeComarca,"
						+ " m.info_ano_mes_competencia as infoAnoMesCompetencia FROM v_spgamapa m WHERE m.info_ano_mes_competencia = ?");

		ps.adicionarString(anoMesCompetencia);

		if (!nomeComarca.equalsIgnoreCase("")) {
			sql.append(" AND m.nome_comarca = ?");
			ps.adicionarString(nomeComarca);
		}

		if (!cpfUsuario.equalsIgnoreCase("")) {
			sql.append(" AND m.info_cpf = ?");
			ps.adicionarLong(cpfUsuario);

		}

		sql.append(" ORDER BY nomeComarca,nomeUsuario");

		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {

				RelatoriosMandadoDt obTemp = new RelatoriosMandadoDt();
				obTemp.setCpfUsuario(rs.getString("cpfUsuario"));
				obTemp.setNomeUsuario(rs.getString("nomeUsuario").substring(0, 59));
				oficialTipo = rs.getString("nomeUsuario").substring(59, 60);
				if (oficialTipo.equalsIgnoreCase("C"))
					obTemp.setEscalaTipo("Cenops");
				else if (oficialTipo.equalsIgnoreCase("P"))
					obTemp.setEscalaTipo("Plantão");
				else
					obTemp.setEscalaTipo("Normal");
				obTemp.setQuantMandadoMesAnt1(rs.getInt("quantMandadoMesAnt1"));
				obTemp.setQuantMandadoMesAnt2(rs.getInt("quantMandadoMesAnt2"));
				obTemp.setQuantMandadoMesAnt3(rs.getInt("quantMandadoMesAnt3"));
				obTemp.setQuantMandadoMesAnt4(rs.getInt("quantMandadoMesAnt4"));
				obTemp.setQuantMandadoMesAnt5(rs.getInt("quantMandadoMesAnt5"));
				obTemp.setQuantMandadoMesAnt6(rs.getInt("quantMandadoMesAnt6"));
				obTemp.setQuantMandadoMesAnt7(rs.getInt("quantMandadoMesAnt7"));
				obTemp.setNomeComarca(rs.getString("nomeComarca"));
				obTemp.setBanco(rs.getInt("codgBanco"));
				obTemp.setAgencia(rs.getInt("numrAgencia"));
				obTemp.setContaOperacao(rs.getInt("numrContaOperacao"));
				obTemp.setConta(rs.getInt("numrConta"));
				obTemp.setContaDv(rs.getString("numrContaDv"));
				obTemp.setMesAnoCompetencia(rs.getInt("infoAnoMesCompetencia"));
				obTemp.setNomeBanco(rs.getString("nomeBanco"));

				 System.out.println(rs.getString("nomeComarca") + "..." +
				 rs.getString("nomeUsuario") + "..."
				 + rs.getInt("infoAnoMesCompetencia") + "..." +
				 rs.getString("quantMandadoMesAnt1") + "..."
				 + rs.getString("quantMandadoMesAnt2") + "..." +
				 rs.getString("quantMandadoMesAnt3") + "..."
				 + rs.getString("quantMandadoMesAnt4") + "..." +
				 rs.getString("quantMandadoMesAnt5") + "..."
				 + rs.getString("quantMandadoMesAnt6") + "..." +
				 rs.getString("quantMandadoMesAnt7") + "...");

				listaMandado.add(obTemp);
			}
		}

		finally {
			if (rs != null)
				rs.close();
		}
		return listaMandado;
	}
	
	
	
	
	
	
	///////FIM TIRAR
	
	
	
	
	
	
	
	
	
	
	
	
	
}
