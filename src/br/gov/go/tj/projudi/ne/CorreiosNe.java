package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Utilities;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.CorreiosDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.PendenciaCorreiosDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.CorreiosPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

public class CorreiosNe extends Negocio {

	private static final long serialVersionUID = -4374110733781499042L;

	public void enviarCartasCorreios() throws Exception {
		List<CorreiosDt> listaPendencias;
		List<CorreiosDt> listaCartas = new ArrayList();
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt;
		ArquivoDt arquivoDt = new ArquivoDt();
		int tamanhoArquivo = 0;
		LogNe logNe = new LogNe();
		ObjectMapper mapper = new ObjectMapper();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		try {
			listaPendencias = (List<CorreiosDt>) pendenciaNe.consultarPendenciasCorreiosCompleta("", String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS), -1, "", false, obFabricaConexao);
			
			for (CorreiosDt pendencia : listaPendencias) {
				try {
					obFabricaConexao.iniciarTransacao();
					if(pendencia.getEnderecoDestinatario().equalsIgnoreCase("") || pendencia.getBairroDestinatario().equalsIgnoreCase("") || pendencia.getCidadeDestinatario().equalsIgnoreCase("") || pendencia.getUfDestinatario().equalsIgnoreCase("") || pendencia.getCepDestinatario().equalsIgnoreCase("")) {
						if(!pendenciaNe.verificarExistenciaVerificarEnderecoParte(pendencia.getId_ProcessoParte(), obFabricaConexao)) {
							pendenciaDt = pendenciaNe.consultarId(pendencia.getIdPendencia());
							pendenciaDt.setComplemento("Logradouro, Bairro, Cidade, Uf e Cep são campos obrigatórios");
							arquivoDt = new ArquivoNe().montaArquivoVerificarECarta(pendenciaDt);
							arquivoDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
							arquivoDt.setIpComputadorLog("Servidor");
							Signer.assinaArquivoCertificadoSistema(arquivoDt);
							new ArquivoNe().salvar(arquivoDt, obFabricaConexao);
							List arquivos = new ArrayList();
							arquivos.add(arquivoDt);
							pendenciaNe.gerarPendenciaVerificarEnderecoParteCorreios(pendenciaDt.getId_Processo(), UsuarioServentiaDt.SistemaProjudi, pendenciaDt.getId_ServentiaCadastrador(), "", arquivos, null, UsuarioDt.SistemaProjudi, pendenciaDt.getId_ProcessoParte(), "Servidor", obFabricaConexao, pendenciaDt.getId_ProcessoPrioridade());
							pendenciaNe.alterarStatusInconsistenciaCorreios(pendencia.getIdPendencia(), obFabricaConexao);
						}
					} else {
						CorreiosDt.ModeloCarta modeloCarta = new CorreiosDt().getModelo(pendencia.getCodigoModelo());
						String html = montaConteudoPorModelo(pendencia, modeloCarta.getId_Modelo());
						byte[] byModelo = html.getBytes();
						byModelo = ConverterHtmlPdf.converteCartaPDF(byModelo, Utilities.millimetersToPoints(12), 0, Utilities.millimetersToPoints(99));
						pendencia.setArquivoComplementar(byModelo);
						pendencia.setModelo(modeloCarta.getTituloModelo());
						if((tamanhoArquivo += byModelo.length + pendencia.montarArquivoServico().getBytes().length) > 200000000) {  // max 200MB
							enviarCartas(listaCartas, tamanhoArquivo, obFabricaConexao);
							listaCartas = new ArrayList();
							listaCartas.add(pendencia);
							tamanhoArquivo = byModelo.length + pendencia.montarArquivoServico().getBytes().length;
						} else {
							listaCartas.add(pendencia);
						}
					}
					obFabricaConexao.finalizarTransacao();
				} catch (Exception e) {
					obFabricaConexao.cancelarTransacao();
					LogDt logDt = new LogDt("JobEnviarCartasCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), mapper.writeValueAsString(pendencia), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					logNe.salvar(logDt);
				} 
			}
			if(listaCartas.size() > 0) {
				enviarCartas(listaCartas, tamanhoArquivo, obFabricaConexao);
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	private void enviarCartas(List<CorreiosDt> listaCartas, int tamanhoArquivo, FabricaConexao obFabricaConexao) throws Exception {
		CorreiosPs correiosPs = new CorreiosPs(obFabricaConexao.getConexao());
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaCorreiosDt pendenciaCorreiosDt;
		LogNe logNe = new LogNe();
		String lote = correiosPs.getLote();
		StringBuffer log = new StringBuffer();
		LogDt logDt = null;
		ObjectMapper mapper = new ObjectMapper();
		String cartaJson = "";

		if (correiosPs.enviarArquivoServicoRespostaNotificacao(listaCartas, lote)) {
			try {
				log.append("(");
				for (CorreiosDt carta : listaCartas) {
					pendenciaNe.alterarStatusAguardandoConfirmacaoCorreios(carta.getIdPendencia());
					pendenciaCorreiosDt = pendenciaCorreiosNe.consultarIdPendencia(carta.getIdPendencia());
					if(pendenciaCorreiosDt != null) {
						cartaJson = mapper.writeValueAsString(carta);
						pendenciaCorreiosDt.setLote(carta.getNumeroLote());
						pendenciaCorreiosDt.setMatriz(carta.getMatrizRelacionamento());
						pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
						pendenciaCorreiosDt.setIpComputadorLog("Servidor");
						pendenciaCorreiosDt.setMetaDados(cartaJson);
						pendenciaCorreiosNe.salvar(pendenciaCorreiosDt);
						log.append(carta.getIdPendencia() + ", ");
					}
				}
				if (log.indexOf(",") != -1) log.delete(log.length()-2, log.length());
				log.append(")");
				if(log.length() > 2) {
					logDt = new LogDt("JobEnviarCartasCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), "Lote: "+lote+", "+(tamanhoArquivo/1024)+" KB, "+listaCartas.size()+" carta(s)", log.toString());
					logNe.salvar(logDt);
				}
			} catch (Exception e) {
				logDt = new LogDt("JobEnviarCartasCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), "Lote: " + lote, Funcoes.obtenhaConteudoPrimeiraExcecao(e));
				logNe.salvar(logDt);
			}
		}
	}

	public void confirmarRecebimentoCorreios(String nomeArquivo) throws Exception {
		CorreiosPs correiosPs = new CorreiosPs();
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		PendenciaCorreiosDt pendenciaCorreiosDt;
		PendenciaDt pendenciaDt;
		UsuarioDt usuarioDt = new UsuarioDt();
		LogDt logDt = new LogDt();
		LogNe logNe = new LogNe();
		StringBuffer log = new StringBuffer();
		int nCartas = 0;
		String[] arquivoRecibo = null;
		String[] arquivoInconsistencia = null;
		
		usuarioDt.setId(UsuarioDt.SistemaProjudi);
        usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
        usuarioDt.setNome("Sistema PROJUDI");
         
        logDt.setId_UsuarioLog(usuarioDt.getId());
        logDt.setIpComputadorLog("Servidor");
		
		if(!nomeArquivo.equalsIgnoreCase("")) {	// BANCO_DADOS
			arquivoRecibo = correiosPs.lerReciboServicoBD(nomeArquivo, "", ""); // [Tipo de Registro|Código do Objeto do Cliente|Número do Lote|Número da Etiqueta|Informação sobre Limite de Postagem]
		} else {								// SERVIDOR_CORREIOS
			arquivoRecibo = correiosPs.lerReciboServico(); 						// [Tipo de Registro|Código do Objeto do Cliente|Número do Lote|Número da Etiqueta|Informação sobre Limite de Postagem]
		}
		
		if(arquivoRecibo != null) {
			log.append("(");
			for (int i = 0; i < arquivoRecibo.length; i++) {
				try {
					String[] reciboCarta = arquivoRecibo[i].split("\\|"); 
					if(reciboCarta.length >= 4) {
						pendenciaCorreiosDt = pendenciaCorreiosNe.consultarIdPendenciaStatus(reciboCarta[1], String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS));
						if(pendenciaCorreiosDt != null) {
							pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
							pendenciaCorreiosDt.setIpComputadorLog("Servidor");
							pendenciaCorreiosDt.setCodigoRastreamento(reciboCarta[3]);
							pendenciaCorreiosNe.salvar(pendenciaCorreiosDt);
							log.append(reciboCarta[1] + ", ");
							nCartas++;
						}
					}
				} catch (Exception e) {
					logDt = new LogDt("JobConfirmarRecebimentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), "Id_Pend="+arquivoRecibo[i], Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					logNe.salvar(logDt);
				}
			}
			if (log.indexOf(",") != -1) log.delete(log.length()-2, log.length());
			log.append(")");
			
			if(log.length() > 2) {
				logDt = new LogDt("JobConfirmarRecebimentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), "Recibo " + nCartas + " carta(s)", log.toString());
				logNe.salvar(logDt);
			}
		}
		
		nCartas = 0;
		if(!nomeArquivo.equalsIgnoreCase("")) {	// BANCO_DADOS
			arquivoInconsistencia = correiosPs.lerNotificacaoInconsistenciaBD(nomeArquivo, "", ""); // [Tipo de Registro|Código do Objeto do Cliente Inconsistente|Identificador Tipo Inconsistência Objeto|Mensagem Tipo Inconsistência Objeto]
		} else {								// SERVIDOR_CORREIOS
			arquivoInconsistencia = correiosPs.lerNotificacaoInconsistencia(); 						// [Tipo de Registro|Código do Objeto do Cliente Inconsistente|Identificador Tipo Inconsistência Objeto|Mensagem Tipo Inconsistência Objeto]
		}
		
		if(arquivoInconsistencia != null) {
			log = new StringBuffer();
			log.append("(");
			FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try {
				for(int i=0; i < arquivoInconsistencia.length; i++) {
					try {
						obFabricaConexao.iniciarTransacao();
						String[] cartaInconsistencia = arquivoInconsistencia[i].split("\\|");
						
						if(cartaInconsistencia[0].equalsIgnoreCase("0") && cartaInconsistencia.length >= 4 && cartaInconsistencia[2].equalsIgnoreCase("16")) {
							pendenciaNe.retornarStatusAguardandoEnvioCorreios(cartaInconsistencia[1]);
							logDt = new LogDt("PendenciaCorreios", cartaInconsistencia[1], UsuarioDt.SistemaProjudi, "Servidor", String.valueOf(LogTipoDt.Alterar), "Lote: " + cartaInconsistencia[1] , "Lote: , DataExpedicao: , Matriz: ");
							pendenciaCorreiosNe.reverterEnvioLote(cartaInconsistencia[1], logDt);
						} else if(cartaInconsistencia.length >= 4 && !cartaInconsistencia[2].trim().equals("")) {
							if(CorreiosDt.isUsuarioCorrigir(Funcoes.StringToInt(cartaInconsistencia[2]))) {
								pendenciaDt = pendenciaNe.consultarId(cartaInconsistencia[1]);
								if(pendenciaDt != null && !pendenciaNe.verificarExistenciaVerificarEnderecoParte(pendenciaDt.getId_ProcessoParte(), obFabricaConexao) && 
										(Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus())==PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS || Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus())==PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS)) {
									pendenciaDt.setComplemento(cartaInconsistencia[3]);
									ArquivoDt arquivoDt = new ArquivoNe().montaArquivoVerificarECarta(pendenciaDt);
									Signer.assinaArquivoCertificadoSistema(arquivoDt);
									arquivoDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
									arquivoDt.setIpComputadorLog("Servidor");
									new ArquivoNe().salvar(arquivoDt, obFabricaConexao);
									List arquivos = new ArrayList();
									arquivos.add(arquivoDt);
									pendenciaNe.gerarPendenciaVerificarEnderecoParteCorreios(pendenciaDt.getId_Processo(), UsuarioServentiaDt.SistemaProjudi, pendenciaDt.getId_ServentiaCadastrador(), "", arquivos, null, UsuarioDt.SistemaProjudi, pendenciaDt.getId_ProcessoParte(), "Servidor", obFabricaConexao, pendenciaDt.getId_ProcessoPrioridade());
									
									pendenciaNe.alterarStatusInconsistenciaCorreios(cartaInconsistencia[1], obFabricaConexao);
									pendenciaCorreiosDt = pendenciaCorreiosNe.consultarIdPendencia(cartaInconsistencia[1]);
									pendenciaCorreiosDt.setCodigoInconsistencia(cartaInconsistencia[2]);
									pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
									pendenciaCorreiosDt.setIpComputadorLog("Servidor");
									pendenciaCorreiosNe.salvar(pendenciaCorreiosDt, obFabricaConexao);
									log.append(cartaInconsistencia[1] + ", ");
									nCartas++;
								}
							}
						}
						obFabricaConexao.finalizarTransacao();
					} catch (Exception e) {
						obFabricaConexao.cancelarTransacao();
						logDt = new LogDt("JobConfirmarRecebimentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), "Id_Pend="+arquivoInconsistencia[i], Funcoes.obtenhaConteudoPrimeiraExcecao(e));
						logNe.salvar(logDt);
					}
				}
			} finally {
				obFabricaConexao.fecharConexao();
			}
			
			if (log.indexOf(",") != -1) log.delete(log.length()-2, log.length());
			log.append(")");
			
			if(log.length() > 2) {
				logDt = new LogDt("JobConfirmarRecebimentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), "Inconsistencia " + nCartas + " carta(s)", log.toString());
				logNe.salvar(logDt);
			}
		}
	}
	
	public void confirmarPostagemCorreios(String nomeArquivo) throws Exception {
		CorreiosPs correiosPs = new CorreiosPs();
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		PendenciaCorreiosDt pendenciaCorreiosDt = null;
		PendenciaDt pendenciaDt;
		UsuarioDt usuarioDt = new UsuarioDt();
		LogDt logDt = new LogDt();
		LogNe logNe = new LogNe();
		StringBuffer log = new StringBuffer();
		String[] postagemCarta = null;
		String idPendencia = null;
		int nCartas = 0;
		String[] arquivoPostagem = null;
		
		usuarioDt.setId(UsuarioDt.SistemaProjudi);
        usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
        usuarioDt.setNome("Sistema PROJUDI");
         
        logDt.setId_UsuarioLog(usuarioDt.getId());
        logDt.setIpComputadorLog("Servidor");
		
        if(!nomeArquivo.equalsIgnoreCase("")) {	// BANCO_DADOS
        	arquivoPostagem = correiosPs.lerRastreamentoDataEstimadaEntregaBD(nomeArquivo, "", ""); 	// [NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataPostagem|HoraPostagem|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPrimeiraTentativaEntrega]
        } else {								// SERVIDOR_CORREIOS
        	arquivoPostagem = correiosPs.lerRastreamentoDataEstimadaEntrega(); 							// [NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataPostagem|HoraPostagem|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPrimeiraTentativaEntrega]
        }
		
		if(arquivoPostagem != null) {
			log.append("(");
			for (int i = 0; i < arquivoPostagem.length; i++) {
				try {
					postagemCarta = arquivoPostagem[i].split("\\|"); 
					if(postagemCarta.length >= 15) {
						idPendencia = pendenciaCorreiosNe.consultarIdPendenciaRastreamento(postagemCarta[1], String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS));
						if(idPendencia != null && !idPendencia.equalsIgnoreCase("")) {
							pendenciaDt = pendenciaNe.consultarId(idPendencia);
							if(pendenciaDt != null) {
								pendenciaCorreiosDt = pendenciaCorreiosNe.consultarIdPendencia(pendenciaDt.getId());
								if(pendenciaCorreiosDt != null) pendenciaDt.setIdPendenciaCorreios(pendenciaCorreiosDt.getId());
								pendenciaDt.setComplemento(postagemCarta[1]);
								pendenciaNe.efetuarConfirmacaoCorreios(pendenciaDt, usuarioDt, logDt);
								if(pendenciaCorreiosDt != null) {
									pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
									pendenciaCorreiosDt.setIpComputadorLog("Servidor");
									pendenciaCorreiosDt.setDataExpedicao(postagemCarta[7]);
									pendenciaCorreiosNe.salvar(pendenciaCorreiosDt);
								}
								log.append(postagemCarta[1] + ", ");
								nCartas++;
							}
						}
					}
				} catch (Exception e) {
					logDt = new LogDt("JobConfirmarPostagemCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), arquivoPostagem[i], Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					logNe.salvar(logDt);
				}
			}
			if (log.indexOf(",") != -1) log.delete(log.length()-2, log.length());
			log.append(")");
			
			if(log.length() > 2) {
				logDt = new LogDt("JobConfirmarPostagemCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), nCartas + " carta(s)", log.toString());
				logNe.salvar(logDt);
			}
		}

	}
	
	public void confirmarEntregaCorreios(String nomeArquivo) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		StringBuffer log = new StringBuffer();
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		PendenciaDt pendenciaDt;
		String idPendencia = "";
		LogNe logNe = new LogNe();
		LogDt logDt = null;
		int nCartas = 0;
		String[] arquivoEntrega = null;
		
		CorreiosPs correiosPs = new CorreiosPs(obFabricaConexao.getConexao());
		
		if(!nomeArquivo.equalsIgnoreCase("")) {	// BANCO_DADOS
			arquivoEntrega = correiosPs.lerRastreamentoDataFinalEntregaBD(nomeArquivo, "", ""); 	//[NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataEntrega|HoraEntrega|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPostagem]
		} else {								// SERVIDOR_CORREIOS
			arquivoEntrega = correiosPs.lerRastreamentoDataFinalEntrega(); 							//[NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataEntrega|HoraEntrega|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPostagem]
		}
		
		if(arquivoEntrega != null) {
			log.append("(");
			for (int i = 0; i < arquivoEntrega.length; i++) {
				try {
					String[] entregaCarta = arquivoEntrega[i].split("\\|"); 
					if(entregaCarta.length >= 15) {
						idPendencia = pendenciaCorreiosNe.consultarIdPendenciaRastreamento(entregaCarta[1], String.valueOf(PendenciaStatusDt.ID_RECEBIDO_CORREIOS));
						if(idPendencia != null && idPendencia.length() > 0) {
							pendenciaDt = new PendenciaDt();
							pendenciaDt.setId(idPendencia);
							if (Funcoes.StringToInt(entregaCarta[6]) == CorreiosDt.CodigoBaixa.ENTREGUE.getCodigo()) { 
								pendenciaDt.setId_PendenciaStatus(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
							} else {
								pendenciaDt.setId_PendenciaStatus(String.valueOf(PendenciaStatusDt.ID_NAO_CUMPRIDA));
							}
							pendenciaNe.confirmarEntregaCorreios(pendenciaDt, entregaCarta[6], entregaCarta[7]+" "+entregaCarta[8]);
							log.append(idPendencia + ", ");
							nCartas++;
						}
					}
				} catch (Exception e) {
					logDt = new LogDt("JobConfirmarEntregaCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), "Id_Pend="+arquivoEntrega[i], Funcoes.obtenhaConteudoPrimeiraExcecao(e));
					logNe.salvar(logDt);
				}
			}
			if (log.indexOf(",") != -1) log.delete(log.length()-2, log.length());
			log.append(")");
			
			if(log.length() > 2) {
				logDt = new LogDt("JobConfirmarEntregaCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), nCartas + " carta(s)", log.toString());
				logNe.salvar(logDt);
			}
		}
	}

	public void confirmarRecebimentoAR(String nomeArquivo) throws Exception {
		CorreiosPs correiosPs = new CorreiosPs();
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt;
		UsuarioDt usuarioDt = new UsuarioDt();
		List arquivos = null;
		ArquivoDt arquivoDt;
		String[] idPendencia_codRastreamento;
		LogNe logNe = new LogNe();
		LogDt logDt = null;
		StringBuffer log = new StringBuffer();
		int nCartas = 0;
		Map<String, byte[]> arquivoDevolucao = null;
		
		usuarioDt.setId(UsuarioDt.SistemaProjudi);
        usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
        usuarioDt.setIpComputadorLog("Servidor");
        usuarioDt.setNome("Sistema PROJUDI");
        
        if(!nomeArquivo.equalsIgnoreCase("")) {	// BANCO_DADOS
        	arquivoDevolucao = correiosPs.lerDevolucaoARsBD(nomeArquivo, "", "");	//<Id_Pend-Cod_Rastreamento, ArquivoAR>
        } else {								// SERVIDOR_CORREIOS
        	arquivoDevolucao = correiosPs.lerDevolucaoARs();						//<Id_Pend-Cod_Rastreamento, ArquivoAR>
        }
		
		log.append("(");
		for (Map.Entry<String, byte[]> carta : arquivoDevolucao.entrySet()) {
			try {
				if(carta.getKey() != null) {
					idPendencia_codRastreamento = carta.getKey().split("-");
					if(idPendencia_codRastreamento != null) {
						pendenciaDt = pendenciaNe.consultarId(idPendencia_codRastreamento[0]);
						if(pendenciaDt != null && pendenciaDt.getDataFim().isEmpty() && (Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus())==PendenciaStatusDt.ID_CUMPRIDA || Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus())==PendenciaStatusDt.ID_NAO_CUMPRIDA)) {
							arquivos = new ArrayList();
							pendenciaDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
							pendenciaDt.setIpComputadorLog("Servidor");
							arquivoDt = new ArquivoDt();
							arquivoDt.setContentType("application/pdf");
							arquivoDt.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.ID_CORREIO_AVISO_RECEBIMENTO));
							arquivoDt.setNomeArquivo(idPendencia_codRastreamento[1]+".pdf");
							arquivoDt.setArquivo(carta.getValue());
							arquivoDt.setRecibo("false");
							arquivos.add(arquivoDt);				
							pendenciaNe.concluirPendenciaCorreios(pendenciaDt, usuarioDt, arquivos);
							log.append(idPendencia_codRastreamento[0]+", ");
							nCartas++;
						}
					}
				}
			} catch (Exception e) {
				logDt = new LogDt("JobConfirmarRecebimentoARCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), carta.getKey(), Funcoes.obtenhaConteudoPrimeiraExcecao(e));
				logNe.salvar(logDt);
			}
		}
		
		if (log.indexOf(",") != -1) log.delete(log.length()-2, log.length());
		log.append(")");
		
		if(log.length() > 2) {
			logDt = new LogDt("JobConfirmarRecebimentoARCorreios", "", UsuarioDt.SistemaProjudi, "Servidor",  String.valueOf(LogTipoDt.ExecucaoAutomatica), nCartas + " carta(s)", log.toString());
			logNe.salvar(logDt);
		}
	}
	
	public void vincularItemGuiaPostagem(String idProcesso, FabricaConexao obFabricaConexao) throws Exception {
		if(idProcesso == null || idProcesso.equalsIgnoreCase("")) return;
			
		PendenciaNe pendenciaNe = new PendenciaNe();
		GuiaItemDisponivelNe guiaItemDisponivelNe = new GuiaItemDisponivelNe();
		List idsPendencia = pendenciaNe.consultarPendenciasAguardandoPagamentoPostagem(idProcesso);
			
		     
		for (Iterator iterator = idsPendencia.iterator(); iterator.hasNext();) {
			String idPendencia = (String) iterator.next();
			if (guiaItemDisponivelNe.processoPossuiItemCustaDespesaPostalEmitidoPagoSemVinculoPendencia(idProcesso)) {
				if (guiaItemDisponivelNe.vincularGuiaItemDepesaPostalPendencia(idPendencia, idProcesso, "1", obFabricaConexao)) {
					pendenciaNe.alterarStatusAguardandoEnvioCorreios(idPendencia, obFabricaConexao);
				} else {
					break;
				}
			}
		}
	}

	public String montaConteudoPorModelo(CorreiosDt carta, String id_Modelo) throws Exception {
		ModeloNe modeloNe = new ModeloNe();
		ModeloCorreiosNe modeloCorreiosNe = new ModeloCorreiosNe();
		String texto = modeloNe.consultarId(id_Modelo).getTexto();
		return modeloCorreiosNe.montaConteudo(carta, texto);
	}
	
	public boolean executarRecibo(String idPendencia, String codRastreamento) throws Exception {
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		PendenciaCorreiosDt pendenciaCorreiosDt;
		boolean realizado = false;
		
		pendenciaCorreiosDt = pendenciaCorreiosNe.consultarIdPendenciaStatus(idPendencia, String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS));
		if(pendenciaCorreiosDt != null) {
			pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
			pendenciaCorreiosDt.setIpComputadorLog("Servidor");
			pendenciaCorreiosDt.setCodigoRastreamento(codRastreamento);
			pendenciaCorreiosNe.salvar(pendenciaCorreiosDt);
			realizado = true;
		}
		return realizado;
	}
	
	public boolean executarInconsistencia(String idPendencia, String codInconsistencia, String msgInconsistencia) throws Exception {
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		PendenciaCorreiosDt pendenciaCorreiosDt;
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt = null;
		boolean realizado = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		if(CorreiosDt.isUsuarioCorrigir(Funcoes.StringToInt(codInconsistencia))) {
			pendenciaDt = pendenciaNe.consultarId(idPendencia);
			if(pendenciaDt != null && !pendenciaNe.verificarExistenciaVerificarEnderecoParte(pendenciaDt.getId_ProcessoParte(), obFabricaConexao) && 
					(Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus())==PendenciaStatusDt.ID_AGUARDANDO_ENVIO_CORREIOS || Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus())==PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS)) {
				pendenciaDt.setComplemento(msgInconsistencia);
				ArquivoDt arquivoDt = new ArquivoNe().montaArquivoVerificarECarta(pendenciaDt);
				Signer.assinaArquivoCertificadoSistema(arquivoDt);
				arquivoDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
				arquivoDt.setIpComputadorLog("Servidor");
				new ArquivoNe().salvar(arquivoDt, obFabricaConexao);
				List arquivos = new ArrayList();
				arquivos.add(arquivoDt);
				pendenciaNe.gerarPendenciaVerificarEnderecoParteCorreios(pendenciaDt.getId_Processo(), UsuarioServentiaDt.SistemaProjudi, pendenciaDt.getId_ServentiaCadastrador(), "", arquivos, null, UsuarioDt.SistemaProjudi, pendenciaDt.getId_ProcessoParte(), "Servidor", obFabricaConexao, pendenciaDt.getId_ProcessoPrioridade());
				pendenciaNe.alterarStatusInconsistenciaCorreios(idPendencia, obFabricaConexao);
				pendenciaCorreiosDt = pendenciaCorreiosNe.consultarIdPendencia(idPendencia);
				pendenciaCorreiosDt.setCodigoInconsistencia(codInconsistencia);
				pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
				pendenciaCorreiosDt.setIpComputadorLog("Servidor");
				pendenciaCorreiosNe.salvar(pendenciaCorreiosDt, obFabricaConexao);
				realizado = true;
			}
		}
		return realizado;
	}
	
	public boolean executarPostagem(String idPendencia, String codRastreamento, String dataPostagem) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		PendenciaCorreiosDt pendenciaCorreiosDt = null;
		PendenciaDt pendenciaDt;
		UsuarioDt usuarioDt = new UsuarioDt();
		LogDt logDt = new LogDt();
		boolean realizado = false;
		
		usuarioDt.setId(UsuarioDt.SistemaProjudi);
        usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
        usuarioDt.setNome("Sistema PROJUDI");
         
        logDt.setId_UsuarioLog(usuarioDt.getId());
        logDt.setIpComputadorLog("Servidor");
		
        pendenciaDt = pendenciaNe.consultarId(idPendencia);
		if(pendenciaDt != null && Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus())==PendenciaStatusDt.ID_AGUARDANDO_CONFIRMACAO_CORREIOS) {
			pendenciaCorreiosDt = pendenciaCorreiosNe.consultarIdPendencia(pendenciaDt.getId());
			if(pendenciaCorreiosDt != null) pendenciaDt.setIdPendenciaCorreios(pendenciaCorreiosDt.getId());
			pendenciaDt.setComplemento(codRastreamento);
			pendenciaNe.efetuarConfirmacaoCorreios(pendenciaDt, usuarioDt, logDt);
			if(pendenciaCorreiosDt != null) {
				pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
				pendenciaCorreiosDt.setIpComputadorLog("Servidor");
				pendenciaCorreiosDt.setDataExpedicao(dataPostagem);
				pendenciaCorreiosNe.salvar(pendenciaCorreiosDt);
			}
			realizado = true;
		}
		return realizado;
	}
	
	public boolean executarEntrega(String codRastreamento, String codBaixa, String dataEntrega, String horaEntrega) throws Exception {
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		PendenciaDt pendenciaDt;
		String idPendencia = "";
		boolean realizado = false;
		
		idPendencia = pendenciaCorreiosNe.consultarIdPendenciaRastreamento(codRastreamento, String.valueOf(PendenciaStatusDt.ID_RECEBIDO_CORREIOS));
		if(idPendencia != null && idPendencia.length() > 0) {
			pendenciaDt = new PendenciaDt();
			pendenciaDt.setId(idPendencia);
			if (Funcoes.StringToInt(codBaixa) == CorreiosDt.CodigoBaixa.ENTREGUE.getCodigo()) { 
				pendenciaDt.setId_PendenciaStatus(String.valueOf(PendenciaStatusDt.ID_CUMPRIDA));
			} else {
				pendenciaDt.setId_PendenciaStatus(String.valueOf(PendenciaStatusDt.ID_NAO_CUMPRIDA));
			}
			pendenciaNe.confirmarEntregaCorreios(pendenciaDt, codBaixa, dataEntrega+" "+horaEntrega);
			realizado = true;
		}
		return realizado;
	}
	
	public boolean executarAR(String idPendencia, String codRastreamento, String nomeArquivo) throws Exception {
		CorreiosPs correiosPs = new CorreiosPs();
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt;
		UsuarioDt usuarioDt = new UsuarioDt();
		List arquivos = null;
		ArquivoDt arquivoDt;
		boolean realizado = false;
		
		usuarioDt.setId(UsuarioDt.SistemaProjudi);
        usuarioDt.setId_UsuarioServentia(UsuarioServentiaDt.SistemaProjudi);
        usuarioDt.setIpComputadorLog("Servidor");
        usuarioDt.setNome("Sistema PROJUDI");
        
		byte[] avisoRecebimento = correiosPs.getArBD(idPendencia, "", nomeArquivo, "", "");
		
		pendenciaDt = pendenciaNe.consultarId(idPendencia);
		if(pendenciaDt != null && pendenciaDt.getDataFim().isEmpty() && (Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus())==PendenciaStatusDt.ID_CUMPRIDA || Funcoes.StringToInt(pendenciaDt.getId_PendenciaStatus())==PendenciaStatusDt.ID_NAO_CUMPRIDA)) {
			arquivos = new ArrayList();
			pendenciaDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
			pendenciaDt.setIpComputadorLog("Servidor");
			arquivoDt = new ArquivoDt();
			arquivoDt.setContentType("application/pdf");
			arquivoDt.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.ID_CORREIO_AVISO_RECEBIMENTO));
			arquivoDt.setNomeArquivo(codRastreamento + ".pdf");
			arquivoDt.setArquivo(avisoRecebimento);
			arquivoDt.setRecibo("false");
			arquivos.add(arquivoDt);				
			pendenciaNe.concluirPendenciaCorreios(pendenciaDt, usuarioDt, arquivos);
			realizado = true;
		}
		return realizado;
	}
	
	public byte[] getArBD(String codRastreamento, String nomeArquivo) throws Exception {
		CorreiosPs correiosPs = new CorreiosPs();
		byte[] arquivoAR = correiosPs.getArBD("", codRastreamento, nomeArquivo, "", "");
		return arquivoAR;
	}
	
	public String[][] consultarHistorico(String idPendencia, String rastreamento, String qtdeDias) throws Exception {
		String dataInicial = "";
		String dataFinal = "";
		ArquivoNe arquivoNe = new ArquivoNe();
		Map<String, String> codRastreamento = new HashMap<String, String>();
		String[][] historico = new String[100][16];
		long tempoInicial = System.currentTimeMillis();
		int segundos = 0;
		int minutos = 0;
		int contador = 4;

		if(idPendencia.length() > 0) {
			consultarPendencia(idPendencia, historico);
			dataInicial = historico[1][7].substring(0, 10);
		}
		
		// Notificação de Recibo de Serviço(NR)
		dataFinal = calcularDataFinal(dataInicial, dataFinal, qtdeDias);
		String[] arquivoRecibo = arquivoNe.lerReciboServicoBD(ArquivoTipoDt.ID_CORREIO_RECIBO, dataInicial, dataFinal, idPendencia, rastreamento);	//[TipoRegistro|CódigoObjetoCliente|NúmeroLote|NúmeroEtiqueta|InformaçãoLimitePostagem]
		for (int i = 0; i < arquivoRecibo.length; i++) {
			String[] aux = arquivoRecibo[i].split("\\|");
			idPendencia = aux[2]; 
			codRastreamento.put(aux[4], idPendencia);
			historico[contador][0]=aux[0];historico[contador][1]=aux[1];historico[contador][2]=aux[2];historico[contador][3]=aux[3];historico[contador++][4]=aux[4];
			dataInicial = Funcoes.getDataArquivoCorreios(aux[0]);
		}
		arquivoRecibo = null;
		
		// Notificação de Inconsistência(NI)
		dataFinal = calcularDataFinal(dataInicial, dataFinal, qtdeDias);
		String[] arquivoInconsistencia = arquivoNe.lerNotificacaoInconsistenciaBD(ArquivoTipoDt.ID_CORREIO_INCONSISTENCIA, dataInicial, dataFinal, idPendencia);	//[Tipo de Registro|Número do Lote|Identificador Tipo Iconsistência Arquivo|Mensagem Tipo Iconsistência Arquivo]
		for (int i = 0; i < arquivoInconsistencia.length; i++) {																									//[Tipo de Registro|Código do Objeto do Cliente Inconsistente|Identificador Tipo Inconsistência Objeto|Mensagem Tipo Inconsistência Objeto]
			String[] aux = arquivoInconsistencia[i].split("\\|");
			historico[contador][0]=aux[0];historico[contador][1]=aux[1];historico[contador][2]=aux[2];historico[contador][3]=aux[3];historico[contador++][4]=aux[4];
			dataInicial = Funcoes.getDataArquivoCorreios(aux[0]);
		}
		arquivoInconsistencia = null;
		
		// Rastreamento Antecipado sobre Data de Postagem e Data Estimada para Entrega(RP)
		dataFinal = calcularDataFinal(dataInicial, dataFinal, qtdeDias);
		String[] arquivoPostagem = arquivoNe.lerRastreamentoDataEstimadaEntregaBD(ArquivoTipoDt.ID_CORREIO_POSTAGEM, dataInicial, dataFinal, codRastreamento); //[NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataPostagem|HoraPostagem|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPrimeiraTentativaEntrega]
		for (int i = 0; i < arquivoPostagem.length; i++) {
			String[] aux = arquivoPostagem[i].split("\\|");
			historico[contador][0]=aux[0];historico[contador][1]=aux[1];historico[contador][2]=aux[2];historico[contador][3]=aux[3];historico[contador][4]=aux[4];historico[contador][5]=aux[5];historico[contador][6]=aux[6];historico[contador][7]=aux[7];historico[contador][8]=aux[8];historico[contador][9]=aux[9];historico[contador][10]=aux[10];historico[contador][11]=aux[11];historico[contador][12]=aux[12];historico[contador][13]=aux[13];historico[contador][14]=aux[14];historico[contador++][15]=aux[15];
			dataInicial = Funcoes.getDataArquivoCorreios(aux[0]);
		}
		arquivoPostagem = null;
		
		if(idPendencia.length() > 0 && historico[1][0] == null) {
			consultarPendencia(idPendencia, historico);
			dataInicial = historico[1][7].substring(0, 10);
		}
		
		// Rastreamento Antecipado sobre Final de Entrega(RF)
		dataFinal = calcularDataFinal(dataInicial, dataFinal, qtdeDias);
		String[] arquivoEntrega = arquivoNe.lerRastreamentoDataFinalEntregaBD(ArquivoTipoDt.ID_CORREIO_ENTREGA, dataInicial, dataFinal, codRastreamento);	//[NúmeroLote|EtiquetaRegistro|TipoPostal|ServiçoContratado|CategoriaServiço|SiglaEventoSRO|CódigoEvento|DataEntrega|HoraEntrega|EventoSRO|UnidadeCorreios|CEPCorreios|CidadeCorreios|UFCorreios|DataEstimadaPostagem]
		for (int i = 0; i < arquivoEntrega.length; i++) {
			String[] aux = arquivoEntrega[i].split("\\|");
			historico[contador][0]=aux[0];historico[contador][1]=aux[1];historico[contador][2]=aux[2];historico[contador][3]=aux[3];historico[contador][4]=aux[4];historico[contador][5]=aux[5];historico[contador][6]=aux[6];historico[contador][7]=aux[7];historico[contador][8]=aux[8];historico[contador][9]=aux[9];historico[contador][10]=aux[10];historico[contador][11]=aux[11];historico[contador][12]=aux[12];historico[contador][13]=aux[13];historico[contador][14]=aux[14];historico[contador++][15]=aux[15];
			dataInicial = Funcoes.getDataArquivoCorreios(aux[0]);
		}
		arquivoEntrega = null;
		
		// Devolução de ARs e Meta Dados(AD)
		dataFinal = calcularDataFinal(dataInicial, dataFinal, qtdeDias);
		String[] arquivoARs = arquivoNe.lerDevolucaoARsBD(ArquivoTipoDt.ID_CORREIO_DEVOLUCAO_AR, dataInicial, dataFinal, idPendencia, codRastreamento);	//[Tipo de Registro|Código do Objeto do Cliente|Número do Lote|Número da Etiqueta|Número do AR|Código da Baixa|Nome da Imagem do AR]
		for (int i = 0; i < arquivoARs.length; i++) {
			String[] aux = arquivoARs[i].split("\\|");
			historico[contador][0]=aux[0];historico[contador][1]=aux[1];historico[contador][2]=aux[2];historico[contador][3]=aux[3];historico[contador][4]=aux[4];historico[contador][5]=aux[5];historico[contador][6]=aux[6];historico[contador++][7]=aux[7];
		}
		arquivoARs = null;
		
		segundos = (int) ((System.currentTimeMillis() - tempoInicial)/1000); 
		minutos = segundos/60; 
		segundos = segundos-(minutos*60);
		historico[0][0] = ((minutos<10?"0"+minutos:minutos)+":"+(segundos<10?"0"+segundos:segundos));
		
		return historico;
	}

	private String calcularDataFinal(String dataInicial, String dataFinal, String qtdeDias) throws Exception {
		String data = "";
		if(dataFinal.length() == 0 && dataInicial.length() > 0 && qtdeDias != null && qtdeDias.length() > 0) 
			data = Funcoes.somaData(dataInicial, Funcoes.StringToInt(qtdeDias));
		else
			data = dataFinal;
		return data;
	}

	private void consultarPendencia(String idPendencia, String[][] historico) throws Exception {
		PendenciaDt pendenciaDt = null;
		PendenciaCorreiosDt pendenciaCorreiosDt = null; 
		MovimentacaoProcessoDt movimentacaoProcessoDt = null;
		ProcessoDt processoDt = null;
		MovimentacaoNe movimentacaoNe = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		
		pendenciaDt = new PendenciaNe().consultarId(idPendencia);
		historico[1][0] = "PEND";
		pendenciaCorreiosDt = new PendenciaCorreiosNe().consultarIdPendencia(idPendencia);
		historico[2][0] = "PEND_CORREIOS";
		
		if(pendenciaDt == null) {
			pendenciaDt = new PendenciaNe().consultarFinalizadaId(idPendencia);
			historico[1][0] = "PEND_FINAL";
		}
		if(pendenciaCorreiosDt == null) {
			pendenciaCorreiosDt = new PendenciaCorreiosNe().consultarFinalizadaIdPendencia(idPendencia);
			historico[2][0] = "PEND_FINAL_CORREIOS";
		}
		
		if (pendenciaDt != null) {
			historico[1][1]=pendenciaDt.getId();historico[1][2]=pendenciaDt.getId_PendenciaTipo();historico[1][3]=pendenciaDt.getId_PendenciaStatus();historico[1][4]=pendenciaDt.getId_Movimentacao();historico[1][5]=pendenciaDt.getId_Processo();historico[1][6]=pendenciaDt.getId_ProcessoParte();historico[1][7]=pendenciaDt.getDataInicio();historico[1][8]=pendenciaDt.getId_UsuarioFinalizador();historico[1][9]=pendenciaDt.getDataFim();
			if(pendenciaCorreiosDt.getCodigoRastreamento().length()>0) {
				movimentacaoNe = new MovimentacaoNe();
				movimentacaoProcessoDt = movimentacaoNe.consultarMovimentacaoECarta(pendenciaDt.getId_Processo(), pendenciaCorreiosDt.getCodigoRastreamento());
				if(movimentacaoProcessoDt != null) {historico[3][2]=movimentacaoProcessoDt.getComplemento();historico[3][3]=movimentacaoProcessoDt.getDataRealizacao();}else{historico[3][2]="";historico[3][3]="";}
				movimentacaoProcessoDt = movimentacaoNe.consultarMovimentacaoArquivoECarta(pendenciaDt.getId_Processo(), pendenciaCorreiosDt.getCodigoRastreamento());
				if(movimentacaoProcessoDt != null) {historico[3][0]=movimentacaoProcessoDt.getComplemento()+" "+movimentacaoProcessoDt.getArquivoTipo();historico[3][1]=movimentacaoProcessoDt.getDataRealizacao();}else{historico[3][0]="";historico[3][1]="";}
			} else {
				historico[3][2]="";historico[3][3]="";historico[3][0]="";historico[3][1]="";
			}
			processoDt = new ProcessoNe().consultarIdCompleto(pendenciaDt.getId_Processo());
			historico[0][1] = processoDt.getProcessoNumero();
			historico[0][2] = processoDt.getServentia();
		}
		if (pendenciaCorreiosDt != null) {
			historico[2][1]=pendenciaCorreiosDt.getId();historico[2][2]=pendenciaCorreiosDt.getId_Pend();historico[2][3]=pendenciaCorreiosDt.getCodigoModelo();historico[2][4]=(pendenciaCorreiosDt.getMaoPropria().equalsIgnoreCase("true")?"1":"0");historico[2][5]=pendenciaCorreiosDt.getId_ProcessoCustaTipo();historico[2][6]=(pendenciaCorreiosDt.getOrdemServico().equalsIgnoreCase("true")?"1":"0");historico[2][7]=pendenciaCorreiosDt.getMatriz();historico[2][8]=pendenciaCorreiosDt.getLote();historico[2][9]=pendenciaCorreiosDt.getCodigoRastreamento();historico[2][10]=pendenciaCorreiosDt.getCodigoInconsistencia();historico[2][11]=pendenciaCorreiosDt.getDataExpedicao();historico[2][12]=pendenciaCorreiosDt.getDataEntrega();historico[2][13]=pendenciaCorreiosDt.getCodigoBaixa();
			if(new PendenciaNe().verificarExistenciaVerificarEnderecoParte(pendenciaDt.getId_ProcessoParte(), obFabricaConexao))
				historico[3][4] = "POSSUI PENDÊNCIA VERIFICAR ENDEREÇO PARTE";
			else
				historico[3][4] = "NÃO POSSUI PENDÊNCIA VERIFICAR ENDEREÇO PARTE";
		}
	}
	
	public int preencherMetaDados(String numeroRegistros) throws Exception {
		List<CorreiosDt> listaPendencias;
		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaCorreiosNe pendenciaCorreiosNe = new PendenciaCorreiosNe();
		PendenciaCorreiosDt pendenciaCorreiosDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		ObjectMapper mapper = new ObjectMapper();
		int contador = 0;
		int offset = 0;
		
		
		do {
			listaPendencias = (List<CorreiosDt>) pendenciaNe.consultarPendenciasCorreiosCompleta("", "", offset, numeroRegistros, true, obFabricaConexao);
			for (CorreiosDt carta : listaPendencias) {
				try {
					if(carta.getMetaDados() == null) {
						pendenciaCorreiosDt = pendenciaCorreiosNe.consultarIdPendencia(carta.getIdPendencia());
						carta.setDataExpedicao(pendenciaCorreiosDt.getDataExpedicao());
						pendenciaCorreiosDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
						pendenciaCorreiosDt.setIpComputadorLog("Servidor");
						pendenciaCorreiosDt.setMetaDados(mapper.writeValueAsString(carta));
						pendenciaCorreiosNe.salvar(pendenciaCorreiosDt);
						contador++;
					}
				} catch (Exception e) {
					throw e;
				}
			}
			offset += Funcoes.StringToInt(numeroRegistros);
		} while (listaPendencias.size() > 0);
		
		return contador;

	}
	
	public boolean corrigirDataInsercao() throws Exception {
		boolean retorno = false;
		List<String[]> lista = null;
		ArquivoNe arquivoNe = new ArquivoNe();
		
		lista = arquivoNe.consultarECarta();
		for (String[] arquivo : lista) {
			arquivo[1] = Funcoes.getDataArquivoCorreios(arquivo[1]);
			if(arquivo[1].length() > 0) 
				arquivoNe.alterarDataInsercao(arquivo);
			retorno = true;
		}
		return retorno;
	}

}