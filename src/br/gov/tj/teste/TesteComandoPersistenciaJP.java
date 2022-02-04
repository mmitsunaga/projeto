package br.gov.tj.teste;


public class TesteComandoPersistenciaJP {
	
//	private static void inicieAmbiente() throws Exception{		
//		OracleDataSource dataSourceOracle = new OracleDataSource();
//		dataSourceOracle.setUser("PROJUDI");
//		dataSourceOracle.setPassword("oracle");
//		dataSourceOracle.setURL("jdbc:oracle:thin:@127.0.0.1:1521/projudi_online.tjgo.gov");
//				
//		ConexaoBD.setDatasource(dataSourceOracle);
//	}
//	
//	public static void main(String[] args) {
//		FabricaConexao fabricaconexao = null;
//		try{
//			inicieAmbiente();
//			fabricaconexao = new FabricaConexao();	
//			
////			ArrecadacaoCustaPsTest(fabricaconexao);
////			AudienciaTipoPsTest(fabricaconexao);
////			AudienciaProcessoPsTest(fabricaconexao);
////			AudienciaProcessoResponsavelPsTest(fabricaconexao);
////			AudienciaProcessoStatusPsTest(fabricaconexao);
////			CustaValorPsTest(fabricaconexao);
////			CidadePsTest(fabricaconexao);
////			CidadeSSPPsTest(fabricaconexao);
////			ClassificadorPsTest(fabricaconexao);
////			ComarcaCidadePsTest(fabricaconexao);
////			ComutacaoTransitoJulgadoPsTest(fabricaconexao);
////			CustaPsTest(fabricaconexao);
////			EventoRegimePsTest(fabricaconexao);
////			EstatisticaMovimentacaoPsTest(fabricaconexao);
////			EstatisticaProcessoServentiaPsTest(fabricaconexao);
////			EstatisticaProdutividadeItemPsTest(fabricaconexao);
////			EstatisticaRelatorioEstProItensPsTest(fabricaconexao);
////			GuiaTipoPsTest(fabricaconexao);
////			GrupoMovimentacaoTipoPsTest(fabricaconexao);
////			GrupoPendenciaTipoPsTest(fabricaconexao);
////			GrupoPsTest(fabricaconexao);
////			MovimentacaoTipoPsTest(fabricaconexao);
////			ModeloPsTest(fabricaconexao);
////			PrazoSuspensoPsTest(fabricaconexao);
////			PropriedadePsTest(fabricaconexao);
////           PendenciaArquivoPsTest(fabricaconexao);
////			PendenciaTipoPsTest(fabricaconexao);
////			PermissaoPsTest(fabricaconexao);
////			ProcessoFasePsTest(fabricaconexao);
////			ProcessoNumeroPsTest(fabricaconexao);
////			ProcessoParteAusenciaPsTest(fabricaconexao);
////			ProcessoParteDebitoPsTest(fabricaconexao);
////			ProcessoParteTipoPsTest(fabricaconexao);
////			ProcessoPrioridadePsTest(fabricaconexao);
////			ProcessoStatusPsTest(fabricaconexao);
////			ProcessoTipoProcessoSubtipoPsTest(fabricaconexao);
////			ProcessoTipoPsTest(fabricaconexao);
////			ProfissaoPsTest(fabricaconexao);			
////			ProjetoParticipantePsTest(fabricaconexao);
//			RelatorioImplantacaoPsTest(fabricaconexao);
////			RecursoAssuntoPsTest(fabricaconexao);
////			RecursoPartePsTest(fabricaconexao);
////			RecursoPsTest(fabricaconexao);
////			ServentiaPsTest(fabricaconexao);
////			ServentiaSubtipoProcessoTipoPsTest(fabricaconexao);
////			TarefaPsTest(fabricaconexao);
////			UsuarioServentiaOabPsTest(fabricaconexao);
////			UsuarioArquivoPsTest(fabricaconexao);
////			UsuarioServentiaEscalaStatusHistoricoPsTest(fabricaconexao);
////			UsuarioServentiaGrupoPsTest(fabricaconexao);
////			testeGuiaItemPs(fabricaconexao);
//		}catch( Exception ex){
//			ex.printStackTrace();
//		}finally{
//			if(fabricaconexao != null) try{
//				fabricaconexao.fecharConexao();
//			} catch(Exception e) {				
//			}
//		}
//	}
//	
//	/*
//	private static void teste(FabricaConexao fabricaconexao) throws Exception{		
//		
//		ps.setConexao(fabricaconexao);
//	}
//	*/
//	
//	private static void testeGuiaItemPs(FabricaConexao fabricaconexao) throws Exception{		
//		GuiaItemPs ps = new GuiaItemPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultaItensGuia("25/10/2010");
//		
//		GuiaItemDt guiaItemDt = new GuiaItemDt();
//		GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();		
//		guiaEmissaoDt.setCodigoOficialSPGAvaliador("10");
//				
//		guiaItemDt.setGuiaItem("1");
//		guiaItemDt.setCustaDt(new CustaDt());
//		guiaItemDt.getCustaDt().setId(String.valueOf(CustaDt.CUSTA_AVALIADOR));
//		guiaItemDt.setGuiaItemCodigo("1");
//		guiaItemDt.setQuantidade("2");
//		guiaItemDt.setValorCalculado("10.25");
//		guiaItemDt.setParcelaCorrente("1");
//		
//		ps.salvar(guiaItemDt, "1", guiaEmissaoDt);
//	}
//	
//	
//	private static void UsuarioServentiaGrupoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		UsuarioServentiaGrupoPs ps = new UsuarioServentiaGrupoPs();
//		ps.setConexao(fabricaconexao);
//		
////		ps.consultarDescricao("Ana","0");
////		
////		ps.consultarServentiasGrupos("155");
////		
////		ps.consultarUsuarioServentiaCargo("13", "9", "ana", "0");
////		
////		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
////		usuarioServentiaGrupoDt.setId_Grupo("1");
////		usuarioServentiaGrupoDt.setId_Serventia("1");
////		usuarioServentiaGrupoDt.setId_Usuario("1");
////		ps.verificarHabilitacaoUsuario(usuarioServentiaGrupoDt);
////		
////		ps.consultarIdUsuarioServentiaGrupo("1", "1", "1");
////		
////		ps.alterarStatusUsuarioServentiaGrupo("1", 1);
//		
//		ps.alterarStatusTodosUsuarioServentiaGrupo("1", 1);
//		
//		ps.consultarServentiasGruposServidorJudiciario("1");
//		
//		UsuarioNe UsuarioSessao = new UsuarioNe();
//		UsuarioSessao.consultaUsuarioSenha("jrcorrea", "12345");
//		UsuarioSessao.getUsuarioDt().setDataEntrada("05/04/2011");		
//		UsuarioSessao.getUsuarioDt().setIpComputadorLog("127.0.0.1");
//		List tempList = UsuarioSessao.consultarServentiasGrupos();	
//		UsuarioDt Usuariodt = ((UsuarioDt) tempList.get(0));		
//		UsuarioSessao.validarAcessoAssistentes(Usuariodt);
//		UsuarioSessao.setObdtUsuario(Usuariodt);	
//		UsuarioSessao.getMenu();
//		
//		ps.consultarTodosUsuarioServentiaGrupo(UsuarioSessao);
//	}
//	
//	private static void UsuarioServentiaEscalaStatusHistoricoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		UsuarioServentiaEscalaStatusHistoricoPs ps = new UsuarioServentiaEscalaStatusHistoricoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.inserir("10", "1");
//	}
//	
//	private static void UsuarioArquivoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		UsuarioArquivoPs ps = new UsuarioArquivoPs();
//		ps.setConexao(fabricaconexao);
//		
//		String id_UsuarioArquivo = "1";
//		String id_Usuario = "151";
//		
//		ps.consultarIdCompleto(id_UsuarioArquivo);
//		
//		ps.consultarArquivosUsuario(id_Usuario);
//	}
//	
//	private static void UsuarioServentiaOabPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		UsuarioServentiaOabPs ps = new UsuarioServentiaOabPs();
//		ps.setConexao(fabricaconexao);
//		
//		String oabNumero = "7849";
//		String oabComplemento = "N";
//		String oabUf = "GO"; 
//		int codigoInstituicao = 8;
//		
//		ps.consultarAdvogado(oabNumero, oabComplemento, oabUf, codigoInstituicao);
//		
//		ps.consultarUsuarioServentiaOab(oabNumero, oabComplemento, oabUf);
//		
//		
//		
//	}
//	
//	private static void TarefaPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		TarefaPs ps = new TarefaPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.pesquisarTarefaAguardandoVisto("T", "1", "1", "1", "0");
//		
//		ps.pesquisarTarefaNaoFinalizada("T", "1", "0");
//		
//		ps.pesquisarTarefa("T", "1", "1", "1", "0");
//		
//		ps.pesquisarTarefaByParticipante("T", "1", "1", "1", "1", "0");
//		
//		TarefaDt dados = new TarefaDt();
//		dados.setTarefa("1");  
//		dados.setDescricao("Teste");  
//		dados.setResposta("Teste");  
//		dados.setDataInicio("10/03/1985");  
//		dados.setDataCriacao("10/03/1985");  
//		dados.setPrevisao("1");  
//		dados.setDataFim("11/03/1985");  
//		dados.setId_TarefaPai("1");  
//		dados.setPontosApf("10");  
//		dados.setPontosApg("10");  
//		dados.setId_TarefaPrioridade("1");  
//		dados.setId_TarefaStatus("1");  
//		dados.setId_TarefaTipo("2");  
//		dados.setId_Projeto("1");  
//		dados.setId_ProjetoParticipanteResponsavel("1");  
//		dados.setId_UsuarioCriador("1");  
//		dados.setId_UsuarioFinalizador("1");  		
//		ps.inserir(dados);
//		
//		ps.alterar(dados);
//		
//		ps.atribuirTarefaResponsavel("1", "1", "1");
//		
//		ps.buscarIdPrimeiraTarefaPendenteByProjeto("1");
//		
//	}
//	
//	private static void ServentiaSubtipoProcessoTipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ServentiaSubtipoProcessoTipoPs ps = new ServentiaSubtipoProcessoTipoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarProcessoTipoServentiaSubtipoGeral("1");
//		
//		ps.consultarProcessoTipos("1", "c", "0");
//		
//		ps.consultarDescricaoProcessoTipos("1", "c", "0");
//		
//		ps.consultarTiposProcesso(1);	
//		
//	}
//	
//	private static void ServentiaPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ServentiaPs ps = new ServentiaPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarId("1");
//		
//		ps.consultarDescricao("Vara", "0");
//		
//		ps.consultarIdSimples("1");
//		
//		ps.consultarServentiaDescricao("1");
//		
//		ps.consultarServentiaCodigo("1");
//		
//		ps.consultarServentiasAtivas("vara", "0", "16", "1", "11");
//		
//		ps.consultarServentiasAtivas("16");
//		
//		ps.consultarServentiasHabilitacaoAdvogado("Ordem", "0");
//		
//		ps.consultarServentiasHabilitacaoUsuarios("vara", "0", "16");
//		
//		UsuarioNe UsuarioSessao = new UsuarioNe();
//		UsuarioSessao.consultaUsuarioSenha("jrcorrea", "12345");
//		UsuarioSessao.getUsuarioDt().setDataEntrada("05/04/2011");		
//		UsuarioSessao.getUsuarioDt().setIpComputadorLog("127.0.0.1");
//		List tempList = UsuarioSessao.consultarServentiasGrupos();	
//		UsuarioDt Usuariodt = ((UsuarioDt) tempList.get(0));		
//		UsuarioSessao.validarAcessoAssistentes(Usuariodt);
//		UsuarioSessao.setObdtUsuario(Usuariodt);	
//		UsuarioSessao.getMenu();
//		
//		ps.consultarDescricaoServentia("vara", "16", UsuarioSessao.getUsuarioDt() , "0");
//		
//		ps.consultarServentiasAreaDistribuicao("1", "1", "16");
//		
//		ps.consultarServentiasRelacionadas("3");
//		
//		ps.consultarAreasDistribuicoesServentia("3");
//		
//		ps.consultarServentiasVaraTurma();
//		
//		ps.consultarServentiasVaraTurma("vara", "0");
//		
//		ps.consultarServentiasDistribuicaoProcesso();
//		
//		ps.consultarServentiaCodigoExterno("1");
//		
//		ps.consultarCodigoExterno("1001");
//		
//		ps.consultarServentiaTipoCodigo("3", "0");
//		
//		
//		
//	}
//	
//	private static void RecursoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		RecursoPs ps = new RecursoPs();
//		ps.setConexao(fabricaconexao);	
//			
//		RecursoDt dt = ps.consultarId("44");;
//		dt.setAssunto("bla bla bla");
//		ps.inserir(dt);
//		
//		ps.getServentiaRecursoAnterior("513","2053");
//		
//		ps.getRecursoAtivo("2561");
//		
//		ps.consultarRecursosAutuar("34", "457464", "42", "0");
//		
//		ps.consultarQuantidadeRecursosAutuar("42");
//		
//		ps.alterarRecursoAutuacao("44", Funcoes.DataHora(new Date(System.currentTimeMillis())));
//		
//		ps.alterarRecursoRetornoOrigem("45", Funcoes.DataHora(new Date(System.currentTimeMillis())));
//		
//		ps.alterarServentiaRecurso("44","34");
//				
//		ps.consultarIdCompleto("44");
//		
//		
//	}
//	
//	private static void RecursoPartePsTest(FabricaConexao fabricaconexao) throws Exception{		
//		RecursoPartePs ps = new RecursoPartePs();
//		ps.setConexao(fabricaconexao);
//		
//		RecursoParteDt dados = new RecursoParteDt();
//		dados.setId_Recurso("10");
//		dados.setId_ProcessoParte("700");
//		dados.setId_ProcessoParteTipo("1");
//		dados.setProcessoParteTipoCodigo("1");		
//		dados.setDataBaixa("10/07/1992 13:25:47");		
//		ps.inserir(dados);
//		
//		ps.alterar(dados);
//	}
//	
//	private static void RecursoAssuntoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		RecursoAssuntoPs ps = new  RecursoAssuntoPs();
//		ps.setConexao(fabricaconexao);
//		
//		String id_Recurso = "44"; 
//		
//		ps.getAssuntosRecurso(id_Recurso);		
//		
//	}
//	
//	private static void RelatorioImplantacaoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		RelatorioImplantacaoPs ps = new  RelatorioImplantacaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		String idComarca = "1";
//		String idServentiaTipo = "16";
//		
//		ps.relImplantacaoServentias(idComarca, idServentiaTipo);
//		
//		ps.relImplantacaoServentiasPublico(idServentiaTipo);
//	}
//	
//	private static void ProjetoParticipantePsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProjetoParticipantePs ps = new ProjetoParticipantePs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarDescricao("Te", "0");
//		
//		ps.consultarId_ParticipanteServentiaCargo("10");
//		
//		ProjetoParticipanteDt dados = new ProjetoParticipanteDt();
//		dados.setId_ServentiaCargo("397");
//		ps.inserir("1", dados);
//				
//	}
//	
//	private static void ProfissaoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProfissaoPs ps = new ProfissaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarCodigo("1");
//		
//		
//	}
//	
//	private static void ProcessoTipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoTipoPs ps = new ProcessoTipoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarDescricao("aç", "0");
//		
//		ps.getProcessoTipos();
//		
//		ps.consultarProcessoTipoCodigo("1");
//		
//		ps.consultarProcessoTipoDescricao("aça");
//		
//		ps.consultarCodigo("1");
//		
//		ps.consultarDescricao("aça");
//		
//		List listaAreaDistribuicao = new ArrayList(); 
//		listaAreaDistribuicao.add("20");
//		listaAreaDistribuicao.add("2");
//		listaAreaDistribuicao.add("6");
//		List lista = ps.consultarProcessoTipoServentia("aça", listaAreaDistribuicao, "0");
//		
//		
//	}
//	
//	private static void ProcessoTipoProcessoSubtipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoTipoProcessoSubtipoPs ps = new ProcessoTipoProcessoSubtipoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarProcessoTipoCodigoProcessoSubtipoCodigo(1052, 1);
//		
//		ps.consultarProcessoTipoCodigoProcessoSubtipoCodigo(99, 10);
//	}
//	
//	private static void ProcessoStatusPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoStatusPs ps = new ProcessoStatusPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarDescricao("a", "1");
//		
//		ps.getProcessoStatus();
//		
//		ps.consultarProcessoStatusCodigo(ProcessoStatusDt.ATIVO);	
//		
//	}
//	
//	private static void ProcessoPrioridadePsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoPrioridadePs ps = new ProcessoPrioridadePs();
//		ps.setConexao(fabricaconexao);
//		
//		String processoPrioridadeCodigo = "1";
//		
//		ps.consultarDescricao("Re", "1");
//		
//		ps.consultarProcessoPrioridadeCodigo(processoPrioridadeCodigo);		
//		
//	}
//	
//	private static void ProcessoParteTipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoParteTipoPs ps = new ProcessoParteTipoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));
//		
//		ps.consultarOutrosTiposPartes("tes", "1");	
//		
//	}
//	
//	private static void ProcessoParteDebitoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoParteDebitoPs ps = new  ProcessoParteDebitoPs();
//		ps.setConexao(fabricaconexao);
//		
//		String  id_ProcessoParteDebito = "51";
//		String nomeParte = "";
//		String cpfParte = "";
//		String posicao = "1";
//		
//		ps.consultarId(id_ProcessoParteDebito);
//		
//		ps.consultarDebitosProcessoParte(nomeParte, cpfParte, posicao);
//		
//	}
//	
//	private static void ProcessoParteAusenciaPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoParteAusenciaPs ps = new ProcessoParteAusenciaPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.alterarAusenciaProcessoParte("1025","1255");
//	}
//	
//	private static void ProcessoNumeroPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoNumeroPs ps = new ProcessoNumeroPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.limparTabela();
//		
//		ps.reiniciarTabela();
//	}
//	
//	private static void ProcessoFasePsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoFasePs ps = new ProcessoFasePs();
//		ps.setConexao(fabricaconexao);
//		
//		int processoFaseCodigo = 1;
//		
//		ps.consultarDescricao("Re", "1");
//		
//		ps.consultarProcessoFaseCodigo(processoFaseCodigo);	
//		
//	}
//	
//	private static void PermissaoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		PermissaoPs ps = new PermissaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarId("1");
//		
//		ps.consultarDescricao("em", "0");
//		
//		ps.consultarPermissoes();
//		
//		ps.consultarTodasPermissoes("1");
//		
//		ps.consultarTodasPermissoesUsuarioServentia("1");
//		
//		ps.consultarMenusUsuarioGrupo("1", "1");
//		
//		ps.consultarMenusEspecialUsuarioGrupo("1", "1", 1);
//		
//		ps.consultarCodigoDescricao("451", null, "0");
//		
//		ps.consultarFuncoesPermissao("2", "116");
//		
//		ps.consultarPermissaoCodigoMaior();
//		
//		ps.consultarPermissaoCodigo("100");
//		
//		
//	}
//	
//	private static void PendenciaTipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		PendenciaTipoPs ps = new PendenciaTipoPs();
//		ps.setConexao(fabricaconexao);
//		
//		int pendenciaTipoCodigo = 1;
//		String nomeBusca = "car";
//		
//		ps.consultarPendenciaTipoCodigo(pendenciaTipoCodigo);
//		
//		ps.consultarTiposPendenciaMovimentacao();
//		
//		ps.consultarPendenciaTipoDescricao(nomeBusca);
//		
//		
//	}
//	
//	private static void PendenciaArquivoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		PendenciaArquivoPs ps = new PendenciaArquivoPs();
//		ps.setConexao(fabricaconexao);
//		
//		List listaAux = ps.consultarPendencias("1035434");
//		
//		PendenciaDt pendenciaDt = (PendenciaDt)listaAux.get(0);
//		
////		ps.consultarPendenciaResposta(pendenciaDt, false);
////		
////		ps.consultarPendencias("1035434");
////		
////		ps.consultarArquivosRespostaConclusao("12558");		
//			
//		ps.consultarArquivosAssinados(pendenciaDt, true);
//		
//		ps.consultarPendencia(pendenciaDt, true);
//		
//		ps.consultarRespostaAssinados(pendenciaDt);
//		
//		ps.consultarResposta(pendenciaDt, true);
//		
//		UsuarioNe UsuarioSessao = new UsuarioNe();
//		UsuarioSessao.consultaUsuarioSenha("jrcorrea", "12345");
//		UsuarioSessao.getUsuarioDt().setDataEntrada("05/04/2011");		
//		UsuarioSessao.getUsuarioDt().setIpComputadorLog("127.0.0.1");
//		List tempList = UsuarioSessao.consultarServentiasGrupos();	
//		UsuarioDt Usuariodt = ((UsuarioDt) tempList.get(0));		
//		UsuarioSessao.validarAcessoAssistentes(Usuariodt);
//		UsuarioSessao.setObdtUsuario(Usuariodt);	
//		UsuarioSessao.getMenu();
//		
//		ps.consultarPendencia(pendenciaDt, UsuarioSessao, true, true, true);
//		
//		ps.getArquivoConfiguracaoPreAnalise("12558");
//		
//		ps.getArquivoPreAnaliseConclusao("12558");
//		
//		ps.consultaVotoDesembargador("1", "1");
//		
//		ps.verificaPreAnaliseConclusao("12558");
//		
//		//ps.consultarPreAnalisesConclusaoSimples("1", "5025563", "1", "1", "1");
//		
//		ps.consultarPreAnalisesConclusoesMultiplas("12", "5025563");
//		
//		ps.consultarPreAnalisesConclusaoFinalizadas("123", "225", "15/10/1978", "19/09/1980", "1", "1");
//		
//		ps.consultarArquivosResposta("12256");
//		
//		PendenciaArquivoDt pendenciaArquivoDt = new PendenciaArquivoDt();
//		pendenciaArquivoDt.setId_Pendencia("125");
//		ps.consultarArquivos(pendenciaArquivoDt);
//		
//		ps.consultarArquivosPendencia("125563");
//		
//		ps.consultarPendencias("1512");
//		
//		ps.alterarStatusPendenciaArquivo("122", "2");
//		
//		ps.consultarPreAnalisesSimples("12", "1", "1", "1", "1");
//		
//		ps.consultarPreAnalisesMultiplas("1", "1", "1");
//		
//		ps.getArquivoPreAnalise("12");
//		
//		ps.consultarPreAnalisesFinalizadas("1", "1", "1", "1", "10/05/1980", "10/05/1990");
//		
//		ps.consultarArquivosRespostaPendencia("1");
//		
//		ps.consultarPreAnalisesConclusaoSimplesPendentesAssinatura("1", "1", "1", "1", "1");
//		
//		ps.atualizeStatusPreAnalisesConclusaoSimplesParaPendentesAssinatura("1");
//		
//		ps.atualizeStatusPreAnalisesConclusaoSimplesParaNaoPendentesAssinatura("1");
//		
//		ps.consultarPreAnalisesSimplesPendentesAssinatura("1", "1", "1", "1", "1");
//		
//		ps.atualizeStatusPreAnalisesSimplesParaPendentesAssinatura("1");
//		
//		ps.atualizeStatusPreAnalisesSimplesParaNaoPendentesAssinatura("1");
//		
//		
//	}
//	
//	private static void PropriedadePsTest(FabricaConexao fabricaconexao) throws Exception{		
//		PropriedadePs ps = new PropriedadePs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.getPropriedades();
//	}
//	
//	private static void PrazoSuspensoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		PrazoSuspensoPs ps = new PrazoSuspensoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("a", "1");
//		Date data;
//		data = Funcoes.DataHora("1/1/2008 00:00:00");
//		boolean valida = ps.isDataValida(data, "1", "1", "1");
//		data = Funcoes.DataHora("1/1/2002 00:00:00");
//		boolean invalida = ps.isDataValida(data, "1", "1", "1");
//		
//	}
//	
//	private static void ModeloPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ModeloPs ps = new ModeloPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarModelos("cert", "0", "13", "42", true);
//		ps.consultarModelosUsuario("cert", "0", "25", "6");
//		ps.consultarModelosGrupo("cert", "0", "25", "6", "1");
//		ps.consultarModeloCodigoAcesso("1");
//		ps.consultarModeloCodigo("124");		
//	}
//	
//	private static void MovimentacaoTipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		MovimentacaoTipoPs ps = new MovimentacaoTipoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.AUDIENCIA_CONCILIACAO_MARCADA);
//	}
//	
//	private static void GrupoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		GrupoPs ps = new GrupoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("an", "0");
//		ps.consultarTodos();
//		ps.consultarCodigo(1);
//		ps.consultarGrupoCodigo("1");
//		ps.consultarGruposHabilitacaoServidores("an", "0");
//		ps.consultarGruposListaUsuarios("an", "0");
//	}
//	
//	private static void GrupoPendenciaTipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		GrupoPendenciaTipoPs ps = new GrupoPendenciaTipoPs();
//		ps.setConexao(fabricaconexao);
//		
//		String id_grupo = "4";
//		String descricao = "";
//		String grupoCodigo = "4";
//		String posicao = "1";
//		boolean limite = true;
//		
//		ps.consultarPendenciaTipoGrupoGeral(id_grupo);
//		
//		ps.consultarGrupoPendenciaTipo(descricao, grupoCodigo, posicao, limite);
//	}
//	
//	private static void GrupoMovimentacaoTipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		GrupoMovimentacaoTipoPs ps = new GrupoMovimentacaoTipoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarDescricao("ana", "0");
//		
//		ps.consultarMovimentacaoTipoGrupoGeral("16");
//		
//		ps.consultarGrupoMovimentacaoTipo("16", "Despacho", "0");	
//		
//	}
//	
//	private static void GuiaTipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		GuiaTipoPs ps = new GuiaTipoPs();
//		ps.setConexao(fabricaconexao);
//		
//		
//		String id_guiatipo = "1";
//		ps.consultarId(id_guiatipo);
//		
//		ps.consultarDescricao("1");
//		
//		ps.consultarDescricao("recurso", "1");
//		 
//		ps.consultarListaId_GuiaTipo("10");
//		
//	}
//	
//	private static void EstatisticaRelatorioEstProItensPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		EstatisticaRelatorioEstProItensPs ps = new EstatisticaRelatorioEstProItensPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarIdRelatorioEstPro("10");
//		
//		ps.excluirIdRelatorio("10");
//	}
//	
//	private static void EstatisticaProdutividadeItemPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		EstatisticaProdutividadeItemPs ps = new EstatisticaProdutividadeItemPs();
//		ps.setConexao(fabricaconexao);
//		
//		String descricao = "ce";
//		String posicao = "1";
//		
//		ps.listarEstatisticasProdutividadeItem(descricao);
//		
//		ps.consultarEstatisticaProdutividadeItem(descricao, posicao);
//		
//		
//	}
//	
//	private static void EstatisticaProcessoServentiaPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		EstatisticaProcessoServentiaPs ps = new EstatisticaProcessoServentiaPs();
//		ps.setConexao(fabricaconexao);
////		
////		ps.consultaQtdProcessosAtivos("3", "107");
////		
////		EstatisticaProcessoServentiaDt	estatisticaProcessoServentia = new EstatisticaProcessoServentiaDt();;
////		estatisticaProcessoServentia.setAno("2010");
////		DiaHoraEventos dh = new DiaHoraEventos();
////		dh.setAno(2011);
////		dh.setMes(10);
////		dh.setDia(19);
////		dh.setHora(13);
////		dh.setMinuto(00);
////		dh.setSegundo(10);		
////		estatisticaProcessoServentia.setDataFinalArquivamento(dh);
////		estatisticaProcessoServentia.setDataFinalDistribuicao(dh);
////		estatisticaProcessoServentia.setDataInicioArquivamento(dh);
////		estatisticaProcessoServentia.setDataInicioDistribuicao(dh);
////		estatisticaProcessoServentia.setDiasParalisados("10");
////		estatisticaProcessoServentia.setId_Serventia("1");
////		estatisticaProcessoServentia.setId_ProcessoTipo("1");
////		estatisticaProcessoServentia.setPrioridadeProcesso("1");
////		estatisticaProcessoServentia.setSegredoJustica("1");
////		estatisticaProcessoServentia.setFaseProcesso("2");
////		estatisticaProcessoServentia.setProcessoStatus("1");
////		
////		ps.consultaQtdProcessosEmSituacao(estatisticaProcessoServentia);
////		
////		ps.mediaDiasEmTramitacao(estatisticaProcessoServentia);
////		
////		ps.consultaQtdProcessosSuspensos(estatisticaProcessoServentia);
////		
////		ps.consultaQtdProcessosProcessosParalisados(estatisticaProcessoServentia);
//		
//		ps.consultaQtdProcessosAtivos("577", "96");
//		
//	}
//	
//	private static void EstatisticaMovimentacaoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		EstatisticaMovimentacaoPs ps = new EstatisticaMovimentacaoPs();
//		ps.setConexao(fabricaconexao);
//		EstatisticaMovimentacaoDt dt = new EstatisticaMovimentacaoDt();
//		dt.setId_Serventia("1");
//		dt.setDataInicial("01/01/2007");
//		dt.setDataFinal("01/05/2009");
//		ps.consultarDadosEstatisticaMovimentacaoServentia(dt);
//		
//		ps.consultarDadosEstatisticaMovimentacaoServentia(dt);
//		
//		ps.consultarDadosEstatisticaMovimentacaoUsuario(dt);
//		
//		ps.consultarDadosEstatisticaMovimentacaoUsuarioServentia(dt);
//				
//	}
//	
//	private static void EventoRegimePsTest(FabricaConexao fabricaconexao) throws Exception{		
//		EventoRegimePs ps = new EventoRegimePs();
//		ps.setConexao(fabricaconexao);
//		
//		EventoRegimeDt dt = ps.consultarId("651");
//		dt.setId_RegimeExecucao("2");
//		ps.alterar(dt);
//		
//		
//	}
//	
//	private static void CustaPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		CustaPs ps = new CustaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarId("1");
//		ps.consultarDescricao("AVERBAÇÃO", "0");
//		ps.consultarItemGuiaPorCodigoRegimento("1");
//		List listDt = ps.consultarDescricao("AVERBAÇÃO", "0");
//		listDt.remove(listDt.size() - 1);
//		ps.consultarDescricao(listDt);
//	}
//	
//	private static void ComutacaoTransitoJulgadoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		TransitoJulgadoEventoPs ps = new TransitoJulgadoEventoPs();
//		ps.setConexao(fabricaconexao);
//		
//		TransitoJulgadoEventoDt dt = ps.consultarId("1");
//		ps.excluir("444");
//		dt.setId("444");
//		ps.inserir(dt);
//		
//		dt.setDataInicioTransito(Funcoes.DataHora(new Date(System.currentTimeMillis())));
//		ps.alterar(dt);
//		
//		ps.excluirId_Evento("138");
//		
//		ps.listarTransitoJulgadoEvento("138", "4");
//		
//		ps.listarTodosTransitoJulgadoEventoNaoExtinto("444", "3", false, true);
//		
//		ps.listarTodosTransitoJulgadoEventoNaoExtinto("444", "3", true, false);
//		
//	}
//	
//	private static void ComarcaCidadePsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ComarcaCidadePs ps = new ComarcaCidadePs();
//		ps.setConexao(fabricaconexao);
//		
//		String id_comarca = "1";
//		
//		 ps.consultarCidadeComarcaGeral(id_comarca);
//	}
//	
//	private static void ClassificadorPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ClassificadorPs ps = new ClassificadorPs();
//		ps.setConexao(fabricaconexao);
//		
//		String descricao = "Despacho";
//		String posicao = "0";
//		String id_Serventia = "1";
//		String id_Processo = "2053";		
//		
//		ps.consultarDescricao(descricao, posicao);
//		
//		ps.consultarClassificadorServentia(descricao, posicao, id_Serventia);
//		
//		ps.consultarClassificadorProcesso(id_Processo);
//		
//		
//	}
//	
//	private static void CidadeSSPPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		CidadeSSPPs ps = new CidadeSSPPs();
//		ps.setConexao(fabricaconexao);
//		
//		String id_CidadeSSP = "25300";
//		
//		ps.buscaCidadeTJ(id_CidadeSSP);
//		
//		
//	}
//	
//	private static void CidadePsTest(FabricaConexao fabricaconexao) throws Exception{		
//		CidadePs ps = new CidadePs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarDescricao("Goia", "0");
//		
//		ps.consultarDescricao("Goia", "GO","0");
//		
//		ps.buscaCidade("Goiania", "GO");
//		
//		ps.relListagemCidades();
//		
//		ps.consultarCidadesGoias();
//	}
//	
//	private static void CustaValorPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		CustaValorPs ps = new CustaValorPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarDescricao("d", "0");
//		
//		ps.consultaValorCusta("1");
//		
//		List listaCustaDt = new ArrayList();
//		CustaDt custaDt = new CustaDt();
//		custaDt.setPorcentagem("10");
//		listaCustaDt.add(custaDt);
//		
//		CustaDt custaDt2 = new CustaDt();
//		custaDt2.setPorcentagem("10");
//		listaCustaDt.add(custaDt2);		
//		ps.consultaValorIntevaloCusta(listaCustaDt , "10");
//		
//		ps.consultaValorEspecificoCodigoRegimento("10");
//	}
//	
//	private static void AudienciaProcessoStatusPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		AudienciaProcessoStatusPs ps = new AudienciaProcessoStatusPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarIdAudienciaProcessoStatusCodigo(AudienciaProcessoStatusDt.LIVRE);
//		ps.consultarAudienciaProcessoStatusMovimentacao(String.valueOf(ServentiaTipoDt.VARA));	
//		ps.consultarDescricao("Realizada", String.valueOf(ServentiaTipoDt.VARA), "0");
//		
//	}
//	
//	private static void AudienciaProcessoResponsavelPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		AudienciaProcessoResponsavelPs ps = new AudienciaProcessoResponsavelPs();
//		ps.setConexao(fabricaconexao);
//		
//		String Id_AudienciaProcesso = "55169";
//		String ProcessoNumero = "457419";
//		String Id_UsuarioServentia = "248";
//		String Usuario = "testejuiz1";
//		String Id_CargoTipo = "5";
//		String CargoTipo = "5";
//		String DataAgendada = "15/01/2010 15:14:20";
//		String Nome = "Zé";
//		
//		AudienciaProcessoResponsavelDt dt = new AudienciaProcessoResponsavelDt();
//		// Cria o Dt para o teste
//		dt.setId_AudienciaProcesso(Id_AudienciaProcesso);
//		dt.setDataAgendada(DataAgendada);
//		dt.setProcessoNumero(ProcessoNumero);
//		dt.setId_UsuarioServentia(Id_UsuarioServentia);
//		dt.setCargoTipo(CargoTipo);
//		dt.setNome(Nome);
//		dt.setId_CargoTipo(Id_CargoTipo);
//		// Tenta Inserir o Dt
//		ps.inserir(dt);
//		
//		ps.consultarResponsaveisAudienciaProcesso(Id_AudienciaProcesso);
//		
//		ps.consultarJuizResponsavelAudiencia(Id_AudienciaProcesso);
//		
//		
//		
//		ps.excluir(dt.getId());
//	}
//	
//	private static void AudienciaProcessoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		AudienciaProcessoPs ps = new AudienciaProcessoPs();
//		ps.setConexao(fabricaconexao);
//		
////		AudienciaProcessoDt dt = new AudienciaProcessoDt();
////		//Cria o Dt para o teste
////		dt.setId_AudienciaProcessoStatus("1");
////		dt.setId_Audiencia("55450");
////		dt.setId_ServentiaCargo("53");
////		dt.setId_Processo("2072");
////		ps.inserir(dt);
////		
////		dt = ps.consultarId(dt.getId());
////		
////		//ps.alterar(dt);		
////			
////		ps.consultarAudienciasPendentesProcesso("2072", 2);
////		
////		ps.consultarAudienciaPendenteProcesso("2072", "53");
////		
////		ps.consultarAudienciasPendentesServentiaCargo("53");
////		
////		ps.consultarSessaoMarcada("53");
////		
////		ps.consultarIdCompleto(dt.getId());
////		
//		//ps.alterarAudienciaProcessoMovimentacao(dt);
//		
//		//ps.consultarAudienciaProcessos("40623", true);
//		
//		//ps.consultarAudienciaProcessos("2599143", true);
//		
////		ps.agendarAudienciaProcessoManual(dt);
////		
////		ps.agendarAudienciaProcessoAutomatico("1", "53", "1");
////		
////		ps.agendarAudienciaProcessoAutomaticoServentiaCargo("53", "1", "1");
//		
////		ps.alterarStatusAudiencia("53", 1);
////		
////		ps.alterarResponsavelAudiencia("53", "1");
////		
////		ps.alterarAudienciaProcessoAgendamento(dt);
////		
////		ps.excluirAudienciaProcesso(dt);
////		
////		ps.excluir(dt.getId());		
//		
//	}
//	
//	private static void AudienciaTipoPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		AudienciaTipoPs ps = new AudienciaTipoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarDescricao("Re", "1");
//		
//		ps.consultarAudienciaTipoCodigo("1");
//	}
//	
//	private static void ArrecadacaoCustaPsTest(FabricaConexao fabricaconexao) throws Exception{		
//		ArrecadacaoCustaPs ps = new ArrecadacaoCustaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("t", "0");
//	}
	
}