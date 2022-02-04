package br.gov.go.tj.projudi.dt;

public class AtributoLogDt {

	private String nomeCampo;
	private String valorAntigo;
	private String valorNovo;
	
	public AtributoLogDt(){
	}
	
	public AtributoLogDt(String nomeCampo, String valorAntigo, String valorNovo){
		
		this.nomeCampo = nomeCampo;
		this.valorAntigo = valorAntigo;
		this.valorNovo = valorNovo;
	}

	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	public String getValorAntigo() {
		return valorAntigo;
	}

	public void setValorAntigo(String valorAntigo) {
		this.valorAntigo = valorAntigo;
	}

	public String getValorNovo() {
		return valorNovo;
	}

	public void setValorNovo(String valorNovo) {
		this.valorNovo = valorNovo;
	}	
	
	public boolean isDiferente(){
		
		boolean boTemp = false;
		//sempre que for diferente vou mostrar
		if((valorAntigo != null && !valorAntigo.equalsIgnoreCase("null") && !valorAntigo.isEmpty())){
			
			if  (valorNovo != null && !valorNovo.equalsIgnoreCase("null") && !valorNovo.isEmpty()){
					
				if(valorAntigo.equalsIgnoreCase(valorNovo)){
					boTemp = false;
				} else {
					boTemp = true;					
				}
			}else{
				boTemp = true;
			}
		}else if  (valorNovo != null && !valorNovo.equalsIgnoreCase("null") && !valorNovo.isEmpty()){
			boTemp = true;			
		}
		return boTemp;
	}
}
