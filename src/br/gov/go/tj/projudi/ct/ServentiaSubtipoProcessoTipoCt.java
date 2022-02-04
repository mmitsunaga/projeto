package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoAssuntoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoProcessoTipoDt;
import br.gov.go.tj.projudi.ne.ServentiaSubtipoProcessoTipoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaSubtipoProcessoTipoCt extends ServentiaSubtipoProcessoTipoCtGen{

    private static final long serialVersionUID = -6238200845646419746L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaSubtipoProcessoTipoDt ServentiaSubtipoProcessoTipodt;
		ServentiaSubtipoProcessoTipoNe ServentiaSubtipoProcessoTipone;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao="/WEB-INF/jsptjgo/ServentiaSubtipoProcessoTipo.jsp";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma","ServentiaSubtipoProcessoTipo");
		request.setAttribute("ListaUlLiServentiaSubtipoProcessoTipo","");

		ServentiaSubtipoProcessoTipone =(ServentiaSubtipoProcessoTipoNe)request.getSession().getAttribute("ServentiaSubtipoProcessoTipone");
		if (ServentiaSubtipoProcessoTipone == null )  ServentiaSubtipoProcessoTipone = new ServentiaSubtipoProcessoTipoNe();  

		ServentiaSubtipoProcessoTipodt =(ServentiaSubtipoProcessoTipoDt)request.getSession().getAttribute("ServentiaSubtipoProcessoTipodt");
		if (ServentiaSubtipoProcessoTipodt == null )  ServentiaSubtipoProcessoTipodt = new ServentiaSubtipoProcessoTipoDt();  

		ServentiaSubtipoProcessoTipodt.setId_ServentiaSubtipo( request.getParameter("Id_ServentiaSubtipo")); 
		ServentiaSubtipoProcessoTipodt.setServentiaSubtipo( request.getParameter("ServentiaSubtipo")); 
		ServentiaSubtipoProcessoTipodt.setId_ProcessoTipo( request.getParameter("Id_ProcessoTipo")); 
		ServentiaSubtipoProcessoTipodt.setProcessoTipo( request.getParameter("ProcessoTipo")); 
		ServentiaSubtipoProcessoTipodt.setServentiaSubtipoCodigo( request.getParameter("ServentiaSubtipoCodigo")); 
		ServentiaSubtipoProcessoTipodt.setProcessoTipoCodigo( request.getParameter("ProcessoTipoCodigo")); 

		ServentiaSubtipoProcessoTipodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaSubtipoProcessoTipodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case (ServentiaSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ServentiaSubtipo"};
					String[] lisDescricao = {"ServentiaSubtipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaSubtipo");
					request.setAttribute("tempBuscaDescricao", "ServentiaSubtipo");
					request.setAttribute("tempBuscaPrograma", "ServentiaSubtipo");
					request.setAttribute("tempRetorno", "ServentiaSubtipoProcessoTipo");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (ServentiaSubtipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ServentiaSubtipoProcessoTipone.consultarDescricaoServentiaSubtipoJSON(stNomeBusca1, PosicaoPaginaAtual);						
					enviarJSON(response, stTemp);																				
					return;
				}
			break;
			case Configuracao.Curinga6:
				String stPasso = request.getParameter("Passo");
				if("incluir".equals(stPasso)) {
					String stId_ServentiaSubtipo = request.getParameter("Id_Principal");
					String stId_ProcessoTipo = request.getParameter("Id_Secundario");
					ServentiaSubtipoProcessoTipoDt tempDt = new ServentiaSubtipoProcessoTipoDt();
					tempDt.setId_ProcessoTipo(stId_ProcessoTipo);
					tempDt.setId_ServentiaSubtipo(stId_ServentiaSubtipo);
					tempDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					tempDt.setIpComputadorLog(getIpCliente(request));					
					ServentiaSubtipoProcessoTipone.salvar(tempDt);
					return;
				}else if("excluir".equals(stPasso)) {
					String stId_ServentiaSubTipoProcessoTipo = request.getParameter("Id_Excluir");
					ServentiaSubtipoProcessoTipoDt tempDt = new ServentiaSubtipoProcessoTipoDt();
					tempDt.setId(stId_ServentiaSubTipoProcessoTipo);					
					tempDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					tempDt.setIpComputadorLog(getIpCliente(request));	
					
					ServentiaSubtipoProcessoTipone.excluir(tempDt);
					return;
				}
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("ServentiaSubtipoProcessoTipodt",ServentiaSubtipoProcessoTipodt );
		request.getSession().setAttribute("ServentiaSubtipoProcessoTipone",ServentiaSubtipoProcessoTipone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
