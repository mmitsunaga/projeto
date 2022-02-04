package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.TarefaPrioridadeDt;
import br.gov.go.tj.projudi.ne.TarefaPrioridadeNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class TarefaPrioridadeCt extends TarefaPrioridadeCtGen{

    /**
     * 
     */
    private static final long serialVersionUID = -2386851557558394904L;

//

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		TarefaPrioridadeDt TarefaPrioridadedt;
		TarefaPrioridadeNe TarefaPrioridadene;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/TarefaPrioridade.jsp";

    	String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","TarefaPrioridade");




		TarefaPrioridadene =(TarefaPrioridadeNe)request.getSession().getAttribute("TarefaPrioridadene");
		if (TarefaPrioridadene == null )  TarefaPrioridadene = new TarefaPrioridadeNe();  


		TarefaPrioridadedt =(TarefaPrioridadeDt)request.getSession().getAttribute("TarefaPrioridadedt");
		if (TarefaPrioridadedt == null )  TarefaPrioridadedt = new TarefaPrioridadeDt();  

		TarefaPrioridadedt.setTarefaPrioridade( request.getParameter("TarefaPrioridade")); 

		TarefaPrioridadedt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		TarefaPrioridadedt.setIpComputadorLog(request.getRemoteAddr());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					TarefaPrioridadene.excluir(TarefaPrioridadedt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Digite o TarefaPrioridade"};
					String[] lisDescricao = {"TarefaPrioridade"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_TarefaPrioridade");
					request.setAttribute("tempBuscaDescricao", "TarefaPrioridade");
					request.setAttribute("tempBuscaPrograma", "TarefaPrioridade");	
					request.setAttribute("tempRetorno","TarefaPrioridade");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
				String stTemp="";
				stTemp = TarefaPrioridadene.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
					
				
				return;			
				}
				break;
			case Configuracao.Novo: 
				TarefaPrioridadedt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=TarefaPrioridadene.Verificar(TarefaPrioridadedt); 
					if (Mensagem.length()==0){
						TarefaPrioridadene.salvar(TarefaPrioridadedt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_TarefaPrioridade");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( TarefaPrioridadedt.getId()))){
						TarefaPrioridadedt.limpar();
						TarefaPrioridadedt = TarefaPrioridadene.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("TarefaPrioridadedt",TarefaPrioridadedt );
		request.getSession().setAttribute("TarefaPrioridadene",TarefaPrioridadene );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
