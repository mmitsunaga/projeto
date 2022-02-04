package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.jurisprudencia.Jurisprudencia;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;

public class JurisprudenciaPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = -414659358579818386L;

	/**
	 * 
	 */

	public JurisprudenciaPs(Connection conexao) {
    	Conexao = conexao;
	}

	
	public Object[] getArrayIdEmentas(String lastIndexedEmentaId) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<String> resultado = new ArrayList<>();		
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT a.ID_ARQ");
			sql.append(" FROM ARQ A");
			sql.append(" JOIN ARQ_TIPO arqt on (a.id_arq_tipo = arqt.id_arq_tipo)");
			sql.append(" JOIN MOVI_ARQ ma on (a.id_arq = ma.id_arq)");
			sql.append(" JOIN MOVI m on (ma.id_movi = m.id_movi)");
			sql.append(" JOIN PROC p on (p.id_proc = m.id_proc)");
			sql.append(" JOIN serv s on (p.id_serv = s.id_serv)");
			sql.append(" JOIN comarca c on (c.id_comarca = s.id_comarca)");
			sql.append(" JOIN AUDI_PROC AP ON AP.ID_PROC = P.ID_PROC");
			sql.append(" JOIN AUDI AD ON AD.ID_AUDI = AP.ID_AUDI");
			sql.append(" JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO");
			sql.append(" JOIN PROC_STATUS PS ON PS.ID_PROC_STATUS = P.ID_PROC_STATUS");
			sql.append(" WHERE arqt.ARQ_TIPO_CODIGO = ? ");
			sql.append(" AND usu_assinador IS NOT NULL");
			sql.append(" AND AD.ID_AUDI_TIPO = ? ");
			sql.append(" AND PS.PROC_STATUS_CODIGO <> ? AND P.SEGREDO_JUSTICA = 0");
			sql.append(" AND TO_NUMBER(TO_CHAR(AP.DATA_MOVI, 'YYYYMMDD')) = TO_NUMBER(TO_CHAR(M.DATA_REALIZACAO, 'YYYYMMDD'))");
			sql.append(" AND a.ID_ARQ > ?"); 
			sql.append(" ORDER BY a.ID_ARQ");
						
			ps.adicionarLong(String.valueOf(ArquivoTipoDt.EMENTA));
			ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			ps.adicionarLong(String.valueOf(ProcessoStatusDt.SIGILOSO));
			ps.adicionarLong(lastIndexedEmentaId);
						
			rs = consultar(sql.toString(), ps);
			
			while (rs.next()) {
				resultado.add(rs.getString("ID_ARQ"));								
			}
						
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();	
			}
		}
		
		return resultado.toArray();
	}
	
	/**
	 * Consulta as jurisprudências de determinado período para ser importadas no TAMINO
	 * Verificado se o processo não é sigiloso (status = 8) e não é segredo de justiça
	 * @param dataInicio
	 * @param dataFinal
	 * @return
	 * @throws Exception
	 */
	public List<String> getArrayIdEmentasPorPeriodo(String dataInicio, String dataFinal) throws Exception {		
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<String> resultado = new ArrayList<>();		
		try {			
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT A.ID_ARQ, (lpad(p.proc_numero,7,0) || lpad(p.digito_verificador,2,0) || p.ano || '809' || lpad(p.forum_codigo,4,0)) AS NUMERO");
			sql.append(" FROM ARQ A");
			sql.append(" JOIN ARQ_TIPO arqt on (a.id_arq_tipo = arqt.id_arq_tipo)");
			sql.append(" JOIN MOVI_ARQ ma on (a.id_arq = ma.id_arq)");
			sql.append(" JOIN MOVI m on (ma.id_movi = m.id_movi)");
			sql.append(" JOIN PROC p on (p.id_proc = m.id_proc)");
			sql.append(" JOIN serv s on (p.id_serv = s.id_serv)");
			sql.append(" JOIN comarca c on (c.id_comarca = s.id_comarca)");
			sql.append(" JOIN AUDI_PROC AP ON AP.ID_PROC = P.ID_PROC");
			sql.append(" JOIN AUDI AD ON AD.ID_AUDI = AP.ID_AUDI");
			sql.append(" JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO");
			sql.append(" JOIN PROC_STATUS PS ON PS.ID_PROC_STATUS = P.ID_PROC_STATUS");
			sql.append(" WHERE arqt.ARQ_TIPO_CODIGO = ? ");
			sql.append(" AND A.usu_assinador IS NOT NULL");
			sql.append(" AND AD.ID_AUDI_TIPO = ?");
			sql.append(" AND (PS.PROC_STATUS_CODIGO <> ? AND P.SEGREDO_JUSTICA = 0)");
			sql.append(" AND TO_NUMBER(TO_CHAR(AP.DATA_MOVI, 'YYYYMMDD')) = TO_NUMBER(TO_CHAR(M.DATA_REALIZACAO, 'YYYYMMDD'))");
			sql.append(" AND AP.DATA_MOVI BETWEEN ? AND ?"); 
			sql.append(" ORDER BY A.ID_ARQ");		
			
			ps.adicionarLong(String.valueOf(ArquivoTipoDt.EMENTA));
			ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			ps.adicionarLong(String.valueOf(ProcessoStatusDt.SIGILOSO));
			ps.adicionarDateTime(dataInicio); 
			ps.adicionarDateTime(dataFinal);
			
			rs = consultar(sql.toString(), ps);
			
			while (rs.next()) {
				resultado.add(rs.getString("ID_ARQ") + ";" + rs.getString("NUMERO"));
			}
						
		} finally {
			try {
				if (rs != null) rs.close();
			} catch (Exception e1) {
				e1.printStackTrace();	
			}
		}
		
		return resultado;
		
	}
	
	
	public Jurisprudencia getJurisprudencia(String idEmenta) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Jurisprudencia resultado = Jurisprudencia.getPreJurisprudencia();
		int proc_tipo_codigo = 0;
		String id_proc = "";
		
		try {


			String sql = " SELECT PT.PROC_TIPO_CODIGO, P.ID_PROC, p.ID_AREA as area, a.ID_ARQ as id_ementa, a.content_type, a.recibo,"
					+ " (LPAD(p.proc_numero,7,0)   || '.' || LPAD(p.digito_verificador,2,0) "
					+ " || '.' || p.ano || '.8.09.' ||LPAD(p.forum_codigo,4,0)) as numero, "
					+ " NVL((SELECT C2.COMARCA "
					+ " FROM RECURSO R2 INNER JOIN SERV S2 ON S2.ID_SERV = R2.ID_SERV_ORIGEM "
					+ " INNER JOIN COMARCA C2 ON C2.ID_COMARCA = S2.ID_COMARCA "
					+ " WHERE R2.ID_PROC = P.ID_PROC AND ROWNUM = 1), c.comarca) as comarca_origem, "
					+ " a.data_insercao as data_acordao, "
					+ " (SELECT case when SC.ID_CARGO_TIPO = 19 then 'Des(a). ' || SC.NOME_USU when SC.ID_CARGO_TIPO = 5 then 'Dr(a). ' || SC.NOME_USU else SC.NOME_USU END " 
					+ " FROM PROJUDI.VIEW_SERV_CARGO SC "
					+ " WHERE AP.ID_SERV_CARGO = SC.ID_SERV_CARGO) as nome_relator, "
					+ " (SELECT case when SC.ID_CARGO_TIPO = 19 then 'Des(a). ' || SC.NOME_USU when SC.ID_CARGO_TIPO = 5 then 'Dr(a). ' || SC.NOME_USU else SC.NOME_USU END "
					+ " FROM PROJUDI.VIEW_SERV_CARGO SC WHERE AP.ID_SERV_CARGO_REDATOR = SC.ID_SERV_CARGO) as nome_redator,"
					+ " A.ARQ as ementaTexto,"
					+ " NVL(PT.POLO_ATIVO, 'POLO ATIVO') as polo_ativo, NVL(PT.POLO_PASSIVO, 'POLO PASSIVO') as polo_passivo, P.SEGREDO_JUSTICA as segredo_justica, "
					+ " PT.PROC_TIPO as classe_recurso, (NVL((SELECT RP.NOME FROM VIEW_RECURSO_PARTE RP WHERE RP.ID_PROC = P.ID_PROC "
					+ " AND RP.ID_PROC_TIPO = AP.ID_PROC_TIPO"
					+ " AND RP.ID_PROC_PARTE_TIPO = 2 "
					+ " AND RP.DATA_BAIXA IS NULL "
					+ " AND RP.DATA_RETORNO IS NULL AND ROWNUM = 1), (SELECT PP.NOME  FROM PROC_PARTE PP  WHERE PP.ID_PROC = P.ID_PROC "
					+ " AND PP.ID_PROC_PARTE_TIPO = 2 AND PP.DATA_BAIXA IS NULL AND ROWNUM = 1))) AS NOME_POLO_ATIVO,"
					+ " (NVL((SELECT RP.NOME FROM VIEW_RECURSO_PARTE RP WHERE RP.ID_PROC = P.ID_PROC AND RP.ID_PROC_TIPO = AP.ID_PROC_TIPO"
					+ " AND RP.ID_PROC_PARTE_TIPO = 3 AND RP.DATA_BAIXA IS NULL AND RP.DATA_RETORNO IS NULL AND ROWNUM = 1),"
					+ " (SELECT PP.NOME FROM PROC_PARTE PP WHERE PP.ID_PROC = P.ID_PROC AND PP.ID_PROC_PARTE_TIPO = 3 AND PP.DATA_BAIXA IS NULL"
					+ " AND ROWNUM = 1))) AS NOME_POLO_PASSIVO, "
					+ "(SELECT A2.ID_ARQ "
					+ " FROM ARQ A2 INNER JOIN MOVI_ARQ MA2 ON MA2.ID_ARQ = A2.ID_ARQ "
					+ " JOIN ARQ_TIPO AT2 ON AT2.ID_ARQ_TIPO = A2.ID_ARQ_TIPO "
					+ " WHERE M.ID_MOVI = MA2.ID_MOVI AND AT2.ARQ_TIPO_CODIGO = " + ArquivoTipoDt.RELATORIO_VOTO
					+ " AND ROWNUM = 1) AS RELATORIO_VOTO,"
					+  " M.ID_MOVI AS ID_MOVIMENTACAO, "
					+ " S.SERV AS CAMARA "
					+ " FROM ARQ a  "
					+ " JOIN ARQ_TIPO arqt on (a.id_arq_tipo = arqt.id_arq_tipo) "
					+ " JOIN MOVI_ARQ ma on (a.id_arq = ma.id_arq)"
					+ " JOIN MOVI m on (ma.id_movi = m.id_movi)"
					+ " JOIN PROC p on (p.id_proc = m.id_proc)"
					+ " JOIN serv s on (p.id_serv = s.id_serv)"
					+ " JOIN comarca c on (c.id_comarca = s.id_comarca)"
					+ " JOIN AUDI_PROC AP ON AP.ID_PROC = P.ID_PROC"
					+ " JOIN AUDI AD ON AD.ID_AUDI = AP.ID_AUDI"
					+ " JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO "					
					+ " WHERE arqt.ARQ_TIPO_CODIGO  = " + ArquivoTipoDt.EMENTA
					+ " AND usu_assinador is not null "
					+ " AND AD.ID_AUDI_TIPO = 12 "
					+ " AND TO_NUMBER(TO_CHAR(AP.DATA_MOVI, 'YYYYMMDD')) = "
					+ " TO_NUMBER(TO_CHAR(M.DATA_REALIZACAO, 'YYYYMMDD')) "
					+ " AND A.ID_ARQ = ?";
			
			ps.adicionarLong(idEmenta);

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				//resultado.setNr_processo(rs1.getString("numero"));
				String numero = rs1.getString("numero").replaceAll("\\.", "");
				numero = numero.replaceAll("-", "");
				resultado.setCd_acao(rs1.getString("area"));
				resultado.setNr_recurso(numero);
				resultado.setDs_comarca(rs1.getString("comarca_origem"));
				resultado.setParte8(rs1.getString("id_ementa"));
				resultado.generateDatasAcordao(rs1.getString("data_acordao"));
				resultado.setNm_relator(rs1.getString("nome_relator") != null ?  rs1.getString("nome_relator"): "");
				resultado.setNm_redator(rs1.getString("nome_redator") != null ?  rs1.getString("nome_redator"): "");
				resultado.setIn_segredo(rs1.getString("segredo_justica").equals("0") ? "N" : "S");
				resultado.setDs_recurso(rs1.getString("classe_recurso") != null ?  rs1.getString("classe_recurso"): "");
				resultado.setDs_camara(rs1.getString("CAMARA"));
				resultado.setParte1(rs1.getString("polo_ativo") + ": " + rs1.getString("NOME_POLO_ATIVO"));
				resultado.setParte2(rs1.getString("polo_passivo") + ": " + rs1.getString("NOME_POLO_PASSIVO"));
				resultado.setConteudo(rs1.getBytes("ementaTexto"));
				resultado.setContentType(rs1.getString("CONTENT_TYPE"));
				resultado.generateAcordaoLink(rs1.getString("ID_MOVIMENTACAO"), rs1.getString("RELATORIO_VOTO")); //id voto
				boolean temRecibo = ValidacaoUtil.isNaoVazio(rs1.getString("recibo")) && rs1.getString("recibo").equals("1");
				resultado.setTemRecibo(temRecibo);
				resultado.generateEmentaTexto(rs1.getString("CONTENT_TYPE"), rs1.getBytes("ementaTexto"), temRecibo);
				proc_tipo_codigo = rs1.getInt("PROC_TIPO_CODIGO");
				id_proc = rs1.getString("ID_PROC");
			}

		} catch (Exception e1) {
			throw e1;
		}
		finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e1) {
				throw e1;
			}
		}
		if(proc_tipo_codigo == ProcessoTipoDt.HABEAS_CORPUS_CF_LIVRO_III) {
			String paciente = resultado.getParte1();
			if(id_proc != null)
				paciente = this.getPaciente(id_proc);
			if (paciente != null) {
				resultado.setParte1("Paciente: " + paciente);
			}
		}

		return resultado;
	}
	
	private String getPaciente(String id_proc) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String resultado = null;
		
		try {

			String sql = "SELECT PP.NOME FROM PROC_PARTE PP "
					+ " JOIN PROC_PARTE_TIPO PPT ON (PPT.ID_PROC_PARTE_TIPO = PP.ID_PROC_PARTE_TIPO) "
					+ " WHERE PP.ID_PROC = ?  "
					+ " AND PP.DATA_BAIXA IS NULL "
					+ " AND PP.ID_PROC_PARTE_TIPO = " + ProcessoParteTipoDt.PACIENTE;
			
			ps.adicionarLong(id_proc);
			rs1 = consultar(sql, ps);
		
			if (rs1.next()) {
				resultado = rs1.getString("NOME");
			}

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		return resultado;
	}	
	
	public String getLastEmentaId() throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String resultado = null;
		String arquiTipo = String.valueOf(ArquivoTipoDt.EMENTA);
		
		try {

			String sql = "SELECT MAX(arq.ID_ARQ) as ID_ARQ FROM ARQ arq "
					+ " JOIN ARQ_TIPO arqt on (arq.id_arq_tipo = arqt.id_arq_tipo) "
					+ " JOIN MOVI_ARQ ma on (arq.id_arq = ma.id_arq) " 
					+ " JOIN MOVI m on (ma.id_movi = m.id_movi) "
					+ " JOIN PROC p on (p.id_proc = m.id_proc) "
					+ " where arqt.ARQ_TIPO_CODIGO = ? and arq.usu_assinador is not null";
			
			ps.adicionarLong(arquiTipo);
			rs1 = consultar(sql, ps);
			rs1.next();
			if (rs1 != null) {
				resultado = rs1.getString("ID_ARQ");
			}

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e1) {
				throw e1;
			}
		}

		return resultado;
	}
	
	
	
	/**
	 * Consulta as jurisprudencias das Turmas Recursais por uma data específica.
	 * São considerados os atos públicos dos tipos: Acórdão, Relatório, Ementa, Relatório e Voto
	 * Não retorna os atos com segredo de justiça (o texto pode ainda mostrar o nome sem tratamento)
	 * @param dataAudiencia - data de realização da audiencia da turma (formato: AAAAMMDD)
	 * @return
	 */
	public List<Jurisprudencia> consultarJurisprudenciasTurmaRecursalPorData(String dataAudiencia) throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();		
		String filtoServentiaSubTipo = ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL + "," + ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL + "," + ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL + "," + ServentiaSubtipoDt.UPJ_TURMA_RECURSAL;		
		String filtoArquivoTipo = ArquivoTipoDt.ACORDAO + "," + ArquivoTipoDt.RELATORIO + "," + ArquivoTipoDt.RELATORIO_VOTO + "," + ArquivoTipoDt.EMENTA;		
		sql.append("SELECT p.id_proc");		
		sql.append(", (lpad(p.proc_numero,7,0) || '.' || lpad(p.digito_verificador,2,0) || '.' || p.ano || '.8.09.' || lpad(p.forum_codigo,4,0)) AS numero");
		sql.append(", p.segredo_justica");
		sql.append(", p.id_area AS area");
		sql.append(", pt.proc_tipo_codigo");
		sql.append(", pt.proc_tipo AS classe_recurso");
		sql.append(", (SELECT sc.nome_usu FROM projudi.view_serv_cargo sc WHERE vw.id_serv_cargo = sc.id_serv_cargo) AS nome_relator");
		sql.append(", (SELECT sc.nome_usu FROM projudi.view_serv_cargo sc WHERE vw.id_serv_cargo_redator = sc.id_serv_cargo) AS nome_redator");		
		sql.append(", NVL (pt.polo_ativo, 'POLO ATIVO') AS polo_ativo");		
		sql.append(", NVL (");
		sql.append("  ( SELECT rp.nome"); 
		sql.append(" 	FROM projudi.view_recurso_parte rp"); 
		sql.append("	WHERE rp.id_proc = p.id_proc and rp.id_proc_tipo = vw.id_proc_tipo"); 
		sql.append("	AND rp.id_proc_parte_tipo = " + ProcessoParteDt.ID_POLO_ATIVO + " AND rp.data_baixa IS NULL AND rp.data_retorno IS NULL AND rownum = 1");
		sql.append(" ), ");
		sql.append(" ( SELECT pp.nome"); 
		sql.append("   FROM projudi.proc_parte pp"); 
		sql.append("   WHERE pp.id_proc = p.id_proc AND pp.id_proc_parte_tipo = " + ProcessoParteDt.ID_POLO_ATIVO + " AND pp.data_baixa IS NULL AND rownum = 1");
		sql.append(" )) AS NOME_POLO_ATIVO");		
		sql.append(", NVL (pt.polo_passivo, 'POLO PASSIVO') AS polo_passivo");  
		sql.append(", NVL (");
		sql.append(" ( SELECT rp.nome"); 
		sql.append("   FROM projudi.view_recurso_parte rp"); 
		sql.append("   WHERE rp.id_proc = p.id_proc AND rp.id_proc_tipo = vw.id_proc_tipo"); 
		sql.append("   AND rp.id_proc_parte_tipo = " + ProcessoParteDt.ID_POLO_PASSIVO + " AND rp.data_baixa IS NULL AND rp.data_retorno IS NULL AND rownum = 1");
		sql.append(" ), ");
		sql.append(" ( SELECT pp.nome"); 
		sql.append("   FROM projudi.proc_parte pp"); 
		sql.append("   WHERE pp.id_proc = p.id_proc AND pp.id_proc_parte_tipo = " + ProcessoParteDt.ID_POLO_PASSIVO + " AND pp.data_baixa IS NULL AND rownum = 1");
		sql.append(" )) AS NOME_POLO_PASSIVO");
		sql.append(", NVL (( select PP.NOME from PROC_PARTE PP where PP.ID_PROC = P.ID_PROC and PP.ID_PROC_PARTE_TIPO = 132 and PP.DATA_BAIXA IS NULL and rownum = 1), NULL) AS NOME_PACIENTE");
		sql.append(", NVL ((SELECT c2.comarca FROM projudi.recurso r2 INNER JOIN serv s2 ON s2.id_serv = r2.id_serv_origem INNER JOIN projudi.comarca c2 ON c2.id_comarca = s2.id_comarca WHERE r2.id_proc = p.id_proc AND rownum = 1), c.comarca) AS comarca_origem");
		sql.append(", s.id_serv");
		sql.append(", s.serv as turma");
		sql.append(", m.id_movi");
		sql.append(", ma.id_movi_arq");
		sql.append(", m.id_movi_tipo");
		sql.append(", mt.movi_tipo");
		sql.append(", a.id_arq");
		sql.append(", atp.arq_tipo");
		sql.append(", a.data_insercao AS data_acordao");
		sql.append(", a.arq as texto");
		sql.append(" FROM (");				  
		sql.append("	SELECT ap.id_proc, ap.id_audi, ap.data_movi, ap.id_serv_cargo, ap.id_serv_cargo_redator, ap.id_proc_tipo");
		sql.append("	FROM projudi.audi a INNER JOIN projudi.audi_proc ap ON a.id_audi = ap.id_audi AND a.id_audi_tipo = " + AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo()); 
		sql.append("	WHERE TO_NUMBER(TO_CHAR(ap.data_movi, 'YYYYMMDD')) = ?");
		sql.append(" ) vw");
		sql.append(" INNER JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = vw.id_proc_tipo");
		sql.append(" INNER JOIN projudi.proc p ON p.id_proc = vw.id_proc AND p.segredo_justica = 0");		
		sql.append(" INNER JOIN projudi.serv s ON s.id_serv = p.id_serv");
		sql.append(" INNER JOIN projudi.serv_subtipo ss ON ss.id_serv_subtipo = s.id_serv_subtipo AND ss.serv_subtipo_codigo in (" + filtoServentiaSubTipo + ")");
		sql.append(" INNER JOIN projudi.comarca c ON c.id_comarca = s.id_comarca");
		sql.append(" INNER JOIN projudi.movi m ON m.id_proc = p.id_proc and TO_NUMBER(TO_CHAR(vw.DATA_MOVI, 'YYYYMMDD')) = TO_NUMBER(TO_CHAR(m.DATA_REALIZACAO, 'YYYYMMDD'))");
		sql.append(" INNER JOIN projudi.movi_tipo mt ON m.id_movi_tipo = mt.id_movi_tipo");
		sql.append(" INNER JOIN projudi.movi_arq ma ON m.id_movi = ma.id_movi AND ma.id_movi_arq_acesso in (1,2)");
		sql.append(" INNER JOIN projudi.arq a ON ma.id_arq = a.id_arq");
		sql.append(" INNER JOIN projudi.arq_tipo atp ON atp.id_arq_tipo = a.id_arq_tipo AND atp.id_arq_tipo in (" + filtoArquivoTipo + ")");
		ps.adicionarString(dataAudiencia);
		List<Jurisprudencia> lista = new ArrayList<>();
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				Jurisprudencia jurisprudencia = Jurisprudencia.getPreJurisprudencia();
				String numero = rs.getString("numero").replaceAll("\\.", "");
				numero = numero.replaceAll("-", "");
				jurisprudencia.setCd_acao(rs.getString("area"));
				jurisprudencia.setNr_recurso(numero);
				jurisprudencia.setDs_comarca(rs.getString("comarca_origem"));
				jurisprudencia.setParte8(rs.getString("id_arq"));
				jurisprudencia.generateDatasAcordao(rs.getString("data_acordao"));
				jurisprudencia.setData_acordao(Funcoes.FormatarData(rs.getDate("data_acordao"), "yyyyMMdd"));
				jurisprudencia.setData_publicacao(Funcoes.FormatarData(rs.getDate("data_acordao"), "yyyyMMdd"));
				jurisprudencia.setDt_acordao(Funcoes.FormatarData(rs.getDate("data_acordao"), "dd/MM/yyyy"));
				jurisprudencia.setDt_publicacao_diario(Funcoes.FormatarData(rs.getDate("data_acordao"), "dd/MM/yyyy"));
				jurisprudencia.setNm_relator(rs.getString("nome_relator") != null ?  rs.getString("nome_relator"): "");
				jurisprudencia.setNm_redator(rs.getString("nome_redator") != null ?  rs.getString("nome_redator"): "");
				jurisprudencia.setIn_segredo(rs.getString("segredo_justica").equals("0") ? "N" : "S");
				jurisprudencia.setDs_recurso(rs.getString("classe_recurso") != null ?  rs.getString("classe_recurso"): "");
				jurisprudencia.setDs_camara(rs.getString("turma"));
				jurisprudencia.setParte1(rs.getString("polo_ativo") + ": " + rs.getString("nome_polo_ativo"));
				jurisprudencia.setParte2(rs.getString("polo_passivo") + ": " + rs.getString("nome_polo_passivo"));
				jurisprudencia.gerarLinkAcordao(rs.getString("id_movi_arq"), rs.getString("id_proc"));				
				jurisprudencia.generateEmentaTexto(rs.getBytes("texto"));
				//jurisprudencia.extrairEmentaDoTexto(rs.getString("id_arq"));
				int procTipoCodigo = rs.getInt("proc_tipo_codigo");
				if (procTipoCodigo == ProcessoTipoDt.HABEAS_CORPUS_CF_LIVRO_III){
					String paciente = rs.getString("nome_paciente");
					if (ValidacaoUtil.isNaoVazio(paciente)){
						jurisprudencia.setParte1("Paciente: " + paciente.toUpperCase());
					}
				}
				lista.add(jurisprudencia);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try {if (rs != null) rs.close();} catch(Exception e) {}			
		}
		return lista;
	}
	

}
