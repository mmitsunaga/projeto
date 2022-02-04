package br.gov.go.tj.projudi.ne;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.gov.go.tj.projudi.ct.ImportarExecpenCt;
import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoDt;
import br.gov.go.tj.projudi.dt.CondenacaoExecucaoSituacaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoExecucaoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RegimeExecucaoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.ProcessoExecucaoPs;
import br.gov.go.tj.projudi.util.DistribuicaoProcesso;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Relatorios;


//---------------------------------------------------------
public class ImportarExecpenNe extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -123203463692870448L;
	protected  ProcessoExecucaoDt obDados;
	
	public ImportarExecpenNe() {
//		obPersistencia = new ProcessoExecucaoPs(); 	
		obLog = new LogNe(); 
		obDados = new ProcessoExecucaoDt(); 

	}
	
	/**
	 * importa todos os processos (execpen) da base do access para a base do mysql.
	 */
	public String importarProcessos(String caminhoArquivo, String conteudoArquivoProcesso, UsuarioNe usuarioSessao, String idComarca, String nomeComarca, String idAreaDistribuicao) throws Exception{
		String mensagem = "";
		
		mensagem = lerArquivoProcessoExecucaoCompleto(caminhoArquivo, conteudoArquivoProcesso, usuarioSessao, idComarca, nomeComarca, idAreaDistribuicao);
		
		return mensagem;
	}
	

	/**
	 * importa todos os eventos da base do access para a base do mysql (o processo deve estar cadastrado).
	 */
	public String importarEventos(String caminhoArquivo, String conteudoArquivoEvento, UsuarioNe usuarioSessao, String nomeComarca) throws Exception{
		String mensagem = "";
		
		mensagem = lerArquivoEvento(caminhoArquivo, conteudoArquivoEvento, usuarioSessao, nomeComarca);
	
		return mensagem;
	}
	
	
	/**
	 * L� os dados do arquivo e salva o Processo de Execu��o Penal completo.
	 * @param processoDt
	 * @param processoExecucaoDt
	 */
	public String lerArquivoProcessoExecucaoCompleto(String diretorioArquivoLog, String conteudoArquivoProcesso, UsuarioNe usuarioSessao, String idComarca, String nomeComarca, String idAreaDistribuicao) throws Exception, IOException, FileNotFoundException{
		String mensagem = "";
		String numeroExecpen = "";
		String depNovo = "";
		int registros = 0;
		
		File arquivoLog = null;
		FileWriter writer = null; 
		PrintWriter saida = null;
		
		FabricaConexao obFabricaConexao = null;

		try{
			HashMap map = new HashMap(); //par�metros que devem ser consultados.
			int processosImportados = 0;
		
			if (conteudoArquivoProcesso.length() == 0){
				mensagem = "O arquivo com o conte�do do PROCESSO n�o existe ou n�o pode ser lido.";
				
			}else {
				//---------- cria o arquivo de log com os dados que foram importados ----------------------
				arquivoLog = new File(diretorioArquivoLog + "log" + nomeComarca + "Processo.txt");
				writer = new FileWriter(arquivoLog,true); //o par�metro "true" indica que estou adicionando dados ao arquivo, e n�o sobrescrevendo. 
				saida = new PrintWriter(writer,true);  //especificando o segundo parametro como true, os dados ser�o enviados para o arquivo a toda chamada do m�todo println()
				saida.println("EXPORTA��O DOS DADOS DOS PROCESSOS DA COMARCA " + nomeComarca + " INICIADO EM: " + Funcoes.DataHora(new Date()));
				//-----------------------------------------------------------------------------------------
				
				//----------- consulta as vari�ves comuns para todos os execpens---------------------------
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				//consulta id_ProcessoTipo
				String idProcessoTipo = new ProcessoTipoNe().consultarIdProcessoTipo(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA), obFabricaConexao);
				if (idProcessoTipo == null || idProcessoTipo.length() == 0) {
					mensagem = "N�o foi poss�vel localizar o Id do ProcessoTipo: C�lculo de Liquida��o de Pena.";
					return mensagem;
				}
				//Consulta os dados da comarca que est� exportando os dados do execpen para o Projudi.
				ComarcaDt comarcaDt = new ComarcaNe().consultarId(idComarca, obFabricaConexao);
				String idBairro = new BairroNe().consultarId("centro", comarcaDt.getComarca(), "GO", obFabricaConexao);
				if (idBairro == null || idBairro.length() == 0){
					mensagem = "N�o foi poss�vel localizar o Id do Bairro: Centro-"+ comarcaDt.getComarca() +"-GO.";
					return mensagem;
				}
				//------------------------------------------------------------------------------------------
				
				String execpen = "-1";
				String dep = "-1";
				boolean novoDEP = true;
				String idProcessoCadastrado = "";
				String idProcessoExecucaoCadastrado = "";
				
				ProcessoDt processoDt = null;
				ProcessoExecucaoDt processoExecucaoDt = null;
				ProcessoParteDt parteDt = null;
				
				String[] linhas = conteudoArquivoProcesso.split("\\n");
				
				ImportarExecpenCt.valorAtualBarraProgresso = 0;
				ImportarExecpenCt.totalBarraProgresso = linhas.length;
				ImportarExecpenCt.execucaoBarraProgresso = "processo";
				
				for (int z=1; z<linhas.length; z++){
					processoExecucaoDt = new ProcessoExecucaoDt();
					parteDt = new ProcessoParteDt();
					processoDt = new ProcessoDt();
					
					registros++;
					ImportarExecpenCt.valorAtualBarraProgresso++;
					String[] campos = linhas[z].split("#");
					int i = 0;
					
					//---------------- IN�CIO LEITURA DOS DADOS DA LINHA ----------------------------
					processoDt.setTcoNumero(campos[i]); i++; 									//campo 1: n�mero do execpen
					numeroExecpen = processoDt.getTcoNumero();
					processoExecucaoDt.setProcessoFisicoNumero(campos[i]); i++;					//campo 2: n�mero do processo de execu��o penal
					map.put("idRegimeExecpen-AreaDistribuicao", campos[i]);	i++;				//campo 3: regime atual do sentenciado. Ser� utilizado para definir a �rea de distribui��o (serventia) do processo no Projudi.
					parteDt.setNome(campos[i].toUpperCase());	i++;							//campo 4: nome do sentenciado
					parteDt.setCpf(campos[i].replaceAll("[/\\.\\-]", "")); i++;					//campo 5: n�mero do cpf
					String rg = campos[i].replaceAll("[/\\.\\-]", ""); i++;						//campo 6: n�mero da identidade
					parteDt.setRg(Funcoes.retirarVogalConsoante(rg, "")); 												
					parteDt.setDataNascimento(campos[i]); i++;									//campo 7: data de nascimento
					map.put("descricaoCidadeNaturalidade", campos[i]); i++;						//campo 8: cidade da naturalidade
					map.put("descricaoUfNaturalidade", campos[i]); i++;							//campo 9: uf da naturalidade
					parteDt.setNomePai(campos[i].toUpperCase()); i++;							//campo 10: nome do pai
					parteDt.setNomeMae(campos[i].toUpperCase()); i++;							//campo 11: nome da m�e
					parteDt.setSexo(campos[i]); i++;											//campo 12: sexo
					map.put("descricaoProfissao", campos[i]); i++;								//campo 13: descri��o da profiss�o
					map.put("codEstadoCivil", campos[i]); i++;									//campo 14: c�digo do estado civil
					parteDt.getEnderecoParte().setLogradouro(campos[i].toUpperCase()); i++;		//campo 15: descri��o do logradouro, incluindo o bairro, cidade e estado.
					parteDt.getEnderecoParte().setCep(campos[i]); i++;							//campo 16: CEP
//					processoExecucaoDt.setId_ProcessoAcaoPenal(campos[i]); i++;					//campo 17: n�mero do DEP (do execpen)
					depNovo = campos[i]; i++;													//campo 17: n�mero do DEP (do execpen)
					String numeroAcaoPenal = campos[i].replaceAll("[/\\.\\-;,]", ""); i++;		//campo 18: n�mero do processo de a��o penal
					numeroAcaoPenal = Funcoes.retirarVogalConsoante(numeroAcaoPenal, "");
					if (numeroAcaoPenal.length() > 13) numeroAcaoPenal = numeroAcaoPenal.substring(0,12);
					processoExecucaoDt.setNumeroAcaoPenal(numeroAcaoPenal); 
					processoExecucaoDt.setDataDistribuicao(campos[i]); i++;						//campo 19: data da distribui��o da a��o penal
					processoExecucaoDt.setVaraOrigem(campos[i]); i++;							//campo 20: descri��o do cart�rio de origem da a��o penal
					processoExecucaoDt.setId_CidadeOrigem(campos[i]); i++;						//campo 21: id da comarca de origem da a��o penal (id do projudi)
					processoExecucaoDt.setDataAcordao(campos[i]); i++;							//campo 22: data do ac�rd�o.
					processoExecucaoDt.setDataFato(campos[i]); i++;								//campo 23: data do fato.
					processoExecucaoDt.setDataDenuncia(campos[i]); i++;							//campo 24: data do recebimento da den�ncia
					processoExecucaoDt.setDataSentenca(campos[i]); i++;							//campo 25: data da senten�a
					processoExecucaoDt.setDataTransitoJulgado(campos[i]); i++;					//campo 26: data do tr�nsito em julgado
					processoExecucaoDt.setDataPronuncia(campos[i]); i++;						//campo 27: data da pron�ncia.
					map.put("codRegimeProj", campos[i]); i++;									//campo 28: id do regime (projudi) ..ver se vai ser regime ou modalidade
					processoExecucaoDt.setId_LocalCumprimentoPena(campos[i]); i++;				//campo 29: id do local de cumprimento de pena do projudi
					processoExecucaoDt.setTempoCondenacaoAno(campos[i]); i++;					//campo 30: tempo da pena - anos
					processoExecucaoDt.setTempoCondenacaoMes(campos[i]); i++;					//campo 31: tempo da pena - meses
					processoExecucaoDt.setTempoCondenacaoDia(campos[i]); i++;					//campo 32: tempo da pena - dias
					processoExecucaoDt.setDataAdmonitoria(campos[i]); i++;						//campo 33: data da audi�ncia admonit�ria
					processoExecucaoDt.setTempoSursisAno(campos[i]); i++;						//campo 34: tempo do sursis - anos
					processoExecucaoDt.setTempoSursisMes(campos[i]); i++;						//campo 35: tempo do sursis - meses
					processoExecucaoDt.setTempoSursisDia(campos[i]); i++;						//campo 36: tempo do sursis - dias
					processoExecucaoDt.setDataInicioCumprimentoPena(campos[i]); i++;			//campo 37: data de in�cio de cumprimento da condena��o
					map.put("reincidente", campos[i]); i++;										//campo 38: reincid�ncia: 0 (n�o) e -1 (sim)
					String idCrime = campos[i]; i++;
					idCrime = idCrime.replace("\r", "");
					processoExecucaoDt.setId_CrimeExecucao(idCrime);	 						//campo 39: id da infra��o da tabela do projudi
					//---------------- FIM LEITURA DOS DADOS DA LINHA ----------------------------

					if (dep.equals(depNovo)) 
						novoDEP = false; //incluir apenas a infra��o
					else novoDEP = true;
					
					if (execpen.equals(processoDt.getTcoNumero()))
						processoExecucaoDt.setProcessoNovo(false);
					else processoExecucaoDt.setProcessoNovo(true);

					//seta dados do processo execu��o, processo e processo parte nos obejtos
					if (novoDEP || processoExecucaoDt.isProcessoNovo()){
						mensagem = setDadosProcesso(processoExecucaoDt, processoDt, parteDt, map, usuarioSessao, comarcaDt, idProcessoTipo, depNovo, idBairro, idAreaDistribuicao);
						if (mensagem.length() > 0){
							saida.close();  
							writer.close(); 
							return mensagem;
						}
					} 
					 
					//seta dados do processo execu��o e da condena��o 
					setDadosProcessoExecucao(processoExecucaoDt, map, usuarioSessao);
					
					//grava no arquivo de log
					saida.println("Registro n� " + registros + ":");
					
					//salva dados
					if (novoDEP || processoExecucaoDt.isProcessoNovo()){
						if (novoDEP && !processoExecucaoDt.isProcessoNovo()) {//inlcui apenas nova guia de recolhimento (num processo j� cadastrado)
							processoExecucaoDt.setId_ProcessoExecucaoPenal(idProcessoCadastrado);
							ProcessoDt p = new ProcessoNe().consultarIdSimples(idProcessoCadastrado);
							if (p == null) {
								saida.close();  
								writer.close(); 
								mensagem = "N�o foi poss�vel localizar o Processo com id: " + idProcessoCadastrado;
								return mensagem;
							} else {
								//consulta dados do processo j� cadastro.
								processoExecucaoDt.setProcessoDt(p);
								processoExecucaoDt.getProcessoDt().setId_UsuarioLog(usuarioSessao.getId_Usuario());
								processoExecucaoDt.getProcessoDt().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
							}
						}
						
						//verifica se o processo j� est� cadastrado
						ProcessoDt processoConsulta = new ProcessoNe().consultarProcesso(processoDt.getProcessoNumeroSimples(), processoDt.getDigitoVerificador(), processoDt.getAno(), processoDt.getForumCodigo(), String.valueOf(ProcessoStatusDt.CALCULO));
						if (processoConsulta != null){
							processoExecucaoDt.setProcessoDt(processoConsulta);
							processoExecucaoDt.getProcessoDt().setId_UsuarioLog(usuarioSessao.getId_Usuario());
							processoExecucaoDt.getProcessoDt().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
						}
						
						if (processoExecucaoDt.getProcessoDt().getId_AreaDistribuicao().length() == 0)
							processoExecucaoDt.getProcessoDt().setId_AreaDistribuicao(idAreaDistribuicao);
						
						if (processoExecucaoDt.getProcessoDt().getId_ProcessoTipo().length() == 0) {
							processoExecucaoDt.getProcessoDt().setId_ProcessoTipo(idProcessoTipo);
						}
						
						//salva dados do processo, parte e condena��o
						cadastrarProcessoExecucao(processoExecucaoDt, usuarioSessao.getUsuarioDt(), true);
						idProcessoCadastrado = processoExecucaoDt.getId_ProcessoExecucaoPenal();
						idProcessoExecucaoCadastrado = processoExecucaoDt.getId();
						
						//grava no arquivo de log
						saida.println("Execpen n�: " + numeroExecpen + ".");
						if (processoConsulta != null) saida.println("PROCESSO J� CADASTRADO!");
						saida.println("Dados Processo: " + processoExecucaoDt.getProcessoDt().getPropriedades() + ".");
						saida.println("Dados ProcessoExecucao: " + processoExecucaoDt.getPropriedades() + ".");
						saida.println("Dados ProcessoParte: " + parteDt.getPropriedades() + ".");
						
					} else {//salva dados da condena��o
						if (!novoDEP && !processoExecucaoDt.isProcessoNovo()){//inclui apenas a infra��o (condena��o)
							CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt)processoExecucaoDt.getListaCondenacoes().get(0);
							condenacao.setId_ProcessoExecucao(idProcessoExecucaoCadastrado);
							condenacao.setTempoPena("0");
							condenacao.setTempoPenaEmDias("0");
							new CondenacaoExecucaoNe().salvar(condenacao);	
						}
					}
					
					//grava no arquivo de log
					saida.println("Dados Condenacao: " + ((CondenacaoExecucaoDt)processoExecucaoDt.getListaCondenacoes().get(0)).getPropriedades() + ".");
					
					if (processoExecucaoDt.isProcessoNovo()) processosImportados++;
					execpen = processoDt.getTcoNumero();
					dep = depNovo;
					processoExecucaoDt = null;
					parteDt = null;
					processoDt = null;
				}
				
				ImportarExecpenCt.valorAtualBarraProgresso = ImportarExecpenCt.totalBarraProgresso;
				
				mensagem = "Leitura do arquivo conclu�da com sucesso! \nN�mero de linhas do arquivo lido: " + registros + ". \nProcessos importados: " + processosImportados + ".\n";
				saida.println(mensagem);
				saida.println("Fim da importa��o: " + Funcoes.DataHora(new Date()));
				saida.close();  
				writer.close(); 
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return mensagem;
	}
	
	/**
	 * Consulta os ids necess�rios e adiciona nos objetos correspondentes.
	 * @param processoExecucaoDt: objeto ProcessoExecucao
	 * @param processoDt: objeto ProcessoDt
	 * @param parteDt: objeto ProcessoParteDt
	 * @param map: HashMap contendo os par�metros para consulta.
	 * @throws Exception
	 */
	private String setDadosProcesso(ProcessoExecucaoDt processoExecucaoDt, ProcessoDt processoDt, ProcessoParteDt parteDt, HashMap map, UsuarioNe usuarioSessao, ComarcaDt comarcaDt, String idProcessoTipo, String numeroDep, String idBairro, String idAreaDistribuicao) throws Exception{
		String mensagem = "";
		FabricaConexao obFabricaConexao = null;
//		String idAreaDistribuicao = ""; //campo obrigat�rio
		String idNaturalidade = "";
		String idProfissao = "";
		String idEstadoCivil = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			if (processoExecucaoDt.isProcessoNovo()){
				idNaturalidade = consultarIdCidade(map.get("descricaoCidadeNaturalidade").toString(), map.get("descricaoUfNaturalidade").toString(), obFabricaConexao);
				idProfissao = consultarIdProfissao(map.get("descricaoProfissao").toString(), obFabricaConexao);
				idEstadoCivil = consultarIdEstadoCivil(map.get("codEstadoCivil").toString(), obFabricaConexao);
				
				//set dados fixos do processoParteDt.
				parteDt.setId_Naturalidade(idNaturalidade);
				parteDt.setId_Profissao(idProfissao);
				parteDt.setId_EstadoCivil(idEstadoCivil);
				parteDt.getEnderecoParte().setId_Bairro(idBairro);
				parteDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));
				parteDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		        parteDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
			
		        processoDt.addListaPolosPassivos(parteDt);
			}
			
			//consulta a �ra de distrubui��o para a comarca de Goi�nia, pois a �rea de distribui��o vai depender do regime atual do sentenciado.
			if (comarcaDt.getComarcaCodigo().equals(String.valueOf(ComarcaDt.GOIANIA)))
				idAreaDistribuicao = consultarIdAreaDistribuicaoGoi�nia(map.get("idRegimeExecpen-AreaDistribuicao").toString(), obFabricaConexao, comarcaDt);
			
			if (idAreaDistribuicao.length() == 0) {
				mensagem = "N�o foi poss�vel encontrar a �rea de Distribui��o do Execpen n�: " + processoDt.getTcoNumero();
			}
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		processoExecucaoDt.setComarca(comarcaDt.getComarca());
		processoExecucaoDt.setId_Comarca(comarcaDt.getId());
		processoExecucaoDt.setComarcaCodigo(comarcaDt.getComarcaCodigo());
		processoExecucaoDt.setPodeCadastrarProcessoFisico(true);
		
		processoDt.setId_AreaDistribuicao(idAreaDistribuicao);
		processoDt.setProcessoTipoCodigo(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA)); //seta o tipo do processo
		processoDt.setId_ProcessoTipo(idProcessoTipo);
		processoDt.setSegredoJustica("false");
        processoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
        processoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		processoExecucaoDt.setProcessoDt(processoDt);
		//est� fazendo no cadastro do processo
		new ProcessoExecucaoNe().atualizarNumeroProcessoFisico(processoExecucaoDt);
//		processoDt.setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.EXECUCAO)); //seta a fase do processo
//		processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO)); //seta status do processo como ativo
//		processoDt.setAreaCodigo(String.valueOf(AreaDt.CRIMINAL)); //seta �rea do processo		
		
        //adicionaAssunto --id do Assunto: "Execu��o da Pena", c�digo: 7942
        ProcessoAssuntoDt processoAssuntoDt = new ProcessoAssuntoDt();
        processoAssuntoDt.setId_Assunto("1020");
        processoExecucaoDt.getProcessoDt().addListaAssuntos(processoAssuntoDt);
        
        return mensagem;
	}
	
	private void setDadosProcessoExecucao(ProcessoExecucaoDt processoExecucaoDt, HashMap map, UsuarioNe usuarioSessao){
		//set dados ProcessoExecucao
		processoExecucaoDt.setMedidaSeguranca(false);
        processoExecucaoDt.setAtivo("true");
		processoExecucaoDt.setCadastroTipo(3);
		processoExecucaoDt.setGuiaRecolhimento("D");
		processoExecucaoDt.setTempoTotalSursisDias(Funcoes.converterParaDias(processoExecucaoDt.getTempoSursisAno(), processoExecucaoDt.getTempoSursisMes(), processoExecucaoDt.getTempoSursisDia()));
		
		//---dados para a inclus�o da condena��o-------
        processoExecucaoDt.setTempoPenaEmDias(Funcoes.converterParaDias(processoExecucaoDt.getTempoCondenacaoAno(), processoExecucaoDt.getTempoCondenacaoMes(), processoExecucaoDt.getTempoCondenacaoDia()));
	    processoExecucaoDt.setIdCondenacaoExecucaoSituacao(String.valueOf(CondenacaoExecucaoSituacaoDt.NAO_APLICA));
	    //reincid�ncia: 0 (n�o) e -1 (sim)
        if (map.get("reincidente").toString().equals("0")) processoExecucaoDt.setReincidente("false");
        else processoExecucaoDt.setReincidente("true");
        //---------------------------------------------
        
        //adicionaCondenacao
        if (processoExecucaoDt.getId_CrimeExecucao().length() > 0) {
            CondenacaoExecucaoDt condenacaoExecucaoDt = new CondenacaoExecucaoDt();
            condenacaoExecucaoDt.setId_CrimeExecucao(processoExecucaoDt.getId_CrimeExecucao());
            condenacaoExecucaoDt.setDataFato(processoExecucaoDt.getDataFato());
			condenacaoExecucaoDt.setReincidente(processoExecucaoDt.getReincidente());
            condenacaoExecucaoDt.setTempoPenaEmDias(processoExecucaoDt.getTempoPenaEmDias());
            condenacaoExecucaoDt.setTempoPena(processoExecucaoDt.getTempoPenaEmDias());
            condenacaoExecucaoDt.setId_CondenacaoExecucaoSituacao(processoExecucaoDt.getIdCondenacaoExecucaoSituacao());
            condenacaoExecucaoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
            condenacaoExecucaoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
            
            processoExecucaoDt.addListaCondenacoes(condenacaoExecucaoDt);
        }
        
        //verifica se vai adicionar o regime ou modalidade no processo
        setRegimeModalidade(map.get("codRegimeProj").toString(), processoExecucaoDt);
        //adicionaModalidade
    	if (processoExecucaoDt.getId_Modalidade().length() > 0){
			ProcessoEventoExecucaoDt pee = new ProcessoEventoExecucaoDt();
			pee.getEventoRegimeDt().setId_RegimeExecucao(processoExecucaoDt.getId_Modalidade());
			new ProcessoExecucaoNe().vincularIdEvento_Quantidade_Modalidade(pee, processoExecucaoDt.getListaCondenacoes());
			processoExecucaoDt.addListaModalidade(pee);
		}	
	    
        processoExecucaoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
        processoExecucaoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
	}
	
	/**
	 * Consulta a �rea de distribui��o � partir do Regime Atual da Execu��o Penal - PARA A COMARCA DE GOI�NIA
	 * @param idRegimeExecucao: regime atual da execu��o penal
	 * @param processoExecucaoDt: objeto, para setar os dados consultados
	 * @param processoDt: objeto, para setar os dados consultados
	 * @throws Exception
	 */
	private String consultarIdAreaDistribuicaoGoi�nia(String idRegimeExecucao, FabricaConexao obFabricaConexao, ComarcaDt comarcaDt) throws Exception{
		String codigoAreaDistribuicao = "";
		String id = "";
		
		//Os processos com regime �fechado�, ser�o distribu�dos para a 4� Vara Criminal � VEP 1); 
		//os processos com regimes �semi-aberto�, �aberto�, �aberto domiciliar� ou �livramento condicional� ser�o distribu�dos para o 8� Juizado Criminal � VEP 2, 
		//e por fim, os processos com regimes �presta��o de servi�o � comunidade�, �limita��o de fim de semana� ou �interdi��o tempor�ria de direitos� ser�o distribu�dos para a 6� Vara Criminal;
		if (idRegimeExecucao.equals(String.valueOf(RegimeExecucaoDt.FECHADO))){
			codigoAreaDistribuicao = String.valueOf(AreaDistribuicaoDt.VARA_CRIMINAL_4VARA_CODIGO);
			
		} else if(idRegimeExecucao.equals(String.valueOf(RegimeExecucaoDt.ABERTO))
				|| idRegimeExecucao.equals(String.valueOf(RegimeExecucaoDt.ABERTO_DOMICILIAR))
				|| idRegimeExecucao.equals(String.valueOf(RegimeExecucaoDt.SEMI_ABERTO))
				|| idRegimeExecucao.equals(String.valueOf(RegimeExecucaoDt.LIVRAMENTO_CONDICIONAL))){
			codigoAreaDistribuicao = String.valueOf(AreaDistribuicaoDt.VARA_CRIMINAL_8JUIZADO_CODIGO);
			
		} else codigoAreaDistribuicao = String.valueOf(AreaDistribuicaoDt.VARA_CRIMINAL_6VARA_CODIGO);
			id = new AreaDistribuicaoNe().consultarIdAreaDistribuicao(codigoAreaDistribuicao, obFabricaConexao);
			
		
		return id;
	}
	
	/**
	 * Consulta o Id da cidade � partir da descri��o da cidade e uf
	 */
	private String consultarIdCidade(String cidade, String uf, FabricaConexao obFabricaConexao) throws Exception{
		String id = "";
		
		if (cidade.length() > 0 && uf.length() == 2){
			id = new CidadeNe().consultarIdCidade(cidade, uf, obFabricaConexao);
		}	
		
		return id;
	}
	
	/**
	 * Consulta o Id da profiss�o � partir da descri��o da profiss�o
	 * @throws Exception
	 */
	private String consultarIdProfissao(String descricao, FabricaConexao obFabricaConexao) throws Exception{
		String id = "";
		
		if (descricao.length() > 0){
			id = new ProfissaoNe().consultarIdProfissao(descricao, obFabricaConexao);
		}	
		
		return id;
	}
	
	/**
	 * A descri��o do estado civil foi definida � partir do idEstadoCivilExecpen. E com esta descri��o, consulta o id do estado civil.
	 * @param idEstadoCivilExecpen
	 * @param obFabricaConexao
	 * @throws Exception
	 */
	private String consultarIdEstadoCivil(String idEstadoCivilExecpen, FabricaConexao obFabricaConexao) throws Exception{
		String id = "";
		String stConsulta = "";

		//c�digo do estado civil no execpen: 1 (solteiro), 2 (casado), 3(desquitado=separado), 4(divorciado) e 5 (vi�vo). 
		if (idEstadoCivilExecpen.equals("1")) stConsulta = "solteiro";
		else if (idEstadoCivilExecpen.equals("2")) stConsulta = "casado";
		else if (idEstadoCivilExecpen.equals("3")) stConsulta = "separado";
		else if (idEstadoCivilExecpen.equals("4")) stConsulta = "divorciado";
		else if (idEstadoCivilExecpen.equals("5")) stConsulta = "viuvo";

		if (stConsulta.length() > 0){
			id = new EstadoCivilNe().consultarId(stConsulta, obFabricaConexao);
		}	
		
		return id;
	}

	/**
	 * Verifica se o id passado ser� da modalidade ou regime de pena.
	 * @param idRegime
	 * @param processoExecucaoDt
	 * @throws Exception
	 */
	private void setRegimeModalidade(String idRegime, ProcessoExecucaoDt processoExecucaoDt){
		
		//Os processos com regime �fechado�, ser�o distribu�dos para a 4� Vara Criminal � VEP 1); 
		//os processos com regimes �semi-aberto�, �aberto�, �aberto domiciliar� ou �livramento condicional� ser�o distribu�dos para o 8� Juizado Criminal � VEP 2, 
		//e por fim, os processos com regimes �presta��o de servi�o � comunidade�, �limita��o de fim de semana� ou �interdi��o tempor�ria de direitos� ser�o distribu�dos para a 6� Vara Criminal;
		if (idRegime.equals(String.valueOf(RegimeExecucaoDt.FECHADO))
				|| idRegime.equals(String.valueOf(RegimeExecucaoDt.ABERTO))
				|| idRegime.equals(String.valueOf(RegimeExecucaoDt.ABERTO_DOMICILIAR))
				|| idRegime.equals(String.valueOf(RegimeExecucaoDt.SEMI_ABERTO))
				|| idRegime.equals(String.valueOf(RegimeExecucaoDt.LIVRAMENTO_CONDICIONAL))){
			processoExecucaoDt.setId_RegimeExecucao(idRegime);
		} else if (idRegime.equals(String.valueOf(RegimeExecucaoDt.INTERDICAO_TEMPORARIA_DIREITOS))
				|| idRegime.equals(String.valueOf(RegimeExecucaoDt.LIMITACAO_FIM_SEMANA))
				|| idRegime.equals(String.valueOf(RegimeExecucaoDt.PERDA_BENS_VALORES))
				|| idRegime.equals(String.valueOf(RegimeExecucaoDt.PRESTACAO_PECUNIARIA))
				|| idRegime.equals(String.valueOf(RegimeExecucaoDt.CESTA_BASICA))
				|| idRegime.equals(String.valueOf(RegimeExecucaoDt.PRESTACAO_SERVICO_COMUNIDADE))) {
			processoExecucaoDt.setId_Modalidade(idRegime);
			processoExecucaoDt.setId_RegimeExecucao(String.valueOf(RegimeExecucaoDt.ABERTO));
		}
	}

	/**
	 * L� os dados do arquivo e salva o Processo de Execu��o Penal completo.
	 * @param processoDt
	 * @param processoExecucaoDt
	 */
	public String lerArquivoEvento(String diretorioArquivoLog, String conteudoArquivoEvento, UsuarioNe usuarioSessao, String nomeComarca) throws Exception, IOException, FileNotFoundException{
		String mensagem = "";
		int registros = 0;
		String numeroExecpenAtual = "-1";
		String numeroProcessoAtual = "0";
		String numeroHistoricoExecpen = ""; //utilizado para controle do registro que est� sendo importado
		
		File arquivoLog = null;
		FileWriter writer = null; 
		PrintWriter saida = null;		

		if (conteudoArquivoEvento.length() == 0){
			mensagem = "O arquivo de EVENTO n�o existe ou n�o pode ser lido.";
		
		}else {
			//---------- cria o arquivo de log com os dados que foram importados ----------------------
			arquivoLog = new File(diretorioArquivoLog + "log" + nomeComarca + "Evento.txt");
			writer = new FileWriter(arquivoLog,true); //o par�metro "true" indica que estou adicionando dados ao arquivo, e n�o sobrescrevendo. 
			saida = new PrintWriter(writer,true);  //especificando o segundo parametro como true, os dados ser�o enviados para o arquivo a toda chamada do m�todo println()
			saida.println("EXPORTA��O DOS DADOS DA COMARCA " + nomeComarca + " INICIADO EM: " + Funcoes.DataHora(new Date()));
			//-----------------------------------------------------------------------------------------
			
			String idLivramentoCondicional = "";
			String considerarTempoLC = "";
			String numeroProcessoAntigo = "";
			String idMovimentacao = "";
			
			String[] linhas = conteudoArquivoEvento.split("\\n");
			
			ImportarExecpenCt.valorAtualBarraProgresso = 0;
			ImportarExecpenCt.totalBarraProgresso = linhas.length;
			ImportarExecpenCt.execucaoBarraProgresso = "evento";
			
			for (int z=1; z<linhas.length; z++){
				registros++;
				ImportarExecpenCt.valorAtualBarraProgresso++;
				String[] campos = linhas[z].split("#");
				int i = 0;

				//--------------- IN�CIO DA LEITURA DOS DADOS DO ARQUIVO -------------------------------------------------
				ProcessoEventoExecucaoDt processoEventoExecucaoDt = new ProcessoEventoExecucaoDt();
				numeroProcessoAtual = campos[i]; i++;
				numeroExecpenAtual = campos[i]; i++;												//campo 1: n�mero do execpen
				numeroHistoricoExecpen = campos[i]; i++;											//campo 2: n�mero do hist�rico
				processoEventoExecucaoDt.setId_EventoExecucao( campos[i]); i++; 					//campo 3: c�digo do evento
				processoEventoExecucaoDt.setDataInicio( campos[i]);	i++;							//campo 4: data in�cio do evento
				processoEventoExecucaoDt.setDataFim(campos[i]);	i++;								//campo 5: data fim do evento - utilizada apenas no evento Pris�o Provis�ria. A data fim da pris�o provis�ria � a data in�cio do evento Interrup��o da pris�o Provis�ria
				processoEventoExecucaoDt.setQuantidade(campos[i].replaceAll("-", "")); i++;			//campo 6: quantidade
				processoEventoExecucaoDt.setObservacao(campos[i]); i++;								//campo 7: observa��o do evento.
				if (processoEventoExecucaoDt.getObservacao().length() > 200)
					processoEventoExecucaoDt.setObservacao(processoEventoExecucaoDt.getObservacao().substring(0,200));
				String regime = campos[i]; i++;														//campo 8: c�digo do regime
				regime = regime.replace("\r", "");
				//--------------- FIM DA LEITURA DOS DADOS DO ARQUIVO -----------------------------------------------------

				//----- in�cio set dados dos evento
				//consulta o idMovimenta��o para cada novo n�mero de execpen.
				if (!numeroProcessoAntigo.equals(numeroProcessoAtual)){
					String procNum[] = numeroProcessoAtual.split("\\.");
					String numero = procNum[0];
					String digito = procNum[1];
					String ano = procNum[2];
					idMovimentacao = new MovimentacaoNe().consultarIdMovimentacao(String.valueOf(MovimentacaoTipoDt.GUIA_RECOLHIMENTO_INSERIDA), numero, digito, ano, numeroExecpenAtual);//consultar id_Movimentacao
					if (idMovimentacao.length() == 0){
						saida.close();  
						writer.close();
						mensagem = "N�o foi localizada a movimenta��o de \"Guia de Recolhimento Inserida\" referente ao processo n� " + numeroProcessoAtual + " com execpen n� " + numeroExecpenAtual + ", Hist�rico n� " + numeroHistoricoExecpen;
						return mensagem;
					}
				}
					
				processoEventoExecucaoDt.setId_Movimentacao(idMovimentacao);
				processoEventoExecucaoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
				processoEventoExecucaoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
				processoEventoExecucaoDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
				
				processoEventoExecucaoDt.setEventoExecucaoDt(new ProcessoEventoExecucaoNe().consultarEventoExecucao(processoEventoExecucaoDt.getId_EventoExecucao()));
				processoEventoExecucaoDt.getEventoExecucaoDt().setId_UsuarioLog(usuarioSessao.getId_Usuario());
				processoEventoExecucaoDt.getEventoExecucaoDt().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				if (!regime.equals("0")){//os eventos que n�o possuem regime, est�o com o c�digo do regime = "0". 
					processoEventoExecucaoDt.getEventoRegimeDt().setId_RegimeExecucao(regime);
					processoEventoExecucaoDt.getEventoRegimeDt().setId_UsuarioLog(usuarioSessao.getId_Usuario());
					processoEventoExecucaoDt.getEventoRegimeDt().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
				}
				
				if (processoEventoExecucaoDt.getEventoExecucaoDt().getId().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL))){
					if (processoEventoExecucaoDt.getQuantidade().length() > 0 && Funcoes.StringToInt(processoEventoExecucaoDt.getQuantidade()) > 0){
						considerarTempoLC = "true";
					} else considerarTempoLC = "false";
				} 
				
				if (new ProcessoEventoExecucaoNe().isInformarQuantidade(processoEventoExecucaoDt.getId_EventoExecucao()).length() == 0){
					processoEventoExecucaoDt.setQuantidade("");
				}
				
				if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.REVOGACAO_LIVRAMENTO_CONDICIONAL))){
					if (considerarTempoLC.equalsIgnoreCase("true"))
						processoEventoExecucaoDt.setConsiderarTempoLivramentoCondicional("1");
					else processoEventoExecucaoDt.setConsiderarTempoLivramentoCondicional("0");
					processoEventoExecucaoDt.setId_LivramentoCondicional(idLivramentoCondicional);
				} else {
	            	processoEventoExecucaoDt.setId_LivramentoCondicional("");
	            	processoEventoExecucaoDt.setConsiderarTempoLivramentoCondicional("0");
	            }
				
				//TODO: o que fazer com o evento de comuta��o de pena?
				
				//-----fim set dados do evento
					
				//salva os dados do processoEventoExecucao
				salvarEvento(processoEventoExecucaoDt);
				
				//grava no arquivo de log
				saida.println("Registro n� " + registros + ":");
				saida.println("Execpen n� " + numeroExecpenAtual + ". Historico Execpen Numero: " + numeroHistoricoExecpen + ". Dados evento: " + processoEventoExecucaoDt.getPropriedades() + ".");
				if (processoEventoExecucaoDt.getEventoRegimeDt().getId().length() > 0)
					saida.println("Dados eventoRegime: " + processoEventoExecucaoDt.getEventoRegimeDt().getPropriedades() + ".");
				
				//captura o id do processoEventoExecucao caso seja LC, para ser utilizado no evento Revoga��o do LC, se existir.
				if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.CONCESSAO_LIVRAMENTO_CONDICIONAL)))
					idLivramentoCondicional = processoEventoExecucaoDt.getId();
					
				//salva o processoEventoExecucao dos eventos de "Interru��o da Pris�o Provis�ria", que n�o existe no Execpen
				if (processoEventoExecucaoDt.getId_EventoExecucao().equals(String.valueOf(EventoExecucaoDt.PRISAO_PROVISORIA))
						&& processoEventoExecucaoDt.getDataFim().length() > 0){
					ProcessoEventoExecucaoDt evento = new ProcessoEventoExecucaoDt();
					evento.setDataInicio(processoEventoExecucaoDt.getDataFim());
					evento.setId_EventoExecucao(String.valueOf(EventoExecucaoDt.INTERRUPCAO_PRISAO_PROVISORIA));
					evento.setEventoExecucaoDt(new ProcessoEventoExecucaoNe().consultarEventoExecucao(processoEventoExecucaoDt.getId_EventoExecucao()));
					evento.setId_Movimentacao(idMovimentacao);
					evento.setId_UsuarioLog(usuarioSessao.getId_Usuario());
					evento.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
					salvarEvento(evento);
					//grava no arquivo de log
					saida.println("Registro n� " + registros + ":");
					saida.println("Execpen n� " + numeroExecpenAtual + ". Historico Execpen Numero: " + numeroHistoricoExecpen + "(INTERRUP��O DA PRIS�O PROVIS�RIA) Dados evento: " + processoEventoExecucaoDt.getPropriedades() + ".");
				}
						
				numeroProcessoAntigo = numeroProcessoAtual;
				processoEventoExecucaoDt = null; //limpa o objeto
			}
			
			ImportarExecpenCt.valorAtualBarraProgresso = ImportarExecpenCt.totalBarraProgresso;
			
			mensagem = "Leitura do arquivo conclu�da com sucesso! \nN�mero de linhas do arquivo lido: " + registros + ".\n";
			saida.println(mensagem);
			saida.println("Fim da importa��o: " + Funcoes.DataHora(new Date()));
			saida.close();  
			writer.close(); 
		}
	
		return mensagem;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 */
	public List consultarDescricaoComarca(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		ComarcaNe neObjeto = new ComarcaNe();
		
		tempList = neObjeto.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ComarcaNe Comarcane = new ComarcaNe(); 
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		
		return stTemp;
	}
	
	/**
	 * Realiza chamada ao objeto que efetuar� a consulta
	 */
	public List consultarAreasDistribuicaoExecucao(String tempNomeBusca, String id_Comarca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
		
		tempList = neObjeto.consultarAreasDistribuicaoExecucao(tempNomeBusca, id_Comarca, posicaoPaginaAtual);
		QuantidadePaginas = neObjeto.getQuantidadePaginas();
		
		neObjeto = null;
		return tempList;
	}
	
	public String consultarAreasDistribuicaoExecucaoJSON(String tempNomeBusca, String id_Comarca, String posicaoPaginaAtual) throws Exception{
		String stTemp ="";
            AreaDistribuicaoNe neObjeto = new AreaDistribuicaoNe();
    		stTemp = neObjeto.consultarAreasDistribuicaoExecucaoJSON(tempNomeBusca, id_Comarca, posicaoPaginaAtual);
		return stTemp;
	}
	
	/**
     * Salva dos dados do evento de um processo de execu��o penal, incluindo regime.
     * @param processoEventoExecu��odt: dt com os dados do Evento
     * @author wcsilva
	 * @throws Exception 
     */
    private void salvarEvento(ProcessoEventoExecucaoDt processoEventoExecucaodt) throws Exception{
        ProcessoEventoExecucaoNe processoEventoExecucaoNe = new ProcessoEventoExecucaoNe();
    	EventoRegimeNe eventoRegimeNe = new EventoRegimeNe();
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            obFabricaConexao.iniciarTransacao();
            
            // altera processoEventoExecucaoDt
            processoEventoExecucaoNe.salvar(processoEventoExecucaodt, obFabricaConexao);
            
            //TODO ver o que vai fazer com a comuta��o
            
			// altera eventoRegimeDt
			if (processoEventoExecucaodt.getEventoRegimeDt().getId_RegimeExecucao().length() > 0 
					&& processoEventoExecucaodt.getEventoExecucaoDt().getAlteraRegime().equalsIgnoreCase("S")){
				processoEventoExecucaodt.getEventoRegimeDt().setId_ProcessoEventoExecucao(processoEventoExecucaodt.getId());
				eventoRegimeNe.salvar(processoEventoExecucaodt.getEventoRegimeDt(), obFabricaConexao);
			}

            obFabricaConexao.finalizarTransacao();

        } catch(Exception e) {
            obFabricaConexao.cancelarTransacao();
            throw e;
        } finally{
            obFabricaConexao.fecharConexao();
        }
    }
    
    /**
	 * Consultar n�mero completo dos processos de c�lculo de liquida��o de pena
	 * @author wcsilva
	 */
    public String relProcessoCalculoCadastrado() throws Exception {
		StringBuilder temp = new StringBuilder();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			List listaProcessos = new ProcessoNe().listarNumeroProcessoCalculo();
			
			Relatorios.getSeparadorRelatorioTxt();
			temp.append( "NumProcProj\n");
			for (int i = 0; i < listaProcessos.size(); i++) {
				temp.append( listaProcessos.get(i).toString() + "\n");  
			}
					
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return temp.toString();
	}
    
    /**
	 * Efetua cadastro de um processo de execu��o penal
	 * 
	 * @param processoExecu��odt: dt com os dados do processo
	 * @param usuarioDt: usuario que est� cadastrando o processo
	 * @author wcsilva
	 */
	public void cadastrarProcessoExecucao(ProcessoExecucaoDt processoExecucaodt, UsuarioDt usuarioDt, boolean isImportacaoExecpen) throws Exception {
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		ProcessoAssuntoNe processoAssuntoNe = new ProcessoAssuntoNe();
		ProcessoEventoExecucaoNe processoEventoExecucaoNe = new ProcessoEventoExecucaoNe();
		String id_Serventia = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();			

			LogDt logDt = new LogDt(processoExecucaodt.getId_UsuarioLog(), processoExecucaodt.getIpComputadorLog());

			String descricaoServentia = "";
			
			// Distribui�ao processo
			List listaServentia = new ServentiaNe().listarServentiasAreaDistribuicao(processoExecucaodt.getProcessoDt().getId_AreaDistribuicao());
			if (listaServentia != null && listaServentia.size() > 0){
				id_Serventia = (String)listaServentia.get(0);	
				descricaoServentia = new ServentiaNe().consultarServentiaDescricao(id_Serventia, obFabricaConexao);
				processoExecucaodt.getProcessoDt().setId_Serventia(id_Serventia);
				processoExecucaodt.getProcessoDt().setServentia(descricaoServentia);
			}

			//verifica se o Id do processo est� setado corretamente
			if (!processoExecucaodt.isProcessoNovo()){
				if (processoExecucaodt.getProcessoDt().getId().length() == 0 && processoExecucaodt.getId_ProcessoExecucaoPenal().length() > 0)
					processoExecucaodt.getProcessoDt().setId(processoExecucaodt.getId_ProcessoExecucaoPenal());
				else if (processoExecucaodt.getProcessoDt().getId().length() > 0 && processoExecucaodt.getId_ProcessoExecucaoPenal().length() == 0)
					processoExecucaodt.setId_ProcessoExecucaoPenal(processoExecucaodt.getProcessoDt().getId());	
			}
			
			//processo fisico ou processo para c�lculo - Atualiza o n�mero do processo de acordo com o informado pelo usu�rio. 
			String ano = processoExecucaodt.getProcessoFisicoNumero().substring(11, 15);
			String numero = processoExecucaodt.getProcessoFisicoNumero().substring(0, 7);
			String digito = processoExecucaodt.getProcessoFisicoNumero().substring(8, 10);
			processoExecucaodt.getProcessoDt().setAnoProcessoNumero(ano + Funcoes.completarZeros(numero,7) + digito);
			processoExecucaodt.getProcessoDt().setForumCodigo(String.valueOf(Funcoes.StringToLong(processoExecucaodt.getProcessoFisicoNumero().substring(21, 25))));
			
			// Seta fase do processo
			processoExecucaodt.getProcessoDt().setProcessoFaseCodigo(String.valueOf(ProcessoFaseDt.EXECUCAO));

			// Seta status do processo
			processoExecucaodt.getProcessoDt().setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.CALCULO));

			// Seta �rea do processo
			processoExecucaodt.getProcessoDt().setAreaCodigo(String.valueOf(AreaDt.CRIMINAL));

			// Salva processo - processoExecucaodt.getProcessoDt.getId() == processoExecucaodt.getId_ProcessoExecucaoPenal
			salvarProcessoExecucaoCompleto(processoExecucaodt, obFabricaConexao);

			// Salva partes do Processo
			if (processoExecucaodt.isProcessoNovo()) 
				new ProcessoParteNe().salvarPartesProcesso(processoExecucaodt.getProcessoDt().getListaPolosPassivos(), processoExecucaodt.getProcessoDt().getId(), logDt, obFabricaConexao);

			// Salva condena��es do Processo
			if (!processoExecucaodt.isMedidaSeguranca())
				new CondenacaoExecucaoNe().salvarListaCondenacao(processoExecucaodt.getListaCondenacoes(), processoExecucaodt.getId(), logDt, obFabricaConexao);

			// Gera movimenta��o GUIA DE RECOLHIMENTO INSERIDA
			MovimentacaoDt movimentacaoGR;
			if (isImportacaoExecpen){
				String complemento = descricaoServentia + " (Importado do Execpen)"; ;
				movimentacaoGR = movimentacaoNe.gerarMovimentacaoGuiaRecolhimentoInserida_UsuarioSistemaProjudi(processoExecucaodt.getProcessoDt().getId(), UsuarioServentiaDt.SistemaProjudi, logDt, obFabricaConexao, complemento);
			} else movimentacaoGR = movimentacaoNe.gerarMovimentacaoGuiaRecolhimentoInserida(processoExecucaodt.getProcessoDt().getId(), usuarioDt, logDt, obFabricaConexao);

			// salva eventos
			List listaEventos = new ProcessoExecucaoNe().montarListaEventosProcessoNovo(processoExecucaodt, movimentacaoGR,  UsuarioServentiaDt.SistemaProjudi);
			processoEventoExecucaoNe.salvarListaEvento(listaEventos, logDt, obFabricaConexao);

			if (processoExecucaodt.isProcessoNovo()){
				// Salva assuntos do processo
				processoAssuntoNe.salvarProcessoAssunto(processoExecucaodt.getProcessoDt().getListaAssuntos(), processoExecucaodt.getId_ProcessoExecucaoPenal(), logDt, obFabricaConexao);

				String id_ServentiaCargo = "";
				
				//para atender o provimento 16/2012
				// Distribui processo para um juiz
				//id_ServentiaCargo = DistribuicaoProcessoServentiaCargo.getInstance().getDistribuicao1Grau(id_Serventia,processoExecucaodt.getProcessoDt().getId_ProcessoTipo() );
				id_ServentiaCargo = DistribuicaoProcesso.getInstance().getDistribuicaoServentiaCargo(id_Serventia ,processoExecucaodt.getProcessoDt().getId_AreaDistribuicao()); 

				// Salva o juiz como respons�vel
				new ProcessoResponsavelNe().salvarProcessoResponsavel(processoExecucaodt.getId_ProcessoExecucaoPenal(), id_ServentiaCargo,true, CargoTipoDt.JUIZ_1_GRAU, logDt, obFabricaConexao);
				
				// Gera movimenta��o PROCESSO DISTRIBU�DO
				String complemento = descricaoServentia + " (Sem Regra de Redistribui��o - Processo F�sico para C�lculo de Liquida��o de Pena)";
				movimentacaoNe.gerarMovimentacaoProcessoDistribuido(processoExecucaodt.getProcessoDt().getId(), UsuarioServentiaDt.SistemaProjudi, complemento, logDt, obFabricaConexao);
			} 
			
			obFabricaConexao.finalizarTransacao();
			
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			cancelarCadastroProcessoExecucao(processoExecucaodt);
			throw e;
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	void cancelarCadastroProcessoExecucao(ProcessoExecucaoDt processoExecucaoDt){
		// Limpa id's dos objetos
		processoExecucaoDt.setId("");
		processoExecucaoDt.getProcessoDt().setId("");
		if (processoExecucaoDt.getProcessoDt().getPartesProcesso() != null) {
			List lista = processoExecucaoDt.getProcessoDt().getPartesProcesso();
			for (int i = 0; i < lista.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) lista.get(i);
				parteDt.setId("");
				if (parteDt.getEnderecoParte() != null) parteDt.getEnderecoParte().setId("");
				if (parteDt.getAdvogadoDt() != null) parteDt.getAdvogadoDt().setId("");
			}
		}
	}
	
	public void salvarProcessoExecucaoCompleto(ProcessoExecucaoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		try{
			// salva dados do processo (ProcessoDt)
			if (dados.getProcessoDt().getId_Processo().equalsIgnoreCase("")){
				new ProcessoNe().salvar(dados.getProcessoDt(), obFabricaConexao);
			}
			dados.setId_ProcessoExecucaoPenal(dados.getProcessoDt().getId());

			//salva demais dados do processo (ProcessoExecucaoDt)
			ProcessoExecucaoPs obPersistencia = new  ProcessoExecucaoPs(obFabricaConexao.getConexao());
			
			if (dados.getId().equalsIgnoreCase("")) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoExecucao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoExecucao", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch(Exception e) {
			dados.setId("");
			throw e;
		}
	}
}
