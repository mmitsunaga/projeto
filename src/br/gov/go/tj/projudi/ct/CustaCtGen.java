package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArrecadacaoCustaDt;
import br.gov.go.tj.projudi.dt.CustaDt;
import br.gov.go.tj.projudi.ne.CustaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class CustaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 769353397474441304L;

    public  CustaCtGen() {

	} 
		public int Permissao(){
			return CustaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		CustaDt Custadt;
		CustaNe Custane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Custa.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Custa");




		Custane =(CustaNe)request.getSession().getAttribute("Custane");
		if (Custane == null )  Custane = new CustaNe();  


		Custadt =(CustaDt)request.getSession().getAttribute("Custadt");
		if (Custadt == null )  Custadt = new CustaDt();  

		Custadt.setCusta( request.getParameter("Custa")); 
		Custadt.setCustaCodigo( request.getParameter("CustaCodigo")); 
		Custadt.setCodigoRegimento( request.getParameter("CodigoRegimento")); 
		Custadt.setCodigoRegimentoValor( request.getParameter("CodigoRegimentoValor")); 
		Custadt.setPorcentagem( request.getParameter("Porcentagem")); 
		Custadt.setMinimo( request.getParameter("Minimo")); 
		Custadt.setId_ArrecadacaoCusta( request.getParameter("Id_ArrecadacaoCusta")); 
		Custadt.setArrecadacaoCusta( request.getParameter("ArrecadacaoCusta")); 

		Custadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Custadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Custane.excluir(Custadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/CustaLocalizar.jsp";
				request.setAttribute("tempBuscaId_Custa","Id_Custa");
				request.setAttribute("tempBuscaCusta","Custa");
				request.setAttribute("tempRetorno","Custa");
				tempList =Custane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaCusta", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Custane.getQuantidadePaginas());
					Custadt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				Custadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Custane.Verificar(Custadt); 
					if (Mensagem.length()==0){
						Custane.salvar(Custadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (ArrecadacaoCustaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_ArrecadacaoCusta","Id_ArrecadacaoCusta");
					request.setAttribute("tempBuscaArrecadacaoCusta","ArrecadacaoCusta");
					request.setAttribute("tempRetorno","Custa");
					stAcao="/WEB-INF/jsptjgo/ArrecadacaoCustaLocalizar.jsp";
					tempList =Custane.consultarDescricaoArrecadacaoCusta(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaArrecadacaoCusta", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", Custane.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Custa");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Custadt.getId()))){
						Custadt.limpar();
						Custadt = Custane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Custadt",Custadt );
		request.getSession().setAttribute("Custane",Custane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
