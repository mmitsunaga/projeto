package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BuscaProcessoDt;
import br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.PermissaoEspecialDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteAdvogadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * Servlet respons�vel pela Busca de Processos
 * Essa busca ser� usada pelos usu�rios externos (Advogados, Delegados, Promotores, Contadores e Consultores) 
 * @author msapaula
 *
 */
public class BuscaProcessoUsuarioExternoCt extends Controle {

	private static final long serialVersionUID = -8528202020491245325L;

	public int Permissao() {
		return ProcessoDt.CodigoPermissaoBuscaUsuarioExterno;
	}

	public BuscaProcessoUsuarioExternoCt() {
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		
		BuscaProcessoDt buscaProcessoDt = null;
		ProcessoDt processoDt = null;
		ProcessoNe Processone;

		String stId = "";
		int passoBusca = BuscaProcessoDt.CONSULTA_DEFAULT;
		int tipoConsulta = 0;
		String posicaoPagina = "";
		String stAcao;
		String serventiaSubTipoCodigo = null;
		int passoEditar = -1;
		int proprios = 0;
		String stTitulo = "Consulta de Processos";
		
		List listaAdvogados = new ArrayList();
		
		//********************************************************************************************************************
			if (request.getParameter("Proprios") == null ){
				if (request.getSession().getAttribute("Proprios") != null){
					if(request.getSession().getAttribute("Proprios").toString().equals("1")) {
						stTitulo = "Consulta de Processos Pr�prios";
					} else{
						stTitulo = "Consulta de Processos";
					}
					
					proprios = Funcoes.StringToInt(request.getSession().getAttribute("Proprios").toString());
				}
			
			} else if (request.getParameter("Proprios").toString().equals("1")){
				stTitulo = "Consulta de Processos Pr�prios";
				proprios = Funcoes.StringToInt(request.getParameter("Proprios"));
			} else {
				stTitulo = "Consulta de Processos";
				proprios = 0;
			}
		//*********************************************************************************************************************
		
				
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		int paginaAnterior = 0;
		request.setAttribute("PaginaAnterior", paginaatual);
		if (request.getParameter("PaginaAnterior") != null) {
			paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));		
		}

		request.setAttribute("tempPrograma", "Processo");
		request.setAttribute("tempRetorno", "BuscaProcessoUsuarioExterno");
		//request.setAttribute("TituloPagina", "Consulta de Processos");
		stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaUsuarioExterno.jsp";

		Processone = (ProcessoNe) request.getSession().getAttribute("BuscaProcessone");
		if (Processone == null) {
			Processone = new ProcessoNe();
		}
																			   
		buscaProcessoDt = (BuscaProcessoDt) request.getSession().getAttribute("buscaProcessoDt");
		if (buscaProcessoDt == null) {
			buscaProcessoDt = new BuscaProcessoDt();			
		}

		processoDt  = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt == null) {
			processoDt = new ProcessoDt();			
		}
		
		atribuiRequest(request, buscaProcessoDt);
		
//		buscaProcessoDt.setProcessoNumero(request.getParameter("ProcessoNumero"));
//		buscaProcessoDt.setId_ProcessoStatus(request.getParameter("Id_ProcessoStatus"));
//		buscaProcessoDt.setProcessoStatus(request.getParameter("ProcessoStatus"));
//		buscaProcessoDt.setProcessoStatusCodigo(request.getParameter("ProcessoStatusCodigo"));
//		buscaProcessoDt.setPromovente(request.getParameter("NomeParte"));
//		buscaProcessoDt.setCpfCnpjParte(request.getParameter("CpfCnpjParte"));
//		buscaProcessoDt.setId_Serventia(request.getParameter("Id_Serventia"));
//		buscaProcessoDt.setServentia(request.getParameter("Serventia"));
//		buscaProcessoDt.setTcoNumero(request.getParameter("TcoNumero"));
//		buscaProcessoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
//		buscaProcessoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
//		buscaProcessoDt.setId_Classificador(request.getParameter("Id_Classificador"));
//		buscaProcessoDt.setClassificador(request.getParameter("Classificador"));
		
		//Se o usu�rio marcar o checkbox na tela solicitando pesquisa por nome exato
		
		//todos os campos de pesquisa so devem ser atribuidos se n�o forem nulos
		//o valor ser� setado no request.
		String pesquisarNomeExato = request.getParameter("pesquisarNomeExato");
		if (pesquisarNomeExato!=null && !pesquisarNomeExato.isEmpty()) {			
			buscaProcessoDt.setPesquisarNomeExato(pesquisarNomeExato);
		}		
		//Vari�vel para auxiliar na consulta por sub-tipo de serventia (p�gina inicial)
		serventiaSubTipoCodigo = request.getParameter("codigo");
		if (serventiaSubTipoCodigo!=null && !serventiaSubTipoCodigo.isEmpty()) {
			buscaProcessoDt.setId_ServentiaSubTipo(serventiaSubTipoCodigo);			
		}
		buscaProcessoDt.setId_UsuarioServentia(UsuarioSessao.getId_UsuarioServentia());
		buscaProcessoDt.setId_ServentiaUsuario(UsuarioSessao.getId_Serventia());
		
		//Vari�vel PassoBusca utilizada para auxiliar na busca
		//	passoBusca 0 : est� iniciando uma nova pesquisa de processos
		//	passoBusca 1 : realiza consulta dos processos
		// 	passoBusca 2 : Significa que busca j� foi realizada e que provavelmente est� se usando pagina��o, n�o devendo assim limpar dados da consulta
		// 	passoBusca 3 : Valida Captcha e caso usuario tenha digitado corretamente, redireciona para tela de Dados do Processo  
		// 	passoBusca 4 : Significa que solicita��o de consulta veio da p�gina inicial
		if (request.getParameter("PassoBusca") != null) {
			passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		}

		// Vari�vel TipoConsulta utilizada para identificar qual consulta se refere, pois essa servlet ser� usada para consultas de todos usu�rios externos (advogado, delegado...)
		if (request.getParameter("TipoConsulta") != null) {
			tipoConsulta = Funcoes.StringToInt((String) request.getParameter("TipoConsulta"));
		}

		//Pega valor digitado na caixa de pagina��o
		if (request.getParameter("PosicaoPagina") == null) {
			posicaoPagina = PosicaoPaginaAtual;
		}else {
			posicaoPagina = String.valueOf((Funcoes.StringToInt(request.getParameter("PosicaoPagina"))) - 1);
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		if (request.getParameter("MensagemOk") != null) {
			request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		}else {
			request.setAttribute("MensagemOk", "");
		}
		if (request.getParameter("MensagemErro") != null) {
			request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		}else {
			request.setAttribute("MensagemErro", "");
		}
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		if (request.getParameter("PassoEditar") != null) {
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		}
		
		String quantidadeRegistrosPagina = "";
		if (request.getParameter("QuantidadeRegistrosPagina") != null) quantidadeRegistrosPagina = request.getParameter("QuantidadeRegistrosPagina");

		switch (paginaatual) {

			//--------------------------------------------------------------------------------------------------------------------
			// Fun��o utilizada para consultar os Advogados do Processo, para segredo de justi�a (DadosProcessoSegredoJustica.jsp)
			//--------------------------------------------------------------------------------------------------------------------
			case Configuracao.Curinga9:
				listaAdvogados = new ProcessoParteAdvogadoNe().consultarAdvogadosProcesso(processoDt.getId_Processo());
				stAcao = "/WEB-INF/jsptjgo/DadosProcessoSegredoJustica.jsp";
				break;
							
			//--------------------------------------------------------------------------------------------------------------------
			// Fun��o utilizada para iniciar uma consulta: limpa dados e traz status "ATIVO" selecionado
			//--------------------------------------------------------------------------------------------------------------------
			case Configuracao.Novo:
				buscaProcessoDt.limpar();
				buscaProcessoDt.setProcessoStatus("Ativo");
				buscaProcessoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
				Processone.limparVariaveisConsulta();
				if (tipoConsulta == Configuracao.Curinga7) {
					stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaDelegacia.jsp";
				}	else {
					stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaUsuarioExterno.jsp";
				}
				break;

			//--------------------------------------------------------------------------------------------------------------------
			// Consulta todos os processos que um usu�rio externo pode visualizar (Consultar - Todos) 
			//--------------------------------------------------------------------------------------------------------------------
			case Configuracao.Localizar:
				if (passoBusca == buscaProcessoDt.CONSULTA_DEFAULT) {
					//Redireciona para tela de Pesquisa de Processos
					limparDados(buscaProcessoDt);
					passoBusca = buscaProcessoDt.CONSULTA_PROCESSOS_ADVOGADO;
					request.getSession().removeAttribute("Proprios");
					stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaUsuarioExterno.jsp";
				} else {
					if (passoBusca != buscaProcessoDt.CONSULTA_USUARIO_INTERNO) {
//						Processone.limparVariaveisConsulta();
					}	else if (buscaProcessoDt.getId().length() > 0) {
						buscaProcessoDt.limpar();
					}

					if (consultarProcessosUsuariosExternos(request, buscaProcessoDt, Processone, UsuarioSessao,  posicaoPagina)) {
						passoBusca = buscaProcessoDt.CONSULTA_USUARIO_INTERNO;
						stAcao = "/WEB-INF/jsptjgo/ListaProcessosUsuarioExterno.jsp";
						
					}
				}
				tipoConsulta = Configuracao.Localizar;
				break;

			//--------------------------------------------------------------------------------------------------------------------
			// Fun��o utilizada para Consultar os processos de um Advogado ou Promotor (Consultar - Pr�prios)
			//--------------------------------------------------------------------------------------------------------------------
			case Configuracao.LocalizarDWR:
				if (passoBusca == buscaProcessoDt.CONSULTA_DEFAULT) {
					//Redireciona para tela de Pesquisa de Processos
					limparDados(buscaProcessoDt);
					passoBusca = buscaProcessoDt.CONSULTA_PROCESSOS_ADVOGADO;
					//request.setAttribute("TituloPagina", "Consulta Processos Pr�prios");
					stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaUsuarioExterno.jsp";
				} else {
					
					//Efetua consulta
					if (passoBusca == buscaProcessoDt.CONSULTA_USUARIO_EXTERNO) {
						Processone.limparVariaveisConsulta();
						//buscaProcessoDt.limpar();
						//Vari�vel para auxiliar na consulta por sub-tipo de serventia (p�gina inicial)
						//serventiaSubTipoCodigo = request.getParameter("codigo");
					} else if (passoBusca != buscaProcessoDt.CONSULTA_USUARIO_INTERNO){
//						Processone.limparVariaveisConsulta();
					}else if (buscaProcessoDt.getId().length() > 0) {
						buscaProcessoDt.limpar();
					}
										
					if (consultarProcessosProprios(request, UsuarioSessao, buscaProcessoDt, posicaoPagina,  Processone)) {
						
						List tempList = (List)request.getAttribute("ListaProcesso");
						if (request.getParameter("ProcessoNumero") != null && 
							request.getParameter("ProcessoNumero").trim().length() > 0 && 
							tempList != null && tempList.size() == 1) {
							
							stId = ((ProcessoDt) tempList.get(0)).getId();
							
							//Define c�digo rash para acesso ao link "e outros"
							armazeneCodigoRashEOutros(request,UsuarioSessao,stId);
							
							if (this.verificaPrivilegioUsuarioExterno(UsuarioSessao.getUsuarioDt())){
								//Se usu�rio logado for promotor, redireciona para tela de dados do processo
								stAcao = this.consultarDadosProcesso(stId, Processone, request, tipoConsulta, UsuarioSessao, ehAcessoOutraServentia(request, stId));
								GuiaEmissaoDt guiaEmissaoDt = Processone.consultarGuiaEmissaoInicialAguardandoPagamento(stId, UsuarioSessao.getId_Usuario());
								if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && guiaEmissaoDt.getId().length()>0){
									request.setAttribute("MensagemOk", "O processo possui uma Guia Inicial pendente de pagamento: "+ Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto())+
											". Desconsiderar se o pagamento da mesma j� foi efetuado, o sistema pode levar algum tempo para atualizar o Status da Guia.");
								}
								
							} else {
								stAcao = this.consultarDadosProcesso(stId, Processone, request, tipoConsulta, UsuarioSessao, ehAcessoOutraServentia(request, stId));
							}
						} else {
							passoBusca = 2;
							
							
							stAcao = "/WEB-INF/jsptjgo/ListaProcessosUsuarioExternoProprios.jsp";	
						}
					}
				}
				tipoConsulta = Configuracao.LocalizarDWR;
				break;

			//--------------------------------------------------------------------------------------------------------------------
			// Fun��o utilizada para baixar os arquivos do processo
			//--------------------------------------------------------------------------------------------------------------------
			case Configuracao.Curinga6:
				String id_MovimentacaoArquivo = request.getParameter("Id_MovimentacaoArquivo");
				
				if (processoDt!=null && UsuarioSessao.VerificarCodigoHash(id_MovimentacaoArquivo+processoDt.getId_Processo(), request.getParameter("hash"))) {
					boolean recibo = false;
					if (request.getParameter("recibo") != null && request.getParameter("recibo").equals("true")) recibo = true;
					if (request.getParameter("CodigoVerificacao") != null && request.getParameter("CodigoVerificacao").equals("true")){
						String stIdArquivo = Processone.consultarIdArquivo(id_MovimentacaoArquivo); 
						if (stIdArquivo != null && stIdArquivo.length() > 0) {
							if (!stIdArquivo.equalsIgnoreCase("")) {
															
								// gerar pdf como arquivos da publica��o
//									byte[] arquivoPDF = Processone.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() ,stIdArquivo);
								byte[] arquivoPDF = Processone.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() ,stIdArquivo, id_MovimentacaoArquivo, processoDt,  UsuarioSessao.getUsuarioDt(), new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()));									
								
								enviarPDF(response, arquivoPDF,"Publicacao");																									
								
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
						if(!Processone.baixarArquivoMovimentacao(id_MovimentacaoArquivo, processoDt, UsuarioSessao.getUsuarioDt(), recibo, response, new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()), false)){
							//se baixou mandar para a jsp de n�o permitido baixar o arquivo
							stAcao = "/WEB-INF/jsptjgo/Padroes/ArquivoNaoPermitido.jsp";
							RequestDispatcher dis = request.getRequestDispatcher(stAcao);
							dis.include(request, response);
						}
					}
				} else {
					super.exibaMensagemInconsistenciaErro(request, "Acesso negado.");
				}
				return;

				//--------------------------------------------------------------------------------------------------------------------
				// Fun��o utilizada para Consultar os processos ativos em uma Delegacia
				//--------------------------------------------------------------------------------------------------------------------
			case Configuracao.Curinga7:
				if (passoBusca == buscaProcessoDt.CONSULTA_DEFAULT) {
					//Redireciona para tela de Pesquisa de Processos da Delegacia
					limparDados(buscaProcessoDt);
					passoBusca = buscaProcessoDt.CONSULTA_PROCESSOS_ADVOGADO;
					stTitulo = "Consulta Processos Delegacia";
					stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaDelegacia.jsp";
				} else {
					//Efetua consulta
					if (passoBusca == BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO) {
						Processone.limparVariaveisConsulta();
						//buscaProcessoDt.limpar();
						passoBusca = buscaProcessoDt.CONSULTA_PROCESSOS_ADVOGADO; // Dever� limpar somente na primeira vez...
					} else if (passoBusca != BuscaProcessoDt.CONSULTA_USUARIO_INTERNO){
						Processone.limparVariaveisConsulta();
					}else if (buscaProcessoDt.getId().length() > 0){
						buscaProcessoDt.limpar();
					}
					buscaProcessoDt.setPesquisarNomeExato(pesquisarNomeExato);
					if (consultarProcessosDelegacia(request, buscaProcessoDt, Processone, UsuarioSessao,  posicaoPagina)) {
						passoBusca = buscaProcessoDt.CONSULTA_USUARIO_INTERNO;
						stAcao = "/WEB-INF/jsptjgo/ListaProcessosUsuarioExterno.jsp";
						
					} else stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaDelegacia.jsp";
				}
				tipoConsulta = Configuracao.Curinga7;
				break;

			//Visualizar Situa��o do Processo
			case Configuracao.Imprimir:
				this.consultarSituacaoProcesso(request, processoDt.getId(), Processone, UsuarioSessao.getUsuarioDt());
				stAcao = "/WEB-INF/jsptjgo/ProcessoSituacao.jsp";
				break;

			//Consultar Respons�veis pelo Processo
			case Configuracao.LocalizarAutoPai:
			    if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.CONSULTORES))) request.getSession().setAttribute("TipoConsulta", "Consultor");
				this.consultarResponsaveisProcesso(request, processoDt, UsuarioSessao, Processone);
				stAcao = "/WEB-INF/jsptjgo/ProcessoResponsaveis.jsp";
				break;

			//Consulta status de processo dispon�veis
			case (ProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Descri��o"};
					String[] lisDescricao = {"Descri��o", "C�digo"};
					String[] camposHidden = {"ProcessoStatusCodigo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoStatus");
					request.setAttribute("tempBuscaDescricao","ProcessoStatus");
					request.setAttribute("tempBuscaPrograma","Status do Processo");			
					request.setAttribute("tempRetorno","BuscaProcessoUsuarioExterno?TipoConsulta=" + tipoConsulta + "&PassoBusca=" + passoBusca);		
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

			//Consulta serventias dispon�veis
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia", "Estado"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Serventia");
				request.setAttribute("tempBuscaDescricao", "Serventia");
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempRetorno", "BuscaProcessoUsuarioExterno?TipoConsulta=" + tipoConsulta + "&PassoBusca=" + passoBusca);
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
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
			//Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPermissao = String.valueOf((ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					if (paginaAnterior == Configuracao.Curinga7){
						atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", "BuscaProcessoUsuarioExterno?TipoConsulta=" + tipoConsulta + "&PassoBusca=" + passoBusca, Configuracao.Curinga7 , stPermissao, lisNomeBusca, lisDescricao);
					} else {
						atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", "BuscaProcessoUsuarioExterno?TipoConsulta=" + tipoConsulta + "&PassoBusca=" + passoBusca, Configuracao.Editar , stPermissao, lisNomeBusca, lisDescricao);
					}
					
					break;
				} else{
					String stTemp1="";
					//stTemp1 = Processone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
					stTemp1 = Processone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, null);						
					enviarJSON(response, stTemp1);
											
					return;								
				}

			default:
				
				// Fun��o Editar � dividida da seguinte forma:
				// Passo  1: lista os eventos da execu��o penal.
				// Passo  6: visualiza �ltimo atestado de pena a cumprir
				// passo  8: consulta dados da a��o penal, na lista de eventos
				// Passo 11: Redireciona para jsp de consulta todas as partes
				switch (passoEditar) {
				
				//lista os eventos da execu��o penal.
				case 1:
					CalculoLiquidacaoDt calculoLiquidacaoDt = new CalculoLiquidacaoDt();
					List listaCondenacaoExtinta = new ArrayList();
					HashMap map = Processone.montarListaEventosCompleta(processoDt.getId_Processo(), UsuarioSessao.getUsuarioDt(), calculoLiquidacaoDt, listaCondenacaoExtinta);
					List listaEventos =  (List)map.get("listaEventos");
					List listaHistoricoPsc = (List)map.get("listaHistoricoPsc");
					List listaHistoricoLfs = (List)map.get("listaHistoricoLfs");
					request.setAttribute("permissaoEditarEvento", "false");
					request.setAttribute("permissaoImprimir", "false");
					stTitulo = "Eventos da Execu��o Penal";
					//utilizado nas jsps para controlar a interface de c�lculo e evento para os tipos de pena: PPL e PRD
					request.setAttribute("isPenaRestritivaDireito", Processone.isProcessoPenaRestritivaDireito(listaEventos));
					request.getSession().setAttribute("listaEventos", listaEventos); //utilizado na jsp
					request.getSession().setAttribute("listaHistoricoPsc", listaHistoricoPsc); //utilizado na jsp
					request.getSession().setAttribute("listaHistoricoLfs", listaHistoricoLfs); //utilizado na jsp
					request.getSession().setAttribute("listaCondenacaoExtinta", listaCondenacaoExtinta); //utilizado na jsp
					request.getSession().setAttribute("CalculoLiquidacaodt", calculoLiquidacaoDt);
					//informa��es do �ltimo c�lculo
					DataProvavelDt ultimoCalculo = new DataProvavelDt();
					ultimoCalculo = Processone.consultarCalculoLiquidacao(buscaProcessoDt.getId_Processo());
					if (ultimoCalculo.getRelatorioByte() != null){
						request.setAttribute("visualizaUltimoCalculo", "true");
						calculoLiquidacaoDt.setRelatorioByte(ultimoCalculo.getRelatorioByte());
					}
					request.getSession().setAttribute("ultimoCalculo", ultimoCalculo);
					stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
					break;
					
				//visualiza �ltimo atestado de pena a cumprir
				case 6:
					stAcao = "/WEB-INF/jsptjgo/DadosProcessoEventoExecucao.jsp";
					DataProvavelDt calculo = (DataProvavelDt)request.getSession().getAttribute("ultimoCalculo");
					
					//visualiza��o do atestado pelo �cone na capa do processo
					if (calculo == null || calculo.getRelatorioByte() == null) {
						calculo = new DataProvavelDt();
						calculo = Processone.consultarCalculoLiquidacao(processoDt.getId_Processo());
						request.getSession().setAttribute("ultimoCalculo", calculo);
						stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
					}
					
					byte[] byTemp = Processone.visulizarAtestadoPenaCumprir(calculo, ProjudiPropriedades.getInstance().getCaminhoAplicacao() );
					
					if (byTemp != null){
						
						enviarPDF(response, byTemp,"Relatorio");				
						
						byTemp = null;
						return;
					} else{
						request.setAttribute("MensagemErro", "Atestado de Pena a cumprir n�o localizado");
						this.consultarDadosProcesso(request.getParameter("Id_Processo"), Processone, request, tipoConsulta, UsuarioSessao, ehAcessoOutraServentia(request, stId));
					}
					request.setAttribute("tempRetorno", "BuscaProcessoUsuarioExterno");
					break;
					
				//consulta dados da a��o penal, na lista de eventos --mesmo passo editar do BuscaProcessoPublica, BuscaProcesso, BuscaProcessoUsuarioExterno
				case 8: 
					String stTemp = "";
					request.setAttribute("tempRetorno", "BuscaProcessoUsuarioExterno");
					stTemp = Processone.consultarIdAcaoPenalJSON(request.getParameter("idProcessoExecucao"));
						
						enviarJSON(response, stTemp);
						
					
					return;
					
				case 11:
					if (validaCodigoRashEOutros(request,UsuarioSessao, buscaProcessoDt.getId_Processo())){
						request.setAttribute("tempRetorno", "BuscaProcessoUsuarioExterno?PaginaAtual=-1&PassoBusca=2&TipoConsulta=3&Id_Processo=" + buscaProcessoDt.getId_Processo());
						stAcao = "/WEB-INF/jsptjgo/BuscaTodasPartesProcesso.jsp";
					}else{
						super.exibaMensagemInconsistenciaErro(request, "Acesso negado. N�o foi poss�vel exibir os dados das Partes.");
					}					
					break;
					
				default:
				
					stId = request.getParameter("Id_Processo");
					
					//Define c�digo rash para acesso ao link "e outros"
					armazeneCodigoRashEOutros(request,UsuarioSessao,stId);
					
					if (this.verificaPrivilegioUsuarioExterno(UsuarioSessao.getUsuarioDt()) && passoBusca == buscaProcessoDt.CONSULTA_USUARIO_INTERNO){
						//Se usu�rio logado for promotor, redireciona para tela de dados do processo
						stAcao = this.consultarDadosProcesso(stId, Processone, request, tipoConsulta, UsuarioSessao, ehAcessoOutraServentia(request, stId));
						GuiaEmissaoDt guiaEmissaoDt = Processone.consultarGuiaEmissaoInicialAguardandoPagamento(stId, UsuarioSessao.getId_Usuario());
						if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && guiaEmissaoDt.getId().length()>0){
							request.setAttribute("MensagemOk", "O processo possui uma Guia Inicial pendente de pagamento: "+Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto())+
									". Desconsiderar se o pagamento da mesma j� foi efetuado, o sistema pode levar algum tempo para atualizar o Status da Guia.");
						}
						
					} else {
					
						switch (passoBusca) {
							case 2: //Redireciona para tela de catpcha
								if (stId != null && stId.length() > 0) {
									stAcao = this.consultarDadosProcesso(stId, Processone, request, tipoConsulta, UsuarioSessao, ehAcessoOutraServentia(request, stId));
								}
								break;
		
							case 5:
								//Nesse caso significa que foi realizado um peticionamento devendo retornar para tela do processo com mensagem de sucesso
								if (stId != null && request.getParameter("MensagemOk") != null) {
									stAcao = this.consultarDadosProcesso(stId, Processone, request, tipoConsulta, UsuarioSessao, ehAcessoOutraServentia(request, stId));
								}
						}
					}
					break;
				}
		}
		
		//usu�rio externo n�o pode ver o classificador, isso ocorre pois a classifica��o j� indica um pr�via do andamento processual. 
		//Exemplo: extin��o permiss�o para visualizar classificador
		if (!UsuarioSessao.isUsuarioExterno()) {
			request.setAttribute("permissaoClassificador", "true");
		}
		
		//Permiss�o para visualizar o check para permitir consultar at� 500 processos por p�gina...
		if (!UsuarioSessao.isUsuarioExterno() || this.verificaAdvogadoPublicoPromotor(UsuarioSessao.getUsuarioDt())) {
			request.setAttribute("permissaoConsultar500ProcessosPorPagina", "true");
		}
		
		request.setAttribute("serventiaSubTipoCodigo", serventiaSubTipoCodigo);
		request.setAttribute("PassoBusca", passoBusca);
		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("TipoConsulta", tipoConsulta);
		request.setAttribute("listaAdvogados", listaAdvogados);
		request.getSession().setAttribute("Proprios", proprios);
		request.setAttribute("TituloPagina", stTitulo);
		request.setAttribute("PosicaoPagina", (Funcoes.StringToInt(posicaoPagina) + 1));
		request.setAttribute("QuantidadeRegistrosPagina", quantidadeRegistrosPagina);
		request.getSession().setAttribute("buscaProcessoDt", buscaProcessoDt);
		//request.getSession().setAttribute("processoDt", processoDt);		
		request.getSession().setAttribute("BuscaProcessone", Processone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Verifica se � um usu�rio externo com privil�gio
	 * @param UsuarioDt, usuarioDt usuario logado
	 */
	protected boolean verificaPrivilegioUsuarioExterno(UsuarioDt usuarioDt){
		boolean retorno = false;
		
		int grupoCodigo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		if ((GrupoDt.MINISTERIO_PUBLICO  == grupoCodigo )			 
		 	 || (GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL == grupoCodigo)
		 	 || (GrupoDt.MP_TCE == grupoCodigo)
		 	 || (GrupoDt.ADVOGADO_PUBLICO_ESTADUAL == grupoCodigo)
		 	 || (GrupoDt.ADVOGADO_PUBLICO == grupoCodigo)
		 	 || (GrupoDt.ADVOGADO_PUBLICO_UNIAO == grupoCodigo)
		 	 || (GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO == grupoCodigo)
		 	 || (GrupoDt.ADVOGADO_PARTICULAR == grupoCodigo)
		 	 || (GrupoDt.ADVOGADO_DEFENSOR_PUBLICO == grupoCodigo)		 	 		 	 
		 	 || (GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA == grupoCodigo )
		 	 || (GrupoDt.COORDENADOR_PROMOTORIA == grupoCodigo)	 
		 	 || (GrupoTipoDt.COORDENADOR_PROCURADORIA == grupoCodigo)){
			retorno = true;
		}
		
		return retorno;		
	}
	
	/**
	 * Verifica se � um advogado p�blico ou promotor
	 * @param UsuarioDt, usuarioDt usuario logado
	 */
	private boolean verificaAdvogadoPublicoPromotor(UsuarioDt usuarioDt){
		boolean retorno = false;
		int grupoCodigo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		if ((GrupoDt.MINISTERIO_PUBLICO  == grupoCodigo)			 
			 || (GrupoDt.MP_TCE == grupoCodigo)				
		 	 || (GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL == grupoCodigo)
		 	 || (GrupoDt.ADVOGADO_PUBLICO_ESTADUAL == grupoCodigo)
		 	 || (GrupoDt.ADVOGADO_PUBLICO == grupoCodigo)
		 	 || (GrupoDt.ADVOGADO_DEFENSOR_PUBLICO == grupoCodigo )
		 	 || (GrupoDt.ADVOGADO_PUBLICO_UNIAO == grupoCodigo)){
			retorno = true;
		}
		
		return retorno;	
	}

	/**
	 * Consulta os dados do processo para exibir na tela os Dados Completos
	 * @param id_Processo
	 * @param processoDt
	 * @param processoNe
	 * @param request
	 * @param usuarioSessao
	 * @throws Exception 
	 */
	private String consultarDadosProcesso(String id_Processo, ProcessoNe processoNe, HttpServletRequest request, int tipoConsulta, UsuarioNe usuarioSessao, boolean acessoOutraServentia) throws Exception{
		
		String stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
		
		ProcessoDt processoDt = null;
		
		//Verifica se usu�rio pode peticionar.
		

		//Verifica se tem necessidade de consultar os dados completos do processo novamente
		if (id_Processo!=null && id_Processo.length()>0) {
			if (tipoConsulta == Configuracao.Localizar) {
				//Refere-se a consulta de Todos Processos feita por usu�rios externos, deve tratar casos de Segredo de Justi�a
				processoDt = processoNe.consultarDadosProcessoAcessoExterno(id_Processo, usuarioSessao.getUsuarioDt(), false, acessoOutraServentia, usuarioSessao.getNivelAcesso());
			} else {
				processoDt = processoNe.consultarDadosProcesso(id_Processo, usuarioSessao.getUsuarioDt(), false, acessoOutraServentia, usuarioSessao.getNivelAcesso());
			}
			request.getSession().setAttribute("processoDt", processoDt);
		
			// Prepara menu especial de Processo
			String Menu = usuarioSessao.getMenuEspecial(PermissaoEspecialDt.OpcoesProcessoCodigo);	
			String Menu2 = usuarioSessao.getMenuEspecial(PermissaoEspecialDt.OpcoesProcessoHibrido);
			String Menu_outas = usuarioSessao.getMenuEspecial(PermissaoEspecialDt.OPCOES_OUTRAS);
			String Menu_exhibrido = usuarioSessao.getMenuEspecial(PermissaoEspecialDt.OpcoesProcessoExHibrido);

			request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoCodigo, Menu);			
			request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoHibrido, Menu2);			
			request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OPCOES_OUTRAS, Menu_outas);
			request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoExHibrido, Menu_exhibrido);
			
			if (processoDt.getId_Recurso().length() != 0){			
				// Quando se tratar de recurso inominado dever� exibir um menu especial diferenciado e os dados do recurso ser�o mostrados				
				RecursoDt recursoDt = processoNe.consultarDadosRecurso(processoDt.getId_Recurso(), false);
				processoDt.setRecursoDt(recursoDt);
			}
			
			if(processoDt.isSegredoJustica() || processoDt.isSigiloso()){			
				if(!processoNe.podeAcessarProcesso(usuarioSessao.getUsuarioDt(), processoDt, null)) {
					stAcao = "/WEB-INF/jsptjgo/DadosProcessoSegredoJustica.jsp";
				}
			} 
		}
		
		return stAcao;

	}

	/**
	 * Consulta os processos que usu�rios da delegacia podem acessar.
	 * Chama ProcessoNe para que fa�a os tratamento necess�rios e devolva a lista de processos
	 * @throws Exception 
	 */
	protected boolean consultarProcessosDelegacia(HttpServletRequest request, BuscaProcessoDt buscaProcessodt, ProcessoNe processone, UsuarioNe usuarioSessao,  String posicaoPagina) throws Exception{
		boolean boRetorno = false;
		int quantidadeRegistrosPagina = Funcoes.StringToInt(request.getParameter("QuantidadeRegistrosPagina"));
		int sigiloso = Funcoes.StringToInt(buscaProcessodt.getProcessoStatusCodigo());
		List tempList = null;
		if (sigiloso== ProcessoStatusDt.SIGILOSO){
			tempList = processone.consultarProcessosSigiloso( usuarioSessao.getUsuarioDt(),  posicaoPagina);
		}else{
			tempList = processone.consultarProcessosUsuariosExternos(buscaProcessodt, usuarioSessao.getUsuarioDt(),   posicaoPagina, String.valueOf(quantidadeRegistrosPagina));
		}
		
		if (tempList.size() > 0) {
			request.setAttribute("ListaProcesso", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Curinga7);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
			request.setAttribute("QuantidadePaginas", processone.getQuantidadePaginas());
			if (quantidadeRegistrosPagina > 0) request.setAttribute("QuantidadeRegistrosPagina", quantidadeRegistrosPagina);
			
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Processo Localizado");
		return boRetorno;
	}

	/**
	 * Consulta os processos pr�prios tanto para advogados quanto para promotores.
	 * Chama ProcessoNe para que fa�a os tratamento necess�rios e devolva a lista de processos
	 * @throws Exception 
	 */
	protected boolean consultarProcessosProprios(HttpServletRequest request, UsuarioNe usuarioSessao, BuscaProcessoDt buscaProcessoDt, String posicaoPagina, ProcessoNe processoNe) throws Exception{
		boolean boRetorno = false;

		buscaProcessoDt.setProcessoStatusCodigo(request.getParameter("ProcessoStatusCodigo"));
		List tempList = null;
		int quantidadeRegistrosPagina = Funcoes.StringToInt(request.getParameter("QuantidadeRegistrosPagina"));
		if (buscaProcessoDt.isSigiloso() && (usuarioSessao.isMp() || usuarioSessao.isAssessorAdvogado() || usuarioSessao.isAssessorMP())){
			tempList = processoNe.consultarProcessosSigiloso( usuarioSessao.getUsuarioDt(), posicaoPagina);
		}else {			
			tempList = processoNe.consultarProcessosUsuariosExternos(buscaProcessoDt, usuarioSessao.getUsuarioDt(),   posicaoPagina, String.valueOf(quantidadeRegistrosPagina));
		}
		

		if (tempList!= null && tempList.size() > 0) {
			request.setAttribute("ListaProcesso", tempList);
			request.setAttribute("PaginaAtual", Configuracao.LocalizarDWR);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
			request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());
			if (quantidadeRegistrosPagina > 0) request.setAttribute("QuantidadeRegistrosPagina", quantidadeRegistrosPagina);
			
			boRetorno = true;
		} else{
			request.setAttribute("MensagemErro", "Nenhum Processo Localizado");
		}

		return boRetorno;
	}

	/**
	 * Consulta os processos que usu�rios externos podem acessar.
	 * Chama ProcessoNe para que fa�a os tratamento necess�rios e devolva a lista de processos
	 * @throws Exception 
	 */
	protected boolean consultarProcessosUsuariosExternos(HttpServletRequest request, BuscaProcessoDt processodt, ProcessoNe processone, UsuarioNe usuarioSessao, String posicaoPagina) throws Exception{
		boolean boRetorno = false;		
		List tempList = processone.consultarTodosProcessosUsuariosExternos(processodt, usuarioSessao.getUsuarioDt(), posicaoPagina);
		if (tempList.size() > 0) {
			request.setAttribute("ListaProcesso", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
			request.setAttribute("QuantidadePaginas", processone.getQuantidadePaginas());			
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Processo foi localizado para os par�metros informados.");
		return boRetorno;
	}

	//Consulta de serventias
	protected boolean consultarServentia(HttpServletRequest request, ProcessoNe processone, String tempNomeBusca, String posicaoPagina, int paginaatual, int tipoConsulta, int passoBusca, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;
		List tempList = processone.consultarServentia(tempNomeBusca, posicaoPagina, usuarioSessao.getUsuarioDt());
		if (tempList.size() > 0) {
			request.setAttribute("ListaServentia", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
			request.setAttribute("QuantidadePaginas", processone.getQuantidadePaginas());
			request.setAttribute("tempRetorno", "BuscaProcessoUsuarioExterno?TipoConsulta=" + tipoConsulta + "&PassoBusca=" + passoBusca);
			request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
			request.setAttribute("tempBuscaServentia", "Serventia");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Dados N�o Localizados");
		return boRetorno;
	}

	//Consulta os status de processo dispon�veis
	protected boolean consultarProcessoStatus(HttpServletRequest request, ProcessoNe processone, String tempNomeBusca, String posicaoPagina, int tipoConsulta, int passoBusca, int paginaatual) throws Exception{
		boolean boRetorno = false;
		List tempList = processone.consultarDescricaoProcessoStatus(tempNomeBusca, posicaoPagina);
		if (tempList.size() > 0) {
			request.setAttribute("ListaProcessoStatus", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
			request.setAttribute("QuantidadePaginas", processone.getQuantidadePaginas());
			request.setAttribute("tempRetorno", "BuscaProcessoUsuarioExterno?TipoConsulta=" + tipoConsulta + "&PassoBusca=" + passoBusca);
			request.setAttribute("tempBuscaId_ProcessoStatus", "Id_ProcessoStatus");
			request.setAttribute("tempBuscaProcessoStatus", "ProcessoStatus");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Dados N�o Localizados");
		return boRetorno;
	}

	/**
	 * Limpa dados para uma nova consulta
	 */
	protected void limparDados(BuscaProcessoDt processoDt) {
		processoDt.limpar();
		processoDt.setProcessoStatus("Ativo");
		processoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
	}

	/**
	 * M�todo respons�vel em consultar a situa��o completa do processo.
	 * Retorna dados referentes a pend�ncias, conclus�es e audi�ncias em aberto no processo.
	 * 
	 * @param request
	 * @param id_processo
	 * @param processoNe
	 * @throws Exception
	 */
	protected void consultarSituacaoProcesso(HttpServletRequest request, String id_processo, ProcessoNe processoNe, UsuarioDt usuarioDt) throws Exception{

		//Consultar pend�ncias em aberto ou n�o vistadas
		List[] listaPendencias = processoNe.consultarPendenciasProcesso(id_processo, false, usuarioDt);
		request.setAttribute("ListaPendencias", listaPendencias);

		//Consultar conclus�es em aberto
		List conclusoesPendentes = processoNe.consultarConclusoesPendentesProcessoPublico(id_processo, false);
		request.setAttribute("ConclusaoPendente", conclusoesPendentes);

		//Consultar audi�ncias em aberto
		List<String[]> audienciaPendente = processoNe.consultarAudienciasPendentes(id_processo, false);
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

		List listaAdvogados = processoNe.consultarAdvogadosProcessoPublico(processoDt.getId());
		request.setAttribute("ListaAdvogados", listaAdvogados);
	}
	
	protected final String idCodigoHashEOutros = "CodigoHashEOutros";
	/**
	 * Armazena o c�digo Rash gerado para valida��o futura durante o acesso ao link e outros, evitando uso de rob�
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected void armazeneCodigoRashEOutros(HttpServletRequest request, UsuarioNe usuarioNe, String idProcesso) throws Exception{
		request.setAttribute(idCodigoHashEOutros, usuarioNe.getCodigoHash(idProcesso));		
			
	}
	
	/**
	 * Valida o c�digo Rash gerado para acesso ao link e outros, evitando uso de rob�
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected boolean validaCodigoRashEOutros(HttpServletRequest request, UsuarioNe usuarioNe, String idProcesso) throws Exception{
		String codigoRashGerado = (String) request.getParameter(idCodigoHashEOutros);
		
		if (codigoRashGerado == null || codigoRashGerado.trim().equalsIgnoreCase("")) return false;
		
		return usuarioNe.VerificarCodigoHash(idProcesso, codigoRashGerado);
	}
	
	protected boolean ehAcessoOutraServentia(HttpServletRequest request, String idProcesso)
	{
		boolean acessoOutraServentia = false;
		String processoOutraServentia = null;
		//Captura se trata de um Processo onde o acesso de outra serventia deve ser liberado, como acontece com as Cartas Precat�rios
		if (request.getSession().getAttribute("AcessoOutraServentia") != null && !request.getSession().getAttribute("AcessoOutraServentia").equals("")) {
			if (request.getSession().getAttribute("ProcessoOutraServentia") != null) processoOutraServentia = (String) request.getSession().getAttribute("ProcessoOutraServentia");
			//Se processo que foi consultado � o mesmo com acesso externo liberado, deve liberar download dos arquivos
			if (processoOutraServentia.equalsIgnoreCase(idProcesso)) acessoOutraServentia = true;
		}
		
		return acessoOutraServentia;
	}

	//este m�todo deve ser sobrescrito pelos ct_publicos
	//retornando o id do grupo publico
	protected String getId_GrupoPublico() {		
		return GrupoDt.ID_GRUPO_PUBLICO;
	}
	
}
