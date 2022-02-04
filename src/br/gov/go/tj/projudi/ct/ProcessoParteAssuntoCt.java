package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AssuntoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAssuntoDt;
import br.gov.go.tj.projudi.ne.ProcessoParteAssuntoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

//---------------------------------------------------------
public class ProcessoParteAssuntoCt extends ProcessoParteAssuntoCtGen{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6771138020005504953L;

//

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ProcessoParteAssuntoDt ProcessoParteAssuntodt;
		ProcessoParteAssuntoNe ProcessoParteAssuntone;


		String stNomeBusca1="";
		String stNomeBusca2 = "";
		String Mensagem="";
		String stId="";

		String stAcao="/WEB-INF/jsptjgo/ProcessoParteAssunto.jsp";

		//--------------------------------------------------------------------------
		//Faz o mapeamento do id e da descrição das buscas externas com as variaveis locais.
		//Ex. localmente existe CtpsUf que na verdade e o uf do Estado, assim:
		//     cria-se uma variável para o mapeamento chamada tempBsucaId_Estado e outra
		//     tempBsucaEstado fazendo mapeamento local para Id_CtpsUf e CtpsUf
		//--------------------------------------------------------------------------
		request.setAttribute("tempPrograma","ProcessoParteAssunto");


		ProcessoParteAssuntone =(ProcessoParteAssuntoNe)request.getSession().getAttribute("ProcessoParteAssuntone");
		if (ProcessoParteAssuntone == null )  ProcessoParteAssuntone = new ProcessoParteAssuntoNe();  


		ProcessoParteAssuntodt =(ProcessoParteAssuntoDt)request.getSession().getAttribute("ProcessoParteAssuntodt");
		if (ProcessoParteAssuntodt == null )  ProcessoParteAssuntodt = new ProcessoParteAssuntoDt();  

		ProcessoParteAssuntodt.setId_ProcessoParte( request.getParameter("Id_ProcParte")); 
		ProcessoParteAssuntodt.setProcessoParteNome( request.getParameter("Nome")); 
		ProcessoParteAssuntodt.setId_Assunto( request.getParameter("Id_Assunto")); 
		ProcessoParteAssuntodt.setAssunto( request.getParameter("Assunto")); 
		ProcessoParteAssuntodt.setDataInclusao( request.getParameter("DataInclusao")); 

		ProcessoParteAssuntodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ProcessoParteAssuntodt.setIpComputadorLog(request.getRemoteAddr());


		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		//é a página padrão
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		ProcessoParteAssuntoDt dt = null;
//--------------------------------------------------------------------------------//
		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoParteAssuntone.excluir(ProcessoParteAssuntodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;
			case Configuracao.Salvar:				
				dt = new ProcessoParteAssuntoDt();
				atribuiRequest(request, dt);
				if (dt.isProntoSalvar()) {

					dt.setDataInclusao(Funcoes.DataHora(new Date()));					
					dt.setIpComputadorLog(request.getRemoteAddr());
					dt.setId_UsuarioLog( UsuarioSessao.getId_Usuario());
					
					ProcessoParteAssuntone.salvar(dt);
					enviarJSON(response, dt.toJson());
					return;
				} else {
					throw new MensagemException("Não foi possivel cadastra o Assunto para a parte");
				}
				
			case Configuracao.Excluir:				
				dt = new ProcessoParteAssuntoDt();
				atribuiRequest(request, dt);								
				if (dt.isProntoExcluir()) {					
					dt.setIpComputadorLog(request.getRemoteAddr());
					dt.setId_UsuarioLog( UsuarioSessao.getId_Usuario());
					ProcessoParteAssuntone.excluir(dt);
					return;
				} else {
					throw new MensagemException("Não foi possível excluíro o Assunto.");
				}
			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: {//localizar{
				
				String id_processo_parte = request.getParameter("id_processo_parte");
				String stTemp = ProcessoParteAssuntone.consultarAssuntosJSON(id_processo_parte);
				try {
					response.setContentType("text/x-json");
					response.getOutputStream().write(stTemp.getBytes());
					response.flushBuffer();
				}catch(Exception e) { }
				return;
							
			}	
			case Configuracao.Novo: 
				ProcessoParteAssuntodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoParteAssuntone.Verificar(ProcessoParteAssuntodt); 
					if (Mensagem.length()==0){
						ProcessoParteAssuntone.salvar(ProcessoParteAssuntodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//			case (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//				if (request.getParameter("Passo")==null){
//					String[] lisNomeBusca = {"ProcessoParte"};
//					String[] lisDescricao = {"ProcessoParte"};
//					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
//					request.setAttribute("tempBuscaId","Id_ProcParte");
//					request.setAttribute("tempBuscaDescricao","Nome");
//					request.setAttribute("tempBuscaPrograma","ProcessoParte");
//					request.setAttribute("tempRetorno","ProcessoParteAssunto");
//					request.setAttribute("tempDescricaoId","Id");
//					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
//					request.setAttribute("PaginaAtual", (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
//					request.setAttribute("PosicaoPaginaAtual", "0");
//					request.setAttribute("QuantidadePaginas", "0");
//					request.setAttribute("lisNomeBusca", lisNomeBusca);
//					request.setAttribute("lisDescricao", lisDescricao);
//				} else {
//					String stTemp = ProcessoParteAssuntone.consultarDescricaoProcessoParteJSON(stNomeBusca1, PosicaoPaginaAtual);
//					try {
//						response.setContentType("text/x-json");
//						response.getOutputStream().write(stTemp.getBytes());
//						response.flushBuffer();
//					}catch(Exception e) { }
//					return;
//				}
					//break;
			case (AssuntoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Assunto"};
					String[] lisDescricao = {"Assunto"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Assunto");
					request.setAttribute("tempBuscaDescricao","Assunto");
					request.setAttribute("tempBuscaPrograma","Assunto");
					request.setAttribute("tempRetorno","ProcessoParteAssunto");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AssuntoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoParteAssuntone.consultarDescricaoAssuntoJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_ProcParteAssunto");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( ProcessoParteAssuntodt.getId()))){
						ProcessoParteAssuntodt.limpar();
						ProcessoParteAssuntodt = ProcessoParteAssuntone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("ProcessoParteAssuntodt",ProcessoParteAssuntodt );
		request.getSession().setAttribute("ProcessoParteAssuntone",ProcessoParteAssuntone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
