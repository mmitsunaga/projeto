package br.gov.go.tj.projudi.dt;

import java.util.List;

//---------------------------------------------------------
public class ProcessoParteBeneficioDt extends ProcessoParteBeneficioDtGen {

	/**
     * 
     */
    private static final long serialVersionUID = 3225876043645149097L;

    public static final int CodigoPermissao = 410;

	/**
	 * Foram criadas essas variáveis para auxiliar na manipulação de dados referentes ao processo, pois não compensava ter um atributo 
	 * do tipo ProcessoDt, pois a maioria dos seus atributos não seriam utilizados
	 */
	private String id_Processo;
	private String processoNumero;
	private String id_Serventia;
	private List listaPartesPromoventes;
	private List listaPartesPromovidas;
	private List listaPartesComBeneficio;

	private String[] partesBeneficio; //Lista de partes marcadas para benefício	

	//Variável para auxiliar na consulta de benefícios
	private String cpfParte;

	public void limpar() {
		super.limpar();
		id_Processo = "";
		processoNumero = "";
		cpfParte = "";
		id_Serventia = "";
		listaPartesPromoventes = null;
		listaPartesPromovidas = null;
		partesBeneficio = null;
		listaPartesComBeneficio = null;
	}

	public String getId_Processo() {
		return id_Processo;
	}

	public void setId_Processo(String id_Processo) {
		if (id_Processo != null) this.id_Processo = id_Processo;
	}

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String numeroProcesso) {
		if (numeroProcesso != null) this.processoNumero = numeroProcesso;
	}

	public List getListaPartesPromoventes() {
		return listaPartesPromoventes;
	}

	public void setListaPartesPromoventes(List listaPartesPromoventes) {
		this.listaPartesPromoventes = listaPartesPromoventes;
	}

	public List getListaPartesPromovidas() {
		return listaPartesPromovidas;
	}

	public void setListaPartesPromovidas(List listaPartesPromovidas) {
		this.listaPartesPromovidas = listaPartesPromovidas;
	}
	
	public List getListaPartesComBeneficio() {
		return listaPartesComBeneficio;
	}

	public void setListaPartesComBeneficio(List listaPartesComBeneficio) {
		this.listaPartesComBeneficio = listaPartesComBeneficio;
	}

	public String[] getPartesBeneficio() {
		return partesBeneficio;
	}

	public void setPartesBeneficio(String[] partesBeneficio) {
		if (partesBeneficio != null) this.partesBeneficio = partesBeneficio;
	}

	public String getCpfParte() {
		return cpfParte;
	}

	public void setCpfParte(String cpfParte) {
		if (cpfParte != null) this.cpfParte = cpfParte;
	}

	public String getId_Serventia() {
		return id_Serventia;
	}

	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null) this.id_Serventia = id_Serventia;
	}

	public String toJson() {
		return "{\"Id\":\"" + getId() 
		+ "\",\"ProcessoBeneficio\":\"" + getProcessoBeneficio() 
		+ "\",\"DataInicial\":\"" + getDataInicial() 
		+ "\",\"DataFinal\":\"" + getDataFinal() 
		+ "\",\"Id_Processo\":\"" + getId_Processo() 
		+ "\",\"Id_ProcessoBeneficio\":\"" + getId_ProcessoBeneficio()
		+ "\",\"Id_ProcessoParte\":\"" + getId_ProcessoParte() 		
		+ "\",\"Id_Serventia\":\"" + getId_Serventia() 		  		
		+ "\",\"Nome\":\"" + getNome()
		+ "\",\"ProcessoNumero\":\"" + getProcessoNumero() 				 
		+ "\"}";	
	}

}
