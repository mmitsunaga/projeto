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
import br.gov.go.tj.utils.Funcoes;

public class ArrecadacaoCustaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = -4864101889142655667L;

    public  ArrecadacaoCustaCtGen() {

	} 
		public int Permissao(){
			return ArrecadacaoCustaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ArrecadacaoCustaDt ArrecadacaoCustadt;
		ArrecadacaoCustaNe ArrecadacaoCustane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ArrecadacaoCusta.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
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
		ArrecadacaoCustadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ArrecadacaoCustane.excluir(ArrecadacaoCustadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ArrecadacaoCustaLocalizar.jsp";
				request.setAttribute("tempBuscaId_ArrecadacaoCusta","Id_ArrecadacaoCusta");
				request.setAttribute("tempBuscaArrecadacaoCusta","ArrecadacaoCusta");
				request.setAttribute("tempRetorno","ArrecadacaoCusta");
				tempList =ArrecadacaoCustane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaArrecadacaoCusta", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ArrecadacaoCustane.getQuantidadePaginas());
					ArrecadacaoCustadt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
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
//--------------------------------------------------------------------------------//
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
