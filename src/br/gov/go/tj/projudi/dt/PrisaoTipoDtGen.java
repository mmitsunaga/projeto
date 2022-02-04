package br.gov.go.tj.projudi.dt;

public class PrisaoTipoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8561496367936350174L;
	private String Id_PrisaoTipo;
	private String PrisaoTipo;

	private String PrisaoTipoCodigo;

	private String CodigoTepmp;

//---------------------------------------------------------
	public PrisaoTipoDtGen() {

		limpar();

	}

	public String getId()  {return Id_PrisaoTipo;}
	public void setId(String valor ) {if(valor!=null) Id_PrisaoTipo = valor;}
	public String getPrisaoTipo()  {return PrisaoTipo;}
	public void setPrisaoTipo(String valor ) {if (valor!=null) PrisaoTipo = valor;}
	public String getPrisaoTipoCodigo()  {return PrisaoTipoCodigo;}
	public void setPrisaoTipoCodigo(String valor ) {if (valor!=null) PrisaoTipoCodigo = valor;}
	public String getCodigoTepmp()  {return CodigoTepmp;}
	public void setCodigoTepmp(String valor ) {if (valor!=null) CodigoTepmp = valor;}


	public void copiar(PrisaoTipoDt objeto){
		 if (objeto==null) return;
		Id_PrisaoTipo = objeto.getId();
		PrisaoTipoCodigo = objeto.getPrisaoTipoCodigo();
		PrisaoTipo = objeto.getPrisaoTipo();
		CodigoTepmp = objeto.getCodigoTepmp();
	}

	public void limpar(){
		Id_PrisaoTipo="";
		PrisaoTipoCodigo="";
		PrisaoTipo="";
		CodigoTepmp="";
	}


	public String getPropriedades(){
		return "[Id_PrisaoTipo:" + Id_PrisaoTipo + ";PrisaoTipoCodigo:" + PrisaoTipoCodigo + ";PrisaoTipo:" + PrisaoTipo + ";CodigoTepmp:" + CodigoTepmp + "]";
	}


} 
