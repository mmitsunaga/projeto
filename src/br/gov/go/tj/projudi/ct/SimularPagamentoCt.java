package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.MensagemException;

public class SimularPagamentoCt extends HttpServlet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5124592404777071293L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			ProjudiPropriedades projudiConfiguration = ProjudiPropriedades.getInstance();
			
			if( projudiConfiguration.getPropriedade(ProjudiPropriedades.servidorSPG).equals("desenv.gyn.tjgo") ) {
				GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe();
				String numeroGuia = "";
				String dataPagamento = "";
				
				if(request.getParameter("numeroGuia") != null) numeroGuia = request.getParameter("numeroGuia");
				if(request.getParameter("dataPagamento") != null) dataPagamento = request.getParameter("dataPagamento");
				
				try {
					
					GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoNumeroGuia(numeroGuia);
					
					if (guiaEmissaoDt != null) {
						guiaEmissaoNe.simularPagamentoGuia_NAO_UTILIZAR(guiaEmissaoDt, numeroGuia, dataPagamento);
						request.setAttribute("Mensagem", "Guia paga com sucesso.");	
					} else {
						request.setAttribute("Mensagem", "Guia não localizada com o número " + numeroGuia + ".");
					}
				} catch (Exception ex) {
					request.setAttribute("Mensagem", ex.getMessage());            
				}
				
				RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/jsptjgo/Mensagem.jsp");
		        dis.include(request, response);
			}
			else {
				throw new MensagemException("Sem permissão!");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
