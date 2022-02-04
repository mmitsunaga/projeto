package br.gov.go.tj.projudi.dt;

public class RedistribuicaoFisicoDt extends Dados {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6807541269038175022L;
	private String data; 
	private String hora; 
	private String serventia;
	private String forum; 
	private String comarca; 
	private String juiz; 
	private String repasse;
	private String natureza;
	private String autuacaoNumero; 
	private String autuacaoData;
	private String processoDependencia;
	private String mandadoOrdenatorio;

	public RedistribuicaoFisicoDt(String data, String hora, String serventia,
			String forum, String comarca, String juiz, String repasse,
			String natureza, String autuacaoNumero, String autuacaoData,
			String processoDependencia, String mandadoOrdenatorio) {
		 this.data = data; 
		 this.hora = hora; 
		 this.serventia= serventia;
		 this.forum = forum; 
		 this.comarca= comarca; 
		 this.juiz= juiz; 
		 this.repasse= repasse;
		 this.natureza= natureza;
		 this.autuacaoNumero= autuacaoNumero; 
		 this.autuacaoData=autuacaoData;
		 this.processoDependencia=processoDependencia;
		 this.mandadoOrdenatorio=mandadoOrdenatorio;

	}
	
	public RedistribuicaoFisicoDt() {
	 data = ""; 
	 hora = ""; 
	 serventia = "";
	 forum = ""; 
	 comarca = ""; 
	 juiz = ""; 
	 repasse = "";
	 natureza = "";
	 autuacaoNumero = ""; 
	 autuacaoData = "";
	 processoDependencia = "";
	 mandadoOrdenatorio = "";
	}
	@Override
	public void setId(String id) {
		

	}

	@Override
	public String getId() {
		
		return null;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getForum() {
		return forum;
	}

	public void setForum(String forum) {
		this.forum = forum;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}

	public String getJuiz() {
		return juiz;
	}

	public void setJuiz(String juiz) {
		this.juiz = juiz;
	}

	public String getRepasse() {
		return repasse;
	}

	public void setRepasse(String repasse) {
		this.repasse = repasse;
	}

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	public String getAutuacaoNumero() {
		return autuacaoNumero;
	}

	public void setAutuacaoNumero(String autuacaoNumero) {
		this.autuacaoNumero = autuacaoNumero;
	}

	public String getAutuacaoData() {
		return autuacaoData;
	}

	public void setAutuacaoData(String autuacaoData) {
		this.autuacaoData = autuacaoData;
	}

	public String getProcessoDependencia() {
		return processoDependencia;
	}

	public void setProcessoDependencia(String processoDependencia) {
		this.processoDependencia = processoDependencia;
	}

	public String getMandadoOrdenatorio() {
		return mandadoOrdenatorio;
	}

	public void setMandadoOrdenatorio(String mandadoOrdenatorio) {
		this.mandadoOrdenatorio = mandadoOrdenatorio;
	}

}
