package br.gov.go.tj.projudi.dt.indexacao;

import br.gov.go.tj.utils.ValidacaoUtil;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Hit {

	@JsonProperty(value = "_index")
    private String index;
 
    @JsonProperty(value = "_type")
    private String type;
 
    @JsonProperty(value = "_id")
    private String id;
 
    @JsonProperty(value = "_score")
    private Double score;
 
    @JsonProperty(value = "_source")
    private Documento source;
    
    @JsonProperty(value = "highlight")
    private Highlight highlight;
        
    @JsonProperty(value = "sort")
    private String[] sort;

	public String getIndex() {
		return index;
	}	
	public void setIndex(String index) {
		this.index = index;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Documento getSource() {
		return source;
	}
	public void setSource(Documento source) {
		this.source = source;
	}
	public Highlight getHighlight() {
		return ValidacaoUtil.isNulo(highlight) ? new Highlight() : highlight;
	}
	public void setHighlight(Highlight highlight) {
		this.highlight = highlight;
	}
	public String[] getSort() {
		return sort;
	}
	public void setSort(String[] sort) {
		this.sort = sort;
	}
	
	public String getTextoDestacado(){		
		if (ValidacaoUtil.isNaoVazio(this.getSource().getTexto())){			
			StringBuilder texto = new StringBuilder(this.getSource().getTexto());
			if (ValidacaoUtil.isNaoVazio(this.getHighlight().getTexto())){
				for (String termoDestaque : this.getHighlight().getTexto()){
					String termoSemDestaque = termoDestaque.replaceAll("\\<em[^>]*>|</em>","");				
					int offset = texto.indexOf(termoSemDestaque);
					int tam = termoSemDestaque.length();
					texto.replace(offset, offset+tam, termoDestaque);
				}
			}
			return texto.toString();					
		} else {
			return null;
		}		
	}
	
	public String getExtraDestacado(){
		if (ValidacaoUtil.isNaoVazio(this.getSource().getExtra())){
			StringBuilder extra = new StringBuilder(this.getSource().getExtra());
			if (ValidacaoUtil.isNaoVazio(this.getHighlight().getExtra())){
				for (String termoDestaque : this.getHighlight().getExtra()){
					String termoSemDestaque = termoDestaque.replaceAll("\\<em[^>]*>|</em>","");			
					int offset = extra.indexOf(termoSemDestaque);
					int tam = termoSemDestaque.length();
					extra.replace(offset, offset+tam, termoDestaque);
				}
			}
			return extra.toString();
		} else {
			return null;
		}
	}
	
}