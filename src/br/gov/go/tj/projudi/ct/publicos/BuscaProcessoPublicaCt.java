package br.gov.go.tj.projudi.ct.publicos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.dt.BuscaProcessoDt;
import br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet responsável pela Consulta Pública de de Processos. Essa servlet estende diretamente de HttpServlet e não de Controle, pois a população terá acesso a essa consulta
 * @author msapaula
 *
 */
public class BuscaProcessoPublicaCt extends Controle {

	private static final long serialVersionUID = -1681636213882054716L;
	
	private final String idCodigoHashEOutros = "CodigoHashEOutros";
		
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception{
		BuscaProcessoDt buscaProcessoDt;
		ProcessoDt objProcessoDt;
		ProcessoNe Processone;
		String numProcesso = "";
		boolean desconsideraExecPen = false;
		List tempList = null;
		String stId = "";
		int passoBusca = 0;
		String posicaoPagina = "";
		String PosicaoPaginaAtual = "";
		String stAcao;
		int passoEditar = -1;
		String servletRedirect = request.getParameter("ServletRedirect");
		String tituloDaPagina = "Consulta Pública de Processos";
		String stNomeBusca1 = "";
		
		if (request.getParameter("TituloDaPagina") != null) tituloDaPagina = request.getParameter("TituloDaPagina");

		response.setContentType("text/html");
		response.setCharacterEncoding("iso-8859-1");
		request.setAttribute("tempPrograma", "Processo");
		request.setAttribute("tempBuscaId_Processo", "Id_Processo");
		request.setAttribute("tempRetorno", "BuscaProcessoPublica");
		request.setAttribute("tempRetornoProcesso", "BuscaProcessoPublica");
		request.setAttribute("ServletRedirect", servletRedirect);
		request.setAttribute("TituloDaPagina", tituloDaPagina);
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		//Se o tipoConsultaProcesso deve-se setá-lo na sessão para que ele não se perca após o submit da tela jsp.
		//Valor 1 passado para consulta publica de processo por advogado
		if (request.getParameter("tipoConsultaProcesso") != null) {
			request.getSession().setAttribute("tipoConsultaProcesso", request.getParameter("tipoConsultaProcesso"));
			request.removeAttribute("tipoConsultaProcesso");
		}
		
		String tipoConsultaProcesso = request.getSession().getAttribute("tipoConsultaProcesso")!=null?(String)request.getSession().getAttribute("tipoConsultaProcesso"):"";
		
		if(tipoConsultaProcesso.equals("1")){
			stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaPublicaDadosAdvogado.jsp";
		}else {
			stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaPublica.jsp";
		}				
		
		Processone = (ProcessoNe) request.getSession().getAttribute("BuscaProcessone");
		if (Processone == null) Processone = new ProcessoNe();		

		buscaProcessoDt = (BuscaProcessoDt) request.getSession().getAttribute("buscaProcessoDt");
		if (buscaProcessoDt == null) {
			buscaProcessoDt = new BuscaProcessoDt();
		}
		
		objProcessoDt = (ProcessoDt) request.getSession().getAttribute("ProcessoDt");
		if (objProcessoDt == null) {
			objProcessoDt = new ProcessoDt();
		}
		if (request.getParameter("PaginaAtual") != null) paginaatual = Funcoes.StringToInt(request.getParameter("PaginaAtual"));
		if (request.getParameter("PosicaoPaginaAtual") == null) PosicaoPaginaAtual = "0"; else PosicaoPaginaAtual = request.getParameter("PosicaoPaginaAtual");
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));

		//Pega valor digitado na caixa de paginação
		if (request.getParameter("PosicaoPagina") == null) {
			posicaoPagina = PosicaoPaginaAtual; 
		}else {
			posicaoPagina = String.valueOf((Funcoes.StringToInt(request.getParameter("PosicaoPagina"))) - 1);
		}
		
		if (request.getParameter("numProcesso") != null){
			numProcesso = String.valueOf(request.getParameter("numProcesso"));
			paginaatual = Configuracao.Curinga7;			
			if (request.getParameter("desconsideraExecPen") != null ){
				desconsideraExecPen = request.getParameter("desconsideraExecPen").trim().equalsIgnoreCase("S");
			}
		}
	
		atribuiRequest(request, buscaProcessoDt);
		
		//todos os campos de pesquisa so devem ser atribuidos se não forem nulos
		//o valor será setado no request.
		String pesquisarNomeExato = request.getParameter("pesquisarNomeExato");
		if (pesquisarNomeExato!=null && !pesquisarNomeExato.isEmpty()) {			
			buscaProcessoDt.setPesquisarNomeExato(pesquisarNomeExato);
		}		

//		//Retira "." e "-" do número do processo
//		buscaProcessoDt.setProcessoNumero(request.getParameter("ProcessoNumero"));
//		buscaProcessoDt.setPromovente(request.getParameter("NomeParte"));
//		//Retira "/" "." e "-" do cpf
//		buscaProcessoDt.setCpfCnpjParte(request.getParameter("CpfCnpjParte"));
//				
//		buscaProcessoDt.setProcessoStatusCodigo(request.getParameter("ProcessoStatusCodigo"));
//		buscaProcessoDt.setId_ProcessoStatus(request.getParameter("Id_ProcessoStatus"));
//		buscaProcessoDt.setProcessoStatus(request.getParameter("ProcessoStatus"));
//		buscaProcessoDt.setServentia(request.getParameter("Serventia"));
//		buscaProcessoDt.setId_Serventia(request.getParameter("Id_Serventia"));
//		buscaProcessoDt.setOabNumero(request.getParameter("OabNumero"));
//		buscaProcessoDt.setOabComplemento(request.getParameter("OabComplemento"));
//		buscaProcessoDt.setOabUf(request.getParameter("OabUf"));
//		buscaProcessoDt.setSituacaoAdvogadoProcesso(request.getParameter("situacaoAdvogadoProcesso"));

		/**
		 * Variável PassoBusca utilizada para auxiliar na busca
		 * PassoBusca 0 : Redireciona para jsp de Dados do Processo 
		 * passoBusca 1 : Significa que está iniciando a busca, redireciona então para jsp de Pesquisa de Processos
		 * PassoBusca 2 : Significa que um processo foi selecionado e deve exibir o captcha
		 * PassoBusca 3 : Valida Captcha e caso usuario tenha digitado corretamente, redireciona para tela de Dados do Processo  
		 */
		if (request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			case Configuracao.Novo:
				buscaProcessoDt.limpar();
				Processone.limparVariaveisConsulta();
				if (request.getSession().getAttribute("tipoConsultaProcesso") != null) request.getSession().removeAttribute("tipoConsultaProcesso");

				stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaPublica.jsp";
				break;

			//Consulta pública de processos. 
			// Método retorna processos que população pode visualizar, aqueles ATIVOS e que NÃO sejam SEGREDO DE JUSTIÇA.
			case Configuracao.Localizar:
				if (checkRecaptcha(request)) {
				//if (passoBusca == 2 || validaCaptcha(request)) {
											
					if (tipoConsultaProcesso.equals("1")) {
						String mensagemRetorno = "";
						if (((buscaProcessoDt.getOabNumero() == null || buscaProcessoDt.getOabNumero().equals("")) ||
								(buscaProcessoDt.getOabComplemento() == null || buscaProcessoDt.getOabComplemento().equals("")) ||
								(buscaProcessoDt.getOabUf() == null || buscaProcessoDt.getOabUf().equals("")))) {
							mensagemRetorno = "OAB Número/Complemento e UF devem ser informados.";
						}
						if (mensagemRetorno.equals("")) {
							tempList = Processone.consultarProcessosDadosAdvogado(buscaProcessoDt, UsuarioSessao.getUsuarioDt(), posicaoPagina);
							stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaPublicaDadosAdvogado.jsp";
						} else {
							request.setAttribute("MensagemErro", mensagemRetorno);
							request.setAttribute("PaginaAtual", Configuracao.Novo);
							stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaPublicaDadosAdvogado.jsp";
						}
					} else {	
						tempList = Processone.consultarProcessosPublica(buscaProcessoDt, posicaoPagina);
					}	
					
					
					if (request.getParameter("ProcessoNumero") != null && request.getParameter("ProcessoNumero").trim().length() > 0 && tempList.size() == 1) {
						request.setAttribute("PaginaAtual", Configuracao.Editar);
						stId = ((ProcessoDt) tempList.get(0)).getId();
						armazeneCodigoHashEOutros(request,UsuarioSessao,stId);
						
						if (stId != null && stId.length() > 0) {
							if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(buscaProcessoDt.getId_Processo())) {
								buscaProcessoDt.limpar();
								objProcessoDt = Processone.consultarDadosProcessoAcessoExterno(stId,UsuarioSessao.getUsuarioDt() , true, false, UsuarioSessao.getNivelAcesso());
								request.getSession().setAttribute("processoDt", objProcessoDt);

								if (objProcessoDt.getId_Recurso().length() > 0) {
									// Quando se tratar de recurso inominado os dados do recurso serão mostrados
									RecursoDt recursoDt = Processone.consultarDadosRecurso(objProcessoDt.getId_Recurso(), objProcessoDt, UsuarioSessao.getUsuarioDt(), true);
									objProcessoDt.setRecursoDt(recursoDt);
								}
							}
							if (servletRedirect != null && servletRedirect.trim().length() > 0 &&	!servletRedirect.trim().equals("null")) {										
								if (servletRedirect.equalsIgnoreCase("GuiaLocomocaoPublica") || servletRedirect.equalsIgnoreCase("GuiaLocomocaoComplementarPublica"))
									servletRedirect += "?PaginaAtual=" + Configuracao.Novo;
								
								redireciona(response, servletRedirect);
								return;																				
							} 
							stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
							if(objProcessoDt.isSegredoJustica() || objProcessoDt.isSigiloso()){
								if(!Processone.podeAcessarProcesso(UsuarioSessao.getUsuarioDt(), objProcessoDt, null)) {
									stAcao = "/WEB-INF/jsptjgo/DadosProcessoSegredoJustica.jsp";
								}
							}
						}
					} else if(tempList != null) {
						if(tempList.size() > 0) {
							request.setAttribute("ListaProcesso", tempList);
							request.setAttribute("PaginaAtual", Configuracao.Localizar);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
							request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
									
							passoBusca = 2;
							stAcao = "/WEB-INF/jsptjgo/ListaProcessosConsultaPublica.jsp";
						} else {
							request.setAttribute("MensagemErro", "Nenhum Processo foi localizado para os parâmetros informados.");
							request.setAttribute("PaginaAtual", Configuracao.Editar);
						}
					}		
				}
				break;

			//Visualizar Situação do Processo
			case Configuracao.Imprimir:
				this.consultarSituacaoProcesso(request, buscaProcessoDt, Processone);
				stAcao = "/WEB-INF/jsptjgo/ProcessoSituacaoConsultaPublica.jsp";
				break;

			//Consultar Responsáveis pelo Processo
			case Configuracao.LocalizarAutoPai:
				this.consultarResponsaveisProcesso(request, buscaProcessoDt, UsuarioSessao, Processone);
				stAcao = "/WEB-INF/jsptjgo/ProcessoResponsaveis.jsp";
				break;

			// Efetua download de arquivos
			case Configuracao.Curinga6:
				String id_MovimentacaoArquivo = request.getParameter("Id_MovimentacaoArquivo");
				if (UsuarioSessao.VerificarCodigoHash(id_MovimentacaoArquivo+objProcessoDt.getId_Processo(), request.getParameter("hash"))) {
					boolean recibo = false;
					if (request.getParameter("recibo") != null && request.getParameter("recibo").equals("true")) recibo = true;

					//Informações do usuário e IP para gerar log do download
					String id_Usuario = UsuarioDt.SistemaProjudi;
					String ipComputador = request.getRemoteAddr();
														
					if (request.getParameter("CodigoVerificacao") != null && request.getParameter("CodigoVerificacao").equals("true")){
						if (request.getParameter("expedirImprimir") != null && request.getParameter("expedirImprimir").equals("true")){
							request.setAttribute("tempRetorno", "BuscaProcesso");
						}
						
						String stIdArquivo = Processone.consultarIdArquivo(id_MovimentacaoArquivo); 
						if (stIdArquivo != null && stIdArquivo.length() > 0) {
							if (!stIdArquivo.equalsIgnoreCase("")) {													
								// gerar pdf como arquivos da publicação
								byte[] arquivoPDF = Processone.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao(),stIdArquivo, id_MovimentacaoArquivo, objProcessoDt,  UsuarioSessao.getUsuarioDt(), new LogDt(id_Usuario, ipComputador));
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
						if (!Processone.baixarArquivoMovimentacao(id_MovimentacaoArquivo, objProcessoDt, UsuarioSessao.getUsuarioDt(), recibo, response, new LogDt(id_Usuario, ipComputador), true)){;
							//se baixou mandar para a jsp de não permitido baixar o arquivo
							stAcao = "/WEB-INF/jsptjgo/Padroes/ArquivoNaoPermitido.jsp";
							RequestDispatcher dis = request.getRequestDispatcher(stAcao);
							dis.include(request, response);
						}
					}
						
				} else exibaMensagemInconsistenciaErro(request, "Acesso negado.");
				return;
				
			// Verificar Processo Existe
			case Configuracao.Curinga7:
				if (numProcesso != null && numProcesso.length() > 0 && Processone.verificarExistenciaProcesso(numProcesso, desconsideraExecPen))
					response.getWriter().write("Ok. Processo encontrado.");
				else
					response.sendError(HttpServletResponse.SC_NOT_FOUND, "Processo Não encontrado.");
				return;
				
			case Configuracao.Curinga9:
				stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaPublicaDadosAdvogado.jsp";
				break;
				
			//Consulta status de processo disponíveis
			case (ProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Descrição"};
					String[] lisDescricao = {"Descrição", "Código"};
					String[] camposHidden = {"ProcessoStatusCodigo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoStatus");
					request.setAttribute("tempBuscaDescricao","ProcessoStatus");
					request.setAttribute("tempBuscaPrograma","Status do Processo");			
					request.setAttribute("tempRetorno","BuscaProcessoPublica?PassoBusca=" + passoBusca);		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);					
				} else {
					String stTemp="";
					stTemp = Processone.consultarDescricaoProcessoStatusJSON(stNomeBusca1, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);
					return;								
				}
				break;

			//Consulta serventias disponíveis
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia", "Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "BuscaProcessoPublica?PassoBusca=" + passoBusca);
					request.setAttribute("tempDescricaoId", "Id");
					//Retornando para a página atual da consulta de busca de processos por advogado - Curinga 9.
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga9);
					request.setAttribute("PaginaAtual", ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = Processone.consultarServentiaJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt());					
					enviarJSON(response, stTemp);									
					return;
				}
				break;

			default:
				
				// Função Editar é dividida da seguinte forma:
				// Passo  1: lista os eventos da execução penal.
				// Passo  6: visualiza último atestado de pena a cumprir
				// passo  8: consulta dados da ação penal, na lista de eventos
				// Passo 11: Redireciona para jsp de consulta todas as partes.
				switch (passoEditar) {

				//lista os eventos da execução penal.
				case 1:
					CalculoLiquidacaoDt calculoLiquidacaoDt = new CalculoLiquidacaoDt();
					List listaCondenacaoExtinta = new ArrayList();
					HashMap map = Processone.montarListaEventosCompleta(buscaProcessoDt.getId_Processo(), UsuarioSessao.getUsuarioDt(), calculoLiquidacaoDt, listaCondenacaoExtinta);
					List listaEventos =  (List)map.get("listaEventos");
					List listaHistoricoPsc = (List)map.get("listaHistoricoPsc");
					List listaHistoricoLfs = (List)map.get("listaHistoricoLfs");
					request.setAttribute("permissaoEditarEvento", "false");
					request.setAttribute("permissaoImprimir", "false");
					request.setAttribute("TituloPagina", "Eventos da Execução Penal");
					//utilizado nas jsps para controlar a interface de cálculo e evento para os tipos de pena: PPL e PRD
					request.setAttribute("isPenaRestritivaDireito", Processone.isProcessoPenaRestritivaDireito(listaEventos));
					request.getSession().setAttribute("listaEventos", listaEventos); //utilizado na jsp
					request.getSession().setAttribute("listaHistoricoPsc", listaHistoricoPsc); //utilizado na jsp
					request.getSession().setAttribute("listaHistoricoLfs", listaHistoricoLfs); //utilizado na jsp
					request.getSession().setAttribute("listaCondenacaoExtinta", listaCondenacaoExtinta); //utilizado na jsp
					request.getSession().setAttribute("CalculoLiquidacaodt", calculoLiquidacaoDt);
					//informações do último cálculo
					DataProvavelDt ultimoCalculo = new DataProvavelDt();
					ultimoCalculo = Processone.consultarCalculoLiquidacao(buscaProcessoDt.getId_Processo());
					if (ultimoCalculo.getRelatorioByte() != null){
						request.setAttribute("visualizaUltimoCalculo", "true");
						calculoLiquidacaoDt.setRelatorioByte(ultimoCalculo.getRelatorioByte());
					}
					request.getSession().setAttribute("ultimoCalculo", ultimoCalculo);
					stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
					break;
					
				//visualiza último atestado de pena a cumprir
				case 6:
					stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
					DataProvavelDt calculo = (DataProvavelDt)request.getSession().getAttribute("ultimoCalculo");
					
					//visualização do atestado pelo ícone na capa do processo
					if (calculo == null || calculo.getRelatorioByte() == null) {
						calculo = new DataProvavelDt();
						calculo = Processone.consultarCalculoLiquidacao(buscaProcessoDt.getId_Processo());
						request.getSession().setAttribute("ultimoCalculo", calculo);
						stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
					}
					
					byte[] byTemp = Processone.visulizarAtestadoPenaCumprir(calculo, ProjudiPropriedades.getInstance().getCaminhoAplicacao());
					
					if (byTemp != null){ 												
				        enviarPDF(response, byTemp,"Relatorio");
																										
						return;
					} else{
						request.setAttribute("MensagemErro", "Atestado de Pena a cumprir não localizado");
					}
					request.setAttribute("tempRetorno", "BuscaProcessoPublica");
					break;
					
				//consulta dados da ação penal, na lista de eventos --mesmo passo editar do BuscaProcessoPublica, BuscaProcesso, BuscaProcessoUsuarioExterno
				case 8: 
					String stTemp = "";
					request.setAttribute("tempRetorno", "BuscaProcessoPublica");
					stTemp = Processone.consultarIdAcaoPenalJSON(request.getParameter("idProcessoExecucao"));
					enviarJSON(response, stTemp);
					return;
					
				case 11:
					if (validaCodigoHashEOutros(request,UsuarioSessao, buscaProcessoDt.getId_Processo())){						
						request.setAttribute("tempRetorno", "BuscaProcessoPublica?Id_Processo=" + buscaProcessoDt.getId_Processo() + "&PassoBusca=2");
						stAcao = "/WEB-INF/jsptjgo/BuscaTodasPartesProcesso.jsp";
					}else{
						exibaMensagemInconsistenciaErro(request, "Acesso negado. Não foi possível exibir os dados das Partes.");
					}
					break;
					
				default:
					
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					stId = request.getParameter("Id_Processo");
					
					if (validaCodigoHashEOutros(request,UsuarioSessao, stId)){	
						stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
						if(objProcessoDt.isSegredoJustica() || objProcessoDt.isSigiloso()){
							if(!Processone.podeAcessarProcesso(UsuarioSessao.getUsuarioDt(), objProcessoDt, null)) {
								stAcao = "/WEB-INF/jsptjgo/DadosProcessoSegredoJustica.jsp";
							}
						}
					}else if (stId!=null && !stId.isEmpty() && checkRecaptcha(request)) {
						//se o processo ja está na session usa-se ele mesmo, não se faz nova consulta
						if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(objProcessoDt.getId_Processo())) {
							objProcessoDt.limpar();
							objProcessoDt = Processone.consultarDadosProcessoAcessoExterno(stId,UsuarioSessao.getUsuarioDt() , true, false, UsuarioSessao.getNivelAcesso());
							request.getSession().setAttribute("processoDt", objProcessoDt);
							
							//Define código hash para acesso ao link "e outros"
							armazeneCodigoHashEOutros(request,UsuarioSessao,stId);
							
							if (objProcessoDt.getId_Recurso().length() > 0) {
								// Quando se tratar de recurso inominado os dados do recurso serão mostrados
								RecursoDt recursoDt = Processone.consultarDadosRecurso(objProcessoDt.getId_Recurso(), objProcessoDt, UsuarioSessao.getUsuarioDt(), true);
								objProcessoDt.setRecursoDt(recursoDt);
							}
						}
						if (servletRedirect != null && servletRedirect.trim().length() > 0 &&	!servletRedirect.trim().equals("null")) {										
							if (servletRedirect.equalsIgnoreCase("GuiaLocomocaoPublica") || servletRedirect.equalsIgnoreCase("GuiaLocomocaoComplementarPublica"))
								servletRedirect += "?PaginaAtual=" + Configuracao.Novo;
							
							redireciona(response, servletRedirect);
							return;																				
						} 
						stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
						if(objProcessoDt.isSegredoJustica() || objProcessoDt.isSigiloso()){
							if(!Processone.podeAcessarProcesso(UsuarioSessao.getUsuarioDt(), objProcessoDt, null)) {
								stAcao = "/WEB-INF/jsptjgo/DadosProcessoSegredoJustica.jsp";
							}
						}
					} else {
						stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaPublica.jsp";
					}
					
					
				}

		}

		request.setAttribute("PassoBusca", passoBusca);
		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("PosicaoPagina", (Funcoes.StringToInt(posicaoPagina) + 1));
		request.getSession().setAttribute("buscaProcessoDt", buscaProcessoDt);
		request.getSession().setAttribute("ProcessoDt", objProcessoDt);
		request.getSession().setAttribute("BuscaProcessone", Processone);
		request.setAttribute("ServletRedirect", servletRedirect);
		request.setAttribute("TituloDaPagina", tituloDaPagina);
		request.setAttribute("TituloPagina", "Consulta de Processos");

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);

	}

	/**
	 * Método responsável em consultar a situação completa do processo.
	 * Retorna dados referentes a pendências, conclusões e audiências em aberto no processo.
	 * 
	 * @param request
	 * @param processoDt
	 * @param processoNe
	 * @throws Exception
	 */
	protected void consultarSituacaoProcesso(HttpServletRequest request, BuscaProcessoDt processoDt, ProcessoNe processoNe) throws Exception{

		//Consultar pendências em aberto ou não vistadas
		List[] listaPendencias = processoNe.consultarPendenciasProcesso(processoDt.getId(), true);
		request.setAttribute("ListaPendencias", listaPendencias);

		//Consultar conclusões em aberto
		List conclusoesPendentes = processoNe.consultarConclusoesPendentesProcessoPublico(processoDt.getId(), true);
		request.setAttribute("ConclusaoPendente", conclusoesPendentes);

		//Consultar audiências em aberto
		List<String[]> audienciaPendente = processoNe.consultarAudienciasPendentes(processoDt.getId(), true);
		request.setAttribute("AudienciaPendente", audienciaPendente);

	}

	/**
	 * Método responsável em consultar os responsáveis pelo processo
	 * @throws Exception 
	 */
	protected void consultarResponsaveisProcesso(HttpServletRequest request, BuscaProcessoDt processoDt, UsuarioNe usuarioNe, ProcessoNe processoNe) throws Exception{
		//Responsáveis Pendência
		List listaResponsaveis = processoNe.consultarResponsaveisProcesso(processoDt.getId(), processoDt.getId_Serventia(), usuarioNe.getUsuarioDt().getGrupoCodigo());
		request.setAttribute("ListaResponsaveis", listaResponsaveis);

		List listaAdvogados = processoNe.consultarAdvogadosProcessoPublico(processoDt.getId());
		request.setAttribute("ListaAdvogados", listaAdvogados);
	}
	
	
	/**
	 * Armazena o código hash gerado para validação futura durante o acesso ao link e outros, evitando uso de robô
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected void armazeneCodigoHashEOutros(HttpServletRequest request, UsuarioNe usuarioNe, String idProcesso) throws Exception{
		request.getSession().setAttribute(idCodigoHashEOutros, usuarioNe.getCodigoHash(idProcesso));
	}
	
	/**
	 * Valida o código hash gerado para acesso ao link e outros, evitando uso de robô
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected boolean validaCodigoHashEOutros(HttpServletRequest request, UsuarioNe usuarioNe, String idProcesso) throws Exception{
		String codigoHashGerado = (String) request.getSession().getAttribute(idCodigoHashEOutros);
		
		if (codigoHashGerado == null || codigoHashGerado.trim().equalsIgnoreCase("")) return false;
		
		return usuarioNe.VerificarCodigoHash(idProcesso, codigoHashGerado);
	}
	
	protected void exibaMensagemInconsistenciaErro(HttpServletRequest request, String mensagem){
    	request.setAttribute("MensagemErro",  mensagem);
    }
	
	
//	protected void redireciona(HttpServletResponse response, String url) throws IOException {
//		 response.sendRedirect(new String(Charset.forName("UTF-8").encode(url).array()));
//	 }


	@Override
	public int Permissao() {
		return 862;
	}

	//este método deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
	protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
}
