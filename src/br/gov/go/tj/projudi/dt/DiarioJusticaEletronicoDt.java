package br.gov.go.tj.projudi.dt;

import java.io.Serializable;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ValidacaoUtil;

/**
 * POJO
 * @author mmitsunaga
 *
 */
public class DiarioJusticaEletronicoDt implements Serializable {
	
	private static final long serialVersionUID = 335385724840775981L;

	private String processoId;
    
    private String processoNumero;
    
    private String digitoVerificador;
    
    private String forumCodigo;
    
    private String ano;
    
    private String segredoJustica;
    
    private String serventia;
    
    private String classeCnjId;
    
    private String classeCnjRecursoId;
    
    private String pendId;
    
    private String pendCodigo;
    
    private String moviId;
    
    private String moviArqId;
    
    private String dataInicio;
    
    private String dataFim;
    
    private String parteId;
    
    private String nomeParte;
    
    private String cpfCnpjParte;
    
    private String poloParte;
    
    private String nomeAdvogado;
    
    private String oabAdvogado;
    
    private String ufOabAdvogado;
    
    private String arqId;
    
    private String tipoArq;
    
    private String arqTipoCodigo;
    
    private String votoArqId;
    
    private String votoMoviArqId;
    
    public DiarioJusticaEletronicoDt(){
    	
    }

	public String getProcessoId() {
		return processoId;
	}

	public void setProcessoId(String processoId) {
		this.processoId = processoId;
	}

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}

	public String getDigitoVerificador() {
		return digitoVerificador;
	}

	public void setDigitoVerificador(String digitoVerificador) {
		this.digitoVerificador = digitoVerificador;
	}

	public String getForumCodigo() {
		return forumCodigo;
	}

	public void setForumCodigo(String forumCodigo) {
		this.forumCodigo = forumCodigo;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSegredoJustica() {
		return segredoJustica;
	}

	public void setSegredoJustica(String segredoJustica) {
		this.segredoJustica = segredoJustica;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getClasseCnjId() {
		return classeCnjId;
	}

	public void setClasseCnjId(String classeCnjId) {
		this.classeCnjId = classeCnjId;
	}

	public String getClasseCnjRecursoId() {
		return classeCnjRecursoId;
	}

	public void setClasseCnjRecursoId(String classeCnjRecursoId) {
		this.classeCnjRecursoId = classeCnjRecursoId;
	}

	public String getPendId() {
		return pendId;
	}

	public void setPendId(String pendId) {
		this.pendId = pendId;
	}

	public String getPendCodigo() {
		return pendCodigo;
	}

	public void setPendCodigo(String pendCodigo) {
		this.pendCodigo = pendCodigo;
	}

	public String getMoviId() {
		return moviId;
	}

	public void setMoviId(String moviId) {
		this.moviId = moviId;
	}
	
	public String getMoviArqId() {
		return moviArqId;
	}

	public void setMoviArqId(String moviArqId) {
		this.moviArqId = moviArqId;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}
	
	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public String getParteId() {
		return parteId;
	}

	public void setParteId(String parteId) {
		this.parteId = parteId;
	}

	public String getNomeParte() {		
		return isSegredoJustica() ? Funcoes.iniciaisNomeComSeparador(nomeParte, ".") : nomeParte;
	}
	
	public void setNomeParte(String nomeParte) {
		this.nomeParte = nomeParte;
	}

	public String getCpfCnpjParte() {
		return cpfCnpjParte;
	}

	public void setCpfCnpjParte(String cpfCnpjParte) {
		this.cpfCnpjParte = cpfCnpjParte;
	}

	public String getPoloParte() {
		return poloParte;
	}

	public void setPoloParte(String poloParte) {
		this.poloParte = poloParte;
	}

	public String getNomeAdvogado() {
		return nomeAdvogado;
	}

	public void setNomeAdvogado(String nomeAdvogado) {
		this.nomeAdvogado = nomeAdvogado;
	}

	public String getOabAdvogado() {
		return oabAdvogado;
	}

	public void setOabAdvogado(String oabAdvogado) {
		this.oabAdvogado = oabAdvogado;
	}

	public String getUfOabAdvogado() {
		return ufOabAdvogado;
	}

	public void setUfOabAdvogado(String ufOabAdvogado) {
		this.ufOabAdvogado = ufOabAdvogado;
	}

	public String getArqId() {
		return arqId;
	}

	public void setArqId(String arqId) {
		this.arqId = arqId;
	}

	public String getTipoArq() {
		return tipoArq;
	}

	public void setTipoArq(String tipoArq) {
		this.tipoArq = tipoArq;
	}

	public String getArqTipoCodigo() {
		return arqTipoCodigo;
	}

	public void setArqTipoCodigo(String arqTipoCodigo) {
		this.arqTipoCodigo = arqTipoCodigo;
	}

	public String getVotoArqId() {
		return votoArqId;
	}

	public void setVotoArqId(String votoArqId) {
		this.votoArqId = votoArqId;
	}
	
	public String getVotoMoviArqId() {
		return votoMoviArqId;
	}

	public void setVotoMoviArqId(String votoMoviArqId) {
		this.votoMoviArqId = votoMoviArqId;
	}

	public boolean isSegredoJustica(){
		return ValidacaoUtil.isNaoVazio(this.segredoJustica) && (this.segredoJustica.equals("1"));
	}
	
}
