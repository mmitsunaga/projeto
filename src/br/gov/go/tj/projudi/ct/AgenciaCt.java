package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AgenciaDt;
import br.gov.go.tj.projudi.dt.BancoDt;
import br.gov.go.tj.projudi.ne.AgenciaNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class AgenciaCt extends AgenciaCtGen{

    private static final long serialVersionUID = -6676276082154589913L;

    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		AgenciaDt Agenciadt;
		AgenciaNe Agenciane;
		
		String stNomeBusca1 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String stAcao="/WEB-INF/jsptjgo/Agencia.jsp";

		request.setAttribute("tempPrograma","Agencia");
		request.setAttribute("tempBuscaId_Agencia","Id_Agencia");
		request.setAttribute("tempBuscaAgencia","Agencia");
		request.setAttribute("tempBuscaId_Banco","Id_Banco");
		request.setAttribute("tempBuscaBanco","Banco");

		request.setAttribute("tempRetorno","Agencia");

		Agenciane =(AgenciaNe)request.getSession().getAttribute("Agenciane");
		if (Agenciane == null )  Agenciane = new AgenciaNe();  

		Agenciadt =(AgenciaDt)request.getSession().getAttribute("Agenciadt");
		if (Agenciadt == null )  Agenciadt = new AgenciaDt();  

		Agenciadt.setAgencia( request.getParameter("Agencia")); 
		Agenciadt.setAgenciaCodigo( request.getParameter("AgenciaCodigo")); 
		Agenciadt.setId_Banco( request.getParameter("Id_Banco")); 
		Agenciadt.setBanco( request.getParameter("Banco")); 
		Agenciadt.setBancoCodigo( request.getParameter("BancoCodigo")); 

		Agenciadt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Agenciadt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Agencia"};
					String[] lisDescricao = {"Codigo", "Agencia","Banco"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Agencia");
					request.setAttribute("tempBuscaDescricao", "Agencia");
					request.setAttribute("tempBuscaPrograma", "Agencia");
					request.setAttribute("tempRetorno", "Agencia");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Agenciane.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case (BancoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Banco"};
					String[] lisDescricao = {"Banco"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Banco");
					request.setAttribute("tempBuscaDescricao", "Banco");
					request.setAttribute("tempBuscaPrograma", "Banco");
					request.setAttribute("tempRetorno", "Agencia");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(BancoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);							
				}else{
					String stTemp = "";
					stTemp = Agenciane.consultarDescricaoBancoJSON(stNomeBusca1, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;				
				}
				break;
			case Configuracao.Imprimir: {// Gera o relatório de Listagem das Cidades
				ProcessoParteNe parte = new ProcessoParteNe();
				parte.simplificarNome();
				return;
			}
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		}

		request.getSession().setAttribute("Agenciadt",Agenciadt );
		request.getSession().setAttribute("Agenciane",Agenciane );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
