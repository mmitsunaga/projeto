package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla as troca de Redator de Processos.
 * 
 * @author hmgodinho
 */
public class ProcessoRedatorResponsavelCt extends ProcessoResponsavelCtGen {

	
	private static final long serialVersionUID = -5837115667842142148L;
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		ProcessoResponsavelDt ProcessoResponsaveldt;
		ProcessoResponsavelDt ProcessoResponsaveldtRevisor;
		ProcessoResponsavelDt ProcessoResponsaveldtVogal;
		ProcessoResponsavelNe ProcessoResponsavelne;
		MovimentacaoProcessoDt movimentacaoProcessoDt;

		ProcessoDt processoDt = null;
		String mensagem = "";
		String stId = "";
		String Id_Serventia = "";
		String Serventia = "";

		String stAcao = "/WEB-INF/jsptjgo/ProcessoRedatorResponsavel.jsp";
		request.setAttribute("tempRetorno", "ProcessoRedatorResponsavel");
		request.setAttribute("tempPrograma", "ProcessoRedatorResponsavel");
		
		// Obtem o Id e a descrição da Serventia selecionada
		if (request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").trim().equalsIgnoreCase("")) Id_Serventia = request.getParameter("Id_Serventia");
		if (request.getParameter("Serventia") != null && !request.getParameter("Serventia").trim().equalsIgnoreCase("")) Serventia = request.getParameter("Serventia");
		if (Id_Serventia == null || Id_Serventia.trim().equalsIgnoreCase("") || Serventia == null || Serventia.trim().equalsIgnoreCase("")){
			Id_Serventia = UsuarioSessao.getUsuarioDt().getId_Serventia();
			Serventia = UsuarioSessao.getUsuarioDt().getServentia();
		}
		
		movimentacaoProcessoDt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
		if (movimentacaoProcessoDt == null) movimentacaoProcessoDt = new MovimentacaoProcessoDt();
		
		ProcessoResponsavelne = (ProcessoResponsavelNe) request.getSession().getAttribute("ProcessoResponsavelne");
		if (ProcessoResponsavelne == null) ProcessoResponsavelne = new ProcessoResponsavelNe();

		ProcessoResponsaveldt = (ProcessoResponsavelDt) request.getSession().getAttribute("ProcessoResponsaveldt");
		if (ProcessoResponsaveldt == null) ProcessoResponsaveldt = new ProcessoResponsavelDt();
		
		ProcessoResponsaveldtRevisor = (ProcessoResponsavelDt) request.getSession().getAttribute("ProcessoResponsaveldtRevisor");
		if (ProcessoResponsaveldtRevisor == null) ProcessoResponsaveldtRevisor = new ProcessoResponsavelDt();
		
		ProcessoResponsaveldtVogal = (ProcessoResponsavelDt) request.getSession().getAttribute("ProcessoResponsaveldtVogal");
		if (ProcessoResponsaveldtVogal == null) ProcessoResponsaveldtVogal = new ProcessoResponsavelDt();
		
		ProcessoResponsaveldt.setId_Processo(request.getParameter("rdRedator"));
		ProcessoResponsaveldt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		ProcessoResponsaveldt.setId_Grupo(request.getParameter("Id_Grupo"));
		ProcessoResponsaveldt.setGrupoCodigo(request.getParameter("GrupoCodigo"));
		
		ProcessoResponsaveldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoResponsaveldt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());	

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		switch (paginaatual) {

			//Inicializa Troca de Redator
			case Configuracao.Novo:
				ProcessoResponsaveldt = new ProcessoResponsavelDt();
				ProcessoResponsaveldtRevisor = new ProcessoResponsavelDt();
				ProcessoResponsaveldtVogal = new ProcessoResponsavelDt();

				processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
				if (processoDt != null && processoDt.getId().length() > 0) {
					ProcessoDt processo = ProcessoResponsavelne.consultarProcessoId(processoDt.getId());
					
					//Verifica se usuário pode trocar o responsável pelo processo
					mensagem = ProcessoResponsavelne.podeTrocarResponsavel(processo, UsuarioSessao.getUsuarioDt(), ProcessoResponsavelne);
					if (mensagem.length() == 0) {
						
						processo.setServentiaCargoResponsavelDt(ProcessoResponsavelne.consultarResponsavelProcesso(processo.getId(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));
						if(processo.getServentiaCargoResponsavelDt().getRedator().equals("1")){
							request.setAttribute("redator", processo.getServentiaCargoResponsavelDt().getId());
						}
						processo.setServentiaCargoRevisorResponsavelDt(ProcessoResponsavelne.consultarRevisorResponsavelProcessoSegundoGrau(processo.getId()));
						if(processo.getServentiaCargoRevisorResponsavelDt() != null 
								&& processo.getServentiaCargoRevisorResponsavelDt().getRedator().equals("1")){
							request.setAttribute("redator", processo.getServentiaCargoRevisorResponsavelDt().getId());
						}
						processo.setServentiaCargoVogalResponsavelDt(ProcessoResponsavelne.consultarVogalResponsavelProcessoSegundoGrau(processo.getId()));															
						if(processo.getServentiaCargoVogalResponsavelDt() != null &&
								processo.getServentiaCargoVogalResponsavelDt().getRedator().equals("1")){
							request.setAttribute("redator", processo.getServentiaCargoVogalResponsavelDt().getId());
						}
						
						//Apesar de a troca de Redator acontecer apenas em um processo por vez, vou adicionar o processo na lista
						//para seguir o DT criado para ProcessoResponsavel
						ProcessoResponsaveldt.addListaProcessos(processo);
							
						ServentiaDt serventiaProcesso = ProcessoResponsavelne.consultarServentiaIdSimples(processo.getId_Serventia());
						if (serventiaProcesso != null) ProcessoResponsaveldt.setServentiaTipoCodigoProcesso(serventiaProcesso.getServentiaTipoCodigo());
					} else {
						
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);

						return;
					}
				} else {
					// Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				break;

			case Configuracao.Salvar:
				request.setAttribute("redator", request.getParameter("redator"));
				request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Redator do Processo.");
				break;

			//Conclui a troca do Redator
			case Configuracao.SalvarResultado:
				String idNovoRedator = request.getParameter("redator");
				//valida se a troca está sendo feita corretamente
				mensagem = ProcessoResponsavelne.verificarTrocaRedatorProcesso(ProcessoResponsaveldt, idNovoRedator);
				
				if (mensagem.length() == 0) {
					String idRedatorAtualProcesso = "";

					//A lista que vem no DT é sempre de um único processo. Foi feito dessa forma para seguir o padrão que
					//já havia sido criado para esse DT.
					processoDt =  ProcessoResponsaveldt.getPrimeiroProcessoLista();
					if (processoDt==null) return;
					
					if(processoDt.getServentiaCargoResponsavelDt().getRedator().equals("1")){
						idRedatorAtualProcesso = processoDt.getServentiaCargoResponsavelDt().getId();
					}
					if(processoDt.getServentiaCargoRevisorResponsavelDt().getRedator().equals("1")){
						idRedatorAtualProcesso = processoDt.getServentiaCargoRevisorResponsavelDt().getId();
					}
					if(processoDt.getServentiaCargoVogalResponsavelDt().getRedator().equals("1")){
						idRedatorAtualProcesso = processoDt.getServentiaCargoVogalResponsavelDt().getId();
					}
					
					ProcessoResponsavelne.salvarTrocaRedatorProcesso(processoDt.getId(), idRedatorAtualProcesso, idNovoRedator, UsuarioSessao.getUsuarioDt(), movimentacaoProcessoDt);
					
					//Limpando dados da movimentação
					movimentacaoProcessoDt.limpar();
					request.getSession().removeAttribute("Movimentacaodt");
					
					request.setAttribute("MensagemOk", "Troca de Redator efetuada com sucesso.");
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk="+request.getAttribute("MensagemOk"));
					return;
					
				} else {
					request.setAttribute("redator", idNovoRedator);
					request.setAttribute("MensagemErro", mensagem);
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

		request.setAttribute("Id_Serventia",Id_Serventia);
		request.setAttribute("Serventia",Serventia);
		
		request.getSession().setAttribute("ProcessoResponsaveldt", ProcessoResponsaveldt);
		request.getSession().setAttribute("ProcessoResponsavelne", ProcessoResponsavelne);
		request.getSession().setAttribute("ProcessoResponsaveldtRevisor", ProcessoResponsaveldtRevisor);
		request.getSession().setAttribute("ProcessoResponsaveldtVogal", ProcessoResponsaveldtVogal);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
}