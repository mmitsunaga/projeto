package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EmpresaTipoDt;
import br.gov.go.tj.projudi.dt.EscolaridadeDt;
import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.GovernoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAlcunhaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteSinalDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.dt.RacaDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;
 
/**
 * Controla a alteração e inserção de novas partes em um processo, e ainda a baixa ou restauração dessas.
 * Essa servlet irá gerenciar as partes de processo de 1º e 2º grau.
 * 
 * @author msapaula
 * 05/02/2010 09:44:04
 */
public class ProcessoParteCt extends ProcessoParteCtGen {

	private static final long serialVersionUID = -2022757480223873998L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDt processoDt;
		ProcessoParteNe ProcessoPartene;
		ProcessoParteDt ProcessoPartedt = null;

		String stId = "";
		String Mensagem = "";
		int passoEditar = -1;
		int parteTipo = 0;
		String posicaoLista = "";
		String stAcao = "/WEB-INF/jsptjgo/ProcessoParte.jsp";
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");

		request.setAttribute("tempPrograma", "Parte Processo");
		request.setAttribute("tempRetorno", "ProcessoParte");

		ProcessoPartene = (ProcessoParteNe) request.getSession().getAttribute("ProcessoPartene");
		if (ProcessoPartene == null) ProcessoPartene = new ProcessoParteNe();

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

		if (processoDt == null){
			request.getSession().removeAttribute("ProcessoPartedt");
			request.getSession().removeAttribute("processoDt");
			throw new MensagemException("Encontrado conflito na sessão por utlização de várias abas");
		}

		if (request.getSession().getAttribute("ProcessoPartedt") != null){			
			ProcessoPartedt = (ProcessoParteDt) request.getSession().getAttribute("ProcessoPartedt");			
		}else{
			ProcessoPartedt = new ProcessoParteDt();			
		}

		if (paginaatual != Configuracao.SalvarResultado) setDadosProcessoParte(request, ProcessoPartedt, processoDt, UsuarioSessao);

		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("ParteTipo") != null) parteTipo = Funcoes.StringToInt(request.getParameter("ParteTipo"));
		posicaoLista = request.getParameter("posicaoLista");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("posicaoLista", posicaoLista);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		//Somente poderá inserir outras partes se não for recurso inominado
		//regra retirada por solicitação de usuários
//		if (processoDt.getId_Recurso() == null || processoDt.getId_Recurso().length() == 0) {
//			request.setAttribute("podeCadastrar", "true");
//		}
		request.setAttribute("podeCadastrar", "true");
		
		//Somente será exibido o tipo da empresa e o tipo de governo se o processo for da area cível		
		if(processoDt != null && processoDt.getAreaCodigo().equalsIgnoreCase(String.valueOf(AreaDt.CIVEL))){
			request.setAttribute("exibeGovernoEmpresa", true);
		}
		else{
			request.setAttribute("exibeGovernoEmpresa", false);
		}
		
		//Quando este controle for utilizado para manipular a jsp ProcessoParteEditar o label do botão será Salvar, 
		//caso contrário o valor será Inserir, pois se trata apenas de seleção da Parte ficando a responsabilidade 
		//da persistência por conta do Controle requisitante
		request.setAttribute("valueBotaoSalvar", "Salvar");
		
			
		// Verifica a opção que o usuário informou para o endereço da parte
		if (request.getParameter("parteEnderecoDesconhecido") != null) ProcessoPartedt.setParteEnderecoDesconhecido(request.getParameter("parteEnderecoDesconhecido"));	
		
		switch (paginaatual) {

			//Prepara tela para alteração de partes do processo
			case Configuracao.Novo:
				if (processoDt != null) {
					//Verifica se usuário pode modificar dados
					Mensagem = ProcessoPartene.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
					if (Mensagem.length() > 0) {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
						return;
					}

					if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))	|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA))){
						ProcessoPartedt.setProcessoParteTipoCodigo(String.valueOf(parteTipo));
						stId = processoDt.getIdPrimeiroPoloPassivo();
						if (stId != null && !stId.equalsIgnoreCase("")) {
							ProcessoPartedt.limpar();
							ProcessoPartedt = ProcessoPartene.consultarId(stId);
							ProcessoPartedt.setListaAlcunhaParte(ProcessoPartene.listarAlcunha(stId));
							ProcessoPartedt.setListaSinalParte(ProcessoPartene.listarSinal(stId));
							if (ProcessoPartedt.getId_Endereco() == null || ProcessoPartedt.getId_Endereco().trim().length() == 0){
								ProcessoPartedt.setParteEnderecoDesconhecido("true");
							}							
						}
						stAcao = "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
						break;
					} else {
						//Efetua consulta das partes do processo, para que liste também as partes que foram baixadas
						ProcessoPartene.setPartesProcesso(processoDt);	
					}
				}
				break;

			case Configuracao.Salvar:
				if (request.getParameter("SegredoJustica") == null) processoDt.setSegredoJustica("false");
				else processoDt.setSegredoJustica(request.getParameter("SegredoJustica"));

				//Restauração de uma parte no processo (Se passoEditar é igual a 2)
				if (passoEditar == 2) {
					ProcessoPartedt = getParteSelecionada(request, processoDt, posicaoLista, request.getParameter("Id_ProcessoParte"), parteTipo, UsuarioSessao);
					request.setAttribute("Mensagem", "Confirma a Restauração da Parte " + ProcessoPartedt.getNome() + "?");
				} else request.setAttribute("Mensagem", "Confirma a alteração da(s) Parte(s) ?");
				break;

			//Salvar alterações das Partes do Processo
			case Configuracao.SalvarResultado:

				// Se passoEditar é 2 - Restauração de uma parte no processo
				if (passoEditar == 2) {
					if (ProcessoPartedt == null) request.setAttribute("MensagemErro", "Nenhuma Parte foi selecionada.");
					else {
						ProcessoPartene.restauraParteProcesso(ProcessoPartedt);
						//Atualiza lista de partes
						ProcessoPartene.setPartesProcessoAtivas(processoDt);
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Parte " + ProcessoPartedt.getNome() + " restaurada com sucesso.");
						return;
					}
				} else {
					//Salva alteração em uma parte do processo
					Mensagem = ProcessoPartene.Verificar(ProcessoPartedt);
					//Se dados obrigatórios foram preenchidos
					if (Mensagem.length() == 0) {
						//Só deve verificar se uma parte já foi cadastrada se não tratar de edição da mesma
						if (ProcessoPartedt.getId_ProcessoParte().length() == 0 && parteProcessoCadastrada(processoDt, ProcessoPartedt)) {
							request.setAttribute("MensagemErro", "Parte já inserida no processo.");
						} else {
							if (ProcessoPartedt.getDataSentenca().length() != 0) {
								Mensagem = ProcessoPartene.consisteDataSentenca(ProcessoPartedt.getDataSentenca(), processoDt.getDataRecebimento());
							
								if (Mensagem.length() != 0) {
									request.setAttribute("MensagemErro", Mensagem);
									break;
								}
							}
							
							if (ProcessoPartedt.getDataPronuncia().length() != 0) {
								Mensagem = ProcessoPartene.consisteDataPronuncia(ProcessoPartedt.getDataPronuncia(), processoDt.getDataRecebimento());
							
								if (Mensagem.length() != 0) {
									request.setAttribute("MensagemErro", Mensagem);
									break;
								}
							}
							
							if (!comparaHashPagina(request.getParameter("Hash").toString(),ProcessoPartedt.getId_Processo(),request.getParameter("HashParte"),ProcessoPartedt.getId_ProcessoParte()))
								throw new MensagemException("Encontrado conflito na sessão por utlização de várias abas");
							ProcessoPartene.salvar(ProcessoPartedt);
							request.setAttribute("MensagemOk", "Dados Salvos com sucesso");
							if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))
									|| processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA))){
								redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Dados Salvos com sucesso.");
								return;
							}
						}
					} else {
						request.setAttribute("MensagemErro", Mensagem);
//						stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
						stAcao = getStAcaoEditar(processoDt.getProcessoTipoCodigo());
					}

					//Atualiza lista de partes
					ProcessoPartene.setPartesProcesso(processoDt);
				}
				break;

			case Configuracao.Excluir:
				//passoEditar = 0;
				//Setando passoEditar como 0 para que na confirmação da exclusão caia no default do Editar.
				ProcessoPartedt = getParteSelecionada(request, processoDt, posicaoLista, request.getParameter("Id_ProcessoParte"), parteTipo, UsuarioSessao);
				ServentiaDt serventiaDt = new ServentiaDt();
				serventiaDt = ProcessoPartene.consultarServentia(processoDt.getId_Serventia());
				
				if (!ProcessoPartene.possuiPartesIsentasClasseIsenta(processoDt, ProcessoPartedt) && serventiaDt.isComCustas() ){
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + "A parte isenta não pode ser baixada ou excluída, pois o processo possui o tipo de custa isento!");
					return;
				}
				
				if (passoEditar == 1) {
					request.setAttribute("Mensagem", "Confirma a Retirada de Ausência da Parte " + ProcessoPartedt.getNome() + "?");
				} else if(passoEditar == 0) {
					request.setAttribute("Mensagem", "Confirma a baixa da Parte " + ProcessoPartedt.getNome() + "?");
				} else if (passoEditar == 2){
					request.setAttribute("Mensagem", "Confirma a exclusão da Parte " + ProcessoPartedt.getNome() + "? A exclusão da parte impede o aparecimento da mesma nas certidões.");
				}
				break;

			// Dar baixa em Parte do processo
			case Configuracao.ExcluirResultado:
				if (ProcessoPartedt == null) request.setAttribute("MensagemErro", "Nenhuma Parte foi selecionada");
				else {
					//PassoEditar 1 - Retirar Revelia/Contumácia
					if (passoEditar == 1) {
						ProcessoPartene.retirarAusenciaProcessoParte(ProcessoPartedt);
						Mensagem = " Parte " + ProcessoPartedt.getNome() + " teve ausência retirada com sucesso.";
					} else if(passoEditar == 0){
						ProcessoPartene.desabilitaParteProcesso(ProcessoPartedt);
						Mensagem = " Parte " + ProcessoPartedt.getNome() + " baixada com sucesso.";
					} else if(passoEditar == 2){
						ProcessoPartene.excluirParteProcesso(ProcessoPartedt);
						Mensagem = " Parte " + ProcessoPartedt.getNome() + " excluída com sucesso. Enquanto a parte estiver excluída, a mesma não aparecerá nas certidões.";
					}

					//Atualiza lista de partes
					ProcessoPartene.setPartesProcessoAtivas(processoDt);
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + Mensagem);
					return;
				}
				break;
				
			//Visualizar outras partes do processo
			case Configuracao.Curinga6:
				if (processoDt != null) {
					//Efetua consulta das partes do processo, para que liste também as partes que foram baixadas
					ProcessoPartene.setPartesProcesso(processoDt, UsuarioSessao.getUsuarioDt(), UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt());
					//se a consulta for pública, seta o atributo na sessão
					if(UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.PUBLICO))) {
						request.getSession().setAttribute("TipoConsulta", "Publica");
					}
					stAcao = "/WEB-INF/jsptjgo/ProcessoParteVisualizar.jsp";
				}
				break;

			//Consulta de Tipos de Parte
			case (ProcessoParteTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Descrição"};
					String[] lisDescricao = {"ProcessoParteTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoParteTipo");
					request.setAttribute("tempBuscaDescricao","ProcessoParteTipo");
					request.setAttribute("tempBuscaPrograma","Tipo de Parte");			
					request.setAttribute("tempRetorno","ProcessoParte?PassoEditar=1&ParteTipo=" + parteTipo);		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoParteTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = ProcessoPartene.consultarOutrosTiposPartesJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;

			//Consulta de Escolaridade
			case (EscolaridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :

				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Escolaridade"};
					String[] lisDescricao = {"Escolaridade"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Escolaridade");
					request.setAttribute("tempBuscaDescricao", "Escolaridade");
					request.setAttribute("tempBuscaPrograma", "Escolaridade");
					request.setAttribute("tempRetorno", "ProcessoParte");
					request.setAttribute("tempDescricaoId", "Id_Escolaridade");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(EscolaridadeDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);							
				}else{
					String stTemp = "";
					stTemp =ProcessoPartene.consultarDescricaoEscolaridadeJSON(tempNomeBusca, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "ProcessoParte");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 1;
				}else{
					String stTemp = "";
					stTemp = ProcessoPartene.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			//Consulta de Estado Civil - Usado no cadastro de partes
			case (EstadoCivilDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Estado Civil"};
					String[] lisDescricao = {"Estado Civil"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPemissao = String.valueOf(EstadoCivilDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_EstadoCivil", "EstadoCivil", "EstadoCivil", "ProcessoParte", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
					passoEditar = 1;
				}else{
					String stTemp = "";
					stTemp = ProcessoPartene.consultarDescricaoEstadoCivilJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;

			//Consulta de Profissão - Usado no cadastro de partes
			case (ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Descrição"};
					String[] lisDescricao = {"Descrição"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Profissao");
					request.setAttribute("tempBuscaDescricao","Profissao");
					request.setAttribute("tempBuscaPrograma","Profissão");			
					request.setAttribute("tempRetorno","ProcessoParte");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProfissaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = ProcessoPartene.consultarDescricaoProfissaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}				
				break;

			//Consulta de Orgao Expedidor - Usado no cadastro de partes
			case (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):

				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Sigla", "Descrição"};
					String[] lisDescricao = {"Sigla","Nome","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_RgOrgaoExpedidor");
					request.setAttribute("tempBuscaDescricao","RgOrgaoExpedidor");
					request.setAttribute("tempBuscaPrograma","Órgão Expeditor RG");			
					request.setAttribute("tempRetorno","ProcessoParte?PassoEditar=1&ParteTipo=" + parteTipo);		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (RgOrgaoExpedidorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = ProcessoPartene.consultarDescricaoOrgaoExpedidorJSON(stNomeBusca1, stNomeBusca2,PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;

			//Consulta de Estado da CTPS - Usado no cadastro de partes 
			case (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome"};
					String[] lisDescricao = {"UF","Descrição"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_CtpsUf");
					request.setAttribute("tempBuscaDescricao","SiglaCtpsUf");
					request.setAttribute("tempBuscaPrograma","Estado");	
					request.setAttribute("tempRetorno","ProcessoParte?PassoEditar=1&ParteTipo=" + parteTipo);
					request.setAttribute("tempFluxo1", "1");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = ProcessoPartene.consultarDescricaoEstadoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
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
					request.setAttribute("tempRetorno", "ProcessoParte");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (BairroDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("ParteTipo", parteTipo);
					passoEditar = 1;
				} else{
					String stTemp = "";
					stTemp = ProcessoPartene.consultarDescricaoBairroJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			// Consulta de Governo Tipo - Usado no cadastro de partes			
			case (GovernoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tipo de Governo"};
					String[] lisDescricao = {"GovernoTipo"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPemissao = String.valueOf(GovernoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);

					atribuirJSON(request, "Id_GovernoTipo", "GovernoTipo", "GovernoTipo", "ProcessoParte?PassoEditar=1&ParteTipo=" + parteTipo, Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
					
				}else{
					String stTemp = "";
					stTemp =ProcessoPartene.consultarDescricaoGovernoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
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
					
					atribuirJSON(request, "Id_EmpresaTipo", "EmpresaTipo", "EmpresaTipo","ProcessoParte?PassoEditar=1&ParteTipo=" + parteTipo, Configuracao.Editar, permissao, lisNomeBusca, lisDescricao);
					
				} else {
					String stTemp="";
					stTemp = ProcessoPartene.consultarDescricaoEmpresaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;									

			 case (RacaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				 if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Raca"};
					String[] lisDescricao = {"Raca"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPemissao = String.valueOf(RacaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					atribuirJSON(request, "Id_Raca", "Raca", "Raca", "ProcessoParte?PassoEditar=1&ParteTipo=" + parteTipo, Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
				} else {
					String stTemp = "";
					stTemp =ProcessoPartene.consultarDescricaoRacaJSON(stNomeBusca1, PosicaoPaginaAtual);
						
					enviarJSON(response, stTemp);
						
					return;
				}
				break;
			 case Configuracao.Curinga9:
				 ProcessoPartene.atualizarNomeSimplificado();
				 break;
			default:

				/**
				 * Essa função é utilizada na inclusão de novas partes ao processo ou edição dos dados da mesma.
				 * É dividida da seguinte forma: 
				 * Passo 0 : Tela para busca de partes 
				 * Passo 1 : Redireciona para jsp de cadastro ou edição de partes
				 * Passo 2 : Realiza a consulta da parte 
				 * Passo 3 : Confirmação de cadastro ou edição de partes
				 * Passo 4 : Consulta parte selecionada e redireciona para tela de edição 
				 * Passo 9 : Redireciona para lista de partes não personificáveis
				 */
				switch (passoEditar) {
					case 0:
						stAcao = "/WEB-INF/jsptjgo/BuscaProcessoParte.jsp";
						break;

					case 1:
//						stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
						stAcao = getStAcaoEditar(processoDt.getProcessoTipoCodigo());
						break;

					case 2: //Busca a parte e exibe dados
						String cpfCnpj = "";
						if (request.getParameter("CpfCnpj") != null) cpfCnpj = request.getParameter("CpfCnpj").replaceAll("[/.-]", "").trim();
						//Valida CPF digitado
						Mensagem = ProcessoPartene.VerificarCpfCnpjParte(cpfCnpj);
						if (Mensagem.length() == 0) {
							ProcessoPartedt = buscaParte(request, parteTipo, ProcessoPartene);
//							stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
							stAcao = getStAcaoEditar(processoDt.getProcessoTipoCodigo());
						} else {
							request.setAttribute("MensagemErro", Mensagem);
							stAcao = "/WEB-INF/jsptjgo/BuscaProcessoParte.jsp";
						}
						break;

					case 3: //Confirmação de cadastro ou edição de partes
						if (!comparaHashPagina(request.getParameter("Hash").toString(),ProcessoPartedt.getId_Processo(),request.getParameter("HashParte"),ProcessoPartedt.getId_ProcessoParte()))
							throw new MensagemException("Encontrado conflito na sessão por utlização de várias abas");
						request.setAttribute("PaginaAnterior", Configuracao.Salvar);
						request.setAttribute("Mensagem", "Confirma os dados da Parte?");
						request.setAttribute("__Pedido__", UsuarioSessao.getPedido());

						stAcao = getStAcaoEditar(processoDt.getProcessoTipoCodigo());
						break;

					case 4://Editar dados da parte
						stId = request.getParameter("Id_ProcessoParte");
						if (stId != null && !stId.equalsIgnoreCase("")) {
							ProcessoPartedt.limpar();
							ProcessoPartedt = ProcessoPartene.consultarId(stId);
							
							ProcessoPartedt.setParteIsenta(ProcessoPartene.isParteIsenta(ProcessoPartedt));
							
//							if (ProcessoPartene.isParteIsenta(ProcessoPartedt)){
//								redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + "Os dados da Parte Isenta não podem ser editados!");
//								return;
//							}
							
							if (ProcessoPartedt.getId_Endereco() == null || ProcessoPartedt.getId_Endereco().trim().length() == 0){
								ProcessoPartedt.setParteEnderecoDesconhecido("true");
							}							
						}
//						stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
						stAcao = getStAcaoEditar(processoDt.getProcessoTipoCodigo());
						break;

					case 5: //Confirmação de cadastro de partes não personificáveis
						if (ProcessoPartedt.ParteNaoPersonificavel() && ProcessoPartedt.getProcessoParteTipoCodigo().equals("-1")) {
//							stAcao = "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
							stAcao = getStAcaoEditar(processoDt.getProcessoTipoCodigo());
						} else {
							request.setAttribute("PaginaAnterior", Configuracao.Salvar);
							request.setAttribute("Mensagem", "Confirma a inclusão da parte " + ProcessoPartedt.getNome() + "?");
							request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
						}
						break;

					case 9://Partes Não Personificáveis
						ProcessoPartedt = new ProcessoParteDt();
						ProcessoPartedt.setProcessoParteTipoCodigo(String.valueOf(parteTipo));
						ProcessoPartedt.setParteNaoPersonificavel(true);
						stAcao = "/WEB-INF/jsptjgo/PartesNaoPersonificaveis.jsp";
						request.setAttribute("tempRetorno", "ProcessoParte?PassoEditar=5&ParteTipo=" + parteTipo);
						break;

					 case 12: // Atualiza alcunha da parte
			                if (posicaoLista != null && posicaoLista.length() > 0) {
			                	ProcessoParteAlcunhaDt alcunha = (ProcessoParteAlcunhaDt) ProcessoPartedt.getListaAlcunhaParte().get(Funcoes.StringToInt(posicaoLista));
			                	if (alcunha.getId().length() > 0){
			                		alcunha.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			                		alcunha.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
			            			ProcessoPartene.excluirAlcunha(alcunha);
			            		}
			                	ProcessoPartedt.getListaAlcunhaParte().remove(Funcoes.StringToInt(posicaoLista));
			                } else {
			                	if (request.getParameter("Alcunha").length() > 0){
			                		ProcessoParteAlcunhaDt alcunha = new ProcessoParteAlcunhaDt();
			                    	alcunha.setAlcunha(request.getParameter("Alcunha"));
			                    	ProcessoPartedt.addListaAlcunhaParte(alcunha);	
			                    	ProcessoPartedt.setAlcunha("null");
			                	}
			                }
			                stAcao = "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
			                break;

			            case 13: // Atualiza sinal particular da parte
			                if (posicaoLista != null && posicaoLista.length() > 0) {
			                	ProcessoParteSinalDt sinal = (ProcessoParteSinalDt) ProcessoPartedt.getListaSinalParte().get(Funcoes.StringToInt(posicaoLista));
			                	if (sinal.getId().length() > 0){
			                		sinal.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
			                		sinal.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
			                		ProcessoPartene.excluirSinal(sinal);
			            		}
			                	ProcessoPartedt.getListaSinalParte().remove(Funcoes.StringToInt(posicaoLista));
			                } else {
			                	if (request.getParameter("Sinal").length() > 0){
			                		ProcessoParteSinalDt sinal = new ProcessoParteSinalDt();
			                        sinal.setSinal(request.getParameter("Sinal"));
			                        ProcessoPartedt.addListaSinalParte(sinal);
			                        ProcessoPartedt.setSinal("null");
			                	}
			                }
			                stAcao = "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
			                break;
		                
				}
				break;
		}
		
		// Exibe ou não a opção de não informar o endereço
		if (!UsuarioSessao.getUsuarioDt().isAdvogado() && (ProcessoPartedt.getId_Endereco() == null || ProcessoPartedt.getId_Endereco().trim().length() == 0))					
			request.setAttribute("exibeparteEnderecoDesconhecido", true);
		else request.setAttribute("exibeparteEnderecoDesconhecido", false);

		request.setAttribute("ParteTipo", parteTipo);
		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("parteEnderecoDesconhecido", String.valueOf(ProcessoPartedt.isParteEnderecoDesconhecido()));
		request.getSession().setAttribute("Enderecodt", ProcessoPartedt.getEnderecoParte());
		request.getSession().setAttribute("ProcessoPartedt", ProcessoPartedt);
		request.getSession().setAttribute("processoDt", processoDt);
		request.getSession().setAttribute("ProcessoPartene", ProcessoPartene);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private boolean comparaHashPagina(String hashProcesso, String id_Processo, String hashParte, String id_ProcessoParte) {
		// TODO Auto-generated method stub
		// Compara o Hash do id_processo e id_processoParte da página com a sessão
		try {
			if (hashProcesso.equals(Funcoes.GeraHashMd5(id_Processo)) && hashParte.equals(Funcoes.GeraHashMd5(id_ProcessoParte))) {
				return true; 
			}else{
				return false;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}				
		
	}

	/** 
	 * Setando atributos ProcessoParteDt 
	 */
	protected void setDadosProcessoParte(HttpServletRequest request, ProcessoParteDt processoParteDt, ProcessoDt processoDt, UsuarioNe usuarioSessao) {

		if( processoParteDt != null && processoDt != null ) {
			processoParteDt.setId_Processo(processoDt.getId());
			processoParteDt.setNome(request.getParameter("Nome"));
			processoParteDt.setCpf(request.getParameter("Cpf"));
			processoParteDt.setCnpj(request.getParameter("Cnpj"));
			processoParteDt.setCulpado(request.getParameter("Culpado"));
			processoParteDt.setId_ProcessoParteTipo(request.getParameter("Id_ProcessoParteTipo"));
			processoParteDt.setProcessoParteTipo(request.getParameter("ProcessoParteTipo"));
			processoParteDt.setRg(request.getParameter("Rg"));
			processoParteDt.setId_RgOrgaoExpedidor(request.getParameter("Id_RgOrgaoExpedidor"));
			processoParteDt.setRgOrgaoExpedidor(request.getParameter("RgOrgaoExpedidor"));
			processoParteDt.setRgDataExpedicao(request.getParameter("RgDataExpedicao"));
			processoParteDt.setTituloEleitor(request.getParameter("TituloEleitor"));
			processoParteDt.setTituloEleitorZona(request.getParameter("TituloEleitorZona"));
			processoParteDt.setTituloEleitorSecao(request.getParameter("TituloEleitorSecao"));
			processoParteDt.setCtps(request.getParameter("Ctps"));
			processoParteDt.setCtpsSerie(request.getParameter("CtpsSerie"));
			processoParteDt.setId_CtpsUf(request.getParameter("Id_CtpsUf"));
			processoParteDt.setEstadoCtpsUf(request.getParameter("EstadoCtpsUf"));
			processoParteDt.setSiglaCtpsUf(request.getParameter("SiglaCtpsUf"));
			processoParteDt.setPis(request.getParameter("Pis"));
			processoParteDt.setSexo(request.getParameter("Sexo"));
			processoParteDt.setNomeMae(request.getParameter("NomeMae"));
			processoParteDt.setNomePai(request.getParameter("NomePai"));
			processoParteDt.setDataNascimento(request.getParameter("DataNascimento"));
			processoParteDt.setId_Naturalidade(request.getParameter("Id_Cidade"));
			processoParteDt.setCidadeNaturalidade(request.getParameter("Cidade"));
			processoParteDt.setId_EstadoCivil(request.getParameter("Id_EstadoCivil"));
			processoParteDt.setEstadoCivil(request.getParameter("EstadoCivil"));
			processoParteDt.setId_Profissao(request.getParameter("Id_Profissao"));
			processoParteDt.setProfissao(request.getParameter("Profissao"));
			processoParteDt.setTelefone(request.getParameter("Telefone"));
			processoParteDt.setCelular(request.getParameter("Celular"));
			processoParteDt.setEMail(request.getParameter("EMail"));
			processoParteDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
			processoParteDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
			processoParteDt.setId_EmpresaTipo(request.getParameter("Id_EmpresaTipo"));
			processoParteDt.setEmpresaTipo(request.getParameter("EmpresaTipo"));
			processoParteDt.setId_GovernoTipo(request.getParameter("Id_GovernoTipo"));
			processoParteDt.setGovernoTipo(request.getParameter("GovernoTipo"));
			processoParteDt.setAlcunha(request.getParameter("Alcunha"));
			processoParteDt.setSinal(request.getParameter("Sinal"));
			processoParteDt.setEscolaridade(request.getParameter("Escolaridade"));
			processoParteDt.setId_Escolaridade(request.getParameter("Id_Escolaridade"));
			processoParteDt.setRaca(request.getParameter("Raca"));
			processoParteDt.setId_Raca(request.getParameter("Id_Raca"));
			processoParteDt.setDataSentenca(request.getParameter("DataSentenca"));
			processoParteDt.setDataPronuncia(request.getParameter("DataPronuncia"));
			
			// Endereço da Parte
			processoParteDt.getEnderecoParte().setId(request.getParameter("Id_Endereco"));
			processoParteDt.getEnderecoParte().setLogradouro(request.getParameter("Logradouro"));
			processoParteDt.getEnderecoParte().setNumero(request.getParameter("Numero"));
			processoParteDt.getEnderecoParte().setComplemento(request.getParameter("Complemento"));
			processoParteDt.getEnderecoParte().setId_Bairro(request.getParameter("Id_Bairro"));
			processoParteDt.getEnderecoParte().setBairro(request.getParameter("Bairro"));
			processoParteDt.getEnderecoParte().setId_Cidade(request.getParameter("BairroId_Cidade"));
			processoParteDt.getEnderecoParte().setCidade(request.getParameter("BairroCidade"));
			processoParteDt.getEnderecoParte().setUf(request.getParameter("BairroUf"));
			processoParteDt.getEnderecoParte().setCep(request.getParameter("Cep"));
			processoParteDt.getEnderecoParte().setId_UsuarioLog(usuarioSessao.getId_Usuario());
			processoParteDt.getEnderecoParte().setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
	
			request.getSession().setAttribute("Enderecodt", processoParteDt.getEnderecoParte());
			request.getSession().setAttribute("ProcessoPartedt", processoParteDt);
		}
	}

	protected ProcessoParteDt buscaParte(HttpServletRequest request, int parteTipo, ProcessoParteNe processoParteNe) throws Exception{
		ProcessoParteDt ProcessoPartedt = null;
		String cpfCnpj = "", Rg = "", Ctps = "", TituloEleitor = "", Pis = "";

		if (request.getParameter("CpfCnpj") != null) cpfCnpj = request.getParameter("CpfCnpj").replaceAll("[/.-]", "");
		if (request.getParameter("Rg") != null) Rg = request.getParameter("Rg");
		if (request.getParameter("Ctps") != null) Ctps = request.getParameter("Ctps");
		if (request.getParameter("TituloEleitor") != null) TituloEleitor = request.getParameter("TituloEleitor");
		if (request.getParameter("Pis") != null) Pis = request.getParameter("Pis");

		ProcessoPartedt = new ProcessoParteDt();

		if ((cpfCnpj.length() > 0) || (Rg.length() > 0) || (Ctps.length() > 0) || (TituloEleitor.length() > 0) || (Pis.length() > 0)) {

			ProcessoPartedt = processoParteNe.consultarParte(cpfCnpj, Rg, Ctps, TituloEleitor, Pis);

			if (ProcessoPartedt.getNome().length() == 0) {
				if (cpfCnpj.length() == 11) ProcessoPartedt.setCpf(cpfCnpj);
				if (cpfCnpj.length() == 14) ProcessoPartedt.setCnpj(cpfCnpj);
				ProcessoPartedt.setRg(Rg);
				ProcessoPartedt.setCtps(Ctps);
				ProcessoPartedt.setTituloEleitor(TituloEleitor);
				ProcessoPartedt.setPis(Pis);
				request.setAttribute("MensagemOk", "Parte não encontrada. Efetue o cadastro.");
			}

		} else request.setAttribute("MensagemOk", "Efetue o cadastro da parte.");
		ProcessoPartedt.setProcessoParteTipoCodigo(String.valueOf(parteTipo));
		return ProcessoPartedt;
	}

	/**
	 * Método que obtém a parte escolhida pelo usuário a ser desabilitada
	 * ou restaurada de um processo.
	 * Seta id_usuario e ip_computador para armazenamento em log
	 */
	protected ProcessoParteDt getParteSelecionada(HttpServletRequest request, ProcessoDt processodt, String posicaoLista, String idParte, int parteTipo, UsuarioNe usuarioSessao) {
		ProcessoParteDt parteDt = null;
		if (posicaoLista.length() > 0) {
			parteDt = new ProcessoParteDt();
			int posicao = Funcoes.StringToInt(posicaoLista);

			if (parteTipo == ProcessoParteTipoDt.POLO_ATIVO_CODIGO) {
				parteDt = (ProcessoParteDt) processodt.getListaPolosAtivos().get(posicao);
			} else if (parteTipo == ProcessoParteTipoDt.POLO_PASSIVO_CODIGO) {
				parteDt = (ProcessoParteDt) processodt.getListaPolosPassivos().get(posicao);
			} else {
				List listaOutrasPartes = processodt.getListaOutrasPartes();
				for (int i = 0; i < listaOutrasPartes.size(); i++) {
					parteDt = (ProcessoParteDt)listaOutrasPartes.get(i);
					if(parteDt.getId().equals(idParte)) {
						break;
					}
				}
			}
			parteDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
			parteDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
		}
		return parteDt;
	}

	/**
	 * Método que verifica se determinada parte já foi inserida em um processo, seja como promovente ou promovida.
	 */
	protected boolean parteProcessoCadastrada(ProcessoDt processoDt, ProcessoParteDt processoPartedt) {
		
		//se a nova parte for PROMOVENTE, vai validar apenas a lista de promoventes.
		if (processoPartedt.getProcessoParteTipoCodigo().equals(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO))) {
			List listaPromoventes = processoDt.getListaPolosAtivos();
			if (listaPromoventes != null) {
				for (int i = 0; i < listaPromoventes.size(); i++) {
					ProcessoParteDt dt = (ProcessoParteDt) listaPromoventes.get(i);
					if (((dt.getCpf().length() > 0) && (dt.getCpf().equals(processoPartedt.getCpf()))) || ((dt.getCnpj().length() > 0) && (dt.getCnpj().equals(processoPartedt.getCnpj())))) {
						return true;
					}
				}
			}
		}
		//se a nova parte for PROMOVIDO, vai validar apenas a lista de promovidos.
		if (processoPartedt.getProcessoParteTipoCodigo().equals(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO))) {
			List listaPromovidos = processoDt.getListaPolosPassivos();
			if (listaPromovidos != null) {
				for (int i = 0; i < listaPromovidos.size(); i++) {
					ProcessoParteDt dt = (ProcessoParteDt) listaPromovidos.get(i);
					if (((dt.getCpf().length() > 0) && (dt.getCpf().equals(processoPartedt.getCpf()))) || ((dt.getCnpj().length() > 0) && (dt.getCnpj().equals(processoPartedt.getCnpj())))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	private String getStAcaoEditar(String processoTipoCodigo){
		if (processoTipoCodigo.equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))
				|| processoTipoCodigo.equals(String.valueOf(ProcessoTipoDt.EXECUCAO_DA_PENA))){
			return "/WEB-INF/jsptjgo/ProcessoParteExecucaoEditar.jsp";
		} else return "/WEB-INF/jsptjgo/ProcessoParteEditar.jsp";
	}

}
