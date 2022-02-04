package br.gov.go.tj.projudi.dt;

public class ProfissaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 217718626827112952L;
    private String Id_Profissao;
	private String Profissao;

	private String ProfissaoCodigo;

	private String CodigoTemp;

//---------------------------------------------------------
	public ProfissaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Profissao;}
	public void setId(String valor ) {if(valor!=null) Id_Profissao = valor;}
	public String getProfissao()  {return Profissao;}
	public void setProfissao(String valor ) {if (valor!=null) Profissao = valor;}
	public String getProfissaoCodigo()  {return ProfissaoCodigo;}
	public void setProfissaoCodigo(String valor ) {if (valor!=null) ProfissaoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProfissaoDt objeto){
		Id_Profissao = objeto.getId();
		ProfissaoCodigo = objeto.getProfissaoCodigo();
		Profissao = objeto.getProfissao();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Profissao="";
		ProfissaoCodigo="";
		Profissao="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Profissao:" + Id_Profissao + ";ProfissaoCodigo:" + ProfissaoCodigo + ";Profissao:" + Profissao + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
