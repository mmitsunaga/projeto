package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class AudienciaProcessoCt extends AudienciaProcessoCtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8740179578005916763L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe usuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		AudienciaNe audienciaNe;
		AudienciaProcessoDt audienciaProcessoDt;
		AudienciaProcessoNe AudienciaProcessone;

		String Mensagem = "";
		String stId = "";
		String audienciaProcessos[] = null;
		String posicaoLista = "";

		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));
		
		String stAcao = "/WEB-INF/jsptjgo/AudienciaProcesso.jsp";
		request.setAttribute("tempRetorno", "AudienciaProcesso");
		request.setAttribute("tempPrograma", "AudienciaProcesso");

		AudienciaProcessone = (AudienciaProcessoNe) request.getSession().getAttribute("AudienciaProcessone");
		if (AudienciaProcessone == null) AudienciaProcessone = new AudienciaProcessoNe();

		audienciaProcessoDt = (AudienciaProcessoDt) request.getSession().getAttribute("AudienciaProcessoDt");
		if (audienciaProcessoDt == null) audienciaProcessoDt = new AudienciaProcessoDt();
		
		audienciaNe = (AudienciaNe) request.getSession().getAttribute("Audienciane");
		if (audienciaNe == null) audienciaNe = new AudienciaNe();

		audienciaProcessoDt.setId_Audiencia(request.getParameter("Id_Audiencia"));
		audienciaProcessoDt.setAudienciaTipo(request.getParameter("AudienciaTipo"));
		audienciaProcessoDt.setId_AudienciaProcessoStatus(request.getParameter("Id_AudienciaProcessoStatus"));
		audienciaProcessoDt.setAudienciaProcessoStatus(request.getParameter("AudienciaProcessoStatus"));
		audienciaProcessoDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		audienciaProcessoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		audienciaProcessoDt.setId_Processo(request.getParameter("Id_Processo"));
		audienciaProcessoDt.setProcessoNumero(request.getParameter("ProcessoNumero"));
		audienciaProcessoDt.setDataMovimentacao(request.getParameter("DataMovimentacao"));
		audienciaProcessoDt.setAudienciaTipoCodigo(request.getParameter("AudienciaTipoCodigo"));
		audienciaProcessoDt.setAudienciaProcessoStatusCodigo(request.getParameter("AudienciaProcessoStatusCodigo"));

		audienciaProcessoDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
		audienciaProcessoDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

		List listaAudienciaProcessos = (List) request.getSession().getAttribute("ListaAudienciaProcessos");

		posicaoLista = request.getParameter("posicaoLista");

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			//Inicializa Troca de Responsável
			case Configuracao.Novo:
				//AudienciaProcessoDt = new AudienciaProcessoDt();

				// Captura as AudienciaProcesso no caso de Troca de Responsável em Lote
				if (request.getParameter("audienciaProcessos") != null) audienciaProcessos = request.getParameterValues("audienciaProcessos");
				else if (request.getParameter("Id_AudienciaProcesso") != null) {
					String id_AudienciaProcesso = request.getParameter("Id_AudienciaProcesso");
					//Adiciona audiência única ao vetor para montar tela corretamente
					if (id_AudienciaProcesso != null && id_AudienciaProcesso.length() > 0) audienciaProcessos = new String[] {id_AudienciaProcesso };
				}

				listaAudienciaProcessos = new ArrayList();

				if (audienciaProcessos != null && audienciaProcessos.length > 0) {
					//Consulta dados básicos de cada AudienciaProcesso e adiciona à lista
					for (int i = 0; i < audienciaProcessos.length; i++) {
						AudienciaProcessoDt obj = AudienciaProcessone.consultarIdCompleto(audienciaProcessos[i]);
						listaAudienciaProcessos.add(obj);
					}
				} else {
					// Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "Audiencia?PaginaAtual=" + Configuracao.Localizar + "&fluxo=4&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				break;

			case Configuracao.Salvar:
				request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Responsável pela Audiência");
				break;

			case Configuracao.SalvarResultado:
				Mensagem = AudienciaProcessone.verificarTrocaResponsavel(audienciaProcessoDt, listaAudienciaProcessos);
				if (Mensagem.length() == 0) {
					AudienciaProcessone.salvarTrocaResponsavel(audienciaProcessoDt.getId_ServentiaCargo(), listaAudienciaProcessos, new LogDt(usuarioSessao.getId_Usuario(), usuarioSessao.getIpComputadorLog()));
					request.setAttribute("MensagemOk", "Troca de Responsável Efetuada com Sucesso.");

					//Atualiza responsáveis alterados
					for (int i = 0; i < listaAudienciaProcessos.size(); i++) {
						AudienciaProcessoDt objTemp = (AudienciaProcessoDt) listaAudienciaProcessos.get(i);
						listaAudienciaProcessos.remove(objTemp);
						objTemp = AudienciaProcessone.consultarIdCompleto(objTemp.getId());
						listaAudienciaProcessos.add(i, objTemp);
					}
					
					audienciaProcessoDt.setId_ServentiaCargo("null");
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			// Remover um processo selecionado
			case Configuracao.LocalizarAutoPai:
				if (posicaoLista.length() > 0) {
					int posicao = Funcoes.StringToInt(posicaoLista);
					listaAudienciaProcessos.remove(posicao);
				}
				break;
	
			// CONSULTAR CARGOS DE UMA SERVENTIA
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): {
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = { "ServentiaCargo" };
					String[] lisDescricao = { "ServentiaCargo", "Usuario", "CargoTipo" };
					String[] camposHidden = { "ServentiaCargoUsuario", "CargoTipo" };
					request.setAttribute("camposHidden", camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
					request.setAttribute("tempRetorno", "AudienciaProcesso");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					
						stTemp = audienciaNe.consultarServentiaCargosAgendaAudienciaJSON(stNomeBusca1, posicaoPaginaAtual, usuarioSessao.getUsuarioDt().getId_Serventia(), usuarioSessao.getServentiaSubTipoCodigo() );
						
						enviarJSON(response, stTemp);
						

					return;
				}
				break;
			}
	
			default:
			stId = request.getParameter("Id_AudienciaProcesso");
				if (stId != null && !stId.isEmpty()) {
					if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(audienciaProcessoDt.getId())) {
						audienciaProcessoDt.limpar();
						audienciaProcessoDt = AudienciaProcessone.consultarId(stId);
					}
				}
				break;
		}

		request.getSession().setAttribute("AudienciaProcessoDt", audienciaProcessoDt);
		request.getSession().setAttribute("AudienciaProcessone", AudienciaProcessone);
		request.getSession().setAttribute("ListaAudienciaProcessos", listaAudienciaProcessos);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
