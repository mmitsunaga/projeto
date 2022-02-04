package br.gov.go.tj.projudi.dt.relatorios;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

public class RelatorioExecpenwebDt  extends Dados {

	private static final long serialVersionUID = 7063417657637762561L;
//	public static final int CodigoPermissao=806;
	public static final int CodigoPermissao=381;	
	
	private int quantidade;
	private String serventia;
	private String processoNumero;
	private String processoNumeroCompleto;
	private String processoAno;
	private String processoDigito;
	private String forumCodigo;
	
	private String dataCalculo;
	private String dataProgressao;
	private String dataLivramento;
	private String dataTerminoPena;
	private String dataValidadeMandadoPrisao;
	private String dataHomologacaoCalculo;
	
	private String idServentia;
	private String idProcesso;
	private String dataInicio;
	private String idEvento;
	private String evento;
	private String idRegime;
	private String regime;
	private String idTipoPena;
	private String tipoPena;
	private String idStatus;
	private String status;
	private String idLocal;
	private String local;
	private String idModalidade;
	private String modalidade;
	
	private String comarca;
	private String sentenciado;
	private String dataProtocolo;
	private String dataFase;
	private String fase;
	private String natureza;
	private String serventiaCargo;
	
	public RelatorioExecpenwebDt() {
		quantidade = 0;
		serventia = "";
		processoNumero = "";
		processoNumeroCompleto = "";
		processoAno = "";
		processoDigito = "";
		forumCodigo = "";
		regime = "";
		dataCalculo = "";
		dataProgressao = "";
		dataLivramento = "";
		dataTerminoPena = "";
		dataValidadeMandadoPrisao = "";
		dataHomologacaoCalculo = "";
		status = "";
		
		idServentia = "";
		idProcesso = "";
		dataInicio = "";
		idEvento = "";
		evento = "";
		idRegime = "";
		idTipoPena = "";
		tipoPena = "";
		idStatus = "";
		idLocal = "";
		local = "";
		idModalidade = "";
		modalidade = "";
		
		comarca = "";
		sentenciado = "";
		dataProtocolo = "";
		dataFase = "";
		fase = "";
		natureza = "";
		serventiaCargo = "";
	}
	
	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public String getServentia() {
		return serventia;
	}

	public void setServentia(String serventia) {
		this.serventia = serventia;
	}

	public String getProcessoNumero() {
		return processoNumero;
	}
	
	public String getProcessoNumeroCompleto() {
		return processoNumeroCompleto;
	}
	
	public void setProcessoNumeroCompleto() {
		this.processoNumeroCompleto = Funcoes.completarZeros(this.getProcessoNumero(), 7) + "." + Funcoes.completarZeros(this.getProcessoDigito(), 2) + "." + this.getProcessoAno() + "." + Configuracao.JTR + "." + Funcoes.completarZeros(getForumCodigo(), 4);
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}

	public String getProcessoAno() {
		return processoAno;
	}

	public void setProcessoAno(String processoAno) {
		this.processoAno = processoAno;
	}

	public String getProcessoDigito() {
		return processoDigito;
	}

	public void setProcessoDigito(String processoDigito) {
		this.processoDigito = processoDigito;
	}

	public String getForumCodigo() {
		return forumCodigo;
	}

	public void setForumCodigo(String forumCodigo) {
		this.forumCodigo = forumCodigo;
	}

	public String getRegime() {
		return regime;
	}

	public void setRegime(String regime) {
		if (regime != null && !regime.equalsIgnoreCase("null")) 
			this.regime = regime;
	}

	public String getDataCalculo() {
		return dataCalculo;
	}

	public void setDataCalculo(String dataCalculo) {
		this.dataCalculo = dataCalculo;
	}

	public String getDataProgressao() {
		return dataProgressao;
	}

	public void setDataProgressao(String dataProgressao) {
		this.dataProgressao = dataProgressao;
	}

	public String getDataLivramento() {
		return dataLivramento;
	}

	public void setDataLivramento(String dataLivramento) {
		this.dataLivramento = dataLivramento;
	}

	public String getDataTerminoPena() {
		return dataTerminoPena;
	}

	public void setDataTerminoPena(String dataTerminoPena) {
		this.dataTerminoPena = dataTerminoPena;
	}

	public String getDataValidadeMandadoPrisao() {
		return dataValidadeMandadoPrisao;
	}

	public void setDataValidadeMandadoPrisao(String dataValidadeMandadoPrisao) {
		this.dataValidadeMandadoPrisao = dataValidadeMandadoPrisao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status != null && !status.equalsIgnoreCase("null"))
			this.status = status;
	}

	public String getIdServentia() {
		return idServentia;
	}

	public void setIdServentia(String idServentia) {
		this.idServentia = idServentia;
	}

	public String getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(String idEvento) {
		this.idEvento = idEvento;
	}

	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public String getIdRegime() {
		return idRegime;
	}

	public void setIdRegime(String idRegime) {
		this.idRegime = idRegime;
	}

	public String getIdTipoPena() {
		return idTipoPena;
	}

	public void setIdTipoPena(String idTipoPena) {
		this.idTipoPena = idTipoPena;
	}

	public String getTipoPena() {
		return tipoPena;
	}

	public void setTipoPena(String tipoPena) {
		if (tipoPena != null && !tipoPena.equalsIgnoreCase("null"))
			this.tipoPena = tipoPena;
	}

	public String getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(String idStatus) {
		this.idStatus = idStatus;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}

	public String getSentenciado() {
		return sentenciado;
	}

	public void setSentenciado(String sentenciado) {
		this.sentenciado = sentenciado;
	}

	public String getDataProtocolo() {
		return dataProtocolo;
	}

	public void setDataProtocolo(String dataProtocolo) {
		this.dataProtocolo = dataProtocolo;
	}

	public String getDataFase() {
		return dataFase;
	}

	public void setDataFase(String dataFase) {
		this.dataFase = dataFase;
	}

	public String getFase() {
		return fase;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	public String getNatureza() {
		return natureza;
	}

	public void setNatureza(String natureza) {
		this.natureza = natureza;
	}

	public String getServentiaCargo() {
		return serventiaCargo;
	}

	public void setServentiaCargo(String serventiaCargo) {
		this.serventiaCargo = serventiaCargo;
	}

	public String getDataHomologacaoCalculo() {
		return dataHomologacaoCalculo;
	}

	public void setDataHomologacaoCalculo(String dataHomologacaoCalculo) {
		this.dataHomologacaoCalculo = dataHomologacaoCalculo;
	}

	public String getIdLocal() {
		return idLocal;
	}

	public void setIdLocal(String idLocal) {
		this.idLocal = idLocal;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		if (local != null && !local.equalsIgnoreCase("null"))
			this.local = local;
	}

	public String getIdModalidade() {
		return idModalidade;
	}

	public void setIdModalidade(String idModalidade) {
		this.idModalidade = idModalidade;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		if (modalidade != null && !modalidade.equalsIgnoreCase("null"))
			this.modalidade = modalidade;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

}
