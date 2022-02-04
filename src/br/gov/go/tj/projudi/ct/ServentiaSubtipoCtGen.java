package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.ServentiaSubtipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaSubtipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 2511774375328087795L;

    public  ServentiaSubtipoCtGen() {

	} 
		public int Permissao(){
			return ServentiaSubtipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaSubtipoDt ServentiaSubtipodt;
		ServentiaSubtipoNe ServentiaSubtipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ServentiaSubtipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ServentiaSubtipo");
		request.setAttribute("tempBuscaId_ServentiaSubtipo","Id_ServentiaSubtipo");
		request.setAttribute("tempBuscaServentiaSubtipo","ServentiaSubtipo");
		

		request.setAttribute("tempRetorno","ServentiaSubtipo");



		ServentiaSubtipone =(ServentiaSubtipoNe)request.getSession().getAttribute("ServentiaSubtipone");
		if (ServentiaSubtipone == null )  ServentiaSubtipone = new ServentiaSubtipoNe();  


		ServentiaSubtipodt =(ServentiaSubtipoDt)request.getSession().getAttribute("ServentiaSubtipodt");
		if (ServentiaSubtipodt == null )  ServentiaSubtipodt = new ServentiaSubtipoDt();  

		ServentiaSubtipodt.setServentiaSubtipo( request.getParameter("ServentiaSubtipo")); 
		ServentiaSubtipodt.setServentiaSubtipoCodigo( request.getParameter("ServentiaSubtipoCodigo")); 

		ServentiaSubtipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaSubtipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				ServentiaSubtipone.excluir(ServentiaSubtipodt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/ServentiaSubtipoLocalizar.jsp";
				tempList =ServentiaSubtipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaServentiaSubtipo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", ServentiaSubtipone.getQuantidadePaginas());
					ServentiaSubtipodt.limpar();
				}else{ 
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					request.setAttribute("PaginaAtual", Configuracao.Editar);
				}
				break;
			case Configuracao.Novo: 
				ServentiaSubtipodt.limpar();
				request.setAttribute("PaginaAtual",Configuracao.Editar);
				break;
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem=ServentiaSubtipone.Verificar(ServentiaSubtipodt); 
				if (Mensagem.length()==0){
					ServentiaSubtipone.salvar(ServentiaSubtipodt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				stId = request.getParameter("Id_ServentiaSubtipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ServentiaSubtipodt.getId()))){
						ServentiaSubtipodt.limpar();
						ServentiaSubtipodt = ServentiaSubtipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ServentiaSubtipodt",ServentiaSubtipodt );
		request.getSession().setAttribute("ServentiaSubtipone",ServentiaSubtipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
