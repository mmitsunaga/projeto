package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.ne.RgOrgaoExpedidorNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RgOrgaoExpedidorCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 6402259996114125253L;

    public  RgOrgaoExpedidorCtGen() {

	} 
		public int Permissao(){
			return RgOrgaoExpedidorDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		RgOrgaoExpedidorDt RgOrgaoExpedidordt;
		RgOrgaoExpedidorNe RgOrgaoExpedidorne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/RgOrgaoExpedidor.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","RgOrgaoExpedidor");
		request.setAttribute("tempBuscaId_RgOrgaoExpedidor","Id_RgOrgaoExpedidor");
		request.setAttribute("tempBuscaRgOrgaoExpedidor","RgOrgaoExpedidor");
		
		request.setAttribute("tempBuscaId_Estado","Id_Estado");
		request.setAttribute("tempBuscaEstado","Estado");

		request.setAttribute("tempRetorno","RgOrgaoExpedidor");



		RgOrgaoExpedidorne =(RgOrgaoExpedidorNe)request.getSession().getAttribute("RgOrgaoExpedidorne");
		if (RgOrgaoExpedidorne == null )  RgOrgaoExpedidorne = new RgOrgaoExpedidorNe();  


		RgOrgaoExpedidordt =(RgOrgaoExpedidorDt)request.getSession().getAttribute("RgOrgaoExpedidordt");
		if (RgOrgaoExpedidordt == null )  RgOrgaoExpedidordt = new RgOrgaoExpedidorDt();  

		RgOrgaoExpedidordt.setRgOrgaoExpedidor( request.getParameter("RgOrgaoExpedidor")); 
		RgOrgaoExpedidordt.setSigla( request.getParameter("Sigla")); 
		RgOrgaoExpedidordt.setId_Estado( request.getParameter("Id_Estado")); 
		RgOrgaoExpedidordt.setEstado( request.getParameter("Estado")); 
		RgOrgaoExpedidordt.setEstadoCodigo( request.getParameter("EstadoCodigo")); 
		RgOrgaoExpedidordt.setUf( request.getParameter("Uf")); 

		RgOrgaoExpedidordt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		RgOrgaoExpedidordt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				RgOrgaoExpedidorne.excluir(RgOrgaoExpedidordt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/RgOrgaoExpedidorLocalizar.jsp";
				tempList =RgOrgaoExpedidorne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaRgOrgaoExpedidor", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", RgOrgaoExpedidorne.getQuantidadePaginas());
					RgOrgaoExpedidordt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				RgOrgaoExpedidordt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=RgOrgaoExpedidorne.Verificar(RgOrgaoExpedidordt); 
				if (Mensagem.length()==0){
					RgOrgaoExpedidorne.salvar(RgOrgaoExpedidordt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
						tempList =RgOrgaoExpedidorne.consultarDescricaoEstado(tempNomeBusca, PosicaoPaginaAtual);
						if (tempList.size()>0){
							request.setAttribute("ListaEstado", tempList); 
							request.setAttribute("PaginaAtual", paginaatual);
							request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
							request.setAttribute("QuantidadePaginas", RgOrgaoExpedidorne.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_RgOrgaoExpedidor");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( RgOrgaoExpedidordt.getId()))){
						RgOrgaoExpedidordt.limpar();
						RgOrgaoExpedidordt = RgOrgaoExpedidorne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("RgOrgaoExpedidordt",RgOrgaoExpedidordt );
		request.getSession().setAttribute("RgOrgaoExpedidorne",RgOrgaoExpedidorne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
