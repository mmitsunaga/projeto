package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoPalavraDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CorreiosDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ListaPendenciaDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialStatusDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoIntimacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoSecundarioParteDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;
import br.gov.go.tj.projudi.dt.relatorios.UsuarioPendenciasDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.types.PendenciaTipo;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.ValidacaoUtil;

//---------------------------------------------------------
public class PendenciaPs extends PendenciaPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -5722701514710892826L;

    private PendenciaPs( ) {}
    
    public PendenciaPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Associa os dados para o dt
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 21/01/2009 14:39
	 * @throws Exception
	 */
	protected void associarDt(PendenciaDt Dados, ResultSetTJGO rs1) throws Exception{
		
		super.associarDt(Dados, rs1);

		if (Dados.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO))) {
			switch (Funcoes.StringToInt(Dados.getPendenciaTipoCodigo())) {
				case PendenciaTipoDt.ALVARA:
				case PendenciaTipoDt.ALVARA_SOLTURA:
					String[] listStatusAlvara = {"Efetivada", "Cancelada" };
					Dados.setListaStatusAguardandoRetorno(listStatusAlvara);
					break;
				case PendenciaTipoDt.INTIMACAO:
				case PendenciaTipoDt.INTIMACAO_AUDIENCIA:
					String[] listStatusIntimacao = {"Efetivada", "Não Efetivada", "Cancelada" };
					Dados.setListaStatusAguardandoRetorno(listStatusIntimacao);
					break;
				case PendenciaTipoDt.CARTA_CITACAO:
				case PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA:
					String[] listStatusCartaCitacao = {"Efetivada", "Não Efetivada", "Cancelada" };
					Dados.setListaStatusAguardandoRetorno(listStatusCartaCitacao);
					break;
				case PendenciaTipoDt.CARTA_PRECATORIA:
					String[] listStatusCartaPrecatoria = {"Efetivada", "Não Efetivada", "Cancelada" };
					Dados.setListaStatusAguardandoRetorno(listStatusCartaPrecatoria);
					break;
				case PendenciaTipoDt.OFICIO_DELEGACIA:
					String[] listStatus = {"Efetivada", "Não Efetivada", "Efetivada em Parte", "Cancelada" };
					Dados.setListaStatusAguardandoRetorno(listStatus);
					break;
				case PendenciaTipoDt.OFICIO:
					String[] listStatusOficio = {"Efetivada", "Não Efetivada", "Efetivada em Parte", "Cancelada" };
					Dados.setListaStatusAguardandoRetorno(listStatusOficio);
					break;
				case PendenciaTipoDt.MANDADO:
					String[] listStatusMandado = {"Efetivada", "Não Efetivada", "Efetivada em Parte"};
					Dados.setListaStatusAguardandoRetorno(listStatusMandado);
					break;
				case PendenciaTipoDt.EDITAL:
				case PendenciaTipoDt.CARTA_ADJUDICACAO:
				case PendenciaTipoDt.CUMPRIMENTO_GENERICO:
				case PendenciaTipoDt.PEDIDO_CONTADORIA:
				case PendenciaTipoDt.CONTADORIA_CALCULO_LIQUIDACAO:
				case PendenciaTipoDt.CONTADORIA_CALCULO_CUSTAS:
				case PendenciaTipoDt.PEDIDO_CONTADORIA_CRIMINAL:
				case PendenciaTipoDt.CONTADORIA_CALCULO_CONTA:
				case PendenciaTipoDt.CONTADORIA_CALCULO_TRIBUTOS:
				case PendenciaTipoDt.CONTADORIA_JUNTADA_DOCUMENTO:
				case PendenciaTipoDt.PEDIDO_NATJUS_SEGUNDO_GRAU:
					String[] listStatusGenerica = {"Efetivada", "Não Efetivada", "Cancelada" };
					Dados.setListaStatusAguardandoRetorno(listStatusGenerica);
					break;
				default:
					String[] listStatusDefault = {"Efetivada", "Não Efetivada", "Efetivada em Parte", "Cancelada" };
					Dados.setListaStatusAguardandoRetorno(listStatusDefault);
					break;
			}
		}
		
		//Validação para pendência do tipo Intimação via telefone.
		if(Dados.getPendenciaTipoCodigo() != null && Dados.getPendenciaTipoCodigo().equals(String.valueOf(PendenciaTipoDt.INTIMACAO_VIA_TELEFONE))){
			String[] listStatusIntimacao = {"Efetivada", "Não Efetivada" };
			Dados.setListaStatusAguardandoRetorno(listStatusIntimacao);
		}

		Dados.setServentiaUsuarioFinalizador(rs1.getString("SERV_FINALIZADOR"));
		Dados.setId_ServentiaFinalizador(rs1.getString("ID_SERV_FINALIZADOR"));
		Dados.setServentiaUsuarioCadastrador(rs1.getString("SERV_CADASTRADOR"));
		Dados.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR"));
		Dados.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));
		
		
	}

	/**
	 * Consulta as pendencias finalizadas para um usuario
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 02/12/2008 11:04
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarIntimacoesCitacoesLidas(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, PendenciaTipoDt pendenciaTipoDt, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlPrimeiraParte = " PROJUDI.VIEW_PEND_FINALIZADAS pff ";
		sqlPrimeiraParte += " INNER JOIN PROJUDI.PEND_FINAL_RESP pr on pr.ID_PEND = pff.ID_PEND WHERE ";
		
		if ((idUsuarioServentia != null && !idUsuarioServentia.equalsIgnoreCase(""))
				&& (idCargoServentia != null && !idCargoServentia.equalsIgnoreCase("")) ){
			sqlPrimeiraParte += " ( pr.ID_USU_RESP = ? OR pr.ID_SERV_CARGO = ? ) ";
			ps.adicionarLong(idUsuarioServentia); ps.adicionarLong(idCargoServentia);
			
		} else if (idUsuarioServentia != null && !idUsuarioServentia.equalsIgnoreCase("")){
			sqlPrimeiraParte += " pr.ID_USU_RESP = ? ";
			ps.adicionarLong(idUsuarioServentia);
		
		} else if (idCargoServentia != null && !idCargoServentia.equalsIgnoreCase("")){
			sqlPrimeiraParte += " pr.ID_SERV_CARGO = ? ";
			ps.adicionarLong(idCargoServentia);
		}
		
		sqlPrimeiraParte += " AND pff.PEND_STATUS_CODIGO <> ? ";
		ps.adicionarLong(PendenciaStatusDt.ID_DESCARTADA);
		
		String filtroGenerico = this.filtroIntimacaoCitacaoLidas("pff", true, pendenciaTipoDt, null, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);
		sqlPrimeiraParte += filtroGenerico;
		
		
		String sqlSegundaParte = " PROJUDI.VIEW_PEND_NORMAIS pn ";
		sqlSegundaParte += " INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = pn.ID_PEND WHERE ";
		
		if ((idUsuarioServentia != null && !idUsuarioServentia.equalsIgnoreCase(""))
				&& (idCargoServentia != null && !idCargoServentia.equalsIgnoreCase("")) ){
			sqlSegundaParte += " ( pr.ID_USU_RESP = ? OR pr.ID_SERV_CARGO = ? ) ";
			ps.adicionarLong(idUsuarioServentia); ps.adicionarLong(idCargoServentia);
			
		} else if (idUsuarioServentia != null && !idUsuarioServentia.equalsIgnoreCase("")){
			sqlSegundaParte += " pr.ID_USU_RESP = ? ";
			ps.adicionarLong(idUsuarioServentia);
		
		} else if (idCargoServentia != null && !idCargoServentia.equalsIgnoreCase("")){
			sqlSegundaParte += " pr.ID_SERV_CARGO = ? ";
			ps.adicionarLong(idCargoServentia);
		}
		
		sqlSegundaParte += " AND pn.PEND_STATUS_CODIGO <> ? ";
		ps.adicionarLong(PendenciaStatusDt.ID_DESCARTADA);
		
		sqlSegundaParte += " AND pn.data_fim IS NOT NULL ";

		String filtroGenerico2 = this.filtroIntimacaoCitacaoLidas("pn", true, pendenciaTipoDt, null, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);
		sqlSegundaParte += filtroGenerico2;
		
		String sql1 = "SELECT pff.id_pend, pff.id_proc, pff.proc_numero_completo, pff.movi, pff.pend_tipo, pff.data_fim, pff.data_limite, pff.data_visto, pff.CODIGO_TEMP FROM " + sqlPrimeiraParte;
		String sql2 = " UNION SELECT pn.id_pend, pn.id_proc, pn.proc_numero_completo, pn.movi, pn.pend_tipo, pn.data_fim, pn.data_limite, pn.data_visto, pn.CODIGO_TEMP FROM " + sqlSegundaParte;

		try{
			rs1 = this.consultarPaginacao(sql1+sql2+" ORDER BY DATA_FIM desc ", ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendenciaTipo( rs1.getString("PEND_TIPO"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setDataFim( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setDataVisto( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_VISTO")));
				pendenciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));

				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}

			String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM ("+sql1+sql2+")";
			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}

	/**
	 * Consulta as pendencias distribuidas de uma promotoria
	 * 
	 * @author lsbernardes
	 * @since 23/02/2011
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarIntimacoesDistribuidasPromotoria(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_INTIMACOES_PROM pip WHERE pip.CODIGO_TEMP = ? AND pip.DATA_FIM IS NULL AND pip.ID_SERV = ? ";
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		
		boolean add = true;
		String filtroGenerico = this.filtroIntimacaoDistribuidas("pip", add, null, null, numero_processo, dataInicialInicio, dataFinalInicio, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_INICIO desc";

		sql = "SELECT * FROM " + sql;	

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setPendenciaStatusCodigo(rs1.getString("PEND_STATUS_CODIGO"));
				
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias distribuidas de uma procuradoria
	 * 
	 * @author lsbernardes
	 * @since 21/03/2011
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarIntimacoesDistribuidasProcuradoria(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_INTIMACOES_PROC pip WHERE pip.CODIGO_TEMP = ? AND pip.DATA_FIM IS NULL AND pip.ID_SERV = ? ";
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());

		boolean add = true;
		String filtroGenerico = this.filtroIntimacaoDistribuidas("pip", add, null, null, numero_processo, dataInicialInicio, dataFinalInicio, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_INICIO desc";

		sql = "SELECT DISTINCT ID_PEND, ID_PROC, PROC_NUMERO_COMPLETO, ID_MOVI, MOVI_TIPO, PEND_TIPO, DATA_INICIO, PEND_STATUS, PEND_STATUS_CODIGO   FROM " + sql;
		
		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setPendenciaStatusCodigo(rs1.getString("PEND_STATUS_CODIGO"));
				
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd,ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consutlar pendencias de prazos decorridos ou a decorrer
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 13:51
	 * @param usuarioDt
	 *            usuario que executa a acao
	 * @param PendenciaTipoDt
	 *            tipo da pendencia
	 * @param numero_processo
	 *            numero do processo
	 * @param posicao
	 *            paginacao
	 * @return lista de pendencias com prazos
	 * @throws Exception
	 */
	public List consultarPrazosDecorridosADecorrer(UsuarioDt usuarioDt, PendenciaTipoDt pendenciaTipoDt, String numero_processo, String posicao,boolean aDecorrer) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "";
		
		if (aDecorrer)
			sql += " PROJUDI.VIEW_PEND_PRAZO_A_DECO_RESP ";
		else
			sql += " PROJUDI.VIEW_PEND_PRAZO_DECOR_RESP ";

		sql += " ppd WHERE " + " ppd.ID_SERV = ? ";
		ps.adicionarLong(usuarioDt.getId_Serventia());

		String filtroGenerico = this.filtroGenerico("ppd", true, pendenciaTipoDt, null, null, null, numero_processo, null, null, null, null, ps);

		sql += filtroGenerico;
		
		if (aDecorrer)
			sql += " ORDER BY DATA_LIMITE ";
		else
			sql += " ORDER BY DATA_LIMITE ";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql = "SELECT * FROM " + sql;	

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consutlar pendencias de prazos decorridos
	 * 
	 * @param usuarioDt
	 *            usuario que executa a acao
	 * @param PendenciaTipoDt
	 *            tipo da pendencia
	 * @param numero_processo
	 *            numero do processo
	 * @param posicao
	 *            paginacao
	 * @return lista de pendencias com prazos
	 * @throws Exception
	 */
	public List consultarPrazosDecorridosDevolucaoAutos(UsuarioDt usuarioDt, String numero_processo, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "";
		
		sql += " PROJUDI.VIEW_PEND_PRAZO_DECOR_SOL_CAR ";

		sql += " ppd WHERE " + " ppd.ID_SERV = ? ";
		ps.adicionarLong(usuarioDt.getId_Serventia());
		
		String filtroGenerico = this.filtroGenerico("ppd", true, null, null, null, null, numero_processo, null, null, null, null, ps);

		sql += filtroGenerico;
		
		sql += " ORDER BY DATA_LIMITE ";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql = "SELECT * FROM " + sql;	

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR") +" - " + rs1.getString("SERV_CADASTRADOR") );
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}

	/**
	 * Consutlar pendencias para leitura automatica
	 * 
	 * @author Leandro Bernardes
	 * @since 26/10/2009
	 * @return lista de pendencias
	 * @throws Exception
	 */
	public List consultarPendenciasLeituraAutomatica() throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;		

		String sql = " SELECT * FROM PROJUDI.VIEW_PEND_LEITURA_AUTOMATICA ";

		try{
			rs1 = this.consultarSemParametros(sql);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));
				pendencias.add(pendenciaDt);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}
	
	/**
	 * Consutlar pendencias para leitura automatica
	 * 
	 * @author Leandro Bernardes
	 * @return lista de pendencias
	 * @throws Exception
	 */
	public List consultarPendenciasFechamentoAutomatica() throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;

		String sql = " SELECT * FROM PROJUDI.VIEW_PEND_FECHAMENTO_AUTO ";

		try{
			rs1 = this.consultarSemParametros(sql);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));
				pendencias.add(pendenciaDt);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}
	
	/**
	 * Consutlar pendencias para Fechamento automatica - solicitação de carga
	 * 
	 * @author Leandro Bernardes
	 * @return lista de pendencias
	 * @throws Exception
	 */
	public List consultarPendenciasSolicitacaoCargaFechamentoAutomatica() throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;

		String sql = " SELECT * FROM PROJUDI.VIEW_PEND_SOL_CARG_FEC_AUT ";

		try{
			rs1 = this.consultarSemParametros(sql);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}

	/**
	 * Consutlar pendencias de prazos decorridos da serventia
	 * 
	 * @author Leandro Bernardes
	 * @since 21/08/2009
	 * @param usuarioDt
	 *            usuario que executa a acao
	 * @param posicao
	 *            paginacao
	 * @return quantidade de pendencia com prazo decorrido na serventia
	 * @throws Exception
	 */
	public Long consultarQtdPrazosDecorridos(UsuarioDt usuarioDt) throws Exception {

		Long qtdPrazoDecorrido = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//String sql = "SELECT COUNT(*) as qtdPrazoDecorrido FROM PROJUDI.VIEW_PEND_PRAZO_DECOR_RESP ppd WHERE " + " ppd.ID_SERV = " + usuarioDt.getId_Serventia() + " ";

		String sql = " SELECT COUNT(*) as QTD_PRAZO_DECORRIDO  FROM  PROJUDI.PEND pen "
						+ "INNER JOIN PROJUDI.PEND_RESP pr on((pr.ID_PEND = pen.ID_PEND)) "
						+ "INNER JOIN PROJUDI.PEND_TIPO pt on((pt.ID_PEND_TIPO = pen.ID_PEND_TIPO)) ";
			   sql += " WHERE ( (pen.DATA_FIM is not null ) AND (pen.DATA_VISTO IS NULL) AND (pen.DATA_LIMITE < SYSDATE)) AND pr.ID_SERV = ? ";
			   ps.adicionarLong(usuarioDt.getId_Serventia());
		
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				qtdPrazoDecorrido = rs1.getLong("QTD_PRAZO_DECORRIDO");
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return qtdPrazoDecorrido;
	}
	
	/**
	 * Consutlar pendencias de prazos decorridos da serventia
	 * 
	 * @author Leandro Bernardes
	 * @param usuarioDt
	 *            usuario que executa a acao
	 * @param posicao
	 *            paginacao
	 * @return quantidade de pendencia com prazo decorrido na serventia
	 * @throws Exception
	 */
	public Long consultarQtdPrazosDecorridosDevolucaoAutos(UsuarioDt usuarioDt) throws Exception {

		Long qtdPrazoDecorrido = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//String sql = "SELECT COUNT(*) as qtdPrazoDecorrido FROM PROJUDI.VIEW_PEND_PRAZO_DECOR_RESP ppd WHERE " + " ppd.ID_SERV = " + usuarioDt.getId_Serventia() + " ";

		String sql = " SELECT COUNT(*) as QTD_PRAZO_DECORRIDO  FROM  PROJUDI.PEND pen "
						+ "INNER JOIN PROJUDI.PEND_RESP pr on((pr.ID_PEND = pen.ID_PEND)) "
						+ "INNER JOIN PROJUDI.PEND_TIPO pt on((pt.ID_PEND_TIPO = pen.ID_PEND_TIPO)) ";
			   sql += " WHERE ( pen.DATA_FIM IS NULL AND pt.PEND_TIPO_CODIGO = ?  AND pen.DATA_VISTO IS NULL AND pen.DATA_LIMITE < SYSDATE AND pr.ID_SERV = ? )";
			   ps.adicionarLong(PendenciaTipoDt.SOLICITACAO_CARGA);
			   ps.adicionarLong(usuarioDt.getId_Serventia());
		
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				qtdPrazoDecorrido = rs1.getLong("QTD_PRAZO_DECORRIDO");
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return qtdPrazoDecorrido;
	}
	
	/**
	 * Consultar pendencias que liberam acesso ao processo 
	 * 
	 * @author Leandro Bernardes
	 * @param usuarioDt
	 *            usuario que executa a acao
	 * @param posicao
	 *            paginacao
	 * @return quantidade de pendencia com prazo decorrido na serventia
	 * @throws Exception
	 */
	public Long consultarQtdPendenciaLiberaAcesso(UsuarioDt usuarioDt) throws Exception {

		Long qtdPendenciaLiberaAcesso = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT count(1) QTD_PENDENCIA_LIBERA_ACESSO  FROM  PROJUDI.VIEW_PEND_ABERTAS_RESP pen " +
			    " WHERE pen.DATA_FIM IS NULL AND pen.DATA_LIMITE > SYSDATE " +
		        " AND pen.PEND_TIPO_CODIGO = ?";												ps.adicionarLong(PendenciaTipoDt.LIBERACAO_ACESSO);		
	
		// pega a liberação de acesso de qualquer lugar (id_serv_cargo ou id_usu_serv)
		if (usuarioDt.isQualquerAssessor()){
			sql += " AND ( pen.ID_USU_RESP = ? )";													ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
		}else{
			sql += " AND ( pen.ID_USU_RESP = ? )";													ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
		}


		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				qtdPendenciaLiberaAcesso = rs1.getLong("QTD_PENDENCIA_LIBERA_ACESSO");
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return qtdPendenciaLiberaAcesso;
	}
	
	/**
	 * Consultar pendencias informativas de um tipo de serventia
	 * 
	 * @author Leandro Bernardes
	 * @param usuarioDt
	 *            usuario que executa a acao
	 * @param posicao
	 *            paginacao
	 * @return quantidade de pendencia com prazo decorrido na serventia
	 * @throws Exception
	 */
	public Long consultarQtdPendenciaInformativa(UsuarioDt usuarioDt) throws Exception {

		Long qtdPendenciaInformativa = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT COUNT(*) as QTD_PENDENCIA_INFORMATIVA  FROM  PROJUDI.PEND pen " +
				" INNER JOIN PROJUDI.PEND_RESP pr on((pr.ID_PEND = pen.ID_PEND)) "+
		        " INNER JOIN PROJUDI.PEND_TIPO pt on((pt.ID_PEND_TIPO = pen.ID_PEND_TIPO)) "+
			    " WHERE pen.DATA_FIM IS NULL AND pen.DATA_LIMITE > SYSDATE " +
			        " AND pt.PEND_TIPO_CODIGO = ? " + 
			   		" AND ( pr.ID_SERV_TIPO = ? " + " OR pr.ID_SERV = ?)";
		ps.adicionarLong(PendenciaTipoDt.INFORMATIVO);
		ps.adicionarLong(usuarioDt.getId_ServentiaTipo());
		ps.adicionarLong(usuarioDt.getId_Serventia());
		
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				qtdPendenciaInformativa = rs1.getLong("QTD_PENDENCIA_INFORMATIVA");
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return qtdPendenciaInformativa;
	}
	
	public List consultarPendenciasLiberarAcessoComHash(UsuarioNe usuarioNe) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT *  FROM  PROJUDI.VIEW_PEND_ABERTAS_RESP pen " +
				    " WHERE pen.DATA_FIM IS NULL AND pen.DATA_LIMITE > SYSDATE " +
			        " AND pen.PEND_TIPO_CODIGO = ?";												ps.adicionarLong(PendenciaTipoDt.LIBERACAO_ACESSO);		
		
		// pega a liberação de acesso de qualquer lugar (id_serv_cargo ou id_usu_serv)
		if (usuarioNe.isQualquerAssessor()){
			sql += " AND ( pen.ID_SERV_CARGO = ? ";												ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
			sql += " OR pen.ID_USU_RESP = ? )";													ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
		}else{
			sql += " AND (pen.ID_SERV_CARGO = ? ";												ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargo());
			sql += " OR pen.ID_USU_RESP = ? )";													ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		}

		
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo( rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo( rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR"));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}
	
	public boolean consultarPendenciasLiberarAcessoWebservice(UsuarioNe usuarioNe, String idProcesso) throws Exception {

		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean existeLiberarAcesso = false;

		String sql = " SELECT *  FROM  PROJUDI.VIEW_PEND_ABERTAS_RESP pen WHERE pen.DATA_FIM IS NULL AND pen.DATA_LIMITE > SYSDATE AND pen.PEND_TIPO_CODIGO = ? AND pen.ID_PROC= ?";
		ps.adicionarLong(PendenciaTipoDt.LIBERACAO_ACESSO);		
		ps.adicionarLong(idProcesso);		
		
		if (Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo()) != GrupoTipoDt.ADVOGADO){
			if (usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe().equals("")){
				sql += " AND pen.ID_SERV_CARGO = ? ";
				ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
			}else{
				sql += " AND pen.ID_SERV_CARGO = ? ";
				ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargo());
			}
		} else {
			if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equals("")){
				sql += " AND pen.ID_USU_RESP = ? ";
				ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
			} else {
				sql += " AND pen.ID_USU_RESP = ? ";
				ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
			}
		}
		
		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) existeLiberarAcesso = true;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return existeLiberarAcesso;
	}
	
	public List consultarPendenciasInformativas(UsuarioNe usuarioNe) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

//		String sql = " SELECT *  FROM  PROJUDI.VIEW_PEND_ABERTAS_RESP pen " +
//				    " WHERE pen.DATA_FIM IS NULL AND pen.DATA_LIMITE > SYSDATE " +
//			        " AND pen.PEND_TIPO_CODIGO = " + PendenciaTipoDt.INFORMATIVO+ 
//			   		" AND pen.ID_SERV_TIPO = " + usuarioNe.getUsuarioDt().getId_ServentiaTipo() + " ";
		
		String sql = " SELECT pen.ID_PEND, pt.ID_PEND_TIPO, pt.PEND_TIPO, pen.DATA_INICIO, pen.DATA_LIMITE, " +
				    " u1.NOME AS NOME_USU_CADASTRADOR " +
				    " FROM  PROJUDI.PEND pen " +  
				    " JOIN PROJUDI.PEND_RESP pr on((pr.ID_PEND = pen.ID_PEND)) " +  
				    " JOIN PROJUDI.PEND_TIPO pt on((pen.ID_PEND_TIPO = pt.ID_PEND_TIPO)) " +
				    " JOIN PROJUDI.USU_SERV us1 on((pen.ID_USU_CADASTRADOR = us1.ID_USU_SERV))  " +
				    " JOIN PROJUDI.USU u1 on((us1.ID_USU = u1.ID_USU))" +  		   
		            " WHERE pen.DATA_FIM IS NULL AND pen.DATA_LIMITE > ? AND pt.PEND_TIPO_CODIGO = ? "+ 
		            " AND ( pr.ID_SERV_TIPO = ? OR pr.ID_SERV = ?) ";
		ps.adicionarDateTime(new Date());
		ps.adicionarLong(PendenciaTipoDt.INFORMATIVO);
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaTipo());
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_PendenciaTipo( rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo( rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR"));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}

	/**
	 * Consutlar pendencias de prazos decorridos de um usuario
	 * 
	 * @author Leandro Bernardes
	 * @since 13/08/2009
	 * @param usuarioDt
	 *            usuario que executa a acao
	 * @param PendenciaTipoDt
	 *            tipo da pendencia
	 * @param numero_processo
	 *            numero do processo
	 * @param posicao
	 *            paginacao
	 * @return lista de pendencias com prazos
	 * @throws Exception
	 */
	public List consultarPrazosDecorridosUsuario(UsuarioDt usuarioDt, PendenciaTipoDt pendenciaTipoDt, String numero_processo, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_PRAZO_DECORRIDO ppd WHERE " + " ppd.ID_USU_CADASTRADOR = ? ";
		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());

		String filtroGenerico = this.filtroGenerico("ppd", true, pendenciaTipoDt, null, null, null, numero_processo, null, null, null, null, ps);

		sql += filtroGenerico;
		
		sql += " ORDER BY DATA_LIMITE desc";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql = "SELECT * FROM " + sql;
		
		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}

	/**
	 * Consulta as pendencias finalizadas para um usuario
	 * 
	 * @author Ronneesley Moura Teles/ Leandro Bernardes
	 * @since 02/12/2008 11:04
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarFinalizadas(UsuarioNe usuarioNe, String id_UsuarioServentia, String id_ServentiaUsuario, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {

		List pendencias 			= new ArrayList();
		ResultSetTJGO rs1 			= null;
		ResultSetTJGO rs2 			= null;
		String sql		 			= "";
		String sqlComum 			= "";
		PreparedStatementTJGO ps	=  new PreparedStatementTJGO();
		
		
		sql = "SELECT ID_PEND, DATA_VISTO, ID_PEND_TIPO, MOVI, ID_PROC, "
				+ "PROC_NUMERO_COMPLETO, PROC_PRIOR, PEND_STATUS,  "
				+ "DATA_INICIO, DATA_FIM, DATA_LIMITE ";

		sqlComum = "FROM PROJUDI.VIEW_PEND_FINAIS_FINALIZADAS pff WHERE ";
		
		
		if (id_UsuarioServentia != null && !id_UsuarioServentia.equals("")){
			sqlComum += " ID_USU_CADASTRADOR = ?";
			ps.adicionarLong(id_UsuarioServentia);
		}else if (id_ServentiaUsuario != null && !id_ServentiaUsuario.equals("")){
			sqlComum += " (ID_SERV_CADASTRADOR = ? OR ID_SERV_FINALIZADOR = ? ) ";
			ps.adicionarLong(id_ServentiaUsuario);
			ps.adicionarLong(id_ServentiaUsuario);
		}

		sqlComum += " AND (PEND_TIPO_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		boolean add = true;
		String filtroGenerico = this.filtroGenerico("pff", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);

		sqlComum += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE " + sqlComum;

		sql += sqlComum;
		sql += " ORDER BY DATA_FIM desc";


		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataVisto( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_VISTO")));
				pendenciaDt.setId_PendenciaTipo( rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setProcessoPrioridade( rs1.getString("PROC_PRIOR"));
				pendenciaDt.setPendenciaStatus( rs1.getString("PEND_STATUS"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataFim( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	 /**
     * Listagem de pendencias fechadas independente de estarem vistadas ou não.
     * 
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
	public List consultarPendenciasFechadas(UsuarioNe usuarioNe, String id_UsuarioServentia, String id_ServentiaUsuario, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_FINALIZADAS pff WHERE ";
		if (id_UsuarioServentia != null && !id_UsuarioServentia.equals("")){
			sql += " ID_USU_CADASTRADOR = ?";
			ps.adicionarLong(id_UsuarioServentia);
		}else if (id_ServentiaUsuario != null && !id_ServentiaUsuario.equals("")){
			sql += " (ID_SERV_CADASTRADOR = ? OR ID_SERV_FINALIZADOR = ? ) ";
			ps.adicionarLong(id_ServentiaUsuario);
			ps.adicionarLong(id_ServentiaUsuario);
		}

		sql += " AND (PEND_TIPO_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		boolean add = true;
		String filtroGenerico = this.filtroGenerico("pff", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_FIM desc";

		sql = "SELECT * FROM " + sql;
		
		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias respondidas da serventia
	 * 
	 * @author Leandro Bernardes
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarRespondidasServentia(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_RESPONDIDAS pff WHERE ID_SERV_CADASTRADOR <> ? AND  ID_SERV_FINALIZADOR = ? ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());

		sql += " AND (PEND_TIPO_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		boolean add = true;
		String filtroGenerico = this.filtroGenerico("pff", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_FIM desc";

		sql = "SELECT * FROM " + sql;

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias respondidas da serventia
	 * 
	 * @author Leandro Bernardes
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarRespondidasUsuario(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_RESPONDIDAS pff WHERE ID_SERV_CADASTRADOR <> ? AND  ID_SERV_FINALIZADOR = ? AND ID_USU_FINALIZADOR = ?";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		
		sql += " AND (PEND_TIPO_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		boolean add = true;
		String filtroGenerico = this.filtroGenerico("pff", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_FIM desc";

		sql = "SELECT * FROM " + sql;		

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}

	/**
	 * Marcar visto da pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 09/01/2009 11:00
	 * @param pendenciaDt
	 *            objeto de pendencia
	 * @throws Exception
	 */
	public void marcarVisto(PendenciaDt pendenciaDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set DATA_VISTO = ?";
		sql += " WHERE ID_PEND = ?";
		ps.adicionarDateTime(pendenciaDt.getDataVisto());
		ps.adicionarLong(pendenciaDt.getId());

			this.executarUpdateDelete(sql, ps);
	}

	/**
	 * Alterar data limite
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 22/01/2009 10:05
	 * @param pendenciaDt
	 *            objeto de pendencia
	 * @throws Exception
	 */
	public void alterarLimite(PendenciaDt pendenciaDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set DATA_LIMITE = ? WHERE ID_PEND = ?";
		ps.adicionarDateTime(pendenciaDt.getDataLimite());
		ps.adicionarLong(pendenciaDt.getId());

			this.executarUpdateDelete(sql, ps);
	}

	/**
	 * Retirar data de visto
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 22/01/2009 10:03
	 * @param pendenciaDt
	 *            objeto de pendencia
	 * @throws Exception
	 */
	public void retirarVisto(PendenciaDt pendenciaDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set DATA_VISTO = null";
		sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(pendenciaDt.getId());

			this.executarUpdateDelete(sql, ps);
	}

	public void AlterarStatusPendencia(PendenciaDt pendenciaDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
		// Monta subSELECT para pesquisar o id real do status
		sql += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";		
		ps.adicionarLong(pendenciaDt.getPendenciaStatusCodigo());
		sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(pendenciaDt.getId());

			this.executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Responder uma pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 20/11/2008 17:09
	 * @param id_pendencia
	 *            id da pendencia
	 * @param id_UsuarioFinalizador
	 *            id do usuario finalizador
	 * @param pendenciaStatusCodigo
	 *            codigo do novo status
	 * @throws Exception
	 */
	private void responderPendencia(String id_pendencia, String id_UsuarioFinalizador, int pendenciaStatusCodigo, String dataFim, String dataVisto) throws MensagemException, Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			
			String sqlConsulta = " SELECT ID_PEND FROM PROJUDI.PEND WHERE ID_PEND = ? AND DATA_FIM is not null ";
			ps.adicionarLong(id_pendencia);
			
			rs1 = this.consultar(sqlConsulta, ps);
			if (rs1.next()){
				throw new MensagemException("Pendência já foi fechada favor clicar na pagina inicial para atualizar.");
			}
			
			// Limpando o comando
			ps.limpar();		
			
			String sqlUpdate = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
			// Monta subSELECT para pesquisar o id real do status
			sqlUpdate += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";
			ps.adicionarLong(pendenciaStatusCodigo);
			
			sqlUpdate += ", ID_USU_FINALIZADOR = ?, DATA_FIM = ?, DATA_VISTO = ?";
			ps.adicionarLong(id_UsuarioFinalizador);
			ps.adicionarDateTime(dataFim);
			ps.adicionarDateTime(dataVisto);
			
			sqlUpdate += " WHERE ID_PEND = ? AND DATA_FIM IS NULL ";
			ps.adicionarLong(id_pendencia);			
			
			this.executarUpdateDelete(sqlUpdate, ps);
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}
	
	/**
	 * Método que fecha uma pendência que foi cumprida e está aguardando visto.
	 * @param id_pendencia: id da pendência
	 * @param pendenciaStatusCodigo: código do status da pendência
	 * @param dataVisto: data do visto que deve ser atribuída à pendência
	 * @author hmgodinho
	 */
	public void fecharPendenciaCumpridaAguardandoVisto(String id_pendencia, int pendenciaStatusCodigo, Date dataVisto) throws MensagemException, Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			
			String sqlUpdate = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
			sqlUpdate += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";
			ps.adicionarLong(pendenciaStatusCodigo);

			sqlUpdate += ", DATA_VISTO = ?";
			ps.adicionarDateTime(dataVisto);
			
			sqlUpdate += " WHERE ID_PEND = ? ";
			ps.adicionarLong(id_pendencia);			
			
			this.executarUpdateDelete(sqlUpdate, ps);
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}
	
	/**
	 * Responder uma pendencia com Data visto
	 * 
	 * @author Leandro Bernardes
	 * @param id_pendencia
	 *            id da pendencia
	 * @param id_UsuarioFinalizador
	 *            id do usuario finalizador
	 * @param pendenciaStatusCodigo
	 *            codigo do novo status
	 * @throws Exception
	 */
	public void responder(String id_pendencia, String id_UsuarioFinalizador, int pendenciaStatusCodigo, String dataFim, String dataVisto) throws MensagemException, Exception {
		this.responderPendencia(id_pendencia, id_UsuarioFinalizador, pendenciaStatusCodigo, dataFim, dataVisto);
	}
	
	/**
	 * Responder uma pendencia
	 * 
	 * @author Leandro Bernardes
	 * @param id_pendencia
	 *            id da pendencia
	 * @param id_UsuarioFinalizador
	 *            id do usuario finalizador
	 * @param pendenciaStatusCodigo
	 *            codigo do novo status
	 * @throws Exception
	 */
	public void responder(String id_pendencia, String id_UsuarioFinalizador, int pendenciaStatusCodigo, String dataFim) throws MensagemException, Exception {
		this.responderPendencia(id_pendencia, id_UsuarioFinalizador, pendenciaStatusCodigo, dataFim, "");
	}
	
	/**
	 * descartar uma pendencia
	 * 
	 * @author Leandro Bernardes
	 * @param id_pendencia
	 *            id da pendencia
	 * @param id_UsuarioFinalizador
	 *            id do usuario finalizador
	 * @param pendenciaStatusCodigo
	 *            codigo do novo status
	 * @throws Exception
	 */
	public void descartarPendencia(String id_pendencia, String id_UsuarioFinalizador, int pendenciaStatusCodigo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
		// Monta subSELECT para pesquisar o id real do status
		sql += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?), ";
		ps.adicionarLong(pendenciaStatusCodigo);
		sql += " ID_USU_FINALIZADOR = ?"  
				+ ", DATA_FIM = ?"
				+ ", DATA_VISTO = ?";
		ps.adicionarLong(id_UsuarioFinalizador);
		ps.adicionarDateTime(new Date());
		ps.adicionarDateTime(new Date());
		sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(id_pendencia);

			this.executarUpdateDelete(sql, ps);
	}

	/**
	 * Finaliza pendencia com data fim e data visto
	 * 
	 * @author Leandro Bernardes
	 * @since 29/05/2009 17:09
	 * @param id_pendencia
	 *            id da pendencia
	 * @param id_UsuarioFinalizador
	 *            id do usuario finalizador
	 * @param pendenciaStatusCodigo
	 *            codigo do novo status
	 * @throws Exception
	 */
	public void FinalizarPendencia(String id_pendencia, String id_UsuarioFinalizador, int pendenciaStatusCodigo, boolean aguardandoParecer) throws MensagemException, Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		try{		
				
			String sqlConsulta = " SELECT ID_PEND FROM PROJUDI.PEND WHERE ID_PEND = ? AND DATA_FIM is not null ";
			ps.adicionarLong(id_pendencia);
			
			rs1 = this.consultar(sqlConsulta, ps);
			if (rs1.next()){
				throw new MensagemException("Intimação já foi lida  favor clicar na pagina inicial para atualizar.");
			}
			
			// Limpando o comando
			ps.limpar();
			
			String sqlUpdate = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
			// Monta subSELECT para pesquisar o id real do status
			sqlUpdate += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";
			ps.adicionarLong(pendenciaStatusCodigo);
			sqlUpdate += ", ID_USU_FINALIZADOR = ?, DATA_FIM = ?, DATA_VISTO = ?";
			ps.adicionarLong(id_UsuarioFinalizador);
			ps.adicionarDateTime(new Date());
			ps.adicionarDateTime(new Date());
			
			if (aguardandoParecer){
				sqlUpdate += ", CODIGO_TEMP = ?";
				ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP);
			}
			
			sqlUpdate += " WHERE ID_PEND = ? AND DATA_FIM IS NULL ";
			ps.adicionarLong(id_pendencia);
			
			this.executarUpdateDelete(sqlUpdate, ps);
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}
	
	/**
	 * Finaliza pendencia intimação aguardando parecer
	 * 
	 * @author Leandro Bernardes
	 * @param id_pendencia
	 *            id da pendencia
	 * @throws Exception
	 */
	public void finalizarPendenciaIntimacaoAguardandoParecer(String id_pendencia) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	

		String sqlConsulta = " SELECT ID_PEND FROM PROJUDI.PEND WHERE ID_PEND = ? AND CODIGO_TEMP IS NULL ";
		ps.adicionarLong(id_pendencia);
		
		try{
			
			rs1 = this.consultar(sqlConsulta, ps);
			if (rs1.next()){
				return;
			} else{
				//Limpa o comando
				ps.limpar();
				
				String sqlUpdate = "UPDATE PROJUDI.PEND set CODIGO_TEMP = null ";

				sqlUpdate += " WHERE ID_PEND = ? AND CODIGO_TEMP is not null ";
				ps.adicionarLong(id_pendencia);
				
				this.executarUpdateDelete(sqlUpdate, ps);
			}
		
		
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}
	
	
	/**
     * Método que consulta todas as pendências de intimação aguardando parecer a partir de certo período.
     * Limitado a 10 mil registros por execução.
     * 
     * @throws Exception
     * @author hmgodinho
     */
	public List consultarPendenciasIntimacaoAguardandoParecer() throws Exception {

		String sql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaPendencias = new ArrayList();

		sql = "SELECT DISTINCT u.ID_PEND "
				+ "	FROM VIEW_PEND_INTIMA_AGUAR_PARECER u "
				+ "	WHERE ";
		sql += "   		u.CODIGO_TEMP IN (?,?) ";
		ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP);
		ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_PARECER_DIARIO_ELETRONICO_CODIGO_TEMP);
		sql += "		AND ( u.ID_PEND_TIPO = ? OR u.ID_PEND_TIPO = ?)" ;
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		sql += " 		AND DATA_VISTO IS NOT NULL "
				+ "		AND (u.DATA_LIMITE < ? OR (u.DATA_LIMITE IS NULL AND u.DATA_FIM < ?)) ";
		ps.adicionarDate(Funcoes.somarData(new Date(), -30));
		ps.adicionarDate(Funcoes.somarData(new Date(), -30));
		sql += "		AND ROWNUM <= 10000 "; //limitando a consulta a dez mil registros por vez	

		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				listaPendencias.add(rs1.getString("ID_PEND"));
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		
		return listaPendencias; 
	}
	
	
	/**
	 * Marcar pendencia intimação aguardando parecer
	 * 
	 * @author Leandro Bernardes
	 * @param id_pendencia
	 *            id da pendencia
	 * @throws Exception
	 */
	public void marcarPendenciaIntimacaoAguardandoParecer(String id_pendencia) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	

		String sqlConsulta = " SELECT ID_PEND FROM PROJUDI.PEND WHERE ID_PEND = ? AND CODIGO_TEMP is not null ";
		ps.adicionarLong(id_pendencia);
		
		try{
			
			rs1 = this.consultar(sqlConsulta, ps);
			if (rs1.next()){
				throw new MensagemException("Intimação já foi marcada aguardando  favor clicar na pagina inicial para atualizar. Dupla tentativa de marcar intimação aguardando parecer.");
			}
			
			// Limpar o comando
			ps.limpar();
			
			String sqlUpdate = "UPDATE PROJUDI.PEND set CODIGO_TEMP = ?";
			ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_PARECER_CODIGO_TEMP);

			sqlUpdate += " WHERE ID_PEND = ? AND CODIGO_TEMP IS NULL ";
			ps.adicionarLong(id_pendencia);
			
			this.executarUpdateDelete(sqlUpdate, ps);
		
		
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}
	
	/**
	 * Consulta as pendencias filhas de uma pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 19/11/2008 13:20
	 * @param PendenciaDt
	 *            pendenciaDt, VO de pendencia
	 * @return List
	 * @throws Exception
	 */
	public List consultarFilhas(PendenciaDt pendenciaDt) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PEND WHERE ID_PEND_PAI = ?";
		ps.adicionarLong(pendenciaDt.getId());

		try{
			rs1 = this.consultar(sql,ps);

			while (rs1.next()) {
				PendenciaDt obj = new PendenciaDt();
				super.associarDt(obj, rs1);
				pendencias.add(obj);
			}

			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}

	/**
	 * Consulta as pendencias filhas de autos
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 22/01/2009 09:05
	 * @param pendenciaDt
	 *            VO de pendencia
	 * @param somenteFinalizadas
	 *            somente as pendencias finalizadas
	 * @return lista de pendencia filhas de autos
	 * @throws Exception
	 */
	public List consultarFilhasAutos(PendenciaDt pendenciaDt, boolean somenteFinalizadas) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PEND_SERV_CARGO WHERE ID_PEND_PAI = ?";
		ps.adicionarLong(pendenciaDt.getId());

		if (somenteFinalizadas == true) sql += " AND DATA_FIM is not null ";

		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				PendenciaDt obj = new PendenciaDt();
				super.associarDt(obj, rs1);
				pendencias.add(obj);
			}

			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias filhas de autos
	 * 
	 * @author Jesus Rodrigo
	 * @since 25/08/2014
	 * @param pendenciaDt	 *            VO de pendencia
	 * @param somenteFinalizadas
	 *            somente as pendencias finalizadas
	 * @return lista de pendencia filhas de autos
	 * @throws Exception
	 */
	public List consultarFilhasFinalizadasAutos(PendenciaDt pendenciaDt) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PEND_FINAL_SERV_CARGO WHERE ID_PEND_PAI = ?";	ps.adicionarLong(pendenciaDt.getId());		

		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				PendenciaDt obj = new PendenciaDt();
				super.associarDt(obj, rs1);
				pendencias.add(obj);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}

	/**
	 * Consulta as pendencias que o usuario cadastrador e o usuario da serventia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 12/11/2008 15:31
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @param String
	 *            posicao, posicao da paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarMinhas(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_NORMAIS pen WHERE " + " ID_USU_CADASTRADOR = ? AND DATA_VISTO IS NULL AND PEND_STATUS_CODIGO <> ? AND (DATA_LIMITE IS NULL) ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		ps.adicionarLong(PendenciaStatusDt.ID_ENCAMINHADA);

		sql += this.filtroGenerico("pen", true, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, null, null, ps);

		sql += " ORDER BY DATA_INICIO , ID_PROC, PEND_STATUS_CODIGO";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql = "SELECT * FROM " + sql;

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}

	/**
	 * Consulta as pendencias expedidas para serventias (on-line)
	 * 
	 * @author Leandro Bernardes
	 * @since 13/08/2009
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @param String
	 *            posicao, posicao da paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarExpedidasServentia(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_NORMAIS pen WHERE DATA_VISTO IS NULL  AND ID_SERV_CADASTRADOR = ?  AND PEND_STATUS_CODIGO <> ? AND (DATA_LIMITE IS NULL) ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(PendenciaStatusDt.ID_ENCAMINHADA);
		sql += this.filtroGenerico("pen", true, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, null, null, ps);

		sql += " ORDER BY DATA_INICIO , ID_PROC, PEND_STATUS_CODIGO";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql = "SELECT * FROM " + sql;

		try{
			 rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta quantidade de pendências em andamentos
	 * 
	 * @author Leandro Bernardes
	 * @param UsuarioNe
	 *            usuarioN

	 * @return Long
	 * @throws Exception
	 */
	public Long consultarQtdPendenciasAndamento(UsuarioDt usuarioDt) throws Exception {
		Long qtdPendenciaAndamento = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT COUNT(*) as QUANTIDADE FROM PROJUDI.PEND pen INNER JOIN PROJUDI.PEND_TIPO pt on ((pt.ID_PEND_TIPO = pen.ID_PEND_TIPO)) ";
	           sql += " JOIN PROJUDI.USU_SERV us1 on((pen.ID_USU_CADASTRADOR = us1.ID_USU_SERV)) ";
			   sql += " LEFT JOIN PROJUDI.SERV sc on((sc.ID_SERV = us1.ID_SERV)) ";
			   sql += " LEFT JOIN PROJUDI.PEND_STATUS ps on((ps.ID_PEND_STATUS = pen.ID_PEND_STATUS)) ";
			   sql +=  " WHERE (DATA_VISTO IS NULL  AND sc.ID_SERV = ? AND PEND_STATUS_CODIGO <> ?  AND DATA_LIMITE IS NULL  AND PEND_TIPO_CODIGO NOT IN (?,?)) ";
			   ps.adicionarLong(usuarioDt.getId_Serventia());
			   ps.adicionarLong(PendenciaStatusDt.ID_ENCAMINHADA);
			   ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
			   ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);

		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				qtdPendenciaAndamento = rs1.getLong("QUANTIDADE");
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return qtdPendenciaAndamento;
	}

	/**
	 * Consulta a quantidade de pendencias expedidas para serventias (on-line) e
	 * que estão aguardando visto
	 * 
	 * @author Leandro Bernardes
	 * @since 26/08/2009
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @return Long
	 * @throws Exception
	 */
	public Long consultarQtdExpedidasAguardandoVisto(UsuarioDt usuarioDt) throws Exception {
		Long qtdQtdExpedidasAguardandoVisto = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

//		String sql = " SELECT COUNT(*) as qtdQtdExpedidasAguardandoVisto FROM PROJUDI.VIEW_PEND_NORMAIS pen WHERE DATA_VISTO IS NULL  AND ID_SERV_CADASTRADOR = " + usuarioDt.getId_Serventia() + " ";
//		sql += " AND pen.PEND_STATUS_CODIGO = " + PendenciaStatusDt.CumprimentoAguardandoVisto;

		String sql = " SELECT COUNT(*) as qtdQtdExpedidasAguardandoVisto FROM PROJUDI.PEND pen INNER JOIN PROJUDI.PEND_TIPO pt on ((pt.ID_PEND_TIPO = pen.ID_PEND_TIPO)) ";
		sql += 	     " join  PROJUDI.USU_SERV us1 on((pen.ID_USU_CADASTRADOR = us1.ID_USU_SERV)) "; 
		sql +=		 "	    LEFT JOIN PROJUDI.SERV sc on((sc.ID_SERV = us1.ID_SERV)) ";
		sql +=		 "	    LEFT JOIN PROJUDI.PEND_STATUS ps on((ps.ID_PEND_STATUS = pen.ID_PEND_STATUS)) ";
		sql +=		 "	 WHERE (DATA_VISTO IS NULL  AND sc.ID_SERV = ? AND ps.PEND_STATUS_CODIGO = ? ) ";
		ps.adicionarLong(usuarioDt.getId_Serventia());
		ps.adicionarLong(PendenciaStatusDt.ID_CUMPRIMENTO_AGUARDANDO_VISTO);
		
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next())
				qtdQtdExpedidasAguardandoVisto = rs1.getLong("qtdQtdExpedidasAguardandoVisto");

			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return qtdQtdExpedidasAguardandoVisto;
	}

	/**
	 * Retorna a estatistica das pendencias para o cargo da serventia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/10/2008 15:19
	 * @param UsusarioDt
	 *            usuarioDt, vo para usuario
	 * @return Map
	 * @throws Exception
	 */
	/*
	public Map consultarTiposServentiaCargo(UsuarioDt usuarioDt) throws Exception {
		String sql = "SELECT u.ID_SERV_CARGO, u.ID_PEND_TIPO, u.PEND_TIPO, COUNT(*) as Quantidade FROM VIEW_PENDAbertasServentiaCargo u " + " WHERE u.ID_SERV_CARGO IN ( " + " SELECT ID_SERV_CARGO FROM SERV_CARGO aux_sg " + " INNER JOIN USU_SERV_GRUPO aux_usg " + " on aux_sg.ID_USU_SERV_GRUPO = aux_usg.ID_USU_SERV_GRUPO " + " INNER JOIN USU_SERV aux_us "
				+ " on aux_usg.ID_USU_SERV = aux_us.ID_USU_SERV " + " WHERE aux_us.ID_USU_SERV = " + usuarioDt.getId_UsuarioServentia() + ") group by u.ID_PEND_TIPO";

		Map est = new HashMap();

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql);

			// Preenche map
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		
		}
		finally{
			 rs1.close();
		}

		return est;
	}*/

	/**
	 * Retorna um lista de tipos de pendencia abertas, reservada, pre-analisadas
	 * por serventia cargo
	 * 
	 * @author Leandro Bernardes
	 * @since 10/08/2009
	 * @param String id_ServentiaCargo
	 * @param boolean ehPendenteAssinatura
	 * @return Map
	 * @throws Exception
	 */
	public Map consultarTiposServentiaCargoJuiz(String id_ServentiaCargo, boolean ehPendenteAssinatura) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT u.ID_SERV_CARGO, u.ID_PEND_TIPO, u.PEND_TIPO, COUNT(*) as QUANTIDADE";
		sql += " FROM VIEW_PEND_ATIVAS_RESP u";		
		sql += " WHERE u.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);

		sql += " AND u.PEND_TIPO_CODIGO NOT IN (";
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.LIBERACAO_ACESSO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		
		// Pendências relacionadas à funcionalidade de Sessão Virtual
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.VERIFICAR_IMPEDIMENTO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_VOTANTES);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.SESSAO_CONHECIMENTO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.RETIRAR_PAUTA);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA_SESSAO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.ADIAR_JULGAMENTO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.RESULTADO_UNANIME);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.RESULTADO_VOTACAO);
		sql += " ?, "; ps.adicionarLong(PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA);
		sql += " ? "; ps.adicionarLong(PendenciaTipoDt.VOTANTE_IMPEDIDO);
		sql += " ) ";
				
				
		if (ehPendenteAssinatura)
			sql += " AND EXISTS (SELECT 1";
		else			
			sql += " AND NOT EXISTS (SELECT 1";
		sql += "                   FROM PROJUDI.PEND_ARQ pa";
		sql += "                  WHERE u.ID_PEND = pa.ID_PEND";
		sql += "                   AND pa.RESPOSTA = ?";
		ps.adicionarLong(1);		
		sql += "                   AND pa.CODIGO_TEMP = ?)";		
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);		
		sql += " group by u.ID_PEND_TIPO, u.ID_SERV_CARGO, u.PEND_TIPO";
		
		Map est = new HashMap();

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			// Preenche map
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return est;
	}

	/**
	 * Retorna a estatistica das pendencias para o usuario responsavel (usuario)
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/10/2008 15:11
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @return Map
	 * @throws Exception
	 */
	/*
	public Map consultarTiposUsuarioResponsavel(UsuarioDt usuarioDt) throws Exception {

		String sql = "SELECT u.ID_USU_RESP, u.ID_PEND_TIPO, u.PEND_TIPO, " + " COUNT(*) as Quantidade FROM VIEW_PENDAbertasUsuarioResponsavel u " + " WHERE u.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia();

		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR u.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " group by u.ID_PEND_TIPO";

		Map est = new HashMap();

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql);

			// Preenche map
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		
		}
		finally{
			 rs1.close();
		}

		return est;
	}*/

	/**
	 * Retorna a estatistica das pendencias para o tipo de serventia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/10/2008 15:09
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @return Map
	 * @throws Exception
	 */
	/*
	public Map consultarTiposServentiaTipodd(UsuarioDt usuarioDt){
		return this.consultarTipos("VIEW_PENDAbertasServentiaTipo", "ID_SERV_TIPO", usuarioDt.getId_ServentiaTipo());
	} */

	/**
	 * Retorna a estatistica das pendencias da serventia organizadas por tipo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/10/2008 15:10
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @return Map
	 * @throws Exception
	 */
	/*
	public Map consultarTiposServentiaddd(UsuarioDt usuarioDt){
		return this.consultarTipos("VIEW_PENDAbertasServentia", "ID_SERV", usuarioDt.getId_Serventia());
	} */

	/**
	 * Consulta a quantidade de pendencias abertas para um determinado tipo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/10/2008 15:03
	 * @param String
	 *            visao, visao a ser acessada para retornar o valor desejado
	 * @param String
	 *            campo, campo para agrupamento da visao
	 * @param valor,
	 *            valor desejado do campo
	 * @return Map
	 * @throws Exception
	 */
	/*private Map consultarTipos(String visao, String campo, String valor){
		String sql = "SELECT u." + campo + ", u.ID_PEND_TIPO, u.PEND_TIPO, " + " COUNT(*) as Quantidade FROM " + visao + " u " + " WHERE u." + campo + " = " + valor + " group by u.ID_PEND_TIPO";

		Map est = new HashMap();

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql);

			// Preenche map
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		
		}
		finally{
			 rs1.close();
		}

		return est;
	}*/

	/**
	 * Consulta a quantidade de pendencias para serventia em andamento
	 * 
	 * @author Leandro Bernardes
	 * @since 03/12/2008 10:34
	 * @param UsuarioDt
	 *            usuarioDt, usuario que deseja a quantidade
	 * @param String
	 *            tipo, tipo da pendencia
	 * @return int
	 * @throws Exception
	 */
	/*public int consultarQuantidadeServentiaAbertasEmAndamento(UsuarioDt usuarioDt, String tipo){
		String sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PEND_ABERTAS_RESP par WHERE " + " ID_PEND_TIPO = " + tipo + " AND (PEND_STATUS_CODIGO = " + PendenciaStatusDt.EmAndamento + " OR PEND_STATUS_CODIGO = " + PendenciaStatusDt.Correcao + " OR PEND_STATUS_CODIGO = " + PendenciaStatusDt.AguardandoCumprimento + " ) " + this.filtroServentia(usuarioDt, "par");

		try{
			ResultSetTJGO rs1 = this.consultar(sql);

			// Preenche map
			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
	}*/
	
	public List consultarPendenciasServentiaAbertasEmAndamento(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	    String sql = " SELECT par.ID_PEND_TIPO, par.PEND_TIPO, COUNT(*) AS QUANTIDADE "
	    			 + "  FROM PROJUDI.VIEW_PEND_ABERTAS_RESP par "
					 + " WHERE (PEND_STATUS_CODIGO IN ( ? , ? , ?) ) " ;
	    
	    ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
	    ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
	    ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
	    
	    //Não deve ser aparcer na tela inicial as pendências do tipo Soliciatação de carga
	    sql += " AND (PEND_TIPO_CODIGO <> ?) ";	ps.adicionarLong(PendenciaTipoDt.SOLICITACAO_CARGA);
	    
	    if (usuarioDt.getGrupoCodigo() != null && usuarioDt.getGrupoCodigo().length()>0
	    		&& Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.DIVISAO_RECURSOS_CONSTITUCIONAIS ){
	    	sql += " AND (PEND_TIPO_CODIGO <> ?) ";					 	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RECURSO_REPETITIVO);
	    }
	    
	    if (usuarioDt != null && (usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL
	    		|| usuarioDt.getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL
	    		|| usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL
	    		|| usuarioDt.getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL)){
	    	sql += " AND (PEND_TIPO_CODIGO NOT IN ( ?,?,?,? )) ";	
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO);  ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO); ps.adicionarLong(PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO);
	    }
	   
	    if (usuarioDt.getGrupoCodigo() != null && usuarioDt.getGrupoCodigo().length() > 0
	    		&& (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU 
	    		|| Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ESTAGIARIO_SEGUNDO_GRAU)){
	    	sql += " AND (PEND_TIPO_CODIGO NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?)) ";
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA);
	    	ps.adicionarLong(PendenciaTipoDt.AVERBACAO_CUSTAS);
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC);
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT);
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC);
	    	ps.adicionarLong(PendenciaTipoDt.SUSPENSAO_PROCESSO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CALCULO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_CAMARA_SAUDE);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_LAUDO_RELATORIO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_CENOPES);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DEVOLUCAO_PRECATORIA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_PAGA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_VENCIDA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_AUDIENCIAS_REALIZADAS_CEJUSC);	
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PARECER);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PETICAO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PROCESSO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PROCESSO_HIBRIDO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CLASSE_PROCESSUAL);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_FATO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_OFICIO_DELEGACIA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RETORNO_AR_CORREIOS);
	    }	    
	    
	    sql += this.filtroServentia(usuarioDt, "par", ps) + " group by  par.ID_PEND_TIPO, par.PEND_TIPO ";
	    
	    ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return pendencias;
	}
	
	public List consultarPendenciasServentiaDistribuidorCamara(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	    String sql = " SELECT par.ID_PEND_TIPO, par.PEND_TIPO, COUNT(*) AS QUANTIDADE "
	    			 + "  FROM PROJUDI.VIEW_PEND_ABERTAS_RESP par "
					 + " WHERE (PEND_STATUS_CODIGO IN ( ? , ? , ?) ) " ;
	    
	    ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
	    ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
	    ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
	    
	    sql += " AND (PEND_TIPO_CODIGO IN( ?,?,?,? )) "; ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO); ps.adicionarLong(PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO);
	   
	    sql += this.filtroServentia(usuarioDt, "par", ps) + " group by  par.ID_PEND_TIPO, par.PEND_TIPO ";
	    
	    ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return pendencias;
	}
	
	public List consultarPendenciasServentiaAbertasPreAnalisada(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	    String sql = " SELECT par.ID_PEND_TIPO, par.PEND_TIPO, COUNT(*) AS QUANTIDADE "
	    			 + "  FROM PROJUDI.VIEW_PEND_ABERTAS_RESP par "
					 + " WHERE (PEND_STATUS_CODIGO IN ( ? ) ) " ;
	    
	    ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);
	    
	    if (usuarioDt.getGrupoCodigo() != null && usuarioDt.getGrupoCodigo().length()>0
	    		&& Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) != GrupoDt.DIVISAO_RECURSOS_CONSTITUCIONAIS ){
	    	sql += " AND (PEND_TIPO_CODIGO <> ?) ";
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RECURSO_REPETITIVO);
	    }
	    
	    if (usuarioDt != null && (usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL 
	    		|| usuarioDt.getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL
	    		|| usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL
	    		|| usuarioDt.getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL)){
	    	sql += " AND (PEND_TIPO_CODIGO NOT IN ( ?,?,?,? )) ";	
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO); ps.adicionarLong(PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO);
	    }
	    
	    if (usuarioDt.getGrupoCodigo() != null && usuarioDt.getGrupoCodigo().length() > 0
	    		&& (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU 
	    		|| Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ESTAGIARIO_SEGUNDO_GRAU)){
	    	sql += " AND (PEND_TIPO_CODIGO NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?)) ";
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA);
	    	ps.adicionarLong(PendenciaTipoDt.AVERBACAO_CUSTAS);
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC);
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT);
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC);
	    	ps.adicionarLong(PendenciaTipoDt.SUSPENSAO_PROCESSO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CALCULO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_CAMARA_SAUDE);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_LAUDO_RELATORIO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_CENOPES);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DEVOLUCAO_PRECATORIA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_PAGA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_VENCIDA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_AUDIENCIAS_REALIZADAS_CEJUSC);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PARECER);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PETICAO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PROCESSO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PROCESSO_HIBRIDO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CLASSE_PROCESSUAL);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_FATO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_OFICIO_DELEGACIA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RETORNO_AR_CORREIOS);
	    }
	   
	    sql += this.filtroServentia(usuarioDt, "par", ps) + " group by  par.ID_PEND_TIPO, par.PEND_TIPO ";
	    
	    ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return pendencias;
	}
	
	
	public List consultarPendenciasServentiaDRCAbertasEmAndamento(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	    String sql = " SELECT par.ID_PEND_TIPO, par.PEND_TIPO, COUNT(*) AS QUANTIDADE "
	    			 + "  FROM PROJUDI.VIEW_PEND_ABERTAS_RESP par "
					 + " WHERE (PEND_STATUS_CODIGO IN ( ? , ? , ?) ) AND (PEND_TIPO_CODIGO = ? or PEND_TIPO_CODIGO = ? )" ;
	    ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
	    ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
	    ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
	    ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RECURSO_REPETITIVO);
	    ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
	    
	    sql += this.filtroServentia(usuarioDt, "par", ps) + " group by  par.ID_PEND_TIPO, par.PEND_TIPO ";
	    
	    ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return pendencias;
	}

	/**
	 * Consulta a quantidade de pendencias para serventia cargo em andamento
	 * 
	 * @author Leandro Bernardes
	 * @since 03/12/2008 10:34
	 * @param UsuarioDt
	 *            usuarioDt, usuario que deseja a quantidade
	 * @param String
	 *            tipo, tipo da pendencia
	 * @return int
	 * @throws Exception
	 */
	/*public int consultarQuantidadeCargoServentiaAbertasEmAndamento(UsuarioDt usuarioDt, String tipo){
		String sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PEND_ABERTAS_RESP par WHERE " 
			+ " ID_PEND_TIPO = " + tipo + " AND (PEND_STATUS_CODIGO = " + PendenciaStatusDt.EmAndamento 
			+ " OR PEND_STATUS_CODIGO = " + PendenciaStatusDt.Correcao + " OR PEND_STATUS_CODIGO = " 
			+ PendenciaStatusDt.AguardandoCumprimento + " ) " + this.filtroCargoServentia(usuarioDt, "par");

		try{
			ResultSetTJGO rs1 = this.consultar(sql);

			// Preenche map
			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
	}*/
	
	public List consultarPendenciasCargoServentiaAbertasEmAndamento(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	    
	    String sql = " SELECT par.ID_PEND_TIPO, par.PEND_TIPO, COUNT(*) AS QUANTIDADE "
	    			 + " FROM PROJUDI.VIEW_PEND_ABERTAS_RESP par "
	    			 + " WHERE ((PEND_STATUS_CODIGO = ?"  
					 +"          OR PEND_STATUS_CODIGO = ?" 
					 + "          OR PEND_STATUS_CODIGO = ?"
					 + "          OR PEND_STATUS_CODIGO = ? )" 
					 +"         AND PEND_TIPO_CODIGO <> ?)";
	    ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
	    ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
	    ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
	    ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);
	    ps.adicionarLong(PendenciaTipoDt.LIBERACAO_ACESSO);
		sql += this.filtroCargoServentia(usuarioDt, "par", ps) + " group by  par.ID_PEND_TIPO, par.PEND_TIPO ";
	    
	    ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return pendencias;
	}
	
	public List consultarPendenciasCargoServentiaAbertasEmAndamentoOficial(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	    
	    String sql = " SELECT par.ID_PEND_TIPO, par.PEND_TIPO, COUNT(*) AS QUANTIDADE "
	    			 + " FROM PROJUDI.VIEW_PEND_ABERTAS_RESP par "
	    			 + " WHERE PEND_STATUS_CODIGO = ?";
	    ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_RETORNO);
	    
		sql += this.filtroCargoServentia(usuarioDt, "par", ps) + " group by  par.ID_PEND_TIPO, par.PEND_TIPO ";
	    
	    ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		} finally {
			if (rs1 != null) rs1.close();
		}
		return pendencias;
	}

	/**
	 * Consulta a quantidade de pendencias para serventia tipo em andamento
	 * 
	 * @author Leandro Bernardes
	 * @since 03/12/2008 10:34
	 * @param UsuarioDt
	 *            usuarioDt, usuario que deseja a quantidade
	 * @param String
	 *            tipo, tipo da pendencia
	 * @return int
	 * @throws Exception
	 */
	public int consultarQuantidadeServentiaTipoAbertasEmAndamento(UsuarioDt usuarioDt, String tipo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PEND_ABERTAS_RESP par WHERE ID_PEND_TIPO = ? AND (PEND_TIPO_CODIGO <> ?  ) AND (PEND_STATUS_CODIGO = ? OR PEND_STATUS_CODIGO = ? ) ";		
		ps.adicionarLong(tipo);	
		ps.adicionarLong(PendenciaTipoDt.INFORMATIVO);
		ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
		ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
		sql += this.filtroServentiaTipo(usuarioDt, "par",ps);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);

			// Preenche map
			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
	}

	/**
	 * Consulta as pendencias de um determinado tipo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 14/10/2008 10:47
	 * @param int
	 *            tipoCodigo, codigo do tipo da pendencia
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param String
	 *            posicao, posicionamento
	 * @return List
	 * @throws Exception
	 */
	public List consultarPorTipo(int tipoCodigo, String dataInicio, String dataFinal, String posicao) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "FROM VIEW_PEND p WHERE p.PEND_TIPO_CODIGO = ?";
		ps.adicionarLong(tipoCodigo);		

		// Se tiver preenchido as duas datas
		if (dataInicio != null && dataFinal != null){
			sql += " AND p.DATA_INICIO between ? AND ?";
			ps.adicionarDateTime(dataInicio);
			ps.adicionarDateTime(dataFinal);
		}

		sql += " ORDER BY DATA_INICIO desc";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE " + sql;

		sql = "SELECT * " + sql;	

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setServentiaUsuarioFinalizador(rs1.getString("SERV_FINALIZADOR"));
				pendenciaDt.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));

				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}

	/**
	 * Retorna todas as pendencias de publicacao publica que contem parte o a
	 * integra do texto informado
	 * 
	 * @author jesus rodrig
	 * @since 25/09/2009
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param String
	 *            texto[], palavras que serão utilizadas para fazer a busca
	 *            textual
	 * @return List
	 * @throws Exception
	 */
	
	public List consultarTextoPublicacaoPublica(String dataInicio, String dataFinal, String id_Serventia, String[] palavras, String posicao) throws Exception{
		List pendencias = new ArrayList();		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String stSqlCorpo ="";
		String stSqlCriterio ="";
		String stSqlCabecalho ="";
		String stSqlCount ="";
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSqlCabecalho = " SELECT P.DATA_INICIO AS DATA_PUBLICACAO,    US.ID_SERV AS ID_SERV, S.SERV AS SERV, a.ID_ARQ AS ID_ARQ, a.NOME_ARQ AS NOME_ARQ, a.USU_ASSINADOR AS USU_ASSINADOR,  AT.ARQ_TIPO AS ARQ_TIPO,  tab.qtd from ";
		
	    stSqlCorpo += " ( select count(1) as qtd, ap.id_arq from arq_palavra ap "; 
		stSqlCorpo += "        inner join palavra p1 on ap.id_palavra_1 = p1.id_palavra ";
		stSqlCorpo += "        inner join palavra p2 on ap.id_palavra_2 = p2.id_palavra ";
		stSqlCorpo += " where ";

		if (palavras.length == 1) {
			stSqlCorpo += " ( p1.PALAVRA = ? OR p2.PALAVRA= ?)";
			ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0]));
			ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0]));
		} else if (palavras.length > 1) {
			// faço o encadeamento das palavras
			stSqlCorpo += "( ( p1.PALAVRA=? AND p2.PALAVRA= ?)";
			ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0]));
			ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[1]));
			for (int i = 1; i < palavras.length - 1; i++) {
				stSqlCorpo += " OR (p1.PALAVRA=? AND p2.PALAVRA= ?) ";
				ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[i]));
				ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[i + 1]));
			}
			stSqlCorpo += " ) ";
		} else {
			throw new MensagemException("A pesquisa textual necessita de pelomenos uma palavra. Faltando o texto.");
		}

		stSqlCorpo += "  group by ap.id_arq ";
		stSqlCorpo += "  order by qtd desc ) tab ";
		stSqlCorpo += "  JOIN PEND_ARQ PA ON pa.id_arq = tab.id_arq ";
		stSqlCorpo += "	join PEND p on p.id_pend = pa.id_pend ";
		stSqlCorpo += "	JOIN USU_SERV US ON P.ID_USU_CADASTRADOR = US.ID_USU_SERV ";
		stSqlCorpo += "	JOIN SERV S ON US.ID_SERV = S.ID_SERV ";
		stSqlCorpo += "  JOIN ARQ a ON tab.ID_ARQ = a.ID_ARQ ";
		stSqlCorpo += "  JOIN ARQ_TIPO AT ON AT.ID_ARQ_TIPO = a.ID_ARQ_TIPO ";
	               
		if (id_Serventia != null && !id_Serventia.equals("")){
			stSqlCriterio += " WHERE p.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);
		}
		
		// Se tiver preenchido as duas datas
		if (dataInicio != null && dataFinal != null){
			if (stSqlCriterio.length()>0)
				stSqlCriterio += " AND p.DATA_PUBLICACAO BETWEEN ? AND ?";
			else
				stSqlCriterio += " WHERE p.DATA_PUBLICACAO BETWEEN ? AND ?";
			ps.adicionarDateTime(dataInicio);
			ps.adicionarDateTime(dataFinal);
		}

		try{
			rs1 = this.consultarPaginacao(stSqlCabecalho + stSqlCorpo + stSqlCriterio, ps,posicao);
			ArquivoPalavraDt dados = null;
			// se encontrou pego o primeiro para começar a contar os encontros
			 while (rs1.next()) {
					
					dados = new ArquivoPalavraDt();

					dados.setId_Arquivo(rs1.getString("ID_ARQ"));
					dados.setNomeArquivo(rs1.getString("NOME_ARQ"));
					dados.setArquivoTipo(rs1.getString("ARQ_TIPO"));
					dados.setUsuarioAssinador(Funcoes.RetirarAssinantes(rs1.getString("USU_ASSINADOR")));
					dados.setServentia(rs1.getString("SERV"));
					dados.setDataInsercao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_PUBLICACAO")));
					pendencias.add(dados);
	
			}
			 stSqlCount = "select count(1) as QUANTIDADE  from ";
			 rs2 = this.consultar(stSqlCount + stSqlCorpo + stSqlCriterio, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			 

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}

	/**
	 * Retorna todas as pendencias de publicacao publica
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 14/10/2008 10:49
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param String
	 *            posicao, posicionamento
	 * @return List
	 * @throws Exception
	 */
	public List consultarPublicacaoPublica(String dataInicio, String dataFinal, String id_Serventia, String posicao) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "FROM VIEW_PEND_FINAL p WHERE p.PEND_TIPO_CODIGO = ?";
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		if (id_Serventia != null && !id_Serventia.equals("")){
			sql += " AND p.ID_SERV_FINALIZADOR = ?";
			ps.adicionarLong(id_Serventia);
		} else {
			throw new Exception ("É necessário informar a serventia.");
		}

		// Se tiver preenchido as duas datas
		if (ValidacaoUtil.isNaoVazio(dataInicio) && ValidacaoUtil.isNaoVazio(dataFinal)){
			sql += " AND p.DATA_INICIO between ? AND ?";
			ps.adicionarDateTime(dataInicio);
			ps.adicionarDateTime(dataFinal);
		} else {
			throw new Exception ("É necessário informar um intervalo de datas (Início e Fim).");
		}

		sql += " ORDER BY DATA_INICIO desc";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE " + sql;

		sql = "SELECT p.ID_PEND, p.PEND, p.ID_PEND_TIPO, p.PEND_TIPO, p.ID_MOVI, p.MOVI, p.ID_PROC, p.PROC_NUMERO"
				+ ", p.DIGITO_VERIFICADOR, p.ID_AREA, p.PROC_NUMERO_COMPLETO, p.ID_PROC_PRIOR, p.PROC_PRIOR, p.PEND_PRIORIDADE_CODIGO, p.PROC_PRIOR_CODIGO"
				+ ", p.ID_PROC_PARTE, p.NOME_PARTE, p.ID_PEND_STATUS, p.PEND_STATUS, p.ID_CLASSIFICADOR, p.CLASSIFICADOR"
				+ ", p.PRIORI, p.ID_USU_CADASTRADOR, p.USU_CADASTRADOR, p.ID_USU_FINALIZADOR, p.USU_FINALIZADOR"
				+ ", p.ID_SERV_FINALIZADOR, p.SERV_FINALIZADOR, p.ID_SERV_CADASTRADOR, p.SERV_CADASTRADOR, p.DATA_INICIO"
				+ ", p.DATA_FIM, p.DATA_LIMITE, p.DATA_DIST, p.PRAZO, p.DATA_TEMP, p.ID_PEND_PAI, p.DATA_VISTO, p.CODIGO_TEMP, p.PEND_TIPO_CODIGO"
				+ ", p.PEND_STATUS_CODIGO, p.NOME_USU_CADASTRADOR, p.NOME_USU_FINALIZADOR " + sql; 

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setServentiaUsuarioFinalizador(rs1.getString("SERV_FINALIZADOR"));
				pendenciaDt.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));

				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			while (rs2.next())
				pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}

	/**
	 * Filtro generico para pendencias
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 14/01/2009 11:38
	 * @param alias
	 *            alias da tabela
	 * @param add
	 *            adicionar o 'and' no sql, significa que ja foi especificado
	 *            alguma condicao no where
	 * @param pendenciaTipoDt
	 *            tipo da pendencia
	 * @param pendenciaStatusDt
	 *            status da pendencia
	 * @param filtroTipo
	 *            tipo de filtro: 0 - somente processo, 1 - normais e 2 - todas
	 * @param numero_processo
	 *            numero do processo
	 * @param dataInicialInicio
	 *            data inicial do inicio
	 * @param dataFinalInicio
	 *            data final do inicio
	 * @return sql de condicao
	 * @throws Exception 
	 */
	private String filtroGenerico(String alias, boolean add, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, Integer filtroTipo, Integer filtroCivelCriminal, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicialFim, String dataFinalFim, PreparedStatementTJGO ps) throws Exception{
		String sqlSel = "";
		
		if (numero_processo != null && !numero_processo.equals("")) {
			sqlSel += add ? "and" : "";

			//numero_processo = String.valueOf(Funcoes.StringToLong(Funcoes.desformataNumeroProcesso(numero_processo)));
			String[] numeroDigito = numero_processo.replaceAll("-", ".").replaceAll(",", ".").split("\\.");

			sqlSel += " " + alias + ".PROC_NUMERO = ? ";
			ps.adicionarLong(numeroDigito[0]);
			
			if (numeroDigito.length > 1){
				sqlSel += " AND  " + alias + ".DIGITO_VERIFICADOR = ? ";
				ps.adicionarLong(numeroDigito[1]);
			}
			
			add = true;
		}

		if (pendenciaTipoDt != null) {
			if (pendenciaTipoDt.getId() != null && !pendenciaTipoDt.getId().equals("") && (pendenciaTipoDt.getPendenciaTipoCodigo() == null || pendenciaTipoDt.getPendenciaTipoCodigo().equals(""))) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_PEND_TIPO = ? ";
				ps.adicionarLong(pendenciaTipoDt.getId());

				add = true;
			} else {
				if (pendenciaTipoDt.getPendenciaTipoCodigo() != null && !pendenciaTipoDt.getPendenciaTipoCodigo().equals("")) {
					sqlSel += add ? "and" : "";

					sqlSel += " " + alias + ".PEND_TIPO_CODIGO = ? ";
					ps.adicionarLong(pendenciaTipoDt.getPendenciaTipoCodigo());

					add = true;
				}
			}
		}

		if (pendenciaStatusDt != null && pendenciaStatusDt.getId() != null && !pendenciaStatusDt.getId().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".ID_PEND_STATUS = ? ";
			ps.adicionarLong(pendenciaStatusDt.getId());

			add = true;
		}

		// Adiciona filtro por genero da pendencia
		if (filtroTipo != null) {
			if (filtroTipo == 1) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_PROC is not null ";

				add = true;
			} else if (filtroTipo == 2) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_PROC IS NULL ";

				add = true;
			}
		}
		
		// Adiciona filtro por área da pendencia (civel e ou criminal)
		if (filtroCivelCriminal != null) {
			if (filtroCivelCriminal == 1) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_AREA = 1 ";

				add = true;
			} else if (filtroCivelCriminal == 2) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_AREA = 2";

				add = true;
			}
		}

		// Filtro de inicio
		if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_INICIO >= ? ";
			ps.adicionarDateTime(dataInicialInicio);

			add = true;
		}

		if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_INICIO <= ? ";
			ps.adicionarDateTime(dataFinalInicio);

			add = true;
		}

		// Filtro de fim
		if (dataInicialFim != null && !dataInicialFim.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_FIM >= ? ";
			ps.adicionarDateTime(dataInicialFim);

			add = true;
		}

		if (dataFinalFim != null && !dataFinalFim.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_FIM <= ? ";
			ps.adicionarDateTime(dataFinalFim);

			add = true;
		}

		return sqlSel;
	}
	
	private String filtroIntimacaoCitacaoLidas(String alias, boolean add, PendenciaTipoDt pendenciaTipoDt, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicialFim, String dataFinalFim, PreparedStatementTJGO ps) throws Exception{
		String sqlSel = "";

		if (numero_processo != null && !numero_processo.equals("")) {
			sqlSel += add ? "and" : "";

			//numero_processo = String.valueOf(Funcoes.StringToLong(Funcoes.desformataNumeroProcesso(numero_processo)));
			String[] numeroDigito = numero_processo.replaceAll("-", ".").replaceAll(",", ".").split("\\.");

			sqlSel += " " + alias + ".PROC_NUMERO = ? ";
			ps.adicionarLong(numeroDigito[0]);
			
			if (numeroDigito.length > 1){
				sqlSel += " AND  " + alias + ".DIGITO_VERIFICADOR = ? ";
				ps.adicionarLong(numeroDigito[1]);
			}
			
			add = true;
		}

		if (pendenciaTipoDt != null) {
			if (pendenciaTipoDt.getId() != null && !pendenciaTipoDt.getId().equals("") && (pendenciaTipoDt.getPendenciaTipoCodigo() == null || pendenciaTipoDt.getPendenciaTipoCodigo().equals(""))) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_PEND_TIPO = ? ";
				ps.adicionarLong(pendenciaTipoDt.getId());

				add = true;
			} else {
				if (pendenciaTipoDt.getPendenciaTipoCodigo() != null && !pendenciaTipoDt.getPendenciaTipoCodigo().equals("")) {
					sqlSel += add ? "and" : "";

					sqlSel += " " + alias + ".PEND_TIPO_CODIGO = ? ";
					ps.adicionarLong(pendenciaTipoDt.getPendenciaTipoCodigo());

					add = true;
				}
			}
		}

		// Adiciona filtro por genero da pendencia
		if (filtroTipo != null) {
			if (filtroTipo == 1) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_PROC is not null ";

				add = true;
			} else if (filtroTipo == 2) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_PROC IS NULL ";

				add = true;
			}
		}

		// Filtro de inicio
		if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_INICIO >= ? ";
			ps.adicionarDateTime(dataInicialInicio);

			add = true;
		}

		if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_INICIO <= ? ";
			ps.adicionarDateTime(dataFinalInicio);

			add = true;
		}

		// Filtro de fim
		if (dataInicialFim != null && !dataInicialFim.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_FIM >= ? ";
			ps.adicionarDateTime(dataInicialFim);

			add = true;
		}

		if (dataFinalFim != null && !dataFinalFim.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_FIM <= ? ";
			ps.adicionarDateTime(dataFinalFim);

			add = true;
		}

		return sqlSel;
	}
	
	private String filtroIntimacaoDistribuidas(String alias, boolean add, PendenciaTipoDt pendenciaTipoDt, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, PreparedStatementTJGO ps) throws Exception{
		String sqlSel = "";

		if (numero_processo != null && !numero_processo.equals("")) {
			sqlSel += add ? "and" : "";

			String[] numeroDigito = numero_processo.replaceAll("-", ".").replaceAll(",", ".").split("\\.");

			sqlSel += " " + alias + ".PROC_NUMERO = ? ";
			ps.adicionarLong(numeroDigito[0]);
			
			if (numeroDigito.length > 1) {
				sqlSel += " AND  " + alias + ".DIGITO_VERIFICADOR = ? ";
				ps.adicionarLong(numeroDigito[1]);
			}
			
			add = true;
		}

		if (pendenciaTipoDt != null) {
			if (pendenciaTipoDt.getId() != null && !pendenciaTipoDt.getId().equals("") && (pendenciaTipoDt.getPendenciaTipoCodigo() == null || pendenciaTipoDt.getPendenciaTipoCodigo().equals(""))) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_PEND_TIPO = ? ";
				ps.adicionarLong(pendenciaTipoDt.getId());

				add = true;
			} else {
				if (pendenciaTipoDt.getPendenciaTipoCodigo() != null && !pendenciaTipoDt.getPendenciaTipoCodigo().equals("")) {
					sqlSel += add ? "and" : "";

					sqlSel += " " + alias + ".PEND_TIPO_CODIGO = ? ";
					ps.adicionarLong(pendenciaTipoDt.getPendenciaTipoCodigo());

					add = true;
				}
			}
		}

		// Adiciona filtro por genero da pendencia
		if (filtroTipo != null) {
			if (filtroTipo == 1) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_PROC is not null ";

				add = true;
			} else if (filtroTipo == 2) {
				sqlSel += add ? "and" : "";

				sqlSel += " " + alias + ".ID_PROC IS NULL ";

				add = true;
			}
		}

		// Filtro de inicio
		if (dataInicialInicio != null && !dataInicialInicio.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_INICIO >= ? ";
			ps.adicionarDateTime(dataInicialInicio);

			add = true;
		}

		if (dataFinalInicio != null && !dataFinalInicio.trim().equals("")) {
			sqlSel += add ? "and" : "";

			sqlSel += " " + alias + ".DATA_INICIO <= ?";
			ps.adicionarDateTime(dataFinalInicio);

			add = true;
		}

		return sqlSel;
	}

	public List consultarAbertasServentiaCargo(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlSel = prioridade == true ? "VIEW_PEND_ABERTAS_PRIOR_RESP" : "VIEW_PEND_ABERTAS_RESP";

		sqlSel += " pen WHERE ";

		boolean add = false;
		String filtroGenerico = this.filtroGenerico("pen", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, null, null, ps);

		sqlSel += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		sqlSel += add ? "and" : "";

		sqlSel += " pen.ID_SERV_CARGO = ? AND pen.ID_SERV IS NULL AND  pen.ID_SERV_TIPO IS NULL AND pen.ID_USU_RESP IS NULL AND PEND_STATUS_CODIGO <> ? ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargo());
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_VISTO);

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM PROJUDI." + sqlSel;		

		try{
			sqlSel = "SELECT * FROM PROJUDI." + sqlSel;

			rs1 = this.consultarPaginacao(sqlSel, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				pendenciaDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				pendenciaDt.setProcessoNumeroCompleto(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_UsuarioFinalizador(rs1.getString("ID_USU_FINALIZADOR"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataFim( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setDataLimite(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setDataVisto(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_VISTO")));
				pendenciaDt.setId_ProcessoPrioridade(rs1.getString("ID_PROC_PRIOR"));
				pendenciaDt.setProcessoPrioridade(rs1.getString("PROC_PRIOR"));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				pendenciaDt.setId_PendenciaStatus(rs1.getString("ID_PEND_STATUS"));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setPendenciaStatusCodigo(rs1.getString("PEND_STATUS_CODIGO"));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta pendencias abertas na serventia Pendencia aberta e: Pendencia
	 * quen nao tem usuario realizador E que nao foi reservada por um usuario
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 07/05/2008 - 16:36 Ateracoes Mudanca na relacao de
	 *        responsabilidade da pendencia e no que o usuario podera visualizar
	 * @author Ronneesley Moura Teles
	 * @since 17/06/2008 - 10:20
	 * 
	 * Retirada do generics, devido a logica atual de programacao do projeto
	 * @author Ronneesley Moura Teles
	 * @since 28/08/2008 17:45
	 * @param usuarioDt
	 *            usuario da serventia
	 * @param pendenciaTipoDt
	 *            vo de tipo de pendencia
	 * @param pendenciaStatusDt
	 *            vo de status de pendencia
	 * @param prioridade
	 *            ordenar por prioridade na busca das pendencias
	 * @param filtroTipo
	 *            filtro por tipo de pendencia, 1 somente processo, 2 somente
	 *            normais os demais todas
	 * @param numero_processo
	 *            filtro para pesquisa
	 * @param dataInicialInicio
	 *            data inicial para o inicio
	 * @param dataFinalInicio
	 *            data final para o inicio
	 * @param posicao
	 *            numero para paginacao
	 * @return lista de pendencias abertas
	 * @throws Exception
	 */
	public List consultarAbertas(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, Integer filtroCivelCriminal, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception{
		return consultarAbertas(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, filtroCivelCriminal, numero_processo, dataInicialInicio, dataFinalInicio, posicao, usuarioNe.getUsuarioDt().getId_Serventia());
	}
	
	/**
	 * Consulta pendencias abertas na serventia Pendencia aberta e: Pendencia
	 * quen nao tem usuario realizador E que nao foi reservada por um usuario
	 * em uma determinada serventia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 07/05/2008 - 16:36 Ateracoes Mudanca na relacao de
	 *        responsabilidade da pendencia e no que o usuario podera visualizar
	 * @author Ronneesley Moura Teles
	 * @since 17/06/2008 - 10:20
	 * 
	 * Retirada do generics, devido a logica atual de programacao do projeto
	 * @author Ronneesley Moura Teles
	 * @since 28/08/2008 17:45
	 * 
	 * Inclusão do parâmetro Id_Serventia (Refatoração)
	 * @author Márcio Gomes
	 * @since 30/09/2010 07:30
	 * 
	 * @param usuarioDt
	 *            usuario da serventia
	 * @param pendenciaTipoDt
	 *            vo de tipo de pendencia
	 * @param pendenciaStatusDt
	 *            vo de status de pendencia
	 * @param prioridade
	 *            ordenar por prioridade na busca das pendencias
	 * @param filtroTipo
	 *            filtro por tipo de pendencia, 1 somente processo, 2 somente
	 *            normais os demais todas
	 * @param numero_processo
	 *            filtro para pesquisa
	 * @param dataInicialInicio
	 *            data inicial para o inicio
	 * @param dataFinalInicio
	 *            data final para o inicio
	 * @param posicao
	 *            numero para paginacao
	 * @return lista de pendencias abertas
	 * @throws Exception
	 */
	public List consultarAbertas(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, Integer filtroCivelCriminal, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao, String Id_Serventia) throws Exception {
	List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlSel = prioridade == true ? "VIEW_PEND_ABERTAS_PRIOR_RESP" : "VIEW_PEND_ABERTAS_SERV_RESP";

		sqlSel += " pen WHERE ";
		
		boolean add = false;
		if (usuarioNe.getUsuarioDt() != null && (usuarioNe.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL 
				|| usuarioNe.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL
				|| usuarioNe.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL
	    		|| usuarioNe.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL) ){
			 	sqlSel += " (PEND_TIPO_CODIGO NOT IN ( ?,?,?,? )) ";	
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO);  ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO); ps.adicionarLong(PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO);
		    	add = true;		 
		}
		
		String filtroGenerico = this.filtroGenerico("pen", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, filtroCivelCriminal, numero_processo, dataInicialInicio, dataFinalInicio, null, null, ps);

		sqlSel += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		sqlSel += add ? "AND" : "";

		sqlSel += " (pen.ID_SERV = ? AND pen.ID_SERV_TIPO IS NULL AND " + "pen.ID_USU_RESP IS NULL AND PEND_STATUS_CODIGO <> ?) ";
		ps.adicionarLong(Id_Serventia);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_VISTO);
		
		//retirando status e-carta --------------------------------------------------------------------------------------------------------
		sqlSel += " AND (PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_PAGAMENTO_POSTAGEM);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS);
		ps.adicionarLong(PendenciaStatusDt.ID_RECEBIDO_CORREIOS);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS);
		sqlSel += " AND (PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ? AND DATA_FIM IS NULL) ";
		ps.adicionarLong(PendenciaStatusDt.ID_CUMPRIDA);
		ps.adicionarLong(PendenciaStatusDt.ID_NAO_CUMPRIDA);
		//retirando status e-carta --------------------------------------------------------------------------------------------------------
		
		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM PROJUDI." + sqlSel;

		try{
			sqlSel = "SELECT * FROM PROJUDI." + sqlSel;

			rs1 = this.consultarPaginacao(sqlSel, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}

	/**
	 * Cria um filtro de condicoes para as pendencias do usuario
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 17/06/2008 14:29
	 * @param UsuarioDt
	 *            usuarioDt, POJO do usuario
	 * @param String
	 *            apelido, Apelido para tabela de responsavel
	 * @return String
	 */
	/*
	private String filtroUsuario(UsuarioDt usuarioDt, String apelido) {
		String filtro = "(";

		// Monta as condicoes para que o usuario visualize uma pendencia
		if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) {
			// Ou nao e para um usuario, nem para um tipo de serventia e sim
			// para uma serventia
			filtro += " ( " + apelido + ".ID_SERV = " + usuarioDt.getId_Serventia() + " AND " + apelido + ".ID_SERV_TIPO IS NULL AND " + apelido + ".ID_USU_RESP IS NULL AND PEND_STATUS_CODIGO <> " + PendenciaStatusDt.AguardandoVisto + " ) OR ";

			// Ou nao e para um usuario mas e para um tipo de serventia
			filtro += " (  " + apelido + ".ID_SERV IS NULL AND " + apelido + ".ID_SERV_TIPO = " + usuarioDt.getId_ServentiaTipo() + " AND " + apelido + ".ID_USU_RESP IS NULL ) OR ";

			// Ou nao e para um usuario, nem para um tipo de serventia e sim
			// para uma serventia cargo
			filtro += " ( " + apelido + ".ID_SERV IS NULL AND " + apelido + ".ID_SERV_TIPO IS NULL AND " + apelido + ".ID_USU_RESP IS NULL AND " + " " + apelido + ".ID_SERV_CARGO IN ( " + " SELECT ID_SERV_CARGO FROM SERV_CARGO aux_sg " + " INNER JOIN USU_SERV_GRUPO aux_usg " + " on aux_sg.ID_USU_SERV_GRUPO = aux_usg.ID_USU_SERV_GRUPO " + " INNER JOIN USU_SERV aux_us "
					+ " on aux_usg.ID_USU_SERV = aux_us.ID_USU_SERV " + " WHERE aux_us.ID_USU_SERV = " + usuarioDt.getId_UsuarioServentia() + ") ) OR ";
		}

		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			// Onde o usuario destinado e o usuario
			// filtro += " ( " + apelido + ".ID_USU_RESP = " +
			// usuarioDt.getId_UsuarioServentia()+" OR "+ apelido
			// +".ID_USU_RESP =
			// "+usuarioDt.getId_UsuarioServentiaChefe()+ ") ) ) ) ";
			filtro += " ( " + apelido + ".ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia() + " OR " + apelido + ".ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe() + " OR " + apelido + ".ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia() + ") ) ";
		} else {
			filtro += " ( ( " + apelido + ".ID_SERV IS NULL AND " + apelido + ".ID_SERV_TIPO IS NULL AND " + apelido + ".ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia() + ") ) ) ";
		}

		return filtro;
	}
	*/

	/**
	 * Cria um filtro de condicoes para as pendencias da serventia
	 * 
	 * @author Leandro Bernardes
	 * @since 01/06/2008
	 * @param UsuarioDt
	 *            usuarioDt, POJO do usuario
	 * @param String
	 *            apelido, Apelido para tabela de responsavel
	 * @return String
	 * @throws Exception 
	 */
	private String filtroServentia(UsuarioDt usuarioDt, String apelido, PreparedStatementTJGO ps) throws Exception{
		String filtro = "";

		// Monta as condicoes para que o usuario visualize uma pendencia
		if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) {
			// Ou nao e para um usuario, nem para um tipo de serventia e sim
			// para uma serventia
			filtro += "  AND  ( ( " + apelido + ".ID_SERV = ? AND " + apelido + ".ID_SERV_TIPO IS NULL AND " + apelido + ".ID_USU_RESP IS NULL AND PEND_STATUS_CODIGO <> ? ) ) ";
			ps.adicionarLong(usuarioDt.getId_Serventia());
			ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_VISTO);
		}

		return filtro;
	}

	/**
	 * Cria um filtro de condicoes para as pendencias da serventia cargo
	 * 
	 * @author Leandro Bernardes
	 * @since 10/08/2008
	 * @param UsuarioDt
	 *            usuarioDt, POJO do usuario
	 * @param String
	 *            apelido, Apelido para tabela de responsavel
	 * @return String
	 * @throws Exception 
	 */
	private String filtroCargoServentia(UsuarioDt usuarioDt, String apelido, PreparedStatementTJGO ps) throws Exception{
		String filtro = "";

		// Monta as condicoes para que o usuario visualize uma pendencia
//		if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) {
//
//			// Ou nao e para um usuario, nem para um tipo de serventia e sim
//			// para uma serventia cargo
//			filtro += "   AND ( ( " + apelido + ".ID_SERV IS NULL AND " + apelido + ".ID_SERV_TIPO IS NULL AND " + apelido + ".ID_USU_RESP IS NULL AND " + " " + apelido + ".ID_SERV_CARGO IN ( " + " SELECT ID_SERV_CARGO FROM SERV_CARGO aux_sg " + " INNER JOIN USU_SERV_GRUPO aux_usg " + " on aux_sg.ID_USU_SERV_GRUPO = aux_usg.ID_USU_SERV_GRUPO " + " INNER JOIN USU_SERV aux_us "
//					+ " on aux_usg.ID_USU_SERV = aux_us.ID_USU_SERV " + " WHERE aux_us.ID_USU_SERV = " + usuarioDt.getId_UsuarioServentia() + ") ) AND PEND_STATUS_CODIGO <> " + PendenciaStatusDt.AguardandoVisto + " ) ";
//		
//		} else if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().trim().equals("")){
//			filtro += "   AND ( ( " + apelido + ".ID_SERV IS NULL AND " + apelido + ".ID_SERV_TIPO IS NULL AND " + apelido + ".ID_USU_RESP IS NULL AND " + " " + apelido + ".ID_SERV_CARGO IN ( " + " SELECT ID_SERV_CARGO FROM SERV_CARGO aux_sg " + " INNER JOIN USU_SERV_GRUPO aux_usg " + " on aux_sg.ID_USU_SERV_GRUPO = aux_usg.ID_USU_SERV_GRUPO " + " INNER JOIN USU_SERV aux_us "
//			+ " on aux_usg.ID_USU_SERV = aux_us.ID_USU_SERV " + " WHERE aux_us.ID_USU_SERV = " + usuarioDt.getId_UsuarioServentiaChefe() + ") ) AND PEND_STATUS_CODIGO <> " + PendenciaStatusDt.AguardandoVisto + " ) ";
//		}
		
		// Monta as condicoes para que o usuario visualize uma pendencia
		if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) {

			// Ou nao e para um usuario, nem para um tipo de serventia e sim
			// para uma serventia cargo
			filtro += "   AND ( ( " + apelido + ".ID_SERV IS NULL AND " + apelido + ".ID_SERV_TIPO IS NULL AND " + apelido + ".ID_USU_RESP IS NULL AND " 
			           + " " + apelido + ".ID_SERV_CARGO = ? ) AND PEND_STATUS_CODIGO <> ? ) ";
			ps.adicionarLong(usuarioDt.getId_ServentiaCargo());
			ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_VISTO);
		
		} else if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().trim().equals("")){
			filtro += "   AND ( ( " + apelido + ".ID_SERV IS NULL AND " + apelido + ".ID_SERV_TIPO IS NULL AND " + apelido + ".ID_USU_RESP IS NULL AND " 
	           + " " + apelido + ".ID_SERV_CARGO = ? ) AND PEND_STATUS_CODIGO <> ? ) ";
			ps.adicionarLong(usuarioDt.getId_ServentiaCargoUsuarioChefe());
			ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_VISTO);
		}


		return filtro;
	}

	/**
	 * Cria um filtro de condicoes para as pendencias da serventia tipo
	 * 
	 * @author Leandro Bernardes
	 * @since 11/08/2009
	 * @param UsuarioDt
	 *            usuarioDt, POJO do usuario
	 * @param String
	 *            apelido, Apelido para tabela de responsavel
	 * @return String
	 * @throws Exception 
	 */
	private String filtroServentiaTipo(UsuarioDt usuarioDt, String apelido, PreparedStatementTJGO ps) throws Exception{
		String filtro = "";

		// Monta as condicoes para que o usuario visualize uma pendencia
		if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) {

			// Ou nao e para um usuario, nem para um tipo de serventia e sim
			// para uma serventia cargo
			// Ou nao e para um usuario mas e para um tipo de serventia
			filtro += "  AND (  " + apelido + ".ID_SERV IS NULL AND " + apelido + ".ID_SERV_TIPO = ? AND " + apelido + ".ID_USU_RESP IS NULL AND PEND_STATUS_CODIGO <> ? ) ";
			ps.adicionarLong(usuarioDt.getId_ServentiaTipo());
			ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_VISTO);
		}

		return filtro;
	}
	
	public List consultarReservadasPreAnalisadasComHash(UsuarioNe usuarioNe) throws Exception {
		return consultarReservadasPreAnalisadasComHash(usuarioNe, "	pa.PEND_TIPO ");
	}

	public List consultarReservadasPreAnalisadasComHashPJD(UsuarioNe usuarioNe) throws Exception {
		return consultarReservadasPreAnalisadasComHash(usuarioNe, "	PODE_LIBERAR, pa.PEND_TIPO "); // VERSAO NOVO LAYOUT KARLA, ORDENACAO NECESSARIA NA LISTAGEM
	}
	
	/**
	 * Consulta pendencias reservadas informando se pode liberar contendo o hash
	 * para verificacao
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 20/01/2009 15:58
	 * @param usuarioNe
	 *            usuario da sessao
	 * @return lista de pendencias reservadas com o hash
	 * @throws Exception
	 */
	private List consultarReservadasPreAnalisadasComHash(UsuarioNe usuarioNe, String ordenacao) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT  ";
		sql += "		 (SELECT ";
		sql += "         	1 as PODE_LIBERAR ";
		sql += "         FROM ";
		sql += "         	PROJUDI.PEND_RESP pr ";
		sql += "         WHERE ";
		sql += "         	pr.ID_PEND = pa.ID_PEND AND ";
		sql += "             pr.ID_USU_RESP = ? ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());

		if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equals("")){
			sql += " OR pr.ID_USU_RESP = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
		}

		sql += "      ) PODE_LIBERAR, ";
		sql += "        pa.ID_PROC, pa.PROC_NUMERO_COMPLETO , pa.ID_MOVI, pa.MOVI, " +
					"   pa.ID_PEND, pa.PEND_TIPO, pa.PEND_STATUS, pa.DATA_INICIO, pa.DATA_TEMP ";
		sql += " FROM ";
		sql += "	VIEW_PEND_RESERV pa ";
		sql += " WHERE ";
		sql += "	(pa.ID_USU_FINALIZADOR = ?";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());

		if (Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo()) == GrupoTipoDt.ADVOGADO){
			sql += " AND pa.PEND_TIPO_CODIGO <> ?  AND pa.PEND_TIPO_CODIGO <> ? ";
			ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
			ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		}

		if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equals("")){
			sql += " OR pa.ID_USU_FINALIZADOR = ?";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
		}
		
		if (usuarioNe.isAssessorAdvogado() || usuarioNe.isAssessorAdvogado()){
			sql += " AND  pa.ID_PEND_TIPO <> ? AND pa.ID_PEND_TIPO <> ?  ";
			ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
			ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		}
		
		sql += " ) ";

		sql += " ORDER BY " + ordenacao;

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			// Preenche map
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();

				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendenciaTipo( rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setPendenciaStatus( rs1.getString("PEND_STATUS"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataTemp( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_TEMP")));
				if(rs1.getString("PODE_LIBERAR") == null || rs1.getString("PODE_LIBERAR").toString().equalsIgnoreCase("null"))
					pendenciaDt.setPodeLiberar(true);
				else
					pendenciaDt.setPodeLiberar(false);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));

				pendencias.add(pendenciaDt);

			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}

	/**
	 * Consulta pendencias reservadas informando se pode liberar contendo o hash
	 * para verificacao
	 * 
	 * @author lsbernardes
	 * @since 24/03/2009
	 * @param usuarioNe
	 *            usuario da sessao
	 * @param tipo
	 *            tipo pendencia
	 * @return lista de pendencias reservadas com o hash
	 * @throws Exception
	 */
	public List consultarReservadasComHash(UsuarioNe usuarioNe, String tipo, String filtro) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

//		String sql = "SELECT * FROM ( SELECT (	SELECT 1 as PODE_LIBERAR FROM PROJUDI.PEND_RESP pr ";
//		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = ? ";
//		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
//
//		if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equals("")){
//			sql += " OR pr.ID_USU_RESP = ? ";
//			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
//		}
//		
//		sql += " ) ) PODE_LIBERAR, ";
//		sql += "        pa.ID_PEND, pa.ID_PROC, pa.PROC_NUMERO_COMPLETO , pa.ID_MOVI, pa.MOVI, " +
//					"   pa.PEND_TIPO, pa.PEND_STATUS, pa.DATA_INICIO, pa.DATA_TEMP ";
//		sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa WHERE ( pa.ID_USU_FINALIZADOR = ?";
//		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
//
//		if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equals("")){
//			sql += " OR pa.ID_USU_FINALIZADOR = ? ";
//			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
//		}
		

		String sql = " SELECT  pa.ID_PEND, pa.ID_PROC, ";
		sql += " (  ";
		sql += "   CASE NVL(PR.PROC_NUMERO,0) ";
		sql += "   WHEN 0 ";
		sql += "    THEN NULL ";
		sql += "   ELSE (PR.PROC_NUMERO ";
		sql += "    || '.' ";
		sql += "    || PR.DIGITO_VERIFICADOR) ";
		sql += "  END) as PROC_NUMERO_COMPLETO, ";
		sql += " pa.ID_MOVI, ";
		sql += " (  ";
		sql += "  CASE NVL(pa.ID_MOVI,0) ";
		sql += "  WHEN 0 ";
		sql += "   THEN NULL ";
		sql += "  ELSE (MT.MOVI_TIPO ";
		sql += "    || ' (' ";
		sql += "    || TO_CHAR(M.DATA_REALIZACAO,'DD/MM/YYYY HH24:MI:SS') ";
		sql += "    ||   ')') ";
		sql += "  END) as MOVI, ";
		sql += " pa.ID_PEND_TIPO,pa.PEND_TIPO, pa.ID_PEND_STATUS, PS.PEND_STATUS, pa.DATA_INICIO,pa.DATA_TEMP ";
		
		sql += " FROM  PROJUDI.VIEW_PEND_RESERV_RESP_NOVA pa ";
		sql += " LEFT JOIN MOVI M   ON  pa.ID_MOVI = M.ID_MOVI ";
		sql += " LEFT JOIN MOVI_TIPO MT ON M.ID_MOVI_TIPO = MT.ID_MOVI_TIPO ";
		sql += " LEFT JOIN PROC PR  ON pa.ID_PROC = PR.ID_PROC ";
		sql += " LEFT JOIN PEND_STATUS PS ON PS.ID_PEND_STATUS = pa.ID_PEND_STATUS ";
     	
     	sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";     		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
   		if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
   			sql += " OR pa.ID_USU_FINALIZADOR = ?";   			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
   		}
   		
   		sql += " ) ";
		
		if (usuarioNe.isAssessorAdvogado() || usuarioNe.isAssessorMP()){
			sql += " AND ( pa.ID_PEND_TIPO <> ? AND pa.ID_PEND_TIPO <> ? ) ";
			ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
			ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		}
		
		if (filtro != null && filtro.equals("1")) sql += " AND  pa.ID_SERV is not null ";
		else if (filtro != null && filtro.equals("2")) sql += " AND  pa.ID_SERV_TIPO is not null ";
		else if (filtro != null && filtro.equals("3")) sql += " AND  pa.ID_SERV_CARGO is not null ";

		if (tipo != null && !tipo.equals("")){
			sql += " AND  pa.ID_PEND_TIPO = ? ";
			ps.adicionarLong(tipo);
		}

		sql += " ORDER BY ";
		sql += "	 pa.PEND_TIPO ";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			
			// retirar botao liberar lista pendencias
			boolean oficial = false;
			if(usuarioNe.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.OFICIAL_JUSTICA){
				oficial = true;
			}
			// Preenche map
			while (rs1.next()) {
				if (pendencias == null) {
					pendencias = new ArrayList();
				}
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendenciaTipo( rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setPendenciaStatus( rs1.getString("PEND_STATUS"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataTemp( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_TEMP")));
				if(!oficial){
					pendenciaDt.setPodeLiberar(true);
				} else {
					pendenciaDt.setPodeLiberar(false);
				}
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return pendencias;
	}
	
	/**
	 * Consulta de pendências de mandados reservadas para o oficial. Utiliza a tabela mand_jud como referência. Consulta elaborada
	 * especificamente para o oficial de justiça da Central de Mandados do Projudi.
	 * 
	 * @param usuarioNe
	 * @param tipo
	 * @param filtro
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public List consultarMandadosReservadosComHash(UsuarioNe usuarioNe, String tipo, String filtro, String ordenacao) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT "; 
		sql += " MJ.ID_MAND_JUD, ";
		sql += " PE.ID_PEND, ";
		sql += " PE.ID_PROC, ";
		sql += " ( ";
		sql += "   CASE NVL(PR.PROC_NUMERO,0) ";
		sql += "     WHEN 0 ";
		sql += "     THEN NULL ";
		sql += "     ELSE (PR.PROC_NUMERO ";
		sql += "       || '.' ";
		sql += "       || PR.DIGITO_VERIFICADOR) ";
		sql += "   END ";
		sql += " ) AS PROC_NUMERO_COMPLETO, ";
		sql += " PE.ID_MOVI, ";
		sql += " ( ";
		sql += "   CASE NVL(MJ.ASSISTENCIA,0) ";
		sql += "     WHEN 1 ";
		sql += "     THEN 'SIM' ";
		sql += "     ELSE 'NÃO' ";
		sql += "   END ";
		sql += " ) AS ASSISTENCIA, ";
		sql += " PT.ID_PEND_TIPO, ";
		sql += " PT.PEND_TIPO, ";
		sql += " PE.ID_PEND_STATUS, ";
		sql += " PS.PEND_STATUS, ";
		sql += " PE.DATA_INICIO, ";
		sql += " PE.DATA_LIMITE ";
		sql += " FROM PEND PE ";
		sql += " INNER JOIN MAND_JUD MJ ON PE.ID_PEND = MJ.ID_PEND ";
		sql += " INNER JOIN MAND_JUD_STATUS MJS ON MJS.ID_MAND_JUD_STATUS = MJ.ID_MAND_JUD_STATUS ";
		sql += " INNER JOIN PROC PR ON PR.ID_PROC = PE.ID_PROC ";
		sql += " INNER JOIN PEND_TIPO PT ON PT.ID_PEND_TIPO = PE.ID_PEND_TIPO ";
		sql += " INNER JOIN PEND_STATUS PS ON PS.ID_PEND_STATUS = PE.ID_PEND_STATUS ";
		sql += " WHERE ";
		sql += " MJ.ID_USU_SERV_1 = ? "; ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		sql += " AND PT.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipo.MANDADO.getValue());
		sql += " AND MJS.MAND_JUD_STATUS_CODIGO IN (?,?) ";
		ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO);
		ps.adicionarLong(MandadoJudicialStatusDt.REDISTRIBUIDO);
		
		
		if(ordenacao == null) {
			sql += " ORDER BY PE.DATA_LIMITE ASC ";
		}
		else {
			
			switch(ordenacao) {
				case "dataInicio":
					sql += " ORDER BY PE.DATA_INICIO ASC ";
				break;
				
				case "dataLimite":
					sql += " ORDER BY PE.DATA_LIMITE ASC ";
				break;
				
				case "processo":
					sql += " ORDER BY PR.ID_PROC ASC ";
				break;
				
				case "numeroMandado":
					sql += " ORDER BY MJ.ID_MAND_JUD ASC ";
				break;
				
				default:
					sql += " ORDER BY PE.DATA_LIMITE ASC ";
				break;
			
			}
		}

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			
			// retirar botao liberar lista pendencias
			boolean oficial = false;
			if(usuarioNe.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.OFICIAL_JUSTICA){
				oficial = true;
			}
			// Preenche map
			while (rs1.next()) {
				if (pendencias == null) {
					pendencias = new ArrayList();
				}
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendenciaTipo( rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setAssistencia( rs1.getString("ASSISTENCIA"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setPendenciaStatus( rs1.getString("PEND_STATUS"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarData(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setId_MandadoJudicial(rs1.getString("ID_MAND_JUD"));
				if(!oficial){
					pendenciaDt.setPodeLiberar(true);
				} else {
					pendenciaDt.setPodeLiberar(false);
				}
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return pendencias;
	}

	/**
	 * Consulta pendencias PreAnalisadas informando se pode liberar contendo o
	 * hash para verificacao
	 * 
	 * @author lsbernardes
	 * @since 24/03/2009
	 * @param usuarioNe
	 *            usuario da sessao
	 * @param tipo
	 *            tipo pendencia
	 * @return lista de pendencias reservadas com o hash
	 * @throws Exception
	 */
	public List consultarPreAnalisadasComHash(UsuarioNe usuarioNe, String tipo, String filtro) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

//		String sql = "SELECT * FROM ( SELECT (	SELECT  1 as PODE_LIBERAR FROM PROJUDI.PEND_RESP pr ";
//		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = ? ";
//		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
//
//		if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equals("")){
//			sql += " OR pr.ID_USU_RESP = ? ";
//			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
//		}
//
//		sql += " ) ) PODE_LIBERAR,  ";
//		sql += "        pa.ID_PROC, pa.PROC_NUMERO_COMPLETO , pa.ID_MOVI, pa.MOVI, " +
//					"   pa.ID_PEND, pa.PEND_TIPO, pa.PEND_STATUS, pa.DATA_INICIO, pa.DATA_TEMP ";
//		sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa WHERE ( pa.ID_USU_FINALIZADOR = ? ";
//		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
//
//		if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equals("")){
//			sql += " OR pa.ID_USU_FINALIZADOR = ? ";
//			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
//		}
		
		String sql = " SELECT  pa.ID_PEND, pa.ID_PROC, ";
		sql += " (  ";
		sql += "   CASE NVL(PR.PROC_NUMERO,0) ";
		sql += "   WHEN 0 ";
		sql += "    THEN NULL ";
		sql += "   ELSE (PR.PROC_NUMERO ";
		sql += "    || '.' ";
		sql += "    || PR.DIGITO_VERIFICADOR) ";
		sql += "  END) as PROC_NUMERO_COMPLETO, ";
		sql += " pa.ID_MOVI, ";
		sql += " (  ";
		sql += "  CASE NVL(pa.ID_MOVI,0) ";
		sql += "  WHEN 0 ";
		sql += "   THEN NULL ";
		sql += "  ELSE (MT.MOVI_TIPO ";
		sql += "    || ' (' ";
		sql += "    || TO_CHAR(M.DATA_REALIZACAO,'DD/MM/YYYY HH24:MI:SS') ";
		sql += "    ||   ')') ";
		sql += "  END) as MOVI, ";
		sql += " pa.ID_PEND_TIPO,pa.PEND_TIPO, pa.ID_PEND_STATUS, PS.PEND_STATUS, pa.DATA_INICIO ";
		
		sql += " FROM  PROJUDI.VIEW_PEND_PRE_ANA_RESP pa ";
		sql += " LEFT JOIN MOVI M   ON  pa.ID_MOVI = M.ID_MOVI ";
		sql += " LEFT JOIN MOVI_TIPO MT ON M.ID_MOVI_TIPO = MT.ID_MOVI_TIPO ";
		sql += " LEFT JOIN PROC PR  ON pa.ID_PROC = PR.ID_PROC ";
		sql += " LEFT JOIN PEND_STATUS PS ON PS.ID_PEND_STATUS = pa.ID_PEND_STATUS ";
     	
     	sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";     		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
   		if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
   			sql += " OR pa.ID_USU_FINALIZADOR = ?";   			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
   		}
   		
   		sql += " ) ";
		
		if (usuarioNe.isAssessorAdvogado() || usuarioNe.isAssessorMP()){
			sql += " AND ( pa.ID_PEND_TIPO <> ? AND pa.ID_PEND_TIPO <> ? ) ";
			ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
			ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		}
		

		if (filtro != null && filtro.equals("1")) sql += " AND  pa.ID_SERV is not null ";
		else if (filtro != null && filtro.equals("2")) sql += " AND  pa.ID_SERV_TIPO is not null ";
		else if (filtro != null && filtro.equals("3")) sql += " AND  pa.ID_SERV_CARGO is not null ";
		else if (filtro != null && filtro.equals("4")) sql += " AND  pa.ID_USU_RESP IS NULL ";

		if (tipo != null && !tipo.equals("")){
			sql += " AND  pa.ID_PEND_TIPO = ? ";
			ps.adicionarLong(tipo);
		}

		sql += " ORDER BY ";
		sql += "	 pa.PEND_TIPO ";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			// Preenche map
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();

				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendenciaTipo( rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setPendenciaStatus( rs1.getString("PEND_STATUS"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				//pendenciaDt.setDataTemp( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_TEMP")));
//				if(rs1.getString("PODE_LIBERAR") == null || rs1.getString("PODE_LIBERAR").toString().equalsIgnoreCase("null"))
//					pendenciaDt.setPodeLiberar(true);
//				else
				pendenciaDt.setPodeLiberar(false);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));

				pendencias.add(pendenciaDt);

			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}

	/**
	 * Consulta pendencias abertas serventia cargo contendo o hash para
	 * verificacao
	 * 
	 * @author lsbernardes
	 * @since 10/08/2009
	 * @param usuarioNe
	 *            usuario da sessao
	 * @param tipo
	 *            tipo pendencia
	 * @param ehPendenteAssinatura
	 * @return lista de pendencias com o hash
	 * @throws Exception
	 */
	public List consultarAbertasServentiaCargoComHash(UsuarioNe usuarioNe, String tipo, boolean ehPendenteAssinatura) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql;
		
		if(tipo != null && tipo.equals("106") ){			
			sql = " SELECT * FROM PROJUDI.VIEW_PEND pasc  ";
			sql += " JOIN PEND_RESP PR ON PR.ID_PEND = pasc.ID_PEND ";
			sql += " JOIN PROC PV ON PV.ID_PROC = pasc.ID_PROC  ";
			sql += " JOIN AUDI_PROC AP ON AP.ID_PROC = PV.ID_PROC ";
			sql += " JOIN AUDI AU ON AU.ID_AUDI = AP.ID_AUDI ";
			sql += "WHERE PR.ID_SERV_CARGO = ? ";
		}else {
			sql = "SELECT * FROM PROJUDI.VIEW_PEND_ATIVAS_RESP pasc ";
			sql += "WHERE pasc.ID_SERV_CARGO = ? ";

		}
		
		if (usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe() != null && usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe().length() > 0){
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
		}		
		else {
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargo());
		}

		if(tipo != null && tipo.equals("106") && !usuarioNe.getUsuarioDt().isTurmaJulgadora() ){
			sql += " AND AP.ID_AUDI_PROC_STATUS = 1 ";
		}
		
		if (tipo != null && !tipo.equals("") ){
			sql += " AND  pasc.ID_PEND_TIPO = ? ";
			ps.adicionarLong(tipo);
		}
		
		if (ehPendenteAssinatura)
			sql += " AND EXISTS (SELECT 1";
		else			
			sql += " AND NOT EXISTS (SELECT 1";
		sql += "                   FROM PROJUDI.PEND_ARQ pa";
		sql += "                  WHERE pasc.ID_PEND = pa.ID_PEND";
		sql += "                   AND pa.RESPOSTA = ?";
		ps.adicionarLong(1);		
		sql += "                   AND pa.CODIGO_TEMP = ?)";		
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);

		if(tipo != null && tipo.equals("106") ){
			sql += "  AND AP.DATA_MOVI IS NULL ORDER BY AU.DATA_AGENDADA, PV.ID_CLASSIFICADOR, pasc.ID_PEND_TIPO, pasc.data_inicio ";
		}else{
			
			sql += "  ORDER BY pasc.PEND_TIPO, pasc.data_inicio ";
		}
		

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			// Preenche map
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();

				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR"));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendencias.add(pendenciaDt);

			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}

	/**
	 * Consulta pendencias reservadas ordenando por tipo da pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 02/12/2008 16:19
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param boolean
	 *            ordemTipo, se deve ordenar pelo tipo da pendencia
	 * @return List
	 * @throws Exception
	 */
	public List consultarReservadas(UsuarioDt usuarioDt, boolean ordemTipo) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlSel = "SELECT * FROM VIEW_PEND_RESERV pa WHERE pa.ID_USU_FINALIZADOR = ? ";
		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());

		// Verifica se e para ordenar pelo tipo da pendencia
		if (ordemTipo == true) sqlSel += " ORDER BY pa.ID_PEND_TIPO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sqlSel, ps);

			// Preenche map
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();

				PendenciaDt pendenciaDt = new PendenciaDt();

				/*
				 * pendenciaDt.setId(rs1.getString("ID_PEND"));
				 * pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				 * pendenciaDt.setDataTemp(Funcoes.FormatarDataHora(rs1.getDate("DATA_TEMP")));
				 */

				super.associarDt(pendenciaDt, rs1);

				pendencias.add(pendenciaDt);

			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}

	/**
	 * Consulta pendencias ativas da serventia para pagina Inicial
	 * 
	 * @author Leandro Bernardes
	 * @since 31/03/2009
	 * @param usuarioDt
	 *            usuario de referencia
	 * @return
	 * @throws Exception
	 */
	/*public Map consultarTiposAtivasServentiaPaginaInicial(UsuarioDt usuarioDt){
		//Map est = new HashMap();
		SortedMap est = new TreeMap();

		String sqlSel = "SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO, COUNT(*) as QTD ";

		sqlSel += " FROM PROJUDI.VIEW_PEND_ATIVAS_RESP pa ";

		// Monta as condicoes para que o usuario visualize uma pendencia
		if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) {
			// Ou nao e para um usuario, nem para um tipo de serventia e sim
			// para uma serventia
			sqlSel += " WHERE ( ( pa.ID_SERV = " + usuarioDt.getId_Serventia() + " AND pa.ID_SERV_TIPO IS NULL AND pa.ID_USU_RESP IS NULL AND PEND_STATUS_CODIGO <> " + PendenciaStatusDt.AguardandoVisto + " AND PEND_STATUS_CODIGO <> " + PendenciaStatusDt.AguardandoRetorno + " ) )";
		}

		sqlSel += " group by pa.ID_PEND_TIPO ORDER BY pa.PEND_TIPO";

		try{
			ResultSetTJGO rs1 = this.consultar(sqlSel);

			// Preenche map
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QTD") });
			}
		
		}

		return est;
	}*/

	/**
	 * Consulta pendencias ativas para pagina Inicial do assistente
	 * 
	 * @author Leandro Bernardes
	 * @since 11/08/2009
	 * @param usuarioDt
	 *            usuario de referencia
	 * @return
	 * @throws Exception
	 */
	/*public Map consultarTiposAtivasAssistentePaginaInicial(UsuarioDt usuarioDt){
		Map est = new HashMap();

		Calendar calendario = Calendar.getInstance();
		calendario.add(Calendar.HOUR_OF_DAY, -1);
		Date tempDate = calendario.getTime();

		String sqlSel = "SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO, COUNT(*) as QTD ";

		sqlSel += " FROM PROJUDI.VIEW_PEND_ATIVAS_RESP pa WHERE pa.PEND_TIPO_CODIGO <> " + PendenciaTipoDt.INTIMACAO + " AND pa.PEND_TIPO_CODIGO <> " + PendenciaTipoDt.CARTA_CITACAO + " AND ( ";

		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			// Onde o usuario destinado e o usuario
			sqlSel += " ( pa.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia() + " OR pa.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe() + " OR pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia();
			sqlSel += "  OR ( pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentiaChefe() + " AND (pa.DATA_TEMP >=" + Funcoes.BancoDataHora(tempDate) + ")";
			sqlSel += " ) ) ) ";

		} else {
			sqlSel += " ( ( pa.ID_SERV IS NULL AND pa.ID_SERV_TIPO IS NULL AND pa.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia() + " ) ) ) ";
		}

		sqlSel += " group by pa.ID_PEND_TIPO ORDER BY pa.PEND_TIPO";

		try{
			ResultSetTJGO rs1 = this.consultar(sqlSel);

			// Preenche map
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QTD") });
			}
		
		}

		return est;
	} */

	/**
	 * Consulta a estatistica dos tipos das pendencias para o cargo serventia
	 * ativas para pagina Inicial
	 * 
	 * @author Leandro Bernardes
	 * @since 31/03/2009
	 * @param usuarioDt
	 *            usuario de referencia
	 * @return
	 * @throws Exception
	 */
	/*public Map consultarTiposAtivasCargoServentiaPaginaInicial(UsuarioDt usuarioDt){
		Map est = new HashMap();

		String sqlSel = "SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO, COUNT(*) as QTD ";

		sqlSel += " FROM PROJUDI.VIEW_PEND_ATIVAS_RESP pa ";

		// Monta as condicoes para que o usuario visualize uma pendencia
		if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) {

			// Ou nao e para um usuario, nem para um tipo de serventia e sim
			// para uma serventia cargo
			sqlSel += "  WHERE (  ( pa.ID_SERV IS NULL AND pa.ID_SERV_TIPO IS NULL AND pa.ID_USU_RESP IS NULL AND " + " pa.ID_SERV_CARGO IN ( " + " SELECT ID_SERV_CARGO FROM SERV_CARGO aux_sg " + " INNER JOIN USU_SERV_GRUPO aux_usg " + " on aux_sg.ID_USU_SERV_GRUPO = aux_usg.ID_USU_SERV_GRUPO " + " INNER JOIN USU_SERV aux_us "
					+ " on aux_usg.ID_USU_SERV = aux_us.ID_USU_SERV " + " WHERE aux_us.ID_USU_SERV = " + usuarioDt.getId_UsuarioServentia() + " AND PEND_STATUS_CODIGO <> " + PendenciaStatusDt.AguardandoVisto + " AND PEND_STATUS_CODIGO <> " + PendenciaStatusDt.AguardandoRetorno + " ) ) )";
		}

		sqlSel += " group by pa.ID_PEND_TIPO ORDER BY pa.PEND_TIPO";

		try{
			ResultSetTJGO rs1 = this.consultar(sqlSel);

			// Preenche map
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QTD") });
			}
		
		}

		return est;
	}*/

	/**
	 * Consulta a estatistica dos tipos das pendencias ativas para cargo
	 * serventia pagina Inicial promotor
	 * 
	 * @author Leandro Bernardes
	 * @since 28/09/2009
	 * @param usuarioDt
	 *            usuario de referencia
	 * @return
	 * @throws Exception
	 */
	public Map consultarTiposAtivasCargoServentiaPaginaInicialPromotor(UsuarioDt usuarioDt) throws Exception {
		Map est = new HashMap();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlSel = "SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO, COUNT(*) as QTD ";

		sqlSel += " FROM PROJUDI.VIEW_PEND_ATIVAS_RESP pa ";

		// Monta as condicoes para que o usuario visualize uma pendencia
		if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) {

			// Ou nao e para um usuario, nem para um tipo de serventia e sim
			// para uma serventia cargo
			sqlSel += "  WHERE  pa.PEND_TIPO_CODIGO <> ? AND (  ( pa.ID_SERV IS NULL AND pa.ID_SERV_TIPO IS NULL AND pa.ID_USU_RESP IS NULL AND " + " pa.ID_SERV_CARGO IN ( " + " SELECT ID_SERV_CARGO FROM SERV_CARGO aux_sg " + " INNER JOIN USU_SERV_GRUPO aux_usg " + " on aux_sg.ID_USU_SERV_GRUPO = aux_usg.ID_USU_SERV_GRUPO "
					+ " INNER JOIN USU_SERV aux_us " + " on aux_usg.ID_USU_SERV = aux_us.ID_USU_SERV " + " WHERE aux_us.ID_USU_SERV = ? AND PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ? ) ) )";
			ps.adicionarLong(PendenciaTipoDt.INTIMACAO );
			ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
			ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_VISTO);
			ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_RETORNO);
		}

		sqlSel += " group by pa.ID_PEND_TIPO, pa.PEND_TIPO ORDER BY pa.PEND_TIPO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sqlSel, ps);

			// Preenche map
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QTD") });
			}
		
		}
		finally{
			if (rs1 != null)  rs1.close();
		}

		return est;
	}

	/**
	 * Consulta a estatistica dos tipos das pendencias para o serventia tipo
	 * ativas para pagina Inicial
	 * 
	 * @author Leandro Bernardes
	 * @since 31/03/2009
	 * @param usuarioDt
	 *            usuario de referencia
	 * @return
	 * @throws Exception
	 */
	public Map consultarTiposAtivasServentiaTipoPaginaInicial(UsuarioDt usuarioDt) throws Exception {
		Map est = new HashMap();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlSel = "SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO, COUNT(*) as QTD ";

		sqlSel += " FROM PROJUDI.VIEW_PEND_ATIVAS_RESP pa ";

		// Monta as condicoes para que o usuario visualize uma pendencia
		if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().trim().equals("")) {
			// Ou nao e para um usuario mas e para um tipo de serventia
			sqlSel += " WHERE ( ( pa.ID_SERV IS NULL AND  pa.ID_SERV_TIPO = ? AND (PEND_TIPO_CODIGO <> ?  ) AND pa.ID_USU_RESP IS NULL AND PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ? ) ) ";
			ps.adicionarLong(usuarioDt.getId_ServentiaTipo());		
			ps.adicionarLong(PendenciaTipoDt.INFORMATIVO);
			ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_VISTO);
			ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_RETORNO);
		}

		sqlSel += " group by pa.ID_PEND_TIPO, pa.PEND_TIPO ORDER BY pa.PEND_TIPO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sqlSel, ps);

			// Preenche map
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QTD") });
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return est;
	}

	/**
	 * Seleciona ultima pendencia e marca como bloqueada O prazo de validade da
	 * pendencia e de uma hora caso o usuario ultrapasse o tempo de reserva,
	 * esta pendencia automaticamente nao ficara mais bloqueada para ele, e
	 * podera ser pega por outro usuario.
	 * 
	 * Descricao Tecnica 1. Atualiza-se o registro dentro das condicoes acima,
	 * alterando o CodigoTemp pelo numero sorteado 2. Seleciona o registro e
	 * retorna para o usuarios
	 * 
	 * Descricao do possivel problema 1. Como nao atualiza-se o registro
	 * utilizando as condicoes de ordenacao, limite e juncao do banco de dados,
	 * a schedule a ser executada nao sera atomica, permitindo assim o confronto
	 * de duas ou mais atualizacoes causando a chamada atualizacao perdida.
	 * 
	 * Solucao do problema 1. O mysql permite a ordenacao e limite em um comando
	 * de atualizacao, para a juncao da tabela, Existe a atualizacao multipla no
	 * mysql, testar se possivel usar a tabela sem atualizar algum registro 1.1 -
	 * O mysql nao permite a ordenacao e o limite com multiplas tabelas 2.
	 * Criou-se uma visao (view) para retornar de acordo com o tipo de
	 * pendencia, a mais antiga
	 * 
	 * @author Ronneesley Moura Teles
	 * @param int
	 *            tipo, Tipo da pendencia
	 * @param UsuarioDt
	 *            usuarioDt, pojo de usuario
	 * @since 31/03/2008
	 * @return PendenciaDt
	 * */
	/*public PendenciaDt consultarUltimaPendencia(int tipo, UsuarioDt usuarioDt){
		if (this.Conexao != null) {
			PendenciaDt pendenciaDt = new PendenciaDt();

			pendenciaDt.setCodigoTemp(String.valueOf(Math.round(Math.random() * 100000)));

			// ****************************************************************************
			// Para teste somente retornar o registro da pendencia
			// ****************************************************************************

			
			 // Colocar o filtro da serventia do usuario
			  
			

			// Atualiza registro nas condicoes especificadas
			//String sqlUp = "UPDATE PROJUDI.Pendencia set PEND.CODIGO_TEMP = " + pendenciaDt.getCodigoTemp() + ", PEND.DATA_TEMP = " + Funcoes.BancoDataHora(new Date()) + " WHERE PEND.ID_PEND = ( " + " SELECT up.ID_PEND FROM PROJUDI.VIEW_ULTIMA_PEND up WHERE up.ID_PEND_TIPO = " + tipo + " )";

			String sqlUp =  "UPDATE PROJUDI.PEND inner joIN ( SELECT ID_PEND";
			sqlUp +=			 " FROM  PROJUDI.VIEW_ULTIMA_PEND ";
			sqlUp +=             " WHERE ID_PEND_TIPO = "+tipo+" group by ID_PEND_TIPO ";
			sqlUp +=         "  ) PendenciaUltimaServentia on PROJUDI.PEND.ID_PEND =  PendenciaUltimaServentia.ID_PEND ";
			sqlUp +=	     " set PEND.CODIGO_TEMP = "+ pendenciaDt.getCodigoTemp() + " , PEND.DATA_TEMP = " + Funcoes.BancoDataHora(new Date());
			
			// Fechar os campos utilizados
			String sqlSel = "SELECT ID_PEND, ID_PEND_TIPO, PEND_TIPO, PEND_STATUS, PROC_NUMERO, " + " ID_PROC, NOME_PARTE, DATA_INICIO, DATA_LIMITE FROM PROJUDI.VIEW_PEND WHERE CODIGO_TEMP = " + pendenciaDt.getCodigoTemp();

			try{
				this.executar(sqlUp);

				ResultSetTJGO rs1 = this.consultar(sqlSel);

				if (rs1.next()) {

					pendenciaDt.setId(rs1.getString("ID_PEND"));
					pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
					pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
					pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
					pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
					pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
					pendenciaDt.setNomeParte(rs1.getString("NOME_PARTE"));
					pendenciaDt.setDataInicio(rs1.getString("DATA_INICIO"));
					pendenciaDt.setDataLimite(rs1.getString("DATA_LIMITE"));

					String sqlUpTemp = "UPDATE PROJUDI.PEND SET CODIGO_TEMP = null WHERE ID_PEND = " + pendenciaDt.getId();

					this.executar(sqlUpTemp);

				}
			
			}

			return pendenciaDt;
		}

		return null;
	}
	*/

	/**
	 * Sobrescrevendo método inserir para setar DataInicio com a data do banco -
	 * 29/09/2008 Modificacoes para o funcionamento sem os codigos
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 29/09/2008
	 */
	public void inserir(PendenciaDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		SqlCampos = "INSERT INTO PROJUDI.PEND (";
		SqlValores = " Values (";		
		if (!(dados.getId_PendenciaTipo().length() == 0)){
			SqlCampos += "ID_PEND_TIPO";
			SqlValores+="?";
			ps.adicionarLong(dados.getId_PendenciaTipo());
		}else if (!(dados.getPendenciaTipoCodigo().length() == 0)){
			SqlCampos += ",ID_PEND_TIPO";
			SqlValores+=", (SELECT ID_PEND_TIPO FROM PROJUDI.PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)";
			ps.adicionarLong(dados.getPendenciaTipoCodigo());
		}		
		if (!(dados.getId_Movimentacao().length() == 0)){
			SqlCampos += ",ID_MOVI";
			SqlValores+=",?";
			ps.adicionarLong(dados.getId_Movimentacao());
		}
		if (!(dados.getId_Processo().length() == 0)){
			SqlCampos += ",ID_PROC";
			SqlValores+=",?";
			ps.adicionarLong(dados.getId_Processo());
		}
		if (!(dados.getId_ProcessoPrioridade().length() == 0)){
			SqlCampos += ",ID_PROC_PRIOR";
			SqlValores+=",?";
			ps.adicionarLong(dados.getId_ProcessoPrioridade());
		} else if (!(dados.getProcessoPrioridadeCodigo().length() == 0)){
			SqlCampos += ",ID_PROC_PRIOR";
			SqlValores += ", (SELECT ID_PROC_PRIOR FROM PROJUDI.PROC_PRIOR WHERE PROC_PRIOR_CODIGO = ?)";
			ps.adicionarLong(dados.getProcessoPrioridadeCodigo());
		}
		// if (!(dados.getId_ProcessoPrioridade().length() == 0)) Sql +=
		// ",ID_PROC_PRIOR ";
		// if (!(dados.getId_ProcessoPrioridade().length() == 0)) Sql += "," +
		// Funcoes.BancoInteiro(dados.getId_ProcessoPrioridade());
		if (!(dados.getId_ProcessoParte().length() == 0)){
			SqlCampos += ",ID_PROC_PARTE";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoParte());
		}
		if (!(dados.getId_PendenciaStatus().length() == 0)){
			SqlCampos += ",ID_PEND_STATUS";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_PendenciaStatus());
		} else if (!(dados.getPendenciaStatusCodigo().length() == 0)){
			SqlCampos += ",ID_PEND_STATUS";
			SqlValores += ", (SELECT ID_PEND_STATUS FROM PROJUDI.PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)";
			ps.adicionarLong(dados.getPendenciaStatusCodigo());
		}
		if (!(dados.getId_UsuarioCadastrador().length() == 0)){
			SqlCampos += ",ID_USU_CADASTRADOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_UsuarioCadastrador());
		}
		if (!(dados.getId_UsuarioFinalizador().length() == 0)){
			SqlCampos += ",ID_USU_FINALIZADOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_UsuarioFinalizador());
		}
		
		if (!(dados.getId_Classificador().length() == 0)){
			SqlCampos += ",ID_CLASSIFICADOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Classificador());
		}
		if (!(dados.getDataInicio().length() == 0)){
			SqlCampos += ",DATA_INICIO";
			SqlValores += ",?";
			ps.adicionarDateTime(dados.getDataInicio());
		} else {
			SqlCampos += ",DATA_INICIO";
			SqlValores += ",?";
			ps.adicionarDateTime(new Date());	
		}
		if (!(dados.getDataFim().length() == 0)){
			SqlCampos += ",DATA_FIM";
			SqlValores += ",?";
			ps.adicionarDateTime(dados.getDataFim());
		}
		if (!(dados.getDataLimite().length() == 0)){
			SqlCampos += ",DATA_LIMITE";
			SqlValores += ",?";
			ps.adicionarDateTime(dados.getDataLimite());
		}
		if (!(dados.getDataTemp().length() == 0)){
			SqlCampos += ",DATA_TEMP";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataTemp());
		}
		if (!(dados.getDataDistribuicao().length() == 0)){
			SqlCampos += ",DATA_DIST";
			SqlValores+=",?";
			ps.adicionarDate(dados.getDataDistribuicao());
		}
		if (!(dados.getPrazo().length() == 0)){
			SqlCampos += ",PRAZO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getPrazo());
		}
		if (!(dados.getId_PendenciaPai().length() == 0)){
			SqlCampos += ",ID_PEND_PAI ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_PendenciaPai());
		}
		if (!(dados.getDataVisto().length() == 0)){
			SqlCampos += ",DATA_VISTO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataVisto());
		}
		if (!(dados.getCodigoTemp().length() == 0)){
			SqlCampos += ",CODIGO_TEMP";
			SqlValores += ",?";
			ps.adicionarLong(dados.getCodigoTemp());
		}
		if ((dados.getCodigoPendTemp() != null)){
			SqlCampos += ",COD_PEND_TEMP";
			SqlValores += ",?";
			ps.adicionarLong(dados.getCodigoPendTemp());
		}
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

		dados.setId(executarInsert(Sql, "ID_PEND", ps));
	}
	
	public void inserirPendenciaLiberarAcesso(PendenciaDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
				
		SqlCampos = "INSERT INTO PROJUDI.PEND (";
		SqlValores = " Values (";		
		if (!(dados.getId_PendenciaTipo().length() == 0)){
			SqlCampos += "ID_PEND_TIPO";
			SqlValores += "?";
			ps.adicionarLong(dados.getId_PendenciaTipo());
		} else if (!(dados.getPendenciaTipoCodigo().length() == 0)){
			SqlCampos += "ID_PEND_TIPO";
			SqlValores += ", (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)";
			ps.adicionarLong(dados.getPendenciaTipoCodigo());
		}		
		if (!(dados.getId_Movimentacao().length() == 0)){
			SqlCampos += ",ID_MOVI";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Movimentacao());
		}
		if (!(dados.getId_Processo().length() == 0)){
			SqlCampos += ",ID_PROC";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Processo());
		}
		if (!(dados.getId_ProcessoPrioridade().length() == 0)){
			SqlCampos += ",ID_PROC_PRIOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoPrioridade());
		} else if (!(dados.getProcessoPrioridadeCodigo().length() == 0)) {
			SqlCampos += ",ID_PROC_PRIOR";
			SqlValores += ", (SELECT ID_PROC_PRIOR FROM PROC_PRIOR WHERE PROC_PRIOR_CODIGO = ?)";;
			ps.adicionarLong(dados.getProcessoPrioridadeCodigo());
		}
		// if (!(dados.getId_ProcessoPrioridade().length() == 0)) Sql +=
		// ",ID_PROC_PRIOR ";
		// if (!(dados.getId_ProcessoPrioridade().length() == 0)) Sql += "," +
		// Funcoes.BancoInteiro(dados.getId_ProcessoPrioridade());
		if (!(dados.getId_ProcessoParte().length() == 0)){
			SqlCampos += ",ID_PROC_PARTE";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoParte());
		}
		if (!(dados.getId_PendenciaStatus().length() == 0)){
			SqlCampos += ",ID_PEND_STATUS";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_PendenciaStatus());
		} else if (!(dados.getPendenciaStatusCodigo().length() == 0)){
			SqlCampos += ",ID_PEND_STATUS";
			SqlValores += ", (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?)";
			ps.adicionarLong(dados.getPendenciaStatusCodigo());
		}
		if (!(dados.getId_UsuarioCadastrador().length() == 0)){
			SqlCampos += ",ID_USU_CADASTRADOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_UsuarioCadastrador());
		}
		if (!(dados.getId_UsuarioFinalizador().length() == 0)){
			SqlCampos += ",ID_USU_FINALIZADOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_UsuarioFinalizador());
		}
		SqlCampos += ",DATA_INICIO";
		SqlValores += ",?";
		ps.adicionarDateTime(dados.getDataInicio());		
		if (!(dados.getDataFim().length() == 0)){
			SqlCampos += ",DATA_FIM";
			SqlValores += ",?";
			ps.adicionarDateTime(dados.getDataFim());
		}
		if (!(dados.getDataLimite().length() == 0)){
			SqlCampos += ",DATA_LIMITE";
			SqlValores += ",?";
			ps.adicionarDateTime(dados.getDataLimite());
		}
		if (!(dados.getDataDistribuicao().length() == 0)){
			SqlCampos += ",DATA_DIST";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataDistribuicao());
		}
		if (!(dados.getPrazo().length() == 0)){
			SqlCampos += ",PRAZO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getPrazo());
		}
		if (!(dados.getId_PendenciaPai().length() == 0)){
			SqlCampos += ",ID_PEND_PAI ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_PendenciaPai());
		}
		if (!(dados.getDataVisto().length() == 0)){
			SqlCampos += ",DATA_VISTO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataVisto());
		}
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

		dados.setId(executarInsert(Sql, "ID_PEND", ps));

	}

	/**
	 * Atualiza as informacoes de uma pendencia para ser encaminhada -
	 * Informacoes a serem mudadas: -- Status da pendencia: Informado -- Data de
	 * fim: informado (normalmente este valor sera o valor do momento,
	 * entretanto fica a cargo da camada superior NE, decidir o valor)
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 25/11/2008 09:31
	 * @param PendenciaDt
	 *            pendenciaDt, vo de pendencia
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @throws Exception
	 */
	public void encaminhar(PendenciaDt pendenciaDt, UsuarioDt usuarioDt, Date data) throws Exception {
		//Date data = new Date();
		//pendenciaDt.setDataFim(Funcoes.DataHora(data));
		//pendenciaDt.setDataVisto(Funcoes.DataHora(data));
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
		// Monta subSELECT para pesquisar o id real do status
		sql += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";
		ps.adicionarLong(pendenciaDt.getPendenciaStatusCodigo());		
		sql += ", DATA_FIM = ?, DATA_VISTO = ?, ID_USU_FINALIZADOR = ?";
		ps.adicionarDateTime(data);
		ps.adicionarDateTime(data);
		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
		sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(pendenciaDt.getId());

		this.executarUpdateDelete(sql, ps);
	}

	/**
	 * Finaliza uma pendencia SIGNIFICADO: Uma pendencia e finalizada quando o
	 * responsavel visualiza depois de cumprida, logo, a data de fim nao e
	 * alterada, pois esta ja estava fechada, entretanto nao estava vista
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 24/11/2008 10:59
	 * @param PendenciaDt
	 *            pendenciaDt, vo de pendencia
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @throws Exception
	 */
	public void finalizar(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
		Date data = new Date();
		pendenciaDt.setDataVisto(Funcoes.DataHora(data));
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
		// Monta subSELECT para pesquisar o id real do status
		sql += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";		ps.adicionarLong(pendenciaDt.getPendenciaStatusCodigo());		
		sql += ", DATA_VISTO = ?";			ps.adicionarDateTime(pendenciaDt.getDataVisto());
		sql += " WHERE ID_PEND = ?";		ps.adicionarLong(pendenciaDt.getId());

		this.executarUpdateDelete(sql, ps);
	}

	/**
	 * Finaliza uma pendencia SIGNIFICADO: Uma pendencia e finalizada quando o
	 * responsavel visualiza depois de cumprida, logo, a data de fim nao e
	 * alterada, pois esta ja estava fechada, entretanto nao estava vista
	 * 
	 * @author Leandro Bernardes
	 * @since 05/08/2009
	 * @param PendenciaDt
	 *            pendenciaDt, vo de pendencia
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @throws Exception
	 */
	public void finalizarPrazoDecorrido(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
		Date data = new Date();
		pendenciaDt.setDataVisto(Funcoes.DataHora(data));
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "UPDATE PROJUDI.PEND set DATA_VISTO = ?";		ps.adicionarDateTime(pendenciaDt.getDataVisto());
		sql += " WHERE ID_PEND = ?";								ps.adicionarLong(pendenciaDt.getId());

		this.executarUpdateDelete(sql, ps);
	}

	/**
	 * Parametros em objetos Este caso serve somente para pendencias que sao
	 * autos conclusos, pois fecham a data de visto
	 * 
	 * @since 26/11/2008 09:25 alteracoes
	 * @author Ronneesley Moura Teles
	 * @since 04/04/2008 16:54 Inclui as variaveis especificas para formacao do
	 *        SQL, entretanto nao testei se a rotina funciona corretamente a
	 *        modificacao foi necessaria, pois estava evidente que a troca dos
	 *        caracteres, deixou-a sem funcionar. *
	 * @author Ronneesley Moura Teles
	 * @since 06/06/2008 10:01 Otimizacao
	 * @param PendenciaDt
	 *            pendenciaDt, vo de pendencia
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @throws Exception
	 */
	public void fecharPendencia(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Date tempDate = new Date();
		
		try{
			
			String sqlSelect = " SELECT ID_PEND FROM PROJUDI.PEND WHERE ID_PEND = ? AND DATA_FIM is not null ";
			ps.adicionarLong(pendenciaDt.getId());
			
			rs1 = this.consultar(sqlSelect, ps);
			if (rs1.next()){
				throw new MensagemException("Pendência (ID: "+ pendenciaDt.getId() +") já finalizada. Efetuar consulta novamente para atualizar. Dupla tentativa de fechar pendência.");
			}
			
			//Limpa os parâmetros utilizados anteriormente
			ps.limpar();
			
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
	        pendenciaDt.setDataFim(df.format(tempDate));
	        pendenciaDt.setDataVisto(df.format(tempDate));
			
			// String id_pendencia, String id_UsuarioFinalizador, int pendenciaStatusCodigo
			String sqlUpdate = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
			// Monta subSELECT para pesquisar o id real do status
			sqlUpdate += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";
			ps.adicionarLong(pendenciaDt.getPendenciaStatusCodigo());
			sqlUpdate += ", ID_USU_FINALIZADOR = ?, DATA_VISTO = ?, DATA_FIM = ?, ID_CLASSIFICADOR = ?";
			ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
			ps.adicionarDateTime(tempDate);
			ps.adicionarDateTime(tempDate);	
			ps.adicionarBigDecimalNull();
			sqlUpdate += " WHERE ID_PEND = ? AND DATA_FIM IS NULL ";
			ps.adicionarLong(pendenciaDt.getId());		
			
			this.executarUpdateDelete(sqlUpdate, ps);
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}

	/**
	 * Distribui pendencia para um usuario da serventia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 16/05/2008 17:06
	 * @param String
	 *            idUsuarioServentia, id do usuario da serventia
	 * @return boolean
	 */
	public boolean distribuir(String idPendencia, String idUsuarioServentia, Date data_temp) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "UPDATE PROJUDI.PEND set ID_USU_FINALIZADOR = ?, DATA_TEMP = ? WHERE ID_PEND = ?";
		ps.adicionarLong(idUsuarioServentia);
		ps.adicionarDateTime(data_temp);
		ps.adicionarLong(idPendencia);

		this.executarUpdateDelete(sql, ps);

		return true;
	}

	public int[] consultarUltimaDistribuicaoMandado(String id_escala) throws Exception {
		String stSql;

		int inUltimo[] = new int[2];
		int inQuantidadeDistribuicao = 0;
		int inContador = 0;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{

			// pego para qual escala foi distribuido a última pendencia

			stSql = "SELECT man.ID_USU_SERV_1, esc.QUANTIDADE_MAND as QUANTIDADE_DIST";
			stSql += " FROM PROJUDI.PEND pen";
			stSql += " INNER JOIN PROJUDI.MAND_JUD man on pen.ID_PEND = man.ID_PEND";
			stSql += " INNER JOIN PROJUDI.ESC esc on man.ID_ESC = esc.ID_ESC";
			stSql += " WHERE man.ID_ESC = ?";
			ps.adicionarLong(id_escala);
			stSql += " ORDER BY pen.ID_PEND desc ";
			//stSql += " limit 1";
			/*
			stSql = "SELECT man.ID_USU_FINALIZADOR, esc.QUANTIDADE_MAND as QUANTIDADE_DIST";
			stSql += " FROM PROJUDI.PEND pen";
			stSql += " INNER JOIN PROJUDI.MAND_JUD man on pen.ID_PEND = man.ID_PEND";
			stSql += " INNER JOIN PROJUDI.ESC esc on man.ID_ESC = esc.ID_ESC";
			stSql += " WHERE man.ID_ESC =" + id_escala;
			stSql += " ORDER BY pen.ID_PEND desc ";
			stSql += " limit 1";
			*/

			rs1 = consultar(stSql, ps);

			if (rs1.next()) {
				//inUltimo[0] = rs1.getInt("ID_USU_FINALIZADOR");
				inUltimo[0] = rs1.getInt("ID_USU_SERV_1");
				inQuantidadeDistribuicao = rs1.getInt("QUANTIDADE_DIST");
			}
			//rs1.close();
			
			// Limpa os parâmetros do PreparedStatemant
			ps.limpar();

			// Conta quantas pendencias foram para o UsuarioServentia que
			// recebeu o ultimo processo
			stSql = "SELECT COUNT(*) as QUANTIDADE FROM (";
			stSql += " SELECT manjud.ID_USU_SERV_1";
			stSql += " FROM PROJUDI.MAND_JUD manjud INNER JOIN PROJUDI.PEND pen on pen.ID_PEND= manjud.ID_PEND";
			stSql += " WHERE manjud.ID_ESC=?";
			ps.adicionarLong(id_escala);
			stSql += " AND manjud.ID_MAND_JUD_STATUS = ?"; //Para verificar somente os que estão distribuídos
			ps.adicionarLong(MandadoJudicialStatusDt.DISTRIBUIDO);
			stSql += " ORDER BY pen.ID_PEND desc";
			//stSql += " limit " + inQuantidadeDistribuicao;
			stSql += " ) Tabela WHERE ID_USU_SERV_1=?";
			ps.adicionarLong(inUltimo[0]);
			
			/*
			stSql = "SELECT COUNT(*) as QUANTIDADE FROM (";
			stSql += " SELECT pen.ID_USU_FINALIZADOR";
			stSql += " FROM  PROJUDI.PEND pen INNER JOIN  PROJUDI.MAND_JUD manjud on pen.ID_PEND= manjud.ID_PEND";
			stSql += " WHERE manjud.ID_ESC=" + id_escala;
			stSql += " ORDER BY pen.ID_PEND desc";
			stSql += " limit " + inQuantidadeDistribuicao;
			stSql += " ) Tabela WHERE ID_USU_FINALIZADOR=" + inUltimo[0];
			*/

			rs2 = consultar(stSql, ps);
			
			while (rs2.next() && inContador < inQuantidadeDistribuicao) {
				inUltimo[1] = rs2.getInt("QUANTIDADE");
				inContador += 1;
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return inUltimo;
	}

	/**
	 * Liberar uma pendencia reservada por um usuario
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/06/2008 14:29
	 * @param String
	 *            id
	 * @return boolean
	 */
	public boolean liberar(String id) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set ID_USU_FINALIZADOR = null, DATA_TEMP = null WHERE ID_PEND = ?";
		ps.adicionarLong(id);

		this.executarUpdateDelete(sql, ps);

		return true;
	}

	/**
	 * Consulta as pendencias do tipo conclusão que estão em aberto, e que o responsável seja o serventiaCargo passado
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param numeroProcesso, filtro por número de processo
	 * @param id_Classificador, filtro por classificador
	 * @param id_PendenciaTipo, filtro para tipo de conclusão
	 * 
	 * @author msapaula / mmgomes
	 */
	public List consultarConclusoesPendentesProjudi(UsuarioNe usuarioSessao, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehIniciada) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String idProc;
		String idProcAnt = "";
		
		String sql = " SELECT DISTINCT ";
		sql += "           pen.ID_PEND, pen.DATA_INICIO, pen.ID_PEND_TIPO, pen.PEND_TIPO, pen.ID_PROC, pen.PROC_NUMERO_COMPLETO,  ";
		sql += "           pen.ID_CLASSIFICADOR, pen.CLASSIFICADOR, pen.PRIOR_CLASSIFICADOR, pen.PROC_TIPO, pen.PEND_PRIORIDADE_CODIGO, pen.PEND_PRIOR_ORDEM, pen.PEND_TIPO_CODIGO, pen.ID_AREA, pen.AREA, pen.CODIGO_TEMP_PEND ";
		if(ehVoto && !ehVotoVencido) {
			sql += " , A.VIRTUAL ";
		}
		
		//Obtendo a classe do recurso
		sql += " , (SELECT PROC_TIPO ";
		sql += " FROM PROJUDI.RECURSO R INNER JOIN PROJUDI.PROC P ON P.ID_PROC = R.ID_PROC ";
		sql += " INNER JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = R.ID_PROC_TIPO ";
		sql += " WHERE R.ID_PROC = pen.ID_PROC ";
		sql += " AND R.ID_SERV_RECURSO = P.ID_SERV ";
		sql += " AND DATA_RETORNO IS NULL ";
		sql += " AND ROWNUM = 1) AS PROC_TIPO_RECURSO ";

		
		if (ehVoto && !ehVotoVencido){
			sql += "          ,AP.ID_PROC_TIPO as ID_PROC_TIPO_SESSAO , ";
			sql += "           PT.PROC_TIPO as PROC_TIPO_SESSAO, ";
			sql += " (SELECT COUNT(*) FROM PROJUDI.VIEW_PEND PD WHERE PD.ID_PROC = pen.ID_PROC AND PD.PEND_TIPO_CODIGO = ?) AS QUANT_ADIADO "; ps.adicionarLong(PendenciaTipoDt.ADIAR_JULGAMENTO);
			
		}
		if(ehVoto) {
	        sql += ",			(" + 
	        		"	SELECT DISTINCT" + 
	        		"		PROC_TIPO" + 
	        		"	FROM" + 
	        		"		PROJUDI.PROC_TIPO PT" + 
	        		"	JOIN RECURSO_SECUNDARIO_PARTE RCP ON RCP.ID_PROC_TIPO_RECURSO_SEC = PT.ID_PROC_TIPO" + 
	        		"	JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = RCP.ID_AUDI_PROC" + 
	        		"	WHERE" + 
	        		"		AP.DATA_MOVI IS NULL" + 
	        		"		AND AP.ID_PROC = pen.ID_PROC"
	        		+ " AND ROWNUM = 1) AS PROC_TIPO_RECURSO_SECUNDARIO";
		}
		sql += " FROM ";
		sql += "   PROJUDI.VIEW_BUSCA_CONCLUSOES_PEND pen  ";
		if (ehVoto && !ehVotoVencido){
			sql += "   INNER JOIN PROJUDI.AUDI_PROC AP on AP.ID_PEND_VOTO = pen.ID_PEND OR AP.ID_PEND_EMENTA = pen.ID_PEND OR AP.ID_PEND_VOTO_REDATOR = pen.ID_PEND OR AP.ID_PEND_EMENTA_REDATOR = pen.ID_PEND ";
			sql += "   INNER JOIN PROJUDI.PROC_TIPO PT ON (AP.ID_PROC_TIPO = PT.ID_PROC_TIPO)  ";
			sql += "   INNER JOIN AUDI A ON A.ID_AUDI = AP.ID_AUDI ";	
		}
		
		sql += " WHERE pen.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);
		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sql += " AND pen.ID_CLASSIFICADOR = ?";
			ps.adicionarLong(id_Classificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){			
			sql += " AND pen.ID_PEND_TIPO = ?";
			ps.adicionarLong(id_PendenciaTipo);			
			if (ehVoto){
				if(ehVotoVencido){
					sql += " AND pen.CODIGO_TEMP_PEND = ?";	ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					
				} else {
					sql += " AND (pen.CODIGO_TEMP_PEND IS NULL OR pen.CODIGO_TEMP_PEND <> ?)";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					sql += " AND NOT EXISTS (SELECT 1";        
			        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
			        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
			        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
			        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
			        sql += "                   AND ((aci.ID_PEND_VOTO IS NOT NULL AND aci.ID_PEND_VOTO = pen.ID_PEND) OR (aci.ID_PEND_VOTO_REDATOR IS NOT NULL AND aci.ID_PEND_VOTO_REDATOR = pen.ID_PEND))";
			        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE && usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO ) {
		        			sql += "           AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
			        		ps.adicionarLong(id_ServentiaCargo);
			        		ps.adicionarLong(id_ServentiaCargo);	
		        		} else {
		        			sql += " AND EXISTS (SELECT 1";
		        	        sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
		        	        sql += "          WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR) ";
		        	        sql += "           AND pri.ID_SERV_CARGO = ? ";
		        	        ps.adicionarLong(id_ServentiaCargo);     
		        	        sql += "           AND pi.ID_PROC = aci.ID_PROC ";
		        	        sql += "         ) ";			        		
		        		}
			        }        
		        	sql += "                   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
		        	ps.adicionarLong(0);
		        	sql += " AND aci.ID_AUDI_PROC = AP.ID_AUDI_PROC";        	
		        	sql += " )";
					//lrcampos 08/08/2019 * Apresentar somente as audiProc com status a Realizar.
					sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
				}
				
				sql += " AND EXISTS (  SELECT 1 "
						+ " FROM AUDI_PROC AP1 JOIN AUDI AD1 ON AD1.ID_AUDI = AP1.ID_AUDI "
						+ " WHERE AP1.ID_PROC = PEN.ID_PROC " 
				        + " AND AD1.SESSAO_INICIADA "  + (ehIniciada ? " = 1 " : " IS NULL ")
				        + " AND AD1.VIRTUAL "  + (ehIniciada ? " = 1 " : " IS NULL ");
				if(ehVotoVencido){
					sql += " AND AP1.ID_SERV_CARGO_REDATOR IS NOT NULL ";
					sql += " AND AP1.ID_SERV_CARGO <> AP1.ID_SERV_CARGO_REDATOR ";
				} else {
					sql += " AND AP1.DATA_MOVI IS NULL ";
					sql += " AND AP1.ID_AUDI_PROC_STATUS = 1 ";
					sql += " AND AP1.ID_PEND_VOTO = PEN.ID_PEND ";	
				}
				sql += " )";
			}
		}
		
		//Teve que agrupar temporariamente pois havia conclusão pendente com mais de um responsável em virtude da importação
		//sql += " group by pen.ID_PEND"; Foi comentado devido a migração do oracle, e em virtude do comentário acima.
		sql += " ORDER BY pen.ID_AREA, pen.PEND_TIPO, pen.PRIOR_CLASSIFICADOR desc, pen.ID_CLASSIFICADOR, pen.PEND_PRIOR_ORDEM, pen.DATA_INICIO";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setHash(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				if(ehVoto && !ehVotoVencido) {
					pendenciaDt.setId_ProcessoTipoSessao(rs1.getString("ID_PROC_TIPO_SESSAO"));
				}
				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
                idProc = processoDt.getId_Processo();
                if (idProc.equals(idProcAnt))
                	continue;
                idProcAnt = idProc;
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				String classificador = "";
				if (!rs1.isNull("CLASSIFICADOR")) {
					classificador = rs1.getString("CLASSIFICADOR");
				}
				if (!rs1.isNull("PRIOR_CLASSIFICADOR")) {
					if (classificador.length() > 0) classificador += " - ";
					classificador += "(Prioridade: " + rs1.getString("PRIOR_CLASSIFICADOR") + ")";
				}
				processoDt.setClassificador(classificador);
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				if (!rs1.isNull("PROC_TIPO_RECURSO") && rs1.getString("PROC_TIPO_RECURSO").trim().length() > 0) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_RECURSO"));	
				}
				processoDt.setId_Area(rs1.getString("ID_AREA"));
				processoDt.setArea(rs1.getString("AREA"));
				pendenciaDt.setProcessoDt(processoDt);
				pendenciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_PEND"));
				
				
				if (ehVoto && !ehVotoVencido){
					pendenciaDt.setProcessoTipoSessao(rs1.getString("PROC_TIPO_SESSAO"));
					pendenciaDt.setCodigoTemp(rs1.getString("QUANT_ADIADO"));
				}
				if(ehVoto) {
					processoDt.setRecursoDt(new RecursoDt());
					processoDt.getRecursoDt().setProcessoTipoRecursoParteAtual(rs1.getString("PROC_TIPO_RECURSO_SECUNDARIO"));
				}

				pendencias.add(pendenciaDt);
			}
				
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return pendencias;	}
	
	/**
	 * Consulta as pendencias do tipo conclusão que estão em aberto, e que o responsável seja o serventiaCargo passado
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param numeroProcesso, filtro por número de processo
	 * @param id_Classificador, filtro por classificador
	 * @param id_PendenciaTipo, filtro para tipo de conclusão
	 * 
	 * @author msapaula / mmgomes
	 */
	public List<PendenciaDt> consultarConclusoesPendentesPJD(UsuarioNe usuarioSessao, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehIniciada) throws Exception {
		List<PendenciaDt> pendencias = new ArrayList<PendenciaDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = " SELECT DISTINCT ";
		sql += "           pen.ID_PEND, pen.DATA_INICIO, pen.ID_PEND_TIPO, pen.PEND_TIPO, pen.ID_PROC, pen.PROC_NUMERO_COMPLETO,  ";
		sql += "           pen.ID_CLASSIFICADOR, pen.CLASSIFICADOR, pen.PRIOR_CLASSIFICADOR, " + ((ehVoto && !ehVotoVencido) ? "PT" : "pen") + ".PROC_TIPO, pen.PEND_PRIORIDADE_CODIGO, pen.PEND_PRIOR_ORDEM, pen.PEND_TIPO_CODIGO, pen.ID_AREA, pen.AREA, pen.CODIGO_TEMP_PEND "; // jvosantos - 18/02/2020 13:43 - Correção
		if(ehVoto && !ehVotoVencido) {
			sql += " , A.VIRTUAL ";

			// jvosantos - 18/02/2020 13:43 - Correção
			sql += " , (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ";
			sql += "	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "; 
			sql += "	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ";
			sql += " 	WHERE AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "; 
			sql += " 	(SELECT 1 FROM RECURSO R "; 
			sql += "		INNER JOIN AUDI_PROC AP2 ON AP2.ID_PROC = R.ID_PROC "; 
			sql += "		WHERE AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO, ";
			sql += " (SELECT COUNT(*) FROM PROJUDI.VIEW_PEND PD WHERE PD.ID_PROC = pen.ID_PROC AND PD.PEND_TIPO_CODIGO = ?) AS QUANT_ADIADO "; ps.adicionarLong(PendenciaTipoDt.ADIAR_JULGAMENTO);
		} else {
			sql += " , (SELECT PROC_TIPO ";
			sql += " FROM PROJUDI.RECURSO R INNER JOIN PROJUDI.PROC P ON P.ID_PROC = R.ID_PROC ";
			sql += " INNER JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = R.ID_PROC_TIPO ";
			sql += " WHERE R.ID_PROC = pen.ID_PROC ";
			sql += " AND R.ID_SERV_RECURSO = P.ID_SERV ";
			sql += " AND DATA_RETORNO IS NULL ";
			sql += " AND ROWNUM = 1) AS PROC_TIPO_RECURSO ";
		}
		sql += " FROM ";
		sql += "   PROJUDI.VIEW_BUSCA_CONCLUSOES_PEND pen  ";
		if (ehVoto && !ehVotoVencido){
			sql += "   INNER JOIN PROJUDI.AUDI_PROC AP on AP.ID_PEND_VOTO = pen.ID_PEND OR AP.ID_PEND_EMENTA = pen.ID_PEND OR AP.ID_PEND_VOTO_REDATOR = pen.ID_PEND OR AP.ID_PEND_EMENTA_REDATOR = pen.ID_PEND ";
			sql += "   INNER JOIN PROJUDI.PROC_TIPO PT ON (AP.ID_PROC_TIPO = PT.ID_PROC_TIPO)  ";
			sql += "   INNER JOIN AUDI A ON A.ID_AUDI = AP.ID_AUDI ";	
		}
		
		sql += " WHERE pen.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);
		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sql += " AND pen.ID_CLASSIFICADOR = ?";
			ps.adicionarLong(id_Classificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){			
			sql += " AND pen.ID_PEND_TIPO = ?";
			ps.adicionarLong(id_PendenciaTipo);			
			if (ehVoto){
				if(ehVotoVencido){
					sql += " AND pen.CODIGO_TEMP_PEND = ?";	ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					
				} else {
					sql += " AND (pen.CODIGO_TEMP_PEND IS NULL OR pen.CODIGO_TEMP_PEND <> ?)";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					sql += " AND NOT EXISTS (SELECT 1";        
			        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
			        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
			        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
			        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
			        sql += "                   AND ((aci.ID_PEND_VOTO IS NOT NULL AND aci.ID_PEND_VOTO = pen.ID_PEND) OR (aci.ID_PEND_VOTO_REDATOR IS NOT NULL AND aci.ID_PEND_VOTO_REDATOR = pen.ID_PEND))";
			        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE && usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO ) {
		        			sql += "           AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
			        		ps.adicionarLong(id_ServentiaCargo);
			        		ps.adicionarLong(id_ServentiaCargo);	
		        		} else {
		        			sql += " AND EXISTS (SELECT 1";
		        	        sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
		        	        sql += "          WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR) ";
		        	        sql += "           AND pri.ID_SERV_CARGO = ? ";
		        	        ps.adicionarLong(id_ServentiaCargo);       
		        	        sql += "           AND pi.ID_PROC = aci.ID_PROC ";
		        	        sql += "         ) ";			        		
		        		}
			        }        
		        	sql += "                   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
		        	ps.adicionarLong(0);
		        	sql += " AND aci.ID_AUDI_PROC = AP.ID_AUDI_PROC";        	
		        	sql += " )";
					//lrcampos 08/08/2019 * Apresentar somente as audiProc com status a Realizar.
					sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
				}
				
				sql += " AND EXISTS (  SELECT 1 "
						+ " FROM AUDI_PROC AP1 JOIN AUDI AD1 ON AD1.ID_AUDI = AP1.ID_AUDI "
						+ " WHERE AP1.ID_PROC = PEN.ID_PROC " 
				        + " AND AD1.SESSAO_INICIADA "  + (ehIniciada ? " = 1 " : " IS NULL ")
				        + " AND AD1.VIRTUAL "  + (ehIniciada ? " = 1 " : " IS NULL ");
				if(ehVotoVencido){
					sql += " AND AP1.ID_SERV_CARGO_REDATOR IS NOT NULL ";
					sql += " AND AP1.ID_SERV_CARGO <> AP1.ID_SERV_CARGO_REDATOR ";
				} else {
					sql += " AND AP1.DATA_MOVI IS NULL ";
					sql += " AND AP1.ID_AUDI_PROC_STATUS = 1 ";
					sql += " AND (AP1.ID_PEND_VOTO = PEN.ID_PEND OR AP1.ID_PEND_VOTO_REDATOR = PEN.ID_PEND) ";	
				}
				sql += " )";
			}
		}
		
		//Teve que agrupar temporariamente pois havia conclusão pendente com mais de um responsável em virtude da importação
		//sql += " group by pen.ID_PEND"; Foi comentado devido a migração do oracle, e em virtude do comentário acima.
		sql += " ORDER BY pen.ID_AREA, pen.PEND_TIPO, pen.PRIOR_CLASSIFICADOR desc, pen.ID_CLASSIFICADOR, pen.PEND_PRIOR_ORDEM, pen.DATA_INICIO";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setHash(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));

				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR") + " - (Prioridade: " + rs1.getString("PRIOR_CLASSIFICADOR") + ")");
				
				// jvosantos - 18/02/2020 13:43 - Correção
				if(ehVoto && !ehVotoVencido) {
					//lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sessão possui recurso secundario. 
					if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
						processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
					} else if (rs1.getString("POSSUI_RECURSO") == null) {
						processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
					} else {
						processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
					}
				}else {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				}
				processoDt.setId_Area(rs1.getString("ID_AREA"));
				processoDt.setArea(rs1.getString("AREA"));
				pendenciaDt.setProcessoDt(processoDt);
				pendenciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_PEND"));
				

				pendencias.add(pendenciaDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return pendencias;
	}
	
	
	/**
	 * Consulta as pendencias do tipo conclusão que estão em aberto na sessão virtual, e que o responsável seja o serventiaCargo passado
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param numeroProcesso, filtro por número de processo
	 * @param id_Classificador, filtro por classificador
	 * @param id_PendenciaTipo, filtro para tipo de conclusão
	 * @since 14/10/2019 15:56
	 * @author lrcampos
	 */
	public List consultarConclusoesPendentesVirtual(UsuarioDt usuarioDt, String idServentia, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehIniciada) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		// lrcampos 14/10/2019 15:57 mudança do sql para o mesmo da listagem de sessões não analisadas

		String sql = " SELECT DISTINCT ";
		sql += "           pen.ID_PEND, pen.DATA_INICIO, pen.ID_PEND_TIPO, pen.PEND_TIPO, pen.ID_PROC, pen.PROC_NUMERO_COMPLETO, A.ID_SERV, SS.SERV, ";
		sql += "           pen.ID_CLASSIFICADOR, pen.CLASSIFICADOR, pen.PRIOR_CLASSIFICADOR, PT.PROC_TIPO, pen.PEND_PRIORIDADE_CODIGO, pen.PEND_PRIOR_ORDEM, pen.PEND_TIPO_CODIGO, pen.ID_AREA, pen.AREA, pen.CODIGO_TEMP_PEND ";
		if(ehVoto && !ehVotoVencido) {
			sql += " , A.VIRTUAL ";
		}
		
		//Obtendo a classe do recurso
		sql += ", (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ";
		sql += "	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "; 
		sql += "	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ";
		sql += " 	WHERE AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "; 
		sql += " 	(SELECT 1 FROM RECURSO R "; 
		sql += "		INNER JOIN AUDI_PROC AP1 ON AP1.ID_PROC = R.ID_PROC "; 
		sql += "		WHERE AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1) POSSUI_RECURSO ";

		
		if (ehVoto && !ehVotoVencido){
			sql += "          ,AP.ID_PROC_TIPO as ID_PROC_TIPO_SESSAO , ";
			sql += "           PT.PROC_TIPO as PROC_TIPO_SESSAO, ";
			sql += " (SELECT COUNT(*) FROM PROJUDI.VIEW_PEND PD INNER JOIN AUDI_PROC_PEND APP1 ON APP1.ID_PEND = PD.ID_PEND "
					+ " WHERE APP1.ID_AUDI_PROC = AP.ID_AUDI_PROC AND PD.PEND_TIPO_CODIGO = ?) AS QUANT_ADIADO "; ps.adicionarLong(PendenciaTipoDt.ADIAR_JULGAMENTO);
			
		}
		if(ehVoto) {
	        sql += ",			(" + 
	        		"	SELECT DISTINCT" + 
	        		"		PROC_TIPO" + 
	        		"	FROM" + 
	        		"		PROJUDI.PROC_TIPO PT" + 
	        		"	JOIN RECURSO_SECUNDARIO_PARTE RCP ON RCP.ID_PROC_TIPO_RECURSO_SEC = PT.ID_PROC_TIPO" + 
	        		"	JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = RCP.ID_AUDI_PROC" + 
	        		"	WHERE" + 
	        		"		AP.DATA_MOVI IS NULL" + 
	        		"		AND AP.ID_PROC = pen.ID_PROC"
	        		+ " AND ROWNUM = 1) AS PROC_TIPO_RECURSO_SECUNDARIO";
		}
		sql += " FROM ";
		sql += "   PROJUDI.VIEW_BUSCA_CONCLUSOES_PEND pen  ";
		if (ehVoto && !ehVotoVencido){
			//lrcampos 05/03/2020 11:47 - Melhoria de performace retirando exists e colocando as condições no JOIN
			sql += "   INNER JOIN PROJUDI.AUDI_PROC AP on (AP.ID_PEND_VOTO = pen.ID_PEND OR AP.ID_PEND_EMENTA = pen.ID_PEND OR AP.ID_PEND_VOTO_REDATOR = pen.ID_PEND OR AP.ID_PEND_EMENTA_REDATOR = pen.ID_PEND) AND AP.DATA_MOVI IS NULL ";
			sql += "   INNER JOIN PROJUDI.PROC_TIPO PT ON (AP.ID_PROC_TIPO = PT.ID_PROC_TIPO)  ";
			sql += "   INNER JOIN AUDI A ON ( A.ID_AUDI = AP.ID_AUDI AND A.VIRTUAL  = 1 AND A.SESSAO_INICIADA " + (ehIniciada  ? " = 1" : "IS NULL") + ")";
			sql += "   INNER JOIN SERV SS ON SS.ID_SERV = A.ID_SERV ";
		}
		
		sql += " WHERE pen.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);
		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sql += " AND pen.ID_CLASSIFICADOR = ?";
			ps.adicionarLong(id_Classificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){			
			sql += " AND pen.ID_PEND_TIPO = ?";
			ps.adicionarLong(id_PendenciaTipo);			
			if (ehVoto){
				if(ehVotoVencido){
					sql += " AND pen.CODIGO_TEMP_PEND = ?";	ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					
				} else {
					sql += " AND (pen.CODIGO_TEMP_PEND IS NULL OR pen.CODIGO_TEMP_PEND <> ?)";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					sql += " AND NOT EXISTS (SELECT 1";        
			        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
			        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
			        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
			        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
			        sql += "                   AND ((aci.ID_PEND_VOTO IS NOT NULL AND aci.ID_PEND_VOTO = pen.ID_PEND) OR (aci.ID_PEND_EMENTA IS NOT NULL AND aci.ID_PEND_EMENTA = pen.ID_PEND))";
			        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        		if (usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE && usuarioDt.getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO ) {
		        			sql += "           AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
			        		ps.adicionarLong(id_ServentiaCargo);
			        		ps.adicionarLong(id_ServentiaCargo);	
		        		} else {
		        			sql += " AND EXISTS (SELECT 1";
		        	        sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
		        	        sql += "          WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR) ";
		        	        sql += "           AND pri.ID_SERV_CARGO = ? ";
		        	        ps.adicionarLong(id_ServentiaCargo);       
		        	        sql += "           AND pi.ID_PROC = aci.ID_PROC ";
		        	        sql += "         ) ";			        		
		        		}
			        }        
		        	sql += "                   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
		        	ps.adicionarLong(0);
		        	sql += " AND aci.ID_PROC = pen.ID_PROC";        	
		        	sql += " )";
					//lrcampos 08/08/2019 * Apresentar somente as audiProc com status a Realizar.
					sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
				}
				sql += " AND A.VIRTUAL IS NOT NULL ";
				//lrcampos 19/03/2020 15:38 - Incluindo filtro de serventia
				if(StringUtils.isNotEmpty(idServentia)) {
					sql += " AND A.ID_SERV = ? "; ps.adicionarLong(idServentia);					
				}
			}
		}
		
		//Teve que agrupar temporariamente pois havia conclusão pendente com mais de um responsável em virtude da importação
		//sql += " group by pen.ID_PEND"; Foi comentado devido a migração do oracle, e em virtude do comentário acima.
		sql += " ORDER BY pen.ID_AREA, pen.PEND_TIPO, pen.PRIOR_CLASSIFICADOR desc, pen.ID_CLASSIFICADOR, pen.PEND_PRIOR_ORDEM, pen.DATA_INICIO";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				if(ehVoto && !ehVotoVencido) {
					pendenciaDt.setId_ProcessoTipoSessao(rs1.getString("ID_PROC_TIPO_SESSAO"));
				}
				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				processoDt.setId_Area(rs1.getString("ID_AREA"));
				processoDt.setArea(rs1.getString("AREA"));
				pendenciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_PEND"));
				processoDt.setCodigoTemp(rs1.getString("ID_SERV")+"@"+rs1.getString("SERV"));
				
				if (ehVoto && !ehVotoVencido){
					pendenciaDt.setCodigoTemp(rs1.getString("QUANT_ADIADO"));
					pendenciaDt.setValor(rs1.getString("VIRTUAL"));
				}
				//lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sessão possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
				}
				pendenciaDt.setProcessoDt(processoDt);

				pendencias.add(pendenciaDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return pendencias;
	}
	
	/**
	 * Consulta as pendencias do tipo conclusão que estão em aberto, e que o responsável seja o serventiaCargo passado
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param numeroProcesso, filtro por número de processo
	 * @param id_Classificador, filtro por classificador
	 * @param id_PendenciaTipo, filtro para tipo de conclusão
	 * 
	 * @author lsbernardes
	 */
	public List consultarConclusoesPendentesAssistenteGabinete(UsuarioNe usuarioSessao, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, String id_ServentiaGrupo, boolean ehVoto, boolean ehVotoVencido) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT pen.ID_PEND, pen.DATA_INICIO, pen.ID_PEND_TIPO, pen.PEND_TIPO, pen.ID_PROC, pen.PROC_NUMERO_COMPLETO, ";
		sql += "              pen.ID_CLASSIFICADOR, pen.CLASSIFICADOR, pen.PROC_TIPO, pen.PEND_PRIORIDADE_CODIGO, pen.PEND_PRIOR_ORDEM, pen.PEND_TIPO_CODIGO, ";
		sql += "		      pen.ID_AREA, pen.AREA, pen.CODIGO_TEMP_PEND, sg.id_serv_grupo,  sg.atividade ";
		sql += "       FROM PROJUDI.VIEW_BUSCA_CONCLUSOES_PEND pen ";
		sql += "       INNER JOIN PROJUDI.pend_resp_hist prh on pen.id_pend = prh.id_pend and prh.data_fim is null "; 
		sql += "       INNER JOIN PROJUDI.serv_grupo sg on prh.id_serv_grupo = sg.id_serv_grupo  ";
		sql += " WHERE pen.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sql += " AND pen.ID_CLASSIFICADOR = ?";
			ps.adicionarLong(id_Classificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){		
			sql += " AND pen.ID_PEND_TIPO = ?";
			ps.adicionarLong(id_PendenciaTipo);			
			if (ehVoto){
				if(ehVotoVencido){
					sql += " AND pen.CODIGO_TEMP_PEND = ?";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
				} else {
					sql += " AND (pen.CODIGO_TEMP_PEND IS NULL OR pen.CODIGO_TEMP_PEND <> ?)";
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
		        	sql += " AND aci.ID_PROC = pen.ID_PROC";        	
		        	sql += " )";
				}
			}		
		}
		
		if (id_ServentiaGrupo != null && id_ServentiaGrupo.length()>0){
			sql += " AND prh.id_serv_grupo = ?";
			ps.adicionarLong(id_ServentiaGrupo);	
		}
		
		sql += " ORDER BY pen.ID_AREA, pen.PEND_TIPO, pen.PRIOR_CLASSIFICADOR desc, pen.ID_CLASSIFICADOR, pen.PEND_PRIOR_ORDEM, pen.DATA_INICIO, sg.id_serv_grupo,  sg.atividade";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO")+" - "+rs1.getString("atividade"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setHash(usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")));
				
				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				processoDt.setId_Area(rs1.getString("ID_AREA"));
				processoDt.setArea(rs1.getString("AREA"));
				pendenciaDt.setProcessoDt(processoDt);
				pendenciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_PEND"));

				pendencias.add(pendenciaDt);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return pendencias;
	}
	
	/**
	 * Consulta as pendencias que estão em aberto, e que o responsável seja o serventiaCargo passado.
	 * Essas pendências são aquelas que tem o mesmo tratamento de conclusões.
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param numeroProcesso, filtro por número de processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param id_PendenciaTipo, filtro para tipo de pendência
	 * 
	 * @author msapaula
	 */
	public List consultarPendenciasNaoAnalisadas(String id_ServentiaCargo, String id_UsuarioServentia, String numeroProcesso, String digitoVerificador, String id_PendenciaTipo) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT ID_PEND, DATA_INICIO, ID_PEND_TIPO, PEND_TIPO,ID_PROC, PROC_NUMERO_COMPLETO, ";
		sql += "ID_CLASSIFICADOR, CLASSIFICADOR, PROC_TIPO, PEND_PRIORIDADE_CODIGO , PEND_PRIOR_ORDEM, PRIOR_CLASSIFICADOR, PEN.DATA_INICIO " +
				"FROM PROJUDI.VIEW_PEND_NAO_ANALISADAS pen ";
		if (id_ServentiaCargo != null){
			sql += " WHERE pen.ID_SERV_CARGO = ?";
			ps.adicionarLong(id_ServentiaCargo);
		} else if (id_UsuarioServentia != null) {
			sql += " WHERE pen.ID_USU_RESP = ?";
			ps.adicionarLong(id_UsuarioServentia);
		}		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pen.ID_PEND_TIPO = ?";
			ps.adicionarLong(id_PendenciaTipo);
		}
		//Teve que agrupar temporariamente pois há pendências com mais de um responsável
		sql += " GROUP BY pen.ID_PEND, DATA_INICIO, ID_PEND_TIPO, PEND_TIPO,ID_PROC, PROC_NUMERO_COMPLETO, ID_CLASSIFICADOR, CLASSIFICADOR, PROC_TIPO, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM, PRIOR_CLASSIFICADOR, PEN.DATA_INICIO ";
		sql += " ORDER BY pen.PEND_TIPO, pen.PRIOR_CLASSIFICADOR desc, pen.ID_CLASSIFICADOR, pen.PEND_PRIOR_ORDEM, pen.DATA_INICIO";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				
				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);

				pendencias.add(pendenciaDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return pendencias;
	}
	
	/**
	 * Consulta as pendencias que estão em aberto, e que o responsável seja o serventiaCargo passado.
	 * Essas pendências são aquelas que tem o mesmo tratamento de conclusões.
	 * 
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param numeroProcesso, filtro por número de processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param id_PendenciaTipo, filtro para tipo de pendência
	 * 
	 * @author lsbernardes
	 */
	public List consultarPendenciasNaoAnalisadas(String id_UsuarioServentia, String numeroProcesso, String digitoVerificador, String id_PendenciaTipo) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT ID_PEND, DATA_INICIO, ID_PEND_TIPO, PEND_TIPO,ID_PROC, PROC_NUMERO_COMPLETO, ";
		sql += "ID_CLASSIFICADOR_PEND, CLASSIFICADOR_PEND, PROC_TIPO, PEND_PRIORIDADE_CODIGO , PEND_PRIOR_ORDEM, PRIOR_CLASSIFICADOR_PEND, PEN.DATA_INICIO " +
				"FROM PROJUDI.VIEW_PEND_NAO_ANALISADAS pen  WHERE pen.ID_USU_RESP = ? ";ps.adicionarLong(id_UsuarioServentia);
		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pen.ID_PEND_TIPO = ?";
			ps.adicionarLong(id_PendenciaTipo);
		}
		sql += " GROUP BY pen.ID_PEND, DATA_INICIO, ID_PEND_TIPO, PEND_TIPO,ID_PROC, PROC_NUMERO_COMPLETO, ID_CLASSIFICADOR_PEND, CLASSIFICADOR_PEND, PROC_TIPO, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM, PRIOR_CLASSIFICADOR_PEND, PEN.DATA_INICIO ";
		sql += " ORDER BY pen.PEND_TIPO, pen.PRIOR_CLASSIFICADOR_PEND desc, pen.ID_CLASSIFICADOR_PEND, pen.PEND_PRIOR_ORDEM, pen.DATA_INICIO";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR_PEND"));
				pendenciaDt.setClassificador(rs1.getString("CLASSIFICADOR_PEND"));
				
				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);

				pendencias.add(pendenciaDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return pendencias;
	}
	
	/**
	 * Consulta as pendencias que estão em aberto, e que o responsável seja o serventiaCargo passado.
	 * Essas pendências são aquelas que tem o mesmo tratamento de conclusões.
	 * 
	 * @param id_ServentiaCargo, identificação do cargo de um usuário da serventia
	 * @param numeroProcesso, filtro por número de processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @param id_PendenciaTipo, filtro para tipo de pendência
	 * 
	 * @author lsbernardes
	 */
	public List consultarPendenciasNaoAnalisadasCargo(String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_PendenciaTipo) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT ID_PEND, DATA_INICIO, ID_PEND_TIPO, PEND_TIPO,ID_PROC, PROC_NUMERO_COMPLETO, ";
		sql += "ID_CLASSIFICADOR_PEND, CLASSIFICADOR_PEND, PROC_TIPO, PEND_PRIORIDADE_CODIGO , PEND_PRIOR_ORDEM, PRIOR_CLASSIFICADOR_PEND, PEN.DATA_INICIO " +
				"FROM PROJUDI.VIEW_PEND_NAO_ANALISADAS pen WHERE pen.ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargo);
		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pen.ID_PEND_TIPO = ?";
			ps.adicionarLong(id_PendenciaTipo);
		}
		sql += " GROUP BY pen.ID_PEND, DATA_INICIO, ID_PEND_TIPO, PEND_TIPO,ID_PROC, PROC_NUMERO_COMPLETO, ID_CLASSIFICADOR_PEND, CLASSIFICADOR_PEND, PROC_TIPO, PEND_PRIORIDADE_CODIGO, PEND_PRIOR_ORDEM, PRIOR_CLASSIFICADOR_PEND, PEN.DATA_INICIO ";
		sql += " ORDER BY pen.PEND_TIPO, pen.PRIOR_CLASSIFICADOR_PEND desc, pen.ID_CLASSIFICADOR_PEND, pen.PEND_PRIOR_ORDEM, pen.DATA_INICIO";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				pendenciaDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR_PEND"));
				pendenciaDt.setClassificador(rs1.getString("CLASSIFICADOR_PEND"));
				
				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);

				pendencias.add(pendenciaDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return pendencias;
	}

	/**
	 * Reserva a ultima pendencia para o usuario responsavel
	 * 
	 * @author Ronneesely Moura Teles
	 * @since 29/10/2008 14:09
	 * @param String
	 *            tipo, tipo da pendencia
	 * @param String
	 *            idUsuarioServentia, id o usuario serventia
	 * @return PendenciaDt
	 * @throws Exception
	 */
	/*
	public PendenciaDt reservarUltimaUsuarioResponsavel(String tipo, String idUsuarioServentia){
		String temp = String.valueOf(Math.round(Math.random() * 100000));

		// Atualiza registro nas condicoes especificadas
		String sqlUp = "UPDATE PROJUDI.Pendencia set PEND.CODIGO_TEMP = " + temp + ", PEND.DATA_TEMP = " + Funcoes.BancoDataHora(new Date()) + " WHERE PEND.ID_PEND = ( " + " SELECT up.ID_PEND FROM PROJUDI.VIEW_PENDUltimaUsuarioResponsavel up WHERE up.ID_PEND_TIPO = " + tipo + " AND up.ID_USU_RESP = " + idUsuarioServentia + " )";

			this.executar(sqlUp);

		return this.consultarLimparTemp(temp);
	}
	 */

	/**
	 * Consulta uma pendencia pelo seu codigo temp e o limpa
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 29/10/2008 15:31
	 * @param String
	 *            temp, codigo temporario
	 * @return PendenciaDt
	 * @throws Exception
	 */
	private PendenciaDt consultarLimparTemp(String temp) throws Exception{
		PendenciaDt pendenciaDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		// Fechar os campos utilizados
		String sql = "SELECT * FROM VIEW_PEND WHERE CODIGO_TEMP = ?";
		ps.adicionarLong(temp);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				pendenciaDt = new PendenciaDt();

				this.associarDt(pendenciaDt, rs1);

				// Limpar parâmetros
				ps.limpar();
				String sqlUpTemp = "UPDATE PROJUDI.PEND SET CODIGO_TEMP = null WHERE ID_PEND = ?";
				ps.adicionarLong(pendenciaDt.getId());

				this.executarUpdateDelete(sqlUpTemp, ps);

			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendenciaDt;
	}

	/**
	 * Reserva a ultima pendencia para um serventia cargo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 29/10/2008 15:35
	 * @param String
	 *            tipo, tipo da pendencia
	 * @param String
	 *            idServentiaCargo, serventia cargo
	 * @return PendenciaDt
	 * @throws Exception
	 */
	public PendenciaDt reservarUltimaServentiaCargo(String tipo, String idServentiaCargo) throws Exception {
		String temp = String.valueOf(Math.round(Math.random() * 100000))+idServentiaCargo;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		// Atualiza registro nas condicoes especificadas
		//String sqlUp = "UPDATE PROJUDI.Pendencia set PEND.CODIGO_TEMP = " + temp + ", PEND.DATA_TEMP = " + Funcoes.BancoDataHora(new Date()) + " WHERE PEND.ID_PEND = ( " + " SELECT up.ID_PEND FROM PROJUDI.VIEW_PEND_ULTIMA_SERV_CARGO up WHERE up.ID_PEND_TIPO = " + tipo + " AND up.ID_SERV_CARGO = " + idServentiaCargo + " )";
//		String sqlUp =  "UPDATE PROJUDI.PEND inner joIN ( SELECT ID_PEND";
//		sqlUp +=			 " FROM  PROJUDI.VIEW_PEND_ULTIMA_SERV_CARGO ";
//		sqlUp +=             " WHERE ID_PEND_TIPO = ? AND ID_SERV_CARGO = ? group by ID_PEND_TIPO,ID_SERV_CARGO ";
//		ps.adicionarLong(tipo);
//		ps.adicionarLong(idServentiaCargo);
//		sqlUp +=         "  ) PEND_ULTIMA_SERV_CARGO on PROJUDI.PEND.ID_PEND =  PEND_ULTIMA_SERV_CARGO.ID_PEND ";
//		sqlUp +=	     " set PEND.CODIGO_TEMP = ? , PEND.DATA_TEMP = ?";
//		ps.adicionarLong(temp);
//		ps.adicionarDateTime(new Date());
		
		String sqlUp =  "UPDATE PROJUDI.PEND  p set p.CODIGO_TEMP = ? , p.DATA_TEMP = ? ";
		ps.adicionarLong(temp);
		ps.adicionarDateTime(new Date());
		sqlUp +=             " WHERE  p.ID_PEND = (  SELECT min(ID_PEND) as ID_PEND ";
		sqlUp +=             " FROM  PROJUDI.VIEW_PEND_ULTIMA_SERV_CARGO ";  
		sqlUp +=             " WHERE  ID_PEND_TIPO = ? AND  ID_SERV_CARGO = ?  ";
		sqlUp +=             " group by ID_PEND_TIPO,ID_SERV_CARGO )  ";
		ps.adicionarLong(tipo);
		ps.adicionarLong(idServentiaCargo);

		this.executarUpdateDelete(sqlUp,  ps);

		return this.consultarLimparTemp(temp);
	}

	/**
	 * Reserva a ultima pendencia de uma serventia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 29/10/2008 15:39
	 * @param String
	 *            tipo, tipo de pendencia
	 * @param String
	 *            idServentia, id da serventia
	 * @return PendenciaDt
	 * @throws Exception
	 */
	public PendenciaDt reservarUltimaServentia(String tipo, String idServentia) throws Exception {
		String temp = String.valueOf(Math.round(Math.random() * 100000))+idServentia;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		// Atualiza registro nas condicoes especificadas
		//String sqlUp = "UPDATE PROJUDI.Pendencia set PEND.CODIGO_TEMP = " + temp + ", PEND.DATA_TEMP = " + Funcoes.BancoDataHora(new Date()) + " WHERE PEND.ID_PEND = ( " + " SELECT up.ID_PEND FROM PROJUDI.VIEW_PEND_ULTIMA_SERV up WHERE up.ID_PEND_TIPO = " + tipo + " AND up.ID_SERV = " + idServentia + " )";

//		String sqlUp =  "UPDATE PROJUDI.PEND inner joIN ( SELECT  min(ID_PEND) as ID_PEND ";
//		sqlUp +=			 " FROM  PROJUDI.VIEW_PEND_ULTIMA_SERV ";
//		sqlUp +=             " WHERE ID_PEND_TIPO = ? AND ID_SERV = ? group by ID_PEND_TIPO,ID_SERV ";
//		ps.adicionarLong(tipo);
//		ps.adicionarLong(idServentia);
//		sqlUp +=         "  ) PEND_ULT_SERV on PROJUDI.PEND.ID_PEND =  PEND_ULT_SERV.ID_PEND ";
//		sqlUp +=	     " set PEND.CODIGO_TEMP = ? , PEND.DATA_TEMP = ?";
//		ps.adicionarLong(temp);
//		ps.adicionarDateTime(new Date());
		
		String sqlUp =  "UPDATE PROJUDI.PEND  p set p.CODIGO_TEMP = ? , p.DATA_TEMP = ? ";
		ps.adicionarLong(temp);
		ps.adicionarDateTime(new Date());
		sqlUp +=             " WHERE p.ID_PEND = (  SELECT ID_PEND ";
		sqlUp +=             "                        FROM PROJUDI.VIEW_PEND_ABERTAS_PRIOR_RESP PEN  ";  
		sqlUp +=             " 	                     WHERE  PEN.ID_PEND_TIPO = ? "; ps.adicionarLong(tipo);
		//sqlUp +=             "                        AND PEN.ID_PROC IS NOT NULL ";
		sqlUp +=             "                        AND  (PEN.ID_SERV = ? "; ps.adicionarLong(idServentia);
		sqlUp +=             "                        AND PEN.ID_SERV_TIPO IS NULL ";
		sqlUp +=             "                        AND PEN.ID_USU_RESP IS NULL ";
		sqlUp +=             "                        AND PEN.ID_SERV_CARGO IS NULL ";
		sqlUp +=             "                        AND PEND_STATUS_CODIGO IN (?, ?, ?)) ";
		ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
		ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		sqlUp +=             "                        AND ROWNUM = 1 ) ";

		this.executarUpdateDelete(sqlUp, ps);

		return this.consultarLimparTemp(temp);
	}
	
	/**
	 * Reserva a ultima pendencia de uma serventia para o distribuidor câmara
	 * 
	 * @author jrcorrea
	 * @param String
	 *            tipo, tipo de pendencia
	 * @param String
	 *            idServentia, id da serventia
	 * @return PendenciaDt
	 * @throws Exception
	 */
	public PendenciaDt reservarUltimaServentiaDistribuidorCamara(String tipo, String idServentia) throws Exception {
		String temp = String.valueOf(Math.round(Math.random() * 100000))+idServentia;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlUp =  "UPDATE PROJUDI.PEND  p set p.CODIGO_TEMP = ? , p.DATA_TEMP = ? ";
		ps.adicionarLong(temp);
		ps.adicionarDateTime(new Date());
		sqlUp +=             " WHERE p.ID_PEND = (  SELECT ID_PEND ";
		sqlUp +=             "                        FROM PROJUDI.VIEW_PEND_ABERTAS_DIST_CAMARA PEN  ";  
		sqlUp +=             " 	                     WHERE  PEN.ID_PEND_TIPO = ? "; ps.adicionarLong(tipo);
		//sqlUp +=             "                        AND PEN.ID_PROC IS NOT NULL ";
		sqlUp +=             "                        AND  (PEN.ID_SERV = ? "; ps.adicionarLong(idServentia);
		sqlUp +=             "                        AND PEN.ID_SERV_TIPO IS NULL ";
		sqlUp +=             "                        AND PEN.ID_USU_RESP IS NULL ";
		sqlUp +=             "                        AND PEN.ID_SERV_CARGO IS NULL ";
		sqlUp +=             "                        AND PEND_STATUS_CODIGO IN (?, ?, ?)) ";
		ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
		ps.adicionarLong(PendenciaStatusDt.ID_CORRECAO);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		sqlUp +=             "                        AND ROWNUM = 1 ) ";

		this.executarUpdateDelete(sqlUp, ps);

		return this.consultarLimparTemp(temp);
	}
	
	
	public PendenciaDt reservarUltimaPreAnalisadaServentia(String tipo, String idServentia) throws Exception {
		String temp = String.valueOf(Math.round(Math.random() * 100000))+idServentia;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlUp =  "UPDATE PROJUDI.PEND  p set p.CODIGO_TEMP = ? , p.DATA_TEMP = ? ";
		ps.adicionarLong(temp);
		ps.adicionarDateTime(new Date());
		sqlUp +=             " WHERE p.ID_PEND = (  SELECT ID_PEND ";
		sqlUp +=             "                        FROM PROJUDI.VIEW_PEND_ABERTAS_PRIOR_RESP PEN  ";  
		sqlUp +=             " 	                     WHERE  PEN.ID_PEND_TIPO = ? "; ps.adicionarLong(tipo);
		//sqlUp +=             "                        AND PEN.ID_PROC IS NOT NULL ";
		sqlUp +=             "                        AND  (PEN.ID_SERV = ? "; ps.adicionarLong(idServentia);
		sqlUp +=             "                        AND PEN.ID_SERV_TIPO IS NULL ";
		sqlUp +=             "                        AND PEN.ID_USU_RESP IS NULL ";
		sqlUp +=             "                        AND PEN.ID_SERV_CARGO IS NULL ";
		sqlUp +=             "                        AND PEND_STATUS_CODIGO IN (?)) ";
		ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);
		sqlUp +=             "                        AND ROWNUM = 1 ) ";

		this.executarUpdateDelete(sqlUp, ps);

		return this.consultarLimparTemp(temp);
	}

	/**
	 * Reserva a ultima pendencia para um serventia tipo
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 29/10/2008 15:400
	 * @param String
	 *            tipo, tipo da pendencia
	 * @param String
	 *            idServentiaTipo, id da serventia tipo
	 * @return PendenciaDt
	 * @throws Exception
	 */
	public PendenciaDt reservarUltimaServentiaTipo(String tipo, String idServentiaTipo) throws Exception {
		String temp = String.valueOf(Math.round(Math.random() * 100000));
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		// Atualiza registro nas condicoes especificadas
		//String sqlUp = "UPDATE PROJUDI.Pendencia set PEND.CODIGO_TEMP = " + temp + ", PEND.DATA_TEMP = " + Funcoes.BancoDataHora(new Date()) + " WHERE PEND.ID_PEND = ( " + " SELECT up.ID_PEND FROM PROJUDI.VIEW_PEND_ULTIMA_SERV_TIPO up WHERE up.ID_PEND_TIPO = " + tipo + " AND up.ID_SERV_TIPO = " + idServentiaTipo + " )";
//		String sqlUp =  "UPDATE PROJUDI.PEND inner joIN ( SELECT ID_PEND";
//		sqlUp +=			 " FROM  PROJUDI.VIEW_PEND_ULTIMA_SERV_TIPO ";
//		sqlUp +=             " WHERE ID_PEND_TIPO = ? AND ID_SERV_TIPO = ? group by ID_PEND_TIPO,ID_SERV_TIPO ";
//		ps.adicionarLong(tipo);
//		ps.adicionarLong(idServentiaTipo);
//		sqlUp +=         "  ) PEND_ULT_SERVTipo on PROJUDI.PEND.ID_PEND =  PEND_ULT_SERVTipo.ID_PEND ";
//		sqlUp +=	     " set PEND.CODIGO_TEMP = ? , PEND.DATA_TEMP = ?";
//		ps.adicionarLong(temp);
//		ps.adicionarDateTime(new Date());
		
		String sqlUp =  "UPDATE PROJUDI.PEND  p set p.CODIGO_TEMP = ? , p.DATA_TEMP = ? ";
		ps.adicionarLong(temp);
		ps.adicionarDateTime(new Date());
		sqlUp +=             " WHERE  p.ID_PEND = (  SELECT min(ID_PEND) as ID_PEND ";
		sqlUp +=             " FROM  PROJUDI.VIEW_PEND_ULTIMA_SERV_TIPO ";  
		sqlUp +=             " WHERE  ID_PEND_TIPO = ? AND  ID_SERV_TIPO = ?  ";
		sqlUp +=             " group by ID_PEND_TIPO,ID_SERV_TIPO )  ";
		ps.adicionarLong(tipo);
		ps.adicionarLong(idServentiaTipo);

		this.executarUpdateDelete(sqlUp, ps);

		return this.consultarLimparTemp(temp);
	}

	/**
	 * Consulta as pendencias do tipo conclusão que já foram finalizadas
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param numeroProcesso, filtro número do processo
	 * @param digitoVerificador, dígito do processo
	 * @param dataInicial, data inicial para consulta
	 * @param dataFinal, data final para consulta
	 * 
	 * @author msapaula
	 * @since 15/01/2009 12:00
	 */
	public List consultarConclusoesFinalizadas(String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String dataInicial, String dataFinal) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT * FROM PROJUDI.VIEW_PEND_FINAIS_FINA_RS_SV_CG pen";
		sql += " WHERE pen.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);
		if (dataInicial.length() > 0){
			sql += " AND pen.DATA_FIM >= ?";
			ps.adicionarDateTime(Funcoes.DataHora(dataInicial + " 00:00:00"));						
		}
		if (dataFinal.length() > 0){
			sql += " AND pen.DATA_FIM <= ?";
			ps.adicionarDateTime(Funcoes.DataHora(dataFinal + " 23:59:59"));
		}
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}
		sql += " ORDER BY pen.PEND_TIPO, pen.DATA_FIM desc";

		ResultSetTJGO rs1 = null;
		try{

			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataFim(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setUsuarioFinalizador(rs1.getString("USU_FINALIZADOR"));
				pendenciaDt.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));
				pendencias.add(pendenciaDt);
			}

		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}

	/**
	 * Método que verifica se há pendências do tipo conclusão em aberto para o processo passado. Será utilizado em verificações.
	 * 
	 * @param id_Processo, identificação de processo
	 * 
	 * @author msapaula
	 *         mmgomes - Alterado para retornar uma lista de conclusões
	 */
	public List consultarConclusoesAbertas(String id_Processo) throws Exception {
		List conclusoesPendentes = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT p.ID_PEND, p.PEND_TIPO, p.DATA_INICIO, COUNT(pa.ID_ARQ) as quantidade, PROC_NUMERO_COMPLETO";
		sql += " FROM PROJUDI.VIEW_PEND_ABERTAS_SERV_CARGO p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on (p.ID_PEND_TIPO = pt.ID_PEND_TIPO) ";
		sql += " LEFT JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = 1)";
		sql += " LEFT JOIN PROJUDI.ARQ a on (pa.ID_ARQ = a.ID_ARQ  AND a.USU_ASSINADOR IS NULL)";
		sql += " WHERE p.ID_PROC = ? ";																		ps.adicionarLong(id_Processo);
		sql += " group by p.ID_PEND, pa.ID_ARQ, p.PEND_TIPO, p.DATA_INICIO, PROC_NUMERO_COMPLETO";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				conclusoesPendentes.add(new String[] {rs1.getString("ID_PEND"), rs1.getString("PEND_TIPO"),Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")), rs1.getString("quantidade"),rs1.getString("PROC_NUMERO_COMPLETO")});
			}		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return conclusoesPendentes;
	}
	
	/**
	 * Método de consulta PÚBLICA que verifica se há pendências do tipo conclusão em aberto para o processo passado. Será utilizado em verificações.
	 * @param id_Processo, identificação de processo
	 * @author hmgodinho
	 */
	public List consultarConclusoesAbertasPublico(String id_Processo) throws Exception {
		List conclusoesPendentes = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT p.ID_PEND, p.PEND_TIPO, p.DATA_INICIO, COUNT(pa.ID_ARQ) as quantidade, PROC_NUMERO_COMPLETO";
		sql += " FROM PROJUDI.VIEW_PEND_ABERTAS_SERV_CARGO p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on (p.ID_PEND_TIPO = pt.ID_PEND_TIPO) ";
		sql += " LEFT JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = 1)";
		sql += " LEFT JOIN PROJUDI.ARQ a on (pa.ID_ARQ = a.ID_ARQ  AND a.USU_ASSINADOR IS NULL)";
		sql += " WHERE p.ID_PROC = ? ";																		ps.adicionarLong(id_Processo);
		sql += " AND pt.PEND_TIPO_CODIGO <> ? ";															ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		sql += " AND pt.PEND_TIPO_CODIGO <> ?";																ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);		  
		sql += " group by p.ID_PEND, pa.ID_ARQ, p.PEND_TIPO, p.DATA_INICIO, PROC_NUMERO_COMPLETO";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				conclusoesPendentes.add(new String[] {rs1.getString("ID_PEND"), rs1.getString("PEND_TIPO"),Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")), rs1.getString("quantidade"),rs1.getString("PROC_NUMERO_COMPLETO")});
			}		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return conclusoesPendentes;
	}
	
	/**
	 * Método que verifica se há pendências do tipo conclusão em aberto para o processo passado. Será utilizado em verificações.
	 * 
	 * @param id_Processo, identificação de processo
	 * 
	 * @author lsbernardes
	 * 		   mmgomes - Alteração para consultar mais de uma conclusão aberta
	 */
	public List consultarConclusoesAbertasHash(String id_Processo, UsuarioNe usuarioSessao) throws Exception {
		List conclusoesPendentes = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT p.ID_PEND, p.PEND_TIPO, p.DATA_INICIO, COUNT(pa.ID_ARQ) as quantidade, PROC_NUMERO_COMPLETO,  pa.CODIGO_TEMP";
		sql += " FROM PROJUDI.VIEW_PEND_ABERTAS_SERV_CARGO p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on (p.ID_PEND_TIPO = pt.ID_PEND_TIPO) ";
		sql += " LEFT JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = 1)";
		sql += " LEFT JOIN PROJUDI.ARQ a on (pa.ID_ARQ = a.ID_ARQ  AND a.USU_ASSINADOR IS NULL)";
		sql += " WHERE p.ID_PROC = ? AND pt.PEND_TIPO_CODIGO <> ? AND pt.PEND_TIPO_CODIGO <> ?";
		ps.adicionarLong(id_Processo); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		sql += " group by p.ID_PEND, pa.ID_ARQ, p.PEND_TIPO, p.DATA_INICIO, p.PROC_NUMERO_COMPLETO, pa.CODIGO_TEMP";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				conclusoesPendentes.add(new String[] {rs1.getString("ID_PEND") + "@#!@"+ usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")), rs1.getString("PEND_TIPO"),Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")), rs1.getString("quantidade"),rs1.getString("PROC_NUMERO_COMPLETO"), usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")), rs1.getString("CODIGO_TEMP")});
			}

		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return conclusoesPendentes;
	}
	
	/**
	 * Método que consulta se há alguma conclusão aberta para um processo passado
	 * 
	 * @param id_Processo, identificação de processo
	 * @param id_ServentiaCargo, identificação do cargo (opcional)
	 * 
	 * @author msapaula
	 */
	public PendenciaDt consultarConclusaoAbertaProcesso(String id_Processo, String id_ServentiaCargo) throws Exception {
		PendenciaDt conclusaoPendente = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT * FROM PROJUDI.VIEW_PEND_ABER_RESP_SERV_CARGO p";
		sql += " WHERE p.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		//Para processos de 1 grau não há necessidade de informar o serv_cargo, pois a conclusão fica 
		//sob responsabilidade apenas do magistrado.
		if(id_ServentiaCargo != null && !id_ServentiaCargo.equalsIgnoreCase("")){
			sql += " AND p.ID_SERV_CARGO = ?";
			ps.adicionarLong(id_ServentiaCargo);
		}
		
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				conclusaoPendente = new PendenciaDt();
				super.associarDt(conclusaoPendente, rs1);
			}
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return conclusaoPendente;
	}
	
	 /**
     * Consultar as Dez conclusões mais antingas em aberto para um determinado Serventia Cargo
     * 
     * @param id_UsuarioServentiaCargoAtual,
     *            identificação do Cargo
     * 
     * @return List PendenciaDt
     * 
     * @author lsbernardes
     */
	public List consultarConclusoesMaisAntigasMagistado(String id_UsuarioServentiaCargoAtual, String quantidade) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List conclusoesPendentes = new ArrayList();

		String sql = " SELECT * FROM ( ";
		sql += " SELECT * FROM PROJUDI.VIEW_PEND_ABER_RESP_SERV_CARGO p ";
		sql += " WHERE p.ID_SERV_CARGO = ? "; ps.adicionarLong(id_UsuarioServentiaCargoAtual);
		sql += " AND not exists ";    
		sql += " 	(SELECT pa.ID_PEND FROM PROJUDI.PEND_ARQ pa WHERE pa.RESPOSTA = ? and pa.id_pend=p.id_pend)"; ps.adicionarLong(1);
		sql += " ORDER BY ID_PEND ) ";
		sql += " WHERE ROWNUM <= ? "; ps.adicionarLong(quantidade);
		
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt conclusaoPendente = new PendenciaDt();
				super.associarDt(conclusaoPendente, rs1);
				conclusoesPendentes.add(conclusaoPendente);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return conclusoesPendentes;
	}

	/**
	 * Consulta pendências em um processo em uma determinada serventia, ou seja, qualquer pendência em aberto (até mesmo as reservadas),
	 * ou pendências finalizadas mas que ainda não foram vistadas
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_Serventia, identificação da serventia
	 * @param fabConexao, conexão ativa
	 * 
	 * @author msapaula
	 * @since 02/03/2009 10:56
	 */
	public int consultarQuantidadePendenciasProcesso(String id_Processo, String id_Serventia) throws Exception {
		int qtde = 0;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT COUNT(*) as qtde FROM PROJUDI.VIEW_PEND_PROC p ";
		sql += " INNER JOIN PROJUDI.VIEW_PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE p.ID_PROC = ? and p.PEND_TIPO_CODIGO not in (?)"; ps.adicionarLong(id_Processo); 	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_PENDENTE);
		sql += " AND (pr.ID_SERV = ? or pr.ID_SERVENTIA_CARGO = ?)";	 ps.adicionarLong(id_Serventia);	ps.adicionarLong(id_Serventia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) qtde = rs1.getInt("qtde");
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return qtde;
	}
	
	/**
	 * Verifica se há pendências em um processo em uma determinada serventia, ou seja, qualquer pendência em aberto (até mesmo as reservadas),
	 * ou pendências finalizadas mas que ainda não foram vistadas
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_Serventia, identificação da serventia
	 * @param fabConexao, conexão ativa
	 * 
	 * @author lsbernardes
	 */
	public List consultarPendenciasProcessoRedistribuicaoLote(String id_Processo, String id_Serventia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List pendenciasAbertas = new ArrayList();
			

		String sql = "SELECT p.ID_PEND, pr.ID_PEND_RESP, pr.ID_SERV FROM PROJUDI.VIEW_PEND_PROC p ";
		sql += " INNER JOIN PROJUDI.VIEW_PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE p.ID_PROC = ? ";
		ps.adicionarLong(id_Processo);
		sql += " AND pr.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				pendenciasAbertas.add( new String[] {rs1.getString("ID_PEND"),rs1.getString("ID_PEND_RESP"),rs1.getString("ID_SERV") });
			}

		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendenciasAbertas;
	}

	/**
	 * Consulta a quantidade de pendencias reservadas por tipo
	 * 
	 * @param usuarioDt,
	 *            dt de usuario
	 * @param tipo,
	 *            tipo da pendencia
	 * 
	 * @author lsbernardes
	 */
	/*public int consultarQuantidadeServentiaReservadasPorTipo(UsuarioDt usuarioDt, String tipo){
		String sql = "SELECT COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT COUNT(*) = 0 FROM PROJUDI.PEND_RESP pr ";
		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " ) ) podeLiberar, pa.ID_PEND ";
		//sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
		sql += " WHERE ( pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " )  AND  pa.ID_SERV is not null AND pa.ID_PEND_TIPO = " + tipo + " ) preAnalisar WHERE preAnalisar.podeLiberar = 1";

		try{
			ResultSetTJGO rs1 = this.consultar(sql);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
	}*/
	
	public List consultarPendenciasServentiaReservadas(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		
//		String sql = "SELECT reservadas.ID_PEND_TIPO,reservadas.PEND_TIPO , COUNT(*)AS QUANTIDADE  FROM (SELECT (	SELECT COUNT(*) = 0 FROM PROJUDI.PEND_RESP pr ";
//		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia();
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe();
//		}
//		sql += " ) ) podeLiberar,  pa.ID_PEND_TIPO,  pa.PEND_TIPO ";
//		sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
//		sql += " WHERE ( pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia();
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentiaChefe();
//		}
//		sql += " )  AND  pa.ID_SERV is not null ) reservadas WHERE reservadas.podeLiberar = 1 group by reservadas.ID_PEND_TIPO";

		
		String sql = " SELECT  pa.ID_PEND_TIPO, pa.PEND_TIPO , COUNT(*)AS QUANTIDADE ";
		sql += " FROM  PROJUDI.VIEW_PEND_RESERV_RESP_NOVA pa ";
     	
     	sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";     		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
   		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
   			sql += " OR pa.ID_USU_FINALIZADOR = ?";   			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
   		}
   		
   		if (usuarioDt != null && (usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL 
   				|| usuarioDt.getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL
   				|| usuarioDt.getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL
	    		|| usuarioDt.getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL) ){
	    	sql += " AND (PEND_TIPO_CODIGO NOT IN ( ?,?,?,? )) ";	
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO); ps.adicionarLong(PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO);
	    }
   		
   		//Não deve ser aparcer na tela inicial as pendências do tipo Soliciatação de carga
   		sql += " AND (PEND_TIPO_CODIGO <> ?) ";	ps.adicionarLong(PendenciaTipoDt.SOLICITACAO_CARGA);
   		
   		if (usuarioDt.getGrupoCodigo() != null && usuarioDt.getGrupoCodigo().length() > 0
	    		&& (Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU 
	    		|| Funcoes.StringToInt(usuarioDt.getGrupoCodigo()) == GrupoDt.ESTAGIARIO_SEGUNDO_GRAU)){
	    	sql += " AND (PEND_TIPO_CODIGO NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?)) ";
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA);
	    	ps.adicionarLong(PendenciaTipoDt.AVERBACAO_CUSTAS);
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC);
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT);
	    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC);
	    	ps.adicionarLong(PendenciaTipoDt.SUSPENSAO_PROCESSO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CALCULO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_CAMARA_SAUDE);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_LAUDO_RELATORIO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_CENOPES);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DEVOLUCAO_PRECATORIA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_PAGA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_VENCIDA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_AUDIENCIAS_REALIZADAS_CEJUSC);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PARECER);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PETICAO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PROCESSO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PROCESSO_HIBRIDO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CLASSE_PROCESSUAL);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_FATO);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_OFICIO_DELEGACIA);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE);
	    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RETORNO_AR_CORREIOS);
	    }
   		
   		sql += " )	AND pa.ID_SERV is not null group by  pa.ID_PEND_TIPO, pa.PEND_TIPO ";   
		
   		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	public List consultarPendenciasServentiaReservadasDistribuidorCamara(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = " SELECT  pa.ID_PEND_TIPO, pa.PEND_TIPO , COUNT(*)AS QUANTIDADE ";
		sql += " FROM  PROJUDI.VIEW_PEND_RESERV_RESP_NOVA pa ";
     	
     	sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";     		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
   		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
   			sql += " OR pa.ID_USU_FINALIZADOR = ?";   			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
   		}
   		
   		sql += " AND (PEND_TIPO_CODIGO IN( ?,?,?,? )) "; ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO); ps.adicionarLong(PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO);
   		
   		sql += " )	AND pa.ID_SERV is not null group by  pa.ID_PEND_TIPO, pa.PEND_TIPO ";   
		
   		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}

	/**
	 * Consulta a quantidade de pendencias cargo serventia reservadas por tipo
	 * 
	 * @param usuarioDt,
	 *            dt de usuario
	 * @param tipo,
	 *            tipo da pendencia
	 * 
	 * @author lsbernardes
	 */
	/*public int consultarQuantidadeCargoServentiaReservadasPorTipo(UsuarioDt usuarioDt, String tipo){
		String sql = "SELECT COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT COUNT(*) = 0 FROM PROJUDI.PEND_RESP pr ";
		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " ) ) as podeLiberar, pa.ID_PEND ";
		//sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
		sql += " WHERE ( pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " )  AND  pa.ID_SERV_CARGO is not null AND pa.ID_PEND_TIPO = " + tipo + " ) preAnalisar WHERE preAnalisar.podeLiberar = 1";

		try{
			ResultSetTJGO rs1 = this.consultar(sql);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
	}*/
	
	public List consultarPendenciasCargoServentiaReservadas(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		String sql = "SELECT reservadas.ID_PEND_TIPO,reservadas.PEND_TIPO , COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT  1 as PODE_LIBERAR FROM PROJUDI.PEND_RESP pr ";
//		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = ?";
//		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pr.ID_USU_RESP = ?";
//			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
//		}
//		sql += " ) ) PODE_LIBERAR, pa.ID_PEND_TIPO, pa.PEND_TIPO ";
//		sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
//		sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";
//		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pa.ID_USU_FINALIZADOR = ? ";
//			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
//		}
//		sql += " )  AND  pa.ID_SERV_CARGO is not null ) reservadas WHERE reservadas.PODE_LIBERAR IS NULL group by reservadas.ID_PEND_TIPO, reservadas.PEND_TIPO";
		
		String sql = " SELECT  pa.ID_PEND_TIPO, pa.PEND_TIPO , COUNT(*)AS QUANTIDADE ";
		sql += " FROM  PROJUDI.VIEW_PEND_RESERV_RESP_NOVA pa ";
     	
     	sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";     		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
   		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
   			sql += " OR pa.ID_USU_FINALIZADOR = ?";   			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
   		}
   		
   		sql += " )	AND pa.ID_SERV_CARGO is not null group by  pa.ID_PEND_TIPO, pa.PEND_TIPO ";   
		
   		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}

	/**
	 * Retorna as pendências de mandados reservadas para os oficiais de justiça.
	 * @param usuarioDt
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public List consultarPendenciasMandadosReservadosOficial(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = " SELECT  ID_PEND_TIPO, PEND_TIPO , COUNT(*) AS QUANTIDADE ";
		sql +=  " FROM ( ";
		sql += " SELECT PEN.ID_PEND       AS ID_PEND, ";
		sql += " PT.PEND_TIPO           AS PEND_TIPO, ";
		sql += " PT.ID_PEND_TIPO        AS ID_PEND_TIPO ";
		
		sql += " FROM PEND PEN ";
		sql += " JOIN PEND_TIPO PT ON PEN.ID_PEND_TIPO = PT.ID_PEND_TIPO and PT.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.MANDADO);;
		sql += " JOIN PEND_RESP PR ON Pen.Id_Pend                           = Pr.Id_Pend ";
		sql += " INNER JOIN MAND_JUD MJ ON MJ.ID_PEND = PEN.ID_PEND ";
		sql += " WHERE ";
		sql += " PEN.DATA_FIM                        IS NULL ";
		sql += " AND PEN.ID_USU_FINALIZADOR          IS NOT NULL ";
		sql += " AND MJ.ID_USU_SERV_1 = ? )"; ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
		sql += "  group by ID_PEND_TIPO, PEND_TIPO ";
		
   		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
	 * Consulta a quantidade de pendencias serventia tipo reservadas por tipo
	 * 
	 * @param usuarioDt,
	 *            dt de usuario
	 * @param tipo,
	 *            tipo da pendencia
	 * 
	 * @author lsbernardes
	 */
	public int consultarQuantidadeServentiaTipoReservadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		String sql = "SELECT COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT 1 as PODE_LIBERAR FROM PROJUDI.PEND_RESP pr ";
//		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = ? ";
//		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pr.ID_USU_RESP = ? ";
//			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
//		}
//		sql += " ) ) PODE_LIBERAR, pa.ID_PEND ";
//		sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
//		sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";
//		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pa.ID_USU_FINALIZADOR = ? ";
//			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
//		}
//		sql += " )  AND  pa.ID_SERV_TIPO is not null AND pa.ID_PEND_TIPO = ? ) preAnalisar WHERE preAnalisar.PODE_LIBERAR IS NULL ";
//		ps.adicionarLong(tipo);
		
		String sql = " SELECT  pa.ID_PEND_TIPO, pa.PEND_TIPO , COUNT(*)AS QUANTIDADE ";
		sql += " FROM  PROJUDI.VIEW_PEND_RESERV_RESP_NOVA pa ";
     	
     	sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";     		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
   		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
   			sql += " OR pa.ID_USU_FINALIZADOR = ?";   			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
   		}
   		
   		sql += " )	AND pa.ID_SERV_TIPO is not null group by  pa.ID_PEND_TIPO, pa.PEND_TIPO ";   

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
	}

	/**
	 * Consulta a quantidade de pendencias para assistente do tipo reservadas
	 * por tipo
	 * 
	 * @param usuarioDt,
	 *            dt de usuario
	 * @param tipo,
	 *            tipo da pendencia
	 * 
	 * @author lsbernardes
	 */
	/*public int consultarQuantidadeAssistenteTipoReservadasPorTipo(UsuarioDt usuarioDt, String tipo){
		String sql = "SELECT COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT COUNT(*) = 0 FROM PROJUDI.PEND_RESP as pr ";
		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " ) ) podeLiberar, pa.ID_PEND ";
		//sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
		sql += " WHERE ( pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " )  AND pa.ID_PEND_TIPO = " + tipo + " ) preAnalisar WHERE preAnalisar.podeLiberar = 1";

		try{
			ResultSetTJGO rs1 = this.consultar(sql);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
	}*/

	/**
	 * Consulta a quantidade de pendencias serventia pre-anlisadas por tipo
	 * 
	 * @param usuarioDt,
	 *            dt de usuario
	 * @param tipo,
	 *            tipo da pendencia
	 * 
	 * @author lsbernardes
	 */
	/*public int consultarQuantidadeServentiaPreAnalisadasPorTipo(UsuarioDt usuarioDt, String tipo){
		String sql = "SELECT COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT COUNT(*) = 0 FROM PROJUDI.PEND_RESP as pr ";
		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " ) ) podeLiberar, pa.ID_PEND ";
		//sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
		sql += " WHERE ( pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " )  AND  pa.ID_SERV is not null AND pa.ID_PEND_TIPO = " + tipo + " ) preAnalisar WHERE preAnalisar.podeLiberar = 0";

		try{
			ResultSetTJGO rs1 = this.consultar(sql);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
	}*/
	
	public List consultarPendenciasServentiaPreAnalisadas(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
//		String sql = "SELECT preAnalisar.ID_PEND_TIPO,preAnalisar.PendenciaTipo , COUNT(*)AS QUANTIDADE    FROM (SELECT (	SELECT COUNT(*) = 0 FROM PROJUDI.PEND_RESP pr ";
//		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia();
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe();
//		}
//		sql += " ) ) podeLiberar,  pa.ID_PEND_TIPO,  pa.PEND_TIPO ";
//		sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
//		sql += " WHERE ( pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia();
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentiaChefe();
//		}
//		sql += " )  AND  pa.ID_SERV is not null ) preAnalisar WHERE preAnalisar.podeLiberar = 0 " +
//				"group by  preAnalisar.ID_PEND_TIPO";

		
		String sql = " SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO , COUNT(*)AS QUANTIDADE ";
		sql += " FROM PROJUDI.VIEW_PEND_PRE_ANA_RESP pa ";
   	 	
   	 	sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";   	 	ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
   		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
   			sql += " OR pa.ID_USU_FINALIZADOR = ? ";   			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
   		}
   		int GrupoCodigo = usuarioDt.getGrupoCodigoToInt();
   		switch (GrupoCodigo) {
   			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL: 
			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
		    	sql += " AND (PEND_TIPO_CODIGO NOT IN ( ?,?,?,? )) ";	
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DISTRIBUICAO); 
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO); 
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO); 
		    	ps.adicionarLong(PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO);			
		    	break;
			case GrupoDt.MINISTERIO_PUBLICO:
			case GrupoDt.MP_TCE:
			case GrupoDt.ADVOGADO_DEFENSOR_PUBLICO:
			case GrupoDt.ADVOGADO_PARTICULAR:
			case GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO:  
			case GrupoDt.ADVOGADO_PUBLICO:
			case GrupoDt.ADVOGADO_PUBLICO_ESTADUAL:
			case GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL:
			case GrupoDt.ADVOGADO_PUBLICO_UNIAO: 
		   	case GrupoDt.ASSESSOR_ADVOGADOS:
			case GrupoDt.ASSESSOR_MP:		   	
		    	sql += " AND (PEND_TIPO_CODIGO NOT IN ( ?,? )) ";	
		    	ps.adicionarLong(PendenciaTipoDt.INTIMACAO); 
		    	ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		    	break;
			case GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU:
			case GrupoDt.ESTAGIARIO_SEGUNDO_GRAU:
		    	sql += " AND (PEND_TIPO_CODIGO NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?)) ";
		    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA);
		    	ps.adicionarLong(PendenciaTipoDt.AVERBACAO_CUSTAS);
		    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC);
		    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT);
		    	ps.adicionarLong(PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC);
		    	ps.adicionarLong(PendenciaTipoDt.SUSPENSAO_PROCESSO);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CALCULO);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_CAMARA_SAUDE);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_LAUDO_RELATORIO);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_CENOPES);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DEVOLUCAO_PRECATORIA);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_PAGA);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_GUIA_VENCIDA);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_AUDIENCIAS_REALIZADAS_CEJUSC);	  
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PARECER);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PETICAO);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PROCESSO);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PROCESSO_HIBRIDO);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CLASSE_PROCESSUAL);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_FATO);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESPOSTA_OFICIO_DELEGACIA);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE);
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RETORNO_AR_CORREIOS);
		    	break;		
		}   			   		   			   
   		
   		sql += " )	AND pa.ID_SERV is not null group by  pa.ID_PEND_TIPO, pa.pend_tipo ";   
		
   		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}

	/**
	 * Consulta a quantidade de pendencias cargo serventia pre-anlisadas por
	 * tipo
	 * 
	 * @param usuarioDt,
	 *            dt de usuario
	 * @param tipo,
	 *            tipo da pendencia
	 * 
	 * @author lsbernardes
	 */
	/*public int consultarPendenciasCargoServentiaPreAnalisadasPorTipo(UsuarioDt usuarioDt, String tipo){
		String sql = "SELECT COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT COUNT(*) = 0 FROM PROJUDI.PEND_RESP pr ";
		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " ) ) podeLiberar, pa.ID_PEND ";
		//sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
		sql += " WHERE ( pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " )  AND  pa.ID_SERV_CARGO is not null AND pa.ID_PEND_TIPO = " + tipo + " ) preAnalisar WHERE preAnalisar.podeLiberar = 0";

		try{
			ResultSetTJGO rs1 = this.consultar(sql);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
	}*/
	
	public List consultarPendenciasCargoServentiaPreAnalisadas(UsuarioDt usuarioDt) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		String sql = "SELECT preAnalisar.ID_PEND_TIPO,preAnalisar.PEND_TIPO , COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT  1 as PODE_LIBERAR FROM PROJUDI.PEND_RESP pr ";
//		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = ? ";
//	    ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pr.ID_USU_RESP = ? ";
//			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
//		}
//		sql += " ) ) PODE_LIBERAR, pa.ID_PEND_TIPO, pa.PEND_TIPO ";
//		sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
//		sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";
//		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pa.ID_USU_FINALIZADOR = ? ";
//			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
//		}
//		sql += " )  AND  pa.ID_SERV_CARGO is not null ) preAnalisar WHERE preAnalisar.PODE_LIBERAR = 1 " +
//				"group by preAnalisar.ID_PEND_TIPO, preAnalisar.PEND_TIPO";
		
		String sql = " SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO , COUNT(*)AS QUANTIDADE ";
		sql += " FROM PROJUDI.VIEW_PEND_PRE_ANA_RESP pa ";
   	 	
   	 	sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";   	 	ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
   		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
   			sql += " OR pa.ID_USU_FINALIZADOR = ? ";   			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
   		}
   		
   		sql += " )	AND pa.ID_SERV_CARGO is not null group by  pa.ID_PEND_TIPO, pa.pend_tipo ";  

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				String[] stTemp = {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") };
				pendencias.add(stTemp);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
	 * Consulta a quantidade de pendencias serventia tipo pre-anlisadas por tipo
	 * 
	 * @param usuarioDt,
	 *            dt de usuario
	 * @param tipo,
	 *            tipo da pendencia
	 * 
	 * @author lsbernardes
	 */
	public int consultarQuantidadeServentiaTipoPreAnalisadasPorTipo(UsuarioDt usuarioDt, String tipo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		String sql = "SELECT COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT 1 as PODE_LIBERAR FROM PROJUDI.PEND_RESP pr ";
//		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = ? ";
//		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pr.ID_USU_RESP = ? ";
//			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
//		}
//		sql += " ) ) PODE_LIBERAR, pa.ID_PEND ";
//		sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
//		sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";
//		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
//		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
//			sql += " OR pa.ID_USU_FINALIZADOR = ? ";
//			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
//		}
//		sql += " )  AND  pa.ID_SERV_TIPO is not null AND pa.ID_PEND_TIPO = ?) preAnalisar WHERE preAnalisar.PODE_LIBERAR = ? ";
//		ps.adicionarLong(tipo);
//		ps.adicionarLong(1);  
		
		String sql = " SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO , COUNT(*)AS QUANTIDADE ";
		sql += " FROM PROJUDI.VIEW_PEND_PRE_ANA_RESP pa ";
   	 	
   	 	sql += " WHERE ( pa.ID_USU_FINALIZADOR = ? ";   	 	ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
   		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
   			sql += " OR pa.ID_USU_FINALIZADOR = ? ";   			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
   		}
   		
   		sql += " )	AND pa.ID_SERV_TIPO is not null group by  pa.ID_PEND_TIPO, pa.pend_tipo ";  

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
		finally{
			 if (rs1!= null) rs1.close();
		}
	}

	/**
	 * Consulta a quantidade de pendencias assistente pre-anlisadas por tipo
	 * 
	 * param usuarioDt,
	 *            dt de usuario
	 * param tipo,
	 *            tipo da pendencia
	 * 
	 * author lsbernardes
	 */
	/*public int consultarQuantidadeAssistentePreAnalisadasPorTipo(UsuarioDt usuarioDt, String tipo){
		String sql = "SELECT COUNT(*)AS QUANTIDADE FROM (SELECT (	SELECT COUNT(*) = 0 FROM PROJUDI.PEND_RESP pr ";
		sql += " WHERE pr.ID_PEND = pa.ID_PEND AND ( pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pr.ID_USU_RESP = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " ) ) podeLiberar, pa.ID_PEND ";
		//sql += " FROM PROJUDI.VIEW_PEND_RESERVADAS_RESP pa ";
		sql += " WHERE ( pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentia();
		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR pa.ID_USU_FINALIZADOR = " + usuarioDt.getId_UsuarioServentiaChefe();
		}
		sql += " )  AND pa.ID_USU_RESP is not null  AND pa.ID_PEND_TIPO = " + tipo + " ) preAnalisar WHERE preAnalisar.podeLiberar = 0";

		try{
			ResultSetTJGO rs1 = this.consultar(sql);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
	}*/

	public List relUsuariosPendencias(String stId_Usuario, String stId_Serventia, String stAno, String stMes) throws Exception {
		List liTemp = new ArrayList();
		String stSql = "";
		String stSql1 = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		stSql = " SELECT * ";
		stSql += " FROM VIEW_USU_PEND01 ";

		if (stId_Serventia != null && stId_Serventia.length() > 0){
			stSql1 = " WHERE ID_SERV = ? ";
			ps.adicionarLong(stId_Serventia);
		}

		if (stId_Usuario != null && stId_Usuario.length() > 0){
			if (stSql1.length() > 0) stSql1 += " AND ";
			else stSql1 = " WHERE ";
			stSql1 += " ID_USU = ? ";
			ps.adicionarLong(stId_Usuario);
		}	

		if (stAno != null && stAno.length() > 0){
			if (stSql1.length() > 0) stSql1 += " AND ";
			else stSql1 = " WHERE ";
			stSql1 += " ANO = ? ";
			ps.adicionarLong(stAno);		
		}		

		if (stMes != null && stMes.length() > 0){
			if (stSql1.length() > 0) stSql1 += " AND ";
			else stSql1 = " WHERE ";			
			stSql1 += " MES = ? ";
			ps.adicionarLong(stMes);			
		}
		

		stSql1 += " ORDER BY ANO, MES, ID_USU, ID_SERV";
		try{
			rs1 = consultar(stSql + stSql1, ps);
			while (rs1.next()) {
				UsuarioPendenciasDt obTemp = new UsuarioPendenciasDt();
				obTemp.setId_Serventia(rs1.getLong("ID_SERV"));
				obTemp.setServentia(rs1.getString("SERV"));
				obTemp.setId_Usuario(rs1.getString("ID_USU"));
				obTemp.setUsuario(rs1.getString("USU"));
				obTemp.setAno(rs1.getInt("ANO"));
				obTemp.setMes(rs1.getInt("MES"));
				obTemp.setPendencia(rs1.getString("PEND"));
				obTemp.setQuantidade(rs1.getLong("QUANTIDADE"));

				liTemp.add(obTemp);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return liTemp;
	}

	/**
	 * Retorna os tipos de pendência do tipo conclusão que estão abertas para
	 * listar na página inicial
	 * 
	 * @author msapaula
	 * @since 25/05/2009
	 * @param id_ServentiaCargo
	 *            serventia cargo responsavel pelas conclusões
	 */
	public Map consultarTiposConclusoesAbertas(String id_ServentiaCargo) throws Exception {
		Map mapConclusoes = new HashMap();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT DISTINCT pa.ID_PEND_TIPO, pa.PEND_TIPO ";
		sql += " FROM PROJUDI.VIEW_PEND_ABER_RESP_SERV_CARGO pa WHERE ";
		sql += " pa.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		sql += " ORDER BY pa.PEND_TIPO";
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);

			// Preenche map
			while (rs1.next()) {
				mapConclusoes.put(rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"));
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		
		return mapConclusoes;
	}

	/**
	 * Consulta a quantidade de pendencias do tipo Conclusão que não foram analisadas, de um determinado tipo para o usuario.
	 * - Consulta somente as conclusões que não possuem nenhuma pré-análise registrada
	 * 
	 * @author msapaula
	 * @since 25/05/2009 14:44
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param tipo, tipo da conclusão
	 */
	public List consultarQuantidadeConclusoesNaoAnalisadas(String id_ServentiaCargo, boolean somenteSessao, boolean ehAssistente, boolean ehVotoVencido) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "SELECT p.ID_PEND_TIPO, pt.PEND_TIPO,  COUNT(*) AS QUANTIDADE FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		if (somenteSessao){
			//sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?) ";			
			//ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
			sql += " WHERE pt.PEND_TIPO_CODIGO = ? ";
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
			if(ehVotoVencido){
				sql += " AND p.CODIGO_TEMP = ?";
				ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
			} else {
				sql += " AND (p.CODIGO_TEMP IS NULL OR p.CODIGO_TEMP <> ?)";
				ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
				sql += " AND NOT EXISTS (SELECT 1";        
		        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
		        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
		        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
		        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
		        sql += "                   AND ((aci.ID_PEND_VOTO IS NOT NULL AND aci.ID_PEND_VOTO = P.ID_PEND) OR (aci.ID_PEND_VOTO_REDATOR IS NOT NULL AND aci.ID_PEND_VOTO_REDATOR = P.ID_PEND))";
		        
		        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        	if(ehAssistente){
	        		 	sql += " AND EXISTS (SELECT 1";
	        		 	sql += "               FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
	        	        sql += "              WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR) ";
	        	        sql += "               AND pri.ID_SERV_CARGO = ? ";
	        	        ps.adicionarLong(id_ServentiaCargo);       
	        	        sql += "               AND pi.ID_PROC = aci.ID_PROC ";
	        	        sql += "            ) ";
		        	} else {
		        		sql += "               AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
		        		ps.adicionarLong(id_ServentiaCargo);
		        		ps.adicionarLong(id_ServentiaCargo);	        		
		        	}
		        }        
	        	sql += "                   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
	        	ps.adicionarLong(0);
	        	sql += " AND aci.ID_PROC = p.ID_PROC";
	        	sql += " )";
	        	
			}
			sql += " AND EXISTS (  SELECT 1 "
    				+ " FROM PROJUDI.AUDI_PROC AP JOIN PROJUDI.AUDI AD ON AD.ID_AUDI = AP.ID_AUDI "
    				+ " WHERE AP.ID_PROC = P.ID_PROC "    				
    				+ " AND AD.VIRTUAL IS NULL "
    				+ " AND AD.SESSAO_INICIADA IS NULL "
    				+ " AND AD.VIRTUAL IS NULL";			        
			if(ehVotoVencido){
				sql += " AND AP.ID_SERV_CARGO_REDATOR IS NOT NULL ";
				sql += " AND AP.ID_SERV_CARGO <> AP.ID_SERV_CARGO_REDATOR ";
			} else {
				sql += " AND AP.DATA_MOVI IS NULL ";
				sql += " AND AP.ID_AUDI_PROC_STATUS = 1 ";
				sql += " AND (AP.ID_PEND_VOTO = P.ID_PEND OR AP.ID_PEND_VOTO_REDATOR = P.ID_PEND) ";	
			}
			sql += " )";	
		} else {
			sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?) ";
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);
		}
		sql += " AND p.ID_PROC is not null AND p.ID_USU_FINALIZADOR IS NULL";
		sql += " AND p.DATA_FIM IS NULL";
		sql += " AND  not exists ";    
		sql += " 	(SELECT pa.ID_PEND FROM PROJUDI.PEND_ARQ pa WHERE pa.RESPOSTA = ? and pa.id_pend=p.id_pend)";  	ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? ";																		ps.adicionarLong(id_ServentiaCargo);
		sql += " group by p.ID_PEND_TIPO, pr.ID_SERV_CARGO, pt.PEND_TIPO";
				
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
			
		
		}
		finally{
			 if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	/**
	 * Consulta a quantidade de pendencias do tipo Conclusão que não foram analisadas, de um determinado tipo para o usuario.
	 * - Consulta somente as conclusões que não possuem nenhuma pré-análise registrada
	 * 
	 * @author msapaula
	 * @since 25/05/2009 14:44
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param tipo, tipo da conclusão
	 */
	public List consultarQuantidadeConclusoesNaoAnalisadasPJD(String id_ServentiaCargo, boolean somenteSessao, boolean ehAssistente, boolean ehVotoVencido, boolean ehVirtual) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "SELECT p.ID_PEND_TIPO, pt.PEND_TIPO,  COUNT(*) AS QUANTIDADE FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " INNER JOIN PROJUDI.AUDI_PROC APROC ON APROC.ID_PROC = P.ID_PROC"; 
		sql += " INNER JOIN AUDI A ON A.ID_AUDI = APROC.ID_AUDI ";
		if (somenteSessao){
			//sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?) ";			
			//ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
			sql += " WHERE pt.PEND_TIPO_CODIGO = ? ";
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
			if(ehVotoVencido){
				sql += " AND p.CODIGO_TEMP = ?";
				ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
			} else {
				sql += " AND (p.CODIGO_TEMP IS NULL OR p.CODIGO_TEMP <> ?)";
				ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
				sql += " AND NOT EXISTS (SELECT 1";        
		        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
		        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
		        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
		        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
		        sql += "                   AND ((aci.ID_PEND_VOTO IS NOT NULL AND aci.ID_PEND_VOTO = P.ID_PEND) OR (aci.ID_PEND_VOTO_REDATOR IS NOT NULL AND aci.ID_PEND_VOTO_REDATOR = P.ID_PEND))";
		        
		        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        	if(ehAssistente){
	        		 	sql += " AND EXISTS (SELECT 1";
	        		 	sql += "               FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
	        	        sql += "              WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR) ";
	        	        sql += "               AND pri.ID_SERV_CARGO = ? ";
	        	        ps.adicionarLong(id_ServentiaCargo);       
	        	        sql += "               AND pi.ID_PROC = aci.ID_PROC ";
	        	        sql += "            ) ";
		        	} else {
		        		sql += "               AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
		        		ps.adicionarLong(id_ServentiaCargo);
		        		ps.adicionarLong(id_ServentiaCargo);	        		
		        	}
		        }        
	        	sql += "                   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
	        	ps.adicionarLong(0);
	        	sql += " AND aci.ID_PROC = p.ID_PROC";
	        	sql += " )";
	        	
			}
			sql += " AND EXISTS (  SELECT 1 "
    				+ " FROM PROJUDI.AUDI_PROC AP JOIN PROJUDI.AUDI AD ON AD.ID_AUDI = AP.ID_AUDI "
    				+ " WHERE AP.DATA_MOVI IS NULL AND AP.ID_PROC = P.ID_PROC "
    				+ " AND AP.ID_AUDI_PROC_STATUS = 1 "
    				+ " AND AD.SESSAO_INICIADA "  + (ehVirtual ? " = 1 " : " IS NULL ");			        
			if(!ehVotoVencido){
				sql += " AND (AP.ID_PEND_VOTO = P.ID_PEND OR AP.ID_PEND_VOTO_REDATOR = P.ID_PEND)";	
			}
			sql += " )";	
		} else {
			sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?) ";
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);
		}
		sql += " AND p.ID_PROC is not null AND p.ID_USU_FINALIZADOR IS NULL";
		sql += " AND p.DATA_FIM IS NULL";
		sql += " AND (A.VIRTUAL IS NULL OR A.VIRTUAL = 0 ) ";
		sql += " AND APROC.ID_AUDI_PROC_STATUS = 1 ";
		sql += " AND  not exists ";    
		sql += " 	(SELECT pa.ID_PEND FROM PROJUDI.PEND_ARQ pa WHERE pa.RESPOSTA = ? and pa.id_pend=p.id_pend)";  	ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? ";																		ps.adicionarLong(id_ServentiaCargo);
		sql += " group by p.ID_PEND_TIPO, pr.ID_SERV_CARGO, pt.PEND_TIPO";
				
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
			
		
		}
		finally{
			 if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	/**
	 * Consulta as pendencias do tipo conclusão que estão em aberto na sessão virtual que estão com status de não analisadas,[
	 *	 e que o responsável seja o serventiaCargo passado
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param numeroProcesso, filtro por número de processo
	 * @param id_Classificador, filtro por classificador
	 * @param id_PendenciaTipo, filtro para tipo de conclusão
	 * @since 14/10/2019 15:56
	 * @author lrcampos
	 */
	public List consultarQuantidadeConclusoesNaoAnalisadasVirtual(UsuarioNe usuarioSessao, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean isIniciada, boolean teste) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
			
		String sql = " SELECT DISTINCT  PEN.ID_PEND_TIPO, PEN.PEND_TIPO, COUNT(*) AS QUANTIDADE ";
		sql += " FROM ";
		sql += "   PROJUDI.VIEW_BUSCA_CONCLUSOES_PEND pen  ";
		if (ehVoto && !ehVotoVencido){
			sql += "   INNER JOIN PROJUDI.AUDI_PROC AP on AP.ID_PEND_VOTO = pen.ID_PEND OR AP.ID_PEND_EMENTA = pen.ID_PEND OR AP.ID_PEND_VOTO_REDATOR = pen.ID_PEND OR AP.ID_PEND_EMENTA_REDATOR = pen.ID_PEND ";
			sql += "   INNER JOIN PROJUDI.PROC_TIPO PT ON (AP.ID_PROC_TIPO = PT.ID_PROC_TIPO)  ";
			sql += "   INNER JOIN AUDI A ON A.ID_AUDI = AP.ID_AUDI ";	
		}
		
		sql += " WHERE pen.ID_SERV_CARGO = ?";
		ps.adicionarLong(id_ServentiaCargo);
		
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ?";
			ps.adicionarLong(digitoVerificador);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sql += " AND pen.ID_CLASSIFICADOR = ?";
			ps.adicionarLong(id_Classificador);
		}
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){			
			sql += " AND pen.ID_PEND_TIPO = ?";
			ps.adicionarLong(id_PendenciaTipo);			
			if (ehVoto){
				if(ehVotoVencido){
					sql += " AND pen.CODIGO_TEMP_PEND = ?";	ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					
				} else {
					sql += " AND (pen.CODIGO_TEMP_PEND IS NULL OR pen.CODIGO_TEMP_PEND <> ?)";
					ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
					sql += " AND NOT EXISTS (SELECT 1";        
			        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
			        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
			        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
			        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
			        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
			        sql += "                   AND ((aci.ID_PEND_VOTO IS NOT NULL AND aci.ID_PEND_VOTO = pen.ID_PEND) OR (aci.ID_PEND_VOTO_REDATOR IS NOT NULL AND aci.ID_PEND_VOTO_REDATOR = pen.ID_PEND))";			        						   
			        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE && usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO ) {
		        			sql += "           AND (aci.ID_SERV_CARGO = ? OR aci.ID_SERV_CARGO_REDATOR = ?)";
			        		ps.adicionarLong(id_ServentiaCargo);
			        		ps.adicionarLong(id_ServentiaCargo);	
		        		} else {
		        			sql += " AND EXISTS (SELECT 1";
		        	        sql += "           FROM PEND_RESP pri INNER JOIN PEND pi ON pi.ID_PEND = pri.ID_PEND ";
		        	        sql += "          WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR)";
		        	        sql += "           AND pri.ID_SERV_CARGO = ? ";
		        	        ps.adicionarLong(id_ServentiaCargo);       
		        	        sql += "           AND pi.ID_PROC = aci.ID_PROC ";
		        	        sql += "         ) ";			        		
		        		}
			        }        
		        	sql += "                   AND (NOT aci.ID_ARQ_ATA IS NULL AND aci.ID_ARQ_ATA > ?) ";
		        	ps.adicionarLong(0);
		        	sql += " AND aci.ID_AUDI_PROC = AP.ID_AUDI_PROC";        	
		        	sql += " )";
					//lrcampos 08/08/2019 * Apresentar somente as audiProc com status a Realizar.
					sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
				}
				sql += " AND A.VIRTUAL  = 1 ";
				sql += " AND AP.DATA_MOVI IS NULL ";
				sql += " AND A.SESSAO_INICIADA " + (isIniciada ? " = 1 " : " IS NULL ");
				sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			}
		}
		
		//Teve que agrupar temporariamente pois havia conclusão pendente com mais de um responsável em virtude da importação
		//sql += " group by pen.ID_PEND"; Foi comentado devido a migração do oracle, e em virtude do comentário acima.
		sql += " group by PEN.ID_PEND_TIPO, PEN.PEND_TIPO ";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				pendencias.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return pendencias;
	}
	
	/**
	 * Consulta as pendencias do tipo conclusão que estão em aberto na sessão virtual com status de Aguardando inicio da sessão, 
		e que o responsável seja o serventiaCargo passado
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param numeroProcesso, filtro por número de processo
	 * @param id_Classificador, filtro por classificador
	 * @param id_PendenciaTipo, filtro para tipo de conclusão
	 * @since 14/10/2019 15:56
	 * @author lrcampos
	 */	
	public List consultarQuantidadeConclusoesAguardandoInicioSessaoVirtual(UsuarioNe usuarioSessao, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, boolean ehVoto, boolean ehVotoVencido) throws Exception {
		List preAnalises = new ArrayList();
		StringBuilder sql = new StringBuilder();

		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		ResultSetTJGO rs1 = null;

		sql.append("SELECT ");
		sql.append("DISTINCT PT.ID_PEND_TIPO, ");
		sql.append("PT.PEND_TIPO, ");
		sql.append("COUNT(*) AS QUANTIDADE ");

		sql.append("FROM PROJUDI.PEND P ");

		sql.append("INNER JOIN PROJUDI.PEND_TIPO PT ON P.ID_PEND_TIPO = PT.ID_PEND_TIPO ");
		sql.append("INNER JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ");
		sql.append("INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON P.ID_PEND = APP.ID_PEND ");
		sql.append("INNER JOIN AUDI_PROC AUDIPROC ON AUDIPROC.ID_AUDI_PROC = APP.ID_AUDI_PROC ");
		sql.append("INNER JOIN PROJUDI.PEND_ARQ PA ON (P.ID_PEND = PA.ID_PEND AND PA.RESPOSTA = ? AND PA.CODIGO_TEMP <> ? AND (PA.ID_PEND = AUDIPROC.ID_PEND_VOTO OR PA.ID_PEND = AUDIPROC.ID_PEND_EMENTA OR PA.ID_PEND = AUDIPROC.ID_PEND_VOTO_REDATOR OR PA.ID_PEND = AUDIPROC.ID_PEND_EMENTA_REDATOR)) ");
		ps.adicionarLong(1);
		ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP);
		sql.append("INNER JOIN PROJUDI.SERV_CARGO SC ON (SC.ID_SERV_CARGO = PR.ID_SERV_CARGO AND PR.ID_USU_RESP IS NULL) ");
		sql.append("INNER JOIN PROJUDI.CARGO_TIPO CT ON SC.ID_CARGO_TIPO = CT.ID_CARGO_TIPO ");
		sql.append("INNER JOIN USU_SERV_GRUPO UG ON SC.ID_USU_SERV_GRUPO = UG.ID_USU_SERV_GRUPO ");
		sql.append("INNER JOIN USU_SERV US1 ON US1.ID_USU_SERV = UG.ID_USU_SERV ");
		sql.append("INNER JOIN USU U ON US1.ID_USU = U.ID_USU ");
		sql.append("INNER JOIN PROJUDI.ARQ A ON PA.ID_ARQ = A.ID_ARQ ");
		sql.append("INNER JOIN PROC PRO ON P.ID_PROC = PRO.ID_PROC ");
		sql.append("LEFT JOIN PROJUDI.CLASSIFICADOR C ON C.ID_CLASSIFICADOR = P.ID_CLASSIFICADOR ");
		sql.append("LEFT JOIN AUDI AD ON AUDIPROC.ID_AUDI = AD.ID_AUDI ");
		sql.append("LEFT JOIN AUDI_TIPO ATP ON AD.ID_AUDI_TIPO = ATP.ID_AUDI_TIPO ");
		sql.append("INNER JOIN ( ");
		sql.append("SELECT ");
		sql.append("AP.ID_AUDI_PROC AS ID_AUDI_PROC_VIRTUAL, ");
		sql.append("AU.SESSAO_INICIADA AS SESSAO_INICIADA, ");
		sql.append("AU.VIRTUAL ");
		sql.append("FROM AUDI_PROC AP ");
		sql.append("INNER JOIN AUDI AU ON AU.ID_AUDI = AP.ID_AUDI ");
		sql.append("LEFT JOIN PROJUDI.PEND_VOTO_VIRTUAL PVV ON PVV.ID_AUDI_PROC = AP.ID_AUDI_PROC ");

		sql.append("WHERE AP.DATA_MOVI IS NULL ");
		sql.append("AND AP.ID_AUDI_PROC_STATUS = 1 ");
		sql.append("AND AU.VIRTUAL = 1 ");
		sql.append("AND PVV.ID_PEND_VOTO_VIRTUAL IS NULL ");
		sql.append(") PENDVIRTUAL ON PENDVIRTUAL.ID_AUDI_PROC_VIRTUAL = AUDIPROC.ID_AUDI_PROC ");

		sql.append("WHERE PT.PEND_TIPO_CODIGO = ? ");
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		if (ehVotoVencido) {
			sql.append("AND P.CODIGO_TEMP = ? ");
			ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
		} else {
			sql.append("AND (P.CODIGO_TEMP IS NULL OR P.CODIGO_TEMP <> ?) ");
			ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
		}
		sql.append("AND P.ID_PROC NOT IN ( ");
		sql.append("SELECT PROC.ID_PROC ");
		sql.append("FROM PEND P2 ");
		sql.append("INNER JOIN PROC ON P2.ID_PROC = PROC.ID_PROC ");
		sql.append("INNER JOIN PEND_TIPO PT ON P2.ID_PEND_TIPO = PT.ID_PEND_TIPO ");
		sql.append("WHERE PEND_TIPO_CODIGO IN (?, ?) ");
		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
		sql.append(") ");
		sql.append("AND PENDVIRTUAL.SESSAO_INICIADA = 1 ");
		sql.append("AND PENDVIRTUAL.VIRTUAL = 1 ");
		sql.append("AND P.ID_PROC IS NOT NULL ");
		sql.append("AND P.DATA_FIM IS NULL ");
		sql.append("AND PR.ID_SERV_CARGO = ? ");
		ps.adicionarLong(id_ServentiaCargo);
		sql.append("AND P.ID_PEND_STATUS = ? ");
		ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);
		if (numeroProcesso != null && !numeroProcesso.isEmpty()) {
			sql.append("AND PRO.PROC_NUMERO = ? ");
			ps.adicionarString(numeroProcesso);
		}
		if (id_Classificador != null && !id_Classificador.isEmpty()) {
			sql.append("AND P.ID_CLASSIFICADOR = ? ");
			ps.adicionarString(id_Classificador);
		}

		sql.append("GROUP BY PT.ID_PEND_TIPO, PT.PEND_TIPO ");

		try {
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				preAnalises.add(new String[] { rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
			
			return preAnalises;
		}
	}
	
	/**
	 * Consulta a quantidade de pendencias do tipo Conclusão que não foram analisadas, de um determinado tipo para o usuario.
	 * - Consulta somente as conclusões que não possuem nenhuma pré-análise registrada
	 * 
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param tipo, tipo da conclusão
	 */
	public List consultarQuantidadeConclusoesNaoAnalisadasAssistenteGabinete(String id_ServentiaCargo, boolean somenteSessao, boolean ehAssistente, boolean ehVotoVencido) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "SELECT p.ID_PEND_TIPO, pt.PEND_TIPO, sg.id_serv_grupo, sg.serv_grupo, sg.atividade, COUNT(*) AS QUANTIDADE FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " INNER JOIN PROJUDI.pend_resp_hist prh on p.id_pend = prh.id_pend and prh.data_fim is null ";
		sql += " INNER JOIN PROJUDI.serv_grupo sg on prh.id_serv_grupo = sg.id_serv_grupo ";
		if (somenteSessao){
			sql += " WHERE pt.PEND_TIPO_CODIGO = ? ";
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
			if(ehVotoVencido){
				sql += " AND p.CODIGO_TEMP = ?";
				ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
			} else {
				sql += " AND (p.CODIGO_TEMP IS NULL OR p.CODIGO_TEMP <> ?)";
				ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
				sql += " AND NOT EXISTS (SELECT 1";        
		        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
		        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
		        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
		        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
		        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        	if(ehAssistente){
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
	        	        sql += "          WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR) ";
	        	        sql += "           AND pri.ID_SERV_CARGO = ? ";
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
	        	sql += " AND aci.ID_PROC = p.ID_PROC";        	
	        	sql += " )";
			}
		} else {
			sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?) ";
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);
		}
		sql += " AND p.ID_PROC is not null AND p.ID_USU_FINALIZADOR IS NULL";
		sql += " AND p.DATA_FIM IS NULL";
		sql += " AND  not exists ";    
		sql += " 	(SELECT pa.ID_PEND FROM PROJUDI.PEND_ARQ pa WHERE pa.RESPOSTA = ? and pa.id_pend=p.id_pend)";	ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? ";																		ps.adicionarLong(id_ServentiaCargo);
		sql += " group by p.ID_PEND_TIPO, pr.ID_SERV_CARGO, pt.PEND_TIPO, sg.id_serv_grupo, sg.atividade, sg.serv_grupo ";
		sql += " order by pt.PEND_TIPO, sg.atividade ";
		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"),  rs1.getString("id_serv_grupo") , rs1.getString("atividade"), rs1.getString("QUANTIDADE"), rs1.getString("serv_grupo") });
			}
			
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	/**
	 * Consulta a quantidade de pendências que não foram analisadas, de um determinado tipo para o usuario.
	 * - Consulta somente as pendências que não possuem nenhuma pré-análise registrada
	 * 
	 * @author msapaula
	 * @param id_ServentiaCargo, identificação do serventia cargo
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 */
	public List consultarQuantidadePendenciasNaoAnalisadas(String id_ServentiaCargo, String id_UsuarioServentia) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "SELECT p.ID_PEND_TIPO, pt.PEND_TIPO,  COUNT(*) AS QUANTIDADE FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?) ";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO );
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		sql += " AND p.ID_PROC is not null AND p.ID_USU_FINALIZADOR IS NULL";
		sql += " AND p.DATA_FIM IS NULL";
		sql += " AND  not exists ";    
		sql += " 	(SELECT pa.ID_PEND FROM PROJUDI.PEND_ARQ pa WHERE pa.RESPOSTA = ? and pa.id_pend=p.id_pend)";	ps.adicionarLong(1);
		if (id_ServentiaCargo != null){
			sql += " AND pr.ID_SERV_CARGO = ? ";																	ps.adicionarLong(id_ServentiaCargo);
		} else if (id_UsuarioServentia != null){
			sql += " AND pr.ID_USU_RESP = ? ";																		ps.adicionarLong(id_UsuarioServentia);
		}
		sql += " group by p.ID_PEND_TIPO, pr.ID_SERV_CARGO, pt.PEND_TIPO";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
			
		
		} finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}

	/**
	 * Consulta a quantidade de pendencias do tipo conclusão que estão pré-analisadas por tipo
	 * 
	 * @author msapaula
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param tipo, tipo da pendencia
	 */
	public List consultarQuantidadeConclusoesPreAnalisadas(String id_ServentiaCargo, boolean multiplo, boolean somenteSessao, boolean ehAssistente, boolean ehVotoVencido) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT p.ID_PEND_TIPO, pt.PEND_TIPO, COUNT(*) AS QUANTIDADE FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ? ";	ps.adicionarLong(1); 
		sql += " AND pa.CODIGO_TEMP <> ?)";															ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		if (somenteSessao){
			//sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?)";			
			//ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
			sql += " WHERE pt.PEND_TIPO_CODIGO = ? ";									ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);	
			if(ehVotoVencido){
				sql += " AND p.CODIGO_TEMP = ?";										ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
			} else {
				sql += " AND (p.CODIGO_TEMP IS NULL OR p.CODIGO_TEMP <> ?)";			ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
				sql += " AND NOT EXISTS (SELECT 1";        
		        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
		        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";		        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";       ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
		        sql += "                   AND ((aci.ID_PEND_VOTO IS NOT NULL AND aci.ID_PEND_VOTO = P.ID_PEND) OR (aci.ID_PEND_VOTO_REDATOR IS NOT NULL AND aci.ID_PEND_VOTO_REDATOR = P.ID_PEND))";		        
		        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        	if(ehAssistente){
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
	        	        sql += "          WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR) ";
	        	        sql += "           AND pri.ID_SERV_CARGO = ? ";
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
	        	sql += " AND aci.ID_PROC = p.ID_PROC";
	        	sql += " )";
	        	sql += "AND NOT EXISTS (SELECT 1 FROM PROJUDI.PEND P2 WHERE P2.ID_PROC = p.ID_PROC AND P2.ID_PEND_TIPO IN "
        				+ " (SELECT ID_PEND_TIPO FROM PROJUDI.PEND_TIPO WHERE PEND_TIPO_CODIGO IN (?, ?)) )"; 
        		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
        		ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
        		sql += " AND EXISTS (  SELECT 1 "
        				+ " FROM AUDI_PROC AP JOIN AUDI AD ON AD.ID_AUDI = AP.ID_AUDI "
        				+ " WHERE AP.DATA_MOVI IS NULL AND AP.ID_PROC = P.ID_PROC AND AP.ID_ARQ_ATA IS NULL "
        				+ " AND NVL(AD.SESSAO_INICIADA, 0) =  0)";        	
//				sql += " AND EXISTS (SELECT 1 ";
//		        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
//		        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
//		        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
//		        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
//		        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
//	        	sql += " AND aci.ID_PROC = p.ID_PROC ";
//	        	sql += " AND aci.ID_PEND_VOTO = P.ID_PEND ";
//	        	sql += " AND aci.VIRTUAL " + (virtual ? "=" : "<>") + " ? "; ps.adicionarLong(1);
//	        	sql += " )";
			}
	        
		} else {
			sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?)";
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
		}		
		sql += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(1) FROM PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pa.ID_ARQ ) " + (multiplo ? ">" : "=") + " ?"; ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? "; ps.adicionarLong(id_ServentiaCargo);
		sql += " group by p.ID_PEND_TIPO, pt.PEND_TIPO";		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	public List consultarQuantidadeConclusoesPreAnalisadasBoxPresencialPJD(String id_ServentiaCargo, boolean somenteSessao, boolean ehAssistente, boolean ehVotoVencido, 
			String id_PendenciaTipo) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	
		String sql = "SELECT ID_PEND_TIPO, PEND_TIPO, COUNT(*) QUANTIDADE ";
		sql += " FROM PROJUDI.VIEW_PRE_ANALISE_CONCLUSAO pre ";
			//lrcampos 16/07/2019 * Join com a tabela de Audiencia para verificar se uma audiencia é virtual.
		sql += " INNER JOIN AUDI_PROC AP ON AP.ID_PROC = PRE.ID_PROC ";
		sql += " INNER JOIN AUDI A ON A.ID_AUDI = AP.ID_AUDI ";
		sql += " WHERE pre.DATA_FIM IS NULL";
		sql += " AND (A.VIRTUAL IS NULL OR A.VIRTUAL = 0) ";
		sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PROJUDI.PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pre.ID_ARQ";
		sql += " 		group by pa1.ID_ARQ) = ? ";
		ps.adicionarLong(1);
		sql += " AND pre.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		sql += " AND pre.CODIGO_TEMP <> ? ";
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		if (id_PendenciaTipo != null && id_PendenciaTipo.length() > 0){
			sql += " AND pre.ID_PEND_TIPO = ? ";
			ps.adicionarLong(id_PendenciaTipo);
			
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
			        sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			        sql += " AND (A.VIRTUAL IS NOT NULL OR A.VIRTUAL = 0) ";
			        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
			        	if(ehAssistente){
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
			        		sql += "          WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR) ";
			        		sql += "           AND pri.ID_SERV_CARGO = ? ";
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
		        			+ " AND AD1.SESSAO_INICIADA IS NULL  )";
				}		
			
		}
		
		// alsqueiroz 21/08/2019 * Condição para verificar se o PROC_TIPO não é nulo.
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
	        	if(ehAssistente){
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
	        sql += " AND AP1.ID_PROC = pre.ID_PROC) IS NOT NULL ";

		sql += " group by ID_PEND_TIPO, PEND_TIPO";		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	/**
	 * Consulta a quantidade de pendencias do tipo conclusão que estão pré-analisadas virtual por tipo
	 * 
	 * @author lrcampos
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param tipo, tipo da pendencia
	 * @since 15/10/2019 11:07
	 */
	public List consultarQuantidadeConclusoesPreAnalisadasVirtual(UsuarioNe usuarioSessao, String id_ServentiaCargo, String numeroProcesso, String digitoVerificador, String id_Classificador, String id_PendenciaTipo, boolean ehVoto, boolean ehVotoVencido, boolean ehIniciada) throws Exception {
		StringBuilder sql = new StringBuilder();
		List preAnalises = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;

		sql.append("SELECT ");
			sql.append("DISTINCT PT.ID_PEND_TIPO, ");
			sql.append("PT.PEND_TIPO, ");
			sql.append("COUNT(*) AS QUANTIDADE ");

		sql.append("FROM PROJUDI.PEND P ");

		sql.append("INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND ");
		sql.append("INNER JOIN PROJUDI.PEND_ARQ PA ON (P.ID_PEND = PA.ID_PEND AND PA.RESPOSTA = ? AND PA.CODIGO_TEMP <> ?) ");
		ps.adicionarLong(1);
		ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP);
		sql.append("INNER JOIN PROJUDI.PEND_TIPO PT ON P.ID_PEND_TIPO = PT.ID_PEND_TIPO ");
		sql.append("INNER JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ");
		sql.append("INNER JOIN PROJUDI.SERV_CARGO SC ON (SC.ID_SERV_CARGO = PR.ID_SERV_CARGO AND PR.ID_USU_RESP IS NULL) ");
		sql.append("INNER JOIN PROJUDI.CARGO_TIPO CT ON SC.ID_CARGO_TIPO = CT.ID_CARGO_TIPO ");
		sql.append("INNER JOIN USU_SERV_GRUPO UG ON SC.ID_USU_SERV_GRUPO = UG.ID_USU_SERV_GRUPO ");
		sql.append("INNER JOIN USU_SERV US1 ON US1.ID_USU_SERV = UG.ID_USU_SERV ");
		sql.append("INNER JOIN USU U ON US1.ID_USU = U.ID_USU ");
		sql.append("INNER JOIN AUDI_PROC AUDIPROC ON AUDIPROC.ID_AUDI_PROC = APP.ID_AUDI_PROC ");
		sql.append("INNER JOIN AUDI AD ON AD.ID_AUDI = AUDIPROC.ID_AUDI ");
		sql.append("INNER JOIN PROJUDI.ARQ A ON PA.ID_ARQ = A.ID_ARQ ");
		sql.append("INNER JOIN PROC PRO ON P.ID_PROC = PRO.ID_PROC ");
		
		sql.append("LEFT JOIN     (SELECT DISTINCT ID_PEND_VOTO AS ID_PEND_VOTO_VIRTUAL FROM AUDI ADV            ");
		sql.append(" INNER JOIN AUDI_PROC APV ON ADV.ID_AUDI = APV.ID_AUDI                                       ");
		sql.append(" INNER JOIN PROJUDI.PEND_VOTO_VIRTUAL PVV ON APV.ID_AUDI_PROC = PVV.ID_AUDI_PROC             ");
		sql.append(" INNER JOIN PROJUDI.AUDI_PROC_PEND APPV ON APV.ID_AUDI_PROC = APPV.ID_AUDI_PROC              ");
		sql.append(" INNER JOIN AUDI_TIPO ATPV ON ADV.ID_AUDI_TIPO = ATPV.ID_AUDI_TIPO                           ");
		sql.append(" INNER JOIN AUDI_PROC_STATUS APSV ON APV.ID_AUDI_PROC_STATUS = APSV.ID_AUDI_PROC_STATUS      ");
		sql.append(" LEFT JOIN PEND PENDV ON APPV.ID_PEND = PENDV.ID_PEND                                        ");
		sql.append(" LEFT JOIN PEND_RESP PRV ON PRV.ID_PEND = PENDV.ID_PEND                                      ");
		sql.append(" WHERE ATPV.AUDI_TIPO_CODIGO = ?                                                            ");
		ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		sql.append("   AND APSV.AUDI_PROC_STATUS_CODIGO = ?                                                     ");
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql.append("   AND PRV.ID_SERV_CARGO = ?                                                           ");
		ps.adicionarLong(id_ServentiaCargo);
		
		sql.append("   AND APPV.ID_AUDI_PROC = APV.ID_AUDI_PROC                                                  ");
		sql.append(") TABELA_PEND_VOTO_VIRTUAL ON APP.ID_PEND = TABELA_PEND_VOTO_VIRTUAL.ID_PEND_VOTO_VIRTUAL    ");

		sql.append("WHERE PT.PEND_TIPO_CODIGO = ? ");
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		if(ehVotoVencido){
			sql.append("AND P.CODIGO_TEMP = ? ");
			ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
		} else {
			sql.append("AND (P.CODIGO_TEMP IS NULL OR P.CODIGO_TEMP <> ?) ");
			ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
		}
		sql.append("AND NOT EXISTS ( ");
		sql.append("SELECT ");
			sql.append("1 ");
		sql.append("FROM PROJUDI.PEND P2 ");
		sql.append("INNER JOIN PROJUDI.AUDI_PROC_PEND APP1 ON APP1.ID_PEND = P2.ID_PEND ");
		sql.append("WHERE APP1.ID_AUDI_PROC = APP.ID_AUDI_PROC ");
		sql.append("AND P2.ID_PEND_TIPO IN ( ");
			sql.append("SELECT ID_PEND_TIPO FROM PROJUDI.PEND_TIPO WHERE PEND_TIPO_CODIGO IN (?, ?, ?) ");
		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
		ps.adicionarLong(PendenciaTipoDt.RESULTADO_VOTACAO);
		sql.append(") ");
		sql.append(") ");
		
		sql.append("AND P.ID_PROC IS NOT NULL ");

		sql.append("AND P.DATA_FIM IS NULL ");

		sql.append("AND ( ");
			sql.append("SELECT ");
				sql.append("COUNT(1) ");
			sql.append("FROM PROJUDI.PEND_ARQ PA1 ");
			sql.append("WHERE PA1.ID_ARQ = PA.ID_ARQ ");
		sql.append(") = 1 ");

		sql.append("AND PR.ID_SERV_CARGO = ? ");
		ps.adicionarLong(id_ServentiaCargo);

		sql.append("AND P.ID_PEND_STATUS <> ? ");
		ps.adicionarLong(PendenciaStatusDt.ID_PRE_ANALISADA);

		sql.append("AND AD.VIRTUAL = 1 ");
		if (ehIniciada) {
			sql.append("AND AD.SESSAO_INICIADA = 1 ");
		} else {
			sql.append("AND (AD.SESSAO_INICIADA = 0 OR AD.SESSAO_INICIADA IS NULL) ");
		}
		sql.append("AND AUDIPROC.DATA_MOVI IS NULL ");
		sql.append("GROUP BY PT.ID_PEND_TIPO, PT.PEND_TIPO ");
		
		try {
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				preAnalises.add(new String[] { rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}
	
		return preAnalises;
	}
	
	
	/**
	 * Consulta a quantidade de pendencias do tipo conclusão que estão pré-analisadas por tipo
	 * 
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param tipo, tipo da pendencia
	 */
	public List consultarQuantidadeConclusoesPreAnalisadasAssistenteGabinete(String id_ServentiaCargo, boolean multiplo, boolean somenteSessao, boolean ehAssistente, boolean ehVotoVencido) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT p.ID_PEND_TIPO, pt.PEND_TIPO, sg.id_serv_grupo,  sg.atividade, COUNT(*) AS QUANTIDADE FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ? AND pa.CODIGO_TEMP <> ?)";
		ps.adicionarLong(1);
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " INNER JOIN PROJUDI.pend_resp_hist prh on p.id_pend = prh.id_pend and prh.data_fim is null";
		sql += " INNER JOIN PROJUDI.serv_grupo sg on prh.id_serv_grupo = sg.id_serv_grupo";
		if (somenteSessao){
			sql += " WHERE pt.PEND_TIPO_CODIGO = ? ";
			ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);	
			if(ehVotoVencido){
				sql += " AND p.CODIGO_TEMP = ?";
				ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
			} else {
				sql += " AND (p.CODIGO_TEMP IS NULL OR p.CODIGO_TEMP <> ?)";
				ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
				sql += " AND NOT EXISTS (SELECT 1";        
		        sql += "                   FROM PROJUDI.VIEW_AUDI_COMPLETA aci";
		        sql += "                  WHERE aci.AUDI_TIPO_CODIGO = ? ";
		        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		        sql += "                   AND aci.AUDI_PROC_STATUS_CODIGO = ? ";
		        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		        sql += "                   AND aci.ID_PROC IS NOT NULL AND aci.DATA_MOVI_AUDI_PROC IS NULL";
		        if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
		        	if(ehAssistente){
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
	        	        sql += "          WHERE (pri.ID_PEND = aci.ID_PEND_VOTO OR pri.ID_PEND = aci.ID_PEND_VOTO_REDATOR) ";
	        	        sql += "           AND pri.ID_SERV_CARGO = ? ";
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
	        	sql += " AND aci.ID_PROC = p.ID_PROC";        	
	        	sql += " )";
			}
	        
		} else {
			sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?)";
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
		}		
		sql += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pa.ID_ARQ group by pa1.ID_ARQ) " + (multiplo ? ">" : "=") + " ?";
		ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		sql += " group by p.ID_PEND_TIPO, pt.PEND_TIPO, sg.id_serv_grupo, sg.atividade";		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("id_serv_grupo"), rs1.getString("atividade"), rs1.getString("QUANTIDADE") });
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	/**
	 * Consulta a quantidade de pendencias do tipo conclusão que estão pré-analisadas por tipo
	 *  e pendentes de assinatura.
	 * @author mmgomes
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param multiplo, indicador se é múltiplo
	 * @param ehVotoVencido, indica se é voto vencido
	 */
	public List consultarQuantidadeConclusoesPreAnalisadasPendentesAssinatura(String id_ServentiaCargo, boolean multiplo, boolean ehVotoVencido) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT p.ID_PEND_TIPO, pt.PEND_TIPO, COUNT(*) AS QUANTIDADE FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ? AND pa.CODIGO_TEMP = ?)";
		ps.adicionarLong(1);
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";		
		if (ehVotoVencido){
			sql += " WHERE pt.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
			sql += " AND p.CODIGO_TEMP = ?";		  ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
		} else {
			sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?)";
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
		}		
		sql += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pa.ID_ARQ group by pa1.ID_ARQ) " + (multiplo ? ">" : "=") + "?";
		ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? ";	
		ps.adicionarLong(id_ServentiaCargo);
		sql += " group by p.ID_PEND_TIPO, pt.PEND_TIPO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	/**
	 * Consulta a quantidade de pendencias que estão pré-analisadas por tipo. Trata os tipos de pendências semelhantes às conclusões,
	 * que terão análise e pré-análise
	 * 
	 * @author msapaula
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param tipo, tipo da pendencia
	 */
	public List consultarQuantidadePendenciasPreAnalisadas(String id_ServentiaCargo, String id_UsuarioServentia, boolean multiplo) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT p.ID_PEND_TIPO, pt.PEND_TIPO, COUNT(*) AS QUANTIDADE FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ? AND pa.CODIGO_TEMP <> ?)";
		ps.adicionarLong(1);
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		sql += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pa.ID_ARQ group by pa1.ID_ARQ) " + (multiplo ? ">" : "=") + "?";
		ps.adicionarLong(1);
		if (id_ServentiaCargo != null){
			sql += " AND pr.ID_SERV_CARGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
		} else if (id_UsuarioServentia != null){
			sql += " AND pr.ID_USU_RESP = ? ";
			ps.adicionarLong(id_UsuarioServentia);
		}
		sql += " group by p.ID_PEND_TIPO, pt.PEND_TIPO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	/**
	 * Consulta a quantidade de pendencias que estão pré-analisadas por tipo e pendentes de assinatura. Trata os tipos de pendências semelhantes às conclusões,
	 * que terão análise e pré-análise
	 * 
	 * @author mmgomes
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param tipo, tipo da pendencia
	 */
	public List consultarQuantidadePendenciasPreAnalisadasPendentesAssinatura(String id_ServentiaCargo, String id_UsuarioServentia, boolean multiplo) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT p.ID_PEND_TIPO, pt.PEND_TIPO, COUNT(*) AS QUANTIDADE FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ? AND pa.CODIGO_TEMP = ?)";
		ps.adicionarLong(1);
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		sql += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pa.ID_ARQ group by pa1.ID_ARQ) " + (multiplo ? ">" : "=") + "?";
		ps.adicionarLong(1);
		if (id_ServentiaCargo != null){
			sql += " AND pr.ID_SERV_CARGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
		} else if (id_UsuarioServentia != null){
			sql += " AND pr.ID_USU_RESP = ? ";
			ps.adicionarLong(id_UsuarioServentia);
		}
		sql += " group by p.ID_PEND_TIPO, pt.PEND_TIPO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("PEND_TIPO"), rs1.getString("QUANTIDADE") });
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}

	/**
	 * Consulta pendencias do tipo Intimação e Carta de Citação para o advogado
	 * informado para que seja montada a página inicial do Advogado. Retorna a
	 * informação se o advogado é principal ou não
	 * 
	 * @author lsbernardes, msapaula
	 * @since 10/06/2009
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de pendencias do advogado
	 * @throws Exception
	 */
	public List consultarIntimacoesCitacoesAdvogado(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT u.*, pa.PRINC FROM PROJUDI.VIEW_PEND_ATIVAS_RESP u ";
		sql += " LEFT JOIN PROJUDI.PROC_PARTE pp on u.ID_PROC = pp.ID_PROC";
		sql += " 	AND pp.ID_PROC_PARTE = u.ID_PROC_PARTE";
		sql += " LEFT JOIN PROJUDI.PROC_PARTE_ADVOGADO pa on pp.ID_PROC_PARTE=pa.ID_PROC_PARTE AND pa.DATA_SAIDA IS NULL";
		sql += "	AND u.ID_USU_RESP=pa.ID_USU_SERV";
		sql += " WHERE ( u.ID_USU_RESP = ? ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());

		if (usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe() != null && !usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR u.ID_USU_RESP = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
		}
		sql += " ) AND (u.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		sql += " OR u.PEND_TIPO_CODIGO = ? )";
		ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		
		sql += " ORDER BY pa.PRINC desc, u.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setAdvogadoPrincipal(Funcoes.StringToBoolean(Funcoes.FormatarLogico(rs1.getString("PRINC"))));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId(rs1.getString("ID_PROC"));
				processoDt.setHash(usuarioNe.getCodigoHash(processoDt.getId()));
				pendenciaDt.setProcessoDt(processoDt);
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
	 * Consulta pendencias do tipo Intimação ou citação do id_usuario_serventia informado
	 * 
	 * @author lsbernardes
	 * @since 25/03/2011
	 * @param String id  usuario serventia
	 * @return lista de pendencias
	 * @throws Exception
	 */
	public List consultarIntimacoesCitacoesUsuarioServentia(String id_Processo, String Id_UsuarioServentia) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT u.*, pa.PRINC FROM VIEW_PEND_ATIVAS_RESP u ";
		sql += " LEFT JOIN PROJUDI.PROC_PARTE pp on u.ID_PROC = pp.ID_PROC";
		sql += " 	AND pp.ID_PROC_PARTE = u.ID_PROC_PARTE";
		sql += " LEFT JOIN PROJUDI.PROC_PARTE_ADVOGADO pa on pp.ID_PROC_PARTE=pa.ID_PROC_PARTE AND pa.DATA_SAIDA IS NULL";
		sql += "	AND u.ID_USU_RESP=pa.ID_USU_SERV";
		sql += " WHERE ( u.ID_USU_RESP = ? ";
		ps.adicionarLong(Id_UsuarioServentia);
		sql += " ) AND (u.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		sql += " OR u.PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		sql += " AND u.ID_PROC = ? ";
		ps.adicionarLong(id_Processo);
		
		sql += " ORDER BY pa.PRINC desc, u.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setAdvogadoPrincipal(Funcoes.StringToBoolean(Funcoes.FormatarLogico(rs1.getString("PRINC"))));
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoDt(processoDt);
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}

	/**
	 * Consulta pendencias do tipo Intimação para o cargo promotor informado
	 * para que seja montada a página inicial do Promotor.
	 * 
	 * @author lsbernardes
	 * @since 27/09/2009
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de pendencias do promotor
	 * @throws Exception
	 */
	public List consultarIntimacoesPromotor(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT u.* FROM VIEW_PEND_ATIVAS_RESP u ";
		sql += " WHERE  u.ID_SERV_CARGO = ? ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargo());
		sql += " AND u.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		sql += " ORDER BY u.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
	 * Consulta pendencias do tipo Intimação para o cargo promotor chefe informado
	 * para que seja montada a página inicial do Assistente.
	 * 
	 * @author lsbernardes
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de pendencias do promotor
	 * @throws Exception
	 */
	public List consultarIntimacoesPromotorChefe(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT u.* FROM VIEW_PEND_ATIVAS_RESP u ";
		sql += " WHERE  u.ID_SERV_CARGO = ? ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
		sql += " AND u.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		sql += " ORDER BY u.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
     * Consulta pendencias do tipo Intimação de todos os procuradores da procuradoria
     * do usuário logado
	 * 
	 * @author asrocha
	 * @since 22/10/2010
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de intimações de todos os procuradores da procuradoria
	 * @throws Exception
	 */
	public List consultarIntimacoesProcuradoria(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;	
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = " SELECT distinct P.ID_PEND, P.ID_PROC, (PRO.PROC_NUMERO || '.'|| PRO.DIGITO_VERIFICADOR) AS PROC_NUMERO_COMPLETO,"
				+ " M.ID_MOVI, Mt.MOVI_TIPO, Pt.PEND_TIPO, P.DATA_INICIO, P.DATA_LIMITE, PS.PEND_STATUS "
		+ " FROM Serv S  "
		+ " JOIN Usu_Serv Us ON S.Id_Serv = Us.Id_Serv  "
		+ " JOIN usu u ON us.id_usu= u.id_usu  "
		+ " JOIN Pend_Resp Pr ON Pr.Id_Usu_Resp = Us.Id_Usu_Serv  "
		+ " JOIN Pend P   ON Pr.Id_Pend = P.Id_Pend " 
		+ " JOIN Pend_Tipo Pt ON P.Id_Pend_Tipo = Pt.Id_Pend_Tipo"
		+ " JOIN PROC Pro ON Pro.Id_Proc = P.Id_Proc "
		+ " JOIN Movi M ON M.Id_Movi = P.Id_Movi "
		+ " JOIN Movi_Tipo Mt ON Mt.Id_Movi_Tipo = M.Id_Movi_Tipo "
		+ " JOIN Pend_Status Ps ON Ps.Id_Pend_Status  = P.Id_Pend_Status "
		+ " WHERE S.ID_SERV = ? AND ( P.CODIGO_TEMP <> ? OR P.CODIGO_TEMP is null ) AND PT.PEND_TIPO_CODIGO = ? AND P.DATA_FIM IS NULL ";		
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoDt(new ProcessoDt());
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				//pendenciaDt.setProcurador(rs1.getString("NOME"));
				//pendenciaDt.setId_Procurador(rs1.getString("ID_USU_SERV"));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.getProcessoDt().setHash(usuarioNe.getCodigoHash(pendenciaDt.getId_Processo()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	 /**
     * Método que consulta as pendências do tipo Carta de Citação de todos os advogados
     * do Escritório Jurídico do usuário logado 
     * 
     * @param usuarioNe usuário da sessão
     * @return lista de pendências do tipo carta de citação
     * @throws Exception
     * @author hmgodinho
     */
	public List consultarCitacoesEscritorioJuridicoProcuradoria(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;	
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = " SELECT distinct ID_PEND, ID_PROC, PROC_NUMERO_COMPLETO, ID_MOVI, MOVI_TIPO, PEND_TIPO, DATA_INICIO, DATA_LIMITE, PEND_STATUS FROM VIEW_PEND_CITACOES_ESC WHERE ID_SERV = ? AND ( CODIGO_TEMP <> ? OR CODIGO_TEMP is null ) AND DATA_FIM IS NULL ";		
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoDt(new ProcessoDt());
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.getProcessoDt().setHash(usuarioNe.getCodigoHash(pendenciaDt.getId_Processo()));
				
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
     * Consulta pendencias do tipo Intimação de todos os promotoroes da promotoria
     * do usuário logado
	 * 
	 * @author lsbernardes
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de intimações de todos os procuradores da procuradoria
	 * @throws Exception
	 */
	public List consultarIntimacoesPromotoria(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;	
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = " SELECT * FROM VIEW_PEND_INTIMACOES_PROM " +
				"WHERE ID_SERV = ? AND ( CODIGO_TEMP <> ? OR CODIGO_TEMP is null ) AND DATA_FIM IS NULL ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoDt(new ProcessoDt());
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setNomeParte(rs1.getString("NOME_PARTE"));
				pendenciaDt.setPromotor(rs1.getString("NOME"));
				pendenciaDt.setId_Promotor(rs1.getString("ID_SERV_CARGO"));
				pendenciaDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoNumeroCompleto(rs1.getString("PROC_NUMERO_CNJ"));
				
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.getProcessoDt().setHash(usuarioNe.getCodigoHash(pendenciaDt.getId_Processo()));
				
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
	 * Consulta pendencias do distribuidor de gabinete para o cargo informado
	 * para que seja montada a página inicial do distribuidor.
	 * 
	 * @author lsbernardes
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de pendencias do distribuidor
	 * @throws Exception
	 */
	public List consultarPendenciasDistribuidorGabinete(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT p.* FROM PROJUDI.VIEW_PEND p";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " INNER JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sql += " INNER JOIN PROJUDI.SERV s on sc.ID_SERV = s.ID_SERV";
		sql += " INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
		sql += " WHERE  ct.CARGO_TIPO_CODIGO = ? AND s.ID_SERV = ? AND p.DATA_FIM IS NULL ";
		ps.adicionarLong(CargoTipoDt.DISTRIBUIDOR_GABINETE);
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		sql += " AND p.PEND_TIPO_CODIGO IN (?,?,?, ?, ?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		sql += " ORDER BY p.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
	 * Consulta pendencias do tipo Conclusão que estão para o distribuidor de gabinete,
	 * para que seja montada a página inicial do distribuidor.
	 * 
	 * @author msapaula
	 * @param usuarioNe, usuario da sessao
	 * @return lista de pendencias do distribuidor
	 * @throws Exception
	 */
	public List consultarConclusoesDistribuidorGabinete(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql= "SELECT * FROM PROJUDI.VIEW_PEND_CONC_SERV_CARGO p";
		sql += " WHERE  p.CARGO_TIPO_CODIGO = ? AND p.ID_SERV = ?";
		ps.adicionarLong(CargoTipoDt.DISTRIBUIDOR_GABINETE);
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		sql += " ORDER BY p.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.setId_Classificador(rs1.getString("Id_Classificador"));
				if (rs1.getString("CLASSIFICADOR") != null){
					pendenciaDt.setClassificador(rs1.getString("CLASSIFICADOR") + " - (Prioridade: " + rs1.getString("PRIOR_CLASSIFICADOR") + ")");
				}
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
	 * Consulta pendencias do tipo Intimação para o cargo promotor informado que estão aguardando parecer
	 * para que seja montada a página inicial do Promotor.
	 * 
	 * @author lsbernardes
	 * @since 26/04/2010
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de pendencias do promotor
	 * @throws Exception
	 */
	public List consultarIntimacoesPromotorAguardandoParecer(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT DISTINCT u.ID_PEND, u.PEND,u.ID_MOVI ,u.MOVI, u.ID_PROC, " +
		"u.PROC_NUMERO_COMPLETO, u.DATA_FIM, u.DATA_LIMITE ,u.DATA_INICIO, u.PROC_PRIOR_CODIGO  " +
		"FROM VIEW_PEND_INTIMA_AGUAR_PARECER u ";
		
		if ( usuarioNe.isAssessorMP() ){
			sql += " WHERE  u.ID_SERV_CARGO = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
		} else {
			sql += " WHERE  u.ID_SERV_CARGO = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargo());
		}
		
		sql += " AND u.CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_PARECER_CODIGO_TEMP);
		sql += " AND u.ID_PEND_TIPO = ? ";
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		sql += " ORDER BY u.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setDataFim( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setProcessoPrioridadeCodigo( rs1.getString("PROC_PRIOR_CODIGO"));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	public List consultarIntimacoesLidasAutomaticamentePromotorAguardandoParecer(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT DISTINCT u.ID_PEND, u.PEND,u.ID_MOVI ,u.MOVI, u.ID_PROC, " +
		"u.PROC_NUMERO_COMPLETO, u.DATA_FIM, u.DATA_LIMITE ,u.DATA_INICIO  " +
		"FROM VIEW_PEND_INTIMA_AGUAR_PARECER u ";
		
		if ( usuarioNe.isAssessorMP()){
			sql += " WHERE  u.ID_SERV_CARGO = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
		} else {
			sql += " WHERE  u.ID_SERV_CARGO = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargo());
		}
		
		sql += " AND u.CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP);
		sql += " AND u.ID_PEND_TIPO = ? ";
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		sql += " ORDER BY u.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setDataFim( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	public List consultarIntimacoesAdvogadoAguardandoPeticionamento(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT DISTINCT u.ID_PEND, u.PEND,u.ID_MOVI ,u.MOVI, u.ID_PROC, " +
		"u.PROC_NUMERO_COMPLETO, u.DATA_FIM, u.DATA_LIMITE ,u.DATA_INICIO  " +
		"FROM VIEW_PEND_INTIMA_AGUAR_PARECER u ";
		
		if ( usuarioNe.isAssessorAdvogado()){
			sql += " WHERE  u.ID_USU_RESP = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
		} else {
			sql += " WHERE  u.ID_USU_RESP = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		}
		
		sql += " AND u.CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_PARECER_CODIGO_TEMP);
		sql += " AND ( u.ID_PEND_TIPO = ? OR u.ID_PEND_TIPO = ?)";
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		sql += " ORDER BY u.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setDataFim( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	public List consultarIntimacoesLidasAutomaticamenteAdvogadoAguardandoPeticionamento(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT DISTINCT u.ID_PEND, u.PEND,u.ID_MOVI ,u.MOVI, u.ID_PROC, " +
				"u.PROC_NUMERO_COMPLETO, u.DATA_FIM, u.DATA_LIMITE ,u.DATA_INICIO, u.PROC_PRIOR_CODIGO  " +
				"FROM VIEW_PEND_INTIMA_AGUAR_PARECER u ";
		
		if ( usuarioNe.isAssessorAdvogado() ){
			sql += " WHERE  u.ID_USU_RESP = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
		} else {
			sql += " WHERE  u.ID_USU_RESP = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		}
		
		sql += " AND u.CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_PARECER_LEITURA_AUTOMATICA_CODIGO_TEMP);
		sql += " AND ( u.ID_PEND_TIPO = ? OR u.ID_PEND_TIPO = ?)" ;
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		sql += " ORDER BY u.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setDataFim( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	public List consultarIntimacoesPublicadasDiarioEletronicoAdvogadoAguardandoPeticionamento(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT DISTINCT u.ID_PEND, u.PEND,u.ID_MOVI ,u.MOVI, u.ID_PROC, " +
				"u.PROC_NUMERO_COMPLETO, u.DATA_FIM, u.DATA_LIMITE ,u.DATA_INICIO  " +
				"FROM VIEW_PEND_INTIMA_AGUAR_PARECER u ";
		
		if ( usuarioNe.isAssessorAdvogado() ){
			sql += " WHERE  u.ID_USU_RESP = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentiaChefe());
		} else {
			sql += " WHERE  u.ID_USU_RESP = ? ";
			ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		}
		
		sql += " AND u.CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_PARECER_DIARIO_ELETRONICO_CODIGO_TEMP);
		sql += " AND ( u.ID_PEND_TIPO = ? OR u.ID_PEND_TIPO = ?)" ;
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		sql += " ORDER BY u.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendenciaDt.setId_Movimentacao( rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao( rs1.getString("MOVI"));
				pendenciaDt.setId_Processo( rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero( rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setDataFim( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}

	/**
	 * Consulta a quantidade de pendencias em andamento de um determinado tipo
	 * para o usuario - Consulta inclusive para a serventia, para o tipo de
	 * serventia, para o cargo do usuario, para o usuario resposavel
	 * 
	 * @author Leandro Bernardes
	 * @since 03/12/2008 10:34
	 * @param UsuarioDt
	 *            usuarioDt, usuario que deseja a quantidade
	 * @param String
	 *            tipo, tipo da pendencia
	 * @return int
	 * @throws Exception
	 */
	public int consultarQuantidadePendenciasAdvogadosEmAndamento(UsuarioDt usuarioDt, String tipo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PEND_ATIVAS_RESP u ";
		sql += " WHERE ( u.ID_USU_RESP = ? ";
		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());

		if (usuarioDt.getId_UsuarioServentiaChefe() != null && !usuarioDt.getId_UsuarioServentiaChefe().equalsIgnoreCase("")) {
			sql += " OR u.ID_USU_RESP = ? ";
			ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
		}
		sql += " ) AND  u.ID_PEND_TIPO = ? ";
		ps.adicionarLong(tipo);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				return rs1.getInt("QUANTIDADE");
			} else return 0;
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
	}

	/**
	 * Consulta a estatistica dos tipos das pendencias ativas para a página
	 * Inicial do advogado. Nessa consulta serão desconsideradas as pendências
	 * do tipo Intimação e Carta de Citação pois essas são tratadas de forma
	 * diferente.
	 * 
	 * @author msapaula
	 * @since 15/06/2009
	 * @param usuarioDt
	 *            usuario de referencia
	 */
	public Map consultarTiposAtivasPaginaInicalAdvogado(UsuarioDt usuarioDt) throws Exception {
		Map est = new HashMap();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlSel = "SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO, COUNT(*) as QTD ";
		sqlSel += " FROM PROJUDI.VIEW_PEND_ATIVAS_RESP pa WHERE ";
		sqlSel += " ( pa.ID_SERV IS NULL AND pa.ID_SERV_TIPO IS NULL AND pa.ID_USU_RESP = ?) ";
		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
		sqlSel += " AND pa.PEND_TIPO_CODIGO <> ? ";
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		sqlSel += " AND pa.PEND_TIPO_CODIGO <> ? ";
		ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		sqlSel += " group by pa.ID_PEND_TIPO, pa.PEND_TIPO ORDER BY pa.PEND_TIPO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sqlSel, ps);
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QTD") });
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		

		return est;
	}
	
	public Map consultarTiposAtivasPaginaInicalAssistenteAdvogado(UsuarioDt usuarioDt) throws Exception {
		Map est = new HashMap();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sqlSel = "SELECT pa.ID_PEND_TIPO, pa.PEND_TIPO, COUNT(*) as QTD ";
		sqlSel += " FROM PROJUDI.VIEW_PEND_ATIVAS_RESP pa WHERE ";
		sqlSel += " ( pa.ID_SERV IS NULL AND pa.ID_SERV_TIPO IS NULL AND (pa.ID_USU_RESP = ? OR pa.ID_USU_RESP = ?) ) ";
		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
		ps.adicionarLong(usuarioDt.getId_UsuarioServentiaChefe());
		sqlSel += " AND pa.PEND_TIPO_CODIGO <> ? ";
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		sqlSel += " AND pa.PEND_TIPO_CODIGO <> ? ";
		ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);
		sqlSel += " group by pa.ID_PEND_TIPO, pa.PEND_TIPO ORDER BY pa.PEND_TIPO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sqlSel, ps);
			while (rs1.next()) {
				est.put(rs1.getString("PEND_TIPO"), new String[] {rs1.getString("ID_PEND_TIPO"), rs1.getString("QTD") });
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return est;
	}

	/**
	 * Consultar se há pendências ativas para um ServentiaCargo, ou seja,
	 * qualquer pendência em aberto (até mesmo as reservadas)
	 * 
	 * @param id_ServentiaCargo,
	 *            identificação do ServentiaCargo
	 * @author msapaula
	 * @since 20/08/2009 10:44
	 */
	public int consultarPendenciasAtivasServentiaCargo(String id_ServentiaCargo) throws Exception {
		int qtde = 0;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT COUNT(*) as qtde FROM PROJUDI.VIEW_PEND_ATIVAS_RESP p ";
		sql += " WHERE p.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);

		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) qtde = rs1.getInt("qtde");
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return qtde;
	}
	
	public int consultarPendenciasAtivasServentiaCargo(String id_ServentiaCargo, int pendenciaTipoCodigo) throws Exception {
		int qtde = 0;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT COUNT(*) as qtde FROM PROJUDI.VIEW_PEND_ATIVAS_RESP p JOIN PROJUDI.PEND_TIPO t ON  p.ID_PEND_TIPO = t.ID_PEND_TIPO";
		sql += " WHERE p.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		sql += " AND t.PEND_TIPO_CODIGO = ? ";
		ps.adicionarLong(pendenciaTipoCodigo);

		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) qtde = rs1.getInt("qtde");
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return qtde;
	}

	/**
	 * Método que verifica se há pendências do tipo conclusão em aberto para o
	 * ServentiaCargo passado.
	 * 
	 * @param id_ServentiaCargo,
	 *            identificação de id_ServentiaCargo
	 * 
	 * @author msapaula
	 */
	public int consultarConclusoesAbertasServentiaCargo(String id_ServentiaCargo) throws Exception {
		int qtde = 0;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT COUNT(*) as qtde FROM PROJUDI.VIEW_PEND_ABER_RESP_SERV_CARGO pen";
		sql += " WHERE pen.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) qtde = rs1.getInt("qtde");

		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return qtde;
	}
	/**
	 * Consulta as pendencias de um determinado tipo
	 * 
	 * @author Leandro Bernardes
	 * @since 03/09/2009
	 * @param String
	 *            idProcesso, id do processo que terá suas pendencias abertas
	 *            consultadas
	 * @param String
	 *            idServentiaCargo, id do serventia cargo que terá suas
	 *            pendencias abertas consultadas
	 * @return List
	 * @throws Exception
	 */
	public List<PendenciaDt> consultarPendenciasAbertasProcessoPJD(String idProcesso, String idServentiaCargo)
			throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "SELECT * FROM VIEW_PEND_ABERTAS_RESP par WHERE par.ID_PROC = ? AND par.ID_SERV_CARGO = ? ";
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(idServentiaCargo);
		try {
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendencias.add(pendenciaDt);
			}
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e1) {
			}
		}

		return pendencias;
	}

	/**
	 * Consulta as pendencias de um determinado tipo
	 * 
	 * @author Leandro Bernardes
	 * @since 03/09/2009
	 * @param String
	 *            idProcesso, id do processo que terá suas pendencias abertas
	 *            consultadas
	 * @param String
	 *            idServentiaCargo, id do serventia cargo que terá suas
	 *            pendencias abertas consultadas
	 * @return List
	 * @throws Exception
	 */
	public List consultarPendenciasAbertasProcesso(String idProcesso, String idServentiaCargo) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT ID_PEND, PEND FROM VIEW_PEND_ABERTAS_RESP par WHERE par.ID_PROC = ? AND par.ID_SERV_CARGO = ? ";
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(idServentiaCargo);
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendencias.add(pendenciaDt);
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}
	
	
	/**
     * Método que consulta todas as pendências abertas de um processo.
     * @param idProcesso - ID do processo
     * @return lista de pendências
     * @throws Exception
     * @author hmgodinho
     */
	public List consultarPendenciasAbertasProcesso(String idProcesso) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT ID_PEND, PEND, ID_PEND_TIPO, PEND_TIPO, PEND_TIPO_CODIGO FROM VIEW_PEND pa WHERE pa.ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setPendenciaTipoCodigo(rs1.getString("PEND_TIPO_CODIGO"));
				pendencias.add(pendenciaDt);
			}
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}
	
	/**
	 * Consulta intimaçoes de um promotor (substituto processual)
	 * 
	 * @author Leandro Bernardes
	 * @param String
	 *            idProcesso, id do processo que terá suas pendencias abertas
	 *            consultadas
	 * @param String
	 *            id_UsuarioServentia, id do usuario serventia que terá suas
	 *            pendencias abertas consultadas
	 * @return List
	 * @throws Exception
	 */
	public List consultarIntimacoesPromotorSubstitutoProcessual(String idProcesso, String id_UsuarioServentia) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT ID_PEND, PEND FROM VIEW_PEND_ABERTAS_RESP par WHERE par.ID_PROC = ? AND par.ID_USU_RESP = ? ";
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(id_UsuarioServentia);
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				//super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND"));
				pendencias.add(pendenciaDt);
			}			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}
	
	public PendenciaDt consultarPendenciaVotoRelatorSessao(String idAudienciaProcessoSessaoSegundoGrau, String idServentiaCargoRelator) throws Exception {
		ResultSetTJGO rs1 = null;
		PendenciaDt pendenciaDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT p.ID_PEND, pt.PEND_TIPO, p.ID_MOVI, p.ID_PEND_TIPO, p.ID_PEND_STATUS, p.ID_USU_CADASTRADOR, p.CODIGO_TEMP ";
		sql += " FROM PEND p ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = p.ID_PEND ";
		sql += " INNER JOIN PROJUDI.AUDI_PROC ap on ap.ID_PEND_VOTO = p.ID_PEND AND ap.ID_SERV_CARGO = pr.ID_SERV_CARGO ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " WHERE p.DATA_FIM IS NULL AND ap.ID_AUDI_PROC = ? "; ps.adicionarLong(idAudienciaProcessoSessaoSegundoGrau);
		sql += " AND pr.ID_SERV_CARGO  = ? "; ps.adicionarLong(idServentiaCargoRelator);		
		sql += " AND pt.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setId_PendenciaStatus(rs1.getString("ID_PEND_STATUS"));
				pendenciaDt.setId_UsuarioCadastrador(rs1.getString("ID_USU_CADASTRADOR"));
				pendenciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
					
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return pendenciaDt;
	}
	
	public PendenciaDt consultarPendenciaEmentaRelatorSessao(String idAudienciaProcessoSessaoSegundoGrau, String idServentiaCargoRelator) throws Exception {
		ResultSetTJGO rs1 = null;
		PendenciaDt pendenciaDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT p.ID_PEND, pt.PEND_TIPO, p.ID_MOVI, p.ID_PEND_TIPO, p.ID_PEND_STATUS, p.ID_USU_CADASTRADOR, p.CODIGO_TEMP  ";
		sql += " FROM PEND p ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = p.ID_PEND ";
		sql += " INNER JOIN PROJUDI.AUDI_PROC ap on ap.ID_PEND_EMENTA = p.ID_PEND AND ap.ID_SERV_CARGO = pr.ID_SERV_CARGO ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO ";
		sql += " WHERE p.DATA_FIM IS NULL AND ap.ID_AUDI_PROC = ? "; ps.adicionarLong(idAudienciaProcessoSessaoSegundoGrau);
		sql += " AND pr.ID_SERV_CARGO  = ? "; ps.adicionarLong(idServentiaCargoRelator);
		sql += " AND pt.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setId_PendenciaStatus(rs1.getString("ID_PEND_STATUS"));
				pendenciaDt.setId_UsuarioCadastrador(rs1.getString("ID_USU_CADASTRADOR"));
				pendenciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
					
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return pendenciaDt;
	}
	
	public PendenciaDt consultarPendenciaVotoRedatorSessao(String idAudienciaProcessoSessaoSegundoGrau, String idServentiaCargoRedator) throws Exception {
		ResultSetTJGO rs1 = null;
		PendenciaDt pendenciaDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT p.ID_PEND, pt.PEND_TIPO, p.ID_MOVI, p.ID_PEND_TIPO, p.ID_PEND_STATUS, p.ID_USU_CADASTRADOR, p.CODIGO_TEMP  ";
		sql += " FROM PEND p ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = p.ID_PEND ";
		sql += " INNER JOIN PROJUDI.AUDI_PROC ap on ap.ID_PEND_VOTO_REDATOR = p.ID_PEND AND ap.ID_SERV_CARGO_REDATOR = pr.ID_SERV_CARGO ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " WHERE p.DATA_FIM IS NULL AND ap.ID_AUDI_PROC = ? "; ps.adicionarLong(idAudienciaProcessoSessaoSegundoGrau);
		sql += " AND pr.ID_SERV_CARGO  = ? "; ps.adicionarLong(idServentiaCargoRedator);
		sql += " AND pt.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setId_PendenciaStatus(rs1.getString("ID_PEND_STATUS"));
				pendenciaDt.setId_UsuarioCadastrador(rs1.getString("ID_USU_CADASTRADOR"));
				pendenciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
					
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return pendenciaDt;
	}
	
	public PendenciaDt consultarPendenciaEmentaRedatorSessao(String idAudienciaProcessoSessaoSegundoGrau, String idServentiaCargoRedator) throws Exception {
		ResultSetTJGO rs1 = null;
		PendenciaDt pendenciaDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT p.ID_PEND, pt.PEND_TIPO, p.ID_MOVI, p.ID_PEND_TIPO, p.ID_PEND_STATUS, p.ID_USU_CADASTRADOR, p.CODIGO_TEMP  ";
		sql += " FROM PEND p ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = p.ID_PEND ";
		sql += " INNER JOIN PROJUDI.AUDI_PROC ap on ap.ID_PEND_EMENTA_REDATOR = p.ID_PEND AND ap.ID_SERV_CARGO_REDATOR = pr.ID_SERV_CARGO";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " WHERE p.DATA_FIM IS NULL AND ap.ID_AUDI_PROC = ? "; ps.adicionarLong(idAudienciaProcessoSessaoSegundoGrau);
		sql += " AND pr.ID_SERV_CARGO  = ? "; ps.adicionarLong(idServentiaCargoRedator);
		sql += " AND pt.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setId_PendenciaStatus(rs1.getString("ID_PEND_STATUS"));
				pendenciaDt.setId_UsuarioCadastrador(rs1.getString("ID_USU_CADASTRADOR"));
				pendenciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
			}
					
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		
		return pendenciaDt;
	}
	
	public List consultarPendenciasVotoEmentaProcesso(String idProcesso, String idServentiaCargo, String pendenciaTipoCodigo, String idAudienciaProcessoSessaoSegundoGrau) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT p.ID_PEND, pt.PEND_TIPO, p.ID_MOVI, p.ID_PEND_TIPO, p.ID_PEND_STATUS, p.ID_USU_CADASTRADOR FROM PEND p ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = p.ID_PEND ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " WHERE p.DATA_FIM IS NULL AND p.ID_PROC = ? "; ps.adicionarLong(idProcesso);
		if (idServentiaCargo != null && idServentiaCargo.trim().length() > 0) {
			sql += " AND pr.ID_SERV_CARGO  = ? "; ps.adicionarLong(idServentiaCargo);
		}
		sql += " AND pt.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(pendenciaTipoCodigo);
	
		if (idAudienciaProcessoSessaoSegundoGrau != null && idAudienciaProcessoSessaoSegundoGrau.trim().length() > 0) {
			if (Funcoes.StringToInt(pendenciaTipoCodigo) == PendenciaTipoDt.CONCLUSO_VOTO) {
				sql += " AND (";
				sql += "        EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap WHERE ap.ID_AUDI_PROC = ? AND ap.ID_PROC = p.ID_PROC AND (ap.ID_PEND_VOTO = p.ID_PEND OR ap.ID_PEND_VOTO_REDATOR = p.ID_PEND)) ";
				sql += "     OR EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap WHERE ap.ID_AUDI_PROC = ? AND ap.ID_PROC = p.ID_PROC AND ap.ID_PEND_VOTO IS NULL AND NOT EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap2 WHERE ap2.ID_PEND_VOTO = p.ID_PEND)) ";
				sql += " ) ";
				ps.adicionarLong(idAudienciaProcessoSessaoSegundoGrau);
				ps.adicionarLong(idAudienciaProcessoSessaoSegundoGrau);
			} else if (Funcoes.StringToInt(pendenciaTipoCodigo) == PendenciaTipoDt.CONCLUSO_EMENTA) {
				sql += " AND (";
				sql += "        EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap WHERE ap.ID_AUDI_PROC = ? AND ap.ID_PROC = p.ID_PROC AND (ap.ID_PEND_EMENTA = p.ID_PEND OR ap.ID_PEND_EMENTA_REDATOR = p.ID_PEND)) ";
				sql += "     OR EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap WHERE ap.ID_AUDI_PROC = ? AND ap.ID_PROC = p.ID_PROC AND ap.ID_PEND_EMENTA IS NULL AND NOT EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap2 WHERE ap2.ID_PEND_EMENTA = p.ID_PEND)) ";
				sql += " ) ";
				ps.adicionarLong(idAudienciaProcessoSessaoSegundoGrau);
				ps.adicionarLong(idAudienciaProcessoSessaoSegundoGrau);
			}
		} else {
			if (Funcoes.StringToInt(pendenciaTipoCodigo) == PendenciaTipoDt.CONCLUSO_VOTO) {
				sql += " AND NOT EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap WHERE ap.ID_PROC = p.ID_PROC AND (ap.ID_PEND_VOTO = p.ID_PEND OR ap.ID_PEND_VOTO_REDATOR = p.ID_PEND)) ";				
			} else if (Funcoes.StringToInt(pendenciaTipoCodigo) == PendenciaTipoDt.CONCLUSO_EMENTA) {
				sql += " AND NOT EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC ap WHERE ap.ID_PROC = p.ID_PROC AND (ap.ID_PEND_EMENTA = p.ID_PEND OR ap.ID_PEND_EMENTA_REDATOR = p.ID_PEND)) ";				
			}
		}
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setPendencia(rs1.getString("PEND_TIPO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setId_PendenciaStatus(rs1.getString("ID_PEND_STATUS"));
				pendenciaDt.setId_UsuarioCadastrador(rs1.getString("ID_USU_CADASTRADOR"));
				pendencias.add(pendenciaDt);
			}
					
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}

	/**
	 * Consulta as pendencias de um determinado processo
	 * 
	 * @author Leandro Bernardes
	 * @since 03/09/2009
	 * @param String
	 *            idProcesso, id do processo que terá suas pendencias abertas
	 *            consultadas
	 * @return List
	 * @throws Exception
	 */
	public List consultarPendenciasProcesso(String idProcesso) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT * FROM VIEW_PEND_PROC pp WHERE pp.ID_PROC = ? ";
		ps.adicionarLong(idProcesso);
		//sql += " group by  pp.ID_PEND ";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);
				pendenciaDt.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));
				pendenciaDt.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR") + " - "+rs1.getString("SERV_CADASTRADOR"));

			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}
	
	public List consultarPendenciasProcessoHash(String idProcesso, UsuarioNe UsuarioSessao) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		String sql = "SELECT DISTINCT pp.*, (SELECT sc.ID_SERV_CARGO FROM projudi.SERV_CARGO sc WHERE sc.ID_SERV_CARGO = pr.ID_SERV_CARGO AND sc.ID_SERV = ?) AS ID_SERV_CARGO FROM VIEW_PEND_PROC pp ";  ps.adicionarLong(UsuarioSessao.getUsuarioDt().getId_Serventia());
//		sql += " LEFT JOIN PEND_RESP pr ON pp.ID_PEND = pr.ID_PEND ";
//		sql += " WHERE pp.ID_PROC = ? "; ps.adicionarLong(idProcesso);
		
		String sql = " SELECT ";
		sql += "   DISTINCT pp.*, pr.id_serv_cargo AS ID_SERV_CARGO, ct.CARGO_TIPO_CODIGO, mj.id_mand_jud AS ID_MAND_JUD ";
		sql += " FROM VIEW_PEND_PROC pp "; 
		sql += " LEFT JOIN PEND_RESP pr ON pp.ID_PEND = pr.ID_PEND and pr.ID_SERV is null ";
		sql += " LEFT JOIN PROJUDI.SERV_CARGO sc ON sc.ID_SERV_CARGO = pr.ID_SERV_CARGO ";
		sql += " LEFT JOIN PROJUDI.CARGO_TIPO ct ON sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ";
		sql += " LEFT JOIN PROJUDI.MAND_JUD mj ON mj.ID_PEND = pp.ID_PEND ";
		sql += " WHERE pp.ID_PROC = ?  ";  ps.adicionarLong(idProcesso);
		
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				
				if (rs1.getString("CARGO_TIPO_CODIGO")!= null) {
					int cargoTipoCodigo = Funcoes.StringToInt(rs1.getString("CARGO_TIPO_CODIGO"));
					if (cargoTipoCodigo != CargoTipoDt.ASSISTENTE_GABINETE && cargoTipoCodigo != CargoTipoDt.ASSISTENTE_GABINETE_FLUXO  && cargoTipoCodigo != CargoTipoDt.DISTRIBUIDOR_GABINETE){
						PendenciaDt pendenciaDt = new PendenciaDt();
						super.associarDt(pendenciaDt, rs1);
						pendencias.add(pendenciaDt);
						pendenciaDt.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));
						pendenciaDt.setDataLimite(Funcoes.FormatarDataHora(rs1.getString("DATA_LIMITE")));
				        pendenciaDt.setId_MandadoJudicial(rs1.getString("ID_MAND_JUD"));
						pendenciaDt.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR") + " - "+rs1.getString("SERV_CADASTRADOR"));
						pendenciaDt.setHash(UsuarioSessao.getCodigoHash(pendenciaDt.getId()));
						pendenciaDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
					}
				} else{
					//Tratamento para verificar se a pendência já está na lista. 
					//O tratamento foi adicionado devido a pendência estar para um cargo e a mesma for pre-anlisada, ficando com 2 resgistros na pend_resp. 
					//Devido a isso a pendência ficava duplicado na tela de pendências dos processo 
					PendenciaDt pendenciaDt = new PendenciaDt();
					super.associarDt(pendenciaDt, rs1);
					boolean boPendelista=false;
					for (Iterator iterator = pendencias.iterator(); iterator.hasNext();) {
						PendenciaDt itemLista = (PendenciaDt) iterator.next();
						if (itemLista.getId().equalsIgnoreCase(pendenciaDt.getId())){
							boPendelista = true;
							break;
						}
					}
					if (boPendelista == false){
						pendencias.add(pendenciaDt);
						pendenciaDt.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));
						pendenciaDt.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR") + " - "+rs1.getString("SERV_CADASTRADOR"));
						pendenciaDt.setHash(UsuarioSessao.getCodigoHash(pendenciaDt.getId()));
						pendenciaDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
					}
				}
			}
			//rs1.close();

		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}
	
	/**
	 * Consulta a pendência do tipo "Suspensão de Processo" para um processo passado
	 * @param id_Processo, identificação do processo
	 * @return
	 * @throws Exception
	 */
	public PendenciaDt consultarPendenciaSuspensaoProcesso(String id_Processo)  throws Exception {
		String Sql;
		PendenciaDt Dados=null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "SELECT * FROM PROJUDI.VIEW_PEND p ";
		Sql += " WHERE p.ID_PROC = ? ";
		ps.adicionarLong(id_Processo);
		Sql += " AND p.PEND_TIPO_CODIGO = ? AND p.DATA_FIM IS NULL";
		ps.adicionarLong(PendenciaTipoDt.SUSPENSAO_PROCESSO);
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				Dados= new PendenciaDt();
				associarDt(Dados, rs1);
			}
		}finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return Dados; 
	}
	
	/**
	 * Consulta as pendências que foram analisadas, sendo aquelas com tratamento semelhante à conclusão
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param id_UsuarioServentia, identificação do usuário na serventia
	 * @param numeroProcesso, filtro número do processo
	 * @param digitoVerificador, dígito do processo
	 * @param dataInicial, data inicial para consulta
	 * @param dataFinal, data final para consulta
	 * 
	 * @author msapaula
	 */
	public List consultarPendenciasAnalisadas(String id_ServentiaCargo, String id_UsuarioServentia, String numeroProcesso, String digitoVerificador, String dataInicial, String dataFinal) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT * FROM PROJUDI.VIEW_PEND_ANALISADAS pen";
		sql += " WHERE pen.PEND_TIPO_CODIGO IN (?,?,?,?,?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		
		if (id_ServentiaCargo != null){
			sql += " AND pen.ID_SERV_CARGO = ? ";
			ps.adicionarLong(id_ServentiaCargo);
		} else if (id_UsuarioServentia != null) {
			sql += " AND pen.ID_USU_RESP = ? ";
			ps.adicionarLong(id_UsuarioServentia);
		}
		if (dataInicial.length() > 0){
			sql += " AND pen.DATA_FIM >= ? ";
			ps.adicionarDateTime(Funcoes.DataHora(dataInicial + " 00:00:00"));
		}
		if (dataFinal.length() > 0){
			sql += " AND pen.DATA_FIM <= ? ";
			ps.adicionarDateTime(Funcoes.DataHora(dataFinal + " 23:59:59"));
		}
		if (numeroProcesso != null && numeroProcesso.length() > 0){
			sql += " AND pen.PROC_NUMERO = ? ";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			sql += " AND pen.DIGITO_VERIFICADOR = ? ";
			ps.adicionarLong(digitoVerificador);
		}
		sql += " ORDER BY pen.PEND_TIPO, pen.DATA_FIM desc";

		ResultSetTJGO rs1 = null;
		try{

			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataFim(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				pendenciaDt.setUsuarioFinalizador(rs1.getString("USU_FINALIZADOR"));
				pendencias.add(pendenciaDt);
			}

		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
	 * Consulta os ids de pendências que não foram analisadas, de um determinado tipo para o usuario.
	 * - Consulta somente as pendências que não possuem nenhuma pré-análise registrada
	 *
	 * @param id_ServentiaCargo, identificação do serventia cargo
	 * @param id_Processo, identificação do processo
	 * @author mmgomes
	 */
	public List consultarIdPendenciasProcessoNaoAnalisadas(String id_ServentiaCargo, String id_Processo) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "SELECT p.ID_PEND FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA );
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		sql += " AND p.ID_PROC is not null AND p.ID_USU_FINALIZADOR IS NULL";
		sql += " AND p.DATA_FIM IS NULL";
		sql += " AND  not exists ";    
		sql += " 	(SELECT pa.ID_PEND FROM PROJUDI.PEND_ARQ pa WHERE pa.RESPOSTA = ? and pa.id_pend=p.id_pend)";		ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? ";																			ps.adicionarLong(id_ServentiaCargo);
		sql += " AND p.ID_PROC = ? ";																					ps.adicionarLong(id_Processo);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(rs1.getString("ID_PEND"));
			}
			
		
		} finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	/**
	 * Consulta os ids de pendencias que estão pré-analisadas por tipo. Trata os tipos de pendências semelhantes às conclusões,
	 * que terão análise e pré-análise
	 *	 
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param id_Processo, identificação do processo
	 * @param tipo, tipo da pendencia e indicador se é para consultar as pré-análises múltiplas ou não
	 * @author mmgomes
	 */
	public List consultarIdPendenciasProcessoPreAnalisadas(String id_ServentiaCargo, String id_Processo, boolean multiplo) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT p.ID_PEND FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ? AND pa.CODIGO_TEMP <> ?)";
		ps.adicionarLong(1);
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?)";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		sql += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pa.ID_ARQ group by pa1.ID_ARQ) " + (multiplo ? ">" : "=") + "?";
		ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		sql += " AND p.ID_PROC = ? ";
		ps.adicionarLong(id_Processo);		

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(rs1.getString("ID_PEND"));
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	/**
	 * Consulta os ids de pendencias que estão pré-analisadas por tipo e pendentes de assinatura. Trata os tipos de pendências semelhantes às conclusões,
	 * que terão análise e pré-análise
	 *	 
	 * @param id_ServentiaCargo, serventia cargo responsável pelas conclusões
	 * @param id_Processo, identificação do processo
	 * @param tipo, tipo da pendencia e indicador se é para consultar as pré-análises múltiplas ou não
	 * @author mmgomes
	 */
	public List consultarIdPendenciasProcessoPreAnalisadasPendentesAssinatura(String id_ServentiaCargo, String id_Processo, boolean multiplo) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT p.ID_PEND FROM PROJUDI.PEND p";
		sql += " INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = ? AND pa.CODIGO_TEMP = ?)";
		ps.adicionarLong(1);
		ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on p.ID_PEND_TIPO = pt.ID_PEND_TIPO";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?)";		
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.RELATORIO);
		ps.adicionarLong(PendenciaTipoDt.PRE_ANALISE_PRECATORIA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.REVISAO);
		sql += " AND p.ID_PROC is not null AND p.DATA_FIM IS NULL";
		sql += " AND (SELECT COUNT(pa1.ID_ARQ) FROM PEND_ARQ pa1";
		sql += " 		WHERE pa1.ID_ARQ = pa.ID_ARQ group by pa1.ID_ARQ) " + (multiplo ? ">" : "=") + "?";
		ps.adicionarLong(1);
		sql += " AND pr.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		sql += " AND p.ID_PROC = ? ";
		ps.adicionarLong(id_Processo);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(rs1.getString("ID_PEND"));
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}

	/**
	 * Marcar pendência do tipo intimação ou citação como distribuída.
	 * 
	 * @author lsbernardes
	 * @since 23/02/2011
	 * @param String id da pendência
	 * @throws Exception
	 */
	public void marcarIntimacaoCitacaoDistribuida(String idPendencia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set CODIGO_TEMP = ? ";
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		sql += " WHERE ID_PEND = ? ";
		ps.adicionarLong(idPendencia);

		this.executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Consulta as pendencias que estão em aberto, e que o responsável seja o serventiaCargo passado.
	 * Essas pendências são aquelas que tem o mesmo tratamento de conclusões.
	 * 
	 * @param id_Processo, identificação do processo
	 * @param pendenciaTipoCodigo, identificação da pendencia tipo código
	 * 
	 * @author mmgomes
	 */
	public List consultarPendenciasNaoAnalisadasProcesso(String id_Processo, String pendenciaTipoCodigo) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT ID_PEND, DATA_INICIO, ID_PEND_TIPO, PEND_TIPO, ID_PROC, PROC_NUMERO_COMPLETO, ";
		sql += "ID_CLASSIFICADOR, CLASSIFICADOR, PROC_TIPO, PEND_PRIORIDADE_CODIGO , PEND_PRIOR_ORDEM, PRIOR_CLASSIFICADOR, PEN.DATA_INICIO, ID_SERV_CARGO " +
				"FROM PROJUDI.VIEW_PEND_NAO_ANALISADAS pen ";	
		sql += " WHERE pen.ID_PROC = ?";
		ps.adicionarLong(id_Processo);			
		if (pendenciaTipoCodigo != null && pendenciaTipoCodigo.length() > 0){
			sql += " AND pen.ID_PEND_TIPO IN (SELECT ID_PEND_TIPO FROM PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)";
			ps.adicionarLong(pendenciaTipoCodigo);
		}			
		sql += " ORDER BY pen.PEND_TIPO, pen.PRIOR_CLASSIFICADOR desc, pen.ID_CLASSIFICADOR, pen.PEND_PRIOR_ORDEM, pen.DATA_INICIO";
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setProcessoPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				
				// Criando objeto do tipo processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				processoDt.setClassificador(rs1.getString("CLASSIFICADOR"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				pendenciaDt.setProcessoDt(processoDt);
				pendenciaDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));

				pendencias.add(pendenciaDt);
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return pendencias;
	}
	
	/**
	 * Atualiza o codigo temp de uma pendência
	 * 
	 * @param pendenciaDt
	 * @throws Exception
	 * @author jvosantos
	 */
	public void limparCodigoTempPendencia(String idPend) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set CODIGO_TEMP = ? WHERE ID_PEND = ?";
		ps.adicionarLongNull();
		ps.adicionarLong(idPend);

		this.executarUpdateDelete(sql, ps);
	}
	
	
	/**
	 * Atualiza o codigo temp de uma pendência
	 * 
	 * @param pendenciaDt
	 * @throws Exception
	 * @author mmgomes
	 */
	public void AlterarCodigoTempPendencia(PendenciaDt pendenciaDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set CODIGO_TEMP = ? ";		
		ps.adicionarLong(pendenciaDt.getCodigoTemp());
		sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(pendenciaDt.getId());

		this.executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Consulta as pendências vinculadas a um arquivo.
	 * Geralmente esse arquivo refere-se a uma pré-analise multipla realizada.
	 * Filtra pelas pendências que estão em aberto, porque algumas das pendências vinculadas ao arquivo
	 * podem ter sido concluídas separadamente.
	 * 
	 * @param id_Arquivo: identificação do arquivo de pré-analise
	 * @return lista de pendências vinculadas ao arquivo
	 * @throws Exception 
	 */
	public List consultarPendencias(String id_Arquivo) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT pen.* FROM PROJUDI.PEND_ARQ pa ";
		sql += " JOIN PROJUDI.VIEW_PEND pen on pa.ID_PEND=pen.ID_PEND";
		sql += " WHERE pa.ID_ARQ = ? ";
		ps.adicionarLong(id_Arquivo);
		sql += " AND pen.DATA_FIM IS NULL ORDER BY pen.DATA_INICIO";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();

				PendenciaPs pendenciaPs = new PendenciaPs();
				pendenciaPs.associarDt(pendenciaDt, rs1);

				pendencias.add(pendenciaDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return pendencias;
	}
	
	/**
	 * Método que verifica se há pendências do tipo conclusão em aberto para o processo e serventia cargo passados. Será utilizado em verificações.
	 * 
	 * @param id_Processo, identificação de processo
	 * @param id_ServentiaCargo, identificação de processo
	 * 
	 * @author mmgomes
	 */
	public String[] consultarConclusoesAbertasProcessoServentiaCargo(String id_Processo, String id_ServentiaCargo) throws Exception {
		String[] conclusaoPendente = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT p.ID_PEND, p.PEND_TIPO, p.DATA_INICIO, COUNT(pa.ID_ARQ) as quantidade, PROC_NUMERO_COMPLETO";
		sql += " FROM PROJUDI.VIEW_PEND_ABERTAS_SERV_CARGO p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on (p.ID_PEND_TIPO = pt.ID_PEND_TIPO) ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on (p.ID_PEND = pr.ID_PEND) ";
		sql += " LEFT JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = 1)";
		sql += " LEFT JOIN PROJUDI.ARQ a on (pa.ID_ARQ = a.ID_ARQ  AND a.USU_ASSINADOR IS NULL)";
		sql += " WHERE p.ID_PROC = ? AND pt.PEND_TIPO_CODIGO <> ? AND pt.PEND_TIPO_CODIGO <> ? AND pr.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_Processo); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO); ps.adicionarLong(id_ServentiaCargo);
		
		sql += " group by p.ID_PEND, pa.ID_ARQ, p.PEND_TIPO, p.DATA_INICIO, PROC_NUMERO_COMPLETO";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				conclusaoPendente = new String[] {rs1.getString("ID_PEND"), rs1.getString("PEND_TIPO"),Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")), rs1.getString("quantidade"),rs1.getString("PROC_NUMERO_COMPLETO")};
			}

		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return conclusaoPendente;
	}
	
	/**
	 * Consulta as pendencias lidas para distribuição de uma promotoria
	 * 
	 * @author mmgomes
	 * @since 30/08/2012
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarIntimacoesLidasDistribuicaoPromotoria(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_INTIMACOES_PROM pip WHERE NOT pip.DATA_FIM IS NULL ";
		
		String filtroGenerico = this.filtroIntimacaoDistribuidas("pip", true, null, null, numero_processo, dataInicialInicio, dataFinalInicio, ps);

		sql += filtroGenerico;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_INICIO desc";

		sql = "SELECT * FROM " + sql;	

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setPendenciaStatusCodigo(rs1.getString("PEND_STATUS_CODIGO"));
				pendenciaDt.setDataFim(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias lidas para distribuição de uma procuradoria
	 * 
	 * @author mmgomes
	 * @since 30/08/2012
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public List consultarIntimacoesLidasDistribuicaoProcuradoria(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {

		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "PROJUDI.VIEW_PEND_INTIMACOES_PROC pip WHERE NOT pip.DATA_FIM IS NULL ";

		String filtroGenerico = this.filtroIntimacaoDistribuidas("pip", true, null, null, numero_processo, dataInicialInicio, dataFinalInicio, ps);

		sql += filtroGenerico;		

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_INICIO desc";

		sql = "SELECT DISTINCT ID_PEND, ID_PROC, PROC_NUMERO_COMPLETO, ID_MOVI, MOVI_TIPO, PEND_TIPO, DATA_INICIO, PEND_STATUS, PEND_STATUS_CODIGO, DATA_FIM   FROM " + sql;
		
		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setPendenciaStatusCodigo(rs1.getString("PEND_STATUS_CODIGO"));
				pendenciaDt.setDataFim(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_FIM")));
				
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				
				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd,ps);
			if (rs2.next()) pendencias.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	
	/**
     * Consulta pendencias do tipo Intimação de todos os promotoroes da promotoria
     * do usuário logado
	 * 
	 * @author mmgomes
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de intimações de todos os procuradores da procuradoria
	 * @throws Exception
	 */
	public List consultarIntimacoesLidasPromotoria(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;	
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		TJDataHora dataAtual = new TJDataHora();
		dataAtual.atualizePrimeiraHoraDia();
		
		String sql = " SELECT * FROM VIEW_PEND_INTIMACOES_PROM " +
				"WHERE ID_SERV = ? AND ( CODIGO_TEMP <> ? OR CODIGO_TEMP is null ) AND NOT DATA_FIM IS NULL AND DATA_LIMITE >= ? ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		ps.adicionarDateTime(dataAtual);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoDt(new ProcessoDt());
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setNomeParte(rs1.getString("NOME_PARTE"));
				
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.getProcessoDt().setHash(usuarioNe.getCodigoHash(pendenciaDt.getId_Processo()));
				
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
     * Consulta pendencias do tipo Intimação de todos os procuradores da procuradoria
     * do usuário logado
	 * 
	 * @author mmgomes
	 * @since 30/08/2012
	 * @param usuarioDt
	 *            usuario da sessao
	 * @return lista de intimações lidas de todos os procuradores da procuradoria
	 * @throws Exception
	 */
	public List consultarIntimacoesLidasProcuradoria(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;	
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		TJDataHora dataAtual = new TJDataHora();
		dataAtual.atualizePrimeiraHoraDia();
		
		String sql = " SELECT distinct P.ID_PEND, P.ID_PROC, (PRO.PROC_NUMERO || '.'|| PRO.DIGITO_VERIFICADOR) AS PROC_NUMERO_COMPLETO,"
						+ " M.ID_MOVI, Mt.MOVI_TIPO, Pt.PEND_TIPO, P.DATA_INICIO, P.DATA_LIMITE, PS.PEND_STATUS "
				+ " FROM Serv S  "
				+ " JOIN Usu_Serv Us ON S.Id_Serv = Us.Id_Serv  "
				+ " JOIN usu u ON us.id_usu= u.id_usu  "
				+ " JOIN Pend_Resp Pr ON Pr.Id_Usu_Resp = Us.Id_Usu_Serv  "
				+ " JOIN Pend P   ON Pr.Id_Pend = P.Id_Pend " 
				+ " JOIN Pend_Tipo Pt ON P.Id_Pend_Tipo = Pt.Id_Pend_Tipo"
				+ " JOIN PROC Pro ON Pro.Id_Proc = P.Id_Proc "
				+ " JOIN Movi M ON M.Id_Movi = P.Id_Movi "
				+ " JOIN Movi_Tipo Mt ON Mt.Id_Movi_Tipo = M.Id_Movi_Tipo "
				+ " JOIN Pend_Status Ps ON Ps.Id_Pend_Status  = P.Id_Pend_Status "
				+ " WHERE S.ID_SERV = ? AND ( P.CODIGO_TEMP <> ? OR P.CODIGO_TEMP is null ) AND PT.PEND_TIPO_CODIGO = ? "
				+ "AND NOT P.DATA_FIM IS NULL AND P.DATA_LIMITE >= ?  ";		
		
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		ps.adicionarDateTime(dataAtual);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoDt(new ProcessoDt());
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.getProcessoDt().setHash(usuarioNe.getCodigoHash(pendenciaDt.getId_Processo()));
				
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	 /**
     * Método que consulta as pendências do tipo Carta de Citação lidas pelos advogados
     * do Escritório Jurídico do usuário logado 
     * 
     * @param usuarioNe usuário da sessão
     * @return lista de pendências do tipo carta de citação
     * @throws Exception
     * @author hmgodinho
     * @author jrcorrea 15/05/2015
     */
	public List consultarCitacoesLidasEscritorioJuridicoProcuradorias(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;	
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		TJDataHora dataAtual = new TJDataHora();
		dataAtual.atualizePrimeiraHoraDia();
		
		String sql = " SELECT distinct ID_PEND, ID_PROC, PROC_NUMERO_COMPLETO, ID_MOVI, MOVI_TIPO, PEND_TIPO, DATA_INICIO, DATA_LIMITE, PEND_STATUS FROM VIEW_PEND_CITACOES_ESC WHERE ID_SERV = ? AND ( CODIGO_TEMP <> ? OR CODIGO_TEMP is null ) AND NOT DATA_FIM IS NULL AND DATA_LIMITE >= ?  ";		
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		ps.adicionarDateTime(dataAtual);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoDt(new ProcessoDt());
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.getProcessoDt().setHash(usuarioNe.getCodigoHash(pendenciaDt.getId_Processo()));
				
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	/**
	 * Consultas as publicações de atos judicias em Consulta Pública -> Publicações com a pesquisa do tipo "Por Texto" e "Texto Exato" 
	 * @param dataInicio
	 * @param dataFinal
	 * @param id_Serventia
	 * @param palavras
	 * @param posicao
	 * @param usuarioSessao
	 * @return
	 * @throws MensagemException
	 * @throws Exception
	 */
	public String consultarTextoPublicacaoDuplaPalvrasJSON(String dataInicio, String dataFinal, String id_Serventia, String[] palavras, String posicao, UsuarioNe usuarioSessao) throws MensagemException, Exception {
		
		String stTemp = "";		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		int qtdeColunas = 4;
		
		String sqlCorpo = "";
		String sqlPaginacao = "";
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		PreparedStatementTJGO ps2 =  new PreparedStatementTJGO();
		
		sqlCorpo += " FROM (";
		sqlCorpo += " SELECT ap.id_arq FROM projudi.arq_palavra ap WHERE";
				
		if (palavras.length == 1){			
			sqlCorpo += " ( ap.id_palavra_1 = (SELECT id_palavra FROM projudi.palavra WHERE palavra = ?) )"; 
			ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0])); 
			ps2.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0]));
			
		} else if (palavras.length > 1){
			sqlCorpo += "(  ap.id_palavra_1 = (SELECT id_palavra FROM projudi.palavra WHERE palavra =?) AND ap.id_palavra_2 = (SELECT id_palavra FROM projudi.palavra WHERE palavra =?)";			
			ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0])); 
			ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[1])); 
			ps2.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0])); 
			ps2.adicionarString(Funcoes.convertePalavraSimplificada(palavras[1]));			
			for (int i = 1; i < palavras.length - 1; i++) {
				sqlCorpo += " OR ap.id_palavra_1 = (SELECT id_palavra FROM projudi.palavra WHERE palavra =?) AND ap.id_palavra_2 = (SELECT id_palavra FROM projudi.palavra WHERE palavra =?)";		
				ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[i])); 
				ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[i + 1])); 
				ps2.adicionarString(Funcoes.convertePalavraSimplificada(palavras[i])); 
				ps2.adicionarString(Funcoes.convertePalavraSimplificada(palavras[i + 1]));
			}
			sqlCorpo += " )";
			
		} else {
			throw new MensagemException("<{Erro: A pesquisa textual necessita de pelo menos uma palavra.}> Local Exception: " + this.getClass().getName() + ".consultarTextoPublicacaoPublicaJSON(): faltando o texto ");
		}
		
		sqlCorpo += "	GROUP BY ap.id_arq";
		sqlCorpo += " ) ap ";
		sqlCorpo += " JOIN projudi.PEND_FINAL_ARQ pa ON pa.id_arq = ap.id_arq ";
		sqlCorpo += " JOIN projudi.PEND_FINAL p on p.id_pend = pa.id_pend ";
		sqlCorpo += " JOIN projudi.PEND_TIPO pt on pt.id_pend_tipo = p.id_pend_tipo and pt.pend_tipo_codigo = ?"; 
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA); ps2.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);
		
		sqlCorpo += " JOIN projudi.USU_SERV us ON p.ID_USU_CADASTRADOR = us.ID_USU_SERV";
		sqlCorpo += " JOIN projudi.ARQ a ON ap.ID_ARQ = a.ID_ARQ";
		sqlCorpo += " JOIN projudi.ARQ_TIPO at ON at.ID_ARQ_TIPO = a.ID_ARQ_TIPO";
		sqlCorpo += " JOIN projudi.SERV s ON US.ID_SERV = s.ID_SERV";
		sqlCorpo += " WHERE 1=1";
		
		if (id_Serventia != null && id_Serventia.length() > 0) {
			sqlCorpo += " AND s.ID_SERV = ?"; ps.adicionarLong(id_Serventia); ps2.adicionarLong(id_Serventia);
		}
												
		if (dataInicio != null && dataFinal != null && dataInicio.length()>0 && dataFinal.length()>0) {
			sqlCorpo += " AND p.DATA_INICIO BETWEEN ? AND ?"; 
			ps.adicionarDateTime(dataInicio); ps.adicionarDateTime(dataFinal); ps2.adicionarDateTime(dataInicio); ps2.adicionarDateTime(dataFinal);
		}
		
		try {
			
			sqlPaginacao =  " SELECT * FROM (";
			sqlPaginacao += " SELECT tab.*, ROWNUM linha FROM (";
			sqlPaginacao += " SELECT a.ID_ARQ AS ID, S.SERV AS DESCRICAO1, AT.ARQ_TIPO AS DESCRICAO2, SUBSTR(a.NOME_ARQ,0,30) AS DESCRICAO3, TO_CHAR(P.DATA_INICIO, 'dd/mm/yyyy HH24:mi:ss') AS DESCRICAO4";
			sqlPaginacao += sqlCorpo;
			sqlPaginacao += " ORDER BY p.DATA_INICIO desc";
			sqlPaginacao += " ) tab";
			sqlPaginacao += " )";
			sqlPaginacao += " WHERE LINHA > ?"; ps.adicionarLong(Configuracao.TamanhoRetornoConsulta * Funcoes.StringToInt(posicao));
			sqlPaginacao += " AND LINHA <= ?";  ps.adicionarLong((Configuracao.TamanhoRetornoConsulta * Funcoes.StringToInt(posicao)) + Configuracao.TamanhoRetornoConsulta);
			
			rs1 = this.consultar(sqlPaginacao, ps);
			
			String sqlCount = "SELECT count(1) as QUANTIDADE " + sqlCorpo;
			rs2 = this.consultar(sqlCount, ps2);
			rs2.next();
			
			stTemp = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioSessao);
			stTemp = stTemp.replaceAll(".p7s", "");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        } 

		return stTemp;
	}
	
	/**
	 * Consultas as publicações de atos judicias em Consulta Pública -> Publicações com a pesquisa do tipo "Por Texto" e "Qualquer Palavra" 
	 * @param dataInicio
	 * @param dataFinal
	 * @param id_Serventia
	 * @param palavras
	 * @param posicao
	 * @param usuarioSessao
	 * @return
	 * @throws MensagemException
	 * @throws Exception
	 */
	public String consultarTextoPublicacaoQualquerPalavraJSON(String dataInicio, String dataFinal, String id_Serventia, String[] palavras, String posicao, UsuarioNe usuarioSessao) throws MensagemException, Exception {
		
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		int qtdeColunas = 4;
		
		String sqlCorpo = "";
		String sqlPaginacao = "";
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		PreparedStatementTJGO ps2 =  new PreparedStatementTJGO();
		
		sqlCorpo += " FROM (";
		sqlCorpo += " SELECT ap.id_arq FROM projudi.arq_palavra ap WHERE";
				
		if (palavras.length == 1) {
			sqlCorpo += " ap.id_palavra_1 = (select id_palavra from projudi.palavra where palavra =?) "; 
			ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0])); 
			ps2.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0]));
			
		} else if (palavras.length > 1) {
			sqlCorpo += " ap.id_palavra_1 in (select id_palavra from projudi.palavra where palavra in (? "; 
			ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0])); 
			ps2.adicionarString(Funcoes.convertePalavraSimplificada(palavras[0]));
			for (int i = 1; i < palavras.length; i++) {
				sqlCorpo += ",? "; 
				ps.adicionarString(Funcoes.convertePalavraSimplificada(palavras[i])); 
				ps2.adicionarString(Funcoes.convertePalavraSimplificada(palavras[i]));
			}
			sqlCorpo += " ))";
		} else {
			throw new MensagemException("<{Erro: A pesquisa textual necessita de pelo menos uma palavra.}> Local Exception: " + this.getClass().getName() + ".consultarTextoPublicacaoPublicaJSON(): faltando o texto ");
		}
		
		sqlCorpo += " GROUP BY ap.id_arq";
		sqlCorpo += " ) ap ";
		sqlCorpo += " JOIN projudi.PEND_FINAL_ARQ pa ON pa.id_arq = ap.id_arq ";
		sqlCorpo += " JOIN projudi.PEND_FINAL p on p.id_pend = pa.id_pend ";
		sqlCorpo += " JOIN projudi.PEND_TIPO pt on pt.id_pend_tipo = p.id_pend_tipo and pt.pend_tipo_codigo = ?"; 
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA); ps2.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);
		
		sqlCorpo += " JOIN projudi.USU_SERV us ON p.ID_USU_CADASTRADOR = us.ID_USU_SERV";
		sqlCorpo += " JOIN projudi.ARQ a ON ap.ID_ARQ = a.ID_ARQ";
		sqlCorpo += " JOIN projudi.ARQ_TIPO at ON at.ID_ARQ_TIPO = a.ID_ARQ_TIPO";
		sqlCorpo += " JOIN projudi.SERV s ON US.ID_SERV = s.ID_SERV";
		sqlCorpo += " WHERE 1=1";
		
		if (id_Serventia != null && id_Serventia.length() > 0) {
			sqlCorpo += " AND s.ID_SERV = ?"; ps.adicionarLong(id_Serventia); ps2.adicionarLong(id_Serventia);
		}
												
		if (dataInicio != null && dataFinal != null && dataInicio.length()>0 && dataFinal.length()>0) {
			sqlCorpo += " AND p.DATA_INICIO BETWEEN ? AND ?"; 
			ps.adicionarDateTime(dataInicio); ps.adicionarDateTime(dataFinal); ps2.adicionarDateTime(dataInicio); ps2.adicionarDateTime(dataFinal);
		}
		
		try {
			
			sqlPaginacao =  " SELECT * FROM (";
			sqlPaginacao += " SELECT tab.*, ROWNUM linha FROM (";
			sqlPaginacao += " SELECT a.ID_ARQ AS ID, S.SERV AS DESCRICAO1, AT.ARQ_TIPO AS DESCRICAO2, SUBSTR(a.NOME_ARQ,0,30) AS DESCRICAO3, TO_CHAR(P.DATA_INICIO, 'dd/mm/yyyy HH24:mi:ss') AS DESCRICAO4";
			sqlPaginacao += sqlCorpo;
			sqlPaginacao += " ORDER BY p.DATA_INICIO desc";
			sqlPaginacao += " ) tab";
			sqlPaginacao += " )";
			sqlPaginacao += " WHERE LINHA > ?"; ps.adicionarLong(Configuracao.TamanhoRetornoConsulta * Funcoes.StringToInt(posicao));
			sqlPaginacao += " AND LINHA <= ?";  ps.adicionarLong((Configuracao.TamanhoRetornoConsulta * Funcoes.StringToInt(posicao)) + Configuracao.TamanhoRetornoConsulta);
			
			rs1 = this.consultar(sqlPaginacao, ps);
			
			String sqlCount = "SELECT count(1) as QUANTIDADE " + sqlCorpo;
			rs2 = this.consultar(sqlCount, ps2);
			rs2.next();
			
			stTemp = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioSessao);
			stTemp = stTemp.replaceAll(".p7s", "");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        } 

		return stTemp;
			
	}
	
	/**
	 * Consultas as publicações dos atos de acordo com a data de publicação.
	 * @param dataInicio
	 * @param dataFinal
	 * @param posicao
	 * @param UsuarioSessao
	 * @return
	 * @throws MensagemException
	 * @throws Exception
	 */
	public String consultarPublicacaoPublicaPorPeriodo(String dataInicio, String dataFinal, String posicao, UsuarioNe usuarioSessao) throws MensagemException, Exception {
		
		String stTemp = "";		
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String sqlPaginacao="";
		String stSqlCorpo ="";		
		String stSqlCount ="";
		
		int qtdeColunas = 4;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		PreparedStatementTJGO ps2 =  new PreparedStatementTJGO();
				
		sqlPaginacao = "SELECT a.ID_ARQ AS ID, ";
		sqlPaginacao += " S.SERV AS DESCRICAO1, ";
		sqlPaginacao += " AT.ARQ_TIPO AS DESCRICAO2, ";
		sqlPaginacao += " SUBSTR(a.NOME_ARQ,0,30) AS DESCRICAO3, ";
		sqlPaginacao += " TO_CHAR(P.DATA_INICIO, 'dd/mm/yyyy HH24:mi:ss') AS DESCRICAO4 ";
		
		sqlPaginacao += " FROM projudi.PEND_FINAL p ";
		sqlPaginacao += " JOIN projudi.PEND_FINAL_ARQ pa ";
		sqlPaginacao += " ON p.id_pend = pa.id_pend ";
		sqlPaginacao += " JOIN projudi.PEND_TIPO pt ";
		sqlPaginacao += " ON pt.id_pend_tipo = p.id_pend_tipo ";
		sqlPaginacao += " AND pt.pend_tipo_codigo = ? "; ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);
		sqlPaginacao += " JOIN projudi.USU_SERV US ";
		sqlPaginacao += " ON P.ID_USU_CADASTRADOR = US.ID_USU_SERV ";
		sqlPaginacao += " JOIN projudi.ARQ a ";
		sqlPaginacao += " ON pa.ID_ARQ = a.ID_ARQ ";
		sqlPaginacao += " JOIN projudi.ARQ_TIPO AT ";
		sqlPaginacao += " ON AT.ID_ARQ_TIPO = a.ID_ARQ_TIPO ";
		sqlPaginacao += " JOIN projudi.SERV S ";
		sqlPaginacao += " ON US.ID_SERV = S.ID_SERV ";
		
		// Se tiver preenchido as duas datas
		if (dataInicio != null && dataFinal != null && dataInicio.length()>0 && dataFinal.length()>0) {
			sqlPaginacao += " WHERE p.DATA_INICIO BETWEEN ? AND ?"; ps.adicionarDateTime(dataInicio); ps.adicionarDateTime(dataFinal);
		}

		// Ordena pela data de publicação (descrescente)
		sqlPaginacao += " ORDER BY P.DATA_INICIO DESC";
		
		try {
			rs1 = this.consultarPaginacao(sqlPaginacao, ps, posicao);
			stSqlCount = "select count(1) as QUANTIDADE from (" + sqlPaginacao + ")";
			rs2 = this.consultar( stSqlCount, ps);
			rs2.next();
			stTemp = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioSessao);
			
		} finally {
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		
		return stTemp;
	}
	
	
	
	/**
	 * Consulta as pendencias do tipo conclusão que estão em aberto, e que o responsável seja o serventiaCargo passado
	 * 
	 * @param id_ServentiaCargo, serventiaCargo responsável pelas pendências
	 * @param id_Processo, id do processo	 
	 * 
	 * @author mmgomes
	 */
	public boolean ehResponsavelConclusoesPendentes(String id_ServentiaCargo, String id_Processo) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean retorno = false;

		String sql = " SELECT COUNT(1) AS QUANTIDADE ";
		sql += " FROM PROJUDI.PEND P INNER JOIN PROJUDI.PEND_RESP PR ON (P.ID_PEND = PR.ID_PEND) ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO PT ON (P.ID_PEND_TIPO = PT.ID_PEND_TIPO) ";
		sql += " WHERE P.ID_PROC = ? "; ps.adicionarLong(id_Processo);		
		sql += " AND PR.ID_SERV_CARGO = ? "; ps.adicionarLong(id_ServentiaCargo);
		sql += " AND P.DATA_FIM IS NULL ";
		sql += " AND PT.PEND_TIPO_CODIGO  IN (11,12,13,14,18,19,20,21,57,67,68,78,89,90,193) ";
				
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) retorno = (rs1.getLong("QUANTIDADE") > 0);		
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return retorno;
	}
	
	/**
	 * Altera o classificador de uma pendencia
	 * 
	 * @param id_Pendencia identificação da pendencia
	 * @param id_Classificador novo classificador
	 */
	public void alterarClassificadorPendencia(String id_Pendencia, String id_Classificador) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND SET ID_CLASSIFICADOR = ?";
		ps.adicionarLong(id_Classificador);
		Sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);

		executarUpdateDelete(Sql, ps);
	}
	
	public void alterarReserva(String id_Pendencia, String id_UsuarioServentia) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND SET ID_USU_FINALIZADOR = ?";
		ps.adicionarLong(id_UsuarioServentia);
		Sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(id_Pendencia);

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * 
	 * @author Jesus Rodrigo
     * @since 22/11/2013
	 * @param id_pendencia - o 
	 * a pendencia, todas os responsáveis e arquivos serão mudados para as tabelas finais
	 * @throws Exception 
	 */
	public void moverPendencia(String id_pendencia) throws Exception{
		String stSql;				

		stSql = " insert into projudi.pend_final  (select * from projudi.pend where id_pend = " + id_pendencia + ");";
		
		stSql += " insert into projudi.pend_final_resp (select * from projudi.pend_resp where id_pend = " + id_pendencia + ");";

		stSql += " insert into projudi.pend_final_arq  (select * from projudi.pend_arq where id_pend = " + id_pendencia + ");";
		
		stSql += " insert into projudi.pend_resp_hist_final  (select * from projudi.pend_resp_hist where id_pend = " + id_pendencia + ");";
		
		stSql += " insert into projudi.pend_final_correios (select * from projudi.pend_correios where id_pend = " + id_pendencia + ");";
		
		stSql += " delete from projudi.pend_correios where id_pend = " + id_pendencia + ";";

		stSql += " delete from projudi.pend_arq where id_pend = " + id_pendencia + ";";
		
		stSql += " delete from projudi.pend_resp where id_pend = " + id_pendencia + ";";
		
		stSql += " delete from projudi.pend_resp_hist where id_pend = " + id_pendencia + ";";
		
		stSql += " delete from projudi.pend where id_pend = " + id_pendencia + ";";

		executarComandos(stSql);
	}
	
	/**
	 * 
	 * @author mmgomes
     * @since 09/11/2018
	 * @param id_pendencia - o 
	 * a pendencia, todas os responsáveis e arquivos serão mudados das tabelas finais para as tabelas originais
	 * @throws Exception 
	 */
	public void moverPendenciaPendFinalParaPend(String id_pendencia) throws Exception{
		String stSql;				

		stSql = " insert into projudi.pend  (select * from projudi.pend_final where id_pend = " + id_pendencia + ");";

		stSql += " insert into projudi.pend_resp (select * from projudi.pend_final_resp where id_pend = " + id_pendencia + ");";

		stSql += " insert into projudi.pend_arq  (select * from projudi.pend_final_arq where id_pend = " + id_pendencia + ");";
		
		stSql += " insert into projudi.pend_resp_hist  (select * from projudi.pend_resp_hist_final where id_pend = " + id_pendencia + ");";

		stSql += " delete from projudi.pend_final_arq where id_pend = " + id_pendencia + ";";
		
		stSql += " delete from projudi.pend_final_resp where id_pend = " + id_pendencia + ";";
		
		stSql += " delete from projudi.pend_resp_hist_final where id_pend = " + id_pendencia + ";";
		
		stSql += " delete from projudi.pend_final where id_pend = " + id_pendencia + ";";

		executarComandos(stSql);
	}
	
	public PendenciaDt consultarFinalizadaId(String id_pendencia )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaDt Dados=null;
		////System.out.println("....ps-ConsultaId_Pendencia)");

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND_Final WHERE ID_PEND = ?";		ps.adicionarLong(id_pendencia); 

		////System.out.println("....Sql  " + stSql  );

		try{
			////System.out.println("..ps-ConsultaId_Pendencia  " + stSql);
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaDt();
				associarDt(Dados, rs1);
				Dados.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				Dados.setClassificador(rs1.getString("CLASSIFICADOR"));
			}
			////System.out.println("..ps-ConsultaId");
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return Dados; 
	}

	/**
     * Libera pendencia reservada.
     * 
     * @author Jesus Rodrigo
     * @since 22/11/2013
     * @param void
     * Limpa as pendencia reservadas           
     * 
     * @throws Exception
     */
	public void liberarPendenciasReservadas() throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND SET data_temp = NULL , id_usu_finalizador = null, id_pend_status = 1 WHERE id_pend_status = 14 AND  (DATA_TEMP + INTERVAL '1' HOUR) < SYSDATE ";

		executarUpdateDelete(Sql, ps);
		
	}
	/**
	 * Consulta as pendencias abertas para um serventia cargo.
	 * 
	 * @author Márcio Gomes
	 * @since 03/07/2013 11:53
	 * @param id_ServentiaCargo
	 * 
	 * @return List
	 * @throws Exception
	 */
	public List consultarTodasPendenciasSemDataFim(String id_ServentiaCargo) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PEND p ";
		sql += " WHERE DATA_FIM IS NULL ";
		sql += " AND EXISTS (SELECT 1 ";
		sql += "               FROM PROJUDI.PEND_RESP pr ";
		sql += "              WHERE pr.ID_PEND = p.ID_PEND ";
		sql += "               AND pr.ID_SERV_CARGO = ?) ";
		ps.adicionarLong(id_ServentiaCargo);

		try{
			rs1 = this.consultar(sql,ps);

			while (rs1.next()) {
				PendenciaDt obj = new PendenciaDt();
				super.associarDt(obj, rs1);
				pendencias.add(obj);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
        
		return pendencias;
	}	
	
	/**
	 * Método que verifica se há pendências do tipo voto / ementa em aberto para o processo passado. Será utilizado em verificações.
	 * 
	 * @param id_Processo, identificação de processo
	 * 
	 * @author mmgomes - Consultar voto/ementa de um determinado processo.
	 */
	public List consultarVotoEmentaAbertasHash(String id_Processo, UsuarioNe usuarioSessao) throws Exception {
		List votoEmentaPendentes = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT p.ID_PEND, p.PEND_TIPO, p.DATA_INICIO, COUNT(pa.ID_ARQ) as quantidade, PROC_NUMERO_COMPLETO, p.PEND_TIPO_CODIGO, pa.CODIGO_TEMP";
		sql += " FROM PROJUDI.VIEW_PEND_ABERTAS_SERV_CARGO p";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on (p.ID_PEND_TIPO = pt.ID_PEND_TIPO) ";
		sql += " LEFT JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = 1)";
		sql += " LEFT JOIN PROJUDI.ARQ a on (pa.ID_ARQ = a.ID_ARQ  AND a.USU_ASSINADOR IS NULL)";
		sql += " WHERE p.ID_PROC = ? AND (pt.PEND_TIPO_CODIGO = ? OR pt.PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(id_Processo); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		sql += " group by p.ID_PEND, pa.ID_ARQ, p.PEND_TIPO, p.DATA_INICIO, PROC_NUMERO_COMPLETO, p.PEND_TIPO_CODIGO, pa.CODIGO_TEMP";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				votoEmentaPendentes.add(new String[] {rs1.getString("ID_PEND") + "@#!@"+ usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")), rs1.getString("PEND_TIPO"),Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")), rs1.getString("quantidade"), rs1.getString("PROC_NUMERO_COMPLETO"), usuarioSessao.getCodigoHash(rs1.getString("ID_PEND")), rs1.getString("PEND_TIPO_CODIGO"), rs1.getString("CODIGO_TEMP"), "[SEM_SESSAO]"});
			}

		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return votoEmentaPendentes;
	}
	
	
	/**
	 * Método que verifica se há pendências do tipo Elaboracao de Voto em aberto para o processo passado. Será utilizado em verificações.
	 * 
	 * @param id_Processo, identificação de processo
	 * 
	 * @author lsbernardes
	 */
	public boolean verificarExistenciaElaboracaoVoto(String id_Processo) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT p.ID_PEND, pt.PEND_TIPO, p.DATA_INICIO";
		sql += " FROM PROJUDI.pend p ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on (p.ID_PEND_TIPO = pt.ID_PEND_TIPO) ";
		sql += " WHERE p.ID_PROC = ? AND p.data_fim is null AND (pt.PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(id_Processo); ps.adicionarLong(PendenciaTipoDt.ELABORACAO_VOTO); 
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				retorno = true;
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return retorno;
	}
	
	/**
     * Verifica se um determinado processo pussui a pendência concluso presidente tjgo
     * 
     * @author Leandro Bernardes
     * @param String idProcesso, processo que será verificado
     * @return boolean
     * @throws Exception
     */
	public boolean verificarExistenciaConclusoPresidenteTJGO(String id_Processo) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT p.ID_PEND, pt.PEND_TIPO, p.DATA_INICIO";
		sql += " FROM PROJUDI.pend p ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on (p.ID_PEND_TIPO = pt.ID_PEND_TIPO) ";
		sql += " WHERE p.ID_PROC = ? AND p.data_fim is null AND (pt.PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(id_Processo); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO); 
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				retorno = true;
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return retorno;
	}
	
	/**
     * Verifica se um determinado processo pussui a pendência concluso vice-presidente tjgo
     * 
     * @author Leandro Bernardes
     * @param String idProcesso, processo que será verificado
     * @return boolean
     * @throws Exception
     */
	public boolean verificarExistenciaConclusoVicePresidenteTJGO(String id_Processo) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT p.ID_PEND, pt.PEND_TIPO, p.DATA_INICIO";
		sql += " FROM PROJUDI.pend p ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO pt on (p.ID_PEND_TIPO = pt.ID_PEND_TIPO) ";
		sql += " WHERE p.ID_PROC = ? AND p.data_fim is null AND (pt.PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(id_Processo); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO); 
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				retorno = true;
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return retorno;
	}
	
	public PendenciaDt consultarId(String id_pendencia )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND WHERE ID_PEND = ?";		ps.adicionarLong(id_pendencia); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaDt();
				associarDt(Dados, rs1);
				Dados.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				Dados.setClassificador(rs1.getString("CLASSIFICADOR"));
				Dados.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				Dados.setProcessoNumeroCompleto(rs1.getString("PROC_NUMERO_COMPLETO"));
				Dados.setId_ServentiaCadastrador(rs1.getString("ID_SERV_CADASTRADOR"));
				Dados.setId_ServentiaFinalizador(rs1.getString("ID_SERV_FINALIZADOR"));
				Dados.setMovimentacao(rs1.getString("MOVI"));
				Dados.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR"));
				Dados.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));
				Dados.setPendenciaPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				Dados.setProcessoPrioridade(rs1.getString("PRIORI"));
				Dados.setServentiaCadastrador(rs1.getString("SERV_CADASTRADOR"));
				Dados.setServentiaFinalizador(rs1.getString("SERV_FINALIZADOR"));
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}}
		return Dados; 
	}
	
	/**
	 * Consulta as pendências do tipo INTIMAÇÃO e CARTA DE CITAÇÃO que são publicadas no DJE.
	 * @param dataInicial
	 * @param dataFinal
	 * @return
	 * @throws Exception
	 */	
	public List<MovimentacaoIntimacaoDt> consultarIntimacoesParaPublicacao(String dataInicial, String dataFinal, int opcaoPublicacao) throws Exception {
				
		List<MovimentacaoIntimacaoDt> pendencias = null;		
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.ID_PROC");
		sql.append(" , p.PROC_NUMERO");
		sql.append(" , p.DIGITO_VERIFICADOR");
	    sql.append(" , p.FORUM_CODIGO");
	    sql.append(" , p.ANO");
	    sql.append(" , p.SEGREDO_JUSTICA");
	    sql.append(" , (SELECT PP1.ID_PROC_PARTE");
		sql.append(" || '@'");
		sql.append(" || PP1.NOME");
		sql.append(" || '@'");
		sql.append(" || PP1.PROC_PARTE_TIPO");
		sql.append(" FROM PROJUDI.VIEW_PROC_PARTE PP1");
		sql.append(" WHERE PP1.DATA_BAIXA IS NULL");
		sql.append(" AND PP1.PROC_PARTE_TIPO_CODIGO IN (1, 5)");
		sql.append(" AND PP1.ID_PROC = p.id_proc");
		sql.append(" AND ROWNUM = 1");
		sql.append(" ) AS PRIMEIRO_PROMOVENTE_RECORRENTE");
		sql.append(" , (SELECT PP2.ID_PROC_PARTE");
		sql.append(" || '@'");
		sql.append(" || PP2.NOME");
		sql.append(" || '@'");
		sql.append(" || PP2.PROC_PARTE_TIPO");
		sql.append(" FROM projudi.VIEW_PROC_PARTE PP2");
		sql.append(" WHERE PP2.DATA_BAIXA IS NULL");
		sql.append(" AND PP2.PROC_PARTE_TIPO_CODIGO IN (0, 6)");
		sql.append(" AND PP2.ID_PROC = p.id_proc");
		sql.append(" AND ROWNUM = 1");
		sql.append(" ) AS PRIMEIRO_PROMOVIDO_RECORRIDO");
	    sql.append(" , pt.ID_PROC_TIPO");
	    sql.append(" , pt.PROC_TIPO");
	    sql.append(" , vw.ID_PEND");
	    sql.append(" , vw.PEND_TIPO_CODIGO");
		sql.append(" , vw.DATA_INICIO");
		sql.append(" , vw.ID_PARTE_INTIMADA");
		sql.append(" , vw.NOME_PARTE_INTIMADA");
		sql.append(" , vw.ID_USU_RESP");
		sql.append(" , vw.NOME_ADVOGADO_INTIMADO");
		sql.append(" , m.ID_MOVI");
		sql.append(" , m.COMPLEMENTO");
		sql.append(" , mt.MOVI_TIPO");
		sql.append(" , m.DATA_REALIZACAO");
		sql.append(" , vw.ID_ARQ");
		sql.append(" , vw.NOME_ARQ");
		sql.append(" , vw.CONTENT_TYPE");
		sql.append(" , vw.USU_ASSINADOR"); 
		sql.append(" , vw.RECIBO");
	    sql.append(" , vw.DJE");
		sql.append(" , c.COMARCA");
	    sql.append(" , s.SERV");
		sql.append(" , CASE WHEN ss.serv_subtipo_codigo = " + ServentiaSubtipoDt.CORTE_ESPECIAL + " THEN 0");
		sql.append(" 		WHEN ss.serv_subtipo_codigo IN (" + ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL + "," + ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL + "," + ServentiaSubtipoDt.UPJ_TURMA_RECURSAL + "," + ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL + ") THEN 9 ");
		sql.append( "		ELSE 1 END AS ordem");
		sql.append(" FROM ");
		
		sql.append("( ");
		sql.append(" SELECT ");
		sql.append(" pf.id_proc,");
		sql.append(" pf.id_movi,");
		sql.append(" pf.ID_PEND ,");
		sql.append(" pf.PEND_TIPO_CODIGO ,");
		sql.append(" pf.DATA_INICIO ,");
		sql.append(" pp.ID_PROC_PARTE AS ID_PARTE_INTIMADA ,");
		sql.append(" pp.NOME AS NOME_PARTE_INTIMADA ,");
		sql.append(" pf.id_usu_resp ,");
		sql.append(" (Oab.OAB_NUMERO");
		sql.append(" || '  '");
		sql.append(" || est.UF");
		sql.append(" || ' - '");
		sql.append(" || u.NOME) AS NOME_ADVOGADO_INTIMADO ,");
		sql.append(" a.ID_ARQ ,");
		sql.append(" a.NOME_ARQ ,");
		sql.append(" a.CONTENT_TYPE ,");
		sql.append(" a.USU_ASSINADOR,");
		sql.append(" a.RECIBO ,");
		sql.append(" ta.DJE");
		sql.append(" FROM");
		
		/**
		 * Subconsulta responsável em buscar as pendências em PEND, com tipo INTIMACAO E CARTA_CITACAO, status CUMPRIDA, onde tem advogado 
		 * UNIAO
		 * Subconsulta responsável em buscar as pendências em PEND_FINAL, com tipo INTIMACAO E CARTA_CITACAO, status CUMPRIDA, onde tem advogado
		 */
		sql.append(" ( ");
		sql.append("   SELECT p.id_pend, p.id_pend_tipo, p.id_pend_status, p.data_inicio, p.id_proc, p.id_proc_parte, pr.id_usu_resp, ma.id_arq, p.id_movi, pr.id_serv, pt.pend_tipo_codigo");
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
		sql.append("   SELECT pf.id_pend, pf.id_pend_tipo, pf.id_pend_status, pf.data_inicio, pf.id_proc, pf.id_proc_parte, prf.id_usu_resp, ma.id_arq, pf.id_movi, prf.id_serv, ptf.pend_tipo_codigo");
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
		sql.append(" JOIN projudi.usu_serv us ON us.id_usu_serv = pf.id_usu_resp");
		sql.append(" JOIN projudi.usu_serv_oab oab ON oab.id_usu_serv = us.id_usu_serv");
		sql.append(" JOIN projudi.usu u ON u.id_usu = us.id_usu");
		sql.append(" LEFT JOIN projudi.serv serv ON serv.id_serv = us.id_serv");
		sql.append(" LEFT JOIN projudi.estado est ON est.id_estado = serv.id_estado_representacao");
		sql.append(" JOIN projudi.usu_serv_grupo usg ON us.id_usu_serv = usg.id_usu_serv");
		sql.append(" JOIN projudi.grupo g ON usg.id_grupo = g.id_grupo AND g.grupo_codigo IN (" + GrupoDt.ADVOGADO_PARTICULAR + "," + GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO + ")");		
		sql.append(" JOIN projudi.proc_parte pp ON pp.id_proc_parte = pf.id_proc_parte");
		sql.append(" JOIN projudi.proc_parte_tipo ppt ON ppt.id_proc_parte_tipo = pp.id_proc_parte_tipo");
		sql.append(" LEFT JOIN projudi.arq a ON pf.id_arq = a.id_arq and a.usu_assinador is not null");
		sql.append(" LEFT JOIN projudi.arq_tipo ta ON a.id_arq_tipo = ta.id_arq_tipo AND ta.dje = 1");
		sql.append(" WHERE EXISTS (SELECT ppa.id_proc_parte FROM projudi.proc_parte_advogado ppa WHERE ppa.id_proc_parte = pp.id_proc_parte AND (ppa.dativo = 0 OR ppa.dativo IS NULL))");
		
		sql.append(" UNION ALL");
		
		/**
		 * Consulta as movimentações do tipo INTIMACAO_EFETIVADA_VIA_DIARIO_ELETRONICO com intimação para advogados externo ao processo
		 */
		sql.append(" SELECT m.id_proc, ");
		sql.append(" m.id_movi,");
		sql.append(" null as id_pend,");
		sql.append(" null as pend_tipo_codigo,");
		sql.append(" null as data_inicio,");
		sql.append(" null as id_parte_intimada,");
		sql.append(" null as nome_parte_intimada,");
		sql.append(" null as id_usu_resp,");
		sql.append(" null as nome_advogado_intimado,");
		sql.append(" a.ID_ARQ ,");
		sql.append(" a.NOME_ARQ ,");
		sql.append(" a.CONTENT_TYPE ,");
		sql.append(" a.USU_ASSINADOR,");
		sql.append(" a.RECIBO ,");
		sql.append(" ta.DJE");
		sql.append(" FROM projudi.movi m ");
		sql.append(" JOIN projudi.movi_tipo mt ON mt.id_movi_tipo = m.id_movi_tipo AND mt.movi_tipo_codigo = " + MovimentacaoTipoDt.INTIMACAO_EFETIVADA_VIA_DIARIO_ELETRONICO);
		sql.append(" JOIN projudi.movi_arq ma ON ma.id_movi = m.id_movi AND ma.id_movi_arq_acesso IN (" + MovimentacaoArquivoDt.ACESSO_NORMAL + "," + MovimentacaoArquivoDt.ACESSO_PUBLICO + ")");
		sql.append(" LEFT JOIN projudi.arq a ON a.id_arq = ma.id_arq and a.usu_assinador is not null");
		sql.append(" LEFT JOIN projudi.arq_tipo ta ON a.id_arq_tipo = ta.id_arq_tipo AND ta.dje = 1");
		sql.append(" WHERE m.data_realizacao BETWEEN ? AND ?");
		ps.adicionarDateTime(dataInicial); 
		ps.adicionarDateTime(dataFinal);
		
		sql.append(" ) vw");
		  
		sql.append(" JOIN projudi.proc p ON vw.id_proc = p.id_proc");
		sql.append(" JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = p.id_proc_tipo");
		sql.append(" JOIN projudi.movi m on m.id_movi = vw.id_movi");
		sql.append(" JOIN projudi.movi_tipo mt on m.id_movi_tipo = mt.id_movi_tipo");
		sql.append(" JOIN projudi.SERV s ON p.id_serv = s.id_serv");
		sql.append(" JOIN projudi.SERV_SUBTIPO ss ON s.id_serv_subtipo = ss.id_serv_subtipo");
		sql.append(" JOIN projudi.COMARCA c ON c.id_comarca = s.id_comarca");
		
		if (opcaoPublicacao > 0){
						
			sql.append(" WHERE 1=1");
			
			// Publicacao de 1o ou 2o Grau?
			if (opcaoPublicacao == 1){
				sql.append(" AND ss.serv_subtipo_codigo IN (?,?,?,?,?,?)");
			} else {
				sql.append(" AND ss.serv_subtipo_codigo NOT IN (?,?,?,?,?,?)");
			}
			
			//14,16,18,41,17
			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL); 
			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
			ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA); 
			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
						
			// Publicação de 1o grau: capital ou interior?
			if (opcaoPublicacao == 2){
				sql.append(" AND c.comarca_codigo IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
				
			} else if (opcaoPublicacao == 3){
				sql.append(" AND c.comarca_codigo NOT IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
			}
									
		}
		
		// Ordenação
		String sqlFinal = "SELECT * FROM (" + sql.toString() + ") ORDER BY ordem, comarca, serv, data_inicio asc, id_proc, id_pend, nome_parte_intimada, id_arq";
		
		try {
			rs = consultar(sqlFinal, ps);
			pendencias = agruparIntimacoesParaPublicar(rs);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return pendencias;
	}
	
	/** Versão de correção para 01/04 ate´ 10/04
	 * TODO: remover o código apos gerar os arquivos
	 * @param dataInicial
	 * @param dataFinal
	 * @param opcaoPublicacao
	 * @return
	 * @throws Exception
	 */
	public List<MovimentacaoIntimacaoDt> consultarIntimacoesParaPublicacaoV2(String dataInicial, String dataFinal, int opcaoPublicacao) throws Exception {
		
		List<MovimentacaoIntimacaoDt> pendencias = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p.ID_PROC");
		sql.append(" , p.PROC_NUMERO");
		sql.append(" , p.DIGITO_VERIFICADOR");
	    sql.append(" , p.FORUM_CODIGO");
	    sql.append(" , p.ANO");
	    sql.append(" , p.SEGREDO_JUSTICA");
	    sql.append(" , (SELECT PP1.ID_PROC_PARTE");
		sql.append(" || '@'");
		sql.append(" || PP1.NOME");
		sql.append(" || '@'");
		sql.append(" || PP1.PROC_PARTE_TIPO");
		sql.append(" FROM PROJUDI.VIEW_PROC_PARTE PP1");
		sql.append(" WHERE PP1.DATA_BAIXA IS NULL");
		sql.append(" AND PP1.PROC_PARTE_TIPO_CODIGO IN (1, 5)");
		sql.append(" AND PP1.ID_PROC = p.id_proc");
		sql.append(" AND ROWNUM = 1");
		sql.append(" ) AS PRIMEIRO_PROMOVENTE_RECORRENTE");
		sql.append(" , (SELECT PP2.ID_PROC_PARTE");
		sql.append(" || '@'");
		sql.append(" || PP2.NOME");
		sql.append(" || '@'");
		sql.append(" || PP2.PROC_PARTE_TIPO");
		sql.append(" FROM projudi.VIEW_PROC_PARTE PP2");
		sql.append(" WHERE PP2.DATA_BAIXA IS NULL");
		sql.append(" AND PP2.PROC_PARTE_TIPO_CODIGO IN (0, 6)");
		sql.append(" AND PP2.ID_PROC = p.id_proc");
		sql.append(" AND ROWNUM = 1");
		sql.append(" ) AS PRIMEIRO_PROMOVIDO_RECORRIDO");
	    sql.append(" , pt.ID_PROC_TIPO");
	    sql.append(" , pt.PROC_TIPO");
	    sql.append(" , vw.ID_PEND");
	    sql.append(" , vw.PEND_TIPO_CODIGO");
		sql.append(" , vw.DATA_INICIO");
		sql.append(" , vw.ID_PARTE_INTIMADA");
		sql.append(" , vw.NOME_PARTE_INTIMADA");
		sql.append(" , vw.ID_USU_RESP");
		sql.append(" , vw.NOME_ADVOGADO_INTIMADO");
		sql.append(" , m.ID_MOVI");
		sql.append(" , m.COMPLEMENTO");
		sql.append(" , mt.MOVI_TIPO");
		sql.append(" , m.DATA_REALIZACAO");
		sql.append(" , vw.ID_ARQ");
		sql.append(" , vw.NOME_ARQ");
		sql.append(" , vw.CONTENT_TYPE");
		sql.append(" , vw.USU_ASSINADOR"); 
		sql.append(" , vw.RECIBO");		
	    sql.append(" , vw.DJE");
		sql.append(" , c.COMARCA");
	    sql.append(" , s.SERV");
		sql.append(" , CASE WHEN ss.serv_subtipo_codigo = " + ServentiaSubtipoDt.CORTE_ESPECIAL + " THEN 0");
		sql.append(" 		WHEN ss.serv_subtipo_codigo IN (" + ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL + "," + ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL + "," + ServentiaSubtipoDt.UPJ_TURMA_RECURSAL + "," + ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL + ") THEN 9 ");
		sql.append( "		ELSE 1 END AS ordem");
		sql.append(" FROM ");
		
		sql.append("( ");
		sql.append(" SELECT ");
		sql.append(" pf.id_proc,");
		sql.append(" pf.id_movi,");
		sql.append(" pf.ID_PEND ,");
		sql.append(" pf.PEND_TIPO_CODIGO ,");
		sql.append(" pf.DATA_INICIO ,");
		sql.append(" pp.ID_PROC_PARTE AS ID_PARTE_INTIMADA ,");
		sql.append(" pp.NOME AS NOME_PARTE_INTIMADA ,");
		sql.append(" pf.id_usu_resp ,");
		sql.append(" (Oab.OAB_NUMERO");
		sql.append(" || '  '");
		sql.append(" || est.UF");
		sql.append(" || ' - '");
		sql.append(" || u.NOME) AS NOME_ADVOGADO_INTIMADO ,");
		sql.append(" a.ID_ARQ ,");
		sql.append(" a.NOME_ARQ ,");
		sql.append(" a.CONTENT_TYPE ,");
		sql.append(" a.USU_ASSINADOR,");
		sql.append(" a.RECIBO ,");		
		sql.append(" ta.DJE");
		sql.append(" FROM");
		
		/**
		 * Subconsulta responsável em buscar as pendências em PEND, com tipo INTIMACAO E CARTA_CITACAO, status CUMPRIDA, onde tem advogado 
		 * UNIAO
		 * Subconsulta responsável em buscar as pendências em PEND_FINAL, com tipo INTIMACAO E CARTA_CITACAO, status CUMPRIDA, onde tem advogado
		 */
		sql.append(" ( ");
		sql.append("   SELECT p.id_pend, p.id_pend_tipo, p.id_pend_status, p.data_inicio, p.id_proc, p.id_proc_parte, pr.id_usu_resp, ma.id_arq, p.id_movi, pr.id_serv, pt.pend_tipo_codigo");
		sql.append("   FROM projudi.pend p");
		sql.append("   JOIN projudi.pend_resp pr ON pr.id_pend = p.id_pend");
		sql.append("   JOIN projudi.pend_tipo pt ON pt.id_pend_tipo = p.id_pend_tipo");
		sql.append("   LEFT JOIN projudi.movi_arq ma ON ma.id_movi = p.id_movi AND ma.id_movi_arq_acesso IN (" + MovimentacaoArquivoDt.ACESSO_NORMAL + "," + MovimentacaoArquivoDt.ACESSO_PUBLICO + ")");
		sql.append("   WHERE p.id_pend_tipo IN (" + PendenciaTipoDt.INTIMACAO  + "," + PendenciaTipoDt.CARTA_CITACAO + ")");
		sql.append("    AND ((p.id_pend_status = " + PendenciaStatusDt.ID_CUMPRIDA + " and p.data_fim > to_date ('11/04/2019 10:30:00', 'DD/MM/YYYY HH24:MI:SS')) or (p.id_pend_status = 9 and exists (select usb.id_usu_serv from projudi.usu_serv_oab usb where usb.id_usu_serv = pr.id_usu_resp and pr.id_usu_resp is not null)))");
		sql.append("    AND p.id_proc_parte IS NOT NULL");
		sql.append("    AND p.DATA_INICIO BETWEEN ? AND ?"); 
		ps.adicionarDateTime(dataInicial); 
		ps.adicionarDateTime(dataFinal);
		sql.append("    AND (p.id_pend_pai IS NULL OR EXISTS (");
		sql.append(" 		SELECT p2.id_pend FROM projudi.pend_final p2 JOIN projudi.pend_tipo pt2 ON pt2.id_pend_tipo = p2.id_pend_tipo");
		sql.append(" 		WHERE p2.id_pend = p.id_pend_pai AND pt2.pend_tipo_codigo NOT IN (" + PendenciaTipoDt.INTIMACAO + "," + PendenciaTipoDt.CARTA_CITACAO + ")))");		
		sql.append("   UNION ALL");
		sql.append("   SELECT pf.id_pend, pf.id_pend_tipo, pf.id_pend_status, pf.data_inicio, pf.id_proc, pf.id_proc_parte, prf.id_usu_resp, ma.id_arq, pf.id_movi, prf.id_serv, ptf.pend_tipo_codigo");
		sql.append("   FROM projudi.pend_final pf");
		sql.append("   JOIN projudi.pend_final_resp prf ON prf.id_pend = pf.id_pend");
		sql.append("   JOIN projudi.pend_tipo ptf ON ptf.id_pend_tipo = pf.id_pend_tipo");	
		sql.append("   LEFT JOIN projudi.movi_arq ma ON ma.id_movi = pf.id_movi AND ma.id_movi_arq_acesso IN (" + MovimentacaoArquivoDt.ACESSO_NORMAL + "," + MovimentacaoArquivoDt.ACESSO_PUBLICO + ")");		
		sql.append("   WHERE pf.id_pend_tipo IN (" + PendenciaTipoDt.INTIMACAO  + "," + PendenciaTipoDt.CARTA_CITACAO + ")");
		sql.append("    AND ((pf.id_pend_status = " + PendenciaStatusDt.ID_CUMPRIDA + " and pf.data_fim > to_date ('11/04/2019 10:30:00', 'DD/MM/YYYY HH24:MI:SS')) or (pf.id_pend_status = 9 and exists (select usb.id_usu_serv from projudi.usu_serv_oab usb where usb.id_usu_serv = prf.id_usu_resp and prf.id_usu_resp is not null)))");
		sql.append("    AND pf.id_proc_parte IS NOT NULL");
		sql.append("    AND pf.DATA_INICIO BETWEEN ? AND ?");
		ps.adicionarDateTime(dataInicial); 
		ps.adicionarDateTime(dataFinal);
		sql.append("    AND (pf.id_pend_pai IS NULL OR EXISTS ( ");
		sql.append(" 		SELECT pf2.id_pend FROM projudi.pend_final pf2 JOIN projudi.pend_tipo ptf2 ON ptf2.id_pend_tipo = pf2.id_pend_tipo");
		sql.append(" 		WHERE pf2.id_pend = pf.id_pend_pai AND ptf2.pend_tipo_codigo NOT IN (" + PendenciaTipoDt.INTIMACAO + "," + PendenciaTipoDt.CARTA_CITACAO + ")))");
		sql.append(" ) pf");        
		sql.append(" JOIN projudi.usu_serv us ON us.id_usu_serv = pf.id_usu_resp");
		sql.append(" JOIN projudi.usu_serv_oab oab ON oab.id_usu_serv = us.id_usu_serv");
		sql.append(" JOIN projudi.usu u ON u.id_usu = us.id_usu");
		sql.append(" LEFT JOIN projudi.serv serv ON serv.id_serv = us.id_serv");
		sql.append(" LEFT JOIN projudi.estado est ON est.id_estado = serv.id_estado_representacao");
		sql.append(" JOIN projudi.usu_serv_grupo usg ON us.id_usu_serv = usg.id_usu_serv");
		sql.append(" JOIN projudi.grupo g ON usg.id_grupo = g.id_grupo AND g.grupo_codigo IN (" + GrupoDt.ADVOGADO_PARTICULAR + "," + GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO + ")");		
		sql.append(" JOIN projudi.proc_parte pp ON pp.id_proc_parte = pf.id_proc_parte");
		sql.append(" JOIN projudi.proc_parte_tipo ppt ON ppt.id_proc_parte_tipo = pp.id_proc_parte_tipo");
		sql.append(" LEFT JOIN projudi.arq a ON pf.id_arq = a.id_arq");
		sql.append(" LEFT JOIN projudi.arq_tipo ta ON a.id_arq_tipo = ta.id_arq_tipo AND ta.dje = 1");
		sql.append(" ) vw");
		  
		sql.append(" JOIN projudi.proc p ON vw.id_proc = p.id_proc");
		sql.append(" JOIN projudi.proc_tipo pt ON pt.id_proc_tipo = p.id_proc_tipo");
		sql.append(" JOIN projudi.movi m on m.id_movi = vw.id_movi");
		sql.append(" JOIN projudi.movi_tipo mt on m.id_movi_tipo = mt.id_movi_tipo");
		sql.append(" JOIN projudi.SERV s ON p.id_serv = s.id_serv");
		sql.append(" JOIN projudi.SERV_SUBTIPO ss ON s.id_serv_subtipo = ss.id_serv_subtipo");
		sql.append(" JOIN projudi.COMARCA c ON c.id_comarca = s.id_comarca");
								
		if (opcaoPublicacao > 0){
						
			sql.append(" WHERE 1=1");
			
			// Publicacao de 1o ou 2o Grau?
			if (opcaoPublicacao == 1){
				sql.append(" AND ss.serv_subtipo_codigo IN (?,?,?,?,?,?)");
			} else {
				sql.append(" AND ss.serv_subtipo_codigo NOT IN (?,?,?,?,?,?)");
			}
			
			//14,16,18,41,17
			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL); 
			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
			ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA); 
			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
						
			// Publicação de 1o grau: capital ou interior?
			if (opcaoPublicacao == 2){
				sql.append(" AND c.comarca_codigo IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
				
			} else if (opcaoPublicacao == 3){
				sql.append(" AND c.comarca_codigo NOT IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
			}
									
		}
		
		// Ordenação
		String sqlFinal = "SELECT * FROM (" + sql.toString() + ") ORDER BY ordem, comarca, serv, data_inicio asc, id_proc, id_pend, nome_parte_intimada, id_arq";
		
		try {
			rs = consultar(sqlFinal, ps);
			pendencias = agruparIntimacoesParaPublicar(rs);
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return pendencias;
	}
	
	/**
	 * Agrupa os registros de intimação por movimentação e data de inicio
	 * @param rs
	 * @return 
	 * @throws Exception
	 */
	public List<MovimentacaoIntimacaoDt> agruparIntimacoesParaPublicar(ResultSetTJGO rs) throws Exception {
		
		List<MovimentacaoIntimacaoDt> lista = new ArrayList<MovimentacaoIntimacaoDt>();
		
		String movimentacaoAtual = "-1";
		String pendenciaAtual = "-1";		
		
		List<String> advogadoAtual = new ArrayList<String>();
		List<String> arquivoAtual = new ArrayList<String>();
		
		PendenciaDt pendenciaDt = null;
		MovimentacaoIntimacaoDt movimentacaoIntimacaoDt = null;
							
		while (rs.next()){
			
			if (!movimentacaoAtual.equals(rs.getString("ID_MOVI"))){
				
				/*
				 * Caso de uma nova intimação.
				 * Copiar os dados de processo e nome das partes (polo ativo e passivo).
				 * Copiar os dados de movimentação (tipo e data de realização).
				 * Copiar os dados da pendência, se existir.
				 * Copiar o primeiro arquivo da movimentação.
				 */
								
				// Nova movimentação, limpar os advogados e arquivos anteriores
				arquivoAtual.clear();
				advogadoAtual.clear();
				
				movimentacaoIntimacaoDt = new MovimentacaoIntimacaoDt();
				movimentacaoIntimacaoDt.setId(rs.getString("ID_MOVI"));
				movimentacaoIntimacaoDt.setTipoMovimentacao(rs.getString("MOVI_TIPO"));
				movimentacaoIntimacaoDt.setDataRealizacao(rs.getString("DATA_REALIZACAO"));
				movimentacaoIntimacaoDt.setComplemento(rs.getString("COMPLEMENTO"));
								
				// Seta os dados do processo
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				processoDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				processoDt.setForumCodigo(rs.getString("FORUM_CODIGO"));
				processoDt.setAno(rs.getString("ANO"));
				processoDt.setProcessoTipo(rs.getString("PROC_TIPO"));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs.getString("SEGREDO_JUSTICA")));
				processoDt.setServentia(rs.getString("SERV"));
				
				if (rs.getString("PRIMEIRO_PROMOVENTE_RECORRENTE") != null){
					String partePoloAtivo[] = rs.getString("PRIMEIRO_PROMOVENTE_RECORRENTE").split("@");
					ProcessoParteDt poloAtivo = new ProcessoParteDt();
					poloAtivo.setNome(partePoloAtivo[1]);
					poloAtivo.setProcessoParteTipo(partePoloAtivo[2]);
					processoDt.addListaPoloAtivo(poloAtivo);
				}
				
				if (rs.getString("PRIMEIRO_PROMOVIDO_RECORRIDO") != null){
					String partePoloPassivo[] = rs.getString("PRIMEIRO_PROMOVIDO_RECORRIDO").split("@");
					ProcessoParteDt poloPassivo = new ProcessoParteDt();
					poloPassivo.setNome(partePoloPassivo[1]);
					poloPassivo.setProcessoParteTipo(partePoloPassivo[2]);
					processoDt.addListaPolosPassivos(poloPassivo);
				}
												
				/*
				 * Se existir a pendência, instanciar uma PendenciaDt e copiar os dados.
				 * Seta o nome da parte intimada e seu advogado (apenas o primeiro)				 
				 */
				
				if (rs.getString("ID_PEND") != null){
				
					pendenciaDt = new PendenciaDt();
					pendenciaDt.setId(rs.getString("ID_PEND"));
					pendenciaDt.setId_Processo(rs.getString("ID_PROC"));
					pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs.getDateTime("DATA_INICIO")));
					pendenciaDt.setPendenciaTipoCodigo(rs.getString("PEND_TIPO_CODIGO"));
					
					// Nome e tipo da parte da pendência
					pendenciaDt.setId_ProcessoParte(rs.getString("ID_PARTE_INTIMADA"));
					pendenciaDt.setNomeParte(rs.getString("NOME_PARTE_INTIMADA"));
					
					// Responsável da pendência (advogado)
					PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
					responsavel.setNomeUsuarioResponsavel(rs.getString("NOME_ADVOGADO_INTIMADO"));
					pendenciaDt.addResponsavel(responsavel);
					
					// Movimentação da pendência
					pendenciaDt.setMovimentacao(rs.getString("MOVI_TIPO"));
					pendenciaDt.setDataTemp(rs.getString("DATA_REALIZACAO"));
					
					pendenciaDt.setProcessoDt(processoDt);
					
					// Seta referência para a pendência e o processo
					movimentacaoIntimacaoDt.addPendencia(pendenciaDt);
													
				}
				
				/*
				 * Se existir arquivo na pendência e for publicável (DJE = 1), 
				 * Instanciar um ArquivoDt e setar as propriedades.
				 */
				if (ValidacaoUtil.isNaoVazio(rs.getString("ID_ARQ"))){
					if (!arquivoAtual.contains(rs.getString("ID_ARQ"))){
						if (rs.getInt("DJE") == 1){
							ArquivoDt arquivoDt = new ArquivoDt();
							arquivoDt.setId(rs.getString("ID_ARQ"));
							arquivoDt.setNomeArquivo(rs.getString("NOME_ARQ"));
							arquivoDt.setUsuarioAssinador(rs.getString("USU_ASSINADOR"));
							arquivoDt.setRecibo(Funcoes.FormatarLogico(rs.getString("RECIBO")));
							arquivoDt.setContentType(rs.getString("CONTENT_TYPE"));
							movimentacaoIntimacaoDt.addArquivo(arquivoDt);
						}
					}
				}
				
				// Seta referência para a pendência e o processo
				movimentacaoIntimacaoDt.setProcessoDt(processoDt);
				
				// Adiciona na lista de resultado
				lista.add(movimentacaoIntimacaoDt);
																
			} else {
				
				/*
				 * Mesma movimentação.
				 */
				
				if (rs.getString("ID_PEND") != null){
					
					if (!pendenciaAtual.equals(rs.getString("ID_PEND"))){
						
						/*
						 * Caso em que uma nova pendência utilizou a mesma movimentação.
						 * Com isso, copiar os dados da pendência, nome da parte intimada e o primeiro advogado.
						 * Copiar o primeiro arquivo da pendência.
						 */
						
						// Dados da pendência
						pendenciaDt = new PendenciaDt();
						pendenciaDt.setId(rs.getString("ID_PEND"));
						pendenciaDt.setId_Processo(rs.getString("ID_PROC"));
						pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs.getDateTime("DATA_INICIO")));
						pendenciaDt.setPendenciaTipoCodigo(rs.getString("PEND_TIPO_CODIGO"));
						
						// Nome e tipo da parte da pendência
						pendenciaDt.setId_ProcessoParte(rs.getString("ID_PARTE_INTIMADA"));
						pendenciaDt.setNomeParte(rs.getString("NOME_PARTE_INTIMADA"));
						
						// Responsável da pendência (advogado)
						PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
						responsavel.setNomeUsuarioResponsavel(rs.getString("NOME_ADVOGADO_INTIMADO"));
						pendenciaDt.addResponsavel(responsavel);
						
						// Movimentação da pendência
						pendenciaDt.setMovimentacao(rs.getString("MOVI_TIPO"));
						pendenciaDt.setDataTemp(rs.getString("DATA_REALIZACAO"));
						
						// Arquivo da pendência
						if (ValidacaoUtil.isNaoVazio(rs.getString("ID_ARQ"))){
							if (!arquivoAtual.contains(rs.getString("ID_ARQ"))){
								if (rs.getInt("DJE") == 1){
									ArquivoDt arquivoDt = new ArquivoDt();
									arquivoDt.setId(rs.getString("ID_ARQ"));
									arquivoDt.setNomeArquivo(rs.getString("NOME_ARQ"));
									arquivoDt.setUsuarioAssinador(rs.getString("USU_ASSINADOR"));
									arquivoDt.setRecibo(Funcoes.FormatarLogico(rs.getString("RECIBO")));
									arquivoDt.setContentType(rs.getString("CONTENT_TYPE"));
									movimentacaoIntimacaoDt.addArquivo(arquivoDt);
								}
							}
						}
						
						// Seta referência para a pendência
						movimentacaoIntimacaoDt.addPendencia(pendenciaDt);
						
						// Nova pendência, limpar lista de advogados anteriores
						advogadoAtual.clear();
						
					} else {
						
						/*
						 * Caso de mesma movimentação e pendência.
						 * É um novo advogado ou um novo arquivo.
						 */
						
						// Responsável da pendência (advogado)						
						if (!advogadoAtual.contains(rs.getString("ID_USU_RESP"))){
							PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();
							responsavel.setNomeUsuarioResponsavel(rs.getString("NOME_ADVOGADO_INTIMADO"));
							pendenciaDt.addResponsavel(responsavel);
						}
												
						if (ValidacaoUtil.isNaoVazio(rs.getString("ID_ARQ"))){
							if (!arquivoAtual.contains(rs.getString("ID_ARQ"))){
								if (rs.getInt("DJE") == 1){
									ArquivoDt arquivoDt = new ArquivoDt();
									arquivoDt.setId(rs.getString("ID_ARQ"));
									arquivoDt.setNomeArquivo(rs.getString("NOME_ARQ"));
									arquivoDt.setUsuarioAssinador(rs.getString("USU_ASSINADOR"));
									arquivoDt.setRecibo(Funcoes.FormatarLogico(rs.getString("RECIBO")));
									arquivoDt.setContentType(rs.getString("CONTENT_TYPE"));
									movimentacaoIntimacaoDt.addArquivo(arquivoDt);
								}
							}
						}
						
					}
								
				} else {
					
					 /*
					  * Caso que não existe pendência e há mais de um arquivo na movimentação.
					  * Copiar os arquivos para MovimentacaoIntimacaoDt 
					 */
					
					// Arquivo da pendência
					if (ValidacaoUtil.isNaoVazio(rs.getString("ID_ARQ"))){
						if (rs.getInt("DJE") > 0){
							ArquivoDt arquivoDt = new ArquivoDt();
							arquivoDt.setId(rs.getString("ID_ARQ"));
							arquivoDt.setNomeArquivo(rs.getString("NOME_ARQ"));
							arquivoDt.setUsuarioAssinador(rs.getString("USU_ASSINADOR"));
							arquivoDt.setRecibo(Funcoes.FormatarLogico(rs.getString("RECIBO")));
							arquivoDt.setContentType(rs.getString("CONTENT_TYPE"));
							movimentacaoIntimacaoDt.addArquivo(arquivoDt);
						}
						
					}
				}
			}
			
			// Atualiza os ponteiros
			movimentacaoAtual = rs.getString("ID_MOVI");
			pendenciaAtual = rs.getString("ID_PEND") != null ? rs.getString("ID_PEND") : "-1";
			advogadoAtual.add(rs.getString("ID_USU_RESP") != null ? rs.getString("ID_USU_RESP") : "-1");
			arquivoAtual.add(rs.getString("ID_ARQ") != null ? rs.getString("ID_ARQ") : "-1");
			
		}
		
		return lista;
	}
	
	public List<MovimentacaoIntimacaoDt> consultarArquivosParaPublicacaoDJE(String dataInicial, String dataFinal, int opcaoPublicacao) throws Exception{
		List<MovimentacaoIntimacaoDt> pendencias = new ArrayList<>();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();		
		StringBuilder sql = new StringBuilder();
		sql.append(" select vw.*, a.nome_arq, a.content_type, a.usu_assinador, a.recibo, at.dje");
		sql.append(" from (");
		sql.append(" select p.id_proc, p.proc_numero, p.digito_verificador, p.forum_codigo, p.ano, p.segredo_justica, pt.id_proc_tipo, pt.proc_tipo, pf.id_pend, pf.data_inicio");
		sql.append(" , m.id_movi, mt.movi_tipo, m.data_realizacao, s.id_serv, s.serv, c.comarca, c.comarca_codigo, ss.serv_subtipo_codigo, pfa.id_arq as id_arq");
		sql.append(" , (SELECT PP1.ID_PROC_PARTE");
		sql.append(" || '@'");
		sql.append(" || PP1.NOME");
		sql.append(" || '@'");
		sql.append(" || PP1.PROC_PARTE_TIPO");
		sql.append(" FROM PROJUDI.VIEW_PROC_PARTE PP1");
		sql.append(" WHERE PP1.DATA_BAIXA IS NULL");
		sql.append(" AND PP1.PROC_PARTE_TIPO_CODIGO IN (1, 5)");
		sql.append(" AND PP1.ID_PROC = p.id_proc");
		sql.append(" AND ROWNUM = 1");
		sql.append(" ) AS PRIMEIRO_PROMOVENTE_RECORRENTE");
		sql.append(" , (SELECT PP2.ID_PROC_PARTE");
		sql.append(" || '@'");
		sql.append(" || PP2.NOME");
		sql.append(" || '@'");
		sql.append(" || PP2.PROC_PARTE_TIPO");
		sql.append(" FROM projudi.VIEW_PROC_PARTE PP2");
		sql.append(" WHERE PP2.DATA_BAIXA IS NULL");
		sql.append(" AND PP2.PROC_PARTE_TIPO_CODIGO IN (0, 6)");
		sql.append(" AND PP2.ID_PROC = p.id_proc");
		sql.append(" AND ROWNUM = 1");
		sql.append(" ) AS PRIMEIRO_PROMOVIDO_RECORRIDO");		
		sql.append(" , CASE WHEN ss.serv_subtipo_codigo = 18 THEN 0 WHEN ss.serv_subtipo_codigo IN (4,5,6) THEN 9 ELSE 1 END AS ordem");     
		sql.append(" from projudi.pend_final pf");
		sql.append(" inner join projudi.pend_final_arq pfa on pf.id_pend = pfa.id_pend and pf.id_pend_tipo = 23 and pf.codigo_temp is null");
		sql.append(" inner join projudi.movi_arq ma on ma.id_arq = pfa.id_arq and ma.id_movi_arq_acesso in (1,2)");
		sql.append(" inner join projudi.movi m on m.id_movi = ma.id_movi");
		sql.append(" inner join projudi.movi_tipo mt on m.id_movi_tipo = mt.id_movi_tipo");
		sql.append(" inner join projudi.proc p on p.id_proc = m.id_proc");
		sql.append(" inner join projudi.proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo");
		sql.append(" left join projudi.cnj_classe cc on cc.ID_CNJ_CLASSE = pt.id_cnj_classe");
		sql.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
		sql.append(" inner join projudi.serv_subtipo ss on s.id_serv_subtipo = ss.id_serv_subtipo");
		sql.append(" inner join projudi.comarca c on c.id_comarca = s.id_comarca");		
		sql.append(" where pf.data_inicio BETWEEN ? AND ?");		
		sql.append(" ) vw inner join arq a on a.id_arq = vw.id_arq");
		sql.append(" inner join arq_tipo at on at.id_arq_tipo = a.id_arq_tipo and at.dje = 1");
		sql.append(" where a.arq is not null and a.recibo = 1 and a.content_type in ('text/plain','text/html','application/pdf','document/pdf','adobe/pdf')");
		
		ps.adicionarDateTime(dataInicial); 
		ps.adicionarDateTime(dataFinal);
		
		if (opcaoPublicacao > 0){
			
			// Publicacao de 1o ou 2o Grau?
			if (opcaoPublicacao == 1){
				sql.append(" and vw.serv_subtipo_codigo IN (?,?,?,?,?,?)");
			} else {
				sql.append(" and vw.serv_subtipo_codigo NOT IN (?,?,?,?,?,?)");
			}
			
			//14,16,18,41,17
			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CIVEL); 
			ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
			ps.adicionarLong(ServentiaSubtipoDt.CORTE_ESPECIAL);
			ps.adicionarLong(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA); 
			ps.adicionarLong(ServentiaSubtipoDt.SECAO_CIVEL);
						
			// Publicação de 1o grau: capital ou interior?
			if (opcaoPublicacao == 2){
				sql.append(" and vw.comarca_codigo IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
				
			} else if (opcaoPublicacao == 3){
				sql.append(" and vw.comarca_codigo NOT IN (?)"); ps.adicionarLong(ComarcaDt.GOIANIA);
			}
									
		}
		
		// Ordenação
		String sqlFinal = "SELECT * FROM (" + sql.toString() + ") ORDER BY ordem, comarca, serv, data_inicio asc";
						
		try {
			rs = consultar(sqlFinal, ps);
			while (rs.next()){
				
				MovimentacaoIntimacaoDt movimentacaoIntimacaoDt = new MovimentacaoIntimacaoDt();
				movimentacaoIntimacaoDt.setId(rs.getString("ID_MOVI"));
				movimentacaoIntimacaoDt.setTipoMovimentacao(rs.getString("MOVI_TIPO"));
				movimentacaoIntimacaoDt.setDataRealizacao(rs.getString("DATA_REALIZACAO"));
							
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				processoDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				processoDt.setForumCodigo(rs.getString("FORUM_CODIGO"));
				processoDt.setAno(rs.getString("ANO"));
				processoDt.setProcessoTipo(rs.getString("PROC_TIPO"));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs.getString("SEGREDO_JUSTICA")));
				processoDt.setServentia(rs.getString("SERV"));
				
				if (rs.getString("PRIMEIRO_PROMOVENTE_RECORRENTE") != null){
					String partePoloAtivo[] = rs.getString("PRIMEIRO_PROMOVENTE_RECORRENTE").split("@");
					ProcessoParteDt poloAtivo = new ProcessoParteDt();
					poloAtivo.setNome(partePoloAtivo[1]);
					poloAtivo.setProcessoParteTipo(partePoloAtivo[2]);
					processoDt.addListaPoloAtivo(poloAtivo);
				}
				
				if (rs.getString("PRIMEIRO_PROMOVIDO_RECORRIDO") != null){
					String partePoloPassivo[] = rs.getString("PRIMEIRO_PROMOVIDO_RECORRIDO").split("@");
					ProcessoParteDt poloPassivo = new ProcessoParteDt();
					poloPassivo.setNome(partePoloPassivo[1]);
					poloPassivo.setProcessoParteTipo(partePoloPassivo[2]);
					processoDt.addListaPolosPassivos(poloPassivo);
				}
				
				// Dados da pendência
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setId(rs.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs.getString("ID_PROC"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs.getDateTime("DATA_INICIO")));			
				pendenciaDt.setMovimentacao(rs.getString("MOVI_TIPO"));
				pendenciaDt.setDataTemp(rs.getString("DATA_REALIZACAO"));			
				movimentacaoIntimacaoDt.addPendencia(pendenciaDt);
				
				
				// Seta referência para a pendência e o processo
				movimentacaoIntimacaoDt.setProcessoDt(processoDt);
				
				// Arquivo da pendência
				if (ValidacaoUtil.isNaoVazio(rs.getString("ID_ARQ"))){
					if (rs.getInt("DJE") == 1){
						ArquivoDt arquivoDt = new ArquivoDt();
						arquivoDt.setId(rs.getString("ID_ARQ"));
						arquivoDt.setNomeArquivo(rs.getString("NOME_ARQ"));
						arquivoDt.setUsuarioAssinador(rs.getString("USU_ASSINADOR"));
						arquivoDt.setRecibo(Funcoes.FormatarLogico(rs.getString("RECIBO")));
						arquivoDt.setContentType(rs.getString("CONTENT_TYPE"));
						movimentacaoIntimacaoDt.addArquivo(arquivoDt);				
					}
				}
				
				pendencias.add(movimentacaoIntimacaoDt);
				
			}
						
		} catch (Exception ex){
			ex.printStackTrace();
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return pendencias;
	}

	public List consultarTodasPendenciasSemDataFimUsuarioServentia(String id_UsuarioServentia) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PEND p ";
		sql += " WHERE DATA_FIM IS NULL ";
		sql += " AND EXISTS (SELECT 1 ";
		sql += "               FROM PROJUDI.PEND_RESP pr ";
		sql += "              WHERE pr.ID_PEND = p.ID_PEND ";
		sql += "               AND pr.ID_USU_RESP = ?) ";
		ps.adicionarLong(id_UsuarioServentia);

		try{
			rs1 = this.consultar(sql,ps);

			while (rs1.next()) {
				PendenciaDt obj = new PendenciaDt();
				super.associarDt(obj, rs1);
				pendencias.add(obj);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
        
		return pendencias;
	}
	
	public boolean isAcessoLiberado(String idResponsavel, String idProcesso) throws Exception {

		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean existeLiberarAcesso = false;

		String sql = " SELECT *  FROM  PROJUDI.VIEW_PEND_ABERTAS_RESP pen WHERE pen.DATA_FIM IS NULL AND pen.DATA_LIMITE > SYSDATE AND pen.PEND_TIPO_CODIGO = ? AND pen.ID_PROC= ?";
		ps.adicionarLong(PendenciaTipoDt.LIBERACAO_ACESSO);		
		ps.adicionarLong(idProcesso);

		sql += " AND pen.ID_USU_RESP = ? ";			ps.adicionarLong(idResponsavel);
		
		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) existeLiberarAcesso = true;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return existeLiberarAcesso;
	}
	
	public boolean temPendenciaAberta(String idServ, String idProcesso, String idServCargo) throws Exception {

		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean existeLiberarAcesso = false;

		String sql = " SELECT *  FROM  PROJUDI.Pend pen INNER JOIN pend_tipo pt ON pen.id_pend_tipo = pt.id_pend_tipo "
				+ "INNER JOIN pend_resp pr ON pen.ID_PEND = pr.ID_PEND ";
		
		sql += " WHERE pen.DATA_FIM IS NULL ";
		
		sql += " AND pen.ID_PROC= ?";			ps.adicionarLong(idProcesso);
		sql += " AND (pr.ID_SERV = ? "; 			ps.adicionarLong(idServ);
		sql += " OR pr.ID_SERV_CARGO = ? "; 		ps.adicionarLong(idServCargo);
		sql += " ) ";
		
		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) existeLiberarAcesso = true;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return existeLiberarAcesso;
	}
	
//	public boolean isPendenciaPedidoContadoria(String idServ, String idProcesso, String idServCargo) throws Exception {
//
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		boolean existeLiberarAcesso = false;
//
//		String sql = " SELECT *  FROM  PROJUDI.pend pen INNER JOIN pend_tipo pt ON pen.id_pend_tipo = pt.id_pend_tipo "
//				+ "INNER JOIN pend_resp pr ON pen.ID_PEND = pr.ID_PEND "
//				+ " WHERE pen.DATA_FIM IS NULL AND pt.PEND_TIPO_CODIGO = ? AND pen.ID_PROC= ?";
//		
//		ps.adicionarLong(PendenciaTipoDt.PEDIDO_CONTADORIA);		
//		ps.adicionarLong(idProcesso);
//
//		sql += " AND (pr.ID_SERV = ? "; 			ps.adicionarLong(idServ);
//		sql += " OR pr.ID_SERV_CARGO = ? "; 		ps.adicionarLong(idServCargo);
//		sql += " ) ";
//		
//		try{
//			rs1 = this.consultar(sql, ps);
//			if(rs1.next()) existeLiberarAcesso = true;
//		
//        } finally{
//             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
//        } 
//
//		return existeLiberarAcesso;
//	}
	
//	public boolean isPendenciaPedidoCamaraSaude(String idServ, String idProcesso, String idServCargo) throws Exception {
//
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		boolean existeLiberarAcesso = false;
//
//		String sql = " SELECT *  FROM  PROJUDI.Pend pen INNER JOIN pend_tipo pt ON pen.id_pend_tipo = pt.id_pend_tipo "
//				+ "INNER JOIN pend_resp pr ON pen.ID_PEND = pr.ID_PEND "
//				+ " WHERE pen.DATA_FIM IS NULL AND pt.PEND_TIPO_CODIGO = ? AND pen.ID_PROC= ?";
//		
//		ps.adicionarLong(PendenciaTipoDt.PEDIDO_CAMARA_SAUDE);		
//		ps.adicionarLong(idProcesso);
//
//		sql += " AND (pr.ID_SERV = ? "; 			ps.adicionarLong(idServ);
//		sql += " OR pr.ID_SERV_CARGO = ? "; 		ps.adicionarLong(idServCargo);
//		sql += " ) ";
//		
//		try{
//			rs1 = this.consultar(sql, ps);
//			if(rs1.next()) existeLiberarAcesso = true;
//		
//        } finally{
//             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
//        } 
//
//		return existeLiberarAcesso;
//	}
	
	public void reAbrirPendencia(PendenciaDt pendenciaDt) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Date tempDate = new Date();
		
		try{
			
			String sqlSelect = " SELECT ID_PEND FROM PROJUDI.PEND WHERE ID_PEND = ? AND DATA_FIM is null ";
			ps.adicionarLong(pendenciaDt.getId());
			
			rs1 = this.consultar(sqlSelect, ps);
			if (rs1.next()){
				throw new MensagemException("Pendência (ID: "+ pendenciaDt.getId() +") não finalizada. Efetuar consulta novamente para atualizar. Dupla tentativa de abrir pendência.");
			}
			
			//Limpa os parâmetros utilizados anteriormente
			ps.limpar();
		
			// String id_pendencia, String id_UsuarioFinalizador, int pendenciaStatusCodigo
			String sqlUpdate = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
			// Monta subSELECT para pesquisar o id real do status
			sqlUpdate += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";
			ps.adicionarLong(pendenciaDt.getPendenciaStatusCodigo());
			sqlUpdate += ", ID_USU_FINALIZADOR = ?, DATA_FIM = ?";
			ps.adicionarLongNull();
			ps.adicionarDateTimeNull();
			sqlUpdate += " WHERE ID_PEND = ? AND DATA_FIM IS NOT NULL ";
			ps.adicionarLong(pendenciaDt.getId());		
			
			this.executarUpdateDelete(sqlUpdate, ps);
			
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}
	
	/**
	 * Consulta as pendencias do tipo conclusão que estão em aberto, e que o responsável seja a serventia passada
	 * 
	 * @param id_Serventia, serventia responsável pelas pendências
	 * @param id_Processo, id do processo	 
	 * 
	 * @author mmgomes
	 */
	public boolean isServentiaResponsavelConclusoesPendentes(String id_Serventia, String id_Processo) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean retorno = false;

		String sql = " SELECT COUNT(1) AS QUANTIDADE ";
		sql += " FROM PROJUDI.PEND P INNER JOIN PROJUDI.PEND_RESP PR ON (P.ID_PEND = PR.ID_PEND) ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO PT ON (P.ID_PEND_TIPO = PT.ID_PEND_TIPO) ";
		sql += " WHERE P.ID_PROC = ? "; ps.adicionarLong(id_Processo);		
		sql += " AND PR.ID_SERV_CARGO IN (SELECT DISTINCT ID_SERV_CARGO FROM SERV_CARGO WHERE ID_SERV  = ?) "; ps.adicionarLong(id_Serventia);
		sql += " AND P.DATA_FIM IS NULL ";
		sql += " AND PT.PEND_TIPO_CODIGO  IN (11,12,13,14,18,19,20,21,57,67,68,78,89,90,193) ";
				
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) retorno = (rs1.getLong("QUANTIDADE") > 0);		
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return retorno;
	}
	
	public void alterarPrioridadePendencia(String id_Pendencia, String PrioridadeCodigo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PEND SET ID_PROC_PRIOR = ?";
		ps.adicionarLong(PrioridadeCodigo);
		Sql += " WHERE ID_PEND = ?";

		ps.adicionarLong(id_Pendencia);
		executarUpdateDelete(Sql, ps);
	}
	
	
	
	/**
	 * Consulta as pendencias de um determinado processo a partir do idProcesso
	 * e do tipo da pendência.
	 * 
	 * @author Lucas Soares Rodrigues
	 * @since 23/05/2018
	 * @param String
	 *            idProcesso, id do processo que terá suas pendencias abertas
	 *            consultadas
	 * @param int
	 *            pendenciaTipoCodigo	           
	 * @return List
	 * @throws Exception
	 */
	public List consultarPendenciasProcessoPorTipo(String idProcesso, int pendenciaTipoCodigo) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT *";
		sql += " FROM PROJUDI.VIEW_PEND p ";
		sql += " WHERE p.ID_PROC = ? AND p.DATA_FIM is null AND (p.PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(idProcesso); ps.adicionarLong(pendenciaTipoCodigo); 
		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}
	
	public List<PendenciaDt> consultarPendenciasProcessoPorListaTipo(String idProcesso, Integer ...listaPendenciaTipoCodigo) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT *";
		sql += " FROM PROJUDI.VIEW_PEND p ";
		sql += " WHERE p.ID_PROC = ? AND p.DATA_FIM is null ";
		ps.adicionarLong(idProcesso);
		for (Integer tipo : listaPendenciaTipoCodigo) {
			ps.adicionarLong(tipo);
		}
		String tipos = Stream.of(listaPendenciaTipoCodigo).map(tipo -> "?").collect(Collectors.joining(","));
		sql += "AND p.PEND_TIPO_CODIGO IN (" + tipos + ")";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}
	
	public List<PendenciaDt> consultarPendenciasAudienciaProcessoPorListaTipo(String idAudiProc, Integer ...listaPendenciaTipoCodigo) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT *";
		sql += " FROM PROJUDI.VIEW_PEND p ";
		sql += " INNER JOIN AUDI_PROC_PEND app on app.ID_PEND = P.ID_PEND ";
		sql += " WHERE app.ID_AUDI_PROC = ? AND p.DATA_FIM is null ";
		ps.adicionarLong(idAudiProc);
		for (Integer tipo : listaPendenciaTipoCodigo) {
			ps.adicionarLong(tipo);
		}
		String tipos = Stream.of(listaPendenciaTipoCodigo).map(tipo -> "?").collect(Collectors.joining(","));
		sql += "AND p.PEND_TIPO_CODIGO IN (" + tipos + ")";
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}
	
	// jvosantos - 27/08/2019 14:25 - Adicionar parâmetro para filtrar por Serventia Responsável
	public List<PendenciaDt> consultarPendenciasAudienciaProcessoPorListaTipo(String idAudienciaProcesso, String idServentiaProcesso, String idServentiaResponsavel, Integer ...listaPendenciaTipoCodigo) throws Exception {
		List<PendenciaDt> pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM PROJUDI.");
		
		sql.append("VIEW_PEND P ");
		
		sql.append("INNER JOIN AUDI_PROC_PEND APP ON P.ID_PEND = APP.ID_PEND ");
		
		if(StringUtils.isNotEmpty(idServentiaProcesso))
			sql.append(" INNER JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = APP.ID_AUDI_PROC INNER JOIN PROC PROC ON PROC.ID_PROC = AP.ID_PROC ");
		
		if(StringUtils.isNotEmpty(idServentiaResponsavel))
			sql.append(" INNER JOIN PEND_RESP PR ON PR.ID_PEND = P.ID_PEND ");

		sql.append(" WHERE APP.ID_AUDI_PROC = ? ");
		ps.adicionarLong(idAudienciaProcesso);
		
		if(StringUtils.isNotEmpty(idServentiaProcesso)) {
			sql.append(" AND PROC.ID_SERV = ? ");
			ps.adicionarLong(idServentiaProcesso);
		}
		
		if(StringUtils.isNotEmpty(idServentiaResponsavel)) {
			sql.append(" AND PR.ID_SERV = ? ");
			ps.adicionarLong(idServentiaResponsavel);		
		}
		
		for (Integer tipo : listaPendenciaTipoCodigo) {
			ps.adicionarLong(tipo);
		}
		
		String tipos = Stream.of(listaPendenciaTipoCodigo).map(tipo -> "?").collect(Collectors.joining(","));
		sql.append("AND P.PEND_TIPO_CODIGO IN (");
			sql.append(tipos);
		sql.append(")");
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}
	
	// lrcampos - 25/11/2019 15:25 - Consulta Pendencias de Para Conhecimento de uma audienciaProcesso
		public List<PendenciaDt> consultarPendenciasAudienciaProcessoSessaoConhecimento(String idAudienciaProcesso, Integer ...listaPendenciaTipoCodigo) throws Exception {
			List<PendenciaDt> pendencias = new ArrayList();
			PreparedStatementTJGO ps =  new PreparedStatementTJGO();

			StringBuilder sql = new StringBuilder("SELECT * FROM PROJUDI.VIEW_PEND P INNER JOIN AUDI_PROC AP ON P.ID_PROC = AP.ID_PROC ");
			
			sql.append(" WHERE AP.ID_AUDI_PROC = ? AND P.DATA_FIM IS NULL ");
			ps.adicionarLong(idAudienciaProcesso);
			
			for (Integer tipo : listaPendenciaTipoCodigo) {
				ps.adicionarLong(tipo);
			}
			
			String tipos = Stream.of(listaPendenciaTipoCodigo).map(tipo -> "?").collect(Collectors.joining(","));
			sql.append("AND P.PEND_TIPO_CODIGO IN (");
				sql.append(tipos);
			sql.append(")");
			
			ResultSetTJGO rs1 = null;
			try{
				rs1 = this.consultar(sql.toString(), ps);
				while (rs1.next()) {
					PendenciaDt pendenciaDt = new PendenciaDt();
					super.associarDt(pendenciaDt, rs1);
					pendencias.add(pendenciaDt);
				}
			}
			finally{
				if (rs1 != null) rs1.close();
			}

			return pendencias;
		}
		
	/**
	 * Consulta as pendencias de um determinado processo a partir do idProcesso
	 * e do tipo da pendência.
	 * 
	 * @author Lucas Soares Rodrigues
	 * @since 23/05/2018
	 * @param String
	 *            idProcesso, id do processo que terá suas pendencias abertas
	 *            consultadas
	 * @param int
	 *            pendenciaTipoCodigo	           
	 * @return List
	 * @throws Exception
	 */
	public List<PendenciaDt> consultarPendenciasProcessoResponsavelPorTipo(String idProcesso, String idServentiaCargo, int pendenciaTipoCodigo) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT *";
		sql += " FROM PROJUDI.VIEW_PEND p "
				+ " JOIN PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ";
		sql += " WHERE p.ID_PROC = ? AND p.DATA_FIM is null AND (p.PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(idProcesso); ps.adicionarLong(pendenciaTipoCodigo); 
		sql += " AND PR.ID_SERV_CARGO = ?"; ps.adicionarLong(idServentiaCargo);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);			
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}

	public List<PendenciaDt> consultarPendenciasAudienciaProcessoResponsavelPorTipo(String idAudienciaProcesso, String idServentiaCargo, int pendenciaTipoCodigo) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT * FROM PROJUDI.VIEW_PEND P INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND INNER JOIN PEND_RESP PR ON P.ID_PEND = PR.ID_PEND WHERE APP.ID_AUDI_PROC = ? AND P.DATA_FIM IS NULL AND (P.PEND_TIPO_CODIGO = ?) AND PR.ID_SERV_CARGO = ?";
		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(pendenciaTipoCodigo); 
		ps.adicionarLong(idServentiaCargo);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);			
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}
	
	/**
     * Verifica se o processo já possui uma pendencia do tipo pedido CENOPES
     * 
     * @author Leandro Bernardes
     * @param  id_Processo, identificado do processo 
     * @return boolean
     */
	public boolean possuiPedidoCENOPES(String id_Processo) throws Exception {

		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean existePedidoCENOPES = false;

		String sql = " SELECT *  FROM  PROJUDI.PEND pen  inner join PROJUDI.PEND_TIPO pt on pen.id_pend_tipo = pt.id_pend_tipo WHERE pen.DATA_FIM IS NULL AND pt.PEND_TIPO_CODIGO = ? AND pen.ID_PROC= ?";
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_CENOPES);		
		ps.adicionarLong(id_Processo);

		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) existePedidoCENOPES = true;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return existePedidoCENOPES;
	}
	
	/**
     * Verifica se o processo já possui uma pendencia do tipo solicitação de carga para o usuário  e o processo passado
     * 
     * @author Leandro Bernardes
     * @param  id_Processo, identificado do processo
     * @param  id_UsuarioServentia, identificador do cadastrador
     * @return boolean
     */
	public boolean possuiPendeciaSolicitarCargaProcesso(String id_Processo, String id_UsuarioServentia) throws Exception {

		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean existePedido= false;

		String sql = " SELECT *  FROM  PROJUDI.PEND pen  inner join PROJUDI.PEND_TIPO pt on pen.id_pend_tipo = pt.id_pend_tipo "
				+ "WHERE pen.DATA_FIM IS NULL AND pt.PEND_TIPO_CODIGO = ? AND pen.ID_PROC= ? AND pen.ID_USU_CADASTRADOR = ? ";
		ps.adicionarLong(PendenciaTipoDt.SOLICITACAO_CARGA);		
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(id_UsuarioServentia);

		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) existePedido = true;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return existePedido;
	}
	
	/**
     * Verifica se o processo já possui uma pendencia do tipo solicitação de carga aguardando Retorno gerara pela serventia
     * 
     * @author Leandro Bernardes
     * @param  id_Processo, identificado do processo
     * @return boolean
     */
	public boolean possuiPendeciaSolicitarCargaAguardandoRetornoProcesso(String id_Processo) throws Exception {

		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean existePedido= false;

		String sql = " SELECT *  FROM  PROJUDI.PEND pen  "
				+ "inner join PROJUDI.PEND_TIPO pt on pen.id_pend_tipo = pt.id_pend_tipo "
				+ "inner join PROJUDI.PEND_STATUS ps on pen.id_pend_status = ps.id_pend_status "
				+ "WHERE pen.DATA_FIM IS NULL AND pt.PEND_TIPO_CODIGO = ? AND pen.ID_PROC= ? AND ps.PEND_STATUS_CODIGO = ? ";
		ps.adicionarLong(PendenciaTipoDt.SOLICITACAO_CARGA);		
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_RETORNO);

		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) existePedido = true;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return existePedido;
	}
	
	/**
	 * Consulta as Pendencias do Tipo (Verificar Novo Processo com Pedido de Assistência, Concluso com Pedido de Benefício de Assistência) se existirem a partir do idProcesso.
	 * 
	 * @param String idProcesso, id do processo que terá suas pendencias abertas consultadas	      
	 * @return List
	 * @throws Exception
	 */
	public List consultarPendenciasProcessoRelacionadasPedidoAssistencia(String idProcesso, int pendenciaTipoCodigo) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT *";
		sql += " FROM PROJUDI.VIEW_PEND p ";
		sql += " WHERE p.ID_PROC = ? AND p.DATA_FIM is null AND (p.PEND_TIPO_CODIGO = ?)";
		ps.adicionarLong(idProcesso); ps.adicionarLong(pendenciaTipoCodigo);
		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}
	
	 /**
		 * Método utilizado para alterar o tipo da pendencia de conclusão ao mudar status de uma guia aguardando deferimento
		 * 
		 * @param pendenciaDt,
		 *            identificação da pendência
		 * @param usuarioDt,
		 *            objeto para identificar o tipo de serventia do usuário logado
		    
	 */
	public void alterarTipoConclusaoPedidoAssistenciaParaGenericaOuRelator(PendenciaDt pendenciaDt, UsuarioDt usuarioDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set Id_Pend_tipo = (SELECT ID_PEND_TIPO FROM PROJUDI.PEND_TIPO WHERE PEND_TIPO_CODIGO = ?)"; 
		
		 if (usuarioDt.isServentiaTipo2Grau()) {
			 ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
		 } else {
			 ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO); 
		 }
		
		sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(pendenciaDt.getId());

		this.executarUpdateDelete(sql, ps);
	}
	
	
	/**
	 * Consulta os id_serv_cargo de todos os juízes de uma serventia.
	 * 
	 * @param String idServentia      
	 * @return List
	 * @throws Exception
	 */
	public List consultarJuizesServentia(String idServentia) throws Exception {
		List serventiaCargo = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT *";
		sql += " FROM PROJUDI.SERV_CARGO p ";
		sql += " WHERE p.id_serv = ? AND (p.id_cargo_tipo = ?)";
		ps.adicionarLong(idServentia); ps.adicionarLong(5);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				serventiaCargo.add(rs1.getString("ID_SERV_CARGO"));
				
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return serventiaCargo;
	}
	
	
	
	/**
	 * Consulta os id_serv_cargo do juíz responsável pela pendência 
	 * @param String idPendencia      
	 * @return List
	 * @throws Exception
	 */
	public String consultarServentiaCargoResponsavelPendencia(String idPendencia) throws Exception {
		String serventiaCargo = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		
		String sql = "select pr.ID_SERV_CARGO from pend_resp pr " +
		
		"inner join serv_cargo sc on sc.id_serv_cargo = pr.ID_SERV_CARGO " +
		"where pr.id_pend = ? and sc.id_cargo_tipo = 5 ";
		ps.adicionarLong(idPendencia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				serventiaCargo = rs1.getString("ID_SERV_CARGO");
				
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return serventiaCargo;
	}
	

	
	/**
	 * Consulta as datas das sessoes relacionadas às pendências elaboração de voto que foram criadas 
	 * @param String idPendencia      
	 * @return List
	 * @throws Exception
	 */
	public List consultarDataSessoesElaboracaoDeVoto(UsuarioNe usuarioNe) throws Exception {
		List dataSessoes = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
			
		
		String sql = "SELECT AU.DATA_AGENDADA FROM PROJUDI.PEND pasc  "
				+ " JOIN PEND_RESP PR ON PR.ID_PEND = pasc.ID_PEND"
				+ " JOIN PROC PV ON PV.ID_PROC = pasc.ID_PROC  "
				+ " JOIN AUDI_PROC AP ON AP.ID_PROC = PV.ID_PROC"
				+ " JOIN AUDI AU ON AU.ID_AUDI = AP.ID_AUDI"

				+ " WHERE PR.ID_SERV_CARGO = ?  "
				+ " AND AP.Data_Movi IS NULL ";

				if (usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe() != null && usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe().length() > 0) ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
				else ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargo());

				sql += " AND pasc.ID_PEND_TIPO = 106 AND NOT EXISTS "
				+ " (SELECT 1 FROM PROJUDI.PEND_ARQ pa WHERE pasc.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = 1 AND pa.CODIGO_TEMP = -3) "
				+ " ORDER BY AU.DATA_AGENDADA, PV.ID_CLASSIFICADOR, pasc.ID_PEND_TIPO, pasc.data_inicio";
		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				dataSessoes.add( Funcoes.FormatarDataHora(rs1.getString("data_agendada")));
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return dataSessoes;
	}
	
	

	
	
	
	public int consultarQuantidadeVotoSessaoVirtual(String idServentiaCargo) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;
		
		// lrcampos - 15/10/2019 11:08 - Correção do SQL para trazer o mesmo da grid correspondente
		StringBuilder sql =  new StringBuilder("SELECT "
				+ "COUNT(*) QUANTIDADE FROM PROJUDI.VIEW_PEND P "); 
		sql.append(" JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ");
		sql.append(" JOIN PROJUDI.AUDI_PROC AP ON P.ID_PROC = AP.ID_PROC ");
		sql.append(" JOIN PROJUDI.AUDI A ON A.ID_AUDI = AP.ID_AUDI ");
		sql.append(" INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON (APP.ID_PEND = P.ID_PEND AND AP.ID_AUDI_PROC = APP.ID_AUDI_PROC) ");
		sql.append(" JOIN PROJUDI.VIEW_SERV_CARGO SC ON SC.ID_SERV_CARGO = AP.ID_SERV_CARGO ");
		sql.append(" WHERE P.PEND_TIPO_CODIGO = ? ");  ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		sql.append(" AND PR.ID_SERV_CARGO = ? "); ps.adicionarLong(idServentiaCargo);
		sql.append(" AND AP.DATA_MOVI IS NULL ");
		sql.append(" AND P.PEND_STATUS_CODIGO = ? "); ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		

		try{
			rs1 = this.consultar(sql.toString(), ps);
			if(rs1.next()) quantidade = rs1.getInt("QUANTIDADE");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return quantidade;
	}
	
	
	// jvosantos - 10/10/2019 14:18 - Corrigir contador
	public int consultarQuantidadeFinalizarVoto(String idServentiaCargo, int status) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;

		StringBuilder sql = new StringBuilder(" SELECT COUNT(*) AS QUANTIDADE ");
		sql.append(" FROM ");
		sql.append(" 	PROJUDI.VIEW_AUDI_PROC_COMPLETA AP ");
		sql.append(" JOIN PROJUDI.AUDI_PROC_PEND APP ON ");
		sql.append(" 	AP.ID_AUDI_PROC = APP.ID_AUDI_PROC ");
		sql.append(" JOIN PROJUDI.VIEW_PEND PV ON ");
		sql.append(" 	(PV.ID_PEND = APP.ID_PEND ");
		sql.append(" 	AND PV.PEND_TIPO_CODIGO = ? "); ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
		sql.append(" 	AND PEND_STATUS_CODIGO = ?) "); ps.adicionarLong(status);
		sql.append(" JOIN AUDI_PROC_PEND APP ON ");
		sql.append(" 	APP.ID_PEND = PV.ID_PEND ");
		sql.append(" WHERE ");
		sql.append(" 	AP.ID_SERV_CARGO = ? "); ps.adicionarLong(idServentiaCargo);
		sql.append(" 	AND AP.AUDI_PROC_STATUS_CODIGO = ? "); ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);

		try{
			rs1 = this.consultar(sql.toString(), ps);
			if(rs1.next()) quantidade = rs1.getInt("QUANTIDADE");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return quantidade;
	}
	
	public int consultarQuantidadeFinalizarVotoPrazoExpirado(String idServentiaCargo) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;

		String sql = "SELECT COUNT(DISTINCT AP.ID_AUDI_PROC) AS QUANTIDADE "
				+ " FROM PROJUDI.VIEW_PEND P "
				+ " JOIN PROJUDI.AUDI_PROC AP ON P.ID_PROC = AP.ID_PROC ";
		sql += "WHERE P.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		sql += " AND P.DATA_LIMITE < SYSDATE ";
		sql += " AND AP.ID_SERV_CARGO = ? "; ps.adicionarLong(idServentiaCargo);
		sql += " AND AP.ID_AUDI_PROC_STATUS = ? "; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA); // jvosantos - 16/08/2019 15:22 - Adicionar verificação do Status da AudiProc

		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) quantidade = rs1.getInt("QUANTIDADE");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return quantidade;
	}
	
	
	public List consultarAguardandoVotoSessaoVirtual(String idServentiaCargo, String procNumero) throws Exception {
		List pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT A.DATA_AGENDADA, P.ID_PEND, P.DATA_LIMITE, P.ID_PROC, P.PROC_NUMERO_COMPLETO, AP.ID_AUDI_PROC FROM PROJUDI.VIEW_PEND P "
				+ " JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND "
				+ " JOIN PROJUDI.AUDI_PROC AP ON P.ID_PROC = AP.ID_PROC "
				+ " JOIN PROJUDI.AUDI A ON A.ID_AUDI = AP.ID_AUDI ";

		sql += " WHERE P.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);		
		sql += " AND PR.ID_SERV_CARGO = ?"; ps.adicionarLong(idServentiaCargo);
		sql += " AND AP.DATA_MOVI IS NULL";
		sql += " AND P.PEND_STATUS_CODIGO = ?"; ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO);
		sql += " AND NOT EXISTS (SELECT 1 FROM PEND_VOTO_VIRTUAL WHERE ID_PEND = P.ID_PEND)";
				
		if(procNumero != null && !procNumero.isEmpty()) {
			sql += " AND P.PROC_NUMERO_COMPLETO = ?";
			ps.adicionarString(procNumero);
		}
		
		
		ResultSetTJGO rs1 = null, rs2 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
				voto.setIdPendencia(rs1.getString("ID_PEND"));
				voto.setIdProcesso(rs1.getString("ID_PROC"));
				voto.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				voto.setDataSessao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
				voto.setPrazoVotacao(rs1.getString("DATA_LIMITE"));
				voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				
				
				PreparedStatementTJGO psVotos =  new PreparedStatementTJGO();
				
				pendencias.add(voto);
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}
	
	public int consultarQuantidadeAguardandoLeituraPedidoVista(String idServentiaCargo, String idServentia, int status) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;

		String sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PEND P JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ";
		sql += "WHERE P.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA_SESSAO);
		sql += " AND P.PEND_STATUS_CODIGO = ?"; ps.adicionarLong(status);
		
		if(idServentiaCargo == null) {
			sql += " AND PR.ID_SERV = ? "; ps.adicionarLong(idServentia);
		}
		else {
			sql += "AND PR.ID_SERV_CARGO = ?"; ps.adicionarLong(idServentiaCargo);
		}
		

		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) quantidade = rs1.getInt("QUANTIDADE");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return quantidade;
	}
	
	public ListaPendenciaDt consultarQuantidadeSessaoVirtualPorTipo(String idServentiaCargo, String idServentia, int codigo, int status) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;
		// lrcampos - 15/10/2019 11:08 - Correção do SQL para trazer o mesmo da grid correspondente
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT PT.PEND_TIPO, P.ID_PEND_TIPO, COUNT(*) AS QUANTIDADE  ");
		sql.append(" FROM  PROJUDI.VIEW_AUDI_PROC_COMPLETA AC");
		sql.append(" JOIN PROJUDI.AUDI_PROC_PEND APP ON APP.ID_AUDI_PROC = AC.ID_AUDI_PROC ");
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
		ps.adicionarLong(codigo);

		if(codigo == PendenciaTipoDt.RETIRAR_PAUTA) {
			sql.append(" AND ((AUDI.DATA_MOVI IS NULL ");
			sql.append(" AND AC.AUDI_PROC_STATUS_CODIGO = ? ");
			ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			sql.append(" AND P.CODIGO_TEMP IS NULL) ");
			sql.append(" OR (P.CODIGO_TEMP = AC.ID_AUDI_PROC AND P.ID_PROC = AC.ID_PROC))");
		} else if (codigo != PendenciaTipoDt.ADIADO_PELO_RELATOR) {
			sql.append(" AND AUDI.DATA_MOVI IS NULL ");
			sql.append(" AND AC.AUDI_PROC_STATUS_CODIGO = ? ");
			ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		}

		sql.append(" GROUP BY pt.PEND_TIPO, P.ID_PEND_TIPO ");
		ListaPendenciaDt listaPendenciaDt = new ListaPendenciaDt();
		try{
			rs1 = this.consultar(sql.toString(), ps);
			if(rs1.next()) {
				listaPendenciaDt.setIdTipo(String.valueOf(codigo));
				quantidade = rs1.getInt("QUANTIDADE");
				listaPendenciaDt.setTitulo(rs1.getString("PEND_TIPO"));
				listaPendenciaDt.setIdTipo(rs1.getString("ID_PEND_TIPO"));
				listaPendenciaDt.setCodigoTemp(String.valueOf(codigo));
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		listaPendenciaDt.setQuantidadePendencias(quantidade);
		return listaPendenciaDt;
	}
	
	public int consultarQuantidadeResultadoUnanime(String idServentiaCargo, String idServentia, int status) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;

		String sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PEND P JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ";
		sql += "WHERE P.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.RESULTADO_UNANIME);
		sql += " AND P.PEND_STATUS_CODIGO = ?"; ps.adicionarLong(status);
		
		if(idServentiaCargo == null) {
			sql += " AND PR.ID_SERV = ? "; ps.adicionarLong(idServentia);
		}
		else {
			sql += "AND PR.ID_SERV_CARGO = ?"; ps.adicionarLong(idServentiaCargo);
		}
		

		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) quantidade = rs1.getInt("QUANTIDADE");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return quantidade;
	}
	
	public int consultarQtdJulgamentoAdiadoAguardandoLeitura(String idServentiaCargo, int status) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;

		String sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PEND P JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ";
		sql += "WHERE P.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.RETIRAR_PAUTA);
		sql += " AND P.PEND_STATUS_CODIGO = ?"; ps.adicionarLong(status);
		
		if(idServentiaCargo == null) {
			sql += " AND PR.ID_SERV IS NOT NULL";
		}
		else {
			sql += "AND PR.ID_SERV_CARGO = ?"; ps.adicionarLong(idServentiaCargo);
		}
		

		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) quantidade = rs1.getInt("QUANTIDADE");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return quantidade;
	}
	
	public int consultarQtdSustentacaoOral(String idServentiaCargo, int status) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;

		String sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PEND P JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ";
		sql += "WHERE P.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
		sql += " AND P.PEND_STATUS_CODIGO = ?"; ps.adicionarLong(status);
		sql += " AND EXISTS (SELECT 1 FROM AUDI_PROC AP WHERE AP.ID_PROC = P.ID_PROC AND AP.DATA_MOVI IS NULL) "; 
		
		if(idServentiaCargo == null) {
			sql += " AND PR.ID_SERV IS NOT NULL";
		}
		else {
			sql += "AND PR.ID_SERV_CARGO = ?"; ps.adicionarLong(idServentiaCargo);
		}
		

		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) quantidade = rs1.getInt("QUANTIDADE");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return quantidade;
	}
	
	
	public void cadastrarVotantesSessaoVirtual(AudienciaProcessoVotantesDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlCampos = "INSERT INTO PROJUDI.AUDI_PROC_VOTANTES (";
		SqlValores = " Values (";
		
		SqlCampos += "ID_AUDI_PROC";
		SqlValores+="?";
		ps.adicionarLong(dados.getId_AudienciaProcesso());			
		
		SqlCampos += ",ID_SERV_CARGO";
		SqlValores+=",?";
		ps.adicionarLong(dados.getId_ServentiaCargo());			
		
		SqlCampos += ",RELATOR";
		SqlValores+=",?";
		ps.adicionarBoolean(dados.isRelator());
		
		SqlCampos += ",ID_IMPEDIMENTO_TIPO";
		SqlValores+=",?";
		ps.adicionarLong(dados.getId_ImpedimentoTipo());
		
		SqlCampos += ",ORDEM_VOTANTE";
		SqlValores+=",?";
		ps.adicionarLong(dados.getOrdemVotante());
		
		SqlCampos += ",ID_VOTANTE_TIPO";
		SqlValores+=",?";
		ps.adicionarLong(dados.getId_VotanteTipo());
		
		SqlCampos+=")";
 		SqlValores+=")";
 		
 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

		dados.setId(executarInsert(Sql, "ID_AUDI_PROC_VOTANTES", ps));	
		
	}
	
	public void cadastrarRecursoSecundarioParte(RecursoSecundarioParteDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlCampos = "INSERT INTO PROJUDI.RECURSO_SECUNDARIO_PARTE (";
		SqlValores = " Values (";
		
		SqlCampos += "ID_PROC_PARTE";
		SqlValores+="?";
		ps.adicionarLong(dados.getId_ProcessoParte());			
		
		SqlCampos += ",ID_PROC_PARTE_TIPO";
		SqlValores+=",?";
		ps.adicionarLong(dados.getId_ProcessoParteTipo());			
		
		SqlCampos += ",ID_PROC_TIPO";
		SqlValores+=",?";
		ps.adicionarLong(dados.getId_ProcessoTipo());
		
		SqlCampos += ",ID_AUDI_PROC";
		SqlValores+=",?";
		ps.adicionarLong(dados.getId_AudienciaProcesso());
		
		SqlCampos += ",ID_PROC_TIPO_RECURSO_SEC";
		SqlValores+=",?";
		ps.adicionarLong(dados.getId_ProcessoTipoRecursoSecundario());
		
		SqlCampos += ",ORDEM_PARTE";
		SqlValores+=",?";
		ps.adicionarLong(dados.getOrdemParte());
		
		SqlCampos+=")";
 		SqlValores+=")";
 		
 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

		dados.setId(executarInsert(Sql, "ID_RECURSO_SECUNDARIO_PARTE", ps));	
		
	}
	
	/**
	 * Método que gera o relatório de intimações lidas no dia anterior.
	 * @return lista de intimações
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List listarIntimacoesDiaAnterior() throws Exception {
		List pendencias = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		String sql = "SELECT * FROM VIEW_INTIMACAO_ADVOGADO";

		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) {
					pendencias = new ArrayList();
				}
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoNumero(rs1.getString("PROCESSO_NUMERO"));
				pendenciaDt.setCodigoTemp(rs1.getString("OAB_NUMERO") + rs1.getString("OAB_COMPLEMENTO") + rs1.getString("UF"));
				pendenciaDt.setDataInicio(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setComplemento(rs1.getString("COMPLEMENTO"));
				pendenciaDt.setDataTemp(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_REALIZACAO")));
				pendencias.add(pendenciaDt);
			}

		} finally {
			if (rs1 != null)
				rs1.close();
		}

		return pendencias;
	}
	
	// jvosantos - 04/06/2019 10:39 - Método que consulta as pendencias de apreciados
	public String consultarPendenciaApreciados(String idAudiProc) throws Exception {
		StringBuilder sql = new StringBuilder();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql.append("SELECT * FROM VIEW_PEND WHERE CODIGO_TEMP = ? AND PEND_TIPO_CODIGO = ? ORDER BY DATA_INICIO DESC");
		ps.adicionarLong(idAudiProc);
		
		ps.adicionarLong(PendenciaTipoDt.APRECIADOS);
		ResultSetTJGO rs1 = null;
		try {
			rs1 = this.consultar(sql.toString(), ps);
			if (rs1.next()) {
				return rs1.getString("ID_PEND");
			}

		} finally {
			if (rs1 != null) rs1.close();
		}
		
		return null;
	}
	
	/**
	 * Retorna todas as pendencias de publicacao publica filtrando pelo idArquivoTipo
	 * 
	 * @author lrcampos
	 * @since 02/12/2019 10:20
	 * @param String dataInicio, data de inicio
	 * @param String dataFinal, data de fim
	 * @param String idArquivoTipo
	 * @param String posicao, posicionamento
	 * @return List
	 * @throws Exception
	 */
	public List consultarPublicacaoPublicaPJD(String dataInicio, String dataFinal, String id_Serventia,
			String idArquivoTipo, String posicao, boolean isInterna, Boolean isVirtual) throws Exception {
		List pendencias = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();

		sql.append(" FROM VIEW_PEND_FINAL p ");
		sql.append(" INNER JOIN PEND_FINAL_ARQ PA ON PA.ID_PEND = P.ID_PEND ");
		sql.append(" INNER JOIN ARQ A ON A.ID_ARQ = PA.ID_ARQ ");
		sql.append(" WHERE p.PEND_TIPO_CODIGO = ? ");
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		if (id_Serventia != null && !id_Serventia.equals("")) {
			sql.append(" AND p.ID_SERV_FINALIZADOR = ? ");
			ps.adicionarLong(id_Serventia);
		}

		if (!isInterna) {
			sql.append(" AND EXISTS (SELECT 1 FROM PROJUDI.AUDI AD WHERE AD.ID_ARQ_FINALIZACAO = PA.ID_ARQ ");
			if(isVirtual != null) {
				if(isVirtual) {
					sql.append(" AND AD.VIRTUAL IS NOT NULL ");
				}else {
					sql.append(" AND AD.VIRTUAL IS NULL ");
				}
			}
			sql.append(" ) ");
		} else {
			sql.append(" AND EXISTS (SELECT 1 FROM PROJUDI.AUDI_PROC AP WHERE AP.ID_ARQ_ATA = PA.ID_ARQ AND AP.ID_AUDI_PROC_STATUS NOT IN (1, 7, 8, 10, 14, 34, 35, 54, 60))");
		}

		if (idArquivoTipo != null && !idArquivoTipo.equals("")) {
			sql.append(" AND A.ID_ARQ_TIPO = ? ");
			ps.adicionarLong(idArquivoTipo);
		}

		// Se tiver preenchido as duas datas
		if (dataInicio != null && dataFinal != null) {
			sql.append(" AND p.DATA_INICIO between ? AND ?");
			ps.adicionarDateTime(dataInicio);
			ps.adicionarDateTime(dataFinal);
		}

		sql.append(" ORDER BY DATA_INICIO desc");

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE " + sql;

		String sqlPublicacao = "SELECT * " + sql.toString();

		try {
			rs1 = this.consultarPaginacao(sqlPublicacao, ps, posicao);

			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendenciaDt.setServentiaUsuarioFinalizador(rs1.getString("SERV_FINALIZADOR"));
				pendenciaDt.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));

				pendencias.add(pendenciaDt);
			}

			rs2 = this.consultar(sqlSelQtd, ps);
			while (rs2.next())
				pendencias.add(rs2.getLong("QUANTIDADE"));
			// rs1.close();

		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e1) {
			}
			try {
				if (rs2 != null)
					rs2.close();
			} catch (Exception e1) {
			}
		}

		return pendencias;
	}

	
	/**
	 * Consulta para verificar se um processo está concluso
	 * 
	 * @param id_Processo, id do processo	
	 *  
	 * @return boolean
	 * @author lsbernardes
	 */
	public boolean processoConcluso(String id_Processo) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean retorno = false;

		String sql = " SELECT COUNT(1) AS QUANTIDADE ";
		sql += " FROM PROJUDI.PEND P ";
		sql += " INNER JOIN PROJUDI.PEND_TIPO PT ON (P.ID_PEND_TIPO = PT.ID_PEND_TIPO) ";
		sql += " WHERE P.ID_PROC = ? "; ps.adicionarLong(id_Processo);		
		sql += " AND P.DATA_FIM IS NULL ";
		sql += " AND PT.PEND_TIPO_CODIGO  IN (11,12,13,14,18,19,20,21,57,67,68,78,89,90,193) ";
				
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				retorno = (rs1.getLong("QUANTIDADE") > 0);			
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return retorno;
	}	
	
	/**
     * Consulta pendencias do tipo Intimação ou Citação de um procurador/advogado/defensor da serventia do usuário logado
	 * 
	 * @author lsbernardes
	 * @param id_UsuarioServentia, identificação do usuario para verificar se existe pendências
	 * @return int, quantidade de pendências encontradas
	 * @throws Exception
	 */
	public int possuiIntimacoesCitacoesAbertasUsuario(String id_UsuarioServentia) throws Exception {
		int retorno = 0;	
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = " SELECT count(1) QUANTIDADE"
		+ " FROM Serv S  "
		+ " JOIN Usu_Serv Us ON S.Id_Serv = Us.Id_Serv  "
		+ " JOIN usu u ON us.id_usu= u.id_usu  "
		+ " JOIN Pend_Resp Pr ON Pr.Id_Usu_Resp = Us.Id_Usu_Serv  "
		+ " JOIN Pend P   ON Pr.Id_Pend = P.Id_Pend " 
		+ " JOIN Pend_Tipo Pt ON P.Id_Pend_Tipo = Pt.Id_Pend_Tipo"
		+ " JOIN PROC Pro ON Pro.Id_Proc = P.Id_Proc "
		+ " JOIN Movi M ON M.Id_Movi = P.Id_Movi "
		+ " JOIN Movi_Tipo Mt ON Mt.Id_Movi_Tipo = M.Id_Movi_Tipo "
		+ " JOIN Pend_Status Ps ON Ps.Id_Pend_Status  = P.Id_Pend_Status "
		+ " WHERE Pr.ID_USU_RESP = ? AND ( P.CODIGO_TEMP <> ? OR P.CODIGO_TEMP is null ) AND (PT.PEND_TIPO_CODIGO = ? OR PT.PEND_TIPO_CODIGO = ? ) AND P.DATA_FIM IS NULL ";		
		ps.adicionarLong(id_UsuarioServentia);
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		ps.adicionarLong(PendenciaTipoDt.INTIMACAO);
		ps.adicionarLong(PendenciaTipoDt.CARTA_CITACAO);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				retorno = rs1.getInt("QUANTIDADE");
}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return retorno;
	}
	
	public List consultarIntimacoesCitacoesServentia(UsuarioNe usuarioNe) throws Exception {
		List pendencias = null;	
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = " SELECT distinct ID_PEND, ID_PROC, PROC_NUMERO_COMPLETO, ID_MOVI, MOVI_TIPO, PEND_TIPO, DATA_INICIO, DATA_LIMITE, NOME, ID_USU_SERV, PEND_STATUS FROM VIEW_PEND_INTIMACOES_PROC WHERE ID_SERV = ? AND ( CODIGO_TEMP <> ? OR CODIGO_TEMP is null ) AND DATA_FIM IS NULL ";		
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (pendencias == null) pendencias = new ArrayList();
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoDt(new ProcessoDt());
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setMovimentacao(rs1.getString("MOVI_TIPO"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setProcurador(rs1.getString("NOME"));
				pendenciaDt.setId_Procurador(rs1.getString("ID_USU_SERV"));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.getProcessoDt().setHash(usuarioNe.getCodigoHash(pendenciaDt.getId_Processo()));
				pendencias.add(pendenciaDt);
			}
		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendencias;
	}
	
	// jvosantos - 11/07/2019 18:15 - Adicionar pendência de "Verificar Resultado da Votação"
	public int consultarQuantidadeVerificarResultadoVotacao(String idServentiaCargo, int status) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;

		String sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PEND P JOIN PROJUDI.PEND_RESP PR ON P.ID_PEND = PR.ID_PEND ";
		sql += "WHERE P.PEND_TIPO_CODIGO = ? "; ps.adicionarLong(PendenciaTipoDt.VERIFICAR_RESULTADO_VOTACAO);
		sql += "AND PR.ID_SERV_CARGO = ?"; ps.adicionarLong(idServentiaCargo);
		sql += " AND P.PEND_STATUS_CODIGO = ?"; ps.adicionarLong(status);
		

		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) quantidade = rs1.getInt("QUANTIDADE");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return quantidade;
	}
	
	public boolean temPendenciaAbertaVotoSessaoVirtual(String idServCargo, String idProcesso) throws Exception {

		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean existeVotoSessaoVirtual = false;

		String sql = " SELECT * FROM PROJUDI.Pend pen INNER JOIN pend_tipo pt ON pen.id_pend_tipo = pt.id_pend_tipo "
				+ "INNER JOIN pend_resp pr ON pen.ID_PEND = pr.ID_PEND ";
		
		sql += " WHERE pen.DATA_FIM IS NULL ";
		sql += " AND pt.PEND_TIPO_CODIGO= ?"; ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);		
		sql += " AND pen.ID_PROC= ?";		  ps.adicionarLong(idProcesso);
		sql += " AND pr.ID_SERV_CARGO = ? ";  ps.adicionarLong(idServCargo);		
		
		try{
			rs1 = this.consultar(sql, ps);
			if(rs1.next()) existeVotoSessaoVirtual = true;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return existeVotoSessaoVirtual;
	}

	// jvosantos - 04/09/2019 17:07 - Implementar box de "Voto / Ementa - Aguradando Assinatura"
	public List<PendenciaDt> consultarPendenciasVotoEmentaAguardandoAssinatura(UsuarioNe usuarioNe) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List<PendenciaDt> pendencias = new ArrayList();
		
		String sql = " SELECT * FROM PROJUDI.view_pend pen INNER JOIN pend_tipo pt ON pen.id_pend_tipo = pt.id_pend_tipo "
				+ "INNER JOIN pend_status ps ON pen.id_pend_status = ps.id_pend_status "
				+ "INNER JOIN pend_resp pr ON pen.ID_PEND = pr.ID_PEND ";
		
		sql += " WHERE pen.DATA_FIM IS NULL ";
		sql += " AND pt.PEND_TIPO_CODIGO= ?"; ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		sql += " AND pr.ID_SERV_CARGO = ? ";  ps.adicionarLong(usuarioNe.getId_ServentiaCargo());		
		sql += " AND ps.PEND_STATUS_CODIGO = ? ";  ps.adicionarLong(PendenciaStatusDt.ID_CUMPRIDA_PARCIALMENTE);		
		
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				pendenciaDt.setProcessoDt(new ProcessoDt());
				pendenciaDt.setId(rs1.getString("ID_PEND"));
				pendenciaDt.setId_Processo(rs1.getString("ID_PROC"));
				pendenciaDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				pendenciaDt.setProcessoNumeroCompleto(rs1.getString("PROC_NUMERO_COMPLETO"));
				pendenciaDt.setId_Movimentacao(rs1.getString("ID_MOVI"));
				pendenciaDt.setPendenciaTipo(rs1.getString("PEND_TIPO"));
				pendenciaDt.setDataInicio( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_INICIO")));
				pendenciaDt.setDataLimite( Funcoes.FormatarDataHora(rs1.getDateTime("DATA_LIMITE")));
				pendenciaDt.setPendenciaStatus(rs1.getString("PEND_STATUS"));
				pendenciaDt.setHash(usuarioNe.getCodigoHash(pendenciaDt.getId()));
				pendenciaDt.getProcessoDt().setHash(usuarioNe.getCodigoHash(pendenciaDt.getId_Processo()));
				pendenciaDt.getProcessoDt().setId_Processo(pendenciaDt.getId_Processo());
				pendenciaDt.getProcessoDt().setProcessoNumero(pendenciaDt.getProcessoNumeroCompleto());
				pendencias.add(pendenciaDt);
			}
		} finally {		
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return pendencias;
	}

	// jvosantos - 10/10/2019 14:18 - Corrigir contador
	public int consultarQuantidadeFinalizarVotoPreAnalisado(String id_ServentiaCargo) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int quantidade = 0;

		StringBuilder sql = new StringBuilder(" SELECT ");
		sql.append(" 	COUNT(*) AS QUANTIDADE ");
		sql.append(" FROM ");
		sql.append(" 	( ");
		sql.append(" 	SELECT ");
		sql.append(" 		DISTINCT AC.ID_PROC ");
		sql.append(" 	FROM ");
		sql.append(" 		PROJUDI.PEND_VOTO_VIRTUAL PV ");
		sql.append(" 	JOIN PROJUDI.PEND P ON ");
		sql.append(" 		PV.ID_PEND = P.ID_PEND ");
		sql.append(" 	JOIN PROJUDI.PEND_RESP PR ON ");
		sql.append(" 		PV.ID_PEND = PR.ID_PEND ");
		sql.append(" 	INNER JOIN PROJUDI.AUDI_PROC_PEND APP ON ");
		sql.append(" 		APP.ID_PEND = P.ID_PEND ");
		sql.append(" 	JOIN PROJUDI.VIEW_AUDI_COMPLETA AC ON ");
		sql.append(" 		PV.ID_AUDI_PROC = AC.ID_AUDI_PROC ");
		sql.append(" 	WHERE ");
		sql.append(" 		PR.ID_SERV_CARGO = ? "); ps.adicionarLong(id_ServentiaCargo);
		sql.append(" 		AND ID_PEND_STATUS = ( ");
		sql.append(" 		SELECT ");
		sql.append(" 			ID_PEND_STATUS ");
		sql.append(" 		FROM ");
		sql.append(" 			PEND_STATUS ");
		sql.append(" 		WHERE ");
		sql.append(" 			PEND_STATUS_CODIGO = ?) "); ps.adicionarLong(PendenciaStatusDt.ID_EM_ANDAMENTO);
		sql.append(" 		AND ID_PEND_TIPO = ( ");
		sql.append(" 		SELECT ");
		sql.append(" 			ID_PEND_TIPO ");
		sql.append(" 		FROM ");
		sql.append(" 			PEND_TIPO ");
		sql.append(" 		WHERE ");
		sql.append(" 			PEND_TIPO_CODIGO = ?) ) "); ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);

		try{
			rs1 = this.consultar(sql.toString(), ps);
			if(rs1.next()) quantidade = rs1.getInt("QUANTIDADE");
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return quantidade;
	}
	//mrbatista - 10/12/2019 - consulta pendências finalizadas.
	public List<PendenciaDt> consultarPendenciasFinalizadasAudienciaProcessoPorListaTipo(String idAudienciaProcesso, String idServentiaProcesso, String idServentiaResponsavel, Integer ...listaPendenciaTipoCodigo) throws Exception {
		List<PendenciaDt> pendencias = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		StringBuilder sql = new StringBuilder("SELECT * FROM PROJUDI.VIEW_PEND_FINAL P INNER JOIN AUDI_PROC_PEND APP ON P.ID_PEND = APP.ID_PEND ");
		
		if(StringUtils.isNotEmpty(idServentiaProcesso))
			sql.append(" INNER JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = APP.ID_AUDI_PROC INNER JOIN PROC PROC ON PROC.ID_PROC = AP.ID_PROC ");
		
		if(StringUtils.isNotEmpty(idServentiaResponsavel))
			sql.append(" INNER JOIN PEND_RESP PR ON PR.ID_PEND = P.ID_PEND ");

		sql.append(" WHERE APP.ID_AUDI_PROC = ? ");
		ps.adicionarLong(idAudienciaProcesso);
		
		if(StringUtils.isNotEmpty(idServentiaProcesso)) {
			sql.append(" AND PROC.ID_SERV = ? ");
			ps.adicionarLong(idServentiaProcesso);
		}
		
		if(StringUtils.isNotEmpty(idServentiaResponsavel)) {
			sql.append(" AND PR.ID_SERV = ? ");
			ps.adicionarLong(idServentiaResponsavel);		
		}
		
		for (Integer tipo : listaPendenciaTipoCodigo) {
			ps.adicionarLong(tipo);
		}
		
		String tipos = Stream.of(listaPendenciaTipoCodigo).map(tipo -> "?").collect(Collectors.joining(","));
		sql.append("AND P.PEND_TIPO_CODIGO IN (");
			sql.append(tipos);
		sql.append(")");
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				PendenciaDt pendenciaDt = new PendenciaDt();
				super.associarDt(pendenciaDt, rs1);
				pendencias.add(pendenciaDt);
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return pendencias;
	}

	
	/**
	 * Consulta a lista de pendencias do tipo Conclusão que estão abertas no gabinete
	 * @author jrcorrea
	 * @since 21/11/2019 12:46
	 * @param id_Serventia do gabinete
	 */
	public List consultarListaConclusoesGabinete(String id_Serventia) throws Exception {
		List liTemp = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "SELECT pc.id_proc, pc.proc_numero || '-' || pc.digito_verificador proc_numero,  pt.pend_tipo, pp.id_proc_prior, pp.proc_prior, pp.proc_prior_codigo, sc.serv_cargo || ' - ' || u.nome usuario, trunc(sysdate - p.DATA_INICIO) dias FROM SERV_CARGO SC "; 
		sql += "    INNER JOIN PEND_RESP PR ON SC.ID_SERV_CARGO = PR.ID_SERV_CARGO ";  
		sql += "    INNER JOIN PEND P ON P.ID_PEND = PR.ID_PEND "; 
		sql += "    INNER JOIN PEND_TIPO PT ON P.ID_PEND_TIPO = PT.ID_PEND_TIPO "; 
		sql += "    INNER JOIN PROC_PRIOR PP ON P.ID_PROC_PRIOR = PP.ID_PROC_PRIOR "; 
		sql += "    INNER JOIN PROC PC ON PC.ID_PROC = P.ID_PROC "; 
		sql += "    INNER JOIN USU_SERV_GRUPO USG ON SC.ID_USU_SERV_GRUPO = USG.ID_USU_SERV_GRUPO "; 
		sql += "    INNER JOIN USU_SERV US ON USG.ID_USU_SERV = US.ID_USU_SERV "; 
		sql += "    INNER JOIN USU U ON US.ID_USU = U.ID_USU "; 
		sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?) ";
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);            		
		
		sql += " AND sc.id_serv= ?  AND p.data_fim is null ";		ps.adicionarLong(id_Serventia);
		sql += " ORDER BY dias desc, pc.id_proc asc";
				
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("id_proc"),rs1.getString("proc_numero"), rs1.getString("pend_tipo"), rs1.getString("ID_PROC_PRIOR"), rs1.getString("PROC_PRIOR"), rs1.getString("PROC_PRIOR_CODIGO"), rs1.getString("USUARIO"), rs1.getString("DIAS")});
			}
			
		
		}
		finally{
			 if (rs1 != null) rs1.close();
		}
		
		return liTemp;
	}
	
	/**
	 * Consulta a quantidade de pendencias do tipo Conclusão que estão abertas no gabinete
	 * @author jrcorrea
	 * @since 21/11/2019 12:46
	 * @param id_Serventia do gabinete
	 */
	public int  consultarQuantidadeConclusoesGabinete(String id_Serventia) throws Exception {
		int inTemp= 0;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql= "SELECT count(distinct p.id_pend) QUANTIDADE FROM SERV_CARGO SC "; 
		sql += "    INNER JOIN PEND_RESP PR ON SC.ID_SERV_CARGO = PR.ID_SERV_CARGO ";  
		sql += "    INNER JOIN PEND P ON P.ID_PEND = PR.ID_PEND "; 
		sql += "    INNER JOIN PEND_TIPO PT ON P.ID_PEND_TIPO = PT.ID_PEND_TIPO "; 
		sql += "    INNER JOIN PROC_PRIOR PP ON P.ID_PROC_PRIOR = PP.ID_PROC_PRIOR "; 
		sql += "    INNER JOIN PROC PC ON PC.ID_PROC = P.ID_PROC "; 
		sql += "    INNER JOIN USU_SERV_GRUPO USG ON SC.ID_USU_SERV_GRUPO = USG.ID_USU_SERV_GRUPO "; 
		sql += "    INNER JOIN USU_SERV US ON USG.ID_USU_SERV = US.ID_USU_SERV "; 
		sql += "    INNER JOIN USU U ON US.ID_USU = U.ID_USU "; 
		sql += " WHERE pt.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?) ";
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
            		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);            		
		
		sql += " AND sc.id_serv= ? AND p.data_fim is null ";		ps.adicionarLong(id_Serventia);

				
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);

			if (rs1.next()) {
				inTemp =  rs1.getInt("QUANTIDADE");
			}
			
		
		}
		finally{
			 if (rs1 != null) rs1.close();
		}
		
		return inTemp;
	}

	// jvosantos - 08/01/2020 17:29 - Método para buscar a quantida de pendências de Erro Material
	public long consultarQtdErroMaterial(boolean analisado, boolean aguardandoAssinatura, String idServentiaCargo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder("SELECT COUNT(1) QNT FROM PEND P INNER JOIN PEND_RESP PR ON PR.ID_PEND = P.ID_PEND INNER JOIN PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO INNER JOIN PEND_STATUS PS ON PS.ID_PEND_STATUS = P.ID_PEND_STATUS WHERE PT.PEND_TIPO_CODIGO = ? ");
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL);
		
		sql.append(" AND PR.ID_SERV_CARGO = ? ");
		ps.adicionarLong(idServentiaCargo);
		
		sql.append(" AND PS.PEND_STATUS_CODIGO = ? ");
		ps.adicionarLong(analisado ? PendenciaStatusDt.ID_PRE_ANALISADA : PendenciaStatusDt.ID_EM_ANDAMENTO);

		if(aguardandoAssinatura) {
			sql.append(" AND P.CODIGO_TEMP = ? ");
			ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP);
		}else
			sql.append(" AND P.CODIGO_TEMP IS NULL ");
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql.toString(), ps);
			if (rs1.next()) {
				return rs1.getLong("QNT");
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return 0;
	}

	// jvosantos - 13/01/2020 16:46 - Método para buscar as pendências de Erro Material
	public List<PendenciaDt> consultarQtdErroMaterial(String idServentiaCargo, boolean analisado, boolean aguardandoAssinatura) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List<PendenciaDt> list = new ArrayList<PendenciaDt>();
		StringBuilder sql = new StringBuilder("SELECT * FROM PEND P INNER JOIN PEND_RESP PR ON PR.ID_PEND = P.ID_PEND INNER JOIN PEND_TIPO PT ON PT.ID_PEND_TIPO = P.ID_PEND_TIPO INNER JOIN PEND_STATUS PS ON PS.ID_PEND_STATUS = P.ID_PEND_STATUS WHERE PT.PEND_TIPO_CODIGO = ? ");
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL);
		
		sql.append(" AND PR.ID_SERV_CARGO = ? ");
		ps.adicionarLong(idServentiaCargo);
		
		sql.append(" AND PS.PEND_STATUS_CODIGO = ? ");
		ps.adicionarLong(analisado ? PendenciaStatusDt.ID_PRE_ANALISADA : PendenciaStatusDt.ID_EM_ANDAMENTO);
		
		if(aguardandoAssinatura) {
			sql.append(" AND P.CODIGO_TEMP = ? ");
			ps.adicionarLong(PendenciaStatusDt.AGUARDANDO_ASSINATURA_PRE_ANALISE_CODIGO_TEMP);
		}
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql.toString(), ps);
			while (rs1.next()) {
				PendenciaDt dados = new PendenciaDt(); 
				associarDt(dados, rs1);
				list.add(dados);
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}

		return list;
	}

	/**
	 * Verifica se há pendências em um processo em uma determinada serventia, ou seja, qualquer pendência em aberto (até mesmo as reservadas).
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_Serventia, identificação da serventia
	 * 
	 * @author lsbernardes
	 */
	public List consultarPendenciasProcessoRetornoAutomaticoCEJUSC(String id_Processo, String id_Serventia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List pendenciasAbertas = new ArrayList();
			

		String sql = "SELECT p.ID_PEND, pr.ID_PEND_RESP, pr.ID_SERV FROM PROJUDI.VIEW_PEND_PROC p ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE p.data_fim is null AND p.ID_PROC = ? ";
		ps.adicionarLong(id_Processo);
		sql += " AND pr.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				pendenciasAbertas.add( new String[] {rs1.getString("ID_PEND")});
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendenciasAbertas;
	}
	
	/**
	 * Verifica se há pendências em um processo em uma determinada serventia, ou seja, qualquer pendência em aberto (até mesmo as reservadas).
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_Serventia, identificação da serventia
	 * 
	 * @author lsbernardes
	 * @author hrrosa
	 */
	public List consultarPendenciasProcessoArquivamentoAutomaticoExecucaoPenal(String id_Processo, String id_Serventia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List pendenciasAbertas = new ArrayList();
			

		String sql = "SELECT p.ID_PEND, pr.ID_PEND_RESP, pr.ID_SERV FROM PROJUDI.VIEW_PEND_PROC p ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE p.data_fim is null AND p.ID_PROC = ? ";
		ps.adicionarLong(id_Processo);
		sql += " AND pr.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				pendenciasAbertas.add( new String[] {rs1.getString("ID_PEND")});
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendenciasAbertas;
	}
	
	/**
	 * Verifica se há conclusões em aberto para um processo em uma determinada serventia.
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_Serventia, identificação da serventia
	 * 
	 * @author lsbernardes
	 * @author hrrosa
	 */
	public List consultarConclusoesProcessoArquivamentoAutomaticoExecucaoPenal(String id_Processo, String id_Serventia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List pendenciasAbertas = new ArrayList();
			

		String sql = "SELECT p.ID_PEND, pr.ID_PEND_RESP, pr.ID_SERV FROM PROJUDI.PEND p ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " INNER JOIN PROJUDI.SERV_CARGO sc on pr.ID_SERV_CARGO = sc.ID_SERV_CARGO";
		sql += " WHERE p.data_fim is null AND p.ID_PROC = ? ";
		ps.adicionarLong(id_Processo);
		sql += " AND sc.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				pendenciasAbertas.add( new String[] {rs1.getString("ID_PEND")});
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendenciasAbertas;
	}
	
	/**
	 * Verifica se há pendências Aguardando visto em um processo em uma determinada serventia
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_Serventia, identificação da serventia
	 * 
	 * @author lsbernardes
	 */
	public List consultarPendenciasAguardandoVistoProcesso(String id_Processo, String id_Serventia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List pendenciasAbertas = new ArrayList();
			

		String sql = "SELECT p.ID_PEND, pr.ID_PEND_RESP, pr.ID_SERV FROM PROJUDI.VIEW_PEND_PROC p ";
		sql += " INNER JOIN PROJUDI.PEND_RESP pr on p.ID_PEND = pr.ID_PEND";
		sql += " WHERE p.data_fim is not null AND p.data_visto is null AND p.ID_PROC = ? ";
		ps.adicionarLong(id_Processo);
		sql += " AND pr.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		
		
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				pendenciasAbertas.add( new String[] {rs1.getString("ID_PEND")});
			}

		
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		
		return pendenciasAbertas;
	}
	
	/**
	 * Metodo para fechar pendência - Funcioanlidade de retorno automático do CEJUSC
	 * 
	 * @author lsbernardes
	 * @param String
	 *             id_Pendencia, identificador da pendencia
	 * @throws Exception
	 */
	public void fecharPendenciaAutomaticamente(String id_Pendencia) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Date tempDate = new Date();
		
		try{
			
			String sqlSelect = " SELECT ID_PEND FROM PROJUDI.PEND WHERE ID_PEND = ? AND DATA_FIM is not null ";
			ps.adicionarLong(id_Pendencia);
			
			rs1 = this.consultar(sqlSelect, ps);
			if (rs1.next()){
				throw new MensagemException("Pendência (ID: "+id_Pendencia +") já finalizada. Efetuar consulta novamente para atualizar. Dupla tentativa de fechar pendência.");
			}
			
			//Limpa os parâmetros utilizados anteriormente
			ps.limpar();
		
			// String id_pendencia, String id_UsuarioFinalizador, int pendenciaStatusCodigo
			String sqlUpdate = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
			// Monta subSELECT para pesquisar o id real do status
			sqlUpdate += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";
			ps.adicionarLong(PendenciaStatusDt.ID_DESCARTADA);
			sqlUpdate += ", ID_USU_FINALIZADOR = ?, DATA_VISTO = ?, DATA_FIM = ?, ID_CLASSIFICADOR = ?";
			ps.adicionarLong(UsuarioServentiaDt.SistemaProjudi);
			ps.adicionarDateTime(tempDate);
			ps.adicionarDateTime(tempDate);	
			ps.adicionarBigDecimalNull();
			sqlUpdate += " WHERE ID_PEND = ? AND DATA_FIM IS NULL ";
			ps.adicionarLong(id_Pendencia);		
			
			this.executarUpdateDelete(sqlUpdate, ps);

		
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}
	
	/**
	 * Metodo para vistar pendência - Funcioanlidade de retorno automático do CEJUSC
	 * 
	 * @author lsbernardes
	 * @param String
	 *             id_Pendencia, identificador da pendencia
	 * @throws Exception
	 */
	public void vistarPendenciaAutomaticamente(String id_Pendencia) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Date tempDate = new Date();
		
		try{
			// String id_pendencia, String id_UsuarioFinalizador, int pendenciaStatusCodigo
			String sqlUpdate = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ";
			// Monta subSELECT para pesquisar o id real do status
			sqlUpdate += " (SELECT ps.ID_PEND_STATUS FROM PROJUDI.PEND_STATUS ps WHERE ps.PEND_STATUS_CODIGO = ?)";
			ps.adicionarLong(PendenciaStatusDt.ID_DESCARTADA);
			sqlUpdate += ", DATA_VISTO = ?, ID_CLASSIFICADOR = ?";
			ps.adicionarDateTime(tempDate);	
			ps.adicionarBigDecimalNull();
			sqlUpdate += " WHERE ID_PEND = ? ";
			ps.adicionarLong(id_Pendencia);		
			
			this.executarUpdateDelete(sqlUpdate, ps);

		
		} finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
	}
	
	/**
	 * Altera tipo da pendência
	 * 
	 * @author lsbernardes
	 * @param pendenciaDt
	 *            objeto de pendencia
	 * @param novoCodPendenciaTipo
	 *            novo tipo de pendência         
	 *   
	 * @throws Exception
	 */
	public void alterarTipoPendencia(PendenciaDt pendenciaDt, String novoCodPendenciaTipo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set ID_PEND_TIPO = (SELECT pt.ID_PEND_TIPO FROM PEND_TIPO pt where pt.PEND_TIPO_CODIGO = ?)"; ps.adicionarLong(novoCodPendenciaTipo);
		sql += " WHERE ID_PEND = ?";
		ps.adicionarLong(pendenciaDt.getId());

		this.executarUpdateDelete(sql, ps);
	}
	
	public List<CorreiosDt> consultarPendenciasCorreiosCompleta(String idPendencia, String pendenciaStatus, int offset, String numeroRegistros, boolean semMetaDados) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List<CorreiosDt> pendenciasAbertas = new ArrayList();
		String endereco = "";
			
		String sql = "SELECT * FROM PROJUDI.VIEW_PEND_CORREIOS_COMPLETA";
		
		if (pendenciaStatus != null && !pendenciaStatus.equalsIgnoreCase("")){
			sql += " WHERE ID_PEND_STATUS = ? ";
			ps.adicionarLong(pendenciaStatus);
		}		
		if (idPendencia != null && !idPendencia.equalsIgnoreCase("")){
			sql += " WHERE ID_PEND = ? ";
			ps.adicionarLong(idPendencia);
		}
		if (semMetaDados){
			sql += " WHERE META_DADOS IS NULL ";
		}
		if (offset >= 0){
			sql += " ORDER BY ID_PEND OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
			ps.adicionarLong(offset);
			if(numeroRegistros.equalsIgnoreCase("")) ps.adicionarLong("500");
			else ps.adicionarLong(numeroRegistros);
		}
							
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				CorreiosDt dt = new CorreiosDt();
				dt.setIdPendencia(rs1.getString("ID_PEND"));
				dt.setId_PendenciaTipo(rs1.getString("ID_PEND_TIPO"));
				dt.setNomeDestinatario(rs1.getString("NOME"));
				dt.setEnderecoDestinatario(rs1.getString("LOGRADOURO"));
				dt.setNumeroEnderecoDestinatario(rs1.getString("NUMERO"));
				dt.setComplementoEnderecoDestinatario(rs1.getString("COMPLEMENTO"));
				dt.setBairroDestinatario(rs1.getString("BAIRRO"));
				dt.setCidadeDestinatario(rs1.getString("CIDADE"));
				dt.setUfDestinatario(rs1.getString("UF"));
				dt.setCepDestinatario(Funcoes.completarZeros(rs1.getString("CEP"), 8));
				dt.setNomeRemetente(Funcoes.capitalizeNome(rs1.getString("SERV")));
				dt.setCidadeRemetente(rs1.getString("CIDADE_SERVENTIA"));
				dt.setUfRemetente(rs1.getString("UF_SERVENTIA"));
				dt.setCepRemetente(Funcoes.completarZeros(rs1.getString("CEP_SERVENTIA"),8));
				dt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				dt.setId_Serventia(rs1.getString("ID_SERV"));
				dt.setId_Processo(rs1.getString("ID_PROC"));
                dt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
                dt.setValorCausa(Funcoes.FormatarDecimal(rs1.getString("VALOR")));
                dt.setPoloAtivo(rs1.getString("PRIMEIRO_POLO_ATIVO"));
                dt.setPoloPassivo(rs1.getString("PRIMEIRO_POLO_PASSIVO"));
                dt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
                dt.setProcessoTipo(rs1.getString("PROC_TIPO"));
                dt.setId_Comarca(rs1.getString("ID_COMARCA"));
                dt.setComarca(Funcoes.capitalizeNome(rs1.getString("COMARCA")));
                dt.setMovimentacaoTipoCodigo(rs1.getString("MOVI_TIPO_CODIGO"));
                dt.setMovimentacaoTipo(rs1.getString("MOVI_TIPO"));
                dt.setId_Movimentacao(rs1.getString("ID_MOVI"));
                dt.setMovimentacaoComplemento(rs1.getString("MOVI_COMPLEMENTO"));
                dt.setMovimentacaoData(Funcoes.FormatarDataHora(rs1.getString("DATA_REALIZACAO")));
                dt.setNumeroGuia(Funcoes.FormatarNumeroSerieGuia(rs1.getString("GUIA_NUMERO")));
                dt.setPendenciaTipocodigo(rs1.getString("PEND_TIPO_CODIGO"));
                dt.setServicoAdicional(Funcoes.StringToBoolean(Funcoes.FormatarLogico(rs1.getString("MAO_PROPRIA"))));
                dt.setCodigoModelo(rs1.getString("COD_MODELO"));
                dt.setId_UsuarioServentiaCadastrador(rs1.getString("ID_USU_SERV"));
                dt.setUsuarioCadastrador(rs1.getString("NOME_CADASTRADOR"));
                dt.setTelefoneServentia(rs1.getString("FONE_SERVENTIA"));
                dt.setEmailRemetente(rs1.getString("EMAIL"));
                if(rs1.getString("LOGRADOURO_SERVENTIA") != null) endereco = rs1.getString("LOGRADOURO_SERVENTIA");
                if(rs1.getString("NUMERO_SERVENTIA") != null) endereco += ", " + rs1.getString("NUMERO_SERVENTIA");
                if(rs1.getString("COMPLEMENTO_SERVENTIA") != null) endereco += ", " + rs1.getString("COMPLEMENTO_SERVENTIA");
                if(rs1.getString("BAIRRO_SERVENTIA") != null) endereco += ", " + rs1.getString("BAIRRO_SERVENTIA");
                dt.setEnderecoRemetente(endereco);
                dt.setMetaDados(rs1.getString("META_DADOS"));
				pendenciasAbertas.add(dt);
			}
		
		}finally{
			if (rs1 != null) rs1.close();
		}
		return pendenciasAbertas;
	}
	
	public PendenciaDt consultarIdPendenciaStatusCorreios(String id_pendencia, int idPendenciaStatus)  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		PendenciaDt Dados=null;

		stSql= "SELECT * FROM PROJUDI.VIEW_PEND WHERE ID_PEND = ? AND ID_PEND_STATUS = ? ";		
		ps.adicionarLong(id_pendencia);
		ps.adicionarLong(idPendenciaStatus);

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				Dados= new PendenciaDt();
				associarDt(Dados, rs1);
				Dados.setId_Classificador(rs1.getString("ID_CLASSIFICADOR"));
				Dados.setClassificador(rs1.getString("CLASSIFICADOR"));
				Dados.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				Dados.setProcessoNumeroCompleto(rs1.getString("PROC_NUMERO_COMPLETO"));
				Dados.setId_ServentiaCadastrador(rs1.getString("ID_SERV_CADASTRADOR"));
				Dados.setId_ServentiaFinalizador(rs1.getString("ID_SERV_FINALIZADOR"));
				Dados.setMovimentacao(rs1.getString("MOVI"));
				Dados.setNomeUsuarioCadastrador(rs1.getString("NOME_USU_CADASTRADOR"));
				Dados.setNomeUsuarioFinalizador(rs1.getString("NOME_USU_FINALIZADOR"));
				Dados.setPendenciaPrioridadeCodigo(rs1.getString("PEND_PRIORIDADE_CODIGO"));
				Dados.setProcessoPrioridade(rs1.getString("PRIORI"));
				Dados.setServentiaCadastrador(rs1.getString("SERV_CADASTRADOR"));
				Dados.setServentiaFinalizador(rs1.getString("SERV_FINALIZADOR"));
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}}
		return Dados; 
	}
	
	/**
	 * Um item de guia postagem é pago e aguarda o envio para os correios
	 */
	public void alterarStatusAguardandoEnvioCorreios(String id_Pendencia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND SET ID_PEND_STATUS = ? WHERE ID_PEND = ?"; 
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS);
		ps.adicionarLong(id_Pendencia);
		this.executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Uma carta é enviada aos correios e aguarda a confirmação de recebimento
	 */
	public void alterarStatusAguardandoConfirmacaoCorreios(String id_Pendencia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND SET ID_PEND_STATUS = ? WHERE ID_PEND = ? AND ID_PEND_STATUS = ?"; 
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS);
		ps.adicionarLong(id_Pendencia);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS);
		this.executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Uma carta é recebida pelos correios e aguarda confirmação de entrega
	 */
	public void alterarStatusRecebidoCorreios(String id_Pendencia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND SET ID_PEND_STATUS = ? WHERE ID_PEND = ? AND ID_PEND_STATUS = ?"; 
		ps.adicionarLong(PendenciaStatusDt.ID_RECEBIDO_CORREIOS);
		ps.adicionarLong(id_Pendencia);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS);
		this.executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Uma carta é entregue(ou não) e aguarda o retorno do Aviso de Recebimento(AR) digital
	 * O status é defino como "Cumprido" ou "Não Cumprido"
	 */
	public void alterarStatusAguardandoRetornoARCorreios(PendenciaDt pendenciaDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND set ID_PEND_STATUS = ? WHERE ID_PEND = ? AND ID_PEND_STATUS = ?"; 
		ps.adicionarLong(pendenciaDt.getId_PendenciaStatus());
		ps.adicionarLong(pendenciaDt.getId());
		ps.adicionarLong(PendenciaStatusDt.ID_RECEBIDO_CORREIOS);
		this.executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Ocorreu uma inconsistência, foi finalizada a pendência para corrigir o endereço e agora é hora de reenviar a carta
	 */
	public void retornarStatusAguardandoEnvioCorreios(String id_ProcessoParte) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND SET ID_PEND_STATUS = ? WHERE ID_PROC_PARTE = ? AND ID_PEND_STATUS = ?"; 
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS);
		ps.adicionarLong(id_ProcessoParte);
		ps.adicionarLong(PendenciaStatusDt.ID_INCONSISTENCIA_CORREIOS);
		this.executarUpdateDelete(sql, ps);
	}

	/**
	 * Consutlar pendencias de prazos a decorrer da serventia
	 * 
	 * @author Leandro Bernardes
	 * @param usuarioDt
	 *            usuario que executa a acao
	 * @param posicao
	 *            paginacao
	 * @return quantidade de pendencia com prazo decorrido na serventia
	 * @throws ExceptionconsultarQtdPrazosDecorridosDecorrer
	 */
	public Long consultarQtdPrazosADecorrer(UsuarioDt usuarioDt) throws Exception {

		Long qtdPrazoDecorrido = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = " SELECT COUNT(*) as QTD_PRAZO_DECORRIDO  FROM  PROJUDI.PEND pen "
						+ "INNER JOIN PROJUDI.PEND_RESP pr on((pr.ID_PEND = pen.ID_PEND)) "
						+ "INNER JOIN PROJUDI.PEND_TIPO pt on((pt.ID_PEND_TIPO = pen.ID_PEND_TIPO)) ";
			   sql += " WHERE ( (pen.DATA_FIM is not null ) AND (pen.DATA_VISTO IS NULL)  AND (pen.DATA_LIMITE > SYSDATE) ) AND pr.ID_SERV = ? ";
			   ps.adicionarLong(usuarioDt.getId_Serventia());
		
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				qtdPrazoDecorrido = rs1.getLong("QTD_PRAZO_DECORRIDO");
			}
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 

		return qtdPrazoDecorrido;
	}
	
	public List consultarPendenciasAguardandoPagamentoPostagem(String idProcesso)  throws Exception {
		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List idPendencia = new ArrayList();

		stSql= "SELECT ID_PEND FROM PROJUDI.VIEW_PEND WHERE ID_PROC = ? AND ID_PEND_STATUS = ? ";		
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_PAGAMENTO_POSTAGEM);

		try{
			rs1 = consultar(stSql,ps);
			while (rs1.next()) {
				idPendencia.add(rs1.getString("ID_PEND"));
			}
		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}}
		return idPendencia; 
	}
	
	/**
     * Verifica se um determinado processo pussui a pendência Verifica Endereço Parte
     * 
     * @author asrocha
     * @param String idProcesso, processo que será verificado
     * @return boolean
     * @throws Exception
     */
	public boolean verificarExistenciaVerificarEnderecoParte(String id_ProcessoParte) throws Exception {
		boolean retorno = false;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT ID_PEND FROM PROJUDI.PEND WHERE ID_PROC_PARTE = ? AND DATA_FIM IS NULL AND ID_PEND_TIPO = ?";
		ps.adicionarLong(id_ProcessoParte); 
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE); 
		
		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				retorno = true;
			}
		}
		finally{
			if (rs1 != null) rs1.close();
		}
		return retorno;
	}
	
	/**
	 * Alterar data limite
	 * @param pendenciaDt objeto de pendencia
	 * @throws Exception
	 */
	public void fecharPendenciaEcarta(PendenciaDt pendenciaDt) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND SET DATA_FIM = ?, ID_USU_FINALIZADOR = ? WHERE ID_PEND = ?";
		ps.adicionarDateTime(pendenciaDt.getDataFim());
		ps.adicionarLong(UsuarioServentiaDt.SistemaProjudi);
		ps.adicionarLong(pendenciaDt.getId());
		this.executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Consutlar pendencias de prazos decorridos
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 13:51
	 * @param usuarioDt
	 *            usuario que executa a acao
	 * @param PendenciaTipoDt
	 *            tipo da pendencia
	 * @param numero_processo
	 *            numero do processo
	 * @param posicao
	 *            paginacao
	 * @return lista de pendencias com prazos
	 * @throws Exception
	 */
	public String consultarPrazosDecorridosADecorrerJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, String numero_processo, String posicao, boolean aDecorrer, boolean hash) throws Exception {
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 5;
		
		String sql = "";
		UsuarioDt usuarioDt = usuarioNe.getUsuarioDt();		
		
		if (aDecorrer)
			sql += " PROJUDI.VIEW_PEND_PRAZO_A_DECO_RESP ";
		else
			sql += " PROJUDI.VIEW_PEND_PRAZO_DECOR_RESP ";

		sql += " ppd WHERE " + " ppd.ID_SERV = ? ";
		ps.adicionarLong(usuarioDt.getId_Serventia());

		String filtroGenerico = this.filtroGenerico("ppd", true, pendenciaTipoDt, null, null, null, numero_processo, null, null, null, null, ps);

		sql += filtroGenerico;
		
		if (aDecorrer)
			sql += " ORDER BY ID_PEND ";
		else
			sql += " ORDER BY ID_PEND ";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql = "SELECT ID_PEND as id, PROC_NUMERO_COMPLETO as descricao1, PEND_TIPO as descricao2, MOVI as descricao3, to_char(DATA_LIMITE,'DD/MM/YYYY hh24:mi:ss') as descricao4, ID_PROC as descricao5 FROM " + sql;	

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			if(hash){
				pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
			}else{
				pendencias = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			}
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias finalizadas para um usuario
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 02/12/2008 11:04
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarIntimacoesCitacoesLidasJSON(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, PendenciaTipoDt pendenciaTipoDt, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 8;

		String sqlPrimeiraParte = " PROJUDI.VIEW_PEND_FINALIZADAS pff ";
		sqlPrimeiraParte += " INNER JOIN PROJUDI.PEND_FINAL_RESP pr on pr.ID_PEND = pff.ID_PEND WHERE ";
		
		if ((idUsuarioServentia != null && !idUsuarioServentia.equalsIgnoreCase(""))
				&& (idCargoServentia != null && !idCargoServentia.equalsIgnoreCase("")) ){
			sqlPrimeiraParte += " ( pr.ID_USU_RESP = ? OR pr.ID_SERV_CARGO = ? ) ";
			ps.adicionarLong(idUsuarioServentia); ps.adicionarLong(idCargoServentia);
			
		} else if (idUsuarioServentia != null && !idUsuarioServentia.equalsIgnoreCase("")){
			sqlPrimeiraParte += " pr.ID_USU_RESP = ? ";
			ps.adicionarLong(idUsuarioServentia);
		
		} else if (idCargoServentia != null && !idCargoServentia.equalsIgnoreCase("")){
			sqlPrimeiraParte += " pr.ID_SERV_CARGO = ? ";
			ps.adicionarLong(idCargoServentia);
		}
		
		sqlPrimeiraParte += " AND pff.PEND_STATUS_CODIGO <> ? ";
		ps.adicionarLong(PendenciaStatusDt.ID_DESCARTADA);
		
		String filtroGenerico = this.filtroIntimacaoCitacaoLidas("pff", true, pendenciaTipoDt, null, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);
		sqlPrimeiraParte += filtroGenerico;
		
		
		String sqlSegundaParte = " PROJUDI.VIEW_PEND_NORMAIS pn ";
		sqlSegundaParte += " INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = pn.ID_PEND WHERE ";
		
		if ((idUsuarioServentia != null && !idUsuarioServentia.equalsIgnoreCase(""))
				&& (idCargoServentia != null && !idCargoServentia.equalsIgnoreCase("")) ){
			sqlSegundaParte += " ( pr.ID_USU_RESP = ? OR pr.ID_SERV_CARGO = ? ) ";
			ps.adicionarLong(idUsuarioServentia); ps.adicionarLong(idCargoServentia);
			
		} else if (idUsuarioServentia != null && !idUsuarioServentia.equalsIgnoreCase("")){
			sqlSegundaParte += " pr.ID_USU_RESP = ? ";
			ps.adicionarLong(idUsuarioServentia);
		
		} else if (idCargoServentia != null && !idCargoServentia.equalsIgnoreCase("")){
			sqlSegundaParte += " pr.ID_SERV_CARGO = ? ";
			ps.adicionarLong(idCargoServentia);
		}
		
		sqlSegundaParte += " AND pn.PEND_STATUS_CODIGO <> ? ";
		ps.adicionarLong(PendenciaStatusDt.ID_DESCARTADA);
		
		sqlSegundaParte += " AND pn.data_fim IS NOT NULL ";

		String filtroGenerico2 = this.filtroIntimacaoCitacaoLidas("pn", true, pendenciaTipoDt, null, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);
		sqlSegundaParte += filtroGenerico2;
		
		String sql1 = "SELECT pff.id_pend as id, pff.id_proc as descricao1, pff.proc_numero_completo as descricao2, pff.movi as descricao3, pff.pend_tipo as descricao4, to_char(pff.data_fim,'DD/MM/YYYY hh24:mi:ss') as descricao5, to_char(pff.data_limite,'DD/MM/YYYY hh24:mi:ss') as descricao6, to_char(pff.data_visto,'DD/MM/YYYY hh24:mi:ss') as descricao7, 1 as descricao8, pff.data_fim FROM " + sqlPrimeiraParte;
		String sql2 = " UNION SELECT pn.id_pend as id, pn.id_proc as descricao1, pn.proc_numero_completo as descricao2, pn.movi as descricao3, pn.pend_tipo as descricao4, to_char(pn.data_fim,'DD/MM/YYYY hh24:mi:ss') as descricao5, to_char(pn.data_limite,'DD/MM/YYYY hh24:mi:ss') as descricao6, to_char(pn.data_visto,'DD/MM/YYYY hh24:mi:ss') as descricao7, 0 as descricao8, pn.data_fim FROM " + sqlSegundaParte;

		try{
			rs1 = this.consultarPaginacao(sql1+sql2+" ORDER BY data_fim desc ", ps, posicao);

			String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM ("+sql1+sql2+")";
			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias finalizadas para um usuario
	 * 
	 * @author Ronneesley Moura Teles/ Leandro Bernardes
	 * @since 02/12/2008 11:04
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarFinalizadasJSON(UsuarioNe usuarioNe, String id_UsuarioServentia, String id_ServentiaUsuario, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 8;

		String sql = "PROJUDI.VIEW_PEND_FINAIS_FINALIZADAS pff WHERE ";
		if (id_UsuarioServentia != null && !id_UsuarioServentia.equals("")){
			sql += " ID_USU_CADASTRADOR = ?";
			ps.adicionarLong(id_UsuarioServentia);
		}else if (id_ServentiaUsuario != null && !id_ServentiaUsuario.equals("")){
			sql += " (ID_SERV_CADASTRADOR = ? OR ID_SERV_FINALIZADOR = ? ) ";
			ps.adicionarLong(id_ServentiaUsuario);
			ps.adicionarLong(id_ServentiaUsuario);
		}

		sql += " AND (PEND_TIPO_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		boolean add = true;
		String filtroGenerico = this.filtroGenerico("pff", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_FIM desc";

		sql = "SELECT ID_PEND as id, ID_PROC as descricao1, PROC_NUMERO_COMPLETO as descricao2, MOVI as descricao3, PEND as descricao4, to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao5, to_char(DATA_FIM,'DD/MM/YYYY hh24:mi:ss') as descricao6, to_char(DATA_LIMITE,'DD/MM/YYYY hh24:mi:ss') as descricao7, PEND_STATUS as descricao8 FROM " + sql;

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias distribuidas de uma procuradoria
	 * 
	 * @author lsbernardes
	 * @since 21/03/2011
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarIntimacoesDistribuidasProcuradoriaJSON(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 5;
		
		String sql = "PROJUDI.VIEW_PEND_INTIMACOES_PROC pip WHERE pip.CODIGO_TEMP = ? AND pip.DATA_FIM IS NULL AND pip.ID_SERV = ? ";
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());

		boolean add = true;
		String filtroGenerico = this.filtroIntimacaoDistribuidas("pip", add, null, null, numero_processo, dataInicialInicio, dataFinalInicio, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_INICIO desc";

		sql = "SELECT DISTINCT ID_PEND as id, PEND_TIPO as descricao1, PROC_NUMERO_COMPLETO as descricao2, MOVI_TIPO as descricao3, to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao4, ID_PROC as descricao5, DATA_INICIO FROM " + sql;
		
		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd,ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return pendencias;
	}
	
	/**
	 * Consulta as pendencias distribuidas de uma promotoria
	 * 
	 * @author lsbernardes
	 * @since 23/02/2011
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarIntimacoesDistribuidasPromotoriaJSON(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {

		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 5;

		String sql = "PROJUDI.VIEW_PEND_INTIMACOES_PROM pip WHERE pip.CODIGO_TEMP = ? AND pip.DATA_FIM IS NULL AND pip.ID_SERV = ? ";
		ps.adicionarLong(PendenciaDt.DISTRIBUIDA);
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		
		String filtroGenerico = this.filtroIntimacaoDistribuidas("pip", true, null, null, numero_processo, dataInicialInicio, dataFinalInicio, ps);

		sql += filtroGenerico;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_INICIO desc";

		sql = "SELECT ID_PEND as id, PEND_TIPO as descricao1, PROC_NUMERO_COMPLETO as descricao2, MOVI_TIPO as descricao3 , to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao4 , ID_PROC as descricao5 FROM " + sql;	

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias respondidas da serventia
	 * 
	 * @author Leandro Bernardes
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarRespondidasServentiaJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 8;

		String sql = "PROJUDI.VIEW_PEND_RESPONDIDAS pff WHERE ID_SERV_CADASTRADOR <> ? AND  ID_SERV_FINALIZADOR = ? ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());

		sql += " AND (PEND_TIPO_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		boolean add = true;
		String filtroGenerico = this.filtroGenerico("pff", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_FIM desc";

		sql = "SELECT ID_PEND as id, PEND_TIPO as descricao1, MOVI as descricao2, PROC_NUMERO_COMPLETO as descricao3, to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao4, to_char(DATA_FIM,'DD/MM/YYYY hh24:mi:ss') as descricao5, to_char(DATA_LIMITE,'DD/MM/YYYY hh24:mi:ss') as descricao6, PEND_STATUS as descricao7, ID_PROC as descricao8  FROM " + sql;

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);
			
			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias respondidas  de um usuário
	 * 
	 * @author Leandro Bernardes
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarRespondidasUsuarioJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 8;

		String sql = "PROJUDI.VIEW_PEND_RESPONDIDAS pff WHERE ID_SERV_CADASTRADOR <> ? AND  ID_SERV_FINALIZADOR = ? AND ID_USU_FINALIZADOR = ?";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		
		sql += " AND (PEND_TIPO_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		boolean add = true;
		String filtroGenerico = this.filtroGenerico("pff", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_FIM desc";

		sql = "SELECT ID_PEND as id, PEND_TIPO as descricao1, MOVI as descricao2, PROC_NUMERO_COMPLETO as descricao3, to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao4, to_char(DATA_FIM,'DD/MM/YYYY hh24:mi:ss') as descricao5, to_char(DATA_LIMITE,'DD/MM/YYYY hh24:mi:ss') as descricao6, PEND_STATUS as descricao7, ID_PROC as descricao8 FROM " + sql;		

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias expedidas para serventias (on-line)
	 * 
	 * @author Leandro Bernardes
	 * @since 13/08/2009
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @param String
	 *            posicao, posicao da paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarExpedidasServentiaJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
		String Id_Serventia = usuarioNe.getUsuarioDt().getId_Serventia(); 
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 7;

		String sql = "PROJUDI.VIEW_PEND_NORMAIS pen WHERE DATA_VISTO IS NULL  AND ID_SERV_CADASTRADOR = ?  AND PEND_STATUS_CODIGO <> ? AND (DATA_LIMITE IS NULL) ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_Serventia());
		ps.adicionarLong(PendenciaStatusDt.ID_ENCAMINHADA);
		sql += this.filtroGenerico("pen", true, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, null, null, ps);

		sql += " ORDER BY DATA_INICIO , ID_PROC, PEND_STATUS_CODIGO";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql = "SELECT ID_PEND as id, PROC_NUMERO_COMPLETO as descricao1, MOVI as descricao2, PEND_TIPO as descricao3, PEND_STATUS as descricao4, to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao5, to_char(DATA_FIM,'DD/MM/YYYY hh24:mi:ss') as descricao6, ID_PROC as descricao7 FROM " + sql;

		try{
			 rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias que o usuario cadastrador e o usuario da serventia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 12/11/2008 15:31
	 * @param UsuarioDt
	 *            usuarioDt, vo do usuario
	 * @param String
	 *            posicao, posicao da paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarMinhasJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 7;

		String sql = "PROJUDI.VIEW_PEND_NORMAIS pen WHERE " + " ID_USU_CADASTRADOR = ? AND DATA_VISTO IS NULL AND PEND_STATUS_CODIGO <> ? AND (DATA_LIMITE IS NULL) ";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		ps.adicionarLong(PendenciaStatusDt.ID_ENCAMINHADA);

		sql += this.filtroGenerico("pen", true, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, null, null, ps);

		sql += " ORDER BY DATA_INICIO , ID_PROC, PEND_STATUS_CODIGO";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql = "SELECT ID_PEND as id, PROC_NUMERO_COMPLETO as descricao1, MOVI as descricao2, PEND_TIPO as descricao3, PEND_STATUS as descricao4, to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao5, to_char(DATA_FIM,'DD/MM/YYYY hh24:mi:ss') as descricao6, ID_PROC as descricao7 FROM " + sql;

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	 /**
     * Listagem de pendencias fechadas independente de estarem vistadas ou não.
     * 
     * @param dataInicio
     *            data de inicio
     * @param dataFinal
     *            data de fim
     * @param posicao
     *            posicao para paginacao
     * @return List
     * @throws Exception
     */
	public String consultarPendenciasFechadasJSON(UsuarioNe usuarioNe, String id_UsuarioServentia, String id_ServentiaUsuario, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, String numero_processo, String dataInicialInicio, String dataFinalInicio, String dataInicioFim, String dataFinalFim, String posicao) throws Exception {
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 8;

		String sql = "PROJUDI.VIEW_PEND_FINALIZADAS pff WHERE ";
		if (id_UsuarioServentia != null && !id_UsuarioServentia.equals("")){
			sql += " ID_USU_CADASTRADOR = ?";
			ps.adicionarLong(id_UsuarioServentia);
		}else if (id_ServentiaUsuario != null && !id_ServentiaUsuario.equals("")){
			sql += " (ID_SERV_CADASTRADOR = ? OR ID_SERV_FINALIZADOR = ? ) ";
			ps.adicionarLong(id_ServentiaUsuario);
			ps.adicionarLong(id_ServentiaUsuario);
		}

		sql += " AND (PEND_TIPO_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaTipoDt.PUBLICACAO_PUBLICA);

		boolean add = true;
		String filtroGenerico = this.filtroGenerico("pff", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, 0, numero_processo, dataInicialInicio, dataFinalInicio, dataInicioFim, dataFinalFim, ps);

		sql += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_FIM desc";

		sql = "SELECT ID_PEND as id, PROC_NUMERO_COMPLETO as descricao1, MOVI as descricao2, PEND as descricao3, to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao4, to_char(DATA_FIM,'DD/MM/YYYY hh24:mi:ss') as descricao5, to_char(DATA_LIMITE,'DD/MM/YYYY hh24:mi:ss') as descricao6, PEND_STATUS as descricao7, ID_PROC as descricao8 FROM " + sql;
		
		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias lidas para distribuição de uma promotoria
	 * 
	 * @author mmgomes
	 * @since 30/08/2012
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarIntimacoesLidasDistribuicaoPromotoriaJSON(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {

		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 6;

		String sql = "PROJUDI.VIEW_PEND_INTIMACOES_PROM pip WHERE NOT pip.DATA_FIM IS NULL ";
		
		String filtroGenerico = this.filtroIntimacaoDistribuidas("pip", true, null, null, numero_processo, dataInicialInicio, dataFinalInicio, ps);

		sql += filtroGenerico;

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY DATA_INICIO desc";

		sql = "SELECT ID_PEND as id, PEND_TIPO as descricao1, PROC_NUMERO_COMPLETO as descricao2, MOVI_TIPO as descricao3 , to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao4 , to_char(DATA_FIM,'DD/MM/YYYY hh24:mi:ss') as descricao5, ID_PROC as descricao6 FROM " + sql;	

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
	/**
	 * Consulta as pendencias lidas para distribuição de uma procuradoria
	 * 
	 * @author mmgomes
	 * @since 30/08/2012
	 * @param String
	 *            dataInicio, data de inicio
	 * @param String
	 *            dataFinal, data de fim
	 * @param UsuarioDt
	 *            usuarioDt, vo de usuario
	 * @param String
	 *            posicao, posicao para paginacao
	 * @return List
	 * @throws Exception
	 */
	public String consultarIntimacoesLidasDistribuicaoProcuradoriaJSON(UsuarioNe usuarioNe, String idUsuarioServentia, String idCargoServentia, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 6;

		String sql = "PROJUDI.VIEW_PEND_INTIMACOES_PROC pip WHERE NOT pip.DATA_FIM IS NULL ";

		String filtroGenerico = this.filtroIntimacaoDistribuidas("pip", true, null, null, numero_processo, dataInicialInicio, dataFinalInicio, ps);

		sql += filtroGenerico;		

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		sql += " ORDER BY descricao4";

		sql = "SELECT ID_PEND as id, PEND_TIPO as descricao1, PROC_NUMERO_COMPLETO as descricao2, MOVI_TIPO as descricao3 , to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao4 , to_char(DATA_FIM,'DD/MM/YYYY hh24:mi:ss') as descricao5, ID_PROC as descricao6 FROM " + sql;	
		
		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return pendencias;
	}
	
	/**
	 * Ocorreu uma inconsistência, vai ser gerada uma pendência de verificar endereço. O status vai para Inconsistência Correios
	 */
	public void alterarStatusInconsistenciaCorreios(String idPendencia) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "UPDATE PROJUDI.PEND SET ID_PEND_STATUS = ? WHERE ID_PEND = ? AND ID_PEND_STATUS IN (?, ?)"; 
		ps.adicionarLong(PendenciaStatusDt.ID_INCONSISTENCIA_CORREIOS);
		ps.adicionarLong(idPendencia);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS);
		this.executarUpdateDelete(sql, ps);
	}
	
	/**
	 * Consulta pendencias abertas na serventia Pendencia aberta e: Pendencia
	 * quen nao tem usuario realizador E que nao foi reservada por um usuario
	 * em uma determinada serventia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 07/05/2008 - 16:36 Ateracoes Mudanca na relacao de
	 *        responsabilidade da pendencia e no que o usuario podera visualizar
	 * @author Ronneesley Moura Teles
	 * @since 17/06/2008 - 10:20
	 * 
	 * Retirada do generics, devido a logica atual de programacao do projeto
	 * @author Ronneesley Moura Teles
	 * @since 28/08/2008 17:45
	 * 
	 * Inclusão do parâmetro Id_Serventia (Refatoração)
	 * @author Márcio Gomes
	 * @since 30/09/2010 07:30
	 * 
	 * @param usuarioDt
	 *            usuario da serventia
	 * @param pendenciaTipoDt
	 *            vo de tipo de pendencia
	 * @param pendenciaStatusDt
	 *            vo de status de pendencia
	 * @param prioridade
	 *            ordenar por prioridade na busca das pendencias
	 * @param filtroTipo
	 *            filtro por tipo de pendencia, 1 somente processo, 2 somente
	 *            normais os demais todas
	 * @param numero_processo
	 *            filtro para pesquisa
	 * @param dataInicialInicio
	 *            data inicial para o inicio
	 * @param dataFinalInicio
	 *            data final para o inicio
	 * @param posicao
	 *            numero para paginacao
	 * @return lista de pendencias abertas
	 * @throws Exception
	 */
	public String consultarAbertasJSON(UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, boolean prioridade, Integer filtroTipo, Integer filtroCivelCriminal, String numero_processo, String dataInicialInicio, String dataFinalInicio, String posicao) throws Exception {
		String Id_Serventia = usuarioNe.getUsuarioDt().getId_Serventia(); 
		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 6;

		String sqlSel = prioridade == true ? "VIEW_PEND_ABERTAS_PRIOR_RESP" : "VIEW_PEND_ABERTAS_SERV_RESP";

		sqlSel += " pen WHERE ";

		boolean add = false;
		
		if (usuarioNe.getUsuarioDt() != null && (usuarioNe.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL 
				|| usuarioNe.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL
				|| usuarioNe.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL
	    		|| usuarioNe.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL) ){
			 	sqlSel += " (PEND_TIPO_CODIGO NOT IN ( ?,?,?,? )) ";	
		    	ps.adicionarLong(PendenciaTipoDt.VERIFICAR_DISTRIBUICAO); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO);  ps.adicionarLong(PendenciaTipoDt.VERIFICAR_CONEXAO); ps.adicionarLong(PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO);
		    	add = true;		 
		}
		
		String filtroGenerico = this.filtroGenerico("pen", add, pendenciaTipoDt, pendenciaStatusDt, filtroTipo, filtroCivelCriminal, numero_processo, dataInicialInicio, dataFinalInicio, null, null, ps);

		sqlSel += filtroGenerico;

		if (!filtroGenerico.trim().equals("")) add = true;

		sqlSel += add ? "and" : "";

		sqlSel += "  ( ( pen.ID_SERV = ? AND pen.ID_SERV_TIPO IS NULL AND " + "pen.ID_USU_RESP IS NULL AND PEND_STATUS_CODIGO <> ? ) ) ";
		ps.adicionarLong(Id_Serventia);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_VISTO);

		//retirando status e-carta --------------------------------------------------------------------------------------------------------
		sqlSel += " AND (PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ?) ";
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_PAGAMENTO_POSTAGEM);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS);
		ps.adicionarLong(PendenciaStatusDt.ID_RECEBIDO_CORREIOS);
		ps.adicionarLong(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS);
		sqlSel += " AND (PEND_STATUS_CODIGO <> ? AND PEND_STATUS_CODIGO <> ? AND DATA_FIM IS NULL) ";
		ps.adicionarLong(PendenciaStatusDt.ID_CUMPRIDA);
		ps.adicionarLong(PendenciaStatusDt.ID_NAO_CUMPRIDA);
		//retirando status e-carta --------------------------------------------------------------------------------------------------------
		
		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM PROJUDI." + sqlSel;
		
		String sqlOrderby = prioridade == true ? " " : " ORDER BY descricao3 ";

		try{
			sqlSel = "SELECT ID_PEND as id, PROC_NUMERO_COMPLETO as descricao1, PEND_TIPO as descricao2, to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao3, PEND_STATUS as descricao4, ID_PROC as descricao5, ID_PEND_STATUS as descricao6 FROM PROJUDI." + sqlSel + sqlOrderby;

			rs1 = this.consultarPaginacao(sqlSel, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			pendencias = gerarJsonHash(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas, usuarioNe);
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	

	/**
	 * Consutlar pendencias de prazos decorridos
	 * 
	 * @param id_Serventia
	 *           identificador da serventia do usuario que executa a acao
	 * @param PendenciaTipoDt
	 *            tipo da pendencia
	 * @param numero_processo
	 *            numero do processo
	 * @param posicao
	 *            paginacao
	 * @return lista de pendencias com prazos
	 * @throws Exception
	 */
	public String consultarPrazosDecorridosDevolucaoAutosJSON(String id_Serventia, String numero_processo, String posicao) throws Exception {

		String pendencias = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdeColunas = 6;
		
		String sql = "";
		
		sql += " PROJUDI.VIEW_PEND_PRAZO_DECOR_SOL_CAR ";

		sql += " ppd WHERE " + " ppd.ID_SERV = ? ";
		ps.adicionarLong(id_Serventia);
		
		String filtroGenerico = this.filtroGenerico("ppd", true, null, null, null, null, numero_processo, null, null, null, null, ps);

		sql += filtroGenerico;
		
		sql += " ORDER BY DATA_LIMITE ";

		String sqlSelQtd = "SELECT COUNT(*) as QUANTIDADE FROM " + sql;

		//sql = "SELECT * FROM " + sql;
		sql = "SELECT ID_PEND as id,  PROC_NUMERO_COMPLETO as descricao1,  PEND_TIPO as descricao2,  NOME_USU_CADASTRADOR || ' - '|| SERV_CADASTRADOR as descricao3,  to_char(DATA_INICIO,'DD/MM/YYYY hh24:mi:ss') as descricao4, to_char(DATA_LIMITE,'DD/MM/YYYY hh24:mi:ss') as descricao5,  ID_PROC as descricao6  FROM " + sql;	

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);

			rs2 = this.consultar(sqlSelQtd, ps);
			rs2.next();
			
			pendencias = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }

		return pendencias;
	}
	
}
