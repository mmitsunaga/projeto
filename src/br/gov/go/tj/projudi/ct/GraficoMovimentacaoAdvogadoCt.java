package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GraficoMovimentacaoAdvogadoDt;
import br.gov.go.tj.projudi.dt.GraficoMovimentacaoAdvogadoDt.EnumDataRelatorio;
import br.gov.go.tj.projudi.ne.GraficoMovimentacaoAdvogadoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.TJDataHora;

/**
 * Servlet implementation class GraficoMovimentacaoAdvogadoCt
 * Classe:     GraficoMovimentacaoAdvogadoCt
 * Autor:      Márcio Mendonça Gomes 
 * Data:       02/2013
 * Finalidade: Realizar a lógica de acordo com os estados (passoBusca) das páginas htmls
 */ 
public class GraficoMovimentacaoAdvogadoCt extends Controle {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1948003835298718484L;
	
	@Override
	public int Permissao() {		
		return GraficoMovimentacaoAdvogadoDt.CodigoPermissao;
	}	
	
	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String nomebusca, String posicaopaginaatual) throws Exception, ServletException, IOException {
				
		byte[] byTemp = null;
		
		GraficoMovimentacaoAdvogadoDt graficoMovimentacaoAdvogadoDt = (GraficoMovimentacaoAdvogadoDt) request.getSession().getAttribute("graficoMovimentacaoAdvogadoDt");
		if (graficoMovimentacaoAdvogadoDt == null) graficoMovimentacaoAdvogadoDt = new GraficoMovimentacaoAdvogadoDt();		
		
		GraficoMovimentacaoAdvogadoNe graficoMovimentacaoAdvogadoNe = (GraficoMovimentacaoAdvogadoNe) request.getSession().getAttribute("graficoMovimentacaoAdvogadoNe");
		if (graficoMovimentacaoAdvogadoNe == null) graficoMovimentacaoAdvogadoNe = new GraficoMovimentacaoAdvogadoNe();
				
		//Obtem os intervalos de data...			
		if (request.getParameter("DataAnalise") != null && !request.getParameter("DataAnalise").equals("")) {
			graficoMovimentacaoAdvogadoDt.setDataMovimentacaoAnalise(request.getParameter("DataAnalise"));
		}
		if (request.getParameter("DataComparacao") != null && !request.getParameter("DataComparacao").equals("")) {
			graficoMovimentacaoAdvogadoDt.setDataMovimentacaoComparacao(request.getParameter("DataComparacao"));
		}
		if (request.getParameter("DataAdicional001") != null && !request.getParameter("DataAdicional001").equals("")) {
			graficoMovimentacaoAdvogadoDt.setDataAdicional001(request.getParameter("DataAdicional001"));
		}
		if (request.getParameter("DataAdicional002") != null && !request.getParameter("DataAdicional002").equals("")) {
			graficoMovimentacaoAdvogadoDt.setDataAdicional002(request.getParameter("DataAdicional002"));
		}
		if (request.getParameter("DataAdicional003") != null && !request.getParameter("DataAdicional003").equals("")) {
			graficoMovimentacaoAdvogadoDt.setDataAdicional003(request.getParameter("DataAdicional003"));
		}
		
		switch (paginaatual) {
			case Configuracao.Novo: //Processos por Item Produtividade - Novo gráfico
				graficoMovimentacaoAdvogadoDt.limpar();							
				break;
				
			case Configuracao.Imprimir:
				if (validaParametros(graficoMovimentacaoAdvogadoDt, request))				{
					byTemp = graficoMovimentacaoAdvogadoNe.GeraRelatorioGrafico(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , graficoMovimentacaoAdvogadoDt, UsuarioSessao.getUsuarioDt().getNome());
										
					enviarPDF(response,byTemp,"Relatorio");
					return;
				}
				break;
		}	
		
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("PaginaAtual", Configuracao.Editar);		
		request.getSession().setAttribute("graficoMovimentacaoAdvogadoDt", graficoMovimentacaoAdvogadoDt);	
		super.redirecione(super.obtenhaAcaoJSP("GraficoMovimentacaoAdvogado"), request, response);
	}	
	
	/**
     * Valida os parâmetros
     *      
     * @param GraficoMovimentacaoAdvogadoDt graficoMovimentacaoAdvogadoDt
     * @param HttpServletRequest request
     *  
     */
	protected boolean validaParametros(GraficoMovimentacaoAdvogadoDt graficoMovimentacaoAdvogadoDt, HttpServletRequest request) throws Exception{				
		String mensagemRetorno = "";	
				
		if (periodoEhSuperiorADataDeOntem(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise())){
			mensagemRetorno = obtenhaMensagemPeriodoEhSuperiorADataDeOntem("para análise");
		} else if (periodoEhSuperiorADataDeOntem(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao())){
			mensagemRetorno = obtenhaMensagemPeriodoEhSuperiorADataDeOntem("para comparação");			
		} else if (periodoEhSuperiorADataDeOntem(graficoMovimentacaoAdvogadoDt.getDataAdicional001())){
			mensagemRetorno = obtenhaMensagemPeriodoEhSuperiorADataDeOntem("adicional 001");
		} else if (periodoEhSuperiorADataDeOntem(graficoMovimentacaoAdvogadoDt.getDataAdicional002())){
			mensagemRetorno = obtenhaMensagemPeriodoEhSuperiorADataDeOntem("adicional 002");
		} else if (periodoEhSuperiorADataDeOntem(graficoMovimentacaoAdvogadoDt.getDataAdicional003())){
			mensagemRetorno = obtenhaMensagemPeriodoEhSuperiorADataDeOntem("adicional 003");
		} 
		
		mensagemRetorno += valideDatasIguais(graficoMovimentacaoAdvogadoDt, "para análise", graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise(), EnumDataRelatorio.MOVIMENTACAO_ANALISE);		
		mensagemRetorno += valideDatasIguais(graficoMovimentacaoAdvogadoDt, "para comparação", graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao(), EnumDataRelatorio.MOVIMENTACAO_COMPARACAO);		
		mensagemRetorno += valideDatasIguais(graficoMovimentacaoAdvogadoDt, "adicional 001", graficoMovimentacaoAdvogadoDt.getDataAdicional001(), EnumDataRelatorio.ADICIONAL_001);
		mensagemRetorno += valideDatasIguais(graficoMovimentacaoAdvogadoDt, "adicional 002", graficoMovimentacaoAdvogadoDt.getDataAdicional002(), EnumDataRelatorio.ADICIONAL_002);
		mensagemRetorno += valideDatasIguais(graficoMovimentacaoAdvogadoDt, "adicional 003", graficoMovimentacaoAdvogadoDt.getDataAdicional003(), EnumDataRelatorio.ADICIONAL_003);
		
		super.exibaMensagemInconsistenciaErro(request, mensagemRetorno);	
		
		return (mensagemRetorno.equals(""));
	}
	
	private String valideDatasIguais(GraficoMovimentacaoAdvogadoDt graficoMovimentacaoAdvogadoDt, String nomeCampo, TJDataHora datahora, EnumDataRelatorio enumDataRelatorio)
	{
		String mensagemRetorno = "";
		
		if (datahora != null)
		{
			//mensagemRetorno = 
			if (enumDataRelatorio != EnumDataRelatorio.MOVIMENTACAO_ANALISE && datahora.equals(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoAnalise()))
			{
				mensagemRetorno += String.format("A data %s deve ser diferente da data de análise.", nomeCampo);
			}
			
			if (enumDataRelatorio != EnumDataRelatorio.MOVIMENTACAO_COMPARACAO && datahora.equals(graficoMovimentacaoAdvogadoDt.getDataMovimentacaoComparacao()))
			{
				mensagemRetorno += String.format("A data %s deve ser diferente da data de comparação.", nomeCampo);
			}
			
			if (enumDataRelatorio != EnumDataRelatorio.ADICIONAL_001 && graficoMovimentacaoAdvogadoDt.getDataAdicional001() != null && datahora.equals(graficoMovimentacaoAdvogadoDt.getDataAdicional001()))
			{
				mensagemRetorno += String.format("A data %s deve ser diferente da data adicional 001.", nomeCampo);
			}
			
			if (enumDataRelatorio != EnumDataRelatorio.ADICIONAL_002 && graficoMovimentacaoAdvogadoDt.getDataAdicional002() != null && datahora.equals(graficoMovimentacaoAdvogadoDt.getDataAdicional002()))
			{
				mensagemRetorno += String.format("A data %s deve ser diferente da data adicional 002.", nomeCampo);
			}
			
			if (enumDataRelatorio != EnumDataRelatorio.ADICIONAL_003 && graficoMovimentacaoAdvogadoDt.getDataAdicional003() != null && datahora.equals(graficoMovimentacaoAdvogadoDt.getDataAdicional003()))
			{
				mensagemRetorno += String.format("A data %s deve ser diferente da data adicional 003.", nomeCampo);
			}
		}
		
		return mensagemRetorno;
	}
		
	/**
     * Retorna a análise.
     *      
     * @param String nomeCampo 
     *  
     */
	private String obtenhaMensagemPeriodoEhSuperiorADataDeOntem(String nomeCampo)
	{
		return "Relatório disponível somente para dias com expedientes já finalizados. Verifique a data " + nomeCampo + ".";
	}
	
	/**
     * Valida se a data informada é superior à data de ontem
     *      
     * @param TJDataHora data 
     *  
     */
	private boolean periodoEhSuperiorADataDeOntem(TJDataHora data) throws Exception{
		
		if (data == null) return false;
		
		TJDataHora dataOntem = new TJDataHora();
		dataOntem.adicioneDia(-1);
		dataOntem.atualizeUltimaHoraDia();		
		
		return data.ehApos(dataOntem);
	}	
}