package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.DiarioJusticaEletronicoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

/**
 *  
 * @author mmitsunaga
 *
 */
public class DiarioJusticaEletronicoPs extends Persistencia {

	private static final long serialVersionUID = -8804851234672173087L;

	private final int CODIGO_OPCAO_PUBLICACAO_2_GRAU = 1;	
	private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL = 2;	
	private final int CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR = 3;
		
	public DiarioJusticaEletronicoPs(Connection conexao) {
    	Conexao = conexao;
	}
	
	/**
	 * Somente para os advogados habilitados no processo
	 * @param dataInicio
	 * @param dataFinal
	 * @return
	 * @throws Exception 
	 */
	public List<DiarioJusticaEletronicoDt> listarIntimacoesOuCartaCitacaoPorData(String dataInicial, String dataFinal, int opcao) throws Exception {
		
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.ID_PROC");
		sql.append(", p.PROC_NUMERO");
		sql.append(", p.DIGITO_VERIFICADOR");
		sql.append(", p.FORUM_CODIGO");
		sql.append(", p.ANO");
		sql.append(", p.SEGREDO_JUSTICA");
		sql.append(", s.SERV");
		sql.append(", pt.id_cnj_classe");
		sql.append(", ptr.id_cnj_classe as id_cnj_classe_recurso");
		sql.append(", vw.ID_PEND");
		sql.append(", vw.PEND_TIPO_CODIGO");
		sql.append(", vw.ID_MOVI"); 
		sql.append(", vw.ID_MOVI_ARQ");
		sql.append(", vw.DATA_INICIO");    
		sql.append(", vw.DATA_FIM");
		sql.append(", vw.ID_PARTE_INTIMADA");
		sql.append(", vw.NOME_PARTE_INTIMADA");
		sql.append(", vw.CPF_CNPJ_PARTE_INTIMADA");    
		sql.append(", CASE WHEN vw.tipo_parte_intimada = 1 OR vw.tipo_parte_intimada_recurso = 1 THEN 'A'");
		sql.append("        WHEN vw.tipo_parte_intimada = 0 OR vw.tipo_parte_intimada_recurso = 0 THEN 'P'");
		sql.append("        ELSE 'a' END AS POLO_PARTE_INTIMADA");		
		sql.append(", vw.OAB_ADVOGADO_INTIMADO");
		sql.append(", vw.UF_OAB_ADVOGADO_INTIMADO");
		sql.append(", vw.NOME_ADVOGADO_INTIMADO");
		sql.append(", vw.ID_ARQ");
		sql.append(", vw.ARQ_TIPO");
		sql.append(", vw.ARQ_TIPO_CODIGO");
		sql.append(", vw.ID_ARQ_VOTO");
		sql.append(", vw.ID_MOVI_ARQ_VOTO");
		sql.append(" FROM (");
		
		/*
		 * Subconsulta 2o nível - responsável em selecionar os advogados ADVOGADO_PARTICULAR e ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO
		 * das pendências e os dados das partes intimadas
		 */
		sql.append(" SELECT DISTINCT pf.ID_PROC");
		sql.append(", pf.ID_MOVI");
		sql.append(", pf.ID_MOVI_ARQ");
		sql.append(", pf.ID_PEND");
		sql.append(", pf.PEND_TIPO_CODIGO");
		sql.append(", pf.DATA_INICIO");
		sql.append(", pf.DATA_FIM");
		sql.append(", pp.ID_PROC_PARTE AS ID_PARTE_INTIMADA");
		sql.append(", pp.NOME AS NOME_PARTE_INTIMADA");
		sql.append(", CASE WHEN pp.cpf IS NOT NULL THEN pp.cpf WHEN pp.cnpj IS NOT NULL THEN pp.cnpj ELSE null END AS CPF_CNPJ_PARTE_INTIMADA");
		sql.append(", ppt.proc_parte_tipo_codigo AS TIPO_PARTE_INTIMADA");		
		sql.append(", oab.oab_numero as OAB_ADVOGADO_INTIMADO");
		sql.append(", est.UF AS UF_OAB_ADVOGADO_INTIMADO");
		sql.append(", u.NOME AS NOME_ADVOGADO_INTIMADO");
		sql.append(", a.ID_ARQ");
		sql.append(", ta.ARQ_TIPO");
		sql.append(", ta.ARQ_TIPO_CODIGO");
		sql.append(", ( SELECT a2.id_arq");
		sql.append(" 	FROM projudi.arq a2");
		sql.append(" 	JOIN projudi.movi_arq ma2 ON ma2.id_arq = a2.id_arq AND ma2.id_movi = pf.id_movi");
		sql.append(" 	JOIN projudi.arq_tipo at2 ON at2.id_arq_tipo = a2.id_arq_tipo AND at2.arq_tipo_codigo = " + ArquivoTipoDt.RELATORIO_VOTO);
		sql.append(" 	WHERE ta.arq_tipo_codigo = " + ArquivoTipoDt.EMENTA + " AND ROWNUM = 1");
		sql.append("  ) AS ID_ARQ_VOTO");
		sql.append(", ( SELECT ma2.id_movi_arq");
		sql.append(" 	FROM projudi.arq a2");
		sql.append(" 	JOIN projudi.movi_arq ma2 ON ma2.id_arq = a2.id_arq AND ma2.id_movi = pf.id_movi");
		sql.append(" 	JOIN projudi.arq_tipo at2 ON at2.id_arq_tipo = a2.id_arq_tipo AND at2.arq_tipo_codigo = " + ArquivoTipoDt.RELATORIO_VOTO);
		sql.append(" 	WHERE ta.arq_tipo_codigo = " + ArquivoTipoDt.EMENTA + " AND ROWNUM = 1");
		sql.append("  ) AS ID_MOVI_ARQ_VOTO");
		sql.append(", ( SELECT vw.proc_parte_tipo_codigo");
		sql.append("	FROM projudi.view_recurso_parte vw");
		sql.append("	WHERE vw.id_proc = pf.id_proc AND id_proc_parte = pp.id_proc_parte AND ROWNUM = 1");
		sql.append("  ) AS tipo_parte_intimada_recurso");       
		sql.append(" FROM");
						
	    /*
	     * Subconsulta 3o nivel - responsável em buscar as pendências em PEND e PEND_FINAL, com tipo INTIMACAO E CARTA_CITACAO, status CUMPRIDA, onde tem advogado
	     * Obs: começar analisar o SQL por aqui e ir subindo de nível
	     */
		sql.append(" ( ");
		sql.append("   SELECT p.id_pend, p.id_pend_tipo, p.id_pend_status, p.data_inicio, p.data_fim, p.id_proc, p.id_proc_parte, pr.id_usu_resp, ma.id_arq, p.id_movi, pr.id_serv, pt.pend_tipo_codigo, ma.id_movi_arq");
		sql.append("   FROM projudi.pend p");
		sql.append("   JOIN projudi.pend_resp pr ON pr.id_pend = p.id_pend");
		sql.append("   JOIN projudi.pend_tipo pt ON pt.id_pend_tipo = p.id_pend_tipo");
		sql.append("   LEFT JOIN projudi.movi_arq ma ON ma.id_movi = p.id_movi AND ma.id_movi_arq_acesso IN (" + MovimentacaoArquivoDt.ACESSO_NORMAL + "," + MovimentacaoArquivoDt.ACESSO_PUBLICO + ")");
		sql.append("   WHERE p.id_pend_tipo IN (" + PendenciaTipoDt.INTIMACAO  + "," + PendenciaTipoDt.CARTA_CITACAO + ")");
		sql.append("    AND p.id_pend_status = " + PendenciaStatusDt.ID_CUMPRIDA);
		sql.append("    AND p.id_proc_parte IS NOT NULL");
		sql.append("    AND p.DATA_INICIO BETWEEN ? AND ?"); 
		ps.adicionarDateTime(dataInicial); 
		ps.adicionarDateTime(dataFinal);
		sql.append("    AND (p.id_pend_pai IS NULL OR EXISTS (");
		sql.append(" 		SELECT p2.id_pend FROM projudi.pend_final p2 JOIN projudi.pend_tipo pt2 ON pt2.id_pend_tipo = p2.id_pend_tipo");
		sql.append(" 		WHERE p2.id_pend = p.id_pend_pai AND pt2.pend_tipo_codigo NOT IN (" + PendenciaTipoDt.INTIMACAO + "," + PendenciaTipoDt.CARTA_CITACAO + ")))");		
		sql.append("   UNION ALL");
		sql.append("   SELECT pf.id_pend, pf.id_pend_tipo, pf.id_pend_status, pf.data_inicio, pf.data_fim, pf.id_proc, pf.id_proc_parte, prf.id_usu_resp, ma.id_arq, pf.id_movi, prf.id_serv, ptf.pend_tipo_codigo, ma.id_movi_arq");
		sql.append("   FROM projudi.pend_final pf");
		sql.append("   JOIN projudi.pend_final_resp prf ON prf.id_pend = pf.id_pend");
		sql.append("   JOIN projudi.pend_tipo ptf ON ptf.id_pend_tipo = pf.id_pend_tipo");	
		sql.append("   LEFT JOIN projudi.movi_arq ma ON ma.id_movi = pf.id_movi AND ma.id_movi_arq_acesso IN (" + MovimentacaoArquivoDt.ACESSO_NORMAL + "," + MovimentacaoArquivoDt.ACESSO_PUBLICO + ")");		
		sql.append("   WHERE pf.id_pend_tipo IN (" + PendenciaTipoDt.INTIMACAO  + "," + PendenciaTipoDt.CARTA_CITACAO + ")");
		sql.append("    AND pf.id_pend_status = " + PendenciaStatusDt.ID_CUMPRIDA);
		sql.append("    AND pf.id_proc_parte IS NOT NULL");
		sql.append("    AND pf.DATA_INICIO BETWEEN ? AND ?");
		ps.adicionarDateTime(dataInicial); 
		ps.adicionarDateTime(dataFinal);
		sql.append("    AND (pf.id_pend_pai IS NULL OR EXISTS ( ");
		sql.append(" 		SELECT pf2.id_pend FROM projudi.pend_final pf2 JOIN projudi.pend_tipo ptf2 ON ptf2.id_pend_tipo = pf2.id_pend_tipo");
		sql.append(" 		WHERE pf2.id_pend = pf.id_pend_pai AND ptf2.pend_tipo_codigo NOT IN (" + PendenciaTipoDt.INTIMACAO + "," + PendenciaTipoDt.CARTA_CITACAO + ")))");
		sql.append(" ) pf");		
		// Final da subconsulta 3o nível ---		
		sql.append(" JOIN projudi.usu_serv us ON us.id_usu_serv = pf.id_usu_resp");
		sql.append(" JOIN projudi.usu_serv_oab oab ON oab.id_usu_serv = us.id_usu_serv");
		sql.append(" JOIN projudi.usu u ON u.id_usu = us.id_usu");
		sql.append(" LEFT JOIN projudi.serv serv ON serv.id_serv = us.id_serv");
		sql.append(" LEFT JOIN projudi.estado est ON est.id_estado = serv.id_estado_representacao");
		sql.append(" JOIN projudi.usu_serv_grupo usg ON us.id_usu_serv = usg.id_usu_serv");
		sql.append(" JOIN projudi.grupo g ON usg.id_grupo = g.id_grupo AND g.grupo_codigo IN (" + GrupoDt.ADVOGADO_PARTICULAR + "," + GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO + ")");		
		sql.append(" JOIN projudi.proc_parte pp ON pp.id_proc_parte = pf.id_proc_parte");
		sql.append(" JOIN projudi.proc_parte_tipo ppt ON ppt.id_proc_parte_tipo = pp.id_proc_parte_tipo");
		sql.append(" LEFT JOIN projudi.arq a ON pf.id_arq = a.id_arq and a.usu_assinador is not null and exists (select ta.dje from projudi.arq_tipo ta WHERE a.id_arq_tipo = ta.id_arq_tipo AND ta.dje = 1)");
		sql.append(" LEFT JOIN projudi.arq_tipo ta ON a.id_arq_tipo = ta.id_arq_tipo and ta.dje = 1");
		sql.append(" WHERE EXISTS (SELECT ppa.id_proc_parte FROM projudi.proc_parte_advogado ppa WHERE ppa.id_proc_parte = pp.id_proc_parte AND (ppa.dativo = 0 OR ppa.dativo IS NULL))");
		sql.append(" ) vw");
		 // Final da subconsulta 2o nível ---
		sql.append(" JOIN projudi.proc p ON vw.id_proc = p.id_proc");
		sql.append(" JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = p.id_proc_tipo");
		sql.append(" JOIN projudi.proc_status ps ON ps.id_proc_status = p.id_proc_status and ps.proc_status_codigo <> " + ProcessoStatusDt.SIGILOSO);
		sql.append(" LEFT JOIN projudi.recurso r on r.id_proc = p.id_proc AND r.id_serv_recurso = p.id_serv and r.data_recebimento IS NOT NULL AND r.data_retorno IS NULL");
		sql.append(" LEFT JOIN projudi.proc_tipo ptr ON ptr.id_proc_tipo = r.id_proc_tipo");
		sql.append(" JOIN projudi.serv s ON p.id_serv = s.id_serv");
		sql.append(" JOIN projudi.serv_subtipo ss ON s.id_serv_subtipo = ss.id_serv_subtipo");
		sql.append(" JOIN projudi.comarca c ON c.id_comarca = s.id_comarca");
		sql.append(" WHERE 1=1 and p.id_proc= 166898");
		
		switch (opcao){
			case CODIGO_OPCAO_PUBLICACAO_2_GRAU:			
				sql.append(" AND c.comarca_codigo IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
				sql.append(" AND ss.serv_subtipo_codigo IN (?,?,?,?,?,?)");
				ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL); 
				ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
				ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
				ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
				ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA); 
				ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);				
				break;
			case CODIGO_OPCAO_PUBLICACAO_1_GRAU_CAPITAL:				
				sql.append(" AND c.comarca_codigo IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
				sql.append(" AND ss.serv_subtipo_codigo NOT IN (?,?,?,?,?,?)");
				ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL); 
				ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
				ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
				ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
				ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA); 
				ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
				break;
			case CODIGO_OPCAO_PUBLICACAO_1_GRAU_INTERIOR:
				sql.append(" AND c.comarca_codigo NOT IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
				break;
		}
				
		sql.append(" ORDER BY p.id_proc, vw.id_pend");
		
		List<DiarioJusticaEletronicoDt> lista = new ArrayList<>();
		
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				DiarioJusticaEletronicoDt dados = new DiarioJusticaEletronicoDt();
				dados.setProcessoId(rs.getString("ID_PROC"));
				dados.setProcessoNumero(rs.getString("PROC_NUMERO"));
				dados.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				dados.setForumCodigo(rs.getString("FORUM_CODIGO"));
				dados.setAno(rs.getString("ANO"));
				dados.setSegredoJustica(rs.getString("SEGREDO_JUSTICA"));
				dados.setServentia(rs.getString("SERV"));
				dados.setClasseCnjId(rs.getString("ID_CNJ_CLASSE"));
				dados.setClasseCnjRecursoId(rs.getString("ID_CNJ_CLASSE_RECURSO"));
				dados.setPendId(rs.getString("ID_PEND"));
				dados.setPendCodigo(rs.getString("PEND_TIPO_CODIGO"));
				dados.setMoviId(rs.getString("ID_MOVI"));
				dados.setMoviArqId(rs.getString("ID_MOVI_ARQ"));
				dados.setDataInicio(rs.getString("DATA_INICIO"));
				dados.setDataFim(rs.getString("DATA_FIM"));
				dados.setParteId(rs.getString("ID_PARTE_INTIMADA"));
				dados.setNomeParte(rs.getString("NOME_PARTE_INTIMADA"));
				dados.setCpfCnpjParte(rs.getString("CPF_CNPJ_PARTE_INTIMADA"));
				dados.setPoloParte(rs.getString("POLO_PARTE_INTIMADA"));
				dados.setNomeAdvogado(rs.getString("NOME_ADVOGADO_INTIMADO"));
				dados.setOabAdvogado(rs.getString("OAB_ADVOGADO_INTIMADO"));
				dados.setUfOabAdvogado(rs.getString("UF_OAB_ADVOGADO_INTIMADO"));
				dados.setArqId(rs.getString("ID_ARQ"));
				dados.setTipoArq(rs.getString("ARQ_TIPO"));
				dados.setArqTipoCodigo(rs.getString("ARQ_TIPO_CODIGO"));
				dados.setVotoArqId(rs.getString("ID_ARQ_VOTO"));
				dados.setVotoMoviArqId(rs.getString("ID_MOVI_ARQ_VOTO"));
				lista.add(dados);
			}
			
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		
		return lista;
	}
	
}

