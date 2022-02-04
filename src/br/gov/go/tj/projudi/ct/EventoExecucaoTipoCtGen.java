package br.gov.go.tj.projudi.ct;


import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.EventoExecucaoTipoDt;
import br.gov.go.tj.projudi.ne.EventoExecucaoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class EventoExecucaoTipoCtGen extends Controle {


	/**
     * 
     */
    private static final long serialVersionUID = 7633810878402119823L;

    public  EventoExecucaoTipoCtGen() {

	} 
		public int Permissao(){
			return EventoExecucaoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EventoExecucaoTipoDt EventoExecucaoTipodt;
		EventoExecucaoTipoNe EventoExecucaoTipone;


		List tempList=null; 
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/EventoExecucaoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","EventoExecucaoTipo");




		EventoExecucaoTipone =(EventoExecucaoTipoNe)request.getSession().getAttribute("EventoExecucaoTipone");
		if (EventoExecucaoTipone == null )  EventoExecucaoTipone = new EventoExecucaoTipoNe();  


		EventoExecucaoTipodt =(EventoExecucaoTipoDt)request.getSession().getAttribute("EventoExecucaoTipodt");
		if (EventoExecucaoTipodt == null )  EventoExecucaoTipodt = new EventoExecucaoTipoDt();  

		EventoExecucaoTipodt.setEventoExecucaoTipo( request.getParameter("EventoExecucaoTipo")); 
		EventoExecucaoTipodt.setEventoExecucaoTipoCodigo( request.getParameter("EventoExecucaoTipoCodigo")); 

		EventoExecucaoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EventoExecucaoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					EventoExecucaoTipone.excluir(EventoExecucaoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				stAcao="/WEB-INF/jsptjgo/EventoExecucaoTipoLocalizar.jsp";
				request.setAttribute("tempBuscaId_EventoExecucaoTipo","Id_EventoExecucaoTipo");
				request.setAttribute("tempBuscaEventoExecucaoTipo","EventoExecucaoTipo");
				request.setAttribute("tempRetorno","EventoExecucaoTipo");
				tempList =EventoExecucaoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
				if (tempList.size()>0){
					request.setAttribute("ListaEventoExecucaoTipo", tempList); 
					request.setAttribute("PaginaAtual", Configuracao.Localizar);
					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
					request.setAttribute("QuantidadePaginas", EventoExecucaoTipone.getQuantidadePaginas());
					EventoExecucaoTipodt.limpar();
				}else{
					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
					stAcao="/WEB-INF/jsptjgo/EventoExecucaoTipo.jsp";
				}
				break;
			case Configuracao.Novo: 
				EventoExecucaoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=EventoExecucaoTipone.Verificar(EventoExecucaoTipodt); 
					if (Mensagem.length()==0){
						EventoExecucaoTipone.salvar(EventoExecucaoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_EventoExecucaoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( EventoExecucaoTipodt.getId()))){
						EventoExecucaoTipodt.limpar();
						EventoExecucaoTipodt = EventoExecucaoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("EventoExecucaoTipodt",EventoExecucaoTipodt );
		request.getSession().setAttribute("EventoExecucaoTipone",EventoExecucaoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
