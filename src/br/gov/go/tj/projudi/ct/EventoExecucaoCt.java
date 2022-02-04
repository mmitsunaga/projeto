package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.ne.EventoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class EventoExecucaoCt extends EventoExecucaoCtGen{

    /**
     * 
     */
    private static final long serialVersionUID = 6960922171929869412L;

//
    

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EventoExecucaoDt EventoExecucaodt;
		EventoExecucaoNe EventoExecucaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";
		
		
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao="/WEB-INF/jsptjgo/EventoExecucao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","EventoExecucao");




		EventoExecucaone =(EventoExecucaoNe)request.getSession().getAttribute("EventoExecucaone");
		if (EventoExecucaone == null )  EventoExecucaone = new EventoExecucaoNe();  


		EventoExecucaodt =(EventoExecucaoDt)request.getSession().getAttribute("EventoExecucaodt");
		if (EventoExecucaodt == null )  EventoExecucaodt = new EventoExecucaoDt();  

		EventoExecucaodt.setEventoExecucao( request.getParameter("EventoExecucao")); 
		EventoExecucaodt.setAlteraLocal( request.getParameter("AlteraLocal")); 
		EventoExecucaodt.setAlteraRegime( request.getParameter("AlteraRegime")); 
		EventoExecucaodt.setValorNegativo( request.getParameter("ValorNegativo"));
		EventoExecucaodt.setObservacao( request.getParameter("Observacao")); 
		EventoExecucaodt.setEventoExecucaoCodigo( request.getParameter("EventoExecucaoCodigo")); 
		EventoExecucaodt.setId_EventoExecucaoTipo( request.getParameter("Id_EventoExecucaoTipo")); 
		EventoExecucaodt.setEventoExecucaoTipo( request.getParameter("EventoExecucaoTipo")); 
		EventoExecucaodt.setId_EventoExecucaoStatus( request.getParameter("Id_EventoExecucaoStatus")); 
		EventoExecucaodt.setEventoExecucaoStatus( request.getParameter("EventoExecucaoStatus")); 

		EventoExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EventoExecucaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
				case (EventoExecucaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Descrição"};
						String[] lisDescricao = {"Descrição"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_EventoExecucaoTipo");
						request.setAttribute("tempBuscaDescricao","EventoExecucaoTipo");
						request.setAttribute("tempBuscaPrograma","Tipo de Evento Execução");			
						request.setAttribute("tempRetorno","EventoExecucao");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (EventoExecucaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
						String stTemp="";
						stTemp = EventoExecucaone.consultarDescricaoEventoExecucaoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
							
							enviarJSON(response, stTemp);
							
						
						return;								
					}
					break;
					
				case (EventoExecucaoStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					if (request.getParameter("Passo")==null){
						String[] lisNomeBusca = {"Situação"};
						String[] lisDescricao = {"EventoExecucaoStatus"};
						stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
						request.setAttribute("tempBuscaId","Id_EventoExecucaoStatus");
						request.setAttribute("tempBuscaDescricao","EventoExecucaoStatus");
						request.setAttribute("tempBuscaPrograma","Situações de Processo de Execução Penal");			
						request.setAttribute("tempRetorno","EventoExecucao");		
						request.setAttribute("tempDescricaoId","Id");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
						request.setAttribute("PaginaAtual", (EventoExecucaoStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
						request.setAttribute("PosicaoPaginaAtual", "0");
						request.setAttribute("QuantidadePaginas", "0");
						request.setAttribute("lisNomeBusca", lisNomeBusca);
						request.setAttribute("lisDescricao", lisDescricao);
					} else {
						String stTemp="";
						stTemp = EventoExecucaone.consultarDescricaoEventoExecucaoStatusJSON(stNomeBusca1, PosicaoPaginaAtual);
							
							enviarJSON(response, stTemp);
							
						
						return;								
					}
					break;
					
//--------------------------------------------------------------------------------//
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("EventoExecucaodt",EventoExecucaodt );
		request.getSession().setAttribute("EventoExecucaone",EventoExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}


}
