package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.ne.ArquivoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class ArquivoCt extends ArquivoCtGen{

    private static final long serialVersionUID = -5994445691174228045L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ArquivoDt Arquivodt;
		ArquivoNe Arquivone;
		String stNomeBusca1 = "";
		String stAcao="/WEB-INF/jsptjgo/Arquivo.jsp";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		request.setAttribute("tempPrograma","Arquivo");
		request.setAttribute("tempBuscaId_Arquivo","Id_Arquivo");
		request.setAttribute("tempBuscaArquivo","Arquivo");
		request.setAttribute("tempBuscaId_ArquivoTipo","Id_ArquivoTipo");
		request.setAttribute("tempBuscaArquivoTipo","ArquivoTipo");
		request.setAttribute("tempRetorno","Arquivo");

		Arquivone =(ArquivoNe)request.getSession().getAttribute("Arquivone");
		if (Arquivone == null )  Arquivone = new ArquivoNe();  

		Arquivodt =(ArquivoDt)request.getSession().getAttribute("Arquivodt");
		if (Arquivodt == null )  Arquivodt = new ArquivoDt();  

		Arquivodt.setNomeArquivo( request.getParameter("NomeArquivo")); 
		Arquivodt.setId_ArquivoTipo( request.getParameter("Id_ArquivoTipo")); 
		Arquivodt.setArquivoTipo( request.getParameter("ArquivoTipo")); 
		Arquivodt.setContentType( request.getParameter("ContentType")); 
		Arquivodt.setArquivo( request.getParameter("Arquivo")); 
		Arquivodt.setCaminho( request.getParameter("Caminho")); 
		Arquivodt.setDataInsercao( request.getParameter("DataInsercao")); 
		Arquivodt.setUsuarioAssinador( request.getParameter("UsuarioAssinador")); 
		if (request.getParameter("Recibo") != null)
			Arquivodt.setRecibo( request.getParameter("Recibo")); 
		else Arquivodt.setRecibo("false");
		Arquivodt.setArquivoTipoCodigo( request.getParameter("ArquivoTipoCodigo")); 

		Arquivodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Arquivodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);
		
		switch (paginaatual) {
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","Arquivo");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Arquivone.consultarDescricaoArquivoTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("Arquivodt",Arquivodt );
		request.getSession().setAttribute("Arquivone",Arquivone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
