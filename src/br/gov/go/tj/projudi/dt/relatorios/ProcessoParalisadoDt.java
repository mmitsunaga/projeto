package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;

/**
 * Objeto para auxiliar na criação do relatório
 */
public class ProcessoParalisadoDt extends Dados{

	private static final long serialVersionUID = -1981439337721992739L;

	public static final int CodigoPermissao = 489;

	private String idServentia;
	private String serventia;
	private String periodo;

	private String idProcesso;
	private String numeroProcesso;
	private String dataRecebimento;
	private String quantidadeDiasParalisados;
	private String tipoPendencia;
	
	private String idServentiaCargo;
	private String serventiaCargo;
	//guarda o asssistente que esta paralisando o processo
	private String assistente;

	

	/**
	 * Construtor. Inicializa todas as variáveis
	 */
	public ProcessoParalisadoDt() {
		limpar();
	}

	public void limpar() {
		idServentia = "";
		idProcesso = "";
		serventia = "";
		numeroProcesso = "";
		dataRecebimento = "";
		tipoPendencia = "";
		periodo = "";
		quantidadeDiasParalisados = "";
		idServentiaCargo = "";
		serventiaCargo = "";
		assistente = "";
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		if (serventia!= null){
			if (serventia.equalsIgnoreCase("null")) this.serventia = "";
			else this.serventia = serventia;
		}
	}

	public String getIdServentia() {
		return idServentia;
	}

	public void setIdServentia(String idServentia) {		
		if (idServentia!= null){
			if (idServentia.equalsIgnoreCase("null")) this.idServentia = "";
			else this.idServentia = idServentia;
		}
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(String dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getTipoPendencia() {
		return tipoPendencia;
	}

	public void setTipoPendencia(String tipoPendencia) {
		this.tipoPendencia = tipoPendencia;
	}

	public String getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {			   
        if (periodo != null &&  !periodo.equalsIgnoreCase("null"))  this.periodo = periodo;
	}

	public String getQuantidadeDiasParalisados() {
		return quantidadeDiasParalisados;
	}

	public void setQuantidadeDiasParalisados(String quantidadeDiasParalisados) {
		this.quantidadeDiasParalisados = quantidadeDiasParalisados;
	}
	
	public String getAssistente() {
		return assistente;
	}

	public void setAssistente(String assistente) {
		this.assistente = assistente;
	}

	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}

	public String getIdServentiaCargo(){
		return this.idServentiaCargo;
	}
	
	public void setIdServentiaCargo(String idServentiaCargo){
		if (idServentiaCargo!= null){
			if (idServentiaCargo.equalsIgnoreCase("null")){
				this.idServentiaCargo = "";
			}else if( !idServentiaCargo.equalsIgnoreCase("")){
				this.idServentiaCargo = idServentiaCargo;
			}
		}
	}
	
	public String getServentiaCargo(){
		return this.serventiaCargo;
	}
	
	public void setServentiaCargo(String serventiaCargo){
		if (serventiaCargo != null) {
			if (serventiaCargo.equalsIgnoreCase("null")){
				this.serventiaCargo = "";
			}else if( !serventiaCargo.equalsIgnoreCase("")){
				this.serventiaCargo = serventiaCargo;
			}
		}
	}
	
	
}