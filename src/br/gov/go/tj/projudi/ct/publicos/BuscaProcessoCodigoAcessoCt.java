package br.gov.go.tj.projudi.ct.publicos;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet para controlar a consulta de processos feita pelas partes baseado em c�digo de acesso fornecido
 * pelo sistema
 * 
 * @author msapaula
 * 30/04/2010 09:24:24
 */
public class BuscaProcessoCodigoAcessoCt extends Controle {

	private static final long serialVersionUID = 64713257283622314L;	
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception{

		String stId = "";
		String processoNumero = "";
		String codigoAcesso = "";
		ProcessoDt processoDt;
		String stMensagem = "";
		int passoBusca = 0;
		ProcessoNe Processone;		
		int paginaAtual = 4;
		int passoEditar = -1;

		response.setContentType("text/html");
		response.setCharacterEncoding("iso-8859-1");
		request.setAttribute("tempRetorno", "BuscaProcessoCodigoAcesso");
		request.setAttribute("tempPrograma", "Busca Processo por C�digo");

		String stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaCodigoAcesso.jsp";

		Processone = (ProcessoNe) request.getSession().getAttribute("BuscaProcessoneCodigoAcesso");
		if (Processone == null) Processone = new ProcessoNe();

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt == null) processoDt = new ProcessoDt();
		
		if (request.getParameter("PaginaAtual") != null) paginaAtual = Funcoes.StringToInt(request.getParameter("PaginaAtual"));

		if (request.getParameter("ProcessoNumero") == null) processoNumero = "";
		else processoNumero = request.getParameter("ProcessoNumero").trim();

		if (request.getParameter("CodigoAcesso") == null) {
			codigoAcesso = "";
			if (request.getSession().getAttribute("CodigoAcesso") != null) codigoAcesso = (String)(request.getSession().getAttribute("CodigoAcesso"));
		}
		else {
			codigoAcesso = request.getParameter("CodigoAcesso");
			request.getSession().setAttribute("CodigoAcesso",codigoAcesso);
		}

		/**
		 * Vari�vel PassoBusca utilizada para auxiliar na busca
		 * PassoBusca 0 : Redireciona para jsp de Dados do Processo 
		 * passoBusca 1 : Significa que est� iniciando a busca, redireciona ent�o para jsp de Pesquisa de Processos
		 * PassoBusca 2 : Significa que um processo foi selecionado e deve exibir o captcha
		 * PassoBusca 3 : Valida Captcha e caso usuario tenha digitado corretamente, redireciona para tela de Dados do Processo  
		 */
		if (request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));

		request.setAttribute("PaginaAnterior", paginaAtual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaAtual) {

			case Configuracao.Novo:
				UsuarioSessao = new UsuarioNe();
				UsuarioSessao.setUsuarioDt(new UsuarioDt());
				UsuarioSessao.getUsuarioDt().setGrupoCodigo(String.valueOf(GrupoDt.POPULACAO));
				if (request.getSession(false) != null) request.getSession(false).invalidate();
				request.getSession().setMaxInactiveInterval(ProjudiPropriedades.getInstance().getTempoExpiraSessao());
				request.getSession().setAttribute("UsuarioSessaoCodigoAcesso", UsuarioSessao);
				request.getSession().setAttribute("CodigoCaptcha", "1");
				processoNumero = "";
				codigoAcesso = "";
				stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaCodigoAcesso.jsp";
				break;

			//Consulta processo pelo c�digo de acesso informado 
			case Configuracao.Localizar:

				if (processoNumero == null || processoNumero.length() == 0) stMensagem += "N�mero do Processo � campo obrigat�rio. \n";
				else if (processoNumero.indexOf(".") < 0 && processoNumero.indexOf("-") < 0) stMensagem += "N�mero do Processo no formato incorreto. \n";

				if (codigoAcesso == null || codigoAcesso.length() == 0) stMensagem += "C�digo de Acesso � campo obrigat�rio. \n";

				if (stMensagem.length() == 0) {

					if (checkRecaptcha(request)) {
						//Verifica o c�digo passado pelo usu�rio, se for v�lido retorna o Id do Processo
						stId = Processone.verificarCodigoAcessoProcesso(codigoAcesso, processoNumero, Controle.getIpCliente(request), true);
	
						if (stId != null && !stId.isEmpty()) {
								//o id_processo ser� utilizado para liberar o acesso especifico ao um unico processo
								UsuarioSessao.setId_ProcessoCodigoAcesso(stId);
								//permiss�es para poder gerar o pdf completo
								UsuarioSessao.setPermissao(ProcessoDt.CodigoPermissaoPDF_COMPLETO);
								UsuarioSessao.setPermissao(2861);
								
								request.getSession().setAttribute("AcessoPermitido", stId);
	
								if (!stId.equalsIgnoreCase("")) {
									processoDt = Processone.consultarDadosProcessoAcessoExterno(stId, UsuarioSessao.getUsuarioDt(), true, true, UsuarioSessao.getNivelAcesso());
									request.getSession().setAttribute("processoDt", processoDt);
	
									if (processoDt.getId_Recurso().length() > 0) {
										// Quando se tratar de recurso inominado os dados do recurso ser�o mostrados
										RecursoDt recursoDt = Processone.consultarDadosRecurso(processoDt.getId_Recurso(), true);
										processoDt.setRecursoDt(recursoDt);
									}
									
									//Define c�digo hash para acesso ao link "e outros"
									armazeneCodigoHashEOutros(request,UsuarioSessao,stId);
								}
								stAcao = "/WEB-INF/jsptjgo/DadosProcessoCodigoAcesso.jsp";							
						} else {
							request.setAttribute("MensagemErro", "C�digo Inv�lido. \n N�o foi poss�vel obter Processo de acordo com os dados informados.");							
						}
					}
				} else request.setAttribute("MensagemErro", stMensagem);
				
				break;

			// Efetua download de arquivos
			case Configuracao.Curinga6:
				String id_MovimentacaoArquivo = request.getParameter("Id_MovimentacaoArquivo");
				String acessoPermitido =  (String) request.getSession().getAttribute("AcessoPermitido");
				String id_proc = request.getParameter("id_proc");
				
				//if (acessoPermitido == null || id_proc==null || !id_proc.equals(acessoPermitido)){
				if(id_proc!=null){
					if (acessoPermitido == null && !id_proc.equals(acessoPermitido)){
						request.setAttribute("MensagemErro", "Sem permis�o para baixar o arquivo!");
						break;
					}
				}
				
				if (UsuarioSessao.VerificarCodigoHash(id_MovimentacaoArquivo+processoDt.getId_Processo(), request.getParameter("hash"))) {
					boolean recibo = false;
					if (request.getParameter("recibo") != null && request.getParameter("recibo").equals("true")) recibo = true;

					//Informa��es do usu�rio e IP para gerar log do download
					String id_Usuario = UsuarioDt.SistemaProjudi;
					String ipComputador = request.getRemoteAddr();															

					if (request.getParameter("CodigoVerificacao") != null && request.getParameter("CodigoVerificacao").equals("true")){
						String stIdArquivo = Processone.consultarIdArquivo(id_MovimentacaoArquivo); 
						if (stIdArquivo != null && stIdArquivo.length() > 0) {
							if (!stIdArquivo.equalsIgnoreCase("")) {
																
								// gerar pdf como arquivos da publica��o
								byte[] arquivoPDF = Processone.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(),stIdArquivo, id_MovimentacaoArquivo, processoDt,  UsuarioSessao.getUsuarioDt(), new LogDt(id_Usuario, ipComputador));
								enviarPDF(response, arquivoPDF, "publicacao");								
							
								return;
								//**********************************************************************************************								

							}
						} 
						else {
							request.setAttribute("Mensagem", "Erro ao efetuar consulta do arquivo.");
							RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
							dis.include(request, response);
							return;
						}
					} else {
						if (!Processone.baixarArquivoMovimentacao(id_MovimentacaoArquivo, processoDt, UsuarioSessao.getUsuarioDt(), recibo, response, new LogDt(id_Usuario, ipComputador), true)){
							//se baixou mandar para a jsp de n�o permitido baixar o arquivo
							stAcao = "/WEB-INF/jsptjgo/Padroes/ArquivoNaoPermitido.jsp";
							RequestDispatcher dis = request.getRequestDispatcher(stAcao);
							dis.include(request, response);
						}
					}
					
					//retirando permiss�o para consultar processo que esteja em segredo de justi�a por c�digo de acesso
					//Motivo: se n�o retirar a permiss�o, o usu�rio poderia consultar na consulta p�blica sem o c�digo de acesso.
										
				} else exibaMensagemInconsistenciaErro(request, "Acesso negado.");
				return;

				//Visualizar Situa��o do Processo
			case Configuracao.Imprimir:
				this.consultarSituacaoProcesso(request, processoDt, Processone);
				stAcao = "/WEB-INF/jsptjgo/ProcessoSituacaoConsultaPublica.jsp";
				break;

			//Consultar Respons�veis pelo Processo
			case Configuracao.LocalizarAutoPai:
				this.consultarResponsaveisProcesso(request, processoDt, UsuarioSessao, Processone);
				stAcao = "/WEB-INF/jsptjgo/ProcessoResponsaveis.jsp";
				break;

			default:
				
				// Fun��o Editar � dividida da seguinte forma: 
				// Passo 11: Redireciona para jsp de consulta todas as partes
				
				switch (passoEditar) {
					case 11:
						if (validaCodigoHashEOutros(request,UsuarioSessao, processoDt.getId_Processo())){
							request.setAttribute("tempRetorno", "BuscaProcessoCodigoAcesso?ProcessoNumero=" + processoDt.getProcessoNumero() + "&PaginaAtual=2");												
							stAcao = "/WEB-INF/jsptjgo/BuscaTodasPartesProcesso.jsp";
						}else{
							exibaMensagemInconsistenciaErro(request, "Acesso negado. N�o foi poss�vel exibir os dados das Partes.");
						}
						break;
						
					default:
						request.setAttribute("PaginaAtual", Configuracao.Editar);
						stId = request.getParameter("Id_Processo");
						
						if (validaCodigoHashEOutros(request,UsuarioSessao, stId)){	
							stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
							processoDt = Processone.consultarDadosProcessoAcessoExterno(stId, UsuarioSessao.getUsuarioDt(), true, true, UsuarioSessao.getNivelAcesso());
							if(processoDt.isSegredoJustica() || processoDt.isSigiloso()){
								if(!Processone.podeAcessarProcesso(UsuarioSessao.getUsuarioDt(), processoDt, null)) {
									stAcao = "/WEB-INF/jsptjgo/DadosProcessoSegredoJustica.jsp";
								}
							}
						}else if (checkRecaptcha(request)) {
							//Define c�digo rash para acesso ao link "e outros"
							armazeneCodigoHashEOutros(request,UsuarioSessao,stId);
							
							if (stId != null && !stId.isEmpty()) {
								if (checkRecaptcha(request)) {
									if (!stId.equalsIgnoreCase("")) {
										processoDt = Processone.consultarDadosProcessoAcessoExterno(stId, UsuarioSessao.getUsuarioDt(), true, true, UsuarioSessao.getNivelAcesso());
										request.getSession().setAttribute("processoDt", processoDt);
										
										if (processoDt.getId_Recurso().length() > 0) {
											// Quando se tratar de recurso inominado os dados do recurso ser�o mostrados
											RecursoDt recursoDt = Processone.consultarDadosRecurso(processoDt.getId_Recurso(), true);
											processoDt.setRecursoDt(recursoDt);
										}
									}
									stAcao = "/WEB-INF/jsptjgo/DadosProcessoCodigoAcesso.jsp";
								}
							} else stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaPublica.jsp";				
						}
						break;
				}
		}

		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("PassoBusca", passoBusca);
		request.setAttribute("ProcessoNumero", processoNumero);
		request.setAttribute("CodigoAcesso", codigoAcesso);
		request.getSession().setAttribute("BuscaProcessoneCodigoAcesso", Processone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);

		
	}

	protected void exibaMensagemInconsistenciaErro(HttpServletRequest request, String mensagem){
    	request.setAttribute("MensagemErro",  mensagem);
    }


	/**
	 * M�todo respons�vel em consultar a situa��o completa do processo.
	 * Retorna dados referentes a pend�ncias, conclus�es e audi�ncias em aberto no processo.
	 * 
	 * @param request
	 * @param processoDt
	 * @param processoNe
	 * @throws Exception
	 */
	protected void consultarSituacaoProcesso(HttpServletRequest request, ProcessoDt processoDt, ProcessoNe processoNe) throws Exception{
		//Consultar pend�ncias em aberto ou n�o vistadas
		List[] listaPendencias = processoNe.consultarPendenciasProcesso(processoDt.getId(), true);
		request.setAttribute("ListaPendencias", listaPendencias);

		//Consultar conclus�es em aberto
		List conclusoesPendentes = processoNe.consultarConclusoesPendentesProcessoPublico(processoDt.getId(), true);
		request.setAttribute("ConclusaoPendente", conclusoesPendentes);

		//Consultar audi�ncias em aberto
		List<String[]> audienciaPendente = processoNe.consultarAudienciasPendentes(processoDt.getId(), true);
		request.setAttribute("AudienciaPendente", audienciaPendente);
	}

	/**
	 * M�todo respons�vel em consultar os respons�veis pelo processo
	 * @throws Exception 
	 */
	protected void consultarResponsaveisProcesso(HttpServletRequest request, ProcessoDt processoDt, UsuarioNe usuarioNe, ProcessoNe processoNe) throws Exception{
		//Respons�veis Pend�ncia
		List listaResponsaveis = processoNe.consultarResponsaveisProcesso(processoDt.getId(), processoDt.getId_Serventia(), usuarioNe.getUsuarioDt().getGrupoCodigo());
		request.setAttribute("ListaResponsaveis", listaResponsaveis);

		List listaAdvogados = processoNe.consultarAdvogadosProcesso(processoDt.getId());
		request.setAttribute("ListaAdvogados", listaAdvogados);
	}
	
	private final String idCodigoHashEOutros = "CodigoHashEOutros";

	/**
	 * Armazena o c�digo hash gerado para valida��o futura durante o acesso ao link e outros, evitando uso de rob�
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected void armazeneCodigoHashEOutros(HttpServletRequest request, UsuarioNe usuarioNe, String idProcesso) throws Exception{
		request.getSession().setAttribute(idCodigoHashEOutros, usuarioNe.getCodigoHash(idProcesso));
	}
	
	/**
	 * Valida o c�digo hash gerado para acesso ao link e outros, evitando uso de rob�
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected boolean validaCodigoHashEOutros(HttpServletRequest request, UsuarioNe usuarioNe, String idProcesso) throws Exception{
		String codigoHashGerado = (String) request.getSession().getAttribute(idCodigoHashEOutros);
		
		if (codigoHashGerado == null || codigoHashGerado.trim().equalsIgnoreCase("")) return false;
		
		return usuarioNe.VerificarCodigoHash(idProcesso, codigoHashGerado);
	}
	
	public int Permissao() {
		return 861;
	}
	
	//este m�todo deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
    protected String getId_GrupoPublico() {		
    	return GrupoDt.ID_GRUPO_PUBLICO;
	}

}

