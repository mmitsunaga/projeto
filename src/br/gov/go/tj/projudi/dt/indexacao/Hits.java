package br.gov.go.tj.projudi.dt.indexacao;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hits {

	@JsonProperty(value = "total")
	private Double total;
	
	@JsonProperty(value = "max_score")
	private String max_score;

	@JsonProperty(value = "hits")
	private List<Hit> items;

	public Double getTotal() {
		return items != null ? this.total : 0D;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public List<Hit> getItems() {
		return items;
	}
	public void setItems(List<Hit> items) {
		this.items = items;
	}
	public void add(Hit hit){
		if (items == null) items = new ArrayList<Hit>();		
		items.add(hit);
	}
	
}