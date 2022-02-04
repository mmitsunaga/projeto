package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ImpedimentoTipoDt;
import br.gov.go.tj.projudi.dt.JulgamentoAdiadoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoSecundarioParteDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.SessaoVirtualPublicaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.VotanteDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.types.VotoTipo;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class VotoPs extends Persistencia {

	private static final String VIEW_AUDI_PROC = "PROJUDI.VIEW_AUDI_PROC";
	private static final String TABELA_VOTO_VIRTUAL = "PROJUDI.PEND_VOTO_VIRTUAL";
	private static final String TABELA_PENDENCIA = "PROJUDI.PEND";
	private static final String TABELA_PENDENCIA_RESPONSAVEL = "PROJUDI.PEND_RESP";
	private static final String TABELA_PENDENCIA_RESPONSAVEL_FINALIZADA = "PROJUDI.PEND_FINAL_RESP";
	private static final String VIEW_AUDIENCIA_PROCESSO_COMPLETA = "PROJUDI.VIEW_AUDI_PROC_COMPLETA";
//	private static final String TABELA_AUDIENCIA_PROCESSO = "PROJUDI.AUDI_PROC";
	private static final String TABELA_VOTANTE_TIPO = "PROJUDI.VOTANTE_TIPO";
	private static final String TABELA_VOTANTES = "PROJUDI.AUDI_PROC_VOTANTES";
	private static final String VIEW_PENDENCIA = "PROJUDI.VIEW_PEND";
	private static final String VIEW_PENDENCIA_FINALIZADA = "PROJUDI.VIEW_PEND_FINAL";
	private static final Integer ID_JULGAMENTO_ADIADO_SUSTENTACAO_ORAL = 76;
	private static final String  SUSTENTACAO_ORAL_DEFERIDA = "201";

	/**
	 * 
	 */
	private static final long serialVersionUID = 8655351812316252660L;

	public VotoPs(Connection conexao) {
		Conexao = conexao;
	}

	public void inserir(VotoDt dados) throws Exception {
		List<String> colunas = new ArrayList<>();
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		adicionarLong(colunas, ps, "ID_VOTO_VIRTUAL_TIPO", dados.getIdVotoTipo());
		adicionarLong(colunas, ps, "ID_PEND", dados.getIdPendencia());
		adicionarLong(colunas, ps, "ID_AUDI_PROC", dados.getIdAudienciaProcesso());
		if(StringUtils.isNotEmpty(dados.getIdAudienciaProcessoStatusVencido())) adicionarLong(colunas, ps, "ID_AUDI_PROC_STATUS_VENCIDO", dados.getIdAudienciaProcessoStatusVencido()); // jvosantos - 04/06/2019 10:41 - Salva o status vencido na tabela de votos
		adicionarBoolean(colunas, ps, "VOTO_ATIVO", dados.isAtivo()); // jvosantos - 06/01/2020 17:04 - Salva se o voto é ativo ou não

		sql = String
				.format(
						"INSERT INTO PROJUDI.PEND_VOTO_VIRTUAL (%s) VALUES (%s)",
						String.join(",", colunas),
						getSqlValues(colunas.size()));

		dados.setId(executarInsert(sql, "ID_PEND_VOTO_VIRTUAL", ps));
	}

	public void alterarIdPendenciaPeloVotante(String idPendencia, String idAudienciaProcesso, String idServentiaCargo)
			throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";

		stSql = "UPDATE PROJUDI.PEND_VOTO_VIRTUAL SET  ";
		stSql += " ID_PEND = ?";
		ps.adicionarString(idPendencia);

		stSql += " WHERE ID_PEND IN (";
		stSql += "	SELECT ID_PEND FROM PEND_FINAL_RESP WHERE ID_SERV_CARGO = ?";
		ps.adicionarLong(idServentiaCargo);
		stSql += ")";
		stSql += " AND ID_AUDI_PROC = ? ";
		ps.adicionarLong(idAudienciaProcesso);

		executarUpdateDelete(stSql, ps);

	}

	public String consultarIdPendenciaPeloVotante(String idAudienciaProcesso, String idServentiaCargo)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		// jvosantos - 04/03/2020 12:04 - Ordenar pelo maior ID_PEND_VOTO_VIRTUAL para trazer o último voto
		// jvosantos - 04/03/2020 12:04 - Reformatar
		String sql = "SELECT ID_PEND FROM PROJUDI.PEND_VOTO_VIRTUAL " + 
				"WHERE ID_PEND IN ( SELECT ID_PEND FROM PEND_FINAL_RESP WHERE ID_SERV_CARGO = ? ) AND ID_AUDI_PROC =  ? " +
				"ORDER BY ID_PEND_VOTO_VIRTUAL DESC";

		ps.adicionarLong(idServentiaCargo);
		ps.adicionarLong(idAudienciaProcesso);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getString("ID_PEND");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return null;
	}

	public boolean isVotacaoConcluida(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "WITH VOTANTES AS (" + "	SELECT * FROM AUDI_PROC_VOTANTES AV"
				+ "	JOIN VOTANTE_TIPO VT ON AV.ID_VOTANTE_TIPO = VT.ID_VOTANTE_TIPO"
				+ "	WHERE ID_AUDI_PROC = ? "
				+ "	AND VT.VOTANTE_TIPO_CODIGO = ?"
				+ ") ";
		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(VotanteTipoDt.VOTANTE);
		sql += " SELECT 1 FROM DUAL"
				+ " WHERE "
				+ "	("
				+ "	SELECT"
				+ "		COUNT(*)" + "	FROM"
				+ "		VIEW_PEND_VOTO_VIRTUAL PV" + "	WHERE ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		sql += "	AND PV.ID_SERV_CARGO IN (SELECT ID_SERV_CARGO FROM VOTANTES ) "
				+ "	AND PV.VOTO_VIRTUAL_TIPO_CODIGO NOT IN (?) "
				+ ") = ("
				+ "SELECT COUNT(*) FROM VOTANTES" + ")";
		ps.adicionarLong(VotoTipoDt.OBSERVACAO);
		sql += " AND NOT EXISTS ("
				+ "	SELECT 1 FROM PEND P "
				+ "	WHERE P.ID_PROC = (SELECT ID_PROC FROM AUDI_PROC AP WHERE AP.ID_AUDI_PROC = ?)"
				+ "	AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)" + ")";
		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
		ResultSetTJGO rs = null;
		try {
			rs = consultar(sql, ps);
			return rs.next();
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	public boolean isVotacaoNaoIniciada(String idProcesso, String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = new Select("COUNT(*) AS Q", ps)
				.from("PROJUDI.view_PEND")
				.whereEqual("ID_PROC", idProcesso)
				.and()
				.in("PEND_TIPO_CODIGO", PendenciaTipoDt.VOTO_SESSAO, PendenciaTipoDt.PROCLAMACAO_VOTO)
				.build();
		ResultSetTJGO rs = null;
		try {
			rs = consultar(sql, ps);
			if (rs.next()) {
				boolean temPendencia = rs.getInt("Q") > 0;
				if (!temPendencia) {
					rs.close();
					ps = new PreparedStatementTJGO();
					sql = new Select("COUNT(*) AS Q", ps)
							.from("PROJUDI.VIEW_PEND_VOTO_VIRTUAL")
							.whereEqual("ID_AUDI_PROC", idAudienciaProcesso)
							.build();
					rs = consultar(sql, ps);
					if (rs.next()) {
						return rs.getInt("Q") == 0;
					}
				}
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return false;
	}

	public void adicionarLong(List<String> colunas, PreparedStatementTJGO ps, String nomeColuna, String dado)
			throws Exception {
		if (dado != null && !dado.isEmpty()) {
			colunas.add(nomeColuna);
			ps.adicionarLong(dado);
		}
	}

	public void adicionarBoolean(List<String> colunas, PreparedStatementTJGO ps, String nomeColuna, boolean dado)
			throws Exception {
		colunas.add(nomeColuna);
		ps.adicionarBoolean(dado);
	}

	public String getSqlValues(int tamanho) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < tamanho; i++) {
			builder.append("?,");
		}
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

	public void excluir(String id) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "DELETE FROM PROJUDI.PEND_VOTO_VIRTUAL PV WHERE ID_PEND_VOTO_VIRTUAL = ?";
		ps.adicionarLong(id);
		executarUpdateDelete(sql, ps);

	}

	// jvosantos - 25/09/2019 13:22 - M\E9todo para excluir um votante pelo ID_AUDI_PROC_VOTANTE
	public void excluirVotante(String idAudiProcVotante) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "DELETE FROM PROJUDI.AUDI_PROC_VOTANTES APV WHERE APV.ID_AUDI_PROC_VOTANTES = ?";
		ps.adicionarLong(idAudiProcVotante);
		executarUpdateDelete(sql, ps);
	}

	public void excluirIdPendencia(String id) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "DELETE FROM PROJUDI.PEND_VOTO_VIRTUAL PV WHERE ID_PEND = ?";
		ps.adicionarLong(id);
		executarUpdateDelete(sql, ps);

	}

	public VotoDt consultarIdPendencia(String id) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT * FROM PROJUDI.PEND_VOTO_VIRTUAL PV"
				+ " JOIN VOTO_VIRTUAL_TIPO VT ON PV.ID_VOTO_VIRTUAL_TIPO = VT.ID_VOTO_VIRTUAL_TIPO"
				+ " WHERE ID_PEND = ?";
		ps.adicionarLong(id);
		ResultSetTJGO rs = null;
		try {
			rs = consultar(sql, ps);
			if (rs.next()) {
				return setDadosVoto(rs);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return null;
	}

	public List<VotoSessaoLocalizarDt> consultarIdPendenciaMultiplo(String[] ids) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		String sql = "SELECT DISTINCT AC.ID_PROC, AC.DATA_AGENDADA, AC.PROC_NUMERO, PV.ID_PEND, PT.PROC_TIPO ";
		sql += ", (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ";
        sql += "	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "; 
        sql += "	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ";
        sql += " 	WHERE AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "; 
        sql += " 	(SELECT 1 FROM RECURSO R "; 
        sql += "		INNER JOIN AUDI_PROC AP2 ON AP2.ID_PROC = R.ID_PROC "; 
        sql += "		WHERE AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO "
				+ " FROM PROJUDI.PEND_VOTO_VIRTUAL PV" + " JOIN PROJUDI.PEND P ON PV.ID_PEND = P.ID_PEND"
				+ " JOIN PROJUDI.PEND_RESP PR ON PV.ID_PEND = PR.ID_PEND"
				+ " JOIN PROJUDI.VIEW_AUDI_PROC_COMPLETA AC ON PV.ID_AUDI_PROC = AC.ID_AUDI_PROC"
				+ " INNER JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = AC.ID_AUDI_PROC "
				+ " INNER JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO";
		sql += " WHERE PR.ID_PEND IN (" + getSqlValues(ids.length) + ")";
		for (String id : ids) {
			ps.adicionarLong(id);
		}
		sql += " AND PR.ID_SERV_CARGO IS NOT NULL";
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				ProcessoDt processoDt = new ProcessoDt();
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				
				 //lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sess\E3o possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				voto.setProcessoDt(processoDt);
				if(StringUtils.isEmpty(voto.getIdAudienciaProcesso()))
					voto.setExisteAudiProc(false);
				
				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	public List<String> consultarVotantesSessao(String idAudienciaProcesso) throws Exception {
		return consultarVotantesSessaoCompleto(idAudienciaProcesso).stream()
				.map(votante -> votante.getIdServentiaCargo())
				.collect(Collectors.toList());
	}

	public List<VotanteDt> consultarVotantesSessaoCompleto(String idAudienciaProcesso)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		// jvosantos - 25/09/2019 10:45 - Adicionar ID do votante
		String sql = new Select("DISTINCT PV.ID_AUDI_PROC_VOTANTES, PV.ID_SERV_CARGO, SC.NOME_USU", ps)
				.from(TABELA_VOTANTES, "PV")
				.join(TABELA_VOTANTE_TIPO, "VT", "VT.ID_VOTANTE_TIPO = PV.ID_VOTANTE_TIPO")
				.join(VIEW_AUDIENCIA_PROCESSO_COMPLETA, "APC", "APC.ID_AUDI_PROC = PV.ID_AUDI_PROC")
				.join("PROJUDI.VIEW_SERV_CARGO", "SC", "SC.ID_SERV_CARGO = PV.ID_SERV_CARGO")
				.whereEqual("PV.ID_AUDI_PROC", idAudienciaProcesso)
				.andEqual("VT.VOTANTE_TIPO_CODIGO", VotanteTipoDt.VOTANTE)
//				.and()
//				.in("IT.IMPEDIMENTO_TIPO_CODIGO", (Object[]) tiposImpedimento)
				.andEqual("APC.AUDI_PROC_STATUS_CODIGO", AudienciaProcessoStatusDt.A_SER_REALIZADA)
				.build();
		try {
			List<VotanteDt> list = new ArrayList<>();
			rs = consultar(sql, ps);
			while (rs.next()) {
				VotanteDt votanteDt = new VotanteDt();
				votanteDt.setId(rs.getString("ID_AUDI_PROC_VOTANTES")); // jvosantos - 25/09/2019 10:45 - Adicionar ID do votante
				votanteDt.setIdServentiaCargo(rs.getString("ID_SERV_CARGO"));
				votanteDt.setNome(rs.getString("NOME_USU"));
				list.add(votanteDt);
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	public List<VotanteDt> consultarTodosVotantesSessaoCompletoDeVerdade(String idAudienciaProcesso)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		// jvosantos - 25/09/2019 10:45 - Adicionar ID do votante
		String sql = new Select("DISTINCT PV.ID_AUDI_PROC_VOTANTES, PV.ID_SERV_CARGO, SC.NOME_USU, VT.VOTANTE_TIPO_CODIGO, PV.ID_IMPEDIMENTO_TIPO, IT.IMPEDIMENTO_TIPO_CODIGO", ps)
				.from(TABELA_VOTANTES, "PV")
				.join(TABELA_VOTANTE_TIPO, "VT", "VT.ID_VOTANTE_TIPO = PV.ID_VOTANTE_TIPO")
				.join(VIEW_AUDIENCIA_PROCESSO_COMPLETA, "APC", "APC.ID_AUDI_PROC = PV.ID_AUDI_PROC")
				.join("PROJUDI.VIEW_SERV_CARGO", "SC", "SC.ID_SERV_CARGO = PV.ID_SERV_CARGO")
				.join("PROJUDI.IMPEDIMENTO_TIPO", "IT", "PV.ID_IMPEDIMENTO_TIPO  = IT.ID_IMPEDIMENTO_TIPO ")
				.whereEqual("PV.ID_AUDI_PROC", idAudienciaProcesso)
				.andEqual("APC.AUDI_PROC_STATUS_CODIGO", AudienciaProcessoStatusDt.A_SER_REALIZADA)
				.build();
		try {
			List<VotanteDt> list = new ArrayList<>();
			rs = consultar(sql, ps);
			while (rs.next()) {
				VotanteDt votanteDt = new VotanteDt();
				votanteDt.setId(rs.getString("ID_AUDI_PROC_VOTANTES")); // jvosantos - 25/09/2019 10:45 - Adicionar ID do votante
				votanteDt.setIdServentiaCargo(rs.getString("ID_SERV_CARGO"));
				votanteDt.setNome(rs.getString("NOME_USU"));
				
				VotanteTipoDt votanteTipoDt = new VotanteTipoDt();
				votanteTipoDt.setVotanteTipoCodigo(rs.getString("VOTANTE_TIPO_CODIGO"));
				
				ImpedimentoTipoDt impedimentoTipoDt = new ImpedimentoTipoDt();
				impedimentoTipoDt.setImpedimentoTipoCodigo(rs.getString("IMPEDIMENTO_TIPO_CODIGO"));
				impedimentoTipoDt.setId(rs.getString("ID_IMPEDIMENTO_TIPO"));
				
				votanteDt.setVotanteTipoDt(votanteTipoDt);
			    votanteDt.setImpedimentoTipoDt(impedimentoTipoDt);
				
				list.add(votanteDt);
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	public List<String> consultarIdIntegrantesSessaoPorTipo(String idAudienciaProcesso, List<Integer> tipos)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		String sql = "SELECT ID_SERV_CARGO FROM PROJUDI.AUDI_PROC_VOTANTES PV"
				+ " JOIN PROJUDI.VOTANTE_TIPO VT ON VT.ID_VOTANTE_TIPO = PV.ID_VOTANTE_TIPO "
				+ " WHERE PV.ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		if (tipos != null) {
			sql += "AND VT.VOTANTE_TIPO_CODIGO IN (" + getSqlValues(tipos.size()) + ")";
			for (Integer tipo : tipos) {
				ps.adicionarLong(tipo);
			}
		}
		try {
			List<String> list = new ArrayList<>();
			rs = consultar(sql, ps);
			while (rs.next()) {
				list.add(rs.getString("ID_SERV_CARGO"));
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	public List<VotanteDt> consultarIntegrantesSessaoPorTipo(String idAudienciaProcesso, List<Integer> tipos)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		String sql = "SELECT * FROM PROJUDI.AUDI_PROC_VOTANTES PV"
				+ " JOIN PROJUDI.VOTANTE_TIPO VT ON VT.ID_VOTANTE_TIPO = PV.ID_VOTANTE_TIPO "
				+ " JOIN PROJUDI.IMPEDIMENTO_TIPO IT ON IT.ID_IMPEDIMENTO_TIPO = PV.ID_IMPEDIMENTO_TIPO"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO VT ON VT.ID_SERV_CARGO = PV.ID_SERV_CARGO "
				+ " WHERE PV.ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		if (tipos != null) {
			sql += "AND VT.VOTANTE_TIPO_CODIGO IN (" + getSqlValues(tipos.size()) + ")";
			for (Integer tipo : tipos) {
				ps.adicionarLong(tipo);
			}
		}
		try {
			List<VotanteDt> list = new ArrayList<>();
			rs = consultar(sql, ps);
			while (rs.next()) {
				VotanteDt votante = new VotanteDt();
				votante.setIdAudienciaProcesso(rs.getString("ID_AUDI_PROC"));
				votante.setIdServentiaCargo(rs.getString("ID_SERV_CARGO"));
				votante.setNome(rs.getString("NOME_USU"));
				votante.setOrdem(rs.getInt("ORDEM_VOTANTE"));
				VotanteTipoDt votanteTipo = new VotanteTipoDt();
				votanteTipo.setId(rs.getString("ID_VOTANTE_TIPO"));
				votanteTipo.setVotanteTipoCodigo(rs.getString("VOTANTE_TIPO_CODIGO"));
				votanteTipo.setVotanteTipo(rs.getString("VOTANTE_TIPO"));
				votante.setVotanteTipoDt(votanteTipo);
				ImpedimentoTipoDt impedimentoTipo = new ImpedimentoTipoDt();
				impedimentoTipo.setId(rs.getString("ID_IMPEDIMENTO_TIPO"));
				impedimentoTipo.setImpedimentoTipo(rs.getString("IMPEDIMENTO_TIPO"));
				impedimentoTipo.setImpedimentoTipoCodigo(rs.getString("IMPEDIMENTO_TIPO_CODIGO"));
				votante.setImpedimentoTipoDt(impedimentoTipo);
				list.add(votante);
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	public String consultarNomeVotante(String idAudienciaProcesso, Integer votanteTipo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;

		String sql = "SELECT * FROM PROJUDI.AUDI_PROC_VOTANTES PV"
				+ " JOIN PROJUDI.VOTANTE_TIPO VT ON VT.ID_VOTANTE_TIPO = PV.ID_VOTANTE_TIPO "
				+ " JOIN PROJUDI.IMPEDIMENTO_TIPO IT ON IT.ID_IMPEDIMENTO_TIPO = PV.ID_IMPEDIMENTO_TIPO"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO VT ON VT.ID_SERV_CARGO = PV.ID_SERV_CARGO "
				+ " WHERE PV.ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		sql += "AND VT.VOTANTE_TIPO_CODIGO = ? ";
		ps.adicionarLong(votanteTipo);

		try {
			rs = this.consultar(sql, ps);

			if (rs.next()) {
				return rs.getString("NOME_USU");
			}
		} finally {
			if (rs != null)
				rs.close();
		}

		return "";

	}
	
	public List consultarTomarConhecimento(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		String sql = "SELECT DISTINCT pv.ID_SERV_CARGO FROM PROJUDI.AUDI_PROC_VOTANTES PV"
				+ " JOIN PROJUDI.VOTANTE_TIPO VT ON VT.ID_VOTANTE_TIPO = PV.ID_VOTANTE_TIPO "
				+ " JOIN PROJUDI.VIEW_AUDI_PROC_COMPLETA APC ON APC.ID_AUDI_PROC = PV.ID_AUDI_PROC "
				+ " WHERE PV.ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		sql += " AND VT.VOTANTE_TIPO_CODIGO NOT IN ( ? , ?, ? ) ";
		ps.adicionarLong(VotanteTipoDt.VOTANTE);
		ps.adicionarLong(VotanteTipoDt.RELATOR);
		ps.adicionarLong(VotanteTipoDt.MINISTERIO_PUBLICO);
		sql += " AND APC.AUDI_PROC_STATUS_CODIGO = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		try {
			List list = new ArrayList<>();
			rs = consultar(sql, ps);
			while (rs.next()) {
				list.add(rs.getString("ID_SERV_CARGO"));
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}
	}
	
	public String consultarVerificarImpedimento(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		String sql = "SELECT DISTINCT pv.ID_SERV_CARGO FROM PROJUDI.AUDI_PROC_VOTANTES PV"
				+ " JOIN PROJUDI.VOTANTE_TIPO VT ON VT.ID_VOTANTE_TIPO = PV.ID_VOTANTE_TIPO "
				+ " JOIN PROJUDI.VIEW_AUDI_PROC_COMPLETA APC ON APC.ID_AUDI_PROC = PV.ID_AUDI_PROC "
				+ " WHERE PV.ID_AUDI_PROC = ?"; ps.adicionarLong(idAudienciaProcesso);
		sql += "AND VT.VOTANTE_TIPO_CODIGO = ? "; ps.adicionarLong(VotanteTipoDt.MINISTERIO_PUBLICO);
		sql += " AND APC.AUDI_PROC_STATUS_CODIGO = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		try {
			String idServCargo = new String();
			rs = consultar(sql, ps);
			if (rs.next()) {
				idServCargo = rs.getString("ID_SERV_CARGO");
			}
			return idServCargo;
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	public List<VotoDt> consultarVotosSessao(String idAudienciaProcesso) throws Exception {
		List<VotoDt> list = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT PV.ID_PEND_VOTO_VIRTUAL, PV.ID_PEND, PV.ID_VOTO_VIRTUAL_TIPO, VT.VOTO_VIRTUAL_TIPO, VT.VOTO_VIRTUAL_TIPO_CODIGO, SC.NOME_USU, SC.ID_SERV_CARGO, AV.ORDEM_VOTANTE, VOT_T.VOTANTE_TIPO_CODIGO, PV.VOTO_ATIVO "
				+ " FROM PROJUDI.PEND_VOTO_VIRTUAL PV "
				+ " JOIN PROJUDI.VOTO_VIRTUAL_TIPO VT ON PV.ID_VOTO_VIRTUAL_TIPO = VT.ID_VOTO_VIRTUAL_TIPO"
				+ " JOIN PROJUDI.PEND_FINAL_RESP PR ON PR.ID_PEND = PV.ID_PEND"
				+ " JOIN PROJUDI.AUDI_PROC_VOTANTES AV ON (AV.ID_SERV_CARGO = PR.ID_SERV_CARGO AND AV.ID_AUDI_PROC = PV.ID_AUDI_PROC)"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO SC ON AV.ID_SERV_CARGO = SC.ID_SERV_CARGO"
				+ " JOIN PROJUDI.VOTANTE_TIPO VOT_T ON VOT_T.ID_VOTANTE_TIPO = AV.ID_VOTANTE_TIPO"
				+ " WHERE PV.VOTO_ATIVO <> 0 "
				+ " AND VOT_T.VOTANTE_TIPO_CODIGO = ? "
				+ " AND PV.ID_AUDI_PROC = ?";
		ps.adicionarLong(VotanteTipoDt.VOTANTE);
		ps.adicionarLong(idAudienciaProcesso);
		sql += " ORDER BY AV.ORDEM_VOTANTE";
		ResultSetTJGO rs = null;
		try {
			rs = consultar(sql, ps);
			while (rs.next()) {
				VotoDt voto = setDadosVoto(rs);
				list.add(voto);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return list;
	}

	// jvosantos - 24/09/2019 13:30 - M\E9todo que busca todos os votos de uma audi_proc
	public List<VotoDt> consultarTodosVotosSessao(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT PV.ID_PEND_VOTO_VIRTUAL, PV.ID_PEND, PV.ID_VOTO_VIRTUAL_TIPO, VT.VOTO_VIRTUAL_TIPO, VT.VOTO_VIRTUAL_TIPO_CODIGO, SC.NOME_USU, SC.ID_SERV_CARGO, AV.ORDEM_VOTANTE "
				+ " FROM PROJUDI.PEND_VOTO_VIRTUAL PV "
				+ " JOIN PROJUDI.VOTO_VIRTUAL_TIPO VT ON PV.ID_VOTO_VIRTUAL_TIPO = VT.ID_VOTO_VIRTUAL_TIPO"
				+ " JOIN ((SELECT P.* FROM PEND_RESP P) UNION (SELECT PF.* FROM PEND_FINAL_RESP PF)) PR ON PR.ID_PEND = PV.ID_PEND"
				+ " JOIN PROJUDI.AUDI_PROC_VOTANTES AV ON (AV.ID_SERV_CARGO = PR.ID_SERV_CARGO AND AV.ID_AUDI_PROC = PV.ID_AUDI_PROC)"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO SC ON AV.ID_SERV_CARGO = SC.ID_SERV_CARGO"
				+ " WHERE PV.VOTO_ATIVO <> 0 AND " // jvosantos - 06/01/2020 15:58 - Filtrar votos não ativos
				+ " PV.ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		sql += " ORDER BY AV.ORDEM_VOTANTE";
		ResultSetTJGO rs = null;
		try {
			List<VotoDt> list = new ArrayList<>(); //jvosantos - 04/03/2020 11:09 - Tipar lista
			rs = consultar(sql, ps);
			while (rs.next()) {
				VotoDt voto = setDadosVoto(rs);
				list.add(voto);
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}
	}
	
	public List<VotoDt> consultarTodosVotosSessaoAtivosInativos(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT PV.ID_PEND_VOTO_VIRTUAL, PV.ID_PEND, PV.ID_VOTO_VIRTUAL_TIPO, VT.VOTO_VIRTUAL_TIPO, VT.VOTO_VIRTUAL_TIPO_CODIGO, SC.NOME_USU, SC.ID_SERV_CARGO, AV.ORDEM_VOTANTE "
				+ " FROM PROJUDI.PEND_VOTO_VIRTUAL PV "
				+ " JOIN PROJUDI.VOTO_VIRTUAL_TIPO VT ON PV.ID_VOTO_VIRTUAL_TIPO = VT.ID_VOTO_VIRTUAL_TIPO"
				+ " JOIN ((SELECT P.* FROM PEND_RESP P) UNION (SELECT PF.* FROM PEND_FINAL_RESP PF)) PR ON PR.ID_PEND = PV.ID_PEND"
				+ " JOIN PROJUDI.AUDI_PROC_VOTANTES AV ON (AV.ID_SERV_CARGO = PR.ID_SERV_CARGO AND AV.ID_AUDI_PROC = PV.ID_AUDI_PROC)"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO SC ON AV.ID_SERV_CARGO = SC.ID_SERV_CARGO"
				+ " WHERE PV.ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		sql += " ORDER BY AV.ORDEM_VOTANTE";
		ResultSetTJGO rs = null;
		try {
			List<VotoDt> list = new ArrayList<>(); //jvosantos - 04/03/2020 11:09 - Tipar lista
			rs = consultar(sql, ps);
			while (rs.next()) {
				VotoDt voto = setDadosVoto(rs);
				list.add(voto);
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	// jvosantos - 08/07/2019 14:03 - M\E9todo para consultar os votos da sess\E3o com data de voto
	public List<VotoDt> consultarVotosSessaoComData(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT PV.ID_PEND_VOTO_VIRTUAL, PV.ID_PEND, PV.ID_VOTO_VIRTUAL_TIPO, VT.VOTO_VIRTUAL_TIPO, VT.VOTO_VIRTUAL_TIPO_CODIGO, SC.NOME_USU, SC.ID_SERV_CARGO, AV.ORDEM_VOTANTE, P.DATA_VISTO "
				+ " FROM PROJUDI.PEND_VOTO_VIRTUAL PV "
				+ " JOIN PROJUDI.VOTO_VIRTUAL_TIPO VT ON PV.ID_VOTO_VIRTUAL_TIPO = VT.ID_VOTO_VIRTUAL_TIPO"
				+ " JOIN PROJUDI.PEND_FINAL_RESP PR ON PR.ID_PEND = PV.ID_PEND"
				+ " JOIN PROJUDI.PEND_FINAL P ON P.ID_PEND = PV.ID_PEND"
				+ " JOIN PROJUDI.AUDI_PROC_VOTANTES AV ON (AV.ID_SERV_CARGO = PR.ID_SERV_CARGO AND AV.ID_AUDI_PROC = PV.ID_AUDI_PROC)"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO SC ON AV.ID_SERV_CARGO = SC.ID_SERV_CARGO"
				+ " WHERE PV.ID_AUDI_PROC = ? AND PV.VOTO_ATIVO <> 0 " // jvosantos - 06/01/2020 15:58 - Filtrar votos não ativos
				+ " AND AV.ID_VOTANTE_TIPO = (SELECT ID_VOTANTE_TIPO FROM VOTANTE_TIPO WHERE VOTANTE_TIPO_CODIGO = ?) ";
		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(VotanteTipoDt.VOTANTE);
		sql += " ORDER BY AV.ORDEM_VOTANTE";
		ResultSetTJGO rs = null;
		try {
			List<VotoDt> list = new ArrayList<>(); //jvosantos - 04/03/2020 11:09 - Tipar lista
			rs = consultar(sql, ps);
			while (rs.next()) {
				VotoDt voto = setDadosVoto(rs);
				list.add(voto);
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	public List<VotoDt> consultarVotosIncluindoVazio(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder().append("SELECT PV.ID_PEND_VOTO_VIRTUAL, PV.ID_PEND, PV.ID_VOTO_VIRTUAL_TIPO, PV.VOTO_VIRTUAL_TIPO, PV.VOTO_VIRTUAL_TIPO_CODIGO, SC.NOME_USU, SC.ID_SERV_CARGO, AV.ORDEM_VOTANTE "
				+ " FROM PROJUDI.AUDI_PROC_VOTANTES AV "
				+ " JOIN PROJUDI.VIEW_SERV_CARGO SC ON AV.ID_SERV_CARGO = SC.ID_SERV_CARGO"
				+ " LEFT JOIN PROJUDI.VIEW_PEND_VOTO_VIRTUAL PV ON (AV.ID_AUDI_PROC = PV.ID_AUDI_PROC AND AV.ID_SERV_CARGO = PV.ID_SERV_CARGO AND PV.VOTO_ATIVO = 1)"
				+ " LEFT JOIN PROJUDI.VOTO_VIRTUAL_TIPO VT ON VT.ID_VOTO_VIRTUAL_TIPO = PV.ID_VOTO_VIRTUAL_TIPO "
				+ " WHERE AV.ID_AUDI_PROC = ?");
		ps.adicionarLong(idAudienciaProcesso);
		sql.append(" AND AV.ID_VOTANTE_TIPO = (SELECT ID_VOTANTE_TIPO FROM VOTANTE_TIPO WHERE VOTANTE_TIPO_CODIGO = ?)");
		ps.adicionarLong(2);
		sql.append(" AND AV.ID_IMPEDIMENTO_TIPO IN (SELECT ID_IMPEDIMENTO_TIPO FROM IMPEDIMENTO_TIPO WHERE IMPEDIMENTO_TIPO_CODIGO IN (?, ?))");
		ps.adicionarLong(ImpedimentoTipoDt.NAO_IMPEDIDO);
		ps.adicionarLong(ImpedimentoTipoDt.AGUARDANDO_VERIFICACAO_VOTANTE);
		sql.append(" ORDER BY AV.ORDEM_VOTANTE");
		ResultSetTJGO rs = null;
		try {
			List<VotoDt> list = new ArrayList<>(); //jvosantos - 04/03/2020 11:09 - Tipar lista
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				VotoDt voto = setDadosVoto(rs);
				list.add(voto);
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	public List<VotoDt> consultarObservacoes(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		//lrcampos 18/02/2020 15:19 - Busca somente as observações de desembargadores que não são relatores/votantes.
		String sql = "SELECT * FROM PROJUDI.PEND_VOTO_VIRTUAL PV" + " JOIN PROJUDI.PEND P ON P.ID_PEND = PV.ID_PEND"
				+ " JOIN PROJUDI.PEND_RESP PR ON PR.ID_PEND = PV.ID_PEND"
				+ " JOIN PROJUDI.VOTO_VIRTUAL_TIPO VT ON VT.ID_VOTO_VIRTUAL_TIPO = PV.ID_VOTO_VIRTUAL_TIPO "
				+ " JOIN PROJUDI.AUDI_PROC_VOTANTES AV ON (PV.ID_AUDI_PROC = AV.ID_AUDI_PROC AND PR.ID_SERV_CARGO = AV.ID_SERV_CARGO)"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO SC ON AV.ID_SERV_CARGO = SC.ID_SERV_CARGO"
				+ " JOIN PROJUDI.VOTO_VIRTUAL_TIPO VT ON VT.ID_VOTO_VIRTUAL_TIPO = PV.ID_VOTO_VIRTUAL_TIPO "
				+ " JOIN PROJUDI.VOTANTE_TIPO VOT_TIPO ON VOT_TIPO.ID_VOTANTE_TIPO = AV.ID_VOTANTE_TIPO";
		sql += " WHERE VT.VOTO_VIRTUAL_TIPO_CODIGO = ?";
		ps.adicionarLong(VotoTipoDt.OBSERVACAO);
		sql += " AND VOT_TIPO.VOTANTE_TIPO_CODIGO NOT IN (?, ?) "; 
		ps.adicionarLong(VotanteTipoDt.RELATOR);
		ps.adicionarLong(VotanteTipoDt.VOTANTE);
		sql += " AND PV.ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		sql += " AND NOT EXISTS (SELECT 1 FROM VIEW_PEND_FINAL PF WHERE PF.ID_PEND = PV.ID_PEND AND PF.PEND_STATUS_CODIGO IN (?))";
		ps.adicionarLong(PendenciaStatusDt.ID_NAO_CUMPRIDA);
		sql += " ORDER BY AV.ORDEM_VOTANTE";
		ResultSetTJGO rs = null;
		try {
			List<VotoDt> list = new ArrayList<>(); //jvosantos - 04/03/2020 11:09 - Tipar lista
			rs = consultar(sql, ps);
			while (rs.next()) {
				VotoDt voto = setDadosVoto(rs);
				list.add(voto);
			}
			return list;
		} finally {
			if (rs != null)
				rs.close();
		}

	}

	private VotoDt setDadosVoto(ResultSetTJGO rs) throws Exception {
		VotoDt voto = new VotoDt();
		voto.setPendenciaDt(new PendenciaDt());
		voto.getPendenciaDt().setId(getString(rs, "ID_PEND"));
		voto.setId(getString(rs, "ID_PEND_VOTO_VIRTUAL"));
		voto.setIdVotoTipo(getString(rs, "ID_VOTO_VIRTUAL_TIPO"));
		voto.setVotoTipo(getString(rs, "VOTO_VIRTUAL_TIPO"));
		voto.setVotoTipoCodigo(getString(rs, "VOTO_VIRTUAL_TIPO_CODIGO"));
		voto.setNomeVotante(getString(rs, "NOME_USU"));
		voto.setIdServentiaCargo(getString(rs, "ID_SERV_CARGO"));
		voto.setOrdemVotante(getString(rs, "ORDEM_VOTANTE"));
		
		// jvosantos - 08/07/2019 14:03 - Adicionar a busca do campo de data
		try {
			voto.setDataVoto(rs.getDateTime("DATA_VISTO"));
		}catch(Exception e) {}
		
		try {
			voto.setVotanteTipoCodigo(getString(rs, "VOTANTE_TIPO_CODIGO"));
		} catch (Exception e) {	}

		try {
			voto.setAtivo(StringUtils.equals(getString(rs, "VOTO_ATIVO"), "1"));
		} catch (Exception e) {	}	

		return voto;
	}

	private String getString(ResultSetTJGO rs, String campo) {
		try {
			return StringUtils.defaultIfEmpty(rs.getString(campo), "");
		} catch (Exception e) {
		}
		return "";
	}

	public String consultarDescricaoVotoTipo(String descricao, String posicao, List<Integer> codigoIgnorar)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String retorno = "";

		String sql = " FROM PROJUDI.VOTO_VIRTUAL_TIPO WHERE 1=1";
		if (descricao != null && !descricao.isEmpty()) {
			sql += " AND VOTO_VIRTUAL_TIPO LIKE ?";
			ps.adicionarString("%" + descricao + "%");
		}
		if (CollectionUtils.isNotEmpty(codigoIgnorar)) {
			sql += " AND VOTO_VIRTUAL_TIPO_CODIGO NOT IN (";
			sql += getSqlValues(codigoIgnorar.size());
			sql += ")";

			for (int codigo : codigoIgnorar) {
				ps.adicionarLong(codigo);
			}
		}

		ResultSetTJGO rs1 = null, rs2 = null;
		try {
			rs1 = this
					.consultarPaginacao(
							"SELECT ID_VOTO_VIRTUAL_TIPO AS ID, VOTO_VIRTUAL_TIPO AS DESCRICAO1, VOTO_VIRTUAL_TIPO_CODIGO AS DESCRICAO2"
									+ sql,
							ps,
							posicao);
			rs2 = this.consultar("SELECT COUNT(*) AS QUANTIDADE " + sql, ps);
			rs2.next();
			retorno = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, 2);
		} finally {
			if (rs1 != null)
				rs1.close();
		}

		return retorno;
	}

	public String consultarOrdemVotante(String idServentiaCargo, String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ORDEM_VOTANTE FROM PROJUDI.AUDI_PROC_VOTANTES";
		sql += " WHERE ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		sql += " AND ID_SERV_CARGO = ?";
		ps.adicionarLong(idServentiaCargo);
		sql += " AND ID_VOTANTE_TIPO = ( SELECT	ID_VOTANTE_TIPO	FROM VOTANTE_TIPO WHERE	VOTANTE_TIPO_CODIGO = ? )"; ps.adicionarLong(VotanteTipoDt.VOTANTE);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getString("ORDEM_VOTANTE");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}

		return "";
	}

	public List consultarVotosPreAnalisados(String idServentiaCargo, String processoNumero, int tipoPendencia, UsuarioNe usuario, FabricaConexao fabrica)
			throws Exception {
		return consultarVotosPreAnalisados(
				idServentiaCargo,
				processoNumero,
				PendenciaStatusDt.ID_EM_ANDAMENTO,
				tipoPendencia, usuario, fabrica);
	}

	public List consultarVotosAguardandoAssinatura(String idServentiaCargo, String processoNumero, int tipoPendencia, UsuarioNe usuario, FabricaConexao fabrica)
			throws Exception {
		return consultarVotosPreAnalisados(
				idServentiaCargo,
				processoNumero,
				PendenciaStatusDt.ID_PRE_ANALISADA,
				tipoPendencia, usuario, fabrica);
	}

	private List<VotoSessaoLocalizarDt> consultarVotosPreAnalisados(String idServentiaCargo,
			String processoNumero,
			int statusPendencia,
			int tipoPendencia,
			UsuarioNe usuarioSessao,
			FabricaConexao fabrica
			) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT DISTINCT AC.ID_PROC, AC.DATA_AGENDADA, AC.PROC_NUMERO_COMPLETO, P.ID_PEND, AC.ID_AUDI_PROC, AC.NOME, "); // jvosantos - 20/08/2019 15:05 - Usar StringBuilder e adicionar INNER JOIN com AUDI_PROC_PEND
		sql.append(" c.ID_CLASSIFICADOR ID_CLASSIFICADOR_PEND, c.CLASSIFICADOR CLASSIFICADOR_PEND, c1.ID_CLASSIFICADOR ID_CLASSIFICADOR_PROC, c1.CLASSIFICADOR CLASSIFICADOR_PROC, PT.PROC_TIPO, ");
		sql.append(" (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ");
		sql.append("	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "); 
		sql.append("	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ");
		sql.append(" 	WHERE AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "); 
		sql.append(" 	(SELECT 1 FROM RECURSO R "); 
		sql.append("		INNER JOIN AUDI_PROC AP2 ON AP2.ID_PROC = R.ID_PROC "); 
		sql.append("		WHERE AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO ");
		sql.append(" FROM PROJUDI.PEND P ");
		sql.append(" INNER JOIN PROC PROC ON PROC.ID_PROC = P.ID_PROC ");
		sql.append(" left JOIN CLASSIFICADOR C ON C.ID_CLASSIFICADOR = P.ID_CLASSIFICADOR");
		sql.append(" left JOIN CLASSIFICADOR C1 on c1.id_classificador = proc.id_classificador");
		sql.append(" JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ");
		sql.append(" INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND ");
		sql.append(" JOIN PROJUDI.VIEW_AUDI_COMPLETA AC ON APP.ID_AUDI_PROC = AC.ID_AUDI_PROC ");
		sql.append(" INNER JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = AC.ID_AUDI_PROC");
		sql.append(" INNER JOIN PROC PROC ON PROC.ID_PROC = AP.ID_PROC");
		sql.append(" JOIN PROJUDI.PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" WHERE PR.ID_SERV_CARGO = ? ");
		ps.adicionarLong(idServentiaCargo);
		sql.append(" AND ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)");
		ps.adicionarLong(statusPendencia);
		sql.append(" AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)");
		ps.adicionarLong(tipoPendencia);
		if(tipoPendencia == PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL) {
			sql.append(" AND P.CODIGO_TEMP = ? ");
			ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP);
		}

		filtroProcessoNumero(sql, processoNumero, ps);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);

			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				voto.setHashUsuario(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				voto.setPendenciaDt(new PendenciaDt());
				voto.setProcessoDt(new ProcessoDt());
				voto.getPendenciaDt().setId(rs1.getString("ID_PEND"));
				voto.getPendenciaDt().setId_Classificador(rs1.getString("ID_CLASSIFICADOR_PEND"));
				voto.getPendenciaDt().setClassificador(rs1.getString("CLASSIFICADOR_PEND"));
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME"));
				voto.getProcessoDt().setId(rs1.getString("ID_PROC"));
				voto.getProcessoDt().setId_Classificador(rs1.getString("ID_CLASSIFICADOR_PROC"));
				voto.getProcessoDt().setClassificador(rs1.getString("CLASSIFICADOR_PROC"));

				 //lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sess\E3o possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					voto.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					voto.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					voto.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}

				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}
	
	public List<VotoSessaoLocalizarDt> consultarVotosPreAnalisadosOtimizado(String idServentiaCargo,
			String processoNumero,
			int tipoPendencia,
			int statusPendencia,
			UsuarioNe usuarioSessao,
			FabricaConexao fabrica,
			String idServentiaFiltro
			) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" 	* ");
		sql.append(" FROM ");
		sql.append(" 	( ");
		sql.append(" 	SELECT ");
		sql.append(" 		DISTINCT AP.ID_PROC, ");
		sql.append(" 		A.ID_SERV, ");
		sql.append(" 		SS.SERV, ");
		sql.append(" 		A.DATA_AGENDADA, ");
		sql.append(" 		VP.PROC_NUMERO_COMPLETO, ");
		sql.append(" 		P.ID_PEND, ");
		sql.append(" 		AP.ID_AUDI_PROC, ");
		sql.append(" 		U.NOME, ");
		sql.append(" 		C.ID_CLASSIFICADOR ID_CLASSIFICADOR_PEND, ");
		sql.append(" 		C.CLASSIFICADOR CLASSIFICADOR_PEND, ");
		sql.append(" 		C1.ID_CLASSIFICADOR ID_CLASSIFICADOR_PROC, ");
		sql.append(" 		C1.CLASSIFICADOR CLASSIFICADOR_PROC, ");
		sql.append(" 		PT.PROC_TIPO, ");
		sql.append(" 		PA.ID_ARQ, ");
		sql.append(" 		( ");
		sql.append(" 		SELECT ");
		sql.append(" 			DISTINCT PT1.PROC_TIPO ");
		sql.append(" 		FROM ");
		sql.append(" 			AUDI_PROC AP1 ");
		sql.append(" 		INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON ");
		sql.append(" 			RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC ");
		sql.append(" 		INNER JOIN PROC_TIPO PT1 ON ");
		sql.append(" 			PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ");
		sql.append(" 		WHERE ");
		sql.append(" 			AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 			AND ROWNUM = 1) PROC_TIPO_REC_SEC , ");
		sql.append(" 		( ");
		sql.append(" 		SELECT ");
		sql.append(" 			1 ");
		sql.append(" 		FROM ");
		sql.append(" 			RECURSO R ");
		sql.append(" 		INNER JOIN AUDI_PROC AP2 ON ");
		sql.append(" 			AP2.ID_PROC = R.ID_PROC ");
		sql.append(" 		WHERE ");
		sql.append(" 			AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 			AND ROWNUM = 1) POSSUI_RECURSO ");
		sql.append(" 	FROM ");
		sql.append(" 		PROJUDI.PEND P ");
		sql.append(" 	INNER JOIN PROJUDI.VIEW_PEND VP ON ");
		sql.append(" 		VP.ID_PEND = P.ID_PEND ");
		sql.append(" 	INNER JOIN PROJUDI.PEND_VOTO_VIRTUAL PV ON ");
		sql.append(" 		PV.ID_PEND = P.ID_PEND ");
		sql.append(" 	INNER JOIN PROJUDI.PEND_ARQ PA ON ");
		sql.append(" 		PA.ID_PEND = PV.ID_PEND ");
		sql.append(" 	INNER JOIN PROC PROC ON ");
		sql.append(" 		PROC.ID_PROC = P.ID_PROC ");
		sql.append(" 	LEFT JOIN CLASSIFICADOR C ON ");
		sql.append(" 		C.ID_CLASSIFICADOR = P.ID_CLASSIFICADOR ");
		sql.append(" 	LEFT JOIN CLASSIFICADOR C1 ON ");
		sql.append(" 		C1.ID_CLASSIFICADOR = PROC.ID_CLASSIFICADOR ");
		sql.append(" 	JOIN PROJUDI.PEND_RESP PR ON ");
		sql.append(" 		P.ID_PEND = PR.ID_PEND ");
		sql.append(" 	INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON ");
		sql.append(" 		APP.ID_PEND = P.ID_PEND ");
		sql.append(" 	INNER JOIN AUDI_PROC AP ON ");
		sql.append(" 		AP.ID_AUDI_PROC = PV.ID_AUDI_PROC ");
		sql.append(" 	JOIN PROJUDI.PROC_TIPO PT ON ");
		sql.append(" 		PT.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" 	LEFT JOIN AUDI A ON ");
		sql.append(" 		A.ID_AUDI = AP.ID_AUDI ");
		sql.append(" 	LEFT JOIN SERV_CARGO SC	ON ");
		sql.append(" 		AP.ID_SERV_CARGO = SC.ID_SERV_CARGO ");
		sql.append("   	LEFT JOIN USU_SERV_GRUPO USG ON ");
		sql.append("   		SC.ID_USU_SERV_GRUPO = USG.ID_USU_SERV_GRUPO ");
		sql.append("   	LEFT JOIN USU_SERV US ON ");
		sql.append("   		USG.ID_USU_SERV = US.ID_USU_SERV ");
		sql.append("   	LEFT JOIN USU U ON ");
		sql.append("   		US.ID_USU = U.ID_USU	 ");
		sql.append(" 	JOIN PEND_STATUS STATUS ON ");
		sql.append(" 		STATUS.ID_PEND_STATUS = P.ID_PEND_STATUS ");
		sql.append(" 	JOIN PEND_TIPO TIP ON ");
		sql.append(" 		P.ID_PEND_TIPO = TIP.ID_PEND_TIPO ");
		sql.append(" 	INNER JOIN SERV SS ON ");
		sql.append(" 		SS.ID_SERV = A.ID_SERV ");
		sql.append(" 	WHERE ");
		sql.append(" 		PR.ID_SERV_CARGO = ? ");
		ps.adicionarLong(idServentiaCargo);
		sql.append(" 		AND STATUS.PEND_STATUS_CODIGO = ? ");
		ps.adicionarLong(statusPendencia);
		if(StringUtils.isNotEmpty(idServentiaFiltro)) {
			sql.append(" 		AND A.ID_SERV = ? ");
			ps.adicionarLong(idServentiaFiltro);
		}
		sql.append(" 		AND TIP.PEND_TIPO_CODIGO = ? ) MYVIEW ");
		ps.adicionarLong(tipoPendencia);
		sql.append(" WHERE ");
		sql.append(" 	( ");
		sql.append(" 	SELECT ");
		sql.append(" 		COUNT(PA1.ID_ARQ) ");
		sql.append(" 	FROM ");
		sql.append(" 		PROJUDI.PEND_ARQ PA1 ");  
		sql.append(" 	WHERE ");  
		sql.append(" 		PA1.ID_ARQ = MYVIEW.ID_ARQ ");  
		sql.append(" 	GROUP BY ");  
		sql.append(" 		PA1.ID_ARQ) = 1 ");  
		if (StringUtils.isNotEmpty(processoNumero)) {
			sql.append(" AND P.ID_PROC = ?");
			ps.adicionarLong(processoNumero);
		}
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				voto.setHashUsuario(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				voto.setPendenciaDt(new PendenciaDt());
				voto.setProcessoDt(new ProcessoDt());
				voto.getPendenciaDt().setId(rs1.getString("ID_PEND"));
				voto.getPendenciaDt().setId_Classificador(rs1.getString("ID_CLASSIFICADOR_PEND"));
				voto.getPendenciaDt().setClassificador(rs1.getString("CLASSIFICADOR_PEND"));
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME"));
				voto.getProcessoDt().setId(rs1.getString("ID_PROC"));
				voto.getProcessoDt().setId_Classificador(rs1.getString("ID_CLASSIFICADOR_PROC"));
				voto.getProcessoDt().setClassificador(rs1.getString("CLASSIFICADOR_PROC"));
				voto.getPendenciaDt().setCodigoTemp(rs1.getString("ID_SERV")+"@"+rs1.getString("SERV"));
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					voto.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					voto.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					voto.getProcessoDt().setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	/**
	 * Consulta a quantidade de votos pre analisados aberto
	 * 
	 * @param id_ServentiaCargo, serventiaCargo respons\E1vel pelos votos
	 * @since 14/10/2019 15:56
	 * @author lrcampos
	 */
	public int consultarQuantidadeVotosPreAnalisados(String idServentiaCargo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		//lrcampos 14/10/2019 15:56 - Mudando o sql para ficar igual a listagem da grid de processos pre analisados
		StringBuilder sql = new StringBuilder("SELECT DISTINCT COUNT(*) QUANTIDADE  ");
		sql.append(" FROM PROJUDI.PEND_VOTO_VIRTUAL PV ");
		sql.append(" JOIN PROJUDI.PEND P ON PV.ID_PEND = P.ID_PEND ");
		sql.append(" JOIN PROJUDI.PEND_RESP PR ON PV.ID_PEND = PR.ID_PEND ");
		sql.append(" INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND ");
		sql.append(" JOIN PROJUDI.VIEW_AUDI_COMPLETA AC ON PV.ID_AUDI_PROC = AC.ID_AUDI_PROC ");
		sql.append(" WHERE PR.ID_SERV_CARGO = ? ");	ps.adicionarLong(idServentiaCargo);
		sql.append(" AND ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)");	ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
		sql.append(" AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)");	ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return 0;
	}
	
	public int consultarQuantidadeVotosPreAnalisadosOtimizado(String idServentiaCargo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		//lrcampos 14/10/2019 15:56 - Mudando o sql para ficar igual a listagem da grid de processos pre analisados
		
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT   ");
		sql.append("  	COUNT(*) QUANTIDADE   ");
		sql.append("  FROM   ");
		sql.append("  	(   ");
		sql.append("  	SELECT   ");
		sql.append("  		DISTINCT AP.ID_PROC,   ");
		sql.append("  		P.ID_PEND,   ");
		sql.append("  		AP.ID_AUDI_PROC,   ");
		sql.append("  		PA.ID_ARQ   ");
		sql.append("  	FROM   ");
		sql.append("  		PROJUDI.PEND P   ");
		sql.append("  	INNER JOIN PROJUDI.PEND_VOTO_VIRTUAL PV ON   ");
		sql.append("  		PV.ID_PEND = P.ID_PEND   ");
		sql.append("  	INNER JOIN PROJUDI.PEND_ARQ PA ON   ");
		sql.append("  		PA.ID_PEND = PV.ID_PEND   ");
		sql.append("  	INNER JOIN PROC PROC ON   ");
		sql.append("  		PROC.ID_PROC = P.ID_PROC   ");
		sql.append("  	INNER JOIN PROJUDI.PEND_RESP PR ON   ");
		sql.append("  		P.ID_PEND = PR.ID_PEND   ");
		sql.append("  	INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON   ");
		sql.append("  		APP.ID_PEND = P.ID_PEND   ");
		sql.append("  	INNER JOIN AUDI_PROC AP ON   ");
		sql.append("  		AP.ID_PROC = P.ID_PROC   ");
		sql.append("  	INNER JOIN PEND_STATUS STATUS ON   ");
		sql.append("  		STATUS.ID_PEND_STATUS = P.ID_PEND_STATUS   ");
		sql.append("  	INNER JOIN PEND_TIPO TIP ON   ");
		sql.append("  		P.ID_PEND_TIPO = TIP.ID_PEND_TIPO   ");
		sql.append("  	WHERE   ");
		sql.append("  		PR.ID_SERV_CARGO = ?   ");
		ps.adicionarLong(idServentiaCargo);
		sql.append("  		AND STATUS.PEND_STATUS_CODIGO = 1   ");
		sql.append("  		AND TIP.PEND_TIPO_CODIGO = 200 ) MYVIEW   ");
		sql.append("  WHERE   ");
		sql.append("  	(   ");
		sql.append("  	SELECT   ");
		sql.append("  		COUNT(PA1.ID_ARQ)   ");
		sql.append("  	FROM   ");
		sql.append("  		PROJUDI.PEND_ARQ PA1   ");
		sql.append("  	WHERE   ");
		sql.append("  		PA1.ID_ARQ = MYVIEW.ID_ARQ   ");
		sql.append("  	GROUP BY   ");
		sql.append("  		PA1.ID_ARQ) = 1   ");
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return 0;
	}

	private String selectPendenciaStatus(PreparedStatementTJGO ps, int tipo) throws Exception {
		return new Select("ID_PEND_STATUS", ps)
				.from("PROJUDI.PEND_STATUS")
				.whereEqual("PEND_STATUS_CODIGO", tipo)
				.build();
	}

	private String selectPendenciaTipo(PreparedStatementTJGO ps, int tipo) throws Exception {
		return new Select("ID_PEND_TIPO", ps).from("PROJUDI.PEND_TIPO").whereEqual("PEND_TIPO_CODIGO", tipo).build();
	}

/**
	 * Consulta a quantidade de votos aguardando assinatura
	 * 
	 * @param id_ServentiaCargo, serventiaCargo respons\E1vel pelos votos
	 * @since 14/10/2019 15:56
	 * @author lrcampos
	 */
	public int consultarQuantidadeVotosAguardandoAssinatura(String idServentiaCargo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//lrcampos 14/10/2019 16:18 - Mudando o sql para ficar igual a listagem da grid de processos aguardando assinatura
		StringBuilder sql = new StringBuilder("SELECT COUNT(*) Q  "); 
		sql.append(" FROM PROJUDI.PEND_VOTO_VIRTUAL PV ");
		sql.append(" JOIN PROJUDI.PEND P ON PV.ID_PEND = P.ID_PEND ");
		sql.append(" JOIN PROJUDI.PEND_RESP PR ON PV.ID_PEND = PR.ID_PEND ");
		sql.append(" INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND ");
		sql.append(" JOIN PROJUDI.VIEW_AUDI_COMPLETA AC ON PV.ID_AUDI_PROC = AC.ID_AUDI_PROC ");
		sql.append(" WHERE PR.ID_SERV_CARGO = ? ");	ps.adicionarLong(idServentiaCargo);
		sql.append(" AND ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)");	ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);
		sql.append(" AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)");	ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);

			if (rs1.next()) {
				return rs1.getInt("Q");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return 0;
	}

	// jvosantos - 20/08/2019 12:06 - Alterar ID_PROC para ID_AUDI_PROC 
	public int verificarVotosAbertos(String idAudiProc) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = " SELECT COUNT(*) AS Q FROM PROJUDI.PEND P INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON P.ID_PEND = APP.ID_PEND WHERE P.ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PROJUDI.PEND_TIPO WHERE PEND_TIPO_CODIGO = ?) AND APP.ID_AUDI_PROC = ? ";
		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		ps.adicionarLong(idAudiProc);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getInt("Q");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return 0;

	}

	public String consultarIdVotoTipo(String codigo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT ID_VOTO_VIRTUAL_TIPO AS Q FROM PROJUDI.VOTO_VIRTUAL_TIPO "
				+ " WHERE VOTO_VIRTUAL_TIPO_CODIGO = ?";
		ps.adicionarLong(codigo);
		ResultSetTJGO rs = null;
		try {
			rs = consultar(sql, ps);
			if (rs.next()) {
				return rs.getString("Q");
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return null;
	}

	// jvosantos - 21/08/2019 09:20 - Usar tabela de rela\E7\E3o AUDI_PROC_PEND
	public List consultarAguardandoFinalizacao(String id_ServentiaCargo, String processoNumero, int status)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT AP.DATA_AGENDADA, AP.ID_AUDI_PROC, AP.ID_PROC, AP.PROC_NUMERO, PV.ID_PEND, PT.PROC_TIPO, ");
		sql.append(" (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ");
	    sql.append("	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "); 
	    sql.append("	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ");
	    sql.append(" 	WHERE AP1.ID_AUDI_PROC = APP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "); 
	    sql.append(" 	(SELECT 1 FROM RECURSO R "); 
	    sql.append("		INNER JOIN AUDI_PROC AP2 ON AP2.ID_PROC = R.ID_PROC "); 
	    sql.append("		WHERE AP2.ID_AUDI_PROC = APP.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO ");
		sql.append(" FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA AP ");
		sql.append(" JOIN PROJUDI.AUDI_PROC_PEND APP ON AP.ID_AUDI_PROC = APP.ID_AUDI_PROC ");
		sql.append(" JOIN PROJUDI.AUDI_PROC AP ON AP.ID_AUDI_PROC = APP.ID_AUDI_PROC ");
		sql.append(" JOIN PROJUDI.PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" JOIN PROJUDI.VIEW_PEND PV ON (PV.ID_PEND = APP.ID_PEND AND PV.PEND_TIPO_CODIGO = ? AND PEND_STATUS_CODIGO = ?)");
		ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
		ps.adicionarLong(status);

		sql.append(" AND AP.ID_SERV_CARGO = ?");
		ps.adicionarLong(id_ServentiaCargo);
		sql.append(" AND AP.AUDI_PROC_STATUS_CODIGO = ? ");
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		if (processoNumero != null && !processoNumero.isEmpty()) {
			sql.append(" AND AP.PROC_NUMERO = ? ");
			ps.adicionarString(processoNumero);
		}

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				VotoSessaoLocalizarDt audiencia = new VotoSessaoLocalizarDt();
				ProcessoDt processoDt = new ProcessoDt();
				audiencia.setDataSessao(Funcoes.FormatarDataHora(rs1.getString("DATA_AGENDADA")));
				audiencia.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				audiencia.setIdProcesso(rs1.getString("ID_PROC"));
				audiencia.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				audiencia.setIdPendencia(rs1.getString("ID_PEND"));
				 //lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sess\E3o possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				audiencia.setProcessoDt(processoDt);
				lista.add(audiencia);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	public List<VotoSessaoLocalizarDt> consultarAguardandoFinalizacaoPrazo(String id_ServentiaCargo,
			String processoNumero)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT DISTINCT AP.DATA_AGENDADA, AP.ID_AUDI_PROC, AP.ID_PROC, AP.PROC_NUMERO ";
		List lista = new ArrayList<>();
		sql += " FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA AP"
				+ " JOIN PROJUDI.VIEW_PEND PV ON (PV.ID_PROC = AP.ID_PROC) "
				+ " WHERE PV.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		sql += " AND PV.DATA_LIMITE < SYSDATE";

		sql += " AND AP.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);
		sql += " AND AP.AUDI_PROC_STATUS_CODIGO = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		if (processoNumero != null && !processoNumero.isEmpty()) {
			sql += " AND AP.PROC_NUMERO = ? ";
			ps.adicionarString(processoNumero);
		}

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				VotoSessaoLocalizarDt audiencia = new VotoSessaoLocalizarDt();
				audiencia.setDataSessao(Funcoes.FormatarDataHora(rs1.getString("DATA_AGENDADA")));
				audiencia.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				audiencia.setIdProcesso(rs1.getString("ID_PROC"));
				audiencia.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				lista.add(audiencia);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}
	
	public List<VotoSessaoLocalizarDt> consultarPrazoExpiradoServentiaEspecial()
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT DISTINCT AP.DATA_AGENDADA, AP.ID_AUDI_PROC, AP.ID_PROC, AP.PROC_NUMERO ";
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		sql += " FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA AP"
				+ " JOIN PROJUDI.VIEW_PEND PV ON (PV.ID_PROC = AP.ID_PROC) "
				+ " JOIN AUDI_PROC_PEND APP ON (APP.ID_AUDI_PROC = AP.ID_AUDI_PROC AND APP.ID_PEND = PV.ID_PEND) "
				+ " JOIN PROC P ON P.ID_PROC = AP.ID_PROC "
				+ " WHERE PV.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		sql += " AND PV.DATA_LIMITE < SYSDATE";
		sql += " AND P.ID_SERV  IN (?, ?, ?, ?, ?)";
		ps.adicionarLong(ServentiaDt.SERV_PRIMEIRA_SECAO_CIVEL);
		ps.adicionarLong(ServentiaDt.SERV_SEGUNDA_SECAO_CIVEL);
		ps.adicionarLong(ServentiaDt.SERV_ORGAO_ESPECIAL);
		ps.adicionarLong(ServentiaDt.SERV_CONSELHO_SUPERIOR_MAGISTRATURA);
		ps.adicionarLong(ServentiaDt.SERV_SESSAO_CRIMINAL);
		sql += " AND AP.AUDI_PROC_STATUS_CODIGO = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);


		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				VotoSessaoLocalizarDt audiencia = new VotoSessaoLocalizarDt();
				audiencia.setDataSessao(Funcoes.FormatarDataHora(rs1.getString("DATA_AGENDADA")));
				audiencia.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				audiencia.setIdProcesso(rs1.getString("ID_PROC"));
				audiencia.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				lista.add(audiencia);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}


	public String consultarIdRelator(String idAudiProc) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_SERV_CARGO AS Q FROM AUDI_PROC_VOTANTES ";
		sql += " WHERE ID_AUDI_PROC = ?  AND RELATOR = 1 ";

		ps.adicionarLong(idAudiProc);

		ResultSetTJGO rs = null;
		try {
			rs = consultar(sql, ps);
			if (rs.next()) {
				return rs.getString("Q");
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return null;
	}

	public List<VotoSessaoLocalizarDt> consultarPendeciasVoto(String idServentiaCargo,
			String procNumero,
			int tipo,
			int status,
			String idServentiaFiltro) throws Exception {
		List<VotoSessaoLocalizarDt> pendencias = new ArrayList<VotoSessaoLocalizarDt>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		StringBuilder sql =  new StringBuilder("SELECT PT.PROC_TIPO, A.DATA_AGENDADA, P.ID_PEND, P.DATA_LIMITE, P.ID_PROC, P.PROC_NUMERO_COMPLETO, ");
		sql.append("AP.ID_AUDI_PROC, SC.NOME_USU, A.ID_SERV, SS.SERV, "); // jvosantos - 20/08/2019 15:05 - Usar StringBuilder e adicionar INNER JOIN com AUDI_PROC_PEND
		sql.append(" (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ");
	    sql.append("	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "); 
	    sql.append("	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ");
	    sql.append(" 	WHERE AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "); 
	    sql.append(" 	(SELECT 1 FROM RECURSO R "); 
	    sql.append("		INNER JOIN AUDI_PROC AP2 ON AP2.ID_PROC = R.ID_PROC "); 
	    sql.append("		WHERE AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO ");
		sql.append(" FROM PROJUDI.VIEW_PEND P ");
		sql.append(" JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ");		
		sql.append(" JOIN PROJUDI.AUDI_PROC AP ON P.ID_PROC = AP.ID_PROC ");
		sql.append(" JOIN PROJUDI.AUDI A ON A.ID_AUDI = AP.ID_AUDI ");
		sql.append(" JOIN PROJUDI.AUDI_PROC_PEND APP ON (APP.ID_PEND = P.ID_PEND AND AP.ID_AUDI_PROC = APP.ID_AUDI_PROC) ");
		sql.append(" JOIN PROJUDI.VIEW_SERV_CARGO SC ON SC.ID_SERV_CARGO = AP.ID_SERV_CARGO ");
		sql.append(" INNER JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" INNER JOIN SERV SS ON SS.ID_SERV = A.ID_SERV ");
		sql.append(" WHERE P.PEND_TIPO_CODIGO = ? ");
		ps.adicionarLong(tipo);
		sql.append(" AND PR.ID_SERV_CARGO = ? ");
		ps.adicionarLong(idServentiaCargo);
		sql.append(" AND AP.DATA_MOVI IS NULL ");
		sql.append(" AND P.PEND_STATUS_CODIGO = ? ");
		ps.adicionarLong(status);

		if (procNumero != null && !procNumero.isEmpty()) {
			sql.append(" AND P.PROC_NUMERO_COMPLETO = ? ");
			ps.adicionarString(procNumero);
		}
		//lrcampos 19/03/2020 15:38 - Incluindo filtro de serventia
		if(StringUtils.isNotEmpty(idServentiaFiltro)) {
			sql.append(" AND A.ID_SERV = ? "); ps.adicionarLong(idServentiaFiltro);
		}

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				ProcessoDt processoDt = new ProcessoDt();
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setPrazoVotacao(rs1.getString("DATA_LIMITE"));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME_USU"));

				PendenciaDt pendenciaDt = new PendenciaDt();
				voto.setPendenciaDt(pendenciaDt);
				voto.getPendenciaDt().setCodigoTemp(rs1.getString("ID_SERV")+"@"+rs1.getString("SERV"));

				//lrcampos 30/01/2020 13:09 - Incluindo classe do Recurso secundario em caso do processo possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				
				voto.setProcessoDt(processoDt);
				pendencias.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}

		return pendencias;
	}

	public VotoTipoDt consultarVotoTipo(String codigo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT * FROM PROJUDI.VOTO_VIRTUAL_TIPO " + " WHERE VOTO_VIRTUAL_TIPO_CODIGO = ?";
		ps.adicionarLong(codigo);
		ResultSetTJGO rs = null;
		try {
			rs = consultar(sql, ps);
			if (rs.next()) {
				VotoTipoDt votoTipo = new VotoTipoDt();
				votoTipo.setCodigo(rs.getString("VOTO_VIRTUAL_TIPO_CODIGO"));
				votoTipo.setDescricao(rs.getString("VOTO_VIRTUAL_TIPO"));
				votoTipo.setId(rs.getString("ID_VOTO_VIRTUAL_TIPO"));
				return votoTipo;
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return null;
	}

	public int consultarQuantidadePendenciasVoto(String idServentiaCargo, int tipo, int status) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT count(*) as Q" + " FROM PROJUDI.PEND P ";
		sql += " INNER JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ";
		sql += " INNER JOIN AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND ";
		sql += " WHERE PR.ID_SERV_CARGO = ?";
		ps.adicionarLong(idServentiaCargo);
		sql += " AND ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)";
		ps.adicionarLong(status);
		sql += " AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(tipo);

		sql += " AND EXISTS (SELECT 1 FROM AUDI_PROC AP WHERE AP.ID_PROC = P.ID_PROC AND AP.DATA_MOVI IS NULL )";

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getInt("Q");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return 0;
	}

	public List consultarRetirarPauta(String idServentiaCargo, String processoNumero) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		String sql = "SELECT  AC.ID_PROC, AC.DATA_AGENDADA, AC.PROC_NUMERO, P.ID_PEND, AC.ID_AUDI_PROC, SC.NOME_USU  "
				+ " FROM  PROJUDI.VIEW_AUDI_PROC_COMPLETA AC" + " JOIN PROJUDI.PEND P ON P.ID_PROC = AC.ID_PROC"
				+ " JOIN PROJUDI.PEND_RESP PR ON PR.ID_PEND = P.ID_PEND"
				+ " JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO"
				+ " JOIN PROJUDI.PROC PROC ON PROC.ID_PROC = P.ID_PROC"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO SC ON SC.ID_SERV_CARGO = AC.ID_SERV_CARGO ";

		if (idServentiaCargo == null) {
			sql += " WHERE PR.ID_SERV IS NOT NULL ";
		} else {
			sql += " WHERE PR.ID_SERV_CARGO = ?";
			ps.adicionarLong(idServentiaCargo);
		}
		sql += " AND ID_PEND_STATUS = ? ";
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		sql += " AND PT.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.RETIRAR_PAUTA);
		sql += " AND AC.ID_AUDI_PROC_STATUS = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);

		filtroProcessoNumero(sql, processoNumero, ps);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME_USU"));
				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	@Deprecated
	public List consultarResultadoUnanime(String idServentiaCargo, String processoNumero) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		String sql = "SELECT  AC.ID_PROC, AC.DATA_AGENDADA, AC.PROC_NUMERO, P.ID_PEND, AC.ID_AUDI_PROC, SC.NOME_USU  "
				+ " FROM  PROJUDI.VIEW_AUDI_PROC_COMPLETA AC" + " JOIN PROJUDI.PEND P ON P.ID_PROC = AC.ID_PROC"
				+ " JOIN PROJUDI.PEND_RESP PR ON PR.ID_PEND = P.ID_PEND"
				+ " JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO SC ON SC.ID_SERV_CARGO = AC.ID_SERV_CARGO ";

		if (idServentiaCargo == null) {
			sql += " WHERE PR.ID_SERV IS NOT NULL ";
		} else {
			sql += " WHERE PR.ID_SERV_CARGO = ?";
			ps.adicionarLong(idServentiaCargo);
		}
		sql += " AND ID_PEND_STATUS = ? ";
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		sql += " AND PT.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.RESULTADO_UNANIME);
		sql += " AND AC.DATA_MOVI_AUDI IS NULL ";
		sql += " AND AC.ID_AUDI_PROC_STATUS = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		if (StringUtils.isNotEmpty(processoNumero)) {
			sql += " AND P.ID_PROC = ?";
			ps.adicionarLong(processoNumero);
		}

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME_USU"));
				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	// jvosantos - 15/10/2019 17:04 - Corrigir nome de atributo
	public List consultarSustentacaoOral(String idUsuarioServentia, String processoNumero) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		String sql = "SELECT DISTINCT AC.ID_PROC, AC.DATA_AGENDADA, AC.PROC_NUMERO, P.ID_PEND, AC.ID_AUDI_PROC, "
				+ " SC.NOME_USU NOME_RELATOR, USV.NOME SOLICITANTE_SO, UADV.NOME NOME_ADV  "
				+ " FROM  PROJUDI.VIEW_AUDI_PROC_COMPLETA AC" + " JOIN PROJUDI.PEND P ON P.ID_PROC = AC.ID_PROC"
				+ " JOIN PROJUDI.PROC PROC ON PROC.ID_PROC = P.ID_PROC "
				+ " JOIN PROJUDI.PEND_RESP PR ON PR.ID_PEND = P.ID_PEND"
				+ " JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO SC ON SC.ID_SERV_CARGO = AC.ID_SERV_CARGO "
				+ " JOIN PROJUDI.VIEW_USU_SERV_GRUPO USV ON USV.ID_USU_SERV = P.ID_USU_CADASTRADOR "
				+ " JOIN SUSTENTACAO_ORAL SO ON SO.ID_PEND = P.ID_PEND "
			    + " JOIN PROC_PARTE_ADVOGADO PPA ON	PPA.ID_PROC_PARTE_ADVOGADO = SO.ID_PROC_PARTE_ADVOGADO "
			    + " JOIN USU_SERV USVADV ON	USVADV.ID_USU_SERV = PPA.ID_USU_SERV "
			    + " JOIN USU UADV ON UADV.ID_USU = USVADV.ID_USU ";

		if (idUsuarioServentia == null) {
			sql += " WHERE PR.ID_SERV IS NOT NULL ";
		} else {
			sql += " WHERE PR.ID_SERV_CARGO = ?";
			ps.adicionarLong(idUsuarioServentia);
		}
		sql += " AND ID_PEND_STATUS = ? ";
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		sql += " AND PT.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
		sql += " AND AC.DATA_MOVI_AUDI IS NULL ";
		sql += " AND AC.ID_AUDI_PROC_STATUS = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql = filtroProcessoNumero(sql, processoNumero, ps);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME_RELATOR"));
				voto.setNomeAdvPedidoSusOral(rs1.getString("NOME_ADV"));
				voto.setSolicitanteSustentacaOral(rs1.getString("SOLICITANTE_SO"));
				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}
	
	private String filtroProcessoNumero(String sql, String processoNumero, PreparedStatementTJGO ps) throws Exception {
		return filtroProcessoNumero(sql, processoNumero, ps, "PROC");
	}
	
	private void filtroProcessoNumero(StringBuilder sql, String processoNumero, PreparedStatementTJGO ps) throws Exception {
		filtroProcessoNumero(sql, processoNumero, ps, "PROC");
	}

	private String filtroProcessoNumero(String sql, String processoNumero, PreparedStatementTJGO ps, String fieldName) throws Exception {
		StringBuilder stringBuilder = new StringBuilder(sql);
		
		filtroProcessoNumero(stringBuilder, processoNumero, ps, fieldName);
		
		return stringBuilder.toString();
	}

	private void filtroProcessoNumero(StringBuilder sql, String processoNumero, PreparedStatementTJGO ps, String fieldName) throws Exception {
		fieldName = StringUtils.defaultString("PROC");
		//mrbatista 21/10/2019 16:47 - Corre\E7\E3o da query.
		if (StringUtils.isNotEmpty(processoNumero)) {
			if ((processoNumero.length() > 0)) {
				sql.append(" AND "+fieldName+".PROC_NUMERO = ?");
				String[] numeroProcesso = processoNumero.split("[-\\.]");
				ps.adicionarLong(numeroProcesso[0]);

				if (numeroProcesso.length > 1) {
					sql.append(" AND "+fieldName+".DIGITO_VERIFICADOR = ?");
					ps.adicionarLong(numeroProcesso[1]);
				}
			}
		}
	}

	public List<VotoSessaoLocalizarDt> consultarTomarConhecimento(String idServentiaCargo, String idServentia, int tipo, String processoNumero)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  AC.ID_PROC, AC.DATA_AGENDADA, AC.PROC_NUMERO, P.ID_PEND, AC.ID_AUDI_PROC, SC.NOME_USU, PTIPO.PROC_TIPO  ");
		sql.append(", (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ");
	    sql.append("	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "); 
	    sql.append("	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ");
	    sql.append(" 	WHERE AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "); 
	    sql.append(" 	(SELECT 1 FROM RECURSO R "); 
	    sql.append("		INNER JOIN AUDI_PROC AP2 ON AP2.ID_PROC = R.ID_PROC "); 
	    sql.append("		WHERE AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO ");
		sql.append(" FROM  PROJUDI.VIEW_AUDI_PROC_COMPLETA AC");
		sql.append(" JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_AUDI_PROC = AC.ID_AUDI_PROC"); // jvosantos - 03/12/2019 15:44 - Amarrar com AUDI_PROC_PEND
		sql.append(" JOIN PROJUDI.AUDI_PROC AP ON AP.ID_AUDI_PROC = APP.ID_AUDI_PROC ");
		sql.append(" JOIN PROJUDI.PROC PROC ON PROC.ID_PROC = AP.ID_PROC ");
		sql.append(" JOIN PROJUDI.PROC_TIPO PTIPO ON PTIPO.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" JOIN PROJUDI.PEND P ON P.ID_PEND = APP.ID_PEND");
		sql.append(" JOIN PROJUDI.PEND_RESP PR ON PR.ID_PEND = P.ID_PEND");
		sql.append(" JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO");
		sql.append(" JOIN PROJUDI.VIEW_SERV_CARGO SC ON SC.ID_SERV_CARGO = AC.ID_SERV_CARGO ");
		sql.append(" JOIN PROJUDI.AUDI AUDI ON AUDI.ID_AUDI = AC.ID_AUDI ");

		if (idServentiaCargo == null) {
			sql.append(" WHERE PR.ID_SERV = ? ");
			ps.adicionarLong(idServentia);
		} else {
			sql.append(" WHERE PR.ID_SERV_CARGO = ?");
			ps.adicionarLong(idServentiaCargo);
		}
		sql.append(" AND ID_PEND_STATUS = ? ");
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		sql.append(" AND PT.PEND_TIPO_CODIGO = ? ");
		ps.adicionarLong(tipo);
		
		if(tipo == PendenciaTipoDt.RETIRAR_PAUTA) {
			sql.append(" AND ((AUDI.DATA_MOVI IS NULL ");
			sql.append(" AND AC.AUDI_PROC_STATUS_CODIGO = ? ");
			ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			sql.append(" AND P.CODIGO_TEMP IS NULL) ");
			sql.append(" OR (P.CODIGO_TEMP = AC.ID_AUDI_PROC AND P.ID_PROC = AC.ID_PROC))");		
		}else if(tipo != PendenciaTipoDt.ADIADO_PELO_RELATOR) {
			sql.append(" AND AUDI.DATA_MOVI IS NULL ");
			sql.append(" AND AC.AUDI_PROC_STATUS_CODIGO = ? ");
			ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		}
		
		filtroProcessoNumero(sql, processoNumero, ps);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);

			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME_USU"));
				 //lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sess\E3o possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					voto.setClasseProcesso(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					voto.setClasseProcesso(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					voto.setClasseProcesso(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	public List<String> consultarIdsPendProcesso(String idProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<String> lista = new ArrayList<>();
		String sql = "SELECT P.ID_PEND FROM PROJUDI.PEND P ";
		sql += " JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO ";
		sql += " WHERE ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		sql += " AND PT.PEND_TIPO_CODIGO IN (202, 200) ";

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String idPend = new String();
				idPend = rs1.getString("ID_PEND");
				lista.add(idPend);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	// jvosantos - 20/08/2019 17:02 - M\E9todo para consultar pend\EAncias de Voto e Proclama\E7\E3o de uma AUDI_PROC
	public List<String> consultarIdsPendAudienciaProcesso(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<String> lista = new ArrayList<>();
		
		String sql = "SELECT P.ID_PEND FROM PROJUDI.PEND P JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON P.ID_PEND = APP.ID_PEND WHERE APP.ID_AUDI_PROC = ? AND PT.PEND_TIPO_CODIGO IN (?, ?)";

		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);		
		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String idPend = new String();
				idPend = rs1.getString("ID_PEND");
				lista.add(idPend);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	public String verificarSessaoVirtual(String idProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String virtual = null;
		String sql = "SELECT A.VIRTUAL FROM PROJUDI.VIEW_AUDI_PROC AP "
				+ " JOIN PROJUDI.AUDI A ON A.ID_AUDI = AP.ID_AUDI " + " WHERE ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		sql += " AND A.VIRTUAL = ? ";
		ps.adicionarLong(1);
		sql += " AND ID_AUDI_PROC_STATUS = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				virtual = rs1.getString("VIRTUAL");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return virtual;
	}

	public String pendenciaAbertasAdvSustentacaoOral(String idProcesso, String idUsuLogado, String idServentia, Boolean isSecretario, String idServentiaProc)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String virtual = "";
		String sql = "SELECT PT.PEND_TIPO_CODIGO AS PEND_TIPO_CODIGO  FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA APC "
				+ " JOIN PROJUDI.PEND P ON P.ID_PROC = APC.ID_PROC "
				+ " JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO "
				+ " JOIN PROJUDI.PEND_RESP PR ON PR.ID_PEND = P.ID_PEND ";
		sql += " WHERE P.ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		sql += " AND APC.AUDI_PROC_STATUS_CODIGO = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		if(isSecretario) {
			sql += " AND ( P.ID_USU_CADASTRADOR = ? OR PR.ID_USU_RESP = ? OR pr.ID_SERV = ? ) ";
			ps.adicionarLong(idUsuLogado);
			ps.adicionarLong(idUsuLogado);
			ps.adicionarLong(idServentiaProc);
			
		}
		else {
			if (idServentia == null) {
				sql += " AND PR.ID_SERV IS NULL ";
				sql += " AND ( P.ID_USU_CADASTRADOR = ? OR PR.ID_USU_RESP = ? ) ";
				ps.adicionarLong(idUsuLogado);
				ps.adicionarLong(idUsuLogado);
			} else {
				sql += " AND PR.ID_SERV IS NULL";
			}
		}
		sql += " AND PT.PEND_TIPO_CODIGO IN (?, ?, ?, ?) ";
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA);
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SO_DEFERIMENTO_AUTOMATICO);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				virtual = rs1.getString("PEND_TIPO_CODIGO");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return virtual;
	}

	public boolean existeSustentacaoOralDeferida(String idProcesso)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = new Select("1", ps)
				.from("PROJUDI.VIEW_PEND_FINAL")
				.whereEqual("PEND_TIPO_CODIGO", PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO)
				.andEqual("ID_PROC", idProcesso)
				.build();
		sql += " UNION ";
		sql += new Select("1", ps)
				.from("PROJUDI.VIEW_PEND")
				.whereEqual("PEND_TIPO_CODIGO", PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO)
				.andEqual("ID_PROC", idProcesso)
				.build();

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			return rs1.next();
		} finally {
			if (rs1 != null)
				rs1.close();
		}
	}

	public String pendenciaAbertasAdvSustentacaoOralIdPend(String idProcesso, String idUsuLogado, String idServentia)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String virtual = null;
		String sql = "SELECT P.ID_PEND  FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA APC "
				+ " JOIN PROJUDI.PEND P ON P.ID_PROC = APC.ID_PROC "
				+ " JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO "
				+ " JOIN PROJUDI.PEND_RESP PR ON PR.ID_PEND = P.ID_PEND ";
		sql += " WHERE P.ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		sql += " AND APC.AUDI_PROC_STATUS_CODIGO = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		if (idServentia == null) {
			sql += " AND PR.ID_SERV IS NULL ";
			sql += " AND ( P.ID_USU_CADASTRADOR = ? OR PR.ID_USU_RESP = ? ) ";
			ps.adicionarLong(idUsuLogado);
			ps.adicionarLong(idUsuLogado);
		} else {
			sql += " AND PR.ID_SERV IS NULL";
		}
		sql += " AND PT.PEND_TIPO_CODIGO IN (?, ?, ?) ";
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				virtual = rs1.getString("ID_PEND");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return virtual;
	}

	public List pendenciaAbertasSecretarioSustentacaoOral(String idProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List lista = new ArrayList<>();
		String sql = " SELECT PT.PEND_TIPO_CODIGO AS PEND_TIPO_CODIGO, " + "	CASE "
				+ "		WHEN P.ID_USU_CADASTRADOR IS NULL THEN PR.ID_USU_RESP " + "		ELSE P.ID_USU_CADASTRADOR "
				+ "	END AS ADV_RESP " + " FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA APC"
				+ " JOIN PROJUDI.PEND P ON P.ID_PROC = APC.ID_PROC "
				+ " JOIN PROJUDI.PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO "
				+ " JOIN PROJUDI.PEND_RESP PR ON PR.ID_PEND = P.ID_PEND ";
		sql += " WHERE P.ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		sql += " AND APC.AUDI_PROC_STATUS_CODIGO = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql += " AND PR.ID_SERV IS NULL";
		sql += " AND PT.PEND_TIPO_CODIGO IN (?, ?, ?) ";
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				PendenciaDt pendResp = new PendenciaDt();
				pendResp.setUsuarioCadastrador(rs1.getString("ADV_RESP"));
				pendResp.setId_PendenciaTipo(rs1.getString("PEND_TIPO_CODIGO"));

				lista.add(pendResp);

			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	public int verificarImpedimentosAbertos(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT COUNT(*) AS Q FROM PROJUDI.AUDI_PROC_VOTANTES";
		sql += " WHERE ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		sql += " AND ID_IMPEDIMENTO_TIPO = (SELECT ID_IMPEDIMENTO_TIPO FROM IMPEDIMENTO_TIPO WHERE IMPEDIMENTO_TIPO_CODIGO = ?) ";
		ps.adicionarLong(ImpedimentoTipoDt.AGUARDANDO_VERIFICACAO_VOTANTE);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getLong("Q").intValue();
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}

		return 0;

	}

	public void alterarIdPendenciaVoto(String idPendenciaFinalizada, String idNovaPendencia) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";

		stSql = "UPDATE PROJUDI.PEND_VOTO_VIRTUAL SET  ";
		stSql += " ID_PEND = ?";
		ps.adicionarString(idNovaPendencia);

		stSql += " WHERE ID_PEND = ? ";
		ps.adicionarLong(idPendenciaFinalizada);

		executarUpdateDelete(stSql, ps);
	}

	public void alterarFuncaoVotante(String idAudienciaProcesso,
			String idServentiaCargo,
			int votanteTipoCodigo,
			int ordem) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql = "";

		stSql = "UPDATE PROJUDI.AUDI_PROC_VOTANTES SET  ";
		stSql += " ID_VOTANTE_TIPO = (SELECT ID_VOTANTE_TIPO FROM VOTANTE_TIPO WHERE VOTANTE_TIPO_CODIGO = ?), ";
		ps.adicionarLong(votanteTipoCodigo);
		stSql += " ORDEM_VOTANTE = ? ";
		ps.adicionarLong(ordem);

		stSql += " WHERE ID_AUDI_PROC = ? AND ID_SERV_CARGO = ? ";
		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(idServentiaCargo);

		executarUpdateDelete(stSql, ps);
	}

	public List<AudienciaDt> consultarSessoesPublica(String dataInicial, String dataFinal, String pagina)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<AudienciaDt> list = new ArrayList<>();
		Select select = new Select("ID_AUDI, DATA_AGENDADA, SERV", ps)
				.from("PROJUDI.VIEW_AUDI")
				.whereEqual("VIRTUAL", true)
				.andEqual("SESSAO_INICIADA", true)
				.andNull("DATA_MOVI");
		if (StringUtils.isNotEmpty(dataInicial)) {
			select.and().greaterOrEqual("DATA_AGENDADA", dataInicial);
		}
		if (StringUtils.isNotEmpty(dataFinal)) {
			select.and().lessOrEqual("DATA_AGENDADA", dataFinal + " 23:59");
		}
		select.order("DATA_AGENDADA DESC");
		ResultSetTJGO rs = null;
		try {
			rs = consultarPaginacao(select.build(), ps, pagina);
			while (rs.next()) {
				AudienciaDt audienciaDt = new AudienciaDt();
				audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs.getString("DATA_AGENDADA")));
				audienciaDt.setId(rs.getString("ID_AUDI"));
				audienciaDt.setServentia(rs.getString("SERV"));
				list.add(audienciaDt);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return list;
	}

	public long consultarQuantidadeSessoesPublica(String dataInicial, String dataFinal) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Select select = new Select("COUNT(*) q", ps)
				.from("PROJUDI.VIEW_AUDI")
				.whereEqual("VIRTUAL", true)
				.andEqual("SESSAO_INICIADA", true)
				.andNull("DATA_MOVI");

		if (StringUtils.isNotEmpty(dataInicial)) {
			select.and().greaterOrEqual("DATA_AGENDADA", dataInicial);
		}
		if (StringUtils.isNotEmpty(dataFinal)) {
			select.and().lessOrEqual("DATA_AGENDADA", dataInicial);
		}
		select.order("DATA_AGENDADA DESC");
		ResultSetTJGO rs = null;
		try {
			rs = consultar(select.build(), ps);
			if (rs.next()) {
				return rs.getLong("q");
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return 0;
	}

	public List<SessaoVirtualPublicaDt> consultarProcessosSessaoPublica(String idAudiencia) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<SessaoVirtualPublicaDt> list = new ArrayList<>();
		// jvosantos - 28/08/2019 16:09 - Corre\E7\F5es para processos com v\E1rios recursos (gerava duplicidade na Corte Especial)
		Select select = new Select("P.ID_PROC, AP.ID_AUDI_PROC", ps)
				.from("AUDI_PROC", "AP")
				.join("PROJUDI.PROC", "P", "P.ID_PROC = AP.ID_PROC")
				.whereEqual("AP.ID_AUDI", idAudiencia)
				.andEqual("P.SEGREDO_JUSTICA", false)
				.andEqual("AP.ID_AUDI_PROC_STATUS", AudienciaProcessoStatusDt.A_SER_REALIZADA); //jvosantos - 02/09/2019 11:14 - Adicionar verifica\E7\E3o para trazer apenas audiencias a serem realizadas
		ResultSetTJGO rs = null;
		try {
			rs = consultar(select.build(), ps);
			while (rs.next()) {
				SessaoVirtualPublicaDt sessao = new SessaoVirtualPublicaDt();
				AudienciaProcessoDt audiProc = new AudienciaProcessoDt();
				audiProc.setProcessoDt(new ProcessoDt());
				audiProc.setId(rs.getString("ID_AUDI_PROC"));
				audiProc.getProcessoDt().setId(rs.getString("ID_PROC"));
				sessao.setAudienciaProcesso(audiProc);
				list.add(sessao);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return list;
	}

	public List<ProcessoParteDt> consultarPartesRecursoSecundario(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<ProcessoParteDt> list = new ArrayList<>();
		Select select = new Select("P.NOME, R.ORDEM_PARTE, PT.PROC_PARTE_TIPO_CODIGO, PT.ID_PROC_PARTE_TIPO", ps)
				.from("PROJUDI.RECURSO_SECUNDARIO_PARTE", "R")
				.join("PROJUDI.PROC_PARTE_TIPO", "PT", "PT.ID_PROC_PARTE_TIPO=R.ID_PROC_PARTE_TIPO")
				.join("PROJUDI.VIEW_PROC_PARTE", "P", "P.ID_PROC_PARTE=R.ID_PROC_PARTE")
				.whereEqual("R.ID_AUDI_PROC", idAudienciaProcesso)
				.order("r.ORDEM_PARTE");
		ResultSetTJGO rs = null;
		try {
			rs = consultar(select.build(), ps);
			while (rs.next()) {
				ProcessoParteDt recurso = new ProcessoParteDt();
				recurso.setId_ProcessoParteTipo(rs.getString("ID_PROC_PARTE_TIPO"));
				recurso.setNome(rs.getString("NOME"));
				recurso.setProcessoParteTipoCodigo(rs.getString("PROC_PARTE_TIPO_CODIGO"));
				recurso.setOrdemParte(rs.getInt("ORDEM_PARTE"));	// jvosantos - 12/06/2019 11:09 - Seta a ordem da parte
				list.add(recurso);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return list;
	}
	//lrcampos 15/08/2019 * Busca as partes do recurso e agrupa pela ordem.
	public List<ProcessoParteDt> consultarPartesRecurso(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<ProcessoParteDt> list = new ArrayList<>();
		Select select = new Select("PP.NOME, RP.ORDEM_PARTE,PT.PROC_PARTE_TIPO_CODIGO, PT.ID_PROC_PARTE_TIPO", ps)
				.from("PROJUDI.RECURSO_PARTE", "RP")
				.join("PROJUDI.PROC_PARTE", "PP", "PP.ID_PROC_PARTE = RP.ID_PROC_PARTE")
				.join("PROJUDI.AUDI_PROC", "AP", "( AP.ID_PROC = PP.ID_PROC AND AP.ID_PROC_TIPO = RP.ID_PROC_TIPO)")
				.join("PROC_PARTE_TIPO", "PT", " PT.ID_PROC_PARTE_TIPO = RP.ID_PROC_PARTE_TIPO")
				.whereEqual("AP.ID_AUDI_PROC", idAudienciaProcesso)
				.order("RP.ORDEM_PARTE");
		ResultSetTJGO rs = null;
		try {
			rs = consultar(select.build(), ps);
			while (rs.next()) {
				ProcessoParteDt recurso = new ProcessoParteDt();
				recurso.setId_ProcessoParteTipo(rs.getString("ID_PROC_PARTE_TIPO"));
				recurso.setNome(rs.getString("NOME"));
				recurso.setProcessoParteTipoCodigo(rs.getString("PROC_PARTE_TIPO_CODIGO"));
				recurso.setOrdemParte(rs.getInt("ORDEM_PARTE"));	// jvosantos - 12/06/2019 11:09 - Seta a ordem da parte
				list.add(recurso);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return list;
	}

	public String consultarOrdemTurmaJulgadora(String idServentiaProcesso, String idServentiaGabinete)
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Select select = new Select("ORDEM_TURMA_JULGADORA O", ps)
				.from("PROJUDI.SERV_RELACIONADA")
				.whereEqual("ID_SERV_PRINC", idServentiaProcesso)
				.andEqual("ID_SERV_REL", idServentiaGabinete);
		ResultSetTJGO rs = null;
		try {
			rs = consultar(select.build(), ps);
			if (rs.next()) {
				return rs.getString("O");
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return "";
	}

	public String consultarIdUltimaFinalizacao(String idProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Select select = new Select("ID_PEND", ps)
				.from("PROJUDI.VIEW_PEND_FINAL")
				.whereEqual("PEND_TIPO_CODIGO", PendenciaTipoDt.PROCLAMACAO_VOTO)
				.andEqual("ID_PROC", idProcesso)
				.order("DATA_INICIO DESC");
		ResultSetTJGO rs = null;
		String sql = "SELECT * FROM (" + select.build() + ") WHERE ROWNUM = 1";
		try {
			rs = consultar(sql, ps);
			if (rs.next()) {
				return rs.getString("ID_PEND");
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		return "";
	}

	public RecursoSecundarioParteDt consultarRecursoSecundarioIdProcesso(String idProcesso) throws Exception {
		String sql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		sql = " SELECT R.ID_PROC_TIPO_RECURSO_SEC, PT.PROC_TIPO, R.ID_AUDI_PROC, PT.POLO_ATIVO AS POLO_ATIVO_NVL, PT.POLO_PASSIVO AS POLO_PASSIVO_NVL ";
		sql += " FROM RECURSO_SECUNDARIO_PARTE R "
				+ " JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = R.ID_PROC_TIPO_RECURSO_SEC "				
				+ " JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = R.ID_AUDI_PROC ";
		sql += " WHERE ap.ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		sql += " AND AP.DATA_MOVI IS NULL ";
		sql += " AND ROWNUM = 1 ";

		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				RecursoSecundarioParteDt parteDt = new RecursoSecundarioParteDt();
				parteDt.setId_ProcessoTipoRecursoSecundario(rs1.getString("ID_PROC_TIPO_RECURSO_SEC"));
				parteDt.setProcessoTipoRecursoSecundario(rs1.getString("PROC_TIPO"));
				parteDt.setId_AudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				parteDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO_NVL"));
				parteDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO_NVL"));
				return parteDt;
			}

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public String advogadoSolicitanteSustentacaoOral(String idProcesso, boolean finalizada) throws Exception {
		return advogadoSolicitanteSustentacaoOral(idProcesso, finalizada, false);
	}
	
	public String advogadoSolicitanteSustentacaoOral(String idProcesso, boolean finalizada, boolean diretoPeloSecretario) throws Exception {
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        Select sql = new Select("US.NOME", ps)
                .from(finalizada || diretoPeloSecretario ? VIEW_PENDENCIA_FINALIZADA : VIEW_PENDENCIA, "P")
                .join(
                        finalizada || diretoPeloSecretario ? TABELA_PENDENCIA_RESPONSAVEL_FINALIZADA : TABELA_PENDENCIA_RESPONSAVEL,
                        "PR",
                        "P.ID_PEND=PR.ID_PEND")
                .join("PROJUDI.VIEW_USU_SERV", "US", diretoPeloSecretario ? "US.ID_USU_SERV=P.ID_USU_CADASTRADOR" : "US.ID_USU_SERV=PR.ID_USU_RESP")
                .join("PROJUDI.SERV", "S", "S.ID_SERV=US.ID_SERV")
                .join("AUDI_PROC", "AP", "AP.ID_PROC=P.ID_PROC")
                .join("GRUPO", "G", "G.ID_SERV_TIPO = S.ID_SERV_TIPO")
                .whereEqual("P.ID_PROC", idProcesso)
                .andEqual("P.PEND_TIPO_CODIGO", diretoPeloSecretario ? PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL : PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO)
                .and()
                .in(
                        "G.GRUPO_CODIGO",
                        GrupoDt.ADVOGADO_PARTICULAR,
                        GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL,
                        GrupoDt.ADVOGADO_PUBLICO_ESTADUAL,
                        GrupoDt.ADVOGADO_PUBLICO_UNIAO,
                        GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO,
                        GrupoDt.ADVOGADO_DEFENSOR_PUBLICO,
                        GrupoDt.ADVOGADO_PUBLICO);
                //mrbatista 26/09/2019 - 12:08 pegando o codigo do status da audi proc.
                if(diretoPeloSecretario) {
                    sql.and().addRaw("AP.ID_AUDI_PROC_STATUS = ").subquery("SELECT ID_AUDI_PROC_STATUS FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?");
                    ps.adicionarLong(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL);
                }
                sql.order("DATA_INICIO DESC");
        try {
            rs1 = this.consultar(sql.build(), ps);
            if (rs1.next()) {
                return rs1.getString("NOME");
            }
        } finally {
            try {
                if (rs1 != null)
                    rs1.close();
            } catch (Exception e) {
            }
        }
        return null;
    }

	//lrcampos 06/08/2019 * Busca o advogado que pediu S.O na tabela de SUSTENTACAO_ORAL
		public List<JulgamentoAdiadoDt> solicitantePedidoSustentacaoOralPJD(String idAudiProc) throws Exception {
			ResultSetTJGO rs1 = null;
			
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			PreparedStatementTJGO psDefAut = new PreparedStatementTJGO();
			List<JulgamentoAdiadoDt> listaJulgamentoAdiados = new ArrayList<JulgamentoAdiadoDt>();
			String pendTipoCod = "";
			pendTipoCod = montaSubquerySo(idAudiProc);

			Select sql = new Select("U.NOME NOME_ADV, PP.NOME NOME_PARTE, P.DATA_INICIO DATA_SOLICITADO, PT.PEND_TIPO_CODIGO", ps)
					.from("SUSTENTACAO_ORAL", "SO")
					.join("PROC_PARTE_ADVOGADO","PPA", "PPA.ID_PROC_PARTE_ADVOGADO = SO.ID_PROC_PARTE_ADVOGADO")
					.join("PROC_PARTE", "PP", "PP.ID_PROC_PARTE = PPA.ID_PROC_PARTE")
					.join("USU_SERV", "US", "US.ID_USU_SERV = PPA.ID_USU_SERV")
					.join("USU", "U", "U.ID_USU = US.ID_USU")
					.join("PEND_FINAL", "P", "P.ID_PEND = SO.ID_PEND")
					.join("PEND_TIPO", "PT", "PT.ID_PEND_TIPO = P.ID_PEND_TIPO")
					.whereEqual("SO.ID_AUDI_PROC", idAudiProc)
					.andEqual("PT.PEND_TIPO_CODIGO", pendTipoCod);	
			
			 Select sqlDefAuto = new Select("U.NOME NOME_ADV, PP.NOME NOME_PARTE, P.DATA_INICIO DATA_SOLICITADO, PT.PEND_TIPO_CODIGO", psDefAut)
						.from("SUSTENTACAO_ORAL", "SO")
						.join("PROC_PARTE_ADVOGADO","PPA", "PPA.ID_PROC_PARTE_ADVOGADO = SO.ID_PROC_PARTE_ADVOGADO")
						.join("PROC_PARTE", "PP", "PP.ID_PROC_PARTE = PPA.ID_PROC_PARTE")
						.join("USU_SERV", "US", "US.ID_USU_SERV = PPA.ID_USU_SERV")
						.join("USU", "U", "U.ID_USU = US.ID_USU")
						.join("PEND_FINAL", "P", "P.ID_PEND = SO.ID_PEND")
						.join("PEND_TIPO", "PT", "PT.ID_PEND_TIPO = P.ID_PEND_TIPO")
						.whereEqual("SO.ID_AUDI_PROC", idAudiProc)
						.andEqual("PT.PEND_TIPO_CODIGO", pendTipoCod);
				 
			
			try {
				if(pendTipoCod.equals("307")) {
					rs1 = this.consultar(sqlDefAuto.build(), psDefAut);
				}
				else {
					rs1 = this.consultar(sql.build(), ps);
				}

				//lrcampos 25/11/2019 11:28 - Corre\E7\E3o para Listar todos os advogados que pediram S.O 

				while (rs1.next()) {
					JulgamentoAdiadoDt julgamento = new JulgamentoAdiadoDt();
					julgamento.setAdvogadoSolicitante(rs1.getString("NOME_ADV"));
					julgamento.setParte(rs1.getString("NOME_PARTE"));
					julgamento.setDataHoraSolicitacao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_SOLICITADO")));
					listaJulgamentoAdiados.add(julgamento);
				}

			} finally {
				try {
					if (rs1 != null)
						rs1.close();
				} catch (Exception e) {
				}
			}
			return listaJulgamentoAdiados;
		}
		private String montaSubquerySo(String idAudiProc) throws Exception {
				StringBuilder subQuery = new StringBuilder();
				PreparedStatementTJGO ps1 = new PreparedStatementTJGO();
				ResultSetTJGO rs1 = null;
				
			 subQuery.append("(SELECT CASE WHEN EXISTS (SELECT PT1.PEND_TIPO_CODIGO FROM SUSTENTACAO_ORAL SO1 ");
			 subQuery.append(" JOIN PEND_FINAL P1 ON P1.ID_PEND = SO1.ID_PEND ");
			 subQuery.append(" JOIN PEND_TIPO PT1 ON PT1.ID_PEND_TIPO = P1.ID_PEND_TIPO ");
			 subQuery.append(" WHERE SO1.ID_AUDI_PROC = ? "); ps1.adicionarString(idAudiProc);
			 subQuery.append(" AND PT1.PEND_TIPO_CODIGO = ?) "); ps1.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
			 subQuery.append(" THEN ? "); ps1.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
			 subQuery.append(" WHEN EXISTS (SELECT PT1.PEND_TIPO_CODIGO FROM SUSTENTACAO_ORAL SO1 ");
			 subQuery.append(" JOIN PEND_FINAL P1 ON P1.ID_PEND = SO1.ID_PEND ");
			 subQuery.append(" JOIN PEND_TIPO PT1 ON PT1.ID_PEND_TIPO = P1.ID_PEND_TIPO ");
			 subQuery.append(" WHERE SO1.ID_AUDI_PROC = ? "); ps1.adicionarString(idAudiProc);
			 subQuery.append(" AND PT1.PEND_TIPO_CODIGO = ?) "); ps1.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SO_DEFERIMENTO_AUTOMATICO);
			 subQuery.append(" THEN ? "); ps1.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SO_DEFERIMENTO_AUTOMATICO);
			 subQuery.append(" ELSE ? "); ps1.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
			 subQuery.append(" END AS PEND_TIPO_CODIGO FROM DUAL)");
			 
			 rs1 = this.consultar(subQuery.toString(), ps1);
			
			 if (rs1.next()) {
				 return rs1.getString("PEND_TIPO_CODIGO");
				}
			 return SUSTENTACAO_ORAL_DEFERIDA;
		}

		//lrcampos 06/08/2019 * Busca a audiencia na qual o processo pertencia antes de ser adiado
		public String buscaAudienciaVirtualOriginalPJD(String idProc, String dataAudienciaOriginal) throws Exception {
			ResultSetTJGO rs1 = null;
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			String sql = new Select("AP.ID_AUDI_PROC", ps)
					.from("AUDI", "A")
					.join("AUDI_PROC", "AP", "AP.ID_AUDI = A.ID_AUDI")
					.whereEqual("AP.ID_PROC", idProc)
					.andEqual("A.DATA_AGENDADA", dataAudienciaOriginal)
					.build();
					
			try {
				rs1 = this.consultar(sql, ps);
				if (rs1.next()) {
					return rs1.getString("ID_AUDI_PROC");
				}

			} finally {
				try {
					if (rs1 != null)
						rs1.close();
				} catch (Exception e) {
				}
			}
			
			return null;
		}
		
	public void definirDadosPedidoSustencaoOral(String idProcesso, JulgamentoAdiadoDt julgamento) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = new Select("P.DATA_INICIO, PP.NOME", ps)
				.from(VIEW_PENDENCIA_FINALIZADA, "P")
				.leftJoin("PROJUDI.PROC_PARTE", "PP", "PP.ID_PROC_PARTE=P.ID_PROC_PARTE")
				.whereEqual("P.ID_PROC", idProcesso)
				.andEqual("P.PEND_TIPO_CODIGO", PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL)
				.order("DATA_INICIO DESC")
				.build();

		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				julgamento.setDataHoraSolicitacao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_INICIO")));
				julgamento.setParte(rs1.getString("NOME"));
			}

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}

	}

	public void definirDadosPedidoSustencaoOral(String idProcesso) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = new Select("P.DATA_INICIO, PP.NOME", ps)
				.from(VIEW_PENDENCIA_FINALIZADA, "P")
				.leftJoin("PROJUDI.PROC_PARTE", "PP", "PP.ID_PROC_PARTE=P.ID_PROC_PARTE")
				.whereEqual("P.ID_PROC", idProcesso)
				.andEqual("P.PEND_TIPO_CODIGO", PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO)
				.order("DATA_INICIO DESC")
				.build();

		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
			}

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}

	}

	public boolean existeSustentacaoOral(String idProcesso) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = new Select("1", ps)
				.from(VIEW_PENDENCIA_FINALIZADA, "P")
				.whereEqual("P.PEND_TIPO_CODIGO", PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL)
				.andEqual("P.ID_PROC", idProcesso) 
				.and()
				.notExists(
						new Select("1", ps)
								.from("PROJUDI.AUDI_PROC", "AP")
								.join("PROJUDI.AUDI", "A", "A.ID_AUDI=AP.ID_AUDI")
								.where()
								.addRaw("AP.ID_PROC=P.ID_PROC")
								.and()
								.addRaw("A.DATA_AGENDADA > P.DATA_INICIO")
								.andNotNull("AP.DATA_MOVI")
								.addRaw("HAVING COUNT(*) > 1")
								.build())
				.and()
				.exists(
						new Select("*", ps).from(
								"(SELECT P2.ID_PROC, P2.PEND_TIPO_CODIGO, P2.DATA_INICIO FROM VIEW_PEND_FINALIZADAS P2"
										+
										"		UNION" +
										"		SELECT P2.ID_PROC, P2.PEND_TIPO_CODIGO, P2.DATA_INICIO FROM VIEW_PEND P2) P2")
								.whereEqual("P2.PEND_TIPO_CODIGO", PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO)
								.and()
								.addRaw(" P2.ID_PROC = P.ID_PROC")
								.and()
								.addRaw("P2.DATA_INICIO > P.DATA_INICIO")
								.build())
				.build();

		try {
			rs1 = this.consultar(sql, ps);

			return rs1.next();

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}

	}
	
	public ArquivoDt consultarArquivoVotoDivergente(String idAudienciaProcesso) throws Exception {ArquivoDt arquivoDt = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM ARQ WHERE ID_ARQ IN ");
		sql.append(" (SELECT ID_ARQ FROM PEND_FINAL_ARQ WHERE ID_PEND IN ");
		sql.append(" (SELECT ID_PEND FROM PEND_VOTO_VIRTUAL WHERE ID_VOTO_VIRTUAL_TIPO = '4' AND ID_AUDI_PROC = ?))");
		ps.adicionarLong(idAudienciaProcesso);
		
		try {
			rs = consultar(sql.toString(), ps);
			if (rs.next()) {
				arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs.getString("NOME_ARQ"));
				arquivoDt.setUsuarioAssinador(rs.getString("USU_ASSINADOR"));
				arquivoDt.setRecibo(Funcoes.FormatarLogico(rs.getString("RECIBO")));
				arquivoDt.setContentType(rs.getString("CONTENT_TYPE"));
				arquivoDt.setArquivo(rs.getBytes("ARQ"));
			}
	   } finally {
	        try{if (rs != null) rs.close();} catch(Exception e1) {}
	   }
	   return arquivoDt;
	}

	//lrcampos 26/06/2019 Verifica se existe sustenta\E7\E3o oral aberta 
	public String consultaIdPendSusOralAberta(String idProcesso, Integer codigoPendenciaTipo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		String idPendSusOral = null;

		sql.append(" SELECT PR.ID_PEND FROM PROJUDI.AUDI_PROC AP ");
		sql.append(" LEFT JOIN PEND P ON P.ID_PROC = AP.ID_PROC  ");
		sql.append(" LEFT JOIN PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO ");
		sql.append(" LEFT JOIN PEND_RESP PR ON PR.ID_PEND = P.ID_PEND ");
		if(codigoPendenciaTipo == PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO) {
			sql.append(" WHERE PR.ID_SERV IS NOT NULL AND AP.ID_PROC = ? AND PT.PEND_TIPO_CODIGO = ? ");
		}
		else {
			sql.append(" WHERE PR.ID_SERV_CARGO IS NOT NULL AND AP.ID_PROC = ? AND PT.PEND_TIPO_CODIGO = ? ");			
		}
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(codigoPendenciaTipo);
		
		sql.append(" AND AP.ID_AUDI_PROC_STATUS = ? "); ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			if (rs1.next()) {
				idPendSusOral = rs1.getString("ID_PEND");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return idPendSusOral;
	}

	// jvosantos - 04/06/2019 10:44 - M\E9todo que consulta o ID do voto de proclama\E7\E3o de um determinado audi_proc
	public String consultarIdProclamacao(String idAudiProc) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Select select = new Select("ID_PEND", ps)
				.from("PROJUDI.VIEW_PEND_VOTO_VIRTUAL")
				.whereEqual("VOTO_VIRTUAL_TIPO_CODIGO", VotoTipoDt.PROCLAMACAO_DECISAO)
				.andEqual("ID_AUDI_PROC", idAudiProc);
		
		ResultSetTJGO rs = null;
		
		try {
			rs = consultar(select.build(), ps);
			if (rs.next()) {
				return rs.getString("ID_PEND");
			}
		} finally {
			if (rs != null)	rs.close();
		}
		return null;
	}

	// jvosantos - 04/06/2019 10:44 - M\E9todo que consulta o ID do status vencido no voto de proclama\E7\E3o de um determinado audi_proc
	public String consultarIdStatusVencidoProclamacao(String idAudiProc) throws Exception {
		String idCodigo = null;
		PreparedStatementTJGO psIdCodigo = new PreparedStatementTJGO();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rsIdCodigo = null;
		ResultSetTJGO rs = null;
		Select selectIdCodigo = new Select("ID_VOTO_VIRTUAL_TIPO", psIdCodigo)
									.from("VOTO_VIRTUAL_TIPO")
									.whereEqual("VOTO_VIRTUAL_TIPO_CODIGO", VotoTipoDt.PROCLAMACAO_DECISAO);
		
		try {
			rsIdCodigo = consultar(selectIdCodigo.build(), psIdCodigo);
			if (rsIdCodigo.next()) {
				idCodigo = rsIdCodigo.getString("ID_VOTO_VIRTUAL_TIPO");
			}
		} finally {
			if (rsIdCodigo != null)	rsIdCodigo.close();
			if(StringUtils.isEmpty(idCodigo)) throw new Exception("Tipo de Voto 'Proclamação da Decisão' não foi encontrado.");
		}
		
		Select select = new Select("ID_AUDI_PROC_STATUS_VENCIDO", ps)
							.from("PROJUDI.PEND_VOTO_VIRTUAL")
							.whereEqual("ID_VOTO_VIRTUAL_TIPO", idCodigo)
							.andEqual("ID_AUDI_PROC", idAudiProc)
							.andEqual("VOTO_ATIVO", 1);
		
		try {
			rs = consultar(select.build(), ps);
			if (rs.next()) {
				return rs.getString("ID_AUDI_PROC_STATUS_VENCIDO");
			}
		} finally {
			if (rs != null)	rs.close();
		}
		return null;
	}

	// jvosantos - 04/06/2019 10:44 - M\E9todo que consulta o ID da pendencia de apreciados de uma audi_proc
	public String consultarIdPendenciaApreciadosPorAudienciaProcesso(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		
		Select select = new Select("*", ps)
							.from("PROJUDI.PEND")
							.whereEqual("CODIGO_TEMP", idAudienciaProcesso)
							.and()
							.conditionQuery("ID_PEND_TIPO", "=", "SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?");
		ps.adicionarLong(PendenciaTipoDt.APRECIADOS);
		
		try {
			rs = consultar(select.build(), ps);
			if (rs.next()) {
				return rs.getString("ID_PEND");
			}
		} finally {
			if (rs != null)	rs.close();
		}
		return null;
	}

	// jvosantos - 04/06/2019 10:44 - M\E9todo que consulta a quantidade de votos a renovar n\E3o analisados
	public int consultarQuantidadeVotosRenovarNaoAnalisadas(String idServentiaCargo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		/*lrcampos 14/10/2019 16:27 - Mudando o sql para ficar igual a listagem da grid de renovar votos n\E3o analisados */
		String sql = "SELECT DISTINCT COUNT(*) Q  "
				+ " FROM PROJUDI.PEND_VOTO_VIRTUAL PV"
				+ " JOIN PROJUDI.PEND P ON PV.ID_PEND = P.ID_PEND"
				+ " JOIN PROJUDI.PEND_RESP PR ON PV.ID_PEND = PR.ID_PEND"
				+ " JOIN PROJUDI.VIEW_AUDI_COMPLETA AC ON PV.ID_AUDI_PROC = AC.ID_AUDI_PROC";
		sql += " WHERE PR.ID_SERV_CARGO = ?";ps.adicionarLong(idServentiaCargo);
		sql += " AND P.CODIGO_TEMP = ? "; ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		sql += " AND ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)";	ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
		sql += " AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)";	ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getInt("Q");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return 0;
	}

	// jvosantos - 04/06/2019 10:44 - M\E9todo que consulta a quantidade de votos a renovar pre analisados
	public int consultarQuantidadeVotosRenovarPreAnalisados(String idServentiaCargo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//lrcampos 14/10/2019 16:29 - Mudando o sql para ficar igual a listagem da grid de renovar votos pre analisados
		String sql = "SELECT DISTINCT COUNT(*) Q  "
				+ " FROM PROJUDI.PEND_VOTO_VIRTUAL PV"
				+ " JOIN PROJUDI.PEND P ON PV.ID_PEND = P.ID_PEND"
				+ " JOIN PROJUDI.PEND_RESP PR ON PV.ID_PEND = PR.ID_PEND"
				+ " JOIN PROJUDI.VIEW_AUDI_COMPLETA AC ON PV.ID_AUDI_PROC = AC.ID_AUDI_PROC";
		sql += " WHERE PR.ID_SERV_CARGO = ?";ps.adicionarLong(idServentiaCargo);
		sql += " AND P.CODIGO_TEMP = ? "; ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);
		sql += " AND ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)";	ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
		sql += " AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)";	ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getInt("Q");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return 0;
	}

	// jvosantos - 04/06/2019 10:44 - M\E9todo que consulta a quantidade de votos a renovar aguardando assinatura
	public int consultarQuantidadeVotosRenovarAguardandoAssinatura(String idServentiaCargo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		//lrcampos 14/10/2019 16:30 - Mudando o sql para ficar igual a listagem da grid de renovar Voto Aguardando Assinatura
		String sql = "SELECT DISTINCT COUNT(*) Q  "
				+ " FROM PROJUDI.PEND_VOTO_VIRTUAL PV"
				+ " JOIN PROJUDI.PEND P ON PV.ID_PEND = P.ID_PEND"
				+ " JOIN PROJUDI.PEND_RESP PR ON PV.ID_PEND = PR.ID_PEND"
				+ " JOIN PROJUDI.VIEW_AUDI_COMPLETA AC ON PV.ID_AUDI_PROC = AC.ID_AUDI_PROC";
		sql += " WHERE PR.ID_SERV_CARGO = ?";ps.adicionarLong(idServentiaCargo);
		sql += " AND P.CODIGO_TEMP = ? "; ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP);
		sql += " AND ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)";	ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
		sql += " AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)";	ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getInt("Q");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return 0;
	}

	//lrcampos 23/03/2020 14:11 - Incluindo filtro por serventia.
	// jvosantos - 04/06/2019 10:44 - M\E9todo que os votos a renovar n\E3o analisados
	public List<VotoSessaoLocalizarDt> consultarVotosRenovarNaoAnalisado(String idServentiaCargo, String processoNumero, int tipoPendencia, String idServentiaFiltro)
			throws Exception {
		return consultarVotosRenovar(
				idServentiaCargo,
				processoNumero,
				PendenciaStatusDt.ID_CORRECAO,
				tipoPendencia,
				PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO,
				idServentiaFiltro);
	}

	//lrcampos 23/03/2020 14:11 - Incluindo filtro por serventia.
	// jvosantos - 04/06/2019 10:44 - M\E9todo que os votos a renovar aguardando assinatura
	public List<VotoSessaoLocalizarDt> consultarVotosRenovarAguardandoAssinatura(String idServentiaCargo, String processoNumero, int tipoPendencia)
			throws Exception {
		return consultarVotosRenovar(
				idServentiaCargo,
				processoNumero,
				PendenciaStatusDt.ID_CORRECAO,
				tipoPendencia,
				PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP,
				null);
	}

	//lrcampos 23/03/2020 14:11 - Incluindo filtro por serventia.
	// jvosantos - 04/06/2019 10:44 - M\E9todo que os votos a renovar pre analisados
	public List<VotoSessaoLocalizarDt> consultarVotosRenovarPreAnalisados(String idServentiaCargo, String processoNumero, int tipoPendencia, String idServentiaFiltro)
			throws Exception {
		return consultarVotosRenovar(
				idServentiaCargo,
				processoNumero,
				PendenciaStatusDt.ID_CORRECAO,
				tipoPendencia,
				PendenciaStatusDt.ID_PRE_ANALISADA,
				idServentiaFiltro);
	}

	// jvosantos - 04/06/2019 10:44 - Metodo que os votos a renovar
	private List<VotoSessaoLocalizarDt> consultarVotosRenovar(String idServentiaCargo,
			String processoNumero,
			int statusPendencia,
			int tipoPendencia, int codigoTemp, String idServentiaFiltro) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		String sql = "SELECT DISTINCT AC.ID_PROC, AC.DATA_AGENDADA, AC.PROC_NUMERO_COMPLETO, PV.ID_PEND, AC.ID_AUDI_PROC, AC.NOME, PT.PROC_TIPO, AC.ID_SERV, SS.SERV ";
		//Obtendo a classe do recurso
		sql += ", (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ";
        sql += "	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "; 
        sql += "	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ";
        sql += " 	WHERE AP1.ID_AUDI_PROC = PV.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "; 
        sql += " 	(SELECT 1 FROM RECURSO R "; 
        sql += "		INNER JOIN AUDI_PROC AP1 ON AP1.ID_PROC = R.ID_PROC "; 
        sql += "		WHERE AP1.ID_AUDI_PROC = PV.ID_AUDI_PROC AND ROWNUM = 1) POSSUI_RECURSO "
				+ " FROM PROJUDI.PEND_VOTO_VIRTUAL PV"
				+ " JOIN PROJUDI.PEND P ON PV.ID_PEND = P.ID_PEND"
				+ " JOIN PROJUDI.PEND_RESP PR ON PV.ID_PEND = PR.ID_PEND"
				+ " JOIN PROJUDI.VIEW_AUDI_COMPLETA AC ON PV.ID_AUDI_PROC = AC.ID_AUDI_PROC"
				+ " JOIN PROJUDI.AUDI_PROC AP ON AP.ID_AUDI_PROC = AC.ID_AUDI_PROC "
				+ " JOIN PROJUDI.PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO "
				+ " JOIN PROJUDI.PROC PROC ON PROC.ID_PROC = AP.ID_PROC "
				+ " INNER JOIN SERV SS ON SS.ID_SERV = AC.ID_SERV ";

		sql += " WHERE PR.ID_SERV_CARGO = ?";
		ps.adicionarLong(idServentiaCargo);
		sql += " AND P.CODIGO_TEMP = ? "; ps.adicionarLong(codigoTemp);
		sql += " AND ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)";
		ps.adicionarLong(statusPendencia);
		sql += " AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(tipoPendencia);
		
		sql = filtroProcessoNumero(sql, processoNumero, ps);

		//lrcampos 23/03/2020 14:11 - Incluindo filtro por serventia.
		if(StringUtils.isNotEmpty(idServentiaFiltro)) {
			sql += " AND AC.ID_SERV = ? ";
			ps.adicionarLong(idServentiaFiltro);
		
		}

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				ProcessoDt processoDt = new ProcessoDt();
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME"));
				PendenciaDt pend = new PendenciaDt();
				voto.setPendenciaDt(pend);
				voto.getPendenciaDt().setCodigoTemp(rs1.getString("ID_SERV")+"@"+rs1.getString("SERV"));
				
				//lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sess\E3o possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				voto.setProcessoDt(processoDt);
				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	// jvosantos - 14/06/2019 11:33 - M\E9todo para adicionar votantes na AUDI_PROC_VOTANTES
	public boolean adicionarVotante(String idServCargo, String idAudienciaProcesso, boolean convocado, FabricaConexao fabrica) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		sql.append(" INSERT ");
		sql.append(" 	INTO ");
		sql.append(" 		AUDI_PROC_VOTANTES (ID_AUDI_PROC, ");
		sql.append(" 		ID_SERV_CARGO, ");
		sql.append(" 		RELATOR, ");
		sql.append(" 		ID_IMPEDIMENTO_TIPO, ");
		sql.append(" 		ORDEM_VOTANTE, ");
		sql.append(" 		ID_VOTANTE_TIPO, ");
		sql.append(" 		CONVOCADO) ");		
		sql.append(" 	VALUES(?, "); ps.adicionarLong(idAudienciaProcesso);
		sql.append(" 	?, "); ps.adicionarLong(idServCargo);
		sql.append(" 	?, "); ps.adicionarBoolean(false);
		sql.append(" 	( ");
		sql.append(" 		SELECT ID_IMPEDIMENTO_TIPO ");
		sql.append(" 	FROM ");
		sql.append(" 		IMPEDIMENTO_TIPO ");
		sql.append(" 	WHERE ");
		sql.append(" 		IMPEDIMENTO_TIPO_CODIGO = ?), "); ps.adicionarLong(ImpedimentoTipoDt.NAO_IMPEDIDO);
		sql.append(" 	( ");
		sql.append(" 	SELECT ");
		sql.append(" 		MAX(ORDEM_VOTANTE)+ 1 AS ORDEM ");
		sql.append(" 	FROM ");
		sql.append(" 		AUDI_PROC_VOTANTES ");
		sql.append(" 	WHERE ");
		sql.append(" 		ID_AUDI_PROC = ? "); ps.adicionarLong(idAudienciaProcesso);
		sql.append(" 		AND ID_VOTANTE_TIPO = ( ");
		sql.append(" 		SELECT ");
		sql.append(" 			ID_VOTANTE_TIPO ");
		sql.append(" 		FROM ");
		sql.append(" 			VOTANTE_TIPO ");
		sql.append(" 		WHERE ");
		sql.append(" 			VOTANTE_TIPO_CODIGO = ? )), "); ps.adicionarLong(VotanteTipoDt.VOTANTE);
		sql.append(" 	( ");
		sql.append(" 	SELECT ");
		sql.append(" 		ID_VOTANTE_TIPO ");
		sql.append(" 	FROM ");
		sql.append(" 		VOTANTE_TIPO ");
		sql.append(" 	WHERE ");
		sql.append(" 		VOTANTE_TIPO_CODIGO = ? ), "); ps.adicionarLong(VotanteTipoDt.VOTANTE);
		sql.append("	?) "); ps.adicionarBoolean(convocado);
		

		return StringUtils.isNotEmpty(executarInsert(sql.toString(), "ID_AUDI_PROC_VOTANTES", ps));		
	}
	//lrcampos 10/07/2019 consulta MP e presidente da sess\E3o
	public AudienciaProcessoVotantesDt consultarMPPresidenteSessao(AudienciaProcessoDt audiProcDt, Integer tipoVotante )
			throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		AudienciaProcessoVotantesDt votante = new AudienciaProcessoVotantesDt();
		String sql = "SELECT * FROM PROJUDI.AUDI_PROC_VOTANTES PV"
				+ " JOIN PROJUDI.VOTANTE_TIPO VT ON VT.ID_VOTANTE_TIPO = PV.ID_VOTANTE_TIPO "
				+ " JOIN PROJUDI.IMPEDIMENTO_TIPO IT ON IT.ID_IMPEDIMENTO_TIPO = PV.ID_IMPEDIMENTO_TIPO"
				+ " JOIN PROJUDI.VIEW_SERV_CARGO VT ON VT.ID_SERV_CARGO = PV.ID_SERV_CARGO "
				+ " JOIN PROJUDI.AUDI_PROC AP ON AP.ID_AUDI_PROC = PV.ID_AUDI_PROC "
				+ " WHERE AP.ID_AUDI = ? ";
		ps.adicionarLong(audiProcDt.getId_Audiencia()); 
			sql += " AND VT.VOTANTE_TIPO_CODIGO = ? "; 	ps.adicionarLong(tipoVotante);
		try {
			List<AudienciaProcessoVotantesDt> list = new ArrayList<>();
			rs = consultar(sql, ps);
			if (rs.next()) {

				if(tipoVotante == VotanteTipoDt.MINISTERIO_PUBLICO) {
					votante.setId_ServentiaCargo(rs.getString("ID_SERV_CARGO_MP"));
				}else {
					votante.setId_ServentiaCargo(rs.getString("ID_SERV_CARGO_PRESIDENTE"));
				}
				
				votante.setRelator(rs.getInt("RELATOR") == 1 ? true : false);
				votante.setId_ImpedimentoTipo(rs.getString("ID_IMPEDIMENTO_TIPO"));
				votante.setOrdemVotante(rs.getString("ORDEM_VOTANTE"));
				votante.setId_VotanteTipo(rs.getString("ID_VOTANTE_TIPO"));
				votante.setImpedimentoTipoCodigo(rs.getString("IMPEDIMENTO_TIPO_CODIGO"));
				votante.setVotanteTipoCodigo(rs.getString("VOTANTE_TIPO_CODIGO"));
				
			}
			return votante;
		} finally {
			if (rs != null)
				rs.close();
		}
	}

	// jvosantos - 08/08/2019 11:58 - Adicionar m\E9todo que consulta as condi\E7\F5es para mostrar a op\E7\E3o de incluir Extrato de Ata
	public boolean consultarCondicaoExtratoAta(AudienciaProcessoDt audienciaProcessoDt) throws Exception{
		ResultSetTJGO rs = null;
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		PreparedStatementTJGO psFinal = new PreparedStatementTJGO();
		StringBuilder sqlFinal = new StringBuilder();

		sql.append(" SELECT COUNT(*) COUNT FROM PEND WHERE ID_PROC = ? "); ps.adicionarLong(audienciaProcessoDt.getId_Processo());
		sql.append(" AND ID_PEND_TIPO IN (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO IN (?)) "); ps.adicionarLong(PendenciaTipoDt.RESULTADO_VOTACAO);
		
		sqlFinal.append(" SELECT COUNT(*) COUNT FROM PEND_FINAL WHERE ID_PROC = ? "); psFinal.adicionarLong(audienciaProcessoDt.getId_Processo());
		sqlFinal.append(" AND ID_PEND_TIPO IN (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO IN (?)) "); psFinal.adicionarLong(PendenciaTipoDt.RESULTADO_VOTACAO);
		
		try {
			// Consulta na PEND
			rs = consultar(sql.toString(), ps);
			if (rs.next() && rs.getLong("COUNT") > 0)
				return true;
			
			// Se n\E3o encontrar nenhuma na PEND, consulta na PEND_FINAL
			rs = consultar(sqlFinal.toString(), ps);
			if (rs.next() && rs.getLong("COUNT") > 0)
				return true;
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		
		return false;
	}
	
	//lrcmapos 03/07/2019 * Criado para buscar as condicoes para mostrar funcionalides na tela de sess\E3o
	//alsqueiroz 09/09/2019 * Mudan\E7a para melhorar a performance da consulta
		public HashMap<String, ArrayList<Boolean>> consultaCondicoesTelaSessao(AudienciaDt audienciaDt, UsuarioDt usuarioDt) throws Exception {
		
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		Boolean possuiDeferidoRelator = null;
		Boolean possuiExtratoAta = null;
		Boolean possuiVerificarSO = null;
		Boolean possuiIndeferidaRelator = null;
		Boolean possuiDeferimentoAutomatico = null;
		HashMap<String, ArrayList<Boolean>> condicoesTelaSessao = new HashMap<String, ArrayList<Boolean>>();
		ArrayList<Boolean> condicoes = null;
		sql.append(" SELECT TAB.ID_AUDI_PROC, ");
		sql.append(" CASE WHEN TAB.EXTRATO_ATA IS NULL THEN 'false' ELSE tab.EXTRATO_ATA END AS EXTRATO_ATA, ");
		sql.append(" CASE WHEN TAB.DEFERIDO IS NULL THEN 'false' ELSE tab.DEFERIDO END AS deferido, ");
		sql.append(" CASE WHEN TAB.INDEFERIDA IS NULL THEN 'false' ELSE tab.INDEFERIDA END AS indeferido, ");
		sql.append(" CASE WHEN TAB.SUSTENTACAO_ABERTA IS NULL THEN 'false' ELSE tab.SUSTENTACAO_ABERTA END AS SUSTENTACAO_ABERTA, ");
		sql.append(" CASE WHEN TAB.DEFERIDO_AUTOMATICO IS NULL THEN 'false' ELSE TAB.DEFERIDO_AUTOMATICO END AS DEFERIDO_AUTOMATICO ");
		sql.append(" FROM ");
		sql.append(" 	( SELECT DISTINCT AP.ID_AUDI_PROC, ");
		sql.append("		(SELECT DISTINCT CASE WHEN PT2.PEND_TIPO_CODIGO = ? THEN 'true' ELSE 'false' END AS INDEFERIDO " ); ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
		sql.append("			FROM audi a2 ");
		sql.append("			INNER JOIN AUDI_PROC ap2 ON ap2.ID_AUDI = a2.ID_AUDI ");
		sql.append("			INNER JOIN PEND P2 ON P2.ID_PROC = AP2.ID_PROC "); 
		sql.append("  			INNER JOIN PEND_TIPO PT2 ON PT2.ID_PEND_TIPO = P2.ID_PEND_TIPO "); 
		sql.append(" 			INNER JOIN PEND_RESP PR2 ON	PR2.ID_PEND = P2.ID_PEND ");
		sql.append("			INNER JOIN SUSTENTACAO_ORAL SO ON SO.ID_AUDI_PROC = AP2.ID_AUDI_PROC"); //lrcampos 27/12/2019 13:55 Incluindo inner join com a tabela de sustenta\E7\E3o oral.
		sql.append(" 			WHERE AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 				AND PT2.PEND_TIPO_CODIGO = ? "); ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
		sql.append(" 				AND PR2.ID_SERV IS NOT NULL ");
		sql.append(" 				AND P2.ID_PROC = P.ID_PROC ) DEFERIDO, ");
		//lrcampos 12/07/2019 * Busca se existe pedido de sustentacao oral indeferido
		sql.append("		(SELECT DISTINCT CASE WHEN PT2.PEND_TIPO_CODIGO = ? THEN 'true' ELSE 'false' END AS DEFERIDO " ); ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA);
		sql.append("			FROM audi a2 ");
		sql.append("			INNER JOIN AUDI_PROC ap2 ON ap2.ID_AUDI = a2.ID_AUDI ");
		sql.append("			INNER JOIN PEND P2 ON P2.ID_PROC = AP2.ID_PROC "); 
		sql.append("  			INNER JOIN PEND_TIPO PT2 ON PT2.ID_PEND_TIPO = P2.ID_PEND_TIPO "); 
		sql.append(" 			INNER JOIN PEND_RESP PR2 ON	PR2.ID_PEND = P2.ID_PEND ");
		sql.append("			INNER JOIN SUSTENTACAO_ORAL SO ON SO.ID_AUDI_PROC = AP2.ID_AUDI_PROC"); //lrcampos 27/12/2019 13:55 Incluindo inner join com a tabela de sustenta\E7\E3o oral.
		sql.append(" 			WHERE AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 				AND PT2.PEND_TIPO_CODIGO = ? "); ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA);
		sql.append(" 				AND PR2.ID_SERV IS NOT NULL ");
		sql.append(" 				AND P2.ID_PROC = P.ID_PROC ) INDEFERIDA, ");
		
			if(isServentiaEspecial(usuarioDt.getId_Serventia())) {
				sql.append(" 'true' AS EXTRATO_ATA, ");
			}else {
				
				sql.append("	( SELECT DISTINCT 'true' FROM AUDI_PROC AP2"); 
				sql.append("		WHERE AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC");
				sql.append("		AND (");
				sql.append("				AP2.ID_AUDI_PROC IN (");
				sql.append("					SELECT");
				sql.append("						DISTINCT AP2.ID_AUDI_PROC");
				sql.append("					FROM AUDI_PROC_PEND APP");
				sql.append("					INNER JOIN PEND P2 ON P2.ID_PEND = APP.ID_PEND");
				sql.append("					INNER JOIN PEND_TIPO PT2 ON PT2.ID_PEND_TIPO = P2.ID_PEND_TIPO");
				sql.append("					WHERE APP.ID_AUDI_PROC = AP.ID_AUDI_PROC");
				sql.append("					AND PT2.PEND_TIPO_CODIGO IN (214)");
				sql.append("				) OR AP2.ID_AUDI_PROC IN (");
				sql.append("					SELECT");
				sql.append("						DISTINCT AP2.ID_AUDI_PROC");
				sql.append("					FROM AUDI_PROC_PEND APP");
				sql.append("					INNER JOIN PEND P2 ON P2.ID_PEND = APP.ID_PEND");
				sql.append("					INNER JOIN PEND_TIPO PT2 ON PT2.ID_PEND_TIPO = P2.ID_PEND_TIPO");
				sql.append("					WHERE APP.ID_AUDI_PROC = AP.ID_AUDI_PROC");
				sql.append("					AND PT2.PEND_TIPO_CODIGO IN (214)");
				sql.append("				)");
				sql.append("		)");
				sql.append(" ) EXTRATO_ATA, ");
			}
		sql.append("		(SELECT DISTINCT CASE WHEN PT3.PEND_TIPO_CODIGO IN (?) THEN 'true' ELSE 'false' END AS SUSTENTACAO_ABERTA " ); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
		sql.append("			FROM audi a3 ");
		sql.append("			INNER JOIN AUDI_PROC ap3 ON ap3.ID_AUDI = a3.ID_AUDI ");
		sql.append("			INNER JOIN PEND P3 ON P3.ID_PROC = AP3.ID_PROC "); 
		sql.append("  			INNER JOIN PEND_TIPO PT3 ON PT3.ID_PEND_TIPO = P3.ID_PEND_TIPO "); 
		sql.append("			INNER JOIN SUSTENTACAO_ORAL SO ON SO.ID_AUDI_PROC = AP3.ID_AUDI_PROC");	//lrcampos 27/12/2019 13:55 Incluindo inner join com a tabela de sustenta\E7\E3o oral.
		sql.append(" 			WHERE AP3.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 			AND PT3.PEND_TIPO_CODIGO IN (?) ");
								ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
		sql.append(" 			AND P3.ID_PROC = P.ID_PROC ) SUSTENTACAO_ABERTA, ");

		
		sql.append("	( SELECT DISTINCT 'true' FROM AUDI_PROC AP4"); 
		sql.append("		WHERE AP4.ID_AUDI_PROC = AP.ID_AUDI_PROC");
		sql.append("		AND (");
		sql.append("				AP4.ID_AUDI_PROC IN (");
		sql.append("					SELECT");
		sql.append("						DISTINCT AP4.ID_AUDI_PROC");
		sql.append("					FROM AUDI_PROC_PEND APP");
		sql.append("					INNER JOIN PEND P4 ON P4.ID_PEND = APP.ID_PEND");
		sql.append("					INNER JOIN PEND_TIPO PT4 ON PT4.ID_PEND_TIPO = P4.ID_PEND_TIPO");
		sql.append("					WHERE APP.ID_AUDI_PROC = AP.ID_AUDI_PROC");
		sql.append("					AND PT4.PEND_TIPO_CODIGO IN (307)");
		sql.append("				) OR AP4.ID_AUDI_PROC IN (");
		sql.append("					SELECT");
		sql.append("						DISTINCT AP4.ID_AUDI_PROC");
		sql.append("					FROM AUDI_PROC_PEND APP");
		sql.append("					INNER JOIN PEND P4 ON P4.ID_PEND = APP.ID_PEND");
		sql.append("					INNER JOIN PEND_TIPO PT4 ON PT4.ID_PEND_TIPO = P4.ID_PEND_TIPO");
		sql.append("					WHERE APP.ID_AUDI_PROC = AP.ID_AUDI_PROC");
		sql.append("					AND PT4.PEND_TIPO_CODIGO IN (307)");
		sql.append("				)");
		sql.append("		)");
		sql.append(" ) DEFERIDO_AUTOMATICO ");
		
		sql.append(" 		FROM audi a "); 
		sql.append(" 		INNER JOIN AUDI_PROC ap ON ap.ID_AUDI = a.ID_AUDI ");
		sql.append(" 		INNER JOIN PEND P ON P.ID_PROC = AP.ID_PROC " );
		sql.append("		INNER JOIN PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO ");
		sql.append(" 		WHERE A.ID_AUDI = ? AND A.VIRTUAL = 1 AND AP.ID_AUDI_PROC_STATUS = ? )TAB"); 
		ps.adicionarLong(audienciaDt.getId());
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		
		
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				condicoes = new ArrayList<Boolean>();
				possuiExtratoAta = Boolean.valueOf(rs.getString("EXTRATO_ATA"));
				possuiDeferidoRelator = Boolean.valueOf(rs.getString("DEFERIDO"));
				possuiVerificarSO = Boolean.valueOf(rs.getString("SUSTENTACAO_ABERTA"));
				possuiIndeferidaRelator = Boolean.valueOf(rs.getString("INDEFERIDO"));
				possuiDeferimentoAutomatico = Boolean.valueOf(rs.getString("DEFERIDO_AUTOMATICO"));
				condicoes.add(possuiExtratoAta);
				condicoes.add(possuiDeferidoRelator);
				condicoes.add(possuiVerificarSO);
				condicoes.add(possuiIndeferidaRelator);
				condicoes.add(possuiDeferimentoAutomatico);
				condicoesTelaSessao.put(rs.getString("ID_AUDI_PROC"), condicoes);
			}
	   } finally {
	        try{if (rs != null) rs.close();} catch(Exception e1) {}
	   }
		
	   return condicoesTelaSessao;
		
	}
		
	// lrcampos 15/08/2019 * Verifica se a serventia \E9 de processos especiais.
	public boolean isServentiaEspecial(String idServentia) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		Boolean possuiServentiaEspecial = false;
		sql.append("SELECT * FROM PROJUDI.SERV WHERE ? IN (?, ?, ?, ?, ?)");
		ps.adicionarString(idServentia);
		ps.adicionarLong(ServentiaDt.SERV_PRIMEIRA_SECAO_CIVEL);
		ps.adicionarLong(ServentiaDt.SERV_SEGUNDA_SECAO_CIVEL);
		ps.adicionarLong(ServentiaDt.SERV_ORGAO_ESPECIAL);
		ps.adicionarLong(ServentiaDt.SERV_CONSELHO_SUPERIOR_MAGISTRATURA);
		ps.adicionarLong(ServentiaDt.SERV_SESSAO_CRIMINAL);

		try {
			rs = consultar(sql.toString(), ps);
			if (rs.next()) {
				possuiServentiaEspecial = true;
				}
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e1) {
			}
		}

		return possuiServentiaEspecial;

	}

	// jvosantos - 11/07/2019 18:15 - Adicionar pend\EAncia de "Verificar Resultado da Vota\E7\E3o"
	@Deprecated
	public List<VotoSessaoLocalizarDt> consultarVerificarResultadoVotacao(String idServentiaCargo,
			String processoNumero, int status) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder("SELECT AP.DATA_AGENDADA, AP.ID_AUDI_PROC, AP.ID_PROC, AP.PROC_NUMERO, PV.ID_PEND ");
		
		sql.append(" FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA AP");
		sql.append(" JOIN PROJUDI.VIEW_PEND PV ON (PV.ID_PROC = AP.ID_PROC AND PV.PEND_TIPO_CODIGO = ? AND PEND_STATUS_CODIGO = ?)");
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESULTADO_VOTACAO);
		ps.adicionarLong(status);

		sql.append(" AND AP.ID_SERV_CARGO = ?");
		ps.adicionarLong(idServentiaCargo);
		
		sql.append(" AND AP.AUDI_PROC_STATUS_CODIGO = ? ");
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		
		if (StringUtils.isNotEmpty(processoNumero)) {
			sql.append(" AND AP.PROC_NUMERO = ? ");
			ps.adicionarString(processoNumero);
		}

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				VotoSessaoLocalizarDt audiencia = new VotoSessaoLocalizarDt();
				audiencia.setDataSessao(Funcoes.FormatarDataHora(rs1.getString("DATA_AGENDADA")));
				audiencia.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				audiencia.setIdProcesso(rs1.getString("ID_PROC"));
				audiencia.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				audiencia.setIdPendencia(rs1.getString("ID_PEND"));
				lista.add(audiencia);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	// lrcampos - 11/10/2019 12:23 - M\E9todo para verificar se existem varias audi_procs
	public Boolean possuiVariasAudiProc(String idProcesso) throws Exception {

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		StringBuilder sql = new StringBuilder("SELECT count(*) QTD FROM AUDI_PROC WHERE ID_PROC = ? AND ID_AUDI_PROC_STATUS = 1 ");
		ps.adicionarString(idProcesso);

		ResultSetTJGO rs1 = null;

		try {
			rs1 = this.consultar(sql.toString(), ps);

			if (rs1.next()) {
				if(rs1.getInt("QTD") > 1)
					return true;
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return false;

	}
	
	public boolean verificarSeHouveConvocacao(String idAudiProc) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		
		StringBuilder sql = new StringBuilder("SELECT COUNT(1) QNT FROM AUDI_PROC_VOTANTES WHERE ID_AUDI_PROC = ? AND CONVOCADO = ?");
		
		ps.adicionarLong(idAudiProc);
		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			if (rs1.next()) {
				return rs1.getLong("QNT") > 0;
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return false;
	}

	// jvosantos - 02/09/2019 12:02 - Criar m\E9todo para verificar se a quantidade de votos bate com a quantidade de votantes
	public boolean verificarQuantidadeVotos(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(1) AS QNT FROM PEND_VOTO_VIRTUAL WHERE ID_AUDI_PROC = ? UNION ALL SELECT COUNT(1) AS QNT FROM AUDI_PROC_VOTANTES WHERE ID_VOTANTE_TIPO = (SELECT ID_VOTANTE_TIPO FROM VOTANTE_TIPO WHERE VOTANTE_TIPO_CODIGO = ?) AND ID_AUDI_PROC = ?";
		
		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(VotanteTipoDt.VOTANTE);
		ps.adicionarLong(idAudienciaProcesso);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			
			long votosRealizados = -1;	// jvosantos - 18/09/2019 14:04 - Renomear vari\E1vel
			long votantesCadastrados = -1; // jvosantos - 18/09/2019 14:04 - Renomear vari\E1vel
			
			if (rs1.next())	votosRealizados = rs1.getLong("QNT"); // jvosantos - 18/09/2019 14:04 - Renomear vari\E1vel
			if (rs1.next())	votantesCadastrados = rs1.getLong("QNT"); // jvosantos - 18/09/2019 14:04 - Renomear vari\E1vel
			
			return votosRealizados >= votantesCadastrados; // jvosantos - 18/09/2019 14:04 - Corre\E7\E3o de erro, onde o n\FAmero de votos \E9 maior que de votantes (devido ao vota de proclama\E7\E3o da decis\E3o do relator
		} finally {
			if (rs1 != null) rs1.close();
		}
	}

	
	// jsantonelli - 07/11/2019 - metodo para consultar nome relator pelo id audi proc
	public String consultarNomeRelator(String idAudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT SC.NOME_USU FROM AUDI_PROC_VOTANTES APV JOIN VIEW_SERV_CARGO SC ON SC.ID_SERV_CARGO=APV.ID_SERV_CARGO WHERE ID_AUDI_PROC = ? AND APV.ID_VOTANTE_TIPO = ?";
		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(1);
		ResultSetTJGO rs1 = null;
		String retorno = "";
		try {
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				retorno =  rs1.getString("NOME_USU");
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return retorno;
	}
	
	public boolean existeVotoDeServCargo(String idAudienciaProcesso, String idServCargo, int[] votosTipos) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder(" SELECT * FROM PEND_VOTO_VIRTUAL PVV ");
		sql.append(" INNER JOIN PEND_FINAL_RESP PFR ON PFR.ID_PEND = PVV.ID_PEND ");
		
		sql.append(" WHERE PVV.ID_AUDI_PROC = ? "); ps.adicionarLong(idAudienciaProcesso);
		sql.append(" AND PFR.ID_SERV_CARGO = ? "); ps.adicionarLong(idServCargo);
		
		if(!ArrayUtils.isEmpty(votosTipos)) {		
			sql.append(" AND PVV.ID_VOTO_VIRTUAL_TIPO IN (SELECT ID_VOTO_VIRTUAL_TIPO FROM VOTO_VIRTUAL_TIPO WHERE VOTO_VIRTUAL_TIPO_CODIGO IN (");
			
			sql.append(StringUtils.repeat("?,", votosTipos.length));
	
			sql.deleteCharAt(sql.length() - 1); // Remover \FAltima ','
			sql.append("))");
			
			for(int votoTipo : votosTipos) {
				ps.adicionarLong(votoTipo);
			}
		}
		
		try {
			rs1 = this.consultar(sql.toString(), ps);
			if (rs1.next()) return true;
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return false;
	}

	// jvosantos - 06/01/2020 11:36 - Método para desativar todos os votos de uma AUDI_PROC
	public void desativarTodosVotos(String idAudiProc) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "UPDATE PEND_VOTO_VIRTUAL SET VOTO_ATIVO = 0 WHERE ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudiProc);
		
		executarUpdateDelete(sql, ps);
	}

	private void atualizarAtivoVoto(String idVoto, boolean ativo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "UPDATE PEND_VOTO_VIRTUAL SET VOTO_ATIVO = ? WHERE ID_PEND_VOTO_VIRTUAL = ?";
		ps.adicionarLong(ativo ? 1 : 0);
		ps.adicionarLong(idVoto);
		
		executarUpdateDelete(sql, ps);		
	}

	public void desativarVoto(String idVoto) throws Exception {
		atualizarAtivoVoto(idVoto, false);	
	}
	
	public void ativarVoto(String idVoto) throws Exception {
		atualizarAtivoVoto(idVoto, true);	
	}

	// jvosantos - 06/01/2020 17:55 - Método para retornar se existe algum voto desativado para aquela AUDI_PROC
	public boolean existeVotoDesativado(String idAudienciaProcesso, int[] tipos) throws Exception {
		return existeVoto(idAudienciaProcesso, tipos, true);
	}
	
	// jvosantos - 02/03/2020 16:55 - Método para retornar se existe algum voto para aquela AUDI_PROC
	public boolean existeVoto(String idAudienciaProcesso, int[] tipos) throws Exception {
		return existeVoto(idAudienciaProcesso, tipos, false);
	}
	
	// jvosantos - 02/03/2020 16:55 - Método para retornar se existe algum voto para aquela AUDI_PROC
	public boolean existeVoto(String idAudienciaProcesso, int[] tipos, boolean somenteDesativado) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder(" SELECT * FROM PEND_VOTO_VIRTUAL PVV ");
		sql.append(" JOIN VOTO_VIRTUAL_TIPO VVT ON VVT.ID_VOTO_VIRTUAL_TIPO = PVV.ID_VOTO_VIRTUAL_TIPO");
		
		sql.append(" WHERE PVV.ID_AUDI_PROC = ? "); ps.adicionarLong(idAudienciaProcesso);
		
		if(somenteDesativado) {
			sql.append(" AND PVV.VOTO_ATIVO = 0");
		}
		
		sql.append(" AND VVT.VOTO_VIRTUAL_TIPO_CODIGO IN (");
		sql.append(StringUtils.repeat("?,", tipos.length));
		sql.deleteCharAt(sql.length()-1);
		sql.append(" ) ");
		
		for(int tipo: tipos) {
			ps.adicionarLong(tipo);
		}
		
		try {
			rs1 = this.consultar(sql.toString(), ps);
			if (rs1.next()) return true;
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return false;
	}
	
	// jvosantos - 02/03/2020 16:55 - Método para retornar se existe algum voto do SERV_CARGO para aquela AUDI_PROC
	public boolean existeVotoDeServCargo(String idAudienciaProcesso, String idServCargo) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder(" SELECT * FROM PEND_VOTO_VIRTUAL PVV ");
		sql.append(" INNER JOIN PEND_FINAL_RESP PFR ON PFR.ID_PEND = PVV.ID_PEND ");
		
		sql.append(" WHERE PVV.ID_AUDI_PROC = ? "); ps.adicionarLong(idAudienciaProcesso);
		sql.append(" AND PFR.ID_SERV_CARGO = ? "); ps.adicionarLong(idServCargo);
		
		try {
			rs1 = this.consultar(sql.toString(), ps);
			if (rs1.next()) return true;
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return false;
	}

	// jvosantos - 15/01/2020 12:14 - Método para consultar as pendências de erro material
	public List<VotoSessaoLocalizarDt> consultarErroMaterial(String idServentiaCargo, boolean preAnalisado,
			boolean aguardandoAssinatura, String[] processoNumeroEDigito, UsuarioNe usuario, FabricaConexao fabricaConexao) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<VotoSessaoLocalizarDt> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder("SELECT DISTINCT AC.ID_PROC, AC.DATA_AGENDADA, AC.PROC_NUMERO_COMPLETO, P.ID_PEND, AC.ID_AUDI_PROC, AC.NOME, PT.PROC_TIPO,  "); // jvosantos - 20/08/2019 15:05 - Usar StringBuilder e adicionar INNER JOIN com AUDI_PROC_PEND
		sql.append(" (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ");
	    sql.append("	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "); 
	    sql.append("	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ");
	    sql.append(" 	WHERE AP1.ID_AUDI_PROC = APP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "); 
	    sql.append(" 	(SELECT 1 FROM RECURSO R "); 
	    sql.append("		INNER JOIN AUDI_PROC AP2 ON AP2.ID_PROC = R.ID_PROC "); 
	    sql.append("		WHERE AP2.ID_AUDI_PROC = APP.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO ");
		sql.append(" FROM PROJUDI.PEND P ");
		sql.append(" JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ");
		sql.append(" INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND ");
		sql.append(" INNER JOIN PROJUDI.PROC PP ON PP.ID_PROC = P.ID_PROC ");
		sql.append(" JOIN PROJUDI.AUDI_PROC AP ON AP.ID_AUDI_PROC = APP.ID_AUDI_PROC ");
		sql.append(" INNER JOIN PROJUDI.PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" INNER JOIN PROJUDI.VIEW_AUDI_COMPLETA AC ON APP.ID_AUDI_PROC = AC.ID_AUDI_PROC ");
		sql.append(" WHERE PR.ID_SERV_CARGO = ? ");
		ps.adicionarLong(idServentiaCargo);
		sql.append(" AND ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)");
		ps.adicionarLong(preAnalisado || aguardandoAssinatura ? PendenciaStatusDt.ID_PRE_ANALISADA : PendenciaStatusDt.ID_EM_ANDAMENTO);
		sql.append(" AND ID_PEND_TIPO = (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)");
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL);
		
		sql.append(" AND P.CODIGO_TEMP ");
		if(aguardandoAssinatura) {
			sql.append(" = ? ");
			ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP);
		}else {
			sql.append(" IS NULL ");
		}

		if (processoNumeroEDigito != null) {
			sql.append(" AND PP.PROC_NUMERO = ? AND PP.DIGITO_VERIFICADOR = ? ");
			ps.adicionarLong(processoNumeroEDigito[0]);
			ps.adicionarLong(processoNumeroEDigito[1]);
		}

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);

			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				voto.setHashUsuario(usuario.getCodigoHash(rs1.getString("ID_PEND")));
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				voto.setNomeRelator(rs1.getString("NOME"));
				
				ProcessoDt processoDt = new ProcessoDt();
				
				//lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sessão possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				voto.setProcessoDt(processoDt);
				
				
				lista.add(voto);
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}
		return lista;
	}

	// jvosantos - 24/01/2020 18:23 - Método para desativar todos os votos em uma AUDI_PROC de uma lista de votantes
	public void desativarTodosVotosIdServentiaCargo(String idAudiProc, List<String> idServentiaCargo) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "UPDATE PEND_VOTO_VIRTUAL SET VOTO_ATIVO = 0 WHERE ID_AUDI_PROC = ? AND ID_PEND_VOTO_VIRTUAL IN (SELECT ID_PEND_VOTO_VIRTUAL FROM PEND_VOTO_VIRTUAL PVV INNER JOIN PEND_FINAL_RESP PR ON PVV.ID_PEND = PR.ID_PEND WHERE PVV.ID_AUDI_PROC = ? AND PR.ID_SERV_CARGO IN (";
		
		String repeat = StringUtils.repeat("?,", idServentiaCargo.size());
		repeat = repeat.substring(0, repeat.length() - 1);
		
		sql += repeat + "))";
		
		ps.adicionarLong(idAudiProc);
		ps.adicionarLong(idAudiProc);
		
		for(String id : idServentiaCargo)
			ps.adicionarLong(id);
		
		executarUpdateDelete(sql, ps);
	}

}
