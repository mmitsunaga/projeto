package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     GraficoMovimentacaoAdvogadoDt
 * Autor:      Márcio Mendonça Gomes 
 * Data:       02/2013
 * Finalidade: Armazenar os parametros informados na página pelo usuário, para emissão do relatório 
 */
public class GraficoMovimentacaoAdvogadoDt extends Dados {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1978093609051281876L;
	
	// CÓDIGO DE SEGURANÇA DA CLASSE
	public static final int CodigoPermissao = 803;
	
	public enum EnumDataRelatorio
	{
		MOVIMENTACAO_COMPARACAO,
		MOVIMENTACAO_ANALISE,
		ADICIONAL_001,
		ADICIONAL_002,
		ADICIONAL_003
	}
	
	private TJDataHora dataMovimentacaoAnalise;
	private TJDataHora dataMovimentacaoComparacao;
	private TJDataHora dataAdicional001;
	private TJDataHora dataAdicional002;
	private TJDataHora dataAdicional003;
	
	public GraficoMovimentacaoAdvogadoDt(){
		limpar();
	}
	
	@Override
	public String getId() {
		return "";
	}

	@Override
	public void setId(String id) {				
	}
	
	/**
     * Limpa os todos os parâmetros       
     */  
	public void limpar(){
		dataMovimentacaoAnalise = new TJDataHora();
		dataMovimentacaoAnalise.adicioneDia(-1);		
		dataMovimentacaoAnalise.atualizePrimeiraHoraDia();	
		
		dataMovimentacaoComparacao = new TJDataHora();
		dataMovimentacaoComparacao.adicioneDia(-2);
		dataMovimentacaoAnalise.atualizePrimeiraHoraDia();
		
		this.dataAdicional001 = null;
		this.dataAdicional002 = null;
		this.dataAdicional003 = null;
	}

	/**
     * Armazena a data para análise
     * @param String dataMovimentacaoAnalise       
	 * @throws Exception 
     */
	public void setDataMovimentacaoAnalise(String dataMovimentacaoAnalise) throws Exception{
		if (dataMovimentacaoAnalise == null || dataMovimentacaoAnalise.trim().equalsIgnoreCase("")) return;
		this.dataMovimentacaoAnalise.setDataddMMaaaa(dataMovimentacaoAnalise);
		this.dataMovimentacaoAnalise.atualizePrimeiraHoraDia();		
	}

	/**
     * retorna a data para análise
     *        
     */
	public TJDataHora getDataMovimentacaoAnalise() {
		return dataMovimentacaoAnalise;
	}

	/**
     * Armazena a data para comparação
     * @param String dataMovimentacaoComparacao       
	 * @throws Exception 
     */
	public void setDataMovimentacaoComparacao(String dataMovimentacaoComparacao) throws Exception{
		if (dataMovimentacaoComparacao == null || dataMovimentacaoComparacao.trim().equalsIgnoreCase("")) return;
		this.dataMovimentacaoComparacao.setDataddMMaaaa(dataMovimentacaoComparacao);
		this.dataMovimentacaoComparacao.atualizePrimeiraHoraDia();		
	}

	/**
     * retorna a data para comparação
     *        
     */
	public TJDataHora getDataMovimentacaoComparacao() {
		return dataMovimentacaoComparacao;
	}
	
	/**
     * Armazena a data adicional 001
     * @param String dataAdicional001       
	 * @throws Exception 
     */
	public void setDataAdicional001(String dataAdicional001) throws Exception{
		if (dataAdicional001 == null || dataAdicional001.trim().equalsIgnoreCase("")) return;
		this.dataAdicional001 = new TJDataHora(dataAdicional001);
		this.dataAdicional001.atualizePrimeiraHoraDia();		
	}

	/**
     * retorna a data adicional 001
     *        
     */
	public TJDataHora getDataAdicional001() {
		return dataAdicional001;
	}
	
	/**
     * retorna a data adicional 001 formatada
     *        
     */
	public String getDataAdicional001FormatadaddMMyyyy() {
		if (this.dataAdicional001 == null) return "";
		
		return this.dataAdicional001.getDataFormatadaddMMyyyy();
	}
	
	/**
     * Armazena a data adicional 002
     * @param String dataAdicional002       
	 * @throws MensagemException 
     */
	public void setDataAdicional002(String dataAdicional002) throws MensagemException{
		if (dataAdicional002 == null || dataAdicional002.trim().equalsIgnoreCase("")) return;
		this.dataAdicional002 = new TJDataHora(dataAdicional002);
		this.dataAdicional002.atualizePrimeiraHoraDia();		
	}

	/**
     * retorna a data adicional 002
     *        
     */
	public TJDataHora getDataAdicional002() {
		return dataAdicional002;
	}
	
	/**
     * retorna a data adicional 002 formatada
     *        
     */
	public String getDataAdicional002FormatadaddMMyyyy() {
		if (this.dataAdicional002 == null) return "";
		
		return this.dataAdicional002.getDataFormatadaddMMyyyy();
	}
	
	/**
     * Armazena a data adicional 003
     * @param String dataAdicional003       
	 * @throws MensagemException 
     */
	public void setDataAdicional003(String dataAdicional003) throws MensagemException{
		if (dataAdicional003 == null || dataAdicional003.trim().equalsIgnoreCase("")) return;
		this.dataAdicional003 = new TJDataHora(dataAdicional003);
		this.dataAdicional003.atualizePrimeiraHoraDia();		
	}

	/**
     * retorna a data adicional 003
     *        
     */
	public TJDataHora getDataAdicional003() {
		return dataAdicional003;
	}
	
	/**
     * retorna a data adicional 003 formatada
     *        
     */
	public String getDataAdicional003FormatadaddMMyyyy() {
		if (this.dataAdicional003 == null) return "";
		
		return this.dataAdicional003.getDataFormatadaddMMyyyy();
	}
}