package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoPrimeiroGrauDt;
import br.gov.go.tj.projudi.ne.RelatorioEstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioProcessoPrimeiroGrauJuizesCt extends Controle {

	private static final long serialVersionUID = -2068882717141755533L;

	public int Permissao() {
		return RelatorioProcessoPrimeiroGrauDt.CodigoPermissaoProcessoPrimeiroGrauJuizes;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioProcessoPrimeiroGrauDt relatorioProcessoPrimeiroGrauDt;
		RelatorioEstatisticaNe relatorioEstatisticaNe;

		List tempList = null;
		byte[] byTemp = null;
		String mensagemRetorno = "";
		String stAcao = "";

		request.setAttribute("tempRetorno", "RelatorioProcessoPrimeiroGrauJuizes");
		request.setAttribute("tempBuscaSistema", "Sistema");
		request.setAttribute("tempPrograma", "Relatório de Distribuição de Processos 1º Grau por Juízes da Serventia");

		relatorioEstatisticaNe = (RelatorioEstatisticaNe) request.getSession().getAttribute("relatorioEstatisticaNe");
		if (relatorioEstatisticaNe == null)
			relatorioEstatisticaNe = new RelatorioEstatisticaNe();

		relatorioProcessoPrimeiroGrauDt = (RelatorioProcessoPrimeiroGrauDt) request.getSession().getAttribute("relatorioProcessoPrimeiroGrauDt");
		if (relatorioProcessoPrimeiroGrauDt == null)
			relatorioProcessoPrimeiroGrauDt = new RelatorioProcessoPrimeiroGrauDt();

		if (request.getParameter("AnoConsulta") != null && !request.getParameter("AnoConsulta").equals("")) {
			relatorioProcessoPrimeiroGrauDt.setAnoConsulta(request.getParameter("AnoConsulta"));
		}

		if (request.getParameter("Id_Serventia") != null) {
			if (request.getParameter("Id_Serventia").equals("null")) {
				relatorioProcessoPrimeiroGrauDt.setIdServentiaConsulta("");
			} else {
				relatorioProcessoPrimeiroGrauDt.setIdServentiaConsulta(request.getParameter("Id_Serventia"));
			}
		}

		if (request.getParameter("Serventia") != null) {
			if (request.getParameter("Serventia").equals("null")) {
				relatorioProcessoPrimeiroGrauDt.setServentiaConsulta("");
			} else {
				relatorioProcessoPrimeiroGrauDt.setServentiaConsulta(request.getParameter("Serventia"));
			}
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("Curinga", "vazio");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
		case Configuracao.Imprimir:
			mensagemRetorno = "";
			if (relatorioProcessoPrimeiroGrauDt.getAnoConsulta() == null || relatorioProcessoPrimeiroGrauDt.getAnoConsulta().equals("")) {
				mensagemRetorno = "O campo Ano de Consulta é obrigatório.";
			}
			if (relatorioProcessoPrimeiroGrauDt.getIdServentiaConsulta() == null || relatorioProcessoPrimeiroGrauDt.getIdServentiaConsulta().equals("")) {
				mensagemRetorno = "O campo Serventia é obrigatório.";
			}
			if(relatorioProcessoPrimeiroGrauDt.getIdServentiaConsulta() != null || !relatorioProcessoPrimeiroGrauDt.getIdServentiaConsulta().equals("")){
				int idServentiaSubTipoCodigo = Funcoes.StringToInt(relatorioEstatisticaNe.consultarServentiaSubTipoCodigo(relatorioProcessoPrimeiroGrauDt.getIdServentiaConsulta()),-1);
				if(idServentiaSubTipoCodigo != ServentiaSubtipoDt.VARAS_CIVEL && 
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL && 
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL && 
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA && 
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL && 
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR && 
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.FAMILIA_CAPITAL &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.UPJ_FAMILIA &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.UPJ_CRIMINAL &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.FAMILIA_INTERIOR &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.UPJ_SUCESSOES &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.UPJ_CUSTODIA &&
						idServentiaSubTipoCodigo != ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL) {
					//se não for nenhum dos tipos citados acima, deve retornar mensagem de erro.
					mensagemRetorno = "Esse relatório é destinado a serventias de 1º Grau: Juizados, Varas, Família e Fazenda.";
				}
			}
			
			if (mensagemRetorno.equals("")) {
				byTemp = relatorioEstatisticaNe.imprimirRelatorioProcessoPrimeiroGrauJuizes(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioProcessoPrimeiroGrauDt.getAnoConsulta(), relatorioProcessoPrimeiroGrauDt.getIdServentiaConsulta(), relatorioProcessoPrimeiroGrauDt.getServentiaConsulta(), UsuarioSessao.getUsuarioDt().getNome());
				String nome="RelatorioDistribuicao1GrauJuizes";				
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
			relatorioProcessoPrimeiroGrauDt = this.atribuirDataAtual(relatorioProcessoPrimeiroGrauDt);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}
			break;
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
			request.setAttribute("tempBuscaServentia", "Serventia");
			tempList = relatorioEstatisticaNe.consultarServentiaDescricao(tempNomeBusca, posicaoPaginaAtual);
			stAcao = "/WEB-INF/jsptjgo/ServentiaLocalizar.jsp";
			if (tempList.size() > 0) {
				request.setAttribute("ListaServentia", tempList);
				request.setAttribute("PaginaAtual", paginaatual);
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
				request.setAttribute("QuantidadePaginas", relatorioEstatisticaNe.getQuantidadePaginas());
				request.setAttribute("Curinga", "ServentiaVara");
			} else {
				request.setAttribute("MensagemErro", "Dados Não Localizados");
				request.setAttribute("PaginaAtual", Configuracao.Editar);
			}
			break;
		case Configuracao.Curinga6:
			// Curinga de acesso comum
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			relatorioProcessoPrimeiroGrauDt.limpar();
			if (relatorioProcessoPrimeiroGrauDt.getAnoConsulta().equals("")) {
				relatorioProcessoPrimeiroGrauDt = this.atribuirDataAtual(relatorioProcessoPrimeiroGrauDt);
			}
			relatorioProcessoPrimeiroGrauDt.setIdServentiaConsulta(UsuarioSessao.getUsuarioDt().getId_Serventia());
			relatorioProcessoPrimeiroGrauDt.setServentiaConsulta(UsuarioSessao.getUsuarioDt().getServentia());
			stAcao = "WEB-INF/jsptjgo/RelatorioProcessoPrimeiroGrauJuizes.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("acessoEspecial", false);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		case Configuracao.Curinga7:
			// Curinga para acesso especial
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			relatorioProcessoPrimeiroGrauDt.limpar();
			if (relatorioProcessoPrimeiroGrauDt.getAnoConsulta().equals("")) {
				relatorioProcessoPrimeiroGrauDt = this.atribuirDataAtual(relatorioProcessoPrimeiroGrauDt);
			}
			stAcao = "WEB-INF/jsptjgo/RelatorioProcessoPrimeiroGrauJuizesCorregedoria.jsp";
			request.getSession().setAttribute("stAcaoRetorno", stAcao);
			request.getSession().setAttribute("acessoEspecial", true);
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			break;
		default:
			request.setAttribute("PaginaAtual", Configuracao.Editar);
			if (relatorioProcessoPrimeiroGrauDt.getAnoConsulta().equals("")) {
				relatorioProcessoPrimeiroGrauDt = this.atribuirDataAtual(relatorioProcessoPrimeiroGrauDt);
			}
			if (request.getSession().getAttribute("stAcaoRetorno") != null && !request.getSession().getAttribute("stAcaoRetorno").equals("")) {
				stAcao = request.getSession().getAttribute("stAcaoRetorno").toString();
			}
			break;
		}

		request.getSession().setAttribute("relatorioProcessoPrimeiroGrauDt", relatorioProcessoPrimeiroGrauDt);
		request.getSession().setAttribute("relatorioEstatisticaNe", relatorioEstatisticaNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que atribui o ano atual ao relatório ao DT.
	 * 
	 * @param relatorioDt
	 * @return DT com data atualizada
	 * @author hmgodinho
	 */
	protected RelatorioProcessoPrimeiroGrauDt atribuirDataAtual(RelatorioProcessoPrimeiroGrauDt relatorioDt) {
		Calendar dataAtual = Calendar.getInstance();
		relatorioDt.setAnoConsulta(String.valueOf(dataAtual.get(Calendar.YEAR)));
		return relatorioDt;
	}
}
