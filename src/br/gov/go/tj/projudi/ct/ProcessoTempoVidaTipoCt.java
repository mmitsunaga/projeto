package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.ProcessoTempoVidaTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class ProcessoTempoVidaTipoCt extends Controle {

	private static final long serialVersionUID = 2838483345791588863L;

	public int Permissao() {
		return ProcessoTempoVidaTipoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoTempoVidaTipoDt processoTempoVidaTipoDt;
		ProcessoNe processoNe;

		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stAcao = "";
		byte[] byTemp = null;

		request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
		request.setAttribute("tempBuscaServentia", "Serventia");
		request.setAttribute("tempRetorno", "ProcessoTempoVidaTipo");
		if (request.getParameter("nomeBusca1") != null)
			stNomeBusca1 = request.getParameter("nomeBusca1");
		if (request.getParameter("nomeBusca2") != null)
			stNomeBusca2 = request.getParameter("nomeBusca2");
		if (request.getParameter("nomeBusca3") != null)
			stNomeBusca3 = request.getParameter("nomeBusca3");

		processoNe = (ProcessoNe) request.getSession().getAttribute("processoNe");
		if (processoNe == null)
			processoNe = new ProcessoNe();

		processoTempoVidaTipoDt = (ProcessoTempoVidaTipoDt) request.getSession().getAttribute("processoTempoVidaTipoDt");
		if (processoTempoVidaTipoDt == null)
			processoTempoVidaTipoDt = new ProcessoTempoVidaTipoDt();

		if (request.getParameter("Id_Serventia") != null) {
			processoTempoVidaTipoDt.setIdServentia(request.getParameter("Id_Serventia"));
			processoTempoVidaTipoDt.setServentia(request.getParameter("Serventia"));
		}
		if (request.getParameter("Id_ProcessoTipo") != null) {
			processoTempoVidaTipoDt.setIdProcessoTipo(request.getParameter("Id_ProcessoTipo"));
			processoTempoVidaTipoDt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		}
		if (request.getParameter("Periodo") != null) {
			if (request.getParameter("Periodo").equals("null")) {
				processoTempoVidaTipoDt.setPeriodo("");
			} else {
				processoTempoVidaTipoDt.setPeriodo(request.getParameter("Periodo"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempPrograma", "Liminar Deferida");
		request.setAttribute("tituloPagina", "Liminar Deferida");
		

		switch (paginaatual) {
		case Configuracao.Localizar:
			String stTemp = "", mensagemErro = "";
			
			if(!mensagemErro.equalsIgnoreCase("")){
				throw new MensagemException(mensagemErro); 
			}
			
			if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA))) {
				stTemp = processoNe.consultarProcessosTempoVidaPorTipoJSON(stNomeBusca1, stNomeBusca2, posicaoPaginaAtual);
			} else {
				if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.DESEMBARGADOR))) {
					stTemp = processoNe.consultarProcessosTempoVidaPorTipoDesemJSON(UsuarioSessao.getId_UsuarioServentia(), stNomeBusca1, posicaoPaginaAtual);
					processoTempoVidaTipoDt.setIdServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
					processoTempoVidaTipoDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
				}else{
					stTemp = processoNe.consultarProcessosTempoVidaPorTipoJSON(UsuarioSessao.getId_Serventia(), stNomeBusca1, posicaoPaginaAtual);
					processoTempoVidaTipoDt.setIdServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
					processoTempoVidaTipoDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());					
				}
				
				
			}

			enviarJSON(response, stTemp);
			return;

		case Configuracao.Novo:
			processoTempoVidaTipoDt.limpar();
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
			
		case Configuracao.Curinga7:
			
			
			if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA))) {
				request.setAttribute("estatistica", "sim");
			}			
			
			stAcao = "/WEB-INF/jsptjgo/RelatorioProcessosLiminarDeferida.jsp";
			
		break;
		
		case Configuracao.Imprimir:
			
			
			if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA))) {
				byTemp = processoNe.relLiminaresDeferidas(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), false, processoTempoVidaTipoDt.getIdServentia(), stNomeBusca2, UsuarioSessao.getUsuarioDt().getNome());				
			} else {
				if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.DESEMBARGADOR))) {
					byTemp = processoNe.relLiminaresDeferidas(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), true, UsuarioSessao.getId_UsuarioServentia(), stNomeBusca2, UsuarioSessao.getUsuarioDt().getNome());				
				}else{
					byTemp = processoNe.relLiminaresDeferidas(ProjudiPropriedades.getInstance().getCaminhoAplicacao(), false, UsuarioSessao.getId_Serventia(), stNomeBusca2, UsuarioSessao.getUsuarioDt().getNome());		
				}				
			}		
			
			enviarPDF(response, byTemp,"Relatorio");
			
		    byTemp = null;			
			
			break;
		case Configuracao.Curinga6:
			redireciona(response, "BuscaProcesso?Id_Processo=" + request.getParameter("Id_Processo").toString());
			break;

		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):			
			
			String passo = request.getParameter("Passo");
			if (passo == null) {
				String[] lisNomeBusca = { "Serventia" };
				String[] lisDescricao = { "Serventia", "Estado" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Serventia");
				request.setAttribute("tempBuscaDescricao", "Serventia");
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempRetorno", "ProcessoTempoVidaTipo");
				request.setAttribute("PaginaAtual", String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				
				if(request.getParameter("paginaPrincipal")!= null && request.getParameter("paginaPrincipal").toString().equals("7")){					
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga7);
				}else{
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				}
				
				
			} else {
				stTemp = "";
				stTemp = processoNe.consultarDescricaoServentiaJSON(stNomeBusca1, posicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;

		case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = { "ProcessoTipo" };
				String[] lisDescricao = { "ProcessoTipo" };
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_ProcessoTipo");
				request.setAttribute("tempBuscaDescricao", "ProcessoTipo");
				request.setAttribute("tempBuscaPrograma", "Tipo de Processo");
				request.setAttribute("tempRetorno", "ProcessoTempoVidaTipo");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				stTemp = "";
				stTemp = processoNe.consultarDescricaoProcessoTipoJSON(stNomeBusca1, posicaoPaginaAtual);
				enviarJSON(response, stTemp);
				return;
			}
			break;

		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		}
		if (stAcao.equals("")) {
			if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA))) {
				if (request.getParameter("Passo") == null) {
					String[][] lislocalizarBusca = new String[][] { { "*Serventia", String.valueOf(ServentiaDt.CodigoPermissao), "Serventia", processoTempoVidaTipoDt.getServentia(), processoTempoVidaTipoDt.getIdServentia()}, };

					request.setAttribute("lislocalizarBusca", lislocalizarBusca);
					String[][] lislocalizarDropdown = new String[][] { { "Tempo de Distribuição", "Até 20 dias", "19", "Mais de 20 dias", "20", "Mais de 30 dias", "30", "Mais de 40 dias", "40", "Mais de 50 dias", "50", "Mais de 60 dias", "60", "Mais de 70 dias", "70", "Mais de 80 dias", "80", "Mais de 90 dias", "90", "Mais de 100 dias", "100", "Mais de 110 dias", "110", "Mais de 120 dias", "120", "Mais de 130 dias", "130", "Mais de 140 dias", "140", "Mais de 150 dias", "150", "Mais de 180 dias", "180", "Mais de 240 dias", "240", "Mais de 360 dias", "360" } };
					request.setAttribute("lislocalizarDropdown", lislocalizarDropdown);
					String[] lisDescricao = { "Nº Processo", "Aceite", "Serventia", "Gabinete", "Nome do Desembargador", "Dias"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/LocalizarMultiplo.jsp";
					request.setAttribute("tempBuscaId", "Id_Processo");
					request.setAttribute("tempBuscaDescricao", "Processo");
					request.setAttribute("tempBuscaPrograma", "Processos não julgados com Liminar Deferida");
					request.setAttribute("tempRetorno", "ProcessoTempoVidaTipo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisDescricao", lisDescricao);
				}
				
			} else {
				if (request.getParameter("Passo") == null) {
					String[][] lislocalizarBusca = new String[0][0];
					request.setAttribute("lislocalizarBusca", lislocalizarBusca);
					String[][] lislocalizarDropdown = new String[][] { { "Tempo de Distribuição", "Até 20 dias", "1", "Mais de 20 dias", "20", "Mais de 30 dias", "30", "Mais de 40 dias", "40", "Mais de 50 dias", "50", "Mais de 60 dias", "60", "Mais de 70 dias", "70", "Mais de 80 dias", "80", "Mais de 90 dias", "90", "Mais de 100 dias", "100", "Mais de 110 dias", "110", "Mais de 120 dias", "120", "Mais de 130 dias", "130", "Mais de 140 dias", "140", "Mais de 150 dias", "150", "Mais de 180 dias", "180", "Mais de 240 dias", "240", "Mais de 360 dias", "360" } };
					request.setAttribute("lislocalizarDropdown", lislocalizarDropdown);
					String[] lisDescricao = { "Nº Processo", "Aceite", "Serventia", "Gabinete", "Nome do Desembargador", "Dias"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/LocalizarMultiplo.jsp";
					request.setAttribute("tempBuscaId", "Id_Processo");
					request.setAttribute("tempBuscaDescricao", "Processo");
					request.setAttribute("tempBuscaPrograma", "Processos não julgados com Liminar Deferida");
					request.setAttribute("tempRetorno", "ProcessoTempoVidaTipo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisDescricao", lisDescricao);
				}
			}
		}
		
		request.getSession().setAttribute("processoTempoVidaTipoDt", processoTempoVidaTipoDt);
		request.getSession().setAttribute("processoNe", processoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}