package br.gov.go.tj.projudi.dt;

public class ProcessoParteDebitoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -8525045493177110102L;
    private String Id_ProcessoParteDebito;
	private String ProcessoDebito;

	private String Id_ProcessoDebito;

	private String Id_ProcessoParte;
	private String Nome;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoParteDebitoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteDebito;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParteDebito = valor;}
	public String getProcessoDebito()  {return ProcessoDebito;}
	public void setProcessoDebito(String valor ) {if (valor!=null) ProcessoDebito = valor;}
	public String getId_ProcessoDebito()  {return Id_ProcessoDebito;}
	public void setId_ProcessoDebito(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoDebito = ""; ProcessoDebito = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoDebito = valor;}
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoParteDebitoDt objeto){
		Id_ProcessoParteDebito = objeto.getId();
		Id_ProcessoDebito = objeto.getId_ProcessoDebito();
		ProcessoDebito = objeto.getProcessoDebito();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		Nome = objeto.getNome();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_ProcessoParteDebito="";
		Id_ProcessoDebito="";
		ProcessoDebito="";
		Id_ProcessoParte="";
		Nome="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_ProcessoParteDebito:" + Id_ProcessoParteDebito + ";Id_ProcessoDebito:" + Id_ProcessoDebito + ";ProcessoDebito:" + ProcessoDebito + ";Id_ProcessoParte:" + Id_ProcessoParte + ";Nome:" + Nome + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
