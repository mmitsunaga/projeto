package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import br.gov.go.tj.projudi.ne.PrazoSuspensoNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
import br.gov.go.tj.utils.ChaveCrescente;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

/**
 * 
 * Classe:     RelatorioRepassePrefeitura.java
 * Autor:      Márcio Mendonça Gomes 
 * Data:       06/2015
 */
public class RelatorioRepassePrefeituraDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3405258770171294902L;

	private FinanceiroConsultarRepassePrefeituraDt parametrosUtilizados;	
	
	private SortedMap<ChaveCrescente, OcorrenciaDiariaPrevisaoRepasseDt> dicDePrevisaoRepasse = null;
	
	private TJDataHora dataInicioPrevisaoRepasse;
	
	private TJDataHora dataFimPrevisaoRepasse;
	
	private String id_Comarca;
	
	private String id_Cidade;
	
	private String id_Serventia;
	
	private List<TJDataHora> listaDataMovimentoConsulta;
	
	private Double valorTotalPrevistoParaRepasse = 0.0D;
	
	private Double valorTotalDasGuias = 0.0D;
	
	public RelatorioRepassePrefeituraDt(FinanceiroConsultarRepassePrefeituraDt parametrosUtilizados) throws Exception{		
		this.dicDePrevisaoRepasse = new TreeMap<ChaveCrescente, OcorrenciaDiariaPrevisaoRepasseDt>();
		this.listaDataMovimentoConsulta = new ArrayList<TJDataHora>();
		
		this.limpar();
		
		this.parametrosUtilizados = parametrosUtilizados;
		
		ServentiaDt serventia = new ServentiaNe().consultarId(parametrosUtilizados.getId());		
		if (serventia != null) {
			this.id_Comarca = serventia.getId_Comarca();
			this.id_Cidade = serventia.getId_Cidade();
			this.id_Serventia = serventia.getId();	
		}
		
		if (parametrosUtilizados.getTipoFiltroData().equalsIgnoreCase("0")) {
			this.dataInicioPrevisaoRepasse = parametrosUtilizados.getDataInicio();
			this.dataFimPrevisaoRepasse = parametrosUtilizados.getDataFim();			
		} else {
			this.dataInicioPrevisaoRepasse = TJDataHora.CloneObjeto(this.parametrosUtilizados.getDataInicio());
			this.dataInicioPrevisaoRepasse.adicioneDia(10);
			
			this.dataFimPrevisaoRepasse = TJDataHora.CloneObjeto(this.parametrosUtilizados.getDataFim());
			this.dataFimPrevisaoRepasse.adicioneDia(10);
		}	
		
		atualizeDataInicioEFimPrevisaoRepasse();
		
		if (parametrosUtilizados.getTipoFiltroData().equalsIgnoreCase("0")) {
			// Por data de previsão para o repasse...
			atualizeListaDeDatasDePagamentoComCalculoDeDatas(parametrosUtilizados.getDataInicio(), parametrosUtilizados.getDataFim());
		} else {
			//Por data de pagamento da guia...
			atualizeListaDeDatasDePagamento(parametrosUtilizados.getDataInicio(), parametrosUtilizados.getDataFim());
		}
	}
	
	private void atualizeListaDeDatasDePagamentoComCalculoDeDatas(TJDataHora dataInicio, TJDataHora dataFim) throws Exception
	{
		TJDataHora dataInicioPagamento = TJDataHora.CloneObjeto(dataInicio);
		dataInicioPagamento.adicioneDia(-10);
		ajusteDataDePagamentoParaDiaUtil(dataInicioPagamento);
		
		//No caso de haver mais de uma data de pagamento para a mesma data de repasse...
		TJDataHora dataRepasseProvavel = null;
		do {			
			TJDataHora dataInicioPagamentoProvavel = TJDataHora.CloneObjeto(dataInicioPagamento);
			dataInicioPagamentoProvavel.adicioneDia(-1);
			ajusteDataDePagamentoParaDiaUtil(dataInicioPagamentoProvavel);
			
			dataRepasseProvavel = TJDataHora.CloneObjeto(dataInicioPagamentoProvavel);  
			dataRepasseProvavel.adicioneDia(10);					
			ajusteDataDeRepasseParaDiaUtil(dataRepasseProvavel);
			
			if (dataInicio.equals(dataRepasseProvavel)) dataInicioPagamento = dataInicioPagamentoProvavel;			
						
		} while (dataInicio.equals(dataRepasseProvavel));
		
		TJDataHora dataFimPagamento = TJDataHora.CloneObjeto(dataFim);
		dataFimPagamento.adicioneDia(-10);
		ajusteDataDePagamentoParaDiaUtil(dataFimPagamento);
		
		atualizeListaDeDatasDePagamento(dataInicioPagamento, dataFimPagamento);
	}
		
	private void ajusteDataDePagamentoParaDiaUtil(TJDataHora dataHora) throws Exception
	{
		verificaSabadoDomigoDecremente(dataHora);
		PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
		
		boolean boTeste;		
		ServentiaDt serventiaPrazoDt = new ServentiaDt();
		
		serventiaPrazoDt.setId_Cidade( this.id_Cidade);
		serventiaPrazoDt.setId_Comarca( this.id_Comarca);
		serventiaPrazoDt.setId( this.id_Serventia);
		
		do {
            boTeste = prazoSuspensoNe.isDataValida(dataHora.getDate(), serventiaPrazoDt);
            if (!boTeste) dataHora.adicioneDia(-1);

        } while (verificaSabadoDomigoDecremente(dataHora) || !boTeste);
	}
	
	private boolean verificaSabadoDomigoDecremente(TJDataHora dataHora) {
        if (dataHora.isSabado()) {
        	dataHora.adicioneDia(-1);
            return true;
        } else if (dataHora.isDomingo()) {
        	dataHora.adicioneDia(-2);
        	return true;
        }
        return false;
    }
	
	private void atualizeListaDeDatasDePagamento(TJDataHora dataInicio, TJDataHora dataFim)
	{
		TJDataHora datapagamento = TJDataHora.CloneObjeto(dataInicio);
		listaDataMovimentoConsulta.add(datapagamento);
		while (dataFim.ehApos(datapagamento)) {
			datapagamento = TJDataHora.CloneObjeto(datapagamento);
			datapagamento.adicioneDia(1);
			listaDataMovimentoConsulta.add(datapagamento);
		}
	}
	
	private void atualizeDataInicioEFimPrevisaoRepasse() throws Exception
	{	
		ajusteDataDeRepasseParaDiaUtil(this.dataInicioPrevisaoRepasse);
		ajusteDataDeRepasseParaDiaUtil(this.dataFimPrevisaoRepasse);
	}
	
	private void ajusteDataDeRepasseParaDiaUtil(TJDataHora dataHora) throws Exception
	{
		verificaSabadoDomigoIncremente(dataHora);
		PrazoSuspensoNe prazoSuspensoNe = new PrazoSuspensoNe();
        
		ServentiaDt serventiaPrazoDt = new ServentiaDt();
		
		serventiaPrazoDt.setId_Cidade( this.id_Cidade);
		serventiaPrazoDt.setId_Comarca( this.id_Comarca);
		serventiaPrazoDt.setId( this.id_Serventia);
        
		boolean boTeste;		
		do {
            boTeste = prazoSuspensoNe.isDataValida(dataHora.getDate(),serventiaPrazoDt );
            if (!boTeste) dataHora.adicioneDia(1);

        } while (verificaSabadoDomigoIncremente(dataHora) || !boTeste);
	}
	
	private boolean verificaSabadoDomigoIncremente(TJDataHora dataHora) {
        if (dataHora.isSabado()) {
        	dataHora.adicioneDia(2);
            return true;
        } else if (dataHora.isDomingo()) {
        	dataHora.adicioneDia(1);
        	return true;
        }
        return false;
    }
	
	public void adicionePrevisaoDeRepasse(OcorrenciaPrevisaoRepasseDt ocorrenciaPrevisaoRepasse){
		//Cria um objeto de chave crescente
		ChaveCrescente chaveTemp = new ChaveCrescente(ocorrenciaPrevisaoRepasse.getDataPrevisaoRepasse());
		
		OcorrenciaDiariaPrevisaoRepasseDt ocorrenciaTemp = null;
		
		//Verifica se já existe a data de repasse no dicionário...
		ocorrenciaTemp = dicDePrevisaoRepasse.get(chaveTemp);	
		if (ocorrenciaTemp == null){
			//Caso não exista, será criado uma ocorrência diária para representar o dia da interrupção
			ocorrenciaTemp = new OcorrenciaDiariaPrevisaoRepasseDt();
			//Armazena a ocorrência diária no dicionário
			dicDePrevisaoRepasse.put(chaveTemp, ocorrenciaTemp);
		}
		
		//Adiciona a previsão de repasse na lista como ocorrência diária,
		//obtida do dicionário ou criada e armazenada no dicionário...
		ocorrenciaTemp.adicionePeriodo(ocorrenciaPrevisaoRepasse);
		
		valorTotalPrevistoParaRepasse += Funcoes.StringToDouble( Funcoes.FormatarDecimal(ocorrenciaPrevisaoRepasse.getValorRecebimento()).replace(".", "").replace(",", ".") );
		
		valorTotalDasGuias += Funcoes.StringToDouble( Funcoes.FormatarDecimal(ocorrenciaPrevisaoRepasse.getValorTotalGuia()).replace(".", "").replace(",", ".") );
	}
	
	public Iterator<OcorrenciaDiariaPrevisaoRepasseDt> getIteratorOcorrenciaPrevisaoRepasseDiarias(){
		return dicDePrevisaoRepasse.values().iterator();
	}	
	
	public boolean possuiPrevisaoParaRepasse(){
		return  !dicDePrevisaoRepasse.isEmpty();
	}	
	
	public void limpar(){		
		this.dicDePrevisaoRepasse.clear();
				
		valorTotalPrevistoParaRepasse = 0.0D;
		
		valorTotalDasGuias = 0.0D;
		
		parametrosUtilizados = null;
		
		this.id_Comarca = "";
		this.id_Cidade = "";
		this.id_Serventia = "";
	}

	public void setParametrosUtilizados(FinanceiroConsultarRepassePrefeituraDt parametrosUtilizados) {
		this.parametrosUtilizados = parametrosUtilizados;
	}

	public FinanceiroConsultarRepassePrefeituraDt getParametrosUtilizados() {
		return parametrosUtilizados;
	}

	public TJDataHora getDataInicioRepasse() {
		return dataInicioPrevisaoRepasse;
	}

	public void setDataInicioRepasse(TJDataHora dataInicioRepasse) {
		this.dataInicioPrevisaoRepasse = dataInicioRepasse;
	}

	public TJDataHora getDataFimPrevisaoRepasse() {
		return dataFimPrevisaoRepasse;
	}

	public void setDataFimPrevisaoRepasse(TJDataHora dataFimPrevisaoRepasse) {
		this.dataFimPrevisaoRepasse = dataFimPrevisaoRepasse;
	}
	
	public List<TJDataHora> getListaDataMovimentoConsulta()
	{
		return this.listaDataMovimentoConsulta;
	}

	public TJDataHora getDataPrevisaoRepasse(TJDataHora dataBaixa) throws Exception{
		TJDataHora dataPrevisaoRepasse = TJDataHora.CloneObjeto(dataBaixa);
		dataPrevisaoRepasse.adicioneDia(10);
		ajusteDataDeRepasseParaDiaUtil(dataPrevisaoRepasse);
		return dataPrevisaoRepasse;
	}
	
	public String getValorTotalPrevistoParaRepasse(){
		return Funcoes.FormatarDecimal(valorTotalPrevistoParaRepasse);
	}

	public String getValorTotalDasGuias() {
		return Funcoes.FormatarDecimal(valorTotalDasGuias);
	}
	
	public String getDiferenca() {
		if (valorTotalDasGuias > valorTotalPrevistoParaRepasse)
			return "(" + Funcoes.FormatarDecimal(valorTotalDasGuias - valorTotalPrevistoParaRepasse) + ")";
		
		return Funcoes.FormatarDecimal(valorTotalDasGuias - valorTotalPrevistoParaRepasse);
	}
	
	private final String SEPARADOR_CSV = ";";
	
	public String getConteudoArquivoCSV() {
		StringBuilder conteudo = new StringBuilder();
		
		if (getParametrosUtilizados().getTipoFiltroDetalhe().equalsIgnoreCase("0")) {
			conteudo.append("SEQ");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("GUIA");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("PROCESSO");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("BAIXA (MOVIMENTO)");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("PAGAMENTO");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("PREVISAO REPASSE");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("VALOR GUIA");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("VALOR RECEBIMENTO/REPASSE");
			conteudo.append("\n");
		} else {
			conteudo.append("PREVISAO DATA REPASSE");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("VALOR TOTAL GUIAS");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("PREVISAO TOTAL REPASSE");
			conteudo.append(SEPARADOR_CSV);
			conteudo.append("DIFERENCA");
			conteudo.append("\n");	
		}		
		
		if (!possuiPrevisaoParaRepasse()) {
			conteudo.append("Não existem repasses para o período informado");
			conteudo.append("\n");
		}  else {
			Iterator<OcorrenciaDiariaPrevisaoRepasseDt> iteratorDiario = getIteratorOcorrenciaPrevisaoRepasseDiarias();
			Iterator<OcorrenciaPrevisaoRepasseDt> iteratorItem = null;
					
			OcorrenciaDiariaPrevisaoRepasseDt objDiarioTemp = null;														
			OcorrenciaPrevisaoRepasseDt objTemp = null;
			
			long qtdeLinha = 0;
			
			while (iteratorDiario.hasNext()){
				objDiarioTemp = iteratorDiario.next();
				iteratorItem = objDiarioTemp.getIteratorDePrevisaoRepasse();
				
				if (getParametrosUtilizados().getTipoFiltroDetalhe().equalsIgnoreCase("0")) {
					while (iteratorItem.hasNext()){																								
						objTemp = iteratorItem.next();
						qtdeLinha += 1;
						
						conteudo.append(qtdeLinha);
						conteudo.append(SEPARADOR_CSV);
						conteudo.append(objTemp.getNumeroGuiaEmissao());
						conteudo.append(SEPARADOR_CSV);
						conteudo.append(Funcoes.formataNumeroCompletoProcesso(objTemp.getNumeroProcesso()));
						conteudo.append(SEPARADOR_CSV);
						conteudo.append(objTemp.getDataMovimento().getDataFormatadaddMMyyyy());
						conteudo.append(SEPARADOR_CSV);
						conteudo.append(objTemp.getDataPagamento().getDataFormatadaddMMyyyy());
						conteudo.append(SEPARADOR_CSV);
						conteudo.append(objTemp.getDataPrevisaoRepasse().getDataFormatadaddMMyyyy());
						conteudo.append(SEPARADOR_CSV);
						conteudo.append("R$ " + Funcoes.FormatarDecimal(objTemp.getValorTotalGuia()).replace(".", "").replace(",", "."));
						conteudo.append(SEPARADOR_CSV);
						conteudo.append("R$ " + Funcoes.FormatarDecimal(objTemp.getValorRecebimento()).replace(".", "").replace(",", "."));
						conteudo.append("\n");
					}
				} else {
					conteudo.append(objDiarioTemp.getDataPrevisaoRepasse().getDataFormatadaddMMyyyy());
					conteudo.append(SEPARADOR_CSV);
					conteudo.append("R$ " + objDiarioTemp.getValorTotalDasGuias().replace(".", "").replace(",", "."));
					conteudo.append(SEPARADOR_CSV);
					conteudo.append("R$ " + objDiarioTemp.getValorTotalPrevistoParaRepasse().replace(".", "").replace(",", "."));
					conteudo.append(SEPARADOR_CSV);
					conteudo.append("R$ " + Funcoes.FormatarDecimal(objDiarioTemp.getDiferenca()).replace(".", "").replace(",", "."));				
					conteudo.append("\n");
				}
			}
		}
		
		return conteudo.toString();
	}
	
	public String getNomeArquivoCSV() {
		StringBuilder nomeArquivo = new StringBuilder();
		
		nomeArquivo.append("PREVISAO_REPASSE_PREFEITURA_GOIANIA_");
		nomeArquivo.append(getDataInicioRepasse().getDataHoraFormatadayyyyMMdd());
		if(!getDataInicioRepasse().getDataHoraFormatadayyyyMMdd().equals(getDataFimPrevisaoRepasse().getDataHoraFormatadayyyyMMdd())) {
			nomeArquivo.append("_");
			nomeArquivo.append(getDataFimPrevisaoRepasse().getDataHoraFormatadayyyyMMdd());
		}
		//nomeArquivo.append(".CSV");
		
		return nomeArquivo.toString();
	}
}
