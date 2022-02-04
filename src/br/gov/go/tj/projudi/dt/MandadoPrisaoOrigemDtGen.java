package br.gov.go.tj.projudi.dt;

public class MandadoPrisaoOrigemDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8809940098784669878L;
	private String Id_MandadoPrisaoOrigem;
	private String MandadoPrisaoOrigem;

	private String MandadoPrisaoOrigemCodigo;

	private String CodigoTemp;

//---------------------------------------------------------
	public MandadoPrisaoOrigemDtGen() {

		limpar();

	}

	public String getId()  {return Id_MandadoPrisaoOrigem;}
	public void setId(String valor ) {if(valor!=null) Id_MandadoPrisaoOrigem = valor;}
	public String getMandadoPrisaoOrigem()  {return MandadoPrisaoOrigem;}
	public void setMandadoPrisaoOrigem(String valor ) {if (valor!=null) MandadoPrisaoOrigem = valor;}
	public String getMandadoPrisaoOrigemCodigo()  {return MandadoPrisaoOrigemCodigo;}
	public void setMandadoPrisaoOrigemCodigo(String valor ) {if (valor!=null) MandadoPrisaoOrigemCodigo = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(MandadoPrisaoOrigemDt objeto){
		 if (objeto==null) return;
		Id_MandadoPrisaoOrigem = objeto.getId();
		MandadoPrisaoOrigemCodigo = objeto.getMandadoPrisaoOrigemCodigo();
		MandadoPrisaoOrigem = objeto.getMandadoPrisaoOrigem();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_MandadoPrisaoOrigem="";
		MandadoPrisaoOrigemCodigo="";
		MandadoPrisaoOrigem="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_MandadoPrisaoOrigem:" + Id_MandadoPrisaoOrigem + ";MandadoPrisaoOrigemCodigo:" + MandadoPrisaoOrigemCodigo + ";MandadoPrisaoOrigem:" + MandadoPrisaoOrigem + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
