package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaArquivoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PreAnalisePendenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet para controlar as análises de pendências comuns, que não sejam autos conclusos, mas tenham a figura do assistente.
 * Será disponibilizado para Desembargadores, Promotores e Advogados
 * 
 * @author msapaula
 */
public class AnalisarPendenciaCt extends Controle {

	private static final long serialVersionUID = 8652459075296978892L;

	public int Permissao() {
		return AnalisePendenciaDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AnalisePendenciaDt analisePendenciaDt;
		MovimentacaoNe Movimentacaone;

		String Mensagem = "";
		int paginaAnterior = 0;
		int passoEditar = -1;
		String pendencias[] = null;
		int tempFluxo1 = -1;
		
		String stAcao = "/WEB-INF/jsptjgo/AnalisarPendencia.jsp";
		request.setAttribute("tempPrograma", "Analisar Pendência");
		request.setAttribute("tempRetorno", "AnalisarPendencia");
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").toString().equalsIgnoreCase("null")) 
			tempFluxo1 = Funcoes.StringToInt(request.getParameter("tempFluxo1"));

		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();

		analisePendenciaDt = (AnalisePendenciaDt) request.getSession().getAttribute("AnalisePendenciadt");
		if (analisePendenciaDt == null) analisePendenciaDt = new AnalisePendenciaDt();

		analisePendenciaDt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));
		analisePendenciaDt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		analisePendenciaDt.setComplementoMovimentacao(request.getParameter("MovimentacaoComplemento"));
		analisePendenciaDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		analisePendenciaDt.setNomeArquivo(request.getParameter("nomeArquivo"));
		analisePendenciaDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		analisePendenciaDt.setId_Modelo(request.getParameter("Id_Modelo"));
		analisePendenciaDt.setModelo(request.getParameter("Modelo"));
		analisePendenciaDt.setTextoEditor(request.getParameter("TextoEditor"));
		analisePendenciaDt.setId_TipoPendencia(request.getParameter("tipo"));
		analisePendenciaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		analisePendenciaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		if (analisePendenciaDt.getListaPendenciasFechar() != null && analisePendenciaDt.getListaPendenciasFechar().size() > 1) request.setAttribute("TituloPagina", "Analisar Múltiplas Pendências");
		else request.setAttribute("TituloPagina", "Analisar Pendência");

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));

		setParametrosAuxiliares(request, analisePendenciaDt, paginaatual, paginaAnterior, passoEditar, UsuarioSessao, Movimentacaone);
		
		int fluxo = analisePendenciaDt.getFluxo();
		String tipoPendencia = analisePendenciaDt.getId_PendenciaTipo();
		boolean fluxoPreAnalise = analisePendenciaDt.isPreAnalise();

		switch (paginaatual) {

			// Inicializa análise de pendência
			case Configuracao.Novo:

				// Captura as pendências que serão fechadas
				if (request.getParameter("pendencias") != null) pendencias = request.getParameterValues("pendencias");
				else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")) pendencias = new String[] {request.getParameter("Id_Pendencia") };

				if (pendencias != null && pendencias.length > 0) {
					if (pendencias.length > 1) {
						//Análise Múltipla
						analisePendenciaDt = new AnalisePendenciaDt();
						montarTelaAnaliseMultipla(request, analisePendenciaDt, pendencias, null, UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoPendencia);
					} else {
						//Análise Simples: verifica se existe uma pré-analise para a pendência selecionada
						PendenciaArquivoDt arquivoPreAnalise = Movimentacaone.getArquivoPreAnalisePendencia(pendencias[0]);
						if (arquivoPreAnalise != null) {
							analisePendenciaDt = Movimentacaone.getPreAnalisePendencia(arquivoPreAnalise);
							setarPreAnalise(request, analisePendenciaDt, Movimentacaone, UsuarioSessao.getUsuarioDt());
						} else analisePendenciaDt = new AnalisePendenciaDt();

						//Montar tela pré-analise simples
						montarTelaAnaliseSimples(request, analisePendenciaDt, pendencias[0], UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoPendencia);
					}
				} else {
					request.setAttribute("MensagemErro", "Nenhuma Pendência foi selecionada.");
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				// Seta os tipos de movimentação configuradas para o usuário e grupo do usuário logado, parametrizados por ele
				analisePendenciaDt.setListaTiposMovimentacaoConfigurado(Movimentacaone.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioSessao.getUsuarioDt()));
				break;

			case Configuracao.Salvar:
				stAcao = "/WEB-INF/jsptjgo/AnalisarPendenciaConfirmacao.jsp";
				analisePendenciaDt.setListaArquivos(getListaArquivos(request)); // Captura lista de arquivos
				analisePendenciaDt.setPasso1("Passo 1 OK");
				analisePendenciaDt.setPasso2("Passo 2");
				break;

			// Salvando Análise de Pendências
			case Configuracao.SalvarResultado:
				if (tempFluxo1 == 1 || tempFluxo1 == 2) {
					Mensagem = Movimentacaone.verificarPreAnalisePendencia(analisePendenciaDt, request.getParameter("IdProcessoValidacaoAba"));
					if (Mensagem.length() == 0) {
						analisePendenciaDt.setPendenteAssinatura((tempFluxo1 == 2));
						Movimentacaone.salvarPreAnalisePendencia(analisePendenciaDt, UsuarioSessao.getUsuarioDt());

						//Quando trata de análise múltipla
						if (analisePendenciaDt.isMultipla()) request.setAttribute("MensagemOk", "Pré-Análise Múltipla registrada com sucesso.");
						else request.setAttribute("MensagemOk", "Pré-Análise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar()));

						// Limpa da sessão os atributos
						analisePendenciaDt.limpar();

						this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
						return;

					} else request.setAttribute("MensagemErro", Mensagem);
				} else {
					Mensagem = Movimentacaone.verificarAnalisePendencia(analisePendenciaDt);
					if (Mensagem.length() == 0) {
						Movimentacaone.salvarAnalisePendencia(analisePendenciaDt, UsuarioSessao.getUsuarioDt());
	
						//Quando trata de despacho múltiplo
						if (analisePendenciaDt.isMultipla()) Mensagem = "Análise Múltipla registrada com sucesso.";
						else Mensagem = "Análise efetuada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar());
	
						request.setAttribute("MensagemOk", Mensagem);
						// Limpa da sessão os atributos
						analisePendenciaDt.limpar();
						limparListas(request);
	
						if (analisePendenciaDt.isPreAnalise()) redireciona(response, "PreAnalisarPendencia?PaginaAtual=" + fluxo + "&MensagemOk=" + Mensagem);
						else this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
						return;
	
					} else request.setAttribute("MensagemErro", Mensagem);
				}
				break;

			// Localiza pendências não analisadas
			case Configuracao.Localizar:
				consultarPendenciasNaoAnalisadas(request, analisePendenciaDt, Movimentacaone, UsuarioSessao, passoEditar, fluxo);
				if (request.getParameter("MensagemOk")!= null)
					request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
				stAcao = "/WEB-INF/jsptjgo/PendenciasNaoAnalisadasLocalizar.jsp";
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
						PendenciaArquivoDt pendenciaArquivoDt = Movimentacaone.getArquivoPreAnalisePendencia(primeiraPendencia.getId());
	
						//Se encontrou uma pré-analise para a pendência, compara se é o mesmo arquivo passado
						if (pendenciaArquivoDt != null && pendenciaArquivoDt.getArquivoDt().getId().equals(id_Arquivo)) {
							//Recupera dados da pré-analise
							analisePendenciaDt = Movimentacaone.getPreAnalisePendencia(pendenciaArquivoDt);
	
							//Monta tela para análise das pré-analises multiplas
							montarTelaAnaliseMultipla(request, analisePendenciaDt, null, pendenciasFechar, UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoPendencia);
						}
					}
				}
				break;

			//Consulta de pendências finalizadas
			case Configuracao.Curinga7:
				//Se fluxo anterior é esse, deve fazer a consulta
				if (paginaAnterior == Configuracao.Curinga7) 
					consultarPendenciasAnalisadas(request, analisePendenciaDt, UsuarioSessao.getUsuarioDt(), Movimentacaone);
				else {
					analisePendenciaDt = new AnalisePendenciaDt();
					analisePendenciaDt.setFluxo(paginaatual);
				}
				stAcao = "/WEB-INF/jsptjgo/PendenciasAnalisadasLocalizar.jsp";
				break;
			
			case Configuracao.Curinga8:
				//Pesquisa dados da pendência
				PendenciaDt pendenciaVotoDt = Movimentacaone.consultarPendenciaId( request.getParameter("Id_Pendencia"));
				Movimentacaone.criarPendenciaDesembargador(pendenciaVotoDt, String.valueOf(PendenciaTipoDt.CONCLUSO_VOTO), UsuarioSessao.getUsuarioDt());
				boolean ehAssistenteVoto = (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE || UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO || 
						UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR);
				
				if (analisePendenciaDt.isPreAnalise() || ehAssistenteVoto) {
					if ((request.getParameter("preAnaliseConclusao") != null && request.getParameter("preAnaliseConclusao").toString().equals("true")) || 
						(request.getParameter("AnaliseConclusao") != null && request.getParameter("AnaliseConclusao").toString().equals("true")))
						redireciona(response, "PreAnalisarConclusao?PaginaAtual=" + fluxo + "&MensagemOk=Voto / Ementa criado com sucesso.");
					else
						redireciona(response, "PreAnalisarPendencia?PaginaAtual=" + fluxo + "&MensagemOk=Voto / Ementa criado com sucesso.");
					return;
				} else {
					if (request.getParameter("AnaliseConclusao") != null && request.getParameter("AnaliseConclusao").toString().equals("true")){
						redireciona(response, "AnalisarConclusao?PaginaAtual=" + fluxo + "&MensagemOk=Voto / Ementa criado com sucesso.");
						return;
					} else {
						request.setAttribute("MensagemOk", "Voto / Ementa criado com sucesso.");
						consultarPendenciasNaoAnalisadas(request, analisePendenciaDt, Movimentacaone, UsuarioSessao, passoEditar, fluxo);
						stAcao = "/WEB-INF/jsptjgo/PendenciasNaoAnalisadasLocalizar.jsp";
						analisePendenciaDt.setFluxo(paginaatual);
						analisePendenciaDt.limpar();
					}
				}
				break;
			
			case Configuracao.Curinga9:
				//Pesquisa dados da pendência
				PendenciaDt pendenciaEmentaDt = Movimentacaone.consultarPendenciaId( request.getParameter("Id_Pendencia"));
				Movimentacaone.criarPendenciaDesembargador(pendenciaEmentaDt, String.valueOf(PendenciaTipoDt.CONCLUSO_EMENTA), UsuarioSessao.getUsuarioDt());
				boolean ehAssistenteEmenta = (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE || UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO 
						|| UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA  || UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR);
				
				if (analisePendenciaDt.isPreAnalise() || ehAssistenteEmenta){
					if ((request.getParameter("preAnaliseConclusao") != null && request.getParameter("preAnaliseConclusao").toString().equals("true")) ||
					    (request.getParameter("AnaliseConclusao") != null && request.getParameter("AnaliseConclusao").toString().equals("true")))
						redireciona(response, "PreAnalisarConclusao?PaginaAtual=" + fluxo + "&MensagemOk=Ementa criado com sucesso.");
					else
						redireciona(response, "PreAnalisarPendencia?PaginaAtual=" + fluxo + "&MensagemOk=Ementa criado com sucesso.");
					return;
				} else {
					if (request.getParameter("AnaliseConclusao") != null && request.getParameter("AnaliseConclusao").toString().equals("true")){
						redireciona(response, "AnalisarConclusao?PaginaAtual=" + fluxo + "&MensagemOk=Ementa criado com sucesso.");
						return;
					} else {
						request.setAttribute("MensagemOk", "Ementa criado com sucesso.");
						consultarPendenciasNaoAnalisadas(request, analisePendenciaDt, Movimentacaone, UsuarioSessao, passoEditar, fluxo);
						stAcao = "/WEB-INF/jsptjgo/PendenciasNaoAnalisadasLocalizar.jsp";
						analisePendenciaDt.setFluxo(paginaatual);
						analisePendenciaDt.limpar();
					}
				}
				break;
				
			//Prepara dados para Descartar Análise
			case Configuracao.Excluir:
				PendenciaDt pendenciaDt = Movimentacaone.consultarPendenciaId(request.getParameter("Id_Pendencia"));
				analisePendenciaDt.setPendenciaDt(pendenciaDt);
				request.setAttribute("Mensagem", "Deseja descartar o Pedido de Manifestação/Peticionamento?");
				stAcao = "/WEB-INF/jsptjgo/VisualizarPendenciaDescartar.jsp";
				break;

			//Função utilizada para Descartar Análise
			case Configuracao.ExcluirResultado:
				Movimentacaone.descartarAnalise(analisePendenciaDt.getPendenciaDt(), UsuarioSessao.getUsuarioDt());
				request.setAttribute("MensagemOk", "Pedido de Manifestação/Peticionamento descartado com sucesso.");

				// Limpa da sessão os atributos
				analisePendenciaDt.limpar();

				this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
				return;

				// Consultar Tipos de Movimentação
			case (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"MovimentacaoTipo"};
					String[] lisDescricao = {"MovimentacaoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MovimentacaoTipo");
					request.setAttribute("tempBuscaDescricao","MovimentacaoTipo");
					request.setAttribute("tempBuscaPrograma","MovimentacaoTipo");			
					request.setAttribute("tempRetorno","AnalisarPendencia");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PaginaAnterior", String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
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

			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"ArquivoTipo"};
				String[] lisDescricao = {"ArquivoTipo"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_ArquivoTipo");
				request.setAttribute("tempBuscaDescricao","ArquivoTipo");
				request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
				request.setAttribute("tempRetorno","AnalisarPendencia");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				passoEditar = 0;
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
				String[] lisDescricao = {"Modelo","Serventia","Tipo Modelo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Modelo");
				request.setAttribute("tempBuscaDescricao", "Modelo");
				request.setAttribute("tempBuscaPrograma", "Modelo");
				request.setAttribute("tempRetorno", "AnalisarPendencia");
				request.setAttribute("tempDescricaoDescricao", "Modelo");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				passoEditar = 0;
			}else{
				String stTemp = "";
				stTemp = Movimentacaone.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), analisePendenciaDt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
				
					enviarJSON(response, stTemp);
					
				
				return;
			}
				break;

			default:
				//Controles da variável passoEditar
				// 0 : Redireciona para passo 1 - Dados Análise
				// 1 : Redireciona para passo 2 - Confirmação
				switch (passoEditar) {

					case 0:
						analisePendenciaDt.setPasso1("Passo 1");
						analisePendenciaDt.setPasso2("");
						break;

					default:
						//Refere-se a visualização de uma analise já finalizada
						if (!analisePendenciaDt.isPreAnalise() && fluxo == Configuracao.Curinga7) {
							analisePendenciaDt = montarAnalise(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
							analisePendenciaDt.setFluxo(fluxo);
							stAcao = "/WEB-INF/jsptjgo/VisualizarAnalise.jsp";
						}
				}
				break;
		}

		//Seta variáveis auxiliares
		request.setAttribute("GrupoTipoUsuarioLogado", UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt());
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
		//request.setAttribute("exibePendenciaAssinatura", UsuarioSessao.isPodeExibirPendenciaAssinatura(analisePendenciaDt.isMultipla(), Funcoes.StringToInt(analisePendenciaDt.getTipoPendenciaCodigo(),-1)));

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Retorna todos os dados de uma análise efetuada a serem exibidos na tela:
	 * dados análise, arquivos que foram inseridos pelo chefe, e todo o histórico de pré-analises registradas
	 * @throws Exception 
	 */
	protected AnalisePendenciaDt montarAnalise(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{

		List arquivosResposta = null;
		List historico = null;
		PendenciaDt pendencia = null;

		String id_Pendencia = request.getParameter("Id_Pendencia");

		if (id_Pendencia != null) {
			if (analisePendenciaDt.getPendenciaDt() == null || !id_Pendencia.equalsIgnoreCase(analisePendenciaDt.getPendenciaDt().getId())) {

				pendencia = movimentacaone.consultarFinalizadaId(id_Pendencia);

				//Pesquisa as pré-analises anteriores registradas para a pendência
				historico = movimentacaone.consultarPreAnalisesAnteriores(pendencia, usuarioSessao);

				//Consulta os arquivos de resposta da pendência, que serão o arquivo da análise e da pré-analise (se houver)
				arquivosResposta = movimentacaone.consultarArquivosRespostaPendenciaFinalizada(pendencia.getId(), usuarioSessao);
				if (arquivosResposta != null) {
					for (int i = 0; i < arquivosResposta.size(); i++) {
						PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) arquivosResposta.get(i);
						pendenciaArquivoDt.setPendenciaDt(pendencia);
						//Se arquivo não é assinado, é pré-analise
						if (pendenciaArquivoDt.getArquivoDt().getUsuarioAssinador().equals("")) {
							historico.add(0, pendenciaArquivoDt);
						} else pendencia.addListaArquivos(pendenciaArquivoDt);
					}
				}
				analisePendenciaDt.setHistoricoPendencia(historico);
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
	 * @param tipoPendencia
	 */
	protected void setFluxoRedirecionamento(AnalisePendenciaDt analisePendenciaDt, int fluxo, boolean fluxoPreAnalise, String tipoPendencia) {
		analisePendenciaDt.setFluxo(fluxo);
		analisePendenciaDt.setPreAnalise(fluxoPreAnalise);
		analisePendenciaDt.setId_TipoPendencia(tipoPendencia);
	}

	/**
	 * Zera listas de arquivos
	 * @param request
	 */
	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
	}

	/**
	 * Monta tela para análise de múltiplas pré-análises.
	 * Pode receber um vetor de pendências pendencias[] quando usuario selecionou as pendências,
	 * ou pode vir uma lista das pendências a serem fechadas com a análise pendenciasFechar.
	 * @throws Exception 
	 */
	protected void montarTelaAnaliseMultipla(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String[] pendencias, List pendenciasFechar, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, int fluxo, boolean fluxoPreAnalise, String tipoPendencia) throws Exception{

		analisePendenciaDt.setMultipla(true);

		if (pendencias != null) {
			//Pesquisa pendências
			for (int i = 0; i < pendencias.length; i++) {
				String id_Pendencia = (String) pendencias[i];
				PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
				//Resgata dados básicos de cada processo
				pendenciaDt.setProcessoDt(movimentacaoNe.consultarProcessoId(pendenciaDt.getId_Processo()));
				analisePendenciaDt.addPendenciasFechar(pendenciaDt);
			}
		} else analisePendenciaDt.setListaPendenciasFechar(pendenciasFechar); //Adiciona pendências a serem fechadas

		request.setAttribute("TituloPagina", "Analisar Múltiplas Pendências");
		setFluxoRedirecionamento(analisePendenciaDt, fluxo, fluxoPreAnalise, tipoPendencia);
		setarPreAnalise(request, analisePendenciaDt, movimentacaoNe, usuarioDt);

		// Quando for pré-analise limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
	}

	/**
	 * Método que irá setar no request as informações da pré-análise já efetuada por um assistente para que o chefe possa finalizar
	 * @param request
	 * @param movimentacaodt
	 * @param id_PendenciaFechar 
	 * @throws Exception 
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, MovimentacaoNe movimentacaoNe, UsuarioDt usuarioDt){

		//Seta dados da pré-analise em AnalisePendenciaDt para mostrar na tela
		if (analisePendenciaDt.getArquivoPreAnalise() != null) {
			PendenciaArquivoDt preAnalise = analisePendenciaDt.getArquivoPreAnalise();
			analisePendenciaDt.setTextoEditor(preAnalise.getArquivoDt().getArquivo());
			analisePendenciaDt.setId_ArquivoTipo(preAnalise.getArquivoDt().getId_ArquivoTipo());
			analisePendenciaDt.setArquivoTipo(preAnalise.getArquivoDt().getArquivoTipo());
			analisePendenciaDt.setNomeArquivo(preAnalise.getArquivoDt().getNomeArquivo());
			analisePendenciaDt.setDataPreAnalise(preAnalise.getArquivoDt().getDataInsercao());

		}

	}

	/**
	 * Monta tela para análise de pendência (único)
	 * @throws Exception 
	 */
	protected void montarTelaAnaliseSimples(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String id_Pendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, int fluxo, boolean fluxoPreAnalise, String tipoPendencia) throws Exception{
		//Pesquisa dados da pendência
		PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);

		//Resgata processo ligado a pendência
		ProcessoDt processoDt = movimentacaoNe.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, fluxoPreAnalise, tipoPendencia);

		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
	}

	/**
	 * Consulta pendências não analisadas
	 * @throws Exception 
	 */
	protected void consultarPendenciasNaoAnalisadas(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao, int passoEditar, int fluxo) throws Exception{
		if (passoEditar == 0) {//Significa que foi acionado Botão Limpar
			analisePendenciaDt.setNumeroProcesso("null");
		}

		//Verifica se usuário pode analisar
		request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
		request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnalisePendenciaDt.CodigoPermissao));
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());
		
		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)
			request.setAttribute("podeGerarVotoEmenta", "true");

		//Verifica se usuário digitou "." e dígito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
			return;
		}

		List tempList = movimentacaone.consultarPendenciasNaoAnalisadas(usuarioSessao.getUsuarioDt(), analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_PendenciaTipo());
		if (tempList!=null && tempList.size() > 0) {
			request.setAttribute("ListaPendencias", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "Não há Pendências em Aberto.");
		}

	}

	/**
	 * Consulta pendências analisadas
	 * @throws Exception 
	 */
	protected boolean consultarPendenciasAnalisadas(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, UsuarioDt usuarioDt, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;

		if (analisePendenciaDt.getNumeroProcesso().length() > 0 || (analisePendenciaDt.getDataInicial().length() > 0 && analisePendenciaDt.getDataFinal().length() > 0)) {

			//Verifica se usuário digitou "." e dígito verificador
			if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
				request.setAttribute("MensagemErro", "Número do Processo no formato incorreto. ");
				return boRetorno;
			}

			List tempList = movimentacaone.consultarPendenciasAnalisadas(usuarioDt, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getDataInicial(), analisePendenciaDt.getDataFinal());
			if (tempList != null && tempList.size() > 0) {
				request.setAttribute("ListaConclusoes", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				boRetorno = true;
			} else request.setAttribute("MensagemErro", "Nenhuma Pendência Localizada.");
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
	 * Consulta tipos de movimentação filtrando pelo grupo do usuário logado
	 * @throws Exception 
	 */
	protected boolean consultarMovimentacaoTipo(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, MovimentacaoNe movimentacaone, int paginaatual, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;
		List tempList = movimentacaone.consultarGrupoMovimentacaoTipo(usuarioSessao.getUsuarioDt(), tempNomeBusca, posicaoPaginaAtual);
		if (tempList.size() > 0) {
			request.setAttribute("ListaMovimentacaoTipo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", movimentacaone.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_MovimentacaoTipo", "Id_MovimentacaoTipo");
			request.setAttribute("tempBuscaMovimentacaoTipo", "MovimentacaoTipo");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Tipo de Movimentação Localizado.");
		return boRetorno;
	}

	/**
	 * Consulta de modelos Se não encontrar nenhum modelo retorna false
	 * @throws Exception 
	 */
	protected boolean consultarModelo(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, MovimentacaoNe movimentacaone, AnalisePendenciaDt analisePendenciaDt, int paginaatual, UsuarioNe usuarioSessao) throws Exception{
		boolean boRetorno = false;
		List tempList = movimentacaone.consultarModelo(usuarioSessao.getUsuarioDt(), analisePendenciaDt.getId_ArquivoTipo(), tempNomeBusca, posicaoPaginaAtual);
		if (tempList != null && tempList.size() > 0) {
			request.setAttribute("ListaModelo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", movimentacaone.getQuantidadePaginas());
			request.setAttribute("tempBuscaId_Modelo", "Id_Modelo");
			request.setAttribute("tempBuscaModelo", "Modelo");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Modelo Localizado.");
		return boRetorno;
	}

	/**
	 * Consulta tipos de arquivos vinculados ao grupo do usuário logado
	 * @throws Exception 
	 */
	protected boolean consultarArquivoTipo(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, int paginaatual, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;
		List tempList = movimentacaone.consultarGrupoArquivoTipo(usuarioSessao.getUsuarioDt(), tempNomeBusca, posicaoPaginaAtual);
		if (tempList.size() > 0) {
			request.setAttribute("ListaArquivoTipo", tempList);
			request.setAttribute("PaginaAtual", paginaatual);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
			request.setAttribute("QuantidadePaginas", movimentacaone.getQuantidadePaginas());
			request.setAttribute("tempBuscaArquivoTipo", "ArquivoTipo");
			request.setAttribute("tempBuscaId_ArquivoTipo", "Id_ArquivoTipo");
			boRetorno = true;
		} else request.setAttribute("MensagemErro", "Nenhum Tipo de Arquivo Localizado.");
		return boRetorno;
	}

	/**
	 * Seta parametros necessarios
	 * @param fluxo 
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, int paginaAtual, int paginaAnterior, int passoEditar, UsuarioNe usuarioSessao, MovimentacaoNe movimentacaoNe) throws Exception{

		//Captura parâmetros consulta		
		if (request.getParameter("numeroProcesso") != null) analisePendenciaDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
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

		if (request.getAttribute("MensagemOk") != null) request.setAttribute("MensagemOk", request.getAttribute("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getAttribute("MensagemErro") != null) request.setAttribute("MensagemErro", request.getAttribute("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

	}
	
	/**
	 * Verifica se o tipo de movimentação selecionado é um tipo de movimentação já configurado para o usuário logado
	 * @param analisePendenciaDt
	 */
	protected void verifiqueMovimentacaoTipo(AnalisePendenciaDt analisePendenciaDt) {
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
	
}
