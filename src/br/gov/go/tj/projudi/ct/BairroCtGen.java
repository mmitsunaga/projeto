package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.ne.BairroNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class BairroCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 2549385525161551743L;

    public  BairroCtGen() {

	} 
		public int Permissao(){
			return BairroDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		BairroDt Bairrodt;
		BairroNe Bairrone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Bairro.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Bairro");
		request.setAttribute("tempBuscaId_Bairro","Id_Bairro");
		request.setAttribute("tempBuscaBairro","Bairro");
		
		request.setAttribute("tempBuscaId_Cidade","Id_Cidade");
		request.setAttribute("tempBuscaCidade","Cidade");

		request.setAttribute("tempRetorno","Bairro");



		Bairrone =(BairroNe)request.getSession().getAttribute("Bairrone");
		if (Bairrone == null )  Bairrone = new BairroNe();  


		Bairrodt =(BairroDt)request.getSession().getAttribute("Bairrodt");
		if (Bairrodt == null )  Bairrodt = new BairroDt();  

		Bairrodt.setBairro( request.getParameter("Bairro")); 
		Bairrodt.setBairroCodigo( request.getParameter("BairroCodigo")); 
		Bairrodt.setId_Cidade( request.getParameter("Id_Cidade")); 
		Bairrodt.setCidade( request.getParameter("Cidade")); 
		Bairrodt.setUf( request.getParameter("Uf"));
		Bairrodt.setCodigoSPG( request.getParameter("CodigoSPG"));

		Bairrodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Bairrodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Bairrone.excluir(Bairrodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/BairroLocalizar.jsp";
				tempList =Bairrone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaBairro", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Bairrone.getQuantidadePaginas());
					Bairrodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				Bairrodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=Bairrone.Verificar(Bairrodt); 
				if (Mensagem.length()==0){
					Bairrone.salvar(Bairrodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_Bairro");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Bairrodt.getId()))){
						Bairrodt.limpar();
						Bairrodt = Bairrone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Bairrodt",Bairrodt );
		request.getSession().setAttribute("Bairrone",Bairrone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
