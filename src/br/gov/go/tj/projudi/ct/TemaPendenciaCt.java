package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTemaDt;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoTemaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.ValidacaoUtil;

public class TemaPendenciaCt extends Controle {

	private static final long serialVersionUID = -3307586197556894158L;

	private static final int CodigoPermissao = 919;
	
	@Override
	public int Permissao() {
		return CodigoPermissao;
	}
	
	@SuppressWarnings("unchecked")
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		List<ProcessoDt> listaProcessos = new ArrayList<>();
		
		PendenciaNe pendenciaNe = new PendenciaNe();
		ProcessoTemaNe processoTemaNe = new ProcessoTemaNe();
		
		String stAcao="/WEB-INF/jsptjgo/TemaPendencia.jsp";

		if (ValidacaoUtil.isNaoVazio(request.getSession().getAttribute("ListaProcessos"))){
			listaProcessos = (List<ProcessoDt>) request.getSession().getAttribute("ListaProcessos");
		}
		
		request.setAttribute("tempPrograma","TemaPendencia");
		request.setAttribute("tempRetorno", "TemaPendencia");
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		
		switch (paginaatual) {
			
			case Configuracao.Localizar:
				listaProcessos = processoTemaNe.consultarProcessoComTodosTemasTransitadoJulgado();
				break;
			
			case Configuracao.Salvar:
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				break;
				
			case Configuracao.SalvarResultado:
				String[] arrIdsProcessosSelecionados = request.getParameterValues("processos");
				if (ValidacaoUtil.isNaoVazio(arrIdsProcessosSelecionados)){
					for (String id : arrIdsProcessosSelecionados){
						ProcessoDt p = getProcessoDaSessaoPorId(request, id);
						if (ValidacaoUtil.isNaoNulo(p)){
							try {
								// Seta informações para o LOG
								p.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
								p.setId_UsuarioServentia(UsuarioSessao.getId_UsuarioServentia());
								p.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
								// Cria a pendência para o processo
								List<ProcessoTemaDt> listaProcessoTemaDt = processoTemaNe.consultarTemasProcessoPorIdProcesso(id, null);
								pendenciaNe.gerarPendenciaVerificarTemaTransitoJulgado(p, listaProcessoTemaDt, p.getId_Serventia());
							} catch (Exception ex){
								throw new MensagemException(ex.getMessage());
							}
							listaProcessos = processoTemaNe.consultarProcessoComTodosTemasTransitadoJulgado();
							request.setAttribute("MensagemOk", "Pendência(s) gerada(s) com sucesso");
						} else {
							request.setAttribute("MensagemErro", "O processo selecionado não foi encontrado em sessão. Tente novamente.");
						}
					}					
				} else {
					request.setAttribute("MensagemErro", "Selecione pelo menos um processo");
				}
				break;
			
			default:
				request.getSession().setAttribute("ListaProcessos", null);
				break;				
		}
				
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("isPodeGerarPendencia", isPodeGerarPendencia(paginaatual));
		request.getSession().setAttribute("ListaProcessos", listaProcessos);
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	/**
	 * Obtém os dados do processo pelo id que estão na sessão
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ProcessoDt getProcessoDaSessaoPorId(HttpServletRequest request, String id) throws Exception {		
		List<ProcessoDt> listaProcessos = (List<ProcessoDt>) request.getSession().getAttribute("ListaProcessos");
		return listaProcessos.stream().filter(o -> o.getId().equals(id)).collect(Collectors.toList()).get(0);
	}
	
	/**
	 * 
	 * @param request
	 * @param paginaAtual
	 * @return
	 */
	public boolean isPodeGerarPendencia(int paginaAtual){
		return (paginaAtual == Configuracao.Salvar);
	}
	
}
