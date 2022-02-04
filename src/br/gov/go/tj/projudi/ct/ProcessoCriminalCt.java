package br.gov.go.tj.projudi.ct;

import java.io.IOException;
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
import br.gov.go.tj.projudi.dt.EscolaridadeDt;
import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
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
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet que controla o cadastramento de um processo criminal
 * @author msapaula
 *
 */
public class ProcessoCriminalCt extends ProcessoComumCt {

	private static final long serialVersionUID = 3105175391192322448L;

	public int Permissao() {
		return ProcessoCadastroDt.CodigoPermissaoProcessoCriminal;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoCadastroDt ProcessoCriminaldt;
		ProcessoParteDt ProcessoPartedt = null;
		ProcessoNe Processone;

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
		
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		
		if (request.getSession().getAttribute("parteDependenteJaSelecionada") != null){
			parteDependenteJaSelecionada = String.valueOf(request.getSession().getAttribute("parteDependenteJaSelecionada"));
		}
		
		request.setAttribute("tempPrograma", "Processo");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempRetorno", "ProcessoCriminal");

		Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null) Processone = new ProcessoNe();

		ProcessoCriminaldt = (ProcessoCadastroDt) request.getSession().getAttribute("ProcessoCadastroDt");
		if (ProcessoCriminaldt == null) ProcessoCriminaldt = new ProcessoCadastroDt();

		if (request.getSession().getAttribute("ProcessoPartedt") != null) ProcessoPartedt = (ProcessoParteDt) request.getSession().getAttribute("ProcessoPartedt");
		else ProcessoPartedt = new ProcessoParteDt();
				
		if (request.getParameter("nomeArquivo")!= null )
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
			request.setAttribute("nomeArquivo", "");
		
		if (request.getParameter("fluxo") != null)
			fluxo = Funcoes.StringToInt(request.getParameter("fluxo"));
		
		// Resgata se o processo for físico
		if (request.getParameter("fisico") != null) ProcessoCriminaldt.setProcessoFisico(Funcoes.StringToBoolean(request.getParameter("fisico")));

		ProcessoCriminaldt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		ProcessoCriminaldt.setProcessoNumeroPrincipal(request.getParameter("ProcessoNumeroDependente"));
		ProcessoCriminaldt.setId_Comarca(request.getParameter("Id_Comarca"));
		ProcessoCriminaldt.setComarca(request.getParameter("Comarca"));
		ProcessoCriminaldt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));		
		if (ProcessoCriminaldt.isProcessoFisico()) {
			ProcessoCriminaldt.setProcessoNumeroFisico(request.getParameter("ProcessoNumeroFisico"));
			ProcessoCriminaldt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
			ProcessoCriminaldt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		}
		else{
			ProcessoCriminaldt.setId_AreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			ProcessoCriminaldt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		}		
		ProcessoCriminaldt.setId_ServentiaSubTipo(request.getParameter("Id_ServentiaSubTipo"));
		ProcessoCriminaldt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		if(request.getParameter("Id_ProcessoTipo") != null && !request.getParameter("Id_ProcessoTipo").toString().equalsIgnoreCase("")){
			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(request.getParameter("Id_ProcessoTipo"));
			if(processoTipoDt.isHabeasCorpus()) {
			request.setAttribute("MensagemOk", "ATENÇÂO - Favor informar para a ação de habeas corpus: na parte ativa, o impetrante da ação; na parte passiva, a autoridade coatora; e, em outras partes, a parte do tipo PACIENTE que receberá o beneficio da ação.");
			}
		}
		ProcessoCriminaldt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		ProcessoCriminaldt.setId_ProcessoPrioridade(request.getParameter("Id_ProcessoPrioridade"));
		ProcessoCriminaldt.setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
		ProcessoCriminaldt.setValor(request.getParameter("Valor"));
		ProcessoCriminaldt.setTcoNumero(request.getParameter("TcoNumero"));
		ProcessoCriminaldt.setMarcarAudiencia(request.getParameter("NaoMarcarAudiencia"));
		ProcessoCriminaldt.setMandarConcluso(request.getParameter("NaoMandarConcluso"));
		ProcessoCriminaldt.setMarcarAudienciaConciliacao(request.getParameter("AudienciaConciliacao"));
		ProcessoCriminaldt.setSigiloso(request.getParameter("Sigiloso"));		
		ProcessoCriminaldt.setForumCodigo(request.getParameter("ForumCodigo"));

		if (request.getParameter("SegredoJustica") != null) ProcessoCriminaldt.setSegredoJustica(request.getParameter("SegredoJustica"));
		else ProcessoCriminaldt.setSegredoJustica("false");
		ProcessoCriminaldt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		ProcessoCriminaldt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		ProcessoCriminaldt.setId_Modelo(request.getParameter("Id_Modelo"));
		ProcessoCriminaldt.setModelo(request.getParameter("Modelo"));
		ProcessoCriminaldt.setId_Assunto(request.getParameter("Id_Assunto"));
		ProcessoCriminaldt.setAssunto(request.getParameter("Assunto"));
		ProcessoCriminaldt.setTextoEditor(request.getParameter("TextoEditor"));
		ProcessoCriminaldt.setPasso1(request.getParameter("Passo1"));
		ProcessoCriminaldt.setPasso2(request.getParameter("Passo2"));
		ProcessoCriminaldt.setPasso3(request.getParameter("Passo3"));
		ProcessoCriminaldt.setOabNumero(request.getParameter("OabNumero"));
		ProcessoCriminaldt.setOabComplemento(request.getParameter("OabComplemento"));
		ProcessoCriminaldt.setOabUf(request.getParameter("OabUf"));
		ProcessoCriminaldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoCriminaldt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		
		// Nas telas de "Recadastramento Cível e "Recadastramento Criminal" de processos físicos
		// será possível especificar a Data de Recebimento do processo. Portanto, se receber este
		// parâmetro, usará a data especificada pelo usuário. Se ele estiver em branco ou nulo irá
		// persistir a data atual. Portanto, para os casos que não sejam estas duas telas, continuará
		// o mesmo funcionamento.
		ProcessoCriminaldt.setDataRecebimento(request.getParameter("dataRecebimento"));
		
		//Variáveis da tela de processo comum (radios)
		ProcessoCriminaldt.setGrauProcesso(request.getParameter("grauProcesso"));
		ProcessoCriminaldt.setTipoProcesso(request.getParameter("tipoProcesso"));
		ProcessoCriminaldt.setAssistenciaProcesso(request.getParameter("assistenciaProcesso"));
		ProcessoCriminaldt.setDependenciaProcesso(request.getParameter("dependenciaProcesso"));
		
		request.setAttribute("RgOrgaoExpedidorEstado", request.getParameter("RgOrgaoExpedidorEstado"));
		
		//Resgata se processo é dependente
		if (request.getParameter("dependente") != null) ProcessoCriminaldt.setProcessoDependente(Funcoes.StringToBoolean(request.getParameter("dependente")));
		
		// Seta Tipo de Custa do Processo
		if (request.getParameter("custaTipo") != null) {
			if (request.getParameter("custaTipo").equals("1")) {
				ProcessoCriminaldt.setId_Custa_Tipo(String.valueOf(ProcessoCriminaldt.COM_CUSTAS));
			} else if (request.getParameter("custaTipo").equals("2")) {
				ProcessoCriminaldt.setId_Custa_Tipo(String.valueOf(ProcessoCriminaldt.ASSISTENCIA_JURIDICA));
			} else if(request.getParameter("custaTipo").equals("3")){
				ProcessoCriminaldt.setId_Custa_Tipo(String.valueOf(ProcessoCriminaldt.ISENTO));
			}
		}
				
		// obtendo a ação default
		stAcao = obtenhaAcaoDefault(ProcessoCriminaldt);

		// Variáveis auxiliares
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("ParteTipo") != null) parteTipo = Funcoes.StringToInt(request.getParameter("ParteTipo"));
		posicaoLista = request.getParameter("posicaoLista");

		List listaArquivosInseridos = getListaArquivos(request); // Captura arquivos inseridos

		setParametrosAuxiliares(ProcessoCriminaldt, ProcessoPartedt, passoEditar, paginaatual, request, paginaAnterior, Processone, UsuarioSessao);
			
		// Exibe a opção de não informar o endereço no cadastro de Partes
		request.setAttribute("exibeparteEnderecoDesconhecido", true);

		switch (paginaatual) {
		
			case Configuracao.Curinga9: 
				
				//Add Partes Recorrentes e Limpa as Partes Dependentes do Processo.
				if (request.getParameterValues("Recorrente") != null){
					addPartesRecorrentes (request, ProcessoCriminaldt, UsuarioSessao);
				}
				
				parteDependenteJaSelecionada = "true";
						
				break;

			// Novo Processo, limpar todos os dados
			case Configuracao.Novo:
				
				//limpa dados do processo criminal
				super.limpaProcesso(UsuarioSessao, ProcessoCriminaldt, Processone);
				
				ProcessoPartedt.limpar();
				// Seta área do processo
				ProcessoCriminaldt.setAreaCodigo(String.valueOf(AreaDt.CRIMINAL));

				int GrupoTipoCodigo = UsuarioSessao.getGrupoTipoCodigoToInt();
				int GrupoCodigo = UsuarioSessao.getGrupoCodigoToInt();
				if (GrupoTipoCodigo == GrupoTipoDt.ADVOGADO ||	GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo == GrupoDt.MP_TCE) {
					adicionaAdvogadoProcesso(ProcessoCriminaldt, UsuarioSessao);
				}
				
				if (UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo() != null && UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo().length() > 0
						&&Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.PROMOTORIA){
					request.setAttribute("MensagemOk", "ATENÇÃO - Para o caso de Substituto Processual,  o  Ministério Público deve ser incluído em Substituto  Processual  /  Outras Partes; No campo parte tipo escolha Substituto Processual.");
				}
				
				// Se for delegacia, o id da serventia deverá ser preenchido...
				if (UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo() != null && UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo().trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.DELEGACIA)))
					ProcessoCriminaldt.setId_ServentiaOrigem(UsuarioSessao.getUsuarioDt().getId_Serventia());
				
				//Limpa lista DWR e zera contador Arquivos
				request.getSession().removeAttribute("ListaArquivosDwr");
				request.getSession().removeAttribute("ListaArquivos");
				request.getSession().removeAttribute("Id_ListaArquivosDwr");

				passoEditar = -1;
				break;

			// Remover partes inseridas 
			case Configuracao.Excluir:
				if (posicaoLista.length() > 0) {
					int posicao = Funcoes.StringToInt(posicaoLista);
					if (passoEditar == 10) {
						//Refere-se a exclusão de advogados
						ProcessoCriminaldt.getListaAdvogados().remove(posicao);
						stAcao = "/WEB-INF/jsptjgo/BuscaAdvogado.jsp";
					} else excluirParteProcesso(ProcessoCriminaldt, posicao, parteTipo);
					passoEditar = -1;
				}
				break;

			case Configuracao.SalvarResultado:
				// Seta área do processo
				ProcessoCriminaldt.setAreaCodigo(String.valueOf(AreaDt.CRIMINAL));
				
				// Se for delegacia, o id da serventia de origem deverá ser preenchido
				if (UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo() != null && UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo().trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.DELEGACIA)))
					ProcessoCriminaldt.setId_ServentiaOrigem(UsuarioSessao.getUsuarioDt().getId_Serventia());
				
				//Resgatar partes intimadas delegacia
				getPartesIntimadasDelegacia(request, ProcessoCriminaldt);
				// Salvar dados do Processo Criminal			
				Processone.cadastrarProcessoCriminal(ProcessoCriminaldt, UsuarioSessao.getUsuarioDt());

				//Redireciona para jsp de Processo Cadastrado com sucesso
				stAcao = "/WEB-INF/jsptjgo/ProcessoCriminalCadastrado.jsp";

				//Joga processo no request e limpa da sessão
				request.setAttribute("ProcessoCadastroDt", ProcessoCriminaldt);
				ProcessoCriminaldt = new ProcessoCadastroDt();
				ProcessoPartedt = new ProcessoParteDt();

				//Limpa lista DWR e zera contador Arquivos
				request.getSession().removeAttribute("ListaArquivosDwr");
				request.getSession().removeAttribute("ListaArquivos");
				request.getSession().removeAttribute("Id_ListaArquivosDwr");
				break;
				
				
				case Configuracao.Localizar: 
					
					parteDependenteJaSelecionada = "false";
				
					String processoNumeroVinculo = request.getParameter("ProcessoNumero");
			
						if (processoNumeroVinculo != null && processoNumeroVinculo.length() > 0) {
							ProcessoCriminaldt.setProcessoNumeroPrincipal(processoNumeroVinculo);
							
							ProcessoDt processoVinculoDt = Processone.consultarProcessoDigitalEFisico(processoNumeroVinculo);							
							
							if (processoVinculoDt != null) {
								
								ProcessoCriminaldt.setProcessoDependente(true);
								
								if(StringUtils.isNotEmpty(processoVinculoDt.getId())) { 
									
									ProcessoCriminaldt.setProcessoDependenteDt(processoVinculoDt);
									ProcessoCriminaldt.setId_ProcessoPrincipal(processoVinculoDt.getId());
								
									if (ProcessoCriminaldt.isMesmoGrauJurisdicao() && ProcessoCriminaldt.isProcessoMesmaArea()){
										ProcessoCriminaldt.setAreaDistribuicao(processoVinculoDt.getAreaDistribuicao());
										ProcessoCriminaldt.setId_AreaDistribuicao(processoVinculoDt.getId_AreaDistribuicao());
										ProcessoCriminaldt.setForumCodigo(processoVinculoDt.getForumCodigo());
										//Consulta assuntos do processo originário
										getAssuntosProcessoOriginario(ProcessoCriminaldt, processoVinculoDt.getId(), Processone);
									}
								} else if (StringUtils.isNotEmpty(processoVinculoDt.getProcessoFisicoTipo())) {
									ProcessoCriminaldt.setProcessoFisicoTipo(processoVinculoDt.getProcessoFisicoTipo());
									ProcessoCriminaldt.setProcessoFisicoNumero(processoVinculoDt.getProcessoFisicoNumero());
									ProcessoCriminaldt.setProcessoFisicoComarcaNome(processoVinculoDt.getProcessoFisicoComarcaNome());
									ProcessoCriminaldt.setProcessoFisicoComarcaCodigo(processoVinculoDt.getProcessoFisicoComarcaCodigo());
								} else {
									super.limpaProcesso(UsuarioSessao, ProcessoCriminaldt, Processone);
									request.setAttribute("MensagemErro", "Não foi possível localizar o Processo informado.");
								}
		
							} else {
								super.limpaProcesso(UsuarioSessao, ProcessoCriminaldt, Processone);
								request.setAttribute("MensagemErro", "Não foi possível localizar o Processo informado.");
							}
						} else {
							request.setAttribute("MensagemErro", "Número do Processo deve ser informado. ");
						}
					break;

			//Consulta as Comarcas disponíveis 
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","ProcessoCriminal");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					ProcessoCriminaldt.setId_AreaDistribuicao("null");
					ProcessoCriminaldt.setAreaDistribuicao("null");
					ProcessoCriminaldt.setId_ProcessoTipo("null");
					ProcessoCriminaldt.setProcessoTipo("");
					passoEditar = -1;
					break;
				} else {
					String stTemp="";
					stTemp = Processone.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
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
					request.setAttribute("tempRetorno","ProcessoCriminal");
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
				if (ProcessoCriminaldt.getId_Comarca().length() > 0) {
					if (request.getParameter("Passo")==null && request.getParameter("tempFluxo1") == null){
						String[] lisNomeBusca = {"AreaDistribuicao"};
						String[] lisDescricao = {"AreaDistribuicao","ForumCodigo","Id_ServentiaSubTipo"};
						//quando for necessário retornar outros valos além do id, coloque outras colunas de descrição
						// na localizar.jsp as descrições geram novos input hidem para retornar ao ct
						// na funcoes.js as descricoes serão usadas para gerar os AlterarValue para retornar para o ct
						String[] camposHidden = {"ForumCodigo","Id_ServentiaSubTipo"};
						request.setAttribute("camposHidden",camposHidden);
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_AreaDistribuicao");
						request.setAttribute("tempBuscaDescricao","AreaDistribuicao");
						request.setAttribute("tempBuscaPrograma","AreaDistribuicao");			
						request.setAttribute("tempRetorno","ProcessoCriminal");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON",Configuracao.Editar);
						request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						request.setAttribute("tempFluxo1", "PreencherForum");
						passoEditar = -1;					
					} else {
						String stTemp="";
						stTemp = Processone.consultarAreasDistribuicaoPrimeiroGrauCriminalJSON(stNomeBusca1, ProcessoCriminaldt.getId_Comarca(),  PosicaoPaginaAtual);
						enviarJSON(response, stTemp);
						return;								
					}
				} else {
					request.setAttribute("MensagemErro", "Selecione primeiramente a Comarca.");
				}
				ProcessoCriminaldt.setId_ProcessoTipo("null");
				ProcessoCriminaldt.setProcessoTipo("");
				break;

			//Consulta tipos de processo disponíveis na área de distribuição escolhida
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (ProcessoCriminaldt.getAreaDistribuicao().length() > 0 || ProcessoCriminaldt.isProcessoDependente()) {	
					if (request.getParameter("Passo")==null){
						
						String[] lisNomeBusca = {"Classe"};
						String[] lisDescricao = {"Classe","Código","Código CNJ"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_ProcessoTipo");
						request.setAttribute("tempBuscaDescricao","ProcessoTipo");
						request.setAttribute("tempBuscaPrograma","ProcessoTipo");			
						request.setAttribute("tempRetorno","ProcessoCriminal");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						passoEditar = -1;
					} else {
						String stTemp="";
						
						if(ProcessoCriminaldt.isProcessoFisico()) {
							if (ProcessoCriminaldt.getId_Serventia() != null & ProcessoCriminaldt.getId_Serventia().length() > 0) {								
								stTemp = Processone.consultarProcessoTipoServentiaJSON(stNomeBusca1, ProcessoCriminaldt.getId_Serventia(),  UsuarioSessao.getUsuarioDt(),PosicaoPaginaAtual);
							} else {
								request.setAttribute("MensagemErro", "Não foi possível obter a Area de Distribuição da Serventia escolhida.");
								return;	
							}
						} else {
							if (ProcessoCriminaldt.getId_AreaDistribuicao() != null & ProcessoCriminaldt.getId_AreaDistribuicao().length() > 0) {								
								stTemp = Processone.consultarDescricaoProcessoTipoJSON(stNomeBusca1, ProcessoCriminaldt.getId_AreaDistribuicao(), UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
							} else {
								request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
								return;
							}
						}
													
						enviarJSON(response, stTemp);
													
						return;								
					}				
				} else {
					request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
				}
				break;

			//Consulta tipos de prioridade disponíveis
			case (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoPrioridade"};
					String[] lisDescricao = {"ProcessoPrioridade", "Codigo", "Ordem"};
					String[] camposHidden = {"ProcessoPrioridadeCodigo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoPrioridade");
					request.setAttribute("tempBuscaDescricao","ProcessoPrioridade");
					request.setAttribute("tempBuscaPrograma","ProcessoPrioridade");			
					request.setAttribute("tempRetorno","ProcessoCriminal");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = -1;
					break;
				} else {
					String stTemp="";
					stTemp = Processone.consultarDescricaoProcessoPrioridadeJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}

			//Consulta de Cidades - Naturalidade da Parte
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "ProcessoCriminal");
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

			//Consulta de Estado Civil - Usado no cadastro de partes
			case (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"EstadoCivil"};
					String[] lisDescricao = {"EstadoCivil"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_EstadoCivil");
					request.setAttribute("tempBuscaDescricao", "EstadoCivil");
					request.setAttribute("tempBuscaPrograma", "EstadoCivil");
					request.setAttribute("tempRetorno", "ProcessoCriminal");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = Processone.consultarDescricaoEstadoCivilJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			//Consulta de Profissão - Usado no cadastro de partes
			case (ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Profissao"};
					String[] lisDescricao = {"Profissao"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Profissao");
					request.setAttribute("tempBuscaDescricao", "Profissao");
					request.setAttribute("tempBuscaPrograma", "Profissao");
					request.setAttribute("tempRetorno", "ProcessoCriminal");
					request.setAttribute("tempDescricaoId", "Id");
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

			//Consulta de Tipos de Parte
			case (ProcessoParteTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoParteTipo"};
					String[] lisDescricao = {"ProcessoParteTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoParteTipo");
					request.setAttribute("tempBuscaDescricao", "ProcessoParteTipo");
					request.setAttribute("tempBuscaPrograma", "ProcessoParteTipo");
					request.setAttribute("tempRetorno", "ProcessoCriminal");
					request.setAttribute("tempDescricaoId", "Id");
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

			//Consulta de Orgao Expedidor - Usado no cadastro de partes
			case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"OrgaoExpedidor","Sigla"};
					String[] lisDescricao = {"Sigla","Nome","Estado"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] camposHidden = {"RgOrgaoExpedidorNome", "RgOrgaoExpedidorEstado"};
					request.setAttribute("camposHidden",camposHidden);
					request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
					request.setAttribute("tempRetorno", "ProcessoCriminal");
					request.setAttribute("tempDescricaoId", "Id");
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

			//Consulta de Estado da CTPS - Usado no cadastro de partes 
			case (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Uf"};
					String[] lisDescricao = {"Uf", "Descrição"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_CtpsUf");
					request.setAttribute("tempBuscaDescricao", "EstadoCtpsUf");
					request.setAttribute("tempBuscaPrograma", "EstadoCtpsUf");
					request.setAttribute("tempRetorno", "ProcessoCriminal");
					request.setAttribute("tempDescricaoId", "Id");
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

			//Consulta de Bairro - Usado no endereço da parte
			case (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","Uf"};
					String[] lisDescricao = {"Bairro","Cidade","Uf"};
					String[] camposHidden = {"BairroCidade","BairroUf"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "ProcessoCriminal");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
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
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","ProcessoCriminal");		
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
					stTemp = Processone.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
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
					request.setAttribute("tempRetorno", "ProcessoCriminal");
					request.setAttribute("tempDescricaoId", "Id");
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
					stTemp = Processone.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), ProcessoCriminaldt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
			break;

			// Consulta Assuntos
			case (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (ProcessoCriminaldt.getProcessoTipo().length() > 0) {
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Assunto", "Código CNJ"};
						String[] lisDescricao = {"Assunto","Pai","Disp. Legal", "Código CNJ"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Assunto");
						request.setAttribute("tempBuscaDescricao","Assunto");
						request.setAttribute("tempBuscaPrograma","Assunto");			
						request.setAttribute("tempRetorno","ProcessoCriminal");		
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
						if (ProcessoCriminaldt.getId_AreaDistribuicao().length() > 0) {
							//se for processo físico deve consultar pelo ID da serventia, pois a área de distribuição que está na sessão é a área recursal da serventia
							if(ProcessoCriminaldt.getProcessoNumeroFisico() != null && !ProcessoCriminaldt.getProcessoNumeroFisico().equalsIgnoreCase("")) {									
								stTemp = Processone.consultarAssuntosServentiaJSON(stNomeBusca1, UsuarioSessao.getUsuarioDt().getId_Serventia(), PosicaoPaginaAtual);
							} else {									
								stTemp = Processone.consultarAssuntosAreaDistribuicaoJSON(stNomeBusca1, stNomeBusca2, ProcessoCriminaldt.getId_AreaDistribuicao(), PosicaoPaginaAtual);
							}
						}
						else {
							request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
						}
						enviarJSON(response, stTemp);
						return;								
					}
				} else {
					request.setAttribute("MensagemErro", "Selecione primeiramente a Classe.");
				}	
				break;

			// Consultar Advogados
			case (UsuarioServentiaOabDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if(fluxo == 1){
					//consultarAdvogado(request, Processone, ProcessoCriminaldt);
					if (ProcessoCriminaldt.getOabNumero().length() > 0 && ProcessoCriminaldt.getOabComplemento().length() > 0 && ProcessoCriminaldt.getOabUf().length() > 0) {
						tempList = UsuarioSessao.consultarAdvogadoOab(ProcessoCriminaldt.getOabNumero(), ProcessoCriminaldt.getOabComplemento(), ProcessoCriminaldt.getOabUf());
						
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
								
								if (ProcessoCriminaldt.getListaAdvogados() != null){
									for (Iterator iterator2 = ProcessoCriminaldt.getListaAdvogados().iterator(); iterator2.hasNext();) {
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
									ProcessoCriminaldt.addListaAdvogados(usuarioServentiaOabDt);
									
									ProcessoCriminaldt.setOabNumero("");
									ProcessoCriminaldt.setOabComplemento("");
									ProcessoCriminaldt.setOabUf("");
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
				
			// Consultar Juiz Responsável - Serventia Cargo
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
					String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "ProcessoCriminal");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Processone.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
				
			case (EscolaridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Escolaridade"};
					String[] lisDescricao = {"Escolaridade"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Escolaridade");
					request.setAttribute("tempBuscaDescricao", "Escolaridade");
					request.setAttribute("tempBuscaPrograma", "Escolaridade");
					request.setAttribute("tempRetorno", "ProcessoCriminal");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(EscolaridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);	
					passoEditar = 4;
				}else{
								
					String stTemp = "";
					stTemp =Processone.consultarDescricaoEscolaridadeJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
				
				

			default:
				// Função Editar é dividida da seguinte forma: 
				//	Passo 0 : Redireciona para passo 2
				// 	Passo 1 : Redireciona para passo 3
				//	Passo 2 : Efetua consulta da parte e exibe dados para edição
				//	Passo 3 : Valida dados da parte 
				//	Passo 4 : Redireciona para jsp de cadastro de parte 
				// 	Passo 5 : Valida dados do processo 
				//	Passo 6 : Resgata arquivos inseridos no passo 2 e redireciona para passo 3
				//	Passo 7 : Redireciona para jsp de busca de partes
				//	Passo 8 : Atualiza assuntos do processo (inserção e exclusão)
				// 	Passo 9	: Redireciona para jsp de Partes Não Personificáveis

				switch (passoEditar) {

					case 0:
						request.setAttribute("tempRetorno", "ProcessoCriminal");
						ProcessoCriminaldt.setPasso3("");
						stAcao = "/WEB-INF/jsptjgo/ProcessoPeticionar.jsp";
						break;

					case 1:
						stAcao = "/WEB-INF/jsptjgo/ProcessoCriminalVisualizar.jsp";
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

					case 3: //Valida dados da parte 
						if (request.getParameter("parteEnderecoDesconhecido") != null) ProcessoPartedt.setParteEnderecoDesconhecido(request.getParameter("parteEnderecoDesconhecido"));
						Mensagem = Processone.VerificarParteProcesso(ProcessoPartedt);
						if (Mensagem.length() == 0) {
							if (!parteProcessoCadastrada(ProcessoCriminaldt, ProcessoPartedt)) {
								// Adiciona lista promoventes
								switch (parteTipo) {
									case ProcessoParteTipoDt.POLO_ATIVO_CODIGO:
										ProcessoCriminaldt.addListaPoloAtivo(ProcessoPartedt);
										break;
									case ProcessoParteTipoDt.POLO_PASSIVO_CODIGO:
										ProcessoCriminaldt.addListaPolosPassivos(ProcessoPartedt);
										break;
									default:
										ProcessoCriminaldt.addListaOutrasPartes(ProcessoPartedt);
										break;
								}
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

					case 4:
						stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
						break;

					case 5: //Valida dados do processo
						Mensagem = Processone.verificarProcessoCriminal(ProcessoCriminaldt, UsuarioSessao.getUsuarioDt());

						if (Mensagem.length() > 0) request.setAttribute("MensagemErro", Mensagem);
						else {//Se não há erros, passa para passo de inserção de arquivos
							ProcessoCriminaldt.setPasso1("Passo 1 OK");
							ProcessoCriminaldt.setPasso2("Passo 2");
							stAcao = "/WEB-INF/jsptjgo/ProcessoPeticionar.jsp";
						}
						break;

					case 6: // Resgata lista de arquivos inseridos e adiciona ao processo. Redireciona para confirmação cadastro processo
						if (listaArquivosInseridos != null && listaArquivosInseridos.size() > 0) {
							ProcessoCriminaldt.setListaArquivos(listaArquivosInseridos);
							ProcessoCriminaldt.setPasso1("Passo 1 OK");
							ProcessoCriminaldt.setPasso2("Passo 2 OK");
							ProcessoCriminaldt.setPasso3("Passo 3");
							
							//se o usuário que está cadatrando o processo for autoridade policial, permitir marcar audiencia de conciliação
							if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.AUTORIDADES_POLICIAIS))){
								request.setAttribute("marcarAudienciaConciliacao", true);
							}
							
							//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
							request.setAttribute("__Pedido__", UsuarioSessao.getPedido());

							stAcao = "/WEB-INF/jsptjgo/ProcessoCriminalVisualizar.jsp";
							ProcessoCriminaldt.setId_ArquivoTipo("");
							ProcessoCriminaldt.setArquivoTipo("");
							ProcessoCriminaldt.setId_Modelo("");
							ProcessoCriminaldt.setModelo("");
							ProcessoCriminaldt.setTextoEditor("");
						} else {
							request.setAttribute("MensagemErro", "É necessário inserir um arquivo para prosseguir.");
							stAcao = "/WEB-INF/jsptjgo/ProcessoPeticionar.jsp";
						}
						break;

					//Redireciona para tela de Pesquisa de Parte
					case 7:
						stAcao = "/WEB-INF/jsptjgo/BuscaProcessoParte.jsp";
						break;

					case 8:
						removerAssuntoProcesso(ProcessoCriminaldt, posicaoLista);
						break;

					case 9:
						//Chama método para inicializar objeto parte
						buscaParte(request, ProcessoPartedt, parteTipo, passoEditar, Processone);
						stAcao = "/WEB-INF/jsptjgo/PartesNaoPersonificaveis.jsp";
						request.setAttribute("tempRetorno", "ProcessoCriminal?PassoEditar=3&ParteTipo=" + parteTipo);
						break;

					//Redireciona para tela de Inclusão de Advogados
					case 10:
						stAcao = "/WEB-INF/jsptjgo/BuscaAdvogado.jsp";
						break;
						
					//Redireciona para tela de Restaurar Dados
					case 11:
						redireciona(response, "ProcessoCadastroRestaurar?tempRetorno=ProcessoCriminal");
						break;
						
					//Salva os dados da primeira tela de cadastro
					case 12:
						String byTemp = Processone.salvarProcesso(ProcessoCriminaldt);						
						enviarProjudi(response, byTemp.getBytes(),"CadastroProcesso");
					    
					    byTemp = null;
					    return;

					default:
						ProcessoCriminaldt.setPasso2("");
						ProcessoCriminaldt.setPasso3("");
						break;
				}
		}

		request.setAttribute("fluxo",fluxo);
		request.setAttribute("ParteTipo", parteTipo);
		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("parteEnderecoDesconhecido", String.valueOf(ProcessoPartedt.isParteEnderecoDesconhecido()));
		request.getSession().setAttribute("parteDependenteJaSelecionada", parteDependenteJaSelecionada);
		request.getSession().setAttribute("ArquivosInseridos", ProcessoCriminaldt.getListaArquivos());
		request.getSession().setAttribute("ProcessoCadastroDt", ProcessoCriminaldt);
		request.getSession().setAttribute("Processone", Processone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}	

	/**
	 * Método que faz tratamentos necessários com parâmetros auxiliares no cadastro de processo
	 */
	public String obtenhaAcaoDefault(ProcessoCadastroDt processoCriminaldt){
		
		if (processoCriminaldt.isProcessoFisico()) return "/WEB-INF/jsptjgo/ProcessoCriminalFisico.jsp";
		
		if (processoCriminaldt.isProcessoDependente()) return "/WEB-INF/jsptjgo/ProcessoComum.jsp";		
		
		return "/WEB-INF/jsptjgo/ProcessoComum.jsp";
	}
	
}
