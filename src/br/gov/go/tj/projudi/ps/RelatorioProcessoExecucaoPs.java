package br.gov.go.tj.projudi.ps;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioExecpenwebDt;
import br.gov.go.tj.projudi.dt.relatorios.SituacaoAtualExecucaoPenalDt;
import java.sql.Connection;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;


public class RelatorioProcessoExecucaoPs extends Persistencia{
	
		
	/**
     * 
     */
    private static final long serialVersionUID = 2353619284798743358L;

    public RelatorioProcessoExecucaoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Lista os sentenciados com as datas prováveis de pr, lc e mandado de prisão, a partir do periodo informado
	 * param dataProvavelDt1124
	 * return lista com os sentenciados
	 * throws Exception
	 */
	public List consultarPeriodoDataProvavel(DataProvavelDt dataProvavelDt, String posicaoPaginaAtual, String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//lista os sentenciados de processos ativos que possuem calculo de liquidacao de pena e de acordo com a serventia do usuário logado
		sql.append(" SELECT distinct p.id_proc, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, ps.PROC_status_CODIGO, " +
				"pp.CPF as CPF_SENTENCIADO, pp.NOME as NOME_SENTENCIADO, " +
				"sit.id_evento_exe_status, sit.evento_exe_status, sit.regime_exe,");
		sql.append(" sit.MODALIDADE as DESCRICAO_MODALIDADE, sit.ID_MODALIDADE, sit.data_fuga, " );
		sql.append(" cl.data_calculo, cl.data_req_temporal_livramento, cl.data_req_temporal_progressao, cl.data_termino_pena, cl.data_validade_mand_prisao, cl.data_homologacao");
		sql.append(" FROM PROJUDI.CALCULO_LIQUIDACAO cl ");
		sql.append(" INNER JOIN PROJUDI.PROC p on cl.ID_PROC = p.ID_PROC ");
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status ");
		sql.append(" INNER JOIN PROJUDI.PROC_PARTE pp on cl.ID_PROC = pp.ID_PROC");
		sql.append(" LEFT JOIN PROJUDI.VIEW_SITUACAO_ATUAL_EXE sit on sit.id_proc = cl.ID_PROC");
		sqlCondicao.append(" WHERE ps.PROC_STATUS_CODIGO IN (?,?) AND p.ID_SERV = ?");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);

		ps.adicionarLong(idServentia);
		if (dataProvavelDt.getTipoConsulta().equals("PROGRESSAO")) {//somente progressao
			sqlCondicao.append(" AND (cl.DATA_REQ_TEMPORAL_PROGRESSAO >= ? ");
			ps.adicionarDate(dataProvavelDt.getDataInicialPeriodo());
			if (dataProvavelDt.getDataFinalPeriodo().length()>0) {
				sqlCondicao.append(" AND cl.DATA_REQ_TEMPORAL_PROGRESSAO <= ? ");
				ps.adicionarDate(dataProvavelDt.getDataFinalPeriodo());
			}
			sqlCondicao.append(")");
		}
		else if (dataProvavelDt.getTipoConsulta().equals("LIVRAMENTO")) {//somente livramento
			sqlCondicao.append(" AND (cl.DATA_REQ_TEMPORAL_LIVRAMENTO >= ? ");
			ps.adicionarDate(dataProvavelDt.getDataInicialPeriodo());
			if (dataProvavelDt.getDataFinalPeriodo().length()>0) {
				sqlCondicao.append(" AND cl.DATA_REQ_TEMPORAL_LIVRAMENTO <= ? ");
				ps.adicionarDate(dataProvavelDt.getDataFinalPeriodo());
			}
			sqlCondicao.append(")");
		}
		else if (dataProvavelDt.getTipoConsulta().equals("MANDADOPRISAO")) {//mandado
			sqlCondicao.append(" AND (cl.DATA_VALIDADE_MAND_PRISAO >= ? ");
			ps.adicionarDate(dataProvavelDt.getDataInicialPeriodo());
			if (dataProvavelDt.getDataFinalPeriodo().length()>0) {
				sqlCondicao.append(" AND cl.DATA_VALIDADE_MAND_PRISAO <= ? ");
				ps.adicionarDate(dataProvavelDt.getDataFinalPeriodo());
			}
			sqlCondicao.append(")");
		}
		sql.append(sqlCondicao);
		sql.append(" ORDER BY pp.nome, p.ID_PROC");
		
		try{
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			String idProcesso = ""; //utilizado para identificar o processo, pois apenas na consulta de modalidade traz registros com o id_Processo duplicado
			
			while (rs.next()){
				if (!idProcesso.equals(rs.getString("ID_PROC"))){
					DataProvavelDt sentenciado = new DataProvavelDt();
					sentenciado.setIdProcesso(rs.getString("ID_PROC"));
					sentenciado.setNumeroProcesso(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR") + "." + rs.getString("ANO"));
					sentenciado.setProcessoTipoCodigo(rs.getString("PROC_STATUS_CODIGO"));
					if (sentenciado.getProcessoTipoCodigo().equals(String.valueOf(ProcessoStatusDt.CALCULO)))
						sentenciado.setProcessoTipo("Físico");
					else sentenciado.setProcessoTipo("Projudi");
					sentenciado.setInformacaoSentenciado(rs.getString("NOME_SENTENCIADO").toUpperCase());
					if (rs.getString("DATA_REQ_TEMPORAL_PROGRESSAO")!=null) sentenciado.setDataProvavelProgressao(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_PROGRESSAO")));
					else sentenciado.setDataProvavelProgressao("--");
					if (rs.getString("DATA_REQ_TEMPORAL_LIVRAMENTO")!=null) sentenciado.setDataProvavelLivramento(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_LIVRAMENTO")));
					else sentenciado.setDataProvavelLivramento("--");
					if (rs.getString("DATA_VALIDADE_MAND_PRISAO")!=null) sentenciado.setDataValidadeMandadoPrisao(Funcoes.FormatarData(rs.getDateTime("DATA_VALIDADE_MAND_PRISAO")));
					else sentenciado.setDataValidadeMandadoPrisao("--");
					if (rs.getString("DATA_CALCULO")!=null) sentenciado.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_CALCULO")));
					else sentenciado.setDataCalculo("--");
					if (rs.getString("ID_EVENTO_EXE_STATUS") != null && rs.getString("ID_EVENTO_EXE_STATUS").equals(String.valueOf(EventoExecucaoStatusDt.FORAGIDO))){
						if (rs.getString("DATA_FUGA") != null) sentenciado.setDataFuga(Funcoes.FormatarData(rs.getDateTime("DATA_FUGA")));
						else sentenciado.setDataFuga("Sim");
					}
						
					if (rs.getString("regime_exe") != null) sentenciado.setRegime(rs.getString("regime_exe"));
					if (rs.getString("data_homologacao") != null) sentenciado.setDataHomologacao(Funcoes.FormatarData(rs.getDateTime("data_homologacao")));
					else sentenciado.setDataHomologacao("--");
					if (rs.getString("DESCRICAO_MODALIDADE")!= null) sentenciado.setDescricaoModalidade(rs.getString("DESCRICAO_MODALIDADE"));
					lista.add(sentenciado);
					
				} else {
					//pega a descrição modalidade do objeto da última posição da lista.
					if (rs.getString("DESCRICAO_MODALIDADE")!= null && ((DataProvavelDt)lista.get(lista.size()-1)).getDescricaoModalidade().length() > 0) {
						String descricaoModalidade = ((DataProvavelDt)lista.get(lista.size()-1)).getDescricaoModalidade() + ", " + rs.getString("DESCRICAO_MODALIDADE");
						((DataProvavelDt)lista.get(lista.size()-1)).setDescricaoModalidade(descricaoModalidade);
					}	
				}
				idProcesso = rs.getString("ID_PROC");
			}
			//consultar o quantitativo total
			if (posicaoPaginaAtual.length() > 0){
				sqlCount.append("SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.CALCULO_LIQUIDACAO cl " +
						" INNER JOIN PROJUDI.PROC p on cl.ID_PROC = p.ID_PROC " +
						" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status" +
						" INNER JOIN PROJUDI.PROC_PARTE pp on p.ID_PROC = pp.ID_PROC");
				sqlCount.append(sqlCondicao);
				
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));	
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
            try{if (rsCount != null) rsCount.close();} catch(Exception e) {}
        } 
		return lista;
	}
	
	/**
	 * Lista os sentenciados sem calculo de liquidação de pena
	 * return lista com os sentenciados
	 * throws Exception
	 */
	public List consultarProcessoSemCalculo(String posicaoPaginaAtual, String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		//lista somente os processo ativos que não tem calculo de liquidação de pena e da serventia do usuario logado
		sql.append(	"SELECT distinct pp.ID_PROC, p.PROC_NUMERO, ps.PROC_status_CODIGO, p.DIGITO_VERIFICADOR, p.ANO, " +
					"pp.CPF as CPF_SENTENCIADO, pp.NOME as NOME_SENTENCIADO " +
					" FROM PROJUDI.PROC_EXE pe " +
					" INNER JOIN PROJUDI.PROC p on pe.ID_PROC_EXE_PENAL = p.ID_PROC " +
					" INNER JOIN PROJUDI.PROC_STATUS PS ON PS.ID_PROC_STATUS = P.ID_PROC_STATUS " +
					" INNER JOIN PROJUDI.PROC_PARTE pp on pe.ID_PROC_EXE_PENAL = pp.ID_PROC ");
		sqlCondicao.append(" WHERE ps.PROC_STATUS_CODIGO IN (?,?)" + 
					" AND p.ID_SERV = ? " + 
					" AND pp.ID_PROC NOT IN (SELECT cl.ID_PROC FROM PROJUDI.CALCULO_LIQUIDACAO cl) ");
		
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(idServentia);
		sql.append(sqlCondicao);
		sql.append(" ORDER BY pp.NOME");		
		try{
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				DataProvavelDt sentenciado = new DataProvavelDt();
				sentenciado.setIdProcesso(rs.getString("ID_PROC"));
				sentenciado.setNumeroProcesso(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR") + "." + rs.getString("ANO"));
				
//				sentenciado.setProcessoTipoCodigo(rs.getString("PROC_TIPO_CODIGO"));
//				if (sentenciado.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA)))
//					sentenciado.setProcessoTipo("Processo Físico");
//				else sentenciado.setProcessoTipo("Processo Projudi");
//				
				sentenciado.setProcessoTipoCodigo(rs.getString("PROC_STATUS_CODIGO"));
				if (sentenciado.getProcessoTipoCodigo().equals(String.valueOf(ProcessoStatusDt.CALCULO)))
					sentenciado.setProcessoTipo("Físico");
				else sentenciado.setProcessoTipo("Projudi");
				
				if (rs.getString("CPF_SENTENCIADO")!=null) sentenciado.setInformacaoSentenciado(Funcoes.formataCPF(rs.getString("CPF_SENTENCIADO")) + " - " + rs.getString("NOME_SENTENCIADO").toUpperCase());
				else sentenciado.setInformacaoSentenciado(rs.getString("NOME_SENTENCIADO").toUpperCase());
				lista.add(sentenciado);
			}
			if (posicaoPaginaAtual.length() > 0){
				//consultar o quantitativo total
				sqlCount.append("SELECT COUNT(distinct pp.ID_PROC) AS QUANTIDADE " +
						" FROM PROJUDI.PROC_EXE pe " +
						" INNER JOIN PROJUDI.PROC p on pe.ID_PROC_EXE_PENAL = p.ID_PROC " +
						" INNER JOIN PROJUDI.PROC_STATUS PS ON PS.ID_PROC_STATUS = P.ID_PROC_STATUS " +
						" INNER JOIN PROJUDI.PROC_PARTE pp on pe.ID_PROC_EXE_PENAL = pp.ID_PROC ");
				sqlCount.append(sqlCondicao);
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));	
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
            try{if (rsCount != null) rsCount.close();} catch(Exception e) {}
        } 
		return lista;
	}
	
	/**
	 * Lista os sentenciados apresentando o regime, local de pena e situação do processo de execuçaõ , a partir dos parametros informados
	 * param situacaoAtualDt
	 * return lista com os sentenciados
	 * throws Exception
	 */
	public List consultarSituacaoAtualProcessoExecucao(List listaIdFormaCumprimento, List listaIdLocal, List listaIdRegime, List listaIdModalidade, List listaIdStatus, String posicaoPaginaAtual, String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlFrom = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
		//lista os sentenciados
		//----retirada data da prisão: pelc.DATA_PRISAO
		sql.append("SELECT distinct	" );
		sql.append("sit.id_situacao_atual_exe, sit.id_local_cump_pena, sit.id_regime_exe, sit.id_evento_exe_status, sit.id_forma_cumprimento_exe, sit.data_alteracao,  " );
		sql.append("pe.ID_PROC_exe_penal as id_proc, pe.proc_tipo_codigo, s.serv, pe.proc_exe_penal_numero as proc_numero, pe.digito_verificador, pe.ano, pe.forum_codigo, pe.proc_status_codigo " );
		sql.append(",  pp.nome as nome_sentenciado, sit.local_cump_pena as descricao_local_cumprimento, sit.regime_Exe as descricao_regime " );
		sql.append(", cl.data_termino_pena, cl.data_calculo,  " );
		sql.append("sit.id_evento_Exe_status as id_situacao, sit.evento_exe_status as descricao_situacao, sm.regime_exe as descricao_modalidade, sit.forma_cumprimento_exe" );
		sqlFrom.append(" FROM PROJUDI.view_PROC_EXE pe   " );
		sqlFrom.append(" INNER JOIN PROJUDI.SERV s on s.id_serv = pe.id_serv  " );
		sqlFrom.append(" INNER JOIN PROJUDI.PROC_PARTE pp on pp.id_proc = pe.ID_PROC_exe_penal  " );
		sqlFrom.append(" LEFT JOIN PROJUDI.CALCULO_LIQUIDACAO cl on pe.ID_PROC_exe_penal = cl.ID_PROC   " );
		sqlFrom.append(" left join view_situacao_atual_exe sit on sit.id_proc = pe.ID_PROC_exe_penal  " );
		sqlFrom.append(" left join view_situacao_atual_modalidade sm on sm.id_situacao_atual_exe = sit.id_situacao_atual_exe " );
		
		sqlCondicao.append(" WHERE pe.PROC_STATUS_CODIGO IN (?,?) ");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		if (idServentia.length() > 0) {
			sqlCondicao.append("AND pe.ID_SERV = ? ");//busca somente para processos ativos e da serventia do usuario logado
			ps.adicionarLong(idServentia);
		}
		if (listaIdStatus != null && listaIdStatus.size() > 0){
			sqlCondicao.append(" AND sit.ID_EVENTO_EXE_STATUS in ( ");
			for (int i=0; i<listaIdStatus.size(); i++){
				sqlCondicao.append("?");
				ps.adicionarLong((String)listaIdStatus.get(i));
				if (i != listaIdStatus.size() - 1){
					sqlCondicao.append(",");
				}
			}
			sqlCondicao.append(")");
		}
		if (listaIdRegime != null && listaIdRegime.size() > 0){
			sqlCondicao.append(" AND sit.ID_REGIME_EXE in ( ");
			for (int i=0; i<listaIdRegime.size(); i++){
				sqlCondicao.append("?");
				ps.adicionarLong((String)listaIdRegime.get(i));
				if (i != listaIdRegime.size() - 1){
					sqlCondicao.append(",");
				}
			}
			sqlCondicao.append(")");
		}
		if (listaIdLocal!= null && listaIdLocal.size() > 0){
			sqlCondicao.append(" AND sit.ID_LOCAL_CUMP_PENA in ( ");
			for (int i=0; i<listaIdLocal.size(); i++){
				sqlCondicao.append("?");
				ps.adicionarLong((String)listaIdLocal.get(i));
				if (i != listaIdLocal.size() - 1){
					sqlCondicao.append(",");
				}
			}
			sqlCondicao.append(")");
		}
		if (listaIdModalidade!= null && listaIdModalidade.size() > 0){
			sqlCondicao.append(" AND sit.ID_MODALIDADE in ( ");
			for (int i=0; i<listaIdModalidade.size(); i++){
				sqlCondicao.append("?");
				ps.adicionarLong((String)listaIdModalidade.get(i));
				if (i != listaIdModalidade.size() - 1){
					sqlCondicao.append(",");
				}
			}
			sqlCondicao.append(")");
		}
		if (listaIdFormaCumprimento!= null && listaIdFormaCumprimento.size() > 0){
			sqlCondicao.append(" AND sit.ID_FORMA_CUMPRIMENTO_EXE in ( ");
			for (int i=0; i<listaIdFormaCumprimento.size(); i++){
				sqlCondicao.append("?");
				ps.adicionarLong((String)listaIdFormaCumprimento.get(i));
				if (i != listaIdFormaCumprimento.size() - 1){
					sqlCondicao.append(",");
				}
			}
			sqlCondicao.append(")");
		}
		sql.append(sqlFrom);
		sql.append(sqlCondicao);
		sql.append(" ORDER BY ");
		if (idServentia.length() == 0){
			sql.append("s.SERV,");
		}
		sql.append("pp.nome, pe.id_proc_exe_penal");
		
		try{
			String idProcesso = ""; //utilizado para identificar o processo, pois apenas na consulta de modalidade traz registros com o id_Processo duplicado
		
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				if (!idProcesso.equals(rs.getString("ID_PROC"))){
					SituacaoAtualExecucaoPenalDt situacaoAtual = new SituacaoAtualExecucaoPenalDt();
					situacaoAtual.setServentia(rs.getString("SERV"));
					situacaoAtual.setIdProcesso(rs.getString("ID_PROC"));
					situacaoAtual.setNumeroProcesso(rs.getString("PROC_NUMERO") + "." + Funcoes.completarZeros(rs.getString("DIGITO_VERIFICADOR"), 2) + "." + rs.getString("ANO"));
					situacaoAtual.setNumeroProcessoCompleto(rs.getString("PROC_NUMERO") + "." + Funcoes.completarZeros(rs.getString("DIGITO_VERIFICADOR"), 2) + "." + rs.getString("ANO") + ".8.09." + Funcoes.completarZeros(rs.getString("forum_codigo"), 4));
					situacaoAtual.setProcessoTipoCodigo(rs.getString("PROC_STATUS_CODIGO"));
					if (situacaoAtual.getProcessoTipoCodigo().equals(String.valueOf(ProcessoStatusDt.CALCULO)))
						situacaoAtual.setProcessoTipo("Físico");
					else situacaoAtual.setProcessoTipo("Projudi");
					
					situacaoAtual.setInformacaoSentenciado(rs.getString("NOME_SENTENCIADO").toUpperCase());
					situacaoAtual.setIdLocalCumprimentoPena(rs.getString("ID_LOCAL_CUMP_PENA"));
					situacaoAtual.setDescricaoLocalCumprimentoPena(rs.getString("DESCRICAO_LOCAL_CUMPRIMENTO"));
					situacaoAtual.setIdRegime(rs.getString("ID_REGIME_EXE"));
					situacaoAtual.setDescricaoRegime(rs.getString("DESCRICAO_REGIME"));
					if (rs.getString("DATA_TERMINO_PENA") != null) situacaoAtual.setDataTerminoPena(Funcoes.FormatarData(rs.getDateTime("DATA_TERMINO_PENA")));
					else situacaoAtual.setDataTerminoPena("--");
					if (rs.getString("DATA_CALCULO") != null) situacaoAtual.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_CALCULO")));
					else situacaoAtual.setDataCalculo("--");
					situacaoAtual.setIdSituacao(rs.getString("ID_SITUACAO"));
					situacaoAtual.setDescricaoSituacao(rs.getString("DESCRICAO_SITUACAO"));	
					if (rs.getString("DESCRICAO_MODALIDADE")!= null) situacaoAtual.setDescricaoModalidade(rs.getString("DESCRICAO_MODALIDADE"));
					if (rs.getString("forma_cumprimento_exe")!= null) situacaoAtual.setFormaCumprimento(rs.getString("forma_cumprimento_exe"));
					if (rs.getString("DATA_ALTERACAO") != null) situacaoAtual.setDataAtualizacao(Funcoes.FormatarData(rs.getDateTime("DATA_ALTERACAO")));
					else situacaoAtual.setDataAtualizacao("--");
					lista.add(situacaoAtual);
				} else {
					//pega a descrição modalidade do objeto da última posição da lista.
					if (rs.getString("DESCRICAO_MODALIDADE")!= null && ((SituacaoAtualExecucaoPenalDt)lista.get(lista.size()-1)).getDescricaoModalidade().length() > 0) {
						String descricaoModalidade = ((SituacaoAtualExecucaoPenalDt)lista.get(lista.size()-1)).getDescricaoModalidade() + ", " + rs.getString("DESCRICAO_MODALIDADE");
						((SituacaoAtualExecucaoPenalDt)lista.get(lista.size()-1)).setDescricaoModalidade(descricaoModalidade);	
					}	
				}
				idProcesso = rs.getString("ID_PROC");
			}
			if (posicaoPaginaAtual.length() > 0){
				//consultar o quantitativo total
				StringBuffer sqlCount = new StringBuffer();
				sqlCount.append("SELECT COUNT(distinct pe.id_proc_exe_penal) AS QUANTIDADE");
				sqlCount.append(sqlFrom);
				sqlCount.append(sqlCondicao);
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));
			}
		
        } finally{
            try{if (rsCount != null) rsCount.close();} catch(Exception e) {}
            try{if (rs != null) rs.close();} catch(Exception e) {}
        } 
		return lista;
	}
	
	/**
	 * lista somente os processo ativos que  tem calculo de liquidação de pena e da serventia do usuario logado, no período informado
	 * param dataProvavelDt: objeto com os parâmetros da consulta
	 * param posicaoPaginaAtual: utilizado na paginação
	 * param idServentia: serventia do usuário logado
	 * return
	 * throws Exception
	 */
	public List consultarPeriodoCalculo(DataProvavelDt dataProvavelDt, String posicaoPaginaAtual, String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//lista somente os processo ativos que  tem calculo de liquidação de pena e da serventia do usuario logado, no período informado
		sql.append(	"SELECT distinct pp.ID_PROC, p.PROC_NUMERO, p.ANO, p.DIGITO_VERIFICADOR, ps.PROC_status_CODIGO, " +
					" pp.CPF as CPF_SENTENCIADO, pp.NOME as NOME_SENTENCIADO, " +
					" cl.DATA_CALCULO, cl.DATA_REQ_TEMPORAL_PROGRESSAO, cl.DATA_REQ_TEMPORAL_LIVRAMENTO, cl.DATA_VALIDADE_MAND_PRISAO, cl.DATA_TERMINO_PENA, cl.DATA_HOMOLOGACAO," +
					" us.ID_USU_SERV, u.NOME, vur.REGIME_EXE" +
					" FROM PROJUDI.CALCULO_LIQUIDACAO cl" +
					" INNER JOIN PROJUDI.PROC p ON p.ID_PROC = cl.ID_PROC" +
					" INNER JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS" +
					" INNER JOIN PROJUDI.PROC_PARTE pp on p.ID_PROC = pp.ID_PROC " +
					" LEFT join projudi.view_situacao_atual_exe vur on vur.id_proc = p.id_proc" +
					" LEFT JOIN PROJUDI.USU_SERV us on cl.ID_USU_SERV = us.ID_USU_SERV" + 
					" LEFT JOIN PROJUDI.USU u on u.ID_USU = us.ID_USU");
		sqlCondicao.append(" WHERE ps.PROC_STATUS_CODIGO IN (?,?)" + 
					" AND p.ID_SERV = ?");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(idServentia);
		sqlCondicao.append(" AND (cl.DATA_CALCULO >= ? ");
		ps.adicionarDate(dataProvavelDt.getDataInicialPeriodo());
		if (dataProvavelDt.getDataFinalPeriodo().length()>0) {
			sqlCondicao.append(" AND cl.DATA_CALCULO <= ? ");
			ps.adicionarDate(dataProvavelDt.getDataFinalPeriodo());
		}
		sqlCondicao.append(")");
		sql.append(sqlCondicao);
		sql.append(" ORDER BY pp.NOME");
		try{
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				DataProvavelDt sentenciado = new DataProvavelDt();
				sentenciado.setIdProcesso(rs.getString("ID_PROC"));
				sentenciado.setNumeroProcesso(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR") + "." + rs.getString("ANO"));
				
				sentenciado.setProcessoTipoCodigo(rs.getString("PROC_STATUS_CODIGO"));
				if (sentenciado.getProcessoTipoCodigo().equals(String.valueOf(ProcessoStatusDt.CALCULO)))
					sentenciado.setProcessoTipo("Físico");
				else sentenciado.setProcessoTipo("Projudi");
				
				sentenciado.setInformacaoSentenciado(rs.getString("NOME_SENTENCIADO").toUpperCase());
				if (rs.getString("DATA_CALCULO")!=null) sentenciado.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_CALCULO")));
				else sentenciado.setDataCalculo("--");
				if (rs.getString("DATA_REQ_TEMPORAL_PROGRESSAO")!=null) sentenciado.setDataProvavelProgressao(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_PROGRESSAO")));
				else sentenciado.setDataProvavelProgressao("--");
				if (rs.getString("DATA_REQ_TEMPORAL_LIVRAMENTO")!=null) sentenciado.setDataProvavelLivramento(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_LIVRAMENTO")));
				else sentenciado.setDataProvavelLivramento("--");
				if (rs.getString("DATA_VALIDADE_MAND_PRISAO")!=null) sentenciado.setDataValidadeMandadoPrisao(Funcoes.FormatarData(rs.getDateTime("DATA_VALIDADE_MAND_PRISAO")));
				else sentenciado.setDataValidadeMandadoPrisao("--");
				if (rs.getString("DATA_TERMINO_PENA")!=null) sentenciado.setDataProvavelTerminoPena(Funcoes.FormatarData(rs.getDateTime("DATA_TERMINO_PENA")));
				else sentenciado.setDataProvavelTerminoPena("--");
				if (rs.getString("ID_USU_SERV")!=null) sentenciado.setIdUsuarioServentia(rs.getString("ID_USU_SERV"));
				else sentenciado.setIdUsuarioServentia("--");
				if (rs.getString("NOME")!=null) sentenciado.setNomeUsuario(rs.getString("NOME"));
				else sentenciado.setNomeUsuario("--");
				if (rs.getString("REGIME_EXE") != null) sentenciado.setRegime(rs.getString("REGIME_EXE"));
				else sentenciado.setRegime("--");
				if (rs.getString("DATA_HOMOLOGACAO") != null) sentenciado.setDataHomologacao(Funcoes.FormatarData(rs.getDateTime("DATA_HOMOLOGACAO")));
				else sentenciado.setDataHomologacao("--");
				
				lista.add(sentenciado);
			}
			if (posicaoPaginaAtual.length() > 0){
				//consultar o quantitativo total
				sqlCount.append("SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE " +
						" FROM PROJUDI.CALCULO_LIQUIDACAO cl " +
						" INNER JOIN PROJUDI.PROC p on p.ID_PROC = cl.ID_PROC" +
						" INNER JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS");
				sqlCount.append(sqlCondicao);
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));	
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
            try{if (rsCount != null) rsCount.close();} catch(Exception e) {}
        } 
		return lista;
	}
	
	//lista os processos de execução penal ativos da serventia.
	public List listarProcessoAtivoServentia(String posicaoPaginaAtual, String idServentia) throws Exception{
		return listarProcessoAtivoServentia(posicaoPaginaAtual, idServentia, false);
	}
	
	public List listarProcessoAtivoServentia(String posicaoPaginaAtual, String idServentia, boolean somenteMaioresDe70Anos) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//lista somente os processo ativos que  tem calculo de liquidação de pena e da serventia do usuario logado, no período informado
		sql.append("select distinct p.ID_PROC, p.PROC_NUMERO, p.ANO, p.DIGITO_VERIFICADOR, ps.PROC_status_CODIGO, ");
		sql.append(" pp.CPF as CPF_SENTENCIADO, pp.NOME as NOME_SENTENCIADO, ");
		sql.append(" cl.DATA_CALCULO, cl.DATA_REQ_TEMPORAL_PROGRESSAO, cl.DATA_REQ_TEMPORAL_LIVRAMENTO, cl.DATA_VALIDADE_MAND_PRISAO, cl.DATA_TERMINO_PENA, cl.DATA_HOMOLOGACAO,");
		sql.append(" us.ID_USU_SERV, u.NOME, vur.REGIME_EXE ");
		sql.append(" FROM PROJUDI.PROC_EXE pe ");
		sql.append(" INNER JOIN PROJUDI.PROC p on pe.ID_PROC_EXE_PENAL = p.ID_PROC ");
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status ");
		sql.append(" INNER JOIN PROJUDI.PROC_PARTE pp on p.ID_PROC = pp.ID_PROC ");
		sql.append(" LEFT join projudi.view_situacao_atual_exe vur on vur.id_proc = p.id_proc");
		sql.append(" LEFT JOIN PROJUDI.calculo_liquidacao cl on pe.id_proc_exe_penal = cl.id_proc ");
		sql.append(" LEFT JOIN PROJUDI.USU_SERV us on cl.ID_USU_SERV = us.ID_USU_SERV ");
		sql.append(" LEFT JOIN PROJUDI.USU u on u.ID_USU = us.ID_USU ");		
		sqlCondicao.append(" WHERE ps.PROC_STATUS_CODIGO IN (?,?)"); 
		sqlCondicao.append(" AND p.ID_SERV = ?");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(idServentia);
		if(somenteMaioresDe70Anos){
			sqlCondicao.append(" AND pp.DATA_NASCIMENTO <= ?");
			Calendar hoje = Calendar.getInstance();
			hoje.roll(Calendar.YEAR, -70);			
			ps.adicionarDate(hoje.getTime());
		}
		sql.append(sqlCondicao);
		sql.append(" ORDER BY pp.NOME");
		
		try{
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				DataProvavelDt sentenciado = new DataProvavelDt();
				sentenciado.setIdProcesso(rs.getString("ID_PROC"));
				sentenciado.setNumeroProcesso(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR") + "." + rs.getString("ANO"));
				
				sentenciado.setProcessoTipoCodigo(rs.getString("PROC_STATUS_CODIGO"));
				if (sentenciado.getProcessoTipoCodigo().equals(String.valueOf(ProcessoStatusDt.CALCULO)))
					sentenciado.setProcessoTipo("Físico");
				else sentenciado.setProcessoTipo("Projudi");
				
				sentenciado.setInformacaoSentenciado(rs.getString("NOME_SENTENCIADO").toUpperCase());
				if (rs.getString("DATA_CALCULO")!= null) sentenciado.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_CALCULO")));
				else sentenciado.setDataCalculo("--");
				if (rs.getString("DATA_REQ_TEMPORAL_PROGRESSAO")!=null) sentenciado.setDataProvavelProgressao(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_PROGRESSAO")));
				else sentenciado.setDataProvavelProgressao("--");
				if (rs.getString("DATA_REQ_TEMPORAL_LIVRAMENTO")!=null) sentenciado.setDataProvavelLivramento(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_LIVRAMENTO")));
				else sentenciado.setDataProvavelLivramento("--");
				if (rs.getString("DATA_VALIDADE_MAND_PRISAO")!=null) sentenciado.setDataValidadeMandadoPrisao(Funcoes.FormatarData(rs.getDateTime("DATA_VALIDADE_MAND_PRISAO")));
				else sentenciado.setDataValidadeMandadoPrisao("--");
				if (rs.getString("DATA_TERMINO_PENA")!=null) sentenciado.setDataProvavelTerminoPena(Funcoes.FormatarData(rs.getDateTime("DATA_TERMINO_PENA")));
				else sentenciado.setDataProvavelTerminoPena("--");
				if (rs.getString("ID_USU_SERV")!=null) sentenciado.setIdUsuarioServentia(rs.getString("ID_USU_SERV"));
				if (rs.getString("NOME")!=null) sentenciado.setNomeUsuario(rs.getString("NOME"));
				else sentenciado.setNomeUsuario("--");
				if (rs.getString("REGIME_EXE") != null) sentenciado.setRegime(rs.getString("REGIME_EXE"));
				else sentenciado.setRegime("--");
				if (rs.getString("DATA_HOMOLOGACAO") != null) sentenciado.setDataHomologacao(Funcoes.FormatarData(rs.getDateTime("DATA_HOMOLOGACAO")));
				else sentenciado.setDataHomologacao("--");
				
				lista.add(sentenciado);
			}
			if (posicaoPaginaAtual.length() > 0){
				//consultar o quantitativo total
				sqlCount.append("SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ");
				sqlCount.append(" FROM PROJUDI.PROC_EXE pe ");
				sqlCount.append(" INNER JOIN PROJUDI.PROC p on pe.ID_PROC_EXE_PENAL = p.ID_PROC ");
				sqlCount.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status ");
				
				sqlCount.append(sqlCondicao);
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));	
			}
					
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
            try{if (rsCount != null) rsCount.close();} catch(Exception e) {}
        } 
		return lista;
	}

	//lista os processos de execução penal ativos da serventia.
	public List listarProcessoCalculoAtraso(String posicaoPaginaAtual, String idServentia) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//lista somente os processo ativos que  tem calculo de liquidação de pena e da serventia do usuario logado, no período informado
		sql.append(" select p.id_proc, p.proc_numero, p.ano, p.digito_verificador, c.DATA_CALCULO, c.DATA_REQ_TEMPORAL_PROGRESSAO, c.DATA_REQ_TEMPORAL_LIVRAMENTO, c.DATA_VALIDADE_MAND_PRISAO, c.DATA_TERMINO_PENA, c.DATA_HOMOLOGACAO, vur.regime_exe, pp.nome as NOME_SENTENCIADO, ps.PROC_status_CODIGO");
		sql.append(" from projudi.calculo_liquidacao c ");
		sql.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on ps.id_proc_status = p.id_proc_status");
		sql.append(" inner join projudi.proc_parte pp on pp.id_proc = p.id_proc");
		sql.append(" inner join projudi.view_situacao_atual_exe vur on vur.id_proc = p.id_proc");
		sqlCondicao.append(" where ps.proc_status_codigo in (?, ?) ");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		sqlCondicao.append(" and data_calculo < sysdate-365"); //cálculo atraso
		sqlCondicao.append(" and vur.id_regime_exe <> ?"); //quando o sentenciado esta em LC, não tem mais cálculo.
		ps.adicionarLong(RegimeExecucaoDt.LIVRAMENTO_CONDICIONAL);
		sqlCondicao.append(" AND p.id_serv = ?");
		ps.adicionarLong(idServentia);
		sqlCondicao.append(" and vur.id_evento_exe_status <> ? and c.DATA_VALIDADE_MAND_PRISAO is null"); //quando o sentenciado está foragido, não há cálculo em atraso.
		ps.adicionarLong(EventoExecucaoStatusDt.FORAGIDO);
		sql.append(sqlCondicao);
		sql.append(" order by c.data_calculo");
		
		try{
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				DataProvavelDt sentenciado = new DataProvavelDt();
				sentenciado.setIdProcesso(rs.getString("ID_PROC"));
				sentenciado.setNumeroProcesso(rs.getString("PROC_NUMERO") + "." + rs.getString("DIGITO_VERIFICADOR") + "." + rs.getString("ANO"));
				
				sentenciado.setProcessoTipoCodigo(rs.getString("PROC_STATUS_CODIGO"));
				if (sentenciado.getProcessoTipoCodigo().equals(String.valueOf(ProcessoStatusDt.CALCULO)))
					sentenciado.setProcessoTipo("Físico");
				else sentenciado.setProcessoTipo("Projudi");
				
				sentenciado.setInformacaoSentenciado(rs.getString("NOME_SENTENCIADO").toUpperCase());
				if (rs.getString("DATA_CALCULO")!=null) sentenciado.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_CALCULO")));
				else sentenciado.setDataCalculo("--");
				if (rs.getString("DATA_REQ_TEMPORAL_PROGRESSAO")!=null) sentenciado.setDataProvavelProgressao(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_PROGRESSAO")));
				else sentenciado.setDataProvavelProgressao("--");
				if (rs.getString("DATA_REQ_TEMPORAL_LIVRAMENTO")!=null) sentenciado.setDataProvavelLivramento(Funcoes.FormatarData(rs.getDateTime("DATA_REQ_TEMPORAL_LIVRAMENTO")));
				else sentenciado.setDataProvavelLivramento("--");
				if (rs.getString("DATA_VALIDADE_MAND_PRISAO")!=null) sentenciado.setDataValidadeMandadoPrisao(Funcoes.FormatarData(rs.getDateTime("DATA_VALIDADE_MAND_PRISAO")));
				else sentenciado.setDataValidadeMandadoPrisao("--");
				if (rs.getString("DATA_TERMINO_PENA")!=null) sentenciado.setDataProvavelTerminoPena(Funcoes.FormatarData(rs.getDateTime("DATA_TERMINO_PENA")));
				else sentenciado.setDataProvavelTerminoPena("--");
				if (rs.getString("REGIME_EXE") != null) sentenciado.setRegime(rs.getString("REGIME_EXE"));
				else sentenciado.setRegime("--");
				if (rs.getString("DATA_HOMOLOGACAO") != null) sentenciado.setDataHomologacao(Funcoes.FormatarData(rs.getDateTime("DATA_HOMOLOGACAO")));
				else sentenciado.setDataHomologacao("--");
				
				lista.add(sentenciado);
			}
			
			if (posicaoPaginaAtual.length() > 0){
				//consultar o quantitativo total
				sqlCount.append("SELECT COUNT(p.ID_PROC) AS QUANTIDADE ");
				sqlCount.append(" from projudi.calculo_liquidacao c ");
				sqlCount.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
				sqlCount.append(" inner join projudi.proc_status ps on ps.id_proc_status = p.id_proc_status");
				sqlCount.append(" inner join projudi.view_situacao_atual_exe vur on vur.id_proc = p.id_proc");
				sqlCount.append(sqlCondicao);
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));	
			}
						
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
            try{if (rsCount != null) rsCount.close();} catch(Exception e) {}
        } 
		return lista;
	}
	
	//Lista de processos com data de término da pena anterior à data atual, agrupados por serventia
	public List listarProcessoTerminoPenaAtraso(String posicaoPaginaAtual, String idServentia, String serventia) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select distinct p.id_proc, p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo, c.data_termino_pena, sit.regime_exe, c.data_calculo, ps.proc_status_codigo, pp.nome");
		sql.append(" from projudi.calculo_liquidacao c ");
		sql.append(" inner join projudi.view_situacao_atual_exe sit on sit.id_proc = c.id_proc");
		sql.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on ps.id_proc_status = p.id_proc_status");
		sql.append(" inner join projudi.proc_parte pp on pp.id_proc = p.id_proc");
		sqlCondicao.append(" where ps.proc_status_codigo in (?,?)");
		sqlCondicao.append(" and sit.id_evento_exe_status = ?"); //cumprindo pena
		sqlCondicao.append(" and c.data_termino_pena < sysdate"); //calculo atualizado
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoStatusDt.CUMPRINDO_PENA);
		sqlCondicao.append(" AND p.id_serv = ?");
		ps.adicionarLong(idServentia);
		sql.append(sqlCondicao);
		sql.append(" order by c.data_termino_pena");
		
		try{
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				RelatorioExecpenwebDt sentenciado = new RelatorioExecpenwebDt();
				sentenciado.setIdProcesso(rs.getString("ID_PROC"));
				sentenciado.setProcessoAno(rs.getString("ano"));
				sentenciado.setProcessoDigito(rs.getString("digito_verificador"));
				sentenciado.setProcessoNumero(rs.getString("proc_numero"));
				sentenciado.setForumCodigo(rs.getString("forum_codigo"));
				sentenciado.setProcessoNumeroCompleto();
				sentenciado.setSentenciado(rs.getString("nome").toUpperCase());
				sentenciado.setServentia(serventia);
				
				if (rs.getString("DATA_CALCULO")!=null) sentenciado.setDataCalculo(Funcoes.FormatarData(rs.getDateTime("DATA_CALCULO")));
				else sentenciado.setDataCalculo("--");
				if (rs.getString("DATA_TERMINO_PENA")!=null) sentenciado.setDataTerminoPena(Funcoes.FormatarData(rs.getDateTime("DATA_TERMINO_PENA")));
				else sentenciado.setDataTerminoPena("--");
				if (rs.getString("REGIME_EXE") != null) sentenciado.setRegime(rs.getString("REGIME_EXE"));
				else sentenciado.setRegime("--");
				lista.add(sentenciado);
			}
			
			if (posicaoPaginaAtual.length() > 0){
				//consultar o quantitativo total
				sqlCount.append("SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ");
				sqlCount.append(" from projudi.calculo_liquidacao c ");
				sqlCount.append(" inner join projudi.view_situacao_atual_exe sit on sit.id_proc = c.id_proc");
				sqlCount.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
				sqlCount.append(" inner join projudi.proc_status ps on ps.id_proc_status = p.id_proc_status");
				sqlCount.append(sqlCondicao);
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));	
			}
			
		
        } finally{
        	 try{if (rs != null) rs.close();} catch(Exception e1) {}
             try{if (rsCount != null) rsCount.close();} catch(Exception e) {}
        } 
		return lista;
	}
	
	//Lista dos processos que não receberam a Progressão de Regime, agrupados por serventia.
	//Ob.: o sentenciado tem data provável de Progressão de Regime anterior à data atual, o cálculo está atualizado, mas não recebeu a Progressão.
	public List consultarProcessoProgressaoAtraso(String posicaoPaginaAtual, String idServentia, String serventia) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select distinct p.id_proc, c.data_calculo, c.data_req_temporal_progressao, c.data_termino_pena, p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo, ur.regime_exe, pp.nome");
		sql.append(" from projudi.calculo_liquidacao c");
		sql.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
		sql.append(" inner join projudi.view_situacao_atual_exe ur on ur.id_proc = c.id_proc");
		sql.append(" inner join projudi.proc_parte pp on pp.id_proc = p.id_proc");
		sqlCondicao.append(" where ps.proc_status_codigo in (?,?)");
		sqlCondicao.append(" and c.data_req_temporal_progressao is not null"); //tem data provável da PR
		sqlCondicao.append(" and (c.data_homologacao is null or c.data_homologacao < data_calculo)"); //cálculo não foi homologado
		sqlCondicao.append(" and c.data_req_temporal_progressao < sysdate"); //data provável menor que a data atual
		sqlCondicao.append(" and (");
		sqlCondicao.append("   p.id_proc not in ("); //não tem evento de PR com data início >= à data do provável benefício
		sqlCondicao.append("                       select id_proc_exe_penal"); 
		sqlCondicao.append("                       from projudi.proc_evento_exe"); 
		sqlCondicao.append("                       where id_evento_exe = ? ");
		sqlCondicao.append("                       and c.data_req_temporal_progressao < data_inicio");
		sqlCondicao.append("                     ) ");
		sqlCondicao.append(" )");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoDt.PROGRESSAO_REGIME);
		sqlCondicao.append(" AND p.id_serv = ?");
		ps.adicionarLong(idServentia);
		sql.append(sqlCondicao);
		sql.append(" order by c.data_req_temporal_progressao");
		
		try{
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setIdProcesso(rs.getString("id_proc"));
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				rel.setRegime(rs.getString("regime_exe"));
				rel.setSentenciado(rs.getString("nome"));
				if (rs.getDateTime("data_calculo") != null) rel.setDataCalculo(Funcoes.dateToStringSoData(rs.getDateTime("data_calculo")));
				if (rs.getDateTime("data_req_temporal_progressao") != null) rel.setDataProgressao(Funcoes.dateToStringSoData(rs.getDateTime("data_req_temporal_progressao")));
				if (rs.getDateTime("data_termino_pena") != null) rel.setDataTerminoPena(Funcoes.dateToStringSoData(rs.getDateTime("data_termino_pena")));
				rel.setSentenciado(rs.getString("nome"));
				rel.setServentia(serventia);
				lista.add(rel);
			}
			
			if (posicaoPaginaAtual.length() > 0){
				//consultar o quantitativo total
				sqlCount.append("SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ");
				sqlCount.append(" from projudi.calculo_liquidacao c");
				sqlCount.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
				sqlCount.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
				sqlCount.append(" inner join projudi.serv s on p.id_serv = s.id_serv");
				sqlCount.append(" inner join projudi.view_situacao_atual_exe ur on ur.id_proc = c.id_proc");
				sqlCount.append(sqlCondicao);
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));	
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	
	//Lista dos processos que não receberam o benefício do Livramento Condicional, agrupados por serventia.
	//Ob.: o sentenciado têm data provável de Livramento Condicional anterior à data atual, o cálculo está atualizado, mas não recebeu o benefício.
	public List consultarProcessoLivramentoAtraso(String posicaoPaginaAtual, String idServentia, String serventia) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select c.data_calculo, c.data_req_temporal_livramento, c.data_termino_pena, p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo, ur.regime_exe, pp.nome, p.id_proc");
		sql.append(" from projudi.calculo_liquidacao c");
		sql.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
		sql.append(" inner join projudi.proc_parte pp on pp.id_proc = p.id_proc");
		sql.append(" inner join projudi.view_situacao_atual_exe ur on ur.id_proc = c.id_proc");
		sqlCondicao.append(" where ps.proc_status_codigo in (?,?)");
		sqlCondicao.append(" and c.data_req_temporal_livramento is not null"); //tem data provável da LC
		sqlCondicao.append(" and (c.data_homologacao is null or c.data_homologacao < data_calculo)"); //cálculo não foi homologado
		sqlCondicao.append(" and c.data_req_temporal_livramento < sysdate"); //data provável menor que a data atual
		sqlCondicao.append(" and (");
		sqlCondicao.append("   p.id_proc not in ("); //não tem evento de concessão do LC com data início >= à data do provável benefício
		sqlCondicao.append("                       select id_proc_exe_penal"); 
		sqlCondicao.append("                       from projudi.proc_evento_exe"); 
		sqlCondicao.append("                       where id_evento_exe = ?");
		sqlCondicao.append("                       and c.data_req_temporal_livramento < data_inicio");
		sqlCondicao.append("                     ) ");
		sqlCondicao.append(" )");
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL);		
		sqlCondicao.append(" AND p.id_serv = ?");
		ps.adicionarLong(idServentia);
		sql.append(sqlCondicao);
		sql.append(" order by c.data_req_temporal_livramento");
		
		try{
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setIdProcesso(rs.getString("id_proc"));
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				rel.setSentenciado(rs.getString("nome"));
				rel.setRegime(rs.getString("regime_exe"));
				if (rs.getDateTime("data_calculo") != null) rel.setDataCalculo(Funcoes.dateToStringSoData(rs.getDateTime("data_calculo")));
				if (rs.getDateTime("data_req_temporal_livramento") != null) rel.setDataProgressao(Funcoes.dateToStringSoData(rs.getDateTime("data_req_temporal_livramento"))); //seta a dataProgressao para utilizar no relatório
				if (rs.getDateTime("data_termino_pena") != null) rel.setDataTerminoPena(Funcoes.dateToStringSoData(rs.getDateTime("data_termino_pena")));
				rel.setServentia(serventia);
				lista.add(rel);
			}
			
			if (posicaoPaginaAtual.length() > 0){
				//consultar o quantitativo total
				sqlCount.append("SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ");
				sqlCount.append(" from projudi.calculo_liquidacao c");
				sqlCount.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
				sqlCount.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
				sqlCount.append(" inner join projudi.view_situacao_atual_exe ur on ur.id_proc = c.id_proc");
				sqlCount.append(sqlCondicao);
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));	
			}
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
	//Lista dos processos com data de validade do mandado de prisão vencido e status foragido.
	//Ob.: o sentenciado têm data de validade do mandado de prisão anterior à data atual, o cálculo está atualizado, mas o sentenciado está foragido.
	public List consultarProcessoMandadoAtraso(String posicaoPaginaAtual, String idServentia, String serventia) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlCount = new StringBuffer();
		StringBuffer sqlCondicao = new StringBuffer();
		List lista = new ArrayList();
		ResultSetTJGO rs = null;
		ResultSetTJGO rsCount = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append(" select c.data_calculo, c.data_validade_mand_prisao, c.data_termino_pena, p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo, pp.nome, vus.regime_exe, p.id_proc");
		sql.append(" from projudi.calculo_liquidacao c");
		sql.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
		sql.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
		sql.append(" inner join projudi.proc_parte pp on pp.id_proc = p.id_proc");
		sql.append(" inner join projudi.view_situacao_atual_exe vus ON vus.id_proc = p.id_proc"); 
		sqlCondicao.append(" where ps.proc_status_codigo in (?,?)"); //processo ativo
		sqlCondicao.append(" and c.data_validade_mand_prisao is not null"); //tem data de validade do mandado de prisão
		sqlCondicao.append(" and c.data_validade_mand_prisao < sysdate"); //data de validade do mandado de prisão anterior à data atual
		sqlCondicao.append(" and vus.id_evento_exe_status = ?"); //status foragido
		ps.adicionarLong(ProcessoStatusDt.ATIVO);
		ps.adicionarLong(ProcessoStatusDt.CALCULO);
		ps.adicionarLong(EventoExecucaoStatusDt.FORAGIDO);
		sqlCondicao.append(" AND p.id_serv = ?");
		ps.adicionarLong(idServentia);
		sql.append(sqlCondicao);
		sql.append(" order by c.data_validade_mand_prisao");
		
		
		try{
			if (posicaoPaginaAtual.length() > 0)
				rs = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
			else rs = consultar(sql.toString(), ps);
			
			while (rs.next()){
				RelatorioExecpenwebDt rel = new RelatorioExecpenwebDt();
				rel.setIdProcesso(rs.getString("id_proc"));
				rel.setProcessoAno(rs.getString("ano"));
				rel.setProcessoDigito(rs.getString("digito_verificador"));
				rel.setProcessoNumero(rs.getString("proc_numero"));
				rel.setForumCodigo(rs.getString("forum_codigo"));
				rel.setProcessoNumeroCompleto();
				rel.setSentenciado(rs.getString("nome"));
				rel.setRegime(rs.getString("regime_exe"));
				if (rs.getDateTime("data_calculo") != null) rel.setDataCalculo(Funcoes.dateToStringSoData(rs.getDateTime("data_calculo")));
				if (rs.getDateTime("data_validade_mand_prisao") != null) rel.setDataProgressao(Funcoes.dateToStringSoData(rs.getDateTime("data_validade_mand_prisao"))); //seta a dataProgressao para utilizar no relatório
				if (rs.getDateTime("data_termino_pena") != null) rel.setDataTerminoPena(Funcoes.dateToStringSoData(rs.getDateTime("data_termino_pena")));
				rel.setServentia(serventia);
				lista.add(rel);
			}
			
			if (posicaoPaginaAtual.length() > 0){
				//consultar o quantitativo total
				sqlCount.append("SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ");
				sqlCount.append(" from projudi.calculo_liquidacao c");
				sqlCount.append(" inner join projudi.proc p on c.id_proc = p.id_proc");
				sqlCount.append(" inner join projudi.proc_status ps on p.id_proc_status = ps.id_proc_status");
				sqlCount.append(" inner join projudi.view_situacao_atual_exe vus ON vus.id_proc = p.id_proc");
				sqlCount.append(sqlCondicao);
				rsCount = consultar(sqlCount.toString(), ps);
				if (rsCount.next()) lista.add(rsCount.getLong("QUANTIDADE"));	
			}
			
		
        } finally{
            try{if (rs != null) rs.close();} catch(Exception e1) {}
        } 
		return lista;
	}
	
}