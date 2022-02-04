package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RedistribuicaoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que controla a redistribuição de processos em Lote.
 * Essa redistribuição pode ser feita com regra de distribuição (distribuição em uma área de varas)
 *  
 * @author lsbernardes
 *
 */
public class RedistribuicaoLoteCt extends Controle {

	private static final long serialVersionUID = -3980698554964936517L;
	String arquivado = "";
	String processos[] = null;

    public int Permissao() {
		return 734; //Permissão de nome "Redistribuição em Lote"
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		RedistribuicaoDt redistribuicaoDt;
		ProcessoNe processoNe;
		ProcessoDt processoDt = null;
		MovimentacaoProcessoDt movimentacaoProcessoDt;
		String posicaoPagina = "";
		
		String stNomeBusca1 = "";
		
		arquivado = "";
		processos = null;
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String Mensagem = "";
		int arquivarRedistribuir = 0;
		List tempList = null;
		String stAcao = "/WEB-INF/jsptjgo/ProcessoRedistribuicaoLoteLocalizar.jsp";

		request.setAttribute("tempPrograma", "RedistribuicaoLote");
		request.setAttribute("tempRetorno", "RedistribuicaoLote");
		request.setAttribute("TituloPagina", "Redistribuição de Processo em Lote");

		processoNe = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (processoNe == null) processoNe = new ProcessoNe();
		 
		movimentacaoProcessoDt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
		if (movimentacaoProcessoDt == null) movimentacaoProcessoDt = new MovimentacaoProcessoDt();

		redistribuicaoDt = (RedistribuicaoDt) request.getSession().getAttribute("Redistribuicaodt");
		if (redistribuicaoDt == null) redistribuicaoDt = new RedistribuicaoDt();
		
		
		// Pega valor digitado na caixa de paginação
		if (request.getParameter("PosicaoPagina") == null) posicaoPagina = PosicaoPaginaAtual;
		else posicaoPagina = String.valueOf((Funcoes.StringToInt(request.getParameter("PosicaoPagina"))) - 1);
		
		if (request.getParameter("Arquivado") != null)
			arquivado = request.getParameter("Arquivado"); 
		else 
			arquivado = "false";
		
		//Fluxo
		if (request.getParameter("arquivarRedistribuir") == null || request.getParameter("arquivarRedistribuir").toString().length() == 0 ) {
			if (request.getSession().getAttribute("arquivarRedistribuir") != null)	
				arquivarRedistribuir = Integer.parseInt(request.getSession().getAttribute("arquivarRedistribuir").toString());
		} else
			arquivarRedistribuir = Integer.parseInt(request.getParameter("arquivarRedistribuir"));

		redistribuicaoDt.setId_AreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
		redistribuicaoDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		redistribuicaoDt.setId_Serventia(request.getParameter("Id_Serventia"));
		redistribuicaoDt.setServentia(request.getParameter("Serventia"));
		
		redistribuicaoDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		//Esse if foi criado para apresentar o nome e o cargo do novo responsável pelo processo.
		//Sem essa consulta só é possível apresentar o cargo devido a forma como as informações voltam da tela JSON.
		if(redistribuicaoDt.getId_ServentiaCargo() != null && !redistribuicaoDt.getId_ServentiaCargo().equals("")){
			ServentiaCargoDt servCargoDt = new ServentiaCargoNe().consultarId(redistribuicaoDt.getId_ServentiaCargo());
			redistribuicaoDt.setServentiaCargo(servCargoDt.getServentiaCargo() + " - " +  servCargoDt.getNomeUsuario());
		}
		
		redistribuicaoDt.setPorcentagemRepasse(request.getParameter("Porcentagem"));
		redistribuicaoDt.setId_Classificador(request.getParameter("Id_Classificador"));
		redistribuicaoDt.setClassificador(request.getParameter("Classificador"));
		redistribuicaoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		redistribuicaoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		redistribuicaoDt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		redistribuicaoDt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		redistribuicaoDt.setOpcaoRedistribuicao(request.getParameter("opcaoRedistribuicao"));
		redistribuicaoDt.setRedistribuirTodosProcessoClassificado(request.getParameter("qtdRegistros"));

		request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));

		request.setAttribute("PaginaAnterior", paginaatual);
		// Mensagem de sucesso
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		// Mensagem de erro
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			//Inicializa redistribuição
			case Configuracao.Novo:

				// Captura os processos que serão movimentados, se for o caso de Movimentação em Lote
				if (request.getSession().getAttribute("processos") != null) 
					processos = (String[]) request.getSession().getAttribute("processos");

				if (processos != null && processos.length > 0) {
					this.consultaDadosProcessos(processos, UsuarioSessao.getUsuarioDt(), redistribuicaoDt, processoDt, processoNe);
					montaTelaRedistribuicao(processos, request);
					stAcao = "/WEB-INF/jsptjgo/RedistribuicaoLote.jsp";
				} 
				break;
			
			case Configuracao.Salvar: 
				
				//Se o usuário tentar redistribuir pela opção 3, deverá informar a área de distribuição e serventia OBRIGATORIAMENTE  
				if(redistribuicaoDt.getOpcaoRedistribuicao() != null && redistribuicaoDt.getOpcaoRedistribuicao().equals("3")) {
					if (redistribuicaoDt.getId_AreaDistribuicao() == null || redistribuicaoDt.getId_AreaDistribuicao().equals("")
							|| redistribuicaoDt.getId_Serventia() == null || redistribuicaoDt.getId_Serventia().equals("")) { 
						request.setAttribute("MensagemErro", "Na opção 3 é preciso informar a Área de Distribuição e Serventia.");
						break;
					}
				}
				
				if(Funcoes.StringToBoolean(request.getParameter("qtdeRegistros"))) {
					request.setAttribute("MensagemConfirmacao", "Confirma Redistribuição de todos processo(s) da serventia?");
				}
				
				if (movimentacaoProcessoDt != null && movimentacaoProcessoDt.getListaArquivos() != null	&& movimentacaoProcessoDt.getListaArquivos().size() > 0){
					stAcao = "/WEB-INF/jsptjgo/RedistribuicaoLote.jsp";
					if (arquivarRedistribuir == 1){
						request.setAttribute("TituloPagina", "Redistribuir e Arquivar Múltiplos Processos");
						request.setAttribute("MensagemConfirmacao", "Confirma Arquivamento e Redistribuição do(s) processo(s)?");
					} else{
						request.setAttribute("MensagemConfirmacao", "Confirma Redistribuição do(s) processo(s)?");
					}
				} else {
					request.setAttribute("MensagemErro", "Dados da movimetação não encontrados!");
				}
				
				break;
				
			case Configuracao.SalvarResultado:
				
				if (arquivarRedistribuir == 1){
					Mensagem = this.verificaRedistribuicao(redistribuicaoDt, processoNe, UsuarioSessao.getUsuarioDt());
					if (Mensagem.length() == 0) {
						processoNe.salvarRedistribuicao(redistribuicaoDt, UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt, false,  true);
						request.setAttribute("MensagemOk", "Arquivamento e Redistribuição de processo(s) efetuada com sucesso.");
						redistribuicaoDt.limpar();
						LimparSessao(request.getSession());
					} else request.setAttribute("MensagemErro", Mensagem);
					
				} else if (arquivarRedistribuir == 0){
					
					Mensagem = this.verificaRedistribuicaoDistribuidorCivel(redistribuicaoDt, processoNe, UsuarioSessao.getUsuarioDt());
					if (Mensagem.length() == 0) {
						processoNe.salvarRedistribuicao(redistribuicaoDt, UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt, false, false);
						request.setAttribute("MensagemOk", "Redistribuição de processo(s) efetuada com sucesso.");
						redistribuicaoDt.limpar();
						LimparSessao(request.getSession());
					} else request.setAttribute("MensagemErro", Mensagem);
					
				}
				
				//Dados movimentacao
				movimentacaoProcessoDt.limpar();
				limparListas(request);
				arquivarRedistribuir = 0;
				request.getSession().removeAttribute("Movimentacaodt");
				request.getSession().removeAttribute("arquivarRedistribuir");
				request.getSession().removeAttribute("redistribuicaoDt");
				request.setAttribute("PosicaoPaginaAtual", 1L);
				request.setAttribute("QuantidadePaginas", 1L);
				request.setAttribute("PosicaoPagina", 1L);
				
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
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao", "AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma", "AreaDistribuicao");
					request.setAttribute("tempRetorno", "RedistribuicaoLote");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Novo);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					
					redistribuicaoDt.setId_Serventia("null");
					redistribuicaoDt.setServentia("");
					redistribuicaoDt.setId_ServentiaCargo("null");
					redistribuicaoDt.setServentiaCargo("");
				}else{
					String stTemp = "";
					stTemp = processoNe.consultarDescricaoAreasDistribuicaoAtivaJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);						
					return;
				}
			break;
				
			//Consultar Serventias
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "RedistribuicaoLote");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Novo);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					
					redistribuicaoDt.setId_ServentiaCargo("null");
					redistribuicaoDt.setServentiaCargo("");
				}else{
					String stTemp = "";
					if(redistribuicaoDt.getId_AreaDistribuicao() == null || redistribuicaoDt.getId_AreaDistribuicao().isEmpty()) {
						stTemp = processoNe.consultarServentiasAtivasRedistribuicaoLoteJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao);
					} else {
						stTemp = processoNe.consultarServentiasAtivasAreaDistribuicaoJSON(stNomeBusca1, redistribuicaoDt.getId_AreaDistribuicao(), PosicaoPaginaAtual);
					}
					enviarJSON(response, stTemp);
					return;
				}
			break;
				
//			// Consultar juizes responsáveis pelo processo
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				String idServentiaSelecionada = "";
			    ServentiaDt serventiaDt = null;
				
				if (UsuarioSessao.isDistribuidor()){
					idServentiaSelecionada = redistribuicaoDt.getId_Serventia();
				} else {
					idServentiaSelecionada = UsuarioSessao.getUsuarioDt().getId_Serventia();
				}
				
				if (idServentiaSelecionada != null && idServentiaSelecionada.length()>0){
					serventiaDt =  processoNe.consultarServentia(idServentiaSelecionada);
				}  else {
					request.setAttribute("MensagemErro", "Selecione uma Serventia.");
					return;
				}
			
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
					String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "RedistribuicaoLote");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Novo);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					
					if(serventiaDt.isTurma()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaDt.getId(), String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU), String.valueOf(ServentiaSubtipoDt.TURMA_RECURSAL_CIVEL));
					} else if(serventiaDt.isUpjFamilia()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaDt.getId(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_FAMILIA)); 
					} else if(serventiaDt.isUpjSucessoes()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaDt.getId(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_SUCESSOES)); 
					} else if(serventiaDt.isUpjCriminal()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaDt.getId(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_CRIMINAL));
					} else if(serventiaDt.isUpjViolenciaDomestica()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA));							
					} else if(serventiaDt.isUpjCustodia()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaDt.getId(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_CUSTODIA));	
					} else if(serventiaDt.isUpjTurmaRecursal()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, redistribuicaoDt.getId_Serventia(), String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU), String.valueOf(ServentiaSubtipoDt.UPJ_TURMA_RECURSAL));	
					} else if(serventiaDt.isUpjJuizadoEspecialFazendaPublica()) {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaDt.getId(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA));	
					} else {
						stTemp = processoNe.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaDt.getId(), String.valueOf(ServentiaTipoDt.VARA), String.valueOf(ServentiaSubtipoDt.VARAS_CIVEL));
					}
						
					enviarJSON(response, stTemp);
					
					return;
				}
				break;

				
				// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				String id_serventia = UsuarioSessao.getUsuarioDt().getId_Serventia();
				if (UsuarioSessao.isDistribuidor()){
					id_serventia= redistribuicaoDt.getId_Serventia();
				}
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
									
					String stPermissao = String.valueOf((ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					
					atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", "RedistribuicaoLote", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
					
					break;
				} else{
					String stTemp="";
					stTemp = processoNe.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, id_serventia);
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				
				//Consultar Processo Tipo
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoTipo"};
					String[] lisDescricao = {"ProcessoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoTipo");
					request.setAttribute("tempBuscaDescricao","ProcessoTipo");
					request.setAttribute("tempBuscaPrograma","Tipo de Processo");			
					request.setAttribute("tempRetorno","RedistribuicaoLote");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = processoNe.consultarDescricaoProcessoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
				
			case Configuracao.Curinga6:
				if (request.getParameter("processos") != null) {
					processos = null;
					processos = request.getParameterValues("processos");
				}
				
				
				if(Funcoes.StringToBoolean(request.getParameter("qtdRegistros"))) {
					if(redistribuicaoDt.getId_Classificador() == null || redistribuicaoDt.getId_Classificador().isEmpty()) {
						request.setAttribute("MensagemErro", "Para redistribuir TODOS os processos, o Classificador é obrigatório.");
						break;
					}
					
					consultaDadosProcessos(processos, UsuarioSessao.getUsuarioDt(), redistribuicaoDt, processoDt, processoNe);
				}
				
				if ((processos != null && processos.length > 0)) {
					//Antes de redirecionar para o MovimentacaoCt deve limpar as variáveis da tela para nao chegarem
					//preenchidas no último passo da redistribuicao
					redistribuicaoDt.limpar();
					
					request.getSession().setAttribute("processos", processos);
					request.getSession().setAttribute("AcessoOutraServentia", "1");
					redireciona(response, "Movimentacao?PaginaAtual=4&RedirecionaOutraServentia=5");
						
				} else {
					
					if (redistribuicaoDt.getId_Serventia() != null && redistribuicaoDt.getId_Serventia().length()>0){
						tempList = processoNe.consultarProcesssosRedistribuicaoLote(redistribuicaoDt.getId_Serventia(), redistribuicaoDt.getId_ServentiaCargo(), redistribuicaoDt.getId_ProcessoTipo(), arquivado, redistribuicaoDt.getId_Classificador(), PosicaoPaginaAtual);
						stAcao = "/WEB-INF/jsptjgo/ProcessoRedistribuicaoLoteLocalizar.jsp";
						if (tempList != null && tempList.size() > 0) {
							request.setAttribute("ListaProcesso", tempList);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
							request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());
						}
						request.setAttribute("MensagemErro", "Nenhum Processo Foi Selecionado.");
					
					} else					
						request.setAttribute("MensagemErro", "Nenhum Processo Foi Selecionado.");
				}
				break;

			default:
				
				if (UsuarioSessao.isDistribuidor()){
					if (redistribuicaoDt.getId_Serventia() != null && redistribuicaoDt.getId_Serventia().length()>0){
						if (request.getParameter("PassoEditar") != null && request.getParameter("PassoEditar").equals("2")){
							tempList = processoNe.consultarProcesssosRedistribuicaoLote(redistribuicaoDt.getId_Serventia(), redistribuicaoDt.getId_ServentiaCargo(), redistribuicaoDt.getId_ProcessoTipo(), arquivado, redistribuicaoDt.getId_Classificador(), PosicaoPaginaAtual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
							request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());
							stAcao = "/WEB-INF/jsptjgo/ProcessoRedistribuicaoLoteLocalizar.jsp";
							
							if (tempList != null) {
								if (tempList.size() > 0) {
									request.setAttribute("ListaProcesso", tempList);
								} else request.setAttribute("MensagemErro", "Nenhum Processo foi localizado para os parâmetros informados.");
							}	
						} else					
							stAcao = "/WEB-INF/jsptjgo/ProcessoRedistribuicaoLoteLocalizar.jsp";
						
					} else if (request.getParameter("PassoEditar") != null && request.getParameter("PassoEditar").equals("2")){
						request.setAttribute("MensagemErro", "Selecione uma Serventia.");
					} 
				} else {
					if (request.getParameter("PassoEditar") != null && request.getParameter("PassoEditar").equals("2")){
						tempList = processoNe.consultarProcesssosRedistribuicaoLote(UsuarioSessao.getUsuarioDt().getId_Serventia(), redistribuicaoDt.getId_ServentiaCargo(), redistribuicaoDt.getId_ProcessoTipo(), arquivado, redistribuicaoDt.getId_Classificador(), PosicaoPaginaAtual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
						request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());
						stAcao = "/WEB-INF/jsptjgo/ProcessoRedistribuicaoLoteLocalizar.jsp";
						
						if (tempList != null) {
							if (tempList.size() > 0) {
								request.setAttribute("ListaProcesso", tempList);
							} else request.setAttribute("MensagemErro", "Nenhum Processo foi localizado para os parâmetros informados.");
						}	
					} else					
						stAcao = "/WEB-INF/jsptjgo/ProcessoRedistribuicaoLoteLocalizar.jsp";
				}
				
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPagina));
				request.setAttribute("QuantidadePaginas", processoNe.getQuantidadePaginas());
				request.setAttribute("PosicaoPagina", Funcoes.StringToLong(posicaoPagina)+1);
				
				String stId = request.getParameter("Id_Processo");
				// Se foi passado Id_Processo efetua consulta e redireciona para tela de Dados do Processo
				if (stId != null && stId.length()>0)
					redireciona(response, "BuscaProcesso?Id_Processo=" +stId);
				
				break;
		}

		request.getSession().setAttribute("Redistribuicaodt", redistribuicaoDt);
		request.getSession().setAttribute("Processone", processoNe);
		request.getSession().setAttribute("arquivarRedistribuir", arquivarRedistribuir);
		request.setAttribute("Arquivado", arquivado);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Verifica se um processo pode ser redistribuído
	 * @throws Exception 
	 */
	private String verificaRedistribuicao(RedistribuicaoDt redistribuicaoDt, ProcessoNe processoNe, UsuarioDt usuarioDt) throws Exception{
		String Mensagem = "";
		List listaProcessos = redistribuicaoDt.getListaProcessos();

		for (int i = 0; i < listaProcessos.size(); i++) {
			ProcessoDt dt = (ProcessoDt) listaProcessos.get(i);
			Mensagem += processoNe.podeRedistribuir(dt, usuarioDt.getId_Serventia(), usuarioDt.getId_ServentiaCargo(), usuarioDt.getGrupoCodigo());			
		}

		if (Mensagem.length() > 0) Mensagem = "Erro ao redistribuir. Motivo(s): " + Mensagem;

		return Mensagem;
	}
	
	/**
	 * Verifica se um processo pode ser redistribuído quando trantado-se do Distribuidor Cível
	 * @throws Exception 
	 */
	protected String verificaRedistribuicaoDistribuidorCivel(RedistribuicaoDt redistribuicaoDt, ProcessoNe processoNe, UsuarioDt usuarioDt) throws Exception{
		String Mensagem = "";
		List listaProcessos = redistribuicaoDt.getListaProcessos();

		for (int i = 0; i < listaProcessos.size(); i++) {
			ProcessoDt dt = (ProcessoDt) listaProcessos.get(i);
			if (processoNe.verificarProcessoHibrido(dt.getId()) && !GrupoDt.isDistribuidor(usuarioDt.getGrupoCodigo()) ){//B.O 2019/9175 e 2020/8523
				Mensagem += "Este processo "+ dt.getProcessoNumeroCompleto() +" não poderá ser redistribuído, em conformidade ao Decreto 1.374/2019 artigo 5º. \n";
			}
		}

		if (Mensagem.length() > 0) Mensagem = "Erro ao redistribuir. Motivo(s): " + Mensagem;

		return Mensagem;
	}

	/**
	 * Consulta os dados básicos para cada processo a ser movimentado,
	 * para que possam ser efetuadas verificações para cada processo
	 * @throws Exception 
	 */
	private void consultaDadosProcessos(String[] processosSelecionados, UsuarioDt usuarioDt, RedistribuicaoDt redistribuicaoDt, ProcessoDt processoDt, ProcessoNe processoNe) throws Exception{
		if(redistribuicaoDt.isRedistribuirTodosProcessosClassificados()) {
			List listaProcessos = processoNe.consultarProcesssosRedistribuicaoLote(redistribuicaoDt.getId_Serventia(), redistribuicaoDt.getId_ServentiaCargo(), redistribuicaoDt.getId_ProcessoTipo(), arquivado, redistribuicaoDt.getId_Classificador(),null);
			for (int i = 0; i < listaProcessos.size(); i++) {
				ProcessoDt processo = (ProcessoDt) listaProcessos.get(i);
				redistribuicaoDt.addListaProcessos(processo);
				if(processos == null) {
					processos = new String[listaProcessos.size()];
				}
				processos[i] = processo.getId();
			}
		}else {
			if (processosSelecionados != null) {
				if (processosSelecionados.length == 1) {
					if (processoDt == null || processoDt.getId().equals("")){
						if (!redistribuicaoDt.isListaProcesso(processosSelecionados[0])){
							processoDt = processoNe.consultarId(processosSelecionados[0]);
							processoDt.setServentiaCargoResponsavelDt(processoNe.consultarResponsavelProcesso(processoDt.getId(), usuarioDt.getGrupoTipoCodigo(), usuarioDt.getServentiaTipoCodigo(), usuarioDt.getServentiaSubtipoCodigo()));
							redistribuicaoDt.addListaProcessos(processoDt);
						}
					}else if (!redistribuicaoDt.isListaProcesso(processoDt.getId())){
						redistribuicaoDt.addListaProcessos(processoDt);
					}
					
				} else {
					//Consulta dados básicos de cada processo e adiciona à lista
					for (int i = 0; i < processosSelecionados.length; i++) {	
						if (!redistribuicaoDt.isListaProcesso(processosSelecionados[i])){
							ProcessoDt obj = processoNe.consultarId(processosSelecionados[i]);
							obj.setServentiaCargoResponsavelDt(processoNe.consultarResponsavelProcesso(obj.getId(), usuarioDt.getGrupoTipoCodigo(), usuarioDt.getServentiaTipoCodigo(), usuarioDt.getServentiaSubtipoCodigo()));
							redistribuicaoDt.addListaProcessos(obj);
						}
					}
				}
			}
		}
	}

	/**
	 * Monta tela de redistribuição
	 */
	private void montaTelaRedistribuicao(String[] processos, HttpServletRequest request){

		if (processos.length > 1) request.setAttribute("TituloPagina", "Redistribuir e Arquivar Múltiplos Processos");
		else request.setAttribute("TituloPagina", "Redistribuir Processo");

	}

	private void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");

		// Limpa lista DWR e zera contador Pendências
		request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
		request.getSession().removeAttribute("ListaPendencias");
	}
	

}
