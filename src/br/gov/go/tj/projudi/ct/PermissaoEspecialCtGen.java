package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PermissaoEspecialDt;
import br.gov.go.tj.projudi.ne.PermissaoEspecialNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class PermissaoEspecialCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 1983830314088518098L;

    public  PermissaoEspecialCtGen() {

	} 
		public int Permissao(){
			return PermissaoEspecialDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		PermissaoEspecialDt PermissaoEspecialdt;
		PermissaoEspecialNe PermissaoEspecialne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/PermissaoEspecial.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","PermissaoEspecial");




		PermissaoEspecialne =(PermissaoEspecialNe)request.getSession().getAttribute("PermissaoEspecialne");
		if (PermissaoEspecialne == null )  PermissaoEspecialne = new PermissaoEspecialNe();  


		PermissaoEspecialdt =(PermissaoEspecialDt)request.getSession().getAttribute("PermissaoEspecialdt");
		if (PermissaoEspecialdt == null )  PermissaoEspecialdt = new PermissaoEspecialDt();  

		PermissaoEspecialdt.setPermissaoEspecial( request.getParameter("PermissaoEspecial")); 
		PermissaoEspecialdt.setPermissaoEspecialCodigo( request.getParameter("PermissaoEspecialCodigo")); 

		PermissaoEspecialdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		PermissaoEspecialdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					PermissaoEspecialne.excluir(PermissaoEspecialdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/PermissaoEspecialLocalizar.jsp";
				request.setAttribute("tempBuscaId_PermissaoEspecial","Id_PermissaoEspecial");
				request.setAttribute("tempBuscaPermissaoEspecial","PermissaoEspecial");
				request.setAttribute("tempRetorno","PermissaoEspecial");
				tempList =PermissaoEspecialne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaPermissaoEspecial", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", PermissaoEspecialne.getQuantidadePaginas());
					PermissaoEspecialdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				PermissaoEspecialdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=PermissaoEspecialne.Verificar(PermissaoEspecialdt); 
					if (Mensagem.length()==0){
						PermissaoEspecialne.salvar(PermissaoEspecialdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_PermissaoEspecial");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( PermissaoEspecialdt.getId()))){
						PermissaoEspecialdt.limpar();
						PermissaoEspecialdt = PermissaoEspecialne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("PermissaoEspecialdt",PermissaoEspecialdt );
		request.getSession().setAttribute("PermissaoEspecialne",PermissaoEspecialne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
