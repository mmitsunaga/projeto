package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.ne.ConfirmacaoEmailInscricaoNe;
import br.gov.go.tj.utils.Configuracao;

public class UsuarioCejuscDt extends Dados {
	
	private static final long serialVersionUID = 587393745041175753L;
	
	public static final int CodigoPermissao = 845;
	
	public static final String CODIGO_STATUS_AGUARDANDO_AVALIACAO = "1";
	public static final String CODIGO_STATUS_APROVADO = String.valueOf(Configuracao.Curinga6);
	public static final String CODIGO_STATUS_PENDENTE = String.valueOf(Configuracao.Curinga7);
	public static final String CODIGO_STATUS_REPROVADO = String.valueOf(Configuracao.Curinga8);
	public static final String CODIGO_VOLUNTARIO_SIM = "1";
	public static final String CODIGO_VOLUNTARIO_NAO = "0";

	private String id;
	private String numeroConta;
	private String numeroAgencia;
	private String codigoBanco;
	private String curriculo;
	private String dataInscricao;
	private String observacaoStatus;
	private String codigoStatusAtual;
	private String codigoStatusAnterior;
	private String dataStatusAtual;
	private String dataStatusAnterior;
	private String opcaoMediador;
	private String opcaoConciliador;
	private String voluntario;
	private UsuarioDt usuarioDt;
	
	private List<UsuarioCejuscArquivoDt> listaUsuarioCejuscArquivoDt;
	private List<BancoDt> listaBancosConveniados;
	
	public UsuarioCejuscDt() {
		this.usuarioDt = new UsuarioDt();
		
		this.usuarioDt.setSexo(ConfirmacaoEmailInscricaoNe.SEXO_MASCULINO); //Valor default
		
		//Opções default
		this.opcaoConciliador = ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO;
		this.opcaoMediador = ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO;
		this.voluntario = CODIGO_VOLUNTARIO_NAO;
		
		listaBancosConveniados = new ArrayList<BancoDt>();
		
		BancoDt bancoDt = new BancoDt();
		bancoDt.setBancoCodigo(String.valueOf(BancoDt.BANCO_DO_BRASIL));
		bancoDt.setBanco("Banco do Brasil");
		listaBancosConveniados.add(bancoDt);
		
		bancoDt = new BancoDt();
		bancoDt.setBancoCodigo(String.valueOf(BancoDt.CAIXA_ECONOMICA_FEDERAL));
		bancoDt.setBanco("Caixa Econômica Federal");
		listaBancosConveniados.add(bancoDt);
		
		//******
		//BANCO ITAÚ NÃO É MAIS CONVENIADO
		
//		bancoDt = new BancoDt();
//		bancoDt.setBancoCodigo(String.valueOf(BancoDt.ITAU));
//		bancoDt.setBanco("Itaú");
//		listaBancosConveniados.add(bancoDt);
	}
	
	public List<BancoDt> getListaBancosConveniados() {
		return listaBancosConveniados;
	}
	public void setListaBancosConveniados(List<BancoDt> listaBancosConveniados) {
		this.listaBancosConveniados = listaBancosConveniados;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(String numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getNumeroAgencia() {
		return numeroAgencia;
	}

	public void setNumeroAgencia(String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getCurriculo() {
		if( curriculo == null ) {
			curriculo = "";
		}
		return curriculo;
	}

	public void setCurriculo(String curriculo) {
		this.curriculo = curriculo;
	}

	public String getDataInscricao() {
		return dataInscricao;
	}

	public void setDataInscricao(String dataInscricao) {
		this.dataInscricao = dataInscricao;
	}

	public String getObservacaoStatus() {
		if( observacaoStatus == null ) {
			observacaoStatus = "";
		}
		return observacaoStatus;
	}

	public void setObservacaoStatus(String observacaoStatus) {
		this.observacaoStatus = observacaoStatus;
	}

	public String getCodigoStatusAtual() {
		return codigoStatusAtual;
	}

	public void setCodigoStatusAtual(String codigoStatusAtual) {
		this.codigoStatusAtual = codigoStatusAtual;
	}

	public String getCodigoStatusAnterior() {
		return codigoStatusAnterior;
	}

	public void setCodigoStatusAnterior(String codigoStatusAnterior) {
		this.codigoStatusAnterior = codigoStatusAnterior;
	}

	public String getDataStatusAtual() {
		return dataStatusAtual;
	}

	public void setDataStatusAtual(String dataStatusAtual) {
		this.dataStatusAtual = dataStatusAtual;
	}

	public String getDataStatusAnterior() {
		return dataStatusAnterior;
	}

	public void setDataStatusAnterior(String dataStatusAnterior) {
		this.dataStatusAnterior = dataStatusAnterior;
	}

	public String getOpcaoMediador() {
		return opcaoMediador;
	}

	public void setOpcaoMediador(String opcaoMediador) {
		this.opcaoMediador = opcaoMediador;
	}

	public String getOpcaoConciliador() {
		return opcaoConciliador;
	}

	public void setOpcaoConciliador(String opcaoConciliador) {
		this.opcaoConciliador = opcaoConciliador;
	}

	public UsuarioDt getUsuarioDt() {
		return usuarioDt;
	}

	public void setUsuarioDt(UsuarioDt usuarioDt) {
		this.usuarioDt = usuarioDt;
	}

	public List<UsuarioCejuscArquivoDt> getListaUsuarioCejuscArquivoDt() {
		return listaUsuarioCejuscArquivoDt;
	}

	public void setListaUsuarioCejuscArquivoDt(List<UsuarioCejuscArquivoDt> listaUsuarioCejuscArquivoDt) {
		this.listaUsuarioCejuscArquivoDt = listaUsuarioCejuscArquivoDt;
	}
	
	public String getVoluntario() {
		return voluntario;
	}

	public void setVoluntario(String voluntario) {
		this.voluntario = voluntario;
	}

	/**
	 * Método que retorna o label para o código de status
	 * @param String codigoStatus
	 * @return String
	 */
	public String getLabelCodigoStatus(String codigoStatus) {
		if( codigoStatus != null ) {
			if( codigoStatus.equals(CODIGO_STATUS_AGUARDANDO_AVALIACAO) ) {
				return "AGUARDANDO AVALIAÇÃO";
			}
			if( codigoStatus.equals(CODIGO_STATUS_APROVADO) ) {
				return "APROVADO";
			}
			if( codigoStatus.equals(CODIGO_STATUS_PENDENTE) ) {
				return "PENDENTE";
			}
			if( codigoStatus.equals(CODIGO_STATUS_REPROVADO) ) {
				return "REPROVADO";
			}
		}
		return "";
	}
	
	/**
	 * Método que retorna o cor do label para o código de status
	 * @param String codigoStatus
	 * @return String
	 */
	public String getCorLabelCodigoStatus(String codigoStatus) {
		if( codigoStatus != null ) {
			if( codigoStatus.equals(CODIGO_STATUS_AGUARDANDO_AVALIACAO) ) {
				return "orange";
			}
			if( codigoStatus.equals(CODIGO_STATUS_APROVADO) ) {
				return "green";
			}
			if( codigoStatus.equals(CODIGO_STATUS_PENDENTE) ) {
				return "orange";
			}
			if( codigoStatus.equals(CODIGO_STATUS_REPROVADO) ) {
				return "red";
			}
		}
		return "";
	}

	public String getLabelOpcaoPerfil(String codigoPerfil) {
		if( codigoPerfil != null ) {
			if( codigoPerfil.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) {
				return "OPÇÃO NÃO ESCOLHIDA";
			}
			if( codigoPerfil.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_AGUARDANDO_APROVACAO) ) {
				return "AGUARDANDO AVALIAÇÃO";
			}
			if( codigoPerfil.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO) ) {
				return "APROVADO";
			}
			if( codigoPerfil.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_APROVADO) ) {
				return "NÃO APROVADO";
			}
		}
		return "";
	}
	
	public String getCorLabelOpcaoPerfil(String codigoPerfil) {
		if( codigoPerfil != null ) {
			if( codigoPerfil.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) {
				return "black";
			}
			if( codigoPerfil.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_AGUARDANDO_APROVACAO) ) {
				return "orange";
			}
			if( codigoPerfil.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_APROVADO) ) {
				return "green";
			}
			if( codigoPerfil.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_APROVADO) ) {
				return "red";
			}
		}
		return "";
	}
	
	public String getCheckedConciliadorTrue() {
		String retorno = "";
		if( opcaoConciliador != null && !opcaoConciliador.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) {
			retorno = "true";
		}
		return retorno;
	}
	
	public String getCheckedMediadorTrue() {
		String retorno = "";
		if( opcaoMediador != null && !opcaoMediador.equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) {
			retorno = "true";
		}
		return retorno;
	}
	
	public String getPropriedades() {
		return "UsuarioCandidatoDt [id=" + id + ", numeroConta=" + numeroConta + ", numeroAgencia=" + numeroAgencia
				+ ", codigoBanco=" + codigoBanco + ", curriculo=" + curriculo + ", dataInscricao=" + dataInscricao
				+ ", observacaoStatus=" + observacaoStatus + ", codigoStatusAtual=" + codigoStatusAtual
				+ ", codigoStatusAnterior=" + codigoStatusAnterior + ", dataStatusAtual=" + dataStatusAtual
				+ ", dataStatusAnterior=" + dataStatusAnterior + ", opcaoMediador=" + opcaoMediador + ", voluntario=" + voluntario
				+ ", opcaoConciliador=" + opcaoConciliador + "]";
	}
	
}