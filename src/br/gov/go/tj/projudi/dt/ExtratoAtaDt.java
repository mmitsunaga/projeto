package br.gov.go.tj.projudi.dt;

import java.util.HashMap;
import java.util.List;

public class ExtratoAtaDt {
	private String serventia;
	private ProcessoDt processoDt;
	private String nomeAnalista;
	private String turmaJulgadora;
	private String dataSessao;
	private String horaSessao;
	private String procurador;
	private String nomePresidenteSessao;
	private String nomeRelator;
	private String nomeRedator; // jvosantos - 03/06/2019 17:17 - Adicionar nome do redator na Ata
	private String decisao;
	private String complementoDecisao;
	private HashMap<Integer, List<String>> poloAtivo; // jvosantos - 12/06/2019 11:22 - Mudança para separar polo ativo por ordem de parte
	private HashMap<Integer, List<String>> poloPassivo;  // jvosantos - 12/06/2019 11:22 - Mudança para separar polo passivo por ordem de parte
	private List<String> impedidos;
	private List<String> suspeitos;
	private List<String> listaAcompanhaRelator;
	private List<String> listaDivergeRelator;
	private List<String> listaNaoVotaram; // jvosantos - 22/08/2019 11:24 - Adicionar lista de votantes que não votaram nas serventias especiais
	private List<String> listaPedidoVista; // alsqueiroz - 31/10/2019 09:49 - Adicionar lista de votantes de pedido de vista para a funcionalidade extrato do julgamento iniciado
	private String data;
	private String classe;
	private boolean votoVencido; // jvosantos - 03/06/2019 17:17 - Adicionar variavel para saber se é voto vencido
	private Integer maiorOrdem; // jvosantos - 12/06/2019 12:09 - Adicionar variavel para saber qual é a maior ordem das partes
	private boolean temRecSecPoloAtivo; // jvosantos - 12/06/2019 13:52 - Adicionar variavel para saber se tem rec. sec. no polo ativo
	private boolean temRecSecPoloPassivo; // jvosantos - 12/06/2019 13:52 - Adicionar variavel para saber se tem rec. sec. no polo passivo
	
	public ExtratoAtaDt() {
		serventia = "";
		nomeAnalista = "";
		turmaJulgadora = "";
		dataSessao = "";
		procurador = "";
		nomePresidenteSessao = "";
		decisao = "";
		data = "";
		nomeRedator = "";
		setVotoVencido(false);
		setTemRecSecPoloAtivo(false);
		setTemRecSecPoloPassivo(false);
	}

	public List<String> getSuspeitos() {
		return suspeitos;
	}

	public void setSuspeitos(List<String> suspeitos) {
		this.suspeitos = suspeitos;
	}

	public String getServentia() {
		return serventia;
	}
	public void setServentia(String serventia) {
		this.serventia = serventia;
	}
	public ProcessoDt getProcessoDt() {
		return processoDt;
	}
	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}
	public String getNomeAnalista() {
		return nomeAnalista;
	}
	public void setNomeAnalista(String nomeAnalista) {
		this.nomeAnalista = nomeAnalista;
	}
	public String getTurmaJulgadora() {
		return turmaJulgadora;
	}
	public void setTurmaJulgadora(String turmaJulgadora) {
		this.turmaJulgadora = turmaJulgadora;
	}
	public String getDataSessao() {
		return dataSessao;
	}
	public void setDataSessao(String dataSessao) {
		this.dataSessao = dataSessao;
	}
	public String getProcurador() {
		return procurador;
	}
	public void setProcurador(String procurador) {
		this.procurador = procurador;
	}
	public String getNomePresidenteSessao() {
		return nomePresidenteSessao;
	}
	public void setNomePresidenteSessao(String nomePresidenteSessao) {
		this.nomePresidenteSessao = nomePresidenteSessao;
	}
	public String getDecisao() {
		return decisao;
	}
	public void setDecisao(String decisao) {
		this.decisao = decisao;
	}
	public List<String> getImpedidos() {
		return impedidos;
	}
	public void setImpedidos(List<String> impedidos) {
		this.impedidos = impedidos;
	}
	public List<String> getListaAcompanhaRelator() {
		return listaAcompanhaRelator;
	}
	public void setListaAcompanhaRelator(List<String> listaAcompanhaRelator) {
		this.listaAcompanhaRelator = listaAcompanhaRelator;
	}
	public List<String> getListaDivergeRelator() {
		return listaDivergeRelator;
	}
	public void setListaDivergeRelator(List<String> listaDivergeRelator) {
		this.listaDivergeRelator = listaDivergeRelator;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}

	public String getNomeRelator() {
		return nomeRelator;
	}

	public void setNomeRelator(String nomeRelator) {
		this.nomeRelator = nomeRelator;
	}

	public String getHoraSessao() {
		return horaSessao;
	}

	public void setHoraSessao(String horaSessao) {
		this.horaSessao = horaSessao;
	}

	public String getComplementoDecisao() {
		return complementoDecisao;
	}

	public void setComplementoDecisao(String complementoDecisao) {
		this.complementoDecisao = complementoDecisao;
	}

	 // jvosantos - 12/06/2019 11:22 - Mudança para separar polo ativo por ordem de parte
	public HashMap<Integer, List<String>> getPoloAtivo() {
		return poloAtivo;
	}

	 // jvosantos - 12/06/2019 11:22 - Mudança para separar polo ativo por ordem de parte
	public void setPoloAtivo(HashMap<Integer, List<String>> poloAtivo) {
		this.poloAtivo = poloAtivo;
	}

	// jvosantos - 12/06/2019 11:22 - Mudança para separar polo passivo por ordem de parte
	public HashMap<Integer, List<String>> getPoloPassivo() {
		return poloPassivo;
	}

	// jvosantos - 12/06/2019 11:22 - Mudança para separar polo passivo por ordem de parte
	public void setPoloPassivo(HashMap<Integer, List<String>> poloPassivo) {
		this.poloPassivo = poloPassivo;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	// jvosantos - 03/06/2019 17:17 - Adicionar nome do redator na Ata
	public String getNomeRedator() {
		return nomeRedator;
	}

	public void setNomeRedator(String nomeRedator) {
		this.nomeRedator = nomeRedator;
	}

	// jvosantos - 03/06/2019 17:17 - Adicionar variavel para saber se é voto vencido
	public boolean isVotoVencido() {
		return votoVencido;
	}

	public void setVotoVencido(boolean votoVencido) {
		this.votoVencido = votoVencido;
	}
	
	// jvosantos - 12/06/2019 12:09 - Adicionar variavel para saber qual é a maior ordem das partes
	public Integer getMaiorOrdem() {
		return maiorOrdem;
	}
	
	// jvosantos - 12/06/2019 12:09 - Adicionar variavel para saber qual é a maior ordem das partes
	public void setMaiorOrdem(Integer maiorOrdem) {
		this.maiorOrdem = maiorOrdem;
	}

	// jvosantos - 12/06/2019 13:52 - Adicionar variavel para saber se tem rec. sec. no polo ativo
	public boolean isTemRecSecPoloAtivo() {
		return temRecSecPoloAtivo;
	}

	// jvosantos - 12/06/2019 13:52 - Adicionar variavel para saber se tem rec. sec. no polo ativo
	public void setTemRecSecPoloAtivo(boolean temRecSecPoloAtivo) {
		this.temRecSecPoloAtivo = temRecSecPoloAtivo;
	}

	// jvosantos - 12/06/2019 13:52 - Adicionar variavel para saber se tem rec. sec. no polo passivo
	public boolean isTemRecSecPoloPassivo() {
		return temRecSecPoloPassivo;
	}

	// jvosantos - 12/06/2019 13:52 - Adicionar variavel para saber se tem rec. sec. no polo passivo
	public void setTemRecSecPoloPassivo(boolean temRecSecPoloPassivo) {
		this.temRecSecPoloPassivo = temRecSecPoloPassivo;
	}

	public List<String> getListaNaoVotaram() {
		return listaNaoVotaram;
	}

	public void setListaNaoVotaram(List<String> listaNaoVotaram) {
		this.listaNaoVotaram = listaNaoVotaram;
	}

	public List<String> getListaPedidoVista() {
		return listaPedidoVista;
	}

	public void setListaPedidoVista(List<String> listaPedidoVista) {
		this.listaPedidoVista = listaPedidoVista;
	}

}
