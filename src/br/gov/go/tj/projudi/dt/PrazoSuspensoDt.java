package br.gov.go.tj.projudi.dt;

public class PrazoSuspensoDt extends PrazoSuspensoDtGen {

	private static final long serialVersionUID = -6670468456067580804L;

    public static final int CodigoPermissao = 218;

    // Keila
    /*
     * Constante herdada do código do Leandro Lira para o funcionamento de
     * classes relacionadas à processo
     */
    public static final int AUSENCIA_DE_PRAZO = 0;

	public static final int ID_FERIADO_MUNICIPAL = 1;
	public static final int ID_FERIADO_ESTADUAL = 2;
	public static final int ID_FERIADO_FEDERAL = 3;
	public static final int ID_RECESSO = 4;
	public static final int ID_DECRETO = 5;
	public static final int ID_RECESSO_CPC = 6;
	
    private String DataInicial;
    private String DataFinal;
    private String DataLancamento;
    private String plantaoLiberado;
    
    
    public PrazoSuspensoDt() {
		super();
		limpar();
	}


	public void limpar(){
    	super.limpar();
    	DataInicial = "";
    	DataFinal = "";
    	DataLancamento = "";
    	plantaoLiberado = "1";
	}

	
    public String getDataInicial() {
		return DataInicial;
	}
    
	public void setDataInicial(String dataInicial) {
		if (dataInicial != null)
			DataInicial = dataInicial;
	}
	
	public String getDataFinal() {
		return DataFinal;
	}
	
	public void setDataFinal(String dataFinal) {
		if (dataFinal != null)
			DataFinal = dataFinal;
	}
	public String getDataLancamento() {
		return DataLancamento;
	}
	
	public void setDataLancamento(String dataLancamento) {
		if (dataLancamento != null)
			DataLancamento = dataLancamento;
	}

	public String getPlantaoLiberado() {
		return plantaoLiberado;
	}

	public void setPlantaoLiberado(String plantaoLiberado) {
		if (plantaoLiberado != null)
			this.plantaoLiberado = plantaoLiberado;
	}
	
	public String getPropriedades(){
		String texto = super.getPropriedades();
		return texto + ";PlantaoLiberado:" + plantaoLiberado + "]";
	}
	
}
