package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.List;

import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     ParametroRelatorioTarefaDt
 * Autor:      Márcio Mendonça Gomes 
 * Data:       02/2012
 * Finalidade: Armazenar os parametros informados na página pelo usuário, para emissão do relatório 
 */
public class ParametroRelatorioTarefaDt implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2554892753658736827L;

	public static final int CodigoPermissao = 729;
	
	private TJDataHora periodoInicialUtilizado;
	private TJDataHora periodoFinalUtilizado;
	private String id_Projeto;
	private String projeto;
	private String id_ServentiaCargo;
	private String serventiaCargo;
	private List listaTarefas;
	
	public ParametroRelatorioTarefaDt(){
		limpar();
	}
	
	/**
     * Limpa os todos os parâmetros, retornando ao estado inicial, sendo:
     * Data Inicial = Primeiro dia do mês anterior à data atual
     * Data Final = Data do dia anterior à data atual (ontem)       
     */  
	public void limpar(){
		periodoInicialUtilizado = new TJDataHora();
		periodoInicialUtilizado.adicioneDia(-1);		
		periodoInicialUtilizado.atualizePrimeiraHoraDia();	
		
		periodoFinalUtilizado = new TJDataHora();
		periodoFinalUtilizado.adicioneDia(-1);
		periodoFinalUtilizado.atualizeUltimaHoraDia();	
		
		id_Projeto = "";
		projeto = "";
		
		id_ServentiaCargo = "";
		serventiaCargo = "";
		if (listaTarefas != null) listaTarefas.clear();
		listaTarefas = null;
	}

	/**
     * Armazena o período inicial utilizado no formato dd/MM/yyyy
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
     * Armazena o período fianl utilizado no formato dd/MM/yyyy
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
	
	public String getId_Projeto()
	{
		return this.id_Projeto;
	}
	
	public void setId_Projeto(String id_Projeto)
	{
		if(id_Projeto != null) this.id_Projeto = id_Projeto;
	}
	
	public String getProjeto()
	{
		return this.projeto;	
	}
	
	public void setProjeto(String projeto)
	{
		if (projeto != null) this.projeto = projeto;
	}
	
	public String getId_ServentiaCargo()
	{
		return this.id_ServentiaCargo;
	}
	
	public void setId_ServentiaCargo(String id_ServentiaCargo)
	{
		if(id_ServentiaCargo != null) this.id_ServentiaCargo = id_ServentiaCargo;
	}
	
	public String getServentiaCargo()
	{
		return this.serventiaCargo;	
	}
	
	public void setServentiaCargo(String serventiaCargo)
	{
		if (serventiaCargo != null) this.serventiaCargo = serventiaCargo;
	}
	
	public List getListaTarefas()
	{
		return this.listaTarefas;	
	}
	
	public void setListaTarefas(List listaTarefas)
	{
		this.listaTarefas = listaTarefas;
	}
	
}