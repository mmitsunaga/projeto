package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AssinarSessaoSegundoGrauDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaSegundoGrauDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.SessaoSegundoGrauProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaArquivoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet para controlar as assinaturas das pré-analises dos relátorios, votos e ementas das sessões de segundo grau, após opção de "Guardar para assinar".
 * 
 * @author mmgomes
 */
public class AssinarSessaoSegundoGrauCt extends Controle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -628294558178559525L;
	
	private static final String CHAVE_SESSAO_ACORDAO_EMENTA_ASSINATURA = "CHAVE_SESSAO_ACORDAO_EMENTA_ASSINATURA";

	public int Permissao() {
		return AnaliseConclusaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AssinarSessaoSegundoGrauDt assinarSessaoSegundoGrauDt;
		AudienciaNe audienciaNe;
				
		int passoEditar = -1;		
		String audienciasProcesso[] = null;
		String stAcao = "/WEB-INF/jsptjgo/AssinarSessaoSegundoGrauLocalizar.jsp";
		
		request.setAttribute("tempPrograma", "Sessão Segundo Grau");
		request.setAttribute("tempRetorno", "AssinarSessaoSegundoGrau");
		
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
			
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else if (request.getAttribute("MensagemOk") != null) request.setAttribute("MensagemOk", request.getAttribute("MensagemOk"));
		else request.setAttribute("MensagemOk", "");

		if (request.getAttribute("MensagemErro") != null) request.setAttribute("MensagemErro", request.getAttribute("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		request.setAttribute("PassoEditar", passoEditar);

		audienciaNe = (AudienciaNe) request.getSession().getAttribute("AudienciaNe");
		if (audienciaNe == null) audienciaNe = new AudienciaNe();

		assinarSessaoSegundoGrauDt = (AssinarSessaoSegundoGrauDt) request.getSession().getAttribute("AssinarSessaoSegundoGrauDt");
		if (assinarSessaoSegundoGrauDt == null) assinarSessaoSegundoGrauDt = new AssinarSessaoSegundoGrauDt();
		
		assinarSessaoSegundoGrauDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		assinarSessaoSegundoGrauDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		long acaoSalvar = 0; 
		if (request.getParameter("acaoSalvar") != null) acaoSalvar = Funcoes.StringToLong(String.valueOf(request.getParameter("acaoSalvar")));			
		if (acaoSalvar> 0)	assinarSessaoSegundoGrauDt.setAcaoAssinatura((acaoSalvar == Configuracao.Curinga6));

		request.setAttribute("TituloPagina", "Assinar Relatórios, Votos e Ementas");
		
		//----------------------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {		
		
			// Solicitar assinaturas ou confirmação de descarte de assinatura
			case Configuracao.Salvar:
				
				if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE &&
					UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO &&
				    UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA &&
					UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR &&
					UsuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao)) {
					
					// Captura as pendências que serão assinadas
					if (request.getParameterValues("audienciasProcesso") != null) {
						audienciasProcesso = request.getParameterValues("audienciasProcesso");
					} else if (request.getParameterValues("pendencias") != null) {
						audienciasProcesso = request.getParameterValues("pendencias");
					} else if (request.getParameter("Id_AudienciaProcesso") != null && 
							   !request.getParameter("Id_AudienciaProcesso").equals("")) {
						audienciasProcesso = new String[] {request.getParameter("Id_AudienciaProcesso") };
					} else if (request.getParameter("id_pendencia") != null && 
							   !request.getParameter("id_pendencia").equals("")) {
						audienciasProcesso = new String[] {request.getParameter("id_pendencia") };						
					}	
					
					
					//Quando trata de despacho múltiplo
					if (audienciasProcesso != null) {
						assinarSessaoSegundoGrauDt.atualizeListaSessoesSelecionadas(audienciasProcesso);
						if (assinarSessaoSegundoGrauDt.isAcaoAssinatura()){
							prepareDadosAssinatura(assinarSessaoSegundoGrauDt, audienciaNe, request, UsuarioSessao, audienciasProcesso);					
							request.setAttribute("Mensagem", "Clique para confirmar a assinatura.");
							request.setAttribute("ListaSessoes", assinarSessaoSegundoGrauDt.getListaSessoesSegundoGrau());
							stAcao = "/WEB-INF/jsptjgo/AssinarSessaoSegundoGrauConfirmacao.jsp";
						}else{
							request.setAttribute("Mensagem", "Clique para confirmar o descarte do status 'Aguardando assinatura', alterando para o status 'Pré-analisadas'.");
							request.setAttribute("ListaSessoes", assinarSessaoSegundoGrauDt.getListaSessoesSegundoGrau());
							stAcao = "/WEB-INF/jsptjgo/AssinarSessaoSegundoGrauDescarteConfirmacao.jsp";
						}		
											
					}else{
						request.setAttribute("MensagemErro", "Nenhuma Pré-Análise foi selecionada.");					
						this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
						return;					
					}
				} else {
					if (assinarSessaoSegundoGrauDt.isAcaoAssinatura())
						request.setAttribute("MensagemErro", "Usuário sem permissão para assinar.");
					else 
						request.setAttribute("MensagemErro", "Usuário sem permissão para retornar para pré-análise.");
					this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
					return;	
				}
				
				break;

			// Confirmar assinatura ou descarta as pendências de assinatura
			case Configuracao.SalvarResultado:
				if (assinarSessaoSegundoGrauDt.isAcaoAssinatura()){
					if (!salvarAnaliseSessaoSegundoGrau(assinarSessaoSegundoGrauDt, audienciaNe, request, UsuarioSessao)){						
						this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
						return;	
					}
				} else {
					audienciaNe.descarteStatusPreAnalisesConclusaoAguardandoAssinatura(assinarSessaoSegundoGrauDt);
					request.setAttribute("MensagemOk", "Descarte efetuado com sucesso.");
				}				
				this.consultarSessoesPendentes(request, assinarSessaoSegundoGrauDt, audienciaNe, PosicaoPaginaAtual, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/AssinarSessaoSegundoGrauLocalizar.jsp";
				break;

			// Função para consultar todas as sessões que estão pendentes de assinatura
			case Configuracao.Localizar:
				this.consultarSessoesPendentes(request, assinarSessaoSegundoGrauDt, audienciaNe, PosicaoPaginaAtual, UsuarioSessao);				
				stAcao = "/WEB-INF/jsptjgo/AssinarSessaoSegundoGrauLocalizar.jsp";
				break;
				
			case Configuracao.Curinga6:
				//prepararAnaliseSessaoSegundoGrauAssincrona(assinarSessaoSegundoGrauDt, request, response);
				if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE &&
					UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO &&
				    UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA &&
					UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR &&
					UsuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao)) {
					
					// Captura as sessões que serão assinadas
					if (request.getParameterValues("audienciasProcesso") != null) {
						audienciasProcesso = request.getParameterValues("audienciasProcesso");
					} else if (request.getParameterValues("pendencias") != null) {
						audienciasProcesso = request.getParameterValues("pendencias");
					} else if (request.getParameter("Id_AudienciaProcesso") != null && 
							   !request.getParameter("Id_AudienciaProcesso").equals("")) {
						audienciasProcesso = new String[] {request.getParameter("Id_AudienciaProcesso") };
					} else if (request.getParameter("id_pendencia") != null && 
							   !request.getParameter("id_pendencia").equals("")) {
						audienciasProcesso = new String[] {request.getParameter("id_pendencia") };						
					}	
					
					if (audienciasProcesso != null) {						
						List<SessaoSegundoGrauProcessoDt> listasDeSessoesSegundoGrau = assinarSessaoSegundoGrauDt.getListaSessoesSegundoGrau(audienciasProcesso);
						
						// Captura a senha do certificado
						String senhaCertificado = (String) request.getParameter("senha");
						boolean salvarSenha =  Boolean.parseBoolean(request.getParameter("salvarSenha"));
						
						//verifico se o certificado ja está carregado
						if(!UsuarioSessao.isCertificadoCarregado()){
							//se não carrego o certificado
							UsuarioSessao.carregarCertificado();
						} 
						
						//nesse ponto o certificado já foi carregado pefeitamente, ou uma exception foi lançada
						if (senhaCertificado != null &&  !senhaCertificado.isEmpty()) {
							UsuarioSessao.setSenhaCertificado(senhaCertificado);
						}
						
						String mensagemRetorno = "";
						String mensagem = "";
										
						for (SessaoSegundoGrauProcessoDt sessaoSegundoGrauProcessoDt : listasDeSessoesSegundoGrau) {
							AudienciaMovimentacaoDt audienciaMovimentacaoDt = audienciaNe.obtenhaAudienciaMovimentacaoPeloIdAudienciaProcesso(request, sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt().getId(), UsuarioSessao, null, null, false);
							
							if (audienciaMovimentacaoDt.getListaArquivos() == null) {
								audienciaMovimentacaoDt.setListaArquivos(new ArrayList());
							} else {
								audienciaMovimentacaoDt.getListaArquivos().clear();
							}
							
							// Assinatura do voto / ementa. Início...
							ArquivoDt preAnaliseVoto = new ArquivoDt();
							preAnaliseVoto.setId("");
							preAnaliseVoto.setDataInsercao("");
							preAnaliseVoto.setContentType("text/html");
							preAnaliseVoto.setNomeArquivo(audienciaMovimentacaoDt.getNomeArquivo());							
							preAnaliseVoto.setArquivo(audienciaMovimentacaoDt.getTextoEditor());
							preAnaliseVoto.setId_ArquivoTipo(audienciaMovimentacaoDt.getId_ArquivoTipo());
							preAnaliseVoto.setArquivoTipo(audienciaMovimentacaoDt.getArquivoTipo());
							preAnaliseVoto.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.RELATORIO_VOTO));
							preAnaliseVoto.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
							preAnaliseVoto.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
							
							//assino o arquivo e incluo o assinante e preparo o .p7s
							UsuarioSessao.assinarByte(preAnaliseVoto);
							
							audienciaMovimentacaoDt.getListaArquivos().add(preAnaliseVoto);
							
							ArquivoDt preAnaliseEmenta = new ArquivoDt();
							preAnaliseEmenta.setId("");
							preAnaliseEmenta.setDataInsercao("");
							preAnaliseEmenta.setContentType("text/html");
							preAnaliseEmenta.setNomeArquivo(audienciaMovimentacaoDt.getNomeArquivoEmenta());							
							preAnaliseEmenta.setArquivo(audienciaMovimentacaoDt.getTextoEditorEmenta());
							preAnaliseEmenta.setId_ArquivoTipo(audienciaMovimentacaoDt.getId_ArquivoTipoEmenta());
							preAnaliseEmenta.setArquivoTipo(audienciaMovimentacaoDt.getArquivoTipoEmenta());
							preAnaliseEmenta.setArquivoTipoCodigo(String.valueOf(ArquivoTipoDt.EMENTA));
							preAnaliseEmenta.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
							preAnaliseEmenta.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
							
							//assino o arquivo e incluo o assinante e preparo o .p7s
							UsuarioSessao.assinarByte(preAnaliseEmenta);
							
							audienciaMovimentacaoDt.getListaArquivos().add(preAnaliseEmenta);							
							// Assinatura do voto / ementa. Fim...
							
							mensagem = audienciaNe.verificarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());
							if (mensagem.length() == 0) {
								audienciaMovimentacaoDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
								audienciaMovimentacaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
								mensagem = audienciaNe.podeMovimentarAudiencia(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), UsuarioSessao.getUsuarioDt().getId_Serventia());

								if (mensagem.length() == 0) {						
									if (Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() 
											&& (UsuarioSessao.getUsuarioDt().isSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteUPJ())) {
										mensagem = audienciaNe.salvarMovimentacaoAudienciaProcessoSessaoSegundoGrau(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());						
									} else {
										mensagem = "A sessão não é do tipo segundo grau";
									}											
								} 
							} 
							
							if (mensagem.length() > 0) {
								if (audienciaMovimentacaoDt != null &&
									audienciaMovimentacaoDt.getAudienciaDt() != null &&
									audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt() != null) {
									
									if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoNumero() != null &&
										audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoNumero().trim().length() > 0)
									{
										mensagem = "\nProcesso: " + Funcoes.formataNumeroProcesso(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoNumero()) + " - Motivo: " + mensagem;	
									} 
									else if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt() != null &&
											audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getProcessoNumero() != null &&
											audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getProcessoNumero().trim().length() > 0) 
									{
										mensagem = "\nProcesso: " + Funcoes.formataNumeroProcesso(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getProcessoNumero()) + " - Motivo: " + mensagem;
									}
								}								
								if (mensagemRetorno.trim().length() > 0) {
									mensagemRetorno += "\n";				
								}
								mensagemRetorno += mensagem;
								mensagem = "";
							}						
							
						}	
						
						//se não é para salvar o cetificado eu limpo
						if (!salvarSenha) {
							UsuarioSessao.setSenhaCertificado("");
						}
						
						if (mensagemRetorno != null && mensagemRetorno.length() > 0) {
							throw new MensagemException( mensagemRetorno);
						}
						
					} else {
						throw new MensagemException("Nenhuma Pré-Análise foi selecionada.");
					}
				} else {
					request.setAttribute("MensagemErro", "Usuário sem permissão para executar essa ação.");
					this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
				}				
				return;	
			
			case Configuracao.Curinga7:
				//salvarPrimeiraAnaliseSessaoSegundoGrauListaAssincrona(audienciaNe, request, UsuarioSessao, response);
				this.executar(request, response, UsuarioSessao, Configuracao.Localizar, tempNomeBusca, PosicaoPaginaAtual);
				return;	
		}
		
		if (!(UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE &&
			  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO &&
			  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA &&
			  UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR &&
			  UsuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao))) {
			stAcao = "/WEB-INF/jsptjgo/AssinarSessaoSegundoGrauLocalizarAssessor.jsp";
		}	

		request.getSession().setAttribute("AssinarSessaoSegundoGrauDt", assinarSessaoSegundoGrauDt);
		request.getSession().setAttribute("AudienciaNe", audienciaNe);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	private void consultarSessoesPendentes(HttpServletRequest request, AssinarSessaoSegundoGrauDt assinarSessaoSegundoGrauDt, AudienciaNe audienciaNe, String posicaoPaginaAtual, UsuarioNe UsuarioSessao) throws Exception{
		List tempList = audienciaNe.consultarSessoesPendentesPendentesAcordao(UsuarioSessao.getUsuarioDt(), null, true, true);
		tempList.addAll(audienciaNe.consultarSessoesPendentesPendentesAcordao(UsuarioSessao.getUsuarioDt(), null, true, false));
		SessaoSegundoGrauProcessoDt sessaoSegundoGrauProcessoDt = null;		
		String Id_ServentiaCargo = UsuarioSessao.getUsuarioDt().getId_ServentiaCargo();
				
		if ((UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)) 
			Id_ServentiaCargo = UsuarioSessao.getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
		
		assinarSessaoSegundoGrauDt.getListaSessoesSegundoGrau().clear();
		AssinarSessaoSegundoGrauDt assinarSessaoSegundoGrauDtDescarte = new AssinarSessaoSegundoGrauDt();
		assinarSessaoSegundoGrauDtDescarte.setId_UsuarioLog(assinarSessaoSegundoGrauDt.getId_UsuarioLog());
		assinarSessaoSegundoGrauDtDescarte.setIpComputadorLog(assinarSessaoSegundoGrauDt.getIpComputadorLog());
		
		if (tempList.size() > 0) {
			String mensagemDescarte = "";
			
			//Atualiza as pré-análises e rash para consulta
			for (Object audienciaDtObj : tempList) {
				
				AudienciaDt audienciaDt = (AudienciaDt)audienciaDtObj;
				
				if(audienciaDt.getListaAudienciaProcessoDt() != null && audienciaDt.getListaAudienciaProcessoDt().size() > 0) {
					for (int i = 0; i < audienciaDt.getListaAudienciaProcessoDt().size(); i++) {
						AudienciaProcessoDt audienciaProcessoDt = (AudienciaProcessoDt)audienciaDt.getListaAudienciaProcessoDt().get(i);
						
						if (audienciaProcessoDt == null) continue;						
						
						audienciaProcessoDt.setAudienciaDt(audienciaDt);						
													
						sessaoSegundoGrauProcessoDt = new SessaoSegundoGrauProcessoDt();
						sessaoSegundoGrauProcessoDt.setAudienciaProcessoDt(audienciaProcessoDt);
						
						PendenciaArquivoDt pendenciaArquivoDtVoto = null;
						PendenciaArquivoDt pendenciaArquivoDtEmenta = null;
						
						if (audienciaProcessoDt.possuiVotoOuEmentaRelator()) {
							pendenciaArquivoDtVoto =  audienciaNe.consultarVotoDesembargadorPendencia(Id_ServentiaCargo, audienciaProcessoDt.getId(), audienciaProcessoDt.getId_PendenciaVotoRelator());					
							pendenciaArquivoDtEmenta = audienciaNe.consultarEmentaDesembargadorEmentaPendencia(Id_ServentiaCargo, audienciaProcessoDt.getId(), audienciaProcessoDt.getId_PendenciaEmentaRelator());
						
							PendenciaNe pendenciaNe = new PendenciaNe();
							if (pendenciaArquivoDtVoto == null) {
								PendenciaDt pendenciaVotoDtDescartada = pendenciaNe.consultarFinalizadaId(audienciaProcessoDt.getId_PendenciaVotoRelator());
								if (pendenciaVotoDtDescartada != null) {
									pendenciaNe.reaberturaAutomaticaPendencia(pendenciaVotoDtDescartada);
									pendenciaArquivoDtVoto =  audienciaNe.consultarVotoDesembargadorPendencia(Id_ServentiaCargo, audienciaProcessoDt.getId(), audienciaProcessoDt.getId_PendenciaVotoRelator());				
									
								}
							}
							if (pendenciaArquivoDtEmenta == null) {
								PendenciaDt pendenciaEmentaDtDescartada = pendenciaNe.consultarFinalizadaId(audienciaProcessoDt.getId_PendenciaEmentaRelator());
								if (pendenciaEmentaDtDescartada != null) {
									pendenciaNe.reaberturaAutomaticaPendencia(pendenciaEmentaDtDescartada);
									pendenciaArquivoDtEmenta = audienciaNe.consultarEmentaDesembargadorEmentaPendencia(Id_ServentiaCargo, audienciaProcessoDt.getId(), audienciaProcessoDt.getId_PendenciaEmentaRelator());
								}
							}							
						}
						
						if (pendenciaArquivoDtVoto == null && 
							pendenciaArquivoDtEmenta == null &&
							audienciaProcessoDt.possuiVotoEEmentaRedator()) {
							pendenciaArquivoDtVoto =  audienciaNe.consultarVotoDesembargadorPendencia(Id_ServentiaCargo, audienciaProcessoDt.getId(), audienciaProcessoDt.getId_PendenciaVotoRedator());					
							pendenciaArquivoDtEmenta = audienciaNe.consultarEmentaDesembargadorEmentaPendencia(Id_ServentiaCargo, audienciaProcessoDt.getId(), audienciaProcessoDt.getId_PendenciaEmentaRedator());
						
							PendenciaNe pendenciaNe = new PendenciaNe();
							if (pendenciaArquivoDtVoto == null) {
								PendenciaDt pendenciaVotoDtDescartada = pendenciaNe.consultarFinalizadaId(audienciaProcessoDt.getId_PendenciaVotoRedator());
								if (pendenciaVotoDtDescartada != null) {
									pendenciaNe.reaberturaAutomaticaPendencia(pendenciaVotoDtDescartada);
									pendenciaArquivoDtVoto =  audienciaNe.consultarVotoDesembargadorPendencia(Id_ServentiaCargo, audienciaProcessoDt.getId(), audienciaProcessoDt.getId_PendenciaVotoRedator());				
									
								}
							}
							if (pendenciaArquivoDtEmenta == null) {
								PendenciaDt pendenciaEmentaDtDescartada = pendenciaNe.consultarFinalizadaId(audienciaProcessoDt.getId_PendenciaEmentaRedator());
								if (pendenciaEmentaDtDescartada != null) {
									pendenciaNe.reaberturaAutomaticaPendencia(pendenciaEmentaDtDescartada);
									pendenciaArquivoDtEmenta = audienciaNe.consultarEmentaDesembargadorEmentaPendencia(Id_ServentiaCargo, audienciaProcessoDt.getId(), audienciaProcessoDt.getId_PendenciaEmentaRedator());
								}
							}	
						}
													
						if (pendenciaArquivoDtVoto != null && pendenciaArquivoDtEmenta != null) {						
							
							pendenciaArquivoDtVoto.setHash(UsuarioSessao.getCodigoHash(pendenciaArquivoDtVoto.getId()));
							pendenciaArquivoDtEmenta.setHash(UsuarioSessao.getCodigoHash(pendenciaArquivoDtEmenta.getId()));
							
							sessaoSegundoGrauProcessoDt.setPendenciaArquivoDtRelatorioEVoto(pendenciaArquivoDtVoto);
							sessaoSegundoGrauProcessoDt.setPendenciaArquivoDtEmenta(pendenciaArquivoDtEmenta);
							
							assinarSessaoSegundoGrauDt.adicioneListaSessoesSegundoGrau(sessaoSegundoGrauProcessoDt);
						} else {
							if (pendenciaArquivoDtVoto != null) {
								sessaoSegundoGrauProcessoDt.setPendenciaArquivoDtRelatorioEVoto(pendenciaArquivoDtVoto);	
							}
							if (pendenciaArquivoDtEmenta != null) {
								sessaoSegundoGrauProcessoDt.setPendenciaArquivoDtEmenta(pendenciaArquivoDtEmenta);	
							}
							
							assinarSessaoSegundoGrauDtDescarte.adicioneListaSessoesSegundoGrau(sessaoSegundoGrauProcessoDt);
							if (mensagemDescarte.length() > 0) mensagemDescarte += "\n";
							mensagemDescarte += "O processo " + audienciaProcessoDt.getProcessoDt().getProcessoNumero()  + " retornou para os pré-analisados, pois o texto do acórdão ou da ementa não foi localizado.";	
						}						
					}
				}
			}
			
			if (assinarSessaoSegundoGrauDtDescarte.getListaSessoesSegundoGrau().size() > 0) {
				audienciaNe.descarteStatusPreAnalisesConclusaoAguardandoAssinatura(assinarSessaoSegundoGrauDtDescarte);	
			}
			
			if (mensagemDescarte.trim().length() > 0) {
				request.setAttribute("MensagemOk", mensagemDescarte);	
			}
			
			request.setAttribute("ListaSessoes", assinarSessaoSegundoGrauDt.getListaSessoesSegundoGrau());
			request.setAttribute("PaginaAtual", Configuracao.Curinga7);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", "1");
		} else {
			if (request.getAttribute("MensagemOk") == null && request.getAttribute("MensagemErro") == null)			
				request.setAttribute("MensagemOk", "Todas as pré-análises foram assinadas, e não existem novas pré-análises aguardando assinatura.");
		}
	}

	
	/**
	 * Prepara estrutura com os nomes dos arquivos e o conteúdo dos mesmos para serem assinados pelo applet, 
	 * e prepara também uma estruta map no Dt para que seja possível vincular os processos assinados com as preanálises.
	 * 
	 * Exemplo:
	 * Processo: 5000.14 / Nome do arquivo: textoonline.html / Conteúdo: Teste análise 
	 * Processo: 5002.29 / Nome do arquivo: textoonline.html / Conteúdo: Teste análise arquivo 2
	 *           
	 * nomeArquivos = 5000.14 : textoonline.html__@---5002.29 : textoonline.html
	 * conteudoArquivos = Teste análise__@---Teste análise arquivo 2  
	 * 
	 * @param assinarSessaoSegundoGrauDt
	 * @param audienciaNe
	 * @throws Exception 
	 */
	private void prepareDadosAssinatura(AssinarSessaoSegundoGrauDt assinarSessaoSegundoGrauDt, AudienciaNe audienciaNe, HttpServletRequest request, UsuarioNe UsuarioSessao, String[] pendencias) throws Exception{
		
		String tipoAudienciaProcessoMovimentacao = null;
		String fluxo = null;
		List<AudienciaMovimentacaoDt> listaAnalisePendencia = new ArrayList<AudienciaMovimentacaoDt>();
		
		for (SessaoSegundoGrauProcessoDt sessaoSegundoGrauProcessoDt : assinarSessaoSegundoGrauDt.getListaSessoesSegundoGrau()) {
			AudienciaMovimentacaoDt audienciaMovimentacaoDt = audienciaNe.obtenhaAudienciaMovimentacaoPeloIdAudienciaProcesso(request, sessaoSegundoGrauProcessoDt.getAudienciaProcessoDt().getId(), UsuarioSessao, tipoAudienciaProcessoMovimentacao, fluxo, false);
			listaAnalisePendencia.add(audienciaMovimentacaoDt);
		}
		
		assinarSessaoSegundoGrauDt.prepareDadosAssinatura(listaAnalisePendencia);
		request.setAttribute("conteudoArquivos", assinarSessaoSegundoGrauDt.getConteudoArquivos());
		
		// Limpa lista DWR
		limparListas(request);
	}
	
	/**
	 * Vincula os arquivos assinados da sessão (inseridas no ct AssinarConclusaoCtDwr) na preanalise e salva a análise conclusão.
	 * 
	 * @param assinarSessaoSegundoGrauDt
	 * @param audienciaNe
	 * @param request
	 * @param UsuarioSessao
	 * @return
	 * @throws Exception
	 */
	private boolean salvarAnaliseSessaoSegundoGrau(AssinarSessaoSegundoGrauDt assinarSessaoSegundoGrauDt, AudienciaNe audienciaNe, HttpServletRequest request, UsuarioNe UsuarioSessao) throws Exception{
		String Mensagem = "";
		String MensagemRetorno = "";
		
		for (AudienciaMovimentacaoDt audienciaMovimentacaoDt : assinarSessaoSegundoGrauDt.getListaAudienciaMovimentacaoDt()) {
			Mensagem = audienciaNe.verificarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());
			if (Mensagem.length() == 0) {
				audienciaMovimentacaoDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
				audienciaMovimentacaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				Mensagem = audienciaNe.podeMovimentarAudiencia(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), UsuarioSessao.getUsuarioDt().getId_Serventia());

				if (Mensagem.length() == 0) {						
					if (Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() 
							&& (UsuarioSessao.getUsuarioDt().isSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteUPJ())) {
						Mensagem = audienciaNe.salvarMovimentacaoAudienciaProcessoSessaoSegundoGrau(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());						
					} else {
						Mensagem = "A sessão não é do tipo segundo grau";
					}											
				}
			}
			
			if (Mensagem.trim().length() > 0){
				if (MensagemRetorno.trim().length() == 0) MensagemRetorno = "Não foi possível assinar a análise dos seguintes processos:";				
				MensagemRetorno += "\nProcesso: " + Funcoes.formataNumeroProcesso(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoNumero()) + " - Motivo: " + Mensagem;
			}
			
			audienciaMovimentacaoDt.limpar();
			Mensagem= "";
			
			//Thread.sleep(5000);	
		}
		
		limparListas(request);
		
		if (MensagemRetorno.trim().length() == 0) request.setAttribute("MensagemOk", "Assinatura efetuada com sucesso.");		
		else request.setAttribute("MensagemErro", MensagemRetorno);
		
		return true;
	}
	
	/**
	 * Vincula os arquivos assinados da sessão (inseridas no ct AssinarConclusaoCtDwr) na preanalise.
	 * 
	 * @param assinarSessaoSegundoGrauDt
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected void prepararAnaliseSessaoSegundoGrauAssincrona(AssinarSessaoSegundoGrauDt assinarSessaoSegundoGrauDt, HttpServletRequest request, HttpServletResponse response) throws Exception{
		List<AudienciaMovimentacaoDt> listaAnaliseSessao = null;
		String mensagem = "";
		int quantidade = 0;
		String sucesso = "true";
		
		listaAnaliseSessao = assinarSessaoSegundoGrauDt.getListaAudienciaMovimentacaoDt();
		
		request.getSession().setAttribute(CHAVE_SESSAO_ACORDAO_EMENTA_ASSINATURA, listaAnaliseSessao);
		
		quantidade = listaAnaliseSessao.size();
		
		limparListas(request);	
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mensagem", mensagem);
		jsonObject.put("sucesso", sucesso);
		jsonObject.put("quantidade", quantidade);
		montaRetornoJSON(response, jsonObject.toString());
	}
	
	/**
	 * salva a primeira análise conclusão da lista.
	 * 
	 * @param Movimentacaone
	 * @param request
	 * @param UsuarioSessao
	 * @return
	 * @throws Exception
	 */
	protected void salvarPrimeiraAnaliseSessaoSegundoGrauListaAssincrona(AudienciaNe audienciaNe, HttpServletRequest request, UsuarioNe UsuarioSessao, HttpServletResponse response) throws Exception{
		List<AudienciaMovimentacaoDt> listaAnaliseSessao = (List<AudienciaMovimentacaoDt>) request.getSession().getAttribute(CHAVE_SESSAO_ACORDAO_EMENTA_ASSINATURA);
		String mensagem = "";
		String sucesso = "true";
		
		if (listaAnaliseSessao.size() == 0) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("mensagem", "Lista de sessões vazia.");
			jsonObject.put("sucesso", "false");
			montaRetornoJSON(response, jsonObject.toString());
			return;
		}
		
		AudienciaMovimentacaoDt audienciaMovimentacaoDt = listaAnaliseSessao.get(0);
		
		mensagem = audienciaNe.verificarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());
		if (mensagem.length() == 0) {
			audienciaMovimentacaoDt.setIpComputadorLog(UsuarioSessao.getIpComputadorLog());
			audienciaMovimentacaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			mensagem = audienciaNe.podeMovimentarAudiencia(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), UsuarioSessao.getUsuarioDt().getId_Serventia());

			if (mensagem.length() == 0) {						
				if (Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() 
						&& (UsuarioSessao.getUsuarioDt().isSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteUPJ() )) {
					mensagem = audienciaNe.salvarMovimentacaoAudienciaProcessoSessaoSegundoGrau(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());						
				} else {
					mensagem = "A sessão não é do tipo segundo grau";
				}											
			} else {
				sucesso = "false";				
			}
		} else {
			sucesso = "false";
		}
		
		if (sucesso.equalsIgnoreCase("false") &&
			audienciaMovimentacaoDt != null &&
			audienciaMovimentacaoDt.getAudienciaDt() != null &&
			audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt() != null) {
			
			if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoNumero() != null &&
				audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoNumero().trim().length() > 0)
			{
				mensagem = "\nProcesso: " + Funcoes.formataNumeroProcesso(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoNumero()) + " - Motivo: " + mensagem;	
			} 
			else if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt() != null &&
					audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getProcessoNumero() != null &&
					audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getProcessoNumero().trim().length() > 0) 
			{
				mensagem = "\nProcesso: " + Funcoes.formataNumeroProcesso(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getProcessoNumero()) + " - Motivo: " + mensagem;
			}
		}						
			
		audienciaMovimentacaoDt.limpar();
		
		listaAnaliseSessao.remove(0);
		request.getSession().setAttribute(CHAVE_SESSAO_ACORDAO_EMENTA_ASSINATURA, listaAnaliseSessao);
		
		//Thread.sleep(5000);	
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("mensagem", mensagem);
		jsonObject.put("sucesso", sucesso);		
		montaRetornoJSON(response, jsonObject.toString()); 
	}
	
	/**
	 * Zera listas de arquivos da sessão
	 * @param request
	 */
	private void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("MapArquivosAssinados");
		request.getSession().removeAttribute("Id_MapArquivosAssinadosDwr");		
	}	
}
