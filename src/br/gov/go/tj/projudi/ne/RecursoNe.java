package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PonteiroLogDt;
import br.gov.go.tj.projudi.dt.PonteiroLogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoAssuntoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.RecursoPs;
import br.gov.go.tj.projudi.util.DistribuicaoProcesso;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;

public class RecursoNe extends RecursoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2677423747141652680L;

	/**
	 * Verifica os dados de um recurso para autuação
	 * @throws Exception 
	 */
	public String Verificar(RecursoDt dados) throws Exception {
		String stRetorno = "";
		if (dados.ehAutuacao() && dados.getSemParte() != null && dados.getSemParte().equalsIgnoreCase("0")){
			if (dados.getListaRecorrentes() == null || dados.getListaRecorrentes().size() == 0) stRetorno += "Selecione uma Parte Recorrente. \n";
			if (dados.getListaRecorridos() == null || dados.getListaRecorridos().size() == 0) stRetorno += "Selecione uma Parte Recorrida. \n";
		}
		if (dados.getListaAssuntos() == null || dados.getListaAssuntos().size() == 0) stRetorno += "Insira ao menos um Assunto no Recurso.";
		
		if ((dados.getDataRecebimento() == null || dados.getDataRecebimento().equals("")) && new RecursoNe().isRecursoAutuado(dados.getId_Processo(), dados.getId_ServentiaRecurso())){
			stRetorno += "O Processo já foi Autuado.";
		}
		
		List listaAssuntosTemp = new ArrayList();
		listaAssuntosTemp.addAll(dados.getListaAssuntos());
		while (!listaAssuntosTemp.isEmpty()) {
			RecursoAssuntoDt assunto = (RecursoAssuntoDt) listaAssuntosTemp.get(0);
			listaAssuntosTemp.remove(0);
			for (int i = 0; i < listaAssuntosTemp.size(); i++) {
				RecursoAssuntoDt assuntoComp = (RecursoAssuntoDt) listaAssuntosTemp.get(i);
				if(assuntoComp.getId_Assunto().equals(assunto.getId_Assunto())) {
					stRetorno += "O Assunto: " + assunto.getAssunto() + " está duplicado na lista. Remova a duplicidade antes de prosseguir.";
					break;
				}
			}
		}
		
		if (stRetorno.length() == 0)
		{
			RecursoDt dadosBanco = this.consultarIdCompleto(dados.getId());
			if (!dadosBanco.getId_ProcessoTipo().equalsIgnoreCase(dados.getId_ProcessoTipo())) {
				if (dadosBanco.getListaRecorrentesAtivos(dados.getId_ProcessoTipo()).size() > 0 || 
				    dadosBanco.getListaRecorridosAtivos(dados.getId_ProcessoTipo()).size() > 0) {
					stRetorno += "Já exite um recurso ativo utilizando a classe informada.";
				}
			}
		}
		return stRetorno;
	}

	private String verificarProcessoTipoRecursoInominado(String serventiaSubTipoCodigo){
	    String stTemp=null;
	    switch  (Funcoes.StringToInt(serventiaSubTipoCodigo)){
	        case ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL:
	        case ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL_CRIMINAL:
	        case ServentiaSubtipoDt.TURMA_RECURSAL_CRIMINAL:
	        case ServentiaSubtipoDt.UPJ_TURMA_RECURSAL:
	            stTemp = String.valueOf(ProcessoTipoDt.RECURSO_INOMINADO);
	            break;
	        case ServentiaSubtipoDt.CAMARA_CIVEL:
	        case ServentiaSubtipoDt.CORTE_ESPECIAL:
	        case ServentiaSubtipoDt.SECAO_CIVEL:	        
	            stTemp = String.valueOf(ProcessoTipoDt.APELACAO_CIVEL);
                break;
            case ServentiaSubtipoDt.CAMARA_CRIMINAL:
            case ServentiaSubtipoDt.SECAO_CRIMINAL:
                stTemp = String.valueOf(ProcessoTipoDt.APELACAO_CRIMINAL);
                break;
            case ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL:
            case ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL:
            case ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR:
            case ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL:
            case ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL:
            case ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR:
            case ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL:
            	stTemp = String.valueOf(ProcessoTipoDt.REEXAME_NECESSARIO);
            	break;
	    }
	    return stTemp;
	}
	
	public String VerificarConversaoProcessoRecurso(ProcessoDt dados) throws Exception {
		String stRetorno = "";		
		FabricaConexao obFabricaConexao = null;
		
		try{			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);			
			String id_Recurso = this.getRecursoAtivo(dados.getId_Processo(), dados.getId_Serventia(), obFabricaConexao);
			if (id_Recurso != null && id_Recurso.length()>0) 
				stRetorno += "Processo já possui um Recurso Ativo.";
			
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return stRetorno;
	}

	
	/**
	 * Método que salva um recurso inominado, continuação do processo de 1º grau em uma instância superior
	 * @param processoDt
	 * @param obFabricaConexao
	 * 
	 * @author msapaula
	 * Recurso inominado, Apelação Civel e Criminal
	 * @param usuarioDt 
	 */
	public void salvarRecursoInstaciaSuperior(ProcessoDt processoDt, UsuarioDt usuarioDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ServentiaNe serventiaNe = new ServentiaNe();
		String id_ServentiaRecursalAnterior = null;
		AreaDistribuicaoDt areaRecursal = null;
		String id_Serventia = null;
		ServentiaDt serventiaSegundoGrauDt = null;
		
		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		//ServentiaCargoDt cargosDistribuicao = null;
		String stProcessoTipoCodigo = null;

		String id_ServentiaAnterior = processoDt.getId_Serventia();
		ServentiaDt serventiaAterior = serventiaNe.consultarId(id_ServentiaAnterior);
		String faseAnterior = processoDt.getProcessoFaseCodigo();
		//ServentiaCargoDt serventiaCargoRelator = null;
		//String[] dadoConexao=null;
		String id_relator="";
		String stTipoConexaoPrevencao="Serventia"; 

		//Recurso é gerado
		RecursoDt recursoDt = new RecursoDt();
		recursoDt.setId_Processo(processoDt.getId());
		recursoDt.setId_ServentiaOrigem(processoDt.getId_Serventia());
		recursoDt.setId_UsuarioLog(logDt.getId_Usuario());
		recursoDt.setIpComputadorLog(logDt.getIpComputador());
		
		//Validação para impedir que um processo arquivado seja redistribuído em instância superior
		if(processoDt.getDataArquivamento() != null && !processoDt.getDataArquivamento().equalsIgnoreCase("")) {
			throw new MensagemException("Processo ARQUIVADO não pode ser enviado para instância superior. Desarquive-o e, em seguida, faça o envio.");
		}
		//Processo não pode subir sem um assunto definido
		if (!processoNe.isTemAssunto(processoDt.getId_Processo())){
 		   throw new MensagemException("Processo Sem Assunto: Para enviar a Instância Superior defina pelo menos um Assunto para o Processo.");
 	    }
		
		//Processo não pode subir se for um processo misto
		if (processoDt.isProcessoHibrido()){
			throw new MensagemException("Atenção: Processo híbrido. Encaminhe os Autos Físicos ao Tribunal para Digitalização e Movimente o processo com a movimentação tipo \"Aguardando Digitalização para Distribuição no 2º Grau\"");
		}
				
		//se ja esteve em uma serventia será prevenção
		String stComplemento =" (Normal)";
		
		//consulta a área distribuição recursal vinculada a serventia do processo
		areaRecursal = new AreaDistribuicaoNe().consultarId(processoDt.getId_AreaDistribuicaoRecursal());
		
		//Verifica se processo já esteve no Segundo Grau anteriormente
		id_ServentiaRecursalAnterior = getServentiaRecursoAnterior(areaRecursal.getId(), processoDt.getId_Processo(), obFabricaConexao);
		
		//pego o código tipo de ação
		if (ServentiaSubtipoDt.isFazenda(serventiaAterior.getServentiaSubtipoCodigo())) {
			stProcessoTipoCodigo = verificarProcessoTipoRecursoInominado(serventiaAterior.getServentiaSubtipoCodigo());
		} else {
			stProcessoTipoCodigo = verificarProcessoTipoRecursoInominado(areaRecursal.getServentiaSubtipoCodigo());
		}
		
		// Ao redistribuir os responsáveis pelo processo devem ser desativados
		processoResponsavelNe.desativarResponsaveisProcesso(processoDt, obFabricaConexao);
		
		boolean boRetorno = false;
		/// se for retorno, tanta para a turma como para o segundo grau, reativa o responsável ------////
		if (id_ServentiaRecursalAnterior != null) {
			
			id_Serventia = id_ServentiaRecursalAnterior;
			serventiaSegundoGrauDt = new ServentiaNe().consultarId(id_Serventia);
			
			//Setanto o tipo da ação, se estiver subindo da Câmara é Apelação, senão é Recurso Inominado
			if (!ServentiaSubtipoDt.isFazenda(serventiaAterior.getServentiaSubtipoCodigo())){
				stProcessoTipoCodigo = verificarProcessoTipoRecursoInominado(serventiaSegundoGrauDt.getServentiaSubtipoCodigo());
			}
			
			//Ao retornar ao Segundo Grau, os responsáveis pelo recurso devem ser ativados		
			//reativando todos os responsáveis, exceto desembargador
			processoResponsavelNe.reativarResponsaveisProcesso(processoDt.getId(), id_Serventia, obFabricaConexao);
			//Consultando o id do relator que está sendo reativado
			ServentiaCargoDt novoRelator = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoSegundoGrauInativo(processoDt.getId(), id_Serventia, obFabricaConexao);
			//Reativando desembargador responsável encontrado.
			//Caso não entre no if, vai sortear o relator mais a frente no código.
			if(novoRelator != null) {
				try{
					id_relator = novoRelator.getId();
					processoResponsavelNe.reativarMagistradoResposavelProcesso(processoDt.getId(), id_relator, obFabricaConexao);
				} catch(Exception e){
					throw new MensagemException("Não foi possível encontrar um magistrado responsável para o processo, favor entrar em contato com o suporte.");
				}
			}
			
			boRetorno = (id_relator!=null && id_relator.length()>0);
			
			
			stComplemento = " (retorno)";		
									
		}else{
			//quando não for retorno
			//se for turma não verificar conexao ou prevensao (despacho 1944/2012 corregedoria)
			if(ServentiaSubtipoDt.isTurma(areaRecursal.getServentiaSubtipoCodigo())){
				//alteração para cumprir o provimento 16/2012 da CGJ
				//id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(areaRecursal.getId(), processoDt.getId_ProcessoTipo());
				id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(areaRecursal.getId());
				//id_relator = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_Serventia,areaRecursal.getId());
				
			}else{
					//se não esteve, verifico se existe algum processo em recurso dependete do processo de primeiro grau.				
					// [0] id do processo [1] JULGADO_2_GRAU [2] numero do processo [3] id serventia recurso [4] id serv cargo relator
					String[] stProcessoDependente = { null, null, null, null, null };
					stProcessoDependente = processoNe.consultarRecursosDependente(processoDt.getId_Processo(), processoDt.getId_AreaDistribuicaoRecursal(), obFabricaConexao);
					// se achou o processo, pego o responsável
					if (stProcessoDependente[0] != null) {
						id_ServentiaRecursalAnterior = stProcessoDependente[3];
						id_Serventia = stProcessoDependente[3];
						
						if (stProcessoDependente[4] != null) {
							id_relator = stProcessoDependente[4];
							stTipoConexaoPrevencao = "Relator";
						} else {
							id_Serventia = id_ServentiaRecursalAnterior;
							// alteração para atender o provimento 16/2012 da CGJ
							//id_relator = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_ServentiaRecursalAnterior, processoDt.getId_AreaDistribuicaoRecursal());
						}

						// se hover valor na possicao [1] o processo está arquivado
						if (stProcessoDependente[1] != null) {
							stComplemento = " (Prevenção " + stTipoConexaoPrevencao + ") " + stProcessoDependente[2];
						} else {
							stComplemento = " (Conexão " + stTipoConexaoPrevencao + ") " + stProcessoDependente[2];
						}
					}
					//se não encontrou um processo dependente tento verificar a existência de conexão
					if (id_ServentiaRecursalAnterior==null){
						String[] dadosConexao =  verificaConexaoRecurso(processoDt.getId_AreaDistribuicaoRecursal(), processoDt.getId_Processo(), processoDt.getListaPolosAtivos(), processoDt.getListaPolosPassivos(), processoDt.getListaOutrasPartes(), obFabricaConexao);
						//dadosConexao têm as informações do processo conexo, na posição 3 esta a data do retorno, se retornou é prevensão, caso contrario conexão. na posicao 4 está o número do processo conexo	
						if (dadosConexao != null && dadosConexao[4]!=null &&  dadosConexao[4].length()>0 ) {
							id_ServentiaRecursalAnterior = dadosConexao[0];
							id_Serventia = dadosConexao[0];
							id_relator = dadosConexao[1];
							stComplemento = " (Conexão  " + stTipoConexaoPrevencao + ") " + dadosConexao[4];
							if (dadosConexao[3]!=null && dadosConexao[3].length()>0) stComplemento = " (prevenção " + stTipoConexaoPrevencao + ") " + dadosConexao[4];
						} else {
							id_Serventia = DistribuicaoProcesso.getInstance().getDistribuicao(areaRecursal.getId());
						}
					}
			}									 
			
		}
		
		if (id_Serventia == null){
			throw new MensagemException("Não foi possível encontrar uma serventia para essa área de distribuição, favor entrar em contato com o suporte.");
		
		} else if (serventiaSegundoGrauDt == null){
			serventiaSegundoGrauDt = new ServentiaNe().consultarId(id_Serventia);
		}		
		
		if (!boRetorno ){
			if (id_relator==null || id_relator.length() == 0){
				// Consulta dados da serventia
				
				id_relator = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_Serventia,areaRecursal.getId());
				if (id_relator == null){
					throw new MensagemException("Não foi possível encontrar um magistrado responsável para essa serventia (" + serventiaSegundoGrauDt.getServentia() + "), favor entrar em contato com o suporte.");
				}
			}
			processoResponsavelNe.salvarProcessoResponsavel(processoDt.getId(), id_relator, true, CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, logDt, obFabricaConexao);
		}
								
		boolean boPonteiroCriado = (new PonteiroLogNe()).temPonteiroCriado(processoDt.getId_AreaDistribuicaoRecursal(),processoDt.getId(),id_Serventia, id_relator);
		
		//se o processo já esteve no segundo grau não se deve acrescer o ponteiro, desde que o mesmo não tenha sido redistribuido para outra serventia ou relator
		if (!boPonteiroCriado){
			/* ---------- PONTEIRO ----------------*/
			///salvo o ponteiro			
			new PonteiroLogNe().salvar(new PonteiroLogDt(processoDt.getId_AreaDistribuicaoRecursal(),PonteiroLogTipoDt.DISTRIBUICAO,processoDt.getId(),id_Serventia,UsuarioDt.SistemaProjudi, UsuarioDt.SistemaProjudi, new Date(), id_relator  ),obFabricaConexao );
			/* ---------- PONTEIRO ----------------*/
		}
				
        recursoDt.setProcessoTipoCodigo(stProcessoTipoCodigo);
		recursoDt.setId_ServentiaRecurso(id_Serventia);
		recursoDt.setId_AreaDistribuicaoOrigem(processoDt.getId_AreaDistribuicao());
		
		//Salva Recurso
		this.salvar(recursoDt, obFabricaConexao);

		//Atualiza dados do processo
		processoDt.setId_Serventia(id_Serventia);
		processoDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.RECURSO)); //Muda fase do processo para Recurso
		//Se o processo que estiver subindo tiver PEDIDO DE LIMINAR ou ANTECIPAÇÃO DE TUTELA é preciso retira essa prioridade.
		String idProcessoPrioridadeAnterior = "";
		if(processoDt.isLiminar() || processoDt.isAntecipacaoTutela()){
			idProcessoPrioridadeAnterior = processoDt.getId_ProcessoPrioridade();
			processoDt.setId_ProcessoPrioridade("1");
			processoDt.setProcessoPrioridade("");
			processoDt.setProcessoPrioridadeCodigo("");
		}
		
		processoNe.alterarProcessoEnvioInstanciaSuperior(processoDt, id_ServentiaAnterior, faseAnterior, idProcessoPrioridadeAnterior, logDt, obFabricaConexao);
		
/*--------------------------
  ---- em reunião com a marcia, maria de fatima e ana claudia (29/11/2017 13:30), ficou acordado que não mais seriam remetidos os apensos à instancia superior.		
		//Verifica se o processo tem apenso. Se tiver, realiza a redistribuição dele.
		List listaProcessosApensos = processoNe.consultarProcessosApensosRedistribuicao(processoDt.getId());
		if(listaProcessosApensos != null && listaProcessosApensos.size() > 0){	
			List processoPassados = new ArrayList();
			processoPassados.add(processoDt.getId());
			processoNe.remessaApenso(listaProcessosApensos, usuarioDt.getId_Serventia(), usuarioDt.getGrupoCodigo(), usuarioDt.getId_UsuarioServentia(), id_Serventia, areaRecursal.getId(), serventiaSegundoGrauDt.getServentia(), processoDt.getProcessoNumeroCompleto(),id_relator,CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU,logDt, obFabricaConexao, processoPassados );					
		}
*/		
		
//		//Atualiza dados dos apensos (somente dos apensos)
//		for (int i = 0; listaApensos != null && i < listaApensos.size(); i++) {
//			ProcessoDt apensoDt = processoNe.consultarId(((ProcessoDt)listaApensos.get(i)).getId());
//			apensoDt.setId_Serventia(id_Serventia);
//			apensoDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.RECURSO)); //Muda fase do processo para Recurso
//			processoNe.alterarProcessoRecursoNaoOriginario(apensoDt, id_ServentiaAnterior, faseAnterior, idProcessoPrioridadeAnterior, logDt, obFabricaConexao);
//		}
		
		//Se for Câmara Cível gera movimentação específica
		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		ServentiaCargoDt novoResponsavelDt = serventiaCargoNe.consultarId(id_relator);
		stComplemento = stComplemento + " - Distribuído para: " + novoResponsavelDt.getNomeUsuario();

		movimentacaoDt = movimentacaoNe.gerarMovimentacaoAutosDistribuidos(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaSegundoGrauDt.getServentia() + stComplemento, logDt, obFabricaConexao);
		
		if ( serventiaSegundoGrauDt.isSegundoGrau() ) {				
//			// Gera movimentação AUTOS DISTRIBUÍDOS
//			movimentacaoDt = movimentacaoNe.gerarMovimentacaoRecursoDistribuido(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaSegundoGrauDt.getServentia() + stComplemento, logDt, obFabricaConexao);
//			// Gera a movimentação de distribuição nos apensos (somente dos apensos)
//			for (int i = 0; listaApensos != null && i < listaApensos.size(); i++) {
//				ProcessoDt apensoDt = processoNe.consultarId(((ProcessoDt)listaApensos.get(i)).getId());
//				movimentacaoDt = movimentacaoNe.gerarMovimentacaoAutosDistribuidos(apensoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaSegundoGrauDt.getServentia() + stComplemento + " - (Proc. apenso)", logDt, obFabricaConexao);
//			}
			//Gera pendência Verificar Distribuição para Distribuidor da Câmara
			new PendenciaNe().gerarPendenciaVerificarDistribuicao(processoDt, UsuarioServentiaDt.SistemaProjudi, movimentacaoDt.getId(), logDt, null, obFabricaConexao);
		}
//		} else {
//			// Gera movimentação AUTOS DISTRIBUÍDOS TURMA RECURSAL
//			movimentacaoDt = movimentacaoNe.gerarMovimentacaoRecursoDistribuido(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaSegundoGrauDt.getServentia() + stComplemento, logDt, obFabricaConexao);
//			// Gera a movimentação de distribuição nos apensos (somente dos apensos)
//			for (int i = 0; listaApensos != null && i < listaApensos.size(); i++) {
//				ProcessoDt apensoDt = processoNe.consultarId(((ProcessoDt)listaApensos.get(i)).getId());
//				movimentacaoDt = movimentacaoNe.gerarMovimentacaoAutosDistribuidos(apensoDt.getId(), UsuarioServentiaDt.SistemaProjudi, serventiaSegundoGrauDt.getServentia() + stComplemento + " - (Proc. apenso)", logDt, obFabricaConexao);
//			}
//		}
	}
	
	/**
	 * Método que transforma um processo em recurso, continuação do processo de 1º grau em uma instância superior
	 * @param processoDt
	 * @param logDt
	 * @param obFabricaConexao
	 * 
	 * @author lsbernardes
	 */
	public void converterProcessoRecurso(ProcessoDt processoDt, UsuarioDt usuarioDt, LogDt logDt) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		ServentiaNe serventiaNe = new ServentiaNe();
		AreaDistribuicaoDt areaRecursal = null;
		String stProcessoTipoCodigo = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			//consutlar serventia anterior por meio do serventia do usuário que fez a última movimentação de redistribuição do processo
			String id_ServentiaAnterior = serventiaNe.consultarServentiaAteriorProcesso(processoDt.getId(), processoDt.getId_Serventia(), obFabricaConexao);
			
			if (id_ServentiaAnterior != null && id_ServentiaAnterior.length()>0) {
			
				ServentiaDt serventiaAterior = serventiaNe.consultarId(id_ServentiaAnterior, obFabricaConexao);
				String faseAnterior = processoDt.getProcessoFaseCodigo(); // vai ser a mesma
	
				//Recurso é gerado
				RecursoDt recursoDt = new RecursoDt();
				recursoDt.setId_Processo(processoDt.getId());
				recursoDt.setId_ServentiaOrigem(serventiaAterior.getId());
				recursoDt.setId_UsuarioLog(logDt.getId_Usuario());
				recursoDt.setIpComputadorLog(logDt.getIpComputador());
				
				//consulta a área distribuição recursal vinculada a serventia anterior do processo
				areaRecursal = new AreaDistribuicaoNe().consultarIdAreaDistribuicaoRecursal(serventiaAterior.getId_AreaDistribuicao(), obFabricaConexao);
				
				//pego o código tipo de ação
				if (Funcoes.StringToInt(serventiaAterior.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL
						|| Funcoes.StringToInt(serventiaAterior.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL
						|| Funcoes.StringToInt(serventiaAterior.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR
						|| Funcoes.StringToInt(serventiaAterior.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL
						|| Funcoes.StringToInt(serventiaAterior.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL
						|| Funcoes.StringToInt(serventiaAterior.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR
						|| Funcoes.StringToInt(serventiaAterior.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL)
					
					stProcessoTipoCodigo = verificarProcessoTipoRecursoInominado(serventiaAterior.getServentiaSubtipoCodigo());
				
				else
					stProcessoTipoCodigo = verificarProcessoTipoRecursoInominado(areaRecursal.getServentiaSubtipoCodigo());
				
	            recursoDt.setProcessoTipoCodigo(stProcessoTipoCodigo);
	            //É a serventia onde se encontra o processo no memento
				recursoDt.setId_ServentiaRecurso(processoDt.getId_Serventia());
	
				//Salva Recurso
				this.salvar(recursoDt, obFabricaConexao);
	
				//Atualiza dados do processo
				processoDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.RECURSO)); //Muda fase do processo para Recurso
				processoNe.alterarProcessoRecursoNaoOriginarioConversaoProcesso(processoDt, id_ServentiaAnterior, faseAnterior, logDt, obFabricaConexao);
				
				processoDt.setRecursoDt(recursoDt);
				
				new MovimentacaoNe().gerarMovimentacaoProcessoConvertidoRecurso(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), logDt.getIpComputador(), logDt.getId_UsuarioLog(), obFabricaConexao);
			} else {
				throw new MensagemException("Não é possível converter processo originário em recurso.");
			}
			
			obFabricaConexao.finalizarTransacao();
		
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();	
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

//	private List consultarConexaoRecurso(String id_areadistribuicao, String id_processotipo, List recorrente, List recorrido, FabricaConexao conexao){
//		List lisConexo = null;		
//	obPersistencia.setConexao(conexao);
//	lisConexo = obPersistencia.consultarConexaoRecurso( id_areadistribuicao, id_processotipo,  recorrente,  recorrido);
//		return lisConexo;
//	}

	/**
	 * Método verifica conexão de um recurso
	 * @param processoDt
	 * @param obFabricaConexao
	 * 
	 * @author jrcorrea
	 * @throws Exception 
	 */
	public String[] verificaConexaoRecurso(String id_areaDistribuicao, String id_processotipo, List recorrente, List recorrido, List listaOutrasPartes, FabricaConexao obFabricaConexao) throws Exception {
		List lisConexo = null;
		String[] dadosConexao = {null,null,null,null,null};
		ServentiaCargoDt serventiaCargoRelator = null;		
		ServentiaCargoDt serventiaCargoAssistente = null;
		
		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		
		//Antes de consultar a conexão, caso na lista de outras partes tiver alguém do tipo PACIENTE, ele deve ser incluído
		//na lista de recorrentes para busca de conexão.
		if(listaOutrasPartes != null && !listaOutrasPartes.isEmpty()) {
			for (int i = 0; i < listaOutrasPartes.size(); i++) {
				ProcessoParteDt procParteDt = (ProcessoParteDt) listaOutrasPartes.get(i);
				if (procParteDt.isPaciente()) {
					if(recorrente == null) {
						recorrente = new ArrayList();
					}
					recorrente.add(procParteDt);
				}
			}
		}
		
		if (recorrente==null || recorrente.size()==0) {
			throw new MensagemException("Informe pelo menos um Recorrente, para enviar o Processo a Instância Superior"); 
		}
		
		lisConexo = obPersistencia.consultarConexaoRecurso(id_areaDistribuicao, id_processotipo, recorrente, recorrido);

		if (lisConexo != null && lisConexo.size() > 0) {
			RecursoDt processoConexo = (RecursoDt) lisConexo.get(0);

			ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();

			// Se encontrou algum prevento, resgata a primeira serventia
			// encontrada para que o processo vá para a mesma
			String id_ServentiaPrevencao = processoConexo.getId_ServentiaRecurso();

			ServentiaDt serventiaSegundoGrauDt = new ServentiaNe().consultarId(id_ServentiaPrevencao);

			if (ServentiaSubtipoDt.isSegundoGrau(serventiaSegundoGrauDt.getServentiaSubtipoCodigo())){
				// Consulta o Relator do processo prevento
				serventiaCargoRelator = processoResponsavelNe.consultarProcessoResponsavelRedator2Grau(processoConexo.getId(), obFabricaConexao);

//					// Consulta o Revisor do processo prevento
//					serventiaCargoRevisor = processoResponsavelNe.consultarProcessoResponsavelCargoTipo(processoPrevento.getId(), null, String.valueOf(CargoTipoDt.REVISOR_SEGUNDO_GRAU), fabricaConexao);
//
//					// Consulta o Vogal do processo prevento
//					serventiaCargoVogal = processoResponsavelNe.consultarProcessoResponsavelCargoTipo(processoPrevento.getId(), null, String.valueOf(CargoTipoDt.VOGAL_SEGUNDO_GRAU), fabricaConexao);

				// Verifica se processo já possui um assistente de gabinete
				// responsável
				serventiaCargoAssistente = processoResponsavelNe.getAssistenteGabineteResponsavelProcesso(processoConexo.getId(), serventiaCargoRelator.getId_Serventia(), obFabricaConexao);
			} else {
				// Consulta o Relator do processo conexo
				serventiaCargoRelator = processoResponsavelNe.getRelatorResponsavelProcesso(processoConexo.getId(), id_ServentiaPrevencao, obFabricaConexao);
			}

			dadosConexao = new String[5];
			dadosConexao[0] = id_ServentiaPrevencao;
			if (serventiaCargoRelator != null) dadosConexao[1] = serventiaCargoRelator.getId();				
			if (serventiaCargoAssistente != null) dadosConexao[2] = serventiaCargoAssistente.getId();
			dadosConexao[3] = processoConexo.getDataRetorno();
			dadosConexao[4] = processoConexo.getProcessoNumeroCompleto();
		}
		
		return dadosConexao;
	}

	/**
	 * Método que salva um recurso inominado, continuação do processo de 1º grau em uma instância superior
	 * @param processoDt
	 * @param obFabricaConexao
	 * 
	 * @author msapaula
	 */
	public void salvarAutuacaoRecurso(RecursoDt recursoDt, UsuarioDt usuarioDt) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			LogDt logDt = new LogDt(recursoDt.getId_UsuarioLog(), recursoDt.getIpComputadorLog());

			//Data de recebimento (autuação é armazenado)
			recursoDt.setDataRecebimento(Funcoes.DataHora(new Date()));
			this.alterarRecursoAutuacao(recursoDt, logDt, obFabricaConexao);

			if (recursoDt.getSemParte() != null && recursoDt.getSemParte().equalsIgnoreCase("1")){
				//Salva partes recursos
				RecursoParteNe recursoParteNe = new RecursoParteNe();
				recursoParteNe.inserirPromoventes(recursoDt.getProcessoDt().getListaPolosAtivos(), recursoDt.getId(), recursoDt.getId_ProcessoTipo(), obFabricaConexao);
				recursoParteNe.inserirPromovidos(recursoDt.getProcessoDt().getListaPolosPassivos(), recursoDt.getId(), recursoDt.getId_ProcessoTipo(), obFabricaConexao);
			} else {
				//Salva partes recorrentes e recorridas
				RecursoParteNe recursoParteNe = new RecursoParteNe();
				recursoParteNe.inserirRecorrentes(recursoDt.getListaRecorrentes(), recursoDt.getId(), recursoDt.getId_ProcessoTipo(), obFabricaConexao);
				recursoParteNe.inserirRecorridas(recursoDt.getListaRecorridos(), recursoDt.getId(), recursoDt.getId_ProcessoTipo(), obFabricaConexao);
			}

			//Salvando assuntos do recurso
			RecursoAssuntoNe recursoAssuntoNe = new RecursoAssuntoNe();
			recursoAssuntoNe.salvarRecursoAssunto(recursoDt.getListaAssuntos(), recursoDt.getId(), logDt, obFabricaConexao);

			// Gera movimentação RECURSO AUTUADO
			movimentacaoNe.gerarMovimentacaoRecursoAutuado(recursoDt.getProcessoDt().getId(), usuarioDt.getId_UsuarioServentia(), "(Recurso " + recursoDt.getProcessoTipo() + ")", logDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável em alterar os dados de um recurso inominado
	 * @param recursoDt: dados do recurso
	 * @param listaAssuntosAnterior: lista de assuntos do processos antes da modificação
	 * 
	 * @author msapaula
	 */
	public void alterarDadosRecurso(RecursoDt recursoDt, List listaAssuntosAnterior, String Id_UsuarioServentia) throws Exception {
		LogDt logDt = new LogDt(recursoDt.getId_UsuarioLog(), recursoDt.getIpComputadorLog());
		ProcessoNe processoNe = new ProcessoNe();
		AudienciaProcessoNe audienciaProcessoNe = new AudienciaProcessoNe();
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
		
			RecursoDt recursoBancoDt = this.consultarIdCompleto(recursoDt.getId(), obFabricaConexao);
			
			obFabricaConexao.iniciarTransacao();
			
			// Se houve alteração em assuntos, insere uma movimentação
			String listaAnterior = "";
			String listaAtual = "";
			
			for (int j = 0; j < listaAssuntosAnterior.size(); j++) {								
				listaAnterior = listaAnterior + ((RecursoAssuntoDt) listaAssuntosAnterior.get(j)).getId_Assunto();
			}
			
			for (int j = 0; j < recursoDt.getListaAssuntos().size(); j++) {								
				listaAtual = listaAtual + ((RecursoAssuntoDt) recursoDt.getListaAssuntos().get(j)).getId_Assunto();
			}
			
			if (!listaAnterior.equals(listaAtual)){			
				String complemento = "";
				(new MovimentacaoNe()).gerarMovimentacaoMudancaAssuntoProcesso(recursoDt.getProcessoDt().getId(), Id_UsuarioServentia, complemento, logDt, obFabricaConexao);
			}
									

			//Altera o processo tipo do recurso
			this.alterarProcessoTipoRecurso(recursoDt, logDt, Id_UsuarioServentia, true, obFabricaConexao);
			
			//Salva assuntos do recurso, inserindo os novos e excluindo os que não existem mais
			RecursoAssuntoNe recursoAssuntoNe = new RecursoAssuntoNe();
			recursoAssuntoNe.salvarAssuntosRecurso(recursoDt, listaAssuntosAnterior, logDt, obFabricaConexao);
				
			
			//Alterar dados do processo originario
			processoNe.alterarDadosProcessoRecurso(recursoDt.getProcessoDt(), Id_UsuarioServentia, obFabricaConexao);
			
			if (!recursoBancoDt.getId_ProcessoTipo().equalsIgnoreCase(recursoDt.getId_ProcessoTipo())) {
				AudienciaProcessoDt sessaoAberta = audienciaProcessoNe.consultarSessaoProcesso(recursoDt.getProcessoDt().getId(), recursoBancoDt.getId_ProcessoTipo(), obFabricaConexao);
				if (sessaoAberta != null) 
					audienciaProcessoNe.alterarProcessoTipoSessaoSegundoSegundoGrau(sessaoAberta, recursoDt.getId_ProcessoTipo(), logDt, obFabricaConexao);				
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método que verifica se determinado processo já esteve na turma anteriormente, e retorna a serventia desse
	 * para que o recurso vá para a mesma serventia.
	 * Caso o processo já tenha subido em recurso, o relator já estará como responsável no processo, não precisando mais
	 * adicionar esse.
	 * 
	 * @param processoDt identificação do processo
	 * @param conexao conexão ativa com o banco
	 * 
	 * @author msapaula
	 */
	public String getServentiaRecursoAnterior(String id_AreaDistribuicaoRecursal, String id_processo, FabricaConexao obFabricaConexao) throws Exception {
		String id_turma = null;
		
		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		id_turma = obPersistencia.getServentiaRecursoAnterior(id_AreaDistribuicaoRecursal, id_processo);
		
		return id_turma;
	}

	/**
	 * Método que verifica se determinado processo tem algum processo dependente em recurso
	 * para que o recurso vá para a mesma serventia.
	 * 
	 * @param id_processo identificação do processo
	 * @param id_areadistribuição id da area de distribuição
	 * @param conexao conexão ativa com o banco
	 * 
	 * @author jrcorrea
	 */
	public String getServentiaRecursoDependente(String id_processo, String id_ProcessoDependente, String id_areaDistribuicao, FabricaConexao obFabricaConexao) throws Exception {
		String id_Serventia = null;
		
		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		id_Serventia = obPersistencia.getServentiaRecursoDependente(id_processo, id_ProcessoDependente, id_areaDistribuicao );
		
		return id_Serventia;
	}
	
	/**
	 * Método que verifica se existe um recurso ativo para o processo passado
	 * 
	 * @param id_Processo, identificação do processo
	 * 
	 * @author msapaula
	 */
	public String getRecursoAtivo(String id_Processo, String id_Serventia, FabricaConexao obFabricaConexao) throws Exception {
		String id_Recurso = null;
		
		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		id_Recurso = obPersistencia.getRecursoAtivo(id_Processo,id_Serventia);
		
		return id_Recurso;
	}
	
	/**
	 * Método que verifica se existe um recurso ativo para o processo passado e retorna o id do recurso.
	 * @param id_Processo, identificação do processo
	 * 
	 * @author hmgodinho
	 */
	public String getRecursoAtivo(String id_Processo, String id_Serventia) throws Exception {
		String id_Recurso = null;
		FabricaConexao obFabricaConexao =null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());

			id_Recurso = obPersistencia.getRecursoAtivo(id_Processo,id_Serventia);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return id_Recurso;
	}

	/**
	 * Método responsável em gravar no banco os dados (inserção ou alteração)
	 */
	public void salvar(RecursoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("")) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("Recurso", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("Recurso", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

	}

	/**
	 * Consulta os recursos para autuar em uma turma recursal
	 * 
	 * @param id_serventia, identificação da serventia para localizar os recursos
	 * @param processoNumero, filtro para número de processo
	 * @param posicao, utilizada na paginação
	 * 
	 * @author msapaula
	 */
	public List consultarRecursosAutuar(String id_Serventia, String processoNumero, String posicao) throws Exception {
		List tempList = null;
		String digitoVerificador = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());

			//Divide número de processo do dígito verificador
			String[] stTemp = processoNumero.split("\\.");
			if (stTemp.length >= 1) processoNumero = stTemp[0];
			else processoNumero = "";
			if (stTemp.length >= 2) digitoVerificador = stTemp[1];

			tempList = obPersistencia.consultarRecursosAutuar(id_Serventia, processoNumero, digitoVerificador, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public String consultarRecursosAutuarJSON(String id_Serventia, String processoNumero, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		String digitoVerificador = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());			

			//Divide número de processo do dígito verificador
			String[] stTemp2 = processoNumero.split("\\.");
			if (stTemp2.length >= 1) processoNumero = stTemp2[0];
			else processoNumero = "";
			if (stTemp2.length >= 2) digitoVerificador = stTemp2[1];
			
			stTemp = obPersistencia.consultarRecursosAutuarJSON(id_Serventia, processoNumero, digitoVerificador, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	/**
	 * Método que verifica se há recurso (aguardando autuação) referente ao processo.
	 * @param idProcesso - ID do processo
	 * @param idServentia - ID da serventia onde corre o recurso
	 * @return true se tiver recurso e false se não tiver
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean existeRecursoNaoAutuadoProcesso(String idProcesso, String idServentia) throws Exception {
		boolean existeRecurso = false;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			existeRecurso = obPersistencia.existeRecursoNaoAutuadoProcesso(idProcesso,idServentia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return existeRecurso;
	}

	/**
	 * Consulta os dados completos de um recurso com suas partes
	 * 
	 * @param id_Recurso, identificação do recurso
	 * @author msapaula
	 */
	public RecursoDt consultarIdCompleto(String id_Recurso) throws Exception {
		RecursoDt dtRetorno = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdCompleto(id_Recurso);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consulta os dados completos de um recurso com suas partes
	 * 
	 * @param id_Recurso, identificação do recurso
	 * @param obFabricaConexao, conexão com o banco de dados
	 * @author msapaula
	 */
	public RecursoDt consultarIdCompleto(String id_Recurso, FabricaConexao obFabricaConexao) throws Exception {
		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		RecursoDt dtRetorno = obPersistencia.consultarIdCompleto(id_Recurso);
		obDados.copiar(dtRetorno);		
		return dtRetorno;
	}

	/**
	 * Consulta os dados completos de um recurso com suas partes e assuntos
	 * 
	 * @param id_Recurso, identificação do recurso
	 * @author msapaula
	 */
	public RecursoDt consultarDadosRecurso(String id_Recurso, boolean ehConsultaPublica) throws Exception {
		RecursoDt dtRetorno = null;
		FabricaConexao obFabricaConexao =null;
		try{
			obFabricaConexao = new FabricaConexao((ehConsultaPublica ? FabricaConexao.CONSULTA : FabricaConexao.PERSISTENCIA));
			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdCompleto(id_Recurso);

			//Captura lista de assuntos
			RecursoAssuntoNe recursoAssuntoNe = new RecursoAssuntoNe();
			dtRetorno.setListaAssuntos(recursoAssuntoNe.getAssuntosRecurso(id_Recurso));

			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public RecursoDt consultarDadosRecurso(String id_Recurso, FabricaConexao obFabricaConexao) throws Exception {
		RecursoDt dtRetorno = null;
		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarIdCompleto(id_Recurso);

		//Captura lista de assuntos
		RecursoAssuntoNe recursoAssuntoNe = new RecursoAssuntoNe();
		dtRetorno.setListaAssuntos(recursoAssuntoNe.getAssuntosRecurso(id_Recurso, obFabricaConexao));

		obDados.copiar(dtRetorno);
		return dtRetorno;
	}

	/**
	 * Método que efetua o retorno de um recurso à serventia de origem.
	 * 
	 * @param processoDt, processo que está sendo retornado à serventia de origem
	 * @param id_Movimentacao, movimentação que gerou o retorno à origem
	 * @param usuarioDt, usuário que está devolvendo processo à origem
	 * @param arquivos, lista de arquivos a ser vinculado com pendência "Verificar Processo" no 1º grau
	 * @param logDt, dados do log
	 * @param obFabricaConexao, conexão ativa
	 * 
	 * @author msapaula
	 */
	public void salvarRetornoServentiaOrigem(ProcessoDt processoDt, UsuarioDt usuarioDt, List arquivos, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoNe processoNe = new ProcessoNe();
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ServentiaDt serventiaRecursoDt = null;

		String id_ServentiaAnterior = processoDt.getId_Serventia();
		String faseAnterior = processoDt.getProcessoFaseCodigo();
		String classificadorAnterior = processoDt.getId_Classificador();
		
//		// Verifica se o processo tem apenso. Se tiver, junta o principal e apensos na mesma lista para posterior alteração. O primeiro
//		// registro sempre será o processo principal
//		List listaProcessosPrincipalApensos = new ArrayList();
//		listaProcessosPrincipalApensos.add(processoDt.getId());
//		List listaApensos = processoNe.consultarProcessosApensosRedistribuicao(processoDt.getId());
//		if (listaApensos != null) {
//			for (int i = 0; i < listaApensos.size(); i++) {
//				ProcessoDt apensoDt = (ProcessoDt) listaApensos.get(i);
//				listaProcessosPrincipalApensos.add(apensoDt.getId());
//				// consultando se o apenso possui mais apensos
//				List listaTemp = processoNe.consultarProcessosApensosRedistribuicao(apensoDt.getId());
//				if (listaTemp != null) {
//					listaApensos.addAll(listaTemp);
//				}
//			}
//		}

		RecursoDt recursoAtivo = processoDt.getRecursoDt();
		String dataRetorno = Funcoes.DataHora(new Date());
		this.alterarRecursoRetornoOrigem(recursoAtivo.getId(), dataRetorno, logDt, obFabricaConexao);

		ProcessoResponsavelNe processoResponsavelNe = new ProcessoResponsavelNe();
		// Ao retornar a origem deve-se desativados os responsaveis
		processoResponsavelNe.desativarResponsaveisProcesso(processoDt, obFabricaConexao);

//		for (int i = 0; i < listaProcessosPrincipalApensos.size(); i++) {
			
		//ProcessoDt processoLista = new ProcessoNe().consultarId(listaProcessosPrincipalApensos.get(i).toString());
		
		//Altera dados do processo
		processoDt.setId_Serventia(recursoAtivo.getId_ServentiaOrigem());
		processoDt.setId_AreaDistribuicao(recursoAtivo.getId_AreaDistribuicaoOrigem());
		processoDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.CONHECIMENTO));
		processoDt.setId_Classificador("null");
		
		//Repetindo as alterações acima no processoLista para que o apenso acompanhe o processo principal
//			processoLista.setId_Serventia(recursoInominado.getId_ServentiaOrigem());
//			processoLista.setId_AreaDistribuicao(recursoInominado.getId_AreaDistribuicaoOrigem());
//			processoLista.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.CONHECIMENTO));
//			processoLista.setId_Classificador("null");
		
		processoNe.alterarProcessoRetornoRecursoNaoOriginario(processoDt, id_ServentiaAnterior, faseAnterior, classificadorAnterior, logDt, obFabricaConexao);

		// Consulta dados da serventia
		
		serventiaRecursoDt = new ServentiaNe().consultarId(recursoAtivo.getId_ServentiaRecurso());
		ServentiaDt serventiaDestinoDt = new ServentiaNe().consultarId(recursoAtivo.getId_ServentiaOrigem());
		
		
		ServentiaCargoDt serv_cargo_Magistrado;
		
		//Ao retornar ao Segundo Grau, os responsáveis pelo recurso devem ser ativados		
		//reativando todos os responsáveis, exceto desembargador
		processoResponsavelNe.reativarResponsaveisProcesso(processoDt.getId(), serventiaDestinoDt.getId(), obFabricaConexao);
		
		if (serventiaDestinoDt.isSegundoGrau() || serventiaDestinoDt.isTurma() || serventiaDestinoDt.isUpjTurmaRecursal()){
			//Consultando o id do relator que está sendo reativado
			 serv_cargo_Magistrado = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoSegundoGrauInativo(processoDt.getId(), serventiaDestinoDt.getId(), obFabricaConexao);
			 
			//reativando desembargador responsável
			 if (serv_cargo_Magistrado == null){
				 serv_cargo_Magistrado = processoResponsavelNe.consultarServentiaCargoResponsavelProcessoSegundoGrauAtivo(processoDt.getId(), serventiaDestinoDt.getId(), obFabricaConexao);
			 }
			 try{				
				 processoResponsavelNe.reativarMagistradoResposavelProcesso(processoDt.getId(), serv_cargo_Magistrado.getId(), obFabricaConexao);
			 } catch(Exception e){
				 throw new MensagemException("Não foi possível encontrar um magistrado responsável para o processo, favor entrar em contato com o suporte.");
			 }
		}
		
		MovimentacaoDt movimentacaoDt = null;
		if (ServentiaSubtipoDt.isSegundoGrau(serventiaRecursoDt.getServentiaSubtipoCodigo())){
			// Gera movimentação AUTOS DEVOLVIDOS DO SEGUNDO GRAU
			movimentacaoDt = movimentacaoNe.gerarMovimentacaoAutosDevolvidosSegundoGrau(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, "", logDt, obFabricaConexao);
		} else {
			// Gera movimentação AUTOS DEVOLVIDOS DA TURMA RECURSAL
			movimentacaoDt = movimentacaoNe.gerarMovimentacaoAutosDevolvidosTurmaRecursal(processoDt.getId(), UsuarioServentiaDt.SistemaProjudi, "", logDt, obFabricaConexao);
		}
		
		//se o processo possuir movimentação do tipo não conhecido, deverá excluir os desembargadores e juiz do segundo grau.
		if (movimentacaoNe.possuiMovimentacaoNaoConhecido(processoDt.getId(), obFabricaConexao)){
			processoResponsavelNe.limparResponsaveisProcessoSegundoGrau(processoResponsavelNe.consultarResponsaveisProcessoSegundoGrau(processoDt.getId(), obFabricaConexao), obFabricaConexao);
		}
		
		//Gera pendência Verificar Processo para serventia de origem
		PendenciaNe pendenciaNe = new PendenciaNe();
		pendenciaNe.gerarPendenciaVerificarProcesso(processoDt.getId(), usuarioDt.getId_UsuarioServentia(), processoDt.getId_Serventia(), movimentacaoDt.getId(), arquivos, null, logDt, obFabricaConexao, processoDt.getId_ProcessoPrioridade());

	}

	/**
	 * Consulta a quantidade de processos para autuar em uma turma recursal.
	 * Esse método é utilizado na consulta feita na página inicial dos usuários internos.
	 * 
	 * @param id_Serventia identificação da serventia
	 * @author msapaula
	 */
	public int consultarQuantidadeRecursosAutuar(String id_Serventia) throws Exception {
		int qtde = 0;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			qtde = obPersistencia.consultarQuantidadeRecursosAutuar(id_Serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return qtde;
	}

	/**
	 * Atualiza os dados de um recurso em virtude da autuação do mesmo e gera log da alteração
	 * 
	 * @param id_Recurso identificação do recurso
	 * @param conexao conexão ativa
	 * 
	 * @author msapaula
	 */
	public void alterarRecursoAutuacao(RecursoDt recursoDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		
		RecursoDt recursoDtAnterior = obPersistencia.consultarId(recursoDt.getId());
		String Id_Processo_TipoAnterior = "";
		if (recursoDtAnterior != null) Id_Processo_TipoAnterior = recursoDtAnterior.getId_ProcessoTipo();
		
		String valorAtual = "[Id_Recurso:" + recursoDt.getId() + ";DataRecebimento:" + ";Id_Processo_Tipo:" + Id_Processo_TipoAnterior + "]";
		String valorNovo = "[Id_Recurso:" + recursoDt.getId() + ";DataRecebimento:" + recursoDt.getDataRecebimento()  + ";Id_Processo_Tipo:" + recursoDt.getId_ProcessoTipo() + "]";		
		
		obLogDt = new LogDt("Recurso", recursoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.AutuacaoRecurso), valorAtual, valorNovo);
		obPersistencia.alterarRecursoAutuacao(recursoDt.getId(), recursoDt.getDataRecebimento(), recursoDt.getId_ProcessoTipo());

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}
	
	/**
	 * Atualiza os dados de um recurso em virtude da autuação do mesmo e gera log da alteração
	 * 
	 * @author lsbernardes
	 * @author mmgomes (Alteração: Incluir mais de um recurso (classe/processo Tipo) por processo/recurso.
	 */
	public void alterarProcessoTipoRecurso(RecursoDt recursoDt, LogDt logDt, String Id_UsuarioServentia, boolean validaRecursoJaExsistente, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		RecursoParteNe recursoParteNe = new RecursoParteNe();
					
		// Dados anteriores do processo
		obDados = this.consultarId(recursoDt.getId(), obFabricaConexao);
		
		// Se houve alteração na classe, devemos verificar se existe um recurso parte ativo que já utiliza essa classe 
		if (validaRecursoJaExsistente && !obDados.getId_ProcessoTipo().trim().equalsIgnoreCase(recursoDt.getId_ProcessoTipo().trim())){		
			if (recursoParteNe.existeRecursoParteAtivoNaClasse(recursoDt.getId(), recursoDt.getId_ProcessoTipo(), obFabricaConexao))	
				throw new MensagemException("Já existe um outro recurso ativo utilizando a nova classe informada: " + recursoDt.getProcessoTipo() + ".");			
		}
		
		String valorAtual = "[Id_Recurso:" + recursoDt.getId() + ";Id_ProcessoTipo:" + obDados.getId_ProcessoTipo() + "]";
		String valorNovo = "[Id_Recurso:" + recursoDt.getId() + ";Id_ProcessoTipo:" + recursoDt.getId_ProcessoTipo() + "]";

		obLogDt = new LogDt("Recurso", recursoDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
		
		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		obPersistencia.alterarProcessoTipoRecurso(recursoDt.getId(), recursoDt.getId_ProcessoTipo());
		
		// Se houve alteração na classe, insere uma movimentação e altera as classes das partes no recurso
		if (validaRecursoJaExsistente && !obDados.getId_ProcessoTipo().trim().equalsIgnoreCase(recursoDt.getId_ProcessoTipo().trim())){
			
			List<RecursoParteDt> listaDePartes = recursoParteNe.consultar(recursoDt.getId(), obDados.getId_ProcessoTipo(), obFabricaConexao);
			for(RecursoParteDt recursoParteDt : listaDePartes) {
				recursoParteDt.setId_ProcessoTipo(recursoDt.getId_ProcessoTipo());
				recursoParteDt.setId_UsuarioLog(logDt.getId_UsuarioLog());
				recursoParteDt.setIpComputadorLog(logDt.getIpComputador());
				recursoParteNe.alterar(recursoParteDt, obFabricaConexao);
			}
			
			String complemento = String.format("Houve uma mudança da classe \"%s-%s\" para a classe \"%s-%s\" no Recurso Principal", obDados.getId_ProcessoTipo().trim(), obDados.getProcessoTipo(), recursoDt.getId_ProcessoTipo().trim(), recursoDt.getProcessoTipo());
			(new MovimentacaoNe()).gerarMovimentacaoMudancaClasseProcesso(recursoDt.getProcessoDt().getId(), Id_UsuarioServentia, complemento, logDt, obFabricaConexao);
		}	
		
		
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public RecursoDt consultarId(String id_recurso, FabricaConexao obFabricaConexao) throws Exception {

		RecursoDt dtRetorno = null;

		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarId(id_recurso);
		obDados.copiar(dtRetorno);
		
		return dtRetorno;
	}

	/**
	 * Atualiza os dados de um recurso em virtude do retorno à serventia de origem e gera log da alteração
	 * 
	 * @param id_Recurso, identificação do recurso
	 * @param dataRetorno, data de retorno à origem
	 * @param logDt, objeto de log
	 * @param conexao conexão ativa
	 * 
	 * @author msapaula
	 */
	public void alterarRecursoRetornoOrigem(String id_Recurso, String dataRetorno, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		String valorAtual = "[Id_Recurso:" + id_Recurso + ";DataRetorno:;]";
		String valorNovo = "[Id_Recurso:" + id_Recurso + ";DataRetorno:" + dataRetorno + "]";

		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("Recurso", id_Recurso, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.RetornoRecurso), valorAtual, valorNovo);
		obPersistencia.alterarRecursoRetornoOrigem(id_Recurso, dataRetorno);

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	/**
	 * Realiza chamado a ProcessoNe realizar a consulta
	 */
	public ProcessoDt consultarProcessoIdCompleto(String id_Processo) throws Exception {
		ProcessoDt processodt = null;
		ProcessoNe processoNe = new ProcessoNe();

		processodt = processoNe.consultarIdCompleto(id_Processo);
		processoNe = null;
		return processodt;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 * @throws Exception 
	 */
	public List getAssuntosProcesso(String id_Processo) throws Exception{
		List tempList = null;
		ProcessoAssuntoNe neObjeto = new ProcessoAssuntoNe();
		
		tempList = neObjeto.consultarAssuntosProcesso(id_Processo);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}

	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoAssunto(String tempNomeBusca, String id_AreaDistribuicao, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		
		tempList = neObjeto.consultarAssuntosAreaDistribuicao(id_AreaDistribuicao, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoAssuntosServentia(String tempNomeBusca, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
			
		List areasDistribuicoes = areaDistribuicaoNe.consultarAreasDistribuicaoServentia(id_Serventia);
		tempList = neObjeto.consultarAssuntosAreasDistribuicoes(areasDistribuicoes, tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Método que consulta os Tipos de Processo ligados à Serventia através de
	 * suas Áreas de Distribuição.
	 * 
	 * @param descricao
	 *            - descrição do tipo de processo
	 * @param idServentia
	 *            - ID da Serventia
	 * @param posicaoPaginaAtual
	 *            - posição da paginação
	 * @return lista de tipos de processo
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarProcessoTipoServentia(String descricao, String idServentia, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ProcessoTipoNe processoTipoNe = new ProcessoTipoNe();
		
		List listaAreaDistribuicao = this.consultarAreasDistribuicaoServentia(idServentia);
		if (!listaAreaDistribuicao.isEmpty()) {
			List listaIdsAreaDistribuicao = new ArrayList();
			for (int i = 0; i < listaAreaDistribuicao.size(); i++) {
				AreaDistribuicaoDt areaTemp = (AreaDistribuicaoDt) listaAreaDistribuicao.get(i);
				listaIdsAreaDistribuicao.add(areaTemp.getId());
			}
			tempList = processoTipoNe.consultarProcessoTipoServentia(descricao, listaIdsAreaDistribuicao, posicaoPaginaAtual);
			QuantidadePaginas = processoTipoNe.getQuantidadePaginas();
		}
	
		processoTipoNe = null;
		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoProcessoFase(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ProcessoFaseNe ProcessoFasene = new ProcessoFaseNe(); 
		tempList = ProcessoFasene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoFasene.getQuantidadePaginas();
		ProcessoFasene = null;

		return tempList;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 */
	public List consultarDescricaoProcessoPrioridade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ProcessoPrioridadeNe ProcessoPrioridadene = new ProcessoPrioridadeNe(); 
		tempList = ProcessoPrioridadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = ProcessoPrioridadene.getQuantidadePaginas();
		ProcessoPrioridadene = null;

		return tempList;
	}
	
	public String consultarDescricaoProcessoPrioridadeJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = "";
		ProcessoPrioridadeNe ProcessoPrioridadene = new ProcessoPrioridadeNe(); 
		stTemp = ProcessoPrioridadene.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		ProcessoPrioridadene = null;
		return stTemp;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuará a consulta
	 * @throws Exception 
	 */
	public List consultarDescricaoClassificador(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception{
		List tempList = null;
		ClassificadorNe neObjeto = new ClassificadorNe();
		
		tempList = neObjeto.consultarClassificadorServentia(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	/**
	 * Método que consulta as Áreas de Distribuição relacionadas à Serventia.
	 * 
	 * @param idServentia
	 *            - ID da Serventia
	 * @return lista de Áreas de Distribuição
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarAreasDistribuicaoServentia(String idServentia) throws Exception {
		List tempList = null;
		AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();

		tempList = areaDistribuicaoNe.consultarAreasDistribuicaoServentia(idServentia);
		areaDistribuicaoNe = null;
		return tempList;
	}

	/**
	 * Atualiza a serventia de um recurso em virtude de redistribuição e gera log com dados
	 * 
	 * @param id_Recurso, identificação do recurso
	 * @param id_ServentiaAnterior, serventia anterior
	 * @param id_ServentiaNova, nova serventia
	 * @param logDt, objeto com dados do log
	 * @param conexao conexão ativa
	 * 
	 * @author msapaula
	 */
	public void alterarServentiaRecurso(String id_Recurso, String id_ServentiaAnterior, String id_ServentiaNova, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		String valorAtual = "[Id_Recurso:" + id_Recurso + ";Id_ServentiaRecurso:" + id_ServentiaAnterior + "]";
		String valorNovo = "[Id_Recurso:" + id_Recurso + ";Id_ServentiaRecurso:" + id_ServentiaNova + "]";

		RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("Recurso", id_Recurso, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Redistribuicao), valorAtual, valorNovo);
		obPersistencia.alterarServentiaRecurso(id_Recurso, id_ServentiaNova);

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	public void ConverterRecursoProcesso(RecursoDt recursoDt, String id_usuarioServentia, String id_serventia, String ipComputador, String id_usuarioLog) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		MovimentacaoNe mov = new MovimentacaoNe();
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			 
			MovimentacaoDt movDt= mov.gerarMovimentacaoRecursoConvertidoProcesso(recursoDt.getId_Processo(),  id_usuarioServentia, ipComputador, id_usuarioLog, obFabricaConexao);
			String prioridadeProcesso = new ProcessoNe().getPrioridade(recursoDt.getId_Processo(), obFabricaConexao);
			// Gera pendência para a nova serventia Verificar Processo
			new PendenciaNe().gerarPendenciaVerificarProcesso(recursoDt.getId_Processo(), id_usuarioServentia, id_serventia, movDt.getId(), null, null,  id_usuarioLog, ipComputador, obFabricaConexao, prioridadeProcesso);
			     
			String valorAtual = "[Id_Processo:" +recursoDt.getId_Processo() + ";Id_Recurso:" + recursoDt.getId() + "]";
			String valorNovo = "[Id_Processo:" +recursoDt.getId_Processo() + ";Id_Recurso:"+ "]";

			LogDt obLogDt = new LogDt("Processo", recursoDt.getId_Processo(), id_usuarioLog, ipComputador, String.valueOf(LogTipoDt.ConverterRecursoProcesso), valorAtual, valorNovo);
			
			obPersistencia.CancelarRecurso(recursoDt.getId());
			obLog.salvar(obLogDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			 obFabricaConexao.fecharConexao();
		}		
		
	}
	
	public void ConverterRecursoAutuadoProcesso(RecursoDt recursoDt, String id_usuarioServentia, String id_serventia, String ipComputador, String id_usuarioLog) throws Exception {
		
		FabricaConexao obFabricaConexao = null;
		MovimentacaoNe mov = new MovimentacaoNe();
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			
			// Gera movimententação no Processo
			MovimentacaoDt movDt= mov.gerarMovimentacaoRecursoConvertidoProcesso(recursoDt.getId_Processo(),  id_usuarioServentia, ipComputador, id_usuarioLog, obFabricaConexao);
			String prioridadeProcesso = new ProcessoNe().getPrioridade(recursoDt.getId_Processo(), obFabricaConexao);
			// Gera pendência para a nova serventia Verificar Processo
			new PendenciaNe().gerarPendenciaVerificarProcesso(recursoDt.getId_Processo(), id_usuarioServentia, id_serventia, movDt.getId(), null, null,  id_usuarioLog, ipComputador, obFabricaConexao, prioridadeProcesso);
			// Exclui assuntos vinculados ao recurso
			new RecursoAssuntoNe().excluirRecursoAssunto(recursoDt.getId(), obFabricaConexao);
			// Exclui partes  vinculadas ao recurso
			new RecursoParteNe().excluirRecursoPartes(recursoDt.getId(), obFabricaConexao);
			
			String valorAtual = "[Id_Processo:" +recursoDt.getId_Processo() + ";Id_Recurso:" + recursoDt.getId() + "]";
			String valorNovo = "[Id_Processo:" +recursoDt.getId_Processo() + ";Id_Recurso:"+ "]";

			LogDt obLogDt = new LogDt("Processo", recursoDt.getId_Processo(), id_usuarioLog, ipComputador, String.valueOf(LogTipoDt.ConverterRecursoProcesso), valorAtual, valorNovo);

			obPersistencia.excluirRecurso(recursoDt.getId() );
			obLog.salvar(obLogDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			 obFabricaConexao.fecharConexao();
		}		
		
	}
	
	/**
	 * Método para consultar recurso mais novo do processo.
	 * @param String idProcesso
	 * @return String idRecurso
	 * @throws Exception
	 */
	public String consultarRecursoMaisNovo(String idProcesso) throws Exception {
		String retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new  RecursoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarRecursoMaisNovo(idProcesso);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para consultar recurso mais novo do processo.
	 * @param String idProcesso
	 * @param FabricaConexao obFabricaConexao
	 * @return String idRecurso
	 * @throws Exception
	 */
	public String consultarRecursoMaisNovo(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		String retorno = null;
		
		RecursoPs obPersistencia = new  RecursoPs(obFabricaConexao.getConexao());
		
		retorno = obPersistencia.consultarRecursoMaisNovo(idProcesso);
		
		return retorno;
	}
	
	/**
	 * Método para consultar recurso ativo mais antigo de um processo.
	 * @param String idProcesso
	 * @return String idRecurso
	 * @throws Exception
	 */
	public String consultarRecursoAtivoMaisAntigo(String idProcesso) throws Exception {
		String retorno = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new  RecursoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultarRecursoAtivoMaisAntigo(idProcesso);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}
	
	/**
	 * Método para verificar se recurso já foi autuado
	 * @param String idProcesso
	 * @param String idServentiaRecurso
	 * @return boolean retorno
	 * @throws Exception
	 */
	public boolean isRecursoAutuado(String idProcesso, String idServentiaRecurso) throws Exception {
		boolean retorno = false;
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			RecursoPs obPersistencia = new  RecursoPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.isRecursoAutuado(idProcesso, idServentiaRecurso);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return retorno;
	}

	public String consultarAssuntosAreaDistribuicaoJSON(String stNomeBusca1, String codigoCNJ, String id_AreaDistribuicao, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();		

		stTemp = neObjeto.consultarAssuntosAreaDistribuicaoJSON(id_AreaDistribuicao, codigoCNJ, stNomeBusca1, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarAssuntosServentiaJSON(String stNomeBusca1, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ServentiaSubtipoAssuntoNe neObjeto = new ServentiaSubtipoAssuntoNe();		

			stTemp = neObjeto.consultarAssuntosServentiaJSON(stNomeBusca1, id_Serventia, posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String consultarDescricaoProcessoFaseJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ProcessoFaseNe ProcessoFasene = new ProcessoFaseNe(); 
		stTemp = ProcessoFasene.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		ProcessoFasene = null;
		
		return stTemp;
	}

	public String consultarDescricaoClassificadorJSON(String tempNomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception{
		String stTemp = "";
		ClassificadorNe neObjeto = new ClassificadorNe();

		stTemp = neObjeto.consultarClassificadorServentiaJSON(tempNomeBusca, posicaoPaginaAtual, id_Serventia);
		neObjeto = null;
		return stTemp;
	}

	public String consultarDescricaoProcessoTipoJSON(String stNomeBusca1, String id_serventia, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ProcessoTipoNe neObjeto = new ProcessoTipoNe();
		
		stTemp = neObjeto.consultarProcessoTipoServentiaJSON(stNomeBusca1, id_serventia , posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}

	public long consultarQuantidadeRecursos(String id_Serventia) throws Exception {
		long loQuantidade = 0;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			loQuantidade = obPersistencia.consultarQuantidadeRecursos(id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return loQuantidade;
	}

	public boolean isExisteRecurso(String id_Processo, String id_Serventia) throws Exception {
		boolean boExiste =false;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			boExiste = obPersistencia.isExisteRecurso(id_Processo,id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boExiste;
	}
	
	/**
	 * Consulta recursos por tipo de processo
	 * 
	 * @param id_Proc, identificação do processo
	 * @param id_ProcessoTipo, identificação do tipo do processo
	 * @author alsqueiroz
	 */
	public RecursoDt consultarRecursoPorProcessoTipo(String id_Proc, String id_ProcessoTipo) throws Exception {
		RecursoDt dtRetorno = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			RecursoPs obPersistencia = new RecursoPs(obFabricaConexao.getConexao());
			
			dtRetorno = obPersistencia.consultarRecursoPorProcessoTipo(id_Proc, id_ProcessoTipo);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

}
