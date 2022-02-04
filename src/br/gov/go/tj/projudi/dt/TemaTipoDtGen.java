package br.gov.go.tj.projudi.dt;

public class TemaTipoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6768474949942922819L;
	protected String Id_TemaTipo;
	protected String TemaTipo;
	protected String TemaTipoCodigo;
	protected String TemaTipoCnj;
	protected String CodigoTemp;

//---------------------------------------------------------
	public TemaTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_TemaTipo;}
	public void setId(String valor ) {if(valor!=null) Id_TemaTipo = valor;}
	public String getTemaTipo()  {return TemaTipo;}
	public void setTemaTipo(String valor ) {if (valor!=null) TemaTipo = valor;}
	public String getTemaTipoCodigo()  {return TemaTipoCodigo;}
	public void setTemaTipoCodigo(String valor ) {if (valor!=null) TemaTipoCodigo = valor;}
	public String getTemaTipoCnj() { return TemaTipoCnj;}
	public void setTemaTipoCnj(String valor) { if (valor!=null) TemaTipoCnj = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}

	public void copiar(TemaTipoDt objeto){
		if (objeto==null) return;
		Id_TemaTipo = objeto.getId();
		TemaTipoCodigo = objeto.getTemaTipoCodigo();
		TemaTipo = objeto.getTemaTipo();
		TemaTipoCnj = objeto.getTemaTipoCnj();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_TemaTipo="";
		TemaTipoCodigo="";
		TemaTipo="";
		TemaTipoCnj="";
		CodigoTemp="";
	}

	public String getPropriedades(){
		return "[Id_TemaTipo:" + Id_TemaTipo + ";TemaTipoCodigo:" + TemaTipoCodigo + ";TemaTipo:" + TemaTipo + ";TemaTipoCNJ:" + TemaTipoCnj + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
