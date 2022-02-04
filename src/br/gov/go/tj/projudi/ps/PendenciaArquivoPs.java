package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class PendenciaArquivoPs extends PendenciaArquivoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = 8208802088539870887L;

    public PendenciaArquivoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Retorna os arquivos de resposta de uma determinada pendencia
	 * @author Ronneesley Moura Teles
	 * @since 15/01/2009 10:54
	 * @param pendenciaDt pojo da pendencia
	 * @param comArquivos se deseja que seja adicionado os dados do arquivo na pendencia
	 * @return lista de pendencias com arquivos
	 * @throws Exception
	 */
	public List consultarPendenciaResposta(PendenciaDt pendenciaDt, boolean comArquivos) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI." + (comArquivos ? "VIEW_PEND_ARQ_COMPLETA" : "VIEW_PEND_ARQ");
		sql += " WHERE ID_PEND = ? AND RESPOSTA = ?";
		ps.adicionarLong(pendenciaDt.getId());
		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);

				//Se e para vir com os dados do arquivo 
				if (comArquivos) {
					ArquivoDt arquivoDt = new ArquivoDt();

					this.associarArquivo(arquivoDt, rs1);

					pendenciaArquivoDt.setArquivoDt(arquivoDt);
				}

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	
	/**
	 * Retorna os arquivos de resposta de uma determinada pendencia
	 * @author Jesus Rodrigo
	 * @since 26/08/2014
	 * @param pendenciaDt pojo da pendencia
	 * @param comArquivos se deseja que seja adicionado os dados do arquivo na pendencia
	 * @return lista de pendencias com arquivos
	 * @throws Exception
	 */
	public List consultarPendenciaFinalizadaResposta(PendenciaDt pendenciaDt, boolean comArquivos) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI." + (comArquivos ? "VIEW_PEND_FINAL_ARQ_COMPLETA" : "VIEW_PEND_FINAL_ARQ");
		sql += " WHERE ID_PEND = ? AND RESPOSTA = ?";		ps.adicionarLong(pendenciaDt.getId());		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);

				//Se e para vir com os dados do arquivo 
				if (comArquivos) {
					ArquivoDt arquivoDt = new ArquivoDt();

					this.associarArquivo(arquivoDt, rs1);

					pendenciaArquivoDt.setArquivoDt(arquivoDt);
				}

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	
	// jvosantos - 29/10/2019 17:21 - Método para consultar PendenciaArquivoDt de pendencias finalizadas (comArquivos=true não parece funcionar)
	public List consultarPendenciaFinalizada(PendenciaDt pendenciaDt, boolean comArquivos) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI." + (comArquivos ? "VIEW_PEND_FINAL_ARQ_COMPLETA" : "VIEW_PEND_FINAL_ARQ");
		sql += " WHERE ID_PEND = ? ";		ps.adicionarLong(pendenciaDt.getId());

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);

				//Se e para vir com os dados do arquivo 
				if (comArquivos) {
					ArquivoDt arquivoDt = new ArquivoDt();

					this.associarArquivo(arquivoDt, rs1);

					pendenciaArquivoDt.setArquivoDt(arquivoDt);
				}

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}


	/**
	 * Retorna os arquivos de resposta de uma determinada conclusão
	 * @author msapaula
	 * @param id_Pendencia, id da pendencia
	 * @return lista de pendencias com arquivos
	 * @throws Exception
	 */
	public List consultarArquivosRespostaConclusao(String id_Pendencia) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_ARQ_PEND_COMPLETA";
		sql += " WHERE ID_PEND = ? AND RESPOSTA = ?";
		ps.adicionarLong(id_Pendencia);
		ps.adicionarLong(1);
		sql += " AND PEND_TIPO_CODIGO IN (?, ?, ?, ?, ?,?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);

				//Se e para vir com os dados do arquivo 
				ArquivoDt arquivoDt = new ArquivoDt();

				this.associarArquivo(arquivoDt, rs1);

				pendenciaArquivoDt.setArquivoDt(arquivoDt);

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}

	/**
	 * Retorna os arquivos de resposta de uma determinada conclusão
	 * @author Jesus Rodrigo Corrêa
	 * @param id_Pendencia, id da pendencia
	 * @return lista de pendencias com arquivos
	 * @throws Exception
	 * 26/08/2014
	 */
	public List consultarArquivosRespostaConclusaoFinalizada(String id_Pendencia) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_ARQ_PEND_FINAL_COMPLETA";
		sql += " WHERE ID_PEND = ? AND RESPOSTA = ?";		ps.adicionarLong(id_Pendencia);		ps.adicionarLong(1);
		sql += " AND PEND_TIPO_CODIGO IN (?, ?, ?, ?, ?,?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);

				//Se e para vir com os dados do arquivo 
				ArquivoDt arquivoDt = new ArquivoDt();

				this.associarArquivo(arquivoDt, rs1);

				pendenciaArquivoDt.setArquivoDt(arquivoDt);

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	/**
	 * Retorna os arquivos de uma determinada pendencia que sao assinados e sao o problema
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 14:37
	 * @param pendenciaDt pojo da pendencia
	 * @param comArquivos se deseja que seja adicionado os dados do arquivo na pendencia
	 * @return lista de arquivos assinados que sao o problema
	 * @throws Exception
	 */
	public List consultarArquivosAssinadosProblema(PendenciaDt pendenciaDt, boolean comArquivos) throws Exception{
		return this.consultarPendencia(pendenciaDt, null, comArquivos, true, true);
	}

	/**
	 * Retorna os arquivos de uma determinada pendencia que sao assinados
	 * @author Ronneesley Moura Teles
	 * @since 21/01/2009 16:35
	 * @param pendenciaDt pojo da pendencia
	 * @param comArquivos se deseja que seja adicionado os dados do arquivo na pendencia
	 * @return lista de arquivos
	 * @throws Exception
	 */
	public List consultarArquivosAssinados(PendenciaDt pendenciaDt, boolean comArquivos) throws Exception{
		return this.consultarPendencia(pendenciaDt, null, comArquivos, true, false);
	}

	/**
	 * Retorna os arquivos de uma determinada pendencia
	 * @author Ronneesley Moura Teles
	 * @since 21/01/2009 16:22
	 * @param pendenciaDt pojo da pendencia
	 * @param comArquivos se deseja que seja adicionado os dados do arquivo na pendencia
	 * @return lista de arquivos
	 * @throws Exception
	 */
	public List consultarPendencia(PendenciaDt pendenciaDt, boolean comArquivos) throws Exception{
		return this.consultarPendencia(pendenciaDt, null, comArquivos, false, false);
	}

	/**
	 * Consultar arquivos de resposta e assinados
	 * @author Ronneesley Moura Teles
	 * @since 05/02/2009 10:53
	 * @param pendenciaDt vo da pendencia
	 * @return lista de vinculos entre pendencia e arquivos
	 * @throws Exception
	 */
	public List consultarRespostaAssinados(PendenciaDt pendenciaDt) throws Exception{
		return this.consultarRespostaAssinados(pendenciaDt, false);
	}

	/**
	 * Consultar arquivos de resposta e assinados
	 * @author Ronneesley Moura Teles
	 * @since 04/02/2009 09:21
	 * @param pendenciaDt vo da pendencia
	 * @return lista de vinculos entre pendencia e arquivos
	 * @throws Exception
	 */
	public List consultarRespostaAssinados(PendenciaDt pendenciaDt, boolean comConteudo) throws Exception {
		List arquivos = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, ID_ARQ, NOME_ARQ, RESPOSTA, USU_ASSINADOR, DATA_INSERCAO, ARQ_TIPO, ARQ_TIPO_CODIGO, ID_ARQ_TIPO, RECIBO ";

		if (comConteudo) sql += ", ARQ ";

		sql += " FROM PROJUDI.VIEW_PEND_ARQ_COMPLETA WHERE ID_PEND = ? ";
		ps.adicionarLong(pendenciaDt.getId());
		sql += " AND RESPOSTA = ? AND USU_ASSINADOR is not null";
		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();

				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setResposta(Funcoes.FormatarLogico(rs1.getString("RESPOSTA")));

				ArquivoDt arquivoDt = new ArquivoDt();

				//Se nao for um arquivo de configuracao 
				if (!rs1.getString("ARQ_TIPO_CODIGO").trim().equals(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)) {
					arquivoDt.setId(rs1.getString("ID_ARQ"));
					arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
					arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
					arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
					arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
					arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
					arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
					if (arquivoDt.isRecibo()) arquivoDt.setAssinado(true);					

					//if (comConteudo) arquivoDt.setArquivo(new Compactar().descompactarBytes(rs1.getBytes("ARQ")));
					if (comConteudo) {
						arquivoDt.setArquivo(rs1.getBytes("ARQ"));
						//if (arquivoDt.isRecibo()) arquivoDt.setConteudoSemRecibo(rs1.getBytes("ARQ")); 
					}

					pendenciaArquivoDt.setArquivoDt(arquivoDt);
				}

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	
	/**
	 * Consultar arquivos anexados a uma publicação
	 * 
	 * @param pendenciaDt - dados da pendência
	 * @param comConteudo - com o conteudo do arquivo
	 * @return lista de vinculos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarArquivosAssinadosPublicacao(PendenciaDt pendenciaDt, boolean comConteudo) throws Exception {
		List arquivos = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, ID_ARQ, NOME_ARQ, RESPOSTA, USU_ASSINADOR, DATA_INSERCAO, ARQ_TIPO, ARQ_TIPO_CODIGO, ID_ARQ_TIPO, RECIBO, BLOQUEADO ";

		if (comConteudo) sql += ", ARQ ";

		sql += " FROM PROJUDI.VIEW_PEND_FINAL_ARQ_COMPLETA WHERE ID_PEND = ? ";
		ps.adicionarLong(pendenciaDt.getId());
		sql += " AND RESPOSTA = ? AND USU_ASSINADOR is not null";
		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();

				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setResposta(Funcoes.FormatarLogico(rs1.getString("RESPOSTA")));

				ArquivoDt arquivoDt = new ArquivoDt();

				//Se nao for um arquivo de configuracao 
				if (!rs1.getString("ARQ_TIPO_CODIGO").trim().equals(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)) {
					arquivoDt.setId(rs1.getString("ID_ARQ"));
					arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
					arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
					arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
					arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
					arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
					arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
					arquivoDt.setBloqueado(rs1.getString("BLOQUEADO"));
					if (arquivoDt.isRecibo()) arquivoDt.setAssinado(true);					

					//if (comConteudo) arquivoDt.setArquivo(new Compactar().descompactarBytes(rs1.getBytes("ARQ")));
					if (comConteudo) {
						arquivoDt.setArquivo(rs1.getBytes("ARQ"));
						//if (arquivoDt.isRecibo()) arquivoDt.setConteudoSemRecibo(rs1.getBytes("ARQ")); 
					}

					pendenciaArquivoDt.setArquivoDt(arquivoDt);
				}

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}

	/**
	 * Consultar arquivos de resposta
	 * @author Leandro Bernardes
	 * @since 03/09/2009
	 * @param pendenciaDt vo da pendencia
	 * @return lista de vinculos entre pendencia e arquivos
	 * @throws Exception
	 */
	public List consultarResposta(PendenciaDt pendenciaDt, boolean comConteudo) throws Exception {
		List arquivos = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, ID_ARQ, NOME_ARQ, RESPOSTA, ARQ_TIPO_CODIGO ";

		if (comConteudo) sql += ", ARQ ";

		sql += " FROM PROJUDI.VIEW_PEND_ARQ_COMPLETA WHERE ID_PEND = ? ";
		ps.adicionarLong(pendenciaDt.getId());
		sql += " AND RESPOSTA = ? ";
		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();

				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setResposta(Funcoes.FormatarLogico(rs1.getString("RESPOSTA")));

				ArquivoDt arquivoDt = new ArquivoDt();

				//Se nao for um arquivo de configuracao 
				if (!rs1.getString("ARQ_TIPO_CODIGO").trim().equals(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)) {
					arquivoDt.setId(rs1.getString("ID_ARQ"));
					arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));

					//if (comConteudo) arquivoDt.setArquivo(new Compactar().descompactarBytes(rs1.getBytes("ARQ")));
					if (comConteudo) arquivoDt.setArquivo(rs1.getBytes("ARQ"));

					pendenciaArquivoDt.setArquivoDt(arquivoDt);
				}

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}

	// jvosantos - 29/10/2019 13:22 - Tipar lista
	/**
	 * Retorna os arquivos de uma determinada pendencia
	 * @author Ronneesley Moura Teles
	 * @since 02/09/2008 10:59
	 * @param pendenciaDt pojo da pendencia
	 * @param comArquivos se deseja que seja adicionado os dados do arquivo na pendencia
	 * @param somenteAssinados se deseja somente os arquivos assinados
	 * @param somenteProblema somente arquivos que nao sao de resposta
	 * @return lista de arquivos
	 * @throws Exception
	 */
	public List consultarPendencia(PendenciaDt pendenciaDt, UsuarioNe usuarioNe, boolean comArquivos, boolean somenteAssinados, boolean somenteProblema) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder(); // jvosantos - 14/08/2019 14:29 - Usando StringBuilder

		sql.append(" SELECT ID_ARQ, NOME_ARQ, ");
		
		if(comArquivos) // jvosantos - 14/08/2019 14:29 - Correção de erro de SQL quando comArquivos é falso
			sql.append(" ARQ_TIPO_CODIGO, USU_ASSINADOR, DATA_INSERCAO, ARQ_TIPO, ID_ARQ_TIPO, ");
		
		sql.append(" ID_PEND_ARQ, ID_PEND, PEND_TIPO, RESPOSTA, RECIBO, CODIGO_TEMP ");
		sql.append(" FROM PROJUDI." + (comArquivos ? "VIEW_PEND_ARQ_COMPLETA" : "VIEW_PEND_ARQ"));

		sql.append(" WHERE ID_PEND = ? ");
		ps.adicionarLong(pendenciaDt.getId());

		if (somenteAssinados) sql.append(" AND USU_ASSINADOR is not null ");

		if (somenteProblema){
			sql.append(" AND RESPOSTA = ? ");
			ps.adicionarLong(1);
		}
		
		sql.append(" ORDER BY ID_ARQ ");

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql.toString(), ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				//this.associarDt(pendenciaArquivoDt, rs1);

				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				pendenciaArquivoDt.setId_Arquivo(rs1.getString("ID_ARQ"));
				pendenciaArquivoDt.setId_Pendencia(rs1.getString("ID_PEND"));
				pendenciaArquivoDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaArquivoDt.setResposta(Funcoes.FormatarLogico(rs1.getString("RESPOSTA")));
				pendenciaArquivoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));

				//Se e para vir com os dados do arquivo 
				if (comArquivos) {
					ArquivoDt arquivoDt = new ArquivoDt();

					//Se nao for um arquivo de configuracao 
					//if (!rs1.getString("ARQ_TIPO_CODIGO").trim().equals(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)) {
					arquivoDt.setId(rs1.getString("ID_ARQ"));
					arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
					arquivoDt.setArquivoTipoCodigo(rs1.getString("ARQ_TIPO_CODIGO"));
					arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
					arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
					arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
					arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
					if (usuarioNe != null) arquivoDt.setHash(usuarioNe.getCodigoHash(arquivoDt.getId()));
					arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));
					
					if (pendenciaDt.getPendenciaTipoCodigo() != null && pendenciaDt.getPendenciaTipoCodigo().length()>0 && pendenciaDt.getPendenciaTipoCodigo().equals(String.valueOf(PendenciaTipoDt.ELABORACAO_VOTO)))
						arquivoDt.setCodigoTemp(usuarioNe.getUsuarioDt().getNome());

					pendenciaArquivoDt.setArquivoDt(arquivoDt);
					//}
				}

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	
	/**
	 * Retorna os arquivos de uma determinada pendencia
	 * @param pendenciaDt pojo da pendencia
	 * @param comArquivos se deseja que seja adicionado os dados do arquivo na pendencia
	 * @param somenteAssinados se deseja somente os arquivos assinados
	 * @param somenteProblema somente arquivos que nao sao de resposta
	 * @return lista de arquivos
	 * @throws Exception
	 */
	public List consultarArquivosPendenciaFinalizada(PendenciaDt pendenciaDt, UsuarioNe usuarioNe, boolean comArquivos, boolean somenteAssinados, boolean somenteProblema) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_ARQ, ID_ARQ_TIPO, ARQ_TIPO, NOME_ARQ, DATA_INSERCAO, USU_ASSINADOR, ";
		sql += " ID_PEND_ARQ, ID_PEND, PEND_TIPO, RESPOSTA, ARQ_TIPO_CODIGO, RECIBO, CODIGO_TEMP ";
		sql += " FROM PROJUDI." + (comArquivos ? "VIEW_PEND_FINAL_ARQ_COMPLETA" : "VIEW_PEND_ARQ");

		sql += " WHERE ID_PEND = ? ";
		ps.adicionarLong(pendenciaDt.getId());

		if (somenteAssinados) sql += " AND USU_ASSINADOR is not null ";

		if (somenteProblema){
			sql += " AND RESPOSTA = ? ";
			ps.adicionarLong(1);
		}		

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				//this.associarDt(pendenciaArquivoDt, rs1);

				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				pendenciaArquivoDt.setId_Arquivo(rs1.getString("ID_ARQ"));
				pendenciaArquivoDt.setId_Pendencia(rs1.getString("ID_PEND"));
				pendenciaArquivoDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaArquivoDt.setResposta(Funcoes.FormatarLogico(rs1.getString("RESPOSTA")));
				pendenciaArquivoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));

				//Se e para vir com os dados do arquivo 
				if (comArquivos) {
					ArquivoDt arquivoDt = new ArquivoDt();

					//Se nao for um arquivo de configuracao 
					//if (!rs1.getString("ARQ_TIPO_CODIGO").trim().equals(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)) {
					arquivoDt.setId(rs1.getString("ID_ARQ"));
					arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
					arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
					arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
					arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
					arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
					if (usuarioNe != null) arquivoDt.setHash(usuarioNe.getCodigoHash(arquivoDt.getId()));
					arquivoDt.setRecibo(Funcoes.FormatarLogico(rs1.getString("RECIBO")));

					pendenciaArquivoDt.setArquivoDt(arquivoDt);
					//}
				}

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}

	/**
	 * Retorna o arquivo de configuração de uma pré-análise. Deve descompactar o arquivo.
	 * 
	 * @param id_Pendencia: pendência para a qual será pesquisada a pré-analise
	 * @return objeto PendenciaArquivoDt com os dados do arquivo de configuração
	 * @author msapaula
	 */
	public PendenciaArquivoDt getArquivoConfiguracaoPreAnalise(String id_Pendencia) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try{
			String sql = "SELECT * FROM PROJUDI.VIEW_CONFIGURACAO_PRE_ANALISE ";
			sql += " WHERE ID_PEND = ? ";
			ps.adicionarLong(id_Pendencia);

			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);

				//Seta dados do arquivo
				ArquivoDt arquivo = new ArquivoDt();
				arquivo.setId(rs1.getString("ID_ARQ"));
				arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				//arquivo.setArquivo(new Compactar().descompactarBytes(rs1.getBytes("ARQ")));
				arquivo.setArquivo(rs1.getBytes("ARQ"));
				arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				pendenciaArquivoDt.setArquivoDt(arquivo);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return pendenciaArquivoDt;
	}
	
	/**
	 * Retorna o arquivo de configuração de uma pré-análise finalizada. Deve descompactar o arquivo.
	 * 
	 * @param id_Pendencia: pendência para a qual será pesquisada a pré-analise
	 * @return objeto PendenciaArquivoDt com os dados do arquivo de configuração
	 * @author jesus
	 * 26/08/2014
	 */
	public PendenciaArquivoDt getArquivoConfiguracaoPreAnaliseFinalizada(String id_Pendencia) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		try{
			String sql = "SELECT * FROM PROJUDI.VIEW_CONF_PRE_ANALISE_FINAL ";
			sql += " WHERE ID_PEND = ? "; ps.adicionarLong(id_Pendencia);

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);

				//Seta dados do arquivo
				ArquivoDt arquivo = new ArquivoDt();
				arquivo.setId(rs1.getString("ID_ARQ"));
				arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				//arquivo.setArquivo(new Compactar().descompactarBytes(rs1.getBytes("ARQ")));
				arquivo.setArquivo(rs1.getBytes("ARQ"));
				arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				pendenciaArquivoDt.setArquivoDt(arquivo);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return pendenciaArquivoDt;
	}

	/**
	 * Retorna o arquivo de pré-análise (texto feito pelo assistente ou pelo próprio juiz),
	 * já retornando o responsável pela pré-analise. Deve descompactar o arquivo.
	 * 
	 * @param id_Pendencia, pendência para a qual será retornado o arquivo da pré-analise
	 * @return objeto PendenciaArquivoDt referente ao arquivo da pré-análise
	 * @author msapaula
	 */
	public PendenciaArquivoDt getArquivoPreAnaliseConclusao(String id_Pendencia) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{

//			String sql = "SELECT pa.*, (SELECT COUNT(pa1.ID_ARQ) quantidade";
//			sql += " FROM PROJUDI.PEND_ARQ pa1 group by pa1.ID_ARQ";
//			sql += " having (pa1.ID_ARQ = pa.ID_ARQ)) MULTIPLO";
//			sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pa";
//			sql += " WHERE pa.ID_PEND = ? ";
			
			String sql = "SELECT  pac.*,  ( SELECT  COUNT(1)  as quantidade FROM    PROJUDI.PEND_ARQ pa where pa.ID_ARQ = pac.ID_ARQ ) MULTIPLO";
			sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pac  WHERE  pac.ID_PEND = ? ";	ps.adicionarLong(id_Pendencia);

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);
				pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				if (rs1.getInt("MULTIPLO") > 1) pendenciaArquivoDt.setMultiplo(true);

				//Seta dados do arquivo
				ArquivoDt arquivo = new ArquivoDt();
				arquivo.setId(rs1.getString("ID_ARQ"));
				arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivo.setArquivo(rs1.getBytes("ARQ"));
				arquivo.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				pendenciaArquivoDt.setArquivoDt(arquivo);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return pendenciaArquivoDt;
	}
	
	
	/**
	 * Retorna o arquivo de pré-análise (texto feito pelo assistente ou pelo próprio juiz),
	 * já retornando o responsável pela pré-analise. Deve descompactar o arquivo. das conclusões ja finalizadas.
	 * 
	 * @param id_Pendencia, pendência para a qual será retornado o arquivo da pré-analise
	 * @return objeto PendenciaArquivoDt referente ao arquivo da pré-análise
	 * @author jrcorrea
	 * 21/08/2014
	 */
	public PendenciaArquivoDt getArquivoPreAnaliseConclusaoFinalizada(String id_Pendencia) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{

//			String sql = "SELECT pa.*, (SELECT COUNT(pa1.ID_ARQ) quantidade";
//			sql += " FROM PROJUDI.PEND_ARQ pa1 group by pa1.ID_ARQ";
//			sql += " having (pa1.ID_ARQ = pa.ID_ARQ)) MULTIPLO";
//			sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pa";
//			sql += " WHERE pa.ID_PEND = ? ";
			
			String sql = "SELECT  pac.*,  ( SELECT  COUNT(1)  as quantidade FROM    PROJUDI.PEND_FINAL_ARQ pa where pa.ID_ARQ = pac.ID_ARQ ) MULTIPLO";
			sql += " FROM PROJUDI.VIEW_PRE_ANA_CONCLUSAO_FINAL pac  WHERE  pac.ID_PEND = ? ";	ps.adicionarLong(id_Pendencia);

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);
				
				pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				
				if (rs1.getInt("MULTIPLO") > 1) pendenciaArquivoDt.setMultiplo(true);

				//Seta dados do arquivo
				ArquivoDt arquivo = new ArquivoDt();
				arquivo.setId(rs1.getString("ID_ARQ"));
				arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivo.setArquivo(rs1.getBytes("ARQ"));
				arquivo.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				pendenciaArquivoDt.setArquivoDt(arquivo);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return pendenciaArquivoDt;
	}

	/**
	 * Método responsável por consultar a pendência do tipo voto em um processo para um serventia cargo
	 * @param String id_ServentiaCargo
	 * @param String id_Processo
	 * @param String id_ProcessoTipoSessao
	 * @param String id_Pendencia
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 */
	public PendenciaArquivoDt consultaVotoDesembargador(String id_ServentiaCargo, String idAudiProc, String id_ProcessoTipoSessao, String id_Pendencia) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{

			String sql = "SELECT pa.ID_PEND_ARQ, pa.ID_PEND, pa.PEND_TIPO, pa.ID_SERV_CARGO, pa.ARQ, " +
							" pa.ID_ARQ, pa.ID_ARQ_TIPO, pa.ARQ_TIPO, pa.NOME_ARQ, pa.DATA_INSERCAO" +
					     ", ASSISTENTE, JUIZ, DATA_INSERCAO, ASSISTENTE_SEGUNDO_GRAU";
			sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pa";
			sql += " INNER JOIN AUDI_PROC AP ON AP.ID_PROC = PA.ID_PROC ";
			sql += " LEFT JOIN AUDI_PROC_PEND APP ON APP.ID_AUDI_PROC = AP.ID_AUDI_PROC ";
			sql += " WHERE pa.ID_SERV_CARGO = ? AND AP.ID_AUDI_PROC = ? AND pa.DATA_FIM IS NULL AND pa.PEND_TIPO_CODIGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
			ps.adicionarLong(idAudiProc);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
			if (id_ProcessoTipoSessao != null && id_ProcessoTipoSessao.trim().length() > 0) {
				sql += " AND EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap WHERE ap.ID_PROC_TIPO = ? AND pa.ID_PROC = ap.ID_PROC AND (ap.ID_PEND_VOTO = pa.ID_PEND OR ap.ID_PEND_VOTO_REDATOR = pa.ID_PEND)) ";
				ps.adicionarLong(id_ProcessoTipoSessao);
			}
			if (id_Pendencia != null && id_Pendencia.trim().length() > 0) {
				sql += " AND pa.ID_PEND = ? ";
				ps.adicionarLong(id_Pendencia);
			}

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				pendenciaArquivoDt = new PendenciaArquivoDt();
				
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setId_Pendencia(rs1.getString("ID_PEND"));
				pendenciaArquivoDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaArquivoDt.setId_Arquivo(rs1.getString("ID_ARQ"));
				
				if (rs1.getString("ASSISTENTE") != null && !rs1.getString("ASSISTENTE").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				else if (rs1.getString("ASSISTENTE_SEGUNDO_GRAU") != null && !rs1.getString("ASSISTENTE_SEGUNDO_GRAU").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE_SEGUNDO_GRAU"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				
				//Seta dados do arquivo
				ArquivoDt arquivo = new ArquivoDt();
				arquivo.setId(rs1.getString("ID_ARQ"));
				arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivo.setArquivo(rs1.getBytes("ARQ"));
				arquivo.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				pendenciaArquivoDt.setArquivoDt(arquivo);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return pendenciaArquivoDt;
	}
	
	/**
	 * Método que verifica se já existe uma pré-análise registrada para uma Conclusão.
	 * 
	 * @param id_Pendencia, pendência para a qual será verificada a pré-analise
	 * @author msapaula
	 */
	public boolean verificaPreAnaliseConclusao(String id_Pendencia) throws Exception {
		boolean boTeste = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{

			String sql = "SELECT pa.ID_PEND_ARQ FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pa";
			sql += " WHERE pa.ID_PEND = ? ";
			ps.adicionarLong(id_Pendencia);

			rs1 = consultar(sql, ps);
			if (rs1.next()) boTeste = true;

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return boTeste;
	}

	/**
	 * Consulta as pré-análises simples efetuadas por um Usuário.
	 * Retorna as pré-analises em aberto.
	 * O juiz vê as suas pré-analises e dos assistentes, e os assistentes também
	 * poderá ver as suas, de outros assistentes e outros juizes
	 * 
	 * @param id_ServentiaCargo, identificação do cargo para o qual será retornado as pré-analises 
	 * @param numeroProcesso, filtro para número do processo
	 * @param id_Classificador, filtro para classificador
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author msapaula
	 */
	public List consultarPreAnalisesConclusaoSimples(UsuarioNe usuarioSessao, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehAssistenteGabinete) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, ASSISTENTE, JUIZ, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, PEND_TIPO,";
		sql += " DATA_INICIO, ID_PROC, PROC_NUMERO_COMPLETO, ID_CLASSIFICADOR, CLASSIFICADOR, PRIOR_CLASSIFICADOR, PEND_PRIORIDADE_CODIGO,PEND_PRIOR_ORDEM, ASSISTENTE_SEGUNDO_GRAU, PEND_TIPO_CODIGO ";
		if (ehVoto && !ehVotoVencido) {
			sql += 	", (SELECT PROC_TIPO";
			sql += 	"     FROM PROJUDI.AUDI_PROC AP";
			sql += 	"     INNER JOIN PROJUDI.AUDI A ON (A.ID_AUDI = AP.ID_AUDI)";
			sql += 	"     INNER JOIN PROJUDI.PROC_TIPO PT ON (AP.ID_PROC_TIPO = PT.ID_PROC_TIPO)";
			sql += 	"     INNER JOIN PROJUDI.AUDI_TIPO AT ON (AT.ID_AUDI_TIPO = A.ID_AUDI_TIPO)";
			sql += 	"     INNER JOIN PROJUDI.AUDI_PROC_STATUS APS ON (APS.ID_AUDI_PROC_STATUS = AP.ID_AUDI_PROC_STATUS)";
			sql += "     WHERE AT.AUDI_TIPO_CODIGO = ? "; ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
	        sql += "      AND APS.AUDI_PROC_STATUS_CODIGO = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
	        sql += "      AND AP.ID_PROC IS NOT NULL AND AP.DATA_MOVI IS NULL";
	        sql += "      AND (AP.ID_PEND_VOTO IS NULL OR AP.ID_PEND_VOTO = pre.ID_PEND OR AP.ID_PEND_VOTO_REDATOR = pre.ID_PEND)";
	        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
	        	if(ehAssistenteGabinete){
	        		sql += " AND (";		        		
	        		sql += " EXISTS (SELECT 1";
	        		sql += "           FROM SERV_CARGO sci ";        
	        		sql += "          INNER JOIN PROC_RESP pri ON sci.ID_SERV_CARGO = pri.ID_SERV_CARGO ";        
	        		sql += "          INNER JOIN PROC pi ON pri.ID_PROC = pi.ID_PROC ";
	        		sql += "          INNER JOIN CARGO_TIPO cti ON pri.ID_CARGO_TIPO = cti.ID_CARGO_TIPO";
	        		sql += "          WHERE sci.ID_SERV_CARGO = ? ";
	        		ps.adicionarLong(id_ServentiaCargo);        
	        		sql += "          AND pi.ID_PROC = AP.ID_PROC ";
	        		sql += "          AND pri.CODIGO_TEMP <> ? ";
	        		ps.adicionarLong(-1);
	        		sql += "          ) ";	        	        
	        		sql += "  OR  ";
	        		sql += " EXISTS (SELECT 1";
	        		sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
	        		sql += "          WHERE (pri.ID_PEND = AP.ID_PEND_VOTO OR pri.ID_PEND = AP.ID_PEND_VOTO_REDATOR) ";
	        		sql += "           AND pri.ID_SERV_CARGO = ? ";
	        		ps.adicionarLong(id_ServentiaCargo);       
	        		sql += "           AND pi.ID_PROC = AP.ID_PROC ";        
	        		sql += "         ) ";
	        		sql += "     ) ";	        		 	
	        	} else {
	        		sql += "               AND (AP.ID_SERV_CARGO = ? OR AP.ID_SERV_CARGO_REDATOR = ?)";
	        		ps.adicionarLong(id_ServentiaCargo);
	        		ps.adicionarLong(id_ServentiaCargo);	
	        	}
	        }
	        sql += " AND AP.ID_PROC = pre.ID_PROC) AS PROC_TIPO_SESSAO ";			
		}
		sql += ", (SELECT COUNT(*) FROM PROJUDI.VIEW_PEND PD WHERE PD.ID_PROC = pre.ID_PROC AND PD.PEND_TIPO_CODIGO = ?) AS QUANT_ADIADO"; ps.adicionarLong(PendenciaTipoDt.ADIAR_JULGAMENTO);
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pre ";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		sql += " AND pre.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";			
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";			
			ps.adicionarLong(digitoVerificador);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sql += " AND pre.ID_CLASSIFICADOR = ? ";
			ps.adicionarLong(id_Classificador);
		}
		sql += " AND pre.CODIGO_TEMP <> ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
			
			if(ehVoto){
				if(ehVotoVencido){
					sql += " AND pre.CODIGO_TEMP_PEND = ?";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);					
				} else {
					sql += " AND (pre.CODIGO_TEMP_PEND IS NULL OR pre.CODIGO_TEMP_PEND <> ?)";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					sql += " AND NOT EXISTS (SELECT 1";        
			        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
			        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
			        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
			        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
			        sql += "                   AND (aci.ID_PEND_VOTO IS NULL OR aci.ID_PEND_VOTO = pre.ID_PEND OR aci.ID_PEND_VOTO_REDATOR = pre.ID_PEND)";
			        sql += "AND NOT EXISTS (SELECT 1 FROM PROJUDI.PEND P2 WHERE P2.ID_PROC = pre.ID_PROC AND P2.ID_PEND_TIPO IN "
			        		+ " (SELECT ID_PEND_TIPO FROM PROJUDI.PEND_TIPO WHERE PEND_TIPO_CODIGO IN (?,?)) )"; 
			        ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
			        ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
			        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
			        	if(ehAssistenteGabinete){
			        		sql += " AND (";		        		
			        		sql += " EXISTS (SELECT 1";
			        		sql += "           FROM SERV_CARGO sci ";        
			        		sql += "          INNER JOIN PROC_RESP pri ON sci.ID_SERV_CARGO = pri.ID_SERV_CARGO ";        
			        		sql += "          INNER JOIN PROC pi ON pri.ID_PROC = pi.ID_PROC ";
			        		sql += "          INNER JOIN CARGO_TIPO cti ON pri.ID_CARGO_TIPO = cti.ID_CARGO_TIPO";
			        		sql += "          WHERE sci.ID_SERV_CARGO = ? ";
			        		ps.adicionarLong(id_ServentiaCargo);        
			        		sql += "          AND pi.ID_PROC = aci.ID_PROC ";
			        		sql += "          AND pri.CODIGO_TEMP <> ? ";
			        		ps.adicionarLong(-1);
			        		sql += "          ) ";	        	        
			        		sql += "  OR  ";
			        		sql += " EXISTS (SELECT 1";
			        		sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
			        		sql += "          WHERE pri.ID_PEND = aci.ID_PEND_VOTO ";
			        		sql += "           AND pri.ID_SERV_CARGO = ? ";
			        		ps.adicionarLong(id_ServentiaCargo);       
			        		sql += "           AND pi.ID_PROC = aci.ID_PROC ";        
			        		sql += "         ) ";
			        		sql += "  OR  ";
			        		sql += " EXISTS (SELECT 1";
			        		sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
			        		sql += "          WHERE pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR ";
			        		sql += "           AND aci.ID_SERV_CARGO_REDATOR = ? ";
			        		ps.adicionarLong(id_ServentiaCargo);       
			        		sql += "           AND pi.ID_PROC = aci.ID_PROC ";        
			        		sql += "         ) ";
			        		sql += "     ) ";	        		 	
			        	} else {
			        		sql += "               AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
			        		ps.adicionarLong(id_ServentiaCargo);
			        		ps.adicionarLong(id_ServentiaCargo);	
			        	}
			        }			        
			        sql += "                   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
		        	ps.adicionarLong(0);
		        	sql += " AND aci.ID_PROC = pre.ID_PROC";        	
		        	sql += " )";
		        	
		        	sql += " AND EXISTS (  SELECT 1 "
		        			+ " FROM AUDI_PROC AP1 JOIN AUDI AD1 ON AD1.ID_AUDI = AP1.ID_AUDI "
		        			+ " WHERE AP1.DATA_MOVI IS NULL AND AP1.ID_PROC = pre.ID_PROC"
		        			+ " AND NOT EXISTS ( SELECT 1 FROM PEND_VOTO_VIRTUAL PVV WHERE PVV.ID_AUDI_PROC = AP1.ID_AUDI_PROC )"
		        			+ " AND AD1.SESSAO_INICIADA IS NULL)";		        	
				}
			}		
			
		}

		sql += " ORDER BY pre.PEND_TIPO, pre.PRIOR_CLASSIFICADOR desc, pre.ID_CLASSIFICADOR, pre.DATA_INSERCAO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				if (rs1.getString("ASSISTENTE") != null && !rs1.getString("ASSISTENTE").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				else if (rs1.getString("ASSISTENTE_SEGUNDO_GRAU") != null && !rs1.getString("ASSISTENTE_SEGUNDO_GRAU").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE_SEGUNDO_GRAU"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));			

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setHash(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setCodigoTemp(rs1.getString("QUANT_ADIADO"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				if (ehVoto && !ehVotoVencido) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_SESSAO"));
				}				
				if(rs1.getString("ID_CLASSIFICADOR") != null && !rs1.getString("ID_CLASSIFICADOR").isEmpty()) {
					processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
					processoDt.setClassificador(rs1.getString("CLASSIFICADOR") + " - (Prioridade: " + rs1.getString("PRIOR_CLASSIFICADOR") + ")");
				}
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}

/**
	 * Consulta as pré-análises simples efetuadas por um Usuário.
	 * Retorna as pré-analises em aberto.
	 * O juiz vê as suas pré-analises e dos assistentes, e os assistentes também
	 * poderá ver as suas, de outros assistentes e outros juizes
	 * 
	 * @param id_ServentiaCargo, identificação do cargo para o qual será retornado as pré-analises 
	 * @param numeroProcesso, filtro para número do processo
	 * @param id_Classificador, filtro para classificador
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author msapaula
	 */

	//TODO REVER AQUI
	public List<PendenciaArquivoDt> consultarPreAnalisesConclusaoSimplesPJD(UsuarioNe usuarioSessao, String idServentia, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehVirtual, boolean ehAssistenteGabinete) throws Exception {
		List<PendenciaArquivoDt> preAnalises = new ArrayList<>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, ASSISTENTE, JUIZ, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, PEND_TIPO,";
		sql += " DATA_INICIO, PRE.ID_PROC, PROC_NUMERO_COMPLETO, ID_CLASSIFICADOR, CLASSIFICADOR, PEND_PRIORIDADE_CODIGO,PEND_PRIOR_ORDEM, ASSISTENTE_SEGUNDO_GRAU, PEND_TIPO_CODIGO ";
		if (ehVoto /*&& !ehVotoVencido*/) {
			sql += 	", (SELECT PROC_TIPO";
			sql += 	"     FROM PROJUDI.AUDI_PROC AP1";
			sql += 	"     INNER JOIN PROJUDI.AUDI A1 ON (A1.ID_AUDI = AP1.ID_AUDI)";
			sql += 	"     INNER JOIN PROJUDI.PROC_TIPO PT ON (AP1.ID_PROC_TIPO = PT.ID_PROC_TIPO)";
			sql += 	"     INNER JOIN PROJUDI.AUDI_TIPO AT ON (AT.ID_AUDI_TIPO = A1.ID_AUDI_TIPO)";
			sql += 	"     INNER JOIN PROJUDI.AUDI_PROC_STATUS APS ON (APS.ID_AUDI_PROC_STATUS = AP1.ID_AUDI_PROC_STATUS)";
			sql += "     WHERE AT.AUDI_TIPO_CODIGO = ? "; ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
	        sql += "      AND APS.AUDI_PROC_STATUS_CODIGO = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
	        sql += "      AND AP1.ID_PROC IS NOT NULL AND AP.DATA_MOVI IS NULL";
	        sql += "      AND (AP1.ID_PEND_VOTO IS NULL OR AP1.ID_PEND_VOTO = pre.ID_PEND OR AP1.ID_PEND_VOTO_REDATOR = pre.ID_PEND)";
	        sql += "	  AND AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC ";
	        sql += "	  AND A1.ID_AUDI = A.ID_AUDI ";
	        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
	        	if(ehAssistenteGabinete){
	        		sql += " AND (";		        		
	        		sql += " EXISTS (SELECT 1";
	        		sql += "           FROM SERV_CARGO sci ";        
	        		sql += "          INNER JOIN PROC_RESP pri ON sci.ID_SERV_CARGO = pri.ID_SERV_CARGO ";        
	        		sql += "          INNER JOIN PROC pi ON pri.ID_PROC = pi.ID_PROC ";
	        		sql += "          INNER JOIN CARGO_TIPO cti ON pri.ID_CARGO_TIPO = cti.ID_CARGO_TIPO";
	        		sql += "          WHERE sci.ID_SERV_CARGO = ? ";
	        		ps.adicionarLong(id_ServentiaCargo);        
	        		sql += "          AND pi.ID_PROC = AP.ID_PROC ";
	        		sql += "          AND pri.CODIGO_TEMP <> ? ";
	        		ps.adicionarLong(-1);
	        		sql += "          ) ";	        	        
	        		sql += "  OR  ";
	        		sql += " EXISTS (SELECT 1";
	        		sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
	        		sql += "          WHERE (pri.ID_PEND = AP.ID_PEND_VOTO OR pri.ID_PEND = AP.ID_PEND_VOTO_REDATOR) ";
	        		sql += "           AND pri.ID_SERV_CARGO = ? ";
	        		ps.adicionarLong(id_ServentiaCargo);       
	        		sql += "           AND pi.ID_PROC = AP.ID_PROC ";        
	        		sql += "         ) ";
	        		sql += "     ) ";	        		 	
	        	} else {
	        		sql += "               AND (AP1.ID_SERV_CARGO = ? OR AP1.ID_SERV_CARGO_REDATOR = ?)";
	        		ps.adicionarLong(id_ServentiaCargo);
	        		ps.adicionarLong(id_ServentiaCargo);	
	        	}
	        }
	        sql += " AND AP1.ID_PROC = pre.ID_PROC) AS PROC_TIPO_SESSAO ";
	        
	        sql += ",			(" + 
	        		"	SELECT DISTINCT" + 
	        		"		PROC_TIPO" + 
	        		"	FROM" + 
	        		"		PROJUDI.PROC_TIPO PT" + 
	        		"	JOIN RECURSO_SECUNDARIO_PARTE RCP ON RCP.ID_PROC_TIPO_RECURSO_SEC = PT.ID_PROC_TIPO" + 
	        		"	JOIN AUDI_PROC AP1 ON AP1.ID_AUDI_PROC = RCP.ID_AUDI_PROC" + 
	        		"	WHERE" + 
	        		"		AP1.DATA_MOVI IS NULL" + 
	        		"		AND AP1.ID_PROC = pre.ID_PROC" +
	        		"		AND AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC "	
	        		+ " AND ROWNUM = 1) AS PROC_TIPO_RECURSO"
	        		+ " , A.VIRTUAL ";
		}
		sql += ", (SELECT COUNT(*) FROM PROJUDI.VIEW_PEND PD INNER JOIN AUDI_PROC_PEND APP_ADI ON APP_ADI.ID_AUDI_PROC = AP.ID_AUDI_PROC AND APP_ADI.ID_PEND = PD.ID_PEND WHERE APP_ADI.ID_AUDI_PROC = AP.ID_AUDI_PROC AND AP.ID_AUDI_PROC_STATUS = 1 AND PD.PEND_TIPO_CODIGO = ?) AS QUANT_ADIADO"; ps.adicionarLong(PendenciaTipoDt.ADIAR_JULGAMENTO);
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pre ";
		if(ehVoto) {
			//lrcampos 16/07/2019 * Join com a tabela de Audiencia para verificar se uma audiencia é virtual.
			sql += " INNER JOIN AUDI_PROC AP ON AP.ID_PROC = PRE.ID_PROC ";
			sql += " INNER JOIN AUDI A ON A.ID_AUDI = AP.ID_AUDI ";
		}
		sql += " WHERE pre.DATA_FIM IS NULL";
		//lrcampos 19/03/2020 15:38 - Incluindo filtro de serventia
		if(ehVirtual) {
			sql += " AND A.VIRTUAL = 1 ";
			sql += " AND A.ID_SERV = ? "; ps.adicionarLong(idServentia);
		}else {
			sql += " AND (A.VIRTUAL IS NULL OR A.VIRTUAL = 0) ";
		}
		sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		sql += " AND pre.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";			
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";			
			ps.adicionarLong(digitoVerificador);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sql += " AND pre.ID_CLASSIFICADOR = ? ";
			ps.adicionarLong(id_Classificador);
		}
		sql += " AND pre.CODIGO_TEMP <> ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
			
			if(ehVoto){
				if(ehVotoVencido){
					sql += " AND pre.CODIGO_TEMP_PEND = ?";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);					
				} else {
					sql += " AND (pre.CODIGO_TEMP_PEND IS NULL OR pre.CODIGO_TEMP_PEND <> ?)";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					sql += " AND NOT EXISTS (SELECT 1";        
			        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
			        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
			        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
			        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
			        sql += "                   AND (aci.ID_PEND_VOTO IS NULL OR aci.ID_PEND_VOTO = pre.ID_PEND)";
			        sql += "AND NOT EXISTS (SELECT 1 FROM PROJUDI.PEND P2 WHERE P2.ID_PROC = pre.ID_PROC AND P2.ID_PEND_TIPO IN "
			        		+ " (SELECT ID_PEND_TIPO FROM PROJUDI.PEND_TIPO WHERE PEND_TIPO_CODIGO IN (?,?)) )"; 
			        ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
			        ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
			        sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			        sql += " AND (A.VIRTUAL IS NOT NULL OR A.VIRTUAL = 0) ";
			        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
			        	if(ehAssistenteGabinete){
			        		sql += " AND (";		        		
			        		sql += " EXISTS (SELECT 1";
			        		sql += "           FROM SERV_CARGO sci ";        
			        		sql += "          INNER JOIN PROC_RESP pri ON sci.ID_SERV_CARGO = pri.ID_SERV_CARGO ";        
			        		sql += "          INNER JOIN PROC pi ON pri.ID_PROC = pi.ID_PROC ";
			        		sql += "          INNER JOIN CARGO_TIPO cti ON pri.ID_CARGO_TIPO = cti.ID_CARGO_TIPO";
			        		sql += "          WHERE sci.ID_SERV_CARGO = ? ";
			        		ps.adicionarLong(id_ServentiaCargo);        
			        		sql += "          AND pi.ID_PROC = aci.ID_PROC ";
			        		sql += "          AND pri.CODIGO_TEMP <> ? ";
			        		ps.adicionarLong(-1);
			        		sql += "          ) ";	        	        
			        		sql += "  OR  ";
			        		sql += " EXISTS (SELECT 1";
			        		sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
			        		sql += "          WHERE pri.ID_PEND = aci.ID_PEND_VOTO ";
			        		sql += "           AND pri.ID_SERV_CARGO = ? ";
			        		ps.adicionarLong(id_ServentiaCargo);       
			        		sql += "           AND pi.ID_PROC = aci.ID_PROC ";        
			        		sql += "         ) ";
			        		sql += "  OR  ";
			        		sql += " EXISTS (SELECT 1";
			        		sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
			        		sql += "          WHERE pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR ";
			        		sql += "           AND aci.ID_SERV_CARGO_REDATOR = ? ";
			        		ps.adicionarLong(id_ServentiaCargo);       
			        		sql += "           AND pi.ID_PROC = aci.ID_PROC ";        
			        		sql += "         ) ";
			        		sql += "     ) ";	        		 	
			        	} else {
			        		sql += "               AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
			        		ps.adicionarLong(id_ServentiaCargo);
			        		ps.adicionarLong(id_ServentiaCargo);	
			        	}
			        }
			        sql += "                   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
		        	ps.adicionarLong(0);
		        	sql += " AND aci.ID_PROC = pre.ID_PROC";        	
		        	sql += " )";
		        	
		        	sql += " AND EXISTS (  SELECT 1 "
		        			+ " FROM AUDI_PROC AP1 JOIN AUDI AD1 ON AD1.ID_AUDI = AP1.ID_AUDI "
		        			+ " WHERE AP1.DATA_MOVI IS NULL AND AP1.ID_PROC = pre.ID_PROC"
		        			+ " AND AP1.ID_ARQ_ATA IS NULL "
		        			+ " AND AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC "
		        			+ " AND NOT EXISTS ( SELECT 1 FROM PEND_VOTO_VIRTUAL PVV WHERE PVV.ID_AUDI_PROC = AP1.ID_AUDI_PROC )"
		        			+ " )";
				}
			}		
			
		}
		
		// alsqueiroz 21/08/2019 * Condição para verificar se o PROC_TIPO não é nulo.
		if (ehVoto) {
			sql += 	"AND ";
			sql += 	" (SELECT PROC_TIPO";
			sql += 	"     FROM PROJUDI.AUDI_PROC AP1";
			sql += 	"     INNER JOIN PROJUDI.AUDI A1 ON (A1.ID_AUDI = AP1.ID_AUDI)";
			sql += 	"     INNER JOIN PROJUDI.PROC_TIPO PT ON (AP1.ID_PROC_TIPO = PT.ID_PROC_TIPO)";
			sql += 	"     INNER JOIN PROJUDI.AUDI_TIPO AT ON (AT.ID_AUDI_TIPO = A1.ID_AUDI_TIPO)";
			sql += 	"     INNER JOIN PROJUDI.AUDI_PROC_STATUS APS ON (APS.ID_AUDI_PROC_STATUS = AP1.ID_AUDI_PROC_STATUS)";
			sql += "     WHERE AT.AUDI_TIPO_CODIGO = ? "; ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
	        sql += "      AND APS.AUDI_PROC_STATUS_CODIGO = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
	        sql += "      AND AP1.ID_PROC IS NOT NULL AND AP.DATA_MOVI IS NULL";
	        sql += "      AND (AP1.ID_PEND_VOTO IS NULL OR AP1.ID_PEND_VOTO = pre.ID_PEND OR AP1.ID_PEND_VOTO_REDATOR = pre.ID_PEND)";
	        sql += "	  AND AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC ";
	        sql += "	  AND A1.ID_AUDI = A.ID_AUDI ";
	        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
	        	if(ehAssistenteGabinete){
	        		sql += " AND (";		        		
	        		sql += " EXISTS (SELECT 1";
	        		sql += "           FROM SERV_CARGO sci ";        
	        		sql += "          INNER JOIN PROC_RESP pri ON sci.ID_SERV_CARGO = pri.ID_SERV_CARGO ";        
	        		sql += "          INNER JOIN PROC pi ON pri.ID_PROC = pi.ID_PROC ";
	        		sql += "          INNER JOIN CARGO_TIPO cti ON pri.ID_CARGO_TIPO = cti.ID_CARGO_TIPO";
	        		sql += "          WHERE sci.ID_SERV_CARGO = ? ";
	        		ps.adicionarLong(id_ServentiaCargo);        
	        		sql += "          AND pi.ID_PROC = AP.ID_PROC ";
	        		sql += "          AND pri.CODIGO_TEMP <> ? ";
	        		ps.adicionarLong(-1);
	        		sql += "          ) ";	        	        
	        		sql += "  OR  ";
	        		sql += " EXISTS (SELECT 1";
	        		sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
	        		sql += "          WHERE pri.ID_PEND = AP.ID_PEND_VOTO ";
	        		sql += "           AND pri.ID_SERV_CARGO = ? ";
	        		ps.adicionarLong(id_ServentiaCargo);       
	        		sql += "           AND pi.ID_PROC = AP.ID_PROC ";        
	        		sql += "         ) ";
	        		sql += "  OR  ";
	        		sql += " EXISTS (SELECT 1";
	        		sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
	        		sql += "          WHERE pri.ID_PEND = AP.ID_PEND_VOTO_REDATOR ";
	        		sql += "           AND AP.ID_SERV_CARGO_REDATOR = ? ";
	        		ps.adicionarLong(id_ServentiaCargo);       
	        		sql += "           AND pi.ID_PROC = AP.ID_PROC ";        
	        		sql += "         ) ";
	        		sql += "     ) ";	        		 	
	        	} else {
	        		sql += "               AND (AP1.ID_SERV_CARGO = ? OR AP1.ID_SERV_CARGO_REDATOR = ?)";
	        		ps.adicionarLong(id_ServentiaCargo);
	        		ps.adicionarLong(id_ServentiaCargo);	
	        	}
	        }
	        sql += " AND AP1.ID_PROC = pre.ID_PROC) IS NOT NULL ";
		}

		sql += " ORDER BY pre.PEND_TIPO, pre.PRIORI desc, pre.ID_CLASSIFICADOR, pre.DATA_INSERCAO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				if (rs1.getString("ASSISTENTE") != null && !rs1.getString("ASSISTENTE").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				else if (rs1.getString("ASSISTENTE_SEGUNDO_GRAU") != null && !rs1.getString("ASSISTENTE_SEGUNDO_GRAU").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE_SEGUNDO_GRAU"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));			

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setHash(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setCodigoTemp(rs1.getString("QUANT_ADIADO"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				if(ehVoto)
					pendenciaDt.setValor(rs1.getString("VIRTUAL"));
				
				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				if (ehVoto /*&& !ehVotoVencido*/) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_SESSAO"));;
					processoDt.setRecursoDt(new RecursoDt());
					processoDt.getRecursoDt().setProcessoTipoRecursoParteAtual(rs1.getString("PROC_TIPO_RECURSO"));
				}				
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}

	public List consultarPreAnalisesConclusaoSimplesVirtual(UsuarioNe usuarioSessao, String idServentia, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean isIniciada) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT PA.ID_PEND_ARQ, U.NOME AS JUIZ, A.DATA_INSERCAO, P.ID_PEND, P.ID_PEND_TIPO, PT.PEND_TIPO, P.DATA_INICIO, P.ID_PROC, AD.ID_SERV, SS.SERV, " +
				"	   (PRO.PROC_NUMERO || '.' || PRO.DIGITO_VERIFICADOR) AS PROC_NUMERO_COMPLETO, ";
		sql += 	" AUDIPROC.ID_PROC_TIPO AS ID_PROC_TIPO_SESSAO , PT.PROC_TIPO AS PROC_TIPO_SESSAO, ";
        sql += "			(" + 
        		"	SELECT DISTINCT" + 
        		"		PROC_TIPO" + 
        		"	FROM" + 
        		"		PROJUDI.PROC_TIPO PT" + 
        		"	JOIN RECURSO_SECUNDARIO_PARTE RCP ON RCP.ID_PROC_TIPO_RECURSO_SEC = PT.ID_PROC_TIPO" + 
        		"	JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = RCP.ID_AUDI_PROC" + 
        		"	JOIN AUDI_PROC_PEND APP1 ON APP1.ID_AUDI_PROC = AP.ID_AUDI_PROC" + 
        		"	WHERE" + 
        		"		AP.DATA_MOVI IS NULL" + 
        		"		AND APP1.ID_PEND = APP.ID_PEND AND APP1.ID_AUDI_PROC = APP.ID_AUDI_PROC) AS PROC_TIPO_RECURSO,";
		//lrcampos 05/03/2020 15:53 - retirando view desnecessária.
		sql += " (SELECT COUNT(*) FROM PROJUDI.PEND PD "
				+ " INNER JOIN AUDI_PROC_PEND APP2 ON APP2.ID_PEND = PD.ID_PEND"
				+ " INNER JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = APP2.ID_AUDI_PROC"
				+ " INNER JOIN PEND_TIPO PENTIPO ON PENTIPO.ID_PEND_TIPO = PD.ID_PEND_TIPO "
				+ " WHERE AP.ID_AUDI_PROC = APP.ID_AUDI_PROC ";
		sql	+= " AND PENTIPO.PEND_TIPO_CODIGO = ?) AS QUANT_ADIADO, "; ps.adicionarLong(PendenciaTipoDt.ADIAR_JULGAMENTO);
		sql += " PT.PEND_TIPO_CODIGO, ";
		sql += " P.ID_CLASSIFICADOR, C.CLASSIFICADOR ";
		sql += " FROM PROJUDI.PEND p ";
		sql += " LEFT JOIN CLASSIFICADOR C ON P.ID_CLASSIFICADOR = C.ID_CLASSIFICADOR";
		sql += " INNER JOIN AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND ";
		sql += " INNER JOIN AUDI_PROC AUDIPROC ON AUDIPROC.ID_AUDI_PROC = APP.ID_AUDI_PROC ";
		sql += " INNER JOIN PROJUDI.PROC_TIPO PT ON PT.ID_PROC_TIPO = AUDIPROC.ID_PROC_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ? ";	ps.adicionarLong(1); 
		sql += " AND pa.CODIGO_TEMP <> ? )"; ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " INNER JOIN PROJUDI.SERV_CARGO SC ON (SC.ID_SERV_CARGO = PR.ID_SERV_CARGO AND PR.ID_USU_RESP IS NULL)";
		sql += " INNER JOIN CARGO_TIPO CT ON SC.ID_CARGO_TIPO = CT.ID_CARGO_TIPO";
		sql += " INNER JOIN USU_SERV_GRUPO UG ON SC.ID_USU_SERV_GRUPO = UG.ID_USU_SERV_GRUPO"; 
		sql += " INNER JOIN USU_SERV US1 ON US1.ID_USU_SERV = UG.ID_USU_SERV";
		sql += " INNER JOIN USU U ON US1.ID_USU = U.ID_USU";
		sql += " INNER JOIN AUDI AD ON AD.ID_AUDI = AUDIPROC.ID_AUDI ";
		sql += " JOIN ARQ A   ON PA.ID_ARQ = A.ID_ARQ";
		sql += " JOIN PROC PRO  ON P.ID_PROC = PRO.ID_PROC";
		sql += " INNER JOIN SERV SS ON SS.ID_SERV = AD.ID_SERV";
	
		sql += " WHERE pt.PEND_TIPO_CODIGO = ? ";									ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		//sql += " AND AD.VIRTUAL = 1 ";
		sql += " AND AUDIPROC.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		if(ehVotoVencido){
			sql += " AND p.CODIGO_TEMP = ?";										ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
		} else {
			sql += " AND (p.CODIGO_TEMP IS NULL OR p.CODIGO_TEMP <> ?)";			ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
			sql += " AND NOT EXISTS (SELECT 1";		
			sql += "				   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
			sql += "				  WHERE aci.AUDI_TIPO_CODIGO = ? ";				ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			sql += "				   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";	   ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			sql += "				   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
			sql += "				   AND ((aci.ID_PEND_VOTO IS NOT NULL AND aci.ID_PEND_VOTO = P.ID_PEND) OR (aci.ID_PEND_VOTO_REDATOR IS NOT NULL AND aci.ID_PEND_VOTO_REDATOR = P.ID_PEND))";				
			if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
				sql += "			   AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
				ps.adicionarLong(id_ServentiaCargo);
				ps.adicionarLong(id_ServentiaCargo);	
			}
				
		}		
		sql += "				   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
		ps.adicionarLong(0);
		sql += " AND aci.ID_PROC = p.ID_PROC";
		sql += " AND aci.ID_AUDI_PROC = app.ID_AUDI_PROC ";
		sql += " )";
			sql += " AND NOT EXISTS (SELECT 1 FROM PROJUDI.PEND P2 INNER JOIN AUDI_PROC_PEND APP1 ON APP1.ID_PEND = P2.ID_PEND WHERE APP1.ID_AUDI_PROC = APP.ID_AUDI_PROC AND P2.ID_PEND_TIPO IN "
					+ " ( SELECT ID_PEND_TIPO FROM PROJUDI.PEND_TIPO WHERE PEND_TIPO_CODIGO IN (?, ?)) )"; 
			ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
			ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
			//lrcampos 14/01/2020 09:35 - vinculando pela audiProc em vez de idProc
			sql += " AND EXISTS (  SELECT 1 "
					+ " FROM AUDI_PROC AP JOIN AUDI AD ON AD.ID_AUDI = AP.ID_AUDI "
					+ " WHERE AP.DATA_MOVI IS NULL AND AP.ID_AUDI_PROC = APP.ID_AUDI_PROC"
					+ " AND NOT EXISTS ( SELECT 1 FROM PEND_VOTO_VIRTUAL PVV "
					+ " 					 INNER JOIN AUDI_PROC_PEND APP1 ON (APP1.ID_AUDI_PROC = PVV.ID_AUDI_PROC AND PVV.ID_PEND = APP1.ID_PEND) "
					+ "						 WHERE APP1.ID_AUDI_PROC = APP.ID_AUDI_PROC AND APP1.ID_PEND = APP.ID_PEND )";
					if(isIniciada) {
						sql += " AND AD.SESSAO_INICIADA = 1 )";
					}else {
						sql += " AND AD.SESSAO_INICIADA IS NULL )";
					}
		
		sql += " AND NOT EXISTS (SELECT 1 ";
		//lrcampos 09/03/2020 - Substituindo view que deixava a consulta lenta.
		sql += "				   FROM PROJUDI.AUDI_PROC aci"
				+ "					JOIN PROJUDI.PEND_VOTO_VIRTUAL PV ON ACI.ID_AUDI_PROC = PV.ID_AUDI_PROC "
				+ "					JOIN AUDI AUD ON AUD.ID_AUDI = ACI.ID_AUDI "
				+ "					JOIN AUDI_TIPO ATIPO ON ATIPO.ID_AUDI_TIPO = AUD.ID_AUDI_TIPO "
				+ "					JOIN AUDI_PROC_STATUS APS1 ON APS1.ID_AUDI_PROC_STATUS = ACI.ID_AUDI_PROC_STATUS "		;
		
		sql += "				  WHERE ATIPO.AUDI_TIPO_CODIGO = ? ";
		ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		sql += "				   AND APS1.AUDI_PROC_STATUS_CODIGO = ? ";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql += "AND aci.ID_AUDI_PROC = APP.ID_AUDI_PROC AND aci.ID_PEND_VOTO = APP.ID_PEND ";
		sql += " )";
					
		sql += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL ";
		sql += " AND ( SELECT COUNT(1) FROM PEND_ARQ pa1 ";
		sql += " 		WHERE pa1.ID_ARQ = pa.ID_ARQ ) = ? ";		ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? "; ps.adicionarLong(id_ServentiaCargo);
		if (StringUtils.isNotEmpty(numeroProcesso)){
			sql += " AND PRO.PROC_NUMERO = ? ";			
			ps.adicionarLong(numeroProcesso);
		}
		if (StringUtils.isNotEmpty(digitoVerificador)){
			sql += " AND PRO.DIGITO_VERIFICADOR = ? ";			
			ps.adicionarLong(digitoVerificador);
		}
		sql += " AND P.ID_PEND_STATUS <> ? "; ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);
		sql += " AND AD.VIRTUAL = 1 ";
		//lrcampos 19/03/2020 15:38 - Incluindo filtro de serventia
		if(StringUtils.isNotEmpty(idServentia)) {
			sql+= "AND AD.ID_SERV = ? "; ps.adicionarLong(idServentia);
		}
		
		//lrcampos 23/03/2020 11:38 - Incluindo filtro de classificador 
		if(StringUtils.isNotEmpty(id_Classificador)) {
			sql += " AND (CASE WHEN P.ID_CLASSIFICADOR IS NULL THEN PRO.ID_CLASSIFICADOR "
					+ "		ELSE P.ID_CLASSIFICADOR END) = ?"; ps.adicionarLong(id_Classificador);
		}
		sql += " ORDER BY pt.PEND_TIPO, P.ID_CLASSIFICADOR";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				/*if (rs1.getString("ASSISTENTE") != null && !rs1.getString("ASSISTENTE").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				else if (rs1.getString("ASSISTENTE_SEGUNDO_GRAU") != null && !rs1.getString("ASSISTENTE_SEGUNDO_GRAU").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE_SEGUNDO_GRAU"));*/
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));			

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setHash(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				/*pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));*/
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setCodigoTemp(rs1.getString("QUANT_ADIADO"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				if (ehVoto && !ehVotoVencido) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_SESSAO"));;	
					processoDt.setRecursoDt(new RecursoDt());
					processoDt.getRecursoDt().setProcessoTipoRecursoParteAtual(rs1.getString("PROC_TIPO_RECURSO"));
				}				
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				processoDt.setCodigoTemp(rs1.getString("ID_SERV")+"@"+rs1.getString("SERV"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}
	
	public List consultarPreAnalisesAguardandoInicioVirtual(UsuarioNe usuarioSessao, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT PA.ID_PEND_ARQ, U.NOME AS JUIZ, A.DATA_INSERCAO, P.ID_PEND, P.ID_PEND_TIPO, PT.PEND_TIPO, P.DATA_INICIO, P.ID_PROC, PRTIPO.PROC_TIPO, " +
				"	   (PRO.PROC_NUMERO || '.' || PRO.DIGITO_VERIFICADOR) AS PROC_NUMERO_COMPLETO ";
		//lrcampos 30/01/2019 10:57 - Obtendo a classe do recurso/recurso secundario
		sql += ", (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ";
		sql += "	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "; 
		sql += "	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ";
		sql += " 	WHERE AP1.ID_AUDI_PROC = APP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "; 
		sql += " 	(SELECT 1 FROM RECURSO R "; 
		sql += "		INNER JOIN AUDI_PROC AP1 ON AP1.ID_PROC = R.ID_PROC "; 
		sql += "		WHERE AP1.ID_AUDI_PROC = APP.ID_AUDI_PROC AND ROWNUM = 1) POSSUI_RECURSO, ";
		//lrcampos 09/03/2020 15:32 - Substituindo view para melhorar a performace da query
		sql += " (SELECT COUNT(*) FROM PROJUDI.PEND PD " ;
		sql += " INNER JOIN PEND_TIPO PTC ON PTC.ID_PEND_TIPO = PD.ID_PEND_TIPO ";
		sql += " INNER JOIN AUDI_PROC_PEND APP_ADIAMENTO ON APP_ADIAMENTO.ID_PEND = PD.ID_PEND";
		sql += " WHERE APP_ADIAMENTO.ID_AUDI_PROC = AP.ID_AUDI_PROC ";
		sql	+= " AND PTC.PEND_TIPO_CODIGO = ?) AS QUANT_ADIADO, "; ps.adicionarLong(PendenciaTipoDt.ADIAR_JULGAMENTO);
		sql += " PT.PEND_TIPO_CODIGO, ";
		sql += " P.ID_CLASSIFICADOR, C.CLASSIFICADOR ";
		sql += " FROM PROJUDI.PEND p ";
		sql += " LEFT JOIN CLASSIFICADOR C ON P.ID_CLASSIFICADOR = C.ID_CLASSIFICADOR ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " INNER JOIN PROJUDI.AUDI_PROC_PEND APP on p.ID_PEND = APP.ID_PEND";
		//lrcampos 14/11/2019 10:16 buscando somente as pendencias que estão salvas na audi proc nos campos de PEND_VOTO OU PEND_EMENTA
		sql += " INNER JOIN PROJUDI.AUDI_PROC AP ON (AP.ID_AUDI_PROC = APP.ID_AUDI_PROC AND AP.ID_AUDI_PROC_STATUS = 1 AND AP.DATA_MOVI IS NULL) ";
		sql += " INNER JOIN AUDI A ON (A.ID_AUDI  = AP.ID_AUDI AND A.SESSAO_INICIADA = 1 AND A.VIRTUAL = 1 )";
		sql += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ? ";	ps.adicionarLong(1); 
		sql += " AND pa.CODIGO_TEMP <> ? AND (PA.ID_PEND = AP.ID_PEND_VOTO OR PA.ID_PEND = AP.ID_PEND_EMENTA OR PA.ID_PEND = AP.ID_PEND_VOTO_REDATOR OR PA.ID_PEND = AP.ID_PEND_EMENTA_REDATOR))"; ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " INNER JOIN PROJUDI.SERV_CARGO SC ON (SC.ID_SERV_CARGO = PR.ID_SERV_CARGO AND PR.ID_USU_RESP IS NULL)";
		sql += " INNER JOIN CARGO_TIPO CT ON SC.ID_CARGO_TIPO = CT.ID_CARGO_TIPO";
		sql += " INNER JOIN USU_SERV_GRUPO UG ON SC.ID_USU_SERV_GRUPO = UG.ID_USU_SERV_GRUPO"; 
		sql += " INNER JOIN USU_SERV US1 ON US1.ID_USU_SERV = UG.ID_USU_SERV";
		sql += " INNER JOIN USU U ON US1.ID_USU = U.ID_USU";
		sql += " INNER JOIN PROC_TIPO PRTIPO ON PRTIPO.ID_PROC_TIPO = AP.ID_PROC_TIPO ";
		sql += " JOIN ARQ A   ON PA.ID_ARQ = A.ID_ARQ";
		sql += " JOIN PROC PRO  ON P.ID_PROC = PRO.ID_PROC";
				
		
		sql += " WHERE pt.PEND_TIPO_CODIGO = ? ";									ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);	
		if(ehVotoVencido){
			sql += " AND p.CODIGO_TEMP = ?";										ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
		} else {
			sql += " AND (p.CODIGO_TEMP IS NULL OR p.CODIGO_TEMP <> ?)";			ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
			sql += " AND NOT EXISTS (SELECT 1";
			//lrcampos 09/03/2020 15:32 - Substituindo view para melhorar a performace da query		
			sql += "				   FROM PROJUDI.AUDI_PROC AP1";
			sql += " 					INNER JOIN AUDI A1 ON A1.ID_AUDI = AP1.ID_AUDI ";
			sql += "					INNER JOIN AUDI_TIPO AT1 ON AT1.ID_AUDI_TIPO  = A1.ID_AUDI_TIPO ";
			sql += "					INNER JOIN AUDI_PROC_STATUS APS1 ON APS1.ID_AUDI_PROC_STATUS  = AP1.ID_AUDI_PROC_STATUS ";
			sql += "				  WHERE AT1.AUDI_TIPO_CODIGO = ? ";				ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			sql += "				   AND APS1.AUDI_PROC_STATUS_CODIGO = ? ";	   ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			sql += "				   AND AP1.ID_PROC IS NOT NULL AND AP1.DATA_MOVI IS NULL";
			sql += "				   AND (AP1.ID_PEND_VOTO = P.ID_PEND OR AP1.ID_PEND_VOTO_REDATOR = P.ID_PEND)";				
			if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
					sql += "			   AND (AP1.ID_SERV_CARGO = ? OR AP1.ID_SERV_CARGO_REDATOR = ?)";
					ps.adicionarLong(id_ServentiaCargo);
					ps.adicionarLong(id_ServentiaCargo);	
			}
			sql += "				   AND (NOT AP1.ID_ARQ_ATA IS NULL AND AP1.ID_ARQ_ATA > ?) ";
			ps.adicionarLong(0);
			sql += " AND AP1.ID_PROC = p.ID_PROC";
			sql += " )";
		}
		sql += " AND NOT EXISTS (SELECT 1 FROM PROJUDI.PEND P2 WHERE P2.ID_PROC = p.ID_PROC AND P2.ID_PEND_TIPO IN "
				+ " ( SELECT ID_PEND_TIPO FROM PROJUDI.PEND_TIPO WHERE PEND_TIPO_CODIGO IN (?, ?)) )"; 
		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
		//lrcampos 09/03/2020 15:32 - Retirado exists e colocando as condições no inner join.
		sql += " AND NOT EXISTS ( SELECT 1 FROM PEND_VOTO_VIRTUAL PVV WHERE PVV.ID_AUDI_PROC = AP.ID_AUDI_PROC )";				
		sql += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL ";
		sql += " AND ( SELECT COUNT(1) FROM PEND_ARQ pa1 ";
		sql += " 		WHERE pa1.ID_ARQ = pa.ID_ARQ ) = ? ";		ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? "; ps.adicionarLong(id_ServentiaCargo);
		sql += " AND P.ID_PEND_STATUS = ? "; ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);
		if(numeroProcesso != null && !numeroProcesso.isEmpty()) {
			sql += " AND  PRO.PROC_NUMERO  = ? "; ps.adicionarString(numeroProcesso);;
		}
		if(id_Classificador != null && !id_Classificador.isEmpty()) {
			sql += " AND  p.ID_CLASSIFICADOR = ? "; ps.adicionarString(id_Classificador);
		}

		sql += " ORDER BY  p.ID_CLASSIFICADOR";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				/*if (rs1.getString("ASSISTENTE") != null && !rs1.getString("ASSISTENTE").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				else if (rs1.getString("ASSISTENTE_SEGUNDO_GRAU") != null && !rs1.getString("ASSISTENTE_SEGUNDO_GRAU").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE_SEGUNDO_GRAU"));*/
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));			

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setHash(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				/*pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));*/
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setCodigoTemp(rs1.getString("QUANT_ADIADO"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));			
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				
				//lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sessão possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}
	/**
	 * Consulta as pré-análises simples efetuadas por um Usuário.
	 * Retorna as pré-analises em aberto.
	 * O juiz vê as suas pré-analises e dos assistentes, e os assistentes também
	 * poderá ver as suas, de outros assistentes e outros juizes
	 * 
	 * @param id_ServentiaCargo, identificação do cargo para o qual será retornado as pré-analises 
	 * @param numeroProcesso, filtro para número do processo
	 * @param id_Classificador, filtro para classificador
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author lsbernardes
	 */
	public List consultarPreAnalisesConclusaoSimplesAssistenteGabinete(UsuarioNe usuarioSessao, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, String id_ServentiaGrupo, boolean ehVoto, boolean ehVotoVencido) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT pre.ID_PEND_ARQ, ASSISTENTE, JUIZ, pre.DATA_INSERCAO, pre.ID_PEND, pre.ID_PEND_TIPO, pre.PEND_TIPO, sg.id_serv_grupo,  sg.atividade, ";
		sql += " pre.DATA_INICIO, pre.ID_PROC, pre.PROC_NUMERO_COMPLETO, pre.ID_CLASSIFICADOR, pre.CLASSIFICADOR, pre.PEND_PRIORIDADE_CODIGO, pre.PEND_PRIOR_ORDEM, pre.ASSISTENTE_SEGUNDO_GRAU, pre.PEND_TIPO_CODIGO ";
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pre ";
		sql += " INNER JOIN PROJUDI.pend_resp_hist prh on pre.id_pend = prh.id_pend and prh.data_fim is null ";   
		sql += " INNER JOIN PROJUDI.serv_grupo sg on prh.id_serv_grupo = sg.id_serv_grupo ";
		
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		sql += " AND pre.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";			
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";			
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
			
			if(ehVoto){
				if(ehVotoVencido){
					sql += " AND pre.CODIGO_TEMP_PEND = ?";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
				} else {
					sql += " AND (pre.CODIGO_TEMP_PEND IS NULL OR pre.CODIGO_TEMP_PEND <> ?)";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					sql += " AND NOT EXISTS (SELECT 1";        
			        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
			        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
			        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
			        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
			        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        		sql += "               AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
		        		ps.adicionarLong(id_ServentiaCargo);
		        		ps.adicionarLong(id_ServentiaCargo);	        		
			        }
			        sql += "                   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
		        	ps.adicionarLong(0);
		        	sql += " AND aci.ID_PROC = pre.ID_PROC";        	
		        	sql += " )";
				}
			}		
		}
		
		if (id_ServentiaGrupo != null && id_ServentiaGrupo.length()>0){
			sql += " AND  prh.id_serv_grupo = ? ";
			ps.adicionarLong(id_ServentiaGrupo);
		}
		
		if (id_Classificador != null && id_Classificador.length() > 0){
			sql += " AND pre.ID_CLASSIFICADOR = ? ";
			ps.adicionarLong(id_Classificador);
		}
		sql += " AND pre.CODIGO_TEMP <> ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		//sql += " ORDER BY pre.PEND_TIPO, pre.PRIORI desc, pre.ID_CLASSIFICADOR, pre.DATA_INSERCAO";
		sql += " ORDER BY pre.PEND_TIPO, pre.PRIOR_CLASSIFICADOR desc, pre.ID_CLASSIFICADOR, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO , sg.id_serv_grupo,  sg.atividade";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				if (rs1.getString("ASSISTENTE") != null && !rs1.getString("ASSISTENTE").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				else if (rs1.getString("ASSISTENTE_SEGUNDO_GRAU") != null && !rs1.getString("ASSISTENTE_SEGUNDO_GRAU").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE_SEGUNDO_GRAU"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));			

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setHash(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO")+" - "+rs1.getString("atividade"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}
	
	/**
	 * Consulta as pré-análises múltiplas realizadas para um ServentiaCargo, seja feita pelo juiz ou pelos assistentes.
	 * Retorna as pré-analises em aberto.
	 * 
	 * @param id_ServentiaCargo, serventiaCargo para o qual quer retornar as pré-analises
	 * @param numeroProcesso, filtro para numero do processo
	 * @author msapaula
	 */
	public List consultarPreAnalisesConclusoesMultiplas(String id_ServentiaCargo, String numeroProcesso) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, ASSISTENTE, JUIZ, PROC_NUMERO_COMPLETO, ID_ARQ, DATA_INSERCAO, ID_ARQ_TIPO,";
		sql += "  ARQ_TIPO FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pre";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += "		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) > ? ";
		ps.adicionarLong(1);
		sql += " AND pre.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";
			ps.adicionarLong(Funcoes.desformataNumeroProcesso(numeroProcesso));
		}
		sql += " ORDER BY pre.DATA_INSERCAO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));

				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				pendenciaArquivoDt.setArquivoDt(arquivoDt);
				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return preAnalises;
	}

	/**
	 * Consulta as pré-análises de um usuário ou ServentiaCargo que já foram finalizadas, assinadas pelo juiz.
	 * 
	 * @param numeroProcesso, filtro de número de processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param dataInicial, filtro data início
	 * @param dataFinal, filtro data Final
	 * @param id_ServentiaCargo, identificação do cargo para filtrar pré-análises
	 * @param id_UsuarioServentia, identificação do usuario para filtrar pré-análises
	 * @author msapaula
	 */
	public List consultarPreAnalisesConclusaoFinalizadas(String numeroProcesso, String digitoVerificador, String dataInicial, String dataFinal, String id_ServentiaCargo, String id_UsuarioServentia) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT ID_PEND_ARQ, ASSISTENTE, JUIZ, PROC_NUMERO_COMPLETO, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, ID_PROC, PEND_TIPO, DATA_FIM, USU_FINALIZADOR, PEND_STATUS, PEND_STATUS_CODIGO  FROM PROJUDI.VIEW_PRE_ANALISE_CONCL_FINALIZ pre where";
		
		if (id_ServentiaCargo != null){
			sql += " pre.ID_SERV_CARGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
		} else if (id_UsuarioServentia != null) {
			sql += " pre.ID_USU_SERV = ? ";
			ps.adicionarLong(id_UsuarioServentia);
		}

		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";			
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";
			ps.adicionarLong(digitoVerificador);
		}
		
		if (dataInicial != null && dataInicial.length() > 0){
			sql += " AND pre.DATA_FIM >= ? ";
			ps.adicionarDateTime(dataInicial + " 00:00:00");
		}
		if (dataFinal != null && dataFinal.length() > 0){
			sql += " AND pre.DATA_FIM <= ? ";
			ps.adicionarDateTime(dataFinal + " 23:59:59");
		}

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				PendenciaDt pendenciaDt = new PendenciaDt();
				
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataFim(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setUsuarioFinalizador(rs1.getString("USU_FINALIZADOR"));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setPendenciaStatusCodigo(rs1.getString("PEND_STATUS_CODIGO"));
				
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);
				
				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return preAnalises;
	}

	/**
	 * Consultar arquivos de resposta de uma pendencia
	 * @author Ronneesley Moura Teles
	 * @since 15/01/2009 10:44
	 * @param pendenciaArquivoDt, pojo das pendencias arquivo
	 * @return lista de arquivos de resposta
	 */
	public List consultarArquivosResposta(String id_Pendencia) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PEND_ARQ_COMPLETA pac WHERE pac.ID_PEND = ? AND pac.RESPOSTA = ? ";
		ps.adicionarLong(id_Pendencia);
		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				ArquivoDt arquivoDt = new ArquivoDt();

				this.associarArquivo(arquivoDt, rs1);

				arquivos.add(arquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}

	/**
	 * Associar um arquivo
	 * @author Ronneesley Moura Teles
	 * @since 15/01/2009 10:47
	 * @param arquivoDt vo do arquivo
	 * @param rs result set do arquivo
	 * @throws Exception 
	 * @throws Exception
	 */
	private void associarArquivo(ArquivoDt arquivoDt, ResultSetTJGO rs1) throws Exception{
		arquivoDt.setId(rs1.getString("ID_ARQ"));
		arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
		arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
		arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
		arquivoDt.setContentType(rs1.getString("CONTENT_TYPE"));
		arquivoDt.setArquivo(new String(rs1.getBytes("ARQ")));
		arquivoDt.setCaminho(rs1.getString("CAMINHO"));
		arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
		arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
	}

	/**
	 * Consultar arquivos de uma pendencia
	 * @author Ronneesley Moura Teles
	 * @since 08/09/2008 14:05
	 * @param PendenciaArquivoDt pendenciaArquivoDt, pojo das pendencias arquivo
	 * @return List
	 */
	public List consultarArquivos(PendenciaArquivoDt pendenciaArquivoDt) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PEND_ARQ_COMPLETA WHERE ID_PEND = ? ";
		ps.adicionarLong(pendenciaArquivoDt.getId_Pendencia());

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				ArquivoDt arquivoDt = new ArquivoDt();

				this.associarArquivo(arquivoDt, rs1);

				arquivos.add(arquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	
	// jvosantos - 29/10/2019 13:22 - Tipar lista
	/**
	 * Consultar arquivos de uma pendencia
	 * @author Leandro Bernardes
	 * @param String id_Pendencia, id da pendencia
	 * @return List
	 */
	public List consultarArquivosPendencia(String id_Pendencia) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = " SELECT PA.ID_ARQ AS ID_ARQ, VUS.NOME_ARQ AS NOME_ARQ, ART.ARQ_TIPO_CODIGO AS ARQ_TIPO_CODIGO FROM "
				+ "(((PEND_ARQ PA JOIN ARQ VUS ON((PA.ID_ARQ = VUS.ID_ARQ))) "
				+ "JOIN ARQ_TIPO ART ON((ART.ID_ARQ_TIPO = VUS.ID_ARQ_TIPO)))) WHERE PA.ID_PEND = ?";		
		
		ps.adicionarLong(id_Pendencia);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setArquivoTipoCodigo(rs1.getString("ARQ_TIPO_CODIGO"));
				arquivos.add(arquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}

	/**
	 * Consultar arquivos de uma pendencia finalizada
	 * @author hrrosa
	 * @param String id_Pendencia, id da pendencia
	 * @return List
	 */
	public List consultarArquivosPendenciaFinalizada(String id_Pendencia) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = " SELECT PFA.ID_ARQ AS ID_ARQ, VUS.NOME_ARQ AS NOME_ARQ, ART.ARQ_TIPO_CODIGO AS ARQ_TIPO_CODIGO FROM "
				+ "(((PEND_FINAL_ARQ PFA JOIN ARQ VUS ON((PFA.ID_ARQ = VUS.ID_ARQ))) "
				+ "JOIN ARQ_TIPO ART ON((ART.ID_ARQ_TIPO = VUS.ID_ARQ_TIPO)))) WHERE PFA.ID_PEND = ?";		
		
		ps.adicionarLong(id_Pendencia);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivoDt.setArquivoTipoCodigo(rs1.getString("ARQ_TIPO_CODIGO"));
				arquivos.add(arquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	
//	/**
//	 * Consulta as pendências vinculadas a um arquivo.
//	 * Geralmente esse arquivo refere-se a uma pré-analise multipla realizada.
//	 * Filtra pelas pendências que estão em aberto, porque algumas das pendências vinculadas ao arquivo
//	 * podem ter sido concluídas separadamente.
//	 * 
//	 * @param id_Arquivo: identificação do arquivo de pré-analise
//	 * @return lista de pendências vinculadas ao arquivo
//	 * @throws Exception 
//	 */
//	public List consultarPendencias(String id_Arquivo){
//		List pendencias = new ArrayList();
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//
//		String sql = "SELECT pen.* FROM PROJUDI.PEND_ARQ pa ";
//		sql += " JOIN PROJUDI.VIEW_PEND pen on pa.ID_PEND=pen.ID_PEND";
//		sql += " WHERE pa.ID_ARQ = ? ";
//		ps.adicionarLong(id_Arquivo);
//		sql += " AND pen.DATA_FIM IS NULL ORDER BY pen.DATA_INICIO";
//
//		ResultSetTJGO rs1 = null;
//		try{
//			rs1 = this.consultar(sql, ps);
//
//			while (rs1.next()) {
//				PendenciaDt pendenciaDt = new PendenciaDt();
//
//				PendenciaPs pendenciaPs = new PendenciaPs();
//				pendenciaPs.associarDt(pendenciaDt, rs1);
//
//				pendencias.add(pendenciaDt);
//			}
//		} finally {
//			try{
//				if (rs1 != null) rs1.close();
//			} catch(Exception e) {
//			}
//		}
//
//		return pendencias;
//	}
	
	/**
	 * Altera o status de um arquivo em uma pendência
	 * 
	 * @param String id, id de PendenciaArquivo
	 * @param String novo status do arquivo 0 - normal, 1 - vírus, 2 - restrição download
	 * 
	 * @author lsbernardes
	 */
	public void alterarStatusPendenciaArquivo(String id, String novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND_ARQ SET CODIGO_TEMP = ? ";
		ps.adicionarLong(novoStatus);
		Sql += " WHERE ID_PEND_ARQ = ? ";
		ps.adicionarLong(id);

			executarUpdateDelete(Sql, ps);

	}

	/**
	 * Consulta as pré-análises simples efetuadas por um Usuário.
	 * Retorna as pré-analises em aberto.
	 * 
	 * @param id_ServentiaCargo, identificação do cargo para o qual será retornado as pré-analises 
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param numeroProcesso, filtro para número do processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author msapaula
	 */
	public List consultarPreAnalisesSimples(String id_ServentiaCargo, String id_UsuarioServentia, String numeroProcesso, String digitoVerificador, String id_PendenciaTipo) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, PEND_TIPO,";
		sql += " DATA_INICIO, ID_PROC, PROC_NUMERO_COMPLETO, ID_PROC_TIPO, PROC_TIPO, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM ";
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_PEND pre ";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		if (id_ServentiaCargo != null){
			sql += " AND pre.ID_SERV_CARGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
		}
		else if (id_UsuarioServentia != null){
			sql += " AND pre.ID_USU_RESP = ? ";
			ps.adicionarLong(id_UsuarioServentia);
		}
		sql += " AND pre.PEND_TIPO_CODIGO IN (?, ?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
		}
		sql += " AND pre.CODIGO_TEMP <> ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " ORDER BY pre.PEND_TIPO, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}
	
	/**
	 * Consulta as pré-análises simples efetuadas por um Usuário.
	 * Retorna as pré-analises em aberto.
	 * 
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param numeroProcesso, filtro para número do processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author lsbernardes
	 */
	public List consultarPreAnalisesSimples(String id_UsuarioServentia, String numeroProcesso, String digitoVerificador, String id_PendenciaTipo) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, PEND_TIPO, ID_CLASSIFICADOR, CLASSIFICADOR, PRIORI_CLASSIFICADOR, ";
		sql += " DATA_INICIO, ID_PROC, PROC_NUMERO_COMPLETO, ID_PROC_TIPO, PROC_TIPO, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM ";
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_PEND pre ";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		
		sql += " AND pre.ID_USU_RESP = ? ";
		ps.adicionarLong(id_UsuarioServentia);
			
		sql += " AND pre.PEND_TIPO_CODIGO IN (?, ?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
		}
		sql += " AND pre.CODIGO_TEMP <> ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		
		sql += " ORDER BY pre.PEND_TIPO, pre.ID_CLASSIFICADOR, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				pendenciaDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}
	
	/**
	 * Consulta as pré-análises simples efetuadas por um Usuário.
	 * Retorna as pré-analises em aberto.
	 * 
	 * @param id_ServentiaCargo, identificação do cargo de um usuário da serventia
	 * @param numeroProcesso, filtro para número do processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author lsbernardes
	 */
	public List consultarPreAnalisesSimplesCargo(String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_PendenciaTipo) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, PEND_TIPO, ID_CLASSIFICADOR, CLASSIFICADOR, PRIORI_CLASSIFICADOR, ";
		sql += " DATA_INICIO, ID_PROC, PROC_NUMERO_COMPLETO, ID_PROC_TIPO, PROC_TIPO, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM ";
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_PEND pre ";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		
		sql += " AND pre.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
			
		sql += " AND pre.PEND_TIPO_CODIGO IN (?, ?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
		}
		sql += " AND pre.CODIGO_TEMP <> ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		
		sql += " ORDER BY pre.PEND_TIPO, pre.ID_CLASSIFICADOR, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				pendenciaDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}

	/**
	 * Consulta as pré-análises múltiplas de Pendências realizadas para um ServentiaCargo.
	 * Retorna as pré-analises em aberto.
	 * 
	 * @param id_ServentiaCargo, serventiaCargo para o qual quer retornar as pré-analises
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param numeroProcesso, filtro para numero do processo
	 * @author msapaula
	 */
	public List consultarPreAnalisesMultiplas(String id_ServentiaCargo, String id_UsuarioServentia, String numeroProcesso) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, PROC_NUMERO_COMPLETO, ID_ARQ, DATA_INSERCAO, ID_ARQ_TIPO, ID_PROC,";
		sql += "  ARQ_TIPO FROM PROJUDI.VIEW_PRE_ANALISE_PEND pre";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += "		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) > ? ";
		ps.adicionarLong(1);
		if (id_ServentiaCargo != null){
			sql += " AND pre.ID_SERV_CARGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
		}
		else if (id_UsuarioServentia != null) {
			sql += " AND pre.ID_USU_RESP = ? ";
			ps.adicionarLong(id_UsuarioServentia);
		}
		sql += " AND pre.PEND_TIPO_CODIGO IN (? ,?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";
			ps.adicionarLong(Funcoes.desformataNumeroProcesso(numeroProcesso));
		}
		sql += " ORDER BY pre.DATA_INSERCAO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));

				PendenciaDt pendenciadt = new PendenciaDt();
				pendenciaArquivoDt.setPendenciaDt(pendenciadt);
				pendenciaArquivoDt.getPendenciaDt().setId_Processo(rs1.getString("ID_PROC"));
				
				ArquivoDt arquivoDt = new ArquivoDt();
				arquivoDt.setId(rs1.getString("ID_ARQ"));
				arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				pendenciaArquivoDt.setArquivoDt(arquivoDt);
				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return preAnalises;
	}

	/**
	 * Retorna o arquivo de pré-análise (texto feito pelo assistente ou pelo próprio juiz),
	 * já retornando o responsável pela pré-analise.
	 * 
	 * @param id_Pendencia, pendência para a qual será retornado o arquivo da pré-analise
	 * @return objeto PendenciaArquivoDt referente ao arquivo da pré-análise
	 * @author msapaula
	 */
	public PendenciaArquivoDt getArquivoPreAnalise(String id_Pendencia) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{

//			String sql = "SELECT pa.*, (SELECT COUNT(pa1.ID_ARQ) AS quantidade";
//			sql += " FROM PROJUDI.PEND_ARQ pa1 group by pa1.ID_ARQ";
//			sql += " having (pa1.ID_ARQ = pa.ID_ARQ)) MULTIPLO";
//			sql += " FROM PROJUDI.VIEW_PRE_ANALISE_PEND pa";
//			sql += " WHERE pa.ID_PEND = ? ";
			
			String sql = "SELECT  pap.*, ( SELECT COUNT(1) as quantidade FROM PROJUDI.PEND_ARQ pa where pa.ID_ARQ = pap.ID_ARQ ) MULTIPLO";
			sql += " FROM PROJUDI.VIEW_PRE_ANALISE_PEND pap	WHERE  pap.ID_PEND = ? ";	ps.adicionarLong(id_Pendencia);						

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);
				if (rs1.getInt("MULTIPLO") > 1) pendenciaArquivoDt.setMultiplo(true);

				//Seta dados do arquivo
				ArquivoDt arquivo = new ArquivoDt();
				arquivo.setId(rs1.getString("ID_ARQ"));
				arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivo.setArquivo(rs1.getBytes("ARQ"));
				arquivo.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				pendenciaArquivoDt.setArquivoDt(arquivo);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return pendenciaArquivoDt;
	}
	
	/**
	 * Retorna o arquivo de pré-análise (texto feito pelo assistente ou pelo próprio juiz),
	 * já retornando o responsável pela pré-analise.
	 * 
	 * @param id_Pendencia, pendência para a qual será retornado o arquivo da pré-analise
	 * @return objeto PendenciaArquivoDt referente ao arquivo da pré-análise
	 * @author msapaula
	 */
	public PendenciaArquivoDt getArquivoPreAnaliseFinalizada(String id_Pendencia) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{

			String sql = "SELECT  pap.*, ( SELECT COUNT(1) as quantidade FROM PROJUDI.PEND_FINAL_ARQ pa where pa.ID_ARQ = pap.ID_ARQ ) MULTIPLO";
			sql += " FROM PROJUDI.VIEW_PRE_ANALISE_PEND_FINALIZA pap	WHERE  pap.ID_PEND = ? ";	ps.adicionarLong(id_Pendencia);						

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);
				if (rs1.getInt("MULTIPLO") > 1) pendenciaArquivoDt.setMultiplo(true);

				//Seta dados do arquivo
				ArquivoDt arquivo = new ArquivoDt();
				arquivo.setId(rs1.getString("ID_ARQ"));
				arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivo.setArquivo(rs1.getBytes("ARQ"));
				arquivo.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				pendenciaArquivoDt.setArquivoDt(arquivo);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return pendenciaArquivoDt;
	}

	/**
	 * Consulta as pré-análises de Pendências que já foram finalizadas, ou seja, assinadas por um usuário chefe.
	 *
	 * @param id_ServentiaCargo, identificação do cargo para filtrar pré-análises
	 * @param id_UsuarioServentia, identificação do usuario para filtrar pré-análises
	 * @param numeroProcesso, filtro de número de processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param dataInicial, filtro data início
	 * @param dataFinal, filtro data Final
	 * @author msapaula
	 */
	public List consultarPreAnalisesFinalizadas(String id_ServentiaCargo, String id_UsuarioServentia, String numeroProcesso, String digitoVerificador, String dataInicial, String dataFinal) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String ComplementoSQL = " WHERE ";

		String sql = "SELECT * FROM PROJUDI.VIEW_PRE_ANALISE_PEND_FINALIZA pre ";

		if (id_ServentiaCargo != null){
			sql += ComplementoSQL + " pre.ID_SERV_CARGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
			ComplementoSQL = " AND ";
		}
		if (id_UsuarioServentia != null){
			sql += ComplementoSQL + " pre.ID_USU_RESP = ? ";
			ps.adicionarLong(id_UsuarioServentia);
			ComplementoSQL = " AND "; 
		}
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += ComplementoSQL + " pre.PROC_NUMERO = ? ";
			ps.adicionarString(numeroProcesso);
			ComplementoSQL = " AND ";
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += ComplementoSQL + " pre.DIGITO_VERIFICADOR = ? ";
			ps.adicionarString(digitoVerificador);
			ComplementoSQL = " AND ";
		}
		if (dataInicial != null && dataInicial.length() > 0) {
			sql += ComplementoSQL + " pre.DATA_FIM >= ? ";
			ps.adicionarDateTime(dataInicial + " 00:00:00");
		}
		if (dataFinal != null && dataFinal.length() > 0){
			sql += ComplementoSQL + " pre.DATA_FIM <= ? ";
			ps.adicionarDateTime(dataFinal + " 23:59:59");
		}
		sql += " ORDER BY pre.PEND_TIPO, pre.DATA_FIM desc";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataFim(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setUsuarioFinalizador(rs1.getString("USU_FINALIZADOR"));
				//pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setPendenciaStatusCodigo(rs1.getString("PEND_STATUS_CODIGO"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);
				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return preAnalises;
	}

	/**
	 * Retorna os arquivos de resposta de uma determinada pendência
	 * @author msapaula
	 * @param id_Pendencia, id da pendencia
	 * @return lista de pendencias com arquivos (Lista de PendenciaArquivoDt)
	 * @throws Exception
	 */
	public List consultarArquivosRespostaPendencia(String id_Pendencia) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_ARQ_PEND_COMPLETA";
		sql += " WHERE ID_PEND = ? AND RESPOSTA = ? ";
		ps.adicionarLong(id_Pendencia);
		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);

				//Se e para vir com os dados do arquivo 
				ArquivoDt arquivoDt = new ArquivoDt();

				this.associarArquivo(arquivoDt, rs1);

				pendenciaArquivoDt.setArquivoDt(arquivoDt);

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	
	/**
	 * Retorna os arquivos de resposta de uma determinada pendência
	 * @author lsbernardes
	 * @param id_Pendencia, id da pendencia
	 * @return lista de pendencias com arquivos (Lista de PendenciaArquivoDt)
	 * @throws Exception
	 */
	public List consultarArquivosRespostaPendenciaFinalizada(String id_Pendencia) throws Exception {
		List arquivos = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PEND_FINAL_ARQ_COMPLETA";
		sql += " WHERE ID_PEND = ? AND RESPOSTA = ? ";
		ps.adicionarLong(id_Pendencia);
		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				this.associarDt(pendenciaArquivoDt, rs1);

				//Se e para vir com os dados do arquivo 
				ArquivoDt arquivoDt = new ArquivoDt();

				this.associarArquivo(arquivoDt, rs1);

				pendenciaArquivoDt.setArquivoDt(arquivoDt);

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	
	/**
	 * Consulta as pré-análises simples pendentes de assinatura efetuadas por um Usuário.
	 * Retorna as pré-analises em aberto.
	 * O juiz vê as suas pré-analises e dos assistentes, e os assistentes também
	 * poderá ver as suas, de outros assistentes e outros juizes
	 * 
	 * @param id_ServentiaCargo, identificação do cargo para o qual será retornado as pré-analises 
	 * @param numeroProcesso, filtro para número do processo
	 * @param id_Classificador, filtro para classificador
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author mmgomes
	 */
	public List consultarPreAnalisesConclusaoSimplesPendentesAssinatura(String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, ASSISTENTE, JUIZ, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, PEND_TIPO,";
		sql += " DATA_INICIO, ID_PROC, PROC_NUMERO_COMPLETO, ID_CLASSIFICADOR, CLASSIFICADOR, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM, CODIGO_TEMP ";
		sql += 	", (SELECT PROC_TIPO";
		sql += 	"     FROM PROJUDI.AUDI_PROC AP";
		sql += 	"     INNER JOIN PROJUDI.AUDI A ON (A.ID_AUDI = AP.ID_AUDI)";
		sql += 	"     INNER JOIN PROJUDI.PROC_TIPO PT ON (AP.ID_PROC_TIPO = PT.ID_PROC_TIPO)";
		sql += 	"     INNER JOIN PROJUDI.AUDI_TIPO AT ON (AT.ID_AUDI_TIPO = A.ID_AUDI_TIPO)";
		sql += 	"     INNER JOIN PROJUDI.AUDI_PROC_STATUS APS ON (APS.ID_AUDI_PROC_STATUS = AP.ID_AUDI_PROC_STATUS)";
		sql += "     WHERE AT.AUDI_TIPO_CODIGO = ? "; ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
        sql += "      AND APS.AUDI_PROC_STATUS_CODIGO = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
        sql += "      AND AP.ID_PROC IS NOT NULL AND AP.DATA_MOVI IS NULL";
        sql += "      AND (AP.ID_PEND_VOTO IS NULL OR AP.ID_PEND_VOTO = pre.ID_PEND OR AP.ID_PEND_VOTO_REDATOR = pre.ID_PEND)";
        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
    		sql += "  AND (AP.ID_SERV_CARGO = ? OR AP.ID_SERV_CARGO_REDATOR = ?)";
    		ps.adicionarLong(id_ServentiaCargo);
    		ps.adicionarLong(id_ServentiaCargo);
        }
        sql += " AND AP.ID_PROC = pre.ID_PROC) AS PROC_TIPO_SESSAO ";
        
        sql += ",			(" + 
        		"	SELECT DISTINCT" + 
        		"		PROC_TIPO" + 
        		"	FROM" + 
        		"		PROJUDI.PROC_TIPO PT" + 
        		"	JOIN RECURSO_SECUNDARIO_PARTE RCP ON RCP.ID_PROC_TIPO_RECURSO_SEC = PT.ID_PROC_TIPO" + 
        		"	JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = RCP.ID_AUDI_PROC" + 
        		"	WHERE" + 
        		"		AP.DATA_MOVI IS NULL" + 
        		"		AND AP.ID_PROC = pre.ID_PROC) AS PROC_TIPO_RECURSO";
        
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pre ";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		sql += " AND pre.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";
			ps.adicionarLong(digitoVerificador);			
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
		} else {
			sql += " AND NOT pre.PEND_TIPO_CODIGO IN (? , ?) ";
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sql += " AND pre.ID_CLASSIFICADOR = ? ";
			ps.adicionarLong(id_Classificador);
		}
		sql += " AND pre.CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " ORDER BY pre.PEND_TIPO, pre.PRIOR_CLASSIFICADOR desc, pre.ID_CLASSIFICADOR, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				pendenciaArquivoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_SESSAO"));;
				processoDt.setRecursoDt(new RecursoDt());
				processoDt.getRecursoDt().setProcessoTipoRecursoParteAtual(rs1.getString("PROC_TIPO_RECURSO"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}
	
	/**
	 *Atualize o status da pré-análise de Normal para Aguardando Assinatura
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_Pendencia
	 * @throws Exception 
	 */
	public void atualizeStatusPreAnalisesConclusaoSimplesParaPendentesAssinatura(String Id_Pendencia) throws Exception{
		atualizeStatusPreAnalisesConclusaoSimples(Id_Pendencia, true);
	}
	
	/**
	 * Atualize o status da pré-análise de Aguardando Assinatura para Normal
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_Pendencia
	 * @throws Exception 
	 */
	public void atualizeStatusPreAnalisesConclusaoSimplesParaNaoPendentesAssinatura(String Id_Pendencia) throws Exception{
		atualizeStatusPreAnalisesConclusaoSimples(Id_Pendencia, false);
	}
	
	/**
	 * Atualize o status da pré-análise de Aguardando Assinatura para Normal ou de Normal para Aguardando Assinatura
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_Pendencia
	 * @param ehPendenciaAssinatura
	 * @throws Exception 
	 */
	private void atualizeStatusPreAnalisesConclusaoSimples(String Id_Pendencia, boolean ehPendenciaAssinatura) throws Exception{
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql1 = "SELECT pa.id_pend_arq FROM PROJUDI.PEND p";
		sql1 += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ?)";
		ps.adicionarLong(1);
		sql1 += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql1 += " WHERE pt.PEND_TIPO_CODIGO IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		sql1 += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL";		
		sql1 += " AND p.ID_PEND = ? ";
		ps.adicionarLong(Id_Pendencia);
		
		rs1 = consultar(sql1, ps);
		
		if (rs1.next()){	
			String sql2 = "UPDATE  PROJUDI.PEND_ARQ pa set pa.CODIGO_TEMP = ? WHERE pa.id_pend_arq = ? ";
			ps.limpar();
			ps.adicionarLong((ehPendenciaAssinatura ? PendenciaArquivoDt.AGUARDANDO_ASSINATURA : PendenciaArquivoDt.NORMAL ));
			ps.adicionarLong(rs1.getLong("id_pend_arq"));
			executarUpdateDelete(sql2, ps);
		}

		
	}
	
	/**
	 * Consulta as pré-análises simples efetuadas por um Usuário que estejam pendentes de assinatura.
	 * Retorna as pré-analises em aberto.
	 * 
	 * @param id_ServentiaCargo, identificação do cargo para o qual será retornado as pré-analises 
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param numeroProcesso, filtro para número do processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author mmgomes
	 */
	public List consultarPreAnalisesSimplesPendentesAssinatura(String id_ServentiaCargo, String id_UsuarioServentia, String numeroProcesso, String digitoVerificador, String id_PendenciaTipo) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, PEND_TIPO,";
		sql += " DATA_INICIO, ID_PROC, PROC_NUMERO_COMPLETO, ID_PROC_TIPO, PROC_TIPO, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM, CODIGO_TEMP ";
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_PEND pre ";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		if (id_ServentiaCargo != null) {
			sql += " AND pre.ID_SERV_CARGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
		}
		else if (id_UsuarioServentia != null) {
			sql += " AND pre.ID_USU_RESP = ? ";
			ps.adicionarLong(id_UsuarioServentia);
		}
		sql += " AND NOT pre.PEND_TIPO_CODIGO IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);
		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0) {
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
		}
		sql += " AND pre.CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " ORDER BY pre.PEND_TIPO, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				pendenciaArquivoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}
	
	/**
	 * Consulta as pré-análises simples efetuadas por um Usuário que estejam pendentes de assinatura.
	 * Retorna as pré-analises em aberto.
	 * 
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param numeroProcesso, filtro para número do processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author lsbernardes
	 */
	public List consultarPreAnalisesSimplesPendentesAssinatura(String id_UsuarioServentia, String numeroProcesso, String digitoVerificador, String id_PendenciaTipo) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, PEND_TIPO, ID_CLASSIFICADOR, CLASSIFICADOR, PRIORI_CLASSIFICADOR, ";
		sql += " DATA_INICIO, ID_PROC, PROC_NUMERO_COMPLETO, ID_PROC_TIPO, PROC_TIPO, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM, CODIGO_TEMP ";
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_PEND pre ";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		
		sql += " AND pre.ID_USU_RESP = ? ";
		ps.adicionarLong(id_UsuarioServentia);
		
		sql += " AND pre.PEND_TIPO_CODIGO IN (?, ?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0) {
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
		}
		sql += " AND pre.CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		//sql += " ORDER BY pre.PEND_TIPO, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO";
		sql += " ORDER BY  pre.PEND_TIPO, pre.PRIORI_CLASSIFICADOR desc, pre.ID_CLASSIFICADOR, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				pendenciaArquivoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				pendenciaDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}
	
	/**
	 * Consulta as pré-análises simples efetuadas por um Usuário que estejam pendentes de assinatura.
	 * Retorna as pré-analises em aberto.
	 * 
	 * @param id_ServentiaCargo, identificação do cargo de um usuário da serventia
	 * @param numeroProcesso, filtro para número do processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param id_PendenciaTipo, filtro para tipo da pendência
	 * @author lsbernardes
	 */
	public List consultarPreAnalisesSimplesCargoPendentesAssinatura(String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_PendenciaTipo) throws Exception {
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, DATA_INSERCAO, ID_PEND, ID_PEND_TIPO, PEND_TIPO, ID_CLASSIFICADOR, CLASSIFICADOR, PRIORI_CLASSIFICADOR, ";
		sql += " DATA_INICIO, ID_PROC, PROC_NUMERO_COMPLETO, ID_PROC_TIPO, PROC_TIPO, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM, CODIGO_TEMP ";
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_PEND pre ";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		
		sql += " AND pre.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		
		sql += " AND pre.PEND_TIPO_CODIGO IN (?, ?, ?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pre.PROC_NUMERO = ? ";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pre.DIGITO_VERIFICADOR = ? ";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0) {
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
		}
		sql += " AND pre.CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		//sql += " ORDER BY pre.PEND_TIPO, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO";
		sql += " ORDER BY  pre.PEND_TIPO, pre.PRIORI_CLASSIFICADOR desc, pre.ID_CLASSIFICADOR, pre.PEND_PRIOR_ORDEM, pre.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				pendenciaArquivoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));

				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				pendenciaDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				pendenciaArquivoDt.setPendenciaDt(pendenciaDt);

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);

				preAnalises.add(pendenciaArquivoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return preAnalises;
	}
	
	/**
	 *Atualize o status da pré-análise de Normal para Aguardando Assinatura
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_PendenciaArquivo
	 * @throws Exception 
	 */
	public void atualizeStatusPreAnalisesSimplesParaPendentesAssinatura(String Id_PendenciaArquivo) throws Exception{
		atualizeStatusPreAnalisesSimples(Id_PendenciaArquivo, true);
	}
	
	/**
	 * Atualize o status da pré-análise de Aguardando Assinatura para Normal
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_PendenciaArquivo
	 * @throws Exception 
	 */
	public void atualizeStatusPreAnalisesSimplesParaNaoPendentesAssinatura(String Id_PendenciaArquivo) throws Exception{
		atualizeStatusPreAnalisesSimples(Id_PendenciaArquivo, false);
	}
	
	/**
	 * Atualize o status da pré-análise de Aguardando Assinatura para Normal ou de Normal para Aguardando Assinatura
	 * 
	 * @param id_ServentiaCargo
	 * @param Id_PendenciaArquivo
	 * @param ehPendenciaAssinatura
	 * @throws Exception 
	 */
	private void atualizeStatusPreAnalisesSimples(String Id_PendenciaArquivo, boolean ehPendenciaAssinatura) throws Exception{
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql =  "UPDATE PROJUDI.PEND_ARQ";		
		sql += " set CODIGO_TEMP = ? ";
		ps.adicionarLong((ehPendenciaAssinatura ? PendenciaArquivoDt.AGUARDANDO_ASSINATURA : PendenciaArquivoDt.NORMAL ));
		sql += " WHERE ID_PEND_ARQ = ? ";
		ps.adicionarLong(Id_PendenciaArquivo);
		
		executarUpdateDelete(sql, ps); 		
	}
	
	/**
	 * Método responsável por consultar a pendência do tipo ementa em um processo para um serventia cargo
	 * @param String id_ServentiaCargo
	 * @param String id_Processo
	 * @param String id_ProcessoTipoSessao
	 * @param String id_Pendencia	 
	 * @return PendenciaArquivoDt
	 * @throws Exception
	 * @author mmgomes
	 */
	public PendenciaArquivoDt consultaEmentaDesembargador(String id_ServentiaCargo, String idAudiProc, String id_ProcessoTipoSessao, String id_Pendencia) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{

			String sql = "SELECT pa.ID_PEND_ARQ, pa.ID_PEND, pa.PEND_TIPO, pa.ID_SERV_CARGO, pa.ARQ, " +
							" pa.ID_ARQ, pa.ID_ARQ_TIPO, pa.ARQ_TIPO, pa.NOME_ARQ, pa.DATA_INSERCAO" + 
							" ASSISTENTE, JUIZ, DATA_INSERCAO, ASSISTENTE_SEGUNDO_GRAU";
			sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pa";
			sql += " INNER JOIN AUDI_PROC AP ON AP.ID_PROC = PA.ID_PROC ";
			sql += " LEFT JOIN AUDI_PROC_PEND APP ON APP.ID_AUDI_PROC = AP.ID_AUDI_PROC ";
			sql += " WHERE pa.ID_SERV_CARGO = ? AND AP.ID_AUDI_PROC = ? AND pa.DATA_FIM IS NULL AND pa.PEND_TIPO_CODIGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
			ps.adicionarLong(idAudiProc);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
			if (id_ProcessoTipoSessao != null && id_ProcessoTipoSessao.trim().length() > 0) {
				sql += " AND EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap WHERE ap.ID_PROC_TIPO = ? AND pa.ID_PROC = ap.ID_PROC AND (ap.ID_PEND_EMENTA = pa.ID_PEND OR ap.ID_PEND_EMENTA_REDATOR = pa.ID_PEND)) ";
				ps.adicionarLong(id_ProcessoTipoSessao);				
			}
			if (id_Pendencia != null && id_Pendencia.trim().length() > 0) {
				sql += " AND pa.ID_PEND = ? ";
				ps.adicionarLong(id_Pendencia);				
			}

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				pendenciaArquivoDt = new PendenciaArquivoDt();
				
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setId_Pendencia(rs1.getString("ID_PEND"));
				pendenciaArquivoDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaArquivoDt.setId_Arquivo(rs1.getString("ID_ARQ"));
				
				if (rs1.getString("ASSISTENTE") != null && !rs1.getString("ASSISTENTE").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				else if (rs1.getString("ASSISTENTE_SEGUNDO_GRAU") != null && !rs1.getString("ASSISTENTE_SEGUNDO_GRAU").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE_SEGUNDO_GRAU"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				
				//Seta dados do arquivo
				ArquivoDt arquivo = new ArquivoDt();
				arquivo.setId(rs1.getString("ID_ARQ"));
				arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivo.setArquivo(rs1.getBytes("ARQ"));
				arquivo.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				pendenciaArquivoDt.setArquivoDt(arquivo);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return pendenciaArquivoDt;
	}
	
	public PendenciaArquivoDt consultarFinalizadaId(String id_PendenciaArquivo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaArquivoDt Dados=null;
		////System.out.println("....ps-ConsultaId_PendenciaArquivo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_FINAL_ARQ WHERE ID_PEND_ARQ = ?";		ps.adicionarLong(id_PendenciaArquivo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PendenciaArquivo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaArquivoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}
	
	
	public PendenciaArquivoDt consultarPendenciaArquivo(String id_arquivo )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaArquivoDt Dados=null;
		////System.out.println("....ps-ConsultaId_PendenciaArquivo)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_ARQ WHERE ID_ARQ = ?";		ps.adicionarLong(id_arquivo); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_PendenciaArquivo  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaArquivoDt();
				associarDt(Dados, rs1);
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return Dados; 
	}
	
	public List consultarArquivoFinalizarVotoPreAnalise(String idPendencia) throws Exception{
	
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaArquivos= new ArrayList();
	

		stSql= "SELECT ARQ.*"; 
		stSql += " FROM PROJUDI.PEND_VOTO_VIRTUAL PV\r\n ";
		stSql += " JOIN PROJUDI.PEND P ON PV.ID_PEND = P.ID_PEND ";
		stSql += " JOIN PROJUDI.PEND_RESP PR ON (PV.ID_PEND = PR.ID_PEND AND PR.ID_SERV_CARGO IS NOT NULL) ";
		stSql += " JOIN PROJUDI.VIEW_AUDI_PROC_COMPLETA AC ON (PV.ID_AUDI_PROC = AC.ID_AUDI_PROC AND AC.DATA_MOVI_AUDI_PROC IS NULL) ";
		stSql += " JOIN PROJUDI.VOTO_VIRTUAL_TIPO AR ON AR.ID_VOTO_VIRTUAL_TIPO = PV.ID_VOTO_VIRTUAL_TIPO";
		stSql += " JOIN PROJUDI.PEND_ARQ PA ON PA.ID_PEND = PV.ID_PEND";
		stSql += " JOIN PROJUDI.ARQ ARQ ON ARQ.ID_ARQ = PA.ID_ARQ";
		stSql += " WHERE ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)"; ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
		stSql += " AND P.ID_PEND = ?";  ps.adicionarLong(idPendencia);
		stSql += " ORDER BY ARQ.ID_ARQ DESC"; 


		try{
			rs1 = consultar(stSql,ps);
			while(rs1.next()) {
					ArquivoDt dados = new ArquivoDt();
					dados.setId(rs1.getString("ID_ARQ"));
					dados.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
					dados.setNomeArquivo(rs1.getString("NOME_ARQ"));
					listaArquivos.add(dados);
				}
		}finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return listaArquivos; 
	}

	public PendenciaArquivoDt consultarVotoDesembargadorPorIdAudienciaProcesso(String id_ServentiaCargo, String idAudienciaProcesso) throws Exception {
			PendenciaArquivoDt pendenciaArquivoDt = null;
			ResultSetTJGO rs1 = null;
			PreparedStatementTJGO ps = new PreparedStatementTJGO();
			try{

				String sql = "SELECT pa.ID_PEND_ARQ, pa.ID_PEND, pa.PEND_TIPO, pa.ID_SERV_CARGO, pa.ARQ, " +
								" pa.ID_ARQ, pa.ID_ARQ_TIPO, pa.ARQ_TIPO, pa.NOME_ARQ, pa.DATA_INSERCAO" +
						     ", ASSISTENTE, JUIZ, DATA_INSERCAO, ASSISTENTE_SEGUNDO_GRAU";
				sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pa";
				sql += " INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_PEND = pa.ID_PEND ";
				sql += " WHERE pa.ID_SERV_CARGO = ? AND pa.DATA_FIM IS NULL AND pa.PEND_TIPO_CODIGO = ? AND APP.ID_AUDI_PROC = ? ";
				ps.adicionarLong(id_ServentiaCargo);
				ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
				ps.adicionarLong(idAudienciaProcesso);

				rs1 = consultar(sql, ps);
				if (rs1.next()) {
					pendenciaArquivoDt = new PendenciaArquivoDt();
					
					pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
					pendenciaArquivoDt.setId_Pendencia(rs1.getString("ID_PEND"));
					pendenciaArquivoDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
					pendenciaArquivoDt.setId_Arquivo(rs1.getString("ID_ARQ"));
					
					if (rs1.getString("ASSISTENTE") != null && !rs1.getString("ASSISTENTE").equalsIgnoreCase(""))
						pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
					else if (rs1.getString("ASSISTENTE_SEGUNDO_GRAU") != null && !rs1.getString("ASSISTENTE_SEGUNDO_GRAU").equalsIgnoreCase(""))
						pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE_SEGUNDO_GRAU"));
					pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
					pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
					
					//Seta dados do arquivo
					ArquivoDt arquivo = new ArquivoDt();
					arquivo.setId(rs1.getString("ID_ARQ"));
					arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
					arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
					arquivo.setArquivo(rs1.getBytes("ARQ"));
					arquivo.setNomeArquivo(rs1.getString("NOME_ARQ"));
					arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

					pendenciaArquivoDt.setArquivoDt(arquivo);
				}
			
			} finally{
				try{
					if (rs1 != null) rs1.close();
				} catch(Exception e) {
				}
			}
			return pendenciaArquivoDt;
		}

	public PendenciaArquivoDt consultarEmentaDesembargadorPorIdAudienciaProcesso(String id_ServentiaCargo,
			String idAudienciaProcesso) throws Exception {
		PendenciaArquivoDt pendenciaArquivoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{

			String sql = "SELECT pa.ID_PEND_ARQ, pa.ID_PEND, pa.PEND_TIPO, pa.ID_SERV_CARGO, pa.ARQ, " +
							" pa.ID_ARQ, pa.ID_ARQ_TIPO, pa.ARQ_TIPO, pa.NOME_ARQ, pa.DATA_INSERCAO" +
					     ", ASSISTENTE, JUIZ, DATA_INSERCAO, ASSISTENTE_SEGUNDO_GRAU";
			sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pa";
			sql += " INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_PEND = pa.ID_PEND ";
			sql += " WHERE pa.ID_SERV_CARGO = ? AND pa.DATA_FIM IS NULL AND pa.PEND_TIPO_CODIGO = ? AND APP.ID_AUDI_PROC = ? ";
			ps.adicionarLong(id_ServentiaCargo);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
			ps.adicionarLong(idAudienciaProcesso);

			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				pendenciaArquivoDt = new PendenciaArquivoDt();
				
				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setId_Pendencia(rs1.getString("ID_PEND"));
				pendenciaArquivoDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaArquivoDt.setId_Arquivo(rs1.getString("ID_ARQ"));
				
				if (rs1.getString("ASSISTENTE") != null && !rs1.getString("ASSISTENTE").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE"));
				else if (rs1.getString("ASSISTENTE_SEGUNDO_GRAU") != null && !rs1.getString("ASSISTENTE_SEGUNDO_GRAU").equalsIgnoreCase(""))
					pendenciaArquivoDt.setAssistenteResponsavel(rs1.getString("ASSISTENTE_SEGUNDO_GRAU"));
				pendenciaArquivoDt.setJuizResponsavel(rs1.getString("JUIZ"));
				pendenciaArquivoDt.setDataPreAnalise(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
				
				//Seta dados do arquivo
				ArquivoDt arquivo = new ArquivoDt();
				arquivo.setId(rs1.getString("ID_ARQ"));
				arquivo.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				arquivo.setArquivoTipo(rs1.getString("ARQ_TIPO"));
				arquivo.setArquivo(rs1.getBytes("ARQ"));
				arquivo.setNomeArquivo(rs1.getString("NOME_ARQ"));
				arquivo.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));

				pendenciaArquivoDt.setArquivoDt(arquivo);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return pendenciaArquivoDt;
	}	

	public List consultarIdPendenciaPorIdArquivo(String idArquivo) throws Exception {
		String sql = StringUtils.EMPTY;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List result = new ArrayList();
		sql = "SELECT PA.ID_PEND FROM PEND_ARQ PA WHERE PA.ID_ARQ = ?";
		ps.adicionarLong(idArquivo);
		try {
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				result.add(rs1.getString("ID_PEND"));
			}
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			  }
		  }
		return result;
	}
	
	/**
	 * Consultar arquivos de resposta não assinados
	 * @author asrocha
	 * @param pendenciaDt vo da pendencia
	 * @return lista de vinculos entre pendencia e arquivos
	 * @throws Exception
	 */
	public List consultarRespostaNaoAssinados(PendenciaDt pendenciaDt, boolean comConteudo) throws Exception {
		List arquivos = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND_ARQ, ID_ARQ, NOME_ARQ, RESPOSTA, USU_ASSINADOR, DATA_INSERCAO, ARQ_TIPO, ARQ_TIPO_CODIGO, ID_ARQ_TIPO, RECIBO ";

		if (comConteudo) sql += ", ARQ ";

		sql += " FROM PROJUDI.VIEW_PEND_ARQ_COMPLETA WHERE ID_PEND = ? AND USU_ASSINADOR IS NULL ";
		ps.adicionarLong(pendenciaDt.getId());
		sql += " AND RESPOSTA = ?";
		ps.adicionarLong(1);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				if (arquivos == null) arquivos = new ArrayList();

				PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();

				pendenciaArquivoDt.setId(rs1.getString("ID_PEND_ARQ"));
				pendenciaArquivoDt.setResposta(Funcoes.FormatarLogico(rs1.getString("RESPOSTA")));

				ArquivoDt arquivoDt = new ArquivoDt();

				//Se nao for um arquivo de configuracao 
				if (!rs1.getString("ARQ_TIPO_CODIGO").trim().equals(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE)) {
					arquivoDt.setId(rs1.getString("ID_ARQ"));
					arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
					arquivoDt.setUsuarioAssinador(rs1.getString("USU_ASSINADOR"));
					arquivoDt.setArquivoTipo(rs1.getString("ARQ_TIPO"));
					arquivoDt.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INSERCAO")));
					arquivoDt.setId_ArquivoTipo(rs1.getString("ID_ARQ_TIPO"));
				    arquivoDt.setRecibo(Funcoes.FormatarLogico("false"));

					//if (comConteudo) arquivoDt.setArquivo(new Compactar().descompactarBytes(rs1.getBytes("ARQ")));
					if (comConteudo) {
						arquivoDt.setArquivo(rs1.getBytes("ARQ"));
					}

					pendenciaArquivoDt.setArquivoDt(arquivoDt);
				}

				arquivos.add(pendenciaArquivoDt);
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return arquivos;
	}
	
	/**
	 * Desvincular arquivo pré-analise que sao resposta e não assinados
	 * 
	 * @author lsbernardes
	 * @param idPendencia, identificador da pendencia
	 * @param fabConexao conexao
	 * @throws Exception
	 */
	public void desvincularArquivosPreAnalise(String idPendencia) throws Exception {

		String stSql="";

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		if (idPendencia == null || idPendencia.length() == 0){
			throw new Exception();
		}

		stSql= "DELETE FROM PROJUDI.PEND_ARQ";
		stSql += " WHERE ID_PEND = ?";		ps.adicionarLong(idPendencia);
		stSql += " AND RESPOSTA = 1 ";

		executarUpdateDelete(stSql,ps);

	} 
	
	/**
	 * Retorna os arquivos de uma determinada pendencia
	 * @param pendenciaDt pojo da pendencia
	 * @param comArquivos se deseja que seja adicionado os dados do arquivo na pendencia
	 * @param somenteAssinados se deseja somente os arquivos assinados
	 * @param somenteProblema somente arquivos que nao sao de resposta
	 * @return lista de arquivos
	 * @throws Exception
	 */
	public String consultarArquivosPendenciaFinalizadaJSON(PendenciaDt pendenciaDt, UsuarioNe usuarioNe, boolean comArquivos, boolean somenteAssinados, boolean somenteProblema, boolean hash, String posicao) throws Exception {
		String arquivos = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 0;

		String sql = "SELECT ID_ARQ as descricao1, ID_PEND_ARQ as id, NOME_ARQ as descricao2, RECIBO as descricao3, ID_PEND as descricao4, PEND_TIPO as descricao5, RESPOSTA as descricao6, CODIGO_TEMP as descricao7";
		if(comArquivos){
			qtdeColunas = 12;
			sql += ", ID_ARQ_TIPO as descricao8, ARQ_TIPO as descricao9, DATA_INSERCAO as descricao10, USU_ASSINADOR as descricao11, ARQ_TIPO_CODIGO as descricao12";
			sql += " FROM PROJUDI.VIEW_PEND_FINAL_ARQ_COMPLETA";
		}else{
			qtdeColunas = 7;			
			sql += " FROM PROJUDI.VIEW_PEND_ARQ";
		}
		
		sql += " WHERE ID_PEND = ? ";
		ps.adicionarLong(pendenciaDt.getId());

		if (somenteAssinados) sql += " AND USU_ASSINADOR is not null ";
		if (somenteProblema){
			sql += " AND RESPOSTA = ? ";
			ps.adicionarLong(1);
		}		

		String sqlSelQtd = "SELECT COUNT(1) as QUANTIDADE from (" + sql + ")";
		
		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);
			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			if(hash){
				arquivos = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
			}else{
				arquivos = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
		}
		return arquivos;
	}
	
	/**
	 * Retorna os arquivos de uma determinada pendencia
	 * @author Ronneesley Moura Teles
	 * @since 02/09/2008 10:59
	 * @param pendenciaDt pojo da pendencia
	 * @param comArquivos se deseja que seja adicionado os dados do arquivo na pendencia
	 * @param somenteAssinados se deseja somente os arquivos assinados
	 * @param somenteProblema somente arquivos que nao sao de resposta
	 * @return lista de arquivos
	 * @throws Exception
	 */
	public String consultarArquivosPendenciaJSON(PendenciaDt pendenciaDt, UsuarioNe usuarioNe, boolean comArquivos, boolean somenteAssinados, boolean somenteProblema, boolean hash, String posicao) throws Exception {
		String arquivos = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 0;

		String sql = "SELECT ID_ARQ as descricao1, ID_PEND_ARQ as id, NOME_ARQ as descricao2, RECIBO as descricao3, ID_PEND as descricao4, PEND_TIPO as descricao5, RESPOSTA as descricao6, CODIGO_TEMP as descricao7";
		if(comArquivos){
			qtdeColunas = 12;
			sql += ", ID_ARQ_TIPO as descricao8, ARQ_TIPO as descricao9, DATA_INSERCAO as descricao10, USU_ASSINADOR as descricao11, ARQ_TIPO_CODIGO as descricao12";
			sql += " FROM PROJUDI.VIEW_PEND_ARQ_COMPLETA";
		}else{
			qtdeColunas = 7;
			sql += " FROM PROJUDI.VIEW_PEND_ARQ";
		}

		sql += " WHERE ID_PEND = ? ";										ps.adicionarLong(pendenciaDt.getId());
		
		if (comArquivos){
			sql += " and ARQ_TIPO_CODIGO <> ? "; 							ps.adicionarLong(ArquivoTipoDt.CONFIGURACAO_PRE_ANALISE);
		}
				
		if (somenteAssinados) {
			sql += " AND USU_ASSINADOR is not null ";
		}
		
		if (somenteProblema){
			sql += " AND RESPOSTA = ? ";									 ps.adicionarLong(1);
			
		}
		
		String sqlSelQtd = "SELECT COUNT(1) as QUANTIDADE from (" + sql + ")";
		
		sql += " ORDER BY ID_ARQ ";


		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);
			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			if(hash){
				arquivos = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
			}else{
				arquivos = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			}
		} finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
       }
		return arquivos;
	}
	
}
