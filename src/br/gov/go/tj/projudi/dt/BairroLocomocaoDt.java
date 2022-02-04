package br.gov.go.tj.projudi.dt;

public class BairroLocomocaoDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7077279781848065023L;
	private BairroDt Bairro;
	private OficialSPGDt OficialSPG;

	public BairroDt getBairroDt() {
		return Bairro;
	}

	public void setBairroDt(BairroDt bairro) {
		if (bairro != null) {
			this.Bairro = bairro;
		}
	}

	public OficialSPGDt getOficialSPGDt() {
		return this.OficialSPG;
	}

	public void setOficialSPGDt(OficialSPGDt OficialSPG) {
		this.OficialSPG = OficialSPG;
	}
	
	public void copiar(BairroLocomocaoDt objeto){
		if (objeto==null) return;		
		Bairro = objeto.getBairroDt();
		OficialSPG = objeto.getOficialSPGDt();
	}

	public void limpar(){
		super.limpar();
		Bairro = null;
		OficialSPG = null;		
	}

	@Override
	public void setId(String id) {
				
	}

	@Override
	public String getId() {
		if (this.Bairro == null || this.Bairro.getId() == null) return "";
		return this.Bairro.getId();
	}
}