package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaSegundoGrauDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.PendenciaArquivoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.ConflitoDeAbasException;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que controla a movimentação de "AudienciaProcesso", ou seja, controla a movimentação de cada processo vinculado a uma audiência.
 * No caso de sessão de 2º grau, vários processos podem ser julgados, mas a sessão será finalizada posteriormente.
 * 
 * @author msapaula
 * 
 */
public class AudienciaProcessoMovimentacaoCt extends AudienciaCtGen {

	private static final long serialVersionUID = 5073964565523621658L;
	
	protected static final int PASSO_CONSULTA_PRESIDENTE = 1;
	protected static final int PASSO_CONSULTA_REPRESENTANTE_MP = 2;
	protected static final int PASSO_CONSULTA_REDATOR = 3;
	
	protected static final String VALOR_ATRIBUTO_ID_CONSULTA_PRESIDENTE = "Id_NovoServentiaCargoPresidente";
	protected static final String VALOR_ATRIBUTO_ID_CONSULTA_SERVENTIA_MP = "Id_NovaServentiaMP";
	protected static final String VALOR_ATRIBUTO_ID_CONSULTA_REPRESENTANTE_MP = "Id_NovoServentiaCargoMP";
	protected static final String VALOR_ATRIBUTO_ID_CONSULTA_REDATOR = "Id_NovoServentiaCargoRedator";
		
	protected static final String VALOR_ATRIBUTO_NOME_CONSULTA_PRESIDENTE = "NovoServentiaCargoPresidente";
	protected static final String VALOR_ATRIBUTO_NOME_CONSULTA_SERVENTIA_MP = "NovaSeventiaMP";
	protected static final String VALOR_ATRIBUTO_NOME_CONSULTA_REPRESENTANTE_MP = "NovoServentiaCargoMP";
	protected static final String VALOR_ATRIBUTO_NOME_CONSULTA_REDATOR = "NovoServentiaCargorRedator";

	public int Permissao() {
		return AudienciaMovimentacaoDt.CodigoPermissaoAudienciaProcesso;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		AudienciaMovimentacaoDt audienciaMovimentacaoDt;
		AudienciaNe audienciaNe;

		String stNomeBusca1 = "";
		
		//variável para realizar movimentação múltipla de processos pendentes em sessão
		String audienciasProcesso[] = null;

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
				
		String Mensagem = "";
		String stId = "";
		int paginaAnterior = 0;
		int passoEditar = -1;	
		int tempFluxo1 = -1;
		String stAcao = "/WEB-INF/jsptjgo/AudienciaProcessoMovimentacao.jsp";
		List tempList = null;
		boolean somentePreAnalisadas = false;
		PendenciaNe pendenciaNe = new PendenciaNe();
		
		request.setAttribute("tempPrograma", "Movimentação de Audiência/Sessão");
		request.setAttribute("tempRetorno", "AudienciaProcessoMovimentacao");
		
		audienciaNe = (AudienciaNe) request.getSession().getAttribute("Audienciane");
		if (audienciaNe == null) audienciaNe = new AudienciaNe();

		audienciaMovimentacaoDt = (AudienciaMovimentacaoDt) request.getSession().getAttribute("AudienciaMovimentacaoDt");
		if (audienciaMovimentacaoDt == null) audienciaMovimentacaoDt = new AudienciaMovimentacaoDt();

		// Variáveis auxiliares
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));	
		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").toString().equalsIgnoreCase("null")) 
			tempFluxo1 = Funcoes.StringToInt(request.getParameter("tempFluxo1"));
		
		audienciaMovimentacaoDt.setAudienciaStatusCodigo(request.getParameter("AudienciaStatusCodigo"));
		audienciaMovimentacaoDt.setAudienciaStatus(request.getParameter("AudienciaStatus"));
		audienciaMovimentacaoDt.setAcordo(request.getParameter("Acordo"));
		audienciaMovimentacaoDt.setValorAcordo(request.getParameter("ValorAcordo"));
		audienciaMovimentacaoDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		audienciaMovimentacaoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		audienciaMovimentacaoDt.setId_Modelo(request.getParameter("Id_Modelo"));
		audienciaMovimentacaoDt.setModelo(request.getParameter("Modelo"));
		audienciaMovimentacaoDt.setTextoEditor(request.getParameter("TextoEditor"));
		audienciaMovimentacaoDt.setId_NovaSessao(request.getParameter("id_Sessao"));
		audienciaMovimentacaoDt.setDataNovaSessao(request.getParameter("dataSessao"));
		audienciaMovimentacaoDt.setId_Classificador(request.getParameter("Id_Classificador"));
		audienciaMovimentacaoDt.setClassificador(request.getParameter("Classificador"));
		audienciaMovimentacaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		audienciaMovimentacaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());		
		audienciaMovimentacaoDt.setId_ArquivoTipoEmenta(request.getParameter("Id_ArquivoTipoEmenta"));
		audienciaMovimentacaoDt.setArquivoTipoEmenta(request.getParameter("ArquivoTipoEmenta"));
		audienciaMovimentacaoDt.setId_ModeloEmenta(request.getParameter("Id_ModeloEmenta"));
		audienciaMovimentacaoDt.setModeloEmenta(request.getParameter("ModeloEmenta"));
		audienciaMovimentacaoDt.setTextoEditorEmenta(request.getParameter("TextoEditorEmenta"));
		audienciaMovimentacaoDt.setNomeArquivo(request.getParameter("nomeArquivo"));
		audienciaMovimentacaoDt.setNomeArquivoEmenta(request.getParameter("nomeArquivoEmenta"));
		
		audienciaMovimentacaoDt.setId_ServentiaCargoPresidente(request.getParameter("Id_NovoServentiaCargoPresidente"));
		if (request.getParameter("NovoServentiaCargoPresidente") != null){
			audienciaMovimentacaoDt.setServentiaCargoPresidente(request.getParameter("NovoServentiaCargoPresidente")+" - "+request.getParameter("ServentiaCargoUsuario"));
		}
		
		audienciaMovimentacaoDt.setId_ServentiaCargoMp(request.getParameter("Id_NovoServentiaCargoMP"));
		if (request.getParameter("NovoServentiaCargoMP") != null){
			audienciaMovimentacaoDt.setServentiaCargoMp(request.getParameter("NovoServentiaCargoMP")+" - "+request.getParameter("ServentiaCargoUsuario"));
		}
		
		audienciaMovimentacaoDt.setId_ServentiaMp(request.getParameter("Id_NovaServentiaMP"));
		audienciaMovimentacaoDt.setServentiaMp(request.getParameter("NovaSeventiaMP"));
		
		
		audienciaMovimentacaoDt.setId_ServentiaCargoRedator(request.getParameter("Id_NovoServentiaCargoRedator"));
		if (request.getParameter("NovoServentiaCargorRedator") != null){
			audienciaMovimentacaoDt.setServentiaCargoRedator(request.getParameter("NovoServentiaCargorRedator")+" - "+request.getParameter("ServentiaCargoUsuario"));
		}
		
		//request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));
		
		if (request.getParameter("julgadoMerito") == null && request.getParameter("fluxo") != null && request.getParameter("fluxo").toString().equals("1"))
			audienciaMovimentacaoDt.setJulgadoMeritoProcessoPrincipal("false");
		else if (request.getParameter("julgadoMerito") != null)
			audienciaMovimentacaoDt.setJulgadoMeritoProcessoPrincipal(request.getParameter("julgadoMerito"));		

		setParametrosAuxiliares(audienciaMovimentacaoDt, paginaAnterior, paginaatual, request, audienciaNe, UsuarioSessao);
		
		int grupoTipoUsuarioLogado = UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt();		

		if (UsuarioSessao.getUsuarioDt().isGrupoTipoCodigoDeAutoridade()){
			request.setAttribute("SegundoGrau", "true");
		}
		
		if (request.getParameter("SomentePreAnalisadas") != null && request.getParameter("SomentePreAnalisadas").trim().length() > 0 && 
		   (request.getParameter("SomentePreAnalisadas").trim().equalsIgnoreCase("S") || request.getParameter("SomentePreAnalisadas").trim().equalsIgnoreCase("N"))) 
				somentePreAnalisadas = (request.getParameter("SomentePreAnalisadas").trim().equalsIgnoreCase("S"));

		// -----------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {

			// Inicializa movimentação
			case Configuracao.Novo:
				audienciaMovimentacaoDt = new AudienciaMovimentacaoDt();
				
				String tipoAudienciaProcessoMovimentacao = request.getParameter("TipoAudienciaProcessoMovimentacao");
				if (tipoAudienciaProcessoMovimentacao == null) tipoAudienciaProcessoMovimentacao= "";
				
				audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao(tipoAudienciaProcessoMovimentacao);
				
				//Captura o menu acionado para permitir retorno para consulta correta
				audienciaMovimentacaoDt.setfluxo(request.getParameter("fluxo"));

				stId = request.getParameter("Id_AudienciaProcesso");
				if (stId != null && !stId.equals("")) {
					
					limparListas(request);
					
					boolean isAlteracaoDoExtratoDaAta = request.getParameter("EhAlteracaoExtratoAta") != null && request.getParameter("EhAlteracaoExtratoAta").equalsIgnoreCase("S");
					
					audienciaMovimentacaoDt = audienciaNe.obtenhaAudienciaMovimentacaoPeloIdAudienciaProcesso(request, stId, UsuarioSessao, tipoAudienciaProcessoMovimentacao, request.getParameter("fluxo"), isAlteracaoDoExtratoDaAta);					
					
					AudienciaDt audienciaProcessoDt = audienciaNe.consultarAudienciaProcessoCompleta(stId);
					
					boolean elaboracaoVoto = pendenciaNe.verificarExistenciaElaboracaoVoto(audienciaProcessoDt.getAudienciaProcessoDt().getProcessoDt().getId());
					
					if(elaboracaoVoto && !UsuarioSessao.isAnalistaVara()) {
						PendenciaDt pendencia = audienciaNe.obtemElaboracaoDeVotoSessao(audienciaProcessoDt.getAudienciaProcessoDt().getProcessoDt());
						
						if(pendencia.getListaArquivos() != null){
							
							ArquivoDt arquivo = (ArquivoDt)pendencia.getListaArquivos().get(0);							
							List pendenciasGerar = audienciaNe.getConfiguracaoElaboracaoDeVoto(pendencia.getId());
							
							if(pendenciasGerar != null){
								audienciaMovimentacaoDt.setListaPendenciasGerar(pendenciasGerar);				
								request.getSession().setAttribute("ListaPendencias", Funcoes.converterListParaSet(audienciaMovimentacaoDt.getListaPendenciasGerar()));
								request.getSession().setAttribute("Id_ListaDadosMovimentacao", audienciaMovimentacaoDt.getListaPendenciasGerar().size());				
							}
							audienciaMovimentacaoDt.setId_ArquivoTipo(arquivo.getId_ArquivoTipo());
							audienciaMovimentacaoDt.setArquivoTipo(arquivo.getArquivoTipo());
							audienciaMovimentacaoDt.setTextoEditor(arquivo.getArquivo());
							audienciaMovimentacaoDt.setNomeArquivo(arquivo.getNomeArquivo());

							
							request.setAttribute("TextoEditor", audienciaMovimentacaoDt.getTextoEditor());
							request.setAttribute("Id_ArquivoTipo", audienciaMovimentacaoDt.getId_ArquivoTipo());
							request.setAttribute("ArquivoTipo", audienciaMovimentacaoDt.getArquivoTipo());
							request.setAttribute("nomeArquivo", audienciaMovimentacaoDt.getNomeArquivo());
							
						}

					}
					
					// Captura os processos que serão movimentados, se for o caso de Movimentação em Lote
				} else if (request.getParameter("audicenciasProcesso") != null || request.getSession().getAttribute("audicenciasProcesso") != null){
					if(request.getParameter("audicenciasProcesso") != null) {
						audienciasProcesso = request.getParameterValues("audicenciasProcesso");
					} else {
						audienciasProcesso = (String[]) request.getSession().getAttribute("audicenciasProcesso");
					}
					request.getSession().setAttribute("audicenciasProcesso", audienciasProcesso);
					
					List listaProcessos = new ArrayList();
					AudienciaDt audienciaProcessoDt = null;
					for (int i = 0; i < audienciasProcesso.length; i++) {
						audienciaProcessoDt = audienciaNe.consultarAudienciaProcessoCompleta(audienciasProcesso[i].toString());
						listaProcessos.add(audienciaProcessoDt.getAudienciaProcessoDt().getProcessoDt());
					}
					request.getSession().setAttribute("processos", listaProcessos);
					
					// Consulta dados completos do processo com as partes para permitir por exemplo Intimação de Partes e Testemunhas
					audienciaProcessoDt.getAudienciaProcessoDt().setProcessoDt(audienciaNe.consultarProcessoIdCompleto(audienciaProcessoDt.getAudienciaProcessoDt().getProcessoDt().getId()));
					// Seta o processo
					audienciaMovimentacaoDt.setAudienciaDt(audienciaProcessoDt);

					//Seta status de audiência possíveis
					audienciaMovimentacaoDt.setListaAudienciaProcessoStatus(audienciaNe.consultarStatusAudiencia(UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));
					// Seta tipos de pendências que poderão ser geradas
					audienciaMovimentacaoDt.setListaPendenciaTipos(audienciaNe.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
					
					//Armazena o redator da sessão, caso exista
					audienciaMovimentacaoDt.setId_ServentiaCargoRedator(audienciaProcessoDt.getAudienciaProcessoDt().getId_ServentiaCargoRedator());
					audienciaMovimentacaoDt.setServentiaCargoRedator(audienciaProcessoDt.getAudienciaProcessoDt().getServentiaCargoRedator());
					
					//para garantir que a tela voltará para o passo 1 após consultar Tipo de Arquivo ou Modelo
					passoEditar = -1;

					limparListas(request);		
				}
				
				setRequestAtributesEditores(audienciaMovimentacaoDt, request);
				break;

			//Redireciona para confirmação de movimentação
			case Configuracao.Salvar:
				if (audienciaMovimentacaoDt.isIgnoraEtapa2Pendencias() && !audienciaMovimentacaoDt.isPreAnalise()){
					audienciaMovimentacaoDt.setPasso2("");
					audienciaMovimentacaoDt.setPasso3("Passo 2");
				} else {
					audienciaMovimentacaoDt.setPasso2("Passo 2 OK");
					audienciaMovimentacaoDt.setPasso3("Passo 3");
					// Captura lista de pendências
					audienciaMovimentacaoDt.setListaPendenciasGerar(getListaPendencias(request));
				}				
				// Captura lista de arquivos
				audienciaMovimentacaoDt.setListaArquivos(getListaArquivos(request));
				
				stAcao = "/WEB-INF/jsptjgo/AudienciaProcessoMovimentacaoConfirmacao.jsp";				
				break;

			// Salva movimentação de audiência
			case Configuracao.SalvarResultado:
				
				if (request.getParameter("ElaboracaoVoto") != null && request.getParameter("ElaboracaoVoto").toString().equalsIgnoreCase("true")){
				 
					ProcessoNe processoNe = new ProcessoNe();
					PendenciaDt pendenciaDt = null;					 
					
					String msgElaboracaoVoto = null;
					ProcessoDt processoDt = audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt();
						 
					boolean elaboracaoVoto = pendenciaNe.verificarExistenciaElaboracaoVoto(processoDt.getId());
				
					if (!elaboracaoVoto){
						
						msgElaboracaoVoto = processoNe.verificarElaboracaoVoto(UsuarioSessao.getUsuarioDt(), processoDt.getId());
						msgElaboracaoVoto = processoNe.verificarElaboracaoVotoPreAnalise(audienciaMovimentacaoDt);
												
						if(msgElaboracaoVoto != null && msgElaboracaoVoto.length()>0){
							redireciona(response, "AudienciaProcessoMovimentacao?PaginaAtual="+Configuracao.Salvar+"&MensagemErro=" + msgElaboracaoVoto);
						}else{
							UsuarioDt usuarioDt = UsuarioSessao.getUsuarioDt();
							
							if(usuarioDt.isAssessorMagistrado()){
								
								if(usuarioDt.getId_ServentiaCargoUsuarioChefe() != null && !usuarioDt.getId_ServentiaCargoUsuarioChefe().equals("")){
									usuarioDt.setId_ServentiaCargo(usuarioDt.getId_ServentiaCargoUsuarioChefe());
								}
							}
														
							pendenciaDt = processoNe.gerarPendenciaElaboracaoVoto(usuarioDt, processoDt.getId());
						}
						
					} else {
						
						msgElaboracaoVoto = processoNe.verificarEditarElaboracaoVoto(UsuarioSessao.getUsuarioDt(), processoDt.getId());
						msgElaboracaoVoto = processoNe.verificarElaboracaoVotoPreAnalise(audienciaMovimentacaoDt);

						if(msgElaboracaoVoto != null && msgElaboracaoVoto.length()>0){
							redireciona(response, "AudienciaProcessoMovimentacao?PaginaAtual="+Configuracao.Salvar+"&MensagemErro=" + msgElaboracaoVoto);
						
						} else{
							pendenciaDt = audienciaNe.obtemElaboracaoDeVotoSessao(processoDt);	
						}
					}
				
					if(pendenciaDt != null){
						PendenciaArquivoNe pendArquivoNe = new PendenciaArquivoNe();
						pendArquivoNe.salvarPreAnaliseElaboracaoVoto(pendenciaDt, audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());
						
						
						if(!elaboracaoVoto)  redireciona(response, "AudienciaSegundoGrau?PaginaAtual="+Configuracao.Curinga7+"&MensagemOk=Pendência de Elaboração de Voto Criada com Sucesso.");
						else  redireciona(response, "AudienciaSegundoGrau?PaginaAtual="+Configuracao.Curinga7+"&MensagemOk=Pendência de Elaboração de Voto Alterada com Sucesso.");
					}
				
					return;					
				} 
				
				if (tempFluxo1 == 3) {
					
					LogDt logDt = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());

					audienciaNe.descartarPreAnalise(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt(), logDt);
					
					redireciona(response, "AudienciaSegundoGrau?PaginaAtual=7&SomentePendentesAcordao=S&SomenteAguardandoAssinatura=N&MensagemOk=Pré-Análise descartada com sucesso.&SomentePreAnalisadas="+(somentePreAnalisadas?"S":"N"));
				} else {
					if ((tempFluxo1 == 1 || tempFluxo1 == 2) && 
						(Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && 
						 (UsuarioSessao.getUsuarioDt().isSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteUPJ()) &&
						 (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA ||  grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE  ||  grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
						)
					   )
					{
						audienciaMovimentacaoDt.setTipoAudienciaProcessoMovimentacao("1");	
						audienciaMovimentacaoDt.setPendenteAssinatura(tempFluxo1 == 2);
					}
					
					Mensagem = audienciaNe.verificarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());
					if (Mensagem.length() == 0) {
						Mensagem = audienciaNe.podeMovimentarAudiencia(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), UsuarioSessao.getUsuarioDt().getId_Serventia());

						if (Mensagem.length() == 0) {						
							if (Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && (UsuarioSessao.getUsuarioDt().isSegundoGrau() || UsuarioSessao.getUsuarioDt().isUPJTurmaRecursal()  || UsuarioSessao.getUsuarioDt().isGabineteSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteUPJ())) {
								Mensagem = audienciaNe.salvarMovimentacaoAudienciaProcessoSessaoSegundoGrau(audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt());
								if (Mensagem.length() == 0){
									if (audienciaMovimentacaoDt.isPreAnalise()){
										if(Funcoes.StringToBoolean(request.getParameter("SalvarRedistribuir")) ) {
											String voto = audienciaMovimentacaoDt.getId_PendenciaVotoGerada();
											String ementa = audienciaMovimentacaoDt.getId_PendenciaEmentaGerada();
											String novoServentiaCargo = audienciaMovimentacaoDt.getId_ServentiaCargoVotoEmentaGerada();
											redireciona(response, "PendenciaResponsavel?PaginaAtual="+Configuracao.Novo+"&pendencia="+voto+"&CodigoPendencia="+UsuarioSessao.getCodigoHash(voto)+"&ementa="+ementa+"&novoServentiaCargo="+novoServentiaCargo+"&MensagemOk=Pré-Análise registrada com sucesso.");
										} else {
											redireciona(response, "AudienciaSegundoGrau?PaginaAtual=7&SomentePendentesAcordao=S&SomenteAguardandoAssinatura=N&MensagemOk=Pré-Análise realizada com sucesso.&SomentePreAnalisadas="+(somentePreAnalisadas?"S":"N"));
										}
									} else {	
										String mensagemComplementar = "Sessão realizada com sucesso.";
										if (Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo()) == AudienciaProcessoStatusDt.DESMARCAR_PAUTA) {
											mensagemComplementar = "Processo desmarcado com sucesso.";
										}
										if (Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaStatusCodigo()) == AudienciaProcessoStatusDt.RETIRAR_PAUTA) {
											mensagemComplementar = "Processo retirado com sucesso.";
										}
										if (UsuarioSessao.getUsuarioDt().isGabineteSegundoGrau()) {
											redireciona(response, "AudienciaSegundoGrau?PaginaAtual=7&SomentePendentesAcordao=S&SomenteAguardandoAssinatura=N&MensagemOk=" + mensagemComplementar + "&SomentePreAnalisadas="+(somentePreAnalisadas?"S":"N"));														
										} else {										
											redireciona(response, "BuscaProcesso?Id_Processo=" + audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getId() + "&MensagemOk=" + mensagemComplementar);							
										}
									}								
								}
							} else {
								//se for movimentação múltipla
								if (request.getParameter("audicenciasProcesso") != null || request.getSession().getAttribute("audicenciasProcesso") != null){
									if(request.getParameter("audicenciasProcesso") != null) {
										audienciasProcesso = request.getParameterValues("audicenciasProcesso");
									} else {
										audienciasProcesso = (String[]) request.getSession().getAttribute("audicenciasProcesso");
									}
									request.getSession().setAttribute("audicenciasProcesso", audienciasProcesso);
									List listaProcessos = new ArrayList();
									AudienciaDt audienciaProcessoDt = null;
									for (int i = 0; i < audienciasProcesso.length; i++) {
										audienciaProcessoDt = audienciaNe.consultarAudienciaProcessoCompleta(audienciasProcesso[i].toString());
										listaProcessos.add(audienciaProcessoDt.getAudienciaProcessoDt().getProcessoDt());
									}
									request.getSession().setAttribute("processos", listaProcessos);
									
									audienciaNe.salvarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, listaProcessos, UsuarioSessao.getUsuarioDt());
									
									//Seta serventia do usuário logado
									AudienciaSegundoGrauDt audienciaSegundoGrauDt = new AudienciaSegundoGrauDt();
									audienciaSegundoGrauDt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
									audienciaSegundoGrauDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
									request.setAttribute("tempBuscaId_Audiencia", "Id_Audiencia");
									request.setAttribute("tempBuscaAudiencia", "Audiencia");

									List listaAudiencias = audienciaNe.consultarSessoesAbertas(UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), true);
									if (listaAudiencias.size() > 0) {
										request.setAttribute("ListaAudiencias", listaAudiencias);
										request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
										request.setAttribute("QuantidadePaginas", audienciaNe.getQuantidadePaginas());
									}
									stAcao = "/WEB-INF/jsptjgo/AudienciaSegundoGrauLocalizar.jsp";
									request.setAttribute("MensagemOk", "Movimentação de processo efetuada com sucesso.");
									
									break;
									
								} else {
									//se não for movimentação múltipla, segue o fluxo normal
									audienciaNe.salvarMovimentacaoAudienciaProcesso(audienciaMovimentacaoDt, null, UsuarioSessao.getUsuarioDt());
									redireciona(response, "BuscaProcesso?Id_Processo=" + audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt().getId() + "&MensagemOk=Movimentação de Audiência efetuada com sucesso.");
								}
							}
							if (!audienciaMovimentacaoDt.isPreAnalise() && Mensagem.length() == 0) {
								audienciaMovimentacaoDt.limpar();
								limparListas(request);
								return;	
							}						
						} 
					} 
					
					if (Mensagem.length() > 0) {
						request.setAttribute("MensagemErro", Mensagem);
						if ((tempFluxo1 == 1 || tempFluxo1 == 2) && (Funcoes.StringToInt(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && (UsuarioSessao.getUsuarioDt().isSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteUPJ() ))) {
							audienciaMovimentacaoDt.LimparIndicadorDePreAnalise();
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
					
					atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", "AudienciaProcessoMovimentacao?PassoEditar=0", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
					
					break;
				} else{
					String stTemp="";
					stTemp = audienciaNe.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}			

			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					boolean EhEmenta = (request.getParameter("ConsultaEmenta") != null && String.valueOf(request.getParameter("ConsultaEmenta")).trim().equalsIgnoreCase("S"));
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo" + (EhEmenta ? "Ementa" : ""));
					request.setAttribute("tempBuscaDescricao","ArquivoTipo" + (EhEmenta ? "Ementa" : ""));
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","AudienciaProcessoMovimentacao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = audienciaNe.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;		
			// Consultar Modelos do Usuário
			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Modelo"};
					String[] lisDescricao = {"Modelo","Serventia","Tipo Modelo"};
					boolean EhEmenta = (request.getParameter("ConsultaEmenta") != null && String.valueOf(request.getParameter("ConsultaEmenta")).trim().equalsIgnoreCase("S"));
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Modelo" + (EhEmenta ? "Ementa" : ""));
					request.setAttribute("tempBuscaDescricao", "Modelo" + (EhEmenta ? "Ementa" : ""));
					request.setAttribute("tempBuscaPrograma", "Modelo");
					request.setAttribute("tempRetorno", "AudienciaProcessoMovimentacao");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					if(EhEmenta){
						request.getSession().setAttribute("EhEmenta", "S");
					}else{
						request.getSession().setAttribute("EhEmenta", "null");
					}
				}else{
					String stTemp = "";
					
					boolean EhEmenta = (((String)request.getSession().getAttribute("EhEmenta")).equalsIgnoreCase("S") ? true : false);
					stTemp = audienciaNe.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), (EhEmenta ? audienciaMovimentacaoDt.getId_ArquivoTipoEmenta() : audienciaMovimentacaoDt.getId_ArquivoTipo()), stNomeBusca1, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);					
					
					return;
				}
				break;
				
			//Consultar uma Serventia do tipo Promotoria
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):				
				request.setAttribute("tempBuscaId_Serventia", VALOR_ATRIBUTO_ID_CONSULTA_SERVENTIA_MP);
				request.setAttribute("tempBuscaServentia", VALOR_ATRIBUTO_NOME_CONSULTA_SERVENTIA_MP);							


				if (request.getParameter("Passo")==null){
					
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId",VALOR_ATRIBUTO_ID_CONSULTA_SERVENTIA_MP);
					request.setAttribute("tempBuscaDescricao",VALOR_ATRIBUTO_NOME_CONSULTA_SERVENTIA_MP);
					request.setAttribute("tempBuscaPrograma","Consulta de Serventias");			
					request.setAttribute("tempRetorno","AudienciaProcessoMovimentacao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 2;
				}else{
					String stTemp = "";
					stTemp = audienciaNe.consultarServentiasAtivasJSON(stNomeBusca1, PosicaoPaginaAtual, String.valueOf(ServentiaTipoDt.PROMOTORIA), "");
						
						enviarJSON(response, stTemp);
						
						
						request.getSession().setAttribute(VALOR_ATRIBUTO_ID_CONSULTA_REPRESENTANTE_MP, "");
						request.getSession().setAttribute(VALOR_ATRIBUTO_NOME_CONSULTA_REPRESENTANTE_MP, "");
						audienciaMovimentacaoDt.limparServentiaCargoMp();	
					
					return;
				}
				break;
				
			//Consultar Cargos de uma Serventia
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				// Controles da variável passoEditar				
				// 1  : Consultar Cargos de uma Serventia - Desembargadores (Presidente)	
				// 2  : Consultar Cargos de uma Serventia - Promotores/Procuradores
				// 3  : Consultar Cargos de uma Serventia - Desembargadores (Redator)
				String ValorCampo_id_ServentiaCargo = "";
				String ValorCampoServentiaCargo = "";				
				String Id_Serventia = "";				
				String ServentiaTipoCodigo = "";
				
				if (passoEditar == PASSO_CONSULTA_PRESIDENTE) {
					ValorCampo_id_ServentiaCargo = VALOR_ATRIBUTO_ID_CONSULTA_PRESIDENTE;
					ValorCampoServentiaCargo = VALOR_ATRIBUTO_NOME_CONSULTA_PRESIDENTE;				
					Id_Serventia = UsuarioSessao.getUsuarioDt().getId_Serventia();
					ServentiaTipoCodigo = UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo();
				} else if (passoEditar == PASSO_CONSULTA_REPRESENTANTE_MP) {
					ValorCampo_id_ServentiaCargo = VALOR_ATRIBUTO_ID_CONSULTA_REPRESENTANTE_MP; 
					ValorCampoServentiaCargo = VALOR_ATRIBUTO_NOME_CONSULTA_REPRESENTANTE_MP;
					//Id_Serventia = audienciaDt.getId_NovaServentiaMP();
					Id_Serventia = audienciaMovimentacaoDt.getId_ServentiaMp();
					ServentiaTipoCodigo = String.valueOf(ServentiaTipoDt.PROMOTORIA);					
				} else if (passoEditar == PASSO_CONSULTA_REDATOR) {
					ValorCampo_id_ServentiaCargo = VALOR_ATRIBUTO_ID_CONSULTA_REDATOR;
					ValorCampoServentiaCargo = VALOR_ATRIBUTO_NOME_CONSULTA_REDATOR;				
					Id_Serventia = UsuarioSessao.getUsuarioDt().getId_Serventia();
					ServentiaTipoCodigo = UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo();					
				}
				
				if ((Id_Serventia == null || Id_Serventia.trim().length() == 0) && request.getParameter("tempFluxo1")==null) request.setAttribute("MensagemErro", "Favor selecionar uma serventia.");
				else {
					request.setAttribute("tempBuscaId_ServentiaCargo", ValorCampo_id_ServentiaCargo);
					request.setAttribute("tempBuscaServentiaCargo", ValorCampoServentiaCargo);
				
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"ServentiaCargo"};
						String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
						String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
						request.setAttribute("camposHidden",camposHidden);
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", ValorCampo_id_ServentiaCargo);
						request.setAttribute("tempBuscaDescricao", ValorCampoServentiaCargo);
						request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
						request.setAttribute("tempRetorno", "AudienciaProcessoMovimentacao");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						request.setAttribute("tempFluxo1", Id_Serventia);
						request.setAttribute("tempFluxo2", ServentiaTipoCodigo);
						passoEditar = -1;
					}else{
						String stTemp = "";
						stTemp = audienciaNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual,  request.getParameter("tempFluxo1"), request.getParameter("tempFluxo2"), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
							
							enviarJSON(response, stTemp);
							
						
						return;
					}
				}						
				break;	
				
		    //Retirada do extrato da ata e do acórdão (caso já tenha sido inserido)
			case Configuracao.Curinga6:
				MovimentacaoProcessoDt movimentacaoProcessodt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
				
				ProcessoDt processoDt = audienciaNe.retirarExtratoAtaJulgamentoOuAcordaoOuRetornarAudienciaProcessoSessaoSegundoGrauAnalista(UsuarioSessao.getUsuarioDt(), movimentacaoProcessodt);
				
				if (processoDt != null) {
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Movimentação realizada com sucesso.");	
				}
				
				break;
			default:
				// Controles da variável passoEditar
				// -1 : Redireciona para passo 1
				// 0 : Redireciona para passo 2 - Pendências a Gerar
				// 1 : Realiza o download da ata (arquivo) vinculado à audiência processo
				switch (passoEditar) {

					case 0:
						audienciaMovimentacaoDt.setPasso1("Passo 1 OK");
						
						if (audienciaMovimentacaoDt.isIgnoraEtapa2Pendencias() || audienciaNe.isAudienciaCejuscProcessoExterno( audienciaMovimentacaoDt, UsuarioSessao.getUsuarioDt())){
							audienciaMovimentacaoDt.setPasso3("Passo 2");
							// Captura lista de arquivos
							audienciaMovimentacaoDt.setListaArquivos(getListaArquivos(request));
							stAcao = "/WEB-INF/jsptjgo/AudienciaProcessoMovimentacaoConfirmacao.jsp";
							// é gerado o código do pedido, assim o submit so pode ser
			                // executado uma unica vez
			                request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
						} else {					
							audienciaMovimentacaoDt.setPasso2("Passo 2");
							audienciaMovimentacaoDt.setPasso3("");
							 // Limpa as variáveis de sessão que não deverão ser utilizadas no método getListaDestinatarios da classe MovimentacaoCtDwr
							request.getSession().removeAttribute("Movimentacaodt");
							request.getSession().removeAttribute("AnalisePendenciadt");
							stAcao = "/WEB-INF/jsptjgo/AudienciaProcessoMovimentacaoPendencias.jsp";							
						}									
						break;
						
					case 1:
						boolean baixouArquivo = false;
						if (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA ||  grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE  ||  grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO  || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_DESEMBARGADOR){
							if (audienciaMovimentacaoDt != null && audienciaMovimentacaoDt.getAudienciaDt() != null && audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt() != null){								
								if (audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_ArquivoAta() != null && audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_ArquivoAta().length() > 0){
									audienciaNe.baixarArquivo(audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getId_ArquivoAta(), response,UsuarioSessao.getUsuarioDt().getId(), audienciaMovimentacaoDt.getIpComputadorLog());
									baixouArquivo = true;								
									
									return;
								}
							}											
						}
						if (!baixouArquivo) throw new MensagemException("Acesso negado.");				
						
						break;

					default:
						stId = request.getParameter("Id_AudienciaProcesso");
						if (stId != null && !stId.isEmpty()) {
							if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(audienciaMovimentacaoDt.getId())) {
								audienciaMovimentacaoDt.limpar();
								audienciaMovimentacaoDt.setAudienciaDt(audienciaNe.consultarAudienciaProcessoCompleta(stId));
							}
						}

						//Se possui uma Sessão selecionada para Remarcar deve consultar a lista de sessões abertas pra mostrar na tela
						if (audienciaMovimentacaoDt.getId_NovaSessao().length() > 0 && (audienciaMovimentacaoDt.getListaSessoesAbertas() == null || audienciaMovimentacaoDt.getListaSessoesAbertas().size() == 0)) {
							audienciaMovimentacaoDt.setListaSessoesAbertas(audienciaNe.consultarSessoesAbertas(UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), true));
						}

						audienciaMovimentacaoDt.setPasso1("Passo 1");
						audienciaMovimentacaoDt.setPasso2("");
						audienciaMovimentacaoDt.setPasso3("");
						break;
				}
		}	
		
		atualizarTitulo(request, grupoTipoUsuarioLogado, audienciaMovimentacaoDt.isPreAnalise(), audienciaMovimentacaoDt.getAudienciaTipoCodigo(), audienciaMovimentacaoDt.isMovimentacaoSessaoAdiada(), audienciaMovimentacaoDt.isMovimentacaoSessaoIniciada(), (UsuarioSessao.getUsuarioDt().isSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteSegundoGrau() || UsuarioSessao.getUsuarioDt().isGabineteUPJ() ));
				
		request.setAttribute("GrupoTipoUsuarioLogado", grupoTipoUsuarioLogado);
		request.setAttribute("PassoEditar", passoEditar);		
		request.getSession().setAttribute("AudienciaMovimentacaoDt", audienciaMovimentacaoDt);
		request.getSession().setAttribute("audienciaNe", audienciaNe);
		request.setAttribute("votoPorMaioria", (audienciaMovimentacaoDt.isVotoPorMaioria() ? "true" : "false"));
		//Consultando a serventia para saber se é alguma serventia que não necessita MP para inserir extrato de ata.
		//Atualmente, somente o Conselho de Magistratura tem essa exceção.
		ServentiaDt serventiaDt = new ServentiaNe().consultarId(UsuarioSessao.getUsuarioDt().getId_Serventia());
		if(serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.CONSELHO_SUPERIOR_MAGISTRATURA))){
			request.setAttribute("ocultarRepresentanteMP", "true");
		}
		//Libera o botão Guardar para Assinar segundo o perfil do usuário
		//request.setAttribute("exibePendenciaAssinatura", UsuarioSessao.isPodeExibirPendenciaAssinatura(false,  (PendenciaTipoDt.CONCLUSO_RELATOR)));
		request.setAttribute("SomentePreAnalisadas", (somentePreAnalisadas ? "S" : "N"));

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Tratamentos necessários ao realizar uma movimentação
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(AudienciaMovimentacaoDt audienciaMovimentacaoDt, int paginaAnterior, int paginaatual, HttpServletRequest request, AudienciaNe audienciaNe, UsuarioNe usuarioNe) throws Exception{

		// Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo
		// do arquivo
		if ((!audienciaMovimentacaoDt.getId_Modelo().equals("") || !audienciaMovimentacaoDt.getId_ModeloEmenta().equals(""))  && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			boolean EhEmenta = (request.getSession().getAttribute("EhEmenta") != null && String.valueOf(request.getSession().getAttribute("EhEmenta")).trim().equalsIgnoreCase("S"));
			
			ModeloDt modeloDt = null;
			if (EhEmenta) {
				modeloDt = audienciaNe.consultarModeloId(audienciaMovimentacaoDt.getId_ModeloEmenta(), (ProcessoDt) audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt(), usuarioNe.getUsuarioDt());
				audienciaMovimentacaoDt.setId_ArquivoTipoEmenta(modeloDt.getId_ArquivoTipo());
				audienciaMovimentacaoDt.setArquivoTipoEmenta(modeloDt.getArquivoTipo());
				audienciaMovimentacaoDt.setTextoEditorEmenta(modeloDt.getTexto());
			} else {
				modeloDt = audienciaNe.consultarModeloId(audienciaMovimentacaoDt.getId_Modelo(), (ProcessoDt) audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt(), usuarioNe.getUsuarioDt());
				audienciaMovimentacaoDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
				audienciaMovimentacaoDt.setArquivoTipo(modeloDt.getArquivoTipo());
				audienciaMovimentacaoDt.setTextoEditor(modeloDt.getTexto());
			}			
			request.getSession().removeAttribute("EhEmenta"); 
			
		}
		
//		if (paginaAnterior == (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
//			if(request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_SERVENTIA_MP) != null && String.valueOf(request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_SERVENTIA_MP)).trim().length() > 0){
//				request.getSession().setAttribute(VALOR_ATRIBUTO_ID_CONSULTA_SERVENTIA_MP, request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_SERVENTIA_MP));
//				request.getSession().setAttribute(VALOR_ATRIBUTO_NOME_CONSULTA_SERVENTIA_MP, request.getParameter(VALOR_ATRIBUTO_NOME_CONSULTA_SERVENTIA_MP));			
//			}
//		}
//		
//		if (paginaAnterior == (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
//			if(request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_PRESIDENTE) != null && String.valueOf(request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_PRESIDENTE)).trim().length() > 0){
//				request.getSession().setAttribute(VALOR_ATRIBUTO_ID_CONSULTA_PRESIDENTE, request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_PRESIDENTE));
//				request.getSession().setAttribute(VALOR_ATRIBUTO_NOME_CONSULTA_PRESIDENTE, request.getParameter(VALOR_ATRIBUTO_NOME_CONSULTA_PRESIDENTE));			
//			}	
//			
//			if(request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_REPRESENTANTE_MP) != null && String.valueOf(request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_REPRESENTANTE_MP)).trim().length() > 0){
//				request.getSession().setAttribute(VALOR_ATRIBUTO_ID_CONSULTA_REPRESENTANTE_MP, request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_REPRESENTANTE_MP));
//				request.getSession().setAttribute(VALOR_ATRIBUTO_NOME_CONSULTA_REPRESENTANTE_MP, request.getParameter(VALOR_ATRIBUTO_NOME_CONSULTA_REPRESENTANTE_MP));			
//			}
//			
//			if(request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_REDATOR) != null && String.valueOf(request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_REDATOR)).trim().length() > 0){
//				audienciaMovimentacaoDt.setId_ServentiaCargoRedator(request.getParameter(VALOR_ATRIBUTO_ID_CONSULTA_REDATOR));
//				audienciaMovimentacaoDt.setServentiaCargoRedator(request.getParameter(VALOR_ATRIBUTO_NOME_CONSULTA_REDATOR));			
//			}
//		}	

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		setRequestAtributesEditores(audienciaMovimentacaoDt, request);

		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");
		
		// Verifica a opção que o usuário informou para a opção por maioria
		if (request.getParameter("votoPorMaioria") != null) audienciaMovimentacaoDt.setVotoPorMaioria(String.valueOf(request.getParameter("votoPorMaioria")).trim().equalsIgnoreCase("true"));

	}

	private void setRequestAtributesEditores(AudienciaMovimentacaoDt audienciaMovimentacaoDt,
			HttpServletRequest request) {
		request.setAttribute("TextoEditor", audienciaMovimentacaoDt.getTextoEditor());
		request.setAttribute("Id_ArquivoTipo", audienciaMovimentacaoDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", audienciaMovimentacaoDt.getArquivoTipo());
		request.setAttribute("Modelo", audienciaMovimentacaoDt.getModelo());		
		request.setAttribute("TextoEditorEmenta", audienciaMovimentacaoDt.getTextoEditorEmenta());
		request.setAttribute("Id_ArquivoTipoEmenta", audienciaMovimentacaoDt.getId_ArquivoTipoEmenta());
		request.setAttribute("ArquivoTipoEmenta", audienciaMovimentacaoDt.getArquivoTipoEmenta());
		request.setAttribute("ModeloEmenta", audienciaMovimentacaoDt.getModeloEmenta());
	}

	/**
	 * Resgata lista de arquivos inseridos Converte de Map para List
	 */
	protected List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		List lista = Funcoes.converterMapParaList(mapArquivos);

		return lista;
	}

	/**
	 * Resgata lista de pendências a serem inseridas Converte de Set para List
	 */
	protected List getListaPendencias(HttpServletRequest request) {
		Set listaPendencias = (Set) request.getSession().getAttribute("ListaPendencias");
		List lista = Funcoes.converterSetParaList(listaPendencias);
		return lista;
	}

	/**
	 * Consulta de modelos Se não encontrar nenhum modelo retorna false
	 * @throws Exception 
	 */
	private boolean consultarModelo(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, AudienciaNe audienciaNe, AudienciaMovimentacaoDt audienciaMovimentacaoDt, int paginaatual, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;
		boolean EhEmenta = (request.getParameter("ConsultaEmenta") != null && String.valueOf(request.getParameter("ConsultaEmenta")).trim().equalsIgnoreCase("S"));
		List tempList = audienciaNe.consultarModelo(usuarioSessao.getUsuarioDt(), (EhEmenta ? audienciaMovimentacaoDt.getId_ArquivoTipoEmenta() : audienciaMovimentacaoDt.getId_ArquivoTipo()), tempNomeBusca, posicaoPaginaAtual);
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaModelo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", audienciaNe.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_Modelo", "Id_Modelo" + (EhEmenta ? "Ementa" : ""));
			request.setAttribute("tempBuscaModelo", "Modelo" + (EhEmenta ? "Ementa" : ""));
			boRetorno = true;
			if(EhEmenta) request.getSession().setAttribute("EhEmenta", "S");
		} else request.setAttribute("MensagemErro", "Nenhum Modelo foi localizado.");
		return boRetorno;
	}

	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		
		if (!(request.getParameter("ElaboracaoVoto")  != null && request.getParameter("ElaboracaoVoto").equalsIgnoreCase("true"))){
			request.getSession().removeAttribute("ListaArquivosDwr");
			request.getSession().removeAttribute("ListaArquivos");
			request.getSession().removeAttribute("Id_ListaArquivosDwr");
		}

		// Limpa lista DWR e zera contador Pendências
		request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
		request.getSession().removeAttribute("ListaPendencias");
	}
	
	protected void atualizarTitulo(HttpServletRequest request, int grupoTipoUsuarioLogado, boolean EhPreAnalise, int AudienciaTipoCodigo, boolean EhAdiamentoSessao, boolean EhInicioSessao, boolean ehCamaraGabineteSegundoGrau){
		request.setAttribute("TituloPagina", "Concluir Audiência");
		if (AudienciaTipoCodigo == AudienciaTipoDt.Codigo.SESSAO_SEGUNDO_GRAU.ordinal() && ehCamaraGabineteSegundoGrau) {
			if (grupoTipoUsuarioLogado == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU){	
				if (EhAdiamentoSessao) request.setAttribute("TituloPagina", "Inserir Extrato da Ata de Adiamento de Julgamento");
				else if (EhInicioSessao) request.setAttribute("TituloPagina", "Inserir Extrato da Ata de Início de Julgamento");
				else request.setAttribute("TituloPagina", "Inserir Extrato da Ata de Julgamento");
			} else if (grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE  || grupoTipoUsuarioLogado == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_DESEMBARGADOR){
				request.setAttribute("TituloPagina", "Pré-Analisar Sessão");
			} else if (grupoTipoUsuarioLogado == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || grupoTipoUsuarioLogado == GrupoTipoDt.JUIZ_TURMA ){
				if (EhPreAnalise){
					request.setAttribute("TituloPagina", "Pré-Analisar Sessão");
				} else {
					request.setAttribute("TituloPagina", "Concluir Sessão");
				}			
			}
		}else{
			if(grupoTipoUsuarioLogado == GrupoTipoDt.JUIZ_TURMA || grupoTipoUsuarioLogado == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA ){
				if (EhPreAnalise){
					request.setAttribute("TituloPagina", "Pré-Analisar Sessão");
				}
			}
		} 		
	}
	
}
