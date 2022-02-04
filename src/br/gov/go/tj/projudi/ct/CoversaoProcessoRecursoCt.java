package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.ne.RecursoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

/**
 * Servlet que controla a conversão de um processo em recurso.
 *  
 * @author lsbernardes
 *
 */
public class CoversaoProcessoRecursoCt extends RecursoCtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3371968452550658266L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoDt processoDt;
		RecursoNe Recursone;

		String Mensagem = "";
		String fluxo = "";
		String stAcao = "";
		
		request.setAttribute("tempPrograma", "CoversaoProcessoRecurso");
		

		Recursone = (RecursoNe) request.getSession().getAttribute("Recursone");
		if (Recursone == null) Recursone = new RecursoNe();

		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");

		
		
		LogDt logDt = new LogDt();

		logDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		logDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());		

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		fluxo = request.getParameter("fluxo");
		
		if (fluxo.equalsIgnoreCase("1")){
			stAcao = "/WEB-INF/jsptjgo/ConverterProcessoRecurso.jsp";
		} else if (fluxo.equalsIgnoreCase("2")){
			stAcao = "/WEB-INF/jsptjgo/ConverterRecursoProcesso.jsp";
		}

		switch (paginaatual) {

			//Inicializa dados
			case Configuracao.Novo:
				if (fluxo.equalsIgnoreCase("1")){
					stAcao = "/WEB-INF/jsptjgo/ConverterProcessoRecurso.jsp";
				} else if (fluxo.equalsIgnoreCase("2")){
					if (processoDt.getId_Recurso() == null || processoDt.getId_Recurso().length()==0){
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemErro=Processo não é um Recurso.");
						return;
					} else
						stAcao = "/WEB-INF/jsptjgo/ConverterRecursoProcesso.jsp";
				}
				
				
				break;

			case Configuracao.ExcluirResultado: //Excluir
				
				break;

			//Localiza recursos para autuar
			case Configuracao.Localizar:
			
				break;

			case Configuracao.Salvar:
				//Se não tem data de recebimento refere-se a autuação
				if (fluxo.equalsIgnoreCase("1")){
					request.setAttribute("MensagemOk", "Clique para confirmar a Conversão do processo em recurso.");
				} else if (fluxo.equalsIgnoreCase("2")){
					request.setAttribute("MensagemOk", "Clique para confirmar a Conversão do recurso em processo.");
				}
				request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				
				break;

			case Configuracao.SalvarResultado:

				if (fluxo.equalsIgnoreCase("1")){
					Mensagem = Recursone.VerificarConversaoProcessoRecurso(processoDt);
					if (Mensagem.length() == 0) {
						if (processoDt.getId() == null || !processoDt.getId().equals("")) {
							Recursone.converterProcessoRecurso(processoDt, UsuarioSessao.getUsuarioDt(), logDt);
							redireciona(response, "Recurso?PaginaAtual=6&Id_Recurso=" + processoDt.getRecursoDt().getId()+"&MensagemOk=Processo Convertido em Recurso com Sucesso. Processo encontra-se aguardando autuação.");
						} else {
							request.setAttribute("MensagemErro", "Processo não encontrado!");
						}
					} else {
						request.setAttribute("MensagemErro", Mensagem);
					}
				} else if (fluxo.equalsIgnoreCase("2")){
					Recursone.ConverterRecursoAutuadoProcesso(processoDt.getRecursoDt(), UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), processoDt.getRecursoDt().getId_ServentiaRecurso() , UsuarioSessao.getUsuarioDt().getIpComputadorLog(), UsuarioSessao.getId_Usuario()  );
					processoDt.setRecursoDt(null);
					processoDt.setId_Recurso("null");
					redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&PassoBusca=2&MensagemOk=Recurso Convertido em Processo com Sucesso.");
					return;
				}

				break;

			//Inicia autuação de recurso
			case Configuracao.Curinga6:
				break;
			case Configuracao.Curinga7:
				break;

		}
		
		request.setAttribute("fluxo", fluxo);
		request.getSession().setAttribute("processoDt", processoDt);
		request.getSession().setAttribute("Recursone", Recursone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
