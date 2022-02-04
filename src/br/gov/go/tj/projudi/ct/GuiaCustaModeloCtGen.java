package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GuiaCustaModeloDt;
import br.gov.go.tj.projudi.dt.GuiaModeloDt;
import br.gov.go.tj.projudi.ne.GuiaCustaModeloNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class GuiaCustaModeloCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 3731607800216743287L;

    public  GuiaCustaModeloCtGen() {

	} 
		public int Permissao(){
			return GuiaCustaModeloDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		GuiaCustaModeloDt GuiaCustaModelodt;
		GuiaCustaModeloNe GuiaCustaModelone;


		List tempList=null; 
		String Mensagem="";
		String stAcao="/WEB-INF/jsptjgo/GuiaCustaModelo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","GuiaCustaModelo");
		request.setAttribute("ListaUlLiGuiaCustaModelo","");




		GuiaCustaModelone =(GuiaCustaModeloNe)request.getSession().getAttribute("GuiaCustaModelone");
		if (GuiaCustaModelone == null )  GuiaCustaModelone = new GuiaCustaModeloNe();  


		GuiaCustaModelodt =(GuiaCustaModeloDt)request.getSession().getAttribute("GuiaCustaModelodt");
		if (GuiaCustaModelodt == null )  GuiaCustaModelodt = new GuiaCustaModeloDt();  

		GuiaCustaModelodt.setGuiaCustaModelo( request.getParameter("GuiaCustaModelo")); 
		GuiaCustaModelodt.setId_GuiaModelo( request.getParameter("Id_GuiaModelo")); 
		GuiaCustaModelodt.setGuiaModelo( request.getParameter("GuiaModelo")); 
		GuiaCustaModelodt.setId_Custa( request.getParameter("Id_Custa")); 
		GuiaCustaModelodt.setCodigoRegimento( request.getParameter("CodigoRegimento")); 

		GuiaCustaModelodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		GuiaCustaModelodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.Localizar: //localizar
				localizar(request,GuiaCustaModelodt.getId_GuiaModelo() ,GuiaCustaModelone, UsuarioSessao);
				break;
			case Configuracao.Novo: 
				GuiaCustaModelodt.limpar();
				request.setAttribute("ListaUlLiGuiaCustaModelo", ""); 
				break;
			case Configuracao.SalvarResultado: 
				String[] idsDados = request.getParameterValues("chkEditar");
				Mensagem=GuiaCustaModelone.Verificar(GuiaCustaModelodt); 
				if (Mensagem.length()==0){
					GuiaCustaModelone.salvarMultiplo(GuiaCustaModelodt, idsDados); 
					localizar(request,GuiaCustaModelodt.getId_GuiaModelo() ,GuiaCustaModelone, UsuarioSessao);
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			case (GuiaModeloDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				request.setAttribute("tempBuscaId_GuiaModelo","Id_GuiaModelo");
				request.setAttribute("tempBuscaGuiaModelo","GuiaModelo");
				request.setAttribute("tempRetorno","GuiaCustaModelo");
				stAcao="/WEB-INF/jsptjgo/GuiaModeloLocalizar.jsp";
				tempList =GuiaCustaModelone.consultarDescricaoGuiaModelo(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaGuiaModelo", tempList); 
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", GuiaCustaModelone.getQuantidadePaginas());
					request.setAttribute("tempRetorno","GuiaCustaModelo?PaginaAtual="+ Configuracao.Localizar );
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
//--------------------------------------------------------------------------------//
			default:
				break;
		}

		request.getSession().setAttribute("GuiaCustaModelodt",GuiaCustaModelodt );
		request.getSession().setAttribute("GuiaCustaModelone",GuiaCustaModelone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void localizar(HttpServletRequest request, String id, GuiaCustaModeloNe objNe, UsuarioNe UsuarioSessao) throws Exception{
				String tempDados =objNe.consultarCustaGuiaModeloUlLiCheckBox( id);
				if (tempDados.length()>0){
					request.setAttribute("ListaUlLiGuiaCustaModelo", tempDados); 
					//é gerado o código do pedido, assim o submit so pode ser executado uma unica vez
					request.setAttribute("__Pedido__", UsuarioSessao.getPedido());
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
	}
}
