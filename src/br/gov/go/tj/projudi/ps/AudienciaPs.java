package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.AudienciaRelatorioDt;
import br.gov.go.tj.projudi.dt.AudienciaSegundoGrauDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RelatorioAudienciaProcesso;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.Certificado.Signer;

@SuppressWarnings("unchecked")
public class AudienciaPs extends AudienciaPsGen {

    /**
     * 
     */
    private static final long serialVersionUID = 5925255604033591827L;
    
    private static final int QUANTIDADE_PARTES_RELATORIO = 10;

    public AudienciaPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
     * Sobrescrevendo o método inserir da classe AudienciaPsGen. Método que
     * possui a SQL que irá inserir as agendas geradas com os dados fornecidos
     * pelo usuário, ou seja, inserir objetos do tipo AudienciaDt
     * 
     * @author Keila Sousa Silva
     * @param audienciaDt
     * @throws Exception
     */
    public void inserir(AudienciaDt audienciaDt) throws Exception {
    	String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO PROJUDI.AUDI (";
		SqlValores = " Values (";
		 // Audiência tipo
		if (!(audienciaDt.getId_AudienciaTipo().length() == 0)){
			SqlCampos+= "ID_AUDI_TIPO ";
			SqlValores+= "?";
			ps.adicionarLong(audienciaDt.getId_AudienciaTipo());            
		} else if (audienciaDt.getAudienciaTipoCodigo().length() > 0){
			SqlCampos+= "ID_AUDI_TIPO ";
			SqlValores += ", (SELECT ID_AUDI_TIPO FROM AUDI_TIPO WHERE AUDI_TIPO_CODIGO = ?)";
			ps.adicionarLong(audienciaDt.getAudienciaTipoCodigo());
		}        
		// Serventia
        if (!(audienciaDt.getId_Serventia().length() == 0)){
        	SqlCampos += ",ID_SERV ";
        	SqlValores+= ",?";
        	ps.adicionarLong(audienciaDt.getId_Serventia());
        }
        // Data agendada
        if (!(audienciaDt.getDataAgendada().length() == 0)){
        	SqlCampos += ",DATA_AGENDADA ";
        	SqlValores+= ",?";
        	ps.adicionarDateTime(audienciaDt.getDataAgendada());
        }
        // Data movimentação
        if (!(audienciaDt.getDataMovimentacao().length() == 0)){
        	SqlCampos += ",DATA_MOVI ";
        	SqlValores+= ",?";
        	ps.adicionarDateTime(audienciaDt.getDataMovimentacao());
        }
        // Data reservada
        if (!(audienciaDt.getReservada().length() == 0)){
        	SqlCampos += ",RESERVADA ";
        	SqlValores+= ",?";
        	ps.adicionarBoolean(audienciaDt.getReservada());
        }
        // Observações
        if(StringUtils.isNotEmpty(audienciaDt.getObservacoes())) {
        	SqlCampos += ",OBSERVACOES ";
        	SqlValores+= ",?";
        	ps.adicionarClob(audienciaDt.getObservacoes());
        }
        if(audienciaDt instanceof AudienciaSegundoGrauDt && ((AudienciaSegundoGrauDt) audienciaDt).isVirtual()) {
        	AudienciaSegundoGrauDt audienciaSegundoGrau = (AudienciaSegundoGrauDt) audienciaDt;
        	SqlCampos += ",VIRTUAL ";
        	SqlValores+= ",?";
        	ps.adicionarBoolean(audienciaSegundoGrau.isVirtual());
        }
        SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");
 		
        // Executar SQL            
        audienciaDt.setId(executarInsert(Sql, "ID_AUDI", ps));
                
    }    
    
    /**
     * Método que possui a sql que buscará a última audiência marcada de um determinado tipo e para um único processo com status igua a "A Realizar"
     * 
     * @author msapaula
     * @param audienciaTipoCodigo
     * 
     * Auteração:
     * Data: Márcio Gomes - 15/09/2010 
     */
    public AudienciaDt getUltimaAudienciaMarcadaAgendamentoAutomatico(String audienciaTipoCodigo, String id_Processo) throws Exception {
        String sql = "";
        AudienciaDt audienciaDtLivre = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
        sql = "SELECT ID_AUDI, ID_AUDI_TIPO, AUDI_TIPO_CODIGO, AUDI_TIPO, DATA_AGENDADA, DATA_MOVI_AUDI, RESERVADA, ID_AUDI_PROC, ID_AUDI_PROC_STATUS, ID_SERV_CARGO, SERV_CARGO, ID_PROC, ID_SERV, SERV FROM PROJUDI.VIEW_AUDI_COMPLETA ap";
		sql += " WHERE ap.ID_PROC = ? AND AUDI_TIPO_CODIGO = ? AND ap.DATA_MOVI_AUDI_PROC IS NULL ";
		ps.adicionarLong(id_Processo);
		ps.adicionarLong(audienciaTipoCodigo);
		sql += " AND ap.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql += " ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC, ap.ID_AUDI_PROC";
        try{	        	       	
        	
            rs1 = consultar(sql, ps);
            if (rs1.next()) {
                audienciaDtLivre = new AudienciaDt();
                audienciaDtLivre = prepararAudienciaLivreAgendamento(rs1);
            }
        
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return audienciaDtLivre;
    }

    /**
     * Método que possui a SQL que buscará as audiências livres, reservadas ou
     * não, de acordo com o tipo da audiência, menos o tipo "Sessão de 2º Grau",
     * e o cargo da serventia (ServentiaCargo) do usuário para o qual o processo
     * foi distribuído (Usuário = conciliador, no caso de audiência de
     * conciliação. Usuário = juiz no caso de audiência de instrução)
     * 
     * @param audienciaDtAgendar
     * @param posicaoPaginaAtual
     * @return List listaAudienciasLivres
     * @throws Exception
     */
    public List consultarAudienciasLivresAgendamentoManual(AudienciaDt audienciaDtAgendar, String id_Serventia, boolean cargoTipoCodigo, String posicaoPaginaAtual) throws Exception {
        String sqlComum = "";
        String sql = "";
        List listaAudienciaDtLivres = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        Date tempDate = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        sql = "SELECT ID_AUDI, ID_AUDI_TIPO, AUDI_TIPO_CODIGO, AUDI_TIPO, DATA_AGENDADA, DATA_MOVI_AUDI, RESERVADA, ID_AUDI_PROC, ID_AUDI_PROC_STATUS, ID_SERV_CARGO, SERV_CARGO, ID_PROC, ID_SERV, SERV ";
        sqlComum = " FROM PROJUDI.VIEW_PROX_AUDI_LIVRE_MANUAL";
        sqlComum += " WHERE AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(audienciaDtAgendar.getAudienciaTipoCodigo());
        sqlComum += " AND ID_SERV = ? ";
        ps.adicionarLong(id_Serventia);
        // Verificar se o cargo tipo foi informado
        if (cargoTipoCodigo == true) {
            sqlComum += " AND ID_CARGO_TIPO in ( ?,?,?,?,? ) ";
            ps.adicionarLong(CargoTipoDt.JUIZ_1_GRAU);
            ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL);
            ps.adicionarLong(CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL);
            ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);
            ps.adicionarLong(CargoTipoDt.CONCILIADOR_JUIZADO);
        }
        // Verificar se o prazo para agendamento da audiência foi informado
        if (Funcoes.StringToInt(audienciaDtAgendar.getPrazoAgendamentoAudiencia()) > 0) {
            Calendar calendario = Calendar.getInstance();
            calendario.add(Calendar.DAY_OF_MONTH, Funcoes.StringToInt(audienciaDtAgendar.getPrazoAgendamentoAudiencia()));
            tempDate = calendario.getTime();
            sqlComum += " AND DATA_AGENDADA >= ? ";
            ps.adicionarDate(tempDate);
        }
        // Verificar se a data incial de consulta foi informada
        if (audienciaDtAgendar.getDataInicialConsulta().length() > 0) {
            sqlComum += " AND DATA_AGENDADA >= ? ";
            ps.adicionarDateTime(audienciaDtAgendar.getDataInicialConsulta() + " 00:00:00");
        }
        // Verificar se a data final de consulta foi informada
        if (audienciaDtAgendar.getDataFinalConsulta().length() > 0) {
            sqlComum += " AND DATA_AGENDADA <= ? ";
            ps.adicionarDateTime(audienciaDtAgendar.getDataFinalConsulta() + " 23:59:59");
        }
        sql += sqlComum;
        // Order by
        sql += " ORDER BY DATA_AGENDADA ASC";        
        try{
            rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);
            while (rs1.next()) {
                // Criando objeto do tipo "AudienciaDt"
                AudienciaDt audienciaDt = new AudienciaDt();
                audienciaDt = prepararAudienciaLivreAgendamento(rs1);
                // LISTA RECEBE O(S) OBJETO(S) DO TIPO "AUDIENCIADT"
                // CONSULTADO(S)
                listaAudienciaDtLivres.add(audienciaDt);
            }     
            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs12.next()) listaAudienciaDtLivres.add(rs12.getLong("QUANTIDADE"));            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciaDtLivres;
    }
    
    /**
     * Método responsável em montar e retornar os campos das views VIEW_AUDI_COMPLETA e VIEW_AUDI_COMPLETA_PRIM_GRAU 
     * 
     * @return
     */
    private String obtenhaListaCamposViewAudiCompleta() {
    	String sql = ""; 
    	sql = "SELECT ID_AUDI, AUDI_TIPO, ID_AUDI_TIPO, ID_SERV, SERV, DATA_AGENDADA, DATA_MOVI_AUDI, RESERVADA, CODIGO_TEMP, ";
        sql += " AUDI_TIPO_CODIGO, ID_PROC, ID_AUDI_PROC, ID_SERV_CARGO, SERV_CARGO, NOME, ID_AUDI_PROC_STATUS, ";
        sql += " AUDI_PROC_STATUS_CODIGO, AUDI_PROC_STATUS, DATA_MOVI_AUDI_PROC, PROC_NUMERO_COMPLETO, ";
        sql += " CODIGO_TEMP_AUDI_PROC, ID_AUDI_PROC_STATUS_ANA, AUDI_PROC_STATUS_ANA, AUDI_PROC_STATUS_CODIGO_ANA, ID_ARQ_ATA, ";
        sql += " DATA_AUDI_ORIGINAL, ID_AUDI_PROC_ORIGEM, ID_ARQ_ATA_ADIAMENTO, ID_ARQ_ATA_INICIADO, ID_ARQ_FINALIZACAO, ";
        sql += " ID_SERV_CARGO_PRESIDENTE, SERV_CARGO_PRESIDENTE, USU_PRESIDENTE, NOME_PRESIDENTE, ID_SERV_PRESIDENTE, SERV_PRESIDENTE, ";
        sql += " ID_SERV_CARGO_MP, SERV_CARGO_MP, USU_MP, NOME_MP, ID_SERV_MP, SERV_MP, ";
        sql += " ID_SERV_CARGO_REDATOR, SERV_CARGO_REDATOR, USU_REDATOR, NOME_REDATOR, ID_SERV_REDATOR, SERV_REDATOR, PRIMEIRO_PROMOVENTE_RECORRENTE, PRIMEIRO_PROMOVIDO_RECORRIDO, ";
        sql += " ID_PROC_TIPO_AUDIENCIA, PROC_TIPO_AUDIENCIA, POLO_ATIVO, POLO_PASSIVO, ID_PEND_VOTO, ID_PEND_EMENTA, VIRTUAL, SESSAO_INICIADA, ID_PEND_VOTO_REDATOR, ID_PEND_EMENTA_REDATOR ";        
        return sql;
    }
    
    private String obtenhaListaCamposViewAudiCompletaSessaoVirtual() {
    	String sql = obtenhaListaCamposViewAudiCompleta(); 
    	sql += ", (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ";
        sql += "	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "; 
        sql += "	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ";
        sql += " 	WHERE AP1.ID_AUDI_PROC = vac.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "; 
        sql += " 	(SELECT 1 FROM RECURSO R "; 
        sql += "		INNER JOIN AUDI_PROC AP2 ON AP2.ID_PROC = R.ID_PROC "; 
        sql += "		WHERE AP2.ID_AUDI_PROC = vac.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO ";
        return sql;
    }
        
    private String obtenhaListaCamposViewAudiAdvogadoPromotor() {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT ID_AUDI, AUDI_TIPO, ID_AUDI_TIPO, ID_SERV, SERV, DATA_AGENDADA, DATA_MOVI_AUDI, RESERVADA, CODIGO_TEMP,");
		sql.append(" AUDI_TIPO_CODIGO, ID_PROC, ID_AUDI_PROC, ID_SERV_CARGO, SERV_CARGO, NOME, ID_AUDI_PROC_STATUS,");
		sql.append(" AUDI_PROC_STATUS_CODIGO, AUDI_PROC_STATUS, DATA_MOVI_AUDI_PROC, PROC_NUMERO_COMPLETO,");
		sql.append(" CODIGO_TEMP_AUDI_PROC, ID_ARQ_FINALIZACAO, PRIMEIRO_PROMOVENTE_RECORRENTE, PRIMEIRO_PROMOVIDO_RECORRIDO");
    	return sql.toString();
    }

    /**
     * Método que possui a sql responsável por consultar as audiências pendentes
     * requisitadas por serventuários. Audiências Pendentes do Tipo X =
     * (Id_AudiênciaTipo = Id do Tipo X) + (Id_Processo IS NOT NULL) + (Data da
     * Movimentação IS NULL) + (Id_ServentiaCargo = Id do Cargo da Serventia
     * selecionado pelo usuário OU Id_Serventia = Todos os cargos da serventia
     * na qual ele está logado, caso ele não tenha selecionado algum cargo).
     * Como a data de movimentação será null e existe um processo, então o
     * status da audiência é automaticamente 1 = "A Ser Realizada".
     * 14/08/2015 - Modificado para receber os parâmetros ordenacao e qtdRegistros.
     * @author hrrosa
     * @author Keila Sousa Silva
     * @param audienciaDtConsulta
     * @param posicaoPaginaAtual
     * @param impressaoRelatorio -
     *            variável que determina se está sendo realizada a impressão de
     *            um relatório. Se positivo, não há limitação de registros no
     *            retorno da consulta e a ordenação da consulta é diferenciada.
     * @return List listaAudienciasPendentes
     * @throws Exception
     */
    public List consultarAudienciasPendentes(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, Boolean impressaoRelatorio, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenação PROJUDI
    		ordenacao = " vac.DATA_AGENDADA ASC, vac.AUDI_TIPO ASC, vac.SERV_CARGO ASC, vac.PROC_NUMERO ASC ";
    		
        String sql = "";
        String sqlComum = "";
        List listaAudienciasPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();        
       
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        // Id do Processo ou processo físico
        sqlComum += " (vac.ID_PROC IS NOT NULL OR vac.CODIGO_TEMP_AUDI_PROC IS NOT NULL) ";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NULL";
        // Id do Cargo da Serventia ou Id da Serventia
        if (!audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase("")) {
        	sqlComum += " AND vac.ID_SERV_CARGO = ? ";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo());
        } else {
        	sqlComum += " AND vac.ID_SERV = ? ";
            ps.adicionarLong(usuarioDt.getId_Serventia());
        }
        if (audienciaDtConsulta.getDataInicialConsulta().length() > 0 && audienciaDtConsulta.getDataFinalConsulta().length() > 0) {
        	sqlComum += " AND vac.DATA_AGENDADA BETWEEN ? AND ? ";
            ps.adicionarDateTime(audienciaDtConsulta.getDataInicialConsulta() + " 00:00:00");
            ps.adicionarDateTime(audienciaDtConsulta.getDataFinalConsulta() + " 23:59:59");
        }
        
        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;

        if (impressaoRelatorio != null && !impressaoRelatorio) {
        	sql += " ORDER BY " + ordenacao;
        } else {
            sql += " ORDER BY vac.DATA_AGENDADA ASC, vac.AUDI_TIPO ASC ";
        }

        try{
        	if (impressaoRelatorio != null && !impressaoRelatorio){
        		rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
        	} else{
        		rs1 = consultar(sql, ps);
        	}            
            listaAudienciasPendentes = getListaAudienciaAgendadaCompleta(rs1, impressaoRelatorio);
            
            if (impressaoRelatorio != null && !impressaoRelatorio){
            	rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
                if (rs12.next()) listaAudienciasPendentes.add(rs12.getLong("QUANTIDADE"));            	
            } else {
            	listaAudienciasPendentes.add(listaAudienciasPendentes.size());
            }                        
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasPendentes;
    }

    /**
     * Método que possui a sql responsável por consultar as audiências pendentes
     * requisitadas por advogados. Audiências Pendentes do Tipo X =
     * (Id_AudiênciaTipo = Id do Tipo X) + (Id_Processo IS NOT NULL) + (Data da
     * Movimentação IS NULL) + (Usuário requisitante é advogado de uma das
     * partes do processo da audiência). Como a data de movimentação será null e
     * existe um processo, então o status da audiência é automaticamente 1 = "A
     * Ser Realizada"
     * @author hrrosa
     * @author Keila Sousa Silva
     * @param id_UsuarioServentia
     * @param audienciaDtConsulta
     * @param posicaoPaginaAtual
     * @return List listaAudienciasPendentes
     * @throws Exception
     */
    public List consultarAudienciasPendentesAdvogado(String id_UsuarioServentia, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenação do PROJUDI
    		ordenacao = " vac.DATA_AGENDADA ASC, vac.AUDI_TIPO ASC, vac.ID_PROC ASC ";
    		
        String sql = "";
        String sqlComum = "";
        List listaAudienciasPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();   
        
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        // Id do Processo ou processo físico
        sqlComum += " (vac.ID_PROC IS NOT NULL OR vac.CODIGO_TEMP_AUDI_PROC IS NOT NULL) ";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NULL";
        /*
         * Verificar se o advogado que requisitou a consulta é advogado de uma
         * das partes do processo da(s) audiência(s) encontrada(s)
         */
        sqlComum += " AND EXISTS (";
        sqlComum += " (SELECT 1 FROM";
        sqlComum += " (PROJUDI.PROC p";
        sqlComum += " INNER JOIN PROJUDI.PROC_PARTE pp ON p.ID_PROC = pp.ID_PROC";
        sqlComum += " INNER JOIN PROJUDI.PROC_PARTE_ADVOGADO ppa ON pp.ID_PROC_PARTE = ppa.ID_PROC_PARTE AND ppa.DATA_SAIDA IS NULL)";
        sqlComum += " WHERE vac.ID_PROC = p.ID_PROC AND ppa.ID_USU_SERV = ? ";							       ps.adicionarLong(id_UsuarioServentia);
        sqlComum += " ))";

        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        sql += " ORDER BY " + ordenacao;
        
        try{
            rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // as partes
            listaAudienciasPendentes = getListaAudienciaAgendadaCompleta(rs1, false);           

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs12.next()) listaAudienciasPendentes.add(rs12.getLong("QUANTIDADE"));
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasPendentes;
    }
    
    /**
     * Método que possui a sql responsável por consultar as audiências pendentes
     * requisitadas por usuários(conciliadores ou juizes recursais ou juizes
     * togados). Audiências Pendentes do Tipo X = (Id_AudiênciaTipo = Id do Tipo
     * X) + (Id_Processo IS NOT NULL) + (Data da Movimentação IS NULL) +
     * (Id_ServentiaCargo = Cargo do usuário requisitante). Como a data de
     * movimentação será null e existe um processo, então o status da audiência
     * é automaticamente 1 = "A Ser Realizada"
     * @author hrrosa
     * @since 28/09/2015
     * @author Keila Sousa Silva
     * @since 13/04/2009
     * @param usuarioNe
     * @param audienciaDt
     * @param posicaoPaginaAtual
     * @return List listaAudienciasPendentes
     * @throws Exception
     */
    public List consultarAudienciasPendentesUsuario(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
    		ordenacao = " vac.DATA_AGENDADA ASC, vac.AUDI_TIPO ASC, vac.ID_PROC ASC ";
    	
        String sql = "";
        String sqlComum = "";
        List listaAudienciasPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        // Id do Processo ou processo físico
        sqlComum += " (vac.ID_PROC IS NOT NULL OR vac.CODIGO_TEMP_AUDI_PROC IS NOT NULL) ";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NULL";
        // Id do Cargo da Serventia
        sqlComum += " AND vac.ID_SERV_CARGO = ? ";
        ps.adicionarLong(usuarioDt.getId_ServentiaCargo());

        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        sql += " ORDER BY " + ordenacao;
        try{
        	rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // as partes
            listaAudienciasPendentes = getListaAudienciaAgendadaCompleta(rs1, false);            

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs12.next()) listaAudienciasPendentes.add(rs12.getLong("QUANTIDADE"));
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasPendentes;
    }

    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Relator.
     * 
     * @param id_ServentiaCargo ,
     *            identificação do cargo do relator para filtro na pesquisa
     * @param id_Audiencia ,
     *            identificação de audiência no caso de retornar somente os
     *            processos a serem julgados em uma sessão
     * @param posicaoPaginaAtual ,
     *            parâmetro para paginação
     * 
     * @author msapaula
     * @throws Exception 
     */
    public List consultarSessoesPendentesRelatorDesembargador(String id_ServentiaCargo, String id_Audiencia, boolean somentePreAnalisadas) throws Exception{    	
    	return consultarSessoesPendentesRelatorDesembargador(id_ServentiaCargo, id_Audiencia, false, false, somentePreAnalisadas);    	
    }
    
    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Relator.
     * 
     * @param id_ServentiaCargo ,
     *            identificação do cargo do relator para filtro na pesquisa
     * @param id_Audiencia ,
     *            identificação de audiência no caso de retornar somente os
     *            processos a serem julgados em uma sessão
     * @param posicaoPaginaAtual ,
     *            parâmetro para paginação
     * 
     * @author msapaula
     * @throws Exception 
     */
    public List consultarSessoesPendentesRelatorJuizTurma(String id_ServentiaCargo, String id_Audiencia) throws Exception{    	
    	return consultarSessoesPendentesRelatorJuizTurma(id_ServentiaCargo, id_Audiencia, false, false);    	
    }

    /**
     * Consulta as Sessões de 2º Grau pendentes para um Advogado
     * 
     * @param id_UsuarioServentia
     * @param posicaoPaginaAtual
     * 
     * @author msapaula
     */
    public List consultarSessoesPendentesAdvogado(String id_UsuarioServentia) throws Exception {
		String sql = "";
        List listaSessoesPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        sql = "  	SELECT ac.ID_AUDI, ac.ID_AUDI_TIPO, ac.AUDI_TIPO, ac.AUDI_TIPO_CODIGO, ac.ID_SERV, ac.SERV,";
        sql += " ac.DATA_AGENDADA, ac.ID_AUDI_PROC, ac.ID_AUDI_PROC_STATUS , ac.DATA_MOVI_AUDI,";
        sql += " ac.AUDI_PROC_STATUS, ac.AUDI_PROC_STATUS_CODIGO, ac.ID_SERV_CARGO, ac.SERV_CARGO,";
        sql += " ac.NOME, ac.ID_PROC, ac.PROC_NUMERO_COMPLETO, ac.RESERVADA, ac.DATA_MOVI_AUDI_PROC,";
        sql += " ac.ID_RECURSO, ac.CODIGO_TEMP, ";
        sql += " ac.CODIGO_TEMP_AUDI_PROC, ac.ID_AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_CODIGO_ANA, ";
        sql += " ID_ARQ_ATA, DATA_AUDI_ORIGINAL, ID_ARQ_ATA_ADIAMENTO, ID_ARQ_ATA_INICIADO, ID_ARQ_FINALIZACAO, ";
        sql += " PRIMEIRO_PROMOVENTE_RECORRENTE, PRIMEIRO_PROMOVIDO_RECORRIDO, ac.ID_PROC_TIPO_AUDIENCIA, ac.PROC_TIPO_AUDIENCIA, ac.POLO_ATIVO, ac.POLO_PASSIVO, ac.ID_PEND_VOTO, ac.ID_PEND_EMENTA, ac.ID_PEND_VOTO_REDATOR, ac.ID_PEND_EMENTA_REDATOR ";
        sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA ac";
        sql += " WHERE ac.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
        sql += " AND ac.AUDI_PROC_STATUS_CODIGO = ? ";
        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
        sql += " AND ac.ID_PROC IS NOT NULL  AND ac.DATA_MOVI_AUDI_PROC IS NULL";
        sql += " AND EXISTS (";
        sql += " 	(SELECT p.ID_PROC FROM PROJUDI.PROC p";
        sql += " 	INNER JOIN PROJUDI.PROC_PARTE pp ON p.ID_PROC = pp.ID_PROC";
        sql += " 	INNER JOIN PROJUDI.PROC_PARTE_ADVOGADO ppa ON (pp.ID_PROC_PARTE = ppa.ID_PROC_PARTE AND ppa.DATA_SAIDA IS NULL)";
        sql += " 	WHERE ac.ID_PROC = p.ID_PROC AND ppa.ID_USU_SERV = ? ";
        ps.adicionarLong(id_UsuarioServentia);
        sql += " )";
        sql += ")";      
        sql += " ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC, DATA_AGENDADA ASC, ID_PROC ASC";
        try{
            rs1 = consultar(sql, ps);
            // Chama método que retorna a lista de audiências já com os processos e as partes
            listaSessoesPendentes = getListaAudienciaProcessoRecurso(rs1);

        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        return listaSessoesPendentes;
    }
    
    /**
     * Consulta as Sessões de 2º Grau pendentes para um Promotor
     * 
     * @param id_ServentiaCargo
     * @param posicaoPaginaAtual
     * 
     * @author msapaula
     */
    public List consultarSessoesPendentesPromotor(String id_ServentiaCargo) throws Exception {
        String sql = "";
        List listaSessoesPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        sql = "  	SELECT ac.ID_AUDI, ac.ID_AUDI_TIPO, ac.AUDI_TIPO, ac.AUDI_TIPO_CODIGO, ac.ID_SERV, ac.SERV,";
        sql += " ac.DATA_AGENDADA, ac.ID_AUDI_PROC, ac.ID_AUDI_PROC_STATUS , ac.DATA_MOVI_AUDI,";
        sql += " ac.AUDI_PROC_STATUS, ac.AUDI_PROC_STATUS_CODIGO, ac.ID_SERV_CARGO, ac.SERV_CARGO,";
        sql += " ac.NOME, ac.ID_PROC, ac.PROC_NUMERO_COMPLETO, ac.RESERVADA, ac.DATA_MOVI_AUDI_PROC,";
        sql += " ac.ID_RECURSO, ac.CODIGO_TEMP ";
        sql += ",ac.CODIGO_TEMP_AUDI_PROC, ac.ID_AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_CODIGO_ANA, ID_ARQ_ATA, DATA_AUDI_ORIGINAL, ID_ARQ_ATA_ADIAMENTO, ID_ARQ_ATA_INICIADO, ID_ARQ_FINALIZACAO, ";
        sql += " PRIMEIRO_PROMOVENTE_RECORRENTE, PRIMEIRO_PROMOVIDO_RECORRIDO, ac.ID_PROC_TIPO_AUDIENCIA, ac.PROC_TIPO_AUDIENCIA, ac.POLO_ATIVO, ac.POLO_PASSIVO, ac.ID_PEND_VOTO, ac.ID_PEND_EMENTA, ac.ID_PEND_VOTO_REDATOR, ac.ID_PEND_EMENTA_REDATOR ";
        sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA ac";
        sql += " WHERE ac.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
        sql += " AND ac.AUDI_PROC_STATUS_CODIGO = ? ";
        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
        sql += " AND ac.ID_PROC IS NOT NULL  AND ac.DATA_MOVI_AUDI_PROC IS NULL";
        sql += " AND ac.ID_PROC IN (";
        sql += " 	(SELECT p.ID_PROC FROM PROJUDI.PROC p";
        sql += " 	INNER JOIN PROJUDI.PROC_RESP pr ON p.ID_PROC = pr.ID_PROC";
        sql += " 	WHERE ac.ID_PROC = p.ID_PROC AND pr.ID_SERV_CARGO = ? ";
        ps.adicionarLong(id_ServentiaCargo);
        sql += " )";
        sql += ")";      
        sql += " ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC, DATA_AGENDADA ASC, ID_PROC ASC";
        try{
            rs1 = consultar(sql, ps);
            // Chama método que retorna a lista de audiências já com os processos e as partes
            listaSessoesPendentes = getListaAudienciaProcessoRecurso(rs1);

        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        return listaSessoesPendentes;
    }
    
    /**
     * Método que possui a sql responsável por consultar as audiências pendentes para um promotor
     * 
     * @param id_ServentiaCargo, cargo do promotor
     * @param audienciaDtConsulta, objeto com dados para consulta
     * @param posicaoPaginaAtual, parâmetro para paginação
     * @author msapaula
     */
    public List consultarAudienciasPendentesPromotor(String id_ServentiaCargo, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
    	
    	String sql = "";
        String sqlComum = "";
        String sqlOrderBy = "";
        List listaAudienciasPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        sql = " SELECT * FROM ( ";
        
        sqlComum = " SELECT DISTINCT ID_AUDI, AUDI_TIPO, ID_AUDI_TIPO, ID_SERV, SERV, DATA_AGENDADA, DATA_MOVI_AUDI, RESERVADA, CODIGO_TEMP, ";
        sqlComum +="   AUDI_TIPO_CODIGO, ID_PROC, ID_AUDI_PROC, ID_SERV_CARGO, SERV_CARGO, NOME, ID_AUDI_PROC_STATUS, ";
        sqlComum +="   AUDI_PROC_STATUS_CODIGO, AUDI_PROC_STATUS, DATA_MOVI_AUDI_PROC, PROC_NUMERO_COMPLETO, ";
        sqlComum +="   CODIGO_TEMP_AUDI_PROC, ID_ARQ_FINALIZACAO, PRIMEIRO_PROMOVENTE_RECORRENTE, PRIMEIRO_PROMOVIDO_RECORRIDO ";
        sqlComum +=" FROM PROJUDI.VIEW_AUDI_COMPLETA_PROMOTOR vw WHERE";
        
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vw.AUDI_TIPO_CODIGO = ?  AND ";
        	ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        
        sqlComum += " (vw.ID_PROC IS NOT NULL OR vw.CODIGO_TEMP_AUDI_PROC IS NOT NULL) ";
        
        sqlComum += " AND vw.DATA_MOVI_AUDI_PROC IS NULL";
       
        sqlComum += " AND vw.ID_SERV_CARGO_RESP = ? ";
		ps.adicionarLong(id_ServentiaCargo);
      
        sqlOrderBy += ") ORDER BY DATA_AGENDADA ASC, SERV_CARGO ASC, PROC_NUMERO_COMPLETO ASC, AUDI_PROC_STATUS ASC ";
        
        try{
            rs1 = consultarPaginacao(sql+sqlComum+sqlOrderBy, ps, posicaoPaginaAtual, qtdRegistros);
            // Chama método que retorna a lista de audiências já com os processos e as partes
            listaAudienciasPendentes = getListaAudienciaAgendadaAdvogado(rs1, false);  
            
            rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE FROM ( "+ sqlComum+" ) ", ps);
            
            if (rs2.next()){ 
            	listaAudienciasPendentes.add(rs2.getLong("QUANTIDADE"));
            }
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e) {}
        }
        return listaAudienciasPendentes;
    }

    /**
     * Método quwe possui a sql responsável por consultar as audiências
     * agendadas para a data corrente e requisitadas por serventuários.
     * Audiências Para Hoje do Tipo X = (Id_AudiênciaTipo = Id do Tipo X) +
     * (Id_Processo IS NOT NULL) + (Data da Movimentação IS NULL) +
     * (Id_ServentiaCargo = Id do Cargo da Serventia selecionado pelo usuário OU
     * Id_Serventia = Todos os cargos da serventia na qual ele está logado, caso
     * ele não tenha selecionado algum cargo) + (Data da Audiência é data
     * corrente). Como a data de movimentação será null e existe um processo
     * então o status da audiência é automaticamente 1 = a ser realizada
     * @author hrrosa
     * @author Keila Sousa Silva
     * @param audienciaDt
     * @param posicaoPaginaAtual
     * @param impressaoRelatorio -
     *            variável que determina se está sendo realizada a impressão de
     *            um relatório. Se positivo, não há limitação de registros no
     *            retorno da consulta.
     * @return List listaAudienciasParaHoje
     * @throws Exception
     */
   public List consultarAudienciasParaHoje(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, Boolean impressaoRelatorio, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao do PROJUDI
    		ordenacao = " vac.AUDI_TIPO ASC, vac.DATA_AGENDADA ASC, vac.SERV_CARGO ASC, vac.ID_PROC ASC";
    		
        String sql = "";
        String sqlComum = "";
        List listaAudienciasParaHoje = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Date dataAtual = new Date();
        String dataInicial = Funcoes.dateToStringSoData(dataAtual) + " 00:00:00";
        String dataFinal = Funcoes.dateToStringSoData(dataAtual) + " 23:59:59";  
        
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        // Id do Processo ou processo físico
        sqlComum += " (vac.ID_PROC IS NOT NULL OR vac.CODIGO_TEMP_AUDI_PROC IS NOT NULL)";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NULL";
        // Data da Agenda
        sqlComum += " AND vac.DATA_AGENDADA >= ? AND vac.DATA_AGENDADA <= ? ";
        ps.adicionarDateTime(dataInicial);
        ps.adicionarDateTime(dataFinal);
        // Id do Cargo da Serventia ou Id da Serventia
        if (!audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase("")) {
        	sqlComum += " AND vac.ID_SERV_CARGO = ? ";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo());
        } else {
        	sqlComum += " AND vac.ID_SERV = ? ";
            ps.adicionarLong(usuarioDt.getId_Serventia());
        }

        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;        
        if (impressaoRelatorio != null && !impressaoRelatorio) {
            sql += " ORDER BY " + ordenacao;
        } else {
            sql += " ORDER BY " + ordenacao;
        }
        try{
        	if (impressaoRelatorio != null && !impressaoRelatorio) {
        		rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
        	} else {
        		rs1 = consultar(sql, ps);
        	}            
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // as partes
            listaAudienciasParaHoje = getListaAudienciaAgendadaCompleta(rs1, impressaoRelatorio);

            if (impressaoRelatorio != null && !impressaoRelatorio) {
            	rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
                if (rs12.next()) listaAudienciasParaHoje.add(rs12.getLong("QUANTIDADE"));
            } else {
            	listaAudienciasParaHoje.add(listaAudienciasParaHoje.size());
            }            
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasParaHoje;
    }

    /**
     * Método quwe possui a sql responsável por consultar as audiências
     * agendadas para a data corrente e requisitadas por advogados. Os advogados
     * poderão ver somentes as audiências de processos nos quais são advogados
     * de uma das partes. Audiências Para Hoje do Tipo X = (Id_AudiênciaTipo =
     * Id do Tipo X) + (Id_Processo IS NOT NULL) + (Data da Movimentação IS
     * NULL) + (Data da Audiência é data corrente). Como a data de movimentação
     * será null e existe um processo então o status da audiência é
     * automaticamente 1 = a ser realizada
     * 
     * @author Keila Sousa Silva
     * @since 14/04/2009
     * @author hrrosa
     * @since 28/09/2015
     * @param usuarioNe
     * @param audienciaDtConsulta
     * @param posicaoPaginaAtual
     * @return List listaAudienciasParaHoje
     * @throws Exception
     */
    public List consultarAudienciasParaHojeAdvogado(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao do PROJUDI
    		ordenacao = " vac.AUDI_TIPO ASC, vac.DATA_AGENDADA ASC, vac.SERV_CARGO ASC, vac.ID_PROC ASC ";
    	
        String sql = "";
        String sqlComum = "";
        List listaAudienciasParaHoje = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Date dataAtual = new Date();
        String dataInicial = Funcoes.dateToStringSoData(dataAtual) + " 00:00:00";
        String dataFinal = Funcoes.dateToStringSoData(dataAtual) + " 23:59:59";      
        
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        sqlComum += " vac.ID_PROC IS NOT NULL";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NULL";
        // Data do Agendamento
        sqlComum += " AND vac.DATA_AGENDADA >= ? AND vac.DATA_AGENDADA < ? ";
        ps.adicionarDateTime(dataInicial);
        ps.adicionarDateTime(dataFinal);
        // Processo
        sqlComum += " AND EXISTS (";
        sqlComum += " (SELECT p.ID_PROC FROM";
        sqlComum += " (PROJUDI.PROC p";
        sqlComum += " INNER JOIN PROJUDI.PROC_PARTE pp ON p.ID_PROC = pp.ID_PROC";
        sqlComum += " INNER JOIN PROJUDI.PROC_PARTE_ADVOGADO ppa ON pp.ID_PROC_PARTE = ppa.ID_PROC_PARTE)";
        sqlComum += " WHERE vac.ID_PROC = p.ID_PROC AND ppa.ID_USU_SERV = ? ";
        ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
        sqlComum += " ))";
        
        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        // Order by, limit, offset
        sql += " ORDER BY " + ordenacao;
        
        try{
        	rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // as partes
            listaAudienciasParaHoje = getListaAudienciaAgendadaCompleta(rs1, false);           

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs12.next()) listaAudienciasParaHoje.add(rs12.getLong("QUANTIDADE"));
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasParaHoje;
    }
    
    public List consultarAudienciasSegundoGrauParaHojeAdvogado(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual) throws Exception {
        String sql = "";
        String sqlComum = "";
        List listaAudienciasParaHoje = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Date dataAtual = new Date();
        String dataInicial = Funcoes.dateToStringSoData(dataAtual) + " 00:00:00";
        String dataFinal = Funcoes.dateToStringSoData(dataAtual) + " 23:59:59";      
        
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        sqlComum += " vac.ID_PROC IS NOT NULL";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NULL";
        // Data do Agendamento
        sqlComum += " AND vac.DATA_AGENDADA >= ? AND vac.DATA_AGENDADA < ? ";
        ps.adicionarDateTime(dataInicial);
        ps.adicionarDateTime(dataFinal);
        // Processo
        sqlComum += " AND EXISTS (";
        sqlComum += " (SELECT p.ID_PROC FROM";
        sqlComum += " (PROJUDI.PROC p";
        sqlComum += " INNER JOIN PROJUDI.PROC_PARTE pp ON p.ID_PROC = pp.ID_PROC";
        sqlComum += " INNER JOIN PROJUDI.PROC_PARTE_ADVOGADO ppa ON pp.ID_PROC_PARTE = ppa.ID_PROC_PARTE)";
        sqlComum += " WHERE vac.ID_PROC = p.ID_PROC AND ppa.ID_USU_SERV = ? ";
        ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
        sqlComum += " ))";
        
        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        // Order by, limit, offset
        sql += " ORDER BY vac.AUDI_TIPO ASC, vac.DATA_AGENDADA ASC, vac.SERV_CARGO ASC, vac.ID_PROC ASC";
        
        try{
            rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);
            // Chama método que retorna a lista de audiências já com os
            // processos e as partes
            listaAudienciasParaHoje = getListaAudienciaAgendadaCompleta(rs1, false);           

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs12.next()) listaAudienciasParaHoje.add(rs12.getLong("QUANTIDADE"));
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasParaHoje;
    }

    /**
     * Método quwe possui a sql responsável por consultar as audiências
     * agendadas para a data corrente e requisitadas por usuários(conciliador ou
     * juizes recursais ou juizes togados). Audiências Para Hoje do Tipo X =
     * (Id_AudiênciaTipo = Id do Tipo X) + (Id_Processo IS NOT NULL) + (Data da
     * Movimentação IS NULL) + (Id_ServentiaCargo = Id do Cargo do usuário) +
     * (Data da Audiência é data corrente). Como a data de movimentação será
     * null e existe um processo então o status da audiência é automaticamente 1 =
     * a ser realizada
     * 
     * @author Keila Sousa Silva
     * @since 14/04/2009
     * @author hrrosa
     * @since 28/09/2015
     * @param usuarioNe
     * @param audienciaDtConsulta
     * @param posicaoPaginaAtual
     * @return List listaAudienciasParaHoje
     * @throws Exception
     */
   public List consultarAudienciasParaHojeUsuario(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao do PROJUDI
    		ordenacao = " vac.AUDI_TIPO ASC, vac.DATA_AGENDADA ASC, vac.SERV_CARGO ASC, vac.ID_PROC ASC";
    	
        String sql = "";
        String sqlComum = "";
        List listaAudienciasParaHoje = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Date dataAtual = new Date();
        String dataInicial = Funcoes.dateToStringSoData(dataAtual) + " 00:00:00";
        String dataFinal = Funcoes.dateToStringSoData(dataAtual) + " 23:59:59";

        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        // Id do Processo ou processo físico
        sqlComum += " (vac.ID_PROC IS NOT NULL OR vac.CODIGO_TEMP_AUDI_PROC IS NOT NULL)";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NULL";
        // Data da Agenda
        sqlComum += " AND vac.DATA_AGENDADA >= ? AND vac.DATA_AGENDADA < ? ";
        ps.adicionarDateTime(dataInicial);
        ps.adicionarDateTime(dataFinal);
        // Id do Cargo da Serventia
        sqlComum += " AND vac.ID_SERV_CARGO = ? ";
        ps.adicionarLong(usuarioDt.getId_ServentiaCargo());
        
        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        // Order by, limit, offset
        sql += " ORDER BY " + ordenacao;
        
        try{
        	 rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // as partes
            listaAudienciasParaHoje = getListaAudienciaAgendadaCompleta(rs1, false);
            
            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs12.next()) listaAudienciasParaHoje.add(rs12.getLong("QUANTIDADE"));
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasParaHoje;
    }

    /**
     * Método que contém a SQL responsável por consultar a quantidade das
     * audiências para hoje de um dado cargo da serventia e dos seguintes tipos:
     * conciliação, instrução, preliminar e una.
     * 
     * Audiências Para Hoje = Audiências pendentes, ou seja, audiências cujo
     * status é "A Ser Realizada", cujo id do processo is not null e cuja data
     * de agendamento é a data corrente.
     * 
     * @author Keila Sousa Silva
     * @since 21/08/2009
     * @param id_ServentiaCargo
     * @return List listaTipoAudienciaQuantidadeAudienciasParaHoje = lista
     *         contendo um array de string que possuirá os seguintes valores, de
     *         acordo com a ordem a seguir: "AUDI_TIPO_CODIGO",
     *         "AUDI_TIPO" e "QUANTIDADE" (Quantidade de audiências
     *         pendentes para hoje)
     * @throws Exception
     */
    public List consultarQuantidadeAudienciasParaHoje(String id_ServentiaCargo) throws Exception {
        List listaTipoAudienciaQuantidadeAudienciasParaHoje = new ArrayList();
        String sql = "";
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Date dataAtual = new Date();
        String dataInicial = Funcoes.dateToStringSoData(dataAtual) + " 00:00:00";
        String dataFinal = Funcoes.dateToStringSoData(dataAtual) + " 23:59:59";

        sql = "SELECT COUNT(1) AS Quantidade, at.AUDI_TIPO_CODIGO, at.AUDI_TIPO FROM PROJUDI.AUDI a";
        // Audiência Tipo
        sql += " INNER JOIN PROJUDI.AUDI_TIPO at";
        sql += " ON (a.ID_AUDI_TIPO = at.ID_AUDI_TIPO AND at.AUDI_TIPO_CODIGO <> ?)";
        ps.adicionarLong(AudienciaTipoDt.Codigo.EXECUCAO.getCodigo());
        // Data de Agendamento
        sql += " INNER JOIN PROJUDI.AUDI_PROC ap";
        sql += " ON (ap.ID_AUDI = a.ID_AUDI AND a.DATA_AGENDADA >= ? AND a.DATA_AGENDADA <= ?)";
        ps.adicionarDateTime(dataInicial);
        ps.adicionarDateTime(dataFinal);
        // Audiência Processo Status
        sql += " INNER JOIN PROJUDI.AUDI_PROC_STATUS aps";
        sql += " ON (aps.AUDI_PROC_STATUS_CODIGO = ? AND ap.ID_AUDI_PROC_STATUS = aps.ID_AUDI_PROC_STATUS)";
        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
        // Serventia Cargo
        sql += " WHERE ap.ID_SERV_CARGO = ? ";
        ps.adicionarLong(id_ServentiaCargo);
        // Group by
        sql += " GROUP BY at.AUDI_TIPO_CODIGO, at.AUDI_TIPO";

        try{
            rs1 = consultar(sql, ps);
            while (rs1.next()) {
                listaTipoAudienciaQuantidadeAudienciasParaHoje.add(new String[] {rs1.getString("AUDI_TIPO_CODIGO"), rs1.getString("AUDI_TIPO"), rs1.getString("QUANTIDADE") });
            }

        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return listaTipoAudienciaQuantidadeAudienciasParaHoje;
    }

    /**
     * Método que contém a SQL responsável por consultar a quantidade das
     * audiências para hoje de advogados e dos seguintes tipos: conciliação,
     * instrução, preliminar, sessões de 2º grau e una.
     * 
     * Audiências Para Hoje Advogado = Audiências pendentes, ou seja, audiências
     * cujo status é "A Ser Realizada", cujo id do processo is not null, cuja
     * data de agendamento é a data corrente e cujo processo possui o advogado
     * em questão como advogado de uma das partes deste processo.
     * 
     * @author Keila Sousa Silva
     * @since 27/08/2009
     * @param usuarioDt
     * @return List listaTipoAudienciaQuantidadeAudienciasParaHojeAdvogado =
     *         lista contendo um array de string que possuirá os seguintes
     *         valores, de acordo com a ordem a seguir: "AUDI_TIPO_CODIGO",
     *         "AUDI_TIPO" e "QUANTIDADE" (Quantidade de audiências
     *         pendentes para hoje)
     * @throws Exception
     */
    public List consultarQuantidadeAudienciasParaHojeAdvogado(UsuarioDt usuarioDt) throws Exception {
        List listaTipoAudienciaQuantidadeAudienciasParaHojeAdvogado = new ArrayList();
        String sql = "";
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Date dataAtual = new Date();
        String dataInicial = Funcoes.dateToStringSoData(dataAtual) + " 00:00:00";
        String dataFinal = Funcoes.dateToStringSoData(dataAtual) + " 23:59:59";


        sql =  " SELECT at.AUDI_TIPO_CODIGO, at.AUDI_TIPO, COUNT(DISTINCT(ap.ID_AUDI_PROC)) AS QUANTIDADE"; 
    	sql += " FROM PROJUDI.PROC_PARTE_ADVOGADO ppa ";
        sql += " INNER JOIN PROJUDI.PROC_PARTE pp on ppa.ID_PROC_PARTE=pp.ID_PROC_PARTE ";
		sql += " INNER JOIN PROJUDI.AUDI_PROC ap on pp.ID_PROC= ap.ID_PROC ";
		sql += " INNER JOIN PROJUDI.AUDI a on ap.ID_AUDI = a.ID_AUDI ";
		sql += " INNER JOIN PROJUDI.AUDI_TIPO at on a.ID_AUDI_TIPO = at.ID_AUDI_TIPO ";
		sql += " WHERE ppa.ID_USU_SERV = ? ";
		ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
		sql += " AND ap.DATA_MOVI IS NULL  ";
		
        sql += " AND a.DATA_AGENDADA >= ? ";
        ps.adicionarDateTime(dataInicial);
        sql += " AND a.DATA_AGENDADA < ? ";
        ps.adicionarDateTime(dataFinal);
        sql += " group by at.AUDI_TIPO_CODIGO, at.AUDI_TIPO";

        try{
            rs1 = consultar(sql, ps);
            while (rs1.next()) {
                listaTipoAudienciaQuantidadeAudienciasParaHojeAdvogado.add(new String[] {rs1.getString("AUDI_TIPO_CODIGO"), rs1.getString("AUDI_TIPO"), rs1.getString("QUANTIDADE") });
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return listaTipoAudienciaQuantidadeAudienciasParaHojeAdvogado;
    }

    /**
     * Método responsável por consultar audiências que foram movimentadas na
     * data corrente e requisitadas por serventuários. Audiência Movimentada
     * Hoje do Tipo X = (Id_AudienciaTipo = Id do Tipo X) + (Id_ServentiaCargo =
     * Id do Cargo da Serventia selecionado pelo usuário OU Id_Serventia = Todos
     * os cargos da serventia na qual ele está "logado", caso ele não tenha
     * selecionado algum cargo) + (Data da Movimentação é data corrente). Como a
     * data de movimentação é diferente de null, então o status da audiência é
     * automaticamente diferente de '1 = a ser realizada'
     * 
     * @author Keila Sousa Silva
     * @author hrrosa
     * @param audienciaDt
     * @param posicaoPaginaAtual
     * @param impressaoRelatorio -
     *            variável que determina se está sendo realizada a impressão de
     *            um relatório. Se positivo, não há limitação de registros no
     *            retorno da consulta e a ordenação é diferenciada.
     * @return List listaAudienciasMovHoje
     * @throws Exception
     */
    public List consultarAudienciasMovimentadasHoje(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, Boolean impressaoRelatorio, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
    		ordenacao = " vac.AUDI_TIPO ASC, vac.DATA_AGENDADA ASC, vac.SERV_CARGO ASC, vac.ID_PROC ASC ";
    	
        String sql = "";
        String sqlComum = "";
        List listaAudienciasMovHoje = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Date dataAtual = new Date();
        String dataInicial = Funcoes.dateToStringSoData(dataAtual) + " 00:00:00";
        String dataFinal = Funcoes.dateToStringSoData(dataAtual) + " 23:59:59";
        
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        // Id do Processo ou processo físico
        sqlComum += " (vac.ID_PROC IS NOT NULL OR vac.CODIGO_TEMP_AUDI_PROC IS NOT NULL)";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NOT NULL";
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC >= ? AND vac.DATA_MOVI_AUDI_PROC < ? ";
        ps.adicionarDateTime(dataInicial);
        ps.adicionarDateTime(dataFinal);
        // Id do Cargo da Serventia ou Id da Serventia
        if (!audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase("")) {
        	sqlComum += " AND vac.ID_SERV_CARGO = ? ";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo());
        } else {
        	sqlComum += " AND vac.ID_SERV = ? ";
            ps.adicionarLong(usuarioDt.getId_Serventia());
        }
        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        // Impressão de Relatório
        if (impressaoRelatorio != null && !impressaoRelatorio) {
        	sql += " ORDER BY " + ordenacao;
        } else {
            sql += " ORDER BY vac.AUDI_TIPO ASC, vac.DATA_AGENDADA ASC ";
        }
        try{
        	 if (impressaoRelatorio != null && !impressaoRelatorio) {
        		 rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros); // Modificado para receber qtdRegistros.
        	 } else {
        		 rs1 = consultar(sql, ps);
        	 }            
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // as partes
            listaAudienciasMovHoje = getListaAudienciaAgendadaCompleta(rs1, impressaoRelatorio);
            
			if (impressaoRelatorio != null && !impressaoRelatorio) {	
	            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
	            if (rs12.next()) listaAudienciasMovHoje.add(rs12.getLong("QUANTIDADE")); 
			} else {
				listaAudienciasMovHoje.add(listaAudienciasMovHoje.size());
			}
			
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasMovHoje;
    }

    /**
     * Método responsável por consultar audiências que foram movimentadas na
     * data corrente e requisitadas por advogados. O advogado requisitante só
     * poderá visualizar audiências de processos no qual é advogado de alguma
     * das partes. Audiência Movimentada Hoje do Tipo X = (Id_AudienciaTipo = Id
     * do Tipo X) + (Id_ServentiaCargo = Id do Cargo da Serventia selecionado
     * pelo usuário ou todos os cargos caso ele não selecione nenhum) + (Data da
     * Movimentação é data corrente). Como a data de movimentação é diferente de
     * null, então o status da audiência é automaticamente diferente de '1 = a
     * ser realizada'
     * 
     * @author Keila Sousa Silva
     * @since 14/04/2009
     * @author hrrosa
     * @since 28/09/2015
     * @param usuarioNe
     * @param audienciaDtConsulta
     * @param posicaoPaginaAtual
     * @return List listaAudienciasMovHoje
     * @throws Exception
     */
    public List consultarAudienciasMovimentadasHojeAdvogado(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenação PROJUDI
    		ordenacao = " vac.AUDI_TIPO ASC, vac.DATA_AGENDADA ASC, vac.SERV_CARGO ASC, vac.ID_PROC ASC ";
    	
        String sql = "";
        String sqlComum = "";
        List listaAudienciasMovHoje = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Date dataAtual = new Date();
        String dataInicial = Funcoes.dateToStringSoData(dataAtual) + " 00:00:00";
        String dataFinal = Funcoes.dateToStringSoData(dataAtual) + " 23:59:59";
       
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        // Id do Processo ou processo físico
        sqlComum += " (vac.ID_PROC IS NOT NULL OR vac.CODIGO_TEMP_AUDI_PROC IS NOT NULL)";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NOT NULL";
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC >= ? AND vac.DATA_MOVI_AUDI_PROC < ? ";
        ps.adicionarDateTime(dataInicial);
        ps.adicionarDateTime(dataFinal);
        /*
         * Verificar se o advogado que requisitou a consulta é advogado de uma
         * das partes do processo da(s) audiência(s) encontrada(s)
         */
        sqlComum += " AND EXISTS (";
        sqlComum += " (SELECT p.ID_PROC FROM";
        sqlComum += " (PROJUDI.PROC p";
        sqlComum += " INNER JOIN PROJUDI.PROC_PARTE pp ON p.ID_PROC = pp.ID_PROC";
        sqlComum += " INNER JOIN PROJUDI.PROC_PARTE_ADVOGADO ppa ON pp.ID_PROC_PARTE = ppa.ID_PROC_PARTE)";
        sqlComum += " WHERE vac.ID_PROC = p.ID_PROC AND ppa.ID_USU_SERV = ? ";
        ps.adicionarLong(usuarioDt.getId_UsuarioServentia());
        sqlComum += " ))";

        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        sql += " ORDER BY " + ordenacao;
        try{
        	rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
            // Chama método que retorna a lista de audiências já com osprocessos
            // e
            // as partes
            listaAudienciasMovHoje = getListaAudienciaAgendadaCompleta(rs1, false);           

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs12.next()) listaAudienciasMovHoje.add(rs12.getLong("QUANTIDADE"));            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasMovHoje;
    }

    /**
     * Método responsável por consultar audiências que foram movimentadas na
     * data corrente e requisitadas por usuários (conciliadores ou juizes
     * recursais ou juizes togados). Audiência Movimentada Hoje do Tipo X =
     * (Id_AudienciaTipo = Id do Tipo X) + (Id_ServentiaCargo = Id do cargo do
     * usuário) + (Data da Movimentação é data corrente). Como a data de
     * movimentação é diferente de null, então o status da audiência é
     * automaticamente diferente de '1 = a ser realizada'
     * 
     * @author Keila Sousa Silva
     * @since 14/04/2009
     * @author hrrosa
     * @since 24/09/2015
     * @param usuarioNe
     * @param audienciaDtConsulta
     * @param posicaoPaginaAtual
     * @return List listaAudienciasMovHoje
     * @throws Exception
     */
    public List consultarAudienciasMovimentadasHojeUsuario(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
    		ordenacao = " vac.AUDI_TIPO ASC, vac.DATA_AGENDADA ASC, vac.SERV_CARGO ASC, vac.ID_PROC ASC ";
    	
        String sql = "";
        String sqlComum = "";
        List listaAudienciasMovHoje = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        Date dataAtual = new Date();
        String dataInicial = Funcoes.dateToStringSoData(dataAtual) + " 00:00:00";
        String dataFinal = Funcoes.dateToStringSoData(dataAtual) + " 23:59:59";
        
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }
        // Id do Processo ou processo físico
        sqlComum += " (vac.ID_PROC IS NOT NULL OR vac.CODIGO_TEMP_AUDI_PROC IS NOT NULL)";
        // Data da Movimentação
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC IS NOT NULL";
        sqlComum += " AND vac.DATA_MOVI_AUDI_PROC >= ? AND vac.DATA_MOVI_AUDI_PROC < ? ";
        ps.adicionarDateTime(dataInicial);
        ps.adicionarDateTime(dataFinal);
        // Id do Cargo da Serventia ou Id da Serventia
        sqlComum += " AND vac.ID_SERV_CARGO = ? ";
        ps.adicionarLong(usuarioDt.getId_ServentiaCargo());

        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        sql += " ORDER BY " + ordenacao;
        
        try{
        	rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // as partes
            listaAudienciasMovHoje = getListaAudienciaAgendadaCompleta(rs1, false);           

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs12.next()) listaAudienciasMovHoje.add(rs12.getLong("QUANTIDADE"));
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasMovHoje;
    }

    /**
     * Método responsável por consultar audiências de acordo com os parâmetros
     * informados pelo serventuário na consulta por filtros. Filtros: número do
     * processo, cargo da serventia, status da audiência, data inicial
     * (audiências com data de agendamento >= à data inicial) e data final
     * (audiências com data de agendamento <= à data final)
     * 
     * @author Keila Sousa Silva
     * @author hrrosa
     * @param id_ServentiaAudiencia
     * @param id_ServentiaProcesso
     * @param audienciaDtConsulta
     * @param numeroProcesso
     * @param digitoVerificador
     * @return List listaAudienciasComFiltro
     * @throws Exception
     */
    public List consultarAudienciasFiltro(String id_ServentiaAudiencia, String id_ServentiaProcesso, AudienciaDt audienciaDtConsulta, String numeroProcesso, String digitoVerificador, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception, MensagemException{
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
    		ordenacao = " vac.DATA_AGENDADA ASC, vac.AUDI_TIPO ASC, vac.SERV_CARGO ASC, vac.PROC_NUMERO ASC, vac.AUDI_PROC_STATUS ASC ";
    		
    	String sql = "";
        String sqlComum = "";
        List listaAudienciasComFiltro = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";

        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && !(audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ?  AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }

        // Número do Processo
        if (numeroProcesso != null && !numeroProcesso.equalsIgnoreCase("")) {
        	sqlComum += " vac.PROC_NUMERO = ? AND";
            ps.adicionarLong(numeroProcesso);

            // Dígito Verificador do Processo
            if ((digitoVerificador != null) && !(digitoVerificador.equalsIgnoreCase(""))){
            	sqlComum += " vac.DIGITO_VERIFICADOR = ? AND";
            	ps.adicionarLong(digitoVerificador);
            }
        }

        // Serventia Cargo
        if (!(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase("")) && !(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase(null))) {
        	sqlComum += " vac.ID_SERV_CARGO = ? ";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo());
        } else {
        	sqlComum += " vac.ID_SERV = ? ";
            ps.adicionarLong(id_ServentiaAudiencia);
        }
        
        if (id_ServentiaProcesso != null && id_ServentiaProcesso.trim().length() > 0) {
        	sqlComum += " AND ( ";
        	sqlComum += "       EXISTS (SELECT 1 ";
        	sqlComum += "                 FROM PROJUDI.PROC P";
        	sqlComum += "                WHERE P.ID_PROC = vac.ID_PROC";
        	sqlComum += "                 AND P.ID_SERV = ?)"; 
        	ps.adicionarLong(id_ServentiaProcesso);
        	sqlComum += " OR EXISTS (SELECT 1 ";
        	sqlComum += "              FROM PROJUDI.AUDI_PROC_FISICO APF ";
        	sqlComum += "             WHERE APF.ID_AUDI_PROC = vac.ID_AUDI_PROC";
        	sqlComum += "              AND APF.SERVENTIA_CODIGO = (SELECT SERV_CODIGO ";
        	sqlComum += "                                            FROM PROJUDI.SERV";
        	sqlComum += "                                           WHERE ID_SERV = ?)";
        	sqlComum += "            )";
        	sqlComum += " )";
        	ps.adicionarLong(id_ServentiaProcesso);
        }
        
        // Audiência Status do Processo
        if ((!(audienciaDtConsulta.getAudienciaProcessoDt().getId_AudienciaProcessoStatus().length() == 0)) && (audienciaDtConsulta.getAudienciaProcessoDt().getAudienciaProcessoStatusCodigo() != String.valueOf(AudienciaProcessoStatusDt.LIVRE))) {
        	sqlComum += " AND vac.ID_AUDI_PROC_STATUS = ? ";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_AudienciaProcessoStatus());
        } else {
        	sqlComum += " AND vac.ID_AUDI_PROC_STATUS <> ? ";
            ps.adicionarLong(AudienciaProcessoStatusDt.LIVRE);
        }
        
        // Data Agendada
        if (audienciaDtConsulta.getDataInicialConsulta().length() > 0 && audienciaDtConsulta.getDataFinalConsulta().length() > 0) {
        	sqlComum += " AND vac.DATA_AGENDADA BETWEEN ? AND ?";
            ps.adicionarDateTime(audienciaDtConsulta.getDataInicialConsulta() + " 00:00:00");
            ps.adicionarDateTime(audienciaDtConsulta.getDataFinalConsulta() + " 23:59:59");
        }
        
        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        sql += " ORDER BY " + ordenacao;
        try{
        	rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // partes setados
            listaAudienciasComFiltro = getListaAudienciaAgendadaCompleta(rs1, false);           

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs12.next()) listaAudienciasComFiltro.add(rs12.getLong("QUANTIDADE"));
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasComFiltro;
    }

    /**
     * Método responsável por consultar audiências de acordo com os parâmetros
     * informados pelos advogados na consulta por filtros. Filtros: número do
     * processo, status da audiência, data inicial (audiências com data de
     * agendamento >= à data inicial) e data final (audiências com data de
     * agendamento <= à data final). A sistema consultará somente as audiências
     * de processos nos quais o usuário requisitante é advogado de uma das
     * partes.
     * @author mmitsunaga
     * @author hrrosa
     * @param id_UsuarioServentia
     * @param audienciaDtConsulta
     * @param numeroProcesso
     * @param digitoVerificador
     * @param posicaoPaginaAtual
     * @return
     * @throws Exception
     */
    public List consultarAudienciasFiltroAdvogado(String id_UsuarioServentia, AudienciaDt audienciaDtConsulta, String numeroProcesso, String digitoVerificador, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenação PROJUDI
    		ordenacao = " vw.DATA_AGENDADA ASC, vw.SERV_CARGO ASC, vw.PROC_NUMERO ASC, vw.AUDI_PROC_STATUS ASC ";
    	
    	List listaAudienciasComFiltro = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
        StringBuilder sql2 = new StringBuilder();
		sql2.append(" FROM PROJUDI.VIEW_AUDI_PRIM_GRAU_ADVOGADO vw WHERE");
		
		/*
         * Verificar se o advogado requisitante é advogado de algumas das
         * partes(promovente e promovido) do processo da(s) audiência(s)
         * encontrada(s)
         */       
        sql2.append(" vw.ID_USU_SERV = ?");
        ps.adicionarLong(id_UsuarioServentia);		
		
		// Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && (!audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {        	
        	sql2.append(" AND vw.AUDI_TIPO_CODIGO = ?");
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }

        // Número do Processo
        if (numeroProcesso != null && !numeroProcesso.equalsIgnoreCase("")) {
        	sql2.append(" AND vw.PROC_NUMERO = ?");
            ps.adicionarLong(numeroProcesso);

            // Dígito Verificador do Processo
            if ((digitoVerificador != null) && !(digitoVerificador.equalsIgnoreCase(""))){
            	sql2.append(" AND vw.DIGITO_VERIFICADOR = ?");
            	ps.adicionarLong(digitoVerificador);
            }
        }

        // Serventia Cargo
        if (!(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase("")) && !(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase(null))) {
        	sql2.append(" AND vw.ID_SERV_CARGO = ?");
            ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo());
        }
        // Audiência Status
        if (!(audienciaDtConsulta.getAudienciaProcessoDt().getId_AudienciaProcessoStatus().length() == 0)) {
        	sql2.append(" AND vw.ID_AUDI_PROC_STATUS = ?");
            ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_AudienciaProcessoStatus());
        }
        // Data Agendada
        if (audienciaDtConsulta.getDataInicialConsulta().length() > 0 && audienciaDtConsulta.getDataFinalConsulta().length() > 0) {
        	sql2.append(" AND vw.DATA_AGENDADA BETWEEN ? AND ?");
            ps.adicionarDateTime(audienciaDtConsulta.getDataInicialConsulta() + " 00:00:00");
            ps.adicionarDateTime(audienciaDtConsulta.getDataFinalConsulta() + " 23:59:59");
        }
        
        StringBuilder sql = new StringBuilder();        
        sql.append(obtenhaListaCamposViewAudiAdvogadoPromotor());
		sql.append(sql2);
        sql.append(" ORDER BY " + ordenacao);        
                        
        try{
            rs1 = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual, qtdRegistros); // Modificado para receber qtdRegistros
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // as partes
            listaAudienciasComFiltro = getListaAudienciaAgendadaAdvogado(rs1, false);

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sql2.toString(), ps);
            if (rs12.next()) listaAudienciasComFiltro.add(rs12.getLong("QUANTIDADE"));
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasComFiltro;
    }
    
    
    /**
     * Método responsável por consultar audiências de acordo com os parâmetros
     * informados pelos usuários(conciliadores ou juizes recursais ou juizes
     * togados) na consulta por filtros. Filtros: número do processo, status da
     * audiência, data inicial (audiências com data de agendamento >= à data
     * inicial) e data final (audiências com data de agendamento <= à data
     * final).
     * 
     * @author Keila Sousa silva
     * @author hrrosa
     * @param usuarioNe
     * @param audienciaDtConsulta
     * @param posicaoPaginaAtual
     * @return List listaAudienciasComFiltro
     * @throws Exception
     */
    public List consultarAudienciasFiltroUsuario(String id_ServentiaCargo, AudienciaDt audienciaDtConsulta, String numeroProcesso, String digitoVerificador, String posicaoPaginaAtual, String ordenacao, String qtdRegistros) throws Exception {
    	
    	if (ORDENACAO_PADRAO.equals(ordenacao)) //ordenacao PROJUDI
    		ordenacao = " vac.DATA_AGENDADA ASC, vac.SERV_CARGO ASC, vac.PROC_NUMERO ASC, vac.AUDI_PROC_STATUS ASC ";
    	
        String sql = "";
        String sqlComum = "";
        List listaAudienciasComFiltro = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
       
        sqlComum += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE";
        // Tipo da Audiência
        if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && (!audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
        	sqlComum += " vac.AUDI_TIPO_CODIGO = ? AND";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
        }

        // Número do Processo
        if (numeroProcesso != null && !numeroProcesso.equalsIgnoreCase("")) {
        	sqlComum += " vac.PROC_NUMERO = ? AND";
            ps.adicionarLong(numeroProcesso);

            // Dígito Verificador do Processo
            if ((digitoVerificador != null) && !(digitoVerificador.equalsIgnoreCase(""))){
            	sqlComum += " vac.DIGITO_VERIFICADOR = ? AND";
            	ps.adicionarLong(digitoVerificador);
            }
        }

        // Serventia Cargo
        sqlComum += " vac.ID_SERV_CARGO = ? ";
        ps.adicionarLong(id_ServentiaCargo);
        // Audiência Status
        if ((!(audienciaDtConsulta.getAudienciaProcessoDt().getId_AudienciaProcessoStatus().length() == 0)) && (audienciaDtConsulta.getAudienciaProcessoDt().getAudienciaProcessoStatusCodigo() != String.valueOf(AudienciaProcessoStatusDt.LIVRE))) {
        	sqlComum += " AND vac.ID_AUDI_PROC_STATUS = ? ";
            ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_AudienciaProcessoStatus());
        } else {
        	sqlComum += " AND vac.ID_AUDI_PROC_STATUS <> ? ";
            ps.adicionarLong(AudienciaProcessoStatusDt.LIVRE);
        }
        // Data Agendada
        if (!(audienciaDtConsulta.getDataInicialConsulta().length() == 0)) {
        	sqlComum += " AND vac.DATA_AGENDADA >= ? ";
            ps.adicionarDateTime(audienciaDtConsulta.getDataInicialConsulta() + " 00:00:00");
        }
        // Data Agendada
        if (!(audienciaDtConsulta.getDataFinalConsulta().length() == 0)) {
        	sqlComum += " AND vac.DATA_AGENDADA <= ? ";
            ps.adicionarDateTime(audienciaDtConsulta.getDataFinalConsulta() + " 23:59:59");
        }
        
        sql += obtenhaListaCamposViewAudiCompleta();
        sql += sqlComum;
        // Order By
        sql += " ORDER BY " + ordenacao;   
        try{
            rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, qtdRegistros);
            // Chama método que retorna a lista de audiências já com os
            // processos e
            // as partes
            listaAudienciasComFiltro = getListaAudienciaAgendadaCompleta(rs1, false);          

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            while (rs12.next()) {
                listaAudienciasComFiltro.add(rs12.getLong("QUANTIDADE"));
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasComFiltro;
    }
    
    /**
     * Método responsável por consultar audiências de acordo com os parâmetros informados pelos promotores na consulta por filtros.
     * 
     * @param id_ServentiaCargo, identificação do cargo do promotor
     * @param audienciaDtConsulta, objeto com dados para consulta
     * @param posicaoPaginaAtual, parâmetro para paginação
     * 
     * @author mmitsunaga
     */
    public List consultarAudienciasFiltroPromotor(String id_ServentiaCargo, AudienciaDt audienciaDtConsulta, String numeroProcesso, String digitoVerificador, String posicaoPaginaAtual) throws Exception {
    	
    	List listaAudienciasComFiltro = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs12 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
		StringBuilder sql2 = new StringBuilder();
		sql2.append(" FROM PROJUDI.VIEW_AUDI_PRIM_GRAU_PROMOTOR vw WHERE");
		
		sql2.append(" vw.ID_SERV_CARGO_RESP = ?");
		ps.adicionarLong(id_ServentiaCargo);
		
		if ((audienciaDtConsulta.getAudienciaTipoCodigo() != null) && (!audienciaDtConsulta.getAudienciaTipoCodigo().equalsIgnoreCase(""))) {
			sql2.append(" AND vw.AUDI_TIPO_CODIGO = ?");
		    ps.adicionarLong(audienciaDtConsulta.getAudienciaTipoCodigo());
		}
		
		if (numeroProcesso != null && !numeroProcesso.equalsIgnoreCase("")) {
			sql2.append(" AND vw.PROC_NUMERO = ?");
			ps.adicionarLong(numeroProcesso);
			if ((digitoVerificador != null) && !(digitoVerificador.equalsIgnoreCase(""))){
				sql2.append(" AND vw.DIGITO_VERIFICADOR = ?");
		    	ps.adicionarLong(digitoVerificador);
		    }
		}
		
		if (!(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase("")) && !(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase(null))) {
			sql2.append(" AND vw.ID_SERV_CARGO = ?");
		    ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_ServentiaCargo());
		}
		if (!(audienciaDtConsulta.getAudienciaProcessoDt().getId_AudienciaProcessoStatus().length() == 0)) {
			sql2.append(" AND vw.ID_AUDI_PROC_STATUS = ?");
		    ps.adicionarLong(audienciaDtConsulta.getAudienciaProcessoDt().getId_AudienciaProcessoStatus());
		}
		if (!(audienciaDtConsulta.getDataInicialConsulta().length() == 0)) {
			sql2.append(" AND vw.DATA_AGENDADA >= ?");
			ps.adicionarDateTime(audienciaDtConsulta.getDataInicialConsulta() + " 00:00:00");
		}
		if (!(audienciaDtConsulta.getDataFinalConsulta().length() == 0)) {
			sql2.append(" AND vw.DATA_AGENDADA <= ?");
			ps.adicionarDateTime(audienciaDtConsulta.getDataFinalConsulta() + " 23:59:59");
		}
		
		StringBuilder sql = new StringBuilder();			
		sql.append(obtenhaListaCamposViewAudiAdvogadoPromotor());
		sql.append(sql2);		
		sql.append(" ORDER BY vw.DATA_AGENDADA ASC, vw.SERV_CARGO ASC, vw.PROC_NUMERO ASC, vw.AUDI_PROC_STATUS ASC");       
		        
		try{
            rs1 = consultarPaginacao(sql.toString(), ps, posicaoPaginaAtual);
            // Chama método que retorna a lista de audiências já com os processos e as partes
            listaAudienciasComFiltro = getListaAudienciaAgendadaAdvogado(rs1, false);         

            rs12 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sql2.toString(), ps);
            if (rs12.next()) listaAudienciasComFiltro.add(rs12.getLong("QUANTIDADE"));
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasComFiltro;
    }

    /**
     * Método que possui a sql responsável por consultar as audiências do tipo Sessão do 2º Grau que estão abertas, 
     * ou seja, aquelas onde a DataMovimentacao é nula.
     * 
     * @param id_Serventia
     * @param posicaoPaginaAtual
     * @param ordemDataInversa
     * @return List listaSessoes
     * @throws Exception
     */
    public List consultarSessoesAbertas(String id_Serventia, boolean ordemDataInversa) throws Exception {
        String sql = "";
        List listaSessoes = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        sql = " SELECT DISTINCT a.ID_AUDI, a.DATA_AGENDADA, a.SERV, a.VIRTUAL, a.SESSAO_INICIADA FROM PROJUDI.VIEW_AUDI a";
        sql += " WHERE a.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal());
        sql += " AND a.DATA_MOVI IS NULL";
        sql += " AND a.ID_SERV = ? ";
        ps.adicionarLong(id_Serventia);
        sql += " AND (a.CODIGO_TEMP IS NULL OR a.CODIGO_TEMP <> ?)";
        ps.adicionarLong(AudienciaDt.DESATIVADA);
        sql += " ORDER BY a.DATA_AGENDADA";
        if (ordemDataInversa) sql += " DESC";
        try{
            rs1 = consultar(sql, ps);

            while (rs1.next()) {
                AudienciaDt audienciaDt = new AudienciaDt();
                audienciaDt.setId(rs1.getString("ID_AUDI"));
                audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
                audienciaDt.setServentia(rs1.getString("SERV"));
                audienciaDt.setVirtual(rs1.getBoolean("VIRTUAL"));
                audienciaDt.setSessaoIniciada(rs1.getBoolean("SESSAO_INICIADA"));
                listaSessoes.add(audienciaDt);
            }

        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        return listaSessoes;
    }
    
    /**
     * Método que possui a sql responsável por consultar as audiências, destacando as Virtuais, do tipo Sessão do 2º Grau que estão abertas, 
     * ou seja, aquelas onde a DataMovimentacao é nula.
     * 
     * @param id_Serventia
     * @param posicaoPaginaAtual
     * @param ordemDataInversa
     * @return List listaSessoes
     * @throws Exception
     */
    public List consultarSessoesVirtuaisAbertas(String id_Serventia, boolean ordemDataInversa, boolean naoTrazerIniciadas) throws Exception {
        String sql = "";
        List listaSessoes = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        sql = " SELECT DISTINCT a.ID_AUDI, a.DATA_AGENDADA, a.SERV, a.VIRTUAL FROM PROJUDI.VIEW_AUDI a";
        sql += " WHERE a.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal());
        sql += " AND a.DATA_MOVI IS NULL ";
//        sql += " AND (SESSAO_INICIADA IS NULL OR VIRTUAL IS NULL) AND DATA_AGENDADA > SYSDATE ";
        
        // jvosantos - 30/10/2019 17:21 - Filtrar sessões iniciadas apenas de acordo com a flag
        if(naoTrazerIniciadas)
        	sql += " AND (SESSAO_INICIADA IS NULL OR VIRTUAL IS NULL) "; // jvosantos - 11/10/2019 11:31 - Filtrar sessões iniciadas
        
        sql += " AND a.ID_SERV = ? ";
        ps.adicionarLong(id_Serventia);
        sql += " AND (a.CODIGO_TEMP IS NULL OR a.CODIGO_TEMP <> ?)";
        ps.adicionarLong(AudienciaDt.DESATIVADA);
        sql += " ORDER BY a.DATA_AGENDADA";
        
        if (ordemDataInversa) sql += " DESC";
        try{
            rs1 = consultar(sql, ps);

            while (rs1.next()) {
                AudienciaSegundoGrauDt audienciaDt = new AudienciaSegundoGrauDt();
                audienciaDt.setId(rs1.getString("ID_AUDI"));
                audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
                audienciaDt.setServentia(rs1.getString("SERV"));
                audienciaDt.setVirtual(rs1.getBoolean("VIRTUAL"));
                listaSessoes.add(audienciaDt);
            }

        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        return listaSessoes;
    }

    /**
     * Método que possui a sql responsável por consultar as audiências do tipo Sessão do 2º Grau que estão abertas, 
     * ou seja, aquelas onde a DataMovimentacao é nula.
     * Esse método irá trazer todas as sessões abertas em todas as serventias que o gabinete do desembargador esteja relacionado.
     * 
     * @param id_Serventia
     * @param ordemDataInversa
     * @return List listaSessoes
     * @throws Exception
     */
	public List consultarSessoesAbertasCamaras(String id_ServentiaGabinete, boolean ordemDataInversa) throws Exception {
        String sql = "";
        List listaSessoes = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        sql = " SELECT DISTINCT a.ID_AUDI, a.DATA_AGENDADA, a.SERV, a.VIRTUAL, a.SESSAO_INICIADA FROM PROJUDI.VIEW_AUDI a";
        sql += " LEFT JOIN PROJUDI.SERV_RELACIONADA sr on a.ID_SERV = sr.ID_SERV_PRINC";
        sql += " WHERE a.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal());
        sql += " AND a.DATA_MOVI IS NULL";
        sql += " AND sr.ID_SERV_REL = ? ";
        ps.adicionarLong(id_ServentiaGabinete);
        sql += " AND (a.CODIGO_TEMP IS NULL OR a.CODIGO_TEMP <> ?)";
        ps.adicionarLong(AudienciaDt.DESATIVADA);
        sql += " ORDER BY a.DATA_AGENDADA";
        if (ordemDataInversa) sql += " DESC";
        sql += " , a.SERV";
        
        try{
            rs1 = consultar(sql, ps);

            while (rs1.next()) {
                AudienciaDt audienciaDt = new AudienciaDt();
                audienciaDt.setId(rs1.getString("ID_AUDI"));
                audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
                audienciaDt.setServentia(rs1.getString("SERV"));
                audienciaDt.setVirtual(rs1.getBoolean("VIRTUAL"));
                audienciaDt.setSessaoIniciada(rs1.getBoolean("SESSAO_INICIADA"));
                listaSessoes.add(audienciaDt);
            }

        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        return listaSessoes;
    }
    
    /**
     * Consulta data da ultima audiencia determinando-se o processo e o tipo da
     * audiencia Anotacoes: 18/04/2008 - Provavelmente sera necessario verificar
     * o status e se ja foi realizada
     * 
     * @author Ronneesley Moura Teles
     * @author msapaula
     * @since 07/04/2009 10:34
     * @param String
     *            id_processo, id do processo
     * @param int
     *            audienciaTipoCodigo, codigo do tipo de audiencia
     */
    public Date consultarDataUltimaAudiencia(String id_processo) throws Exception {
        Date data = null;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        String sql = " SELECT a.DATA_AGENDADA FROM PROJUDI.VIEW_AUDI_PROC_A_REALIZAR a";
        sql += " WHERE a.ID_PROC = ? ";
        ps.adicionarLong(id_processo);
        try{
            rs1 = this.consultar(sql, ps);

            if (rs1.next()) {
                data = rs1.getDateTime("DATA_AGENDADA");
            }
            // rs1.close();
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        return data;
    }

    /**
     * Método que contém a sql que validará o agendamento/reagendamento de uma
     * audiência para um dado processo, ou seja, verificará se o processo possui
     * uma audiência pendente (Status da audiência pendente = "A Ser Realizada" =
     * 1) cujo tipo da audiência seja o mesmo da audiência livre selecionada.
     * Caso já exista essa audiência, o agendamento/reagendamento dessa
     * audiência livre para esse processo não poderá ocorrer
     * 
     * @author Keila Sousa Silva
     * 
     * @param id_Processo ,
     *            processo para o qual a audiência será marcada
     * @return int quantidade
     * @throws Exception
     */
    public int validarAudienciaAgendamento(String id_Processo) throws Exception {
        String sql = "";
        ResultSetTJGO rs1 = null;
        int quantidade = 0;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();

        sql = "SELECT COUNT(1) AS Quantidade FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU va WHERE";
        sql += " va.ID_AUDI_PROC_STATUS = ? ";
        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
        sql += " AND va.ID_PROC IS NOT NULL AND va.ID_PROC = ? ";
        ps.adicionarLong(id_Processo);
        try{
            rs1 = consultar(sql, ps);
            if (rs1.next()) {
                quantidade = Funcoes.StringToInt(rs1.getString("QUANTIDADE"));
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return quantidade;
    }

     /**
     * Método que contém a sql que irá buscar as audiências livres, de um dado
     * tipo, menos o tipo "Sessão de 2º Grau", e de um cargo da serventia na
     * qual o usuário corrente está logado
     * 
     * @author Keila Sousa Silva
     * @param id_AudienciaTipo
     * @param id_ServentiaCargo
     * @param id_Serventia
     * @param posicaoPaginaAtual
     * @return List listaAudienciasLivres: audiências livres de um dado tipo e
     *         do cargo da serventia na qual o usuário está logado
     * @throws Exception
     * Alterado por: Márcio Mendonça Gomes em 10/09/2010
     */
    public List consultarAudienciasLivres(String id_AudienciaTipo, String id_ServentiaCargo, String id_Serventia, String posicaoPaginaAtual) throws Exception {
        String sql;
        String sqlComum;
        List listaAudienciasLivres = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        
        // Parte da SQL utilizada na seleção dos registros e no count 
        sqlComum = " FROM (((PROJUDI.AUDI ac";
        sqlComum += " INNER JOIN PROJUDI.AUDI_PROC ap ON (ac.ID_AUDI = ap.ID_AUDI))";
        sqlComum += " INNER JOIN PROJUDI.AUDI_TIPO at ON((ac.ID_AUDI_TIPO = at.ID_AUDI_TIPO)))";
        sqlComum += " LEFT JOIN PROJUDI.SERV_CARGO sc ON((ap.ID_SERV_CARGO = sc.ID_SERV_CARGO)))";        
        sqlComum += " WHERE (at.AUDI_TIPO_CODIGO <> ?)";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
        sqlComum += " AND ac.ID_AUDI_TIPO = ? ";
        ps.adicionarLong(id_AudienciaTipo);
                
        // Verificar se o cargo de serventia foi informado
        if ((id_ServentiaCargo != null) && (!id_ServentiaCargo.trim().equalsIgnoreCase(""))) {
        	sqlComum += " AND ap.ID_SERV_CARGO = ? ";
        	ps.adicionarLong(id_ServentiaCargo);
        } else {
            // Caso o cargo não tenha sido informado, buscar somente as agendas
            // da
            // serventia na qual o usuário logado
        	sqlComum += " AND ac.ID_SERV = ? ";
        	ps.adicionarLong(id_Serventia);
        }
        sqlComum += " AND ap.ID_PROC IS NULL";      
        
        // SQL utilizada para retornar os registros
        sql = "SELECT ac.ID_AUDI, at.AUDI_TIPO, ac.DATA_AGENDADA, ac.RESERVADA, sc.SERV_CARGO";
        sql += sqlComum;
        sql += " ORDER BY ac.DATA_AGENDADA, at.AUDI_TIPO";
        
        try{
            rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);
            while (rs1.next()) {
                listaAudienciasLivres.add(new String[] {rs1.getString("ID_AUDI"), rs1.getString("AUDI_TIPO"), Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")), Funcoes.FormatarLogico(rs1.getString("RESERVADA")), rs1.getString("SERV_CARGO")});                
            }

            sql = "SELECT COUNT(1) AS QUANTIDADE ";
            sql += sqlComum;            
            rs12 = consultar(sql, ps);
            if (rs12.next()) listaAudienciasLivres.add(rs12.getLong("QUANTIDADE"));
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return listaAudienciasLivres;
    }   
    

    /**
     * Método responsável por criar, instanciar, "setar" e retornar um objeto
     * COMPLETO do tipo "AudienciaDt". Este objeto será "setado" com os dados
     * consultados na busca de audiência(s), ou seja, de acordo com o resultado
     * da sql contida na propriedade do tipo resultset.
     * 
     * @author Keila Sousa Silva
     * @param rs1
     * @throws Exception
     */
    private AudienciaDt prepararAudienciaLivreCompleta(ResultSetTJGO rs1) throws Exception{
        // PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIADT"
        AudienciaDt audienciaDt = new AudienciaDt();
        associarAudienciaDt(audienciaDt, rs1);

        // PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIAPROCESSODT"
        AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
        AudienciaProcessoPs audienciaProcessoPs = new AudienciaProcessoPs();
        audienciaProcessoPs.associarAudienciaProcessoDt(audienciaProcessoDt, rs1);

        // LISTA CONTENDO OBJETO(S) DO TIPO "AUDIÊNCIAPROCESSODT"
        // Adicionando objeto do tipo "AudiênciaProcessoDt" à uma lista do
        // objeto do
        // tipo "AudiênciaDt"
        audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);

        // RETORNO
        return audienciaDt;
    }

    /**
     * Método responsável por criar, instanciar, "setar" e retornar um objeto
     * SIMPLES do tipo "AudienciaDt". Este objeto será "setado" com os dados
     * contidos no ResultSetTJGO passado como parâmetro e este foi obtido com a
     * execução da SQL de busca da próxima audiência válida e livre que poderá
     * ser utilizada no agendamento automático ou manual de audiência(s).
     * 
     * @author Keila Sousa Silva
     * @param rs1
     * @throws Exception 
     * @throws Exception
     */
    private AudienciaDt prepararAudienciaLivreAgendamento(ResultSetTJGO rs1) throws Exception{
        // PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIADT"
        AudienciaDt audienciaDt = new AudienciaDt();
        audienciaDt.setId(rs1.getString("ID_AUDI"));
        audienciaDt.setId_AudienciaTipo(rs1.getString("ID_AUDI_TIPO"));
        audienciaDt.setAudienciaTipoCodigo(rs1.getString("AUDI_TIPO_CODIGO"));
        audienciaDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
        audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
        audienciaDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI")));
        audienciaDt.setReservada(Funcoes.FormatarLogico(rs1.getString("RESERVADA")));
        audienciaDt.setId_Serventia(rs1.getString("ID_SERV"));
        audienciaDt.setServentia(rs1.getString("SERV"));

        // PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIAPROCESSODT"
        AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
        audienciaProcessoDt.setId(rs1.getString("ID_AUDI_PROC"));
        audienciaProcessoDt.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
        audienciaProcessoDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
        audienciaProcessoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));

        // Adicionando objeto do tipo "AudiênciaProcessoDt" à uma lista do
        // objeto do
        // tipo "AudiênciaDt"
        audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
        audienciaDt.getAudienciaProcessoDt().setId_Processo(rs1.getString("ID_PROC"));

        // RETORNO
        return audienciaDt;
    }
    
    /**
     * Método responsável por montar a lista de audiências com seu(s) respectivo(s) processo(s) e parte(s), de acordo com um ResultSetTJGO recebido
     * como parâmetro, retornando uma lista de objeto(s) do tipo AudienciaDt pronto(s), ou seja, com os dados necessários para exibição na tela
     * 
     * @author Marielli
     * @param rs1, resultado da consulta de audiências
     * @return List listaAudiencias, lista de objetos do tipo AudienciaDt contendo o(s) processo(s) e parte(s)
     * @throws Exception
     */
    private List getListaAudienciaAgendadaCompleta(ResultSetTJGO rs1, Boolean buscaTodasPartes) throws Exception{
        List listaAudiencias = new ArrayList();
        AudienciaDt audienciaDt = null;
        ProcessoDt processoDt = null;
        String audienciaAnterior = "";
        String processoAnterior = "";
        String processoFisicoAnterior = "";
        Map mapProcesso = new LinkedHashMap();
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        //contador criado para evitar erro do Oracle em casos onde
        //há mais de mil registros a serem consultados
        int contadorSQL = 0;
        
        // Criando objeto do tipo AudienciaDt. Só deverá preencher um novo
        while (rs1.next()) {
           // objeto de audiência se não for igual ao anterior
            if (!rs1.getString("ID_AUDI").equalsIgnoreCase(audienciaAnterior)) {
                audienciaDt = new AudienciaDt();
                listaAudiencias.add(audienciaDt);
                processoAnterior = "";
            }

            // SET AUDIENCIADT
            associarAudienciaDt(audienciaDt, rs1);

            // SET AUDIÊNCIAPROCESSODT E PROCESSODT
            if (rs1.getString("ID_PROC") != null) {
				if (!rs1.getString("ID_PROC").equalsIgnoreCase(processoAnterior)) {
                    AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
                    audienciaProcessoDt.setId(rs1.getString("ID_AUDI_PROC"));
                    audienciaProcessoDt.setId_Audiencia(rs1.getString("ID_AUDI"));
                    audienciaProcessoDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
                    audienciaProcessoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
                    audienciaProcessoDt.setNomeResponsavel(rs1.getString("NOME"));
                   
                    audienciaProcessoDt.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
                    audienciaProcessoDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
                    audienciaProcessoDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
                    audienciaProcessoDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI_PROC")));
                    audienciaProcessoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_AUDI_PROC"));
                    audienciaProcessoDt.setId_AudienciaProcessoStatusAnalista(rs1.getString("ID_AUDI_PROC_STATUS_ANA"));
                    audienciaProcessoDt.setAudienciaProcessoStatusAnalista(rs1.getString("AUDI_PROC_STATUS_ANA"));
                    audienciaProcessoDt.setAudienciaProcessoStatusCodigoAnalista(rs1.getString("AUDI_PROC_STATUS_CODIGO_ANA"));
                    audienciaProcessoDt.setId_ArquivoAta(rs1.getString("ID_ARQ_ATA"));      
                    audienciaProcessoDt.setDataAudienciaOriginal(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AUDI_ORIGINAL")));
                    audienciaProcessoDt.setId_Audi_Proc_Origem(rs1.getString("ID_AUDI_PROC_ORIGEM"));
                    audienciaProcessoDt.setId_ArquivoAtaSessaoAdiada(rs1.getString("ID_ARQ_ATA_ADIAMENTO"));
                    audienciaProcessoDt.setId_ArquivoAtaSessaoIniciada(rs1.getString("ID_ARQ_ATA_INICIADO"));
                    audienciaProcessoDt.setId_ServentiaCargoPresidente(rs1.getString("ID_SERV_CARGO_PRESIDENTE"));
                    audienciaProcessoDt.setServentiaCargoPresidente(rs1.getString("SERV_CARGO_PRESIDENTE") + " - " + rs1.getString("NOME_PRESIDENTE"));
                    audienciaProcessoDt.setId_ServentiaPresidente(rs1.getString("ID_SERV_PRESIDENTE"));
                    audienciaProcessoDt.setServentiaPresidente(rs1.getString("SERV_PRESIDENTE"));
                    audienciaProcessoDt.setId_ServentiaCargoMP(rs1.getString("ID_SERV_CARGO_MP"));
                    audienciaProcessoDt.setServentiaCargoMP(rs1.getString("SERV_CARGO_MP") + " - " + rs1.getString("NOME_MP"));
                    audienciaProcessoDt.setId_ServentiaMP(rs1.getString("ID_SERV_MP"));
                    audienciaProcessoDt.setServentiaMP(rs1.getString("SERV_MP"));
                    if (!rs1.isNull("ID_SERV_CARGO_REDATOR")) audienciaProcessoDt.setId_ServentiaCargoRedator(rs1.getString("ID_SERV_CARGO_REDATOR"));                    
                    if (rs1.getString("SERV_CARGO_REDATOR") != null)
                    audienciaProcessoDt.setServentiaCargoRedator(rs1.getString("SERV_CARGO_REDATOR") + " - " + rs1.getString("NOME_REDATOR"));
                    audienciaProcessoDt.setId_ServentiaRedator(rs1.getString("ID_SERV_REDATOR"));
                    audienciaProcessoDt.setServentiaRedator(rs1.getString("SERV_REDATOR"));
                    audienciaProcessoDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
                    audienciaProcessoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_AUDIENCIA"));
                    audienciaProcessoDt.setProcessoTipo(rs1.getString("PROC_TIPO_AUDIENCIA"));
                    audienciaProcessoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
                    audienciaProcessoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
                    if (!rs1.isNull("ID_PEND_VOTO")) audienciaProcessoDt.setId_PendenciaVotoRelator(rs1.getString("ID_PEND_VOTO"));
                    if (!rs1.isNull("ID_PEND_EMENTA")) audienciaProcessoDt.setId_PendenciaEmentaRelator(rs1.getString("ID_PEND_EMENTA"));
                    if (!rs1.isNull("ID_PEND_VOTO_REDATOR")) audienciaProcessoDt.setId_PendenciaVotoRedator(rs1.getString("ID_PEND_VOTO_REDATOR"));
                    if (!rs1.isNull("ID_PEND_EMENTA_REDATOR")) audienciaProcessoDt.setId_PendenciaEmentaRedator(rs1.getString("ID_PEND_EMENTA_REDATOR"));
                   
                    // Dados do processo
                    audienciaProcessoDt.setId_Processo(rs1.getString("ID_PROC")); // jvosantos - 25/09/2019 13:18 - Setar o ID_PROC
                    processoDt = new ProcessoDt();
                    processoDt.setId_Processo(rs1.getString("ID_PROC"));
					// jvosantos - 26/08/2019 16:45 - Formatar número do processo com digito verificador para apresentar no "Sessões"
                    processoDt.setProcessoNumero(Funcoes.formatarProcessoDigito(rs1.getString("PROC_NUMERO_COMPLETO")));
                    audienciaProcessoDt.setProcessoDt(processoDt);
                    
                    if (buscaTodasPartes) {
                    	// Concatena os processos encontrados para depois buscar as partes desses
                        contadorSQL++;
                        ps.adicionarLong(rs1.getString("ID_PROC"));
                        mapProcesso.put(processoDt.getId(), processoDt);                    	
                    } else {
                    	ProcessoParteDt primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE_RECORRENTE"));
                        if (primeiroPromoventeRecorrente != null) processoDt.addListaPoloAtivo(primeiroPromoventeRecorrente);
                        
                        ProcessoParteDt primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO_RECORRIDO"));
                        if (primeiroPromovidoRecorrido != null) processoDt.addListaPolosPassivos(primeiroPromovidoRecorrido);
                    }             

                    // Incluíndo objeto do tipo "AudienciaProcessoDt" na lista
                    audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
                }
            } else if (rs1.getString("CODIGO_TEMP_AUDI_PROC") != null) {
            	if (!rs1.getString("CODIGO_TEMP_AUDI_PROC").equalsIgnoreCase(processoFisicoAnterior)) {
            		AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = new AudienciaProcessoFisicoDt();
                    audienciaProcessoFisicoDt.setId(rs1.getString("ID_AUDI_PROC"));
                    audienciaProcessoFisicoDt.setId_Audiencia(rs1.getString("ID_AUDI"));
                    audienciaProcessoFisicoDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
                    audienciaProcessoFisicoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
                    audienciaProcessoFisicoDt.setNomeResponsavel(rs1.getString("NOME"));
                    audienciaProcessoFisicoDt.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
                    audienciaProcessoFisicoDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI_PROC")));
                    audienciaProcessoFisicoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_AUDI_PROC"));
                    audienciaProcessoFisicoDt.setId_AudienciaProcessoStatusAnalista(rs1.getString("ID_AUDI_PROC_STATUS_ANA"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatusAnalista(rs1.getString("AUDI_PROC_STATUS_ANA"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatusCodigoAnalista(rs1.getString("AUDI_PROC_STATUS_CODIGO_ANA"));
                    audienciaProcessoFisicoDt.setId_ArquivoAta(rs1.getString("ID_ARQ_ATA"));      
                    audienciaProcessoFisicoDt.setDataAudienciaOriginal(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AUDI_ORIGINAL")));
                    audienciaProcessoFisicoDt.setId_Audi_Proc_Origem(rs1.getString("ID_AUDI_PROC_ORIGEM"));
                    audienciaProcessoFisicoDt.setId_ArquivoAtaSessaoAdiada(rs1.getString("ID_ARQ_ATA_ADIAMENTO"));
                    audienciaProcessoFisicoDt.setId_ArquivoAtaSessaoIniciada(rs1.getString("ID_ARQ_ATA_INICIADO"));
                    audienciaProcessoFisicoDt.setId_ServentiaCargoPresidente(rs1.getString("ID_SERV_CARGO_PRESIDENTE"));
                    audienciaProcessoFisicoDt.setServentiaCargoPresidente(rs1.getString("SERV_CARGO_PRESIDENTE") + " - " + rs1.getString("NOME_PRESIDENTE"));
                    audienciaProcessoFisicoDt.setId_ServentiaPresidente(rs1.getString("ID_SERV_PRESIDENTE"));
                    audienciaProcessoFisicoDt.setServentiaPresidente(rs1.getString("SERV_PRESIDENTE"));
                    audienciaProcessoFisicoDt.setId_ServentiaCargoMP(rs1.getString("ID_SERV_CARGO_MP"));
                    audienciaProcessoFisicoDt.setServentiaCargoMP(rs1.getString("SERV_CARGO_MP") + " - " + rs1.getString("NOME_MP"));
                    audienciaProcessoFisicoDt.setId_ServentiaMP(rs1.getString("ID_SERV_MP"));
                    audienciaProcessoFisicoDt.setServentiaMP(rs1.getString("SERV_MP"));
                    if (!rs1.isNull("ID_SERV_CARGO_REDATOR")) audienciaProcessoFisicoDt.setId_ServentiaCargoRedator(rs1.getString("ID_SERV_CARGO_REDATOR"));
                    if (rs1.getString("SERV_CARGO_REDATOR") != null)
                    audienciaProcessoFisicoDt.setServentiaCargoRedator(rs1.getString("SERV_CARGO_REDATOR") + " - " + rs1.getString("NOME_REDATOR"));
                    audienciaProcessoFisicoDt.setId_ServentiaRedator(rs1.getString("ID_SERV_REDATOR"));
                    audienciaProcessoFisicoDt.setServentiaRedator(rs1.getString("SERV_REDATOR"));  
                    audienciaProcessoFisicoDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
                    
                    ProcessoFisicoDt processoFisicoDt = new ProcessoFisicoDt();
                    
                    processoFisicoDt.setNumeroProcesso(rs1.getString("PROC_NUMERO_COMPLETO"));
                    processoFisicoDt.setPromovente(rs1.getString("PRIMEIRO_PROMOVENTE_RECORRENTE"));
                    processoFisicoDt.setPromovido(rs1.getString("PRIMEIRO_PROMOVIDO_RECORRIDO"));
                    
                    audienciaProcessoFisicoDt.setProcessoNumero(Funcoes.formataNumeroDigitoProcesso(processoFisicoDt.getNumeroProcesso()));

                    audienciaProcessoFisicoDt.setProcessoFisicoDt(processoFisicoDt);
                    
                    // Incluíndo objeto do tipo "AudienciaProcessoDt" na lista
                    audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoFisicoDt);
            	}
            }

            processoAnterior = rs1.getString("ID_PROC");
            audienciaAnterior = rs1.getString("ID_AUDI");
            processoFisicoAnterior = rs1.getString("CODIGO_TEMP_AUDI_PROC");
        }
        try{if (rs1 != null) rs1.close();} catch(Exception e1) {}

        if (contadorSQL > 0) {
            // Busca as partes para cada processo encontrado anteriormente
            String sql = " SELECT p.ID_PROC_PARTE, p.NOME, p.PROC_PARTE_TIPO_CODIGO, p.ID_PROC, p.PROC_PARTE_TIPO FROM PROJUDI.VIEW_PROC_PARTE p";
            sql += " WHERE p.DATA_BAIXA IS NULL AND (";
            for (int i = 0; i < contadorSQL; i++) {
            	if(i > 0) {
            		sql += " OR ";
            	}
            	sql += " p.ID_PROC = ? ";
			}
            sql += " ) ";
            rs1 = consultar(sql, ps);
            while (rs1.next()) {
                String id_Processo = rs1.getString("ID_PROC");
                ProcessoDt tempProcessoDt = (ProcessoDt) mapProcesso.get(id_Processo);

                ProcessoParteDt parteDt = new ProcessoParteDt();
                parteDt.setId(rs1.getString("ID_PROC_PARTE"));
                parteDt.setNome(rs1.getString("NOME"));
                parteDt.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));

                // Adiciona parte a lista correspondente
                switch (Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"))) {
                case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):                
                    tempProcessoDt.addListaPoloAtivo(parteDt);
                    break;
                case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):                
                    tempProcessoDt.addListaPolosPassivos(parteDt);
                    break;
                }
            }
            try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return listaAudiencias;
    }    
    
    // jvosantos - 05/07/2019 15:29 - Adicionar parâmetro para corrigir o erro de não trazer alguns processos
    /**
     * Método responsável por montar a lista de audiências com seu(s) respectivo(s) processo(s) e parte(s), de acordo com um ResultSetTJGO recebido
     * como parâmetro, retornando uma lista de objeto(s) do tipo AudienciaDt pronto(s), ou seja, com os dados necessários para exibição na tela
     * 
     * @author Marielli
     * @param rs1, resultado da consulta de audiências
     * @return List listaAudiencias, lista de objetos do tipo AudienciaDt contendo o(s) processo(s) e parte(s)
     * @throws Exception
     */
    private List<AudienciaDt> getListaAudienciaAgendadaCompletaSessaoVirtual(ResultSetTJGO rs1, Boolean buscaTodasPartes) throws Exception{
        List<AudienciaDt> listaAudiencias = new ArrayList<AudienciaDt>();
        AudienciaDt audienciaDt = null;
        ProcessoDt processoDt = null;
        String audienciaAnterior = "";
        String processoFisicoAnterior = "";
        Map<String, ProcessoDt> mapProcesso = new LinkedHashMap<String, ProcessoDt>();
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        //contador criado para evitar erro do Oracle em casos onde
        //há mais de mil registros a serem consultados
        int contadorSQL = 0;
        
		SustentacaoOralPs sustentacaoOralPs = new SustentacaoOralPs(this.Conexao);
        
        // Criando objeto do tipo AudienciaDt. Só deverá preencher um novo
        while (rs1.next()) {
           // objeto de audiência se não for igual ao anterior
            if (!rs1.getString("ID_AUDI").equalsIgnoreCase(audienciaAnterior)) {
                audienciaDt = new AudienciaDt();
                listaAudiencias.add(audienciaDt);
            }

            // SET AUDIENCIADT
            associarAudienciaDt(audienciaDt, rs1);

            // SET AUDIÊNCIAPROCESSODT E PROCESSODT
            if (rs1.getString("ID_PROC") != null) {
                AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
                audienciaProcessoDt.setId(rs1.getString("ID_AUDI_PROC"));
                audienciaProcessoDt.setId_Audiencia(rs1.getString("ID_AUDI"));
                audienciaProcessoDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
                audienciaProcessoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
                audienciaProcessoDt.setNomeResponsavel(rs1.getString("NOME"));
               
                audienciaProcessoDt.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
                audienciaProcessoDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
                audienciaProcessoDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
                audienciaProcessoDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI_PROC")));
                audienciaProcessoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_AUDI_PROC"));
                audienciaProcessoDt.setId_AudienciaProcessoStatusAnalista(rs1.getString("ID_AUDI_PROC_STATUS_ANA"));
                audienciaProcessoDt.setAudienciaProcessoStatusAnalista(rs1.getString("AUDI_PROC_STATUS_ANA"));
                audienciaProcessoDt.setAudienciaProcessoStatusCodigoAnalista(rs1.getString("AUDI_PROC_STATUS_CODIGO_ANA"));
                audienciaProcessoDt.setId_ArquivoAta(rs1.getString("ID_ARQ_ATA"));      
                audienciaProcessoDt.setDataAudienciaOriginal(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AUDI_ORIGINAL")));
                audienciaProcessoDt.setId_Audi_Proc_Origem(rs1.getString("ID_AUDI_PROC_ORIGEM"));
                audienciaProcessoDt.setId_ArquivoAtaSessaoAdiada(rs1.getString("ID_ARQ_ATA_ADIAMENTO"));
                audienciaProcessoDt.setId_ArquivoAtaSessaoIniciada(rs1.getString("ID_ARQ_ATA_INICIADO"));
                audienciaProcessoDt.setId_ServentiaCargoPresidente(rs1.getString("ID_SERV_CARGO_PRESIDENTE"));
                audienciaProcessoDt.setServentiaCargoPresidente(rs1.getString("SERV_CARGO_PRESIDENTE") + " - " + rs1.getString("NOME_PRESIDENTE"));
                audienciaProcessoDt.setId_ServentiaPresidente(rs1.getString("ID_SERV_PRESIDENTE"));
                audienciaProcessoDt.setServentiaPresidente(rs1.getString("SERV_PRESIDENTE"));
                audienciaProcessoDt.setId_ServentiaCargoMP(rs1.getString("ID_SERV_CARGO_MP"));
                audienciaProcessoDt.setServentiaCargoMP(rs1.getString("SERV_CARGO_MP") + " - " + rs1.getString("NOME_MP"));
                audienciaProcessoDt.setId_ServentiaMP(rs1.getString("ID_SERV_MP"));
                audienciaProcessoDt.setServentiaMP(rs1.getString("SERV_MP"));
                if (!rs1.isNull("ID_SERV_CARGO_REDATOR")) audienciaProcessoDt.setId_ServentiaCargoRedator(rs1.getString("ID_SERV_CARGO_REDATOR"));
                if (rs1.getString("SERV_CARGO_REDATOR") != null)
                	audienciaProcessoDt.setServentiaCargoRedator(rs1.getString("SERV_CARGO_REDATOR") + " - " + rs1.getString("NOME_REDATOR"));
                audienciaProcessoDt.setId_ServentiaRedator(rs1.getString("ID_SERV_REDATOR"));
                audienciaProcessoDt.setServentiaRedator(rs1.getString("SERV_REDATOR"));
                audienciaProcessoDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
                audienciaProcessoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_AUDIENCIA"));
                    
                //lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sessão possui recurso secundario. 
				if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
					audienciaProcessoDt.setProcessoTipo(rs1.getString("PROC_TIPO_AUDIENCIA"));
				} else if (rs1.getString("POSSUI_RECURSO") == null) {
					audienciaProcessoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
				} else {
					audienciaProcessoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO_AUDIENCIA"));
				}
				
                audienciaProcessoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
                audienciaProcessoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
                if (!rs1.isNull("ID_PEND_VOTO")) audienciaProcessoDt.setId_PendenciaVotoRelator(rs1.getString("ID_PEND_VOTO"));
                if (!rs1.isNull("ID_PEND_EMENTA")) audienciaProcessoDt.setId_PendenciaEmentaRelator(rs1.getString("ID_PEND_EMENTA"));
                if (!rs1.isNull("ID_PEND_VOTO_REDATOR")) audienciaProcessoDt.setId_PendenciaVotoRedator(rs1.getString("ID_PEND_VOTO_REDATOR"));
                if (!rs1.isNull("ID_PEND_EMENTA_REDATOR")) audienciaProcessoDt.setId_PendenciaEmentaRedator(rs1.getString("ID_PEND_EMENTA_REDATOR"));
                
                // jvosantos - 16/10/2019 15:36 - Correção para chamar o método apenas caso seja uma audiencia virtual
				// lrcampos - 11/10/2019 9:15 - Vide mensagem abaixo
                // Caso a Audiencia for adiada por sustentação oral setar como verdadeiro para listagem na grid de audiencia "Adiado Por sustentação oral"
                if(audienciaDt.isVirtual())
                	audienciaProcessoDt.setAdiadaPorSustentacaoOral(sustentacaoOralPs.possuiAudienciaVirtualOriginalPJD(rs1.getString("ID_PROC"), rs1.getString("DATA_AUDI_ORIGINAL")));

                // Dados do processo
                audienciaProcessoDt.setId_Processo(rs1.getString("ID_PROC")); // jvosantos - 25/09/2019 13:18 - Setar o ID_PROC
                processoDt = new ProcessoDt();
                processoDt.setId_Processo(rs1.getString("ID_PROC"));
				// jvosantos - 26/08/2019 16:45 - Formatar número do processo com digito verificador para apresentar no "Sessões"
                processoDt.setProcessoNumero(Funcoes.formatarProcessoDigito(rs1.getString("PROC_NUMERO_COMPLETO")));
                audienciaProcessoDt.setProcessoDt(processoDt);
                
                if (buscaTodasPartes) {
                	// Concatena os processos encontrados para depois buscar as partes desses
                    contadorSQL++;
                    ps.adicionarLong(rs1.getString("ID_PROC"));
                    mapProcesso.put(processoDt.getId(), processoDt);                    	
                } else {
                	ProcessoParteDt primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE_RECORRENTE"));
                    if (primeiroPromoventeRecorrente != null) processoDt.addListaPoloAtivo(primeiroPromoventeRecorrente);
                    
                    ProcessoParteDt primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO_RECORRIDO"));
                    if (primeiroPromovidoRecorrido != null) processoDt.addListaPolosPassivos(primeiroPromovidoRecorrido);
                }             

                    // Incluíndo objeto do tipo "AudienciaProcessoDt" na lista
                    audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
            } else if (rs1.getString("CODIGO_TEMP_AUDI_PROC") != null) {
            	if (!rs1.getString("CODIGO_TEMP_AUDI_PROC").equalsIgnoreCase(processoFisicoAnterior)) {
            		AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = new AudienciaProcessoFisicoDt();
                    audienciaProcessoFisicoDt.setId(rs1.getString("ID_AUDI_PROC"));
                    audienciaProcessoFisicoDt.setId_Audiencia(rs1.getString("ID_AUDI"));
                    audienciaProcessoFisicoDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
                    audienciaProcessoFisicoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
                    audienciaProcessoFisicoDt.setNomeResponsavel(rs1.getString("NOME"));
                    audienciaProcessoFisicoDt.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
                    audienciaProcessoFisicoDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI_PROC")));
                    audienciaProcessoFisicoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_AUDI_PROC"));
                    audienciaProcessoFisicoDt.setId_AudienciaProcessoStatusAnalista(rs1.getString("ID_AUDI_PROC_STATUS_ANA"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatusAnalista(rs1.getString("AUDI_PROC_STATUS_ANA"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatusCodigoAnalista(rs1.getString("AUDI_PROC_STATUS_CODIGO_ANA"));
                    audienciaProcessoFisicoDt.setId_ArquivoAta(rs1.getString("ID_ARQ_ATA"));      
                    audienciaProcessoFisicoDt.setDataAudienciaOriginal(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AUDI_ORIGINAL")));
                    audienciaProcessoFisicoDt.setId_Audi_Proc_Origem(rs1.getString("ID_AUDI_PROC_ORIGEM"));
                    audienciaProcessoFisicoDt.setId_ArquivoAtaSessaoAdiada(rs1.getString("ID_ARQ_ATA_ADIAMENTO"));
                    audienciaProcessoFisicoDt.setId_ArquivoAtaSessaoIniciada(rs1.getString("ID_ARQ_ATA_INICIADO"));
                    audienciaProcessoFisicoDt.setId_ServentiaCargoPresidente(rs1.getString("ID_SERV_CARGO_PRESIDENTE"));
                    audienciaProcessoFisicoDt.setServentiaCargoPresidente(rs1.getString("SERV_CARGO_PRESIDENTE") + " - " + rs1.getString("NOME_PRESIDENTE"));
                    audienciaProcessoFisicoDt.setId_ServentiaPresidente(rs1.getString("ID_SERV_PRESIDENTE"));
                    audienciaProcessoFisicoDt.setServentiaPresidente(rs1.getString("SERV_PRESIDENTE"));
                    audienciaProcessoFisicoDt.setId_ServentiaCargoMP(rs1.getString("ID_SERV_CARGO_MP"));
                    audienciaProcessoFisicoDt.setServentiaCargoMP(rs1.getString("SERV_CARGO_MP") + " - " + rs1.getString("NOME_MP"));
                    audienciaProcessoFisicoDt.setId_ServentiaMP(rs1.getString("ID_SERV_MP"));
                    audienciaProcessoFisicoDt.setServentiaMP(rs1.getString("SERV_MP"));
                    if (!rs1.isNull("ID_SERV_CARGO_REDATOR")) audienciaProcessoFisicoDt.setId_ServentiaCargoRedator(rs1.getString("ID_SERV_CARGO_REDATOR"));
                    if (rs1.getString("SERV_CARGO_REDATOR") != null)
                    audienciaProcessoFisicoDt.setServentiaCargoRedator(rs1.getString("SERV_CARGO_REDATOR") + " - " + rs1.getString("NOME_REDATOR"));
                    audienciaProcessoFisicoDt.setId_ServentiaRedator(rs1.getString("ID_SERV_REDATOR"));
                    audienciaProcessoFisicoDt.setServentiaRedator(rs1.getString("SERV_REDATOR"));  
                    audienciaProcessoFisicoDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
                    
                    ProcessoFisicoDt processoFisicoDt = new ProcessoFisicoDt();
                    
                    processoFisicoDt.setNumeroProcesso(rs1.getString("PROC_NUMERO_COMPLETO"));
                    processoFisicoDt.setPromovente(rs1.getString("PRIMEIRO_PROMOVENTE_RECORRENTE"));
                    processoFisicoDt.setPromovido(rs1.getString("PRIMEIRO_PROMOVIDO_RECORRIDO"));
                    
                    audienciaProcessoFisicoDt.setProcessoNumero(Funcoes.formataNumeroDigitoProcesso(processoFisicoDt.getNumeroProcesso()));

                    audienciaProcessoFisicoDt.setProcessoFisicoDt(processoFisicoDt);
                    
                    // Incluíndo objeto do tipo "AudienciaProcessoDt" na lista
                    audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoFisicoDt);
            	}
            }

            audienciaAnterior = rs1.getString("ID_AUDI");
            processoFisicoAnterior = rs1.getString("CODIGO_TEMP_AUDI_PROC");
        }
        try{if (rs1 != null) rs1.close();} catch(Exception e1) {}

        if (contadorSQL > 0) {
            // Busca as partes para cada processo encontrado anteriormente
            String sql = " SELECT p.ID_PROC_PARTE, p.NOME, p.PROC_PARTE_TIPO_CODIGO, p.ID_PROC, p.PROC_PARTE_TIPO FROM PROJUDI.VIEW_PROC_PARTE p";
            sql += " WHERE p.DATA_BAIXA IS NULL AND (";
            for (int i = 0; i < contadorSQL; i++) {
            	if(i > 0) {
            		sql += " OR ";
            	}
            	sql += " p.ID_PROC = ? ";
			}
            sql += " ) ";
            rs1 = consultar(sql, ps);
            while (rs1.next()) {
                String id_Processo = rs1.getString("ID_PROC");
                ProcessoDt tempProcessoDt = (ProcessoDt) mapProcesso.get(id_Processo);

                ProcessoParteDt parteDt = new ProcessoParteDt();
                parteDt.setId(rs1.getString("ID_PROC_PARTE"));
                parteDt.setNome(rs1.getString("NOME"));
                parteDt.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));

                // Adiciona parte a lista correspondente
                switch (Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"))) {
                case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):                
                    tempProcessoDt.addListaPoloAtivo(parteDt);
                    break;
                case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):                
                    tempProcessoDt.addListaPolosPassivos(parteDt);
                    break;
                }
            }
            try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return listaAudiencias;
    }
    
    private List getListaAudienciaAgendadaAdvogado(ResultSetTJGO rs1, Boolean buscaTodasPartes) throws Exception{
        List listaAudiencias = new ArrayList();
        AudienciaDt audienciaDt = null;
        ProcessoDt processoDt = null;
        String audienciaAnterior = "";
        String processoAnterior = "";
        String processoFisicoAnterior = "";
        Map mapProcesso = new LinkedHashMap();
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        //contador criado para evitar erro do Oracle em casos onde
        //há mais de mil registros a serem consultados
        int contadorSQL = 0;
        
            // Criando objeto do tipo AudienciaDt. Só deverá preencher um novo
        while (rs1.next()) {
           // objeto de audiência se não for igual ao anterior
            if (!rs1.getString("ID_AUDI").equalsIgnoreCase(audienciaAnterior)) {
                audienciaDt = new AudienciaDt();
                listaAudiencias.add(audienciaDt);
                processoAnterior = "";
            }

            // SET AUDIENCIADT
            associarAudienciaDt(audienciaDt, rs1);

            // SET AUDIÊNCIAPROCESSODT E PROCESSODT
            if (rs1.getString("ID_PROC") != null) {
                if (!rs1.getString("ID_PROC").equalsIgnoreCase(processoAnterior)) {
                    AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
                    audienciaProcessoDt.setId(rs1.getString("ID_AUDI_PROC"));
                    audienciaProcessoDt.setId_Audiencia(rs1.getString("ID_AUDI"));
                    audienciaProcessoDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
                    audienciaProcessoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
                    audienciaProcessoDt.setNomeResponsavel(rs1.getString("NOME"));
                   
                    audienciaProcessoDt.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
                    audienciaProcessoDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
                    audienciaProcessoDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
                    audienciaProcessoDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI_PROC")));
                    audienciaProcessoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_AUDI_PROC"));
                    audienciaProcessoDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));

                    // Dados do processo
                    processoDt = new ProcessoDt();
                    processoDt.setId_Processo(rs1.getString("ID_PROC"));
                    processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
                    audienciaProcessoDt.setProcessoDt(processoDt);
                    
                    if (buscaTodasPartes) {
                    	// Concatena os processos encontrados para depois buscar as partes desses
                        contadorSQL++;
                        ps.adicionarLong(rs1.getString("ID_PROC"));
                        mapProcesso.put(processoDt.getId(), processoDt);                    	
                    } else {
                    	ProcessoParteDt primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE_RECORRENTE"));
                        if (primeiroPromoventeRecorrente != null) processoDt.addListaPoloAtivo(primeiroPromoventeRecorrente);
                        
                        ProcessoParteDt primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO_RECORRIDO"));
                        if (primeiroPromovidoRecorrido != null) processoDt.addListaPolosPassivos(primeiroPromovidoRecorrido);
                    }             

                    // Incluíndo objeto do tipo "AudienciaProcessoDt" na lista
                    audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
                }
            } else if (rs1.getString("CODIGO_TEMP_AUDI_PROC") != null) {
            	if (!rs1.getString("CODIGO_TEMP_AUDI_PROC").equalsIgnoreCase(processoFisicoAnterior)) {
            		AudienciaProcessoFisicoDt audienciaProcessoFisicoDt = new AudienciaProcessoFisicoDt();
                    audienciaProcessoFisicoDt.setId(rs1.getString("ID_AUDI_PROC"));
                    audienciaProcessoFisicoDt.setId_Audiencia(rs1.getString("ID_AUDI"));
                    audienciaProcessoFisicoDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
                    audienciaProcessoFisicoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
                    audienciaProcessoFisicoDt.setNomeResponsavel(rs1.getString("NOME"));
                    audienciaProcessoFisicoDt.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
                    audienciaProcessoFisicoDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
                    audienciaProcessoFisicoDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI_PROC")));
                    audienciaProcessoFisicoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_AUDI_PROC"));
                    audienciaProcessoFisicoDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
                    
                    ProcessoFisicoDt processoFisicoDt = new ProcessoFisicoDt();
                    
                    processoFisicoDt.setNumeroProcesso(rs1.getString("PROC_NUMERO_COMPLETO"));
                    processoFisicoDt.setPromovente(rs1.getString("PRIMEIRO_PROMOVENTE_RECORRENTE"));
                    processoFisicoDt.setPromovido(rs1.getString("PRIMEIRO_PROMOVIDO_RECORRIDO"));
                    
                    audienciaProcessoFisicoDt.setProcessoNumero(Funcoes.formataNumeroDigitoProcesso(processoFisicoDt.getNumeroProcesso()));

                    audienciaProcessoFisicoDt.setProcessoFisicoDt(processoFisicoDt);
                    
                    // Incluíndo objeto do tipo "AudienciaProcessoDt" na lista
                    audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoFisicoDt);
            	}
            }

            processoAnterior = rs1.getString("ID_PROC");
            audienciaAnterior = rs1.getString("ID_AUDI");
            processoFisicoAnterior = rs1.getString("CODIGO_TEMP_AUDI_PROC");
        }
        try{if (rs1 != null) rs1.close();} catch(Exception e1) {}

        if (contadorSQL > 0) {
            // Busca as partes para cada processo encontrado anteriormente
            String sql = " SELECT p.ID_PROC_PARTE, p.NOME, p.PROC_PARTE_TIPO_CODIGO, p.ID_PROC, p.PROC_PARTE_TIPO FROM PROJUDI.VIEW_PROC_PARTE p";
            sql += " WHERE p.DATA_BAIXA IS NULL AND (";
            for (int i = 0; i < contadorSQL; i++) {
            	if(i > 0) {
            		sql += " OR ";
            	}
            	sql += " p.ID_PROC = ? ";
			}
            sql += " ) ";
            rs1 = consultar(sql, ps);
            while (rs1.next()) {
                String id_Processo = rs1.getString("ID_PROC");
                ProcessoDt tempProcessoDt = (ProcessoDt) mapProcesso.get(id_Processo);

                ProcessoParteDt parteDt = new ProcessoParteDt();
                parteDt.setId(rs1.getString("ID_PROC_PARTE"));
                parteDt.setNome(rs1.getString("NOME"));
                parteDt.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));

                // Adiciona parte a lista correspondente
                switch (Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"))) {
                case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):                
                    tempProcessoDt.addListaPoloAtivo(parteDt);
                    break;
                case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):                
                    tempProcessoDt.addListaPolosPassivos(parteDt);
                    break;
                }
            }
            try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }
        return listaAudiencias;
    }    

    /**
     * Método que contém a SQL que irá consultar uma instância completa
     * (AudiênciaDt + AudiênciaProcessoDt) do objeto do tipo "AudiênciaDt", de
     * acordo com o id da audiência que se deseja buscar.
     * 
     * @author Keila Sousa Silva
     * @since 19/08/2009
     * @param id_Audiencia
     * @return AudienciaDt audienciaCompleta
     * @throws Exception
     */
    public AudienciaDt consultarAudienciaLivreCompleta(String id_Audiencia) throws Exception {
        AudienciaDt audienciaCompleta = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        // SQL
        String sql = null;
        sql = "SELECT ID_AUDI, AUDI_TIPO, ID_AUDI_TIPO, ID_SERV, SERV, DATA_AGENDADA, DATA_MOVI_AUDI, RESERVADA, ";
        sql += " CODIGO_TEMP, AUDI_TIPO_CODIGO, ID_AUDI_PROC, ID_AUDI_PROC_STATUS, AUDI_PROC_STATUS, ID_SERV_CARGO, ";
        sql += " SERV_CARGO, ID_PROC, PROC_NUMERO, DATA_MOVI_AUDI_PROC, AUDI_PROC_STATUS_CODIGO, ID_ARQ_FINALIZACAO ";
        sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA_PRIM_GRAU vac WHERE vac.ID_AUDI = ? ";
        ps.adicionarLong(id_Audiencia);

        // RESULTSET
        ResultSetTJGO rs1 = null;
        try{
            rs1 = consultar(sql, ps);

            if (rs1.next()) {
                audienciaCompleta = prepararAudienciaLivreCompleta(rs1);
            }
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return audienciaCompleta;
    }



    /**
     * Método que contém a SQL que irá consultar uma instância completa de
     * Audiência (AudiênciaDt + lista de "AudiênciaProcessoDt" + partes dos
     * processos) do bjeto do tipo "AudiênciaDt", de acordo com o id da
     * audiência que se deseja buscar.
     * 
     * @param id_Audiencia
     * @return AudienciaDt audienciaCompleta
     * @author msapaula
     */
    public AudienciaDt consultarAudienciaCompleta(String id_Audiencia) throws Exception {
        AudienciaDt audienciaCompleta = null;
        String sql = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        sql = obtenhaListaCamposViewAudiCompleta();
        sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA vac WHERE vac.ID_AUDI = ? ";
        ps.adicionarLong(id_Audiencia);
        sql += " ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC";

        // RESULTSET
        ResultSetTJGO rs1 = null;
        try{
            rs1 = consultar(sql,ps);

            List liTemp = getListaAudienciaAgendadaCompleta(rs1, false);
            if (liTemp != null && liTemp.size() > 0) audienciaCompleta = (AudienciaDt) liTemp.get(0);

        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return audienciaCompleta;
    }
    
	/**
     * Método que contém a SQL que irá consultar uma instância completa de
     * Audiência (AudiênciaDt + lista de "AudiênciaProcessoDt" + partes dos
     * processos) do bjeto do tipo "AudiênciaDt", de acordo com o id da
     * audiência que se deseja buscar.
     * 
     * @param id_Audiencia
     * @return AudienciaDt audienciaCompleta
     * @author msapaula
     */
    public AudienciaDt consultarAudienciaCompletaExcetoFinalizados(String id_Audiencia) throws Exception {
        AudienciaDt audienciaCompleta = null;
        String sql = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        sql = obtenhaListaCamposViewAudiCompleta();
        sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA vac WHERE vac.ID_AUDI = ? ";
        ps.adicionarLong(id_Audiencia);
        sql += " ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC";

        // RESULTSET
        ResultSetTJGO rs1 = null;
        try{
            rs1 = consultar(sql,ps);
			
			List liTemp = getListaAudienciaAgendadaCompleta(rs1, false);
            
			if (liTemp != null && liTemp.size() > 0) audienciaCompleta = (AudienciaDt) liTemp.get(0);

        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return audienciaCompleta;
    }

    // jvosantos - 05/07/2019 15:29 - Adicionar parâmetro para corrigir o erro de não trazer alguns processos
    /**
     * Método que contém a SQL que irá consultar uma instância completa de
     * Audiência (AudiênciaDt + lista de "AudiênciaProcessoDt" + partes dos
     * processos) do bjeto do tipo "AudiênciaDt", de acordo com o id da
     * audiência que se deseja buscar.
     * 
     * @param id_Audiencia
     * @return AudienciaDt audienciaCompleta
     * @author msapaula
     */
    public AudienciaDt consultarAudienciaCompletaExcetoFinalizadosSessaoVirtual(String id_Audiencia) throws Exception {
        AudienciaDt audienciaCompleta = null;
        String sql = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        
        sql = obtenhaListaCamposViewAudiCompletaSessaoVirtual();
        sql += ", (SELECT DISTINCT PT.PROC_TIPO FROM AUDI_PROC AP ";
        sql += "	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP.ID_AUDI_PROC "; 
        sql += "	INNER JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ";
        sql += " 	WHERE AP.ID_AUDI_PROC = VAC.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "; 
        sql += " 	(SELECT 1 FROM RECURSO R "; 
        sql += "		INNER JOIN AUDI_PROC AP1 ON AP1.ID_PROC = R.ID_PROC "; 
        sql += "		WHERE AP1.ID_AUDI_PROC = VAC.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO ";
        sql += ", (SELECT DISTINCT PT.PROC_TIPO FROM AUDI_PROC AP ";
        sql += "	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP.ID_AUDI_PROC "; 
        sql += "	INNER JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ";
        sql += " 	WHERE AP.ID_AUDI_PROC = VAC.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "; 
        sql += " 	(SELECT 1 FROM RECURSO R "; 
        sql += "		INNER JOIN AUDI_PROC AP1 ON AP1.ID_PROC = R.ID_PROC "; 
        sql += "		WHERE AP1.ID_AUDI_PROC = VAC.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO ";
        sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA vac WHERE vac.ID_AUDI = ? ";
        ps.adicionarLong(id_Audiencia);
        sql += " ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC";

        // RESULTSET
        ResultSetTJGO rs1 = null;
        try{
            rs1 = consultar(sql,ps);
			
			// jvosantos - 05/07/2019 15:29 - Adicionar parâmetro para corrigir o erro de não trazer alguns processos
            List<AudienciaDt> liTemp = getListaAudienciaAgendadaCompletaSessaoVirtual(rs1, false);            		
            
            if (liTemp != null && liTemp.size() > 0) audienciaCompleta = (AudienciaDt) liTemp.get(0);

        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return audienciaCompleta;
    }

    /**
     * Consulta os dados de uma AudiênciaProcesso necessário para movimentação,
     * devendo retornar dados da Audiencia, da AudienciaProcesso, Processo e
     * suas partes
     * 
     * @param id_AudienciaProcesso ,
     *            identificação da audiência que está sendo movimentada
     * @return AudienciaDt audienciaCompleta
     * @author msapaula
     */
    public AudienciaDt consultarAudienciaProcessoCompleta(String id_AudienciaProcesso) throws Exception {
        AudienciaDt audienciaCompleta = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        String sql = obtenhaListaCamposViewAudiCompleta();
        sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA vac WHERE vac.ID_AUDI_PROC = ? ";
        ps.adicionarLong(id_AudienciaProcesso);
        sql += " AND ROWNUM = 1";
        sql += " ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC";
        ResultSetTJGO rs1 = null;
        try{
            rs1 = consultar(sql, ps);

            List liTemp = getListaAudienciaAgendadaCompleta(rs1, false);
            if (liTemp != null && liTemp.size() > 0) {
            	audienciaCompleta = (AudienciaDt) liTemp.get(0);            	
            }

        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return audienciaCompleta;
    }

    /**
     * Atualiza os dados de uma Audiencia em virtude de uma movimentação
     * 
     * @param audienciaDt ,
     *            objeto com dados da audiência e a data da movimentação a ser
     *            setada
     * 
     * @author msapaula
     */
    public void alterarAudienciaMovimentacao(AudienciaDt audienciaDt) throws Exception {
        String Sql;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        Sql = "UPDATE PROJUDI.AUDI SET DATA_MOVI = ? ";
        ps.adicionarDateTime(audienciaDt.getDataMovimentacao());
        if(audienciaDt.getId_ArquivoFinalizacaoSessao() != null && audienciaDt.getId_ArquivoFinalizacaoSessao().trim().length() > 0){
        	Sql += " , ID_ARQ_FINALIZACAO = ? ";
        	ps.adicionarLong(audienciaDt.getId_ArquivoFinalizacaoSessao());
        }
        Sql += " WHERE ID_AUDI = ? ";
        ps.adicionarLong(audienciaDt.getId());
        
        executarUpdateDelete(Sql, ps);
        
        
    }

    /**
     * Método para associar os dados de "Audiencia" de acordo com campos
     * definidos nas visões para consulta de audiências
     * 
     * @param audienciaDt
     * @param rs
     * @throws Exception 
     */
    protected void associarAudienciaDt(AudienciaDt audienciaDt, ResultSetTJGO rs1) throws Exception{
        audienciaDt.setId(rs1.getString("ID_AUDI"));
        audienciaDt.setAudienciaTipo(rs1.getString("AUDI_TIPO"));
        audienciaDt.setId_AudienciaTipo(rs1.getString("ID_AUDI_TIPO"));
        audienciaDt.setId_Serventia(rs1.getString("ID_SERV"));
        audienciaDt.setServentia(rs1.getString("SERV"));
        audienciaDt.setDataAgendada(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
        audienciaDt.setDataMovimentacao(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_MOVI_AUDI")));
        audienciaDt.setReservada(Funcoes.FormatarLogico(rs1.getString("RESERVADA")));
        audienciaDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
        audienciaDt.setAudienciaTipoCodigo(rs1.getString("AUDI_TIPO_CODIGO"));
        audienciaDt.setId_ArquivoFinalizacaoSessao(rs1.getString("ID_ARQ_FINALIZACAO"));
        try {
        	if (rs1.contains("VIRTUAL")) {
            	audienciaDt.setVirtual(rs1.getBoolean("VIRTUAL"));	
            }        
            if (rs1.contains("SESSAO_INICIADA")) {
            	audienciaDt.setSessaoIniciada(rs1.getBoolean("SESSAO_INICIADA"));	
            }
        } catch (Exception e) {}               
    }

    /**
     * Método responsável por consultar Sessões de 2º grau de acordo com os
     * parâmetros informados na consulta por filtros.
     * 
     * @param id_Serventia
     * @param audienciaDtConsulta
     * @param statusAudiencia
     * @param posicaoPaginaAtual
     * @return List listaSessoes
     * 
     * @author msapaula
     */
    public List consultarSessoesFiltro(String id_Serventia, AudienciaDt audienciaDtConsulta, String statusAudiencia, String posicaoPaginaAtual) throws Exception {
        String sql, sqlComum = "";
        List listaSessoes = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        sql = " SELECT a.ID_AUDI, a.DATA_AGENDADA, a.DATA_MOVI, a.SERV, a.ID_ARQ_FINALIZACAO, a.VIRTUAL, a.SESSAO_INICIADA ";
        sqlComum += "FROM PROJUDI.VIEW_AUDI a";
        sqlComum += " WHERE a.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal());
        if (Funcoes.StringToInt(statusAudiencia) == AudienciaDt.ABERTA)
            sqlComum += " AND a.DATA_MOVI IS NULL";
        else if (Funcoes.StringToInt(statusAudiencia) == AudienciaDt.FINALIZADA) sqlComum += " AND a.DATA_MOVI is not null";
        sqlComum += " AND a.ID_SERV = ? ";
        ps.adicionarLong(id_Serventia);

        if (audienciaDtConsulta.getDataInicialConsulta().length() > 0){
        	sqlComum += " AND a.DATA_AGENDADA >= ? ";
        	ps.adicionarDateTime(audienciaDtConsulta.getDataInicialConsulta() + " 00:00:00");
        }
        if (audienciaDtConsulta.getDataFinalConsulta().length() > 0){
        	sqlComum += " AND a.DATA_AGENDADA <= ? ";
        	ps.adicionarDateTime(audienciaDtConsulta.getDataFinalConsulta() + " 23:59:59");
        }
        sql += sqlComum + " ORDER BY a.DATA_AGENDADA DESC, a.SERV, a.DATA_MOVI";        
        try{
            rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);

            while (rs1.next()) {
                AudienciaDt audienciaDt = new AudienciaDt();
                audienciaDt.setId(rs1.getString("ID_AUDI"));
                audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
                audienciaDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI")));
                audienciaDt.setServentia(rs1.getString("SERV"));
                audienciaDt.setId_ArquivoFinalizacaoSessao(rs1.getString("ID_ARQ_FINALIZACAO"));
                audienciaDt.setVirtual(rs1.getBoolean("VIRTUAL"));
                audienciaDt.setSessaoIniciada(rs1.getBoolean("SESSAO_INICIADA"));
                listaSessoes.add(audienciaDt);
            }
            // rs1.close();
            
            rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs2.next()) listaSessoes.add(rs2.getLong("QUANTIDADE"));
            
            // rs1.close();
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e) {}
        }
        return listaSessoes;
    }
    
    /**
     * Método responsável por consultar Sessões de 2º grau de acordo com os parâmetros informados na consulta por filtros.
     * Esse método irá retornar todas as sessões das serventias que tenham o gabinete do desembargador como relacionado.
     * 
     * @param id_Serventia
     * @param audienciaDtConsulta
     * @param statusAudiencia
     * @param posicaoPaginaAtual
     * @return List listaSessoes
     * 
     * @author msapaula
     */
    public List consultarSessoesFiltroCamaras(String id_ServentiaGabinete, AudienciaDt audienciaDtConsulta, String statusAudiencia, String posicaoPaginaAtual) throws Exception {
        String sql, sqlComum = "";
        List listaSessoes = new ArrayList();
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        sql = " SELECT a.ID_AUDI, a.DATA_AGENDADA, a.DATA_MOVI, a.SERV, a.ID_ARQ_FINALIZACAO, a.VIRTUAL, a.SESSAO_INICIADA ";
        
        sqlComum = "FROM PROJUDI.VIEW_AUDI a";
        
        sqlComum += " LEFT JOIN PROJUDI.SERV_RELACIONADA sr on a.ID_SERV = sr.ID_SERV_PRINC";
        sqlComum += " WHERE a.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal());
        if (Funcoes.StringToInt(statusAudiencia) == AudienciaDt.ABERTA)
            sqlComum += " AND a.DATA_MOVI IS NULL";
        else if (Funcoes.StringToInt(statusAudiencia) == AudienciaDt.FINALIZADA) sqlComum += " AND a.DATA_MOVI is not null";
        sqlComum += " AND sr.ID_SERV_REL = ? ";
        ps.adicionarLong(id_ServentiaGabinete);
        if (audienciaDtConsulta.getDataInicialConsulta().length() > 0){
        	sqlComum += " AND a.DATA_AGENDADA >= ? ";
        	ps.adicionarDateTime(audienciaDtConsulta.getDataInicialConsulta() + " 00:00:00");
        }
        if (audienciaDtConsulta.getDataFinalConsulta().length() > 0){
        	sqlComum += " AND a.DATA_AGENDADA <= ? ";
        	ps.adicionarDateTime(audienciaDtConsulta.getDataFinalConsulta() + " 23:59:59");
        }
        
        sql += sqlComum + " ORDER BY a.DATA_AGENDADA DESC, a.SERV, a.DATA_MOVI";        
        try{
            rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);

            while (rs1.next()) {
                AudienciaDt audienciaDt = new AudienciaDt();
                audienciaDt.setId(rs1.getString("ID_AUDI"));
                audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
                audienciaDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI")));
                audienciaDt.setServentia(rs1.getString("SERV"));
                audienciaDt.setId_ArquivoFinalizacaoSessao(rs1.getString("ID_ARQ_FINALIZACAO"));
                audienciaDt.setVirtual(rs1.getBoolean("VIRTUAL"));
                audienciaDt.setSessaoIniciada(rs1.getBoolean("SESSAO_INICIADA"));
                listaSessoes.add(audienciaDt);
            }
            
            rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            if (rs2.next()) listaSessoes.add(rs2.getLong("QUANTIDADE"));            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs2 != null) rs2.close();} catch(Exception e) {}
        }
        return listaSessoes;
    }
  
    /**
     * Método responsável por criar um relatório de listagem das audiências em
     * PDF.
     * 
     * @return
     * @throws Exception
     */
    public List relListagemAudienciasParaHoje(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta) throws Exception {
        List liTemp = new ArrayList();
        List liAudiencias = new ArrayList();
        // //System.out.println("..ps-relListagemAudienciasParaHoje()");

        liTemp = this.consultarAudienciasParaHoje(usuarioDt, audienciaDtConsulta, null, true, ORDENACAO_PADRAO, null);
        // //System.out.println("....Execução Query OK");

        for (int i = 0; i < liTemp.size(); i++) {
            if (liTemp.get(i) instanceof AudienciaDt) {
                AudienciaDt audienciaTemp = (AudienciaDt) liTemp.get(i);
                for (int j = 0; j < audienciaTemp.getListaAudienciaProcessoDt().size(); j++) {
                    AudienciaRelatorioDt audienciaRelatorio = new AudienciaRelatorioDt();
                    String promoventes = "";
                    String promovidos = "";
                    audienciaRelatorio.setTipoAudiencia(audienciaTemp.getAudienciaTipo());
                    AudienciaProcessoDt audiencia = (AudienciaProcessoDt) audienciaTemp.getListaAudienciaProcessoDt().get(j);
                    audienciaRelatorio.setHora(Funcoes.FormatarHora(audienciaTemp.getDataAgendada()));
                                        
                    if (audiencia.getProcessoDt() != null) {
                    	audienciaRelatorio.setNumeroProcesso(audiencia.getProcessoDt().getProcessoNumero());
                    	if (audiencia.getProcessoDt().getListaPolosAtivos() != null) {
                            for (int k = 0; k < audiencia.getProcessoDt().getListaPolosAtivos().size(); k++) {
                            	if (k < QUANTIDADE_PARTES_RELATORIO) {
                                    ProcessoParteDt parte = new ProcessoParteDt();
                                    parte = (ProcessoParteDt) audiencia.getProcessoDt().getListaPolosAtivos().get(k);
                                    if (promoventes.length() > 0) {
                                        promoventes += ", ";
                                    }
                                    promoventes += parte.getNome();
                            	}
                            }
                        }
                    	if (audiencia.getProcessoDt().getListaPolosPassivos() != null) {
                            for (int l = 0; l < audiencia.getProcessoDt().getListaPolosPassivos().size(); l++) {
                            	if (l < QUANTIDADE_PARTES_RELATORIO) {
                            		ProcessoParteDt parte = new ProcessoParteDt();
                                    parte = (ProcessoParteDt) audiencia.getProcessoDt().getListaPolosPassivos().get(l);
                                    if (promovidos.length() > 0) {
                                        promovidos += ", ";
                                    }
                                    promovidos += parte.getNome();	
                            	}                                
                            }
                        }
                    } else if (audiencia instanceof AudienciaProcessoFisicoDt) {
                    	ProcessoFisicoDt processoFisicoDt = ((AudienciaProcessoFisicoDt)audiencia).getProcessoFisicoDt();
                    	if (processoFisicoDt != null) {
                    		audienciaRelatorio.setNumeroProcesso(Funcoes.formataNumeroDigitoProcesso(processoFisicoDt.getNumeroProcesso()) + " F");
                    		promoventes = processoFisicoDt.getPromovente();
                    		promovidos = processoFisicoDt.getPromovido();
                    	}                    	
                    }                    
                    audienciaRelatorio.setPromoventes(promoventes);                    
                    audienciaRelatorio.setPromovidos(promovidos);

                    liAudiencias.add(audienciaRelatorio);
                }
            }

        }
        return liAudiencias;
    }

    /**
     * Método responsável por criar um relatório de listagem das audiências
     * pendentes em PDF.
     * 
     * @return
     * @throws Exception
     */
    public List relListagemAudienciasPendentes(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta) throws Exception{
        List liTemp = new ArrayList();
        List liAudiencias = new ArrayList();
        
        liTemp = this.consultarAudienciasPendentes(usuarioDt, audienciaDtConsulta, null, true, ORDENACAO_PADRAO, null);

        for (int i = 0; i < liTemp.size(); i++) {
            if (liTemp.get(i) instanceof AudienciaDt) {
                AudienciaDt audienciaTemp = (AudienciaDt) liTemp.get(i);
                for (int j = 0; j < audienciaTemp.getListaAudienciaProcessoDt().size(); j++) {
                    AudienciaRelatorioDt audienciaRelatorio = new AudienciaRelatorioDt();
                    String promoventes = "";
                    String promovidos = "";
                    audienciaRelatorio.setTipoAudiencia(audienciaTemp.getAudienciaTipo());
                    AudienciaProcessoDt audiencia = (AudienciaProcessoDt) audienciaTemp.getListaAudienciaProcessoDt().get(j);
                    audienciaRelatorio.setHora(audienciaTemp.getDataAgendada());
                    if (audiencia.getProcessoDt() != null) {
                    	audienciaRelatorio.setNumeroProcesso(audiencia.getProcessoDt().getProcessoNumero());
                    	if (audiencia.getProcessoDt().getListaPolosAtivos() != null) {
                            for (int k = 0; k < audiencia.getProcessoDt().getListaPolosAtivos().size(); k++) {
                            	if (k < QUANTIDADE_PARTES_RELATORIO){
                            		ProcessoParteDt parte = new ProcessoParteDt();
                                    parte = (ProcessoParteDt) audiencia.getProcessoDt().getListaPolosAtivos().get(k);
                                    if (promoventes.length() > 0) {
                                        promoventes += ", ";
                                    }
                                    promoventes += parte.getNome();
                            	}                                
                            }
                        }
                    	if (audiencia.getProcessoDt().getListaPolosPassivos() != null) {
                            for (int l = 0; l < audiencia.getProcessoDt().getListaPolosPassivos().size(); l++) {
                            	if (l < QUANTIDADE_PARTES_RELATORIO) {
                            		ProcessoParteDt parte = new ProcessoParteDt();
                                    parte = (ProcessoParteDt) audiencia.getProcessoDt().getListaPolosPassivos().get(l);
                                    if (promovidos.length() > 0) {
                                        promovidos += ", ";
                                    }
                                    promovidos += parte.getNome();
                            	}                                
                            }
                        }
                    } else if (audiencia instanceof AudienciaProcessoFisicoDt) {
                    	ProcessoFisicoDt processoFisicoDt = ((AudienciaProcessoFisicoDt)audiencia).getProcessoFisicoDt();
                    	if (processoFisicoDt != null) {
                    		audienciaRelatorio.setNumeroProcesso(Funcoes.formataNumeroDigitoProcesso(processoFisicoDt.getNumeroProcesso()) + " F");
                    		promoventes = processoFisicoDt.getPromovente();
                    		promovidos = processoFisicoDt.getPromovido();
                    	}                    	
                    }                    
                    audienciaRelatorio.setPromoventes(promoventes); 
                    audienciaRelatorio.setPromovidos(promovidos);
                    audienciaRelatorio.setStatus(audiencia.getAudienciaProcessoStatus());

                    liAudiencias.add(audienciaRelatorio);
                }
            }

        }

        return liAudiencias;
    }

    /**
     * Método responsável por criar um relatório de listagem das audiências
     * movimentadas na data de hoje em PDF.
     * 
     * @return
     * @throws Exception
     */
    public List relListagemAudienciasMovimentadasHoje(UsuarioDt usuarioDt, AudienciaDt audienciaDtConsulta) throws Exception{
        List liTemp = new ArrayList();
        List liAudiencias = new ArrayList();

        liTemp = this.consultarAudienciasMovimentadasHoje(usuarioDt, audienciaDtConsulta, null, true, ORDENACAO_PADRAO, null);

        for (int i = 0; i < liTemp.size(); i++) {
            if (liTemp.get(i) instanceof AudienciaDt) {
                AudienciaDt audienciaTemp = (AudienciaDt) liTemp.get(i);
                for (int j = 0; j < audienciaTemp.getListaAudienciaProcessoDt().size(); j++) {
                    AudienciaRelatorioDt audienciaRelatorio = new AudienciaRelatorioDt();
                    String promoventes = "";
                    String promovidos = "";
                    AudienciaProcessoDt audiencia = (AudienciaProcessoDt) audienciaTemp.getListaAudienciaProcessoDt().get(j);
                    audienciaRelatorio.setTipoAudiencia(audienciaTemp.getAudienciaTipo());
                    audienciaRelatorio.setHora(audienciaTemp.getDataAgendada());
                    
                    if (audiencia.getProcessoDt() != null) {
                    	audienciaRelatorio.setNumeroProcesso(audiencia.getProcessoDt().getProcessoNumero());
                    	if (audiencia.getProcessoDt().getListaPolosAtivos() != null) {
                            for (int k = 0; k < audiencia.getProcessoDt().getListaPolosAtivos().size(); k++) {
                            	if (k < QUANTIDADE_PARTES_RELATORIO)
                            	{
                            		ProcessoParteDt parte = new ProcessoParteDt();
                                    parte = (ProcessoParteDt) audiencia.getProcessoDt().getListaPolosAtivos().get(k);
                                    if (promoventes.length() > 0) {
                                        promoventes += ", ";
                                    }
                                    promoventes += parte.getNome();
                            	}                                
                            }
                        }
                    	if (audiencia.getProcessoDt().getListaPolosPassivos() != null) {
                            for (int l = 0; l < audiencia.getProcessoDt().getListaPolosPassivos().size(); l++) {
                            	if (l < QUANTIDADE_PARTES_RELATORIO){
                            		ProcessoParteDt parte = new ProcessoParteDt();
                                    parte = (ProcessoParteDt) audiencia.getProcessoDt().getListaPolosPassivos().get(l);
                                    if (promovidos.length() > 0) {
                                        promovidos += ", ";
                                    }
                                    promovidos += parte.getNome();
                            	}                                
                            }
                        }                    	
                    } else if (audiencia instanceof AudienciaProcessoFisicoDt) {
                    	ProcessoFisicoDt processoFisicoDt = ((AudienciaProcessoFisicoDt)audiencia).getProcessoFisicoDt();
                    	if (processoFisicoDt != null) {
                    		audienciaRelatorio.setNumeroProcesso(Funcoes.formataNumeroDigitoProcesso(processoFisicoDt.getNumeroProcesso()) + " F");
                    		promoventes = processoFisicoDt.getPromovente();
                    		promovidos = processoFisicoDt.getPromovido();
                    	}                    	
                    }

                    
                    audienciaRelatorio.setPromoventes(promoventes);
                    audienciaRelatorio.setPromovidos(promovidos);
                    audienciaRelatorio.setStatus(audiencia.getAudienciaProcessoStatus());

                    liAudiencias.add(audienciaRelatorio);
                }
            }

        }
        
        return liAudiencias;
    }
    
    /**
	 * Atualiza os dados de uma Audiencia em virtude de uma desativação
	 * 
	 * @param id_Audiencia, identificação da audiência
	 * 
	 * @author msapaula
	 */
	public void desativarAudiencia(String id_Audiencia) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.AUDI SET CODIGO_TEMP = ? ";
		ps.adicionarLong(AudienciaDt.DESATIVADA);
		Sql += " , DATA_MOVI = ? ";
		ps.adicionarDate((new Date()));
		Sql += " WHERE ID_AUDI = ? ";
		ps.adicionarLong(id_Audiencia);
		
		executarUpdateDelete(Sql, ps);
		
	}
	
	  /**
     * Método responsável por montar a lista de audiências com seu(s) respectivo(s) processo(s) e parte(s), retornando tanto as partes de processo
     * quanto as partes de recurso, nos casos de recurso ativo.
     * 
     * @author Marielli
     * @param rs1, resultado da consulta de audiências
     * @return List listaAudiencias, lista de objetos do tipo AudienciaDt contendo o(s) processo(s) e parte(s)
	 * @throws Exception 
     * @throws Exception
     */
    private List getListaAudienciaProcessoRecurso(ResultSetTJGO rs1) throws Exception{
        List listaAudiencias = new ArrayList();
        AudienciaDt audienciaDt = null;
        ProcessoDt processoDt = null;
        String audienciaAnterior = "";
        String processoAnterior = "";
        String audienciaProcessoAnterior = "";
        //String processos = "";
        //String recursos = "";
        //Map mapProcesso = new LinkedHashMap();
        
        //PreparedStatementTJGO psRecursos = new PreparedStatementTJGO();
		//PreparedStatementTJGO psProcessos = new PreparedStatementTJGO();
		
        while (rs1.next()) {
            // Criando objeto do tipo AudienciaDt. Só deverá preencher um novo objeto de audiência se não for igual ao anterior
            if (!rs1.getString("ID_AUDI").equalsIgnoreCase(audienciaAnterior)) {
                audienciaDt = new AudienciaDt();
                listaAudiencias.add(audienciaDt);
                processoAnterior = "";
                audienciaProcessoAnterior = "";
            }

            // SET AUDIENCIADT
            associarAudienciaDt(audienciaDt, rs1);

            // SET AUDIÊNCIAPROCESSODT E PROCESSODT
            if (rs1.getString("ID_PROC") != null && rs1.getString("ID_AUDI_PROC") != null) {
                if (!(rs1.getString("ID_PROC").equalsIgnoreCase(processoAnterior) && rs1.getString("ID_AUDI_PROC").equalsIgnoreCase(audienciaProcessoAnterior))) {
                	
                    AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
                    audienciaProcessoDt.setId(rs1.getString("ID_AUDI_PROC"));
                    audienciaProcessoDt.setId_Audiencia(rs1.getString("ID_AUDI"));
                    audienciaProcessoDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
                    audienciaProcessoDt.setServentiaCargo(rs1.getString("SERV_CARGO"));
                    audienciaProcessoDt.setNomeResponsavel(rs1.getString("NOME"));
                    audienciaProcessoDt.setId_AudienciaProcessoStatus(rs1.getString("ID_AUDI_PROC_STATUS"));
                    audienciaProcessoDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
                    audienciaProcessoDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
                    audienciaProcessoDt.setDataMovimentacao(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_MOVI_AUDI_PROC")));                    
                    audienciaProcessoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP_AUDI_PROC"));
                    audienciaProcessoDt.setId_AudienciaProcessoStatusAnalista(rs1.getString("ID_AUDI_PROC_STATUS_ANA"));
                    audienciaProcessoDt.setAudienciaProcessoStatusAnalista(rs1.getString("AUDI_PROC_STATUS_ANA"));
                    audienciaProcessoDt.setAudienciaProcessoStatusCodigoAnalista(rs1.getString("AUDI_PROC_STATUS_CODIGO_ANA"));
                    audienciaProcessoDt.setId_ArquivoAta(rs1.getString("ID_ARQ_ATA")); 
                    audienciaProcessoDt.setId_ArquivoAtaSessaoAdiada(rs1.getString("ID_ARQ_ATA_ADIAMENTO"));
                    audienciaProcessoDt.setId_ArquivoAtaSessaoIniciada(rs1.getString("ID_ARQ_ATA_INICIADO"));
                    audienciaProcessoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_AUDIENCIA"));
                    audienciaProcessoDt.setProcessoTipo(rs1.getString("PROC_TIPO_AUDIENCIA"));
                    audienciaProcessoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
                    audienciaProcessoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
                    if(!rs1.isNull("ID_PEND_VOTO")) audienciaProcessoDt.setId_PendenciaVotoRelator(rs1.getString("ID_PEND_VOTO"));
                    if(!rs1.isNull("ID_PEND_EMENTA")) audienciaProcessoDt.setId_PendenciaEmentaRelator(rs1.getString("ID_PEND_EMENTA"));
                    if(!rs1.isNull("ID_PEND_VOTO_REDATOR")) audienciaProcessoDt.setId_PendenciaVotoRedator(rs1.getString("ID_PEND_VOTO_REDATOR"));
                    if(!rs1.isNull("ID_PEND_EMENTA_REDATOR")) audienciaProcessoDt.setId_PendenciaEmentaRedator(rs1.getString("ID_PEND_EMENTA_REDATOR"));
                    
                    // Dados do processo
                    processoDt = new ProcessoDt();
                    processoDt.setId_Processo(rs1.getString("ID_PROC"));
                    processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
                    processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
                    processoDt.setRecursoDt(new RecursoDt());
                    try {
                    	processoDt.getRecursoDt().setProcessoTipoRecursoParteAtual(rs1.getString("PROC_TIPO_RECURSO_SECUNDARIO"));                    	
                    } catch(SQLException e) {}
                    audienciaProcessoDt.setProcessoDt(processoDt);
                    
                    ProcessoParteDt primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE_RECORRENTE"));
                    if (primeiroPromoventeRecorrente != null) processoDt.addListaPoloAtivo(primeiroPromoventeRecorrente);
                    
                    ProcessoParteDt primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO_RECORRIDO"));
                    if (primeiroPromovidoRecorrido != null) processoDt.addListaPolosPassivos(primeiroPromovidoRecorrido);  
                    
                    //Concatena os processos encontrados para depois buscar as partes desses
//    				if (processoDt.getId_Recurso().length() > 0){
//    					recursos += (recursos.length() > 0 ? ",?" : "?");
//    					psRecursos.adicionarLong(processoDt.getId_Recurso());
//    				}
//    				else {
//    					processos += (processos.length() > 0 ? ",?" : "?");
//    					psProcessos.adicionarLong(processoDt.getId());
//    				}
//    				//Chave dupla no map para permitir diferenciar processo com mais de um recurso
//    				mapProcesso.put(processoDt.getId() + processoDt.getId_Recurso(), processoDt);             

                    // Incluíndo objeto do tipo "AudienciaProcessoDt" na lista
                    audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
                }
            }

            processoAnterior = rs1.getString("ID_PROC");
            audienciaAnterior = rs1.getString("ID_AUDI");
            audienciaProcessoAnterior = rs1.getString("ID_AUDI_PROC");
        }
        try{if (rs1 != null) rs1.close();} catch(Exception e1) {}

//        if (processos.length() > 0) {
//			//Busca as partes para cada processo encontrado anteriormente
//			String sql = " SELECT p.ID_PROC_PARTE, p.NOME, p.PROC_PARTE_TIPO_CODIGO, p.ID_PROC FROM PROJUDI.VIEW_PROC_PARTE p";
//			sql += " WHERE p.ID_PROC IN (" + processos + ") AND p.DATA_BAIXA IS NULL";
//			rs1 = consultar(sql, psProcessos);
//			setPartesProcesso(rs1, mapProcesso, false);
//			rs1.close();
//		}
//
//		if (recursos.length() > 0) {
//			//Busca as partes para cada recurso inominado
//			String sql = " SELECT rp.ID_PROC_PARTE, rp.NOME, rp.PROC_PARTE_TIPO_CODIGO, rp.ID_PROC, rp.ID_RECURSO FROM PROJUDI.VIEW_RECURSO_PARTE rp";
//			sql += " WHERE rp.ID_RECURSO IN (" + recursos + ") AND rp.DATA_BAIXA IS NULL";
//			//Foi retirado pois se o recurso tinha voltado a origem nao aparecia as partes AND rp.DATA_RETORNO IS NULL";
//			rs1 = consultar(sql, psRecursos);
//			setPartesProcesso(rs1, mapProcesso, true);
//			rs1.close();
//		}
		
        return listaAudiencias;
    }
    
//    /**
//	 * Método que monta a lista de partes para cada audiência resultante da consulta
//	 * @param rs
//	 * @param mapProcesso
//	 * @throws Exception
//	 */
//	private void setPartesProcesso(ResultSetTJGO rs1, Map mapProcesso, boolean recurso) throws Exception {
//		while (rs1.next()) {
//			String id_Processo = rs1.getString("ID_PROC");
//			String id_Recurso = "";
//			if (recurso) id_Recurso = rs1.getString("ID_RECURSO");
//			ProcessoDt tempProcessoDt = (ProcessoDt) mapProcesso.get(id_Processo + id_Recurso);
//
//			ProcessoParteDt parteDt = new ProcessoParteDt();
//			parteDt.setId(rs1.getString("ID_PROC_PARTE"));
//			parteDt.setNome(rs1.getString("NOME"));
//
//			// Adiciona parte a lista correspondente
//			int tipo = Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
//			switch (tipo) {
//				case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
//				case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
//					tempProcessoDt.addListaPromoventes(parteDt);
//					break;
//				case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
//				case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
//					tempProcessoDt.addListaPromovidos(parteDt);
//					break;
//			}
//		}
//	}

	/**
	 * Método responsável em vincular o arquivo de ementa ao arquivo de acórdão
	 * 
	 * @param idAudienciaProcesso
	 * @param idArquivoEmenta
	 * @param idAquivoAcordao
	 * @author mmgomes
	 * @throws Exception
	 */
	public void vincularEmentaAcordaoSegundoGrau(String idAudienciaProcesso, String idArquivoEmenta, String idAquivoAcordao) throws Exception {
		String Sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		Sql = "INSERT INTO PROJUDI.EMENTA (ID_AUDI_PROC, ID_ARQ_EMENTA, ID_ARQ_ACORDAO) VALUES (?, ?, ?)";
		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(idArquivoEmenta);
		ps.adicionarLong(idAquivoAcordao);
		            
        executarInsert(Sql, "ID_EMENTA", ps);
        	
	}
	
	/**
	 * Método responsável em consultar a próxima sessão aberta para a camara
	 * 
	 * @param id_Serventia
	 * @param dataAudiencia
	 * @param isVirtual 
	 * @author mmgomes
	 * @throws Exception
	 */
    public AudienciaDt consultarProximaSessaoAberta(String id_Serventia, String dataAudiencia, boolean isVirtual) throws Exception {
        String sql = "";
        AudienciaDt audienciaDt = null;
        ResultSetTJGO rs = null;        
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        sql = " SELECT a.ID_AUDI, a.AUDI_TIPO, a.ID_AUDI_TIPO, a.ID_SERV, a.SERV, a.DATA_AGENDADA, a.DATA_MOVI, a.RESERVADA, a.CODIGO_TEMP, a.AUDI_TIPO_CODIGO, a.ID_ARQ_FINALIZACAO, a.VIRTUAL ";
        sql += " FROM PROJUDI.VIEW_AUDI a";
        sql += " WHERE a.AUDI_TIPO_CODIGO = ? "; ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal());
        sql += " AND a.DATA_MOVI IS NULL ";        
        sql += " AND a.ID_SERV = ? "; ps.adicionarLong(id_Serventia);       
        sql += " AND a.DATA_AGENDADA > ? "; ps.adicionarDateTime(dataAudiencia.substring(0, 10) + " 23:59:59"); 
    	sql += " AND NVL(a.VIRTUAL, 0) = ? "; ps.adicionarLong((isVirtual ? 1 : 0)); 
        sql += " ORDER BY a.DATA_AGENDADA ASC, a.SERV, a.DATA_MOVI";       
        
        try{
        	rs = consultar(sql, ps);
        	
            if (rs.next()){
            	audienciaDt = new AudienciaDt();
            	associarDt(audienciaDt, rs);   
            }
        } finally{
            try{
                if (rs != null) rs.close();
            } catch(Exception e) {}            
        }
        return audienciaDt;
    }
    
    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Relator somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param somentePendentesAssinatura
     * @return
     * @throws Exception
     */
    public List consultarSessoesPendentesRelatorPendentesAcordaoDesembargador(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception{        
        return consultarSessoesPendentesRelatorDesembargador(id_ServentiaCargo, id_Audiencia, true, somentePendentesAssinatura, somentePreAnalisadas);        
    }
    
    /**
     * Método que possui a sql responsável por consultar as Sessões Virtuais de 2º grau
     * pendentes por Relator somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param somentePendentesAssinatura
     * @return
     * @throws Exception
     */
    public List consultarSessoesVirtuaisPendentesRelatorPendentesAcordaoDesembargador(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception{        
        return consultarSessoesVirtuaisPendentesRelatorDesembargador(id_ServentiaCargo, id_Audiencia, true, somentePendentesAssinatura, somentePreAnalisadas);        
    }
    
    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Relator somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param somentePendentesAssinatura
     * @return
     * @throws Exception
     */
    public List consultarSessoesPendentesRelatorPendentesAcordaoJuizTurma(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAssinatura) throws Exception{        
        return consultarSessoesPendentesRelatorJuizTurma(id_ServentiaCargo, id_Audiencia, true, somentePendentesAssinatura);        
    }

    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Relator somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param posicaoPaginaAtual
     * @return
     * @throws Exception
     */
    public long consultarQuantidadeSessoesPendentesPendentesAcordao(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception {   	
          	
    	long quantidade = 0;
    	String sql;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO(); 

        sql = "  SELECT COUNT(1) AS QUANTIDADE ";
        sql += obtenhaconsultaSQLSessoesPendentesDesembargador(id_ServentiaCargo, "", id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas, ps);          

        try{
            rs1 = consultar(sql, ps);                
            if(rs1.next()) quantidade = rs1.getLong("QUANTIDADE");        
        } finally{
            try{if (rs1 != null) rs1.close(); } catch(Exception e) {}
        }
    	return quantidade;

    }
    
    /**
     * Método que possui a sql responsável por consultar as Sessões Virtuais de 2º grau
     * pendentes por Relator somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param posicaoPaginaAtual
     * @return
     * @throws Exception
     */
    public long consultarQuantidadeSessoesVirtuaisPendentesPendentesAcordao(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception {   	
          	
    	long quantidade = 0;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO(); 
        StringBuilder sql = new StringBuilder();
        
        sql.append(" SELECT ");
        sql.append(" 	COUNT(*) AS QUANTIDADE ");
        sql.append(" FROM ");
        sql.append(" 	PEND P ");
        sql.append(" INNER JOIN PEND_RESP PR ON P.ID_PEND = PR.ID_PEND "); 
        sql.append(" WHERE ");
        sql.append("	P.ID_PEND_TIPO = ( ");
        sql.append("			SELECT ID_PEND_TIPO ");
        sql.append(" 				FROM ");
        sql.append(" 					PEND_TIPO ");
        sql.append(" 				WHERE ");
        sql.append("	 				PEND_TIPO_CODIGO = ?) "); ps.adicionarLong(PendenciaTipoDt.APRECIADOS);
        sql.append("	 AND PR.ID_SERV_CARGO = ? "); ps.adicionarLong(id_ServentiaCargo);
        sql.append("	 AND P.CODIGO_TEMP IS NOT NULL ");
        
        sql.append("	 AND P.ID_PEND_STATUS = (SELECT PS.ID_PEND_STATUS FROM PEND_STATUS PS WHERE PS.PEND_STATUS_CODIGO = ?) "); ps.adicionarLong(somentePendentesAssinatura ? PendenciaStatusDt.ID_AGUARDANDO_RETORNO : ( somentePreAnalisadas ? PendenciaStatusDt.ID_PRE_ANALISADA : PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));

        try{
            rs1 = consultar(sql.toString(), ps);                
            if(rs1.next()) quantidade = rs1.getLong("QUANTIDADE");        
        } finally{
            try{if (rs1 != null) rs1.close(); } catch(Exception e) {}
        }
    	return quantidade;

    }
    
    
    
    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Relator ou somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia     
     * @param somentePendentesAcordao
     * @return
     * @throws Exception
     */
    private List consultarSessoesPendentesRelatorDesembargador(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception{
        String sql;
        List listaSessoesPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();      

        sql = "  SELECT ac.ID_AUDI, ac.ID_AUDI_TIPO, ac.AUDI_TIPO, ac.AUDI_TIPO_CODIGO, ac.ID_SERV, ac.SERV,";
        sql += " ac.DATA_AGENDADA, ac.ID_AUDI_PROC, ac.ID_AUDI_PROC_STATUS , ac.DATA_MOVI_AUDI,";
        sql += " ac.AUDI_PROC_STATUS, ac.AUDI_PROC_STATUS_CODIGO, ac.ID_SERV_CARGO, ac.SERV_CARGO,";
        sql += " ac.NOME, ac.ID_PROC, ac.PROC_NUMERO_COMPLETO, ac.RESERVADA, ac.DATA_MOVI_AUDI_PROC,";
        sql += " ac.ID_RECURSO, ac.CODIGO_TEMP ";
        sql += ",ac.CODIGO_TEMP_AUDI_PROC, ac.ID_AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_CODIGO_ANA, ID_ARQ_ATA, DATA_AUDI_ORIGINAL, ID_AUDI_PROC_ORIGEM, ID_ARQ_ATA_ADIAMENTO, ID_ARQ_ATA_INICIADO, ID_ARQ_FINALIZACAO, ";
        sql += " PRIMEIRO_PROMOVENTE_RECORRENTE, PRIMEIRO_PROMOVIDO_RECORRIDO, ac.ID_PROC_TIPO_AUDIENCIA, ac.PROC_TIPO_AUDIENCIA, ac.POLO_ATIVO, ac.POLO_PASSIVO, ac.ID_PEND_VOTO, ac.ID_PEND_EMENTA, ac.ID_PEND_VOTO_REDATOR, ac.ID_PEND_EMENTA_REDATOR ";
        sql += ",			(" + 
        		"	SELECT PROC_TIPO" + 
        		"	FROM" + 
        		"		PROJUDI.PROC_TIPO PT" + 
        		"	JOIN RECURSO_SECUNDARIO_PARTE RCP ON RCP.ID_PROC_TIPO_RECURSO_SEC = PT.ID_PROC_TIPO" + 
        		"	JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = RCP.ID_AUDI_PROC" + 
        		"	WHERE" + 
        		"		AP.DATA_MOVI IS NULL" + 
        		"		AND AP.ID_PROC = AC.ID_PROC "
        		+ "		AND ROWNUM=1) "
        		+ "		AS PROC_TIPO_RECURSO_SECUNDARIO";
        sql += obtenhaconsultaSQLSessoesPendentesDesembargador(id_ServentiaCargo, "", id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas, ps);        
        sql += " ORDER BY ID_AUDI, ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC, DATA_AGENDADA ASC, ID_PROC ASC";

        try{
            rs1 = consultar(sql, ps);
            // Chama método que retorna a lista de audiências já com os processos e as partes
            listaSessoesPendentes = getListaAudienciaProcessoRecurso(rs1);

        
        } finally{
            try{
                if (rs1 != null) rs1.close();
            } catch(Exception e) {
            }
        }
        return listaSessoesPendentes;
    }
    
    
    
    /**
     * Método que possui a sql responsável por consultar as Sessões Virtuais de 2º grau
     * pendentes por Relator ou somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia     
     * @param somentePendentesAcordao
     * @return
     * @throws Exception
     */
    private List consultarSessoesVirtuaisPendentesRelatorDesembargador(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception{
        StringBuilder sql = new StringBuilder();
        List listaSessoesPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();      

        sql.append(" SELECT ");
        sql.append(" 	ac.ID_AUDI, ");
        sql.append(" 	ac.ID_AUDI_TIPO, ");
        sql.append(" 	ac.AUDI_TIPO, ");
        sql.append(" 	ac.AUDI_TIPO_CODIGO, ");
        sql.append(" 	ac.ID_SERV, ");
        sql.append(" 	ac.SERV, ");
        sql.append(" 	ac.DATA_AGENDADA, ");
        sql.append(" 	ac.ID_AUDI_PROC, ");
        sql.append(" 	ac.ID_AUDI_PROC_STATUS, ");
        sql.append(" 	ac.DATA_MOVI_AUDI, ");
        sql.append(" 	ac.AUDI_PROC_STATUS, ");
        sql.append(" 	ac.AUDI_PROC_STATUS_CODIGO, ");
        sql.append(" 	ac.ID_SERV_CARGO, ");
        sql.append(" 	ac.SERV_CARGO, ");
        sql.append(" 	ac.NOME, ");
        sql.append(" 	ac.ID_PROC, ");
        sql.append(" 	ac.PROC_NUMERO_COMPLETO, ");
        sql.append(" 	ac.RESERVADA, ");
        sql.append(" 	ac.DATA_MOVI_AUDI_PROC, ");
        sql.append(" 	ac.ID_RECURSO, ");
        sql.append(" 	ac.CODIGO_TEMP, ");
        sql.append(" 	ac.CODIGO_TEMP_AUDI_PROC, ");
        sql.append(" 	ac.ID_AUDI_PROC_STATUS_ANA, ");
        sql.append(" 	ac.AUDI_PROC_STATUS_ANA, ");
        sql.append(" 	ac.AUDI_PROC_STATUS_CODIGO_ANA, ");
        sql.append(" 	ac.VIRTUAL, ");
        sql.append(" 	ID_ARQ_ATA, ");
        sql.append(" 	DATA_AUDI_ORIGINAL, ");
        sql.append(" 	ID_AUDI_PROC_ORIGEM, ");
        sql.append(" 	ID_ARQ_ATA_ADIAMENTO, ");
        sql.append(" 	ID_ARQ_ATA_INICIADO, ");
        sql.append(" 	ID_ARQ_FINALIZACAO, ");
        sql.append(" 	PRIMEIRO_PROMOVENTE_RECORRENTE, ");
        sql.append(" 	PRIMEIRO_PROMOVIDO_RECORRIDO, ");
        sql.append(" 	ac.ID_PROC_TIPO_AUDIENCIA, ");
        sql.append(" 	ac.PROC_TIPO_AUDIENCIA, ");
        sql.append(" 	ac.POLO_ATIVO, ");
        sql.append(" 	ac.POLO_PASSIVO, ");
        sql.append(" 	ac.ID_PEND_VOTO, ");
        sql.append(" 	ac.ID_PEND_EMENTA, ");
        sql.append(" 	ac.ID_PEND_VOTO_REDATOR, ");
        sql.append(" 	ac.ID_PEND_EMENTA_REDATOR, ");
        sql.append(" 	( ");
        sql.append(" 		SELECT ");
        sql.append(" 			PROC_TIPO ");
        sql.append(" 		FROM ");
        sql.append(" 			PROJUDI.PROC_TIPO PT ");
        sql.append(" 		JOIN RECURSO_SECUNDARIO_PARTE RCP ON ");
        sql.append(" 			RCP.ID_PROC_TIPO_RECURSO_SEC = PT.ID_PROC_TIPO ");
        sql.append(" 		JOIN AUDI_PROC AP ON ");
        sql.append(" 			AP.ID_AUDI_PROC = RCP.ID_AUDI_PROC ");
        sql.append(" 		WHERE ");
        sql.append(" 			AP.DATA_MOVI IS NULL ");
        sql.append(" 			AND AP.ID_PROC = AC.ID_PROC ");
        sql.append(" 			AND ROWNUM = 1) AS PROC_TIPO_RECURSO_SECUNDARIO ");
        sql.append(" 	FROM ");
        sql.append(" 		PEND P ");
        sql.append(" 	INNER JOIN PEND_RESP PR ON P.ID_PEND = PR.ID_PEND "); 
        sql.append(" 	INNER JOIN 	PROJUDI.VIEW_AUDI_COMPLETA AC ON P.CODIGO_TEMP = AC.ID_AUDI_PROC ");
        sql.append(" 	WHERE ");
        sql.append(" 		P.ID_PEND_TIPO = ( ");
        sql.append(" 			SELECT ID_PEND_TIPO ");
        sql.append(" 			FROM ");
        sql.append(" 				PEND_TIPO ");
        sql.append(" 			WHERE ");
        sql.append(" 				PEND_TIPO_CODIGO = ?) "); ps.adicionarLong(PendenciaTipoDt.APRECIADOS);
        sql.append(" 		AND PR.ID_SERV_CARGO = ? "); ps.adicionarLong(id_ServentiaCargo);
        sql.append(" 		AND P.CODIGO_TEMP IS NOT NULL ");
        sql.append(" 		AND AC.VIRTUAL = ? "); ps.adicionarBoolean(true);
        
        if(StringUtils.isNotEmpty(id_Audiencia)) {
        	sql.append(" 		AND AC.ID_AUDI = ? "); ps.adicionarLong(id_Audiencia);
        }
        
        sql.append("	    AND P.ID_PEND_STATUS = (SELECT PS.ID_PEND_STATUS FROM PEND_STATUS PS WHERE PS.PEND_STATUS_CODIGO = ?) "); ps.adicionarLong(somentePendentesAssinatura ? PendenciaStatusDt.ID_AGUARDANDO_RETORNO : (somentePreAnalisadas ? PendenciaStatusDt.ID_PRE_ANALISADA : PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO));
        
        sql.append(" 	ORDER BY ");
        sql.append(" 		ID_AUDI, ");
        sql.append(" 		ORDEM_2_GRAU, ");
        sql.append(" 		PROC_TIPO, ");
        sql.append(" 		ID_AUDI_PROC, ");
        sql.append(" 		DATA_AGENDADA ASC, ");
        sql.append(" 		ID_PROC ASC ");
        
        try{
            rs1 = consultar(sql.toString(), ps);
            // Chama método que retorna a lista de audiências já com os processos e as partes
            listaSessoesPendentes = getListaAudienciaProcessoRecurso(rs1);

        
        } finally{
            try{
                if (rs1 != null) rs1.close();
            } catch(Exception e) {
            }
        }
        return listaSessoesPendentes;
    }
    
    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Relator ou somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia     
     * @param somentePendentesAcordao
     * @return
     * @throws Exception
     */
    private List consultarSessoesPendentesRelatorJuizTurma(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura) throws Exception{
        String sql;
        List listaSessoesPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();      

        sql = "  SELECT ac.ID_AUDI, ac.ID_AUDI_TIPO, ac.AUDI_TIPO, ac.AUDI_TIPO_CODIGO, ac.ID_SERV, ac.SERV,";
        sql += " ac.DATA_AGENDADA, ac.ID_AUDI_PROC, ac.ID_AUDI_PROC_STATUS , ac.DATA_MOVI_AUDI,";
        sql += " ac.AUDI_PROC_STATUS, ac.AUDI_PROC_STATUS_CODIGO, ac.ID_SERV_CARGO, ac.SERV_CARGO,";
        sql += " ac.NOME, ac.ID_PROC, ac.PROC_NUMERO_COMPLETO, ac.RESERVADA, ac.DATA_MOVI_AUDI_PROC,";
        sql += " ac.ID_RECURSO, ac.CODIGO_TEMP ";
        sql += ",ac.CODIGO_TEMP_AUDI_PROC, ac.ID_AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_CODIGO_ANA, ID_ARQ_ATA, DATA_AUDI_ORIGINAL, ID_ARQ_ATA_ADIAMENTO, ID_ARQ_ATA_INICIADO, ID_ARQ_FINALIZACAO, ";
        sql += " PRIMEIRO_PROMOVENTE_RECORRENTE, PRIMEIRO_PROMOVIDO_RECORRIDO, ac.ID_PROC_TIPO_AUDIENCIA, ac.PROC_TIPO_AUDIENCIA, ac.POLO_ATIVO, ac.POLO_PASSIVO, ac.ID_PEND_VOTO, ac.ID_PEND_EMENTA, ac.ID_PEND_VOTO_REDATOR, ac.ID_PEND_EMENTA_REDATOR, ID_AUDI_PROC_ORIGEM ";
        sql += obtenhaconsultaSQLSessoesPendentesJuizTurma(id_ServentiaCargo, id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, ps);        
        sql += " ORDER BY ID_AUDI, ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC, DATA_AGENDADA ASC, ID_PROC ASC";

        try{
            rs1 = consultar(sql, ps);
            // Chama método que retorna a lista de audiências já com os processos e as partes
            listaSessoesPendentes = getListaAudienciaProcessoRecurso(rs1);

        
        } finally{
            try{
                if (rs1 != null) rs1.close();
            } catch(Exception e) {
            }
        }
        return listaSessoesPendentes;
    }
    
    /**
     * Consulta sql comum às consultas de sessões de segundo grau.
     * 
     * @param id_ServentiaCargoDesembargador
     * @param id_ServentiaCargoAssistente
     * @param id_Audiencia
     * @param somentePendentesAcordao
     * @param somentePendentesAssinatura
     * @param somentePreAnalisadas
     * @param ps
     * @return
     * @throws Exception
     */
    private String obtenhaconsultaSQLSessoesPendentesDesembargador(String id_ServentiaCargoDesembargador, 
    		                                                       String id_ServentiaCargoAssistente, 
    		                                                       String id_Audiencia, 
    		                                                       boolean somentePendentesAcordao, 
    		                                                       boolean somentePendentesAssinatura, 
    		                                                       boolean somentePreAnalisadas, 
    		                                                       PreparedStatementTJGO ps) throws Exception{
        String sql;
        
        if (Funcoes.StringToLong(id_ServentiaCargoDesembargador) == 0 && Funcoes.StringToLong(id_ServentiaCargoAssistente) == 0) {
        	throw new MensagemException("Deve ser informado o cargo do desembargador ou o cargo do assistente de gabinete");
        }
         
        sql = " FROM PROJUDI.VIEW_AUDI_COMPLETA ac";
        sql += " WHERE ac.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
        sql += " AND ac.VIRTUAL IS NULL "; // jvosantos - 07/08/2019 16:40 - Adicionar condição para trazer apenas as sessões presenciais
        sql += " AND ac.AUDI_PROC_STATUS_CODIGO = ? ";
        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
        sql += " AND ac.ID_PROC IS NOT NULL AND ac.DATA_MOVI_AUDI_PROC IS NULL";
        if (id_ServentiaCargoDesembargador != null && id_ServentiaCargoDesembargador.trim().length() > 0){         
        	sql += " AND ( ";
        	sql += "       ( ";
        	sql += "         (AC.ID_SERV_CARGO_REDATOR IS NULL OR AC.ID_SERV_CARGO_REDATOR = ?) "; ps.adicionarLong(0);        	
        	sql += "          AND AC.ID_SERV_CARGO = ? "; ps.adicionarLong(id_ServentiaCargoDesembargador);
        	sql += "       ) ";
        	sql += "   OR (AC.ID_SERV_CARGO_REDATOR = ?) "; ps.adicionarLong(id_ServentiaCargoDesembargador);        	
            sql += " ) ";
        } 
        if (id_Audiencia != null && id_Audiencia.length() > 0) {
        	sql += " AND ac.ID_AUDI = ? ";
        	ps.adicionarLong(id_Audiencia);        	
        }
        if (somentePendentesAcordao){
        	sql += " AND (NOT ac.ID_ARQ_ATA IS NULL AND ac.ID_ARQ_ATA > ?) ";
        	ps.adicionarLong(0);     
        	
        	sql += " AND ";
        	if (!somentePendentesAssinatura) {
        		sql += " NOT ";	
        	}
        	sql += " EXISTS (SELECT 1";
        	sql += "           FROM PROJUDI.PEND P INNER JOIN PROJUDI.PEND_ARQ PA ON PA.ID_PEND = P.ID_PEND AND PA.RESPOSTA = ?"; ps.adicionarLong(1); 
        	sql += "          WHERE (P.ID_PEND = ac.ID_PEND_VOTO OR P.ID_PEND = ac.ID_PEND_VOTO_REDATOR)";
        	sql += "           AND PA.CODIGO_TEMP = ? "; ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);
        	sql += "           AND P.DATA_FIM IS NULL";
        	sql += "           AND P.ID_USU_FINALIZADOR IS NULL";
        	sql += ")";
        }        
        
        if (somentePreAnalisadas) {
        	sql += " AND (";            
        	sql += "     EXISTS (SELECT 1";
        	sql += "               FROM PROJUDI.PEND P INNER JOIN PROJUDI.PEND_ARQ PA ON PA.ID_PEND = P.ID_PEND"; 
        	sql += "              INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = P.ID_PEND INNER JOIN PROJUDI.SERV_CARGO SC ON SC.ID_SERV_CARGO = PR.ID_SERV_CARGO ";
        	sql += "              WHERE P.ID_PEND = ac.ID_PEND_VOTO";
        	sql += "               AND P.DATA_FIM IS NULL";
        	sql += "               AND P.ID_USU_FINALIZADOR IS NULL ";
        	if (id_ServentiaCargoDesembargador != null && id_ServentiaCargoDesembargador.trim().length() > 0){  
        		sql += "           AND sc.ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargoDesembargador);
        	} else if (id_ServentiaCargoAssistente != null && id_ServentiaCargoAssistente.trim().length() > 0){  
        		sql += "           AND sc.ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargoAssistente);        		
        	} 
        	sql += "               AND NVL(P.CODIGO_TEMP, 0) <> ? "; ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
    		sql += "               AND (SELECT COUNT(1) FROM PEND_ARQ pa1 WHERE pa1.ID_ARQ = pa.ID_ARQ ) = ?"; ps.adicionarLong(1);
    		sql += "             ) ";
    		sql += " OR EXISTS (SELECT 1";
        	sql += "               FROM PROJUDI.PEND P INNER JOIN PROJUDI.PEND_ARQ PA ON PA.ID_PEND = P.ID_PEND"; 
        	sql += "              INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = P.ID_PEND INNER JOIN PROJUDI.SERV_CARGO SC ON SC.ID_SERV_CARGO = PR.ID_SERV_CARGO ";
        	sql += "              WHERE P.ID_PEND = ac.ID_PEND_VOTO_REDATOR";
        	sql += "               AND P.DATA_FIM IS NULL";
        	sql += "               AND P.ID_USU_FINALIZADOR IS NULL ";
        	if (id_ServentiaCargoDesembargador != null && id_ServentiaCargoDesembargador.trim().length() > 0){  
        		sql += "           AND sc.ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargoDesembargador);
        	} else if (id_ServentiaCargoAssistente != null && id_ServentiaCargoAssistente.trim().length() > 0){  
        		sql += "           AND sc.ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargoAssistente);
        		        	}
    		sql += "               AND (SELECT COUNT(1) FROM PEND_ARQ pa1 WHERE pa1.ID_ARQ = pa.ID_ARQ ) = ?"; ps.adicionarLong(1);
    		sql += "           ) ";
    		sql += "   ) ";
    	} else {
    		sql += " AND (";     
    		sql += "   EXISTS (SELECT 1";
        	sql += "               FROM PROJUDI.PEND P INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = P.ID_PEND INNER JOIN PROJUDI.SERV_CARGO SC ON SC.ID_SERV_CARGO = PR.ID_SERV_CARGO"; 
        	sql += "              WHERE P.ID_PEND = ac.ID_PEND_VOTO";
        	sql += "               AND P.DATA_FIM IS NULL";
        	sql += "               AND P.ID_USU_FINALIZADOR IS NULL";  
        	if (id_ServentiaCargoDesembargador != null && id_ServentiaCargoDesembargador.trim().length() > 0){  
        		sql += "           AND sc.ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargoDesembargador);
        	} else if (id_ServentiaCargoAssistente != null && id_ServentiaCargoAssistente.trim().length() > 0){  
        		sql += "           AND sc.ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargoAssistente);        		
        	}
        	sql += "               AND NVL(P.CODIGO_TEMP, 0) <> ? "; ps.adicionarLong(PendenciaDt.VOTO_VENCIDO_RELATOR);
    		sql += "               AND p.ID_PEND not in (SELECT pa.ID_PEND FROM PROJUDI.PEND_ARQ pa WHERE pa.RESPOSTA = ?)"; ps.adicionarLong(1);
        	sql += "        ) ";
        	sql += " OR EXISTS (SELECT 1";
        	sql += "               FROM PROJUDI.PEND P INNER JOIN PROJUDI.PEND_RESP pr on pr.ID_PEND = P.ID_PEND INNER JOIN PROJUDI.SERV_CARGO SC ON SC.ID_SERV_CARGO = PR.ID_SERV_CARGO"; 
        	sql += "              WHERE P.ID_PEND = ac.ID_PEND_VOTO_REDATOR";
        	sql += "               AND P.DATA_FIM IS NULL";
        	sql += "               AND P.ID_USU_FINALIZADOR IS NULL";  
        	if (id_ServentiaCargoDesembargador != null && id_ServentiaCargoDesembargador.trim().length() > 0){  
        		sql += "           AND sc.ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargoDesembargador);
        	} else if (id_ServentiaCargoAssistente != null && id_ServentiaCargoAssistente.trim().length() > 0){  
        		sql += "           AND sc.ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargoAssistente);
        	}
    		sql += "               AND p.ID_PEND not in (SELECT pa.ID_PEND FROM PROJUDI.PEND_ARQ pa WHERE pa.RESPOSTA = ?)"; ps.adicionarLong(1);
        	sql += "            ) ";
        	sql += "    ) ";
    	}
    	
        return sql;
    }
    
    /**
     * 
     * Consulta sql comum às consultas de sessões da turma.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param somentePendentesAcordao
     * @param ps
     * @return
     * @throws Exception
     * @author mmgomes 
     */
    private String obtenhaconsultaSQLSessoesPendentesJuizTurma(String id_ServentiaCargoJuizTurma, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, PreparedStatementTJGO ps) throws Exception{
        String sql;
         
        sql = " FROM PROJUDI.VIEW_AUDI_COMPLETA ac";
        sql += " WHERE ac.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
        sql += " AND ac.AUDI_PROC_STATUS_CODIGO = ? ";
        ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
        sql += " AND ac.ID_PROC IS NOT NULL AND ac.DATA_MOVI_AUDI_PROC IS NULL";
        if (id_ServentiaCargoJuizTurma != null && id_ServentiaCargoJuizTurma.trim().length() > 0){         
        	sql += " AND ( ";
        	sql += "       ( ";
        	sql += "         (AC.ID_SERV_CARGO_REDATOR IS NULL OR AC.ID_SERV_CARGO_REDATOR = ?) "; ps.adicionarLong(0);        	
        	sql += "          AND AC.ID_SERV_CARGO = ? "; ps.adicionarLong(id_ServentiaCargoJuizTurma);
        	sql += "       ) ";
        	sql += "   OR (AC.ID_SERV_CARGO_REDATOR = ?) "; ps.adicionarLong(id_ServentiaCargoJuizTurma);        	
            sql += " ) ";
        } 
        if (id_Audiencia != null && id_Audiencia.length() > 0) {
        	sql += " AND ac.ID_AUDI = ? ";
        	ps.adicionarLong(id_Audiencia);        	
        }
        if (somentePendentesAcordao){
        	sql += " AND (NOT ac.ID_ARQ_ATA IS NULL AND ac.ID_ARQ_ATA > ?) ";
        	ps.adicionarLong(0);     
        	
        	sql += " AND ";
        	if (!somentePendentesAssinatura) {
        		sql += " NOT ";	
        	}
        	sql += " EXISTS (SELECT 1";
        	sql += "           FROM PROJUDI.PEND P INNER JOIN PROJUDI.PEND_ARQ PA ON PA.ID_PEND = P.ID_PEND AND PA.RESPOSTA = ?"; ps.adicionarLong(1); 
        	sql += "          WHERE (P.ID_PEND = ac.ID_PEND_VOTO OR P.ID_PEND = ac.ID_PEND_VOTO_REDATOR)";
        	sql += "           AND PA.CODIGO_TEMP = ? "; ps.adicionarLong(PendenciaArquivoDt.AGUARDANDO_ASSINATURA);        	
        	sql += ")";
        }        
            
        return sql;
    }
    
    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Assistente de Gabinete somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param posicaoPaginaAtual
     * @return
     * @throws Exception
     */
    public long consultarQuantidadeSessoesPendentesPendentesAcordaoAssistenteGabinete(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception {   	
           	
    	long quantidade = 0;
    	String sql;
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO(); 

        sql = "  SELECT COUNT(1) AS QUANTIDADE ";
        sql += obtenhaconsultaSQLSessoesPendentesAssistenteGabinete(id_ServentiaCargo, id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas, ps);          

        try{
            rs1 = consultar(sql, ps);                
            if(rs1.next()) quantidade = rs1.getLong("QUANTIDADE");        
        } finally{
            try{if (rs1 != null) rs1.close(); } catch(Exception e) {}
        }
    	return quantidade;
        
    }   
    
    /**
     * Método que possui a sql responsável por consultar as Sessões virtuais de 2º grau
     * pendentes por Assistente de Gabinete somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param posicaoPaginaAtual
     * @return
     * @throws Exception
     */
    public long consultarQuantidadeSessoesVirtuaisPendentesPendentesAcordaoAssistenteGabinete(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception {   	
           	
    	long quantidade = 0;
    	StringBuilder sql = new StringBuilder();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO(); 
        
        sql.append(" SELECT ");
        sql.append(" 	* ");
        sql.append(" FROM ");
        sql.append(" 	PEND P ");
        sql.append(" INNER JOIN PEND_RESP PR ON P.ID_PEND = PR.ID_PEND "); 
        sql.append(" WHERE ");
        sql.append("	P.ID_PEND_TIPO = ( ");
        sql.append("			SELECT ID_PEND_TIPO ");
        sql.append(" 				FROM ");
        sql.append(" 					PEND_TIPO ");
        sql.append(" 				WHERE ");
        sql.append("	 				PEND_TIPO_CODIGO = ?) "); ps.adicionarLong(PendenciaTipoDt.APRECIADOS);
        sql.append("	 AND PR.ID_SERV_CARGO = ? "); ps.adicionarLong(id_ServentiaCargo);
        sql.append("	 AND P.CODIGO_TEMP IS NOT NULL ");
                
        try{
            rs1 = consultar(sql.toString(), ps);
            if(rs1.next()) quantidade = rs1.getLong("QUANTIDADE");
        } finally{
            try{if (rs1 != null) rs1.close(); } catch(Exception e) {}
        }
    	return quantidade;
        
    }   
    
    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Assistentes de Gabinete somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param posicaoPaginaAtual
     * @return
     * @throws Exception
     */
    public List consultarSessoesPendentesAssistenteGabinetePendentesAcordao(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception{
        
        return consultarSessoesPendentesAssistenteGabinete(id_ServentiaCargo, id_Audiencia, true, somentePendentesAssinatura, somentePreAnalisadas);

    }
    
    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Assistentes de Gabinete.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param posicaoPaginaAtual
     * @return
     * @throws Exception
     */
    public List consultarSessoesPendentesAssistenteGabinete(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception{
        
        return consultarSessoesPendentesAssistenteGabinete(id_ServentiaCargo, id_Audiencia, false, somentePendentesAssinatura, somentePreAnalisadas);
 
    }
    
    /**
     * Método que possui a sql responsável por consultar as Sessões de 2º grau
     * pendentes por Assistente de Gabinete ou somente as que estão pendentes de inserção de acórdão.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia     
     * @param somentePendentesAcordao
     * @return
     * @throws Exception
     */
    private List consultarSessoesPendentesAssistenteGabinete(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception{
        String sql;
        List listaSessoesPendentes = new ArrayList();
        ResultSetTJGO rs1 = null;
        
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();      

        sql = "  SELECT ac.ID_AUDI, ac.ID_AUDI_TIPO, ac.AUDI_TIPO, ac.AUDI_TIPO_CODIGO, ac.ID_SERV, ac.SERV,";
        sql += " ac.DATA_AGENDADA, ac.ID_AUDI_PROC, ac.ID_AUDI_PROC_STATUS , ac.DATA_MOVI_AUDI,";
        sql += " ac.AUDI_PROC_STATUS, ac.AUDI_PROC_STATUS_CODIGO, ac.ID_SERV_CARGO, ac.SERV_CARGO,";
        sql += " ac.NOME, ac.ID_PROC, ac.PROC_NUMERO_COMPLETO, ac.RESERVADA, ac.DATA_MOVI_AUDI_PROC,";
        sql += " ac.ID_RECURSO, ac.CODIGO_TEMP ";
        sql += ",ac.CODIGO_TEMP_AUDI_PROC, ac.ID_AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_ANA, ac.AUDI_PROC_STATUS_CODIGO_ANA, ID_ARQ_ATA, DATA_AUDI_ORIGINAL, ID_ARQ_ATA_ADIAMENTO, ID_ARQ_ATA_INICIADO, ID_ARQ_FINALIZACAO, ";
        sql += " PRIMEIRO_PROMOVENTE_RECORRENTE, PRIMEIRO_PROMOVIDO_RECORRIDO, ac.ID_PROC_TIPO_AUDIENCIA, ac.PROC_TIPO_AUDIENCIA, ac.POLO_ATIVO, ac.POLO_PASSIVO, ac.ID_PEND_VOTO, ac.ID_PEND_EMENTA, ac.ID_PEND_VOTO_REDATOR, ac.ID_PEND_EMENTA_REDATOR, ID_AUDI_PROC_ORIGEM ";
        sql += obtenhaconsultaSQLSessoesPendentesAssistenteGabinete(id_ServentiaCargo, id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas, ps);        
        sql += " ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC, DATA_AGENDADA ASC, ID_PROC ASC";

        try{
            rs1 = consultar(sql, ps);
            // Chama método que retorna a lista de audiências já com os processos e as partes
            listaSessoesPendentes = getListaAudienciaProcessoRecurso(rs1);

        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        return listaSessoesPendentes;
    }
    
    
    /**
     * 
     * Consulta sql comum às consultas de sessões de segundo grau para um assistente de gabinete.
     * 
     * @param id_ServentiaCargo
     * @param id_Audiencia
     * @param somentePendentesAcordao
     * @param somentePendentesAssinatura
     * @param ps
     * @return
     * @throws Exception
     * @author mmgomes 
     */
    private String obtenhaconsultaSQLSessoesPendentesAssistenteGabinete(String id_ServentiaCargo, String id_Audiencia, boolean somentePendentesAcordao, boolean somentePendentesAssinatura, boolean somentePreAnalisadas, PreparedStatementTJGO ps) throws Exception{
        String sql;
        sql = obtenhaconsultaSQLSessoesPendentesDesembargador("", id_ServentiaCargo, id_Audiencia, somentePendentesAcordao, somentePendentesAssinatura, somentePreAnalisadas, ps);         
        return sql;
    }
    
    public String consultarAudienciasLivresJSON(String id_AudienciaTipo, String id_ServentiaCargo, String id_Serventia, String posicaoPaginaAtual) throws Exception {
        String sql;
        String sqlComum;
        String stTemp = "";
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs12 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        int qtdeColunas = 4;
        
        sqlComum = " FROM (((PROJUDI.AUDI ac";
        sqlComum += " INNER JOIN PROJUDI.AUDI_PROC ap ON (ac.ID_AUDI = ap.ID_AUDI))";
        sqlComum += " INNER JOIN PROJUDI.AUDI_TIPO at ON((ac.ID_AUDI_TIPO = at.ID_AUDI_TIPO)))";
        sqlComum += " LEFT JOIN PROJUDI.SERV_CARGO sc ON((ap.ID_SERV_CARGO = sc.ID_SERV_CARGO)))";        
        sqlComum += " WHERE (at.AUDI_TIPO_CODIGO <> ?)";        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
        sqlComum += " AND ac.ID_AUDI_TIPO = ? ";		        ps.adicionarLong(id_AudienciaTipo);
                
        if ((id_ServentiaCargo != null) && (!id_ServentiaCargo.trim().equalsIgnoreCase(""))) {
        	sqlComum += " AND ap.ID_SERV_CARGO = ? ";        	ps.adicionarLong(id_ServentiaCargo);
        } else {
        	sqlComum += " AND ac.ID_SERV = ? ";		        	ps.adicionarLong(id_Serventia);
        }
        sqlComum += " AND ( ap.ID_PROC IS NULL";
        
        // Adicionado para que não apareçam os processos físicos pois estes não possuem ID_PROC
        // mas possuem o número do processo físico registrado no campo CODIGO_TEMP 
        sqlComum += " AND (ap.CODIGO_TEMP IS NULL OR ap.CODIGO_TEMP < 0) )";
        
        sql = "SELECT ac.ID_AUDI AS ID, at.AUDI_TIPO AS DESCRICAO2,   to_char(ac.DATA_AGENDADA,'dd/mm/yyyy HH24:mi:ss')   AS DESCRICAO1, ac.RESERVADA AS DESCRICAO4, sc.SERV_CARGO AS DESCRICAO3";
        sql += sqlComum;
        sql += " ORDER BY ac.DATA_AGENDADA, at.AUDI_TIPO";
        
        try{
        	 rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual);

             sql = "SELECT COUNT(1) AS QUANTIDADE ";
             sql += sqlComum;            
             rs12 = consultar(sql, ps);
             rs12.next();
             
             stTemp = gerarJSON(rs12.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
            try{if (rs12 != null) rs12.close();} catch(Exception e) {}
        }
        return stTemp;
    }   
    
  //---------------------------------------------------------
  	public void reservar(String[] id) throws Exception{

  		PreparedStatementTJGO ps = new PreparedStatementTJGO();
  		String stSql="";

  		stSql= "UPDATE PROJUDI.AUDI SET  ";
  		stSql+= "RESERVADA = 1";
 	 	stSql += " WHERE ID_AUDI IN (?";	ps.adicionarLong(id[0]);; 	
 	 	
 	 	for(int i=1; i< id.length; i++) {
 	 		stSql += ", ?";					ps.adicionarLong(id[i]);; 	 		
 	 	}
 		stSql += ")";
 		executarUpdateDelete(stSql,ps); 
  	
  	} 
  	
  	public void excluir(String[] id) throws Exception{

  		PreparedStatementTJGO ps = new PreparedStatementTJGO();
  		String stSql="";

  		stSql= "DELETE FROM PROJUDI.AUDI  ";
 	 	stSql += " WHERE ID_AUDI IN (?";	ps.adicionarLong(id[0]);; 	
 	 	
 	 	for(int i=1; i< id.length; i++) {
 	 		stSql += ", ?";					ps.adicionarLong(id[i]);; 	 		
 	 	}
 		stSql += ")";
 		executarUpdateDelete(stSql,ps); 
  	
  	} 
  	
  	public void liberar(String[] id) throws Exception{

  		PreparedStatementTJGO ps = new PreparedStatementTJGO();
  		String stSql="";

  		stSql= "UPDATE PROJUDI.AUDI SET  ";
  		stSql+= "RESERVADA = 0";
 	 	stSql += " WHERE ID_AUDI IN (?";	ps.adicionarLong(id[0]);; 	
 	 	
 	 	for(int i=1; i< id.length; i++) {
 	 		stSql += ", ?";					ps.adicionarLong(id[i]);; 	 		
 	 	}
 		stSql += ")";
 		executarUpdateDelete(stSql,ps);
  	
  	}
  	
  	/**
  	 * Método que conta quantos processos estão vinculados a uma sessão.
  	 * @param idAudiencia - id da sessão
  	 * @return número de processos
  	 * @throws Exception
  	 * @author hmgodinho
  	 */
  	public int contarProcessosSessao(String idAudiencia) throws Exception {
        AudienciaDt audienciaCompleta = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        String sql = null;
        int qtde = 0;
        sql = "SELECT COUNT(1) AS QTDE ";
        sql += " FROM PROJUDI.AUDI_PROC WHERE ID_AUDI = ? ";
        ps.adicionarLong(idAudiencia);

        ResultSetTJGO rs1 = null;
        try{
            rs1 = consultar(sql, ps);

            if (rs1.next()) {
                qtde = rs1.getInt("QTDE");
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return qtde;
    }

	public List buscarPauta(Date data, String id_serventia) throws Exception {
        AudienciaDt audienciaCompleta = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        List lisPautas = new ArrayList();

        Calendar inicioExpediente = Calendar.getInstance();
        inicioExpediente.setTime(data);
        inicioExpediente.set(Calendar.HOUR_OF_DAY,7);
        inicioExpediente.set(Calendar.MINUTE,0);
        inicioExpediente.set(Calendar.MILLISECOND,0);
        
        Calendar inicioTarde = Calendar.getInstance();
        inicioTarde.setTime(data);
        inicioTarde.set(Calendar.HOUR_OF_DAY,13);
        inicioTarde.set(Calendar.MINUTE,0);
        inicioTarde.set(Calendar.MILLISECOND,0);
        
        Calendar inicioNoite = Calendar.getInstance();
        inicioNoite.setTime(data);
        inicioNoite.set(Calendar.HOUR_OF_DAY,19);
        inicioNoite.set(Calendar.MINUTE,0);
        inicioNoite.set(Calendar.MILLISECOND,0);                
        
        String sql = null;
                
        sql = "select a.id_serv, ap.id_serv_cargo , 'M' as periodo, a.id_audi_tipo ";
        sql += " from audi a inner join  audi_proc ap on a.id_audi = ap.id_audi "; 
        sql += " inner join audi_tipo atp on atp.id_audi_tipo = a.id_audi_tipo ";
        sql += " where a.id_serv = ?";																ps.adicionarLong(id_serventia);
        sql += "  and data_agendada >=? ";															ps.adicionarDateTime(inicioExpediente.getTime());
        sql += "  and data_agendada <? ";															ps.adicionarDateTime(inicioTarde.getTime());
        sql += "  and atp.audi_tipo_codigo in( ?, ?, ?, ? )";															
        ps.adicionarLong(AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo());
        ps.adicionarLong(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo());
        ps.adicionarLong(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo());
        ps.adicionarLong(AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo());
        sql += " group by a.id_serv, ap.id_serv_cargo, a.id_audi_tipo ";
        sql += " union all ";
        sql += " select a.id_serv, ap.id_serv_cargo , 'V' as periodo, a.id_audi_tipo ";
        sql += " from audi a inner join  audi_proc ap on a.id_audi = ap.id_audi ";
        sql += " inner join audi_tipo atp on atp.id_audi_tipo = a.id_audi_tipo ";
        sql += " where a.id_serv = ?";																ps.adicionarLong(id_serventia);
        sql += "  and data_agendada >=?";															ps.adicionarDateTime(inicioTarde.getTime());	
        sql += "  and data_agendada <? ";															ps.adicionarDateTime(inicioNoite.getTime());
        sql += "  and atp.audi_tipo_codigo in( ?, ?, ?, ? )";															
        ps.adicionarLong(AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo());
        ps.adicionarLong(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo());
        ps.adicionarLong(AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo());
        ps.adicionarLong(AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo());
        sql += " group by a.id_serv, ap.id_serv_cargo, a.id_audi_tipo ";
                        
        ResultSetTJGO rs1 = null;
        try{
            rs1 = consultar(sql, ps);

            while (rs1.next()) {
            	
            	String[] campos = new String[4]; 
            	campos[0] = rs1.getString("id_serv");
            	campos[1] = rs1.getString("id_serv_cargo");
            	campos[2] = rs1.getString("periodo");
            	campos[3] = rs1.getString("id_audi_tipo");
            	
            	lisPautas.add(campos);
            }
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }

        return lisPautas;
	}
	 /**
     * Método responsável por consultar Sessões de 2º grau de acordo com os
     * parâmetros informados na consulta por filtros.
     * 
     * @param id_Serventia
     * @param audienciaDtConsulta
     * @param statusAudiencia
     * @param posicaoPaginaAtual
     * @return String listaSessoes
     * 
     * @author gschiquini
     * @param tipoOrdenacao 
     * @param quantidadeRegistros 
     * @param campoOrdenacao 
     */
    public String consultarSessoesFiltroJSON(String id_Serventia, AudienciaDt audienciaDtConsulta, String statusAudiencia, String posicaoPaginaAtual, String campoOrdenacao, String quantidadeRegistros, String tipoOrdenacao) throws Exception {
        String sql, sqlComum = "";
        String listaSessoes = "";
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        sql = " SELECT a.ID_AUDI as ID, a.DATA_AGENDADA as DESCRICAO1, a.SERV as DESCRICAO2, a.ID_ARQ_FINALIZACAO as DESCRICAO3, a.DATA_MOVI as DESCRICAO4, a.VIRTUAL as DESCRICAO5 ";
        sqlComum += "FROM PROJUDI.VIEW_AUDI a";
        sqlComum += " WHERE a.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal());
        if (Funcoes.StringToInt(statusAudiencia) == AudienciaDt.ABERTA)
            sqlComum += " AND a.DATA_MOVI IS NULL";
        else if (Funcoes.StringToInt(statusAudiencia) == AudienciaDt.FINALIZADA) sqlComum += " AND a.DATA_MOVI is not null";
        sqlComum += " AND a.ID_SERV = ? ";
        ps.adicionarLong(id_Serventia);

        if (audienciaDtConsulta.getDataInicialConsulta().length() > 0){
        	sqlComum += " AND a.DATA_AGENDADA >= ? ";
        	ps.adicionarDateTime(audienciaDtConsulta.getDataInicialConsulta() + " 00:00:00");
        }
        if (audienciaDtConsulta.getDataFinalConsulta().length() > 0){
        	sqlComum += " AND a.DATA_AGENDADA <= ? ";
        	ps.adicionarDateTime(audienciaDtConsulta.getDataFinalConsulta() + " 23:59:59");
        }
        sql += sqlComum + " ORDER BY a."+campoOrdenacao + " " + tipoOrdenacao;
        try {
            rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, quantidadeRegistros);
            
            rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            rs2.next();
            listaSessoes = gerarJSON(rs2.getLong("Quantidade"), posicaoPaginaAtual, rs1, 5);
            // rs1.close();
        } finally {
            try {if (rs1 != null) rs1.close();} catch (Exception e) {}
            try {if (rs2 != null) rs2.close();} catch (Exception e) {}
        }
        return listaSessoes;
    }
    
    /**
     * Método responsável por consultar Sessões de 2º grau de acordo com os parâmetros informados na consulta por filtros.
     * Esse método irá retornar todas as sessões das serventias que tenham o gabinete do desembargador como relacionado.
     * 
     * @param id_Serventia
     * @param audienciaDtConsulta
     * @param statusAudiencia
     * @param posicaoPaginaAtual
     * @return List listaSessoes
     * 
     * @author gschiquini
     */
    public String consultarSessoesFiltroCamarasJSON(String id_ServentiaGabinete, AudienciaDt audienciaDtConsulta, String statusAudiencia, String posicaoPaginaAtual, String campoOrdenacao, String  quantidadeRegistros, String tipoOrdenacao) throws Exception {
        String sql, sqlComum = "";
        String listaSessoes = "";
        ResultSetTJGO rs1 = null;
        ResultSetTJGO rs2 = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        sql = " SELECT a.ID_AUDI as ID, a.DATA_AGENDADA as DESCRICAO1, a.SERV as DESCRICAO2, a.ID_ARQ_FINALIZACAO as DESCRICAO3, a.DATA_MOVI as DESCRICAO4, a.VIRTUAL as DESCRICAO5 ";
        
        sqlComum = "FROM PROJUDI.VIEW_AUDI a";
        
        sqlComum += " LEFT JOIN PROJUDI.SERV_RELACIONADA sr on a.ID_SERV = sr.ID_SERV_PRINC";
        sqlComum += " WHERE a.AUDI_TIPO_CODIGO = ? ";
        ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal());
        if (Funcoes.StringToInt(statusAudiencia) == AudienciaDt.ABERTA)
            sqlComum += " AND a.DATA_MOVI IS NULL";
        else if (Funcoes.StringToInt(statusAudiencia) == AudienciaDt.FINALIZADA) sqlComum += " AND a.DATA_MOVI is not null";
        sqlComum += " AND sr.ID_SERV_REL = ? ";
        ps.adicionarLong(id_ServentiaGabinete);
        if (audienciaDtConsulta.getDataInicialConsulta().length() > 0){
        	sqlComum += " AND a.DATA_AGENDADA >= ? ";
        	ps.adicionarDateTime(audienciaDtConsulta.getDataInicialConsulta() + " 00:00:00");
        }
        if (audienciaDtConsulta.getDataFinalConsulta().length() > 0){
        	sqlComum += " AND a.DATA_AGENDADA <= ? ";
        	ps.adicionarDateTime(audienciaDtConsulta.getDataFinalConsulta() + " 23:59:59");
        }
        
        sql += sqlComum + " ORDER BY a."+campoOrdenacao + " " + tipoOrdenacao;
        
        try {

        	rs1 = consultarPaginacao(sql, ps, posicaoPaginaAtual, quantidadeRegistros);
            
            rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE " + sqlComum, ps);
            rs2.next();
            listaSessoes = gerarJSON(rs2.getLong("Quantidade"), posicaoPaginaAtual, rs1, 5);
        } finally {
            try {if (rs1 != null) rs1.close();} catch (Exception e) {}
            try {if (rs2 != null) rs2.close();} catch (Exception e) {}
        }
        return listaSessoes;
    }
    
    
    
    public List listarPautaSessao(String idAudiencia, List<String> tiposDeAudiencia) throws Exception {
    	
    	String sql;
        ResultSetTJGO rs1 = null;
        RelatorioAudienciaProcesso audiProc = null;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
        List listaAudiencias = new ArrayList();
        
        sql = "	Select P.Id_Proc, S.Serv, A.Data_Agendada, Pt.Proc_Tipo, LPAD(p.proc_numero, 7, '0') || '.' || LPAD (p.digito_verificador, 2, '0') || '.' || LPAD (p.ano, 4, '0') || '.8.09.' || LPAD (p.forum_codigo, 4, '0') AS Numero_Completo "
        		+ " ,Cr.Comarca ,  U.Nome,C1.Cargo_Tipo, U1.Nome AS NOME_PROCURADOR, NVL(Cnj1.POLO_ATIVO_SESSAO, Cnj1.Polo_Ativo) As Polo_Ativo_desc , NVL(Cnj1.POLO_PASSIVO_SESSAO, Cnj1.Polo_Passivo) as Polo_Passivo_desc,Pp_A.Nome As Polo_Ativo, U_Ppa_A.Nome As Adv_Polo_Ativo"
        		+ " , Uso_A.Oab_Numero  As Adv_Polo_Ativo_Numero, Uso_A.Oab_Complemento As Adv_Polo_Ativo_Comp,Pp_P.Nome As Polo_Passivo,U_Ppa_P.Nome As adv_Polo_Passivo, Uso_P.Oab_Numero  As Adv_Polo_Passivo_Numero"
        		+ " , uso_p.oab_complemento As Adv_Polo_Passivo_comp,p.segredo_justica, pf.proc_fase_codigo, pt.ORDEM_2_GRAU, ap.complemento, ap.id_audi_proc, ap.CODIGO_TEMP as tipo_sessao, ct.cargo_tipo_codigo ";
        
        sql += ",   (SELECT PP.NOME "                
			    + " FROM VIEW_PROC_PARTE PP"
			    + " WHERE PP.DATA_BAIXA           IS NULL"
			    + " AND PP.PROC_PARTE_TIPO_CODIGO  = ?" 
			    + " AND PP.ID_PROC                 = AP.ID_PROC"
			    + " AND PP.ID_PROC                 = P.ID_PROC"
			    + " AND P.ID_PROC_TIPO IN (1235,144,167)"
			    + " AND ROWNUM                     = 1"
			    + " ) AS NOME_PACIENTE";
        		ps.adicionarLong(ProcessoParteTipoDt.PACIENTE);
        		
        if (tiposDeAudiencia != null) {
			 if (tiposDeAudiencia.contains(AudienciaProcessoDt.STATUS_JULGAMENTO_INICIADO)) {
				 sql += ", (select arq from projudi.arq a where a.id_arq = ap.id_arq_ata_iniciado) ata_iniciado ";
			 }
			 if (tiposDeAudiencia.contains(AudienciaProcessoDt.STATUS_JULGAMENTO_ADIADO)) {
				 sql += ", (select arq from projudi.arq a where a.id_arq = ap.id_arq_ata_adiamento) ata_adiado ";
			 }
		}  
        sql += "	From Projudi.Audi A Inner Join Projudi.Audi_Proc Ap On A.Id_Audi=Ap.Id_Audi "
        		+ "	Inner Join Projudi.Audi_Proc_Status aps On aps.id_audi_proc_status = ap.id_audi_proc_status "
        		+ " Inner Join Projudi.Serv S On S.Id_Serv = A.Id_Serv "
        		+ " Inner Join Projudi.Proc P On Ap.Id_Proc = P.Id_Proc "
        		+ "	Inner Join Projudi.Proc_fase Pf On p.Id_Proc_fase = Pf.Id_Proc_fase "
        		+ " inner Join Projudi.Recurso R on p.id_proc =r.id_proc and r.id_serv_recurso = p.id_serv "
        		+ " and TO_CHAR(A.DATA_AGENDADA, 'YYYYMMDD') >= TO_CHAR(R.DATA_RECEBIMENTO, 'YYYYMMDD') "
        		+ " and (R.DATA_RETORNO IS NULL or TO_CHAR(A.DATA_AGENDADA, 'YYYYMMDD') <= TO_CHAR(NVL(R.DATA_RETORNO, SYSDATE), 'YYYYMMDD') ) "
        		+ " Inner Join Projudi.Serv Sr On R.id_Serv_Origem = Sr.Id_Serv "
        		+ " Inner Join Projudi.Comarca Cr On Sr.Id_Comarca = Cr.Id_Comarca "
        		+ " Inner Join Projudi.Proc_Tipo Pt On ap.Id_Proc_Tipo = Pt.Id_Proc_Tipo "
        		+ " left join Projudi.cnj_classe cnj1 on cnj1.ID_CNJ_CLASSE = Pt.Id_Cnj_Classe "
		        + " Inner Join (Projudi.Serv_Cargo Sc Inner Join Projudi.Usu_Serv_Grupo Usg On Usg.Id_Usu_Serv_Grupo = Sc.Id_Usu_Serv_Grupo "
		        + "             Inner Join Projudi.Cargo_Tipo Ct On Ct.Id_Cargo_Tipo = Sc.Id_Cargo_Tipo "
				+ "				Inner Join Projudi.Usu_Serv Us On Usg.Id_Usu_Serv = Us.Id_Usu_Serv "
				+ "				Inner Join Projudi.Usu U On Us.Id_Usu = U.Id_Usu ) On ap.Id_Serv_Cargo = Sc.Id_Serv_Cargo ";
		sql += "	Left Join (Projudi.Proc_Resp Pr1 "
        		+ "					Inner Join Projudi.Cargo_Tipo C1 On Pr1.Id_Cargo_Tipo = C1.Id_Cargo_Tipo "
        		+ "					Inner Join Projudi.Serv_Cargo Sc1 On Pr1.Id_Serv_Cargo  = Sc1.Id_Serv_Cargo "
        		+ "					Inner Join Projudi.Usu_Serv_Grupo Usg1 On Usg1.Id_Usu_Serv_Grupo = Sc1.Id_Usu_Serv_Grupo "
        		+ "					Inner Join Projudi.Usu_Serv Us1 On Usg1.Id_Usu_Serv = Us1.Id_Usu_Serv "
        		+ "					Inner Join Projudi.Usu U1 On Us1.Id_Usu = U1.Id_Usu "
        		+ "					Inner Join Projudi.Serv s1 On s1.id_serv = us1.id_serv and s1.id_serv_subtipo = ?) On Pr1.Id_Proc = P.Id_Proc And Pr1.Codigo_Temp = ? And Pr1.Id_Cargo_Tipo = ? ";
		ps.adicionarLong(ServentiaSubtipoDt.ID_PROMOTORIA_SEGUNDO_GRAU);
		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);		
		ps.adicionarLong(CargoTipoDt.MINISTERIO_PUBLICO);
        sql += "    left Join Projudi.Recurso_Parte Rp_A on Rp_A.Id_Recurso = R.Id_Recurso And rp_a.Id_Proc_Parte_Tipo = ? and rp_a.DATA_BAIXA IS NULL and  ap.id_proc_tipo = rp_a.id_proc_tipo";
        ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
        sql += "	left Join Projudi.Proc_Parte Pp_A On Rp_A.Id_Proc_Parte = Pp_A.Id_Proc_Parte  And Pp_A.Data_Baixa Is Null "
        		+ " left Join Projudi.Recurso_Parte Rp_P On Rp_P.Id_Recurso = R.Id_Recurso And rp_p.Id_Proc_Parte_Tipo = ? and rp_p.DATA_BAIXA IS NULL and  ap.id_proc_tipo = rp_p.id_proc_tipo";
        ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
        sql += " left Join Projudi.Proc_Parte Pp_P On Rp_P.Id_Proc_Parte = Pp_P.Id_Proc_Parte And Pp_P.Data_Baixa Is Null "
        		+ " Left Join (Projudi.Proc_Parte_Advogado Ppa_A "
        		+ "					Inner Join Projudi.Usu_Serv Us_Ppa_A On Us_Ppa_A.Id_Usu_Serv = Ppa_A.Id_Usu_Serv "
        		+ "					Inner Join Projudi.Usu_Serv_Oab Uso_A On Uso_A.Id_Usu_Serv = Ppa_A.Id_Usu_Serv "
        		+ "					Inner Join Projudi.Usu U_Ppa_A On U_Ppa_A.Id_Usu = Us_Ppa_A.Id_Usu) On Pp_A.Id_Proc_Parte = Ppa_A.Id_Proc_Parte  And Ppa_A.Data_Saida Is Null "
        		+ "	Left Join (Projudi.Proc_Parte_Advogado Ppa_P "
        		+ "					Inner Join Projudi.Usu_Serv Us_Ppa_P On Us_Ppa_P.Id_Usu_Serv = Ppa_p.Id_Usu_Serv "
        		+ "					Inner Join Projudi.Usu_Serv_Oab Uso_P On Uso_P.Id_Usu_Serv = Ppa_P.Id_Usu_Serv "
        		+ "					inner join Projudi.usu u_ppa_p on u_ppa_p.id_usu = us_ppa_p.id_usu) On Pp_p.Id_Proc_Parte = Ppa_p.Id_Proc_Parte  And Ppa_p.Data_Saida Is Null "
        		+ " Where a.Id_Audi = ? And aps.audi_proc_status_codigo not in (?,?,?,?,?) ";
    		ps.adicionarLong(idAudiencia);
    		ps.adicionarLong(AudienciaProcessoStatusDt.DESMARCAR_PAUTA);
    		ps.adicionarLong(AudienciaProcessoStatusDt.RETIRAR_PAUTA);
    		ps.adicionarLong(AudienciaProcessoStatusDt.REMARCADA);
    		ps.adicionarLong(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO);
    		ps.adicionarLong(AudienciaProcessoStatusDt.JULGAMENTO_INICIADO);
    		sql += " And (";
    		if (tiposDeAudiencia == null || tiposDeAudiencia.size() == 0) {
    			sql +=  " ap.codigo_temp is null "; //Pauta do dia
    		} else {
    			for(int i=0; i < tiposDeAudiencia.size(); i++) {
    				if (i>0) sql +=  " OR ";
    				if (tiposDeAudiencia.get(i).equalsIgnoreCase(AudienciaProcessoDt.STATUS_JULGAMENTO_PAUTA_DIA)) { 
    					sql +=  " ap.codigo_temp is null "; //Pauta do dia
    				} else {
    					sql +=  " ap.codigo_temp = ? ";
    					ps.adicionarLong(tiposDeAudiencia.get(i)); //Adiados / Iniciados / Em mesa para julgamento (Extra pauta)
    				}
    			}
    		}
    		sql += ")";   
    		sql += " AND Rp_A.DATA_BAIXA IS NULL";
    		sql += " AND Rp_P.DATA_BAIXA IS NULL";
    		sql += " AND Pp_A.DATA_BAIXA IS NULL";
    		sql += " AND Pp_P.DATA_BAIXA IS NULL";
        	sql += " And NVL(Rp_A.Id_Recurso_Parte,0) = NVL((Select Min(Id_Recurso_Parte) From Projudi.Recurso_Parte rpint inner join Projudi.Proc_parte ppint on ppint.id_proc_parte = rpint.id_proc_parte Where rpint.Id_Recurso = R.Id_Recurso And rpint.Id_Proc_Parte_Tipo = ? and rpint.id_proc_tipo = ap.id_proc_tipo and rpint.data_baixa is null),0) ";
        	ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
        	sql += " And NVL(rp_P.id_recurso_parte,0) = NVL((select min(id_recurso_parte) From Projudi.Recurso_Parte rpint inner join Projudi.Proc_parte ppint on ppint.id_proc_parte = rpint.id_proc_parte where rpint.id_recurso = r.id_recurso and rpint.Id_Proc_Parte_Tipo = ? and rpint.id_proc_tipo = ap.id_proc_tipo and rpint.data_baixa is null),0) ";
        	ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
        	sql += " union all "
        		+ "	Select  P.Id_Proc, S.Serv, A.Data_Agendada, Pt.Proc_Tipo, LPAD(p.proc_numero, 7, '0') || '.' || LPAD (p.digito_verificador, 2, '0') || '.' || LPAD (p.ano, 4, '0') || '.8.09.' || LPAD (p.forum_codigo, 4, '0') AS Numero_Completo "
        		+ "		, C.Comarca,  U.Nome,C1.Cargo_Tipo, U1.Nome, Cnj.Polo_Ativo As Polo_Ativo_Desc , Cnj.Polo_Passivo As Polo_Passivo_Desc, Pp_A.Nome As Polo_Ativo, U_Ppa_A.Nome As Adv_Polo_Ativo "
        		+ "		, Uso_A.Oab_Numero  As Adv_Polo_Ativo_Numero, Uso_A.Oab_Complemento As Adv_Polo_Ativo_Comp,Pp_P.Nome As Polo_Passivo, U_Ppa_P.Nome As Adv_Polo_Passivo, Uso_P.Oab_Numero  As Adv_Polo_Passivo_Numero "
        		+ "		, uso_p.oab_complemento As Adv_Polo_Passivo_comp , p.segredo_justica, pf.proc_fase_codigo, pt.ORDEM_2_GRAU, ap.complemento, ap.id_audi_proc, ap.CODIGO_TEMP as tipo_sessao, ct.cargo_tipo_codigo  ";
        	
        	sql += ",   (SELECT PP.NOME "                    
    			    + " FROM VIEW_PROC_PARTE PP"
    			    + " WHERE PP.DATA_BAIXA           IS NULL"
    			    + " AND PP.PROC_PARTE_TIPO_CODIGO  = ?" 
    			    + " AND PP.ID_PROC                 = AP.ID_PROC"
    			    + " AND PP.ID_PROC                 = P.ID_PROC"
    			    + " AND P.ID_PROC_TIPO IN (1235,144,167)"
    			    + " AND ROWNUM                     = 1"
    			    + " ) AS NOME_PACIENTE";
            		ps.adicionarLong(ProcessoParteTipoDt.PACIENTE);
        	
        		if (tiposDeAudiencia != null) {
        			 if (tiposDeAudiencia.contains(AudienciaProcessoDt.STATUS_JULGAMENTO_INICIADO)) {
        				 sql += ", (select arq from projudi.arq a where a.id_arq = ap.id_arq_ata_iniciado) ata_iniciado ";
        			 }
        			 if (tiposDeAudiencia.contains(AudienciaProcessoDt.STATUS_JULGAMENTO_ADIADO)) {
        				 sql += ", (select arq from projudi.arq a where a.id_arq = ap.id_arq_ata_adiamento) ata_adiado ";
        			 }
        		}        		
        	sql += "	From Projudi.Audi A "
        		+ "		Inner Join Projudi.Audi_Proc Ap On A.Id_Audi=Ap.Id_Audi "
        		+ "		Inner Join Projudi.Audi_Proc_Status aps On aps.id_audi_proc_status = ap.id_audi_proc_status "
        		+ "		Inner Join Projudi.Serv S On S.Id_Serv = A.Id_Serv "
        		+ "		Inner Join Projudi.Proc P On Ap.Id_Proc = P.Id_Proc "
        		+ "		Inner Join Projudi.Proc_fase Pf On p.Id_Proc_fase = Pf.Id_Proc_fase "
        		+ "		Inner Join Projudi.Proc_Tipo Pt On ap.Id_Proc_Tipo = Pt.Id_Proc_Tipo "
        		+ "		Inner Join Projudi.Comarca C On S.Id_Comarca = C.Id_Comarca "        		   
        	    + "     Inner Join (Projudi.Serv_Cargo Sc Inner Join Projudi.Usu_Serv_Grupo Usg On Usg.Id_Usu_Serv_Grupo = Sc.Id_Usu_Serv_Grupo "
				+ "				Inner Join Projudi.Usu_Serv Us On Usg.Id_Usu_Serv = Us.Id_Usu_Serv "
		        + "             Inner Join Projudi.Cargo_Tipo Ct On Ct.Id_Cargo_Tipo = Sc.Id_Cargo_Tipo "
				+ "				Inner Join Projudi.Usu U On Us.Id_Usu = U.Id_Usu ) On ap.Id_Serv_Cargo = Sc.Id_Serv_Cargo ";
        	sql += "	Left Join (Projudi.Proc_Resp Pr1 "
        		+ "						Inner Join Projudi.Cargo_Tipo C1 On Pr1.Id_Cargo_Tipo = C1.Id_Cargo_Tipo "
        		+ "						Inner Join Projudi.Serv_Cargo Sc1 On Pr1.Id_Serv_Cargo  = Sc1.Id_Serv_Cargo "
        		+ "						Inner Join Projudi.Usu_Serv_Grupo Usg1 On Usg1.Id_Usu_Serv_Grupo = Sc1.Id_Usu_Serv_Grupo "
        		+ "						Inner Join Projudi.Usu_Serv Us1 On Usg1.Id_Usu_Serv = Us1.Id_Usu_Serv "
        		+ "						Inner Join Projudi.Usu U1 On Us1.Id_Usu = U1.Id_Usu "
        		+ "						Inner Join Projudi.Serv s1 On s1.id_serv = us1.id_serv and s1.id_serv_subtipo = ?) On Pr1.Id_Proc = P.Id_Proc And Pr1.Codigo_Temp = ? And Pr1.Id_Cargo_Tipo = ? ";
        	ps.adicionarLong(ServentiaSubtipoDt.ID_PROMOTORIA_SEGUNDO_GRAU);
    		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);		
    		ps.adicionarLong(CargoTipoDt.MINISTERIO_PUBLICO);
        	sql += "	left Join Projudi.Proc_Parte Pp_A On P.Id_Proc = Pp_A.Id_Proc  And Pp_A.Id_Proc_Parte_Tipo = ? And Pp_A.Data_Baixa Is Null ";
        	ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
        	sql += "	left Join Projudi.Proc_Parte Pp_P On P.Id_Proc = Pp_P.Id_Proc And Pp_P.Id_Proc_Parte_Tipo = ? and pp_p.data_baixa is null ";
        	ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
        	sql += "	Left Join (Projudi.Proc_Parte_Advogado Ppa_A "
        		+ "						Inner Join Projudi.Usu_Serv Us_Ppa_A On Us_Ppa_A.Id_Usu_Serv = Ppa_A.Id_Usu_Serv "
        		+ "						Inner Join Projudi.Usu_Serv_Oab Uso_A On Uso_A.Id_Usu_Serv = Ppa_A.Id_Usu_Serv "
        		+ "						Inner Join Projudi.Usu U_Ppa_A On U_Ppa_A.Id_Usu = Us_Ppa_A.Id_Usu) On Pp_A.Id_Proc_Parte = Ppa_A.Id_Proc_Parte  And Ppa_A.Data_Saida Is Null "
        		+ "		Left Join (Projudi.Proc_Parte_Advogado Ppa_P "
        		+ "						Inner Join Projudi.Usu_Serv Us_Ppa_P On Us_Ppa_P.Id_Usu_Serv = Ppa_p.Id_Usu_Serv "
        		+ "						Inner Join Projudi.Usu_Serv_Oab Uso_P On Uso_P.Id_Usu_Serv = Ppa_p.Id_Usu_Serv "
        		+ "						inner join Projudi.usu u_ppa_p on u_ppa_p.id_usu = us_ppa_p.id_usu) On Pp_p.Id_Proc_Parte = Ppa_p.Id_Proc_Parte  And Ppa_p.Data_Saida Is Null "
        		+ "		Left Join Projudi.Cnj_Classe Cnj On Cnj.ID_CNJ_CLASSE = Pt.Id_Cnj_Classe "
        		+ "		Where a.Id_Audi = ? And aps.audi_proc_status_codigo not in (?,?,?,?,?) ";
    		ps.adicionarLong(idAudiencia);
    		ps.adicionarLong(AudienciaProcessoStatusDt.DESMARCAR_PAUTA);
    		ps.adicionarLong(AudienciaProcessoStatusDt.RETIRAR_PAUTA);
    		ps.adicionarLong(AudienciaProcessoStatusDt.REMARCADA);
    		ps.adicionarLong(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO);
    		ps.adicionarLong(AudienciaProcessoStatusDt.JULGAMENTO_INICIADO);
    		sql +=   "		And (";
    		if (tiposDeAudiencia == null || tiposDeAudiencia.size() == 0) {
    			sql +=  " ap.codigo_temp is null "; //Pauta do dia
    		} else {
    			for(int i=0; i < tiposDeAudiencia.size(); i++) {
    				if (i>0) sql +=  " OR ";
    				if (tiposDeAudiencia.get(i).equalsIgnoreCase(AudienciaProcessoDt.STATUS_JULGAMENTO_PAUTA_DIA)) { 
    					sql +=  " ap.codigo_temp is null "; //Pauta do dia
    				} else {
    					sql +=  " ap.codigo_temp = ? ";
    					ps.adicionarLong(tiposDeAudiencia.get(i)); //Adiados / Iniciados / Em mesa para julgamento (Extra pauta)
    				}
    			}
    		}
    		sql +=  ")"
        		+ "		And Not Exists (Select 1 From Projudi.Recurso R2 Where R2.Id_Proc = P.Id_Proc And R2.Data_Retorno Is Null) ";
    		sql += " AND Pp_A.DATA_BAIXA IS NULL";
    		sql += " AND Pp_P.DATA_BAIXA IS NULL";
    		sql += " And NVL(Pp_A.Id_Proc_Parte,0) = NVL((Select Min(Id_Proc_Parte) From Projudi.Proc_Parte Where Id_Proc = P.Id_Proc And Id_Proc_Parte_Tipo = ? and data_baixa is null),0) ";
    		ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
        	sql += " And NVL(Pp_P.Id_Proc_Parte,0) = NVL((Select Min(Id_Proc_Parte) From Projudi.Proc_Parte Where Id_Proc = P.Id_Proc And Id_Proc_Parte_Tipo = ? and data_baixa is null),0) ";
        	ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
        	
        	sql =  "SELECT * FROM (" + sql + ") order by segredo_justica, ORDEM_2_GRAU, Proc_Tipo, id_audi_proc";
        
        try{
            rs1 = consultar(sql, ps);
            
            String numeroProcesso = "", processoTipo = "", processoFaseCodigo = "";
            int i = 0;
            while (rs1.next()) {
            	audiProc = new RelatorioAudienciaProcesso();
            	
            	if(numeroProcesso.equals(rs1.getString("NUMERO_COMPLETO"))) {
            		//Se não tiver mudado o número do processo, quer dizer que é o mesmo processo e a única alteração que haverá
            		//será nome de advogado de polo ativo ou passivo
            		RelatorioAudienciaProcesso rel = (RelatorioAudienciaProcesso)listaAudiencias.get(i-1);
            		rel.addAdvogadoPoloAtivo(Funcoes.capitalizeNome(rs1.getString("ADV_POLO_ATIVO")), rs1.getString("ADV_POLO_ATIVO_NUMERO"), rs1.getString("ADV_POLO_ATIVO_COMP"));
            		rel.addAdvogadoPoloPassivo(Funcoes.capitalizeNome(rs1.getString("ADV_POLO_PASSIVO")), rs1.getString("ADV_POLO_PASSIVO_NUMERO"), rs1.getString("ADV_POLO_PASSIVO_COMP"));
            		if (rel.getNomePoloAtivo() != null && rel.getNomePoloAtivo().startsWith("PARTE HABILITADA NÃO LOCALIZADA PARA O RECURSO")) {
            			atualizeNomeDasPartesPautaSessao(rs1, rel, rel.getProcessoTipo());
            		}        			
            	} else {
            		audiProc.setServentia(rs1.getString("SERV"));
            		
            		//a informação que irá em ProcessoTipo tem alguns detalhes extras para atender solicitação dos usuários
            		processoTipo = rs1.getString("PROC_TIPO");
            		//Se o processo se encontrar na fase Execução de Acórdão, vai escrever no relatório logo após o proc_tipo
            		processoFaseCodigo = rs1.getString("PROC_FASE_CODIGO");
            		if(processoFaseCodigo.equalsIgnoreCase(String.valueOf(ProcessoFaseDt.EXECUCAO_ACORDAO))) {
            			processoTipo += " - (Execução de Acórdão)";
            		}
            		if(rs1.getString("COMPLEMENTO") != null && !rs1.getString("COMPLEMENTO").equalsIgnoreCase("")) {
            			processoTipo += " - " + rs1.getString("COMPLEMENTO");
            		}
            		if(rs1.getString("SEGREDO_JUSTICA") != null && rs1.getString("SEGREDO_JUSTICA").equals("1")) {
            			processoTipo += " - SEGREDO DE JUSTIÇA"; 
            		}
            		audiProc.setProcessoTipo(processoTipo);
            		
            		audiProc.setProcessoNumero(rs1.getString("NUMERO_COMPLETO"));
            		audiProc.setComarca(rs1.getString("COMARCA"));
            		if (!rs1.isNull("cargo_tipo_codigo") && Funcoes.StringToInt(rs1.getString("cargo_tipo_codigo")) == CargoTipoDt.DESEMBARGADOR) {
            			audiProc.setRelator("DES " + rs1.getString("NOME").toUpperCase());	
            		} else {
            			audiProc.setRelator("DR " + rs1.getString("NOME").toUpperCase());	
            		}   
            		
            		atualizeNomeDasPartesPautaSessao(rs1, audiProc, processoTipo);
            		
            		if(rs1.getString("ADV_POLO_PASSIVO") != null) {
            			audiProc.addAdvogadoPoloPassivo(Funcoes.capitalizeNome(rs1.getString("ADV_POLO_PASSIVO")), rs1.getString("ADV_POLO_PASSIVO_NUMERO"), rs1.getString("ADV_POLO_PASSIVO_COMP"));
            		}
            		audiProc.setProcuradorJustica(Funcoes.capitalizeNome(rs1.getString("NOME_PROCURADOR")));
            		
            		audiProc.setTipoSessao(rs1.getString("tipo_sessao"));

            		audiProc.setIdAudiProc(rs1.getString("id_audi_proc"));

            		audiProc.setIdProcesso(rs1.getString("id_proc")); // jvosantos - 04/06/2019 10:37 - Adiciona ID do processo
            		
            		if (tiposDeAudiencia != null) {
	           			 if (tiposDeAudiencia.contains(AudienciaProcessoDt.STATUS_JULGAMENTO_INICIADO) && !rs1.isNull("ata_iniciado")) {
	           				 audiProc.setTextoAtaAdiadoIniciado(Signer.extrairConteudoP7sRecibo(rs1.getBytes("ata_iniciado")));
	           			 }
	           			 if (tiposDeAudiencia.contains(AudienciaProcessoDt.STATUS_JULGAMENTO_ADIADO) && !rs1.isNull("ata_adiado")) {
	           				audiProc.setTextoAtaAdiadoIniciado(Signer.extrairConteudoP7sRecibo(rs1.getBytes("ata_adiado")));
	           			 }
	           		}  
            		
            		numeroProcesso = rs1.getString("NUMERO_COMPLETO");
            		i++;
            		
            		listaAudiencias.add(audiProc);
            	}
            	
            }
            	
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        
        return listaAudiencias;
    }

	private void atualizeNomeDasPartesPautaSessao(ResultSetTJGO rs1, RelatorioAudienciaProcesso audiProc, String processoTipo) throws Exception {
		if (!rs1.isNull("NOME_PACIENTE") && rs1.getString("NOME_PACIENTE").trim().length() > 0) {
			audiProc.setDescricaoPoloAtivo("Paciente");
		} else if(rs1.getString("POLO_ATIVO_DESC") != null && !rs1.getString("POLO_ATIVO_DESC").trim().equalsIgnoreCase("")) {
			audiProc.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO_DESC"));
		} else {
			audiProc.setDescricaoPoloAtivo("Polo Ativo");
		}
		if(rs1.getString("SEGREDO_JUSTICA") != null && rs1.getString("SEGREDO_JUSTICA").equals("1")) {
			if (!rs1.isNull("NOME_PACIENTE") && rs1.getString("NOME_PACIENTE").trim().length() > 0) {
				audiProc.setNomePoloAtivo(Funcoes.iniciaisNome(rs1.getString("NOME_PACIENTE")));	
			} else {
				audiProc.setNomePoloAtivo(Funcoes.iniciaisNome(rs1.getString("POLO_ATIVO")));	
			}            			
		} else {
			if (!rs1.isNull("NOME_PACIENTE") && rs1.getString("NOME_PACIENTE").trim().length() > 0) {
				audiProc.setNomePoloAtivo(Funcoes.capitalizeNome(rs1.getString("NOME_PACIENTE")));	
			} else {
				audiProc.setNomePoloAtivo(Funcoes.capitalizeNome(rs1.getString("POLO_ATIVO")));	
			}            			
		}
		if(rs1.getString("ADV_POLO_ATIVO") != null){
			audiProc.addAdvogadoPoloAtivo(Funcoes.capitalizeNome(rs1.getString("ADV_POLO_ATIVO")), rs1.getString("ADV_POLO_ATIVO_NUMERO"), rs1.getString("ADV_POLO_ATIVO_COMP"));
		}
		if(rs1.getString("POLO_PASSIVO_DESC") != null && !rs1.getString("POLO_PASSIVO_DESC").trim().equalsIgnoreCase("")) {
			audiProc.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO_DESC"));
		} else {
			audiProc.setDescricaoPoloPassivo("Polo Passivo");
		}
		
		if(rs1.getString("SEGREDO_JUSTICA") != null && rs1.getString("SEGREDO_JUSTICA").equals("1")) {
			audiProc.setNomePoloPassivo(Funcoes.iniciaisNome(rs1.getString("POLO_PASSIVO")));
		} else {
			audiProc.setNomePoloPassivo(Funcoes.capitalizeNome(rs1.getString("POLO_PASSIVO")));
		} 
		
		if ((audiProc.getNomePoloAtivo() == null || audiProc.getNomePoloAtivo().trim().length() == 0) &&
			(audiProc.getNomePoloPassivo() == null || audiProc.getNomePoloPassivo().trim().length() == 0)) {
			audiProc.setNomePoloAtivo("PARTE HABILITADA NÃO LOCALIZADA PARA O RECURSO " + processoTipo.toUpperCase());
			audiProc.setNomePoloPassivo("PARTE HABILITADA NÃO LOCALIZADA PARA O RECURSO " + processoTipo.toUpperCase());
		}
	}
    
    /**
	 * Método que contém a SQL que irá excluir os registros da tabela AUDI_PROC livres, ou seja, os registros cujo id do processo é nulo e que também não tenham vinculo com a tabela AUDI_PROC_FISICO
	 * 
	 * @author lsbernardes
	 * @param audienciaProcessoDtExcluir
	 * @throws Exception
	 */
	public void excluirAudienciasProcessosNaoUtilizadas() throws Exception {
		String sqlAudiProc="";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		Calendar dataLimite = Calendar.getInstance();
		dataLimite.set(Calendar.HOUR_OF_DAY, 0);
		dataLimite.set(Calendar.MINUTE, 0);
		dataLimite.set(Calendar.SECOND, 0);
		
		sqlAudiProc = "DELETE FROM PROJUDI.AUDI_PROC ap ";
		sqlAudiProc += "  WHERE(  ";
		sqlAudiProc += "          ap.ID_PROC IS NULL ";
		sqlAudiProc += "          AND EXISTS(  ";
		sqlAudiProc += "                     SELECT 1  ";
		sqlAudiProc += "                     FROM PROJUDI.AUDI ad  ";
		sqlAudiProc += "                     WHERE ad.ID_AUDI = ap.ID_AUDI  AND ad.DATA_AGENDADA < ? "; ps.adicionarDateTime(dataLimite.getTime()); //to_date('01/06/2017 00:00:00', 'dd/mm/yyyy HH24:mi:ss')
		sqlAudiProc += "                   )  ";
		sqlAudiProc += "           AND NOT EXISTS(  ";
		sqlAudiProc += "                         SELECT 1  ";
		sqlAudiProc += "                         FROM AUDI_PROC_FISICO apf  ";
		sqlAudiProc += "                         WHERE apf.ID_AUDI_PROC = ap.ID_AUDI_PROC  ";
		sqlAudiProc += "                         ) ";
		sqlAudiProc += "       ) ";
		
		executarUpdateDelete(sqlAudiProc, ps);
		
	}
    
	/**
	 * Método que contém a SQL que irá excluir os registros da tabela AUDI livres, ou seja, os registros cujo id da audiencia não estaja na tabela AUDI_PROC
	 * 
	 * @author lsbernardes
	 * @param audienciaProcessoDtExcluir
	 * @throws Exception
	 */
	public void excluirPautasAudienciasNaoUtilizadas() throws Exception{

  		PreparedStatementTJGO ps = new PreparedStatementTJGO();
  		String stSql="";
  		
  		Calendar dataLimite = Calendar.getInstance();
		dataLimite.set(Calendar.HOUR_OF_DAY, 0);
		dataLimite.set(Calendar.MINUTE, 0);
		dataLimite.set(Calendar.SECOND, 0);
  		
  		stSql = " DELETE FROM PROJUDI.AUDI ad ";
  		stSql += "   WHERE ad.DATA_AGENDADA < ? "; ps.adicionarDateTime(dataLimite.getTime()); //to_date('01/06/2017 00:00:00', 'dd/mm/yyyy HH24:mi:ss')
  		stSql +="         AND NOT EXISTS(  ";
  		stSql +="                         SELECT 1  ";
  		stSql +="                         FROM PROJUDI.AUDI_PROC ap  ";
  		stSql +="                         WHERE ap.ID_AUDI = ad.ID_AUDI  ";
  		stSql +="                        )  ";
 		executarUpdateDelete(stSql,ps); 
  	
  	}

	public boolean temAudienciaProcessoServentiaPendente(String id_Processo, String id_Serventia) throws Exception {
    	    	
        String sql = "";            
        ResultSetTJGO rs1 = null;
        
        boolean boRetorno = false;
        
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
        				
        sql = "select a.id_audi from audi a inner join audi_proc ap on a.id_audi = ap.id_audi ";        
        sql += " WHERE ap.id_proc = ? "; 											ps.adicionarLong(id_Processo);
        sql += " and a.id_serv = ? ";												ps.adicionarLong(id_Serventia);        
        sql += " and ap.data_movi is null";

        try{
        	rs1 = consultar(sql, ps);
        	
        	if (rs1.next() ){
        		boRetorno = true;            
        	}          
                    
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
           
        }
        return boRetorno;
	} 
	
//	/**
//	 * Altera a serventia das pautas vinculadas ao processo passado.
//	 * Utilizada para a redistribuição em lote do distribuidor.
//	 * 
//	 * @param idProc
//	 * @param idServentiaNova
//	 * @throws Exception
//	 * @author hrrosa
//	 */
//	public void alterarServentiaAudienciaRedistribuicaoLoteDistribuidor(String idProc, String idServentiaNova) throws Exception{
//		String sql;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		
//		sql = "UPDATE PROJUDI.AUDI A ";
//		
//		sql += " INNER JOIN PROJUDI.AUDI_PROC AP ON AP.ID_AUDI = A.ID_AUDI";
//		sql += " INNER JOIN PROJUDI.SERV_CARGO SC ON AP.ID_SERV_CARGO = SC.ID_SERV_CARGO";
//				
//		sql += " SET A.ID_SERV = ?"; ps.adicionarLong(idServentiaNova);
//		sql += " WHERE A.ID_AUDI IN ( ";
//		sql += "						SELECT ID_AUDI FROM PROJUDI.AUDI_PROC AP WHERE AP.ID_PROC = ? "; ps.adicionarLong(idProc);
//		sql += "						AND AP.ID_AUDI_PROC_STATUS = ?"; ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
//		sql += " 						AND EXISTS (";
//		sql += "										SELECT (1) FROM PROJUDI.PROC P";
//		sql += " 										INNER JOIN PROJUDI.AUDI_PROC AP ON AP.ID_PROC = P.ID_PROC";
//		sql += " 										INNER JOIN PROJUDI.SERV_CARGO SC ON AP.ID_SERV_CARGO = SC.ID_SERV_CARGO";
//		sql	+= "										WHERE P.ID_PROC = ?"; ps.adicionarLong(idProc);
//		sql += "										AND P.ID_SERV = SC.ID_SERV";
//		sql += "									)";
//		sql += "				 	)";
//		
// 		executarUpdateDelete(sql,ps); 
//	}
	
	/**
	 * Altera o responsável das audiências que estão em aberto para o processo passado.
	 * Utilizada para a redistribuição em lote do distribuidor.
	 * 
	 * @param idProc
	 * @param idServCargoNovoResponsavel
	 * @throws Exception
	 * @author hrrosa
	 */
	public void alterarServentiaResponsavelAudienciaRedistribuicaoLoteDistribuidor(String idProc,String idServentiaAtual, String idServentiaNova, String idServCargoNovo) throws Exception {
		String sql1, sql2;
		PreparedStatementTJGO ps1 = new PreparedStatementTJGO();
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
		
		sql1 = "UPDATE (select A.ID_SERV, AP.ID_SERV_CARGO"; 
		sql1 += "	from PROJUDI.AUDI_PROC AP"; 
		sql1 += "		        INNER JOIN PROJUDI.AUDI A ON AP.ID_AUDI = A.ID_AUDI"; 
		sql1 += "				        WHERE AP.ID_PROC = ?";  ps1.adicionarLong(idProc);
		sql1 += "	AND AP.ID_AUDI_PROC_STATUS = ?"; 			ps1.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql1 += "	AND A.ID_SERV = ? )"; 						ps1.adicionarLong(idServentiaAtual);
		sql1 += "	SET  ID_SERV_CARGO = ?";					ps1.adicionarLong(idServCargoNovo);

		sql2 = "UPDATE AUDI"; 
		sql2 += "	SET  ID_SERV = ?";							ps2.adicionarLong(idServentiaNova);
		sql2 += "	WHERE  ID_SERV = ? AND DATA_MOVI IS NULL";  ps2.adicionarLong(idServentiaAtual);
		sql2 += "	AND ID_AUDI IN (SELECT ID_AUDI FROM AUDI_PROC AP WHERE AP.ID_PROC = ?"; ps2.adicionarLong(idProc);
		sql2 += "	        AND AP.ID_AUDI_PROC_STATUS = ?  )"; ps2.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		
		
		
		executarUpdateDelete(sql1, ps1);
		executarUpdateDelete(sql2, ps2);
	}
	
	public boolean isVirtualIniciada(String idAudiencia )  throws Exception {

		String sql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT VIRTUAL FROM PROJUDI.AUDI WHERE ID_AUDI = ? "; 	ps.adicionarLong(idAudiencia); 
		sql += " AND SESSAO_INICIADA = 1 ";

		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				return rs1.getBoolean("VIRTUAL");
			}
			return false;
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
	}
	
	public boolean isVirtual(String idAudiencia )  throws Exception {

		String sql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "SELECT VIRTUAL FROM PROJUDI.AUDI WHERE ID_AUDI = ? "; 	ps.adicionarLong(idAudiencia); 

		try{
			rs1 = consultar(sql,ps);
			if (rs1.next()) {
				return rs1.getBoolean("VIRTUAL");
			}
			return false;
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
	}
	
	public String consultarAudienciasProcessoDescricao(String numeroProcesso) throws Exception {
		 String descricaoAudiencia = "";
		 String sql = "";            
	        ResultSetTJGO rs1 = null;
	        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	        				
	        sql = "SELECT AUDI_PROC_STATUS as statusAudiencia FROM PROJUDI.VIEW_AUDI_COMPLETA A";
	       
	        if(numeroProcesso != null && numeroProcesso != "") {
	        	sql += " WHERE A.PROC_NUMERO = ? "; 			ps.adicionarLong(numeroProcesso);
	        }
	      
	        try{
	            rs1 = this.consultar(sql, ps);

	            if (rs1.next()) {
	                descricaoAudiencia = rs1.getString("statusAudiencia");
	            }
	            // rs1.close();
	        
	        } finally{
	            try{
	            	if (rs1 != null) 
	            		rs1.close();
	            	
	            } catch(Exception e) {}
	        }
	        
	        return descricaoAudiencia;
	}
	
	//lrcampos 29/07/2019 * Busca descrição do status da AudiProc passando o idAudiProc
	public String consultarAudiProcStatusDescricaoPeIdAudiProc(String idAudiProc) throws Exception {
		 String descricaoAudiencia = "";
		 String sql = "";            
	        ResultSetTJGO rs1 = null;
	        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	        				
	        sql = "SELECT AUDI_PROC_STATUS as statusAudiencia FROM PROJUDI.VIEW_AUDI_COMPLETA A";
	       
	        if(idAudiProc != null && idAudiProc != "") {
	        	sql += " WHERE A.ID_AUDI_PROC = ? "; 			ps.adicionarLong(idAudiProc);
	        }
	      
	        try{
	            rs1 = this.consultar(sql, ps);

	            if (rs1.next()) {
	                descricaoAudiencia = rs1.getString("statusAudiencia");
	            }
	            // rs1.close();
	        
	        } finally{
	            try{
	            	if (rs1 != null) 
	            		rs1.close();
	            	
	            } catch(Exception e) {}
	        }
	        
	        return descricaoAudiencia;
	}
	
	public List<AudienciaProcessoStatusDt> consultarAudienciaProcesso( ) throws Exception {
		
		List<AudienciaProcessoStatusDt> audienciaProcessoStatusDt = new ArrayList<AudienciaProcessoStatusDt>();
		
		 String sql = "";            
	        ResultSetTJGO rs1 = null;
	        PreparedStatementTJGO ps =  new PreparedStatementTJGO();
	        				
	        sql = "SELECT A.AUDI_PROC_STATUS_CODIGO as PROC_STATUS_CODIGO,  A.AUDI_PROC_STATUS as PROC_STATUS FROM PROJUDI.VIEW_AUDI_COMPLETA A";
	       
	      
	        try{
	            rs1 = this.consultar(sql, ps);

	            while (rs1.next()) {
	            if (rs1.next()) {
	            	AudienciaProcessoStatusDt objTemp = new AudienciaProcessoStatusDt();
	            	objTemp.setAudienciaProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
	            	objTemp.setAudienciaProcessoStatus(rs1.getString("PROC_STATUS"));
	                audienciaProcessoStatusDt.add(objTemp);
	            }
	            }
	        
	        } finally{
	            try{
	            	if (rs1 != null) 
	            		rs1.close();
	            	} 
	            catch(Exception e) {}
	        }
	        
	        return audienciaProcessoStatusDt;
	}
	
	public void iniciarJulgamentoVirtual(AudienciaDt audienciaDt) throws Exception {
        String Sql;
        PreparedStatementTJGO ps = new PreparedStatementTJGO();

        Sql = "UPDATE PROJUDI.AUDI SET SESSAO_INICIADA = ? ";
        ps.adicionarBoolean(true);
        Sql += " WHERE ID_AUDI = ? ";
        ps.adicionarLong(audienciaDt.getId());      
        
        executarUpdateDelete(Sql, ps);
        
        audienciaDt.setSessaoIniciada(true);
    }
	
	public List<AudienciaProcessoVotantesDt> consultarVotantesSessaoVirtual(AudienciaProcessoDt audienciaProcesso) throws Exception {
		List<AudienciaProcessoVotantesDt> votantes = new ArrayList<AudienciaProcessoVotantesDt>();

		ResultSetTJGO rs1 = null;
		StringBuilder sql = new StringBuilder();
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();   
		
		sql.append(" SELECT "); 
		sql.append("	ID_AUDI_PROC_VOTANTES, ");
		sql.append("	ID_AUDI_PROC, ");
		sql.append("	ID_SERV_CARGO, ");
		sql.append("	RELATOR, ");
		sql.append("	ID_IMPEDIMENTO_TIPO, "); 
		sql.append("	ORDEM_VOTANTE, ");
		sql.append("	ID_VOTANTE_TIPO, "); 
		sql.append("	CONVOCADO ");  //jvosantos - 12/07/2019 16:16 - Buscar flag que indica se votante foi convocado
		sql.append(" FROM ");
		sql.append("	AUDI_PROC_VOTANTES ");
		sql.append(" WHERE ID_AUDI_PROC = ? ");
        
		ps.adicionarLong(audienciaProcesso.getId());
		
        try{
        	rs1 = this.consultar(sql.toString(), ps);   
        	
			while (rs1.next()) {
				AudienciaProcessoVotantesDt vot = new AudienciaProcessoVotantesDt();
				    	
				vot.setId(rs1.getString("ID_AUDI_PROC_VOTANTES"));
				vot.setId_AudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				vot.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
				vot.setRelator(rs1.getBoolean("RELATOR"));
				vot.setId_ImpedimentoTipo(rs1.getString("ID_IMPEDIMENTO_TIPO"));
				vot.setOrdemVotante(rs1.getString("ORDEM_VOTANTE"));
				vot.setId_VotanteTipo(rs1.getString("ID_VOTANTE_TIPO"));
				vot.setConvocado(rs1.getBoolean("CONVOCADO")); //jvosantos - 12/07/2019 16:16 - Buscar flag que indica se votante foi convocado
				    	
				votantes.add(vot);
			}
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        
		return votantes; 
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
		
		//jvosantos - 12/07/2019 16:16 - Gravar flag que indica se votante foi convocado
		SqlCampos += ",CONVOCADO";
		SqlValores+=",?";
		ps.adicionarBoolean(dados.isConvocado());
		
		SqlCampos+=")";
 		SqlValores+=")";
 		
 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

		dados.setId(executarInsert(Sql, "ID_AUDI_PROC_VOTANTES", ps));	
		
	}
	
	public List consultarAudienciasPresenciais(String idServentia) throws Exception {
    			
        String sql = "";
        List listaAudienciasPresenciais = new ArrayList();
        ResultSetTJGO rs1 = null;
        PreparedStatementTJGO ps =  new PreparedStatementTJGO();   
        
        sql = " SELECT DATA_AGENDADA, ID_AUDI";
        sql += " FROM AUDI ";
        sql += " WHERE VIRTUAL IS NULL AND SYSDATE <  DATA_AGENDADA ";
        sql += " AND ID_AUDI_TIPO = ? "; ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
        sql += " AND ID_SERV = ?"; ps.adicionarLong(idServentia);

        
        sql += " ORDER BY  DATA_AGENDADA ASC ";
        
        try{
        	rs1 = this.consultar(sql, ps);   
        	
        	while (rs1.next()) {
	            	AudienciaDt audiDt = new AudienciaDt();
	            	audiDt.setDataAgendada(Funcoes.FormatarDataHoraMinuto(rs1.getString("DATA_AGENDADA")));
	            	audiDt.setId(rs1.getString("ID_AUDI"));
	            	listaAudienciasPresenciais.add(audiDt);
	            }
            
        
        } finally{
            try{if (rs1 != null) rs1.close();} catch(Exception e) {}
        }
        return listaAudienciasPresenciais;
    }

	public List consultarProximasAudiencias(String idServentia) throws Exception {
		
	    StringBuilder sql = new StringBuilder(); // jvosantos - 16/07/2019 12:30 - Usar StringBuilder
	    List listaAudiencias = new ArrayList();
	    ResultSetTJGO rs1 = null;
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();   
	    
	    sql.append(" SELECT DATA_AGENDADA, ID_AUDI, VIRTUAL, SESSAO_INICIADA");
	    sql.append(" FROM AUDI ");
	    sql.append(" WHERE SYSDATE <  DATA_AGENDADA ");
	    sql.append(" AND ID_AUDI_TIPO = ? "); ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
	    sql.append(" AND ID_SERV = ? "); ps.adicionarLong(idServentia);
	    sql.append(" AND DATA_MOVI IS NULL"); // jvosantos - 16/07/2019 12:31 - Trazer apenas sessões que não foram finalizadas
	    
	    sql.append(" ORDER BY  DATA_AGENDADA ASC ");
	    
	    try{
	    	rs1 = this.consultar(sql.toString(), ps);   
	    	
	    	while (rs1.next()) {
	            	AudienciaDt audienciaDt = new AudienciaDt();
	            	audienciaDt.setDataAgendada(Funcoes.convertStringDateUSToStringDatePTBR(rs1.getString("DATA_AGENDADA"))); // jvosantos - 16/07/2019 12:32 - Formatar data para padrão pt_BR
	            	audienciaDt.setId(rs1.getString("ID_AUDI"));
	            	audienciaDt.setVirtual(rs1.getBoolean("VIRTUAL"));
	            	audienciaDt.setSessaoIniciada(rs1.getBoolean("SESSAO_INICIADA"));
	            	listaAudiencias.add(audienciaDt);
	            }
	        
	    
	    } finally{
	        try{if (rs1 != null) rs1.close();} catch(Exception e) {}
	    }
	    return listaAudiencias;
	}
	
	public String consultarProcessoTipoRecursoSecundarioIdAudienciaProcesso(String idAudienciaProcesso) throws Exception {
		
	    String sql = "";
	    ResultSetTJGO rs1 = null;
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();   
	    sql = " SELECT PROC_TIPO ";
	    sql += " FROM RECURSO_SECUNDARIO_PARTE R "
	    		+ " JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = R.ID_PROC_TIPO_RECURSO_SEC ";
	    sql += " WHERE ID_AUDI_PROC = ? "; ps.adicionarLong(idAudienciaProcesso);
	    sql += " AND ROWNUM = 1 "; 
	    
	    try{
	    	rs1 = this.consultar(sql, ps);   
	    	
	    	while (rs1.next()) {
	            	return rs1.getString("PROC_TIPO");
	            }
	    
	    } finally{
	        try{if (rs1 != null) rs1.close();} catch(Exception e) {}
	    }
		return null;
	}

	public String consultarProcessoTipoRecursoSecundarioIdProcesso(String idProcesso) throws Exception {
	    String sql = "";
	    ResultSetTJGO rs1 = null;
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();   
	    sql = " SELECT PT.PROC_TIPO ";
	    sql += " FROM RECURSO_SECUNDARIO_PARTE R "
	    		+ " JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = R.ID_PROC_TIPO_RECURSO_SEC "
	    		+ " JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = R.ID_AUDI_PROC ";
	    sql += " WHERE ap.ID_PROC = ? "; ps.adicionarLong(idProcesso);
	    sql += " AND AP.DATA_MOVI IS NULL ";
	    sql += " AND ROWNUM = 1 "; 
	    
	    try{
	    	rs1 = this.consultar(sql, ps);   
	    	
	    	while (rs1.next()) {
	            	return rs1.getString("PROC_TIPO");
	            }
	    
	    } finally{
	        try{if (rs1 != null) rs1.close();} catch(Exception e) {}
	    }
		return null;
	}

	// jvosantos - 04/06/2019 10:38 - Método que consulta o arquivo da ementa de uma audi_proc
	public ArquivoDt consultarArquivoEmenta(String idAudiProc) throws Exception {
	    ResultSetTJGO rs1 = null;
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();   
	    StringBuilder sql = new StringBuilder();
		ArquivoDt arquivoDt = null;
	    
		sql.append("	SELECT ID_ARQ, NOME_ARQ FROM ARQ WHERE ");
		sql.append("		ID_ARQ IN ( ");
		sql.append("		SELECT ");
		sql.append("			ID_ARQ ");
		sql.append("		FROM ");
		sql.append("			PEND_ARQ ");
		sql.append("		WHERE ");
		sql.append("			ID_PEND = ( ");
		sql.append("			SELECT ");
		sql.append("				ID_PEND_EMENTA ");
		sql.append("			FROM ");
		sql.append("				AUDI_PROC ");
		sql.append("			WHERE ");
		sql.append("				ID_AUDI_PROC = ?)) "); ps.adicionarLong(idAudiProc);
		sql.append("		AND ID_ARQ_TIPO = (SELECT ID_ARQ_TIPO FROM ARQ_TIPO WHERE ARQ_TIPO_CODIGO = ?) "); ps.adicionarLong(ArquivoTipoDt.EMENTA);
	    
	    try{
	    	rs1 = this.consultar(sql.toString(), ps);   
	    	
	    	if(rs1.next()) {
	    		arquivoDt = new ArquivoDt();
	    		arquivoDt.setId(rs1.getString("ID_ARQ"));
	    		arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
            }
	    } finally{
	        try{if (rs1 != null) rs1.close();} catch(Exception e) {}
	    }
	    
		return arquivoDt;
	}
	
	// jvosantos - 04/06/2019 10:38 - Método que consulta o arquivo do voto de uma audi_proc
	public ArquivoDt consultarArquivoVoto(String idAudiProc) throws Exception {
	    ResultSetTJGO rs1 = null;
	    PreparedStatementTJGO ps =  new PreparedStatementTJGO();   
	    StringBuilder sql = new StringBuilder();
		ArquivoDt arquivoDt = null;
	    
		sql.append("	SELECT ID_ARQ, NOME_ARQ FROM ARQ WHERE ");
		sql.append("		ID_ARQ IN ( ");
		sql.append("		SELECT ");
		sql.append("			ID_ARQ ");
		sql.append("		FROM ");
		sql.append("			PEND_ARQ ");
		sql.append("		WHERE ");
		sql.append("			ID_PEND = ( ");
		sql.append("			SELECT ");
		sql.append("				ID_PEND_VOTO ");
		sql.append("			FROM ");
		sql.append("				AUDI_PROC ");
		sql.append("			WHERE ");
		sql.append("				ID_AUDI_PROC = ?)) "); ps.adicionarLong(idAudiProc);
		sql.append("		AND ID_ARQ_TIPO = (SELECT ID_ARQ_TIPO FROM ARQ_TIPO WHERE ARQ_TIPO_CODIGO = ?) "); ps.adicionarLong(ArquivoTipoDt.RELATORIO_VOTO);
	    
	    try{
	    	rs1 = this.consultar(sql.toString(), ps);   
	    	
	    	if(rs1.next()) {
	    		arquivoDt = new ArquivoDt();
	    		arquivoDt.setId(rs1.getString("ID_ARQ"));
	    		arquivoDt.setNomeArquivo(rs1.getString("NOME_ARQ"));
            }
	    } finally{
	        try{if (rs1 != null) rs1.close();} catch(Exception e) {}
	    }
	    
		return arquivoDt;
	}

	public String consultarObservacoes(String id_audiencia )  throws Exception {

		String stSql="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		stSql= "SELECT OBSERVACOES FROM PROJUDI.AUDI WHERE ID_AUDI = ?";		ps.adicionarLong(id_audiencia); 

		try{
			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				return rs1.getString("OBSERVACOES");
			}
		}finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}

		return null; 
	}
	
}
