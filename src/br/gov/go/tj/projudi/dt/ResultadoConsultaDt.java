package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Dt responsável em motar o resultado da consulta sql
 * @author mmgomes - 04/02/2013
 * 
 */
public class ResultadoConsultaDt implements Serializable {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -6826026312984060023L;

	public static final int CodigoPermissao = 226;
		
	private static final String startTagTabela = "<table id=\"Tabela\" class=\"Tabela\">\n";	
	private static final String endtTagTabela = "</table>\n";
	private static final String startTagBodyTabela = "<tbody>\n";	
	private static final String endTagBodyTabela = "</tbody>\n";
	
	private List<String> listaDeCampos;
	private List<List<String>> listaDeValores;
	
	public ResultadoConsultaDt(){		
		this.listaDeCampos = new ArrayList<String>();
		this.listaDeValores = new ArrayList<List<String>>();
	}
	
	public void setNomeCampo(String nomeCampo)
	{
		this.listaDeCampos.add(nomeCampo);
	}
	
	public void setValoresCampo(List<String> listaDeCampos)
	{
		this.listaDeValores.add(listaDeCampos);
	}
	
	public boolean PossuiResultados() {
		return (listaDeValores.size() > 0);
	}
	
	public String getTextoResultadoConsulta()
	{
		boolean linha = false;
		
		if(!PossuiResultados()) return "";
	
		String conteudoTabela = "";	
		
		conteudoTabela += startTagTabela;
		conteudoTabela += getHeaderTabela();
		conteudoTabela += "\t" + startTagBodyTabela;
		Iterator iteratorValores = this.listaDeValores.iterator();	
		while (iteratorValores.hasNext())
		{	
			conteudoTabela += getRowTabela((List<String>) iteratorValores.next(), linha);				
			linha = !linha;
		}
		conteudoTabela += "\t" + endTagBodyTabela;
		conteudoTabela += endtTagTabela;
		
		return conteudoTabela;		
	}
	
	private String getHeaderTabela()
	{		
		String headerTabela = "\t<thead>\n";
		
		headerTabela += "\t\t\t<tr class=\"TituloColuna1\">\n";
		
		Iterator iteratorCampos = this.listaDeCampos.iterator();	
		while (iteratorCampos.hasNext())
			headerTabela += "\t\t\t\t<th>" + (String) iteratorCampos.next() + "</th>\n";		
		
		headerTabela += "\t\t\t</tr>\n";	
		
		headerTabela += "\t</thead>\n";
		
		return headerTabela;
	}
	
	private String getRowTabela(List<String> listaDeValoresLinha, boolean linha)
	{	
		String rowTabela = "";
		
		rowTabela += "\t\t<tr class=\"TabelaLinha" + (linha?1:2)+ "\">\n";
		
		Iterator iteratorValores = listaDeValoresLinha.iterator();	
		while (iteratorValores.hasNext())
			rowTabela += "\t\t\t\t<td>" + (String) iteratorValores.next() + "</td>\n";
	
		rowTabela += "\t\t</tr>\n";		
	
		return rowTabela;		
	}
}