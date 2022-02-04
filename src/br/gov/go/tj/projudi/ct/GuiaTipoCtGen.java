package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.ne.GuiaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class GuiaTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -1898823199678839078L;

    public  GuiaTipoCtGen() {

	} 
		public int Permissao(){
			return GuiaTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GuiaTipoDt GuiaTipodt;
		GuiaTipoNe GuiaTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/GuiaTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GuiaTipo");




		GuiaTipone =(GuiaTipoNe)request.getSession().getAttribute("GuiaTipone");
		if (GuiaTipone == null )  GuiaTipone = new GuiaTipoNe();  


		GuiaTipodt =(GuiaTipoDt)request.getSession().getAttribute("GuiaTipodt");
		if (GuiaTipodt == null )  GuiaTipodt = new GuiaTipoDt();  

		GuiaTipodt.setGuiaTipo( request.getParameter("GuiaTipo")); 
		GuiaTipodt.setGuiaTipoCodigo( request.getParameter("GuiaTipoCodigo")); 

		GuiaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GuiaTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					GuiaTipone.excluir(GuiaTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Novo: 
				GuiaTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=GuiaTipone.Verificar(GuiaTipodt); 
					if (Mensagem.length()==0){
						GuiaTipone.salvar(GuiaTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_GuiaTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( GuiaTipodt.getId()))){
						GuiaTipodt.limpar();
						GuiaTipodt = GuiaTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("GuiaTipodt",GuiaTipodt );
		request.getSession().setAttribute("GuiaTipone",GuiaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
