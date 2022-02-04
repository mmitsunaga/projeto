package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

public class ProcessoSSGDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6931997420200075464L;

	private String isnProcessoFisico;
	private boolean ehProcessoFisico;
	private NumeroProcessoDt numeroProcessoCompletoDt;
	private String numeroProcessoSSG;
	private String nomeComarca;
	private String codComarca;
	private List<PoloSPGDt> polosAtivos;
	private List<PoloSPGDt> polosPassivos;
	private List<PoloSPGDt> outrosPolos;

	public ProcessoSSGDt() {
		this.setNumeroProcessoCompletoDt(new NumeroProcessoDt());
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

	public String getNumeroProcessoSSG() {
		return numeroProcessoSSG;
	}

	public void setNumeroProcessoSSG(String numeroProcessoSSG) {
		this.numeroProcessoSSG = numeroProcessoSSG;
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
			return "SSG";
		return "Projudi";
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
