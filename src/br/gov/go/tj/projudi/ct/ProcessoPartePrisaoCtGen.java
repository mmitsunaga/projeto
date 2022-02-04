package br.gov.go.tj.projudi.ct;

 import br.gov.go.tj.projudi.dt.ProcessoParteDt;
 import br.gov.go.tj.projudi.dt.PrisaoTipoDt;
 import br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt;
 import br.gov.go.tj.projudi.dt.EventoTipoDt;
 import br.gov.go.tj.projudi.dt.MovimentacaoDt;
 import br.gov.go.tj.projudi.dt.MovimentacaoDt;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;

import br.gov.go.tj.projudi.ne.ProcessoPartePrisaoNe;
import br.gov.go.tj.projudi.dt.ProcessoPartePrisaoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ProcessoPartePrisaoCtGen extends Controle { 


	public  ProcessoPartePrisaoCtGen() { 

	} 
		public int Permissao(){
			return ProcessoPartePrisaoDt.CodigoPermissao;
		}

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

		ProcessoPartePrisaodt.setId_ProcessoParte( request.getParameter("Id_ProcParte")); 
		ProcessoPartePrisaodt.setNome( request.getParameter("Nome")); 
		ProcessoPartePrisaodt.setDataPrisao( request.getParameter("DataPrisao")); 
		ProcessoPartePrisaodt.setId_PrisaoTipo( request.getParameter("Id_PrisaoTipo")); 
		ProcessoPartePrisaodt.setPrisaoTipo( request.getParameter("PrisaoTipo")); 
		ProcessoPartePrisaodt.setId_LocalCumpPena( request.getParameter("Id_LocalCumpPena")); 
		ProcessoPartePrisaodt.setLocalCumpPena( request.getParameter("LocalCumpPena")); 		
		ProcessoPartePrisaodt.setDataEvento( request.getParameter("DataEvento")); 
		ProcessoPartePrisaodt.setId_EventoTipo( request.getParameter("Id_EventoTipo")); 
		ProcessoPartePrisaodt.setEventoTipo( request.getParameter("EventoTipo")); 
		ProcessoPartePrisaodt.setPrazoPrisao( request.getParameter("PrazoPrisao")); 
		ProcessoPartePrisaodt.setId_MoviEvento( request.getParameter("Id_MoviEvento")); 
		ProcessoPartePrisaodt.setMoviEvento( request.getParameter("MoviEvento")); 
		ProcessoPartePrisaodt.setId_MoviPrisao( request.getParameter("Id_MoviPrisao")); 
		ProcessoPartePrisaodt.setMoviPrisao( request.getParameter("MoviPrisao")); 
		ProcessoPartePrisaodt.setObservacao( request.getParameter("Observacao")); 

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
			case Configuracao.ExcluirResultado: //Excluir
					ProcessoPartePrisaone.excluir(ProcessoPartePrisaodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ProcessoPartePrisao"};
					String[] lisDescricao = {"ProcessoPartePrisao"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ProcPartePrisao");
					request.setAttribute("tempBuscaDescricao","ProcessoPartePrisao");
					request.setAttribute("tempBuscaPrograma","ProcessoPartePrisao");
					request.setAttribute("tempRetorno","ProcessoPartePrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoPartePrisaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;
			case Configuracao.Novo: 
				ProcessoPartePrisaodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=ProcessoPartePrisaone.Verificar(ProcessoPartePrisaodt); 
					if (Mensagem.length()==0){
						ProcessoPartePrisaone.salvar(ProcessoPartePrisaodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//				case (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
//				if (request.getParameter("Passo")==null){
//					String[] lisNomeBusca = {"ProcessoParte"};
//					String[] lisDescricao = {"ProcessoParte"};
//					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
//					request.setAttribute("tempBuscaId","Id_ProcParte");
//					request.setAttribute("tempBuscaDescricao","Nome");
//					request.setAttribute("tempBuscaPrograma","ProcessoParte");
//					request.setAttribute("tempRetorno","ProcessoPartePrisao");
//					request.setAttribute("tempDescricaoId","Id");
//					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
//					request.setAttribute("PaginaAtual", (ProcessoParteDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
//					request.setAttribute("PosicaoPaginaAtual", "0");
//					request.setAttribute("QuantidadePaginas", "0");
//					request.setAttribute("lisNomeBusca", lisNomeBusca);
//					request.setAttribute("lisDescricao", lisDescricao);
//				} else {
//					String stTemp = ProcessoPartePrisaone.consultarDescricaoProcessoParteJSON(stNomeBusca1, PosicaoPaginaAtual);
//					try {
//						response.setContentType("text/x-json");
//						response.getOutputStream().write(stTemp.getBytes());
//						response.flushBuffer();
//					}catch(Exception e) { }
//					return;
//				}
//					break;
			case (PrisaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"PrisaoTipo"};
					String[] lisDescricao = {"PrisaoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_PrisaoTipo");
					request.setAttribute("tempBuscaDescricao","PrisaoTipo");
					request.setAttribute("tempBuscaPrograma","PrisaoTipo");
					request.setAttribute("tempRetorno","ProcessoPartePrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (PrisaoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoPartePrisaone.consultarDescricaoPrisaoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
				case (LocalCumprimentoPenaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"LocalCumprimentoPena"};
					String[] lisDescricao = {"LocalCumprimentoPena"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_LocalCumpPena");
					request.setAttribute("tempBuscaDescricao","LocalCumpPena");
					request.setAttribute("tempBuscaPrograma","LocalCumprimentoPena");
					request.setAttribute("tempRetorno","ProcessoPartePrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (LocalCumprimentoPenaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoPartePrisaone.consultarDescricaoLocalCumprimentoPenaJSON(stNomeBusca1, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
					break;
				case (EventoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"EventoTipo"};
					String[] lisDescricao = {"EventoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_EventoTipo");
					request.setAttribute("tempBuscaDescricao","EventoTipo");
					request.setAttribute("tempBuscaPrograma","EventoTipo");
					request.setAttribute("tempRetorno","ProcessoPartePrisao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (EventoTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = ProcessoPartePrisaone.consultarDescricaoEventoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
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
