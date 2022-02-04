package br.gov.go.tj.projudi.util;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.AssuntoNe;
import br.gov.go.tj.projudi.ne.BairroNe;
import br.gov.go.tj.projudi.ne.CidadeNe;
import br.gov.go.tj.projudi.ne.EstadoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.RgOrgaoExpedidorNe;
import br.gov.go.tj.projudi.ne.UsuarioServentiaOabNe;
import br.gov.go.tj.utils.Funcoes;

/**
 * Esta classe tem a função de efetuar a importação dos dados de um arquivo
 * 
 */
public class ControleImportacaoDados {

	private ProcessoCadastroDt processoDt;
	private ProcessoParteDt processoParteDt;
	private String Mensagem = "";
	private LogDt logDt;
	private int contadorPromovente;
	private int contadorPromovido;
	private int contadorLinhas;
	private int contadorTestemunha;
	private int contadorComunicante;
	private int contadorAdvogadosPromoventes;
	private int contadorAdvogadosPromovidos;
	private int tipoLeiaute = 1;//Por default começa com código 1 - leiaute da SSP
	

	public ControleImportacaoDados(String conteudoArquivo, LogDt logDt) throws Exception {
		this.logDt = logDt;
		this.lerArquivo(conteudoArquivo);
	}

	/**
	 * Método responsável em ler o arquivo do qual serão importados os dados da ação
	 */
	public void lerArquivo(String conteudo) throws Exception {
		int codigoInstituicao = 0;
		
		boolean registro0 = false;
		boolean registro1 = false;
		boolean registro2 = false;
		boolean registro8 = false;
		boolean registro9 = false;

		if (conteudo != null) {

			processoDt = new ProcessoCadastroDt();
			int aux;
			String[] linha = null; // Vetor que armazena as linhas
			String[] vetor = null; // Vetor que armazena os dados de cada linha
			String[] vetorEndereco = null;
			String[] vetorAdvogado = null;
			List lisAdv = null;

			// Separa por quebra de linha
			linha = conteudo.split("\n");

			contadorLinhas = linha.length;
			
			// Armazena o código da instituição para busca de advogado
			for(int i = 0; i < contadorLinhas; i++) {
				vetor = linha[i].split(";", -1);
				if(vetor[0].equalsIgnoreCase("8")) {
					codigoInstituicao = Funcoes.StringToInt(vetor[8]);
					break;					
				}
			}

			for (int i = 0; i < linha.length; i++) {
				if(linha[i].length() <= 0) continue;
				vetor = linha[i].split(";", -1); // Separa por ; (ponto e vírgula)
				
				if (vetor.length > 1) {
					//Armazena o primeiro dígito da linha, ou seja, o identificador do registro
					aux = Funcoes.StringToInt(vetor[0]);

					switch (aux) {

						case 0: //Captura o tipo do leiaute: 1 - SSP  /  2 - Advogado	/ 3 - Webservice  / 6 - Advogado+Assunto
							if (!vetor[1].equals("")) tipoLeiaute = Funcoes.StringToInt(vetor[1]);
							registro0 = true;
							break;

						case 1: // Informações da Parte Promovente
						    lisAdv = new ArrayList();
							registro1 = true;
							if(linha[i+1] != null) vetorEndereco = linha[++i].split(";", -1); //Próxima linha é endereço
							if(linha[i+1] != null) vetorAdvogado = linha[++i].split(";", -1); //Próxima linha é advogado
                            
							while (vetorAdvogado[0].equals("12")){								
							    lisAdv.add(vetorAdvogado);
							    if(linha[i+1] != null) vetorAdvogado = linha[++i].split(";", -1); //Próxima linha é advogado	                               
							}
							instanciarParteProcesso(vetor, vetorEndereco, lisAdv, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, codigoInstituicao);
							i--;
							break;

						case 2: // Informações da Parte Promovida
						    lisAdv = new ArrayList();
							registro2 = true;
							if(linha[i+1] != null) vetorEndereco = linha[++i].split(";", -1);
							if(linha[i+1] != null) vetorAdvogado = linha[++i].split(";", -1);		
							
							while (vetorAdvogado[0].equals("22")){
							    lisAdv.add(vetorAdvogado);
							    if(linha[i+1] != null) vetorAdvogado = linha[++i].split(";", -1);
					        }
							instanciarParteProcesso(vetor, vetorEndereco, lisAdv, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, codigoInstituicao);
					        i--;
							break;

						case 3: // Informações do comunicante
							if(linha[i+1] != null) vetorEndereco = linha[++i].split(";", -1);
							instanciarParteProcesso(vetor, vetorEndereco, null, ProcessoParteTipoDt.COMUNICANTE, codigoInstituicao);
							break;

						case 4: // Informações da Testemunha
							if(linha[i+1] != null) vetorEndereco = linha[++i].split(";", -1);
							instanciarParteProcesso(vetor, vetorEndereco, null, ProcessoParteTipoDt.TESTEMUNHA, codigoInstituicao);
							break;
						case 5: // Outras partes
							if(linha[i+1] != null) vetorEndereco = linha[++i].split(";", -1);
							instanciarParteProcesso(vetor, vetorEndereco, null, Funcoes.StringToInt(vetor[14],-999), codigoInstituicao);
							break;
						case 8: // Outras informações
							registro8 = true;
							instanciarProcesso(vetor, tipoLeiaute);
							break;

						case 9: // Sumarização
							registro9 = true;
							validaImportacao(vetor);
							break;

						default:
							break;
					} // fim switch
				} else {
					Mensagem += "Existe uma linha inválida no arquivo. Verifique e tente novamente.\n";
				}

			} // fim for

			if (!registro0) Mensagem += "Falta o Registro 0 - Informações do Tipo do Leiaute.";
			if (!registro1) Mensagem += "Falta o Registro 1 - Informações da Parte Promovente.";
			if (!registro2) Mensagem += "Falta o Registro 2 - Informações da Parte Promovida.";
			if (!registro8) Mensagem += "Falta o Registro 8 - Outras informações.";
			if (!registro9) Mensagem += "Falta o Registro 9 - Sumarização.";
		} else {
			Mensagem += "Não há um arquivo válido a ser lido.\n";
		}
	}

	/**
	 * Método responsável por instanciar um Processo e setar seus dados
	 * @throws Exception 
	 */

	private void instanciarProcesso(String[] vetor, int tipoLeiaute) throws Exception{
		String[] informacao = new String[20];
		ProcessoNe processoNe = new ProcessoNe();
		
		for (int i = 0; i < vetor.length; i++) {
			informacao[i] = "";
			if(!vetor[i].equalsIgnoreCase("\r") && !vetor[i].equalsIgnoreCase("\n")) 
				informacao[i] = vetor[i];
		}
		
		switch (tipoLeiaute) {
		case 1: //[1]Código da Área Distribuição [2]Código da Ação [3]Valor da Ação [4]Maior 60 [5]Segredo de Justiça [6]Pedido de Urgência [7]Código da Delegacia [8]Código da Instituição [9]Número do TCO [10]Data do Fato [11]Sigiloso [12]Código da Prioridade [13]Processo Principal [14]Assunto(s) [15]Marcar Audiencia
			processoDt.setProcessoDependente(false);
			processoDt.setAreaDistribuicaoCodigo(informacao[1]);
			if (informacao[2] != null && informacao[2].equalsIgnoreCase("424"))	// 424-TERMO CIRCUNSTANCIADO DE OCORRENCIA   
				informacao[2] = "1278";											//1278-TERMO CIRCUNSTANCIADO(Lei Esparsa)
			processoDt.setProcessoTipoCodigo(informacao[2]);
			if (!informacao[3].equals("")) {
				informacao[3] = informacao[3].replace(",", "");
				processoDt.setValor(informacao[3]);
			}
			processoDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.NORMAL));
			if (informacao[4] != null && informacao[4].equalsIgnoreCase("S")) 	processoDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.MAIOR_60_ANOS));
			if (informacao[5] != null && informacao[5].equalsIgnoreCase("S")) 	processoDt.setSegredoJustica("true");
			if (informacao[6] != null && informacao[6].equalsIgnoreCase("S")) 	processoDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.LIMINAR));
			if (informacao[7] != null && !informacao[7].equalsIgnoreCase("")) 	processoDt.setServentiaOrigemCodigo(informacao[7]);
			if (informacao[8] != null && !informacao[8].equalsIgnoreCase("")) 	processoDt.setCodigoInstituicao(informacao[8]);
			if (informacao[9] != null && !informacao[9].equalsIgnoreCase("")) 	processoDt.setTcoNumero(informacao[9]);
			if (informacao[10] != null && !informacao[10].equalsIgnoreCase("")) processoDt.setDataFato(informacao[10]);
			if (informacao[11] != null && informacao[11].equalsIgnoreCase("S")) processoDt.setSigiloso("true");
			if (informacao[12] != null && !informacao[12].equalsIgnoreCase("") && ProcessoPrioridadeDt.isCodigoPrioridade(Funcoes.StringToInt(informacao[12]))) {
				processoDt.setProcessoPrioridadeCodigo(informacao[12]);
				if(Funcoes.StringToInt(informacao[12]) == ProcessoPrioridadeDt.REU_PRESO)
					processoDt.getProcessoCriminalDt().setReuPreso("true");
			}
			//Processo Dependente
			if (informacao[13] != null && !informacao[13].equalsIgnoreCase("")) {
				AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
				if(informacao[1] != null && !informacao[1].equalsIgnoreCase("")) {
					AreaDistribuicaoDt areaDistribuicaoDt = areaDistribuicaoNe.consultarAreaDistribuicaoCodigo(informacao[1]);
					if(areaDistribuicaoDt != null) {
						if(Funcoes.StringToInt(areaDistribuicaoDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.CAMARA_CRIMINAL || Funcoes.StringToInt(areaDistribuicaoDt.getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.SECAO_CRIMINAL) {
							processoDt.setGrauProcesso("2");
						} else {
							processoDt.setGrauProcesso("1");
						}
					}
				}
				if(processoDt.getCodigoInstituicao().equalsIgnoreCase("1")) processoDt.setTipoProcesso("2");
				String processoNumeroVinculo = informacao[13];
				ProcessoDt processoVinculoDt = processoNe.consultarProcessoPrincipal(processoNumeroVinculo,null);						
				if (processoVinculoDt != null && processoVinculoDt.getId().length() > 0) {
					processoDt.setProcessoNumeroPrincipal(processoNumeroVinculo);
					processoDt.setProcessoDependenteDt(processoVinculoDt);
					processoDt.setId_ProcessoPrincipal(processoVinculoDt.getId());
					processoDt.setProcessoDependente(true);
					if (processoDt.isMesmoGrauJurisdicao() && processoDt.isProcessoMesmaArea()){
						processoDt.setAreaDistribuicao(processoVinculoDt.getAreaDistribuicao());
						processoDt.setId_AreaDistribuicao(processoVinculoDt.getId_AreaDistribuicao());
						processoDt.setForumCodigo(processoVinculoDt.getForumCodigo());
						getAssuntosProcessoOriginario(processoDt, processoVinculoDt.getId(), processoNe);
					}
				} else {
					Mensagem+= "Não foi possível localizar o Processo informado.";
				}
			}
			if (informacao[14] != null && !informacao[14].equalsIgnoreCase("")) {
				String[] assuntos = informacao[14].split("@");
				for (int i = 0; i < assuntos.length; i++) {
					AssuntoNe assuntoNe = new AssuntoNe();
					AssuntoDt assuntoDt = assuntoNe.consultarId(assuntos[i]);
					ProcessoAssuntoDt processoAssuntoDt = new ProcessoAssuntoDt();
					processoAssuntoDt.setId_Assunto(assuntoDt.getId());
					processoAssuntoDt.setAssunto(assuntoDt.getAssunto());
					if (processoAssuntoDt != null)
						processoDt.addListaAssuntos(processoAssuntoDt);
				}
			}
			if (informacao[15] != null && informacao[15].equalsIgnoreCase("N")) 	processoDt.setMarcarAudiencia("false");			
			
			break;
		case 2: //[1]Código da Área Distribuição [2]Código da Ação [3]Valor da Ação [4]Maior 60 [5]Segredo de Justiça [6]Pedido de Urgência [7]Código da Delegacia [8]Código da Instituição [9]Número do TCO
			processoDt.setProcessoDependente(false);
			processoDt.setAreaDistribuicaoCodigo(informacao[1]);
			if(informacao[2] != null && informacao[2].equalsIgnoreCase("424"))	// 424-TERMO CIRCUNSTANCIADO DE OCORRENCIA   
				informacao[2] = "1278";											//1278-TERMO CIRCUNSTANCIADO(Lei Esparsa)
			processoDt.setProcessoTipoCodigo(informacao[2]);
			if (!informacao[3].equals("")) {
				informacao[3] = informacao[3].replace(",", "");
				processoDt.setValor(informacao[3]);
			}
			processoDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.NORMAL));
			if (informacao[4] != null && informacao[4].equalsIgnoreCase("S")) 	processoDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.MAIOR_60_ANOS));
			if (informacao[5] != null && informacao[5].equalsIgnoreCase("S")) 	processoDt.setSegredoJustica("true");
			if (informacao[6] != null && informacao[6].equalsIgnoreCase("S")) 	processoDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.LIMINAR));
			if (informacao[7] != null && !informacao[7].equalsIgnoreCase("")) 	processoDt.setServentiaOrigemCodigo(informacao[7]);
			if (informacao[8] != null && !informacao[8].equalsIgnoreCase("")) 	processoDt.setCodigoInstituicao(informacao[8]);
			if (informacao[9] != null && !informacao[9].equalsIgnoreCase("")) 	processoDt.setTcoNumero(informacao[9]);

			break;
		case 6: //[1]Código da Área Distribuição [2]Código da Ação [3]Valor da Ação [4]Maior 60 [5]Segredo de Justiça [6]Pedido de Urgência [7]Código da Delegacia [8]Código da Instituição [9]Número do TCO [10]Id Assunto@Id Assunto [11]Sigiloso [12]Código da Prioridade [13]Data do Fato [14]Processo Principal
			processoDt.setProcessoDependente(false);
			processoDt.setAreaDistribuicaoCodigo(informacao[1]);
			if(informacao[2] != null && informacao[2].equalsIgnoreCase("424"))	// 424-TERMO CIRCUNSTANCIADO DE OCORRENCIA   
				informacao[2] = "1278";											//1278-TERMO CIRCUNSTANCIADO(Lei Esparsa)
			processoDt.setProcessoTipoCodigo(informacao[2]);
			if (!informacao[3].equals("")) {
				informacao[3] = informacao[3].replace(",", "");
				processoDt.setValor(informacao[3]);
			}
			processoDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.NORMAL));
			if (informacao[4] != null && informacao[4].equalsIgnoreCase("S")) 	processoDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.MAIOR_60_ANOS));
			if (informacao[5] != null && informacao[5].equalsIgnoreCase("S")) 	processoDt.setSegredoJustica("true");
			if (informacao[6] != null && informacao[6].equalsIgnoreCase("S")) 	processoDt.setProcessoPrioridadeCodigo(String.valueOf(ProcessoPrioridadeDt.LIMINAR));
			if (informacao[7] != null && !informacao[7].equalsIgnoreCase("")) 	processoDt.setServentiaOrigemCodigo(informacao[7]);
			if (informacao[8] != null && !informacao[8].equalsIgnoreCase("")) 	processoDt.setCodigoInstituicao(informacao[8]);
			if (informacao[9] != null && !informacao[9].equalsIgnoreCase("")) 	processoDt.setTcoNumero(informacao[9]);
			if (informacao[10] != null && !informacao[10].equalsIgnoreCase("")) {
				String[] assuntos = informacao[10].split("@");
				for (int i = 0; i < assuntos.length; i++) {
					AssuntoNe assuntoNe = new AssuntoNe();
					AssuntoDt assuntoDt = assuntoNe.consultarId(assuntos[i]);
					ProcessoAssuntoDt processoAssuntoDt = new ProcessoAssuntoDt();
					processoAssuntoDt.setId_Assunto(assuntoDt.getId());
					processoAssuntoDt.setAssunto(assuntoDt.getAssunto());
					if (processoAssuntoDt != null)
						processoDt.addListaAssuntos(processoAssuntoDt);
				}
			}
			if (informacao[11] != null && informacao[11].equalsIgnoreCase("S")) processoDt.setSigiloso("true");
			if (informacao[12] != null && !informacao[12].equalsIgnoreCase("") && ProcessoPrioridadeDt.isCodigoPrioridade(Funcoes.StringToInt(informacao[12]))) {
				processoDt.setProcessoPrioridadeCodigo(informacao[12]);
			}
			if (informacao[13] != null && informacao[13].equalsIgnoreCase("")) processoDt.setDataFato(informacao[13]);
			//Processo Principal
			if (informacao[14] != null && !informacao[14].equalsIgnoreCase("")) {
				AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
				if(informacao[1] != null && !informacao[1].equalsIgnoreCase("")) {
					AreaDistribuicaoDt areaDistribuicaoDt = areaDistribuicaoNe.consultarAreaDistribuicaoCodigo(informacao[1]);
					if(areaDistribuicaoDt != null) {
						if(areaDistribuicaoDt.isSegundoGrau()) {
							processoDt.setGrauProcesso("2");
						} else {
							processoDt.setGrauProcesso("1");
						}
						processoDt.setTipoProcesso(areaDistribuicaoDt.getAreaDistribuicaoCodigo());
					}
				}
				String processoNumeroVinculo = informacao[14];
				ProcessoDt processoVinculoDt = processoNe.consultarProcessoPrincipal(processoNumeroVinculo,null);						
				if (processoVinculoDt != null && processoVinculoDt.getId().length() > 0) {
					processoDt.setProcessoNumeroPrincipal(processoNumeroVinculo);
					processoDt.setProcessoDependenteDt(processoVinculoDt);
					processoDt.setId_ProcessoPrincipal(processoVinculoDt.getId());
					processoDt.setProcessoDependente(true);
					if (processoDt.isMesmoGrauJurisdicao() && processoDt.isProcessoMesmaArea()){
						processoDt.setAreaDistribuicao(processoVinculoDt.getAreaDistribuicao());
						processoDt.setId_AreaDistribuicao(processoVinculoDt.getId_AreaDistribuicao());
						processoDt.setForumCodigo(processoVinculoDt.getForumCodigo());
						getAssuntosProcessoOriginario(processoDt, processoVinculoDt.getId(), processoNe);
					}
				} else {
					Mensagem+= "Não foi possível localizar o Processo informado.";
				}
			}
			break;
		}
		Mensagem += processoNe.verificarProcessoImportacao(processoDt, tipoLeiaute);
	}

	/**
	 * Método responsável por instanciar uma Parte de Processo e setar seus respectivos dados
	 * @throws Exception 
	 */
	private void instanciarParteProcesso(String[] vetor, String[] vetorEndereco, List lisAdvogado, int tipo, int codigoInstituicao) throws Exception{

		setDadosProcessoParte(vetor, tipo);

		switch (tipo) {
			case ProcessoParteTipoDt.POLO_ATIVO_CODIGO:
				contadorPromovente++;

				if(!processoParteDt.isParteEnderecoDesconhecido()) {
					if (vetorEndereco[0].equals("11")) {
						instanciarEndereco(vetorEndereco, processoParteDt.getNome());
					} else Mensagem += "Falta o Registro 11 - Endereço da Parte Promovente: " + processoParteDt.getNome();
				}
				for (int i=0; i<lisAdvogado.size();i++){
				    String[] vetorAdvogado = (String[])lisAdvogado.get(i);				
				    if (vetorAdvogado[0].equals("12")) {
				        contadorAdvogadosPromoventes++;
				        instanciarAdvogado(vetorAdvogado, tipo, codigoInstituicao);
				    }
				}
				break;

			case ProcessoParteTipoDt.POLO_PASSIVO_CODIGO:
				contadorPromovido++;

				if(!processoParteDt.isParteEnderecoDesconhecido()) {
					if (vetorEndereco[0].equals("21")) {
						instanciarEndereco(vetorEndereco, processoParteDt.getNome());
					} else Mensagem += "Falta o Registro 21 - Endereço da Parte Promovida: " + processoParteDt.getNome();
				}
				for (int i=0; i<lisAdvogado.size();i++){
                    String[] vetorAdvogado = (String[])lisAdvogado.get(i);
                    if (vetorAdvogado[0].equals("22")) {
                        contadorAdvogadosPromovidos++;
					    instanciarAdvogado(vetorAdvogado, tipo, codigoInstituicao);
                    }
				}
				break;

			case ProcessoParteTipoDt.COMUNICANTE:
				contadorComunicante++;
				if(!processoParteDt.isParteEnderecoDesconhecido()) {
					if (vetorEndereco[0].equals("31")) {
						instanciarEndereco(vetorEndereco, processoParteDt.getNome());
					} else Mensagem += "Falta o Registro 31 - Endereço do Comunicante:" + processoParteDt.getNome();
				}
				break;

			case ProcessoParteTipoDt.TESTEMUNHA:
				contadorTestemunha++;
				if(!processoParteDt.isParteEnderecoDesconhecido()) {
					if (vetorEndereco[0].equals("41")) {
						instanciarEndereco(vetorEndereco, processoParteDt.getNome());
					} else Mensagem += "Falta o Registro 41 - Endereço da Testemunha:" + processoParteDt.getNome();
				}
				break;
			case ProcessoParteTipoDt.VITIMA:
			case ProcessoParteTipoDt.CURATELADO:
			case ProcessoParteTipoDt.INTERESSADO:
				if(!processoParteDt.isParteEnderecoDesconhecido()) {
					if (vetorEndereco[0].equals("51")) {
						instanciarEndereco(vetorEndereco, processoParteDt.getNome());
					} else Mensagem += "Falta o Registro 51 - Endereço da Parte:" + processoParteDt.getNome();
				}
				break;
			default:
				 Mensagem += "Não foi possível definir o Tipo da Parte";
				 break;
		}

		//Valida dados da parte e endereço e adiciona ao processo
		adicionaParteProcesso(tipo);

	}

	/**
	 * Seta os dados de uma parte baseado nos dados do arquivo passados
	 * @throws Exception 
	 */
	private void setDadosProcessoParte(String[] vetor, int tipo) throws Exception{

		processoParteDt = new ProcessoParteDt();

		switch (tipo) {
			case ProcessoParteTipoDt.POLO_ATIVO_CODIGO:			
			case ProcessoParteTipoDt.POLO_PASSIVO_CODIGO:
			case ProcessoParteTipoDt.COMUNICANTE:
			case ProcessoParteTipoDt.TESTEMUNHA:
			case ProcessoParteTipoDt.VITIMA:
			case ProcessoParteTipoDt.CURATELADO:
			case ProcessoParteTipoDt.INTERESSADO:
				processoParteDt.setProcessoParteTipoCodigo(String.valueOf(tipo));
				break;
			default:
				 Mensagem += "Não foi possível definir o Tipo da Parte";
				 break;
		}
		
		if (vetor[1].length() == 14) processoParteDt.setCnpj(vetor[1]);
		else processoParteDt.setCpf(vetor[1]);
		processoParteDt.setNome(vetor[2]);
		if(vetor[2].equalsIgnoreCase("A ESCLARECER")) {
			processoParteDt.setParteEnderecoDesconhecido("true");
			processoParteDt.setParteNaoPersonificavel(true);
		}
		processoParteDt.setRg(vetor[3]);

		//Busca Orgao Expedidor
		RgOrgaoExpedidorDt orgaoExpedidorDt = new RgOrgaoExpedidorNe().buscaOrgaoExpedidor(vetor[4], vetor[5]);
		if (orgaoExpedidorDt != null) processoParteDt.setId_RgOrgaoExpedidor(orgaoExpedidorDt.getId());
		else processoParteDt.setId_RgOrgaoExpedidor("");

		processoParteDt.setDataNascimento(vetor[6]);
		processoParteDt.setCtps(vetor[7]);

		//Busca Estado CTPS
		EstadoDt estadoDt = new EstadoNe().buscaEstado(vetor[8]);
		if (estadoDt != null) processoParteDt.setId_CtpsUf(estadoDt.getId());
		else processoParteDt.setId_CtpsUf("");

		processoParteDt.setCtpsSerie(vetor[9]);
		processoParteDt.setPis(vetor[10]);
		processoParteDt.setTituloEleitor(vetor[11]);

		//De acordo com tipo de Leiaute seta dados para parte
		switch (tipoLeiaute) {
			case 1: //Leiaute SSP
			case 2: //Leiaute Advogado - [1]cpf/cnpj [2]nome [3]rg [4]orgaoExpedidorRg [5]ufRg [6]dataNascimento [7]ctps [8]ufCtps [9]serieCtps [10]pis [11]tituloEleitor [12]nomeMae [13]sexo
			case 6: //Leiaute Advogado - [1]cpf/cnpj [2]nome [3]rg [4]orgaoExpedidorRg [5]ufRg [6]dataNascimento [7]ctps [8]ufCtps [9]serieCtps [10]pis [11]tituloEleitor [12]nomeMae [13]sexo
				if(vetor.length > 14) {
					processoParteDt.setNomeMae(vetor[12]);
					processoParteDt.setSexo(vetor[13]);
				} else Mensagem += "Faltam informações da Parte:" + processoParteDt.getNome()+"\n";
				break;

			case 3: //Leiaute Websevice - [1]cpf/cnpj [2]nome [3]rg [4]orgaoExpedidorRG [5]ufRg [6]dataNascimento [7]ctps [8]ufCtps [9]serieCtps [10]pis [11]tituloEleitor [12]tituloEleitorZona [13]tituloEleitorSecao [14]nomeMae [15]sexo [16]naturalidade [17]estadoCivil [18]telefone [19]celular [20]email
				if(vetor.length > 21) {
					processoParteDt.setTituloEleitorZona(vetor[12]);
					processoParteDt.setTituloEleitorSecao(vetor[13]);
					processoParteDt.setNomeMae(vetor[14]);
					processoParteDt.setSexo(vetor[15]);
					
					//Busca Naturalidade
					CidadeDt cidadeDt = new CidadeNe().buscaCidade(vetor[16], "");
					if (cidadeDt != null) processoParteDt.setId_Naturalidade(cidadeDt.getId());
					else processoParteDt.setId_Naturalidade("");
					
					processoParteDt.setId_EstadoCivil(vetor[17]);
					processoParteDt.setTelefone(vetor[18]);
					processoParteDt.setCelular(vetor[19]);
					processoParteDt.setEMail(vetor[20]);
				} else Mensagem += "Faltam informações da Parte:" + processoParteDt.getNome()+"\n";
				break;
		}

		processoParteDt.setId_UsuarioLog(logDt.getId_Usuario());
		processoParteDt.setIpComputadorLog(logDt.getIpComputador());

	}

	/**
	 * Valida dados de uma parte e se estiver com todos dados corretos, adiciona ao processo
	 * @throws Exception 
	 * 
	 */
	private void adicionaParteProcesso(int tipo) throws Exception {
		Mensagem += validarDadosParteProcesso(processoParteDt);
		if (Mensagem.length() == 0) {

			switch (tipo) {
				case ProcessoParteTipoDt.POLO_PASSIVO_CODIGO:
					processoDt.addListaPolosPassivos(processoParteDt);
					break;
				case ProcessoParteTipoDt.POLO_ATIVO_CODIGO:
					processoDt.addListaPoloAtivo(processoParteDt);
					break;
				case ProcessoParteTipoDt.TESTEMUNHA:
					processoDt.addListaTestemunhas(processoParteDt);
					break;
				case ProcessoParteTipoDt.COMUNICANTE:
					processoDt.addListaComunicantes(processoParteDt);
					break;
				case ProcessoParteTipoDt.VITIMA:
				case ProcessoParteTipoDt.CURATELADO:
				case ProcessoParteTipoDt.INTERESSADO:
					processoDt.addListaOutrasPartes(processoParteDt);
					break;
			}

		}
	}
	
	private String validarDadosParteProcesso(ProcessoParteDt processoParteDt) throws Exception{
		String mensagem = "";
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		
		//De acordo com tipo de Leiaute valida dados da parte e endereço
		switch (tipoLeiaute) {
			case 1: //Leiaute SSP
			case 2: //Leiaute Advogado
			case 6: //Leiaute Advogado
			case 7: //Leiaute SSP
				mensagem = processoParteNe.VerificarParteCriminal(processoParteDt);
				break;
			
			case 3: //Leiaute Webservice
				mensagem = processoParteNe.Verificar(processoParteDt);
				break;
		}
		
		if (mensagem.length() > 0){
			mensagem = " Erro nos Dados da Parte " + processoParteDt.getNome() + ": " + mensagem;
		}
		
		return mensagem;
	}

	/**
	 * Método responsável por instanciar um endereço de uma Parte e setar seus respectivos dados
	 * @throws Exception 
	 */
	private void instanciarEndereco(String[] vetor, String nomeParte) throws Exception{
		EnderecoDt enderecoDt = new EnderecoDt();
		enderecoDt.setLogradouro(vetor[1]);
		enderecoDt.setNumero(vetor[2]);
		enderecoDt.setComplemento(vetor[3]);
		enderecoDt.setId_UsuarioLog(logDt.getId_Usuario());
		enderecoDt.setIpComputadorLog(logDt.getIpComputador());

		switch (tipoLeiaute) {
			case 1: //Leiaute SSP
			case 7: //Leiaute SSP
			case 2: //Leiaute Advogado - [1]logradouro [2]numero [3]complemento [4]bairro [5]telefone [6]codigoCidade [7]descricaoCidade [8]estado [9]cep [10]email
				if(vetor.length >= 11) {
					if (!vetor[4].equals("") && !vetor[7].equals("") && !vetor[8].equals("")) {
						BairroNe bairroNe = new BairroNe();
						BairroDt bairroDt = bairroNe.consultarBairro(vetor[4], vetor[7], vetor[8]);
						if (bairroDt != null) {
							enderecoDt.setId_Bairro(bairroDt.getId());
							enderecoDt.setBairro(bairroDt.getBairro());
						} else {
							//Se não encontrar o bairro passado seta como padrão Bairro Central de Goiania
							enderecoDt.setId_Bairro("6");
						}
					} else Mensagem += "Faltam informações do Endereço da Parte (Bairro, Cidade e Estado):" + processoParteDt.getNome() +"\n";
					
					enderecoDt.setCep(vetor[9]);
					processoParteDt.setTelefone(vetor[5]);
					processoParteDt.setEMail(vetor[10]);
				} else Mensagem += "Falta(m) " +(12-vetor.length) + " campo(s) nas informações do Endereço da Parte:" + processoParteDt.getNome() + "\n";
				break;
			
			case 3: //Leiaute Webservice - [1]logradouro [2]numero [3]complemento [4]bairro [5]codigoCidade [6]descricaoCidade [7]uf [8]cep
				if (vetor.length > 9) {
					if (!vetor[4].equals("") && !vetor[6].equals("") && !vetor[7].equals("")) {
						BairroNe bairroNe = new BairroNe();
						BairroDt bairroDt = bairroNe.consultarBairro(vetor[4], vetor[6], vetor[7]);
						if (bairroDt != null) {
							enderecoDt.setId_Bairro(bairroDt.getId());
							enderecoDt.setBairro(bairroDt.getBairro());
						} else {
							//Se não encontrar o bairro passado seta como padrão Bairro Central de Goiania
							enderecoDt.setId_Bairro("6");
						}
					} else Mensagem += "Faltam informações do Endereço da Parte (Bairro, Cidade e Estado):" + processoParteDt.getNome() +"\n";
					enderecoDt.setCep(vetor[8]);
				} else Mensagem += "Falta(m) " +(10-vetor.length) + " campo(s) informações do Endereço da Parte:" + processoParteDt.getNome()+"\n";
				break;
				
			case 5: //Leiaute Advogado - [1]logradouro [2]numero [3]complemento [4]idbairro [5]telefone [6]cep [7]email
				if(vetor.length > 8) {
					if (!vetor[4].equals("")) {
						BairroNe bairroNe = new BairroNe();
						BairroDt bairroDt = bairroNe.consultarId(vetor[4]);
						if (bairroDt != null) {
							enderecoDt.setId_Bairro(bairroDt.getId());
							enderecoDt.setBairro(bairroDt.getBairro());
							enderecoDt.setId_Cidade(bairroDt.getId_Cidade());
							enderecoDt.setCidade(bairroDt.getCidade());
							enderecoDt.setUf(bairroDt.getUf());
						} else Mensagem += "Bairro não encontrado:" + processoParteDt.getNome() +"\n";
					} else Mensagem += "Faltam informações do Endereço da Parte (Id do Bairro):" + processoParteDt.getNome() +"\n";
					
					enderecoDt.setCep(vetor[8]);
					processoParteDt.setTelefone(vetor[5]);
					processoParteDt.setEMail(vetor[7]);
				} else Mensagem += "Falta(m) " +(9-vetor.length) + " campo(s) nas informações do Endereço da Parte:" + processoParteDt.getNome() + "\n";
				break;
			case 6: //Leiaute Advogado - [1]logradouro [2]numero [3]complemento [4]id_bairro [5]telefone [6]cep [7]email
				if(vetor.length > 8) {
					if (!vetor[4].equals("")) {
						BairroNe bairroNe = new BairroNe();
						BairroDt bairroDt = bairroNe.consultarId(vetor[4]);
						if (bairroDt != null) {
							enderecoDt.setId_Bairro(bairroDt.getId());
							enderecoDt.setBairro(bairroDt.getBairro());
							enderecoDt.setId_Cidade(bairroDt.getId_Cidade());
							enderecoDt.setCidade(bairroDt.getCidade());
							enderecoDt.setUf(bairroDt.getUf());
						} else {
							//Se não encontrar o bairro passado define como padrão Bairro Central de Goiania
							enderecoDt.setId_Bairro("6");
						}
					} else Mensagem += "Faltam informações do Endereço da Parte (Id Bairro):" + processoParteDt.getNome() +"\n";
					
					enderecoDt.setCep(vetor[6]);
					processoParteDt.setTelefone(vetor[5]);
					processoParteDt.setEMail(vetor[7]);
				} else Mensagem += "Falta(m) " +(12-vetor.length) + " campo(s) nas informações do Endereço da Parte:" + processoParteDt.getNome() + "\n";
				break;
		}

		processoParteDt.setEnderecoParte(enderecoDt);

	}

	/**
	 * Método que valida a importação de acordo com a linha de Sumarização
	 * existente no arquivo
	 * 
	 * @param vetor:
	 *            [1]quantidadePromoventes, 
	 *            [2]quantidadePromovidos,
	 *            [3]quantidadeAdvogadosPromoventes
	 *            [4]quantidadeAdvogadosPromovidos, 
	 *            [5]quantidadeComunicantes,
	 *            [6]quantidadeTestemunhas e 
	 *            [7]quantidadeLinhas
	 */
	private void validaImportacao(String[] vetor) {

		if ((vetor.length == 8) || (vetor.length == 9)) {
			if (!vetor[1].equals("")) {
				if (contadorPromovente != Funcoes.StringToInt(vetor[1])) Mensagem += "De acordo com a sumarização, quantidade de promoventes está incorreta. \n";
			} else Mensagem += "Arquivo inválido: falta a quantidade de promoventes na sumarização. \n";

			if (!vetor[2].equals("")) {
				if (contadorPromovido != Funcoes.StringToInt(vetor[2])) Mensagem += "De acordo com a sumarização, quantidade de promovidos está incorreta. \n";
			} else Mensagem += "Arquivo inválido: falta a quantidade de promovidos na sumarização. \n";

			if (!vetor[3].equals("")) {
				if (contadorAdvogadosPromoventes != Funcoes.StringToInt(vetor[3])) Mensagem += "De acordo com a sumarização, quantidade de advogados de promoventes está incorreta. \n";
			} else Mensagem += "Arquivo inválido: falta a quantidade de advogados de promoventes na sumarização. \n";

			if (!vetor[4].equals("")) {
				if (contadorAdvogadosPromovidos != Funcoes.StringToInt(vetor[4])) Mensagem += "De acordo com a sumarização, quantidade de advogados de promovidos está incorreta. \n";
			} else Mensagem += "Arquivo inválido: falta a quantidade de advogados de promovidos na sumarização. \n";

			if (!vetor[5].equals("")) {
				if (contadorComunicante != Funcoes.StringToInt(vetor[5])) Mensagem += "De acordo com a sumarização, quantidade de comunicantes está incorreta. \n";
			} else Mensagem += "Arquivo inválido: falta a quantidade de comunicantes na sumarização. \n";

			if (!vetor[6].equals("")) {
				if (contadorTestemunha != Funcoes.StringToInt(vetor[6])) Mensagem += "De acordo com a sumarização, quantidade de testemunhas está incorreta. \n";
			} else Mensagem += "Arquivo inválido: falta a quantidade de testemunhas na sumarização. \n";

			if (!vetor[7].equals("")) {
				if (contadorLinhas != Funcoes.StringToInt(vetor[7])) Mensagem += "De acordo com a sumarização, quantidade de linhas está incorreta. \n";

			} else Mensagem += "Arquivo inválido: falta a quantidade de linhas na sumarização. \n";

		} else Mensagem += "Registro 9(Sumarização) está incompleto. Verifique os campos obrigatórios. \n";

	}

	/**
	 * Método responsável por instanciar um Advogado
	 * @param tipo 
	 * 
	 * @param: vetor [1] NumeroOAB, [2] estadoOAB, [3] complementoOAB
	 * @throws Exception 
	 */
	private void instanciarAdvogado(String[] vetor, int tipo, int codigoInstituicao) throws Exception{

		UsuarioServentiaOabNe advogadoNe = new UsuarioServentiaOabNe();
		String id_UsuarioServentia = advogadoNe.consultarAdvogado(vetor[1], vetor[3], vetor[2], codigoInstituicao);
		if (id_UsuarioServentia == null) {
			Mensagem += "Não foi possível obter Advogado da parte:" + processoParteDt.getNome()+"\n";
		} else {
			ProcessoParteAdvogadoDt advogadoDt = new ProcessoParteAdvogadoDt();
			advogadoDt.setId_UsuarioServentiaAdvogado(id_UsuarioServentia);
			advogadoDt.setOabNumero(vetor[1]);
			advogadoDt.setEstadoOabUf(vetor[2]);
			advogadoDt.setOabComplemento(vetor[3]);
			processoParteDt.setAdvogadoDt(advogadoDt);
		}
	}

	/**
	 * Método responsável por instanciar um Advogado
	 * 
	 * @param: vetor [1] NumeroOAB, [2] estadoOAB, [3] complementoOAB
	 */
	/*private void instanciarAdvogado(String[] vetor) {
		ValidaAdvogado validaAdvogado = new ValidaAdvogado();
		erros.addAll(validaAdvogado.validaCampos(vetor[1], vetor[2], vetor[3]));

		// Instancia um advogado
		if (erros.size() == 0) {
			if (parteProcesso.getEhParteAutora()) {
				processo.adicionaAdvogadoPromovente(Funcoes.StringToInt(vetor[1]), vetor[2], vetor[3], parte.getNome());
			} else if (parteProcesso.getEhParteRe()) {
				processo.adicionaAdvogadoPromovido(Funcoes.StringToInt(vetor[1]), vetor[2], vetor[3], parte.getNome());
			}
		}
	}*/

	public ProcessoCadastroDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoCadastroDt processoDt) {
		this.processoDt = processoDt;
	}

	public String getMensagem() {
		return Mensagem;
	}

	public void setMensagem(String mensagem) {
		Mensagem = mensagem;
	}
	
	protected void getAssuntosProcessoOriginario(ProcessoCadastroDt processoDt, String id_Processo1Grau, ProcessoNe processoNe) throws Exception{

		List assuntosProcesso = processoNe.getAssuntosProcesso(id_Processo1Grau);

		for (int i = 0; i < assuntosProcesso.size(); i++) {
			ProcessoAssuntoDt processoAssuntoDt = (ProcessoAssuntoDt) assuntosProcesso.get(i);
			ProcessoAssuntoDt objAssunto = new ProcessoAssuntoDt();
			objAssunto.setId_Assunto(processoAssuntoDt.getId_Assunto());
			objAssunto.setAssunto(processoAssuntoDt.getAssunto());
			processoDt.addListaAssuntos(objAssunto);
		}
	}
	
}
