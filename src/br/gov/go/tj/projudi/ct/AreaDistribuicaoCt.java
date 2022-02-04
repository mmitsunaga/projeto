package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AreaDistribuicaoCt extends AreaDistribuicaoCtGen {

    private static final long serialVersionUID = -5489724073231463504L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AreaDistribuicaoDt AreaDistribuicaodt;
		AreaDistribuicaoNe AreaDistribuicaone;
		String stNomeBusca1 = "";
		boolean turmaJulgadora = false;

		ProcessoNe Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null)
			Processone = new ProcessoNe();

		ProcessoCadastroDt ProcessoCiveldt = (ProcessoCadastroDt) request.getSession().getAttribute("ProcessoCadastroDt");
		if (ProcessoCiveldt == null) {
			ProcessoCiveldt = new ProcessoCadastroDt();
		}
		
		if (request.getSession().getAttribute("turmaJulgadora") != null){
			turmaJulgadora = Funcoes.StringToBoolean(String.valueOf(request.getSession().getAttribute("turmaJulgadora")));
		}

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String Mensagem = "";
		String stId = "";

		String stAcao = "/WEB-INF/jsptjgo/AreaDistribuicao.jsp";

		request.setAttribute("tempPrograma", "AreaDistribuicao");

		AreaDistribuicaone = (AreaDistribuicaoNe) request.getSession().getAttribute("AreaDistribuicaone");
		if (AreaDistribuicaone == null) AreaDistribuicaone = new AreaDistribuicaoNe();

		AreaDistribuicaodt = (AreaDistribuicaoDt) request.getSession().getAttribute("AreaDistribuicaodt");
		if (AreaDistribuicaodt == null) AreaDistribuicaodt = new AreaDistribuicaoDt();

		AreaDistribuicaodt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		AreaDistribuicaodt.setAreaDistribuicaoCodigo(request.getParameter("AreaDistribuicaoCodigo"));
		
		if(request.getParameter("Id_Forum")!= null && !AreaDistribuicaodt.getId_Forum().equals(request.getParameter("Id_Forum")) && request.getParameter("Forum") != null && !AreaDistribuicaodt.getForum().equals(request.getParameter("Forum")) && paginaatual == -1) {
			ForumDt forum = AreaDistribuicaone.consultarForum(request.getParameter("Id_Forum"));
			if(forum != null){
				AreaDistribuicaodt.setId_Comarca(forum.getId_Comarca());
				AreaDistribuicaodt.setComarca(forum.getComarca());
				AreaDistribuicaodt.setComarcaCodigo(forum.getComarcaCodigo());
			}
		} else {
			AreaDistribuicaodt.setId_Comarca(request.getParameter("Id_Comarca"));
			AreaDistribuicaodt.setComarca(request.getParameter("Comarca"));
			AreaDistribuicaodt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));
		}
		
		AreaDistribuicaodt.setId_Forum(request.getParameter("Id_Forum"));
		AreaDistribuicaodt.setForum(request.getParameter("Forum"));
		AreaDistribuicaodt.setId_ServentiaSubtipo(request.getParameter("Id_ServentiaSubtipo"));
		AreaDistribuicaodt.setServentiaSubtipo(request.getParameter("ServentiaSubtipo"));
		//AreaDistribuicaodt.setId_AreaDistribuicaoRelacionada(request.getParameter("Id_AreaDistribuicaoRelacionada"));
		//AreaDistribuicaodt.setAreaDistribuicaoRelacionada(request.getParameter("AreaDistribuicaoRelacionada"));
		AreaDistribuicaodt.setServentiaSubtipoCodigo(request.getParameter("ServentiaSubtipoCodigo"));
		AreaDistribuicaodt.setForumCodigo(request.getParameter("ForumCodigo"));

		AreaDistribuicaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		AreaDistribuicaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());	
		
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				AreaDistribuicaone.excluir(AreaDistribuicaodt);
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir:
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Área de Distribuição"};
					String[] lisDescricao = {"Área de Distribuição"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao","AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma","Área de Distribuição");			
					request.setAttribute("tempRetorno","AreaDistribuicao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp;
	
					String Id_Comarca = request.getParameter("filtroTabela");
					if (StringUtils.isNotEmpty(Id_Comarca)) {
						stTemp = selecionarConsultaAreaDistribuicao(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual, stNomeBusca1, Processone, ProcessoCiveldt, Id_Comarca, turmaJulgadora);
					} else {
						stTemp = AreaDistribuicaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					}
	
					enviarJSON(response, stTemp);
	
					return;
				}
				
				break;

			case Configuracao.Novo:
				AreaDistribuicaodt.limpar();
				break;
				
			case Configuracao.Salvar:
				if (request.getParameter("Curinga").equals("D")) {
					request.setAttribute("Mensagem", "Clique para Desbloquear a area de distribuição " + AreaDistribuicaodt.getAreaDistribuicao());
					request.setAttribute("Curinga", "D");					
				} else if (request.getParameter("Curinga").equals("A")) {
					request.setAttribute("Mensagem", "Clique para Ativar a area de distribuição " + AreaDistribuicaodt.getAreaDistribuicao());
					request.setAttribute("Curinga", "A");					
				} else if (request.getParameter("Curinga").equals("B")) {
					request.setAttribute("Mensagem", "Clique para Bloquear a area de distribuição " + AreaDistribuicaodt.getAreaDistribuicao());
					request.setAttribute("Curinga", "B");					
				} else if (request.getParameter("Curinga").equals("I")) {
					request.setAttribute("Mensagem", "Clique para Inativar a area de distribuição " + AreaDistribuicaodt.getAreaDistribuicao());
					request.setAttribute("Curinga", "I");					
				}					
				break;

			case Configuracao.SalvarResultado:
				if (request.getParameter("Curinga").equals("D")) {
					AreaDistribuicaone.ativarAreaDistribuicao(AreaDistribuicaodt);
					request.setAttribute("MensagemOk", "Area de Distribuição Desbloqueada com sucesso.");
					AreaDistribuicaodt.setCodigoTemp(String.valueOf(AreaDistribuicaoDt.ATIVO));
				} else if (request.getParameter("Curinga").equals("A")) {
					AreaDistribuicaone.ativarAreaDistribuicao(AreaDistribuicaodt);
					request.setAttribute("MensagemOk", "Area de Distribuição Ativada com sucesso.");
					AreaDistribuicaodt.setCodigoTemp(String.valueOf(AreaDistribuicaoDt.ATIVO));
				} else if (request.getParameter("Curinga").equals("B")) {
					AreaDistribuicaone.bloquearAreaDistribuicao(AreaDistribuicaodt);
					request.setAttribute("MensagemOk", "Area de Distribuição Bloqueada com sucesso.");
					AreaDistribuicaodt.setCodigoTemp(String.valueOf(AreaDistribuicaoDt.BLOQUEADO));
				} else if (request.getParameter("Curinga").equals("I")) {
					AreaDistribuicaone.inativarAreaDistribuicao(AreaDistribuicaodt);
					request.setAttribute("MensagemOk", "Area de Distribuição Inativada com sucesso.");
					AreaDistribuicaodt.setCodigoTemp(String.valueOf(AreaDistribuicaoDt.INATIVO));
				} else {	
					Mensagem = AreaDistribuicaone.Verificar(AreaDistribuicaodt);
					if (Mensagem.length() == 0) {
						AreaDistribuicaone.salvar(AreaDistribuicaodt);
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
					} else request.setAttribute("MensagemErro", Mensagem);
				}
				break;

			case (ForumDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Forum"};
					String[] lisDescricao = {"Forum"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Forum");
					request.setAttribute("tempBuscaDescricao","Forum");
					request.setAttribute("tempBuscaPrograma","Forum");			
					request.setAttribute("tempRetorno","AreaDistribuicao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ForumDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = AreaDistribuicaone.consultarDescricaoForumJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;				

			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Forum");			
					request.setAttribute("tempRetorno","AreaDistribuicao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = AreaDistribuicaone.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;

			case (ServentiaSubtipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaSubtipo"};
					String[] lisDescricao = {"ServentiaSubtipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ServentiaSubtipo");
					request.setAttribute("tempBuscaDescricao","ServentiaSubtipo");
					request.setAttribute("tempBuscaPrograma","ServentiaSubtipo");			
					request.setAttribute("tempRetorno","AreaDistribuicao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaSubtipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					request.setAttribute("tempRetorno","AreaDistribuicao");	
					stTemp = AreaDistribuicaone.consultarDescricaoServentiaSubtipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
			break;
			default:
				stId = request.getParameter("Id_AreaDistribuicao");
				if (stId != null && !stId.isEmpty()) { 
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(AreaDistribuicaodt.getId())) {
						AreaDistribuicaodt.limpar();
						AreaDistribuicaodt = AreaDistribuicaone.consultarId(stId);
						
						//aplica o setListaServentias para obter a lista					
						AreaDistribuicaodt.setListaServentias(AreaDistribuicaone.consultarListaServentias(stId));					
					}
				}
				break;
		}

		request.getSession().setAttribute("AreaDistribuicaodt", AreaDistribuicaodt);
		request.getSession().setAttribute("AreaDistribuicaone", AreaDistribuicaone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	protected String selecionarConsultaAreaDistribuicao(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual, String stNomeBusca1, ProcessoNe processoNe, ProcessoCadastroDt processoCadastroDt, String Id_Comarca, boolean turmaJulgadora) throws Exception {
		String grauProcesso = processoCadastroDt.getGrauProcesso(); // value do radio, valores possíveis: 1, 2 ou 3
		String tipoProcesso = processoCadastroDt.getTipoProcesso(); // value do radio, valores possíveis: 1 ou 2
		String assistenciaProcesso = processoCadastroDt.getAssistenciaProcesso(); // value do radio, valores possíveis: 1, 2 ou 3
		String dependenciaProcesso = processoCadastroDt.getDependenciaProcesso(); // value do radio, valores possíveis: 1 ou 2

		switch (grauProcesso) {
		case "1":
			switch (tipoProcesso) {
			case "1":
				switch (assistenciaProcesso) {
				case "1":
				case "3":
				default:
					return processoNe.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, Id_Comarca, UsuarioSessao.getUsuarioDt().getId_Serventia(), processoCadastroDt.isComCusta(), PosicaoPaginaAtual);
				case "2":
					switch (dependenciaProcesso) {
					case "1":
					case "2":
						return "";
					default:
						return processoNe.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, Id_Comarca, null, true, PosicaoPaginaAtual);
					}
				}
			case "2":
				switch (assistenciaProcesso) {
				case "1":
				case "3":
					return processoNe.consultarAreasDistribuicaoPrimeiroGrauCriminalJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual);
				case "2":
					return "";
				default:
					return processoNe.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, Id_Comarca, UsuarioSessao.getUsuarioDt().getId_Serventia(), processoCadastroDt.isComCusta(), PosicaoPaginaAtual);
				}
			default:
				return processoNe.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, Id_Comarca, UsuarioSessao.getUsuarioDt().getId_Serventia(), processoCadastroDt.isComCusta(), PosicaoPaginaAtual);
			}
		case "2":
			switch (tipoProcesso) {
			case "1":
				switch (assistenciaProcesso) {
				case "1":
				case "3":
				default:
					return processoNe.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
				case "2":
					switch (dependenciaProcesso) {
					case "1":
					case "2":
						return "";
					default:
						return processoNe.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
					}
				}
			case "2":
				switch (assistenciaProcesso) {
				case "1":
				case "3":
					return processoNe.consultarAreasDistribuicaoSegundoGrauCriminalJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
				case "2":
					return "";
				default:
					return processoNe.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
				}
			default:
				return processoNe.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
			}
		case "3":
			switch (tipoProcesso) {
			case "1":
				switch (assistenciaProcesso) {
				case "1":
					return processoNe.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
				case "2":
					switch (dependenciaProcesso) {
					case "1":
					case "2":
						return "";
					default:
						return processoNe.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
					}
				case "3":
					return processoNe.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
				default:
					return processoNe.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, Id_Comarca, UsuarioSessao.getUsuarioDt().getId_Serventia(), processoCadastroDt.isComCusta(), PosicaoPaginaAtual);
				}
			case "2":
				switch (assistenciaProcesso) {
				case "1":
				case "3":
					return processoNe.consultarAreasDistribuicaoSegundoGrauCriminalJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
				case "2":
					return "";
				default:
					return processoNe.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, Id_Comarca, PosicaoPaginaAtual, turmaJulgadora);
				}
			default:
				return processoNe.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, Id_Comarca, UsuarioSessao.getUsuarioDt().getId_Serventia(), processoCadastroDt.isComCusta(), PosicaoPaginaAtual);
			}
		default:
			return processoNe.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, Id_Comarca, UsuarioSessao.getUsuarioDt().getId_Serventia(), processoCadastroDt.isComCusta(), PosicaoPaginaAtual);
		}
	}

}
