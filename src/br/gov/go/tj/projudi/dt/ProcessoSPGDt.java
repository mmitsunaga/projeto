package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.TJDataHora;

public class ProcessoSPGDt extends Dados {

	private static final long serialVersionUID = 4564190214373263778L;

	private String isnProcessoFisico;
	private boolean ehProcessoFisico;
	private NumeroProcessoDt numeroProcessoCompletoDt;
	private String numeroProcessoSPG;
	private String nomeMagistradoResponsavel;
	private String numeroTCOInquerito;
	private long idServentia;
	private String serventia;
	private String classe;
	private String fase;
	private String classificador;
	private String nomeComarca;
	private String codComarca;
	private List<String> assuntos;
	private TJDataHora dataDistribuicao;
	private boolean ehSegredoDeJustica;
	private String status;
	private List<PoloSPGDt> polosAtivos;
	private List<PoloSPGDt> polosPassivos;
	private List<PoloSPGDt> outrosPolos;

	public ProcessoSPGDt() {
		this.setNumeroProcessoCompletoDt(new NumeroProcessoDt());
		this.assuntos = new ArrayList<String>();
		this.polosAtivos = new ArrayList<PoloSPGDt>();
		this.polosPassivos = new ArrayList<PoloSPGDt>();
		this.outrosPolos = new ArrayList<PoloSPGDt>();
	}

	@Override
	public void setId(String id) {
		if (id != null)
			this.isnProcessoFisico = id;
	}

	@Override
	public String getId() {
		return isnProcessoFisico;
	}

	public void setEhProcessoFisico(boolean ehProcessoFisico) {
		this.ehProcessoFisico = ehProcessoFisico;
	}

	public boolean isEhProcessoFisico() {
		return ehProcessoFisico;
	}

	public void setNumeroProcessoCompletoDt(
			NumeroProcessoDt numeroProcessoCompletoDt) {
		this.numeroProcessoCompletoDt = numeroProcessoCompletoDt;
	}

	public NumeroProcessoDt getNumeroProcessoCompletoDt() {
		return numeroProcessoCompletoDt;
	}

	public String getNumeroProcessoSPG() {
		return numeroProcessoSPG;
	}

	public void setNumeroProcessoSPG(String numeroProcessoSPG) {
		this.numeroProcessoSPG = numeroProcessoSPG;
	}

	public void setNomeMagistradoResponsavel(String nomeMagistradoResponsavel) {
		this.nomeMagistradoResponsavel = nomeMagistradoResponsavel;
	}

	public String getNomeMagistradoResponsavel() {
		if (nomeMagistradoResponsavel == null)
			return "";
		return nomeMagistradoResponsavel;
	}

	public void setNumeroTCOInquerito(String numeroTCOInquerito) {
		this.numeroTCOInquerito = numeroTCOInquerito;
	}

	public String getNumeroTCOInquerito() {
		return numeroTCOInquerito;
	}

	public void setIdServentia(long idServentia) {
		this.idServentia = idServentia;
	}

	public long getIdServentia() {
		return idServentia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getServentia() {
		if (serventia == null)
			return "";
		return serventia;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getClasse() {
		if (classe == null)
			return "";
		return classe;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	public String getFase() {
		if (fase == null)
			return "";
		return fase;
	}

	public void setClassificador(String classificador) {
		this.classificador = classificador;
	}

	public String getClassificador() {
		if (classificador == null)
			return "";
		return classificador;
	}

	public void setDataDistribuicao(TJDataHora dataDistribuicao) {
		this.dataDistribuicao = dataDistribuicao;
	}

	public TJDataHora getDataDistribuicao() {
		return dataDistribuicao;
	}

	public void setEhSegredoDeJustica(boolean ehSegredoDeJustica) {
		this.ehSegredoDeJustica = ehSegredoDeJustica;
	}

	public boolean isSegredoDeJustica() {
		return ehSegredoDeJustica;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		if (status == null)
			return "";
		return status;
	}

	public void adicioneAssunto(String assunto) {
		this.assuntos.add(assunto);
	}

	public List<String> getAssuntos() {
		return this.assuntos;
	}

	public void adicionePoloAtivo(PoloSPGDt poloDt) {
		this.polosAtivos.add(poloDt);
	}

	public List<PoloSPGDt> getPolosAtivos() {
		return this.polosAtivos;
	}

	public void adicionePoloPassivos(PoloSPGDt poloDt) {
		this.polosPassivos.add(poloDt);
	}

	public List<PoloSPGDt> getPolosPassivos() {
		return this.polosPassivos;
	}

	public void adicioneOutrosPolos(PoloSPGDt poloDt) {
		this.outrosPolos.add(poloDt);
	}

	public List<PoloSPGDt> getOutrosPolos() {
		return this.outrosPolos;
	}

	public String getNomeSistema() {
		if (this.ehProcessoFisico)
			return "SPG / SSG";
		return "Projudi";
	}

	public boolean possuiTCO() {
		return this.numeroTCOInquerito != null
				&& this.numeroTCOInquerito.trim().length() > 0;
	}

	public String getNomeComarca() {
		return nomeComarca;
	}

	public void setNomeComarca(String nomeComarca) {
		this.nomeComarca = nomeComarca;
	}

	public String getCodComarca() {
		return codComarca;
	}

	public void setCodComarca(String codComarca) {
		this.codComarca = codComarca;
	}
	
}
