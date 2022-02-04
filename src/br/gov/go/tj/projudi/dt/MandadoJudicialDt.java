package br.gov.go.tj.projudi.dt;

public class MandadoJudicialDt extends MandadoJudicialDtGen {

	private static final long serialVersionUID = 885118540983448161L;
	public static final int CodigoPermissao = 113;
	public static final int SIM_OFICIAL_COMPANHEIRO = 1; // Possui oficial companheiro
	public static final int NAO_OFICIAL_COMPANHEIRO = 0; // Não possui oficial companheiro
	public static final int SIM_ASSISTENCIA = 1;
	public static final int NAO_ASSISTENCIA = 0;
	public static final int SIM_LOCOMOCAO_HORA_MARCADA = 1;
	public static final int NAO_LOCOMOCAO_HORA_MARCADA = 0;
	public static final int QTD_LOCOMOCAO_OFICIAL_COMPANHEIRO = 4;
	
	public static final int SIM_RESOLUTIVO = 1;
	public static final int NAO_RESOLUTIVO = 2;

	public static final String ID_PAGAMENTO_PENDENTE = "1";
	public static final String ID_PAGAMENTO_AUTORIZADO = "2";
	public static final String ID_PAGAMENTO_ENVIADO = "3";
	public static final String ID_PAGAMENTO_NEGADO = "5";
	public static final String ID_ADICIONAL_MANDADO_GRATUITO_AUTORIZADO = "6";
	
	public static final String ANALISAR_GRATUITO_DEPOIS_DE = "0";

	public static final String CNPJ_TJGO = "02292266000180";

	private UsuarioServentiaDt usuarioServentiaDt_1;
	private UsuarioServentiaDt usuarioServentiaDt_2;
	private String dataDistribuicao;
	private String dataRetorno;
	private String dataLimite;
	private String Id_Serventia;
	private String Id_Proc;
	private String idComarca;
	private String idBairro;
	private String procNumero;
	private String mandJudPagamentoStatus;
	private String comarca;
	private String bairro;
	private String quantidadeLocomocao;
	private String valorLocomocao;
	private String idUsuPagamentoStatus;
	private String nomeUsuPagamentoStatus;
	private String dataPagamentoStatus;
	private String idUsuPagamentoEnvio;
	private String nomeUsuPagamentoEnvio;
	private String dataPagamentoEnvio;
	private String nomeServentia;
	private String modelo;
	
	public MandadoJudicialDt() {
		super.limpar();
		limpar();
	}

	public void limpar() {
		super.limpar();
		usuarioServentiaDt_1 = new UsuarioServentiaDt();
		usuarioServentiaDt_2 = new UsuarioServentiaDt();
		dataDistribuicao = "";
		dataRetorno = "";
		dataLimite = "";
		Id_Serventia = "";
		Id_Proc = "";
		idComarca = "";
		procNumero = "";
		mandJudPagamentoStatus = "";
		comarca = "";
		bairro = "";
		quantidadeLocomocao = "";
		valorLocomocao = "";
		idUsuPagamentoStatus = "";
		nomeUsuPagamentoStatus = "";
		dataPagamentoStatus = "";
		idUsuPagamentoEnvio = "";
		nomeUsuPagamentoEnvio = "";
		dataPagamentoEnvio = "";
		nomeServentia = "";
		modelo = "";
	}

	public UsuarioServentiaDt getUsuarioServentiaDt_1() {
		return usuarioServentiaDt_1;
	}

	public void setUsuarioServentiaDt_1(UsuarioServentiaDt usuarioServentiaDt_1) {
		this.usuarioServentiaDt_1 = usuarioServentiaDt_1;
	}

	public UsuarioServentiaDt getUsuarioServentiaDt_2() {
		return usuarioServentiaDt_2;
	}

	public void setUsuarioServentiaDt_2(UsuarioServentiaDt usuarioServentiaDt_2) {
		this.usuarioServentiaDt_2 = usuarioServentiaDt_2;
	}

	public String getDataDistribuicao() {
		return dataDistribuicao;
	}

	public void setDataDistribuicao(String dataDistribuicao) {
		this.dataDistribuicao = dataDistribuicao;
	}

	public String getDataRetorno() {
		return dataRetorno;
	}

	public void setDataRetorno(String dataRetorno) {
		if (dataRetorno == null) this.dataRetorno = "";
		else this.dataRetorno = dataRetorno;
	}

	public String getDataLimite() {
		return dataLimite;
	}

	public void setDataLimite(String dataLimite) {
		this.dataLimite = dataLimite;
	}

	public String getId_Serventia() {
		return Id_Serventia;
	}

	public void setId_Serventia(String id_serventia) {
		this.Id_Serventia = id_serventia;
	}

	public String getNomeUsuarioServentia_1() {
		if (usuarioServentiaDt_1 != null)
			return usuarioServentiaDt_1.getNome();
		else
			return "";
	}

	public void setNomeUsuarioServentia_1(String nome) {
		if (usuarioServentiaDt_1 != null)
			usuarioServentiaDt_1.setNome(nome);
	}

	public String getNomeUsuarioServentia_2() {
		if (usuarioServentiaDt_2 != null)
			return usuarioServentiaDt_2.getNome();
		else
			return "";
	}

	public void setNomeUsuarioServentia_2(String nome) {
		if (usuarioServentiaDt_2 != null)
			usuarioServentiaDt_2.setNome(nome);
	}

	public String getMandJudPagamentoStatus() {
		return mandJudPagamentoStatus;
	}

	public void setMandJudPagamentoStatus(String mandJudPagamentoStatus) {
		this.mandJudPagamentoStatus = mandJudPagamentoStatus;
	}

	public String getProcNumero() {
		return procNumero;
	}

	public void setProcNumero(String procNumero) {
		this.procNumero = procNumero;
	}

	public String getQuantidadeLocomocao() {
		return quantidadeLocomocao;
	}

	public void setQuantidadeLocomocao(String quantidadeLocomocao) {
		this.quantidadeLocomocao = quantidadeLocomocao;
	}

	public String getValorLocomocao() {
		return valorLocomocao;
	}

	public void setValorLocomocao(String valorLocomocao) {
		this.valorLocomocao = valorLocomocao;
	}

	public String getIdComarca() {
		return idComarca;
	}

	public void setIdComarca(String idComarca) {
		this.idComarca = idComarca;
	}

	public String getIdBairro() {
		return idBairro;
	}

	public void setIdBairro(String idBairro) {
		this.idBairro = idBairro;
	}

	public String getComarca() {
		return comarca;
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getIdUsuPagamentoStatus() {
		return idUsuPagamentoStatus;
	}

	public void setIdUsuPagamentoStatus(String idUsuPagamentoStatus) {
		this.idUsuPagamentoStatus = idUsuPagamentoStatus;
	}

	public String getNomeUsuPagamentoStatus() {
		return nomeUsuPagamentoStatus;
	}

	public void setNomeUsuPagamentoStatus(String nomeUsuPagamentoStatus) {
		if (nomeUsuPagamentoStatus == null) this.nomeUsuPagamentoStatus = "";
		else this.nomeUsuPagamentoStatus = nomeUsuPagamentoStatus;
	}

	public String getDataPagamentoStatus() {
		return dataPagamentoStatus;
	}

	public void setDataPagamentoStatus(String dataPagamentoStatus) {
		if (dataPagamentoStatus == null) this.dataPagamentoStatus = "";
		else this.dataPagamentoStatus = dataPagamentoStatus;	
	}

	public String getIdUsuPagamentoEnvio() {
		return idUsuPagamentoEnvio;
	}

	public void setIdUsuPagamentoEnvio(String idUsuPagamentoEnvio) {
		this.idUsuPagamentoEnvio = idUsuPagamentoEnvio;
	}

	public String getNomeUsuPagamentoEnvio() {
		return nomeUsuPagamentoEnvio;
	}

	public void setNomeUsuPagamentoEnvio(String nomeUsuPagamentoEnvio) {
		if (nomeUsuPagamentoEnvio == null) this.nomeUsuPagamentoEnvio = "";
		else this.nomeUsuPagamentoEnvio = nomeUsuPagamentoEnvio;
	}

	public String getDataPagamentoEnvio() {
	    return dataPagamentoEnvio;
	}

	public void setDataPagamentoEnvio(String dataPagamentoEnvio) {
		if (dataPagamentoEnvio == null) this.dataPagamentoEnvio = "";
		else this.dataPagamentoEnvio = dataPagamentoEnvio;
	}

	public String getNomeServentia() {
		return nomeServentia;
	}

	public void setNomeServentia(String nomeServentia) {
		this.nomeServentia = nomeServentia;
	}

	public String getId_Proc() {
		return Id_Proc;
	}

	public void setId_Proc(String id_Proc) {
		Id_Proc = id_Proc;
	}
	
	public boolean isAssistencia() {
		if(getAssistencia().equals( String.valueOf( SIM_ASSISTENCIA ) ) ) {
			return true;
		}
		return false;
	}
	
	public boolean isResolutivo() {
		if(getAssistencia().equals( String.valueOf( SIM_RESOLUTIVO ) ) ) {
			return true;
		}
		return false;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		if (modelo == null) modelo = "";
		else this.modelo = modelo;
	}
}
