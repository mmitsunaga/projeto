package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

/**
 * Controla as trocas de Procuradores, Defensores e Advogados Escritório Jurídico Responsáveis por Processos.
 * 
 * @author lsbernardes
 * 21/03/2011
 */
public class ProcessoProcuradorResponsavelCt extends Controle {
	
	private static final long serialVersionUID = 7405258784986923819L;

	public int Permissao(){
		return ProcessoResponsavelDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoResponsavelDt ProcessoResponsaveldt;
		ProcessoResponsavelNe ProcessoResponsavelne;
		
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) {
			stNomeBusca1 = request.getParameter("nomeBusca1");
		}
		
		ProcessoDt processoDt = null;	
		String Mensagem = "";
		String processos[] = null;
		String stId = "";
		String Id_UsuarioServentiaAtural = null;

		String stAcao = "/WEB-INF/jsptjgo/TrocaResponsavelProcessoParteAdvogado.jsp";
		
		request.setAttribute("tempRetorno", "ProcessoProcuradorResponsavel");
		request.setAttribute("tempPrograma", "ProcessoProcuradorResponsavel");				
		
		ProcessoResponsavelne = (ProcessoResponsavelNe) request.getSession().getAttribute("ProcessoResponsavelne");
		if (ProcessoResponsavelne == null) ProcessoResponsavelne = new ProcessoResponsavelNe();

		ProcessoResponsaveldt = (ProcessoResponsavelDt) request.getSession().getAttribute("ProcessoResponsaveldt");
		if (ProcessoResponsaveldt == null) ProcessoResponsaveldt = new ProcessoResponsavelDt();
		
		//id_usuario_serventia que será substituido
		if (request.getParameter("id_UsuarioServentiaAtual") != null && !request.getParameter("id_UsuarioServentiaAtual").equals("")) {
			Id_UsuarioServentiaAtural = request.getParameter("id_UsuarioServentiaAtual");
			request.getSession().setAttribute("id_UsuarioServentiaAtual", Id_UsuarioServentiaAtural);
			
		} else if (request.getSession().getAttribute("id_UsuarioServentiaAtual") != null && !request.getSession().getAttribute("id_UsuarioServentiaAtual").equals("") ){
			Id_UsuarioServentiaAtural = request.getSession().getAttribute("id_UsuarioServentiaAtual").toString();
		
		} 
		
		ProcessoResponsaveldt.setId_UsuarioServentia(request.getParameter("Id_UsuarioServentia"));
		ProcessoResponsaveldt.setUsuarioServentia(request.getParameter("UsuarioServentia"));
		ProcessoResponsaveldt.setId_Processo(request.getParameter("Id_Processo"));
		ProcessoResponsaveldt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		ProcessoResponsaveldt.setId_Grupo(request.getParameter("Id_Grupo"));
		ProcessoResponsaveldt.setGrupoCodigo(request.getParameter("GrupoCodigo"));
		ProcessoResponsaveldt.setCargoTipoCodigo(request.getParameter("CargoTipoCodigo"));
	
		ProcessoResponsaveldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoResponsaveldt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		//se for em lote tem uma jsp espcifica
		if (ProcessoResponsaveldt.getListaProcessos() != null && ProcessoResponsaveldt.getListaProcessos().size()>1){
			stAcao = "/WEB-INF/jsptjgo/TrocaResponsavelLoteProcessoParteAdvogado.jsp";
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			//Inicializa Troca de Responsável
			case Configuracao.Novo:
				ProcessoResponsaveldt = new ProcessoResponsavelDt();

				// Captura os processos no caso de Troca de Responsável em Lote
				if (request.getParameter("processos") != null){
					//se for em lote tem uma jsp espcifica
					stAcao = "/WEB-INF/jsptjgo/TrocaResponsavelLoteProcessoParteAdvogado.jsp";
					processos = request.getParameterValues("processos");				
				} else {
					//Recupera o processo da sessão e adiciona ao vetor
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					//Adiciona processo único ao vetor para montar tela corretamente
					if (processoDt != null && processoDt.getId().length() > 0) {
						processos = new String[] {processoDt.getId() };
					}
				}
				
				if (processos != null && processos.length > 0) {
					//Consulta dados básicos de cada processo e adiciona à lista
					for (int i = 0; i < processos.length; i++) {
						ProcessoDt obj = ProcessoResponsavelne.consultarProcessoIdTrocaResponsavel(processos[i]);
						//Verifica se usuário pode trocar o responsável pelo processo parte
						Mensagem = ProcessoResponsavelne.podeTrocarResponsavelProcessoParte(obj, UsuarioSessao.getUsuarioDt(), ProcessoResponsavelne);
						
						if (Mensagem.length() == 0) {
							obj.setListaProcessoParteAdvogado(ProcessoResponsavelne.consultarProcessosPartesAdvogados(obj.getId(), UsuarioSessao.getUsuarioDt().getId_Serventia(), Id_UsuarioServentiaAtural));
							ProcessoResponsaveldt.addListaProcessos(obj);
						} else {
							if (processoDt != null){
								redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
							} else {
								redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=" + Mensagem);
							}
							return;
						}
					}
				} else {
					// Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				break;
				
			case Configuracao.Salvar:
				request.setAttribute("Mensagem", "Clique para confirmar a Troca do Responsável.");
				break;

			case Configuracao.SalvarResultado:
				Mensagem = ProcessoResponsavelne.verificarTrocaResponsavelProcessoParte(ProcessoResponsaveldt, Id_UsuarioServentiaAtural);
				if (Mensagem.length() == 0) {
					ProcessoResponsavelne.salvarTrocaResponsavelProcessoParteAdvogado(ProcessoResponsaveldt.getId_UsuarioServentia(), Id_UsuarioServentiaAtural, UsuarioSessao.getUsuarioDt().getId_UsuarioServentia() ,UsuarioSessao.getUsuarioDt().getGrupoCodigo(), ProcessoResponsaveldt.getListaProcessos(),  new LogDt(UsuarioSessao.getId_Usuario(), request.getRemoteAddr()));

					if (ProcessoResponsaveldt.getListaProcessos() != null && ProcessoResponsaveldt.getListaProcessos().size()==1){
						ProcessoDt objTemp =  ProcessoResponsaveldt.getPrimeiroProcessoLista();
						if (objTemp!=null){
							objTemp.setListaProcessoParteAdvogado(ProcessoResponsavelne.consultarProcessosPartesAdvogados(objTemp.getId(), UsuarioSessao.getUsuarioDt().getId_Serventia(), null));
							ProcessoResponsaveldt.setListaProcessos(new ArrayList());
							ProcessoResponsaveldt.addListaProcessos(objTemp);
						
							request.getSession().removeAttribute("id_UsuarioServentiaAtual");
							ProcessoResponsaveldt.setId_UsuarioServentia("null");
							ProcessoResponsaveldt.setUsuarioServentia("");
							request.setAttribute("MensagemOk", "Troca do Responsável Efetuada com Sucesso.");
						}
					} else {
						request.getSession().removeAttribute("id_UsuarioServentiaAtual");
						int qtd = ProcessoResponsaveldt.getListaProcessos().size();
						ProcessoResponsaveldt = new ProcessoResponsavelDt();
						ProcessoResponsaveldt.setId_UsuarioServentia("null");
						ProcessoResponsaveldt.setUsuarioServentia("");
						super.redireciona(response, "BuscaProcesso?PaginaAtual=" + Configuracao.Editar+"&tipoConsultaProcesso=3&MensagemOk=A troca de responsável em "+qtd+" Processos foi efetuado com Sucesso.");
					}
					
				} else {
					request.setAttribute("MensagemErro", Mensagem);
				}
				
				break;

			//Consulta possíveis usuários que podem ser novos responsáveis para uma pendência
			case (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Nome Usuário"};
					String[] lisDescricao = {"Usuário","Serventia"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_UsuarioServentia");
					request.setAttribute("tempBuscaDescricao", "UsuarioServentia");
					request.setAttribute("tempBuscaPrograma", "Usuario Serventia");
					request.setAttribute("tempRetorno", "ProcessoProcuradorResponsavel");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);	
				} else {
					String stTemp = "";
					stTemp = ProcessoResponsavelne.consultarUsuariosServentiaJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
					
					enviarJSON(response, stTemp);
					
					return;								
				}
				break;	
			
			default:
				stId = request.getParameter("Id_ProcessoResponsavel");
				if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(ProcessoResponsaveldt.getId())) {
					ProcessoResponsaveldt.limpar();
					ProcessoResponsaveldt = ProcessoResponsavelne.consultarId(stId);
				}
				break;
		}	

		request.getSession().setAttribute("ProcessoResponsaveldt", ProcessoResponsaveldt);
		request.getSession().setAttribute("ProcessoResponsavelne", ProcessoResponsavelne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
}
