package br.gov.go.tj.projudi.dt;

public class ProcessoParteDebitoFisicoDtGen extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7636321353054855288L;
	
	private String Id_ProcessoParteDebitoFisico;
	private String ProcessoDebito;
	private String Id_ProcessoDebito;
	private String ProcessoNumeroCompleto;
	private String CodigoExternoServentia;
	private String DescricaoServentia;
	private String Id_ProcessoParte;	
	private String Nome;
	private String NomeSimplificado;
	private String cpfParte;
	private String tipoParte;
	private String numeroGuia;
	private String valorTotalGuia;
	private String processoNumeroPROAD;
	private String id_ProcessoDebitoStatus;
	private String processoDebitoStatus;
	private String dataBaixa;
	private String CodigoTemp;

//---------------------------------------------------------
	public ProcessoParteDebitoFisicoDtGen() {

		limpar();

	}

	public String getId()  {return Id_ProcessoParteDebitoFisico;}
	public void setId(String valor ) {if(valor!=null) Id_ProcessoParteDebitoFisico = valor;}
	public String getProcessoDebito()  {return ProcessoDebito;}
	public void setProcessoDebito(String valor ) {if (valor!=null) ProcessoDebito = valor;}
	public String getId_ProcessoDebito()  {return Id_ProcessoDebito;}
	public void setId_ProcessoDebito(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoDebito = ""; ProcessoDebito = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoDebito = valor;}
	
	public String getProcessoNumeroCompleto() {
		return ProcessoNumeroCompleto;
	}

	public void setProcessoNumeroCompleto(String numeroProcessoCompleto) {
		if (numeroProcessoCompleto != null) this.ProcessoNumeroCompleto = numeroProcessoCompleto;
	}
	
	public String getCodigoExternoServentia() {
		return CodigoExternoServentia;
	}

	public void setCodigoExternoServentia(String codigoExternoServentia) {
		if (codigoExternoServentia != null) this.CodigoExternoServentia = codigoExternoServentia;
	}
	
	public String getDescricaoServentia() {
		return DescricaoServentia;
	}

	public void setDescricaoServentia(String descricaoServentia) {
		if (descricaoServentia != null) this.DescricaoServentia = descricaoServentia;
	}
	
	public String getId_ProcessoParte()  {return Id_ProcessoParte;}
	public void setId_ProcessoParte(String valor ) {if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoParte = ""; Nome = "";}else if (!valor.equalsIgnoreCase("")) Id_ProcessoParte = valor;}
	public String getNome()  {return Nome;}
	public void setNome(String valor ) {if (valor!=null) Nome = valor;}
	
	public String getNomeSimplificado() {
        return NomeSimplificado;
    }

    public void setNomeSimplificado(String nomeSimplificado) {
        NomeSimplificado = nomeSimplificado;
    }
	
	public String getCpfParte() {
		return cpfParte;
	}

	public void setCpfParte(String cpfParte) {
		if (cpfParte != null) this.cpfParte = cpfParte;
	}
	
	public String getTipoParte() {
		return tipoParte;
	}

	public void setTipoParte(String tipoParte) {
		if (tipoParte != null) this.tipoParte = tipoParte;
	}
	
	public String getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(String numeroGuia) {
		if (numeroGuia != null) this.numeroGuia = numeroGuia;
	}
	
	public String getValorTotalGuia() {
		return valorTotalGuia;
	}

	public void setValorTotalGuia(String valorTotalGuia) {
		if (valorTotalGuia != null) this.valorTotalGuia = valorTotalGuia;
	}
	
	public String getProcessoNumeroPROAD() {
		return this.processoNumeroPROAD;
	}
	
	public void setProcessoNumeroPROAD(String processoNumeroPROAD) {
		if (processoNumeroPROAD != null) this.processoNumeroPROAD = processoNumeroPROAD;
	}
	
	public String getId_ProcessoDebitoStatus() {
		return id_ProcessoDebitoStatus;
	}

	public void setId_ProcessoDebitoStatus(String id_ProcessoDebitoStatus) {
		if (id_ProcessoDebitoStatus != null) this.id_ProcessoDebitoStatus = id_ProcessoDebitoStatus;
	}
	
	public String getProcessoDebitoStatus() {
		return this.processoDebitoStatus;
	}
	
	public void setProcessoDebitoStatus(String processoDebitoStatus) {
		if (processoDebitoStatus != null) this.processoDebitoStatus = processoDebitoStatus;
	}
	
	public String getDataBaixa() {
		return this.dataBaixa;
	}
	
	public void setDataBaixa(String dataBaixa) {
		if (dataBaixa != null) this.dataBaixa = dataBaixa;
	}
	
	public String getCodigoTemp()  {return CodigoTemp;}
	public void setCodigoTemp(String valor ) {if (valor!=null) CodigoTemp = valor;}


	public void copiar(ProcessoParteDebitoFisicoDt objeto){
		Id_ProcessoParteDebitoFisico = objeto.getId();
		Id_ProcessoDebito = objeto.getId_ProcessoDebito();
		ProcessoDebito = objeto.getProcessoDebito();
		Id_ProcessoParte = objeto.getId_ProcessoParte();
		Nome = objeto.getNome();
		CodigoTemp = objeto.getCodigoTemp();
		ProcessoNumeroCompleto = objeto.getProcessoNumeroCompleto();
		CodigoExternoServentia = objeto.getCodigoExternoServentia();
		DescricaoServentia = objeto.getDescricaoServentia();
		cpfParte = objeto.getCpfParte();
		numeroGuia = objeto.getNumeroGuia();
		valorTotalGuia = objeto.getValorTotalGuia();
		id_ProcessoDebitoStatus = objeto.getId_ProcessoDebitoStatus();
		processoDebitoStatus = objeto.getProcessoDebitoStatus();
		dataBaixa = objeto.getDataBaixa();
		processoNumeroPROAD = objeto.getProcessoNumeroPROAD();
		NomeSimplificado = objeto.getNomeSimplificado();
		tipoParte = objeto.getTipoParte();
	}

	public void limpar(){
		limparParcial();
		ProcessoNumeroCompleto = "";
		CodigoExternoServentia = "";
		DescricaoServentia = "";		
	}
	
	public void limparParcial(){
		Id_ProcessoParteDebitoFisico="";
		Id_ProcessoDebito="";
		ProcessoDebito="";
		Id_ProcessoParte="";
		Nome="";
		NomeSimplificado = "";
		CodigoTemp="";
		cpfParte = "";
		numeroGuia = "";
		valorTotalGuia = "";
		id_ProcessoDebitoStatus = "";
		processoDebitoStatus = "";
		dataBaixa = "";
		processoNumeroPROAD = "";
		tipoParte = "";		
	}

	public String getPropriedades(){
		return "[Id_ProcessoParteDebito:" + Id_ProcessoParteDebitoFisico + ";Id_ProcessoDebito:" + Id_ProcessoDebito + ";ProcessoDebito:" + ProcessoDebito + ";ProcessoNumeroCompleto:" + ProcessoNumeroCompleto + ";CodigoExternoServentia:" + CodigoExternoServentia + ";DescricaoServentia:" + DescricaoServentia + ";Id_ProcessoParte:" + Id_ProcessoParte + ";Nome:" + Nome + ";NomeSimplificado:" + NomeSimplificado + ";CPFParte:" + cpfParte + ";NumeroGuia:" + numeroGuia + ";ValorTotalGuia:" + valorTotalGuia + ";ProcessoNumeroPROAD:" + processoNumeroPROAD + ";Status:" + processoDebitoStatus + ";DataBaixa:" + dataBaixa + ";CodigoTemp:" + CodigoTemp + "]";
	}
} 
