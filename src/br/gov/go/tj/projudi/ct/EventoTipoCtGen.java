package br.gov.go.tj.projudi.ct;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.EventoTipoNe;
import br.gov.go.tj.projudi.dt.EventoTipoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class EventoTipoCtGen extends Controle { 


	/**
	 * 
	 */
	private static final long serialVersionUID = -7681885545854712199L;

	public  EventoTipoCtGen() { 

	} 
		public int Permissao(){
			return EventoTipoDt.CodigoPermissao;
		}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		EventoTipoDt EventoTipodt;
		EventoTipoNe EventoTipone;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/EventoTipo.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","EventoTipo");




		EventoTipone =(EventoTipoNe)request.getSession().getAttribute("EventoTipone");
		if (EventoTipone == null )  EventoTipone = new EventoTipoNe();  


		EventoTipodt =(EventoTipoDt)request.getSession().getAttribute("EventoTipodt");
		if (EventoTipodt == null )  EventoTipodt = new EventoTipoDt();  

		EventoTipodt.setEventoTipo( request.getParameter("EventoTipo")); 

		EventoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		EventoTipodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					EventoTipone.excluir(EventoTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"EventoTipo"};
					String[] lisDescricao = {"EventoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_EventoTipo");
					request.setAttribute("tempBuscaDescricao","EventoTipo");
					request.setAttribute("tempBuscaPrograma","EventoTipo");
					request.setAttribute("tempRetorno","EventoTipo");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = EventoTipone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				EventoTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=EventoTipone.Verificar(EventoTipodt); 
					if (Mensagem.length()==0){
						EventoTipone.salvar(EventoTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_EventoTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( EventoTipodt.getId()))){
						EventoTipodt.limpar();
						EventoTipodt = EventoTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("EventoTipodt",EventoTipodt );
		request.getSession().setAttribute("EventoTipone",EventoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
