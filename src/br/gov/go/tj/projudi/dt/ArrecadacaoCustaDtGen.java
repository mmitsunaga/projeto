package br.gov.go.tj.projudi.dt;

public class ArrecadacaoCustaDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 471726984771000418L;
    private String Id_ArrecadacaoCusta;
	private String ArrecadacaoCusta;


	private String ArrecadacaoCustaCodigo;
	private String CodigoArrecadacao;
	private String Custa;
	private String CodigoTemp;

//---------------------------------------------------------
	public ArrecadacaoCustaDtGen() {

		limpar();

	}

	public String getId()  {return Id_ArrecadacaoCusta;}
	public void setId(String valor ) {if(valor!=null) Id_ArrecadacaoCusta = valor;}
	public String getArrecadacaoCusta()  {return ArrecadacaoCusta;}
	public void setArrecadacaoCusta(String valor ) {if (valor!=null) ArrecadacaoCusta = valor;}
	public String getArrecadacaoCustaCodigo()  {return ArrecadacaoCustaCodigo;}
	public void setArrecadacaoCustaCodigo(String valor ) {if (valor!=null) ArrecadacaoCustaCodigo = valor;}
	public String getCodigoArrecadacao()  {return CodigoArrecadacao;}
	public void setCodigoArrecadacao(String valor ) {if (valor!=null) CodigoArrecadacao = valor;}
	public String getCusta()  {return Custa;}
	public void setCusta(String valor ) {if (valor!=null) Custa = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ArrecadacaoCustaDt objeto){
		 if (objeto==null) return;
		Id_ArrecadacaoCusta = objeto.getId();
		ArrecadacaoCusta = objeto.getArrecadacaoCusta();
		ArrecadacaoCustaCodigo = objeto.getArrecadacaoCustaCodigo();
		CodigoArrecadacao = objeto.getCodigoArrecadacao();
		Custa = objeto.getCusta();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ArrecadacaoCusta="";
		ArrecadacaoCusta="";
		ArrecadacaoCustaCodigo="";
		CodigoArrecadacao="";
		Custa="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ArrecadacaoCusta:" + Id_ArrecadacaoCusta + ";ArrecadacaoCusta:" + ArrecadacaoCusta + ";ArrecadacaoCustaCodigo:" + ArrecadacaoCustaCodigo + ";CodigoArrecadacao:" + CodigoArrecadacao + ";Custa:" + Custa + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
