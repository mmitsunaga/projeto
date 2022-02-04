package br.gov.go.tj.projudi.dt;

public class OficialCertidaoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5240369983713545960L;
	private String Id_OficialCertidao;
	private String CertidaoNome;
	private String Id_UsuarioServentia;
	private String Nome;
	private String Status;
	private String NumeroMandado;
	private String DataEmissao;
	private String Texto;
	private String CodigoTemp;
	private String Grupo;

//---------------------------------------------------------
	public OficialCertidaoDtGen() {

		limpar();

	}

	public String getId()  {return Id_OficialCertidao;}
	public void setId(String valor ) {if(valor!=null) Id_OficialCertidao = valor;}
	public String getCertidaoNome()  {return CertidaoNome;}
	public void setCertidaoNome(String valor ) {if (valor!=null) CertidaoNome = valor;}
	public String getId_UsuarioServentia()  {return Id_UsuarioServentia;}
	public void setId_UsuarioServentia(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioServentia = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioServentia = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	public String getStatus()  {return Status;}
	public void setStatus(String valor ) {if (valor!=null) Status = valor;}
	public String getNumeroMandado()  {return NumeroMandado;}
	public void setNumeroMandado(String valor ) {if (valor!=null) NumeroMandado = valor;}
	public String getDataEmissao()  {return DataEmissao;}
	public void setDataEmissao(String valor ) {if (valor!=null) DataEmissao = valor;}
	public String getTexto()  {return Texto;}
	public void setTexto(String valor ) {if (valor!=null) Texto = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}
	public String getGrupo()  {return Grupo;}
	public void setGrupo(String valor ) {if (valor!=null) Grupo = valor;}


	public void copiar(OficialCertidaoDt objeto){
		 if (objeto==null) return;
		Id_OficialCertidao = objeto.getId();
		CertidaoNome = objeto.getCertidaoNome();
		Id_UsuarioServentia = objeto.getId_UsuarioServentia();
		Nome = objeto.getNome();
		Status = objeto.getStatus();
		NumeroMandado = objeto.getNumeroMandado();
		DataEmissao = objeto.getDataEmissao();
		Texto = objeto.getTexto();
		CodigoTemp = objeto.getCodigoTemp();
		Grupo = objeto.getGrupo();
	}

	public void limpar(){
		Id_OficialCertidao="";
		CertidaoNome="";
		Id_UsuarioServentia="";
		Nome="";
		Status="";
		NumeroMandado="";
		DataEmissao="";
		Texto="";
		CodigoTemp="";
		Grupo="";
	}


	public String getPropriedades(){
		return "[Id_OficialCertidao:" + Id_OficialCertidao + ";CertidaoNome:" + CertidaoNome + ";Id_UsuarioServentia:" + Id_UsuarioServentia + ";Nome:" + Nome + ";Status:" + Status + ";NumeroMandado:" + NumeroMandado + ";DataEmissao:" + DataEmissao + ";Texto:" + Texto + ";CodigoTemp:" + CodigoTemp + ";Grupo:" + Grupo + "]";
	}


} 
