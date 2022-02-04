package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Controla as trocas de Responsáveis (Assistente Gabinete)por Processo.
 * 
 * @author lsbernardes
 * 19/01/2011
 */
public class ProcessoResponsavelAssistenteCt extends ProcessoResponsavelCtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5113143067034432564L;

	/**
	 * 
	 */
	

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

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao = "/WEB-INF/jsptjgo/ProcessoResponsavel.jsp";
		request.setAttribute("tempRetorno", "ProcessoResponsavelAssistente");
		request.setAttribute("tempPrograma", "ProcessoResponsavelAssistente");
		
		// Obtem o Id e a descrição da Serventia selecionada
		if (request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").trim().equalsIgnoreCase("")) Id_Serventia = request.getParameter("Id_Serventia");
		if (request.getParameter("Serventia") != null && !request.getParameter("Serventia").trim().equalsIgnoreCase("")) Serventia = request.getParameter("Serventia");
		if (Id_Serventia == null || Id_Serventia.trim().equalsIgnoreCase("") || Serventia == null || Serventia.trim().equalsIgnoreCase("")){
			Id_Serventia = UsuarioSessao.getUsuarioDt().getId_Serventia();
			Serventia = UsuarioSessao.getUsuarioDt().getServentia();
		}
		
		ProcessoResponsavelne = (ProcessoResponsavelNe) request.getSession().getAttribute("ProcessoResponsavelne");
		if (ProcessoResponsavelne == null) ProcessoResponsavelne = new ProcessoResponsavelNe();

		ProcessoResponsaveldt = (ProcessoResponsavelDt) request.getSession().getAttribute("ProcessoResponsaveldt");
		if (ProcessoResponsaveldt == null) ProcessoResponsaveldt = new ProcessoResponsavelDt();

		ProcessoResponsaveldt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		ProcessoResponsaveldt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		ProcessoResponsaveldt.setId_Processo(request.getParameter("Id_Processo"));
		ProcessoResponsaveldt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		ProcessoResponsaveldt.setId_Grupo(request.getParameter("Id_Grupo"));
		ProcessoResponsaveldt.setGrupoCodigo(request.getParameter("GrupoCodigo"));
		ProcessoResponsaveldt.setCargoTipoCodigo(request.getParameter("CargoTipoCodigo"));

		ProcessoResponsaveldt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoResponsaveldt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));

		posicaoLista = request.getParameter("posicaoLista");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			//Inicializa Troca ou Definição de Responsável
			case Configuracao.Novo:
				
				//Se chegou nesse ponto e o usuário é da UPJ, deve ser redirecionado para a tela de troca de assistente de gabinete com fluxo
				if(UsuarioSessao.isGabineteUpj()) {
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					String link = ProcessoResponsavelne.redirecionarTelaAssistenteUPJ(processoDt, UsuarioSessao);
					if(link.length() > 0) {
						redireciona(response, link);
					} else {
						 request.setAttribute("MensagemErro", "Não foi possível concluir a operação. Entre em contato com o suporte.");
					}
					return;
				}
				
				ProcessoResponsaveldt = new ProcessoResponsavelDt();

				// Captura os processos no caso de Troca de Responsável em Lote
				if (request.getParameter("processos") != null) processos = request.getParameterValues("processos");
				else {
					//Recupera o processo da sessão e adiciona ao vetor
					processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					//Adiciona processo único ao vetor para montar tela corretamente
					if (processoDt != null && processoDt.getId().length() > 0) processos = new String[] {processoDt.getId() };
				}
				
				boolean atualizaTipoServentia = true; // Utilizada para obter o tipo da serventia do primeiro processo da lista (lote) ou o único processo selecionado
				if (processos != null && processos.length > 0) {
					//Consulta dados básicos de cada processo e adiciona à lista
					for (int i = 0; i < processos.length; i++) {
						ProcessoDt obj = ProcessoResponsavelne.consultarProcessoId(processos[i]);
						//Verifica se usuário pode trocar o responsável pelo processo
						obj.setServentiaCargoResponsavelDt(ProcessoResponsavelne.consultarAssistenteResponsavelProcesso(obj.getId(),UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));
						ProcessoResponsaveldt.addListaProcessos(obj);
						if (obj.getServentiaCargoResponsavelDt() != null){	
							if (atualizaTipoServentia){
								ServentiaDt serventiaProcesso = ProcessoResponsavelne.consultarServentiaIdSimples(obj.getId_Serventia());
								if (serventiaProcesso != null) ProcessoResponsaveldt.setServentiaTipoCodigoProcesso(serventiaProcesso.getServentiaTipoCodigo());
								atualizaTipoServentia = false; // Para garantir que só irá obter de um único processo
							}
						} 
						//se não houve conclusão será definido o axistente previamente
					}
				} else {
					// Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				break;

			case Configuracao.Salvar:
				if (String.valueOf(request.getParameter("ProcessoSemAssistente")).equalsIgnoreCase("true")) request.setAttribute("ProcessoSemAssistente", "S");		
				request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Responsável pelo Processo");
				break;

			case Configuracao.SalvarResultado:
				// Verifica se a pendencia deverá ficar sem o assistente
				boolean ehSemAssistente = false;
				if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
					ehSemAssistente = ((request.getParameter("ProcessoSemAssistente") != null && String.valueOf(request.getParameter("ProcessoSemAssistente")).equalsIgnoreCase("true")));							
				}
				Mensagem = ProcessoResponsavelne.verificarTrocaResponsavel(ProcessoResponsaveldt, UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo(), UsuarioSessao.getUsuarioDt().getGrupoCodigo(), ehSemAssistente);
//será possivel trocar ou definir o assiste de processo				
//				Mensagem += ProcessoResponsavelne.verificarResponsavel(ProcessoResponsaveldt);
				//, (String.valueOf(request.getParameter("ProcessoSemAssistente")).equalsIgnoreCase("true"))
				if (Mensagem.length() == 0) {							
					ProcessoResponsavelne.AtualizarTrocaResponsavel(ProcessoResponsaveldt.getId_ServentiaCargo(), ProcessoResponsaveldt.getListaProcessos(), ProcessoResponsaveldt.getCargoTipoCodigo(), new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog()), ehSemAssistente);
					if (ehSemAssistente)
						request.setAttribute("MensagemOk", "Retirada de Assistente Efetuada com Sucesso.");
					else
						request.setAttribute("MensagemOk", "Troca de Responsável Efetuada com Sucesso.");	

					List listaProcessos = ProcessoResponsaveldt.getListaProcessos();
					for (int i = 0; i < listaProcessos.size(); i++) {
						ProcessoDt objTemp = (ProcessoDt) listaProcessos.get(i);
						objTemp.setServentiaCargoResponsavelDt(ProcessoResponsavelne.consultarAssistenteResponsavelProcesso(objTemp.getId(), UsuarioSessao.getUsuarioDt().getId_Serventia(), UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()));
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
				request.setAttribute("tempBuscaProcessoResponsavel", "ProcessoResponsavel");
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
					request.setAttribute("tempRetorno", "ProcessoResponsavelAssistente");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp =  ProcessoResponsavelne.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, Id_Serventia, UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
						
					enviarJSON(response, stTemp);
					return;
				}
				break;
			
			// Consultar as serventias que utilizam o mesmo tipo da Serventia logada 
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					request.setAttribute("tempBuscaPrograma", "Serventia");	
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", ServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
				String stTemp="";
				
					String serventiaSubTipo = obtenhaServentiaSubTipo(ProcessoResponsaveldt, UsuarioSessao.getUsuarioDt());
					stTemp = ProcessoResponsavelne.consultarServentiasAtivasJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), serventiaSubTipo);
					
					enviarJSON(response, stTemp);
					
					request.setAttribute("serventiaTipoCodigo", UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo());
					request.setAttribute("serventiaSubTipoCodigo", serventiaSubTipo);
					
					// Limpa as informações do Cargo anteriormente escolhidos
					ProcessoResponsaveldt.setId_ServentiaCargo("null");
					ProcessoResponsaveldt.setServentiaCargo("");
				
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
		
		if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
			request.setAttribute("EhParaExibirSemAssistente", "S");			
		}		

		request.setAttribute("Id_Serventia",Id_Serventia);
		request.setAttribute("Serventia",Serventia);
		//request.setAttribute("UsuarioDt", UsuarioSessao.getUsuarioDt());		
		
		request.getSession().setAttribute("ProcessoResponsaveldt", ProcessoResponsaveldt);
		request.getSession().setAttribute("ProcessoResponsavelne", ProcessoResponsavelne);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Obtem o SubTipo da Serventia para ser utilizado na pesquisa de serventias
	 * @param processoResponsaveldt
	 * @param usuarioDt
	 * @return
	 */
	protected String obtenhaServentiaSubTipo(ProcessoResponsavelDt processoResponsaveldt, UsuarioDt usuarioDt){
		
		if (processoResponsaveldt == null) return "";
		
		String serventiaTipo_Processo = processoResponsaveldt.getServentiaTipoCodigoProcesso();
		
		if (serventiaTipo_Processo == null) return "";
		
		if (usuarioDt.getServentiaTipoCodigo().trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.PROMOTORIA))){
			if (serventiaTipo_Processo.trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.VARA))) return String.valueOf(ServentiaSubtipoDt.MP_PRIMEIRO_GRAU);
		
			if (serventiaTipo_Processo.trim().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.SEGUNDO_GRAU))) return String.valueOf(ServentiaSubtipoDt.MP_SEGUNDO_GRAU);
		}
		
		return "";
	}
}
