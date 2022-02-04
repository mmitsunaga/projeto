package br.gov.go.tj.projudi.dt;


public class ProcessoParteSPGDt extends Dados {
	
	private static final long serialVersionUID = 6175637716442198593L;
	
	private String id;
	private String isnParte;
	private String nomePessoa;
	private String nomeTipoParte;
	private String codigoTipoParte;
	private String numeroISN;
	private String numeroProcesso;
	private String dataSentenca;
	private String codigoRegime;
	private String tipoPena;
	private String infoPena;
	private String infoMulta;
	private String infoCustas;
	private String codigoEspeciePena;
	private String codigoBeneficios;
	private String infoPrazoSursis;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public String getIsnParte() {
		return isnParte;
	}

	public void setIsnParte(String isnParte) {
		this.isnParte = isnParte;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public String getNomeTipoParte() {
		return nomeTipoParte;
	}

	public void setNomeTipoParte(String nomeTipoParte) {
		this.nomeTipoParte = nomeTipoParte;
	}

	public String getCodigoTipoParte() {
		return codigoTipoParte;
	}

	public void setCodigoTipoParte(String codigoTipoParte) {
		this.codigoTipoParte = codigoTipoParte;
	}

	public String getNumeroISN() {
		return numeroISN;
	}

	public void setNumeroISN(String numeroISN) {
		this.numeroISN = numeroISN;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getDataSentenca() {
		return dataSentenca;
	}

	public void setDataSentenca(String dataSentenca) {
		this.dataSentenca = dataSentenca;
	}

	public String getCodigoRegime() {
		return codigoRegime;
	}

	public void setCodigoRegime(String codigoRegime) {
		this.codigoRegime = codigoRegime;
	}

	public String getTipoPena() {
		return tipoPena;
	}

	public void setTipoPena(String tipoPena) {
		this.tipoPena = tipoPena;
	}

	public String getInfoPena() {
		return infoPena;
	}

	public void setInfoPena(String infoPena) {
		this.infoPena = infoPena;
	}

	public String getInfoMulta() {
		return infoMulta;
	}

	public void setInfoMulta(String infoMulta) {
		this.infoMulta = infoMulta;
	}

	public String getInfoCustas() {
		return infoCustas;
	}

	public void setInfoCustas(String infoCustas) {
		this.infoCustas = infoCustas;
	}

	public String getCodigoEspeciePena() {
		return codigoEspeciePena;
	}

	public void setCodigoEspeciePena(String codigoEspeciePena) {
		this.codigoEspeciePena = codigoEspeciePena;
	}

	public String getCodigoBeneficios() {
		return codigoBeneficios;
	}

	public void setCodigoBeneficios(String codigoBeneficios) {
		this.codigoBeneficios = codigoBeneficios;
	}

	public String getInfoPrazoSursis() {
		return infoPrazoSursis;
	}

	public void setInfoPrazoSursis(String infoPrazoSursis) {
		this.infoPrazoSursis = infoPrazoSursis;
	}
}
