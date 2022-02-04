package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PendenciaTipoRelacionadaDt;
import br.gov.go.tj.projudi.ne.PendenciaTipoRelacionadaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class PendenciaTipoRelacionadaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 8409065540944800568L;

    public  PendenciaTipoRelacionadaCtGen() {

	} 
		public int Permissao(){
			return PendenciaTipoRelacionadaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PendenciaTipoRelacionadaDt PendenciaTipoRelacionadadt;
		PendenciaTipoRelacionadaNe PendenciaTipoRelacionadane;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/PendenciaTipoRelacionada.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PendenciaTipoRelacionada");
		request.setAttribute("ListaUlLiPendenciaTipoRelacionada","");




		PendenciaTipoRelacionadane =(PendenciaTipoRelacionadaNe)request.getSession().getAttribute("PendenciaTipoRelacionadane");
		if (PendenciaTipoRelacionadane == null )  PendenciaTipoRelacionadane = new PendenciaTipoRelacionadaNe();  


		PendenciaTipoRelacionadadt =(PendenciaTipoRelacionadaDt)request.getSession().getAttribute("PendenciaTipoRelacionadadt");
		if (PendenciaTipoRelacionadadt == null )  PendenciaTipoRelacionadadt = new PendenciaTipoRelacionadaDt();  

		PendenciaTipoRelacionadadt.setId_PendenciaTipoPrincipal( request.getParameter("Id_PendenciaTipoPrincipal")); 
		PendenciaTipoRelacionadadt.setPendenciaTipoPrincipal( request.getParameter("PendenciaTipoPrincipal")); 
		PendenciaTipoRelacionadadt.setId_PendenciaTipoRelacao( request.getParameter("Id_PendenciaTipoRelacao")); 
		PendenciaTipoRelacionadadt.setPendenciaTipoRelacao( request.getParameter("PendenciaTipoRelacao")); 

		PendenciaTipoRelacionadadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PendenciaTipoRelacionadadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request,PendenciaTipoRelacionadadt.getId_PendenciaTipoPrincipal() ,PendenciaTipoRelacionadane, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				PendenciaTipoRelacionadadt.limpar();
				request.setAttribute("ListaUlLiPendenciaTipoRelacionada", ""); 
				break;
			case Configuracao.SalvarResultado: 
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=PendenciaTipoRelacionadane.Verificar(PendenciaTipoRelacionadadt); 
				if (Mensagem.length()==0){
					PendenciaTipoRelacionadane.salvarMultiplo(PendenciaTipoRelacionadadt, idsDados); 
					localizar(request,PendenciaTipoRelacionadadt.getId_PendenciaTipoPrincipal() ,PendenciaTipoRelacionadane, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				break;
		}

		request.getSession().setAttribute("PendenciaTipoRelacionadadt",PendenciaTipoRelacionadadt );
		request.getSession().setAttribute("PendenciaTipoRelacionadane",PendenciaTipoRelacionadane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, PendenciaTipoRelacionadaNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarPendenciaTipoPendenciaTipoUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiPendenciaTipoRelacionada", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
