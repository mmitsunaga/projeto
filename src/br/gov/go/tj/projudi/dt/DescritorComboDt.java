package br.gov.go.tj.projudi.dt;

public class DescritorComboDt extends Dados {

	private static final long serialVersionUID = -5629507453088953964L;
	
	private String Id;
	private String Descricao;
	
	public DescritorComboDt() {
		
	}
	
	public DescritorComboDt (String id, String descricao) {
		this.Id = id;
		this.Descricao = descricao;
	}
	
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	
	public String getDescricao() {
		return Descricao;
	}
	public void setDescricao(String descricao) {
		Descricao = descricao;
	}
}
