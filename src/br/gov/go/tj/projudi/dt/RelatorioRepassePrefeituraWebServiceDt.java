package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     RelatorioRepassePrefeitura.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2015
 */
public class RelatorioRepassePrefeituraWebServiceDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700116459698735195L;

	private List<OcorrenciaPrevisaoRepasseWebServiceDt> listaPrevisaoRepasse = null;
	
	private Double valorTotalDosPagamentos = 0.0D;
	
	private Double valorTotalDosEstornos = 0.0D;
	
	private TJDataHora dataRepasseConfirmado;
	
	private TJDataHora dataPagamentoConfirmado;
	
	public RelatorioRepassePrefeituraWebServiceDt(){		
		this.listaPrevisaoRepasse = new ArrayList<OcorrenciaPrevisaoRepasseWebServiceDt>();
		this.limpar();		
	}
	
	public void adicioneRepasse(OcorrenciaPrevisaoRepasseWebServiceDt ocorrenciaPrevisaoRepasse){
		if (ocorrenciaPrevisaoRepasse.isMovimentoEhPagamento()) 		
			valorTotalDosPagamentos += Funcoes.StringToDouble( Funcoes.FormatarDecimal(ocorrenciaPrevisaoRepasse.getValorcustas()).replace(".", "").replace(",", ".") );
		else
			valorTotalDosEstornos += Funcoes.StringToDouble( Funcoes.FormatarDecimal(ocorrenciaPrevisaoRepasse.getValorcustas()).replace(".", "").replace(",", ".") );
		
		this.listaPrevisaoRepasse.add(ocorrenciaPrevisaoRepasse);
	}
	
	public Iterator<OcorrenciaPrevisaoRepasseWebServiceDt> getIteratorOcorrenciaRepasseDiarias(){
		return listaPrevisaoRepasse.iterator();
	}	
	
	public boolean possuiRepasse(){
		return  !listaPrevisaoRepasse.isEmpty();
	}	
	
	public void limpar(){		
		this.listaPrevisaoRepasse.clear();
				
		valorTotalDosPagamentos = 0.0D;
		
		valorTotalDosEstornos = 0.0D;
		
		dataRepasseConfirmado = null;
		
		dataPagamentoConfirmado = null;
	}
	
	public String getValorTotalDosPagamentos(){
		return Funcoes.FormatarDecimal(valorTotalDosPagamentos);
	}

	public String getValorTotalDosEstornos() {
		return Funcoes.FormatarDecimal(valorTotalDosEstornos);
	}
	
	public String getValorRepasse() {
		if (valorTotalDosEstornos > valorTotalDosPagamentos)
			return "0,00 - Saldo Negativo: R$ " + Funcoes.FormatarDecimal(valorTotalDosEstornos - valorTotalDosPagamentos);
		
		return Funcoes.FormatarDecimal(valorTotalDosPagamentos - valorTotalDosEstornos);
	}
	
	private final String SEPARADOR_CSV = ";";
	
	public String getConteudoArquivoCSV() {
		StringBuilder conteudo = new StringBuilder();
		
		conteudo.append("SEQ");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("PROCESSO");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("GUIA");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("TIPO MOVIMENTO");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("VALOR ARRECADAÇÃO");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("DATA ARRECADAÇÃO");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("BANCO ARRECADAÇÃO");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("AGÊNCIA ARRECADAÇÃO");
		conteudo.append("\n");	
		
		Iterator<OcorrenciaPrevisaoRepasseWebServiceDt> iterator = getIteratorOcorrenciaRepasseDiarias();
		
		OcorrenciaPrevisaoRepasseWebServiceDt objTemp = null;																						
																																												
		long qtdeLinha = 0;
		
		while (iterator.hasNext()){
			objTemp = iterator.next();
			
			qtdeLinha += 1;
			
			conteudo.append(qtdeLinha);
			conteudo.append(SEPARADOR_CSV);
			conteudo.append(objTemp.getNumeroProcesso());
			conteudo.append(SEPARADOR_CSV);
			conteudo.append(objTemp.getNumeroGuia());
			conteudo.append(SEPARADOR_CSV);
			conteudo.append(objTemp.getTextoMovimento());
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("R$ " + Funcoes.FormatarDecimal(objTemp.getValorcustas()).replace(".", "").replace(",", "."));
			conteudo.append(SEPARADOR_CSV);
			conteudo.append(objTemp.getDataArrecadacao().getDataFormatadaddMMyyyy());
			conteudo.append(SEPARADOR_CSV);
			conteudo.append(objTemp.getNumeroBancoPagamento());
			conteudo.append(SEPARADOR_CSV);
			conteudo.append(objTemp.getNumeroAgenciaPagamento());
			conteudo.append("\n");
		}	
		conteudo.append("");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("Total de Pagamentos:");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append(getValorTotalDosPagamentos().replace(".", "").replace(",", "."));
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("Total de Estornos:");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append(getValorTotalDosEstornos().replace(".", "").replace(",", "."));
		conteudo.append(SEPARADOR_CSV);
		conteudo.append("Total Repasse:");
		conteudo.append(SEPARADOR_CSV);
		conteudo.append(getValorRepasse().replace(".", "").replace(",", "."));
		conteudo.append("\n");		
		
		return conteudo.toString();
	}
	
	public String getNomeArquivoCSV(TJDataHora dataRepasse) {
		StringBuilder nomeArquivo = new StringBuilder();
		
		nomeArquivo.append("REPASSE_PREFEITURA_GOIANIA_BAIXA_EM_");
		nomeArquivo.append(dataRepasse.getDataHoraFormatadayyyyMMdd());		
		
		return nomeArquivo.toString();
	}

	public TJDataHora getDataRepasseConfirmado() {
		return dataRepasseConfirmado;
	}

	public void setDataRepasseConfirmado(TJDataHora dataRepasseConfirmado) {
		this.dataRepasseConfirmado = dataRepasseConfirmado;
	}

	public TJDataHora getDataPagamentoConfirmado() {
		return dataPagamentoConfirmado;
	}

	public void setDataPagamentoConfirmado(TJDataHora dataPagamentoConfirmado) {
		this.dataPagamentoConfirmado = dataPagamentoConfirmado;
	}
}
