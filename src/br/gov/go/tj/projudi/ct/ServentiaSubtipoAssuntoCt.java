package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ServentiaSubtipoAssuntoDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.ne.ServentiaSubtipoAssuntoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ServentiaSubtipoAssuntoCt extends ServentiaSubtipoAssuntoCtGen {

	private static final long serialVersionUID = -5129312643827335527L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ServentiaSubtipoAssuntoDt ServentiaSubtipoAssuntodt;
		ServentiaSubtipoAssuntoNe ServentiaSubtipoAssuntone;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax

		String stAcao = "/WEB-INF/jsptjgo/ServentiaSubtipoAssunto.jsp";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma", "ServentiaSubtipoAssunto");
		request.setAttribute("ListaUlLiServentiaSubtipoAssunto", "");

		ServentiaSubtipoAssuntone = (ServentiaSubtipoAssuntoNe) request.getSession().getAttribute("ServentiaSubtipoAssuntone");
		if (ServentiaSubtipoAssuntone == null) ServentiaSubtipoAssuntone = new ServentiaSubtipoAssuntoNe();

		ServentiaSubtipoAssuntodt = (ServentiaSubtipoAssuntoDt) request.getSession().getAttribute("ServentiaSubtipoAssuntodt");
		if (ServentiaSubtipoAssuntodt == null) ServentiaSubtipoAssuntodt = new ServentiaSubtipoAssuntoDt();

		ServentiaSubtipoAssuntodt.setId_ServentiaSubtipo(request.getParameter("Id_ServentiaSubtipo"));
		ServentiaSubtipoAssuntodt.setServentiaSubtipo(request.getParameter("ServentiaSubtipo"));
		ServentiaSubtipoAssuntodt.setId_Assunto(request.getParameter("Id_Assunto"));
		ServentiaSubtipoAssuntodt.setAssunto(request.getParameter("Assunto"));
		ServentiaSubtipoAssuntodt.setServentiaSubtipoCodigo(request.getParameter("ServentiaSubtipoCodigo"));
		ServentiaSubtipoAssuntodt.setAssuntoCodigo(request.getParameter("AssuntoCodigo"));

		ServentiaSubtipoAssuntodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ServentiaSubtipoAssuntodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);
		
		switch (paginaatual) {
			case (ServentiaSubtipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ServentiaSubtipo"};
					String[] lisDescricao = {"ServentiaSubtipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaSubtipo");
					request.setAttribute("tempBuscaDescricao", "ServentiaSubtipo");
					request.setAttribute("tempBuscaPrograma", "ServentiaSubtipo");
					request.setAttribute("tempRetorno", "ServentiaSubtipoAssunto");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
					request.setAttribute("PaginaAtual", (ServentiaSubtipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = ServentiaSubtipoAssuntone.consultarDescricaoServentiaSubtipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
			break;
			case Configuracao.Curinga6:
				String stPasso = request.getParameter("Passo");
				if("incluir".equals(stPasso)) {
					String stId_ServentiaSubtipo = request.getParameter("Id_Principal");
					String stId_Assunto = request.getParameter("Id_Secundario");
					ServentiaSubtipoAssuntoDt tempDt = new ServentiaSubtipoAssuntoDt();
					tempDt.setId_Assunto(stId_Assunto);
					tempDt.setId_ServentiaSubtipo(stId_ServentiaSubtipo);
					tempDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					tempDt.setIpComputadorLog(getIpCliente(request));					
					ServentiaSubtipoAssuntone.salvar(tempDt);
					return;
				}else if("excluir".equals(stPasso)) {
					String stId_ServentiaSubTipoAssunoto = request.getParameter("Id_Excluir");
					ServentiaSubtipoAssuntoDt tempDt = new ServentiaSubtipoAssuntoDt();
					tempDt.setId(stId_ServentiaSubTipoAssunoto);					
					tempDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					tempDt.setIpComputadorLog(getIpCliente(request));	
					
					ServentiaSubtipoAssuntone.excluir(tempDt);
					return;
				}
				
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("ServentiaSubtipoAssuntodt", ServentiaSubtipoAssuntodt);
		request.getSession().setAttribute("ServentiaSubtipoAssuntone", ServentiaSubtipoAssuntone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
