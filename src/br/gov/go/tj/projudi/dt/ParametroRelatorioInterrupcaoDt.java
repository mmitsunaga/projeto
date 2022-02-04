package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

import br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaPingdom;
import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     ParametroRelatorioInterrupcaoDt.java
 * Autor:      M�rcio Mendon�a Gomes 
 * Data:       07/2010
 * Finalidade: Armazenar os parametros informados na p�gina pelo usu�rio, para emiss�o do relat�rio 
 */
public class ParametroRelatorioInterrupcaoDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1670888252613409455L;

	private EnumSistemaPingdom sistema;
	private TJDataHora periodoInicialUtilizado;
	private TJDataHora periodoFinalUtilizado;
	private boolean consideraInterrupcaoTotal;
	private boolean consideraInterrupcaoParcial;
	
	public ParametroRelatorioInterrupcaoDt(){
		limpar();
	}
	
	/**
     * Limpa os todos os par�metros, retornando ao estado inicial, sendo:
     * Data Inicial = Primeiro dia do m�s anterior � data atual
     * Data Final = Data do dia anterior � data atual (ontem)       
     */  
	public void limpar(){
		periodoInicialUtilizado = new TJDataHora();
		periodoInicialUtilizado.adicioneDia(-1);		
		periodoInicialUtilizado.atualizePrimeiraHoraDia();	
		
		periodoFinalUtilizado = new TJDataHora();
		periodoFinalUtilizado.adicioneDia(-1);
		periodoFinalUtilizado.atualizeUltimaHoraDia();	
	
		this.sistema = EnumSistemaPingdom.projudi;
		this.consideraInterrupcaoTotal = false;
		this.consideraInterrupcaoParcial = false;
	}

	/**
     * Armazena o per�odo inicial utilizado no formato dd/MM/yyyy
     * @param String dataInicial       
	 * @throws Exception 
     */
	public void setPeriodoInicialUtilizado(String dataInicial) throws Exception{
		if (dataInicial == null || dataInicial.trim().equalsIgnoreCase("")) return;
		this.periodoInicialUtilizado.setDataddMMaaaa(dataInicial);
		this.periodoInicialUtilizado.atualizePrimeiraHoraDia();		
	}

	/**
     * retorna Periodo Inicial
     *        
     */
	public TJDataHora getPeriodoInicialUtilizado() {
		return periodoInicialUtilizado;
	}

	/**
     * Armazena o per�odo fianl utilizado no formato dd/MM/yyyy
     * @param String dataFinal       
	 * @throws Exception 
     */
	public void setPeriodoFinalUtilizado(String dataFinal) throws Exception{
		if (dataFinal == null || dataFinal.trim().equalsIgnoreCase("")) return;
		this.periodoFinalUtilizado.setDataddMMaaaa(dataFinal);
		this.periodoFinalUtilizado.atualizeUltimaHoraDia();				
	}
	
	public void setPeriodoFinalUtilizado(TJDataHora dataFinal){
		if (dataFinal == null) return;
		this.periodoFinalUtilizado = dataFinal;	
		this.periodoFinalUtilizado.atualizeUltimaHoraDia();		
	}

	/**
     * retorna Periodo Final
     *        
     */
	public TJDataHora getPeriodoFinalUtilizado() {
		return periodoFinalUtilizado;
	}

	/**
     * Obtem o nome do sistema parametrizado no web.xml
     *        
     */
	public String getNomeDoSistema(){
		return this.sistema.getValor();
	}
	
	public String getNomeDoSistemaCompleto(){
		return this.sistema.getValorCompleto();
	}
	
	public String getURLDoSistema(){
		return this.sistema.getURL();
	}
	
	public EnumSistemaPingdom getSistema(){
		return this.sistema;
	} 
	
	public EnumSistemaPingdom setSistema(EnumSistemaPingdom sistema){
		return this.sistema = sistema;
	}

	public boolean isConsideraInterrupcaoTotal() {
		return consideraInterrupcaoTotal;
	}

	public void setConsideraInterrupcaoTotal(boolean consideraInterrupcaoTotal) {
		this.consideraInterrupcaoTotal = consideraInterrupcaoTotal;
	}

	public boolean isConsideraInterrupcaoParcial() {
		return consideraInterrupcaoParcial;
	}

	public void setConsideraInterrupcaoParcial(boolean consideraInterrupcaoParcial) {
		this.consideraInterrupcaoParcial = consideraInterrupcaoParcial;
	}
}