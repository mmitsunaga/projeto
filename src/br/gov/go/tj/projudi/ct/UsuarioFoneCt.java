package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioFoneDt;
import br.gov.go.tj.projudi.ne.UsuarioFoneNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

//---------------------------------------------------------
public class UsuarioFoneCt extends UsuarioFoneCtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1517130098083015954L;

//
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		UsuarioFoneDt UsuarioFonedt;
		UsuarioFoneNe UsuarioFonene;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";
		List listaFones=null;

		String stAcao="/WEB-INF/jsptjgo/CelularUsuario.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","UsuarioFone");


		UsuarioFonene =(UsuarioFoneNe)request.getSession().getAttribute("UsuarioFonene");
		if (UsuarioFonene == null )  UsuarioFonene = new UsuarioFoneNe();  


		UsuarioFonedt =(UsuarioFoneDt)request.getSession().getAttribute("UsuarioFonedt");
		if (UsuarioFonedt == null )  UsuarioFonedt = new UsuarioFoneDt();  

		UsuarioFonedt.setId_Usuario( request.getParameter("Id_Usuario")); 
		UsuarioFonedt.setUsuario( request.getParameter("Usuario")); 
		UsuarioFonedt.setImei( request.getParameter("Imei")); 
		UsuarioFonedt.setFone( request.getParameter("Fone")); 
		UsuarioFonedt.setCodigo( request.getParameter("Codigo")); 
		UsuarioFonedt.setCodigoValidade( request.getParameter("CodigoValidade")); 
		UsuarioFonedt.setDataPedido( request.getParameter("DataPedido")); 
		UsuarioFonedt.setDataLiberacao( request.getParameter("DataLiberacao")); 

		UsuarioFonedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		UsuarioFonedt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				UsuarioFonene.excluir(UsuarioFonedt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
			case Configuracao.Excluir:
				stId = request.getParameter("Id_UsuarioFone");
				UsuarioFonedt = UsuarioFonene.consultarId(stId);
				request.getSession().setAttribute("UsuarioFonedt",UsuarioFonedt );
				request.setAttribute("Mensagem","Confirma a Exclusão do Fone:" + UsuarioFonedt.getFone() + " Imei: " + UsuarioFonedt.getFone() );
				break;
			case Configuracao.Curinga6:
				 stId = request.getParameter("Id_UsuarioFone");
				 String bloquear = request.getParameter("bloquear");
				 
				 if("true".equals(bloquear)) {
					 UsuarioFonene.bloquear(stId);
				 }else if("false".equals(bloquear)) {
					 UsuarioFonene.liberar(stId, UsuarioSessao.getId_Usuario());
				 }			
				break;
			
		}
		
		listaFones =  UsuarioFonene.consultarFones(UsuarioSessao.getId_Usuario());
		request.setAttribute("ListaCelulares",listaFones );
		request.getSession().setAttribute("UsuarioFonene",UsuarioFonene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
