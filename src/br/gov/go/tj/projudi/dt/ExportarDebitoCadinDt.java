package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class ExportarDebitoCadinDt extends Dados{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6926282185939890190L;

	public static final int CodigoPermissao = 927;
	
	public enum EnumTipoExportacao
	{
		TODOS_NAO_PROCESSADOS,
		REPROCESSAMENTO_POR_LOTE,
		REPROCESSAMENTO_POR_DATA
	}
	
	private String idExportarDebitoCadin;
	private String tipoExportacao;
	private String numeroDoLote;
	private String dataExportacao;
	private String nomeArquivo;
	private String conteudoArquivo;
	private List<ProcessoParteDebitoCadinDt> registros;
	private String conteudoArquivoInconsistencias;
	private String nomeArquivoInconsistencias;
	
	@Override
	public void setId(String id) {
		this.idExportarDebitoCadin = id;		
	}

	@Override
	public String getId() {
		return this.idExportarDebitoCadin;
	}
	
	public String getTipoExportacao() {
		return tipoExportacao;
	}

	public void setTipoExportacao(String tipoExportacao) {
		if (tipoExportacao != null) {
			this.tipoExportacao = tipoExportacao;	
		}		
	}
	
	public boolean isTipoTodosNaoProcessados() {
		return EnumTipoExportacao.TODOS_NAO_PROCESSADOS.ordinal() == Funcoes.StringToInt(tipoExportacao);
	}
	
	public boolean isReprocessamentoPorLote() {
		return EnumTipoExportacao.REPROCESSAMENTO_POR_LOTE.ordinal() == Funcoes.StringToInt(tipoExportacao);
	}
	
	public boolean isReprocessamentoPorData() {
		return EnumTipoExportacao.REPROCESSAMENTO_POR_DATA.ordinal() == Funcoes.StringToInt(tipoExportacao);
	}
	
	public String getNumeroDoLote() {
		return numeroDoLote;
	}

	public void setNumeroDoLote(String numeroDoLote) {
		if (numeroDoLote != null) this.numeroDoLote = numeroDoLote;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}
	
	public String getDataExportacao() {
		return dataExportacao;
	}

	public void setDataExportacao(String dataExportacao) {
		if (dataExportacao != null) this.dataExportacao = dataExportacao;
	}

	public void setNomeArquivo(String nomeArquivo) {
		if (nomeArquivo != null) {
			this.nomeArquivo = nomeArquivo;	
		}		
	}
	
	public String getConteudoArquivo() {
		return conteudoArquivo;
	}

	public void setConteudoArquivo(String conteudoArquivo) {
		if (conteudoArquivo != null) {
			this.conteudoArquivo = conteudoArquivo;	
		}		
	}
	
	public List<ProcessoParteDebitoCadinDt> getRegistros() {
		return registros;
	}

	public void setRegistros(List<ProcessoParteDebitoCadinDt> registros) {
		if (registros != null) {
			this.registros = registros;	
		}		
	}
	
	public ExportarDebitoCadinDt() {
		this.registros = new ArrayList<ProcessoParteDebitoCadinDt>();
		limpar();		
	}

	public void copiar(ExportarDebitoCadinDt objeto){
		idExportarDebitoCadin = objeto.getId();
		nomeArquivo = objeto.getNomeArquivo();
		conteudoArquivo = objeto.getConteudoArquivo();	
		setRegistros(objeto.getRegistros());
		numeroDoLote = objeto.getNumeroDoLote();
		dataExportacao = objeto.getDataExportacao();
	}

	public void limpar(){
		idExportarDebitoCadin="";
		tipoExportacao = String.valueOf(EnumTipoExportacao.TODOS_NAO_PROCESSADOS.ordinal());
		numeroDoLote = "";
		dataExportacao = "";
		nomeArquivo="";
		conteudoArquivo="";
		registros.clear();		
	}
	
	public String getNomeArquivoInconsistencias() {
		return nomeArquivoInconsistencias;
	}

	public void setNomeArquivoInconsistencias(String nomeArquivoInconsistencias) {
		this.nomeArquivoInconsistencias = nomeArquivoInconsistencias;
	}
	
	public String getConteudoArquivoInconsistencias() {
		return conteudoArquivoInconsistencias;
	}

	public void setConteudoArquivoInconsistencias(String conteudoArquivoInconsistencias) {
		this.conteudoArquivoInconsistencias = conteudoArquivoInconsistencias;
	}

	public String getPropriedades(){
		return "[IdImportarRetornoCadin:" + idExportarDebitoCadin + ";NomeArquivonomeArquivo:" + nomeArquivo + ";ConteudoArquivo:" + conteudoArquivo + ";QuantidadeDeRegistros:" + (this.registros == null ? "0" : this.registros.size()) + ";NumeroDoLote:" + numeroDoLote + ";DataExportacao:" + dataExportacao + "]";		
	}				
} 
