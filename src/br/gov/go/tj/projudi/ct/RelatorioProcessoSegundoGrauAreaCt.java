package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoSegundoGrauDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;

public class RelatorioProcessoSegundoGrauAreaCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioProcessoSegundoGrauDt.CodigoPermissaoProcessoSegundoGrauArea;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioProcessoSegundoGrauDt relatorioProcessoSegundoGrauDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		List tempList = null;
		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";
		String stNomeBusca1 = "";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempRetorno", "RelatorioProcessoSegundoGrauArea");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório de Processos Distribuídos por Área de Distribuição");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("relatorioEstatisticaNe");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioProcessoSegundoGrauDt = (RelatorioProcessoSegundoGrauDt) request.getSession().getAttribute("relatorioProcessoSegundoGrauDt");
		if (relatorioProcessoSegundoGrauDt == null)
			relatorioProcessoSegundoGrauDt = new RelatorioProcessoSegundoGrauDt();

		if (request.getParameter("AnoConsulta") != null && !request.getParameter("AnoConsulta").equals("")) {
			relatorioProcessoSegundoGrauDt.setAnoConsulta(request.getParameter("AnoConsulta"));
		}

		if (request.getParameter("Id_AreaDistribuicao") != null) {
			if (request.getParameter("Id_AreaDistribuicao").equals("null")) {
				relatorioProcessoSegundoGrauDt.setIdAreaDistribuicao("");
			} else {
				relatorioProcessoSegundoGrauDt.setIdAreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			}
		}

		if (request.getParameter("AreaDistribuicao") != null) {
			if (request.getParameter("AreaDistribuicao").equals("null")) {
				relatorioProcessoSegundoGrauDt.setAreaDistribuicao("");
			} else {
				relatorioProcessoSegundoGrauDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
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
			
			if (relatorioProcessoSegundoGrauDt.getIdAreaDistribuicao() == null || relatorioProcessoSegundoGrauDt.getIdAreaDistribuicao().equals("")) {
				mensagemRetorno = "O campo Área de Distribuição é obrigatório.";
			}

			if (mensagemRetorno.equals("")) {
				byTemp = relatorioEstatisticaNe.imprimirRelatorioProcessoSegundoGrauArea(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioProcessoSegundoGrauDt.getAnoConsulta(), relatorioProcessoSegundoGrauDt.getIdAreaDistribuicao(), relatorioProcessoSegundoGrauDt.getAreaDistribuicao(), UsuarioSessao.getUsuarioDt().getNome());				
				
				enviarPDF(response,byTemp,"RelatorioSumarioProcesso");				
				
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
		case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			request.setAttribute("tempBuscaId_AreaDistribuicao", "Id_AreaDistribuicao");
			request.setAttribute("tempBuscaAreaDistribuicao", "AreaDistribuicao");
			
			boolean acessoEspecial = false;
			if (request.getSession().getAttribute("acessoEspecial") != null) {
				acessoEspecial = new Boolean(request.getSession().getAttribute("acessoEspecial").toString());
			} 
			
			if(acessoEspecial){
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Área de Distribuição"};
					String[] lisDescricao = {"Área de Distribuição"};
					String[] camposHidden = {"ForumCodigo","Id_ServentiaSubTipo"};
					request.setAttribute("camposHidden",camposHidden);
					request.setAttribute("tempBuscaId", "Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao", "AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma", "Área de Distribuição");
					request.setAttribute("tempRetorno", "RelatorioProcessoSegundoGrauArea");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = relatorioEstatisticaNe.consultarDescricaoAreaDistribuicaoJSON(stNomeBusca1, posicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}			
				
				
				
				
//				AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
//				tempList = areaDistribuicaoNe.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
//				request.setAttribute("QuantidadePaginas", areaDistribuicaoNe.getQuantidadePaginas());
//				stAcao = "/WEB-INF/jsptjgo/AreaDistribuicaoLocalizar.jsp";
			} else {
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"AreaDistribuicao"};
					String[] lisDescricao = {"AreaDistribuicao"};
					String[] camposHidden = {"ForumCodigo","Id_ServentiaSubTipo"};
					request.setAttribute("camposHidden",camposHidden);
					request.setAttribute("tempBuscaId", "Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao", "AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma", "AreaDistribuicao");
					request.setAttribute("tempRetorno", "RelatorioProcessoSegundoGrauArea");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = relatorioEstatisticaNe.consultarDescricaoAreaDistribuicaoServentiaJSON(stNomeBusca1, UsuarioSessao.getUsuarioDt().getId_Serventia(), posicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				
//				ProcessoNe processoNe = new ProcessoNe();
//				tempList = processoNe.consultarAreasDistribuicaoServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
//				stAcao = "/WEB-INF/jsptjgo/AreaDistribuicaoServentiaLocalizar.jsp";
			}
			
//			if (tempList != null && tempList.size() > 0) {
//				request.setAttribute("ListaAreaDistribuicao", tempList);
//				request.setAttribute("PaginaAtual", Configuracao.Localizar);
//				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
//			} else {
//				request.setAttribute("MensagemErro", "Nenhuma Área de Distribuição localizada.");
//				request.setAttribute("PaginaAtual", Configuracao.Editar);
//			}
			break;
		case Configuracao.Curinga6:
			// Curinga de acesso comum
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			relatorioProcessoSegundoGrauDt.limpar();
			if (relatorioProcessoSegundoGrauDt.getAnoConsulta().equals("")) {
				relatorioProcessoSegundoGrauDt = this.atribuirDataAtual(relatorioProcessoSegundoGrauDt);
			}

			stAcao = "WEB-INF/jsptjgo/RelatorioProcessoSegundoGrauArea.jsp";
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

			stAcao = "WEB-INF/jsptjgo/RelatorioProcessoSegundoGrauAreaCorregedoria.jsp";
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
		request.getSession().setAttribute("relatorioEstatisticaNe", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui a data atual ao relatório ao DT.
	 * @param relatorioDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	protected RelatorioProcessoSegundoGrauDt atribuirDataAtual(RelatorioProcessoSegundoGrauDt relatorioDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioDt.setAnoConsulta(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioDt;
	}
}
