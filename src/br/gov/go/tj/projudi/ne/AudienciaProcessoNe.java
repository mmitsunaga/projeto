
package br.gov.go.tj.projudi.ne;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.FinalizacaoVotoSessaoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotanteDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ps.AudienciaProcessoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Certificado.Signer;

public class AudienciaProcessoNe extends AudienciaProcessoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8118898458055380765L;

	/**
	 * M�todo para vericar dados informados pelo usu�rio
	 */
	public String Verificar(AudienciaProcessoDt dados) {
		String stRetorno = "";
		if (dados.getAudienciaTipo().length() == 0) {
			stRetorno += "O Campo AudienciaTipo � obrigat�rio.";
		}
		if (dados.getProcessoNumero().length() == 0) {
			stRetorno += "O Campo ProcessoNumero � obrigat�rio.";
		}
		return stRetorno;
	}

	/**
	 * M�todo que receber� uma conex�o j� ativa. Este m�todo � respons�vel por comunicar com o base de dados para inserir ou atualizar objeto do tipo
	 * "AudienciaProcessoDt".
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaProcessoDt
	 * @param obFabricaConexao
	 * @throws Exception
	 */
	public void salvarAudienciaProcesso(AudienciaProcessoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt logDt;
		
		// SET CONEX�O
		AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());

		// INSER��O (AUDIENCIAPROCESSODT)
		if (audienciaProcessoDt.getId().equalsIgnoreCase("")) {
			logDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", audienciaProcessoDt.getPropriedades());
			obPersistencia.inserir(audienciaProcessoDt);
		} else {
			// ATUALIZA��O (AUDIENCIAPROCESSODT)
			logDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), audienciaProcessoDt.getPropriedades());
			obPersistencia.alterarAudienciaProcessoAgendamento(audienciaProcessoDt);
		}

		// COPIAR OBJETO DO TIPO "AUDIENCIAPROCESSODT" INSERIDO OU ATUALIZADO
		obDados.copiar(audienciaProcessoDt);

		// SALVAR LOG DA INSER��O OU ATUALIZA��O DO OBJETO DO TIPO "AUDIENCIAPROCESSODT"
		obLog.salvar(logDt, obFabricaConexao);
				
	}

	/**
	 * M�todo respons�vel por estabelecer uma conex�o com o banco de dados, caso essa conex�o venha null, e excluir objetos do tipo
	 * "AudienicaProcessoDt" livres, ou seja, objetos do tipo "AudienicaProcessoDt" que n�o estejam vinculados a algum processo (Id_Processo = NULL)
	 * 
	 * @author Keila Sousa Silva
	 * @param audienciaDtExcluir
	 * @param obFabricaConexao
	 * @throws Exception
	 */
	public void excluirAudienciaProcessoLivre(AudienciaDt audienciaDtExcluir, FabricaConexao obFabricaConexao) throws Exception {
	    
	    AudienciaProcessoPs obPersistencia = null;
		obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		/*
		 * SET AUDIENCIAPROCESSODT
		 */
		AudienciaProcessoDt audienciaProcessoDtExcluir = new AudienciaProcessoDt();
		// Id da Audi�ncia
		audienciaProcessoDtExcluir.setId_Audiencia(audienciaDtExcluir.getId());
		// Log
		audienciaProcessoDtExcluir.setId_UsuarioLog(audienciaDtExcluir.getId_UsuarioLog());
		audienciaProcessoDtExcluir.setIpComputadorLog(audienciaDtExcluir.getIpComputadorLog());

		/*
		 * EXCLUIR AUDIENCIAPROCESSODT
		 */
		obPersistencia.excluirAudienciaProcesso(audienciaProcessoDtExcluir);
		
	}

	/**
	 * Consulta se j� audi�ncias do tipo Sess�o de 2� Grau pendentes em um processo
	 * 
	 * @param id_Processo
	 *            identifica��o do processo
	 * @param obFabricaConexao
	 *            conex�o ativa
	 * @author msapaula
	 */
	public boolean verificaSessoesPendentesProcesso(String id_Processo, String id_processoTipo, FabricaConexao obFabricaConexao) throws Exception {
		return this.verificaAudienciasPendentesProcesso(id_Processo, AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.getCodigo(), id_processoTipo, obFabricaConexao);
	}

	/**
	 * Consulta se j� audi�ncias de qualquer tipo pendentes em um processo
	 * 
	 * @param id_Processo
	 *            identifica��o do processo
	 * @param obFabricaConexao
	 *            conex�o ativa
	 * @author msapaula
	 */
	public boolean verificaAudienciasPendentesProcesso(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		return this.verificaAudienciasPendentesProcesso(id_Processo, null, null, obFabricaConexao);
	}

	/**
	 * Consultar se h� audi�ncias pendentes em um processo, podendo filtrar por um determinado tipo ou retornar todas
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param audienciaTipoCodigo, tipo da audi�ncia para realizar a consulta de pendentes
	 * @return int quantidade de audi�ncias em aberto para processo
	 * @author msapaula
	 * @throws Exception 
	 * @since 02/03/2009 14:24
	 */
	private boolean verificaAudienciasPendentesProcesso(String id_Processo, Integer audienciaTipoCodigo, String id_processoTipo, FabricaConexao conexao) throws Exception{
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) 
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;

			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			
			List<String[]> audienciasPendentes = obPersistencia.consultarAudienciasPendentesProcesso(id_Processo, audienciaTipoCodigo, id_processoTipo);
			
			for (String[] audienciaPendente : audienciasPendentes) {
				if (audienciaPendente != null && 
						Funcoes.StringToInt(audienciaPendente[9]) != AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() &&
						Funcoes.StringToInt(audienciaPendente[9]) != AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() &&
						Funcoes.StringToInt(audienciaPendente[9]) != AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo()) boRetorno = true;
			}		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * Consultar se h� audi�ncias pendentes em um processo
	 * 
	 * @param id_Processo, identifica��o do processo
	 * 
	 * @return String[], vetor contendo a descri��o da audi�ncia e data agendada
	 * @author msapaula
	 */
	public List<String[]> consultarAudienciasPendentesProcesso(String id_Processo, boolean ehConsultaPublica) throws Exception {
		List<String[]> audienciaPendente = null;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao((ehConsultaPublica ? FabricaConexao.CONSULTA : FabricaConexao.PERSISTENCIA));
			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());

			audienciaPendente = obPersistencia.consultarAudienciasPendentesProcesso(id_Processo, null, null);
			PendenciaNe pendenciaNe = new PendenciaNe();
			for(int i = 0; i < audienciaPendente.size(); ++i) {
				String[] p = audienciaPendente.get(i);
				List<PendenciaDt> pendenciasResultado = pendenciaNe.consultarPendenciasAudienciaProcessoPorListaTipo(p[0], PendenciaTipoDt.RESULTADO_VOTACAO);
				List<PendenciaDt> pendenciasFinalizadasResultado = pendenciaNe.consultarPendenciasFinalizadasAudienciaProcessoPorListaTipo(p[0], obFabricaConexao, null, PendenciaTipoDt.RESULTADO_VOTACAO);
				p[12] = pendenciasResultado.size() + pendenciasFinalizadasResultado.size() > 0 ? "true" : "false";
				
				audienciaPendente.set(i, p);
			}
		
		} finally{
			obFabricaConexao.fecharConexao(); 
		}
		return audienciaPendente;
	}

	/**
	 * Consultar se h� audi�ncia pendente em um processo e para um determinado Serventia Cargo
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param id_ServentiaCargo, identifica��o do cargo
	 * @author msapaula
	 */
	public AudienciaProcessoDt consultarAudienciaPendenteProcesso(String id_Processo, String id_ServentiaCargo, String id_ProcessoTipoSessao, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoDt audienciaPendente = null;
		
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		audienciaPendente = obPersistencia.consultarAudienciaPendenteProcesso(id_Processo, id_ServentiaCargo, id_ProcessoTipoSessao);
		
		return audienciaPendente;
	}
	
	/**
	 * Consultar se h� audi�ncia pendente em um processo e para um determinado Serventia Cargo
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param id_ServentiaCargo, identifica��o do cargo
	 * @author mmgomes
	 */
	public AudienciaProcessoDt consultarAudienciaPendenteProcesso(String id_Processo, String id_ServentiaCargo, String id_ProcessoTipoSessao) throws Exception {
		AudienciaProcessoDt audienciaPendente = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			audienciaPendente = obPersistencia.consultarAudienciaPendenteProcesso(id_Processo, id_ServentiaCargo, id_ProcessoTipoSessao);		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return audienciaPendente;
	}

	/**
	 * Consultar se h� audi�ncia pendentes para um determinado Serventia Cargo
	 * 
	 * @param id_ServentiaCargo, identifica��o do cargo
	 * @author msapaula
	 */
	public boolean verificaAudienciasPendentesServentiaCargo(String id_ServentiaCargo, FabricaConexao obFabricaConexao) throws Exception {
		boolean boRetorno = false;
		
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());

		int qtde = obPersistencia.consultarAudienciasPendentesServentiaCargo(id_ServentiaCargo);
		if (qtde > 0) boRetorno = true;

		return boRetorno;
	}

	/**
	 * Atualiza o status de uma audi�ncia
	 * 
	 * @param audienciaProcessoDt, dt com dados da audi�ncia que ter� status modificado
	 * @param novoStatus, novo status da audi�ncia
	 * @param logDt, dados do log
	 * @param obFabricaConexao, conex�o ativa
	 * @author msapaula
	 */
	public void alterarStatusAudiencia(AudienciaProcessoDt audienciaProcessoDt, int novoStatus, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		String valorAtual = "[Id_AudienciaProcesso:" + audienciaProcessoDt.getId() + ";Id_AudienciaProcessoStatus:" + audienciaProcessoDt.getId_AudienciaProcessoStatus() + "]";
		String valorNovo = "[Id_AudienciaProcesso:" + audienciaProcessoDt.getId() + ";Id_AudienciaProcessoStatus:" + novoStatus + "]";

		obLogDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
		obPersistencia.alterarStatusAudiencia(audienciaProcessoDt.getId(), novoStatus);

		obLog.salvar(obLogDt, obFabricaConexao);

	}

	/**
	 * Altera o ServentiaCargo respons�vel por uma audi�ncia, em virtude de uma "Troca de Respons�vel"
	 * 
	 * @param audienciaProcessoDt, dt com dados da audi�ncia que ter� status modificado
	 * @param novoStatus, novo status da audi�ncia
	 * @param logDt, dados do log
	 * @param obFabricaConexao, conex�o ativa
	 * @author msapaula
	 */
	public void alterarResponsavelAudiencia(AudienciaProcessoDt audienciaProcessoDt, String id_NovoServentiaCargo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		String valorAtual = "[Id_AudienciaProcesso:" + audienciaProcessoDt.getId() + ";Id_ServentiaCargo:" + audienciaProcessoDt.getId_ServentiaCargo() + "]";
		String valorNovo = "[Id_AudienciaProcesso:" + audienciaProcessoDt.getId() + ";Id_ServentiaCargo:" + id_NovoServentiaCargo + "]";

		obLogDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
		obPersistencia.alterarResponsavelAudiencia(audienciaProcessoDt.getId(), id_NovoServentiaCargo);

		obLog.salvar(obLogDt, obFabricaConexao);

	}

	/**
	 * Retorna a sess�o do 2� grau marcada para o processo passado
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param id_ProcessoTipo, identifica��o do processo tipo
	 * @param obFabricaConexao, conex�o ativa
	 * @author msapaula
	 */
	public AudienciaProcessoDt consultarSessaoProcesso(String id_Processo, String id_ProcessoTipo, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoDt sessao = null;		
		
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		sessao = obPersistencia.consultarSessaoMarcada(id_Processo, id_ProcessoTipo);
		
		return sessao;
	}

	/**
	 * Vincula o processo a uma audi�ncia do tipo Sess�o do 2� grau passados. Consulta o serventiaCargo respons�vel pelo processo e adiciona ele como
	 * respons�vel pela sess�o, e ao final gera movimentacao "Inclu�do em Pauta"
	 * 
	 * @param audienciaDt, dt da audi�ncia do tipo sess�o
	 * @param processoDt, dt do processo a ser vinculado e para o qual ser� gerada a movimenta��o
	 * @param id_UsuarioServentia, usu�rio que est� marcando a sess�o
	 * @param logDt, objeto com dados do log
	 * @param obFabricaConexao, conexao ativa
	 * @author msapaula
	 */
	public AudienciaProcessoDt marcarSessaoProcesso(String id_Audiencia, String dataAgendada, String id_ProcessoTipo, String processoTipo, ProcessoDt processoDt, String id_UsuarioServentia, LogDt logDt, FabricaConexao obFabricaConexao, boolean isEmMesaParaJulgamento, UsuarioDt usuarioDt, String id_ServentiaCargoResponsavel) throws Exception {
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaNe audienciaNe = new AudienciaNe();
		ProcessoNe processoNe = new ProcessoNe();
		ServentiaNe serventiaNe = new ServentiaNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
				
		// Vincula audiencia com processo
		AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
		audienciaProcessoDt.setId_Audiencia(id_Audiencia);
		audienciaProcessoDt.setId_Processo(processoDt.getId());
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA));
		audienciaProcessoDt.setId_ProcessoTipo(id_ProcessoTipo);
		audienciaProcessoDt.setProcessoTipo(processoTipo);
		if (isEmMesaParaJulgamento) audienciaProcessoDt.setJulgamentoEmMesaParaJulgamento();

		//Consulta dados da serventia
		ServentiaDt serventiaDt = serventiaNe.consultarId(processoDt.getId_Serventia());
		
		if (id_ServentiaCargoResponsavel != null && id_ServentiaCargoResponsavel.trim().length() > 0) {
			audienciaProcessoDt.setId_ServentiaCargo(id_ServentiaCargoResponsavel);	
		} else {		
			ServentiaCargoDt serventiaCargoRelator = null;
			//Se Serventia � do Tipo Segundo Grau o tratamento ser� diferenciado
	    	if (ServentiaSubtipoDt.isSegundoGrau(serventiaDt.getServentiaSubtipoCodigo())){
	    		// Consulta o Relator do processo, nesse caso consulta o tipo de ProcessoResponsavel com CargoTipo Relator
	            serventiaCargoRelator = responsavelNe.consultarRelator2GrauConsideraSubstituicao(processoDt.getId(), processoDt.getId_Serventia(), null, obFabricaConexao);
	    	} else if (serventiaDt.isUpjTurmaRecursal()) {
	    		serventiaCargoRelator = responsavelNe.consultarProcessoResponsavel2Grau(processoDt.getId(), CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, true, obFabricaConexao); 	    		
	    	} else {
	    		serventiaCargoRelator = responsavelNe.getRelatorResponsavelProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);	
	    	}
	    	if (serventiaCargoRelator == null) {
	    		throw new MensagemException("Relator n�o encontrado.");
	    	}
			
			audienciaProcessoDt.setId_ServentiaCargo(serventiaCargoRelator.getId());
		}
		audienciaProcessoDt.setId_UsuarioLog(logDt.getId_Usuario());
		audienciaProcessoDt.setIpComputadorLog(logDt.getIpComputador());
		
		// jvosantos - 08/10/2019 12:23 - Caso a sess�o virtual j� esteja iniciada, busca um presidente e um MP
		AudienciaDt audienciaDt = audienciaNe.consultarAudienciaCompleta(id_Audiencia, obFabricaConexao);
		
		if(audienciaDt.isVirtual() && audienciaDt.isSessaoIniciada()) {
			Optional<String> idServentiaCargoPresidente = audienciaDt.getListaAudienciaProcessoDt().stream().map(x -> x.getId_ServentiaCargoPresidente()).filter(x -> StringUtils.isNotEmpty(x)).findAny();
			Optional<String> idServentiaCargoMP = audienciaDt.getListaAudienciaProcessoDt().stream().map(x -> x.getId_ServentiaCargoMP()).filter(x -> StringUtils.isNotEmpty(x)).findAny();
			
			if(idServentiaCargoPresidente.isPresent())
				audienciaProcessoDt.setId_ServentiaCargoPresidente(idServentiaCargoPresidente.get());
			
			if(idServentiaCargoMP.isPresent())
				audienciaProcessoDt.setId_ServentiaCargoMP(idServentiaCargoMP.get());
		}
		
		this.salvarAudienciaProcesso(audienciaProcessoDt, obFabricaConexao);
		
		// Gera movimenta��o Inclu�do em pauta
		MovimentacaoDt movimentacaoDt = null;
		
		if (audienciaDt.isVirtual()) {
			if (isEmMesaParaJulgamento) movimentacaoDt = movimentacaoNe.gerarMovimentacaoSessaoEmMesaParaJulgamento(processoDt.getId(), id_UsuarioServentia, getDescricaoMovimentacaoSessaoVirtual("Em Mesa para Julgamento - Sess�o do dia ", dataAgendada, processoTipo), logDt, obFabricaConexao);
			else movimentacaoDt = movimentacaoNe.gerarMovimentacaoAudienciaSessaoMarcada(processoDt.getId(), id_UsuarioServentia, getDescricaoMovimentacaoSessaoVirtual("Sess�o do dia", dataAgendada, processoTipo), logDt, obFabricaConexao);
		} else {
			if (isEmMesaParaJulgamento) movimentacaoDt = movimentacaoNe.gerarMovimentacaoSessaoEmMesaParaJulgamento(processoDt.getId(), id_UsuarioServentia, "(Em Mesa para Julgamento - Sess�o do dia " + dataAgendada + (processoTipo != null && processoTipo.length() > 0 ? " - " + processoTipo : "") + ")", logDt, obFabricaConexao);
			else movimentacaoDt = movimentacaoNe.gerarMovimentacaoAudienciaSessaoMarcada(processoDt.getId(), id_UsuarioServentia, "(Sess�o do dia " + dataAgendada + (processoTipo != null && processoTipo.length() > 0 ? " - " + processoTipo : "") + ")", logDt, obFabricaConexao);
		}		
		
		//gerar voto se for 2� grau
		if (!ServentiaSubtipoDt.isTurma( usuarioDt.getServentiaSubtipoCodigo())){
			//Gerar pend�ncia de voto para o desembargador inserir o voto, caso n�o exista...			
			audienciaProcessoDt.setProcessoDt(processoNe.consultarId(audienciaProcessoDt.getId_Processo()));
			
			PendenciaDt pendenciaDtVoto = null;	//Tentar obter uma poss�vel pend�ncia do tipo voto que o magistrado poderia ter criado por uma conclus�o.
			List listaDePendenciasVoto = pendenciaNe.consultarPendenciasVotoEmentaProcesso(audienciaProcessoDt.getProcessoDt().getId(), audienciaProcessoDt.getId_ServentiaCargo(), String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), audienciaProcessoDt.getId(), obFabricaConexao);
			if (listaDePendenciasVoto != null && listaDePendenciasVoto.size() > 0) pendenciaDtVoto = (PendenciaDt) listaDePendenciasVoto.get(0);
			if (pendenciaDtVoto == null) pendenciaDtVoto = audienciaNe.gerarPendenciaVoto(audienciaProcessoDt.getId_ServentiaCargo(), movimentacaoDt.getId(), audienciaProcessoDt.getProcessoDt().getId(), audienciaProcessoDt.getProcessoDt().getId_ProcessoPrioridade(), false, pendenciaNe, usuarioDt, obFabricaConexao, null, audienciaProcessoDt.getId());
			audienciaProcessoDt.setId_PendenciaVotoRelator(pendenciaDtVoto.getId());
			
			PendenciaDt pendenciaDtEmenta = null; //Tentar obter uma poss�vel pend�ncia do tipo ementa que o magistrado poderia ter criado por uma conclus�o.
			List listaDePendenciasEmenta = pendenciaNe.consultarPendenciasVotoEmentaProcesso(audienciaProcessoDt.getProcessoDt().getId(), audienciaProcessoDt.getId_ServentiaCargo(), String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), audienciaProcessoDt.getId(), obFabricaConexao);
			if (listaDePendenciasEmenta != null && listaDePendenciasEmenta.size() > 0) pendenciaDtEmenta = (PendenciaDt) listaDePendenciasEmenta.get(0);
			if (pendenciaDtEmenta != null) audienciaProcessoDt.setId_PendenciaEmentaRelator(pendenciaDtEmenta.getId());
			
			this.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);
			
			List<String> idPends = new ArrayList<String>();
			
			if(pendenciaDtEmenta != null) idPends.add(pendenciaDtEmenta.getId());
			if(pendenciaDtVoto != null) idPends.add(pendenciaDtVoto.getId());
			
			if (audienciaDt.isVirtual()) {
				AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
				audienciaProcessoPendenciaNe.salvar(idPends, audienciaProcessoDt.getId(), obFabricaConexao);
			}
		}
				
		return audienciaProcessoDt;

	}

	public String getDescricaoMovimentacaoSessaoVirtual(String texto, String dataAgendada, String processoTipo) {
		Pattern p = Pattern.compile("((\\d?\\d\\/){2}\\d{4}) (\\d{2}:\\d{2}:\\d{2})");
		Matcher m = p.matcher(dataAgendada);
		
		String dataLimpa = m.find() ? m.group(1) + " " + m.group(3) : dataAgendada;
		
		return "("+ texto +" " + dataLimpa + " (Virtual)" + (processoTipo != null && processoTipo.length() > 0 ? " - " + processoTipo : "") + ")";
	}

	/**
	 * Metodo que desmarca a sess�o de um processo. Internamente ir� modificar o status da audi�ncia conforme o par�metro passado e gera a
	 * movimenta��o "Retirado de pauta"
	 * 
	 * @param processoDt, dt do processo para o qual a sessao sera redesignada
	 * @param id_ProcessoTipo, identifica��o do processo tipo
	 * @param statusAudiencia, novo status da audi�ncia (por exemplo, redesignada ou cancelada)
	 * @param usuarioDt, usu�rio que est� desmarcando a sess�o
	 * @param logDt, objeto com dados do log
	 * @param obFabricaConexao, conexao ativa
	 * @author msapaula
	 */
	public void desmarcarSessaoProcesso(ProcessoDt processoDt, String id_ProcessoTipo, int statusAudiencia, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
		
		// Resgata a sess�o marcada para o processo
		AudienciaProcessoDt audienciaProcessoDt = this.consultarSessaoProcesso(processoDt.getId(), id_ProcessoTipo, obFabricaConexao);

		if (audienciaProcessoDt != null) {
			// Atualiza dados em "AudienciaProcesso"
			this.alterarAudienciaProcessoMovimentacao(audienciaProcessoDt, Funcoes.DataHora(new Date()), String.valueOf(statusAudiencia), logDt, obFabricaConexao);

			// Insere respons�veis pela audi�ncia, ou seja, quem realizou
			AudienciaProcessoResponsavelNe audienciaProcessoResponsavelNe = new AudienciaProcessoResponsavelNe();
			audienciaProcessoResponsavelNe.salvarAudienciaProcessoResponsavel(audienciaProcessoDt.getId(), audienciaProcessoDt.getAudienciaDt().getAudienciaTipoCodigo(), processoDt, usuarioDt, logDt, obFabricaConexao);

			// Gera movimenta��o Retirado de pauta
			MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
			String complemento = "(Sess�o do dia " + audienciaProcessoDt.getAudienciaDt().getDataAgendada() + ")";
			movimentacaoNe.gerarMovimentacaoAudienciaSessaoDesmarcada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, obFabricaConexao);
		}
	}
	
	/**
	 * Metodo que retirar a sess�o de um processo. Internamente ir� modificar o status da audi�ncia conforme o par�metro passado e gera a
	 * movimenta��o "Retirar Sess�o"
	 * 
	 * @param processoDt, dt do processo para o qual a sessao sera redesignada
	 * @param id_ProcessoTipo, identifica��o do processo tipo
	 * @param statusAudiencia, novo status da audi�ncia (por exemplo, redesignada ou cancelada)
	 * @param usuarioDt, usu�rio que est� desmarcando a sess�o
	 * @param logDt, objeto com dados do log
	 * @param obFabricaConexao, conexao ativa
	 * @author msapaula
	 */
	public void retirarSessaoProcesso(ProcessoDt processoDt, String id_ProcessoTipo, int statusAudiencia, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws MensagemException, Exception {
		
		// Resgata a sess�o marcada para o processo
		AudienciaProcessoDt audienciaProcessoDt = this.consultarSessaoProcesso(processoDt.getId(), id_ProcessoTipo, obFabricaConexao);

		if (audienciaProcessoDt != null) {
			// Atualiza dados em "AudienciaProcesso"
			this.alterarAudienciaProcessoMovimentacao(audienciaProcessoDt, Funcoes.DataHora(new Date()), String.valueOf(statusAudiencia), logDt, obFabricaConexao);

			// Insere respons�veis pela audi�ncia, ou seja, quem realizou
			AudienciaProcessoResponsavelNe audienciaProcessoResponsavelNe = new AudienciaProcessoResponsavelNe();
			audienciaProcessoResponsavelNe.salvarAudienciaProcessoResponsavel(audienciaProcessoDt.getId(), audienciaProcessoDt.getAudienciaDt().getAudienciaTipoCodigo(), processoDt, usuarioDt, logDt, obFabricaConexao);

			// Gera movimenta��o Retirado de pauta
			MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
			String complemento = "(Sess�o do dia " + audienciaProcessoDt.getAudienciaDt().getDataAgendada() + ")";
			movimentacaoNe.gerarMovimentacaoAudienciaSessaoRetirada(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), complemento, logDt, obFabricaConexao);
		}
	}

	/**
	 * M�todo respons�vel por "alimentar" as propriedades do objeto do tipo "Audi�nciaProcessoDt" de acordo com os par�metros passados.
	 * 
	 * @author Keila Sousa Silva
	 * @since 06/08/2009
	 * @param audienciaDt
	 * @param id_AudienciaProcessoStatus
	 * @param audienciaProcessoStatusCodigo
	 * @param audienciaProcessoStatus
	 * @param id_ServentiaCargo
	 * @param serventiaCargo
	 * @param id_Processo
	 * @param processoNumero
	 * @param id_UsuarioLog
	 * @param iPComputadorLog
	 * @return AudienciaProcessoDt
	 */
	public AudienciaProcessoDt prepararAudienciaProcessoDt(AudienciaDt audienciaDt, String id_AudienciaProcessoStatus, String audienciaProcessoStatusCodigo, String audienciaProcessoStatus, String id_ServentiaCargo, String serventiaCargo, String id_Processo, String processoNumero, String id_UsuarioLog, String iPComputadorLog) {
		AudienciaProcessoDt audienciaProcessoDt = null;

		if (audienciaDt.getListaAudienciaProcessoDt().size() == 0) {
			audienciaProcessoDt = new AudienciaProcessoDt();
		} else {
			audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
		}

		// Id do Status da Audi�ncia de Processo
		audienciaProcessoDt.setId_AudienciaProcessoStatus(id_AudienciaProcessoStatus);
		// C�digo do Status da Audi�ncia de Processo
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo(audienciaProcessoStatusCodigo);
		// Descri��o do Status da Audi�ncia de Processo
		audienciaProcessoDt.setAudienciaProcessoStatus(audienciaProcessoStatus);
		// Id do Cargo da Serventia
		audienciaProcessoDt.setId_ServentiaCargo(id_ServentiaCargo);
		// Descri��o do Cargo da Serventia
		audienciaProcessoDt.setServentiaCargo(serventiaCargo);
		// Id do Processo
		audienciaProcessoDt.setId_Processo(id_Processo);
		// N�mero do Processo
		audienciaProcessoDt.setProcessoNumero(processoNumero);

		// LOG (ID DO USU�RIO E IP DO COMPUTADOR)
		// Id do Usu�rio
		audienciaProcessoDt.setId_UsuarioLog(id_UsuarioLog);
		// IP do Computador
		audienciaProcessoDt.setIpComputadorLog(iPComputadorLog);

		return audienciaProcessoDt;
	}

	/**
	 * M�todo respons�vel por criar, instanciar e retornar uma lista contendo objeto do tipo "Audi�nciaProcessoDt". Este m�todo est� sendo usado no
	 * neg�cio referente � cria��o de agendas de audi�ncias.
	 * 
	 * @author Keila Sousa Silva
	 * @since 10/08/2009
	 * @param id_AudienciaProcessoStatus
	 * @param audienciaDt
	 * @return
	 */
	public List criarListaAudienciaProcessoDt(int audienciaProcessoStatusCodigo, AudienciaDt audienciaDt) {
		List listaAudienciaProcessoDt = new ArrayList();
		AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo(String.valueOf(audienciaProcessoStatusCodigo));

		listaAudienciaProcessoDt.add(audienciaProcessoDt);

		return listaAudienciaProcessoDt;
	}
	
	/**
	 * Atualiza dados em "AudienciaProcesso" em virtude da movimenta��o.
	 * Quando uma audi�ncia � movimentada dever� armazenar na tabela "AudienciaProcesso" a data da movimenta��o,
	 * e o status da Audi�ncia.
	 * 
	 * @param audienciaProcessoDt
	 * @param id_UsuarioRealizador
	 * @param statusCodigo
	 * @param logDt
	 * @param obFabricaConexao
	 * @throws Exception
	 */
	public void alterarAudienciaProcessoMovimentacao(AudienciaProcessoDt audienciaProcessoDt, String dataMovimentacao, String statusCodigo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		//Guarda dados anteriores � altera��o para gerar log
		String valorAtual = audienciaProcessoDt.getPropriedades();

		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());

		//Atualiza dados em AudienciaProcesso
		audienciaProcessoDt.setDataMovimentacao(dataMovimentacao);
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo(statusCodigo);

		//Captura dados novos do objeto
		String valorNovo = audienciaProcessoDt.getPropriedades();
		LogDt obLogDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.MovimentacaoAudiencia), valorAtual, valorNovo);

		obPersistencia.alterarAudienciaProcessoMovimentacao(audienciaProcessoDt);

		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	/**
	 * M�todo respons�vel em consultar todos os processos vinculados a uma audi�ncia passada, independente do status de "AudienciaProcesso".
	 * 
	 * @param id_Audiencia, identifica��o da audi�ncia selecionada
	 * @return List listaSessoes
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAudienciaProcessos(String id_Audiencia, UsuarioDt usuarioDt, boolean isSegundoGrau) throws Exception {
		List listaAudienciaProcesso = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		
		try{

			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {			
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					listaAudienciaProcesso = obPersistencia.consultarAudienciaProcessosAssistenteGabinete(id_Audiencia, true, usuarioDt.getId_ServentiaCargo());
					break;				
	
				default:				
					listaAudienciaProcesso = obPersistencia.consultarAudienciaProcessos(id_Audiencia, false);
					break;			
		    }
			
			if (listaAudienciaProcesso != null)
			{
				for (int i = 0; i < listaAudienciaProcesso.size(); i++)
				{
					AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) listaAudienciaProcesso.get(i);
					List listaPromotores = processoResponsavelNe.getListaPromotoresResponsavelProcesso(audienciaProcessoDt.getProcessoDt().getId(), null , obFabricaConexao);
					if(listaPromotores != null && listaPromotores.size() > 0){
						for(Object promotorObj : listaPromotores) {
							ServentiaCargoDt promotor = (ServentiaCargoDt) promotorObj;
							if (audienciaProcessoDt.getProcessoDt() != null) {
								if ((isSegundoGrau && promotor.isPromotoriaSegundoGrau()) ||
									(!isSegundoGrau && promotor.isPromotoriaPrimeiroGrau())) {
									audienciaProcessoDt.setId_ServentiaCargoMP(promotor.getId());
									audienciaProcessoDt.setServentiaCargoMP(promotor.getServentiaCargo());
									audienciaProcessoDt.setNomeMPProcesso(promotor.getNomeUsuario());
								}
							}								
						}											
					}
					
					 switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {			
						case GrupoTipoDt.ASSISTENTE_GABINETE:
						case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
							if (processoResponsavelNe.isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), audienciaProcessoDt.getProcessoDt().getId(), obFabricaConexao)){
								if(audienciaProcessoDt.getId_ServentiaCargoRedator() != null && audienciaProcessoDt.getId_ServentiaCargoRedator().trim().length() > 0) audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaRedator().equalsIgnoreCase(usuarioDt.getId_Serventia())));
								else audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaResponsavel().equalsIgnoreCase(usuarioDt.getId_Serventia())));
							}
							break;
							
						case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
							if(audienciaProcessoDt.getId_ServentiaCargoRedator() != null && audienciaProcessoDt.getId_ServentiaCargoRedator().trim().length() > 0) audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaCargoRedator().equalsIgnoreCase(usuarioDt.getId_ServentiaCargoUsuarioChefe())));
							else audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaCargo().equalsIgnoreCase(usuarioDt.getId_ServentiaCargoUsuarioChefe())));
							break;
							
						case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
							if(audienciaProcessoDt.getId_ServentiaCargoRedator() != null && audienciaProcessoDt.getId_ServentiaCargoRedator().trim().length() > 0) audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaCargoRedator().equalsIgnoreCase(usuarioDt.getId_ServentiaCargo())));
							else audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaCargo().equalsIgnoreCase(usuarioDt.getId_ServentiaCargo())));
							break;
					}         
				}
			}
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciaProcesso;
	}

	/**
	 * M�todo respons�vel em consultar todos os processos vinculados a uma audi�ncia passada, onde o julgamento ainda n�o foi realizado.
	 * Retorna uma lista de objetos "AudienciaProcessoDt" onde o status � "A Ser Realizada".
	 * 
	 * @param id_Audiencia, identifica��o da audi�ncia selecionada
	 * @return List listaSessoes
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarAudienciaProcessosPendentes(String id_Audiencia, UsuarioDt usuarioDt) throws Exception {
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		List listaAudienciaProcesso = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {			
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					listaAudienciaProcesso = obPersistencia.consultarAudienciaProcessosAssistenteGabinete(id_Audiencia, true, usuarioDt.getId_ServentiaCargo());
					break;				
	
				default:				
					listaAudienciaProcesso = obPersistencia.consultarAudienciaProcessos(id_Audiencia, true);
					break;
			}
			
			if (listaAudienciaProcesso != null)
			{
				for (int i = 0; i < listaAudienciaProcesso.size(); i++)
				{
					AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) listaAudienciaProcesso.get(i);
					List listaPromotores = processoResponsavelNe.getListaPromotoresResponsavelProcesso(audienciaProcessoDt.getProcessoDt().getId(), null , obFabricaConexao);
					if(listaPromotores != null && listaPromotores.size() > 0){
						ServentiaCargoDt promotor = (ServentiaCargoDt) listaPromotores.get(0);
						audienciaProcessoDt.setId_ServentiaCargoMP(promotor.getId());
						audienciaProcessoDt.setServentiaCargoMP(promotor.getServentiaCargo());
						audienciaProcessoDt.setNomeMPProcesso(promotor.getNomeUsuario());						
					}
					
					 switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {			
						case GrupoTipoDt.ASSISTENTE_GABINETE:
						case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
							if (processoResponsavelNe.isResponsavelProcesso(usuarioDt.getId_ServentiaCargo(), audienciaProcessoDt.getProcessoDt().getId(), obFabricaConexao)){
								if(audienciaProcessoDt.getId_ServentiaCargoRedator() != null && audienciaProcessoDt.getId_ServentiaCargoRedator().trim().length() > 0) audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaRedator().equalsIgnoreCase(usuarioDt.getId_Serventia())));
								else audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaResponsavel().equalsIgnoreCase(usuarioDt.getId_Serventia())));
							}
							break;
							
						case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
							if(audienciaProcessoDt.getId_ServentiaCargoRedator() != null && audienciaProcessoDt.getId_ServentiaCargoRedator().trim().length() > 0) audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaCargoRedator().equalsIgnoreCase(usuarioDt.getId_ServentiaCargoUsuarioChefe())));
							else audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaCargo().equalsIgnoreCase(usuarioDt.getId_ServentiaCargoUsuarioChefe())));
							break;
							
						case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
							if(audienciaProcessoDt.getId_ServentiaCargoRedator() != null && audienciaProcessoDt.getId_ServentiaCargoRedator().trim().length() > 0) audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaCargoRedator().equalsIgnoreCase(usuarioDt.getId_ServentiaCargo())));
							else audienciaProcessoDt.setIsResponsavelSessao((audienciaProcessoDt.getId_ServentiaCargo().equalsIgnoreCase(usuarioDt.getId_ServentiaCargo())));
							break;
					}         
				}
			}
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciaProcesso;
	}

	/**
	 * Altera o ServentiaCargo respons�vel por uma audi�ncia (Sess�o de 2� grau), em virtude de uma "Troca de Respons�vel"
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param id_ServentiaCargoAtual, cargo atual que ser� trocado
	 * @param id_ServentiaCargoNovo, novo cargo
	 * @param logDt, objeto log
	 * @param fabrica, conex�o ativa
	 * @author msapaula
	 */
	public void atualizarResponsavelAudiencia(String id_Processo, String id_ServentiaCargoAtual, String id_ServentiaCargoNovo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		
		//Se possuir uma audi�ncia ou sess�o de 2� marcada deve trocar o respons�vel por essa tamb�m
		AudienciaProcessoDt audienciaPendente = this.consultarAudienciaPendenteProcesso(id_Processo, id_ServentiaCargoAtual, null, obFabricaConexao);

		if (audienciaPendente != null) {
			this.alterarResponsavelAudiencia(audienciaPendente, id_ServentiaCargoNovo, logDt, obFabricaConexao);
		}

	}

	/**
	 * Verifica dados obrigat�rios na troca de respons�vel da audi�ncia
	 * @author msapaula
	 */
	public String verificarTrocaResponsavel(AudienciaProcessoDt dados, List listaAudienciaProcessos) {
		String stRetorno = "";
		if (dados.getId_ServentiaCargo().length() == 0 && dados.getId_ServentiaCargo().length() == 0) stRetorno = "Selecione o Novo Respons�vel pelo Processo. \n";
		if (listaAudienciaProcessos == null || listaAudienciaProcessos.size() == 0) stRetorno += "Nenhum Processo foi selecionado. \n";
		else {
			List processos = listaAudienciaProcessos;
			for (int i = 0; i < processos.size(); i++) {
				AudienciaProcessoDt obj = (AudienciaProcessoDt) processos.get(i);
				// Novo respons�vel n�o pode ser o mesmo que o atual
				if (obj.getId_ServentiaCargo() != null && obj.getId_ServentiaCargo().equals(dados.getId_ServentiaCargo())) stRetorno += dados.getServentiaCargo() + " j� � Respons�vel pela Audi�ncia.";
			}
		}
		return stRetorno;
	}

	/**
	 * Retorna os dados completos de uma AudienciaProcesso, j� setando os dados da audi�ncia vinculada
	 * 
	 * @param id_AudienciaProcesso, identifica��o da AudienciaProcesso
	 * @author msapaula
	 * @throws Exception 
	 */
	public AudienciaProcessoDt consultarIdCompleto(String id_AudienciaProcesso, FabricaConexao obFabricaConexao) throws Exception {
		if(id_AudienciaProcesso == null) return null; // jvosantos - 14/10/2019 12:33 - Corre��o erro de NullPointer
		AudienciaProcessoDt dtRetorno = null;
		
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarIdCompleto(id_AudienciaProcesso);
		obDados.copiar(dtRetorno);
		
		return dtRetorno;
	}
	
	// jvosantos - 07/10/2019 15:36 - Overload para passar fabrica de conex�o
	public AudienciaProcessoDt consultarIdCompleto(String id_AudienciaProcesso) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			return consultarIdCompleto(id_AudienciaProcesso, obFabricaConexao);
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public AudienciaProcessoDt consultarIdCompleto(String id_AudienciaProcesso, Connection connection) throws Exception {
		if(id_AudienciaProcesso == null) return null; // jvosantos - 14/10/2019 12:33 - Corre��o erro de NullPointer
		AudienciaProcessoDt dtRetorno = null;
		
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(connection);
		dtRetorno = obPersistencia.consultarIdCompleto(id_AudienciaProcesso);
		obDados.copiar(dtRetorno);
		
		return dtRetorno;
	}

	/**
	 * Salva Troca de Respons�vel de uma ou mais Audi�ncias.
	 * 
	 * @param id_ServentiaCargo, identifica��o do novo serventia cargo
	 * @param listaAudienciaProcessos, lista de AudienciaProcesso que ter� o respons�vel alterado
	 * @param logDt, objeto log
	 * @author msapaula
	 */
	public void salvarTrocaResponsavel(String id_ServentiaCargo, List listaAudienciaProcessos, LogDt logDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			//Para cada AudienciaProcesso atualiza o respons�vel
			for (int i = 0; i < listaAudienciaProcessos.size(); i++) {
				AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt) listaAudienciaProcessos.get(i);

				this.alterarResponsavelAudiencia(audienciaProcessoDt, id_ServentiaCargo, logDt, obFabricaConexao);
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

	}

	/**
	 * Realiza chamada ao m�todo que retorna os Cargos dispon�veis para o agendamento de audi�ncia que correspode aos cargos
	 * que podem ser respons�veis por audi�ncia
	 * @param tempNomeBusca
	 * @param posicaoPaginaAtual
	 * @param id_Serventia
	 * @return
	 */
	public List consultarServentiaCargosAgendaAudiencia(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		List listaServentiaCargos = new ArrayList();
		listaServentiaCargos = serventiaCargoNe.consultarServentiaCargosAgendaAudiencia(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		QuantidadePaginas = serventiaCargoNe.getQuantidadePaginas();
		serventiaCargoNe = null;
		return listaServentiaCargos;
	}
	
	public String consultarServentiaCargosAgendaAudienciaJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaSubTipoCodigo) throws Exception {
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		String stTemp = "";
		
		stTemp = serventiaCargoNe.consultarServentiaCargosAgendaAudienciaJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, serventiaSubTipoCodigo);
		serventiaCargoNe = null;
		
		return stTemp;
	}
	
	/**
	 * Realiza o agendamento manual da Audi�ncia 
	 * @param audienciaProcessoDt
	 * @param obFabricaConexao
	 * @return um booleano indicando se o agendamento foi realizado com sucesso
	 * @throws Exception
	 */
	public boolean agendarAudienciaProcessoManual(AudienciaProcessoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
		boolean resultado;

		// SET CONEX�O
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());			
		
		resultado = obPersistencia.agendarAudienciaProcessoManual(audienciaProcessoDt);
		
		if (resultado) {
			LogDt logDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), audienciaProcessoDt.getId_UsuarioLog(), audienciaProcessoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), audienciaProcessoDt.getPropriedades());
			// COPIAR OBJETO DO TIPO "AUDIENCIAPROCESSODT" ATUALIZADO
			obDados.copiar(audienciaProcessoDt);

			// SALVAR LOG DA INSER��O OU ATUALIZA��O DO OBJETO DO TIPO "AUDIENCIAPROCESSODT"
			obLog.salvar(logDt, obFabricaConexao);	
		}
		
		return resultado;
		
	}
	
	/**
	 * Realiza o agendamento autom�tico da Audi�ncia para a pr�xima audi�ncia livre v�lida, n�o reservada, de acordo com o tipo da audi�ncia, 
     * menos o tipo "Sess�o de 2� Grau".      
	 * @param audienciaTipoCodigo
	 * @param id_Processo
	 * @param id_Serventia
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * @author M�rcio Gomes - 15/09/2010
	 */
	public boolean agendarAudienciaProcessoAutomatico(String audienciaTipoCodigo, String id_Processo, String id_Serventia, FabricaConexao obFabricaConexao) throws Exception {		
		
		// SET CONEX�O
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());			
		
		return obPersistencia.agendarAudienciaProcessoAutomatico(audienciaTipoCodigo, id_Processo, id_Serventia);		
		
    }    
    
	/**
	 * Realiza o agendamento autom�tico para a pr�xima audi�ncia livre v�lida, n�o reservada, de acordo com o tipo da audi�ncia, menos o tipo 
     * "Sess�o de 2� Grau", e o cargo da serventia (ServentiaCargo) do usu�rio para o qual o processo foi distribu�do 
	 * @param id_ServentiaCargo
	 * @param audienciaTipoCodigo
	 * @param id_Processo
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * @author M�rcio Gomes - 15/09/2010
	 */
    public boolean agendarAudienciaProcessoAutomaticoServentiaCargo(String id_ServentiaCargo, String audienciaTipoCodigo, String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
    	
		// SET CONEX�O
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());			
		
		return obPersistencia.agendarAudienciaProcessoAutomaticoServentiaCargo(id_ServentiaCargo, audienciaTipoCodigo, id_Processo);			
		
    }
    
    
	/**
	 * Realiza a altera��o de uma Audiencia Processo para sess�o de julgamento adiada, reutilizando a conex�o
	 * @param audienciaProcessoDt
	 * @param logDt
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * @author M�rcio Gomes - 09/09/2011
	 */
	public AudienciaProcessoDt alterarAudienciaProcessoJulgamentoAdiado(AudienciaProcessoDt audienciaProcessoDt, AudienciaDt audienciaNovaDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {			
		
		return alterarAudienciaProcessoJulgamentoIniciadoAdiado(audienciaProcessoDt, audienciaNovaDt, logDt, obFabricaConexao, false);
		
	}
	
	/**
	 * Realiza a altera��o de uma Audiencia Processo para sess�o de julgamento iniciado, reutilizando a conex�o
	 * @param audienciaProcessoDt
	 * @param logDt
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * @author M�rcio Gomes - 09/09/2011
	 */
	public AudienciaProcessoDt alterarAudienciaProcessoJulgamentoIniciado(AudienciaProcessoDt audienciaProcessoDt, AudienciaDt audienciaNovaDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
				
		return alterarAudienciaProcessoJulgamentoIniciadoAdiado(audienciaProcessoDt, audienciaNovaDt, logDt, obFabricaConexao, true);
		
	}
	
	/***
	 * Realiza a altera��o de uma Audiencia Processo para sess�o de julgamento iniciado ou adiada, reutilizando a conex�o
	 * 
	 * @param audienciaProcessoDt
	 * @param audienciaNovaDt
	 * @param logDt
	 * @param obFabricaConexao
	 * @param ehJulgamentoIniciado
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	private AudienciaProcessoDt alterarAudienciaProcessoJulgamentoIniciadoAdiado(AudienciaProcessoDt audienciaProcessoDt, AudienciaDt audienciaNovaDt, LogDt logDt, FabricaConexao obFabricaConexao, boolean ehJulgamentoIniciado) throws Exception{
		AudienciaProcessoDt audienciaProcessoDtNova = new AudienciaProcessoDt();
		
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());	
		
		//Guarda dados anteriores � altera��o para gerar log
		String valorAtual = audienciaProcessoDt.getPropriedades();
		
		//Copia todos os dados para o novo objeto			
		audienciaProcessoDtNova.copiar(audienciaProcessoDt);
		
		//Atualiza status para julgamento iniciado ou adiado
		audienciaProcessoDtNova.setId("");
		audienciaProcessoDtNova.setAudienciaDt(audienciaNovaDt);
		audienciaProcessoDtNova.setId_Processo(audienciaProcessoDt.getProcessoDt().getId());
		audienciaProcessoDtNova.setId_Audiencia(audienciaNovaDt.getId());

		// jvosantos - 22/11/2019 11:42 - Copia a flag de permitir sustenta��o oral da AUDI_PROC antiga para a nova.
		audienciaProcessoDtNova.setPermiteSustentacaoOral(audienciaProcessoDt.isPermiteSustentacaoOral());
		
		if (ehJulgamentoIniciado) audienciaProcessoDtNova.setJulgamentoIniciado();
		else audienciaProcessoDtNova.setJulgamentoAdiado();
		if(audienciaProcessoDtNova.getDataAudienciaOriginal().trim().length() == 0 && audienciaProcessoDt.getAudienciaDt() != null) audienciaProcessoDtNova.setDataAudienciaOriginal(audienciaProcessoDt.getAudienciaDt().getDataAgendada()); 
		if(audienciaProcessoDtNova.getId_Audi_Proc_Origem().trim().length() == 0 && audienciaProcessoDt.getAudienciaDt() != null) audienciaProcessoDtNova.setId_Audi_Proc_Origem(audienciaProcessoDt.getId());
		obPersistencia.inserir(audienciaProcessoDtNova);
		
		//Atualiza o status da audi�ncia			
		audienciaProcessoDt.setDataMovimentacao(Funcoes.DataHora(new Date()));
		if (ehJulgamentoIniciado) audienciaProcessoDt.setAudienciaProcessoStatusCodigo(String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_INICIADO));
		else audienciaProcessoDt.setAudienciaProcessoStatusCodigo(String.valueOf(AudienciaProcessoStatusDt.JULGAMENTO_ADIADO));
			
		//Captura dados novos do objeto
		String valorNovo = audienciaProcessoDt.getPropriedades();			
		
		LogDt obLogDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.MovimentacaoAudiencia), valorAtual, valorNovo);
		
		obPersistencia.alterarAudienciaProcessoMovimentacao(audienciaProcessoDt);		

		obLog.salvar(obLogDt, obFabricaConexao);
		
		return audienciaProcessoDtNova;		
	}
	
	/**
	 * Atualiza dados em "AudienciaProcesso" em virtude da movimenta��o.
	 * Quando uma audi�ncia � movimentada dever� armazenar na tabela "AudienciaProcesso" a data da movimenta��o,
	 * e o status da Audi�ncia.
	 * 
	 * @param audienciaProcessoDt
	 * @param id_UsuarioRealizador
	 * @param statusCodigo
	 * @param logDt
	 * @param obFabricaConexao
	 * @throws Exception
	 * @author mmgomes
	 */	
	public void alterarAudienciaProcessoMovimentacaoAnalistaSegundoGrau(AudienciaProcessoDt audienciaProcessoDt, String statusCodigo, String audienciaProcessoStatusAnalista, String audienciaProcessoStatusCodigoAnalista, String idArquivoAta, boolean ehMovimentacaoSessaoIniciada, boolean ehMovimentacaoSessaoAdiada, String Id_ServentiaCargoPresidente, String Id_ServentiaCargoMP, String Id_ServentiaCargoRedator, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		//Guarda dados anteriores � altera��o para gerar log
		String valorAtual = audienciaProcessoDt.getPropriedades();

		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());

		//Atualiza dados em AudienciaProcesso
		//Vincula os arquivos, dependendo do tipo da movimenta��o
		if (ehMovimentacaoSessaoIniciada){
			audienciaProcessoDt.setId_ArquivoAtaSessaoIniciada(idArquivoAta);
		} else if (ehMovimentacaoSessaoAdiada){
			audienciaProcessoDt.setId_ArquivoAtaSessaoAdiada(idArquivoAta);
		} else {
			audienciaProcessoDt.setAudienciaProcessoStatusCodigo(statusCodigo);
			audienciaProcessoDt.setAudienciaProcessoStatusAnalista(audienciaProcessoStatusAnalista);
			audienciaProcessoDt.setAudienciaProcessoStatusCodigoAnalista(audienciaProcessoStatusCodigoAnalista);	
			audienciaProcessoDt.setId_ArquivoAta(idArquivoAta);	
			audienciaProcessoDt.setId_ServentiaCargoPresidente(Id_ServentiaCargoPresidente);
			audienciaProcessoDt.setId_ServentiaCargoMP(Id_ServentiaCargoMP);			
			audienciaProcessoDt.setId_ServentiaCargoRedator(Id_ServentiaCargoRedator);			
		}			

		//Captura dados novos do objeto
		String valorNovo = audienciaProcessoDt.getPropriedades ();
		LogDt obLogDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.MovimentacaoAudiencia), valorAtual, valorNovo);

		obPersistencia.alterarAudienciaProcessoMovimentacao(audienciaProcessoDt);

		obLog.salvar(obLogDt, obFabricaConexao);

	}
	
	/**
	 * M�todo respons�vel em consultar todos os processos vinculados a uma audi�ncia passada, onde o julgamento ainda n�o foi realizado.
	 * Retorna uma lista de objetos "AudienciaProcessoDt" onde o status � "A Ser Realizada" com status iniciados e/ou adiados.
	 * 
	 * @param id_Audiencia, identifica��o da audi�ncia selecionada
	 * @return List listaSessoes
	 * 
	 * @author mmgomes
	 * @throws Exception 
	 */
	/*public List consultarAudienciaProcessosPendentesIniciadosAdiados(){
		List listaAudienciaProcesso = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());

			listaAudienciaProcesso = obPersistencia.consultarAudienciaProcessosIniciadosAdiados();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaAudienciaProcesso;
	}*/
	
	/**
	 * Consulta os dados de uma audi�ncia processo que ainda n�o foi movimentado
	 * 
	 * @param id_Processo, identifica��o do processo
	 * @param usuarioDt, usu�rio logado
	 * 
	 * @author mmgomes
	 */
	public AudienciaProcessoDt consultarAudienciaProcessoPendente(String id_Processo, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarAudienciaProcessoPendente(id_Processo, usuarioDt, obFabricaConexao);
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public AudienciaProcessoDt consultarAudienciaProcessoPendente(String id_Processo, UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoDt audienciaProcesso = null;
			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			
			switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {			
				case GrupoTipoDt.ASSISTENTE_GABINETE:
				case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
					audienciaProcesso = obPersistencia.consultarAudienciaProcessoPendenteAssistenteGabinete(id_Processo, usuarioDt.getId_ServentiaCargo());
					break;				

				default:				
					audienciaProcesso = obPersistencia.consultarAudienciaProcessoPendente(id_Processo);
					break;
			}
		return audienciaProcesso;
	}
	
	/**
	 * Retorna a sess�o do 2� grau marcada para o processo passado
	 * 
	 * @param id_Processo, identifica��o do processo	 
	 * @param id_ProcessoTipo, id processo tipo
	 * @author msapaula
	 */
	public AudienciaProcessoDt consultarSessaoProcesso(String id_Processo, String id_ProcessoTipo) throws Exception {
		AudienciaProcessoDt sessao = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
		
			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		
			sessao = obPersistencia.consultarSessaoMarcada(id_Processo, id_ProcessoTipo);
		}  finally{
			obFabricaConexao.fecharConexao();
		}
		return sessao;
	}
	
	/**
	 * Retorna as sess�es do 2� grau marcadas para o processo passado
	 * 
	 * @param id_Processo, identifica��o do processo	 
	 * @author mmgomes
	 */
	public List<AudienciaProcessoDt> consultarSessoesMarcadas(String id_Processo) throws Exception {
		List<AudienciaProcessoDt> sessoes = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
		
			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		
			sessoes = obPersistencia.consultarSessoesMarcadas(id_Processo);
		}  finally{
			obFabricaConexao.fecharConexao();
		}
		return sessoes;
	}
	
	/**
	 * Retorna as sess�es do 2� grau marcadas para o processo passado
	 * 
	 * @param id_Processo, identifica��o do processo	
	 * @param obFabricaConexao, conex�o com o banco de dados 
	 * @author mmgomes
	 */
	public List<AudienciaProcessoDt> consultarSessoesMarcadas(String id_Processo, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());		
		return obPersistencia.consultarSessoesMarcadas(id_Processo);		
	}
	
	/**
     * Consulta de serventias
     * 
     * @author mmgomes
     * @param descricao
     *            descricao da serventia
     * @param posicao
     *            posicionamento da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarDescricaoServentia(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
        ServentiaNe serventiaNe = new ServentiaNe();

        List lista = serventiaNe.consultarDescricaoServentiaAlterarResponsavelConclusao(descricao, usuarioDt, posicao);
        this.QuantidadePaginas = serventiaNe.getQuantidadePaginas();
        serventiaNe = null;
        return lista;
    }
	
	public String consultarDescricaoServentiaJSON(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		String stTemp ="";
		ServentiaNe neObjeto = new ServentiaNe();
    	stTemp = neObjeto.consultarDescricaoServentiaAlterarResponsavelConclusaoJSON(descricao, usuarioDt, posicao);
		return stTemp;
	}
    
    /**
     * Consultar os cargos pela descricao, somente os cargos da serventia do
     * usuario informado
     * 
     * @author mmgomes
     * @param idServentia
     *            id da serventia
     * @param descricao
     *            descricao da serventia
     * @param posicao
     *            posicao da paginacao
     * @return List
     * @throws Exception
     */
    public List consultarServentiaCargosGrupo(String idServentia, String grupoTipoCodigo, String descricao, String posicao) throws Exception {
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        
        List lista = serventiaCargoNe.consultarServentiaCargos(descricao, posicao, idServentia, grupoTipoCodigo);
        this.QuantidadePaginas = serventiaCargoNe.getQuantidadePaginas();
        
        serventiaCargoNe = null;
        return lista;
    }    

	public String consultarServentiaCargosGrupoJSON(String idServentia, String grupoTipoCodigo, String descricao, String posicao) throws Exception {
		String stTemp = "";
		
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe(); 
		stTemp = serventiaCargoNe.consultarServentiaCargosJSON(descricao, posicao, idServentia, grupoTipoCodigo);
		
		return stTemp;
	}
	

   /**
    * Verifica troca de respons�vel pela sess�o
    * 
    * @param serventiaCargoMagistradoDt
    * @param idServentiaCargoMagistradoAtual
    * @param serventiaCargoAssistenteDt
    * @param serventiaCargoAssistenteDistribuidorAtual
    * @param EhSemAssistente
    * @return
    */
	public String verificarTrocaResponsavelSessao(ServentiaCargoDt serventiaCargoMagistradoDt, String idServentiaCargoMagistradoAtual, ServentiaCargoDt serventiaCargoAssistenteDt, ServentiaCargoDt serventiaCargoAssistenteDistribuidorAtual, boolean EhSemAssistente) {
		String stRetorno = "";
		boolean mudouMagistrado = false;
		boolean mudouAssistente = false;
		
		if (serventiaCargoMagistradoDt == null || serventiaCargoMagistradoDt.getId() == null || serventiaCargoMagistradoDt.getId().trim().length() == 0) {
			stRetorno += "O novo cargo de magistrado deve ser informado.";
		}	
		if (!EhSemAssistente && (serventiaCargoAssistenteDt == null || serventiaCargoAssistenteDt.getId() == null || serventiaCargoAssistenteDt.getId().trim().length() == 0)) {
			stRetorno += "O novo cargo de assistente deve ser informado.";
		}	
		if (stRetorno.trim().length()==0)
		{
			mudouMagistrado = (!(idServentiaCargoMagistradoAtual.trim().equalsIgnoreCase(serventiaCargoMagistradoDt.getId().trim())));
			if (serventiaCargoAssistenteDistribuidorAtual == null) {
				mudouAssistente = true;
			} else if (EhSemAssistente) {
				mudouAssistente = (serventiaCargoAssistenteDistribuidorAtual.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE)));									
			} else {
				if (!serventiaCargoAssistenteDistribuidorAtual.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE)))
					mudouAssistente = true;
				else if (serventiaCargoAssistenteDistribuidorAtual.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE)) && !serventiaCargoAssistenteDistribuidorAtual.getId().equals(serventiaCargoAssistenteDt.getId()))
					mudouAssistente = true;
			}
			if (!mudouAssistente && !mudouMagistrado)
				stRetorno += "O novo cargo do Magistrado ou do Assistente deve ser diferente do atual.";			
		}		
		return stRetorno;
	}

	/**
	 * Altera o respons�vel pela sess�o de segundo grau...
	 * 
	 * @param serventiaCargoMagistradoDt
	 * @param audienciaProcessoDt
	 * @param usuarioDt
	 * @throws Exception
	 */
	public void salvarTrocaResponsavelSessao(ServentiaCargoDt serventiaCargoMagistradoDt, ServentiaCargoDt serventiaCargoAssistenteDt, AudienciaProcessoDt audienciaProcessoDt, UsuarioDt usuarioDt) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		String valorLog;
		String id_ServentiaCargoAtual = "";
				
		
		try{
			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());			
			id_ServentiaCargoAtual = audienciaProcessoDt.getId_ServentiaCargo();
			valorLog = audienciaProcessoDt.getPropriedades();
		    audienciaProcessoDt.setId_ServentiaCargo(serventiaCargoMagistradoDt.getId());
		    
		    serventiaCargoMagistradoDt = serventiaCargoNe.consultarId(serventiaCargoMagistradoDt.getId());
		    
		    obFabricaConexao.iniciarTransacao();
		    
			LogDt logDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorLog, audienciaProcessoDt.getPropriedades());				
			obPersistencia.alterarResponsavelAudiencia(audienciaProcessoDt.getId(), serventiaCargoMagistradoDt.getId());
			obLog.salvar(logDt, obFabricaConexao);
			
			// Caso n�o tenha assistente dever� buscar o distribuidor do gabinete				
            if (serventiaCargoAssistenteDt == null || serventiaCargoAssistenteDt.getId() == null || serventiaCargoAssistenteDt.getId().trim().equalsIgnoreCase(""))
            {
            	//Captura o Distribuidor do Gabinete do Desembargador e adiciona como respons�vel pela pend�ncia
            	serventiaCargoAssistenteDt = serventiaCargoNe.getDistribuidorGabinete(serventiaCargoMagistradoDt.getId_Serventia(), obFabricaConexao);
            	if(serventiaCargoAssistenteDt == null) throw new MensagemException("N�o foi poss�vel identificar o Cargo de Distribuidor do Gabinete da serventia " + serventiaCargoMagistradoDt.getId_Serventia() + ".");
            }
			
			//Consulta Voto 2� Grau 			
			PendenciaDt pendenciaDtVoto = null;
			List listaDePendenciasVoto = pendenciaNe.consultarPendenciasVotoEmentaProcesso(audienciaProcessoDt.getId_Processo(), id_ServentiaCargoAtual, String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), audienciaProcessoDt.getId(), obFabricaConexao);
			if (listaDePendenciasVoto != null && listaDePendenciasVoto.size() > 0) pendenciaDtVoto = (PendenciaDt) listaDePendenciasVoto.get(0);
			if (pendenciaDtVoto != null)
			{	
				// Alterar desembargador respons�vel pelo voto
				pendenciaResponsavelNe.alterarResponsavelPendencia(pendenciaDtVoto.getId(), id_ServentiaCargoAtual, serventiaCargoMagistradoDt.getId(), logDt, obFabricaConexao);
				
				List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendenciaDtVoto.getId(), null, obFabricaConexao);
				boolean alterouAssistenteDistribuidor = false;
				if (responsaveis != null){
            		Iterator iteratorResponsavel = responsaveis.iterator();
            		while (iteratorResponsavel.hasNext()) {
            			PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
            			if (dados != null && dados.getCargoTipoCodigo() != null && (dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE)) || dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.DISTRIBUIDOR_GABINETE)))){
            				// Alterar assistente/distribuidor respons�vel pelo voto            				
            				pendenciaResponsavelNe.alterarResponsavelPendencia(pendenciaDtVoto.getId(), dados.getId_ServentiaCargo(), serventiaCargoAssistenteDt.getId(), logDt, obFabricaConexao);
            				alterouAssistenteDistribuidor = true;
            			}
            		}
            	}
				if (!alterouAssistenteDistribuidor)		{
					PendenciaResponsavelDt pendenciaResponsavelVoto = new PendenciaResponsavelDt();
					pendenciaResponsavelVoto.setId_ServentiaCargo(serventiaCargoAssistenteDt.getId());
					pendenciaResponsavelVoto.setId_Pendencia(pendenciaDtVoto.getId());
					pendenciaResponsavelVoto.setId_UsuarioLog(usuarioDt.getId());
		            pendenciaResponsavelVoto.setIpComputadorLog(usuarioDt.getIpComputadorLog());
					pendenciaResponsavelNe.inserir(pendenciaResponsavelVoto, obFabricaConexao);
				}
				
				//Consulta Ementa 2� Grau 	
				PendenciaDt pendenciaDtEmenta = null;
				List listaDePendenciasEmenta = pendenciaNe.consultarPendenciasVotoEmentaProcesso(audienciaProcessoDt.getId_Processo(), id_ServentiaCargoAtual, String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), audienciaProcessoDt.getId(), obFabricaConexao);
				if (listaDePendenciasEmenta != null && listaDePendenciasEmenta.size() > 0) pendenciaDtEmenta = (PendenciaDt) listaDePendenciasEmenta.get(0);
				
				if (pendenciaDtEmenta != null)
				{
					// Alterar desembargador respons�vel pela ementa
					pendenciaResponsavelNe.alterarResponsavelPendencia(pendenciaDtEmenta.getId(), id_ServentiaCargoAtual, serventiaCargoMagistradoDt.getId(), logDt, obFabricaConexao);
					
					responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendenciaDtEmenta.getId(), null, obFabricaConexao);
					alterouAssistenteDistribuidor = false;
					if (responsaveis != null){
	            		Iterator iteratorResponsavel = responsaveis.iterator();
	            		while (iteratorResponsavel.hasNext()) {
	            			PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
	            			if (dados != null && dados.getCargoTipoCodigo() != null && (dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE)) || dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.DISTRIBUIDOR_GABINETE)))){
	            				// Alterar assistente/distribuidor respons�vel pela ementa
	        					pendenciaResponsavelNe.alterarResponsavelPendencia(pendenciaDtEmenta.getId(), dados.getId_ServentiaCargo(), serventiaCargoAssistenteDt.getId(), logDt, obFabricaConexao);
	            				alterouAssistenteDistribuidor = true;
	            			}
	            		}
	            	}
					if (!alterouAssistenteDistribuidor)	{
						PendenciaResponsavelDt pendenciaResponsavelEmenta = new PendenciaResponsavelDt();
						pendenciaResponsavelEmenta.setId_ServentiaCargo(serventiaCargoAssistenteDt.getId());
						pendenciaResponsavelEmenta.setId_Pendencia(pendenciaDtEmenta.getId());
						pendenciaResponsavelEmenta.setId_UsuarioLog(usuarioDt.getId());
						pendenciaResponsavelEmenta.setIpComputadorLog(usuarioDt.getIpComputadorLog());
						pendenciaResponsavelNe.inserir(pendenciaResponsavelEmenta, obFabricaConexao);
					}
				}	
			} else {
				// Gera pend�ncia de voto/ementa...			
				audienciaProcessoDt.setProcessoDt((new ProcessoNe()).consultarId(audienciaProcessoDt.getId_Processo()));
				PendenciaDt pendenciaDt = (new AudienciaNe()).gerarPendenciaVoto(serventiaCargoMagistradoDt.getId(), null, audienciaProcessoDt.getId_Processo(), audienciaProcessoDt.getProcessoDt().getId_ProcessoPrioridade(), false, pendenciaNe, usuarioDt, obFabricaConexao, serventiaCargoAssistenteDt, audienciaProcessoDt.getId());
				audienciaProcessoDt.setId_PendenciaVotoRelator(pendenciaDt.getId());
				this.vincularPendenciaVotoEmenta(audienciaProcessoDt, obFabricaConexao);
			}			
			
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {			
			obFabricaConexao.cancelarTransacao();
			audienciaProcessoDt.setId_ServentiaCargo(id_ServentiaCargoAtual);
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}		
	}	
	
	/**
	 * Consulta Serventia Cargo atrav�s do Id
	 * @param id_serventiacargo
	 * @return
	 * @throws Exception
	 */
	public ServentiaCargoDt consultarServentiaCargoId(String id_serventiacargo) throws Exception {
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        ServentiaCargoDt serventiaCargoDt = serventiaCargoNe.consultarId(id_serventiacargo);
        serventiaCargoNe = null;
        return serventiaCargoDt;
    }

	/**
	 *Consulta o assistente respons�vel pelo voto da sess�o.
	 * 
	 * @param idProcesso
	 * @param idServentiaCargo
	 * @return
	 * @throws Exception
	 */
	public PendenciaResponsavelDt consultarAssistenteDistribuidorResponsavelVotoProcesso(String idProcesso, String idServentiaCargo, String idAudienciaProcessoSessaoSegundoGrau) throws Exception {
		PendenciaDt pendenciaDtVoto = null;
		PendenciaResponsavelDt pendenciaResponsavelDt = null;
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try 
		{
			List listaDePendenciasVoto = pendenciaNe.consultarPendenciasVotoEmentaProcesso(idProcesso, idServentiaCargo, String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), idAudienciaProcessoSessaoSegundoGrau, obFabricaConexao);
			if (listaDePendenciasVoto != null && listaDePendenciasVoto.size() > 0) pendenciaDtVoto = (PendenciaDt) listaDePendenciasVoto.get(0);
			
			if (pendenciaDtVoto != null)
			{	
				List responsaveis = pendenciaResponsavelNe.consultarResponsaveisDetalhado(pendenciaDtVoto.getId(), null, obFabricaConexao);				
				if (responsaveis != null){
            		Iterator iteratorResponsavel = responsaveis.iterator();
            		while (iteratorResponsavel.hasNext()) {
            			PendenciaResponsavelDt dados = (PendenciaResponsavelDt) iteratorResponsavel.next();
            			if (dados != null && dados.getCargoTipoCodigo() != null && (dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE)) || dados.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.DISTRIBUIDOR_GABINETE)))){
            				pendenciaResponsavelDt = dados;
            			}
            		}
            	}
			}
			
		} finally{
			obFabricaConexao.fecharConexao();
			pendenciaNe = null;
			pendenciaResponsavelNe = null;
		}
		
		return pendenciaResponsavelDt;
	}
	
	public void excluirAudienciaProcessoLivre(String[] id, FabricaConexao obFabricaConexao, String id_Usuario, String ip_Computador) throws Exception {
		    
	    AudienciaProcessoPs obPersistencia = null;
	    LogDt obLogDt = null;
		obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		obPersistencia.excluir(id);
			
		for(int i=0; i< id.length; i++) {
			obLogDt = new LogDt("AudienciaProcesso", id[i], id_Usuario, ip_Computador, String.valueOf(LogTipoDt.Excluir), "", "");				
		}		
		obLog.salvar(obLogDt, obFabricaConexao);
		
	}
	
	/**
	 * Retorna os dados completos de uma AudienciaProcesso, j� setando os dados da audi�ncia vinculada
	 * 
	 * @param id_PendenciaVoto, identifica��o da pendencia voto
	 * @author mmgomes
	 */
	public AudienciaProcessoDt consultarCompletoPelaPendenciaDeVoto(String id_PendenciaVoto) throws Exception {
		AudienciaProcessoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarCompletoPelaPendenciaDeVoto(id_PendenciaVoto);			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna os dados completos de uma AudienciaProcesso, j� setando os dados da audi�ncia vinculada
	 * 
	 * @param id_PendenciaEmenta, identifica��o da pendencia ementa
	 * @author mmgomes
	 */
	public AudienciaProcessoDt consultarCompletoPelaPendenciaDeEmenta(String id_PendenciaEmenta) throws Exception {
		AudienciaProcessoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarCompletoPelaPendenciaDeEmenta(id_PendenciaEmenta);			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public void vincularPendenciaVotoEmenta(AudienciaProcessoDt audienciaProcessoDt,
			                                FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
		if (audienciaProcessoDt.possuiVotoOuEmentaRelator() || audienciaProcessoDt.possuiVotoOuEmentaRedator()) {
			obPersistencia.vincularPendenciaVotoEmenta(audienciaProcessoDt);	
		}		
	}
	
	public void limpaVinculoPendenciaVoto(AudienciaProcessoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());

		obPersistencia.limpaVinculoPendenciaVoto(audienciaProcessoDt);
	}
	
	public void limpaVinculoPendenciaEmenta(AudienciaProcessoDt audienciaProcessoDt, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());

		obPersistencia.limpaVinculoPendenciaEmenta(audienciaProcessoDt);
	}
	
	/**
	 * M�todo utilizado para atualizar a sess�o de segundo grau.
	 * @param audienciaProcessoDt
	 * @param novo_Id_ProcessoTipo
	 * @param logDt
	 * @param obFabricaConexao
	 * @throws Exception
	 */
	public void alterarProcessoTipoSessaoSegundoSegundoGrau(AudienciaProcessoDt audienciaProcessoDt, String novo_Id_ProcessoTipo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoPs obPersistencia = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
		
		//Guarda dados anteriores � altera��o para gerar log
		String valorAtual = audienciaProcessoDt.getPropriedades();		
		
		audienciaProcessoDt.setId_ProcessoTipo(novo_Id_ProcessoTipo);

		//Captura dados novos do objeto
		String valorNovo = audienciaProcessoDt.getPropriedades ();
		LogDt obLogDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);

		obPersistencia.atualizaIdProcessoTipoSessaoSegundoGrau(audienciaProcessoDt);

		obLog.salvar(obLogDt, obFabricaConexao);

	}
	
	/**
	 * Retirar o extrato da ata de julgamento.
	 * 
	 * @param usuarioDt
	 * @param audienciaDtCompleta
	 * @param movimentacaoProcessodt
	 * @param obFabricaConexao
	 * @return 
	 * @throws Exception
	 */
	public MovimentacaoDt retirarExtratoAtaJulgamentoOuAcordaoOuRetornar(UsuarioDt usuarioDt, AudienciaDt audienciaDtCompleta, MovimentacaoProcessoDt movimentacaoProcessodt, FabricaConexao obFabricaConexao) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		AudienciaNe audienciaNe = new AudienciaNe();
		
		AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
		
		AudienciaProcessoDt audienciaProcessoDt = audienciaDtCompleta.getAudienciaProcessoDt();

		//Guarda dados anteriores � altera��o para gerar log
		String valorAtual = audienciaProcessoDt.getPropriedades();	
		
		int statusOriginal = Funcoes.StringToInt(audienciaProcessoDt.getAudienciaProcessoStatusCodigo()); 
				
		if (statusOriginal != AudienciaProcessoStatusDt.A_SER_REALIZADA) {
			
			AudienciaProcessoDt audienciaProcessoPendente = this.consultarAudienciaPendenteProcesso(audienciaProcessoDt.getProcessoDt().getId(), null, audienciaProcessoDt.getId_ProcessoTipo(), obFabricaConexao);
			if (audienciaProcessoPendente != null && !audienciaProcessoPendente.getId().equalsIgnoreCase(audienciaProcessoDt.getId())) {
				
				String mensagem = "Imposs�vel executar essa a��o, pois j� existe uma sess�o de julgamento com os par�metros informados";
				
				AudienciaDt audienciaDt = new AudienciaNe().consultarId(audienciaProcessoPendente.getId_Audiencia());
				if (audienciaDt != null) {
					mensagem += " na sess�o de julgamento no dia " + audienciaDt.getDataAgendada();
					if (audienciaProcessoPendente.isJulgamentoEmMesaParaJulgamento())
						mensagem += " em mesa para julgamento";
					else if (audienciaProcessoPendente.isJulgamentoAdiado())
						mensagem += " em Julgamentos Adiados";
					else
						mensagem += " na Pauta do Dia";
				}
				
				throw new MensagemException(mensagem + ". Favor desmarcar ou retirar o processo dessa sess�o para concluir a a��o.");
			}
			
			AudienciaProcessoStatusDt audienciaProcessoStatusDt = new AudienciaProcessoStatusNe().consultarAudienciaProcessoStatusCodigo(AudienciaProcessoStatusDt.A_SER_REALIZADA);
			
			audienciaProcessoDt.setAudienciaProcessoStatusCodigo(String.valueOf(AudienciaProcessoStatusDt.A_SER_REALIZADA));
			audienciaProcessoDt.setId_AudienciaProcessoStatus(audienciaProcessoStatusDt.getId());
			audienciaProcessoDt.setAudienciaProcessoStatus(audienciaProcessoStatusDt.getAudienciaProcessoStatus());
			
			if (audienciaProcessoDt.possuiVotoRelator()) {
				PendenciaArquivoDt pendenciaArquivoDtVotoRelatorSessao = audienciaNe.consultarVotoDesembargadorPendencia(audienciaProcessoDt.getId_ServentiaCargo(),
						                                                                                                 audienciaProcessoDt.getId(), 
                                                                                                                         audienciaProcessoDt.getId_PendenciaVotoRelator(),
                                                                                                                         obFabricaConexao);
				if (pendenciaArquivoDtVotoRelatorSessao == null) {
					PendenciaDt pendenciaVotoDtDescartada = pendenciaNe.consultarFinalizadaId(audienciaProcessoDt.getId_PendenciaVotoRelator(), obFabricaConexao);
					if (pendenciaVotoDtDescartada != null) {
						pendenciaNe.reaberturaAutomaticaPendencia(pendenciaVotoDtDescartada, usuarioDt, obFabricaConexao);
						pendenciaArquivoDtVotoRelatorSessao = audienciaNe.consultarVotoDesembargadorPendencia(audienciaProcessoDt.getId_ServentiaCargo(), 
																										      audienciaProcessoDt.getId(), 
																										      audienciaProcessoDt.getId_PendenciaVotoRelator(),
																										      obFabricaConexao);
					}
				}
				
				if (pendenciaArquivoDtVotoRelatorSessao == null) {
					// Gerar pend�ncia de voto para o desembargador inserir o voto, caso n�o exista...
			    	PendenciaDt pendenciaDtVotoRelatorSessao = audienciaNe.gerarPendenciaVotoRelatorSessao(audienciaProcessoDt.getId_ServentiaCargo(),
			    			                                                                               movimentacaoProcessodt.getId(), 
																			                               audienciaProcessoDt.getProcessoDt().getId(), 
																			                               audienciaProcessoDt.getProcessoDt().getId_ProcessoPrioridade(), 
																			                               null,
																			                               audienciaProcessoDt.getId(),
																			                               false, 
																			                               usuarioDt, 
																			                               obFabricaConexao);
			    	
			    	if (pendenciaDtVotoRelatorSessao != null) {
			    		audienciaProcessoDt.setId_PendenciaVotoRelator(pendenciaDtVotoRelatorSessao.getId());
			    	}
				}
			}
	    	
	    	if (audienciaProcessoDt.possuiEmentaRelator()) {
	    		PendenciaArquivoDt pendenciaArquivoDtEmenta = audienciaNe.consultarEmentaDesembargadorEmentaPendencia(audienciaProcessoDt.getId_ServentiaCargo(), 
	    			 	                                                                                              audienciaProcessoDt.getId(), 
	    				                                                                                              audienciaProcessoDt.getId_PendenciaEmentaRelator(),
	    				                                                                                              obFabricaConexao);
			
				if (pendenciaArquivoDtEmenta == null) {
					PendenciaDt pendenciaEmentaDtDescartada = pendenciaNe.consultarFinalizadaId(audienciaProcessoDt.getId_PendenciaEmentaRelator(), 
							                                                                    obFabricaConexao);
					if (pendenciaEmentaDtDescartada != null) {
						pendenciaNe.reaberturaAutomaticaPendencia(pendenciaEmentaDtDescartada, usuarioDt, obFabricaConexao);
						pendenciaArquivoDtEmenta = audienciaNe.consultarEmentaDesembargadorEmentaPendencia(audienciaProcessoDt.getId_ServentiaCargo(), 
																										   audienciaProcessoDt.getId(), 
																										   audienciaProcessoDt.getId_PendenciaEmentaRelator(),
																										   obFabricaConexao);
					}
				}	
				
				if (pendenciaArquivoDtEmenta == null) {
					audienciaProcessoDt.setId_PendenciaEmentaRelator("null");
					obPersistencia.limpaVinculoPendenciaEmenta(audienciaProcessoDt);
				}
	    	}
	    	
	    	if (audienciaProcessoDt.possuiVotoRedator()) {
				PendenciaDt pendenciaDtVotoRedator = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaVotoRedator(), obFabricaConexao);
				if (pendenciaDtVotoRedator != null) {
					pendenciaNe.descartarPendencia(pendenciaDtVotoRedator, usuarioDt, obFabricaConexao);
				}
			}
			
			if (audienciaProcessoDt.possuiEmentaRedator()) {
				PendenciaDt pendenciaDtEmentaRedator = pendenciaNe.consultarId(audienciaProcessoDt.getId_PendenciaEmentaRedator(), obFabricaConexao);
				if (pendenciaDtEmentaRedator != null) {
					pendenciaNe.descartarPendencia(pendenciaDtEmentaRedator, usuarioDt, obFabricaConexao);
				}
			}
			
			audienciaProcessoDt.setDataMovimentacao("");			
		}
		
		audienciaProcessoDt.setId_AudienciaProcessoStatusAnalista("");
		audienciaProcessoDt.setAudienciaProcessoStatusCodigoAnalista("");
		audienciaProcessoDt.setAudienciaProcessoStatusAnalista("");
		audienciaProcessoDt.setId_ArquivoAta("");
		audienciaProcessoDt.setId_ServentiaCargoPresidente("");
		audienciaProcessoDt.setId_ServentiaCargoMP("");
		audienciaProcessoDt.setId_ServentiaCargoRedator("");
		
		//Captura dados novos do objeto
		String valorNovo = audienciaProcessoDt.getPropriedades();
		LogDt obLogDt = new LogDt("AudienciaProcesso", audienciaProcessoDt.getId(), usuarioDt.getId(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.MovimentacaoAudiencia), valorAtual, valorNovo);

		obPersistencia.alterarAudienciaProcessoMovimentacao(audienciaProcessoDt);
		
		if (audienciaProcessoDt.possuiVotoOuEmentaRelator() || audienciaProcessoDt.possuiVotoOuEmentaRedator()) {
			obPersistencia.vincularPendenciaVotoEmenta(audienciaProcessoDt);	
		}
		
		obLog.salvar(obLogDt, obFabricaConexao);
		
		// Gera movimenta��o no processo...
		String complemento = "(Sess�o do dia " + audienciaDtCompleta.getDataAgendada() + ")";
		if (movimentacaoProcessodt.getComplemento() != null && movimentacaoProcessodt.getComplemento().trim().length() > 0)
			complemento += " - " + movimentacaoProcessodt.getComplemento().trim();
		
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();	
		MovimentacaoDt movimentacaoDt = null;
		List arquivos = movimentacaoProcessodt.getListaArquivos();
		if (statusOriginal == AudienciaProcessoStatusDt.DESMARCAR_PAUTA ||
			statusOriginal == AudienciaProcessoStatusDt.RETIRAR_PAUTA ||
			statusOriginal == AudienciaProcessoStatusDt.JULGAMENTO_ADIADO ||
			statusOriginal == AudienciaProcessoStatusDt.JULGAMENTO_INICIADO) {
			movimentacaoDt = movimentacaoNe.gerarMovimentacaoSessaoRetornarProcessoSessaoDeJulgamento(audienciaProcessoDt.getProcessoDt().getId(), usuarioDt.getId_UsuarioServentia(), complemento, obLogDt, obFabricaConexao);
		} else if (statusOriginal != AudienciaProcessoStatusDt.A_SER_REALIZADA) {
			movimentacaoDt = movimentacaoNe.gerarMovimentacaoSessaoRetirarAcordaoEmentaExtratoAta(audienciaProcessoDt.getProcessoDt().getId(), usuarioDt.getId_UsuarioServentia(), complemento, obLogDt, obFabricaConexao);
		} else {
			movimentacaoDt = movimentacaoNe.gerarMovimentacaoSessaoRetirarExtratoAta(audienciaProcessoDt.getProcessoDt().getId(), usuarioDt.getId_UsuarioServentia(), complemento, obLogDt, obFabricaConexao);
		}	
		
		// Salvando v�nculo entre movimenta��o e arquivos inseridos
		if (arquivos != null) {
			MovimentacaoArquivoNe movimentacaoArquivoNe = new MovimentacaoArquivoNe();
			
			movimentacaoArquivoNe.inserirArquivos(arquivos, obLogDt, obFabricaConexao);	
			
			movimentacaoArquivoNe.vinculaMovimentacaoArquivoControleAcesso(arquivos, movimentacaoDt.getId(), null, obLogDt, obFabricaConexao);
			
			List movimentacoes = new ArrayList();
			movimentacoes.add(movimentacaoDt);
//			movimentacaoArquivoNe.gerarReciboArquivoMovimentacao(arquivos, movimentacoes, obFabricaConexao);
		}
		
		return movimentacaoDt;
	}
	
	public AudienciaProcessoDt consultarId(String id_audienciaprocesso, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoDt dtRetorno=null;
		AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_audienciaprocesso ); 
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}
	
	// jvosantos - 06/01/2020 17:12 - Tipar lista
	public List<VotoSessaoLocalizarDt> consultarEmVotacaoSessaoVirtual(String idServentiaCargo, String processoNumero) throws Exception {
		List<VotoSessaoLocalizarDt> lisRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			lisRetorno = obPersistencia.consultarEmVotacao(idServentiaCargo, processoNumero);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lisRetorno;
	}
	
	public long consultarQuantidadeEmVotacaoSessaoVirtual(String idServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarQuantidadeEmVotacaoVirtual(idServentiaCargo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Atualiza o status de uma audi�ncia
	 * 
	 * @param audienciaProcessoDt, dt com dados da audi�ncia que ter� status modificado
	 * @param novoStatus, novo status da audi�ncia
	 * @param logDt, dados do log
	 * @param obFabricaConexao, conex�o ativa
	 * @author msapaula
	 */
	public void alterarStatusAudienciaTemp(String  idAudienciaProcesso, String status) throws Exception {
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao(); // jvosantos - 02/09/2019 15:05 - Usar transa��o
			alterarStatusAudienciaTemp(idAudienciaProcesso, status, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public AudienciaProcessoStatusDt consultarStatusAudienciaTemp(String idAudienciaProcesso) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			return consultarStatusAudienciaTemp(idAudienciaProcesso, obFabricaConexao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	

	
	public AudienciaProcessoStatusDt consultarStatusAudienciaTemp(String idAudienciaProcesso, FabricaConexao fabrica) throws Exception {
		AudienciaProcessoPs ps = new  AudienciaProcessoPs(fabrica.getConexao());
		return ps.consultarStatusAudienciaTemp(idAudienciaProcesso);
	}
	
	public void alterarPedidoSustentacaoOral(String id_AudienciaProcesso, boolean pedidoSustentacaoOral, FabricaConexao obFabricaConexao) throws Exception {		
			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			ps.alterarPedidoSustentacaoOral(id_AudienciaProcesso, pedidoSustentacaoOral);		
	}
	
	public boolean consultarPodeSustentacaoOral(String  idAudienciaProcesso) throws Exception {
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			return ps.consultarPodeSustentacaoOral(idAudienciaProcesso);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public void alterarStatusAudienciaTemp(String idAudienciaProcesso, String audienciaProcessoStatusCodigoTemp,
			FabricaConexao fabrica) throws Exception {
		AudienciaProcessoPs ps = new  AudienciaProcessoPs(fabrica.getConexao());
		ps.alterarStatusAudienciaTemp(idAudienciaProcesso, audienciaProcessoStatusCodigoTemp);
		
	}
	
	public boolean verificarVotacaoIniciada(String idAudienciaProcesso) throws Exception {
		return verificarVotacaoIniciada(idAudienciaProcesso, null);
	}
	
	public boolean verificarVotacaoIniciada(String idAudienciaProcesso, FabricaConexao fabConexao) throws Exception {
		FabricaConexao obFabricaConexao = fabConexao;
		
		try {
			if(fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			return ps.verificarVotacaoIniciada(idAudienciaProcesso);
		} finally {
			if(fabConexao == null) obFabricaConexao.fecharConexao();
		}
	}
	
	// jvosantos - 04/06/2019 10:00 - M�todo que altera a pendencia de voto e ementa de uma audi_proc
	public void alterarPendenciaVotoEmenta(String id, String idVoto, String idEmenta, FabricaConexao fabrica) throws Exception {
		FabricaConexao obFabricaConexao = fabrica;
		
		try {
			if(fabrica == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			
			ps.alterarPendenciaVotoEmenta(id, idVoto, idEmenta);
		} finally {
			if(fabrica == null) obFabricaConexao.fecharConexao();
		}
		
	}

	// jvosantos - 04/06/2019 10:01 - Cria��o de um m�todo salvar que permite a passagem de conex�o
	public void salvar(AudienciaProcessoDt dados, FabricaConexao fabrica) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = (fabrica == null) ? new FabricaConexao(FabricaConexao.PERSISTENCIA) : fabrica;
		//////System.out.println("..neAudienciaProcessosalvar()");
		try{
			if(fabrica == null) obFabricaConexao.iniciarTransacao();
			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			/* use o id do objeto para saber se os dados ja est�o ou n�o salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("AudienciaProcesso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("AudienciaProcesso", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			if(fabrica == null) obFabricaConexao.finalizarTransacao();
		
		}finally{
			if(fabrica == null) obFabricaConexao.fecharConexao();
		}
	}
	
	// jvosantos - 04/06/2019 13:57 - Verifica se existe pendencia de apreciados ou proclama��o
	public boolean verificarApreciadosOuProclamacao(String id) throws Exception {
		return verificarApreciadosOuProclamacao(id, null);
	}
	
	public boolean verificarApreciadosOuProclamacao(String idAudienciaProcesso, FabricaConexao fabConexao) throws Exception {
		FabricaConexao obFabricaConexao = fabConexao;
		
		try {
			if(fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			return ps.verificarApreciadosOuProclamacao(idAudienciaProcesso);
		} finally {
			if(fabConexao == null) obFabricaConexao.fecharConexao();
		}
	}
	
	public void alterarAudienciaProcessoVotantes(String id_AudienciaProcesso, List<AudienciaProcessoVotantesDt> listaAudiProcVotantes, FabricaConexao fabConexao) throws Exception {
		FabricaConexao obFabricaConexao = fabConexao;
		
		try {		
			if(fabConexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			}
			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			AudienciaProcessoDt audiProcDt = consultarIdCompleto(id_AudienciaProcesso, obFabricaConexao);
			ps.excluirVotantesSessaoVirtual(id_AudienciaProcesso);
			ps.cadastrarVotantesSessaoVirtual(listaAudiProcVotantes);
			if(fabConexao == null) obFabricaConexao.finalizarTransacao();		
		}catch(Exception e){
			if(fabConexao == null) obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			if(fabConexao == null) obFabricaConexao.fecharConexao();
		}
		
		
	}
	
	//mrbatista - 16/10/2019 15:19 refatora��o da altera��o dos votantes para que possa ser alterado o relator.
	public void alterarAudienciaProcessoVotantesRelator(String id_AudienciaProcesso, List<AudienciaProcessoVotantesDt> listaAudiProcVotantes, String id_UsuarioLog ,FabricaConexao fabConexao) throws Exception {
		FabricaConexao obFabricaConexao = fabConexao;
		if(fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		try {		
			if(fabConexao == null) obFabricaConexao.iniciarTransacao();
			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			AudienciaProcessoDt audiProcDt = consultarIdCompleto(id_AudienciaProcesso, obFabricaConexao);
			PendenciaNe pendenciaNe = new PendenciaNe();
			PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
			List <PendenciaDt> listaPendenciaSO = pendenciaNe.consultarPendenciasAudienciaProcessoPorListaTipo(id_AudienciaProcesso, obFabricaConexao, PendenciaTipoDt.VERIFICAR_PEDIDO_SUSTENTACAO_ORAL);
			ServentiaCargoDt novoRelator = listaAudiProcVotantes.stream().filter(x -> x.isRelator()).map(x -> {
				try {
					return new ServentiaCargoNe().consultarId(x.getId_ServentiaCargo());
				} catch (Exception e) {
					return null;
				}
				
			}).findAny().orElse(null);
			ServentiaCargoDt antigoRelatorDt = new ServentiaCargoNe().consultarId(audiProcDt.getId_ServentiaCargo());
			if (antigoRelatorDt.getId_Serventia().equals(novoRelator.getId_Serventia())) {
				if (!audiProcDt.getId_ServentiaCargo().equals(novoRelator.getId())) {
					audiProcDt.setId_ServentiaCargo(novoRelator.getId());
					audiProcDt.setId_UsuarioLog(id_UsuarioLog);
					salvar(audiProcDt, obFabricaConexao);
					PendenciaResponsavelDt responsavelVotoMesmaServentia = (PendenciaResponsavelDt) Optional.ofNullable(pendenciaResponsavelNe.consultarResponsaveis(audiProcDt.getId_PendenciaVotoRelator(), obFabricaConexao)).orElseThrow(()-> new MensagemException("Processo se encontra sem respons�vel.")).stream().filter(x -> ((PendenciaResponsavelDt) x).getId_ServentiaCargo().equals(antigoRelatorDt.getId())).findFirst().orElse(null);
					if (responsavelVotoMesmaServentia != null) {
						responsavelVotoMesmaServentia.setId_UsuarioLog(id_UsuarioLog);
						pendenciaResponsavelNe.excluir(responsavelVotoMesmaServentia, obFabricaConexao);
					}
					pendenciaResponsavelNe.trocarResponsavelPendenciaDesembargadorMesmaServentia(obFabricaConexao,  audiProcDt.getId_PendenciaVotoRelator(), novoRelator, id_UsuarioLog);
					
					if(StringUtils.isNotEmpty(audiProcDt.getId_PendenciaEmentaRelator())) {
						PendenciaResponsavelDt responsavelEmenta = (PendenciaResponsavelDt) pendenciaResponsavelNe.consultarResponsaveis(audiProcDt.getId_PendenciaEmentaRelator(), obFabricaConexao).stream().filter(x -> ((PendenciaResponsavelDt) x).getId_ServentiaCargo().equals(antigoRelatorDt.getId())).findFirst().orElse(null);
						if (responsavelEmenta != null) {
							responsavelEmenta.setId_UsuarioLog(id_UsuarioLog);
							pendenciaResponsavelNe.excluir(responsavelEmenta, obFabricaConexao);
						}
						pendenciaResponsavelNe.trocarResponsavelPendenciaDesembargadorMesmaServentia(obFabricaConexao,  audiProcDt.getId_PendenciaEmentaRelator(), novoRelator, id_UsuarioLog);
					}
				}
			} else {
				if (!audiProcDt.getId_ServentiaCargo().equals(novoRelator.getId())) {
					String antigoRelator = audiProcDt.getId_ServentiaCargo();
					audiProcDt.setId_ServentiaCargo(novoRelator.getId());
					audiProcDt.setId_UsuarioLog(id_UsuarioLog);
					salvar(audiProcDt, obFabricaConexao);
					
					
					PendenciaResponsavelDt responsavelVoto = (PendenciaResponsavelDt) Optional.ofNullable(pendenciaResponsavelNe.consultarResponsaveis(audiProcDt.getId_PendenciaVotoRelator(), obFabricaConexao)).orElseThrow(()-> new MensagemException("Processo se encontra sem respons�vel.")).stream().filter(x -> ((PendenciaResponsavelDt) x).getId_ServentiaCargo().equals(antigoRelator)).findFirst().orElse(null);
					List<PendenciaResponsavelDt> listResponsavelVoto =  pendenciaResponsavelNe.consultarResponsaveis(audiProcDt.getId_PendenciaVotoRelator(), obFabricaConexao);
					 if (listResponsavelVoto != null && listResponsavelVoto.size() > 0) { 
						for (PendenciaResponsavelDt dados : listResponsavelVoto){
							if (dados != null) {
								dados.setId_UsuarioLog(id_UsuarioLog);
								pendenciaResponsavelNe.excluir(dados, obFabricaConexao);
							}
						}
					 }
					responsavelVoto.setId_UsuarioLog(id_UsuarioLog);
					pendenciaResponsavelNe.trocarResponsavelPendenciaDesembargador(obFabricaConexao, audiProcDt.getId_PendenciaVotoRelator(), novoRelator, id_UsuarioLog);
					if(StringUtils.isNotEmpty(audiProcDt.getId_PendenciaEmentaRelator())) {
						PendenciaResponsavelDt responsavelEmenta = (PendenciaResponsavelDt) pendenciaResponsavelNe.consultarResponsaveis(audiProcDt.getId_PendenciaEmentaRelator(), obFabricaConexao).stream().filter(x -> ((PendenciaResponsavelDt) x).getId_ServentiaCargo().equals(antigoRelator)).findFirst().orElse(null);
						 List<PendenciaResponsavelDt> listResponsavelEmenta =  pendenciaResponsavelNe.consultarResponsaveis(audiProcDt.getId_PendenciaEmentaRelator(), obFabricaConexao);
						 if (listResponsavelEmenta != null && listResponsavelEmenta.size() > 0) { 
							for (PendenciaResponsavelDt dados : listResponsavelEmenta){
								if (dados != null) {
									dados.setId_UsuarioLog(id_UsuarioLog);
									pendenciaResponsavelNe.excluir(dados, obFabricaConexao);
								}
							}
						 }
						responsavelEmenta.setId_UsuarioLog(id_UsuarioLog);
						pendenciaResponsavelNe.trocarResponsavelPendenciaDesembargador(obFabricaConexao,  audiProcDt.getId_PendenciaEmentaRelator(), novoRelator, id_UsuarioLog);
					}
					if(listaPendenciaSO.size() > 0) {
						for (PendenciaDt pendSO : listaPendenciaSO) {
							 
							PendenciaResponsavelDt responsavelSO = (PendenciaResponsavelDt) pendenciaResponsavelNe.consultarResponsaveis(pendSO.getId(), obFabricaConexao).stream().filter(x -> ((PendenciaResponsavelDt) x).getId_ServentiaCargo().equals(antigoRelator)).findFirst().orElse(null);
							if(responsavelSO == null) continue;
							responsavelSO.setId_UsuarioLog(id_UsuarioLog);
							pendenciaResponsavelNe.trocarResponsavelPendencia(obFabricaConexao,  pendSO.getId(), novoRelator.getId(), responsavelSO);
							
						}
					};
					
				}
			}
			
			ps.excluirVotantesSessaoVirtual(id_AudienciaProcesso);
			ps.cadastrarVotantesSessaoVirtual(listaAudiProcVotantes);
			if(fabConexao == null) obFabricaConexao.finalizarTransacao();		
		}catch(Exception e){
			if(fabConexao == null) obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			if(fabConexao == null) obFabricaConexao.fecharConexao();
		}
		
	}

	public String consultarAudienciaProcessoDoProcesso(String idProcesso) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {		
			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			return ps.consultarAudienciaProcessoDoProcesso(idProcesso);	
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


	public String consultarAudienciaProcessoDoProcessoComStatus(String idProcesso, int audienciaProcessoStatus) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {		
			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			return ps.consultarAudienciaProcessoDoProcesso(idProcesso, audienciaProcessoStatus);	
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	// jvosantos - 14/06/2019 12:15 - Adicionar m�todo para consultar os ID_SERV_CARGO dos votantes de uma AUDI_PROC
	public List<String> consultarIdsVotantesAudienciaProcesso(String id_AudienciaProcesso) throws Exception {
		return consultarIdsVotantesAudienciaProcesso(id_AudienciaProcesso, null);
	}

	// jvosantos - 14/06/2019 12:15 - Adicionar m�todo para consultar os ID_SERV_CARGO dos votantes de uma AUDI_PROC
	public List<String> consultarIdsVotantesAudienciaProcesso(String id_AudienciaProcesso, FabricaConexao fabConexao) throws Exception {
		FabricaConexao obFabricaConexao = fabConexao;
		
		try {		
			if(fabConexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			AudienciaProcessoPs ps = new  AudienciaProcessoPs(obFabricaConexao.getConexao());
			
			return ps.consultarIdsVotantesAudienciaProcesso(id_AudienciaProcesso, obFabricaConexao);
		}catch(Exception e){
			if(fabConexao == null) obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			if(fabConexao == null) obFabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 04/07/2019 14:59 - Adicionar m�todo para consultar quantidade de sess�es abertas em que o usuario j� votou
	public long consultarQuantidadeAcompanharVotacaoSessaoVirtual(String idServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarQuantidadeAcompanharVotacaoSessaoVirtual(idServentiaCargo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public long consultarQuantidadeAcompanharVotacaoSessaoVirtualErroMaterial(String idServentiaCargo) throws Exception {
			FabricaConexao obFabricaConexao = null;
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
				return obPersistencia.consultarQuantidadeAcompanharVotacaoSessaoVirtualErroMaterial(idServentiaCargo);
			} finally {
				obFabricaConexao.fecharConexao();
			}
	}	

	public List<VotoSessaoLocalizarDt> consultarAcompanharVotacaoSessaoVirtual(String idServentiaCargo,
			String processoNumero) throws Exception {
		List<VotoSessaoLocalizarDt> lisRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			lisRetorno = obPersistencia.consultarAcompanharVotacao(idServentiaCargo, processoNumero);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return lisRetorno;
	}
	
	public List<VotoSessaoLocalizarDt> consultarAcompanharVotacaoSessaoVirtualErroMaterial(String idServentiaCargo,
				String processoNumero) throws Exception {
			List<VotoSessaoLocalizarDt> lisRetorno = null;
			FabricaConexao obFabricaConexao = null;
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
				lisRetorno = obPersistencia.consultarAcompanharVotacaoErroMaterial(idServentiaCargo, processoNumero);
			} finally {
				obFabricaConexao.fecharConexao();
			}
			return lisRetorno;
	}	
	
	//lrcampos 19/08/2019 * Verifica se mais de 2/3 dos votantes acompanharam o relator
	public boolean verificarResultadoOrgaoEspecial(AudienciaProcessoDt audienciaProcessoDt, UsuarioNe usuario) throws Exception {
		VotoNe votoNe = new VotoNe();
		boolean isConfirmarExtratoAtaOrgalEspecial = false;
		if (verificarPrazoExpiradoCorteEspecial(audienciaProcessoDt.getId(), audienciaProcessoDt.getProcessoDt().getId()) && votoNe.isServentiaEspecial(usuario.getUsuarioDt().getId_Serventia())) {
			isConfirmarExtratoAtaOrgalEspecial = votoNe.podeConfirmarExtratoAtaOrgalEspecial(audienciaProcessoDt.getId(), usuario);
		}

		return isConfirmarExtratoAtaOrgalEspecial;
	}

	public boolean verificarPrazoExpiradoCorteEspecial(String id_AudiProc, String id_Processo) throws Exception {
		VotoNe votoNe = new VotoNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		LocalDateTime atual = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		 
		List<VotanteDt> votantes = votoNe.consultarVotantesSessaoCompleto(id_AudiProc);
		
		boolean prazoVencido = votantes.parallelStream().filter(x -> {
			try {
				return pendenciaNe.consultarPendenciasAbertasProcessoPJD(id_Processo, x.getIdServentiaCargo()).parallelStream()
						.filter(y -> StringUtils.equals(((PendenciaDt)y).getPendenciaTipoCodigo(), String.valueOf(PendenciaTipoDt.VOTO_SESSAO)))
						.filter(y -> StringUtils.isNotEmpty(((PendenciaDt)y).getDataLimite()))
						.filter(y -> LocalDateTime.parse(((PendenciaDt)y).getDataLimite().substring(0, 16), formatter).isBefore(atual))
						.findAny().isPresent();
			}catch (Exception e) {
				System.out.println("Exception ao verificarPrazoExpiradoCorteEspecial: "+e.getMessage()+";");
				return false;
			}			
		}).findAny().isPresent();
		return prazoVencido;
	}
	
	// jvosantos - 03/09/2019 16:32 - Implementar m�todo que retorna a quantidade de Voto/Ementa aguardando assinatura
	public long consultarQuantidadeVotoEmentaAguardandoAssinaturaSessaoVirtual(String idServentiaCargo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarQuantidadeVotoEmentaAguardandoAssinaturaSessaoVirtual(idServentiaCargo);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	// jvosantos - 13/09/2019 14:45 - M�todo que busca audiencia por pend�ncia e depois por processo
	public AudienciaProcessoDt buscarAudienciaProcessoPendentePorPendenciaOuProcesso(String id_Pendencia,
			UsuarioNe usuario) throws Exception {
		AudienciaNe audienciaNe = new AudienciaNe();
		
		String idAudiProc = new AudienciaProcessoPendenciaNe().consultarPorIdPendEmAndamento(id_Pendencia); // jvosantos - 06/11/2019 12:20 - Corre��o para buscar as audi_proc em andamento que tem aquela pendencia
		
		AudienciaProcessoDt audiProc = null;
		
		if(StringUtils.isEmpty(idAudiProc)) {
			PendenciaNe pendenciaNe = new PendenciaNe();
			audiProc = audienciaNe.consultarAudienciaProcessoPendente(pendenciaNe.consultarId(id_Pendencia).getId_Processo(), usuario.getUsuarioDt());
		}else
			audiProc = consultarIdCompleto(idAudiProc);
		return audiProc;
	}

	// jvosantos - 25/09/2019 13:20 - M�todo para cadastrar votante
	public void cadastrarVotante(AudienciaProcessoVotantesDt votante, FabricaConexao fabricaConexao) throws Exception {
		AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(fabricaConexao.getConexao());
		obPersistencia.cadastrarVotanteSessaoVirtual(votante);
	}

	// jvosantos - 29/10/2019 17:23 - M�todo para Retornar uma sess�o de julgamento retirada de pauta para a pauta
	public void retornarJulgamento(MovimentacaoProcessoDt movimentacaoProcessodt, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		VotoNe votoNe = new VotoNe();
		AudienciaNe audienciaNe = new AudienciaNe();
		ArquivoNe arquivoNe = new ArquivoNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		
		AudienciaDt audienciaDt = audienciaNe.consultarAudienciaProcessoCompleta(movimentacaoProcessodt.getIdRedirecionaOutraServentia());
		AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
		
		if(audienciaDt.isSessaoIniciada()) 
			throw new MensagemException("N�o � poss�vel retornar para julgamento quando a sess�o j� foi iniciada.");
		
		try {
			fabricaConexao.iniciarTransacao();
			
			MovimentacaoDt movimentacaoDt = retirarExtratoAtaJulgamentoOuAcordaoOuRetornar(usuarioDt, audienciaDt, movimentacaoProcessodt, fabricaConexao);
			List<PendenciaArquivoDt> arquivosVoto;
			List<PendenciaArquivoDt> arquivosEmenta;
			List<String> idPendencias = new ArrayList<String>();

			arquivosVoto = consultarArquivosEFinalizarPendencia(audienciaProcessoDt.getId_PendenciaVotoRelator(), usuarioDt, votoNe, pendenciaNe,
					pendenciaArquivoNe, fabricaConexao);
			
			arquivosEmenta = StringUtils.isNotEmpty(audienciaProcessoDt.getId_PendenciaEmentaRelator()) ? consultarArquivosEFinalizarPendencia(audienciaProcessoDt.getId_PendenciaEmentaRelator(), usuarioDt, votoNe, pendenciaNe,
					pendenciaArquivoNe, fabricaConexao) : null;
			
			PendenciaDt pendenciaEmentaDt = null;
			PendenciaDt pendenciaVotoDt = audienciaNe.gerarPendenciaVoto(audienciaProcessoDt.getId_ServentiaCargo(), movimentacaoDt.getId(), audienciaProcessoDt.getProcessoDt().getId(), audienciaProcessoDt.getProcessoDt().getId_ProcessoPrioridade(), false, pendenciaNe, usuarioDt, fabricaConexao, null, audienciaProcessoDt.getId());
			
			audienciaProcessoDt.setId_PendenciaVotoRelator(pendenciaVotoDt.getId());
			
			idPendencias.add(pendenciaVotoDt.getId());
			
			boolean temEmenta = arquivosEmenta != null && !arquivosEmenta.isEmpty();
			boolean temVoto = arquivosVoto != null && !arquivosVoto.isEmpty();
			
			if(temVoto)
				salvarArquivosPendencia(pendenciaVotoDt, arquivosVoto, usuarioDt, pendenciaArquivoNe, arquivoNe, fabricaConexao);
			
			if (temEmenta) {
				pendenciaVotoDt.setId_Movimentacao(StringUtils.EMPTY); // limpa o ID_Movimentacao para criar a pendencia de ementa
				pendenciaEmentaDt = pendenciaNe.criarPendenciaDesembargador(pendenciaVotoDt, String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), usuarioDt, audienciaProcessoDt.getId(), audienciaProcessoDt.getId_ServentiaCargo(), fabricaConexao);
				
				// A pendencia de ementa foi criada com o Analista como respons�vel (por algum motivo o comportamento do met�do � esse)
				// Altera o responsavel de Analista para Relator
				PendenciaResponsavelNe pendenciaResponsavelNe = new PendenciaResponsavelNe();
				pendenciaResponsavelNe.alterarResponsavelPendencia(pendenciaEmentaDt.getId(), usuarioDt.getId_ServentiaCargo(), audienciaProcessoDt.getId_ServentiaCargo(), new LogDt(usuarioDt.getId(), usuarioDt.getIpComputadorLog()), fabricaConexao);
				
				if(StringUtils.isNotEmpty(pendenciaEmentaDt.getDataFim())) {
					pendenciaEmentaDt.setDataFim(StringUtils.EMPTY);
					pendenciaNe.salvar(pendenciaEmentaDt, fabricaConexao);
				}
				
				idPendencias.add(pendenciaEmentaDt.getId());
				salvarArquivosPendencia(pendenciaEmentaDt, arquivosEmenta, usuarioDt, pendenciaArquivoNe, arquivoNe, fabricaConexao);
			}
			
			audienciaProcessoDt.setId_PendenciaEmentaRelator(temEmenta ? pendenciaEmentaDt.getId() : null);
			
			vincularPendenciaVotoEmenta(audienciaProcessoDt, fabricaConexao);
			
			if(!temEmenta)
				limpaVinculoPendenciaEmenta(audienciaProcessoDt, fabricaConexao);
			
			audienciaProcessoPendenciaNe.salvar(idPendencias, audienciaProcessoDt.getId(), fabricaConexao);
			
			fabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			fabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			fabricaConexao.fecharConexao();
		}
	}

	// jvosantos - 29/10/2019 17:23 - M�todo para salvar os arquivos da pendencia anterior na nova pendencia
	private void salvarArquivosPendencia(PendenciaDt pendenciaDt, List<PendenciaArquivoDt> arquivosPend,  UsuarioDt usuario, PendenciaArquivoNe pendenciaArquivoNe, ArquivoNe arquivoNe,
			FabricaConexao fabricaConexao) throws Exception {
		for(PendenciaArquivoDt x : arquivosPend) {
			x.setArquivoDt(arquivoNe.consultarId(x.getId_Arquivo(), fabricaConexao.getConexao()));
			x.getArquivoDt().setId_UsuarioLog(usuario.getId());
			x.getArquivoDt().setIpComputadorLog(usuario.getIpComputadorLog());
		}
		
		List<PendenciaArquivoDt> arquivos  = arquivosPend.stream().map(x -> {
				x.setId(StringUtils.EMPTY);
				x.getArquivoDt().setId(StringUtils.EMPTY);
				x.setId_Arquivo("null");
				x.setNomeArquivo(x.getArquivoDt().getNomeArquivo());
				x.setPendenciaDt(pendenciaDt);
				x.setId_Pendencia(pendenciaDt.getId());
				x.setId_UsuarioLog(usuario.getId());
				x.setIpComputadorLog(usuario.getIpComputadorLog());
				return x;
			}).collect(Collectors.toList());
		
		for(PendenciaArquivoDt x : arquivos) {
			arquivoNe.salvar(x.getArquivoDt(), fabricaConexao);
			x.setId_Arquivo(x.getArquivoDt().getId());
			pendenciaArquivoNe.salvar(x, fabricaConexao);
		}
	}

	// jvosantos - 29/10/2019 17:23 - M�todo para consultar arquivos e finalizar as pendencias em aberto
	private List<PendenciaArquivoDt> consultarArquivosEFinalizarPendencia(String idPendencia, UsuarioDt usuarioDt,
			VotoNe votoNe, PendenciaNe pendenciaNe, PendenciaArquivoNe pendenciaArquivoNe,
			FabricaConexao fabricaConexao) throws Exception {
		PendenciaDt pendenciaDt = pendenciaNe.consultarId(idPendencia, fabricaConexao);
		
		if(pendenciaDt != null)
			votoNe.setInfoPendenciaFinalizar(pendenciaDt, usuarioDt, fabricaConexao);
		else
			pendenciaDt = pendenciaNe.consultarFinalizadaId(idPendencia);
		
		return pendenciaArquivoNe.consultarPendenciaFinalizada(pendenciaDt, false, fabricaConexao);
	}

	public void alterarStatusAnalista(String idAudienciaProcesso, int novoStatusCodigo, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());

		obPersistencia.alterarStatusAnalista(idAudienciaProcesso, novoStatusCodigo);
	}

	public void alterarStatusTemp(String idAudienciaProcesso, int novoStatusCodigo, FabricaConexao obFabricaConexao) throws Exception {
		AudienciaProcessoPs obPersistencia = new AudienciaProcessoPs(obFabricaConexao.getConexao());

		obPersistencia.alterarStatusTemp(idAudienciaProcesso, novoStatusCodigo);
	}

	public void alterarVotoEmenta(AnaliseConclusaoDt analiseConclusaoDt, List<ArquivoDt> listaArquivos, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		AudienciaProcessoPendenciaNe audienciaProcessoPendenciaNe = new AudienciaProcessoPendenciaNe();
		PendenciaArquivoNe pendenciaArquivoNe = new PendenciaArquivoNe();
		VotoNe votoNe = new VotoNe();
		
		try {
			fabricaConexao.iniciarTransacao();
			
			AudienciaProcessoDt audienciaProcessoDt = audienciaProcessoNe.consultarIdCompleto(audienciaProcessoPendenciaNe.consultarPorIdPend(analiseConclusaoDt.getPendenciaDt().getId()), fabricaConexao);
			
			audienciaProcessoNe.alterarStatusAudienciaTemp(audienciaProcessoDt.getId(), analiseConclusaoDt.getAudienciaStatusCodigo(), fabricaConexao); // Alterar status_temp (decis�o) da AUDI_PROC
			
			tratarArquivosAnaliseConclusao(analiseConclusaoDt, listaArquivos, usuarioDt);
			
			pendenciaArquivoNe.salvarPreAnaliseVotoEmenta(analiseConclusaoDt, usuarioDt, audienciaProcessoDt.getId(), true, fabricaConexao);
			
			votoNe.desativarTodosVotos(audienciaProcessoDt.getId(), fabricaConexao);
			
			votoNe.finalizarPendenciasVoto(audienciaProcessoDt.getId(), usuarioDt, fabricaConexao);
			
			votoNe.iniciarVotacaoSessaoVirtual(audienciaProcessoDt, usuarioDt, true, fabricaConexao);

			fabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			fabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			fabricaConexao.fecharConexao();
		}		
	}

	// jvosantos - 27/12/2019 14:02 - Extrair bloco de c�digo reutilizado para tratar os arquivos na analiseConclusao
	public void tratarArquivosAnaliseConclusao(AnaliseConclusaoDt analiseConclusaoDt, List<ArquivoDt> listaArquivos,
			UsuarioDt usuarioDt) throws Exception {
		Signer.acceptSSL();
		for (ArquivoDt arquivo : listaArquivos) {			
			arquivo.setAssinado(true);
			String conteudoArquivo = new String((arquivo.getConteudo()));
			
			if(arquivo.getId_ArquivoTipo().equals(analiseConclusaoDt.getId_ArquivoTipoEmenta())) {
				analiseConclusaoDt.setTextoEditorEmenta(conteudoArquivo);
			} else {
				analiseConclusaoDt.setTextoEditor(conteudoArquivo);
			}

			arquivo.setId_UsuarioLog(analiseConclusaoDt.getId_UsuarioLog());
			arquivo.setIpComputadorLog(analiseConclusaoDt.getIpComputadorLog());
		}
	}

	// jvosantos - 24/01/2020 18:14 - M�todo para alterar voto ementa no erro material
	public void alterarVotoEmentaErroMaterial(FinalizacaoVotoSessaoDt finalizacaoVoto, UsuarioNe usuario, List<ArquivoDt> lista) throws Exception {
		FabricaConexao fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

		VotoNe votoNe = new VotoNe();
		
		try {
			fabricaConexao.iniciarTransacao();
			
			AudienciaProcessoDt audienciaProcessoDt = finalizacaoVoto.getAudienciaProcesso();
			
			alterarStatusAudienciaTemp(audienciaProcessoDt.getId(), finalizacaoVoto.getAudienciaProcesso().getAudienciaProcessoStatusCodigoTemp(), fabricaConexao); // Alterar status_temp (decis�o) da AUDI_PROC
			
			List<String> idServentiaCargoVotantes = votoNe.consultarVotantesSessaoCompleto(audienciaProcessoDt.getId()).stream().map(VotanteDt::getIdServentiaCargo).collect(Collectors.toList());
			
			votoNe.desativarTodosVotosIdServentiaCargo(audienciaProcessoDt.getId(), fabricaConexao, idServentiaCargoVotantes);
			
			votoNe.finalizarPendenciasVoto(audienciaProcessoDt.getId(), usuario.getUsuarioDt(), fabricaConexao);

			List<PendenciaDt> pendenciaVotoEmenta = votoNe.gerarPendenciaVotoEmenta(usuario, lista, audienciaProcessoDt, finalizacaoVoto, fabricaConexao);

			votoNe.iniciarVotacaoSessaoVirtual(audienciaProcessoDt, usuario.getUsuarioDt(), true, fabricaConexao);
			
			PendenciaNe pendenciaNe = new PendenciaNe();
			
			PendenciaDt pendenciaDt = pendenciaNe.consultarId(finalizacaoVoto.getIdPendencia(), fabricaConexao);

			VotoDt votoDt = new VotoDt();
			votoDt.setAtivo(true);
			votoDt.setAudienciaProcessoDt(audienciaProcessoDt);
			votoDt.setVotoTipoCodigo(String.valueOf(VotoTipoDt.JULGAMENTO_REINICIADO));
			
			if(pendenciaDt != null) {
				pendenciaDt.setId_PendenciaStatus(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
				pendenciaNe.setInfoPendenciaFinalizar(pendenciaDt, usuario.getUsuarioDt(), fabricaConexao);
				votoDt.setPendenciaDt(pendenciaDt);
			}else
				votoDt.setPendenciaDt(pendenciaVotoEmenta.get(0));
			
			votoNe.inserirVotoDesativandoAntigo(votoDt, fabricaConexao);
			
			// Finalizar pendencia de Verificar Erro Material, se ela existir
			
			votoNe.finalizarPendenciasVotacao(audienciaProcessoDt.getId(), usuario.getUsuarioDt(), fabricaConexao, PendenciaTipoDt.VERIFICAR_ERRO_MATERIAL);
			
			fabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			fabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			fabricaConexao.fecharConexao();
		}		
	}
	
	/**
	 * ATEN��O: ESTE M�TODO � EXCLUSIVO PARA O RETORNO AUTOM�TICO DE PROCESSOS ENCAMINHADOS AO S�TIMO CEJUSC.
	 * SER� UTILIZADO EXCLUSIVAMENTE PELO SERVI�O DE EXECU��O AUTOM�TICA DESENVOLVIDO PARA ESTE FIM. N�O
	 * UTILIZAR EM OUTRAS SITUA��ES. 	 
	 *  
	 * @author hrrosa
	 * @param ProcessoDt
	 * @throws Exception
	 */
	public void fecharAudienciasRetornoAutomaticoCEJUSC(ProcessoDt processoDt, String nomeTabelaLog, FabricaConexao obFabricaConexao)  throws Exception {
		AudienciaProcessoPs audienciaProcessoPs = new AudienciaProcessoPs(obFabricaConexao.getConexao());
		List<AudienciaProcessoDt> listaAudienciaProcesso = null;
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		AudienciaProcessoResponsavelNe audiProcRespNe = new AudienciaProcessoResponsavelNe();
		AudienciaNe audienciaNe = new AudienciaNe();
		LogDt logDt = new LogDt();
		AudienciaProcessoResponsavelDt audiProcRespDt;
		AudienciaDt audienciaDt;
		
		logDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		logDt.setId_LogTipo(String.valueOf(LogTipoDt.Alterar));
		String complemento;
		String valorLogNovo;
		
		//RETORNAR A LISTA DAS AUDI�NCIAS ABERTAS NO PROCESSO
		listaAudienciaProcesso = audienciaProcessoPs.listarAudienciaProcessoPendente(processoDt.getId());
		
		
		//PERCORRER A LISTA FECHANDO AS AUDI�NCIAS
		for(AudienciaProcessoDt audienciaProcessoDt: listaAudienciaProcesso){
			//FINALIZAR AUDI�NCIA
			
			//-- atualizando registro na audi_proc
			audienciaProcessoDt = this.consultarIdCompleto(audienciaProcessoDt.getId(), obFabricaConexao);
			audienciaProcessoDt.setId_AudienciaProcessoStatus(String.valueOf(AudienciaProcessoStatusDt.RETIRAR_PAUTA));
			audienciaProcessoDt.setDataMovimentacao(Funcoes.DataHora(new Date()));
			audienciaProcessoDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
			this.salvar(audienciaProcessoDt, obFabricaConexao);
			
			//-- criando registro na audi_proc_resp
			audiProcRespDt = new AudienciaProcessoResponsavelDt();
			audiProcRespDt.setId_AudienciaProcesso(audienciaProcessoDt.getId());
			audiProcRespDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
			audiProcRespDt.setId_CargoTipo(String.valueOf(CargoTipoDt.SISTEMA_PROJUDI)); //na tabela, o id deste cargo_tipo � igual ao c�digo.
			audiProcRespDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
			audiProcRespNe.salvar(audiProcRespDt, obFabricaConexao);
			
			//GERAR MOVIMENTA��O
			//-- obtendo complemento da movimenta��o
			audienciaDt = audienciaNe.consultarId(audienciaProcessoDt.getId_Audiencia(), obFabricaConexao);
			complemento = "[RETORNO AUTOM�TICO](" + audienciaDt.getAudienciaTipo() + " - " + audienciaDt.getDataAgendada() + ")";
			movimentacaoNe.gerarMovimentacaoAudienciaDesmarcada(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, complemento, logDt, obFabricaConexao);
			
			//SALVAR LOG DO FECHAMENTO DA AUDI�NCIA
            valorLogNovo = "[Id_audi_proc:" + audienciaProcessoDt.getId() + "; Id_UsuarioFinalizador:" + UsuarioServentiaDt.SistemaProjudi + "; Data_Fim:" + Funcoes.DataHora(new Date()) + "]";
            logDt = new LogDt(nomeTabelaLog, audienciaProcessoDt.getId(), UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.FechamentoAutomaticoPendencia), "", valorLogNovo);
            obLog.salvar(logDt, obFabricaConexao);

		}

	}
	
	/**
	 * ATEN��O: ESTE M�TODO � EXCLUSIVO PARA O ARQUIVAMENTO AUTOM�TICO DE PROCESSOS DE EXECU��O PENAL.
	 * SER� UTILIZADO EXCLUSIVAMENTE PELO SERVI�O DE EXECU��O AUTOM�TICA DESENVOLVIDO PARA ESTE FIM. N�O
	 * UTILIZAR EM OUTRAS SITUA��ES. 	 
	 *  
	 * @author hrrosa
	 * @param ProcessoDt
	 * @throws Exception
	 */
	public void fecharAudienciasArquivamentoAutomaticoExecucaoPenal(ProcessoDt processoDt, String nomeTabelaLog, FabricaConexao obFabricaConexao)  throws Exception {
		AudienciaProcessoPs audienciaProcessoPs = new AudienciaProcessoPs(obFabricaConexao.getConexao());
		List<AudienciaProcessoDt> listaAudienciaProcesso = null;
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		AudienciaProcessoResponsavelNe audiProcRespNe = new AudienciaProcessoResponsavelNe();
		AudienciaNe audienciaNe = new AudienciaNe();
		LogDt logDt = new LogDt();
		AudienciaProcessoResponsavelDt audiProcRespDt;
		AudienciaDt audienciaDt;
		
		logDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
		logDt.setId_LogTipo(String.valueOf(LogTipoDt.Alterar));
		String complemento;
		String valorLogNovo;
		
		//RETORNAR A LISTA DAS AUDI�NCIAS ABERTAS NO PROCESSO
		listaAudienciaProcesso = audienciaProcessoPs.listarAudienciaProcessoPendente(processoDt.getId());
		
		
		//PERCORRER A LISTA FECHANDO AS AUDI�NCIAS
		for(AudienciaProcessoDt audienciaProcessoDt: listaAudienciaProcesso){
			//FINALIZAR AUDI�NCIA
			
			//-- atualizando registro na audi_proc
			audienciaProcessoDt = this.consultarIdCompleto(audienciaProcessoDt.getId(), obFabricaConexao);
			audienciaProcessoDt.setId_AudienciaProcessoStatus(String.valueOf(AudienciaProcessoStatusDt.RETIRAR_PAUTA));
			audienciaProcessoDt.setDataMovimentacao(Funcoes.DataHora(new Date()));
			audienciaProcessoDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
			this.salvar(audienciaProcessoDt, obFabricaConexao);
			
			//-- criando registro na audi_proc_resp
			audiProcRespDt = new AudienciaProcessoResponsavelDt();
			audiProcRespDt.setId_AudienciaProcesso(audienciaProcessoDt.getId());
			audiProcRespDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
			audiProcRespDt.setId_CargoTipo(String.valueOf(CargoTipoDt.SISTEMA_PROJUDI)); //na tabela, o id deste cargo_tipo � igual ao c�digo.
			audiProcRespDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
			audiProcRespNe.salvar(audiProcRespDt, obFabricaConexao);
			
			//GERAR MOVIMENTA��O
			//-- obtendo complemento da movimenta��o
			audienciaDt = audienciaNe.consultarId(audienciaProcessoDt.getId_Audiencia(), obFabricaConexao);
			complemento = "[ARQUIVAMENTO AUTOM�TICO](" + audienciaDt.getAudienciaTipo() + " - " + audienciaDt.getDataAgendada() + ")";
			movimentacaoNe.gerarMovimentacaoAudienciaDesmarcada(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, complemento, logDt, obFabricaConexao);
			
			//SALVAR LOG DO FECHAMENTO DA AUDI�NCIA
            valorLogNovo = "[Id_audi_proc:" + audienciaProcessoDt.getId() + "; Id_UsuarioFinalizador:" + UsuarioServentiaDt.SistemaProjudi + "; Data_Fim:" + Funcoes.DataHora(new Date()) + "]";
            logDt = new LogDt(nomeTabelaLog, audienciaProcessoDt.getId(), UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.FechamentoAutomaticoPendencia), "", valorLogNovo);
            obLog.salvar(logDt, obFabricaConexao);

		}

	}

	// alsqueiroz - 18/09/2019 09:25 - Cria��o do m�todo para consultar os votantes da audi�ncia processo
	public List<AudienciaProcessoVotantesDt> consultarVotantesAudienciaProcesso(String idAudienciaProcesso, Boolean soVotante) throws Exception {
		FabricaConexao fabricaConexao = null;

		try {
			fabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);

			return (new AudienciaProcessoPs(fabricaConexao.getConexao())).consultarVotantesAudienciaProcesso(idAudienciaProcesso, soVotante);
		} finally {
			fabricaConexao.fecharConexao();
		}
	}

}
