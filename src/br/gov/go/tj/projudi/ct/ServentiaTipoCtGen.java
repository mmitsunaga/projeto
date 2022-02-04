package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.ServentiaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -8401121797818720059L;

    public  ServentiaTipoCtGen() {

	} 
		public int Permissao(){
			return ServentiaTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaTipoDt ServentiaTipodt;
		ServentiaTipoNe ServentiaTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ServentiaTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ServentiaTipo");




		ServentiaTipone =(ServentiaTipoNe)request.getSession().getAttribute("ServentiaTipone");
		if (ServentiaTipone == null )  ServentiaTipone = new ServentiaTipoNe();  


		ServentiaTipodt =(ServentiaTipoDt)request.getSession().getAttribute("ServentiaTipodt");
		if (ServentiaTipodt == null )  ServentiaTipodt = new ServentiaTipoDt();  

		ServentiaTipodt.setServentiaTipo( request.getParameter("ServentiaTipo")); 
		ServentiaTipodt.setServentiaTipoCodigo( request.getParameter("ServentiaTipoCodigo")); 
		if (request.getParameter("Externa") != null)
			ServentiaTipodt.setExterna( request.getParameter("Externa")); 
		else ServentiaTipodt.setExterna("false");

		ServentiaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ServentiaTipone.excluir(ServentiaTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				ServentiaTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ServentiaTipone.Verificar(ServentiaTipodt); 
					if (Mensagem.length()==0){
						ServentiaTipone.salvar(ServentiaTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ServentiaTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ServentiaTipodt.getId()))){
						ServentiaTipodt.limpar();
						ServentiaTipodt = ServentiaTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ServentiaTipodt",ServentiaTipodt );
		request.getSession().setAttribute("ServentiaTipone",ServentiaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
