package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import br.gov.go.tj.utils.ChaveDecrescente;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     RelatorioInterrupcao.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       07/2010
 * Finalidade: Encapsular o relatório de interrupções as seguintes informações:
 *             1 - O tempo total de interrupções nas últimas 24 horas 
 *                 do dia final escolhido para ser utilizado no cálculo de disponibilidade 24 horas;
 *             2 - O tempo total de disponibilidade do período para ser utilizado no cálculo de disponibilidade período;
 *             3 - A quantidade de interrupções no período;
 *             4 - Um dicionario em ordem decrescente por data de interrupção. 
 *             
 */
public class RelatorioInterrupcao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5730828355287873984L;
	
	private static final long SEGUNDOS_EM_24HORAS = 86400;
	//private static final long SEGUNDOS_EM_30DIAS = SEGUNDOS_EM_24HORAS * 30;
		
	private long tempoEmSegundosDeInterrupcoesUltimas24Horas;
	private long tempoEmSegundosDeInterrupcoesNoPeriodo;
	
	private ParametroRelatorioInterrupcaoDt parametrosUtilizados;	
	
	private SortedMap<ChaveDecrescente, OcorrenciaDiariaInterrupcao> dicDeInterrupcoes = null;
	
	private long quantidadeDeInterrupcoes;
		
	public RelatorioInterrupcao(){		
		this.dicDeInterrupcoes = new TreeMap<ChaveDecrescente, OcorrenciaDiariaInterrupcao>();
		this.limpar();		
	}
	
	/**
     * Calcula e retorna o percentual de disponibilide das últimas 24 horas da data final escolhida.
     *  
     */
	public String getPercentualDeDisponibilidadeNasUltimas24Horas() {
		return Funcoes.getDiferencaDePercentual(tempoEmSegundosDeInterrupcoesUltimas24Horas, SEGUNDOS_EM_24HORAS);		
	}
	
	/**
     * Calcula e retorna o percentual de disponibilide do período escolhido.
     *  
     */
	public String getPercentualDeDisponibilidadeNoPeriodo() {
		return Funcoes.getDiferencaDePercentual(tempoEmSegundosDeInterrupcoesNoPeriodo, obtenhaTotalSegundosPeriodo());
	}
	
	/**
     * Obtem o tempo total em segundos do período escolhido, 
     * para ser utilizado no cálculo do percentual de disponibilidade
     *  
     */
	private long obtenhaTotalSegundosPeriodo(){
		return Funcoes.diferencaDatasEmSegundos(parametrosUtilizados.getPeriodoFinalUtilizado().getDate(), 
				                                          parametrosUtilizados.getPeriodoInicialUtilizado().getDate());
	}
		
	/**
     * Armazena a interrupção no período selecionado
     *      
     * @param OcorrenciaInterrupcao interrupcao
     *  
     */
	public void adicioneInterrupcao(OcorrenciaInterrupcao interrupcao){
		//Cria um objeto de chave decrescente
		ChaveDecrescente chaveTemp = new ChaveDecrescente(interrupcao.getPeriodoInicial());
		
		OcorrenciaDiariaInterrupcao ocorrenciaTemp = null;
		
		//Verifica se já existe a data da interrupção no dicionário...
		ocorrenciaTemp = dicDeInterrupcoes.get(chaveTemp);	
		if (ocorrenciaTemp == null){
			//Caso não exista, será criado uma ocorrência diária para representar o dia da interrupção
			ocorrenciaTemp = new OcorrenciaDiariaInterrupcao();
			//Armazena a ocorrência diária no dicionário
			dicDeInterrupcoes.put(chaveTemp, ocorrenciaTemp);
		}
		
		//Adiciona a interrupção na lista de interrupções na referência da ocorrência diário,
		//obtida do dicionário ou criada e armazenada no dicionário...
		ocorrenciaTemp.adicionePeriodo(interrupcao, calculeInterrupcoes(interrupcao));	
		
		quantidadeDeInterrupcoes +=1;
		
	}
	
	/**
     * Calcula o tempo de interrupcao em segundos da ocorrência informada,
     * e verifica se a interrupção ocorreu nas últimas 24 horas da data final informada. 
     *      
     * @param OcorrenciaInterrupcao interrupcoes
     *  
     */
	private long calculeInterrupcoes(OcorrenciaInterrupcao interrupcoes){		
		long tempoEmSegundos = Funcoes.diferencaDatasEmSegundos(interrupcoes.getPeriodoFinal().getDate(),
				                                                          interrupcoes.getPeriodoInicial().getDate());
		if (tempoEmSegundos > 0){
			tempoEmSegundosDeInterrupcoesNoPeriodo += tempoEmSegundos;
			//Se ocorreu nas últimas 24 horas, o tempo em segundos é incrementado na variável correspondentes...
			if (ocorreuNasUltimas24Horas(interrupcoes.getPeriodoFinal().getDate(),
					                     interrupcoes.getPeriodoInicial().getDate())){
				tempoEmSegundosDeInterrupcoesUltimas24Horas += tempoEmSegundos;
			}
		}
		
		return tempoEmSegundos;
	}
	
	/**
     * Verica se a interrupção ocorreu nas últimas 24 horas. 
     *      
     * @param Date dataFinal
     * @param Date dataInicial
     *  
     */
	private boolean ocorreuNasUltimas24Horas(Date dataFinal, 
			                                 Date dataInicial){
		
		Date dataUltimas24Horas = obtenhaDataRefUltimas24Horas();
		
		if (dataFinal.after(dataUltimas24Horas)) return true;
		
		if (dataInicial.after(dataUltimas24Horas)) return true;		
		
		return false;
		
	}
	
	/**
     * Obtem a data com um dia anterior à data final infomada, 
     * para verificar se a interrupção ocorreu nas últimas 24 horas da data final.      
     *  
     */
	private TJDataHora dataUltimas24Horas = null;
	private Date obtenhaDataRefUltimas24Horas(){
		if (dataUltimas24Horas == null){
			dataUltimas24Horas = TJDataHora.CloneObjeto(this.parametrosUtilizados.getPeriodoFinalUtilizado());
			dataUltimas24Horas.adicioneHora(-24);
			dataUltimas24Horas.adicioneMilisegundos(-1);
		}
		
		return dataUltimas24Horas.getDate();
	}
	
	/**
     * Retorna o iterator da relação do Composite Interrupções Diárias          
     *  
     */
	public Iterator<OcorrenciaDiariaInterrupcao> getIteratorInterrupcoesDiarias(){
		return dicDeInterrupcoes.values().iterator();
	}	
	
	/**
     * Verifica se possui interrupções no período informado.          
     *  
     */
	public boolean possuiInterrupcoes(){
		return  !dicDeInterrupcoes.isEmpty();
	}	
	
	/**
     * Limpa o estado dos parâmetros do objeto.          
     *  
     */
	public void limpar(){		
		this.dicDeInterrupcoes.clear();
				
		tempoEmSegundosDeInterrupcoesUltimas24Horas = 0;
		tempoEmSegundosDeInterrupcoesNoPeriodo = 0;
		
		quantidadeDeInterrupcoes = 0;
		
		parametrosUtilizados = null;
	}

	/**
     * Armazena os parametros escolhidos pelo usuário na página.          
     *  
     *  @param ParametroRelatorioInterrupcaoDt parametrosUtilizados
     *  
     */
	public void setParametrosUtilizados(ParametroRelatorioInterrupcaoDt parametrosUtilizados) {
		this.parametrosUtilizados = parametrosUtilizados;
	}

	/**
     * Retorna os parametros escolhidos pelo usuário na página.    
     *   
     */
	public ParametroRelatorioInterrupcaoDt getParametrosUtilizados() {
		return parametrosUtilizados;
	}
	
	/**
     * Prepara e retorna uma lista de interrupções que será utilizada pelo Jasper na emissão do
     * relatório em PDF.    
	 * @throws Exception 
     *   
     */
	public List<RelatorioInterrupcaoDetalhes> obtenhaDetalhesImpressaoPDF() throws Exception{
		
		List<RelatorioInterrupcaoDetalhes> listaDeDetalhes = new ArrayList<RelatorioInterrupcaoDetalhes>();
		
		
		Iterator<OcorrenciaDiariaInterrupcao> iteratorDiario = this.getIteratorInterrupcoesDiarias();
		Iterator<OcorrenciaInterrupcao> iteratorItem = null;
		
		OcorrenciaDiariaInterrupcao objOcorDiarioTemp = null;														
		OcorrenciaInterrupcao objOcorTemp = null;
		RelatorioInterrupcaoDetalhes objIntDetTemp= null;		
												
		long numeroDaInterrupcao = 0;
		
		if (!this.possuiInterrupcoes()) {
			objIntDetTemp =  new RelatorioInterrupcaoDetalhes();
			
			objIntDetTemp.setEhTotalizador(false);
			objIntDetTemp.setNumeroDaInterrupcao("");
			objIntDetTemp.setDataHoraInicialInterrupcao("");
			objIntDetTemp.setDataHoraFinalInterrupcao("O sistema não apresentou interrupções.");
			objIntDetTemp.setTempoDeInterrupcao("");
			objIntDetTemp.setMotivo("");
			
			listaDeDetalhes.add(objIntDetTemp);
		}
		else {
			
			while (iteratorDiario.hasNext()){
				objOcorDiarioTemp = iteratorDiario.next();
				iteratorItem = objOcorDiarioTemp.getIteratorDeInterrupcoes();
				
				while (iteratorItem.hasNext()){
					
					objOcorTemp = iteratorItem.next();
					
					numeroDaInterrupcao += 1;
					
					objIntDetTemp =  new RelatorioInterrupcaoDetalhes();
					
					objIntDetTemp.setEhTotalizador(false);
					objIntDetTemp.setNumeroDaInterrupcao(String.valueOf(numeroDaInterrupcao));
					objIntDetTemp.setDataHoraInicialInterrupcao(objOcorTemp.getPeriodoInicial().getDataFormatadaddMMyyyyHHmmss());
					objIntDetTemp.setDataHoraFinalInterrupcao(objOcorTemp.getPeriodoFinal().getDataFormatadaddMMyyyyHHmmss());
					objIntDetTemp.setTempoDeInterrupcao(objOcorTemp.getTempoDeInterrupcao());
					objIntDetTemp.setMotivo(objOcorTemp.getMotivo());
					
					listaDeDetalhes.add(objIntDetTemp);	
					
				}
				
				
				objIntDetTemp =  new RelatorioInterrupcaoDetalhes();
				
				objIntDetTemp.setEhTotalizador(true);
				objIntDetTemp.setNumeroDaInterrupcao("");
				objIntDetTemp.setDataHoraInicialInterrupcao("");
				objIntDetTemp.setDataHoraFinalInterrupcao("Total no dia " + objOcorDiarioTemp.getDataDeReferencia().getDataFormatadaddMMyyyy());
				objIntDetTemp.setTempoDeInterrupcao(objOcorDiarioTemp.getTempoTotalDeInterrupcoes());
				objIntDetTemp.setMotivo("");
				
				listaDeDetalhes.add(objIntDetTemp);				
				
			}
			
		}
									
			
		
		return listaDeDetalhes;
		
	}	

	/**
     * Retorna a quantidade de interrupções que será utilizada pelo Jasper na emissão do
     * relatório em PDF.    
     *   
     */
	public long getQuantidadeDeInterrupcoes() {
		return quantidadeDeInterrupcoes;
	}	
}
