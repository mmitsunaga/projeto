package br.gov.go.tj.projudi.ct;

 import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EventoExecucaoDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt;
import br.gov.go.tj.projudi.ne.EventoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class EventoExecucaoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 6151898342446370006L;

    public  EventoExecucaoCtGen() {

	} 
		public int Permissao(){
			return EventoExecucaoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EventoExecucaoDt EventoExecucaodt;
		EventoExecucaoNe EventoExecucaone;


		List tempList=null; 
		String Mensagem="";
		String stId="";
		String stNomeBusca1 = "";

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

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					EventoExecucaone.excluir(EventoExecucaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Evento"};
					String[] lisDescricao = {"Evento", "Observação", "Tipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_EventoExecucao");
					request.setAttribute("tempBuscaDescricao","EventoExecucao");
					request.setAttribute("tempBuscaPrograma","EventoExecucao");
					request.setAttribute("tempRetorno","EventoExecucao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = EventoExecucaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case Configuracao.Novo: 
				EventoExecucaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=EventoExecucaone.Verificar(EventoExecucaodt); 
					if (Mensagem.length()==0){
						EventoExecucaone.salvar(EventoExecucaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
				case (EventoExecucaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_EventoExecucaoTipo","Id_EventoExecucaoTipo");
					request.setAttribute("tempBuscaEventoExecucaoTipo","EventoExecucaoTipo");
					request.setAttribute("tempRetorno","EventoExecucao");
					stAcao="/WEB-INF/jsptjgo/EventoExecucaoTipoLocalizar.jsp";
					tempList =EventoExecucaone.consultarDescricaoEventoExecucaoTipo(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEventoExecucaoTipo", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", EventoExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
				case (EventoExecucaoStatusDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
					request.setAttribute("tempBuscaId_EventoExecucaoStatus","Id_EventoExecucaoStatus");
					request.setAttribute("tempBuscaEventoExecucaoStatus","EventoExecucaoStatus");
					request.setAttribute("tempRetorno","EventoExecucao");
					stAcao="/WEB-INF/jsptjgo/EventoExecucaoStatusLocalizar.jsp";
					tempList =EventoExecucaone.consultarDescricaoEventoExecucaoStatus(tempNomeBusca, PosicaoPaginaAtual);
					if (tempList.size()>0){
						request.setAttribute("ListaEventoExecucaoStatus", tempList); 
						request.setAttribute("PaginaAtual", paginaatual);
						request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
						request.setAttribute("QuantidadePaginas", EventoExecucaone.getQuantidadePaginas());
					}else{ 
						request.setAttribute("MensagemErro", "Dados Não Localizados"); 
						request.setAttribute("PaginaAtual", Configuracao.Editar);
					}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_EventoExecucao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( EventoExecucaodt.getId()))){
						EventoExecucaodt.limpar();
						EventoExecucaodt = EventoExecucaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("EventoExecucaodt",EventoExecucaodt );
		request.getSession().setAttribute("EventoExecucaone",EventoExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
