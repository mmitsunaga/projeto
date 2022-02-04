package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;


public class ProcessoAntecedenteCriminalDt extends ProcessoCertidaoDt implements Comparable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1826026927546753507L;
	
	
	private String processoResponsavel;
	private String dataTransitoJulgado;
	private String dataFato;
	private String fase;
	private String sentenca;
	private String comarca;
	private List beneficio;
	private String sistema;
	private String dataBaixa;
	private String motivoBaixa;
	private String dataRecebimentoDenuncia;
	private String nomeVitima;
	private String processoArquivamentoTipo;

	private List infracao;


	private String status;

	
	public String getDataBaixa() {
		return dataBaixa;
	}

	public void setDataBaixa(String dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	public String getMotivoBaixa() {
		return motivoBaixa;
	}

	public void setMotivoBaixa(String motivoBaixa) {
		this.motivoBaixa = motivoBaixa;
	}

	public void addBeneficio(BeneficioCertidaoAntecedenteCriminalDt ben) {
			this.beneficio.add(ben);
	}

	/**
	 * @return the assunto
	 */
	public List getBeneficioList() {
		return beneficio;
	}
	
	/**
	 * Método para gerar a string com todos os benefícios do processo que será listado na certidão.
	 * @return string
	 * @author hmgodinho
	 */
	public String getBeneficiosCertidao(){
		String beneficiosCertidao = "";
		if(!beneficio.isEmpty()) {
			for (int i = 0; i < beneficio.size(); i++) {
				BeneficioCertidaoAntecedenteCriminalDt beneficioCertidao = (BeneficioCertidaoAntecedenteCriminalDt)beneficio.get(i);
				if(beneficiosCertidao.length() > 0) {
					beneficiosCertidao += "\n";
				}
				beneficiosCertidao += "Tipo Benefício: " + beneficioCertidao.getBeneficio() + " - Início: " + beneficioCertidao.getData_beneficio_inicio() + " - Fim: " + beneficioCertidao.getData_beneficio_fim();
			}
		} else {
			return "";
		}
		return beneficiosCertidao;
		
	}

	
	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}
	
	public int compareTo(Object arg0) {
		if(this.getProcessoNumero().matches("")){
			int numProc = Funcoes.StringToInt(processoNumero);
			int numProcAux = Funcoes.StringToInt(((ProcessoCertidaoPositivaNegativaDt) arg0).getProcessoNumero());
			return numProc - numProcAux;
			
		} else {
				
		int numProc = Funcoes.StringToInt(processoNumero);
		int numProcAux = Funcoes.StringToInt(((ProcessoCertidaoPositivaNegativaDt) arg0).getProcessoNumero());
		return numProc - numProcAux;
		}
	}


	public ProcessoAntecedenteCriminalDt() {
		super();
		this.promoventeNome = new ArrayList(5);
		this.assunto = new ArrayList(5);
		this.beneficio = new ArrayList(5);
		this.infracao = new ArrayList(5);
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}

	/**
	 * @return the processoResponsavel
	 */
	public String getProcessoResponsavel() {
		return processoResponsavel;
	}

	/**
	 * @param processoResponsavel the processoResponsavel to set
	 */
	public void setProcessoResponsavel(String processoResponsavel) {
		this.processoResponsavel = processoResponsavel;
	}

	/**
	 * @return the dataTransitoJulgado
	 */
	public String getDataTransitoJulgado() {
		return dataTransitoJulgado;
	}

	/**
	 * @param dataTransitoJulgado the dataTransitoJulgado to set
	 */
	public void setDataTransitoJulgado(String dataTransitoJulgado) {
		this.dataTransitoJulgado = dataTransitoJulgado;
	}

	/**
	 * @return the dataFato
	 */
	public String getDataFato() {
		return dataFato;
	}

	/**
	 * @param dataFato the dataFato to set
	 */
	public void setDataFato(String dataFato) {
		this.dataFato = dataFato;
	}

	/**
	 * @return the fase
	 */
	public String getFase() {
		return fase;
	}

	/**
	 * @param fase the fase to set
	 */
	public void setFase(String fase) {
		this.fase = fase;
	}

	/**
	 * @return the sentenca
	 */
	public String getSentenca() {
		return sentenca;
	}

	/**
	 * @param sentenca the sentenca to set
	 */
	public void setSentenca(String sentenca) {
		this.sentenca = sentenca;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {		
	}

	public int getBuscadaProcessoParteTipo() {
		return 0;
	}

	

	public void setSistema(String string) {
		this.sistema = string;		
	}
	
	public String getSistema() {
		return this.sistema;
	}

	public List getAssunto() {
		return assunto;
	}

	public void setAssunto(List assunto) {
		this.assunto = assunto;
	}
	
	public String getInfracaoCertidao() {
		if(infracao.size() == 0)
			return "";
		StringBuilder sb = new StringBuilder();
		Iterator it = infracao.iterator();
		while (it.hasNext()) {
			String infr = (String) it.next();
			if(infr.contains("->")){
				infr = infr.replace("->", ", ");
			}
			sb.append(infr + "; ");
		}
		if (infracao.size() >= 1) {
		int tamanho = sb.length();
		sb.delete(tamanho - 2, tamanho - 1);
		}
		return sb.toString();
	}

	public void addInfracao(String infracao) {
		if (!this.infracao.contains(infracao))
			this.infracao.add(infracao);
	}

	public void setStatus(String string) {
		this.status = string;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public String getDataRecebimentoDenuncia() {
		return dataRecebimentoDenuncia;
	}

	public void setDataRecebimentoDenuncia(String dataRecebimentoDenuncia) {
		this.dataRecebimentoDenuncia = dataRecebimentoDenuncia;
	}

	public String getNomeVitima() {
		return nomeVitima;
	}

	public void setNomeVitima(String nomeVitima) {
		this.nomeVitima = nomeVitima;
	}

	public String getProcessoArquivamentoTipo() {
		return processoArquivamentoTipo;
	}

	public void setProcessoArquivamentoTipo(String processoArquivamentoTipo) {
		this.processoArquivamentoTipo = processoArquivamentoTipo;
	}
	
	public boolean isProcessoFisico() {
		return getSistema().equals("SPG") || getSistema().equals("SSG");
	}
}
