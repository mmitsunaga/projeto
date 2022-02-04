package br.gov.go.tj.projudi.sessaoVirtual.ps;

import java.sql.Connection;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

public class AudienciaProcessoSessaoVirtualPs extends Persistencia {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6597424631076281295L;

	public AudienciaProcessoSessaoVirtualPs(Connection conexao) {
		Conexao = conexao;
	}

	/**
	 * Retorna os dados completos de uma AudienciaProcesso, já setando os dados da audiência vinculada
	 * 
	 * @param id_AudienciaProcesso, identificação da AudienciaProcesso
	 * @author msapaula
	 */
	public AudienciaProcessoDt consultarIdPJD(String id_AudienciaProcesso) throws Exception {
		AudienciaProcessoDt audienciaProcessoDt = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(obtenhaListaCamposViewAudiCompletaPJD()); 
		sql.append(", AP.OBSERVACOES, AP.SITUACAO_TMP FROM PROJUDI.VIEW_AUDI_COMPLETA ac");
		sql.append(" 	INNER JOIN AUDI_PROC AP ON AP.ID_AUDI_PROC = AC.ID_AUDI_PROC ");
		sql.append(" 	WHERE ac.ID_AUDI_PROC = ?"); ps.adicionarLong(id_AudienciaProcesso);
		sql.append(" 	ORDER BY ORDEM_2_GRAU, PROC_TIPO, ID_AUDI_PROC ");

		ResultSetTJGO rs1 = null;
		try{
			rs1 = this.consultar(sql.toString(),ps);

			if (rs1.next()) {
				audienciaProcessoDt = new AudienciaProcessoDt();
				this.associarAudienciaProcessoDt(audienciaProcessoDt, rs1);
				audienciaProcessoDt.setNomeResponsavel(rs1.getString("NOME"));
				audienciaProcessoDt.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
				audienciaProcessoDt.setNomeMPProcesso(rs1.getString("NOME_MP"));
				//audienciaProcessoDt.setObservacoes(rs1.getString("OBSERVACOES"));
				//audienciaProcessoDt.setSituacaoTemp(rs1.getInt("SITUACAO_TMP"));

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
	
	private String obtenhaListaCamposViewAudiCompletaPJD() {
    	String sql = ""; 
    	sql = "SELECT AC.ID_AUDI, AC.ID_AUDI_TIPO, AC.AUDI_TIPO, AC.AUDI_TIPO_CODIGO, AC.DATA_AGENDADA, AC.DATA_MOVI_AUDI, AC.RESERVADA, ";
    	sql += " AC.ID_AUDI_PROC, AC.ID_AUDI_PROC_STATUS, AC.AUDI_PROC_STATUS, AC.AUDI_PROC_STATUS_CODIGO, AC.ID_SERV_CARGO, AC.SERV_CARGO, ";
    	sql += " AC.USU, AC.NOME, AC.ID_SERV, AC.SERV, AC.ID_PROC, AC.PROC_NUMERO, AC.DIGITO_VERIFICADOR, AC.PROC_NUMERO_COMPLETO, AC.DATA_MOVI_AUDI_PROC, ";
    	sql += " AC.ID_RECURSO, AC.DATA_RETORNO, AC.CODIGO_TEMP, AC.CODIGO_TEMP_AUDI_PROC, AC.ID_AUDI_PROC_STATUS_ANA, AC.AUDI_PROC_STATUS_ANA, ";
    	sql += " AC.AUDI_PROC_STATUS_CODIGO_ANA, AC.ID_ARQ_ATA, AC.DATA_AUDI_ORIGINAL, AC.ID_ARQ_ATA_ADIAMENTO, AC.ID_ARQ_ATA_INICIADO, ";
    	sql += " AC.ORDEM_2_GRAU,  AC.ID_PROC_TIPO, AC.PROC_TIPO_CODIGO, AC.PROC_TIPO, AC.ID_ARQ_FINALIZACAO, AC.ID_SERV_CARGO_PRESIDENTE, ";
    	sql += " AC.SERV_CARGO_PRESIDENTE, AC.USU_PRESIDENTE, AC.NOME_PRESIDENTE, AC.ID_SERV_PRESIDENTE, AC.SERV_PRESIDENTE, AC.ID_SERV_CARGO_MP, ";
    	sql += " AC.SERV_CARGO_MP, AC.USU_MP, AC.NOME_MP, AC.ID_SERV_MP, AC.SERV_MP, AC.ID_SERV_CARGO_REDATOR, AC.SERV_CARGO_REDATOR, AC.USU_REDATOR, ";
    	sql += " AC.NOME_REDATOR, AC.ID_SERV_REDATOR, AC.SERV_REDATOR, AC.PRIMEIRO_PROMOVENTE_RECORRENTE, AC.PRIMEIRO_PROMOVIDO_RECORRIDO, ";
    	sql += " AC.ID_PROC_TIPO_AUDIENCIA, AC.PROC_TIPO_AUDIENCIA, AC.POLO_ATIVO, AC.POLO_PASSIVO, AC.ID_PEND_VOTO, AC.ID_PEND_EMENTA, AC.SESSAO_INICIADA, AC.ID_PEND_VOTO_REDATOR, AC.ID_PEND_EMENTA_REDATOR ";
        return sql;
    }
	
	/**
	 * Método para associar os dados de "AudienciaProcesso" de acordo com campos definidos nas visões
	 * para consulta de audiências
	 * 
	 * @param audienciaProcessoDt
	 * @param rs
	 * @throws Exception 
	 * @author lrcampos
	 * @since 14/05/2020 14:17
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
	
	public void alterar(AudienciaProcessoDt dados) throws Exception{

		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		////System.out.println("....psAudienciaProcessoalterar()");

		stSql= "UPDATE PROJUDI.AUDI_PROC SET  ";
		stSql+= "ID_AUDI = ?";		 ps.adicionarLong(dados.getId_Audiencia());  

		stSql+= ",ID_AUDI_PROC_STATUS = ?";		 ps.adicionarLong(dados.getId_AudienciaProcessoStatus());  

		stSql+= ",ID_SERV_CARGO = ?";		 ps.adicionarLong(dados.getId_ServentiaCargo());  

		stSql+= ",ID_SERV_CARGO_REDATOR = ?";		 ps.adicionarLong(dados.getId_ServentiaCargoRedator());  // jvosantos - 04/06/2019 10:33 - Adiciona o redator
		
		stSql+= ",ID_SERV_CARGO_PRESIDENTE = ?";		 ps.adicionarLong(dados.getId_ServentiaCargoPresidente());  // jvosantos - 25/09/2019 09:25 - Adiciona o presidente
		
		stSql+= ",ID_SERV_CARGO_MP = ?";		 ps.adicionarLong(dados.getId_ServentiaCargoMP());  // mrbatista - 05/03/2020 09:25 - Adiciona o MP

		stSql+= ",ID_PROC = ?";		 ps.adicionarLong(dados.getId_Processo());

		stSql+= ",DATA_MOVI = ?";		 ps.adicionarDateTime(dados.getDataMovimentacao());  

		stSql += " WHERE ID_AUDI_PROC  = ? "; 		ps.adicionarLong(dados.getId()); 

		executarUpdateDelete(stSql,ps);
	
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
}
