package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ComarcaCt extends ComarcaCtGen{

    /**
     * 
     */
    private static final long serialVersionUID = 913555747015890147L;


	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ComarcaDt Comarcadt;
		ComarcaNe Comarcane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Comarca.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Comarca");
		request.setAttribute("tempBuscaId_Comarca","Id_Comarca");
		request.setAttribute("tempBuscaComarca","Comarca");
		

		request.setAttribute("tempRetorno","Comarca");
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		Comarcane =(ComarcaNe)request.getSession().getAttribute("Comarcane");
		if (Comarcane == null )  Comarcane = new ComarcaNe();  


		Comarcadt =(ComarcaDt)request.getSession().getAttribute("Comarcadt");
		if (Comarcadt == null )  Comarcadt = new ComarcaDt();  

		Comarcadt.setComarca( request.getParameter("Comarca")); 
		Comarcadt.setComarcaCodigo( request.getParameter("ComarcaCodigo")); 

		Comarcadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Comarcadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Comarca");			
					request.setAttribute("tempRetorno","Comarca");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Comarcane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("Comarcadt",Comarcadt );
		request.getSession().setAttribute("Comarcane",Comarcane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
