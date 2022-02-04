package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioImplantacaoDt;
import br.gov.go.tj.projudi.ne.RelatorioImplantacaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioImplantacaoCt extends Controle{
	
	/**
     * 
     */
    private static final long serialVersionUID = 1230001905144422838L;

    public int Permissao() {
		return 529;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String posicaoPaginaAtual) throws Exception, ServletException, IOException {

		RelatorioImplantacaoDt relatorioImplantacaoDt;
		RelatorioImplantacaoNe relatorioImplantacaoNe;
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		List tempList = null;
		byte[] byTemp = null;
		String stAcao = "/WEB-INF/jsptjgo/RelatorioImplantacaoComarcaServentia.jsp";


		relatorioImplantacaoNe = (RelatorioImplantacaoNe) request.getSession().getAttribute("RelatorioImplantacaone");
		if (relatorioImplantacaoNe == null)
			relatorioImplantacaoNe = new RelatorioImplantacaoNe();

		relatorioImplantacaoDt = (RelatorioImplantacaoDt) request.getSession().getAttribute("RelatorioImplantacaodt");
		if (relatorioImplantacaoDt == null)
			relatorioImplantacaoDt = new RelatorioImplantacaoDt();
		
		
		relatorioImplantacaoDt.setId_Comarca(request.getParameter("Id_Comarca"));
		relatorioImplantacaoDt.setComarca(request.getParameter("Comarca"));
		relatorioImplantacaoDt.setId_ServentiaTipo(request.getParameter("Id_ServentiaTipo"));
		relatorioImplantacaoDt.setServentiaTipo(request.getParameter("ServentiaTipo"));
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("tempRetorno", "RelatorioImplantacao");
		request.setAttribute("tempPrograma", "Relatório de Serventias Implantadas");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Imprimir:
	
				byTemp = relatorioImplantacaoNe.relImplantacaoServentias(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , relatorioImplantacaoDt.getId_Comarca(), relatorioImplantacaoDt.getId_ServentiaTipo(), UsuarioSessao.getUsuarioDt().getNome());				
				
				enviarPDF(response, byTemp,"Relatorio");
				
			    byTemp = null;
			    return;
	
			case Configuracao.Novo:
				relatorioImplantacaoDt.limpar();
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				break;
	
			case (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Comarca"};
					String[] lisDescricao = {"Comarca"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Comarca");
					request.setAttribute("tempBuscaDescricao","Comarca");
					request.setAttribute("tempBuscaPrograma","Forum");			
					request.setAttribute("tempRetorno","RelatorioImplantacao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = relatorioImplantacaoNe.consultarDescricaoComarcaJSON(stNomeBusca1, posicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;								
				}				
				break;
			case (ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar): //localizar
								
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"ServentiaTipo"};
					String[] lisDescricao = {"Tipo de Serventia", "Código"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String permissao = String.valueOf(ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
					
					atribuirJSON(request, "Id_ServentiaTipo", "ServentiaTipo", "ServentiaTipo","RelatorioImplantacao", Configuracao.Editar , permissao, lisNomeBusca, lisDescricao);
					
				} else {
					String stTemp="";
					stTemp = relatorioImplantacaoNe.consultarDescricaoServentiaTipoJSON(stNomeBusca1);
//					stTemp = relatorioImplantacaoNe.consultarDescricaoServentiaTipoJSON(tempNomeBusca);
					
						enviarJSON(response, stTemp);
						
					
					return;								
				}
				break;
							
				
			
		}
		

		request.getSession().setAttribute("RelatorioImplantacaodt", relatorioImplantacaoDt);
		request.getSession().setAttribute("Relatorioimplantacaone", relatorioImplantacaoNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);//(stAcao);
		dis.include(request, response);
	}	
}
