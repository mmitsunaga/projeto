package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ne.LogTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class LogTipoCt extends LogTipoCtGen{

    private static final long serialVersionUID = 7117287733952961268L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		LogTipoDt LogTipodt;
		LogTipoNe LogTipone;
		List tempList=null; 
		String Mensagem="";
		String stId="";
		String stAcao="/WEB-INF/jsptjgo/LogTipo.jsp";

		request.setAttribute("tempPrograma","LogTipo");

		LogTipone =(LogTipoNe)request.getSession().getAttribute("LogTipone");
		if (LogTipone == null )  LogTipone = new LogTipoNe();  


		LogTipodt =(LogTipoDt)request.getSession().getAttribute("LogTipodt");
		if (LogTipodt == null )  LogTipodt = new LogTipoDt();  

		LogTipodt.setLogTipo( request.getParameter("LogTipo")); 
		LogTipodt.setLogTipoCodigo( request.getParameter("LogTipoCodigo")); 

		LogTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		LogTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());


		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.ExcluirResultado: //Excluir
					LogTipone.excluir(LogTipodt); 
					request.setAttribute("MensagemOk", "Dados Excluidos com Sucesso");
				break;

			case Configuracao.Imprimir: 
				break;
			case Configuracao.Localizar: //localizar
				
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Tipo de Log"};
					String[] lisDescricao = {"LogTipo"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPemissao = String.valueOf(Configuracao.Localizar);

					atribuirJSON(request, "Id_LogTipo", "LogTipo", "LogTipo", "LogTipo", Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
					
				}else{
					String stTemp = "";
						stTemp =LogTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			
				break;
				
//				stAcao="/WEB-INF/jsptjgo/LogTipoLocalizar.jsp";
//				request.setAttribute("tempBuscaId_LogTipo","Id_LogTipo");
//				request.setAttribute("tempBuscaLogTipo","LogTipo");
//				request.setAttribute("tempRetorno","LogTipo");
//				tempList =LogTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
//				if (tempList.size()>0){
//					request.setAttribute("ListaLogTipo", tempList); 
//					request.setAttribute("PaginaAtual", Configuracao.Localizar);
//					request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
//					request.setAttribute("QuantidadePaginas", LogTipone.getQuantidadePaginas());
//					LogTipodt.limpar();
//				}else{
//					request.setAttribute("MensagemErro", "Dados Não Localizados"); 
//				}
//				break;
			case Configuracao.Novo: 
				LogTipodt.limpar();
				break;
			case Configuracao.SalvarResultado: 
					Mensagem=LogTipone.Verificar(LogTipodt); 
					if (Mensagem.length()==0){
						LogTipone.salvar(LogTipodt); 
						request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
					}else	request.setAttribute("MensagemErro", Mensagem );
				break;
//--------------------------------------------------------------------------------//
			default:
				stId = request.getParameter("Id_LogTipo");
				if (stId != null && !stId.isEmpty())
					if (paginaatual==Configuracao.Atualizar || (!stId.equalsIgnoreCase("") &&  !stId.equalsIgnoreCase( LogTipodt.getId()))){
						LogTipodt.limpar();
						LogTipodt = LogTipone.consultarId(stId);
					}
				break;
		}

		request.getSession().setAttribute("LogTipodt",LogTipodt );
		request.getSession().setAttribute("LogTipone",LogTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
