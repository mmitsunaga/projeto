package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.List;

/**
 * Dt filho responsável em motar o relatório txt de histórico das alterações da zona
 * @author mmgomes - 19/08/2015
 * 
 */
public class RelatorioZonaHistoricoDt implements Serializable {
	   
	/**
	 * 
	 */
	private static final long serialVersionUID = 5352955322673375108L;

	public static final int CodigoPermissao=172;
		
	private static final String startTagTabela = "<table id=\"Tabela\" class=\"Tabela\">\n";	
	private static final String endtTagTabela = "</table>\n";
	private static final String startTagBodyTabela = "<tbody>\n";	
	private static final String endTagBodyTabela = "</tbody>\n";
	
	private List<ZonaHistoricoDt> listaDeHistoricos;
	
	public void setListaZonaHistorico(List<ZonaHistoricoDt> listaDeHistoricos) {
		this.listaDeHistoricos = listaDeHistoricos;				
	}
	
	public boolean possuiHistorico() {
		return (this.listaDeHistoricos != null);
	}
	
	public String getTextoHistorico()
	{
		if (this.listaDeHistoricos == null || this.listaDeHistoricos.size() == 0) return "Não foram encontradas alterações nessa zona!";
	
		String conteudoTabela = "";
		
		for(ZonaHistoricoDt zonaHistorico : this.listaDeHistoricos) {
			conteudoTabela += startTagTabela;
			conteudoTabela += getHeaderTabelaValores(zonaHistorico);
			conteudoTabela += "\t" + startTagBodyTabela;
			
			conteudoTabela += getRowTabelaValores(zonaHistorico);				
			
			conteudoTabela += "\t" + endTagBodyTabela;
			conteudoTabela += endtTagTabela;
		}
				
		return conteudoTabela;		
	}
	
	private String getRowTabelaValores(ZonaHistoricoDt zonaHistorico)
	{
		String rowTabela = "";
		boolean linha = false;
		
		rowTabela += getRowTabelaValores("Identificador", zonaHistorico.getId(), linha);
		linha = !linha;	
		
		rowTabela += getRowTabelaValores("Zona", zonaHistorico.getZonaDt().getZona(), linha);
		linha = !linha;
		
		rowTabela += getRowTabelaValores("Código Zona", zonaHistorico.getZonaDt().getZonaCodigo(), linha);
		linha = !linha;
		
		rowTabela += getRowTabelaValores("Comarca", zonaHistorico.getZonaDt().getId_Comarca() + " - " + zonaHistorico.getZonaDt().getComarca(), linha);
		linha = !linha;
		
		rowTabela += getRowTabelaValores("Valor Cível", zonaHistorico.getZonaDt().getValorCivel(), linha);
		linha = !linha;
		
		rowTabela += getRowTabelaValores("Valor Criminal", zonaHistorico.getZonaDt().getValorCriminal(), linha);
		linha = !linha;
		
		rowTabela += getRowTabelaValores("Valor Conta Vinculada", zonaHistorico.getZonaDt().getValorContaVinculada(), linha);
		linha = !linha;
		
		rowTabela += getRowTabelaValores("Valor Conta Segundo Grau", zonaHistorico.getZonaDt().getValorSegundoGrau(), linha);
		linha = !linha;
		
		rowTabela += getRowTabelaValores("Valor 2º Grau Contadoria", zonaHistorico.getZonaDt().getValorSegundoGrauContadoria(), linha);
		linha = !linha;
		
		return rowTabela;	
	}
	
	private String getRowTabelaValores(String nomeCampo, String valorCampo, boolean linha)
	{
		String rowTabela = "\t\t\t\t\t\t<tr class=\"TabelaLinha" + (linha?1:2)+ "\">\n";
		
		rowTabela += "\t\t\t\t\t\t\t<td colspan=\"2\">" + nomeCampo + "</td>\n";
		
		rowTabela += "\t\t\t\t\t\t\t<td>" + valorCampo + "</td>\n";
			
		rowTabela += "\t\t\t\t\t\t</tr>\n";		
	
		return rowTabela;	
	}
	
	private String getHeaderTabelaValores(ZonaHistoricoDt zonaHistorico)
	{
		
		String headerTabela = "\t\t\t\t\t<thead>\n";		
		
		headerTabela += "\t\t\t\t\t\t<tr class=\"TituloColuna1\">\n";	
		
		headerTabela += "\t\t\t\t\t\t\t<th width=\"20%\">Data Início </th>\n";
		
		headerTabela += "\t\t\t\t\t\t\t<th width=\"20%\">Data Fim </th>\n";
		
		headerTabela += "\t\t\t\t\t\t\t<th width=\"60%\">Usuário</th>\n";		
		
		headerTabela += "\t\t\t\t\t\t</tr>\n";
		
		headerTabela += "\t\t\t\t\t\t<tr class=\"TituloColuna\">\n";
		
		headerTabela += "\t\t\t\t\t\t\t<th width=\"25%\">" + zonaHistorico.getZonaDt().getDataInicio()  + "</th>\n";
		
		headerTabela += "\t\t\t\t\t\t\t<th width=\"25%\">" + zonaHistorico.getDataFim() + "</th>\n";
		
		headerTabela += "\t\t\t\t\t\t\t<th width=\"50%\">" + zonaHistorico.getNomeUsuario() + "</th>\n";
		
		headerTabela += "\t\t\t\t\t\t</tr>\n";
		
		headerTabela += "\t\t\t\t\t\t<tr class=\"TituloColuna\">\n";	
		
		headerTabela += "\t\t\t\t\t\t\t<th colspan=\"2\" width=\"50%\">Campo</th>\n";
		
		headerTabela += "\t\t\t\t\t\t\t<th width=\"50%\">Valor</th>\n";
		
		headerTabela += "\t\t\t\t\t\t</tr>\n";
		
		headerTabela += "\t\t\t\t\t</thead>\n";
		
		return headerTabela;
	}
}