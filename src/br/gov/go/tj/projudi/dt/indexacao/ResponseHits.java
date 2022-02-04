package br.gov.go.tj.projudi.dt.indexacao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * ResponseHits: 
 * {
 * 	 took: 'Indica o tempo de resposta da consulta',
 * 	 timed_out: '',	
 * 	 hits: {[
 * 		{
 * 			total: 'Quantidade de documentos encontrados'
 * 			type: 'nome do indice',
 * 			id: 'identificado do item da resposta',
 * 			score: 'pontuação de semelhança do item de resposta com o termo pesquisado',
 * 			source: {
 * 				'atributos do documento (classe Documento)
 * 			},  
 * 			highlight: {
 * 				texto: [] 'Texto do documento destacado e que é igual ao termo pesquisado
 * 			}
 * 		}
 * 	 ]}
 * }
 */

@JsonIgnoreProperties({"_shards"})
public class ResponseHits {
	
	@JsonProperty(value = "took")
	private String took;
	
	@JsonProperty(value = "timed_out")
	private String timed_out;
	
	@JsonProperty(value = "hits")
    private Hits hits;

	public String getTook() {
		return took;
	}
	public void setTook(String took) {
		this.took = took;
	}	
	public String getTimed_out() {
		return timed_out;
	}
	public void setTimed_out(String timed_out) {
		this.timed_out = timed_out;
	}
	public Hits getHits() {
		return hits;
	}
	public void setHits(Hits hits) {
		this.hits = hits;
	}	
}

