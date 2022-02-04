package br.gov.go.tj.projudi.dt;

public class PenaExecucaoTipoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -5774060051774165220L;
    private String Id_PenaExecucaoTipo;
	private String PenaExecucaoTipo;


	private String CodigoTemp;

//---------------------------------------------------------
	public PenaExecucaoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_PenaExecucaoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_PenaExecucaoTipo = valor;}
	public String getPenaExecucaoTipo()  {return PenaExecucaoTipo;}
	public void setPenaExecucaoTipo(String valor ) {if (valor!=null) PenaExecucaoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(PenaExecucaoTipoDt objeto){
		Id_PenaExecucaoTipo = objeto.getId();
		PenaExecucaoTipo = objeto.getPenaExecucaoTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PenaExecucaoTipo="";
		PenaExecucaoTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PenaExecucaoTipo:" + Id_PenaExecucaoTipo + ";PenaExecucaoTipo:" + PenaExecucaoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
