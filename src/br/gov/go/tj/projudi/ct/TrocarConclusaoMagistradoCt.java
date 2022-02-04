package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla as trocas de Responsáveis por Pendencia.
 * 
 * @author lsbernardes
 * 18/03/2011
 */
public class TrocarConclusaoMagistradoCt extends Controle {
	
	private static final long serialVersionUID = 6612779659847261085L;

    public int Permissao(){
		return 805;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual)
			throws MensagemException, Exception, ServletException, IOException {

		PendenciaDt pendenciaDt;
		PendenciaResponsavelNe pendenciaResponsavelne;

		String Mensagem = "";
		String id_ServentiaCargoAtual = null;
		String id_ServentiaCargoNovoResponsavel = null;
		String qtdConclusoes = "";

		String stAcao = "/WEB-INF/jsptjgo/TrocarConclusaoMagistrado.jsp";
		request.setAttribute("tempRetorno", "TrocarConclusaoMagistrado");
		request.setAttribute("tempPrograma", "TrocarConclusaoMagistrado");

		pendenciaResponsavelne = (PendenciaResponsavelNe) request.getSession().getAttribute("PendenciaResponsavelne");
		if (pendenciaResponsavelne == null) pendenciaResponsavelne = new PendenciaResponsavelNe();

		pendenciaDt = (PendenciaDt) request.getSession().getAttribute("Pendenciadt");
		if (pendenciaDt == null) 
			pendenciaDt = new PendenciaDt();
		
		//quantidade concluseoes
		if (request.getParameter("qtdConclusoes") != null && !request.getParameter("qtdConclusoes").equals("")) {
			qtdConclusoes = request.getParameter("qtdConclusoes");
		} else if (request.getParameter("Quantidade") != null && !request.getParameter("Quantidade").equals("")){
			qtdConclusoes = request.getParameter("Quantidade");
		}

		//responsável que será substituido
		if (request.getParameter("id_UsuarioServentiaCargoAtual") != null && !request.getParameter("id_UsuarioServentiaCargoAtual").equals("")) {
			id_ServentiaCargoAtual = request.getParameter("id_UsuarioServentiaCargoAtual");
		} else if (request.getParameter("AtualResponsavel") != null && !request.getParameter("AtualResponsavel").equals("")){
			id_ServentiaCargoAtual = request.getParameter("AtualResponsavel");
		}
		
		//novo responsável
		if (request.getParameter("id_UsuarioServentiaCargoNovoResponsavel") != null && !request.getParameter("id_UsuarioServentiaCargoNovoResponsavel").equals("")) {
			id_ServentiaCargoNovoResponsavel = request.getParameter("id_UsuarioServentiaCargoNovoResponsavel");
		}else if (request.getParameter("NovoResponsavel") != null && !request.getParameter("NovoResponsavel").equals("")){
			id_ServentiaCargoNovoResponsavel = request.getParameter("NovoResponsavel");
		}
		
		request.setAttribute("Quantidade", qtdConclusoes);
		request.setAttribute("AtualResponsavel", id_ServentiaCargoAtual);
		request.setAttribute("NovoResponsavel", id_ServentiaCargoNovoResponsavel);
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

		//Inicializa Troca de Responsável
		case Configuracao.Novo:
			//Consulta dados da pendencia
			List listaMagistradosServentia = pendenciaResponsavelne.consultarJuizesServentia(UsuarioSessao.getUsuarioDt().getId_Serventia());
			
			pendenciaDt = new PendenciaDt();
			if (listaMagistradosServentia != null && listaMagistradosServentia.size() > 0){
				for (Iterator iterator = listaMagistradosServentia.iterator(); iterator.hasNext();) {
					ServentiaCargoDt responsavel = (ServentiaCargoDt) iterator.next();
					if (responsavel != null && responsavel.getId() != null && !responsavel.getId().equals("")){
						pendenciaDt.addMagistradoResponsavelAtual(responsavel);
					}
				}
			
			} else
				request.setAttribute("MensagemErro", "Não é possivel trocar responsável das Conclusões.");
			break;

		case Configuracao.Salvar:
			//Se o atributo idConclusao estiver no request é porque é uma troca de uma conclusão de determinado processo. Senão é a troca de uma quantidade de conclusões de um magistrado para outro.
			if(request.getParameter("idConclusao") == null){
				request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Responsável das "+ qtdConclusoes +" Conclusões mais antigas do Magistrado Selecionado.");
			} else {
				request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Responsável da conclusão.");
				request.setAttribute("idConclusao", request.getParameter("idConclusao"));
				request.setAttribute("nomeMagistradoAtual", request.getParameter("nomeMagistradoAtual"));
				stAcao = "/WEB-INF/jsptjgo/TrocarConclusaoProcesso.jsp";
			}
			
			break;

		case Configuracao.SalvarResultado:
			//Se o atributo idConclusao estiver no request é porque é uma troca de uma conclusão de determinado processo. Senão é a troca de uma quantidade de conclusões de um magistrado para outro.
			if(request.getParameter("idConclusao") == null){
				Mensagem = pendenciaResponsavelne.verificarTrocaResponsavelConclusaoLote(id_ServentiaCargoAtual, id_ServentiaCargoNovoResponsavel, qtdConclusoes);
				
				if (Mensagem.length() == 0) {
					pendenciaResponsavelne.salvarTrocaResponsavelPendenciaConclusaoLote(id_ServentiaCargoAtual, id_ServentiaCargoNovoResponsavel, qtdConclusoes, UsuarioSessao.getId_Usuario(), UsuarioSessao.getUsuarioDt().getIpComputadorLog());
					request.setAttribute("MensagemOk", "A Troca de Responsável das "+qtdConclusoes+" Conclusões mais antigas foi Efetuada com Sucesso.");
					request.setAttribute("Quantidade", "");
					request.setAttribute("AtualResponsavel", "");
					request.setAttribute("NovoResponsavel", "");	
				} else 
					request.setAttribute("MensagemErro", Mensagem);
			} else {
				String idConclusao = request.getParameter("idConclusao");
				ProcessoDt processoDt = (ProcessoDt)request.getSession().getAttribute("processoDt");
				
				pendenciaResponsavelne.trocarResponsavelConclusaoProcessoPrimeiroGrau(idConclusao, id_ServentiaCargoNovoResponsavel, processoDt.getId(), UsuarioSessao);

				request.removeAttribute("nomeMagistradoAtual");
				request.removeAttribute("idConclusao");
				request.removeAttribute("Quantidade");
				request.removeAttribute("AtualResponsavel");
				request.removeAttribute("NovoResponsavel");
				redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemOk=" + "Troca de Responsável pela conclusão do processo " + processoDt.getProcessoNumero() + " efetuada com sucesso.");
			}
			
			break;
			
		case Configuracao.Curinga6:
			//Acesso à tela de troca de responsável por conclusão de um determinado processo
			ProcessoDt processoDt = (ProcessoDt)request.getSession().getAttribute("processoDt");
			PendenciaDt conclusaoDt = new PendenciaNe().consultarConclusaoAbertaProcesso(processoDt.getId(), null, null);
			if(conclusaoDt == null) {
				throw new MensagemException("O processo "+ processoDt.getProcessoNumero() +" não está concluso.");
			}
			List listaResponsaveis = new PendenciaResponsavelNe().consultarResponsaveisDetalhado(conclusaoDt.getId());
			for (int i = 0; i < listaResponsaveis.size(); i++) {
				PendenciaResponsavelDt responsavelDt = (PendenciaResponsavelDt) listaResponsaveis.get(i);
				if(responsavelDt.getId_ServentiaCargo() != null && !responsavelDt.getId_ServentiaCargo().equals("")) {
					ServentiaCargoDt servCargoDt = new ServentiaCargoNe().consultarId(responsavelDt.getId_ServentiaCargo());
					request.setAttribute("nomeMagistradoAtual", "(" + servCargoDt.getCargoTipo() + ") " + servCargoDt.getNomeUsuario() + " (" + servCargoDt.getServentia() + ")");
				}
			}
			
			if(conclusaoDt != null){
				listaMagistradosServentia = pendenciaResponsavelne.consultarJuizesServentia(processoDt.getId_Serventia());
				pendenciaDt = new PendenciaDt();
				if (listaMagistradosServentia != null && listaMagistradosServentia.size() > 0){
					for (Iterator iterator = listaMagistradosServentia.iterator(); iterator.hasNext();) {
						ServentiaCargoDt responsavel = (ServentiaCargoDt) iterator.next();
						if (responsavel != null && responsavel.getId() != null && !responsavel.getId().equals("")){
							pendenciaDt.addMagistradoResponsavelAtual(responsavel);
						}
					}
				} else {
					throw new MensagemException("Não é possível trocar responsável da conclusão.");
				}
				request.setAttribute("idConclusao", conclusaoDt.getId());
				stAcao = "/WEB-INF/jsptjgo/TrocarConclusaoProcesso.jsp";
			}
			break;
		
		default:
			break;
		}
		
		request.getSession().setAttribute("Pendenciadt", pendenciaDt);
		request.getSession().setAttribute("PendenciaResponsavelne", pendenciaResponsavelne);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
