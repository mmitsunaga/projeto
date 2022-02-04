package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.TarefaStatusDt;
import br.gov.go.tj.projudi.ne.TarefaStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class TarefaStatusCt extends TarefaStatusCtGen{

    /**
     * 
     */
    private static final long serialVersionUID = -4639865650697156564L;

//
	public int Permissao(){
		return TarefaStatusDt.CodigoPermissao;
	}

public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

	TarefaStatusDt TarefaStatusdt;
	TarefaStatusNe TarefaStatusne;


	List tempList=null; 
	String Mensagem="";
	String stId="";

	String stAcao="/WEB-INF/jsptjgo/TarefaStatus.jsp";

	String stNomeBusca1 = "";
	if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

	//--------------------------------------------------------------------------
	//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
	//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
	//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
	//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
	//--------------------------------------------------------------------------
	request.setAttribute("tempPrograma","TarefaStatus");




	TarefaStatusne =(TarefaStatusNe)request.getSession().getAttribute("TarefaStatusne");
	if (TarefaStatusne == null )  TarefaStatusne = new TarefaStatusNe();  


	TarefaStatusdt =(TarefaStatusDt)request.getSession().getAttribute("TarefaStatusdt");
	if (TarefaStatusdt == null )  TarefaStatusdt = new TarefaStatusDt();  

	TarefaStatusdt.setTarefaStatus( request.getParameter("TarefaStatus")); 
	TarefaStatusdt.setTarefaStatusCodigo( request.getParameter("TarefaStatusCodigo")); 

	TarefaStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
	TarefaStatusdt.setIpComputadorLog(request.getRemoteAddr());


	request.setAttribute("PaginaAnterior",paginaatual);
	request.setAttribute("MensagemOk", "");
	request.setAttribute("MensagemErro", "");
	//é a página padrão
	request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
	switch (paginaatual) {
		case Configuracao.ExcluirResultado: //Excluir
				TarefaStatusne.excluir(TarefaStatusdt); 
				request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
			break;

		case Configuracao.Imprimir: 
			break;
		case Configuracao.Localizar: //localizar
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Digite a Descrição"};
				String[] lisDescricao = {"TarefaStatus"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_TarefaStatus");
				request.setAttribute("tempBuscaDescricao", "TarefaStatus");
				request.setAttribute("tempBuscaPrograma", "Status de Tarefa");	
				request.setAttribute("tempRetorno","TarefaStatus");
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", Configuracao.Localizar);
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";			
				stTemp = TarefaStatusne.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);				
				enviarJSON(response, stTemp);							
				return;			
			}
			break;
		case Configuracao.Novo: 
			TarefaStatusdt.limpar();
			break;
		case Configuracao.SalvarResultado: 
				Mensagem=TarefaStatusne.Verificar(TarefaStatusdt); 
				if (Mensagem.length()==0){
					TarefaStatusne.salvar(TarefaStatusdt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
			break;
//--------------------------------------------------------------------------------//
		default:
			stId = request.getParameter("Id_TarefaStatus");
			if (stId != null && !stId.isEmpty())
				if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( TarefaStatusdt.getId()))){
					TarefaStatusdt.limpar();
					TarefaStatusdt = TarefaStatusne.consultarId(stId);
				}
			break;
	}

	request.getSession().setAttribute("TarefaStatusdt",TarefaStatusdt );
	request.getSession().setAttribute("TarefaStatusne",TarefaStatusne );

	RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
	dis.include(request, response);
}

}
