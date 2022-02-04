package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PreAnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet para controlar as análises de autos conclusos.
 * Se uma conclusão possuir uma pré-análise registrada, essa será disponibilizada para juiz finalizar
 * 
 * @author msapaula
 */
public class AnalisarConclusaoCt extends Controle {

    private static final long serialVersionUID = 8652459075296978892L;

    public int Permissao() {
		return AnaliseConclusaoDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		AnaliseConclusaoDt analisePendenciaDt;
		MovimentacaoNe Movimentacaone;
		ProcessoEncaminhamentoDt processoEncaminhamentoDt;

		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		int tempFluxo1 = -1;

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").toString().equalsIgnoreCase("null")) 
			tempFluxo1 = Funcoes.StringToInt(request.getParameter("tempFluxo1"));
		
		//----------------------------------------------------------------------------------------------------------------------
		processoEncaminhamentoDt = (ProcessoEncaminhamentoDt) request.getSession().getAttribute("processoEncaminhamentoDt");
		if (processoEncaminhamentoDt == null) processoEncaminhamentoDt = new ProcessoEncaminhamentoDt();
		
		
		if(request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").equals(""))  
			processoEncaminhamentoDt.setIdServentia(request.getParameter("Id_Serventia"));
		
		if(request.getParameter("Serventia") != null && !request.getParameter("Serventia").equals("")) 
			processoEncaminhamentoDt.setServentia(request.getParameter("Serventia"));
		
		if(request.getParameter("Id_AreaDistribuicao") != null && !request.getParameter("Id_AreaDistribuicao").equals("")){ 
			processoEncaminhamentoDt.setIdAreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			processoEncaminhamentoDt.setIdServentia("");
			processoEncaminhamentoDt.setServentia("");
		}
		
		if(request.getParameter("AreaDistribuicao") != null && !request.getParameter("AreaDistribuicao").equals("")) 
			processoEncaminhamentoDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		
		//------------------------------------------------------------------------------------------------------------------------
		
		
		
		String Mensagem = "";
		int paginaAnterior = 0;
		int passoEditar = -1;
		String pendencias[] = null;
		String stAcao = "/WEB-INF/jsptjgo/AnalisarConclusao.jsp";
		request.setAttribute("tempPrograma", "Autos Conclusos");
		request.setAttribute("tempRetorno", "AnalisarConclusao");
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();

		if (request.getSession().getAttribute("AnalisePendenciadt") instanceof AnaliseConclusaoDt)
			analisePendenciaDt = (AnaliseConclusaoDt) request.getSession().getAttribute("AnalisePendenciadt");
		else analisePendenciaDt = new AnaliseConclusaoDt();

		analisePendenciaDt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));
		analisePendenciaDt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		analisePendenciaDt.setComplementoMovimentacao(request.getParameter("MovimentacaoComplemento"));
		analisePendenciaDt.setClassificador(request.getParameter("Classificador"));
		analisePendenciaDt.setId_Classificador(request.getParameter("Id_Classificador"));
		analisePendenciaDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		analisePendenciaDt.setNomeArquivo(request.getParameter("nomeArquivo"));
		analisePendenciaDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		analisePendenciaDt.setId_Modelo(request.getParameter("Id_Modelo"));
		analisePendenciaDt.setModelo(request.getParameter("Modelo"));
		analisePendenciaDt.setTextoEditor(request.getParameter("TextoEditor"));
		
		analisePendenciaDt.setId_TipoPendencia(request.getParameter("tipo"));
		
		if (request.getParameter("julgadoMerito") == null && request.getParameter("fluxo") != null && request.getParameter("fluxo").toString().equals("1"))
			analisePendenciaDt.setJulgadoMeritoProcessoPrincipal("false");
		else if (request.getParameter("julgadoMerito") != null)
			analisePendenciaDt.setJulgadoMeritoProcessoPrincipal(request.getParameter("julgadoMerito"));
		
		analisePendenciaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		analisePendenciaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		if (analisePendenciaDt.getListaPendenciasFechar() != null && analisePendenciaDt.getListaPendenciasFechar().size() > 1) request.setAttribute("TituloPagina", "Analisar Múltiplos Autos Conclusos");
		else request.setAttribute("TituloPagina", "Analisar Autos Conclusos");

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));		
		
		//Setando váriavel para não gerar automaticamente a pendência verificar processo
		if (request.getParameter("naoGerarVerificarProcesso") != null && request.getParameter("naoGerarVerificarProcesso").equals("true")) analisePendenciaDt.setNaoGerarVeficarProcesso(true);
		
		//Setando váriavel para verifcar se o pedido de assistência foi deferido ou não
		if (request.getParameter("PedidoAssistencia") != null && request.getParameter("PedidoAssistencia").equals("1")) {
			analisePendenciaDt.setPedidoAssistencia("1");
		} else if (request.getParameter("PedidoAssistencia") != null && request.getParameter("PedidoAssistencia").equals("0")) {
			analisePendenciaDt.setPedidoAssistencia("0");
		} else if (request.getParameter("PedidoAssistencia") != null && request.getParameter("PedidoAssistencia").equals("2")) {
			analisePendenciaDt.setPedidoAssistencia("2");
		} 
		
		setParametrosAuxiliares(request, analisePendenciaDt, paginaatual, paginaAnterior, passoEditar, UsuarioSessao, Movimentacaone);

		int fluxo = analisePendenciaDt.getFluxo();
		String tipoConclusao = analisePendenciaDt.getId_PendenciaTipo();
		boolean fluxoPreAnalise = analisePendenciaDt.isPreAnalise();
		
		if (UsuarioSessao.getUsuarioDt().isGrupoTipoCodigoDeAutoridade()){
			request.setAttribute("SegundoGrau", "true");
		}

		switch (paginaatual) {

			// Inicializa análise de autos conclusos
			case Configuracao.Novo:

				// Captura as pendências que serão fechadas
				if (request.getParameter("pendencias") != null) pendencias = request.getParameterValues("pendencias");
				else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")) pendencias = new String[] {request.getParameter("Id_Pendencia") };

				if (pendencias != null && pendencias.length > 0) {
					if (pendencias.length > 1) {//Análise múltipla
						
						if(UsuarioSessao.isGabinetePresidenciaTjgo() || UsuarioSessao.isGabineteVicePresidenciaTjgo()) {
							throw new MensagemException("Acesso bloqueado. A pré-análise múltipla está sendo desenvolvida para as serventias UPJ e não é possível realizar a tarefa por enquanto.");
						}
						
						analisePendenciaDt = new AnaliseConclusaoDt();
						montarTelaAnaliseMultipla(request, analisePendenciaDt, pendencias, null, UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoConclusao, UsuarioSessao);
					} else {
						//Verifica se existe uma pré-analise para a conclusão selecionada
						analisePendenciaDt = Movimentacaone.getPreAnaliseConclusao(pendencias[0]);

						if (analisePendenciaDt == null) {
							analisePendenciaDt = new AnaliseConclusaoDt();
						} else{ 
							setarPreAnalise(request, analisePendenciaDt, Movimentacaone, UsuarioSessao.getUsuarioDt());							
						}

						//Montar tela pré-analise simples
						montarTelaAnaliseSimples(request, analisePendenciaDt, pendencias[0], UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoConclusao, UsuarioSessao);
					}
				} else {
					request.setAttribute("MensagemErro", "Nenhuma Conclusão foi selecionada.");
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				break;

			case Configuracao.Salvar:
				stAcao = "/WEB-INF/jsptjgo/AnalisarConclusaoConfirmacao.jsp";
				analisePendenciaDt.setListaPendenciasGerar(getListaPendencias(request)); // Captura lista de pendências
				analisePendenciaDt.setListaArquivos(getListaArquivos(request)); // Captura lista de arquivos
				analisePendenciaDt.setPasso2("Passo 2 OK");
				analisePendenciaDt.setPasso3("Passo 3");
				break;

			// Salvando Análise de Conclusões
			case Configuracao.SalvarResultado:
				if (tempFluxo1 == 1 || tempFluxo1 == 2) {
					Mensagem = Movimentacaone.verificarPreAnaliseConclusao(analisePendenciaDt, request.getParameter("IdProcessoValidacaoAba"), UsuarioSessao);
					if (Mensagem.length() == 0) {
						analisePendenciaDt.setPendenteAssinatura((tempFluxo1 == 2));
						Movimentacaone.salvarPreAnaliseConclusao(analisePendenciaDt, UsuarioSessao.getUsuarioDt());

						//Quando trata de despacho múltiplo
						if (analisePendenciaDt.isMultipla()) Mensagem = "Pré-Análise Múltipla registrada com sucesso.";
						else Mensagem = "Pré-Análise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar());

						// Limpa da sessão os atributos
						analisePendenciaDt.limpar();
						request.getSession().removeAttribute("__Pedido__");
						request.getSession().removeAttribute("exibePendenciaAssinatura");
						limparListas(request);
						
						if (analisePendenciaDt.isPreAnalise()) 
							redireciona(response, "PreAnalisarConclusao?PaginaAtual=" + Configuracao.Curinga6 + "&tipo=" + analisePendenciaDt.getId_PendenciaTipo() + "&MensagemOk=" + Mensagem);
						else  												
							redireciona(response, "AnalisarConclusao?PaginaAtual=" + Configuracao.Localizar + "&tipo=" + analisePendenciaDt.getId_PendenciaTipo() + "&MensagemOk=" + Mensagem);
						return;

					} else request.setAttribute("MensagemErro", Mensagem);
				} else {
					Mensagem = Movimentacaone.verificarAnaliseConclusao(analisePendenciaDt, UsuarioSessao);
					if (Mensagem.length() == 0) {
						Mensagem = Movimentacaone.podeAnalisarConclusao(analisePendenciaDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo());
						if (Mensagem.length() == 0) {
							Movimentacaone.salvarAnaliseConclusao(analisePendenciaDt, UsuarioSessao.getUsuarioDt());

							//Quando trata de despacho múltiplo
							if (analisePendenciaDt.isMultipla()) Mensagem = "Análise Múltipla registrada com sucesso.";
							else Mensagem = "Análise efetuada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar());

							request.setAttribute("MensagemOk", Mensagem);
							// Limpa da sessão os atributos
							analisePendenciaDt.limpar();
							limparListas(request);

							if (analisePendenciaDt.isPreAnalise()) 
								redireciona(response, "PreAnalisarConclusao?PaginaAtual=" + fluxo + "&MensagemOk=" + Mensagem);
							else 
								this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
							return;
						} else request.setAttribute("MensagemErro", Mensagem);
					} else request.setAttribute("MensagemErro", Mensagem);	
				}
				break;
			
			//Prepara dados para Descartar Análise
			case Configuracao.Excluir:
				PendenciaDt pendenciaDt = null;
				if (request.getParameter("Id_Pendencia") != null && request.getParameter("Id_Pendencia").trim().length() > 0) {
					pendenciaDt = Movimentacaone.consultarPendenciaId(request.getParameter("Id_Pendencia"));					
				} else if (analisePendenciaDt.getPrimeiraPendenciaListaFechar() != null) {
					pendenciaDt = Movimentacaone.consultarPendenciaId(analisePendenciaDt.getPrimeiraPendenciaListaFechar().getId());
				}
				analisePendenciaDt.setPendenciaDt(pendenciaDt);
				request.setAttribute("Mensagem", "Deseja descartar conclusão?");
				stAcao = "/WEB-INF/jsptjgo/VisualizarPendenciaDescartar.jsp";
				break;

			//Função utilizada para Descartar Análise
			case Configuracao.ExcluirResultado:
				//este teste é necessário pois existe as várias abas
				// e no caso de clicar na pagina inicial o objeto na sessão será limpo
				if (analisePendenciaDt.isTemPendencia()){
					Movimentacaone.descartarAnalise(analisePendenciaDt.getPendenciaDt(), UsuarioSessao.getUsuarioDt());
					request.setAttribute("MensagemOk", "Conclusão descartada com sucesso.");
	
					// Limpa da sessão os atributos
					analisePendenciaDt.limpar();
	
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				break;
			// Localiza autos conclusos para análise
			case Configuracao.Localizar:
				consultarConclusao(request, analisePendenciaDt, Movimentacaone, UsuarioSessao, passoEditar, fluxo);
				stAcao = "/WEB-INF/jsptjgo/ConclusaoLocalizar.jsp";
				analisePendenciaDt.setFluxo(paginaatual);
				analisePendenciaDt.limpar();
				break;

			//Tratando pré-análise múltipla
			case Configuracao.Curinga6:

				//Se um arquivo de pré-análise foi passado, verificar quais pendências ele está vinculado
				String id_Arquivo = request.getParameter("Id_Arquivo");

				if (id_Arquivo != null) {
					//Busca pendências vinculadas ao arquivo
					List pendenciasFechar = Movimentacaone.consultarPendencias(id_Arquivo);

					if (pendenciasFechar!=null && pendenciasFechar.size()>0){
						//Busca arquivo de pré-análise para primeira pendência vinculada
						PendenciaDt primeiraPendencia = (PendenciaDt) pendenciasFechar.get(0);
						PendenciaArquivoDt pendenciaArquivoDt = Movimentacaone.getArquivoPreAnaliseConclusao(primeiraPendencia.getId());
	
						//Se encontrou uma pré-analise para a pendência, compara se é o mesmo arquivo passado
						if (pendenciaArquivoDt != null && pendenciaArquivoDt.getArquivoDt().getId().equals(id_Arquivo)) {
							//Recupera dados da pré-analise
							analisePendenciaDt = Movimentacaone.getPreAnaliseConclusao(pendenciaArquivoDt);
							
							//Seta tipos de pendências que poderão ser geradas
							analisePendenciaDt.setListaPendenciaTipos(Movimentacaone.consultarTiposPendenciaMovimentacao(UsuarioSessao.getUsuarioDt()));
							
							//Monta tela para análise das pré-analises multiplas
							montarTelaAnaliseMultipla(request, analisePendenciaDt, null, pendenciasFechar, UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoConclusao, UsuarioSessao);
							
							if (primeiraPendencia != null && primeiraPendencia.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA) {
								analisePendenciaDt.setVisualizarPedidoAssistencia(true);
							}
						}
					}
				}
				break;

			//Consulta de conclusões finalizadas
			case Configuracao.Curinga7:
				//Se fluxo anterior é esse, deve fazer a consulta
				if (paginaAnterior == Configuracao.Curinga7){
					consultarConclusoesFinalizadas(request, analisePendenciaDt, UsuarioSessao.getUsuarioDt().getId_ServentiaCargo(), Movimentacaone);
				}
				else {
					analisePendenciaDt = new AnaliseConclusaoDt();
					analisePendenciaDt.setFluxo(paginaatual);
				}
				stAcao = "/WEB-INF/jsptjgo/ConclusoesFinalizadas.jsp";
				break;

			// Consultar Tipos de Movimentação
			case (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"MovimentacaoTipo"};
					String[] lisDescricao = {"MovimentacaoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MovimentacaoTipo");
					request.setAttribute("tempBuscaDescricao","MovimentacaoTipo");
					request.setAttribute("tempBuscaPrograma","MovimentacaoTipo");			
					request.setAttribute("tempRetorno","AnalisarConclusao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Movimentacaone.consultarGrupoMovimentacaoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;

			// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador","Prioridade","Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Classificador");
					request.setAttribute("tempBuscaDescricao","Classificador");
					request.setAttribute("tempBuscaPrograma","Classificador");			
					request.setAttribute("tempRetorno","AnalisarConclusao");		
					request.setAttribute("tempDescricaoId","Id");
					//Esse valor 2 parao parâmetro consultaClassificador é para indicar que está sendo feita uma análise e o
					//usuário está na tela de localizar as análises a serem realizadas
					if(request.getParameter("consultaClassificador") != null && request.getParameter("consultaClassificador").equals("2")) {
						request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Localizar));
					} else {
						request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
						if (paginaAnterior == Configuracao.Editar) {
							request.setAttribute("PassoEditar", "1");
						}
						else {
							request.setAttribute("PaginaAtual", analisePendenciaDt.getFluxo());
						}
					}
					request.setAttribute("PaginaAtual", (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Movimentacaone.consultarClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;

			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","AnalisarConclusao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Movimentacaone.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
				
			// Consultar Modelos do Usuário
			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Modelo"};
					String[] lisDescricao = {"Modelo", "Serventia", "Tipo Modelo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Modelo");
					request.setAttribute("tempBuscaDescricao", "Modelo");
					request.setAttribute("tempBuscaPrograma", "Modelo");
					request.setAttribute("tempRetorno", "AnalisarConclusao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Modelo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				}else{
					String stTemp = "";
					stTemp = Movimentacaone.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), analisePendenciaDt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}

			
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
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao","AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma","AreaDistribuicao");			
					request.setAttribute("tempRetorno","AnalisarConclusao?PassoEditar=1");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
//					idAreaDistribuicao = null;
//					areaDistribuicao = "";
				} else {
					String stTemp="";
					
					stTemp = new ProcessoNe().consultarDescricaoAreasDistribuicaoAtivaJSON(stNomeBusca1, PosicaoPaginaAtual);
						
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
					request.setAttribute("tempRetorno","AnalisarConclusao?PassoEditar=1");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
//					idServentia = null;
//					serventia = "";
				} else {
					String stTemp="";
					stTemp = new ProcessoNe().consultarServentiasAtivasAreaDistribuicaoJSON(stNomeBusca1, processoEncaminhamentoDt.getIdAreaDistribuicao(), PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
			break;

			case Configuracao.Curinga8:
				if(passoEditar == Configuracao.Excluir)
				{
					PendenciaNe pendenciane = new PendenciaNe();
					
					pendenciane.alterarPrioridadePendencia(((PendenciaDt) request.getSession().getAttribute("retirarPrioridadePendenciaDt")).getId(), "1", UsuarioSessao, null);
					request.setAttribute("MensagemOk", "Prioridade removida com sucesso.");
					
					consultarConclusao(request, analisePendenciaDt, Movimentacaone, UsuarioSessao, passoEditar, fluxo);
					stAcao = "/WEB-INF/jsptjgo/ConclusaoLocalizar.jsp";
					analisePendenciaDt.setFluxo(paginaatual);

					analisePendenciaDt.limpar();
					request.getSession().removeAttribute("retirarPrioridadePendenciaDt");
				}
				else
				{
					PendenciaNe pendenciane = new PendenciaNe();
					
					PendenciaDt retirarPrioridadePendenciaDt = pendenciane.consultarId(request.getParameter("retirarPrioridadePendencia_Id"));
					request.getSession().setAttribute("retirarPrioridadePendenciaDt", retirarPrioridadePendenciaDt);
					stAcao = "/WEB-INF/jsptjgo/ConfirmarRetiradaPrioridadePendenciaProcesso.jsp";
				}
			break;
			case Configuracao.Curinga9:
				PendenciaNe pendenciane = new PendenciaNe();
				
				List conclusoesGabinete = pendenciane.consultarListaConclusoesGabinete(UsuarioSessao.getId_Serventia());
				request.setAttribute("ListaConclusoesGabinete", conclusoesGabinete);
				stAcao = "/WEB-INF/jsptjgo/ConclusoesGabinete.jsp";
				break;
			default:
				//Controles da variável passoEditar
				// 0 : Redireciona para passo 1 - Dados Análise
				// 1 : Redireciona para passo 2 - Pendências a Gerar
				switch (passoEditar) {

					case 0:
						analisePendenciaDt.setPasso1("Passo 1");
						analisePendenciaDt.setPasso2("");
						analisePendenciaDt.setPasso3("");
						break;

					case 1:
						analisePendenciaDt.setPasso1("Passo 1 OK");
						analisePendenciaDt.setPasso2("Passo 2");
						analisePendenciaDt.setPasso3("");
						stAcao = "/WEB-INF/jsptjgo/AnalisarConclusaoPendencias.jsp";

						break;

					default:
						//Refere-se a visualização de uma analise já finalizada
						if (!analisePendenciaDt.isPreAnalise() && fluxo == Configuracao.Curinga7) {
							analisePendenciaDt = montarAnaliseFinalizada(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
							analisePendenciaDt.setFluxo(fluxo);
							stAcao = "/WEB-INF/jsptjgo/VisualizarAnaliseConclusao.jsp";
						}
				}
				break;
		}
		
		//Seta variáveis auxiliares
		request.setAttribute("Id_ArquivoTipo", analisePendenciaDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", analisePendenciaDt.getArquivoTipo());
		request.setAttribute("nomeArquivo", analisePendenciaDt.getNomeArquivo());
		request.setAttribute("Modelo", analisePendenciaDt.getModelo());
		request.setAttribute("TextoEditor", analisePendenciaDt.getTextoEditor());

		request.getSession().setAttribute("AnalisePendenciadt", analisePendenciaDt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);
		
		//Caso o tipo de Movimentação escolhido não esteja configurado teremos que adicioná-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(analisePendenciaDt);
		
		//Libera o botão Guardar para Assinar segundo o perfil do usuário
		
		//request.setAttribute("exibePendenciaAssinatura", UsuarioSessao.isPodeExibirPendenciaAssinatura(analisePendenciaDt.isMultipla(), Funcoes.StringToInt(analisePendenciaDt.getTipoPendenciaCodigo())));		
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Retorna todos os dados de uma análise efetuada a serem exibidos na tela:
	 * dados análise, arquivos que foram inseridos pelo juiz, e todo o histórico de pré-analises registradas
	 * @throws Exception 
	 */
	protected AnaliseConclusaoDt montarAnaliseFinalizada(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{

		List arquivosResposta = null;
		List historico = null;
		PendenciaDt pendencia = null;

		String id_Pendencia = request.getParameter("Id_Pendencia");

		if (id_Pendencia != null) {
			if (analisePendenciaDt.getPendenciaDt() == null || !id_Pendencia.equalsIgnoreCase(analisePendenciaDt.getPendenciaDt().getId())) {

				pendencia = movimentacaone.consultarFinalizadaId(id_Pendencia);

				//Pesquisa as pré-analises anteriores registradas para a pendência
				historico = movimentacaone.consultarPreAnalisesConclusaoFinalizadaAnteriores(pendencia, usuarioSessao);

				//Consulta os arquivos de resposta da pendência, que serão o arquivo da análise e da pré-analise (se houver)
				arquivosResposta = movimentacaone.consultarArquivosRespostaConclusaoFinalizada(pendencia.getId(), usuarioSessao);
				if (arquivosResposta != null) {
					for (int i = 0; i < arquivosResposta.size(); i++) {
						PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) arquivosResposta.get(i);
						pendenciaArquivoDt.setPendenciaDt(pendencia);
						//Se arquivo não é assinado, é pré-analise
						if (pendenciaArquivoDt.getArquivoDt().getUsuarioAssinador().equals("")) {
							//Sendo o arquivo de resposta a última pré-analise, busca então o responsável
							pendenciaArquivoDt.setResponsavelPreAnalise(movimentacaone.consultaResponsavelPreAnaliseFinalizada(pendenciaArquivoDt.getPendenciaDt().getId()));
							historico.add(0, pendenciaArquivoDt);
						} else pendencia.addListaArquivos(pendenciaArquivoDt);

					}
				}
				analisePendenciaDt.setHistoricoPendencia(historico);
				analisePendenciaDt.setListaPendenciasGerar(movimentacaone.consultarFilhasPendencia(pendencia));
				analisePendenciaDt.setPendenciaDt(pendencia);
			}
		}

		request.setAttribute("linkArquivo", "PendenciaArquivo");
		request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
		request.setAttribute("paginaArquivo", Configuracao.Curinga6);
		return analisePendenciaDt;
	}

	/**
	 * Seta variáveis necessárias para redirecionamento para telas corretas
	 * @param analisePendenciaDt
	 * @param fluxo
	 * @param fluxoPreAnalise
	 * @param tipoConclusao
	 */
	protected void setFluxoRedirecionamento(AnaliseConclusaoDt analisePendenciaDt, int fluxo, boolean fluxoPreAnalise, String tipoConclusao) {
		analisePendenciaDt.setFluxo(fluxo);
		analisePendenciaDt.setPreAnalise(fluxoPreAnalise);
		analisePendenciaDt.setId_TipoPendencia(tipoConclusao);
	}

	/**
	 * Zera listas de arquivos e pendências
	 * @param request
	 */
	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");

		// Limpa lista DWR e zera contador Pendências
		request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
		request.getSession().removeAttribute("ListaPendencias");
	}

	/**
	 * Monta tela para análise de múltiplas pré-análises.
	 * Pode receber um vetor de pendências pendencias[] quando usuario selecionou as pendências,
	 * ou pode vir uma lista das pendências a serem fechadas com a análise pendenciasFechar.
	 * @throws Exception 
	 */
	protected void montarTelaAnaliseMultipla(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String[] pendencias, List pendenciasFechar, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, int fluxo, boolean fluxoPreAnalise, String tipoConclusao, UsuarioNe usuarioSessao) throws Exception{

		analisePendenciaDt.setMultipla(true);		
		if (pendencias != null) {
			//Pesquisa pendências
			for (int i = 0; i < pendencias.length; i++) {
				String id_Pendencia = (String) pendencias[i];
				PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
				
				if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA) {
					analisePendenciaDt.setVisualizarPedidoAssistencia(true);
				}
				
				//Resgata dados básicos de cada processo
				pendenciaDt.setProcessoDt(movimentacaoNe.consultarProcessoId(pendenciaDt.getId_Processo()));				
				//Verifica se existe peticionamento com data posterior à conclusão
				pendenciaDt.getProcessoDt().setExistePeticaoPendente(existePeticionamentoPendente(usuarioSessao, movimentacaoNe, pendenciaDt));
				//Verifica se existe Guias Pendentes para o processo
				pendenciaDt.getProcessoDt().setExisteGuiasPendentes(existeGuiasPendentes(movimentacaoNe, pendenciaDt));
				analisePendenciaDt.addPendenciasFechar(pendenciaDt);				
			}
		} else analisePendenciaDt.setListaPendenciasFechar(pendenciasFechar); //Adiciona pendências a serem fechadas

		request.setAttribute("TituloPagina", "Analisar Múltiplos Autos Conclusos");

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, fluxoPreAnalise, tipoConclusao);

		setarPreAnalise(request, analisePendenciaDt, movimentacaoNe, usuarioDt);
		
		//Seta tipos de pendências que poderão ser geradas
		analisePendenciaDt.setListaPendenciaTipos(movimentacaoNe.consultarTiposPendenciaMovimentacao(usuarioDt));
		
		// Quando for pré-analise limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
		
		// Seta os tipos de movimentação configuradas para o usuário e grupo do usuário logado, parametrizados por ele
		analisePendenciaDt.setListaTiposMovimentacaoConfigurado(movimentacaoNe.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(usuarioSessao.getUsuarioDt()));

	}

	/**
	 * Método que irá setar no request as informações da pré-análise já efetuada por um assistente
	 * para que o juiz possa finalizar
	 * @param request
	 * @param movimentacaodt
	 * @param id_PendenciaFechar 
	 * @throws Exception 
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe movimentacaoNe, UsuarioDt usuarioDt){

		if (analisePendenciaDt.getListaPendenciasGerar() != null) {
			request.getSession().setAttribute("ListaPendencias", Funcoes.converterListParaSet(analisePendenciaDt.getListaPendenciasGerar()));
			request.getSession().setAttribute("Id_ListaDadosMovimentacao", analisePendenciaDt.getListaPendenciasGerar().size());
		}

		//Seta dados da pré-analise em AnaliseConclusaoDt para mostrar na tela
		if (analisePendenciaDt.getArquivoPreAnalise() != null) {
			PendenciaArquivoDt preAnalise = analisePendenciaDt.getArquivoPreAnalise();
			analisePendenciaDt.setTextoEditor(preAnalise.getArquivoDt().getArquivo());
			analisePendenciaDt.setId_ArquivoTipo(preAnalise.getArquivoDt().getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(preAnalise.getArquivoDt().getArquivoTipo());
			analisePendenciaDt.setNomeArquivo(preAnalise.getArquivoDt().getNomeArquivo());
			analisePendenciaDt.setDataPreAnalise(preAnalise.getArquivoDt().getDataInsercao());

			//Se pré-análise foi feita por assistente
			if (preAnalise.getAssistenteResponsavel() != null && !preAnalise.getAssistenteResponsavel().equals("")) analisePendenciaDt.setUsuarioPreAnalise(preAnalise.getAssistenteResponsavel());
			else analisePendenciaDt.setUsuarioPreAnalise(preAnalise.getJuizResponsavel());
		}
	}

	/**
	 * Monta tela para análise de conclusão (único)
	 * @throws Exception 
	 */
	protected void montarTelaAnaliseSimples(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_Pendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, int fluxo, boolean fluxoPreAnalise, String tipoConclusao, UsuarioNe usuarioSessao) throws Exception{
		//Pesquisa dados da pendência de conclusão
		PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
		pendenciaDt.setHash(usuarioSessao.getCodigoHash(pendenciaDt.getId()));
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);
		
		if (pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA) {
			analisePendenciaDt.setVisualizarPedidoAssistencia(true);
		}
		
		//Resgata processo ligado a pendência
		ProcessoDt processoDt = movimentacaoNe.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);	
		
		//Verifica se existe peticionamento com data posterior à conclusão
		processoDt.setExistePeticaoPendente(existePeticionamentoPendente(usuarioSessao, movimentacaoNe, pendenciaDt));
		
		//Verifica se existe Guias Pendentes para o processo
		processoDt.setExisteGuiasPendentes(existeGuiasPendentes(movimentacaoNe, pendenciaDt));

		//Seta tipos de pendências que poderão ser geradas
		analisePendenciaDt.setListaPendenciaTipos(movimentacaoNe.consultarTiposPendenciaMovimentacao(usuarioDt));

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, fluxoPreAnalise, tipoConclusao);
		//na verdade tem que setar o id...
		analisePendenciaDt.setId_TipoPendencia(pendenciaDt.getId_PendenciaTipo());
		
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
		
		// Seta os tipos de movimentação configuradas para o usuário e grupo do usuário logado, parametrizados por ele
		analisePendenciaDt.setListaTiposMovimentacaoConfigurado(movimentacaoNe.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(usuarioSessao.getUsuarioDt()));
	}

	/**
	 * Consulta autos conclusos e verifica se usuário verá todas as conclusões ou somente aquelas que não possuem pré-analise.
	 * Se usuário tem permissão para analisar, poderá ver todas.
	 * @param fluxo 
	 * @throws Exception 
	 */
	protected void consultarConclusao(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao, int passoEditar, int fluxo) throws Exception{
		if (passoEditar == 0) {//Significa que foi acionado Botão Limpar
			analisePendenciaDt.setNumeroProcesso("null");
			analisePendenciaDt.setId_Classificador("null");
		}

		//Verifica se usuário pode analisar			
		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA
				&& usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() != GrupoTipoDt.ASSESSOR_DESEMBARGADOR)
			request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
		else
			request.setAttribute("podeAnalisar", "false");
		request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao));
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());
		request.setAttribute("id_Classificador", analisePendenciaDt.getId_Classificador());
		request.setAttribute("classificador", analisePendenciaDt.getClassificador());

		if (usuarioSessao.isDesembargador() && !usuarioSessao.isGabinetePresidenciaTjgo() && !usuarioSessao.isGabineteVicePresidenciaTjgo()) {
			request.setAttribute("podeGerarVotoEmenta", "true");
		}
		
		boolean EhVoto = false;
		boolean EhVotoVencido = false;
		
		//verifica se o usuário pode trocar o responsável de pendência
		if (analisePendenciaDt.getId_PendenciaTipo() != null && analisePendenciaDt.getId_PendenciaTipo().length()>0){
			PendenciaTipoDt pendenciaTipoDt = movimentacaone.consultarPendenciaTipo(analisePendenciaDt.getId_PendenciaTipo());

			if(pendenciaTipoDt != null && pendenciaTipoDt.getPendenciaTipoCodigo() != null && pendenciaTipoDt.getPendenciaTipoCodigo().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO))){
				EhVoto = true;
				EhVotoVencido = true;
			}
		}
		
		//Verifica se usuário digitou "." e dígito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
			return;
		}
		
		List tempList = movimentacaone.consultarConclusoesPendentes(usuarioSessao, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_Classificador(), analisePendenciaDt.getId_PendenciaTipo(), EhVoto, EhVotoVencido);
		if (tempList.size() > 0) {
			request.setAttribute("ListaConclusao", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Não há Conclusões Pendentes.");
		}

	}

	/**
	 * Consulta conclusões finalizadas
	 * @throws Exception 
	 */
	protected boolean consultarConclusoesFinalizadas(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, String id_ServentiaCargo, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;

		if (analisePendenciaDt.getNumeroProcesso().length() > 0 || (analisePendenciaDt.getDataInicial().length() > 0 && analisePendenciaDt.getDataFinal().length() > 0)) {

			//Verifica se usuário digitou "." e dígito verificador
			if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
				request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
				return boRetorno;
			}

			List tempList = movimentacaone.consultarConclusoesFinalizadas(id_ServentiaCargo, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getDataInicial(), analisePendenciaDt.getDataFinal());
			if (tempList.size() > 0) {
				request.setAttribute("ListaConclusoes", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				boRetorno = true;
			} else request.setAttribute("MensagemErro", "Nenhuma Conclusão Localizada.");
		} else request.setAttribute("MensagemErro", "Informe parâmetros para consulta: Data Inicial e Data Final ou Número do Processo.");
		return boRetorno;
	}

	/**
	 * Recupera lista de arquivos inseridas usando DWR e converte de Map para List
	 */
	protected List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		List lista = Funcoes.converterMapParaList(mapArquivos);

		return lista;
	}

	/**
	 * Recupera lista de pendências inseridas usando DWR e converte de Set para List
	 */
	protected List getListaPendencias(HttpServletRequest request) {
		Set listaPendencias = (Set) request.getSession().getAttribute("ListaPendencias");
		List lista = Funcoes.converterSetParaList(listaPendencias);

		return lista;
	}

	/**
	 * Seta parametros necessarios
	 * @param fluxo 
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(HttpServletRequest request, AnaliseConclusaoDt analisePendenciaDt, int paginaAtual, int paginaAnterior, int passoEditar, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe) throws Exception{

		//Captura parâmetros consulta		
		if (request.getParameter("numeroProcesso") != null) analisePendenciaDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
		if (request.getParameter("classificador") != null) analisePendenciaDt.setClassificador(request.getParameter("classificador"));
		if (request.getParameter("DataInicial") != null) analisePendenciaDt.setDataInicial(request.getParameter("DataInicial"));
		if (request.getParameter("DataFinal") != null) analisePendenciaDt.setDataFinal(request.getParameter("DataFinal"));
		if (request.getParameter("preAnalise") != null && request.getParameter("preAnalise").equals("true")) analisePendenciaDt.setPreAnalise(true);
		if (request.getParameter("preAnaliseConclusao") != null && request.getParameter("preAnaliseConclusao").equals("true")) analisePendenciaDt.setPreAnalise(true);
		// Quando modelo foi selecionado monta conteúdo para aparecer no editor e já carrego o tipo do arquivo
		if (!analisePendenciaDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = movimentacaoNe.consultarModeloId(analisePendenciaDt.getId_Modelo(), analisePendenciaDt.getPrimeiroProcessoListaFechar(), usuarioSessao.getUsuarioDt());
			analisePendenciaDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(modeloDt.getArquivoTipo());
			analisePendenciaDt.setTextoEditor(modeloDt.getTexto());
		}
		request.setAttribute("PaginaAnterior", paginaAtual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		request.setAttribute("PassoEditar", passoEditar);

		//Tratamento mensagem de Confirmação de Análise partindo de uma pré-analise simples
		//Essa mensagem pode vir redirecionada da servlet AnalisarConclusaoCt e virá como parameter
		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else if (request.getAttribute("MensagemOk") != null) request.setAttribute("MensagemOk", request.getAttribute("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		
		if (request.getAttribute("MensagemErro") != null) request.setAttribute("MensagemErro", request.getAttribute("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

	}
	
	protected boolean existePeticionamentoPendente(UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe, PendenciaDt pendenciaDt) throws Exception{
		if (pendenciaDt == null) return false;
		if (pendenciaDt.getProcessoDt() == null) return false;
		return movimentacaoNe.existePeticionamentoPendente(usuarioSessao, pendenciaDt.getProcessoDt().getProcessoNumero(), pendenciaDt.getDataInicio(), pendenciaDt.getProcessoDt().getId_Serventia());
	}
	
	
	protected boolean existeGuiasPendentes(MovimentacaoNe movimentacaoNe, PendenciaDt pendenciaDt) throws Exception{
		if (pendenciaDt == null) return false;
		if (pendenciaDt.getProcessoDt() == null) return false;
		return movimentacaoNe.verificarGuiasPendentesProcesso(pendenciaDt.getProcessoDt().getId());
	}

	/**
	 * Verifica se o tipo de movimentação selecionado é um tipo de movimentação já configurado para o usuário logado
	 * @param analisePendenciaDt
	 */
	protected void verifiqueMovimentacaoTipo(AnaliseConclusaoDt analisePendenciaDt) {
		if ((analisePendenciaDt.getId_MovimentacaoTipo() == null) || (analisePendenciaDt.getId_MovimentacaoTipo().trim().equalsIgnoreCase("")) 
		    || (analisePendenciaDt.getMovimentacaoTipo() == null) || (analisePendenciaDt.getMovimentacaoTipo().trim().equalsIgnoreCase(""))) return;
		boolean encontrouNaLista = false;	
		List listaMovimentacaoTipo = analisePendenciaDt.getListaTiposMovimentacaoConfigurado();
		for (int i=0;i<listaMovimentacaoTipo.size();i++){
			UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);
			if (analisePendenciaDt.getId_MovimentacaoTipo().equalsIgnoreCase(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo())) encontrouNaLista = true;
		}
		if (!encontrouNaLista){
			UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = new UsuarioMovimentacaoTipoDt();
			usuarioMovimentacaoTipoDt.setId_MovimentacaoTipo(analisePendenciaDt.getId_MovimentacaoTipo());
			usuarioMovimentacaoTipoDt.setMovimentacaoTipo(analisePendenciaDt.getMovimentacaoTipo());
			analisePendenciaDt.addListaTiposMovimentacaoConfigurado(usuarioMovimentacaoTipoDt);
		}
	}
	
	protected boolean verificaPodeTrocarResponsavel(PendenciaTipoDt pendenciaTipoDt, UsuarioDt usuarioDt){
		boolean retorno = false;
		
		if (pendenciaTipoDt.getPendenciaTipoCodigo() != null && pendenciaTipoDt.getPendenciaTipoCodigo().length() > 0 
				&& (Funcoes.StringToInt(pendenciaTipoDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO
					|| Funcoes.StringToInt(pendenciaTipoDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO)
				&& (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
					|| Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo()) == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)){
			retorno = true;
		}
		
		return retorno;
	}
	
}
