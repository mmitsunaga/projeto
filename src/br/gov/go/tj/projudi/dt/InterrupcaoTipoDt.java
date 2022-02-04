package br.gov.go.tj.projudi.dt;

public class InterrupcaoTipoDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5012217533056015825L;
	
	public static final int CodigoPermissao = 908;
	
	private String Id_InterrupcaoTipo;
	private String InterrupcaoTipo;
	private String InterrupcaoTotal;

	public InterrupcaoTipoDt() {
		limpar();
	}

	public String getId()  {return Id_InterrupcaoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_InterrupcaoTipo = valor;}
	public String getInterrupcaoTipo()  {return InterrupcaoTipo;}
	public void setInterrupcaoTipo(String valor ) {if (valor!=null) InterrupcaoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getInterrupcaoTotal() {return InterrupcaoTotal;}
	public void setInterrupcaoTotal(String InterrupcaoTotal) {this.InterrupcaoTotal = InterrupcaoTotal;}
	
	public void copiar(InterrupcaoTipoDt objeto){
		Id_InterrupcaoTipo = objeto.getId();
		InterrupcaoTipo = objeto.getInterrupcaoTipo();
		CodigoTemp = objeto.getCodigoTemp();
		InterrupcaoTotal = objeto.getInterrupcaoTotal();		
	}

	public void limpar(){
		Id_InterrupcaoTipo="";
		InterrupcaoTipo="";
		CodigoTemp="";
		InterrupcaoTotal = "";		
	}

	public String getPropriedades(){
		return "[Id_InterrupcaoTipo:" + Id_InterrupcaoTipo + ";InterrupcaoTipo:" + InterrupcaoTipo + ";InterrupcaoTotal:" + InterrupcaoTotal + ";CodigoTemp:" + CodigoTemp + "]";
	}
} 
