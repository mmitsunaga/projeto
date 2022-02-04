package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoEscalaNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaCargoEscalaCtGen extends Controle {

    private static final long serialVersionUID = 2133866038593042594L;

    public  ServentiaCargoEscalaCtGen() {

	} 
		public int Permissao(){
			return ServentiaCargoEscalaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaCargoEscalaDt ServentiaCargoEscaladt;
		ServentiaCargoEscalaNe ServentiaCargoEscalane;

		String stId = "";
		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/ServentiaCargoEscala.jsp";

		request.setAttribute("tempPrograma","ServentiaCargoEscala");
		request.setAttribute("tempBuscaId_ServentiaCargoEscala","Id_ServentiaCargoEscala");
		request.setAttribute("tempBuscaServentiaCargoEscala","ServentiaCargoEscala");
		request.setAttribute("ListaUlLiServentiaCargoEscala","");
		request.setAttribute("tempBuscaId_ServentiaCargo","Id_ServentiaCargo");
		request.setAttribute("tempBuscaServentiaCargo","ServentiaCargo");
		request.setAttribute("tempBuscaId_Escala","Id_Escala");
		request.setAttribute("tempBuscaEscala","Escala");

		request.setAttribute("tempRetorno","ServentiaCargoEscala");

		ServentiaCargoEscalane =(ServentiaCargoEscalaNe)request.getSession().getAttribute("ServentiaCargoEscalane");
		if (ServentiaCargoEscalane == null )  ServentiaCargoEscalane = new ServentiaCargoEscalaNe();  


		ServentiaCargoEscaladt =(ServentiaCargoEscalaDt)request.getSession().getAttribute("ServentiaCargoEscaladt");
		if (ServentiaCargoEscaladt == null )  ServentiaCargoEscaladt = new ServentiaCargoEscalaDt();  

		ServentiaCargoEscaladt.setServentiaCargoEscala( request.getParameter("ServentiaCargoEscala")); 
		ServentiaCargoEscaladt.setId_ServentiaCargo( request.getParameter("Id_ServentiaCargo")); 
		ServentiaCargoEscaladt.setServentiaCargo( request.getParameter("ServentiaCargo")); 
		ServentiaCargoEscaladt.setId_Escala( request.getParameter("Id_Escala")); 
		ServentiaCargoEscaladt.setEscala( request.getParameter("Escala")); 
		if (request.getParameter("Ativo") != null)
			ServentiaCargoEscaladt.setAtivo( request.getParameter("Ativo")); 
		else ServentiaCargoEscaladt.setAtivo("false");

		ServentiaCargoEscaladt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaCargoEscaladt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		switch (paginaatual) {
			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
//				localizar(request,ServentiaCargoEscaladt.getId_ServentiaCargo() ,ServentiaCargoEscalane, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				ServentiaCargoEscaladt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=ServentiaCargoEscalane.Verificar(ServentiaCargoEscaladt); 
				if (Mensagem.length()==0){
					ServentiaCargoEscalane.salvarMultiplo(ServentiaCargoEscaladt, idsDados); 
//					localizar(request,ServentiaCargoEscaladt.getId_ServentiaCargo() ,ServentiaCargoEscalane, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;

			default:
				stId = request.getParameter("Id_ServentiaCargoEscala");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase(ServentiaCargoEscaladt.getId()))){
						ServentiaCargoEscaladt.limpar();
						ServentiaCargoEscaladt = ServentiaCargoEscalane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ServentiaCargoEscaladt",ServentiaCargoEscaladt );
		request.getSession().setAttribute("ServentiaCargoEscalane",ServentiaCargoEscalane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
