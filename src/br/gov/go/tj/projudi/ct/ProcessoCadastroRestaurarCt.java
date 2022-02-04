package br.gov.go.tj.projudi.ct;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoCadastroDt;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ProcessoCadastroRestaurarCt extends Controle {

	private static final long serialVersionUID = -7395820095433129259L;

	public int Permissao() {
		return 657;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception {

		ProcessoCadastroDt processoDt;
		ProcessoNe Processone;
		String stAcao = "/WEB-INF/jsptjgo/ProcessoCadastroRestaurar.jsp";

		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		if (request.getParameter("tempRetorno") != null)
			request.setAttribute("tempRetorno", request.getParameter("tempRetorno"));
		else
			request.setAttribute("tempRetorno", request.getAttribute("tempRetorno"));
		Processone = (ProcessoNe) request.getSession().getAttribute("Processone");
		if (Processone == null)	Processone = new ProcessoNe();
		if (request.getParameter("nomeArquivo") != null)
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
			request.setAttribute("nomeArquivo", "");

		switch (paginaatual) {
			case Configuracao.Novo:
				break;
			case Configuracao.Curinga6:
				String conteudoArquivo = getConteudoArquivo(request);
				if (conteudoArquivo != null && conteudoArquivo.length() > 60) {
					processoDt = Processone.carregarProcesso(new ProcessoCadastroDt(), conteudoArquivo);
					request.getSession().setAttribute("ProcessoCadastroDt", processoDt);
				} else
					request.setAttribute("MensagemErro", "Selecione um Arquivo para prosseguir.");
				String[] vetor = conteudoArquivo.split("\n");
				if (vetor.length>=4){
					String conteudo = "";
					for (int i = 0; i < vetor.length - 1; i++) {
						conteudo += vetor[i] + "\n";
					}
					String[] stHash = vetor[vetor.length-1].split("@;#");
					if (stHash.length>=1 && Funcoes.GeraHashMd5(conteudo).equals(stHash[1])) {
						redireciona(response, request.getAttribute("tempRetorno").toString());
					} else {
						request.setAttribute("MensagemErro", "Arquivo de dados corrompido");
					}
				}else{
					request.setAttribute("MensagemErro", "Arquivo de dados corrompido");
				}
				break;
		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);

	}
}
