package br.gov.go.tj.projudi.dt;

public class EscalaTipoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5143185220339599972L;
	protected String Id_EscalaTipo;
	protected String EscalaTipo;

	protected String EscalaTipoCodigo;

	protected String CodigoTemp;

//---------------------------------------------------------
	public EscalaTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_EscalaTipo;}
	public void setId(String valor ) { if(valor!=null) Id_EscalaTipo = valor;}
	public String getEscalaTipo()  {return EscalaTipo;}
	public void setEscalaTipo(String valor ) { if (valor!=null) EscalaTipo = valor;}
	public String getEscalaTipoCodigo()  {return EscalaTipoCodigo;}
	public void setEscalaTipoCodigo(String valor ) { if (valor!=null) EscalaTipoCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) { if (valor!=null) CodigoTemp = valor;}


	public void copiar(EscalaTipoDt objeto){
		 if (objeto==null) return;
		Id_EscalaTipo = objeto.getId();
		EscalaTipoCodigo = objeto.getEscalaTipoCodigo();
		EscalaTipo = objeto.getEscalaTipo();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_EscalaTipo="";
		EscalaTipoCodigo="";
		EscalaTipo="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_EscalaTipo:" + Id_EscalaTipo + ";EscalaTipoCodigo:" + EscalaTipoCodigo + ";EscalaTipo:" + EscalaTipo + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
