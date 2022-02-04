package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ne.AreaDistribuicaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * Servlet que controla o cadastramento de um processo cível
 * @author msapaula
 */
public class ProcessoCivelSemAssistenciaCt extends ProcessoComumCt {

	private static final long serialVersionUID = 6757477987269926374L;

	public int Permissao() {
		return ProcessoCadastroDt.CodigoPermissaoProcessoCivel;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoCadastroDt ProcessoCiveldt;
		ProcessoParteDt ProcessoPartedt = null;
		ProcessoNe Processone;

		String Mensagem = "";
		int passoEditar = -1;
		int parteTipo = 0;
		int paginaAnterior = 0;
		int fluxo = 0;
		String posicaoLista = "0";
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
		request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");

		Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null) Processone = new ProcessoNe();

 		ProcessoCiveldt = (ProcessoCadastroDt) request.getSession().getAttribute("ProcessoCadastroDt");
  		if (ProcessoCiveldt == null) ProcessoCiveldt = new ProcessoCadastroDt();

 		if (request.getSession().getAttribute("ProcessoPartedt") != null) ProcessoPartedt = (ProcessoParteDt) request.getSession().getAttribute("ProcessoPartedt");
 		else ProcessoPartedt = new ProcessoParteDt();
 		
		if (request.getParameter("nomeArquivo")!= null )
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
  			request.setAttribute("nomeArquivo", "");
		
		if (request.getParameter("fluxo") != null)
			fluxo = Funcoes.StringToInt(request.getParameter("fluxo"));
		
		// Resgata se o processo for físico
 		if (request.getParameter("fisico") != null) ProcessoCiveldt.setProcessoFisico(Funcoes.StringToBoolean(request.getParameter("fisico")));

		ProcessoCiveldt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		ProcessoCiveldt.setId_Comarca(request.getParameter("Id_Comarca"));
		ProcessoCiveldt.setComarca(request.getParameter("Comarca"));
		ProcessoCiveldt.setComarcaCodigo(request.getParameter("ComarcaCodigo"));
		if (ProcessoCiveldt.isProcessoFisico()) {
			ProcessoCiveldt.setProcessoNumeroFisico(request.getParameter("ProcessoNumeroFisico"));
			ProcessoCiveldt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
			ProcessoCiveldt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		}
		else{
			ProcessoCiveldt.setId_AreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			ProcessoCiveldt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		}			
		ProcessoCiveldt.setId_ServentiaSubTipo(request.getParameter("Id_ServentiaSubTipo"));
		ProcessoCiveldt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		ProcessoCiveldt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		ProcessoCiveldt.setId_ProcessoPrioridade(request.getParameter("Id_ProcessoPrioridade"));
		ProcessoCiveldt.setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
		ProcessoCiveldt.setProcessoPrioridadeCodigo(request.getParameter("ProcessoPrioridadeCodigo"));
		ProcessoCiveldt.setValor(request.getParameter("Valor"));
		ProcessoCiveldt.setMarcarAudiencia(request.getParameter("NaoMarcarAudiencia"));
		ProcessoCiveldt.setMandarConcluso(request.getParameter("NaoMandarConcluso"));
		if (request.getParameter("SegredoJustica") != null) ProcessoCiveldt.setSegredoJustica(request.getParameter("SegredoJustica"));
		else ProcessoCiveldt.setSegredoJustica("false");
		ProcessoCiveldt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		ProcessoCiveldt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		ProcessoCiveldt.setId_Modelo(request.getParameter("Id_Modelo"));
		ProcessoCiveldt.setModelo(request.getParameter("Modelo"));
		ProcessoCiveldt.setId_Assunto(request.getParameter("Id_Assunto"));
		ProcessoCiveldt.setForumCodigo(request.getParameter("ForumCodigo"));
		ProcessoCiveldt.setAssunto(request.getParameter("Assunto"));
		ProcessoCiveldt.setTextoEditor(request.getParameter("TextoEditor"));
		ProcessoCiveldt.setPasso1(request.getParameter("Passo1"));
		ProcessoCiveldt.setPasso2(request.getParameter("Passo2"));
		ProcessoCiveldt.setPasso3(request.getParameter("Passo3"));
		ProcessoCiveldt.setOabNumero(request.getParameter("OabNumero"));
		ProcessoCiveldt.setOabComplemento(request.getParameter("OabComplemento"));
		ProcessoCiveldt.setOabUf(request.getParameter("OabUf"));
		ProcessoCiveldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoCiveldt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		if (request.getParameter("ProcessoNumeroDependente") != null)ProcessoCiveldt.setProcessoNumeroPrincipal(request.getParameter("ProcessoNumeroDependente"));
		
		//Variáveis da tela de processo comum (radios)
		if (ProcessoCiveldt.getGrauProcesso()!=null && request.getParameter("grauProcesso") != null
				&& !ProcessoCiveldt.getGrauProcesso().equalsIgnoreCase(request.getParameter("grauProcesso"))){
			limpaProcesso(UsuarioSessao, ProcessoCiveldt, Processone);
			
			int GrupoTipoCodigo = UsuarioSessao.getGrupoTipoCodigoToInt();
			int GrupoCodigo = UsuarioSessao.getGrupoCodigoToInt();
			if (GrupoTipoCodigo == GrupoTipoDt.ADVOGADO ||	GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo == GrupoDt.MP_TCE) {
				adicionaAdvogadoProcesso(ProcessoCiveldt, UsuarioSessao);
			}
		}
		
		if (request.getParameter("assistenciaProcesso") != null && (request.getParameter("assistenciaProcesso").equalsIgnoreCase("1") || request.getParameter("assistenciaProcesso").equalsIgnoreCase("3")) ){
			if (!ProcessoCiveldt.getAssistenciaProcesso().equalsIgnoreCase(request.getParameter("assistenciaProcesso"))){
				request.getSession().removeAttribute("GuiaEmissaoDt");
				ProcessoCiveldt.limparProcessoTrocarCustaTipo();
				
				int GrupoTipoCodigo = UsuarioSessao.getGrupoTipoCodigoToInt();
				int GrupoCodigo = UsuarioSessao.getGrupoCodigoToInt();
				if (GrupoTipoCodigo == GrupoTipoDt.ADVOGADO ||	GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo == GrupoDt.MP_TCE) {
					adicionaAdvogadoProcesso(ProcessoCiveldt, UsuarioSessao);
				}
			}
		}
		
		//Se processo de Segundo Grau redireciona
		if (request.getParameter("grauProcesso") != null && request.getParameter("grauProcesso").equals("2")) {
			redireciona(response, "ProcessoSegundoGrauCivel?PaginaAtual=" + Configuracao.Editar );
		}
		
		ProcessoCiveldt.setGrauProcesso(request.getParameter("grauProcesso"));
		ProcessoCiveldt.setTipoProcesso(request.getParameter("tipoProcesso"));
		ProcessoCiveldt.setAssistenciaProcesso(request.getParameter("assistenciaProcesso"));
		ProcessoCiveldt.setDependenciaProcesso(request.getParameter("dependenciaProcesso"));
		
		// Resgata se processo for dependente
		if (request.getParameter("dependente") != null) ProcessoCiveldt.setProcessoDependente(Funcoes.StringToBoolean(request.getParameter("dependente")));
		
		// obtendo a ação default
		stAcao = obtenhaAcaoDefault(ProcessoCiveldt);

		// Variáveis auxiliares
		if (request.getParameter("PassoEditar") != null) 
			passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("PaginaAnterior") != null) 
			paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("ParteTipo") != null) 
			parteTipo = Funcoes.StringToInt(request.getParameter("ParteTipo"));
			
		posicaoLista = (request.getParameter("posicaoLista") != null) ? request.getParameter("posicaoLista").trim() : ""; 
				
		if (request.getParameter("datafile") != null);
		
		List listaArquivosInseridos = getListaArquivos(request); // Captura arquivos inseridos
		
		//controle de tela 
		if (request.getParameter("assistenciaProcesso") != null && request.getParameter("assistenciaProcesso").equalsIgnoreCase("3")){
			ProcessoCiveldt.setId_Custa_Tipo(String.valueOf(ProcessoCiveldt.ISENTO));
		} 
		
 		setParametrosAuxiliares(ProcessoCiveldt, ProcessoPartedt, passoEditar, paginaatual, request, paginaAnterior, Processone, UsuarioSessao);
		
		//Utilizado no cadastro de Partes
		request.setAttribute("exibeGovernoEmpresa", true);
		request.setAttribute("exibeparteEnderecoDesconhecido", true);
			
		request.setAttribute("exibeComercaAreaClasse", false);

 		switch (paginaatual) {
 		
	 		case Configuracao.Curinga9: 
					
					//Add Partes Recorrentes e Limpa as Partes Dependentes do Processo.
				if (request.getParameterValues("Recorrente") != null){
					addPartesRecorrentes (request, ProcessoCiveldt, UsuarioSessao);
				}
				
				parteDependenteJaSelecionada = "true";
						
				break;

			// Novo Processo, limpar todos os dados
			case Configuracao.Novo:
				//limpa dados do processo civel
				super.limpaProcesso(UsuarioSessao, ProcessoCiveldt, Processone);
				
				ProcessoPartedt.limpar();				
				// Seta área do processo
				ProcessoCiveldt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));

				if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ADVOGADO) adicionaAdvogadoProcesso(ProcessoCiveldt, UsuarioSessao);

				
				if (UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo() != null && UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo().length() > 0
						&&Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.PROMOTORIA){
					request.setAttribute("MensagemOk", "ATENÇÂO - Para o caso de Substituto Processual,  o  Ministério Público deve ser incluído em Substituto  Processual  /  Outras Partes; No campo parte tipo escolha Substituto Processual.");
				}
				
				//Limpa lista DWR e zera contador Arquivos
				request.getSession().removeAttribute("ListaArquivosDwr");
				request.getSession().removeAttribute("ListaArquivos");
				request.getSession().removeAttribute("Id_ListaArquivosDwr");
				
				// Resgata se processo for dependente
				if (request.getParameter("dependente") != null) ProcessoCiveldt.setProcessoDependente(Funcoes.StringToBoolean(request.getParameter("dependente")));
				
				stAcao = "/WEB-INF/jsptjgo/CadastroProcessoSemAssistencia.jsp";
				passoEditar = -1;
				break;

			// Remover partes ou advogados inseridos
			case Configuracao.Excluir:
				Mensagem = "";
				if (posicaoLista.length() > 0) {
					int posicao = Funcoes.StringToInt(posicaoLista);
					if (passoEditar == 10) {
						//Refere-se a exclusão de advogados
						ProcessoCiveldt.getListaAdvogados().remove(posicao);
						stAcao = "/WEB-INF/jsptjgo/BuscaAdvogado.jsp";
					} else 
						Mensagem = super.excluirParteProcesso(ProcessoCiveldt, posicao, parteTipo);
					
					if (Mensagem.length() > 0) 
						request.setAttribute("MensagemErro", Mensagem);
					
					passoEditar = -1;
				}
				break;

			// Salvar dados do Processo Cível
			case Configuracao.SalvarResultado: {
				// Seta área do processo
				ProcessoCiveldt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));
				
				GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt");
				//processo Com Custas
				ProcessoCiveldt.setId_Custa_Tipo(String.valueOf(ProcessoDt.COM_CUSTAS));				
				
				Processone.cadastrarProcessoCivel(ProcessoCiveldt, UsuarioSessao.getUsuarioDt(), guiaEmissaoDt);
				request.getSession().removeAttribute("GuiaEmissaoDt");
				
				// Redireciona para jsp de Processo Cadastrado com sucesso
				stAcao = "/WEB-INF/jsptjgo/ProcessoCivelCadastrado.jsp";
				
				//Joga processo no request e limpa da sessão
				request.setAttribute("ProcessoCadastroDt", ProcessoCiveldt);
				ProcessoCiveldt = new ProcessoCadastroDt();
				ProcessoPartedt = new ProcessoParteDt();

				//Limpa lista DWR e zera contador Arquivos
				request.getSession().removeAttribute("ListaArquivosDwr");
				request.getSession().removeAttribute("ListaArquivos");
				request.getSession().removeAttribute("Id_ListaArquivosDwr");
				break;
			}

			
			case Configuracao.Localizar : {
				
				parteDependenteJaSelecionada = "false";
				
				String numeroCompletoGuiaInicial = request.getParameter("numeroCompletoGuiaInicial").toString();
				stAcao = "/WEB-INF/jsptjgo/ProcessoComum.jsp";
				passoEditar = -1;
				
				if( numeroCompletoGuiaInicial != null && numeroCompletoGuiaInicial.length() > 0 ) {
					Map partes = Processone.consultarGuiaInicialPrimeiroGrau(numeroCompletoGuiaInicial);
					
					if( partes != null ) {
						if( partes.containsKey("MENSAGEMERROR") ) {
							
							request.setAttribute("MensagemErro", partes.get("MENSAGEMERROR").toString());
						
						} else {
							
							GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) partes.get("GUIAEMISSAODT");
							
							if( guiaEmissaoDt.getListaRequerentes() != null ) {
								ProcessoCiveldt.setListaPolosAtivos(guiaEmissaoDt.getListaRequerentes());
							}
							if( guiaEmissaoDt.getListaRequeridos() != null ) {
								ProcessoCiveldt.setListaPolosPassivos(guiaEmissaoDt.getListaRequeridos());
							}
							if( guiaEmissaoDt.getListaOutrasPartes() != null ) {
								ProcessoCiveldt.setListaOutrasPartes(guiaEmissaoDt.getListaOutrasPartes());
							}
							if( guiaEmissaoDt.getListaAdvogados() != null ) {
								ProcessoCiveldt.setListaAdvogados(guiaEmissaoDt.getListaAdvogados());
							}
							
							if( guiaEmissaoDt.getNumeroProcessoDependente() != null && guiaEmissaoDt.getNumeroProcessoDependente().length() > 0 ) {
								ProcessoCiveldt.setProcessoDependente(true);
								ProcessoCiveldt.setProcessoNumeroPrincipal(guiaEmissaoDt.getNumeroProcessoDependente());
								ProcessoCiveldt.setProcessoDependenteDt(Processone.consultarProcessoNumeroCompleto(guiaEmissaoDt.getNumeroProcessoDependente(),null));
							} 
							
							if (!(guiaEmissaoDt.isGuiaEmitidaSPG())) {
								AreaDistribuicaoNe areaDistribuicaoNe = new AreaDistribuicaoNe();
								
								AreaDistribuicaoDt areaDistribuicaoDt = (AreaDistribuicaoDt) areaDistribuicaoNe.consultarId(guiaEmissaoDt.getId_AreaDistribuicao());
								ProcessoCiveldt.setAreaDistribuicao(areaDistribuicaoDt.getAreaDistribuicao());
								ProcessoCiveldt.setId_ServentiaSubTipo(areaDistribuicaoDt.getId_ServentiaSubtipo());
								ProcessoCiveldt.setForumCodigo(areaDistribuicaoDt.getForumCodigo());
								ProcessoCiveldt.setId_AreaDistribuicao(guiaEmissaoDt.getId_AreaDistribuicao());
								ProcessoCiveldt.setComarca(guiaEmissaoDt.getComarca());
								ProcessoCiveldt.setId_Comarca(guiaEmissaoDt.getId_Comarca());
								ProcessoCiveldt.setId_ProcessoTipo(guiaEmissaoDt.getId_ProcessoTipo());
								ProcessoCiveldt.setProcessoTipo(guiaEmissaoDt.getProcessoTipo());	
									
							}
							
							if (guiaEmissaoDt.getNaturezaSPG() != null && guiaEmissaoDt.getNaturezaSPG().length() > 0)
								ProcessoCiveldt.setNaturezaSPG(guiaEmissaoDt.getNaturezaSPG());
							
							//ProcessoCiveldt.setListaAssuntos(guiaEmissaoDt.getListaAssuntos());
							//ProcessoCiveldt.setId_ProcessoPrioridade(guiaEmissaoDt.getId_ProcessoPrioridade());
							ProcessoCiveldt.setValor(Funcoes.FormatarDecimal(guiaEmissaoDt.getValorAcao()));
							
							request.getSession().setAttribute("GuiaEmissaoDt", guiaEmissaoDt);
							request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
							request.setAttribute("numeroGuiaInicial", Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto()));
							
							if (Mensagem.length() > 0) 
								request.setAttribute("MensagemErro", Mensagem);
							else {//Se não há erros, passa para passo de inserção de arquivos
								ProcessoCiveldt.setPasso2("");
								ProcessoCiveldt.setPasso3("");
								passoEditar = -1;
								request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
								stAcao = obtenhaAcaoDefault(ProcessoCiveldt,guiaEmissaoDt);
								if( partes.containsKey("MENSAGEM") ) {
									request.setAttribute("MensagemOk", partes.get("MENSAGEM").toString());
								}
							}
						}
					}
					else {
						request.setAttribute("MensagemOk", "Guia Inicial não Encontrada!");
					}
				}
				else {
					request.setAttribute("MensagemErro", "Informe o número da Guia Inicial!");
				}
				
				break;
			}
			
			//Consulta as Comarcas disponíveis
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","ProcessoCivelSemAssistencia");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					ProcessoCiveldt.setId_AreaDistribuicao("null");
					ProcessoCiveldt.setAreaDistribuicao("null");
					ProcessoCiveldt.setId_ProcessoTipo("null");
					ProcessoCiveldt.setProcessoTipo("");
					passoEditar = -1;
					break;
				} else {
					String stTemp="";
					stTemp = Processone.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}

			//Consulta as áreas de distribuição disponíveis para a comarca escolhida
			case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
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
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
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
					
					stTemp = Processone.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, ProcessoCiveldt.getId_Comarca(), null, true, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);					
					
					return;
				}
				break;
			
				// Consulta tipos de processo disponíveis na área de distribuição escolhida
				case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
					if (ProcessoCiveldt.getAreaDistribuicao().length() > 0 || ProcessoCiveldt.isProcessoDependente()) {
						
						if (request.getParameter("Passo")==null){
						
							String[] lisNomeBusca = {"Classe"};
							String[] lisDescricao = {"Classe","Código","Código CNJ"};
							stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
							request.setAttribute("tempBuscaId","Id_ProcessoTipo");
							request.setAttribute("tempBuscaDescricao","ProcessoTipo");
							request.setAttribute("tempBuscaPrograma","ProcessoTipo");			
							request.setAttribute("tempRetorno","ProcessoCivelSemAssistencia");		
							request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
							request.setAttribute("PaginaAtual", (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
							request.setAttribute("PosicaoPaginaAtual", "0");
							request.setAttribute("QuantidadePaginas", "0");
							request.setAttribute("lisNomeBusca", lisNomeBusca);
							request.setAttribute("lisDescricao", lisDescricao);
							passoEditar = -1;
						} else {
							String stTemp="";
							String id_Serventia = "";
							
							if(ProcessoCiveldt.isProcessoFisico()) {
								if (ProcessoCiveldt.getId_Serventia() != null & ProcessoCiveldt.getId_Serventia().length() > 0) {								
										stTemp = Processone.consultarProcessoTipoServentiaJSON(stNomeBusca1, ProcessoCiveldt.getId_Serventia(), UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
								} else {
									request.setAttribute("MensagemErro", "Não foi possível obter a Area de Distribuição da Serventia escolhida.");
									return;	
								}
							} else {
								if (ProcessoCiveldt.getId_AreaDistribuicao() != null & ProcessoCiveldt.getId_AreaDistribuicao().length() > 0)								
										stTemp = Processone.consultarDescricaoProcessoTipoJSON(stNomeBusca1, ProcessoCiveldt.getId_AreaDistribuicao(), UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
							}						
							
							enviarJSON(response, stTemp);
								
							return;		
						}
					} else {
						request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
					}	
					break;	
								
			// Consulta tipos de prioridade disponíveis
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
					request.setAttribute("tempRetorno","ProcessoCivelSemAssistencia");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = -1;
				} else {
					String stTemp="";
					stTemp = Processone.consultarDescricaoProcessoPrioridadeJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;

			// Consulta Assuntos
			case (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Assunto", "Código CNJ"};
					String[] lisDescricao = {"Assunto","Pai","Disp. Legal", "Código CNJ"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Assunto");
					request.setAttribute("tempBuscaDescricao","Assunto");
					request.setAttribute("tempBuscaPrograma","Assunto");			
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = -1;
					break;
				} else {
					String stTemp = "";
					
					if (ProcessoCiveldt.getId_AreaDistribuicao().length() > 0) 							
						stTemp = Processone.consultarAssuntosAreaDistribuicaoJSON(stNomeBusca1, stNomeBusca2, ProcessoCiveldt.getId_AreaDistribuicao(), PosicaoPaginaAtual);
					else {
						request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
					}
					
					enviarJSON(response, stTemp);
											
					return;								
				}				

			// Consulta de Cidades - Naturalidade usada no cadastro de Partes
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
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
					String[] lisNomeBusca = {"EstadoCivil"};
					String[] lisDescricao = {"EstadoCivil"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_EstadoCivil");
					request.setAttribute("tempBuscaDescricao", "EstadoCivil");
					request.setAttribute("tempBuscaPrograma", "EstadoCivil");
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia?PassoEditar=4&ParteTipo=" + parteTipo);
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
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
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
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
				request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
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

			// Consulta de Orgao Expedidor - Usado no cadastro de partes
			case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					String[] lisNomeBusca = {"OrgaoExpedidor","Sigla"};
					String[] lisDescricao = {"Sigla","Nome","Estado"};
					String[] camposHidden = {"RgOrgaoExpedidorNome", "RgOrgaoExpedidorEstado"};
					request.setAttribute("camposHidden",camposHidden);
					request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
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
				
				
				// Consulta de escolaridade - Usado no cadastro de partes
			case (EscolaridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Escolaridade"};
					String[] lisDescricao = {"Escolaridade"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Escolaridade");
					request.setAttribute("tempBuscaDescricao", "Escolaridade");
					request.setAttribute("tempBuscaPrograma", "Escolaridade");
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
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

			// Consulta de Estado da CTPS - Usado no cadastro de partes
			case (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Uf"};
				String[] lisDescricao = {"Uf", "Descrição"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_CtpsUf");
				request.setAttribute("tempBuscaDescricao", "EstadoCtpsUf");
				request.setAttribute("tempBuscaPrograma", "EstadoCtpsUf");
				request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
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
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
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
					String[] lisDescricao = {"ArquivoTipo"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao", "ArquivoTipo");
					request.setAttribute("tempBuscaPrograma", "ArquivoTipo");
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);							
				}else{
					String stTemp = "";
						stTemp =Processone.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(),stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Modelo"};
					String[] lisDescricao = {"Modelo","Serventia","Tipo Modelo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Modelo");
					request.setAttribute("tempBuscaDescricao", "Modelo");
					request.setAttribute("tempBuscaPrograma", "Modelo");
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
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
					stTemp = Processone.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), ProcessoCiveldt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			// Consultar Advogados
			case (UsuarioServentiaOabDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if(fluxo == 1){
					//consultarAdvogado(request, Processone, ProcessoCiveldt);
					if (ProcessoCiveldt.getOabNumero().length() > 0 && ProcessoCiveldt.getOabComplemento().length() > 0 && ProcessoCiveldt.getOabUf().length() > 0) {
						tempList = UsuarioSessao.consultarAdvogadoOab(ProcessoCiveldt.getOabNumero(), ProcessoCiveldt.getOabComplemento(), ProcessoCiveldt.getOabUf());
						
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
								
								if (ProcessoCiveldt.getListaAdvogados() != null){
									for (Iterator iterator2 = ProcessoCiveldt.getListaAdvogados().iterator(); iterator2.hasNext();) {
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
									ProcessoCiveldt.addListaAdvogados(usuarioServentiaOabDt);
									
									ProcessoCiveldt.setOabNumero("");
									ProcessoCiveldt.setOabComplemento("");
									ProcessoCiveldt.setOabUf("");
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
					String[] lisNomeBusca = {"Tipo de Governo"};
					String[] lisDescricao = {"GovernoTipo"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_GovernoTipo");
					request.setAttribute("tempBuscaDescricao", "GovernoTipo");
					request.setAttribute("tempBuscaPrograma", "GovernoTipo");
					request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia?PassoEditar=4&ParteTipo=" + parteTipo);
					request.setAttribute("tempDescricaoId", "Id_GovernoTipo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(GovernoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);							
				}else{
					String stTemp = "";
						stTemp =Processone.consultarDescricaoGovernoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
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
					
					atribuirJSON(request, "Id_EmpresaTipo", "EmpresaTipo", "EmpresaTipo", "ProcessoCivelSemAssistencia", Configuracao.Editar, permissao, lisNomeBusca, lisDescricao);
					
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
				//	Passo 2 : Efetua consulta da parte e exibe dados para edição
				//	Passo 3 : Valida dados da parte 
				//	Passo 4 : Redireciona para jsp de cadastro de parte 
				// 	Passo 5 : Valida dados do processo 
				//	Passo 6 : Resgata arquivos inseridos no passo 2 e redireciona para passo 3
				//	Passo 7 : Redireciona para jsp de busca de partes
				//	Passo 8 : Remover assuntos do processo
				//	Passo 9	: Redireciona para jsp de Partes Não Personificáveis

				switch (passoEditar) {

					case 0:
						request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia");
						ProcessoCiveldt.setPasso3("");
						stAcao = "/WEB-INF/jsptjgo/ProcessoPeticionar.jsp";
						break;

					case 1:
						stAcao = "/WEB-INF/jsptjgo/ProcessoCivelVisualizar.jsp";
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
						
					case 13: //Editar Parte						
						if (ValidacaoUtil.isNaoVazio(posicaoLista)) {
							int posicao = Funcoes.StringToInt(posicaoLista);
							//excluirParteProcesso(ProcessoCiveldt, posicao, parteTipo);
							
						    if(parteTipo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO) {
						    	ProcessoPartedt = (ProcessoParteDt) ProcessoCiveldt.getPoloAtivo(posicao);						    							    							    
						    	ProcessoCiveldt.getListaPolosAtivos().remove(ProcessoPartedt);
						    	ProcessoPartedt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
						    	if (ProcessoPartedt.getAdvogadoDt() != null && ProcessoPartedt.getAdvogadoDt().getId_UsuarioServentiaAdvogado().length() > 0) ProcessoCiveldt.getListaAdvogados().remove(ProcessoPartedt.getAdvogadoDt());
						    }
						    
						    if(parteTipo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) {
						    	ProcessoPartedt = (ProcessoParteDt) ProcessoCiveldt.getPoloPassivo(posicao);
						    	//ProcessoPartedt = (ProcessoParteDt) ProcessoCiveldt.getListaPromovidos().get(posicao);
						    	ProcessoCiveldt.getListaPolosPassivos().remove(ProcessoPartedt);
						    	ProcessoPartedt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));
						    	if (ProcessoPartedt.getAdvogadoDt() != null && ProcessoPartedt.getAdvogadoDt().getId_UsuarioServentiaAdvogado().length() > 0) ProcessoCiveldt.getListaAdvogados().remove(ProcessoPartedt.getAdvogadoDt());
						    }
						    
						}
						
						request.getSession().setAttribute("ProcessoPartedt", ProcessoPartedt);
						stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
						break;

					case 3: // Valida dados da parte
						if (request.getParameter("parteEnderecoDesconhecido") != null) ProcessoPartedt.setParteEnderecoDesconhecido(request.getParameter("parteEnderecoDesconhecido"));						
						Mensagem = Processone.VerificarParteProcesso(ProcessoPartedt);
						if (Mensagem.length() == 0) {
							if (!parteProcessoCadastrada(ProcessoCiveldt, ProcessoPartedt)) {
								// Valida o tipo de parte e adicion à lista correspondente
								if (parteTipo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO) ProcessoCiveldt.addListaPoloAtivo(ProcessoPartedt);
								else if (parteTipo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) ProcessoCiveldt.addListaPolosPassivos(ProcessoPartedt);
								else ProcessoCiveldt.addListaOutrasPartes(ProcessoPartedt);

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

					case 4:
						stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
						break;

					case 5: // Valida dados do processo
						Mensagem = Processone.verificarProcessoCivel(ProcessoCiveldt, UsuarioSessao.getUsuarioDt());

						if (Mensagem.length() > 0) {
							request.setAttribute("MensagemErro", Mensagem);
							ProcessoCiveldt.setPasso1("Passo 1");
							ProcessoCiveldt.setPasso2("");
							ProcessoCiveldt.setPasso3("");
							passoEditar = -1;
						}
						else {//Se não há erros, passa para passo de inserção de arquivos
							ProcessoCiveldt.setPasso1("Passo 1 OK");
							ProcessoCiveldt.setPasso2("Passo 2");
							stAcao = "/WEB-INF/jsptjgo/ProcessoPeticionar.jsp";
						}
						break;

					case 6: // Resgata lista de arquivos inseridos e adiciona ao processo. Redireciona para confirmação cadastro processo
						if (listaArquivosInseridos != null && listaArquivosInseridos.size() > 0) {
							ProcessoCiveldt.setListaArquivos(listaArquivosInseridos);
							ProcessoCiveldt.setPasso1("Passo 1 OK");
							ProcessoCiveldt.setPasso2("Passo 2 OK");
							ProcessoCiveldt.setPasso3("Passo 3");

							//Gera código do pedido, assim o submit so pode ser executado uma unica vez
							request.setAttribute("__Pedido__", UsuarioSessao.getPedido());

							stAcao = "/WEB-INF/jsptjgo/ProcessoCivelVisualizar.jsp";
							ProcessoCiveldt.setId_ArquivoTipo("");
							ProcessoCiveldt.setArquivoTipo("");
							ProcessoCiveldt.setId_Modelo("");
							ProcessoCiveldt.setModelo("");
							ProcessoCiveldt.setTextoEditor("");
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
						removerAssuntoProcesso(ProcessoCiveldt, posicaoLista);
						break;

					case 9:
						//Chama método para inicializar objeto parte
						buscaParte(request, ProcessoPartedt, parteTipo, passoEditar, Processone);
						stAcao = "/WEB-INF/jsptjgo/PartesNaoPersonificaveis.jsp";
						request.setAttribute("tempRetorno", "ProcessoCivelSemAssistencia?PassoEditar=3&ParteTipo=" + parteTipo);
						break;

					//Redireciona para tela de Inclusão de Advogados
					case 10:
						stAcao = "/WEB-INF/jsptjgo/BuscaAdvogado.jsp";
						break;
						
					//Redireciona para tela de Restaurar Dados
					case 11:
						redireciona(response, "ProcessoCadastroRestaurar?tempRetorno=ProcessoCivelSemAssistencia");
						break;
						
					//Salva os dados da primeira tela de cadastro
					case 12:
						String byTemp = Processone.salvarProcesso(ProcessoCiveldt);
						
						enviarProjudi(response, byTemp.getBytes(), "CadastroProcesso");
						
					    byTemp = null;
					    return;
						
					default:
						ProcessoCiveldt.setPasso2("");
						ProcessoCiveldt.setPasso3("");
						break;
				}
		}

 		request.setAttribute("fluxo",fluxo);
		request.setAttribute("ParteTipo", parteTipo);
		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("parteEnderecoDesconhecido", String.valueOf(ProcessoPartedt.isParteEnderecoDesconhecido()));
		request.getSession().setAttribute("parteDependenteJaSelecionada", parteDependenteJaSelecionada);
		request.getSession().setAttribute("ArquivosInseridos", ProcessoCiveldt.getListaArquivos());
		request.getSession().setAttribute("ProcessoCadastroDt", ProcessoCiveldt);
		request.getSession().setAttribute("Processone", Processone);
		
		GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt");
		request.setAttribute("permitePesquisaComarcaAreaETipo",  guiaEmissaoDt == null || (guiaEmissaoDt != null && guiaEmissaoDt.isGuiaEmitidaSPG()));

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Método que faz tratamentos necessários com parâmetros auxiliares no cadastro de processo
	 */
	public String obtenhaAcaoDefault(ProcessoCadastroDt ProcessoCiveldt){	
		
		if (ProcessoCiveldt.isProcessoFisico()) return "/WEB-INF/jsptjgo/ProcessoCivelFisico.jsp";
		
		if (ProcessoCiveldt.isProcessoDependente()) return "/WEB-INF/jsptjgo/ProcessoComum.jsp";
		
		return "/WEB-INF/jsptjgo/ProcessoComum.jsp";
	}
	
	/**
	 * Método que faz tratamentos necessários com parâmetros auxiliares no cadastro de processo
	 */
	private String obtenhaAcaoDefault(ProcessoCadastroDt ProcessoCiveldt, GuiaEmissaoDt guiaEmissaoDt){	
		
		if (ProcessoCiveldt.isProcessoFisico()) return "/WEB-INF/jsptjgo/ProcessoCivelFisico.jsp";
		
		if (ProcessoCiveldt.isProcessoDependente() &&  guiaEmissaoDt.getNumeroProcessoDependente() != null && guiaEmissaoDt.getNumeroProcessoDependente().length() > 0 ) 
			return "/WEB-INF/jsptjgo/ProcessoComum.jsp";
		
		return "/WEB-INF/jsptjgo/ProcessoComum.jsp";
	}
	
}