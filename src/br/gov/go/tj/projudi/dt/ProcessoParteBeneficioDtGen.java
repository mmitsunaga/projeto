package br.gov.go.tj.projudi.dt;

public class ProcessoParteBeneficioDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -7957849532487049233L;
    private String Id_ProcessoParteBeneficio;
	private String ProcessoBeneficio;

	private String Id_ProcessoBeneficio;

	private String Id_ProcessoParte;
	private String Nome;
	private String DataInicial;
	private String DataFinal;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoParteBeneficioDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteBeneficio;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParteBeneficio = valor;}
	public String getProcessoBeneficio()  {return ProcessoBeneficio;}
	public void setProcessoBeneficio(String valor ) {if (valor!=null) ProcessoBeneficio = valor;}
	public String getId_ProcessoBeneficio()  {return Id_ProcessoBeneficio;}
	public void setId_ProcessoBeneficio(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoBeneficio = ""; ProcessoBeneficio = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoBeneficio = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getDataInicial()  {return DataInicial;}
	public void setDataInicial(String valor ) {if (valor!=null) DataInicial = valor;}
	public String getDataFinal()  {return DataFinal;}
	public void setDataFinal(String valor ) {if (valor!=null) DataFinal = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoParteBeneficioDt objeto){
		Id_ProcessoParteBeneficio = objeto.getId();
		Id_ProcessoBeneficio = objeto.getId_ProcessoBeneficio();
		ProcessoBeneficio = objeto.getProcessoBeneficio();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		Nome = objeto.getNome();
		DataInicial = objeto.getDataInicial();
		DataFinal = objeto.getDataFinal();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoParteBeneficio="";
		Id_ProcessoBeneficio="";
		ProcessoBeneficio="";
		Id_ProcessoParte="";
		Nome="";
		DataInicial="";
		DataFinal="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoParteBeneficio:" + Id_ProcessoParteBeneficio + ";Id_ProcessoBeneficio:" + Id_ProcessoBeneficio + ";ProcessoBeneficio:" + ProcessoBeneficio + ";Id_ProcessoParte:" + Id_ProcessoParte + ";Nome:" + Nome + ";DataInicial:" + DataInicial + ";DataFinal:" + DataFinal + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
