package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.projudi.ne.ProcessoPartePrisaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

//---------------------------------------------------------
public class ProcessoPartePrisaoCt extends ProcessoPartePrisaoCtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4465469829753326084L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoPartePrisaoDt ProcessoPartePrisaodt;
		ProcessoPartePrisaoNe ProcessoPartePrisaone;


		String stNomeBusca1="";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoPartePrisao.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoPartePrisao");




		ProcessoPartePrisaone =(ProcessoPartePrisaoNe)request.getSession().getAttribute("ProcessoPartePrisaone");
		if (ProcessoPartePrisaone == null )  ProcessoPartePrisaone = new ProcessoPartePrisaoNe();  


		ProcessoPartePrisaodt =(ProcessoPartePrisaoDt)request.getSession().getAttribute("ProcessoPartePrisaodt");
		if (ProcessoPartePrisaodt == null )  ProcessoPartePrisaodt = new ProcessoPartePrisaoDt();  


		ProcessoPartePrisaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoPartePrisaodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
//--------------------------------------------------------------------------------//
		switch (paginaatual) {

			case Configuracao.Excluir:				
				String id_excluir = request.getParameter("Id");				
				if (id_excluir != null && !id_excluir.isEmpty()) {
					ProcessoPartePrisaone.excluir(id_excluir, UsuarioSessao.getId_Usuario(), request.getRemoteAddr());
				} else {
					throw new MensagemException("Não foi possível encontrar o Id da Prisão da Parte para ser excluído.");
				}
				
			case Configuracao.Localizar:{ //localizar
				
				String id_processo_parte = request.getParameter("Id_ProcessoParte");
				String stTemp = ProcessoPartePrisaone.consultarPrisoesParteJSON(id_processo_parte);
				try {
					response.setContentType("text/x-json");
					response.getOutputStream().write(stTemp.getBytes());
					response.flushBuffer();
				}catch(Exception e) { }
				return;
			}	
			case Configuracao.Novo: 
				ProcessoPartePrisaodt.limpar();
				break;
			case Configuracao.Salvar:
								
				ProcessoPartePrisaoDt dt = new ProcessoPartePrisaoDt();
				super.atribuiRequest(request, dt);
				Mensagem=ProcessoPartePrisaone.Verificar(dt); 
				if (Mensagem.length()==0){
					dt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					dt.setIpComputadorLog(request.getRemoteAddr());
					ProcessoPartePrisaone.salvar(dt);
					enviarJSON(response, dt.toJson());
				}else {
					throw new MensagemException( Mensagem );
				}				
				return;

//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcPartePrisao");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoPartePrisaodt.getId()))){
						ProcessoPartePrisaodt.limpar();
						ProcessoPartePrisaodt = ProcessoPartePrisaone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoPartePrisaodt",ProcessoPartePrisaodt );
		request.getSession().setAttribute("ProcessoPartePrisaone",ProcessoPartePrisaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
