package br.gov.go.tj.projudi.dt;

public class TemaOrigemDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3452556642039446847L;
	protected String Id_TemaOrigem;
	protected String TemaOrigem;

	protected String TemaOrigemCodigo;

	protected String CodigoTemp;

//---------------------------------------------------------
	public TemaOrigemDtGen() {

		limpar();

	}

	public String getId()  {return Id_TemaOrigem;}
	public void setId(String valor ) {if(valor!=null) Id_TemaOrigem = valor;}
	public String getTemaOrigem()  {return TemaOrigem;}
	public void setTemaOrigem(String valor ) {if (valor!=null) TemaOrigem = valor;}
	public String getTemaOrigemCodigo()  {return TemaOrigemCodigo;}
	public void setTemaOrigemCodigo(String valor ) {if (valor!=null) TemaOrigemCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(TemaOrigemDt objeto){
		 if (objeto==null) return;
		Id_TemaOrigem = objeto.getId();
		TemaOrigemCodigo = objeto.getTemaOrigemCodigo();
		TemaOrigem = objeto.getTemaOrigem();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_TemaOrigem="";
		TemaOrigemCodigo="";
		TemaOrigem="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_TemaOrigem:" + Id_TemaOrigem + ";TemaOrigemCodigo:" + TemaOrigemCodigo + ";TemaOrigem:" + TemaOrigem + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
