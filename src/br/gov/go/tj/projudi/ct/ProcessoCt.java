package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoAssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoCriminalDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoNotaDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet para controlar as modificações de dados dos processos, valendo para os os processos cíveis, 
 * criminais e de segundo grau
 * 
 * @author msapaula
 *
 */
public class ProcessoCt extends ProcessoCtGen {

	private static final long serialVersionUID = -6841031865918102194L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDt processoDt;
		ProcessoNe Processone;

		String Mensagem = "";
		int passoEditar = -1;
		int paginaAnterior = 0;
		String posicaoLista = "";
		String stAcao = "";
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempBuscaId_Processo", "Id_Processo");
		request.setAttribute("tempBuscaProcessoNumero", "ProcessoNumero");
		request.setAttribute("tempRetorno", "Processo");

		Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null) Processone = new ProcessoNe();

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		
		if (processoDt == null) {
			redireciona(response, "Usuario?PaginaAtual=-10");
			return;
		}

		request.setAttribute("ProcessoArea", request.getParameter("ProcessoArea"));
		
		processoDt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		processoDt.setId_ProcessoTipo(request.getParameter("Id_ProcessoTipo"));
		processoDt.setProcessoTipoCodigo(request.getParameter("ProcessoTipoCodigo"));
		processoDt.setProcessoTipo(request.getParameter("ProcessoTipo"));
		processoDt.setId_ProcessoFase(request.getParameter("Id_ProcessoFase"));
		processoDt.setProcessoFase(request.getParameter("ProcessoFase"));
		processoDt.setId_ProcessoPrioridade(request.getParameter("Id_ProcessoPrioridade"));
		processoDt.setProcessoPrioridade(request.getParameter("ProcessoPrioridade"));
		processoDt.setId_Classificador(request.getParameter("Id_Classificador"));
		processoDt.setClassificador(request.getParameter("Classificador"));
		processoDt.setValor(request.getParameter("Valor"));
		processoDt.setId_Assunto(request.getParameter("Id_Assunto"));
		processoDt.setAssunto(request.getParameter("Assunto"));
		processoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		processoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		processoDt.setDataTransitoJulgado(request.getParameter("DataTransitoJulgado"));
		processoDt.setTcoNumero(request.getParameter("TcoNumero"));
		processoDt.setValorCondenacao(request.getParameter("ValorCondenacao"));
		
		if(request.getParameter("DataPrescricao") != null) {
			processoDt.setDataPrescricao(request.getParameter("DataPrescricao"));
		}
				
		if(request.getParameter("CustaTipo") != null) processoDt.setId_Custa_Tipo(request.getParameter("CustaTipo"));
		if (processoDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))){
			if(processoDt.getProcessoCriminalDt() == null)
				processoDt.setProcessoCriminalDt(new ProcessoCriminalDt());
			if(request.getParameter("DataPrisao") != null) processoDt.getProcessoCriminalDt().setDataPrisao(request.getParameter("DataPrisao"));
			if(request.getParameter("DataOferecimentoQueixa") != null)processoDt.getProcessoCriminalDt().setDataOferecimentoDenuncia(request.getParameter("DataOferecimentoQueixa"));
			if(request.getParameter("DataRecebimentoQueixa") != null)processoDt.getProcessoCriminalDt().setDataRecebimentoDenuncia(request.getParameter("DataRecebimentoQueixa"));
			if(request.getParameter("DataTransacaoPenal") != null)processoDt.getProcessoCriminalDt().setDataTransacaoPenal(request.getParameter("DataTransacaoPenal"));
			if(request.getParameter("DataSuspensaoPenal") != null)processoDt.getProcessoCriminalDt().setDataSuspensaoPenal(request.getParameter("DataSuspensaoPenal"));
			if(request.getParameter("DataFato") != null)processoDt.getProcessoCriminalDt().setDataFato(request.getParameter("DataFato"));
			if(request.getParameter("DataBaixa") != null)processoDt.getProcessoCriminalDt().setDataBaixa(request.getParameter("DataBaixa"));
			if(request.getParameter("ProcessoArquivamentoTipo") != null)processoDt.getProcessoCriminalDt().setProcessoArquivamentoTipo(request.getParameter("ProcessoArquivamentoTipo"));
			if(request.getParameter("Id_ProcessoArquivamentoTipo") != null)processoDt.getProcessoCriminalDt().setId_ProcessoArquivamentoTipo(request.getParameter("Id_ProcessoArquivamentoTipo"));
			processoDt.getProcessoCriminalDt().setId_UsuarioLog(processoDt.getId_UsuarioLog());
			processoDt.getProcessoCriminalDt().setIpComputadorLog(processoDt.getIpComputadorLog());
		}

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		posicaoLista = request.getParameter("posicaoLista");

		List listaAssuntosEditada = (List) request.getSession().getAttribute("ListaAssuntos"); //Lista dos assuntos após alteração
		if (listaAssuntosEditada == null) listaAssuntosEditada = new ArrayList();

		//Quando um assunto é selecionado já insere na lista de assuntos
		if (!processoDt.getId_Assunto().equals("") && paginaAnterior == (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			this.adicionarAssuntoProcesso(processoDt, listaAssuntosEditada, request);
		}

		//Processo de 2º grau terá tela de alteração de dados diferenciada
//		if (Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU) 
//			stAcao = "/WEB-INF/jsptjgo/ProcessoSegundoGrauAlterar.jsp";
//		else 
//			stAcao = "/WEB-INF/jsptjgo/Processo.jsp";
		
		stAcao = "/WEB-INF/jsptjgo/Processo.jsp";
		
		if (Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU) 
			request.setAttribute("tempPrograma", "Processo Segundo Grau");
		else 
			request.setAttribute("tempPrograma", "Processo Primeiro Grau");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("posicaoLista", posicaoLista);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("Id_AreaDistribuicao", processoDt.getId_AreaDistribuicao());
		request.setAttribute("Id_Serventia", processoDt.getId_Serventia());
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			
			case Configuracao.Localizar:
				break;
				//o imprimir será usando para salvar as notas e motrar as mesma na tela
			case Configuracao.Imprimir:
				String passo = request.getParameter("Passo");
				String id_usuarioServentia = UsuarioSessao.getId_UsuarioServentia();
				if (UsuarioSessao.isAssessorMagistrado()){
					id_usuarioServentia = UsuarioSessao.getId_UsuarioServentiaChefe();
				}
				//consultar notas do processo
				if ("1".equals(passo)){
					String id_serv= null;
					if ( UsuarioSessao.isUsuarioInterno()){
						id_serv = UsuarioSessao.getId_Serventia();
					}					
					String notas = Processone.consultarNotas(processoDt.getId(),id_usuarioServentia, id_serv);
					enviarJSON(response, notas);
					return;
				//salvar notas	
				}else if ("2".equals(passo)){
					String id_nota = request.getParameter("id_nota");
					if (id_nota!=null && id_nota.equals("undefined")){
						id_nota = "";
					}
					String nota = request.getParameter("nota");
					String id_serv_nota = request.getParameter("id_serv_nota");
					
					if(nota==null || id_nota == null){
						return;
					}
										
					ProcessoNotaDt notaDt = new ProcessoNotaDt();
					
					notaDt.setId(id_nota);
					if (id_serv_nota!=null && !id_serv_nota.equals("0")){
						notaDt.setId_Serventia(processoDt.getId_Serventia());
					}
					notaDt.setId_Processo(processoDt.getId());
					notaDt.setId_UsuarioServentia(id_usuarioServentia);
					notaDt.setProcessoNota(nota);
					notaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					notaDt.setIpComputadorLog(this.getIpCliente(request));
					notaDt.setCodigoTemp(request.getParameter("isPrivada"));
					
					Processone.salvarNota(notaDt);
					enviarJSON(response, "{\"id\":\"" + notaDt.getId() + "\"}");
					return;
				//excuir nota
				}else if ("3".equals(passo)){
					String id_nota = request.getParameter("id_nota");
					String isPrivada = request.getParameter("isPrivada");
					if (id_nota!=null && id_nota.length()>0){
						Processone.excluirNota(id_nota,id_usuarioServentia, isPrivada, UsuarioSessao.getId_Serventia() );
					}
					return;
				}
				//se chegar nesse ponto houve algum problema na chamada das anotações
				throw new MensagemException("Ocorreu algum problema na atualização das Anotações. Favor informar o Gerenciamento do Sistema");
				
			case Configuracao.LocalizarDWR:
			
			//Prepara tela para alteração de dados do processo
			case Configuracao.Novo:
				if (processoDt != null) {
					//Verifica se usuário pode modificar dados
					Mensagem = Processone.podeModificarDados(processoDt, UsuarioSessao.getUsuarioDt());
					if (Mensagem.length() > 0) {
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
						return;
					}
					
					//Se é usuário do segundo grau tentando alterar dados do processo (do 1 grau), os dados do recurso estarão carregados no dt
					//e o AlterarProcesso estará preenchido. Nesse caso, segue usando este ct e carrega a tela de alteração de dados do processo.
					if (processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0 && request.getParameter("AlterarProcesso") != null && request.getParameter("AlterarProcesso").equals("1")) {
						//Joga lista de assuntos atuais na sessão
						listaAssuntosEditada = new ArrayList();
						listaAssuntosEditada.addAll(processoDt.getListaAssuntos());
						//Setando o atributo AlterarProcesso na sessão para controle futuro na consulta de classe, assunto, etc.
						request.getSession().setAttribute("AlterarProcesso", request.getParameter("AlterarProcesso"));
						break;
					}

					//Se é recurso inominado, encaminha para outra servlet
					if (processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) {
						redireciona(response, "Recurso?PaginaAtual=" + Configuracao.Novo + "&Id_Recurso=" + processoDt.getId_Recurso());
						return;
					}
					
					//Se for Processo físico para cálculo de liquidação de pna, encaminha para outro servlet
					if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
						redireciona(response, "ProcessoExecucao?PaginaAtual=" + Configuracao.Editar + "&PassoEditar=17&ModificaDados=true");
						return;
					}
					
					//Joga lista de assuntos atuais na sessão
					listaAssuntosEditada = new ArrayList();
					listaAssuntosEditada.addAll(processoDt.getListaAssuntos());
				}
				break;

			case Configuracao.Salvar:
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				if (request.getParameter("SegredoJustica") == null) processoDt.setSegredoJustica("false");
				else processoDt.setSegredoJustica(request.getParameter("SegredoJustica"));
				
				if(processoDt.isCriminal()) {
					if (request.getParameter("DeclaracaoInconsistencias") == null) {
						processoDt.getProcessoCriminalDt().setIdUsuCertificaInconsistencias(null);
					} else {
						processoDt.getProcessoCriminalDt().setIdUsuCertificaInconsistencias(UsuarioSessao.getId_Usuario());
					}
				}
				
				if (request.getParameter("EfeitoSuspensivo") == null) processoDt.setEfeitoSuspensivo("false");
				else processoDt.setEfeitoSuspensivo(request.getParameter("EfeitoSuspensivo"));
				
				if (Funcoes.StringToInt(processoDt.getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU){
					if (request.getParameter("Julgado2Grau") == null) processoDt.setJulgado2Grau("false");
					else processoDt.setJulgado2Grau(request.getParameter("Julgado2Grau"));
				}
				if (!processoDt.getAreaCodigo().trim().equalsIgnoreCase(String.valueOf(AreaDt.CRIMINAL))){
					if (request.getParameter("Penhora") == null) processoDt.setPenhora("false");
					else processoDt.setPenhora(request.getParameter("Penhora"));
				} else {
					if (request.getParameter("ReuPreso") == null) processoDt.getProcessoCriminalDt().setReuPreso("false");
					else processoDt.getProcessoCriminalDt().setReuPreso(request.getParameter("ReuPreso"));
				}
				if(!request.getParameter("localizador").equals("") &&  request.getParameter("localizador") != null){
					processoDt.setLocalizador(request.getParameter("localizador"));	
				}

				if (request.getParameter("Digital100") == null) processoDt.set100Digital(false);
				else processoDt.set100Digital(request.getParameter("Digital100").equals("true")?true:false);

				break;

			//Remover assuntos de processo
			case Configuracao.Editar:
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				removerAssuntoProcesso(listaAssuntosEditada, posicaoLista);
				break;

			//Salvar alterações das Características do Processo
			case Configuracao.SalvarResultado:
				// Salva dados do processo
				if (listaAssuntosEditada != null && listaAssuntosEditada.size() > 0) {
					List listaAssuntosAnterior = processoDt.getListaAssuntos();
					processoDt.setListaAssuntos(listaAssuntosEditada);

					Mensagem = Processone.verificarDadosAlteracao(processoDt);
					if (Mensagem.length() == 0) {
						processoDt.setId_Area(request.getParameter("ProcessoArea"));
						request.removeAttribute("ProcessoArea");

						Processone.alterarDadosProcesso(processoDt, listaAssuntosAnterior, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
						listaAssuntosEditada = null;
						
						//Ao concluir a alteração do processo, limpa o atributo AlterarProcesso da sessão para evitar possíveis impactos
						request.getSession().removeAttribute("AlterarProcesso");

						//Volta para tela do processo
						request.getSession().removeAttribute("processoDt");
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Dados atualizados com sucesso.");
						return;
					} else request.setAttribute("MensagemErro", Mensagem);
				} else request.setAttribute("MensagemErro", "Insira ao menos um Assunto para o processo.");
				break;

			//Alterar Classificador de Um Processo
			case Configuracao.Curinga6:
				if (processoDt != null) {
					stAcao = "/WEB-INF/jsptjgo/ClassificadorAlterar.jsp";
					String id_NovoClassificador = "", novoClassificador = processoDt.getClassificador();
					if (request.getParameter("Id_NovoClassificador") != null && !request.getParameter("Id_NovoClassificador").equalsIgnoreCase("null")) {
						id_NovoClassificador = request.getParameter("Id_NovoClassificador");
					}
					if (request.getParameter("NovoClassificador") != null) {
						novoClassificador = request.getParameter("NovoClassificador");
					}
					request.setAttribute("Id_NovoClassificador", id_NovoClassificador);
					request.setAttribute("NovoClassificador", novoClassificador);

					if (passoEditar == 2) {
						if (!processoDt.getId_Classificador().equalsIgnoreCase(id_NovoClassificador)) {
							Processone.alterarClassificadorProcesso(processoDt.getId(), processoDt.getId_Classificador(), id_NovoClassificador, new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()));
							//Volta para tela do processo
							request.getSession().removeAttribute("processoDt");
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Classificador Atualizado com Sucesso.");
							return;
						} else request.setAttribute("MensagemErro", "Altere o Classificador primeiramente.");
					}
				}
				break;

			//Listar possíveis preventos no processo
			case Configuracao.Curinga7:
				//Consulta dados completos da serventia para consultar preventos
				ServentiaDt serventiaDt = Processone.consultarIdServentia(processoDt.getId_Serventia());
				request.setAttribute("preventos", Processone.consultarConexaoProcessoOriginario(processoDt, serventiaDt.getId_Comarca(), serventiaDt.getId_ServentiaSubtipo()));
				stAcao = "/WEB-INF/jsptjgo/PrevencoesProcesso.jsp";
				break;
			
			//Excluir a Prioridade do Processo
			case Configuracao.Curinga8: {
				//Quando o passoEditar for igual a -3 significa que já foi confirmada a exclusão da prioridade
				if(passoEditar == -3){
					processoDt.setProcessoPrioridade("Normal");
					processoDt.setProcessoPrioridadeCodigo("0");
					processoDt.setId_ProcessoPrioridade("1");
					Processone.salvar(processoDt);
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Prioridade removida com sucesso.");
				} else {
					stAcao = "/WEB-INF/jsptjgo/ConfirmarRetiradaPrioridadeProcesso.jsp";
				}
				break;
			}
			
			//Arquiva e Desarquiva Processo do tipo Cálculo de Liquidação de Pena (processo físico tramitando no SPG)
			case Configuracao.Curinga9:
				int opc = 0;
				String tipoAcao = "";
				String complemento = "";
				
				if (request.getParameter("opc") != null && !request.getParameter("opc").equalsIgnoreCase("null") && !request.getParameter("opc").equals("")) 
					opc = Funcoes.StringToInt(request.getParameter("opc")); 
				
				if (request.getParameter("tipoAcao") != null && !request.getParameter("tipoAcao").equalsIgnoreCase("null") && !request.getParameter("tipoAcao").equals("")) 
					tipoAcao = request.getParameter("tipoAcao"); 
				
				switch(opc){
					case 0:
						if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
							if (request.getParameter("Arquivar") != null && Funcoes.StringToBoolean(request.getParameter("Arquivar").toString())){
								tipoAcao = "ARQUIVAR";
								opc = 1; //arquivar
							}
							else if (request.getParameter("Desarquivar") != null && Funcoes.StringToBoolean(request.getParameter("Desarquivar").toString())){
								tipoAcao = "DESARQUIVAR";
								opc = 2; //desarquivar
							}
							stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoArquivarDesarquivar.jsp";
							request.setAttribute("MensagemOk", "Clique no botão \"Confirmar\" para " + tipoAcao + " o processo.");
							request.setAttribute("opc", opc);
							request.setAttribute("tipoAcao", tipoAcao);
							
						} else {
							Mensagem = "Não é possível realizar esta ação! (Motivo: Ação realizada apenas para os processo da classe \"Cálculo de Liquidação de Pena\".)";
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
						}
						break;
						
					case 1: //arquivar
						complemento = "(";
						if (request.getParameter("radioArquivar") != null) {
				        	if (request.getParameter("radioArquivar").equalsIgnoreCase("Cálculo_duplicado")
				            		|| request.getParameter("radioArquivar").equalsIgnoreCase("Transferência_de_Comarca")
				            		|| request.getParameter("radioArquivar").equalsIgnoreCase("Transferência_de_Estado")
				            		|| request.getParameter("radioArquivar").equalsIgnoreCase("Morte_do_Agente")
				            		|| request.getParameter("radioArquivar").equalsIgnoreCase("Extinção")){
				                complemento += request.getParameter("radioArquivar");
				                complemento = complemento.replace("_", " ");
				                if (request.getParameter("Obs").length() > 0){
				                	complemento += ": ";
				                }
				            }
				        }
						if (request.getParameter("radioArquivar") == null || request.getParameter("radioArquivar").length() == 0){
			        		request.setAttribute("MensagemErro", "Informe o motivo do arquivamento!");
			        		request.setAttribute("opc", opc);
							request.setAttribute("tipoAcao", tipoAcao);
							stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoArquivarDesarquivar.jsp";
						} else {
							complemento += request.getParameter("Obs") + ")";
							Mensagem = Processone.arquivarProcessoCalculo(processoDt, UsuarioSessao.getUsuarioDt(), complemento);
							if (Mensagem.length() > 0){
								redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
							} else redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Processo ARQUIVADO com sucesso!");
			        	}
						
						break;
						
					case 2: //desarquivar
						complemento = "(";
						if (request.getParameter("radioArquivar") != null) {
				        	if (request.getParameter("radioArquivar").equalsIgnoreCase("Cálculo_duplicado")
				            		|| request.getParameter("radioArquivar").equalsIgnoreCase("Transferência_de_Comarca")
				            		|| request.getParameter("radioArquivar").equalsIgnoreCase("Transferência_de_Estado")
				            		|| request.getParameter("radioArquivar").equalsIgnoreCase("Morte_do_Agente")
				            		|| request.getParameter("radioArquivar").equalsIgnoreCase("Extinção")){
				                complemento += request.getParameter("radioArquivar");
				                complemento = complemento.replace("_", " ");
				                if (request.getParameter("Obs").length() > 0){
				                	complemento += ": ";
				                }
				            }
				        }
						
						if (request.getParameter("radioArquivar") == null || request.getParameter("radioArquivar").length() == 0){
							request.setAttribute("MensagemErro", "Informe o motivo do desarquivamento!");
			        		request.setAttribute("opc", opc);
							request.setAttribute("tipoAcao", tipoAcao);
							stAcao = "/WEB-INF/jsptjgo/ProcessoExecucaoArquivarDesarquivar.jsp";
						} else {
							complemento += request.getParameter("Obs") + ")";
							Mensagem = Processone.desarquivarProcessoCalculo(processoDt, UsuarioSessao.getUsuarioDt(), complemento);
							if (Mensagem.length() > 0){
								redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
							} else redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=Processo DESARQUIVADO com sucesso!");
			        	}
						break;
				}
				break;
				
			// Consultar tipos de processo
			case (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classe"};
					String[] lisDescricao = {"Classe","Código","Código CNJ"};
					String[] camposHidden = {"ProcessoTipoCodigo","desc2"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ProcessoTipo");
					request.setAttribute("tempBuscaDescricao", "ProcessoTipo");
					request.setAttribute("tempBuscaPrograma", "Classe");
					request.setAttribute("tempRetorno", "Processo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("Id_Serventia", processoDt.getId_Serventia());
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					passoEditar = 0;
					break;
				}else{
					String stTemp = "";
					
					//Esse atributo AlterarProcesso está na sessão exclusivamente para atender a necessidade de alterar dados do processo enquanto o recurso
					//está ativo no 2º grau. Ao finalizar a alteração, o atributo é removido da sessão.
					if(request.getSession().getAttribute("AlterarProcesso") != null && request.getSession().getAttribute("AlterarProcesso").equals("1")) {
						stTemp = Processone.consultarProcessoTipoServentiaJSON(stNomeBusca1, processoDt.getRecursoDt().getId_ServentiaOrigem(),  UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
					} else {
						stTemp = Processone.consultarProcessoTipoServentiaJSON(stNomeBusca1, processoDt.getId_Serventia(),  UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
					}
					enviarJSON(response, stTemp);
					return;
				}

			// Consulta Assuntos
			case (AssuntoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Assunto"};
					String[] lisDescricao = {"Assunto","Pai","Disp. Legal"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Assunto");
					request.setAttribute("tempBuscaDescricao","Assunto");
					request.setAttribute("tempBuscaPrograma","Assunto");			
					request.setAttribute("tempRetorno","Processo");		
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
					if (processoDt.getId_Serventia().length() > 0) {
						//Esse atributo AlterarProcesso está na sessão exclusivamente para atender a necessidade de alterar dados do processo enquanto o recurso
						//está ativo no 2º grau. Ao finalizar a alteração, o atributo é removido da sessão.
						if(request.getSession().getAttribute("AlterarProcesso") != null && request.getSession().getAttribute("AlterarProcesso").equals("1")) {
							stTemp = Processone.consultarAssuntosServentiaJSON(stNomeBusca1, processoDt.getRecursoDt().getId_ServentiaOrigem(), PosicaoPaginaAtual);
						} else {
							stTemp = Processone.consultarAssuntosServentiaJSON(stNomeBusca1, processoDt.getId_Serventia(), PosicaoPaginaAtual);
						}
						enviarJSON(response, stTemp);
						return;								
					}else {
						request.setAttribute("MensagemErro", "Selecione primeiramente a Área de Distribuição.");
					}
				}
			// Consultar fases de processo
			case (ProcessoFaseDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				//Quando o processo estiver na fase Recurso não deve permitir que o usuário altere
				boolean temRecursoAtivo = new ProcessoNe().temRecursoAtivo(processoDt.getId());
				if (Funcoes.StringToInt(processoDt.getProcessoFaseCodigo(),-1) == ProcessoFaseDt.RECURSO && temRecursoAtivo) {
					request.setAttribute("MensagemErro", "Fase Processual não pode ser modificada.");
					break;
				} else if (request.getParameter("Passo")==null) {
					String[] lisNomeBusca = {"ProcessoFase"};
					String[] lisDescricao = {"ProcessoFase"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcessoFase");
					request.setAttribute("tempBuscaDescricao","ProcessoFase");
					request.setAttribute("tempBuscaPrograma","ProcessoFase");			
					request.setAttribute("tempRetorno","Processo");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoFaseDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else{
					String stTemp="";
					stTemp = Processone.consultarDescricaoProcessoFaseJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
				
			// Consultar prioridades de processo
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
					request.setAttribute("tempRetorno","Processo");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ProcessoPrioridadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else{
					String stTemp="";
					stTemp = Processone.consultarDescricaoProcessoPrioridadeJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}

			// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Classificador"};
					String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					if (passoEditar == 1) {
						request.setAttribute("tempBuscaId", "Id_NovoClassificador");
						request.setAttribute("tempBuscaDescricao", "NovoClassificador");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					} else {
						request.setAttribute("tempBuscaId", "Id_Classificador");
						request.setAttribute("tempBuscaDescricao", "Classificador");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					}
					request.setAttribute("tempBuscaPrograma","Classificador");			
					request.setAttribute("tempRetorno","Processo");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					break;
				} else{
					String stTemp="";
					
					//Esse atributo AlterarProcesso está na sessão exclusivamente para atender a necessidade de alterar dados do processo enquanto o recurso
					//está ativo no 2º grau. Ao finalizar a alteração, o atributo é removido da sessão.
					if(request.getSession().getAttribute("AlterarProcesso") != null && request.getSession().getAttribute("AlterarProcesso").equals("1")) {
						stTemp = Processone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, processoDt.getRecursoDt().getId_ServentiaOrigem());
					} else {
						stTemp = Processone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, processoDt.getId_Serventia());
					}
					enviarJSON(response, stTemp);
					return;								
				}
		}

		request.setAttribute("PassoEditar", passoEditar);
		request.getSession().setAttribute("ListaAssuntos", listaAssuntosEditada);
		request.getSession().setAttribute("processoDt", processoDt);
		request.getSession().setAttribute("Processone", Processone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Método responsável em remover um assunto de um processo
	 */
	protected void removerAssuntoProcesso(List listaEditada, String posicaoLista) {
		if (posicaoLista != null && posicaoLista.length() > 0) {
			listaEditada.remove(Funcoes.StringToInt(posicaoLista));
		}
	}

//	/**
//	 * Consulta assuntos cadastrados
//	 */
//	private boolean consultarAssunto(HttpServletRequest request, String tempNomeBusca, String posicaoPaginaAtual, int paginaatual, ProcessoNe processone, ProcessoDt processoDt){
//		boolean boRetorno = false;
//		List tempList = null;
//		
//		//if (processoDt.getId_AreaDistribuicao().length() > 0) {
//			tempList = processone.consultarDescricaoAssunto(tempNomeBusca, processoDt.getId_Serventia(), posicaoPaginaAtual);
//
//			if (tempList.size() > 0) {
//				request.setAttribute("ListaAssunto", tempList);
//				request.setAttribute("PaginaAtual", paginaatual);
//				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
//				request.setAttribute("QuantidadePaginas", processone.getQuantidadePaginas());
//				request.setAttribute("tempBuscaId_Assunto", "Id_Assunto");
//				request.setAttribute("tempBuscaAssunto", "Assunto");
//				boRetorno = true;
//			} else request.setAttribute("MensagemErro", "Nenhum Assunto encontrado.");
//		//}
//		
//		return boRetorno;
//	}
	
	/**
	 * Método responsável em adicionar assuntos a um processo
	 */
	protected void adicionarAssuntoProcesso(ProcessoDt processoDt, List listaEditada, HttpServletRequest request) {
		if (processoDt.getId_Assunto().length() > 0) {
			ProcessoAssuntoDt processoAssuntoDt = new ProcessoAssuntoDt();
			processoAssuntoDt.setId_Assunto(processoDt.getId_Assunto());
			processoAssuntoDt.setAssunto(processoDt.getAssunto());
			listaEditada.add(processoAssuntoDt);

			processoDt.setId_Assunto("null");
			processoDt.setAssunto("null");

		} else request.setAttribute("MensagemErro", "Selecione um Assunto para ser adicionado.");
	}

}
