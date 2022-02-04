package br.gov.go.tj.projudi.ct;

import java.io.File;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.ne.CidadeNe;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;

/**
 * Classe de controle para Cidades 
 */
public class CidadeCt extends CidadeCtGen {

	private static final long serialVersionUID = 93915055124900215L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {

		CidadeDt cidadeDt ;
		CidadeNe cidadeNe;
		
		String stNomeBusca1  = "";
		String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		String stAcao = "/WEB-INF/jsptjgo/Cidade.jsp";

		/*
		 * Faz o mapeamento do id e da descrição das buscas externas com as
		 * variaveis locais. Ex. localmente existe CtpsUf que na verdade e o uf
		 * do Estado, assim: cria-se uma variável para o mapeamento chamada
		 * tempBsucaId_Estado e outra tempBuscaEstado fazendo mapeamento local
		 * para Id_CtpsUf e CtpsUf
		 */
		request.setAttribute("tempPrograma", "Cidade");

		cidadeNe = (CidadeNe) request.getSession().getAttribute("Cidadene");
		if (cidadeNe == null) cidadeNe = new CidadeNe();
		cidadeDt = (CidadeDt) request.getSession().getAttribute("Cidadedt");
		if (cidadeDt == null) cidadeDt = new CidadeDt();

		cidadeDt.setCidade(request.getParameter("Cidade"));
		cidadeDt.setCidadeCodigo(request.getParameter("CidadeCodigo"));
		cidadeDt.setId_Estado(request.getParameter("Id_Estado"));
		cidadeDt.setEstado(request.getParameter("Estado"));
		cidadeDt.setEstadoCodigo(request.getParameter("EstadoCodigo"));
		cidadeDt.setUf(request.getParameter("Uf"));
		cidadeDt.setCodigoSPG( request.getParameter("CodigoSPG"));

		cidadeDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		cidadeDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

				
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {
//			case Configuracao.SalvarResultado: 
//				String Mensagem=cidadeNe.Verificar(cidadeDt); 
//				if (Mensagem.length()==0){
//					cidadeNe.salvar(cidadeDt); 
//					String stTemp = cidadeDt.getPropriedades().replace("{","{mensagem:Dados Salvos com sucesso,");
//					
//					try{
//						enviarJSON(response, stTemp);
//						
//					
//					return;
//					//request.setAttribute("MensagemOk", "Dados Salvos com sucesso"); 
//				} else	request.setAttribute("MensagemErro", Mensagem );
//				break;
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Cidade","Uf"};
					String[] lisDescricao = {"Cidade","Uf"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Cidade");
					request.setAttribute("tempBuscaDescricao", "Cidade");
					request.setAttribute("tempBuscaPrograma", "Cidade");
					request.setAttribute("tempRetorno", "Cidade");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					cidadeDt.limpar();
				}else{
					String stTemp = "";
					stTemp = cidadeNe.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case (EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Estado"};
					String[] lisDescricao = {"Sigla","Estado"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Estado");
					request.setAttribute("tempBuscaDescricao", "Estado");
					request.setAttribute("tempBuscaPrograma", "Estado");
					request.setAttribute("tempRetorno", "Cidade");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);							
				}else{
					String stTemp = "";
						stTemp =cidadeNe.consultarDescricaoEstadoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}
				break;
			case Configuracao.Imprimir: {// Gera o relatório de Listagem das Cidades				
			    byte[] byTemp = (new CidadeNe()).relListagemCidades(this.getServletContext().getRealPath(File.separator));
			    enviarPDF(response, byTemp, "RelatorioCidade.pdf");			    
			    return;
			}
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}

		} // fim switch

		request.getSession().setAttribute("Cidadedt", cidadeDt);
		request.getSession().setAttribute("Cidadene", cidadeNe);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);

	} // fim do método executar

} // fim da classe CidadeCt
