package br.gov.go.tj.projudi.ne;

import java.util.HashMap;
import java.util.List;

import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ExportarDebitoCadinDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaStatusDt;
import br.gov.go.tj.projudi.dt.ImportarRetornoCadinDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PoloSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoDebitoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoCadinDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDebitoFisicoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoSSGDt;
import br.gov.go.tj.projudi.ps.ProcessoParteDebitoCadinPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class ExportarDebitoCadinNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3384773040230626723L;

	protected ExportarDebitoCadinDt obDados;

	public ExportarDebitoCadinNe() {
		obLog = new LogNe(); 
		obDados = new ExportarDebitoCadinDt(); 
	}
	
	public String Verificar(ExportarDebitoCadinDt dados) {
		String stRetorno = "";

		if (dados.getTipoExportacao() == null || dados.getTipoExportacao().length() == 0) {
			stRetorno += "Selecione um tipo de exportação. \n";
		} else if (dados.isReprocessamentoPorLote() && 
				    (dados.getNumeroDoLote() == null || 
				     dados.getNumeroDoLote().trim().length() == 0)) {
			stRetorno += "O número do lote deve ser informado. \n";
		} else if (dados.isReprocessamentoPorData() && 
				   (dados.getDataExportacao() == null || 
				    dados.getDataExportacao().trim().length() == 0)) {
			stRetorno += "A data de exportação deve ser informada. \n";
		} else if (!dados.isTipoTodosNaoProcessados()) {
			ProcessoParteDebitoCadinNe processoParteDebitoCadinNe = new ProcessoParteDebitoCadinNe();
			if (dados.isReprocessamentoPorLote()) {
				try {
					if (!processoParteDebitoCadinNe.lotePossuiRegistros(dados.getNumeroDoLote())) {
						stRetorno += "O número do lote informado não possui registros. \n";
					}
				} catch (Exception e) {
					e.printStackTrace();
					stRetorno += "Ocorreu um erro ao consultar o lote informado. \n" + Funcoes.obtenhaConteudoPrimeiraExcecao(e);
				}
			} else {
				try {
					if (!processoParteDebitoCadinNe.dataGeracaoPossuiRegistros(dados.getDataExportacao())) {
						stRetorno += "A data de geração informada não possui registros. \n";
					}
				} catch (Exception e) {
					e.printStackTrace();
					stRetorno += "Ocorreu um erro ao consultar a data de geração informada. \n" + Funcoes.obtenhaConteudoPrimeiraExcecao(e);
				}
			}
		}
		
		return stRetorno;
	}
	
	public ExportarDebitoCadinDt Exportar(ExportarDebitoCadinDt arquivo) throws Exception {
		List<ProcessoParteDebitoCadinDt> registros = null;
		ProcessoParteDebitoCadinNe processoParteDebitoCadinNe = new ProcessoParteDebitoCadinNe();
		ProcessoParteDebitoNe processoParteDebitoNe = new ProcessoParteDebitoNe();
		ProcessoParteDebitoFisicoNe processoParteDebitoFisicoNe = new ProcessoParteDebitoFisicoNe();
		ProcessoParteNe processoParteNe = new ProcessoParteNe();
		GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
		GuiaSSGNe guiaSSGNe = new GuiaSSGNe();
		GuiaEmissaoNe guiaPJDNe = new GuiaEmissaoNe();
		ProcessoNe processoNe = new ProcessoNe();
		ProcessoSPGNe processoSPGNe = new ProcessoSPGNe();
		ProcessoSSGNe processoSSGNe = new ProcessoSSGNe();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		FabricaConexao obFabricaConexaoAdabas = new FabricaConexao(FabricaConexao.ADABAS);
		FabricaConexao obFabricaConexaoAdabasProcessos = new FabricaConexao(FabricaConexao.ADABASPROCESSOS);
		StringBuilder retornoInconsistencias = new StringBuilder();	
		obFabricaConexao.iniciarTransacao();
		
		try {
			TJDataHora dataGeracao = new TJDataHora();
			boolean atualizaDataGeracao = false;
			if (arquivo.isTipoTodosNaoProcessados()) {				
				long numeroDoLote = processoParteDebitoCadinNe.obtenhaNumeroDoProximoLote(obFabricaConexao);
				arquivo.setNumeroDoLote(String.valueOf(numeroDoLote));
				
				List<ProcessoParteDebitoDt> debitosPJD = processoParteDebitoNe.consultarListaLiberadoCadinAindaNaoEnvido(obFabricaConexao);
				for(ProcessoParteDebitoDt debito : debitosPJD) {
					ProcessoParteDebitoCadinDt dados = new ProcessoParteDebitoCadinDt();
					dados.setFisico(false);
					dados.setNumeroLote(String.valueOf(numeroDoLote));
					dados.setId_ProcessoParteDebito(debito.getId());
					dados.setDataGeracao(dataGeracao.getDataHoraFormatadaaaaa_MM_ddHHmmss());
					dados.setTipoDocumento(debito.getCpfParte() != null && debito.getCpfParte().trim().length() > 11 ? "2" : "1"); //sendo 1-CPF 2-CNPJ
					dados.setNumeroDocumento(debito.getCpfParte());
					dados.setNomePessoa(debito.getNome());
					if (dados.getNomePessoa().length() > 60) {
						dados.setNomePessoa(dados.getNomePessoa().substring(0, 60));
					}
					dados.setNumeroProcesso(debito.getProcessoNumeroCompleto());
					dados.setNaturezaPendencia("02"); //01-TRIBUTÁRIA 02-NÃO TRIBUTÁRIA
					dados.setCategoriaPendencia("08"); //08 Não tributário, confirmar outros valores com Administrado do Sistema
					dados.setDataVencimentoDebito(debito.getDataVencimentoGuiaEmissao());
					dados.setValorDevido(debito.getValorTotalGuia());										
					dados.setMeioEnvioComunicado("02"); //sendo 01-DTE 02-EDITAL 03-CARTA
					
					ProcessoParteDt processoParteDt = processoParteNe.consultarId(debito.getId_ProcessoParte(), obFabricaConexao);
					if (processoParteDt != null && processoParteDt.getEnderecoParte() != null) {
						AtualizeTipoDeLogradouroELogradouro(dados, processoParteDt.getEnderecoParte().getLogradouro());
	                    if (dados.getNomeLogradouro().length() > 60) {
							dados.setNomeLogradouro(dados.getNomeLogradouro().substring(0, 60));
						}
						dados.setNumeroEndereco(processoParteDt.getEnderecoParte().getNumero());
						if (dados.getNumeroEndereco().length() > 10) {
							dados.setNumeroEndereco(dados.getNumeroEndereco().substring(0, 10));
						}
						dados.setDescricaoComplementoEndereco(processoParteDt.getEnderecoParte().getComplemento() + " " + processoParteDt.getEnderecoParte().getCidade() + " " + processoParteDt.getEnderecoParte().getUf());
						if (dados.getDescricaoComplementoEndereco().length() > 80) {
							dados.setDescricaoComplementoEndereco(dados.getDescricaoComplementoEndereco().substring(0, 80));
						}
						dados.setNomeBairro(processoParteDt.getEnderecoParte().getBairro());
						if (dados.getNomeBairro().length() > 60) {
							dados.setNomeBairro(dados.getNomeBairro().substring(0, 60));
						}
						dados.setMunicipioEndereco(processoParteDt.getEnderecoParte().getCidade());
						dados.setUFEndereco(getUF(processoParteDt.getEnderecoParte().getEstado(), processoParteDt.getEnderecoParte().getId_Cidade(), obFabricaConexao));
						dados.setCodigoCep(processoParteDt.getEnderecoParte().getCep());
					}
					dados.setObservacaoPendencia(dados.getNumeroLote() + "|" + dataGeracao.getDataHoraFormatadayyyyMMddHHmmss() + "|PJD|" + debito.getProcessoNumeroPROAD() + "|" + debito.getId_Processo());
					dados.setId_UsuarioLog(arquivo.getId_UsuarioLog());
					dados.setIpComputadorLog(arquivo.getIpComputadorLog());	
					String mensagem = processoParteDebitoCadinNe.Verificar(dados);
					if (mensagem.trim().length() == 0) {
						processoParteDebitoCadinNe.salvar(dados, obFabricaConexao);
						
						debito.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.ENVIADO_CADIN));
						debito.setId_UsuarioLog(arquivo.getId_UsuarioLog());
						debito.setIpComputadorLog(arquivo.getIpComputadorLog());
						processoParteDebitoNe.salvar(debito, obFabricaConexaoAdabasProcessos);	
					} else {
						if (retornoInconsistencias.length() == 0) {
							retornoInconsistencias.append("PROCESSO\tCPF/CNPJ\tNOME\tINCONSISTENCIA\n");	
						}
						retornoInconsistencias.append(debito.getProcessoNumeroCompleto() + " : (PROJUDI)\t" + debito.getCpfParte() + "\t" + debito.getNome() + "\t" + mensagem.replace("\n", " | ") + "\n");
					}
				}
				
				List<ProcessoParteDebitoFisicoDt> debitosFisico = processoParteDebitoFisicoNe.consultarListaLiberadoCadinAindaNaoEnvido(obFabricaConexao);
				for(ProcessoParteDebitoFisicoDt debito : debitosFisico) {
					ProcessoParteDebitoCadinDt dados = new ProcessoParteDebitoCadinDt();
					dados.setFisico(true);
					dados.setNumeroLote(String.valueOf(numeroDoLote));
					dados.setDataGeracao(dataGeracao.getDataHoraFormatadaaaaa_MM_ddHHmmss());					
					dados.setTipoDocumento(debito.getCpfParte().trim().length() > 11 ? "2" : "1"); //sendo 1-CPF 2-CNPJ
					dados.setNumeroDocumento(debito.getCpfParte());
					dados.setNomePessoa(debito.getNome());
					if (dados.getNomePessoa().length() > 60) {
						dados.setNomePessoa(dados.getNomePessoa().substring(0, 60));
					}
					dados.setNumeroProcesso(debito.getProcessoNumeroCompleto());
					dados.setNaturezaPendencia("02"); //01-TRIBUTÁRIA 02-NÃO TRIBUTÁRIA
					dados.setCategoriaPendencia("08"); //08 Não tributário, confirmar outros valores com Administrado do Sistema
					
					GuiaEmissaoDt guiaFisico = guiaSPGNe.consultarGuiaEmissaoSPG(debito.getNumeroGuia(), obFabricaConexaoAdabas);
					if (guiaFisico == null) guiaFisico = guiaSSGNe.consultarGuiaEmissaoSSG(debito.getNumeroGuia(), obFabricaConexaoAdabas);
					if (guiaFisico == null) guiaFisico = guiaPJDNe.consultarGuiaEmissaoNumeroGuia(debito.getNumeroGuia(), obFabricaConexao);
					
					if (guiaFisico != null) {
						dados.setDataVencimentoDebito(guiaFisico.getDataVencimento());	
					} 					
					
					dados.setValorDevido(debito.getValorTotalGuia());
					dados.setMeioEnvioComunicado("02"); //sendo 01-DTE 02-EDITAL 03-CARTA													
					
					ProcessoSPGDt processoSPG = null; //Incluir endereço da parte na consulta...
					ProcessoSSGDt processoSSG = null; //Incluir endereço da parte na consulta...
					ProcessoDt processoDt = null;
					//processoSPG = processoSPGNe.consulteProcesso(debito.getProcessoNumeroCompleto(), obFabricaConexaoAdabasProcessos);
					//if (processoSPG == null) processoSSG = processoSSGNe.consulteProcesso(debito.getProcessoNumeroCompleto(), obFabricaConexaoAdabasProcessos);
					if (processoSPG == null && processoSSG == null) processoDt = processoNe.consultarProcessoNumeroCompleto(debito.getProcessoNumeroCompleto(), obFabricaConexao);
					
					boolean atualizouEndereco = false;
//					if (processoSPG != null) {
//						//Iterar para o polo ativo, passivo e outros...
//						for(PoloSPGDt polo : processoSPG.getPolosAtivos()) {
//							if (polo.getCpf() != null && 
//								debito.getCpfParte() != null && 
//								debito.getCpfParte().trim().equalsIgnoreCase(polo.getCpf().trim())) {							
//								
//								//if (polo.get)
//								
//								break;
//							}
//						}
//					}
//					
//					if (!atualizouEndereco && processoSSG != null) {
//						//Iterar para o polo ativo, passivo e outros...
//					}
					
					//Para os processos físicos que já foram digitalizados...
					if (!atualizouEndereco && processoDt != null) {
						if (processoDt.getListaPolosAtivos() != null) {
							for(Object processoParteObj : processoDt.getListaPolosAtivos()) {
								ProcessoParteDt processoParteDt = (ProcessoParteDt)processoParteObj;
								
								if (processoParteDt.getEnderecoParte() != null &&
									processoParteDt.getCpfCnpj() != null && 
								    debito.getCpfParte() != null && 
								    debito.getCpfParte().trim().equalsIgnoreCase(processoParteDt.getCpfCnpj().trim())) {
									
									AtualizeTipoDeLogradouroELogradouro(dados, processoParteDt.getEnderecoParte().getLogradouro());
				                    if (dados.getNomeLogradouro().length() > 60) {
										dados.setNomeLogradouro(dados.getNomeLogradouro().substring(0, 60));
									}
									dados.setNumeroEndereco(processoParteDt.getEnderecoParte().getNumero());
									if (dados.getNumeroEndereco().length() > 10) {
										dados.setNumeroEndereco(dados.getNumeroEndereco().substring(0, 10));
									}
									dados.setDescricaoComplementoEndereco(processoParteDt.getEnderecoParte().getComplemento() + " " + processoParteDt.getEnderecoParte().getCidade() + " " + processoParteDt.getEnderecoParte().getUf());
									if (dados.getDescricaoComplementoEndereco().length() > 80) {
										dados.setDescricaoComplementoEndereco(dados.getDescricaoComplementoEndereco().substring(0, 80));
									}
									dados.setNomeBairro(processoParteDt.getEnderecoParte().getBairro());
									if (dados.getNomeBairro().length() > 60) {
										dados.setNomeBairro(dados.getNomeBairro().substring(0, 60));
									}
									dados.setMunicipioEndereco(processoParteDt.getEnderecoParte().getCidade());
									dados.setUFEndereco(getUF(processoParteDt.getEnderecoParte().getEstado(), processoParteDt.getEnderecoParte().getId_Cidade(), obFabricaConexao));
									dados.setCodigoCep(processoParteDt.getEnderecoParte().getCep());
									
									atualizouEndereco = true;
								}
							}	
						}						
					}
					
					if (!atualizouEndereco && debito.getEnderecoPartePROAD() != null && debito.getEnderecoPartePROAD().trim().length() > 0) {
						AtualizeTipoDeLogradouroELogradouro(dados, debito.getEnderecoPartePROAD().trim().toUpperCase());						
						if (debito.getEnderecoPartePROAD().contains(",")) {
	                    	String partesEndereco[] = debito.getEnderecoPartePROAD().trim().split(",");
	                    	if (partesEndereco != null) {
	                    		if (partesEndereco.length > 0 && partesEndereco[0] != null && partesEndereco[0].trim().length() > 0) {
	                    			dados.setNomeLogradouro(partesEndereco[0].trim());
	                    		} else if (partesEndereco.length > 1 && partesEndereco[1] != null && partesEndereco[1].trim().length() > 0) {
	                    			dados.setNomeLogradouro(partesEndereco[1].trim());
	                    		} else {
	                    			dados.setNomeLogradouro(debito.getEnderecoPartePROAD());
	                    		}
	                    	}
	                    } else {
	                    	dados.setNomeLogradouro(debito.getEnderecoPartePROAD());
	                    }
					}
					
					if (dados.getNomeLogradouro() != null &&
						dados.getNomeLogradouro().length() > 60) {
						dados.setNomeLogradouro(dados.getNomeLogradouro().substring(0, 60));
					}
                    
                    dados.setDescricaoComplementoEndereco(debito.getEnderecoPartePROAD());
					if (dados.getDescricaoComplementoEndereco().length() > 80) {
						dados.setDescricaoComplementoEndereco(dados.getDescricaoComplementoEndereco().replace(dados.getNomeLogradouro(), ""));
						if (dados.getDescricaoComplementoEndereco().length() > 80) {
							dados.setDescricaoComplementoEndereco(dados.getDescricaoComplementoEndereco().replace(",", ""));
						}
						if (dados.getDescricaoComplementoEndereco().length() > 80) {
							dados.setDescricaoComplementoEndereco(dados.getDescricaoComplementoEndereco().substring(0, 80));
						}														
					}
					
					dados.setObservacaoPendencia(dados.getNumeroLote() + "|" + dataGeracao.getDataHoraFormatadayyyyMMddHHmmss() + "|SPG|" + debito.getProcessoNumeroPROAD() + "|" + debito.getProcessoNumeroCompleto());
					dados.setId_UsuarioLog(arquivo.getId_UsuarioLog());
					dados.setIpComputadorLog(arquivo.getIpComputadorLog());
					dados.setId_ProcessoParteDebito(debito.getId());
					
					String mensagem = processoParteDebitoCadinNe.Verificar(dados);
					if (mensagem.trim().length() == 0) {
						processoParteDebitoCadinNe.salvar(dados, obFabricaConexao);
						
						debito.setId_ProcessoDebitoStatus(String.valueOf(ProcessoDebitoStatusDt.ENVIADO_CADIN));
						debito.setId_UsuarioLog(arquivo.getId_UsuarioLog());
						debito.setIpComputadorLog(arquivo.getIpComputadorLog());
						processoParteDebitoFisicoNe.salvar(debito, obFabricaConexaoAdabasProcessos);	
					} else {
						if (retornoInconsistencias.length() == 0) {
							retornoInconsistencias.append("PROCESSO\tCPF/CNPJ\tNOME\tINCONSISTENCIA\n");	
						}
						retornoInconsistencias.append(debito.getProcessoNumeroCompleto() + " : (FISICO)\t" + debito.getCpfParte() + "\t" + debito.getNome() + "\t" + mensagem.replace("\n", " | ") + "\n");
					}									
				}
				
				registros = processoParteDebitoCadinNe.obtenhaRegistros(arquivo.getNumeroDoLote(), obFabricaConexao);
			} else if (arquivo.isReprocessamentoPorLote()) {
				registros = processoParteDebitoCadinNe.obtenhaRegistros(arquivo.getNumeroDoLote(), obFabricaConexao);
				atualizaDataGeracao = true;
			} else {				
				registros = processoParteDebitoCadinNe.obtenhaRegistrosPelaDataDeGeracao(arquivo.getDataExportacao(), obFabricaConexao);
				atualizaDataGeracao = true;
			}
			
			StringBuilder retorno = new StringBuilder();
			for (ProcessoParteDebitoCadinDt registro : registros) {
				
				if (atualizaDataGeracao) {
					registro.setId_UsuarioLog("1");
					registro.setIpComputadorLog("127.0.0.1");
					registro.setDataGeracao(dataGeracao.getDataHoraFormatadaaaaa_MM_ddHHmmss());

					processoParteDebitoCadinNe.salvar(registro, obFabricaConexao);	
				}				

				retorno.append(registro.getLinhaArquivo());				
			}
			
			arquivo.setConteudoArquivo(retorno.toString());
			arquivo.setNomeArquivo("EnvioCadinTJGO_Lote" + Funcoes.completarZeros(arquivo.getNumeroDoLote(), 3) + "_" + new TJDataHora().getDataHoraFormatadayyyyMMddHHmmss() + ".txt");
			
			arquivo.setConteudoArquivoInconsistencias(retornoInconsistencias.toString());
			arquivo.setNomeArquivoInconsistencias("InconsistenciasEnvioCadinTJGO_Lote" + Funcoes.completarZeros(arquivo.getNumeroDoLote(), 3) + "_" + new TJDataHora().getDataHoraFormatadayyyyMMddHHmmss() + ".csv");
			
			obFabricaConexao.finalizarTransacao();
			//obFabricaConexao.cancelarTransacao();
			return arquivo;
		
		} finally {
			obFabricaConexao.fecharConexao();
			obFabricaConexaoAdabas.fecharConexao();
			obFabricaConexaoAdabasProcessos.fecharConexao();
		}
	}
	
	private HashMap<String, String> estados = new HashMap<String, String>();
	private String getUF(String nomeEstado, String IdCidade, FabricaConexao obFabricaConexao) throws Exception {
		if (!estados.containsKey(nomeEstado)) {
			CidadeDt cidadeDt = null;
			if (IdCidade != null && IdCidade.trim().length() > 0) {
				cidadeDt = new CidadeNe().consultarId(IdCidade, obFabricaConexao);
			}
			if (cidadeDt != null) {
				estados.put(nomeEstado, cidadeDt.getUf());	
			} else {
				return "GO";
			}
		}			
		
		return estados.get(nomeEstado);
	}
	
	private void AtualizeTipoDeLogradouroELogradouro(ProcessoParteDebitoCadinDt processoParteDebitoCadinDt, String logradouro) {
		logradouro = logradouro.trim().toUpperCase();
		
		String vetorLogradouro[] = logradouro.split(" ");
		
		if (vetorLogradouro.length > 1 && ObtenhaDeParaTipoLogradouro().containsKey(vetorLogradouro[0])) {
			String tipo = ObtenhaDeParaTipoLogradouro().get(vetorLogradouro[0]);
			
			String novoLogradouro = "";
			for(int i = 1 ; i < vetorLogradouro.length ; i++) {
				if (novoLogradouro.length() > 0) novoLogradouro += " ";
				novoLogradouro += vetorLogradouro[i].trim(); 
			}
			
			processoParteDebitoCadinDt.setTipoLogradouro(tipo);
			processoParteDebitoCadinDt.setNomeLogradouro(novoLogradouro);			
		} else {
			if (logradouro.toUpperCase().startsWith("AV")) {
				processoParteDebitoCadinDt.setTipoLogradouro("AVE");
			} else {
				processoParteDebitoCadinDt.setTipoLogradouro("RUA");
			}
			processoParteDebitoCadinDt.setNomeLogradouro(logradouro.toUpperCase());
		}		
		if (processoParteDebitoCadinDt.getNomeLogradouro().length() > 60) {
			processoParteDebitoCadinDt.setNomeLogradouro(processoParteDebitoCadinDt.getNomeLogradouro().substring(0, 60));
		}
	}	
		
	private static HashMap<String, String> DeParaTipoLogradouro = new HashMap<String, String>();
	private static HashMap<String, String> ObtenhaDeParaTipoLogradouro() {
		if (DeParaTipoLogradouro.size() == 0) {
			DeParaTipoLogradouro.put("RUA:", "RUA");
			DeParaTipoLogradouro.put("RUA", "RUA");
			DeParaTipoLogradouro.put("R", "RUA");
			DeParaTipoLogradouro.put("AVENIDA", "AVE");
			DeParaTipoLogradouro.put("AENIDA", "AVE");
			DeParaTipoLogradouro.put("AVENIA", "AVE");
			DeParaTipoLogradouro.put("AVENDIA", "AVE");
			DeParaTipoLogradouro.put("AVENIDA\\", "AVE");
			DeParaTipoLogradouro.put("AVE", "AVE");
			DeParaTipoLogradouro.put("AV.", "AVE");
			DeParaTipoLogradouro.put("A.", "AVE");
			DeParaTipoLogradouro.put("AV", "AVE");
			DeParaTipoLogradouro.put("VIA", "VIA");
			DeParaTipoLogradouro.put("RODOVIA", "ROD");
			DeParaTipoLogradouro.put("DRODOVIA", "ROD");
			DeParaTipoLogradouro.put("ROD", "ROD");
			DeParaTipoLogradouro.put("ROD.", "ROD");
			DeParaTipoLogradouro.put("ALAMEDA", "ALD");
			DeParaTipoLogradouro.put("Chácara", "CHA");
			DeParaTipoLogradouro.put("CHACARA","CHA");
			DeParaTipoLogradouro.put("AL", "ALD");
			DeParaTipoLogradouro.put("CONTEM AV.", "AVE");
			DeParaTipoLogradouro.put("R", "RUA");
			DeParaTipoLogradouro.put("ALAMEDA", "ALD");
			DeParaTipoLogradouro.put("FAZENDA", "FAZ");
			DeParaTipoLogradouro.put("FAZ.", "FAZ");
			DeParaTipoLogradouro.put("FAZ", "FAZ");
			DeParaTipoLogradouro.put("ACESSO", "ACE");
			DeParaTipoLogradouro.put("RUAS", "RUA");
			DeParaTipoLogradouro.put("BR", "ROD");
			DeParaTipoLogradouro.put("COLÔNIA", "COL");
			DeParaTipoLogradouro.put("PRACA", "PCA");
			DeParaTipoLogradouro.put("PRALÇA", "PCA");
			DeParaTipoLogradouro.put("VIELA", "VIE");
			DeParaTipoLogradouro.put("COND.", "CON");
			DeParaTipoLogradouro.put("CONDOMÍNIO", "CON");
			DeParaTipoLogradouro.put("CONJUNTO", "CON");
			DeParaTipoLogradouro.put("ESTRADA", "EST");
			DeParaTipoLogradouro.put("LOCAL", "LOC");
			DeParaTipoLogradouro.put("PC", "PRA");
			DeParaTipoLogradouro.put("PRACA", "PRA");
			DeParaTipoLogradouro.put("PRAÇA", "PRA");
			DeParaTipoLogradouro.put("SETOR", "SET");
			DeParaTipoLogradouro.put("SITIO", "SIT");
			DeParaTipoLogradouro.put("SÍTIO", "SIT");
			DeParaTipoLogradouro.put("TRAVESSA", "TRV");
			DeParaTipoLogradouro.put("VIA", "VIA");
		}		
		return DeParaTipoLogradouro;
	}
}
