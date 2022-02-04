package br.gov.go.tj.projudi.ct.publicos;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ParametroRelatorioInterrupcaoDt;
import br.gov.go.tj.projudi.dt.RelatorioInterrupcao;
import br.gov.go.tj.projudi.ne.RelatorioInterrupcoesNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaPingdom;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

/**
 * Servlet implementation class RelatorioInterrupcoesCt
 * Classe:     RelatorioInterrupcoesCt.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: Realizar a lógica de acordo com os estados (passoBusca) das páginas htmls
 */ 
public class RelatorioInterrupcoesCt extends Controle {

	private static final long serialVersionUID = -8288588742679240212L;
	
	protected final String idParamRelIndisponibilidadeDt = "ParamRelIndisponibilidadeDt";
	protected final String idRelatorioPDF = "RelatorioPDF";
	protected final String codigoRashRelatorioPDF = "EmissaoPDFRelatorioInterrupcoesCt";
	protected final String nomeServlet = "RelatorioInterrupcoes";
	
	/**
     * Responsável em executar os processamentos de acordo com o estado da tela
     *      
     * @param HttpServletRequest request
     * @param HttpServletResponse response
     *  
     */
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {

		int passoBusca = 0;
		String posicaoPagina = "";
		String PosicaoPaginaAtual = "";
		paginaatual = Configuracao.Editar;
		byte[] byTemp = null;			
			
		request.getSession().setAttribute("CodigoCaptcha", "1");		
		
		if (request.getParameter("PaginaAtual") != null) paginaatual = Funcoes.StringToInt(request.getParameter("PaginaAtual"));
		if (request.getParameter("PosicaoPaginaAtual") == null) PosicaoPaginaAtual = "0";
		else PosicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual");

		//Pega valor digitado na caixa de paginação
		if (request.getParameter("PosicaoPagina") == null) posicaoPagina = PosicaoPaginaAtual;
		else posicaoPagina = String.valueOf((Funcoes.StringToInt(request.getParameter("PosicaoPagina"))) - 1);
				
		ParametroRelatorioInterrupcaoDt paramRelInterrupcoesDt = (ParametroRelatorioInterrupcaoDt) request.getSession().getAttribute(idParamRelIndisponibilidadeDt);
		if (paramRelInterrupcoesDt == null) paramRelInterrupcoesDt = new ParametroRelatorioInterrupcaoDt();		
				
		//Obtem os intervalos de data...			
		if (request.getParameter("DataInicial") != null && !request.getParameter("DataInicial").equals("")) {
			paramRelInterrupcoesDt.setPeriodoInicialUtilizado(request.getParameter("DataInicial"));
		}
		if (request.getParameter("DataFinal") != null && !request.getParameter("DataFinal").equals("")) {
			paramRelInterrupcoesDt.setPeriodoFinalUtilizado(request.getParameter("DataFinal"));
		}
		//Obtem o sistema...
		if (request.getParameter("Sistema") != null && !request.getParameter("Sistema").equals("") && !String.valueOf(request.getParameter("Sistema")).equalsIgnoreCase("null")) {
			EnumSistemaPingdom sistema = EnumSistemaPingdom.getEnum(Funcoes.StringToInt(request.getParameter("Sistema")));
			if (sistema != null) paramRelInterrupcoesDt.setSistema(sistema);
		}
		
		if (paramRelInterrupcoesDt.getSistema() != null)
			request.setAttribute("TituloMensagens", "Sistema " + paramRelInterrupcoesDt.getSistema().getValor());

		/**
		 * Variável PassoBusca utilizada para auxiliar na busca  
		 * passoBusca 1 : Significa que o usuário escolheu a opção emitir em PDF, após emitir o relatório em Video
		 * passoBusca 2 : Significa que está iniciando a busca, valida os parâmetros e deve exibir o captcha
		 * PassoBusca 3 : Valida Captcha e caso usuario tenha digitado corretamente, redireciona para tela de Relatório
		 * PassoBusca 4 : Significa que o usuário deseja limpar os parâmetros, selecionando o período inicial 
		 */
		if (request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));							

		String sPagina = "RelatorioInterrupcoesPeriodo";
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		request.setAttribute("PaginaAnterior", paginaatual);
		
		RelatorioInterrupcoesNe relInterrupcaoNe = new RelatorioInterrupcoesNe();							
		RelatorioInterrupcao relInterrupcaoDt = null;
		
		switch (passoBusca) {
		
			case Configuracao.Imprimir:	
				
				if (validaCodigoRashEmissaoPDF(request)){							
					relInterrupcaoDt = relInterrupcaoNe.obtenhaRelatorioInterrupcoes(paramRelInterrupcoesDt);						
					byTemp = relInterrupcaoNe.relListagemDeInterrupcoes(relInterrupcaoDt, ProjudiPropriedades.getInstance().getCaminhoAplicacao() , "");
					enviarPDF(response, byTemp, "RelatorioInterrupcao");			
					return;
				}
				else {
					//exibirCaptcha(request, nomeServlet, nomeServlet, nomeServlet);
					//sPagina = "Padroes/Captcha";
					if (gereRelatorio(request, paramRelInterrupcoesDt, relInterrupcaoNe)) {
						sPagina = "RelatorioAnaliticoInterrupcoes";	
					}
				}
				
				break;
				
			case Configuracao.Localizar: //Redireciona para tela de catpcha
				if (validaIntervaloDatas(paramRelInterrupcoesDt, request)) {
					//exibirCaptcha(request, nomeServlet, nomeServlet, nomeServlet);
					request.getSession().setAttribute("TipoConsulta", "Publica");
					//sPagina = "Padroes/Captcha";
					if (gereRelatorio(request, paramRelInterrupcoesDt, relInterrupcaoNe)) {
						sPagina = "RelatorioAnaliticoInterrupcoes";	
					}
				}
				break;
				
			case Configuracao.LocalizarDWR:				
				if (gereRelatorio(request, paramRelInterrupcoesDt, relInterrupcaoNe)) {
					sPagina = "RelatorioAnaliticoInterrupcoes";	
				}
				break;	
				
			default:				
				if (request.getSession(false) != null) request.getSession(false).invalidate();
				break;			
			
		}		

		request.setAttribute("PassoBusca", passoBusca);
		request.setAttribute("PosicaoPagina", (Funcoes.StringToInt(posicaoPagina) + 1));
		request.getSession().setAttribute(idParamRelIndisponibilidadeDt, paramRelInterrupcoesDt);
		request.setAttribute("Sistema",  paramRelInterrupcoesDt.getSistema().getId());
		
		super.redirecione(super.obtenhaAcaoJSP(sPagina), request, response);
		
	}

	private boolean gereRelatorio(HttpServletRequest request, ParametroRelatorioInterrupcaoDt paramRelInterrupcoesDt, RelatorioInterrupcoesNe relInterrupcaoNe) throws Exception {
		RelatorioInterrupcao relInterrupcaoDt;
		if (super.checkRecaptcha(request)) {																
			relInterrupcaoDt = relInterrupcaoNe.obtenhaRelatorioInterrupcoes(paramRelInterrupcoesDt);								
			this.armazeneCodigoRashEmissaoPDF(request);								
			request.setAttribute("RelIndisponibilidadeDt", relInterrupcaoDt);
			return true;
		} 
		return false;
	}	
	
	/**
     * Valida os intervalos de data informados na tela
     *      
     * @param ParametroRelatorioInterrupcaoDt paramRelIndisponibilidadeDt
     * @param HttpServletRequest request
     *  
     */
	protected boolean validaIntervaloDatas(ParametroRelatorioInterrupcaoDt paramRelIndisponibilidadeDt,
			                             HttpServletRequest request) throws Exception{				
		String mensagemRetorno = "";	
				
		if (paramRelIndisponibilidadeDt.getPeriodoInicialUtilizado().ehApos(paramRelIndisponibilidadeDt.getPeriodoFinalUtilizado())) {
			mensagemRetorno = "Data Inicial deve ser anterior ou igual a Data Final. ";
		}
		//else if (periodoFinalEhSuperiorADataDeOntem(paramRelIndisponibilidadeDt)){
		//	mensagemRetorno = String.format("Relatório disponível somente para dias com expedientes já finalizados.");
		//}
		else if (periodoFinalEhSuperiorADataDeHoje(paramRelIndisponibilidadeDt)){
			mensagemRetorno = "Data Final deve ser anterior ou igual a data de hoje. ";
		}
		
		super.exibaMensagemInconsistenciaErro(request, mensagemRetorno);	
		
		return (mensagemRetorno.equals(""));
	}
	
	/**
     * Valida se o período final informado na tela é superior à data de ontem
     *      
     * @param ParametroRelatorioInterrupcaoDt paramRelIndisponibilidadeDt     * 
     *  
     */
	protected boolean periodoFinalEhSuperiorADataDeOntem(ParametroRelatorioInterrupcaoDt paramRelIndisponibilidadeDt) throws Exception{
		
		TJDataHora dataOntem = new TJDataHora();
		dataOntem.adicioneDia(-1);
		dataOntem.atualizeUltimaHoraDia();		
		
		return (paramRelIndisponibilidadeDt.getPeriodoFinalUtilizado().ehApos(dataOntem));
	}
	
	/**
     * Valida se o período final informado na tela é superior à data de hoje
     *      
     * @param ParametroRelatorioInterrupcaoDt paramRelIndisponibilidadeDt     * 
     *  
     */
	protected boolean periodoFinalEhSuperiorADataDeHoje(ParametroRelatorioInterrupcaoDt paramRelIndisponibilidadeDt) throws Exception{
		
		TJDataHora dataHoje = new TJDataHora();
		dataHoje.atualizeUltimaHoraDia();		
		
		return (paramRelIndisponibilidadeDt.getPeriodoFinalUtilizado().ehApos(dataHoje));
	}
	
	/**
	 * Armazena o código Rash gerado para validação futura durante a emissão do PDF, evitando uso de robô
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected void armazeneCodigoRashEmissaoPDF(HttpServletRequest request) throws Exception{
		request.setAttribute(idRelatorioPDF, super.getCodigoHash(request, codigoRashRelatorioPDF));
	}
	
	/**
	 * Valida o código Rash gerado para emissão do PDF, evitando uso de robô
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected boolean validaCodigoRashEmissaoPDF(HttpServletRequest request) throws Exception{
		String codigoRash = (String) request.getParameter(idRelatorioPDF);
		
		if (codigoRash == null || codigoRash.trim().equalsIgnoreCase("")) return false;
		
		return super.verificarCodigoHash(request, codigoRashRelatorioPDF, codigoRash);
	}

	@Override
	public int Permissao() {
		return 870;
	}

	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
	protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
}
