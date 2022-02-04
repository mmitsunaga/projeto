package br.gov.go.tj.projudi.dt;

public class CertidaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6085348753603714013L;
	private String Id_Certidao;
	private byte[] Certidao;
	private String DataValidade;
	private String CodigoTemp;

//---------------------------------------------------------
	public CertidaoDtGen() {

		limpar();

	}

	public CertidaoDtGen(byte[] certidao, String dataValidade) {
		super();
		Certidao = certidao;
		DataValidade = dataValidade;
	}

	public String getId()  {return Id_Certidao;}
	public void setId(String valor ) {if(valor!=null) Id_Certidao = valor;}
	public byte[] getCertidao()  {return Certidao;}
	public void setCertidao(byte[] valor ) {if (valor!=null) Certidao = valor;}
	public String getDataValidade()  {return DataValidade;}
	public void setDataValidade(String valor ) {if (valor!=null) DataValidade = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(CertidaoValidacaoDt objeto){
		 if (objeto==null) return;
		Id_Certidao = objeto.getId();
		Certidao = objeto.getCertidao();
		DataValidade = objeto.getDataValidade();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Certidao="";
		Certidao=null;
		DataValidade="";
		CodigoTemp="";
	}
	
	

	public String getPropriedades(){
		return "[Id_Certidao:" + Id_Certidao + ";Certidao:ver arquivo;DataValidade:" + DataValidade + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
