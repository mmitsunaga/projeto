package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla as trocas de Responsáveis por Pendencia.
 * 
 * @author lsbernardes 18/03/2011
 */
public class PendenciaUsuarioServentiaResponsavelCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5449692605517280949L;

	public int Permissao() {
		return PendenciaResponsavelDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		PendenciaResponsavelDt pendenciaResponsavelDt;
		PendenciaDt pendenciaDt;
		PendenciaResponsavelNe pendenciaResponsavelne;

		// -Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null;
		//List lisDescricao = null;
		String stNomeBusca1 = "";
		// String stNomeBusca2 = "";

		if (request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		// if(request.getParameter("nomeBusca2") != null) stNomeBusca2 =
		// request.getParameter("nomeBusca2");

		// -fim controle de buscas ajax

		String Mensagem = "";
		String idPendencia = null;
		String hash = null;
		String Id_UsuarioServentiaAtual = null;

		String stAcao = "/WEB-INF/jsptjgo/PendenciaUsuarioServentiaResponsavel.jsp";
		request.setAttribute("tempRetorno", "PendenciaUsuarioServentiaResponsavel");
		request.setAttribute("tempPrograma", "PendenciaUsuarioServentiaResponsavel");

		if (request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		pendenciaResponsavelne = (PendenciaResponsavelNe) request.getSession().getAttribute("PendenciaResponsavelne");
		if (pendenciaResponsavelne == null) pendenciaResponsavelne = new PendenciaResponsavelNe();

		pendenciaResponsavelDt = (PendenciaResponsavelDt) request.getSession().getAttribute("PendenciaResponsaveldt");
		if (pendenciaResponsavelDt == null) pendenciaResponsavelDt = new PendenciaResponsavelDt();

		pendenciaDt = (PendenciaDt) request.getSession().getAttribute("Pendenciadt");
		if (pendenciaDt == null) pendenciaDt = new PendenciaDt();

		// Usuario serventia que será substituido
		if (request.getParameter("id_UsuarioServentiaAtual") != null && !request.getParameter("id_UsuarioServentiaAtual").equals("")) {
			Id_UsuarioServentiaAtual = request.getParameter("id_UsuarioServentiaAtual");
			request.getSession().setAttribute("id_UsuarioServentiaAtual", Id_UsuarioServentiaAtual);

		} else if (request.getSession().getAttribute("id_UsuarioServentiaAtual") != null) {
			Id_UsuarioServentiaAtual = request.getSession().getAttribute("id_UsuarioServentiaAtual").toString();
		}

		pendenciaResponsavelDt.setId_UsuarioResponsavel(request.getParameter("Id_UsuarioServentia"));
		pendenciaResponsavelDt.setUsuarioResponsavel(request.getParameter("UsuarioServentia"));

		pendenciaResponsavelDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		pendenciaResponsavelDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

		// Inicializa Troca de Responsável
		case Configuracao.Novo:
			request.getSession().removeAttribute("TrocouResponsavel");
			pendenciaResponsavelDt = new PendenciaResponsavelDt();

			idPendencia = String.valueOf(request.getParameter("pendencia"));
			hash = String.valueOf(request.getParameter("CodigoPendencia"));
			if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)) {
				// Consulta dados da pendencia
				pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, idPendencia);

				List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisIntimacoesCitacoes(idPendencia, UsuarioSessao.getUsuarioDt().getId_Serventia());

				if (listaResponsavelPendencia != null && listaResponsavelPendencia.size() > 0) {

					for (Iterator iterator = listaResponsavelPendencia.iterator(); iterator.hasNext();) {
						PendenciaResponsavelDt pendRespDt = (PendenciaResponsavelDt) iterator.next();
						if (pendRespDt != null && pendRespDt.getId_UsuarioResponsavel() != null && !pendRespDt.getId_UsuarioResponsavel().equals("")) {
							pendenciaDt.addResponsavel(pendRespDt);
							pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
						}
					}

				} else
					request.setAttribute("MensagemErro", "Não é possivel trocar responsável da pendência.");
			} else{
				throw new MensagemException("Não foi possível visualizar pendência. executar()");
			}
			break;

		case Configuracao.Salvar:
			request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Responsável da pendência");
			break;

		case Configuracao.SalvarResultado:
			Mensagem = pendenciaResponsavelne.verificarTrocaResponsavelProcessoParteAdvogado(pendenciaResponsavelDt, Id_UsuarioServentiaAtual, UsuarioSessao.getUsuarioDt());

			if (Mensagem.length() == 0) {
				if (UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())) {
					pendenciaResponsavelne.salvarTrocaResponsavelPendencia(pendenciaResponsavelDt, Id_UsuarioServentiaAtual, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), pendenciaDt.getId_Processo());
					request.setAttribute("MensagemOk", "Troca de Responsável Efetuada com Sucesso.");
					if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE) {
						request.getSession().setAttribute("TrocouResponsavel", "S");
					}

					// atualiza objeto da tela
					pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, pendenciaDt.getId());

					List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisIntimacoesCitacoes(pendenciaDt.getId(), UsuarioSessao.getUsuarioDt().getId_Serventia());
					if (listaResponsavelPendencia != null && listaResponsavelPendencia.size() > 0) {
						for (Iterator iterator = listaResponsavelPendencia.iterator(); iterator.hasNext();) {
							PendenciaResponsavelDt pendRespDt = (PendenciaResponsavelDt) iterator.next();
							if (pendRespDt != null && pendRespDt.getId_UsuarioResponsavel() != null && !pendRespDt.getId_UsuarioResponsavel().equals("")) {
								pendenciaDt.addResponsavel(pendRespDt);
								pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
							}
						}
					}

					request.getSession().removeAttribute("id_UsuarioServentiaAtual");
					pendenciaResponsavelDt = new PendenciaResponsavelDt();

				} else
					throw new MensagemException("Não foi possível visualizar pendência. executar()");
			} else
				request.setAttribute("MensagemErro", Mensagem);

			break;

		case Configuracao.Curinga6:
			idPendencia = String.valueOf(request.getParameter("pendencia"));
			hash = String.valueOf(request.getParameter("CodigoPendencia"));
			if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)) {
				pendenciaResponsavelne.marcarIntimacaoDistribuida(idPendencia, UsuarioSessao.getUsuarioDt().getGrupoCodigo(), pendenciaResponsavelDt.getId_UsuarioLog(), pendenciaResponsavelDt.getIpComputadorLog());
				redireciona(response, "Usuario?PaginaAtual=-10");
				return;
			} else
				throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");

			// Consulta possíveis usuários que podem ser novos responsáveis para uma pendencia
		case (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo") == null) {
				//lisNomeBusca = new ArrayList();
				//lisDescricao = new ArrayList();
				String[] lisNomeBusca = {"Usuário"};
				String[] lisDescricao = {"Usuário","Serventia"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_UsuarioServentia");
				request.setAttribute("tempBuscaDescricao", "UsuarioServentia");
				request.setAttribute("tempBuscaPrograma", "UsuarioServentia");
				request.setAttribute("tempRetorno", "PendenciaUsuarioServentiaResponsavel");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp = "";
				stTemp = pendenciaResponsavelne.consultarUsuariosServentiaJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
									
				enviarJSON(response, stTemp);
									
				return;
			}
			break;
		default:
			break;
		}

		request.getSession().setAttribute("Pendenciadt", pendenciaDt);
		request.getSession().setAttribute("PendenciaResponsaveldt", pendenciaResponsavelDt);
		request.getSession().setAttribute("PendenciaResponsavelne", pendenciaResponsavelne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
