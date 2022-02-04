package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ne.ExpedirMandadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;

//---------------------------------------------------------
public class ExpedirMandadoCt extends ExpedirMandadoCtGen{

    /**
     * 
     */
    private static final long serialVersionUID = -6430125993840184112L;
    
    private ExpedirMandadoNe expedirMandadoNe;
    
    public ExpedirMandadoCt() {
    	this.expedirMandadoNe = new ExpedirMandadoNe();
    }
    
    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
    	
		String stAcao = "";
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
    }
    
    public ExpedirMandadoNe getExpedirMandadoNe() {
    	return this.expedirMandadoNe;
    }
    
    public void setExpedirMandadoNe(ExpedirMandadoNe expedirMandadoNe) {
    	this.expedirMandadoNe = expedirMandadoNe;
    }
}
