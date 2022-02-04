package br.gov.go.tj.projudi.dt;

import java.util.List;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class ServentiaCargoDt extends ServentiaCargoDtGen {

	private static final long serialVersionUID = 309505857849077682L;
	public static final int	  CodigoPermissao  = 261;

	// Variáveis para controlar possíveis status de ServentiaCargo (CodigoTemp)
	public static final int	  VAZIO			   = -1;
	public static final int	  OCUPADO		   = 0;
	private String			  hash;									 // Atributo para validação
	private String			  PrazoAgenda;
	private List			  listaCargosServentia;
	private String			  Id_UsuarioServentia;
	private String			  redator;
	private String			  Id_ServentiaSubtipo;
	private String			  ServentiaSubtipo;
	private String			  ServentiaSubtipoCodigo;
	private String			  DataInicialSubstituicao;
	private String			  DataFinalSubstituicao;
	private String			  quantidadeDistribuicao;
	private boolean			  substituicao;
	private String			  ProcessoNumeroCompleto;
	private String			  Id_ProcessoResponsavel;
	private String Id_ServentiaGrupo;
	private int ordem;

	public String getProcessoNumeroCompleto() {
		return ProcessoNumeroCompleto;
	}

	public void setProcessoNumeroCompleto(String processoNumeroCompleto) {
		ProcessoNumeroCompleto = processoNumeroCompleto;
	}

	public void limpar() {
		PrazoAgenda = "";
		listaCargosServentia = null;
		Id_UsuarioServentia = "";
		redator = "";
		Id_ServentiaSubtipo = "";
		ServentiaSubtipoCodigo = "";
		ServentiaSubtipo = "";
		DataInicialSubstituicao = "";
		DataFinalSubstituicao = "";
		quantidadeDistribuicao = "";
		substituicao = false;
		Id_ProcessoResponsavel = "";
		super.limpar();
	}

	public ServentiaCargoDt() {

		this.limpar();

	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPrazoAgenda() {
		return PrazoAgenda;
	}

	public void setPrazoAgenda(String valor) {
		if (valor != null)
			PrazoAgenda = valor;
	}

	public List getListaCargosServentia() {
		return listaCargosServentia;
	}

	public void setListaCargosServentia(List listaCargosServentia) {
		if (listaCargosServentia != null)
			this.listaCargosServentia = listaCargosServentia;
	}

	public String getId_UsuarioServentia() {
		return Id_UsuarioServentia;
	}

	public void setId_UsuarioServentia(String idUsuarioServentia) {
		if (idUsuarioServentia != null)
			this.Id_UsuarioServentia = idUsuarioServentia;
	}

	public String getQuantidadeDistribuicao() {
		return quantidadeDistribuicao;
	}

	public void setQuantidadeDistribuicao(String quantidadeDistribuicao) {
		if (quantidadeDistribuicao != null)
			this.quantidadeDistribuicao = quantidadeDistribuicao;
	}

	public String getRedator() {
		return redator;
	}

	public void setRedator(String redator) {
		this.redator = redator;
	}

	public void setSubstituicao(String substituicao) {
		if (substituicao != null && substituicao.trim().length() > 0) {
			this.substituicao = Funcoes.BancoLogicoBoolean(substituicao);
		}
		return;

	}

	public boolean isSubstituicao() {
		return substituicao;
	}

	public String getId_ServentiaSubtipo() {
		return Id_ServentiaSubtipo;
	}

	public void setId_ServentiaSubtipo(String valor) {
		if (valor != null)
			if (valor.equalsIgnoreCase("null")) {
				Id_ServentiaSubtipo = "";
				ServentiaSubtipo = "";
			} else if (!valor.equalsIgnoreCase(""))
				Id_ServentiaSubtipo = valor;
	}

	public String getServentiaSubtipo() {
		return ServentiaSubtipo;
	}

	public void setServentiaSubtipo(String valor) {
		if (valor != null)
			ServentiaSubtipo = valor;
	}

	public String getServentiaSubtipoCodigo() {
		return ServentiaSubtipoCodigo;
	}

	public void setServentiaSubtipoCodigo(String valor) {
		if (valor != null)
			ServentiaSubtipoCodigo = valor;
	}

	public String getDataInicialSubstituicao() {
		return DataInicialSubstituicao;
	}

	public void setDataInicialSubstituicao(String dataInicialSubstituicao) {
		if (dataInicialSubstituicao != null)
			DataInicialSubstituicao = dataInicialSubstituicao;
	}

	public String getDataFinalSubstituicao() {
		return DataFinalSubstituicao;
	}

	public void setDataFinalSubstituicao(String dataFinalSubstituicao) {
		if (dataFinalSubstituicao != null)
			DataFinalSubstituicao = dataFinalSubstituicao;
	}

	public void setId_Serventia(String valor) {
		if (valor != null)
			if (valor.equalsIgnoreCase("null")) {
				Id_Serventia = "";
				Serventia = "";
			} else if (!valor.equalsIgnoreCase(""))
				Id_Serventia = valor;
	}

	public String getServentia() {
		return Serventia;
	}

	public void setServentia(String valor) {
		if (valor != null)
			if (valor.equalsIgnoreCase("null")) {
				Serventia = "";
			} else if (!valor.equalsIgnoreCase(""))
				Serventia = valor;
	}

	public String getId_ProcessoResponsavel() {
		return Id_ProcessoResponsavel;
	}

	public void setId_ProcessoResponsavel(String id_ProcessoResponsavel) {
		Id_ProcessoResponsavel = id_ProcessoResponsavel;
	}

	public boolean isMagistrado() throws MensagemException {
		if (this.getCargoTipoCodigo() == null)
			throw new MensagemException("CargoTipoCodigo não inicializado.");

		switch (Funcoes.StringToInt(getCargoTipoCodigo())) {
			case CargoTipoDt.JUIZ_1_GRAU:
			case CargoTipoDt.JUIZ_AUXILIAR_PRESIDENCIA:
			case CargoTipoDt.JUIZ_CORREGEDOR:
			case CargoTipoDt.JUIZ_EXECUCAO:
			case CargoTipoDt.DESEMBARGADOR:
			case CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU:
			case CargoTipoDt.REVISOR_SEGUNDO_GRAU:
			case CargoTipoDt.VOGAL_SEGUNDO_GRAU:
			case CargoTipoDt.PRESIDENTE_TURMA:
			case CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL:
			case CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
			case CargoTipoDt.JUIZ_UPJ:

				return true;
		}

		return false;
	}
	
	public boolean isJuizUPJ() throws MensagemException {
		if (this.getCargoTipoCodigo() == null)
			throw new MensagemException("CargoTipoCodigo não inicializado.");

		switch (Funcoes.StringToInt(getCargoTipoCodigo())) {
			case CargoTipoDt.JUIZ_UPJ:
				return true;
		}

		return false;
	}

	public boolean isMP() throws MensagemException {
		if (this.getCargoTipoCodigo() == null)
			throw new MensagemException("CargoTipoCodigo não inicializado.");

		switch (Funcoes.StringToInt(getCargoTipoCodigo())) {
			case CargoTipoDt.MINISTERIO_PUBLICO:
				return true;
		}

		return false;
	}
	
	public boolean isServentiaCargoOcupado(){
		if (this.getCodigoTemp() != null && this.getCodigoTemp().equals(String.valueOf(ServentiaCargoDt.OCUPADO))){
			return true;
		}
		return false;
	}

	public boolean isPromotoriaPrimeiroGrau() throws MensagemException {
		if (this.getServentiaSubtipoCodigo() == null)
			throw new MensagemException("ServentiaSubtipoCodigo não inicializado.");
		return getServentiaSubtipoCodigo().trim().equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.MP_PRIMEIRO_GRAU));
	}

	public boolean isPromotoriaSegundoGrau() throws MensagemException {
		if (this.getServentiaSubtipoCodigo() == null)
			throw new MensagemException("ServentiaSubtipoCodigo não inicializado.");
		return getServentiaSubtipoCodigo().trim().equalsIgnoreCase(String.valueOf(ServentiaSubtipoDt.MP_SEGUNDO_GRAU));
	}

	public void setId_ServentiaGrupo(String id_serventia_grupo) {
		Id_ServentiaGrupo = id_serventia_grupo;
	}
	
	public String getId_ServentiaGrupo() {
		return Id_ServentiaGrupo;
	}

	public void setOrdemTurmaJulgadora(int ordem) {
		this.ordem = ordem;
	}
	
	public int getOrdemTurmaJulgadora() {
		return this.ordem;
	}
}
