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
 * Servlet para controlar as an�lises de pend�ncias comuns, que n�o sejam autos conclusos, mas tenham a figura do assistente.
 * Ser� disponibilizado para Desembargadores, Promotores e Advogados
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
		request.setAttribute("tempPrograma", "Analisar Pend�ncia");
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

		if (analisePendenciaDt.getListaPendenciasFechar() != null && analisePendenciaDt.getListaPendenciasFechar().size() > 1) request.setAttribute("TituloPagina", "Analisar M�ltiplas Pend�ncias");
		else request.setAttribute("TituloPagina", "Analisar Pend�ncia");

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));

		setParametrosAuxiliares(request, analisePendenciaDt, paginaatual, paginaAnterior, passoEditar, UsuarioSessao, Movimentacaone);
		
		int fluxo = analisePendenciaDt.getFluxo();
		String tipoPendencia = analisePendenciaDt.getId_PendenciaTipo();
		boolean fluxoPreAnalise = analisePendenciaDt.isPreAnalise();

		switch (paginaatual) {

			// Inicializa an�lise de pend�ncia
			case Configuracao.Novo:

				// Captura as pend�ncias que ser�o fechadas
				if (request.getParameter("pendencias") != null) pendencias = request.getParameterValues("pendencias");
				else if (request.getParameter("Id_Pendencia") != null && !request.getParameter("Id_Pendencia").equals("")) pendencias = new String[] {request.getParameter("Id_Pendencia") };

				if (pendencias != null && pendencias.length > 0) {
					if (pendencias.length > 1) {
						//An�lise M�ltipla
						analisePendenciaDt = new AnalisePendenciaDt();
						montarTelaAnaliseMultipla(request, analisePendenciaDt, pendencias, null, UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoPendencia);
					} else {
						//An�lise Simples: verifica se existe uma pr�-analise para a pend�ncia selecionada
						PendenciaArquivoDt arquivoPreAnalise = Movimentacaone.getArquivoPreAnalisePendencia(pendencias[0]);
						if (arquivoPreAnalise != null) {
							analisePendenciaDt = Movimentacaone.getPreAnalisePendencia(arquivoPreAnalise);
							setarPreAnalise(request, analisePendenciaDt, Movimentacaone, UsuarioSessao.getUsuarioDt());
						} else analisePendenciaDt = new AnalisePendenciaDt();

						//Montar tela pr�-analise simples
						montarTelaAnaliseSimples(request, analisePendenciaDt, pendencias[0], UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoPendencia);
					}
				} else {
					request.setAttribute("MensagemErro", "Nenhuma Pend�ncia foi selecionada.");
					this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
					return;
				}
				// Seta os tipos de movimenta��o configuradas para o usu�rio e grupo do usu�rio logado, parametrizados por ele
				analisePendenciaDt.setListaTiposMovimentacaoConfigurado(Movimentacaone.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(UsuarioSessao.getUsuarioDt()));
				break;

			case Configuracao.Salvar:
				stAcao = "/WEB-INF/jsptjgo/AnalisarPendenciaConfirmacao.jsp";
				analisePendenciaDt.setListaArquivos(getListaArquivos(request)); // Captura lista de arquivos
				analisePendenciaDt.setPasso1("Passo 1 OK");
				analisePendenciaDt.setPasso2("Passo 2");
				break;

			// Salvando An�lise de Pend�ncias
			case Configuracao.SalvarResultado:
				if (tempFluxo1 == 1 || tempFluxo1 == 2) {
					Mensagem = Movimentacaone.verificarPreAnalisePendencia(analisePendenciaDt, request.getParameter("IdProcessoValidacaoAba"));
					if (Mensagem.length() == 0) {
						analisePendenciaDt.setPendenteAssinatura((tempFluxo1 == 2));
						Movimentacaone.salvarPreAnalisePendencia(analisePendenciaDt, UsuarioSessao.getUsuarioDt());

						//Quando trata de an�lise m�ltipla
						if (analisePendenciaDt.isMultipla()) request.setAttribute("MensagemOk", "Pr�-An�lise M�ltipla registrada com sucesso.");
						else request.setAttribute("MensagemOk", "Pr�-An�lise registrada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar()));

						// Limpa da sess�o os atributos
						analisePendenciaDt.limpar();

						this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
						return;

					} else request.setAttribute("MensagemErro", Mensagem);
				} else {
					Mensagem = Movimentacaone.verificarAnalisePendencia(analisePendenciaDt);
					if (Mensagem.length() == 0) {
						Movimentacaone.salvarAnalisePendencia(analisePendenciaDt, UsuarioSessao.getUsuarioDt());
	
						//Quando trata de despacho m�ltiplo
						if (analisePendenciaDt.isMultipla()) Mensagem = "An�lise M�ltipla registrada com sucesso.";
						else Mensagem = "An�lise efetuada com sucesso. Processo " + Funcoes.formataNumeroProcesso(analisePendenciaDt.getNumeroPrimeiroProcessoListaFechar());
	
						request.setAttribute("MensagemOk", Mensagem);
						// Limpa da sess�o os atributos
						analisePendenciaDt.limpar();
						limparListas(request);
	
						if (analisePendenciaDt.isPreAnalise()) redireciona(response, "PreAnalisarPendencia?PaginaAtual=" + fluxo + "&MensagemOk=" + Mensagem);
						else this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
						return;
	
					} else request.setAttribute("MensagemErro", Mensagem);
				}
				break;

			// Localiza pend�ncias n�o analisadas
			case Configuracao.Localizar:
				consultarPendenciasNaoAnalisadas(request, analisePendenciaDt, Movimentacaone, UsuarioSessao, passoEditar, fluxo);
				if (request.getParameter("MensagemOk")!= null)
					request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
				stAcao = "/WEB-INF/jsptjgo/PendenciasNaoAnalisadasLocalizar.jsp";
				analisePendenciaDt.setFluxo(paginaatual);
				analisePendenciaDt.limpar();
				break;

			//Tratando pr�-an�lise m�ltipla
			case Configuracao.Curinga6:

				//Se um arquivo de pr�-an�lise foi passado, verificar quais pend�ncias ele est� vinculado
				String id_Arquivo = request.getParameter("Id_Arquivo");

				if (id_Arquivo != null) {
					//Busca pend�ncias vinculadas ao arquivo
					List pendenciasFechar = Movimentacaone.consultarPendencias(id_Arquivo);

					if (pendenciasFechar!=null && pendenciasFechar.size()>0){
						//Busca arquivo de pr�-an�lise para primeira pend�ncia vinculada
						PendenciaDt primeiraPendencia = (PendenciaDt) pendenciasFechar.get(0);
						PendenciaArquivoDt pendenciaArquivoDt = Movimentacaone.getArquivoPreAnalisePendencia(primeiraPendencia.getId());
	
						//Se encontrou uma pr�-analise para a pend�ncia, compara se � o mesmo arquivo passado
						if (pendenciaArquivoDt != null && pendenciaArquivoDt.getArquivoDt().getId().equals(id_Arquivo)) {
							//Recupera dados da pr�-analise
							analisePendenciaDt = Movimentacaone.getPreAnalisePendencia(pendenciaArquivoDt);
	
							//Monta tela para an�lise das pr�-analises multiplas
							montarTelaAnaliseMultipla(request, analisePendenciaDt, null, pendenciasFechar, UsuarioSessao.getUsuarioDt(), Movimentacaone, fluxo, fluxoPreAnalise, tipoPendencia);
						}
					}
				}
				break;

			//Consulta de pend�ncias finalizadas
			case Configuracao.Curinga7:
				//Se fluxo anterior � esse, deve fazer a consulta
				if (paginaAnterior == Configuracao.Curinga7) 
					consultarPendenciasAnalisadas(request, analisePendenciaDt, UsuarioSessao.getUsuarioDt(), Movimentacaone);
				else {
					analisePendenciaDt = new AnalisePendenciaDt();
					analisePendenciaDt.setFluxo(paginaatual);
				}
				stAcao = "/WEB-INF/jsptjgo/PendenciasAnalisadasLocalizar.jsp";
				break;
			
			case Configuracao.Curinga8:
				//Pesquisa dados da pend�ncia
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
				//Pesquisa dados da pend�ncia
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
				
			//Prepara dados para Descartar An�lise
			case Configuracao.Excluir:
				PendenciaDt pendenciaDt = Movimentacaone.consultarPendenciaId(request.getParameter("Id_Pendencia"));
				analisePendenciaDt.setPendenciaDt(pendenciaDt);
				request.setAttribute("Mensagem", "Deseja descartar o Pedido de Manifesta��o/Peticionamento?");
				stAcao = "/WEB-INF/jsptjgo/VisualizarPendenciaDescartar.jsp";
				break;

			//Fun��o utilizada para Descartar An�lise
			case Configuracao.ExcluirResultado:
				Movimentacaone.descartarAnalise(analisePendenciaDt.getPendenciaDt(), UsuarioSessao.getUsuarioDt());
				request.setAttribute("MensagemOk", "Pedido de Manifesta��o/Peticionamento descartado com sucesso.");

				// Limpa da sess�o os atributos
				analisePendenciaDt.limpar();

				this.executar(request, response, UsuarioSessao, fluxo, tempNomeBusca, PosicaoPaginaAtual);
				return;

				// Consultar Tipos de Movimenta��o
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

			// Consultar Modelos do Usu�rio
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
				//Controles da vari�vel passoEditar
				// 0 : Redireciona para passo 1 - Dados An�lise
				// 1 : Redireciona para passo 2 - Confirma��o
				switch (passoEditar) {

					case 0:
						analisePendenciaDt.setPasso1("Passo 1");
						analisePendenciaDt.setPasso2("");
						break;

					default:
						//Refere-se a visualiza��o de uma analise j� finalizada
						if (!analisePendenciaDt.isPreAnalise() && fluxo == Configuracao.Curinga7) {
							analisePendenciaDt = montarAnalise(request, analisePendenciaDt, UsuarioSessao, Movimentacaone);
							analisePendenciaDt.setFluxo(fluxo);
							stAcao = "/WEB-INF/jsptjgo/VisualizarAnalise.jsp";
						}
				}
				break;
		}

		//Seta vari�veis auxiliares
		request.setAttribute("GrupoTipoUsuarioLogado", UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt());
		request.setAttribute("Id_ArquivoTipo", analisePendenciaDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", analisePendenciaDt.getArquivoTipo());
		request.setAttribute("nomeArquivo", analisePendenciaDt.getNomeArquivo());
		request.setAttribute("Modelo", analisePendenciaDt.getModelo());
		request.setAttribute("TextoEditor", analisePendenciaDt.getTextoEditor());

		request.getSession().setAttribute("AnalisePendenciadt", analisePendenciaDt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);
		
		//Caso o tipo de Movimenta��o escolhido n�o esteja configurado teremos que adicion�-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(analisePendenciaDt);
		
		//Libera o bot�o Guardar para Assinar segundo o perfil do usu�rio
		//request.setAttribute("exibePendenciaAssinatura", UsuarioSessao.isPodeExibirPendenciaAssinatura(analisePendenciaDt.isMultipla(), Funcoes.StringToInt(analisePendenciaDt.getTipoPendenciaCodigo(),-1)));

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Retorna todos os dados de uma an�lise efetuada a serem exibidos na tela:
	 * dados an�lise, arquivos que foram inseridos pelo chefe, e todo o hist�rico de pr�-analises registradas
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

				//Pesquisa as pr�-analises anteriores registradas para a pend�ncia
				historico = movimentacaone.consultarPreAnalisesAnteriores(pendencia, usuarioSessao);

				//Consulta os arquivos de resposta da pend�ncia, que ser�o o arquivo da an�lise e da pr�-analise (se houver)
				arquivosResposta = movimentacaone.consultarArquivosRespostaPendenciaFinalizada(pendencia.getId(), usuarioSessao);
				if (arquivosResposta != null) {
					for (int i = 0; i < arquivosResposta.size(); i++) {
						PendenciaArquivoDt pendenciaArquivoDt = (PendenciaArquivoDt) arquivosResposta.get(i);
						pendenciaArquivoDt.setPendenciaDt(pendencia);
						//Se arquivo n�o � assinado, � pr�-analise
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
	 * Seta vari�veis necess�rias para redirecionamento para telas corretas
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
	 * Monta tela para an�lise de m�ltiplas pr�-an�lises.
	 * Pode receber um vetor de pend�ncias pendencias[] quando usuario selecionou as pend�ncias,
	 * ou pode vir uma lista das pend�ncias a serem fechadas com a an�lise pendenciasFechar.
	 * @throws Exception 
	 */
	protected void montarTelaAnaliseMultipla(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String[] pendencias, List pendenciasFechar, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, int fluxo, boolean fluxoPreAnalise, String tipoPendencia) throws Exception{

		analisePendenciaDt.setMultipla(true);

		if (pendencias != null) {
			//Pesquisa pend�ncias
			for (int i = 0; i < pendencias.length; i++) {
				String id_Pendencia = (String) pendencias[i];
				PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
				//Resgata dados b�sicos de cada processo
				pendenciaDt.setProcessoDt(movimentacaoNe.consultarProcessoId(pendenciaDt.getId_Processo()));
				analisePendenciaDt.addPendenciasFechar(pendenciaDt);
			}
		} else analisePendenciaDt.setListaPendenciasFechar(pendenciasFechar); //Adiciona pend�ncias a serem fechadas

		request.setAttribute("TituloPagina", "Analisar M�ltiplas Pend�ncias");
		setFluxoRedirecionamento(analisePendenciaDt, fluxo, fluxoPreAnalise, tipoPendencia);
		setarPreAnalise(request, analisePendenciaDt, movimentacaoNe, usuarioDt);

		// Quando for pr�-analise limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
	}

	/**
	 * M�todo que ir� setar no request as informa��es da pr�-an�lise j� efetuada por um assistente para que o chefe possa finalizar
	 * @param request
	 * @param movimentacaodt
	 * @param id_PendenciaFechar 
	 * @throws Exception 
	 */
	protected void setarPreAnalise(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, MovimentacaoNe movimentacaoNe, UsuarioDt usuarioDt){

		//Seta dados da pr�-analise em AnalisePendenciaDt para mostrar na tela
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
	 * Monta tela para an�lise de pend�ncia (�nico)
	 * @throws Exception 
	 */
	protected void montarTelaAnaliseSimples(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, String id_Pendencia, UsuarioDt usuarioDt, MovimentacaoNe movimentacaoNe, int fluxo, boolean fluxoPreAnalise, String tipoPendencia) throws Exception{
		//Pesquisa dados da pend�ncia
		PendenciaDt pendenciaDt = movimentacaoNe.consultarPendenciaId(id_Pendencia);
		analisePendenciaDt.addPendenciasFechar(pendenciaDt);

		//Resgata processo ligado a pend�ncia
		ProcessoDt processoDt = movimentacaoNe.consultarProcessoIdCompleto(pendenciaDt.getId_Processo());
		pendenciaDt.setProcessoDt(processoDt);

		setFluxoRedirecionamento(analisePendenciaDt, fluxo, fluxoPreAnalise, tipoPendencia);

		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");
	}

	/**
	 * Consulta pend�ncias n�o analisadas
	 * @throws Exception 
	 */
	protected void consultarPendenciasNaoAnalisadas(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao, int passoEditar, int fluxo) throws Exception{
		if (passoEditar == 0) {//Significa que foi acionado Bot�o Limpar
			analisePendenciaDt.setNumeroProcesso("null");
		}

		//Verifica se usu�rio pode analisar
		request.setAttribute("podeAnalisar", usuarioSessao.getVerificaPermissao(AnalisePendenciaDt.CodigoPermissao));
		request.setAttribute("podePreAnalisar", usuarioSessao.getVerificaPermissao(PreAnalisePendenciaDt.CodigoPermissao));
		request.setAttribute("numeroProcesso", analisePendenciaDt.getNumeroProcesso());
		
		if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)
			request.setAttribute("podeGerarVotoEmenta", "true");

		//Verifica se usu�rio digitou "." e d�gito verificador
		if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
			request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
			return;
		}

		List tempList = movimentacaone.consultarPendenciasNaoAnalisadas(usuarioSessao.getUsuarioDt(), analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getId_PendenciaTipo());
		if (tempList!=null && tempList.size() > 0) {
			request.setAttribute("ListaPendencias", tempList);
			request.setAttribute("PaginaAtual", Configuracao.Localizar);
		} else {
			if (request.getAttribute("MensagemErro").equals("") && request.getAttribute("MensagemOk").equals("")) request.setAttribute("MensagemErro", "N�o h� Pend�ncias em Aberto.");
		}

	}

	/**
	 * Consulta pend�ncias analisadas
	 * @throws Exception 
	 */
	protected boolean consultarPendenciasAnalisadas(HttpServletRequest request, AnalisePendenciaDt analisePendenciaDt, UsuarioDt usuarioDt, MovimentacaoNe movimentacaone) throws Exception{
		boolean boRetorno = false;

		if (analisePendenciaDt.getNumeroProcesso().length() > 0 || (analisePendenciaDt.getDataInicial().length() > 0 && analisePendenciaDt.getDataFinal().length() > 0)) {

			//Verifica se usu�rio digitou "." e d�gito verificador
			if (analisePendenciaDt.getNumeroProcesso().length() > 0 && analisePendenciaDt.getNumeroProcesso().indexOf(".") < 0) {
				request.setAttribute("MensagemErro", "N�mero do Processo no formato incorreto. ");
				return boRetorno;
			}

			List tempList = movimentacaone.consultarPendenciasAnalisadas(usuarioDt, analisePendenciaDt.getNumeroProcesso(), analisePendenciaDt.getDataInicial(), analisePendenciaDt.getDataFinal());
			if (tempList != null && tempList.size() > 0) {
				request.setAttribute("ListaConclusoes", tempList);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				boRetorno = true;
			} else request.setAttribute("MensagemErro", "Nenhuma Pend�ncia Localizada.");
		} else request.setAttribute("MensagemErro", "Informe par�metros para consulta: Data Inicial e Data Final ou N�mero do Processo.");
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
	 * Consulta tipos de movimenta��o filtrando pelo grupo do usu�rio logado
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
		} else request.setAttribute("MensagemErro", "Nenhum Tipo de Movimenta��o Localizado.");
		return boRetorno;
	}

	/**
	 * Consulta de modelos Se n�o encontrar nenhum modelo retorna false
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
	 * Consulta tipos de arquivos vinculados ao grupo do usu�rio logado
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

		//Captura par�metros consulta		
		if (request.getParameter("numeroProcesso") != null) analisePendenciaDt.setNumeroProcesso(request.getParameter("numeroProcesso"));
		if (request.getParameter("DataInicial") != null) analisePendenciaDt.setDataInicial(request.getParameter("DataInicial"));
		if (request.getParameter("DataFinal") != null) analisePendenciaDt.setDataFinal(request.getParameter("DataFinal"));
		if (request.getParameter("preAnalise") != null && request.getParameter("preAnalise").equals("true")) analisePendenciaDt.setPreAnalise(true);
		if (request.getParameter("preAnaliseConclusao") != null && request.getParameter("preAnaliseConclusao").equals("true")) analisePendenciaDt.setPreAnalise(true);

		// Quando modelo foi selecionado monta conte�do para aparecer no editor e j� carrego o tipo do arquivo
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
	 * Verifica se o tipo de movimenta��o selecionado � um tipo de movimenta��o j� configurado para o usu�rio logado
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
