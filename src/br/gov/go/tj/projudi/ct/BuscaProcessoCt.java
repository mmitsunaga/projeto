package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.BuscaProcessoDt;
import br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ListaPendenciaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PermissaoEspecialDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RedistribuicaoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.VotanteDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteAdvogadoNe;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet responsável pela Busca de Processos.
 * Essa busca será usada pelos usuários internos (Analista, Técnicos, Juizes, Conciliadores...) e a função Editar será
 * utilizada para que todos os usuários tenham acesso aos dados do processo através dos links existentes em várias jsp's
 * 
 * @author msapaula
 * 
 */
public class BuscaProcessoCt extends Controle {

	private static final long serialVersionUID = -8999834724334616406L;
	public static final int CodigoPermissao = 152;

	public int Permissao() {
		return BuscaProcessoCt.CodigoPermissao;
	}

	public BuscaProcessoCt() {
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {
		
		BuscaProcessoDt buscaProcessoDt = null;
		ProcessoDt processoDt = null;
		ProcessoNe Processone;

		String stTemp;
		List tempList = null;
		String stId = "";
		int passoBusca = 0;
		int paginaAnterior = 0;
		int passoEditar = -1;
		//variavel para tratar consulta de processos por desembargador - consultar todos (0) consultar em que ele é responsável (1)
		int proprios = 0;
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		boolean boSegredoJustica = false;
		
		List listaAdvogados = new ArrayList();

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
				
		String stAcao;

		request.setAttribute("tempPrograma", "Processo");
		request.setAttribute("tempRetorno", "BuscaProcesso");
		request.setAttribute("TituloPagina", "Consulta Todos Processos");
		request.setAttribute("tempRetornoProcesso", "BuscaProcesso");
		stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisa.jsp";
		
		//********************************************************************************************************************
		if (request.getParameter("Proprios") == null ){
			if (request.getSession().getAttribute("Proprios") != null){				
				proprios = Funcoes.StringToInt(request.getSession().getAttribute("Proprios").toString());
			}
		
		} else if (request.getParameter("Proprios").toString().equals("1")){
			proprios = Funcoes.StringToInt(request.getParameter("Proprios"));
		} else {
			proprios = 0;
		}
		//*********************************************************************************************************************

		Processone = (ProcessoNe) request.getSession().getAttribute("BuscaProcessone");
		if (Processone == null) Processone = new ProcessoNe();
							
		buscaProcessoDt = (BuscaProcessoDt) request.getSession().getAttribute("buscaProcessoDt");
		if (buscaProcessoDt == null) {
			buscaProcessoDt = new BuscaProcessoDt();								
		}
		atribuiRequest(request, buscaProcessoDt);
		
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt == null) processoDt = new ProcessoDt();

		//Se o tipoConsultaProcesso deve-se setá-lo na sessão para que ele não se perca após o submit da tela jsp.
		if (request.getParameter("tipoConsultaProcesso") != null) {
			request.getSession().setAttribute("tipoConsultaProcesso", request.getParameter("tipoConsultaProcesso"));
			request.removeAttribute("tipoConsultaProcesso");
		}
				
		
		request.setAttribute("PaginaAnterior", paginaatual);
		
		if (request.getParameter("outrasServentias") != null){
			request.setAttribute("outrasServentias", request.getParameter("outrasServentias"));
		}

		// Variável PassoBusca utilizada para auxiliar na busca
		// passoBusca 1 : Significa que busca já foi realizada e que provavelmente está se usando paginação, não devendo assim limpar dados da consulta
		// passoBusca 2 : Significa que solicitação de consulta veio da página inicial
		// passoBusca 3 : Significa que solicitação de consulta veio da página inicial e são processos com possível prescrição
		if (request.getParameter("PassoBusca") != null) passoBusca = Funcoes.StringToInt(request.getParameter("PassoBusca"));
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		
		if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) != GrupoDt.ADVOGADO_PARTICULAR && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) != GrupoDt.PARTES && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) != GrupoDt.POPULACAO) {
			request.setAttribute("CarregarAppletEtiqueta", "true");
		}
		
		ServentiaDt serventiaDt = Processone.consultarIdServentia(processoDt.getId_Serventia());
		//analisando esse ponto....
		if (serventiaDt != null && (serventiaDt.isSegundoGrau() || serventiaDt.isUpjTurmaRecursal() )) {
			request.setAttribute("processoSegundoGrau", "true");
		}

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

		switch (paginaatual) {

			// Sempre q tratar de uma nova consulta deve limpar Dados e trazer Serventia atual selecionada
			case Configuracao.Novo:{
				buscaProcessoDt.limpar();
				Processone.limparVariaveisConsulta();				
				buscaProcessoDt.setProcessoStatus("Ativo");
				buscaProcessoDt.setProcessoStatusCodigo(String.valueOf(ProcessoStatusDt.ATIVO));
				
				//Sempre que consultar um processo vamos limpar os objetos da sessão
				LimparSessao(request.getSession());
				
				request.getSession().removeAttribute("Proprios");
				if (!UsuarioSessao.isUsuarioGabinete() && proprios==1 ){
					buscaProcessoDt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
					buscaProcessoDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
				}
				if (request.getSession().getAttribute("tipoConsultaProcesso") != null) {
					request.getSession().removeAttribute("tipoConsultaProcesso");
				}
				int  tipoConsulta = Funcoes.StringToInt(request.getParameter("tipoConsultaProcesso"),-10);
				stAcao = definirTelaAbertura(tipoConsulta);
				break;

			// Consulta processos de acordo com parâmetros
			}case Configuracao.Localizar:
				
				if (passoBusca == BuscaProcessoDt.CONSULTA_USUARIO_INTERNO) {
					// Se veio da pagina inicial irá consultar apenas os ativos da serventia do usuario
					Processone.limparVariaveisConsulta();
					//buscaProcessoDt.limpar();
					if (!UsuarioSessao.isUsuarioGabinete() ){
						buscaProcessoDt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
					}
					proprios = 1;					
				} 							
				
				//Se o tipoConsulta for igual a 1, é para realizar a consulta baseando-se nos dados do Advogado, informados na tela ProcessoPesquisaDadosAdvogado. 
				//Se o tipoConsulta for igual a 2, é para realizar consulta de processos por juiz.
				//Se o tipoConsulta for igual a 3, é para realizar consulta de processos por defensor público/procurador/advogado escritorio juridico.
				//Se o tipoConsulta for igual a 4, é para realizar consulta de processos para o perfil de coodenador de promotoria.
				//Se o tipoConsulta for igual a 5, é para realizar consulta de processos que serão classificados.
				//Caso contrário, realizar consultar normal da tela ProcessoPesquisa.

				//String tipoConsulta = String.valueOf(request.getSession().getAttribute("tipoConsultaProcesso"));
				int  tipoConsulta = Funcoes.StringToInt((String)request.getSession().getAttribute("tipoConsultaProcesso"),-10);
				
				if ( tipoConsulta==BuscaProcessoDt.CONSULTA_PROCESSOS_ADVOGADO) {
					tempList = buscarProcessoAdvogado(request, Processone, PosicaoPaginaAtual, UsuarioSessao, buscaProcessoDt);		
				
				} else if ( tipoConsulta == 2) {		
					tempList = buscarProcessoJuiz(request, Processone, UsuarioSessao, PosicaoPaginaAtual, buscaProcessoDt);										
				
				} else if ( tipoConsulta == 3) {
					tempList = buscarProcessoAdvogadoPublicosEscritorio(request, Processone, PosicaoPaginaAtual, UsuarioSessao, buscaProcessoDt);						
				
				} else if ( tipoConsulta== 4) {
					consultaPendenciasMP(request, UsuarioSessao, Processone);					
					break;
				
				} else if ( tipoConsulta==BuscaProcessoDt.CONSULTA_PROCESSOS_CLASSIFICAR) {					
					tempList = buscarProcessoClassificar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual, Processone, buscaProcessoDt);
				
				} else if ( tipoConsulta==BuscaProcessoDt.CONSULTA_PROCESSOS_ARQUIVADOS_SEM_MOTIVO) {					
					tempList = buscarProcessoArquivadosSemMotivo(request,  UsuarioSessao, PosicaoPaginaAtual, Processone);
				
				} else if ( tipoConsulta==BuscaProcessoDt.CONSULTA_PROCESSOS_INCONSISTENCIA_POLO_PASSIVO) {
					tempList = buscarProcessoInconsistenciaPoloPassivo(request,  UsuarioSessao, PosicaoPaginaAtual, Processone);
					
				} else if ( tipoConsulta == BuscaProcessoDt.CONSULTA_PROCESSOS_PRISAO_FORA_PRAZO) {
					tempList = buscarProcessosPrisaoForaPrazo(request,  UsuarioSessao, PosicaoPaginaAtual, Processone);	
				
				} else if ( tipoConsulta==  BuscaProcessoDt.CONSULTA_PROCESSOS_PRESCRITOS) {
					tempList = Processone.consultarProcessosPossivelPrescricao(UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getId_Comarca(), UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
				
				} else if ( tipoConsulta== BuscaProcessoDt.CONSULTA_PROCESSOS_SEM_ASSUNTO) {
					tempList = buscarProcessosSemAssunto(request,  UsuarioSessao, PosicaoPaginaAtual, Processone);	

				} else if ( tipoConsulta== BuscaProcessoDt.CONSULTA_PROCESSOS_COM_ASSUNTO_PAI) {
					tempList = buscarProcessosComAssuntoPai(request,  UsuarioSessao, PosicaoPaginaAtual, Processone);	

				} else if ( tipoConsulta== BuscaProcessoDt.CONSULTA_PROCESSOS_COM_CLASSE_PAI) {
					tempList = buscarProcessosComClassePai(request,  UsuarioSessao, PosicaoPaginaAtual, Processone);	
					
				} else if ( tipoConsulta== BuscaProcessoDt.CONSULTA_PROCESSOS_INQUERITO) {
					tempList = buscarProcessoInquerito(request, Processone, PosicaoPaginaAtual, UsuarioSessao, buscaProcessoDt);	
				} else {
					tempList = buscarProcessoGeral(request, Processone, PosicaoPaginaAtual, UsuarioSessao, buscaProcessoDt,proprios, passoBusca);					
				}

				if (tempList == null || tempList.size() == 0) {
					request.setAttribute("MensagemErro", "Nenhum Processo foi localizado.");
					stAcao = definirTelaAbertura(tipoConsulta);
				}else {						
					if (request.getParameter("ProcessoNumero") != null && request.getParameter("ProcessoNumero").trim().length() > 0 && tempList.size() == 1) {
						stId = ((ProcessoDt)tempList.get(0)).getId();
						//Define código hash para acesso ao link "e outros"
						armazeneCodigoHashEOutros(request,UsuarioSessao,stId);

						//Verifica se é usuário externo para redirecionar para servlet com Captcha
						if (UsuarioSessao.isUsuarioExterno()) {
							// Define um possível complemento para a requisição, em caso de mensagem de sucesso ou erro
							String complementoRequest = "";
							if (request.getParameter("MensagemOk") != null) {
								complementoRequest = "MensagemOk=" + request.getParameter("MensagemOk");
							}else if (request.getParameter("MensagemErro") != null) {
								complementoRequest = "MensagemErro=" + request.getParameter("MensagemErro");
							}
							if (complementoRequest.length() > 0) {
								redireciona(response, "BuscaProcessoUsuarioExterno?Id_Processo=" + stId + "&PassoBusca=2&" + complementoRequest);
							} else { 
								redireciona(response, "BuscaProcessoUsuarioExterno?Id_Processo=" + stId + "&PassoBusca=2");							
							}
							return;
						} else {
							boSegredoJustica = this.consultarDadosProcesso(stId,  Processone, request, UsuarioSessao);	
						}
						stAcao = this.definirPaginaProcesso(boSegredoJustica);
						
					} else {
						request.setAttribute("ListaProcesso", tempList);
						request.setAttribute("PaginaAtual", Configuracao.Localizar);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Processone.getQuantidadePaginas());
						request.setAttribute("tipoConsultaProcesso", tipoConsulta);
						
						//buscaProcessoDt.limpar();
						//se a busca for de processos com possível prescrição, não há necessidade de mudar o passoBusca
						if(passoBusca != 3 ){
							passoBusca = 1;
						}	
						stAcao = this.definirAcaoRetorno(proprios, tipoConsulta);
																	
					}
				} 
				
				montarOpcoesLote(request, UsuarioSessao);	
				break;


			case Configuracao.LocalizarDWR: 
				String stringBusca1 = "";
            	String stringBusca2 = "";
            	int inFluxo = 0;
				if(request.getParameter("fluxo") != null){
					inFluxo = Funcoes.StringToInt( request.getParameter("fluxo").toString()); 
				 }
                 switch (inFluxo) {
                 case 1:
                	 String tempBusca = "";
                	 String Id_Relator = buscaProcessoDt.getId_Relator();
                	 if (StringUtils.isEmpty(Id_Relator)){
                		 Id_Relator = buscaProcessoDt.getId_ServentiaCargo();
                	 }
                	 tempBusca = Processone.consultarProcessosJuizClassificadorJSON( UsuarioSessao.getUsuarioDt(),buscaProcessoDt.getId_Classificador(), Id_Relator,buscaProcessoDt.getId_ProcessoTipo(), buscaProcessoDt.getId_Assunto() , PosicaoPaginaAtual);                		 
               		 enviarJSON(response, tempBusca);				
                     break;
                 default:
                     break;
              }
              return;
				
				
			// Efetua download de arquivos
			case Configuracao.Curinga6:
				String id_MovimentacaoArquivo = request.getParameter("Id_MovimentacaoArquivo");
				boolean acessoOutraServentia = false;
				String processoOutraServentia = null;
				//Captura se trata de um Processo onde o acesso de outra serventia deve ser liberado, como acontece com as Cartas Precatórios, Liberação de Acesso
				if (request.getSession().getAttribute("AcessoOutraServentia") != null && !request.getSession().getAttribute("AcessoOutraServentia").equals("")) {
					if (request.getSession().getAttribute("ProcessoOutraServentia") != null) processoOutraServentia = (String) request.getSession().getAttribute("ProcessoOutraServentia");
					//Se processo que foi consultado é o mesmo com acesso externo liberado, deve liberar download dos arquivos
					if (processoOutraServentia.equalsIgnoreCase(processoDt.getId())) acessoOutraServentia = true;
				}

				if (request.getParameter("eCarta") != null && request.getParameter("eCarta").equals("true")){
					InputStream template = request.getServletContext().getResourceAsStream("/imagens/envelopeCarta.pdf");
					byte[] byModelo = Processone.baixarArquivoECarta(id_MovimentacaoArquivo, processoDt, UsuarioSessao.getUsuarioDt(), response, template, new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()));
					enviarPDF(response, byModelo, "e-Carta");
					return;
				} else if (UsuarioSessao.VerificarCodigoHash(id_MovimentacaoArquivo+processoDt.getId_Processo(), request.getParameter("hash"))) {
					boolean recibo = false;
					if (request.getParameter("recibo") != null && request.getParameter("recibo").equals("true")) recibo = true;
					if (request.getParameter("CodigoVerificacao") != null && request.getParameter("CodigoVerificacao").equals("true")){
						if (request.getParameter("expedirImprimir") != null && request.getParameter("expedirImprimir").equals("true")){
							request.setAttribute("tempRetorno", "BuscaProcesso");
						}
						String stIdArquivo = Processone.consultarIdArquivo(id_MovimentacaoArquivo); 
						if (stIdArquivo != null && stIdArquivo.length() > 0) {
							if (!stIdArquivo.equalsIgnoreCase("")) {															
								// gerar pdf como arquivos da publicação
								byte[] arquivoPDF = Processone.gerarPdfPublicacao(ProjudiPropriedades.getInstance().getCaminhoAplicacao() ,stIdArquivo, id_MovimentacaoArquivo, processoDt,  UsuarioSessao.getUsuarioDt(), new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()));
								enviarPDF(response, arquivoPDF, "relatorio");
								return;
							}
						} else {
							request.setAttribute("Mensagem", "Erro ao efetuar consulta do arquivo.");
							RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Erro.jsp");
							dis.include(request, response);
							return;
						}
					} else {
						if (!Processone.baixarArquivoMovimentacao(id_MovimentacaoArquivo, processoDt, UsuarioSessao.getUsuarioDt(), recibo, response, new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()), false)){
							//se baixou mandar para a jsp de não permitido baixar o arquivo
							stAcao = "/WEB-INF/jsptjgo/Padroes/ArquivoNaoPermitido.jsp";
							RequestDispatcher dis = request.getRequestDispatcher(stAcao);
							dis.include(request, response);
						}
					}
						
				} else throw new MensagemException("Acesso negado.");
				return;

				//Consulta de Processos por Classificador
			case Configuracao.Curinga7:
							
				if(request.getParameter("Id_ServentiaCargo") == null) {
					buscaProcessoDt.setId_ServentiaCargo("");
				}else{
					buscaProcessoDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));	
				}
				
				if(request.getParameter("ServentiaCargo") == null) {
					buscaProcessoDt.setServentiaCargo("");
				}else{
					buscaProcessoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));	
				}
				
				if(request.getParameter("ServentiaCargoUsuario") == null) {
					buscaProcessoDt.setServentiaCargoUsuario("");
				}else{
					buscaProcessoDt.setServentiaCargoUsuario(request.getParameter("ServentiaCargoUsuario"));	
				}
				
				montarOpcoesLote(request, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/ProcessoClassificadorLocalizar.jsp";
				break;

			//Consulta de Processos por Relator
			case Configuracao.Curinga8:

				if (buscaProcessoDt.temRelator()) {
					tempList = Processone.consultarProcessosUsuarioRelator(buscaProcessoDt.getId_Relator(), UsuarioSessao.getUsuarioDt().getId_Serventia(), buscaProcessoDt.getId_ProcessoTipo());
					if (tempList.size() > 0) {
						request.setAttribute("ListaProcessos", tempList);
					} else {
						request.setAttribute("MensagemErro", "Não foram localizados processos para o relator selecionado.");
					}
				}
				montarOpcoesLote(request, UsuarioSessao);
				stAcao = "/WEB-INF/jsptjgo/ProcessoRelatorLocalizar.jsp";
				
				break;

			//Visualizar Situação do Processo
			case Configuracao.Imprimir:
				this.consultarSituacaoProcesso(request, UsuarioSessao,  Processone, serventiaDt,response);
				//Se usuário não é da serventia do processo não mostra JSP com opção de resolver pendências
				if ((Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE)){ 
					stAcao = "/WEB-INF/jsptjgo/ProcessoSituacaoPendenciasDistribuidorGabinete.jsp";
				} else if ((Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_CAMARA)){ 
						stAcao = "/WEB-INF/jsptjgo/ProcessoSituacaoPendenciasDistribuidorCamara.jsp";
				} else if (UsuarioSessao.isAssistenteGabineteComFluxo()){
					stAcao = "/WEB-INF/jsptjgo/ProcessoSituacao.jsp";
				}else if (!processoDt.isMesmaServentia(UsuarioSessao.getId_Serventia())) {				
					stAcao = "/WEB-INF/jsptjgo/ProcessoSituacao.jsp";
				} else 
					stAcao = "/WEB-INF/jsptjgo/ProcessoSituacaoPendencias.jsp";
				break;

			//Consultar Responsáveis pelo Processo
			case Configuracao.LocalizarAutoPai:
				this.consultarResponsaveisProcesso(request,  UsuarioSessao, Processone);
				stAcao = "/WEB-INF/jsptjgo/ProcessoResponsaveis.jsp";
				break;

			// Consulta de status possíveis
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
					request.setAttribute("tempRetorno","BuscaProcesso");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					stTemp="";
					stTemp = Processone.consultarDescricaoProcessoStatusJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
				break;
				
			// Consulta de processo tipo
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"ProcessoTipo"};
					String[] lisDescricao = {"Tipo de Processo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPermissao = String.valueOf((ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					if (paginaAnterior == Configuracao.Curinga8){
						atribuirJSON(request, "Id_ProcessoTipo", "ProcessoTipo", "ProcessoTipo", "BuscaProcesso", Configuracao.Curinga8, stPermissao, lisNomeBusca, lisDescricao);
					}else if (paginaAnterior == Configuracao.Curinga7){
						atribuirJSON(request, "Id_ProcessoTipo", "ProcessoTipo", "ProcessoTipo", "BuscaProcesso", Configuracao.Curinga7 , stPermissao, lisNomeBusca, lisDescricao);
					} else {
						atribuirJSON(request, "Id_ProcessoTipo", "ProcessoTipo", "ProcessoTipo", "BuscaProcesso", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
					}
				} else {
					String stTemp1 = "";
					stTemp1 = Processone.consultarDescricaoProcessoTipoJSON(stNomeBusca1,PosicaoPaginaAtual);
					enviarJSON(response, stTemp1);
					return;
				}
				break;
				
			// Consulta de Assunto
			case (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Assunto", "Código CNJ"};
					String[] lisDescricao = {"Assunto", "Código CNJ","Dispositivo","Código"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPermissao = String.valueOf((AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					if (paginaAnterior == Configuracao.Curinga8){
						atribuirJSON(request, "Id_Assunto", "Assunto", "Assunto", "BuscaProcesso", Configuracao.Curinga8, stPermissao, lisNomeBusca, lisDescricao);
					}else if (paginaAnterior == Configuracao.Curinga7){
						atribuirJSON(request, "Id_Assunto", "Assunto", "Assunto", "BuscaProcesso", Configuracao.Curinga7 , stPermissao, lisNomeBusca, lisDescricao);
					} else {
						atribuirJSON(request, "Id_Assunto", "Assunto", "Assunto", "BuscaProcesso", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
					}
				} else {
					stTemp = Processone.consultarDescricaoAssuntoJSON(stNomeBusca1, stNomeBusca2, serventiaDt.getId(), PosicaoPaginaAtual);
					enviarJSON(response, stTemp);	
					return;
				}
			break;
			// Consulta serventias disponíveis
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serventia");
					request.setAttribute("tempBuscaPrograma","Serventia");			
					request.setAttribute("tempRetorno","BuscaProcesso");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					stTemp="";
					stTemp = Processone.consultarServentiaJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt());
					enviarJSON(response, stTemp);
					return;								
				}
				break;

			// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					//Variável outrasServentias será usado para consultar classificador
					request.setAttribute("tempFluxo1", request.getParameter("outrasServentias"));
					
					String stPermissao = String.valueOf((ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					if (paginaAnterior == Configuracao.Curinga7){
						atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", "BuscaProcesso", Configuracao.Curinga7 , stPermissao, lisNomeBusca, lisDescricao);
					} else {
						atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", "BuscaProcesso", Configuracao.Editar , stPermissao, lisNomeBusca, lisDescricao);
					}
					
					break;
				} else{
					String stTemp1="";
					//Se o tempFluxo1 for true, vai buscar classificador de outras serventias
					if(request.getParameter("tempFluxo1") != null && request.getParameter("tempFluxo1").equalsIgnoreCase("true")){
						stTemp1 = Processone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, "");
					} else {
						stTemp1 = Processone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getId_Serventia());						
					}
					enviarJSON(response, stTemp1);											
					return;								
				}
			
			// Consultar relatores ou juizes responsáveis pelo processo
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if((request.getParameter("ConsultaResponsavelPrimeiroGrau") != null && request.getParameter("ConsultaResponsavelPrimeiroGrau").equals("1")) || ((request.getParameter("tempFluxo1") != null) && (request.getParameter("tempFluxo1").equalsIgnoreCase("1")))){
					ProcessoNe processoNe = new ProcessoNe();
					
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"ServentiaCargo"};
						String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
						String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
						request.setAttribute("camposHidden",camposHidden);
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
						request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
						request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
						request.setAttribute("tempRetorno", "BuscaProcesso");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga7);
						request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						
						request.setAttribute("tempFluxo1", "1");
					}else{
						stTemp = "";
						stTemp = Processone.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
						enviarJSON(response, stTemp);
						return;
					}
					break;
				} else {
					ProcessoResponsavelNe ProcessoResponsavelne = new ProcessoResponsavelNe();
					
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"ServentiaCargo"};
						String[] lisDescricao = {"ServentiaCargo [Serventia]", "Usuário", "CargoTipo"};
						String[] camposHidden = {"Relator", "CargoTipo"};
						request.setAttribute("camposHidden",camposHidden);
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_Relator");
						request.setAttribute("tempBuscaDescricao", "ServentiaCargoUsuario");
						request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
						request.setAttribute("tempRetorno", "BuscaProcesso");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga8);
						request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					}else{
						stTemp = "";
						stTemp = ProcessoResponsavelne.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia(), String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
						enviarJSON(response, stTemp);
						return;
					}
				}
				break;
				
				// Consultar advogados/denfensores/procuradores responsáveis
				case (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
					ProcessoResponsavelNe ProcessoResponsavelne = new ProcessoResponsavelNe();
					if (request.getParameter("Passo") == null) {
						String[] lisNomeBusca = { "Nome Usuário" };
						String[] lisDescricao = { "Usuário", "Serventia"};
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_UsuarioServentia");
						request.setAttribute("tempBuscaDescricao", "UsuarioServentia");
						request.setAttribute("tempBuscaPrograma", "Defensor Público/Procurador/Advogado ");
						request.setAttribute("tempRetorno", "BuscaProcesso");
						request.setAttribute("tempPaginaAtualJSON", "-1");
						request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
						stTemp = "";
						stTemp = ProcessoResponsavelne.consultarUsuariosServentiaJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
						enviarJSON(response, stTemp);
						return;
					}
				break;				
					
			case Configuracao.Curinga9:
				stTemp = "";
				
				switch (passoBusca) {
					case 3:
						//Passo busca == 3 lista os advogados do processo para segredo de justiça
						listaAdvogados = new ProcessoParteAdvogadoNe().consultarAdvogadosProcesso(processoDt.getId_Processo());
						stAcao = "/WEB-INF/jsptjgo/DadosProcessoSegredoJustica.jsp";
						break;
					case 4:
						stAcao = "/WEB-INF/jsptjgo/NavegacaoArquivos.jsp";
						break;
					case 5:
						stTemp = Processone.consultarArquivosJSON(processoDt.getId(), UsuarioSessao);
						enviarJSON(response, stTemp);
						return;									
				}							
				break;
			// Função editar possibilita a exibição de dados do processo. Será utilizada por todos usuarios (internos e externos)
			default:
				
				// Função Editar é dividida da seguinte forma:
				// Passo  1: lista os eventos da execução penal.
				// Passo  6: visualiza último atestado de pena a cumprir. 
				// passo  8: consulta dados da ação penal, na lista de eventos
				// Passo 11: Redireciona para jsp de consulta todas as partes
				
				switch (passoEditar) {

				//lista os eventos da execução penal.
				case 1:
					CalculoLiquidacaoDt calculoLiquidacaoDt = new CalculoLiquidacaoDt();
					List listaCondenacaoExtinta = new ArrayList();
					HashMap map = Processone.montarListaEventosCompleta(processoDt.getId_Processo(), UsuarioSessao.getUsuarioDt(), calculoLiquidacaoDt, listaCondenacaoExtinta);
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
					ultimoCalculo = Processone.consultarCalculoLiquidacao(processoDt.getId_Processo());
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
						calculo = Processone.consultarCalculoLiquidacao(processoDt.getId_Processo());
						request.getSession().setAttribute("ultimoCalculo", calculo);
						stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
					}
					
					byte[] byTemp = Processone.visulizarAtestadoPenaCumprir(calculo, ProjudiPropriedades.getInstance().getCaminhoAplicacao() );
					
					if (byTemp != null){
						
				        enviarPDF(response, byTemp,"Relatorio");
													
						byTemp = null;
						return;
					} else {
						this.consultarDadosProcesso(stId,  Processone, request, UsuarioSessao);
						request.setAttribute("MensagemErro", "Atestado de Pena a cumprir não localizado");
					}
					
					request.setAttribute("tempRetorno", "BuscaProcesso");
					break;
					
					//consulta dados da ação penal, na lista de eventos --mesmo passo editar do BuscaProcessoPublica, BuscaProcesso, BuscaProcessoUsuarioExterno
				case 8: 
					stTemp = "";
					String idProcessoExecucao = "";
					if(request.getParameter("idProcessoExecucao") != null) idProcessoExecucao = request.getParameter("idProcessoExecucao");
					request.setAttribute("tempRetorno", "BuscaProcesso");
					stTemp = Processone.consultarIdAcaoPenalJSON(idProcessoExecucao);
						
					enviarJSON(response, stTemp);
											
					return;					
				case 11:
					if (validaCodigoHashEOutros(request,UsuarioSessao, processoDt.getId_Processo())){					
						request.setAttribute("tempRetorno", "BuscaProcesso?Id_Processo=" + processoDt.getId_Processo() + "&atualiza=true");					
						stAcao = "/WEB-INF/jsptjgo/BuscaTodasPartesProcesso.jsp";
					}else {
						throw new MensagemException("Acesso negado. Não foi possível exibir os dados das Partes.");						
					}					
					break;				
				default:
					if(request.getParameter("expedirImprimir") != null && request.getParameter("expedirImprimir").equals("true")){
						request.setAttribute("expedirImprimir", "true");
					}
					
					if(request.getParameter("expedirImprimirSolicitarCargaProcesso") != null && request.getParameter("expedirImprimirSolicitarCargaProcesso").equals("true")){
						request.setAttribute("expedirImprimirSolicitarCargaProcesso", "true");
					}					
					
					stId = request.getParameter("Id_Processo");

					int tipoBusca = Funcoes.StringToInt((String) request.getSession().getAttribute("tipoConsultaProcesso"),-10);
					// Se foi passado Id_Processo efetua consulta e redireciona para tela de Dados do Processo
					if (stId != null && !stId.isEmpty()) {
						
						//Define código hash para acesso ao link "e outros"
						armazeneCodigoHashEOutros(request,UsuarioSessao,stId);

//						//Verifica se é usuário externo para redirecionar para servlet com Captcha
//						if (UsuarioSessao.isUsuarioExterno()) {
//							// Define um possível complemento para a requisição, em caso de mensagem de sucesso ou erro
//							String complementoRequest = "";
//							if (request.getParameter("MensagemOk") != null){
//								complementoRequest = "MensagemOk=" + request.getParameter("MensagemOk");
//							} else if (request.getParameter("MensagemErro") != null) {
//								complementoRequest = "MensagemErro=" + request.getParameter("MensagemErro");
//							}
//							
//							if (complementoRequest.length() > 0) {
//								redireciona(response, "BuscaProcessoUsuarioExterno?Id_Processo=" + stId + "&PassoBusca=2&" + complementoRequest);
//							} else { 
//								redireciona(response, "BuscaProcessoUsuarioExterno?Id_Processo=" + stId + "&PassoBusca=2");							
//							}
//							return;
//						} else {
						//se o processo ja está na session usa-se ele mesmo, não se faz nova consulta
						if (stId.equalsIgnoreCase("") && stId.equalsIgnoreCase(processoDt.getId_Processo())) {						
							boSegredoJustica = processoDt.isSegredoJustica();
						}else {
							boSegredoJustica = this.consultarDadosProcesso(stId, Processone, request, UsuarioSessao);
						}
						
						stAcao = definirPaginaProcesso(boSegredoJustica);

					} else {
						
						if (tipoBusca !=-10){
							buscaProcessoDt.limpar();
							atribuiRequest(request, buscaProcessoDt);
							buscaProcessoDt.setId_Serventia(UsuarioSessao.getUsuarioDt().getId_Serventia());
							buscaProcessoDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
							
							if (request.getAttribute("MensagemOk") != null && request.getAttribute("MensagemOk").toString().length()>0){
								request.setAttribute("MensagemOk", request.getAttribute("MensagemOk"));
							}
							
							if ( tipoBusca==4) {
								consultaPendenciasMP(request, UsuarioSessao, Processone);	
							}							
						}
						stAcao = definirTelaAbertura(tipoBusca);
					}
					break;
			}			
				
		}

		request.setAttribute("PassoBusca", passoBusca);
		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("PosicaoPagina", (Funcoes.StringToInt(PosicaoPaginaAtual) + 1));
		request.setAttribute("listaAdvogados", listaAdvogados);		
		request.getSession().setAttribute("Proprios", proprios); 
		request.getSession().setAttribute("buscaProcessoDt", buscaProcessoDt);
		request.getSession().setAttribute("BuscaProcessone", Processone);
		
		//Se o usuário for ESTAGIÁRIO não pode mostrar o botão assinar
		if(UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.ESTAGIARIO))){
			request.setAttribute("ocultarBotoesEstagiario", true);
		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	protected void consultaPendenciasMP(HttpServletRequest request, UsuarioNe UsuarioSessao, ProcessoNe Processone)	throws Exception {
		PendenciaResponsavelNe pendenciaResponsavelne = new PendenciaResponsavelNe();
		
		// Consulta as intimações da serventia
		List pendencias = Processone.consultarIntimacoesPromotoria(UsuarioSessao);
		
		ListaPendenciaDt listaIntimacoes = new ListaPendenciaDt();
		listaIntimacoes.setTitulo("Intimações da Serventia Aguardando Leitura");
		
		ListaPendenciaDt listaIntimacoesSubstitutoProcessual = new ListaPendenciaDt();
		listaIntimacoesSubstitutoProcessual.setTitulo("Intimações da Serventia (Substituto Processual) Aguardando Leitura");
		
		if (pendencias != null) {
			List intimacoes = new ArrayList();
			List intimacoesSubstitutoProcessual = new ArrayList();
			
			for (int i = 0; i < pendencias.size(); i++) {
				PendenciaDt dt = (PendenciaDt) pendencias.get(i);
				
				List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhado(dt.getId(), UsuarioSessao.getUsuarioDt().getGrupoCodigo());							
				if (listaResponsavelPendencia != null && listaResponsavelPendencia.size()>0){
					dt.addResponsavel((PendenciaResponsavelDt)listaResponsavelPendencia.get(0));								
				}
				
				if (dt.getNomeParte() != null && dt.getNomeParte().length()>0)
					intimacoesSubstitutoProcessual.add(dt);
				else						
					intimacoes.add(dt);
			}

			if (intimacoes.size() > 0) {							
				listaIntimacoes.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
				listaIntimacoes.setPendenciasAndamento(intimacoes);		
				listaIntimacoes.setQuantidadePendencias(intimacoes.size());
			}
			
			if (intimacoesSubstitutoProcessual.size() > 0) {							
				listaIntimacoesSubstitutoProcessual.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
				listaIntimacoesSubstitutoProcessual.setPendenciasAndamento(intimacoesSubstitutoProcessual);
				listaIntimacoesSubstitutoProcessual.setQuantidadePendencias(intimacoesSubstitutoProcessual.size());
			}
			
		}
		
		// Consulta as intimações lidas e dentro do prazo da serventia
		List pendenciasLidas = Processone.consultarIntimacoesLidasDistribuicaoPromotoria(UsuarioSessao);
		
		ListaPendenciaDt listaIntimacoesLidas = new ListaPendenciaDt();
		listaIntimacoesLidas.setTitulo("Intimações da Serventia Lidas e Dentro do Prazo de Cumprimento");
		
		ListaPendenciaDt listaIntimacoesLidasSubstitutoProcessual = new ListaPendenciaDt();
		listaIntimacoesLidasSubstitutoProcessual.setTitulo("Intimações da Serventia (Substituto Processual) Lidas e Dentro do Prazo de Cumprimento");
		
		if (pendenciasLidas != null) {
			List intimacoesLidas = new ArrayList();
			List intimacoesLidasSubstitutoProcessual = new ArrayList();
			
			for (int i = 0; i < pendenciasLidas.size(); i++) {
				PendenciaDt dt = (PendenciaDt) pendenciasLidas.get(i);
				
				List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhado(dt.getId(), UsuarioSessao.getUsuarioDt().getGrupoCodigo());							
				if (listaResponsavelPendencia != null && listaResponsavelPendencia.size()>0){
					dt.addResponsavel((PendenciaResponsavelDt)listaResponsavelPendencia.get(0));								
				}
				
				if (dt.getNomeParte() != null && dt.getNomeParte().length()>0)
					intimacoesLidasSubstitutoProcessual.add(dt);
				else						
					intimacoesLidas.add(dt);
			}

			if (intimacoesLidas.size() > 0) {							
				listaIntimacoesLidas.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
				listaIntimacoesLidas.setPendenciasAndamento(intimacoesLidas);
				listaIntimacoesLidas.setQuantidadePendencias(intimacoesLidas.size());
			}
			
			if (intimacoesLidasSubstitutoProcessual.size() > 0) {							
				listaIntimacoesLidasSubstitutoProcessual.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
				listaIntimacoesLidasSubstitutoProcessual.setPendenciasAndamento(intimacoesLidasSubstitutoProcessual);
				listaIntimacoesLidasSubstitutoProcessual.setQuantidadePendencias(intimacoesLidasSubstitutoProcessual.size());
			}				
		}					

		if ((listaIntimacoes.getQuantidadePendencias() > 0 ||
			 listaIntimacoesSubstitutoProcessual.getQuantidadePendencias() > 0 ||
			 listaIntimacoesLidas.getQuantidadePendencias() > 0 ||
			 listaIntimacoesLidasSubstitutoProcessual.getQuantidadePendencias() > 0)) {
			request.setAttribute("ListaIntimacoes", listaIntimacoes);
			request.setAttribute("ListaIntimacoesSubstitutoProcessual", listaIntimacoesSubstitutoProcessual);
			request.setAttribute("ListaIntimacoesLidas", listaIntimacoesLidas);
			request.setAttribute("ListaIntimacoesLidasSubstitutoProcessual", listaIntimacoesLidasSubstitutoProcessual);
		} else if (request.getParameter("MensagemOk") == null && request.getParameter("MensagemErro") == null) {
			request.setAttribute("MensagemErro", "Não foram localizados processos para o usuário logado.");
		}		
		
		this.montarOpcoesLote(request, UsuarioSessao);
	}

	/**
	 * Método que define a tela de retorno (stAcao) dependendo do tipoConsulta setado no request.
	 * @param tipoBusca - tipo de Consulta setado no request
	 * @return string com o caminho da tela de retorno
	 * @author hmgodinho
	 * @param proprios 
	 */
	protected String definirAcaoRetorno(int proprios, int tipoConsulta) {
		String stAcao = null;

		//Se o tipoConsulta for setado no request como 1, deve redirecionar a pesquisa para a página
		//de consulta por dados de advogado.
		//Se for 3, deve direcionar a pesquisa para a página de consulta por defensor público/procurador/advogado escritorio juridico.
		//Se não, redireciona para a pesquisa de processo normal

		if ( tipoConsulta==5) {
			stAcao = "/WEB-INF/jsptjgo/ProcessoClassificar.jsp";
		} else if ( tipoConsulta == 3) {
			stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaDefensorProcuaradorAdvogadoEJ.jsp";
		}else {
    		if (proprios == 1){
    			stAcao = "/WEB-INF/jsptjgo/ProcessoLocalizarProprios.jsp";			
    		} else {
				stAcao = "/WEB-INF/jsptjgo/ProcessoLocalizar.jsp";
			}
		}

		return stAcao;
	}
	
	protected String definirPaginaProcesso(boolean boSegredoJustica) {
		String stAcao = null;

		stAcao = "/WEB-INF/jsptjgo/DadosProcesso.jsp";
		if(boSegredoJustica) {
			stAcao = "/WEB-INF/jsptjgo/DadosProcessoSegredoJustica.jsp";
		}

		return stAcao;
	}
	
	protected String definirTelaAbertura(int tipoBusca) {
		String stAcao = null;

		//Se o tipoConsulta for setado no request como 1, deve redirecionar a pesquisa para a página
		//de consulta por dados de advogado.
		//Se for 3, deve direcionar a pesquisa para a página de consulta por defensor público/procurador/advogado escritorio juridico.
		//Se não, redireciona para a pesquisa de processo normal
		 if (tipoBusca== 1) {
			stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaDadosAdvogado.jsp";
		} else if ( tipoBusca== 3) {
			stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaDefensorProcuaradorAdvogadoEJ.jsp";
		} else if (tipoBusca==4) {
			stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaPromotorResponsavelMP.jsp";					
		} else if (tipoBusca==5) {
			stAcao = "/WEB-INF/jsptjgo/ProcessoClassificar.jsp";
		} else if (tipoBusca==13) {
			stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisaInquerito.jsp";
		} else {
			stAcao = "/WEB-INF/jsptjgo/ProcessoPesquisa.jsp";	
		}

		return stAcao;
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
	protected void consultarSituacaoProcesso(HttpServletRequest request, UsuarioNe usuarioSessao,  ProcessoNe processoNe, ServentiaDt serventiaDt, HttpServletResponse response) throws Exception{

		//Consultar pendências em aberto ou não vistadas
		List[] listaPendencias = null;
		
		ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt==null){
			redireciona(response, "Usuario?PaginaAtual=-10&MensagemErro=Problema Detectado por Usar o Botão Voltar do Navegador. Refaça a Operação.");
			return;
		}
		//Se usuário não é da serventia do processo a consulta de pendências é diferenciada
		if (!processoDt.isMesmaServentia(usuarioSessao.getId_Serventia())) {
			listaPendencias = processoNe.consultarPendenciasProcesso(processoDt.getId(), false, usuarioSessao.getUsuarioDt());
		} else {
			listaPendencias = processoNe.consultarPendenciasProcessoHash(processoDt.getId(), usuarioSessao);		
		}
		request.setAttribute("ListaPendencias", listaPendencias);

		//Consultar conclusões em aberto
		List conclusoesPendentes = processoNe.consultarConclusoesPendentesProcessoHash(processoDt.getId(),  usuarioSessao);
		request.setAttribute("ConclusaoPendente", conclusoesPendentes);
		//Verifica se usuário tem permissão para analisar e pré-analisar conclusoes
//		request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao));
//		request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));

		//Consultar audiências em aberto
		List<String[]> audienciasPendentes = processoNe.consultarAudienciasPendentes(processoDt.getId(), false);
		
		//analisando esse ponto também
		if (serventiaDt != null && (serventiaDt.isSegundoGrau() || serventiaDt.isUpjTurmaRecursal() )) {
				
			List<String[]> audienciasPendentesTemp = new ArrayList<String[]>();
			List<String[]> audienciasCejuscPendentes = new ArrayList<String[]>();
			boolean podeMovimentarAudienciaCejusc = false;
			
			for (String[] audienciaPendente : audienciasPendentes) {
				if (audienciaPendente != null) {
					if (Funcoes.StringToInt(audienciaPendente[9]) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
						Funcoes.StringToInt(audienciaPendente[9]) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
						Funcoes.StringToInt(audienciaPendente[9]) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo()) {
						
						audienciasCejuscPendentes.add(audienciaPendente); 	
						
						if (Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.PREPROCESSUAL &&
							Funcoes.StringToLong(audienciaPendente[7]) == Funcoes.StringToLong(usuarioSessao.getUsuarioDt().getId_Serventia())) {
							podeMovimentarAudienciaCejusc = true;
						}
					} else {
						audienciasPendentesTemp.add(audienciaPendente);
					}	
				}				
			}
			
			audienciasPendentes = audienciasPendentesTemp;
			request.setAttribute("podeMovimentarAudienciaCejusc", podeMovimentarAudienciaCejusc);
			request.setAttribute("AudienciaCejuscPendente", audienciasCejuscPendentes);
			
		} else {
			
			for (String[] audienciaPendente : audienciasPendentes) {			
				if (Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getId_Serventia()) != Funcoes.StringToInt(processoDt.getId_Serventia())) {
					boolean podeMovimentarAudiencia = false;
					if (audienciaPendente != null && Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.PREPROCESSUAL &&
					    Funcoes.StringToLong(audienciaPendente[7]) == Funcoes.StringToLong(usuarioSessao.getUsuarioDt().getId_Serventia()) &&
					    (Funcoes.StringToInt(audienciaPendente[9]) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC.getCodigo() ||
					     Funcoes.StringToInt(audienciaPendente[9]) == AudienciaTipoDt.Codigo.CONCILIACAO.getCodigo() ||
					     Funcoes.StringToInt(audienciaPendente[9]) == AudienciaTipoDt.Codigo.CONCILIACAO_CEJUSC_DPVAT.getCodigo() ||
					     Funcoes.StringToInt(audienciaPendente[9]) == AudienciaTipoDt.Codigo.MEDIACAO_CEJUSC.getCodigo())) {
						podeMovimentarAudiencia = true;
					}
					request.setAttribute("podeMovimentarAudiencia", podeMovimentarAudiencia);
				}
			}
		}
		
		request.setAttribute("AudienciaPendente", audienciasPendentes);
		//Verifica se usuário tem permissão para movimentar audiência
		request.setAttribute("podeMovimentarAudiencia", usuarioSessao.getVerificaPermissao(AudienciaMovimentacaoDt.CodigoPermissaoAudienciaProcesso));
		
		if((Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU || 
			Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL)
	       && processoDt.getId_Serventia().equalsIgnoreCase(usuarioSessao.getUsuarioDt().getId_Serventia())){
			
			if (Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU) {
				request.setAttribute("HabilitadoTrocarResponsavelConclusao", "true");
				request.setAttribute("HabilitadoTrocarResponsavelVotoEmenta", "true");
			}
			request.setAttribute("podeVisualizarVotoEmenta", "true");
			//Consultar voto/ementa em aberto
			List votoEmentaPendentes = processoNe.consultarVotoEmentaPendentesProcessoHash(processoDt.getId(),  usuarioSessao);
			request.setAttribute("VotoEmentaPendente", votoEmentaPendentes);
		}
		
		if (Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE) {
			List<ServentiaCargoDt> responsaveis =  processoNe.consultarJuizesSegundoGrauResponsaveisProcesso(processoDt.getId());
			for (Iterator<ServentiaCargoDt> iterator = responsaveis.iterator(); iterator.hasNext();) {
				ServentiaCargoDt serventiaCargoDt = iterator.next();
				if (serventiaCargoDt.getId_Serventia().equalsIgnoreCase(usuarioSessao.getUsuarioDt().getId_Serventia())){
					request.setAttribute("HabilitadoTrocarResponsavelConclusao", "true");
					request.setAttribute("HabilitadoTrocarResponsavelVotoEmenta", "true");
					request.setAttribute("podeVisualizarVotoEmenta", "true");
					//Consultar voto/ementa em aberto
					List votoEmentaPendentes = processoNe.consultarVotoEmentaPendentesProcessoHash(processoDt.getId(),  usuarioSessao);
					request.setAttribute("VotoEmentaPendente", votoEmentaPendentes);
					break;
				}
			}
		}
		

	}

	/**
	 * Método responsável em consultar os responsáveis pelo processo
	 * @throws Exception 
	 */
	protected void consultarResponsaveisProcesso(HttpServletRequest request,  UsuarioNe UsuarioSessao, ProcessoNe processoNe) throws Exception{
		
		ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		
		//Responsáveis Pendência
		List listaResponsaveis = processoNe.consultarResponsaveisProcesso(processoDt.getId(), processoDt.getId_Serventia(), UsuarioSessao.getUsuarioDt().getGrupoCodigo());
		request.setAttribute("ListaResponsaveis", listaResponsaveis);
		
		//Responsáveis deabilitados
		if(UsuarioSessao.isGerenciamentoSegundoGrau() || UsuarioSessao.isAdministrador()){
			List listaResponsaveisDesabilitados = processoNe.consultarResponsaveisDesabilitadosProcesso(processoDt.getId(), processoDt.getId_Serventia());
			request.setAttribute("ListaResponsaveisDesabilitados", listaResponsaveisDesabilitados);
		}

		List listaAdvogados = processoNe.consultarAdvogadosProcesso(processoDt.getId());
		request.setAttribute("ListaAdvogados", listaAdvogados);
		
		List listaAdvogadosDesabilitados = processoNe.consultarAdvogadosDesabilitadosProcesso(processoDt.getId());
		request.setAttribute("ListaAdvogadosDesabilitados", listaAdvogadosDesabilitados);

		//Verifica se usuário pode habilitar advogados
		request.setAttribute("podeHabilitarAdvogados", UsuarioSessao.getVerificaPermissao(ProcessoParteAdvogadoDt.CodigoPermissao));
		
		//Verifica se o usuário pode habilitar relator desabilitado no processo de 2º grau
		request.setAttribute("podeHabilitarRelatorInativo", UsuarioSessao.getGrupoCodigo().equalsIgnoreCase(String.valueOf(GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU)));
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
	protected boolean consultarDadosProcesso(String id_Processo,  ProcessoNe processoNe, HttpServletRequest request, UsuarioNe usuarioSessao) throws Exception{
		
		ProcessoDt processoDt = new ProcessoDt();
		
		boolean acessoOutraServentia = false, boSegredoJustica = false;
		String processoOutraServentia = null;
		//Captura se trata de um Processo onde o acesso de outra serventia deve ser liberado, como acontece com as Cartas Precatórios, Liberação de Acesso
		if (request.getSession().getAttribute("AcessoOutraServentia") != null && !request.getSession().getAttribute("AcessoOutraServentia").equals("")) {
			if (request.getSession().getAttribute("ProcessoOutraServentia") != null) processoOutraServentia = (String) request.getSession().getAttribute("ProcessoOutraServentia");
			//Se processo que foi consultado é o mesmo com acesso externo liberado, deve liberar download dos arquivos
			if (processoOutraServentia.equalsIgnoreCase(id_Processo)) acessoOutraServentia = true;
		}
		
		//Verifica se tem necessidade de consultar os dados completos do processo novamente
		//Se tem uma mensagem de sucesso no request, significa que fluxo está vindo de outra servlet e deve atualizar os dados do processo
		if (!id_Processo.equalsIgnoreCase("")) if (!id_Processo.equalsIgnoreCase(processoDt.getId_Processo()) || request.getParameter("MensagemOk") != null) {			
			processoDt = processoNe.consultarDadosProcesso(id_Processo, usuarioSessao.getUsuarioDt(), false, acessoOutraServentia, usuarioSessao.getNivelAcesso());			
			request.getSession().setAttribute("processoDt", processoDt);
			if(processoDt.getListaMovimentacoes() != null) {
				Map<String, List<VotanteDt>> listaVotantesMovimentacaoExtratoAtaInserido = new MovimentacaoNe().consultaListaVotantesPorMovimentacaoExtratoAtaInserido(
						processoDt.getListaMovimentacoes());
				request.getSession().setAttribute("listaVotantesMovimentacaoExtratoAtaInserido", listaVotantesMovimentacaoExtratoAtaInserido);
			}
		} else { 
			atualizaDadosProcesso(request, processoDt, processoNe, usuarioSessao, acessoOutraServentia);
		}
		
		// Prepara menu especial de Processo
		String Menu = usuarioSessao.getMenuEspecial(PermissaoEspecialDt.OpcoesProcessoCodigo);

		String Menu2 = usuarioSessao.getMenuEspecial(PermissaoEspecialDt.OpcoesProcessoHibrido);
		String Menu_exhibrido = usuarioSessao.getMenuEspecial(PermissaoEspecialDt.OpcoesProcessoExHibrido);
		String Menu_outras = usuarioSessao.getMenuEspecial(PermissaoEspecialDt.OPCOES_OUTRAS);
		String Menu_Pre = usuarioSessao.getMenuEspecial(PermissaoEspecialDt.OPCOES_PRECATORIA);
		
		request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoCodigo, Menu);
		request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoHibrido, Menu2);		
		request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OPCOES_OUTRAS, Menu_outras);
		request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OPCOES_PRECATORIA, Menu_Pre);
		request.setAttribute("MenuEspecial" + PermissaoEspecialDt.OpcoesProcessoExHibrido, Menu_exhibrido);
		
		if (processoDt.getId_Recurso().length() != 0) {		
			// Quando se tratar de recurso inominado deverá exibir um menu especial diferenciado e os dados do recurso serão mostrados
			RecursoDt recursoDt = processoNe.consultarDadosRecurso(processoDt.getId_Recurso(), false);
			processoDt.setRecursoDt(recursoDt);
		}
		
		//Se foi passada variável para liberar acesso ao Processo Originário joga variáveis na sessão
		if (request.getParameter("ProcessoOutraServentia") != null && request.getParameter("ProcessoOutraServentia").equals("true")) {
			request.getSession().setAttribute("ProcessoOutraServentia", processoDt.getId_Processo());
			request.getSession().setAttribute("AcessoOutraServentia", "1");
		}
		
		//passando a lista de movimentações do processo para o request
		processoDt = processoNe.prepararListaMovimentacoesProcesso(processoDt, usuarioSessao, false);
		//conform ata 11º Reunião (Extraordinária) – Comissão de Informatização (05/02/2018)
		// a ata esta salva na documentação do sistema Ata da 11ª Reunião Extraordinária - Comissão de Informatização.pdf
		if(!usuarioSessao.isDesembargador() && processoDt.isSegredoJustica() || processoDt.isSigiloso()){
			if(!processoNe.podeAcessarProcesso(usuarioSessao.getUsuarioDt(), processoDt, null)) {
				for (int j = 0; j < processoDt.getPartesProcesso().size(); j++) {
					ProcessoParteDt parteDt = (ProcessoParteDt) processoDt.getPartesProcesso().get(j);
					String nomeParteSimplicado = Funcoes.iniciaisNome(parteDt.getNome());
					parteDt.limpar();
					parteDt.setNome(nomeParteSimplicado);
				}
				boSegredoJustica = true;
			}
		}
		
		return boSegredoJustica;
		
	}

	/**
	 * Verifica quais opções de movimentação em lote o usuário poderá visualizar
	 * @param request
	 * @param usuarioSessao
	 */
	protected void montarOpcoesLote(HttpServletRequest request, UsuarioNe usuarioSessao) {
		if (Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoCodigo(),-1) != GrupoDt.ANALISTA_PRE_PROCESSUAL) {
			request.setAttribute("podeRedistribuir", usuarioSessao.getVerificaPermissao(RedistribuicaoDt.CodigoPermissao));
		}else {
			request.setAttribute("podeRedistribuir", "false");
		}
		request.setAttribute("podeMovimentar", usuarioSessao.getVerificaPermissao(MovimentacaoDt.CodigoPermissao));		
		request.setAttribute("podeEncaminhar", usuarioSessao.getVerificaPermissao(ProcessoEncaminhamentoDt.CodigoPermissao));
	}

	/**
	 * Atualiza os dados do processo
	 * @throws Exception 
	 */
	protected void atualizaDadosProcesso(HttpServletRequest request, ProcessoDt processoDt, ProcessoNe processoNe, UsuarioNe usuarioNe, boolean acessoOutraServentia) throws Exception{

		// Atualiza somente lista de movimentações do processo
		processoDt.setListaMovimentacoes(processoNe.consultarMovimentacoesProcesso(usuarioNe.getUsuarioDt(), processoDt.getId_Processo(), acessoOutraServentia, usuarioNe.getNivelAcesso()));

		String atualizaPartes = request.getParameter("atualiza");
		// Define se lista de partes deve ser atualizada
		if (atualizaPartes != null && atualizaPartes.equals("true")) {
			processoDt.setListaPolosAtivos(processoNe.consultarPromoventesAtivos(processoDt.getId_Processo()));
			processoDt.setListaPolosPassivos(processoNe.consultarPromovidosAtivos(processoDt.getId_Processo()));
			processoDt.setListaOutrasPartes(processoNe.consultarOutrasPartesAtivas(processoDt.getId_Processo()));
		}
	}

	/**
	 * Consulta os status de processo disponíveis
	 * @throws Exception 
	 */
	private boolean consultarProcessoStatus(HttpServletRequest request, ProcessoNe processone, String tempNomeBusca, String posicaoPagina, int paginaatual) throws Exception{
		boolean boRetorno = false;
		List tempList = processone.consultarDescricaoProcessoStatus(tempNomeBusca, posicaoPagina);
		if (tempList.size() > 0) {
			request.setAttribute("ListaProcessoStatus", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
			request.setAttribute("QuantidadePaginas", processone.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_ProcessoStatus", "Id_ProcessoStatus");
			request.setAttribute("tempBuscaProcessoStatus", "ProcessoStatus");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Status foi localizado.");
		return boRetorno;
	}
	

	/**
	 * Consulta de serventias
	 * @throws Exception 
	 */
	private boolean consultarServentia(HttpServletRequest request, ProcessoNe processone, String tempNomeBusca, String posicaoPagina, int paginaatual, int passoBusca, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;
		List tempList = processone.consultarServentia(tempNomeBusca, posicaoPagina, usuarioSessao.getUsuarioDt());
		if (tempList.size() > 0) {
			request.setAttribute("ListaServentia", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
			request.setAttribute("QuantidadePaginas", processone.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_Serventia", "Id_Serventia");
			request.setAttribute("tempBuscaServentia", "Serventia");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhuma Serventia foi localizada.");
		return boRetorno;
	}


	/** 
	 * Consulta de relatores de processos da serventia
	 * @author hmgodinho 
	 * @throws Exception 
	 */
	protected boolean consultarRelatorProcessoServentia(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, int paginaatual, int passoBusca, String id_Serventia, String Serventia, ProcessoResponsavelNe processoResponsavelNe, String serventiaSubtipoCodigo) throws Exception{
		boolean boRetorno = false;
		request.setAttribute("tempBuscaId_ServentiaCargo", "Id_Relator");
		request.setAttribute("tempBuscaServentiaCargo", "Relator");
		List tempList = processoResponsavelNe.consultarServentiaCargos(tempNomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU), serventiaSubtipoCodigo);
		if (tempList.size() > 0) {
			request.setAttribute("ListaServentiaCargo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);		
			
			request.setAttribute("Id_Serventia", id_Serventia);
			request.setAttribute("Serventia", Serventia);
			request.setAttribute("ServentiaTipoCodigo", String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU));
			request.setAttribute("ServentiaSubTipoCodigo", serventiaSubtipoCodigo);
			

			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", processoResponsavelNe.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_Relator", "Id_Relator");
			request.setAttribute("tempBuscaRelator", "Relator");
			request.setAttribute("tempRetorno", "BuscaProcesso?PaginaAtual=" + Configuracao.Curinga8 + "&PassoBusca=" + passoBusca);
			boRetorno = true;
		} else {
			request.setAttribute("MensagemErro", "Nenhum Cargo Disponível.");
		}

		return boRetorno;
	}
	
	protected final String idCodigoHashEOutros = "CodigoHashEOutros";
	/**
	 * Armazena o código Rash gerado para validação futura durante o acesso ao link e outros, evitando uso de robô
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected void armazeneCodigoHashEOutros(HttpServletRequest request, UsuarioNe usuarioNe, String idProcesso) throws Exception{
		request.setAttribute(idCodigoHashEOutros, usuarioNe.getCodigoHash(idProcesso));
	}
	
	/**
	 * Valida o código Rash gerado para acesso ao link e outros, evitando uso de robô
     *      
     * @param HttpServletRequest request 
     *
	 */
	protected boolean validaCodigoHashEOutros(HttpServletRequest request, UsuarioNe usuarioNe, String idProcesso) throws Exception{
		String codigoHashGerado = (String) request.getParameter(idCodigoHashEOutros);
		
		if (codigoHashGerado == null || codigoHashGerado.trim().equalsIgnoreCase("")) return false;
		
		return usuarioNe.VerificarCodigoHash(idProcesso, codigoHashGerado);
	}
	
	protected List<?> buscarProcessoClassificar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual, ProcessoNe processoNe, BuscaProcessoDt buscaProcesso) throws Exception {
		List<?> tempList = null;


		Boolean podeClassificar = false;
		buscaProcesso.setId_Serventia( UsuarioSessao.getUsuarioDt().getId_Serventia());
		tempList = processoNe.consultarProcessosClassificar(buscaProcesso,  PosicaoPaginaAtual);

		if ((tempList != null) && (!tempList.isEmpty()) && (tempList.size() > 0)) {
			podeClassificar = true;
		}

		request.setAttribute("PassoBusca", 9);
		request.setAttribute("ListaProcesso", tempList);
		request.setAttribute("podeClassificar", podeClassificar);

		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
		request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());

		return tempList;
	}
	
	protected List buscarProcessoJuiz(HttpServletRequest request,  ProcessoNe Processone,  UsuarioNe UsuarioSessao, String posicaoPagina, BuscaProcessoDt buscaProcessoDt ) throws Exception {
		List tempList = null;
		if (buscaProcessoDt.isSigiloso()){
			tempList = Processone.consultarProcessosSigiloso(  UsuarioSessao.getUsuarioDt(), posicaoPagina);											
    	}else{
    		tempList = Processone.consultarProcessosJuiz(buscaProcessoDt, UsuarioSessao.getUsuarioDt(), posicaoPagina);
    	}
		return tempList;
	}
	
	protected List buscarProcessoInquerito(HttpServletRequest request, ProcessoNe processone, String posicaoPagina, UsuarioNe usuarioSessao, BuscaProcessoDt buscaProcessoDt ) throws Exception {
		List tempList = null;
		String mensagemRetorno = "";
		if (buscaProcessoDt.getInquerito() == null || buscaProcessoDt.getInquerito().equals(""))  {
			mensagemRetorno = "O número do inquérito deve ser informado.";
		}
		if (mensagemRetorno.equals("")) {
			tempList = processone.consultarProcessosInquerito( buscaProcessoDt,usuarioSessao.getUsuarioDt(), posicaoPagina);			
		} else {
			request.setAttribute("MensagemErro", mensagemRetorno);
			request.setAttribute("PaginaAtual", Configuracao.Novo);			
		}
		return tempList;
	}
	
	protected List buscarProcessoAdvogado(HttpServletRequest request, ProcessoNe processone, String posicaoPagina, UsuarioNe usuarioSessao, BuscaProcessoDt buscaProcessoDt ) throws Exception {
		List tempList = null;
		String mensagemRetorno = "";
		if (((buscaProcessoDt.getOabNumero() == null || buscaProcessoDt.getOabNumero().equals("")) ||
				(buscaProcessoDt.getOabComplemento() == null || buscaProcessoDt.getOabComplemento().equals("")) ||
				(buscaProcessoDt.getOabUf() == null || buscaProcessoDt.getOabUf().equals(""))) ) {
			mensagemRetorno = "OAB Número/Complemento e UF devem ser informados.";
		}
		if (mensagemRetorno.equals("")) {
			tempList = processone.consultarProcessosDadosAdvogado( buscaProcessoDt,usuarioSessao.getUsuarioDt(), posicaoPagina);			
		} else {
			request.setAttribute("MensagemErro", mensagemRetorno);
			request.setAttribute("PaginaAtual", Configuracao.Novo);			
		}
		return tempList;
	}
	
	protected List buscarProcessoAdvogadoPublicosEscritorio(HttpServletRequest request, ProcessoNe processone, String posicaoPagina, UsuarioNe usuarioSessao, BuscaProcessoDt buscaProcessoDt) throws Exception {
		List tempList = null;
		String mensagemRetorno = "";
		if ( !buscaProcessoDt.temId_Serventia() || !buscaProcessoDt.temId_UsuarioServentia()) {
			mensagemRetorno = "Serventia e Defensor/Procurador/Advogado devem ser informados.";
		}
		if (mensagemRetorno.equals("")) {
			tempList = processone.consultarProcessosDefensorPublicoProcuradorAdvogado(buscaProcessoDt.getId_UsuarioServentia());					
		} else {
			request.setAttribute("MensagemErro", mensagemRetorno);
			request.setAttribute("PaginaAtual", Configuracao.Novo);			
		}
		return tempList;
	}
	
	protected List buscarProcessoGeral(HttpServletRequest request, ProcessoNe Processone, String posicaoPagina, UsuarioNe usuarioSessao, BuscaProcessoDt buscaProcessoDt, int proprios, int passoBusca) throws Exception {
		List tempList = null;

		//Se o usuário marcar o checkbox na tela solicitando pesquisa por nome exato o valor será setado no request.
		String pesquisarNomeExato =String.valueOf( request.getParameter("pesquisarNomeExato"));	
		
		if(proprios == 0 && usuarioSessao.isProprio()) {
			if (StringUtils.equals(buscaProcessoDt.getServentia(), usuarioSessao.getServentia())) {
				proprios = 1;
			}
		}
		
		if (proprios == 1){
			if (buscaProcessoDt.isSigiloso()){
				if (usuarioSessao.isMagistrado() || usuarioSessao.isAssessorMagistrado() ){
					tempList = Processone.consultarProcessosSigiloso(  usuarioSessao.getUsuarioDt(), posicaoPagina);									
				}else{
					request.setAttribute("MensagemErro", "Processos Sigilosos estão disponiveis somente aos magistrados."); 
				}
				
			}else{
				buscaProcessoDt.setPesquisarNomeExato(pesquisarNomeExato);
				buscaProcessoDt.setComarca( usuarioSessao.getUsuarioDt().getId_Comarca());
				tempList = Processone.consultarProcessos(buscaProcessoDt,usuarioSessao.getUsuarioDt(), posicaoPagina);
			}
		} else{
			tempList =  Processone.consultarTodosProcessos(buscaProcessoDt, usuarioSessao.getUsuarioDt(), posicaoPagina);
		}
		
		return tempList;
		
	}
	
	protected List buscarProcessoArquivadosSemMotivo(HttpServletRequest request,  UsuarioNe UsuarioSessao, String PosicaoPaginaAtual, ProcessoNe processoNe) throws Exception {
			List tempList = null;
						
			tempList = processoNe.consultarProcessosArquivadosSemMovito(UsuarioSessao.getUsuarioDt().getId_Serventia(), PosicaoPaginaAtual);			

			//request.setAttribute("PassoBusca", 9);
			request.setAttribute("ListaProcesso", tempList);
			
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());

			return tempList;
	}
	
	protected List buscarProcessoInconsistenciaPoloPassivo(HttpServletRequest request,  UsuarioNe UsuarioSessao, String PosicaoPaginaAtual, ProcessoNe processoNe) throws Exception {
		List tempList = null;
					
		tempList = processoNe.consultarProcessosInconsistenciaPoloPassivo(UsuarioSessao.getUsuarioDt().getId_Serventia(), PosicaoPaginaAtual);			

		//request.setAttribute("PassoBusca", 9);
		request.setAttribute("ListaProcesso", tempList);
		
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
		request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());

		return tempList;
	}
	
	protected List buscarProcessosSemAssunto(HttpServletRequest request,  UsuarioNe UsuarioSessao, String PosicaoPaginaAtual, ProcessoNe processoNe) throws Exception {
		List tempList = null;
					
		tempList = processoNe.consultarProcessosSemAssunto(UsuarioSessao.getUsuarioDt().getId_Serventia(), PosicaoPaginaAtual);			

		request.setAttribute("ListaProcesso", tempList);
		
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
		request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());

		return tempList;
	}
	
	protected List buscarProcessosComAssuntoPai(HttpServletRequest request,  UsuarioNe UsuarioSessao, String PosicaoPaginaAtual, ProcessoNe processoNe) throws Exception {
		List tempList = null;
					
		tempList = processoNe.consultarProcessosComAssuntoPai(UsuarioSessao.getUsuarioDt().getId_Serventia(), PosicaoPaginaAtual);			

		request.setAttribute("ListaProcesso", tempList);
		
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
		request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());

		return tempList;
	}
	
	protected List buscarProcessosComClassePai(HttpServletRequest request,  UsuarioNe UsuarioSessao, String PosicaoPaginaAtual, ProcessoNe processoNe) throws Exception {
		List tempList = null;
					
		tempList = processoNe.consultarProcessosComClassePai(UsuarioSessao.getUsuarioDt().getId_Serventia(), PosicaoPaginaAtual);			

		request.setAttribute("ListaProcesso", tempList);
		
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
		request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());

		return tempList;
	}
	
	
	protected List buscarProcessosPrisaoForaPrazo(HttpServletRequest request,  UsuarioNe UsuarioSessao, String PosicaoPaginaAtual, ProcessoNe processoNe) throws Exception {
		List tempList = null;
		
		tempList = processoNe.consultarProcessosPrisaoForaPrazo(UsuarioSessao.getUsuarioDt().getId_Serventia(), PosicaoPaginaAtual);			

		//request.setAttribute("PassoBusca", 9);
		request.setAttribute("ListaProcesso", tempList);
		
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
		request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());

		return tempList;
	}
}

