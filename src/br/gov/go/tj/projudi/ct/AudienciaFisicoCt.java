package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoFisicoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.AudienciaFisicoNe;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class AudienciaFisicoCt extends Controle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6267356490763957380L;
	
	public int Permissao(){
		return AudienciaDt.CodigoPermissao;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioNe, int paginaAtual, String nomeBusca, String posicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {
		AudienciaFisicoNe audienciaNe;
		AudienciaFisicoDt audienciaDt;
		
		String acao = "/WEB-INF/jsptjgo/AudienciaFisicoAgendamento.jsp"; 
		String tituloPagina = "Agendamento de Audiência";
		String mensagem = "";
		int paginaAnterior = 0;
		

		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));
		
		setAtributosIniciais(request, paginaAtual);
		
		audienciaNe = (AudienciaFisicoNe) request.getSession().getAttribute("AudienciaFisicone");
		if (audienciaNe == null) audienciaNe = new AudienciaFisicoNe();

		audienciaDt = (AudienciaFisicoDt) request.getSession().getAttribute("AudienciaFisicodt");
		if (audienciaDt == null) audienciaDt = new AudienciaFisicoDt();
		
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
				break;
			}
			
			// Consultar Audiências
			case Configuracao.Localizar: {
				this.consultarAudiencias(request.getParameter("fluxo"), request, usuarioNe.getUsuarioDt(), audienciaDt, posicaoPaginaAtual, paginaAtual, audienciaNe);
				acao = "/WEB-INF/jsptjgo/AudienciaFisicoAgendamentoManual.jsp";
				break;
			}
			
			// CONSULTAR TIPOS DE AUDIÊNCIA
			case (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo")==null){
					acao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"AudienciaTipo"};
					String[] lisDescricao = {"AudienciaTipo", "Código"};
					String[] camposHidden = {"AudienciaTipoCodigo"};
					request.setAttribute("tempBuscaId", "Id_AudienciaTipo");
					request.setAttribute("tempBuscaDescricao", "AudienciaTipo");
					request.setAttribute("tempBuscaPrograma", "AudienciaTipo");
					request.setAttribute("tempRetorno", "AudienciaFisico");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("PaginaAtual", (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("camposHidden",camposHidden);
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga9);
				}else{
					String stTemp = "";
					request.setAttribute("PaginaAnterior", (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					stTemp = audienciaNe.consultarDescricaoAudienciaTipoJSON(stNomeBusca1, posicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}
			
			// Consulta de Agendas Livres e Confirmação de Agendamento Manual
			case Configuracao.Curinga6: {
				
				ProcessoFisicoDt processoFisicoDt = null;
				if (audienciaDt != null && audienciaDt.getAudienciaProcessoDt() != null && audienciaDt.getAudienciaProcessoDt().getProcessoFisicoDt() != null)
					processoFisicoDt = audienciaDt.getAudienciaProcessoDt().getProcessoFisicoDt();
				
				//se não existe o processo não sessão houve algum erro, possivel uso de abas e clique na pagina iniciar
				if (processoFisicoDt==null){
					redireciona(response, "Usuario?PaginaAtual=-10");
					return;
				}

				if (request.getParameter("BotaoVoltar") == null) {
					// Se foi passado Id_Audiencia refere-se a confirmação de agendamento/reagendamento
					String id_Audiencia = request.getParameter("Id_Audiencia");
					if (id_Audiencia != null && id_Audiencia.length() > 0) {

						// Buscar o objeto do tipo AudienciaDt selecionado pelo usuário, utilizando para tal o seu id
						audienciaDt = audienciaNe.consultarAudienciaLivreCompleta(id_Audiencia);

						// Se uma audiência foi selecionada para agendamento automático ou manual direciona para confirmação
						if (audienciaDt != null && audienciaDt.getId().length() > 0) {
							audienciaDt.getAudienciaProcessoDt().setProcessoFisicoDt(processoFisicoDt);
							audienciaDt.setProcessoNumero(processoFisicoDt.getNumeroProcesso());
							audienciaDt.setTipoAgendamento("1");
							acao = "/WEB-INF/jsptjgo/AudienciaFisicoAgendamentoConfirmacao.jsp";
							request.setAttribute("Mensagem", "Clique para confirmar o Agendamento da Audiência.");
						}
					} else {
						acao = "/WEB-INF/jsptjgo/AudienciaFisicoAgendamentoManual.jsp?";
						mensagem = audienciaNe.validarAudienciaAgendamento(audienciaDt, usuarioNe.getUsuarioDt());
						if (mensagem.length() == 0) {
							// Redireciona para jsp de listagem de audiências livres
							audienciaDt.setPrazoAgendamentoAudiencia("0");
							audienciaDt.setDataInicialConsulta("");
							audienciaDt.setDataFinalConsulta("");
							return;
						} else {								
							request.setAttribute("MensagemErro", mensagem);	
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
					// Valida se Agendamento pode ser efetuado
					if (audienciaDt.getId().length() > 0) {
						// AGENDAR/REAGENDAR AUDIÊNCIA MANUALMENTE						
						if (audienciaNe.agendarAudienciaManualmente(audienciaDt, usuarioNe.getUsuarioDt())){
							mensagem = "Agendamento de Audiência Executado com Sucesso.";
							redireciona(response, "AudienciaFisico?PaginaAtual=" + Configuracao.Novo + "&MensagemOk=" + mensagem);							
						}
						else{
							mensagem = "Agendamento de audiência não realizado. O horário informado já foi utilizado. Selecione outro horário.";														
						}
					}
				}
				return;
			}
			
			// Curinga 9 - Agendamento de Audiências
			case Configuracao.Curinga9:
				acao = "/WEB-INF/jsptjgo/AudienciaFisicoAgendamento.jsp";
				
				if (paginaAnterior != (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
					mensagem = audienciaNe.validarAudienciaAgendamento(audienciaDt, usuarioNe.getUsuarioDt());
					if (mensagem.length() == 0) {
						if (audienciaDt.isAgendamentoManual()) {// Agendamento Manual
							request.setAttribute("TituloPagina", "AGENDAMENTO MANUAL DE AUDIÊNCIAS - " + audienciaDt.getAudienciaTipo());
							request.setAttribute("fluxo", "0");
							acao = "/WEB-INF/jsptjgo/AudienciaFisicoAgendamentoManual.jsp";
						} else if (audienciaDt.isAgendamentoAutomatico()) {// Agendamento Automático
							if (agendarAudienciaAutomaticamente(audienciaDt, audienciaNe, request, usuarioNe)) {
								mensagem = "Agendamento de Audiência Executado com Sucesso em " + audienciaDt.getDataAgendada() + ".";
								redireciona(response, "AudienciaFisico?PaginaAtual=" + Configuracao.Novo + "&MensagemOk=" + mensagem);
								audienciaDt.setId_AudienciaTipo("");
								audienciaDt.setAudienciaTipo("");
							} else request.setAttribute("MensagemErro", "Agendamento de audiência não realizado. Não existe Agenda Disponível.");
						}
					} else request.setAttribute("MensagemErro", mensagem);		
				}
				break;
		}
		
		// Página anterior
		request.setAttribute("PaginaAnterior", String.valueOf(paginaAtual));		
		// SET ATRIBUTOS FINAIS
		// Audienciane
		request.getSession().setAttribute("AudienciaFisicone", audienciaNe);
		// Audienciadt
		request.getSession().setAttribute("AudienciaFisicodt", audienciaDt);
		// Seta posição da página selecionada para paginação
		request.setAttribute("PosicaoPagina", (Funcoes.StringToInt(posicaoPaginaAtual) + 1));	
		// Atributo - Título da Página
		request.setAttribute("TituloPagina", tituloPagina);

		// DISPATCHER PARA A JSP
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(acao);
		requestDispatcher.include(request, response);
	}
	
	private void setAtributosIniciais(HttpServletRequest request, int paginaAtual) {
		request.setAttribute("tempPrograma", "AudienciaFisico");
		request.setAttribute("tempRetorno", "AudienciaFisico");
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
	
	private void prepararAudienciaProcessoDt(HttpServletRequest request, UsuarioNe usuarioSessaoNe, AudienciaDt audienciaDt, AudienciaProcessoFisicoDt audienciaProcessoDt) {
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
		// PROCESSO FÍSICO
		audienciaProcessoDt.setProcessoNumero(request.getParameter("ProcessoNumeroFisico"));
		// LOG DO USUÁRIO
		audienciaProcessoDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
		// IP DO COMPUTADOR
		audienciaProcessoDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());
	}	
	
	private void setAudienciaDt(HttpServletRequest request, UsuarioNe usuarioSessaoNe, AudienciaFisicoDt audienciaDt, AudienciaNe audienciaNe) throws Exception {
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
		
		if (audienciaDt.getAudienciaTipoCodigo() != null && 
		    audienciaDt.getAudienciaTipoCodigo().trim().length() > 0 && 
		    Funcoes.StringToInt(usuarioSessaoNe.getUsuarioDt().getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL && 
		    audienciaDt.isTipoConciliacaoMediacaoCEJUSC()) {
			
			ServentiaDt serventiaDt = audienciaNe.consultarServentiaPreprocessualRelacionada(audienciaDt.getId_Serventia());
			
			if (serventiaDt != null) {
				audienciaDt.setId_Serventia(serventiaDt.getId());
				audienciaDt.setServentia(serventiaDt.getServentia());					
			} else {
				throw new MensagemException("Não foi localizada uma serventia relacionada do tipo Preprocessual.");
			}
			
		} else if (audienciaDt.getId_AudienciaTipo() != null && 
				   audienciaDt.getId_AudienciaTipo().trim().length() > 0 && 
				   Funcoes.StringToInt(usuarioSessaoNe.getUsuarioDt().getServentiaSubtipoCodigo()) != ServentiaSubtipoDt.PREPROCESSUAL) {
				
			AudienciaTipoDt audienciaTipoDt = audienciaNe.consultarIdAudienciaTipo(audienciaDt.getId_AudienciaTipo());
			if (audienciaTipoDt != null && audienciaDt.isTipoConciliacaoMediacaoCEJUSC()) {
				
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
			AudienciaProcessoFisicoDt audienciaProcessoDt = null;
			audienciaProcessoDt = new AudienciaProcessoFisicoDt();
			prepararAudienciaProcessoDt(request, usuarioSessaoNe, audienciaDt, audienciaProcessoDt);
			audienciaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
		} else {
			prepararAudienciaProcessoDt(request, usuarioSessaoNe, audienciaDt, audienciaDt.getAudienciaProcessoDt());
		}

		// INSTANCIAR ATRIBUTOS NECESSÁRIOS PARA A CONSULTA DE AUDIÊNCIAS POR FILTRO
		// DataInicialConsulta
		audienciaDt.setDataInicialConsulta(request.getParameter("DataInicialConsulta"));
		// DataFinalConsulta
		audienciaDt.setDataFinalConsulta(request.getParameter("DataFinalConsulta"));

		// INSTANCIAR ATRIBUTO(S) NECESSÁRIO(S) PARA AGENDAMENTO/REAGENDAMENTO DE AUDIÊNCIA
		// PrazoAgendamentoAudiencia
		audienciaDt.setPrazoAgendamentoAudiencia(request.getParameter("PrazoAgendamentoAudiencia"));
		
		audienciaDt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		
		audienciaDt.setTipoAgendamento(request.getParameter("TipoAgendamento"));
	}
	
	private boolean agendarAudienciaAutomaticamente(AudienciaFisicoDt audienciaDt, AudienciaFisicoNe audienciaNe, HttpServletRequest request, UsuarioNe usuarioNe) throws Exception{
		boolean retorno = false;

		AudienciaFisicoDt audienciaMarcadaDt = audienciaNe.agendarAudienciaAutomaticamenteProcesso(audienciaDt, usuarioNe.getUsuarioDt());
		// Caso encontre alguma audiência disponível, agenda audiência
		if (audienciaMarcadaDt != null)  {
			audienciaDt.setDataAgendada(audienciaMarcadaDt.getDataAgendada());
			retorno = true;
		}

		return retorno;
	}
	
	private List consultarAudiencias(String fluxoAcionado, HttpServletRequest request, UsuarioDt usuarioDt, AudienciaFisicoDt audienciaDt, String posicaoPaginaAtual, int paginaAtual, AudienciaFisicoNe audienciaNe) throws Exception{
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
		} else {
			listaAudiencias = null;
			request.setAttribute("MensagemErro", "Nenhuma Audiência Localizada.");
		}

		return listaAudiencias;
	}
}