package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.QuantidadeDias;
import br.gov.go.tj.projudi.ne.EstatisticaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

public class EstatisticaCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8945792751405674510L;

	@Override
	public int Permissao() {
		
		return 565;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {

		String stAcao = "/WEB-INF/jsptjgo/Estatistica.jsp";

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		// a pagina padrão será a zero
		//request.setAttribute("PaginaAtual", "1");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Novo:
			    
			    Calendar cal = Calendar.getInstance();
			    new EstatisticaNe().gerarEstatisticaMes(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1);
				request.setAttribute("MensagemOk", "Estatística Gerada com Sucesso");
				
				break;
			case Configuracao.Localizar:
				EstatisticaNe est = new EstatisticaNe();
				String id_serv = request.getParameter("id_serv");
				String dias = request.getParameter("dias");
				String fluxo = request.getParameter("fluxo");
				String stTempe = "[]";
				if (id_serv==null || dias==null) {
					return;
				}else {
					if("Comarca".equals(fluxo)) {
						stTempe = est.gerarJsonDistribuicaoProcessoXDiasComarca(id_serv, QuantidadeDias.getQuantidadeDias(dias));
					}else if("ComarcaServentia".equals(fluxo)) {
						stTempe = est.gerarJsonDistribuicaoProcessoXDiasComarcaServentia(id_serv, QuantidadeDias.getQuantidadeDias(dias));
					}else if("ComarcaServentiaCargo".equals(fluxo)) {
							stTempe = est.gerarJsonDistribuicaoProcessoXDiasComarcaServentiaServentiaCargo(id_serv, QuantidadeDias.getQuantidadeDias(dias));
					}
					if(stTempe.isEmpty()) {
						throw new MensagemException("Dados não encontrados para " + dias + " dias");
					}
				}
				enviarJSON(response, stTempe);
				return;
				
//--------------------------------------------------------------------------------//
			default:

				break;
		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
