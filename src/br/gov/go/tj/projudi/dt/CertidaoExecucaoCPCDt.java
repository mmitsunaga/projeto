package br.gov.go.tj.projudi.dt;

public class CertidaoExecucaoCPCDt extends Dados {

	
	/**
     * 
     */
    private static final long serialVersionUID = 4510257022061793246L;
    public static final String MODELO_CODIGO = "";
	public static final int CodigoPermissao = 447;
    private String numero;
	private String serventia;
	private String natureza;
	private String promovente;
	private String advogadoPromovente;
	private String promovido;
	private String cpfCnpj;
	private String dataDistribuicao;
	private String valor;
	private String texto;
	
	
	public CertidaoExecucaoCPCDt() {
		super();
		this.numero = "";
		this.serventia = "";
		this.natureza = "";
		this.promovente = "";
		this.advogadoPromovente = "";
		this.promovido = "";
		this.cpfCnpj = "";
		this.dataDistribuicao = "";
		this.valor = "";
		this.texto = "";
	}


	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		if (numero != null)
		this.numero = numero;
	}

	public String getServentia() {
		
		return serventia;
	}

	public void setServentia(String serventia) {
		if ( serventia != null)
		this.serventia = serventia;
	}

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		if ( natureza != null)
		this.natureza = natureza;
	}

	public String getPromovente() {
		return promovente;
	}

	public void setPromovente(String promovente) {
		if (  promovente != null)
		this.promovente = promovente;
	}

	public String getAdvogadoPromovente() {
		return advogadoPromovente;
	}

	public void setAdvogadoPromovente(String advogadoPromovente) {
		if ( advogadoPromovente != null)
		this.advogadoPromovente = advogadoPromovente;
	}

	public String getPromovido() {
		return promovido;
	}

	public void setPromovido(String promovido) {
		if ( promovido != null)
		this.promovido = promovido;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		if (cpfCnpj  != null)
		this.cpfCnpj = cpfCnpj;
	}

	public String getDataDistribuicao() {
		return dataDistribuicao;
	}

	public void setDataDistribuicao(String areaDistribuicao) {
		if ( areaDistribuicao != null)
		this.dataDistribuicao = areaDistribuicao;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		if ( valor != null)
		this.valor = valor;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		if ( texto != null)
		this.texto = texto;
	}
	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		

	}


	public String getModeloCodigo() {
		return MODELO_CODIGO;
	}


	public Integer getProcessoNumeroCompleto() {
		
		return null;
	}


}
