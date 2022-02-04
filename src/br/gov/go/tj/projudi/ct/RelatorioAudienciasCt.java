package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioAudienciasDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioAudienciasCt extends Controle {

	private static final long serialVersionUID = -5571146456136346179L;

	public int Permissao() {
		return RelatorioAudienciasDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioAudienciasDt relatorioAudienciasDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		String stNomeBusca1 = "";
		byte[] byTemp = null;
		String stAcao = "";
		String fluxo;
		String erroRetorno;
		boolean arquivoDevolvido = false;
		
		request.setAttribute("tempRetorno", "RelatorioSumarioAudiencias");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório Sumário de Audiências");

		
		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("RelatorioEstatisticane");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		
		relatorioAudienciasDt = (RelatorioAudienciasDt) request.getSession().getAttribute("RelatorioAudienciasdt");
		if (relatorioAudienciasDt == null)
			relatorioAudienciasDt = new RelatorioAudienciasDt();
		
		
		if (request.getParameter("dataInicial") != null && !request.getParameter("dataInicial").equals("")) {
			relatorioAudienciasDt.setDataInicial(request.getParameter("dataInicial"));
		}
		
		if (request.getParameter("dataFinal") != null && !request.getParameter("dataFinal").equals("")) {
			relatorioAudienciasDt.setDataFinal(request.getParameter("dataFinal"));
		}
		
		if (request.getParameter("Serventia") != null && !request.getParameter("Serventia").equals("")) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioAudienciasDt.setServentia("");
			} else {
				relatorioAudienciasDt.setServentia(request.getParameter("Serventia"));
			}
		}
		
		if (request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").equals("")) {
			if (request.getParameter("Id_Serventia").equals("null")) {
				relatorioAudienciasDt.setId_Serventia("");
			} else {
				relatorioAudienciasDt.setId_Serventia(request.getParameter("Id_Serventia"));
			}
		}

		if (request.getParameter("Comarca") != null && !request.getParameter("Comarca").equals("")) {
			if (request.getParameter("Comarca").equals("null")) {
				relatorioAudienciasDt.setComarca("");
			} else {
				relatorioAudienciasDt.setComarca(request.getParameter("Comarca"));
			}
		}

		if (request.getParameter("Id_Comarca") != null && !request.getParameter("Id_Comarca").equals("")) {
			if (request.getParameter("Id_Comarca").equals("null")) {
				relatorioAudienciasDt.setId_Comarca("");
			} else {
				relatorioAudienciasDt.setId_Comarca(request.getParameter("Id_Comarca"));
			}
		}
		
		if (request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").equals("")) {
			if (request.getParameter("Id_Serventia").equals("null")) {
				relatorioAudienciasDt.setId_Serventia("");
			} else {
				relatorioAudienciasDt.setId_Serventia(request.getParameter("Id_Serventia"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
		case Configuracao.Imprimir:

			fluxo = request.getParameter("fluxo");
			switch(Funcoes.StringToInt(fluxo)) {
				
				case RelatorioAudienciasDt.FLUXO_SUMARIO:
					erroRetorno = "";
					erroRetorno = this.imprimirRelatorioSumario(relatorioAudienciasDt, UsuarioSessao.getUsuarioDt().getNome(), request, response);
					arquivoDevolvido = true;
					if(!erroRetorno.isEmpty()){
						stAcao = "WEB-INF/jsptjgo/RelatorioSumarioAudienciasDia.jsp";
						request.setAttribute("MensagemErro", erroRetorno);
						request.setAttribute("PaginaAtual", Configuracao.Editar);
						arquivoDevolvido = false;
					}
				break;
				
				case RelatorioAudienciasDt.FLUXO_ANALITICO:
					erroRetorno = "";
					erroRetorno = this.imprimirRelatorioAnalitico(relatorioAudienciasDt, UsuarioSessao.getUsuarioDt().getNome(), request, response);
					arquivoDevolvido = true;
					if(!erroRetorno.isEmpty()){
						stAcao = "WEB-INF/jsptjgo/RelatorioAnaliticoAudiencias.jsp";
						request.setAttribute("MensagemErro", erroRetorno);
						request.setAttribute("PaginaAtual", Configuracao.Editar);
						arquivoDevolvido = false;
					}
				break;
				
			}
			
			
			
			break;
			
		case Configuracao.Novo:
			relatorioAudienciasDt.limparCamposConsulta();
			fluxo = request.getParameter("fluxo");
			switch(Funcoes.StringToInt(fluxo)) {
				case RelatorioAudienciasDt.FLUXO_SUMARIO:
					stAcao = "WEB-INF/jsptjgo/RelatorioSumarioAudienciasDia.jsp";
				break;
				case RelatorioAudienciasDt.FLUXO_ANALITICO:
					stAcao = "WEB-INF/jsptjgo/RelatorioAnaliticoAudiencias.jsp";
				break;
			}
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			
			break;
			
		case Configuracao.Curinga6:
			fluxo = request.getParameter("fluxo");
			
			if(fluxo == null || fluxo.isEmpty()) {
				// CARREGAR PÁGINA DE CONSULTA
				request.getSession().setAttribute("RelatorioAudienciasdt", relatorioAudienciasDt);
				stAcao = "WEB-INF/jsptjgo/RelatorioAnaliticoAudiencias.jsp";
				request.setAttribute("PaginaAtual", Configuracao.Curinga6);
			}
			else if(fluxo.equals("2")){
				// RETORNAR RELATÓRIO
//				stAcao = "WEB-INF/jsptjgo/RelatorioAnaliticoAudiencias.jsp";
//				request.setAttribute("PaginaAtual", Configuracao.Curinga6);
				byTemp = relatorioEstatisticaNe.relAnaliticoAudiencias(relatorioAudienciasDt, UsuarioSessao.getUsuarioDt().getNome());
				enviarPDF(response, byTemp, "RelatorioSumarioAudiencias");
			}
			break;
			
			//Consultar Serventias
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_Serventia");
				request.setAttribute("tempBuscaDescricao","Serventia");
				request.setAttribute("tempBuscaPrograma","Serventia");			
				request.setAttribute("tempRetorno","RelatorioAudiencias");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
				request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				relatorioAudienciasDt.setId_Serventia("null");
			} else {
				
				String stTemp="";
				stNomeBusca1 = request.getParameter("nomeBusca1");
				stTemp = relatorioEstatisticaNe.consultarServentiasComarcaJSON(stNomeBusca1, relatorioAudienciasDt.getId_Comarca(), posicaoPaginaAtual);
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
				request.setAttribute("tempRetorno","RelatorioAudiencias");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
				request.setAttribute("PaginaAtual", String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stNomeBusca1 = request.getParameter("nomeBusca1");
				stTemp = relatorioEstatisticaNe.consultarDescricaoComarcaJSON(stNomeBusca1, posicaoPaginaAtual);
							
					enviarJSON(response, stTemp);
					
					
				return;								
			}
			break;
		
		default:
			relatorioAudienciasDt.limparCamposConsulta();
			request.getSession().setAttribute("RelatorioAudienciasdt", relatorioAudienciasDt);
			stAcao = "WEB-INF/jsptjgo/RelatorioSumarioAudienciasDia.jsp";
			
			request.setAttribute("PaginaAtual", Configuracao.Editar);

			break;
		}
		
		
		request.getSession().setAttribute("RelatorioAudienciasdt", relatorioAudienciasDt);

		if(!arquivoDevolvido) {
			RequestDispatcher dis = request.getRequestDispatcher(stAcao);
			dis.include(request, response);
		}
	}
	
	
	public String imprimirRelatorioSumario(RelatorioAudienciasDt relatorioAudienciasDt, String nomeUsuario, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mensagemRetorno = "";
		byte[] byTemp = null;
		RelatorioEstatisticaNe relatorioEstatisticaNe = new RelatorioEstatisticaNe();
		if(relatorioAudienciasDt.getDataInicial() == null || relatorioAudienciasDt.getDataInicial().isEmpty()) {
			mensagemRetorno = "O campo \"Início\" é obrigatório. ";
		} else {
			
			if(relatorioAudienciasDt.getDataFinal() != null && !relatorioAudienciasDt.getDataFinal().isEmpty()) {
				
				int diferencaDias = Funcoes.calculaDiferencaEntreDatas(relatorioAudienciasDt.getDataInicial(), relatorioAudienciasDt.getDataFinal()) + 1;
				if (diferencaDias <= 0) {
					mensagemRetorno = "A data inicial não pode ser maior que a data final. ";
				} else if (diferencaDias >= 92) {
					mensagemRetorno = "O período entre a data inicial e a data final não pode exceder o total de 92 (noventa e dois) dias. ";
				}
			
			} else {
				relatorioAudienciasDt.setDataFinal( relatorioAudienciasDt.getDataInicial() );
			}
		}
		
		if (mensagemRetorno.equals("")) {
			
			byTemp = relatorioEstatisticaNe.relSumarioAudienciasComarcaDia(relatorioAudienciasDt, nomeUsuario);
			enviarPDF(response, byTemp, "RelatorioSumarioAudiencias");
			return mensagemRetorno;
		}
		
		return mensagemRetorno;
	}
	
	
	public String imprimirRelatorioAnalitico(RelatorioAudienciasDt relatorioAudienciasDt, String nomeUsuario, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String mensagemRetorno = "";
		byte[] byTemp = null;
		RelatorioEstatisticaNe relatorioEstatisticaNe = new RelatorioEstatisticaNe();
		
		if(relatorioAudienciasDt.getId_Serventia() == null || relatorioAudienciasDt.getId_Serventia().isEmpty() ){
			mensagemRetorno = "É obrigatório escolher a \"Serventia\". ";
		}
		else {
		
			if(relatorioAudienciasDt.getId_Comarca() == null || relatorioAudienciasDt.getId_Comarca().isEmpty() ){
				mensagemRetorno = "É obrigatório escolher a \"Comarca\". ";
			}
			else {
			
				if(relatorioAudienciasDt.getDataInicial() == null || relatorioAudienciasDt.getDataInicial().isEmpty()) {
					mensagemRetorno = "O campo \"Início\" é obrigatório. ";
				} else {
					
					if(relatorioAudienciasDt.getDataFinal() != null && !relatorioAudienciasDt.getDataFinal().isEmpty()) {
						
						int diferencaDias = Funcoes.calculaDiferencaEntreDatas(relatorioAudienciasDt.getDataInicial(), relatorioAudienciasDt.getDataFinal()) + 1;
						if (diferencaDias <= 0) {
							mensagemRetorno = "A data inicial não pode ser maior que a data final. ";
						} else if (diferencaDias > 31) {
							mensagemRetorno = "O período entre a data inicial e a data final não pode exceder o total de 31 (trinta e um) dias. ";
						}
					
					} else {
						relatorioAudienciasDt.setDataFinal( relatorioAudienciasDt.getDataInicial() );
					}
				}
			
			}
		}
		
		if (mensagemRetorno.equals("")) {
			
			byTemp = relatorioEstatisticaNe.relAnaliticoAudiencias(relatorioAudienciasDt, nomeUsuario);
			enviarPDF(response, byTemp, "RelatorioSumarioAudiencias");
			return mensagemRetorno;
		}
		
		return mensagemRetorno;
	}
	
}
