package br.gov.go.tj.projudi.dt;

public class PrazoSuspensoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 5839947830726249003L;
    private String Id_PrazoSuspensoTipo;
	private String PrazoSuspensoTipo;


	private String PrazoSuspensoTipoCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public PrazoSuspensoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_PrazoSuspensoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_PrazoSuspensoTipo = valor;}
	public String getPrazoSuspensoTipo()  {return PrazoSuspensoTipo;}
	public void setPrazoSuspensoTipo(String valor ) {if (valor!=null) PrazoSuspensoTipo = valor;}
	public String getPrazoSuspensoTipoCodigo()  {return PrazoSuspensoTipoCodigo;}
	public void setPrazoSuspensoTipoCodigo(String valor ) {if (valor!=null) PrazoSuspensoTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(PrazoSuspensoTipoDt objeto){
		Id_PrazoSuspensoTipo = objeto.getId();
		PrazoSuspensoTipo = objeto.getPrazoSuspensoTipo();
		PrazoSuspensoTipoCodigo = objeto.getPrazoSuspensoTipoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PrazoSuspensoTipo="";
		PrazoSuspensoTipo="";
		PrazoSuspensoTipoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PrazoSuspensoTipo:" + Id_PrazoSuspensoTipo + ";PrazoSuspensoTipo:" + PrazoSuspensoTipo + ";PrazoSuspensoTipoCodigo:" + PrazoSuspensoTipoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
