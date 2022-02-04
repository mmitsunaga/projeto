package br.gov.tj.teste;

import oracle.jdbc.pool.OracleDataSource;
import br.gov.go.tj.projudi.ne.AudienciaPublicadaNe;
import br.gov.go.tj.utils.ConexaoBD;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class TesteComandoPersistencia {
	
	private static void inicieAmbiente() throws Exception{
		/*MysqlDataSource dataSource = new MysqlDataSource();			
		dataSource.setUser("ecnj");
		dataSource.setPassword("jusfr13nds");
		dataSource.setUrl("jdbc:mysql://10.0.10.69:3306/projuditjgo");*/
		
		OracleDataSource dataSourceOracle = new OracleDataSource();
		//dataSourceOracle.setUser("mmgomes");
		//dataSourceOracle.setPassword("MmGomes2013&");
		dataSourceOracle.setUser("wcsilva");
		dataSourceOracle.setPassword("tjgo1234");
		dataSourceOracle.setURL("jdbc:oracle:thin:@127.0.0.1:1521/projudi_online.tjgo.gov");
		//dataSourceOracle.setURL("jdbc:oracle:thin:@10.0.10.150:1521/homolog.tjgo.gov");
		//dataSourceOracle.setURL("jdbc:oracle:thin:@10.0.10.150:1521/desenv.tjgo.gov");
		//dataSourceOracle.setURL("jdbc:oracle:thin:@192.168.200.120:1521/homolog");
				
		ConexaoBD.setDatasourcePersistencia(dataSourceOracle);
	}
	
	public static void main(String[] args) throws Exception {
		String senha12345 = Funcoes.SenhaMd5("12345");
		FabricaConexao fabricaconexao = null;
		try{
			inicieAmbiente();	
			
			AudienciaPublicadaNe audienciaPublicadaNe = new AudienciaPublicadaNe(); 
			audienciaPublicadaNe.executeProcessamento();
//					
//			ProcessoNe processoNe = new ProcessoNe();			
//			UsuarioNe UsuarioSessao = new UsuarioNe();
			
//			// testeadv no desenv
//			UsuarioSessao.setObdtUsuario(UsuarioSessao.consultarId("4"));
//			
//			List listaServentiasGrupos = UsuarioSessao.consultarServentiasGrupos();
//			for (Object objUsuarioDt : listaServentiasGrupos)
//			{
//				UsuarioDt usuariodt = (UsuarioDt) objUsuarioDt;
//				// ordem dos advogados                                                                                                                                                                                                                     no desenv
//				if((usuariodt.getId_Serventia().trim().equalsIgnoreCase("721")) && (Funcoes.StringToInt(usuariodt.getGrupoCodigo()) == GrupoDt.ADVOGADOS)){
//					UsuarioSessao.setObdtUsuario(usuariodt);
//					break;
//				}
//			}
//			
//			List tempList = processoNe.consultarProcessosPromotorAdvogado(UsuarioSessao, "0");
			
//			UsuarioSessao.setObdtUsuario(UsuarioSessao.consultarId("18713"));
//			
//			List listaServentiasGrupos = UsuarioSessao.consultarServentiasGrupos();
//			for (Object objUsuarioDt : listaServentiasGrupos)
//			{
//				UsuarioDt usuariodt = (UsuarioDt) objUsuarioDt;
//				// ordem dos advogados                                                                                                                                                                                                                     no desenv
//				if((usuariodt.getId_Serventia().trim().equalsIgnoreCase("541")) && (Funcoes.StringToInt(usuariodt.getGrupoCodigo()) == GrupoDt.PROMOTORES)){
//					UsuarioSessao.setObdtUsuario(usuariodt);
//					break;
//				}
//			}
			
//			List tempList = processoNe.consultarProcessosAdvogado(UsuarioSessao, "0", true);
//			
//			List tempList2 = processoNe.consultarProcessosDadosAdvogado("", "", "", "", "", "0");
//			
//			fabricaconexao = new FabricaConexao();
//			
//			CustaPs custaPs = new CustaPs(fabricaconexao);	
//			//custaPs.testeInserirBigDecimal("10.256.312.541,3998", "10.256,3855");
//			
//			double d1 = 0.1;
//			double d2 = 0.2;
//			
//			BigDecimal big1 = new BigDecimal(0.1);
//			BigDecimal big2 = new BigDecimal(0.2);
//			
//			/*custaPs.testeInserirBigDecimal(String.valueOf(big1.add(big2)).replace(".", ","), String.valueOf((d1 + d2)).replace(".", ","));
//			
//			big1 = new BigDecimal(0.1);
//			big2 = new BigDecimal(0.2);
//			
//			custaPs.testeInserirBigDecimal(big1.add(big2), (d1 + d2));
//			
//			big1 = new BigDecimal("0.1");
//			big2 = new BigDecimal("0.2");		
//						
//			custaPs.testeInserirBigDecimal(big1.add(big2), (d1 + d2));*/
//			
//			big1 = new BigDecimal("0.1");
//			big2 = new BigDecimal("0.2");
//									
//			custaPs.testeInserirBigDecimal(Funcoes.FormatarDecimal((big1.add(big2)).toString()), Funcoes.FormatarDecimal(String.valueOf((d1 + d2))));
//			
//			custaPs.testeCalculo("1.365,36", "1.258.354,2569");
//			
			/*List listaDeAgrupamento = new ArrayList();
			if (!listaDeAgrupamento.contains("teste-1-existe")) listaDeAgrupamento.add("teste-1-existe");
			if (!listaDeAgrupamento.contains("teste-1-existe")) listaDeAgrupamento.add("teste-1-existe");
			if (!listaDeAgrupamento.contains("teste-1-existe2")) listaDeAgrupamento.add("teste-1-existe2");
			if (!listaDeAgrupamento.contains("teste-1-existe2")) listaDeAgrupamento.add("teste-1-existe2");*/
			
			//int a = 5;
			////System.out.println(String.format("%2s", a));
			
			//testeEscolaridadePs(fabricaconexao);			
			//testeEventoLocalPs(fabricaconexao);			
			//testeAreaPs(fabricaconexao);
			//testePendenciaStatusPs(fabricaconexao);
//			testePenaExecucaoTipoPs(fabricaconexao);
			//testeProcessoAssuntoPs(fabricaconexao);
			//testeComarcaPs(fabricaconexao);
			//testeGuiaNumeroPs(fabricaconexao);
			//testeMandadoJudicialPs(fabricaconexao);			
			//testeMandadoJudicialStatusPs(fabricaconexao);
			//testeCertidaoTipoProcessoTipoPs(fabricaconexao);
			//testeUsuarioServentiaEscalaStatusPs(fabricaconexao);
			//testeEventoExecucaoPs(fabricaconexao);
			//testeEventoExecucaoStatusPs(fabricaconexao);
			//testeEstadoPs(fabricaconexao);
			//testePalavraPs(fabricaconexao);
			//testeUsuarioServentiaPermissaoPs(fabricaconexao);
			//testeArquivoTipoPs(fabricaconexao);
//			testeRelatorioEstProPs(fabricaconexao);
//			testeRgOrgaoExpedidorPs(fabricaconexao);
//			testeTarefaStatusPs(fabricaconexao);
//			testeGuiaInicial1GrauPs(fabricaconexao);
//			testeGuiaModeloPs(fabricaconexao);
//			testeProcessoParteBeneficioPs(fabricaconexao);
//			testePendenciaTipoRelacionadaPs(fabricaconexao);
//			testePendenciaTipoRelacionadaPs(fabricaconexao);
//			testeLocalCumprimentoPenaPs(fabricaconexao);
//			testeLocalCumprimentoPenaPs(fabricaconexao);
//			testeZonaBairroPs(fabricaconexao);
//			testeZonaBairroPs(fabricaconexao);
//			testeAudienciaPublicadaPs(fabricaconexao);
//			testeRegimeExecucaoPs(fabricaconexao);
//			testeLogPs(fabricaconexao);
//			testeServentiaSubtipoAssuntoPs(fabricaconexao);
//			testeGuiaCustaModeloPs(fabricaconexao);
//			testeServentiaRelacionadaPs(fabricaconexao);
//			testeGrupoArquivoTipoPs(fabricaconexao);
//			testeBairroPs(fabricaconexao);
//			testeCrimeExecucaoPs(fabricaconexao);
//			testeZonaPs(fabricaconexao);
//			testeServentiaTipoPs(fabricaconexao);
//			testeGrupoPermissaoPs(fabricaconexao);
//			testeMovimentacaoArquivoPs(fabricaconexao);
//			testeParametroCrimeExecucaoPs(fabricaconexao);
//			testeGraficoProcessoPs(fabricaconexao);
//			testeEscalaPs(fabricaconexao);
//			testeLocomocaoPs(fabricaconexao);
//			testeUsuarioServentiaEscalaPs(fabricaconexao);
//			testePendenciaResponsavelPs(fabricaconexao);
//			testeMovimentacaoPs(fabricaconexao);
//			testeEstatisticaPendenciaPs(fabricaconexao);
//			testeArquivoPs(fabricaconexao);			
//			testeUsuarioServentiaPs(fabricaconexao);
//			testeCondenacaoExecucaoPs(fabricaconexao);
//			testeProcessoParteAdvogadoPs(fabricaconexao);
//			testeRelatorioProcessoExecucaoPs(fabricaconexao);
//			testeDistribuirMandadoPs(fabricaconexao);
//			testeCertificadoPs(fabricaconexao);
//			testeProcessoEventoExecucaoPs(fabricaconexao);
//			testeProcessoResponsavelPs(fabricaconexao);
//			testeGuiaEmissaoPs(fabricaconexao);
//			testeAreaDistribuicaoPs(fabricaconexao);
//			testeProcessoPartePs(fabricaconexao);
//			testeProcessoExecucaoPs(fabricaconexao);
//			testeRelatorioEstatisticaPs(fabricaconexao);
//			testePendenciaPs(fabricaconexao);
			//testeProcessoPs(fabricaconexao);
//			testeAudienciaPs(fabricaconexao);
//			testeUsuarioPs(fabricaconexao);		
//			testeEstatisticaPs(fabricaconexao);
//			testeServentiaCargoPs(fabricaconexao);
//			testeEventoRegimePsGen(fabricaconexao);
//			testeCertidaoTipoProcessoTipoPsGen(fabricaconexao);	
//			testeCidadeSSPPsGen(fabricaconexao);	
//			testeCidadePs(fabricaconexao);
//			testeProcessoParteBeneficioPsGen(fabricaconexao);
//			testeProcessoParteDebitoPsGen(fabricaconexao);
//			testeServentiaCargoPsGen(fabricaconexao);
//			testeZonaPsGen(fabricaconexao);
//			testeLeituraAutomatica();
//			testeProcessoPsUltimasAlteracoes(fabricaconexao);
//			testeReeniciarNumeroDaGuiaEProcesso(fabricaconexao);
		}catch( Exception ex){
			ex.printStackTrace();
		}finally{
			if(fabricaconexao != null) try{
				fabricaconexao.fecharConexao();
			} catch(Exception e) {				
			}
		}
	}
	
//	/*
//	private static void teste(FabricaConexao fabricaconexao) throws Exception{		
//		
//		ps.setConexao(fabricaconexao);
//	}
//	*/
//	
//	
//	
//	
//	private static void testeReeniciarNumeroDaGuiaEProcesso(FabricaConexao fabricaconexao) throws Exception{
//		ProcessoNumeroPs ps =  new ProcessoNumeroPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.reiniciarTabela();
//		
//		GuiaNumeroPs ps2 = new GuiaNumeroPs();
//		ps2.setConexao(fabricaconexao);
//		ps2.reiniciarTabela();
//	}
//	
//	private static void testeProcessoPsUltimasAlteracoes(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoPs ps = new ProcessoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarProcessosSegundoGrau("", "false", "", "5000280", "28", "2010", "1", "1", "58", "1", "true", "1", "1", "1", "0");
//		
//		ps.consultarQuantidadeAtivosSegundoGrau("1");
//		
//	}
//	
//	private static void testeLeituraAutomatica() throws Exception{
//		String valorAtual = "";
//		valorAtual = "Início Leitura Automática Pendência " + Funcoes.DataHora(new Date());
//		new LogNe().salvar(new LogDt("ExecuçãoAutomatica", "", UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.ExecucaoAutomatica), valorAtual, ""));
//		new PendenciaNe().marcarLeituraAutomatica();
//		valorAtual = "Fim Leitura Automática Pendência " + Funcoes.DataHora(new Date());
//		new LogNe().salvar(new LogDt("ExecuçãoAutomatica", "", UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.ExecucaoAutomatica), valorAtual, ""));
//	}
//	
//	private static void testeZonaPsGen(FabricaConexao fabricaconexao) throws Exception{		
//		ZonaPsGen ps = new ZonaPsGen();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarId("2647");
//	}
//	
//	private static void testeServentiaCargoPsGen(FabricaConexao fabricaconexao) throws Exception{		
//		ServentiaCargoPsGen ps = new ServentiaCargoPsGen();
//		ps.setConexao(fabricaconexao);
//		
//		ServentiaCargoDt dados = new ServentiaCargoDt();
//		dados.setServentiaCargo("teste 123");
//		dados.setId_Serventia("1");
//		dados.setId_CargoTipo("1");
//		dados.setId_UsuarioServentiaGrupo("1");
//		dados.setQuantidadeDistribuicao("1");
//		dados.setPrazoAgenda("1");		
//		ps.inserir(dados);
//		
//		ps.alterar(dados);
//		
//		ps.consultarId(dados.getId());
//		
//		ps.consultarDescricao("t", "0");
//		
//		ps.excluir(dados.getId());
//	}
//	
//	private static void testeProcessoParteDebitoPsGen(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoParteDebitoPsGen ps = new ProcessoParteDebitoPsGen();
//		ps.setConexao(fabricaconexao);
//		
//		ProcessoParteDebitoDt dados = new ProcessoParteDebitoDt();
//		dados.setId_ProcessoDebito("1");
//		dados.setId_ProcessoParte("1");
//		ps.inserir(dados);
//		
//		ps.alterar(dados);
//		
//		ps.consultarId(dados.getId());
//		
//		ps.consultarDescricao("t", "0");
//		
//		ps.excluir(dados.getId());		
//		
//	}
//	
//	private static void testeProcessoParteBeneficioPsGen(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoParteBeneficioPsGen ps = new ProcessoParteBeneficioPsGen();
//		ps.setConexao(fabricaconexao);
//		
//		ProcessoParteBeneficioDt dados = new ProcessoParteBeneficioDt();
//		dados.setId_ProcessoBeneficio("1");
//		dados.setId_ProcessoParte("1");
//		dados.setDataInicial("25/03/2011");
//		dados.setDataInicial("25/05/2011");
//		
//		ps.inserir(dados);
//		
//		ps.alterar(dados);
//		
//		ps.consultarId(dados.getId());
//		
//		ps.consultarDescricao("t", "0");
//		
//		ps.excluir(dados.getId());
//	}
//	
//	private static void testeCidadePs(FabricaConexao fabricaconexao) throws Exception{		
//		CidadePs ps = new CidadePs();
//		ps.setConexao(fabricaconexao);
//		
//		CidadeDt dados = new CidadeDt();
//		dados.setCidade("Cidade Teste");
//		dados.setCidadeCodigo("10");
//		dados.setId_Estado("1");		
//		ps.inserir(dados );
//		
//		ps.alterar(dados);
//		
//		ps.consultarDescricao("t", "GO", "0");
//		
//		ps.excluir(dados.getId());
//	}
//	
//	private static void testeCidadeSSPPsGen(FabricaConexao fabricaconexao) throws Exception{		
//		CidadeSSPPsGen ps = new CidadeSSPPsGen();
//		ps.setConexao(fabricaconexao);
//		
//		CidadeSSPDt dados = new CidadeSSPDt();
//		dados.setCidade("teste");
//		dados.setId_CidadeTJ("766");
//		dados.setEstado("GO");
//		ps.inserir(dados );
//		
//		ps.alterar(dados);
//		
//		ps.consultarId(dados.getId());
//		
//		ps.consultarDescricao("t", "0");
//		
//		ps.excluir(dados.getId());
//	}
//	
//	private static void testeCertidaoTipoProcessoTipoPsGen(FabricaConexao fabricaconexao) throws Exception{		
//		CertidaoTipoProcessoTipoPsGen ps = new CertidaoTipoProcessoTipoPsGen();
//		ps.setConexao(fabricaconexao);
//		CertidaoTipoProcessoTipoDt dados = new CertidaoTipoProcessoTipoDt();
//		dados.setId_CertidaoTipo("1");
//		dados.setId_ProcessoTipo("1");
//		ps.inserir(dados );
//		ps.alterar(dados);
//		ps.consultarId("1");
//		ps.consultarDescricao("", "1");
//	}
//	
//	private static void testeEventoRegimePsGen(FabricaConexao fabricaconexao) throws Exception{		
//		EventoRegimePsGen ps = new EventoRegimePsGen();
//		ps.setConexao(fabricaconexao);
//		
//		EventoRegimeDt dados = new EventoRegimeDt();
//		dados.setId_ProcessoEventoExecucao("4");
//		dados.setId_RegimeExecucao("2");
//		ps.inserir(dados );
//		
//		ps.alterar(dados);
//		
//		ps.consultarDescricao("t", "0");
//		
//		ps.consultarId(dados.getId());
//		
//		ps.excluir(dados.getId());
//	}
//	
//	private static void testeServentiaCargoPs(FabricaConexao fabricaconexao) throws Exception{		
//		ServentiaCargoPs ps = new ServentiaCargoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ServentiaCargoDt dados = new ServentiaCargoDt();
//		dados.setServentiaCargo("Teste cargo.");
//		dados.setId_Serventia("1");
//		dados.setId_CargoTipo("1");
//		dados.setId_UsuarioServentiaGrupo("1");		
//		dados.setQuantidadeDistribuicao("1");		
//		dados.setCodigoTemp("1");		
////		ps.inserir(dados);
////		
////		ps.alterar(dados);
////		
////		ps.alterarUsuarioServentiaCargo("1", "1", "1");
//		
////		ps.consultarServentiasCargos("1");
////		
////		ps.consultarServentiaCargo("1");
////		
////		ps.consultarServentiaCargosDistribuicao("1", "1", "1");
////		
////		ps.consultarServentiaCargosDistribuicao2Grau("1", "1", "1", "1");
////		
////		ps.consultarServentiaCargosDistribuicao2GrauPropriaServentia("1", "1", "1", "1", "1");
////		
////		ps.consultarServentiaCargosDistribuicaoSegundoGrau("1");
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
//		ps.consultarServentiaCargos("1", UsuarioSessao);
//		
//		List listaServentias = new ArrayList();
//		ServentiaDt serventia = new ServentiaDt();
//		serventia.setId("1");
//		listaServentias.add(serventia);
//		ps.consultarCargosServentiasRelacionadas(listaServentias );
//		
//		serventia = new ServentiaDt();
//		serventia.setId("2");
//		listaServentias.add(serventia);
//		ps.consultarCargosServentiasRelacionadas(listaServentias );
//		
//		ps.consultarDescricao("j", "0");
//		
//		ps.consultarServentiaCargos("t", "0", "1", "1");
//		
//		ps.consultarServentiaCargo("1", 1);
//		
//		ps.consultarUsuarioServentiaCargo("1", 1);
//		
//		ps.consultarTiposCargos("t", "0", "1");
//		
//		ps.consultarServentiaCargosAgendaAudiencia("t", "0", "1");
//		
//		ps.consultarServentiaCargosDesembargadores("t", "0", "1");
//		
//	}
//	
//	private static void testeEstatisticaPs(FabricaConexao fabricaconexao) throws Exception{		
//		EstatisticaPs ps = new EstatisticaPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.gerarEstatisticaMes("2011", "04");
//	}
//	
//	private static void testeUsuarioPs(FabricaConexao fabricaconexao) throws Exception{		
//		UsuarioPs ps = new UsuarioPs();
//		ps.setConexao(fabricaconexao);
//		
//		//ps.getMenu("1", "1"); // TODO: Não está sendo utilizado e está com erro.
//		
//		//ps.getMenuEspecial("1", "1", 1); // TODO: Não está sendo utilizado e está com erro.
//		
////		ps.ConsultaUsuarioPermissoes("1");
////		
////		ps.consultarAdvogadoOab("1", "1", "1", 1);
//		
//		//ps.getAdministradores(); // TODO: Não está sendo utilizado e está com erro.
//		
////		ps.consultarUsuarios("1", "1");
////		
////		ps.alterarStatusUsuario("1", 1);
////		
////		ps.consultaUsuarioSenha("testeana", "12345");
//		
//		UsuarioDt dados = new UsuarioDt();
//		dados.setId_CtpsUf("1");
//		dados.setNome("Teste Prepareted Statement");
//		dados.setSexo("M");
//		dados.setDataNascimento("19/10/1978");		
//		dados.setId_Naturalidade("766");
//		dados.setTelefone("30932478");
//		dados.setCelular("81598395");		
//		dados.setEMail("marcio.mendonca.gomes@gmail.com");
//		dados.setId_Endereco("1");		
//		dados.setRg("3673333");			
//		dados.setId_RgOrgaoExpedidor("1");			
//		dados.setRgDataExpedicao("15/06/1995");			
//		dados.setCpf("86889346150");			
//		dados.setTituloEleitor("123456789");
//		dados.setTituloEleitorZona("10256");
//		dados.setTituloEleitorSecao("11526");
//		dados.setCtps("115263");
//		dados.setCtpsSerie("12526");
//		dados.setPis("232556636");
//		dados.setMatriculaTjGo("115256365");
//		dados.setNumeroConciliador("1155263");
//		dados.setUsuario("jrcorrea");
//		dados.setSenha("12345");
//		dados.setDataAtivo("08/04/2011");			
//		dados.setDataExpiracao("31/12/2015");
//		dados.setAtivo("true");		
//		ps.inserir(dados);
//		
//		ps.alterar(dados);
//		
//		ps.consultarUsuarioCompleto("1");
//		
//		ps.consultarAdvogadoId("1");
//		
//		ps.consultarIdServentiaCargoDesembargadorServentia("1");
//		
//		ps.consultarAdvogadoServentiaId("1");
//		
//		ps.consultarDescricaoAdvogado("1", "teste", "123", "123","86889346149", "0");
//		
//		ps.consultarServentiaOabAdvogado("1");
//		
//		ps.consultarServidorJudiciarioId("1");
//		
//		ps.consultarUsuarioServentiaGrupoId("1");
//		
//		ps.consultarDescricaoServidorJudiciario("1");
//		
//		ps.consultarServentiasGruposUsuario("1");
//		
//		ps.consultarDescricaoServidorJudiciario("1");
//		
//		ps.limparSenha(dados.getId());
//		
//		ps.alterarSenha(dados.getId(), "12345", "marciogomes");
//		
//		ps.consultarAssistenteId("1");
//		
//		ps.consultarAssistenteServentiaGrupoId("1");
//		
//		ps.consultarTodosUsuarios("teste", "marcio.gomes", "0");
//		
//		ps.consultarDescricaoAssistenteServentia("1", "1", "1");
//		
//		ps.consultarUsuarioCpf("86889346149");
//		
//		ps.consultarUsuarioLogin("marcio.gomes");	
//		
//	}
//	
//	private static void testeAudienciaPs(FabricaConexao fabricaconexao) throws Exception{		
//		AudienciaPs ps = new AudienciaPs();
//		ps.setConexao(fabricaconexao);
//		
////		AudienciaDt audienciaDt = new AudienciaDt();
////		audienciaDt.setAudienciaTipoCodigo("1");
////		audienciaDt.setId_Serventia("1");
////		audienciaDt.setDataAgendada("15/03/2011 11:25:36");
////		audienciaDt.setDataMovimentacao("01/03/2011 11:25:36");
////		audienciaDt.setReservada("true");		
//////		ps.inserir(audienciaDt);
////		
////		audienciaDt.setId_AudienciaTipo("1");
//////		ps.inserir(audienciaDt);
//////		
//////		ps.getUltimaAudienciaMarcadaAgendamentoAutomatico("1", "2053");		
//////		
//////		ps.consultarAudienciasLivresAgendamentoManual(audienciaDt , "1", 1, "0");
////		
////		UsuarioDt usuarioDt = new UsuarioDt();
////		usuarioDt.setId_Serventia("1");
////		ps.consultarAudienciasPendentes(usuarioDt , audienciaDt, "0", false);
////		
////		ps.consultarAudienciasPendentes(usuarioDt, audienciaDt, "0", true);
////		
////		ps.consultarAudienciasPendentesAdvogado("1", audienciaDt, "0");
////		
////		ps.consultarAudienciasPendentesUsuario(usuarioDt, audienciaDt, "0");
////		
//		ps.consultarSessoesPendentesRelator("2017", "562075");
//		
//		ps.consultarSessoesPendentesRelator("8406", "2581564");
////		
////		ps.consultarSessoesPendentesAdvogado("1", "0");
////		
////		ps.consultarSessoesPendentesPromotor("1", "0");
////		
////		ps.consultarAudienciasPendentesPromotor("1", audienciaDt, "0");
////		
////		ps.consultarAudienciasParaHoje(usuarioDt, audienciaDt, "0", true);
////		
////		ps.consultarAudienciasParaHoje(usuarioDt, audienciaDt, "0", false);
////		
////		ps.consultarAudienciasParaHojeAdvogado(usuarioDt, audienciaDt, "0");
////		
////		ps.consultarAudienciasParaHojeUsuario(usuarioDt, audienciaDt, "0");
////		
////		ps.consultarQuantidadeAudienciasParaHoje("1");
////		
////		ps.consultarQuantidadeAudienciasParaHojeAdvogado(usuarioDt);
////		
////		ps.consultarAudienciasMovimentadasHoje(usuarioDt, audienciaDt, "0", false);
////		
////		ps.consultarAudienciasMovimentadasHoje(usuarioDt, audienciaDt, "0", true);
////		
////		ps.consultarAudienciasMovimentadasHojeAdvogado(usuarioDt, audienciaDt, "0");
////		
////		ps.consultarAudienciasMovimentadasHojeUsuario(usuarioDt, audienciaDt, "0");
////		
////		ps.consultarAudienciasFiltro("1", audienciaDt, "50036", "36", "0");
////		
////		ps.consultarAudienciasFiltro("1", audienciaDt, "", "", "0");
////		
////		ps.consultarAudienciasFiltroAdvogado("1", audienciaDt, "50036", "36", "0");
////		
////		ps.consultarAudienciasFiltroAdvogado("1", audienciaDt, "", "", "0");
////		
////		ps.consultarAudienciasFiltroUsuario("1", audienciaDt, "50036", "36", "0");
////		
////		ps.consultarAudienciasFiltroUsuario("1", audienciaDt, "", "", "0");
////		
////		ps.consultarAudienciasFiltroPromotor("1", audienciaDt, "50036", "36", "0");
////		
////		ps.consultarSessoesAbertas("1");
////		
////		ps.consultarSessoesAbertasCamaras("1");
////		
////		ps.consultarDataUltimaAudiencia("1");
////		
////		ps.validarAudienciaAgendamento(audienciaDt, "1");
//		
////		ps.consultarAudienciasLivres("1", "1", "1", "0");
////		
////		ps.consultarAudienciaLivreCompleta("1");
////		
////		ps.consultarAudienciaCompleta("1");
////		
////		ps.consultarAudienciaProcessoCompleta("1");
////		
////		ps.alterarAudienciaMovimentacao(audienciaDt);
////		
////		ps.consultarSessoesFiltro("1", audienciaDt, "1", "0");
////		
////		ps.consultarSessoesFiltroCamaras("1", audienciaDt, "1", "0");
////		
////		ps.relListagemAudienciasParaHoje(usuarioDt, audienciaDt);
////		
////		ps.relListagemAudienciasPendentes(usuarioDt, audienciaDt);
////		
////		ps.relListagemAudienciasMovimentadasHoje(usuarioDt, audienciaDt);
////		
////		ps.desativarAudiencia("1");
//				
//	}
//	
//	private static void testeProcessoPs(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoPs ps = new ProcessoPs();
//		ps.setConexao(fabricaconexao);
//		
////		ps.consultarIdCompleto("", "5000107", "67");
////		ps.consultarIdCompleto("2053", "", "");
////		
////		ps.consultarId("2053");
////		
////		ps.consultarUltimoProcesso();
////		
////		ps.consultarUltimaDistribuicaoServentiaCargo("1", 1);
////		
////		ps.consultarUltimaDistribuicao("1");
//		
////		ProcessoDt dados = new ProcessoDt();
////		dados.setProcessoNumero("5000149");
////		dados.setDigitoVerificador("36");
////		dados.setId_ProcessoDependente("3697");
////		dados.setProcessoTipoCodigo("1");			
////		dados.setProcessoFaseCodigo("1");
////		dados.setProcessoStatusCodigo("1");
////		dados.setId_ProcessoPrioridade("3");
////		dados.setId_Serventia("1");
////		dados.setId_ServentiaOrigem("2");	
////		dados.setAreaCodigo("1");
////		dados.setId_ObjetoPedido("1");
////		dados.setId_Classificador("1");
////		dados.setSegredoJustica("true");
////		dados.setProcessoDiretorio("c:\\");
////		dados.setTcoNumero("1234");
////		dados.setValor("12552,36");
////		dados.setDataArquivamento("05/05/2011");
////		dados.setApenso("true");
////		dados.setAno("2011");
////		dados.setForumCodigo("51");		
////		ps.inserir(dados);
//		
////		dados.setId_ProcessoDependente("3696");
////		dados.setId_ProcessoTipo("305");
////		dados.setId_Area("1");
////		dados.setId_ProcessoFase("1");
////		dados.setId_ProcessoPrioridade("3");
////		dados.setId_Classificador("1");
////		dados.setSegredoJustica("true");
////		dados.setApenso("true");
////		ps.inserir(dados);	
////		
////		ps.alterar(dados);
////		
////		ps.consultarProcessos("", "nomeexato", "", "5000143", "36", "2011", "1", "1", "1", "1", "true", "1", "0");
////		
////		ps.consultarProcessos("nome", "nomeexato", "86889346149", "5000143", "36", "2011", "1", "", "1", "1", "true", "1", "0");
//		
//		ps.consultarProcessosPublica("", "86889346149", "5000143", "36", "2011", ServentiaTipoDt.SEGUNDO_GRAU, "true", "true","0", 0);	
////		
////		ps.consultarProcessosPublica("TESTE", "86889346149", "5000143", "36", "2011", ServentiaTipoDt.VARA, "true", "true","0");
////		
////		ps.consultarProcessosSegundoGrau("", "true", "86889346149", "5000143", "36", "2011", "1", "", "1", "1", "false", "1", "1", "0");
////		
////		ps.consultarProcessosSegundoGrau("teste", "false", "86889346149", "5000143", "36", "2011", "1", "", "1", "1", "false", "", "1", "0");
////		
////		ps.consultarProcessosTurmaRecursalSemLimitacao("sadf", "false", "86889346149", "5000143", "36", "2011", "1", "1", "1", "false", "1");
////		
////		ps.consultarProcessosTurmaRecursalSemLimitacao("", "false", "86889346149", "5000143", "36", "2011", "1", "1", "1", "false", "1");
////		
////		ps.consultarProcessosAdvogado("1", "tste", "true", "123", "5000143", "36", "2011", "1", "1", "16", "0");
////		
////		ps.consultarProcessosAdvogado("1", "tste", "false", "123", "5000143", "36", "2011", "1", "1", "16", "0");
////		
////		ps.consultarProcessosAdvogado("1", "", "false", "123", "5000143", "36", "2011", "1", "1", "16", "0");
//		
////		ps.consultarProcessosDelegacia("teste", "false", "1235", "5000143", "36", "2011", "12552", "1", "0");
////		
////		ps.consultarProcessosDelegacia("teste", "true", "1235", "5000143", "36", "2011", "12552", "1", "0");
////		
////		ps.consultarProcessosDelegacia("", "false", "1235", "5000143", "36", "2011", "12552", "1", "0");
////		
////		ps.consultarProcessosPromotor("asdf", "false", "1235", "5000143", "36", "2011", "1", "1", "1", "0");
//		
////		ps.consultarProcessosPromotoria("sadf", "false", "1225", "5000143","36", "2011", "1", "1", "0");
////		
////		ps.consultarProcessosProcuradoria("adfasdf", "false", "1225", "5000143", "36", "2011", "1", "1", "0");
////		
////		ps.consultarProcessosServentiaCargo("1");
////		
////		ps.consultarProcessosJuiz("teste", "false", "122356", "5000143", "36", "2011", "1", "1", "", "1", "0");
////		
////		ps.consultarProcessoNumero("5000143", "36");
////		
////		ps.verificarTcoCadastrado("1", "12345");
////		
////		ps.consultarProcessosApensos("2053");
////		
////		ps.verificaProcessosApensos("2053");	
////		
////		ps.registrarProcessoApenso("2053", "2063", true);
////		
////		ps.verificaProcessoApensado("2053");
////		
////		ps.alterarClassificadorProcesso("2053", "1");
////		
////		ps.consultarQuantidadeAtivosAdvogado("1");
////		
////		ps.consultarQuantidadeAtivosServentia("1");
////		
////		ps.consultarQuantidadeAtivosServentiaPromotoria("1");
////		
////		ps.consultarQuantidadeAtivosServentiaProcuradoria("1");
////		
////		ps.consultarQuantidadeAtivosSegundoGrau("1");
////		
////		ps.consultarQuantidadeAtivosDelegacia("1");
////		
////		ps.consultarQuantidadeAtivosPromotor("1");
////		
////		ps.consultarQuantidadeAtivosJuiz("1", "1");
////		
////		ps.consultarQuantidadeAtivosJuizTurmaRecursal("1", "1");
////		
////		ps.alterarServentiaProcesso("1", "1");
////		
////		ps.alterarProcessoRecursoNaoOriginario("1", "1", "2");
////		
////		ps.alterarProcessoRetornoRecursoNaoOriginario("2053", "1", "3", "1");
//		
////		ProcessoDt processoDt = new ProcessoDt();
////		processoDt.setId("2053");
////		processoDt.setDataArquivamento("10/03/2011 08:24:36");
////		processoDt.setProcessoStatusCodigo("1");
////		processoDt.setValor("1256.36");
////		processoDt.setId_ProcessoTipo("1");		
////		ProcessoParteDt parte = new ProcessoParteDt();
////		parte.setCnpj("15986565000167");
////		parte.setNome("Nome parte");
////		List listapromoventes = new ArrayList();
////		listapromoventes.add(parte);
////		listapromoventes.add(parte);
////		processoDt.setListaPromoventes(listapromoventes);
////		ps.alterarProcessoArquivamento(processoDt);
//		
////		List listapromovidos = new ArrayList();
////		listapromovidos.add(parte);
////		listapromovidos.add(parte);
////		processoDt.setListaPromovidos(listapromovidos);
//		
////		ps.alterarProcessoSuspensao("2053", 1);
////				
////		ps.consultarPreventosProcesso(processoDt, "1", String.valueOf(ServentiaTipoDt.VARA));
//		
////		ps.consultarProcessosFinalizarSuspensao();
////		
////		ps.consultarProcessoPendencia("5000143", 1);
////		
////		ps.consultarProcessosSemiParalisadosServentia("1", "0");
////		
////		ps.consultarProcessosParalisadosServentia("1", "1", "1", "0");
////		
////		ps.consultarProcessosParalisadosConclusoes("1", "1", "0");
//		
////		ps.consultarProcessosClassificador("1", "1");
//		
////		ps.consultarProcessosDadosAdvogado("1", "1", "1", "a", "1", "0");
//		
////		CertidaoNegativaPositivaDt certidaoNegativaPositivaDt = new CertidaoNegativaPositivaDt();
////		certidaoNegativaPositivaDt.setNome("nome teste");
////		certidaoNegativaPositivaDt.setCpfCnpj("15.986.565/0001-67");
////		certidaoNegativaPositivaDt.setDataNascimento("27/12/1980");
////		certidaoNegativaPositivaDt.setArea(String.valueOf(AreaDt.CIVEL));
////		certidaoNegativaPositivaDt.setComarcaCodigo("1");
////		certidaoNegativaPositivaDt.setNomeMae("teste nome");
////		ps.consultarProcessosCertidaoPositivaNegativa(certidaoNegativaPositivaDt);
//		
////		ps.consultarNumeroCompletoDoProcesso("2053");
//		
////		ps.consultarProcessosSemMovimentacaoServentia("1", "15", "0");
//		
////		CertidaoAntecedenteCriminalDt certidaoAntecedenteCriminalDt = new CertidaoAntecedenteCriminalDt();
////		certidaoAntecedenteCriminalDt.setNome("Testando 123");
////		certidaoAntecedenteCriminalDt.setCpfCnpj("15986565000167");
////		certidaoAntecedenteCriminalDt.setDataNascimento("19/10/1978");
////		certidaoAntecedenteCriminalDt.setNomeMae("Nome da Mae");
////		ps.consultarProcessosCertidaoAntecedenteCriminal(certidaoAntecedenteCriminalDt);
////		
////		ps.consultarProcessosAguardandoAudiencia("1", "1", "1", "", "", "0");
////		
//		ps.consultarProcessosSemAudienciaDias("1", "1", "10", "0");
//		
//		ps.consultarProcessosComAudienciaMarcadaDias("1", "1", "1", "1", "0");
////		
//		ps.consultarProcessosAguardandoAudienciaAposOutraRealizada("1", "1", "1", "1", "1", "0");
////		
////		ps.consultarIdServentiaProcesso("2053");
//		
////		ps.consultarQuantidadeProcessosAdvogado("1", "E", "1", "15/01/2010 15:14:20", "20/06/2011 15:14:32");
//		
//		List listaDeIds = new ArrayList();
//		listaDeIds.add("2053");
//		listaDeIds.add("2054");
//		listaDeIds.add("2055");
//		ps.consultarPartesPraticaForenseAdvogado(listaDeIds);
//		
//		ps.consultarAdvogadoPraticaForenseAdvogado(listaDeIds);
//		
////		ps.consultarProcessoPraticaForenseAdvogado("122", "E", "1", "15/01/2010 15:14:20", "20/06/2011 15:14:32");
//		
////		ps.consultarProcessoSimplificado("2053");
//		
//		//ps.consultarProcessoExecucaoCPC("5000143"); // TODO: Falta implementação no método.
//		
//	}
//	
//	private static void testePendenciaPs(FabricaConexao fabricaconexao) throws Exception{		
//		PendenciaPs ps = new PendenciaPs();
//		ps.setConexao(fabricaconexao);
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
//		PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
//		pendenciaTipoDt.setPendenciaTipoCodigo("1");
//		pendenciaTipoDt.setArquivoTipo("1");
//		pendenciaTipoDt.setCodigoTemp("1");		
//		pendenciaTipoDt.setId_ArquivoTipo("1");
//		pendenciaTipoDt.setId_UsuarioLog("1");
//		pendenciaTipoDt.setPendenciaTipo("1");
////		ps.consultarIntimacoesCitacoesLidas(UsuarioSessao, "1", "1", pendenciaTipoDt , "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "01/04/2011 12:25:12", "30/04/2011 12:25:12", "0");
////		
////		pendenciaTipoDt.setId("12556");
////		ps.consultarIntimacoesCitacoesLidas(UsuarioSessao, "1", "1", pendenciaTipoDt , "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "01/04/2011 12:25:12", "30/04/2011 12:25:12", "0");
////		
////		ps.consultarIntimacoesDistribuidasPromotoria(UsuarioSessao, "1", "1", "500010767", "01/02/2011 12:25:12", "30/03/2011 12:25:12", "0");
////		
////		ps.consultarIntimacoesDistribuidasProcuradoria(UsuarioSessao, "1", "1", "500010767", "01/02/2011 12:25:12", "30/03/2011 12:25:12", "0");
////		
////		ps.consultarPrazosDecorridos(UsuarioSessao.getUsuarioDt(), pendenciaTipoDt, "500010767", "0", true);
////		
////		ps.consultarPendenciasLeituraAutomatica();
////		
////		ps.consultarPendenciasFechamentoAutomatica();
////		
////		ps.consultarQtdPrazosDecorridos(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarQtdPendenciaLiberaAcesso(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarQtdPendenciaInformativa(UsuarioSessao.getUsuarioDt());	
////		
////		ps.consultarPendenciasLiberarAcessoComHash(UsuarioSessao);
////		
////		ps.consultarPendenciasInformativas(UsuarioSessao);
////		
////		ps.consultarPrazosDecorridosUsuario(UsuarioSessao.getUsuarioDt(), pendenciaTipoDt, "500010767", "0");
//		
//		PendenciaStatusDt pendenciaStatusDt = new PendenciaStatusDt();
//		pendenciaStatusDt.setCodigoTemp("1");
//		pendenciaStatusDt.setId("1");
//		pendenciaStatusDt.setId_UsuarioLog("1");
//		pendenciaStatusDt.setIpComputadorLog("127.0.0.1");
//		pendenciaStatusDt.setPendenciaStatus("1");
//		pendenciaStatusDt.setPendenciaStatusCodigo("1");
//			
////		ps.consultarFinalizadas(UsuarioSessao, "1", "1", pendenciaTipoDt, pendenciaStatusDt, false, 1, "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "01/04/2011 12:25:12", "30/04/2011 12:25:12", "0");
////		
////		ps.consultarPendenciasFechadas(UsuarioSessao, "1", "1", pendenciaTipoDt, pendenciaStatusDt, false, 1, "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "01/04/2011 12:25:12", "30/04/2011 12:25:12", "0");
////		
////		ps.consultarRespondidasServentia(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, true, 1, "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "01/04/2011 12:25:12", "30/04/2011 12:25:12", "0");
////		
////		ps.consultarRespondidasUsuario(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, true, 2, "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "01/04/2011 12:25:12", "30/04/2011 12:25:12", "0");
//		
//		PendenciaDt pendenciaDt = new PendenciaDt();
//		pendenciaDt.setDataVisto("05/04/2011 14:30:58");
//		pendenciaDt.setId("12560");
//		pendenciaDt.setPendenciaStatusCodigo("1");
////		ps.marcarVisto(pendenciaDt);
////		
////		ps.alterarLimite(pendenciaDt);
////		
////		ps.retirarVisto(pendenciaDt);
//		
////		ps.AlterarStatusPendencia(pendenciaDt);
////		
////		// ps.responder("12816", "1", 1, "15/04/2011 12:25:12");
////		
////		// ps.FinalizarPendencia("12557", "1", 1, true);
////		
////		ps.descartarPendencia("12557", "1", 1);
////		
////		ps.finalizarPendenciaIntimacaoAguardandoParecer("12816");
////		
////		ps.marcarPendenciaIntimacaoAguardandoParecer("12816");
////		
////		ps.consultarFilhas(pendenciaDt);
////		
////		ps.consultarFilhasAutos(pendenciaDt, true);
////		
////		ps.consultarMinhas(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, true, 1, "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "0");
////		
////		ps.consultarExpedidasServentia(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, true, 2, "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "0");
////		
////		ps.consultarQtdPendenciasAndamento(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarQtdExpedidasAguardandoVisto(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarTiposServentiaCargoJuiz("1");
////		
////		ps.consultarPendenciasServentiaAbertasEmAndamento(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarPendenciasCargoServentiaAbertasEmAndamento(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarQuantidadeServentiaTipoAbertasEmAndamento(UsuarioSessao.getUsuarioDt(), "1");
////		
////		ps.consultarPorTipo(2, "01/03/2011 12:25:12", "03/03/2011 12:25:12", "0");
//		
////		String[] palavras = {"teste", "1213", "testando"};
////		ps.consultarTextoPublicacaoPublica("01/03/2011 12:25:12", "31/03/2011 12:25:12", "1", palavras);
////		
////		ps.consultarTextoPublicacaoPublica("01/03/2011 12:25:12", "31/03/2011 12:25:12", "1", palavras, "0");
////		
////		ps.consultarPublicacaoPublica("01/03/2011 12:25:12", "31/03/2011 12:25:12", "1", "0");
////		
////		ps.consultarAbertasServentiaCargo(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, false, 2, "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "0");
////		
////		ps.consultarAbertas(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, true, 1, "500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "0");
////		
////		ps.consultarAbertas(UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt, false, 2,"500010767", "01/03/2011 12:25:12", "30/03/2011 12:25:12", "0", "1");
////		
////		ps.consultarReservadasPreAnalisadasComHash(UsuarioSessao);
////		
////		ps.consultarReservadasComHash(UsuarioSessao, "1", "3");
////		
////		ps.consultarPreAnalisadasComHash(UsuarioSessao, "1", "3");
////		
////		ps.consultarAbertasServentiaCargoComHash(UsuarioSessao, "3");
////		
////		ps.consultarReservadas(UsuarioSessao.getUsuarioDt(), true);
////		
////		ps.consultarTiposAtivasCargoServentiaPaginaInicialPromotor(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarTiposAtivasServentiaTipoPaginaInicial(UsuarioSessao.getUsuarioDt());
//		
//		PendenciaDt dados = new PendenciaDt();
//		
//		dados.setPendenciaTipoCodigo("1");
//		dados.setId_Movimentacao("1");		
//		dados.setId_Processo("2053");
//		dados.setProcessoPrioridadeCodigo("1");
//		dados.setId_ProcessoParte("1");
//		dados.setPendenciaStatusCodigo("1");
//		dados.setId_UsuarioCadastrador("1");
//		dados.setId_UsuarioFinalizador("1");
//		dados.setDataFim("05/04/2011 12:25:47");
//		dados.setDataLimite("05/04/2011 12:25:47");
//		dados.setDataDistribuicao("05/04/2011 12:25:47");
//		dados.setPrazo("1");
//		dados.setId_PendenciaPai("1");
//		dados.setDataVisto("05/04/2011 12:25:47");		
//		//ps.inserir(dados);
//		
//		dados.setId_PendenciaTipo("1");
//		dados.setId_ProcessoPrioridade("2");
//		dados.setId_PendenciaStatus("1");
//		dados.setDataInicio("15/01/2010 14:30:14");
////		ps.inserirPendenciaLiberarAcesso(dados);
////		
////		ps.encaminhar(dados, UsuarioSessao.getUsuarioDt(), (new Date()));
////		
////		ps.finalizar(dados, UsuarioSessao.getUsuarioDt());
////		
////		ps.finalizarPrazoDecorrido(dados, UsuarioSessao.getUsuarioDt());
//		
//		dados.setId("250");
////		ps.fecharPendencia(dados, UsuarioSessao.getUsuarioDt());
////		
////		ps.distribuir(dados.getId(), UsuarioSessao.getUsuarioDt().getId());
////		
////		ps.consultarUltimaDistribuicaoMandado("1");
////		
////		ps.liberar("1");
//		
////		ps.consultarConclusoesPendentes("1", "5000107", "67", "1", "1");
////		
////		ps.consultarPendenciasNaoAnalisadas("1", "1", "5000107", "67","1");
////		
////		ps.reservarUltimaServentiaCargo("1", "1");
////		
////		ps.reservarUltimaServentia("1", "1");
////		
////		ps.reservarUltimaServentiaTipo("1", "1");
////		
////		ps.consultarConclusoesFinalizadas("1", "5000107", "67", "01/03/2011 14:30:47", "10/03/2011 15:25:36");
////		
////		ps.consultarConclusoesAbertas("500010767");
////		
////		ps.consultarConclusaoAbertaProcesso("500010767", "1");
////		
////		ps.consultarQuantidadePendenciasProcesso("500010767", "1");
////		
////		ps.consultarPendenciasServentiaReservadas(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarPendenciasCargoServentiaReservadas(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarQuantidadeServentiaTipoReservadasPorTipo(UsuarioSessao.getUsuarioDt(), "1");
////		
////		ps.consultarPendenciasServentiaPreAnalisadas(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarPendenciasCargoServentiaPreAnalisadas(UsuarioSessao.getUsuarioDt());
////		
////		ps.consultarQuantidadeServentiaTipoPreAnalisadasPorTipo(UsuarioSessao.getUsuarioDt(), "1");
//		
//		//ps.relUsuariosPendencias("1", "1", "2011", "12"); // TODO: viewUsuariosPendencias01 não existe.
//		
//		ps.consultarTiposConclusoesAbertas("1");
//		
//		//ps.consultarQuantidadeConclusoesNaoAnalisadas("1");
//		
//		ps.consultarQuantidadePendenciasNaoAnalisadas("1", "1");
//		
//		//ps.consultarQuantidadeConclusoesPreAnalisadas("1", false);
//		
//		ps.consultarQuantidadeConclusoesPreAnalisadasPendentesAssinatura("1", false);
//		
//		ps.consultarQuantidadePendenciasPreAnalisadas("1", "1", true);
//		
//		ps.consultarQuantidadePendenciasPreAnalisadasPendentesAssinatura("1", "1", false);
//		
//		ps.consultarIntimacoesCitacoesAdvogado(UsuarioSessao);
//		
//		ps.consultarIntimacoesProcuradorProcesso("2053", "1");
//		
//		ps.consultarIntimacoesPromotor(UsuarioSessao);
//		
//		ps.consultarIntimacoesProcuradoria(UsuarioSessao);
//		
//		ps.consultarIntimacoesPromotoria(UsuarioSessao);
//		
//		ps.consultarPendenciasDistribuidorGabinete(UsuarioSessao);
//		
//		ps.consultarConclusoesDistribuidorGabinete(UsuarioSessao);
//		
//		ps.consultarIntimacoesPromotorAguardandoParecer(UsuarioSessao);
//		
//		ps.consultarIntimacoesLidasAutomaticamentePromotorAguardandoParecer(UsuarioSessao);
//		
//		ps.consultarIntimacoesAdvogadoAguardandoPeticionamento(UsuarioSessao);
//		
//		ps.consultarIntimacoesLidasAutomaticamenteAdvogadoAguardandoPeticionamento(UsuarioSessao);
//		
//		ps.consultarQuantidadePendenciasAdvogadosEmAndamento(UsuarioSessao.getUsuarioDt(), "1");
//		
//		ps.consultarTiposAtivasPaginaInicalAdvogado(UsuarioSessao.getUsuarioDt());
//		
//		ps.consultarTiposAtivasPaginaInicalAssistenteAdvogado(UsuarioSessao.getUsuarioDt());
//		
//		ps.consultarPendenciasAtivasServentiaCargo("1");
//		
//		ps.consultarConclusoesAbertasServentiaCargo("1");
//		
//		ps.consultarPendenciasAbertasProcesso("1", "1");
//		
//		ps.consultarPendenciasVotoEmentaProcesso("1", "1", "1");
//		
//		ps.consultarPendenciasProcesso("2053");
//		
//		ps.consultarPendenciasProcessoHash("2053", UsuarioSessao);
//		
//		ps.consultarPendenciaSuspensaoProcesso("2053");
//		
//		ps.consultarPendenciasAnalisadas("1", "1", "5000107","67", "10/01/2010 14:30:25","30/04/2011 15:30:47");
//		
//		ps.consultarIdPendenciasProcessoNaoAnalisadas("1", "2053");
//		
//		ps.consultarIdPendenciasProcessoPreAnalisadas("1", "2053", false);
//		
//		ps.consultarIdPendenciasProcessoPreAnalisadasPendentesAssinatura("1", "2053",false);
//		
//		ps.marcarIntimacaoDistribuida("12801");
//	}
//	
//	private static void testeRelatorioEstatisticaPs(FabricaConexao fabricaconexao) throws Exception{		
//		RelatorioEstatisticaPs ps = new RelatorioEstatisticaPs();
//		ps.setConexao(fabricaconexao);
//		
////		RelatorioEstatisticaDt tipoRelatorio = new RelatorioEstatisticaDt();
////		tipoRelatorio.setId_Serventia("1");
////		tipoRelatorio.setDataInicial("01/01/2010");
////		tipoRelatorio.setDataFinal("31/03/2011");
////		ps.consultarDadosRelatorioEstatisticaServentia(tipoRelatorio);
////		
////		ps.consultarDadosRelatorioEstatisticaUsuario(tipoRelatorio);
////		
////		ps.consultarDadosRelatorioEstatisticaUsuarioServentia(tipoRelatorio);
////		
////		ps.relSumarioProcessos("2010", "01", "2010", "12", "1", "1", "1", "1", "1");
////		
////		ps.relSumarioProcessos("2010", "01", "2011", "12", "1", "1", "1", "1", "1");
////		
////		ps.relSumarioProdutividade("2010", "01", "2010", "12", "1", "1", "1", "1", "1", "1", "1");
////		
////		ps.relSumarioProdutividade("2010", "01", "2011", "12", "1", "1", "1", "1", "1", "1", "1");
////		
////		ps.relSumarioPendencia("2010", "01", "2010", "12", "1", "1", "1" ,1, "1", "1");
////		
////		ps.relSumarioPendencia("2010", "01", "2011", "12", "1", "1", "1" ,RelatorioSumarioDt.COMARCA_SERVENTIA, "1", "1");
////		
////		ps.relSumarioPendencia("2010", "01", "2011", "12", "1", "1", "1" ,RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO, "1", "1");
////		
////		ps.relSumarioPendencia("2010", "01", "2011", "12", "1", "1", "1" ,RelatorioSumarioDt.COMARCA, "1", "1");
////		
////		List listaEstatisticas = new ArrayList();
////		listaEstatisticas.add("1");
////		ps.imprimirRelatorioEstPro("2010", "01", "2010", "12", "1", "1", "1", "1", listaEstatisticas, "1", "1");
////		
////		listaEstatisticas.add("2");
////		ps.imprimirRelatorioEstPro("2010", "01", "2011", "12", "1", "1", "1", "1", listaEstatisticas, String.valueOf(RelatorioSumarioDt.COMARCA), "1");
////		
////		ps.imprimirRelatorioEstPro("2010", "01", "2011", "12", "1", "1", "1", "1", listaEstatisticas, String.valueOf(RelatorioSumarioDt.COMARCA_SERVENTIA), "1");
////		
////		ps.imprimirRelatorioEstPro("2010", "01", "2011", "12", "1", "1", "1", "1", listaEstatisticas, String.valueOf(RelatorioSumarioDt.COMARCA_SERVENTIA_USUARIO), "1");
////		
////		ps.relAnaliticoProdutividade("2010", "12", "1", "1","1", "1", "1", "0");
////		
////		ps.relAnaliticoProcesso("2010", "12", "1", "1", "1", "0");
////		
////		ps.relAnaliticoPendencia("2010", "10", "1", "1", "1", "1", "0");
////		
////		ps.relMaioresPromoventesPromovidos("1", "2");
////		
////		ps.relSumarioMaisProcessosServentia(String.valueOf(RelatorioSumarioProcessosServentiaDt.ATIVO), "15");
////		
////		ps.relSumarioMaisProcessosServentia(String.valueOf(RelatorioSumarioProcessosServentiaDt.ARQUIVADO), "15");
////		
////		ps.relSumarioMaisProcessosServentia(String.valueOf(RelatorioSumarioProcessosServentiaDt.DISTRIBUIDO), "15");
////		
////		ps.relSumarioMaisProcessosComarca(String.valueOf(RelatorioSumarioProcessosServentiaDt.ATIVO), "10");
////		
////		ps.relSumarioMaisProcessosComarca(String.valueOf(RelatorioSumarioProcessosServentiaDt.ARQUIVADO), "10");
////		
////		ps.relSumarioMaisProcessosComarca(String.valueOf(RelatorioSumarioProcessosServentiaDt.DISTRIBUIDO), "10");
//		
//		ps.imprimirRelatorioProcessoSegundoGrauArea("2011", "1");
//		
//		ps.imprimirRelatorioProcessoSegundoGrauDesembargadoresServentia("2011", "1");
//		
//		ps.imprimirRelatorioProcessoPrimeiroGrauJuizes("2011", "1");
//	}
//	
//	private static void testeProcessoExecucaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoExecucaoPs ps = new ProcessoExecucaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ProcessoExecucaoDt dados = new ProcessoExecucaoDt();
//		dados.setId_ProcessoExecucaoPenal("2053");
//		dados.setId_ProcessoAcaoPenal("2053");
//		dados.setId_CidadeOrigem("5");
//		dados.setDataAcordao("15/03/2011");
//		dados.setDataDistribuicao("01/03/2011");
//		dados.setDataPronuncia("10/03/2011");
//		dados.setDataSentenca("20/03/2011");
//		dados.setDataTransitoJulgado("23/03/2011");
//		dados.setDataDenuncia("17/03/2011");
//		dados.setDataAdmonitoria("23/03/2011");
//		dados.setDataInicioCumprimentoPena("30/03/2011");
//		dados.setNumeroAcaoPenal("103");
//		dados.setVaraOrigem("102");		
//		ps.inserir(dados);
//		
//		ps.alterar(dados);
//		
//		ps.listarAcoesPenais("1");
//		
//		ps.listarAcoesPenaisComCondenacoes("1");
//		
//		ps.listarTransitoComTotalCondenacao_HashMap("1", "1", "1");
//		
//		ps.consultarAcaoPenalComCondenacao("1");
//		
//		/*
//		 * SPG		
//		ProcessoExecucaoDt processoExecucaoDt = new ProcessoExecucaoDt();		
//		ProcessoParteDt processoParteDt = new ProcessoParteDt();
//		ps.consultarGuiaRecolhimento("12325", processoExecucaoDt, processoParteDt);
//		*/
//		
//		CalculoLiquidacaoDt calculo = new CalculoLiquidacaoDt();
//		calculo.setIdProcesso("2053");
//		CalculoLiquidacaoProgressaoLivramentoDt calculoDt = new CalculoLiquidacaoProgressaoLivramentoDt();
//		calculoDt.setDataRequisitoTemporalProgressao("10/01/2011");
//		calculoDt.setDataRequisitoTemporalProgressao("10/01/2011");
//		calculo.setCalculoProgressaoLivramentoDt(calculoDt);
//		calculo.setDataValidadeMandadoPrisao("10/02/2011");
//		calculo.setDataTerminoPena("10/03/2011");
//		calculo.setInformacaoAdicional("Testando 123...");		
//		ps.inserirCalculoLiquidacao(calculo );
//		
//		ps.alterarCalculoLiquidacao(calculo);
//		
//	}	
//	
//	private static void testeProcessoPartePs(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoPartePs ps = new ProcessoPartePs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarUmPromovente("2053");
//		ps.consultarUmPromovido("2053");
//		ps.consultarParteTipo("2053", 1);
//		ps.consultarId("1");
//		ps.consultarPartes("2053", "1");
//		ps.consultarIdCompleto("1");
//		ps.getListaPartes("2053", 1, true);
//		ps.consultarRgCompleto("1");
//		ps.consultarRgCompleto(1);
//		ProcessoParteDt processoParte = new ProcessoParteDt();
//		processoParte.setId("1");
//		ps.consultarRgCompleto(processoParte);
//		//ps.consultarEndereco("1"/; //TODO: Está com erro, pois está reutilizando outro Ps sem passar o objeto Conexão.
//		//ps.consultarEndereco(1); //TODO: Está com erro, pois está reutilizando outro Ps sem passar o objeto Conexão.
//		//EnderecoDt endereco = new EnderecoDt();
//		//endereco.setId("1");
//		processoParte.setEndereco("1");
//		//ps.consultarEndereco(processoParte); //TODO: Está com erro, pois está reutilizando outro Ps sem passar o objeto Conexão.
//		ps.consultarParte("12256", "3673333", "12252663", "12252", "125263");
//		ps.listarPartesProcessoExecucao("114452", "tes", "tsw", "25/03/1985","15526","82", "2252", "0", false);
//		
//		
//		ProcessoParteDt dados = new ProcessoParteDt();
//		dados.setNome("Nome parte");
//		dados.setSexo("M");
//		dados.setNomeMae("Nome Mae");
//		dados.setNomePai("Nome Pai");
//		dados.setProcessoParteTipoCodigo("1");
//		dados.setId_ProcessoParteTipo("1");
//		dados.setId_Processo("2053");
//		dados.setId_ProcessoParteAusencia("1");
//		dados.setId_Naturalidade("766");
//		dados.setDataNascimento("26/05/1980");
//		dados.setId_EstadoCivil("1");
//		dados.setId_Profissao("784");
//		dados.setId_Endereco("1");
//		dados.setRg("123456");
//		dados.setId_RgOrgaoExpedidor("1");
//		dados.setRgDataExpedicao("15/06/1995");
//		dados.setCpf("00159830133");
//		dados.setCnpj("0165836520000129");
//		dados.setTituloEleitor("12563");
//		dados.setTituloEleitorZona("123");
//		dados.setTituloEleitorSecao("12");
//		dados.setCtps("152");
//		dados.setCtpsSerie("1225");
//		dados.setId_CtpsUf("1");
//		dados.setPis("12235");
//		dados.setEMail("date@gmail.com");
//		dados.setTelefone("35232415");
//		dados.setCelular("82364525");
//		dados.setCitacaoOnline("true");
//		dados.setIntimacaoOnline("true");
//		dados.setRecebeEMail("true");
//		dados.setCitada("false");
//		dados.setEmpresaTipo("1");
//		dados.setGovernoTipo("1");			
//		ps.inserir(dados );
//		
//		ps.alterar(dados);
//		
//		ps.desabilitaParteProcesso(dados.getId());
//		
//		ps.restauraParteProcesso(dados.getId());
//		
//		ps.consultarUsuarioServentiaParte(dados.getId());
//		
//		ProcessoParteDt processoParteDt = new ProcessoParteDt();
//		processoParteDt.setId_UsuarioServentia("1");
//		processoParteDt.setId_ProcessoParte(dados.getId());
//		ps.vinculaUsuarioParte(processoParteDt);
//		
//		ps.marcarCitacaoParteProcesso(dados.getId());
//		
//		ps.isParteProcesso("1", "2053");
//		
//		ps.consultarPartesProcesso("2053", true);
//		
//		ps.consultarParteSimplificar();
//		
//	}
//	
//	private static void testeAreaDistribuicaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		AreaDistribuicaoPs ps = new AreaDistribuicaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		AreaDistribuicaoDt dados = new AreaDistribuicaoDt();				
//		dados.setAreaDistribuicao("Area Teste ps");		
//		dados.setAreaDistribuicaoCodigo("961");
//		dados.setId_Forum("1");
//		dados.setId_ServentiaSubtipo("1");
//		dados.setId_AreaDistribuicaoRelacionada("2");
//		dados.setCodigoTemp("1");		
//		ps.inserir(dados);
//		
//		ps.consultarAreasDistribuicao("t", "1", "0");
//		
//		ps.consultarAreasDistribuicaoCivel("t", "1", "0");
//		
//		ps.consultarAreasDistribuicaoCriminal("t", "1", "0");
//		
//		ps.consultarAreasDistribuicaoAtivas();
//		
//		ps.consultarAreasDistribuicaoVaraAtivas("t", "0");
//		
//		ps.consultarAreasDistribuicaoAtivas("t","0");
//		
//		ps.consultarAreasDistribuicaoSegundoGrauAtivas("t", "0");
//		
//		ps.consultarAreaDistribuicaoCodigo("1");
//		
//		ps.consultarAreasDistribuicaoComarcaServentiaSubTipo("t", "1", "1", "0");
//		
//		ps.consultarAreaDistribuicaoPrecatoria("1");
//		
//		ps.consultarAreasDistribuicaoServentiaSubTipo("1", "1");
//		
//		ps.consultarAreasDistribuicaoSegundoGrauCivel("t", "1", "0");
//		
//		ps.consultarAreasDistribuicaoSegundoGrauCriminal("t", "1", "0");
//		
//		ps.consultarAreasDistribuicao("t", "1","0");
//		
//		ps.consultarDescricaoForum("1");
//		
//		ps.consultarAreasDistribuicaoServentia("1");
//		
//		ps.atualizarStatus("1", "0");
//	}
//	
//	private static void testeGuiaEmissaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		GuiaEmissaoPs ps = new GuiaEmissaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		GuiaEmissaoDt dados = new GuiaEmissaoDt();		
//		GuiaModeloDt guia = new GuiaModeloDt();
//		guia.setId("10663");
//		dados.setGuiaModeloDt(guia);
//		dados.setId_AreaDistribuicao("1");
//		dados.setId_ProcessoPrioridade("2053");
//		dados.setDataVencimento("30/04/2010");				
//		dados.setValorAcao("1500.00");
//		dados.setNumeroGuiaCompleto("1525970");
//		dados.setNumeroProcessoDependente("2053");
//		dados.setId_Comarca("1");
//		dados.setId_Serventia("1");
//		dados.setId_Usuario("1");		
//		dados.setId_Processo("2053");
//		dados.setId_ProcessoTipo("1");
//		dados.setId_GuiaEmissaoPrincipal("1");			
//		dados.setRequerente("Teste requerente");
//		dados.setRequerido("Teste requerido");
//		dados.setOutrasPartes("Teste");
//		dados.setAdvogado("Teste advogado");
//		dados.setAssuntos("Teste Assunto");
//		dados.setNumeroDUAM("1235");
//		//dados.setQuantidadeAcressimo("12");
//		dados.setDataVencimento("30/04/2011");
//		dados.setValorImpostoMunicipalDUAM("150.36");
//		
//		//ps.inserir(dados);
//		
//		ps.alterar(dados);
//		
//		ps.isGuiaPaga("152563");
//		
//		//ps.isGuiaPaga(dados);
//	
//		ps.consultarGuiasIdUsuario("1", "1");
//		
//		ps.consultarNumeroCompleto("152563", "1", "1");
//		
//		ps.consultarGuiaEmissao("1");
//		
//		List lista = new ArrayList();
//		lista.add("1");
//		lista.add("152563");
//		lista.add("152565");
//		lista.add("152564");
//		
//		//ps.consultarGuiaEmissao(lista);
//		
//		ps.consultarGuiaEmissao("2053",lista);
//		
//		ps.consultarId("1");
//		
//		ps.consultarIdGuiaTipo("1", "152563","1");
//		
//		ProcessoDt processoDt = new ProcessoDt();
//		processoDt.setId("2053");
//		GuiaTipoDt guiaTipoDt = new GuiaTipoDt();
//		guiaTipoDt.setId("1");
//		ps.possuiGuiaEmitida(processoDt, guiaTipoDt);
//		
//		ps.consultarUltimoNumeroGuia();
//		
//		ps.atualizarPagamentos("1", "30/04/2011");
//		
//		//ps.atualizarPagamento("152563", 1, "20/04/2011");
//		
//		ps.consultarTotalGuia("152563");
//	
//	}
//	
//	private static void testeProcessoResponsavelPs(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoResponsavelPs ps = new ProcessoResponsavelPs();
//		ps.setConexao(fabricaconexao);
//		
//		ProcessoResponsavelDt dados = new ProcessoResponsavelDt();
//		dados.setId_ServentiaCargo("1");
//		dados.setId_Processo("2053");
//		dados.setCargoTipoCodigo("1");		
//		ps.inserir(dados);
//		dados.setId_CargoTipo("1");		
//		ps.inserir(dados);
//		
//		ps.isResponsavelProcesso("1", "2053");
//		
//		ps.consultarServentiaCargoResponsavelProcesso("1", "1", "1", true);
//		
//		ps.consultarServentiaCargoResponsavelProcessoSegundoGrau("1", "1");
//		
//		//ps.consultarProcessoResponsavelCargoTipo("1", "1", "1");
//		
//		ps.consultarResponsaveisProcesso("1");
//		
//		ps.consultarJuizesSegundoGrauResponsaveisProcesso("2053");
//		
//		ps.consultarJuizSegundoGrauGabinete("1");
//		
//		ps.consultarJuizesTurma("1");
//		
//		ps.consultarResponsaveisProcessoAcessoExterno("2053");
//		
//		List teste = ArrayList();
//		teste.add("1");
//		ps.desativarResponsaveisProcesso("2053", teste);
//		
//		teste.add("2");
//		teste.add("3");
//		teste.add("4");
//		ps.ativarResponsaveisProcesso("2053", teste);
//		
//		ps.alterarResponsavelProcesso("397", "1", "2");
//		
//		ps.consultarUsuarioResponsavelProcesso("397", "1", 1);
//		
//		ps.consultarUsuarioResponsavelCargoTipo("397", "1", 1);
//		
//		ps.consultarListaServentiaCargoResponsavelProcesso("397", "1", 1, true);
//		
//		ps.consultarRelatorResponsavelProcessoSegundoGrau("397");
//		
//		ps.consultarRevisorResponsavelProcessoSegundoGrau("397");
//		
//		ps.consultarVogalResponsavelProcessoSegundoGrau("397");
//		
//		ps.consultarIdProcessoResponsavel("1", "397");
//		
//		ps.alterarResponsavelProcesso("397", "397");
//		
//		ps.limparResponsavelProcesso("397");		
//		
//		
//	}
//	
//	private static List ArrayList() {
//		
//		return null;
//	}
//
//	private static void testeProcessoEventoExecucaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoEventoExecucaoPs ps = new ProcessoEventoExecucaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.listarEventos("1", "1");
//		ps.isProcessoOrigemMedidaSeguranca("1");
//		List listaTipoEvento = new ArrayList();
//		listaTipoEvento.add((long)1);
//		listaTipoEvento.add((long)2);
//		listaTipoEvento.add((long)3);
//		ps.consultarUltimoEvento("1", listaTipoEvento);
//		
//		ps.calcularSaldo("1", "05/04/2011", listaTipoEvento);
//		
//		ps.consultarIdCompleto("1");
//		
//		listaTipoEvento.clear();
//		ProcessoEventoExecucaoDt dados = new ProcessoEventoExecucaoDt(); 
//		dados.setId("1");
//		dados.setDataFim("20/03/2011");
//		listaTipoEvento.add(dados);
//		dados = new ProcessoEventoExecucaoDt(); 
//		dados.setId("1");
//		dados.setDataFim("20/03/2011");
//		listaTipoEvento.add(dados);
//		ps.alterarDataFim(listaTipoEvento);
//		
//		CalculoLiquidacaoDt calculoLiquidacaoDt = new CalculoLiquidacaoDt();
//		calculoLiquidacaoDt.setId("1");
//		ps.consultarCalculoLiquidacao(calculoLiquidacaoDt);		
//		
//		dados.setDataInicio("19/10/1978");		
//		dados.setDataFim("15/09/2010");			
//		dados.setQuantidade("1");			
//		dados.setObservacao("observacao");	
//		//dados.setId_LivramentoCondicional("1");			
//		dados.setId_Movimentacao("13261");
//		dados.setId_EventoExecucao("1");			
//		dados.setId_ProcessoExecucao("1");
//		dados.setConsiderarTempoLivramentoCondicional("true");		
//		
//		ps.inserir(dados);
//		
//		ps.alterar(dados);
//		
//		ps.consultarId_ProcessoEventoExecucao("1", "1");
//		
//		ps.listarIdProcessoEventoExecucao_TJ_GRP("1");
//		
//		ps.listarModalidadesDaAcaoPenal("1");
//		
//		ps.consultarIdMovimentacao("1", "1");
//		
//		ps.consultarProcessoEventoExecucao("1", "1");
//		
//	}
//	
//	private static void testeCertificadoPs(FabricaConexao fabricaconexao) throws Exception{		
//		CertificadoPs ps = new CertificadoPs();
//		ps.setConexao(fabricaconexao);
//		
////		CertificadoDt dados = ps.consultarCertificadoSistema();		
////		ps.inserir(dados);
////		ps.inserirCaConfiavel(dados);
////		ps.alterar(dados);
////		ps.atualizaConteudoArquivoP12(dados);
////		ps.consultarCertificadoValidoUsuario("1");
////		ps.consultarCertificadoRaizSistema();
////		ps.consultarCertificadoEmissorSistema();
////		ps.consultarCertificadoUsuario("1");
////		ps.consultarDescricaoCertificadoConfiavel("t");
////		ps.consultaCertificadosConfiaveis();
////		ps.consultaCertificadosNaoLiberados("t", "0");
////		ps.consultaCertificadosNaoRevogados("t", "0");
////		ps.consultaCertificadosUsuario("1");
//		ps.consultaCertificadosRevogados();
//	}
//	
//	private static void testeDistribuirMandadoPs(FabricaConexao fabricaconexao) throws Exception{		
//		DistribuirMandadoPs ps = new DistribuirMandadoPs();
//		ps.setConexao(fabricaconexao);
//		
//		MandadoJudicialDt mandadoJudicialDt = new MandadoJudicialDt();
//		mandadoJudicialDt.setId("1");
//		ps.consultarId(mandadoJudicialDt);
//		
//		mandadoJudicialDt.setDataDistribuicao("04/04/2011");
//		mandadoJudicialDt.setDataLimite("20/04/2011 00:00:00");
//		UsuarioServentiaDt usuario = new UsuarioServentiaDt();
//		usuario.setId("1");
//		mandadoJudicialDt.setUsuarioServentiaDt_1(usuario);
//		usuario = new UsuarioServentiaDt();
//		usuario.setId("4");
//		mandadoJudicialDt.setUsuarioServentiaDt_1(usuario);
//		mandadoJudicialDt.setId_MandadoTipo("1");
//		mandadoJudicialDt.setId_ProcessoParte("4388");
//		mandadoJudicialDt.setId_EnderecoParte("1");
//		mandadoJudicialDt.setId_Pendencia("12572");
//		mandadoJudicialDt.setId_Area("1");
//		mandadoJudicialDt.setId_Zona("1");
//		mandadoJudicialDt.setId_Regiao("1");
//		mandadoJudicialDt.setId_Bairro("1");
//		mandadoJudicialDt.setId_Escala("1");
//		mandadoJudicialDt.setAssistencia("true");
//		mandadoJudicialDt.setLocomocoesFrutiferas("1");
//		mandadoJudicialDt.setLocomocoesInfrutiferas("1");
//		mandadoJudicialDt.setLocomocaoHoraMarcada("1");
//		ps.inserir(mandadoJudicialDt);
//		
//		ps.consultarPorIdPendencia("1");
//		
//		ps.alterarStatusMandadoJudicial(mandadoJudicialDt, 1);
//		
//		ps.consultarMandadosPrazosExpirados();
//		
//		ps.consultarMandadosRetornadosPrazosExpirados();
//		
//		ps.alterarDataLimite(mandadoJudicialDt);
//		
//		ps.alterar(mandadoJudicialDt);
//	}
//	
//	private static void testeRelatorioProcessoExecucaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		RelatorioProcessoExecucaoPs ps = new RelatorioProcessoExecucaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		DataProvavelDt dataProvavelDt = new DataProvavelDt();
//		dataProvavelDt.setDataInicialPeriodo("19/10/1978");
//		dataProvavelDt.setDataFinalPeriodo("19/10/2010");
//		dataProvavelDt.setTipoConsulta("PROGRESSAO");		
//		ps.consultarPeriodoDataProvavel(dataProvavelDt , "0", "1");		
//		dataProvavelDt.setTipoConsulta("LIVRAMENTO");
//		ps.consultarPeriodoDataProvavel(dataProvavelDt , "0", "1");
//		dataProvavelDt.setTipoConsulta("MANDADOPRISAO");
//		ps.consultarPeriodoDataProvavel(dataProvavelDt , "0", "1");
//		
//		ps.consultarProcessoSemCalculo("0", "1");
//		
//		SituacaoAtualExecucaoPenalDt situacaoAtualDt = new SituacaoAtualExecucaoPenalDt();
//		situacaoAtualDt.setIdSituacao("1");
//		situacaoAtualDt.setIdRegime("1");
//		situacaoAtualDt.setIdLocalCumprimentoPena("1");
//		situacaoAtualDt.setIdModalidade("1");		
//		ps.consultarSituacaoAtualProcessoExecucao(situacaoAtualDt , "0", "1");
//		
//	}
//	
//	private static void testeProcessoParteAdvogadoPs(FabricaConexao fabricaconexao) throws Exception{		
//		ProcessoParteAdvogadoPs ps = new ProcessoParteAdvogadoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ps.consultarAdvogadosProcesso("1");
//		ps.consultarProcessoParteProcurador("1", "1");
//		ps.consultarAdvogadosParte("1");
//		ps.consultarAdvogadosRecebemIntimacaoParte("1");
//		ps.consultarAdvogadosProcesso("1");
//		ps.consultarProcuradoresProcesso("1", "1");
//		
//		/*ProcessoParteAdvogadoDt dados = new ProcessoParteAdvogadoDt(); 
//		dados.setId_UsuarioServentiaAdvogado("1");
//		dados.setId_ProcessoParte("1");
//		ps.inserir(dados);
//		
//		dados.setPrincipal("true");
//		ps.alterar(dados); // TODO: Coluna NomeAdvogado não existe na tabela.
//		*/
//		
//		ps.atualizarProcurador("1", "2", "1", "1");
//		
//		ps.desabilitaAdvogado("1");
//		
//		ProcessoParteAdvogadoDt processoParteAdvogadoDt = new ProcessoParteAdvogadoDt();
//		processoParteAdvogadoDt.setId("999");
//		ps.defineAdvogadoPrincipal(processoParteAdvogadoDt);
//		
//		ps.removeAdvogadoPrincipalProcesso("9999");
//		
//		ps.isAdvogadoProcesso("1", "1540");
//		
//		ps.consultarAdvogadosProcessoParte("1540", "1");
//		processoParteAdvogadoDt.setRecebeIntimacao(false);
//		ps.modificarRecebimentoIntimacaoAdvogado(processoParteAdvogadoDt);
//		
//		ps.consultarServentiaUFHabilitacaoAdvogados("1");
//	}
//	
//	private static void testeCondenacaoExecucaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		CondenacaoExecucaoPs ps = new CondenacaoExecucaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		CondenacaoExecucaoDt dados = ps.consultarId("361");
//		ps.alterar(dados);
//		
//		ps.listarCondenacaoDaAcaoPenal("40");		
//		ps.setParametroCrime(ps.listarCondenacaoNaoExtintaDaAcaoPenal("65"));		
//		ps.listarCondenacaoNaoExtintaDoProcesso("36");
//		ps.consultarTempoTotalCondenacaoDias("25");
//		ps.listarCrimeCondenacao("36");
//		ps.consultarCrimesAtivosProcesso("40");
//		ps.consultarCondenacaoSituacao("2");
//	}
//	
//	private static void testeUsuarioServentiaPs(FabricaConexao fabricaconexao) throws Exception{		
//		UsuarioServentiaPs ps = new UsuarioServentiaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarUsuariosServentia("t", "0", "1");
//		ps.consultarDescricaoServidorJudiciarioServentia("t", "t", "0", "1");
//		// TODO: O campo Id_Cidade não existe na View viewUsuarioServentia
//		//ps.consultarServentiasUsuario("t");
//		UsuarioServentiaDt dados = ps.consultarId("1");
//		ps.alterar(dados);
//		ps.buscaUsuarioServentiaId("1", "1");
//		ps.consultarDescricao("t","0");
//		ps.consultarDescricaoUsuarioServentia("1");
//		ps.consultarUsuarioServentiaParte("86889346149");
//		ps.alterarStatusUsuarioServentia("1", 2);
//		ps.alterarStatusTodosUsuarioServentia("1", 1);
//	}
//	
//	private static void testeArquivoPs(FabricaConexao fabricaconexao) throws Exception{		
//		ArquivoPs ps = new ArquivoPs();
//		ps.setConexao(fabricaconexao);
//		
//		ArquivoDt dados = ps.consultarId("1538",true);
//		
//		ps.inserir(dados);
//		ps.alterar(dados);
//		ps.atualizaConteudoArquivo(dados);
//		ps.consultarConteudoArquivo("1538", true);
//		ps.consultarArquivoPendenciaId("1538");
//		ps.consultarArquivosMovimentacao("7548");
//		ps.consultarRelatorioProcesso("1", "1");		
//	}
//	
//	private static void testeEstatisticaPendenciaPs(FabricaConexao fabricaconexao) throws Exception{		
//		EstatisticaPendenciaPs ps = new EstatisticaPendenciaPs();
//		ps.setConexao(fabricaconexao);
//		EstatisticaPendenciaDt pendencia = new EstatisticaPendenciaDt();
//		pendencia.setDataFinal("19/10/2012 23:59:59");
//		pendencia.setDataInicial("19/10/1978 00:00:00");
//		pendencia.setId_Serventia("1");
//		pendencia.setServentia("teste");
//		UsuarioDt usu = new UsuarioDt();
//		usu.setId("1");
//		pendencia.setUsuario(usu);		
//		List detalhesPendenciaServentia = new ArrayList();
//		pendencia.setListaDetalhesPendenciaServentia(detalhesPendenciaServentia);
//		
//		ps.consultarDadosEstatisticaPendenciaServentia(pendencia );		
//		ps.consultarDadosEstatisticaPendenciaUsuario(pendencia);
//		ps.consultarDadosEstatisticaPendenciaUsuarioServentia(pendencia);
//	}
//	
//	private static void testeMovimentacaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		MovimentacaoPs ps = new MovimentacaoPs();
//		ps.setConexao(fabricaconexao);
//		
//		MovimentacaoDt dados = new MovimentacaoDt();
//		dados.setCodigoTemp("1");		
//		dados.setMovimentacaoTipoCodigo("2");
//		dados.setId_Processo("1");
//		dados.setId_UsuarioRealizador("1");
//		dados.setComplemento("comprimento");		
//		ps.inserir(dados);
//		dados.setId_MovimentacaoTipo("1");
//		ps.inserir(dados);		
//		ps.consultarMovimentacoesProcesso("1");		
//		ps.consultarMovimentacoesProcesso("1", "t", "0");
//		ps.alterarStatusMovimentacao("1", 2);
//		ps.consultarDadosMovimentacao("1");
//	}
//	
//	private static void testePendenciaResponsavelPs(FabricaConexao fabricaconexao) throws Exception{		
//		PendenciaResponsavelPs ps = new PendenciaResponsavelPs();
//		ps.setConexao(fabricaconexao);
//		
//		PendenciaDt pendenciaDt = new PendenciaDt();
//		pendenciaDt.setId("1");
//		UsuarioDt usuarioDt = new UsuarioDt();
//		usuarioDt.setId("1");
//		ps.eUsuarioResponsavel(pendenciaDt, usuarioDt);
//		usuarioDt.setId_UsuarioServentiaChefe("2");
//		ps.eUsuarioResponsavel(pendenciaDt, usuarioDt);
//		
//		ps.consultarResponsaveis("1");
//		ps.consultarResponsaveisDetalhado("1");
//		ps.consultarProcuradoresIntimacoes("1", "1");
//		ps.consultarResponsavelId("1");
//		ps.consultarResponsaveisCargoTipo("1", 1);
//		ps.consultarPendenciaPorIdAbertaComResponsavel(1, 1);
//		ps.alterarResponsavelPendencia("1", "1", "2");
//		ps.alterarResponsavelProcuradorPendencia("1", "1", "2");
//		ps.consultarIdPendenciaResponsavel("1", "1");
//	}
//	
//	private static void testeUsuarioServentiaEscalaPs(FabricaConexao fabricaconexao) throws Exception{		
//		UsuarioServentiaEscalaPs ps = new UsuarioServentiaEscalaPs();
//		ps.setConexao(fabricaconexao);
//		// TODO: A coluna NomeUsuario não existe na view viewUsuarioServentiaEscala
//		//ps.consultarUsuarioServentiaEscala("t", "0");		
//		ps.consultarUsuariosServentiaEscala("1");
//		UsuarioServentiaEscalaDt dados = new UsuarioServentiaEscalaDt();
//		// TODO: Campo inexistente na tabela
//		// dados.setUsuarioServentiaEscala("Teste Marcio"); 
//		dados.setId_UsuarioServentia("1");
//		dados.setId_Escala("1");
//		dados.setAtivo("true");
//		UsuarioServentiaEscalaStatusDt usu = new UsuarioServentiaEscalaStatusDt();
//		usu.setId("1");
//		usu.setIdUsuarioServentiaEscalaStatus("1");
//		dados.setUsuarioServentiaEscalaStatusDt(usu);
//		//ps.inserir(dados);
//		dados.setId("8");
//		//dados.setI
//		ps.alterar(dados);
//		ps.consultarId(dados.getId());
//		UsuarioServentiaDt usuarioServentiaDt = new UsuarioServentiaDt();
//		usuarioServentiaDt.setId("1");
//		EscalaDt escalaDt = new EscalaDt();	
//		escalaDt.setId("1");
//		ps.ativaDesativaUsuarioEscala(usuarioServentiaDt, escalaDt, 1);
//	}
//	
//	private static void testeLocomocaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		LocomocaoPs ps = new LocomocaoPs();
//		ps.setConexao(fabricaconexao);
//		//ps.inserir("156",  "1","15", LocomocaoDt.SIM_UTILIZADA.toString());
//		MandadoJudicialDt mandadoJudicialDt = (new MandadoJudicialNe()).consultarId("1");
//		if (mandadoJudicialDt == null){
//			mandadoJudicialDt = new MandadoJudicialDt();
//			mandadoJudicialDt.setId("1");
//		}
//		BairroDt bairroDt = new BairroDt();
//		bairroDt.setId("1");
//		mandadoJudicialDt.setBairroDt(bairroDt);
//		AreaDt areaDt = new AreaDt();
//		areaDt.setId("1");
//		mandadoJudicialDt.setAreaDt(areaDt);
//		ZonaDt zonaDt = new ZonaDt();
//		zonaDt.setId("1");
//		mandadoJudicialDt.setZonaDt(zonaDt);
//		ps.validaEnderecoLocomocaoMandadoJudicial(mandadoJudicialDt);
//		GuiaEmissaoDt guiaEmissaoDt = new GuiaEmissaoDt();
//		guiaEmissaoDt.setId("1");
//		//ps.consultaLocomocoesProcessoNaoUtilizadas(guiaEmissaoDt);
//		List listaGuiasEmissaoProcesso = new ArrayList();
//		listaGuiasEmissaoProcesso.add(guiaEmissaoDt);
//		guiaEmissaoDt = new GuiaEmissaoDt();
//		guiaEmissaoDt.setId("2");
//		listaGuiasEmissaoProcesso.add(guiaEmissaoDt);
//		ps.consultaLocomocoesProcessoNaoUtilizadas(listaGuiasEmissaoProcesso);
//		LocomocaoDt locomocaoDt = new LocomocaoDt();
//		locomocaoDt.setId("1");
//		ps.atualizaLocomocaoUtilizada(mandadoJudicialDt, locomocaoDt);
//		ps.consultarIdGuiaItem("1");		
//	}
//	
//	private static void testeEscalaPs(FabricaConexao fabricaconexao) throws Exception{		
//		EscalaPs ps = new EscalaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarEscalas("t", "0");
//		ps.consultarEscalasAtivas();
//		EscalaDt dados = new EscalaDt();
//		dados.setEscala("ESCALA TESTE MARCIO");
//		dados.setId_MandadoTipo("1");
//		dados.setId_Area("1");
//		dados.setId_Zona("1");
//		dados.setId_Regiao("1");
//		dados.setId_Bairro("1");
//		dados.setId_Serventia("7");
//		dados.setQuantidadeMandado("10");
//		dados.setAtivo("1");
//		ps.inserirEscala(dados);
//		ps.alterarEscala(dados);
//		MandadoTipoDt mandadoTipoDt = new MandadoTipoDt();
//		mandadoTipoDt.setMandadoTipoCodigo("1");
//		AreaDt areaDt = new AreaDt();
//		areaDt.setAreaCodigo("1");
//		RegiaoDt regiaoDt = new RegiaoDt();
//		regiaoDt.setRegiaoCodigo("1");
//		ps.consultarEscala(mandadoTipoDt, areaDt, regiaoDt);
//		ps.consultarEscala("1", "1", "1");		
//	}
//	
//	private static void testeGraficoProcessoPs(FabricaConexao fabricaconexao) throws Exception{		
//		GraficoProcessoPs ps = new GraficoProcessoPs();
//		ps.setConexao(fabricaconexao);
//		ps.graficoProcessoComarca("2011","01","2012","12","1");
//		ps.graficoProcessoComarca("2011","01","2011","12","1");
//		ps.graficoProcessoServentia("2011", "01", "2012", "12", "1");
//		ps.graficoProcessoServentia("2011", "01", "2011", "12", "1");
//		GraficoProcessoDt graficoProcessoDt = new GraficoProcessoDt(); 
//		List listaEstatisticaProdutividadeItem = new ArrayList();
//		EstatisticaProdutividadeItemDt estatisticaProdutividadeItemDt = new EstatisticaProdutividadeItemDt();
//		estatisticaProdutividadeItemDt.setId("1");
//		listaEstatisticaProdutividadeItem.add(estatisticaProdutividadeItemDt);
//		graficoProcessoDt.setListaEstatisticaProdutividadeItem(listaEstatisticaProdutividadeItem);		
//		ps.graficoProcessoItemProdutividade("2011", "01", "2012", "12", "1", "1", graficoProcessoDt);
//		ps.graficoProcessoItemProdutividade("2011", "01", "2011", "12", "1", "1", graficoProcessoDt);
//		
//		estatisticaProdutividadeItemDt = new EstatisticaProdutividadeItemDt();
//		estatisticaProdutividadeItemDt.setId("2");
//		listaEstatisticaProdutividadeItem.add(estatisticaProdutividadeItemDt);
//		graficoProcessoDt.setListaEstatisticaProdutividadeItem(listaEstatisticaProdutividadeItem);		
//		ps.graficoProcessoItemProdutividade("2011", "01", "2012", "12", "1", "1", graficoProcessoDt);
//		ps.graficoProcessoItemProdutividade("2011", "01", "2011", "12", "1", "1", graficoProcessoDt);
//	}
//	
//	private static void testeParametroCrimeExecucaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		ParametroCrimeExecucaoPs ps = new ParametroCrimeExecucaoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("C", "0");
//		ps.listarParametroCrimeExecucao("C", "1", "1", "0");
//		ps.consultarId("1");
//	}
//	
//	private static void testeMovimentacaoArquivoPs(FabricaConexao fabricaconexao) throws Exception{		
//		MovimentacaoArquivoPs ps = new MovimentacaoArquivoPs();
//		ps.setConexao(fabricaconexao);
//		//ps.consultarArquivosMovimentacao("1");
//		MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
//		movimentacaoDt.setId("1");
//		ps.consultarConteudoArquivosMovimentacao(movimentacaoDt, true);
//		ps.consultarConteudoArquivosMovimentacao(movimentacaoDt, false);
//		ps.consultarIdCompleto("1");
//		ps.consultarDadosMovimentacaoArquivo("1");
//		ps.alterarStatusMovimentacaoArquivo("1", MovimentacaoArquivoDt.NORMAL);
//	}
//	
//	private static void testeGrupoPermissaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		GrupoPermissaoPs ps = new GrupoPermissaoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarGruposPermissao("1");
//		ps.consultarPermissoesIdGrupo("1");
//		ps.getPermissao("1");
//		ps.consultarPermissoesIdGrupo("1");
//	}
//	
//	private static void testeServentiaTipoPs(FabricaConexao fabricaconexao) throws Exception{		
//		ServentiaTipoPs ps = new ServentiaTipoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarServentiaTipoCodigo("1");
//		ps.consultarServentiaTipo("C");
//		ps.consultarServentiaTipoId("1");
//		ps.consultarDescricao("C");
//		UsuarioDt usuarioDt = new UsuarioDt();
//		usuarioDt.setGrupoCodigo(String.valueOf(GrupoDt.ADMINISTRADORES));
//		ps.consultarServentiaTipo("C", usuarioDt);
//		usuarioDt.setGrupoCodigo(String.valueOf(GrupoDt.ANALISTAS_JUDICIARIOS_VARA));
//		ps.consultarServentiaTipo("C", usuarioDt);
//	}
//	
//	private static void testeZonaPs(FabricaConexao fabricaconexao) throws Exception{		
//		ZonaPs ps = new ZonaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("C", "0");
//		ps.consultarDescricao("C", "GOI", "0");
//		
//	}
//	
//	private static void testeCrimeExecucaoPs(FabricaConexao fabricaconexao) throws Exception{		
//		CrimeExecucaoPs ps = new CrimeExecucaoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarCodigo("1");
//		ps.consultarDescricao("V", "0");
//		ps.listarCrime("v", "1", "1", "0");
//	}
//	
//	private static void testeBairroPs(FabricaConexao fabricaconexao) throws Exception{
//		BairroPs ps = new BairroPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("C", "0");
//		ps.consultarDescricao("C", "GOI", "G","0");
//		ps.consultarBairro("CENTRO", "GOIANIA", "GO");
//	}
//	
//	private static void testeGrupoArquivoTipoPs(FabricaConexao fabricaconexao) throws Exception{
//		GrupoArquivoTipoPs ps = new GrupoArquivoTipoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarGrupoArquivoTipo("1", "t", "0");
//		ps.consultarDescricao("t", "0");
//		ps.consultarArquivoTipoGrupoGeral("1");
//	}
//	
//	private static void testeServentiaRelacionadaPs(FabricaConexao fabricaconexao) throws Exception{
//		ServentiaRelacionadaPs ps = new ServentiaRelacionadaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarServentiasRelacionadas("1");
//		ps.consultarServentiaRelacionada("1", ServentiaSubtipoDt.CAMARA_CIVEL);
//		ps.consultarServentiasPrincipaisRelacionadas("1");
//	}
//	
//	private static void testeGuiaCustaModeloPs(FabricaConexao fabricaconexao) throws Exception{
//		GuiaCustaModeloPs ps = new GuiaCustaModeloPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarCustaGuiaModeloGeral("109");
//		ps.consultarItensGuia("1", "1");
//	}
//	
//	private static void testeServentiaSubtipoAssuntoPs(FabricaConexao fabricaconexao) throws Exception{
//		ServentiaSubtipoAssuntoPs ps = new ServentiaSubtipoAssuntoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarAssuntosAreaDistribuicao("1", "t", "0");
//		ps.consultarAssuntosAreaDistribuicao("", "t", "0");
//		ps.consultarAssuntoServentiaSubtipoGeral("1");
//	}
//	
//	private static void testeLogPs(FabricaConexao fabricaconexao) throws Exception{
//		LogPs ps = new LogPs();
//		ps.setConexao(fabricaconexao);
//		
//		LogDt dt = new LogDt();
//		dt.setTabela("TABELA_TESTE");
//		dt.setTabela("1");
//		dt.setId_LogTipo("1");
//		dt.setId_Usuario("1");
//		dt.setIpComputador("192.168.12.36");
//		dt.setValorAtual("VALOR ANTIGO");
//		dt.setValorNovo("VALOR NOVO");
//		dt.setCodigoTemp("1");
//		
//		ps.inserir(dt);
//		
//		ps.consultarLog(dt.getId(), "T", "30/03/2011", "31/03/2011", "1", "0", "1");
//		ps.consultarLog(dt.getId(), null, null, null, null, "0", null);
//	}
//	
//	private static void testeRegimeExecucaoPs(FabricaConexao fabricaconexao) throws Exception{
//		RegimeExecucaoPs ps = new RegimeExecucaoPs();  
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("t","1", "0");
//		ps.consultarRegime("1");
//	}
//	
//	private static void testeAudienciaPublicadaPs(FabricaConexao fabricaconexao) throws Exception{
//		AudienciaPublicadaPs ps = new AudienciaPublicadaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarIdProcesso("039.2006.048.174-5");
//		ps.atualizeAudienciaPublicadaProcessoNaoEncontrado(new AudienciaPublicadaDt());
//		ps.excluirAudienciaPublicada(new AudienciaPublicadaDt());
//	}
//	
//	private static void testeZonaBairroPs(FabricaConexao fabricaconexao) throws Exception{
//		ZonaBairroPs ps = new ZonaBairroPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarZonaBairro("39",1);
//		ps.consultarBairroZonaGeral("2641", "1");
//		ZonaBairroDt dt = new ZonaBairroDt();
//		dt.setId_Zona("9999");
//		dt.setId_Bairro("9999");
//		ps.excluir(dt);		
//	}
//	
//	private static void testeLocalCumprimentoPenaPs(FabricaConexao fabricaconexao) throws Exception{
//		LocalCumprimentoPenaPs ps = new LocalCumprimentoPenaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarId("1");
//		ps.consultarDescricaoComEndereco("2053");
//	}
//	
//	private static void testePendenciaTipoRelacionadaPs(FabricaConexao fabricaconexao) throws Exception{
//		PendenciaTipoRelacionadaPs ps = new PendenciaTipoRelacionadaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultaPendenciaTipoRelacionadas();
//		ps.consultarPendenciaTipoPendenciaTipoGeral("1");
//	}
//	
//	private static void testeProcessoParteBeneficioPs(FabricaConexao fabricaconexao) throws Exception{
//		ProcessoParteBeneficioPs ps = new ProcessoParteBeneficioPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarBeneficiosProcessoParte("m", "86889346149", "0");
//		ps.consultarBeneficiosProcessoParte("m", "", "0");
//		ps.consultarId("1");
//	}
//	
//	private static void testeGuiaModeloPs(FabricaConexao fabricaconexao) throws Exception{
//		GuiaModeloPs ps = new GuiaModeloPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarGuiaModelo("3","205");
//		ps.consultarGuiaModelo("3");
//		ps.gerarCalculos("205", "5", GuiaTipoDt.INTERMEDIARIA_LOCOMOCAO);
//	}
//	
//	private static void testeGuiaInicial1GrauPs(FabricaConexao fabricaconexao) throws Exception{
//		GuiaInicial1GrauPs ps = new GuiaInicial1GrauPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarId("","");
//		ps.consultarId("73","");
//		//ps.consultarId("73","12450"); TODO: Verificar com o Fred
//		//ps.consultarId("","12450");		
//		ps.consultarId("73");
//		ps.vinculaGuiaProcesso("73", "2063");
//	}
//
//	private static void testeTarefaStatusPs(FabricaConexao fabricaconexao) throws Exception{
//		TarefaStatusPs ps = new TarefaStatusPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarIdStatusAguardandoVisto();
//		ps.consultarIdStatusAberto();
//		ps.consultarIdStatusEmAndamento();
//	}
//	
//	private static void testeRgOrgaoExpedidorPs(FabricaConexao fabricaconexao) throws Exception{
//		RgOrgaoExpedidorPs ps = new RgOrgaoExpedidorPs();	
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("S", "SSP","0");
//		ps.buscaOrgaoExpedidor("S","GO");
//	}
//		
//	private static void testeRelatorioEstProPs(FabricaConexao fabricaconexao) throws Exception{
//		RelatorioEstProPs ps = new RelatorioEstProPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricaoServentia("t", "1", "0");
//	}
//	
//	private static void testeArquivoTipoPs(FabricaConexao fabricaconexao) throws Exception{
//		ArquivoTipoPs ps = new ArquivoTipoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricaoArquivoTipo("1");
//		ps.consultarPublicoArquivoTipo("1");
//		ps.consultarPeloArquivoTipoCodigo("1");
//	}
//	
//	private static void testeUsuarioServentiaPermissaoPs(FabricaConexao fabricaconexao) throws Exception{
//		UsuarioServentiaPermissaoPs ps = new UsuarioServentiaPermissaoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarPermissoesIdUsuarioServentia("1");
//		ps.consultarUsuariosServentiaPermissao("1");
//	}
//	
//	private static void testePalavraPs(FabricaConexao fabricaconexao) throws Exception{
//		PalavraPs ps = new PalavraPs();
//		ps.setConexao(fabricaconexao);		
//		PalavraDt dt = new PalavraDt();
//	    dt.setPalavra("Testando123456...");
//	    ps.inserir(dt);
//	    dt.setPalavra("Testando1234567...");
//	    ps.alterar(dt);
//	    ps.consultarDescricao(dt.getPalavra());
//	}
//	
//	private static void testeEstadoPs(FabricaConexao fabricaconexao) throws Exception{
//		EstadoPs ps = new EstadoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("G","0");
//		ps.buscaEstado("GO");
//	}
//	
//	private static void testeEventoExecucaoStatusPs(FabricaConexao fabricaconexao) throws Exception{
//		EventoExecucaoStatusPs ps = new EventoExecucaoStatusPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("t", "0");
//	}
//	
//	private static void testeEventoExecucaoPs(FabricaConexao fabricaconexao) throws Exception{
//		EventoExecucaoPs ps = new EventoExecucaoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("t", "0");
//	}
//	
//	private static void testeUsuarioServentiaEscalaStatusPs(FabricaConexao fabricaconexao) throws Exception{
//		UsuarioServentiaEscalaStatusPs ps = new UsuarioServentiaEscalaStatusPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarUsuarioServentiaEscalaStatus("t","0");
//	}
//	
//	private static void testeCertidaoTipoProcessoTipoPs(FabricaConexao fabricaconexao) throws Exception{
//		CertidaoTipoProcessoTipoPs ps = new CertidaoTipoProcessoTipoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarProcessoTipoCertidaoTipoGeral("1");
//	}
//	
//	private static void testeMandadoJudicialStatusPs(FabricaConexao fabricaconexao) throws Exception{
//		MandadoJudicialStatusPs ps = new MandadoJudicialStatusPs();
//		ps.setConexao(fabricaconexao);
//		List listaMandadoStatusExcluir = new ArrayList();
//		ps.consultarListaStatus(listaMandadoStatusExcluir);
//		listaMandadoStatusExcluir.add("12");
//		listaMandadoStatusExcluir.add("10");
//		listaMandadoStatusExcluir.add("15");
//		
//		ps.consultarListaStatus(listaMandadoStatusExcluir);
//		
//	}
//	
//	private static void testeMandadoJudicialPs(FabricaConexao fabricaconexao) throws Exception{
//		MandadoJudicialPs ps = new MandadoJudicialPs();
//		ps.setConexao(fabricaconexao);
//		
//		MandadoJudicialDt dt = new MandadoJudicialDt();
//		dt.setId_MandadoTipo("1");
//		dt.setId_MandadoJudicialStatus("1");
//		dt.setId_Area("1");
//		dt.setId_Zona("1");
//		dt.setId_Regiao("1");
//		dt.setId_Bairro("1");
//		dt.setId_ProcessoParte("4388");
//		dt.setId_EnderecoParte("1");
//		dt.setId_Pendencia("12556");
//		dt.setId_Escala("1");
//		dt.setValor("10.526,35");
//		dt.setAssistencia("true");
//		dt.setDataDistribuicao("as");
//		dt.setDataLimite("19/10/1978");
//		UsuarioServentiaDt usu1 = new UsuarioServentiaDt();
//		usu1.setId("1");
//		dt.setUsuarioServentiaDt_1(usu1);		
//		UsuarioServentiaDt usu2 = new UsuarioServentiaDt();
//		usu2.setId("1");
//		dt.setUsuarioServentiaDt_2(usu2);
//		//ps.inserir(dt);
//		dt.setId("1");
//		dt.setLocomocoesFrutiferas("1");
//		dt.setLocomocoesInfrutiferas("1");
//		dt.setLocomocaoHoraMarcada("1");
//		
//		ps.consultarId(dt);
//		
//		ps.consultarPorIdPendencia("12556");
//		
//		ps.alterarStatusMandadoJudicial(dt, 1);
//		
//		ps.consultarMandadosPrazosExpirados();
//		
//		ps.consultarMandadosRetornadosPrazosExpirados();
//		
//		ps.alterarDataLimite(dt);
//	}
//	
//	private static void testeGuiaNumeroPs(FabricaConexao fabricaconexao) throws Exception{
//		GuiaNumeroPs ps = new GuiaNumeroPs();
//		ps.setConexao(fabricaconexao);
//		ps.reiniciarTabela();
//		GuiaNumeroDt dados = new GuiaNumeroDt();
//		dados.setCodigoTemp("20");
//		ps.inserir(dados);
//		dados.setCodigoTemp("");
//		ps.inserir(dados);	
//	}
//	
//	private static void testeComarcaPs(FabricaConexao fabricaconexao) throws Exception{
//		ComarcaPs ps = new ComarcaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarDescricao("G", "0");
//	}
//	
//	private static void testeProcessoAssuntoPs(FabricaConexao fabricaconexao) throws Exception{
//		ProcessoAssuntoPs ps = new ProcessoAssuntoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarAssuntosProcesso("2053");
//	}
//	
//	private static void testePenaExecucaoTipoPs(FabricaConexao fabricaconexao) throws Exception{
//		PenaExecucaoTipoPs ps = new PenaExecucaoTipoPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarIds("1,2,3,4", "0");
//	}
//	
//	private static void testePendenciaStatusPs(FabricaConexao fabricaconexao) throws Exception{
//		PendenciaStatusPs ps = new PendenciaStatusPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarPendenciaStatusCodigo(1);
//	}
//	
//	private static void testeAreaPs(FabricaConexao fabricaconexao) throws Exception{
//		AreaPs ps = new AreaPs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarAreaCodigo(1);
//	}
//	
//	private static void testeEventoLocalPs(FabricaConexao fabricaconexao) throws Exception{
//		EventoLocalPs ps = new EventoLocalPs();
//		ps.setConexao(fabricaconexao);
//		EventoLocalDt dados = ps.consultarId("653");
//		ps.alterar(dados);
//	}
//	
//	private static void testeEscolaridadePs(FabricaConexao fabricaconexao) throws Exception{
//		EscolaridadePs ps = new EscolaridadePs();
//		ps.setConexao(fabricaconexao);
//		ps.consultarCodigo("1");				
//	}
}
