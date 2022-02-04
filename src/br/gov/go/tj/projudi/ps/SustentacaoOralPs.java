package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.SustentacaoOralDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoParteAdvogadoNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class SustentacaoOralPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8655351812316252660L;

	public SustentacaoOralPs(Connection conexao) {
		Conexao = conexao;
	}

	//lrcampos 13/09/2019 Insere o vinculo entre AudiProc e Pend de sustentação oral
	public void inserir(SustentacaoOralDt dados) throws Exception {

		StringBuilder sql = new StringBuilder();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" INSERT INTO PROJUDI.SUSTENTACAO_ORAL  ");
		sql.append(" ( ID_AUDI_PROC, ID_PROC_PARTE_ADVOGADO, ID_USU_SERV, ID_PEND) ");
		sql.append("	VALUES ");
		sql.append(" (?, ?, ?, ? )");
		ps.adicionarLong(dados.getAudienciaProcessoDt().getId());
		ps.adicionarLong(dados.getProcessoParteAdvogadoDt().getId());
		ps.adicionarLong(dados.getUsuarioServentiaDt().getId());
		ps.adicionarLong(dados.getPendenciaDt().getId());

		dados.setId(executarInsert(sql.toString(), "ID_SUSTENTACAO_ORAL", ps));
	}

	//lrcampos 13/09/2019 consulta Sustencao oral pelo id da pendencia
	public SustentacaoOralDt consultarSustentacaoOralIdPend(String idPendencia) throws Exception {
		StringBuilder sql = new StringBuilder();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;

		sql.append(" SELECT * FROM SUSTENTACAO_ORAL SO ");
		sql.append(" WHERE SO.ID_PEND = ? ");
		ps.adicionarString(idPendencia);

		try {
			SustentacaoOralDt sustentacaoOralDt = new SustentacaoOralDt();
			rs = consultar(sql.toString(), ps);
			if (rs.next()) {
				sustentacaoOralDt.setId(rs.getString("ID_SUSTENTACAO_ORAL"));
				sustentacaoOralDt
						.setAudienciaProcessoDt(new AudienciaProcessoNe().consultarId(rs.getString("ID_AUDI_PROC")));
				sustentacaoOralDt.setProcessoParteAdvogadoDt(
						new ProcessoParteAdvogadoNe().consultarId(rs.getString("ID_PROC_PARTE_ADVOGADO")));
				sustentacaoOralDt
						.setUsuarioServentiaDt(new UsuarioServentiaNe().consultarId(rs.getString("ID_USU_SERV")));
				sustentacaoOralDt.setPendenciaDt(new PendenciaNe().consultarId(rs.getString("ID_PEND")));
			}
			return sustentacaoOralDt;
		} finally {
			if (rs != null)
				rs.close();
		}
	}
	
	//mrbatista 24/11/2020 consulta Sustencao oral pelo id da audiência processo
	public List<SustentacaoOralDt> consultarSustentacaoOralIdAudiProc(String idAudiProc) throws Exception {
		StringBuilder sql = new StringBuilder();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		SustentacaoOralDt susOralDt;
		List<SustentacaoOralDt> listaPedidosSustentacaoOral = new ArrayList<SustentacaoOralDt>();
		
		sql.append(" SELECT * FROM SUSTENTACAO_ORAL SO ");
		sql.append(" WHERE SO.ID_AUDI_PROC = ? ");
		ps.adicionarString(idAudiProc);
	
		try {
			rs = consultar(sql.toString(), ps);
	
			while (rs.next()) {
				susOralDt = new SustentacaoOralDt();
				susOralDt.setId(rs.getString("ID_SUSTENTACAO_ORAL"));
				susOralDt.setAudienciaProcessoDt(new AudienciaProcessoNe().consultarId(idAudiProc));
				susOralDt.setProcessoParteAdvogadoDt(new ProcessoParteAdvogadoNe().consultarId(rs.getString("ID_PROC_PARTE_ADVOGADO")));
				susOralDt.setUsuarioServentiaDt(new UsuarioServentiaNe().consultarId(rs.getString("ID_USU_SERV")));
				susOralDt.setPendenciaDt(new PendenciaNe().consultarId(rs.getString("ID_PEND")));
	
				listaPedidosSustentacaoOral.add(susOralDt);
			}
	
		}  finally {
			try {
				if (rs != null)
					rs.close();
			} catch (Exception e) {
			}
		}
	
		return listaPedidosSustentacaoOral;
	}

	//lrcampos 13/09/2019 consulta Sustencao oral passando o idProcParte Advogado
	public SustentacaoOralDt consultar(String idProcessoParteAdvogado, UsuarioDt usuarioDt) throws Exception {

		StringBuilder sql = new StringBuilder();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;

		sql.append(" SELECT * FROM SUSTENTACAO_ORAL SO ");
		sql.append(" WHERE SO.ID_PROC_PARTE_ADVOGADO = ? ");
		ps.adicionarString(idProcessoParteAdvogado);

		try {
			SustentacaoOralDt sustentacaoOralDt = new SustentacaoOralDt();
			rs = consultar(sql.toString(), ps);
			if (rs.next()) {
				sustentacaoOralDt.setId(rs.getString("ID_SUSTENTACAO_ORAL"));
				sustentacaoOralDt
						.setAudienciaProcessoDt(new AudienciaProcessoNe().consultarId(rs.getString("ID_AUDI_PROC")));
				sustentacaoOralDt.setProcessoParteAdvogadoDt(
						new ProcessoParteAdvogadoNe().consultarId(rs.getString("ID_PROC_PARTE_ADVOGADO")));
				sustentacaoOralDt
						.setUsuarioServentiaDt(new UsuarioServentiaNe().consultarId(rs.getString("ID_USU_SERV")));
				sustentacaoOralDt.setPendenciaDt(new PendenciaNe().consultarId(rs.getString("ID_PEND")));
			}
			return sustentacaoOralDt;
		} finally {
			if (rs != null)
				rs.close();
		}

	}

	//lrcampos 13/09/2019 - Altera Status da sustentacao oral
	public void alterarStatus(SustentacaoOralDt dados) throws Exception {

		StringBuilder sql = new StringBuilder();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql.append(" UPDATE PROJUDI.SUSTENTACAO_ORAL SET ID_PEND = ? "); ps.adicionarLong(dados.getPendenciaDt().getId());
		sql.append(" WHERE ID_SUSTENTACAO_ORAL = ? ");
		ps.adicionarString(dados.getId());

		executarUpdateDelete(sql.toString(), ps);

	}

	// alsqueiroz - 20/09/2019 15:12 - Método para consultar os pedidos de sustentacao oral
	public List<SustentacaoOralDt> consultarPedidosSustentacaoOral(String idAudienciaProcesso) throws Exception {
		ResultSetTJGO rs1 = null;

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		StringBuilder sql = new StringBuilder();

		SustentacaoOralDt sustentacaoOralDt;
		AudienciaProcessoDt audienciaProcessoDt;
		ProcessoParteAdvogadoDt processoParteAdvogadoDt;
		ProcessoParteDt processoParteDt;
		UsuarioServentiaDt usuarioServentiaDt;
		PendenciaDt pendenciaDt;

		List<SustentacaoOralDt> listaPedidosSustentacaoOral = new ArrayList<SustentacaoOralDt>();

		sql.append("SELECT ");
		sql.append("SO.ID_SUSTENTACAO_ORAL, ");
		sql.append("SO.ID_AUDI_PROC, ");
		sql.append("SO.ID_PROC_PARTE_ADVOGADO, ");
		sql.append("U.NOME AS NOME_ADVOGADO, ");
		sql.append("PP.NOME AS NOME_PARTE, ");
		sql.append("SO.ID_USU_SERV, ");
		sql.append("U2.NOME AS NOME_SOLICITANTE, ");
		sql.append("S.SERV, ");
		sql.append("SO.ID_PEND, ");
		sql.append("( ");
		sql.append("CASE (SELECT COUNT(P2.ID_PEND) FROM PEND P2 WHERE P2.ID_PEND = SO.ID_PEND) ");
		sql.append("WHEN 0 ");
		sql.append("THEN PF.DATA_INICIO ");
		sql.append("ELSE ");
		sql.append("P.DATA_INICIO ");
		sql.append("END ");
		sql.append(") AS DATA_INICIO, ");
		sql.append("( ");
		sql.append("CASE (SELECT COUNT(P2.ID_PEND) FROM PEND P2 WHERE P2.ID_PEND = SO.ID_PEND) ");
		sql.append("WHEN 0 ");
		sql.append("THEN PTF.PEND_TIPO_CODIGO ");
		sql.append("ELSE ");
		sql.append("PT.PEND_TIPO_CODIGO ");
		sql.append("END ");
		sql.append(") AS PEND_TIPO_CODIGO, ");
		sql.append("( ");
		sql.append("CASE (SELECT COUNT(P2.ID_PEND) FROM PEND P2 WHERE P2.ID_PEND = SO.ID_PEND) ");
		sql.append("WHEN 0 ");
		sql.append("THEN PTF.PEND_TIPO ");
		sql.append("ELSE ");
		sql.append("PT.PEND_TIPO ");
		sql.append("END ");
		sql.append(") AS PEND_TIPO ");
		sql.append("FROM SUSTENTACAO_ORAL SO ");
		sql.append("INNER JOIN AUDI_PROC AP ");
		sql.append("ON SO.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append("INNER JOIN PROC_PARTE_ADVOGADO PPA ");
		sql.append("ON SO.ID_PROC_PARTE_ADVOGADO = PPA.ID_PROC_PARTE_ADVOGADO ");
		sql.append("INNER JOIN USU_SERV US ");
		sql.append("ON PPA.ID_USU_SERV = US.ID_USU_SERV ");
		sql.append("INNER JOIN USU U ");
		sql.append("ON US.ID_USU = U.ID_USU ");
		sql.append("INNER JOIN PROC_PARTE PP ");
		sql.append("ON PP.ID_PROC_PARTE = PPA.ID_PROC_PARTE ");
		sql.append("INNER JOIN USU_SERV US2 ");
		sql.append("ON SO.ID_USU_SERV = US2.ID_USU_SERV ");
		sql.append("INNER JOIN USU U2 ");
		sql.append("ON US2.ID_USU = U2.ID_USU ");
		sql.append("INNER JOIN SERV S ");
		sql.append("ON US2.ID_SERV = S.ID_SERV ");
		sql.append("LEFT JOIN PEND P ");
		sql.append("ON SO.ID_PEND = P.ID_PEND ");
		sql.append("LEFT JOIN PEND_FINAL PF ");
		sql.append("ON SO.ID_PEND = PF.ID_PEND ");
		sql.append("LEFT JOIN PEND_TIPO PT ");
		sql.append("ON P.ID_PEND_TIPO = PT.ID_PEND_TIPO ");
		sql.append("LEFT JOIN PEND_TIPO PTF ");
		sql.append("ON PF.ID_PEND_TIPO = PTF.ID_PEND_TIPO ");
		sql.append("WHERE SO.ID_AUDI_PROC = ? ");
		ps.adicionarLong(idAudienciaProcesso);
		sql.append("ORDER BY NOME_SOLICITANTE ");

		try {
			rs1 = consultar(sql.toString(), ps);

			while (rs1.next()) {
				sustentacaoOralDt = new SustentacaoOralDt();
				audienciaProcessoDt = new AudienciaProcessoDt();
				processoParteAdvogadoDt = new ProcessoParteAdvogadoDt();
				processoParteDt = new ProcessoParteDt();
				usuarioServentiaDt = new UsuarioServentiaDt();
				pendenciaDt = new PendenciaDt();

				sustentacaoOralDt.setId(rs1.getString("ID_SUSTENTACAO_ORAL"));

				audienciaProcessoDt.setId(rs1.getString("ID_AUDI_PROC"));
				sustentacaoOralDt.setAudienciaProcessoDt(audienciaProcessoDt);

				processoParteAdvogadoDt.setId(rs1.getString("ID_PROC_PARTE_ADVOGADO"));
				processoParteAdvogadoDt.setNomeAdvogado(rs1.getString("NOME_ADVOGADO"));
				sustentacaoOralDt.setProcessoParteAdvogadoDt(processoParteAdvogadoDt);

				processoParteDt.setNome(rs1.getString("NOME_PARTE"));
				sustentacaoOralDt.setProcessoParteDt(processoParteDt);

				usuarioServentiaDt.setId(rs1.getString("ID_USU_SERV"));
				usuarioServentiaDt.setNome(rs1.getString("NOME_SOLICITANTE"));
				usuarioServentiaDt.setServentia(rs1.getString("SERV"));
				sustentacaoOralDt.setUsuarioServentiaDt(usuarioServentiaDt);

				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataInicio(rs1.getString("DATA_INICIO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				sustentacaoOralDt.setPendenciaDt(pendenciaDt);

				listaPedidosSustentacaoOral.add(sustentacaoOralDt);
			}
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}

		return listaPedidosSustentacaoOral;
	}

	//lrcampos 13/09/2019 - consulta audiencia processo pelo id processo
	public List<AudienciaProcessoDt> consultaAudienciaProcessoPeloIdProcesso(String idProcesso) throws Exception {
		
		StringBuilder sql = new StringBuilder();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		List<AudienciaProcessoDt> listaAudienciaProcesso = new ArrayList<AudienciaProcessoDt>();
		
		sql.append(" SELECT * FROM VIEW_AUDI_PROC_COMPLETA AP WHERE VIRTUAL = 1 AND AP.ID_PROC = ? "); ps.adicionarString(idProcesso);
		
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
				associarAudienciaProcessoDt(audienciaProcessoDt, rs);
				listaAudienciaProcesso.add(audienciaProcessoDt);
			}
		} finally {
			if (rs != null)
				rs.close();
		}
		
		return listaAudienciaProcesso;
	}
	
	//lrcampos 13/09/2019 seta os dados no objeto
	protected void associarAudienciaProcessoDt(AudienciaProcessoDt Dados, ResultSetTJGO rs1) throws Exception {

		Dados.setId(rs1.getString("ID_AUDI_PROC"));
		Dados.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
		Dados.setId_Audiencia(rs1.getString("ID_AUDI"));
		Dados.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
		Dados.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
		Dados.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
		Dados.setServentiaCargo(rs1.getString("SERV_CARGO"));
		Dados.setId_Processo(rs1.getString("ID_PROC"));
		Dados.setProcessoNumero(rs1.getString("PROC_NUMERO"));
		Dados.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI_PROC")));
		Dados.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
		Dados.setAudienciaTipoCodigo(rs1.getString("AUDI_TIPO_CODIGO"));
		Dados.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
		Dados.setId_ServentiaCargoPresidente(rs1.getString("ID_SERV_CARGO_PRESIDENTE"));
		Dados.setServentiaCargoPresidente(rs1.getString("SERV_CARGO_PRESIDENTE") + " - " + rs1.getString("NOME_PRESIDENTE"));			 
		Dados.setId_ServentiaMP(rs1.getString("ID_SERV_MP"));
		Dados.setServentiaMP(rs1.getString("SERV_MP"));
		Dados.setId_ServentiaCargoMP(rs1.getString("ID_SERV_CARGO_MP"));
		Dados.setServentiaCargoMP(rs1.getString("SERV_CARGO_MP") + " - " + rs1.getString("NOME_MP"));
		Dados.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_AUDIENCIA"));
		Dados.setProcessoTipo(rs1.getString("PROC_TIPO_AUDIENCIA"));

		try {
			if (!rs1.isNull("ID_PEND_EMENTA")) Dados.setId_PendenciaEmentaRelator(rs1.getString("ID_PEND_EMENTA"));
			if (!rs1.isNull("ID_PEND_VOTO")) Dados.setId_PendenciaVotoRelator(rs1.getString("ID_PEND_VOTO"));
			if (!rs1.isNull("ID_PEND_EMENTA_REDATOR")) Dados.setId_PendenciaEmentaRedator(rs1.getString("ID_PEND_EMENTA_REDATOR"));
			if (!rs1.isNull("ID_PEND_VOTO_REDATOR")) Dados.setId_PendenciaVotoRedator(rs1.getString("ID_PEND_VOTO_REDATOR"));
		} catch (Exception e) {
		}
	}

	//lrcampos 13/09/2019 consulta Situação do pedido de SO de uma audiProc
	public Integer consultarSituacaoPedidoSOidAudiProc(String idAudiProc, UsuarioDt usuarioDt) throws Exception {

		StringBuilder sql = new StringBuilder();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		ResultSetTJGO rs = null;

		sql.append(" SELECT PT.PEND_TIPO_CODIGO FROM SUSTENTACAO_ORAL SO");
		sql.append(" INNER JOIN PEND P ON P.ID_PEND = SO.ID_PEND ");
		sql.append(" INNER JOIN PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO ");
		sql.append(" INNER JOIN PEND_RESP PR ON PR.ID_PEND = P.ID_PEND ");
		sql.append(" WHERE SO.ID_AUDI_PROC = ? "); 	ps.adicionarString(idAudiProc);
		//lrcampos 27/12/2019 13:53 - filtra se o usuario logado possui alguma pendencia de deferido/indeferido ou solicitou S.O
		if(usuarioDt != null) {
			sql.append("AND (PR.ID_USU_RESP = ? OR P.ID_USU_CADASTRADOR = ?) " ); 
			ps.adicionarString(usuarioDt.getId_UsuarioServentia());
			ps.adicionarString(usuarioDt.getId_UsuarioServentia());
		}

		try {
			rs = consultar(sql.toString(), ps);
			if (rs.next()) {
				return rs.getInt("PEND_TIPO_CODIGO");
			}
		} finally {
			if (rs != null)
				rs.close();
		}

		return null;
	}
	
	// jvosantos - 15/10/2019 17:04 - Corrigir nome de atributo
	public List<VotoSessaoLocalizarDt> consultarSustentacaoOral(String idUsuarioServentia, String processoNumero) throws Exception {
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
				+ " JOIN SUSTENTACAO_ORAL SO ON (SO.ID_PEND = P.ID_PEND AND SO.ID_AUDI_PROC = AC.ID_AUDI_PROC) "
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
		//mrbatista 21/10/2019 16:47 - Correção da query.
		if (StringUtils.isNotEmpty(processoNumero)) {
			if ((processoNumero.length() > 0)) {
				sql += " AND PROC.PROC_NUMERO = ?";
				String[] numeroProcesso = processoNumero.split("\\.");
				ps.adicionarLong(numeroProcesso[0]);

				if (numeroProcesso.length > 1) {
					sql += " AND PROC.DIGITO_VERIFICADOR = ?";
					ps.adicionarLong(numeroProcesso[1]);
				}
			}
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

	public HashMap<String, String> consultaStatusPedidoSO(String idProcesso, UsuarioDt usuarioDt, String idServentia) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		HashMap<String, String> statusPedidoSO = new HashMap<String, String>();
		// lrcampos 12/07/2019 * Alterado busca da proc_parte_advogado na tabela de SUSTENTACAO_ORAL

		sql.append(" SELECT SO.ID_PROC_PARTE_ADVOGADO, ");
		sql.append(" CASE WHEN PT.PEND_TIPO_CODIGO = 201  THEN 'Pedido Enviado' ");
		sql.append("	  WHEN PT.PEND_TIPO_CODIGO = 208 THEN 'Deferido' ");
		sql.append(" 	  WHEN PT.PEND_TIPO_CODIGO = 209 THEN 'Indeferido' ");
		sql.append("      WHEN PT.PEND_TIPO_CODIGO = 307 THEN 'Verificar pedido de S.O - Deferimento Automático' ");
		sql.append(" 		   END AS SISTUACAO ");
		sql.append(" FROM AUDI_PROC AP ");
		sql.append(" INNER JOIN SUSTENTACAO_ORAL SO ON SO.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" INNER JOIN PROC_PARTE_ADVOGADO PPA ON PPA.ID_PROC_PARTE_ADVOGADO = SO.ID_PROC_PARTE_ADVOGADO ");
		sql.append(" INNER JOIN PEND P ON P.ID_PEND = SO.ID_PEND ");
		sql.append(" INNER JOIN PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ");
		sql.append(" INNER JOIN PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO ");
		sql.append(" WHERE AP.ID_AUDI_PROC_STATUS = ? ");ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql.append(" AND AP.ID_PROC = ? ");	ps.adicionarLong(idProcesso);
		sql.append(" AND (SO.ID_USU_SERV = ? OR ppa.ID_USU_SERV = ? OR pr.ID_SERV = ?) "); 
		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
		ps.adicionarLong(idServentia);

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);

			while (rs1.next()) {
				statusPedidoSO.put(rs1.getString("ID_PROC_PARTE_ADVOGADO"), rs1.getString("SISTUACAO"));
			}
		} finally {
			if (rs1 != null)
				rs1.close();
		}

		return statusPedidoSO;
	}
	
	public boolean possuiAudienciaVirtualOriginalPJD(String idProcesso, String dataAudienciaOriginal) throws Exception {
		String idAudienciaProcesso = null;
		Boolean possuiAudienciaVirtualOriginalPJD = false;
		VotoPs votoPs = new VotoPs(this.Conexao);
		AudienciaProcessoPs audiProcPs = new AudienciaProcessoPs(this.Conexao);
		if (!StringUtils.isEmpty(dataAudienciaOriginal)) {
			idAudienciaProcesso = votoPs.buscaAudienciaVirtualOriginalPJD(idProcesso, Funcoes.FormatarDataHoraMinuto(dataAudienciaOriginal));
			if (idAudienciaProcesso != null) {
				AudienciaProcessoDt audienciaProcessoDt = audiProcPs.consultarIdCompleto(idAudienciaProcesso);
				if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo()
						.equals(String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL))) {
					possuiAudienciaVirtualOriginalPJD = true;
				}
			}
		}
		return possuiAudienciaVirtualOriginalPJD;
	}
	
}
