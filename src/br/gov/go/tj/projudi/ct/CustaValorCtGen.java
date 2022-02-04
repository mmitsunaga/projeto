package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.dt.CustaValorDt;
import br.gov.go.tj.projudi.ne.CustaValorNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CustaValorCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 2153351280247424000L;

    public  CustaValorCtGen() {

	} 
		public int Permissao(){
			return CustaValorDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CustaValorDt CustaValordt;
		CustaValorNe CustaValorne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/CustaValor.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","CustaValor");




		CustaValorne =(CustaValorNe)request.getSession().getAttribute("CustaValorne");
		if (CustaValorne == null )  CustaValorne = new CustaValorNe();  


		CustaValordt =(CustaValorDt)request.getSession().getAttribute("CustaValordt");
		if (CustaValordt == null )  CustaValordt = new CustaValorDt();  

		CustaValordt.setCustaValor( request.getParameter("CustaValor")); 
		CustaValordt.setCustaValorCodigo( request.getParameter("CustaValorCodigo")); 
		CustaValordt.setLimiteMin( request.getParameter("LimiteMin")); 
		CustaValordt.setLimiteMax( request.getParameter("LimiteMax")); 
		CustaValordt.setValorCusta( request.getParameter("ValorCusta")); 
		CustaValordt.setId_Custa( request.getParameter("Id_Custa")); 
		CustaValordt.setCusta( request.getParameter("Custa")); 

		CustaValordt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		CustaValordt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					CustaValorne.excluir(CustaValordt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/CustaValorLocalizar.jsp";
				request.setAttribute("tempBuscaId_CustaValor","Id_CustaValor");
				request.setAttribute("tempBuscaCustaValor","CustaValor");
				request.setAttribute("tempRetorno","CustaValor");
				tempList = CustaValorne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaCustaValor", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					CustaValordt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
				request.setAttribute("QuantidadePaginas", CustaValorne.getQuantidadePaginas());
				break;
			case Configuracao.Novo: 
				CustaValordt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=CustaValorne.Verificar(CustaValordt); 
					if (Mensagem.length()==0){
						CustaValorne.salvar(CustaValordt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (CustaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_Custa","Id_Custa");
					request.setAttribute("tempBuscaCusta","Custa");
					request.setAttribute("tempRetorno","CustaValor");
					stAcao="/WEB-INF/jsptjgo/CustaLocalizar.jsp";
					tempList = CustaValorne.consultarDescricaoCusta(tempNomeBusca, PosicaoPaginaAtual);
					request.setAttribute("PaginaAtual", paginaatual);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", CustaValorne.getQuantidadePaginas());
					if (tempList.size()>0){
						request.setAttribute("ListaCusta", tempList); 
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_CustaValor");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( CustaValordt.getId()))){
						CustaValordt.limpar();
						CustaValordt = CustaValorne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("CustaValordt",CustaValordt );
		request.getSession().setAttribute("CustaValorne",CustaValorne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
