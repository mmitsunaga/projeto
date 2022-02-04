package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class UfrValorDt extends Dados {

    /**
	 * 
	 */
	private static final long serialVersionUID = -824752452068546274L;
	//public static final int CodigoPermissao=172;

    private String Id_UFR_Valor;
    private String DataInicio;
    private String DataFinal;
    private String ValorUFR;
    private String ValorTaxaJudiciaria;
    private String DataAtualizacao;
    private String CodigoTemp;    
    
    public String getId()  {return Id_UFR_Valor;}
    
	public void setId(String valor ) { if(valor!=null) Id_UFR_Valor = valor;}
    
	public String getDataInicio() {
		return DataInicio;
	}
    
	public void setDataInicio(String DataInicio) {
		if (DataInicio!=null) {
			this.DataInicio = DataInicio;
		}
	}
	
	public String getDataFinal() {
		return DataFinal;
	}
    
	public void setDataFinal(String DataFinal) {
		if (DataFinal!=null) {
			this.DataFinal = DataFinal;
		}
	}
	
	public String getValorUFR() {
		return ValorUFR;
	}
    
	public void setValorUFR(String ValorUFR) {
		if (ValorUFR!=null) {
			this.ValorUFR = ValorUFR;
		}
	}
	
	public String getValorTaxaJudiciaria() {
		return ValorTaxaJudiciaria;
	}
    
	public void setValorTaxaJudiciaria(String ValorTaxaJudiciaria) {
		if (ValorTaxaJudiciaria!=null) {
			this.ValorTaxaJudiciaria = ValorTaxaJudiciaria;
		}
	}
	
	public String getDataAtualizacao() {
		return DataAtualizacao;
	}
    
	public void setDataAtualizacao(String DataAtualizacao) {
		if (DataAtualizacao!=null) {
			this.DataAtualizacao = DataAtualizacao;
		}
	}
	
	public String getCodigoTemp() {
		return CodigoTemp;
	}
    
	public void setCodigoTemp(String CodigoTemp) {
		if (CodigoTemp!=null) {
			this.CodigoTemp = CodigoTemp;
		}
	}
	
	public Double obtenhaValorTaxaJudiciariaEmReais(String valorReferenciaEmReais) {		
		if (valorReferenciaEmReais == null || valorReferenciaEmReais.trim().length() == 0) return 0.0D;
		
		if (valorReferenciaEmReais.contains(",")) valorReferenciaEmReais = Funcoes.BancoDecimal(valorReferenciaEmReais);
		
		Double valorUFRTaxaJudiciaria = this.obtenhaValorUFRTaxaJudiciaria();
		
		return Funcoes.retirarCasasDecimais((Funcoes.StringToDouble(valorReferenciaEmReais) / valorUFRTaxaJudiciaria) * this.obtenhaValorUFR());
	}	
	
	public Double obtenhaValorURFEmReais(String valorEmUFR) {
		if (valorEmUFR == null || valorEmUFR.trim().length() == 0) return 0.0D;
		
		if (valorEmUFR.contains(",")) valorEmUFR = Funcoes.BancoDecimal(valorEmUFR);
		
		return obtenhaValorURFEmReais(Funcoes.StringToDouble(valorEmUFR)); 
	}
	
	public Double obtenhaValorURFEmReais(Double valorEmUFR) {
		return Funcoes.retirarCasasDecimais((valorEmUFR / 100) * this.obtenhaValorUFR()); 
	}
	
	public Double obtenhaValorURFSPGEmReais(String valorEmUFR) {
		if (valorEmUFR == null || valorEmUFR.trim().length() == 0) return 0.0D;
		
		if (valorEmUFR.contains(",")) valorEmUFR = Funcoes.BancoDecimal(valorEmUFR);
		
		return obtenhaValorURFSPGEmReais(Funcoes.StringToDouble(valorEmUFR)); 
	}
	
	public Double obtenhaValorURFSPGEmReais(Double valorEmUFR) {
		return Funcoes.retirarCasasDecimaisTruncando(valorEmUFR * this.obtenhaValorUFR()); 
	}
	
	public Double obtenhaValorTaxaJudiciariaSPGEmReais(String valorReferenciaEmReais) {		
		if (valorReferenciaEmReais == null || valorReferenciaEmReais.trim().length() == 0) return 0.0D;
		
		if (valorReferenciaEmReais.contains(",")) valorReferenciaEmReais = Funcoes.BancoDecimal(valorReferenciaEmReais);
		
		Double valorUFRTaxaJudiciaria = this.obtenhaValorUFRTaxaJudiciaria();
		
		return Funcoes.retirarCasasDecimaisTruncando(Funcoes.StringToDouble(valorReferenciaEmReais) * valorUFRTaxaJudiciaria);
	}	
	
	public Double obtenhaValorUFR() {
		if (this.ValorUFR == null || this.ValorUFR.trim().length() == 0) return 0.0D;
		
		return Funcoes.StringToDouble(Funcoes.BancoDecimal(this.ValorUFR));
	}
	
	public Double obtenhaValorUFRTaxaJudiciaria() {
		if (this.ValorTaxaJudiciaria == null || this.ValorTaxaJudiciaria.trim().length() == 0) return 0.0D;
		
		return Funcoes.StringToDouble(Funcoes.BancoDecimal(this.ValorTaxaJudiciaria));
	}
	
	public void limpar(){
		Id_UFR_Valor = "";
		DataInicio = "";
		DataFinal = "";
		ValorUFR = "";
		ValorTaxaJudiciaria = "";
		DataAtualizacao = "";
		CodigoTemp = "";		
	}
	
	public void copiar(UfrValorDt objeto){
		if (objeto==null) return;
		Id_UFR_Valor = objeto.getId();
		DataInicio = objeto.getDataInicio();
		DataFinal = objeto.getDataFinal();
		ValorUFR = objeto.getValorUFR();
		ValorTaxaJudiciaria = objeto.getValorTaxaJudiciaria();
	    DataAtualizacao = objeto.getDataAtualizacao();
	    CodigoTemp = objeto.getCodigoTemp();			
	}

	public String getPropriedades(){
		return "[Id_UFR_Valor:" + Id_UFR_Valor + ";DataInicio:" + DataInicio + ";DataFinal:" + DataFinal + ";ValorUFR:" + ValorUFR + ";ValorTaxaJudiciaria:" + ValorTaxaJudiciaria + ";DataAtualizacao:" + DataAtualizacao + ";CodigoTemp:" + CodigoTemp + "]";
	}	
}
