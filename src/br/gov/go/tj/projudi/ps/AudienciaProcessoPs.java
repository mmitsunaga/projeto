package br.gov.go.tj.projudi.ps;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.SustentacaoOralDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;
import br.gov.go.tj.projudi.ne.ImpedimentoTipoNe;
import br.gov.go.tj.projudi.ne.VotanteTipoNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AudienciaProcessoPs extends AudienciaProcessoPsGen {

	/**
     * 
     */
    private static final long serialVersionUID = -757448539786206173L;

    //tem que alterar depois
    public AudienciaProcessoPs( ) {}
    
    public AudienciaProcessoPs(Connection conexao){
    	Conexao = conexao;
	}

	/**
	 * Sobrescrevendo o método inserir da classe AudienciaProcessoPsGen.
	 */
    // jvosantos - 22/11/2019 11:55 - Refatorar método para usar StringBuilder
	public void inserir(AudienciaProcessoDt audienciaProcessoDt) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		StringBuilder sql = new StringBuilder("INSERT INTO PROJUDI.AUDI_PROC (");

		if (!(audienciaProcessoDt.getId_Audiencia().length() == 0)) sql.append("ID_AUDI");
		if (!(audienciaProcessoDt.getId_ServentiaCargo().length() == 0)) sql.append(",ID_SERV_CARGO ");
		if (!(audienciaProcessoDt.getId_Processo().length() == 0)) sql.append(",ID_PROC ");
		if ((audienciaProcessoDt.getId_AudienciaProcessoStatus().length() > 0) || (audienciaProcessoDt.getAudienciaProcessoStatusCodigo().length() > 0)) sql.append(",ID_AUDI_PROC_STATUS ");
		if (!(audienciaProcessoDt.getDataMovimentacao().length() == 0)) sql.append(",DATA_MOVI ");
		if (!(audienciaProcessoDt.getCodigoTemp().length() == 0)) sql.append(",CODIGO_TEMP ");
		if (!(audienciaProcessoDt.getDataAudienciaOriginal().length() == 0)) sql.append(",DATA_AUDI_ORIGINAL ");
		if (!(audienciaProcessoDt.getId_Audi_Proc_Origem().length() == 0)) sql.append(",ID_AUDI_PROC_ORIGEM ");
		if (!(audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada().length() == 0)) sql.append(",ID_ARQ_ATA_ADIAMENTO ");
		if (!(audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada().length() == 0)) sql.append(",ID_ARQ_ATA_INICIADO ");
		if (!(audienciaProcessoDt.getId_ProcessoTipo() == null || audienciaProcessoDt.getId_ProcessoTipo().length() == 0)) sql.append(",ID_PROC_TIPO ");
		if (audienciaProcessoDt.possuiVotoRelator()) sql.append(",ID_PEND_VOTO ");
		if (audienciaProcessoDt.possuiEmentaRelator()) sql.append(",ID_PEND_EMENTA ");
		if (audienciaProcessoDt.possuiVotoRedator()) sql.append(",ID_PEND_VOTO_REDATOR ");
		if (audienciaProcessoDt.possuiEmentaRedator()) sql.append(",ID_PEND_EMENTA_REDATOR ");
		if (StringUtils.isNotEmpty(audienciaProcessoDt.getId_ServentiaCargoMP())) sql.append(",ID_SERV_CARGO_MP "); // jvosantos - 08/10/2019 11:46 - Adicionar campo ServentiaCargoMP 
		if (StringUtils.isNotEmpty(audienciaProcessoDt.getId_ServentiaCargoPresidente())) sql.append(",ID_SERV_CARGO_PRESIDENTE "); // jvosantos - 08/10/2019 11:46 - Adicionar campo ServentiaCargoPresidente
		sql.append(",PEDIDO_SUSTENTACAO_ORAL "); // jvosantos - 22/11/2019 11:53 - Salvar flag de permite sustentação oral
		
		sql.append(") Values (");
		// Id da audiência
		if (!(audienciaProcessoDt.getId_Audiencia().length() == 0)) {
			sql.append("?");
			ps.adicionarLong(audienciaProcessoDt.getId_Audiencia());
		}
		// Id do cargo da serventia
		if (!(audienciaProcessoDt.getId_ServentiaCargo().length() == 0)) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_ServentiaCargo());
		}
		// Id do processo
		if (!(audienciaProcessoDt.getId_Processo().length() == 0)) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_Processo());
		}
		// Id do status da audiência de processo
		if (!(audienciaProcessoDt.getId_AudienciaProcessoStatus().length() == 0)) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_AudienciaProcessoStatus());
		} else if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo().length() > 0){
			sql.append(", (SELECT ID_AUDI_PROC_STATUS FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?)");
			ps.adicionarLong(audienciaProcessoDt.getAudienciaProcessoStatusCodigo());
		}

		if (!(audienciaProcessoDt.getDataMovimentacao().length() == 0)) {
			sql.append(", ?");
			ps.adicionarDateTime(audienciaProcessoDt.getDataMovimentacao());
		}
		
		if (!(audienciaProcessoDt.getCodigoTemp().length() == 0)) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getCodigoTemp());
		}
		
		if (!(audienciaProcessoDt.getDataAudienciaOriginal().length() == 0)) {
			sql.append(", ?");
			ps.adicionarDateTime(audienciaProcessoDt.getDataAudienciaOriginal());
		}
		
		if (!(audienciaProcessoDt.getId_Audi_Proc_Origem().length() == 0)) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_Audi_Proc_Origem());
		}
		
		if (!(audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada().length() == 0)) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada());
		}
		
		if (!(audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada().length() == 0)) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada());
		}
		
		if (!(audienciaProcessoDt.getId_ProcessoTipo() == null || audienciaProcessoDt.getId_ProcessoTipo().length() == 0)) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_ProcessoTipo());
		}
		
		if (audienciaProcessoDt.possuiVotoRelator()) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_PendenciaVotoRelator());
		}
		
		if (audienciaProcessoDt.possuiEmentaRelator()) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_PendenciaEmentaRelator());
		}
		
		if (audienciaProcessoDt.possuiVotoRedator()) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_PendenciaVotoRedator());
		}
		
		if (audienciaProcessoDt.possuiEmentaRedator()) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_PendenciaEmentaRedator());
		}
		
		// jvosantos - 08/10/2019 11:46 - Adicionar campo ServentiaCargoMP 
		if (StringUtils.isNotEmpty(audienciaProcessoDt.getId_ServentiaCargoMP())) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_ServentiaCargoMP());
		}
		
		// jvosantos - 08/10/2019 11:46 - Adicionar campo ServentiaCargoPresidente
		if (StringUtils.isNotEmpty(audienciaProcessoDt.getId_ServentiaCargoPresidente())) {
			sql.append(", ?");
			ps.adicionarLong(audienciaProcessoDt.getId_ServentiaCargoPresidente());
		}
		
		sql.append(", ?)");
		ps.adicionarBoolean(audienciaProcessoDt.isPermiteSustentacaoOral()); // jvosantos - 22/11/2019 11:53 - Salvar flag de permite sustentação oral

		String resSQL = sql.toString().replace("(,", "(");

		audienciaProcessoDt.setId(executarInsert(resSQL, "ID_AUDI_PROC", ps));
				
	}

	/**
	 * Método que contém a SQL que irá excluir os objetos do tipo "AudiênciaProcessoDt" livres, ou seja, os objetos de um dado id de audiência cujo id
	 * do processo é nulo
	 * 
	 * @author Keila Sousa Silva
	 * @since 24/03/2009
	 * @param audienciaProcessoDtExcluir
	 * @throws Exception
	 */
	public void excluirAudienciaProcesso(AudienciaProcessoDt audienciaProcessoDtExcluir) throws Exception {
		String sqlAudiProc;
		String sqlAudiProcResp;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sqlAudiProcResp = " DELETE FROM PROJUDI.AUDI_PROC_RESP ";
		sqlAudiProcResp += " WHERE ID_AUDI_PROC IN ( SELECT ID_AUDI_PROC ";
		sqlAudiProcResp += "                           FROM PROJUDI.AUDI_PROC ";
		sqlAudiProcResp += "                          WHERE ID_AUDI = ? ";		
		sqlAudiProcResp += " AND ID_PROC IS NULL)";
				
		sqlAudiProc = " DELETE FROM PROJUDI.AUDI_PROC";
		sqlAudiProc += " WHERE ID_AUDI = ?";		
		sqlAudiProc += " AND ID_PROC IS NULL";
		
		ps.adicionarLong(audienciaProcessoDtExcluir.getId_Audiencia());
				
		executarUpdateDelete(sqlAudiProcResp, ps);
		executarUpdateDelete(sqlAudiProc, ps);
		
	}

	/**
	 * Método que contém a sql responsável por atualizar objeto do tipo "AudienciaProcessoDt" na execuçaõ do agendamento de audiência. 
	 * É necessário atualizar os campos "ID_PROC" e "ID_AUDI_PROC_STATUS".
	 * 
	 * @author Keila Sousa Silva
	 * @since 24/04/2009
	 * @param audienciaProcessoDt
	 * @throws Exception
	 */
	public boolean alterarAudienciaProcessoAgendamento(AudienciaProcessoDt audienciaProcessoDt) throws Exception{
		
	/*
	 * |--------------------------------|
	 * |:Em 29/11/2010 por Ciro Macedo: |
	 * |--------------------------------|
	 * O presente método é chamado por um método synchronized que visa evitar "concorrencia" no agendamento de
	 * audiencias e segundo levantamento feito pelo Jesus havia um problema de desempenho com o mesmo. Após investigar a 
	 * estrutura do mesmo constatei a existecia de uma verificação no método [agendarAudienciaProcesso], que é o método
	 * que dispara a consulta do método atual, esta verificação é executada no método [audienciaProcessoAgendamentoEstaLivre] que
	 * tem como fundamento o seguinte SQL: {sql = "SELECT COUNT(1) AS QUANTIDADE FROM PROJUDI.AUDI_PROC";
	 *												sql += " WHERE ID_PROC IS NULL";
	 *												sql += " AND ID_AUDI_PROC_STATUS = " + AudienciaProcessoStatusDt.LIVRE;
	 *												sql += " AND ID_AUDI_PROC = " + Id_AudienciaProcesso;
	 *										}
	 *
	 * como a quantidade de registros na tabela de AudienciaProcesso é muito grande eu removi a verificação, e alterei o SQL do
	 * método atual que era assim : {sql = "UPDATE PROJUDI.AUDI_PROC SET";
	 *										sql += " ID_PROC = " + Funcoes.BancoInteiro(audienciaProcessoDt.getId_Processo());
	 *										sql += ", ID_AUDI_PROC_STATUS = (SELECT Id_AudienciaProcessoStatus FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = " + Funcoes.BancoInteiro(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()) + ")";
	 *										sql += " WHERE ID_AUDI_PROC = " + audienciaProcessoDt.getId();
	 *								}
	 * , o que fiz foi colocar + 2 restrições na clásulu WHERE, restrições estas que eram utilizadas no método de verificação quer
	 * realizava um COUNT na tabela. Com isso, o fato de existir um UPDATE no SQL abaixo garante que não haverá concorrencia para
	 * com o registro selecionado.
	 */		
		
		if (audienciaProcessoDt.getId_Processo()==null || audienciaProcessoDt.getId_Processo().length()==0 || audienciaProcessoDt.getId() == null || audienciaProcessoDt.getId().length()==0){
			throw new MensagemException("O Id do processo não foi informado.");
		}
	
//			String sqlVerificacao;
//			ResultSetTJGO rs1 = null;
//			PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		
////			sqlVerificacao = "SELECT ID_AUDI_PROC AS ID_AUDI_PROC FROM PROJUDI.AUDI_PROC";
////			sqlVerificacao += " WHERE ID_PROC IS NULL";
////			sqlVerificacao += " AND ID_AUDI_PROC_STATUS = ?"; 		ps.adicionarLong(AudienciaProcessoStatusDt.LIVRE);
////			sqlVerificacao += " AND ID_AUDI_PROC = ?"; 				ps.adicionarLong(audienciaProcessoDt.getId());
////			sqlVerificacao +=" FOR UPDATE ";	 
////		
////		
////			//tenta recuperar o ID da audiência pendente, se não exisitr retorna FALSE
////			rs1 = this.consultar(sqlVerificacao,ps);
////			if (rs1.next()){
////				int quantidade = rs1.getInt("ID_AUDI_PROC");
////				if(quantidade == 0){
////					return false;
////				}
////			}
		
		//conseguindo encontrar a audiencia faz a alteração do registro.
		String sql;
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
	
		
		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " ID_PROC = ?"; ps2.adicionarLong(audienciaProcessoDt.getId_Processo());						
		
		// Ao marcar uma audiência, caso o processo tenha um recurso ativo, coloca o id_proc_tipo do recurso na tabela audi_proc. Se não for o caso,
		// mantém na audi_proc o mesmo valor que já esteja neste campo. Feito para que este registro apareça na view_audi_completa.
		sql += " , ID_PROC_TIPO = NVL(";
    	sql += "							(SELECT R.ID_PROC_TIPO FROM PROC P ";
    	sql += " 							LEFT JOIN RECURSO R ON R.ID_PROC = P.ID_PROC AND P.ID_SERV = R.ID_SERV_RECURSO AND R.DATA_RETORNO is null";
    	sql += "							WHERE P.ID_PROC = ?) ";
    	sql += "							,ID_PROC_TIPO ";
    	sql += "						)";
    	ps2.adicionarLong(audienciaProcessoDt.getId_Processo());
		
		if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo() != null) {
			sql += ", ID_AUDI_PROC_STATUS = (SELECT ID_AUDI_PROC_STATUS FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?)";			
			ps2.adicionarLong(audienciaProcessoDt.getAudienciaProcessoStatusCodigo());
		} 
		
		if (audienciaProcessoDt.getId_ProcessoTipo() != null && audienciaProcessoDt.getId_ProcessoTipo().trim().length() > 0) {
			sql += ", ID_PROC_TIPO = ?";			
			ps2.adicionarLong(audienciaProcessoDt.getId_ProcessoTipo());
		} 
		
		if (audienciaProcessoDt.possuiVotoRelator()) {
			sql += ", ID_PEND_VOTO = ?";			
			ps2.adicionarLong(audienciaProcessoDt.getId_PendenciaVotoRelator());
		}
		
		if (audienciaProcessoDt.possuiEmentaRelator()) {
			sql += ", ID_PEND_EMENTA = ?";			
			ps2.adicionarLong(audienciaProcessoDt.getId_PendenciaEmentaRelator());
		}
		
		if (audienciaProcessoDt.possuiVotoRedator()) {
			sql += ", ID_PEND_VOTO_REDATOR = ?";			
			ps2.adicionarLong(audienciaProcessoDt.getId_PendenciaVotoRedator());
		}
		
		if (audienciaProcessoDt.possuiEmentaRedator()) {
			sql += ", ID_PEND_EMENTA_REDATOR = ?";			
			ps2.adicionarLong(audienciaProcessoDt.getId_PendenciaEmentaRedator());
		}
		
		sql += " WHERE ID_AUDI_PROC = ?"; ps2.adicionarLong(audienciaProcessoDt.getId());			
		sql += " AND ID_PROC IS NULL ";
		sql += " AND CODIGO_TEMP IS NULL ";
		sql += " AND ID_AUDI_PROC_STATUS = ?"; ps2.adicionarLong(AudienciaProcessoStatusDt.LIVRE);
				
		//se tiver alterado algum registro retorna true, senão retorna false
		//Validação para evitar concorrência no agendamento de audiências na seguinte situação: usuário inicia o agendamento de uma audiência e deixa o sistema aguardando na tela
		//de confirmação final. Enquanto o sistema aguarda o CONFIRMAR do usuário, outro usuário (em outra máquina) finaliza o agendamento da mesma audiência. Quando isso ocorre
		//o usuário que deixou o sistema aguardando está recebendo a confirmação do agendamento e a capa do processo é atualizada. Esse método foi criado para impedir que isso
		//ocorra. Caso esse cenário tenha ocorrido, esse método bloqueará a tentativa de agendamento duplicada.
		return executarUpdateDelete(sql, ps2) > 0;						
	
	}

	/**
	 * Método que atualiza o status de uma audiência
	 * 
	 * @param id_AudienciaProcesso, identificação da audiência
	 * @param novoStatus, novo status da audiência
	 * @author msapaula
	 */
	public void alterarStatusAudiencia(String id_AudienciaProcesso, int novoStatus) throws Exception {
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " ID_AUDI_PROC_STATUS  = ?";
		ps.adicionarLong(novoStatus);
		sql = sql + " WHERE ID_AUDI_PROC  = ?";
		if (id_AudienciaProcesso != null) {
			ps.adicionarLong(id_AudienciaProcesso);
		} else {
			ps.adicionarLongNull();
		}
		
		executarUpdateDelete(sql, ps);
		
	}

	/**
	 * Método que atualiza o responsável por uma audiência
	 * 
	 * @param id_AudienciaProcesso, identificação da audiência
	 * @param id_NovoServentiaCargo, novo ServentiaCargo responsável pela audiência
	 * @author msapaula
	 */
	public void alterarResponsavelAudiencia(String id_AudienciaProcesso, String id_NovoServentiaCargo) throws Exception {
		String sql;
		PreparedStatementTJGO  ps = new PreparedStatementTJGO();
		
		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " ID_SERV_CARGO  = ?";
		if (id_NovoServentiaCargo != null) {
			ps.adicionarLong(id_NovoServentiaCargo);
		} else {
			ps.adicionarLongNull();
		}
		
		sql = sql + " WHERE ID_AUDI_PROC  = ? ";
		
		if(id_AudienciaProcesso != null) {
			ps.adicionarLong(id_AudienciaProcesso);
		} else {
			ps.adicionarLongNull();
		}

		executarUpdateDelete(sql, ps);

	}

	/**
	 * Consultar se há audiências pendentes em um processo, podendo filtrar por um determinado tipo ou retornar todas
	 * 
	 * @param id_Processo, identificação do processo
	 * @param audienciaTipoCodigo, tipo da audiência para realizar a consulta de pendentes
	 * 
	 * @author msapaula
	 * @since 02/03/2009 14:24
	 * 
	 * @return List<String[]>, contendo detalhamento de audiencia em aberto
	 */
	public List<String[]> consultarAudienciasPendentesProcesso(String id_Processo, Integer audienciaTipoCodigo, String id_processoTipo) throws Exception {
		List<String[]> audienciasPendentes = new ArrayList<String[]>();
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT ID_AUDI_PROC, AUDI_TIPO, DATA_AGENDADA, SERV_CARGO, ID_ARQ_ATA, VIRTUAL, SESSAO_INICIADA, ";
		sql += " (SELECT AUDI_PROC_STATUS FROM PROJUDI.AUDI_PROC_STATUS aps WHERE ap.ID_AUDI_PROC_STATUS_ANA = aps.ID_AUDI_PROC_STATUS) AUDI_PROC_STATUS,";
		sql += " (SELECT NOME_USU FROM PROJUDI.VIEW_SERV_CARGO sc WHERE sc.ID_SERV_CARGO = ap.ID_SERV_CARGO) NOME_USU,";
		sql += " ID_AUDI_TIPO,";
		
		sql += " (SELECT ID_SERV FROM PROJUDI.VIEW_SERV_CARGO sc WHERE sc.ID_SERV_CARGO = ap.ID_SERV_CARGO) ID_SERV,";
		sql += " (SELECT SERV FROM PROJUDI.VIEW_SERV_CARGO sc WHERE sc.ID_SERV_CARGO = ap.ID_SERV_CARGO) SERV,";		
		sql += " AUDI_TIPO_CODIGO,";
		sql += " PROC_TIPO_AUDIENCIA,";
		
		sql += " (SELECT COUNT(pa.ID_ARQ) ";
		sql += "   FROM PROJUDI.VIEW_PEND_ABERTAS_SERV_CARGO p ";
		sql += "  INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = 1) ";
		sql += "  INNER JOIN PROJUDI.ARQ a on (pa.ID_ARQ = a.ID_ARQ  AND a.USU_ASSINADOR IS NULL)";
		sql += "  WHERE (p.ID_PEND = ap.ID_PEND_VOTO OR p.ID_PEND = ap.ID_PEND_VOTO_REDATOR)) QTDE_ARQ, ";
		
		sql += " (SELECT pa.CODIGO_TEMP ";
		sql += "   FROM PROJUDI.VIEW_PEND_ABERTAS_SERV_CARGO p ";
		sql += "  INNER JOIN PROJUDI.PEND_ARQ pa on (p.ID_PEND = pa.ID_PEND AND pa.RESPOSTA = 1) ";
		sql += "  INNER JOIN PROJUDI.ARQ a on (pa.ID_ARQ = a.ID_ARQ  AND a.USU_ASSINADOR IS NULL)";
		sql += "  WHERE (p.ID_PEND = ap.ID_PEND_VOTO OR p.ID_PEND = ap.ID_PEND_VOTO_REDATOR) AND ROWNUM = 1) CODIGO_TEMP, ";
		
		sql += "  (SELECT PT.PROC_TIPO FROM RECURSO_SECUNDARIO_PARTE RSP "
				+ " JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC "
				+ "WHERE RSP.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1) RECURSO_SECUNDARIO ";
		
		sql += "  ,ap.SERV_CARGO_REDATOR, ap.NOME_REDATOR ";
		sql += " FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA ap";
		sql += " WHERE ap.ID_PROC =  ?";
		ps.adicionarLong(id_Processo); 
		sql += " AND ap.DATA_MOVI_AUDI_PROC IS NULL ";
		if (audienciaTipoCodigo != null) {
			sql += " AND ap.AUDI_TIPO_CODIGO = ?";
			ps.adicionarLong(audienciaTipoCodigo);
		} 
		if (id_processoTipo != null && id_processoTipo.trim().length() > 0) {
			sql += " AND ap.ID_PROC_TIPO_AUDIENCIA = ?";
			ps.adicionarLong(id_processoTipo);
		}
		sql += " AND ap.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				String tipo = rs1.getString("AUDI_TIPO");				
				if (rs1.isNull("VIRTUAL") || !rs1.getBoolean("VIRTUAL")) {
					tipo += " [PRESENCIAL]";
				} else {
					tipo += " [VIRTUAL]";
				}
				
				if (Funcoes.StringToInt(rs1.getString("ID_AUDI_TIPO")) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo() && rs1.getString("PROC_TIPO_AUDIENCIA") != null && rs1.getString("PROC_TIPO_AUDIENCIA").trim().length() > 0) {
					if(!rs1.isNull("RECURSO_SECUNDARIO")) {
						tipo += " - " + rs1.getString("RECURSO_SECUNDARIO");
					}
					tipo += " - " + rs1.getString("PROC_TIPO_AUDIENCIA");
				}
				String status = "Aguardando Realização";
				if (!rs1.isNull("ID_ARQ_ATA")) {
					status = "AGUARDANDO ACÓRDAO/EMENTA";
					if (Funcoes.StringToInt(rs1.getString("QTDE_ARQ")) > 0) {
						status += " (Pré-Analisada"; 
						if (Funcoes.StringToInt(rs1.getString("CODIGO_TEMP")) == -3) {
							status += "/Aguardando Assinatura";
						}
						status += ")";
					}
				}				
				audienciasPendentes.add(new String[] {rs1.getString("ID_AUDI_PROC"), 
						                tipo, 
						                Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")), 
						                rs1.getString("SERV_CARGO") + " - " + rs1.getString("NOME_USU"), 
						                rs1.getString("ID_ARQ_ATA") , 
						                rs1.getString("AUDI_PROC_STATUS"), 
						                rs1.getString("ID_AUDI_TIPO"), 
						                rs1.getString("ID_SERV"), 
						                rs1.getString("SERV"), 
						                rs1.getString("AUDI_TIPO_CODIGO"), 
						                status, 
						                (rs1.isNull("NOME_REDATOR") || rs1.isNull("NOME_REDATOR") ? "" : rs1.getString("SERV_CARGO_REDATOR") + " - " + rs1.getString("NOME_REDATOR")),
                                        "true" });
			}		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return audienciasPendentes;
	}

	/**
	 * Consultar se há audiência pendente em um processo
	 * 
	 * @param id_Processo, identificação do processo
	 * @param id_ServentiaCargo, identificação do cargo
	 * @author msapaula
	 */
	public AudienciaProcessoDt consultarAudienciaPendenteProcesso(String id_Processo, String id_ServentiaCargo, String id_ProcessoTipoSessao) throws Exception {
		AudienciaProcessoDt audienciaProcessoDt = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT * FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA ap";
		sql += " WHERE ap.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		sql  += " AND ap.DATA_MOVI_AUDI_PROC IS NULL ";
		if (id_ServentiaCargo != null) {
			sql += " AND ap.ID_SERV_CARGO = ?";
			ps.adicionarLong(id_ServentiaCargo);
		}
		sql += " AND ap.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		if (id_ProcessoTipoSessao != null && id_ProcessoTipoSessao.trim().length() > 0) {
			sql += " AND ap.ID_PROC_TIPO_AUDIENCIA = ?";
			ps.adicionarLong(id_ProcessoTipoSessao);
		}

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				audienciaProcessoDt = new AudienciaProcessoDt();
				this.associarAudienciaProcessoDt(audienciaProcessoDt, rs1);
				audienciaProcessoDt.setId_ServentiaCargoPresidente(rs1.getString("ID_SERV_CARGO_PRESIDENTE"));
				audienciaProcessoDt.setServentiaCargoPresidente(rs1.getString("SERV_CARGO_PRESIDENTE") + " - " + rs1.getString("NOME_PRESIDENTE"));			 
				audienciaProcessoDt.setId_ServentiaMP(rs1.getString("ID_SERV_MP"));
				audienciaProcessoDt.setServentiaMP(rs1.getString("SERV_MP"));
				audienciaProcessoDt.setId_ServentiaCargoMP(rs1.getString("ID_SERV_CARGO_MP"));
				audienciaProcessoDt.setServentiaCargoMP(rs1.getString("SERV_CARGO_MP") + " - " + rs1.getString("NOME_MP"));
				audienciaProcessoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_AUDIENCIA"));
				audienciaProcessoDt.setProcessoTipo(rs1.getString("PROC_TIPO_AUDIENCIA"));
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return audienciaProcessoDt;
	}

	/**
	 * Consultar se há audiência pendentes para um ServentiaCargo
	 * 
	 * @param id_ServentiaCargo, identificação do cargo
	 * @author msapaula
	 */
	public int consultarAudienciasPendentesServentiaCargo(String id_ServentiaCargo) throws Exception {
		int qtde = 0;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = "SELECT COUNT(1) AS QUANTIDADE FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA ap";
		sql += " WHERE ap.ID_SERV_CARGO = ? ";
		ps.adicionarLong(id_ServentiaCargo);
		sql += " AND ap.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		try{
			 rs1 = this.consultar(sql,ps);
			if (rs1.next()) qtde = rs1.getInt("QUANTIDADE");
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return qtde;
	}

	/**
	 * Retorna a sessão do 2º grau marcada para o processo passado
	 * 
	 * @param id_Processo, identificação do proceso
	 * @param id_ProcessoTipo, id processo tipo
	 * @author msapaula
	 */
	public AudienciaProcessoDt consultarSessaoMarcada(String id_Processo, String id_ProcessoTipo) throws Exception {
		AudienciaProcessoDt sessao = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
		String sql = " SELECT ac.ID_AUDI_PROC, ac.ID_PROC, ac.ID_AUDI, ac.DATA_AGENDADA, ac.AUDI_TIPO_CODIGO, ac.ID_ARQ_ATA, ac.ID_PROC_TIPO_AUDIENCIA ";
		sql += " 	FROM PROJUDI.VIEW_AUDI_COMPLETA ac";
		sql += " 	WHERE ac.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		sql += " 	AND ac.AUDI_TIPO_CODIGO =  ?";
		ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		sql += " 	AND ac.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0) {
			sql += " 	AND ac.ID_PROC_TIPO_AUDIENCIA = ?";
			ps.adicionarLong(id_ProcessoTipo);
		}
		sql += " union";
		sql += " SELECT ar.ID_AUDI_PROC, ar.ID_PROC, ar.ID_AUDI, ar.DATA_AGENDADA, ar.AUDI_TIPO_CODIGO, NULL AS ID_ARQ_ATA, ar.ID_PROC_TIPO_AUDIENCIA ";
		sql += " 	FROM PROJUDI.VIEW_AUDI_RECURSO_COMPLETA ar";
		sql += " 	WHERE ar.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		sql += " 	AND ar.AUDI_TIPO_CODIGO = ?";
		ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		sql += " 	AND ar.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0) {
			sql += " 	AND ar.ID_PROC_TIPO_AUDIENCIA = ?";
			ps.adicionarLong(id_ProcessoTipo);
		}
		
		sql =  "SELECT * FROM (" + sql + ")	ORDER BY ID_AUDI_PROC ";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);

			if (rs1.next()) {
				sessao = new AudienciaProcessoDt();
				sessao.setId(rs1.getString("ID_AUDI_PROC"));
				sessao.setId_Processo(rs1.getString("ID_PROC"));
				sessao.setId_ArquivoAta(rs1.getString("ID_ARQ_ATA"));
				sessao.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_AUDIENCIA"));

				AudienciaDt audienciaDt = new AudienciaDt();
				audienciaDt.setId(rs1.getString("ID_AUDI"));
				audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
				audienciaDt.setAudienciaTipoCodigo(rs1.getString("AUDI_TIPO_CODIGO"));				
				sessao.setAudienciaDt(audienciaDt);
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return sessao;
	}
	
	/**
	 * Retorna as sessões do 2º grau marcadas para o processo passado
	 * 
	 * @param id_Processo, identificação do proceso
	 * @author mmgomes
	 */
	public List<AudienciaProcessoDt> consultarSessoesMarcadas(String id_Processo) throws Exception {
		List<AudienciaProcessoDt> sessoesAbertas = new ArrayList<AudienciaProcessoDt>();
		AudienciaProcessoDt sessao = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
			
		String sql = " SELECT ac.ID_AUDI_PROC, ac.ID_PROC, ac.ID_AUDI, ac.sessao_iniciada, ac.DATA_AGENDADA, ac.AUDI_TIPO_CODIGO, ac.ID_ARQ_ATA, ac.ID_PROC_TIPO_AUDIENCIA ";
		sql += " 	FROM PROJUDI.VIEW_AUDI_COMPLETA ac";
		sql += " 	WHERE ac.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		sql += " 	AND ac.AUDI_TIPO_CODIGO =  ?";
		ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		sql += " 	AND ac.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql += " union";
		sql += " SELECT ar.ID_AUDI_PROC, ar.ID_PROC, ar.ID_AUDI, ar.sessao_iniciada, ar.DATA_AGENDADA, ar.AUDI_TIPO_CODIGO, NULL AS ID_ARQ_ATA, ar.ID_PROC_TIPO_AUDIENCIA ";
		sql += " 	FROM PROJUDI.VIEW_AUDI_RECURSO_COMPLETA ar";
		sql += " 	WHERE ar.ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		sql += " 	AND ar.AUDI_TIPO_CODIGO = ?";
		ps.adicionarLong(AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo());
		sql += " 	AND ar.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		
		sql =  "SELECT * FROM (" + sql + ")	ORDER BY ID_AUDI_PROC ";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);

			while (rs1.next()) {
				sessao = new AudienciaProcessoDt();
				sessao.setId(rs1.getString("ID_AUDI_PROC"));
				sessao.setId_Processo(rs1.getString("ID_PROC"));
				sessao.setId_ArquivoAta(rs1.getString("ID_ARQ_ATA"));
				sessao.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_AUDIENCIA"));

				AudienciaDt audienciaDt = new AudienciaDt();
				audienciaDt.setId(rs1.getString("ID_AUDI"));
				audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
				audienciaDt.setAudienciaTipoCodigo(rs1.getString("AUDI_TIPO_CODIGO"));
				//lrcampos 07/11/2019 12:35 set no objeto se a sessão foi iniciada.
				audienciaDt.setSessaoIniciada(rs1.getBoolean("SESSAO_INICIADA"));
				sessao.setAudienciaDt(audienciaDt);
				
				sessoesAbertas.add(sessao);
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return sessoesAbertas;
	}
	
	/**
     * Método responsável em montar e retornar os campos das views VIEW_AUDI_COMPLETA e VIEW_AUDI_COMPLETA_PRIM_GRAU 
     * 
     * @return
     */
    private String obtenhaListaCamposViewAudiCompleta() {
    	String sql = ""; 
    	sql = "SELECT ID_AUDI, ID_AUDI_TIPO, AUDI_TIPO, AUDI_TIPO_CODIGO, DATA_AGENDADA, DATA_MOVI_AUDI, RESERVADA, ";
    	sql += " ID_AUDI_PROC, ID_AUDI_PROC_STATUS, AUDI_PROC_STATUS, AUDI_PROC_STATUS_CODIGO, ID_SERV_CARGO, SERV_CARGO, ";
    	sql += " USU, NOME, ID_SERV, SERV, ID_PROC, PROC_NUMERO, DIGITO_VERIFICADOR, PROC_NUMERO_COMPLETO, DATA_MOVI_AUDI_PROC, ";
    	sql += " ID_RECURSO, DATA_RETORNO, CODIGO_TEMP, CODIGO_TEMP_AUDI_PROC, ID_AUDI_PROC_STATUS_ANA, AUDI_PROC_STATUS_ANA, ";
    	sql += " AUDI_PROC_STATUS_CODIGO_ANA, ID_ARQ_ATA, DATA_AUDI_ORIGINAL, ID_AUDI_PROC_ORIGEM, ID_ARQ_ATA_ADIAMENTO, ID_ARQ_ATA_INICIADO, ";
    	sql += " ORDEM_2_GRAU,  ID_PROC_TIPO, PROC_TIPO_CODIGO, PROC_TIPO, ID_ARQ_FINALIZACAO, ID_SERV_CARGO_PRESIDENTE, ";
    	sql += " SERV_CARGO_PRESIDENTE, USU_PRESIDENTE, NOME_PRESIDENTE, ID_SERV_PRESIDENTE, SERV_PRESIDENTE, ID_SERV_CARGO_MP, ";
    	sql += " SERV_CARGO_MP, USU_MP, NOME_MP, ID_SERV_MP, SERV_MP, ID_SERV_CARGO_REDATOR, SERV_CARGO_REDATOR, USU_REDATOR, ";
    	sql += " NOME_REDATOR, ID_SERV_REDATOR, SERV_REDATOR, PRIMEIRO_PROMOVENTE_RECORRENTE, PRIMEIRO_PROMOVIDO_RECORRIDO, ";
    	sql += " ID_PROC_TIPO_AUDIENCIA, PROC_TIPO_AUDIENCIA, POLO_ATIVO, POLO_PASSIVO, ID_PEND_VOTO, ID_PEND_EMENTA, SESSAO_INICIADA, ID_PEND_VOTO_REDATOR, ID_PEND_EMENTA_REDATOR ";
        return sql;
    }
	
	/**
	 * Retorna os dados completos de uma AudienciaProcesso, já setando os dados da audiência vinculada
	 * 
	 * @param id_AudienciaProcesso, identificação da AudienciaProcesso
	 * @author msapaula
	 */
	public AudienciaProcessoDt consultarIdCompleto(String id_AudienciaProcesso) throws Exception {
		AudienciaProcessoDt audienciaProcessoDt = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = obtenhaListaCamposViewAudiCompleta(); 
		sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA ac";
		sql += " 	WHERE ac.ID_AUDI_PROC = ?";
		ps.adicionarLong(id_AudienciaProcesso);
		sql += " 	ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC ";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);

			if (rs1.next()) {
				audienciaProcessoDt = new AudienciaProcessoDt();
				this.associarAudienciaProcessoDt(audienciaProcessoDt, rs1);
				audienciaProcessoDt.setNomeResponsavel(rs1.getString("NOME"));
				audienciaProcessoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				audienciaProcessoDt.setNomeMPProcesso(rs1.getString("NOME_MP"));

				AudienciaDt audienciaDt = new AudienciaDt();
				audienciaDt.setId(rs1.getString("ID_AUDI"));
				audienciaDt.setDataAgendada(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AGENDADA")));
				audienciaDt.setSessaoIniciada(rs1.getBoolean("SESSAO_INICIADA"));
				audienciaProcessoDt.setAudienciaDt(audienciaDt);
			}
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return audienciaProcessoDt;
	}

	/**
	 * Atualiza os dados de uma "AudienciaProcesso" em virtude de uma movimentação
	 * 
	 * @param audienciaProcessoDt, objeto com dados a serem atualizados
	 * 
	 * @author msapaula
	 */
	public void alterarAudienciaProcessoMovimentacao(AudienciaProcessoDt audienciaProcessoDt) throws Exception {
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.AUDI_PROC SET DATA_MOVI = ?";
		
		if (audienciaProcessoDt.getDataMovimentacao() != null) {
			ps.adicionarDateTime(audienciaProcessoDt.getDataMovimentacao());
		} else {
			ps.adicionarDateTimeNull();
		}
		sql += ", ID_AUDI_PROC_STATUS = (SELECT ID_AUDI_PROC_STATUS FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?)";
		
		if (audienciaProcessoDt.getAudienciaProcessoStatusCodigo() != null) {
			ps.adicionarLong(audienciaProcessoDt.getAudienciaProcessoStatusCodigo());
		} else {
			ps.adicionarLongNull();
		}
		
		sql += ", CODIGO_TEMP = ?";
		if (audienciaProcessoDt.getCodigoTemp() != null && audienciaProcessoDt.getCodigoTemp().trim().length() > 0){			
			ps.adicionarLong(audienciaProcessoDt.getCodigoTemp().trim());		
		} else {
			ps.adicionarLongNull();
		}
		
		sql += ", ID_AUDI_PROC_STATUS_ANA = (SELECT ID_AUDI_PROC_STATUS FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?)";
		
		if (audienciaProcessoDt.getAudienciaProcessoStatusCodigoAnalista() != null) {
			ps.adicionarLong(audienciaProcessoDt.getAudienciaProcessoStatusCodigoAnalista());
		} else {
			ps.adicionarLongNull();
		}	
		
		sql += ", ID_ARQ_ATA = ?";
		if (audienciaProcessoDt.getId_ArquivoAta() != null && audienciaProcessoDt.getId_ArquivoAta().trim().length() > 0){			
			ps.adicionarLong(audienciaProcessoDt.getId_ArquivoAta().trim());		
		} else {
			ps.adicionarLongNull();
		}
		
		sql += ", ID_ARQ_ATA_ADIAMENTO = ?";
		if (audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada() != null && audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada().trim().length() > 0){			
			ps.adicionarLong(audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada().trim());		
		} else {
			ps.adicionarLongNull();
		}
		
		sql += ", ID_ARQ_ATA_INICIADO = ?";
		if (audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada() != null && audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada().trim().length() > 0){			
			ps.adicionarLong(audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada().trim());		
		} else {
			ps.adicionarLongNull();
		}
		
		sql += ", ID_SERV_CARGO_PRESIDENTE = ?";
		if (audienciaProcessoDt.getId_ServentiaCargoPresidente() != null && audienciaProcessoDt.getId_ServentiaCargoPresidente().trim().length() > 0){			
			ps.adicionarLong(audienciaProcessoDt.getId_ServentiaCargoPresidente().trim());		
		} else {
			ps.adicionarLongNull();
		}
		
		sql += ", ID_SERV_CARGO_MP = ?";
		if (audienciaProcessoDt.getId_ServentiaCargoMP() != null && audienciaProcessoDt.getId_ServentiaCargoMP().trim().length() > 0){			
			ps.adicionarLong(audienciaProcessoDt.getId_ServentiaCargoMP().trim());		
		} else {
			ps.adicionarLongNull();
		}
		
		sql += ", ID_SERV_CARGO_REDATOR = ?";
		if (audienciaProcessoDt.getId_ServentiaCargoRedator() != null && audienciaProcessoDt.getId_ServentiaCargoRedator().trim().length() > 0){			
			ps.adicionarLong(audienciaProcessoDt.getId_ServentiaCargoRedator().trim());		
		} else {
			ps.adicionarLongNull();
		}
		
		sql += ", ACORDO = ?"; 
		if (audienciaProcessoDt.getAcordo() != null && audienciaProcessoDt.getAcordo().trim().length() > 0){			
			ps.adicionarBoolean(audienciaProcessoDt.isHouveAcordo());		
		} else {
			ps.adicionarBooleanNull();
		}
		
		sql += ", VALOR_ACORDO = ?"; 
		if (audienciaProcessoDt.isHouveAcordo() && audienciaProcessoDt.getValorAcordo() != null && audienciaProcessoDt.getValorAcordo().trim().length() > 0){			
			ps.adicionarDecimal(audienciaProcessoDt.getValorAcordo());		
		} else {
			ps.adicionarDecimalNull();
		}
		
		sql += " WHERE ID_AUDI_PROC = ?";
		
		ps.adicionarLong(audienciaProcessoDt.getId());
		
		executarUpdateDelete(sql, ps);

	}

	/**
	 * Método para associar os dados de "AudienciaProcesso" de acordo com campos definidos nas visões
	 * para consulta de audiências
	 * 
	 * @param audienciaProcessoDt
	 * @param rs
	 * @throws Exception 
	 */
	protected void associarAudienciaProcessoDt(AudienciaProcessoDt Dados, ResultSetTJGO rs1) throws Exception{
		
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

		// jvosantos - 18/07/2019 15:08 - Associar campos de PEND_EMENTA e PEND_VOTO da sessão virtual, foi adicionado um try-catch vazio para não causar nenhum problema.
		try {
			if (!rs1.isNull("ID_PEND_EMENTA")) Dados.setId_PendenciaEmentaRelator(rs1.getString("ID_PEND_EMENTA"));
			if (!rs1.isNull("ID_PEND_VOTO")) Dados.setId_PendenciaVotoRelator(rs1.getString("ID_PEND_VOTO"));
			if (!rs1.isNull("ID_PEND_EMENTA_REDATOR")) Dados.setId_PendenciaEmentaRedator(rs1.getString("ID_PEND_EMENTA_REDATOR"));
			if (!rs1.isNull("ID_PEND_VOTO_REDATOR")) Dados.setId_PendenciaVotoRedator(rs1.getString("ID_PEND_VOTO_REDATOR"));
		}catch (Exception e) {}

		// alsqueiroz - 11/11/2019 17:28 - Associar campos da SERV_CARGO, foi adicionado um try-catch vazio para não causar nenhum problema.
		try {
			if (!rs1.isNull("ID_SERV_CARGO_PRESIDENTE")) Dados.setId_ServentiaCargoPresidente(rs1.getString("ID_SERV_CARGO_PRESIDENTE"));
			if (!rs1.isNull("SERV_CARGO_PRESIDENTE")) Dados.setServentiaCargoPresidente(rs1.getString("SERV_CARGO_PRESIDENTE"));
			if (!rs1.isNull("ID_SERV_CARGO_MP")) Dados.setId_ServentiaCargoMP(rs1.getString("ID_SERV_CARGO_MP"));
			if (!rs1.isNull("SERV_CARGO_MP")) Dados.setServentiaCargoMP(rs1.getString("SERV_CARGO_MP"));
		}catch (Exception e) {}
	}

	/**
	 * Método responsável em consultar todos os processos vinculados a uma audiência passada.
	 * Retorna uma lista de objetos "AudienciaProcessoDt".
	 * 
	 * @param id_Audiencia, identificação da audiência selecionada
	 * @param pendentes, booleano que define se devem ser retornados todos ou somente aqueles com status "A ser Realizada".
	 * @return List listaSessoes
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAudienciaProcessos(String id_Audiencia, boolean pendentes) throws Exception {
		String sql = "";
		List listaAudienciaProcesso = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = obtenhaListaCamposViewAudiCompleta();
		sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA a";
		sql += " WHERE a.ID_AUDI = ?";
		ps.adicionarLong(id_Audiencia);
		if (pendentes) {
			sql += " AND a.AUDI_PROC_STATUS_CODIGO = ?";
			ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			//sql += " AND a.DATA_RETORNO IS NULL";
		}		
		sql += " ORDER BY a.SEGREDO_JUSTICA, a.ORDEM_2_GRAU, a.PROC_TIPO, a.ID_AUDI_PROC"; //, a.DATA_AUDI_ORIGINAL, a.DATA_AGENDADA, a.DATA_MOVI_AUDI_PROC";
		try{
			rs1 = consultar(sql,ps);
			//Chama método para devolver lista já com processos e partes setados
			listaAudienciaProcesso = this.getListaAudienciaProcessoCompleta(rs1);
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return listaAudienciaProcesso;
	}
	
	public List consultarPautaSessao2Grau(String id_Audiencia, List ordenacaoProcessoTipo, boolean pendentes) throws Exception {
		String sql = "";
		List listaAudienciaProcesso = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		for (int i = 0; i < ordenacaoProcessoTipo.size(); i++) {
			sql = obtenhaListaCamposViewAudiCompleta();
			sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA a";
			sql += " WHERE a.ID_AUDI = ?";
			ps.adicionarLong(id_Audiencia);
			if (pendentes) {
				sql += " AND a.AUDI_PROC_STATUS_CODIGO = ?";
				ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			}		
			sql += " 	AND a.ID_PROC_TIPO = ? ";
			ps.adicionarLong(ordenacaoProcessoTipo.get(i).toString());
			sql += " ORDER BY a.SEGREDO_JUSTICA, a.ORDEM_2_GRAU, a.PROC_TIPO, a.ID_AUDI_PROC"; //, a.DATA_AUDI_ORIGINAL, a.DATA_AGENDADA, a.DATA_MOVI_AUDI_PROC";
			
			try{
				rs1 = consultar(sql,ps);
				//Chama método para devolver lista já com processos e partes setados
				listaAudienciaProcesso = this.getListaAudienciaProcessoCompleta(rs1);
				//rs1.close();
				
			} finally{
				//se for a última execução, fechar o resultSet
				if(i == ordenacaoProcessoTipo.size() - 1) {
					try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
				} else {
					rs1 = null;
				}
			}  
			
		}
		
		return listaAudienciaProcesso;
	}

	/**
	 * Método responsável por montar a lista de "AudienciaProcesso" com os respectivos processo(s) e parte(s), de acordo com um ResultSetTJGO recebido como
	 * parâmetro, retornando uma lista de objeto(s) do tipo AudienciaProcessoDt pronto(s), ou seja, com os dados necessários para exibição na tela
	 * 
	 * @param rs1, resultado da consulta
	 * @return List listaAudienciaProcesso: lista de objetos do tipo AudienciaProcessoDt contendo o(s) processo(s) e parte(s)
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	private List getListaAudienciaProcessoCompleta(ResultSetTJGO rs1) throws Exception{
		List listaAudienciaProcesso = new ArrayList();
		ProcessoDt processoDt = null;
		//String processos = "";
		//String recursos = "";
		//Map mapProcesso = new LinkedHashMap();
		//String processoAnterior = "";
		
		//PreparedStatementTJGO psProcessos = new PreparedStatementTJGO();
		//PreparedStatementTJGO psRecursos = new PreparedStatementTJGO();

		while (rs1.next()) {

			// SET AUDIÊNCIAPROCESSODT E PROCESSODT
			if (rs1.getString("ID_PROC") != null) {

				// SET AUDIÊNCIAPROCESSODT
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
				audienciaProcessoDt.setDataAudienciaOriginal(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_AUDI_ORIGINAL")));
				audienciaProcessoDt.setId_Audi_Proc_Origem(rs1.getString("ID_AUDI_PROC_ORIGEM"));
				audienciaProcessoDt.setId_ArquivoAtaSessaoAdiada(rs1.getString("ID_ARQ_ATA_ADIAMENTO"));
				audienciaProcessoDt.setId_ArquivoAtaSessaoIniciada(rs1.getString("ID_ARQ_ATA_INICIADO"));
				audienciaProcessoDt.setId_ServentiaRedator(rs1.getString("ID_SERV_REDATOR"));
				audienciaProcessoDt.setId_ServentiaResponsavel(rs1.getString("ID_SERV"));
				audienciaProcessoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO_AUDIENCIA"));
				audienciaProcessoDt.setProcessoTipo(rs1.getString("PROC_TIPO_AUDIENCIA"));
				audienciaProcessoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
				audienciaProcessoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
				if (!rs1.isNull("ID_PEND_EMENTA")) audienciaProcessoDt.setId_PendenciaEmentaRelator(rs1.getString("ID_PEND_EMENTA"));
				if (!rs1.isNull("ID_PEND_VOTO")) audienciaProcessoDt.setId_PendenciaVotoRelator(rs1.getString("ID_PEND_VOTO"));
				if (!rs1.isNull("ID_PEND_EMENTA_REDATOR")) audienciaProcessoDt.setId_PendenciaEmentaRedator(rs1.getString("ID_PEND_EMENTA_REDATOR"));
				if (!rs1.isNull("ID_PEND_VOTO_REDATOR")) audienciaProcessoDt.setId_PendenciaVotoRedator(rs1.getString("ID_PEND_VOTO_REDATOR"));
				
				//if (!rs1.getString("ID_PROC").equalsIgnoreCase(processoAnterior)) {
				// SET PROCESSODT
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
				//}
				audienciaProcessoDt.setProcessoDt(processoDt);
				
				ProcessoParteDt primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE_RECORRENTE"));
                if (primeiroPromoventeRecorrente != null) processoDt.addListaPoloAtivo(primeiroPromoventeRecorrente);
                
                ProcessoParteDt primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO_RECORRIDO"));
                if (primeiroPromovidoRecorrido != null) processoDt.addListaPolosPassivos(primeiroPromovidoRecorrido);    

//				//Concatena os processos encontrados para depois buscar as partes desses
//				if (processoDt.getId_Recurso().length() > 0){
//					recursos += (recursos.length() > 0 ? ",?" : "?");
//					psRecursos.adicionarLong(processoDt.getId_Recurso());
//				}
//				else {
//					processos += (processos.length() > 0 ? ",?" : "?");
//					psProcessos.adicionarLong(processoDt.getId());
//				}
//				//Chave dupla no map para permitir diferenciar processo com mais de um recurso
//				mapProcesso.put(processoDt.getId() + processoDt.getId_Recurso(), processoDt);

				listaAudienciaProcesso.add(audienciaProcessoDt);
			}
			//processoAnterior = rs1.getString("ID_PROC");
		}
		try{if(rs1 != null) rs1.close();} catch(Exception e) {}
		
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		
//		if (processos.length() > 0) {
//			//Busca as partes para cada processo encontrado anteriormente
//			String sql = " SELECT p.ID_PROC_PARTE, p.NOME, p.PROC_PARTE_TIPO_CODIGO, p.ID_PROC FROM PROJUDI.VIEW_PROC_PARTE p";
//			sql += " WHERE p.ID_PROC IN (" + processos + ") AND p.DATA_BAIXA IS NULL";
//			
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
		return listaAudienciaProcesso;
	}
	

//	/**
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
	 * Realiza o agendamento manual da Audiência 
	 * @param audienciaProcessoDt
	 * @return
	 * @throws Exception
	 * @author Márcio Gomes - 15/09/2010
	 */
	public boolean agendarAudienciaProcessoManual(AudienciaProcessoDt audienciaProcessoDt) throws Exception{
		return agendarAudienciaProcesso(audienciaProcessoDt, "", "", "", "");
	}	
	
	/**
	 * Realiza o agendamento automático da Audiência para a próxima audiência livre válida, não reservada, de acordo com o tipo da audiência, 
     * menos o tipo "Sessão de 2º Grau".
	 * @param audienciaTipoCodigo
	 * @param id_Processo
	 * @param id_Serventia
	 * @return
	 * @throws Exception
	 * @author Márcio Gomes - 15/09/2010
	 */
    public boolean agendarAudienciaProcessoAutomatico(String audienciaTipoCodigo, String id_Processo, String id_Serventia) throws Exception{
        return agendarAudienciaProcesso(null, audienciaTipoCodigo, id_Processo, id_Serventia, "");
    }    
    
    /**
     * Realiza o agendamento automático para a próxima audiência livre válida, não reservada, de acordo com o tipo da audiência, menos o tipo 
     * "Sessão de 2º Grau", e o cargo da serventia (ServentiaCargo) do usuário para o qual o processo foi distribuído
     * @param id_ServentiaCargo
     * @param audienciaTipoCodigo
     * @param id_Processo
     * @return
     * @throws Exception
     */
    public boolean agendarAudienciaProcessoAutomaticoServentiaCargo(String id_ServentiaCargo, String audienciaTipoCodigo, String id_Processo) throws Exception{
    	return agendarAudienciaProcesso(null, audienciaTipoCodigo, id_Processo, "", id_ServentiaCargo);
    }
	
    /**
     * Método sincronizado para garantir que uma determinada agenda só será utilizada uma única vez, independente se o agendamento
     * foi realizado de forma manual ou automática 
     * @param audienciaProcessoDt
     * @param audienciaTipoCodigo
     * @param id_Processo
     * @param id_Serventia
     * @param id_ServentiaCargo
     * @return
     * @throws Exception
     * @author Márcio Gomes - 15/09/2010
     */
	private boolean agendarAudienciaProcesso(AudienciaProcessoDt audienciaProcessoDt, String audienciaTipoCodigo, String id_Processo, String id_Serventia, String id_ServentiaCargo) throws Exception{		
		if (audienciaProcessoDt != null) {
			return alterarAudienciaProcessoAgendamento(audienciaProcessoDt);
		}else {
			agendarAudienciaProcessoAutomatico(audienciaTipoCodigo, id_Processo, id_Serventia, id_ServentiaCargo);
		}
		return true;
	}
	
	
	/**
	 * Realiza o agendamento automático para a próxima audiência livre válida, não reservada, de acordo com o tipo da audiência, menos o tipo 
     * "Sessão de 2º Grau", para uma serventia ou um cargo da serventia (ServentiaCargo) do usuário para o qual o processo foi distribuído
	 * @param audienciaTipoCodigo
	 * @param id_Processo
	 * @param id_Serventia
	 * @param id_ServentiaCargo
	 * @throws Exception
	 * @author Márcio Gomes - 15/09/2010
	 */
	private void agendarAudienciaProcessoAutomatico(String audienciaTipoCodigo, String id_Processo, String id_Serventia, String id_ServentiaCargo) throws Exception{
        String sql = "";
        PreparedStatementTJGO ps = new PreparedStatementTJGO();
                        
        String sNomeView = "VIEW_PROX_AUDI_LIVRE_AUTO";
		int prazo = 0;
		if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo()) {
			sNomeView = "VIEW_PROX_AUDI_LIVREAUTOCEJUSC";
			prazo = ProjudiPropriedades.getInstance().getQuantidadeDiasMarcarAudienciaAutomaticaCejusc();
		} else if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo()) {
			sNomeView = "VIEW_PROX_AUDI_LIVREAUTOCEJUSC";
			prazo = ProjudiPropriedades.getInstance().getQuantidadeDiasMarcarAudienciaDPVATAutomaticaCejusc();
		} else if (Funcoes.StringToInt(audienciaTipoCodigo) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo()) {
			sNomeView = "VIEW_PROX_AUDI_LIVREAUTOCEJUSC";
			prazo = ProjudiPropriedades.getInstance().getQuantidadeDiasMarcarMediacaoAutomaticaCejusc();
		} 		
        
    	//Método de update alterado para realizar apenas uma transação, a fim de evitar concorrência.
    	//Antes estava executando SELECT e UPDATE separados, o que fazia com que dois usuários pudessem 
    	//tentar alterar a mesma audiência ao mesmo tempo.
    	//@hmgodinho
    	sql = " UPDATE PROJUDI.AUDI_PROC ap SET ap.ID_PROC = ?";
        if(id_Processo != null) {
           ps.adicionarLong(id_Processo);
        } else {
           ps.adicionarLongNull();
        }
        sql += " , ap.ID_AUDI_PROC_STATUS = (SELECT ID_AUDI_PROC_STATUS FROM PROJUDI.AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?)";
    	ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
    	
    	// Ao marcar uma audiência, caso o processo tenha um recurso ativo, coloca o id_proc_tipo do recurso na tabela audi_proc. Se não for o caso,
    	// mantém na audi_proc o mesmo valor que já esteja neste campo. Feito para que este registro apareça na view_audi_completa.
    	sql += " , ap.ID_PROC_TIPO = NVL(";
    	sql += "							(SELECT R.ID_PROC_TIPO FROM PROC P ";
    	sql += " 							LEFT JOIN RECURSO R ON R.ID_PROC = P.ID_PROC AND P.ID_SERV = R.ID_SERV_RECURSO AND R.DATA_RETORNO is null";
    	sql += "							WHERE P.ID_PROC = ?) ";
    	sql += "							,ap.ID_PROC_TIPO ";
    	sql += "						)";
    	ps.adicionarLong(id_Processo);
    	sql += " WHERE ap.ID_AUDI_PROC = (";
    	
    	//Resgata próxima audiência livre e já dá update
    	sql += " SELECT ap.ID_AUDI_PROC FROM PROJUDI.AUDI_PROC ap";
    	sql += " INNER JOIN (SELECT ID_AUDI_PROC FROM PROJUDI." + sNomeView;
    	sql += " 			  WHERE AUDI_TIPO_CODIGO = ?";
        ps.adicionarLong(audienciaTipoCodigo);
        if (prazo > 0) {
        	sql += " AND DATA_AGENDADA >= (SYSDATE + ?) ";
        	ps.adicionarLong(prazo);
        }
        if (id_Serventia != null && id_Serventia.trim().length() > 0){
        	//independente do Cargo
        	sql += " 		  AND ID_SERV = ?";
        	ps.adicionarLong(id_Serventia);
        }
        else if (id_ServentiaCargo != null && id_ServentiaCargo.trim().length() > 0){
        	//sql o cargo
        	sql += " 		  AND ID_SERV_CARGO = ?";
        	ps.adicionarLong(id_ServentiaCargo);
        }
        sql += " 			  AND ROWNUM = ?) pa";
        ps.adicionarLong(1);
        sql += " 	ON ap.ID_AUDI_PROC = pa.ID_AUDI_PROC";        
        sql += " WHERE ap.ID_AUDI_PROC_STATUS = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.LIVRE);
		sql += " AND ap.ID_PROC IS NULL"; 
		sql +=")";
        	
        executarUpdateDelete(sql, ps);
		 
    }
	
	/**
	 * Método responsável em consultar todos os processos vinculados a uma audiência passada.
	 * Retorna uma lista de objetos "AudienciaProcessoDt".
	 * 
	 * @param id_Audiencia, identificação da audiência selecionada
	 * @param pendentes, booleano que define se devem ser retornados todos ou somente aqueles com status "A ser Realizada".
	 * @return List listaSessoes
	 * 
	 * @author mmgomes
	 * @throws Exception 
	 */
	public List consultarAudienciaProcessosAssistenteGabinete(String id_Audiencia, boolean pendentes, String id_ServentiaCargo) throws Exception {
		String sql = "";
		List listaAudienciaProcesso = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = obtenhaListaCamposViewAudiCompleta();
		sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA a";
		sql += " WHERE a.ID_AUDI = ?";
		ps.adicionarLong(id_Audiencia);
		if (pendentes) {
			sql += " AND a.AUDI_PROC_STATUS_CODIGO = ?";
			ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);			
		}		
		sql += " AND EXISTS (SELECT 1";
        sql += "              FROM SERV_CARGO sc ";        
        sql += "              INNER JOIN PROC_RESP pr ON sc.ID_SERV_CARGO = pr.ID_SERV_CARGO ";        
        sql += "               INNER JOIN PROC p ON pr.ID_PROC = p.ID_PROC ";
        sql += "               INNER JOIN CARGO_TIPO ct ON pr.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
        sql += "               WHERE sc.ID_SERV_CARGO = ? ";
        ps.adicionarLong(id_ServentiaCargo);        
        sql += "               AND p.ID_PROC = a.ID_PROC) ";
		sql += " ORDER BY a.SEGREDO_JUSTICA, a.ORDEM_2_GRAU, a.PROC_TIPO, a.ID_AUDI_PROC"; //,a.DATA_AUDI_ORIGINAL, a.DATA_AGENDADA, a.DATA_MOVI_AUDI_PROC";		
		try{
			rs1 = consultar(sql,ps);			
			listaAudienciaProcesso = this.getListaAudienciaProcessoCompleta(rs1);			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
		return listaAudienciaProcesso;
	}
	
	/**
	 * Método responsável em consultar o processo vinculado a uma audiência pendente.
	 * Retorna uma lista de objetos "AudienciaProcessoDt".
	 * 
	 * @param id_Processo, identificação do processo selecionado 
	 * @return AudienciaProcessoDt
	 * 
	 * @author mmgomes
	 * @throws Exception 
	 */
	public AudienciaProcessoDt consultarAudienciaProcessoPendenteAssistenteGabinete(String id_Processo, String id_ServentiaCargo) throws Exception {
		String sql = "";
		List listaAudienciaProcesso = new ArrayList();
		AudienciaProcessoDt audienciaProcessoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = obtenhaListaCamposViewAudiCompleta();
		sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA a";
		sql += " WHERE a.ID_PROC = ?";
		ps.adicionarLong(id_Processo);		
		sql += " AND a.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);				
		sql += " AND EXISTS (SELECT 1";
        sql += "              FROM SERV_CARGO sc ";        
        sql += "              INNER JOIN PROC_RESP pr ON sc.ID_SERV_CARGO = pr.ID_SERV_CARGO ";        
        sql += "               INNER JOIN PROC p ON pr.ID_PROC = p.ID_PROC ";
        sql += "               INNER JOIN CARGO_TIPO ct ON pr.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";
        sql += "               WHERE sc.ID_SERV_CARGO = ? ";
        ps.adicionarLong(id_ServentiaCargo);        
        sql += "               AND p.ID_PROC = a.ID_PROC) ";
		sql += " ORDER BY a.ORDEM_2_GRAU, a.PROC_TIPO, a.ID_AUDI_PROC, a.DATA_AUDI_ORIGINAL, a.DATA_AGENDADA, a.DATA_MOVI_AUDI_PROC";		
		try{
			rs1 = consultar(sql,ps);			
			listaAudienciaProcesso = this.getListaAudienciaProcessoCompleta(rs1);			
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
        if (listaAudienciaProcesso != null && listaAudienciaProcesso.size() > 0){
        	audienciaProcessoDt = (AudienciaProcessoDt) listaAudienciaProcesso.get(0);
        }
		return audienciaProcessoDt;
	}
	
	/**
	 * Método responsável em consultar todos os processos vinculados a uma audiência passada.
	 * Retorna uma lista de objetos "AudienciaProcessoDt".
	 * 
	 * @param id_Processo, identificação do processo selecionado	 * 
	 * @return AudienciaProcessoDt
	 * 
	 * @author mmgomes
	 * @throws Exception 
	 */
	public AudienciaProcessoDt consultarAudienciaProcessoPendente(String id_Processo) throws Exception {
		String sql = "";
		List listaAudienciaProcesso = new ArrayList();
		AudienciaProcessoDt audienciaProcessoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		sql = obtenhaListaCamposViewAudiCompleta();
		sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA a";
		sql += " WHERE a.ID_PROC = ?";
		ps.adicionarLong(id_Processo);	
		sql += " AND a.AUDI_PROC_STATUS_CODIGO = ?"; 		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);	
		sql += " ORDER BY a.ORDEM_2_GRAU, a.PROC_TIPO, a.ID_AUDI_PROC, a.DATA_AUDI_ORIGINAL, a.ID_AUDI_PROC_ORIGEM, a.DATA_AGENDADA, a.DATA_MOVI_AUDI_PROC";
		try{
			rs1 = consultar(sql,ps);
			//Chama método para devolver lista já com processos e partes setados
			listaAudienciaProcesso = this.getListaAudienciaProcessoCompleta(rs1);
			//rs1.close();
		
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
        }  
        if (listaAudienciaProcesso != null && listaAudienciaProcesso.size() > 0){
        	audienciaProcessoDt = (AudienciaProcessoDt) listaAudienciaProcesso.get(0);
        }
		return audienciaProcessoDt;
	}
	
	public void excluir(String[] id) throws Exception{

  		PreparedStatementTJGO ps = new PreparedStatementTJGO();
  		String stSql="";

  		stSql= "DELETE FROM PROJUDI.AUDI_PROC  ";
 	 	stSql += " WHERE ID_AUDI IN (?";	ps.adicionarLong(id[0]);; 	
 	 	
 	 	for(int i=1; i< id.length; i++) {
 	 		stSql += ", ?";					ps.adicionarLong(id[i]);; 	 		
 	 	}
 		stSql += ")";
 		executarUpdateDelete(stSql,ps);
  	
  	}
	
	/**
	 * Retorna os dados completos de uma AudienciaProcesso, já setando os dados da audiência vinculada
	 * 
	 * @param id_PendenciaVoto, identificação da pendência do tipo voto.
	 * @author mmgomes
	 */
	public AudienciaProcessoDt consultarCompletoPelaPendenciaDeVoto(String id_PendenciaVoto) throws Exception {
		AudienciaProcessoDt audienciaProcessoDt = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = obtenhaListaCamposViewAudiCompleta(); 
		sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA ac";
		sql += " 	WHERE ac.ID_PEND_VOTO = ? ";ps.adicionarLong(id_PendenciaVoto);
		sql += " 	OR ac.ID_PEND_VOTO_REDATOR = ? ";ps.adicionarLong(id_PendenciaVoto);
		sql += " 	ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC ";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);
			List audienciasProcesso = this.getListaAudienciaProcessoCompleta(rs1);
			if (audienciasProcesso != null && audienciasProcesso.size() > 0)
				audienciaProcessoDt = (AudienciaProcessoDt) audienciasProcesso.get(0);
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return audienciaProcessoDt;
	}
	
	/**
	 * Retorna os dados completos de uma AudienciaProcesso, já setando os dados da audiência vinculada
	 * 
	 * @param id_PendenciaVoto, identificação da pendência do tipo voto.
	 * @author mmgomes
	 */
	public AudienciaProcessoDt consultarCompletoPelaPendenciaDeEmenta(String id_PendenciaEmenta) throws Exception {
		AudienciaProcessoDt audienciaProcessoDt = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String sql = obtenhaListaCamposViewAudiCompleta(); 
		sql += " FROM PROJUDI.VIEW_AUDI_COMPLETA ac";
		sql += " 	WHERE ac.ID_PEND_EMENTA = ? ";ps.adicionarLong(id_PendenciaEmenta);
		sql += " 	OR ac.ID_PEND_EMENTA_REDATOR = ? ";ps.adicionarLong(id_PendenciaEmenta);
		sql += " 	ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC ";

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql,ps);
			List audienciasProcesso = this.getListaAudienciaProcessoCompleta(rs1);
			if (audienciasProcesso != null && audienciasProcesso.size() > 0)
				audienciaProcessoDt = (AudienciaProcessoDt) audienciasProcesso.get(0);
		
		}
		finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
		}
		return audienciaProcessoDt;
	}
	
	public void vincularPendenciaVotoEmenta(AudienciaProcessoDt audienciaProcessoDt) throws Exception{		
		String sql;
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
		String sVirgula = "";
		
		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		
		if (audienciaProcessoDt.possuiVotoRelator()) {
			sql += " ID_PEND_VOTO = ?";			
			ps2.adicionarLong(audienciaProcessoDt.getId_PendenciaVotoRelator());
			sVirgula = ", ";
		}
		
		if (audienciaProcessoDt.possuiEmentaRelator()) {
			sql += sVirgula + " ID_PEND_EMENTA = ?";			
			ps2.adicionarLong(audienciaProcessoDt.getId_PendenciaEmentaRelator());
			sVirgula = ", ";
		}
		
		if (audienciaProcessoDt.possuiVotoRedator()) {
			sql += sVirgula + " ID_PEND_VOTO_REDATOR = ?";			
			ps2.adicionarLong(audienciaProcessoDt.getId_PendenciaVotoRedator());
			sVirgula = ", ";
		}
		
		if (audienciaProcessoDt.possuiEmentaRedator()) {
			sql += sVirgula + " ID_PEND_EMENTA_REDATOR = ?";			
			ps2.adicionarLong(audienciaProcessoDt.getId_PendenciaEmentaRedator());
			sVirgula = ", ";
		}
		
		sql += " WHERE ID_AUDI_PROC = ?"; ps2.adicionarLong(audienciaProcessoDt.getId());			
							
		executarUpdateDelete(sql, ps2);
	}
	
	public void limpaVinculoPendenciaVoto(AudienciaProcessoDt audienciaProcessoDt) throws Exception{		
		String sql;
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
			
		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " ID_PEND_VOTO = NULL";
		sql += " WHERE ID_AUDI_PROC = ?"; ps2.adicionarLong(audienciaProcessoDt.getId());			
							
		executarUpdateDelete(sql, ps2);
	}
	
	public void limpaVinculoPendenciaEmenta(AudienciaProcessoDt audienciaProcessoDt) throws Exception{
		String sql;
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
			
		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " ID_PEND_EMENTA = NULL";
		sql += " WHERE ID_AUDI_PROC = ?"; ps2.adicionarLong(audienciaProcessoDt.getId());			
							
		executarUpdateDelete(sql, ps2);
	}
	
	public void atualizaIdProcessoTipoSessaoSegundoGrau(AudienciaProcessoDt audienciaProcessoDt) throws Exception{
		String sql;
		PreparedStatementTJGO ps2 = new PreparedStatementTJGO();
			
		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " ID_PROC_TIPO = ?"; ps2.adicionarLong(audienciaProcessoDt.getId_ProcessoTipo());
		sql += " WHERE ID_AUDI_PROC = ?"; ps2.adicionarLong(audienciaProcessoDt.getId());			
							
		executarUpdateDelete(sql, ps2);
	}
	
	public String getSqlEmVotacaoVirtual(PreparedStatementTJGO ps, String idServentiaCargo) throws Exception {
		String sql = " FROM PROJUDI.AUDI_PROC AP "
				+ " JOIN PROJUDI.PROC P ON P.ID_PROC = AP.ID_PROC"
				+ " JOIN PROJUDI.AUDI A ON AP.ID_AUDI = A.ID_AUDI"
				+ " INNER JOIN PROC_TIPO PT ON PT.ID_PROC_TIPO = AP.ID_PROC_TIPO ";
		sql += " WHERE AP.ID_SERV_CARGO = ?"; ps.adicionarLong(idServentiaCargo);
		sql += " AND AP.DATA_MOVI IS NULL ";
		sql += " AND A.SESSAO_INICIADA = 1 ";
		sql += " AND EXISTS (";
		sql += 		"SELECT 1 FROM PROJUDI.VIEW_PEND VP INNER JOIN AUDI_PROC_PEND APP ON APP.ID_PEND = VP.ID_PEND WHERE AP.ID_AUDI_PROC = APP.ID_AUDI_PROC";
		sql += 		" AND VP.PEND_TIPO_CODIGO IN (?, ?)";
		ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL);
		sql += ")";
		return sql;
	}
	
	// jvosantos - 06/01/2020 17:12 - Tipar lista
	public List<VotoSessaoLocalizarDt> consultarEmVotacao(String idServentiaCargo, String processoNumero) throws Exception {
		ResultSetTJGO rs1 = null;		
		List<VotoSessaoLocalizarDt> listaProcessos = new ArrayList<VotoSessaoLocalizarDt>();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql;
		
		sql = "SELECT P.ID_PROC, P.PROC_NUMERO, P.DIGITO_VERIFICADOR, A.DATA_AGENDADA, AP.ID_PROC, AP.ID_AUDI_PROC, PT.PROC_TIPO ";
		 sql += ", (SELECT DISTINCT PT1.PROC_TIPO FROM AUDI_PROC AP1 ";
	        sql += "	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC "; 
	        sql += "	INNER JOIN PROC_TIPO PT1 ON PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ";
	        sql += " 	WHERE AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1) PROC_TIPO_REC_SEC , "; 
	        sql += " 	(SELECT 1 FROM RECURSO R "; 
	        sql += "		INNER JOIN AUDI_PROC AP2 ON AP2.ID_PROC = R.ID_PROC "; 
	        sql += "		WHERE AP2.ID_AUDI_PROC = AP.ID_AUDI_PROC AND ROWNUM = 1)POSSUI_RECURSO ";
		sql += getSqlEmVotacaoVirtual(ps, idServentiaCargo);
		if(processoNumero != null && !processoNumero.isEmpty()) {
			sql += " AND (CONCAT(CONCAT(P.PROC_NUMERO, '.'), P.DIGITO_VERIFICADOR) = ?"; ps.adicionarString(processoNumero);
			sql += " OR CONCAT(CONCAT(P.PROC_NUMERO, '-'), P.DIGITO_VERIFICADOR) = ?)"; ps.adicionarString(processoNumero);
		}
		
		try{
			rs1 = consultar(sql, ps);
			while (rs1.next()) {
				listaProcessos.add(preencherVotoSessaoLocalizarDt(rs1));
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}

	public long consultarQuantidadeEmVotacaoVirtual(String idServentiaCargo) throws Exception {
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql;
		
		sql = "SELECT COUNT(*) AS QUANTIDADE ";
		sql += getSqlEmVotacaoVirtual(ps, idServentiaCargo);
		
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				return rs1.getLong("QUANTIDADE");
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return 0;
	}
	
	
	public void alterarStatusAudienciaTemp(String id_AudienciaProcesso, String novoStatus) throws Exception {
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " ID_AUDI_PROC_STATUS_TMP  = (SELECT ID_AUDI_PROC_STATUS FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?)";
		ps.adicionarLong(novoStatus);
		sql = sql + " WHERE ID_AUDI_PROC  = ?";
		if (id_AudienciaProcesso != null) {
			ps.adicionarLong(id_AudienciaProcesso);
		} else {
			ps.adicionarLongNull();
		}
		
		executarUpdateDelete(sql, ps);
		
	}

	public AudienciaProcessoStatusDt consultarStatusAudienciaTemp(String idAudienciaProcesso) throws Exception {
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql;
		
		// jvosantos - 04/06/2019 10:29 - Adiciona o ID do Status
		sql = "SELECT C.ID_AUDI_PROC_STATUS, C.AUDI_PROC_STATUS_CODIGO, C.AUDI_PROC_STATUS FROM AUDI_PROC_STATUS C ";
		sql += "WHERE EXISTS (SELECT ID_AUDI_PROC FROM AUDI_PROC AP "
				+ "WHERE AP.ID_AUDI_PROC_STATUS_TMP = C.ID_AUDI_PROC_STATUS";
		sql += " AND AP.ID_AUDI_PROC = ?)"; ps.adicionarLong(idAudienciaProcesso);
		
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				AudienciaProcessoStatusDt audienciaProcessoStatusDt = new AudienciaProcessoStatusDt();
				audienciaProcessoStatusDt.setAudienciaProcessoStatusCodigo(rs1.getString("AUDI_PROC_STATUS_CODIGO"));
				audienciaProcessoStatusDt.setAudienciaProcessoStatus(rs1.getString("AUDI_PROC_STATUS"));
				audienciaProcessoStatusDt.setId(rs1.getString("ID_AUDI_PROC_STATUS"));
				return audienciaProcessoStatusDt;
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return null;
	}
	
	public void alterarPedidoSustentacaoOral(String id_AudienciaProcesso, boolean pedidoSustentacaoOral) throws Exception {
		
		String sql;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.AUDI_PROC SET";
		sql += " PEDIDO_SUSTENTACAO_ORAL  = ?";
		ps.adicionarBoolean(pedidoSustentacaoOral);
		sql = sql + " WHERE ID_AUDI_PROC  = ?";
		ps.adicionarLong(id_AudienciaProcesso);		
		
		executarUpdateDelete(sql, ps);
		
	}
	
	public boolean consultarPodeSustentacaoOral(String idAudienciaProcesso) throws Exception {
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql;
		
		sql = "SELECT PEDIDO_SUSTENTACAO_ORAL FROM AUDI_PROC WHERE ID_AUDI_PROC = ?";
		ps.adicionarLong(idAudienciaProcesso);
		
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				return rs1.getBoolean("PEDIDO_SUSTENTACAO_ORAL");
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return false;
	}
	
	public boolean verificarVotacaoIniciada(String idAudienciaProcesso) throws Exception {
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT COUNT(*) AS COUNT ");
		sql.append(" FROM AUDI_PROC AP ");
		sql.append(" WHERE ");
		sql.append(" AP.ID_AUDI_PROC = ? "); ps.adicionarLong(idAudienciaProcesso);
		sql.append(" AND AP.ID_PEND_EMENTA IS NOT NULL ");
		sql.append(" AND ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		COUNT(*) AS C ");
		sql.append(" 	FROM ");
		sql.append(" 		PEND P");
		sql.append("	INNER JOIN AUDI_PROC_PEND APP ON APP.ID_PEND = P.ID_PEND"); //lrcampos 13/02/2020 - Incluindo vinculo com a audiProcPend
		sql.append(" 	WHERE ");
		sql.append(" 		APP.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 		AND P.ID_PEND_TIPO = ( ");
		sql.append(" 			SELECT PT.ID_PEND_TIPO ");
		sql.append(" 		FROM ");
		sql.append(" 			PEND_TIPO PT");
		sql.append(" 		WHERE ");
		sql.append(" 			PT.PEND_TIPO_CODIGO = ?) ) <> 0 "); ps.adicionarLong(PendenciaTipoDt.VOTO_SESSAO);
		
		try{
			rs1 = consultar(sql.toString(), ps);
			if (rs1.next()) {
				return rs1.getLong("COUNT") != 0;
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return false;
	}
	
	// jvosantos - 04/06/2019 10:31 - Método que alterar as pendencias de voto e ementa de uma audi_proc
	public boolean alterarPendenciaVotoEmenta(String id, String idVoto, String idEmenta) throws Exception {
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		if(idVoto == null && idEmenta == null) return true;
		
		sql.append(" UPDATE AUDI_PROC SET ");
		if(idVoto != null) { sql.append(" ID_PEND_VOTO = ? "); ps.adicionarLong(idVoto); };
		if(idEmenta != null) { if(idVoto != null) sql.append(", "); sql.append(" ID_PEND_EMENTA = ? "); ps.adicionarLong(idEmenta); };
		sql.append(" WHERE ID_AUDI_PROC = ? "); ps.adicionarLong(id);
	
		try{
			rs1 = consultar(sql.toString(), ps);
			if (rs1.next()) {
				return true;
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return false;
	}

	// jvosantos - 26/09/2019 14:06 - Adicionar pendência de VERIFICAR_IMPEDIMENTO_VOTANTES para Serventias especiais
	// jvosantos - 22/08/2019 17:15 - Corrigir para usar a tabela de ligação AUDI_PROC_PEND
	// jvosantos - 04/06/2019 13:57 - Verifica se existe pendencia de apreciados ou proclamação
	public boolean verificarApreciadosOuProclamacao(String idAudienciaProcesso) throws Exception {
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		sql.append(" 	SELECT ");
		sql.append(" 	COUNT(*) AS COUNT ");
		sql.append(" 	FROM ");
		sql.append(" 	AUDI_PROC_PEND APP ");
		sql.append(" INNER JOIN PEND P ON ");
		sql.append(" 	APP.ID_PEND = P.ID_PEND ");
		sql.append(" 	WHERE ");
		sql.append(" 	APP.ID_AUDI_PROC = ? ");
		sql.append(" 	AND P.ID_PEND_TIPO IN ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		ID_PEND_TIPO ");
		sql.append(" 		FROM ");
		sql.append(" 			PEND_TIPO ");
		sql.append(" 		WHERE ");
		sql.append(" 		PEND_TIPO_CODIGO IN (?, ?, ?)) ");
		ps.adicionarLong(idAudienciaProcesso);
		ps.adicionarLong(PendenciaTipoDt.APRECIADOS);
		ps.adicionarLong(PendenciaTipoDt.PROCLAMACAO_VOTO);
		ps.adicionarLong(PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_VOTANTES);
		
		try{
			rs1 = consultar(sql.toString(), ps);
			if (rs1.next()) {
				return rs1.getLong("COUNT") != 0;
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return false;
	}
	
	public void cadastrarVotantesSessaoVirtual(List<AudienciaProcessoVotantesDt> listaAudiProcVotantes) throws Exception {		
		// jvosantos - 25/09/2019 13:22 - Extrair método para reaproveitar código
		for (AudienciaProcessoVotantesDt dados : listaAudiProcVotantes){
			cadastrarVotanteSessaoVirtual(dados);
		}
		
	}

	// jvosantos - 25/09/2019 13:22 - Extrair método para reaproveitar código
	public void cadastrarVotanteSessaoVirtual(AudienciaProcessoVotantesDt dados) throws Exception {
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
		ps.adicionarLong(new ImpedimentoTipoNe().consultarImpedimentoTipoCodigo(dados.getImpedimentoTipoCodigo()).getId());
		
		SqlCampos += ",ORDEM_VOTANTE";
		SqlValores+=",?";
		ps.adicionarLong(dados.getOrdemVotante());
		
		SqlCampos += ",ID_VOTANTE_TIPO";
		SqlValores+=",?";
		ps.adicionarLong(new VotanteTipoNe().consultarVotanteTipoCodigo(dados.getVotanteTipoCodigo()).getId());
		
		SqlCampos+=")";
		SqlValores+=")";
		
		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	

		dados.setId(executarInsert(Sql, "ID_AUDI_PROC_VOTANTES", ps));
	}
	
	public void excluirVotantesSessaoVirtual(String id_AudienciaProcesso) throws Exception {
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
  		String stSql="";

  		stSql= "DELETE FROM PROJUDI.AUDI_PROC_VOTANTES";
 	 	stSql += " WHERE ID_AUDI_PROC = ?";
 	 	ps.adicionarLong(Long.valueOf(id_AudienciaProcesso));
 	 	
 		executarUpdateDelete(stSql,ps);
	}

	public String consultarAudienciaProcessoDoProcesso(String idProcesso) throws Exception {
		return consultarAudienciaProcessoDoProcesso(idProcesso, AudienciaProcessoStatusDt.A_SER_REALIZADA);
	}

	public String consultarAudienciaProcessoDoProcesso(String idProcesso, int audienciaProcessoStatus) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append(" 	ID_AUDI_PROC "); 
		sql.append(" FROM ");
		sql.append(" 	AUDI_PROC "); 
		sql.append(" WHERE ");
		sql.append(" 	ID_PROC = ? ");	ps.adicionarLong(idProcesso);
		sql.append(" 	AND ID_AUDI_PROC_STATUS = ( ");
		sql.append(" 		SELECT ID_AUDI_PROC_STATUS "); 
		sql.append(" 	FROM ");
		sql.append(" 		AUDI_PROC_STATUS "); 
		sql.append(" 	WHERE ");
		sql.append(" 		AUDI_PROC_STATUS_CODIGO = ?) ");	ps.adicionarLong(audienciaProcessoStatus);

		try{
			rs1 = consultar(sql.toString(), ps);
			if (rs1.next()) {
				return rs1.getString("ID_AUDI_PROC");
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		return null;
	}
	
	// jvosantos - 13/06/2019 15:54 - Adicionar método para buscar os IDS_SERV_CARGO dos votantes e relator da sessão
	public List<String> consultarIdsVotantesAudienciaProcesso(String id_AudienciaProcesso, FabricaConexao obFabricaConexao) throws Exception {
		List<String> ids = new ArrayList<String>();
 		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder str = new StringBuilder();
		
		str.append(" SELECT ");
		str.append("	ID_SERV_CARGO AS ID ");
		str.append(" FROM ");
		str.append("	AUDI_PROC_VOTANTES ");
		str.append(" WHERE ");
		str.append("	ID_AUDI_PROC = ? "); ps.adicionarLong(id_AudienciaProcesso);
		str.append("	AND ID_VOTANTE_TIPO IN ( ");
		str.append("		SELECT ID_VOTANTE_TIPO ");
		str.append("	FROM ");
		str.append("		VOTANTE_TIPO ");
		str.append("	WHERE ");
		str.append("		VOTANTE_TIPO_CODIGO IN (?, "); ps.adicionarLong(VotanteTipoDt.RELATOR);
		str.append("		?)) "); ps.adicionarLong(VotanteTipoDt.VOTANTE);
		
		try{
			rs1 = consultar(str.toString(), ps);
			while (rs1.next()) {
				ids.add(rs1.getString("ID"));
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		return ids;
	}
	
	private String getSQLAcompanhaVotacaoErroMaterial(String idServentiaCargo, PreparedStatementTJGO ps) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("     P.ID_PROC, ");
		sql.append("     P.PROC_NUMERO, ");
		sql.append("     P.DIGITO_VERIFICADOR, ");
		sql.append("     A.DATA_AGENDADA, ");
		sql.append("     AP.ID_PROC, ");
		sql.append("     AP.ID_AUDI_PROC, ");
		sql.append("     PROCT.PROC_TIPO , ");
		sql.append("     ( ");
		sql.append("     SELECT ");
		sql.append("         DISTINCT PT1.PROC_TIPO ");
		sql.append("     FROM ");
		sql.append("         AUDI_PROC AP1 ");
		sql.append("     INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON ");
		sql.append("         RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC ");
		sql.append("     INNER JOIN PROC_TIPO PT1 ON ");
		sql.append("         PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ");
		sql.append("     WHERE ");
		sql.append("         AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append("         AND ROWNUM = 1) PROC_TIPO_REC_SEC , ");
		sql.append("     ( ");
		sql.append("     SELECT ");
		sql.append("         1 ");
		sql.append("     FROM ");
		sql.append("         RECURSO R ");
		sql.append("     INNER JOIN AUDI_PROC AP1 ON ");
		sql.append("         AP1.ID_PROC = R.ID_PROC ");
		sql.append("     WHERE ");
		sql.append("         AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append("         AND ROWNUM = 1 ");
		sql.append("         ) POSSUI_RECURSO ");
		sql.append(" FROM ");
		sql.append("     AUDI_PROC AP ");
		sql.append(" LEFT JOIN PROC P ON ");
		sql.append("     AP.ID_PROC = P.ID_PROC ");
		sql.append(" LEFT JOIN AUDI A ON ");
		sql.append("     A.ID_AUDI = AP.ID_AUDI ");
		sql.append(" INNER JOIN PROC_TIPO PROCT ON ");
		sql.append("     PROCT.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" INNER JOIN VIEW_PEND_VOTO_VIRTUAL VPVV ON ");
		sql.append("     VPVV.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" INNER JOIN PEND_FINAL_RESP PFR ON ");
		sql.append("     PFR.ID_PEND = VPVV.ID_PEND ");
		sql.append(" INNER JOIN AUDI_PROC_PEND APPEM ON ");
		sql.append("     APPEM.ID_AUDI_PROC = VPVV.ID_AUDI_PROC ");
		sql.append(" INNER JOIN PEND PEM ON ");
		sql.append("     PEM.ID_PEND = APPEM.ID_PEND ");
		sql.append(" INNER JOIN PEND_TIPO PTEM ON ");
		sql.append("     PTEM.ID_PEND_TIPO = PEM.ID_PEND_TIPO ");
		sql.append(" WHERE ");
		sql.append("     PTEM.PEND_TIPO_CODIGO = ? "); ps.adicionarLong(PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL);
		sql.append(" AND ");
		sql.append("     PFR.ID_SERV_CARGO = ? "); ps.adicionarLong(idServentiaCargo);
		sql.append(" AND ");
		sql.append("     VPVV.VOTO_ATIVO <> 0 ");
		sql.append(" AND ");
		sql.append("     ID_AUDI_PROC_STATUS = ? "); ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		return sql.toString();
	}

	private String getSQLAcompanhaVotacao(String idServentiaCargo, PreparedStatementTJGO ps) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT ");
		sql.append(" 	DISTINCT P.ID_PROC, ");
		sql.append(" 	P.PROC_NUMERO, ");
		sql.append(" 	P.DIGITO_VERIFICADOR, ");
		sql.append(" 	A.DATA_AGENDADA, ");
		sql.append(" 	AP.ID_AUDI_PROC, ");
		sql.append(" 	PROCT.PROC_TIPO , ");
		sql.append(" 	P.PROC_NUMERO, ");
		sql.append(" 	( ");
		sql.append(" 	SELECT ");
		sql.append(" 		DISTINCT PT1.PROC_TIPO ");
		sql.append(" 	FROM ");
		sql.append(" 		AUDI_PROC AP1 ");
		sql.append(" 	INNER JOIN RECURSO_SECUNDARIO_PARTE RSP ON ");
		sql.append(" 		RSP.ID_AUDI_PROC = AP1.ID_AUDI_PROC ");
		sql.append(" 	INNER JOIN PROC_TIPO PT1 ON ");
		sql.append(" 		PT1.ID_PROC_TIPO = RSP.ID_PROC_TIPO_RECURSO_SEC ");
		sql.append(" 	WHERE ");
		sql.append(" 		AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 		AND ROWNUM = 1 ) PROC_TIPO_REC_SEC, ");
		sql.append(" 	( ");
		sql.append(" 	SELECT ");
		sql.append(" 		1 ");
		sql.append(" 	FROM ");
		sql.append(" 		RECURSO R ");
		sql.append(" 	INNER JOIN AUDI_PROC AP1 ON ");
		sql.append(" 		AP1.ID_PROC = R.ID_PROC ");
		sql.append(" 	WHERE ");
		sql.append(" 		AP1.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 		AND ROWNUM = 1) POSSUI_RECURSO ");
		sql.append(" FROM ");
		sql.append(" 	AUDI_PROC AP ");
		sql.append(" INNER JOIN PROC P ON ");
		sql.append(" 	AP.ID_PROC = P.ID_PROC ");
		sql.append(" INNER JOIN AUDI A ON ");
		sql.append(" 	A.ID_AUDI = AP.ID_AUDI ");
		sql.append(" INNER JOIN PROC_TIPO PROCT ON ");
		sql.append(" 	PROCT.ID_PROC_TIPO = AP.ID_PROC_TIPO ");
		sql.append(" INNER JOIN ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		COUNT(APV.ID_AUDI_PROC) AS PVV_QTD_VOTO, AP.ID_AUDI_PROC AS PVV_ID_AUDI_PROC ");
		sql.append(" 	FROM ");
		sql.append(" 		AUDI_PROC AP ");
		sql.append(" 	LEFT JOIN PEND_VOTO_VIRTUAL PVV ON ");
		sql.append(" 		AP.ID_AUDI_PROC = PVV.ID_AUDI_PROC ");
		sql.append(" 	LEFT JOIN PEND_FINAL_RESP PR ON ");
		sql.append(" 		PVV.ID_PEND = PR.ID_PEND ");
		sql.append(" 	LEFT JOIN AUDI_PROC_VOTANTES APV ON ");
		sql.append(" 		PR.ID_SERV_CARGO = APV.ID_SERV_CARGO ");
		sql.append(" 		AND APV.ID_AUDI_PROC = PVV.ID_AUDI_PROC ");
		sql.append(" 	WHERE ");
		sql.append(" 		APV.ID_VOTANTE_TIPO = ( ");
		sql.append(" 		SELECT ");
		sql.append(" 			ID_VOTANTE_TIPO ");
		sql.append(" 		FROM ");
		sql.append(" 			VOTANTE_TIPO ");
		sql.append(" 		WHERE ");
		sql.append(" 			VOTANTE_TIPO_CODIGO = ?) "); ps.adicionarLong(VotanteTipoDt.VOTANTE);
		sql.append(" 		AND VOTO_ATIVO <> 0 ");
		sql.append(" 		OR APV.ID_AUDI_PROC_VOTANTES IS NULL ");
		sql.append(" 	GROUP BY ");
		sql.append(" 		AP.ID_AUDI_PROC ) ON ");
		sql.append(" 	AP.ID_AUDI_PROC = PVV_ID_AUDI_PROC ");
		sql.append(" INNER JOIN ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		COUNT(*) APV_QTD_VOTO, APV.ID_AUDI_PROC AS APV_ID_AUDI_PROC ");
		sql.append(" 	FROM ");
		sql.append(" 		AUDI_PROC_VOTANTES APV ");
		sql.append(" 	INNER JOIN AUDI_PROC AP ON ");
		sql.append(" 		APV.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append(" 	WHERE ");
		sql.append(" 		ID_VOTANTE_TIPO = ( ");
		sql.append(" 		SELECT ");
		sql.append(" 			ID_VOTANTE_TIPO ");
		sql.append(" 		FROM ");
		sql.append(" 			VOTANTE_TIPO ");
		sql.append(" 		WHERE ");
		sql.append(" 			VOTANTE_TIPO_CODIGO = ?) ");ps.adicionarLong(VotanteTipoDt.VOTANTE);
		sql.append(" 	GROUP BY ");
		sql.append(" 		APV.ID_AUDI_PROC ) ON ");
		sql.append(" 	AP.ID_AUDI_PROC = APV_ID_AUDI_PROC ");
		sql.append(" WHERE ");
		sql.append(" 	ID_AUDI_PROC_STATUS = ? "); ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		sql.append(" 	AND AP.ID_AUDI_PROC IN ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		ID_AUDI_PROC ");
		sql.append(" 	FROM ");
		sql.append(" 		PROJUDI.VIEW_PEND_VOTO_VIRTUAL ");
		sql.append(" 	WHERE ");
		sql.append(" 		ID_PEND IN ( ");
		sql.append(" 		SELECT ");
		sql.append(" 			ID_PEND ");
		sql.append(" 		FROM ");
		sql.append(" 			PEND_FINAL_RESP ");
		sql.append(" 		WHERE ");
		sql.append(" 			ID_SERV_CARGO = ?) "); ps.adicionarLong(idServentiaCargo);
		sql.append(" 		AND VOTO_ATIVO <> 0) ");
		sql.append(" 	AND PVV_QTD_VOTO < APV_QTD_VOTO ");
		return sql.toString();
	}
		
	public long consultarQuantidadeAcompanharVotacaoSessaoVirtual(String idServentiaCargo) throws Exception {
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT COUNT(*) AS QUANTIDADE FROM ( ");
		sql.append(getSQLAcompanhaVotacao(idServentiaCargo, ps));
		sql.append(" ) ");
		
		try{
			rs1 = consultar(sql.toString(), ps);
			if (rs1.next()) {
				return rs1.getLong("QUANTIDADE");
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return 0;
	}
	
	public long consultarQuantidadeAcompanharVotacaoSessaoVirtualErroMaterial(String idServentiaCargo) throws Exception {
			ResultSetTJGO rs1 = null;		
			PreparedStatementTJGO ps =  new PreparedStatementTJGO();
			StringBuilder sql = new StringBuilder();
			
			sql.append(" SELECT COUNT(*) AS QUANTIDADE FROM ( ");
			sql.append(getSQLAcompanhaVotacaoErroMaterial(idServentiaCargo, ps));
			sql.append(" ) ");
			
			try{
				rs1 = consultar(sql.toString(), ps);
				if (rs1.next()) {
					return rs1.getLong("QUANTIDADE");
				}
			} finally{
				try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			}
			return 0;
	}	

	public List<VotoSessaoLocalizarDt> consultarAcompanharVotacao(String idServentiaCargo, String processoNumero) throws Exception {
		ResultSetTJGO rs1 = null;		
		List<VotoSessaoLocalizarDt> listaProcessos = new ArrayList<VotoSessaoLocalizarDt>();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		sql.append(getSQLAcompanhaVotacao(idServentiaCargo, ps));

		filtroProcessoAcompanharVotacao(processoNumero, sql, ps);
		
		try{
			rs1 = consultar(sql.toString(), ps);
			while (rs1.next()) {
				listaProcessos.add(preencherVotoSessaoLocalizarDt(rs1));
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}

	protected void filtroProcessoAcompanharVotacao(String processoNumero, StringBuilder sql, PreparedStatementTJGO ps)
			throws Exception {
		// mrbatista - 06/12/2019 11:52 - Corrigir
		if(processoNumero != null && !processoNumero.isEmpty() && processoNumero.matches("\\d*[-\\.]\\d*")) {
			String[] procNum = processoNumero.split("[-\\.]");
			sql.append(" AND P.PROC_NUMERO = ?"); ps.adicionarString(procNum[0]);
			sql.append(" AND P.DIGITO_VERIFICADOR = ?"); ps.adicionarString(procNum[1]);
		}
	}
	
	public List<VotoSessaoLocalizarDt> consultarAcompanharVotacaoErroMaterial(String idServentiaCargo, String processoNumero) throws Exception {
		ResultSetTJGO rs1 = null;		
		List<VotoSessaoLocalizarDt> listaProcessos = new ArrayList<VotoSessaoLocalizarDt>();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		sql.append(getSQLAcompanhaVotacaoErroMaterial(idServentiaCargo, ps));

		filtroProcessoAcompanharVotacao(processoNumero, sql, ps);
		
		try{
			rs1 = consultar(sql.toString(), ps);
			while (rs1.next()) {
				listaProcessos.add(preencherVotoSessaoLocalizarDt(rs1));
			}
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}

	protected VotoSessaoLocalizarDt preencherVotoSessaoLocalizarDt(ResultSetTJGO rs1) throws Exception {
		VotoSessaoLocalizarDt voto = new VotoSessaoLocalizarDt();
		ProcessoDt processoDt = new ProcessoDt();
		voto.setIdAudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
		voto.setIdProcesso(rs1.getString("ID_PROC"));
		voto.setProcessoNumero(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
		voto.setDataSessao(Funcoes.FormatarDataHora(rs1.getString("DATA_AGENDADA")));
		
		//lrcampos 27/01/2020 13:09 - Incluindo classe do Recurso secundario em caso da sessão possui recurso secundario. 
		if (rs1.getString("PROC_TIPO_REC_SEC") == null) {
			processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
		} else if (rs1.getString("POSSUI_RECURSO") == null) {
			processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC"));
		} else {
			processoDt.setProcessoTipo(rs1.getString("PROC_TIPO_REC_SEC") + " - " + rs1.getString("PROC_TIPO"));
		}
		voto.setProcessoDt(processoDt);
		return voto;
	}
	
	//lrcampos 10/07/2019 * altera a serventia do presidente da sessão e represetante mp
	public void alterarPresidenteMPAudiencia(AudienciaProcessoDt audienciaProcessoNovaDt) throws Exception {
		
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String sql = "";
		
		sql = " UPDATE PROJUDI.AUDI_PROC SET";
		sql += " ID_SERV_CARGO_PRESIDENTE = ?"; ps.adicionarLong(audienciaProcessoNovaDt.getId_ServentiaCargoPresidente());
		sql += ", ID_SERV_CARGO_MP = ?";  ps.adicionarLong(audienciaProcessoNovaDt.getId_ServentiaCargoMP());
		sql += " WHERE ID_AUDI_PROC = ? "; ps.adicionarLong(audienciaProcessoNovaDt.getId());
		
		executarUpdateDelete(sql, ps);
		
	}
	
	// jvosantos - 03/09/2019 16:32 - Implementar método que retorna a quantidade de Voto/Ementa aguardando assinatura
	public long consultarQuantidadeVotoEmentaAguardandoAssinaturaSessaoVirtual(String idServentiaCargo) throws Exception {
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT COUNT(*) AS QUANTIDADE FROM ( ");
		sql.append(getSQLVotoEmentaAguardandoAssinatura(idServentiaCargo, ps)); 
		sql.append(" ) ");
		
		try{
			rs1 = consultar(sql.toString(), ps);
			if (rs1.next()) {
				return rs1.getLong("QUANTIDADE");
			}
		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return 0;
	}
	
	// jvosantos - 03/09/2019 16:32 - Implementar método que retorna a Query de consulta do Voto/Ementa Aguardando assinatura
	private String getSQLVotoEmentaAguardandoAssinatura(String idServentiaCargo, PreparedStatementTJGO ps) throws Exception {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append(" 	P.* ");
		sql.append(" FROM ");
		sql.append(" 	AUDI_PROC_PEND APP ");
		sql.append(" JOIN PEND P ON ");
		sql.append(" 	APP.ID_PEND = P.ID_PEND ");
		sql.append(" JOIN PEND_RESP PR ON PR.ID_PEND = P.ID_PEND " );
		sql.append(" WHERE ");
		sql.append(" 	P.ID_PEND_TIPO IN ( ");
		sql.append(" 		SELECT ID_PEND_TIPO ");
		sql.append(" 	FROM ");
		sql.append(" 		PEND_TIPO ");
		sql.append(" 	WHERE ");
		sql.append(" 		PEND_TIPO_CODIGO = ?) "); ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		sql.append(" 	AND P.ID_PEND_STATUS = (SELECT ID_PEND_STATUS FROM PEND_STATUS WHERE PEND_STATUS_CODIGO = ?) "); ps.adicionarLong(PendenciaStatusDt.ID_CUMPRIDA_PARCIALMENTE);
		sql.append(" AND PR.ID_SERV_CARGO = ? "); ps.adicionarLong(idServentiaCargo);
		
		return sql.toString();
	}

	public void alterarStatusAnalista(String idAudienciaProcesso, int novoStatusCodigo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		String sql = "UPDATE AUDI_PROC SET ID_AUDI_PROC_STATUS_ANA = (SELECT ID_AUDI_PROC_STATUS FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?) WHERE ID_AUDI_PROC = ?";
		
		ps.adicionarLong(novoStatusCodigo);
		ps.adicionarLong(idAudienciaProcesso);
		
		this.executarUpdateDelete(sql,  ps);
	}

	public void alterarStatusTemp(String idAudienciaProcesso, int novoStatusCodigo) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		String sql = "UPDATE AUDI_PROC SET ID_AUDI_PROC_STATUS_TEMP = (SELECT ID_AUDI_PROC_STATUS FROM AUDI_PROC_STATUS WHERE AUDI_PROC_STATUS_CODIGO = ?) WHERE ID_AUDI_PROC = ?";
		
		ps.adicionarLong(novoStatusCodigo);
		ps.adicionarLong(idAudienciaProcesso);
		
		this.executarUpdateDelete(sql,  ps);
	}
	
	public List<AudienciaProcessoDt> listarAudienciaProcessoPendente(String idProcesso) throws Exception {
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List<AudienciaProcessoDt> listaAudienciaProcesso = new ArrayList<AudienciaProcessoDt>();
		AudienciaProcessoDt audienciaProcessoDt = null; 
		
		String sql = "SELECT * ";
		
		sql += " FROM PROJUDI.VIEW_AUDI_PROC_COMPLETA ap";
		sql += " WHERE ap.ID_PROC =  ?";
		ps.adicionarLong(idProcesso); 
		sql += " AND ap.DATA_MOVI_AUDI_PROC IS NULL ";
		sql += " AND ap.AUDI_PROC_STATUS_CODIGO = ?";
		ps.adicionarLong(AudienciaProcessoStatusDt.A_SER_REALIZADA);
		
		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				audienciaProcessoDt = new AudienciaProcessoDt();
				audienciaProcessoDt.setId(rs1.getString("ID_AUDI_PROC"));
				listaAudienciaProcesso.add(audienciaProcessoDt);
			}
		} finally {
			if (rs1 != null) rs1.close();
		}
		
		return listaAudienciaProcesso;
	}

	// alsqueiroz - 18/09/2019 09:25 - Método para consultar os votantes da audiência processo
	public List<AudienciaProcessoVotantesDt> consultarVotantesAudienciaProcesso(String idAudienciaProcesso, Boolean soVotante) throws Exception {
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		AudienciaProcessoVotantesDt audienciaProcessoVotantesDt;
		List<AudienciaProcessoVotantesDt> listaVotantes = new ArrayList<AudienciaProcessoVotantesDt>();

		sql.append("SELECT ");
		sql.append("APP.ID_AUDI_PROC_VOTANTES, ");
		sql.append("APP.ID_AUDI_PROC, ");
		sql.append("APP.ID_SERV_CARGO, ");
		sql.append("(SELECT U2.NOME FROM USU U2 WHERE U2.ID_USU = (SELECT US2.ID_USU FROM USU_SERV US2 WHERE US2.ID_USU_SERV = (SELECT USG2.ID_USU_SERV FROM USU_SERV_GRUPO USG2 WHERE USG2.ID_USU_SERV_GRUPO = (SELECT SC2.ID_USU_SERV_GRUPO FROM SERV_CARGO SC2 WHERE SC2.ID_SERV_CARGO = APP.ID_SERV_CARGO)))) AS NOME_VOTANTE, ");
		sql.append("APP.RELATOR, ");
		sql.append("APP.ID_IMPEDIMENTO_TIPO, ");
		sql.append("IT.IMPEDIMENTO_TIPO_CODIGO, ");
		sql.append("IT.IMPEDIMENTO_TIPO, ");
		sql.append("APP.ORDEM_VOTANTE, ");
		sql.append("APP.ID_VOTANTE_TIPO, ");
		sql.append("VT.VOTANTE_TIPO_CODIGO, ");
		sql.append("VT.VOTANTE_TIPO, ");
		sql.append("APP.CONVOCADO ");
		sql.append("FROM AUDI_PROC_VOTANTES APP ");
		sql.append("INNER JOIN AUDI_PROC AP ");
		sql.append("ON APP.ID_AUDI_PROC = AP.ID_AUDI_PROC ");
		sql.append("INNER JOIN SERV_CARGO SC ");
		sql.append("ON APP.ID_SERV_CARGO = SC.ID_SERV_CARGO ");
		sql.append("INNER JOIN SERV S ");
		sql.append("ON SC.ID_SERV = S.ID_SERV ");
		sql.append("INNER JOIN IMPEDIMENTO_TIPO IT ");
		sql.append("ON APP.ID_IMPEDIMENTO_TIPO = IT.ID_IMPEDIMENTO_TIPO ");
		sql.append("INNER JOIN VOTANTE_TIPO VT ");
		sql.append("ON APP.ID_VOTANTE_TIPO = VT.ID_VOTANTE_TIPO ");
		sql.append("WHERE APP.ID_AUDI_PROC = ? ");
		ps.adicionarString(idAudienciaProcesso);
		if (soVotante) {
			sql.append("AND VT.VOTANTE_TIPO_CODIGO = ? ");
			ps.adicionarLong(VotanteTipoDt.VOTANTE);
		}
		sql.append("ORDER BY APP.ORDEM_VOTANTE ");

		try {
			rs1 = consultar(sql.toString(), ps);

			while (rs1.next()) {
				audienciaProcessoVotantesDt = new AudienciaProcessoVotantesDt();

				audienciaProcessoVotantesDt.setId(rs1.getString("ID_AUDI_PROC_VOTANTES"));
				audienciaProcessoVotantesDt.setId_AudienciaProcesso(rs1.getString("ID_AUDI_PROC"));
				audienciaProcessoVotantesDt.setId_ServentiaCargo(rs1.getString("ID_SERV_CARGO"));
				audienciaProcessoVotantesDt.setNomeUsuario(rs1.getString("NOME_VOTANTE"));
				audienciaProcessoVotantesDt.setRelator(rs1.getBoolean("RELATOR"));
				audienciaProcessoVotantesDt.setId_ImpedimentoTipo(rs1.getString("ID_IMPEDIMENTO_TIPO"));
				audienciaProcessoVotantesDt.setImpedimentoTipoCodigo(rs1.getString("IMPEDIMENTO_TIPO_CODIGO"));
				audienciaProcessoVotantesDt.setImpedimentoTipo(rs1.getString("IMPEDIMENTO_TIPO"));
				audienciaProcessoVotantesDt.setOrdemVotante(rs1.getString("ORDEM_VOTANTE"));
				audienciaProcessoVotantesDt.setId_VotanteTipo(rs1.getString("ID_VOTANTE_TIPO"));
				audienciaProcessoVotantesDt.setVotanteTipoCodigo(rs1.getString("VOTANTE_TIPO_CODIGO"));
				audienciaProcessoVotantesDt.setVotanteTipo(rs1.getString("VOTANTE_TIPO"));
				audienciaProcessoVotantesDt.setConvocado(rs1.getBoolean("CONVOCADO"));

				listaVotantes.add(audienciaProcessoVotantesDt);
			}
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
			} catch (Exception e) {
			}
		}

		return listaVotantes;
	}

}
