package br.gov.go.tj.projudi.dt;

public class RegimeExecucaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -5505632305942601590L;
    private String Id_RegimeExecucao;
	private String RegimeExecucao;
	private String Id_ProximoRegimeExecucao;

	private String Id_PenaExecucaoTipo;
	private String PenaExecucaoTipo;
	private String CodigoTemp;
	private String proximoRegimeExecucao;

//---------------------------------------------------------
	public RegimeExecucaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_RegimeExecucao;}
	public void setId(String valor ) {if(valor!=null) Id_RegimeExecucao = valor;}
	public String getRegimeExecucao()  {return RegimeExecucao;}
	public void setRegimeExecucao(String valor ) {if (valor!=null) RegimeExecucao = valor;}
	public String getId_PenaExecucaoTipo()  {return Id_PenaExecucaoTipo;}
	public void setId_PenaExecucaoTipo(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PenaExecucaoTipo = ""; PenaExecucaoTipo = "";}else if (!valor.equalsIgnoreCase("")) Id_PenaExecucaoTipo = valor;}
	public String getPenaExecucaoTipo()  {return PenaExecucaoTipo;}
	public void setPenaExecucaoTipo(String valor ) {if (valor!=null) PenaExecucaoTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getId_ProximoRegimeExecucao()  {return Id_ProximoRegimeExecucao;}
	public void setId_ProximoRegimeExecucao(String valor ) {if(valor!=null) Id_ProximoRegimeExecucao = valor;}
	public String getProximoRegimeExecucao()  {return proximoRegimeExecucao;}
	public void setProximoRegimeExecucao(String valor ) {
		if (valor!=null){
			if (valor.equalsIgnoreCase("null")) {
				proximoRegimeExecucao = "";
			} else if (!valor.equalsIgnoreCase("")) 
				proximoRegimeExecucao = valor;
		}
	}

	public void copiar(RegimeExecucaoDt objeto){
		Id_RegimeExecucao = objeto.getId();
		RegimeExecucao = objeto.getRegimeExecucao();
		Id_PenaExecucaoTipo = objeto.getId_PenaExecucaoTipo();
		PenaExecucaoTipo = objeto.getPenaExecucaoTipo();
		CodigoTemp = objeto.getCodigoTemp();
		Id_ProximoRegimeExecucao = objeto.getId_ProximoRegimeExecucao();
	}

	public void limpar(){
		Id_RegimeExecucao="";
		RegimeExecucao="";
		Id_PenaExecucaoTipo="";
		PenaExecucaoTipo="";
		CodigoTemp="";
		Id_ProximoRegimeExecucao="";
		proximoRegimeExecucao = "";
	}


	public String getPropriedades(){
		return "[Id_RegimeExecucao:" + Id_RegimeExecucao + ";RegimeExecucao:" + RegimeExecucao + ";Id_PenaExecucaoTipo:" + Id_PenaExecucaoTipo + ";PenaExecucaoTipo:" + PenaExecucaoTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
