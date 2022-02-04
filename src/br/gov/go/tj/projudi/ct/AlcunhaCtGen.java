package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AlcunhaDt;
import br.gov.go.tj.projudi.ne.AlcunhaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class AlcunhaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 3021284504200040825L;

    public  AlcunhaCtGen() {

	} 
		public int Permissao(){
			return AlcunhaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AlcunhaDt Alcunhadt;
		AlcunhaNe Alcunhane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Alcunha.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Alcunha");




		Alcunhane =(AlcunhaNe)request.getSession().getAttribute("Alcunhane");
		if (Alcunhane == null )  Alcunhane = new AlcunhaNe();  


		Alcunhadt =(AlcunhaDt)request.getSession().getAttribute("Alcunhadt");
		if (Alcunhadt == null )  Alcunhadt = new AlcunhaDt();  

		Alcunhadt.setAlcunha( request.getParameter("Alcunha")); 

		Alcunhadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Alcunhadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					Alcunhane.excluir(Alcunhadt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/AlcunhaLocalizar.jsp";
				request.setAttribute("tempBuscaId_Alcunha","Id_Alcunha");
				request.setAttribute("tempBuscaAlcunha","Alcunha");
				request.setAttribute("tempRetorno","Alcunha");
				tempList =Alcunhane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaAlcunha", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Alcunhane.getQuantidadePaginas());
					Alcunhadt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					stAcao="/WEB-INF/jsptjgo/Alcunha.jsp";
				}
				break;
			case Configuracao.Novo: 
				Alcunhadt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=Alcunhane.Verificar(Alcunhadt); 
					if (Mensagem.length()==0){
						Alcunhane.salvar(Alcunhadt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_Alcunha");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Alcunhadt.getId()))){
						Alcunhadt.limpar();
						Alcunhadt = Alcunhane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Alcunhadt",Alcunhadt );
		request.getSession().setAttribute("Alcunhane",Alcunhane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
