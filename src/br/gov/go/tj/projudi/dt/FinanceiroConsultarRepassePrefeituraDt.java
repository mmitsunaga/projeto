package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.TJDataHora;

public class FinanceiroConsultarRepassePrefeituraDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5261899010396333204L;

	public static final int CodigoPermissao = 821;
	
	private String idServentia;
	private String idGuiaModelo;
	private TJDataHora dataInicio;
	private TJDataHora dataFim;
	private RelatorioRepassePrefeituraDt relatorio;
	private String tipoFiltroData;
	private String tipoFiltroDetalhe;
		
	@Override
	public String getId() {
		return idServentia;
	}

	@Override
	public void setId(String id) {
		this.idServentia = id;
	}
	
	public FinanceiroConsultarRepassePrefeituraDt(){
		limpar();
	}
	
	public void limpar(){
		dataInicio = new TJDataHora();
		dataInicio.adicioneDia(-10);		
		dataInicio.atualizePrimeiraHoraDia();	
		
		dataFim = new TJDataHora();
		dataFim.adicioneDia(-10);
		dataFim.atualizeUltimaHoraDia();	
		tipoFiltroData = "1";
		tipoFiltroDetalhe = "1";
	}

	public TJDataHora getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) throws Exception{
		if (dataInicio == null || dataInicio.trim().equalsIgnoreCase("")) return;
		this.dataInicio = new TJDataHora();
		this.dataInicio.setDataddMMaaaa(dataInicio);
		this.dataInicio.atualizePrimeiraHoraDia();
	}
	
	public void setDataInicio(TJDataHora dataInicio){
		this.dataInicio = dataInicio;
		if (dataInicio == null) return;
		this.dataInicio.atualizePrimeiraHoraDia();
	}
	
	public TJDataHora getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) throws Exception{
		if (dataFim == null || dataFim.trim().equalsIgnoreCase("")) return;
		this.dataFim = new TJDataHora();
		this.dataFim.setDataddMMaaaa(dataFim);
		this.dataFim.atualizePrimeiraHoraDia();
	}
	
	public void setDataFim(TJDataHora dataFim){
		this.dataFim = dataFim;
		if (dataFim == null) return;
		this.dataFim.atualizePrimeiraHoraDia();
	}
	
	public boolean possuiRelatorio()
	{
		return this.relatorio != null;
	}
	
	public RelatorioRepassePrefeituraDt getRelatorio() {
		return relatorio;
	}

	public void setRelatorio(RelatorioRepassePrefeituraDt relatorio) {
		this.relatorio = relatorio;
	}
	
	public String getTipoFiltroData() {
		return tipoFiltroData;
	}

	public void setTipoFiltroData(String tipoFiltroData) {
		this.tipoFiltroData = tipoFiltroData;
	}
	
	public String getTipoFiltroDetalhe() {
		return tipoFiltroDetalhe;
	}

	public void setTipoFiltroDetalhe(String tipoFiltroDetalhe) {
		this.tipoFiltroDetalhe = tipoFiltroDetalhe;
	}

	public String getIdGuiaModelo() {
		return idGuiaModelo;
	}

	public void setIdGuiaModelo(String idGuiaModelo) {
		this.idGuiaModelo = idGuiaModelo;
	}
}
