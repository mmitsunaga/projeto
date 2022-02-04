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

			// Consultar Audiências
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

			// Consulta de Agendas Livres e Confirmação de Agendamento Manual
			case Configuracao.Curinga6: {
				ProcessoDt processoDt=null;
				
				processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
				//se não existe o processo não sessão houve algum erro, possivel uso de abas e clique na pagina iniciar
				if (processoDt==null){
					redireciona(response, "Usuario?PaginaAtual=-10");
					return;
				}

				setTituloPagina(request, usuarioNe, audienciaNe, audienciaDt);

				if (request.getParameter("BotaoVoltar") == null) {
					// Se foi passado Id_Audiencia refere-se a confirmação de agendamento/reagendamento
					String id_Audiencia = request.getParameter("Id_Audiencia");
					if (id_Audiencia != null && id_Audiencia.length() > 0) {

						// Buscar o objeto do tipo AudienciaDt selecionado pelo usuário, utilizando para tal o seu id
						audienciaDt = audienciaNe.consultarAudienciaLivreCompleta(id_Audiencia);

						// Se acesso de outra serventia seta no Dt de audiencia essa informação
						if (request.getSession().getAttribute("AcessoOutraServentia") != null && (Funcoes.StringToInt(request.getSession().getAttribute("AcessoOutraServentia").toString()) == PendenciaTipoDt.CARTA_PRECATORIA)) {
							audienciaDt.setAcessoOutraServentia(String.valueOf(PendenciaTipoDt.CARTA_PRECATORIA));
						}

						// Se uma audiência foi selecionada para agendamento automático ou manual direciona para confirmação
						if (audienciaDt != null && audienciaDt.getId().length() > 0) {
							audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoDt);
							acao = "/WEB-INF/jsptjgo/AudienciaAgendamentoConfirmacao.jsp";
							request.setAttribute("Mensagem", "Clique para confirmar o Agendamento da Audiência.");
						}
					} else {
						mensagem = audienciaNe.validarAudienciaAgendamento(audienciaDt, processoDt, usuarioNe.getUsuarioDt());
						if (mensagem.length() == 0) {
							// Redireciona para jsp de listagem de audiências livres
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
				// Página anterior
				request.setAttribute("PaginaAnterior", String.valueOf(paginaAtual));
				break;
			}

			// Função Utilizada para Agendamento Manual de Audiência para um Processo já existente
			case Configuracao.Curinga7: {
				// Verificar se algum menu foi acionado
				if (request.getParameter("fluxo") != null) {
					// Captura processo selecionado
					ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

					// Valida se Agendamento pode ser efetuado
					if (audienciaDt.getId().length() > 0) {
						// AGENDAR/REAGENDAR AUDIÊNCIA MANUALMENTE						
						if (audienciaNe.agendarAudienciaManualmente(audienciaDt, usuarioNe.getUsuarioDt())){
							mensagem = "Agendamento de Audiência Executado com Sucesso.";
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + mensagem);
						}
						else{
							mensagem = "Agendamento de audiência não realizado. O horário informado já foi utilizado.";
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);
						}												
						
						audienciaDt.setId_AudienciaTipo("");
						audienciaDt.setAudienciaTipo("");
					}
				}
				return;
			}

				// CURINGA8 - Função Utilizada para Agendar Audiência Automaticamente para um Processo já existente
			case Configuracao.Curinga8: {
				// Captura processo selecionado
				ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

				// Valida se Agendamento pode ser efetuado
				mensagem = audienciaNe.validarAudienciaAgendamento(audienciaDt, processoDt, usuarioNe.getUsuarioDt());

				// Caso não tenha nenhuma restrição efetua agendamento
				if (mensagem.length() == 0) {
					if (agendarAudienciaAutomaticamente(audienciaDt, processoDt, audienciaNe, request, usuarioNe)) {
						mensagem = "Agendamento de Audiência Executado com Sucesso.";
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + mensagem);
						audienciaDt.setId_AudienciaTipo("");
						audienciaDt.setAudienciaTipo("");
					} else {
						mensagem = "Agendamento de audiência não realizado. Não existe Agenda Disponível.";
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);
					}
				} else redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);
				return;
			}

				// Curinga 9 - Agendamento de Audiências proveniente de Carta Precatória
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
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + "Não é possível executar essa ação. Motivo: Processo físico!");
				} else {
					String tipoAgendamento = request.getParameter("TipoAgendamento");
	
					if (request.getParameter("btnConfirmar") != null) {
						if (tipoAgendamento != null && audienciaDt.getAudienciaTipo() != null && !audienciaDt.getAudienciaTipo().equalsIgnoreCase("")) {
							mensagem = audienciaNe.validarAudienciaAgendamento(audienciaDt, processoDt, usuarioNe.getUsuarioDt());
							if (mensagem.length() == 0) {
								if (tipoAgendamento.equals("1")) {// Agendamento Manual
									request.setAttribute("TituloPagina", "AGENDAMENTO MANUAL DE AUDIÊNCIAS - " + audienciaDt.getAudienciaTipo());
									request.setAttribute("fluxo", "0");
									acao = "/WEB-INF/jsptjgo/AudienciaAgendamentoManual.jsp";
								} else if (tipoAgendamento.equals("2")) {// Agendamento Automático
									if (agendarAudienciaAutomaticamente(audienciaDt, processoDt, audienciaNe, request, usuarioNe)) {
										mensagem = "Agendamento de Audiência Executado com Sucesso.";
										redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + mensagem);
										audienciaDt.setId_AudienciaTipo("");
										audienciaDt.setAudienciaTipo("");
									} else request.setAttribute("MensagemErro", "Agendamento de audiência não realizado. Não existe Agenda Disponível.");
								}
							} else request.setAttribute("MensagemErro", mensagem);
						} else request.setAttribute("MensagemErro", "Selecione o Tipo de Audiência e Tipo de Agendamento a ser efetuado.");
					}
					// Página anterior
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

				// CONSULTAR STATUS DE AUDIÊNCIA DE PROCESSO
			case (AudienciaProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Status"};
					String[] lisDescricao = {"AudienciaProcessoStatus"};
					acao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_AudienciaProcessoStatus");
					request.setAttribute("tempBuscaDescricao", "AudienciaProcessoStatus");
					request.setAttribute("tempBuscaPrograma", "Status de Audiência");
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

			// CONSULTAR TIPOS DE AUDIÊNCIA
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
				// Seta título e retorna jsp para redirecionamento
				acao = setAcao_TituloPagina(request, usuarioNe, audienciaNe, audienciaDt);

				// Tratando Botão Voltar da Tela de Consulta de Agendas Livres
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
		// Audiência agendada para um dado processo
		request.setAttribute("AudienciaAgendada", audienciaDtRetornoAgendamento);
		// Seta posição da página selecionada para paginação
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
	 * Chama método para setar o Título correspondente na página, e também para definir para qual jsp deve ir
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
	 * Método responsável por atribuir valores às propriedades de uma instância de um objeto do tipo "AudienciaDt"
	 * 
	 * @author Keila Sousa Silva
	 * @since 24/03/2009
	 * @param request
	 * @param audienciaDt
	 * @param usuarioSessaoNe
	 * @throws Exception 
	 */
	protected void setAudienciaDt(HttpServletRequest request, UsuarioNe usuarioSessaoNe, AudienciaDt audienciaDt, AudienciaNe audienciaNe) throws Exception {
		// SET PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIADT"
		// Id da audiência
		audienciaDt.setId(request.getParameter("Id_Audiencia"));
		// Id do tipo da audiência
		audienciaDt.setId_AudienciaTipo(request.getParameter("Id_AudienciaTipo"));
		// Código do tipo da audiência
		audienciaDt.setAudienciaTipoCodigo(request.getParameter("AudienciaTipoCodigo"));
		// Tipo da audiência
		audienciaDt.setAudienciaTipo(request.getParameter("AudienciaTipo"));
		// Data da audiência
		audienciaDt.setDataAgendada(request.getParameter("DataAgendada"));
		// Data da movimentação da audiência
		audienciaDt.setDataMovimentacao(request.getParameter("DataMovimentacao"));
		// Reservada
		audienciaDt.setReservada(request.getParameter("Reservada"));
		
		// Serventia do usuário logado
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
				throw new MensagemException("Não foi localizada uma serventia relacionada do tipo Preprocessual.");
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
					throw new MensagemException("Não foi localizada uma serventia relacionada do tipo Preprocessual.");
				}
			} 
		}
		
		// Log: id do usuário corrente
		audienciaDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
		// Log: IP do computador do qual o usuário corrente está acessando o sistema
		audienciaDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());

		/*
		 * SET PROPRIEDADES DO OBJETO DO TIPO "AUDIÊNCIAPROCESSODT"
		 */
		// Verificar se o objeto do tipo "AudiênciaDt" já possui uma lista contendo objeto do tipo "AudiênciaProcessoDt"
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
		
		// INSTANCIAR ATRIBUTO(S) NECESSÁRIO(S) PARA AGENDAMENTO/REAGENDAMENTO DE AUDIÊNCIA
		// PrazoAgendamentoAudiencia
		audienciaDt.setPrazoAgendamentoAudiencia(request.getParameter("PrazoAgendamentoAudiencia"));
	}

	/**
	 * Método responsável por atribuir valores às propriedades de uma instância de um objeto do tipo "AudienciaProcessoDt"
	 * 
	 * @author Keila Sousa Silva
	 * @since 18/08/2009
	 * @param request
	 * @param usuarioSessaoNe
	 * @param audienciaDt
	 * @param audienciaProcessoDt
	 */
	protected void prepararAudienciaProcessoDt(HttpServletRequest request, UsuarioNe usuarioSessaoNe, AudienciaDt audienciaDt, AudienciaProcessoDt audienciaProcessoDt) {
		// ID DA AUDIÊNCIA DO PROCESSO
		audienciaProcessoDt.setId(request.getParameter("Id_AudienciaProcesso"));
		// ID DA AUDIÊNCIA
		audienciaProcessoDt.setId_Audiencia(request.getParameter("Id_Audiencia"));
		// ID DO STATUS DA AUDIÊNCIA DE PROCESSO
		audienciaProcessoDt.setId_AudienciaProcessoStatus(request.getParameter("Id_AudienciaProcessoStatus"));
		// CÓDIGO DO STATUS DA AUDIÊNCIA DE PROCESSO
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo(request.getParameter("AudienciaProcessoStatusCodigo"));
		// DESCRIÇÃO DO STATUS DA AUDIÊNCIA DE PROCESSO
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
		// LOG DO USUÁRIO
		audienciaProcessoDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
		// IP DO COMPUTADOR
		audienciaProcessoDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());
	}

	/**
	 * Método responsável por atribuir valores aos atributos iniciais utilizados nas jsp's
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

		// Parâmetro que indica qual o menu de audiências foi acionado pelo usuário. Esse valor vem juntamente com a URL (Link) do menu
		if (request.getParameter("fluxo") != null) {
			request.setAttribute("fluxo", request.getParameter("fluxo"));
		} else request.setAttribute("fluxo", "");

		// ATRIBUTOS NECESSÁRIO PARA A CONSULTA DE AUDIÊNCIAS POR FILTRO
		request.setAttribute("DataInicialConsulta", request.getParameter("DataInicialConsulta"));
		request.setAttribute("DataFinalConsulta", request.getParameter("DataFinalConsulta"));

		// ATRIBUTO NECESSÁRIO PARA O AGENDAMENTO/REAGENDAMENTO DE AUDIÊNCIA
		request.setAttribute("PrazoAgendamentoAudiencia", request.getParameter("PrazoAgendamentoAudiencia"));
	}

	/**
	 * Método responsável por passar um valor para o parâmetro "TituloPagina" usado pelas JSP's. "TituloPagina" será utilizado como título nas jsp's.
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
		String tituloPagina = "AUDIÊNCIAS";
		String fluxoAcionado = request.getParameter("fluxo");

		// Verificar submenu acionado
		if (fluxoAcionado != null && fluxoAcionado.length() > 0) fluxo = Funcoes.StringToInt(fluxoAcionado);

		// Captura a descrição do tipo de audiência selecionado
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
				tituloPagina = "AGENDAMENTO MANUAL DE AUDIÊNCIAS - " + audienciaTipo;
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

		// Atributo - Título da Página
		request.setAttribute("TituloPagina", tituloPagina);
	}

	/**
	 * Nesse método tamém será definida para qual jsp ocorrerá o redirecionamento, definindo um valor para a propriedade "acao"
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
		// De acordo com Menu Acionado e grupo do usuário será redirecionado para uma jsp
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
						// Quanto tratar de advogados não deve visualizar a opção de movimentar
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

				// CONSULTAR TODAS PARA TROCA DE RESPONSÁVEL
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
						// Quanto tratar de advogados não deve visualizar a opção de movimentar
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
	 * Método responsável por analisar qual o tipo de consulta de audiência foi acionado pelo usuário para então acionar o método de consulta de
	 * audiências coerente com esse dado
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
			// CONSULTA AUDIÊNCIAS LIVRES PARA AGENDAMENTO MANUAL
			case 0: {
				listaAudiencias = audienciaNe.consultarAudienciasLivresAgendamentoManual(audienciaDt, audienciaDt.getId_Serventia(), posicaoPaginaAtual);
				break;
			}

				// CONSULTA AUDIÊNCIAS PARA HOJE
			case 1: {
				listaAudiencias = audienciaNe.consultarAudienciasParaHoje(usuarioDt, audienciaDt, posicaoPaginaAtual);
				break;
			}

				// CONSULTA AUDIÊNCIAS PENDENTES
			case 2: {
				listaAudiencias = audienciaNe.consultarAudienciasPendentes(usuarioDt, audienciaDt, posicaoPaginaAtual);
				break;
			}

				// CONSULTA AUDIÊNCIAS MOVIMENTADAS HOJE
			case 3: {
				listaAudiencias = audienciaNe.consultarAudienciasMovimentadasHoje(usuarioDt, audienciaDt, posicaoPaginaAtual);
				break;
			}

				// CONSULTA TODAS AUDIÊNCIAS PARA TROCA DE RESPONSÁVEL
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
			//ficava desconfigurada ao listar as sessões de 2 grau para o advogado.
			if(usuarioDt.getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.ADVOGADO))) {
				request.setAttribute("consultaAdvogado", true);
			}
			
		} else {
			listaAudiencias = null;
			request.setAttribute("MensagemErro", "Nenhuma Audiência Localizada.");
		}

		return listaAudiencias;
	}

	/**
	 * Consulta os tipos de audiência disponíveis
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
		} else request.setAttribute("MensagemErro", "Dados Não Localizados");
	}

	/**
	 * Realiza procedimentos necessários para agendamento automático de audiências
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
		// Caso encontre alguma audiência disponível, agenda audiência
		if (audienciaDt != null) retorno = true;

		return retorno;
	}
	
}