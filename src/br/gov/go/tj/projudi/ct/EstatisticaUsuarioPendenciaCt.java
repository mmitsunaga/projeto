package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;

public class EstatisticaUsuarioPendenciaCt extends Controle {

    /**
     * 
     */
    private static final long serialVersionUID = 7977193288145658094L;

    @Override
    public int Permissao() {
        
        return 362;
    }

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
        
        String stAcao = "/WEB-INF/jsptjgo/EstatisticaUsuarioPendencia.jsp";                          
        int inRelatorio = -1;
        byte[] byTemp = null;               
        
        String stRelatorio =  request.getParameter("Relatorio");
        if (stRelatorio!=null)  inRelatorio = Funcoes.StringToInt(stRelatorio);        

        String stId_Serventia = (String)request.getParameter("Id_Serventia");      
        String stId_Usuario =  (String)request.getParameter("Id_Usuario");        
        String stAno =  (String)request.getParameter("Ano");        
        String stMes =  (String)request.getParameter("Mes");
                
        
        request.setAttribute("PaginaAnterior",paginaatual);
        request.setAttribute("Curinga","vazio");
        request.setAttribute("MensagemOk", "");
        request.setAttribute("MensagemErro", "");
        
        // a pagina padrão será a zero
        request.setAttribute("PaginaAtual","1");
//--------------------------------------------------------------------------------//
        switch (paginaatual) {
            case 1:
                switch (inRelatorio) {
                case 0:
                    byTemp = (new PendenciaNe()).relUsuariosPendencias( ProjudiPropriedades.getInstance().getCaminhoAplicacao() , stId_Serventia, stId_Usuario, stAno, stMes); 
                    
                    enviarPDF(response, byTemp, "Relatorio");
    					    				
                    byTemp = null;
                    return;
                    
                default:
                    break;
                }

//--------------------------------------------------------------------------------//
            default:

                break;
        }
        
        RequestDispatcher dis = request.getRequestDispatcher(stAcao);
        dis.include(request, response);
    }


}
