package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.RedistribuicaoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que controla a redistribuição de processos/recursos.
 * Essa redistribuição pode ser feita com regra de distribuição (distribuição em uma área de varas)
 * ou sem regra de distribuição (distribuição para serventia específica). 
 *  
 * @author msapaula
 *
 */
public class RedistribuicaoCt extends Controle {

	protected static final long serialVersionUID = -8410427876414770648L;

    public int Permissao() {
		return RedistribuicaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		RedistribuicaoDt redistribuicaoDt;
		ProcessoNe processoNe;
		ProcessoDt processoDt = null;
		MovimentacaoProcessoDt movimentacaoProcessoDt;
		int passoEditar = -1;
		
		//-Variáveis para controlar as buscas utilizando ajax
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		//-fim controle de buscas ajax
		
		String Mensagem = "";
		String processos[] = null;
		String stAcao = null;
		
		if (request.getSession().getAttribute("enviarPara") != null && request.getSession().getAttribute("enviarPara").toString().equals("true")){
			stAcao = "/WEB-INF/jsptjgo/RedistribuicaoEnviarParaServentia.jsp";
		} else {
			stAcao = "/WEB-INF/jsptjgo/Redistribuicao.jsp";
		}

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		request.setAttribute("tempPrograma", "Redistribuicao");
		request.setAttribute("tempRetorno", "Redistribuicao");
		if (request.getAttribute("TituloPagina") == null) request.setAttribute("TituloPagina", "Redistribuição de Processo");

		processoNe = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (processoNe == null) processoNe = new ProcessoNe();
		
		movimentacaoProcessoDt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
		if (movimentacaoProcessoDt == null) movimentacaoProcessoDt = new MovimentacaoProcessoDt();

		redistribuicaoDt = (RedistribuicaoDt) request.getSession().getAttribute("Redistribuicaodt");
		if (redistribuicaoDt == null) redistribuicaoDt = new RedistribuicaoDt();

		redistribuicaoDt.setId_AreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
		redistribuicaoDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		redistribuicaoDt.setId_Serventia(request.getParameter("Id_Serventia"));
		redistribuicaoDt.setServentia(request.getParameter("Serventia"));
		redistribuicaoDt.setProcessoNumeroDependente(request.getParameter("ProcessoNumeroDependente"));
		redistribuicaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		redistribuicaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		redistribuicaoDt.setOpcaoRedistribuicao(request.getParameter("opcaoRedistribuicao"));
		redistribuicaoDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		
		//Esse if foi criado para apresentar o nome e o cargo do novo responsável pelo processo.
		//Sem essa consulta só é possível apresentar o cargo devido a forma como as informações voltam da tela JSON.
		if(redistribuicaoDt.getId_ServentiaCargo() != null && !redistribuicaoDt.getId_ServentiaCargo().equals("")){
			ServentiaCargoDt servCargoDt = new ServentiaCargoNe().consultarId(redistribuicaoDt.getId_ServentiaCargo());
			redistribuicaoDt.setServentiaCargo(servCargoDt.getServentiaCargo() + " - " +  servCargoDt.getNomeUsuario());
		}
		
		request.setAttribute("PaginaAnterior", paginaatual);
		// Mensagem de sucesso
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		// Mensagem de erro
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);		
		
		boolean boVerificarPrevensao = true; 
		if (request.getSession().getAttribute("verificarPrevensao")== null ){
			request.getSession().setAttribute("verificarPrevensao","true");
			boVerificarPrevensao = true; 
		}else {
			if (request.getSession().getAttribute("verificarPrevensao").toString().equals("true")){
				boVerificarPrevensao = true;
			} else{
				boVerificarPrevensao = false;
			}
		}
		
		switch (paginaatual) {

			//Inicializa redistribuição
			case Configuracao.Novo:
				redistribuicaoDt = new RedistribuicaoDt();
				
				//limpar variável para cadastro de repasse de guias
				request.getSession().removeAttribute("mostrarTodasOpcoesPorcentagemRepasse");

				// Captura os processos que serão movimentados, se for o caso de Movimentação em Lote
				if (request.getParameter("processos") != null) 
					processos = request.getParameterValues("processos");
				// Captura os processos que serão movimentados, se for o caso de Movimentação em Lote
				else if (request.getSession().getAttribute("processos") != null) 
					processos = (String[]) request.getSession().getAttribute("processos");
				else {
					//Recupera o processo da sessão e adiciona ao vetor
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					//Se processo não está na sessão, tenta capturar id do processo
					if (processoDt == null || processoDt.getId().length() == 0) {
						if (request.getParameter("Id_Processo") != null && !request.getParameter("Id_Processo").equals("")) processoDt = processoNe.consultarId(request.getParameter("Id_Processo"));
					}
					//Adiciona processo único ao vetor para montar tela corretamente
					if (processoDt != null) processos = new String[] {processoDt.getId() };
				}

				if (processos != null && processos.length > 0) {
					//Verifica se processo(s) pode(m) ser redistribuído(s) e monta tela para redistribuição
					Mensagem = verificaRedistribuicao(processos, processoDt, redistribuicaoDt, processoNe, UsuarioSessao, passoEditar, movimentacaoProcessoDt);

					if (Mensagem.length() == 0) montaTelaRedistribuicao(processos, request, passoEditar);
					else {
						if (processoDt != null) redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
						else {
							redireciona(response, "BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=2&MensagemErro=" + Mensagem);
							redistribuicaoDt.limpar();
						}
						return;
					}
					
					//limpar variável para cadastro de repasse de guias
					for( String idProcesso: processos ) {
						request.getSession().removeAttribute("mostrarTodasOpcoesPorcentagemRepasse" + idProcesso);
						request.getSession().removeAttribute("PorcentagemProcesso" + idProcesso);
					}
					
				} else {
					//Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				
				break;
			
			case Configuracao.Salvar:
				//Se for redistribuição dentro da própria câmara, não há necessidade de fazer validações
				if (request.getSession().getAttribute("propriaServentia") != null && request.getSession().getAttribute("propriaServentia").equals(Boolean.valueOf("true"))){
					break;
				};
				
				if ((redistribuicaoDt.getProcessoNumeroDependente() == null || redistribuicaoDt.getProcessoNumeroDependente().equals(""))
						&& (redistribuicaoDt.getId_AreaDistribuicao() == null || redistribuicaoDt.getId_AreaDistribuicao().equals(""))
						&& (redistribuicaoDt.getId_Serventia() == null || redistribuicaoDt.getId_Serventia().equals(""))) { 
					request.setAttribute("MensagemErro", "É preciso informar a forma de redistribuição.");
					break;
				}
				
				//Se o usuário tentar redistribuir pela opção 3, deverá informar a área de distribuição e serventia OBRIGATORIAMENTE  
				if(redistribuicaoDt.getOpcaoRedistribuicao() != null && redistribuicaoDt.getOpcaoRedistribuicao().equals("3")) {
					if (redistribuicaoDt.getId_AreaDistribuicao() == null || redistribuicaoDt.getId_AreaDistribuicao().equals("")
							|| redistribuicaoDt.getId_Serventia() == null || redistribuicaoDt.getId_Serventia().equals("")) { 
						request.setAttribute("MensagemErro", "Na opção 3 é preciso informar a Área de Distribuição e Serventia.");
						break;
					}
				}
				
				if (redistribuicaoDt.getProcessoNumeroDependente() != null && !redistribuicaoDt.getProcessoNumeroDependente().equals("")) {
					// Verifica se usuário digitou "." e dígito verificador
					ProcessoDt processoDependenteDt = processoNe.consultarProcessoNumeroCompleto(redistribuicaoDt.getProcessoNumeroDependente(),null);
					// Se não encontrar processo já devolve o erro
					if (processoDependenteDt == null) {
						request.setAttribute("MensagemErro", "Não foi possível localizar o Processo Principal informado.");
						break;
					}
				}
				
			break;

			//Salva movimentação genérica
			case Configuracao.SalvarResultado:
				//Se o atributo propriaServentia estiver na sessão e for TRUE, deve ser feita redistribuição dentro da mesma serventia
				if(request.getSession().getAttribute("propriaServentia") != null && request.getSession().getAttribute("propriaServentia").equals(true) && passoEditar != 1 && passoEditar != 2 && passoEditar != 3){
					processoNe.salvarRedistribuicaoPropriaServentia(redistribuicaoDt, UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt, null);
					request.setAttribute("MensagemOk", "Redistribuição de processo(s) efetuada com sucesso.");
					redistribuicaoDt.limpar();
				} else {
					
					// Se for redistribuição e não Encaminhamento verifica
					if(request.getSession().getAttribute("enviarPara") == null || !request.getSession().getAttribute("enviarPara").equals(true)) {
						Mensagem = processoNe.verificarRedistribuicao(redistribuicaoDt, UsuarioSessao.getUsuarioDt());
					} else {
						Mensagem = processoNe.verificarEncaminhamento(redistribuicaoDt, UsuarioSessao.getUsuarioDt());
					}
					
					// Valida porcentagem repasse
					if( request.getParameter("validarMenuPorcentagem") != null && request.getParameter("validarMenuPorcentagem").toString().equals("SIM") && redistribuicaoDt != null ) {
						Mensagem += new GuiaEmissaoNe().validaPorcentagemEscolhidaRedistribuicao(redistribuicaoDt);
					}
					
					if (Mensagem.length() == 0) {
						
						String mensagemOK;
						
						obtemPorcentagemRepasse(request, redistribuicaoDt);
						
						//Se o processo estiver sendo Encaminhado persiste usando um método diferente do da redistribuição. 
						if(request.getSession().getAttribute("enviarPara") != null && request.getSession().getAttribute("enviarPara").equals(true)) {
							processoNe.salvarEncaminhamentoProcesso(redistribuicaoDt, UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt, null, null);
							mensagemOK = "Processo encaminho para outra serventia com sucesso.";
							request.setAttribute("MensagemOk", mensagemOK);
							request.getSession().removeAttribute("enviarPara");
						} else {
							processoNe.salvarRedistribuicao(redistribuicaoDt, UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt, boVerificarPrevensao,  false);
						
							if (passoEditar == 1) mensagemOK = "Processo enviado para conciliação CEJUSC com sucesso.";
							else if (passoEditar == 2) mensagemOK = "Processo enviado para mediação CEJUSC com sucesso.";
							else if (passoEditar == 3) mensagemOK = "Processo enviado para a serventia de origem com sucesso.";
							else mensagemOK = "Redistribuição de processo(s) efetuada com sucesso.";
						
						}
						
						redistribuicaoDt.limpar();
						LimparSessao(request.getSession());
						request.getSession().setAttribute("verificarPrevensao", "true");
						
						if (passoEditar == 1 || passoEditar == 2 || passoEditar == 3) {
							//Dados movimentacao
							movimentacaoProcessoDt.limpar();
							limparListas(request);
							request.getSession().removeAttribute("Movimentacaodt");
							request.getSession().removeAttribute("propriaServentia");												
						} else {
							request.setAttribute("MensagemOk", mensagemOK);
						}
						//Volta para a página inicial						
						redireciona(response, "Usuario?PaginaAtual=" + Configuracao.Cancelar + "&MensagemOk=" + mensagemOK);
						return;	
					} else{
						request.setAttribute("MensagemErro", Mensagem);
					}
				}
				
				//Dados movimentacao
				movimentacaoProcessoDt.limpar();
				limparListas(request);
				request.getSession().removeAttribute("Movimentacaodt");
				break;

			case Configuracao.Excluir:
				if (redistribuicaoDt.getListaProcessos() != null) {
					if (redistribuicaoDt.getListaProcessos().size() > 1) {
						String posicao = request.getParameter("posicao");
						if (posicao != null && !posicao.equals("")) {
							ProcessoDt objTemp = (ProcessoDt) redistribuicaoDt.getListaProcessos().get(Funcoes.StringToInt(posicao));
							redistribuicaoDt.getListaProcessos().remove(Funcoes.StringToInt(posicao));
							request.setAttribute("MensagemOk", "Processo " + Funcoes.formataNumeroProcesso(objTemp.getProcessoNumero()) + " retirado dessa redistribuição múltipla.");
						}
					} else if (redistribuicaoDt.getListaProcessos().size() == 1) request.setAttribute("MensagemErro", "Não é possível retirar esse processo da movimentação múltipla.");
				}
				break;

			//Consultar Áreas de Distribuição
			case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"AreaDistribuicao"};
					String[] lisDescricao = {"AreaDistribuicao"};
					//quando for necessário retornar outros valos além do id, coloque outras colunas de descrição
					// na localizar.jsp as descrições geram novos input hidem para retornar ao ct
					// na funcoes.js as descricoes serão usadas para gerar os AlterarValue para retornar para o ct
					//String[] camposHidden = {"ForumCodigo","Id_ServentiaSubTipo"};
					//request.setAttribute("camposHidden",camposHidden);
					
					if(request.getParameter("tempFluxo2") != null && request.getParameter("tempFluxo2").equals("5")){
						request.setAttribute("tempFluxo2","5");
					}
					
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao","AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma","AreaDistribuicao");			
					request.setAttribute("tempRetorno","Redistribuicao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					redistribuicaoDt.setId_Serventia("null");
					redistribuicaoDt.setServentia("");
					redistribuicaoDt.setId_ServentiaCargo("null");
					redistribuicaoDt.setServentiaCargo("");
				} else {
					String stTemp="";
					
					// Se for fluxo 5 significa que é um encaminhamento e permite listar apenas as áreas Preprocessuais
					if(request.getParameter("tempFluxo2") != null && request.getParameter("tempFluxo2").equals("5")){
						stTemp = processoNe.consultarDescricaoAreasDistribuicaoPreprocessualAtivaJSON(stNomeBusca1, PosicaoPaginaAtual);
					}
					else {
						stTemp = processoNe.consultarDescricaoAreasDistribuicaoAtivaJSON(stNomeBusca1, PosicaoPaginaAtual);
					}
						
						enviarJSON(response, stTemp);
						
					
					return;								
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
					request.setAttribute("tempRetorno","Redistribuicao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					redistribuicaoDt.setId_ServentiaCargo("null");
					redistribuicaoDt.setServentiaCargo("");
				} else {
					String stTemp="";
					stTemp = processoNe.consultarServentiasAtivasAreaDistribuicaoJSON(stNomeBusca1, redistribuicaoDt.getId_AreaDistribuicao(), PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
			break;
			
		// Consultar relatores ou juizes responsáveis pelo processo
		case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(redistribuicaoDt.getId_Serventia());
			
			if (!serventiaDt.isSegundoGrau() || serventiaDt.isTurma()) {
				processoNe = new ProcessoNe();

				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = { "Usuário" };
					String[] lisDescricao = { "Cargo [Serventia]", "Usuário", "CargoTipo" };
					String[] camposHidden = { "ServentiaCargoUsuario", "CargoTipo" };
					request.setAttribute("camposHidden", camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "Usuario");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "Redistribuicao");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);

					request.setAttribute("tempFluxo1", "1");
				} else {
					String stTemp = "";
					
					if(serventiaDt.isTurma()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU), String.valueOf(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL));
					} else if(serventiaDt.isUpjFamilia()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_FAMILIA)); 
					} else if(serventiaDt.isUpjSucessoes()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_SUCESSOES)); 
					} else if(serventiaDt.isUpjCriminal()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_CRIMINAL));
					} else if(serventiaDt.isUpjViolenciaDomestica()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA));												
					} else if(serventiaDt.isUpjCustodia()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_CUSTODIA));	
					} else if(serventiaDt.isUpjTurmaRecursal()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU), String.valueOf(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL));	
					} else if(serventiaDt.isUpjJuizadoEspecialFazendaPublica()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA));	
					} else {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.VARAS_CIVEL));
					}
					
					enviarJSON(response, stTemp);
						
					return;
				}
				break;
			} else {
				ProcessoResponsavelNe ProcessoResponsavelne = new ProcessoResponsavelNe();

				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = { "Usuário" };
					String[] lisDescricao = { "Cargo [Serventia]", "Usuário", "CargoTipo" };
					String[] camposHidden = { "ServentiaCargoUsuario", "CargoTipo" };
					request.setAttribute("camposHidden", camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "Redistribuicao");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					
					//Força o envio do ServentiaTipoDt.SEGUNDO_GRAU e ServentiaSubtipoDt.CAMARA_CIVEL para realizar a consulta - devido a validações do método chamado
					stTemp = ProcessoResponsavelne.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU), String.valueOf(ServentiaSubtipoDt.CAMARA_CIVEL));
					
					enviarJSON(response, stTemp);
						

					return;
				}
			}
			break;
			case Configuracao.LocalizarDWR:
				//variavel para ignorar a prevensão e conexao na redistribuição normal				 
				if (request.getParameter("variavel")!=null){
					boVerificarPrevensao = true;
					
					if (request.getSession().getAttribute("verificarPrevensao").toString().equals("true")){
						boVerificarPrevensao = false;
						request.getSession().setAttribute("verificarPrevensao", boVerificarPrevensao);
					} else{
						boVerificarPrevensao = true;
						request.getSession().setAttribute("verificarPrevensao", boVerificarPrevensao);
					}
					
				}
				break;
			// Redistribuição de processo dentro da mesma serventia
			case Configuracao.Curinga6:
				redistribuicaoDt = new RedistribuicaoDt();

				// Captura os processos que serão movimentados, se for o caso de Movimentação em Lote
				if (request.getParameter("processos") != null) processos = request.getParameterValues("processos");
				else {
					//Recupera o processo da sessão e adiciona ao vetor
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					//Se processo não está na sessão, tenta capturar id do processo
					if (processoDt == null || processoDt.getId().length() == 0) {
						if (request.getParameter("Id_Processo") != null && !request.getParameter("Id_Processo").equals("")) processoDt = processoNe.consultarId(request.getParameter("Id_Processo"));
					}
					//Adiciona processo único ao vetor para montar tela corretamente
					if (processoDt != null) processos = new String[] {processoDt.getId() };
				}

				if (processos != null && processos.length > 0) {
					//Verifica se processo(s) pode(m) ser redistribuído(s) e monta tela para redistribuição
					Mensagem = verificaRedistribuicao(processos, processoDt, redistribuicaoDt, processoNe, UsuarioSessao, passoEditar, movimentacaoProcessoDt);

					if (Mensagem.length() == 0) {		
						montaTelaRedistribuicao(processos, request, passoEditar);
						request.getSession().setAttribute("propriaServentia", true);
					}
					else {
						if (processoDt != null) redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
						else redireciona(response, "BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=2&MensagemErro=" + Mensagem);
						return;
					}
				} else {
					//Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				break;
				
				// Enviar para outra serventia
				case Configuracao.Curinga7:
					
					redistribuicaoDt = new RedistribuicaoDt();

					// Captura os processos que serão movimentados, se for o caso de Movimentação em Lote
					if (request.getParameter("processos") != null) 
						processos = request.getParameterValues("processos");
					// Captura os processos que serão movimentados, se for o caso de Movimentação em Lote
					else if (request.getSession().getAttribute("processos") != null) 
						processos = (String[]) request.getSession().getAttribute("processos");
					else {
						//Recupera o processo da sessão e adiciona ao vetor
						processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
						//Se processo não está na sessão, tenta capturar id do processo
						if (processoDt == null || processoDt.getId().length() == 0) {
							if (request.getParameter("Id_Processo") != null && !request.getParameter("Id_Processo").equals("")) processoDt = processoNe.consultarId(request.getParameter("Id_Processo"));
						}
						//Adiciona processo único ao vetor para montar tela corretamente
						if (processoDt != null) processos = new String[] {processoDt.getId() };
					}
					
					
//					// Captura os processos que serão movimentados, se for o caso de Movimentação em Lote
//					if (request.getParameter("processos") != null) processos = request.getParameterValues("processos");
//					else {
//						//Recupera o processo da sessão e adiciona ao vetor
//						processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
//						//Se processo não está na sessão, tenta capturar id do processo
//						if (processoDt == null || processoDt.getId().length() == 0) {
//							if (request.getParameter("Id_Processo") != null && !request.getParameter("Id_Processo").equals("")) processoDt = processoNe.consultarId(request.getParameter("Id_Processo"));
//						}
//						//Adiciona processo único ao vetor para montar tela corretamente
//						if (processoDt != null) processos = new String[] {processoDt.getId() };
//					}

					if (processos != null && processos.length > 0) {
//						//Verifica se processo(s) pode(m) ser enviado(s) e monta tela para redistribuição
						Mensagem = verificaEnvio(processos, processoDt, redistribuicaoDt, processoNe, UsuarioSessao, passoEditar, movimentacaoProcessoDt);

						if (Mensagem.length() == 0) {		
							montaTelaRedistribuicao(processos, request, passoEditar);
						}
						else {
							if (processoDt != null) redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
							else redireciona(response, "BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=2&MensagemErro=" + Mensagem);
							return;
						}
					} else {
						//Volta para tela de consulta de processos com mensagem de erro
						redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=Nenhum Processo foi selecionado.");
						return;
					}
					request.getSession().setAttribute("enviarPara", true);
					stAcao = "/WEB-INF/jsptjgo/RedistribuicaoEnviarParaServentia.jsp";
					
					
					
					break;
				
			default:
				break;
		}
		if (request.getAttribute("TituloPagina") != null) request.setAttribute("TituloPagina", request.getAttribute("TituloPagina"));
		request.setAttribute("PassoEditar", passoEditar);
		
		if (boVerificarPrevensao){
			request.setAttribute("verificarPrevensao", true);
		}
		
		request.getSession().setAttribute("Redistribuicaodt", redistribuicaoDt);
		request.getSession().setAttribute("Processone", processoNe);
		
		if(request.getSession().getAttribute("propriaServentia") != null && request.getSession().getAttribute("propriaServentia").equals(true)){
			//Se a mensagem de ok estiver vazia ou nula, a redistribuição na própria serventia ainda não foi concluída.
			if(request.getAttribute("MensagemOk") == null || request.getAttribute("MensagemOk").equals("")) {
				if (passoEditar == 1 || passoEditar == 2 || passoEditar == 3) {
					stAcao = "/WEB-INF/jsptjgo/RedistribuicaoEnviarParaServentia.jsp";
				} else {
					stAcao = "/WEB-INF/jsptjgo/RedistribuicaoPropriaServentia.jsp";	
				}
			} else {
				//Quando concluir a redistribuição na própria serventia deve voltar pra capa do processo
				processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
				redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk="+request.getAttribute("MensagemOk"));
				request.getSession().removeAttribute("propriaServentia");
				return;
			}
		}
		
		obtemPorcentagemRepasse(request, redistribuicaoDt);
		
		mostrarMenuPorcentagemRepasseProcesso(processos, redistribuicaoDt);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Verifica se um processo pode ser redistribuído
	 * @throws Exception 
	 */
	protected String verificaRedistribuicao(String[] processos, ProcessoDt processoDt, RedistribuicaoDt redistribuicaoDt, ProcessoNe processoNe, UsuarioNe usuarioNe, int passoEditar, MovimentacaoProcessoDt movimentacaoProcessoDt) throws Exception{
		String Mensagem = "";

		this.consultaDadosProcessos(processos, redistribuicaoDt, processoDt, processoNe);
		List listaProcessos = redistribuicaoDt.getListaProcessos();

		for (int i = 0; i < listaProcessos.size(); i++) {
			ProcessoDt dt = (ProcessoDt) listaProcessos.get(i);
			Mensagem += processoNe.podeRedistribuir(dt, usuarioNe.getId_Serventia(), usuarioNe.getId_ServentiaCargo(), usuarioNe.getGrupoCodigo() );
			
			if (passoEditar == 1 || passoEditar == 2 || passoEditar == 3){
				movimentacaoProcessoDt.setCodigoTemp(dt.getId_Serventia());
				
				ServentiaDt serventiaDt = null;
				
				if (passoEditar == 1 || passoEditar == 2) {
					serventiaDt = processoNe.consultarServentiaPreprocessualRelacionada(processoDt.getId_Serventia());
					if (serventiaDt == null) Mensagem = ServentiaDt.MENSAGEM_AUSENCIA_SERVENTIA_RELACIONADA_CEJUSC;
				} else {
					serventiaDt = processoNe.consultarServentiaOrigemMovimentacaoAudienciaConciliacaoEMediacaoCEJUSC(dt.getId());
					if (serventiaDt == null) Mensagem = "Não foi localizada a serventia de origem do processo a partir da movimentação.";
				}
				
				
				if (serventiaDt != null) {
					redistribuicaoDt.setId_Serventia(serventiaDt.getId());
					redistribuicaoDt.setServentia(serventiaDt.getServentia());
					movimentacaoProcessoDt.setComplemento(" (" + serventiaDt.getServentia() + ")");
				}
			} 
			if (Mensagem.length() > 0) Mensagem += " \n ";
		}

		if (Mensagem.length() > 0) Mensagem = "Problema(s) ao redistribuir. Motivo(s): \n\n" + Mensagem;

		return Mensagem;
	}

	
	/**
	 * Verifica se um processo pode ser enviado para outra serventia
	 * @throws Exception 
	 */
	protected String verificaEnvio(String[] processos, ProcessoDt processoDt, RedistribuicaoDt redistribuicaoDt, ProcessoNe processoNe, UsuarioNe usuarioNe, int passoEditar, MovimentacaoProcessoDt movimentacaoProcessoDt) throws Exception{
		String Mensagem = "";

		this.consultaDadosProcessos(processos, redistribuicaoDt, processoDt, processoNe);
		List listaProcessos = redistribuicaoDt.getListaProcessos();

		for (int i = 0; i < listaProcessos.size(); i++) {
			ProcessoDt dt = (ProcessoDt) listaProcessos.get(i);
			
			Mensagem += processoNe.podeEncaminhar(dt, usuarioNe.getUsuarioDt(), null);
			
			if (Mensagem.length() > 0) Mensagem += " \n ";
		}

		if (Mensagem.length() > 0) Mensagem = "Erro ao encaminhar. Motivo(s): \n\n " + Mensagem;

		return Mensagem;
	}
	
	/**
	 * Consulta os dados básicos para cada processo a ser movimentado,
	 * para que possam ser efetuadas verificações para cada processo
	 * @throws Exception 
	 */
	protected void consultaDadosProcessos(String[] processos, RedistribuicaoDt redistribuicaoDt, ProcessoDt processoDt, ProcessoNe processoNe) throws Exception{
		if (processos != null) {
			if (processos.length == 1) {
				if (processoDt == null || processoDt.getId().equals("")) processoDt = processoNe.consultarIdCompleto(processos[0]);
				redistribuicaoDt.addListaProcessos(processoDt);
			} else {
				//Consulta dados básicos de cada processo e adiciona à lista
				for (int i = 0; i < processos.length; i++) {
					ProcessoDt obj = processoNe.consultarIdCompleto(processos[i]);
					redistribuicaoDt.addListaProcessos(obj);
				}
			}
		}
	}

	/**
	 * Monta tela de redistribuição
	 */
	protected void montaTelaRedistribuicao(String[] processos, HttpServletRequest request, int passoEditar){
		String titulo = "";
		
		if (passoEditar == 1) titulo = "Enviar Processo para Conciliação CEJUSC";
		else if (passoEditar == 2) titulo = "Enviar Processo para Mediação CEJUSC";
		else if (passoEditar == 3) titulo = "Retornar Processo da Conciliação/Mediação CEJUSC";
		else if (processos.length > 1) titulo = "Redistribuir Múltiplos Processos";
		else titulo = "Redistribuição de Processo";
		
		request.setAttribute("TituloPagina", titulo);		
	}

	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");

		// Limpa lista DWR e zera contador Pendências
		request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
		request.getSession().removeAttribute("ListaPendencias");
	}

	protected void obtemPorcentagemRepasse(HttpServletRequest request, RedistribuicaoDt redistribuicaoDt) throws Exception {
		if( redistribuicaoDt != null && redistribuicaoDt.getListaProcessos() != null && !redistribuicaoDt.getListaProcessos().isEmpty() ) {
			for (int i = 0; i < redistribuicaoDt.getListaProcessos().size(); i++) {
				ProcessoDt processoDt = (ProcessoDt) redistribuicaoDt.getListaProcessos().get(i);
				
				String porcentagemProcesso = "PorcentagemProcesso" + processoDt.getId();
				
				if( request.getParameter(porcentagemProcesso) != null && request.getParameter(porcentagemProcesso).length() > 0 ) {
					
					if( redistribuicaoDt.getListaIdProcessoPorcentagemRepasse() == null ) {
						redistribuicaoDt.setListaIdProcessoPorcentagemRepasse(new HashMap());
					}
					
					redistribuicaoDt.getListaIdProcessoPorcentagemRepasse().put(processoDt.getId(), request.getParameter(porcentagemProcesso).toString());
					request.getSession().setAttribute(porcentagemProcesso, request.getParameter(porcentagemProcesso).toString());
					
				}
				else {
					//Alteração para validar somente se for 1 não lote, se for lote e não for identificado, então seta 0%
					if( request.getParameter("validarMenuPorcentagem") != null && request.getParameter("validarMenuPorcentagem").toString().equals("SIM") ) {
						if( redistribuicaoDt.getListaProcessos().size() == 1 ) {
							throw new MensagemException("Por favor, informe o percentual de Repasse.");
						}
						else {
							if( request.getParameter(porcentagemProcesso) != null && request.getParameter(porcentagemProcesso).toString().isEmpty() ) {
								throw new MensagemException("Por favor, informe o percentual de Repasse para todos os processos do lote. A porcentagem para o processo " + processoDt.getProcessoNumeroCompleto() + " não foi informado.");
							}
						}
					}
					
					if( request.getParameter(porcentagemProcesso) != null ) {
						redistribuicaoDt.getListaIdProcessoPorcentagemRepasse().put(processoDt.getId(), request.getParameter(porcentagemProcesso).toString());
						request.getSession().setAttribute(porcentagemProcesso, "0");
					}
					
				}
			}
		}
	}
	
	/**
	 * Comentário adicionado dia 10/05/2019:
	 * Como não foi aberto ocorrências, então estou registrando através do email recebido nas conversas.
	 * 
	 * Após email entre a DSI e Gerenciamento da Maria de Fátima, foi confirmado que o menu de repasse voltará a ser aberto para todas as redistribuições.
	 * O usuário assim terá que escolher a porcentagem para cada processo redistribuido.
	 * 
	 * Segue cópia dos email do dia 02/05/2019: (Título do email: "Projudi - Repasse das Custas na Redistribuição")
	 * 
	 * (Email 02/05/2019)*******************************************************
		Maria de Fátima, bom dia!
		 
		Segue abaixo um resumo do que conversamos em sua sala, eu você e a Eliana. Gostaria que vocês validasses o meu entendimento, e me respondesse por e-mail para podermos ajustar o Projudi.

		Foi me explicado que existem várias situações que determinam se houve ou não uma citação em um processo, o que impossibilita fazermos esse procedimento automaticamente pela aplicação.
		
		Possíveis procedimentos que precisam ser realizados para registrar que houve uma citação:
		
		1 - Movimentação de Carta de Citação;
		2 - Movimentação de Mandado de Citação;
		3 - Citação por Edital;
		4 - Certidão de Citação;
		5 - Carta Precatória de Citação;
		6 - Carta Rogatória de Citação.
		
		Das possibilidades acima, existe somente a primeira implementada na aplicação, as demais não existem e quando forem desenvolvidas precisaríamos treinar e acompanhar o usuário.
		
		Com isso teremos que alterar o Projudi para deixar flexível a ponto do próprio escrivão determinar o índice de repasse, como é realizado atualmente pelo SPG:
		
		1 - Continuar sugerindo o percentual de repasse, mas deixar aberto para o usuário alterar;
		2 - Validar se o novo percentual de repasse informado pelo usuário é maior que o último repasse, caso seja o sistema deve emitir um alerta e solicitar que o usuário informe o percentual correto. Por exemplo, se o último repasse foi de 50%, o usuário não poderá informar 100%, caso isso ocorra o sistema emite o alerta e não faz a redistribuição;
		3 - Na tela de consulta de guias incluir um quadro informando todos os detalhes dos percentuais de repasse que foram realizados até a data atual. Contendo a serventia de origem, a serventia de destino e os percentuais de repasse.
		
		Favor confirmar se o meu entendimento está correto quanto aos três ações acima, após recebermos o "de acordo" iremos planejar a implementação.
	
	
		(Resposta dia 03/05/2019)*******************************************************
		Segue email de confirmação:
		
		Márcio,

		Eliana e eu lemos o seu relatório e anuímos com sua conclusão.
		
		Entendemos que, inicialmente, essa é a melhor saída, mas sugerimos a criação das outras "movimentações".
	 * 
	 * 
	 * @param String processos[]
	 * @param RedistribuicaoDt redistribuicaoDt
	 * @throws Exception
	 */
	protected void mostrarMenuPorcentagemRepasseProcesso(String processos[], RedistribuicaoDt redistribuicaoDt) throws Exception {
		//Verificar se é processo com custas e se tem guia inicial ou complementar
		if (processos != null && processos.length > 0 && redistribuicaoDt != null && redistribuicaoDt.getListaProcessos() != null ) {
			
//			MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
			
			List listaProcessos = redistribuicaoDt.getListaProcessos();
			
			boolean mostrarMenuPorcentagem = true;
			
			for (int i = 0; i < listaProcessos.size(); i++) {
				
				ProcessoDt p = (ProcessoDt) listaProcessos.get(i);
				
//				if( p!= null && p.getId() != null 
//						&& 
//						(
//							p.getId_Custa_Tipo() == null 
//							|| 
//							p.getId_Custa_Tipo().isEmpty() 
//							||
//							(
//								p.getId_Custa_Tipo() != null 
//								&& 
//								Funcoes.StringToInt(p.getId_Custa_Tipo()) == ProcessoDt.COM_CUSTAS
//							) 
//						) 
//					) 
//				{
//					if( new ProcessoNe().isGuiaInicial_GuiaComplementarPresente(p.getId()) ) {
//						mostrarMenuPorcentagem = true;
//					}
//					else {
//						mostrarMenuPorcentagem = false;
//					}
//				}
//				else {
//					mostrarMenuPorcentagem = false;
//				}
				
				//Ocorrência 2019/2169: Processo de segundo grau não solicita para informar porcentagem de repasse
				if( p.isSegundoGrau() /*|| p.isCriminal()*/ ) { //Ocorrência 2019/4268: Alterado para processo criminal
					mostrarMenuPorcentagem = false;
				}
				
				if( new ProcessoNe().isProcessoJuizadosTurmas(p.getId(), null) ) {
					mostrarMenuPorcentagem = false;
				}
				
				if( p != null && p.getId() != null && !p.getId().isEmpty() ) {
					
					if( mostrarMenuPorcentagem ) {
						redistribuicaoDt.getListaMostrarMenuPorcentagemRepasseProcesso().put(p.getId(), new Boolean(true));
//						redistribuicaoDt.getListaPorcentagemRepasseProcesso().put(p.getId(), movimentacaoNe.consultarMovimentacaoAnaliseRepasses(p.getId()) );
					}
					else {
						redistribuicaoDt.getListaMostrarMenuPorcentagemRepasseProcesso().put(p.getId(), new Boolean(false));
					}
				}
			}
			
		}
	}
}
