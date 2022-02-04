package br.gov.go.tj.projudi.dt;

public class ServentiaCargoEscalaStatusDt extends ServentiaCargoEscalaStatusDtGen {

	private static final long serialVersionUID = 5957004294312172340L;

	public static final int CodigoPermissao = 591;
	public static final Integer ATIVO = 1;
	public static final Integer INATIVO = 2;
	public static final Integer SUSPENSO = 3;
	
	public String dataStatus;

	public ServentiaCargoEscalaStatusDt() {
		limpar();
	}
	
	public void limpar() {
		dataStatus = "";
		super.limpar();
	}
	
	public String getDataStatus() {return this.dataStatus;}
	public void setDataStatus(String dataStatus) {this.dataStatus = dataStatus;}

}