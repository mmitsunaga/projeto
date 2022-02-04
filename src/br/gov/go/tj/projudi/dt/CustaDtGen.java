package br.gov.go.tj.projudi.dt;

public class CustaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 5922376295690462503L;
    private String Id_Custa;
	private String Custa;


	private String CustaCodigo;
	private String CodigoRegimento;
	private String CodigoRegimentoValor;
	private String Porcentagem;
	private String Minimo;
	private String Id_ArrecadacaoCusta;
	private String ArrecadacaoCusta;
	private String CodigoTemp;

//---------------------------------------------------------
	public CustaDtGen() {

		limpar();

	}

	public String getId()  {return Id_Custa;}
	public void setId(String valor ) {if(valor!=null) Id_Custa = valor;}
	public String getCusta()  {return Custa;}
	public void setCusta(String valor ) {if (valor!=null) Custa = valor;}
	public String getCustaCodigo()  {return CustaCodigo;}
	public void setCustaCodigo(String valor ) {if (valor!=null) CustaCodigo = valor;}
	public String getCodigoRegimento()  {return CodigoRegimento;}
	public void setCodigoRegimento(String valor ) {if (valor!=null) CodigoRegimento = valor;}
	public String getCodigoRegimentoValor()  {return CodigoRegimentoValor;}
	public void setCodigoRegimentoValor(String valor ) {if (valor!=null) CodigoRegimentoValor = valor;}
	public String getPorcentagem()  {return Porcentagem;}
	public void setPorcentagem(String valor ) {if (valor!=null) Porcentagem = valor;}
	public String getMinimo()  {return Minimo;}
	public void setMinimo(String valor ) {if (valor!=null) Minimo = valor;}
	public String getId_ArrecadacaoCusta()  {return Id_ArrecadacaoCusta;}
	public void setId_ArrecadacaoCusta(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ArrecadacaoCusta = ""; ArrecadacaoCusta = "";}else if (!valor.equalsIgnoreCase("")) Id_ArrecadacaoCusta = valor;}
	public String getArrecadacaoCusta()  {return ArrecadacaoCusta;}
	public void setArrecadacaoCusta(String valor ) {if (valor!=null) ArrecadacaoCusta = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(CustaDt objeto){
		 if (objeto==null) return;
		Id_Custa = objeto.getId();
		Custa = objeto.getCusta();
		CustaCodigo = objeto.getCustaCodigo();
		CodigoRegimento = objeto.getCodigoRegimento();
		CodigoRegimentoValor = objeto.getCodigoRegimentoValor();
		Porcentagem = objeto.getPorcentagem();
		Minimo = objeto.getMinimo();
		Id_ArrecadacaoCusta = objeto.getId_ArrecadacaoCusta();
		ArrecadacaoCusta = objeto.getArrecadacaoCusta();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Custa="";
		Custa="";
		CustaCodigo="";
		CodigoRegimento="";
		CodigoRegimentoValor="";
		Porcentagem="";
		Minimo="";
		Id_ArrecadacaoCusta="";
		ArrecadacaoCusta="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Custa:" + Id_Custa + ";Custa:" + Custa + ";CustaCodigo:" + CustaCodigo + ";CodigoRegimento:" + CodigoRegimento + ";CodigoRegimentoValor:" + CodigoRegimentoValor + ";Porcentagem:" + Porcentagem + ";Minimo:" + Minimo + ";Id_ArrecadacaoCusta:" + Id_ArrecadacaoCusta + ";ArrecadacaoCusta:" + ArrecadacaoCusta + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
