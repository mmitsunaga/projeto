package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaAgendaDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class AudienciaAgendaCt extends AudienciaCtGen {

	private static final long serialVersionUID = -4092246872245689238L;

	public int Permissao() {
		return AudienciaAgendaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioNe, int paginaAtual, String nomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {
		AudienciaNe audienciaNe = null;
		AudienciaAgendaDt audienciaAgendaDt = null;
		String mensagemErros = "";
		int paginaAnterior = 0;
		String stAcao = "/WEB-INF/jsptjgo/AudienciaAgenda.jsp";
		String stNomeBusca1 = "";
		String fluxo = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("tempFluxo1") != null) fluxo = request.getParameter("tempFluxo1");
		
		setAtributosIniciais(request, paginaAtual, paginaAnterior);

		if (request.getParameter("PaginaAnterior") != null) {
			paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		}

		audienciaNe = (AudienciaNe) request.getSession().getAttribute("Audienciane");
		if (audienciaNe == null) {
			audienciaNe = new AudienciaNe();
		}

		audienciaAgendaDt = (AudienciaAgendaDt) request.getSession().getAttribute("AudienciaAgendaDt");
		if (audienciaAgendaDt == null) {
			audienciaAgendaDt = new AudienciaAgendaDt();
		}

		setAudienciaAgendaDt(request, response, audienciaAgendaDt, usuarioNe);

		switch (paginaAtual) {

			case Configuracao.Novo: {
				audienciaAgendaDt.limpar();
				break;
			}
	
			case Configuracao.Salvar: {
				if(fluxo.length() > 0 && fluxo.equalsIgnoreCase("Reservar")) {
					stAcao = "/WEB-INF/jsptjgo/AudienciaAgendaLocalizar.jsp";
				} else {
					request.setAttribute("Mensagem", "Clique para Confirmar a Geração de Agenda(s) para Audiência(s)");
				}
					
				break;
			}
	
			case Configuracao.SalvarResultado: {
				mensagemErros = audienciaNe.validarDadosGeracaoAgendasAudiencias(audienciaAgendaDt);
	
				if (mensagemErros.length() == 0) {
					String[] horariosDuracao = (String[]) request.getParameterValues("HorariosDuracao");
	
					audienciaNe.salvarAgendaAudiencia(audienciaAgendaDt, horariosDuracao);
	
					request.setAttribute("MensagemOk", "Agenda(s) salva(s) com sucesso.");
					audienciaAgendaDt.limpar();
				} else {
					request.setAttribute("MensagemErro", mensagemErros);
				}
				break;
			}
	
			// Reservar audiências livres
			case Configuracao.Curinga7: {
			    String[] audiencias = request.getParameterValues("checkboxAudienciaLivre"); 
			    if (audiencias != null) {
					audienciaNe.reservarAudienciaLivre(audiencias, usuarioNe.getId_Usuario(), usuarioNe.getUsuarioDt().getIpComputadorLog());
					request.setAttribute("MensagemOk", "Agenda(s) reservada(s) com sucesso.");
				} else {
					request.setAttribute("MensagemErro", "Nenhuma agenda foi selecionada para reserva.");
				}
			    stAcao = "/WEB-INF/jsptjgo/AudienciaAgendaLocalizar.jsp";
			   break;
				
			}
			
			// Liberar audiências reservadas
			case Configuracao.Curinga8: {
			    String[] audiencias = request.getParameterValues("checkboxAudienciaLivre"); 
			    if (audiencias != null) {
					audienciaNe.liberarAudienciaLivre(audiencias, usuarioNe.getId_Usuario(), usuarioNe.getUsuarioDt().getIpComputadorLog());
					request.setAttribute("MensagemOk", "Agenda(s) liberada(s) com sucesso.");
				} else {
					request.setAttribute("MensagemErro", "Nenhuma agenda foi selecionada para liberação.");
				}
			    stAcao = "/WEB-INF/jsptjgo/AudienciaAgendaLocalizar.jsp";
			   break;
				
			}
			
			// Excluir audiências
			case Configuracao.Curinga6: {
				 String[] audiencias = request.getParameterValues("checkboxAudienciaLivre"); 
				    if (audiencias != null) {
						audienciaNe.excluirAudienciaLivre(audiencias, usuarioNe.getId_Usuario(), usuarioNe.getUsuarioDt().getIpComputadorLog());
						request.setAttribute("MensagemOk", "Agenda(s) excluída(s) com sucesso.");
					} else {
						request.setAttribute("MensagemErro", "Nenhuma agenda foi selecionada para reserva.");
					}
				    stAcao = "/WEB-INF/jsptjgo/AudienciaAgendaLocalizar.jsp";		
				break;
			}
	
			case Configuracao.Localizar: {
				stAcao = "/WEB-INF/jsptjgo/AudienciaAgendaLocalizar.jsp";
				break;
			}
	
			case Configuracao.LocalizarDWR: {
				String stTemp = "";
				
				if (audienciaAgendaDt.temAudienciaTipo()) {
					stTemp = audienciaNe.consultarAudienciasLivresJSON(audienciaAgendaDt.getId_AudienciaTipo(), audienciaAgendaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), audienciaAgendaDt.getId_Serventia(), posicaoPaginaAtual);
				} else {
					throw new MensagemException("Selecione o tipo da audiência.");
				}
								
				response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
				response.setHeader("Pragma", "no-cache");
				response.setDateHeader("Expires", 0); 
				enviarJSON(response, stTemp);
					
				return;
			}
	
			case (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"AudienciaTipo"};
					String[] lisDescricao = {"AudienciaTipo"};
					request.setAttribute("tempBuscaId", "Id_AudienciaTipo");
					request.setAttribute("tempBuscaDescricao", "AudienciaTipo");
					request.setAttribute("tempBuscaPrograma", "AudienciaTipo");
					request.setAttribute("tempRetorno", "AudienciaAgenda");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("PaginaAtual", (AudienciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("tempPaginaAtualJSON", paginaAnterior);
				}else{
					String stTemp = "";
					stTemp = audienciaNe.consultarDescricaoAudienciaTipoJSON(stNomeBusca1, posicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			}
	
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"Cargo", "Usuario", "CargoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "AudienciaAgenda");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					if (paginaAnterior == Configuracao.Editar) {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					} else if (paginaAnterior == Configuracao.Localizar) {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					} 
				}else{
					String stTemp = "";
					stTemp = audienciaNe.consultarServentiaCargosAgendaAudienciaJSON(stNomeBusca1, posicaoPaginaAtual, usuarioNe.getUsuarioDt().getId_Serventia(), usuarioNe.getServentiaSubTipoCodigo());
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			}

		}
		
		ajusteQuantidadeMaximaDeAudienciasSimultaneas(usuarioNe.getUsuarioDt(), audienciaNe, audienciaAgendaDt);

		request.getSession().setAttribute("Audienciane", audienciaNe);
		request.getSession().setAttribute("AudienciaAgendaDt", audienciaAgendaDt);

		RequestDispatcher requestDispatcher = request.getRequestDispatcher(stAcao);
		requestDispatcher.include(request, response);
	}

	/**
	 * Captura audiências selecionadas para exclusão ou reserva de agendas livres
	 * @throws Exception 
	 */
	private boolean getAudienciasSelecionadas(HttpServletRequest request, AudienciaNe audienciaNe) throws Exception{
		boolean selecionada = false;
		// Buscar a(s) audiência(s) livre(s) marcadas para reserva ou exclusão
		String[] audienciasAgendasSelecionadas = request.getParameterValues("checkboxAudienciaLivre");

		// Consulta dados para cada audiência marcada para reserva
		List listaAudienciasSelecionadas = new ArrayList();
		if (audienciasAgendasSelecionadas != null && audienciasAgendasSelecionadas.length > 0) {
			selecionada = true;
			for (int i = 0; i < audienciasAgendasSelecionadas.length; i++) {
				AudienciaDt audienciaDtReservar = new AudienciaDt();
				audienciaDtReservar = audienciaNe.consultarAudienciaCompleta(audienciasAgendasSelecionadas[i]);
				listaAudienciasSelecionadas.add(audienciaDtReservar);
			}
			// Lista contendo as audiências livres selecionadas pelo usuário para serem reservadas ou excluídas
			request.getSession().setAttribute("ListaAudienciasSelecionadas", listaAudienciasSelecionadas);
		} else {
			request.setAttribute("MensagemErro", "Nenhuma Agenda foi Selecionada.");
		}
		return selecionada;
	}

	/**
	 * Método responsável por criar uma instância da classe AudienciaAgendaDt e alimentar seus dados de acordo com os dados fornecidos pelo usuário para
	 * geração das agendas de audiências
	 */
	protected void setAudienciaAgendaDt(HttpServletRequest request, HttpServletResponse response, AudienciaAgendaDt audienciaAgendaDt, UsuarioNe usuarioSessaoNe) {
		audienciaAgendaDt.setId_AudienciaTipo(request.getParameter("Id_AudienciaTipo"));
		audienciaAgendaDt.setAudienciaTipoCodigo(request.getParameter("AudienciaTipoCodigo"));
		audienciaAgendaDt.setAudienciaTipo(request.getParameter("AudienciaTipo"));
		audienciaAgendaDt.setDataAgendada(request.getParameter("DataAgendada"));
		audienciaAgendaDt.setReservada(request.getParameter("Reservada"));
		audienciaAgendaDt.setId_Serventia(usuarioSessaoNe.getUsuarioDt().getId_Serventia());
		audienciaAgendaDt.setServentia(usuarioSessaoNe.getUsuarioDt().getServentia());
		audienciaAgendaDt.setQuantidadeAudienciasSimultaneas(request.getParameter("QuantidadeAudiencias"));
		audienciaAgendaDt.setDataInicial(request.getParameter("DataInicial"));
		audienciaAgendaDt.setDataFinal(request.getParameter("DataFinal"));
		audienciaAgendaDt.setHorariosDuracao((String[]) request.getParameterValues("HorariosDuracao"));
		audienciaAgendaDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
		audienciaAgendaDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());
		if (audienciaAgendaDt.getListaAudienciaProcessoDt().size() == 0) {
			AudienciaProcessoDt audienciaProcessoDt = new AudienciaProcessoDt();
			audienciaAgendaDt.addListaAudienciaProcessoDt(audienciaProcessoDt);
		}
		setAudienciaProcessoDt(request, usuarioSessaoNe, audienciaAgendaDt.getAudienciaProcessoDt());
	}

	/**
	 * Método responsável por atribuir valores às propriedades de uma instância de um objeto do tipo "AudienciaProcessoDt"
	 */
	protected void setAudienciaProcessoDt(HttpServletRequest request, UsuarioNe usuarioSessaoNe, AudienciaProcessoDt audienciaProcessoDt) {
		audienciaProcessoDt.setId(request.getParameter("Id_AudienciaProcesso"));
		audienciaProcessoDt.setId_Audiencia(request.getParameter("Id_Audiencia"));
		audienciaProcessoDt.setId_AudienciaProcessoStatus(request.getParameter("Id_AudienciaProcessoStatus"));
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo(request.getParameter("AudienciaProcessoStatusCodigo"));
		audienciaProcessoDt.setAudienciaProcessoStatus(request.getParameter("AudienciaProcessoStatus"));
		audienciaProcessoDt.setId_UsuarioLog(usuarioSessaoNe.getId_Usuario());
		audienciaProcessoDt.setIpComputadorLog(usuarioSessaoNe.getUsuarioDt().getIpComputadorLog());

		// Verifica se usuário pode selecionar o ServentiaCargo no momento da criação de agendas ou se deve ser recuperado da sessão
		boolean podeSelecionarCargo = true;
		if (Funcoes.StringToInt(usuarioSessaoNe.getUsuarioDt().getGrupoCodigo()) == GrupoDt.CONCILIADORES_VARA) {
			// Quando for conciliador a agenda será para o cargo dele
			audienciaProcessoDt.setId_ServentiaCargo(usuarioSessaoNe.getUsuarioDt().getId_ServentiaCargo());
			podeSelecionarCargo = false;
		} else {
			// Seta dados do Cargo selecionado pelo usuário
			audienciaProcessoDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
			audienciaProcessoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		}
		request.setAttribute("podeSelecionarCargo", podeSelecionarCargo);
	}

	/**
	 * Método responsável por atribuir valores aos atributos iniciais utilizados nas jsp's
	 */
	protected void setAtributosIniciais(HttpServletRequest request, int paginaAtual, int paginaAnterior) {
		request.setAttribute("tempPrograma", "AudienciaAgenda");
		request.setAttribute("tempRetorno", "AudienciaAgenda");
		request.setAttribute("PaginaAtual", (paginaAtual));
		request.setAttribute("PaginaAnterior", String.valueOf(paginaAtual));
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
	}

	/**
	 * Consultar as audiências livres (agendas livres) de um dado tipo, menos do tipo "Sessão de 2º Grau" e/ou de um cargo da serventia (serventia na qual
	 * o usuário corrente está logado)
	 */
	public void consultarAudienciasLivres(HttpServletRequest request, String posicaoPaginaAtual, AudienciaAgendaDt audienciaAgendaDt, AudienciaNe audienciaNe) throws Exception {

		if (!(audienciaAgendaDt.getId_AudienciaTipo().equalsIgnoreCase(""))) {
			List listaAudiencias = audienciaNe.consultarAudienciasLivres(audienciaAgendaDt.getId_AudienciaTipo(), audienciaAgendaDt.getAudienciaProcessoDt().getId_ServentiaCargo(), audienciaAgendaDt.getId_Serventia(), posicaoPaginaAtual);

			if (listaAudiencias.size() > 0) {
				request.setAttribute("ListaAudienciasLivres", listaAudiencias);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
				request.setAttribute("QuantidadePaginas", audienciaNe.getQuantidadePaginas());
				request.setAttribute("Id_ServentiaCargo", audienciaAgendaDt.getAudienciaProcessoDt().getId_ServentiaCargo());
				request.setAttribute("Id_AudienciaTipo", audienciaAgendaDt.getId_AudienciaTipo());
				request.setAttribute("Id_Serventia", audienciaAgendaDt.getId_Serventia());
			} else {
				request.setAttribute("MensagemErro", "Nenhuma agenda disponível.");
			}
		} else {
			request.setAttribute("MensagemErro", "Selecione o tipo da audiência.");

			if ((audienciaAgendaDt.getId_AudienciaTipo().equalsIgnoreCase("")) && !(audienciaAgendaDt.getAudienciaProcessoDt().getId_ServentiaCargo().equalsIgnoreCase(""))) {
				request.setAttribute("MensagemErro", "Selecione o tipo da audiência.");
			}
		}
	}
	
	protected void ajusteQuantidadeMaximaDeAudienciasSimultaneas(UsuarioDt usuarioLogado, AudienciaNe audienciaNe, AudienciaAgendaDt audienciaAgendaDt) throws Exception {		
		if (Funcoes.StringToInt(audienciaAgendaDt.getQuantidadeMaximaAudienciasSimultaneas()) > 0) return;
		
		audienciaAgendaDt.setQuantidadeMaximaAudienciasSimultaneas(String.valueOf(AudienciaAgendaDt.QUANTIDADE_MAXIMA_AUDIENCIAS_SIMULTANEAS_PADRAO));	 
	}
}