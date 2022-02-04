package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ParametroComutacaoExecucaoDt;
import br.gov.go.tj.projudi.ne.ParametroComutacaoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class ParametroComutacaoExecucaoCt extends ParametroComutacaoExecucaoCtGen{

	private static final long serialVersionUID = 9202042509860049618L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		ParametroComutacaoExecucaoDt ParametroComutacaoExecucaodt;
		ParametroComutacaoExecucaoNe ParametroComutacaoExecucaone;
		
		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1"); //data do decreto
		request.setAttribute("descCuringa", "");
		
		String stAcao="/WEB-INF/jsptjgo/ParametroComutacaoExecucao.jsp";
		request.setAttribute("tempPrograma","Parametro das Comutacoes");

		ParametroComutacaoExecucaone =(ParametroComutacaoExecucaoNe)request.getSession().getAttribute("ParametroComutacaoExecucaone");
		if (ParametroComutacaoExecucaone == null )  ParametroComutacaoExecucaone = new ParametroComutacaoExecucaoNe();  

		ParametroComutacaoExecucaodt =(ParametroComutacaoExecucaoDt)request.getSession().getAttribute("ParametroComutacaoExecucaodt");
		if (ParametroComutacaoExecucaodt == null )  ParametroComutacaoExecucaodt = new ParametroComutacaoExecucaoDt();  

		ParametroComutacaoExecucaodt.setDataDecreto( request.getParameter("DataDecreto")); 
		ParametroComutacaoExecucaodt.setFracaoHediondo( request.getParameter("FracaoHediondo")); 
		ParametroComutacaoExecucaodt.setFracaoComum( request.getParameter("FracaoComum")); 
		ParametroComutacaoExecucaodt.setFracaoComumReinc( request.getParameter("FracaoComumReinc")); 
		ParametroComutacaoExecucaodt.setFracaoComumFeminino( request.getParameter("FracaoComumFeminino")); 
		ParametroComutacaoExecucaodt.setFracaoComumReincFeminino( request.getParameter("FracaoComumReincFeminino")); 
		if (request.getParameter("PenaUnificada") != null){
			ParametroComutacaoExecucaodt.setPenaUnificada( request.getParameter("PenaUnificada"));
		} else ParametroComutacaoExecucaodt.setPenaUnificada( "false");
		if (request.getParameter("BeneficioAcumulado") != null){
			ParametroComutacaoExecucaodt.setBeneficioAcumulado( request.getParameter("BeneficioAcumulado"));
		} else ParametroComutacaoExecucaodt.setBeneficioAcumulado("false");

		ParametroComutacaoExecucaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		ParametroComutacaoExecucaodt.setIpComputadorLog(request.getRemoteAddr());

		request.setAttribute("PaginaAnterior",paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual",Configuracao.Editar);

		switch (paginaatual) {
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					//lisNomeBusca = new ArrayList();
					//lisDescricao = new ArrayList();
					String[] lisNomeBusca = {"Data do decreto"};
					String[] lisDescricao = {"Data do decreto","Fração Hediondo","Fração Comum","Fração Comum Reincidente","Considerar Pena Unificada"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ParametroComutacaoExecucao");
					request.setAttribute("tempBuscaDescricao","Parametro das Comutacoes");
					request.setAttribute("tempBuscaPrograma","ParametroComutacaoExecucao");			
					request.setAttribute("tempRetorno","ParametroComutacaoExecucao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("tempPaginaAtualJSON", String.valueOf(Configuracao.Editar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					//validar se o parâmetro de busca é uma data
					boolean isData = true;
					
					if (stNomeBusca1.length() != 0)
						isData = Funcoes.validaData(stNomeBusca1);
					if (isData){							
							stTemp = ParametroComutacaoExecucaone.consultarDescricaoJSON(stNomeBusca1, PosicaoPaginaAtual);
					} else {
						stTemp = ParametroComutacaoExecucaone.gerarJSONVazio();
						request.setAttribute("MensagemErro","Data inválida!");
					}
					
					enviarJSON(response, stTemp);
																
					return;		
				}
				break;
			default: {
				super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
				return;
			}
		}

		request.getSession().setAttribute("ParametroComutacaoExecucaodt",ParametroComutacaoExecucaodt );
		request.getSession().setAttribute("ParametroComutacaoExecucaone",ParametroComutacaoExecucaone );

		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
