package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AdvogadoDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.ProcessoParteAdvogadoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Servlet para controlar a habilitação de advogados em processos
 * 
 * @author msapaula
 */

public class ProcessoParteAdvogadoCt extends ProcessoParteAdvogadoCtGen {

	private static final long serialVersionUID = -1221103240794622263L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteAdvogadoDt ProcessoParteAdvogadodt;
		ProcessoDt processoDt = null;
		ProcessoParteAdvogadoNe ProcessoParteAdvogadone;

		List tempList = null;
		String Mensagem = "";
		String stId = "";
		String posicaoLista = "";
		String[] partesSelecionadas;
		String Id_Serventia = "";
		String Serventia = "";
		int tempFluxo1 = 0;
		String stAcao = "/WEB-INF/jsptjgo/ProcessoParteAdvogado.jsp";
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma", "Habilitação Advogado");
		request.setAttribute("tempRetorno", "ProcessoParteAdvogado");

		ProcessoParteAdvogadone = (ProcessoParteAdvogadoNe) request.getSession().getAttribute("ProcessoParteAdvogadone");
		if (ProcessoParteAdvogadone == null) ProcessoParteAdvogadone = new ProcessoParteAdvogadoNe();

		ProcessoParteAdvogadodt = (ProcessoParteAdvogadoDt) request.getSession().getAttribute("ProcessoParteAdvogadodt");
		if (ProcessoParteAdvogadodt == null) ProcessoParteAdvogadodt = new ProcessoParteAdvogadoDt();
		
		//procurador que será substituido
		if (request.getParameter("usuariosServentiaAdvogado") != null && !request.getParameter("usuariosServentiaAdvogado").equals("")) {
			ProcessoParteAdvogadodt.setId_UsuarioServentiaAdvogado(request.getParameter("usuariosServentiaAdvogado").toString());
		} 
		
		// Obtem o Id e a descrição da Serventia selecionada
		if (request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").trim().equalsIgnoreCase("")) {
			Id_Serventia = request.getParameter("Id_Serventia");
			tempFluxo1 = 2;
		}
		if (request.getParameter("Serventia") != null && !request.getParameter("Serventia").trim().equalsIgnoreCase("")) 
			Serventia = request.getParameter("Serventia");
		
		if (request.getParameter("tempFluxo1") != null)
			tempFluxo1 = Funcoes.StringToInt(request.getParameter("tempFluxo1"));
		
		// Captura processo na sessão
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

		ProcessoParteAdvogadodt.setId_UsuarioServentiaAdvogado(request.getParameter("Id_Advogado"));
		ProcessoParteAdvogadodt.setNomeAdvogado(request.getParameter("Advogado"));
		ProcessoParteAdvogadodt.setOabNumero(request.getParameter("OabNumero"));
		ProcessoParteAdvogadodt.setOabComplemento(request.getParameter("OabComplemento"));
		ProcessoParteAdvogadodt.setAdvogadoTipo(request.getParameter("AdvogadoTipo"));
		//ProcessoParteAdvogadodt.setId_OabUf(request.getParameter("Id_OabUf"));
		ProcessoParteAdvogadodt.setEstadoOabUf(request.getParameter("EstadoOab"));
		ProcessoParteAdvogadodt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		ProcessoParteAdvogadodt.setPrincipal(request.getParameter("Principal"));
		ProcessoParteAdvogadodt.setId_Processo(processoDt.getId_Processo());
		ProcessoParteAdvogadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteAdvogadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		ProcessoParteAdvogadodt.setRecebeIntimacao(new Boolean(request.getParameter("RecebeIntimacao")));
		ProcessoParteAdvogadodt.setDativo(request.getParameter("Dativo"));

		//posicaoLista será passado no caso de Desabilitação ou Habilitação como Advogado Principal
		if (request.getParameter("posicaoLista") != null) posicaoLista = request.getParameter("posicaoLista");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			//Localiza advogados de um processo e redireciona para tela de habilitação de advogados
			case Configuracao.Novo:
				ProcessoParteAdvogadodt.limpar();
				String id_Processo = request.getParameter("Id_Processo");
				if (id_Processo == null) id_Processo = processoDt.getId_Processo();
				if (id_Processo != null) {
					if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + "Não é possível executar essa ação. Motivo: Processo físico!");
					} else {
						if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ADVOGADO) {
							//Caso o usuário seja advogado, a consulta será diferenciada para possibilitar definir advogado principal
							tempList = ProcessoParteAdvogadone.consultarAdvogadosProcessoParte(id_Processo, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
							processoDt.setListaAdvogados(tempList);
							stAcao = "/WEB-INF/jsptjgo/HabilitaAdvogadoPrincipal.jsp";
							request.setAttribute("PaginaAtual", Configuracao.Localizar);
							break;
						} else {
							//Verifica se usuário pode habilitar advogados
							Mensagem = ProcessoParteAdvogadone.podeHabilitarAdvogado(processoDt, UsuarioSessao.getUsuarioDt());
							if (Mensagem.length() == 0) {
								tempList = ProcessoParteAdvogadone.consultarAdvogadosProcesso(id_Processo);
								ProcessoParteAdvogadodt.setEstadoOabUf("GO");
							}
							else {
								//No caso de não poder habilitar, redireciona para tela de visualização apenas
								this.executar(request, response, UsuarioSessao, Configuracao.Curinga7, tempNomeBusca, PosicaoPaginaAtual);
								return;
							}
						}
						processoDt.setListaAdvogados(tempList);
					}
				} else request.setAttribute("MensagemErro", "É necessário selecionar um processo.");
				
				request.getSession().removeAttribute("ListaAdvogadosHabilitacao");
				break;
			
			// Consultar as serventias que utilizam o mesmo tipo da Serventia logada 
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Tipo","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");
					request.setAttribute("tempRetorno", "ProcessoParteAdvogado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					
					if (tempFluxo1 == 3) {							
						stTemp = ProcessoParteAdvogadone.consultarServentiasJuridicasJSON(stNomeBusca1, PosicaoPaginaAtual);
					} else {							
						stTemp = ProcessoParteAdvogadone.consultarServentiasPromotoriaJSON(stNomeBusca1, PosicaoPaginaAtual);
					}
				
					enviarJSON(response, stTemp);
					
					
					return;
				}
				break;
			
			case Configuracao.Localizar:
				Mensagem = ProcessoParteAdvogadone.verificarAdvogadoPromotorHabilitacao(tempFluxo1,ProcessoParteAdvogadodt, Id_Serventia);
				
				if(tempFluxo1 == 1){
					
					//limpa variaveis da serventia promotor
					Id_Serventia = ""; Serventia = "";
					
					if (Mensagem.length() == 0) {
						tempList = UsuarioSessao.consultarAdvogadoOab(ProcessoParteAdvogadodt.getOabNumero(), ProcessoParteAdvogadodt.getOabComplemento(), ProcessoParteAdvogadodt.getEstadoOabUf(), ProcessoParteAdvogadodt.getAdvogadoTipo());
						if (tempList != null && tempList.size() > 0)
							request.getSession().setAttribute("ListaAdvogadosHabilitacao", tempList);
						else 
							request.setAttribute("MensagemErro", "Nenhum advogado foi encontrado.");
					} else 
						request.setAttribute("MensagemErro", Mensagem);
				
				} else if (tempFluxo1 == 2){
					
					//se o usuário da sessão for um procurador (advogado público) , não deve limpar o ProcessoParteAdvogado por conta do funcionamento da tela
					if(request.getSession().getAttribute("solicitacaoProcurador") == null || 
							!(Boolean)request.getSession().getAttribute("solicitacaoProcurador")){
						ProcessoParteAdvogadodt.limpar();
						ProcessoParteAdvogadodt.setEstadoOabUf("GO");
					}
					
					if (Mensagem.length() == 0) {
						ProcessoParteAdvogadodt.setRecebeIntimacao(true);
						tempList = UsuarioSessao.consultarPromotorSubstitutoProcessual(Id_Serventia);
						if (tempList != null && tempList.size() > 0)
							request.getSession().setAttribute("ListaAdvogadosHabilitacao", tempList);
						else 
							request.setAttribute("MensagemErro", "Nenhum promotor foi encontrado.");
					} else 
						request.setAttribute("MensagemErro", Mensagem);
				} else if (tempFluxo1 == 3){
					
					if (Mensagem.length() == 0) {
						ProcessoParteAdvogadodt.setRecebeIntimacao(true);
						tempList = UsuarioSessao.consultarProcuradorJuridico(Id_Serventia);
						if (tempList != null && tempList.size() > 0) {
							request.getSession().setAttribute("ListaAdvogadosHabilitacao", tempList);
						} else{ 
							request.getSession().setAttribute("ListaAdvogadosHabilitacao", null);
							request.setAttribute("MensagemErro", "Nenhum procurador foi encontrado.");
						}
					} else 
						request.setAttribute("MensagemErro", Mensagem);
				}
				
				break;

			// Confirmação de gravação
			case Configuracao.Salvar:
				// Captura partes selecionadas
				partesSelecionadas = request.getParameterValues("ProcessoParteAdvogado");
				request.setAttribute("ListaAdvogadoParte", partesSelecionadas);
				
				//Valida dados digitados
				Mensagem = ProcessoParteAdvogadone.verificarAdvogadoHabilitacao(ProcessoParteAdvogadodt, partesSelecionadas);
				
				//Ao tentar cadastrar um advogado é preciso verificar se o cadastro
				//está sendo feito em duplicidade.
				if(Mensagem.length() == 0){
					for (int i = 0; i < partesSelecionadas.length; i++) {
						List listaAdvogados = ProcessoParteAdvogadone.consultarAdvogadosParte(partesSelecionadas[0]);
							for (int j = 0; j < listaAdvogados.size(); j++) {
								ProcessoParteAdvogadoDt advogadoCadastrado = (ProcessoParteAdvogadoDt) listaAdvogados.get(j);
								if(advogadoCadastrado.getDataSaida().equals("") 
										&& advogadoCadastrado.getId_UsuarioServentiaAdvogado().equals(ProcessoParteAdvogadodt.getId_UsuarioServentiaAdvogado())) {
									Mensagem = "O advogado já encontra-se cadastrado e habilitado para a parte .";
									break;
								}
							}
					}
				}
				
				//Se tentar cadastrar um advogado que não deve receber intimação,
				//é preciso validar se a parte já possui advogado que recebe intimação, pois 
				//ao menos um deve possuir.
				if(Mensagem.length() == 0 && !ProcessoParteAdvogadodt.getRecebeIntimacao()){
					for (int i = 0; i < partesSelecionadas.length; i++) {
						List listaAdvogados = ProcessoParteAdvogadone.consultarAdvogadosParte(partesSelecionadas[0]);
						if(listaAdvogados.isEmpty()){
							ProcessoParteNe processoParteNe = new ProcessoParteNe();
							ProcessoParteDt processoParteDt = processoParteNe.consultarId(partesSelecionadas[i]);
							Mensagem = "A parte de nome: "+ processoParteDt.getNome().toUpperCase() + " deve possuir pelo menos um advogado autorizado a receber intimação.";
						}
						
					}
				}
				
				if (Mensagem.length() > 0)
					request.setAttribute("MensagemErro", Mensagem);
				
//				if (Mensagem.length() == 0) {
//					UsuarioDt advogadoDt = UsuarioSessao.consultarAdvogadoOab(ProcessoParteAdvogadodt.getOabNumero(), ProcessoParteAdvogadodt.getOabComplemento(), ProcessoParteAdvogadodt.getEstadoOabUf(), ProcessoParteAdvogadodt.getAdvogadoTipo());
//					//Se encontrou advogado
//					if (advogadoDt != null) {
//						ProcessoParteAdvogadodt.setId_UsuarioServentiaAdvogado(advogadoDt.getId_UsuarioServentia());
//						request.setAttribute("Mensagem", "Clique para confirmar a habilitação do advogado " + advogadoDt.getNome() + " à(s) parte(s) selecionada(s)");
//					} else request.setAttribute("MensagemErro", "Nenhum Advogado encontrado com essa OAB.");
//
//				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			//Adiciona um novo Advogado ao processo
			case Configuracao.SalvarResultado:
				// Captura lista de partes selecionadas
				partesSelecionadas = request.getParameterValues("ProcessoParteAdvogado");

				Mensagem = ProcessoParteAdvogadone.verificarAdvogadoHabilitacao(ProcessoParteAdvogadodt, partesSelecionadas);
				// Se todos dados obrigatórios foram preenchidos, salva os dados
				if (Mensagem.length() == 0) {
					ProcessoParteAdvogadone.salvarHabilitacaoAdvogado(ProcessoParteAdvogadodt, partesSelecionadas);
					request.setAttribute("MensagemOk", "Habilitação de Advogado/Promotor realizada com sucesso.");

					// Atualiza lista de advogados cadastrados
					processoDt.setListaAdvogados(ProcessoParteAdvogadone.consultarAdvogadosProcesso(processoDt.getId_Processo()));
					
					request.getSession().removeAttribute("ListaAdvogadosHabilitacao");
					
					//limpa variaveis da serventia promotor
					Id_Serventia = ""; Serventia = "";
					
					//se o usuário da sessão for um procurador (advogado público) , não deve limpar o ProcessoParteAdvogado por conta do
					//funcionamento da tela
					if(request.getSession().getAttribute("solicitacaoProcurador") == null || 
							!(Boolean)request.getSession().getAttribute("solicitacaoProcurador")){
						ProcessoParteAdvogadodt.limpar();
						ProcessoParteAdvogadodt.setEstadoOabUf("GO");
					}
					
				} else {
					request.setAttribute("ListaAdvogadoParte", tempList);
					request.setAttribute("MensagemErro", Mensagem);
				}
				break;

			// Confirmação de Desabilitação de um advogado do processo
			case Configuracao.Excluir:
				ProcessoParteAdvogadoDt advogado = getAdvogadoSelecionado(processoDt, posicaoLista);
				request.setAttribute("Mensagem", "Confirma a desabilitação do Advogado " + advogado.getNomeAdvogado() + " da parte " + advogado.getNomeParte() + "?");
				break;

			//Desabilitar Advogado do processo
			case Configuracao.ExcluirResultado:
				ProcessoParteAdvogadodt = getAdvogadoSelecionado(processoDt, posicaoLista);
				ProcessoParteAdvogadodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				ProcessoParteAdvogadodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

				ProcessoParteAdvogadone.desabilitaAdvogado(ProcessoParteAdvogadodt);
				request.setAttribute("MensagemOk", "Advogado Desabilitado com Sucesso");

				// Atualiza lista de advogados cadastrados
				processoDt.setListaAdvogados(ProcessoParteAdvogadone.consultarAdvogadosProcesso(processoDt.getId_Processo()));
				ProcessoParteAdvogadodt.limpar();
				break;

			//Confirmação de Habilitação como Advogado Principal
			case Configuracao.LocalizarAutoPai:
				ProcessoParteAdvogadodt = getAdvogadoSelecionado(processoDt, posicaoLista);
				request.setAttribute("Mensagem", "Confirma a definição do Advogado " + ProcessoParteAdvogadodt.getNomeAdvogado() + " como Principal?");
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				stAcao = "/WEB-INF/jsptjgo/HabilitaAdvogadoPrincipal.jsp";
				break;

			//Habilita Advogado como Principal
			case Configuracao.Curinga6:
				ProcessoParteAdvogadone.defineAdvogadoPrincipal(ProcessoParteAdvogadodt);
				request.setAttribute("MensagemOk", "Advogado Principal Definido com Sucesso");
				// Atualiza lista de advogados
				processoDt.setListaAdvogados(ProcessoParteAdvogadone.consultarAdvogadosProcessoParte(processoDt.getId_Processo(), UsuarioSessao.getUsuarioDt().getId_UsuarioServentia()));

				ProcessoParteAdvogadodt.limpar();
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				stAcao = "/WEB-INF/jsptjgo/HabilitaAdvogadoPrincipal.jsp";
				break;

			//Localiza os advogados habilitados em um processo apenas para visualização
			case Configuracao.Curinga7:
				id_Processo = processoDt.getId_Processo();
				if (id_Processo != null) {
					tempList = ProcessoParteAdvogadone.consultarAdvogadosProcesso(id_Processo);
					if (tempList.size() > 0) request.setAttribute("ListaAdvogados", tempList);
					else request.setAttribute("MensagemErro", "Nenhum advogado habilitado nesse processo.");

					request.setAttribute("processoDt", processoDt);
					stAcao = "/WEB-INF/jsptjgo/ListaAdvogadosProcesso.jsp";
				} else request.setAttribute("MensagemErro", "É necessário selecionar um processo.");
				break;
				
			case Configuracao.Curinga8:
			// trocar nome do link para "Habilitar no Processo"
			// validação de acesso ServentiaTipoDt (só procuradorias - 4) - se não for delas, perfil não terá acesso

			ProcessoParteAdvogadodt.limpar();
			String idProcesso = request.getParameter("Id_Processo");
			if (idProcesso == null) {
				idProcesso = processoDt.getId_Processo();
			}
			if (idProcesso != null) {
				switch (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo())) {
				case ServentiaTipoDt.DEFENSORIA_PUBLICA:
				case ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO:
				case ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL:
				case ServentiaTipoDt.PROCURADORIA_UNIAO:
				case ServentiaTipoDt.ADVOGADO_PUBLICO_AUTARQUIAS:

					ProcessoParteAdvogadodt.setOabNumero(UsuarioSessao.getUsuarioDt().getOabNumero());
					ProcessoParteAdvogadodt.setOabComplemento(UsuarioSessao.getUsuarioDt().getOabComplemento());
					ProcessoParteAdvogadodt.setEstadoOabUf(UsuarioSessao.getUsuarioDt().getEstadoRepresentacao());

					switch (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo())) {
						case ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL:
							ProcessoParteAdvogadodt.setAdvogadoTipo(String.valueOf(AdvogadoDt.ADVOGADO_PARTICULAR));
							break;
						case ServentiaTipoDt.DEFENSORIA_PUBLICA:
							ProcessoParteAdvogadodt.setAdvogadoTipo(String.valueOf(AdvogadoDt.DEFENSOR_PUBLICO));
							break;
						case ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL:
							ProcessoParteAdvogadodt.setAdvogadoTipo(String.valueOf(AdvogadoDt.PROCURADOR_MUNICIPAL));
							break;
						case ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO:
							ProcessoParteAdvogadodt.setAdvogadoTipo(String.valueOf(AdvogadoDt.PROCURADOR_ESTADUAL));
							break;
						case ServentiaTipoDt.ADVOGADO_PUBLICO_AUTARQUIAS:
							ProcessoParteAdvogadodt.setAdvogadoTipo(String.valueOf(AdvogadoDt.ADVOGADO_PUBLICO));
							break;
					}

					tempList = ProcessoParteAdvogadone.consultarAdvogadosProcesso(idProcesso);
					processoDt.setListaAdvogados(tempList);
					
					request.getSession().setAttribute("solicitacaoProcurador", true);
					break;
				default:
					// No caso de não poder habilitar, redireciona para tela de visualização apenas
					tempList = ProcessoParteAdvogadone.consultarAdvogadosProcesso(idProcesso);
					if (tempList.size() > 0) request.setAttribute("ListaAdvogados", tempList);
					else request.setAttribute("MensagemErro", "Nenhum advogado habilitado nesse processo.");

					request.setAttribute("processoDt", processoDt);
					stAcao = "/WEB-INF/jsptjgo/ListaAdvogadosProcesso.jsp";
					request.setAttribute("MensagemErro", "Funcionalidade liberada apenas para o perfil Advogado Público. Redirecionando para listagem de advogados habilitados no processo.");
					break;
				}
			} else
				request.setAttribute("MensagemErro", "É necessário selecionar um processo.");

			break;
			//Alteração do atributo Dativo do advogado
			case Configuracao.Imprimir:
				String idProcessoParteTipoDativo = request.getParameter("idProcessoParteTipoAlterarDativo");
				ProcessoParteAdvogadoDt processoParteAdvogadoDativoAlterar = ProcessoParteAdvogadone.consultarId(idProcessoParteTipoDativo);
				
				processoParteAdvogadoDativoAlterar.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
				processoParteAdvogadoDativoAlterar.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				
				//O que será feito nesta situação é inverter Dativo por parte
				//do advogado. Ou seja, se é true passa a ser false e vice-versa
				
				if(processoParteAdvogadoDativoAlterar.getDativo() != null && processoParteAdvogadoDativoAlterar.getDativo().length() > 0
						&& processoParteAdvogadoDativoAlterar.getDativo().equalsIgnoreCase("1")){
					processoParteAdvogadoDativoAlterar.setDativo("0");
				} else {
					processoParteAdvogadoDativoAlterar.setDativo("1");
				}
				
				ProcessoParteAdvogadone.modificarAdvogadoDativo(processoParteAdvogadoDativoAlterar);
				String mensagemConfirmacaoDativo = "O Advogado " + processoParteAdvogadoDativoAlterar.getNomeAdvogado() + " teve status alterado para";
				if(!(processoParteAdvogadoDativoAlterar.getDativo() != null && processoParteAdvogadoDativoAlterar.getDativo().length() > 0
						&& processoParteAdvogadoDativoAlterar.getDativo().equalsIgnoreCase("1"))) {
					mensagemConfirmacaoDativo += " NÃO";
				}
				mensagemConfirmacaoDativo += " DATIVO.";
				request.setAttribute("MensagemOk", mensagemConfirmacaoDativo);
				
				// Atualiza lista de advogados cadastrados
				processoDt.setListaAdvogados(ProcessoParteAdvogadone.consultarAdvogadosProcesso(processoDt.getId_Processo()));
				
				//se o usuário da sessão for um procurador (advogado público) , não deve limpar o ProcessoParteAdvogado por conta do
				//funcionamento da tela
				if(request.getSession().getAttribute("solicitacaoProcurador") == null || 
						!(Boolean)request.getSession().getAttribute("solicitacaoProcurador")){
					ProcessoParteAdvogadodt.limpar();
					ProcessoParteAdvogadodt.setEstadoOabUf("GO");
				}
				
			break;	
			//Alteração do atributo Receber Intimação do advogado
			case Configuracao.Curinga9:
				boolean autorizarAlteracao = true;
				String idProcessoParteTipo = request.getParameter("idProcessoParteTipoAlterarRecebimento");
				ProcessoParteAdvogadoDt processoParteAdvogadoAlterar = ProcessoParteAdvogadone.consultarId(idProcessoParteTipo);
				
				//Se o status for true, então o usuário estará tentando transformar
				//o status do advogado para false, logo é preciso validar se há algum
				//outro advogado da parte com status true.
				if (processoParteAdvogadoAlterar.getRecebeIntimacao()) {
					autorizarAlteracao = false;
					List listaAdvogadosParte = ProcessoParteAdvogadone.consultarAdvogadosParte(processoParteAdvogadoAlterar.getId_ProcessoParte());
					if (!listaAdvogadosParte.isEmpty()) {
						for (int i = 0; i < listaAdvogadosParte.size(); i++) {
							ProcessoParteAdvogadoDt itemLista = (ProcessoParteAdvogadoDt)listaAdvogadosParte.get(i);
							//É preciso validar para não comparar com o objeto que está sendo alterado, pois 
							//neste caso o status dele será sempre true
							if(!itemLista.getId().equals(idProcessoParteTipo) && itemLista.getRecebeIntimacao()){
								autorizarAlteracao = true;
								break;
							}
						}
					}
				}
				
				if(autorizarAlteracao){
					processoParteAdvogadoAlterar.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					processoParteAdvogadoAlterar.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
					//O que será feito nesta situação é inverter o recebimento de intimação por parte
					//do advogado. Ou seja, se é true passa a ser false e vice-versa
					if(processoParteAdvogadoAlterar.getRecebeIntimacao()){
						processoParteAdvogadoAlterar.setRecebeIntimacao(false);
					} else {
						processoParteAdvogadoAlterar.setRecebeIntimacao(true);
					}
					
					ProcessoParteAdvogadone.modificarRecebimentoIntimacaoAdvogado(processoParteAdvogadoAlterar);
					String mensagemConfirmacao = "O Advogado " + processoParteAdvogadoAlterar.getNomeAdvogado() + " teve status alterado para";
					if(!processoParteAdvogadoAlterar.getRecebeIntimacao()) {
						mensagemConfirmacao += " NÃO";
					}
					mensagemConfirmacao += " receber intimação.";
					request.setAttribute("MensagemOk", mensagemConfirmacao);
					
					// Atualiza lista de advogados cadastrados
					processoDt.setListaAdvogados(ProcessoParteAdvogadone.consultarAdvogadosProcesso(processoDt.getId_Processo()));
					
					//se o usuário da sessão for um procurador (advogado público) , não deve limpar o ProcessoParteAdvogado por conta do
					//funcionamento da tela
					if(request.getSession().getAttribute("solicitacaoProcurador") == null || 
							!(Boolean)request.getSession().getAttribute("solicitacaoProcurador")){
						ProcessoParteAdvogadodt.limpar();
						ProcessoParteAdvogadodt.setEstadoOabUf("GO");
					}
				} else {
					request.setAttribute("MensagemErro", "A parte deve possuir pelo menos um advogado autorizado a receber permissão.");
				}
				
				break;
			
			case (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Estado"};
					String[] lisDescricao = {"Estado", "Descrição"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_EstadoOab");
					request.setAttribute("tempBuscaDescricao", "EstadoOab");
					request.setAttribute("tempBuscaPrograma", "Estado");
					request.setAttribute("tempRetorno", "ProcessoParteAdvogado");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  (EstadoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);							
				}else{
					String stTemp = "";
						stTemp = ProcessoParteAdvogadone.consultarDescricaoEstadoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;

			default:
				stId = request.getParameter("Id_ProcessoParteAdvogado");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoParteAdvogadodt.getId())) {
					ProcessoParteAdvogadodt.limpar();
					ProcessoParteAdvogadodt = ProcessoParteAdvogadone.consultarId(stId);
				}
				break;
		}
		
		request.setAttribute("tempFluxo1",tempFluxo1);
		request.setAttribute("Id_Serventia",Id_Serventia);
		request.setAttribute("Serventia",Serventia);

		request.setAttribute("posicaoLista", posicaoLista);
		request.getSession().setAttribute("processoDt", processoDt);
		request.getSession().setAttribute("ProcessoParteAdvogadodt", ProcessoParteAdvogadodt);
		request.getSession().setAttribute("ProcessoParteAdvogadone", ProcessoParteAdvogadone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Obtem a partir da lista de advogados qual Objeto corresponde a posição escolhida pelo usuário
	 */
	protected ProcessoParteAdvogadoDt getAdvogadoSelecionado(ProcessoDt processoDt, String posicaoLista) {
		ProcessoParteAdvogadoDt dt = null;
		if (posicaoLista.length() > 0) {
			int posicao = Funcoes.StringToInt(posicaoLista);
			dt = (ProcessoParteAdvogadoDt) processoDt.getListaAdvogados().get(posicao);
		}
		return dt;
	}

}
