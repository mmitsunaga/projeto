package br.gov.go.tj.projudi.dt;

public class CustaValorDtGen extends Dados{

	private String Id_CustaValor;
    private static final long serialVersionUID = 8839103071551402964L;
	private String CustaValor;


	private String CustaValorCodigo;
	private String LimiteMin;
	private String LimiteMax;
	private String ValorCusta;
	private String Id_Custa;
	private String Custa;
	private String CodigoRegimento;
	private String CodigoTemp;

//---------------------------------------------------------
	public CustaValorDtGen() {

		limpar();

	}

	public String getId()  {return Id_CustaValor;}
	public void setId(String valor ) {if(valor!=null) Id_CustaValor = valor;}
	public String getCustaValor()  {return CustaValor;}
	public void setCustaValor(String valor ) {if (valor!=null) CustaValor = valor;}
	public String getCustaValorCodigo()  {return CustaValorCodigo;}
	public void setCustaValorCodigo(String valor ) {if (valor!=null) CustaValorCodigo = valor;}
	public String getLimiteMin()  {return LimiteMin;}
	public void setLimiteMin(String valor ) {if (valor!=null) LimiteMin = valor;}
	public String getLimiteMax()  {return LimiteMax;}
	public void setLimiteMax(String valor ) {if (valor!=null) LimiteMax = valor;}
	public String getValorCusta()  {return ValorCusta;}
	public void setValorCusta(String valor ) {if (valor!=null) ValorCusta = valor;}
	public String getId_Custa()  {return Id_Custa;}
	public void setId_Custa(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Custa = ""; Custa = "";}else if (!valor.equalsIgnoreCase("")) Id_Custa = valor;}
	public String getCusta()  {return Custa;}
	public void setCusta(String valor ) {if (valor!=null) Custa = valor;}
	public String getCodigoRegimento()  {return CodigoRegimento;}
	public void setCodigoRegimento(String valor ) {if (valor!=null) CodigoRegimento = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(CustaValorDt objeto){
		 if (objeto==null) return;
		Id_CustaValor = objeto.getId();
		CustaValor = objeto.getCustaValor();
		CustaValorCodigo = objeto.getCustaValorCodigo();
		LimiteMin = objeto.getLimiteMin();
		LimiteMax = objeto.getLimiteMax();
		ValorCusta = objeto.getValorCusta();
		Id_Custa = objeto.getId_Custa();
		Custa = objeto.getCusta();
		CodigoRegimento = objeto.getCodigoRegimento();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_CustaValor="";
		CustaValor="";
		CustaValorCodigo="";
		LimiteMin="";
		LimiteMax="";
		ValorCusta="";
		Id_Custa="";
		Custa="";
		CodigoRegimento="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_CustaValor:" + Id_CustaValor + ";CustaValor:" + CustaValor + ";CustaValorCodigo:" + CustaValorCodigo + ";LimiteMin:" + LimiteMin + ";LimiteMax:" + LimiteMax + ";ValorCusta:" + ValorCusta + ";Id_Custa:" + Id_Custa + ";Custa:" + Custa + ";CodigoRegimento:" + CodigoRegimento + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
