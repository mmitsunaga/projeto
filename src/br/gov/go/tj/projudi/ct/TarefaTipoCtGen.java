package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.TarefaTipoDt;
import br.gov.go.tj.projudi.ne.TarefaTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class TarefaTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 6775563590830300083L;

    public  TarefaTipoCtGen() {

	} 
		public int Permissao(){
			return TarefaTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TarefaTipoDt TarefaTipodt;
		TarefaTipoNe TarefaTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/TarefaTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","TarefaTipo");




		TarefaTipone =(TarefaTipoNe)request.getSession().getAttribute("TarefaTipone");
		if (TarefaTipone == null )  TarefaTipone = new TarefaTipoNe();  


		TarefaTipodt =(TarefaTipoDt)request.getSession().getAttribute("TarefaTipodt");
		if (TarefaTipodt == null )  TarefaTipodt = new TarefaTipoDt();  

		TarefaTipodt.setTarefaTipo( request.getParameter("TarefaTipo")); 

		TarefaTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		TarefaTipodt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					TarefaTipone.excluir(TarefaTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				request.setAttribute("tempBuscaId_TarefaTipo","Id_TarefaTipo");
				request.setAttribute("tempBuscaTarefaTipo","TarefaTipo");
				request.setAttribute("tempRetorno","TarefaTipo");
				tempList =TarefaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaTarefaTipo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", TarefaTipone.getQuantidadePaginas());
					TarefaTipodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
				}
				break;
			case Configuracao.Novo: 
				TarefaTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=TarefaTipone.Verificar(TarefaTipodt); 
					if (Mensagem.length()==0){
						TarefaTipone.salvar(TarefaTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_TarefaTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( TarefaTipodt.getId()))){
						TarefaTipodt.limpar();
						TarefaTipodt = TarefaTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("TarefaTipodt",TarefaTipodt );
		request.getSession().setAttribute("TarefaTipone",TarefaTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
