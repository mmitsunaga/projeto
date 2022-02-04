package br.gov.go.tj.projudi.dt;

public class PonteiroLogTipoDtGen extends Dados{

	protected String Id_PonteiroLogTipo;
	protected String PonteiroLogTipo;


	protected String CodigoTemp;

//---------------------------------------------------------
	public PonteiroLogTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_PonteiroLogTipo;}
	public void setId(String valor ) { if(valor!=null) Id_PonteiroLogTipo = valor;}
	public String getPonteiroLogTipo()  {return PonteiroLogTipo;}
	public void setPonteiroLogTipo(String valor ) { if (valor!=null) PonteiroLogTipo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(PonteiroLogTipoDt objeto){
		 if (objeto==null) return;
		Id_PonteiroLogTipo = objeto.getId();
		PonteiroLogTipo = objeto.getPonteiroLogTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_PonteiroLogTipo="";
		PonteiroLogTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_PonteiroLogTipo:" + Id_PonteiroLogTipo + ";PonteiroLogTipo:" + PonteiroLogTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
