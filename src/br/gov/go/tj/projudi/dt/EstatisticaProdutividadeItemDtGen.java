package br.gov.go.tj.projudi.dt;

public class EstatisticaProdutividadeItemDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 8889784889408254975L;
    private String Id_EstatisticaProdutividadeItem;
	private String EstatisticaProdutividadeItem;


	private String DadoCodigo;
	private String CodigoTemp;

//---------------------------------------------------------
	public EstatisticaProdutividadeItemDtGen() {

		limpar();

	}

	public String getId()  {return Id_EstatisticaProdutividadeItem;}
	public void setId(String valor ) {if(valor!=null) Id_EstatisticaProdutividadeItem = valor;}
	public String getEstatisticaProdutividadeItem()  {return EstatisticaProdutividadeItem;}
	public void setEstatisticaProdutividadeItem(String valor ) {if (valor!=null) EstatisticaProdutividadeItem = valor;}
	public String getDadoCodigo()  {return DadoCodigo;}
	public void setDadoCodigo(String valor ) {if (valor!=null) DadoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(EstatisticaProdutividadeItemDt objeto){
		 if (objeto==null) return;
		Id_EstatisticaProdutividadeItem = objeto.getId();
		EstatisticaProdutividadeItem = objeto.getEstatisticaProdutividadeItem();
		DadoCodigo = objeto.getDadoCodigo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_EstatisticaProdutividadeItem="";
		EstatisticaProdutividadeItem="";
		DadoCodigo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_EstatisticaProdutividadeItem:" + Id_EstatisticaProdutividadeItem + ";EstatisticaProdutividadeItem:" + EstatisticaProdutividadeItem + ";DadoCodigo:" + DadoCodigo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
