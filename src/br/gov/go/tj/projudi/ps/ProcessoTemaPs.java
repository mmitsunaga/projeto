package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBElement;

import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTemaDt;
import br.gov.go.tj.projudi.dt.TemaDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.ValidacaoUtil;
import br.jus.cnj.bnpr.ObjectFactory;
import br.jus.cnj.bnpr.TipoProcessoSobrestado;
import br.jus.cnj.bnpr.model.Tupla;

public class ProcessoTemaPs extends ProcessoTemaPsGen{

	private static final long serialVersionUID = -1421878193766057374L;

	public ProcessoTemaPs(Connection conexao){
		Conexao = conexao;
	}
	
	/**
	 * Consulta os temas relacionados a um processo, através do id do processo.
	 * @param idProcesso
	 * @return Lista de TemasDt com id, codigo, titulo, situação, data de sobrestamento e data de transito
	 * @throws Exception
	 */
	public List<ProcessoTemaDt> consultarTemasProcessoPorIdProcesso (String idProcesso, UsuarioNe usuarioSessao) throws Exception {

		String stSql="";
		List<ProcessoTemaDt> liTemp = new ArrayList<>();
		ResultSetTJGO rs=null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_TEMA WHERE DATA_SOBRESTADO_FINAL IS NULL AND ID_PROC = ? ORDER BY DATA_SOBRESTADO";		
		ps.adicionarLong(idProcesso); 
		
		try {
			rs = consultar(stSql, ps);
			while (rs.next()) {
				ProcessoTemaDt obTemp = new ProcessoTemaDt();
				obTemp.setId(rs.getString("ID_PROC_TEMA"));
				obTemp.setId_Tema(rs.getString("ID_TEMA"));
				obTemp.setTemaCodigo(rs.getString("TEMA_CODIGO"));
				obTemp.setTitulo(rs.getString("TITULO"));
				obTemp.setId_Proc(rs.getString("ID_PROC"));
				obTemp.setProcNumero(rs.getString("PROC_NUMERO"));
				obTemp.setTemaOrigem(rs.getString("TEMA_ORIGEM"));
				obTemp.setTemaSituacao(rs.getString("TEMA_SITUACAO"));
				obTemp.setTemaSituacaoCnj(rs.getString("TEMA_SITUACAO_CNJ"));
				obTemp.setTemaTipo(rs.getString("TEMA_TIPO"));
				obTemp.setTemaTipoCnj(rs.getString("TEMA_TIPO_CNJ"));
				obTemp.setAplicaTese(rs.getString("APLICA_TESE"));
				obTemp.setJulgaMerito(rs.getString("JULGA_MERITO"));
				
				if (ValidacaoUtil.isNaoNulo(rs.getDateTime("DATA_SOBRESTADO"))){
					obTemp.setDataSobrestado(Funcoes.FormatarData(rs.getDateTime("DATA_SOBRESTADO")));
				}
				
				if (ValidacaoUtil.isNaoNulo(rs.getDateTime("DATA_SOBRESTADO_FINAL"))){					
					obTemp.setDataFinalSobrestado(Funcoes.FormatarData(rs.getDateTime("DATA_SOBRESTADO_FINAL")));
				}
				
				obTemp.setTema(new TemaDt());
				obTemp.getTema().setOpcaoProcessual(rs.getString("INFO_PROCESSUAL"));
				if (ValidacaoUtil.isNaoNulo(rs.getDate("DATA_TRANSITO"))){
					obTemp.getTema().setDataTransito(Funcoes.dateToStringSoData(rs.getDate("DATA_TRANSITO")));
				}
				
				if (ValidacaoUtil.isNaoNulo(usuarioSessao)){
					obTemp.setId_UsuarioLog(usuarioSessao.getId_Usuario());
					obTemp.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
				}
				
				liTemp.add(obTemp);
			}
			
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
	
	public void alterar(ProcessoTemaDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PROC_TEMA SET  ";
		stSql+= "ID_TEMA = ?";
		ps.adicionarLong(dados.getId_Tema());  
		stSql+= ",ID_PROC = ?";		 
		ps.adicionarLong(dados.getId_Proc());
						
		if (ValidacaoUtil.isNaoVazio(dados.getMotivo())){
			stSql+= ",MOTIVO = ?";
			ps.adicionarString(dados.getMotivo());
		}		
		if (ValidacaoUtil.isNaoVazio(dados.getDataFinalSobrestado())){
			stSql+= ",DATA_SOBRESTADO_FINAL = ?";			
			ps.adicionarDate(dados.getDataFinalSobrestado());
		}		
		if (ValidacaoUtil.isNaoVazio(dados.getId_Usuario())){
			stSql+= ",ID_USU = ?";
			ps.adicionarLong(dados.getId_Usuario());
		}
		
		stSql += " WHERE ID_PROC_TEMA  = ? ";
		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	} 
	
	public void inserir(ProcessoTemaDt dados ) throws Exception {

		String stSqlCampos="";
		String stSqlValores="";
		String stSql="";
		String stVirgula="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSqlCampos= "INSERT INTO PROJUDI.PROC_TEMA ("; 

		stSqlValores +=  " Values (";
 
		if ((dados.getId_Tema().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_TEMA " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Tema());  

			stVirgula=",";
		}
		if ((dados.getProcNumero().length()>0)) {
			 stSqlCampos+=   stVirgula + "ID_PROC " ;
			 stSqlValores+=   stVirgula + "? " ;
			 ps.adicionarLong(dados.getId_Proc());  

			stVirgula=",";
		}
		
		stSqlCampos += ",DATA_SOBRESTADO";
		stSqlValores += ",?";
		ps.adicionarDateTime(new Date());
		
		stSqlCampos+= ")";
		stSqlValores+= ")";
		stSql+= stSqlCampos + stSqlValores; 

		dados.setId(executarInsert(stSql,"ID_PROC_TEMA",ps));		 
	}

	public List consultarProcessosTema(String idTema, String id_TemaOrigem, String id_TemaSituacao, String id_TemaTipo, String temaCodigo, String id_Serv) throws Exception {
		String stSql="";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql = "SELECT * FROM PROJUDI.VIEW_PROC_TEMA vw WHERE vw.DATA_SOBRESTADO_FINAL IS NULL";
		if(idTema.length() > 0) {
			stSql += " AND vw.ID_TEMA = ? ";
			ps.adicionarLong(idTema);
		}
		if(id_TemaOrigem.length() > 0) {
			stSql += " AND vw.ID_TEMA_ORIGEM = ? ";
			ps.adicionarLong(id_TemaOrigem);
		}
		if(id_TemaSituacao.length() > 0) {
			stSql += " AND vw.ID_TEMA_SITUACAO = ? ";	
			ps.adicionarLong(id_TemaSituacao);
		}
		if(id_TemaTipo.length() > 0) {
			stSql += " AND vw.ID_TEMA_TIPO = ? "; 		
			ps.adicionarLong(id_TemaTipo);
		}
		if(temaCodigo.length() > 0) {
			stSql += " AND vw.TEMA_CODIGO = ? ";			
			ps.adicionarString(temaCodigo);			
		}
		if (id_Serv.length() > 0){
			stSql += " AND EXISTS (SELECT 1 FROM projudi.PROC p WHERE vw.id_proc = p.id_proc and p.id_serv = ?)";
			ps.adicionarString(id_Serv);
		}
		
		stSql += " ORDER BY vw.DATA_SOBRESTADO";

		try{

			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				ProcessoTemaDt obTemp = new ProcessoTemaDt();
				obTemp.setId_Proc(rs1.getString("ID_PROC"));
				obTemp.setProcNumero(rs1.getString("PROC_NUMERO"));
				obTemp.setDataSobrestado(Funcoes.dateToStringSoData(rs1.getDateTime("DATA_SOBRESTADO")));
				obTemp.setTemaOrigem(rs1.getString("TEMA_ORIGEM"));
				obTemp.setTemaSituacao(rs1.getString("TEMA_SITUACAO"));
				obTemp.setTemaTipo(rs1.getString("TEMA_TIPO"));
				listaProcessos.add(obTemp);
			}
			
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos; 
	} 
	
	/**
	 * Lista os processos sobrestados de um período
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 * @throws Exception
	 */	
	public List<Tupla> listarProcessosSobrestadosPorPeriodo(Date dataInicial, Date dataFinal) throws Exception {
		JAXBElement<String> situacaoSobrestamentoSIM = new ObjectFactory().createTipoProcessoSobrestadoSituacaoSobrestamento("S");
		List<Tupla> lista = new ArrayList<>();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT t.tema_codigo");
		sql.append(" , tt.tema_tipo_cnj");
		sql.append(" , tor.tema_origem");
		sql.append(" , ptp.id_cnj_classe as classe_cnj");
		sql.append(" , lpad(p.proc_numero, 7, 0) || '.' || lpad(p.digito_verificador, 2, 0) || '.' || lpad(p.ano, 4, 0) || '.8.09.' || lpad(p.forum_codigo, 4, 0) as proc_numero");		
		sql.append(" , NULL AS data_admissao");			
		sql.append(" , pt.data_sobrestado AS dataSobrestado");
		sql.append(" , p.data_arquivamento AS dataArquivamento");
		sql.append(" , p.data_recebimento AS dataDistribuicao");		
		sql.append(" , p.data_transito_julgado AS dataTransitoJulgado");
		sql.append(" , s.serv");
		sql.append(" FROM proc_tema pt ");
		sql.append(" JOIN proc p on pt.id_proc = p.id_proc");
		sql.append(" JOIN proc_status ps on p.id_proc_status = ps.id_proc_status and ps.proc_status_codigo = " + ProcessoStatusDt.SUSPENSO);
		sql.append(" JOIN proc_tipo ptp on ptp.id_proc_tipo = p.id_proc_tipo");
		sql.append(" JOIN tema t on pt.id_tema = t.id_tema");
		sql.append(" LEFT JOIN tema_origem tor on tor.id_tema_origem = t.id_tema_origem");
		sql.append(" LEFT JOIN tema_tipo tt on tt.id_tema_tipo = t.id_tema_tipo");
		sql.append(" LEFT JOIN serv s on s.id_serv = p.id_serv");
		sql.append(" WHERE pt.data_sobrestado_final IS NULL");
		//sql.append(" AND ptp.t1.ATIVO = ? ");											ps.adicionarLong(1);
		if (dataInicial != null && dataFinal != null){
			sql.append(" AND pt.data_sobrestado BETWEEN ? AND ?");
			ps.adicionarDateTime(dataInicial);
			ps.adicionarDateTime(dataFinal);
		}		
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				TipoProcessoSobrestado processo = new TipoProcessoSobrestado();
				processo.setClasse(rs.getInt("classe_cnj"));
				processo.setNumero(rs.getString("proc_numero"));
				processo.setDataJulgamento(null);
				processo.setDataSobrestamento(Funcoes.asXMLGregorianCalendar(rs.getDate("dataSobrestado")));
				if (ValidacaoUtil.isNaoNulo(rs.getDate("dataDistribuicao"))){
					if (ValidacaoUtil.isNaoNulo(rs.getDate("dataSobrestado"))){
						if (rs.getDate("dataDistribuicao").before(rs.getDate("dataSobrestado"))){
							processo.setDataDistribuicao(Funcoes.asXMLGregorianCalendar(rs.getDate("dataDistribuicao")));
						}
					}
				}
				if (ValidacaoUtil.isNaoNulo(rs.getDate("dataTransitoJulgado"))){
					if (ValidacaoUtil.isNaoNulo(rs.getDate("dataSobrestado"))){
						if (rs.getDate("dataTransitoJulgado").after(rs.getDate("dataSobrestado"))){
							processo.setDataTransitoJulgado(Funcoes.asXMLGregorianCalendar(rs.getDate("dataTransitoJulgado")));
						}
					}
				}
				if (ValidacaoUtil.isNaoNulo(rs.getDate("dataArquivamento"))){
					if (ValidacaoUtil.isNaoNulo(rs.getDate("dataTransitoJulgado"))){
						if (rs.getDate("dataArquivamento").after(rs.getDate("dataTransitoJulgado"))){
							processo.setDataBaixa(Funcoes.asXMLGregorianCalendar(rs.getDate("dataArquivamento")));
						}						
					}
				}
				processo.setOrgaoJulgador(rs.getString("serv"));
				processo.setSituacaoSobrestamento(situacaoSobrestamentoSIM);
				lista.add(new Tupla(String.valueOf(rs.getLong("tema_codigo")), rs.getString("tema_tipo_cnj"), rs.getString("tema_origem"), processo));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return lista;
	}
	
	/**
	 * 
	 * @param dataOntem
	 * @return
	 * @throws Exception
	 */
	public List<Tupla> listarProcessosNaoSobrestadosDiaAnterior(Date dataOntem) throws Exception {
		JAXBElement<String> situacaoSobrestamentoNAO = new ObjectFactory().createTipoProcessoSobrestadoSituacaoSobrestamento("N");
		List<Tupla> lista = new ArrayList<>();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT t.tema_codigo");
		sql.append(" , tt.tema_tipo_cnj");
		sql.append(" , tor.tema_origem");
		sql.append(" , ptp.id_cnj_classe as classe_cnj");
		sql.append(" , lpad(p.proc_numero, 7, 0) || '.' || lpad(p.digito_verificador, 2, 0) || '.' || lpad(p.ano, 4, 0) || '.8.09.' || lpad(p.forum_codigo, 4, 0) as proc_numero");		
		sql.append(" , NULL AS data_admissao");			
		sql.append(" , pt.data_sobrestado AS dataSobrestado");
		sql.append(" , p.data_arquivamento AS dataArquivamento");
		sql.append(" , p.data_recebimento AS dataDistribuicao");		
		sql.append(" , p.data_transito_julgado AS dataTransitoJulgado");
		sql.append(" , s.serv");
		sql.append(" , case when pt.aplica_tese = '1' then t.data_transito when pt.julga_merito = '1' then pt.data_sobrestado_final END AS dataJulgamento");
		sql.append(" , pt.aplica_tese");
		sql.append(" , pt.julga_merito");
		sql.append(" FROM proc_tema pt ");
		sql.append(" JOIN proc p on pt.id_proc = p.id_proc");		
		sql.append(" JOIN proc_tipo ptp on ptp.id_proc_tipo = p.id_proc_tipo");
		sql.append(" JOIN tema t on pt.id_tema = t.id_tema");
		sql.append(" LEFT JOIN tema_origem tor on tor.id_tema_origem = t.id_tema_origem");
		sql.append(" LEFT JOIN tema_tipo tt on tt.id_tema_tipo = t.id_tema_tipo");
		sql.append(" LEFT JOIN serv s on s.id_serv = p.id_serv");
		sql.append(" WHERE pt.data_sobrestado_final IS NOT NULL AND motivo IS NULL");
//		sql.append(" AND ptp.id_cnj_classe IS NOT NULL");
		sql.append(" AND pt.data_sobrestado_final between ? AND ?");
		ps.adicionarDateTime(Funcoes.calculePrimeraHora(dataOntem));
		ps.adicionarDateTime(Funcoes.calculeUltimaHora(dataOntem)); // to_date('24/09/2018 00:00:00', 'dd/mm/yyyy HH24:MI:SS') 
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				TipoProcessoSobrestado processo = new TipoProcessoSobrestado();
				processo.setClasse(rs.getInt("classe_cnj"));
				processo.setNumero(rs.getString("proc_numero"));
				processo.setDataBaixa(Funcoes.asXMLGregorianCalendar(rs.getDate("dataArquivamento")));
				processo.setDataDistribuicao(Funcoes.asXMLGregorianCalendar(rs.getDate("dataDistribuicao")));
				processo.setDataSobrestamento(Funcoes.asXMLGregorianCalendar(rs.getDate("dataSobrestado")));
				processo.setDataTransitoJulgado(Funcoes.asXMLGregorianCalendar(rs.getDate("dataTransitoJulgado")));
				processo.setOrgaoJulgador(rs.getString("serv"));
				processo.setSituacaoSobrestamento(situacaoSobrestamentoNAO);
				processo.setDataJulgamento(Funcoes.asXMLGregorianCalendar(rs.getDate("dataJulgamento")));
				processo.setJulgadoAplicadaTese(new ObjectFactory().createTipoProcessoSobrestadoJulgadoAplicadaTese(rs.getString("aplica_tese").equals("1") ? "S" : "N"));
				processo.setJulgadoMerito(new ObjectFactory().createTipoProcessoSobrestadoJulgadoMerito(rs.getString("julga_merito").equals("1") ? "S" : "N"));
				lista.add(new Tupla(String.valueOf(rs.getLong("tema_codigo")), rs.getString("tema_tipo_cnj"), rs.getString("tema_origem"), processo));
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return lista;
	}
	
	/**
	 * Lista os processos sobrestados do SSG
	 * @return
	 * @throws Exception
	 */
	public List<Tupla> listarProcessosSobrestados_SSG() throws Exception {
		JAXBElement<String> situacaoSobrestamentoSIM = new ObjectFactory().createTipoProcessoSobrestadoSituacaoSobrestamento("S");
		List<Tupla> lista = new ArrayList<>();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM V_SSGATEXTOCNJ");
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				TipoProcessoSobrestado processo = new TipoProcessoSobrestado();
				if (ValidacaoUtil.isNaoVazio(rs.getString("codigo_classe"))){
					processo.setClasse(Integer.valueOf(rs.getString("codigo_classe").trim()));
				}
				if (ValidacaoUtil.isNaoVazio(rs.getString("numero_processo"))){
					processo.setNumero(rs.getString("numero_processo").trim());
				}				
				processo.setDataJulgamento(null);
				processo.setDataBaixa(Funcoes.asXMLGregorianCalendar(rs.getString("data_arquivamento")));
				processo.setDataDistribuicao(Funcoes.asXMLGregorianCalendar(rs.getString("data_distribuicao")));
				processo.setDataSobrestamento(Funcoes.asXMLGregorianCalendar(rs.getString("data_sobrestamento")));
				processo.setDataTransitoJulgado(Funcoes.asXMLGregorianCalendar(rs.getString("data_transito")));
				processo.setOrgaoJulgador("TJGO");
				processo.setSituacaoSobrestamento(situacaoSobrestamentoSIM);
				lista.add(new Tupla(rs.getString("codigo_tema").trim(), rs.getString("tipo_tema").trim(), rs.getString("origem_tema").trim(), processo));				
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}			
		}
		return lista;
	}
	
	/**
	 * Consulta os temas que estão relacionados com um processo e que ainda não estão
	 * em transito julgado ou cancelados. Essa lista de temas será submetida ao CNJ
	 * para verificar se houve mudança de situação
	 * @return
	 */
	public List<ProcessoTemaDt> consultarProcessoTemaComSituacaoTransitadoOuCancelado() throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<ProcessoTemaDt> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT vw.id_tema, vw.tema_codigo, vw.tema_origem, vw.tema_tipo_cnj, vw.tema_situacao");
		sql.append(" FROM PROJUDI.VIEW_PROC_TEMA vw");
		sql.append(" WHERE vw.tema_origem != 'TJGO' AND vw.DATA_SOBRESTADO_FINAL IS NULL");
		sql.append(" AND (vw.tema_situacao_cnj != 'TRANSITO_JULGADO' AND vw.tema_situacao_cnj != 'TRANSITADO_JULGADO' AND vw.tema_situacao_cnj != 'CANCELADO')");
		sql.append(" GROUP BY vw.id_tema, vw.tema_codigo, vw.tema_origem, vw.tema_tipo_cnj, vw.tema_situacao");
		sql.append(" ORDER BY vw.tema_codigo desc");
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				ProcessoTemaDt obTemp = new ProcessoTemaDt();
				obTemp.setTema(new TemaDt());
				obTemp.setId_Tema(rs.getString("ID_TEMA"));
				obTemp.getTema().setTemaCodigo(rs.getString("TEMA_CODIGO"));
				obTemp.getTema().setTemaOrigem(rs.getString("TEMA_ORIGEM"));
				obTemp.getTema().setTemaTipoCnj(rs.getString("TEMA_TIPO_CNJ"));
				obTemp.getTema().setTemaSituacao(rs.getString("TEMA_SITUACAO"));
				lista.add(obTemp);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return lista;
	}
	
	/**
	 * Consulta os processos com status = SUSPENSO e com TEMA associado; Todos os temas devem estar em transito julgado;
	 * Não deve ter a pendencia VERIFICAR PROCESSO COM TEMA TRANSITADO JULGADO
	 * @return
	 * @throws Exception
	 */
	public List<ProcessoDt> consultarProcessoComTodosTemasTransitadoJulgado() throws Exception {
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List<ProcessoDt> lista = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT p.id_proc, p.proc_numero, p.ano, p.forum_codigo, p.digito_verificador, p.id_proc_prior, s.id_serv, s.serv, max(data_realizacao) as data_sobrestamento");
		sql.append(" FROM projudi.proc p");
		sql.append(" INNER JOIN serv s ON p.id_serv = s.id_serv");
		sql.append(" INNER JOIN projudi.proc_tema pt ON pt.id_proc = p.id_proc AND pt.data_sobrestado_final IS NULL");
		sql.append(" INNER JOIN projudi.proc_status ps ON ps.id_proc_status = p.id_proc_status AND ps.proc_status_codigo = " + ProcessoStatusDt.SUSPENSO);
		sql.append(" INNER JOIN projudi.movi m ON m.id_proc = p.id_proc");
		sql.append(" INNER JOIN projudi.movi_tipo mt ON m.id_movi_tipo = mt.id_movi_tipo and mt.movi_tipo_codigo = " + MovimentacaoTipoDt.PROCESSO_SUSPENSO);
		sql.append(" LEFT JOIN ( SELECT vw.id_proc, vw.id_proc_tema");
		sql.append(" 		     FROM projudi.view_proc_tema vw ");
		sql.append(" 			 WHERE vw.data_sobrestado_final IS NULL");
		sql.append(" 			 AND (vw.tema_situacao_cnj = 'TRANSITO_JULGADO' OR vw.tema_situacao_cnj = 'TRANSITADO_JULGADO' OR vw.tema_situacao_cnj = 'CANCELADO')");
		sql.append(" ) vw ON vw.id_proc = p.id_proc");
		sql.append(" GROUP BY p.id_proc, p.proc_numero, p.ano, p.forum_codigo, p.digito_verificador, p.id_proc_prior, s.id_serv, s.serv");
		sql.append(" HAVING COUNT(distinct pt.id_proc_tema) = COUNT(distinct vw.id_proc_tema)");
		sql.append(" AND NOT EXISTS (");
		sql.append("  SELECT pe.id_pend FROM projudi.pend pe");
		sql.append("  INNER JOIN projudi.pend_tipo pt ON pe.id_pend_tipo = pt.id_pend_tipo");
		sql.append("  AND pt.pend_tipo_codigo = " + PendenciaTipoDt.VERIFICAR_TEMA_TRANSITADO_JULGADO + " WHERE pe.id_proc = p.id_proc");
		sql.append(" )");
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				ProcessoDt obTemp = new ProcessoDt();
				obTemp.setId_Processo(rs.getString("ID_PROC"));
				obTemp.setProcessoNumero(rs.getString("PROC_NUMERO"));
				obTemp.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				obTemp.setAno(rs.getString("ANO"));
				obTemp.setForumCodigo(rs.getString("FORUM_CODIGO"));
				obTemp.setId_ProcessoPrioridade(rs.getString("ID_PROC_PRIOR"));
				obTemp.setId_Serventia(rs.getString("ID_SERV"));
				obTemp.setServentia(rs.getString("SERV"));
				obTemp.setDataTransitoJulgado(Funcoes.FormatarDataHora(rs.getDateTime("DATA_SOBRESTAMENTO")));
				lista.add(obTemp);
			}
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return lista;		
	}
	
	/**
	 * Lista os processos tema que foram fechados pela movimentação 'Término Suspensão Processo'.
	 * Contém o campo data_sobrestado_final com a data de realização da movimentação referida.
	 * (histórico em tela)
	 * @param idProcesso
	 * @param usuarioSessao
	 * @return
	 * @throws Exception
	 */
	public List<ProcessoTemaDt> consultarTemasProcessoComDataSobrestadoFinalNaoNulo (String idProcesso) throws Exception {

		String stSql="";
		List<ProcessoTemaDt> liTemp = new ArrayList<>();
		ResultSetTJGO rs=null;		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT * FROM PROJUDI.VIEW_PROC_TEMA WHERE (DATA_SOBRESTADO_FINAL IS NOT NULL AND MOTIVO IS NULL) AND ID_PROC = ? ORDER BY DATA_SOBRESTADO";		
		ps.adicionarLong(idProcesso); 
		
		try {
			rs = consultar(stSql, ps);
			while (rs.next()) {
				ProcessoTemaDt obTemp = new ProcessoTemaDt();
				obTemp.setId(rs.getString("ID_PROC_TEMA"));
				obTemp.setId_Tema(rs.getString("ID_TEMA"));
				obTemp.setTemaCodigo(rs.getString("TEMA_CODIGO"));
				obTemp.setTitulo(rs.getString("TITULO"));
				obTemp.setId_Proc(rs.getString("ID_PROC"));
				obTemp.setProcNumero(rs.getString("PROC_NUMERO"));
				obTemp.setTemaOrigem(rs.getString("TEMA_ORIGEM"));
				obTemp.setTemaSituacao(rs.getString("TEMA_SITUACAO"));
				obTemp.setTemaSituacaoCnj(rs.getString("TEMA_SITUACAO_CNJ"));
				obTemp.setTemaTipo(rs.getString("TEMA_TIPO"));
				obTemp.setAplicaTese(rs.getString("APLICA_TESE"));
				obTemp.setJulgaMerito(rs.getString("JULGA_MERITO"));
				
				if (ValidacaoUtil.isNaoNulo(rs.getDateTime("DATA_SOBRESTADO"))){
					obTemp.setDataSobrestado(Funcoes.FormatarData(rs.getDateTime("DATA_SOBRESTADO")));
				}
				
				if (ValidacaoUtil.isNaoNulo(rs.getDateTime("DATA_SOBRESTADO_FINAL"))){					
					obTemp.setDataFinalSobrestado(Funcoes.FormatarData(rs.getDateTime("DATA_SOBRESTADO_FINAL")));
				}
				
				obTemp.setTema(new TemaDt());
				obTemp.getTema().setOpcaoProcessual(rs.getString("INFO_PROCESSUAL"));
				if (ValidacaoUtil.isNaoNulo(rs.getDate("DATA_TRANSITO"))){
					obTemp.getTema().setDataTransito(Funcoes.dateToStringSoData(rs.getDate("DATA_TRANSITO")));
				}
				
				liTemp.add(obTemp);
			}
			
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return liTemp; 
	}
	
	/**
	 * Altera a data de sobrestamento final para o dia de hoje
	 * @param id - identificador do processo
	 */
	public void alterarDataSobrestamentoFinalTemas(String id) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE projudi.PROC_TEMA SET data_sobrestado_final = SYSDATE WHERE data_sobrestado_final IS NULL AND id_proc = ?";		
		ps.adicionarLong(id);
		executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Altera os campos teseFirmada e JulgaMerito seguindo as seguintes regras:
	 * TeseFirmada = 1 -> JulgaMerito = 1 se infoProcessual != 1
	 */
	public void alterarJulgamentoMeritoQuandoTeseFirmadaAplicada(String id, String aplicaTese, String julgaMerito) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE projudi.PROC_TEMA SET aplica_tese = ? , julga_merito = ? WHERE id_proc_tema = ?";		
		ps.adicionarLong(aplicaTese);
		ps.adicionarLong(julgaMerito);
		ps.adicionarLong(id);
		executarUpdateDelete(sql, ps);
	}
	
}
