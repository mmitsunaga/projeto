package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     OcorrenciaDiariaPrevisaoRepasse.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       06/2015
 * Finalidade: Agrupar as previsões de repasse da prefeitura por dia
 */
public class OcorrenciaDiariaPrevisaoRepasseDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5904504611043381360L;

	private TJDataHora dataPrevisaoRepasse = null;
	
	private List<OcorrenciaPrevisaoRepasseDt> listaDePrevisoesRepasse = null;
	
	private Double valorTotalPrevistoParaRepasse = 0.0D;
	
	private Double valorTotalDasGuias = 0.0D;
	
	
	public OcorrenciaDiariaPrevisaoRepasseDt(){
		this.listaDePrevisoesRepasse = new ArrayList<OcorrenciaPrevisaoRepasseDt>();
	}
	
	public void adicionePeriodo(OcorrenciaPrevisaoRepasseDt previsaoRepasse){
		
		if (previsaoRepasse == null) return;
		
		if (this.dataPrevisaoRepasse == null) this.dataPrevisaoRepasse = previsaoRepasse.getDataPrevisaoRepasse();
		
		this.listaDePrevisoesRepasse.add(previsaoRepasse);
		
		valorTotalPrevistoParaRepasse += Funcoes.StringToDouble( Funcoes.FormatarDecimal(previsaoRepasse.getValorRecebimento()).replace(".", "").replace(",", ".") );
		
		valorTotalDasGuias += Funcoes.StringToDouble( Funcoes.FormatarDecimal(previsaoRepasse.getValorTotalGuia()).replace(".", "").replace(",", ".") );
	}
	
	public List<OcorrenciaPrevisaoRepasseDt> getListaDePrevisaoRepasse(){
		Collections.sort(this.listaDePrevisoesRepasse);
		return this.listaDePrevisoesRepasse;
	}
	
	public Iterator<OcorrenciaPrevisaoRepasseDt> getIteratorDePrevisaoRepasse(){
		Collections.sort(this.listaDePrevisoesRepasse);
		return getListaDePrevisaoRepasse().iterator();
	}
	
	public String getValorTotalPrevistoParaRepasse(){
		return Funcoes.FormatarDecimal(valorTotalPrevistoParaRepasse);
	}
	
	public TJDataHora getDataPrevisaoRepasse(){
		return this.dataPrevisaoRepasse;
	}

	public String getValorTotalDasGuias() {
		return Funcoes.FormatarDecimal(valorTotalDasGuias);
	}
	
	public String getDiferenca() {
		if (valorTotalDasGuias > valorTotalPrevistoParaRepasse)
			return "(" + Funcoes.FormatarDecimal((valorTotalDasGuias - valorTotalPrevistoParaRepasse)) + ")";
		
		return Funcoes.FormatarDecimal((valorTotalDasGuias - valorTotalPrevistoParaRepasse));
	}
}
