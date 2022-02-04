package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Utilities;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.CorreiosDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.PendenciaCorreiosDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PreAnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.ne.CorreiosNe;
import br.gov.go.tj.projudi.ne.PendenciaCorreiosNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

/**
 * Servlet responsável pela Busca de Processos.
 * Essa busca será usada pelos usuários internos (Analista, Promotores, Juizes...) e a função Editar será
 * utilizada para que todos os usuários tenham acesso aos dados do processo através dos links existentes em várias jsp's
 * 
 * @author msapaula
 * 
 */
public class DescartarPendenciaProcessoCt extends Controle {

	private static final long serialVersionUID = -8999834724334616406L;
	public static final int CodigoPermissao = 482;

	public int Permissao() {
		return DescartarPendenciaProcessoCt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoNe Processone;
		ProcessoDt processoDt = null;
		PendenciaDt pendenciaDt = null;
		ModeloDt modeloDt = null;

		String stAcao;
		String fluxo = "0";
		String tempFluxo1 = "0";
		String id_ProcessoDeprecado = "";
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma", "SituaçãoProcesso");
		request.setAttribute("tempRetorno", "DescartarPendenciaProcesso");
		request.setAttribute("TituloPagina", "Situação Processo");
		stAcao = "/WEB-INF/jsptjgo/ProcessoSituacaoPendencias.jsp";

		Processone = (ProcessoNe) request.getSession().getAttribute("BuscaProcessone");
		if (Processone == null) Processone = new ProcessoNe();
	
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");			
		
		
		pendenciaDt = (PendenciaDt) request.getSession().getAttribute("Pendenciadt");
		if (pendenciaDt == null) pendenciaDt = new PendenciaDt();
		
		modeloDt = (ModeloDt) request.getSession().getAttribute("modeloDt");
		if (modeloDt == null)
			modeloDt = new ModeloDt();
		
		if (request.getParameter("fluxo") != null)
			fluxo = request.getParameter("fluxo").toString();
		if (request.getParameter("id_ProcessoDeprecado") != null)
			id_ProcessoDeprecado = request.getParameter("id_ProcessoDeprecado").toString();

		if (request.getParameter("tempFluxo1") != null) {
			tempFluxo1 = request.getParameter("tempFluxo1").toString();
			request.setAttribute("tempFluxo1", tempFluxo1);
		}		

		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

		
		if (request.getParameter("PaginaAnterior") != null)
			Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		request.setAttribute("PaginaAnterior", paginaatual);
		
		//Variáveis para tratamento de opções "Resolver Conclusão" e "Movimentar Audiência" na tela de Situação do Processo
		request.setAttribute("podeAnalisar", UsuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao));
		request.setAttribute("podePreAnalisar", UsuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
		request.setAttribute("podeMovimentarAudiencia", UsuarioSessao.getVerificaPermissao(AudienciaMovimentacaoDt.CodigoPermissaoAudienciaProcesso));

		
		String id_Pendencia = ""; 
		String hash = "";
		
		switch (paginaatual) {
			
			case Configuracao.Curinga6:
				id_Pendencia = String.valueOf(request.getParameter("pendencia"));
				hash = String.valueOf(request.getParameter("CodigoPendencia"));
				
				if(id_Pendencia != null && id_Pendencia.trim().length() > 0) {
					if (UsuarioSessao.VerificarCodigoHash(id_Pendencia, hash)){
						Processone.finalizarPendenciaIntimacaoAguardandoParecer(id_Pendencia);
					} else {
						throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
					}
				} else {
					throw new MensagemException("Pendência não informada. Favor realizar o procedimento novamente.");
				}
				
				redireciona(response, "Usuario?PaginaAtual=-10");
				break;
				
			case Configuracao.Curinga7:
				id_Pendencia = String.valueOf(request.getParameter("pendencia"));
				hash = String.valueOf(request.getParameter("CodigoPendencia"));
				boolean finalizada = Boolean.valueOf(request.getParameter("finalizada"));
				
				if (UsuarioSessao.VerificarCodigoHash(id_Pendencia, hash)){
					Processone.marcarPendenciaIntimacaoAguardandoParecer(id_Pendencia, UsuarioSessao.getUsuarioDt(), finalizada);
					if (!finalizada)
						Processone.finalizarPendenciaIntimacaoAguardandoParecer(id_Pendencia);
				} else
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				
				redireciona(response, "Usuario?PaginaAtual=-10&MensagemOk=Operação Realizada com Sucesso!.");
				break;
				
			// Liberar acesso
			case Configuracao.Curinga8:
				if (processoDt != null && processoDt.getId() != null &&  !processoDt.getId().equals("")){
					
					if (request.getParameter("Peticionamento") != null && request.getParameter("Peticionamento").toString().equalsIgnoreCase("true")){
						
						//bloqueia o peticionamento nas CARTA PRECATÓRIA (criada a partir de pendências e não os processos em si) arquivadas
						if (processoDt != null && (processoDt.getDataArquivamento() != null && !processoDt.getDataArquivamento().equalsIgnoreCase("")) 
								&& (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA))
								|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPC))
								|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_CPP))
								|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_INFANCIA_JUVENTUDE))
								|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CARTA_PRECATORIA_INFRACIONAL)))){
							throw new MensagemException("Não é possível peticionar em Carta Precatória arquivada.");
						}
						
						//validação se pode peticionar em processo apenso
						if(processoDt.isApenso()){
							ProcessoNe processoNe = new ProcessoNe();
							if (!processoNe.podePeticionarProcessoApenso(processoDt.getId_Processo(), processoDt.getId_ProcessoPrincipal())){
								throw new MensagemException("Não é possível peticionar no processo ("+ processoDt.getProcessoNumero() +"). Faça o peticionamento no processo principal/origináio.");
							}
						}
						
						pendenciaDt = Processone.gerarPendenciaAguardandoParecer(UsuarioSessao.getUsuarioDt(), processoDt.getId());
						//this.executar(request, response, UsuarioSessao, Configuracao.LocalizarDWR, tempNomeBusca, PosicaoPaginaAtual);
						redireciona(response, "DescartarPendenciaProcesso?PaginaAtual=3&Id_Pendencia="+pendenciaDt.getId()+"&MensagemOk=Classifique a pendência de peticioanamento se necessário.");
						return;
						
					} if (request.getParameter("ElaboracaoVoto") != null && request.getParameter("ElaboracaoVoto").toString().equalsIgnoreCase("true")){
						String msgElaboracaoVoto = Processone.verificarElaboracaoVoto(UsuarioSessao.getUsuarioDt(), processoDt.getId());
						
						if (msgElaboracaoVoto != null && msgElaboracaoVoto.length()>0){
							redireciona(response, "BuscaProcesso?Id_Processo="+processoDt.getId() + "&MensagemErro=" + msgElaboracaoVoto);
						} else {
							pendenciaDt = Processone.gerarPendenciaElaboracaoVoto(UsuarioSessao.getUsuarioDt(), processoDt.getId());
							redireciona(response, "Usuario?PaginaAtual="+Configuracao.Cancelar+"&MensagemOk=Pedência de Elaboração de Voto Criada com Sucesso.");
						}
						return;
						
					} else {
						if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
							String mensagem = "Não é possível realizar esta ação. Motivo: Processo físico!";
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);
						} else {
							
							Processone.gerarPendeciaLiberarAcessoProcesso(UsuarioSessao.getUsuarioDt(), processoDt.getId(), processoDt.isSegredoJustica());
							request.getSession().setAttribute("AcessoOutraServentia", PendenciaTipoDt.LIBERACAO_ACESSO);
							request.getSession().setAttribute("ProcessoOutraServentia", processoDt.getId());
							redireciona(response, "BuscaProcesso?Id_Processo="+processoDt.getId() + "&MensagemOk=" + "A solicitação de acesso foi registrada, processo liberado.");
							//redireciona(response, "Usuario?PaginaAtual=-10");					
						} 
					}
				}
				break;
			
			// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					 					
					String stPermissao = String.valueOf((ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					atribuirJSON(request, "Id_NovoClassificador", "NovoClassificador", "Classificador", "DescartarPendenciaProcesso", Configuracao.LocalizarDWR, stPermissao, lisNomeBusca, lisDescricao);
					
					break;
				} else{
					String stTemp="";
					stTemp = Processone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
					
					enviarJSON(response, stTemp);
											
					return;								
				}
				
			case Configuracao.LocalizarDWR://alterar classificar pendencia
				id_Pendencia = String.valueOf(request.getParameter("Id_Pendencia"));
				if (id_Pendencia != null && !id_Pendencia.equalsIgnoreCase("null"))
					pendenciaDt = Processone.consultarPendencia(id_Pendencia, UsuarioSessao);
				if (request.getParameter("preAnalise") != null && String.valueOf(request.getParameter("preAnalise")).length()>0)
					pendenciaDt.setCodigoTemp(String.valueOf(request.getParameter("preAnalise")));
				
				
				if (UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())){
					
					if (request.getParameter("MensagemOk")!= null)
						request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
					
					stAcao = "/WEB-INF/jsptjgo/ClassificadorPendenciaAlterar.jsp";
					String id_NovoClassificador = "", novoClassificador = pendenciaDt.getClassificador();
					
					if (request.getParameter("Id_NovoClassificador") != null) id_NovoClassificador = request.getParameter("Id_NovoClassificador");
					if (request.getParameter("NovoClassificador") != null) novoClassificador = request.getParameter("NovoClassificador");
					
					request.setAttribute("Id_NovoClassificador", id_NovoClassificador);
					request.setAttribute("NovoClassificador", novoClassificador);

					if (fluxo != null && fluxo.equals("1")){
						if (id_NovoClassificador.length() > 0) {
							Processone.alterarClassificadorPendencia(pendenciaDt.getId(), pendenciaDt.getId_Classificador(), id_NovoClassificador, new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog()));
							//Volta para listagem de pendencias
							if (pendenciaDt.getCodigoTemp().length() > 0 
									&& pendenciaDt.getCodigoTemp().equalsIgnoreCase("true")){
								request.getSession().removeAttribute("Pendenciadt");
								redireciona(response, "PreAnalisarPendencia?PaginaAtual=6&tipo="+ pendenciaDt.getId_PendenciaTipo()+ "&MensagemOk=Classificador Atualizado com Sucesso.");
							
							} else if ( UsuarioSessao.isAssessorMP() || UsuarioSessao.isAssessorAdvogado()){
								request.getSession().removeAttribute("Pendenciadt");
								redireciona(response, "PreAnalisarPendencia?PaginaAtual=2&tipo="+ pendenciaDt.getId_PendenciaTipo()+ "&MensagemOk=Classificador Atualizado com Sucesso.");
									
							} else if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ADVOGADO
									||  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MP){
								request.getSession().removeAttribute("Pendenciadt");
								redireciona(response, "AnalisarPendencia?PaginaAtual=2&tipo="+ pendenciaDt.getId_PendenciaTipo()+ "&MensagemOk=Classificador Atualizado com Sucesso.");
							
							}  else if ( UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA
										 ||	UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU
										 ||  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.JUIZ_TURMA
										 ||  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
										 ||  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.PRESIDENCIA
										 ||  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU 
										 ||  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE 
										 ||  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO 
										 ||  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU){
								request.getSession().removeAttribute("Pendenciadt");
								if (tempFluxo1 != null && tempFluxo1.equals("1")) {
									redireciona(response, "AnalisarConclusao?PaginaAtual=2&tipo="+ pendenciaDt.getId_PendenciaTipo()+ "&MensagemOk=Classificador Atualizado com Sucesso.");	
								} else {
									redireciona(response, "PreAnalisarConclusao?PaginaAtual=6&tipo="+ pendenciaDt.getId_PendenciaTipo()+ "&MensagemOk=Classificador Atualizado com Sucesso.");	
								}																	
							}else if (UsuarioSessao.getUsuarioDt().isDistribuidorGabinete()){
										redireciona(response, "Usuario?PaginaAtual=-10"+ "&MensagemOk=Classificador Atualizado com Sucesso.");	

									}
							return;
						} else request.setAttribute("MensagemErro", "Altere o Classificador primeiramente.");

					}
				}
				break;
			
			case Configuracao.Curinga9:
				id_Pendencia = String.valueOf(request.getParameter("pendencia"));
				hash = String.valueOf(request.getParameter("CodigoPendencia"));
				if (UsuarioSessao.VerificarCodigoHash(id_Pendencia, hash)){
					pendenciaDt =  Processone.gerarPreAnalisePrecatoria(id_Pendencia, UsuarioSessao.getUsuarioDt());
				} else
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				
				redireciona(response, "PreAnalisarPendencia?PaginaAtual=4&Id_Pendencia="+pendenciaDt.getId());
				break;
			
			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (consultarModelo(request, tempNomeBusca, PosicaoPaginaAtual,	Processone, ArquivoTipoDt.CODIGO_ACESSO, Configuracao.Editar, UsuarioSessao)) {
					stAcao = "/WEB-INF/jsptjgo/ModeloArquivoTipoLocalizar.jsp";
				}
				break;
			
			case Configuracao.Imprimir:
				if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + "Não é possível executar essa ação. Motivo: Processo físico!");
				} else {
					String id_TipoArquivoCodigoAcesso = Processone.consultarArquivoTipoCodigo(ArquivoTipoDt.CODIGO_ACESSO);
					modeloDt = Processone.consultarModeloCodigoAcesso(id_TipoArquivoCodigoAcesso);
					
				    String id_parte = (String)  request.getParameter("id_Parte");
				    modeloDt = this.montaModelo(request, UsuarioSessao, Processone,  modeloDt, processoDt, id_parte);
				    
				    byte[] arquivoPDF = ConverterHtmlPdf.converteHtmlPDF(modeloDt.getTexto().getBytes(), false);
				    					
					enviarPDF(response,arquivoPDF,"CodigoAcesso");
																																									
					return;
				}
				break;

			case Configuracao.Salvar:
				String[] ids_PendenciasConfirmar = request.getParameterValues("chk");

				if (ids_PendenciasConfirmar != null && ids_PendenciasConfirmar.length > 0) {
					request.setAttribute("Mensagem", "Clique em Confirmar para descartar a pendência.");
					request.setAttribute("Ids_Pendencias", ids_PendenciasConfirmar);
					request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);

				} else{
					request.setAttribute("MensagemErro", "Nenhuma pendência selecionada.");
					request.setAttribute("PaginaAtual", Configuracao.Salvar);
				}				
				
				if((Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU ||
					Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL)	
						&& processoDt.getId_Serventia().equalsIgnoreCase(UsuarioSessao.getUsuarioDt().getId_Serventia())){
					if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU) {
						request.setAttribute("HabilitadoTrocarResponsavelConclusao", "true");
						request.setAttribute("HabilitadoTrocarResponsavelVotoEmenta", "true");
					}					
					request.setAttribute("podeVisualizarVotoEmenta", "true");
					//Consultar voto/ementa em aberto
					List votoEmentaPendentes = Processone.consultarVotoEmentaPendentesProcessoHash(processoDt.getId(), UsuarioSessao);
					request.setAttribute("VotoEmentaPendente", votoEmentaPendentes);
				}

				this.consultarSituacaoProcesso(request, processoDt, Processone, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/ProcessoSituacaoPendencias.jsp";
				break;

			case Configuracao.SalvarResultado:
				String[] ids_Pendencias = request.getParameterValues("chk");

				if (ids_Pendencias != null && ids_Pendencias.length > 0) {
					Processone.descartarPendencias(ids_Pendencias, UsuarioSessao);
					request.setAttribute("MensagemOk", ids_Pendencias.length + " Pendência(s) descartada(s) com sucesso.");
				} else request.setAttribute("MensagemErro", "Nenhuma pendência selecionada.");
				
				if((Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU  ||
					Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL)
						&& processoDt.getId_Serventia().equalsIgnoreCase(UsuarioSessao.getUsuarioDt().getId_Serventia())){
					if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU) {
						request.setAttribute("HabilitadoTrocarResponsavelConclusao", "true");
						request.setAttribute("HabilitadoTrocarResponsavelVotoEmenta", "true");
					}					
					request.setAttribute("podeVisualizarVotoEmenta", "true");
					//Consultar voto/ementa em aberto
					List votoEmentaPendentes = Processone.consultarVotoEmentaPendentesProcessoHash(processoDt.getId(), UsuarioSessao);
					request.setAttribute("VotoEmentaPendente", votoEmentaPendentes);
				}

				this.consultarSituacaoProcesso(request, processoDt, Processone, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/ProcessoSituacaoPendencias.jsp";
				request.setAttribute("PaginaAtual", Configuracao.Salvar);
				break;
			
			case Configuracao.Localizar:
				
				if (processoDt.isProcessoPrecatoriaExpedidaOnline()) {
				
					if (fluxo != null && fluxo.equals("1"))
						stAcao = "/WEB-INF/jsptjgo/ListaArquivosProcessoPrecatoria.jsp";
					else if (fluxo != null && fluxo.equals("2"))
						stAcao = "/WEB-INF/jsptjgo/ListaArquivosProcessoPrecatoriaJuntada.jsp";
					
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("fluxo", fluxo);
					
					request.setAttribute("id_ProcessoDeprecado", processoDt.getId());
					request.getSession().setAttribute("processoDt", processoDt);
					
					if (fluxo != null && fluxo.equals("3")){
						// MONTA LISTA DE ARQUIVOS PARA DEVOLVER PRECATÓRIA *************************
						String[] codigosArquivos = request.getParameterValues("chk2");
						String statusPrecatoria = request.getParameter("status").toString();
						
						if (codigosArquivos != null && statusPrecatoria.length()>0) {
						
							if (codigosArquivos != null && codigosArquivos.length > 0){
								List arquivosTransferencia = new ArrayList();
								Map arquivosTransferenciaMap = new HashMap();
								
								for (int i=0; i < codigosArquivos.length; i++){
									ArquivoDt arquivoDt = Processone.consultarArquivoPendenciaId(codigosArquivos[i]);
									arquivosTransferencia.add(arquivoDt);
									arquivosTransferenciaMap.put(arquivoDt.getId(), arquivoDt);
								}
								
								request.getSession().setAttribute("arquivosTransferencia",Funcoes.converterListParaSet(arquivosTransferencia));
								request.getSession().setAttribute("arquivosTransferenciaMap",arquivosTransferenciaMap);
								request.getSession().setAttribute("ProcessoPrecatoria", "DevolverProcessoPrecatoria");
								request.getSession().setAttribute("ComplementoDevolucaoPrecatoria", statusPrecatoria);
								
								if (id_ProcessoDeprecado.equalsIgnoreCase(processoDt.getId()))
									redireciona(response, "Movimentacao?PaginaAtual=" + Configuracao.Novo +(!processoDt.getId().equals("")?"&Id_Processo="+processoDt.getId():""));
								else
									throw new MensagemException("Erro: Mais de um processo na seção.");
							}
		
						} else {
							stAcao = "/WEB-INF/jsptjgo/ListaArquivosProcessoPrecatoria.jsp";
							String mensagem = "";
							if (statusPrecatoria.length() == 0)
								mensagem += "Selecione o Status de devolução da Precatória. \n";
							if (codigosArquivos == null)
								mensagem += "Nenhum Arquivo Selecionado. \n";
							request.setAttribute("MensagemErro", mensagem);
						}
						
					} else if (fluxo != null && fluxo.equals("4")){
						// MONTA LISTA DE ARQUIVOS PARA ENCAMINHAR OFICIO PRECATÓRIA *************************
						String[] codigosArquivos = request.getParameterValues("chk2");
						
						if (codigosArquivos != null) {
						
							if (codigosArquivos != null && codigosArquivos.length > 0){
								List arquivosTransferencia = new ArrayList();
								Map arquivosTransferenciaMap = new HashMap();
								
								for (int i=0; i < codigosArquivos.length; i++){
									ArquivoDt arquivoDt = Processone.consultarArquivoPendenciaId(codigosArquivos[i]);
									arquivosTransferencia.add(arquivoDt);
									arquivosTransferenciaMap.put(arquivoDt.getId(), arquivoDt);
								}
								
								request.getSession().setAttribute("arquivosTransferencia",Funcoes.converterListParaSet(arquivosTransferencia));
								request.getSession().setAttribute("arquivosTransferenciaMap",arquivosTransferenciaMap);
								request.getSession().setAttribute("ProcessoPrecatoria", "DevolverDocumentoPrecatoria");
								request.getSession().setAttribute("ComplementoDevolucaoPrecatoria", "Carta Precatória");
								
								if (id_ProcessoDeprecado.equalsIgnoreCase(processoDt.getId()))
									redireciona(response, "Movimentacao?PaginaAtual=" + Configuracao.Novo +(!processoDt.getId().equals("")?"&Id_Processo="+processoDt.getId():""));
								else
									throw new MensagemException("Erro: Mais de um processo na seção.");
							}
		
						} else {
							stAcao = "/WEB-INF/jsptjgo/ListaArquivosProcessoPrecatoriaJuntada.jsp";
							String mensagem = "";
							if (codigosArquivos == null)
								mensagem += "Nenhum Arquivo Selecionado. \n";
							request.setAttribute("MensagemErro", mensagem);
						}
					}
					
				} else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=O processo não é do tipo Precatória!");
					return;
				}
				
				break;
				
			case Configuracao.Novo:
				if (processoDt.getId_ProcessoPrincipal() != null 
						&& processoDt.getId_ProcessoPrincipal().length() > 0) {
				
					if (fluxo != null && fluxo.equals("1"))
						stAcao = "/WEB-INF/jsptjgo/ListaArquivosProcessoDependenteJuntada.jsp";
					
					request.setAttribute("PaginaAtual", Configuracao.Novo);
					request.setAttribute("fluxo", fluxo);
					
					request.setAttribute("id_ProcessoDeprecado", processoDt.getId());
					request.getSession().setAttribute("processoDt", processoDt);
					
					if (fluxo != null && fluxo.equals("3")){
						
						// MONTA LISTA DE ARQUIVOS PARA ENCAMINHAR OFICIO PRECATÓRIA *************************
						String[] codigosArquivos = request.getParameterValues("chk2");
						
						if (codigosArquivos != null) {
						
							if (codigosArquivos != null && codigosArquivos.length > 0){
								List arquivosTransferencia = new ArrayList();
								Map arquivosTransferenciaMap = new HashMap();
								
								for (int i=0; i < codigosArquivos.length; i++){
									ArquivoDt arquivoDt = Processone.consultarArquivoPendenciaId(codigosArquivos[i]);
									arquivosTransferencia.add(arquivoDt);
									arquivosTransferenciaMap.put(arquivoDt.getId(), arquivoDt);
								}
								
								request.getSession().setAttribute("arquivosTransferencia",Funcoes.converterListParaSet(arquivosTransferencia));
								request.getSession().setAttribute("arquivosTransferenciaMap",arquivosTransferenciaMap);
								request.getSession().setAttribute("ProcessoPrecatoria", "DevolverDocumentoOficio");
								request.getSession().setAttribute("ComplementoDevolucaoPrecatoria", "Ofício Comunicatório");
								
								if (id_ProcessoDeprecado.equalsIgnoreCase(processoDt.getId()))
									redireciona(response, "Movimentacao?PaginaAtual=" + Configuracao.Novo +(!processoDt.getId().equals("")?"&Id_Processo="+processoDt.getId():""));
								else
									throw new MensagemException("Erro: Mais de um processo na seção.");
							}
		
						} else {
							stAcao = "/WEB-INF/jsptjgo/ListaArquivosProcessoDependenteJuntada.jsp";
							String mensagem = "";
							if (codigosArquivos == null)
								mensagem += "Nenhum Arquivo Selecionado. \n";
							request.setAttribute("MensagemErro", mensagem);
						}
					}
					
				} else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=O processo não possui processo dependente.");
					return;
				}
				break;
				
			default:
				stAcao = "/WEB-INF/jsptjgo/CodigoAcesso.jsp";
			    String id_Parte = (String) request.getSession().getAttribute("id_parteCodigoAcesso"); 
			    modeloDt = this.montaModelo(request, UsuarioSessao, Processone,  modeloDt, processoDt, id_Parte);
				request.setAttribute("TextoEditor", modeloDt != null?modeloDt.getTexto():"");
				request.getSession().setAttribute("modeloDt", modeloDt);
				break;
		}

		request.getSession().setAttribute("Pendenciadt", pendenciaDt);
		request.getSession().setAttribute("BuscaProcessone", Processone);
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected ModeloDt montaModelo(HttpServletRequest request, UsuarioNe UsuarioSessao, ProcessoNe processoNe, ModeloDt modeloDt, ProcessoDt processo, String id_Parte)throws Exception {
		if (request.getParameter("Id_Modelo") != null)
			if (request.getParameter("Id_Modelo").equalsIgnoreCase("null")){
				modeloDt.setId("");
		        modeloDt.setModelo("");
		        modeloDt.limpar();
			} else {
				modeloDt.setId( request.getParameter("Id_Modelo") );
			    modeloDt.setModelo(request.getParameter("Modelo"));
			}
		if (modeloDt.getId() != null && modeloDt.getId().equals("") == false)
			modeloDt = processoNe.consultarModeloId( modeloDt.getId(),UsuarioSessao, processo, id_Parte);
		return modeloDt;
	}
	
	protected boolean consultarModelo(HttpServletRequest request,	String tempNomeBusca, String posicaoPaginaAtual, ProcessoNe processoNe, int codigoArquivoTipo, int paginaatual,	UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;

		String tipoArquivoCertidao = processoNe.consultarArquivoTipoCodigo(codigoArquivoTipo);
		List tempList = processoNe.consultarModelo(usuarioSessao.getUsuarioDt(), tipoArquivoCertidao, tempNomeBusca, posicaoPaginaAtual);
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaModelo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Long
					.valueOf(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_Modelo", "Id_Modelo");
			request.setAttribute("tempBuscaModelo", "Modelo");
			boRetorno = true;
		} else
			request.setAttribute("MensagemErro", "Nenhum Modelo foi localizado.");
		return boRetorno;
	}

	/**
	 * Método responsável em consultar a situação completa do processo.
	 * Retorna dados referentes a pendências, conclusões e audiências em aberto no processo.
	 * 
	 * @param request
	 * @param processoDt
	 * @param processoNe
	 * @throws Exception
	 */
	protected void consultarSituacaoProcesso(HttpServletRequest request, ProcessoDt processoDt, ProcessoNe processoNe, UsuarioNe UsuarioSessao) throws Exception{

		//Consultar pendências em aberto ou não vistadas
		List[] listaPendencias = processoNe.consultarPendenciasProcessoHash(processoDt.getId(), UsuarioSessao);
		request.setAttribute("ListaPendencias", listaPendencias);

		//Consultar conclusões em aberto
		List conclusoesPendentes = processoNe.consultarConclusoesPendentesProcessoHash(processoDt.getId(), UsuarioSessao);
		request.setAttribute("ConclusaoPendente", conclusoesPendentes);

		//Consultar audiências em aberto
		List<String[]> audienciaPendente = processoNe.consultarAudienciasPendentes(processoDt.getId(), false);
		request.setAttribute("AudienciaPendente", audienciaPendente);

	}
	

}
