package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.FinanceiroConsultarRepassePrefeituraDt;
import br.gov.go.tj.projudi.ne.FinanceiroConsultarRepassePrefeituraNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.TJDataHora;

public class FinanceiroConsultarRepassePrefeituraCt extends Controle {

	private static final long serialVersionUID = -1804822061775298448L;

	private static final String PAGINA_FINANCEIRO_CONSULTAR_REPASSE_PREFEITURA 	= "/WEB-INF/jsptjgo/FinanceiroConsultarRepassePrefeitura.jsp";
	
	private static final String ID_SERVENTIA_PREFEITURA = "613";
	private static final String ID_GUIA_MODELO_PREFEITURA = "13933";
	//private static final String ID_GUIA_MODELO_PREFEITURA = "15751"; //DESENVOLVIMENTO
	
	private final String ID_RELATORIO_PDF = "RelatorioPDF";
	private final String CODIGO_RASH_RELATORIO_PDF = "FinanceiroConsultarRepassePrefeituraCt";
	
	@Override
	public int Permissao() {
		return FinanceiroConsultarRepassePrefeituraDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {
		
		String stAcao = PAGINA_FINANCEIRO_CONSULTAR_REPASSE_PREFEITURA;
		
		//Variáveis de sessão
		FinanceiroConsultarRepassePrefeituraDt financeiroConsultarRepassePrefeituraDt = (FinanceiroConsultarRepassePrefeituraDt) request.getSession().getAttribute("FinanceiroConsultarRepassePrefeituraDt");
		if( financeiroConsultarRepassePrefeituraDt == null ) {
			financeiroConsultarRepassePrefeituraDt = new FinanceiroConsultarRepassePrefeituraDt();
			//Inicialmente será apenas para a serventia Goiânia - 1ª Vara da Fazenda Pública Municipal (Prefeitura de Goiânia), quando for implantar para as demais serventias esse filtro estará em tela para o usuário selecionar.
			financeiroConsultarRepassePrefeituraDt.setId(ID_SERVENTIA_PREFEITURA);
			financeiroConsultarRepassePrefeituraDt.setIdGuiaModelo(ID_GUIA_MODELO_PREFEITURA);
		}
		
		//********************************************
		//Requests - Attribute
		request.setAttribute("tempPrograma", "Financeiro - Consultar Previsão de Repasse Prefeitura de Goiânia");
		request.setAttribute("tempRetorno", "FinanceiroConsultarRepassePrefeitura");
		
		//********************************************
		//Requests - Parameter
		financeiroConsultarRepassePrefeituraDt.setTipoFiltroData( request.getParameter("tipoFiltroData") );
		financeiroConsultarRepassePrefeituraDt.setDataInicio( request.getParameter("dataInicio") );
		financeiroConsultarRepassePrefeituraDt.setDataFim( request.getParameter("dataFim") );
		financeiroConsultarRepassePrefeituraDt.setTipoFiltroDetalhe( request.getParameter("tipoFiltroDetalhe") );		
				
		switch (paginaatual) {
		
			case Configuracao.Novo : {
				request.getSession().removeAttribute("FinanceiroConsultarRepassePrefeituraDt");
				financeiroConsultarRepassePrefeituraDt = new FinanceiroConsultarRepassePrefeituraDt();
				financeiroConsultarRepassePrefeituraDt.setId(ID_SERVENTIA_PREFEITURA);
				financeiroConsultarRepassePrefeituraDt.setIdGuiaModelo(ID_GUIA_MODELO_PREFEITURA);				
				
				break;
			}
			
			case Configuracao.Imprimir:	
				if (validaParametros(financeiroConsultarRepassePrefeituraDt, request)) {
					FinanceiroConsultarRepassePrefeituraNe financeiroConsultarRepassePrefeituraNe = new FinanceiroConsultarRepassePrefeituraNe();
					financeiroConsultarRepassePrefeituraDt.setRelatorio(financeiroConsultarRepassePrefeituraNe.obtenhaRelatorioPrevisaoRepassePrefeitura(financeiroConsultarRepassePrefeituraDt));
					
					if (financeiroConsultarRepassePrefeituraDt.getRelatorio().possuiPrevisaoParaRepasse()) {
						String stTemp = financeiroConsultarRepassePrefeituraDt.getRelatorio().getConteudoArquivoCSV();	
						String nome = financeiroConsultarRepassePrefeituraDt.getRelatorio().getNomeArquivoCSV();
												
						enviarCSV(response,stTemp, nome);
						
						stTemp = null;				
						return;
					} else {
						super.exibaMensagemInconsistenciaErro(request, "Não existem repasses para o período informado.");
					}
				}
				
				break;
		
			case Configuracao.Localizar: 
				if (validaParametros(financeiroConsultarRepassePrefeituraDt, request)) {
					FinanceiroConsultarRepassePrefeituraNe financeiroConsultarRepassePrefeituraNe = new FinanceiroConsultarRepassePrefeituraNe();
					financeiroConsultarRepassePrefeituraDt.setRelatorio(financeiroConsultarRepassePrefeituraNe.obtenhaRelatorioPrevisaoRepassePrefeitura(financeiroConsultarRepassePrefeituraDt));					
				}
				break;
		}		
		
		request.getSession().setAttribute("FinanceiroConsultarRepassePrefeituraDt", financeiroConsultarRepassePrefeituraDt);
		
		RequestDispatcher dis =	request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	

	protected boolean validaParametros(FinanceiroConsultarRepassePrefeituraDt financeiroConsultarRepassePrefeituraDt,
			                             HttpServletRequest request) throws Exception{				
		String mensagemRetorno = "";	
				
		if (financeiroConsultarRepassePrefeituraDt.getDataInicio().ehApos(financeiroConsultarRepassePrefeituraDt.getDataFim())) {
			mensagemRetorno += "Data Inicial deve ser anterior ou igual a Data Final. ";
		}
		else if (periodoSelecionadoEhSuperiorACincoDias(financeiroConsultarRepassePrefeituraDt)){
			mensagemRetorno += String.format("O intervalo entre a data inicial e da data final não deve ser superior a 60 dias.");
		}	
		
		super.exibaMensagemInconsistenciaErro(request, mensagemRetorno);	
		
		return (mensagemRetorno.equals(""));
	}
	
	private boolean periodoSelecionadoEhSuperiorACincoDias(FinanceiroConsultarRepassePrefeituraDt financeiroConsultarRepassePrefeituraDt){
		
		TJDataHora dataComparacao = TJDataHora.CloneObjeto(financeiroConsultarRepassePrefeituraDt.getDataInicio());
		dataComparacao.adicioneDia(60);
		dataComparacao.atualizeUltimaHoraDia();		
		
		return (financeiroConsultarRepassePrefeituraDt.getDataFim().ehApos(dataComparacao));
	}	
}
