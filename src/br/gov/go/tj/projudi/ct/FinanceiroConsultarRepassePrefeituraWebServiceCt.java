package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.FinanceiroConsultarRepassePrefeituraWebServiceDt;
import br.gov.go.tj.projudi.ne.FinanceiroConsultarRepassePrefeituraWebServiceNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class FinanceiroConsultarRepassePrefeituraWebServiceCt extends Controle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7820061529484272616L;
	
	private static final String PAGINA_FINANCEIRO_CONSULTAR_REPASSE_PREFEITURA_WEBSERVICE 	= "/WEB-INF/jsptjgo/FinanceiroConsultarRepassePrefeituraWebService.jsp";
	
	private static final int PASSO_EDITAR_REPASSE = 1;
	private static final int PASSO_EDITAR_PAGAMENTO = 2;
		
	@Override
	public int Permissao() {
		return FinanceiroConsultarRepassePrefeituraWebServiceDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		
		String stAcao = PAGINA_FINANCEIRO_CONSULTAR_REPASSE_PREFEITURA_WEBSERVICE;
		
		FinanceiroConsultarRepassePrefeituraWebServiceNe financeiroConsultarRepassePrefeituraWebServiceRelatorioNe = new FinanceiroConsultarRepassePrefeituraWebServiceNe();
		
		//Variáveis de sessão
		FinanceiroConsultarRepassePrefeituraWebServiceDt financeiroConsultarRepassePrefeituraDt = (FinanceiroConsultarRepassePrefeituraWebServiceDt) request.getSession().getAttribute("FinanceiroConsultarRepassePrefeituraWebServiceDt");
		if( financeiroConsultarRepassePrefeituraDt == null ) financeiroConsultarRepassePrefeituraDt = new FinanceiroConsultarRepassePrefeituraWebServiceDt();
				
		//********************************************
		//Requests - Parameter
		financeiroConsultarRepassePrefeituraDt.setDataMovimento(request.getParameter("dataMovimento"));		
		int passoEditar = 0;
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		
		//********************************************
		//Requests - Attribute
		request.setAttribute("tempPrograma", "Financeiro - Consultar Repasse Prefeitura de Goiânia WebService");
		request.setAttribute("tempRetorno", "FinanceiroConsultarRepassePrefeituraWebService");
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PassoEditar", passoEditar);
						
		switch (paginaatual) {
		
			case Configuracao.Novo : {
				request.getSession().removeAttribute("FinanceiroConsultarRepassePrefeituraWebServiceDt");
				financeiroConsultarRepassePrefeituraDt = new FinanceiroConsultarRepassePrefeituraWebServiceDt();
				break;
			}
			
			case Configuracao.Imprimir:	
				financeiroConsultarRepassePrefeituraDt.setRelatorio(financeiroConsultarRepassePrefeituraWebServiceRelatorioNe.obtenhaRelatorioRepassePrefeitura(financeiroConsultarRepassePrefeituraDt));
				
				if (financeiroConsultarRepassePrefeituraDt.getRelatorio().possuiRepasse()) {
					String byTemp = financeiroConsultarRepassePrefeituraDt.getRelatorio().getConteudoArquivoCSV();						
					
					enviarCSV(response, byTemp, financeiroConsultarRepassePrefeituraDt.getRelatorio().getNomeArquivoCSV(financeiroConsultarRepassePrefeituraDt.getDataMovimento()));					
					byTemp = null;				
					return;
				} else {
					super.exibaMensagemInconsistenciaErro(request, "Não existem repasses para o período informado.");
				}
				
				break;
		
			case Configuracao.Localizar: 
				FinanceiroConsultarRepassePrefeituraWebServiceNe financeiroConsultarRepassePrefeituraWebServiceNe = new FinanceiroConsultarRepassePrefeituraWebServiceNe();
				financeiroConsultarRepassePrefeituraDt.setRelatorio(financeiroConsultarRepassePrefeituraWebServiceNe.obtenhaRelatorioRepassePrefeitura(financeiroConsultarRepassePrefeituraDt));
				break;
			
			case Configuracao.Salvar:
				if (passoEditar == PASSO_EDITAR_REPASSE)
					request.setAttribute("Mensagem", "Clique para confirmar que o repasse das guias acima foi realizado ao TJGO");
				else if (passoEditar == PASSO_EDITAR_PAGAMENTO)
					request.setAttribute("Mensagem", "Clique para confirmar que o pagamento das guias acima foi realizado à Prefeitura pela parte");
				break;
				
			case Configuracao.SalvarResultado:
				financeiroConsultarRepassePrefeituraDt.setId_Usuario(UsuarioSessao.getId_Usuario());
				financeiroConsultarRepassePrefeituraDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
				if (passoEditar == PASSO_EDITAR_REPASSE) {
					StringBuilder mensagemRetorno = financeiroConsultarRepassePrefeituraWebServiceRelatorioNe.salveRepasseGuiasProjudi(financeiroConsultarRepassePrefeituraDt);
					super.exibaMensagemSucesso(request, "Reapasse atualizado com sucesso nas guias do projudi e SPG." + (mensagemRetorno != null && mensagemRetorno.length() > 0 ? " Observações: \n" + mensagemRetorno.toString() : ""));						
				} else if (passoEditar == PASSO_EDITAR_PAGAMENTO) {
					StringBuilder mensagemRetorno = financeiroConsultarRepassePrefeituraWebServiceRelatorioNe.salvePagamentoGuiasProjudi(financeiroConsultarRepassePrefeituraDt);
					super.exibaMensagemSucesso(request, "Pagamento da Prefeitura atualizado com sucesso nas guias do projudi." + (mensagemRetorno != null && mensagemRetorno.length() > 0 ? " Observações: \n" + mensagemRetorno.toString() : ""));
				}
				
				financeiroConsultarRepassePrefeituraDt.limpeRelatorio();
				
				break;
		}		
		
		request.getSession().setAttribute("FinanceiroConsultarRepassePrefeituraWebServiceDt", financeiroConsultarRepassePrefeituraDt);
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
}
