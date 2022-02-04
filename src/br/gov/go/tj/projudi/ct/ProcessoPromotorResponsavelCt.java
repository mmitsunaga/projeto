package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.ProcessoParteAdvogadoNe;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla as trocas de Promotores Responsáveis por Processos.
 * 
 * @author mmgomes
 * 10/11/2010
 */
public class ProcessoPromotorResponsavelCt extends Controle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3974598564986219710L;

	public ProcessoPromotorResponsavelCt(){
	}
	
	public int Permissao(){
		return ProcessoResponsavelDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoResponsavelDt ProcessoResponsaveldt;
		ProcessoResponsavelNe ProcessoResponsavelne;
		
		ProcessoDt processoDt = null;
		List tempList = null;
		String Mensagem = "";
		String processos[] = null;
		String stId = "";
		String posicaoLista = "";
		String Id_Serventia = "";
		String Serventia = "";
		String stNomeBusca1 = "";
		String idProcessoTemp = "";
		String idUsuServTemp = "";
		String idServentiaCargoTemp = "";
		String idServSubtipoTemp = "";
		MovimentacaoDt movimentacaoDt = null;
		LogDt log = null;
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao = "/WEB-INF/jsptjgo/ProcessoPromotorResponsavel.jsp";
		request.setAttribute("tempRetorno", "ProcessoPromotorResponsavel");
		request.setAttribute("tempPrograma", "ProcessoPromotorResponsavel");
		
				
		ProcessoResponsavelne = (ProcessoResponsavelNe) request.getSession().getAttribute("ProcessoResponsavelne");
		if (ProcessoResponsavelne == null) ProcessoResponsavelne = new ProcessoResponsavelNe();

		ProcessoResponsaveldt = (ProcessoResponsavelDt) request.getSession().getAttribute("ProcessoResponsaveldt");
		if (ProcessoResponsaveldt == null) ProcessoResponsaveldt = new ProcessoResponsavelDt();
		
		MovimentacaoNe Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();
		
		// Obtem o Id e a descrição da Serventia selecionada
		if (request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").trim().equalsIgnoreCase("")) Id_Serventia = request.getParameter("Id_Serventia");
		if (request.getParameter("Serventia") != null && !request.getParameter("Serventia").trim().equalsIgnoreCase("")) Serventia = request.getParameter("Serventia");	
		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").equalsIgnoreCase("null")) Id_Serventia = request.getParameter("tempFluxo1");
		if (request.getParameter("tempFluxo2") != null && !request.getParameter("tempFluxo1").equalsIgnoreCase("null")) Serventia = request.getParameter("tempFluxo2");	
		
		ProcessoResponsaveldt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		ProcessoResponsaveldt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		ProcessoResponsaveldt.setId_UsuarioServentia(request.getParameter("Id_UsuarioServentia"));
		ProcessoResponsaveldt.setId_Processo(request.getParameter("Id_Processo"));
		ProcessoResponsaveldt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		ProcessoResponsaveldt.setId_Grupo(request.getParameter("Id_Grupo"));
		ProcessoResponsaveldt.setGrupoCodigo(request.getParameter("GrupoCodigo"));
		ProcessoResponsaveldt.setCargoTipoCodigo(request.getParameter("CargoTipoCodigo"));
		
		request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));

		ProcessoResponsaveldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoResponsaveldt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		posicaoLista = request.getParameter("posicaoLista");
		idProcessoTemp = request.getParameter("idProcessoTemp");
		idServentiaCargoTemp = request.getParameter("idServentiaCargoTemp");
		
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		
		
		// Captura os processos no caso de Troca de Responsável em Lote
		if (request.getParameter("processos") != null) processos = request.getParameterValues("processos");
		else {
			//Recupera o processo da sessão e adiciona ao vetor
			processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
			//Adiciona processo único ao vetor para montar tela corretamente
			if (processoDt != null && processoDt.getId().length() > 0) processos = new String[] {processoDt.getId() };
		}
		
		List<ServentiaCargoDt> listaPromotoresResponsaveis = ProcessoResponsavelne.consultarResponsavelProcessoPromotores( processoDt.getId());
		request.getSession().setAttribute("listaPromotoresResponsaveis", listaPromotoresResponsaveis);
		
 		switch (paginaatual) {

			//Inicializa Troca de Responsável
			case Configuracao.Novo:
				ProcessoResponsaveldt = new ProcessoResponsavelDt();

				boolean atualizaTipoServentia = true; // Utilizada para obter o tipo da serventia do primeiro processo da lista (lote) ou o único processo selecionado
				if (processos != null && processos.length > 0) {
					if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))){
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + "Não é possível executar essa ação. Motivo: Processo físico!");
					} else {
						//Consulta dados básicos de cada processo e adiciona à lista
						for (int i = 0; i < processos.length; i++) {
							ProcessoDt obj = ProcessoResponsavelne.consultarProcessoId(processos[i]);
							//Verifica se usuário pode trocar o responsável pelo processo
							Mensagem = ProcessoResponsavelne.podeTrocarPromotorResponsavel(obj, UsuarioSessao.getUsuarioDt(), ProcessoResponsavelne);
							if (Mensagem.length() == 0) {
								
								ProcessoResponsaveldt.addListaProcessos(obj);
								
								if (listaPromotoresResponsaveis.size() != 0) {
									obj.setServentiaCargoResponsavelDt(listaPromotoresResponsaveis.get(0));
								}
								// Obtem a a serventia do promotor responsável pelo processo, caso exista.
								if(obj.getServentiaCargoResponsavelDt() != null){
									Id_Serventia = obj.getServentiaCargoResponsavelDt().getId_Serventia();
									Serventia = obj.getServentiaCargoResponsavelDt().getServentia();
								}
								// Obtem a ServentiaTipoCodigo da serventia onde o processo está, 
								// será utilizado para obter somente as promotorias de mesmo nível do processo, 
								// sendo de primeiro grau ou de segundo grau (Serventia SubTipo). 
								if (atualizaTipoServentia){
									ServentiaDt serventiaProcesso = ProcessoResponsavelne.consultarServentiaIdSimples(obj.getId_Serventia());
									if (serventiaProcesso != null) ProcessoResponsaveldt.setServentiaTipoCodigoProcesso(serventiaProcesso.getServentiaTipoCodigo());
									atualizaTipoServentia = false; // Para garantir que só irá obter de um único processo
								}
							} else {
								if (processoDt != null) redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
								else redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=" + Mensagem);
								return;
							}
														
						}
						
						// Se o promotor responsável não for localizado, obtém-se a serventia do tipo promotoria relacionada  
						if (Id_Serventia == null || Id_Serventia.trim().equalsIgnoreCase("") ||
						    Serventia == null || Serventia.trim().equalsIgnoreCase("")){
							// Busca promotoria relacionada a esta serventia
							ServentiaDt promotoriaRelacionada = ProcessoResponsavelne.consultarPromotoriaRelacionada(UsuarioSessao.getUsuarioDt().getId_Serventia());			            
				            if (promotoriaRelacionada == null) throw new MensagemException("Não há nenhuma Promotoria vinculada à Serventia.");
							// Obtem a promotoria relacionada a esta serventiaR			
							Id_Serventia = promotoriaRelacionada.getId();
							Serventia = promotoriaRelacionada.getServentia();
						}
					}
				} else {
					// Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				break;

			case Configuracao.Salvar:
				
				
				Mensagem = ProcessoResponsavelne.validaTrocaResponsavel(ProcessoResponsaveldt, idServentiaCargoTemp, listaPromotoresResponsaveis);
				
				if(!Mensagem.equals("")){
					
					request.setAttribute("MensagemErro", Mensagem);
					request.setAttribute("PaginaAnterior", "-1");
					break;
				}

				request.getSession().setAttribute("idProcessoTemp", idProcessoTemp);
				request.getSession().setAttribute("idServSubtipoTemp", idServSubtipoTemp);
				request.getSession().setAttribute("idUsuServTemp", idUsuServTemp);
				request.getSession().setAttribute("idServentiaCargoTemp", idServentiaCargoTemp);
				
				request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Responsável pelo Processo, esta ação irá gerar uma nova movimentação no processo.");
				break;

			case Configuracao.SalvarResultado:
				Mensagem = ProcessoResponsavelne.verificarTrocaResponsavel(ProcessoResponsaveldt, UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo(), UsuarioSessao.getUsuarioDt().getGrupoCodigo(), false);
				
				
				if (Mensagem.length() == 0) {
					
					log = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
					ProcessoResponsavelne.salvarTrocaResponsavelPromotor(ProcessoResponsaveldt.getId_ServentiaCargo(), idServentiaCargoTemp, ProcessoResponsaveldt.getId_UsuarioServentia(), UsuarioSessao.getUsuarioDt().getGrupoCodigo(), ProcessoResponsaveldt.getListaProcessos(), ProcessoResponsaveldt.getCargoTipoCodigo(), log);
										
					movimentacaoDt = new MovimentacaoDt();
					movimentacaoDt.setId_Processo(processoDt.getId());
					movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumero());
					movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.TROCAR_RESPONSAVEL_PROCESSO));
					movimentacaoDt.setMovimentacaoTipo("Troca de Responsável");
					movimentacaoDt.setComplemento("MP Responsável Anterior: "+  new UsuarioNe().consultarNomeUsuarioServentiaCargo(idServentiaCargoTemp)+ " <br> "
							+ "MP Responsável Atual: "+ new UsuarioNe().consultarNomeUsuarioServentiaCargo(ProcessoResponsaveldt.getId_ServentiaCargo()));
					movimentacaoDt.setId_UsuarioRealizador(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
					movimentacaoDt.setId_UsuarioLog(log.getId_Usuario());
					movimentacaoDt.setIpComputadorLog(log.getIpComputador());
					
					Movimentacaone.salvar(movimentacaoDt);
					
					request.setAttribute("MensagemOk", "Troca de Promotor Responsável Efetuada com Sucesso.");
					
					request.getSession().setAttribute("idProcessoTemp", null);
					request.getSession().setAttribute("idServSubtipoTemp", null);
					request.getSession().setAttribute("idUsuServTemp", null);
					request.getSession().setAttribute("idServentiaCargoTemp", null);
					
					listaPromotoresResponsaveis = ProcessoResponsavelne.consultarResponsavelProcessoPromotores( processoDt.getId());
					request.getSession().setAttribute("listaPromotoresResponsaveis", listaPromotoresResponsaveis);

					List listaProcessos = ProcessoResponsaveldt.getListaProcessos();
					for (int i = 0; i < listaProcessos.size(); i++) {
						ProcessoDt objTemp = (ProcessoDt) listaProcessos.get(i);
						objTemp.setServentiaCargoResponsavelDt(ProcessoResponsavelne.consultarResponsavelProcesso(objTemp.getId(), String.valueOf(GrupoTipoDt.MP), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));
					}
					ProcessoResponsaveldt.setId_ServentiaCargo("null");
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			// Remover um processo selecionado
			case Configuracao.LocalizarAutoPai:
				if (posicaoLista.length() > 0) {
					int posicao = Funcoes.StringToInt(posicaoLista);
					ProcessoResponsaveldt.getListaProcessos().remove(posicao);
				}
				break;

			case Configuracao.Localizar: //localizar
				stAcao = "/WEB-INF/jsptjgo/ProcessoResponsavelLocalizar.jsp";
				request.setAttribute("tempBuscaId_ProcessoResponsavel", "Id_ProcessoResponsavel");
				request.setAttribute("tempBuscaProcessoResponsavel", "ProcessoPromotorResponsavel");
				tempList = ProcessoResponsavelne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size() > 0) {
					request.setAttribute("ListaProcessoResponsavel", tempList);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ProcessoResponsavelne.getQuantidadePaginas());
					ProcessoResponsaveldt.limpar();
				} else {
					request.setAttribute("MensagemErro", "Dados Não Localizados");
				}
				break;

			//Consulta possíveis cargos que podem ser novos responsáveis por um processo
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo [Serventia]", "Usuário", "CargoTipo"};
					String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "ProcessoPromotorResponsavel");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					request.setAttribute("tempFluxo1", Id_Serventia);
					request.setAttribute("tempFluxo2", Serventia);
				}else{
					String stTemp = "";
					request.setAttribute("ServentiaTipoCodigo", ServentiaTipoDt.PROMOTORIA);
					stTemp = ProcessoResponsavelne.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, request.getParameter("tempFluxo1"), String.valueOf(ServentiaTipoDt.PROMOTORIA), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
						
					enviarJSON(response, stTemp);
					return;
				}
				break;
			
			// Consultar as serventias que utilizam o mesmo tipo da Serventia logada 
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serventia");
					request.setAttribute("tempBuscaPrograma","Consulta de Serventias");			
					request.setAttribute("tempRetorno","ProcessoPromotorResponsavel");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
//					ProcessoResponsaveldt.setId_ServentiaCargo("null");
//					ProcessoResponsaveldt.setServentiaCargo("");
					request.setAttribute("serventiaTipoCodigo", String.valueOf(ServentiaTipoDt.PROMOTORIA));
					stTemp = ProcessoResponsavelne.consultarServentiasAtivasJSON(stNomeBusca1, PosicaoPaginaAtual, String.valueOf(ServentiaTipoDt.PROMOTORIA), "");
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			// Desabilita promotor responsável pelo processo
			case (Configuracao.Excluir):

				idServSubtipoTemp = request.getParameter("idServSubtipoTemp");	
				
				int qtdePromotoresResp = 0;
				
				for(int i = 0; i < listaPromotoresResponsaveis.size(); i++){		
					ServentiaCargoDt promotor = (ServentiaCargoDt) listaPromotoresResponsaveis.get(i);
					if(promotor.isServentiaCargoOcupado()){
						qtdePromotoresResp++;
					}	
				}				
				
				if (qtdePromotoresResp <= 1){
					 request.setAttribute("MensagemErro", "Não é possível desabilitar esse promotor/procurador, pois ele é o único responsável do tipo habilitado no processo.");
					 request.setAttribute("PaginaAnterior", Configuracao.Novo);					 
				}else{
					request.setAttribute("Mensagem", "Clique para confirmar a desabilitação do promotor responsável, a ação registrará uma movimentação no processo.");		
					request.getSession().setAttribute("idServentiaCargoTemp", idServentiaCargoTemp);
					request.getSession().setAttribute("idProcessoTemp", idProcessoTemp);
					
				}
					
			break;
			
			case (Configuracao.ExcluirResultado):
				
				ProcessoResponsavelne.alterarCodigoTemp(idServentiaCargoTemp, idProcessoTemp, "-1");
			
				log = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
				movimentacaoDt = new MovimentacaoDt();
				movimentacaoDt.setId_Processo(processoDt.getId());
				movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumero());
				movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.DESABILITAR_RESPONSAVEL_PROCESSO));
				movimentacaoDt.setMovimentacaoTipo("Desabilitação Responsável");
				movimentacaoDt.setComplemento("Promotor Responsável Desabilitado: "+  new UsuarioNe().consultarNomeUsuarioServentiaCargo(idServentiaCargoTemp));
				movimentacaoDt.setId_UsuarioRealizador(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				movimentacaoDt.setId_UsuarioLog(log.getId_Usuario());
				movimentacaoDt.setIpComputadorLog(log.getIpComputador());
				
				Movimentacaone.salvar(movimentacaoDt);				
			
				listaPromotoresResponsaveis = ProcessoResponsavelne.consultarResponsavelProcessoPromotores(idProcessoTemp);
				
				request.getSession().setAttribute("listaPromotoresResponsaveis", listaPromotoresResponsaveis);

				request.setAttribute("MensagemOk", "Promotor Responsável Desabilitado com Sucesso.");
				
				request.getSession().setAttribute("idProcessoTemp", null);
				request.getSession().setAttribute("idServSubtipoTemp", null);
				request.getSession().setAttribute("idUsuServTemp", null);
				request.getSession().setAttribute("idServentiaCargoTemp", null);
				
			break;
			
			case (Configuracao.Atualizar):

				idServSubtipoTemp = request.getParameter("idServSubtipoTemp");	
				request.setAttribute("Mensagem", "Clique para confirmar a habilitação do promotor responsável, a ação registrará uma movimentação no processo.");		
				request.getSession().setAttribute("idServentiaCargoTemp", idServentiaCargoTemp);
				request.getSession().setAttribute("idProcessoTemp", idProcessoTemp);

			break;
			
			case (Configuracao.Curinga6):
				
				ProcessoResponsavelne.alterarCodigoTemp(idServentiaCargoTemp, idProcessoTemp, "0");
			
				log = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
				movimentacaoDt = new MovimentacaoDt();
				movimentacaoDt.setId_Processo(processoDt.getId());
				movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumero());
				movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.HABILITAR_RESPONSAVEL_PROCESSO));
				movimentacaoDt.setMovimentacaoTipo("Habilitação de Responsável");
				movimentacaoDt.setComplemento("Promotor Responsável Habilitado: "+  new UsuarioNe().consultarNomeUsuarioServentiaCargo(idServentiaCargoTemp));
				movimentacaoDt.setId_UsuarioRealizador(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
				movimentacaoDt.setId_UsuarioLog(log.getId_Usuario());
				movimentacaoDt.setIpComputadorLog(log.getIpComputador());
				
				Movimentacaone.salvar(movimentacaoDt);
				
			
				listaPromotoresResponsaveis = ProcessoResponsavelne.consultarResponsavelProcessoPromotores(idProcessoTemp);
				
				request.getSession().setAttribute("listaPromotoresResponsaveis", listaPromotoresResponsaveis);

				request.setAttribute("MensagemOk", "Promotor Responsável Habilitado com Sucesso.");
				
				request.getSession().setAttribute("idProcessoTemp", null);
				request.getSession().setAttribute("idServSubtipoTemp", null);
				request.getSession().setAttribute("idUsuServTemp", null);
				request.getSession().setAttribute("idServentiaCargoTemp", null);
				
			break;
			
			
			

			default:
				stId = request.getParameter("Id_ProcessoResponsavel");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoResponsaveldt.getId())) {
					ProcessoResponsaveldt.limpar();
					ProcessoResponsaveldt = ProcessoResponsavelne.consultarId(stId);
				}
				break;
		}	
		
		
		
		
		request.setAttribute("Id_Serventia",Id_Serventia);
		request.setAttribute("Serventia",Serventia);
		
		request.getSession().setAttribute("ProcessoResponsaveldt", ProcessoResponsaveldt);
		request.getSession().setAttribute("ProcessoResponsavelne", ProcessoResponsavelne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
}
