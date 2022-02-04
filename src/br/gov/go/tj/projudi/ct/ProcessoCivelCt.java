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
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que controla o cadastramento de um processo cível
 * @author msapaula
 */
public class ProcessoCivelCt extends ProcessoComumCt {

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
		request.setAttribute("tempRetorno", "ProcessoCivel");

		Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null) Processone = new ProcessoNe();

 		ProcessoCiveldt = (ProcessoCadastroDt) request.getSession().getAttribute("ProcessoCadastroDt");
  		if (ProcessoCiveldt == null ){ 			
  			ProcessoCiveldt = new ProcessoCadastroDt();
  		}
  		
  		if(paginaatual==Configuracao.SalvarResultado && !ProcessoCiveldt.isTemPoloAtivo()){ 			
  				redireciona(response, "Usuario?PaginaAtual=-10");
  				return;
  		}
  		
 		if (request.getSession().getAttribute("ProcessoPartedt") != null){
 			ProcessoPartedt = (ProcessoParteDt) request.getSession().getAttribute("ProcessoPartedt");
 		}else{
 			ProcessoPartedt = new ProcessoParteDt();
 		}
 		
 		//Variáveis de sessão
 		GuiaEmissaoDt guiaEmissaoDt = null;
 		guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt");
 		if( guiaEmissaoDt == null ) {
 			guiaEmissaoDt = new GuiaEmissaoDt();
 		}
 		
		request.setAttribute("exibeOficialCompanheiro", new Boolean(true)); //Conforme informado pelo Marcelo da Corregedoria, só é realizado o cálculo em dobro automático na guia inicial.
 		//...............
			
		if (request.getParameter("nomeArquivo")!= null ){
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		}else{
  			request.setAttribute("nomeArquivo", "");
		}
		
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
			if (request.getParameter("ServentiaCargo") != null && request.getParameter("ServentiaCargoUsuario") != null)
				ProcessoCiveldt.setServentiaCargo(request.getParameter("ServentiaCargo") + " - " + request.getParameter("ServentiaCargoUsuario"));
		}
		else{
			ProcessoCiveldt.setId_AreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			ProcessoCiveldt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		}			
		ProcessoCiveldt.setId_ServentiaSubTipo(request.getParameter("Id_ServentiaSubTipo"));
		ProcessoCiveldt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		if(request.getParameter("Id_ProcessoTipo") != null && !request.getParameter("Id_ProcessoTipo").toString().equalsIgnoreCase("")){
			ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(request.getParameter("Id_ProcessoTipo"));
			if(processoTipoDt.isHabeasCorpus()) {
			request.setAttribute("MensagemOk", "ATENÇÂO - Favor informar para a ação de habeas corpus: na parte ativa, o impetrante da ação; na parte passiva, a autoridade coatora; e, em outras partes, a parte do tipo PACIENTE que receberá o beneficio da ação.");
			}
		}
		ProcessoCiveldt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		ProcessoCiveldt.setId_ProcessoPrioridade(request.getParameter("Id_ProcessoPrioridade"));
		ProcessoCiveldt.setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
		ProcessoCiveldt.setProcessoPrioridadeCodigo(request.getParameter("ProcessoPrioridadeCodigo"));
		ProcessoCiveldt.setValor(request.getParameter("Valor"));
		ProcessoCiveldt.setMarcarAudiencia(request.getParameter("NaoMarcarAudiencia"));
		ProcessoCiveldt.setMandarConcluso(request.getParameter("NaoMandarConcluso"));
		ProcessoCiveldt.setSegredoJustica(request.getParameter("SegredoJustica"));	
		ProcessoCiveldt.setSigiloso(request.getParameter("Sigiloso"));		
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
		ProcessoCiveldt.setProcessoNumeroPrincipal(request.getParameter("ProcessoNumeroDependente"));
		
		
		//No passo 3 não entra aqui
		if(ProcessoCiveldt.getPasso2().equalsIgnoreCase("") && ProcessoCiveldt.getPasso3().equalsIgnoreCase("")) {
			if (request.getParameter("digital100")!=null) {
				ProcessoCiveldt.set100Digital(Funcoes.StringToBoolean(request.getParameter("digital100")));
			}
		}
		
		// Nas telas de "Recadastramento Cível e "Recadastramento Criminal" de processos físicos
		// será possível especificar a Data de Recebimento do processo. Portanto, se receber este
		// parâmetro, usará a data especificada pelo usuário. Se ele estiver em branco ou nulo irá
		// persistir a data atual. Portanto, para os casos que não sejam estas duas telas, continuará
		// o mesmo funcionamento.
		ProcessoCiveldt.setDataRecebimento(request.getParameter("dataRecebimento"));
		
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
		
		if (request.getParameter("assistenciaProcesso") != null && (request.getParameter("assistenciaProcesso").equalsIgnoreCase("1")|| request.getParameter("assistenciaProcesso").equalsIgnoreCase("2") || request.getParameter("assistenciaProcesso").equalsIgnoreCase("3")) ){
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
		
		posicaoLista = request.getParameter("posicaoLista");
		
		if (request.getParameter("datafile") != null);
		
		List listaArquivosInseridos = getListaArquivos(request); // Captura arquivos inseridos
		
		//controle de tela 
		if (request.getParameter("assistenciaProcesso") != null && request.getParameter("assistenciaProcesso").equalsIgnoreCase("3")){
			ProcessoCiveldt.setId_Custa_Tipo(String.valueOf(ProcessoCiveldt.ISENTO));
		} 
		
		// Seta Tipo de Custa do Processo
		if (request.getParameter("custaTipo") != null) {
			if (request.getParameter("custaTipo").equals("1")) {
				ProcessoCiveldt.setId_Custa_Tipo(String.valueOf(ProcessoCiveldt.COM_CUSTAS));
				// Exibe a opção de gerar locomoções
				request.setAttribute("exibeparteLocomocoes", false);
			} else if (request.getParameter("custaTipo").equals("2")) {
				ProcessoCiveldt.setId_Custa_Tipo(String.valueOf(ProcessoCiveldt.ASSISTENCIA_JURIDICA));
				// Exibe a opção de gerar locomoções
				request.setAttribute("exibeparteLocomocoes", true);
			} else if(request.getParameter("custaTipo").equals("3")){
				ProcessoCiveldt.setId_Custa_Tipo(String.valueOf(ProcessoCiveldt.ISENTO));
				// Exibe a opção de gerar locomoções
				request.setAttribute("exibeparteLocomocoes", false);
			}
		} else if (ProcessoCiveldt.getId_Custa_Tipo() != null && ProcessoCiveldt.getId_Custa_Tipo().equalsIgnoreCase("2")){
			// Exibe a opção de gerar locomoções
			request.setAttribute("exibeparteLocomocoes", true);
		} else {
			// Exibe a opção de gerar locomoções
			request.setAttribute("exibeparteLocomocoes", false);
		}
								
 		setParametrosAuxiliares(ProcessoCiveldt, ProcessoPartedt, passoEditar, paginaatual, request, paginaAnterior, Processone, UsuarioSessao);
 			
		//Utilizado no cadastro de Partes
		request.setAttribute("exibeGovernoEmpresa", true);
		
		// Exibe a opção de não informar o endereço no cadastro de Partes
		request.setAttribute("exibeparteEnderecoDesconhecido", true);
		
		
		if (request.getParameter("parteLocomocoes") != null) ProcessoPartedt.setParteLocomocoes(request.getParameter("parteLocomocoes"));		

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
				
				//leandro*********************
				this.limparGuia(guiaEmissaoDt);
				request.getSession().removeAttribute("GuiaEmissaoDt");
				//**********************************
				
				ProcessoPartedt.limpar();
				// Seta área do processo
				ProcessoCiveldt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));
				int GrupoTipoCodigo = UsuarioSessao.getGrupoTipoCodigoToInt();
				int GrupoCodigo = UsuarioSessao.getGrupoCodigoToInt();
				if (GrupoTipoCodigo == GrupoTipoDt.ADVOGADO ||	GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO || GrupoCodigo == GrupoDt.MP_TCE) {
					adicionaAdvogadoProcesso(ProcessoCiveldt, UsuarioSessao);
				}
				
				if (UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo() != null && UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo().length() > 0
						&&Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo()) == ServentiaTipoDt.PROMOTORIA){
					request.setAttribute("MensagemOk", "ATENÇÂO - Para o caso de Substituto Processual,  o  Ministério Público deve ser incluído em Substituto  Processual  /  Outras Partes; No campo parte tipo escolha Substituto Processual.");
				}
											
				//Limpa lista DWR e zera contador Arquivos
				request.getSession().removeAttribute("ListaArquivosDwr");
				request.getSession().removeAttribute("ListaArquivos");
				request.getSession().removeAttribute("Id_ListaArquivosDwr");
				/*
				// Seta área do processo
				//TODO Fred:
				ProcessoCiveldt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));
				stAcao = "/WEB-INF/jsptjgo/CadastroProcessoSemAssistencia.jsp";
				*/
				passoEditar = -1;
				break;

			// Remover partes ou advogados inseridos
			case Configuracao.Excluir:
				if (posicaoLista.length() > 0) {
					int posicao = Funcoes.StringToInt(posicaoLista);
					if (passoEditar == 10) {
						//Refere-se a exclusão de advogados
						ProcessoCiveldt.getListaAdvogados().remove(posicao);
						stAcao = "/WEB-INF/jsptjgo/BuscaAdvogado.jsp";
					} else excluirParteProcesso(ProcessoCiveldt, posicao, parteTipo);
					passoEditar = -1;
				}
				break;

			// Salvar dados do Processo Cível
			case Configuracao.SalvarResultado: {
				// Seta área do processo
				ProcessoCiveldt.setAreaCodigo(String.valueOf(AreaDt.CIVEL));
				Processone.cadastrarProcessoCivel(ProcessoCiveldt, UsuarioSessao.getUsuarioDt(), guiaEmissaoDt);
				
				// Redireciona para jsp de Processo Cadastrado com sucesso
				stAcao = "/WEB-INF/jsptjgo/ProcessoCivelCadastrado.jsp";
				
				//TODO Fred:
				//Vincula Guia Inicial ao Processo Cadastrado
				/*GuiaEmissaoDt guiaEmissaoDt = (GuiaEmissaoDt) request.getSession().getAttribute("GuiaEmissaoDt");
				Processone.vinculaGuiaProcesso(guiaEmissaoDt.getId(), ProcessoCiveldt.getId_Processo());
				request.getSession().removeAttribute("GuiaEmissaoDt");*/

				//Joga processo no request e limpa da sessão
				request.setAttribute("ProcessoCadastroDt", ProcessoCiveldt);
				ProcessoCiveldt = new ProcessoCadastroDt();
				ProcessoPartedt = new ProcessoParteDt();
				guiaEmissaoDt = new GuiaEmissaoDt();

				//Limpa lista DWR e zera contador Arquivos
				request.getSession().removeAttribute("ListaArquivosDwr");
				request.getSession().removeAttribute("ListaArquivos");
				request.getSession().removeAttribute("Id_ListaArquivosDwr");
				break;
			}
			
			case Configuracao.Localizar: 
				
				parteDependenteJaSelecionada = "false";
				
				String processoNumeroVinculo = request.getParameter("ProcessoNumero");
		
				if (processoNumeroVinculo != null && processoNumeroVinculo.length() > 0) {
					
					ProcessoCiveldt.setProcessoNumeroPrincipal(processoNumeroVinculo);
					
					ProcessoDt processoVinculoDt = Processone.consultarProcessoDigitalEFisico(processoNumeroVinculo);							
					
					if (processoVinculoDt != null) {
						
						ProcessoCiveldt.setProcessoDependente(true);
						
						if(StringUtils.isNotEmpty(processoVinculoDt.getId())) {
							ProcessoCiveldt.setProcessoDependenteDt(processoVinculoDt);
							ProcessoCiveldt.setId_ProcessoPrincipal(processoVinculoDt.getId());
							
							if(ProcessoCiveldt.isProcessoMesmaArea() && ProcessoCiveldt.isMesmoGrauJurisdicao()){
								ProcessoCiveldt.setId_AreaDistribuicao(processoVinculoDt.getId_AreaDistribuicao());
								ProcessoCiveldt.setAreaDistribuicao(processoVinculoDt.getAreaDistribuicao());
								ProcessoCiveldt.setForumCodigo(processoVinculoDt.getForumCodigo());
								//Consulta assuntos do processo originário
								getAssuntosProcessoOriginario(ProcessoCiveldt, processoVinculoDt.getId(), Processone);
							}
						} else if (StringUtils.isNotEmpty(processoVinculoDt.getProcessoFisicoTipo())) {
							ProcessoCiveldt.setProcessoFisicoTipo(processoVinculoDt.getProcessoFisicoTipo());
							ProcessoCiveldt.setProcessoFisicoNumero(processoVinculoDt.getProcessoFisicoNumero());
							ProcessoCiveldt.setProcessoFisicoComarcaNome(processoVinculoDt.getProcessoFisicoComarcaNome());
							ProcessoCiveldt.setProcessoFisicoComarcaCodigo(processoVinculoDt.getProcessoFisicoComarcaCodigo());
						} else {
							super.limpaProcesso(UsuarioSessao, ProcessoCiveldt, Processone);
							request.setAttribute("MensagemErro", "Não foi possível localizar o Processo informado.");
						}
								
					} else {
						super.limpaProcesso(UsuarioSessao, ProcessoCiveldt, Processone);
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
					request.setAttribute("tempRetorno","ProcessoCivel");		
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
				} else {
					String stTemp="";
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
					request.setAttribute("tempRetorno","ProcessoCivel");
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
				if (ProcessoCiveldt.getId_Comarca().length() > 0) {
					if (request.getParameter("Passo")==null && request.getParameter("tempFluxo1") == null){
						String[] lisNomeBusca = {"Área Distribuição"};
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
						request.setAttribute("tempRetorno","ProcessoCivel");		
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						passoEditar = -1;
					}  else {
						String stTemp="";
						stTemp = Processone.consultarAreasDistribuicaoCivelJSON(stNomeBusca1, ProcessoCiveldt.getId_Comarca(), UsuarioSessao.getUsuarioDt().getId_Serventia(), ProcessoCiveldt.isComCusta(), PosicaoPaginaAtual);
						enviarJSON(response, stTemp);							
						return;								
					}
				} else {
					request.setAttribute("MensagemErro", "Selecione primeiramente a Comarca.");
				}
				ProcessoCiveldt.setId_ProcessoTipo("null");
				ProcessoCiveldt.setProcessoTipo("");
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
						request.setAttribute("tempRetorno","ProcessoCivel");		
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						passoEditar = -1;
					} else {
						String stTemp="";
						if(ProcessoCiveldt.isProcessoFisico()) {
							if (ProcessoCiveldt.getId_Serventia() != null & ProcessoCiveldt.getId_Serventia().length() > 0) {								
									stTemp = Processone.consultarProcessoTipoServentiaJSON(stNomeBusca1, ProcessoCiveldt.getId_Serventia(), UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
							} else {
								request.setAttribute("MensagemErro", "Não foi possível obter a Area de Distribuição da Serventia escolhida.");
								return;	
							}
						} else {
							if (ProcessoCiveldt.getId_AreaDistribuicao() != null & ProcessoCiveldt.getId_AreaDistribuicao().length() > 0){								
								stTemp = Processone.consultarDescricaoProcessoTipoJSON(stNomeBusca1, ProcessoCiveldt.getId_AreaDistribuicao(), UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
							}  else {
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
					request.setAttribute("tempRetorno","ProcessoCivel");		
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

			// Consulta Assuntos
			case (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (ProcessoCiveldt.getProcessoTipo().length() > 0) {
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Assunto", "Código CNJ"};
						String[] lisDescricao = {"Assunto","Pai","Disp. Legal", "Código CNJ"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_Assunto");
						request.setAttribute("tempBuscaDescricao","Assunto");
						request.setAttribute("tempBuscaPrograma","Assunto");			
						request.setAttribute("tempRetorno","ProcessoCivel");		
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
						passoEditar = -1;
					} else {
						String stTemp = "";
						if (ProcessoCiveldt.getId_AreaDistribuicao().length() > 0) {								
							stTemp = Processone.consultarAssuntosAreaDistribuicaoJSON(stNomeBusca1, stNomeBusca2, ProcessoCiveldt.getId_AreaDistribuicao(), PosicaoPaginaAtual);
						} else {
							request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
						}
						enviarJSON(response, stTemp);
						return;								
					}
				} else {
					request.setAttribute("MensagemErro", "Selecione primeiramente a Classe.");
				}
				break;

			// Consulta de Cidades - Naturalidade usada no cadastro de Partes
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
					
					String stPemissao = String.valueOf(EstadoCivilDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_EstadoCivil", "EstadoCivil", "EstadoCivil", "ProcessoCivel", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
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
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
					request.setAttribute("tempBuscaId", "Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao", "RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma", "RgOrgaoExpedidor");
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
					String[] lisDescricao = {"Uf", "Descrição"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_CtpsUf");
					request.setAttribute("tempBuscaDescricao", "EstadoCtpsUf");
					request.setAttribute("tempBuscaPrograma", "EstadoCtpsUf");
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","ProcessoCivel");		
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
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
				
			// Consulta de Governo Tipo - Usado no cadastro de partes			
			case (GovernoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"GovernoTipo"};
					String[] lisDescricao = {"GovernoTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_GovernoTipo");
					request.setAttribute("tempBuscaDescricao", "GovernoTipo");
					request.setAttribute("tempBuscaPrograma", "GovernoTipo");
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
					String[] lisNomeBusca = {"EmpresaTipo"};
					String[] lisDescricao = {"EmpresaTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_EmpresaTipo");
					request.setAttribute("tempBuscaDescricao", "EmpresaTipo");
					request.setAttribute("tempBuscaPrograma", "EmpresaTipo");
					request.setAttribute("tempRetorno", "ProcessoCivel");
					request.setAttribute("tempDescricaoDescricao", "EmpresaTipo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EmpresaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 4;
				}else{
					String stTemp = "";
					stTemp = Processone.consultarDescricaoEmpresaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
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
					request.setAttribute("tempRetorno", "ProcessoCivel");
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
				//	Passo 8 : Remover assuntos do processo
				//	Passo 9	: Redireciona para jsp de Partes Não Personificáveis

				switch (passoEditar) {

					case 0:
						request.setAttribute("tempRetorno", "ProcessoCivel");
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

					case 3: // Valida dados da parte
						if (request.getParameter("parteEnderecoDesconhecido") != null) ProcessoPartedt.setParteEnderecoDesconhecido(request.getParameter("parteEnderecoDesconhecido"));						
						Mensagem = Processone.VerificarParteProcesso(ProcessoPartedt);
						if (Mensagem.length() == 0) {
							if (!parteProcessoCadastrada(ProcessoCiveldt, ProcessoPartedt)) {
								// Valida o tipo de parte e adicion à lista correspondente
								if(ProcessoCiveldt.is100Digital()) {
									if (parteTipo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO) {
										if (!ProcessoPartedt.isTemEmail()){
											request.setAttribute("MensagemErro", "Para processos 100% Digitais o Email dos Polos ATIVOS devem estar preenchidos.");
											break;
										}
										if (!ProcessoPartedt.isTemTelefone()){
											request.setAttribute("MensagemErro", "Para processos 100% Digitais o Email dos Polos ATIVOS devem estar preenchidos.");
											break;
										}
									}
								}
								
								if (parteTipo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO) ProcessoCiveldt.addListaPoloAtivo(ProcessoPartedt);
								else if (parteTipo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) ProcessoCiveldt.addListaPolosPassivos(ProcessoPartedt);
								else ProcessoCiveldt.addListaOutrasPartes(ProcessoPartedt);

								//ProcessoPartedt = new ProcessoParteDt();
								request.getSession().setAttribute("ProcessoPartedt", ProcessoPartedt);
							} else{
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
//						if (ProcessoCiveldt.isProcessoDependente()){
//							getPartesRecorrentes(request, ProcessoCiveldt, UsuarioSessao);
//						}
						
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
						
						if (posicaoLista != null && posicaoLista.length() > 0) {
							ProcessoCiveldt.removeAssunto(Funcoes.StringToInt(posicaoLista));																		
						}
						break;

					case 9:
						//Chama método para inicializar objeto parte
						buscaParte(request, ProcessoPartedt, parteTipo, passoEditar, Processone);
						stAcao = "/WEB-INF/jsptjgo/PartesNaoPersonificaveis.jsp";
						request.setAttribute("tempRetorno", "ProcessoCivel?PassoEditar=3&ParteTipo=" + parteTipo);
						break;

					//Redireciona para tela de Inclusão de Advogados
					case 10:
						stAcao = "/WEB-INF/jsptjgo/BuscaAdvogado.jsp";
						break;
						
					//Redireciona para tela de Restaurar Dados
					case 11:
						redireciona(response, "ProcessoCadastroRestaurar?tempRetorno=ProcessoCivel");
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
		request.setAttribute("permitePesquisaComarcaAreaETipo",  Boolean.TRUE);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método que faz tratamentos necessários com parâmetros auxiliares no cadastro de processo
	 */
	public String obtenhaAcaoDefault(ProcessoCadastroDt processoCiveldt){	
		
		if (processoCiveldt.isProcessoFisico()) return "/WEB-INF/jsptjgo/ProcessoCivelFisico.jsp";
		
		if (processoCiveldt.isProcessoDependente()) return "/WEB-INF/jsptjgo/ProcessoComum.jsp";
		
		return "/WEB-INF/jsptjgo/ProcessoComum.jsp";
	}
	
	
//	protected void gerarGuiaInicialProcesso(GuiaEmissaoDt guiaEmissaoDt, ProcessoCadastroDt ProcessoCiveldt, ProcessoNe processoNe, UsuarioDt usuarioDt) {
//		List listaItensGuia = null;
//		
//		//Consulta lista de itens da guia
//		try {
//			
//			guiaEmissaoDt.setId_Comarca(ProcessoCiveldt.getId_Comarca());
//			guiaEmissaoDt.setId_AreaDistribuicao(ProcessoCiveldt.getId_AreaDistribuicao());
//			guiaEmissaoDt.setId_ProcessoTipo(ProcessoCiveldt.getId_ProcessoTipo());
//			guiaEmissaoDt.setValorAcao(ProcessoCiveldt.getValor());
//			guiaEmissaoDt.setId_GuiaStatus(GuiaStatusDt.AGUARDANDO_PAGAMENTO);
//			
//			guiaEmissaoDt.setRequerente(ProcessoCiveldt.getPrimeiroPromoventeNome());
//			guiaEmissaoDt.setRequerido(ProcessoCiveldt.getPrimeiroPromovidoNome());
//			
//			listaItensGuia = processoNe.consultarItensGuiaCustaModeloDtProcessoTipo(null, guiaEmissaoDt, GuiaTipoDt.INICIAL_PRIMEIRO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
//		
//			//consulta guia modelo
//			GuiaModeloDt guiaModeloDt = processoNe.consultarGuiaModeloProcessoTipo(null, GuiaTipoDt.NICIAL_PRIMEIRO_GRAU, guiaEmissaoDt.getId_ProcessoTipo());
//			guiaEmissaoDt.setGuiaModeloDt(guiaModeloDt);
//			
//			Map valoresReferenciaCalculo = new HashMap();
//			valoresReferenciaCalculo.put(CustaDt.VALOR_CAUSA, 				guiaEmissaoDt.getNovoValorAcao());
//			valoresReferenciaCalculo.put(CustaDt.VALOR_BENS, 				guiaEmissaoDt.getNovoValorAcao());
//			valoresReferenciaCalculo.put(CustaDt.PENHORA_QUANTIDADE_VALOR, 	guiaEmissaoDt.getNovoValorAcao());
//			
//			if( guiaEmissaoDt.getNovoValorAcaoAtualizado().toString().length() == 0 ) {
//				valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, "0");
//			}
//			else {
//				valoresReferenciaCalculo.put(CustaDt.TAXA_JUDICIARIA, guiaEmissaoDt.getNovoValorAcaoAtualizado());
//			}
//			
//			if( guiaEmissaoDt.getProcessoTipoCodigo() != null && guiaEmissaoDt.getProcessoTipoCodigo().length() > 0 && processoNe.isProcessoTipoMandado(Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo())) ) {
//				valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
//				if( guiaEmissaoDt.getNumeroImpetrantes() != null && guiaEmissaoDt.getNumeroImpetrantes().length() > 0 ) {
//					valoresReferenciaCalculo.put(CustaDt.MANDADOS, guiaEmissaoDt.getNumeroImpetrantes() + ";" + ProcessoTipoDt.MANDADO_SEGURANCA_8069);
//				}
//				if( guiaEmissaoDt.getProcessoTipoCodigo() != null && Funcoes.StringToInt(guiaEmissaoDt.getProcessoTipoCodigo()) == ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO ) {
//					valoresReferenciaCalculo.put(CustaDt.MANDADOS, "0;" + ProcessoTipoDt.MANDADO_SEGURANCA_COLETIVO);
//				}
//			}
//			
//			if( guiaEmissaoDt.getId_NaturezaSPG() != null && 
//				guiaEmissaoDt.getId_NaturezaSPG().length() > 0 && 
//				Integer.parseInt(guiaEmissaoDt.getId_NaturezaSPG()) == NaturezaSPGDt.MANDADO_SEGURANCA ) {
//				
//				valoresReferenciaCalculo.put(CustaDt.MANDADOS, guiaEmissaoDt.getNumeroImpetrantes() + ";" + guiaEmissaoDt.getProcessoTipoCodigo());
//			}
//	
//			List listaGuiaItemDt = new ArrayList();
//			listaGuiaItemDt = processoNe.calcularItensGuia(guiaEmissaoDt, listaItensGuia, valoresReferenciaCalculo);
//			guiaEmissaoDt.setDataVencimento(Funcoes.getDataVencimentoGuia());
//			guiaEmissaoDt.setListaGuiaItemDt(listaGuiaItemDt);
//			
//			//nova parte
//			
//			if( guiaEmissaoDt.getNumeroGuiaCompleto() == null ) {
//				guiaEmissaoDt.setNumeroGuia(GuiaNumero.getInstance().getGuiaNumero());
//				guiaEmissaoDt.setNumeroGuiaCompleto( processoNe.getNumeroGuiaCompleto(guiaEmissaoDt.getNumeroGuia()) );
//			}
//			
//			guiaEmissaoDt.setId_Usuario(usuarioDt.getId());
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
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
		guiaEmissaoDt.setCodigoGrau(GuiaTipoDt.ID_INICIAL_PRIMEIRO_GRAU);
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