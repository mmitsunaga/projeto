package br.gov.go.tj.projudi.dt;

import java.text.NumberFormat;

import br.gov.go.tj.utils.Funcoes;

public class ServentiaAreaDistribuicaoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -3240746177910522045L;
    private String Id_ServAreaDist;
	private String ServAreaDist;


	private String Id_Serventia;
	private String Serventia;
	private String Id_AreaDistribuicao;
	private String AreaDist;
	private String CodigoTemp;
	private String Probabilidade;

//---------------------------------------------------------
	public ServentiaAreaDistribuicaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ServAreaDist;}
	public void setId(String valor ) {if(valor!=null) Id_ServAreaDist = valor;}
	public String getServAreaDist()  {return ServAreaDist;}
	public void setServAreaDist(String valor ) {if (valor!=null) ServAreaDist = valor;}
	public String getId_Serv()  {return Id_Serventia;}
	public void setId_Serv(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_Serventia = ""; Serventia = "";}else if (!valor.equalsIgnoreCase("")) Id_Serventia = valor;}
	public String getServ()  {return Serventia;}
	public void setServ(String valor ) {if (valor!=null) Serventia = valor;}
	public String getId_AreaDist()  {return Id_AreaDistribuicao;}
	public void setId_AreaDist(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_AreaDistribuicao = ""; AreaDist = "";}else if (!valor.equalsIgnoreCase("")) Id_AreaDistribuicao = valor;}
	public String getAreaDist()  {return AreaDist;}
	public void setAreaDist(String valor ) {if (valor!=null) AreaDist = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getProbabilidade()  {return Probabilidade;}
	public void setProbabilidade(String valor ) {if (valor!=null) Probabilidade = valor;}
	
	public String getQuantidadeDistribuicaoDescricao() {
		Probabilidade = Probabilidade.replace(",", ".");
		float valor = Funcoes.StringToFloat(Probabilidade);
		NumberFormat df = NumberFormat.getPercentInstance();
		df.setMaximumFractionDigits(2);
		return df.format(valor);		
	}


	public void copiar(ServentiaAreaDistribuicaoDt objeto){
		 if (objeto==null) return;
		Id_ServAreaDist = objeto.getId();
		ServAreaDist = objeto.getServAreaDist();
		Id_Serventia = objeto.getId_Serv();
		Serventia = objeto.getServ();
		Id_AreaDistribuicao = objeto.getId_AreaDist();
		AreaDist = objeto.getAreaDist();
		CodigoTemp = objeto.getCodigoTemp();
		Probabilidade = objeto.getProbabilidade();
	}

	public void limpar(){
		Id_ServAreaDist="";
		ServAreaDist="";
		Id_Serventia="";
		Serventia="";
		Id_AreaDistribuicao="";
		AreaDist="";
		CodigoTemp="";
		Probabilidade="";
	}


	public String getPropriedades(){
		return "[Id_ServAreaDist:" + Id_ServAreaDist + ";ServAreaDist:" + ServAreaDist + ";Id_Serv:" + Id_Serventia + ";Serv:" + Serventia + ";Id_AreaDist:" + Id_AreaDistribuicao + ";AreaDist:" + AreaDist + ";CodigoTemp:" + CodigoTemp +  ";Probabilidade:" + Probabilidade + "]";
	}


} 
