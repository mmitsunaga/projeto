package br.gov.go.tj.projudi.dt;

public class HistoricoLotacaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = 5712854423846449318L;
    private String Id_HistoricoLotacao;
	private String UsuarioServentia;
	
	

	private String Id_UsuarioServentia;

	private String DataInicio;
	private String DataFim;
	

//---------------------------------------------------------
	public HistoricoLotacaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_HistoricoLotacao;}
	public void setId(String valor ) {if(valor!=null) Id_HistoricoLotacao = valor;}
	public String getUsuarioServentia()  {return UsuarioServentia;}
	public void setUsuarioServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) UsuarioServentia = ""; else if (!valor.equalsIgnoreCase("")) UsuarioServentia = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia = ""; UsuarioServentia = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getDataInicio()  {return DataInicio;}
	public void setDataInicio(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) DataInicio = ""; else if (!valor.equalsIgnoreCase("")) DataInicio = valor;}
	public String getDataFim()  {return DataFim;}
	public void setDataFim(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) DataFim = ""; else if (!valor.equalsIgnoreCase("")) DataFim = valor;}
			
	
	


	public void copiar(HistoricoLotacaoDt objeto){
		Id_HistoricoLotacao = objeto.getId();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		UsuarioServentia = objeto.getUsuarioServentia();
		DataInicio = objeto.getDataInicio();
		DataFim = objeto.getDataFim();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_HistoricoLotacao="";
		Id_UsuarioServentia="";
		UsuarioServentia="";
		DataInicio="";
		DataFim="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_HistoricoLotacao:" + Id_HistoricoLotacao + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";UsuarioServentia:" + UsuarioServentia + ";DataInicio:" + DataInicio + ";DataFim:" + DataFim + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
