package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.ne.BairroNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

public class BairroCt extends BairroCtGen {
	
    private static final long serialVersionUID = 7534008309886404155L;
    
    public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		BairroDt bairroDt;
		BairroNe bairroNe = new BairroNe();
		String Mensagem = "";
		String stNomeBusca1 = "";
		String stNomeBusca2 = "";
		String stNomeBusca3 = "";
		String stNomeBusca4 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null) stNomeBusca3 = request.getParameter("nomeBusca3");
		if(request.getParameter("nomeBusca4") != null) stNomeBusca4 = request.getParameter("nomeBusca4");
		
		String stAcao="/WEB-INF/jsptjgo/Bairro.jsp";

		request.setAttribute("tempPrograma","Bairro");
		request.setAttribute("tempBuscaId_Bairro","Id_Bairro");
		request.setAttribute("tempBuscaBairro","Bairro");
		request.setAttribute("tempBuscaId_Cidade","Id_Cidade");
		request.setAttribute("tempBuscaCidade","Cidade");
		request.setAttribute("tempBuscaId_Regiao","Id_Regiao");
		request.setAttribute("tempBuscaRegiao","Regiao");
		request.setAttribute("tempBuscaId_Zona","Id_Zona");
		request.setAttribute("tempBuscaZona","Zona");
		request.setAttribute("tempRetorno","Bairro");
					
		bairroDt =(BairroDt)request.getSession().getAttribute("Bairrodt");
		if (bairroDt == null )  {
			bairroDt = new BairroDt();
		}

		bairroDt.setBairro( request.getParameter("Bairro")); 
		bairroDt.setBairroCodigo( request.getParameter("BairroCodigo")); 
		bairroDt.setId_Cidade( request.getParameter("Id_Cidade")); 
		bairroDt.setCidade( request.getParameter("Cidade")); 
		bairroDt.setUf( request.getParameter("Uf")); 
		bairroDt.setCodigoSPG( request.getParameter("CodigoSPG"));
		
		bairroDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		bairroDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
					
		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Bairro","Cidade","UF", "ZONA"};
					String[] lisDescricao = {"Bairro","Cidade","UF", "Zona", "Região"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Bairro");
					request.setAttribute("tempBuscaDescricao", "Bairro");
					request.setAttribute("tempBuscaPrograma", "Bairro");
					request.setAttribute("tempRetorno", "Bairro");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Bairro");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					bairroDt.limpar();
				}else{
					String stTemp = "";
					stTemp = bairroNe.consultarDescricaoBairrosGeralJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar): 
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "Bairro");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (CidadeDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = bairroNe.consultarDescricaoCidadeJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
					
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			
			case Configuracao.SalvarResultado: 
				request.setAttribute("PaginaAtual", Configuracao.Editar);
				Mensagem = bairroNe.Verificar(bairroDt); 
				if (Mensagem.length()==0){
					bairroNe.salvar(bairroDt); 
					request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
				}else	request.setAttribute("MensagemErro", Mensagem );
				break;
			
			default:
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
		}

		request.getSession().setAttribute("Bairrodt",bairroDt );
		request.getSession().setAttribute("Bairrone",bairroNe );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
