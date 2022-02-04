package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioPostagemECartasDt;
import br.gov.go.tj.projudi.ne.LogNe;
import br.gov.go.tj.projudi.ne.RelatorioPostagemECartasNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.correios.EventoCorreios;
import br.gov.go.tj.projudi.ne.correios.IntegracaoCorreiosNe;
import br.gov.go.tj.projudi.ne.correios.ObjetoCorreios;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * Classe responsável em receber requisições da tela de Consulta
 * de informações de rastramento dos correios, na movimentação de
 * Intimação e Carta de Citação do E-carta
 * @author mmitsunaga
 *
 */
public class RastreamentoCorreiosCt extends Controle {

	private final int CODIGO_PERMISSAO = 936;
	
	private static final long serialVersionUID = 1701703020279682747L;

	@Override
	public int Permissao() {
		return CODIGO_PERMISSAO;
	}
	
	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaAtual, String nomebusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {
		
		String codigo = "";
		
		List<ObjetoCorreios> lista = null;
		
		IntegracaoCorreiosNe integracaoCorreiosNe = null;
		RelatorioPostagemECartasNe relatorioPostagemECartasNe = null;
		
		if (request.getParameter("codigo") != null){
			codigo = request.getParameter("codigo");
		}
		
		String stAcao = "/WEB-INF/jsptjgo/ConsultaRastreamentoCorreios.jsp";
		
		request.setAttribute("tempPrograma", "Consulta de Eventos de Rastreamento dos Correios");
		request.setAttribute("tempRetorno", "RastreamentoCorreios");		
		
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
						
		switch (paginaAtual){
								
			case Configuracao.Localizar:
				codigo = request.getParameter("codigo");
				integracaoCorreiosNe = IntegracaoCorreiosNe.get();
				lista = integracaoCorreiosNe.buscaTodosEventos(codigo);
				relatorioPostagemECartasNe = new RelatorioPostagemECartasNe();
				RelatorioPostagemECartasDt relatorioPostagemECartasDt = relatorioPostagemECartasNe.consultarDadosProcessoPorCodigoRastreamento(codigo);
				if (ValidacaoUtil.isNaoNulo(relatorioPostagemECartasDt)){
					request.setAttribute("processo", relatorioPostagemECartasDt);
				}
				request.setAttribute("resultado", getResponseHtml(codigo, lista));
				break;
		
			case Configuracao.Curinga9:
				codigo = request.getParameter("codigo");		
				integracaoCorreiosNe = IntegracaoCorreiosNe.get();
				lista = integracaoCorreiosNe.buscaTodosEventos(codigo);
				String responseHtml = getResponseHtml(codigo, lista);
				try {
					response.setContentType("text/html");
					response.setCharacterEncoding("iso-8859-1");
					response.getOutputStream().write(responseHtml.getBytes());
					response.flushBuffer();
				} catch (Exception e) {
					TenteGravarLogErro(Funcoes.obtenhaConteudoPrimeiraExcecao(e));
				}
				return;
				
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
		}
		
		request.setAttribute("PaginaAtual", paginaAtual);
		request.setAttribute("PosicaoPaginaAtual", posicaoPaginaAtual);
		request.setAttribute("Codigo", codigo);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
		
	}
	
	/**
	 * Retorna o html com as informações de rastreamento (AJAX)
	 * @param codigoRastreamento
	 * @param lista
	 * @return
	 */
	public String getResponseHtml(String codigoRastreamento, List<ObjetoCorreios> lista){
		StringBuilder html = new StringBuilder();		
		html.append("<div>");
		html.append(" <h4>" + codigoRastreamento + "</h4>");
		html.append(" <ul class=\"timeline\">");			
		for (ObjetoCorreios o : lista){
			if (ValidacaoUtil.isNaoVazio(o.getErro())){
				html.append("<li><span><b>" + o.getErro() + "</b></span></li>");
			} else {
				List<EventoCorreios> eventos = o.getEventos();
				if (eventos.size() > 0){
					for (EventoCorreios e: eventos){
						html.append("<li><span><b>" + e.getDataHoraLocal() + "</b></span><p>" + e.getDescricaoDetalhe() + "</p></li>");
					}
				}
			}			
		}		
		html.append(" </ul>");
		html.append("</div>");		
		return html.toString();
	}
	
	/**
	 * 
	 * @param Tabela
	 * @param Mensagem
	 * @throws Exception
	 */
	private void TenteGravarLogErro(String Mensagem) throws Exception	{	
		LogDt logDt = new LogDt("RastreamentoCorreios", "", UsuarioDt.SistemaProjudi, "Servidor", "", Mensagem, "");
		logDt.setLogTipoCodigo(String.valueOf(LogTipoDt.Erro));			
		new LogNe().salvar(logDt);

	}
	
}
