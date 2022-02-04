package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EmpresaTipoDt;
import br.gov.go.tj.projudi.dt.EscolaridadeDt;
import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.GovernoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.PartesIsentaDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteTipoNe;
import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que controla o cadastramento de ações do 2º grau / recursos inominados
 * @author msapaula
 */
public class ProcessoSegundoGrauCivelCt extends ProcessoComumCt {

	private static final long serialVersionUID = 1886200047612471215L;

	public ProcessoSegundoGrauCivelCt() {
	}

	public int Permissao() {
		return ProcessoCadastroDt.CodigoPermissaoProcessoSegundoGrau;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoCadastroDt processoDt;
		ProcessoParteDt ProcessoPartedt = null;
		ProcessoNe Processone;
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		String Mensagem = "";
		int passoEditar = -1;
		int parteTipo = 0;
		int paginaAnterior = 0;
		int fluxo = 0;
		String posicaoLista = "";
		String stAcao;
		String cpfCnpj = "";
		List tempList = null;
		String parteDependenteJaSelecionada = "false";
		boolean turmaJulgadora = false;
		
		if (request.getSession().getAttribute("parteDependenteJaSelecionada") != null){
			parteDependenteJaSelecionada = String.valueOf(request.getSession().getAttribute("parteDependenteJaSelecionada"));
		}
		
		if (request.getSession().getAttribute("turmaJulgadora") != null){
			turmaJulgadora = Funcoes.StringToBoolean(String.valueOf(request.getSession().getAttribute("turmaJulgadora")));
		}

		request.setAttribute("tempPrograma", "Processo de Segundo Grau");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");

		Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null) Processone = new ProcessoNe();

		processoDt = (ProcessoCadastroDt) request.getSession().getAttribute("ProcessoCadastroDt");
		if (processoDt == null) processoDt = new ProcessoCadastroDt();

		if (request.getSession().getAttribute("ProcessoPartedt") != null) ProcessoPartedt = (ProcessoParteDt) request.getSession().getAttribute("ProcessoPartedt");
		else ProcessoPartedt = new ProcessoParteDt();
		
		if (request.getParameter("nomeArquivo")!= null )
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
			request.setAttribute("nomeArquivo", "");
		
		if (request.getParameter("fluxo") != null)
			fluxo = Funcoes.StringToInt(request.getParameter("fluxo"));
		
		//Variáveis de sessão
 		GuiaEmissaoDt guiaEmissaoDt = null;
 		guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt");
 		if( guiaEmissaoDt == null ) {
 			guiaEmissaoDt = new GuiaEmissaoDt();
 		}
 		
		request.setAttribute("exibeOficialCompanheiro", new Boolean(true)); //Conforme informado pelo Marcelo da Corregedoria, só é realizado o cálculo em dobro automático na guia inicial.
 		//...............

		processoDt.setProcessoNumero(request.getParameter("ProcessoNumeroDependente"));
		processoDt.setId_Comarca(request.getParameter("Id_Comarca"));
		processoDt.setComarca(request.getParameter("Comarca"));
		processoDt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));
		processoDt.setId_AreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
		processoDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		processoDt.setForumCodigo(request.getParameter("ForumCodigo"));
		processoDt.setId_ServentiaSubTipo(request.getParameter("Id_ServentiaSubTipo"));
		if(request.getParameter("Id_ProcessoTipo") != null && !request.getParameter("Id_ProcessoTipo").toString().equalsIgnoreCase("")){
			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(request.getParameter("Id_ProcessoTipo"));
			if(processoTipoDt.isHabeasCorpus()) {
			request.setAttribute("MensagemOk", "ATENÇÂO - Favor informar para a ação de habeas corpus: na parte ativa, o impetrante da ação; na parte passiva, a autoridade coatora; e, em outras partes, a parte do tipo PACIENTE que receberá o beneficio da ação.");
			}
		}
		processoDt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		processoDt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		processoDt.setId_ProcessoPrioridade(request.getParameter("Id_ProcessoPrioridade"));
		processoDt.setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
		processoDt.setProcessoPrioridadeCodigo(request.getParameter("ProcessoPrioridadeCodigo"));
		processoDt.setValor(request.getParameter("Valor"));
		if (request.getParameter("SegredoJustica") != null) processoDt.setSegredoJustica(request.getParameter("SegredoJustica"));
		else processoDt.setSegredoJustica("false");
		processoDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		processoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		processoDt.setId_Modelo(request.getParameter("Id_Modelo"));
		processoDt.setModelo(request.getParameter("Modelo"));
		processoDt.setId_Assunto(request.getParameter("Id_Assunto"));
		processoDt.setAssunto(request.getParameter("Assunto"));
		processoDt.setTextoEditor(request.getParameter("TextoEditor"));
		processoDt.setOabNumero(request.getParameter("OabNumero"));
		processoDt.setOabComplemento(request.getParameter("OabComplemento"));
		processoDt.setOabUf(request.getParameter("OabUf"));
		processoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		processoDt.setSigiloso(request.getParameter("Sigiloso"));		
		
		//Variáveis da tela de processo comum (radios)
		if (processoDt.getGrauProcesso()!=null && request.getParameter("grauProcesso") != null
				&& !processoDt.getGrauProcesso().equalsIgnoreCase(request.getParameter("grauProcesso"))){
			limpaProcesso(UsuarioSessao, processoDt, Processone);
			
			int GrupoTipoCodigo = UsuarioSessao.getGrupoTipoCodigoToInt();
			int GrupoCodigo = UsuarioSessao.getGrupoCodigoToInt();
			if (GrupoTipoCodigo == GrupoTipoDt.ADVOGADO ||	GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo == GrupoDt.MP_TCE) {
				adicionaAdvogadoProcesso(processoDt, UsuarioSessao);
			}
		}
		
		if (request.getParameter("assistenciaProcesso") != null && (request.getParameter("assistenciaProcesso").equalsIgnoreCase("1") || request.getParameter("assistenciaProcesso").equalsIgnoreCase("2") || request.getParameter("assistenciaProcesso").equalsIgnoreCase("3")) ){
			if (!processoDt.getAssistenciaProcesso().equalsIgnoreCase(request.getParameter("assistenciaProcesso"))){
				request.getSession().removeAttribute("GuiaEmissaoDt");
				processoDt.limparProcessoTrocarCustaTipo();
				
				int GrupoTipoCodigo = UsuarioSessao.getGrupoTipoCodigoToInt();
				int GrupoCodigo = UsuarioSessao.getGrupoCodigoToInt();
				if (GrupoTipoCodigo == GrupoTipoDt.ADVOGADO ||	GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo == GrupoDt.MP_TCE) {
					adicionaAdvogadoProcesso(processoDt, UsuarioSessao);
				}
			}
		}
		
		//Se processo de Segundo Grau redireciona
		if (request.getParameter("grauProcesso") != null && request.getParameter("grauProcesso").equals("1")) {
			redireciona(response, "ProcessoCivel?PaginaAtual=" + Configuracao.Editar );
		}
				
		processoDt.setGrauProcesso(request.getParameter("grauProcesso"));
		processoDt.setTipoProcesso(request.getParameter("tipoProcesso"));
		processoDt.setAssistenciaProcesso(request.getParameter("assistenciaProcesso"));
		processoDt.setDependenciaProcesso(request.getParameter("dependenciaProcesso"));
		
		List listaArquivosInseridos = getListaArquivos(request); // Captura arquivos inseridos

		// Variáveis auxiliares
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("ParteTipo") != null) parteTipo = Funcoes.StringToInt(request.getParameter("ParteTipo"));
		posicaoLista = request.getParameter("posicaoLista");
		
		//controle de tela -- limpa promentes selecionados anteriormente quando a opção selecionada for Isento
		if (request.getParameter("assistenciaProcesso") != null && request.getParameter("assistenciaProcesso").equalsIgnoreCase("3")){
			processoDt.setId_Custa_Tipo(String.valueOf(processoDt.ISENTO));
		} 
		
		// Seta Tipo de Custa do Processo
		if (request.getParameter("custaTipo") != null) {
			if (request.getParameter("custaTipo").equals("1")) {
				processoDt.setId_Custa_Tipo(String.valueOf(processoDt.COM_CUSTAS));
				// Exibe a opção de gerar locomoções
				request.setAttribute("exibeparteLocomocoes", false);
			} else if (request.getParameter("custaTipo").equals("2")) {
				processoDt.setId_Custa_Tipo(String.valueOf(processoDt.ASSISTENCIA_JURIDICA));
				// Exibe a opção de gerar locomoções
				request.setAttribute("exibeparteLocomocoes", true);
			} else if(request.getParameter("custaTipo").equals("3")){
				processoDt.setId_Custa_Tipo(String.valueOf(processoDt.ISENTO));
				// Exibe a opção de gerar locomoções
				request.setAttribute("exibeparteLocomocoes", false);
			}
		} else if (processoDt.getId_Custa_Tipo() != null && processoDt.getId_Custa_Tipo().equalsIgnoreCase("2")){
			// Exibe a opção de gerar locomoções
			request.setAttribute("exibeparteLocomocoes", true);
		} else {
			// Exibe a opção de gerar locomoções
			request.setAttribute("exibeparteLocomocoes", false);
		}
		
		//Se processo de Segundo Grau seta COMARCA Goiânia
		if (request.getParameter("grauProcesso") != null) {
			if(request.getParameter("grauProcesso").equals("3")) {
				turmaJulgadora = true;
			}else if(request.getParameter("grauProcesso").equals("2")) {
				turmaJulgadora = false;
				ComarcaDt goiania = new ComarcaNe().consultarComarcaCodigo(ComarcaDt.GOIANIA);
				processoDt.setId_Comarca(goiania.getId());
				processoDt.setComarca(goiania.getComarca());
			}
		} else if (processoDt.getGrauProcesso() != null){
			if(processoDt.getGrauProcesso().equals("3")) {
				turmaJulgadora = true;
			}else if(processoDt.getGrauProcesso().equals("2")) {
				turmaJulgadora = false;
				ComarcaDt goiania = new ComarcaNe().consultarComarcaCodigo(ComarcaDt.GOIANIA);
				processoDt.setId_Comarca(goiania.getId());
				processoDt.setComarca(goiania.getComarca());
			}
		}

		setParametrosAuxiliares(processoDt, ProcessoPartedt, passoEditar, paginaatual, request, paginaAnterior, Processone, UsuarioSessao);

		// Resgata se processo é dependente
		if (request.getParameter("dependente") != null) processoDt.setProcessoDependente(Funcoes.StringToBoolean(request.getParameter("dependente")));
		if (processoDt.isProcessoDependente()) stAcao = "/WEB-INF/jsptjgo/ProcessoComum.jsp";
		else stAcao = "/WEB-INF/jsptjgo/ProcessoComum.jsp";
		
		//if (processoDt.isProcessoDependente()) stAcao = "/WEB-INF/jsptjgo/ProcessoSegundoGrauCivelVinculo.jsp";
		//else stAcao = "/WEB-INF/jsptjgo/ProcessoSegundoGrauCivel.jsp";
		
		//Utilizado no cadastro de Partes
		request.setAttribute("exibeGovernoEmpresa", true);	
		
		// Exibe a opção de não informar o endereço no cadastro de Partes
		request.setAttribute("exibeparteEnderecoDesconhecido", true);
			
		if (request.getParameter("parteLocomocoes") != null) ProcessoPartedt.setParteLocomocoes(request.getParameter("parteLocomocoes"));		

		switch (paginaatual) {
		
			case Configuracao.Curinga9: 
			
				//Add Partes Recorrentes e Limpa as Partes Dependentes do Processo.
				if (request.getParameterValues("Recorrente") != null){
					addPartesRecorrentes (request, processoDt, UsuarioSessao);
				}
				
				parteDependenteJaSelecionada = "true";
					
				break;

			// Novo Processo, limpar todos os dados
			case Configuracao.Novo:
				
				//limpa dados do processo civel
				super.limpaProcesso(UsuarioSessao, processoDt, Processone);
				
				ProcessoPartedt.limpar();
				
				//leandro*********************
				this.limparGuia(guiaEmissaoDt);
				request.getSession().removeAttribute("GuiaEmissaoDt");
				//**********************************
				
				// Seta área do processo
				processoDt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));

				if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ADVOGADO 
						|| Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.MINISTERIO_PUBLICO ) 
					adicionaAdvogadoProcesso(processoDt, UsuarioSessao);

				
				if (UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo() != null && UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo().length() > 0
						&&Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.PROMOTORIA){
					request.setAttribute("MensagemOk", "ATENÇÂO - Para o caso de Substituto Processual,  o  Ministério Público deve ser incluído em Substituto  Processual  /  Outras Partes; No campo parte tipo escolha Substituto Processual.");
				}
				
				//Limpa lista DWR e zera contador Arquivos
				request.getSession().removeAttribute("ListaArquivosDwr");
				request.getSession().removeAttribute("ListaArquivos");
				request.getSession().removeAttribute("Id_ListaArquivosDwr");
				
				passoEditar = -1;
				
				break;

			// Remover partes ou advogados inseridos
			case Configuracao.Excluir:
				if (posicaoLista.length() > 0) {
					int posicao = Funcoes.StringToInt(posicaoLista);
					if (passoEditar == 10) {
						//Refere-se a exclusão de advogados
						processoDt.getListaAdvogados().remove(posicao);
						stAcao = "/WEB-INF/jsptjgo/BuscaAdvogado.jsp";
					} else excluirParteProcesso(processoDt, posicao, parteTipo);
					passoEditar = -1;
				}
				break;

			// Salvar dados do Processo
			case Configuracao.SalvarResultado:
				// Seta área do processo
				processoDt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));
				
				Mensagem = Processone.verificarProcessoSegundoGrauCivel(processoDt, UsuarioSessao.getUsuarioDt());

				if (Mensagem.length() == 0) {
					Processone.cadastrarProcessoSegundoGrau(processoDt, UsuarioSessao.getUsuarioDt(), guiaEmissaoDt);

					// Redireciona para jsp de Processo Cadastrado com sucesso
					stAcao = "/WEB-INF/jsptjgo/ProcessoSegundoGrauCadastrado.jsp";

					//Joga processo no request e limpa da sessão
					request.setAttribute("ProcessoCadastroDt", processoDt);
					processoDt = new ProcessoCadastroDt();
					ProcessoPartedt = new ProcessoParteDt();
					guiaEmissaoDt = new GuiaEmissaoDt();

					//Limpa lista DWR e zera contador Arquivos
					request.getSession().removeAttribute("ListaArquivosDwr");
					request.getSession().removeAttribute("ListaArquivos");
					request.getSession().removeAttribute("Id_ListaArquivosDwr");
				}
				break;

			//Equivale a opção Ver Presidente/Relator
			case Configuracao.Imprimir:
				mostrarDadosProcesso(request, response, Processone);
				return;
				
			//Busca do Processo Dependente será feita aqui agora.
			case Configuracao.Localizar:
				
				parteDependenteJaSelecionada = "false";
				
				String processoNumeroVinculo = request.getParameter("ProcessoNumero");
				
				if (processoNumeroVinculo != null && processoNumeroVinculo.length() > 0) {
					processoDt.setProcessoNumeroPrincipal(processoNumeroVinculo);
					//Consulta dados completos do processo de 1º grau
					ProcessoDt processoVinculoDt = Processone.consultarProcessoDigitalEFisico(processoNumeroVinculo);						
					
					if (processoVinculoDt != null) {
						
						processoDt.setProcessoDependente(true);
						
						if(StringUtils.isNotEmpty(processoVinculoDt.getId())) {
							processoDt.setProcessoDependenteDt(processoVinculoDt);
							processoDt.setId_ProcessoPrincipal(processoVinculoDt.getId());
							
							if (processoDt.isMesmoGrauJurisdicao() && processoDt.isProcessoMesmaArea()){
								//Seta dados da área relacionada
								processoDt.setId_AreaDistribuicao(processoVinculoDt.getId_AreaDistribuicao());
								processoDt.setAreaDistribuicao(processoVinculoDt.getAreaDistribuicao());
								processoDt.setForumCodigo(processoVinculoDt.getForumCodigo());
								
								getAssuntosProcessoOriginario(processoDt, processoVinculoDt.getId(), Processone);
							}
						}else if (StringUtils.isNotEmpty(processoVinculoDt.getProcessoFisicoTipo())) {
							processoDt.setProcessoFisicoTipo(processoVinculoDt.getProcessoFisicoTipo());
							processoDt.setProcessoFisicoNumero(processoVinculoDt.getProcessoFisicoNumero());
							processoDt.setProcessoFisicoComarcaNome(processoVinculoDt.getProcessoFisicoComarcaNome());
							processoDt.setProcessoFisicoComarcaCodigo(processoVinculoDt.getProcessoFisicoComarcaCodigo());
						} else {
							super.limpaProcesso(UsuarioSessao, processoDt, Processone);
							request.setAttribute("MensagemErro", "Não foi possível localizar o Processo informado.");
						}


					} else {
						super.limpaProcesso(UsuarioSessao, processoDt, Processone);
						request.setAttribute("MensagemErro", "Não foi possível localizar o Processo informado.");
					}
				} else {
					request.setAttribute("MensagemErro", "Número do Processo deve ser informado. ");
				}
				break;
			

				//Consulta as Comarcas disponíveis
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo") == null) {
						String[] lisNomeBusca = { "Comarca" };
						String[] lisDescricao = { "Comarca" };
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_Comarca");
						request.setAttribute("tempBuscaDescricao", "Comarca");
						request.setAttribute("tempBuscaPrograma", "Comarca");
						request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
						request.setAttribute("tempDescricaoId", "Id_Comarca");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", String.valueOf(ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						processoDt.setId_AreaDistribuicao("null");
						processoDt.setAreaDistribuicao("null");
						processoDt.setId_ProcessoTipo("null");
						processoDt.setProcessoTipo("");
						processoDt.setId_Assunto("null");
						processoDt.setAssunto("");
						processoDt.setListaAssuntos(null);
						passoEditar = -1;
					} else {
						String stTemp = "";
						stTemp = Processone.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
						enviarJSON(response, stTemp);
						return;
					}
				break;
			
				//Consulta partes isentas disponíveis
			case (PartesIsentaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Partes Isentas"};
					String[] lisDescricao = {"Partes Isentas", "CNPJ"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PartesIsentas");
					request.setAttribute("tempBuscaDescricao","Nome");
					request.setAttribute("tempBuscaMensagem","Caso a parte isenta não esteja nesta relação, favor formular requerimento por escrito, endereçado à Presidência do Tribunal de Justiça com os devidos documentos comprobatórios para efeito de análise e inclusão no PJD - Processo Judicial Digital.");
					
					String[] camposHidden = {"Cnpj"};
					request.setAttribute("camposHidden",camposHidden);
					
					request.setAttribute("tempBuscaPrograma","Partes Isentas");
					request.setAttribute("tempRetorno","ProcessoSegundoGrauCivel");
					request.setAttribute("tempDescricaoId","Id");
					parteTipo = 1;
					passoEditar = 3;
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",(PartesIsentaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = Processone.consultarPartesIsentaslJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;

			//Consulta as áreas de distribuição disponíveis para a comarca escolhida
			case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (processoDt.getId_Comarca().length() > 0) {
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"AreaDistribuicao"};
						String[] lisDescricao = {"AreaDistribuicao","ForumCodigo","Id_ServentiaSubTipo"};				
						//quando for necessário retornar outros valos além do id, coloque outras colunas de descrição
						// na localizar.jsp as descrições geram novos input hidem para retornar ao ct
						// na funcoes.js as descricoes serão usadas para gerar os AlterarValue para retornar para o ct
						String[] camposHidden = {"ForumCodigo","Id_ServentiaSubTipo"};
						request.setAttribute("camposHidden",camposHidden);
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_AreaDistribuicao");
						request.setAttribute("tempBuscaDescricao", "AreaDistribuicao");
						request.setAttribute("tempBuscaPrograma", "AreaDistribuicao");
						request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
						request.setAttribute("tempDescricaoId", "Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual",  String.valueOf(AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						passoEditar = -1;
					}else{
						String stTemp = "";
						stTemp =Processone.consultarAreasDistribuicaoSegundoGrauCivelJSON(stNomeBusca1, processoDt.getId_Comarca(),  PosicaoPaginaAtual, turmaJulgadora);													
						enviarJSON(response, stTemp);						
						
						return;
					}
				}else {
					request.setAttribute("MensagemErro", "Selecione primeiramente a Comarca.");
				}
				processoDt.setId_ProcessoTipo("null");
				processoDt.setProcessoTipo("");
				processoDt.setId_Assunto("null");
				processoDt.setAssunto("");
				processoDt.setListaAssuntos(null);
			break;

			// Consulta tipos de processo disponíveis na área de distribuição escolhida
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (processoDt.getAreaDistribuicao().length() > 0 || processoDt.isProcessoDependente()) {
					if (request.getParameter("Passo") == null) {
						String[] lisNomeBusca = {"Classe"};
						String[] lisDescricao = {"Classe", "Codigo da Classe"};
						stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId", "Id_ProcessoTipo");
						request.setAttribute("tempBuscaDescricao", "ProcessoTipo");
						request.setAttribute("tempBuscaPrograma", "ProcessoTipo");
						request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
						request.setAttribute("tempDescricaoId", "Id_ProcessoTipo");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						passoEditar = -1;
					} else {
						String stTemp = "";
						stTemp = Processone.consultarDescricaoProcessoTipoJSON(stNomeBusca1, processoDt.getId_AreaDistribuicao(), UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
													
						enviarJSON(response, stTemp);						
						
						return;
					}
				} else {
					request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
				}
				processoDt.setId_Assunto("null");
				processoDt.setAssunto("");
				processoDt.setListaAssuntos(null);
				break;
			
			case (EscolaridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Escolaridade"};
					String[] lisDescricao = {"Escolaridade"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Escolaridade");
					request.setAttribute("tempBuscaDescricao", "Escolaridade");
					request.setAttribute("tempBuscaPrograma", "Escolaridade");
					request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
					request.setAttribute("tempDescricaoId", "Id_Escolaridade");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(EscolaridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);	
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp =Processone.consultarDescricaoEscolaridadeJSON(tempNomeBusca, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);					
					
					return;
				}
				break;

			// Consulta Assuntos
			case (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (processoDt.getProcessoTipo().length() > 0) {
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Assunto", "Código CNJ"};
						String[] lisDescricao = {"Assunto","Pai","Disp. Legal", "Código CNJ"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Assunto");
						request.setAttribute("tempBuscaDescricao","Assunto");
						request.setAttribute("tempBuscaPrograma","Assunto");			
						request.setAttribute("tempRetorno","ProcessoSegundoGrauCivel");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						passoEditar = -1;
					} else {
						String stTemp = "";
					
						if (processoDt.isProcessoDependente()){
							if (processoDt.isMesmoGrauJurisdicao() && processoDt.isProcessoMesmaArea()){								
								stTemp = Processone.consultarDescricaoAssuntoJSON(stNomeBusca1, stNomeBusca2, processoDt.getProcessoDependenteDt().getId_ServentiaOrigemRecurso(), PosicaoPaginaAtual);
							}else {								
								stTemp = Processone.consultarDescricaoAssuntoJSON(stNomeBusca1, stNomeBusca2, processoDt.getProcessoDependenteDt().getId_Serventia(), PosicaoPaginaAtual);
							}
							
						} else {
							if (processoDt.getId_AreaDistribuicao().length() > 0) 								
								stTemp = Processone.consultarAssuntosAreaDistribuicaoJSON(stNomeBusca1, stNomeBusca2, processoDt.getId_AreaDistribuicao(), PosicaoPaginaAtual);
							else {
								request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
							}
						}					
											
						enviarJSON(response, stTemp);
											
						return;								
					}
				} else {
					request.setAttribute("MensagemErro", "Selecione primeiramente a Classe.");
				}
				break;

			// Consulta tipos de prioridade disponíveis
			case (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoPrioridade"};
					String[] lisDescricao = {"ProcessoPrioridade", "Codigo"};
					String[] camposHidden = {"ProcessoPrioridadeCodigo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoPrioridade");
					request.setAttribute("tempBuscaDescricao","ProcessoPrioridade");
					request.setAttribute("tempBuscaPrograma","ProcessoPrioridade");			
					request.setAttribute("tempRetorno","ProcessoSegundoGrauCivel");		
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = -1;
				} else{
					String stTemp="";
					stTemp = Processone.consultarDescricaoProcessoPrioridadeJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);					
					
					return;								
				}
				break;

			//Consulta de Tipos de Parte
			case (ProcessoParteTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoParteTipo"};
					String[] lisDescricao = {"ProcessoParteTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoParteTipo");
					request.setAttribute("tempBuscaDescricao", "ProcessoParteTipo");
					request.setAttribute("tempBuscaPrograma", "ProcessoParteTipo");
					request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoParteTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = Processone.consultarOutrosTiposPartesJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);					
					
					return;
				}
				break;

			// Consulta de Cidades - Naturalidade usada no cadastro de Partes
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = Processone.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);						
					
					return;
				}
			break;

			// Consulta de Estado Civil - Usado no cadastro de partes
			case (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Estado Civil"};
					String[] lisDescricao = {"Estado Civil"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPermissao = String.valueOf(EstadoCivilDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_EstadoCivil", "EstadoCivil", "EstadoCivil", "ProcessoSegundoGrauCivel", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = Processone.consultarDescricaoEstadoCivilJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);					
					
					return;
				}
			break;

			// Consulta de Profissão - Usado no cadastro de partes
			case (ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Profissao"};
					String[] lisDescricao = {"Profissao"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Profissao");
					request.setAttribute("tempBuscaDescricao", "Profissao");
					request.setAttribute("tempBuscaPrograma", "Profissao");
					request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = Processone.consultarDescricaoProfissaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);						
					
					return;
				}
				break;

			// Consulta de Orgao Expedidor - Usado no cadastro de partes
			case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"OrgaoExpedidor","Sigla"};
					String[] lisDescricao = {"Sigla","Nome","Estado"};
					request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
					request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = Processone.consultarDescricaoOrgaoExpedidorJSON(stNomeBusca2, stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);					
					
					return;
				}
				break;

			// Consulta de Estado da CTPS - Usado no cadastro de partes
			case (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Uf"};
					String[] lisDescricao = {"Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_CtpsUf");
					request.setAttribute("tempBuscaDescricao", "EstadoCtpsUf");
					request.setAttribute("tempBuscaPrograma", "EstadoCtpsUf");
					request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = Processone.consultarDescricaoEstadoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);					
					
					return;
				}
				break;

			// Consulta de Bairro - Usado no endereço da parte
			case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","Uf"};
					String[] lisDescricao = {"Bairro","Cidade","Uf","Zona","Região"};
					String[] camposHidden = {"BairroCidade","BairroUf"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("ParteTipo", parteTipo);
					passoEditar = 4;
				} else{
					String stTemp = "";
					stTemp = Processone.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);						
					
					return;
				}
				break;
				
			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			
				if (request.getParameter("Passo")==null){
					
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"Tipo de Arquivo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPermissaoPaginaAtual = String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					
					atribuirJSON(request, "Id_ArquivoTipo", "ArquivoTipo", "Tipode de Arquivo",  "ProcessoSegundoGrauCivel?PassoEditar=0", Configuracao.Editar, stPermissaoPaginaAtual, lisNomeBusca, lisDescricao);										
				
				}else{
					
					String stTemp = "";
					stTemp = Processone.consultarDescricaoArquivoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);
					return;
				}		
			
				break;

			// Consultar Advogados
			case (UsuarioServentiaOabDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if(fluxo == 1){
					//consultarAdvogado(request, Processone, ProcessoCiveldt);
					if (processoDt.getOabNumero().length() > 0 && processoDt.getOabComplemento().length() > 0 && processoDt.getOabUf().length() > 0) {
						tempList = UsuarioSessao.consultarAdvogadoOab(processoDt.getOabNumero(), processoDt.getOabComplemento(), processoDt.getOabUf());
						
						if (tempList != null && tempList.size()>0) {
							request.getSession().setAttribute("ListaAdvogadosHabilitacao", tempList);
						} else{
							tempList = null;
							request.getSession().removeAttribute("ListaAdvogadosHabilitacao");
							request.setAttribute("MensagemErro", "Nenhum Advogado encontrado com os dados informados.");
						}
					
					} else{
						tempList = null;
						request.getSession().removeAttribute("ListaAdvogadosHabilitacao");
						request.setAttribute("MensagemErro", "Informe o Número da OAB, Complemento e Estado.");
					}
				
				} else if (fluxo == 2){
					String id_UsuariosServentiaAdvogado = "";
					if (request.getParameter("usuariosServentiaAdvogado") != null && !request.getParameter("usuariosServentiaAdvogado").equals("")) {
						id_UsuariosServentiaAdvogado = request.getParameter("usuariosServentiaAdvogado").toString();
						tempList = (List) request.getSession().getAttribute("ListaAdvogadosHabilitacao");
						
						for (Iterator iterator = tempList.iterator(); iterator.hasNext();) {
							UsuarioDt usuarioDt = (UsuarioDt) iterator.next();
							if (id_UsuariosServentiaAdvogado.equalsIgnoreCase(usuarioDt.getId_UsuarioServentia())){
								
								boolean naoExistente = true;
								
								if (processoDt.getListaAdvogados() != null){
									for (Iterator iterator2 = processoDt.getListaAdvogados().iterator(); iterator2.hasNext();) {
										UsuarioServentiaOabDt aux = (UsuarioServentiaOabDt) iterator2.next();
										if (aux.getId_UsuarioServentia().equalsIgnoreCase(usuarioDt.getId_UsuarioServentia())){
											naoExistente = false;
											break;
										}
									}
								}
								
								if (naoExistente){
									UsuarioServentiaOabDt usuarioServentiaOabDt = new UsuarioServentiaOabDt();
									usuarioServentiaOabDt.setNomeUsuario(usuarioDt.getNome());
									usuarioServentiaOabDt.setOabNumero(usuarioDt.getOabNumero());
									usuarioServentiaOabDt.setOabComplemento(usuarioDt.getOabComplemento());
									usuarioServentiaOabDt.setServentia(usuarioDt.getServentia());
									usuarioServentiaOabDt.setId_UsuarioServentia(usuarioDt.getId_UsuarioServentia());
									processoDt.addListaAdvogados(usuarioServentiaOabDt);
									
									processoDt.setOabNumero("");
									processoDt.setOabComplemento("");
									processoDt.setOabUf("");
									tempList = null;
									request.getSession().removeAttribute("ListaAdvogadosHabilitacao");
								} else {
									request.setAttribute("MensagemErro", "Perfil já foi selecionado.");
								}
								 
								break;
							}
						}
					} else {
						request.setAttribute("MensagemErro", "Nenhum Advogado selecionado.");
					}
					
				}
				stAcao = "/WEB-INF/jsptjgo/BuscaAdvogado.jsp";
				break;
				
			// Consulta de Governo Tipo - Usado no cadastro de partes			
			case (GovernoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"GovernoTipo"};
					String[] lisDescricao = {"GovernoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_GovernoTipo");
					request.setAttribute("tempBuscaDescricao", "GovernoTipo");
					request.setAttribute("tempBuscaPrograma", "GovernoTipo");
					request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
					request.setAttribute("tempDescricaoDescricao", "GovernoTipo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (GovernoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = Processone.consultarDescricaoGovernoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);						
					
					return;
				}
				break;
				
			// Consulta de Empresa Tipo - Usado no cadastro de partes			
			case (EmpresaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"EmpresaTipo"};
					String[] lisDescricao = {"Tipo de Empresa"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String permissao = String.valueOf(EmpresaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					
					atribuirJSON(request, "Id_EmpresaTipo", "EmpresaTipo", "EmpresaTipo","ProcessoSegundoGrauCivel?PassoEditar=4&ParteTipo=" + parteTipo, Configuracao.Editar , permissao, lisNomeBusca, lisDescricao);
					
				} else {
					String stTemp="";
					stTemp = Processone.consultarDescricaoEmpresaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);						
					
					return;								
				}
				break;					

			default:
				// Função Editar é dividida da seguinte forma: 
				//	Passo 0 : Redireciona para passo 2
				// 	Passo 1 : Redireciona para passo 3
				//	Passo 2 : Busca uma parte 
				//	Passo 3 : Valida dados da parte
				// 	Passo 4 : Redireciona para tela de cadastro de parte
				//	Passo 5 : Valida dados do processo
				// 	Passo 6 : Resgata arquivos inseridos no passo 2 e redireciona para passo 3
				// 	Passo 7 : Redireciona para jsp de busca de partes
				//	Passo 8 : Remove assuntos do processo
				//  Passo 9 : Cadastro de partes não personificáveis
				

				switch (passoEditar) {

					case 0:
						request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel");
						processoDt.setPasso2("Passo 2");
						processoDt.setPasso3("");
						stAcao = "/WEB-INF/jsptjgo/ProcessoPeticionar.jsp";
						break;

					case 1:
						stAcao = "/WEB-INF/jsptjgo/ProcessoSegundoGrauVisualizar.jsp";
						request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
						break;

					case 2: // Busca a parte e exibe dados
						if (request.getParameter("CpfCnpj") != null) cpfCnpj = request.getParameter("CpfCnpj").replaceAll("[/.-]", "").trim();

						//Valida CPF digitado
						Mensagem = Processone.verificaCpfCnpjParte(cpfCnpj);
						if (Mensagem.length() == 0) {
							buscaParte(request, ProcessoPartedt, parteTipo, passoEditar, Processone);
							stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
						} else {
							request.setAttribute("MensagemErro", Mensagem);
							stAcao = "/WEB-INF/jsptjgo/BuscaProcessoParte.jsp";
						}
						break;

					case 3: // Valida dados da parte
						if (request.getParameter("parteEnderecoDesconhecido") != null) ProcessoPartedt.setParteEnderecoDesconhecido(request.getParameter("parteEnderecoDesconhecido"));			
						Mensagem = Processone.VerificarParteProcesso(ProcessoPartedt);
						if (Mensagem.length() == 0) {

							if (!parteProcessoCadastrada(processoDt, ProcessoPartedt)) {
								// Valida o tipo de parte e adicion à lista correspondente
								if (parteTipo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO) processoDt.addListaPoloAtivo(ProcessoPartedt);
								else if (parteTipo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO || parteTipo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) processoDt.addListaPolosPassivos(ProcessoPartedt);
								else processoDt.addListaOutrasPartes(ProcessoPartedt);

								//ProcessoPartedt = new ProcessoParteDt();

							} else {
								request.setAttribute("MensagemErro", "Parte já inserida no processo.");
							}
							passoEditar = -1;
							request.getSession().removeAttribute("Enderecodt");
							request.getSession().removeAttribute("ProcessoPartedt");
							ProcessoPartedt = new ProcessoParteDt();
						} else {
							request.setAttribute("MensagemErro", Mensagem);
							stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
						}
						
						break;

					case 4: //Redireciona para tela de Cadastro de parte
						stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
						break;

					case 5: // Valida dados do processo
						//if (processoDt.isProcessoDependente()){
							//getPartesRecorrentes(request, processoDt, UsuarioSessao);
						//}

						Mensagem = Processone.verificarProcessoSegundoGrauCivel(processoDt, UsuarioSessao.getUsuarioDt());
						if (Mensagem.length() > 0) {
							request.setAttribute("MensagemErro", Mensagem);
							processoDt.setPasso1("Passo 1");
							processoDt.setPasso2("");
							processoDt.setPasso3("");
							passoEditar = -1;
						}else {
							//Se não há erros, passa para passo de inserção de arquivos
							processoDt.setPasso1("Passo 1 OK");
							processoDt.setPasso2("Passo 2");
							stAcao = "/WEB-INF/jsptjgo/ProcessoPeticionar.jsp";
						}
						break;

					case 6: // Resgata lista de arquivos inseridos e adiciona ao processo. Redireciona para confirmação cadastro processo
						if (listaArquivosInseridos != null && listaArquivosInseridos.size() > 0) {
							processoDt.setListaArquivos(listaArquivosInseridos);
							processoDt.setPasso1("Passo 1 OK");
							processoDt.setPasso2("Passo 2 OK");
							processoDt.setPasso3("Passo 3");

							//Gera código do pedido, assim o submit so pode ser executado uma unica vez
							request.setAttribute("__Pedido__", UsuarioSessao.getPedido());

							stAcao = "/WEB-INF/jsptjgo/ProcessoSegundoGrauVisualizar.jsp";
							processoDt.setId_ArquivoTipo("");
							processoDt.setArquivoTipo("");
							processoDt.setId_Modelo("");
							processoDt.setModelo("");
							processoDt.setTextoEditor("");
						} else {
							request.setAttribute("MensagemErro", "É necessário inserir um arquivo para prosseguir.");
							processoDt.setPasso3("");
							stAcao = "/WEB-INF/jsptjgo/ProcessoPeticionar.jsp";
						}
						break;

					case 7:
						stAcao = "/WEB-INF/jsptjgo/BuscaProcessoParte.jsp";
						break;

					case 8:
						removerAssuntoProcesso(processoDt, posicaoLista);
						break;

					case 9:
						//Chama método para inicializar objeto parte
						buscaParte(request, ProcessoPartedt, parteTipo, passoEditar, Processone);
						stAcao = "/WEB-INF/jsptjgo/PartesNaoPersonificaveis.jsp";
						request.setAttribute("tempRetorno", "ProcessoSegundoGrauCivel?PassoEditar=3&ParteTipo=" + parteTipo);
						break;

					//Redireciona para tela de Inclusão de Advogados
					case 10:
						stAcao = "/WEB-INF/jsptjgo/BuscaAdvogado.jsp";
						break;
						
					//Redireciona para tela de Restaurar Dados
					case 11:
						redireciona(response, "ProcessoCadastroRestaurar?tempRetorno=ProcessoSegundoGrauCivel");
						break;
						
					//Salva os dados da primeira tela de cadastro
					case 12:
						String byTemp = Processone.salvarProcesso(processoDt);											   
						enviarProjudi(response, byTemp.getBytes(),"CadastroProcesso");
					    					    												
					    byTemp = null;
					    return;

					default:
						processoDt.setPasso1("Passo 1");
						processoDt.setPasso2("");
						processoDt.setPasso3("");
						break;
				}
		}

		request.setAttribute("fluxo",fluxo);
		request.setAttribute("ParteTipo", parteTipo);
		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("parteEnderecoDesconhecido", String.valueOf(ProcessoPartedt.isParteEnderecoDesconhecido()));
		request.getSession().setAttribute("parteDependenteJaSelecionada", parteDependenteJaSelecionada);
		request.getSession().setAttribute("turmaJulgadora", turmaJulgadora);
		request.getSession().setAttribute("ArquivosInseridos", processoDt.getListaArquivos());

		request.getSession().setAttribute("ProcessoCadastroDt", processoDt);
		request.getSession().setAttribute("Processone", Processone);
		request.setAttribute("permitePesquisaComarcaAreaETipo",  Boolean.TRUE);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Exibe dados do presidente e relator responsáveis pelo processo
	 * @throws Exception 
	 */
	protected void mostrarDadosProcesso(HttpServletRequest request, HttpServletResponse response, ProcessoNe processone) throws Exception{
		//Captura processo que está na sessão buscaProcessoDt
		ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		if (processoDt != null) {
			
			ServentiaCargoDt presidente = null;			
			ServentiaCargoDt relator = null;			
			ServentiaDt serventiaProcesso = processone.consultarIdServentia(processoDt.getId_Serventia()); 
			
			if (ServentiaSubtipoDt.isSegundoGrau(serventiaProcesso.getServentiaSubtipoCodigo())){
				try{
					presidente = processone.getPresidenteSegundoGrau(processoDt.getId_Serventia());
				} catch(MensagemException msg){
					request.setAttribute("MensagemErro", msg.getMessage());
				}
				
				relator = processone.consultarRelator2Grau(processoDt.getId());	
			} else {
				presidente = processone.getPresidenteTurmaRecursal(processoDt.getId_Serventia());		
				
				relator = processone.getRelatorResponsavelProcesso(processoDt.getId(), processoDt.getId_Serventia());	
			}		
			
			if (presidente != null) request.setAttribute("presidente", presidente);
			else request.setAttribute("presidente", "");
			
			if (relator != null) request.setAttribute("relator", relator);
			else request.setAttribute("relator", "");

			RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/ProcessoSegundoGrauDados.jsp");
			dis.include(request, response);
		} else request.setAttribute("MensagemErro", "Não foi possível capturar Presidente e Relator do Processo");
	}

	/**
	 * Busca uma parte de acordo com os parâmetros passados
	 * @throws Exception 
	 */
	@Override
	public void buscaParte(HttpServletRequest request, ProcessoParteDt ProcessoPartedt, int parteTipo, int passoEditar, ProcessoNe processoNe) throws Exception{
		String cpfCnpj = "";

		if (request.getParameter("CpfCnpj") != null) cpfCnpj = request.getParameter("CpfCnpj").replaceAll("[/.-]", "");

		ProcessoPartedt = new ProcessoParteDt();

		if (cpfCnpj.length() > 0) {

			ProcessoPartedt = processoNe.consultarParte(cpfCnpj, "", "", "", "");

			if (ProcessoPartedt.getId_ProcessoParte().length() == 0) {
				if (cpfCnpj.length() == 11) ProcessoPartedt.setCpf(cpfCnpj);
				if (cpfCnpj.length() == 14) ProcessoPartedt.setCnpj(cpfCnpj);
			}
		}
		ProcessoPartedt.setProcessoParteTipoCodigo(String.valueOf(parteTipo));
		if (parteTipo > -1){
			// consultar a descrição do tipo da parte
			ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarProcessoParteTipoCodigo(String.valueOf(parteTipo));
			ProcessoPartedt.setProcessoParteTipo(processoParteTipoDt.getProcessoParteTipo());
		}
		if (passoEditar == 9) ProcessoPartedt.setParteNaoPersonificavel(true);
		request.getSession().setAttribute("Enderecodt", ProcessoPartedt.getEnderecoParte());
		request.getSession().setAttribute("ProcessoPartedt", ProcessoPartedt);
	}

	@Override
	public String obtenhaAcaoDefault(ProcessoCadastroDt processoCadastroDt) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected GuiaEmissaoDt limparGuia(GuiaEmissaoDt guiaEmissaoDt) {
		List listaBairroDt;
		List listaQuantidadeBairroDt;
		guiaEmissaoDt = new GuiaEmissaoDt();
		
		guiaEmissaoDt.setRequerente("");
		guiaEmissaoDt.setRequerido("");
		guiaEmissaoDt.setComarca("");
		guiaEmissaoDt.setAreaDistribuicao("");
		guiaEmissaoDt.setProcessoTipo("");
		guiaEmissaoDt.setId_NaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPG("");
		guiaEmissaoDt.setNumeroProcessoDependente("");
		guiaEmissaoDt.setCodigoArea(new String(""+AreaDt.CIVEL));
		guiaEmissaoDt.setBensPartilhar(new String(""+GuiaEmissaoDt.VALOR_NAO));
		guiaEmissaoDt.setCodigoGrau(String.valueOf(GuiaTipoDt.ID_INICIAL_SEGUNDO_GRAU));
		guiaEmissaoDt.setDistribuidorQuantidade("1");
		guiaEmissaoDt.setContadorQuantidade("1");
		guiaEmissaoDt.setCustasQuantidade("1");
		guiaEmissaoDt.setTaxaProtocoloQuantidade("1");
		guiaEmissaoDt.setEscrivaniaQuantidade("1");
		guiaEmissaoDt.setNovoValorAcao("0,00");
		guiaEmissaoDt.setValorAcao("0,00");
		guiaEmissaoDt.setNumeroImpetrantes("0");
		guiaEmissaoDt.setPenhoraQuantidade("1");
		guiaEmissaoDt.setOrigemEstado("");
		guiaEmissaoDt.setProtocoloIntegrado("");
		
		guiaEmissaoDt.setId_Comarca("");
		guiaEmissaoDt.setComarca("");
		guiaEmissaoDt.setComarcaCodigo("");
		
		guiaEmissaoDt.setId_NaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPG("");
		guiaEmissaoDt.setNaturezaSPGCodigo("");
		
		guiaEmissaoDt.setId_AreaDistribuicao("");
		guiaEmissaoDt.setAreaDistribuicao("");
		
		guiaEmissaoDt.setId_ProcessoTipo("");
		guiaEmissaoDt.setProcessoTipo("");
		guiaEmissaoDt.setProcessoTipoCodigo("");
		
		listaBairroDt = new ArrayList();
		listaQuantidadeBairroDt = new ArrayList();
		
		return guiaEmissaoDt;
	}

}
