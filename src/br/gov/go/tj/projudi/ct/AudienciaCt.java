package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class AudienciaCt extends AudienciaCtGen {

	private static final long serialVersionUID = 211784492608995766L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioNe, int paginaAtual, String nomeBusca, String posicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {
		AudienciaNe audienciaNe;
		AudienciaDt audienciaDt;
		AudienciaDt audienciaDtRetornoAgendamento = null;
		String acao = ""; 
		String mensagem = "";
		int paginaAnterior = 0;
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));

		setAtributosIniciais(request, paginaAtual);

		audienciaNe = (AudienciaNe) request.getSession().getAttribute("Audienciane");
		if (audienciaNe == null) audienciaNe = new AudienciaNe();

		audienciaDt = (AudienciaDt) request.getSession().getAttribute("Audienciadt");
		if (audienciaDt == null) audienciaDt = new AudienciaDt();

		if (request.getParameter("CaixaTextoPosicionar") != null) posicaoPaginaAtual = String.valueOf((Funcoes.StringToInt(request.getParameter("CaixaTextoPosicionar"))) - 1);

		if (request.getParameter("PaginaAnterior") != null && !request.getParameter("PaginaAnterior").equals("null")) {
			paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
			request.setAttribute("PaginaAnterior", paginaAnterior);
		}
		
		setAudienciaDt(request, usuarioNe, audienciaDt, audienciaNe);
		
		switch (paginaAtual) {

			case Configuracao.Novo: {
				audienciaDt.getAudienciaProcessoDt().limpar();
				audienciaDt.limpar();
				acao = setAcao_TituloPagina(request, usuarioNe, audienciaNe, audienciaDt);
				break;
			}

			// Consultar Audi�ncias
			case Configuracao.Localizar: {
				this.consultarAudiencias(request.getParameter("fluxo"), request, usuarioNe.getUsuarioDt(), audienciaDt, posicaoPaginaAtual, paginaAtual, audienciaNe);
				acao = setAcao_TituloPagina(request, usuarioNe, audienciaNe, audienciaDt);
				break;
			}

			case Configuracao.Imprimir: {
				byte[] byTemp = (new AudienciaNe()).relListagemAudiencias(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , usuarioNe.getUsuarioDt(), audienciaDt, Funcoes.StringToInt(request.getParameter("fluxo")));
						
				enviarPDF(response, byTemp,"Relatorio");
																									
				return;

			}

			// Consulta de Agendas Livres e Confirma��o de Agendamento Manual
			case Configuracao.Curinga6: {
				ProcessoDt processoDt=null;
				
				processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
				//se n�o existe o processo n�o sess�o houve algum erro, possivel uso de abas e clique na pagina iniciar
				if (processoDt==null){
					redireciona(response, "Usuario?PaginaAtual=-10");
					return;
				}

				setTituloPagina(request, usuarioNe, audienciaNe, audienciaDt);

				if (request.getParameter("BotaoVoltar") == null) {
					// Se foi passado Id_Audiencia refere-se a confirma��o de agendamento/reagendamento
					String id_Audiencia = request.getParameter("Id_Audiencia");
					if (id_Audiencia != null && id_Audiencia.length() > 0) {

						// Buscar o objeto do tipo AudienciaDt selecionado pelo usu�rio, utilizando para tal o seu id
						audienciaDt = audienciaNe.consultarAudienciaLivreCompleta(id_Audiencia);

						// Se acesso de outra serventia seta no Dt de audiencia essa informa��o
						if (request.getSession().getAttribute("AcessoOutraServentia") != null && (Funcoes.StringToInt(request.getSession().getAttribute("AcessoOutraServentia").toString()) == PendenciaTipoDt.CARTA_PRECATORIA)) {
							audienciaDt.setAcessoOutraServentia(String.valueOf(PendenciaTipoDt.CARTA_PRECATORIA));
						}

						// Se uma audi�ncia foi selecionada para agendamento autom�tico ou manual direciona para confirma��o
						if (audienciaDt != null && audienciaDt.getId().length() > 0) {
							audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);
							acao = "/WEB-INF/jsptjgo/AudienciaAgendamentoConfirmacao.jsp";
							request.setAttribute("Mensagem", "Clique para confirmar o Agendamento da Audi�ncia.");
						}
					} else {
						mensagem = audienciaNe.validarAudienciaAgendamento(audienciaDt, processoDt, usuarioNe.getUsuarioDt());
						if (mensagem.length() == 0) {
							// Redireciona para jsp de listagem de audi�ncias livres
							acao = getAcao(request, usuarioNe, audienciaNe, audienciaDt);
							audienciaDt.setPrazoAgendamentoAudiencia("0");
							audienciaDt.setDataInicialConsulta(Funcoes.dateToStringSoData(Funcoes.getPrimeiroDiaMes()));
							audienciaDt.setDataFinalConsulta(Funcoes.dateToStringSoData(Funcoes.getUltimoDiaMes()));
						} else {
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);
							return;
						}
					}
				} else {
					this.executar(request, response, usuarioNe, Configuracao.Localizar, nomeBusca, posicaoPaginaAtual);
					return;
				}
				// P�gina anterior
				request.setAttribute("PaginaAnterior", String.valueOf(paginaAtual));
				break;
			}

			// Fun��o Utilizada para Agendamento Manual de Audi�ncia para um Processo j� existente
			case Configuracao.Curinga7: {
				// Verificar se algum menu foi acionado
				if (request.getParameter("fluxo") != null) {
					// Captura processo selecionado
					ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

					// Valida se Agendamento pode ser efetuado
					if (audienciaDt.getId().length() > 0) {
						// AGENDAR/REAGENDAR AUDI�NCIA MANUALMENTE						
						if (audienciaNe.agendarAudienciaManualmente(audienciaDt, usuarioNe.getUsuarioDt())){
							mensagem = "Agendamento de Audi�ncia Executado com Sucesso.";
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + mensagem);
						}
						else{
							mensagem = "Agendamento de audi�ncia n�o realizado. O hor�rio informado j� foi utilizado.";
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);
						}												
						
						audienciaDt.setId_AudienciaTipo("");
						audienciaDt.setAudienciaTipo("");
					}
				}
				return;
			}

				// CURINGA8 - Fun��o Utilizada para Agendar Audi�ncia Automaticamente para um Processo j� existente
			case Configuracao.Curinga8: {
				// Captura processo selecionado
				ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

				// Valida se Agendamento pode ser efetuado
				mensagem = audienciaNe.validarAudienciaAgendamento(audienciaDt, processoDt, usuarioNe.getUsuarioDt());

				// Caso n�o tenha nenhuma restri��o efetua agendamento
				if (mensagem.length() == 0) {
					if (agendarAudienciaAutomaticamente(audienciaDt, processoDt, audienciaNe, request, usuarioNe)) {
						mensagem = "Agendamento de Audi�ncia Executado com Sucesso.";
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + mensagem);
						audienciaDt.setId_AudienciaTipo("");
						audienciaDt.setAudienciaTipo("");
					} else {
						mensagem = "Agendamento de audi�ncia n�o realizado. N�o existe Agenda Dispon�vel.";
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);
					}
				} else redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);
				return;
			}

				// Curinga 9 - Agendamento de Audi�ncias proveniente de Carta Precat�ria
			case Configuracao.Curinga9:
				acao = "/WEB-INF/jsptjgo/AudienciaAgendamento.jsp";

				// Valida se Agendamento proveniente de outra serventia pode ser efetuado
				if (request.getSession().getAttribute("AcessoOutraServentia") != null && 
						(Funcoes.StringToInt(request.getSession().getAttribute("AcessoOutraServentia").toString()) == PendenciaTipoDt.CARTA_PRECATORIA || 
								(Funcoes.StringToInt(request.getSession().getAttribute("AcessoOutraServentia").toString()) == PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC) ||
								(Funcoes.StringToInt(request.getSession().getAttribute("AcessoOutraServentia").toString()) == PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT) ||
								(Funcoes.StringToInt(request.getSession().getAttribute("AcessoOutraServentia").toString()) == PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC)
						)
					) {
					audienciaDt.setAcessoOutraServentia(Boolean.TRUE.toString());
				}

				ProcessoDt processoDt = null;
				String id_Processo = request.getParameter("Id_Processo");
				if (id_Processo != null && id_Processo.length() > 0) {
					processoDt = new ProcessoNe().consultarIdCompleto(id_Processo);
					audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);
					request.getSession().setAttribute("processoDt", processoDt);
				} else{
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
				}

				if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + "N�o � poss�vel executar essa a��o. Motivo: Processo f�sico!");
				} else {
					String tipoAgendamento = request.getParameter("TipoAgendamento");
	
					if (request.getParameter("btnConfirmar") != null) {
						if (tipoAgendamento != null && audienciaDt.getAudienciaTipo() != null && !audienciaDt.getAudienciaTipo().equalsIgnoreCase("")) {
							mensagem = audienciaNe.validarAudienciaAgendamento(audienciaDt, processoDt, usuarioNe.getUsuarioDt());
							if (mensagem.length() == 0) {
								if (tipoAgendamento.equals("1")) {// Agendamento Manual
									request.setAttribute("TituloPagina", "AGENDAMENTO MANUAL DE AUDI�NCIAS - " + audienciaDt.getAudienciaTipo());
									request.setAttribute("fluxo", "0");
									acao = "/WEB-INF/jsptjgo/AudienciaAgendamentoManual.jsp";
								} else if (tipoAgendamento.equals("2")) {// Agendamento Autom�tico
									if (agendarAudienciaAutomaticamente(audienciaDt, processoDt, audienciaNe, request, usuarioNe)) {
										mensagem = "Agendamento de Audi�ncia Executado com Sucesso.";
										redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + mensagem);
										audienciaDt.setId_AudienciaTipo("");
										audienciaDt.setAudienciaTipo("");
									} else request.setAttribute("MensagemErro", "Agendamento de audi�ncia n�o realizado. N�o existe Agenda Dispon�vel.");
								}
							} else request.setAttribute("MensagemErro", mensagem);
						} else request.setAttribute("MensagemErro", "Selecione o Tipo de Audi�ncia e Tipo de Agendamento a ser efetuado.");
					}
					// P�gina anterior
					request.setAttribute("PaginaAnterior", String.valueOf(paginaAtual));
				}
				break;

			// CONSULTAR CARGOS DE UMA SERVENTIA
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
					String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
					request.setAttribute("camposHidden",camposHidden);
					acao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "Audiencia?fluxo=" + request.getParameter("fluxo"));
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = audienciaNe.consultarServentiaCargosAgendaAudienciaJSON(stNomeBusca1, posicaoPaginaAtual, usuarioNe.getUsuarioDt().getId_Serventia(), usuarioNe.getServentiaSubTipoCodigo() );
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}

				// CONSULTAR STATUS DE AUDI�NCIA DE PROCESSO
			case (AudienciaProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Status"};
					String[] lisDescricao = {"AudienciaProcessoStatus"};
					acao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_AudienciaProcessoStatus");
					request.setAttribute("tempBuscaDescricao", "AudienciaProcessoStatus");
					request.setAttribute("tempBuscaPrograma", "Status de Audi�ncia");
					request.setAttribute("tempRetorno", "Audiencia?fluxo=" + request.getParameter("fluxo"));
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AudienciaProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = audienciaNe.consultarDescricaoAudienciaProcessoStatusJSON(stNomeBusca1, usuarioNe.getUsuarioDt().getServentiaTipoCodigo(), posicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}

			// CONSULTAR TIPOS DE AUDI�NCIA
			case (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"AudienciaTipo"};
					String[] lisDescricao = {"AudienciaTipo"};
					String[] camposHidden = {"AudienciaTipoCodigo"};				
					acao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_AudienciaTipo");
					request.setAttribute("tempBuscaDescricao", "AudienciaTipo");
					request.setAttribute("tempBuscaPrograma", "AudienciaTipo");
					request.setAttribute("tempRetorno", "Audiencia");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga9);
					request.setAttribute("PaginaAtual", (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("camposHidden",camposHidden);
				} else {
					String stTemp = "";
					request.setAttribute("PaginaAnterior", (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					stTemp = audienciaNe.consultarDescricaoAudienciaTipoJSON(stNomeBusca1, posicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}

			default: {
				// Seta t�tulo e retorna jsp para redirecionamento
				acao = setAcao_TituloPagina(request, usuarioNe, audienciaNe, audienciaDt);

				// Tratando Bot�o Voltar da Tela de Consulta de Agendas Livres
				if (paginaAnterior == Configuracao.Curinga6) {
					// Captura processo selecionado
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId());
					return;
				} else if (paginaAnterior == Configuracao.Curinga9) {
					acao = "/WEB-INF/jsptjgo/AudienciaAgendamento.jsp";
				}
				break;
			}// FIM CASE DEFAULT
		}// FIM SWITCH

		// SET ATRIBUTOS FINAIS
		// Audienciane
		if(request.isRequestedSessionIdValid()) request.getSession().setAttribute("Audienciane", audienciaNe);
		// Audienciadt
		request.getSession().setAttribute("Audienciadt", audienciaDt);
		// Audi�ncia agendada para um dado processo
		request.setAttribute("AudienciaAgendada", audienciaDtRetornoAgendamento);
		// Seta posi��o da p�gina selecionada para pagina��o
		request.setAttribute("PosicaoPagina", (Funcoes.StringToInt(posicaoPaginaAtual) + 1));
		
		request.setAttribute("podeMovimentar", UsuarioPodeMovimentar(audienciaDt, usuarioNe.getUsuarioDt()));

		// DISPATCHER PARA A JSP
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(acao);
		requestDispatcher.include(request, response);
	}
	
	private boolean UsuarioPodeMovimentar(AudienciaDt audienciaDt, UsuarioDt usuarioDt) {
		if (audienciaDt == null || Funcoes.StringToLong(audienciaDt.getId_Serventia()) == 0) {
			return false;
		}
		if (usuarioDt == null || Funcoes.StringToLong(usuarioDt.getId_Serventia()) == 0){
			return false;
		}
		
		if (usuarioDt.getGrupoTipoCodigoToInt() == GrupoTipoDt.ADVOGADO || usuarioDt.getGrupoTipoCodigoToInt() == GrupoTipoDt.MP 
				|| usuarioDt.getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_ADVOGADO || usuarioDt.getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_MP){
			return false;
		}
		
		return Funcoes.StringToLong(audienciaDt.getId_Serventia()) == Funcoes.StringToLong(usuarioDt.getId_Serventia());
	}

	/**
	 * Chama m�todo para setar o T�tulo correspondente na p�gina, e tamb�m para definir para qual jsp deve ir
	 * 
	 * @param request
	 * @param usuarioNe
	 * @param audienciaNe
	 * @param audienciaDt
	 */
	private String setAcao_TituloPagina(HttpServletRequest request, UsuarioNe usuarioNe, AudienciaNe audienciaNe, AudienciaDt audienciaDt) throws NumberFormatException, Exception {
		this.setTituloPagina(request, usuarioNe, audienciaNe, audienciaDt);
		return this.getAcao(request, usuarioNe, audienciaNe, audienciaDt);
	}

	/**
	 * M�todo respons�vel por atribuir valores �s propriedades de uma inst�ncia de um objeto do tipo "AudienciaDt"
	 * 
	 * @author Keila Sousa Silva
	 * @since 24/03/2009
	 * @param request
	 * @param audienciaDt
	 * @param usuarioSessaoNe
	 * @throws Exception 
	 */
	protected void setAudienciaDt(HttpServletRequest request, UsuarioNe usuarioSessaoNe, AudienciaDt audienciaDt, AudienciaNe audienciaNe) throws Exception {
		// SET PROPRIEDADES DO OBJETO DO TIPO "AUDI�NCIADT"
		// Id da audi�ncia
		audienciaDt.setId(request.getParameter("Id_Audiencia"));
		// Id do tipo da audi�ncia
		audienciaDt.setId_AudienciaTipo(request.getParameter("Id_AudienciaTipo"));
		// C�digo do tipo da audi�ncia
		audienciaDt.setAudienciaTipoCodigo(request.getParameter("AudienciaTipoCodigo"));
		// Tipo da audi�ncia
		audienciaDt.setAudienciaTipo(request.getParameter("AudienciaTipo"));
		// Data da audi�ncia
		audienciaDt.setDataAgendada(request.getParameter("DataAgendada"));
		// Data da movimenta��o da audi�ncia
		audienciaDt.setDataMovimentacao(request.getParameter("DataMovimentacao"));
		// Reservada
		audienciaDt.setReservada(request.getParameter("Reservada"));
		
		// Serventia do usu�rio logado
		audienciaDt.setId_Serventia(usuarioSessaoNe.getUsuarioDt().getId_Serventia());
		audienciaDt.setServentia(usuarioSessaoNe.getUsuarioDt().getServentia());
		
		if ((!usuarioSessaoNe.getUsuarioDt().isAdvogado()) &&  audienciaDt.getAudienciaTipoCodigo() != null && 
		    audienciaDt.getAudienciaTipoCodigo().trim().length() > 0 && 
		    Funcoes.StringToInt(usuarioSessaoNe.getUsuarioDt().getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL &&
		    (Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
		     Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
		     Funcoes.StringToInt(audienciaDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo())) {
			
			ServentiaDt serventiaDt = audienciaNe.consultarServentiaPreprocessualRelacionada(audienciaDt.getId_Serventia());
			
			if (serventiaDt != null) {
				audienciaDt.setId_Serventia(serventiaDt.getId());
				audienciaDt.setServentia(serventiaDt.getServentia());					
			} else {
				throw new MensagemException("N�o foi localizada uma serventia relacionada do tipo Preprocessual.");
			}
			
		} else if ((!usuarioSessaoNe.getUsuarioDt().isAdvogado()) && audienciaDt.getId_AudienciaTipo() != null && 
				   audienciaDt.getId_AudienciaTipo().trim().length() > 0 && 
				   Funcoes.StringToInt(usuarioSessaoNe.getUsuarioDt().getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL) {
				
			AudienciaTipoDt audienciaTipoDt = audienciaNe.consultarIdAudienciaTipo(audienciaDt.getId_AudienciaTipo());
			if (audienciaTipoDt != null && 
			    (Funcoes.StringToInt(audienciaTipoDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
			     Funcoes.StringToInt(audienciaTipoDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
			     Funcoes.StringToInt(audienciaTipoDt.getAudienciaTipoCodigo()) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo())) {
				
				ServentiaDt serventiaDt = audienciaNe.consultarServentiaPreprocessualRelacionada(audienciaDt.getId_Serventia());
				
				if (serventiaDt != null) {
					audienciaDt.setId_Serventia(serventiaDt.getId());
					audienciaDt.setServentia(serventiaDt.getServentia());					
				} else {
					throw new MensagemException("N�o foi localizada uma serventia relacionada do tipo Preprocessual.");
				}
			} 
		}
		
		// Log: id do usu�rio corrente
		audienciaDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
		// Log: IP do computador do qual o usu�rio corrente est� acessando o sistema
		audienciaDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());

		/*
		 * SET PROPRIEDADES DO OBJETO DO TIPO "AUDI�NCIAPROCESSODT"
		 */
		// Verificar se o objeto do tipo "Audi�nciaDt" j� possui uma lista contendo objeto do tipo "Audi�nciaProcessoDt"
		if (audienciaDt.getListaAudienciaProcessoDt().size() == 0) {
			AudienciaProcessoDt audienciaProcessoDt = null;
			audienciaProcessoDt = new AudienciaProcessoDt();
			prepararAudienciaProcessoDt(request, usuarioSessaoNe, audienciaDt, audienciaProcessoDt);
			audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
		} else {
			prepararAudienciaProcessoDt(request, usuarioSessaoNe, audienciaDt, audienciaDt.getAudienciaProcessoDt());
		}

		// DataInicialConsulta
		audienciaDt.setDataInicialConsulta(request.getParameter("DataInicialConsulta"));

		// DataFinalConsulta
		audienciaDt.setDataFinalConsulta(request.getParameter("DataFinalConsulta"));
		
		// INSTANCIAR ATRIBUTO(S) NECESS�RIO(S) PARA AGENDAMENTO/REAGENDAMENTO DE AUDI�NCIA
		// PrazoAgendamentoAudiencia
		audienciaDt.setPrazoAgendamentoAudiencia(request.getParameter("PrazoAgendamentoAudiencia"));
	}

	/**
	 * M�todo respons�vel por atribuir valores �s propriedades de uma inst�ncia de um objeto do tipo "AudienciaProcessoDt"
	 * 
	 * @author Keila Sousa Silva
	 * @since 18/08/2009
	 * @param request
	 * @param usuarioSessaoNe
	 * @param audienciaDt
	 * @param audienciaProcessoDt
	 */
	protected void prepararAudienciaProcessoDt(HttpServletRequest request, UsuarioNe usuarioSessaoNe, AudienciaDt audienciaDt, AudienciaProcessoDt audienciaProcessoDt) {
		// ID DA AUDI�NCIA DO PROCESSO
		audienciaProcessoDt.setId(request.getParameter("Id_AudienciaProcesso"));
		// ID DA AUDI�NCIA
		audienciaProcessoDt.setId_Audiencia(request.getParameter("Id_Audiencia"));
		// ID DO STATUS DA AUDI�NCIA DE PROCESSO
		audienciaProcessoDt.setId_AudienciaProcessoStatus(request.getParameter("Id_AudienciaProcessoStatus"));
		// C�DIGO DO STATUS DA AUDI�NCIA DE PROCESSO
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo(request.getParameter("AudienciaProcessoStatusCodigo"));
		// DESCRI��O DO STATUS DA AUDI�NCIA DE PROCESSO
		audienciaProcessoDt.setAudienciaProcessoStatus(request.getParameter("AudienciaProcessoStatus"));
		// ID DO CARGO DA SERVENTIA
		audienciaProcessoDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		// CARGO DA SERVENTIA
		audienciaProcessoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		// ID DO PROCESSO
		audienciaProcessoDt.setId_Processo(request.getParameter("Id_Processo"));
		audienciaProcessoDt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		// PROCESSODT
		audienciaProcessoDt.setProcessoDt((ProcessoDt) request.getSession().getAttribute("processoDt"));
		// LOG DO USU�RIO
		audienciaProcessoDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
		// IP DO COMPUTADOR
		audienciaProcessoDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());
	}

	/**
	 * M�todo respons�vel por atribuir valores aos atributos iniciais utilizados nas jsp's
	 * 
	 * @author Keila Sousa Silva
	 * @param request
	 * @param paginaAtual
	 */
	private void setAtributosIniciais(HttpServletRequest request, int paginaAtual) {
		request.setAttribute("tempPrograma", "Audiencia");
		request.setAttribute("tempRetorno", "Audiencia");
		request.setAttribute("PaginaAtual", (paginaAtual));

		// Mensagem de sucesso
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		// Mensagem de erro
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

		// Par�metro que indica qual o menu de audi�ncias foi acionado pelo usu�rio. Esse valor vem juntamente com a URL (Link) do menu
		if (request.getParameter("fluxo") != null) {
			request.setAttribute("fluxo", request.getParameter("fluxo"));
		} else request.setAttribute("fluxo", "");

		// ATRIBUTOS NECESS�RIO PARA A CONSULTA DE AUDI�NCIAS POR FILTRO
		request.setAttribute("DataInicialConsulta", request.getParameter("DataInicialConsulta"));
		request.setAttribute("DataFinalConsulta", request.getParameter("DataFinalConsulta"));

		// ATRIBUTO NECESS�RIO PARA O AGENDAMENTO/REAGENDAMENTO DE AUDI�NCIA
		request.setAttribute("PrazoAgendamentoAudiencia", request.getParameter("PrazoAgendamentoAudiencia"));
	}

	/**
	 * M�todo respons�vel por passar um valor para o par�metro "TituloPagina" usado pelas JSP's. "TituloPagina" ser� utilizado como t�tulo nas jsp's.
	 * 
	 * @author Keila Sousa Silva
	 * @author Marielli
	 * @param request
	 * @param usuarioNe
	 * @param audienciaNe
	 * @param audienciaDt
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	private void setTituloPagina(HttpServletRequest request, UsuarioNe usuarioNe, AudienciaNe audienciaNe, AudienciaDt audienciaDt) throws NumberFormatException, Exception {
		int fluxo = -1;
		String audienciaTipo = "";
		String tituloPagina = "AUDI�NCIAS";
		String fluxoAcionado = request.getParameter("fluxo");

		// Verificar submenu acionado
		if (fluxoAcionado != null && fluxoAcionado.length() > 0) fluxo = Funcoes.StringToInt(fluxoAcionado);

		// Captura a descri��o do tipo de audi�ncia selecionado
		if (audienciaDt.getAudienciaTipoCodigo().length() > 0) {
			audienciaTipo = AudienciaTipoDt.Codigo.getDescricao(audienciaDt.getAudienciaTipoCodigo());
			tituloPagina += " " + audienciaTipo;
		} else {
			audienciaTipo = "OUTRAS";
			tituloPagina += " " + audienciaTipo;
		}

		// SWITCH
		switch (fluxo) {
			// AGENDAMENTO MANUAL
			case 0: {
				tituloPagina = "AGENDAMENTO MANUAL DE AUDI�NCIAS - " + audienciaTipo;
				break;
			}
				// PARA HOJE
			case 1: {
				tituloPagina += " PARA HOJE";
				break;
			}
				// PENDENTES
			case 2: {
				tituloPagina += " PENDENTES";
				break;
			}
				// MOVIMENTADAS HOJE
			case 3: {
				tituloPagina += " MOVIMENTADAS HOJE";
				break;
			}
		}// Fim SWITCH

		// Atributo - T�tulo da P�gina
		request.setAttribute("TituloPagina", tituloPagina);
	}

	/**
	 * Nesse m�todo tam�m ser� definida para qual jsp ocorrer� o redirecionamento, definindo um valor para a propriedade "acao"
	 * 
	 * @author Keila Sousa Silva
	 * @author Marielli
	 * @param request
	 * @param usuarioNe
	 * @param audienciaDt
	 * @throws Exception
	 * @throws NumberFormatException
	 */
	private String getAcao(HttpServletRequest request, UsuarioNe usuarioNe, AudienciaNe audienciaNe, AudienciaDt audienciaDt) throws NumberFormatException, Exception {
		String acao = "";
		int fluxo = -1;

		// Verifiar submenu acionado
		String fluxoAcionado = request.getParameter("fluxo");
		if (fluxoAcionado != null && fluxoAcionado.length() > 0) fluxo = Funcoes.StringToInt(fluxoAcionado);

		// Grupo
		//int grupo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo());

		// SWITCH
		// De acordo com Menu Acionado e grupo do usu�rio ser� redirecionado para uma jsp
		switch (fluxo) {
			// AGENDAMENTO MANUAL
			case 0: {
				acao = "/WEB-INF/jsptjgo/AudienciaAgendamentoManual.jsp";
				break;
			}

				// PARA HOJE, PENDENTES, MOVIMENTADAS HOJE
			case 1:
			case 2:
			case 3: {
				switch (grupoTipo) {
//					case GrupoDt.ADVOGADOS:
//					case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
//					case GrupoDt.PROMOTORES: 
					case GrupoTipoDt.ADVOGADO:
					case GrupoTipoDt.MP:
					case GrupoTipoDt.ASSESSOR_ADVOGADO:
					case GrupoTipoDt.ASSESSOR_MP:
					{
						// Quanto tratar de advogados n�o deve visualizar a op��o de movimentar
						request.setAttribute("podeMovimentar", "false");
						acao = "/WEB-INF/jsptjgo/AudienciaLocalizarUsuario.jsp";
						break;
					}

//					case GrupoDt.CONCILIADORES_VARA:
//					case GrupoDt.JUIZES_VARA: 
					case GrupoTipoDt.CONCILIADOR_VARA:
					case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
					{
						request.setAttribute("podeMovimentar", "true");
						acao = "/WEB-INF/jsptjgo/AudienciaLocalizarUsuario.jsp";
						break;
					}

						// DEMAIS GRUPOS
					default: {
						acao = "/WEB-INF/jsptjgo/AudienciaLocalizar.jsp";
						break;
					}

				}
				break;
			}

				// CONSULTAR TODAS PARA TROCA DE RESPONS�VEL
			case 4: {
				acao = "/WEB-INF/jsptjgo/AudienciaProcessoResponsavelLocalizar.jsp";
				break;
			}

				// FILTRO
			default: {
				switch (grupoTipo) {
//					case GrupoDt.ADVOGADOS:
//					case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
//					case GrupoDt.PROMOTORES: 
					case GrupoTipoDt.ADVOGADO:
					case GrupoTipoDt.MP:
					case GrupoTipoDt.ASSESSOR_ADVOGADO:
					case GrupoTipoDt.ASSESSOR_MP:
					{
						// Quanto tratar de advogados n�o deve visualizar a op��o de movimentar
						request.setAttribute("podeMovimentar", "false");
						acao = "/WEB-INF/jsptjgo/AudienciaLocalizarFiltroUsuario.jsp";
						break;
					}

					//case GrupoDt.JUIZES_VARA: 
					case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU: 
					{
						request.setAttribute("podeMovimentar", "true");
						acao = "/WEB-INF/jsptjgo/AudienciaLocalizarFiltro.jsp";
						break;
					}

					//case GrupoDt.CONCILIADORES_VARA: 
					case GrupoTipoDt.CONCILIADOR_VARA:
					{
						request.setAttribute("podeMovimentar", "true");
						acao = "/WEB-INF/jsptjgo/AudienciaLocalizarFiltroUsuario.jsp";
						break;
					}

					default: {
						acao = "/WEB-INF/jsptjgo/AudienciaLocalizarFiltro.jsp";
						break;
					}
				}
				break;
			}
		}

		return acao;
	}

	/**
	 * M�todo respons�vel por analisar qual o tipo de consulta de audi�ncia foi acionado pelo usu�rio para ent�o acionar o m�todo de consulta de
	 * audi�ncias coerente com esse dado
	 * 
	 * @author Keila Sousa Silva
	 * @author Marielli
	 * @param menuAcionado
	 * @param audienciaDt
	 * @param posicaoPaginaAtual
	 * @return List
	 * @throws Exception
	 */
	public List consultarAudiencias(String fluxoAcionado, HttpServletRequest request, UsuarioDt usuarioDt, AudienciaDt audienciaDt, String posicaoPaginaAtual, int paginaAtual, AudienciaNe audienciaNe) throws Exception{
		List listaAudiencias = null;
		int fluxo = -1;

		// Verifica qual o submenu acionado
		if (fluxoAcionado != null && fluxoAcionado.length() > 0) {
			fluxo = Funcoes.StringToInt(fluxoAcionado);
		}

		// SWITCH
		switch (fluxo) {
			// CONSULTA AUDI�NCIAS LIVRES PARA AGENDAMENTO MANUAL
			case 0: {
				listaAudiencias = audienciaNe.consultarAudienciasLivresAgendamentoManual(audienciaDt, audienciaDt.getId_Serventia(), posicaoPaginaAtual);
				break;
			}

				// CONSULTA AUDI�NCIAS PARA HOJE
			case 1: {
				listaAudiencias = audienciaNe.consultarAudienciasParaHoje(usuarioDt, audienciaDt, posicaoPaginaAtual);
				break;
			}

				// CONSULTA AUDI�NCIAS PENDENTES
			case 2: {
				listaAudiencias = audienciaNe.consultarAudienciasPendentes(usuarioDt, audienciaDt, posicaoPaginaAtual);
				break;
			}

				// CONSULTA AUDI�NCIAS MOVIMENTADAS HOJE
			case 3: {
				listaAudiencias = audienciaNe.consultarAudienciasMovimentadasHoje(usuarioDt, audienciaDt, posicaoPaginaAtual);
				break;
			}

				// CONSULTA TODAS AUDI�NCIAS PARA TROCA DE RESPONS�VEL
			case 4: {
				listaAudiencias = audienciaNe.consultarAudienciasPendentesServentia(usuarioDt, audienciaDt, posicaoPaginaAtual);
				break;
			}

				// FILTRO
			default: {
				listaAudiencias = audienciaNe.consultarAudienciasFiltro(usuarioDt, audienciaDt, posicaoPaginaAtual);
				break;
			}
		}// Fim SWITCH

		// tempBuscaId_Audiencia
		request.setAttribute("tempBuscaId_Audiencia", "Id_Audiencia");

		if (listaAudiencias.size() > 0) {
			request.setAttribute("ListaAudiencias", listaAudiencias);
			request.setAttribute("PaginaAtual", (paginaAtual));
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", audienciaNe.getQuantidadePaginas());
			
			//Atributo criado para controlar a montagem da AudienciaLocalizarUsuario.jsp que
			//ficava desconfigurada ao listar as sess�es de 2 grau para o advogado.
			if(usuarioDt.getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.ADVOGADO))) {
				request.setAttribute("consultaAdvogado", true);
			}
			
		} else {
			listaAudiencias = null;
			request.setAttribute("MensagemErro", "Nenhuma Audi�ncia Localizada.");
		}

		return listaAudiencias;
	}

	/**
	 * Consulta os tipos de audi�ncia dispon�veis
	 * 
	 * @author Keila Sousa Silva
	 * @param request
	 * @param audienciaNe
	 * @throws Exception
	 */
	private void consultarAudienciaTipo(HttpServletRequest request, String nomeBusca, String posicaoPaginaAtual, AudienciaNe audienciaNe) throws Exception{
		request.setAttribute("tempBuscaId_AudienciaTipo", "Id_AudienciaTipo");
		request.setAttribute("tempBuscaAudienciaTipo", "AudienciaTipo");

		request.setAttribute("tempRetorno", "Audiencia?PaginaAtual=" + Configuracao.Curinga9);
		List listaAudienciaTipos = audienciaNe.consultarDescricaoAudienciaTipo(nomeBusca, posicaoPaginaAtual);

		if (listaAudienciaTipos.size() > 0) {
			request.setAttribute("ListaAudienciaTipo", listaAudienciaTipos);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", audienciaNe.getQuantidadePaginas());			
		} else request.setAttribute("MensagemErro", "Dados N�o Localizados");
	}

	/**
	 * Realiza procedimentos necess�rios para agendamento autom�tico de audi�ncias
	 * 
	 * @param audienciaDt
	 * @param processoDt
	 * @param audienciaNe
	 * @param request
	 * @param usuarioNe
	 * @return boolean retorno
	 * @throws Exception
	 */
	public boolean agendarAudienciaAutomaticamente(AudienciaDt audienciaDt, ProcessoDt processoDt, AudienciaNe audienciaNe, HttpServletRequest request, UsuarioNe usuarioNe) throws Exception{
		boolean retorno = false;
		setAudienciaDt(request, usuarioNe, audienciaDt, audienciaNe);
		audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);

		audienciaDt = audienciaNe.agendarAudienciaAutomaticamenteProcesso(audienciaDt, usuarioNe.getUsuarioDt());
		// Caso encontre alguma audi�ncia dispon�vel, agenda audi�ncia
		if (audienciaDt != null) retorno = true;

		return retorno;
	}
	
}