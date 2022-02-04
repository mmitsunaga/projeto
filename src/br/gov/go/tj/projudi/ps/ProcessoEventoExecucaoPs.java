package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.EventoLocalDt;
import br.gov.go.tj.projudi.dt.EventoRegimeDt;
import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoEventoExecucaoPs extends ProcessoEventoExecucaoPsGen{

	/**
     * 
     */
    private static final long serialVersionUID = -5624482109043568005L;

    public ProcessoEventoExecucaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Lista os eventos (com as propriedades de cada evento, regime e local de cumprimento de pena) a partir do id do processo e do id da movimentação
	 * (captura os eventos do processo pai e dos processos filho, para atender os casos de processo apenso)
	 * param id_processo, identificação do processo
	 * param id_movimentacao, identificação da movimentação
	 * return lista com os eventos
	 * throws Exception
	 */
	public List listarEventos(String id_processo, String id_movimentacao) throws Exception {
		StringBuffer sql = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		if ((id_processo==null || id_processo.isEmpty()) && (id_movimentacao==null || id_movimentacao.isEmpty())){
			throw new MensagemException("Ocorreu algum problema na sua requisição, clique na página inicial e reinicie a sua operação");
		}
		
		sql.append("SELECT pev.*, e.*, ees.*,et.*, er.*, re.*, l.*, el.*,pet.*, u.NOME, pe.DATA_INICIO_CUMP_PENA ");
		sql.append("FROM PROJUDI.VIEW_PROC_EVENTO_EXE pev ");
		sql.append("INNER JOIN PROJUDI.EVENTO_EXE e ON pev.ID_EVENTO_EXE = e.ID_EVENTO_EXE ");
		sql.append("INNER JOIN PROJUDI.evento_exe_status ees on e.id_evento_exe_status = ees.id_evento_exe_status ");
		sql.append("INNER JOIN PROJUDI.EVENTO_EXE_TIPO ET ON ET.id_evento_exe_tipo = e.id_evento_exe_tipo ");
		sql.append("INNER JOIN PROJUDI.PROC p on p.ID_PROC = pev.id_proc ");
		sql.append("LEFT JOIN PROJUDI.PROC_EXE pe ON pev.ID_PROC_EXE = pe.ID_PROC_EXE ");
		sql.append("LEFT JOIN PROJUDI.EVENTO_REGIME er ON pev.ID_PROC_EVENTO_EXE = er.ID_PROC_EVENTO_EXE "); 
		sql.append("left JOIN projudi.regime_exe re on re.id_regime_exe = er.id_regime_exe ");
		sql.append("left join projudi.pena_exe_tipo pet on re.id_pena_exe_tipo = pet.id_pena_exe_tipo ");
		sql.append("LEFT JOIN PROJUDI.EVENTO_LOCAL el ON pev.ID_PROC_EVENTO_EXE = el.ID_PROC_EVENTO_EXE ");
		sql.append("left join projudi.local_cump_pena l on el.id_local_cump_pena = l.id_local_cump_pena ");
		sql.append("LEFT JOIN PROJUDI.USU_SERV us ON pev.ID_USU_SERV = us.ID_USU_SERV ");
		sql.append("left join projudi.usu u on us.id_usu = u.id_usu ");
		sql.append("WHERE (pe.CODIGO_TEMP = ? OR pe.CODIGO_TEMP is NULL) ");
		ps.adicionarLong(1);
		//consulta o processo pai e os processos filho (para atender os casos de processo apenso)
		if (id_processo.length()>0){
			sql.append(" AND (p.ID_PROC = ? OR p.ID_PROC_DEPENDENTE = ?) ");
			ps.adicionarLong(id_processo);
			ps.adicionarLong(id_processo);
		}
		if (id_movimentacao.length()>0){
			sql.append(" AND pev.ID_MOVI = ? ");
			ps.adicionarLong(id_movimentacao);
		}
		sql.append(" ORDER BY DATA_INICIO, pev.ID_MOVI, pev.ID_PROC_EVENTO_EXE");
		try{
		
			rs1 = consultar(sql.toString(), ps);

			while (rs1.next()){
				ProcessoEventoExecucaoDt processoEventoExecucao = new ProcessoEventoExecucaoDt();
				associarDt(processoEventoExecucao, rs1);
				processoEventoExecucao.setUsuarioAlteracao(rs1.getString("NOME"));
				processoEventoExecucao.setDataInicioCumpirmentoPena(Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO_CUMP_PENA")));
				associarDtEventoExecucao(processoEventoExecucao.getEventoExecucaoDt(), rs1); 
				
				if (rs1.getString("ID_EVENTO_REGIME") != null){
					associarDtEventoRegime(processoEventoExecucao.getEventoRegimeDt(), rs1);
				}
				
				if (rs1.getString("ID_EVENTO_LOCAL") != null){
					associarDtEventoLocal(processoEventoExecucao.getEventoLocalDt(), rs1);
				}
				lista.add(processoEventoExecucao);
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	protected void associarDtEventoLocal( EventoLocalDt dados, ResultSetTJGO rs )  throws Exception {

		
		dados.setId(rs.getString("ID_EVENTO_LOCAL"));
		dados.setLocalCumprimentoPena(rs.getString("LOCAL_CUMP_PENA"));
		dados.setId_ProcessoEventoExecucao( rs.getString("ID_PROC_EVENTO_EXE"));
		dados.setEventoExecucao( rs.getString("EVENTO_EXE"));
		dados.setId_LocalCumprimentoPena( rs.getString("ID_LOCAL_CUMP_PENA"));
		dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));
		
	}
	
	protected void associarDtEventoRegime( EventoRegimeDt dados, ResultSetTJGO rs1 )  throws Exception {
		
		dados.setId(rs1.getString("ID_EVENTO_REGIME"));
		dados.setRegimeExecucao(rs1.getString("REGIME_EXE"));
		dados.setId_ProcessoEventoExecucao( rs1.getString("ID_PROC_EVENTO_EXE"));
		dados.setEventoExecucao( rs1.getString("EVENTO_EXE"));
		dados.setId_RegimeExecucao( rs1.getString("ID_REGIME_EXE"));
		dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		dados.setId_ProximoRegimeExecucao(rs1.getString("ID_PROXIMO_REGIME_EXE"));
		dados.setId_PenaExecucaoTipo( rs1.getString("ID_PENA_EXE_TIPO"));
		dados.setPenaExecucaoTipo( rs1.getString("PENA_EXE_TIPO"));
		
		//dados regimeExecucao
		dados.getRegimeExecucaoDt().setRegimeExecucao(rs1.getString("REGIME_EXE"));
		dados.getRegimeExecucaoDt().setId(rs1.getString("ID_REGIME_EXE"));
		dados.getRegimeExecucaoDt().setId_ProximoRegimeExecucao(rs1.getString("ID_PROXIMO_REGIME_EXE"));
		dados.getRegimeExecucaoDt().setCodigoTemp(rs1.getString("CODIGO_TEMP"));
		dados.getRegimeExecucaoDt().setId_PenaExecucaoTipo(rs1.getString("ID_PENA_EXE_TIPO"));
		dados.getRegimeExecucaoDt().setPenaExecucaoTipo(rs1.getString("PENA_EXE_TIPO"));
		
	}
	
	protected void associarDtEventoExecucao(EventoExecucaoDt dados, ResultSetTJGO rs )  throws Exception {
		
		dados.setId(rs.getString("ID_EVENTO_EXE"));
		dados.setEventoExecucao(rs.getString("EVENTO_EXE"));
		dados.setAlteraLocal( rs.getString("ALTERA_LOCAL"));
		dados.setAlteraRegime( rs.getString("ALTERA_REGIME"));
		dados.setValorNegativo( Funcoes.FormatarLogico(rs.getString("VALOR_NEGATIVO")));
		dados.setEventoExecucaoCodigo( rs.getString("EVENTO_EXE_CODIGO"));
		dados.setId_EventoExecucaoTipo( rs.getString("ID_EVENTO_EXE_TIPO"));
		dados.setEventoExecucaoTipo( rs.getString("EVENTO_EXE_TIPO"));
		dados.setId_EventoExecucaoStatus( rs.getString("ID_EVENTO_EXE_STATUS"));
		dados.setEventoExecucaoStatus( rs.getString("EVENTO_EXE_STATUS"));
		dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));

	}
	
	/**
	 * verifica se o processo de execução penal é de medida de segurança
	 * param id_processo: identificação do processo
	 * return boolean: é ou não medida de segurança
	 * throws Exception
	 */
//	public boolean isProcessoOrigemMedidaSeguranca(String id_processo){
//		StringBuffer sql = new StringBuffer();
//		ResultSetTJGO rs = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//
//		//agrupa por Id_penaExecucaoTipo os registros da tabela EventoRegime, e informa a quantidade de cada tipo
//		sql.append("SELECT COUNT(*) as QTDE_PENA_EXE_TIPO, er.ID_PENA_EXE_TIPO");
//		sql.append(" FROM PROJUDI.VIEW_PROC_EVENTO_EXE pev ");
//		sql.append(" INNER JOIN PROJUDI.VIEW_PROC_EXE pe ON pev.ID_PROC_EXE = pe.ID_PROC_EXE"); 
//		sql.append(" INNER JOIN PROJUDI.VIEW_EVENTO_REGIME er ON pev.ID_PROC_EVENTO_EXE = er.ID_PROC_EVENTO_EXE "); 
//		sql.append(" WHERE pe.CODIGO_TEMP = ? OR pe.CODIGO_TEMP is NULL");
//		ps.adicionarLong(1);
//		sql.append(" AND pev.ID_EVENTO_EXE IN (?,?)");
//		ps.adicionarLong(EventoExecucaoDt.TRANSITO_JULGADO);
//		ps.adicionarLong(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA);
//		//consulta o processo pai e os processos filho (para atender os casos de processo apenso)
//		if (id_processo.length()>0) sql.append(" AND (pe.ID_PROC_EXE_PENAL = ? OR pe.ID_PROC_DEPENDENTE = ?)");
//		ps.adicionarLong(id_processo);
//		ps.adicionarLong(id_processo);
//		sql.append(" GROUP BY er.ID_PENA_EXE_TIPO");
//		
//		try{
//			rs = consultar(sql.toString(), ps);
////			rs.last();
//			
//			//para ser um processo de medida de segurança, deve haver apenas o tipo de pena "medida de segurança"
//			if (rs.getRow() == 1 && rs.getString("ID_PENA_EXE_TIPO").equals(String.valueOf(PenaExecucaoTipoDt.PENA_MEDIDA_SEGURANCA)))
//				return true;
//			else return false;
//
//		} finally {
//            try{if (rs != null) rs.close();} catch(Exception e1) {}
//        } 
//	}
	
	/**
	 * Consultar o último evento do processo informado e dos tipos de evento informados
	 * param id_processo, identificação do processo
	 * param listaTipoEvento, lista com os tipos do eventos
	 * return o último evento dos tipos informados
	 * throws Exception
	 */
	public List consultarUltimoEvento(String id_processo, List listaTipoEvento) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlTipoEvento = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
			
		sql.append("SELECT pev.ID_EVENTO_EXE, pev.ID_PROC_EVENTO_EXE, pev.DATA_INICIO FROM PROJUDI.PROC_EVENTO_EXE pev");
		sql.append(" INNER JOIN PROJUDI.PROC p ON p.ID_PROC = pev.ID_PROC_EXE_PENAL");
		//consulta o processo pai e os processos filho (para atender os casos de processo apenso)
		sql.append(" WHERE (p.ID_PROC = ? OR p.ID_PROC_DEPENDENTE = ?)");
		ps.adicionarLong(id_processo);
		ps.adicionarLong(id_processo);
		if (listaTipoEvento.size()>0){
			for (int i=0;i<listaTipoEvento.size();i++){
				if (sqlTipoEvento.length()>0) sqlTipoEvento.append(",");
				sqlTipoEvento.append("?");
				ps.adicionarLong((listaTipoEvento.get(i).toString()));				
			}
			sql.append(" AND pev.ID_EVENTO_EXE in  (" + sqlTipoEvento.toString() + ")");
		}
		sql.append(" ORDER BY DATA_INICIO DESC");
		
		try{
			rs1 = consultar(sql.toString(), ps);
			if (rs1.next()){			
				lista.add(0, rs1.getString("ID_EVENTO_EXE"));
				lista.add(1, rs1.getString("ID_PROC_EVENTO_EXE"));
				lista.add(2, Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO")));				
			}
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	/**
     * recupera o saldo de dias ou de horas de estudo
     * param id_processo, data início do evento
     * param lista com os tipos dos eventos
     * return o saldo
     * author kbsriccioppo
     */
	public int calcularSaldo(String id_processo, String dataInicio, List listaTipoEvento) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlTipoEvento = new StringBuffer();
		ResultSetTJGO rs1 = null;
		int qtdeDiasTrabalhados = 0;
		int qtdePerdaDiasTrabalhados = 0;
		int saldoDiasTrabalhados = 0;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT SUM(pev.QUANTIDADE) as QUANTIDADE, e.VALOR_NEGATIVO FROM PROJUDI.PROC_EVENTO_EXE pev");
		sql.append(" INNER JOIN PROJUDI.EVENTO_EXE e ON pev.ID_EVENTO_EXE = e.ID_EVENTO_EXE");
		sql.append(" INNER JOIN PROJUDI.PROC p ON p.ID_PROC = pev.ID_PROC_EXE_PENAL");
		sql.append(" WHERE (p.ID_PROC = ? OR p.ID_PROC_DEPENDENTE = ?)");
		ps.adicionarLong(id_processo);
		ps.adicionarLong(id_processo);
		sql.append(" AND pev.DATA_INICIO <= ? ");
		ps.adicionarDate(dataInicio);
		if (listaTipoEvento.size()>0){
			for (int i=0;i<listaTipoEvento.size();i++){
				if (sqlTipoEvento.length()>0) sqlTipoEvento.append(",");
				sqlTipoEvento.append("?");
				String tipoEvento = listaTipoEvento.get(i).toString();
				ps.adicionarLong(tipoEvento);				
			}
			sql.append(" AND e.ID_EVENTO_EXE in  (" + sqlTipoEvento.toString() + ")");
		}
		sql.append(" GROUP BY e.ID_EVENTO_EXE, e.VALOR_NEGATIVO");
		
		try{
			rs1 = consultar(sql.toString(), ps);
			while (rs1.next()){
				if (rs1.getString("VALOR_NEGATIVO").equals("0")) qtdeDiasTrabalhados += rs1.getInt("QUANTIDADE");
				if (rs1.getString("VALOR_NEGATIVO").equals("1")) qtdePerdaDiasTrabalhados += rs1.getInt("QUANTIDADE");
			}
			saldoDiasTrabalhados = qtdeDiasTrabalhados - qtdePerdaDiasTrabalhados;
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        } 
		return saldoDiasTrabalhados;
	}

	/**
	 * sobrescreve o método do gerador, para validar o preenchimento da data fim e observação
	 */
	protected void associarDt( ProcessoEventoExecucaoDt dados, ResultSetTJGO rs1 )  throws Exception {
		
		dados.setId(rs1.getString("ID_PROC_EVENTO_EXE"));
		dados.setEventoExecucao(rs1.getString("EVENTO_EXE"));
		dados.setDataInicio( Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO")));
		if (rs1.getString("DATA_PRISAO_REVOG_LC") == null) dados.setDataPrisaoRevogacaoLC("");
		else dados.setDataPrisaoRevogacaoLC( Funcoes.FormatarData(rs1.getDateTime("DATA_PRISAO_REVOG_LC")));
		dados.setQuantidade( rs1.getString("QUANTIDADE"));
		if (rs1.getString("OBSERVACAO") == null) dados.setObservacao("");
		else dados.setObservacao( rs1.getString("OBSERVACAO"));
		if (rs1.getString("OBSERVACAO_AUX") == null) dados.setObservacaoAux("");
		else dados.setObservacaoAux( rs1.getString("OBSERVACAO_AUX"));
		if (rs1.getString("DATA_DECRETO_COMUTACAO") == null) dados.setDataDecretoComutacao("");
		else dados.setDataDecretoComutacao( Funcoes.FormatarData(rs1.getDateTime("DATA_DECRETO_COMUTACAO")));
		dados.setId_LivramentoCondicional( rs1.getString("ID_LIV_CONDICIONAL"));
		dados.setId_Movimentacao( rs1.getString("ID_MOVI"));
		dados.setMovimentacaoTipo( rs1.getString("MOVI_TIPO"));
		dados.setMovimentacaoDataRealizacaoTipo( Funcoes.FormatarData(rs1.getDateTime("DATA_REALIZACAO")) + " - " + rs1.getString("MOVI_TIPO"));
		dados.setId_EventoExecucao( rs1.getString("ID_EVENTO_EXE"));
		dados.setId_ProcessoExecucao( rs1.getString("ID_PROC_EXE"));
		dados.setId_LivramentoCondicional( rs1.getString("ID_LIV_CONDICIONAL"));
		dados.setConsiderarTempoLivramentoCondicional( rs1.getString("CONS_TEMPO_LIV_CONDICIONAL"));
		dados.setCodigoTemp( rs1.getString("CODIGO_TEMP"));
		dados.setId_UsuarioServentia( rs1.getString("ID_USU_SERV"));
		dados.setDataAlteracao( Funcoes.FormatarData(rs1.getDateTime("DATA_ALTERACAO")));
		if(rs1.getString("DATA_REALIZACAO") != null) dados.setDataRealizacao( rs1.getString("DATA_REALIZACAO"));
		dados.setIdProcessoExecucaoPenal( rs1.getString("ID_PROC_EXE_PENAL"));
		dados.setIdEventoPai(rs1.getString("ID_PROC_EVENTO_EXE_PAI"));
		if (rs1.getString("DATA_INICIO_SURSIS") == null) dados.setDataInicioSursis("");
		else dados.setDataInicioSursis(Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO_SURSIS")));
		
	}
	
	/**
	 * consulta o cálculo de liquidação de um processo de execução a partir do id do processo informado
	 * param id_processo
	 * return dataProvavelDt
	 * throws Exception
	 */
	public CalculoLiquidacaoDt consultarCalculoLiquidacao(CalculoLiquidacaoDt calculoLiquidacaoDt) throws Exception {
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT * FROM PROJUDI.VIEW_CALCULO_LIQUIDACAO cl");
		sql.append(" WHERE cl.ID_PROC = ? ");
		ps.adicionarLong(calculoLiquidacaoDt.getIdProcesso());
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				calculoLiquidacaoDt.setId(rs.getString("ID_CALCULO_LIQUIDACAO"));
				calculoLiquidacaoDt.setRelatorioByte(rs.getBytes("RELATORIO"));
				calculoLiquidacaoDt.setInformacaoAdicional(rs.getString("INFORMACAO_ADICIONAL"));
				calculoLiquidacaoDt.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_CALCULO")));
				calculoLiquidacaoDt.setDataTerminoPena(Funcoes.FormatarData(rs.getDateTime("DATA_TERMINO_PENA")));
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return calculoLiquidacaoDt;
	}
	
	/**
	 * consulta o cálculo de liquidação de um processo de execução a partir do id do processo informado
	 * param id_processo
	 * return DataProvavelDt
	 * throws Exception
	 */
	public DataProvavelDt consultarCalculoLiquidacao(String idProcesso) throws Exception {
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		DataProvavelDt objRetorno = new DataProvavelDt();
		
		sql.append("SELECT * FROM PROJUDI.VIEW_CALCULO_LIQUIDACAO cl");
		sql.append(" WHERE cl.ID_PROC = ? ");
		ps.adicionarLong(idProcesso);
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				if (rs.getString("INFORMACAO_ADICIONAL") == null) objRetorno.setInformacaoSentenciado("");
				else objRetorno.setInformacaoSentenciado(rs.getString("INFORMACAO_ADICIONAL"));
				objRetorno.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_CALCULO")));
				objRetorno.setDataProvavelLivramento(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_LIVRAMENTO")));
				objRetorno.setDataProvavelProgressao(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_PROGRESSAO")));
				objRetorno.setDataProvavelTerminoPena(Funcoes.FormatarData(rs.getDateTime("DATA_TERMINO_PENA")));
				objRetorno.setDataValidadeMandadoPrisao(Funcoes.FormatarData(rs.getDateTime("DATA_VALIDADE_MAND_PRISAO")));
				
				if (rs.getString("HORA_RESTANTE_PSC") == null) objRetorno.setHoraRestantePSC("");
				else objRetorno.setHoraRestantePSC(rs.getString("HORA_RESTANTE_PSC"));
				objRetorno.setDataTerminoLFS(Funcoes.FormatarData(rs.getDateTime("DATA_TERMINO_LFS")));
				objRetorno.setDataTerminoITD(Funcoes.FormatarData(rs.getDateTime("DATA_TERMINO_ITD")));
				if (rs.getString("VALOR_DEVIDO_PEC") == null) objRetorno.setValorDevidoPEC("");
				else objRetorno.setValorDevidoPEC(rs.getString("VALOR_DEVIDO_PEC"));
				if (rs.getString("QTD_DEVIDA_PCB") == null) objRetorno.setQtdDevidaPCB("");
				else objRetorno.setQtdDevidaPCB(rs.getString("QTD_DEVIDA_PCB"));
				if (rs.getString("TERMINO_SURSIS") == null) objRetorno.setTerminoSURSIS("");
				else objRetorno.setTerminoSURSIS(rs.getString("TERMINO_SURSIS"));
				
				objRetorno.setRelatorioByte(rs.getBytes("RELATORIO"));
				objRetorno.setDataHomologacao(Funcoes.FormatarData(rs.getDateTime("DATA_HOMOLOGACAO")));
				objRetorno.setId(rs.getString("ID_CALCULO_LIQUIDACAO"));
				objRetorno.setIdCalculo(rs.getString("ID_CALCULO_LIQUIDACAO"));
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return objRetorno;
	}
	
	/**
	 * consulta a data do último cálculo de liquidação de pena de um processo de execução penal informado
	 * param id_processo
	 * return String: data último cálculo
	 * throws Exception
	 */
	public String consultarDataUltimoCalculo(String idProcesso) throws Exception {
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String stRetorno = "";
		
		sql.append("SELECT max(DATA_CALCULO) as data_calculo FROM PROJUDI.VIEW_CALCULO_LIQUIDACAO cl");
		sql.append(" WHERE cl.ID_PROC = ? ");
		ps.adicionarLong(idProcesso);
		
		try{
			rs = consultar(sql.toString(), ps);
			if (rs.next()){
				stRetorno = Funcoes.FormatarData(rs.getDateTime("DATA_CALCULO"));
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return stRetorno;
	}
	
	/**
	 * sobrescreveou o método do gerador para alterar somente os dados preenchidos
	 */
	public void alterar(ProcessoEventoExecucaoDt dados) throws Exception{
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.PROC_EVENTO_EXE SET  ";
		Sql+= "QUANTIDADE  = ? ";
		ps.adicionarDecimal(dados.getQuantidade().replace(".", ","));
		
		if (dados.getDataInicio().length() > 0){
			Sql+= ",DATA_INICIO = ? ";
			ps.adicionarDate(dados.getDataInicio());
		}
		if (dados.getDataPrisaoRevogacaoLC().length() > 0){
			Sql+= ",DATA_PRISAO_REVOG_LC = ? ";
			ps.adicionarDate(dados.getDataPrisaoRevogacaoLC());
		}		 
		Sql+= ",OBSERVACAO  = ? ";
		ps.adicionarString(dados.getObservacao()); //campo opcional
		
		Sql+= ",OBSERVACAO_AUX  = ? ";
		ps.adicionarString(dados.getObservacaoAux()); //campo opcional
		
		Sql+= ",DATA_DECRETO_COMUTACAO  = ? ";
		ps.adicionarDate(dados.getDataDecretoComutacao()); //campo opcional
		
		Sql+= ",DATA_ALTERACAO  = ? ";
		ps.adicionarDate(new Date()); //campo opcional

		Sql+= ",ID_LIV_CONDICIONAL  = ? ";
		ps.adicionarLong(dados.getId_LivramentoCondicional()); 
		
		if (dados.getId_Movimentacao().length() > 0){
			Sql+= ",ID_MOVI  = ? ";
			ps.adicionarLong(dados.getId_Movimentacao());
		}
		if (dados.getId_EventoExecucao().length() > 0){
			Sql+= ",ID_EVENTO_EXE  = ? ";
			ps.adicionarLong(dados.getId_EventoExecucao());
		}
		
		if (dados.getConsiderarTempoLivramentoCondicional().length() > 0){
			Sql+= ",CONS_TEMPO_LIV_CONDICIONAL  = ? ";
			ps.adicionarLong(dados.getConsiderarTempoLivramentoCondicional());
		}
		if (dados.getId_UsuarioServentia().length() > 0){
			Sql+= ",ID_USU_SERV = ? ";
			ps.adicionarLong(dados.getId_UsuarioServentia());
		}
		if (dados.getIdProcessoExecucaoPenal().length() > 0){
			Sql+= ",ID_PROC_EXE_PENAL = ? ";
			ps.adicionarLong(dados.getIdProcessoExecucaoPenal());
		}
		Sql+= ",DATA_INICIO_SURSIS = ? ";
		ps.adicionarDate(dados.getDataInicioSursis());
				
		Sql+= ",ID_PROC_EVENTO_EXE_PAI = ? ";
		ps.adicionarLong(dados.getIdEventoPai());
		
		Sql = Sql + " WHERE ID_PROC_EVENTO_EXE  = ? ";
		ps.adicionarLong(dados.getId());
		
		executarUpdateDelete(Sql,ps); 
	} 
	
	/**
	 * sobrescreveu o método do gerador
	 */
	public void inserir(ProcessoEventoExecucaoDt dados ) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlCampos = "INSERT INTO PROJUDI.PROC_EVENTO_EXE (";
		SqlValores = " Values (";
		if ((dados.getDataInicio().length()>0)){
			SqlCampos+= "DATA_INICIO " ;
			SqlValores+="?";
			ps.adicionarDate(dados.getDataInicio());
		}
		if ((dados.getDataPrisaoRevogacaoLC().length()>0)) {
			SqlCampos+= ",DATA_PRISAO_REVOG_LC " ;
			SqlValores+=",?";
			ps.adicionarDate(dados.getDataPrisaoRevogacaoLC());
		}
		if ((dados.getQuantidade().length()>0)) {
			SqlCampos+= ",QUANTIDADE " ;
			SqlValores+=",?";
//			ps.adicionarLong(dados.getQuantidade());
			ps.adicionarDecimal(dados.getQuantidade().replace(".", ","));
		}
		if ((dados.getObservacao().length()>0)) {
			SqlCampos+= ",OBSERVACAO " ;
			SqlValores+=",?";
			ps.adicionarString(dados.getObservacao());
		}
		if ((dados.getObservacaoAux().length()>0)) {
			SqlCampos+= ",OBSERVACAO_AUX " ;
			SqlValores+=",?";
			ps.adicionarString(dados.getObservacaoAux());
		}
		if ((dados.getDataDecretoComutacao().length()>0)) {
			SqlCampos+= ",DATA_DECRETO_COMUTACAO " ;
			SqlValores+=",?";
			ps.adicionarDate(dados.getDataDecretoComutacao());
		}
		if ((dados.getId_LivramentoCondicional().length()>0)) {
			SqlCampos+= ",ID_LIV_CONDICIONAL " ;
			SqlValores+=",?";
			ps.adicionarLong(dados.getId_LivramentoCondicional());
		}
		if ((dados.getId_Movimentacao().length()>0)) {
			SqlCampos+= ",ID_MOVI " ;
			SqlValores+=",?";
			ps.adicionarLong(dados.getId_Movimentacao());
		}
		if ((dados.getId_EventoExecucao().length()>0)){
			SqlCampos+= ",ID_EVENTO_EXE " ;
			SqlValores+=",?";
			ps.adicionarLong(dados.getId_EventoExecucao());
		}
		if ((dados.getId_ProcessoExecucao().length()>0)){
			SqlCampos+= ",ID_PROC_EXE " ;
			SqlValores+=",?";
			ps.adicionarLong(dados.getId_ProcessoExecucao());
		}
		if (!(dados.getConsiderarTempoLivramentoCondicional().length()==0)){
			SqlCampos+= ",CONS_TEMPO_LIV_CONDICIONAL ";
			SqlValores+=",?";
			ps.adicionarLong(dados.getConsiderarTempoLivramentoCondicional());
		}		
		if ((dados.getId_UsuarioServentia().length()>0)){
			SqlCampos+= ",ID_USU_SERV " ;
			SqlValores+=",?";
			ps.adicionarLong(dados.getId_UsuarioServentia());
		}
		if ((dados.getIdProcessoExecucaoPenal().length()>0)){
			SqlCampos+= ",ID_PROC_EXE_PENAL " ;
			SqlValores+=",?";
			ps.adicionarLong(dados.getIdProcessoExecucaoPenal());
		}
		
		if ((dados.getIdEventoPai().length()>0)){
			SqlCampos+= ",ID_PROC_EVENTO_EXE_PAI " ;
			SqlValores+=",?";
			ps.adicionarLong(dados.getIdEventoPai());
		}
		
		SqlCampos+= ", DATA_INICIO_SURSIS " ;
		SqlValores+=", ?";
		ps.adicionarDate(dados.getDataInicioSursis());
		
		SqlCampos+= ", DATA_ALTERACAO " ;
		SqlValores+=", ?";
		ps.adicionarDate(new Date());
		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
 		
 		dados.setId(executarInsert(Sql, "ID_PROC_EVENTO_EXE", ps)); 
	}
	
	/**
	 * Consulta o Id_ProcessoEventoExecucao, tendo como filtro o Id_ProcessoExecucao e Id_EventoExecucao
	 * param Id_ProcessoExecucao: idProcessoExecucao relacionado ao id_ProcessoEventoExecucao que será consultado
	 * param Id_EventoExecucao: idEventoExecucao relacionado ao id_ProcessoEventoExecucao que será consultado
	 * return Id_ProcessoEventoExecucao
	 * throws Exception
	 * author wcsilva
	 */
	public String consultarId_ProcessoEventoExecucao(String idProcessoExecucao, String idEventoExecucao) throws Exception {
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		String idProcessoEventoExecucao = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		if (idEventoExecucao == null || idEventoExecucao.length() == 0) return idProcessoEventoExecucao;		
		String[] vetoridEventoExecucao =  idEventoExecucao.split(",");
		if (vetoridEventoExecucao.length == 0) return idProcessoEventoExecucao;
				
		sql.append("SELECT ID_PROC_EVENTO_EXE FROM PROJUDI.PROC_EVENTO_EXE");
		sql.append(" WHERE ID_PROC_EXE = ? ");
		ps.adicionarLong(idProcessoExecucao);
		sql.append(" AND ID_EVENTO_EXE IN (");
		boolean ehPrimeiroItem = true;
		for (String id : vetoridEventoExecucao)
		{
			if (!ehPrimeiroItem) sql.append(",");
			sql.append("?");
			ps.adicionarLong(id);
			ehPrimeiroItem = false;
		}	
		sql.append(")");
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				idProcessoEventoExecucao = rs.getString("ID_PROC_EVENTO_EXE");
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return idProcessoEventoExecucao;
	}
	
	/**
	 * lista os Id_ProcessoEventoExecução dos TJ e GRP do processo e processo dependente
	 * param id_Processo: identificação do processo
	 * return lista<ProcessoEventoExecucaoDt> com id's do processoEventoExecução referente à TJ ou GRP
	 * throws Exception
	 */
	public List listarIdProcessoEventoExecucao_TJ_GRP(String id_Processo) throws Exception{
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List lista = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT pee.ID_PROC_EVENTO_EXE, pee.ID_EVENTO_EXE, pee.DATA_INICIO ");
		sql.append(" FROM PROJUDI.PROC_EVENTO_EXE pee");
		sql.append(" INNER JOIN PROJUDI.VIEW_PROC_EXE pe ON pee.ID_PROC_EXE = pe.ID_PROC_EXE");
		sql.append(" WHERE pee.ID_EVENTO_EXE IN (?,?)");
		ps.adicionarLong(EventoExecucaoDt.TRANSITO_JULGADO);
		ps.adicionarLong(EventoExecucaoDt.GUIA_RECOLHIMENTO_PROVISORIA);
		sql.append(" AND (pe.ID_PROC_EXE_PENAL = ? OR pe.ID_PROC_DEPENDENTE = ?)");
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(id_Processo);
		sql.append(" AND (pe.CODIGO_TEMP =  1 OR pe.CODIGO_TEMP is NULL)");
		sql.append(" ORDER BY pee.DATA_INICIO");
		        
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				ProcessoEventoExecucaoDt pee = new ProcessoEventoExecucaoDt();
				pee.setId(rs.getString("ID_PROC_EVENTO_EXE"));
				pee.getEventoExecucaoDt().setId(rs.getString("ID_EVENTO_EXE"));
				pee.setDataInicio(rs.getString("DATA_INICIO"));
				lista.add(pee);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
    /**
     * Lista as modalidades do processo execução (ação penal)
     * param idProcessoExecucao
     * return lista de modalidades
     * throws Exception
     */
	public List listarModalidadesDaAcaoPenal(String idProcessoExecucao) throws Exception{
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List lista = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT * FROM PROJUDI.VIEW_EVENTO_REGIME er");
		sql.append(" INNER JOIN PROJUDI.VIEW_PROC_EVENTO_EXE pee ON er.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE");
		sql.append(" WHERE er.ID_PENA_EXE_TIPO = ? ");
		ps.adicionarLong(PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO);
		sql.append(" AND pee.ID_PROC_EXE = ? ");
		ps.adicionarLong(idProcessoExecucao);
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				ProcessoEventoExecucaoDt pee = new ProcessoEventoExecucaoDt();
				associarDt(pee, rs);
				pee.getEventoExecucaoDt().setId(rs.getString("ID_EVENTO_EXE"));
				pee.getEventoExecucaoDt().setEventoExecucao(rs.getString("EVENTO_EXE"));
				pee.getEventoRegimeDt().setId(rs.getString("ID_EVENTO_REGIME"));
				pee.getEventoRegimeDt().setId_RegimeExecucao(rs.getString("ID_REGIME_EXE"));
				pee.getEventoRegimeDt().setRegimeExecucao(rs.getString("REGIME_EXE"));
				lista.add(pee);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	public List listarModalidadesDoEvento(String idProcessoEventoExecucao) throws Exception{
		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		List lista = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT * FROM PROJUDI.VIEW_EVENTO_REGIME er");
		sql.append(" INNER JOIN PROJUDI.VIEW_PROC_EVENTO_EXE pee ON er.ID_PROC_EVENTO_EXE = pee.ID_PROC_EVENTO_EXE");
		sql.append(" WHERE er.ID_PENA_EXE_TIPO = ? ");
		ps.adicionarLong(PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO);
		sql.append(" AND pee.ID_PROC_EVENTO_EXE_PAI = ? ");
		ps.adicionarLong(idProcessoEventoExecucao);
		
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				ProcessoEventoExecucaoDt pee = new ProcessoEventoExecucaoDt();
				associarDt(pee, rs);
				pee.getEventoExecucaoDt().setId(rs.getString("ID_EVENTO_EXE"));
				pee.getEventoExecucaoDt().setEventoExecucao(rs.getString("EVENTO_EXE"));
				pee.getEventoRegimeDt().setId(rs.getString("ID_EVENTO_REGIME"));
				pee.getEventoRegimeDt().setId_RegimeExecucao(rs.getString("ID_REGIME_EXE"));
				pee.getEventoRegimeDt().setRegimeExecucao(rs.getString("REGIME_EXE"));
				lista.add(pee);
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	
	/**
	 * Consulta o Id_Movimentação do evento
	 * param idEventoExecucao: id do evento que contém a movimentação
	 * param idProcessoExecucao: id do processoExecucao que do evento que contém a movimentação 
	 * return String idMovimentacao
	 * throws Exception
	 */
    public String consultarIdMovimentacao(String idEventoExecucao, String idProcessoExecucao) throws Exception {
    	StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		String idMovimentacao = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		if (idEventoExecucao == null || idEventoExecucao.length() == 0) return idMovimentacao;		
		String[] vetoridEventoExecucao =  idEventoExecucao.split(",");
		if (vetoridEventoExecucao.length == 0) return idMovimentacao;	
		
		sql.append("SELECT ID_MOVI FROM PROJUDI.VIEW_PROC_EVENTO_EXE");
		sql.append(" WHERE ID_PROC_EXE = ? ");
		ps.adicionarLong(idProcessoExecucao);
		sql.append(" AND ID_EVENTO_EXE IN (");
		boolean ehPrimeiroItem = true;
		for (String id : vetoridEventoExecucao)
		{
			if (!ehPrimeiroItem) sql.append(",");
			sql.append("?");
			ps.adicionarLong(id);
			ehPrimeiroItem = false;
		}	
		sql.append(")");
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				idMovimentacao = rs.getString("ID_MOVI");
			}
		
        } finally{
             rs.close();
        } 
		return idMovimentacao;
    }
    
	/**
	 * consulta o ProcessoEventoExecucao, tendo como filtro o Id_ProcessoExecucao e Id_EventoExecucao
	 * param Id_ProcessoExecucao: idProcessoExecucao relacionado ao id_ProcessoEventoExecucao que será consultado
	 * param Id_EventoExecucao: idEventoExecucao relacionado ao id_ProcessoEventoExecucao que será consultado
	 * return ProcessoEventoExecucao
	 * throws Exception
	 * author wcsilva
	 */
    public ProcessoEventoExecucaoDt consultarProcessoEventoExecucao(String idEventoExecucao, String idProcessoExecucao) throws Exception {
    	StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		ProcessoEventoExecucaoDt objeto = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		if (idEventoExecucao == null || idEventoExecucao.length() == 0) return objeto;		
		String[] vetoridEventoExecucao =  idEventoExecucao.split(",");
		if (vetoridEventoExecucao.length == 0) return objeto;	
		
		sql.append("SELECT * FROM PROJUDI.VIEW_PROC_EVENTO_EXE");
		sql.append(" WHERE ID_PROC_EXE = ? ");
		ps.adicionarLong(idProcessoExecucao);
		sql.append(" AND ID_EVENTO_EXE IN (");
		boolean ehPrimeiroItem = true;
		for (String id : vetoridEventoExecucao)
		{
			if (!ehPrimeiroItem) sql.append(",");
			sql.append("?");
			ps.adicionarLong(id);
			ehPrimeiroItem = false;
		}	
		sql.append(")");
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				objeto = new ProcessoEventoExecucaoDt();
				associarDt(objeto, rs); 
			}
		
        } finally{
             rs.close();
        } 
		return objeto;
    }
    
    /**
     * Consulta a data início do evento (ProcessoEventoExecucao)
     * param idProcessoEventoExecucao
     * return String dataInicio
     * throws Exception
     */
    public String consultarDataInicioEvento(String idProcessoEventoExecucao, String idProcesso, String dataEventoReferencia) throws Exception{
    	StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		String dataInicio = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT DATA_INICIO FROM PROJUDI.VIEW_PROC_EVENTO_EXE");
		sql.append(" WHERE ID_PROC_EVENTO_EXE = ? AND ID_PROC = ? and DATA_INICIO <= ?");
		sql.append(" ORDER BY DATA_INICIO DESC");
		ps.adicionarLong(idProcessoEventoExecucao);
		ps.adicionarLong(idProcesso);
		ps.adicionarDate(dataEventoReferencia);
		try{
			rs = consultar(sql.toString(), ps);
			if (rs.next()){
				dataInicio = Funcoes.FormatarData(rs.getDateTime("DATA_INICIO"));
			}
		
        } finally{
             rs.close();
        } 
		return dataInicio;
    }
    
    /**
     * Consulta a o evento de Livramento Condicinal
     * param idProcesso
     * return String dataEventoReferencia
     * throws Exception
     */
    public ProcessoEventoExecucaoDt consultarLivramentoCondicional(String idProcesso, String dataEventoReferencia) throws Exception{
    	StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		ProcessoEventoExecucaoDt evento = new ProcessoEventoExecucaoDt();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT DATA_INICIO, ID_PROC_EVENTO_EXE FROM PROJUDI.VIEW_PROC_EVENTO_EXE");
		sql.append(" WHERE ID_PROC = ? and ID_EVENTO_EXE = ? and DATA_INICIO <= ?");
		sql.append(" ORDER BY DATA_INICIO DESC");
		ps.adicionarLong(idProcesso);
		ps.adicionarLong(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL);
		ps.adicionarDate(dataEventoReferencia);
		
		try{
			rs = consultar(sql.toString(), ps);
			if (rs.next()){
				evento.setId(rs.getString("ID_PROC_EVENTO_EXE"));
				evento.setDataInicio(Funcoes.FormatarData(rs.getDateTime("DATA_INICIO")));
			}
		
        } finally{
             rs.close();
        } 
		return evento;
    }
    
	
	 /**
	 * Inclui os dados do Cálculo de Liquidação de Penas.
	 */
	public void inserirCalculoLiquidacao(DataProvavelDt dados, boolean salvarTodosDados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		SqlCampos = "INSERT INTO PROJUDI.CALCULO_LIQUIDACAO ("; 
		SqlValores = " Values (";
		
		SqlCampos+= "ID_PROC " ;
		SqlValores += ",?";
		ps.adicionarLong(dados.getIdProcesso());
		
		SqlCampos+= ",INFORMACAO_ADICIONAL " ;
		SqlValores += ",?";
		ps.adicionarString(dados.getInformacaoSentenciado());
	
		SqlCampos+= ",ID_USU_SERV " ;
		SqlValores += ",?";
		ps.adicionarLong(dados.getIdUsuarioServentia());
	
		SqlCampos+= ",HORA_RESTANTE_PSC " ;
		SqlValores += ",?";
		ps.adicionarString(dados.getHoraRestantePSC());
		
		SqlCampos+= ",DATA_TERMINO_LFS " ;
		SqlValores += ",?";
		ps.adicionarDate(dados.getDataTerminoLFS());
		
		SqlCampos+= ",DATA_TERMINO_ITD " ;
		SqlValores += ",?";
		ps.adicionarDate(dados.getDataTerminoITD());
		
		SqlCampos+= ",VALOR_DEVIDO_PEC " ;
		SqlValores += ",?";
		ps.adicionarString(dados.getValorDevidoPEC());
		
		SqlCampos+= ",QTD_DEVIDA_PCB " ;
		SqlValores += ",?";
		ps.adicionarString(dados.getQtdDevidaPCB());
		
		SqlCampos+= ",TERMINO_SURSIS " ;
		SqlValores += ",?";
		ps.adicionarString(dados.getTerminoSURSIS());
		
		if (dados.getRelatorioByte() != null){
			SqlCampos+= ",RELATORIO " ;
			SqlValores += ",?";
			ps.adicionarByte(dados.getRelatorioByte());
		}
		
		SqlCampos += ",DATA_CALCULO ";
		SqlValores += ",?";
		ps.adicionarDate(new Date());		
		
		if (salvarTodosDados){
			SqlCampos += ",DATA_REQ_TEMPORAL_PROGRESSAO ";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataProvavelProgressao());
			
			SqlCampos += ",DATA_REQ_TEMPORAL_LIVRAMENTO ";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataProvavelLivramento());
			
			SqlCampos+= ",DATA_VALIDADE_MAND_PRISAO " ;
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataValidadeMandadoPrisao());
			
			SqlCampos+= ",DATA_TERMINO_PENA " ;
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataProvavelTerminoPena());
			
			SqlCampos+= ",DATA_HOMOLOGACAO " ;
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataHomologacao());	
		}
		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
		
		dados.setId(executarInsert(Sql, "ID_CALCULO_LIQUIDACAO", ps));

		 
	} 
	
	
	public void salvarDataHomologacaoCalculoLiquidacao(DataProvavelDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.CALCULO_LIQUIDACAO SET DATA_HOMOLOGACAO = ? WHERE ID_CALCULO_LIQUIDACAO = ?";
		ps.adicionarDate(dados.getDataHomologacao());		
		ps.adicionarLong(dados.getIdCalculo());

		executarUpdateDelete(Sql,ps);
	} 
	
	
	
	public void alterarCalculoLiquidacao(DataProvavelDt dados, boolean salvarTodosDados) throws Exception{

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql= "UPDATE PROJUDI.CALCULO_LIQUIDACAO SET  ";
		
		Sql += "DATA_CALCULO = ?";
		ps.adicionarDate(new Date());
		
		if (salvarTodosDados){
			Sql += ",DATA_REQ_TEMPORAL_PROGRESSAO = ?";
			ps.adicionarDate(dados.getDataProvavelProgressao());
			
			Sql += ",DATA_REQ_TEMPORAL_LIVRAMENTO = ?";
			ps.adicionarDate(dados.getDataProvavelLivramento());
			
			Sql += ",DATA_VALIDADE_MAND_PRISAO = ?" ;
			ps.adicionarDate(dados.getDataValidadeMandadoPrisao());
			
			Sql += ",DATA_TERMINO_PENA = ?" ;
			ps.adicionarDate(dados.getDataProvavelTerminoPena());	
			
			Sql += ",DATA_HOMOLOGACAO = ?" ;
			ps.adicionarDate(dados.getDataHomologacao());	
		}
		
		Sql += ",HORA_RESTANTE_PSC = ?" ;
		ps.adicionarString(dados.getHoraRestantePSC());
		
		Sql += ",DATA_TERMINO_LFS = ?" ;
		ps.adicionarDate(dados.getDataTerminoLFS());
		
		Sql += ",DATA_TERMINO_ITD = ?" ;
		ps.adicionarDate(dados.getDataTerminoITD());
		
		Sql += ",VALOR_DEVIDO_PEC = ?" ;
		ps.adicionarString(dados.getValorDevidoPEC());
		
		Sql += ",QTD_DEVIDA_PCB = ?" ;
		ps.adicionarString(dados.getQtdDevidaPCB());
		
		Sql += ",TERMINO_SURSIS = ?" ;
		ps.adicionarString(dados.getTerminoSURSIS());
		
		Sql += ",INFORMACAO_ADICIONAL = ?" ;
		ps.adicionarString(dados.getInformacaoSentenciado());
	
		Sql += ",ID_USU_SERV = ? " ;
		ps.adicionarLong(dados.getIdUsuarioServentia());
	
		if (dados.getRelatorioByte() != null){
			Sql += ",RELATORIO = ?" ;
			ps.adicionarByte(dados.getRelatorioByte());
		}
		Sql=Sql.replace("SET  ,","SET  ");
		Sql = Sql + " WHERE ID_CALCULO_LIQUIDACAO = ? ";
		ps.adicionarLong(dados.getId()); 
		Sql = Sql + " AND ID_PROC  = ? ";
		ps.adicionarLong(dados.getIdProcesso());

		executarUpdateDelete(Sql,ps);
	} 
	
	/**
	 * consulta o ProcessoEventoExecucao com eventoLocal e eventoRegime, tendo como filtro o Id_ProcessoExecucao e Id_EventoExecucao
	 * param Id_ProcessoExecucao: idProcessoExecucao relacionado ao id_ProcessoEventoExecucao que será consultado
	 * param Id_EventoExecucao: idEventoExecucao relacionado ao id_ProcessoEventoExecucao que será consultado
	 * return ProcessoEventoExecucao
	 * throws Exception
	 * author wcsilva
	 */
    public ProcessoEventoExecucaoDt consultarProcessoEventoExecucaoCompleto(String idEventoExecucao, String idProcessoExecucao) throws Exception {
    	StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs = null;
		ProcessoEventoExecucaoDt objeto = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		if (idEventoExecucao == null || idEventoExecucao.length() == 0) return objeto;		
		String[] vetoridEventoExecucao =  idEventoExecucao.split(",");
		if (vetoridEventoExecucao.length == 0) return objeto;	
		
		sql.append("SELECT * FROM PROJUDI.VIEW_PROC_EVENTO_EXE pee");
		sql.append(" LEFT JOIN PROJUDI.EVENTO_LOCAL el on pee.id_proc_evento_exe = el.id_proc_evento_exe");
		sql.append(" LEFT JOIN PROJUDI.EVENTO_REGIME er on pee.id_proc_evento_exe = er.id_proc_evento_exe");
		sql.append(" WHERE ID_PROC_EXE = ? ");
		ps.adicionarLong(idProcessoExecucao);
		sql.append(" AND ID_EVENTO_EXE IN (");
		boolean ehPrimeiroItem = true;
		for (String id : vetoridEventoExecucao)
		{
			if (!ehPrimeiroItem) sql.append(",");
			sql.append("?");
			ps.adicionarLong(id);
			ehPrimeiroItem = false;
		}	
		sql.append(")");
		try{
			rs = consultar(sql.toString(), ps);
			while (rs.next()){
				objeto = new ProcessoEventoExecucaoDt();
				associarDt(objeto, rs); 
				
				objeto.getEventoLocalDt().setId(rs.getString("ID_EVENTO_LOCAL"));
				objeto.getEventoLocalDt().setLocalCumprimentoPena(rs.getString("LOCAL_CUMP_PENA"));
				objeto.getEventoLocalDt().setId_ProcessoEventoExecucao( rs.getString("ID_PROC_EVENTO_EXE"));
				objeto.getEventoLocalDt().setId_LocalCumprimentoPena( rs.getString("ID_LOCAL_CUMP_PENA"));
				
				objeto.getEventoExecucaoDt().setId(rs.getString("ID_EVENTO_EXE"));
				objeto.getEventoExecucaoDt().setEventoExecucao(rs.getString("EVENTO_EXE"));
				objeto.getEventoExecucaoDt().setAlteraLocal( rs.getString("ALTERA_LOCAL"));
				objeto.getEventoExecucaoDt().setAlteraRegime( rs.getString("ALTERA_REGIME"));
				objeto.getEventoExecucaoDt().setEventoExecucaoCodigo( rs.getString("EVENTO_EXE_CODIGO"));
				objeto.getEventoExecucaoDt().setId_EventoExecucaoTipo( rs.getString("ID_EVENTO_EXE_TIPO"));
				objeto.getEventoExecucaoDt().setEventoExecucaoTipo( rs.getString("EVENTO_EXE_TIPO"));
				objeto.getEventoExecucaoDt().setId_EventoExecucaoStatus( rs.getString("ID_EVENTO_EXE_STATUS"));
				objeto.getEventoExecucaoDt().setEventoExecucaoStatus( rs.getString("EVENTO_EXE_STATUS"));
				objeto.getEventoExecucaoDt().setCodigoTemp( rs.getString("CODIGO_TEMP"));
				
				objeto.getEventoRegimeDt().setId(rs.getString("ID_EVENTO_REGIME"));
				objeto.getEventoRegimeDt().setRegimeExecucao(rs.getString("REGIME_EXE"));
				objeto.getEventoRegimeDt().setId_ProcessoEventoExecucao( rs.getString("ID_PROC_EVENTO_EXE"));
				objeto.getEventoRegimeDt().setId_RegimeExecucao( rs.getString("ID_REGIME_EXE"));
				objeto.getEventoRegimeDt().setCodigoTemp( rs.getString("CODIGO_TEMP"));
				objeto.getEventoRegimeDt().setId_ProximoRegimeExecucao(rs.getString("ID_PROXIMO_REGIME_EXE"));
				objeto.getEventoRegimeDt().setId_PenaExecucaoTipo( rs.getString("ID_PENA_EXE_TIPO"));
				objeto.getEventoRegimeDt().setPenaExecucaoTipo( rs.getString("PENA_EXE_TIPO"));
			}
		
        } finally{
             rs.close();
        } 
		return objeto;
    }
    
	public void excluirCalculo(String idProcesso) throws Exception {
		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.CALCULO_LIQUIDACAO";
		stSql += " WHERE ID_PROC = ?";		
		ps.adicionarLong(idProcesso); 
			executarUpdateDelete(stSql,ps);

		
	} 

	public void excluirEventoFilho(String idProcessoEventoExecucaoPai) throws Exception {
		String stSql="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "DELETE FROM PROJUDI.PROC_EVENTO_EXE";
		stSql += " WHERE ID_PROC_EVENTO_EXE_PAI = ?";		
		ps.adicionarLong(idProcessoEventoExecucaoPai); 
			executarUpdateDelete(stSql,ps);
		
	} 
	
}


	
