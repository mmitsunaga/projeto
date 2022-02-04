package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt;
import br.gov.go.tj.projudi.ne.EventoExecucaoStatusNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class EventoExecucaoStatusCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 6287221117260790495L;

    public  EventoExecucaoStatusCtGen() {

	} 
		public int Permissao(){
			return EventoExecucaoStatusDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EventoExecucaoStatusDt EventoExecucaoStatusdt;
		EventoExecucaoStatusNe EventoExecucaoStatusne;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/EventoExecucaoStatus.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","EventoExecucaoStatus");




		EventoExecucaoStatusne =(EventoExecucaoStatusNe)request.getSession().getAttribute("EventoExecucaoStatusne");
		if (EventoExecucaoStatusne == null )  EventoExecucaoStatusne = new EventoExecucaoStatusNe();  


		EventoExecucaoStatusdt =(EventoExecucaoStatusDt)request.getSession().getAttribute("EventoExecucaoStatusdt");
		if (EventoExecucaoStatusdt == null )  EventoExecucaoStatusdt = new EventoExecucaoStatusDt();  

		EventoExecucaoStatusdt.setEventoExecucaoStatus( request.getParameter("EventoExecucaoStatus")); 
		EventoExecucaoStatusdt.setEventoExecucaoStatusCodigo( request.getParameter("EventoExecucaoStatusCodigo")); 

		EventoExecucaoStatusdt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EventoExecucaoStatusdt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					EventoExecucaoStatusne.excluir(EventoExecucaoStatusdt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/EventoExecucaoStatusLocalizar.jsp";
				request.setAttribute("tempBuscaId_EventoExecucaoStatus","Id_EventoExecucaoStatus");
				request.setAttribute("tempBuscaEventoExecucaoStatus","EventoExecucaoStatus");
				request.setAttribute("tempRetorno","EventoExecucaoStatus");
				tempList =EventoExecucaoStatusne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaEventoExecucaoStatus", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", EventoExecucaoStatusne.getQuantidadePaginas());
					EventoExecucaoStatusdt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					stAcao="/WEB-INF/jsptjgo/EventoExecucaoStatus.jsp";
				}
				break;
			case Configuracao.Novo: 
				EventoExecucaoStatusdt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=EventoExecucaoStatusne.Verificar(EventoExecucaoStatusdt); 
					if (Mensagem.length()==0){
						EventoExecucaoStatusne.salvar(EventoExecucaoStatusdt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_EventoExecucaoStatus");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( EventoExecucaoStatusdt.getId()))){
						EventoExecucaoStatusdt.limpar();
						EventoExecucaoStatusdt = EventoExecucaoStatusne.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("EventoExecucaoStatusdt",EventoExecucaoStatusdt );
		request.getSession().setAttribute("EventoExecucaoStatusne",EventoExecucaoStatusne );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
