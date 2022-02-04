package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ComarcaCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 1177075620449788179L;

    public  ComarcaCtGen() {

	} 
		public int Permissao(){
			return ComarcaDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ComarcaDt Comarcadt;
		ComarcaNe Comarcane;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/Comarca.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","Comarca");
		request.setAttribute("tempBuscaId_Comarca","Id_Comarca");
		request.setAttribute("tempBuscaComarca","Comarca");
		

		request.setAttribute("tempRetorno","Comarca");



		Comarcane =(ComarcaNe)request.getSession().getAttribute("Comarcane");
		if (Comarcane == null )  Comarcane = new ComarcaNe();  


		Comarcadt =(ComarcaDt)request.getSession().getAttribute("Comarcadt");
		if (Comarcadt == null )  Comarcadt = new ComarcaDt();  

		Comarcadt.setComarca( request.getParameter("Comarca")); 
		Comarcadt.setComarcaCodigo( request.getParameter("ComarcaCodigo")); 

		Comarcadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Comarcadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Comarcane.excluir(Comarcadt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ComarcaLocalizar.jsp";
				tempList =Comarcane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaComarca", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", Comarcane.getQuantidadePaginas());
					Comarcadt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				Comarcadt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=Comarcane.Verificar(Comarcadt); 
				if (Mensagem.length()==0){
					Comarcane.salvar(Comarcadt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_Comarca");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( Comarcadt.getId()))){
						Comarcadt.limpar();
						Comarcadt = Comarcane.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("Comarcadt",Comarcadt );
		request.getSession().setAttribute("Comarcane",Comarcane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
