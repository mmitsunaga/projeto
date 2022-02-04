package br.gov.go.tj.projudi.dt;


public class CertidaoValidacaoDt extends CertidaoDtGen {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8952721873216430315L;
	
	public static String CERTIDAO_PUBLICA_NADA_CONSTA = "1";
		
	private String dataEmissao;
	private String numeroGuia;
	private String id_Modelo;
	private String modelo;
	private byte[] Documento;

	public CertidaoValidacaoDt(byte[] certidao, String dataValidade, String dataEmissao) {
		super(certidao, dataValidade);
			this.dataEmissao = dataEmissao;
	}
	
	public CertidaoValidacaoDt() {
		
	}
	public String getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(String dataFormatadaddMMyyyyHHmmss) {
		this.dataEmissao = dataFormatadaddMMyyyyHHmmss;
	}
	
	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		if( numeroGuia != null)
			this.numeroGuia = numeroGuia;
	}
	
	public String getId_Modelo() {
		return id_Modelo;
	}

	public void setId_Modelo(String id_Modelo) {
		if( id_Modelo != null)
			this.id_Modelo = id_Modelo;
	}
	
	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		if( modelo != null)
			this.modelo = modelo;
	}
	
	private String Id_Comarca;
	
	public String getId_Comarca() {
		return Id_Comarca;
	}

	public void setId_Comarca(String id_Comarca) {
		if (id_Comarca != null)
			Id_Comarca = id_Comarca;
	}
	
	private String Comarca;

	public String getComarca() {
		return Comarca;
	}

	public void setComarca(String comarca) {
		if (comarca != null)
			Comarca = comarca;
	}
	
	private String ComarcaCodigo;
	
	public String getComarcaCodigo() {
		return ComarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		if (comarcaCodigo != null)
			ComarcaCodigo = comarcaCodigo;
	}
	
	private String Finalidade = "";
	
	public String getFinalidade() {
		return Finalidade;
	}

	public void setFinalidade(String finalidade) {
		if (finalidade != null)
			this.Finalidade = finalidade;
	}
	
	public byte[] getDocumento()  {return Documento;}
	public void setDocumento(byte[] valor ) {if (valor!=null) Documento = valor;}
}
