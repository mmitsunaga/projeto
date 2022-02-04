package br.gov.go.tj.projudi.ne;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;
import br.gov.go.tj.utils.Web;

public class GuiaNe {
	
	private static String SERVICO_1_GYN = "1";
	private static String SERVICO_2_GYN = "2";
	private static String CLIENTE_GYN = "1";
	//private static String senhaHomolog =  "876608997871";
	private static String senhaProducao = "415992652583";
	//private static String urlHolomog = "http://www.goiania.go.gov.br/homologa.asp";
	private static String urlProducao = "http://www.goiania.go.gov.br/servico.asp";
	
	private static String RETORNO_SOLICITACAO_PROCESSADA = "0";	
	private static String RETORNO_DIVIDA_BAIXADA = "121";	
	private static String RETORNO_PROTOCOLO_NAO_CADASTRADO = "122";
	private static String RETORNO_PROTOCOLO_FORA_CONVENIO = "123";
	private static String RETORNO_VALOR_CUSTAS_JA_CADASTRADO_ANTERIOMENTE = "124";
	
	private static String ID_CLASSIFICADOR_SOLICITACAO_PROCESSADA = "25329";	
	private static String ID_CLASSIFICADOR_DIVIDA_BAIXADA = "25330";	
	private static String ID_CLASSIFICADOR_PROTOCOLO_NAO_CADASTRADO = "25331";
	
	private static String FORUM_INVALIDO_SPG = "0000";
	private static String FORUM_GOIANIA = "0051";
	
	/**
	 * Serviço 1
	 * Consulta guias pagas na prefeitura de Goiânia numa data fornecida
	 * tipomovimento(1-Pagamento 2-Estorno)
	 * @param diaMovimento
	 * @param mesMovimento
	 * @param anoMovimento
	 * @return [protocolo, tipomovimento, valorcustas, dataarrecadacao, numeroguia]
	 * @throws Exception
	 */
	public String[][] consultarGuiaProcessualPaga(String diaMovimento, String mesMovimento, String anoMovimento) throws Exception {
		
		if (diaMovimento != null && diaMovimento.trim().length() == 1)
			diaMovimento = "0" + diaMovimento.trim();
		
		if (mesMovimento != null && mesMovimento.trim().length() == 1)
			mesMovimento = "0" + mesMovimento.trim();
		
		String dataHora = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "T" + new SimpleDateFormat("HH:mm:ss").format(new Date());
		String hash = "";
		String dados = "";
		String resposta = "";
		String[] registro = null;
		String[][] listaRegistros = null;
		String stTexto = "";
		
		try{
			hash = Funcoes.GeraHashMd5Completa0(SERVICO_1_GYN + CLIENTE_GYN + dataHora + senhaProducao);
			dados = "<?xml version=\"1.0\"?>" + 
				    "<solicitacao_envio> " + 
					"  <servico> " + 
					"    <numero>"+ SERVICO_1_GYN + "</numero> " + 
					"    <cliente>" + CLIENTE_GYN + "</cliente> " + 
					"    <datahora>" + dataHora + "</datahora> " + 
					"    <hash>" + hash + "</hash> " + 
					"  </servico> " + 
					"  <dados_envio> " + 
					"    <datamovimento>" + anoMovimento + "-" + mesMovimento + "-" + diaMovimento + "</datamovimento> " + 
					"  </dados_envio> " + 
					"</solicitacao_envio>";
			
		} catch(Exception e) {
			throw new MensagemException("Erro ao consultar guia paga na prefeitura de Goiânia. Mensagem: " + Funcoes.obtenhaConteudoPrimeiraExcecao(e));
		}
		resposta = Web.sendPostUrl(urlProducao, dados);
		
		Pattern pattern = Pattern.compile("<codigo>.*</codigo>");
		Matcher matcher = pattern.matcher(resposta);
		
		if(matcher.find()){
			stTexto = matcher.group();
			stTexto = stTexto.replaceAll("<codigo>|</codigo>", "");
		}
		// 0 - SOLICITACAO PROCESSADA
		// 1 - LAYOUT EM DESACORDO COM ESQUEMA XSD
		// 2 - SOLICITACAO EXPIRADA
		// 3 - SERVICO INEXISTENTE
		// 4 - CLIENTE INEXISTENTE
		// 5 - ACESSO NAO AUTORIZADO
		// 6 - HASH INVALIDO
		// 7 - IP DE ORIGEM NAO AUTORIZADO
		// 101 - NENHUM MOVIMENTO ENCONTRADO
		if(stTexto.equalsIgnoreCase(RETORNO_SOLICITACAO_PROCESSADA)) {
			pattern = Pattern.compile("<dados_resposta>.*</dados_resposta>");
			matcher = pattern.matcher(resposta);
	
			if (matcher.find()) {
				stTexto = matcher.group();
				stTexto = stTexto.replaceAll("<dados_resposta>|</dados_resposta>", "");
			}
			registro = stTexto.split("@#\\|");
			listaRegistros = new String[registro.length][];
			
			for (int i = 0; i < registro.length; i++) {
				listaRegistros[i] = registro[i].split("\\|@#");
			}
			
		} else if(!stTexto.equalsIgnoreCase("101")) {
			pattern = Pattern.compile("<texto>.*</texto>");
			matcher = pattern.matcher(resposta);
	
			if (matcher.find()) {
				stTexto = matcher.group();
				stTexto = stTexto.replaceAll("<texto>|</texto>", "");
				throw new MensagemException(stTexto);
			} else {
				throw new MensagemException("Ocorreu um erro interno no webservice. Resposta: " + resposta);
			}
		}
		return listaRegistros;
	}
	/**
	 * Serviço 2
	 * Atualiza as guias pagas na prefeitura de Goiânia
	 * @param protocolo número do processo com 20 posições(somente dígitos)
	 * @param guia número da guia
	 * @param valor valor da guia(234.54)
	 * @param usuario
	 * @param dia dia do pagamento
	 * @param mes mes do pagamento
	 * @param ano ano do pagamento
	 * @param forumCodigoGoianiaPROTOCOLO_NAO_CADASTRADO
	 * @return Código de retorno do webservice
	 * @throws Exception
	 */
	public String atualizaCustaProcessual(String protocolo, String guia, String valor, String usuario, String dia, String mes, String ano, String forumCodigoGoianiaPROTOCOLO_NAO_CADASTRADO) throws Exception{
		
		if (dia != null && dia.trim().length() == 1)
			dia = "0" + dia.trim();
		
		if (mes != null && mes.trim().length() == 1)
			mes = "0" + mes.trim();
		
		if( forumCodigoGoianiaPROTOCOLO_NAO_CADASTRADO != null && forumCodigoGoianiaPROTOCOLO_NAO_CADASTRADO.equalsIgnoreCase(FORUM_GOIANIA) ) {
			if( protocolo != null && protocolo.length() > 10 ) {
				String[] arrayProcotolo = protocolo.split("\\.");
				
				protocolo = arrayProcotolo[0] + "." + arrayProcotolo[1] + "." + arrayProcotolo[2] + "." + arrayProcotolo[3] + "." + arrayProcotolo[4] + "." + FORUM_GOIANIA;
			}
		}
		
		if (protocolo != null)
			protocolo = Funcoes.retiraVirgulaPonto(protocolo);
		
		if (valor != null)
			valor = valor.replaceAll(",", "");
		
		String dataHora = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "T" + new SimpleDateFormat("HH:mm:ss").format(new Date());
		String hash = "";
		String dados = "";
		String resposta = "";
		String stTexto = "";
		
		
		hash = Funcoes.GeraHashMd5Completa0(SERVICO_2_GYN + CLIENTE_GYN + dataHora + senhaProducao);
		dados = "<?xml version=\"1.0\"?>" + 
			    "<solicitacao_envio> " + 
				"  <servico> " + 
				"    <numero>"+ SERVICO_2_GYN + "</numero> " + 
				"    <cliente>" + CLIENTE_GYN + "</cliente> " + 
				"    <datahora>" + dataHora + "</datahora> " + 
				"    <hash>" + hash + "</hash> " + 
				"  </servico> " + 
				"  <dados_envio> " + 
				"    <protocolo>" + protocolo + "</protocolo>" +
				"    <numeroguia>" + guia + "</numeroguia>" +
				"    <valorcustas>" + valor + "</valorcustas>" +
				"    <usuariotj>" + usuario + "</usuariotj>" +
				"    <datageracao>" + ano + "-" + mes + "-" + dia + "</datageracao>" +
				"  </dados_envio> " + 
				"</solicitacao_envio>";
			

		resposta = Web.sendPostUrl(urlProducao, dados);
		
		Pattern pattern = Pattern.compile("<codigo>.*</codigo>");
		Matcher matcher = pattern.matcher(resposta);
		
		if(matcher.find()){
			stTexto = matcher.group();
			stTexto = stTexto.replaceAll("<codigo>|</codigo>", "").trim();
		}
		// 0 - SOLICITACAO PROCESSADA
		// 1 - LAYOUT EM DESACORDO COM ESQUEMA XSD
		// 2 - SOLICITACAO EXPIRADA
		// 3 - SERVICO INEXISTENTE
		// 4 - CLIENTE INEXISTENTE
		// 5 - ACESSO NAO AUTORIZADO
		// 6 - HASH INVALIDO
		// 7 - IP DE ORIGEM NAO AUTORIZADO
		// 101 - SERVICO INDISPONIVEL
		// 102 - CARACTERES NAO PERMITIDOS EM DADOS ENVIO 
		// 121 - DIVIDA BAIXADA
		// 122 - PROTOCOLO NAO CADASTRADO
		// 123 - PROTOCOLO FORA DO CONVENIO
		// 124 - VALOR DE CUSTAS JÁ CADASTRADO ANTERIOMENTE
		// 999 - ERRO NAO CATALOGADO
		if(stTexto.equalsIgnoreCase(RETORNO_SOLICITACAO_PROCESSADA) || 
		   stTexto.equalsIgnoreCase(RETORNO_DIVIDA_BAIXADA) || 
		   stTexto.equalsIgnoreCase(RETORNO_PROTOCOLO_NAO_CADASTRADO) ||
		   stTexto.equalsIgnoreCase(RETORNO_PROTOCOLO_FORA_CONVENIO) ||
		   stTexto.equalsIgnoreCase(RETORNO_VALOR_CUSTAS_JA_CADASTRADO_ANTERIOMENTE)) {
			return stTexto;
		} else {
			pattern = Pattern.compile("<texto>.*</texto>");
			matcher = pattern.matcher(resposta);
	
			if (matcher.find()) {
				stTexto = matcher.group();
				stTexto = stTexto.replaceAll("<texto>|</texto>", "");
				throw new MensagemException(stTexto);
			} else {
				throw new MensagemException("Ocorreu um erro interno no webservice. Resposta: " + resposta);
			}
		}	
	}
	
	/**
	 * Enviar todas as guias para a prefeitura.
	 * 
	 * @throws Exception
	 */
	public void enviarGuiasPendentesPrefeitura() throws Exception
	{
		cancelarGuiaUnicaQuandoJaExistirGuiaFazendaMunicipalPaga();
		cancelarGuiaUnicaQuandoTiverSidoCanceladaUsuario();
		enviarGuiasNaoPagasPendentesPrefeitura();
	}
	
	private void enviarGuiasNaoPagasPendentesPrefeitura() throws Exception
	{
		TenteGravarLogEnviarGuiasPendentesPrefeitura("Início Processamento");
		
		List listaGuiasStatusAguardandoEnvioPrefeitura = null;
				
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		LogNe logNe = new LogNe();
		
		listaGuiasStatusAguardandoEnvioPrefeitura = guiaEmissaoNe.consultarGuiasAguardandoEnvioPrefeitura();
		
		TenteGravarLogEnviarGuiasPendentesPrefeitura("Quantidade de Guias: " + listaGuiasStatusAguardandoEnvioPrefeitura.size());
		
		for(int i = 0; i < listaGuiasStatusAguardandoEnvioPrefeitura.size(); i++) {
			GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiasStatusAguardandoEnvioPrefeitura.get(i);
			enviarGuiaParaPrefeitura(guiaEmissaoDt, guiaEmissaoNe, logNe);
		}
		
		TenteGravarLogEnviarGuiasPendentesPrefeitura("Fim Processamento");
	}
	
//	public void enviarGuiasPrefeituraRetiradaDeCustas(String numeroProcessoCompleto, String numeroGuiaCompleto) throws Exception
//	{	
//		TJDataHora dataEmissao = new TJDataHora();
//		String retornoAtualizaCustaProcessual = this.atualizaCustaProcessual(numeroProcessoCompleto, 
//				numeroGuiaCompleto, 
//                "0,00", 
//                "Projudi", 
//                String.valueOf(dataEmissao.getDia()), 
//                String.valueOf(dataEmissao.getMes()), 
//                String.valueOf(dataEmissao.getAno()));
//		
//		if (!retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_SOLICITACAO_PROCESSADA)) {								
//			throw new MensagemException(retornoAtualizaCustaProcessual);
//		} 
//	}
	
	/**
	 * Enviar guia para a prefeitura.
	 * 
	 * @param guiaEmissaoDt
	 * @param guiaEmissaoNe
	 * @param logNe
	 * @throws Exception
	 */
	private void enviarGuiaParaPrefeitura(GuiaEmissaoDt guiaEmissaoDt, GuiaEmissaoNe guiaEmissaoNe, LogNe logNe) throws Exception
	{
		LogDt obLogDt = new LogDt("ExecuçãoAutomaticaEnviarGuiaEmissaoPrefeitura", guiaEmissaoDt.getId(), UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), guiaEmissaoDt.getPropriedades());
		obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));
		
		TJDataHora dataEmissao = new TJDataHora();
		dataEmissao.setDataaaaa_MM_ddHHmmss(guiaEmissaoDt.getDataEmissao());
		String retornoAtualizaCustaProcessual;
		
		ProcessoNe processoNe = new ProcessoNe();
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		
		try 
		{
			ProcessoDt processo = processoNe.consultarId(guiaEmissaoDt.getId_Processo());
			
			if (processo != null && (Funcoes.StringToInt(processo.getProcessoStatusCodigo()) == ProcessoStatusDt.ATIVO || Funcoes.StringToInt(processo.getProcessoStatusCodigo()) == ProcessoStatusDt.ATIVO_PROVISORIAMENTE)) {
				
				retornoAtualizaCustaProcessual = this.atualizaCustaProcessual(processo.getProcessoNumeroAntigoCompleto(), 
															                  guiaEmissaoDt.getNumeroGuiaCompleto(), 
															                  guiaEmissaoDt.getValorTotalGuia(), 
															                  "Projudi", 
															                  String.valueOf(dataEmissao.getDia()), 
															                  String.valueOf(dataEmissao.getMes()), 
															                  String.valueOf(dataEmissao.getAno()),
															                  null); 
				
				if (retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_SOLICITACAO_PROCESSADA) ||
				    retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_VALOR_CUSTAS_JA_CADASTRADO_ANTERIOMENTE)) {
					guiaEmissaoNe.atualizarStatusGuiaEmitidaEnviadaPrefeitura(guiaEmissaoDt.getId());
					
					// Altera classificador do processo para Execução Fiscal (Prefeitura) - Guia Única Processada
					processoNe.alterarClassificadorProcesso(processo.getId(), processo.getId_Classificador(), ID_CLASSIFICADOR_SOLICITACAO_PROCESSADA, new LogDt(UsuarioDt.SistemaProjudi, "Servidor"));
					
					//Envia Guia para Atualização do Status no SPG
					guiaSPGNe.atualizarParaTransmitidaGuiaSPG(guiaEmissaoDt);
					
					logNe.salvar(obLogDt);
					
					//Consultar Guia Fazenda Municipal (Antiga) para Cancelamento, esse processo só será executado durante a implantação...
					GuiaEmissaoDt guiaEmissaoFazendaMunicipalDt = guiaEmissaoNe.consultarGuiaFazendaMunicipal(processo.getId());					
					if (guiaEmissaoFazendaMunicipalDt != null) guiaEmissaoNe.cancelarGuiaEmitida(guiaEmissaoFazendaMunicipalDt);					
					
				} else if (retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_DIVIDA_BAIXADA)) {
					guiaEmissaoNe.atualizarStatusDividaBaixadaPrefeitura(guiaEmissaoDt.getId());
					
					// Altera classificador do processo para Execução Fiscal (Prefeitura) - Dívida Baixada
					processoNe.alterarClassificadorProcesso(processo.getId(), processo.getId_Classificador(), ID_CLASSIFICADOR_DIVIDA_BAIXADA, new LogDt(UsuarioDt.SistemaProjudi, "Servidor"));
					
					//Envia Guia para Cancelamento no SPG
					guiaSPGNe.cancelarGuiaSPG(guiaEmissaoDt);
					
					gravaLogDeErroEnviarGuiaParaPrefeitura(guiaEmissaoDt, logNe, obLogDt, "Dívida Já Baixada na Prefeitura.");
				} else if (retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_PROTOCOLO_NAO_CADASTRADO)) {
					guiaEmissaoNe.atualizarStatusProtocoloNaoCadastradoPrefeitura(guiaEmissaoDt.getId());
					
					// Altera classificador do processo para Execução Fiscal (Prefeitura) - Protocolo não Cadastrado
					processoNe.alterarClassificadorProcesso(processo.getId(), processo.getId_Classificador(), ID_CLASSIFICADOR_PROTOCOLO_NAO_CADASTRADO, new LogDt(UsuarioDt.SistemaProjudi, "Servidor"));
					
					//Envia Guia para Cancelamento no SPG
					guiaSPGNe.cancelarGuiaSPG(guiaEmissaoDt);
					
					gravaLogDeErroEnviarGuiaParaPrefeitura(guiaEmissaoDt, logNe, obLogDt, "Protocolo não cadastrado na Prefeitura.");
				} else if (retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_PROTOCOLO_FORA_CONVENIO)) {
					guiaEmissaoNe.atualizarStatusProcessoForaConvenioPrefeitura(guiaEmissaoDt.getId());
					//Envia Guia para Cancelamento no SPG
					guiaSPGNe.cancelarGuiaSPG(guiaEmissaoDt);
					
					gravaLogDeErroEnviarGuiaParaPrefeitura(guiaEmissaoDt, logNe, obLogDt, "Protocolo não faz parte do convênio da Prefeitura.");
				} else {
					guiaEmissaoNe.atualizarStatusProcessoInativoTJGOPrefeitura(guiaEmissaoDt.getId());
					guiaSPGNe.cancelarGuiaSPG(guiaEmissaoDt);
					gravaLogDeErroEnviarGuiaParaPrefeitura(guiaEmissaoDt, logNe, obLogDt, "retornoAtualizaCustaProcessual não mapeado.");		
				}
				
			} else if (processo == null) {
				TenteGravarLogJobGerarGuiasFazendaPublica("Processo não localizado com id " + guiaEmissaoDt.getId_Processo());
			} else {
				guiaEmissaoNe.cancelarGuiaEmitida(guiaEmissaoDt);
				guiaSPGNe.cancelarGuiaSPG(guiaEmissaoDt);
				TenteGravarLogJobGerarGuiasFazendaPublica("Processo não ativo com id " + guiaEmissaoDt.getId_Processo());
			}
						
		} catch(MensagemException e) {
			gravaLogDeErroEnviarGuiaParaPrefeitura(guiaEmissaoDt, logNe, obLogDt, e.getMessage());
		} catch(Exception e) {
			if (guiaEmissaoDt != null) TenteGravarLogJobGerarGuiasFazendaPublica("Guia não enviada para o processo com id " + guiaEmissaoDt.getId_Processo() + " Mensagem: " + Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			throw e;
		}
	}
	
	/**
	 * Método responsável em obter as guias de fazenda municial (antigo formato) que 
	 * foram pagas (momento de transição), cancelando assim as guias da prefeitura.
	 * @throws Exception
	 */
	private void cancelarGuiaUnicaQuandoJaExistirGuiaFazendaMunicipalPaga() throws Exception
	{
		TenteGravarLogCancelarGuiaUnicaQuandoJaExistirGuiaFazendaMunicipalPaga("Início Processamento");
		
		List listaGuiasPrefeituraGuiaFazendaPaga = null;
				
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		LogNe logNe = new LogNe();
		
		listaGuiasPrefeituraGuiaFazendaPaga = guiaEmissaoNe.consultarGuiasPrefeiturasQueDevemSerCanceladasPeloPagamentoDaGuiaFazendaMunicipal();
		
		TenteGravarLogCancelarGuiaUnicaQuandoJaExistirGuiaFazendaMunicipalPaga("Quantidade de Guias: " + listaGuiasPrefeituraGuiaFazendaPaga.size());
		
		for(int i = 0; i < listaGuiasPrefeituraGuiaFazendaPaga.size(); i++) {
			GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiasPrefeituraGuiaFazendaPaga.get(i);
			cancelarGuiaParaPrefeitura(guiaEmissaoDt, guiaEmissaoNe, logNe, true, null);
		}
		
		TenteGravarLogCancelarGuiaUnicaQuandoJaExistirGuiaFazendaMunicipalPaga("Fim Processamento");
	}
	
	/**
	 * Método responsável em obter as guias que foram canceladas por um usuário do Projudi, no caso por exemplo, de assistência judiciária deferida.
	 * @throws Exception
	 */
	private void cancelarGuiaUnicaQuandoTiverSidoCanceladaUsuario() throws Exception
	{
		TenteGravarLogCancelaGuiasPrefeituraCanceladaUsuario("Início Processamento");
		
		List listaGuiasPrefeituraCanceladaUsuario = null;

		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		LogNe logNe = new LogNe();
		
		listaGuiasPrefeituraCanceladaUsuario = guiaEmissaoNe.consultarGuiasPrefeiturasCanceladasUsuarioEnvioPendentes();

		TenteGravarLogCancelaGuiasPrefeituraCanceladaUsuario("Quantidade de Guias: " + listaGuiasPrefeituraCanceladaUsuario.size());
		
		for(int i = 0; i < listaGuiasPrefeituraCanceladaUsuario.size(); i++) {
			GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) listaGuiasPrefeituraCanceladaUsuario.get(i);
			cancelarGuiaParaPrefeituraQuandoTiverSidoCanceladaUsuario(guiaEmissaoDt, guiaEmissaoNe, logNe, true, null);
		}
		
		TenteGravarLogCancelaGuiasPrefeituraCanceladaUsuario("Fim Processamento");
	}
	
	/**
	 * Cancelar guia para a prefeitura.
	 * 
	 * @param guiaEmissaoDt
	 * @param guiaEmissaoNe
	 * @param logNe
	 * @throws Exception
	 */
	private void cancelarGuiaParaPrefeitura(GuiaEmissaoDt guiaEmissaoDt, GuiaEmissaoNe guiaEmissaoNe, LogNe logNe, boolean reenviaProtocoloNaoCadastrado, String forumCodigoGoianiaPROTOCOLO_NAO_CADASTRADO) throws Exception
	{
		LogDt obLogDt = new LogDt("ExecuçãoAutomaticaCancelarGuiaEmissaoPrefeituraPagTJGO", guiaEmissaoDt.getId(), UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), guiaEmissaoDt.getPropriedades());
		obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));
		
		TJDataHora dataEmissao = new TJDataHora();
		dataEmissao.setDataaaaa_MM_ddHHmmss(guiaEmissaoDt.getDataEmissao());
		String retornoAtualizaCustaProcessual;
		
		ProcessoNe processoNe = new ProcessoNe();
		
		try 
		{
			ProcessoDt processo = processoNe.consultarId(guiaEmissaoDt.getId_Processo());
			
			if (processo != null) {
				
				GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
				
				//Envia Guia para Cancelamento no SPG
				guiaSPGNe.cancelarGuiaSPG(guiaEmissaoDt);
				
				//Envia Guia para Cancelamento no Projudi
				guiaEmissaoNe.cancelarGuiaEmitida(guiaEmissaoDt);
				
				retornoAtualizaCustaProcessual = this.atualizaCustaProcessual(processo.getProcessoNumeroAntigoCompleto(), 
		                  guiaEmissaoDt.getNumeroGuiaCompleto(), 
		                  "0.00", 
		                  "Projudi", 
		                  String.valueOf(dataEmissao.getDia()), 
		                  String.valueOf(dataEmissao.getMes()), 
		                  String.valueOf(dataEmissao.getAno()),
		                  forumCodigoGoianiaPROTOCOLO_NAO_CADASTRADO); 
				
				
				if (reenviaProtocoloNaoCadastrado && retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_PROTOCOLO_NAO_CADASTRADO)) {	
					if (Funcoes.StringToInt(processo.getForumCodigo()) == 0) processo.setForumCodigo(FORUM_GOIANIA);
					else  processo.setForumCodigo(FORUM_INVALIDO_SPG);					
					cancelarGuiaParaPrefeitura(guiaEmissaoDt, guiaEmissaoNe, logNe, false, FORUM_GOIANIA);					
				} else if (retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_SOLICITACAO_PROCESSADA) ||
					retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_VALOR_CUSTAS_JA_CADASTRADO_ANTERIOMENTE) ||
					retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_DIVIDA_BAIXADA) ||
					retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_PROTOCOLO_NAO_CADASTRADO) ||
					retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_PROTOCOLO_FORA_CONVENIO)) {				
					logNe.salvar(obLogDt);
				} else {
					gravaLogDeErroEnviarGuiaParaPrefeitura(guiaEmissaoDt, logNe, obLogDt, "retornoAtualizaCustaProcessual não mapeado.");		
				}
				
			} else if (processo == null) {
				TenteGravarLogJobGerarGuiasFazendaPublica("Processo não localizado com id " + guiaEmissaoDt.getId_Processo());
			} 
						
		} catch(MensagemException e) {
			gravaLogDeErroEnviarGuiaParaPrefeitura(guiaEmissaoDt, logNe, obLogDt, e.getMessage());
		} catch(Exception e) {
			if (guiaEmissaoDt != null) TenteGravarLogJobGerarGuiasFazendaPublica("Guia não enviada para o processo com id " + guiaEmissaoDt.getId_Processo() + " Mensagem: " + Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			throw e;
		}
	}
	
	private void cancelarGuiaParaPrefeituraQuandoTiverSidoCanceladaUsuario(GuiaEmissaoDt guiaEmissaoDt, GuiaEmissaoNe guiaEmissaoNe, LogNe logNe, boolean reenviaProtocoloNaoCadastrado, String forumCodigoGoianiaPROTOCOLO_NAO_CADASTRADO) throws Exception
	{	
		LogDt obLogDt = new LogDt("ExecuçãoAutomaticaCancelarGuiaEmissaoPrefeituraCancUsuario", guiaEmissaoDt.getId(), UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), guiaEmissaoDt.getPropriedades());
		obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));
		
		TJDataHora dataEmissao = new TJDataHora();
		dataEmissao.setDataaaaa_MM_ddHHmmss(guiaEmissaoDt.getDataEmissao());
		String retornoAtualizaCustaProcessual;
		
		ProcessoNe processoNe = new ProcessoNe();
		
		try 
		{
			ProcessoDt processo = processoNe.consultarId(guiaEmissaoDt.getId_Processo());
			
			if (processo != null) {
				
				retornoAtualizaCustaProcessual = this.atualizaCustaProcessual(processo.getProcessoNumeroAntigoCompleto(), 
		                  guiaEmissaoDt.getNumeroGuiaCompleto(), 
		                  "0.00", 
		                  "Projudi", 
		                  String.valueOf(dataEmissao.getDia()), 
		                  String.valueOf(dataEmissao.getMes()), 
		                  String.valueOf(dataEmissao.getAno()),
		                  forumCodigoGoianiaPROTOCOLO_NAO_CADASTRADO); 
				
				if (reenviaProtocoloNaoCadastrado && retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_PROTOCOLO_NAO_CADASTRADO)) {	
					if (Funcoes.StringToInt(processo.getForumCodigo()) == 0) processo.setForumCodigo(FORUM_GOIANIA);
					else  processo.setForumCodigo(FORUM_INVALIDO_SPG);					
					cancelarGuiaParaPrefeituraQuandoTiverSidoCanceladaUsuario(guiaEmissaoDt, guiaEmissaoNe, logNe, false, FORUM_GOIANIA);					
				} else if (retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_SOLICITACAO_PROCESSADA) ||
					retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_VALOR_CUSTAS_JA_CADASTRADO_ANTERIOMENTE) ||
					retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_DIVIDA_BAIXADA) ||
					retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_PROTOCOLO_NAO_CADASTRADO) ||
					retornoAtualizaCustaProcessual.equalsIgnoreCase(RETORNO_PROTOCOLO_FORA_CONVENIO)) {
					
					guiaEmissaoNe.atualizarGuiasPrefeiturasCanceladasUsuarioPendentesDeEnvio(guiaEmissaoDt.getId());
					
					logNe.salvar(obLogDt);
				} else {
					gravaLogDeErroEnviarGuiaParaPrefeitura(guiaEmissaoDt, logNe, obLogDt, "retornoAtualizaCustaProcessual não mapeado.");		
				}
				
			} else if (processo == null) {
				TenteGravarLogJobGerarGuiasFazendaPublica("Processo não localizado com id " + guiaEmissaoDt.getId_Processo());
			} 
						
		} catch(MensagemException e) {
			gravaLogDeErroEnviarGuiaParaPrefeitura(guiaEmissaoDt, logNe, obLogDt, e.getMessage());
		} catch(Exception e) {
			if (guiaEmissaoDt != null) TenteGravarLogJobGerarGuiasFazendaPublica("Guia não enviada para o processo com id " + guiaEmissaoDt.getId_Processo() + " Mensagem: " + Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			throw e;
		}
	}
	
	private void gravaLogDeErroEnviarGuiaParaPrefeitura(GuiaEmissaoDt guiaEmissaoDt, LogNe logNe, LogDt obLogDt, String mensagem) throws Exception
	{
		if (guiaEmissaoDt != null) TenteGravarLogJobGerarGuiasFazendaPublica("Guia não enviada para o processo com id " + guiaEmissaoDt.getId_Processo() + " Mensagem: " + mensagem);
		
		obLogDt.setId("");
		obLogDt.setId_LogTipo("");
		obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ErroIntegracaoPrefeituraGoiania));
		obLogDt.setValorAtual("Erro ao Enviar Guia Prefeitura de Goiânia " + Funcoes.DataHora(new Date()));
		obLogDt.setValorNovo(mensagem);
		logNe.salvar(obLogDt);
	}
	
	public void atualizarGuiasPagasPrefeitura() throws Exception
	{
		TenteGravarLogAtualizarGuiasPagasPrefeitura("Início Processamento");
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try
		{
			GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
			
			LogDt obLogDt = new LogDt("ExecuçãoAutomaticaAtualizarGuiasPagasPrefeitura", "", UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), "");
			obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));
			
			LogNe logNe = new LogNe();
			TJDataHora dataMovimento = obtenhaDataHoraUltimaAtualizarGuiasPagasPrefeitura(logNe);;
					
			while (dataMovimento.getDataHorayyyyMMdd() < new TJDataHora().getDataHorayyyyMMdd()) {			
				String[][] listaRegistros = null;
				try 
				{
					TenteGravarLogAtualizarGuiasPagasPrefeitura("Início Processamento para o dia " + dataMovimento.getDataHorayyyyMMdd());
					
					listaRegistros = this.consultarGuiaProcessualPaga(String.valueOf(dataMovimento.getDia()), String.valueOf(dataMovimento.getMes()), String.valueOf(dataMovimento.getAno()));
					
					if (listaRegistros != null) {
						TenteGravarLogAtualizarGuiasPagasPrefeitura("Quantidade de Guias Pagas " + listaRegistros.length);
						for (int i=0; i< listaRegistros.length; i++ ) {
							 if (listaRegistros[i].length >= 7) {
								 atualizarGuiaPagaPrefeitura(dataMovimento, listaRegistros[i][0], listaRegistros[i][1], listaRegistros[i][2], listaRegistros[i][3], listaRegistros[i][4], listaRegistros[i][5], listaRegistros[i][6], guiaEmissaoNe, logNe, obFabricaConexao);
								 TenteGravarLogAtualizarGuiasPagasPrefeitura("Processamento realizado para o processo " + listaRegistros[i][0] + 
										                                     " tipoMovimento " + listaRegistros[i][1] + 
										                                     " valorCustas " + listaRegistros[i][2] + 
										                                     " dataArrecadacao " + listaRegistros[i][3] +
										                                     " numeroGuia " + listaRegistros[i][4] +
										                                     " numeroBancoPagamento " + listaRegistros[i][5] +
										                                     " numeroAgenciaPagamento " + listaRegistros[i][6] + 
										                                     " dataMovimento " + dataMovimento.getDataHoraFormatadayyyyMMdd());
							 }
						}	
					} else {
						TenteGravarLogAtualizarGuiasPagasPrefeitura("Nenhuma Guias Paga");
					}
					
					TenteGravarLogAtualizarGuiasPagasPrefeitura("Fim Processamento para o dia " + dataMovimento.getDataHorayyyyMMdd());
					 
					LogDt obLogControleDataDt = new LogDt("AtualizarGuiasPagasPrefeitura", "", UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), "");
					obLogControleDataDt.setLogTipoCodigo(String.valueOf(LogTipoDt.IntegracaoPrefeituraGoiania));
					obLogControleDataDt.setData(dataMovimento.getDataFormatadaddMMyyyy());
					logNe.salvar(obLogControleDataDt, obFabricaConexao);
					
				} catch(MensagemException e) {
					TenteGravarLogAtualizarGuiasPagasPrefeitura("Erro ao Consultar Guias Pagas Prefeitura de Goiânia " + e.getMessage());
					
					obLogDt.setId("");
					obLogDt.setId_LogTipo("");
					obLogDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ErroIntegracaoPrefeituraGoiania));
					obLogDt.setValorAtual("Erro ao Consultar Guias Pagas Prefeitura de Goiânia " + Funcoes.DataHora(new Date()));
					obLogDt.setValorNovo(e.getMessage());
					logNe.salvar(obLogDt, obFabricaConexao);
				} catch(Exception e) {
					TenteGravarLogAtualizarGuiasPagasPrefeitura("Erro ao Consultar Guias Pagas Prefeitura de Goiânia " + Funcoes.obtenhaConteudoPrimeiraExcecao(e));					
					throw e;
				} finally{
					TenteGravarLogAtualizarGuiasPagasPrefeitura("Fim Processamento para o dia " + dataMovimento.getDataHorayyyyMMdd());
				}
				
				dataMovimento.adicioneDia(1);
			}
		}
		finally 
		{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
			TenteGravarLogAtualizarGuiasPagasPrefeitura("Fim Processamento");
		}
	}
	
	private TJDataHora obtenhaDataHoraUltimaAtualizarGuiasPagasPrefeitura(LogNe logNe) throws Exception{
		
		LogDt obUltimoLogDt = logNe.consultarUltimoLog(String.valueOf(LogTipoDt.IntegracaoPrefeituraGoiania));
		
		TJDataHora dataEmissao = null;
		if (obUltimoLogDt != null) {
			dataEmissao = new TJDataHora(obUltimoLogDt.getData());
			dataEmissao.adicioneDia(1);
		} else {
			// Data da implantação...
			dataEmissao = new TJDataHora("20/08/2014");			
		}
		
		return dataEmissao;
	}
	
	private void atualizarGuiaPagaPrefeitura(TJDataHora dataMovimento, String protocolo, String tipoMovimento, String valorCustas, String dataArrecadacao, String numeroGuia, String numeroBancoPagamento, String numeroAgenciaPagamento, GuiaEmissaoNe guiaEmissaoNe, LogNe logNe,  FabricaConexao obFabricaConexao) throws Exception{
		GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(numeroGuia, obFabricaConexao);		
		if (guiaEmissaoDt != null) {
			atualizarGuiaPagaPrefeitura(guiaEmissaoDt, dataMovimento, protocolo, tipoMovimento, valorCustas, dataArrecadacao,numeroBancoPagamento , numeroAgenciaPagamento, guiaEmissaoNe, logNe);			
		} else {
			LogDt logDt = new LogDt("ExecuçãoAutomaticaAtualizarGuiasPagasPrefeitura", "", UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), "A guia " + numeroGuia + " não foi encontrada."); 
			logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));
			logNe.salvar(logDt);
		}
	}
	
	private void atualizarGuiaPagaPrefeitura(GuiaEmissaoDt guiaEmissaoDt, TJDataHora dataMovimento, String protocolo, String tipoMovimento, String valorCustas, String dataArrecadacao, String numeroBancoPagamento, String numeroAgenciaPagamento, GuiaEmissaoNe guiaEmissaoNe, LogNe logNe) throws Exception
	{
		AtualizeNumeroDoProcessoNaGuia(guiaEmissaoDt);
		if (guiaEmissaoDt.getNumeroProcesso().trim().equalsIgnoreCase(protocolo.trim()) || Funcoes.StringToLong(guiaEmissaoDt.getNumeroProcesso().trim()) == Funcoes.StringToLong(protocolo.trim())) {
			if (tipoMovimento.trim().equalsIgnoreCase("1")) {
				// Pagamento
				atualizarGuiaPagaPrefeituraTipoMovimentoPagamento(guiaEmissaoDt, dataMovimento, valorCustas, dataArrecadacao, numeroBancoPagamento, numeroAgenciaPagamento, guiaEmissaoNe, logNe);
			} else if (tipoMovimento.trim().equalsIgnoreCase("2")) {
				// Estorno
				atualizarGuiaPagaPrefeituraTipoMovimentoEstorno(guiaEmissaoDt, guiaEmissaoNe, logNe);
			} else {
				LogDt logDt = new LogDt("ExecuçãoAutomaticaAtualizarGuiasPagasPrefeitura", "", UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), "A guia " + guiaEmissaoDt.getNumeroGuiaCompleto() + " possui o tipoMovimento (" + tipoMovimento + ") não mapeado."); 
				logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));
				logNe.salvar(logDt);
			}
		} else {
			LogDt logDt = new LogDt("ExecuçãoAutomaticaAtualizarGuiasPagasPrefeitura", "", UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), "A guia " + guiaEmissaoDt.getNumeroGuiaCompleto() + " é do processo projudi " + guiaEmissaoDt.getNumeroProcesso() + ", mas é do processo/protocolo prefeitura " + protocolo + "."); 
			logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));
			logNe.salvar(logDt);
		}
	}
	
	private void AtualizeNumeroDoProcessoNaGuia(GuiaEmissaoDt guiaEmissaoDt) throws Exception
	{
		guiaEmissaoDt.setNumeroProcesso("");
		ProcessoDt processoDt = new ProcessoNe().consultarIdCompleto(guiaEmissaoDt.getId_Processo());
		if (processoDt != null) {
			guiaEmissaoDt.setNumeroProcesso(Funcoes.retiraVirgulaPonto(processoDt.getProcessoNumeroAntigoCompleto()));
			guiaEmissaoDt.setProcessoDt(processoDt);
		}
	}
	
	public void atualizarGuiaPagaPrefeituraTipoMovimentoPagamento(GuiaEmissaoDt guiaEmissaoDt, TJDataHora dataMovimento,  String valorCustas, String dataArrecadacao, String numeroBancoPagamento, String numeroAgenciaPagamento, GuiaEmissaoNe guiaEmissaoNe, LogNe logNe) throws Exception
	{
		FabricaConexao obFabricaConexao = null;
		
		try
		{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			if( !guiaEmissaoNe.isGuiaRessarcidoPedidoRessarcido(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao) ) {
				if( !guiaEmissaoNe.isGuiaPaga(guiaEmissaoDt.getNumeroGuiaCompleto()) ) {
					
					obFabricaConexao.iniciarTransacao();
					
					String valorTotalGuia = guiaEmissaoDt.getValorTotalGuia();
					Integer guiaStatus = GuiaStatusDt.PAGO;		
					//double valorGuiaComplementar = 0d;
					
					if (Funcoes.StringToDouble(valorCustas) > Funcoes.StringToDouble(valorTotalGuia)) {
						guiaStatus = GuiaStatusDt.PAGO_COM_VALOR_SUPERIOR;
					} else if (Funcoes.StringToDouble(valorCustas) < Funcoes.StringToDouble(valorTotalGuia)) {
						guiaStatus = GuiaStatusDt.PAGO_COM_VALOR_INFERIOR;
					}	
					
					if( guiaEmissaoNe.isGuiaCancelada(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao) ) {
						guiaStatus = GuiaStatusDt.CANCELADA_PAGA;
					} else if( guiaEmissaoNe.isGuiaCanceladaPaga(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao) ) {
						guiaStatus = GuiaStatusDt.CANCELADA_PAGA;						
					}
					
					TJDataHora objDataHoraRecebimento = new TJDataHora();
					objDataHoraRecebimento.setDataaaaa_MM_ddHHmmss(dataArrecadacao);
					
					Long longDataPagamento = objDataHoraRecebimento.getDate().getTime();
					Date dataVencimentoGuia = guiaEmissaoNe.consultarDataVencimento(guiaEmissaoDt.getNumeroGuiaCompleto());
					dataVencimentoGuia.setHours( 23 );
					dataVencimentoGuia.setMinutes( 59 );
					dataVencimentoGuia.setSeconds( 59 );
					Long longDataVencimento = dataVencimentoGuia.getTime();
					if( longDataVencimento < longDataPagamento ) {
						guiaStatus = GuiaStatusDt.PAGO_APOS_VENCIMENTO;
					}
					
					guiaEmissaoNe.atualizarPagamento(guiaEmissaoDt.getNumeroGuiaCompleto(), guiaStatus, objDataHoraRecebimento.getDate(), dataMovimento.getDate(), valorCustas, obFabricaConexao);
					
					//Conforme solicitado pelo financeiro a confirmação do repasse será inserida no SAJ somente no momento da confirmação do repasse pela prefeitura..
//					List listaGuiaItemDt = guiaEmissaoNe.consultarGuiaItens(guiaEmissaoDt.getId(), guiaEmissaoDt.getId_GuiaTipo());
//					//Envia Guia paga para Cadastro no SPG/SAJ
//					GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
//					guiaSPGNe.inserirPagamentoGuiaSPGSAJ(guiaEmissaoDt, listaGuiaItemDt, objDataHoraRecebimento, valorTotalGuia, numeroBancoPagamento, numeroAgenciaPagamento);
					
					LogDt logDt = new LogDt("ExecuçãoAutomaticaAtualizarGuiasPagasPrefeitura", guiaEmissaoDt.getId(), UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), "A guia " + guiaEmissaoDt.getNumeroGuiaCompleto() + " foi atualizada com o status " + String.valueOf(guiaStatus) + " .");
					logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));
					logNe.salvar(logDt, obFabricaConexao);
					
					obFabricaConexao.finalizarTransacao();
				}
			}
		}
		catch(Exception e) 
		{
			if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
			LogDt logDt = new LogDt("ExecuçãoAutomaticaAtualizarGuiasPagasPrefeitura", "", UsuarioDt.SistemaProjudi, "Servidor", "", "Erro ao Atualizar Guia Paga Prefeitura de Goiânia " +  Funcoes.DataHora(new Date()), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ErroIntegracaoPrefeituraGoiania));
			logNe.salvar(logDt, obFabricaConexao);					
		}		
		finally 
		{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
	}
	
	public void atualizarGuiaPagaPrefeituraTipoMovimentoEstorno(GuiaEmissaoDt guiaEmissaoDt, GuiaEmissaoNe guiaEmissaoNe, LogNe logNe) throws Exception
	{
		FabricaConexao obFabricaConexao = null;
		
		try
		{			
			if(guiaEmissaoNe.isGuiaPaga(guiaEmissaoDt.getNumeroGuiaCompleto()) ) {
				
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
				
				guiaEmissaoNe.estornarPagamentoPrefeitura(guiaEmissaoDt.getNumeroGuiaCompleto(), obFabricaConexao);
				
				//Envia Guia para Baixa no SPG
				GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
				guiaSPGNe.estornarPagamentoDevolucaoCheque(guiaEmissaoDt);
				
				guiaEmissaoNe.gerarGuiaFazendaPublicaAutomatica(guiaEmissaoDt.getProcessoDt(), GuiaTipoDt.ID_PREFEITURA_AUTOMATICA, null, obFabricaConexao);
				
				LogDt logDt = new LogDt("ExecuçãoAutomaticaEstornarGuiasPagasPrefeitura", guiaEmissaoDt.getId(), UsuarioDt.SistemaProjudi, "Servidor", "", Funcoes.DataHora(new Date()), "A guia " + guiaEmissaoDt.getNumeroGuiaCompleto() + " foi estornada.");
				logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));
				logNe.salvar(logDt, obFabricaConexao);
				
				obFabricaConexao.finalizarTransacao();
			}			
		}
		catch(Exception e) 
		{
			if (obFabricaConexao != null) obFabricaConexao.cancelarTransacao();
			LogDt logDt = new LogDt("ExecuçãoAutomaticaAtualizarGuiasPagasPrefeitura", "", UsuarioDt.SistemaProjudi, "Servidor", "", "Erro ao Estornar Guia Paga Prefeitura de Goiânia " +  Funcoes.DataHora(new Date()), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ErroIntegracaoPrefeituraGoiania));
			logNe.salvar(logDt,obFabricaConexao);						
		}		
		finally 
		{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		
	}
	
	/**
	 * gerarGuiasPrefeituraDeGoiania
	 * Execute geração de guias únicas com a prefeitura de Goiânia	 
	 * @throws Exception
	 */
	public void gerarGuiasFazendaPublica() throws Exception {
		TenteGravarLogJobGerarGuiasFazendaPublica("Início Processamento");
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
		LogNe logNe = new LogNe();
		try
		{
			List<ProcessoDt> listaProcessos = new ProcessoNe().consultarProcessosFazendaPublicaSemGuiaUnica(obFabricaConexao);			
			TenteGravarLogJobGerarGuiasFazendaPublica("Quantidade de Processos: " + listaProcessos.size());			
			for(ProcessoDt processo : listaProcessos) {
				tenteGerarGuiasFazendaPublica(processo, guiaEmissaoNe, logNe, obFabricaConexao);		
			}
			TenteGravarLogJobGerarGuiasFazendaPublica("Fim Processamento com sucesso");
		}
		catch(Exception e) 
		{
			TenteGravarLogJobGerarGuiasFazendaPublica("Fim Processamento com erro. Mensagem: " + Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			throw e;
		}
		finally 
		{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();			
		}	
	}
	
	private void tenteGerarGuiasFazendaPublica(ProcessoDt processo, GuiaEmissaoNe guiaEmissaoNe, LogNe logNe, FabricaConexao obFabricaConexao) throws Exception {
		try 
		{
			String tipoGuia = GuiaTipoDt.ID_FAZENDA_PUBLICA_AUTOMATICA;
			if (ProcessoNe.isProcessoDa1aVaraDaFazendaPublicaMunicipalDeGoiania(processo)) {
				tipoGuia = GuiaTipoDt.ID_PREFEITURA_AUTOMATICA;
			}
			
			GuiaEmissaoDt guiaEmissaoDt = null;
			boolean gerarGuia = true;
			
			if( processo.isProcessoImportadoSPG() || processo.isProcessoHibrido() ) {
				gerarGuia = false;
			}
			
			if( gerarGuia ) {
				guiaEmissaoDt = guiaEmissaoNe.gerarGuiaFazendaPublicaAutomatica(processo, String.valueOf(tipoGuia), null, obFabricaConexao);
			}
			
			if (guiaEmissaoDt != null) TenteGravarLogJobGerarGuiasFazendaPublica("Guia gerada com sucesso para o processo " + processo.getProcessoNumero());
			else TenteGravarLogJobGerarGuiasFazendaPublica("Guia não gerada para o processo " + processo.getProcessoNumero() + ", verifique a classe.");
		}
		catch(Exception e) 
		{
			LogDt logDt = new LogDt("ExecuçãoAutomaticaGerarGuiasFazendaPublica", "", UsuarioDt.SistemaProjudi, "Servidor", "", "Erro ao Gerar Guia Única Fazenda Pública - Carga Inicial - " +  Funcoes.DataHora(new Date()), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
			logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.Erro));
			logNe.salvar(logDt,obFabricaConexao);
		}	
	}
	
	private void TenteGravarLogJobGerarGuiasFazendaPublica(String Mensagem) throws Exception{	
		TenteGravarLog("GerarGuiasFazendaPublica", Mensagem);
	}
	
	private void TenteGravarLogEnviarGuiasPendentesPrefeitura(String Mensagem) throws Exception{	
		TenteGravarLog("EnviarGuiasPendentesPrefeitura", Mensagem);
	}
	
	private void TenteGravarLogAtualizarGuiasPagasPrefeitura(String Mensagem) throws Exception{	
		TenteGravarLog("AtualizarGuiasPagasPrefeitura", Mensagem);
	}
	
	private void TenteGravarLogCancelarGuiaUnicaQuandoJaExistirGuiaFazendaMunicipalPaga(String Mensagem) throws Exception{	
		TenteGravarLog("CancelaGuiasPrefeituraFazendaPaga", Mensagem);
	}
	
	private void TenteGravarLogCancelaGuiasPrefeituraCanceladaUsuario(String Mensagem) throws Exception{	
		TenteGravarLog("CancelaGuiasPrefeituraCanceladoUsuario", Mensagem);
	}
	
	private void TenteGravarLog(String Tabela, String Mensagem) throws Exception	{	

		LogDt logDt = new LogDt(Tabela, "", UsuarioDt.SistemaProjudi, "Servidor", "", Mensagem, "");
		logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.ExecucaoAutomaticaPrefeituraGoiania));			
		new LogNe().salvar(logDt);

	}
	
	/**
	 * Método responsável por informar o recebimento de uma guia ao SPG.
	 * @param numeroProcesso - número do Processo ao qual pertence a guia
	 * @param numeroGuia - número da guia
	 * @param matricula - matrícula do usuário que está informando a guia
	 * @param serventiaCodigoExterno - código externo da serventia do processo
	 * @return mensagem
	 * @author hmgodinho
	 */
	public String informarRecebimentoGuiaSPG(String numeroProcesso, String numeroGuia, String matricula, String serventiaCodigoExterno)throws Exception{
		//Desenvolvimento
		//String url = ("http://10.0.15.51/cgi-bin/tjg-guia/fordspgi/pjdi001t?proc="+numeroProcesso+"&guia="+numeroGuia+"&serv="+serventiaCodigoExterno);
		
		//Produção
		String url = "http://" + ProjudiPropriedades.getInstance().getServidorSPG() + "/cgi-bin/tjg-guia/for" + ProjudiPropriedades.getInstance().getServidorSPGTipo() + "spgi/pjdi001t?proc=" +  numeroProcesso + "&guia=" + numeroGuia + "&serv=" + serventiaCodigoExterno + "&mat=" + matricula;
		
		try(DefaultHttpClient httpclient = new DefaultHttpClient()){
			HttpGet httpget = new HttpGet(url);
			HttpResponse response;
			String xml = null;
			
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				xml = EntityUtils.toString(entity);
			}
	
			return xml;
		}
	}
}
