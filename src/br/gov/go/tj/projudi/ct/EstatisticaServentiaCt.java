package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Funcoes;

public class EstatisticaServentiaCt extends Controle {

    /**
     * 
     */
    private static final long serialVersionUID = 8945792751405674510L;

    @Override
    public int  Permissao() {
        
        return 362;
    }

    @Override
    public void  executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
       
        String stAcao = "/WEB-INF/jsptjgo/EstatisticaServentiaProcesso.jsp";                          
        int inRelatorio = -1;
        byte[] byTemp = null;
        
        
        
        String stRelatorio =  request.getParameter("Relatorio");
        if (stRelatorio!=null)  inRelatorio = Funcoes.StringToInt(stRelatorio);        

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
                    byTemp = (new ServentiaNe()).relProcessosServentia( ProjudiPropriedades.getInstance().getCaminhoAplicacao() ); 
                                       
                    enviarPDF(response, byTemp,"Relatorio");
                    
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
