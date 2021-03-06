package br.gov.go.tj.projudi.temp;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.ct.Controle;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CertificadosImportarCt extends Controle {



    /**
     * 
     */
    private static final long serialVersionUID = -7036666809074758554L;

    @Override
    public int Permissao() {
        
        return 443;
    }

    @Override
    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
        
    	CertificadosImportar obImportar = null;
        String stAcao="/WEB-INF/jsptjgo/temp/ImportarCertificados.jsp";
          
        obImportar = CertificadosImportar.getInstacia();
        String stQtdThread = request.getParameter("QtdThreads");
        if(stQtdThread==null) stQtdThread="1";
        long loQtdThread = Funcoes.StringToInt(stQtdThread);
        
        request.setAttribute("PaginaAnterior",paginaatual);
        request.setAttribute("MensagemOk", "");
        request.setAttribute("MensagemErro", "");
        request.setAttribute("Status","");
        request.setAttribute("QtdPronto","");
        request.setAttribute("QtdTotal","");
        request.setAttribute("Inicio","");
        request.setAttribute("Atual","");
        request.setAttribute("Previsao","");
        request.setAttribute("QtdThreads","");
        //? a p?gina padr?o
        request.setAttribute("PaginaAtual",Configuracao.Editar);
        
//--------------------------------------------------------------------------------//
        if (paginaatual==Configuracao.Novo) {                
            
            if(obImportar.getStStatus().equalsIgnoreCase("Parado")){
                obImportar.Rodar(); 
                for (int i=0; i<loQtdThread;i++){
//                    //System.out.println(" Antes do START................." + i);             
                    obImportar.adicionaThread( new Thread( new CertificadosImportarRunnable()));
//                    //System.out.println(" Depois do START................." + i);                      
                }
                
            }
            
        }else if(paginaatual==Configuracao.Curinga6) {
            if(obImportar.getStStatus().equalsIgnoreCase("Rodando")){
                obImportar.Parar(); 
            }
        }        
        
        if (obImportar!=null){
            request.setAttribute("Status",obImportar.getStStatus());
	            request.setAttribute("QtdPronto",obImportar.getLoQtdPronto() +"   -    "+obImportar.getPorcentagem()+" %");
	            request.setAttribute("QtdTotal",obImportar.getLoQtdTotal());
	            request.setAttribute("Inicio",obImportar.getDataInicio());
	            request.setAttribute("Atual",Funcoes.DataHora(new Date()));
	            request.setAttribute("Previsao",obImportar.getPrevisao() + " " + obImportar.getPrevisao1());
	            request.setAttribute("QtdThreads",obImportar.getQtdThreads());
        }
        
        RequestDispatcher dis = request.getRequestDispatcher(stAcao);
        dis.include(request, response);

    }

}
