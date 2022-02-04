package br.gov.go.tj.projudi.ps;

import java.io.Serializable;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.CertidaoExecucaoCPCDt;
import br.gov.go.tj.projudi.dt.CertidaoNegativaPositivaDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.NumeroProcessoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPositivaNegativaDt;
import br.gov.go.tj.projudi.dt.ProcessoCertidaoPraticaForenseDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoCriminalDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoSubtipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoAudienciaDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoParalisadoDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoSemMovimentacaoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioLiminarDeferidaDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;
import br.gov.go.tj.utils.TJDataHora;

public class ProcessoPs extends ProcessoPsGen implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7643071962993282074L;
	
	public ProcessoPs(Connection conexao){
		Conexao = conexao;
	}
	
	/**
	 * Consultar os dados do processo completo através do ID
	 * 
	 * @param id_Processo
	 * @return
	 * @throws Exception
	 * @author mmgomes/ jrcorrea 14/10/2015
	 */
	public ProcessoDt consultarIdCompleto(String id_Processo) throws Exception {
		
		String Sql;
		ProcessoDt processoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String digital100;
		
		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_COMPLETO ";		
		Sql += "WHERE ID_PROC = ? ";		ps.adicionarLong(id_Processo);

		// No caso de possuir mais de um recurso ativo...
		Sql += "AND (ID_SERV_RECURSO IS NULL OR ID_SERV_RECURSO = ID_SERV)";

		try{
			rs1 = consultar(Sql, ps);			
			while (rs1.next()) {
				//completo os dados da partes do processo
				if (processoDt == null){
					processoDt = new ProcessoDt();
					associarDt(processoDt, rs1);
					processoDt.setId_AreaDistribuicao(rs1.getString("ID_AREA_DIST"));
					processoDt.setAreaDistribuicao(rs1.getString("AREA_DIST"));
					processoDt.setId_AreaDistribuicaoRecursal(rs1.getString("ID_AREA_DIST_RECURSAL"));
					processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
					processoDt.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
					processoDt.setServentiaSubTipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
					
					processoDt.setProcessoNumeroAntigoTemp(rs1.getString("PROC_NUMERO_ANTIGO_TEMP"));
					processoDt.setTabelaOrigemTemp(rs1.getString("TABELA_ORIGEM_TEMP"));
					processoDt.setDataDigitalizacao(Funcoes.FormatarData(rs1.getDateTime("DATA_DIGITALIZACAO")));
					processoDt.setCodigoTemp(rs1.getString("CODIGO_TEMP"));
					processoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
					processoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
					
					//Parte de processo se dependente de SSG ou SPG
					processoDt.setProcessoFisicoTipo(rs1.getString("PROCESSO_FISICO_TIPO"));
					processoDt.setProcessoFisicoNumero(Funcoes.formataNumeroCompletoProcesso(rs1.getString("PROCESSO_FISICO_NUMERO")));
					processoDt.setProcessoFisicoComarcaCodigo(rs1.getString("PROCESSO_FISICO_COMARCA_CODIGO"));
					processoDt.setProcessoFisicoComarcaNome(rs1.getString("PROCESSO_FISICO_COMARCA_NOME"));
					
					//Nova forma de verificar se um processo é uma precatória expedida on-line por um magistrado
					if (processoDt.isProcessoTipoPrecatorias() && this.isProcessoPrecatoriaExpedidaOnline(processoDt.getId())) {
						processoDt.setProcessoPrecatoriaExpedidaOnline(true);
					} else{
						processoDt.setProcessoPrecatoriaExpedidaOnline(false);
					}
					
					digital100 = rs1.getString("DIGITAL100");
					processoDt.set100Digital(digital100 == null || digital100.equals("0")?false:true);
					
				//se chegar em outro processo saio
				}else if(!processoDt.getId().equals(rs1.getString("ID_PROC"))){
					break;
				}
				
				//Setando dados das Partes
				ProcessoParteDt parteDt = new ProcessoParteDt();
				parteDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				parteDt.setId_Processo(rs1.getString("ID_PROC"));
				parteDt.setNome(rs1.getString("NOME"));
				parteDt.setNomeMae(rs1.getString("NOME_MAE"));
				parteDt.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				parteDt.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
				parteDt.setCpf(rs1.getString("CPF"));
				parteDt.setCnpj(rs1.getString("CNPJ"));
				parteDt.setDataBaixa(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_BAIXA")));
				parteDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				parteDt.setCitada(Funcoes.FormatarLogico(rs1.getString("CITADA")));
				parteDt.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				parteDt.setIdade(String.valueOf(Funcoes.calculeIdade(parteDt.getDataNascimento())));
				parteDt.setId_ProcessoParteAusencia(rs1.getString("ID_PROC_PARTE_AUS"));
				parteDt.setTelefone(rs1.getString("TELEFONE"));
				parteDt.setEMail(rs1.getString("EMAIL"));
				parteDt.setReuPreso(rs1.getString("reu_preso"));

				//Setando endereço da parte
				EnderecoDt enderecoDt = new EnderecoDt();
				enderecoDt.setId(rs1.getString("ID_ENDERECO"));
				enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
				enderecoDt.setNumero(rs1.getString("NUMERO"));
				enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
				enderecoDt.setCep(rs1.getString("CEP"));
				enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
				enderecoDt.setBairro(rs1.getString("BAIRRO"));
				enderecoDt.setCidade(rs1.getString("CIDADE"));
				enderecoDt.setUf(rs1.getString("UF"));
				parteDt.setEnderecoParte(enderecoDt);

				//Adiciona parte a lista correspondente
				if (rs1.getString("PROC_PARTE_TIPO_CODIGO") != null) {
					int tipo = Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					switch (tipo) {
						case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
							processoDt.addListaPoloAtivo(parteDt);
							break;
						case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
							processoDt.addListaPolosPassivos(parteDt);
							break;
						default:
							processoDt.addListaOutrasPartes(parteDt);
							break;
					}
				}
			}	
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
				
		return processoDt;
	}
	
		

	/**
	 * Consultar dados completos do processo de acordo com id ou número passados
	 * 
	 * @param id_processo, identificação do processo
	 * @param numeroProcesso, número do processo
	 * 
	 * @author msapaula
	 *         Alterado por mmgomes jrcorrea 14/10/2015
	 */	
	public ProcessoDt consultarProcessoNumeroCompleto(String numeroProcesso, String digitoVerificador, String ano, String forumCodigo) throws Exception {
		String Sql;
		ProcessoDt processoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();					

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_COMPLETO ";
		
		Sql += " WHERE PROC_NUMERO = ?";			ps.adicionarLong(numeroProcesso);				
		Sql += " AND DIGITO_VERIFICADOR  = ?";		ps.adicionarLong(digitoVerificador);		
		Sql += " AND ANO = ?";						ps.adicionarLong(ano);
		if (forumCodigo != null) {
			Sql += " AND FORUM_CODIGO = ?";				ps.adicionarLong(forumCodigo);
		}
							
		// No caso de possuir mais de um recurso ativo...
		Sql += " AND (ID_SERV_RECURSO IS NULL OR ID_SERV_RECURSO = ID_SERV)";
		Sql += " AND ID_PROC_STATUS != ?";			ps.adicionarLong(ProcessoStatusDt.SIGILOSO);
		
		try{
			rs1 = consultar(Sql, ps);			
			while (rs1.next()) {
																	
				//completo os dados da partes do processo
				if (processoDt == null){
					processoDt = new ProcessoDt();
					associarDt(processoDt, rs1);
					processoDt.setId_AreaDistribuicao(rs1.getString("ID_AREA_DIST"));
					processoDt.setAreaDistribuicao(rs1.getString("AREA_DIST"));
					processoDt.setId_AreaDistribuicaoRecursal(rs1.getString("ID_AREA_DIST_RECURSAL"));
					processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
					processoDt.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
					processoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
					processoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
					
				//se chegar em outro processo saio
				}else if(!processoDt.getId().equals(rs1.getString("ID_PROC"))){
					break;
				}
				
				//Setando dados das Partes
				ProcessoParteDt parteDt = new ProcessoParteDt();
				parteDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				parteDt.setId_Processo(rs1.getString("ID_PROC"));
				parteDt.setNome(rs1.getString("NOME"));
				parteDt.setNomeMae(rs1.getString("NOME_MAE"));
				parteDt.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				parteDt.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
				parteDt.setCpf(rs1.getString("CPF"));
				parteDt.setCnpj(rs1.getString("CNPJ"));
				parteDt.setDataBaixa(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_BAIXA")));
				parteDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				parteDt.setCitada(Funcoes.FormatarLogico(rs1.getString("CITADA")));
				parteDt.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				parteDt.setIdade(String.valueOf(Funcoes.calculeIdade(parteDt.getDataNascimento())));
				parteDt.setId_ProcessoParteAusencia(rs1.getString("ID_PROC_PARTE_AUS"));
				parteDt.setTelefone(rs1.getString("TELEFONE"));
				parteDt.setEMail(rs1.getString("EMAIL"));

				//Setando endereço da parte
				EnderecoDt enderecoDt = new EnderecoDt();
				enderecoDt.setId(rs1.getString("ID_ENDERECO"));
				enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
				enderecoDt.setNumero(rs1.getString("NUMERO"));
				enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
				enderecoDt.setCep(rs1.getString("CEP"));
				enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
				enderecoDt.setBairro(rs1.getString("BAIRRO"));
				enderecoDt.setCidade(rs1.getString("CIDADE"));
				enderecoDt.setUf(rs1.getString("UF"));
				parteDt.setEnderecoParte(enderecoDt);

				//Adiciona parte a lista correspondente
				if (rs1.getString("PROC_PARTE_TIPO_CODIGO") != null) {
					int tipo = Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					switch (tipo) {
						case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
							processoDt.addListaPoloAtivo(parteDt);
							break;
						case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
							processoDt.addListaPolosPassivos(parteDt);
							break;
						default:
							processoDt.addListaOutrasPartes(parteDt);
							break;
					}
				}
			}	
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
				
		
		return processoDt;
	}
	
	/**
	 * ATENÇÃO: ESTE MÉTODO FOI DESENVOLVIDO ESPECIFICAMENTE PARA O SERVIÇO DE TROCA AUTOMÁTICA DE NÚMERO DE
	 * PROCESSOS DE EXECUÇÃO PENAL.
	 * 	
	 * @param idProc
	 * @param numeroProcesso
	 * @param digitoVerificador
	 * @param ano
	 * @param forumCodigo
	 * @return
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoNumeroCompletoIdProc(String idProc, String numeroProcesso, String digitoVerificador, String ano, String forumCodigo) throws Exception {
		String Sql;
		ProcessoDt processoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();					

		Sql = "SELECT ID_PROC, PROC_NUMERO, DIGITO_VERIFICADOR, ANO, FORUM_CODIGO FROM PROJUDI.PROC ";
		
		Sql += " WHERE PROC_NUMERO = ?";			ps.adicionarLong(numeroProcesso);				
		Sql += " AND DIGITO_VERIFICADOR  = ?";		ps.adicionarLong(digitoVerificador);		
		Sql += " AND ANO = ?";						ps.adicionarLong(ano);
		if (forumCodigo != null) {
			Sql += " AND FORUM_CODIGO = ?";			ps.adicionarLong(forumCodigo);
		}
		Sql += " AND ID_PROC = ?";					ps.adicionarLong(idProc);
							
		
		try{
			rs1 = consultar(Sql, ps);			
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
			}	
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
				
		return processoDt;
	}
	
	/**
	 * Consultar dados completos do processo de acordo com número passado
	 * 
	 * @param id_processo, identificação do processo
	 * @param numeroProcesso, número do processo
	 * 
	 * @author asrocha
	 */	
	public ProcessoDt consultarProcessoPrincipal(String numeroProcesso, String digitoVerificador, String ano, String forumCodigo) throws Exception {
		String Sql;
		ProcessoDt processoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();					

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_COMPLETO ";
		
		Sql += " WHERE PROC_NUMERO = ?";			ps.adicionarLong(numeroProcesso);				
		Sql += " AND DIGITO_VERIFICADOR  = ?";		ps.adicionarLong(digitoVerificador);		
		Sql += " AND ANO = ?";						ps.adicionarLong(ano);
		if (forumCodigo != null) {
			Sql += " AND FORUM_CODIGO = ?";				ps.adicionarLong(forumCodigo);
		}
							
		// No caso de possuir mais de um recurso ativo...
		Sql += " AND (ID_SERV_RECURSO IS NULL OR ID_SERV_RECURSO = ID_SERV)";
		
		try{
			rs1 = consultar(Sql, ps);			
			while (rs1.next()) {
																	
				//completo os dados da partes do processo
				if (processoDt == null){
					processoDt = new ProcessoDt();
					associarDt(processoDt, rs1);
					processoDt.setId_AreaDistribuicao(rs1.getString("ID_AREA_DIST"));
					processoDt.setAreaDistribuicao(rs1.getString("AREA_DIST"));
					processoDt.setId_AreaDistribuicaoRecursal(rs1.getString("ID_AREA_DIST_RECURSAL"));
					processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
					processoDt.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
					processoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
					processoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
					
				//se chegar em outro processo saio
				}else if(!processoDt.getId().equals(rs1.getString("ID_PROC"))){
					break;
				}
				
				//Setando dados das Partes
				ProcessoParteDt parteDt = new ProcessoParteDt();
				parteDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				parteDt.setId_Processo(rs1.getString("ID_PROC"));
				parteDt.setNome(rs1.getString("NOME"));
				parteDt.setNomeMae(rs1.getString("NOME_MAE"));
				parteDt.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				parteDt.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
				parteDt.setCpf(rs1.getString("CPF"));
				parteDt.setCnpj(rs1.getString("CNPJ"));
				parteDt.setDataBaixa(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_BAIXA")));
				parteDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				parteDt.setCitada(Funcoes.FormatarLogico(rs1.getString("CITADA")));
				parteDt.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				parteDt.setIdade(String.valueOf(Funcoes.calculeIdade(parteDt.getDataNascimento())));
				parteDt.setId_ProcessoParteAusencia(rs1.getString("ID_PROC_PARTE_AUS"));
				parteDt.setTelefone(rs1.getString("TELEFONE"));
				parteDt.setEMail(rs1.getString("EMAIL"));

				//Setando endereço da parte
				EnderecoDt enderecoDt = new EnderecoDt();
				enderecoDt.setId(rs1.getString("ID_ENDERECO"));
				enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
				enderecoDt.setNumero(rs1.getString("NUMERO"));
				enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
				enderecoDt.setCep(rs1.getString("CEP"));
				enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
				enderecoDt.setBairro(rs1.getString("BAIRRO"));
				enderecoDt.setCidade(rs1.getString("CIDADE"));
				enderecoDt.setUf(rs1.getString("UF"));
				parteDt.setEnderecoParte(enderecoDt);

				//Adiciona parte a lista correspondente
				if (rs1.getString("PROC_PARTE_TIPO_CODIGO") != null) {
					int tipo = Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					switch (tipo) {
						case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
							processoDt.addListaPoloAtivo(parteDt);
							break;
						case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
							processoDt.addListaPolosPassivos(parteDt);
							break;
						default:
							processoDt.addListaOutrasPartes(parteDt);
							break;
					}
				}
			}	
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
				
		
		return processoDt;
	}
	
//	/**
//	 * Consultar os dados do processo completo através do número do processo e do dígito verificador
//	 * 
//	 * @param numeroProcesso
//	 * @param digitoVerificador
//	 * @return
//	 * @throws Exception
//	 */
//	public List<ProcessoDt> consultarIdCompleto(String numeroProcesso, String digitoVerificador) throws Exception {
//		String Sql;
//		ProcessoDt processoDt = null;
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps = new PreparedStatementTJGO();
//		Map<String, ProcessoDt> mapProcesso = new LinkedHashMap<String, ProcessoDt>();
//		List<ProcessoDt> listaProcessos = new ArrayList<ProcessoDt>();
//		boolean possuiClausulaWhere = false;
//
//		Sql = "SELECT * FROM PROJUDI.VIEW_BUSCA_PROC ";
//
//		if (numeroProcesso != null && numeroProcesso.length() > 0) {
//			Sql += "WHERE PROC_NUMERO = ?";
//			ps.adicionarLong(numeroProcesso);
//			possuiClausulaWhere = true;
//		}
//		if (digitoVerificador != null && digitoVerificador.length() > 0) {
//			if (possuiClausulaWhere)
//				Sql += " AND";
//			else {
//				possuiClausulaWhere = true;
//				Sql += " WHERE";
//			}
//			Sql += " DIGITO_VERIFICADOR  = ?";
//			ps.adicionarLong(digitoVerificador);
//		}
//		if (possuiClausulaWhere)
//			Sql += " AND";
//		else {
//			possuiClausulaWhere = true;
//			Sql += " WHERE";
//		}
//		// No caso de possuir mais de um recurso ativo...
//		Sql += " (ID_SERV_RECURSO IS NULL OR ID_SERV_RECURSO = ID_SERV)";
//
//		try {
//			rs1 = consultar(Sql, ps);
//			while (rs1.next()) {
//				
//				processoDt = (ProcessoDt) mapProcesso.get(rs1.getString("ID_PROC"));
//				
//				//completo os dados da partes do processo
//				if (processoDt == null){
//					processoDt = new ProcessoDt();
//					associarDt(processoDt, rs1);
//					processoDt.setId_AreaDistribuicao(rs1.getString("ID_AREA_DIST"));
//					processoDt.setAreaDistribuicao(rs1.getString("AREA_DIST"));
//					processoDt.setId_AreaDistribuicaoRecursal(rs1.getString("ID_AREA_DIST_RECURSAL"));
//					processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
//					processoDt.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
//					processoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
//					processoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
//					
//					mapProcesso.put(rs1.getString("ID_PROC"), processoDt);
//				}
//
//				//Setando dados das Partes
//				ProcessoParteDt parteDt = new ProcessoParteDt();
//				parteDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
//				parteDt.setId_Processo(rs1.getString("ID_PROC"));
//				parteDt.setNome(rs1.getString("NOME"));
//				parteDt.setNomeMae(rs1.getString("NOME_MAE"));
//				parteDt.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
//				parteDt.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
//				parteDt.setCpf(rs1.getString("CPF"));
//				parteDt.setCnpj(rs1.getString("CNPJ"));
//				parteDt.setDataBaixa(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_BAIXA")));
//				parteDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
//				parteDt.setCitada(Funcoes.FormatarLogico(rs1.getString("CITADA")));
//				parteDt.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
//				parteDt.setIdade(String.valueOf(Funcoes.calculeIdade(parteDt.getDataNascimento())));
//				parteDt.setId_ProcessoParteAusencia(rs1.getString("ID_PROC_PARTE_AUS"));
//				parteDt.setTelefone(rs1.getString("TELEFONE"));
//				parteDt.setEMail(rs1.getString("EMAIL"));
//
//				//Setando endereço da parte
//				EnderecoDt enderecoDt = new EnderecoDt();
//				enderecoDt.setId(rs1.getString("ID_ENDERECO"));
//				enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
//				enderecoDt.setNumero(rs1.getString("NUMERO"));
//				enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
//				enderecoDt.setCep(rs1.getString("CEP"));
//				enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
//				enderecoDt.setBairro(rs1.getString("BAIRRO"));
//				enderecoDt.setCidade(rs1.getString("CIDADE"));
//				enderecoDt.setUf(rs1.getString("UF"));
//				parteDt.setEnderecoParte(enderecoDt);
//
//				//Adiciona parte a lista correspondente
//				if (rs1.getString("PROC_PARTE_TIPO_CODIGO") != null) {
//					int tipo = Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
//					switch (tipo) {
//						case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
//							processoDt.addListaPromoventes(parteDt);
//							break;
//						case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
//							processoDt.addListaPromovidos(parteDt);
//							break;
//						default:
//							processoDt.addListaOutrasPartes(parteDt);
//							break;
//					}
//				}
//			}
//		} finally {
//			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
//		}
//
//		listaProcessos.addAll(mapProcesso.values()); // Adiciona na lista os
//													 // valores do map
//		mapProcesso.clear();
//		mapProcesso = null;
//
//		return listaProcessos;
//	}
	
//	/**
//	 * Consultar dados completos do processo de acordo com id ou número passados
//	 * 
//	 * @param id_processo, identificação do processo
//	 * @param numeroProcesso, número do processo
//	 * 
//	 * @author fasoares
//	 *         Criado por fasoares com base no método acima "consultarIdCompleto"
//	 */	
//	public ProcessoDt consultarProcessoNumeroCompleto(String numeroProcesso, String digitoVerificador, String ano, String forumCodigo) throws Exception {
//		String Sql;
//		ProcessoDt processoDt = null;
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();					
//
//		Sql = "SELECT * FROM PROJUDI.VIEW_BUSCA_PROC ";		
//		Sql += " WHERE PROC_NUMERO = ?";			ps.adicionarLong(numeroProcesso);		
//		Sql += " AND DIGITO_VERIFICADOR  = ?";		ps.adicionarLong(digitoVerificador);		
//		Sql += " AND ANO = ?";						ps.adicionarLong(ano);
//		if (forumCodigo != null) {
//			Sql += " AND FORUM_CODIGO = ?";			ps.adicionarLong(forumCodigo);	
//		}
//							
//		try{
//			rs1 = consultar(Sql, ps);			
//			while (rs1.next()) {
//																	
//				//completo os dados da partes do processo
//				if (processoDt == null){
//					processoDt = new ProcessoDt();
//					associarDt(processoDt, rs1);
//					processoDt.setId_AreaDistribuicao(rs1.getString("ID_AREA_DIST"));
//					processoDt.setAreaDistribuicao(rs1.getString("AREA_DIST"));
//					processoDt.setId_AreaDistribuicaoRecursal(rs1.getString("ID_AREA_DIST_RECURSAL"));
//					processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
//					processoDt.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
//					processoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
//					processoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
//					
//				//se chegar em outro processo saio
//				}else if(!processoDt.getId().equals(rs1.getString("ID_PROC"))){
//					break;
//				}
//				
//				//Setando dados das Partes
//				ProcessoParteDt parteDt = new ProcessoParteDt();
//				parteDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
//				parteDt.setId_Processo(rs1.getString("ID_PROC"));
//				parteDt.setNome(rs1.getString("NOME"));
//				parteDt.setNomeMae(rs1.getString("NOME_MAE"));
//				parteDt.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
//				parteDt.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
//				parteDt.setCpf(rs1.getString("CPF"));
//				parteDt.setCnpj(rs1.getString("CNPJ"));
//				parteDt.setDataBaixa(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_BAIXA")));
//				parteDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
//				parteDt.setCitada(Funcoes.FormatarLogico(rs1.getString("CITADA")));
//				parteDt.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
//				parteDt.setIdade(String.valueOf(Funcoes.calculeIdade(parteDt.getDataNascimento())));
//				parteDt.setId_ProcessoParteAusencia(rs1.getString("ID_PROC_PARTE_AUS"));
//				parteDt.setTelefone(rs1.getString("TELEFONE"));
//				parteDt.setEMail(rs1.getString("EMAIL"));
//
//				//Setando endereço da parte
//				EnderecoDt enderecoDt = new EnderecoDt();
//				enderecoDt.setId(rs1.getString("ID_ENDERECO"));
//				enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
//				enderecoDt.setNumero(rs1.getString("NUMERO"));
//				enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
//				enderecoDt.setCep(rs1.getString("CEP"));
//				enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
//				enderecoDt.setBairro(rs1.getString("BAIRRO"));
//				enderecoDt.setCidade(rs1.getString("CIDADE"));
//				enderecoDt.setUf(rs1.getString("UF"));
//				parteDt.setEnderecoParte(enderecoDt);
//
//				//Adiciona parte a lista correspondente
//				if (rs1.getString("PROC_PARTE_TIPO_CODIGO") != null) {
//					int tipo = Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
//					switch (tipo) {
//						case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
//							processoDt.addListaPromoventes(parteDt);
//							break;
//						case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
//							processoDt.addListaPromovidos(parteDt);
//							break;
//						default:
//							processoDt.addListaOutrasPartes(parteDt);
//							break;
//					}
//				}
//			}	
//		
//		} finally{
//			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
//		}
//				
//		
//		return processoDt;
//	}
	
	/**
	 * lista os processos existentes de acordo o número passado
	 * @param digitoVerificador
	 * @param numeroProcesso, número do processo
	 * @param ano
	 * @author wcsilva
	 */
	public List listarProcessoNumeroCompleto(String numeroProcesso, String digitoVerificador, String ano) throws Exception {
		List lista = new ArrayList();
		String Sql;
		ProcessoDt processoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT * FROM PROJUDI.VIEW_PROC_COMPLETO ";

		if (numeroProcesso != null && numeroProcesso.length() > 0){
			Sql += "WHERE PROC_NUMERO = ?";
			ps.adicionarLong(numeroProcesso);
		}
		if (digitoVerificador != null && digitoVerificador.length() > 0){
			Sql += " AND DIGITO_VERIFICADOR  = ?";
			ps.adicionarLong(digitoVerificador);
		}
		if (ano != null && ano.length() > 0){
			Sql += " AND ANO  = ?";
			ps.adicionarLong(ano);
		}
		Sql += " ORDER BY ID_PROC ";
		
		try{
			rs1 = consultar(Sql, ps);
			String idProcesso = "";
			
			while (rs1.next()) {
				
				if (!idProcesso.equals(rs1.getString("ID_PROC"))){
					processoDt = new ProcessoDt();
					associarDt(processoDt, rs1);
					
					processoDt.setId_AreaDistribuicao(rs1.getString("ID_AREA_DIST"));
					processoDt.setAreaDistribuicao(rs1.getString("AREA_DIST"));
					processoDt.setId_AreaDistribuicaoRecursal(rs1.getString("ID_AREA_DIST_RECURSAL"));
					processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
					processoDt.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));	
					processoDt.setDescricaoPoloAtivo(rs1.getString("POLO_ATIVO"));
					processoDt.setDescricaoPoloPassivo(rs1.getString("POLO_PASSIVO"));
					
					lista.add(processoDt);
				}
				
				//Setando dados das Partes
				ProcessoParteDt parteDt = new ProcessoParteDt();
				parteDt.setId_ProcessoParte(rs1.getString("ID_PROC_PARTE"));
				parteDt.setNome(rs1.getString("NOME"));
				parteDt.setNomeMae(rs1.getString("NOME_MAE"));
				parteDt.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO"));
				parteDt.setProcessoParteTipoCodigo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
				parteDt.setCpf(rs1.getString("CPF"));
				parteDt.setCnpj(rs1.getString("CNPJ"));
				parteDt.setDataBaixa(Funcoes.FormatarDataHora(rs1.getDateTime("DATA_BAIXA")));
				parteDt.setId_UsuarioServentia(rs1.getString("ID_USU_SERV"));
				parteDt.setCitada(Funcoes.FormatarLogico(rs1.getString("CITADA")));
				parteDt.setDataNascimento(Funcoes.FormatarData(rs1.getDateTime("DATA_NASCIMENTO")));
				parteDt.setId_ProcessoParteAusencia(rs1.getString("ID_PROC_PARTE_AUS"));
				parteDt.setTelefone(rs1.getString("TELEFONE"));
				parteDt.setEMail(rs1.getString("EMAIL"));

				//Setando endereço da parte
				EnderecoDt enderecoDt = new EnderecoDt();
				enderecoDt.setId(rs1.getString("ID_ENDERECO"));
				enderecoDt.setLogradouro(rs1.getString("LOGRADOURO"));
				enderecoDt.setNumero(rs1.getString("NUMERO"));
				enderecoDt.setComplemento(rs1.getString("COMPLEMENTO"));
				enderecoDt.setCep(rs1.getString("CEP"));
				enderecoDt.setId_Bairro(rs1.getString("ID_BAIRRO"));
				enderecoDt.setBairro(rs1.getString("BAIRRO"));
				enderecoDt.setCidade(rs1.getString("CIDADE"));
				enderecoDt.setUf(rs1.getString("UF"));
				parteDt.setEnderecoParte(enderecoDt);

				//Adiciona parte a lista correspondente
				if (rs1.getString("PROC_PARTE_TIPO_CODIGO") != null) {
					int tipo = Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					switch (tipo) {
						case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):
							((ProcessoDt)lista.get(lista.size()-1)).addListaPoloAtivo(parteDt);
							break;
						case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):
							((ProcessoDt)lista.get(lista.size()-1)).addListaPolosPassivos(parteDt);
							break;
						default:
							((ProcessoDt)lista.get(lista.size()-1)).addListaOutrasPartes(parteDt);
							break;
					}
				}
				
				idProcesso = rs1.getString("ID_PROC");
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}
	
	
	/**
	 * Este método retorna o número do processo baseado no id do processo.
	 * 
	 * @param id_processo, identificação do processo
	 * 
	 * @author msapaula
	 */
	public String getNumeroDoProcesso(String id_processo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String numeroDoProcesso = null;
		Sql = "SELECT PROC_NUMERO, DIGITO_VERIFICADOR FROM PROJUDI.PROC WHERE ID_PROC = ?";
		ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				numeroDoProcesso = rs1.getString( "PROC_NUMERO" ) + "." + rs1.getString( "DIGITO_VERIFICADOR" );
				return numeroDoProcesso;
			}
			//rs1.close();

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return numeroDoProcesso;
	}	
	
	public String getIdDoProcesso(String numeroCompletoDoProcesso) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String idProcesso = null;
		NumeroProcessoDt numeroProcessoDt = new NumeroProcessoDt();
		numeroProcessoDt.setNumeroCompletoProcessoSemValidacao(numeroCompletoDoProcesso);
		
		Sql = "SELECT ID_PROC FROM PROJUDI.PROC WHERE PROC_NUMERO = ? AND DIGITO_VERIFICADOR = ? AND FORUM_CODIGO = ? AND ANO = ?";
		ps.adicionarLong(numeroProcessoDt.getNumero());
		ps.adicionarLong(numeroProcessoDt.getDigito());
		ps.adicionarLong(numeroProcessoDt.getForum());
		ps.adicionarLong(numeroProcessoDt.getAno());

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				idProcesso = rs1.getString( "ID_PROC" );
				return idProcesso;
			}		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return idProcesso;
	}	
	
	public String consultarDataDistribuicao(String id_processo) throws Exception {
		String Sql;
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC WHERE ID_PROC = ? ";
		ps.adicionarLong(id_processo);

		try {
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				stTemp = Funcoes.FormatarDataHora(rs1.getDateTime("DATA_RECEBIMENTO"));
			}

		} finally {
			try {if (rs1 != null) rs1.close();} catch (Exception e) {}
		}
		return stTemp;
	}
	
	
	/**
	 * Sobrescrevendo método consultarId para que possa retornar a área de distribuição vinculada ao processo.
	 * Essa informação será útil nos casos de salvar Recurso Inominado e cadastro de processo com dependência.
	 * 
	 * @param id_processo, identificação do processo
	 * 
	 * @author msapaula
	 */
	public ProcessoDt consultarId(String id_processo) throws Exception {
		String Sql;
		ProcessoDt Dados = new ProcessoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC WHERE ID_PROC = ? AND (ID_SERV_RECURSO IS NULL OR ID_SERV_RECURSO = ID_SERV)"; // No caso de possuir mais de um recurso ativo...
		ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				associarDt(Dados, rs1);		
				Dados.setId_Recurso(rs1.getString("ID_RECURSO"));
				Dados.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	/**
	 * A condição adicionada no método consultarId() - condição que trata sobre recursos - impedia que processos
	 * com recursos não autuados aparecessem na consulta, ocasionando erro na troca de MP responsável pelo
	 * processo. Como o coordenador de MP deve poder trocar o MP responsável independente de recurso
	 * autuado ou não, foi criado o método abaixo que não usa o critério novo.  
	 * 
	 * @param id_processo
	 * @return
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoIdTrocaResponsavel(String id_processo) throws Exception {
		String Sql;
		ProcessoDt Dados = new ProcessoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC WHERE ID_PROC = ?";
		ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				associarDt(Dados, rs1);		
				Dados.setId_Recurso(rs1.getString("ID_RECURSO"));
				Dados.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}
	
	/**
	 * Método que verifica se o processo é híbrido
	 * 
	 * @param id_processo, identificação do processo
	 * 
	 * @author hrrosa
	 */
	public boolean verificarProcessoHibrido(String id_processo) throws Exception {
		String Sql;
		ProcessoDt processoDt = new ProcessoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT PROCESSO_FISICO_TIPO FROM PROJUDI.PROC WHERE ID_PROC = ?";
		ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				processoDt.setProcessoFisicoTipo(rs1.getString("PROCESSO_FISICO_TIPO"));
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return processoDt.isProcessoHibrido();
	}
	
	
	/**
	 * Sobrescrevendo método para retornar a data de distribuição
	 */
	/*protected void consultarTemp1(ProcessoDt dados){
		String Sql;
		Sql = "SELECT * FROM PROJUDI.PROC WHERE CODIGO_TEMP = " + dados.getCodigoTemp();
		try{
			ResultSetTJGO rs1 = consultar(Sql);

			if (rs1.next()) {
				dados.setId_Processo(rs1.getString("ID_PROC"));
				dados.setDataRecebimento(Funcoes.FormatarDataHora(rs1.getDate("DATA_RECEBIMENTO")));
				dados.setCodigoTemp("");
				alterar(dados);
			}
			//rs1.close();
		
		}
	}*/

	/**
	 * Consulta último número de processo cadastrado
	 */
	public String consultarUltimoProcesso() throws Exception {
		String Sql;
		String ultimoNumero = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SimpleDateFormat FormatoData = new SimpleDateFormat("yyyy");
		int stAnoAtual = Funcoes.StringToInt(FormatoData.format(new Date()));		

		Sql = "SELECT max(p.PROC_NUMERO) as NUMERO_PROC FROM PROC p WHERE p.ANO=?";
		ps.adicionarLong(stAnoAtual);
		try{
			rs1 = consultar(Sql, ps);

			if (rs1.next()) {
				if (rs1.getString("NUMERO_PROC") != null) ultimoNumero = stAnoAtual + Funcoes.completarZeros(rs1.getString("NUMERO_PROC"), 7);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return ultimoNumero;
	}

	/**
	* Realiza consulta do último ServentiaCargo de uma Serventia que recebeu um processo,
	* de acordo com o tipo de cargo desejado
	*/
	//TODO: Migração Banco - Ajuste no LIMIT
	public int[] consultarUltimaDistribuicaoServentiaCargo(String id_Serventia, int inCargoCodigo) throws Exception {
		StringBuffer stSql;

		int inUltimo[] = new int[2];
		int inQuantidadeDistribuicao = 0;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{

			//pego para qual ServentiaCargo da serventia foi distribuido o último processo
			stSql = new StringBuffer("SELECT max(proc.ID_PROC) as Ultimo, sercar.ID_SERV_CARGO, sercar.QUANTIDADE_DIST  FROM PROJUDI.PROC proc");
			stSql.append(" INNER JOIN PROC_RESP pr on proc.ID_PROC=pr.ID_PROC");
			stSql.append(" INNER JOIN SERV_CARGO sercar ON pr.ID_SERV_CARGO = sercar.ID_SERV_CARGO");
			stSql.append(" INNER JOIN CARGO_TIPO ct on sercar.ID_CARGO_TIPO = ct.ID_CARGO_TIPO");
			stSql.append(" WHERE  proc.ID_SERV = ? AND ct.CARGO_TIPO_CODIGO =  ?");
			stSql.append(" GROUP BY sercar.ID_SERV_CARGO, sercar.QUANTIDADE_DIST");
			ps.adicionarLong(id_Serventia);
			ps.adicionarLong(inCargoCodigo);

//			stSql = new StringBuffer("SELECT sercar.ID_SERV_CARGO, sercar.QuantidadeDistribuicao FROM PROJUDI.PROC pro ");
//			stSql.append(" INNER JOIN ProcessoResponsavel prores on pro.ID_PROC = pro.ID_PROC ");
//			stSql.append(" INNER JOIN SERV_CARGO sercar ON prores.ID_SERV_CARGO = sercar.ID_SERV_CARGO  ");
//			stSql.append(" INNER JOIN CARGO_TIPO ct on sercar.ID_CARGO_TIPO = ct.ID_CARGO_TIPO AND ct.CargoTipoCodigo = " + inCargoCodigo);
//			stSql.append(" WHERE pro.ID_SERV =" + id_Serventia);
//			stSql.append(" ORDER BY pro.ID_PROC desc ");
//			stSql.append(" limit 1");

			rs1 = consultar(stSql.toString(), ps);

			while (rs1.next()) {
				inUltimo[0] = rs1.getInt("ID_SERV_CARGO");
				inQuantidadeDistribuicao = rs1.getInt("QUANTIDADE_DIST");
			}
			//rs1.close();		

			// Conta quantos processos foram distribuidos para o ServentiaCargo que recebeu o ultimo processo 
			stSql = new StringBuffer("SELECT COUNT(*) AS QUANTIDADE FROM (");
			stSql.append(" SELECT * FROM(");
			stSql.append(" SELECT prores.ID_SERV_CARGO");
			stSql.append(" FROM PROC pro INNER JOIN PROC_RESP prores on pro.ID_PROC = prores.ID_PROC ");
//			stSql.append(" INNER JOIN ServentiaCargo sercar on prores.Id_ServentiaCargo= sercar.Id_ServentiaCargo");
			stSql.append(" WHERE pro.ID_SERV= ?");
			stSql.append(" ORDER BY pro.ID_PROC desc");
			stSql.append(" ) TABELA_INTERNA");
			stSql.append(" WHERE ROWNUM <= " + inQuantidadeDistribuicao);
			stSql.append(" ) TABELA WHERE ID_SERV_CARGO=?");			
			ps.limpar();
			ps.adicionarLong(id_Serventia);
			ps.adicionarLong(inUltimo[0]);

			rs2 = consultar(stSql.toString(), ps);

			while (rs2.next()) {
				inUltimo[1] = rs2.getInt("QUANTIDADE");
			}
			//rs1.close();

		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {	}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {	}
		}
		return inUltimo;
	}

	/**
	 * Realiza consulta da última serventia de uma área de distribuição 
	 * que recebeu um processo
	 */
	public int[] consultarUltimaDistribuicao(String id_AreaDistribuicao) throws Exception {
		String stSql;
		int inUltimo[] = new int[2];
		int inQuantidadeDistribuicao = 0;
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{
			//pego para qual serventia foi distribuido o último processo
			stSql = "SELECT pro.ID_SERV, ser.QUANTIDADE_DIST FROM PROJUDI.PROC pro INNER JOIN SERV ser ";
			stSql += " ON pro.ID_SERV = ser.ID_SERV  ";
			stSql += " WHERE ser.ID_AREA_DIST = ?";
			stSql += " ORDER BY ID_PROC desc ";
			ps.adicionarLong(id_AreaDistribuicao);

			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				inUltimo[0] = rs1.getInt("ID_SERV");
				inQuantidadeDistribuicao = rs1.getInt("QUANTIDADE_DIST");
			}
			//rs1.close();			

			// Conta quantos processos foram da serventia que recebeu o ultimo processo 
			stSql = "SELECT COUNT(*) AS QUANTIDADE FROM (";
			stSql += " SELECT * FROM (";
			stSql += " SELECT pro.ID_SERV";
			stSql += " FROM PROC pro INNER JOIN SERV ser on pro.ID_SERV= ser.ID_SERV";
			stSql += " WHERE ser.ID_AREA_DIST=?";
			stSql += " ORDER BY pro.ID_PROC desc";
			stSql += " ) TABELA_INTERNA ";
			stSql += " WHERE ROWNUM <= ?";
			stSql += " ) TABela WHERE ID_SERV=?";
			ps.limpar();
			ps.adicionarLong(id_AreaDistribuicao);
			ps.adicionarLong(inQuantidadeDistribuicao);
			ps.adicionarLong(inUltimo[0]);
			
			rs2 = consultar(stSql, ps);

			while (rs2.next()) {
				inUltimo[1] = rs2.getInt("QUANTIDADE");
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return inUltimo;
	}

	/**
	 * Sobrescrevendo método inserir para setar dataRecebimento com a data atual do banco
	 */
	public void inserir(ProcessoDt dados) throws Exception {

		String SqlCampos;
		String SqlValores;
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		SqlCampos = "INSERT INTO PROJUDI.PROC (";
		SqlValores = " Values (";
		if (!(dados.getProcessoNumeroSimples().length() == 0)){
			SqlCampos += "PROC_NUMERO";
			SqlValores += "?";
			ps.adicionarLong(dados.getProcessoNumeroSimples());
		}
		if (!(dados.getDigitoVerificador().length() == 0)){
			SqlCampos += ",DIGITO_VERIFICADOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getDigitoVerificador());
		}
		if (!(dados.getId_ProcessoPrincipal().length() == 0)){
			SqlCampos += ",ID_PROC_DEPENDENTE";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoPrincipal());
		}	
		if (dados.getId_ProcessoTipo().length() > 0){
			SqlCampos += ",ID_PROC_TIPO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoTipo());
		}else if (dados.getProcessoTipoCodigo().length() > 0){
			SqlCampos += ",ID_PROC_TIPO";			
			SqlValores += ", (SELECT ID_PROC_TIPO FROM PROC_TIPO WHERE PROC_TIPO_CODIGO = ?)";
			ps.adicionarLong(dados.getProcessoTipoCodigo());
		}
		if (!(dados.getProcessoFaseCodigo().length() == 0)){
			SqlCampos += ",ID_PROC_FASE";
			SqlValores += ", (SELECT ID_PROC_FASE FROM PROC_FASE WHERE PROC_FASE_CODIGO = ?)";
			ps.adicionarLong(dados.getProcessoFaseCodigo());
		}
		if (!(dados.getProcessoStatusCodigo().length() == 0)){
			SqlCampos += ",ID_PROC_STATUS";
			SqlValores += ", (SELECT ID_PROC_STATUS FROM PROC_STATUS WHERE PROC_STATUS_CODIGO = ?)";
			ps.adicionarLong(dados.getProcessoStatusCodigo());
		}
		if (!(dados.getId_ProcessoPrioridade().length() == 0)){
			SqlCampos += ",ID_PROC_PRIOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ProcessoPrioridade());
		}
		if ((dados.getId_Serventia().length() > 0)){
			SqlCampos += ",ID_SERV";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Serventia());
		}
		if (!(dados.getId_ServentiaOrigem().length() == 0)){
			SqlCampos += ",ID_SERV_ORIGEM";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ServentiaOrigem());
		}		
		if ((dados.getId_Area().length() > 0)){
			SqlCampos += ",ID_AREA";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Area());
		}else if ((dados.getAreaCodigo().length() > 0)){
			SqlCampos += ",ID_AREA";
			SqlValores += ", (SELECT ID_AREA FROM AREA WHERE AREA_CODIGO = ?)";
			ps.adicionarLong(dados.getAreaCodigo());
		}
		if ((dados.getId_AreaDistribuicao().length() > 0)){
			SqlCampos += ",ID_AREA_DIST";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_AreaDistribuicao());		
		}	
		if (!(dados.getId_ObjetoPedido().length() == 0)){
			SqlCampos += ",ID_OBJETO_PEDIDO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_ObjetoPedido());
		}
		if (!(dados.getId_Classificador().length() == 0)){
			SqlCampos += ",ID_CLASSIFICADOR";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Classificador());
		}
		if (!(dados.getId_Custa_Tipo().length() == 0)){
			SqlCampos += ",ID_CUSTA_TIPO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getId_Custa_Tipo());	
		}
		if (!(dados.getSegredoJustica().length() == 0)){
			SqlCampos += ",SEGREDO_JUSTICA";
			SqlValores += ",?";
			ps.adicionarBoolean(dados.getSegredoJustica());
		}
		if (!(dados.getProcessoDiretorio().length() == 0)){
			SqlCampos += ",PROC_DIRETORIO";
			SqlValores += ",?";
			ps.adicionarString(dados.getProcessoDiretorio());
		}
		if (!(dados.getTcoNumero().length() == 0)){
			SqlCampos += ",TCO_NUMERO";
			SqlValores += ",?";
			ps.adicionarString(dados.getTcoNumero());
		}
		if (!(dados.getValor().length() == 0)){
			SqlCampos += ",VALOR";
			SqlValores += ",?";
			ps.adicionarDecimal(dados.getValor());
		}
		if (dados.getTabelaOrigemTemp() != null && !(dados.getTabelaOrigemTemp().length() == 0)){
			SqlCampos += ",TABELA_ORIGEM_TEMP";
			SqlValores += ",?";
			ps.adicionarString(dados.getTabelaOrigemTemp().trim());
		}
		SqlCampos += ",DATA_RECEBIMENTO";
		SqlValores += ",?";
		
		if(dados.getDataRecebimento() != null && dados.getDataRecebimento().length() != 0) {
			ps.adicionarDateTime(new TJDataHora( dados.getDataRecebimento() ));
		} else {
			ps.adicionarDateTime(new Date());
		}
		
		if (!(dados.getDataArquivamento().length() == 0)){
			SqlCampos += ",DATA_ARQUIVAMENTO";
			SqlValores += ",?";
			ps.adicionarDate(dados.getDataArquivamento());
		}
		
		if ((dados.getDataPrescricao().length()>0)) {
			SqlCampos +=  ", DATA_PRESCRICAO " ;
			SqlValores += ",?";
			 ps.adicionarDate(dados.getDataPrescricao());  
		}		
		
		if ((dados.getApenso().length() > 0)){
			SqlCampos += ",APENSO ";
			SqlValores += ",?";
			ps.adicionarBoolean(dados.getApenso());
		}
		if ((dados.getAno().length() > 0)){
			SqlCampos += ",ANO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getAno());
		}
		if ((dados.getForumCodigo().length() > 0)){
			SqlCampos += ",FORUM_CODIGO ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getForumCodigo());
		}
		if ((dados.getCodigoTemp() != null && dados.getCodigoTemp().trim().length() > 0)){
			SqlCampos += ",CODIGO_TEMP ";
			SqlValores += ",?";
			ps.adicionarLong(dados.getCodigoTemp());
		}
		
		//Dados do Processo Físico SSG ou SPG Dependente
		if (!(dados.getProcessoFisicoTipo()!=null && dados.getProcessoFisicoTipo().length() == 0)){
			SqlCampos += ",PROCESSO_FISICO_TIPO";
			SqlValores += ",?";
			ps.adicionarString(dados.getProcessoFisicoTipo());
		}
		if (!(dados.getProcessoFisicoNumero()!=null && dados.getProcessoFisicoNumero().length() == 0)){
			SqlCampos += ",PROCESSO_FISICO_NUMERO";
			SqlValores += ",?";
			ps.adicionarString(Funcoes.obtenhaSomenteNumeros(dados.getProcessoFisicoNumero()));
		}
		if (!(dados.getProcessoFisicoComarcaCodigo()!=null && dados.getProcessoFisicoComarcaCodigo().length() == 0)){
			SqlCampos += ",PROCESSO_FISICO_COMARCA_CODIGO";
			SqlValores += ",?";
			ps.adicionarLong(dados.getProcessoFisicoComarcaCodigo());
		}
		
		//Salva dados do processo 100% Digital
		SqlCampos += ",DIGITAL100";
		SqlValores += ",?";
		ps.adicionarBoolean(dados.is100Digital());
		
		SqlCampos+=")";
 		SqlValores+=")"; 		

 		Sql = SqlCampos.replace("(,", "(") + SqlValores.replace("(,", "(");	
			
		dados.setId(executarInsert(Sql, "ID_PROC", ps));

	}

	/**
	 * Sobrescrevendo método alterar(Conversão valor ação para decimal)
	 */
	public void alterar(ProcessoDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET  ";

		Sql += " ID_PROC_DEPENDENTE  = ?";				ps.adicionarLong(dados.getId_ProcessoPrincipal());
		Sql += ",ID_PROC_TIPO  = ?";					ps.adicionarLong(dados.getId_ProcessoTipo());
		Sql += ",ID_PROC_FASE  = ?";					ps.adicionarLong(dados.getId_ProcessoFase());
		Sql += ",ID_PROC_PRIOR  = ?";					ps.adicionarLong(dados.getId_ProcessoPrioridade());
		
		if (dados.getId_Serventia()!=null){
			Sql += ",ID_SERV  = ?";  					ps.adicionarLong(dados.getId_Serventia());
		}

		Sql += ",LOCALIZADOR  = ?"; 						ps.adicionarString(dados.getLocalizador());

		//Sql += ",ID_SERV_ORIGEM  = " + Funcoes.BancoInteiro(dados.getId_ServentiaOrigem());
		Sql += ",ID_AREA  = ?"; 						ps.adicionarLong(dados.getId_Area());
		//Sql += ",ID_OBJETO_PEDIDO  = " + Funcoes.BancoInteiro(dados.getId_ObjetoPedido());
		Sql += ",ID_CLASSIFICADOR  = ?";				ps.adicionarLong(dados.getId_Classificador());
		Sql += ",SEGREDO_JUSTICA  = ?";					ps.adicionarBoolean(dados.getSegredoJustica());
		//Sql += ",PROC_DIRETORIO  = " + dados.getProcessoDiretorio());
		//Sql += ",TCO_NUMERO  = " + dados.getTcoNumero());			
		//Sql += ",DATA_RECEBIMENTO  = " + Funcoes.BancoDataHora(dados.getDataRecebimento());				
		Sql += ",APENSO  = ?";							ps.adicionarBoolean(dados.getApenso());		
		if (dados.getEfeitoSuspensivo() != null){
			Sql += ",EFEITO_SUSPENSIVO = ?";
			ps.adicionarBoolean(dados.getEfeitoSuspensivo());	
		}		
		if (dados.getEfeitoSuspensivo() != null){
			Sql += ",JULGADO_2_GRAU = ?";						ps.adicionarBoolean(dados.getJulgado2Grau());	
		}
		if (dados.getPenhora() != null){
			Sql += ",PENHORA = ?";								ps.adicionarBoolean(dados.getPenhora());	
		}
		if (dados.getDataTransitoJulgado() != null){
			Sql += ",DATA_TRANSITO_JULGADO = ?";				ps.adicionarDate(dados.getDataTransitoJulgado());	
		}
		if (dados.getDataArquivamento() != null){
			Sql += ",DATA_ARQUIVAMENTO = ?";					ps.adicionarDate(dados.getDataArquivamento());	
		}
		
		if (dados.getDataPrescricao() != null){
			Sql += ",DATA_PRESCRICAO = ?";		        		ps.adicionarDate(dados.getDataPrescricao());
		}

		if (dados.getProcessoStatusCodigo() != null){
			Sql += ",ID_PROC_STATUS  = (SELECT ID_PROC_STATUS FROM PROJUDI.PROC_STATUS WHERE PROC_STATUS_CODIGO = ?)"; 	ps.adicionarLong(dados.getProcessoStatusCodigo());
		}
		if (dados.getId_AreaDistribuicao() != null){
			Sql += ",ID_AREA_DIST = ?";							ps.adicionarLong(dados.getId_AreaDistribuicao());	
		}
		if (dados.getValor() != null){
			Sql += ",VALOR  = ?";
			ps.adicionarDecimal(dados.getValor());
		}
		if (dados.getValorCondenacao() != null){
			Sql += ",VALOR_CONDENACAO  = ?";
			ps.adicionarDecimal(dados.getValorCondenacao());
		}
		if (dados.getTcoNumero() != null){
			Sql += ",TCO_NUMERO = ?";
			ps.adicionarString(dados.getTcoNumero());	
		}	
		if (dados.getId_Custa_Tipo() != null){
			Sql += ",ID_CUSTA_TIPO = ?";
			ps.adicionarLong(dados.getId_Custa_Tipo());	
		}
		Sql += ",DIGITAL100 = ?";
		ps.adicionarLong(dados.is100Digital()?1:0);	
		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_PROC  = ?";
		ps.adicionarLong(dados.getId());

		executarUpdateDelete(Sql, ps);

	}
	
	/**
	 * Sobrescrevendo método alterar(Conversão valor ação para decimal)
	 */
	public void alterarProcessoRecurso(ProcessoDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET  ";

		Sql += ",ID_PROC_FASE  = ?";
		ps.adicionarLong(dados.getId_ProcessoFase());
		Sql += ",ID_PROC_PRIOR  = ?";
		ps.adicionarLong(dados.getId_ProcessoPrioridade());
		Sql += ",ID_CLASSIFICADOR  = ?";
		ps.adicionarLong(dados.getId_Classificador());
		Sql += ",SEGREDO_JUSTICA  = ?";
		ps.adicionarBoolean(dados.getSegredoJustica());
		Sql += ",PENHORA  = ?";
		ps.adicionarBoolean(dados.getPenhora());
		Sql += ",VALOR  = ?";
		ps.adicionarDecimal(dados.getValor());

		Sql = Sql.replace("SET  ,", "SET  ");
		Sql = Sql + " WHERE ID_PROC  = ?";
		ps.adicionarLong(dados.getId());

			executarUpdateDelete(Sql, ps);

	}

	/**
	 * Método responsável em consultar os processos de acordo com parâmetros passados.
	 * 
	 * @param nomeParte, filtro de nome da parte
	 * @param pesquisarNomeExato, determina se a pesquisa de nome parte será por nome exato ou não
	 * @param cpfCnpjParte, filtro do cpf ou cnj da parte
	 * @param processoNumero, filtro do número do processo
	 * @param statusCodigo, filtro de status do processo
	 * @param processoTipo, filtro de tipo de processo
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * @param id_Comarca, identificação da comarca para filtrar os processos
	 * @param SegredoJustica, define se devem ser retornados os processos com segredo de justiça
	 * @param posicao parametro para paginação
	 * 
	 * @author msapaula, hmgodinho, jrcorrea 26/05/2015
	 */
	public List consultarProcessos(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, int processoNumero, int digito, int ano, String statusCodigo, String id_ProcessoTipo, String id_Serventia, String id_Comarca, String segredoJustica, String id_Classificador, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
			
		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==-1) &&
				(id_Serventia==null || id_Serventia.length()==0) &&
				(id_Comarca==null || id_Comarca.length()==0) &&
				(cpfCnpjParte==-1) &&
				(id_Classificador==null || id_Classificador.length()==0)	){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: o nome da parte, cpf/cnpj, número do processo, a serventia, a comarca ou classificador. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
			
		}
		boolean boUsaParte = false;
		if (processoNumero==-1 && (cpfCnpjParte > -1 || nomeParteSimplificado.length() > 0 )) {
			boUsaParte=true;
		}
		
		//Realiza SELECT para trazer processos de 2º grau e une com os recursos inominados
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DATA_RECEBIMENTO, p.DIGITO_VERIFICADOR, p.ANO, p.SEGREDO_JUSTICA, p.ID_SERV, p.SERV, p.PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.FORUM_CODIGO, p.ID_PROC_TIPO, p.PROC_TIPO  ";

		//Se a consulta utiliza filtro de parte, utiliza um view diferente para realizar a consulta
		stSqlCamposProcesso += ", p.PRIMEIRO_PROMOVENTE, p.PRIMEIRO_PROMOVIDO ";
		
		//Monta filtros para pesquisa
		String sqlCondicoes = "";
		
		if (boUsaParte) {
			stSqlCamposProcesso += ", p.NOME, p.proc_parte_tipo, p.id_proc_parte  ";			
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE p ";
			 if (cpfCnpjParte > -1) {			
					sqlCondicoes += " ( p.CPF = ?";									ps.adicionarLong(cpfCnpjParte);
					sqlCondicoes += " OR p.CNPJ = ? )";								ps.adicionarLong(cpfCnpjParte);
					
				}else if (nomeParteSimplificado.length() > 0) {
					if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
						sqlCondicoes += " p.NOME_SIMPLIFICADO = '" + nomeParteSimplificado + "'";
					} else {
						sqlCondicoes += " p.NOME_SIMPLIFICADO like '" + nomeParteSimplificado +"%'";
					}
				}
				
		}else {
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_TODOS p ";
			if(processoNumero != -1){			
				sqlCondicoes +=  " p.PROC_NUMERO  = ?";  						ps.adicionarLong(processoNumero);
			 		
				if (digito != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.DIGITO_VERIFICADOR  = ?";				ps.adicionarLong(digito);
				}
				
				if (ano != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.ANO  = ?";								ps.adicionarLong(ano);
				}				
			}
		}
						
		//if (sqlCondicoes.length() > 0) sqlCondicoes = "(" + sqlCondicoes + ")"; //Concatena Sql's

		if (statusCodigo != null && statusCodigo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") + " p.PROC_STATUS_CODIGO = ?";
			ps.adicionarLong(statusCodigo);
		}
		
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") + " p.ID_PROC_TIPO = ?";
			ps.adicionarLong(id_ProcessoTipo);
		}
		
		if (id_Serventia != null && id_Serventia.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") + " p.ID_SERV = ?";
			ps.adicionarLong(id_Serventia);
		}

		if (id_Comarca != null && id_Comarca.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") + " p.ID_COMARCA = ?";
			ps.adicionarLong(id_Comarca);
		}
		if (segredoJustica != null && segredoJustica.equals("false")){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") + " p.SEGREDO_JUSTICA = ?";
			ps.adicionarBoolean(segredoJustica);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") + " p.ID_CLASSIFICADOR = ?";
			ps.adicionarLong(id_Classificador);
		}
				
		try{
			sqlCondicoes = " WHERE " + sqlCondicoes;	
			sql = stSqlCamposProcesso + stSqlComumProcesso + sqlCondicoes;			
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sql, ps, posicao);
			//listaProcessos = getListaProcessos(rs1, false);
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				if (boUsaParte) {
					processoDt.setNomeParteBusca(rs1.getString("NOME"));
					processoDt.setParteTipoBusca(rs1.getString("proc_parte_tipo"));
					processoDt.setId_ParteTipoBusca(rs1.getString("id_proc_parte"));
				}
				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso + sqlCondicoes;	

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	/**
	 * Método responsável em consultar os processos de acordo com parâmetros passados.
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * @param id_Comarca, identificação da comarca para filtrar os processos
	 * @param posicao parametro para paginação
	 * 
	 * @author lsbernardes
	 */
	public List consultarProcessosPossivelPrescricao(String id_Serventia, String id_Comarca, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		
		stSqlCamposProcesso = "SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.PROC_STATUS_CODIGO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.Serv, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
		stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC P WHERE p.DATA_PRESCRICAO  <= ? AND PROC_STATUS_CODIGO <> ? ";
		ps.adicionarDate(new Date());
		ps.adicionarLong(ProcessoStatusDt.ARQUIVADO);
		
		if (id_Serventia != null && id_Serventia.length() > 0){
			stSqlComumProcesso += " AND ID_SERV = ? ";
			ps.adicionarLong(id_Serventia);
		}
		
		if (id_Comarca != null && id_Comarca.length() > 0){
			stSqlComumProcesso += " AND ID_COMARCA = ? ";
			ps.adicionarLong(id_Comarca);
		}
		
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			sql = "SELECT COUNT(1) AS QUANTIDADE FROM (" + stSqlCamposProcesso + stSqlComumProcesso + ")";

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	/**
	 * Método responsável em consultar os processos de acordo com parâmetros passados.
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * 
	 * @author lsbernardes
	 */
	public List consultarProcessosRedistribuicaoLote(String id_Serventia, String id_ServentiaCargo, String id_ProcessoTipo, String arquivado, String id_Classificador, String posicao) throws Exception {
		String sql = "";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, PROC_STATUS_CODIGO, ANO, SEGREDO_JUSTICA, PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV ";
		sql += " FROM PROJUDI.VIEW_BUSCA_PROC p ";	
		sql += " INNER JOIN PROC_RESP pr on (p.ID_PROC = pr.ID_PROC) WHERE ID_SERV = ? "; ps.adicionarLong(id_Serventia);		
		
		if (id_ServentiaCargo != null && id_ServentiaCargo.length()>0){
			sql += " AND ID_SERV_CARGO = ? "; ps.adicionarLong(id_ServentiaCargo);
		}
		
		if (id_Classificador != null && id_Classificador.length()>0){
			sql += " AND ID_CLASSIFICADOR = ? "; ps.adicionarLong(id_Classificador);
		} 
		
		if (id_ProcessoTipo != null && id_ProcessoTipo.length()>0){
			sql += " AND p.ID_PROC_TIPO = ? "; ps.adicionarLong(id_ProcessoTipo);
		} 
		
		if (arquivado != null && arquivado.equals("true")){
			sql += " AND DATA_ARQUIVAMENTO IS NOT NULL ";
		} else {
			sql += " AND DATA_ARQUIVAMENTO IS NULL ";
		}
		
		try{
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sql, ps, posicao, "500");
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(1) AS QUANTIDADE FROM (" + sql +")";

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}

		return listaProcessos;
	}

	/**
	 * Método que consulta uma lista de processos para redistribuição em lote.
	 * @param id_Serventia - id da servntia do processo
	 * @param id_ServentiaCargo - id do serv cargo responsável pelo processo
	 * @param id_ProcessoTipo - id do tipo de processo
	 * @param arquivado - se serão considerados os processos arquivados
 	 * @param id_Classificador - id do classificador dos processos a serem localizados
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosRedistribuicaoLote(String id_Serventia, String id_ServentiaCargo, String id_ProcessoTipo, String arquivado, String id_Classificador) throws Exception {
		String sql = "";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, PROC_STATUS_CODIGO, ANO, SEGREDO_JUSTICA, PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV ";
		sql += " FROM PROJUDI.VIEW_BUSCA_PROC p ";	
		sql += " INNER JOIN PROC_RESP pr on (p.ID_PROC = pr.ID_PROC) WHERE ID_SERV = ? "; ps.adicionarLong(id_Serventia);		
		
		if (id_ServentiaCargo != null && id_ServentiaCargo.length()>0){
			sql += " AND ID_SERV_CARGO = ? "; ps.adicionarLong(id_ServentiaCargo);
		}
		
		if (id_Classificador != null && id_Classificador.length()>0){
			sql += " AND ID_CLASSIFICADOR = ? "; ps.adicionarLong(id_Classificador);
		} 
		
		if (id_ProcessoTipo != null && id_ProcessoTipo.length()>0){
			sql += " AND p.ID_PROC_TIPO = ? "; ps.adicionarLong(id_ProcessoTipo);
		} 
		
		if (arquivado != null && arquivado.equals("true")){
			sql += " AND DATA_ARQUIVAMENTO IS NOT NULL ";
		} else {
			sql += " AND DATA_ARQUIVAMENTO IS NULL ";
		}
		
		try{
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultar(sql, ps);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return listaProcessos;
	}
	
	/**
	 * Método responsável em consultar os processos que estarão disponíveis na consulta pública de acordo com parâmetros passados.
	 * 
	 * @param nomeParte, filtro de nome da parte
	 * @param cpfCnpjParte, filtro do cpf ou cnj da parte
	 * @param processoNumero, filtro do número do processo
	 * @param digito, dígito verificador do processo
	 * @param ano, ano do processo
	 * @param serventiaTipoCodigo, o tipo da serventia para saber se a consulta refere-se a 1º ou 2º grau
	 * @param pesquisarNomeParteExato, boolean que determina se a busca do nome da parte deve ser ou não exato
	 * @param posicao parametro para paginação
	 * 
	 * @author msapaula, tamaralsantos
	 */
	public List consultarProcessosPublica(String nomeParte, long cpfCnpjParte, int processoNumero, int digito, int ano, String pesquisarNomeExato, String posicao) throws Exception {
		String stSqlComumProcesso = "";
		String stSqlCamposProcesso = "";		
		String sqlCondicoes = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		List listaProcessos = new ArrayList();		
		String nomeParteSimplificado = ""; 
		if (nomeParte != null && !nomeParte.equals("%")) {			
			nomeParteSimplificado=Funcoes.converteNomeSimplificado(nomeParte);
		}
				
		if (pesquisarNomeExato==null) pesquisarNomeExato="";
						
		if(( nomeParteSimplificado.length()==0) && ( processoNumero==-1) &&	( cpfCnpjParte==-1)  ){			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: o nome da parte, cpf/cnpj, número do processo. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");			
		}
		
		boolean boUsaParte = false;
		if ((processoNumero == -1) && ( cpfCnpjParte > -1 || nomeParteSimplificado.length() > 0 )) {
			boUsaParte=true;
		}
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, SEGREDO_JUSTICA, SERV, PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
				
		if (boUsaParte) {
			stSqlCamposProcesso += ", p.NOME, p.PROC_PARTE_TIPO_CODIGO, p.proc_parte_tipo, p.id_proc_parte  ";			
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE p ";
			 if (cpfCnpjParte > -1) {			
					sqlCondicoes += " ( p.CPF = ?";													ps.adicionarLong(cpfCnpjParte);
					sqlCondicoes += " OR p.CNPJ = ? )";												ps.adicionarLong(cpfCnpjParte);					
			}else if (nomeParteSimplificado.length() > 0) {
				
				if ( pesquisarNomeExato.equals("true")) {
					sqlCondicoes += " p.NOME_SIMPLIFICADO = '" + nomeParteSimplificado + "'";
				} else {
					sqlCondicoes += " p.NOME_SIMPLIFICADO like '" + nomeParteSimplificado +"%'";   
				}
			}
			sqlCondicoes += " AND PROC_STATUS_CODIGO <> ?"; 										ps.adicionarLong(ProcessoStatusDt.ARQUIVADO);				
		}else {
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_TODOS p ";
			if(processoNumero != -1){	
				sqlCondicoes +=  " p.PROC_NUMERO  = ?";  											ps.adicionarLong(processoNumero);
			 		
				if (digito != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.DIGITO_VERIFICADOR  = ?";									ps.adicionarLong(digito);
				}
				
				if (ano != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.ANO  = ?";													ps.adicionarLong(ano);
				}				
			}
		}		
				
		if (sqlCondicoes.length() > 0) sqlCondicoes = "(" + sqlCondicoes + ")"; //Concatena Sql's						
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += " WHERE " + sqlCondicoes;		
				
		String sqlOrder = " ORDER BY p.PROC_PRIOR_ORDEM, p.DATA_RECEBIMENTO";
		stSqlCamposProcesso +=  stSqlComumProcesso + sqlOrder;
		
		try{
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(stSqlCamposProcesso, ps, posicao);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				if (boUsaParte) {
					processoDt.setParteTipoCodigoBusca(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					processoDt.setNomeParteBusca(rs1.getString("NOME"));					
					processoDt.setParteTipoBusca(rs1.getString("proc_parte_tipo"));
					processoDt.setId_ParteTipoBusca(rs1.getString("id_proc_parte"));
				}
				listaProcessos.add(processoDt);
			}

			//Conta registros
			stSqlCamposProcesso = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE " + stSqlComumProcesso;
			rs2 = consultar(stSqlCamposProcesso, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
					
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}

	
	/**
	 * Método responsável em consultar os processo de acordo com parâmetros passados.
	 * 
	 * @param processoNumero, filtro do número do processo
	 * @param digito, dígito verificador do processo
	 * @param ano, ano do processo
	 * 
	 * @author lsbernardes
	 */
	public Boolean verificarExistenciaProcesso(String processoNumero, String digito, String ano, boolean desconsideraExecPen) throws Exception {
		String sqlComum = "";
		String sql = "";
		boolean retorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sqlComum = " FROM PROJUDI.PROC p ";
		
		if (desconsideraExecPen) {
			sqlComum += " INNER JOIN PROJUDI.SERV s ON s.ID_SERV = p.ID_SERV ";
		}
		
		if (processoNumero != null && processoNumero.length() > 0 && digito != null && digito.length() > 0 && ano != null && ano.length() > 0) {
			sqlComum +=  "  WHERE p.PROC_NUMERO  = ? AND p.DIGITO_VERIFICADOR  = ? AND  p.ANO  = ? ";
			ps.adicionarLong(processoNumero);
			ps.adicionarLong(digito);
			ps.adicionarLong(ano);
		} else
			throw new MensagemException("É necessário informar o número, dígito e ano do processo.");
		
		if (desconsideraExecPen) {
			sqlComum += " AND ID_PROC_TIPO != ? ";
			ps.adicionarLong(ProcessoTipoDt.ID_CALCULO_LIQUIDACAO_PENA);
		}

		sql = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR " + sqlComum;
		
		try{
			//Efetua consulta
			rs1 = consultar(sql, ps);

			//Essa primeira consulta retorna somente os processos
			retorno = rs1.next();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return retorno;
	}

	/**
	 * Método responsável em consultar os processos de uma turma recursal ou câmara cível (2º grau) de acordo com parâmetros passados.
	 * Nesse método são retornados somente os processos ativos (retirando os recursos inominados
	 * que não foram autuados).
	 * 
	 * @param nomeParte, filtro de nome da parte
	 * @param pesquisarNomeExato, determina se a pesquisa de nome parte será por nome exato ou não
	 * @param cpfCnpjParte, filtro do cpf ou cnj da parte
	 * @param processoNumero, filtro do número do processo
	 * @param statusCodigo, filtro de status do processo
	 * @param id_ProcessoTipo, filtro de tipo de processo
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * @param id_Comarca, identificação da comarca para filtrar os processos
	 * @param SegredoJustica, define se devem ser retornados os processos com segredo de justiça
	 * @param id_ServentiaCargo, identificação do cargo responsável por processo a ser filtrado
	 * @param posicao parametro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosSegundoGrau(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, int processoNumero, int digito, int ano, String statusCodigo, String id_ProcessoTipo, String id_Serventia, String id_Comarca, String SegredoJustica, String id_ServentiaCargoResponsavel, String id_Classificador, String posicao) throws Exception {
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";		
		String sqlCondicoes = "";
		
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
		
		boolean boUsaParte = false;
		if ((processoNumero==-1) && (cpfCnpjParte > -1 || nomeParteSimplificado.length() > 0 )) {
			boUsaParte=true;
		}
		
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, SERV, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";		
							
		if (boUsaParte) {
			stSqlCamposProcesso += ", p.NOME, p.proc_parte_tipo, p.id_proc_parte  ";			
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE p ";
			stSqlComumProcesso += " INNER JOIN PROJUDI.PROC_RESP pr on (p.ID_PROC = pr.ID_PROC)";
			 if (cpfCnpjParte > -1) {			
					sqlCondicoes += " ( p.CPF = ?";									ps.adicionarLong(cpfCnpjParte);
					sqlCondicoes += " OR p.CNPJ = ? )";								ps.adicionarLong(cpfCnpjParte);
					
				}else if (nomeParteSimplificado.length() > 0) {
					if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
						sqlCondicoes += " p.NOME_SIMPLIFICADO = '" + nomeParteSimplificado + "'";
					} else {
						sqlCondicoes += " p.NOME_SIMPLIFICADO like '" + nomeParteSimplificado +"%'";   
					}					
				}
				
		}else {
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_TODOS p ";
			stSqlComumProcesso += " INNER JOIN PROJUDI.PROC_RESP pr on (p.ID_PROC = pr.ID_PROC)";
			if (processoNumero != -1) {			
				sqlCondicoes +=  " p.PROC_NUMERO  = ?";  						ps.adicionarLong(processoNumero);
			 		
				if (digito != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.DIGITO_VERIFICADOR  = ?";				ps.adicionarLong(digito);
				}
				
				if (ano != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.ANO  = ?";								ps.adicionarLong(ano);
				}				
			}
		}
				
//		if (id_ServentiaCargoResponsavel != null && id_ServentiaCargoResponsavel.length() > 0) {
//			stSqlComumProcesso += " INNER JOIN PROJUDI.PROC_RESP pr on (p.ID_PROC = pr.ID_PROC";
//			stSqlComumProcesso += " AND pr.ID_SERV_CARGO = ? ";						ps.adicionarLong(id_ServentiaCargoResponsavel);
//			stSqlComumProcesso += " AND pr.CODIGO_TEMP = ?) ";						ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
//		}
		
		if (id_ServentiaCargoResponsavel != null && id_ServentiaCargoResponsavel.length() > 0) {
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "")  ;
			sqlCondicoes += " ( pr.ID_SERV_CARGO = ? ";						ps.adicionarLong(id_ServentiaCargoResponsavel);
			sqlCondicoes += " AND pr.CODIGO_TEMP = ?) ";						ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		}
		
		if (statusCodigo != null && statusCodigo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "")  ;
			sqlCondicoes += " PROC_STATUS_CODIGO = ?";  			ps.adicionarLong(statusCodigo);
		}
		
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "")  ;			
			sqlCondicoes += " ID_PROC_TIPO = ?";		 		ps.adicionarLong(id_ProcessoTipo);
		}
		
		if (id_Serventia != null && id_Serventia.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes +=  " ID_SERV = ?";						ps.adicionarLong(id_Serventia);
		}
		
		if (id_Comarca != null && id_Comarca.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes +=  " ID_COMARCA = ?";					ps.adicionarLong(id_Comarca);
		}
		if (SegredoJustica != null && SegredoJustica.equals("false")){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes +=  " SEGREDO_JUSTICA = ?";				ps.adicionarBoolean(SegredoJustica);
		}
		
		if (id_Classificador != null && id_Classificador.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes += " ID_CLASSIFICADOR = ?";		 		ps.adicionarLong(id_Classificador);
		}
		
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += " WHERE " + sqlCondicoes;
		
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sql, ps, posicao);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				
				if (boUsaParte) {
					processoDt.setNomeParteBusca(rs1.getString("NOME"));
					processoDt.setParteTipoBusca(rs1.getString("proc_parte_tipo"));
					processoDt.setId_ParteTipoBusca(rs1.getString("id_proc_parte"));
				}

				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	/**
	 * Método responsável em consultar os processos de uma turma recursal ou câmara cível (2º grau) de acordo com parâmetros passados.
	 * Nesse método são retornados somente os processos ativos (retirando os recursos inominados
	 * que não foram autuados).
	 * 
	 * @param nomeParte, filtro de nome da parte
	 * @param pesquisarNomeExato, determina se a pesquisa de nome parte será por nome exato ou não
	 * @param cpfCnpjParte, filtro do cpf ou cnj da parte
	 * @param processoNumero, filtro do número do processo
	 * @param statusCodigo, filtro de status do processo
	 * @param id_ProcessoTipo, filtro de tipo de processo
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * @param id_Comarca, identificação da comarca para filtrar os processos
	 * @param SegredoJustica, define se devem ser retornados os processos com segredo de justiça
	 * @param listServentiaCargoResponsavel, identificação do cargo responsável por processo a ser filtrado
	 * @param posicao parametro para paginação
	 * 
	 * @author msapaula
	 * @author jrcorrea 04/06/2018
	 */
	public List consultarTodosProcessosGabinete(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, int processoNumero, int digito, int ano, String statusCodigo, String id_ProcessoTipo, String id_Serventia, String id_Comarca, String SegredoJustica,  String id_Classificador, String posicao) throws Exception {
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";		
		String sqlCondicoes = "";
		
		String sql = "";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
		
		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==-1) &&
				(id_Serventia==null || id_Serventia.length()==0) &&
				(statusCodigo==null || statusCodigo.length()==0) &&
				(cpfCnpjParte==-1) 
			){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: número do processo, o nome da parte, cpf/cnpj, ou serventia. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
			
		}

		//Realiza SELECT para trazer processos de 2º grau e une com os recursos inominados
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.PRIMEIRO_PROMOVENTE, p.PRIMEIRO_PROMOVIDO, p.SERV ";
		stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC p ";
		
		stSqlComumProcesso += " INNER JOIN PROJUDI.PROC_RESP pr on p.ID_PROC = pr.ID_PROC AND pr.CODIGO_TEMP = ?  ";			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		
		stSqlComumProcesso += "  INNER JOIN Serv_Cargo Sc on pr.id_serv_cargo = sc.id_serv_cargo "; 
		stSqlComumProcesso += "      Inner Join Cargo_Tipo C On Sc.Id_Cargo_Tipo = C.Id_Cargo_Tipo ";
		stSqlComumProcesso += "      Inner Join Usu_Serv_Grupo Usg On Sc.Id_Usu_Serv_Grupo = Usg.Id_Usu_Serv_Grupo ";
		stSqlComumProcesso += "      Inner Join Usu_Serv Us On Usg.Id_Usu_Serv =Us.Id_Usu_Serv ";
		stSqlComumProcesso += "      Inner Join Usu U On Us.Id_Usu = U.Id_Usu ";
		stSqlComumProcesso += "      inner join serv s on us.id_serv = s.id_serv ";
		stSqlComumProcesso += " where sc.id_serv = ? and c.cargo_tipo_codigo in (?,?) "; 		ps.adicionarLong(id_Serventia);			ps.adicionarLong(CargoTipoDt.DESEMBARGADOR);	ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);
		stSqlComumProcesso += " and exists (select 1 from serv_relacionada sr where sr.ID_SERV_PRINC = p.id_Serv and sr.ID_SERV_REL = sc.id_serv )";
		
		//Monta filtros para pesquisa
		if(processoNumero != -1){
			
			sqlCondicoes += " AND PROC_NUMERO = ?  "; 			ps.adicionarLong(processoNumero);
			
			if (digito != -1){	
				sqlCondicoes += " AND  DIGITO_VERIFICADOR  = ?  "; ps.adicionarLong(digito);
			}
			if (ano != -1){		
				sqlCondicoes += " AND  ANO  = ?  ";			 ps.adicionarLong(ano);
			}
		}else 	if (cpfCnpjParte!=-1) {
				
			sqlCondicoes += " AND  (CPF = ? ";				ps.adicionarLong(cpfCnpjParte);
			sqlCondicoes += "   OR CNPJ = ? )";			ps.adicionarLong(cpfCnpjParte);				

		}else if (nomeParteSimplificado.length() > 0) {
				if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
					sqlCondicoes += " AND NOME_SIMPLIFICADO = '" +  nomeParteSimplificado +"'";
				} else {
					sqlCondicoes += " AND NOME_SIMPLIFICADO like '" + nomeParteSimplificado +"%'";				
				}
		}
		
		if (statusCodigo != null && statusCodigo.length() > 0){			
			sqlCondicoes += " AND PROC_STATUS_CODIGO = ?";  			ps.adicionarLong(statusCodigo);
		}
		
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){				
			sqlCondicoes += " AND ID_PROC_TIPO = ?";		 		ps.adicionarLong(id_ProcessoTipo);
		}
		
		if (SegredoJustica != null && SegredoJustica.equals("false")){			
			sqlCondicoes +=  " AND SEGREDO_JUSTICA = ?";				ps.adicionarBoolean(SegredoJustica);
		}
		
		if (id_Classificador != null && id_Classificador.length() > 0){			
			sqlCondicoes += " AND ID_CLASSIFICADOR = ?";		 		ps.adicionarLong(id_Classificador);
		}
 
		stSqlComumProcesso +=  sqlCondicoes;	

		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;

			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sql, ps, posicao);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {
			}
		}

		return listaProcessos;
	}
	

	/**
	 * Método responsável em consultar os processos de uma turma recursal ou câmara cível (2º grau) de acordo com parâmetros passados.
	 * Nesse método são retornados somente os processos ativos (retirando os recursos inominados
	 * que não foram autuados).
	 * 
	 * @param nomeParte, filtro de nome da parte
	 * @param pesquisarNomeExato, determina se a pesquisa de nome parte será por nome exato ou não
	 * @param cpfCnpjParte, filtro do cpf ou cnj da parte
	 * @param processoNumero, filtro do número do processo
	 * @param statusCodigo, filtro de status do processo
	 * @param id_ProcessoTipo, filtro de tipo de processo
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * @param id_Comarca, identificação da comarca para filtrar os processos
	 * @param SegredoJustica, define se devem ser retornados os processos com segredo de justiça
	 * @param id_ServentiaCargo, identificação do cargo responsável por processo a ser filtrado
	 * @param codigoCargoTipo, código do cargo do responsável por processo a ser filtrado
	 * @param posicao parametro para paginação
	 * 
	 * @author lsbernardes
	 */
	public List consultarProcessosGabineteSegundoGrau(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, int processoNumero, int digito, int ano, String statusCodigo, String id_ProcessoTipo, String id_Serventia, String id_Comarca, String SegredoJustica, String id_ServentiaCargoResponsavel,  String id_Classificador, int Retator, int cargoTipo, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";	
		String sqlCondicoes = "";
	
		String sql = "";
		
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
		
		if (	(statusCodigo==null || statusCodigo.length()==0)){
			throw new MensagemException("É necessário informar o Status do Processo.");
		}
		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==-1) &&
				(id_Serventia==null || id_Serventia.length()==0) &&			
				(cpfCnpjParte==-1) &&
				(id_ServentiaCargoResponsavel==null || id_ServentiaCargoResponsavel.length()==0)
			){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: número do processo, o nome da parte, cpf/cnpj, ou serventia. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
			
		}
		
		boolean boUsaParte = false;
		if (processoNumero==-1 && (cpfCnpjParte !=-1 || nomeParteSimplificado.length() > 0 )) {
			boUsaParte=true;
		}
		
		//Realiza SELECT para trazer processos de 2º grau e une com os recursos inominados
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.PROC_STATUS_CODIGO, p.ANO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV ";		
		stSqlComumProcesso += " FROM [NOME_TABELA] p ";
		stSqlComumProcesso += " INNER JOIN PROJUDI.PROC_RESP pr on (p.ID_PROC = pr.ID_PROC";
		stSqlComumProcesso += " AND pr.ID_SERV_CARGO = ? AND pr.CODIGO_TEMP = ?)";										ps.adicionarLong(id_ServentiaCargoResponsavel);			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		stSqlComumProcesso += " INNER JOIN PROJUDI.SERV_CARGO sc ON sc.ID_SERV_CARGO = pr.ID_SERV_CARGO ";
		stSqlComumProcesso += " JOIN PROJUDI.CARGO_TIPO ct on ( pr.ID_CARGO_TIPO = ct.ID_CARGO_TIPO ) ";

		//Pesquisa do gabinete sempre será necessário o tipo de cargo 
		sqlCondicoes = " WHERE ct.CARGO_TIPO_CODIGO = ? ";																ps.adicionarLong(cargoTipo) ;
		
		if (boUsaParte) {
			stSqlCamposProcesso += ", p.NOME, p.proc_parte_tipo, p.id_proc_parte  ";			
			stSqlComumProcesso = stSqlComumProcesso.replace("[NOME_TABELA]", "PROJUDI.VIEW_BUSCA_PROC_PARTE");
			sqlCondicoes += " AND ";
			 if (cpfCnpjParte !=-1) {			
					sqlCondicoes += " ( p.CPF = ?";									ps.adicionarLong(cpfCnpjParte);
					sqlCondicoes += " OR p.CNPJ = ? )";								ps.adicionarLong(cpfCnpjParte);
					
				}else if (nomeParteSimplificado.length() > 0) {
					if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
						sqlCondicoes += " p.NOME_SIMPLIFICADO = '" + nomeParteSimplificado + "'";
					} else {
						sqlCondicoes += " p.NOME_SIMPLIFICADO like '" + nomeParteSimplificado +"%'";   
					}					
				}
				
		} else {
			stSqlComumProcesso = stSqlComumProcesso.replace("[NOME_TABELA]", "PROJUDI.VIEW_BUSCA_PROC_TODOS");	
			if(processoNumero != -1){	
				sqlCondicoes +=  " AND p.PROC_NUMERO  = ?";  						ps.adicionarLong(processoNumero);
			 		
				if (digito != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.DIGITO_VERIFICADOR  = ?";				ps.adicionarLong(digito);
				}
				
				if (ano != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.ANO  = ?";								ps.adicionarLong(ano);
				}				
			}
		}				
		
		//Se estiver listando processos dos assistentes, deve-se ignorar a coluna redator pois essa coluna só é
		//usada para desembargadores. Na desabilitação, a coluna redator é alterada e dá impacto na consulta de assistentes.
		if(cargoTipo != CargoTipoDt.ASSISTENTE_GABINETE && cargoTipo != CargoTipoDt.ASSISTENTE_GABINETE_FLUXO) {
			sqlCondicoes += " AND  pr.redator = ? ";																		ps.adicionarLong(Retator);
		}
		sqlCondicoes += " AND  exists (select 1 from serv_relacionada sr where sr.ID_SERV_PRINC = p.id_Serv and sr.ID_SERV_REL = sc.id_serv )";
					
		if (statusCodigo != null && statusCodigo.length() > 0){
			sqlCondicoes += " AND PROC_STATUS_CODIGO = ?";  																ps.adicionarLong(statusCodigo);
		}
		
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){
			sqlCondicoes += " AND ID_PROC_TIPO = ?";		 																ps.adicionarLong(id_ProcessoTipo);
		}
		
		if (id_Serventia != null && id_Serventia.length() > 0){
			sqlCondicoes += " AND p.id_Serv = ?";		 																	ps.adicionarLong(id_Serventia);
		}
		
		if (id_Classificador != null && id_Classificador.length() > 0){
			sqlCondicoes += " AND ID_CLASSIFICADOR = ?";		 															ps.adicionarLong(id_Classificador);
		}
				
					
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += sqlCondicoes ;
		
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			//Efetua consulta e chama método para consultar as partes de cada processo		
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				
				if (boUsaParte) {
					processoDt.setNomeParteBusca(rs1.getString("NOME"));
					processoDt.setParteTipoBusca(rs1.getString("proc_parte_tipo"));
					processoDt.setId_ParteTipoBusca(rs1.getString("id_proc_parte"));
				}

				listaProcessos.add(processoDt);
			}
			
			// Conta registros
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}

	/**
	 * Este método é uma cópia do método consultarProcessosTurmaRecursal, entretanto não há limitação (LIMIT, OFFSET) da consulta nem cálculo de
	 * quantidade de registros encontrados pois a tela que obrigou a criação do método não usa paginação. Método responsável em consultar os 
	 * processos da turma recursal de acordo com parâmetros passados. Nesse método são retornados somente os processos ativos (retirando os 
	 * recursos inominados que não foram autuados).
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * @param id_ServentiaCargo, identificação do cargo responsável por processo a ser filtrado
	 * @param id_ProcessoTipo - ID do tipo de processo 
	 * 
	 * @author hmgodinho
	 * @author jrcorrea 10/05/2017
	 */
	public List consultarProcessosRelator( String id_Serventia, String id_ServentiaCargoResponsavel, String id_ProcessoTipo) throws Exception{
		String sql = "";
		
		List listaProcessos = new ArrayList();
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		//Realiza SELECT para trazer processos de 2º grau e une com os recursos inominados
		sql = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.PRIMEIRO_PROMOVENTE, p.PRIMEIRO_PROMOVIDO, p.SERV ";
		sql += " FROM PROJUDI.VIEW_BUSCA_PROC_REC_RELATOR p ";				
		sql += " WHERE p.ID_SERV = ?";						ps.adicionarLong(id_Serventia);
		sql += " AND p.ID_SERV_CARGO = ?";					ps.adicionarLong(id_ServentiaCargoResponsavel);
		if(id_ProcessoTipo != null && !id_ProcessoTipo.equals("")){
			sql += "AND p.ID_PROC_TIPO = ?";				
			ps.adicionarLong(id_ProcessoTipo);
		}
		sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO ";

		try{
			rs1 = consultar(sql, ps);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	public List consultarProcessosDefensorPublicoProcuradorAdvogado( String idUsuarioServentia) throws Exception{
		String sql = "";
		
		List listaProcessos = new ArrayList();
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql = "select distinct p.id_proc, p.proc_numero, p.digito_verificador, vp.nome_parte, vp.proc_parte_tipo, p.data_recebimento ";
		sql += " FROM view_proc_parte_advogado vp ";	
		sql += " inner join proc p on p.id_proc = vp.id_proc ";
		sql += " WHERE vp.id_usu_serv_advogado = ?";	ps.adicionarLong(idUsuarioServentia);
		sql += " 	and p.data_arquivamento is null ";
		sql += " 	and vp.data_Saida is null ";
		sql += " ORDER BY data_recebimento ";
		
		try{
			rs1 = consultar(sql, ps);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setPoloAtivo(rs1.getString("nome_parte"));
				processoDt.setOutraDescricaoGenerica(rs1.getString("proc_parte_tipo"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));

				listaProcessos.add(processoDt);
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	/**
	 * Retorna todos os processos de um advogado (id_UsuarioServentia passado como parâmetro)
	 * inclusive aqueles que sejam Segredo de Justiça.
	 * Ordena por prioridade e data (decrescente)
	 * @param id_UsuarioServentia para advogado
	 * @param id_ServentiaUsuario para COORDENADOR_DEFENSORIA_PUBLICA, COORDENADOR_PROCURADORIA
	 * @author msapaula, @tamaralsantos
	 */
	public List consultarProcessosAdvogado(String id_UsuarioServentia, String id_ServentiaUsuario, String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, int processoNumero, int digito, int ano, String statusCodigo, String id_Serventia, String id_ServentiaSubTipo, String posicao, String quantidadeRegistrosPagina) throws Exception{
		String stSqlCampos = "";
		String stSqlComumProcesso = "";		
		String sqlCondicoes = "";
		String sql = "";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
		
		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==-1) &&
				(id_Serventia==null || id_Serventia.length()==0) &&
				(id_ServentiaSubTipo==null || id_ServentiaSubTipo.length()==0) &&
				(id_UsuarioServentia==null || id_UsuarioServentia.length()==0) &&
				(id_ServentiaUsuario==null || id_ServentiaUsuario.length()==0) &&				
				(cpfCnpjParte!=-1) 
			){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: número do processo, o nome da parte, cpf/cnpj, ou serventia. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
		}
		
		stSqlCampos = " SELECT distinct ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, FORUM_CODIGO, SEGREDO_JUSTICA, PROC_PRIOR_CODIGO, PROC_PRIOR_ORDEM, SERV, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, ID_PROC_TIPO  ";
		
		//Consulta de processos	
		stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_ADVOGADO p";
		
		/*
		 * id_usu_serv para advogados
		 * id_serv_usu para coordenador defensoria publica, COORDENADOR_PROCURADORIA, escritorio Juridico
		 */
		if(id_UsuarioServentia!=null && !id_UsuarioServentia.isEmpty()) {
			stSqlComumProcesso += " WHERE p.ID_USU_SERV = ?";					ps.adicionarLong(id_UsuarioServentia);
		} else {
			stSqlComumProcesso += " WHERE p.ID_SERV_USU = ?";					ps.adicionarLong(id_ServentiaUsuario);
		}
		
		stSqlComumProcesso += " AND p.DATA_SAIDA IS NULL";
		
		//Prepara filtros para consulta do processo
		if(processoNumero != -1){	
			
			sqlCondicoes += "  p.PROC_NUMERO = ?  "; 			ps.adicionarLong(processoNumero);
			
			if (digito != -1){
				sqlCondicoes += " AND  p.DIGITO_VERIFICADOR  = ?  "; ps.adicionarLong(digito);
			}
			if (ano != -1){		
				sqlCondicoes += " AND  p.ANO  = ?  ";			 ps.adicionarLong(ano);
			}
		}else 	if (cpfCnpjParte!=-1) {
				
			sqlCondicoes += "  (p.CPF = ? ";					ps.adicionarLong(cpfCnpjParte);
			sqlCondicoes += "   OR p.CNPJ = ? )";			ps.adicionarLong(cpfCnpjParte);				

		}else if (nomeParteSimplificado.length() > 0) {
				if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
					sqlCondicoes += " p.NOME_SIMPLIFICADO = '" + nomeParteSimplificado + "'";
				} else {
					sqlCondicoes += " p.NOME_SIMPLIFICADO like '"+ nomeParteSimplificado +"%'";				
				}				
		}
		
		if (sqlCondicoes.length() > 0) sqlCondicoes = "(" + sqlCondicoes + ")"; //Concatena Sql's

		if (statusCodigo != null && statusCodigo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "")  ;
			sqlCondicoes += " p.PROC_STATUS_CODIGO = ?";  			ps.adicionarLong(statusCodigo);
		}
		
		if (id_Serventia != null && id_Serventia.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes +=  " p.ID_SERV = ?";						ps.adicionarLong(id_Serventia);
		}
		if (id_ServentiaSubTipo != null && id_ServentiaSubTipo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes +=  " p.ID_SERV_SUBTIPO = ?"; 				ps.adicionarLong(id_ServentiaSubTipo);
		}
		
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += " AND " + sqlCondicoes;
		
		try{
			
			//Busca registros
			sql = stSqlCampos;
			sql += stSqlComumProcesso;
			sql += " ORDER BY p.PROC_PRIOR_ORDEM , p.DATA_RECEBIMENTO";
			
			//Consulta os processos e chama método para consultar as partes de cada um
			rs1 = consultarPaginacao(sql, ps, posicao, quantidadeRegistrosPagina);
			
			ProcessoDt processoDt;
			
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				
				ProcessoParteDt primeiroPromovente 	= ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE"));
				ProcessoParteDt primeiroPromovido 	= ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));	
		
				if(primeiroPromovente!=null)
					processoDt.addListaPoloAtivo(primeiroPromovente);
				
				if(primeiroPromovido!=null)
				processoDt.addListaPolosPassivos(primeiroPromovido);
				
				/*
				 * processoDt.setNomeParteBusca(rs1.getString("NOME"));
				 * processoDt.setParteTipoBusca(rs1.getString("PROC_PARTE_TIPO"));
				 * processoDt.setId_ParteTipoBusca(rs1.getString("ID_PROC_PARTE"));
				 */

				listaProcessos.add(processoDt);
				
			}

			//Conta registros			
			rs2 = consultar("SELECT COUNT(1) AS QUANTIDADE FROM (" + sql + ") TAB", ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();

		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}

	
	public List consultarProcessosDelegacia(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, int processoNumero, int digito, int ano, String tcoNumero, String id_Serventia, String id_Classificador, String posicao) throws Exception{
		String stSqlComumProcesso = "";
		String stSqlCamposProcesso = "";		
		String sqlCondicoes = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		List listaProcessos = new ArrayList();		
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);

		if(	(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
			(processoNumero==-1) &&
			(id_Serventia==null || id_Serventia.length()==0) && 
			(tcoNumero==null || tcoNumero.length()==0) &&
			(cpfCnpjParte==-1) 
		){
		
		throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: número do processo, número do TCO, o nome da parte, cpf/cnpj, ou serventia. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
		
		}
		
		boolean boUsaParte = false;
		if (processoNumero==-1 && (cpfCnpjParte !=-1 || nomeParteSimplificado.length() > 0 )) {
			boUsaParte=true;
		}
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, FORUM_CODIGO, SEGREDO_JUSTICA, ID_SERV, SERV, PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM, p.PROC_PRIOR_CODIGO, ID_PROC_TIPO, PROC_TIPO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
				
		if (boUsaParte) {
			stSqlCamposProcesso += ", p.NOME, p.proc_parte_tipo, p.id_proc_parte  ";			
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE p ";
			 if (cpfCnpjParte !=-1) {			
					sqlCondicoes += " ( p.CPF = ?";									ps.adicionarLong(cpfCnpjParte);
					sqlCondicoes += " OR p.CNPJ = ? )";								ps.adicionarLong(cpfCnpjParte);
					
				}else if (nomeParteSimplificado.length() > 0) {
					if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
						sqlCondicoes += " p.NOME_SIMPLIFICADO = '" + nomeParteSimplificado + "'";
					} else {
						sqlCondicoes += " p.NOME_SIMPLIFICADO like '" + nomeParteSimplificado +"%'";   
					}
					sqlCondicoes += " AND PROC_STATUS_CODIGO <> ?"; 			ps.adicionarLong(ProcessoStatusDt.ARQUIVADO);
				}
				
		}else {
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_TODOS p ";
			if(processoNumero != -1){		
				sqlCondicoes +=  " p.PROC_NUMERO  = ?";  						ps.adicionarLong(processoNumero);
			 		
				if (digito != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.DIGITO_VERIFICADOR  = ?";				ps.adicionarLong(digito);
				}
				
				if (ano != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.ANO  = ?";								ps.adicionarLong(ano);
				}				
			} else if (tcoNumero != null && tcoNumero.length() > 0){
				sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
				sqlCondicoes +=  " TCO_NUMERO  = ?";  						ps.adicionarString(tcoNumero);
			}
		}
		
		if (id_Serventia != null && id_Serventia.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") + " p.ID_SERV_ORIGEM = ? ";
			ps.adicionarLong(id_Serventia);
		}

		if (id_Classificador != null && id_Classificador.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") + " p.ID_CLASSIFICADOR = ? ";
			ps.adicionarLong(id_Classificador);
		}
		
		sqlCondicoes  += (sqlCondicoes.length() > 0 ? " AND " : "") + " p.PROC_STATUS_CODIGO = ? ";  ps.adicionarLong(ProcessoStatusDt.ATIVO);
		
		if (sqlCondicoes.length() > 0) sqlCondicoes = "(" + sqlCondicoes + ")"; //Concatena Sql's
		
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += " WHERE " + sqlCondicoes;
				
		String sqlOrder = " ORDER BY p.PROC_PRIOR_ORDEM, p.DATA_RECEBIMENTO";
		stSqlCamposProcesso +=  stSqlComumProcesso + sqlOrder;
		
		try{
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(stSqlCamposProcesso, ps, posicao);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				if (boUsaParte) {
					processoDt.setNomeParteBusca(rs1.getString("NOME"));
					processoDt.setParteTipoBusca(rs1.getString("proc_parte_tipo"));
					processoDt.setId_ParteTipoBusca(rs1.getString("id_proc_parte"));
				}
				listaProcessos.add(processoDt);
			}

			//Conta registros
			stSqlCamposProcesso = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE " + stSqlComumProcesso;
			rs2 = consultar(stSqlCamposProcesso, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}

	/**
	 * Consulta os processos que um promotor pode visualizar, ou seja, aqueles onde ele é responsável
	 * 
	 * @param nomeParte filtro de nome da parte
	 * @param cpfCnpjParte filtro do cpf ou cnj da parte
	 * @param processoNumero filtro do número do processo
	 * @param id_ServentiaCargo serventiaCargo do promotor
	 * @param posicao parametro para paginação
	 * 
	 * @author msapaula, tamaralsantos
	 */
	public List consultarProcessosPromotor(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, int processoNumero, int digito, int ano, String id_ServentiaCargo, String statusCodigo, String id_Serventia, String id_ServentiaUsuario, String posicao) throws Exception{
		String Sql;
		String SqlFrom;
		String SqlOrder;		
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ProcessoDt processoDt;
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
		
		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==-1) &&
				(id_Serventia==null || id_Serventia.length()==0) &&
				(cpfCnpjParte==-1)  &&
				(id_ServentiaUsuario==null || id_ServentiaUsuario.length()==0) &&
				(id_ServentiaCargo==null || id_ServentiaCargo.length()==0) 
		  ){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: o nome da parte, cpf/cnpj, número do processo, a serventia, a comarca ou classificador. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
			
		}
		
		boolean boUsaParte = false;
		if (processoNumero==-1 && (cpfCnpjParte !=-1  || nomeParteSimplificado.length() > 0 )) {
			boUsaParte=true;
		}
		
		Sql = " SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DATA_RECEBIMENTO, p.PROC_PRIOR_CODIGO, p.PROC_PRIOR_ORDEM, p.DIGITO_VERIFICADOR, p.ANO, p.FORUM_CODIGO, p.SEGREDO_JUSTICA, p.PRIMEIRO_PROMOVENTE, p.PRIMEIRO_PROMOVIDO, p.ID_SERV, p.SERV, p.ID_PROC_TIPO ";			
		
		SqlFrom = " FROM [TABELA] p ";
		SqlFrom += " JOIN Proc_Resp Pr  ON Pr.Id_Proc       = P.Id_Proc  AND Pr.Codigo_Temp <> ?"; 									ps.adicionarLong(-1);
		SqlFrom += " JOIN SERV_CARGO SC  ON PR.ID_SERV_CARGO = SC.ID_SERV_CARGO ";
		SqlFrom += " JOIN SERV S1  ON SC.ID_SERV = S1.ID_SERV ";
		SqlFrom += " JOIN SERV_TIPO ST  ON S1.ID_SERV_TIPO      = ST.ID_SERV_TIPO  AND ST.SERV_TIPO_CODIGO = ?  ";					ps.adicionarLong(ServentiaTipoDt.PROMOTORIA);  
		
		if(id_ServentiaCargo!=null && !id_ServentiaCargo.isEmpty()){
			SqlFrom += " WHERE sc.ID_SERV_CARGO = ?";   			ps.adicionarLong(id_ServentiaCargo);
		} else{
			SqlFrom += " WHERE s1.ID_SERV = ?";   			ps.adicionarLong(id_ServentiaUsuario);
		}
		
		if (boUsaParte) {
			Sql += ", p.NOME, p.proc_parte_tipo, p.id_proc_parte  ";	
			SqlFrom = SqlFrom.replace("[TABELA]", "PROJUDI.VIEW_BUSCA_PROC_PARTE");
			SqlFrom += " AND ";
			 if (cpfCnpjParte !=-1) {			
				 SqlFrom += " ( p.CPF = ?";																							ps.adicionarLong(cpfCnpjParte);
					SqlFrom += " OR p.CNPJ = ? )";																					ps.adicionarLong(cpfCnpjParte);
					
				}else if (nomeParteSimplificado.length() > 0) {
					if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
						SqlFrom += " p.NOME_SIMPLIFICADO = '" + nomeParteSimplificado + "'";
					} else {
						SqlFrom += " p.NOME_SIMPLIFICADO like '" + nomeParteSimplificado +"%'";   
					}					
				}
				
		}else {			
			SqlFrom = SqlFrom.replace("[TABELA]", "PROJUDI.VIEW_BUSCA_PROC_TODOS");
			if(processoNumero != -1){		
				SqlFrom +=  " AND p.PROC_NUMERO  = ?";  																			ps.adicionarLong(processoNumero);
			 		
				if (digito != -1){
					SqlFrom += (SqlFrom.length() > 0 ? " AND " : "");
					SqlFrom += " p.DIGITO_VERIFICADOR  = ?";																		ps.adicionarLong(digito);
				}
				
				if (ano != -1){
					SqlFrom += (SqlFrom.length() > 0 ? " AND " : "");
					SqlFrom += " p.ANO  = ?";																						ps.adicionarLong(ano);
				}				
			}
		}
																														
		if (statusCodigo != null && statusCodigo.length() > 0){
			SqlFrom += (SqlFrom.length() > 0 ? " AND " : "")  ;
			SqlFrom += " p.PROC_STATUS_CODIGO = ?";  																				ps.adicionarLong(statusCodigo);
		}
		
		if (id_Serventia != null && id_Serventia.length() > 0){
			SqlFrom += (SqlFrom.length() > 0 ? " AND " : "") ;
			SqlFrom +=  " p.ID_SERV = ?";																							ps.adicionarLong(id_Serventia);
		}
			
		try{

			SqlOrder = " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			
			while (rs1.next()) {
				//Criando objeto do tipo processo
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
								
				ProcessoParteDt primeiroPromoventeRecorrente = null;
				ProcessoParteDt primeiroPromovidoRecorrido = null;				
		
				primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE"));
				primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));

                if (primeiroPromoventeRecorrente != null) processoDt.addListaPoloAtivo(primeiroPromoventeRecorrente);                
                if (primeiroPromovidoRecorrido != null) processoDt.addListaPolosPassivos(primeiroPromovidoRecorrido);
				
                listaProcessos.add(processoDt);
			}
	
			//Conta registros 
			Sql = "SELECT COUNT(DISTINCT p.ID_PROC) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}

	
	/* Não será mais utilizado, usará a consulta de promotor
	 * @tamaralsantos
	 * 
	public List consultarProcessosPromotoria(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String statusCodigo, String id_Promotoria, String posicao) throws Exception {
		return consultarProcessosPromotoria(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, digito, ano, statusCodigo, id_Promotoria, null, posicao);
	}
	
	public List consultarProcessosPromotoria(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String statusCodigo, String id_Promotoria, String id_Classificador, String posicao) throws Exception {
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String stSqlCamposRecurso = "";
		String stSqlComumRecurso = "";		
		String sql = "";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, ID_RECURSO, SEGREDO_JUSTICA, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV  ";

		////Se a consulta utiliza filtro de parte, utiliza um view diferente para realizar a consulta
		if (nomeParte.length() > 0 || cpfCnpjParte.length() > 0) {
			stSqlCamposProcesso += ", ID_PROC_PARTE, NOME, PROC_PARTE_TIPO_CODIGO ";
			stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE_PROM_P p ";
		}
		else {
			stSqlCamposProcesso += ", NULL AS ID_PROC_PARTE, NULL AS NOME, NULL AS PROC_PARTE_TIPO_CODIGO ";			
			stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_PROMOTORIA_P p";
		}
		
		//Prepara filtros para consulta
		String sqlCondicoes = this.filtroProcessoPromotoria(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, digito, ano, null, statusCodigo, null, id_Promotoria, null, null, null, id_Classificador, ps);
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += " WHERE " + sqlCondicoes;
		
		stSqlCamposRecurso = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, ID_RECURSO, SEGREDO_JUSTICA, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV  ";

		////Se a consulta utiliza filtro de parte, utiliza um view diferente para realizar a consulta
		if (nomeParte.length() > 0 || cpfCnpjParte.length() > 0) {
			stSqlCamposRecurso += ", ID_PROC_PARTE, NOME, PROC_PARTE_TIPO_CODIGO ";
			stSqlComumRecurso = " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE_PROM_R p ";
		}
		else {
			stSqlCamposRecurso += ", NULL AS ID_PROC_PARTE, NULL AS NOME, NULL AS PROC_PARTE_TIPO_CODIGO ";			
			stSqlComumRecurso = " FROM PROJUDI.VIEW_BUSCA_PROC_PROMOTORIA_R p";
		}
		
		//Prepara filtros para consulta
		sqlCondicoes = this.filtroProcessoPromotoria(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, digito, ano, null, statusCodigo, null, id_Promotoria, null, null, null, id_Classificador, ps);
		if (sqlCondicoes.length() > 0) stSqlComumRecurso += " WHERE " + sqlCondicoes;

		
		try{
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " UNION ";
			sql += stSqlCamposRecurso;
			sql += stSqlComumRecurso;
			sql += " ORDER BY PROC_PRIOR_ORDEM ,DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			listaProcessos = getListaProcessos(rs1, false);
			
			//Conta registros 
			sql = "SELECT (SELECT COUNT(distinct p.ID_PROC) ";
			sql += stSqlComumProcesso;
			sql += ") + (SELECT COUNT(distinct p.ID_PROC) ";
			sql += stSqlComumRecurso;
			sql += ") AS QUANTIDADE FROM DUAL";		
			
			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}
	*/
	
	/* Não será mais utilizado, usará a consulta de advogado
	 * @tamaralsantos
	 * 
	public List consultarProcessosProcuradoria(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String statusCodigo, String id_Promotoria, String posicao) throws Exception {
		return consultarProcessosProcuradoria(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, digito, ano, statusCodigo, id_Promotoria, null, posicao);
	}

	public List consultarProcessosProcuradoria(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String statusCodigo, String id_Promotoria, String id_Classificador, String posicao) throws Exception {
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String stSqlCamposRecurso = "";
		String stSqlComumRecurso = "";		
		String sql = "";	
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO(); 
		
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, ID_RECURSO, SEGREDO_JUSTICA, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV ";

		////Se a consulta utiliza filtro de parte, utiliza um view diferente para realizar a consulta
		if (nomeParte.length() > 0 || cpfCnpjParte.length() > 0){
			stSqlCamposProcesso += ", ID_PROC_PARTE, NOME, PROC_PARTE_TIPO_CODIGO ";
			stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE_PROC_P p ";
		} else {
			stSqlCamposProcesso += ", NULL AS ID_PROC_PARTE, NULL AS NOME, NULL AS PROC_PARTE_TIPO_CODIGO ";
			stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_PROCURADORIA_P p";
		}	

		//Prepara filtros para consulta
		String sqlCondicoes = this.filtroProcessoProcuradoria(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, digito, ano, null, statusCodigo, null, id_Promotoria, null, null, null, id_Classificador, ps);
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += " WHERE " + sqlCondicoes;

		
		stSqlCamposRecurso = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, ID_RECURSO, SEGREDO_JUSTICA, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV ";

		////Se a consulta utiliza filtro de parte, utiliza um view diferente para realizar a consulta
		if (nomeParte.length() > 0 || cpfCnpjParte.length() > 0){
			stSqlCamposRecurso += ", ID_PROC_PARTE, NOME, PROC_PARTE_TIPO_CODIGO ";
			stSqlComumRecurso = " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE_PROC_R p ";
		} else {
			stSqlCamposRecurso += ", NULL AS ID_PROC_PARTE, NULL AS NOME, NULL AS PROC_PARTE_TIPO_CODIGO ";
			stSqlComumRecurso = " FROM PROJUDI.VIEW_BUSCA_PROC_PROCURADORIA_R p";
		}	

		//Prepara filtros para consulta
		sqlCondicoes = this.filtroProcessoProcuradoria(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, digito, ano, null, statusCodigo, null, id_Promotoria, null, null, null, id_Classificador, ps);
		if (sqlCondicoes.length() > 0) stSqlComumRecurso += " WHERE " + sqlCondicoes;
		
		try{
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " UNION ";
			sql += stSqlCamposRecurso;
			sql += stSqlComumRecurso;
			sql += " ORDER BY PROC_PRIOR_ORDEM ,DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			listaProcessos = getListaProcessos(rs1, false);
			
			//Conta registros 
			sql = "SELECT (SELECT COUNT(distinct p.ID_PROC) ";
			sql += stSqlComumProcesso;
			sql += ") + (SELECT COUNT(distinct p.ID_PROC) ";
			sql += stSqlComumRecurso;
			sql += ") AS QUANTIDADE FROM DUAL";
			
			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}
	*/

	/**
	 * Consulta os processos onde um ServentiaCargo é responsável.
	 * Foi necessário passar como parâmetro o UsuarioNe para permitir a geração do código HASH
	 * 
	 * @param usuarioNe, usuario logado
	 * @param posicao parametro para paginação
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarProcessosServentiaCargo(UsuarioNe usuarioNe, String posicao) throws Exception{
		String sqlCamposProcesso = "";
		String sqlComumProcesso = "";
		String sql = "";
		ProcessoDt processoDt;
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sqlCamposProcesso = "SELECT distinct p.ID_PROC, PROC_NUMERO, SERV, DIGITO_VERIFICADOR, ANO, DATA_RECEBIMENTO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, SEGREDO_JUSTICA ";
		sqlCamposProcesso += ", PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
						
		sqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_PROMOTOR p";
		sqlComumProcesso += " WHERE ID_SERV_CARGO = ?";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_ServentiaCargo());	
		
		try{
			
			sql = sqlCamposProcesso + sqlComumProcesso;	
			sql += " ORDER BY PROC_PRIOR_ORDEM ,DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			
			//listaProcessos = getListaProcessosComHash(rs1, usuarioNe, consultaTodasPartes);
			while (rs1.next()) {
				//Criando objeto do tipo processo
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
								
				ProcessoParteDt primeiroPromoventeRecorrente = null;
				ProcessoParteDt primeiroPromovidoRecorrido = null;				
		
				primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE"));
				primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));

                if (primeiroPromoventeRecorrente != null) processoDt.addListaPoloAtivo(primeiroPromoventeRecorrente);                
                if (primeiroPromovidoRecorrido != null) processoDt.addListaPolosPassivos(primeiroPromovidoRecorrido);

                processoDt.setHash(usuarioNe.getCodigoHash(processoDt.getId()));
                
                listaProcessos.add(processoDt);
			}
			
			//Conta registros			
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += sqlComumProcesso;			
			
			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}

	/**
	 * Consulta os processos onde um Advogado é responsável.
	 * Foi necessário passar como parâmetro o UsuarioNe para permitir a geração do código HASH
	 * 
	 * @param usuarioNe, usuario logado
	 * @param posicao parametro para paginação
	 * 
	 * @author msapaula, tamaralsantos
	 */
	/* SERÁ UTILIZADO SÓ O DO WEBSERVICE. FAZIA A MESMA COISA.
	public List consultarProcessosAdvogado(UsuarioNe usuarioNe, String posicao) throws Exception {
		String stSqlCampos = "";
		String stSqlComumProcesso = "";		
		String sql = "";		
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSqlCampos = "SELECT distinct p.ID_PROC, PROC_NUMERO, SERV, DIGITO_VERIFICADOR, ANO, DATA_RECEBIMENTO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, SEGREDO_JUSTICA, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
				
		stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_ADVOGADO p";
		stSqlComumProcesso += " WHERE p.ID_USU_SERV = ?";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		stSqlComumProcesso += " AND p.DATA_SAIDA IS NULL";

		
		try{
			
			//Busca registros
			sql = stSqlCampos;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM ,DATA_RECEBIMENTO";
			
			//Consulta os processos e chama método para consultar as partes de cada um
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			//List listaProcesso = getListaProcessos(rs1, consultaTodasPartes);
			ProcessoDt processoDt;
			
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				
				ProcessoParteDt primeiroPromovente 	= ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE"));
				ProcessoParteDt primeiroPromovido 	= ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));		
		
				if(primeiroPromovente!=null)
					processoDt.addListaPromoventes(primeiroPromovente);
				
				if(primeiroPromovido!=null)
				processoDt.addListaPromovidos(primeiroPromovido);

				//Seta o hash aqui.
				processoDt.setHash(usuarioNe.getCodigoHash(processoDt.getId()));
				
				listaProcessos.add(processoDt);
				
			}
			
			//Conta registros			
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;
			
			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));		
			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}
	*/

	/**
	 * Consulta os processos onde um ServentiaCargo é responsável.
	 * 
	 * @param id_ServentiaCargo, identificação do cargo
	 * 
	 * @author msapaula
	 */
	public int consultarProcessosServentiaCargo(String id_ServentiaCargo) throws Exception {
		String sql = "";
		int qtde = 0;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		try{
			// Processo
			sql = "SELECT (";

			sql += "SELECT COUNT(1) FROM PROJUDI.VIEW_BUSCA_PROC p";
			sql += " JOIN PROJUDI.PROC_RESP pr on p.ID_PROC = pr.ID_PROC";
			sql += " WHERE ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargo);
			sql += ") AS QUANTIDADE FROM DUAL";			

			rs1 = this.consultar(sql,ps);
			if (rs1.next()) qtde = rs1.getInt("QUANTIDADE");
			
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return qtde;
	}

	/**
	 * Consulta os processos que um juiz pode visualizar, ou seja, aqueles onde ele é responsável
	 * na serventia
	 * 
	 * @param nomeParte filtro de nome da parte
	 * @param pesquisarNomeParteExato, boolean que determina se a busca do nome da parte deve ser ou não exato
	 * @param cpfCnpjParte filtro do cpf ou cnj da parte
	 * @param processoNumero filtro do número do processo
	 * @param id_ProcessoTipo , filtro de tipo de processo
	 * @param id_ServentiaCargo serventiaCargo do juiz
	 * @param posicao parametro para paginação
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosJuiz(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, int processoNumero, int digito, int ano, String id_ServentiaCargo, String statusCodigo, String id_ProcessoTipo, String id_Serventia, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";
		
		String sqlCondicoes = "";
		String sql = "";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
		
		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==-1) &&
				(id_Serventia==null || id_Serventia.length()==0) &&
				(cpfCnpjParte==-1)  &&
				(id_ServentiaCargo==null || id_ServentiaCargo.length()==0)
			){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: número do processo, o nome da parte, cpf/cnpj, ou serventia. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
			
		}

		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, PROC_STATUS_CODIGO, ANO, SEGREDO_JUSTICA, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV  ";		
		stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC p ";			
		
		
		stSqlComumProcesso += " JOIN PROJUDI.PROC_RESP pr on p.ID_PROC = pr.ID_PROC";
		stSqlComumProcesso += " WHERE ID_SERV_CARGO = ?"; ps.adicionarLong(id_ServentiaCargo);
		stSqlComumProcesso += " AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(ProcessoResponsavelDt.ATIVO);

		//Prepara filtros para consulta
		if(processoNumero != -1){	
			
			sqlCondicoes += "  PROC_NUMERO = ?  "; 			ps.adicionarLong(processoNumero);
			
			if (digito != -1){		
				sqlCondicoes += " AND  DIGITO_VERIFICADOR  = ?  "; ps.adicionarLong(digito);
			}
			if (ano != -1){		
				sqlCondicoes += " AND  ANO  = ?  ";			 ps.adicionarLong(ano);
			}
		}else 	if (cpfCnpjParte!=-1) {
				
			sqlCondicoes += "  (CPF = ? ";					ps.adicionarLong(cpfCnpjParte);
			sqlCondicoes += "   OR CNPJ = ?) ";			ps.adicionarLong(cpfCnpjParte);				

		}else if (nomeParteSimplificado.length() > 0) {
				if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
					sqlCondicoes += " NOME_SIMPLIFICADO = '" + nomeParteSimplificado +"'";
				} else {
					sqlCondicoes += " NOME_SIMPLIFICADO like '" + nomeParteSimplificado +"%'";				
				}
		}
		
		if (statusCodigo != null && statusCodigo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "")  ;
			sqlCondicoes += " PROC_STATUS_CODIGO = ?";  			ps.adicionarLong(statusCodigo);
		}
		
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "")  ;			
			sqlCondicoes += " ID_PROC_TIPO = ?";		 		ps.adicionarLong(id_ProcessoTipo);
		}
		
		if (id_Serventia != null && id_Serventia.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes +=  " ID_SERV = ?";						ps.adicionarLong(id_Serventia);
		}
		
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += " AND " + sqlCondicoes;
				
		try{
			//Busca registros
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			
			sql += " ORDER BY PROC_PRIOR_ORDEM ,DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			//Conta registros			
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;
			
			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}
	
	/***
	 * 
	 * @param nomeParte
	 * @param pesquisarNomeExato
	 * @param cpfCnpjParte
	 * @param processoNumero
	 * @param digito
	 * @param ano
	 * @param tcoNumero
	 * @param statusCodigo
	 * @param id_ProcessoTipo
	 * @param id_Serventia
	 * @param id_ServentiaSubTipo
	 * @param id_Comarca
	 * @param segredoJustica
	 * @param id_Classificador
	 * @param ps
	 * @return
	 * @throws Exception
	 * @author jrcorrea 26/05/2015 alteracao
	 * @throws Exception 
	 */
	
//	private String filtroProcesso2(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String tcoNumero, String statusCodigo, String id_ProcessoTipo, String id_Serventia, String id_ServentiaSubTipo, String id_Comarca, String segredoJustica, String id_Classificador, PreparedStatementTJGO ps) throws Exception{
//
//		String sqlFiltro = "";
//		
//		if(		(nomeParte==null || nomeParte.length()==0) && 
//				(processoNumero==null || processoNumero.length()==0) &&
//				(cpfCnpjParte==-1)  &&
//				(id_Serventia==null || id_Serventia.length()==0) &&
//				(id_Comarca==null || id_Comarca.length()==0) &&
//				(id_Classificador==null || id_Classificador.length()==0)	){
//			
//			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: o nome da parte, cpf/cnpj, número do processo, a serventia, a comarca ou classificador ");
//			
//		}
//
//		//Monta Sql dec Condições
//		
//		if (processoNumero != null && processoNumero.trim().length() > 0){
//			sqlFiltro +=  " PROC_NUMERO  = ?";							ps.adicionarLong(processoNumero);
//		
//			if (digito != null && digito.trim().length() > 0){
//				sqlFiltro +=  " AND DIGITO_VERIFICADOR  = ?";				ps.adicionarLong(digito);
//			}
//			if (ano != null && ano.trim().length() > 0){
//				sqlFiltro += " AND ANO  = ?";								ps.adicionarLong(ano);
//			}
//		}else if (tcoNumero != null && tcoNumero.trim().length() > 0){
//			sqlFiltro +=  " TCO_NUMERO  = ?";							ps.adicionarString(tcoNumero);
//			
//		}else if (cpfCnpjParte!=null && cpfCnpjParte.trim().length() > 0) {		
//			sqlFiltro += " CPF = ?";									ps.adicionarLong(cpfCnpjParte);
//			sqlFiltro += " OR CNPJ = ?";								ps.adicionarLong(cpfCnpjParte);
//						
//			//Se pesquisarNomeExato == true, deve ser feita pesquisa por nome exato.
//		}else if (nomeParte!=null && nomeParte.trim().length() > 0) {
//			if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
//				sqlFiltro += " NOME_SIMPLIFICADO = ?";  ps.adicionarString( Funcoes.converteNomeSimplificado(nomeParte) );
//			} else {
//				sqlFiltro += " NOME_SIMPLIFICADO like ?";  ps.adicionarString( Funcoes.converteNomeSimplificado(nomeParte) +"%");				
//			}
//		}
//
//		if (sqlFiltro.length() > 0) sqlFiltro = "(" + sqlFiltro + ")"; //Concatena Sql's
//		
//		//Se a consulta utiliza filtro de parte, utiliza o filtro por tipo de parte
//		if ((nomeParte!=null &&  nomeParte.trim().length() > 0 )|| (cpfCnpjParte!=null && cpfCnpjParte.trim().length() > 0)){
//			if (sqlFiltro.length() > 0) sqlFiltro += " AND ";
//			sqlFiltro += " (PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ?)";
//			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
//			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
//			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
//			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
//		}
//
//		//na consulta publica por nome ou cpf não se mostra processos arquivados
//		if (nomeParte.trim().length() > 0 || cpfCnpjParte.trim().length() > 0 && (statusCodigo == null || statusCodigo.length() == 0)){
//			if (sqlFiltro.length() > 0) sqlFiltro += " AND ";
//			sqlFiltro += " PROC_STATUS_CODIGO <> ?"; 			ps.adicionarLong(ProcessoStatusDt.ARQUIVADO);
//		}
//	
//		if (statusCodigo != null && statusCodigo.length() > 0){
//			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " PROC_STATUS_CODIGO = ?";
//			ps.adicionarLong(statusCodigo);
//		}
//		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){
//			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_PROC_TIPO = ?";
//			ps.adicionarLong(id_ProcessoTipo);
//		}
//		if (id_Serventia != null && id_Serventia.length() > 0){
//			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_SERV = ?";
//			ps.adicionarLong(id_Serventia);
//		}
//		if (id_ServentiaSubTipo != null && id_ServentiaSubTipo.length() > 0){
//			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_SERV_SUBTIPO = ?";
//			ps.adicionarLong(id_ServentiaSubTipo);
//		}
//		if (id_Comarca != null && id_Comarca.length() > 0){
//			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_COMARCA = ?";
//			ps.adicionarLong(id_Comarca);
//		}
//		if (segredoJustica != null && segredoJustica.equals("false")) {
//			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " SEGREDO_JUSTICA = ?";
//			ps.adicionarBoolean(segredoJustica);
//		}
//		if (id_Classificador != null && id_Classificador.length() > 0){
//			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_CLASSIFICADOR = ?";
//			ps.adicionarLong(id_Classificador);
//		}
//
//		return sqlFiltro;
//	}

	/**
	 * 
	 * @param nomeParte
	 * @param pesquisarNomeExato
	 * @param cpfCnpjParte
	 * @param processoNumero
	 * @param digito
	 * @param ano
	 * @param tcoNumero
	 * @param statusCodigo
	 * @param id_ProcessoTipo
	 * @param id_Promotoria
	 * @param id_ServentiaSubTipo
	 * @param id_Comarca
	 * @param segredoJustica
	 * @param id_Classificador
	 * @param ps
	 * @return
	 * @throws Exception
	 * @author jrcorrea alterado 26/05/2015
	 * @throws Exception 
	 */
	private String filtroProcessoPromotoria(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String tcoNumero, String statusCodigo, String id_ProcessoTipo, String id_Promotoria, String id_ServentiaSubTipo, String id_Comarca, String segredoJustica, String id_Classificador, PreparedStatementTJGO ps) throws Exception{

		String sqlFiltro = "";
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
		
		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==null || processoNumero.length()==0) &&
				(tcoNumero==null || tcoNumero.length()==0) &&
				(id_Comarca==null || id_Comarca.length()==0) &&
				(cpfCnpjParte==-1)  &&
				(id_Promotoria==null || id_Promotoria.length()==0) &&
				(id_Classificador==null || id_Classificador.length()==0)	){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: o nome da parte, cpf/cnpj, número do processo, o número do tco, promotoria, a comarca ou classificador. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
		}

		//Monta Sql dec Condições
		
		if (processoNumero != null && processoNumero.length() > 0){
			sqlFiltro +=  " PROC_NUMERO  = ?";							ps.adicionarLong(processoNumero);
		
			if (digito != null && digito.length() > 0){
				sqlFiltro +=  " AND DIGITO_VERIFICADOR  = ?";				ps.adicionarLong(digito);
			}
			if (ano != null && ano.length() > 0){
				sqlFiltro += " AND ANO  = ?";								ps.adicionarLong(ano);
			}
		}else if (tcoNumero != null && tcoNumero.length() > 0){
			sqlFiltro +=  " TCO_NUMERO  = ?";							ps.adicionarString(tcoNumero);		
		}else if (cpfCnpjParte !=-1) {		
			sqlFiltro += " CPF = ?";									ps.adicionarLong(cpfCnpjParte);
			sqlFiltro += " OR CNPJ = ?";								ps.adicionarLong(cpfCnpjParte);
						
			
			//Se pesquisarNomeExato == true, deve ser feita pesquisa por nome exato.
		}else if (nomeParteSimplificado.length() > 0) {
			if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
				sqlFiltro += " NOME_SIMPLIFICADO = ?";  ps.adicionarString(nomeParteSimplificado);
			} else {
				sqlFiltro += " NOME_SIMPLIFICADO like ?";  ps.adicionarString(nomeParteSimplificado +"%");				
			}
		}

		if (sqlFiltro.length() > 0) sqlFiltro = "(" + sqlFiltro + ")"; //Concatena Sql's
		
		//Se a consulta utiliza filtro de parte, utiliza o filtro por tipo de parte
		if ((nomeParteSimplificado!=null && nomeParteSimplificado.length() > 0 )|| (cpfCnpjParte!=-1)){
			if (sqlFiltro.length() > 0) sqlFiltro += " AND ";
			sqlFiltro += " (PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ?)";
			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		}
			
		if (statusCodigo != null && statusCodigo.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " PROC_STATUS_CODIGO = ?";
			ps.adicionarLong(statusCodigo);
		}
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_PROC_TIPO = ?";
			ps.adicionarLong(id_ProcessoTipo);
		}
		if (id_Promotoria != null && id_Promotoria.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_PROMOTORIA = ?";
			ps.adicionarLong(id_Promotoria);
		}
		if (id_ServentiaSubTipo != null && id_ServentiaSubTipo.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_SERV_SUBTIPO = ?";
			ps.adicionarLong(id_ServentiaSubTipo);
		}
		if (id_Comarca != null && id_Comarca.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_COMARCA = ?";
			ps.adicionarLong(id_Comarca);
		}
		if (segredoJustica != null && segredoJustica.equals("false")) {
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " SEGREDO_JUSTICA = ?";
			ps.adicionarBoolean(segredoJustica);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_CLASSIFICADOR = ?";
			ps.adicionarLong(id_Classificador);
		}

		return sqlFiltro;
	}
	/**
	 * 
	 * @param nomeParte
	 * @param pesquisarNomeExato
	 * @param cpfCnpjParte
	 * @param processoNumero
	 * @param digito
	 * @param ano
	 * @param tcoNumero
	 * @param statusCodigo
	 * @param id_ProcessoTipo
	 * @param id_Procuradoria
	 * @param id_ServentiaSubTipo
	 * @param id_Comarca
	 * @param segredoJustica
	 * @param id_Classificador
	 * @param ps
	 * @return string filtro para a consulta
	 * @throws Exception
	 * @author jrcorrea alteração 26/05/2015
	 */
	private String filtroProcessoProcuradoria(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String tcoNumero, String statusCodigo, String id_ProcessoTipo, String id_Procuradoria, String id_ServentiaSubTipo, String id_Comarca, String segredoJustica, String id_Classificador, PreparedStatementTJGO ps) throws Exception{

		String sqlFiltro = "";
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);

		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==null || processoNumero.length()==0) &&
				(tcoNumero==null || tcoNumero.length()==0) &&
				(id_Comarca==null || id_Comarca.length()==0) &&
				(cpfCnpjParte==-1)  &&
				(id_Procuradoria==null || id_Procuradoria.length()==0) &&
				(id_Classificador==null || id_Classificador.length()==0)	){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: o nome da parte, cpf/cnpj, número do processo, o número do tco, procuradaria, a comarca ou classificador. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
			
		}
		
		//Monta Sql dec Condições
		
		if (processoNumero != null && processoNumero.length() > 0){
			sqlFiltro +=  " PROC_NUMERO  = ?";							ps.adicionarLong(processoNumero);
		
			if (digito != null && digito.length() > 0){
				sqlFiltro +=  " AND DIGITO_VERIFICADOR  = ?";				ps.adicionarLong(digito);
			}
			if (ano != null && ano.length() > 0){
				sqlFiltro += " AND ANO  = ?";								ps.adicionarLong(ano);
			}
		}else if (tcoNumero != null && tcoNumero.length() > 0){
			sqlFiltro +=  " TCO_NUMERO  = ?";							ps.adicionarString(tcoNumero);	
		}else if (cpfCnpjParte !=-1) {		
			sqlFiltro += " CPF = ?";									ps.adicionarLong(cpfCnpjParte);
			sqlFiltro += " OR CNPJ = ?";								ps.adicionarLong(cpfCnpjParte);
									
			
			//Se pesquisarNomeExato == true, deve ser feita pesquisa por nome exato.
		}else if (nomeParteSimplificado.length() > 0) {
			if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
				sqlFiltro += " NOME_SIMPLIFICADO = ?";  ps.adicionarString(nomeParteSimplificado);
			} else {
				sqlFiltro += " NOME_SIMPLIFICADO like ?";  ps.adicionarString(nomeParteSimplificado +"%");				
			}
		}

		if (sqlFiltro.length() > 0) sqlFiltro = "(" + sqlFiltro + ")"; //Concatena Sql's
		
		//Se a consulta utiliza filtro de parte, utiliza o filtro por tipo de parte
		if ((nomeParteSimplificado!=null && nomeParteSimplificado.length() > 0 )|| (cpfCnpjParte!=-1)){
			if (sqlFiltro.length() > 0) sqlFiltro += " AND ";
			sqlFiltro += " (PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ?)";
			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		}
	
		if (statusCodigo != null && statusCodigo.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " PROC_STATUS_CODIGO = ?";
			ps.adicionarLong(statusCodigo);
		}
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_PROC_TIPO = ?";
			ps.adicionarLong(id_ProcessoTipo);
		}
		if (id_Procuradoria != null && id_Procuradoria.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_PROCURADORIA = ? ";
			ps.adicionarLong(id_Procuradoria);
		}
		if (id_ServentiaSubTipo != null && id_ServentiaSubTipo.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_SERV_SUBTIPO = ?";
			ps.adicionarLong(id_ServentiaSubTipo);
		}
		if (id_Comarca != null && id_Comarca.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_COMARCA = ?";
			ps.adicionarLong(id_Comarca);
		}
		if (segredoJustica != null && segredoJustica.equals("false")) {
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " SEGREDO_JUSTICA = ?";
			ps.adicionarBoolean(segredoJustica);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_CLASSIFICADOR = ?";
			ps.adicionarLong(id_Classificador);
		}

		return sqlFiltro;
	}
	
	/**
	 * Método responsável em realizar a consulta de dados básicos de um Processo de acordo com o número de processo passado. 
	 * Usado no cadastro de processo com dependência
	 * Left join pois a turma pode não ter area distribuicao
	 * @param processoNumero, número do processo
	 * @param digitoVerificador, dígito verificador do processo
	 * @author msapaula
	 */
	public ProcessoDt consultarProcessoNumero(String numero, String digitoVerificador, String ano, String forumCodigo) throws Exception {

		String sql;
		ProcessoDt Dados = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT p.id_proc, p.proc_numero, p.digito_verificador, p.ano, p.forum_codigo, processo_fisico_tipo"
		    + " FROM projudi.proc p"
		    + " WHERE proc_numero = ? AND digito_verificador  = ? AND p.ano = ? and p.forum_codigo = ?";
		 
		ps.adicionarLong(numero);
		ps.adicionarLong(digitoVerificador);
		ps.adicionarLong(ano);
		ps.adicionarLong(forumCodigo);

		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				Dados = new ProcessoDt();
				Dados.setId_Processo(rs1.getString("ID_PROC"));
				Dados.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				Dados.setAno(rs1.getString("ANO"));
				Dados.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				Dados.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				Dados.setProcessoFisicoTipo(rs1.getString("PROCESSO_FISICO_TIPO"));	
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}
	
	/**
	 * Método responsável por consultar a serventia origem de um recurso
	 * @param id_processo, id do processo
	 * @author lsbernardes
	 */
	public ServentiaDt consultarServentiaOrigemRecurso(String id_processo) throws Exception {

		String sql;
		ServentiaDt serventiaDt  = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT p.ID_PROC, r.ID_SERV_ORIGEM, s.SERV  FROM PROJUDI.VIEW_PROC_SIMPLES p ";
		sql += " LEFT JOIN PROJUDI.RECURSO r on r.ID_PROC=p.ID_PROC ";
		sql += " LEFT JOIN PROJUDI.SERV s on s.ID_SERV = r.ID_SERV_ORIGEM ";
		sql += " WHERE p.ID_PROC = ?"; 		ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(sql, ps);
			if (rs1.next()) {
				serventiaDt = new ServentiaDt();
				serventiaDt.setId(rs1.getString("ID_SERV_ORIGEM"));
				serventiaDt.setServentia(rs1.getString("SERV"));
			}
			//rs1.close();

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return serventiaDt;
	}

	/**
	 * Método que verifica se um determinado tco já está cadastrado para a serventia externa passada
	 * 
	 * @param id_Serventia, id da delegacia a verificar o tco
	 * @param tcoNumero, numero do tco a ser verificado
	 *
	 * @author msapaula
	 */
	public boolean verificarTcoCadastrado(String id_Serventia, String tcoNumero) throws Exception {
		String Sql;
		boolean boRetorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC WHERE ID_SERV_ORIGEM  = ?";
		ps.adicionarLong(id_Serventia);
		Sql += " AND TCO_NUMERO = ?";
		ps.adicionarString(tcoNumero);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) boRetorno = true;

			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return boRetorno;
	}

	/**
	 * Consulta quais os processos são apensos OU dependentes de um processo passado por parâmetro
	 * 
	 * @param String id_processo, id do processo
	 * @param boolean buscarApensos, true se for pra buscar apensos e false se for para buscar dependentes
	 * @return lista de processos apensos OU dependentes
	 * @author msapaula, hmgodinho
	 */
	public List consultarProcessosApensosDependentes(String id_processo, boolean buscarApensos) throws Exception {
		List listaProcessos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT ID_PROC, PROC_NUMERO, APENSO, DIGITO_VERIFICADOR, pt.PROC_TIPO, pt.ID_PROC_TIPO, ID_SERV ";
		sql += " FROM PROJUDI.PROC p INNER JOIN PROJUDI.PROC_TIPO pt on (p.ID_PROC_TIPO = pt.ID_PROC_TIPO) ";
		sql += " WHERE ID_PROC_DEPENDENTE = ? AND APENSO = ?"; 
		ps.adicionarLong(id_processo);
		ps.adicionarBoolean(buscarApensos);

		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (listaProcessos == null) listaProcessos = new ArrayList();
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				processoDt.setApenso(Funcoes.FormatarLogico(rs1.getString("APENSO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				listaProcessos.add(processoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	/**
	 * Consulta quais os processos são apensos E dependentes de um processo passado por parâmetro
	 * @param String id_processo, id do processo
	 * @return lista de processos apensos e dependentes
	 * @author hmgodinho
	 */
	public List consultarProcessosDependentes(String id_processo) throws Exception {
		List listaProcessos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT ID_PROC, PROC_NUMERO, APENSO, DIGITO_VERIFICADOR, pt.PROC_TIPO, pt.ID_PROC_TIPO, ID_SERV ";
		sql += " FROM PROJUDI.PROC p INNER JOIN PROJUDI.PROC_TIPO pt on (p.ID_PROC_TIPO = pt.ID_PROC_TIPO) ";
		sql += " WHERE ID_PROC_DEPENDENTE = ?"; 
		ps.adicionarLong(id_processo);

		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (listaProcessos == null) listaProcessos = new ArrayList();
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				processoDt.setApenso(Funcoes.FormatarLogico(rs1.getString("APENSO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				listaProcessos.add(processoDt);
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	/**
	 * Método que consulta um processo originário e retorna alguns parâmetros.
	 * 
	 * @param idProcessoDependente - id do processo filho
	 * @return processo consultado
	 * @throws Exception
	 * @author hmgodinho
	 */
	public ProcessoDt consultarProcessoApensoOriginario(String idProcessoDependente) throws Exception {
		ProcessoDt processoDt = new ProcessoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT ID_PROC, PROC_NUMERO, DIGITO_VERIFICADOR, APENSO, ID_PROC_DEPENDENTE ";
		sql += " FROM PROC p ";
		sql += " WHERE p.ID_PROC = ? ";
		ps.adicionarLong(idProcessoDependente);

		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setApenso(Funcoes.FormatarLogico(rs1.getString("APENSO")));
				processoDt.setId_ProcessoPrincipal(rs1.getString("ID_PROC_DEPENDENTE"));
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return processoDt;
	}
	
	/**
	 * Consulta os processos apensos passíveis de serem redistribuídos. 
	 * @param id_processo - ID do processo pai
	 * @return lista de processos apensos que podem ser redistribuídos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosApensosRedistribuicao(String id_processo) throws Exception {
		List listaProcessos = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT ID_PROC, PROC_NUMERO, APENSO, DIGITO_VERIFICADOR, pt.PROC_TIPO, pt.ID_PROC_TIPO, p.ID_SERV ";
		sql += " FROM PROJUDI.PROC p inner join PROJUDI.PROC_TIPO pt on (p.ID_PROC_TIPO = pt.ID_PROC_TIPO) ";
		sql += " WHERE ID_PROC_DEPENDENTE = ? AND APENSO = 1";  //processo apenso possui ID_PROC_DEPENDENTE e APENSO = 1
		ps.adicionarLong(id_processo);
		sql += " AND pt.PROC_TIPO_CODIGO NOT IN (?,?,?)";
		ps.adicionarLong(ProcessoTipoDt.CARTA_PRECATORIA);
		ps.adicionarLong(ProcessoTipoDt.CARTA_PRECATORIA_CPC);
		ps.adicionarLong(ProcessoTipoDt.CARTA_PRECATORIA_CPP);
		
		try{
			rs1 = this.consultar(sql, ps);
			while (rs1.next()) {
				if (listaProcessos == null) listaProcessos = new ArrayList();
				ProcessoDt processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
				processoDt.setApenso(Funcoes.FormatarLogico(rs1.getString("APENSO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				listaProcessos.add(processoDt);
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	/**
	 * Consulta quais os processos são apensos de um processo passado por parâmetro
	 * 
	 * @author msapaula
	 * @param String id_processo, id do processo
	 */
	public boolean verificaProcessosApensos(String id_processo) throws Exception {
		boolean temApensos = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		String sql = "SELECT ID_PROC FROM PROJUDI.PROC ";
		sql += " WHERE ID_PROC_DEPENDENTE = ?";
		ps.adicionarLong(id_processo);

		try{
			rs1 = this.consultar(sql, ps);
			if (rs1.next()) {
				if (rs1.getString("ID_PROC") != null && !rs1.getString("ID_PROC").equalsIgnoreCase("")) temApensos = true;
			}

			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return temApensos;
	}

	/**
	 * Registra o apensamento/desapensamento, criação/retirada de dependência de um processo
	 * @param id_Processo: processo a ser (des)apensado ou (in)dependente
	 * @param id_ProcessoPrincipal: processo q irá receber o apenso ou dependência
	 * @author hmgodinho
	 */
	public void registrarProcessoApensoDependente(String id_Processo, String id_ProcessoPrincipal, boolean apenso) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET ID_PROC_DEPENDENTE = ?";
		ps.adicionarLong(id_ProcessoPrincipal);
		Sql += ", APENSO = ?";
		ps.adicionarBoolean(apenso);
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

		executarUpdateDelete(Sql, ps);
		

	}

	/**
	 * Verifica se determinado processo já é apensado a algum outro processo
	 */
	public String verificaProcessoApensado(String id_Processo) throws Exception {
		String Sql;
		String stTemp = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			Sql = "SELECT APENSO FROM PROJUDI.PROC";
			Sql += " WHERE ID_PROC = ?";
			ps.adicionarLong(id_Processo);

			rs1 = this.consultar(Sql, ps);
			if (rs1.next()) stTemp = Funcoes.FormatarLogico(rs1.getString("APENSO"));

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return stTemp;
	}

	/**
	 * Altera o classificador de um processo
	 * 
	 * @param id_Processo identificação do processo
	 * @param id_Classificador novo classificador
	 */
	public void alterarClassificadorProcesso(String id_Processo, String id_Classificador) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET ID_CLASSIFICADOR = ?";													ps.adicionarLong(id_Classificador);		
		Sql += " WHERE ID_PROC = ? "; 																			ps.adicionarLong(id_Processo);
		
		//Permite que classifique o processo apenas se o classificador existir no banco, prevenindo
		//assim erros ao tentar concluir pré-análises com classificadores que já foram excluídos. A
		//condicional permite que o classificador seja limpo, gravando null ou vazio no id.
		if(id_Classificador != null && !id_Classificador.isEmpty()){
			Sql += " and exists (select 1 from classificador where id_classificador = ?) ";					ps.adicionarLong(id_Classificador);
		}

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Consulta a quantidade de processos ativos para um advogado agrupados pelo SubTipo da Serventia.
	 * Esse método é utilizado na consulta feita na página inicial do advogado.
	 * 
	 * @param id_UsuarioServentia identificação do advogado
	 * @author msapaula
	 */
	public List consultarQuantidadeAtivosAdvogado(String id_UsuarioServentia) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			
			stSql = "SELECT ID_SERV_SUBTIPO, SERV_SUBTIPO, COUNT(distinct p.ID_PROC) as QUANTIDADE ";
			stSql += " FROM PROJUDI.VIEW_BUSCA_PROC_ADVOGADO p";
			stSql += " WHERE p.ID_USU_SERV = ?";								ps.adicionarLong(id_UsuarioServentia);
			stSql += " AND p.PROC_STATUS_CODIGO = ?";							ps.adicionarLong(ProcessoStatusDt.ATIVO);
			stSql += " AND p.DATA_SAIDA IS NULL";
			stSql += " group by ID_SERV_SUBTIPO, SERV_SUBTIPO";
			
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				liTemp.add(new String[] {rs1.getString("ID_SERV_SUBTIPO"), rs1.getString("SERV_SUBTIPO"), rs1.getString("QUANTIDADE") });
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta a quantidade de processos ativos em uma serventia.
	 * Esse método é utilizado na consulta feita na página inicial dos usuários internos.
	 * 
	 * @param id_Serventia identificação da serventia
	 * @author msapaula
	 */
	public List consultarQuantidadeProcessosServentia(String id_Serventia) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			
			stSql = " Select ps.proc_status_codigo, Tab.Quantidade, ps.proc_status from ";
			stSql += "  (Select Count(1) As Quantidade, P.Id_Proc_Status ";
			stSql += "  from Proc P ";
			stSql += "   inner join proc_status ps on p.id_proc_status = ps.id_proc_status ";						
			stSql += " where Id_Serv = ? "; 									ps.adicionarLong(id_Serventia);
			stSql += "	  and  ps.proc_status_codigo  not in (?,?,?) ";			ps.adicionarLong(ProcessoStatusDt.SIGILOSO);	ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO); ps.adicionarLong(ProcessoStatusDt.CALCULO);
			stSql += "  group by P.Id_Proc_Status ) tab inner join proc_status ps on tab.id_proc_status = ps.id_proc_status ";

			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				
				liTemp.add(new String[] {rs1.getString("proc_status_codigo"), rs1.getString("proc_status"), rs1.getString("Quantidade") });
				//liTemp.add(new String[] {"0", "Ativos", rs1.getString("QUANTIDADE") });
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	/**
	 * Consulta a quantidade de processos com possível prescrição em uma serventia.
	 * Esse método é utilizado na consulta feita na página inicial dos usuários internos.
	 * 
	 * @param id_Serventia identificação da serventia
	 * @author lsbernardes
	 */
	public long consultarQuantidadePossiveisPrescritos(String id_Serventia, String id_Comarca) throws Exception {
		long loQuantidade= 0 ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			stSql =  "SELECT COUNT(1) AS QUANTIDADE FROM ";
			stSql += " (SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.PROC_STATUS_CODIGO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.Serv, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
			stSql += " FROM PROJUDI.VIEW_BUSCA_PROC P WHERE P.PROC_STATUS_CODIGO <> ? AND p.DATA_PRESCRICAO  <= ? ";  			
			ps.adicionarLong(ProcessoStatusDt.ARQUIVADO);
			ps.adicionarDate(new Date());
			
			if (id_Serventia != null && id_Serventia.length() > 0){
				stSql += " AND ID_SERV = ? ";
				ps.adicionarLong(id_Serventia);
			}
			
			if (id_Comarca != null && id_Comarca.length() > 0){
				stSql += " AND ID_COMARCA = ? ";
				ps.adicionarLong(id_Comarca);
			}
			
			stSql += ")";

			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				loQuantidade =  rs1.getLong("QUANTIDADE") ;
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return loQuantidade;
	}
	
	/**
	 * Consulta a quantidade de processos de cálculo em uma serventia.
	 * Esse método é utilizado na consulta feita na página inicial dos usuários internos.
	 * 
	 * @param id_Serventia identificação da serventia
	 * @author wcsilva
	 */
	public List consultarQuantidadeCalculoServentia(String id_Serventia) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			stSql = "SELECT COUNT(1) AS QUANTIDADE FROM PROJUDI.VIEW_PROC p";
			stSql += " WHERE p.ID_SERV = ?";			ps.adicionarLong(id_Serventia);
			stSql += " AND p.PROC_STATUS_CODIGO = ?";	ps.adicionarLong(ProcessoStatusDt.CALCULO);

			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				liTemp.add(new String[] {"0", "Calculo", rs1.getString("QUANTIDADE") });
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	public List consultarQuantidadeAtivosServentiaPromotoria(String id_Serventia) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{			
			stSql = "SELECT COUNT(distinct p.ID_PROC) as QUANTIDADE ";
			stSql += " FROM PROJUDI.VIEW_BUSCA_PROC_PROMOTOR p";
			stSql += " WHERE p.ID_SERV_USU = ? "; 					ps.adicionarLong(id_Serventia);
			stSql += " AND p.PROC_STATUS_CODIGO = ?"; 				ps.adicionarLong(ProcessoStatusDt.ATIVO);

			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				liTemp.add(new String[] {"0", "Ativos", rs1.getString("QUANTIDADE") });
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
	public List consultarQuantidadeAtivosServentiaProcuradoria(String id_Serventia) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
		
			stSql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE FROM PROJUDI.VIEW_BUSCA_PROC_PARTE p "; 
			stSql += " JOIN PROC_PARTE_ADVOGADO pa ON pa.id_proc_parte = p.id_proc_parte ";
			stSql += " JOIN USU_SERV us ON pa.id_usu_serv = us.id_usu_serv ";
			stSql += " WHERE us.ID_SERV = ? AND pa.DATA_SAIDA IS NULL AND  p.PROC_STATUS_CODIGO = ?";
			ps.adicionarLong(id_Serventia);
			ps.adicionarLong(ProcessoStatusDt.ATIVO);
			
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				liTemp.add(new String[] {"0", "Ativos", rs1.getString("QUANTIDADE") });
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {}
		}
		return liTemp;
	}

	/**
	 * Consulta a quantidade de processos ativos em uma turma recursal, ou câmara.
	 * Deve retornar somente os processos de 2º grau ativos e os recursos inominados que já foram autuados.
	 * Esse método é utilizado na consulta feita na página inicial dos usuários internos.
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @author msapaula
	 * @author jrcorrea
	 * 
	 */
//	public List consultarQuantidadeAtivosSegundoGrau(String id_Serventia, String id_ServentiaCargo, String codigoCargoTipo){
		
	public List consultarQuantidadeAtivosSegundoGrau(String id_Serventia) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		//String strServentiaCodigo="";
		ResultSetTJGO rs1 = null;
		//String strAtivo = "";
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
			stSql = "SELECT COUNT(1) as QUANTIDADE FROM PROJUDI.PROC p INNER JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS AND ps.PROC_STATUS_CODIGO = ? "; ps.adicionarLong(ProcessoStatusDt.ATIVO);				
			stSql += " WHERE  p.ID_SERV = ? ";	ps.adicionarLong(Funcoes.StringToInt(id_Serventia));
			
			//stSql += " GROUP BY p.Id_Serventia ";		      								
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				liTemp.add(new String[] {"0", "Ativos", rs1.getString("QUANTIDADE") });
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/* @author jrcorrea*/
	public List consultarQuantidadeAtivosDesembargador(String id_ServentiaCargoDesembargador) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		//String strServentiaCodigo="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
			
			
			stSql = 	"  select s.serv, s.id_serv, tab.Quantidade  from (";
			stSql += 	"	        Select Count(distinct P.Id_Proc) As Quantidade, p.id_serv";
			stSql += 	"	            From Projudi.Proc P ";
			stSql += 	"                   INNER JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS AND ps.PROC_STATUS_CODIGO = ? "; 												ps.adicionarLong(ProcessoStatusDt.ATIVO);			
			stSql += 	"	                Inner Join Projudi.Proc_Resp Pr On (P.Id_Proc = Pr.Id_Proc And Pr.Id_Serv_Cargo = ? And Pr.Codigo_Temp = ? AND pr.redator = 1)"; 						ps.adicionarLong(id_ServentiaCargoDesembargador); ps.adicionarLong(ProcessoResponsavelDt.ATIVO); 
			stSql += 	"	                Inner Join Projudi.Serv_Cargo Sc On Sc.Id_Serv_Cargo = Pr.Id_Serv_Cargo  ";
			stSql += 	"	                Join Projudi.Cargo_Tipo Ct On ( Pr.Id_Cargo_Tipo = Ct.Id_Cargo_Tipo   And Ct.Cargo_Tipo_Codigo = ? ) ";													ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
			stSql += 	"	                Where P.Data_Arquivamento Is Null and exists (select 1 from serv_relacionada sr where sr.ID_SERV_PRINC = p.id_Serv and sr.ID_SERV_REL = sc.id_serv )";
			stSql += 	"	                Group By P.Id_Serv ";
			stSql += 	"	      ) tab inner join serv s on s.id_Serv = tab.id_serv ";

			
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				liTemp.add(new String[] {"0", rs1.getString("serv"), rs1.getString("QUANTIDADE"), rs1.getString("id_serv")  });
			}
	//rs1.close();
    	
    	} finally{
    		try{
    			if (rs1 != null) rs1.close();
    		} catch(Exception e) {
    		}
    	}
		return liTemp;
	}
	
	public List consultarQuantidadeAtivosDistribuidor(String id_Serventia) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		//String strServentiaCodigo="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
					
			stSql = " SELECT COUNT(DISTINCT( p.id_proc)) as QUANTIDADE FROM PROJUDI.SERV_CARGO sc "; 
			stSql += "	INNER JOIN PROJUDI.PROC_RESP pr on sc.ID_SERV_CARGO = pr.ID_SERV_CARGO ";
			stSql += "	INNER JOIN PROJUDI.PROC p on pr.ID_PROC=p.ID_PROC";
			stSql += "  INNER JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS AND ps.PROC_STATUS_CODIGO = ? "; ps.adicionarLong(ProcessoStatusDt.ATIVO);
			stSql += "  INNER JOIN PROJUDI.CARGO_TIPO ct on sc.ID_CARGO_TIPO = ct.ID_CARGO_TIPO";		
			stSql += " where sc.id_serv = ? and ct.cargo_tipo_codigo in (?,?) AND pr.CODIGO_TEMP = ? "; 		ps.adicionarLong(id_Serventia);			ps.adicionarLong(CargoTipoDt.DESEMBARGADOR); ps.adicionarLong(CargoTipoDt.JUIZ_UPJ);	ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
			stSql += " and exists (select 1 from serv_relacionada sr where sr.ID_SERV_PRINC = p.id_Serv and sr.ID_SERV_REL = sc.id_serv )";
		        
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				liTemp.add(new String[] {"0", "Ativos", rs1.getString("QUANTIDADE") });
			}
	//rs1.close();
	
	} finally{
		try{
			if (rs1 != null) rs1.close();
		} catch(Exception e) {
		}
	}
		return liTemp;
	}
	
	/* author jrcorrea*/
	public List consultarQuantidadeAtivosAssistenteGabinete(String id_ServentiaCargoAssistente) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		//String strServentiaCodigo="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{								
		        
			stSql = 	"  select s.serv, s.id_serv, tab.Quantidade  from (";
			stSql += 	"	        Select Count(distinct P.Id_Proc) As Quantidade, p.id_serv";
			stSql += 	"	            From Projudi.Proc P ";
			stSql += 	"                   INNER JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS AND ps.PROC_STATUS_CODIGO = ? "; 							ps.adicionarLong(ProcessoStatusDt.ATIVO);			
			stSql += 	"	                Inner Join Projudi.Proc_Resp Pr On (P.Id_Proc = Pr.Id_Proc And Pr.Id_Serv_Cargo = ? And Pr.Codigo_Temp = ? )"; 						ps.adicionarLong(id_ServentiaCargoAssistente); ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
			stSql += 	"	                Inner Join Projudi.Serv_Cargo Sc On Sc.Id_Serv_Cargo = Pr.Id_Serv_Cargo  ";
			stSql += 	"	                Join Projudi.Cargo_Tipo Ct On  Pr.Id_Cargo_Tipo = Ct.Id_Cargo_Tipo   And (Ct.Cargo_Tipo_Codigo = ? or Ct.Cargo_Tipo_Codigo = ? )  ";		ps.adicionarLong(CargoTipoDt.ASSISTENTE_GABINETE);  ps.adicionarLong(CargoTipoDt.ASSISTENTE_GABINETE_FLUXO);
			stSql += 	"	                Where P.Data_Arquivamento Is Null and exists (select 1 from serv_relacionada sr where sr.ID_SERV_PRINC = p.id_Serv and sr.ID_SERV_REL = sc.id_serv )";
			stSql += 	"	                Group By P.Id_Serv ";
			stSql += 	"	      ) tab inner join serv s on s.id_Serv = tab.id_serv ";
			
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {				
				liTemp.add(new String[] {"0", rs1.getString("serv"), rs1.getString("QUANTIDADE"), rs1.getString("id_serv")  });
			}
	//rs1.close();
	
	} finally{
		try{
			if (rs1 != null) rs1.close();
		} catch(Exception e) {
		}
	}
		return liTemp;
	}	
		/**
	 * Consulta a quantidade de processos ativos em uma delegacia.
	 * Esse método é utilizado na consulta feita na página inicial dos usuários delegados.
	 * 
	 * @param id_Serventia identificação da serventia
	 * @author msapaula
	 */
	public List consultarQuantidadeAtivosDelegacia(String id_ServentiaOrigem) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{

			stSql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PROC p";
			stSql += " where p.ID_SERV_ORIGEM = ?";					ps.adicionarLong(id_ServentiaOrigem);
			stSql += " and p.PROC_STATUS_CODIGO = ?";				ps.adicionarLong(ProcessoStatusDt.ATIVO);

			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				liTemp.add(new String[] {"0", "Ativos", rs1.getString("QUANTIDADE") });
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}
	
//	/**
//	 * Método que consulta os processos ativos de determinada pessoa no sistema SSP.
//	 * @param idPessoa - ID da pessoa
//	 * @return lista de processos
//	 * @throws Exception
//	 * @author hmgodinho
//	 */
//	public List consultarProcessosAtivosPessoa(String idPessoa) throws Exception {
//		List listaProcessos = new ArrayList();
//		String stSql;
//		ResultSetTJGO rs1 = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//		ProcessoDt processoDt = null;
//		try{
//			
//			stSql = "SELECT DISTINCT PR.ID_PROC, PR.PROC_NUMERO || '.' || PR.DIGITO_VERIFICADOR AS PROC_NUMERO, PR.SERV, PR.PROC_TIPO, PR.PROC_STATUS, PR.DATA_RECEBIMENTO FROM SSP.PESSOA P "
//					+ "LEFT JOIN SSP.PESSOA_VINCULO PV ON P.ID_PESSOA = PV.ID_PESSOA AND PV.ID_VINCULO = ? "
//					+ "INNER JOIN SSP.PESSOA P2 ON P2.ID_PESSOA = PV.ID_PESSOA_VINCULADA "
//					+ "INNER JOIN PROC_PARTE PP ON PP.CPF = P.CPF OR ( PP.NOME = P.NOME AND P.DATA_NASC = PP.DATA_NASCIMENTO AND PP.NOME_MAE = P2.NOME) "
//					+ "INNER JOIN VIEW_PROC PR ON PR.ID_PROC = PP.ID_PROC ";
//			ps.adicionarLong(VinculoDt.MAE);
//			stSql += " WHERE P.ID_PESSOA = ?";				ps.adicionarLong(idPessoa);
//			
//			rs1 = consultar(stSql, ps);
//			while (rs1.next()) {
//				processoDt = new ProcessoDt();
//				processoDt.setId_Processo(rs1.getString("ID_PROC"));
//				processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
//				processoDt.setServentia(rs1.getString("SERV"));
//				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));
//				processoDt.setProcessoStatus(rs1.getString("PROC_STATUS"));
//				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
//
//				listaProcessos.add(processoDt);
//			}
//		} finally{
//			try{
//				if (rs1 != null) rs1.close();
//			} catch(Exception e) {
//			}
//		}
//		return listaProcessos;
//	}

	/**
	 * Consulta a quantidade de processos ativos para um promotor.
	 * Esse método é utilizado na consulta feita na página inicial dos usuários promotores
	 * 
	 * @param id_ServentiaCargo identificação do serventiaCargo
	 * @author msapaula
	 */
	public List consultarQuantidadeAtivosPromotor(String id_ServentiaCargo) throws Exception {
		List liTemp = new ArrayList();	
		String sql = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			
			sql = "SELECT COUNT(p.ID_PROC) as QUANTIDADE ";
			sql += " FROM PROJUDI.VIEW_BUSCA_PROC_TODOS p";
			sql += " JOIN Proc_Resp Pr  ON Pr.Id_Proc       = P.Id_Proc  AND Pr.Codigo_Temp <> ?"; 									ps.adicionarLong(-1);
			sql += " JOIN SERV_CARGO SC  ON PR.ID_SERV_CARGO = SC.ID_SERV_CARGO ";
			sql += " JOIN SERV S1  ON SC.ID_SERV = S1.ID_SERV ";
			sql += " JOIN SERV_TIPO ST  ON S1.ID_SERV_TIPO      = ST.ID_SERV_TIPO  AND ST.SERV_TIPO_CODIGO = ?  ";					ps.adicionarLong(ServentiaTipoDt.PROMOTORIA);  
			
			sql += " WHERE Sc.ID_SERV_CARGO = ?";								ps.adicionarLong(id_ServentiaCargo);
			sql += " AND p.PROC_STATUS_CODIGO = ?"; 							ps.adicionarLong(ProcessoStatusDt.ATIVO);	
			
			rs1 = consultar(sql, ps);
			if (rs1.next()) liTemp.add(new String[] {"0", "Ativos", rs1.getString("QUANTIDADE") });			
			rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return liTemp;
	}

	/**
	 * Consulta a quantidade de processos ativos para um juiz.
	 * Os juízes terão acesso aos processos onde ele é responsável e em uma determinada serventia.
	 * Esse método é utilizado na consulta feita na página inicial dos juízes
	 * 
	 * @param id_ServentiaCargo, identificação do serventiaCargo
	 * @param id_Serventia, identtificação da serventia do usuário logado
	 * @author msapaula
	 */
	public int consultarQuantidadeAtivosJuiz(String id_ServentiaCargo, String id_Serventia) throws Exception {
		int qtde = 0;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{

			stSql = "SELECT COUNT(*) as QUANTIDADE FROM PROJUDI.VIEW_PROC p";
			stSql += " JOIN PROJUDI.PROC_RESP pr on p.ID_PROC = pr.ID_PROC";
			stSql += " WHERE pr.ID_SERV_CARGO = ?"; 			ps.adicionarLong(id_ServentiaCargo);
			stSql += " AND p.ID_SERV = ?";					    ps.adicionarLong(id_Serventia);
			stSql += " AND p.PROC_STATUS_CODIGO = ?"; 		    ps.adicionarLong(ProcessoStatusDt.ATIVO);
			stSql += " AND pr.CODIGO_TEMP <> ?";			    ps.adicionarLong(ProcessoResponsavelDt.INATIVO); 

			rs1 = consultar(stSql,ps);
			if (rs1.next()) {
				qtde = rs1.getInt("QUANTIDADE");
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return qtde;
	}

	/**
	 * Consulta a quantidade de processos ativos em uma turma recursal para um Juiz.
	 * Deve retornar somente os processos de 2º grau ativos e os recursos inominados que já foram autuados.
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param id_ServentiaCargo,
	 * @author msapaula
	 */
	public int consultarQuantidadeAtivosJuizTurmaRecursal(String id_ServentiaCargo, String id_Serventia) throws Exception {
		int qtde = 0;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			stSql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_PROC p";
			if (id_ServentiaCargo != null && id_ServentiaCargo.length() > 0) {
				stSql += " JOIN PROJUDI.PROC_RESP pr on (p.ID_PROC = pr.ID_PROC AND pr.ID_SERV_CARGO = ?)";
				ps.adicionarLong(id_ServentiaCargo);
			}
			stSql += " WHERE p.PROC_STATUS_CODIGO = ?";					ps.adicionarLong(ProcessoStatusDt.ATIVO);
			if (id_Serventia != null && id_Serventia.length() > 0){
				stSql += " and p.ID_SERV = ?";						ps.adicionarLong(id_Serventia);
			}
			stSql += " AND p.ID_PROC NOT IN ";
			stSql += " (SELECT r.ID_PROC FROM PROJUDI.RECURSO r WHERE r.DATA_RECEBIMENTO IS NULL )";

			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				qtde = rs1.getInt("QUANTIDADE");
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return qtde;
	}

	/**
	 * Atualiza a serventia do processo
	 * @param id_Processo identificação do processo
	 * @param id_Serventia nova serventia do processo
	 * 
	 * @author msapaula
	 */
	public void alterarServentiaProcesso(String id_Processo, String id_ServentiaUsuarioLogado, String id_ServentiaNova, String id_AreaDistribuicaoNova) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET ID_SERV = ?";		ps.adicionarLong(id_ServentiaNova);
		Sql += " ,ID_AREA_DIST = ? ";						ps.adicionarLong(id_AreaDistribuicaoNova);
		Sql += " WHERE ID_PROC = ? AND ID_SERV = ?";		ps.adicionarLong(id_Processo);				ps.adicionarLong(id_ServentiaUsuarioLogado);

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Atualiza a serventia do processo sem verificar se o usuário está na mesma serventia que o processo.
	 * ATENÇÃO: ESTA FUNCIONALIDADE FOI FEITA PARA A REDISTRIBUIÇÃO EM LOTE DO DISTRIBUIDOR E DESEVE SER
	 * USADA COM CAUTELA POIS NÃO VALIDA SE O USUÁRIO ESTÁ REDISTRIBUINDO O PROCESSO DA MESMA SERVENTIA
	 * EM QUE ELE SE ENCONTRA.
	 * @param id_Processo identificação do processo
	 * @param id_Serventia nova serventia do processo
	 * 
	 * @author hrrosa
	 */
	public void alterarServentiaProcessoDistribuidor(String id_Processo, String id_ServentiaNova, String id_AreaDistribuicaoNova) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET ID_SERV = ?";		ps.adicionarLong(id_ServentiaNova);
		Sql += " ,ID_AREA_DIST = ? ";						ps.adicionarLong(id_AreaDistribuicaoNova);
		Sql += " WHERE ID_PROC = ?";						ps.adicionarLong(id_Processo);

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método que altera os dados do processo durante o envio para a instância superior.
	 * @param processoDt - processo a ser enviado
	 * @param id_ServentiaOrigem - id da serventia de origem do processo
	 * @param faseAnterior - fase anterior do processo
	 * @param id_ProcessoPrioridadeAnterior - id da prioridade anterior do processo
	 * @param logDt - log da alteração
	 * @param obFabricaConexao
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void alterarProcessoEnvioInstanciaSuperior(String id_Processo, String id_ServentiaOrigem, String id_ServentiaDestino, String id_AreaDistribuicao, String faseCodigo, String id_ProcessoPrioridade) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET ID_SERV = ?";
		ps.adicionarLong(id_ServentiaDestino);
		Sql += " ,ID_PROC_FASE = (SELECT ID_PROC_FASE FROM PROJUDI.PROC_FASE WHERE PROC_FASE_CODIGO = ?)";
		ps.adicionarLong(faseCodigo);
		Sql += " ,ID_AREA_DIST = ?";
		ps.adicionarLong(id_AreaDistribuicao);
		Sql += " ,ID_PROC_PRIOR = ?";
		ps.adicionarLong(id_ProcessoPrioridade);
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		//esse AND foi incluído para impedir que o mesmo processo seja enviado duas vezes para instância superior.
		//quando o sistema demora a responder, alguns usuários enviam o processo novamente e causa duplicidade na distribuição.
		//não alterar antes de falar com Jesus ou Henrique.
		Sql += " AND ID_SERV = ?";
		ps.adicionarLong(id_ServentiaOrigem);
		
		int qtdeRegistrosAlterados = executarUpdateDelete(Sql, ps);
		
		//se a quantidade de recursos alterados for zero é porque o processo não foi mais localizado na serventia de origem. Isso
		//indica que o usuário enviou o processo duas (ou mais) vezes para a instância superior.
		if(qtdeRegistrosAlterados == 0) {
			throw new MensagemException("Não foi possível enviar o processo para instância superior. Verifique se o mesmo já foi enviado anteriormente.");
		}
	}
	
	/**
	 * Altera dados do processo em virtude de um recurso não originário
	 * 
	 * @param id_Processo identificação do processo
	 * @param id_Serventia nova serventia do processo
	 * @param id_AreaDistribuicao nova área de distribuição do processo
	 * @param faseCodigo nova fase do processo
	 * 
	 * @author msapaula
	 */
	public void alterarProcessoRecursoNaoOriginario(String id_Processo, String id_Serventia, String id_AreaDistribuicao, String faseCodigo, String id_ProcessoPrioridade) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET ID_SERV = ?";
		ps.adicionarLong(id_Serventia);
		Sql += " ,ID_PROC_FASE = (SELECT ID_PROC_FASE FROM PROJUDI.PROC_FASE WHERE PROC_FASE_CODIGO = ?)";
		ps.adicionarLong(faseCodigo);
		Sql += " ,ID_AREA_DIST = ?";
		ps.adicionarLong(id_AreaDistribuicao);
		Sql += " ,ID_PROC_PRIOR = ?";
		ps.adicionarLong(id_ProcessoPrioridade);
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

		executarUpdateDelete(Sql, ps);
	}

	/**
	 * Atualiza os dados de um processo em virtude do retorno à serventia de origem de um recurso não originário
	 * 
	 * @param id_Processo identificação do processo
	 * @param id_Serventia nova serventia do processo
	 * @param faseAnterior nova fase do processo
	 * @param if_Classificador novo classificador do processo
	 * 
	 * @author msapaula
	 */
	public void alterarProcessoRetornoRecursoNaoOriginario(String id_Processo, String id_Serventia, String id_AreaDistribuicao, String faseCodigo, String id_Classificador) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET ID_SERV = ?";																	ps.adicionarLong(id_Serventia);
		Sql += " ,ID_PROC_FASE = (SELECT ID_PROC_FASE FROM PROJUDI.PROC_FASE WHERE PROC_FASE_CODIGO = ?)";				ps.adicionarLong(faseCodigo);
		Sql += " ,ID_CLASSIFICADOR = ?";																				ps.adicionarLong(id_Classificador);
		Sql += " ,ID_AREA_DIST = ?";																					ps.adicionarLong(id_AreaDistribuicao);
		Sql += " WHERE ID_PROC = ?";																					ps.adicionarLong(id_Processo);

		executarUpdateDelete(Sql, ps);
	}

	/**
	 * Altera dados do processo em virtude do arquivamento ou desarquivamento
	 * 
	 * @param processoDt dt do processo
	 * 
	 * @author msapaula
	 */
	public void alterarProcessoArquivamento(ProcessoDt processoDt) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET DATA_ARQUIVAMENTO = ?";
		ps.adicionarDateTime(processoDt.getDataArquivamento());
		Sql += " ,ID_PROC_STATUS = (SELECT ID_PROC_STATUS FROM PROJUDI.PROC_STATUS WHERE PROC_STATUS_CODIGO = ?)";
		ps.adicionarLong(processoDt.getProcessoStatusCodigo());
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(processoDt.getId());

			executarUpdateDelete(Sql, ps);
	}

	/**
	 * Altera o Status de um processo,em virtude da Suspensão ou fim da suspensão
	 * 
	 * @param id_Processo, identificação do processo que terá o status alterado
	 * @param novoStatus, novo status do processo
	 * 
	 * @author msapaula
	 */
	public void alterarProcessoSuspensao(String id_Processo, int novoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC set ID_PROC_STATUS = ";
		Sql += " (SELECT ID_PROC_STATUS FROM PROJUDI.PROC_STATUS WHERE PROC_STATUS_CODIGO = ?)";
		ps.adicionarLong(novoStatus);
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Altera o Status de um processo,em virtude da Suspensão ou fim da suspensão
	 * 
	 * @param id_Processo, identificação do processo que terá o status alterado
	 * @param novoStatus, novo status do processo
	 * 
	 * @author msapaula
	 */
	public void suspenderProcessoAlterandoFaseProcessual(String id_Processo, int novoStatus, String codFaseProcesso) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC set ID_PROC_STATUS = ";
		Sql += " (SELECT ID_PROC_STATUS FROM PROJUDI.PROC_STATUS WHERE PROC_STATUS_CODIGO = ?)";
		ps.adicionarLong(novoStatus);
		
		if (codFaseProcesso != null && codFaseProcesso.length()>0){
			Sql += ", ID_PROC_FASE =  (SELECT ID_PROC_FASE FROM PROJUDI.PROC_FASE WHERE PROC_FASE_CODIGO = ?)";
			ps.adicionarLong(Funcoes.StringToInt(codFaseProcesso));
		}
		
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

			executarUpdateDelete(Sql, ps);
	}


	/**
	* Método responsável em montar a lista de processos baseado em um ResultSetTJGO recebido.
	* Para cada processo encontrado, irá efetuar outra consulta para trazer as partes ativas de cada processo.
	* 
	* @param rs1 resultado da consulta de processos
	* @return lista de processos com as partes vinculadas
	 * @throws Exception 
	*/
	private List getListaProcessos(ResultSetTJGO rs1, boolean consultaTodasPartes) throws Exception{
		String processos = "";
		String recursos = ""; //variável para concatenar os recursos inominados
		ProcessoDt processoDt;
		Map mapProcesso = new LinkedHashMap();
		List listaProcessos = new ArrayList();
		PreparedStatementTJGO psRecursos = new PreparedStatementTJGO();
		PreparedStatementTJGO psProcessos = new PreparedStatementTJGO();
		
		//Essa primeira consulta retorna somente os processos
		while (rs1.next()) {
			//Criando objeto do tipo processo
			processoDt = new ProcessoDt();
			processoDt.setId_Processo(rs1.getString("ID_PROC"));
			processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
			processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
			processoDt.setAno(rs1.getString("ANO"));
			processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
			processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
			processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
			processoDt.setServentia(rs1.getString("SERV"));
			processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

			if (consultaTodasPartes)
			{
				//Concatena os processos encontrados para depois buscar as partes desses
				if (processoDt.getId_Recurso().length() > 0){					
					recursos += (recursos.length() > 0 ? " OR " : "") + "(rp.ID_PROC = ?)";
					psRecursos.adicionarLong(processoDt.getId());
				}
				else {
					processos += (processos.length() > 0 ? " OR " : "") + "(p.ID_PROC = ?)";
					psProcessos.adicionarLong(processoDt.getId());
				}
			} else {				
				ProcessoParteDt primeiroPromoventeRecorrente = null;
				ProcessoParteDt primeiroPromovidoRecorrido = null;				
		
				primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE"));
				
				try{
					if (rs1.getString("PRIMEIRO_RECORRENTE") != null) primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_RECORRENTE"));
				}catch(Exception e) {}				
				
				primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));
					
				try{
					if (rs1.getString("PRIMEIRO_RECORRIDO") != null) primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_RECORRIDO"));
				}catch(Exception e) {}							
															
				if (rs1.getString("ID_PROC_PARTE") != null && rs1.getString("NOME") != null && rs1.getString("PROC_PARTE_TIPO_CODIGO") != null)
				{	
					if (rs1.getString("PROC_PARTE_TIPO_CODIGO").equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)) || rs1.getString("PROC_PARTE_TIPO_CODIGO").equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)))
					{
						primeiroPromoventeRecorrente = new ProcessoParteDt();
						primeiroPromoventeRecorrente.setId(rs1.getString("ID_PROC_PARTE"));
						primeiroPromoventeRecorrente.setNome(rs1.getString("NOME"));
						primeiroPromoventeRecorrente.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					} else if (rs1.getString("PROC_PARTE_TIPO_CODIGO").equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)) || rs1.getString("PROC_PARTE_TIPO_CODIGO").equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)))
					{
						primeiroPromovidoRecorrido = new ProcessoParteDt();						
						primeiroPromovidoRecorrido.setId(rs1.getString("ID_PROC_PARTE"));
						primeiroPromovidoRecorrido.setNome(rs1.getString("NOME"));
						primeiroPromovidoRecorrido.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					}				
				}
                if (primeiroPromoventeRecorrente != null) processoDt.addListaPoloAtivo(primeiroPromoventeRecorrente);                
                if (primeiroPromovidoRecorrido != null) processoDt.addListaPolosPassivos(primeiroPromovidoRecorrido);
			}
			mapProcesso.put(processoDt.getId(), processoDt);
		}
		rs1.close();

		if (consultaTodasPartes){
			
			if (processos.length() > 0) {
				//Busca as partes para cada processo encontrado anteriormente
				String sql = " SELECT p.ID_PROC_PARTE, p.NOME, p.PROC_PARTE_TIPO_CODIGO, p.ID_PROC FROM PROJUDI.VIEW_PROC_PARTE p";
				sql += " WHERE (" + processos + ") AND p.DATA_BAIXA IS NULL";
				ResultSetTJGO rs2 = consultar(sql, psProcessos);
				setPartesProcesso(rs2, mapProcesso);
				rs2.close();
			}
	
			if (recursos.length() > 0) {
				//Busca as partes para cada recurso inominado
				String sql = " SELECT rp.ID_PROC_PARTE, rp.NOME, rp.PROC_PARTE_TIPO_CODIGO, rp.ID_PROC FROM PROJUDI.VIEW_RECURSO_PARTE rp";
				sql += " WHERE (" + recursos + ") AND rp.DATA_BAIXA IS NULL AND rp.DATA_RETORNO IS NULL";
				ResultSetTJGO rs3 = consultar(sql, psRecursos);
				setPartesProcesso(rs3, mapProcesso);
				rs3.close();
			}
		}

		listaProcessos.addAll(mapProcesso.values()); //Adiciona na lista os valores do map
		mapProcesso = null;
		return listaProcessos;
	}

	/**
	 * Método que monta a lista de partes para cada processo ou recurso inominado resultante da busca de processos.
	 * @param rs
	 * @param mapProcesso
	 * @throws Exception
	 */
	private void setPartesProcesso(ResultSetTJGO rs1, Map mapProcesso) throws Exception {
		while (rs1.next()) {
			String id_Processo = rs1.getString("ID_PROC");
			ProcessoDt tempProcessoDt = (ProcessoDt) mapProcesso.get(id_Processo);

			ProcessoParteDt parteDt = new ProcessoParteDt();
			parteDt.setId(rs1.getString("ID_PROC_PARTE"));
			parteDt.setNome(rs1.getString("NOME"));

			// Adiciona parte a lista correspondente
			int tipo = Funcoes.StringToInt(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
			switch (tipo) {
				case (ProcessoParteTipoDt.POLO_ATIVO_CODIGO):				
					tempProcessoDt.addListaPoloAtivo(parteDt);
					break;
				case (ProcessoParteTipoDt.POLO_PASSIVO_CODIGO):				
					tempProcessoDt.addListaPolosPassivos(parteDt);
					break;
			}
		}
	}

	/**
	* Método responsável em montar a lista de processos baseado em um ResultSetTJGO recebido,
	* devolvendo cada processo com um Código Hash gerado para permitir validações posteriores.
	*  
	* @param rs1, resultado da consulta de processos
	* @param usuarioNe, usuário logado para permitir geração de HASH
	* @return lista de processos com as partes vinculadas
	 * @throws Exception 
	*/
	/* NÃO SERÁ MAIS UTILIZADO
	 * public List getListaProcessosComHash(ResultSetTJGO rs1, UsuarioNe usuarioNe, boolean consultaTodasPartes) throws Exception{
		List listaProcesso = getListaProcessos(rs1, consultaTodasPartes);
		for (int i = 0; i < listaProcesso.size(); i++) {
			ProcessoDt processoDt = (ProcessoDt) listaProcesso.get(i);
			processoDt.setHash(usuarioNe.getCodigoHash(processoDt.getId()));

		}
		return listaProcesso;
	}
	*/

	/**
	 * Retorna lista de possíveis preventos para um processo passado.
	 * Verifica as partes, valor e tipo da ação.
	 * 
	 * @param processoDt, obj para o qual serão consultados os possíveis preventos
	 * @param id_Comarca, identificação da comarca do processo
	 * @param id_ServentiaSubTipo, sub tipo da serventia
	 * 
	 * @return lista de possíveis preventos
	 * @throws Exception 
	 */
	public List consultarConexaoProcessoOriginario(ProcessoDt processoDt, String id_Comarca, String id_ServentiaSubTipo) throws Exception {
		List liConexo = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		List promoventes = processoDt.getListaPolosAtivos();
		List promovidos = processoDt.getListaPolosPassivos();
		try{
			if (promoventes!=null){
				for (int i = 0; i < promoventes.size(); i++) {
					ProcessoParteDt promovente = (ProcessoParteDt) promoventes.get(i);
					
        			String sql = "SELECT pp.ID_PROC, pp.PROC_NUMERO, pp.DIGITO_VERIFICADOR, pp.PROC_NUMERO_COMPLETO, pp.CPF_CNPJ_PROMOVENTE, pp.CPF_CNPJ_PROMOVIDO, " +
        				"pp.PROMOVENTE, pp.PROMOVIDO, pp.VALOR, pp.ID_PROC_TIPO, pp.PROC_TIPO, pp.DATA_RECEBIMENTO, pp.ID_SERV, pp.SERV, pp.ID_COMARCA, " +
        				"pp.ID_SERV_SUBTIPO, pp.DATA_ARQUIVAMENTO FROM PROJUDI.VIEW_PROCS_PARTES pp ";
        			sql += " WHERE pp.ID_COMARCA = ?";			
        			sql += " AND pp.ID_SERV_SUBTIPO = ?";		
        			sql += " AND (pp.VALOR = ? OR pp.ID_PROC_TIPO = ?) AND (";
        						
					String cpfPromovente = "12345";
	
					if (promovente.getCpfCnpj() != null && !promovente.getCpfCnpj().equals("")) cpfPromovente = promovente.getCpfCnpj();
					if (promovidos != null && promovidos.size() > 0) {
						
						for (int j = 0; j < promovidos.size(); j++) {
							ps.limpar();
							ps.adicionarLong(id_Comarca);
							ps.adicionarLong(id_ServentiaSubTipo);
							ps.adicionarDecimal(processoDt.getValor());
		        			ps.adicionarLong(processoDt.getId_ProcessoTipo());
		        			
							String cpfPromovido = "12345";
							ProcessoParteDt promovido = (ProcessoParteDt) promovidos.get(j);
	
							if (promovido.getCpfCnpj() != null && !promovido.getCpfCnpj().equals("")) cpfPromovido = promovido.getCpfCnpj();
									
							//Somente se parte tem CPF e é diferente de 999.999.999-99
							String sql1 = " ((pp.CPF_CNPJ_PROMOVENTE = ? AND pp.CPF_CNPJ_PROMOVIDO= ?) OR";				ps.adicionarLong(cpfPromovente);						ps.adicionarLong(cpfPromovido);
							sql1 += " (pp.CPF_CNPJ_PROMOVENTE = ? AND pp.CPF_CNPJ_PROMOVIDO = ?) OR";					ps.adicionarLong(cpfPromovido);							ps.adicionarLong(cpfPromovente);
							sql1 += " (pp.PROMOVENTE = ? AND pp.PROMOVIDO = ?) OR";										ps.adicionarString(promovente.getNome());				ps.adicionarString(promovido.getNome());
							sql1 += " (pp.PROMOVENTE = ? AND pp.PROMOVIDO = ?) )";										ps.adicionarString(promovido.getNome());				ps.adicionarString(promovente.getNome());
							
							rs1 = this.consultar(sql + sql1 + ")", ps);

							while (rs1.next()) {
								if (liConexo == null) liConexo = new ArrayList();

								ProcessoDt processo = new ProcessoDt();
								processo.setId_Processo(rs1.getString("ID_PROC"));
								processo.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
								processo.setValor(rs1.getString("VALOR"));
								processo.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
								processo.setProcessoTipo(rs1.getString("PROC_TIPO"));
								processo.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
								processo.setId_Serventia(rs1.getString("ID_SERV"));
								processo.setServentia(rs1.getString("SERV"));
								processo.setDataArquivamento(rs1.getString("DATA_ARQUIVAMENTO"));
								
								liConexo.add(processo);
							}
							
						}						
						
					} else {
						ps.limpar();
						ps.adicionarLong(id_Comarca);
						ps.adicionarLong(id_ServentiaSubTipo);
						ps.adicionarDecimal(processoDt.getValor());
	        			ps.adicionarLong(processoDt.getId_ProcessoTipo());
	        			
						String sql1 = " (pp.CPF_CNPJ_PROMOVENTE = ? AND pp.CPF_CNPJ_PROMOVIDO IS NULL) OR ";					ps.adicionarLong(cpfPromovente);
						sql1 += " (pp.PROMOVENTE = ? AND pp.PROMOVIDO IS NULL) ";										ps.adicionarString(promovente.getNome());
						
						rs1 = this.consultar(sql + sql1 + ")", ps);

						while (rs1.next()) {
							if (liConexo == null) liConexo = new ArrayList();

							ProcessoDt processo = new ProcessoDt();
							processo.setId_Processo(rs1.getString("ID_PROC"));
							processo.setProcessoNumero(rs1.getString("PROC_NUMERO_COMPLETO"));
							processo.setValor(rs1.getString("VALOR"));
							processo.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
							processo.setProcessoTipo(rs1.getString("PROC_TIPO"));
							processo.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
							processo.setId_Serventia(rs1.getString("ID_SERV"));
							processo.setServentia(rs1.getString("SERV"));
							processo.setDataArquivamento(rs1.getString("DATA_ARQUIVAMENTO"));
							
							liConexo.add(processo);
						}
					}
					
				}
			}

		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
				
			}
		}

		return liConexo;
	}

	/**
	 * Consulta processos que devem ter a Suspensão Finalizada.
	 * 
	 * @author msapaula
	 */
	public List consultarProcessosFinalizarSuspensao() throws Exception {
		List processos = null;
		String id_Processo = null;
		ResultSetTJGO rs1 = null;
		try{
			String sql = "SELECT ID_PROC FROM PROJUDI.VIEW_PROC_FINALIZAR_SUSPENSAO p ";

			rs1 = this.consultarSemParametros(sql);

			while (rs1.next()) {
				if (processos == null) processos = new ArrayList();
				id_Processo = rs1.getString("ID_PROC");
				processos.add(id_Processo);
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return processos;
	}

	/**
	 * Método de Consulta Processo(s) com um tipo de pendência específico.
	 * @param String numeroProcesso
	 * @param Integer tipoPendencia
	 * @return Lista
	 * @throws Exception
	 */
	public List consultarProcessoPendencia(String numeroProcesso, Integer tipoPendencia) throws Exception {
		StringBuffer sql = new StringBuffer();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String ComandoWhereAnd = " WHERE ";

		List listTemp = null;
		ResultSetTJGO rs1 = null;

		try{

			sql.append("SELECT DISTINCT PROC.* FROM PROC, PEND  ");
			if (numeroProcesso.length() > 0) {
				sql.append(ComandoWhereAnd + " PROC.PROC_NUMERO = ? ");
				ps.adicionarLong(numeroProcesso);
				ComandoWhereAnd = " AND ";
			}
			if (tipoPendencia != null) {
				sql.append(ComandoWhereAnd +" PEND.ID_PEND_TIPO = ? ");
				ps.adicionarLong(tipoPendencia);
				ComandoWhereAnd = " AND ";
			}
			sql.append(" AND PROC.ID_PROC = PEND.ID_PROC ");
			sql.append(" ORDER BY PROC.PROC_NUMERO ");

			rs1 = this.consultar(sql.toString(),ps);

			while (rs1.next()) {
				if (listTemp == null) listTemp = new ArrayList();

				ProcessoDt processoDt = new ProcessoDt();
				associarDt(processoDt, rs1);

				listTemp.add(processoDt);
			}

			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return listTemp;
	}

	/**
	 * Método que consulta os Processos Semi-Paralisados, podendo ser informado ou não o Id da Serventia.
	 * @param idServentia - Id da Serventia
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos semi-paralisados
	 * @throws Exception
	 * @author hmgodinho
	 */
//	public List consultarProcessosSemiParalisadosServentia(String idServentia, String id_ServentiaCargo, String posicaoPaginaAtual) throws Exception {
//		String sqlQuantidade = "";
//		String sqlLista = "";
//		String sqlTemp = "";
//		List listaProcessos = new ArrayList();
//		ResultSetTJGO rs1 = null;
//		ResultSetTJGO rs2 = null;
//		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
//
//		sqlTemp = " FROM PROJUDI.VIEW_PROC_SEMI_PARALISADOS p ";
//		if (idServentia != null && !idServentia.equals("") && !idServentia.equals("null")) {
//			sqlTemp += " WHERE ID_SERV = ?";
//			ps.adicionarLong(idServentia);
//		}
//		if (id_ServentiaCargo != null && !id_ServentiaCargo.equals("") && !id_ServentiaCargo.equals("null")) {
//			sqlTemp += " AND ID_SERV_CARGO = ?";
//			ps.adicionarLong(id_ServentiaCargo);
//		}
//
//		sqlLista = "SELECT * " + sqlTemp;
//		sqlLista += " ORDER BY p.DATA_INICIO,p.PROC_NUMERO";
//		
//		try{
//			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
//			while (rs1.next()) {
//				ProcessoSemiParalisadoDt obTemp = new ProcessoSemiParalisadoDt();
//				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
//				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
//				obTemp.setTipoPendencia(rs1.getString("PEND_TIPO"));
//				obTemp.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO")));
//				listaProcessos.add(obTemp);
//			}
//			sqlQuantidade = "SELECT COUNT(p.ID_PROC) AS QUANTIDADE " + sqlTemp;
//			rs2 = consultar(sqlQuantidade, ps);
//			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
//		
//		} finally{
//			try{
//				if (rs1 != null) rs1.close();
//			} catch(Exception e) {
//			}
//			try{
//				if (rs2 != null) rs2.close();
//			} catch(Exception e) {
//			}
//		}
//		return listaProcessos;
//	}

	/**
	 * Método que consulta os Processos Paralisados, podendo ser informado ou não o Id da Serventia.
	 * @param idServentia - Id da Serventia
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos semi-paralisados
	 * @throws Exception
	 * @author hmgodinho
	 * @author Jesus - inclusão das pendencias tipos
	 */
	public List consultarProcessosParalisadosServentia(String idServentia, String opcaoPeriodo, String cargoServentia, String posicaoPaginaAtual) throws Exception {
		String sqlQuantidade = "";
		String sqlLista = "";
		String sqlTemp00 = "";
		String sqlGroupBy = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;

		sqlTemp00 = " FROM PROJUDI.VIEW_PROCS_PARALISADOS p ";
		sqlTemp00 += " WHERE p.PEND_TIPO_CODIGO NOT IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		ps.adicionarLong(PendenciaTipoDt.AGUARDANDO_CUMPRIMENTO_PENA);
		ps.adicionarLong(PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO);
		ps.adicionarLong(PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO_PRISAO_CIVIL);
		ps.adicionarLong(PendenciaTipoDt.AGUARDANDO_PRAZO_DECADENCIAL);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR );
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.SUSPENSAO_PROCESSO);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_MANIFESTACAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);
		ps.adicionarLong(PendenciaTipoDt.PEDIDO_VISTA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);

		if (idServentia != null && !idServentia.equals("") && !idServentia.equals("null")) {
			sqlTemp00 += " AND ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}

		if (opcaoPeriodo != null && !opcaoPeriodo.equals("") && !opcaoPeriodo.equals("null")) {
			String dataPesquisa = Funcoes.dateToStringSoData(new Date());
			dataPesquisa = Funcoes.somaData(dataPesquisa, Funcoes.StringToInt(opcaoPeriodo) * -1);
			if(Funcoes.StringToInt(opcaoPeriodo) < 20) {
				sqlTemp00 += " AND DATA_INICIO >= ?";
			} else {
				sqlTemp00 += " AND DATA_INICIO <= ?";
			}
			ps.adicionarDate(dataPesquisa);
		}
		if (cargoServentia != null && !cargoServentia.equals("") && !cargoServentia.equals("null")) {
			sqlTemp00 += " AND ID_SERV_CARGO = ?";
			ps.adicionarLong(cargoServentia);
		}

		sqlGroupBy = " GROUP BY p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ID_SERV, p.PEND_TIPO, p.DATA_INICIO, p.PEND_TIPO_CODIGO ";
		
		sqlLista = "SELECT p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ID_SERV, p.PEND_TIPO, p.DATA_INICIO, p.PEND_TIPO_CODIGO " + sqlTemp00;
		sqlLista += sqlGroupBy;
		sqlLista += " ORDER BY p.DATA_INICIO,p.PROC_NUMERO";

		try{
			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ProcessoParalisadoDt obTemp = new ProcessoParalisadoDt();
				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				obTemp.setTipoPendencia(rs1.getString("PEND_TIPO"));
				String dataInicio = Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO"));
				obTemp.setDataRecebimento(dataInicio);
				long[] diferencaDatas = Funcoes.diferencaDatas(new Date(), Funcoes.StringToDate(dataInicio));
				obTemp.setQuantidadeDiasParalisados(String.valueOf(diferencaDatas[0]) + " dias");
				listaProcessos.add(obTemp);
			}
			sqlQuantidade = "SELECT COUNT(1) AS QUANTIDADE FROM (SELECT p.ID_PROC " + sqlTemp00 + sqlGroupBy +" )";
			rs2 = consultar(sqlQuantidade,ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}

	/**
	 * Método que consulta os Processos Paralisados, podendo ser informado ou não o Id da Serventia.
	 * @param idServentia - Id da Serventia
	 * @param idServentiaCargo - Id da Serventia Cargo do Juiz Responsável
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos paralisados com periodo
	 * @throws Exception     
	 * @author Jesus - inclusão das pendencias tipos
	 */
	public List consultarProcessosParalisadosConclusoes(String idServentia, String id_ServentiaCargo, String opcaoPeriodo, String posicaoPaginaAtual) throws Exception {
		String sqlQuantidade = "";
		String sqlLista = "";
		String sqlTemp00 = "";
		String sqlGroupBy = "";

		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sqlTemp00 = " FROM PROJUDI.VIEW_PROCS_PARALISADOS p ";
		sqlTemp00 += " WHERE p.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);

		if (idServentia != null && !idServentia.equals("") && !idServentia.equals("null")) {
			sqlTemp00 += " AND ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}
		
		if (id_ServentiaCargo != null && !id_ServentiaCargo.trim().equalsIgnoreCase("") && !id_ServentiaCargo.trim().equalsIgnoreCase("null")){
			sqlTemp00 += " AND ID_SERV_CARGO = ?";
			ps.adicionarLong(id_ServentiaCargo);
		} else {
			sqlTemp00 += " AND ID_SERV_CARGO IS NOT NULL";
		}

		if (opcaoPeriodo != null && !opcaoPeriodo.equals("") && !opcaoPeriodo.equals("null")) {
			String dataPesquisa = Funcoes.dateToStringSoData(new Date());
			dataPesquisa = Funcoes.somaData(dataPesquisa, Funcoes.StringToInt(opcaoPeriodo) * -1);
			if(Funcoes.StringToInt(opcaoPeriodo) < 20) {
				sqlTemp00 += " AND DATA_INICIO >= ?";
			} else {
				sqlTemp00 += " AND DATA_INICIO <= ?";
			}
			ps.adicionarDate(dataPesquisa);
		}
		
		sqlGroupBy = " GROUP BY p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ID_SERV, p.PEND_TIPO, p.DATA_INICIO, p.PEND_TIPO_CODIGO ";

		sqlLista = "SELECT p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ID_SERV, p.PEND_TIPO, p.DATA_INICIO, p.PEND_TIPO_CODIGO " + sqlTemp00;
		sqlLista += sqlGroupBy;
		sqlLista += " ORDER BY p.DATA_INICIO,p.PROC_NUMERO";
		
		try{
			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ProcessoParalisadoDt obTemp = new ProcessoParalisadoDt();
				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				obTemp.setTipoPendencia(rs1.getString("PEND_TIPO"));
				String dataInicio = Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO"));
				obTemp.setDataRecebimento(dataInicio);
				long[] diferencaDatas = Funcoes.diferencaDatas(new Date(), Funcoes.StringToDate(dataInicio));
				obTemp.setQuantidadeDiasParalisados(String.valueOf(diferencaDatas[0]) + " dias");
				listaProcessos.add(obTemp);
			}
			sqlQuantidade = "SELECT COUNT(1) AS QUANTIDADE FROM (SELECT p.ID_PROC " + sqlTemp00 + sqlGroupBy + " )";
			rs2 = consultar(sqlQuantidade, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	
	/**
	 * Método que consulta os Processos Paralisados, podendo ser informado ou não o Id da Serventia.
	 * @param idServentia - Id da Serventia
	 * @param idServentiaCargo - Id da Serventia Cargo do Juiz Responsável
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos paralisados com periodo
	 * @throws Exception     
	 * @author Jesus - inclusão das pendencias tipos
	 */
	public List consultarProcessosParalisadosConclusoesSegundoGrau(String idServentia, String id_ServentiaCargo, String opcaoPeriodo, String posicaoPaginaAtual) throws Exception {
		String sqlQuantidade = "";
		String sqlLista = "";
		String sqlTemp00 = "";
		String sqlGroupBy = "";

		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sqlTemp00 = " FROM PROJUDI.VIEW_PROCS_PARALISADOS_2GRAU p ";
		sqlTemp00 += " WHERE p.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);

		if (idServentia != null && !idServentia.equals("") && !idServentia.equals("null")) {
			sqlTemp00 += " AND ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}
		
		if (id_ServentiaCargo != null && !id_ServentiaCargo.trim().equalsIgnoreCase("") && !id_ServentiaCargo.trim().equalsIgnoreCase("null")){
			sqlTemp00 += " AND ID_SERV_CARGO = ?";
			ps.adicionarLong(id_ServentiaCargo);
		} else {
			sqlTemp00 += " AND ID_SERV_CARGO IS NOT NULL";
		}

		if (opcaoPeriodo != null && !opcaoPeriodo.equals("") && !opcaoPeriodo.equals("null")) {
			String dataPesquisa = Funcoes.dateToStringSoData(new Date());
			dataPesquisa = Funcoes.somaData(dataPesquisa, Funcoes.StringToInt(opcaoPeriodo) * -1);
			if(Funcoes.StringToInt(opcaoPeriodo) < 20) {
				sqlTemp00 += " AND DATA_INICIO >= ?";
			} else {
				sqlTemp00 += " AND DATA_INICIO <= ?";
			}
			ps.adicionarDate(dataPesquisa);
		}
		
		sqlGroupBy = " GROUP BY p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ID_SERV, p.PEND_TIPO, p.DATA_INICIO, p.PEND_TIPO_CODIGO, p.ASSISTENTE ";

		sqlLista = "SELECT p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ID_SERV, p.PEND_TIPO, p.DATA_INICIO, p.PEND_TIPO_CODIGO, p.ASSISTENTE " + sqlTemp00;
		sqlLista += sqlGroupBy;
		sqlLista += " ORDER BY p.DATA_INICIO,p.PROC_NUMERO";
		
		try{
			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ProcessoParalisadoDt obTemp = new ProcessoParalisadoDt();
				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				obTemp.setTipoPendencia(rs1.getString("PEND_TIPO"));
				obTemp.setAssistente(rs1.getString("ASSISTENTE"));
				String dataInicio = Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO"));
				obTemp.setDataRecebimento(dataInicio);
				long[] diferencaDatas = Funcoes.diferencaDatas(new Date(), Funcoes.StringToDate(dataInicio));
				obTemp.setQuantidadeDiasParalisados(String.valueOf(diferencaDatas[0]) + " dias");
				listaProcessos.add(obTemp);
			}
			sqlQuantidade = "SELECT COUNT(1) AS QUANTIDADE FROM (SELECT p.ID_PROC " + sqlTemp00 + sqlGroupBy + " )";
			rs2 = consultar(sqlQuantidade, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	

	/**
	 * Método responsável em realizar a consulta de processos de acordo com o Classificador passado. 
	 * 
	 * @param id_Classificador, identificação do classificador (campo não obrigatório)
	 * @param id_Juiz , identificação do juiz responsavel pelo processo (campo não obrigatório)
	 * @param id_Serventia, identificação da serventia (campo obrigatório)
	 * @author msapaula
	 */
	public String consultarProcessosJuizClassificadorJSON(String id_Classificador, String id_Juiz, String id_Serventia, String id_Proc_tipo, String id_assunto, String posicao) throws Exception {
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String listaProcessos = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sqlSelect,  sqlWhere, sqlOrder = "";
		int qtdeColunas = 4;
		
		sqlSelect = "SELECT DISTINCT p.ID_PROC  as id, p.PROC_NUMERO || '.' || p.DIGITO_VERIFICADOR as descricao1,  Replace(regexp_substr(p.PRIMEIRO_PROMOVENTE, '__@--(.*)__@--'),'__@--','') as descricao2 ,  Replace(regexp_substr(p.PRIMEIRO_PROMOVIDO, '__@--(.*)__@--'),'__@--','') as descricao3, to_char(p.DATA_RECEBIMENTO,'DD/MM/YYYY') as descricao4,  p.PROC_PRIOR_ORDEM";
		sqlWhere = "		FROM VIEW_BUSCA_PROC p";
		if(id_assunto!=null && !id_assunto.isEmpty()) {
			sqlWhere += "		inner join proc_assunto pa on p.id_proc=pa.id_proc ";			
		}
		if(id_Juiz != null && !id_Juiz.isEmpty()) {
			sqlWhere += " INNER JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(ProcessoStatusDt.ATIVO);
		}
		
		sqlWhere += " WHERE p.PROC_STATUS_CODIGO = ? "; ps.adicionarLong(ProcessoStatusDt.ATIVO);
		
		if(id_Classificador != null && !id_Classificador.isEmpty()) {
			sqlWhere += " AND p.ID_CLASSIFICADOR = ?"; 				ps.adicionarLong(id_Classificador);
		}
		if(id_Juiz != null && !id_Juiz.isEmpty()) {
			sqlWhere += " AND pr.ID_SERV_CARGO = ?"; 				ps.adicionarLong(id_Juiz);
		}
		if(id_Proc_tipo != null && !id_Proc_tipo.isEmpty()) {
			sqlWhere += " AND p.ID_PROC_TIPO = ?"; 					ps.adicionarLong(id_Proc_tipo);
		}
		if(id_assunto != null && !id_assunto.isEmpty()) {
			sqlWhere += " AND pa.ID_ASSUNTO = ?"; 					ps.adicionarLong(id_assunto);
		}
		if(id_Serventia != null && !id_Serventia.isEmpty()) {
			sqlWhere += " AND p.ID_SERV = ?"; 					ps.adicionarLong(id_Serventia);
		}		

		sqlOrder += " ORDER BY p.PROC_PRIOR_ORDEM , descricao4";
		
		String sqlCount = "SELECT COUNT(distinct p.ID_PROC) as QUANTIDADE " + sqlWhere  ;
		
		try{
			rs1 = this.consultarPaginacao(sqlSelect + sqlWhere + sqlOrder, ps, posicao, "500"); 
			rs2 = this.consultar(sqlCount, ps);
			rs2.next();
			listaProcessos = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}

	public List consultarProcessosJuizClassificador(String id_Classificador, String id_Juiz, String id_Serventia) throws Exception {
		ResultSetTJGO rs1 = null;
		List listaProcessos = new ArrayList();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "";

		sql = " SELECT * FROM ";
		sql += "(SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.DATA_RECEBIMENTO, p.DIGITO_VERIFICADOR, p.ANO, NULL as ID_RECURSO, p.SEGREDO_JUSTICA, pp.PROC_PRIOR_CODIGO, PROC_PRIOR_ORDEM, s.SERV" ;
		sql += ", NULL AS ID_PROC_PARTE, NULL AS NOME, NULL AS PROC_PARTE_TIPO_CODIGO ";
		sql += " ,(SELECT PP.ID_PROC_PARTE ";
		sql += "   || '__@--' ";
		sql += "   || PP.NOME ";
		sql += "   || '__@--' ";
		sql += "   || 'Polo Ativo' ";
		sql += " FROM PROC_PARTE PP ";
		sql += " WHERE PP.DATA_BAIXA           IS NULL ";
		sql += " AND PP.ID_PROC_PARTE_TIPO = ? ";							ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
		sql += " AND PP.ID_PROC                 = P.ID_PROC ";
		sql += " AND ROWNUM                     = 1 ";
		sql += " ) AS PRIMEIRO_PROMOVENTE, ";
		sql += " (SELECT PP.ID_PROC_PARTE ";
		sql += "   || '__@--' ";
		sql += "   || PP.NOME ";
		sql += "   || '__@--' ";
		sql += "   || 'Polo Passivo' ";
		sql += " FROM PROC_PARTE PP ";
		sql += " WHERE PP.DATA_BAIXA           IS NULL ";
		sql += " AND PP.ID_PROC_PARTE_TIPO = ? ";							ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
		sql += " AND PP.ID_PROC                 = P.ID_PROC ";
		sql += " AND ROWNUM                     = 1 ";
		sql += " ) AS PRIMEIRO_PROMOVIDO ";		

		sql += " FROM PROJUDI.PROC p  " ;
		sql += " LEFT JOIN PROJUDI.PROC_STATUS ps ON ps.ID_PROC_STATUS = p.ID_PROC_STATUS " ;				
		sql += " LEFT JOIN PROJUDI.PROC_PRIOR pp ON pp.ID_PROC_PRIOR = p.ID_PROC_PRIOR " ;
		sql += " LEFT JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC " ;
		sql += " INNER JOIN PROJUDI.SERV s ON s.ID_SERV = p.ID_SERV" ;
		sql += " WHERE p.ID_SERV = ?"; 	ps.adicionarLong(id_Serventia);

		sql += "  AND p.ID_PROC NOT IN (SELECT r.ID_PROC FROM PROJUDI.RECURSO r WHERE r.ID_PROC = p.ID_PROC AND r.DATA_RETORNO IS NULL)";
				
		if(id_Classificador != null && !id_Classificador.equalsIgnoreCase("")) {
			sql += " AND p.ID_CLASSIFICADOR = ?"; 	ps.adicionarLong(id_Classificador);
		}
		if(id_Juiz != null && !id_Juiz.equalsIgnoreCase("")) {
			sql += " AND pr.ID_SERV_CARGO = ?"; 		ps.adicionarLong(id_Juiz);
		}
		sql += " AND ps.PROC_STATUS_CODIGO = ? "; ps.adicionarLong(ProcessoStatusDt.ATIVO);
		
		sql += " UNION ";
		
		sql += " SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, r.DATA_RECEBIMENTO, p.DIGITO_VERIFICADOR, p.ANO, r.ID_RECURSO, p.SEGREDO_JUSTICA, pp.PROC_PRIOR_CODIGO, PROC_PRIOR_ORDEM, s.SERV" ;
		sql += ", NULL AS ID_PROC_PARTE, NULL AS NOME, NULL AS PROC_PARTE_TIPO_CODIGO ";
		sql += ", (SELECT RP.ID_PROC_PARTE ";
		sql += "   || '__@--' ";
		sql += "   || PP.NOME ";
		sql += "   || '__@--' ";
		sql += "   || 'Polo Ativo' ";
		sql += " FROM RECURSO R INNER JOIN RECURSO_PARTE RP ON R.ID_RECURSO = RP.ID_RECURSO INNER JOIN PROC_PARTE PP ON RP.ID_PROC_PARTE = PP.ID_PROC_PARTE ";
		sql += " WHERE RP.DATA_BAIXA           IS NULL ";
		sql += " AND PP.ID_PROC_PARTE_TIPO = ? ";											ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
		sql += " AND R.ID_PROC                 = P.ID_PROC ";
		sql += " AND ROWNUM                     = 1 ";
		sql += " ) AS PRIMEIRO_PROMOVENTE, ";
		sql += " (SELECT RP.ID_PROC_PARTE ";
		sql += "   || '__@--' ";
		sql += "   || PP.NOME ";
		sql += "   || '__@--' ";
		sql += "   || 'Polo Passsivo' ";
		sql += " FROM  RECURSO R INNER JOIN RECURSO_PARTE RP ON R.ID_RECURSO = RP.ID_RECURSO INNER JOIN PROC_PARTE PP ON RP.ID_PROC_PARTE = PP.ID_PROC_PARTE ";
		sql += " WHERE RP.DATA_BAIXA           IS NULL ";
		sql += " AND PP.ID_PROC_PARTE_TIPO = ? ";											ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
		sql += " AND R.ID_PROC                 = P.ID_PROC ";
		sql += " AND ROWNUM                     = 1 ";
		sql += " ) AS PRIMEIRO_PROMOVIDO ";
		sql += " FROM PROJUDI.PROC p  " ;
		sql += " INNER JOIN PROJUDI.RECURSO r ON r.ID_PROC = p.ID_PROC AND r.DATA_RETORNO IS NULL" ;
		sql += " LEFT JOIN PROJUDI.PROC_STATUS ps ON ps.ID_PROC_STATUS = p.ID_PROC_STATUS " ;		
		sql += " LEFT JOIN PROJUDI.PROC_PRIOR pp ON pp.ID_PROC_PRIOR = p.ID_PROC_PRIOR " ;
		sql += " LEFT JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC " ;
		sql += " INNER JOIN PROJUDI.SERV s ON s.ID_SERV = p.ID_SERV" ;
		sql += " WHERE p.ID_SERV = ?"; 	ps.adicionarLong(id_Serventia);		
		
		if(id_Classificador != null && !id_Classificador.equalsIgnoreCase("")) {
			sql += " AND p.ID_CLASSIFICADOR = ?"; 	ps.adicionarLong(id_Classificador);
		}
		if(id_Juiz != null && !id_Juiz.equalsIgnoreCase("")) {
			sql += " AND pr.ID_SERV_CARGO = ?"; 		ps.adicionarLong(id_Juiz);
		}
		sql += " AND ps.PROC_STATUS_CODIGO = ?)"; ps.adicionarLong(ProcessoStatusDt.ATIVO);

		sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
		
		try{
			//foi criada limitação de 500 registros para evitar o travamento do navegador
			//em casos onde há muitos processos vinculados ao classificador.
			ps.getValoresLog();
			rs1 = consultarLimitacaoRegistros(sql, ps, 500);
			listaProcessos = getListaProcessos(rs1, false);

		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}

	/**
	 * Método que realiza a consulta de Processos baseando-se nos dados do Advogado.
	 * @param statusProcessoCodigo - status do processo
	 * @param idServentia - ID da Serventia 
	 * @param oabNumero - número OAB do advogado
	 * @param oabComplemento - complemento da OAB do advogado
	 * @param oabUf - uf da OAB do advogado
	 * @param posicaoPaginaAtual - posição da página atual, para paginação
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosDadosAdvogado(String statusProcessoCodigo, String idServentia, String oabNumero, String oabComplemento, String oabUf, String situacaoAdvogadoProcesso, String posicaoPaginaAtual) throws Exception {

		StringBuffer sql = new StringBuffer();
		String sqlCont = null;
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ProcessoDt processoDt;
		
		String sqlCamposConsulta = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DATA_RECEBIMENTO, ps.PROC_STATUS_CODIGO, p.ANO, " + "rec.ID_RECURSO, p.ID_SERV, p.SEGREDO_JUSTICA, p.DIGITO_VERIFICADOR, p.ID_PROC_PRIOR, s.Serv ";		
		sqlCamposConsulta += ", NULL AS ID_PROC_PARTE, NULL AS NOME, NULL AS PROC_PARTE_TIPO_CODIGO ";
		sqlCamposConsulta += " ,(SELECT PP.ID_PROC_PARTE ";
		sqlCamposConsulta += "   || '__@--' ";
		sqlCamposConsulta += "   || PP.NOME ";
		sqlCamposConsulta += "   || '__@--' ";
		sqlCamposConsulta += "   || 'Polo Ativo' ";
		sqlCamposConsulta += " FROM PROC_PARTE PP ";
		sqlCamposConsulta += " WHERE PP.DATA_BAIXA           IS NULL ";
		sqlCamposConsulta += " AND PP.ID_PROC_PARTE_TIPO = 2 ";										//	ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);
		sqlCamposConsulta += " AND PP.ID_PROC                 = P.ID_PROC ";
		sqlCamposConsulta += " AND ROWNUM                     = 1 ";
		sqlCamposConsulta += " ) AS PRIMEIRO_PROMOVENTE, ";
		sqlCamposConsulta += " (SELECT PP.ID_PROC_PARTE ";
		sqlCamposConsulta += "   || '__@--' ";
		sqlCamposConsulta += "   || PP.NOME ";
		sqlCamposConsulta += "   || '__@--' ";
		sqlCamposConsulta += "   || 'Polo Passivo' ";
		sqlCamposConsulta += " FROM PROC_PARTE PP ";
		sqlCamposConsulta += " WHERE PP.DATA_BAIXA           IS NULL ";
		sqlCamposConsulta += " AND PP.ID_PROC_PARTE_TIPO = 3 ";										//	ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
		sqlCamposConsulta += " AND PP.ID_PROC                 = P.ID_PROC ";
		sqlCamposConsulta += " AND ROWNUM                     = 1 ";
		sqlCamposConsulta += " ) AS PRIMEIRO_PROMOVIDO, ";
		sqlCamposConsulta += " (SELECT RP.ID_PROC_PARTE ";
		sqlCamposConsulta += "   || '__@--' ";
		sqlCamposConsulta += "   || PP.NOME ";
		sqlCamposConsulta += "   || '__@--' ";
		sqlCamposConsulta += "   || 'Polo Ativo' ";
		sqlCamposConsulta += " FROM RECURSO R INNER JOIN RECURSO_PARTE RP ON R.ID_RECURSO = RP.ID_RECURSO INNER JOIN PROC_PARTE PP ON RP.ID_PROC_PARTE = PP.ID_PROC_PARTE ";
		sqlCamposConsulta += " WHERE RP.DATA_BAIXA           IS NULL ";
		sqlCamposConsulta += " AND PP.ID_PROC_PARTE_TIPO = 2  ";									 //	ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_ATIVO);		
		sqlCamposConsulta += " AND R.ID_PROC                 = P.ID_PROC ";
		sqlCamposConsulta += " AND ROWNUM                     = 1 ";
		sqlCamposConsulta += " ) AS PRIMEIRO_RECORRENTE, ";
		sqlCamposConsulta += " (SELECT RP.ID_PROC_PARTE ";
		sqlCamposConsulta += "   || '__@--' ";
		sqlCamposConsulta += "   || PP.NOME ";
		sqlCamposConsulta += "   || '__@--' ";
		sqlCamposConsulta += "   || 'Polo Passivo' ";
		sqlCamposConsulta += " FROM RECURSO R INNER JOIN RECURSO_PARTE RP ON R.ID_RECURSO = RP.ID_RECURSO INNER JOIN PROC_PARTE PP ON RP.ID_PROC_PARTE = PP.ID_PROC_PARTE ";
		sqlCamposConsulta += " WHERE RP.DATA_BAIXA           IS NULL ";
		sqlCamposConsulta += " AND PP.ID_PROC_PARTE_TIPO = 3 ";										//	ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
		sqlCamposConsulta += " AND R.ID_PROC                 = P.ID_PROC ";
		sqlCamposConsulta += " AND ROWNUM                     = 1 ";
		sqlCamposConsulta += " ) AS PRIMEIRO_RECORRIDO ";	

		sql.append(" FROM PROJUDI.PROC_PARTE_ADVOGADO pa "
				+ "JOIN PROJUDI.PROC_PARTE pp on pa.ID_PROC_PARTE = pp.ID_PROC_PARTE AND pp.DATA_BAIXA IS NULL " 
		        + "JOIN PROJUDI.PROC p on p.ID_PROC = pp.ID_PROC "
				+ "JOIN PROJUDI.USU_SERV_OAB uso on uso.ID_USU_SERV = pa.ID_USU_SERV "
				+ "JOIN PROJUDI.USU_SERV us on us.ID_USU_SERV = uso.ID_USU_SERV "
				+ "JOIN PROJUDI.SERV s on s.ID_SERV = p.ID_SERV "
				+ "JOIN PROJUDI.SERV servAdv on servAdv.ID_SERV = us.ID_SERV "
				+ "JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS "
				+ "LEFT JOIN PROJUDI.RECURSO rec on (rec.ID_PROC = p.ID_PROC AND rec.DATA_RETORNO IS NULL AND rec.DATA_RECEBIMENTO IS NOT NULL) ");

					
		//os Campos oabNumero, OAB_COMPLEMENTO e ID_ESTADO_REPRESENTACAO são obrigatórios
		sql.append(" WHERE uso.OAB_NUMERO = ? ");				ps.adicionarLong(oabNumero);				
		sql.append(" AND uso.OAB_COMPLEMENTO like ?");			ps.adicionarString(oabComplemento);			
		sql.append(" AND servAdv.ID_ESTADO_REPRESENTACAO = ?");		ps.adicionarLong(oabUf);
		
		
		if(situacaoAdvogadoProcesso != null && situacaoAdvogadoProcesso.equals("1")){
			//Se o parâmetro for 1, consulta os processos nos quais o advogado está ativo
			sql.append(" AND pa.DATA_SAIDA IS NULL");
		}
		else if(situacaoAdvogadoProcesso != null && situacaoAdvogadoProcesso.equals("2")) {
			//Se o parâmetro for 2, consulta os processos nos quais o advogado está inativo
			sql.append(" AND pa.DATA_SAIDA IS NOT NULL");
		}
		//Se o parâmetro acima for 3 ou qualquer outro valor diferente de 1 e de 2, não
		//adiciona nenhum critério na consulta para que retorne tanto os ativos quanto
		//os inativos.
					
		if (statusProcessoCodigo != null && !statusProcessoCodigo.equals("")) {					
			sql.append(" AND ps.PROC_STATUS_CODIGO = ?");								ps.adicionarLong(statusProcessoCodigo);			
		}else{
			sql.append(" AND ps.PROC_STATUS_CODIGO = ?");									ps.adicionarLong(ProcessoStatusDt.ATIVO);			
		}
		
		if (idServentia != null && !idServentia.equals("")) {				
			sql.append(" AND p.ID_SERV = ?");											ps.adicionarLong(idServentia);
		}

		sql.append(" ORDER BY p.ID_PROC_PRIOR desc, p.DATA_RECEBIMENTO");
		
		try{
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sqlCamposConsulta + sql.toString(), ps, posicaoPaginaAtual);
			
			while (rs1.next()) {
				//Criando objeto do tipo processo
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				
				ProcessoParteDt primeiroPromovente 	= ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE"));
				ProcessoParteDt primeiroPromovido 	= ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));		
		
				if(primeiroPromovente!=null)
					processoDt.addListaPoloAtivo(primeiroPromovente);
				
				if(primeiroPromovido!=null)
				processoDt.addListaPolosPassivos(primeiroPromovido);
				
				
				listaProcessos.add(processoDt);
			}

			//Conta registros
			sqlCont = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE " + sql.toString();
			rs2 = consultar(sqlCont, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}

	
	/**
	 * Método que realiza a consulta de Processos pelo número do inquérito
	 * @param statusProcessoCodigo - status do processo
	 * @param idServentia - ID da Serventia 
	 * @param posicaoPaginaAtual - posição da página atual, para paginação
	 * @return lista de processos
	 * @throws Exception
	 * @author acblmoncao
	 */
	public List consultarProcessosInquerito(String statusProcessoCodigo, String idServentia,  String inquerito, String posicaoPaginaAtual) throws Exception {

		StringBuffer sql = new StringBuffer();
		String sqlCont = null;
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ProcessoDt processoDt;
		
		String sqlCamposConsulta = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DATA_RECEBIMENTO, ps.PROC_STATUS_CODIGO, p.ANO, p.ID_SERV, p.DIGITO_VERIFICADOR, p.ID_PROC_PRIOR, s.Serv , pc.inquerito, P.SEGREDO_JUSTICA, ";
		sqlCamposConsulta += "(SELECT PP.ID_PROC_PARTE || '__@--'   || PP.NOME   || '__@--'  || 'Polo Passivo' FROM PROC_PARTE PP ";
		sqlCamposConsulta += " WHERE PP.ID_PROC = P.ID_PROC AND PP.ID_PROC_PARTE_TIPO = 3 AND ROWNUM = 1) AS PRIMEIRO_PROMOVIDO";
		sql.append(" FROM PROC_CRIMINAL PC INNER JOIN PROC P ON P.ID_PROC = PC.ID_PROC INNER JOIN SERV S ON S.ID_SERV = P.ID_SERV "
		           +" INNER JOIN PROC_STATUS PS ON P.ID_PROC_STATUS = PS.ID_PROC_STATUS "
		           + " WHERE PC.INQUERITO = ? "); ps.adicionarLong(inquerito);
		
		//Se o parâmetro acima for 3 ou qualquer outro valor diferente de 1 e de 2, não
		//adiciona nenhum critério na consulta para que retorne tanto os ativos quanto
		//os inativos.
					
		if (statusProcessoCodigo != null && !statusProcessoCodigo.equals("")) {					
			sql.append(" AND ps.PROC_STATUS_CODIGO = ?");								ps.adicionarLong(statusProcessoCodigo);			
		}else{
			sql.append(" AND ps.PROC_STATUS_CODIGO = ?");									ps.adicionarLong(ProcessoStatusDt.ATIVO);			
		}
		
		if (idServentia != null && !idServentia.equals("")) {				
			sql.append(" AND p.ID_SERV = ?");											ps.adicionarLong(idServentia);
		}

		sql.append(" ORDER BY p.ID_PROC_PRIOR desc, p.DATA_RECEBIMENTO");
		
		try{
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sqlCamposConsulta + sql.toString(), ps, posicaoPaginaAtual);
			
			while (rs1.next()) {
				//Criando objeto do tipo processo
				processoDt = new ProcessoDt();
				processoDt.setProcessoCriminalDt(new ProcessoCriminalDt());
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.getProcessoCriminalDt().setInquerito(rs1.getString("INQUERITO"));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setId_Serventia(rs1.getString("ID_SERV"));
				
				ProcessoParteDt primeiroPromovido 	= ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));		
		
				if(primeiroPromovido!=null)
					processoDt.addListaPolosPassivos(primeiroPromovido);
				
				listaProcessos.add(processoDt);
			}

			//Conta registros
			sqlCont = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE " + sql.toString();
			rs2 = consultar(sqlCont, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
		
	
	/**
	 * Consulta os processos onde de uma certidão Negativa/Positiva Cível/Criminal.
	 * 
	 * @param certidaoNegativaPositivaDt
	 * 	Dt da certidão sem a lista de processos.
	 * 
	 * @return retorna uma Lista com os processos da certidão no formato
	 * ProcessoCertidaoPositivaNegativaDt
	 * @author jpcpresa
	 */

	public List consultarProcessosCertidaoPositivaNegativa(CertidaoNegativaPositivaDt certidaoNegativaPositivaDt) throws Exception {
		String sql = "";
		String sqlFiltroCpfCnPj = "";
		String sqlFiltroCpfCnpj2 = "";
		String filtroMaeDataNascimento = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		String nome = certidaoNegativaPositivaDt.getNome();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String cpfCnpj = certidaoNegativaPositivaDt.getCpfCnpj().replaceAll("[/.-]", "");
		if ((cpfCnpj != null) && (cpfCnpj.length() > 0)) if (cpfCnpj.length() == 14) {
			sqlFiltroCpfCnPj = " AND (c.CNPJ = ? OR c.CNPJ IS NULL)";			
			sqlFiltroCpfCnpj2 = " OR (c.CNPJ = ?)";
		} else {
			sqlFiltroCpfCnPj = " AND (c.CPF = ? OR c.CPF IS NULL)";
			sqlFiltroCpfCnpj2 = " OR (c.CPF = ?) ";
		}

		boolean condicaoDataNascimento = certidaoNegativaPositivaDt.getDataNascimento() != null && certidaoNegativaPositivaDt.getDataNascimento().length() > 0;
		boolean condicaoNomeMae = certidaoNegativaPositivaDt.getDataNascimento() != null && certidaoNegativaPositivaDt.getDataNascimento().length() > 0;
		if (condicaoNomeMae && condicaoDataNascimento && cpfCnpj.length() < 13 && !certidaoNegativaPositivaDt.getDataNascimento().isEmpty() && !certidaoNegativaPositivaDt.getNomeMae().isEmpty()) {
			//filtroMaeDataNascimento = " OR ( c.DATA_NASCIMENTO = " + Funcoes.BancoData(certidaoNegativaPositivaDt.getDataNascimento()) + " AND c.NOME_MAE regexp '" + Funcoes.getNomePesquisa(certidaoNegativaPositivaDt.getNomeMae()) + "')";
		}
		//A VALIDAÇÃO DO STATUS DO PROCESSO SERA FEITO NA VIEW
		List processosCertidao = null;
		try{
			sql = "SELECT c.ID_PROC, c.PROC_NUMERO, c.DIGITO_VERIFICADOR, c.SEGREDO_JUSTICA, ";
			sql += " c.DATA_RECEBIMENTO, c.PROC_TIPO, c.ASSUNTO, c.VALOR, c.SERV, c.ID_PROC_PARTE, ";
			sql += " c.NOME, c.NOME_MAE, c.PROC_PARTE_TIPO_CODIGO , c.SEXO, c.CPF, c.CNPJ, c.RG, c.DATA_NASCIMENTO, c.ADVOGADO, ";
			sql += " c.ANO, c.FORUM_CODIGO ";
			sql += " FROM PROJUDI.VIEW_CERTIDAO_NP_DADOS_2 c ";
			sql += " WHERE c.ID_PROC in  (SELECT c.ID_PROC FROM PROJUDI.VIEW_CERTIDAO_NP_DADOS_2 c WHERE c.PROC_PARTE_TIPO_CODIGO = 0 AND  c.AREA_CODIGO = ? AND c.COMARCA_CODIGO = ?";
			
			ps.adicionarLong(Funcoes.StringToLong(certidaoNegativaPositivaDt.getAreaCodigo()));
			ps.adicionarLong(certidaoNegativaPositivaDt.getComarcaCodigo());
			sql += " AND ((c.NOME_SIMPLIFICADO  = ?" + sqlFiltroCpfCnPj + ")" + sqlFiltroCpfCnpj2 + filtroMaeDataNascimento + "))";
			ps.adicionarString(Funcoes.converteNomeSimplificado(nome));
			ps.adicionarLong(cpfCnpj);
			ps.adicionarLong(cpfCnpj);
			//sql += " GROUP BY c.ID_PROC, c.ID_PROC_PARTE, c.ID_ADVOGADO ";
			sql += " ORDER BY c.ID_PROC";
			

			rs1 = this.consultar(sql, ps);

			processosCertidao = getListaProcessoCertidaoNP(rs1);
			
			//Verifica se há débitos do Requerente
			if (certidaoNegativaPositivaDt.isCivel()   ) {
				ps.limpar();
				String sqlCustas = " SELECT c.ID_PROC, c.PROC_NUMERO, c.DIGITO_VERIFICADOR, c.SEGREDO_JUSTICA, ";
				sqlCustas += " c.DATA_RECEBIMENTO, c.PROC_TIPO, c.ASSUNTO, c.VALOR, c.SERV, ";
				sqlCustas += " c.ID_PROC_PARTE_BUSCADA,  c.NOME, c.NOME_MAE, c.SEXO, c.CPF, c.CNPJ, c.RG, ";
				sqlCustas += " c.DATA_NASCIMENTO, c.PROC_PARTE_TIPO_BUSCADA, c.ADVOGADO_BUSCADA,  c.ID_PROC_PARTE_CONTRARIA, c.NOME_PARTE_CONTRARIA, ";
				sqlCustas += " c.ADVOGADO_NOME_PARTE_CONTRARIA, c.ANO, c.FORUM_CODIGO ";
				sqlCustas += " FROM PROJUDI.VIEW_CERTIDAO_NP_DADOS_DEBITO c ";
				sqlCustas += " WHERE c.AREA_CODIGO = ?";		
				ps.adicionarLong(Funcoes.StringToInt(certidaoNegativaPositivaDt.getAreaCodigo()));
				sqlCustas += " AND  c.COMARCA_CODIGO = ? AND ((c.NOME_SIMPLIFICADO  = ? " + sqlFiltroCpfCnPj + ")" + sqlFiltroCpfCnpj2 + filtroMaeDataNascimento + ") ";
				ps.adicionarLong(certidaoNegativaPositivaDt.getComarcaCodigo());
				ps.adicionarString(Funcoes.converteNomeSimplificado(nome));
				ps.adicionarLong(cpfCnpj);
				ps.adicionarLong(cpfCnpj);
				//sqlCustas += " GROUP BY c.ID_PROC, c.ID_PROC_PARTE_BUSCADA, ";
				//sqlCustas += " c.ID_ADVOGADO_CONTRARIA, c.ID_ADVOGADO_PARTE_BUSCADA ";
				sqlCustas += " ORDER BY c.ID_PROC ";
				
				
				//c.NOME  regexp '" + Funcoes.getNomePesquisa(nome) 
				rs1 = this.consultar(sqlCustas, ps);

				processosCertidao.addAll(getListaProcessoCertidaoNPCusta(rs1));
			}


		
		} finally{
			try{
				if (rs1 != null) rs1.close();
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return processosCertidao;
	}

	private List getListaProcessoCertidaoNPCusta(ResultSetTJGO rs1) throws Exception {
		List processosCertidao;
		String id_Processo = "";
		String id_ProcessoPartePromovida = "";
		processosCertidao = new ArrayList();
		ProcessoCertidaoPositivaNegativaDt processoAux;
		while (rs1.next()) {
			processoAux = new ProcessoCertidaoPositivaNegativaDt();
			processoAux.setId_Processo(rs1.getString("ID_PROC"));
			processoAux.setProcessoNumero(rs1.getString("PROC_NUMERO"));
			processoAux.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
			processoAux.setSegredoJustica(rs1.getString("SEGREDO_JUSTICA").equals("1") ? true : false);
			processoAux.setDataRecebimento(rs1.getString("DATA_RECEBIMENTO"));
			processoAux.setProcessoTipo(rs1.getString("PROC_TIPO"));
			processoAux.setValor(rs1.getString("VALOR"));
			processoAux.setServentia(rs1.getString("SERV"));
			processoAux.setPromovidoNome(rs1.getString("NOME"));
			processoAux.setPromovidoNomeMae(rs1.getString("NOME_MAE"));
			processoAux.setPromovidodaSexo(rs1.getString("SEXO"));
			processoAux.setPromovidoCpf(rs1.getString("CPF"));
			processoAux.setPromovidoCnpj(rs1.getString("CNPJ"));
			processoAux.setPromovidoDataNascimento(rs1.getString("DATA_NASCIMENTO"));
			processoAux.setPromovidoRg(rs1.getString("RG"));
			processoAux.setAno(rs1.getString("ANO"));
			processoAux.setForumCodigo(rs1.getString("FORUM_CODIGO"));
			int parteTipo = rs1.getString("PROC_PARTE_TIPO_BUSCADA").equals("PROMOVIDO") ? ProcessoParteTipoDt.POLO_PASSIVO_CODIGO : ProcessoParteTipoDt.POLO_ATIVO_CODIGO;
			processoAux.setBuscadaProcessoParteTipo(parteTipo);

			if (rs1.getString("ADVOGADO_BUSCADA") != null) processoAux.addPromovidoAdvogado(rs1.getString("ADVOGADO_BUSCADA"));
			processoAux.addPromovente(rs1.getString("NOME_PARTE_CONTRARIA"));

			if (rs1.getString("ADVOGADO_NOME_PARTE_CONTRARIA") != null) processoAux.addPromoventeAdvogado(rs1.getString("ADVOGADO_NOME_PARTE_CONTRARIA"));
			id_Processo = rs1.getString("ID_PROC");

			if (rs1.getString("ASSUNTO") != null) processoAux.addAssunto(rs1.getString("ASSUNTO"));
			id_Processo = rs1.getString("ID_PROC");
			id_ProcessoPartePromovida = rs1.getString("ID_PROC_PARTE_BUSCADA");
			while (rs1.next() && rs1.getString("ID_PROC").equals(id_Processo) && rs1.getString("ID_PROC_PARTE_BUSCADA").equals(id_ProcessoPartePromovida)) {

				if (rs1.getString("ADVOGADO_BUSCADA") != null) processoAux.addPromovidoAdvogado(rs1.getString("ADVOGADO_BUSCADA"));
				processoAux.addPromovente(rs1.getString("NOME_PARTE_CONTRARIA"));

				if (rs1.getString("ADVOGADO_NOME_PARTE_CONTRARIA") != null) processoAux.addPromovente(rs1.getString("ADVOGADO_NOME_PARTE_CONTRARIA"));

				if (rs1.getString("ASSUNTO") != null) processoAux.addAssunto(rs1.getString("ASSUNTO"));
			}
			processosCertidao.add(processoAux);
			if (rs1.isAfterLast()) {
				break;
			} else {
				rs1.previous();
			}
		}
		return processosCertidao;
	}

		
	private List getListaProcessoCertidaoNP(ResultSetTJGO rs1) throws Exception {
		List processosCertidao;
		String id_Processo;
		processosCertidao = new ArrayList();
		ProcessoCertidaoPositivaNegativaDt processoAux;
		
				
		while (rs1.next()) {
			processoAux = new ProcessoCertidaoPositivaNegativaDt();
			processoAux.setId_Processo(rs1.getString("ID_PROC"));
			processoAux.setProcessoNumero(rs1.getString("PROC_NUMERO"));
			processoAux.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
			processoAux.setSegredoJustica(rs1.getString("SEGREDO_JUSTICA").equals("1") ? true : false);
			processoAux.setDataRecebimento(rs1.getString("DATA_RECEBIMENTO"));
			processoAux.setProcessoTipo(rs1.getString("PROC_TIPO"));
			processoAux.setValor(rs1.getString("VALOR"));
			processoAux.setServentia(rs1.getString("SERV"));
			processoAux.setAno(rs1.getString("ANO"));
			processoAux.setForumCodigo(rs1.getString("FORUM_CODIGO"));
			processoAux.setBuscadaProcessoParteTipo(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
			if (rs1.getString("ASSUNTO") != null) processoAux.addAssunto(rs1.getString("ASSUNTO"));
			
			// 0 promovido
			// 1 promovente
			if (rs1.getString("PROC_PARTE_TIPO_CODIGO").equals("0")) {
						
			processoAux.setPromovidoNome(rs1.getString("NOME"));
			processoAux.setPromovidoNomeMae(rs1.getString("NOME_MAE"));
			processoAux.setPromovidodaSexo(rs1.getString("SEXO"));
			processoAux.setPromovidoCpf(rs1.getString("CPF"));
			processoAux.setPromovidoCnpj(rs1.getString("CNPJ"));
			processoAux.setPromovidoDataNascimento(rs1.getString("DATA_NASCIMENTO"));
			processoAux.setPromovidoRg(rs1.getString("RG"));
			if (rs1.getString("ADVOGADO") != null) processoAux.addPromovidoAdvogado(rs1.getString("ADVOGADO"));

			} else {
				
				processoAux.addPromovente(rs1.getString("NOME"));
				if (rs1.getString("ADVOGADO") != null) processoAux.addPromoventeAdvogado(rs1.getString("ADVOGADO"));
			
			}
				
			id_Processo = rs1.getString("ID_PROC");
			while (rs1.next() && rs1.getString("ID_PROC").equals(id_Processo)) {

				if (rs1.getString("PROC_PARTE_TIPO_CODIGO").equals("0")) {
					processoAux.setPromovidoNome(rs1.getString("NOME"));
					processoAux.setPromovidoNomeMae(rs1.getString("NOME_MAE"));
					processoAux.setPromovidodaSexo(rs1.getString("SEXO"));
					processoAux.setPromovidoCpf(rs1.getString("CPF"));
					processoAux.setPromovidoCnpj(rs1.getString("CNPJ"));
					processoAux.setPromovidoDataNascimento(rs1.getString("DATA_NASCIMENTO"));
					processoAux.setPromovidoRg(rs1.getString("RG"));
					if (rs1.getString("ADVOGADO") != null) processoAux.addPromovidoAdvogado(rs1.getString("ADVOGADO"));
					
				} else {
					processoAux.addPromovente(rs1.getString("NOME"));
					if (rs1.getString("ADVOGADO") != null) processoAux.addPromoventeAdvogado(rs1.getString("ADVOGADO"));
				}
				if (rs1.getString("ASSUNTO") != null) processoAux.addAssunto(rs1.getString("ASSUNTO"));
			}
			processosCertidao.add(processoAux);
			if (rs1.isAfterLast()) {
				break;
			} else {
				rs1.previous();
			}
		}
		return processosCertidao;
	}
	
	
	
	

	/**
	 * Consulta os dados para montar o número completo do processo
	 * @param idProcesso: identificação do processo
	 * @return String numeroCompleto.
	 * @throws Exception
	 * @author wcsilva
	 */
	public String consultarNumeroCompletoDoProcesso(String idProcesso) throws Exception {
		String numeroCompleto = "";
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			sql = "SELECT p.ANO, p.DIGITO_VERIFICADOR, p.PROC_NUMERO, p.FORUM_CODIGO FROM PROJUDI.VIEW_PROC p" + " WHERE p.ID_PROC = ?";
			ps.adicionarLong(idProcesso);			

			rs = consultar(sql, ps);
			while (rs.next()) {
				numeroCompleto = Funcoes.completarZeros(rs.getString("PROC_NUMERO"), 7) + "-" + Funcoes.completarZeros(rs.getString("DIGITO_VERIFICADOR"), 2) + "." + rs.getString("ANO") + "." + Configuracao.JTR + "." + Funcoes.completarZeros(rs.getString("FORUM_CODIGO"), 4);
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return numeroCompleto;
	}

	/**
	 * Método que consulta os Processos Sem Movimentação dentro do período informado, podendo ser informado ou não o Id da Serventia.
	 * @param idServentia - Id da Serventia
	 * @param opcaoPeriodoSemMovimentacao - opção de período de dias selecionada
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos sem movimentação
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosSemMovimentacaoServentia(String idServentia, String idJuiz, String opcaoPeriodoSemMovimentacao, String posicaoPaginaAtual) throws Exception {
		String sqlQuantidade = "";
		String sqlLista = "";
		String sqlTemp = "";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO(); 

		sqlTemp = " FROM PROJUDI.VIEW_PROCS_SEM_MOVI V";
		
		if(idJuiz != null && !idJuiz.equalsIgnoreCase("") && !idJuiz.equalsIgnoreCase("null")) {
			sqlTemp += 	" INNER JOIN PROJUDI.PROC_RESP PR ON PR.ID_PROC = V.ID_PROC AND PR.CODIGO_TEMP = 0 AND PR.ID_SERV_CARGO = ? ";
			ps.adicionarLong(idJuiz);
		}
		if (opcaoPeriodoSemMovimentacao != null) {
			if(Funcoes.StringToInt(opcaoPeriodoSemMovimentacao) >= 20) {
				sqlTemp += " WHERE V.DIAS >= ?";
			} else {
				sqlTemp += " WHERE V.DIAS <= ?";
			}
			ps.adicionarLong(opcaoPeriodoSemMovimentacao);
		}
		if (idServentia != null && !idServentia.equals("") && !idServentia.equals("null")) {
			sqlTemp += " AND V.ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}
		sqlLista += "SELECT * " + sqlTemp;
		sqlLista += " ORDER BY V.DATA_REALIZACAO ";
		
		try{
			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ProcessoSemMovimentacaoDt obTemp = new ProcessoSemMovimentacaoDt();
				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				obTemp.setDataUltimaMovimentacao(Funcoes.FormatarData(rs1.getDateTime("DATA_REALIZACAO")));
				obTemp.setComplementoMovimentacao(rs1.getString("COMPLEMENTO"));
				listaProcessos.add(obTemp);
			}
			sqlQuantidade = "SELECT COUNT(1) AS QUANTIDADE " + sqlTemp;
			rs2 = consultar(sqlQuantidade, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) 
					rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	public String consultarProcessosSemMovimentacaoServentiaJSON(String idServentia, String idJuiz, String opcaoPeriodoSemMovimentacao, String posicaoPaginaAtual) throws Exception {

		String sqlQuantidade = "";
		String sqlLista="";
		String sqlTemp="";
		String stTemp="";
		int qtdeColunas = 3;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sqlTemp = " FROM PROJUDI.VIEW_PROCS_SEM_MOVI V";
		
		if(idJuiz != null && !idJuiz.equalsIgnoreCase("") && !idJuiz.equalsIgnoreCase("null")) {
			sqlTemp += 	" INNER JOIN PROJUDI.PROC_RESP PR ON PR.ID_PROC = V.ID_PROC AND PR.CODIGO_TEMP = 0 AND PR.ID_SERV_CARGO = ? ";
			ps.adicionarLong(idJuiz);
		}
		if (opcaoPeriodoSemMovimentacao != null) {
			if(Funcoes.StringToInt(opcaoPeriodoSemMovimentacao) >= 20) {
				sqlTemp += " WHERE V.DIAS >= ?";
			} else {
				sqlTemp += " WHERE V.DIAS <= ?";
			}
			ps.adicionarLong(opcaoPeriodoSemMovimentacao);
		}
		if (idServentia != null && !idServentia.equals("") && !idServentia.equals("null")) {
			sqlTemp += " AND V.ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}
		sqlLista += "SELECT V.ID_PROC as id, (PROC_NUMERO ||'.'|| DIGITO_VERIFICADOR) as descricao1, to_char(DATA_REALIZACAO,'DD/MM/YYYY')  as descricao2, COMPLEMENTO as descricao3 " + sqlTemp;
		sqlLista += " ORDER BY V.DATA_REALIZACAO ";
		
		
		
		
		try{

			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			sqlQuantidade = "SELECT COUNT(1) AS QUANTIDADE " + sqlTemp;
			rs2 = consultar(sqlQuantidade, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);

		} finally {
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return stTemp; 
	}

	/**
	 * Método que consulta processos informando também o tempo de vida de cada um, filtrando por ProcessoTipo.
	 * @param idServentia - id da serventia 
	 * @param idProcessoTipo - id do tipo de processo 
	 * @param opcaoPeriodo - opção de tempo de distribuição
	 * @param posicaoPaginaAtual - página atual da paginação
	 * @return lista JSON contendo os processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarProcessosTempoVidaPorTipoJSON(boolean desemb, String idServentia, String opcaoPeriodo, String posicaoPaginaAtual) throws Exception {

		String sqlQuantidade = "";
		String sqlLista="";
		String sqlTemp="";
		String stTemp="";
		int qtdeColunas = 6;
		ResultSetTJGO rs1=null;
		ResultSetTJGO rs2=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();

		sqlTemp = " FROM PROJUDI.VIEW_RELATORIO_META_02_2017 V";
		
		if (opcaoPeriodo != null) {
			if(Funcoes.StringToInt(opcaoPeriodo) >= 20) {
				sqlTemp += " WHERE V.TEMPO >= ?";
				ps.adicionarLong(opcaoPeriodo);
			} else {
				sqlTemp += " WHERE V.TEMPO <= 20";
			}

		}
		
		if(desemb == true){
			if(idServentia != null && !idServentia.equalsIgnoreCase("") && !idServentia.equalsIgnoreCase("null")) {
				sqlTemp += 	" AND V.ID_USU_SERV = ? ";
				ps.adicionarLong(idServentia);
			}		
		}else{
			if(idServentia != null && !idServentia.equalsIgnoreCase("") && !idServentia.equalsIgnoreCase("null")) {
				sqlTemp += 	" AND V.ID_SERV = ? ";
				ps.adicionarLong(idServentia);
			}			
		}

		sqlLista += "SELECT ID_PROC as id, PROC_NUMERO as descricao1, to_char(ACEITELIMINAR,'DD/MM/YYYY') as descricao2, SERV as descricao3, GABINETE as descricao4, NOME as descricao5, TEMPO as descricao6 " + sqlTemp;
		sqlLista += " ORDER BY TEMPO DESC ";
		
		try{

			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			sqlQuantidade = "SELECT COUNT(1) AS QUANTIDADE " + sqlTemp;
			rs2 = consultar(sqlQuantidade, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicaoPaginaAtual, rs1, qtdeColunas);

		} finally {
			try{ 
				if (rs1 != null)rs1.close();
			} 
			catch(Exception e) 
			{}
			
			try{
				if (rs2 != null) rs2.close();
			} 
			catch(Exception e) 
			{}
		}
		return stTemp; 
	}
	
	
	
	/**
	 * Método que consulta processos informando também o tempo de vida de cada um, filtrando por ProcessoTipo.
	 * @param idServentia - id da serventia 
	 * @param idProcessoTipo - id do tipo de processo 
	 * @param opcaoPeriodo - opção de tempo de distribuição
	 * @param posicaoPaginaAtual - página atual da paginação
	 * @return lista JSON contendo os processos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosTempoVidaTipoRelatorio(boolean desemb, String idServentia, String opcaoPeriodo) throws Exception {

		String sqlLista="";
		String sqlTemp="";
		String stTemp="";
		ResultSetTJGO rs=null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		List listaProcessos = new ArrayList();


		sqlTemp = " FROM PROJUDI.VIEW_RELATORIO_META_02_2017 V";
		
		if (opcaoPeriodo != null) {
			if(Funcoes.StringToInt(opcaoPeriodo) >= 20) {
				sqlTemp += " WHERE V.TEMPO >= ?";
				ps.adicionarLong(opcaoPeriodo);
			} else {
				sqlTemp += " WHERE V.TEMPO <= 20";
			}

		}
		
		if(desemb == true){
			if(idServentia != null && !idServentia.equalsIgnoreCase("") && !idServentia.equalsIgnoreCase("null")) {
				sqlTemp += 	" AND V.ID_USU_SERV = ? ";
				ps.adicionarLong(idServentia);
			}		
		}else{
			if(idServentia != null && !idServentia.equalsIgnoreCase("") && !idServentia.equalsIgnoreCase("null")) {
				sqlTemp += 	" AND V.ID_SERV = ? ";
				ps.adicionarLong(idServentia);
			}			
		}

		sqlLista += "SELECT ID_PROC, PROC_NUMERO, ACEITELIMINAR, SERV, GABINETE, NOME, TEMPO " + sqlTemp;
		sqlLista += " ORDER BY SERV, NOME, TEMPO DESC ";
		
		try{
			
			rs = consultar(sqlLista, ps);
			while (rs.next()) {
				RelatorioLiminarDeferidaDt obTemp = new RelatorioLiminarDeferidaDt();
				obTemp.setGabinete(rs.getString("GABINETE"));
				obTemp.setServ(rs.getString("SERV"));
				obTemp.setProc_numero(rs.getString("PROC_NUMERO"));
				obTemp.setNome(rs.getString("NOME"));
				obTemp.setAceiteLiminar(rs.getDateTime("ACEITELIMINAR"));
				obTemp.setTempo(rs.getString("TEMPO"));

				listaProcessos.add(obTemp);
			}
			

		} finally {
			try{ 
				if (rs != null)rs.close();
			} 
			catch(Exception e) 
			{}

		}
		return listaProcessos; 
	}
	

	/**
	 * 
	 * Método que consulta os Processos aguardando audiência, podendo ser informado ou não o Id da Serventia.
	 * @param idServentia - Id da Serventia
	 * @param tipoAudiencia - tipo de audiência selecionada
	 * @param opcaoPeriodo - opção de tempo selecionada
	 * @param aPartirDa - identifica se a busca será a partir de uma última audiência ou não
	 * @param tipoAudienciaAnterior - identifica qual será o tipo de audiência anterior
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos aguardando audiência
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessosAguardandoAudiencia(String idServentia, String tipoAudiencia, String opcaoPeriodo, String aPartirDa, String tipoAudienciaAnterior, String posicaoPaginaAtual) throws Exception {
		String sqlQuantidade = "";
		String sqlLista = "";
		String sqlTemp00 = "";

		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		//PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sqlTemp00 = " FROM PROJUDI.VIEW_PROCS_PARALISADOS p ";
//		sqlTemp00 += " WHERE p.PendenciaTipoCodigo NOT IN (" + PendenciaTipoDt.AGUARDANDO_CUMPRIMENTO_PENA + "," + PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO + "," + PendenciaTipoDt.AGUARDANDO_PRAZO_DECADENCIAL + "," + PendenciaTipoDt.CONCLUSO_DECISAO + "," + PendenciaTipoDt.CONCLUSO_DESPACHO + "," + PendenciaTipoDt.CONCLUSO_GENERICO + "," + PendenciaTipoDt.CONCLUSO_SENTENCA + "," + PendenciaTipoDt.CONCLUSO_DECISAO_PRESIDENTE + "," + PendenciaTipoDt.CONCLUSO_DECISAO_RELATOR + ","
//				+ PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE + "," + PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR + "," + PendenciaTipoDt.SUSPENSAO_PROCESSO + "," + PendenciaTipoDt.PEDIDO_MANIFESTACAO + ")";
//
//		if (idServentia != null && !idServentia.equals("") && !idServentia.equals("null")) {
//			sqlTemp00 += " AND ID_SERV = " + idServentia;
//		}
//
//		if (opcaoPeriodo != null && !opcaoPeriodo.equals("") && !opcaoPeriodo.equals("null")) {
//			String dataPesquisa = Funcoes.dateToStringSoData(new Date());
//			dataPesquisa = Funcoes.somaData(dataPesquisa, Funcoes.StringToInt(opcaoPeriodo) * -1);
//			sqlTemp00 += " AND DATA_INICIO < " + Funcoes.BancoData(dataPesquisa);
//		}

		sqlLista = "SELECT * " + sqlTemp00;
		sqlLista += " ORDER BY p.DATA_INICIO,p.PROC_NUMERO";
		
		try{			
			rs1 = consultarPaginacaoSemParametros(sqlLista, posicaoPaginaAtual);
			while (rs1.next()) {
				ProcessoAudienciaDt obTemp = new ProcessoAudienciaDt();
				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				
				obTemp.setArea("Criminal");
				//corrigir esse
				obTemp.setTipoAudiencia(rs1.getString("PEND_TIPO"));
				
				String dataInicio = Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO"));
				obTemp.setDataRecebimento(dataInicio);
				
				obTemp.setDataAgendada("18/09/2010");
				
				long[] diferencaDatas = Funcoes.diferencaDatas(new Date(), Funcoes.StringToDate(dataInicio));
				obTemp.setQuantidadeDiasParalisados(String.valueOf(diferencaDatas[0]) + " dias");
				listaProcessos.add(obTemp);
			}
			sqlQuantidade = "SELECT COUNT(p.ID_PROC) AS QUANTIDADE " + sqlTemp00;
			rs2 = consultarSemParametros(sqlQuantidade);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	/**
	 * 
	 * Método que consulta os Processos que não possuem audiência agendada.
	 * @param idServentia - Id da Serventia
	 * @param opcaoPeriodo - opção de tempo selecionada
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */	
	public List consultarProcessosSemAudienciaDias(String idServentia, String idJuiz, String opcaoPeriodo, String posicaoPaginaAtual) throws Exception {
		String sqlQuantidade = "";
		String sql = "";
		String sqlLista = "";
		
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = 	" FROM PROC p " +
				"	INNER JOIN SERV s on p.ID_SERV = s.ID_SERV " +
				"	LEFT JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC" +
				"	WHERE s.ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		sql += 	"		AND p.DATA_ARQUIVAMENTO IS NULL ";
		if(idJuiz != null && !idJuiz.equalsIgnoreCase("") && !idJuiz.equalsIgnoreCase("null")) {
			sql += 	"	AND pr.ID_SERV_CARGO = ?";
			ps.adicionarLong(idJuiz);
		}
		sql +=	"		AND s.ID_SERV_SUBTIPO IN (?,?,?,?,?,?) " +
				"		AND (p.DATA_RECEBIMENTO + ?) <= ? " +
				"		AND p.ID_PROC NOT IN (" +
				"			SELECT ID_PROC FROM AUDI_PROC ap " +
				"				INNER JOIN AUDI a ON a.ID_AUDI=ap.ID_AUDI " +
				"				WHERE ap.ID_PROC=p.ID_PROC) " +
				"					AND p.ID_PROC NOT IN ( SELECT m.ID_PROC FROM MOVI m " +
				"												WHERE m.ID_MOVI_TIPO IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)) ";
		ps.adicionarLong(1);
		ps.adicionarLong(2);
		ps.adicionarLong(3);
		ps.adicionarLong(7);
		ps.adicionarLong(8);
		ps.adicionarLong(9);
		ps.adicionarLong(opcaoPeriodo);
		ps.adicionarDateTime(new Date());		
		ps.adicionarLong(245);
		ps.adicionarLong(247);
		ps.adicionarLong(249);
		ps.adicionarLong(250);
		ps.adicionarLong(273);
		ps.adicionarLong(274);
		ps.adicionarLong(275);
		ps.adicionarLong(276);
		ps.adicionarLong(277);
		ps.adicionarLong(308);
		ps.adicionarLong(316);
		ps.adicionarLong(317);
		ps.adicionarLong(318);
		ps.adicionarLong(322);
		ps.adicionarLong(323);
		ps.adicionarLong(324);
		ps.adicionarLong(325);
		ps.adicionarLong(326);
		ps.adicionarLong(327);
		ps.adicionarLong(328);
		ps.adicionarLong(329);
		ps.adicionarLong(330);
		ps.adicionarLong(331);
		ps.adicionarLong(332);
		ps.adicionarLong(333);
		ps.adicionarLong(334);
		ps.adicionarLong(344);
		ps.adicionarLong(352);
		ps.adicionarLong(368);
		ps.adicionarLong(377);
		ps.adicionarLong(378);
		ps.adicionarLong(379);
		ps.adicionarLong(380);
		ps.adicionarLong(381);
		ps.adicionarLong(382);
		
		sqlLista = "Select p.ID_AREA, s.SERV, p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.DATA_RECEBIMENTO, '' as DATA_AGENDADA, TRUNC(SYSDATE - p.DATA_RECEBIMENTO) as DIAS " + sql;
		sqlLista +=" ORDER BY DIAS desc ";
		
		try{
			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ProcessoAudienciaDt obTemp = new ProcessoAudienciaDt();
				obTemp.setArea(rs1.getString("ID_AREA"));
				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				
				String dataRecebimento = Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO"));
				obTemp.setDataRecebimento(dataRecebimento);
				
				obTemp.setDataAgendada("-");
				
				obTemp.setQuantidadeDiasParalisados(rs1.getString("DIAS") + " dias");
				
				listaProcessos.add(obTemp);
			}
			sqlQuantidade = "SELECT COUNT(p.ID_PROC) AS QUANTIDADE " + sql;
			rs2 = consultar(sqlQuantidade, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	
	/**
	 * 
	 * Método que consulta os Processos que estão aguardando a realização de audiência.
	 * @param idServentia - Id da Serventia
	 * @param tipoAudiencia - tipo de audiência selecionada
	 * @param opcaoPeriodo - opção de tempo selecionada
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos
	 * @throws Exception
	 * @author hmgodinho
	 */	
	public List consultarProcessosAudienciaMarcadaDias(String idServentia, String idJuiz, String tipoAudiencia, String opcaoPeriodo, String posicaoPaginaAtual) throws Exception {
		String sqlQuantidade = "";
		String sql = "";
		String sqlLista = "";
		
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = 	" FROM AUDI a " +
				"	INNER JOIN AUDI_PROC ap on a.ID_AUDI=ap.ID_AUDI " +
				"	INNER JOIN PROC p on ap.ID_PROC=p.ID_PROC " +
				"	INNER JOIN SERV s on p.ID_SERV=s.ID_SERV " +
				"	LEFT JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC" +
				" WHERE a.DATA_MOVI is null " +
				"	AND s.ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		if(idJuiz != null && !idJuiz.equalsIgnoreCase("") && !idJuiz.equalsIgnoreCase("null")) {
			sql += 	"	AND pr.ID_SERV_CARGO = ?";
			ps.adicionarLong(idJuiz);
		}
		sql +=	"	AND (a.ID_AUDI_TIPO = ?) ";
		ps.adicionarLong(tipoAudiencia);
		sql +=	"	AND (p.DATA_RECEBIMENTO + ?) <= ? ";
		ps.adicionarLong(opcaoPeriodo);
		ps.adicionarDateTime(new Date());
		sql +=		"	AND p.ID_PROC NOT IN ( SELECT m.ID_PROC FROM MOVI m " +
				"								WHERE m.ID_MOVI_TIPO IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))";
		
		ps.adicionarLong(245);
		ps.adicionarLong(247);
		ps.adicionarLong(249);
		ps.adicionarLong(250);
		ps.adicionarLong(273);
		ps.adicionarLong(274);
		ps.adicionarLong(275);
		ps.adicionarLong(276);
		ps.adicionarLong(277);
		ps.adicionarLong(308);
		ps.adicionarLong(316);
		ps.adicionarLong(317);
		ps.adicionarLong(318);
		ps.adicionarLong(322);
		ps.adicionarLong(323);
		ps.adicionarLong(324);
		ps.adicionarLong(325);
		ps.adicionarLong(326);
		ps.adicionarLong(327);
		ps.adicionarLong(328);
		ps.adicionarLong(329);
		ps.adicionarLong(330);
		ps.adicionarLong(331);
		ps.adicionarLong(332);
		ps.adicionarLong(333);
		ps.adicionarLong(334);
		ps.adicionarLong(344);
		ps.adicionarLong(352);
		ps.adicionarLong(368);
		ps.adicionarLong(377);
		ps.adicionarLong(378);
		ps.adicionarLong(379);
		ps.adicionarLong(380);
		ps.adicionarLong(381);
		ps.adicionarLong(382);
		
		sqlLista = "SELECT  p.ID_AREA, s.SERV, p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR,p.DATA_RECEBIMENTO, a.DATA_AGENDADA, TRUNC(SYSDATE - p.DATA_RECEBIMENTO) as DIAS " + sql;
		sqlLista +=" ORDER BY DIAS desc ";
		
		try{
			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ProcessoAudienciaDt obTemp = new ProcessoAudienciaDt();
				obTemp.setArea(rs1.getString("ID_AREA"));
				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				
				String dataRecebimento = Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO"));
				obTemp.setDataRecebimento(dataRecebimento);
				
				String dataAgendada = Funcoes.FormatarData(rs1.getDateTime("DATA_AGENDADA"));
				obTemp.setDataAgendada(dataAgendada);
				
				obTemp.setQuantidadeDiasParalisados(rs1.getString("DIAS") + " dias");
				
				listaProcessos.add(obTemp);
			}
			sqlQuantidade = "SELECT COUNT(p.ID_PROC) AS QUANTIDADE " + sql;
			rs2 = consultar(sqlQuantidade, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	/**
	 * 
	 * Método que consulta os Processos aguardando audiência partindo da suposição de que outra audiência foi realizada. Exemplo: um processo que esteja
	 * aguardando a realização de uma audiência de JULGAMENTO após a realização de uma audiência de INSTRUÇÃO.
	 * @param idServentia - Id da Serventia
	 * @param tipoAudiencia - tipo de audiência atual selecionada
	 * @param opcaoPeriodo - opção de tempo selecionada
	 * @param tipoAudienciaAnterior - tipo de audiência realizada anteriormente selecionada
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos aguardando audiência
	 * @throws Exception
	 * @author hmgodinho
	 */
	//TODO: Migração Banco - Ajustes comandos específicos do MySql (interval / DATEDIFF, etc) e ids fixos
	public List consultarProcessosAguardandoAudienciaAposOutraRealizada(String idServentia, String idJuiz, String tipoAudiencia, String opcaoPeriodo, String tipoAudienciaAnterior, String posicaoPaginaAtual) throws Exception {
		String sqlQuantidade = "";
		String sql = "";
		String sqlLista = "";
		
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql = 	" FROM PROC p " +
				"	INNER JOIN AUDI_PROC ap on p.ID_PROC = ap.ID_PROC " +
				"	INNER JOIN AUDI a on ap.ID_AUDI = a.ID_AUDI " +
				" 	INNER JOIN SERV s on p.ID_SERV = s.ID_SERV " +
				"	LEFT JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC" +
				" WHERE p.DATA_ARQUIVAMENTO is null " +
				"	AND p.ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		if(idJuiz != null && !idJuiz.equalsIgnoreCase("") && !idJuiz.equalsIgnoreCase("null")) {
			sql += 	"	AND pr.ID_SERV_CARGO = ?";
			ps.adicionarLong(idJuiz);
		}
		sql +=	"	AND a.ID_AUDI_TIPO = ? ";
		ps.adicionarLong(tipoAudienciaAnterior);
		sql +=  "	AND a.DATA_MOVI IS NOT NULL " +
				"	AND ap.ID_AUDI_PROC_STATUS IN (?,?,?,?,?,?) " +
				" 	AND s.ID_SERV_SUBTIPO IN (?,?,?,?,?,?) " +
				"	AND (a.DATA_MOVI + ?) <= ? ";
		ps.adicionarLong(2);
		ps.adicionarLong(15);
		ps.adicionarLong(16);
		ps.adicionarLong(17);
		ps.adicionarLong(18);
		ps.adicionarLong(19);
		ps.adicionarLong(1);
		ps.adicionarLong(2);
		ps.adicionarLong(3);
		ps.adicionarLong(7);
		ps.adicionarLong(8);
		ps.adicionarLong(9);
		ps.adicionarLong(opcaoPeriodo);
		ps.adicionarDateTime(new Date());
		sql +=	"	AND p.ID_PROC NOT IN ( SELECT ap.ID_PROC FROM AUDI_PROC ap " +
				"								INNER JOIN AUDI a on ap.ID_AUDI=a.ID_AUDI " +
				"							WHERE ap.ID_PROC = p.ID_PROC " +
				"								AND p.ID_SERV = ? ";
		ps.adicionarLong(idServentia);
		sql +=  "								AND a.ID_AUDI_TIPO = ? ";
		ps.adicionarLong(tipoAudiencia);
		sql +=  "								AND ap.ID_AUDI_PROC_STATUS IN (?,?,?,?,?,?)) " +
				"								AND p.ID_PROC NOT IN ( Select m.ID_PROC FROM MOVI m " +
				"														WHERE m.ID_MOVI_TIPO IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)) ";
		ps.adicionarLong(2);
		ps.adicionarLong(15);
		ps.adicionarLong(16);
		ps.adicionarLong(17);
		ps.adicionarLong(18);
		ps.adicionarLong(19);
		
		ps.adicionarLong(245);
		ps.adicionarLong(247);
		ps.adicionarLong(249);
		ps.adicionarLong(250);
		ps.adicionarLong(273);
		ps.adicionarLong(274);
		ps.adicionarLong(275);
		ps.adicionarLong(276);
		ps.adicionarLong(277);
		ps.adicionarLong(308);
		ps.adicionarLong(316);
		ps.adicionarLong(317);
		ps.adicionarLong(318);
		ps.adicionarLong(322);
		ps.adicionarLong(323);
		ps.adicionarLong(324);
		ps.adicionarLong(325);
		ps.adicionarLong(326);
		ps.adicionarLong(327);
		ps.adicionarLong(328);
		ps.adicionarLong(329);
		ps.adicionarLong(330);
		ps.adicionarLong(331);
		ps.adicionarLong(332);
		ps.adicionarLong(333);
		ps.adicionarLong(334);
		ps.adicionarLong(344);
		ps.adicionarLong(352);
		ps.adicionarLong(368);
		ps.adicionarLong(377);
		ps.adicionarLong(378);
		ps.adicionarLong(379);
		ps.adicionarLong(380);
		ps.adicionarLong(381);
		ps.adicionarLong(382);
		sqlLista = "SELECT  p.ID_AREA, s.SERV, p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR,p.DATA_RECEBIMENTO, a.DATA_AGENDADA, TRUNC(SYSDATE - p.DATA_RECEBIMENTO) as DIAS " + sql;
		sqlLista +=" ORDER BY DIAS desc ";
		
		try{
			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ProcessoAudienciaDt obTemp = new ProcessoAudienciaDt();
				obTemp.setArea(rs1.getString("ID_AREA"));
				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				
				String dataRecebimento = Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO"));
				obTemp.setDataRecebimento(dataRecebimento);
				
				String dataAgendada = Funcoes.FormatarData(rs1.getDateTime("DATA_AGENDADA"));
				obTemp.setDataAgendada(dataAgendada);
				
				obTemp.setQuantidadeDiasParalisados(rs1.getString("DIAS") + " dias");
				
				listaProcessos.add(obTemp);
			}
			sqlQuantidade = "SELECT COUNT(p.ID_PROC) AS QUANTIDADE " + sql;
			rs2 = consultar(sqlQuantidade, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	/**
	 * Método que consulta id da Serventia do Processo.
	 * @param String idProcesso
	 * @return String idServentia
	 * @throws Exception
	 */
	public String consultarIdServentiaProcesso(String idProcesso) throws Exception {
		String idServentia = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql = "SELECT ID_SERV FROM PROJUDI.PROC WHERE ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while(rs1.next()) {
					idServentia = rs1.getString("ID_SERV");
				}
			}
		
		}
		finally{
			rs1.close();
		}
		
		return idServentia;
	}
	
	/**
	 * Método que calcula a quantidade de processos que um advogado figurou.
	 * 
	 * @param oabNumero
	 * @param oaboComplemento
	 * @param oabUf
	 * @return quantidade de processos cujo advogado figurou
	 * @author jpcpresa
	 * @throws Exception 
	 */
	public long consultarQuantidadeProcessosAdvogado(String oabNumero, String oabComplemento, String oabUf, String dateTimeInicial, String dateTimeFinal) throws Exception {

		StringBuffer sql = new StringBuffer();
		long quantidade = 0;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT COUNT(distinct p.ID_PROC) as QUANTIDADE");
		sql.append(" from");
		sql.append(" PROC_PARTE_ADVOGADO ppa");
		sql.append(" join USU_SERV_OAB usoab on ppa.ID_USU_SERV = usoab.ID_USU_SERV");
		sql.append(" join USU_SERV us on usoab.ID_USU_SERV = us.ID_USU_SERV");
		sql.append(" join USU u on us.ID_USU = u.ID_USU");
		sql.append(" join PROC_PARTE pp on ppa.ID_PROC_PARTE = pp.ID_PROC_PARTE");
		sql.append(" join PROC p on pp.ID_PROC = p.ID_PROC");
		sql.append(" join SERV s on p.ID_SERV = s.ID_SERV");
		sql.append(" join PROC_TIPO pt on pt.ID_PROC_TIPO = p.ID_PROC_TIPO");
		sql.append(" join ESTADO e on e.ID_ESTADO = s.ID_ESTADO_REPRESENTACAO");
		sql.append(" WHERE usoab.OAB_NUMERO = ? AND usoab.OAB_COMPLEMENTO = ? AND e.ESTADO_CODIGO =?");
		sql.append(" AND ppa.DATA_ENTRADA BETWEEN ? AND ?");
		ps.adicionarLong(oabNumero);
		ps.adicionarString(oabComplemento);
		ps.adicionarString(oabUf);
		ps.adicionarDateTime(dateTimeInicial);
		ps.adicionarDateTime(dateTimeFinal);
		
		// A data de saída pode ser NULA OU preenchida, desde que fora do período do filtro da consulta. (Antes ou depois do período)
		sql.append(" AND (ppa.data_saida IS NULL OR ppa.data_saida IS NOT NULL AND ppa.data_saida NOT BETWEEN ? AND ? )");
		ps.adicionarDateTime(dateTimeInicial);
		ps.adicionarDateTime(dateTimeFinal);
				
		try{
			rs1 = consultar(sql.toString(), ps);
			//Conta registros
			
			if( rs1 != null ) {
				while(rs1.next()) {
					quantidade = rs1.getLong("QUANTIDADE");
				}
			}
			
		
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
			} catch(Exception e) {
			}
		}
		return quantidade;
	}
	
	
	/**
	 * Método que lista os advogados do processo para a certidão de Prática Forense
	 * @param oabNumero
	 * @param oaboComplemento
	 * @param oabUf
	 * @return quantidade de processos cujo advogado figurou
	 * @author jpcpresa
	 * @throws Exception 
	 */
	public ResultSetTJGO consultarPartesPraticaForenseAdvogado(List<String> listaId_Processo) throws Exception {


		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		if (listaId_Processo == null || listaId_Processo.size() == 0) return null;
			
		sql.append(" SELECT p.PROC_NUMERO, p.DIGITO_VERIFICADOR, pp.NOME, ppt.PROC_PARTE_TIPO_CODIGO");
		sql.append(" FROM");
		sql.append(" PROC_PARTE pp");
		sql.append(" join PROC_PARTE_TIPO ppt on (pp.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO)");
		sql.append(" join PROC p on p.ID_PROC = pp.ID_PROC");
		sql.append(" WHERE");
		sql.append(" p.ID_PROC IN (");
		
		Iterator it = listaId_Processo.iterator();		
		int contador = 0;
	
		boolean EhPrimeiro = true;
		while (it.hasNext()) {	
			if (!EhPrimeiro) sql.append(", ");
			sql.append("?");
			ps.adicionarLong((String)it.next());
			EhPrimeiro = false;
			
			contador++;
			
			if( contador == 990 ) {
				EhPrimeiro = true;
				contador = 0;
				sql.append(")");
				
				if( it.hasNext() ) {
					sql.append(" OR");
					sql.append(" p.ID_PROC IN (");
				}
			}
		}
		sql.append(")");
				
		rs1 = consultar(sql.toString(), ps);
		
		return rs1;
	}
	
	
	/**
	 * Método  que lista os advogados de acordo com as partes para montar a certidão de Prática Forense
	 *  
	 * @param oabNumero
	 * @param oaboComplemento
	 * @param oabUf
	 * @return ResultSetTJGO com os advogados
	 * @author jpcpresa
	 * @throws Exception 
	 */
	public ResultSetTJGO consultarAdvogadoPraticaForenseAdvogado(List<String> listaId_Processo) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		
		if (listaId_Processo == null || listaId_Processo.size() == 0) return null;
		
		sql.append(" SELECT p.PROC_NUMERO, p.DIGITO_VERIFICADOR, u.NOME, ppt.PROC_PARTE_TIPO_CODIGO");
		sql.append(" FROM"); 
		sql.append(" PROC_PARTE_ADVOGADO ppa");
		sql.append(" join USU_SERV_OAB usoab on ppa.ID_USU_SERV = usoab.ID_USU_SERV");
		sql.append(" join USU_SERV us on usoab.ID_USU_SERV = us.ID_USU_SERV");
		sql.append(" join USU u on us.ID_USU = u.ID_USU");
		sql.append(" join PROC_PARTE pp on ppa.ID_PROC_PARTE = pp.ID_PROC_PARTE");
		sql.append(" join PROC p on p.ID_PROC = pp.ID_PROC");
		sql.append(" join PROC_PARTE_TIPO ppt on (pp.ID_PROC_PARTE_TIPO = ppt.ID_PROC_PARTE_TIPO)");
		sql.append(" WHERE");		
		sql.append(" p.ID_PROC IN (");
		Iterator it = listaId_Processo.iterator();		
		
		int contador = 0;
		
		boolean EhPrimeiro = true;
		while (it.hasNext()) {	
			if (!EhPrimeiro) sql.append(", ");
			sql.append("?");
			ps.adicionarLong((String)it.next());
			EhPrimeiro = false;
			
			contador++;
			
			if( contador == 990 ) {
				EhPrimeiro = true;
				contador = 0;
				sql.append(")");
				
				if( it.hasNext() ) {
					sql.append(" OR");
					sql.append(" p.ID_PROC IN (");
				}
			}
		}
		sql.append(")");
		
		sql.append(" AND ppa.DATA_SAIDA IS NULL");
			
		rs1 = consultar(sql.toString(), ps);

		return rs1;
	}

	
	/**
	 * Método que lista os processos que um advogado figurou para a certidão de Prática Forense
	 * 
	 * @param oabNumero
	 * @param oaboComplemento
	 * @param oabUf
	 * @return quantidade de processos cujo advogado figurou
	 * @author jpcpresa
	 * @throws Exception 
	 */
	public List consultarProcessoPraticaForenseAdvogado(String oabNumero, String oabComplemento, String oabUf, String dateTimeInicial, String dateTimeFinal) throws Exception {

		StringBuffer sql = new StringBuffer();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		ResultSetTJGO rs3 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT p.ID_PROC, ppa.ID_PROC_PARTE_ADVOGADO, ppa.DATA_ENTRADA, p.PROC_NUMERO, p.DIGITO_VERIFICADOR,");
		sql.append(" p.FORUM_CODIGO, p.ANO, pt.PROC_TIPO, s.SERV, p.SEGREDO_JUSTICA, p.DATA_RECEBIMENTO");
		sql.append(" FROM");
		sql.append(" PROC_PARTE_ADVOGADO ppa");
		sql.append(" join USU_SERV_OAB usoab on ppa.ID_USU_SERV = usoab.ID_USU_SERV");
		sql.append(" join USU_SERV us on usoab.ID_USU_SERV = us.ID_USU_SERV");
		sql.append(" join USU u on us.ID_USU = u.ID_USU");
		sql.append(" join PROC_PARTE pp on ppa.ID_PROC_PARTE = pp.ID_PROC_PARTE");
		sql.append(" join PROC p on pp.ID_PROC = p.ID_PROC");
		sql.append(" join SERV s on p.ID_SERV = s.ID_SERV");
		sql.append(" join SERV soab on us.ID_SERV = soab.ID_SERV");
		sql.append(" join PROC_TIPO pt on pt.ID_PROC_TIPO = p.ID_PROC_TIPO");
		sql.append(" join ESTADO e on e.ID_ESTADO = soab.ID_ESTADO_REPRESENTACAO");
		sql.append(" WHERE");
		sql.append(" usoab.OAB_NUMERO = ? AND usoab.OAB_COMPLEMENTO = ? AND e.ESTADO_CODIGO =?");
		ps.adicionarLong(oabNumero);
		ps.adicionarString(oabComplemento);
		ps.adicionarString(oabUf);
		sql.append(" AND ppa.DATA_ENTRADA BETWEEN ? AND ?");
		//sql.append(" GROUP BY p.ID_PROC");
		ps.adicionarDateTime(dateTimeInicial);
		ps.adicionarDateTime(dateTimeFinal);
		
		// A data de saída pode ser NULA OU preenchida, desde que fora do período do filtro da consulta. (Antes ou depois do período)
		sql.append(" AND (ppa.data_saida IS NULL OR ppa.data_saida IS NOT NULL AND ppa.data_saida NOT BETWEEN ? AND ? )");
		ps.adicionarDateTime(dateTimeInicial);
		ps.adicionarDateTime(dateTimeFinal);
		 
		List listaProcessos = new ArrayList(20);
 		List listaId_Processo = new ArrayList(20);
		try{
			rs1 = consultar(sql.toString(),ps);
			//Conta registros
			
			
			if( rs1 != null ) {
				while(rs1.next()) {
					ProcessoCertidaoPraticaForenseDt  pcpf = new ProcessoCertidaoPraticaForenseDt();

					listaId_Processo.add(rs1.getString("ID_PROC"));
					pcpf.setProcessoNumero(rs1.getString("PROC_NUMERO"));
					pcpf.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
					pcpf.setForumCodigo(rs1.getString("FORUM_CODIGO"));
					pcpf.setDataRecebimento(rs1.getString("DATA_RECEBIMENTO"));
					pcpf.setAno(rs1.getString("ANO"));
					pcpf.setProcessoTipo(rs1.getString("PROC_TIPO"));
					pcpf.setInicioAtuacao(rs1.getString("DATA_ENTRADA"));
					pcpf.setServentia(rs1.getString("SERV"));
					pcpf.setSegredoJustica(rs1.getString("SEGREDO_JUSTICA").equals("1") ? true : false);
					listaProcessos.add(pcpf);
					
					
				}
			}
			
			//Consulta as Partes dos processos do advogado requerente
			rs2 = consultarPartesPraticaForenseAdvogado(listaId_Processo);
			
			if( rs2 != null ) {
				while(rs2.next()) {
					addParteProcessoPraticaForense(listaProcessos,rs2.getString("NOME"),rs2.getString("PROC_PARTE_TIPO_CODIGO"),rs2.getString("PROC_NUMERO"),rs2.getString("DIGITO_VERIFICADOR"));
				}
			}
			
			//Consulta os advogados das partes dos processos do advogado requerente
			rs3 = consultarAdvogadoPraticaForenseAdvogado(listaId_Processo);
			
			if( rs3 != null ) {
				while(rs3.next()) {
					addAdvogadoProcessoPraticaForense(listaProcessos,rs3.getString("NOME"),rs3.getString("PROC_PARTE_TIPO_CODIGO"),rs3.getString("PROC_NUMERO"),rs3.getString("DIGITO_VERIFICADOR"));
				}
			}
		
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
				if (rs2 != null)
					rs2.close();
				if (rs3 != null)
					rs3.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}
	
	/**
	 * Método que calcula a quantidade de processos que um advogado figurou para a certidão de Prática Forense nova (16/12/2020).
	 * 
	 * @param oabNumero
	 * @param oabComplemento
	 * @param oabUf
	 * @param dateTimeInicial
	 * @param dateTimeFinal
	 * @param id_Comarca
	 * @return quantidade de processos cujo advogado figurou
	 * @author fogomes, alterado por mmgomes em 23/12
	 * @throws Exception
	 */
	public int consultarQuantidadeProcessoPraticaForenseAdvogadoPF(String oabNumero, String oabComplemento, String id_estadoOABUF, String dateTimeInicial, String dateTimeFinal, String id_Comarca, String id_areaServentiaLogada) throws Exception {
		StringBuffer sql = new StringBuffer();
		int quantidade = 0;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT COUNT(distinct t.ID_PROC) as QUANTIDADE");
		sql.append(" from (");
		sql.append(obtenhaConsultaSQLProcessoPraticaForenseAdvogadoPF(oabNumero, oabComplemento, id_estadoOABUF, dateTimeInicial, dateTimeFinal, id_Comarca, id_areaServentiaLogada, ps));
		sql.append(" ) t");
		
		try{
			rs1 = consultar(sql.toString(), ps);
					
			if( rs1 != null ) {
				if(rs1.next()) {
					quantidade = rs1.getInt("QUANTIDADE");
				}
			}
		} finally{
			super.libereResultset(rs1);
		}
		return quantidade;
	}
	
	/**
	 * Método que lista os processos que um advogado figurou para a certidão de Prática Forense nova (16/12/2020)
	 * 
	 * @param oabNumero
	 * @param oabComplemento
	 * @param oabUf
	 * @param dateTimeInicial
	 * @param dateTimeFinal
	 * @param id_Comarca
	 * @return listagem de processos que o advogado figurou
	 * @author fogomes, alterado por mmgomes em 23/12
	 * @throws Exception
	 */
	public List<ProcessoCertidaoPraticaForenseDt> consultarProcessoPraticaForenseAdvogadoPF(String oabNumero, String oabComplemento, String id_estadoOABUF, String dateTimeInicial, String dateTimeFinal, String id_Comarca, String id_areaServentiaLogada) throws Exception {
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		ResultSetTJGO rs3 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String consultaSQL = obtenhaConsultaSQLProcessoPraticaForenseAdvogadoPF(oabNumero, oabComplemento, id_estadoOABUF, dateTimeInicial, dateTimeFinal, id_Comarca, id_areaServentiaLogada, ps);
		
		List<ProcessoCertidaoPraticaForenseDt> listaProcessos = new ArrayList(20);
 		List<String> listaId_Processo = new ArrayList(20);
		try{
			rs1 = consultar(consultaSQL, ps);
			
			if( rs1 != null ) {
				while(rs1.next()) {
					ProcessoCertidaoPraticaForenseDt pcpf = new ProcessoCertidaoPraticaForenseDt();

					listaId_Processo.add(rs1.getString("ID_PROC"));
					
					pcpf.setProcessoNumero(rs1.getString("PROC_NUMERO"));
					pcpf.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
					pcpf.setForumCodigo(rs1.getString("FORUM_CODIGO"));
					pcpf.setDataRecebimento(rs1.getString("DATA_RECEBIMENTO"));
					pcpf.setAno(rs1.getString("ANO"));
					pcpf.setProcessoTipo(rs1.getString("PROC_TIPO"));
					pcpf.setInicioAtuacao(rs1.getString("DATA_ENTRADA"));
					pcpf.setServentia(rs1.getString("SERV"));
					pcpf.setSegredoJustica(rs1.getString("SEGREDO_JUSTICA").equals("1") ? true : false);
					
					listaProcessos.add(pcpf);
				}
			}
			
			if(listaProcessos.size() > 0) {
				//Consulta as Partes dos processos do advogado requerente
				rs2 = consultarPartesPraticaForenseAdvogado(listaId_Processo);
				
				if( rs2 != null ) {
					while(rs2.next()) {
						addParteProcessoPraticaForense(listaProcessos,rs2.getString("NOME"),rs2.getString("PROC_PARTE_TIPO_CODIGO"),rs2.getString("PROC_NUMERO"),rs2.getString("DIGITO_VERIFICADOR"));
					}
				}
				
				//Consulta os advogados das partes dos processos do advogado requerente
				rs3 = consultarAdvogadoPraticaForenseAdvogado(listaId_Processo);
				
				if( rs3 != null ) {
					while(rs3.next()) {
						addAdvogadoProcessoPraticaForense(listaProcessos,rs3.getString("NOME"),rs3.getString("PROC_PARTE_TIPO_CODIGO"),rs3.getString("PROC_NUMERO"),rs3.getString("DIGITO_VERIFICADOR"));
					}
				}
			}
		} finally{
			super.libereResultset(rs1);
			super.libereResultset(rs2);
			super.libereResultset(rs3);
		}
		
		return listaProcessos;
	}
	
	private String obtenhaConsultaSQLProcessoPraticaForenseAdvogadoPF(String oabNumero, String oabComplemento, String id_estadoOABUF, String dateTimeInicial, String dateTimeFinal, String id_Comarca, String id_areaServentiaLogada, PreparedStatementTJGO ps) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT p.ID_PROC, ppa.ID_PROC_PARTE_ADVOGADO, ppa.DATA_ENTRADA, p.PROC_NUMERO, p.DIGITO_VERIFICADOR,");
		sql.append(" p.FORUM_CODIGO, p.ANO, pt.PROC_TIPO, s.SERV, p.SEGREDO_JUSTICA, p.DATA_RECEBIMENTO");
		sql.append(" FROM PROC_PARTE_ADVOGADO ppa");
		sql.append(" INNER JOIN USU_SERV_OAB usoab on ppa.ID_USU_SERV = usoab.ID_USU_SERV");
		sql.append(" INNER JOIN USU_SERV us on usoab.ID_USU_SERV = us.ID_USU_SERV");
		sql.append(" INNER JOIN USU u on us.ID_USU = u.ID_USU");
		sql.append(" INNER JOIN PROC_PARTE pp on ppa.ID_PROC_PARTE = pp.ID_PROC_PARTE");
		sql.append(" INNER JOIN PROC p on pp.ID_PROC = p.ID_PROC");
		sql.append(" INNER JOIN SERV s on p.ID_SERV = s.ID_SERV");
		sql.append(" INNER JOIN SERV soab on us.ID_SERV = soab.ID_SERV");
		sql.append(" INNER JOIN PROC_TIPO pt on pt.ID_PROC_TIPO = p.ID_PROC_TIPO");
		sql.append(" INNER JOIN ESTADO e on e.ID_ESTADO = soab.ID_ESTADO_REPRESENTACAO");
		sql.append(" WHERE usoab.OAB_NUMERO = ? "); ps.adicionarLong(oabNumero);
		if (oabComplemento != null && oabComplemento.trim().length() > 0) {
			sql.append(" AND usoab.OAB_COMPLEMENTO = ? "); ps.adicionarString(oabComplemento);
		}		
		sql.append(" AND e.ID_ESTADO = ?"); ps.adicionarLong(id_estadoOABUF);
		
		sql.append(" AND ppa.DATA_ENTRADA BETWEEN ? AND ?");
		ps.adicionarDateTime(dateTimeInicial);
		ps.adicionarDateTime(dateTimeFinal);
		
		sql.append(" AND s.ID_COMARCA = ? "); ps.adicionarLong(id_Comarca);
		if (Funcoes.StringToLong(id_Comarca) == Funcoes.StringToLong(ComarcaDt.ID_GOIANIA) &&
			Funcoes.StringToLong(id_areaServentiaLogada) > 0) {
			sql.append(" AND p.ID_AREA = ? "); ps.adicionarLong(id_areaServentiaLogada);
		}
				
		// A data de saída pode ser NULA OU preenchida, desde que fora do período do filtro da consulta. (Antes ou depois do período)
		sql.append(" AND (ppa.data_saida IS NULL OR (ppa.data_saida IS NOT NULL AND ppa.data_saida NOT BETWEEN ? AND ? ))");
		ps.adicionarDateTime(dateTimeInicial);
		ps.adicionarDateTime(dateTimeFinal);
		
		return sql.toString();
	}
	
	
	/**
	 * Metodo que auxilia o metodo consultarProcessoPraticaForenseAdvogado a adicionar as partes do processo na pratica
	 * forense descritiva
	 * @param listaProcesso
	 * @param NomeParte
	 * @param parteTipo
	 * @param processoNumero
	 * @param digitoVerificador
	 * @author jpcpresa
	 */
	private void addParteProcessoPraticaForense(List<ProcessoCertidaoPraticaForenseDt> listaProcesso, String NomeParte, String parteTipo, String processoNumero, String digitoVerificador) {
		for (ProcessoCertidaoPraticaForenseDt pcpf : listaProcesso) {
			if(pcpf.getProcessoNumero().equals(processoNumero) && pcpf.getDigitoVerificador().equals(digitoVerificador)) {
				if(parteTipo.matches(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO))) {
					pcpf.addPromovido(NomeParte);
				} else {
					pcpf.addPromovente(NomeParte);
				}
				break;
			}
		}
	}
	
	/**
	 * Metodo que auxilia o metodo consultarProcessoPraticaForenseAdvogado a adicionaros advogados do processo na pratica
	 * forense descritiva
	 * @param listaProcesso
	 * @param NomeParte
	 * @param parteTipo
	 * @param processoNumero
	 * @param digitoVerificador
	 * @author jpcpresa
	 */
	private void addAdvogadoProcessoPraticaForense(List<ProcessoCertidaoPraticaForenseDt> listaProcesso, String advogado, String parteTipo, String processoNumero, String digitoVerificador) {
		for (ProcessoCertidaoPraticaForenseDt pcpf : listaProcesso) {
			if(pcpf.getProcessoNumero().equals(processoNumero) && pcpf.getDigitoVerificador().equals(digitoVerificador)) {
				if(parteTipo.matches(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO))) {
					pcpf.addPromovidoAdvogado(advogado);
				} else {
					pcpf.addPromoventeAdvogado(advogado);
				}
				break;
			}
		}
	}
	
	/**
	 * Método para consultar processo simplificado para auxilio no calculo de locomoção.
	 * @param String idProcesso
	 * @return ProcessoCompletoDt
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoSimplificado(String idProcesso) throws Exception {
		ProcessoDt processoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT ID_PROC, AREA_CODIGO, ps.ID_SERV, SERV_TIPO_CODIGO, sst.SERV_SUBTIPO_CODIGO "
						+"FROM PROJUDI.PROC ps INNER JOIN PROJUDI.SERV se on se.ID_SERV = ps.ID_SERV "
						+"INNER JOIN PROJUDI.SERV_SUBTIPO sst on se.ID_SERV_SUBTIPO = sst.ID_SERV_SUBTIPO "
						+"INNER JOIN PROJUDI.SERV_TIPO st on se.ID_SERV_TIPO = st.ID_SERV_TIPO "
						+"INNER JOIN PROJUDI.Area ar on ar.ID_AREA = ps.ID_AREA "
						+"WHERE ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					processoDt = new ProcessoDt();
					
					processoDt.setId(rs1.getString("ID_PROC"));
					processoDt.setAreaCodigo(rs1.getString("AREA_CODIGO"));
					processoDt.setId_Serventia(rs1.getString("ID_SERV"));
					processoDt.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
					processoDt.setServentiaSubTipoCodigo(rs1.getString("SERV_SUBTIPO_CODIGO"));
				}
			}
		
		}
		finally{
			if( rs1 != null )
				rs1.close();
		}
		
		return processoDt;
	}
	
	/**
	 * Método para consultar processo simplificado Para salvar Mandado no SPG.
	 * @param String idProcesso
	 * @return ProcessoDt
	 * @throws Exception
	 */
	public ProcessoDt consultarProcessoSimplificadoMandadoSPG(String idProcesso) throws Exception {
		ProcessoDt processoDt = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String sql = "SELECT ID_PROC, PROC_NUMERO, DIGITO_VERIFICADOR,  AREA_CODIGO, ANO "
						+"FROM PROJUDI.PROC ps " 
						+"INNER JOIN PROJUDI.AREA ar on ar.ID_AREA = ps.ID_AREA "
						+"WHERE ID_PROC = ?";
		ps.adicionarLong(idProcesso);
		ResultSetTJGO rs1 = null;
		try{
			rs1 = consultar(sql, ps);
			if( rs1 != null ) {
				while( rs1.next() ) {
					processoDt = new ProcessoDt();
					
					processoDt.setId(rs1.getString("ID_PROC"));
					processoDt.setProcessoNumero(rs1.getString("PROC_NUMERO"));
					processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
					processoDt.setAno(rs1.getString("ANO"));
					processoDt.setAreaCodigo(rs1.getString("AREA_CODIGO"));
				}
			}
		
		}
		finally{
			if( rs1 != null )
				rs1.close();
		}
		
		return processoDt;
	}
	
	/**
	 * Metodo que consulta no banco os dados de um processo de execução para a certidão de Execução CPC Art. 615 - a C.P.C.
	 * @param numero
	 * @return CertidaoExecucaoCPCDt preenchida
	 * @throws Exception 
	 */
	public CertidaoExecucaoCPCDt consultarProcessoExecucaoCPC(String numero) throws Exception{
		CertidaoExecucaoCPCDt cecpcDt = new CertidaoExecucaoCPCDt();
		cecpcDt.setNumero(numero);
		String pNumero = "";
		String pDigito = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		ResultSetTJGO rs3 = null;
		String id_Processo = "";
		String sql = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql += " SELECT p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.DATA_RECEBIMENTO, " +
				" s.SERV, pt.PROC_TIPO, p.VALOR  " +
				" FROM  " +
				" PROC p join  SERV s on s.ID_SERV = p.ID_SERV) " +
				" join  PROC_TIPO pt on (pt.ID_PROC_TIPO = p.ID_PROC_TIPO) " +
				" WHERE " +
				" p.PROC_NUMERO = ? AND p.DIGITO_VERIFICADOR = ?";
		ps.adicionarLong(pNumero);
		ps.adicionarLong(pDigito);
	
		try{
			rs1 = consultar(sql.toString(), ps);
			//Conta registros
			
			
			if( rs1 != null ) {
				while(rs1.next()) {

					id_Processo = rs1.getString("ID_PROC");
					cecpcDt.setNumero(rs1.getString("PROC_NUMERO") +"."+rs1.getString("DIGITO_VERIFICADOR"));
					//cecpcDt.setDataDistribuicao(rs1.getString("DATA_RECEBIMENTO"));
					cecpcDt.setNatureza(rs1.getString("PROC_TIPO"));
					cecpcDt.setServentia(rs1.getString("SERV"));
					cecpcDt.setValor(rs1.getString("VALOR"));
					
				}
			}
			
			//Consulta as Partes do processo
			rs2 = consultarPartesExecucaoCPC(id_Processo);
			
			if( rs2 != null ) {
				while(rs2.next()) {
					//addParteProcessoExecucao(rs2.getString("NOME"),rs2.getString("PROC_PARTE_TIPO_CODIGO"),rs2.getString("PROC_NUMERO"),rs2.getString("DIGITO_VERIFICADOR"));
				}
			}
			
			//Consulta os advogados das partes dos processos do advogado requerente
			rs3 = consultarAdvogadoProcessoExecucao(id_Processo);
			
			if( rs3 != null ) {
				while(rs3.next()) {
					//addAdvogadoProcessoExecucao(rs3.getString("NOME"),rs3.getString("PROC_PARTE_TIPO_CODIGO"),rs3.getString("PROC_NUMERO"),rs3.getString("DIGITO_VERIFICADOR"));
				}
			}
		
		
		} finally{
			try{
				if (rs1 != null)
					rs1.close();
				if (rs2 != null)
					rs2.close();
				if (rs3 != null)
					rs3.close();
			} catch(Exception e) {
			}
		}
	
			
		return cecpcDt;
	}
	
	/** Metodo que retorna uma lista de processos de acordo com o array de Id's passado
	 * Filtrando os processos, ou seja, não trazendo os que pertencerem ao processoSubTipo passado
	 * @param ids
	 * @return Lista de processos
	 * @throws Exception
	 */
	public List listarProcessosIdFiltroProcessoSubTipo(List ids, int processoSubTipoCodigo) throws Exception {
		List id_processo = null;
		ResultSetTJGO rs = null; 
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql;

		
		sql = "SELECT p.ID_PROC, p.PROC_NUMERO, pt.PROC_TIPO, ";
		sql += "p.DIGITO_VERIFICADOR, p.SEGREDO_JUSTICA, p.DATA_RECEBIMENTO, p.VALOR, ";
		sql += "p.FORUM_CODIGO, p.ANO, s.SERV, ";
		sql += "(Select Serv From Recurso R Inner Join Serv S On S.Id_Serv = R.Id_Serv_Origem Where R.Id_Proc = P.Id_Proc AND R.Data_Retorno IS NULL) AS SERV_ORIGEM ";
		sql += "  FROM PROJUDI.PROC p ";
		sql += "JOIN PROJUDI.PROC_TIPO pt on p.ID_PROC_TIPO = pt.ID_PROC_TIPO ";
		sql += "JOIN PROJUDI.SERV s  ON (p.ID_SERV = s.ID_SERV) ";
	//	sql += "JOIN PROJUDI.PROC_STATUS ps ON (p.ID_PROC_STATUS = ps.ID_PROC_STATUS) ";
	//	sql += " WHERE ps.PROC_STATUS_CODIGO = ? ";
	//	ps.adicionarLong(ProcessoStatusDt.ATIVO);
//		sql += " AND p.ID_PROC  IN( ";
		sql += " WHERE p.ID_PROC  IN( ";
		for (int i = 0; i < ids.size(); i++) {
			sql+= "?, ";
			ps.adicionarLong(((ProcessoCertidaoPositivaNegativaDt) ids.get(i)).getId_Processo());
		}
		sql = sql.substring(0, sql.length() -2);		
		
		sql += ") AND p.ID_PROC_TIPO NOT IN(";
		
		sql += "SELECT ptps2.ID_PROC_TIPO FROM PROJUDI.PROC_TIPO_PROC_SUBTIPO ptps2  JOIN ";
		sql += "  PROJUDI.PROC_SUBTIPO ps2 ON (ptps2.ID_PROC_SUBTIPO = ps2.ID_PROC_SUBTIPO) WHERE ps2.PROC_SUBTIPO_CODIGO = ?) ";
		ps.adicionarLong(ProcessoSubtipoDt.NAO_GERAR_POSITIVACAO);
		
		
		try{
			id_processo = new ArrayList();
			rs = consultar(sql, ps);
			int i = 0;
			while (rs.next()) {
				ProcessoCertidaoPositivaNegativaDt Dados;
				if(i < ids.size()) {
					Dados = getProcCertNegPos(ids, rs.getString("ID_PROC"));

					// Dados = (ProcessoCertidaoPositivaNegativaDt)ids.get(i);
					i++;
				} else {
					 Dados = new ProcessoCertidaoPositivaNegativaDt(); 
				}
				
				Dados.setProcessoNumero(rs.getString("PROC_NUMERO"));
				Dados.setProcessoTipo( rs.getString("PROC_TIPO"));
				Dados.setDigitoVerificador( rs.getString("DIGITO_VERIFICADOR"));
				Dados.setSegredoJustica(rs.getString("SEGREDO_JUSTICA").equals("1") ? true : false);
				Dados.setDataRecebimento( Funcoes.FormatarDataHora(rs.getDateTime("DATA_RECEBIMENTO")));
				Dados.setValor( rs.getString("VALOR"));
				Dados.setForumCodigo( rs.getString("FORUM_CODIGO"));
				Dados.setAno( rs.getString("ANO"));
				Dados.setSistema("Projudi");
				Dados.setProcessoTipo(rs.getString("PROC_TIPO"));
				if (rs.getString("SERV_ORIGEM") != null) {
					Dados.setServentia(rs.getString("SERV_ORIGEM"));
				} else {
					Dados.setServentia(rs.getString("SERV"));	
				}
			
				
				id_processo.add(Dados);
			}
		} finally {
			try{
				if (rs != null)
					rs.close();
			} catch(Exception e) {
			}
		}
	
		return id_processo;
	}
	
	public ProcessoCertidaoPositivaNegativaDt getProcCertNegPos(List listaProcesso,String id_proc) {
		ProcessoCertidaoPositivaNegativaDt dt = null;
		for (int i = 0; i < listaProcesso.size(); i++) {
			if (((ProcessoCertidaoPositivaNegativaDt) listaProcesso.get(i)).getId_Processo().equals(id_proc)) {
				dt = (ProcessoCertidaoPositivaNegativaDt) listaProcesso.get(i);
			}
		}
		return dt;
	}
	
	
	


//	private void addParteProcessoExecucao(String string, String string2, String string3, String string4) {
//		
//		
//	}
//
//	private void addAdvogadoProcessoExecucao(String string, String string2, String string3, String string4) {
//		
//		
//	}

	private ResultSetTJGO consultarAdvogadoProcessoExecucao(String idProcesso) {
		
		return null;
	}

	private ResultSetTJGO consultarPartesExecucaoCPC(String idProcesso) {
		
		return null;
	}

	public String[] consultarProcessoDependente(String id_ServentiaAnterior, String id_Processo, String id_ProcessoDependente) throws Exception {
		String stSql =""; 
		String[] stRetorno = {null,null,null};
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSql = " SELECT ID_PROC, JULGADO_2_GRAU, PROC_NUMERO, DIGITO_VERIFICADOR, ANO FROM PROC p WHERE p.ID_SERV = ? AND "; ps.adicionarLong(id_ServentiaAnterior);
		
		
		if (id_ProcessoDependente != null && id_ProcessoDependente.length() > 0){
			stSql += " ( p.id_proc_dependente = ? or p.id_proc = ? or p.id_proc_dependente = ? ) ";  
			ps.adicionarLong(id_Processo); 
			ps.adicionarLong(id_ProcessoDependente);
			ps.adicionarLong(id_ProcessoDependente);
		}  else {
			stSql += " p.id_proc_dependente = ? "; ps.adicionarLong(id_Processo);
		}
		
		try{			
			rs1 =  consultar(stSql, ps);
			if (rs1.next()){
				stRetorno[0] = rs1.getString("ID_PROC");
				stRetorno[1] = rs1.getString("JULGADO_2_GRAU");
				stRetorno[2] = rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR") + "." + rs1.getString("ANO");
			}
			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {		}
			
		}
		return stRetorno;
	}
	
	public String[] consultarRecursosDependente(String id_Processo, String id_areaDistribuicao) throws Exception {
		String stSql =""; 
		String[] stRetorno = {null,null,null,null,null};
		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSql = "SELECT pl.id_ponteiro_log, pl.id_serv, p.id_proc, p.julgado_2_grau, p.proc_numero, p.digito_verificador, p.ano, pl.id_serv_cargo ";
		stSql += "	FROM PONTEIRO_LOG pl ";
		stSql += " 	INNER JOIN PROC p ON pl.id_proc = p.id_proc ";
		stSql += " WHERE pl.id_area_dist = ? ";	ps.adicionarLong(id_areaDistribuicao);
		stSql += " 		AND p.id_proc_dependente = ? ";		ps.adicionarLong(id_Processo);
		stSql += " 		AND pl.id_ponteiro_log_tipo = ? ";	ps.adicionarLong(PonteiroLogTipoDt.DISTRIBUICAO);
		stSql += " ORDER BY pl.id_ponteiro_log DESC";

		try{			
			rs1 =  consultar(stSql, ps);
			while (rs1.next()) {
				if (!rs1.getString("ID_PROC").equals(id_Processo)){
					stRetorno[0] = rs1.getString("ID_PROC");
					stRetorno[1] = rs1.getString("JULGADO_2_GRAU");
					stRetorno[2] = rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR") + "." + rs1.getString("ANO");
					stRetorno[3] = rs1.getString("ID_SERV");
					stRetorno[4] = rs1.getString("ID_SERV_CARGO");
				}
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {		}
		}
		return stRetorno;
	}
	
	/**
	 * Método que altera o status de um processo.
	 * @param id_Processo - ID do processo
	 * @param novoProcessoStatusCodigo - novo código de status (ProcessoStatusCodigo)
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void alterarProcessoStatus(String id_Processo, int novoProcessoStatus) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC set ID_PROC_STATUS = ";
		Sql += " (SELECT ID_PROC_STATUS FROM PROJUDI.PROC_STATUS WHERE PROC_STATUS_CODIGO = ?)";
		ps.adicionarLong(novoProcessoStatus);
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

			executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método que altera a fase de um processo.
	 * @param id_Processo - ID do processo
	 * @param novoProcessoFaseCodigo - novo código de fase (ProcessoFaseCodigo)
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void alterarProcessoFase(String id_Processo, int novoProcessoFaseCodigo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC set ID_PROC_FASE = ";
		Sql += " (SELECT ID_PROC_FASE FROM PROJUDI.PROC_FASE WHERE PROC_FASE_CODIGO = ?)";
		ps.adicionarLong(novoProcessoFaseCodigo);
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

		executarUpdateDelete(Sql, ps);
	}
	
	
	/**
	 * Registra o julgamento do Mérito do Processo Principal
	 * @param id_Processo: processo em qual teve o julgamento do Mérito
	 * @param julgado: informa se houve ou não o julgamento de Mérito
	 */
	public void registrarJulgamentoMeritoProcessoPrincipal(String id_Processo, boolean julgado) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET JULGADO_2_GRAU = ?";
		ps.adicionarBoolean(julgado);
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

		executarUpdateDelete(Sql, ps);
		

	}
		
	public ProcessoDt consultarProcesso(String processoNumero, String digitoVerificador, String ano, String forumCodigo, String processoStatusCodigo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		ProcessoDt processoDt = null;
		try{
			Sql = "select * from PROJUDI.view_proc p" +
					" where p.proc_numero = ?" +
					" and p.digito_verificador = ?" +
					" and p.ano = ?" +
					" and p.forum_codigo = ?" +
					" and p.id_proc_status = (select id_proc_status from PROJUDI.view_proc_status where proc_status_codigo = ?)";
			ps.adicionarString(processoNumero);
			ps.adicionarString(digitoVerificador);
			ps.adicionarString(ano);
			ps.adicionarString(forumCodigo);
			ps.adicionarString(processoStatusCodigo);

			rs = consultar(Sql, ps);
			
			if (rs.next()){
				processoDt = new ProcessoDt();
				associarDt(processoDt, rs);
			}
			
		
		}finally{
			if (rs != null) rs.close();
		}
		return processoDt;
	}
	
	/**
	 * Consultar número completo dos processos de cálculo de liquidação de pena
	 * @author wcsilva
	 */
	public List listarNumeroProcessoCalculo() throws Exception {
		String Sql;
		List lista = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "select p.proc_numero, p.ano, p.digito_verificador, p.forum_codigo from projudi.view_proc p where p.proc_status_codigo = ?";
		ps.adicionarLong(ProcessoStatusDt.CALCULO);

		try{
			rs1 = consultar(Sql, ps);
			lista = new ArrayList();
			while (rs1.next()) {
				rs1.getString("proc_numero");
				rs1.getString("ano");
				rs1.getString("digito_verificador");
				rs1.getString("forum_codigo");
				String num = Funcoes.completarZeros(rs1.getString("proc_numero"), 7) + "." + Funcoes.completarZeros(rs1.getString("digito_verificador"), 2) + "." + rs1.getString("ano") + "." + Configuracao.JTR + "." + Funcoes.completarZeros(rs1.getString("forum_codigo"), 4);
				lista.add(num);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return lista;
	}	
	
	/**
	 * Consulta a quantidade de processos ativos para o advogado no ano atual.
	 * @param idUsuarioServentiaAdvogado
	 * @return
	 * @throws Exception
	 */
	public long consultarQuantidadeProcessosAtivosAdvogadoAnoAtual(String idUsuarioServentiaAdvogado) throws Exception {

		StringBuffer sql = new StringBuffer();
		long quantidade = 0;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		sql.append("SELECT COUNT(DISTINCT P.ID_PROC) AS QUANTIDADE");
		sql.append(" FROM PROC_PARTE_ADVOGADO PPA INNER JOIN PROC_PARTE PP ON PP.ID_PROC_PARTE = PPA.ID_PROC_PARTE");
		sql.append(" INNER JOIN PROC P ON P.ID_PROC = PP.ID_PROC");
		sql.append(" INNER JOIN PROC_STATUS PS ON PS.ID_PROC_STATUS = P.ID_PROC_STATUS");
		sql.append(" WHERE PPA.ID_USU_SERV = ?"); ps.adicionarLong(idUsuarioServentiaAdvogado);
		sql.append(" AND PPA.DATA_SAIDA IS NULL"); 
		sql.append(" AND P.ANO = ?"); ps.adicionarLong((new TJDataHora().getAno()));
		sql.append(" AND PS.PROC_STATUS_CODIGO <> ?"); ps.adicionarLong(ProcessoStatusDt.ARQUIVADO);	
		
		
		try{
			rs1 = consultar(sql.toString(), ps);
			//Conta registros			
			if(rs1.next()) quantidade = rs1.getLong("QUANTIDADE");	
		
		
		} finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e) {}
		}
		return quantidade;
	}
	
	public void alterarNumeroProcesso(ProcessoDt dados) throws Exception {

		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET  ";

		Sql += "PROC_NUMERO = ?";
		ps.adicionarLong(dados.getProcessoNumeroSimples());
		Sql += ",DIGITO_VERIFICADOR = ?";
		ps.adicionarLong(dados.getDigitoVerificador());
		Sql += ",ANO = ?";
		ps.adicionarLong(dados.getAno());
		Sql += ",FORUM_CODIGO = ?";
		ps.adicionarLong(dados.getForumCodigo());

		Sql = Sql + " WHERE ID_PROC  = ?";
		ps.adicionarLong(dados.getId());

			executarUpdateDelete(Sql, ps);

	}
	
	/**
	 * Método para consultar se processo é de área criminal.
	 * Retorna true caso sim e false caso não.
	 * 
	 * @param String id_Processo
	 * @return boolean
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public boolean isProcessoAreaCriminal(String id_Processo) throws Exception {
		boolean retorno = false;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT ID_AREA FROM PROJUDI.PROC WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				if( rs1.getString("ID_AREA").equals(AreaDt.CRIMINAL)) {
					retorno = true;
				}
			}
		
		}
		finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e) {}
		}
		
		return retorno;
	}
	
	public String consultarIdArea(String id_Processo) throws Exception {
		String retorno = "";
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		Sql = "SELECT ID_AREA FROM PROJUDI.PROC WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

		try {
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				retorno = rs1.getString("ID_AREA");
			}
		} finally {
			try {if (rs1 != null)rs1.close();} catch (Exception e) {}
		}
		
		return retorno;
	}
	
	/*
	private List getListaProcessosWebService(ResultSetTJGO rs1, boolean consultaTodasPartes) throws Exception{
		String processos = "";
		String recursos = ""; //variável para concatenar os recursos inominados
		ProcessoDt processoDt;
		Map mapProcesso = new LinkedHashMap();
		List listaProcessos = new ArrayList();
		PreparedStatementTJGO psRecursos = new PreparedStatementTJGO();
		PreparedStatementTJGO psProcessos = new PreparedStatementTJGO();
		
		//Essa primeira consulta retorna somente os processos
		while (rs1.next()) {
			//Criando objeto do tipo processo
			processoDt = new ProcessoDt();
			processoDt.setId_Processo(rs1.getString("ID_PROC"));
			processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
			processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
			processoDt.setAno(rs1.getString("ANO"));
			processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
			processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
			processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
			processoDt.setServentia(rs1.getString("SERV"));
			processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
			processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
			
			try{
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
			}catch(Exception e) {}
			
			if (consultaTodasPartes)
			{
				//Concatena os processos encontrados para depois buscar as partes desses
				if (processoDt.getId_Recurso().length() > 0){					
					recursos += (recursos.length() > 0 ? " OR " : "") + "(rp.ID_PROC = ?)";
					psRecursos.adicionarLong(processoDt.getId());
				}
				else {
					processos += (processos.length() > 0 ? " OR " : "") + "(p.ID_PROC = ?)";
					psProcessos.adicionarLong(processoDt.getId());
				}
			} else {				
				ProcessoParteDt primeiroPromoventeRecorrente = null;
				ProcessoParteDt primeiroPromovidoRecorrido = null;				
		
				primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE"));
				
				try{
					if (rs1.getString("PRIMEIRO_RECORRENTE") != null) primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_RECORRENTE"));
				}catch(Exception e) {}				
				
				primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));
					
				try{
					if (rs1.getString("PRIMEIRO_RECORRIDO") != null) primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_RECORRIDO"));
				}catch(Exception e) {}							
															
				if (rs1.getString("ID_PROC_PARTE") != null && rs1.getString("NOME") != null && rs1.getString("PROC_PARTE_TIPO_CODIGO") != null)
				{	
					if (rs1.getString("PROC_PARTE_TIPO_CODIGO").equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)) || rs1.getString("PROC_PARTE_TIPO_CODIGO").equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO)))
					{
						primeiroPromoventeRecorrente = new ProcessoParteDt();
						primeiroPromoventeRecorrente.setId(rs1.getString("ID_PROC_PARTE"));
						primeiroPromoventeRecorrente.setNome(rs1.getString("NOME"));
						primeiroPromoventeRecorrente.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					} else if (rs1.getString("PROC_PARTE_TIPO_CODIGO").equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)) || rs1.getString("PROC_PARTE_TIPO_CODIGO").equalsIgnoreCase(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO)))
					{
						primeiroPromovidoRecorrido = new ProcessoParteDt();						
						primeiroPromovidoRecorrido.setId(rs1.getString("ID_PROC_PARTE"));
						primeiroPromovidoRecorrido.setNome(rs1.getString("NOME"));
						primeiroPromovidoRecorrido.setProcessoParteTipo(rs1.getString("PROC_PARTE_TIPO_CODIGO"));
					}				
				}
                if (primeiroPromoventeRecorrente != null) processoDt.addListaPromoventes(primeiroPromoventeRecorrente);                
                if (primeiroPromovidoRecorrido != null) processoDt.addListaPromovidos(primeiroPromovidoRecorrido);
			}
			mapProcesso.put(processoDt.getId(), processoDt);
		}
		rs1.close();

		if (consultaTodasPartes){
			
			if (processos.length() > 0) {
				//Busca as partes para cada processo encontrado anteriormente
				String sql = " SELECT p.ID_PROC_PARTE, p.NOME, p.PROC_PARTE_TIPO_CODIGO, p.ID_PROC FROM PROJUDI.VIEW_PROC_PARTE p";
				sql += " WHERE (" + processos + ") AND p.DATA_BAIXA IS NULL";
				ResultSetTJGO rs2 = consultar(sql, psProcessos);
				setPartesProcesso(rs2, mapProcesso);
				rs2.close();
			}
	
			if (recursos.length() > 0) {
				//Busca as partes para cada recurso inominado
				String sql = " SELECT rp.ID_PROC_PARTE, rp.NOME, rp.PROC_PARTE_TIPO_CODIGO, rp.ID_PROC FROM PROJUDI.VIEW_RECURSO_PARTE rp";
				sql += " WHERE (" + recursos + ") AND rp.DATA_BAIXA IS NULL AND rp.DATA_RETORNO IS NULL";
				ResultSetTJGO rs3 = consultar(sql, psRecursos);
				setPartesProcesso(rs3, mapProcesso);
				rs3.close();
			}
		}

		listaProcessos.addAll(mapProcesso.values()); //Adiciona na lista os valores do map
		mapProcesso = null;
		return listaProcessos;
	}
	*/
	
	public List consultarProcessosAdvogadoWebService(UsuarioNe usuarioNe, String idProcessoTipo, String dataRecebimento, String posicao) throws Exception {
		String stSqlCampos = "";
		String stSqlComumProcesso = "";		
		String sql = "";		
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSqlCampos = "SELECT distinct p.ID_PROC, PROC_NUMERO, SERV, DIGITO_VERIFICADOR, ANO, DATA_RECEBIMENTO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, SEGREDO_JUSTICA, p.VALOR ";		
		stSqlCampos += ", PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, PROC_STATUS_CODIGO ";
				
		stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_ADVOGADO p";
		stSqlComumProcesso += " WHERE p.ID_USU_SERV = ?";
		ps.adicionarLong(usuarioNe.getUsuarioDt().getId_UsuarioServentia());
		if(idProcessoTipo != null && !idProcessoTipo.trim().equalsIgnoreCase("")) {
			stSqlComumProcesso += " AND p.ID_PROC_TIPO = ? ";
			ps.adicionarLong(idProcessoTipo);
		}
		if(dataRecebimento != null && !dataRecebimento.trim().equalsIgnoreCase("")) {
			stSqlComumProcesso += " AND TO_NUMBER(TO_CHAR(p.DATA_RECEBIMENTO, 'YYYYMMDD')) = ? ";
			ps.adicionarLong(dataRecebimento);
		}
		stSqlComumProcesso += " AND p.DATA_SAIDA IS NULL AND PROC_STATUS_CODIGO = 0";
		
		try{
			
			//Busca registros
			sql = stSqlCampos;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM ,DATA_RECEBIMENTO";
			
			//Consulta os processos e chama método para consultar as partes de cada um
			rs1 = consultarPaginacao(sql, ps, posicao);

			ProcessoDt processoDt;
			
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				processoDt.setValor(rs1.getString("VALOR"));
				
				ProcessoParteDt primeiroPromovente 	= ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE"));
				ProcessoParteDt primeiroPromovido 	= ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));		
		
				if(primeiroPromovente!=null)
					processoDt.addListaPoloAtivo(primeiroPromovente);
				
				if(primeiroPromovido!=null)
				processoDt.addListaPolosPassivos(primeiroPromovido);

				//Seta o hash aqui.
				processoDt.setHash(usuarioNe.getCodigoHash(processoDt.getId()));
				
				listaProcessos.add(processoDt);
				
			}

			//Conta registros			
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;
			
			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));		
			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}
	
	/*public List getListaProcessosComHashWebService(ResultSetTJGO rs1, UsuarioNe usuarioNe, boolean consultaTodasPartes) throws Exception{
		List listaProcesso = getListaProcessosWebService(rs1, consultaTodasPartes);
		for (int i = 0; i < listaProcesso.size(); i++) {
			ProcessoDt processoDt = (ProcessoDt) listaProcesso.get(i);
			processoDt.setHash(usuarioNe.getCodigoHash(processoDt.getId()));

		}
		return listaProcesso;
	}*/
	
	public List<ProcessoDt> consultarProcessosFazendaPublicaSemGuiaUnica() throws Exception {
		String Sql;
		ProcessoDt processoDt = null;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Map<String, ProcessoDt> mapProcesso = new LinkedHashMap();
		List<ProcessoDt> listaProcessos = new ArrayList();
		int quantidadeDeProcessos = 0;
		
		Sql = "SELECT * ";
		Sql += " FROM PROJUDI.VIEW_PROC PC ";
		Sql += " WHERE PROC_STATUS_CODIGO IN (?, ?) "; ps.adicionarLong(ProcessoStatusDt.ATIVO); ps.adicionarLong(ProcessoStatusDt.ATIVO_PROVISORIAMENTE);
		// No caso de possuir mais de um recurso ativo...
		Sql += " AND (ID_SERV IN (?,?)) ";  ps.adicionarLong(613); ps.adicionarLong(1254);
		Sql += " AND (ID_SERV_RECURSO IS NULL OR ID_SERV_RECURSO = ID_SERV)";
		Sql += " AND (ID_PROC_TIPO = ?) "; ps.adicionarLong(ProcessoNe.getIdTipoExecucaoFiscal());
		Sql += " AND NOT EXISTS (SELECT 1 ";
		Sql += "                   FROM PROJUDI.GUIA_EMIS GE ";
		Sql += "                  WHERE PC.ID_PROC = GE.ID_PROC  ";
		Sql += "                   AND ID_GUIA_MODELO IN (SELECT ID_GUIA_MODELO FROM PROJUDI.GUIA_MODELO WHERE ID_GUIA_TIPO IN (?,?))"; ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA);  ps.adicionarLong(GuiaTipoDt.ID_FAZENDA_PUBLICA_AUTOMATICA);
		Sql += "                 ) ";
		Sql += " AND EXISTS (SELECT 1 ";
		Sql += "               FROM PROJUDI.VIEW_ITENS_CALC_ARRECADACAO ICA ";
		Sql += "              WHERE ICA.ID_PROC_TIPO = PC.ID_PROC_TIPO  ";
		Sql += "               AND ICA.ID_GUIA_TIPO IN (?,?)"; ps.adicionarLong(GuiaTipoDt.ID_PREFEITURA_AUTOMATICA); ps.adicionarLong(GuiaTipoDt.ID_FAZENDA_PUBLICA_AUTOMATICA);
		Sql += "             ) ";

		try{
			rs1 = consultar(Sql, ps);			
			while (rs1.next()) {
				
				processoDt = mapProcesso.get(rs1.getString("ID_PROC"));										
				if (processoDt == null){
					if (quantidadeDeProcessos >= 1000) {
						listaProcessos.addAll(mapProcesso.values()); //Adiciona na lista os valores do map
						mapProcesso.clear();
						mapProcesso = null;
						
						return listaProcessos;
					}
					processoDt = new ProcessoDt();
					associarDt(processoDt, rs1);
					
					processoDt.setId_AreaDistribuicao(rs1.getString("ID_AREA_DIST"));
					processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
					processoDt.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));										
					
					mapProcesso.put(rs1.getString("ID_PROC"), processoDt);
					quantidadeDeProcessos += 1;
				}
			}	
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		
		listaProcessos.addAll(mapProcesso.values()); //Adiciona na lista os valores do map
		mapProcesso.clear();
		mapProcesso = null;
		
		return listaProcessos;
	}

	/**
	 * Consulta os processos ativos no Escritório Jurídico.
	 * 
	 * @param nomeParte
	 * @param pesquisarNomeExato
	 * @param cpfCnpjParte
	 * @param processoNumero
	 * @param digito
	 * @param ano
	 * @param statusCodigo
	 * @param idEscritorio
	 * @param posicao
	 * @return
	 * @throws Exception
	 * @author hmgodinho, tamaralsantos
	 */
	
	/* NÃO SERÁ MAIS UTILIZADO, VAI USAR DE ADVOGADO
	public List consultarProcessosEscritorioJuridico(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String statusCodigo, String idEscritorio, String posicao) throws Exception {
		return consultarProcessosEscritorioJuridico(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, digito, ano, statusCodigo, idEscritorio, null, posicao);
	}
	
	public List consultarProcessosEscritorioJuridico(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String statusCodigo, String idEscritorio, String id_Classificador, String posicao) throws Exception {
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String stSqlCamposRecurso = "";
		String stSqlComumRecurso = "";		
		String sql = "";	
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO(); 
		
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, ID_RECURSO, SEGREDO_JUSTICA, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV ";

		////Se a consulta utiliza filtro de parte, utiliza um view diferente para realizar a consulta
		if (nomeParte.length() > 0 || cpfCnpjParte.length() > 0){
			stSqlCamposProcesso += ", ID_PROC_PARTE, NOME, PROC_PARTE_TIPO_CODIGO ";
			stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE_PROC_P p ";
		} else {
			stSqlCamposProcesso += ", NULL AS ID_PROC_PARTE, NULL AS NOME, NULL AS PROC_PARTE_TIPO_CODIGO ";
			stSqlComumProcesso = " FROM PROJUDI.VIEW_BUSCA_PROC_ESCRITORIO_P p";
		}	

		//Prepara filtros para consulta
		String sqlCondicoes = this.filtroProcessoEscritorioJuridico(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, digito, ano, null, statusCodigo, null, idEscritorio, null, null, null, id_Classificador, ps);
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += " WHERE " + sqlCondicoes;

		
		stSqlCamposRecurso = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, ID_RECURSO, SEGREDO_JUSTICA, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV ";

		////Se a consulta utiliza filtro de parte, utiliza um view diferente para realizar a consulta
		if (nomeParte.length() > 0 || cpfCnpjParte.length() > 0){
			stSqlCamposRecurso += ", ID_PROC_PARTE, NOME, PROC_PARTE_TIPO_CODIGO ";
			stSqlComumRecurso = " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE_PROC_R p ";
		} else {
			stSqlCamposRecurso += ", NULL AS ID_PROC_PARTE, NULL AS NOME, NULL AS PROC_PARTE_TIPO_CODIGO ";
			stSqlComumRecurso = " FROM PROJUDI.VIEW_BUSCA_PROC_ESCRITORIO_R p";
		}	

		//Prepara filtros para consulta
		sqlCondicoes = this.filtroProcessoEscritorioJuridico(nomeParte, pesquisarNomeExato, cpfCnpjParte, processoNumero, digito, ano, null, statusCodigo, null, idEscritorio, null, null, null, id_Classificador, ps);
		if (sqlCondicoes.length() > 0) stSqlComumRecurso += " WHERE " + sqlCondicoes;
		
		try{
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " UNION ";
			sql += stSqlCamposRecurso;
			sql += stSqlComumRecurso;
			sql += " ORDER BY PROC_PRIOR_ORDEM ,DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			listaProcessos = getListaProcessos(rs1, false);
			
			//Conta registros 
			sql = "SELECT (SELECT COUNT(distinct p.ID_PROC) ";
			sql += stSqlComumProcesso;
			sql += ") + (SELECT COUNT(distinct p.ID_PROC) ";
			sql += stSqlComumRecurso;
			sql += ") AS QUANTIDADE FROM DUAL";
			
			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}
	
	*/
	
	/**
	 * Cria o filtro da consulta os processos ativos no Escritório Jurídico.
	 * 
	 * @param nomeParte
	 * @param pesquisarNomeExato
	 * @param cpfCnpjParte
	 * @param processoNumero
	 * @param digito
	 * @param ano
	 * @param tcoNumero
	 * @param statusCodigo
	 * @param id_ProcessoTipo
	 * @param idEscritorio
	 * @param id_ServentiaSubTipo
	 * @param id_Comarca
	 * @param segredoJustica
	 * @param id_Classificador
	 * @param ps
	 * @return sql com as condições da cláusula where
	 * @throws Exception
	 * @author hmgodinho
	 */
	private String filtroProcessoEscritorioJuridico(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, String processoNumero, String digito, String ano, String tcoNumero, String statusCodigo, String id_ProcessoTipo, String idEscritorio, String id_ServentiaSubTipo, String id_Comarca, String segredoJustica, String id_Classificador, PreparedStatementTJGO ps) throws Exception{

		String sqlFiltro = "";
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
		
		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==null || processoNumero.length()==0) &&
				(tcoNumero==null || tcoNumero.length()==0) &&
				(id_Comarca==null || id_Comarca.length()==0) &&
				(cpfCnpjParte==-1)  &&
				(idEscritorio==null || idEscritorio.length()==0) &&
				(id_Classificador==null || id_Classificador.length()==0)	){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: o nome da parte, cpf/cnpj, número do processo, o número do tco, escritório jurídico, a comarca ou classificador. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
			
		}
		
		//Monta Sql dec Condições

		//Se pesquisarNomeExato == true, deve ser feita pesquisa por nome exato.
		if (nomeParteSimplificado.length() > 0) {
			if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {	
				sqlFiltro += " NOME_SIMPLIFICADO = ?";  ps.adicionarString(nomeParteSimplificado);
			} else {
				sqlFiltro += " NOME_SIMPLIFICADO like ?";  ps.adicionarString(nomeParteSimplificado +"%");				
			}			
		}

		if (cpfCnpjParte !=-1) {
			if (sqlFiltro.length() > 0) sqlFiltro += " OR ";
			sqlFiltro += " CPF = ? ";
			ps.adicionarLong(cpfCnpjParte);
			sqlFiltro += " OR CNPJ = ? ";
			ps.adicionarLong(cpfCnpjParte);
		}
		
		//Se a consulta utiliza filtro de parte, utiliza o filtro por tipo de parte
		if (nomeParteSimplificado.length() > 0 || cpfCnpjParte !=-1){
			if (sqlFiltro.length() > 0) sqlFiltro += " AND ";
			sqlFiltro += " (PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ? OR PROC_PARTE_TIPO_CODIGO = ?)";
			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
			ps.adicionarLong(ProcessoParteTipoDt.POLO_ATIVO_CODIGO);
			ps.adicionarLong(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO);
		}

		if (processoNumero != null && processoNumero.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " OR " : "") + " PROC_NUMERO  = ? ";
			ps.adicionarLong(processoNumero);
		}
		if (digito != null && digito.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " DIGITO_VERIFICADOR  = ? ";
			ps.adicionarLong(digito);
		}
		if (ano != null && ano.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " OR " : "") + " ANO  = ? ";
			ps.adicionarLong(ano);
		}
		if (tcoNumero != null && tcoNumero.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " OR " : "") + " TCO_NUMERO  = ? ";
			ps.adicionarString(tcoNumero);
		}
		if (sqlFiltro.length() > 0) sqlFiltro = "(" + sqlFiltro + ")"; //Concatena Sql's
		if (statusCodigo != null && statusCodigo.length() > 0) {
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " PROC_STATUS_CODIGO = ? ";
			ps.adicionarLong(statusCodigo);
		}
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_PROC_TIPO = ? ";
			ps.adicionarLong(id_ProcessoTipo);
		}
		if (idEscritorio != null && idEscritorio.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_ESCRITORIO = ? ";
			ps.adicionarLong(idEscritorio);
		}
		if (id_ServentiaSubTipo != null && id_ServentiaSubTipo.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_SERV_SUBTIPO = ? ";
			ps.adicionarLong(id_ServentiaSubTipo);
		}
		if (id_Comarca != null && id_Comarca.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_COMARCA = ? ";
			ps.adicionarLong(id_Comarca);
		}
		if (segredoJustica != null && segredoJustica.equals("false")){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " SEGREDO_JUSTICA = ? ";
			ps.adicionarBoolean(segredoJustica);
		}
		if (id_Classificador != null && id_Classificador.length() > 0){
			sqlFiltro += (sqlFiltro.length() > 0 ? " AND " : "") + " ID_CLASSIFICADOR = ? ";
			ps.adicionarLong(id_Classificador);
		}

		return sqlFiltro;
	}
	

	/**
	 * Método para alterar classe(processo tipo) e/ou valor da causa do processo.
	 * 
	 * @param ProcessoDt processoDt
	 * @return boolean
	 */
	public boolean alterarProcessoTipoValorCausa(ProcessoDt processoDt) throws Exception {

		String sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.PROC SET ";

		boolean campoAdicionado = false;
		
		if (processoDt.getId_ProcessoTipo() != null && processoDt.getId_ProcessoTipo().length() > 0) {
			sql += "ID_PROC_TIPO  = ? ";
			ps.adicionarLong(processoDt.getId_ProcessoTipo());
			campoAdicionado = true;
		}
		
		if (processoDt.getValor() != null && processoDt.getValor().length() > 0) {
			if( campoAdicionado ) {
				sql += ",";
			}
			sql += "VALOR  = ? ";
			ps.adicionarDecimal(processoDt.getValor());
		}
		
		sql += " WHERE ID_PROC = ? ";
		ps.adicionarLong(processoDt.getId());
		
		return executarUpdateDelete(sql, ps) > 0;
		
	}
	
	/**
	 * Método que consulta a quantidade de processsos ativos vinculados a um classificador.
	 * @param idClassificador 
	 * @return quantidade de processos localizados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public int consultarQuantidadeProcessosAtivosClassificador(String idClassificador) throws Exception {
		int quantidade = 0;
		String Sql;
		ProcessoDt Dados = new ProcessoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT COUNT(1) AS QTDE FROM PROJUDI.VIEW_PROC WHERE ID_CLASSIFICADOR = ? AND DATA_ARQUIVAMENTO IS NULL"; 
		ps.adicionarLong(idClassificador);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				quantidade = rs1.getInt("QTDE");
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return quantidade;
	}

	public String consultarAssistencia(String idProcesso) throws Exception {
		String idCustaTipo = "";
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT ID_CUSTA_TIPO FROM PROJUDI.VIEW_PROC WHERE ID_PROC = ? "; 
		ps.adicionarLong(idProcesso);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				idCustaTipo = rs1.getString("ID_CUSTA_TIPO");
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return idCustaTipo;
	}
	
	public int consultarQuantidadeProcessosArquivadosClassificador(String idClassificador) throws Exception {
		int quantidade = 0;
		String Sql;
		ProcessoDt Dados = new ProcessoDt();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT COUNT(1) AS QTDE FROM PROJUDI.VIEW_PROC WHERE ID_CLASSIFICADOR = ? AND DATA_ARQUIVAMENTO IS NOT NULL"; 
		ps.adicionarLong(idClassificador);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				quantidade = rs1.getInt("QTDE");
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return quantidade;
	}
	
	/**
	 * Verifica se o processo está em uma serventia que possua um dos subTipos que não se aplica a contagem de prazo do novo CPC
	 * 
	 * @param id_Processo, identificação do processo
	 * @param conexao,
	 *            conexão com o banco de dados
	 * 
	 * @author lsbernardes
	 */
	public boolean isUtilizaPrazoCorrido(String id_processo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean retorno = false;
		
		// Devera verificar se o processo está em uma serventia que possua um dos subTipos abaixo
		//(Juizado Especial Cível, Juizado Especial Criminal, Juizado Especial Cível e Criminal, Juizado Especial Criminal e Fazenda Pública, Juizado Especial Fazenda Pública)
        //(Turma Recursal Cível, Turma Recursal Criminal, Turma Recursal Cível e Criminal)
		
		Sql = " SELECT p.id_proc, p.PROC_NUMERO || '.' || p.DIGITO_VERIFICADOR as NumProc, s.ID_SERV, s.SERV_CODIGO, s.SERV, ss.ID_SERV_SUBTIPO, ss.SERV_SUBTIPO_CODIGO, ss.SERV_SUBTIPO ";
		Sql += " FROM PROC p INNER JOIN SERV s ON (p.id_serv = s.id_serv) INNER JOIN SERV_SUBTIPO ss ON (s.ID_SERV_SUBTIPO = ss.id_serv_subtipo) ";
		Sql += " WHERE ss.SERV_SUBTIPO_CODIGO IN (?,";		ps.adicionarLong(ServentiaSubtipoDt.VARA_CRIMINAL);
		Sql += "?,";										ps.adicionarLong(ServentiaSubtipoDt.UPJ_CRIMINAL);
		Sql += "?,";										ps.adicionarLong(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA);	
		Sql += "?,"; 										ps.adicionarLong(ServentiaSubtipoDt.UPJ_CUSTODIA);
		Sql += "?,";										ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL);
		Sql += "?,";										ps.adicionarLong(ServentiaSubtipoDt.VARAS_EXECUCAO_PENAL_ALTERNATIVA);
		Sql += "?,";										ps.adicionarLong(ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL);
		Sql += "?,";										ps.adicionarLong(ServentiaSubtipoDt.SECAO_CRIMINAL);
		Sql += "?)";										ps.adicionarLong(ServentiaSubtipoDt.CAMARA_CRIMINAL);
		
		Sql += " AND p.id_proc = ? ";										ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				retorno = true;
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return retorno;
	}
	
	/**
     * Verificar se o processo está em uma serventia que possua um dos subTipos de juizados.
     * @param String id_processo
     * @return
     * @throws Exception
     * @author fasoares
     */
	public boolean isProcessoJuizados(String id_processo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean retorno = false;
		
		// Devera verificar se o processo está em uma serventia que possua um dos subTipos abaixo
		//(Juizado Especial Cível, Juizado Especial Criminal, Juizado Especial Cível e Criminal, Juizado Especial Criminal e Fazenda Pública, Juizado Especial Fazenda Pública)
        //(Turma Recursal Cível, Turma Recursal Criminal, Turma Recursal Cível e Criminal)
		
		Sql = " SELECT p.id_proc, p.PROC_NUMERO || '.' || p.DIGITO_VERIFICADOR as NumProc, s.ID_SERV, s.SERV_CODIGO, s.SERV, ss.ID_SERV_SUBTIPO, ss.SERV_SUBTIPO_CODIGO, ss.SERV_SUBTIPO ";
		Sql += " FROM PROC p INNER JOIN SERV s ON (p.id_serv = s.id_serv) INNER JOIN SERV_SUBTIPO ss ON (s.ID_SERV_SUBTIPO = ss.id_serv_subtipo) ";
		Sql += " WHERE ss.SERV_SUBTIPO_CODIGO IN (?,?,?,?,?,?) AND p.id_proc = ? "; 
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL);
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA);
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		ps.adicionarLong(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		
		ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				retorno = true;
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return retorno;
	}
	
	/**
	 * Verifica se o processo está em uma serventia que possua subTipo vara de precatorias
	 * 
	 * @param id_Processo, identificação do processo
	 * @param conexao,
	 *            conexão com o banco de dados
	 * 
	 * @author lsbernardes
	 */
	public boolean isProcessoVaraPrecatoria(String id_processo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean retorno = false;
		
		// Devera verificar se o processo está em uma serventia que possua um dos subTipos abaixo
		//(Juizado Especial Cível, Juizado Especial Criminal, Juizado Especial Cível e Criminal, Juizado Especial Criminal e Fazenda Pública, Juizado Especial Fazenda Pública)
        //(Turma Recursal Cível, Turma Recursal Criminal, Turma Recursal Cível e Criminal)
		
		Sql = " SELECT p.id_proc, p.PROC_NUMERO || '.' || p.DIGITO_VERIFICADOR as NumProc, s.ID_SERV, s.SERV_CODIGO, s.SERV, ss.ID_SERV_SUBTIPO, ss.SERV_SUBTIPO_CODIGO, ss.SERV_SUBTIPO ";
		Sql += " FROM PROC p INNER JOIN SERV s ON (p.id_serv = s.id_serv) INNER JOIN SERV_SUBTIPO ss ON (s.ID_SERV_SUBTIPO = ss.id_serv_subtipo) ";
		Sql += " WHERE ss.SERV_SUBTIPO_CODIGO IN (?) AND p.id_proc = ? "; 
		ps.adicionarLong(ServentiaSubtipoDt.PRECATORIA);
		ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				retorno = true;
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return retorno;
	}
	
	/**
	 * Consulta os dados para montar o número completo do processo
	 * @param numero
	 * @param digito
	 * @param ano
	 * @return
	 * @throws Exception
	 */
	public String consultarNumeroCompletoDoProcesso(String numero, String digito, String ano) throws Exception {
		String numeroCompleto = "";
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			sql = "SELECT p.ANO, p.DIGITO_VERIFICADOR, p.PROC_NUMERO, p.FORUM_CODIGO";
			sql += " FROM PROJUDI.VIEW_PROC p"; 
			sql += " WHERE p.PROC_NUMERO = ?"; ps.adicionarLong(numero);
			sql += " AND p.DIGITO_VERIFICADOR = ?"; ps.adicionarLong(digito);
			sql += " AND p.ANO = ?"; ps.adicionarLong(ano);						 

			rs = consultar(sql, ps);
			if (rs.next()) {
				numeroCompleto = Funcoes.completarZeros(rs.getString("PROC_NUMERO"), 7) + "-" + Funcoes.completarZeros(rs.getString("DIGITO_VERIFICADOR"), 2) + "." + rs.getString("ANO") + "." + Configuracao.JTR + "." + Funcoes.completarZeros(rs.getString("FORUM_CODIGO"), 4);
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return numeroCompleto;
	}
	
	/**
	 * Consulta os ids de processos que foram vinculados à guias iniciais do SPG
	 * @return
	 * @throws Exception
	 */
	public List<String> consultarIdsProcessosGuiaInicialSPG() throws Exception {
		List<String> ids = new ArrayList<String>();
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			sql = "SELECT p.ID_PROC";
			sql += " FROM PROJUDI.PROC p"; 
			sql += " WHERE TABELA_ORIGEM_TEMP = 'GUIAINICIALSPG'";
			sql += " AND CODIGO_TEMP IS NOT NULL";									 

			rs = consultar(sql, ps);
			while (rs.next()) {
				ids.add(rs.getString("ID_PROC"));				
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return ids;
	}
	
	/**
	 * Vincula a guia inicial ao processo Projudi
	 *
	 * @param idProcesso identificação do processo
	 * @param idGuia identificação da guia projudi ou isn da guia spg
	 * @param isGuiaSPG indica se a guia é do spg
	 */
	public void vincularGuiaInicialAoProcesso(String idProcesso, String idGuia, boolean isGuiaSPG) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET CODIGO_TEMP = ?, TABELA_ORIGEM_TEMP = ? ";
		ps.adicionarLong(idGuia);
		if (isGuiaSPG) 
			ps.adicionarString("GUIAINICIALSPG");
		else 
			ps.adicionarString("GUIAINICIALPROJUDI");
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(idProcesso);

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * @param id_arquivo
	 * 
	 * @return
	 * @throws Exception
	 */
	public ProcessoDt consultarPorIdArquivo(String id_arquivo) throws Exception {
		ProcessoDt processoDt = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.FORUM_CODIGO, p.ANO, s.ID_SERV, s.SERV, s.SERV_CODIGO");
		sql.append(" FROM projudi.PROC p");
		sql.append(" JOIN projudi.MOVI m ON p.id_proc = m.id_proc");
		sql.append(" JOIN projudi.MOVi_ARQ ma ON ma.id_movi = m.id_movi");
		sql.append(" INNER JOIN projudi.SERV s ON s.id_serv = p.id_serv");
		sql.append(" WHERE ma.id_arq = ?");
		ps.adicionarLong(id_arquivo);		
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId(rs.getString("ID_PROC"));
				processoDt.setProcessoNumero(rs.getString("PROC_NUMERO"));
				processoDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));				
				processoDt.setAno(rs.getString("ANO"));
				processoDt.setForumCodigo(rs.getString("FORUM_CODIGO"));
				processoDt.setId_Serventia(rs.getString("ID_SERV"));
				processoDt.setServentia(rs.getString("SERV"));
				processoDt.setServentiaCodigo(rs.getString("SERV_CODIGO"));
			}		
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e1) {}
		}
		return processoDt;		
	}
	
	public List<String> consultarIdsProcessosAtivos() throws Exception {
		List<String> ids = new ArrayList<String>();
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			sql = "SELECT p.ID_PROC";
			sql += " FROM PROJUDI.PROC p"; 
			sql += " WHERE ID_PROC_STATUS <> 2";												 

			rs = consultar(sql, ps);
			while (rs.next()) {
				ids.add(rs.getString("ID_PROC"));				
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return ids;
	}
	
	public List<String> consultarIdsProcessos(String id_Serventia, String id_Classificador) throws Exception, MensagemException {
		List<String> ids = new ArrayList<String>();
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			sql = "SELECT p.ID_PROC";
			sql += " FROM PROJUDI.PROC p"; 
			sql += " WHERE ID_SERV = ?"; ps.adicionarLong(id_Serventia);
			sql += " AND ID_CLASSIFICADOR = ?"; ps.adicionarLong(id_Classificador);									 

			rs = consultar(sql, ps);
			while (rs.next()) {
				ids.add(rs.getString("ID_PROC"));				
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return ids;
	}

	public String getPrioridade(String id_processo) throws Exception {
		String id_ProcessoPrioridade ="";
		String sql;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
			sql = "SELECT p.ID_PROC_PRIOR ";
			sql += " FROM PROJUDI.PROC p"; 
			sql += " WHERE ID_PROC = ?"; ps.adicionarLong(id_processo);												 

			rs = consultar(sql, ps);
			if(rs.next()) {
				id_ProcessoPrioridade = rs.getString("ID_PROC_PRIOR");				
			}
		
		} finally{
			try{
				if (rs != null) rs.close();
			} catch(Exception e) {
			}
		}
		return id_ProcessoPrioridade;
	}
	
	/**
	 * Método responsável em consultar seo advogado tem parte no processo.
	 * 
	 * @param idProcesso
	 * @param id_UsuarioServentia
	 * 
	 * @author tamaralsantos
	 */
	public Boolean verificarAdvogadoParteProcesso(String idProcesso, String id_UsuarioServentia, String id_ServentiaUsuario) throws Exception {

		String sql = "";
		boolean retorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		

		sql = "SELECT * FROM PROJUDI.VIEW_BUSCA_PROC_ADVOGADO p ";
		sql +=  "  WHERE p.ID_PROC  = ? "; 				ps.adicionarLong(idProcesso);
		
		if(id_UsuarioServentia!=null) {
			sql +=  " AND p.ID_USU_SERV  = ?  ";				ps.adicionarLong(id_UsuarioServentia);
		} else {
			sql += " AND p.ID_SERV_USU = ?";					ps.adicionarLong(id_ServentiaUsuario);
		}


		try{
			//Efetua consulta
			rs1 = consultar(sql, ps);

			//Essa primeira consulta retorna somente os processos
			if (rs1.next()) {
				retorno = true;
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return retorno;
	}
	
	/**
	 * Método responsável em consultar seo advogado tem parte no processo.
	 * 
	 * @param idProcesso
	 * @param id_ServCargo
	 * 
	 * @author tamaralsantos
	 */
	public Boolean verificarPromotorParteProcesso(String idProcesso, String id_ServCargo) throws Exception {

		String stSql = "";
		boolean retorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		stSql = "  Select 1";		
		stSql += "  from Proc p ";
		stSql += "   inner join proc_resp pr on p.id_proc = pr.id_proc ";
		stSql += "   inner join proc_status ps on p.id_proc_status = ps.id_proc_status ";
		stSql +=  "  WHERE p.ID_PROC  = ? ";							ps.adicionarLong(idProcesso);		
		stSql += "	  and Id_Serv_Cargo = ? "; 				ps.adicionarLong(id_ServCargo);

		try{
			//Efetua consulta
			rs1 = consultar(stSql, ps);

			//Essa primeira consulta retorna somente os processos
			if (rs1.next()) {
				retorno = true;
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return retorno;
	}
	
	/**
	 * Verifica se o processo está com o status SUSPENSO
	 * @param id_Processo
	 * @return
	 * @throws Exception
	 */
	public boolean isProcessoSuspenso(String id_Processo) throws Exception {		
		boolean retorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT p.id_proc");
		sql.append(" FROM PROJUDI.PROC p INNER JOIN PROJUDI.PROC_STATUS ps ON p.id_proc_status = ps.id_proc_status");
		sql.append(" WHERE ps.proc_status_codigo = " + ProcessoStatusDt.SUSPENSO);		
		sql.append(" AND p.id_proc = ?");		
		ps.adicionarLong(id_Processo);
		try {
			rs1 = consultar(sql.toString(), ps);
			retorno = rs1.next();		
		}
		finally {
			try {if (rs1 != null) rs1.close();} catch(Exception e) {}
		}	
		return retorno;
	}
	
	/**
	 * Consulta geral de processos
	 */
	public String consultarDescricaoJSON(String processo, String posicao) throws Exception {
		
		String Sql = "";
		String SqlFrom = "";
		String SqlOrder = "";
		String stTemp = "";
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		int qtdeColunas = 1;
				
		Sql = "SELECT ID_PROC AS id, (PROC_NUMERO || '.' || DIGITO_VERIFICADOR || '.' || ANO) AS descricao1 ";
		SqlFrom = " FROM PROJUDI.VIEW_PROC " ;
		SqlFrom += " WHERE PROC_NUMERO = ? ";
		ps.adicionarString(processo);
		SqlOrder = " ORDER BY ID, PROC_NUMERO, ANO ";
		
		try{
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);			
			Sql = "SELECT COUNT(*) AS QUANTIDADE ";
			rs2 = consultar(Sql + SqlFrom, ps);
			rs2.next();
			stTemp = gerarJSON(rs2.getLong("QUANTIDADE"), posicao, rs1, qtdeColunas);
        } finally{
             try{if (rs1 != null) rs1.close();} catch(Exception e1) {}
             try{if (rs2 != null) rs2.close();} catch(Exception e1) {}
        }
		return stTemp;
	}
	
	public void limparClassificadorProcesso(String id_Processo) throws Exception{
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		String stSql="";

		stSql= "UPDATE PROJUDI.PROC SET  ";
		stSql+= "ID_CLASSIFICADOR = Null";
		stSql += " WHERE ID_PROC  = ? "; 		ps.adicionarLong(id_Processo); 

		executarUpdateDelete(stSql,ps);
	}
	
	
	
	
	/**
	 * Altera dados do processo em virtude da alteração do status de sigilo
	 * 
	 * @param processoDt dt do processo
	 * 
	 * @author msapaula
	 */
	public void alterarProcessoSigilo(ProcessoDt processoDt) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET";
		Sql += " ID_PROC_STATUS = (SELECT ID_PROC_STATUS FROM PROJUDI.PROC_STATUS WHERE PROC_STATUS_CODIGO = ?)";
		ps.adicionarLong(processoDt.getProcessoStatusCodigo());
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(processoDt.getId());

		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Método responsável em consultar os processos de acordo com parâmetros passados.
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * @param posicao parametro para paginação
	 * 
	 * @author jrcorrea 8/2/2018
	 */
	public List consultarProcessosSigiloso( String id_ServentiaCargo, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
			
		if(	(id_ServentiaCargo==null || id_ServentiaCargo.length()==0))	{			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: o nome da parte, cpf/cnpj, número do processo, a serventia, a comarca ou classificador ");			
		}

		//Realiza SELECT para trazer processos de 2º grau e une com os recursos inominados
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DATA_RECEBIMENTO, p.DIGITO_VERIFICADOR, p.ANO, p.FORUM_CODIGO, p.SEGREDO_JUSTICA, p.SERV, p.PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.ID_PROC_TIPO  ";

		//Se a consulta utiliza filtro de parte, utiliza um view diferente para realizar a consulta
		stSqlCamposProcesso += ", p.PRIMEIRO_PROMOVENTE, p.PRIMEIRO_PROMOVIDO ";			
		stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_SIGILOSO p ";
									
		String sqlCondicoes =  " p.id_serv_cargo = ?";		ps.adicionarLong(id_ServentiaCargo);		
		
		stSqlComumProcesso += " WHERE " + sqlCondicoes;	
			
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sql, ps, posicao);
			//listaProcessos = getListaProcessos(rs1, false);
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));

				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;	

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	/**
	 * Método responsável em consultar os processos de uma turma recursal ou câmara cível (2º grau) Sigilosos.  
	 * @param id_ServentiaCargo, identificação do cargo responsável por processo a ser filtrado
	 * @param posicao parametro para paginação
	 * 
	 * @author jrcorrea 8/2/2018
	 */
	public List consultarProcessosSigilososSegundoGrau(  String id_ServentiaCargoResponsavel, String posicao) throws Exception {
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";				
		
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		
		if(	id_ServentiaCargoResponsavel==null || id_ServentiaCargoResponsavel.length()==0) {			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: número do processo, o nome da parte, cpf/cnpj. ");			
		}

		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, p.FORUM_CODIGO, p.PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, SERV, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, ID_PROC_TIPO ";
		stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_SIGILOSO p ";
		stSqlComumProcesso += " INNER JOIN PROJUDI.PROC_RESP pr on (p.ID_PROC = pr.ID_PROC";
		stSqlComumProcesso += " AND pr.ID_SERV_CARGO = ? AND pr.CODIGO_TEMP = ?)";					ps.adicionarLong(id_ServentiaCargoResponsavel);			ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
		
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sql, ps, posicao);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));

				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}

	public List consultarProcessosSigilosoPromotor(String id_ServentiaCargo, String posicao) throws Exception {
		String Sql;
		String SqlFrom;
		String SqlOrder;		
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ProcessoDt processoDt;
						
		Sql = " SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, PROC_PRIOR_CODIGO, PROC_PRIOR_ORDEM, DIGITO_VERIFICADOR, ANO, FORUM_CODIGO, SEGREDO_JUSTICA, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV, ID_PROC_TIPO ";	
		
		SqlFrom = " FROM VIEW_BUSCA_PROC_SIGILOSO p ";
		SqlFrom += " WHERE ID_SERV_CARGO = ?";   			ps.adicionarLong(id_ServentiaCargo);							
		SqlFrom += " AND PROC_STATUS_CODIGO = ?";  			ps.adicionarLong(ProcessoStatusDt.SIGILOSO);				
			
		try{

			SqlOrder = " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(Sql + SqlFrom + SqlOrder, ps, posicao);
			
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
								
				ProcessoParteDt primeiroPromoventeRecorrente = null;
				ProcessoParteDt primeiroPromovidoRecorrido = null;				
		
				primeiroPromoventeRecorrente = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVENTE"));
				primeiroPromovidoRecorrido = ProcessoParteDt.obtenhaProcessoParte(rs1.getString("PRIMEIRO_PROMOVIDO"));

                if (primeiroPromoventeRecorrente != null) processoDt.addListaPoloAtivo(primeiroPromoventeRecorrente);                
                if (primeiroPromovidoRecorrido != null) processoDt.addListaPolosPassivos(primeiroPromovidoRecorrido);
				
                listaProcessos.add(processoDt);
			}
	
			Sql = "SELECT COUNT(DISTINCT p.ID_PROC) AS QUANTIDADE";
			rs2 = consultar(Sql + SqlFrom, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));			
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}
	
	/**
	 *  
	 * @param id_ServentiaCargo 
	 * @author jrcorrea 21/02/2018
	 */
	public long consultarQuantidadeSigilosos(String id_ServentiaCargo) throws Exception {
		long loQtd = 0;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{
					
			stSql = "  Select Count(1) As Quantidade ";
			stSql += "  from Proc p ";
			stSql += "   inner join proc_resp pr on p.id_proc = pr.id_proc ";
			stSql += "   inner join proc_status ps on p.id_proc_status = ps.id_proc_status ";
			stSql += "	  where Id_Serv_Cargo = ? "; 				ps.adicionarLong(id_ServentiaCargo);
			stSql += "	  and  ps.proc_status_codigo  = ? "; 		ps.adicionarLong(ProcessoStatusDt.SIGILOSO);
			stSql += "	  and  pr.codigo_temp  = ? "; 		ps.adicionarLong(ProcessoResponsavelDt.ATIVO);


			
			rs1 = consultar(stSql, ps);
			if (rs1.next()) {				
				loQtd =  rs1.getLong("Quantidade") ;
			}
			
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return loQtd;
	}
	
	
	
	/**
	 * Método responsável em consultar seo advogado tem parte no processo.
	 * 
	 * @param idProcesso
	 * @param id_serventia
	 * 
	 * @author lsrodrigues
	 */
	public Boolean verificarPromotoriaParteProcesso(String idProcesso, String id_serventia) throws Exception {

		String sql = "";
		boolean retorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		

		sql = "SELECT 1 FROM PROJUDI.VIEW_BUSCA_PROC_PROMOTOR p ";
		sql +=  "  WHERE p.ID_PROC  = ? ";							ps.adicionarLong(idProcesso);		
		sql += " AND p.ID_SERV_USU   = ?  "; 					ps.adicionarLong(id_serventia);

		

		try{
			//Efetua consulta
			rs1 = consultar(sql, ps);

			//Essa primeira consulta retorna somente os processos
			if (rs1.next()) {
				retorno = true;
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return retorno;
	}
	
	/**
	 * Altera o tipo de custa do processo
	 * 
	 * @param id_Processo identificação do processo
	 * @param id_Custa_Tipo novo tipo de custa
	 */
	public void alterarCustaTipoProcesso(String id_Processo, String id_Custa_Tipo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC SET ID_CUSTA_TIPO = ?";
		ps.adicionarLong(id_Custa_Tipo);
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);
		executarUpdateDelete(Sql, ps);
	}
	
	/**
	 * Verifica se o processo está em uma serventia que possua um dos subTipos que não se aplica a contagem de prazo do novo CPC
	 * 
	 * @param id_Processo, identificação do processo
	 * @param conexao,
	 *            conexão com o banco de dados
	 * 
	 * @author lsbernardes
	 */
	public boolean isProcessoJuizadosTurmas(String id_processo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		boolean retorno = false;
		
		// Devera verificar se o processo está em uma serventia que possua um dos subTipos abaixo
		//(Juizado Especial Cível, Juizado Especial Criminal, Juizado Especial Cível e Criminal, Juizado Especial Criminal e Fazenda Pública, Juizado Especial Fazenda Pública)
        //(Turma Recursal Cível, Turma Recursal Criminal, Turma Recursal Cível e Criminal)
		
		Sql = " SELECT p.id_proc, p.PROC_NUMERO || '.' || p.DIGITO_VERIFICADOR as NumProc, s.ID_SERV, s.SERV_CODIGO, s.SERV, ss.ID_SERV_SUBTIPO, ss.SERV_SUBTIPO_CODIGO, ss.SERV_SUBTIPO ";
		Sql += " FROM PROC p INNER JOIN SERV s ON (p.id_serv = s.id_serv) INNER JOIN SERV_SUBTIPO ss ON (s.ID_SERV_SUBTIPO = ss.id_serv_subtipo) ";
		Sql += " WHERE ss.SERV_SUBTIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,? ) AND p.id_proc = ? "; 
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL);
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL);
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL);
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA);
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		ps.adicionarLong(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA);
		ps.adicionarLong(ServentiaSubtipoDt.JUIZADO_VIOLENCIA_DOMESTICA);
		ps.adicionarLong(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL);
		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL);
		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL);
		ps.adicionarLong(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL);
		
		ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				retorno = true;
			}
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return retorno;
	}

	public long consultarQuantidadeSigilososDelegacia(String id_ServentiaOrigem) throws Exception {
		long loQtd = 0;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		try{

			stSql = "SELECT COUNT(*) AS Quantidade FROM PROJUDI.VIEW_PROC p";
			stSql += " where p.ID_SERV_ORIGEM = ?";					ps.adicionarLong(id_ServentiaOrigem);
			stSql += " and p.PROC_STATUS_CODIGO = ?";				ps.adicionarLong(ProcessoStatusDt.SIGILOSO);

			rs1 = consultar(stSql, ps);
			if (rs1.next()) {
				loQtd =  rs1.getLong("Quantidade") ;
			}
			//rs1.close();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return loQtd;
		
	}	

	/**
	 * Método responsável em consultar os processos sigilosos da delegacia 
	 * @param id_Serventia, identificação da delegacia
	 * @param posicao parametro para paginação
	 * 
	 * @author jrcorrea 5/6/2018
	 */
	public List consultarProcessosSigilososDelegacia( String id_Serventia,   String posicao) throws Exception {
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";		
		String sqlCondicoes = "";
		
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		
		if(	(id_Serventia==null || id_Serventia.length()==0)){			
			throw new MensagemException("Ocorreu algum problema na consulta favor tentar mais tarde. Delegacia não indentificada.");
			
		}

		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, p.FORUM_CODIGO, p.PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, SERV, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, ID_PROC_TIPO ";
		stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_SIGILOSO p ";
				
		sqlCondicoes +=  " WHERE ID_SERV_ORIGEM = ?";						ps.adicionarLong(id_Serventia);
							
		
		try{
			stSqlComumProcesso += sqlCondicoes;
			
			sql = stSqlCamposProcesso +  stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sql, ps, posicao);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));

				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	/**
	 * 
	 * @param numeroProcesso
	 * @param digitoVerificador
	 * @param ano
	 * @param forumCodigo
	 * @return
	 * @throws Exception
	 */
	public ProcessoDt consultarIdCnjClassePorProcessoNumeroCompleto(String numeroProcesso, String digitoVerificador, String ano, String forumCodigo) throws Exception {
		
		ProcessoDt processoDt = null;
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT p.id_proc, pt.proc_tipo_codigo, pt.id_cnj_classe");
		sql.append(" FROM projudi.proc p ");
		sql.append(" INNER JOIN projudi.proc_tipo pt ON p.id_proc_tipo = pt.id_proc_tipo");
		sql.append(" WHERE pt.ATIVO = ? ");												ps.adicionarLong(1);
		
		sql.append(" AND p.proc_numero = ?");												ps.adicionarLong(numeroProcesso);
		
		sql.append(" AND p.digito_verificador = ?");										ps.adicionarLong(digitoVerificador);
		
		sql.append(" AND p.ano = ?"); 														ps.adicionarLong(ano);
		
		if (forumCodigo != null) {
			sql.append(" AND p.forum_codigo = ?");
			ps.adicionarLong(forumCodigo);
		}
		
		try {
			rs = consultar(sql.toString(), ps);
			while (rs.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId(rs.getString("ID_PROC"));
				processoDt.setProcessoTipoCodigo(rs.getString("PROC_TIPO_CODIGO"));
				processoDt.setCodigoTemp(rs.getString("ID_CNJ_CLASSE"));
			}
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}		
		
		return processoDt;
	}

	public boolean isTemAssunto(String id_Processo) throws Exception {
		boolean boRetorno = false;
		
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		String stSql = "select id_assunto from proc_assunto where id_proc = ?"; ps.adicionarLong(id_Processo);
				
		try {
			rs = consultar(stSql, ps);
			if (rs.next()) {
				boRetorno = true;				
			}
		} finally {
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}			
				
		return boRetorno;
	}
	
	/**
	 * Método que consulta os Processos Paralisados por Assistente, podendo ser informado ou não o Id da Serventia.
	 * @param idServentia - Id da Serventia
	 * @param idServentiaCargo - Id da Serventia Cargo do Juiz Responsável
	 * @param posicaoPaginaAtual - posição da paginação
	 * @return lista de processos paralisados com periodo
	 * @throws Exception     
	 * @author Jesus - inclusão das pendencias tipos
	 */
	public List consultarProcessosParalisadosConclusoesSegundoGrauPorAssistente(String idServentia, String id_ServentiaCargo, String nomeAssistente, String opcaoPeriodo, String posicaoPaginaAtual) throws Exception {
		String sqlQuantidade = "";
		String sqlLista = "";
		String sqlTemp00 = "";
		String sqlGroupBy = "";

		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sqlTemp00 = " FROM PROJUDI.VIEW_PROCS_PARALISADOS_2GRAU p ";
		sqlTemp00 += " WHERE p.PEND_TIPO_CODIGO IN (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_SENTENCA);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_VOTO);
		ps.adicionarLong(PendenciaTipoDt.CONCLUSO_EMENTA);

		if (idServentia != null && !idServentia.equals("") && !idServentia.equals("null")) {
			sqlTemp00 += " AND ID_SERV = ?";
			ps.adicionarLong(idServentia);
		}
		
		if (id_ServentiaCargo != null && !id_ServentiaCargo.trim().equalsIgnoreCase("") && !id_ServentiaCargo.trim().equalsIgnoreCase("null")){
			sqlTemp00 += " AND ID_SERV_CARGO = ?";
			ps.adicionarLong(id_ServentiaCargo);
		} else {
			sqlTemp00 += " AND ID_SERV_CARGO IS NOT NULL";
		}
		
		if (nomeAssistente != null && !nomeAssistente.equals("") && !nomeAssistente.equals("null")) {
			sqlTemp00 += " AND ASSISTENTE = ?";
			ps.adicionarString(nomeAssistente);
		}

		if (opcaoPeriodo != null && !opcaoPeriodo.equals("") && !opcaoPeriodo.equals("null")) {
			String dataPesquisa = Funcoes.dateToStringSoData(new Date());
			dataPesquisa = Funcoes.somaData(dataPesquisa, Funcoes.StringToInt(opcaoPeriodo) * -1);
			if(Funcoes.StringToInt(opcaoPeriodo) < 20) {
				sqlTemp00 += " AND DATA_INICIO >= ?";
			} else {
				sqlTemp00 += " AND DATA_INICIO <= ?";
			}
			ps.adicionarDate(dataPesquisa);
		}
		
		sqlGroupBy = " GROUP BY p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ID_SERV, p.PEND_TIPO, p.DATA_INICIO, p.PEND_TIPO_CODIGO, p.ASSISTENTE ";

		sqlLista = "SELECT p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ID_SERV, p.PEND_TIPO, p.DATA_INICIO, p.PEND_TIPO_CODIGO, p.ASSISTENTE " + sqlTemp00;
		sqlLista += sqlGroupBy;
		sqlLista += " ORDER BY p.DATA_INICIO,p.PROC_NUMERO";
		
		try{
			rs1 = consultarPaginacao(sqlLista, ps, posicaoPaginaAtual);
			while (rs1.next()) {
				ProcessoParalisadoDt obTemp = new ProcessoParalisadoDt();
				obTemp.setIdProcesso(rs1.getString("ID_PROC"));
				obTemp.setNumeroProcesso(rs1.getString("PROC_NUMERO") + "-" + rs1.getString("DIGITO_VERIFICADOR"));
				obTemp.setTipoPendencia(rs1.getString("PEND_TIPO"));
				obTemp.setAssistente((rs1.getString("ASSISTENTE") != null ? rs1.getString("ASSISTENTE") : ""));
				String dataInicio = Funcoes.FormatarData(rs1.getDateTime("DATA_INICIO"));
				obTemp.setDataRecebimento(dataInicio);
				long[] diferencaDatas = Funcoes.diferencaDatas(new Date(), Funcoes.StringToDate(dataInicio));
				obTemp.setQuantidadeDiasParalisados(String.valueOf(diferencaDatas[0]) + " dias");
				listaProcessos.add(obTemp);
			}
			sqlQuantidade = "SELECT COUNT(1) AS QUANTIDADE FROM (SELECT p.ID_PROC " + sqlTemp00 + sqlGroupBy + " )";
			rs2 = consultar(sqlQuantidade, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
			try{
				if (rs2 != null) rs2.close();
			} catch(Exception e) {
			}
		}
		return listaProcessos;
	}	
	
	public String consultarServentiaProcesso(String id_processo) throws Exception {
		String Sql;
		String idServentia = "";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC WHERE ID_PROC = ? AND (ID_SERV_RECURSO IS NULL OR ID_SERV_RECURSO = ID_SERV)"; 
		ps.adicionarLong(id_processo);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				idServentia = rs1.getString("ID_SERV");
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return idServentia;
	}
	
	public boolean alterarProcessoTipo(String idProcesso, String idProcessoTipo) throws Exception {

		String sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.PROC SET ";

		if (idProcessoTipo != null && idProcessoTipo.length() > 0) {
			sql += "ID_PROC_TIPO  = ? ";
			ps.adicionarString(idProcessoTipo);
		}
		
		sql += " WHERE ID_PROC = ? ";
		ps.adicionarString(idProcesso);
		
		return executarUpdateDelete(sql, ps) > 0;
		
	}

	public boolean consultarIsMisto(String id_proc) throws Exception {
		String Sql;		
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT 1 FROM PROJUDI.PROC WHERE ID_PROC = ? AND PROCESSO_FISICO_TIPO in(?,?)"; 
		ps.adicionarLong(id_proc);
		ps.adicionarString(ProcessoDt.MISTO_SPG);
		ps.adicionarString(ProcessoDt.MISTO_SSG);

		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				return true;
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return false;
	}
	
	public List consultarTodosProcessosClassificadosServentia(String id_Classificador, String id_Serventia, String id_juiz) throws Exception {
		ResultSetTJGO rs1 = null;		
		List listaProcessos = new ArrayList<>();
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String sql;		
		
		if(id_Classificador == null){
			throw new MensagemException("Escolha um Classificador");
		}
				
		sql = "SELECT DISTINCT p.ID_PROC  " ;	
		sql += " FROM PROJUDI.PROC p  " ;
		sql += " LEFT JOIN PROJUDI.PROC_STATUS ps ON ps.ID_PROC_STATUS = p.ID_PROC_STATUS " ;				
		sql += " LEFT JOIN PROJUDI.PROC_PRIOR pp ON pp.ID_PROC_PRIOR = p.ID_PROC_PRIOR " ;
		sql += " LEFT JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC " ;
		sql += " WHERE p.ID_SERV = ?"; 	ps.adicionarLong(id_Serventia);

		sql += "  AND p.ID_PROC NOT IN (SELECT r.ID_PROC FROM PROJUDI.RECURSO r WHERE r.ID_PROC = p.ID_PROC AND r.DATA_RETORNO IS NULL)";
				
		if(id_Classificador != null && !id_Classificador.equalsIgnoreCase("")) {
			sql += " AND p.ID_CLASSIFICADOR = ?"; 	ps.adicionarLong(id_Classificador);
		}
		if(id_juiz != null && !id_juiz.equalsIgnoreCase("")) {
			sql += " AND pr.ID_SERV_CARGO = ?"; 		ps.adicionarLong(id_juiz);
			sql += " AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(ProcessoStatusDt.ATIVO);
		}
		sql += " AND ps.PROC_STATUS_CODIGO = ? "; ps.adicionarLong(ProcessoStatusDt.ATIVO);
		
		sql += " UNION ";
		
		sql += " SELECT DISTINCT p.ID_PROC  " ;		
		sql += " FROM PROJUDI.PROC p  " ;
		sql += " INNER JOIN PROJUDI.RECURSO r ON r.ID_PROC = p.ID_PROC AND r.DATA_RETORNO IS NULL" ;
		sql += " LEFT JOIN PROJUDI.PROC_STATUS ps ON ps.ID_PROC_STATUS = p.ID_PROC_STATUS " ;		
		sql += " LEFT JOIN PROJUDI.PROC_PRIOR pp ON pp.ID_PROC_PRIOR = p.ID_PROC_PRIOR " ;
		sql += " LEFT JOIN PROJUDI.PROC_RESP pr ON pr.ID_PROC = p.ID_PROC " ;
		sql += " WHERE p.ID_SERV = ?"; 	ps.adicionarLong(id_Serventia);		
		
		if(id_Classificador != null && !id_Classificador.equalsIgnoreCase("")) {
			sql += " AND p.ID_CLASSIFICADOR = ?"; 	ps.adicionarLong(id_Classificador);
		}
		if(id_juiz != null && !id_juiz.equalsIgnoreCase("")) {
			sql += " AND pr.ID_SERV_CARGO = ?"; 		ps.adicionarLong(id_juiz);
			sql += " AND pr.CODIGO_TEMP = ? "; ps.adicionarLong(ProcessoStatusDt.ATIVO);
		}
		sql += " AND ps.PROC_STATUS_CODIGO = ?"; ps.adicionarLong(ProcessoStatusDt.ATIVO);
							
		sql = "Select * from (" + sql + ")";
		
		try{
			rs1 = this.consultar(sql , ps); 
			
			while (rs1.next()){
				listaProcessos.add(rs1.getString("ID_PROC"));
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}			
		}
		return listaProcessos;
		
	}
	
	public List consultarProcessosTCO(String tcoNumero) throws Exception{
		String sql = "";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		
		
		sql  = " SELECT distinct ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, ANO, FORUM_CODIGO, PROC_STATUS_CODIGO, SEGREDO_JUSTICA, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV, ID_PROC_TIPO ";
		sql += " FROM PROJUDI.VIEW_BUSCA_PROC p ";			
		sql += " WHERE p.PROC_STATUS_CODIGO = ?"; 					ps.adicionarLong(ProcessoStatusDt.ATIVO);
		
		if (tcoNumero != null && tcoNumero.length() > 0){					
			sql +=  " AND TCO_NUMERO  = ?";  						ps.adicionarString(tcoNumero);
		}
		
		sql += " ORDER BY p.PROC_PRIOR_ORDEM, p.DATA_RECEBIMENTO";
		
		try{
			rs = consultar(sql, ps);

			ProcessoDt processoDt = null;
			while (rs.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs.getString("ANO"));
				processoDt.setForumCodigo(rs.getString("FORUM_CODIGO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs.getString("PROC_PRIOR_CODIGO"));
				processoDt.setId_ProcessoTipo(rs.getString("ID_PROC_TIPO"));

				listaProcessos.add(processoDt);
			}
			
		} finally{
			try{if (rs != null) rs.close();} catch(Exception e) {}
		}
		return listaProcessos;
	}
	
	/**
	 * Altera o Status de um processo, em virtude da ativação de um processo de cálculo
	 * 
	 * @param id_Processo, identificação do processo que terá o status alterado
	 * @param novoStatus, novo status do processo
	 * @param novoProcessoTipo, novo processoTipo do processo
	 * 
	 * @author mmgomes
	 */
	public void alterarStatusProcessoTipoProcesso(String id_Processo, int novoStatus, int novoProcessoTipo) throws Exception {
		String Sql;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "UPDATE PROJUDI.PROC set ID_PROC_STATUS = ";
		Sql += " (SELECT ID_PROC_STATUS FROM PROJUDI.PROC_STATUS WHERE PROC_STATUS_CODIGO = ?)";
		ps.adicionarLong(novoStatus);
		Sql += " ,ID_PROC_TIPO = ";
		Sql += " (SELECT ID_PROC_TIPO FROM PROJUDI.PROC_TIPO WHERE PROC_TIPO_CODIGO = ?)";
		ps.adicionarLong(novoProcessoTipo);
		Sql += " WHERE ID_PROC = ?";
		ps.adicionarLong(id_Processo);

		executarUpdateDelete(Sql, ps);	
	}
	
	public String consultarProcessosDistribuidos(String data, String codigoComarca, String posicao) throws Exception {
		String stTemp = "";
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		
		Date dataDistribuicao = Funcoes.StringToDate(data);
		GregorianCalendar dataInicio = new GregorianCalendar();
		GregorianCalendar dataFim = new GregorianCalendar();
		dataInicio.setTime(dataDistribuicao);
		dataFim.setTime(dataDistribuicao);
		
		String sql = "SELECT * FROM PROJUDI.VIEW_DADOS_COMP_DIST";
		sql += " WHERE DATA_DISTRIBUICAO BETWEEN ? AND ? ";
		ps.adicionarDateTimePrimeiraHoraDia(data);
		ps.adicionarDateTimeUltimaHoraDia(data);
		sql += "AND COD_COMARCA = ?";
		ps.adicionarLong(codigoComarca);

		try{
			rs1 = this.consultarPaginacao(sql, ps, posicao);
			while (rs1.next()) {
				stTemp += rs1.getString("NUMR_PROC")+";";
				stTemp += rs1.getString("DIGITO_VERIFICADOR")+";";
				stTemp += rs1.getString("ANO")+";";
				stTemp += rs1.getString("FIXO")+";";
				stTemp += rs1.getString("UNIDADE_ORIGEM")+";";
				stTemp += rs1.getString("SERV")+";";
				stTemp += rs1.getString("NATUREZA")+";";
				stTemp += rs1.getString("DATA_DISTRIBUICAO")+";";
				stTemp += rs1.getString("TIPO_DISTRIBUICAO")+";";
				stTemp += rs1.getString("POSICAO_JUIZ")+";";
				stTemp += rs1.getString("VALOR_ACAO")+";";
				stTemp += rs1.getString("VALOR_CUSTAS")+";";
				stTemp += rs1.getString("DATA_BAIXA_PROC")+";";
				stTemp += rs1.getString("OAB")+";";
				stTemp += rs1.getString("NOME_ADVOGADO")+";";
				stTemp += rs1.getString("TIPO_PARTE")+";";
				stTemp += rs1.getString("TIPO_PESSOA")+";";
				stTemp += rs1.getString("NOME")+";";
				stTemp += rs1.getString("CPF_CGC")+";";
				stTemp += rs1.getString("SEXO")+";";
				stTemp += rs1.getString("IDENTIDADE")+";";
				stTemp += rs1.getString("ORGAO_EXPEDIDOR")+";";
				stTemp += rs1.getString("DATA_BAIXA_PARTE")+";";
				stTemp += rs1.getString("COD_COMARCA")+";";
				stTemp += rs1.getString("NOME_SIMPLIFICADO")+";";				
				stTemp += "\n";
			}
			sql = "SELECT COUNT(*) AS QUANTIDADE FROM PROJUDI.VIEW_DADOS_COMP_DIST WHERE DATA_DISTRIBUICAO BETWEEN ? AND ? AND COD_COMARCA = ?";
			rs2 = consultar(sql, ps);
			if (rs2.next()) {
			    stTemp += Funcoes.completarZeros(rs2.getString("QUANTIDADE"), 5);
			}
		}
		finally{
			try{if( rs1 != null ) rs1.close(); }catch(Exception e){}
		}

		return stTemp;
	}
	
	public void atualizarProcessoMistoParaDigitaPendenteLimpezaDataSPG(String id_processo) throws Exception {
		String sql;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.PROC SET PROCESSO_FISICO_TIPO = ? WHERE ID_PROC = ? AND PROCESSO_FISICO_TIPO in(?,?)"; 
		ps.adicionarString(ProcessoDt.MISTO_SPG_PENDENTE_LIMPEZA_SPG);
		ps.adicionarLong(id_processo);
		ps.adicionarString(ProcessoDt.MISTO_SPG);
		ps.adicionarString(ProcessoDt.MISTO_SSG);

		super.executarUpdateDelete(sql, ps);	
	}
	
	public void atualizarProcessoDigitalParaMisto(String id_processo) throws Exception {
		String sql;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.PROC SET PROCESSO_FISICO_TIPO = ? WHERE ID_PROC = ? AND PROCESSO_FISICO_TIPO in(?,?)"; 
		ps.adicionarString(ProcessoDt.MISTO_SPG);
		ps.adicionarLong(id_processo);
		ps.adicionarString(ProcessoDt.MISTO_SPG_PENDENTE_LIMPEZA_SPG);
		ps.adicionarString(ProcessoDt.MISTO_SPG_CONCLUIDA_LIMPEZA_SPG);

		super.executarUpdateDelete(sql, ps);	
	}
	
	/* @author jrcorrea*/
	public List consultarListaAtivosJuizUPJ(String id_ServentiaCargoMagistrado) throws Exception {
		List liTemp = new ArrayList();
		String stSql;
		//String strServentiaCodigo="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
			
			
			stSql = 	"  select s.serv, s.id_serv, tab.Quantidade  from (";
			stSql += 	"	        Select Count(distinct P.Id_Proc) As Quantidade, p.id_serv";
			stSql += 	"	            From Projudi.Proc P ";
			stSql += 	"                   INNER JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS AND ps.PROC_STATUS_CODIGO = ? "; 												ps.adicionarLong(ProcessoStatusDt.ATIVO);			
			stSql += 	"	                Inner Join Projudi.Proc_Resp Pr On (P.Id_Proc = Pr.Id_Proc And Pr.Id_Serv_Cargo = ? And Pr.Codigo_Temp = ? AND pr.redator = 1)"; 						ps.adicionarLong(id_ServentiaCargoMagistrado); ps.adicionarLong(ProcessoResponsavelDt.ATIVO); 
			stSql += 	"	                Inner Join Projudi.Serv_Cargo Sc On Sc.Id_Serv_Cargo = Pr.Id_Serv_Cargo  ";
			stSql += 	"	                Join Projudi.Cargo_Tipo Ct On ( Pr.Id_Cargo_Tipo = Ct.Id_Cargo_Tipo   And (Ct.Cargo_Tipo_Codigo = ? OR Ct.Cargo_Tipo_Codigo = ?) ) ";					ps.adicionarLong(CargoTipoDt.JUIZ_UPJ); ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
			stSql += 	"	                Where P.Data_Arquivamento Is Null and exists (select 1 from serv_relacionada sr where sr.ID_SERV_PRINC = p.id_Serv and sr.ID_SERV_REL = sc.id_serv )";
			stSql += 	"	                Group By P.Id_Serv ";
			stSql += 	"	      ) tab inner join serv s on s.id_Serv = tab.id_serv ";

			
			rs1 = consultar(stSql, ps);
			while (rs1.next()) {
				liTemp.add(new String[] {"0", rs1.getString("serv"), rs1.getString("QUANTIDADE"), rs1.getString("id_serv")  });
			}
	//rs1.close();
    	
    	} finally{
    		try{
    			if (rs1 != null) rs1.close();
    		} catch(Exception e) {
    		}
    	}
		return liTemp;
	}
	/* @author jrcorrea*/
	public int consultarQuantidadeAtivosJuizUPJ(String id_ServentiaCargoMagistrado, String id_serventia) throws Exception {
		int inQtd = 0;
		String stSql;
		//String strServentiaCodigo="";
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps = new PreparedStatementTJGO();
		try{
			
			stSql = "SELECT COUNT(*) as QUANTIDADE ";
			stSql += 	" From Projudi.Proc P ";
			stSql += 	"	INNER JOIN PROJUDI.PROC_STATUS ps on p.ID_PROC_STATUS = ps.ID_PROC_STATUS AND ps.PROC_STATUS_CODIGO = ? "; 												ps.adicionarLong(ProcessoStatusDt.ATIVO);			
			stSql += 	"	Inner Join Projudi.Proc_Resp Pr On (P.Id_Proc = Pr.Id_Proc And Pr.Id_Serv_Cargo = ? And Pr.Codigo_Temp = ? AND pr.redator = 1)"; 						ps.adicionarLong(id_ServentiaCargoMagistrado); ps.adicionarLong(ProcessoResponsavelDt.ATIVO); 
			stSql += 	"	Inner Join Projudi.Serv_Cargo Sc On Sc.Id_Serv_Cargo = Pr.Id_Serv_Cargo  ";
			stSql += 	"	Join Projudi.Cargo_Tipo Ct On ( Pr.Id_Cargo_Tipo = Ct.Id_Cargo_Tipo   And (Ct.Cargo_Tipo_Codigo = ? OR Ct.Cargo_Tipo_Codigo = ?)) ";					ps.adicionarLong(CargoTipoDt.JUIZ_UPJ); ps.adicionarLong(CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU);
			stSql += 	"	Where P.Data_Arquivamento Is Null and exists (select 1 from serv_relacionada sr where sr.ID_SERV_PRINC = p.id_Serv and sr.ID_SERV_REL = sc.id_serv )";
			stSql += 	"		AND Sc.id_serv = ?  ";																																	ps.adicionarLong(id_serventia);
			
			
			rs1 = consultar(stSql, ps);
			if(rs1.next()) {
				inQtd =  rs1.getInt("QUANTIDADE");
			}
	//rs1.close();
    	
    	} finally{
    		try{
    			if (rs1 != null) rs1.close();
    		} catch(Exception e) {
    		}
    	}
		return inQtd;
	}
	
	public void atualizarProcessoMistoParaDigitaConcluidaLimpezaDataSPG(String id_processo) throws Exception {
		String sql;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "UPDATE PROJUDI.PROC SET PROCESSO_FISICO_TIPO = ? WHERE ID_PROC = ? AND PROCESSO_FISICO_TIPO in(?,?)"; 
		ps.adicionarString(ProcessoDt.MISTO_SPG_CONCLUIDA_LIMPEZA_SPG);
		ps.adicionarLong(id_processo);
		ps.adicionarString(ProcessoDt.MISTO_SPG);
		ps.adicionarString(ProcessoDt.MISTO_SSG);

		super.executarUpdateDelete(sql, ps);	
	}
	
	public List<ProcessoDt> consultarMistoSPGPendenteDeLimpezaSPG(int quantidade) throws Exception {
		String Sql;
		List<ProcessoDt> Dados = new ArrayList<ProcessoDt>();
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		Sql = "SELECT * FROM PROJUDI.VIEW_PROC P1 WHERE EXISTS (SELECT 1 FROM PROJUDI.PROC P2 WHERE P2.ID_PROC = P1.ID_PROC AND PROCESSO_FISICO_TIPO = ?) AND ROWNUM <= ?";
		ps.adicionarString(ProcessoDt.MISTO_SPG_PENDENTE_LIMPEZA_SPG);
		ps.adicionarLong(quantidade);

		try{
			rs1 = consultar(Sql, ps);
			while (rs1.next()) {
				ProcessoDt processoDt = new ProcessoDt();
				associarDt(processoDt, rs1);		
				processoDt.setId_Recurso(rs1.getString("ID_RECURSO"));
				processoDt.setServentiaTipoCodigo(rs1.getString("SERV_TIPO_CODIGO"));
				Dados.add(processoDt);
			}
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}
		return Dados;
	}

	public List consultarProcessosServentiaRelacionadaConciliador(String nomeParte, String pesquisarNomeExato, long cpfCnpjParte, int processoNumero, int digito, int ano, String statusCodigo, String id_ProcessoTipo, String id_gabinete, String id_Comarca, String SegredoJustica, String id_Classificador, String posicao) throws Exception {
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";		
		String sqlCondicoes = "";
		
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		
		String nomeParteSimplificado = Funcoes.converteNomeSimplificado(nomeParte);
		
		if (	(statusCodigo==null || statusCodigo.length()==0)){
			throw new MensagemException("É necessário informar o Status do Processo.");
		}		
		if(		(nomeParteSimplificado==null || nomeParteSimplificado.length()==0) && 
				(processoNumero==-1) &&								
				(cpfCnpjParte==-1)  &&
				(id_gabinete==null || id_gabinete.length()==0) 
			){
			
			throw new MensagemException("Ao menos um dos parâmetros seguintes deve ser informado: número do processo, o nome da parte, cpf/cnpj. Se foi informado o nome da parte, verifique-o, pois o mesmo não pode ser consultado. ");
			
		}
		
		boolean boUsaParte = false;
		if (processoNumero==-1 && (cpfCnpjParte !=-1  || nomeParteSimplificado.length() > 0 )) {
			boUsaParte=true;
		}
		
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, SERV, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";				
		
		if (boUsaParte) {
			stSqlCamposProcesso += ", p.NOME, p.proc_parte_tipo, p.id_proc_parte  ";			
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_PARTE p ";
			stSqlComumProcesso += " INNER JOIN PROJUDI.PROC_RESP pr on (P.ID_PROC = pr.ID_PROC AND pr.CODIGO_TEMP = ?)";							ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
			stSqlComumProcesso += " INNER JOIN PROJUDI.SERV_CARGO SC ON (SC.ID_SERV_CARGO = PR.ID_SERV_CARGO AND SC.ID_SERV = ?) ";					ps.adicionarLong(id_gabinete);
			 if (cpfCnpjParte !=-1) {			
					sqlCondicoes += " ( p.CPF = ?";									ps.adicionarLong(cpfCnpjParte);
					sqlCondicoes += " OR p.CNPJ = ? )";								ps.adicionarLong(cpfCnpjParte);
					
				}else if (nomeParteSimplificado.length() > 0) {
					if (pesquisarNomeExato.length() > 0 && pesquisarNomeExato.equals("true")) {
						sqlCondicoes += " p.NOME_SIMPLIFICADO = '" + nomeParteSimplificado + "'";
					} else {
						sqlCondicoes += " p.NOME_SIMPLIFICADO like '" + nomeParteSimplificado +"%'";   
					}					
				}
				
		}else {
			stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_TODOS p ";
			stSqlComumProcesso += " INNER JOIN PROJUDI.PROC_RESP pr on (P.ID_PROC = pr.ID_PROC AND pr.CODIGO_TEMP = ?)";							ps.adicionarLong(ProcessoResponsavelDt.ATIVO);
			stSqlComumProcesso += " INNER JOIN PROJUDI.SERV_CARGO SC ON (SC.ID_SERV_CARGO = PR.ID_SERV_CARGO AND SC.ID_SERV = ?) ";					ps.adicionarLong(id_gabinete);
			if(processoNumero != -1){		
				sqlCondicoes +=  " p.PROC_NUMERO  = ?";  						ps.adicionarLong(processoNumero);
			 		
				if (digito != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.DIGITO_VERIFICADOR  = ?";				ps.adicionarLong(digito);
				}
				
				if (ano != -1){
					sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "");
					sqlCondicoes += " p.ANO  = ?";								ps.adicionarLong(ano);
				}				
			}
		}
				
		if (statusCodigo != null && statusCodigo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "")  ;
			sqlCondicoes += " P.PROC_STATUS_CODIGO = ?";  			ps.adicionarLong(statusCodigo);
		}
		
		if (id_ProcessoTipo != null && id_ProcessoTipo.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "")  ;			
			sqlCondicoes += " P.ID_PROC_TIPO = ?";		 			ps.adicionarLong(id_ProcessoTipo);
		}
		
		if (id_Comarca != null && id_Comarca.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes +=  " P.ID_COMARCA = ?";					ps.adicionarLong(id_Comarca);
		}
		if (SegredoJustica != null && SegredoJustica.equals("false")){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes +=  " P.SEGREDO_JUSTICA = ?";				ps.adicionarBoolean(SegredoJustica);
		}
		
		if (id_Classificador != null && id_Classificador.length() > 0){
			sqlCondicoes += (sqlCondicoes.length() > 0 ? " AND " : "") ;
			sqlCondicoes += " P.ID_CLASSIFICADOR = ?";		 		ps.adicionarLong(id_Classificador);
		}
		
		if (sqlCondicoes.length() > 0) stSqlComumProcesso += " WHERE " + sqlCondicoes;
		
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sql, ps, posicao);

			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				
				if (boUsaParte) {
					processoDt.setNomeParteBusca(rs1.getString("NOME"));
					processoDt.setParteTipoBusca(rs1.getString("proc_parte_tipo"));
					processoDt.setId_ParteTipoBusca(rs1.getString("id_proc_parte"));
				}

				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso;

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
			//rs1.close();
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}

	public boolean isGabineteUpjResposavelProcesso(String id_Serventia, String id_proc)throws Exception {		

		boolean retorno = false;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
						
		Sql = "SELECT p.id_proc FROM PROC p ";
		Sql += " INNER JOIN PROJUDI.PROC_RESP PR ON (P.ID_PROC = PR.ID_PROC AND PR.CODIGO_TEMP = ?) ";							ps.adicionarLong(0); 
		Sql += " INNER JOIN PROJUDI.SERV_CARGO SC ON (SC.ID_SERV_CARGO = PR.ID_SERV_CARGO AND SC.ID_SERV = ?)"; 				ps.adicionarLong(id_Serventia);
		Sql += " where p.id_proc = ?";																							ps.adicionarLong(id_proc);
		
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				retorno = true;
			}
		
		}
		finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e) {}
		}
		
		return retorno;
	}
	
	public String getGabineteUpjResposavelProcesso(String id_proc)throws Exception {		

		String retorno = null ;
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
						
		Sql = "SELECT sc.id_serv FROM PROC p ";
		Sql += " INNER JOIN PROJUDI.PROC_RESP PR ON (P.ID_PROC = PR.ID_PROC AND PR.CODIGO_TEMP = ?) ";										ps.adicionarLong(0); 
		Sql += " INNER JOIN PROJUDI.SERV_CARGO SC ON (SC.ID_SERV_CARGO = PR.ID_SERV_CARGO )";
		Sql += " INNER JOIN PROJUDI.SERV S ON (S.ID_SERV = SC.ID_SERV )";
		Sql += " INNER JOIN PROJUDI.SERV_SUBTIPO SST ON (SST.ID_SERV_SUBTIPO = S.ID_SERV_SUBTIPO and SST.SERV_SUBTIPO_CODIGO = ? )";		ps.adicionarLong(ServentiaSubtipoDt.GABINETE_FLUXO_UPJ);
		Sql += " where p.id_proc = ?";																										ps.adicionarLong(id_proc);
		
		
		try{
			rs1 = consultar(Sql, ps);
			if (rs1.next()) {
				retorno = rs1.getString("id_serv");
			}
		
		}
		finally{
			try{if (rs1 != null)rs1.close();} catch(Exception e) {}
		}
		
		return retorno;
	}
	
	
	/**
	 * ATENÇÃO: ESTE MÉTODO É EXCLUSIVO PARA O RETORNO AUTOMÁTICO DE PROCESSOS ENCAMINHADOS AO SÉTIMO CEJUSC.
	 * SERÁ UTILIZADO EXCLUSIVAMENTE PELO SERVIÇO DE EXECUÇÃO AUTOMÁTICA DESENVOLVIDO PARA ESTE FIM. NÃO
	 * UTILIZAR EM OUTRAS SITUAÇÕES. 
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ProcessoDt> listarProcessosRetornoAutomaticoCEJUSC() throws Exception {
		String sql;
		ResultSetTJGO rs = null;
		List<ProcessoDt> listaProcessos = new ArrayList<ProcessoDt>();
		
		sql = "SELECT ID_PROC, PROC_NUMERO, DIGITO_VERIFICADOR, FORUM_CODIGO, ANO, ID_SERV, SEGREDO_JUSTICA FROM VIEW_RETORNO_AUTOMATICO_CEJUSC";
		
		
		try {
			rs = consultarSemParametros(sql);
			while (rs.next()) {
				ProcessoDt processoDt = new ProcessoDt();
				
				processoDt.setId_Processo(rs.getString("ID_PROC"));
				processoDt.setProcessoNumero( rs.getString("PROC_NUMERO"));
				processoDt.setDigitoVerificador(rs.getString("DIGITO_VERIFICADOR"));
				processoDt.setForumCodigo(rs.getString("FORUM_CODIGO"));
				processoDt.setAno(rs.getString("ANO"));
				processoDt.setId_Serventia(rs.getString("ID_SERV"));
				processoDt.setId_Serventia(rs.getString("SEGREDO_JUSTICA"));
				listaProcessos.add(processoDt);
			}
		}
		finally{
			try{if(rs != null)rs.close();} catch(Exception e) {}
		}
		
		return listaProcessos;
	}
	
	/**
	 * ATENÇÃO: ESTE MÉTODO É EXCLUSIVO PARA O ARQUIVAMENTO AUTOMÁTICO DOS PROCESSOS DE EXECUÇÃO PENAL.
	 * SERÁ UTILIZADO EXCLUSIVAMENTE PELO SERVIÇO DE EXECUÇÃO AUTOMÁTICA DESENVOLVIDO PARA ESTE FIM. NÃO
	 * UTILIZAR EM OUTRAS SITUAÇÕES. 
	 * 
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> listarIdProcArquivamentoAutomaticoExecucaoPenal() throws Exception {
		String sql;
		ResultSetTJGO rs = null;
		List<String> listaIdProcessos = new ArrayList<String>();
		
		sql = "SELECT ID_PROC FROM VIEW_ARQ_AUTO_EXEC_PENAL";
		
		
		try {
			rs = consultarSemParametros(sql);
			while (rs.next()) {
				listaIdProcessos.add(rs.getString("ID_PROC"));
			}
		}
		finally{
			try{if(rs != null)rs.close();} catch(Exception e) {}
		}
		
		return listaIdProcessos;
	}

	public List consultarProcessosClassificar(String cpfPoloAtivo, boolean boPoloAtivoNull,  String cpfPoloPassivel, boolean boPoloaPassivoNull, String id_proc_tipo, String id_assunto, String id_classificador, String maxValor, String minValor, String id_serv, String posicao) throws Exception {
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";		
		String sqlCondicoes = "";
		
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		if (id_serv==null || id_serv.length()==0) {
			throw new MensagemException("Ocorreu algum problema na consulta favor tentar mais tarde. Serventia não indentificada.");
		}else if(	(cpfPoloAtivo==null || cpfPoloAtivo.length()==0) 
				&& (cpfPoloPassivel==null || cpfPoloPassivel.length()==0) 
				&& (id_proc_tipo==null || id_proc_tipo.length()==0) 
				&& (id_assunto==null || id_assunto.length()==0) 
				&& (id_classificador==null || id_classificador.length()==0)
				&& (maxValor==null || maxValor.length()==0)
				&& (minValor==null || minValor.length()==0)){			
			throw new MensagemException("Ao menos um parâmentro deve ser informado na consulta (Cpf, Processo Tipo, Assunto, Classificador, Valor Mínimo ou Valor Máximo).");
			
		}
		
		stSqlCamposProcesso = "SELECT distinct p.ID_PROC, p.PROC_NUMERO, p.DIGITO_VERIFICADOR, p.ANO, p.FORUM_CODIGO, p.PROC_STATUS_CODIGO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, SERV, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, ID_PROC_TIPO, PROC_TIPO ";
		
		stSqlComumProcesso += " FROM PROJUDI.VIEW_BUSCA_PROC_TODOS p ";		
		
		sqlCondicoes += " WHERE id_serv = ? "; 																			ps.adicionarLong(id_serv);
		sqlCondicoes +="  AND p.id_proc_status = ?";																	ps.adicionarLong(ProcessoStatusDt.ID_ATIVO);
				
		if (id_proc_tipo!=null && !id_proc_tipo.isEmpty()) {		
			sqlCondicoes+="  AND P.ID_PROC_TIPO = ?";																	ps.adicionarLong(id_proc_tipo);
		}
		if (id_classificador!=null && !id_classificador.isEmpty()) {
			sqlCondicoes+="  AND P.ID_CLASSIFICADOR = ?";																	ps.adicionarLong(id_classificador);
		}
		if (id_assunto!=null && !id_assunto.isEmpty()) {
			sqlCondicoes+="  AND EXISTS (SELECT 1 FROM PROC_ASSUNTO PA WHERE PA.ID_PROC = P.ID_PROC AND PA.ID_ASSUNTO = ?)";																		ps.adicionarLong(id_assunto);
		}
		if (minValor!=null && !minValor.isEmpty()) { 
			sqlCondicoes+="  AND P.VALOR>=? ";																			ps.adicionarDecimal(minValor);
		}
		if (maxValor!=null && !maxValor.isEmpty()) { 
			sqlCondicoes+="  AND P.VALOR<=?";																			ps.adicionarDecimal(maxValor);
		}		
		if (cpfPoloAtivo!=null && !cpfPoloAtivo.isEmpty()) {													//polo ativo
			sqlCondicoes+=" and exists ( ";
			sqlCondicoes+="  SELECT 1 FROM PROC_PARTE PP1 WHERE P.ID_PROC=PP1.ID_PROC AND PP1.ID_PROC_PARTE_TIPO = ?";	ps.adicionarLong(ProcessoParteDt.ID_POLO_ATIVO);
			if (boPoloAtivoNull) {
				sqlCondicoes+="  AND PP1.CPF IS NULL AND PP1.CNPJ IS NULL ";
			}else {
				sqlCondicoes+="  AND (PP1.CPF = ? OR PP1.CNPJ = ?)) "; 														ps.adicionarLong(cpfPoloAtivo);			ps.adicionarLong(cpfPoloAtivo);
			}
		}
		if (cpfPoloPassivel!=null && !cpfPoloPassivel.isEmpty()) {												//polo passivo
			sqlCondicoes+=" and exists ( ";
			sqlCondicoes+="  SELECT 1 FROM PROC_PARTE PP2 WHERE P.ID_PROC=PP2.ID_PROC AND PP2.ID_PROC_PARTE_TIPO = ?";	ps.adicionarLong(ProcessoParteDt.ID_POLO_PASSIVO);
			if (boPoloaPassivoNull) {
				sqlCondicoes+="  AND PP2.CPF IS NULL AND PP2.CNPJ IS NULL ";
			}else {
				sqlCondicoes+="  AND (PP2.CPF = ? OR PP2.CNPJ = ?)) ";														ps.adicionarLong(cpfPoloPassivel);		ps.adicionarLong(cpfPoloPassivel);
			}
		}
		
		try{
			
			sql = stSqlCamposProcesso + stSqlComumProcesso + sqlCondicoes;			
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultarPaginacao(sql, ps, posicao);
			//listaProcessos = getListaProcessos(rs1, false);
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));
				processoDt.setForumCodigo(rs1.getString("FORUM_CODIGO"));
				processoDt.setId_ProcessoTipo(rs1.getString("ID_PROC_TIPO"));
				processoDt.setProcessoTipo(rs1.getString("PROC_TIPO"));

				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(distinct p.ID_PROC) AS QUANTIDADE ";
			sql += stSqlComumProcesso + sqlCondicoes;		

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}

	public int classificarProcessos(String cpfPoloAtivo, boolean boPoloAtivoNull,  String cpfPoloPassivel, boolean boPoloaPassivoNull, String id_proc_tipo, String id_assunto, String id_classificador, String maxValor, String minValor, String id_classificarAlteracao, String id_serv) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		int qtdRetorno =0; 
		
		if (id_serv==null || id_serv.length()==0) {
			throw new MensagemException("Ocorreu algum problema na consulta favor tentar mais tarde. Serventia não indentificada.");
		}else if(	(cpfPoloAtivo==null || cpfPoloAtivo.length()==0) 
				&& (cpfPoloPassivel==null || cpfPoloPassivel.length()==0) 
				&& (id_proc_tipo==null || id_proc_tipo.length()==0) 
				&& (id_assunto==null || id_assunto.length()==0) 
				&& (id_classificador==null || id_classificador.length()==0)
				&& (maxValor==null || maxValor.length()==0)
				&& (minValor==null || minValor.length()==0)){			
			throw new MensagemException("Ao menos um parâmentro deve ser informado na consulta (Cpf, Processo Tipo, Assunto, Classificador, Valor Mínimo ou Valor Máximo).");
		}
		
		String sql ="UPDATE PROC p ";
		if (id_classificarAlteracao!=null && !id_classificarAlteracao.isEmpty() ) {
			sql+="  SET ID_CLASSIFICADOR = ? "; 																ps.adicionarLong(id_classificarAlteracao);
		}else {
			sql+="  SET ID_CLASSIFICADOR = ? "; 																ps.adicionarLongNull();
		}
		
		sql+=" WHERE p.id_proc_status = ?";																		ps.adicionarLong(ProcessoStatusDt.ID_ATIVO);
		sql += " and id_serv = ? "; 																			ps.adicionarLong(id_serv);		
		
		if (id_proc_tipo!=null && !id_proc_tipo.isEmpty()) {		
			sql+="  AND P.ID_PROC_TIPO = ?";																	ps.adicionarLong(id_proc_tipo);
		}
		if (id_classificador!=null && !id_classificador.isEmpty()) {
			sql+="  AND P.ID_CLASSIFICADOR = ?";																ps.adicionarLong(id_classificador);
		}
		if (id_assunto!=null && !id_assunto.isEmpty()) {
			sql+="  AND P.ID_ASSUNTO = ?";																		ps.adicionarLong(id_assunto);
		}
		if (maxValor!=null && !maxValor.isEmpty()) { 
			sql+="  AND P.VALOR>=? ";																			ps.adicionarDecimal(maxValor);
		}
		if (minValor!=null && !minValor.isEmpty()) { 
			sql+="  AND P.VALOR<=?";																			ps.adicionarDecimal(minValor);
		}		
		if (cpfPoloAtivo!=null && !cpfPoloAtivo.isEmpty()) {													//polo ativo
			sql+=" and exists ( ";
			sql+="  SELECT 1 FROM PROC_PARTE PP1 WHERE P.ID_PROC=PP1.ID_PROC AND PP1.ID_PROC_PARTE_TIPO = ?";	ps.adicionarLong(ProcessoParteDt.ID_POLO_ATIVO);
			if (boPoloAtivoNull) {
				sql+="  AND PP1.CPF IS NULL AND PP1.CNPJ IS NULL ";
			}else {
				sql+="  AND (PP1.CPF = ? OR PP1.CNPJ = ?)) "; 														ps.adicionarLong(cpfPoloAtivo);			ps.adicionarLong(cpfPoloAtivo);
			}
		}
		if (cpfPoloPassivel!=null && !cpfPoloPassivel.isEmpty()) {												//polo passivo
			sql+=" and exists ( ";
			sql+="  SELECT 1 FROM PROC_PARTE PP2 WHERE P.ID_PROC=PP2.ID_PROC AND PP2.ID_PROC_PARTE_TIPO = ?";	ps.adicionarLong(ProcessoParteDt.ID_POLO_PASSIVO);
			if (boPoloaPassivoNull) {
				sql+="  AND PP2.CPF IS NULL AND PP2.CNPJ IS NULL ";
			}else {
				sql+="  AND (PP2.CPF = ? OR PP2.CNPJ = ?)) ";														ps.adicionarLong(cpfPoloPassivel);		ps.adicionarLong(cpfPoloPassivel);
			}
		}
		
		qtdRetorno = executarUpdateDelete(sql, ps);
		
		return qtdRetorno;
	}

	/**
	 * ATENÇÃO: ESTE MÉTODO FOI CRIADO ESPECIFICAMENTE PARA O SERVIÇO DE TROCA AUTOMÁTICA DE NÚMERO DE PROCESSOS.
	 * 
	 * Método criado para situações em que se precisa do próximo número de processo disponível para um ano passado,
	 * quando não é mais possível utilizar o sequence que já foi zerado. Retorna o maior número daquele ano e
	 * incrementa 1.
	 *  
	 * @param String ano
	 * @return String
	 * @throws Exception
	 * @author hrrosa
	 */
	public String retornaProximoProcessoNumeroAnoEspecifico(String ano) throws Exception {
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		ResultSetTJGO rs = null;
		String numeroNovo = null;
		String sql;
		
		sql = "SELECT MAX(PROC_NUMERO)+1 NUMERO_NOVO FROM PROC WHERE ANO = ?";
		ps.adicionarString(ano);
		try {
			rs = consultar(sql, ps);
			while (rs.next()) {
				numeroNovo = rs.getString("NUMERO_NOVO");
			}
		}
		finally{
			try{if(rs != null)rs.close();} catch(Exception e) {}
		}
		
		return numeroNovo;
	}
	
	/**
	 * Retorna o ID do processo utilizando número completo
	 * 	
	 * @param idProc
	 * @param numeroProcesso
	 * @param digitoVerificador
	 * @param ano
	 * @param forumCodigo
	 * @return String
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarIdProcessoNumeroCompleto(String numeroProcesso, String digitoVerificador, String ano, String forumCodigo) throws Exception {
		String Sql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		String idProc = null;

		Sql = "SELECT ID_PROC FROM PROJUDI.PROC ";
		
		Sql += " WHERE PROC_NUMERO = ?";			ps.adicionarLong(numeroProcesso);				
		Sql += " AND DIGITO_VERIFICADOR  = ?";		ps.adicionarLong(digitoVerificador);		
		Sql += " AND ANO = ?";						ps.adicionarLong(ano);
		if (forumCodigo != null) {
			Sql += " AND FORUM_CODIGO = ?";			ps.adicionarLong(forumCodigo);
		}
		
		try{
			rs1 = consultar(Sql, ps);			
			while (rs1.next()) {
				idProc = rs1.getString("ID_PROC");
			}	
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
				
		return idProc;
	}
	
	/**
	 * Método responsável em consultar os processos de acordo com parâmetros passados.
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * 
	 * @author lsbernardes/ Jrcorrea
	 * @data 17/07/2020
	 */
	public List consultarProcessosRedistribuicaoLoteTodos(String id_Serventia, String id_ServentiaCargo, String id_ProcessoTipo, String arquivado, String id_Classificador) throws Exception {
		String sql = "";
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;		
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "SELECT distinct p.ID_PROC, PROC_NUMERO, DATA_RECEBIMENTO, DIGITO_VERIFICADOR, PROC_STATUS_CODIGO, ANO, SEGREDO_JUSTICA, PROC_PRIOR_CODIGO, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO, SERV ";
		sql += " FROM PROJUDI.VIEW_BUSCA_PROC p ";	
		sql += " INNER JOIN PROC_RESP pr on (p.ID_PROC = pr.ID_PROC) WHERE ID_SERV = ? "; ps.adicionarLong(id_Serventia);		
		
		if (id_ServentiaCargo != null && id_ServentiaCargo.length()>0){
			sql += " AND ID_SERV_CARGO = ? "; ps.adicionarLong(id_ServentiaCargo);
		}
		
		if (id_Classificador != null && id_Classificador.length()>0){
			sql += " AND ID_CLASSIFICADOR = ? "; ps.adicionarLong(id_Classificador);
		} 
		
		if (id_ProcessoTipo != null && id_ProcessoTipo.length()>0){
			sql += " AND p.ID_PROC_TIPO = ? "; ps.adicionarLong(id_ProcessoTipo);
		} 
		
		if (arquivado != null && arquivado.equals("true")){
			sql += " AND DATA_ARQUIVAMENTO IS NOT NULL ";
		} else {
			sql += " AND DATA_ARQUIVAMENTO IS NULL ";
		}
		
		try{
			//Efetua consulta e chama método para consultar as partes de cada processo
			rs1 = consultar(sql, ps);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			//Conta registros 
			sql = "SELECT COUNT(1) AS QUANTIDADE FROM (" + sql +")";
			
		} finally{
			try{
				if (rs1 != null) rs1.close();				
			} catch(Exception e) {
			}
		}

		return listaProcessos;
	}
	
	
	/**
	 * Consulta a quantidade de processos sem assunto na serventia 
	 * @param id_Serventia identificação da serventia
	 * @author acbloureiro
	 */
	public long consultarQuantidadeProcessosSemAssunto(String id_Serventia) throws Exception {
		long loQuantidade= 0 ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Calendar DataArquivamentoLimite = Calendar.getInstance();
		//a data limete é 5 anos da data do arquivamento
		//retiro 5 anos e passa para o instante 0 do dia
		DataArquivamentoLimite.add(Calendar.YEAR, -5);
		DataArquivamentoLimite.add(Calendar.HOUR_OF_DAY, 0);
		DataArquivamentoLimite.add(Calendar.MINUTE, 0);
		DataArquivamentoLimite.add(Calendar.SECOND, 0);
		DataArquivamentoLimite.add(Calendar.MILLISECOND, 0);
		
		try{
			stSql =  " select count(1)  QUANTIDADE from proc p ";
			stSql += " inner join proc_parte pp on p.id_proc = pp.id_proc ";
			stSql += " where p.data_arquivamento is null ";
			//stSql += " or p.data_arquivamento >= ? ) ";	ps.adicionarDate(DataArquivamentoLimite.getTime());
			stSql += " AND p.ID_SERV = ? ";							ps.adicionarLong(id_Serventia);
			//Ocorrencia 2020/10121
			//Nesta consulta traz quanquer proc_status, mas a view_busca_processo verifica se o processo não é proc_status 7 e 8
			//Adicionei somente o proc_status de erro de migração = 7 
			stSql += " AND p.id_proc_status <> ? ";			ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO);
			
			//Ocorrencia 2021/2845
			//Esta consulta de quantidade é utilizada dentro da consulta dos processos ao clicar no link na tela.
			//Ao consultar no outro link, é feito um filtro na VIEW_BUSCA_PROC
			//Esta view leva em consideração somente o id-proc-parte-tipo 2 e 3
			stSql += " And pp.Id_Proc_Parte_Tipo In (?,?) ";
			ps.adicionarLong(ProcessoParteDt.ID_POLO_ATIVO);
			ps.adicionarLong(ProcessoParteDt.ID_POLO_PASSIVO);
			
			stSql += " and not exists (select 1 from proc_assunto pa where pa.id_proc=p.id_proc)";
						
			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				loQuantidade =  rs1.getLong("QUANTIDADE") ;
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return loQuantidade;
	}
	
	/**
	 * Lista os processos que estão sem assunto
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * 
	 * @author acbloureiro
	 */
	public List consultarProcessosSemAssunto(String id_Serventia, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		Calendar DataArquivamentoLimite = Calendar.getInstance();
		//a data limete é 5 anos da data do arquivamento
		//retiro 5 anos e passa para o instante 0 do dia
		DataArquivamentoLimite.add(Calendar.YEAR, -5);
		DataArquivamentoLimite.add(Calendar.HOUR_OF_DAY, 0);
		DataArquivamentoLimite.add(Calendar.MINUTE, 0);
		DataArquivamentoLimite.add(Calendar.SECOND, 0);
		DataArquivamentoLimite.add(Calendar.MILLISECOND, 0);
		
		stSqlCamposProcesso = "SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.PROC_STATUS_CODIGO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.Serv, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
		stSqlComumProcesso += " FROM  ( select p.id_proc  from proc p ";
		stSqlComumProcesso += "     inner join proc_parte pp on p.id_proc = pp.id_proc ";
		stSqlComumProcesso += "     where p.data_arquivamento is null ";
		//stSqlComumProcesso += " or p.data_arquivamento >= ? ) ";				ps.adicionarDate(DataArquivamentoLimite.getTime());
		stSqlComumProcesso +="      AND p.ID_SERV = ? ";						ps.adicionarLong(id_Serventia);
		
		//Ocorrencia 2020/10121
		//Nesta consulta traz quanquer proc_status, mas a view_busca_processo verifica se o processo não é proc_status 7 e 8
		//Adicionei somente o proc_status de erro de migração = 7 
		stSqlComumProcesso += "     AND p.id_proc_status <> ? ";			ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO);
		
		//Ocorrencia 2021/2845
		//Esta consulta interna é a mesma da consulta da quantidade
		//Não precisava adicionar este trecho aqui internamente pq a VIEW_BUSCA_PROC já faz o filtro do id-proc-parte-tipo
		//Mas adicionei para ficar igual.
		stSqlComumProcesso += "      And pp.Id_Proc_Parte_Tipo In (?,?) ";
		ps.adicionarLong(ProcessoParteDt.ID_POLO_ATIVO);
		ps.adicionarLong(ProcessoParteDt.ID_POLO_PASSIVO);
		
		stSqlComumProcesso += " and not exists (select 1 from proc_assunto pa where pa.id_proc=p.id_proc)";
				
		stSqlComumProcesso += " ) tab inner join VIEW_BUSCA_PROC p on tab.id_proc = p.id_proc ";
		
															
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			sql = "SELECT COUNT(1) AS QUANTIDADE FROM (" + stSqlCamposProcesso + stSqlComumProcesso + ")";

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	
	/**
	 * Consulta a quantidade de processos com assunto Pai na serventia 
	 * @param id_Serventia identificação da serventia
	 * @author acbloureiro
	 */
	public long consultarQuantidadeProcessosComAssuntoPai(String id_Serventia) throws Exception {
		long loQuantidade= 0 ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Calendar DataArquivamentoLimite = Calendar.getInstance();
		//a data limete é 5 anos da data do arquivamento
		//retiro 5 anos e passa para o instante 0 do dia
		DataArquivamentoLimite.add(Calendar.YEAR, -5);
		DataArquivamentoLimite.add(Calendar.HOUR_OF_DAY, 0);
		DataArquivamentoLimite.add(Calendar.MINUTE, 0);
		DataArquivamentoLimite.add(Calendar.SECOND, 0);
		DataArquivamentoLimite.add(Calendar.MILLISECOND, 0);
		
		try{
			stSql =  " select count(distinct(p.id_proc))  QUANTIDADE from proc p "; 
			stSql += "	    inner join proc_assunto pa on p.id_proc = pa.id_proc ";
			stSql += "      inner join proc_parte pp on p.id_proc = pp.id_proc ";
			stSql += "    where pa.id_assunto in (select id_assunto ";
			stSql += "	 from assunto where is_ativo = '0') ";
			stSql += " and p.data_arquivamento is null ";
			//stSql += " or p.data_arquivamento >= ? ) ";				ps.adicionarDate(DataArquivamentoLimite.getTime());
			stSql += " AND p.ID_SERV = ? ";							ps.adicionarLong(id_Serventia);
			
			//Ocorrencia 2021/2845
			stSql += " And pp.Id_Proc_Parte_Tipo In (?,?) ";
			ps.adicionarLong(ProcessoParteDt.ID_POLO_ATIVO);
			ps.adicionarLong(ProcessoParteDt.ID_POLO_PASSIVO);
			
			//Ocorrencia 2021/2948
			stSql += " And P.ID_PROC_STATUS not in (?,?) ";
			ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO);
			ps.adicionarLong(ProcessoStatusDt.SIGILOSO);
						
			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				loQuantidade =  rs1.getLong("QUANTIDADE") ;
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return loQuantidade;
	}
	
	/**
	 * Lista os processos que estão com assunto Pai
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * 
	 * @author acbloureiro
	 */
	public List consultarProcessosComAssuntoPai(String id_Serventia, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		Calendar DataArquivamentoLimite = Calendar.getInstance();
		//a data limete é 5 anos da data do arquivamento
		//retiro 5 anos e passa para o instante 0 do dia
		DataArquivamentoLimite.add(Calendar.YEAR, -5);
		DataArquivamentoLimite.add(Calendar.HOUR_OF_DAY, 0);
		DataArquivamentoLimite.add(Calendar.MINUTE, 0);
		DataArquivamentoLimite.add(Calendar.SECOND, 0);
		DataArquivamentoLimite.add(Calendar.MILLISECOND, 0);
		
		stSqlCamposProcesso = "SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.PROC_STATUS_CODIGO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.Serv, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
		stSqlComumProcesso += " FROM  ( select p.id_proc  from proc p "; 
		stSqlComumProcesso += "	    inner join proc_assunto pa on p.id_proc = pa.id_proc ";
		stSqlComumProcesso += "     inner join proc_parte pp on p.id_proc = pp.id_proc ";
		stSqlComumProcesso += "    where pa.id_assunto in (select id_assunto ";
		stSqlComumProcesso += "	 from assunto where is_ativo = '0') ";
//		stSqlComumProcesso += "	 from assunto where id_cnj_assunto in (select distinct(id_cnj_assunto_pai) ";
//		stSqlComumProcesso += "   from cnj_assunto where id_cnj_assunto_pai is not null  and is_ativo not in ('0','I'))) "; 
		stSqlComumProcesso += "     and p.data_arquivamento is null ";
		//stSqlComumProcesso += " or p.data_arquivamento >= ? ) ";				ps.adicionarDate(DataArquivamentoLimite.getTime());
		stSqlComumProcesso +="      AND p.ID_SERV = ? ";						ps.adicionarLong(id_Serventia);
		
		//Ocorrencia 2021/2845
		stSqlComumProcesso += " And pp.Id_Proc_Parte_Tipo In (?,?) ";
		ps.adicionarLong(ProcessoParteDt.ID_POLO_ATIVO);
		ps.adicionarLong(ProcessoParteDt.ID_POLO_PASSIVO);
		
		//Ocorrencia 2021/2948
		stSqlComumProcesso += " And P.ID_PROC_STATUS not in (?,?) ";
		ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO);
		ps.adicionarLong(ProcessoStatusDt.SIGILOSO);
				
		stSqlComumProcesso += " ) tab inner join VIEW_BUSCA_PROC p on tab.id_proc = p.id_proc ";
															
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			sql = "SELECT COUNT(1) AS QUANTIDADE FROM (" + stSqlCamposProcesso + stSqlComumProcesso + ")";

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	
	/**
	 * Consulta a quantidade de processos com classe Pai na serventia 
	 * @param id_Serventia identificação da serventia
	 * @author acbloureiro
	 */
	public long consultarQuantidadeProcessosComClassePai(String id_Serventia) throws Exception {
		long loQuantidade= 0 ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Calendar DataArquivamentoLimite = Calendar.getInstance();
		//a data limete é 5 anos da data do arquivamento
		//retiro 5 anos e passa para o instante 0 do dia
		DataArquivamentoLimite.add(Calendar.YEAR, -5);
		DataArquivamentoLimite.add(Calendar.HOUR_OF_DAY, 0);
		DataArquivamentoLimite.add(Calendar.MINUTE, 0);
		DataArquivamentoLimite.add(Calendar.SECOND, 0);
		DataArquivamentoLimite.add(Calendar.MILLISECOND, 0);
		
		try{
			stSql =  " select count(tab.id_proc)  QUANTIDADE from (";
			stSql += " (select distinct(p.id_proc) from proc p "; 
			stSql += "	    inner join proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo ";
			stSql += "      inner join serv s on s.id_serv = p.id_serv ";
			stSql += "    where pt.ativo = '0' ";		
			stSql += " and p.data_arquivamento is null ";
			//stSql += " or p.data_arquivamento >= ? ) ";				ps.adicionarDate(DataArquivamentoLimite.getTime());
			stSql += " and ((s.ID_SERV_tipo <>  ? "; 	ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
			stSql += ") or (s.ID_SERV_tipo = ? ";  		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
			stSql += " and not exists (select 1 from recurso r where r.id_proc = p.id_proc))) ";
			stSql += " AND p.ID_SERV = ? ";				ps.adicionarLong(id_Serventia);
			stSql += " ) union (";
			stSql += " select distinct(p.id_proc) from proc p";
			stSql += " inner join serv s on s.id_serv = p.id_serv";
			stSql += " inner join recurso r on r.id_proc = p.id_proc";
			stSql += " inner join proc_tipo pt on r.id_proc_tipo = pt.id_proc_tipo";
			stSql += " where pt.ativo = '0' ";
			stSql += " and p.data_arquivamento is null  ";
			stSql += " and s.ID_SERV_tipo = ? ";  		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
			stSql += " AND p.ID_SERV = ? ";				ps.adicionarLong(id_Serventia);
			stSql += " )) tab";
						
			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				loQuantidade =  rs1.getLong("QUANTIDADE") ;
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return loQuantidade;
	}
	
	/**
	 * Lista os processos que estão com classe Pai
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * 
	 * @author acbloureiro
	 */
	public List consultarProcessosComClassePai(String id_Serventia, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		Calendar DataArquivamentoLimite = Calendar.getInstance();
		//a data limete é 5 anos da data do arquivamento
		//retiro 5 anos e passa para o instante 0 do dia
		DataArquivamentoLimite.add(Calendar.YEAR, -5);
		DataArquivamentoLimite.add(Calendar.HOUR_OF_DAY, 0);
		DataArquivamentoLimite.add(Calendar.MINUTE, 0);
		DataArquivamentoLimite.add(Calendar.SECOND, 0);
		DataArquivamentoLimite.add(Calendar.MILLISECOND, 0);
		
			
		stSqlCamposProcesso = "SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.PROC_STATUS_CODIGO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.Serv, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
		stSqlComumProcesso += " FROM ((select p.id_proc  from proc p  "; 
		stSqlComumProcesso += "	    inner join proc_tipo pt on p.id_proc_tipo = pt.id_proc_tipo ";
		stSqlComumProcesso += "      inner join serv s on s.id_serv = p.id_serv ";
		stSqlComumProcesso += "    where pt.ativo = '0' ";		
		stSqlComumProcesso += " and p.data_arquivamento is null ";
		//stSqlComumProcesso += " or p.data_arquivamento >= ? ) ";				ps.adicionarDate(DataArquivamentoLimite.getTime());
		stSqlComumProcesso += " and ((s.ID_SERV_tipo <>  ? "; 	ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		stSqlComumProcesso += ") or (s.ID_SERV_tipo = ? ";  		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		stSqlComumProcesso += " and not exists (select 1 from recurso r where r.id_proc = p.id_proc))) ";
		stSqlComumProcesso += " AND p.ID_SERV = ? ";				ps.adicionarLong(id_Serventia);
		stSqlComumProcesso += " ) union (";
		stSqlComumProcesso += " select distinct(p.id_proc) from proc p";
		stSqlComumProcesso += " inner join serv s on s.id_serv = p.id_serv";
		stSqlComumProcesso += " inner join recurso r on r.id_proc = p.id_proc";
		stSqlComumProcesso += " inner join proc_tipo pt on r.id_proc_tipo = pt.id_proc_tipo";
		stSqlComumProcesso += " where pt.ativo = '0' ";
		stSqlComumProcesso += " and p.data_arquivamento is null  ";
		stSqlComumProcesso += " and s.ID_SERV_tipo = ? ";  		ps.adicionarLong(ServentiaTipoDt.SEGUNDO_GRAU);
		stSqlComumProcesso += " AND p.ID_SERV = ? ";				ps.adicionarLong(id_Serventia);	
		stSqlComumProcesso += " )) tab inner join VIEW_BUSCA_PROC p on tab.id_proc = p.id_proc ";
															
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			sql = "SELECT COUNT(1) AS QUANTIDADE FROM (" + stSqlCamposProcesso + stSqlComumProcesso + ")";

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	

	/**
	 * Consulta a quantidade de processos criminais de um serventia arquivados que estão sem o motivo do arquivamento 
	 * @param id_Serventia identificação da serventia
	 * @author jrcorrea
	 */
	public long consultarQuantidadeArquivadosSemMovito(String id_Serventia) throws Exception {
		long loQuantidade= 0 ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();
		Calendar DataArquivamentoLimite = Calendar.getInstance();
		//a data limete é 5 anos da data do arquivamento
		//retiro 5 anos e passa para o instante 0 do dia
		DataArquivamentoLimite.add(Calendar.YEAR, -5);
		DataArquivamentoLimite.add(Calendar.HOUR_OF_DAY, 0);
		DataArquivamentoLimite.add(Calendar.MINUTE, 0);
		DataArquivamentoLimite.add(Calendar.SECOND, 0);
		DataArquivamentoLimite.add(Calendar.MILLISECOND, 0);
		
		try{
			stSql =  " select count(1)  QUANTIDADE from proc p "; 
			stSql += "	    left join proc_criminal pc on p.id_proc = pc.id_proc ";
			stSql += "    where id_proc_arquivamento_tipo is null ";
			stSql += " AND id_proc_status not in (7,8)";
			stSql += " and p.data_arquivamento is not null";
			stSql += " and p.data_arquivamento >= ? ";				ps.adicionarDate(DataArquivamentoLimite.getTime());
			stSql += " and p.id_area = ? ";							ps.adicionarLong(AreaDt.CRIMINAL);
			stSql += " AND p.ID_SERV = ? ";							ps.adicionarLong(id_Serventia);
			stSql += " AND pc.id_usu_cert_inconsistencias is null ";
			
			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				loQuantidade =  rs1.getLong("QUANTIDADE") ;
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return loQuantidade;
	}
	
	/**
	 * Lista os processos que estão sem o motivo do arquivamento
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * 
	 * @author jrcorrea
	 */
	public List consultarProcessosArquivadosSemMovito(String id_Serventia, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		Calendar DataArquivamentoLimite = Calendar.getInstance();
		//a data limete é 5 anos da data do arquivamento
		//retiro 5 anos e passa para o instante 0 do dia
		DataArquivamentoLimite.add(Calendar.YEAR, -5);
		DataArquivamentoLimite.add(Calendar.HOUR_OF_DAY, 0);
		DataArquivamentoLimite.add(Calendar.MINUTE, 0);
		DataArquivamentoLimite.add(Calendar.SECOND, 0);
		DataArquivamentoLimite.add(Calendar.MILLISECOND, 0);
		
		stSqlCamposProcesso = "SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.PROC_STATUS_CODIGO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.Serv, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
		stSqlComumProcesso += " FROM  ( select p.id_proc  from proc p "; 
		stSqlComumProcesso += "	    left join proc_criminal pc on p.id_proc = pc.id_proc "; 
		stSqlComumProcesso += "     where id_proc_arquivamento_tipo is null "; 
		
		stSqlComumProcesso += " AND id_proc_status not in (?, ?)";							ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO);		ps.adicionarLong(ProcessoStatusDt.SIGILOSO);
		
		stSqlComumProcesso += "     and p.data_arquivamento is not null";
		stSqlComumProcesso += "     and p.data_arquivamento >= ? ";							ps.adicionarDate(DataArquivamentoLimite.getTime());
		stSqlComumProcesso +="      and p.id_area = ?";										ps.adicionarLong(AreaDt.CRIMINAL); 
		stSqlComumProcesso +="      AND p.ID_SERV = ? ";									ps.adicionarLong(id_Serventia);
		stSqlComumProcesso += " 	AND pc.id_usu_cert_inconsistencias is null ";
		
		stSqlComumProcesso += " ) tab inner join VIEW_BUSCA_PROC_TODOS p on tab.id_proc = p.id_proc ";
															
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			sql = "SELECT COUNT(1) AS QUANTIDADE FROM (" + stSqlCamposProcesso + stSqlComumProcesso + ")";

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	/**
	 * Consulta a quantidade de processos criminais de um serventia que os dados da parte estão incompletos 
	 * @param id_Serventia identificação da serventia
	 * @author jrcorrea
	 */
	public long consultarQuantidadeInconsistenciaPoloPassivo(String id_Serventia) throws Exception {
		long loQuantidade= 0 ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
				
		try{
			stSql =  " select count(1)  QUANTIDADE from proc p";
			stSql += " where p.id_proc in (";
			stSql += "select distinct p.id_proc from proc p inner join proc_parte pp on p.id_proc = pp.id_proc "; 		
			stSql += "    inner join proc_criminal pc on p.id_proc = pc.id_proc and pc.ID_USU_CERT_INCONSISTENCIAS is null ";
			stSql += "    where p.data_arquivamento is null";
			stSql += " and p.id_area = ? ";												ps.adicionarLong(AreaDt.CRIMINAL);
			stSql += " AND p.ID_SERV = ? ";												ps.adicionarLong(id_Serventia);
			
			stSql += " AND id_proc_status not in (?, ?)";								ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO);			ps.adicionarLong(ProcessoStatusDt.SIGILOSO);
			
			stSql += " and pp.id_proc_parte_tipo = ? "; 								ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);  
			stSql += " and ( pp.nome_mae is null or  "; 
			stSql += "            pp.nome_pai is null or  ";  
			stSql += "            pp.data_nascimento is null or  ";
			stSql += "            pp.id_naturalidade is null or "; 
			stSql += "            pp.cpf is null or "; 
			stSql += "            pp.rg is null)";
			stSql += " and pp.nome != 'Justiça Publica'";
			stSql += " and pp.nome != 'Secretaria De Segurança Pública'";
			stSql += " and pp.excluido = 0)";
	                
			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				loQuantidade =  rs1.getLong("QUANTIDADE") ;
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return loQuantidade;
	}
	
	/**
	 * Lista os processos que estão sem o motivo do arquivamento
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * 
	 * @author jrcorrea
	 */
	public List consultarProcessosInconsistenciaPoloPassivo(String id_Serventia, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		
		stSqlCamposProcesso = "SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.PROC_STATUS_CODIGO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.Serv, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
		stSqlComumProcesso += " FROM  ( select p.id_proc  from proc p inner join proc_parte pp on p.id_proc = pp.id_proc ";
		stSqlComumProcesso += "    inner join proc_criminal pc on p.id_proc = pc.id_proc and pc.ID_USU_CERT_INCONSISTENCIAS is null "; 
		stSqlComumProcesso += "    where p.data_arquivamento is null "; 
		stSqlComumProcesso += " and p.id_area = ? ";										ps.adicionarLong(AreaDt.CRIMINAL);  
		stSqlComumProcesso += " AND p.ID_SERV = ? ";										ps.adicionarLong(id_Serventia);  
		stSqlComumProcesso += " AND id_proc_status not in (?,?)";  							ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO);			ps.adicionarLong(ProcessoStatusDt.SIGILOSO);
		stSqlComumProcesso += " and pp.id_proc_parte_tipo = ? ";   							ps.adicionarLong(ProcessoParteTipoDt.ID_POLO_PASSIVO);
		stSqlComumProcesso += " and ( pp.nome_mae is null or  ";  
		stSqlComumProcesso += "            pp.nome_pai is null or  ";  
		stSqlComumProcesso += "            pp.data_nascimento is null or  "; 
		stSqlComumProcesso += "            pp.id_naturalidade is null or "; 
		stSqlComumProcesso += "            pp.cpf is null or "; 
		stSqlComumProcesso += "            pp.rg is null)"; 			
		stSqlComumProcesso += " and pp.nome != 'Justiça Publica'";
		stSqlComumProcesso += " and pp.nome != 'Secretaria De Segurança Pública'";
		stSqlComumProcesso += " and pp.excluido = 0";
		stSqlComumProcesso += " ) tab inner join VIEW_BUSCA_PROC p on tab.id_proc = p.id_proc ";
															
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			sql = "SELECT COUNT(1) AS QUANTIDADE FROM (" + stSqlCamposProcesso + stSqlComumProcesso + ")";

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	/**
	 * Consulta a quantidade de processos criminais de um serventia que o prazo da prisão já passou ou está para findar 
	 * @param id_Serventia identificação da serventia
	 * @author jrcorrea
	 */
	public long consultarQuantidadePrisaoForaPrazo(String id_Serventia) throws Exception {
		long loQuantidade= 0 ;
		String stSql;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();		
				
		try{
			stSql =  " select count(1)  QUANTIDADE from proc p";
			stSql += "  where p.id_proc in (";
			stSql += " select distinct p.id_proc from proc p";
			stSql += " inner join proc_parte pp on p.id_proc = pp.id_proc ";
			stSql += " inner join proc_parte_prisao_novo ppp on pp.id_proc_parte = ppp.id_proc_parte ";
			stSql += "    where p.data_arquivamento is null";
			stSql += " and p.id_area = ? ";											ps.adicionarLong(AreaDt.CRIMINAL);
			stSql += " AND p.ID_SERV = ? ";											ps.adicionarLong(id_Serventia);
			
			stSql += " AND id_proc_status not in (?, ?)";							ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO);
																					ps.adicionarLong(ProcessoStatusDt.SIGILOSO);
			
			stSql += " and (sysdate - 10)>=(ppp.data_prisao+ppp.prazo_prisao) "; 
			stSql += " and ppp.prazo_prisao<>0 ";
			stSql += " and ppp.data_evento is null)";  
	                
			rs1 = consultar(stSql, ps);
			
			if (rs1.next()) {
				loQuantidade =  rs1.getLong("QUANTIDADE") ;
			}
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
		}
		return loQuantidade;
	}
	
	/**
	 * Lista os processos que estão com parte presa e com prazo da prisão vencendo ou vencida
	 * 
	 * @param id_Serventia, identificação da serventia do processo que deseja consultar
	 * 
	 * @author jrcorrea
	 */
	public List consultarProcessosPrisaoForaPrazo(String id_Serventia, String posicao) throws Exception{
		String stSqlCamposProcesso = "";
		String stSqlComumProcesso = "";			
		String sql = "";				
		List listaProcessos = new ArrayList();
		ResultSetTJGO rs1 = null;
		ResultSetTJGO rs2 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();	
		
		stSqlCamposProcesso = "SELECT DISTINCT p.ID_PROC, p.PROC_NUMERO, p.PROC_STATUS_CODIGO, p.DIGITO_VERIFICADOR, p.ANO, p.PROC_PRIOR_ORDEM ,  p.PROC_PRIOR_CODIGO, p.DATA_RECEBIMENTO, p.SEGREDO_JUSTICA, p.Serv, PRIMEIRO_PROMOVENTE, PRIMEIRO_PROMOVIDO ";
		stSqlComumProcesso += " FROM  ( select p.id_proc  from proc p"; 
		stSqlComumProcesso += " inner join proc_parte pp on p.id_proc = pp.id_proc "; 
		stSqlComumProcesso += " inner join proc_parte_prisao_novo ppp on pp.id_proc_parte = ppp.id_proc_parte "; 
		stSqlComumProcesso += "    where p.data_arquivamento is null"; 
		stSqlComumProcesso += " and p.id_area = ? ";											ps.adicionarLong(AreaDt.CRIMINAL); 
		stSqlComumProcesso += " AND p.ID_SERV = ? ";											ps.adicionarLong(id_Serventia); 
				 
		stSqlComumProcesso += " AND id_proc_status not in (?, ?)";								ps.adicionarLong(ProcessoStatusDt.ERRO_MIGRACAO); 
																								ps.adicionarLong(ProcessoStatusDt.SIGILOSO); 
				 
		stSqlComumProcesso += " and (sysdate - 10)>=(ppp.data_prisao+ppp.prazo_prisao) ";
		stSqlComumProcesso += " and ppp.prazo_prisao<>0 ";
		stSqlComumProcesso += " and ppp.data_evento is null ";  
		stSqlComumProcesso += " ) tab inner join VIEW_BUSCA_PROC p on tab.id_proc = p.id_proc ";
															
		try{
			
			sql = stSqlCamposProcesso;
			sql += stSqlComumProcesso;
			sql += " ORDER BY PROC_PRIOR_ORDEM , DATA_RECEBIMENTO";
			
			rs1 = consultarPaginacao(sql, ps, posicao);
			
			ProcessoDt processoDt = null;
			while (rs1.next()) {
				processoDt = new ProcessoDt();
				processoDt.setId_Processo(rs1.getString("ID_PROC"));
				processoDt.setProcessoNumero(Funcoes.formataNumeroProcesso(rs1.getString("PROC_NUMERO")));
				processoDt.setDigitoVerificador(rs1.getString("DIGITO_VERIFICADOR"));
				processoDt.setAno(rs1.getString("ANO"));
				processoDt.setDataRecebimento(Funcoes.FormatarData(rs1.getDateTime("DATA_RECEBIMENTO")));
				processoDt.setSegredoJustica(Funcoes.FormatarLogico(rs1.getString("SEGREDO_JUSTICA")));
				processoDt.setProcessoStatusCodigo(rs1.getString("PROC_STATUS_CODIGO"));
				processoDt.setPrimeiroPromovente(rs1.getString("PRIMEIRO_PROMOVENTE"));
				processoDt.setPrimeiroPromovido(rs1.getString("PRIMEIRO_PROMOVIDO"));
				processoDt.setServentia(rs1.getString("SERV"));
				processoDt.setProcessoPrioridadeCodigo(rs1.getString("PROC_PRIOR_CODIGO"));

				listaProcessos.add(processoDt);
			}
			
			sql = "SELECT COUNT(1) AS QUANTIDADE FROM (" + stSqlCamposProcesso + stSqlComumProcesso + ")";

			rs2 = consultar(sql, ps);
			if (rs2.next()) listaProcessos.add(rs2.getLong("QUANTIDADE"));
		
		} finally{
			try{if (rs1 != null) rs1.close();} catch(Exception e) {}
			try{if (rs2 != null) rs2.close();} catch(Exception e) {}
		}

		return listaProcessos;
	}
	
	/**
	 * Método responsável em verificar se um processo é uma carta precátoria que teve o origem na expedição de uma pendência "Carta Precatória" realizada por um magistrado
	 * 
	 * @param id_processo, identificador do processo
	 * 
	 * @author lsbernardes
	 */
	public boolean isProcessoPrecatoriaExpedidaOnline(String id_processo) throws Exception {
		String sql = "";
		boolean retorno = false;
		ResultSetTJGO rs1 = null;
		PreparedStatementTJGO ps =  new PreparedStatementTJGO();

		sql = "select m.* ";
		sql += " from movi m  ";
		sql += " inner join usu_serv_grupo usg on m.id_usu_realizador = usg.id_usu_serv ";
		sql += " inner join grupo g on g.id_grupo = usg.ID_GRUPO and g.GRUPO_CODIGO in (?,?,?,?,?,?,?,?)  "; 
		ps.adicionarLong(GrupoDt.JUIZES_VARA); ps.adicionarLong(GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU); ps.adicionarLong(GrupoDt.JUIZES_TURMA_RECURSAL);  ps.adicionarLong(GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL);
		 ps.adicionarLong(GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL);  ps.adicionarLong(GrupoDt.JUIZ_EXECUCAO_PENAL);  ps.adicionarLong(GrupoDt.JUIZ_LEIGO);  ps.adicionarLong(GrupoDt.DESEMBARGADOR);
		sql += " where m.id_movi = (select min(id_movi) from movi m1 where id_proc = ?)  "; ps.adicionarLong(id_processo); 
		
		try{
			//Efetua consulta
			rs1 = consultar(sql, ps);
			retorno = rs1.next();
		
		} finally{
			try{
				if (rs1 != null) rs1.close();
			} catch(Exception e) {
			}
		}

		return retorno;
	}
	
}
