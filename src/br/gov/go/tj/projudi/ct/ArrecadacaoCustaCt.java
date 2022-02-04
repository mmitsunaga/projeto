package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.ne.ArrecadacaoCustaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ArrecadacaoCustaCt extends ArrecadacaoCustaCtGen{

    private static final long serialVersionUID = 2890976644855729183L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ArrecadacaoCustaDt ArrecadacaoCustadt;
		ArrecadacaoCustaNe ArrecadacaoCustane;

		List tempList=null; 
		String Mensagem="";
		String stId="";
		String stAcao="/WEB-INF/jsptjgo/ArrecadacaoCusta.jsp";
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("tempPrograma","ArrecadacaoCusta");

		ArrecadacaoCustane =(ArrecadacaoCustaNe)request.getSession().getAttribute("ArrecadacaoCustane");
		if (ArrecadacaoCustane == null )  ArrecadacaoCustane = new ArrecadacaoCustaNe();  

		ArrecadacaoCustadt =(ArrecadacaoCustaDt)request.getSession().getAttribute("ArrecadacaoCustadt");
		if (ArrecadacaoCustadt == null )  ArrecadacaoCustadt = new ArrecadacaoCustaDt();  

		ArrecadacaoCustadt.setArrecadacaoCusta( request.getParameter("ArrecadacaoCusta")); 
		ArrecadacaoCustadt.setArrecadacaoCustaCodigo( request.getParameter("ArrecadacaoCustaCodigo")); 
		ArrecadacaoCustadt.setCodigoArrecadacao( request.getParameter("CodigoArrecadacao")); 
		//ArrecadacaoCustadt.setCusta( request.getParameter("Custa")); 

		ArrecadacaoCustadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ArrecadacaoCustadt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ArrecadacaoCustane.excluir(ArrecadacaoCustadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
				
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArrecadacaoCusta"};
					String[] lisDescricao = {"ArrecadacaoCusta"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ArrecadacaoCusta");
					request.setAttribute("tempBuscaDescricao", "ArrecadacaoCusta");
					request.setAttribute("tempBuscaPrograma", "ArrecadacaoCusta");
					request.setAttribute("tempRetorno", "ArrecadacaoCusta");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = ArrecadacaoCustane.consultarPorDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
				
			case Configuracao.Novo: 
				ArrecadacaoCustadt.limpar();
				break;
				
			case Configuracao.SalvarResultado: 
					Mensagem=ArrecadacaoCustane.Verificar(ArrecadacaoCustadt); 
					if (Mensagem.length()==0){
						ArrecadacaoCustane.salvar(ArrecadacaoCustadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				
			default:
				stId = request.getParameter("Id_ArrecadacaoCusta");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ArrecadacaoCustadt.getId()))){
						ArrecadacaoCustadt.limpar();
						ArrecadacaoCustadt = ArrecadacaoCustane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ArrecadacaoCustadt",ArrecadacaoCustadt );
		request.getSession().setAttribute("ArrecadacaoCustane",ArrecadacaoCustane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
