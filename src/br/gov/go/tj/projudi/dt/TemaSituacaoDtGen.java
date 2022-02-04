package br.gov.go.tj.projudi.dt;

public class TemaSituacaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3324025667627384881L;
	protected String Id_TemaSituacao;
	protected String TemaSituacao;
	protected String TemaSituacaoCnj;
	protected String TemaSituacaoCodigo;
	protected String CodigoTemp;

//---------------------------------------------------------
	public TemaSituacaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_TemaSituacao;}
	public void setId(String valor ) {if(valor!=null) Id_TemaSituacao = valor;}
	public String getTemaSituacao()  {return TemaSituacao;}
	public void setTemaSituacao(String valor ) {if (valor!=null) TemaSituacao = valor;}
	public String getTemaSituacaoCnj() {return TemaSituacaoCnj;}
	public void setTemaSituacaoCnj(String valor) {if (valor!=null) TemaSituacaoCnj = valor;}
	public String getTemaSituacaoCodigo()  {return TemaSituacaoCodigo;}
	public void setTemaSituacaoCodigo(String valor ) {if (valor!=null) TemaSituacaoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}

	public void copiar(TemaSituacaoDt objeto){
		if (objeto==null) return;
		Id_TemaSituacao = objeto.getId();
		TemaSituacaoCodigo = objeto.getTemaSituacaoCodigo();
		TemaSituacao = objeto.getTemaSituacao();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_TemaSituacao="";
		TemaSituacaoCodigo="";
		TemaSituacao="";
		TemaSituacaoCnj="";
		CodigoTemp="";
	}

	public String getPropriedades(){
		return "[Id_TemaSituacao:" + Id_TemaSituacao + ";TemaSituacaoCodigo:" + TemaSituacaoCodigo + ";TemaSituacao:" + TemaSituacao + ";TemaSituacaoCnj:" + TemaSituacaoCnj + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
