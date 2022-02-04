package br.gov.go.tj.projudi.dt;

public class CertificadoDtGen extends Dados{

	/**
     * 
     */
    private static final long serialVersionUID = -2455209401487406408L;
    private String Id_Certificado;
	private String UsuarioCertificado;

	private String Raiz;
	private String Emissor;
	private String Liberado;
	private String DataEmissao;
	private String DataExpiracao;
	private String DataRevogacao;
	private String MotivoRevogacao;
	private String Id_UsuarioCertificado;

	private String Id_UsuarioLiberador;
	private String UsuarioLiberador;
	private String Id_UsuarioRevogador;
	private String UsuarioRevogador;
	private String Certificado;
	private String CodigoTemp;

//---------------------------------------------------------
	public CertificadoDtGen() {

		limpar();

	}

	public String getId()  {return Id_Certificado;}
	public void setId(String valor ) {if(valor!=null) Id_Certificado = valor;}
	public String getUsuarioCertificado()  {return UsuarioCertificado;}
	public void setUsuarioCertificado(String valor ) {if (valor!=null) UsuarioCertificado = valor;}
	public String getRaiz()  {return Raiz;}
	public void setRaiz(String valor ) {if (valor!=null) Raiz = valor;}
	public String getEmissor()  {return Emissor;}
	public void setEmissor(String valor ) {if (valor!=null) Emissor = valor;}
	public String getLiberado()  {return Liberado;}
	public void setLiberado(String valor ) {if (valor!=null) Liberado = valor;}
	public String getDataEmissao()  {return DataEmissao;}
	public void setDataEmissao(String valor ) {if (valor!=null) DataEmissao = valor;}
	public String getDataExpiracao()  {return DataExpiracao;}
	public void setDataExpiracao(String valor ) {if (valor!=null) DataExpiracao = valor;}
	public String getDataRevogacao()  {return DataRevogacao;}
	public void setDataRevogacao(String valor ) {if (valor!=null) DataRevogacao = valor;}
	public String getMotivoRevogacao()  {return MotivoRevogacao;}
	public void setMotivoRevogacao(String valor ) {if (valor!=null) MotivoRevogacao = valor;}
	public String getId_UsuarioCertificado()  {return Id_UsuarioCertificado;}
	public void setId_UsuarioCertificado(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioCertificado = ""; UsuarioCertificado = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioCertificado = valor;}
	public String getId_UsuarioLiberador()  {return Id_UsuarioLiberador;}
	public void setId_UsuarioLiberador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioLiberador = ""; UsuarioLiberador = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioLiberador = valor;}
	public String getUsuarioLiberador()  {return UsuarioLiberador;}
	public void setUsuarioLiberador(String valor ) {if (valor!=null) UsuarioLiberador = valor;}
	public String getId_UsuarioRevogador()  {return Id_UsuarioRevogador;}
	public void setId_UsuarioRevogador(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_UsuarioRevogador = ""; UsuarioRevogador = "";}else if (!valor.equalsIgnoreCase("")) Id_UsuarioRevogador = valor;}
	public String getUsuarioRevogador()  {return UsuarioRevogador;}
	public void setUsuarioRevogador(String valor ) {if (valor!=null) UsuarioRevogador = valor;}
	public String getCertificado()  {return Certificado;}
	public void setCertificado(String valor ) {if (valor!=null) Certificado = valor;}
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(CertificadoDt objeto){
		Id_Certificado = objeto.getId();
		Raiz = objeto.getRaiz();
		Emissor = objeto.getEmissor();
		Liberado = objeto.getLiberado();
		DataEmissao = objeto.getDataEmissao();
		DataExpiracao = objeto.getDataExpiracao();
		DataRevogacao = objeto.getDataRevogacao();
		MotivoRevogacao = objeto.getMotivoRevogacao();
		Id_UsuarioCertificado = objeto.getId_UsuarioCertificado();
		UsuarioCertificado = objeto.getUsuarioCertificado();
		Id_UsuarioLiberador = objeto.getId_UsuarioLiberador();
		UsuarioLiberador = objeto.getUsuarioLiberador();
		Id_UsuarioRevogador = objeto.getId_UsuarioRevogador();
		UsuarioRevogador = objeto.getUsuarioRevogador();
		Certificado = objeto.getCertificado();
		CodigoTemp = objeto.getCodigoTemp();
	}

	public void limpar(){
		Id_Certificado="";
		Raiz="";
		Emissor="";
		Liberado="";
		DataEmissao="";
		DataExpiracao="";
		DataRevogacao="";
		MotivoRevogacao="";
		Id_UsuarioCertificado="";
		UsuarioCertificado="";
		Id_UsuarioLiberador="";
		UsuarioLiberador="";
		Id_UsuarioRevogador="";
		UsuarioRevogador="";
		Certificado="";
		CodigoTemp="";
	}


	public String getPropriedades(){
		return "[Id_Certificado:" + Id_Certificado + ";Raiz:" + Raiz + ";Emissor:" + Emissor + ";Liberado:" + Liberado + ";DataEmissao:" + DataEmissao + ";DataExpiracao:" + DataExpiracao + ";DataRevogacao:" + DataRevogacao + ";MotivoRevogacao:" + MotivoRevogacao + ";Id_UsuarioCertificado:" + Id_UsuarioCertificado + ";UsuarioCertificado:" + UsuarioCertificado + ";Id_UsuarioLiberador:" + Id_UsuarioLiberador + ";UsuarioLiberador:" + UsuarioLiberador + ";Id_UsuarioRevogador:" + Id_UsuarioRevogador + ";UsuarioRevogador:" + UsuarioRevogador + ";CodigoTemp:" + CodigoTemp + "]";
	}


} 
