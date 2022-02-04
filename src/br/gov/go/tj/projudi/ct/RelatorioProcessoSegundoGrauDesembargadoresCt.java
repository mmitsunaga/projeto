package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoSegundoGrauDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;

public class RelatorioProcessoSegundoGrauDesembargadoresCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioProcessoSegundoGrauDt.CodigoPermissaoProcessoSegundoGrauDesembargadores;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioProcessoSegundoGrauDt relatorioProcessoSegundoGrauDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		List tempList = null;
		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioProcessoSegundoGrauDesembargadores");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório de Distribuição de Processos Turma/2º Grau por Juízes/Desembargadores da Serventia");

		relatorioProcessoSegundoGrauDt = (RelatorioProcessoSegundoGrauDt) request.getSession().getAttribute("relatorioProcessoSegundoGrauDt");
		if (relatorioProcessoSegundoGrauDt == null)
			relatorioProcessoSegundoGrauDt = new RelatorioProcessoSegundoGrauDt();

		if (request.getParameter("AnoConsulta") != null && !request.getParameter("AnoConsulta").equals("")) {
			relatorioProcessoSegundoGrauDt.setAnoConsulta(request.getParameter("AnoConsulta"));
		}

		if (request.getParameter("Id_Serventia") != null) {
			if (request.getParameter("Id_Serventia").equals("null")) {
				relatorioProcessoSegundoGrauDt.setIdServentiaConsulta("");
			} else {
				relatorioProcessoSegundoGrauDt.setIdServentiaConsulta(request.getParameter("Id_Serventia"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioProcessoSegundoGrauDt.setServentiaConsulta("");
			} else {
				relatorioProcessoSegundoGrauDt.setServentiaConsulta(request.getParameter("Serventia"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
		case Configuracao.Imprimir:

			mensagemRetorno = "";
			
			if (relatorioProcessoSegundoGrauDt.getAnoConsulta() == null || relatorioProcessoSegundoGrauDt.getAnoConsulta().equals("")) {
				mensagemRetorno = "O campo Ano de Consulta é obrigatório.";
			}
			
			if (relatorioProcessoSegundoGrauDt.getIdServentiaConsulta() == null || relatorioProcessoSegundoGrauDt.getIdServentiaConsulta().equals("")) {
				mensagemRetorno = "O campo Serventia é obrigatório.";
			}

			//Atualizando a serventia selecionada na tela.
			
			relatorioProcessoSegundoGrauDt.setServentiaConsulta(relatorioEstatisticaNe.consultarServentiaId(relatorioProcessoSegundoGrauDt.getIdServentiaConsulta()).getServentia());
			
			if (mensagemRetorno.equals("")) {
				byTemp = relatorioEstatisticaNe.imprimirRelatorioProcessoDistribuidosMagistradoServentia(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioProcessoSegundoGrauDt.getAnoConsulta(), relatorioProcessoSegundoGrauDt.getIdServentiaConsulta(), relatorioProcessoSegundoGrauDt.getServentiaConsulta(), UsuarioSessao.getUsuarioDt().getNome());
				String nome="RelatorioDistribuicao2Grau";								
				enviarPDF(response, byTemp, nome);
				return;

			} else {
				if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
					stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
				}
				request.setAttribute("MensagemErro", mensagemRetorno);
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			break;
		case Configuracao.Novo:
			relatorioProcessoSegundoGrauDt.limparCamposConsulta();
			relatorioProcessoSegundoGrauDt = this.atribuirDataAtual(relatorioProcessoSegundoGrauDt);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}
			break;
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			
			if (request.getParameter("Passo")==null){

					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serventia");
					request.setAttribute("tempBuscaPrograma","RelatorioProcessoSegundoGrauDesembargadores");			
					request.setAttribute("tempRetorno","RelatorioProcessoSegundoGrauDesembargadores");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);					
					break;
				
			} else {
				String stTemp="";
				stTemp = relatorioEstatisticaNe.consultarServentiasSegundoGrauAtivasJSON(tempNomeBusca, posicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;								
			}
		
//			request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
//			request.setAttribute("tempBuscaServentia", "Serventia");
//			serventiaNe = new ServentiaNe();
//			tempList = serventiaNe.consultarServentiasSegundoGrauAtivas(tempNomeBusca, posicaoPaginaAtual);
//			stAcao = "/WEB-INF/jsptjgo/ServentiaSegundoGrauAtivaLocalizar.jsp";
//			if (tempList.size() > 0) {
//				request.setAttribute("ListaServentia", tempList);
//				request.setAttribute("PaginaAtual", paginaatual);
//				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
//				request.setAttribute("QuantidadePaginas", relatorioEstatisticaNe.getQuantidadePaginas());
//				request.setAttribute("Curinga", "ServentiaVara");
//			} else {
//				request.setAttribute("MensagemErro", "Dados Não Localizados");
//				request.setAttribute("PaginaAtual", Configuracao.Editar);
//			}
//			break;
		case Configuracao.Curinga6:
			// Curinga de acesso comum
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			relatorioProcessoSegundoGrauDt.limpar();
			if (relatorioProcessoSegundoGrauDt.getAnoConsulta().equals("")) {
				relatorioProcessoSegundoGrauDt = this.atribuirDataAtual(relatorioProcessoSegundoGrauDt);
			}
			
			//Na tela de acesso comum, o usuário poderá selecionar uma das serventias que estão relacionadas a sua.
			if(UsuarioSessao.getUsuarioDt().getId_ServentiaTipo().equals(String.valueOf(ServentiaTipoDt.GABINETE))) {
				//Se o usuário for de um gabinete, será necessário consultar as serventias relacionadas para disponibilizar
				//a seleção de uma delas na tela.
				
				List listaServentias = new ArrayList();
				ServentiaDt serventiaUsuarioSessao = new ServentiaDt();
				
				serventiaUsuarioSessao.setId(UsuarioSessao.getUsuarioDt().getId_Serventia());
				serventiaUsuarioSessao.setServentia(UsuarioSessao.getUsuarioDt().getServentia());

				listaServentias = relatorioEstatisticaNe.consultarServentiasPrincipaisRelacionadas(serventiaUsuarioSessao.getId());
				listaServentias.add(serventiaUsuarioSessao);
				
				relatorioProcessoSegundoGrauDt.setServentiaRelacionadas(listaServentias);
				
				
			} else {
				//Se o usuário não for de um gabinete, será apresentada apenas a serventia em que ele está logado. 
				ServentiaDt serventiaRelacionada = new ServentiaDt();
				serventiaRelacionada.setId(UsuarioSessao.getUsuarioDt().getId_Serventia());
				serventiaRelacionada.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
				List listaServentias = new ArrayList();
				listaServentias.add(serventiaRelacionada);
				relatorioProcessoSegundoGrauDt.setServentiaRelacionadas(listaServentias);
			}
			

			stAcao = "WEB-INF/jsptjgo/RelatorioProcessoSegundoGrauDesembargadores.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("acessoEspecial", false);
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			break;

		case Configuracao.Curinga7:
			// Curinga para acesso especial
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			relatorioProcessoSegundoGrauDt.limpar();
			if (relatorioProcessoSegundoGrauDt.getAnoConsulta().equals("")) {
				relatorioProcessoSegundoGrauDt = this.atribuirDataAtual(relatorioProcessoSegundoGrauDt);
			}

			stAcao = "WEB-INF/jsptjgo/RelatorioProcessoSegundoGrauDesembargadoresCorregedoria.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("acessoEspecial", true);
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			break;
		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (relatorioProcessoSegundoGrauDt.getAnoConsulta().equals("")) {
				relatorioProcessoSegundoGrauDt = this.atribuirDataAtual(relatorioProcessoSegundoGrauDt);
			}
			if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}
			break;
		}
		
		request.getSession().setAttribute("relatorioProcessoSegundoGrauDt", relatorioProcessoSegundoGrauDt);
		//request.getSession().setAttribute("relatorioEstatisticaNe", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui a data atual ao relatório ao DT.
	 * @param relatorioDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	public RelatorioProcessoSegundoGrauDt atribuirDataAtual(RelatorioProcessoSegundoGrauDt relatorioDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioDt.setAnoConsulta(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioDt;
	}
}
