package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AcaoPenalDt extends Dados {

/**
     * 
     */
    private static final long serialVersionUID = -3976346475021321003L;
    //	private String processoExecucaoNumeroCompleto;
	private String numeroAcaoPenal;
	private String varaOrigem;
	private String cidadeOrigem;
	
	private String dataAcordao;
	private String dataDistribuicao;
	private String dataPronuncia;
	private String dataSentenca;
	private String dataTransitoJulgado;
	private String dataTransitoJulgadoMP;
	private String dataDenuncia;
	private String dataAdmonitoria;
	
	private String localCumprimentoPena;
	private String penaExecucaoTipo;
	private String regimeExecucao;
	
	private String tempoTotalSursisAnos;
	private List listaCondenacoes;
	private List listaModalidades;

	public AcaoPenalDt() {
		limpar();
	}
	
	public void limpar(){
//		processoExecucaoNumeroCompleto = "";
		numeroAcaoPenal = "";
		varaOrigem = "";
		cidadeOrigem = "";
		
		dataAcordao = "";
		dataDistribuicao = "";
		dataPronuncia = "";
		dataSentenca = "";
		dataTransitoJulgado = "";
		dataTransitoJulgadoMP = "";
		dataDenuncia = "";
		dataAdmonitoria = "";
		
		localCumprimentoPena = "";
		penaExecucaoTipo = "";
		regimeExecucao = "";
		
		tempoTotalSursisAnos = "";
		
		listaCondenacoes = null;
		listaModalidades = null;
	}
	

	public List getListaCondenacoes() {	return listaCondenacoes;}
	public void setListaCondenacoes(List listaCondenacoes) {this.listaCondenacoes = listaCondenacoes;}
	public void addListaCondenacoes(CondenacaoExecucaoDt dt) {
		if (this.listaCondenacoes == null) this.listaCondenacoes = new ArrayList();
		this.listaCondenacoes.add(dt);
	}
	
	public String getCondenacoesToString() {
		if(this.listaCondenacoes.size() == 0)
			return "---";
		StringBuilder sb = new StringBuilder();
		sb.append("<table border='0' style=\"margin-left:40px\"><tbody>");
		Iterator it = this.listaCondenacoes.iterator();
		while (it.hasNext()) {
			CondenacaoExecucaoDt condenacao = (CondenacaoExecucaoDt) it.next();
			sb.append("<tr><td  width='55%'>Crime: "+ condenacao.getCrimeExecucao());
			sb.append("</td><td width='25%' align='center'>Tempo de Pena: "+ condenacao.getTempoPenaEmAnos() + " (a-m-d)");
			sb.append("</td><td width='20%' align='center'>Data do Fato: "+ condenacao.getDataFato() + "</td></tr>");
		}
		sb.append("</tbody></table>");
		return sb.toString();
	}
	
	public List getListaModalidades() {	return listaModalidades;}
	public void setListaModalidades(List listaModalidades) {this.listaModalidades = listaModalidades;}
	public void addListaModalidades(ProcessoEventoExecucaoDt dt) {
		if (this.listaModalidades == null) this.listaModalidades = new ArrayList();
		this.listaModalidades.add(dt);
	}
	
	public String getModalidadesToString() {
		if(this.listaModalidades == null || this.listaModalidades.size() == 0)
			return "---";
		StringBuilder sb = new StringBuilder();
		Iterator it = this.listaModalidades.iterator();
		while (it.hasNext()) {
			ProcessoEventoExecucaoDt modalidade = (ProcessoEventoExecucaoDt) it.next();
			sb.append(modalidade.getEventoRegimeDt().getRegimeExecucao() + ", ");
		}
		if (this.listaModalidades.size() >= 1) {
			int tamanho = sb.length();
			sb.delete(tamanho - 2, tamanho - 1);
		}
		return sb.toString();
	}
	
	public String getNumeroAcaoPenal() {if (this.numeroAcaoPenal.length() == 0) return "--"; else return numeroAcaoPenal;}
	public void setNumeroAcaoPenal(String numeroAcaoPenal) {	this.numeroAcaoPenal = numeroAcaoPenal;}

	public String getVaraOrigem() {	if(this.varaOrigem.length() == 0) return "--"; else return varaOrigem;	}
	public void setVaraOrigem(String varaOrigem) {		this.varaOrigem = varaOrigem;	}

	public String getCidadeOrigem() {	if(this.cidadeOrigem.length() == 0) return "--"; else return cidadeOrigem;	}
	public void setCidadeOrigem(String cidadeOrigem) {		this.cidadeOrigem = cidadeOrigem;	}

	public String getDataAcordao() {	if(this.dataAcordao.length() == 0) return "--"; else return dataAcordao;	}
	public void setDataAcordao(String dataAcordao) {		this.dataAcordao = dataAcordao;	}

	public String getDataDistribuicao() {	if(this.dataDistribuicao.length() == 0) return "--"; else return dataDistribuicao;	}
	public void setDataDistribuicao(String dataDistribuicao) {		this.dataDistribuicao = dataDistribuicao;	}

	public String getDataPronuncia() {	if(this.dataPronuncia.length() == 0) return "--"; else return dataPronuncia;	}
	public void setDataPronuncia(String dataPronuncia) {		this.dataPronuncia = dataPronuncia;	}

	public String getDataSentenca() {	if(this.dataSentenca.length() == 0) return "--"; else return dataSentenca;	}
	public void setDataSentenca(String dataSentenca) {		this.dataSentenca = dataSentenca;	}

	public String getDataTransitoJulgado() {	if(this.dataTransitoJulgado.length() == 0) return "--"; else return dataTransitoJulgado;	}
	public void setDataTransitoJulgado(String dataTransitoJulgado) {		this.dataTransitoJulgado = dataTransitoJulgado;	}

	public String getDataTransitoJulgadoMP() {	if(this.dataTransitoJulgadoMP.length() == 0) return "--"; else return dataTransitoJulgadoMP;	}
	public void setDataTransitoJulgadoMP(String dataTransitoJulgadoMP) {		this.dataTransitoJulgadoMP = dataTransitoJulgadoMP;	}
	
	public String getDataDenuncia() {if(this.dataDenuncia.length() == 0) return "--"; else return dataDenuncia;	}
	public void setDataDenuncia(String dataDenuncia) {		this.dataDenuncia = dataDenuncia;	}

	public String getDataAdmonitoria() {	if(this.dataAdmonitoria.length() == 0) return "--"; else return dataAdmonitoria;	}
	public void setDataAdmonitoria(String dataAdmonitoria) {	this.dataAdmonitoria = dataAdmonitoria;	}

	public String getLocalCumprimentoPena() {	if(this.localCumprimentoPena.length() == 0) return "--"; else return localCumprimentoPena;	}
	public void setLocalCumprimentoPena(String localCumprimentoPena) {		this.localCumprimentoPena = localCumprimentoPena;	}

	public String getPenaExecucaoTipo() {	if(this.penaExecucaoTipo.length() == 0) return "--"; else return penaExecucaoTipo;	}
	public void setPenaExecucaoTipo(String penaExecucaoTipo) {		this.penaExecucaoTipo = penaExecucaoTipo;	}

	public String getRegimeExecucao() {	if(this.regimeExecucao.length() == 0) return "--"; else return regimeExecucao;	}
	public void setRegimeExecucao(String regimeExecucao) {		this.regimeExecucao = regimeExecucao;	}

	public String getTempoTotalSursisAnos() {	if(this.tempoTotalSursisAnos.length() == 0) return "--"; else return this.tempoTotalSursisAnos;	}
	public void setTempoTotalSursisAnos(String tempoTotalSursisAnos) {		this.tempoTotalSursisAnos = tempoTotalSursisAnos;	}
	
	@Override
	public String getId() {
		
		return null;
	}

	@Override
	public void setId(String id) {
		
		
	}


	public String getDescricao() {
		
		return this.numeroAcaoPenal;
	}
}
