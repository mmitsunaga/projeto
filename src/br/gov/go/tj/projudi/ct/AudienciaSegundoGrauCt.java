package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaSegundoGrauDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que controla a criação e exclusão de sessões, assim como as consultas dessas
 * @author msapaula
 * 28/09/2009 10:00:56
 */
public class AudienciaSegundoGrauCt extends AudienciaCtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4069277420629722180L;

	public int Permissao() {
		return AudienciaSegundoGrauDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioNe, int paginaAtual, String nomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		AudienciaNe audienciaNe;
		AudienciaSegundoGrauDt audienciaSegundoGrauDt;
		String acao = "/WEB-INF/jsptjgo/AudienciaSegundoGrau.jsp";
		String mensagem = "";
		int paginaAnterior = 0;
		String Id_AudienciaProcesso = "";
		int passoEditar = -1;
		String Id_Audiencia = "";
		boolean somentePendentesAcordao = false;
		boolean somentePendentesAssinatura = false;
		boolean somentePreAnalisadas = false;
		AudienciaDt audienciaCompleta = null;

		setAtributosIniciais(request, paginaAtual);

		audienciaNe = (AudienciaNe) request.getSession().getAttribute("Audienciane");
		if (audienciaNe == null) audienciaNe = new AudienciaNe();

		audienciaSegundoGrauDt = (AudienciaSegundoGrauDt) request.getSession().getAttribute("AudienciaSegundoGraudt");
		if (audienciaSegundoGrauDt == null) audienciaSegundoGrauDt = new AudienciaSegundoGrauDt();

		//Seta dados em AudienciaSegundoGrau
		setDadosAudienciaSegundoGrau(request, audienciaSegundoGrauDt, usuarioNe);

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		request.setAttribute("PaginaAnterior", paginaAtual);

		// Pega valor digitado na caixa de paginação
		if (request.getParameter("CaixaTextoPosicionar") != null) posicaoPaginaAtual = String.valueOf((Funcoes.StringToInt(request.getParameter("CaixaTextoPosicionar"))) - 1);
		request.setAttribute("podeCadastrar", usuarioNe.getVerificaPermissao(AudienciaSegundoGrauDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Novo));
		//----------------------------------------------------------------------------------------------------------------------------------------//
		
		if (request.getParameter("PassoEditar") != null && !String.valueOf(request.getParameter("PassoEditar")).trim().equalsIgnoreCase("null")) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));		
		if (request.getParameter("Id_Audiencia") != null) Id_Audiencia = String.valueOf(request.getParameter("Id_Audiencia"));	
		if (request.getParameter("Id_AudienciaProcesso") != null) Id_AudienciaProcesso = String.valueOf(request.getParameter("Id_AudienciaProcesso"));
		
		// Verifica se a chamada foi realizada da página inicial - Sessões Pendentes
		if (request.getParameter("SomentePendentesAcordao") != null && request.getParameter("SomentePendentesAcordao").trim().length() > 0 && 
		   (request.getParameter("SomentePendentesAcordao").trim().equalsIgnoreCase("S") || request.getParameter("SomentePendentesAcordao").trim().equalsIgnoreCase("N"))) 
				somentePendentesAcordao = (request.getParameter("SomentePendentesAcordao").trim().equalsIgnoreCase("S"));
		
		if (request.getParameter("SomenteAguardandoAssinatura") != null && request.getParameter("SomenteAguardandoAssinatura").trim().length() > 0 && 
			(request.getParameter("SomenteAguardandoAssinatura").trim().equalsIgnoreCase("S") || request.getParameter("SomenteAguardandoAssinatura").trim().equalsIgnoreCase("N"))) 
			somentePendentesAssinatura = (request.getParameter("SomenteAguardandoAssinatura").trim().equalsIgnoreCase("S"));
		
		if (request.getParameter("SomentePreAnalisadas") != null && request.getParameter("SomentePreAnalisadas").trim().length() > 0 && 
			(request.getParameter("SomentePreAnalisadas").trim().equalsIgnoreCase("S") || request.getParameter("SomentePreAnalisadas").trim().equalsIgnoreCase("N"))) 
			somentePreAnalisadas = (request.getParameter("SomentePreAnalisadas").trim().equalsIgnoreCase("S"));
						
		switch (paginaAtual) {
		
			case Configuracao.Novo:
				if (paginaAnterior == Configuracao.Localizar) {
					//Se pagina anterior é "Localizar" refere-se ao botão limpar na tela de Consulta de Sessões com filtro
					acao = "/WEB-INF/jsptjgo/AudienciaSegundoGrauLocalizar.jsp";
					limparDadosConsulta(audienciaSegundoGrauDt, request, audienciaNe, posicaoPaginaAtual, usuarioNe);

				} else audienciaSegundoGrauDt.limpar();
				break;

			case Configuracao.Salvar:
				//Controles da variável passoEditar
				// -1 : Confirmação para criação de pauta de sessão				
				//  1 : Manter Julgamento Adiado	
				if (passoEditar == -1){
					request.setAttribute("Mensagem", "Clique para confirmar a Criação da Sessão.");
					this.consultarSessoesAbertas(request, posicaoPaginaAtual, audienciaSegundoGrauDt, audienciaNe, usuarioNe);
				} else {
					request.setAttribute("PassoEditar", String.valueOf(passoEditar));					
					AudienciaDt audienciaDt = audienciaNe.consultarAudienciaProcessoCompleta(Id_AudienciaProcesso);					
					if (passoEditar == 1) {
						request.setAttribute("Mensagem", String.format("Clique para confirmar a movimentação de \"Manter Adiamento\" para o processo %s", audienciaDt.getAudienciaProcessoDt().getProcessoDt().getProcessoNumero()));					
					} 
					Id_Audiencia = request.getParameter("Id_Audiencia");
					//Consulta somente os processos não julgados em uma sessão
					if (consultarProcessosNaoJulgadosSessao(request, Id_Audiencia, posicaoPaginaAtual, audienciaSegundoGrauDt, audienciaNe, usuarioNe)){
						acao = "/WEB-INF/jsptjgo/AudienciaProcessoPendentesLocalizar.jsp";
					}
				}				
				break;

			case Configuracao.SalvarResultado:
				//Controles da variável passoEditar
				// -1 : Cria Pauta de Sessão de 2º Grau
				//  1 : Manter Julgamento Adiado				
				if (passoEditar == -1) {
					//Valida dados obrigatórios
					mensagem = audienciaNe.validarDadosGeracaoAudienciaSegundoGrau(audienciaSegundoGrauDt);

					if (mensagem.length() == 0) {
						audienciaNe.agendarAudienciaSegundoGrau(audienciaSegundoGrauDt);

						request.setAttribute("MensagemOk", "Sessão Criada com Sucesso.");
						audienciaSegundoGrauDt.limpar();

					} else request.setAttribute("MensagemErro", mensagem);
					//Atualiza lista de sessões abertas
					this.consultarSessoesAbertas(request, posicaoPaginaAtual, audienciaSegundoGrauDt, audienciaNe, usuarioNe);
				} else {					
					Id_AudienciaProcesso = request.getParameter("Id_AudienciaProcesso");					
					if (Id_AudienciaProcesso != null && !Id_AudienciaProcesso.equals("")) {
						AudienciaDt audienciaDt = audienciaNe.consultarAudienciaProcessoCompleta(Id_AudienciaProcesso);
						if (audienciaDt != null && audienciaDt.getAudienciaProcessoDt() != null && audienciaDt.getAudienciaProcessoDt().getProcessoDt() != null){
							String mensagemRetorno = "";
							if (passoEditar == 1) {								
								mensagemRetorno = audienciaNe.gerarMovimentacaoJulgamentoManterAdiado(audienciaDt, usuarioNe.getUsuarioDt().getId_UsuarioServentia(), usuarioNe.getUsuarioDt().getId(), usuarioNe.getUsuarioDt().getIpComputadorLog());
								if (mensagemRetorno.length() == 0) request.setAttribute("MensagemOk", "Movimentação \"Manter Adiamento\" foi realizada com Sucesso.");
							} 
							if (mensagemRetorno.length() > 0) request.setAttribute("MensagemErro", mensagemRetorno);
						}						
					}					
					Id_Audiencia = request.getParameter("Id_Audiencia");
					//Consulta somente os processos não julgados em uma sessão
					if (consultarProcessosNaoJulgadosSessao(request, Id_Audiencia, posicaoPaginaAtual, audienciaSegundoGrauDt, audienciaNe, usuarioNe)){
						acao = "/WEB-INF/jsptjgo/AudienciaProcessoPendentesLocalizar.jsp";
					}										
				}						
				break;

			//Confirmação para exclusão de sessão
			case Configuracao.Excluir:
				if (request.getParameter("Id_Audiencia") != null) {
					audienciaSegundoGrauDt.setId(request.getParameter("Id_Audiencia"));
					request.setAttribute("Id_Audiencia", audienciaSegundoGrauDt.getId());
					request.setAttribute("Mensagem", "Clique para confirmar a Exclusão da Sessão do dia " + request.getParameter("Audiencia"));
					this.consultarSessoesAbertas(request, posicaoPaginaAtual, audienciaSegundoGrauDt, audienciaNe, usuarioNe);
				}
				break;

			//Exclui uma sessão de 2º grau
			case Configuracao.ExcluirResultado:
				mensagem = audienciaNe.validarExclusaoAudiencia(audienciaSegundoGrauDt.getId(), usuarioNe.getUsuarioDt());
				if (mensagem.length() == 0) {
					audienciaNe.excluirSessaoSegundoGrau(audienciaSegundoGrauDt, usuarioNe.getUsuarioDt());

					request.setAttribute("MensagemOk", "Sessão Excluída com Sucesso.");
					audienciaSegundoGrauDt.limpar();

				} else request.setAttribute("MensagemErro", mensagem);
				//Limpa id da sessão selecionada
				audienciaSegundoGrauDt.setId("");
				//Atualiza lista de sessões abertas
				this.consultarSessoesAbertas(request, posicaoPaginaAtual, audienciaSegundoGrauDt, audienciaNe, usuarioNe);
				break;

			//Consultar Sessões com Fitlro (Finalizadas ou Abertas)
			case Configuracao.Localizar:
				acao = "/WEB-INF/jsptjgo/AudienciaSegundoGrauLocalizar.jsp";
				this.consultarSessoesFiltro(request, audienciaSegundoGrauDt, audienciaNe, posicaoPaginaAtual, usuarioNe);
				break;

			//Consultar responsáveis por uma AudienciaProcesso
			case Configuracao.Curinga6:
				Id_AudienciaProcesso = request.getParameter("Id_AudienciaProcesso");				
				if (Id_AudienciaProcesso != null && Id_AudienciaProcesso.length() > 0) {
					acao = "/WEB-INF/jsptjgo/AudienciaProcessoSegundoGrauResponsavel.jsp";
					//Consulta dados da audiência, processo e responsáveis
					AudienciaDt audienciaDt = audienciaNe.consultarAudienciaProcessoCompleta(Id_AudienciaProcesso);
					List tempList = audienciaNe.consultarResponsaveisAudienciaProcesso(Id_AudienciaProcesso);
					audienciaDt.getAudienciaProcessoDt().setListaResponsaveis(tempList);
					request.setAttribute("AudienciaSegundoGrauDt", audienciaDt);
				}
				break;
				
			//Imprimir o relatório 
			case Configuracao.Imprimir:			
				//Se uma sessão foi selecionada, deve consultar os processos vinculados a ela
				if (Id_Audiencia != null && Id_Audiencia.length() > 0 && paginaAnterior == Configuracao.Localizar) {											
					//Nesse caso a consulta deve retornar todos os processos vinculados, sejam os já julgados ou não
					audienciaCompleta = audienciaNe.consultarAudienciaProcessos(Id_Audiencia, usuarioNe.getUsuarioDt(), true);						
					if (audienciaCompleta != null){						
						byte[] byTemp = null;
						if (passoEditar == 1){
							//Pauta do dia
							if(audienciaCompleta.getListaAudienciaProcessoDtPautaDia(false) != null && audienciaCompleta.getListaAudienciaProcessoDtPautaDia(false).size() > 0) {
								byTemp = audienciaNe.relAudienciaProcessoPautaDia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , audienciaCompleta, usuarioNe.getUsuarioDt());
								if (byTemp != null) {																		
									enviarODT(response,byTemp,"Relatorio");																																													
									return;
								}
							}
							else request.setAttribute("MensagemErro", "Sessão não possui nenhum Processo vinculado na Pauta do Dia.");
						} else if (passoEditar == 2) {
							// Adiados
							if(audienciaCompleta.getListaAudienciaProcessoDtAdiadosIniciados(false) != null && audienciaCompleta.getListaAudienciaProcessoDtAdiadosIniciados(false).size() > 0) {
								byTemp = audienciaNe.relAudienciaProcessoAdiados(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , audienciaCompleta, usuarioNe.getUsuarioDt());
								if (byTemp != null) {
									enviarODT(response,byTemp,"Relatorio");																											
									return;
								}
							}
							else request.setAttribute("MensagemErro", "Sessão não possui nenhum Processo vinculado na Pauta Julgamentos Adiados.");
						} else if (passoEditar == 3) {
							// EM MESA PARA JULGAMENTO
							if(audienciaCompleta.getListaAudienciaProcessoDtEmMesaParaJulgamento(false) != null && audienciaCompleta.getListaAudienciaProcessoDtEmMesaParaJulgamento(false).size() > 0) {
								byTemp = audienciaNe.relAudienciaProcessoEmMesaParaJulgamento(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), audienciaCompleta, usuarioNe.getUsuarioDt());
								if (byTemp != null) {
									enviarODT(response,byTemp,"Relatorio");										
									return;
								}
								
							}
							else request.setAttribute("MensagemErro", "Sessão não possui nenhum Processo vinculado Em Mesa Para Julgamento.");
//						} else if (passoEditar == 4) {
//							// Adiados Magistrado
//							if(audienciaCompleta.getListaAudienciaProcessoDtAdiadosIniciados(false) != null && audienciaCompleta.getListaAudienciaProcessoDtAdiadosIniciados(false).size() > 0) {
//								HtmlPipelineContext hpc = new HtmlPipelineContext((CssAppliers) new CssAppliersImpl(new XMLWorkerFontProvider()));
//								byTemp = audienciaNe.relAudienciaProcessoAdiadosMagistrado(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), audienciaCompleta, usuarioNe.getUsuarioDt(), hpc);
//								if (byTemp != null) {
//									response.setHeader("Content-Disposition", "attachment; filename=Relatorio"+System.currentTimeMillis()+".pdf");
//									response.setContentType("application/pdf");
//									try{
//										response.getOut putStream().write(byTemp, 0, byTemp.length);
//										
//									}
//									
//									return;
//								}
//							}
//							else request.setAttribute("MensagemErro", "Sessão não possui nenhum Processo vinculado na Pauta Julgamentos Adiados.");
						} else request.setAttribute("MensagemErro", "Nenhum tipo de sessão foi selecionado.");
					} else request.setAttribute("MensagemErro", "Sessão não possui nenhum Processo vinculado.");					
				} else request.setAttribute("MensagemErro", "Nenhuma sessão foi selecionada.");
				
				acao = "/WEB-INF/jsptjgo/AudienciaSegundoGrauLocalizar.jsp";
				this.consultarSessoesFiltro(request, audienciaSegundoGrauDt, audienciaNe, posicaoPaginaAtual, usuarioNe);
				request.setAttribute("PaginaAnterior", Configuracao.Localizar);
				
				break;
			//Consulta as Sessões de 2º para um Relator (Pauta Relator), Advogado ou Promotor
			case Configuracao.Curinga7:
				int grupoTipo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo());
				this.consultarSessoesPendentes(request, audienciaNe, posicaoPaginaAtual, usuarioNe, somentePendentesAcordao, grupoTipo, somentePendentesAssinatura, somentePreAnalisadas);
				//int grupo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo());				
//				if (grupo == GrupoDt.ADVOGADOS || grupo == GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES || grupo == GrupoDt.MINISTERIO_PUBLICO) {
//					acao = "/WEB-INF/jsptjgo/AudienciaSegundoGrauUsuario.jsp";
//				} else if (grupo == GrupoDt.JUIZES_TURMA_RECURSAL || grupo == GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU || grupo == GrupoDt.DESEMBARGADOR) {
//					request.setAttribute("podeMovimentar", usuarioNe.getVerificaPermissao(AudienciaMovimentacaoDt.CodigoPermissaoAudienciaProcesso));
//					acao = "/WEB-INF/jsptjgo/AudienciaSegundoGrauRelator.jsp";
//				}
				if (usuarioNe.isAdvogado() || usuarioNe.isAssessorAdvogado() || usuarioNe.isMp() || usuarioNe.isAssessorMP()) {				
					acao = "/WEB-INF/jsptjgo/AudienciaSegundoGrauUsuario.jsp";				
				} else if (grupoTipo == GrupoTipoDt.JUIZ_TURMA || 
						   grupoTipo == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU ||
						   grupoTipo == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || 
						   grupoTipo == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || 
						   grupoTipo == GrupoTipoDt.ASSISTENTE_GABINETE  || 
						   grupoTipo == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO || 
						   grupoTipo == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) {
					request.setAttribute("podeMovimentar", usuarioNe.getVerificaPermissao(AudienciaMovimentacaoDt.CodigoPermissaoAudienciaProcesso));
					acao = "/WEB-INF/jsptjgo/AudienciaSegundoGrauRelator.jsp";
				}
				break;		

			// Consultar Status de Audiência
			case (AudienciaProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				acao = "/WEB-INF/jsptjgo/AudienciaProcessoStatusLocalizar.jsp";

				break;

			default: {
				// Controles da variável passoEditar
				// -1 : Redireciona para passo 1
				// 1  : Redireciona para passo 2 - Pendências a Gerar	
				// 2  : Alteração do Extrato da Ata de Julgamento
				// 3  : Alteração do Extrato da Ata de Julgamento Iniciado
				// 4  : Alteração do Extrato da Ata de Julgamento Adiado
				switch (passoEditar) {
					case 1:
						boolean baixouArquivo = false;
						String idArquivo = request.getParameter("IdArquivo");
						
						if (idArquivo != null && idArquivo.trim().length() > 0){
							audienciaNe.baixarArquivo(idArquivo, response, usuarioNe.getUsuarioDt().getId(), audienciaSegundoGrauDt.getIpComputadorLog());
							baixouArquivo = true;								
							
							return;
						}
						
						if (!baixouArquivo) throw new MensagemException("Acesso negado.");				
					
						break;
						
					case 2:
					case 3:
					case 4:
						ProcessoDt processodt = (ProcessoDt) request.getSession().getAttribute("processoDt");						
						String mensagemErro = "";	
						String id_Processo = "";
						if (processodt != null) {	
							id_Processo = processodt.getId();
							AudienciaProcessoDt audienciaProcessoDt = audienciaNe.consultarAudienciaProcessoPendente(processodt.getId(), usuarioNe.getUsuarioDt());
							if (audienciaProcessoDt != null){
								if (passoEditar == 2 && audienciaProcessoDt.getId_ArquivoAta() != null && audienciaProcessoDt.getId_ArquivoAta().trim().length() > 0){
									acao = String.format("AudienciaProcessoMovimentacao?Id_AudienciaProcesso=%s&PaginaAtual=%s&fluxo=%s",audienciaProcessoDt.getId(), Configuracao.Novo, null);
								} else if (passoEditar == 3 && audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada() != null && audienciaProcessoDt.getId_ArquivoAtaSessaoIniciada().trim().length() > 0){
									acao = String.format("AudienciaProcessoMovimentacao?Id_AudienciaProcesso=%s&PaginaAtual=%s&fluxo=%s&TipoAudienciaProcessoMovimentacao=%s&EhAlteracaoExtratoAta=S",audienciaProcessoDt.getId(), Configuracao.Novo, null, passoEditar);									
								} else if (passoEditar == 4 && audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada() != null && audienciaProcessoDt.getId_ArquivoAtaSessaoAdiada().trim().length() > 0){
									acao = String.format("AudienciaProcessoMovimentacao?Id_AudienciaProcesso=%s&PaginaAtual=%s&fluxo=%s&TipoAudienciaProcessoMovimentacao=%s&EhAlteracaoExtratoAta=S",audienciaProcessoDt.getId(), Configuracao.Novo, null, passoEditar);									
								} else{
									mensagemErro = "Impossível alterar, pois o extrato da ata de julgamento ainda não foi inserido.";									
								}
							} else {
								mensagemErro = "Este processo não possui uma sessão em andamento.";								
							}
						} else {
							mensagemErro = "Processo não localizado.";
						}
						
						if(mensagemErro.length() > 0) {
							if (id_Processo.length() > 0) redireciona(response, String.format("BuscaProcesso?Id_Processo=%s&MensagemErro=%s",id_Processo, mensagemErro));
							else redireciona(response, String.format("BuscaProcesso?PassoBusca=%s&PaginaAtual=%s&MensagemErro=%s", 2, Configuracao.Localizar, mensagemErro));							
							return;
						}
						
						break;
				
					default:		
						if (Id_Audiencia != null && Id_Audiencia.length() > 0) {
							//Se uma sessão foi selecionada, deve consultar os processos vinculados a ela						
							if (paginaAnterior == Configuracao.Localizar) {
								//Nesse caso a consulta deve retornar todos os processos vinculados, sejam os já julgados ou não
								audienciaCompleta = audienciaNe.consultarAudienciaProcessos(Id_Audiencia, usuarioNe.getUsuarioDt(), true);
								acao = "/WEB-INF/jsptjgo/AudienciaProcessoLocalizar.jsp";
								request.setAttribute("Audienciadt", audienciaCompleta);
								request.setAttribute("podeMovimentar", usuarioNe.getVerificaPermissao(AudienciaMovimentacaoDt.CodigoPermissaoAudienciaProcesso));
							} else {
								//Consulta somente os processos não julgados em uma sessão
								if (consultarProcessosNaoJulgadosSessao(request, Id_Audiencia, posicaoPaginaAtual, audienciaSegundoGrauDt, audienciaNe, usuarioNe)){
									request.setAttribute("podeMovimentar", usuarioNe.getVerificaPermissao(MovimentacaoDt.CodigoPermissao));
									acao = "/WEB-INF/jsptjgo/AudienciaProcessoPendentesLocalizar.jsp";
								}						
							}
						} else {
							//Consulta as sessões abertas e redireciona para tela de sessões
							this.consultarSessoesAbertas(request, posicaoPaginaAtual, audienciaSegundoGrauDt, audienciaNe, usuarioNe);
						}
					break;
				}
			}
		}
		
		int grupoTipoUsuarioLogado = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo());
		if (grupoTipoUsuarioLogado == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU){
			request.setAttribute("PodeMovimentar", "S");
		} else if (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.JUIZ_TURMA || grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU){
			request.setAttribute("PodeAnalisar", "S");
		} else if ((grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA) || (grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE) || (grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO) || (grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)){
			request.setAttribute("PodePreAnalisar", "S");
		}
		
		request.getSession().setAttribute("Audienciane", audienciaNe);
		request.getSession().setAttribute("AudienciaSegundoGraudt", audienciaSegundoGrauDt);
		request.setAttribute("PosicaoPagina", (Funcoes.StringToInt(posicaoPaginaAtual) + 1));
		request.setAttribute("Id_Audiencia", Id_Audiencia);
		request.setAttribute("Id_AudienciaProcesso", Id_AudienciaProcesso);
		request.setAttribute("SomentePendentesAcordao", (somentePendentesAcordao ? "S" : "N"));
		request.setAttribute("SomentePendentesAssinatura", (somentePendentesAssinatura ? "S" : "N"));
		request.setAttribute("SomentePreAnalisadas", (somentePreAnalisadas ? "S" : "N"));
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(acao);
		requestDispatcher.include(request, response);
	}

	/**
	 * Limpa filtros na consulta de Sessões com Filtro
	 * @param audienciaSegundoGrauDt
	 * @param request
	 * @param audienciaNe
	 * @param posicaoPaginaAtual
	 * @param usuarioNe
	 * @throws Exception 
	 */
	private void limparDadosConsulta(AudienciaSegundoGrauDt audienciaSegundoGrauDt, HttpServletRequest request, AudienciaNe audienciaNe, String posicaoPaginaAtual, UsuarioNe usuarioNe) throws Exception{
		audienciaSegundoGrauDt.setDataInicialConsulta("");
		audienciaSegundoGrauDt.setDataFinalConsulta("");
		request.setAttribute("PaginaAnterior", Configuracao.Localizar);
		this.consultarSessoesFiltro(request, audienciaSegundoGrauDt, audienciaNe, posicaoPaginaAtual, usuarioNe);
	}

	/**
	 * Captura dados e seta no objeto AudienciaSegundoGrauDt
	 * @param request
	 * @param audienciaSegundoGrauDt
	 * @param usuarioSessaoNe
	 */
	protected void setDadosAudienciaSegundoGrau(HttpServletRequest request, AudienciaSegundoGrauDt audienciaSegundoGrauDt, UsuarioNe usuarioSessaoNe) {
		audienciaSegundoGrauDt.setData(request.getParameter("Data"));
		audienciaSegundoGrauDt.setHora(request.getParameter("Hora"));
		audienciaSegundoGrauDt.setId_Serventia(usuarioSessaoNe.getUsuarioDt().getId_Serventia());
		audienciaSegundoGrauDt.setServentia(usuarioSessaoNe.getUsuarioDt().getServentia());
		audienciaSegundoGrauDt.setDataInicialConsulta(request.getParameter("DataInicialConsulta"));
		audienciaSegundoGrauDt.setDataFinalConsulta(request.getParameter("DataFinalConsulta"));
		audienciaSegundoGrauDt.setServentia(usuarioSessaoNe.getUsuarioDt().getServentia());
		audienciaSegundoGrauDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
		audienciaSegundoGrauDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());
	}

	/**
	 * Consulta todas sessões de 2º grau, sejam as abertas ou finalizadas
	 * @param request
	 * @param audienciaSegundoGrauDt
	 * @param audienciaNe
	 * @param posicaoPaginaAtual
	 * @param usuarioNe
	 * @throws Exception
	 */
	private void consultarSessoesFiltro(HttpServletRequest request, AudienciaDt audienciaSegundoGrauDt, AudienciaNe audienciaNe, String posicaoPaginaAtual, UsuarioNe usuarioNe) throws Exception{
		String statusAudiencia = "-1";
		if (request.getParameter("StatusAudiencia") != null) statusAudiencia = request.getParameter("StatusAudiencia");
		List tempList = audienciaNe.consultarSessoesFiltro(usuarioNe.getUsuarioDt().getId_Serventia(), usuarioNe.getUsuarioDt().getGrupoTipoCodigo(), audienciaSegundoGrauDt, statusAudiencia, posicaoPaginaAtual);
		if (tempList.size() > 0) {
			request.setAttribute("ListaAudiencias", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", audienciaNe.getQuantidadePaginas());
		} else {
			request.setAttribute("MensagemErro", "Nenhuma Sessão Localizada.");
		}
		request.setAttribute("StatusAudiencia", statusAudiencia);

	}

	/**
	 * Consulta todas sessões de 2º grau Pendentes de acordo com o usuário logado
	 * @param request
	 * @param audienciaNe
	 * @param posicaoPaginaAtual
	 * @param usuarioNe
	 * @throws Exception 
	 */
	protected void consultarSessoesPendentes(HttpServletRequest request, AudienciaNe audienciaNe, String posicaoPaginaAtual, UsuarioNe usuarioNe, boolean somentePendentesAcordao, int grupoTipo, boolean somentePendentesAssinatura, boolean somentePreAnalisadas) throws Exception{
		String id_Sessao = "";
		if (request.getParameter("Id_Sessao") != null) id_Sessao = request.getParameter("Id_Sessao");
		List tempList = null;
		
		// jvosantos - 03/06/2019 17:14 - Adicionar verificação de se é sessão virtual para mostrar apreciados
		boolean virtual = BooleanUtils.toBoolean(request.getParameter("virtual")); 

		if (somentePendentesAcordao)
			tempList = !virtual
					? audienciaNe.consultarSessoesPendentesPendentesAcordao(
							usuarioNe.getUsuarioDt(),
							id_Sessao,
							somentePendentesAssinatura,
							somentePreAnalisadas)
					: audienciaNe.consultarSessoesVirtuaisPendentesPendentesAcordao(
							usuarioNe.getUsuarioDt(),
							id_Sessao,
							somentePendentesAssinatura,
							somentePreAnalisadas);
		else tempList = audienciaNe.consultarSessoesPendentes(usuarioNe.getUsuarioDt(), id_Sessao, somentePendentesAssinatura, somentePreAnalisadas);
		
		if (tempList.size() > 0) {
			request.setAttribute("ListaSessoes", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Curinga7);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", "1");
		} else if (request.getAttribute("MensagemOk") == null || request.getAttribute("MensagemOk").toString().trim().length() == 0) {
			request.setAttribute("MensagemErro", "Nenhuma Sessão Localizada.");
		}
		if (grupoTipo == GrupoTipoDt.ASSISTENTE_GABINETE || grupoTipo == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO) request.setAttribute("NomeTitulo", ("Assistente de Gabinete: " +  usuarioNe.getUsuarioDt().getNome()));
		else if (grupoTipo == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) request.setAttribute("NomeTitulo", ("Assessor de Desembargador: " +  usuarioNe.getUsuarioDt().getNome()));
		else request.setAttribute("NomeTitulo", ("Relator: " +  usuarioNe.getUsuarioDt().getNome()));
			
		request.setAttribute("Id_Sessao", id_Sessao);
		
		List tempListGeral = null;
		if (somentePendentesAcordao) // jvosantos - 11/09/2019 13:74 - Correção da listagem de sessões no select de sessões
			tempListGeral = !virtual
				? audienciaNe.consultarSessoesPendentesPendentesAcordao(
						usuarioNe.getUsuarioDt(),
						null,
						somentePendentesAssinatura,
						somentePreAnalisadas)
				: audienciaNe.consultarSessoesVirtuaisPendentesPendentesAcordao(
						usuarioNe.getUsuarioDt(),
						null,
						somentePendentesAssinatura,
						somentePreAnalisadas);
		else tempListGeral = audienciaNe.consultarSessoesPendentes(usuarioNe.getUsuarioDt(), null, somentePendentesAssinatura, somentePreAnalisadas);
		
		request.setAttribute("ListaSessoesAbertas", tempListGeral);
	}

	/**
	 * Consulta as sessões abertas na serventia do usuário logado
	 * @param request
	 * @param posicaoPaginaAtual
	 * @param audienciaSegundoGrauDt
	 * @param audienciaNe
	 * @param usuarioNe
	 * @throws Exception
	 */
	protected void consultarSessoesAbertas(HttpServletRequest request, String posicaoPaginaAtual, AudienciaSegundoGrauDt audienciaSegundoGrauDt, AudienciaNe audienciaNe, UsuarioNe usuarioNe) throws Exception{
		//Seta serventia do usuário logado
		audienciaSegundoGrauDt.setId_Serventia(usuarioNe.getUsuarioDt().getId_Serventia());
		audienciaSegundoGrauDt.setServentia(usuarioNe.getUsuarioDt().getServentia());
		request.setAttribute("tempBuscaId_Audiencia", "Id_Audiencia");
		request.setAttribute("tempBuscaAudiencia", "Audiencia");

		List listaAudiencias = audienciaNe.consultarSessoesAbertas(usuarioNe.getUsuarioDt().getId_Serventia(), usuarioNe.getUsuarioDt().getGrupoTipoCodigo(), true);
		if (listaAudiencias.size() > 0) {
			request.setAttribute("ListaAudiencias", listaAudiencias);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", audienciaNe.getQuantidadePaginas());
		}

	}

	/**
	 * Método responsável por atribuir valores aos atributos iniciais utilizados nas jsp's
	 * 
	 * @author Keila Sousa Silva
	 * @param request
	 * @param paginaAtual
	 */
	protected void setAtributosIniciais(HttpServletRequest request, int paginaAtual) {
		request.setAttribute("tempPrograma", "Sessões de Julgamento");
		request.setAttribute("tempRetorno", "AudienciaSegundoGrau");
		request.setAttribute("PaginaAnterior", paginaAtual);
		request.setAttribute("PaginaAtual", (paginaAtual));

		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

		request.setAttribute("DataInicialConsulta", request.getParameter("DataInicialConsulta"));
		request.setAttribute("DataFinalConsulta", request.getParameter("DataFinalConsulta"));
	}

	/**
	 *  Consultar os cargos da serventia corrente aos quais o usuário corrente está vinculado
	 * @param request
	 */
	protected void consultarServentiaCargo(HttpServletRequest request, AudienciaNe audienciaNe, String nomeBusca, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception {
		request.setAttribute("tempBuscaId_ServentiaCargo", "Id_ServentiaCargo");
		request.setAttribute("tempBuscaServentiaCargo", "ServentiaCargo");

		List listaServentiaCargos = audienciaNe.consultarServentiaCargos(nomeBusca, posicaoPaginaAtual, usuarioNe.getUsuarioDt().getId_Serventia(), usuarioNe.getUsuarioDt().getServentiaTipoCodigo(), usuarioNe.getUsuarioDt().getServentiaSubtipoCodigo());

		if (listaServentiaCargos != null && listaServentiaCargos.size() > 0) {
			request.setAttribute("ListaServentiaCargos", listaServentiaCargos);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", audienciaNe.getQuantidadePaginas());
		} else {
			request.setAttribute("MensagemErro", "Dados não localizados.");
		}

	}

	/**
	 * Responsável por consultar os processos não julgados na Sessão 
	 * 
	 * @param request
	 * @param Id_Audiencia
	 * @param posicaoPaginaAtual
	 * @param audienciaSegundoGrauDt
	 * @param audienciaNe
	 * @param usuarioNe
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	protected boolean consultarProcessosNaoJulgadosSessao(HttpServletRequest request, String Id_Audiencia, String posicaoPaginaAtual, AudienciaSegundoGrauDt audienciaSegundoGrauDt, AudienciaNe audienciaNe, UsuarioNe usuarioNe) throws Exception{
		AudienciaDt audienciaCompleta = null;
		boolean retorno = false;
		if (Id_Audiencia != null && Id_Audiencia.trim().length() > 0){
			audienciaCompleta = audienciaNe.consultarAudienciaProcessosPendentes(Id_Audiencia, usuarioNe.getUsuarioDt());	
			
			if (audienciaCompleta != null && audienciaCompleta.possuiSessaoVinculada()) {
				request.setAttribute("Audienciadt", audienciaCompleta);
				retorno = true;
			} 
		} 		
		if (!retorno){
			request.setAttribute("MensagemErro", "Sessão não possui nenhum Processo vinculado.");
			this.consultarSessoesAbertas(request, posicaoPaginaAtual, audienciaSegundoGrauDt, audienciaNe, usuarioNe);			
		}
		return retorno;
	}
}