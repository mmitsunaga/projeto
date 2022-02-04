package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoFisicoDt;
import br.gov.go.tj.projudi.ne.ProcessoFisicoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoFisicoCt extends Controle {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5856199087253428968L;

	public int Permissao() {
		return 755;
	}

	@Override
	public void executar(HttpServletRequest request,	HttpServletResponse response, UsuarioNe UsuarioSessao,	int paginaatual, String nomebusca, String posicaopaginaatual)	throws Exception, ServletException, IOException {
		ProcessoFisicoNe pfne =new ProcessoFisicoNe();
		
		String stAcao ="";
		request.setAttribute("TituloPagina", "Consulta de Processos Físicos Sigilosos");
		
		ProcessoFisicoDt pf = null;
		
		String numero =  request.getParameter("numeroProcesso");
				

		switch (paginaatual) {
		
			case Configuracao.Localizar:
				
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				if (numero.isEmpty()) {
					request.setAttribute("MensagemErro","Informe o número do processo!");
					stAcao = "/WEB-INF/jsptjgo/ProcessoFisicoLocalizar.jsp";
				} else {				
					
					if (UsuarioSessao.isAdvogado()){
						if (pfne.verificarAcesso(numero, UsuarioSessao.getUsuarioDt().getOabNumero() + UsuarioSessao.getUsuarioDt().getOabEstado())){
							pf = pfne.getProcessoFisico(numero);
						}
					}else{
						//se não é adv, para no numero da oab 999999 (6 noves, para ser liberado no spg o processos que não são de segredo de justiça)
						if (pfne.verificarAcesso(numero,"999999")){
							pf = pfne.getProcessoFisico(numero);
						}
					}
												
					stAcao = "/WEB-INF/jsptjgo/ProcessoFisico.jsp";
				}
				break;
			case Configuracao.Novo:		
				request.setAttribute("tempRetorno", "Processo Físico Localizar");
				stAcao = "/WEB-INF/jsptjgo/ProcessoFisicoLocalizar.jsp";					
				break;			
		default:
			break;
		}
		request.getSession().setAttribute("processoFisicoDt", pf);
			
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
}
				
	}


