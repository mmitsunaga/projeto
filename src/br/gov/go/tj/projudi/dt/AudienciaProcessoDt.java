package br.gov.go.tj.projudi.dt;

import java.util.List;
import java.util.Objects;

import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.utils.Funcoes;

public class AudienciaProcessoDt extends AudienciaProcessoDtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2457756340741712266L;

	// CÓDIGO DE SEGURANÇA DA CLASSE
	public static final int CodigoPermissao = 457;
	
	public static final String STATUS_JULGAMENTO_INICIADO = "-1";	
	public static final String STATUS_JULGAMENTO_ADIADO = "-2";
	public static final String STATUS_JULGAMENTO_EM_MESA_EXTRA_PAUTA = "-3";
	public static final String STATUS_JULGAMENTO_PAUTA_DIA = "0";
	
	public static final String EXTRATO_ATA_JULGAMENTO_ADIADO_INICIADO_MODELO_CODIGO = "1002";

	// OBJETO PROCESSOSIMPLESDT
	private ProcessoDt processoDt;

	// OBJETO AUDIÊNCIADT
	private AudienciaDt audienciaDt;

	//Usuário responsável por uma audiência, usuário que ocupa o cargo vinculado a audiência
	private String nomeResponsavel;

	// Usuários que participaram da realização de audiência
	private List listaResponsaveis;	
	
	private String Id_AudienciaProcessoStatusAnalista;
	private String AudienciaProcessoStatusAnalista;	
	private String AudienciaProcessoStatusCodigoAnalista;
	
	private String Id_ArquivoAta;
	private String NomeArquivoAta;	
	
	private String DataAudienciaOriginal;
	
	private String Id_ArquivoAtaSessaoIniciada;
	private String NomeArquivoAtaSessaoIniciada;
	
	private String Id_ArquivoAtaSessaoAdiada;
	private String NomeArquivoAtaSessaoAdiada;
	
	private String id_ServentiaCargoPresidente;
	private String serventiaCargoPresidente;
	private String id_ServentiaPresidente;
	private String serventiaPresidente;
	private String id_ServentiaMP;
	private String seventiaMP;	
	private String id_ServentiaCargoMP;
	private String serventiaCargoMP;	
	private String id_ServentiaCargoRedator;
	private String serventiaCargoRedator;
	private String id_ServentiaRedator;
	private String serventiaRedator;
	private String id_ServentiaCargoMPProcesso;
	private String serventiaCargoMPProcesso;
	private String nomeMPProcesso;
	private boolean isResponsavelSessao;
	private String id_ServentiaResponsavel;
	private String Acordo;
	private String ValorAcordo;
	private String Id_ProcessoTipo;
	private String ProcessoTipo;
	private String descricaoPoloAtivo;
	private String descricaoPoloPassivo;
	private String Id_PendenciaVotoRelator;
	private String Id_PendenciaEmentaRelator;
	private String Id_PendenciaVotoRedator;
	private String Id_PendenciaEmentaRedator;
	private String AudienciaProcessoStatusCodigoTemp;
	private String AudienciaProcessoStatusTemp;
	private boolean votosPrazoExpirado;
	private boolean podeInserirExtrato;
	private boolean podeEncaminhar;
	private boolean permiteSustentacaoOral;
	private boolean podeAdiarComPedidoSusOral;
	private boolean possuiPedidoDeferidoRelator;
	private boolean possuiPedidoIndeferidoRelator;
	private boolean adiadaPorSustentacaoOral; // lrcampos - 11/10/2019 14:44 - Adicionar variavel
	private String situacaoPedidoSO; // lrcampos - 11/10/2019 14:44 - Adicionar variavel
	private boolean originadaSessaoVirtual;
	private boolean possuiDeferimentoAutomatico;
	private String  Id_Audi_Proc_Origem;
	


	/**
	 * Construtor que limpa as propriedades da classe.
	 * 
	 * @author Keila Sousa Silva
	 * @since 07/08/2009
	 */
	public AudienciaProcessoDt() {
		limpar();	
	}
	
	public void copiar(AudienciaProcessoDt objeto){
		if(objeto == null) return; // jvosantos - 14/10/2019 12:33 - Correção erro de NullPointer
		limpar();
		super.copiar(objeto);
		processoDt = objeto.processoDt;
		audienciaDt = objeto.audienciaDt;
		nomeResponsavel = objeto.nomeResponsavel;
		listaResponsaveis = objeto.listaResponsaveis;
		Id_AudienciaProcessoStatusAnalista = objeto.Id_AudienciaProcessoStatusAnalista;
		AudienciaProcessoStatusAnalista = objeto.AudienciaProcessoStatusAnalista;
		AudienciaProcessoStatusCodigoAnalista = objeto.AudienciaProcessoStatusCodigoAnalista;
		Id_ArquivoAta = objeto.Id_ArquivoAta;
		NomeArquivoAta = objeto.NomeArquivoAta;	
		DataAudienciaOriginal = objeto.DataAudienciaOriginal;
		Id_Audi_Proc_Origem = objeto.Id_Audi_Proc_Origem;
		Id_ArquivoAtaSessaoIniciada = objeto.Id_ArquivoAtaSessaoIniciada;
		NomeArquivoAtaSessaoIniciada = objeto.NomeArquivoAtaSessaoIniciada;
		Id_ArquivoAtaSessaoAdiada = objeto.Id_ArquivoAtaSessaoAdiada;
		NomeArquivoAtaSessaoAdiada = objeto.NomeArquivoAtaSessaoAdiada;
		id_ServentiaCargoPresidente = objeto.id_ServentiaCargoPresidente;
		serventiaCargoPresidente = objeto.serventiaCargoPresidente;	
		id_ServentiaMP = objeto.id_ServentiaMP;
		seventiaMP = objeto.seventiaMP;	
		id_ServentiaCargoMP = objeto.id_ServentiaCargoMP;
		serventiaCargoMP = objeto.serventiaCargoMP;	
		id_ServentiaCargoRedator = objeto.id_ServentiaCargoRedator;
		serventiaCargoRedator = objeto.serventiaCargoRedator;
		serventiaRedator = objeto.serventiaRedator;
		id_ServentiaRedator = objeto.id_ServentiaRedator;
		id_ServentiaPresidente = objeto.id_ServentiaPresidente;
		serventiaPresidente = objeto.serventiaPresidente;
		id_ServentiaCargoMPProcesso = objeto.id_ServentiaCargoMPProcesso;
		serventiaCargoMPProcesso = objeto.serventiaCargoMPProcesso;
		nomeMPProcesso = objeto.nomeMPProcesso;
		isResponsavelSessao = objeto.isResponsavelSessao();
		id_ServentiaResponsavel = objeto.getId_ServentiaResponsavel();
		Acordo = objeto.getAcordo();
		ValorAcordo = objeto.getValorAcordo();
		Id_ProcessoTipo = objeto.getId_ProcessoTipo();
		ProcessoTipo = objeto.getProcessoTipo();	
		descricaoPoloAtivo = objeto.getDescricaoPoloAtivo();
		descricaoPoloPassivo = objeto.getDescricaoPoloPassivo();
		Id_PendenciaVotoRelator = objeto.getId_PendenciaVotoRelator();
		Id_PendenciaEmentaRelator = objeto.getId_PendenciaEmentaRelator();
		Id_PendenciaVotoRedator = objeto.getId_PendenciaVotoRedator();
		Id_PendenciaEmentaRedator = objeto.getId_PendenciaEmentaRedator();
	}
	
	public void limpar(){
		super.limpar();
		processoDt = null;
		audienciaDt = null;
		listaResponsaveis = null;
		nomeResponsavel = "";	
		Id_AudienciaProcessoStatusAnalista = "";
		AudienciaProcessoStatusAnalista = "";
		AudienciaProcessoStatusCodigoAnalista = "";
		Id_ArquivoAta = "";
		NomeArquivoAta = "";
		DataAudienciaOriginal = "";
		Id_Audi_Proc_Origem = "";
		Id_ArquivoAtaSessaoIniciada = "";
		NomeArquivoAtaSessaoIniciada = "";
		Id_ArquivoAtaSessaoAdiada = "";
		NomeArquivoAtaSessaoAdiada = "";
		id_ServentiaCargoPresidente = "";
		serventiaCargoPresidente = "";	
		id_ServentiaMP = "";
		seventiaMP = "";	
		id_ServentiaCargoMP = "";
		serventiaCargoMP = "";
		id_ServentiaCargoRedator = "";
		serventiaCargoRedator = "";
		serventiaRedator = "";
		id_ServentiaRedator = "";
		id_ServentiaPresidente = "";
		serventiaPresidente = "";
		isResponsavelSessao = false;
		id_ServentiaResponsavel = "";
		Acordo = "";
		ValorAcordo = "";
		Id_ProcessoTipo = "";
		ProcessoTipo = "";	
		descricaoPoloAtivo = "";
		descricaoPoloPassivo = "";
		Id_PendenciaVotoRelator = "";
		Id_PendenciaEmentaRelator = "";
		Id_PendenciaVotoRedator = "";
		Id_PendenciaEmentaRedator = "";
		AudienciaProcessoStatusCodigoTemp = "";
		podeInserirExtrato = true;
	}

	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public AudienciaDt getAudienciaDt() {
		return audienciaDt;
	}
	
	public boolean isSessaoVirtual() throws Exception {
		if (Objects.isNull(this.audienciaDt) && Objects.nonNull(this.getId_Audiencia())) {
			this.audienciaDt = new AudienciaNe().consultarId(this.getId_Audiencia());
		}
		return this.audienciaDt != null && this.audienciaDt.isVirtual();
	}

	public void setAudienciaDt(AudienciaDt audienciaDt) {
		this.audienciaDt = audienciaDt;
	}

	public List getListaResponsaveis() {
		return listaResponsaveis;
	}

	public void setListaResponsaveis(List listaResponsaveis) {
		this.listaResponsaveis = listaResponsaveis;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		if (nomeResponsavel != null) this.nomeResponsavel = nomeResponsavel;
	}
	
	public boolean isJulgamentoIniciado() {
		if (this.getCodigoTemp() == null) return false;
		return (this.getCodigoTemp().trim().equalsIgnoreCase(STATUS_JULGAMENTO_INICIADO));
	}
	
	public void setJulgamentoIniciado() {
		this.setCodigoTemp(STATUS_JULGAMENTO_INICIADO);
	}
	
	public boolean isJulgamentoAdiado() {
		if (this.getCodigoTemp() == null) return false;
		return (this.getCodigoTemp().trim().equalsIgnoreCase(STATUS_JULGAMENTO_ADIADO));
	}
	
	public boolean isJulgamentoAdiadoSustentacaoOral() {
		return (Funcoes.StringToInt(AudienciaProcessoStatusCodigoAnalista) == AudienciaProcessoStatusDt.JULGAMENTO_ADIADO_SUSTENTACAO_ORAL);
	}
	
	public void setJulgamentoAdiado() {
		this.setCodigoTemp(STATUS_JULGAMENTO_ADIADO);
	}
	
	public boolean isPautaDoDia() {
		if (this.getCodigoTemp() == null) return true;
		return (!isJulgamentoAdiado() && !isJulgamentoIniciado() && !isJulgamentoEmMesaParaJulgamento());
	}
	
	public void setPautaDoDia() {
		this.setCodigoTemp(STATUS_JULGAMENTO_PAUTA_DIA);
	}
	
	public void setJulgamentoEmMesaParaJulgamento() {
		this.setCodigoTemp(STATUS_JULGAMENTO_EM_MESA_EXTRA_PAUTA);
	}
	
	public boolean isJulgamentoEmMesaParaJulgamento() {
		if (this.getCodigoTemp() == null) return false;
		return (this.getCodigoTemp().trim().equalsIgnoreCase(STATUS_JULGAMENTO_EM_MESA_EXTRA_PAUTA));
	}
	
	public boolean isAnalistaPodeMovimentar() {
		if (this.getId_ArquivoAta() == null) return true;
		if (this.getId_ArquivoAta().trim().length() == 0) return true;
		return false;
		//return (this.isJulgamentoAdiado() || this.isJulgamentoIniciado());
	}

	public String getId_AudienciaProcessoStatusAnalista() {
		return Id_AudienciaProcessoStatusAnalista;
	}

	public void setId_AudienciaProcessoStatusAnalista(String idAudienciaProcessoStatusAnalista) {
		if (idAudienciaProcessoStatusAnalista!=null) this.Id_AudienciaProcessoStatusAnalista = idAudienciaProcessoStatusAnalista;
	}

	public String getAudienciaProcessoStatusAnalista() {
		return AudienciaProcessoStatusAnalista;
	}

	public void setAudienciaProcessoStatusAnalista(String audienciaProcessoStatusAnalista) {
		if (audienciaProcessoStatusAnalista!=null) this.AudienciaProcessoStatusAnalista = audienciaProcessoStatusAnalista;
	}

	public String getAudienciaProcessoStatusCodigoAnalista() {
		return AudienciaProcessoStatusCodigoAnalista;
	}

	public void setAudienciaProcessoStatusCodigoAnalista(String audienciaProcessoStatusCodigoAnalista) {
		if (audienciaProcessoStatusCodigoAnalista!=null) this.AudienciaProcessoStatusCodigoAnalista = audienciaProcessoStatusCodigoAnalista;
	}
	
	public String getId_ArquivoAta() {
		return Id_ArquivoAta;
	}

	public void setId_ArquivoAta(String idArquivoAta) {
		if (idArquivoAta!=null) this.Id_ArquivoAta = idArquivoAta;
	}
	
	public String getNomeArquivoAta() {
		return NomeArquivoAta;
	}

	public void setNomeArquivoAta(String nomeArquivoAta) {
		if (nomeArquivoAta!=null) this.NomeArquivoAta = nomeArquivoAta;
	}
	
	public String getDataAudienciaOriginal(){
		return this.DataAudienciaOriginal;
	}
	
	public void setDataAudienciaOriginal(String dataAudienciaOriginal){
		if (dataAudienciaOriginal!=null) this.DataAudienciaOriginal = dataAudienciaOriginal;
	}
	
	public String getId_ArquivoAtaSessaoIniciada() {
		return Id_ArquivoAtaSessaoIniciada;
	}

	public void setId_ArquivoAtaSessaoIniciada(String idArquivoAtaSessaoIniciada) {
		if (idArquivoAtaSessaoIniciada!=null) this.Id_ArquivoAtaSessaoIniciada = idArquivoAtaSessaoIniciada;
	}
	
	public boolean possuiArquivoAtaSessaoIniciada(){
		if (this.Id_ArquivoAtaSessaoIniciada == null) return false;
		return (this.Id_ArquivoAtaSessaoIniciada.trim().length() > 0);
	}
	
	public String getNomeArquivoAtaSessaoIniciada() {
		return NomeArquivoAtaSessaoIniciada;
	}

	public void setNomeArquivoAtaSessaoIniciada(String nomeArquivoAtaSessaoIniciada) {
		if (nomeArquivoAtaSessaoIniciada!=null) this.NomeArquivoAtaSessaoIniciada = nomeArquivoAtaSessaoIniciada;
	}
	
	public String getId_ArquivoAtaSessaoAdiada() {
		return Id_ArquivoAtaSessaoAdiada;
	}

	public void setId_ArquivoAtaSessaoAdiada(String idArquivoAtaSessaoAdiada) {
		if (idArquivoAtaSessaoAdiada!=null) this.Id_ArquivoAtaSessaoAdiada = idArquivoAtaSessaoAdiada;
	}
	
	public boolean possuiArquivoAtaSessaoAdiada(){
		if (this.Id_ArquivoAtaSessaoAdiada == null) return false;
		return (this.Id_ArquivoAtaSessaoAdiada.trim().length() > 0);
	}
	
	public String getNomeArquivoAtaSessaoAdiada() {
		return NomeArquivoAtaSessaoAdiada;
	}

	public void setNomeArquivoAtaSessaoAdiada(String nomeArquivoAtaSessaoAdiada) {
		if (nomeArquivoAtaSessaoAdiada!=null) this.NomeArquivoAtaSessaoAdiada = nomeArquivoAtaSessaoAdiada;
	}

	public String getId_ServentiaCargoPresidente() {
		return id_ServentiaCargoPresidente;
	}

	public void setId_ServentiaCargoPresidente(String idServentiaCargoPresidente) {
		if(idServentiaCargoPresidente != null) this.id_ServentiaCargoPresidente = idServentiaCargoPresidente;
	}

	public String getServentiaCargoPresidente() {
		return serventiaCargoPresidente;
	}

	public void setServentiaCargoPresidente(String serventiaCargoPresidente) {
		if(serventiaCargoPresidente != null) this.serventiaCargoPresidente = serventiaCargoPresidente;
	}
	
	public String getId_ServentiaPresidente() {
		return this.id_ServentiaPresidente;
	}

	public void setId_ServentiaPresidente(String idServentiaPresidente) {
		if(idServentiaPresidente != null) this.id_ServentiaPresidente = idServentiaPresidente;
	}

	public String getServentiaPresidente() {
		return this.serventiaPresidente;
	}

	public void setServentiaPresidente(String serventiaPresidente) {
		if(serventiaPresidente != null) this.serventiaPresidente = serventiaPresidente;
	}

	public String getId_ServentiaMP() {
		return id_ServentiaMP;
	}

	public void setId_ServentiaMP(String idServentiaMP) {
		if(idServentiaMP != null) this.id_ServentiaMP = idServentiaMP;
	}

	public String getServentiaMP() {
		return seventiaMP;
	}

	public void setServentiaMP(String seventiaMP) {
		if(seventiaMP != null) this.seventiaMP = seventiaMP;
	}

	public String getId_ServentiaCargoMP() {
		return id_ServentiaCargoMP;
	}

	public void setId_ServentiaCargoMP(String idServentiaCargoMP) {
		if(idServentiaCargoMP != null) this.id_ServentiaCargoMP = idServentiaCargoMP;
	}

	public String getServentiaCargoMP() {
		return serventiaCargoMP;
	}

	public void setServentiaCargoMP(String serventiaCargoMP) {
		if(serventiaCargoMP != null) this.serventiaCargoMP = serventiaCargoMP;
	}	
	
	public String getId_ServentiaCargoRedator() {
		return this.id_ServentiaCargoRedator;
	}

	public void setId_ServentiaCargoRedator(String idServentiaCargoRedator) {
		if(idServentiaCargoRedator != null) this.id_ServentiaCargoRedator = idServentiaCargoRedator;
	}
	
	public String getServentiaCargoRedator() {
		return this.serventiaCargoRedator;
	}

	public void setServentiaCargoRedator(String serventiaRedator) {
		if(serventiaRedator != null) this.serventiaCargoRedator = serventiaRedator;
	}
	
	public String getId_ServentiaRedator() {
		return this.id_ServentiaRedator;
	}

	public void setId_ServentiaRedator(String idServentiaRedator) {
		if(idServentiaRedator != null) this.id_ServentiaRedator = idServentiaRedator;
	}

	public String getServentiaRedator() {
		return this.serventiaRedator;
	}

	public void setServentiaRedator(String serventiaRedator) {
		if(serventiaRedator != null) this.serventiaRedator = serventiaRedator;
	}
	
	public String getId_ServentiaCargoMPProcesso() {
		return id_ServentiaCargoMPProcesso;
	}

	public void setId_ServentiaCargoMPProcesso(String id_ServentiaCargoMPProcesso) {
		if(id_ServentiaCargoMPProcesso != null) this.id_ServentiaCargoMPProcesso = id_ServentiaCargoMPProcesso;
	}

	public String getServentiaCargoMPProcesso() {
		return serventiaCargoMPProcesso;
	}

	public void setServentiaCargoMPProcesso(String serventiaCargoMPProcesso) {
		if(serventiaCargoMPProcesso != null) this.serventiaCargoMPProcesso = serventiaCargoMPProcesso;
	}
	
	public String getNomeMPProcesso() {
		return nomeMPProcesso;
	}

	public void setNomeMPProcesso(String nomeMPProcesso) {
		if(nomeMPProcesso != null) this.nomeMPProcesso = nomeMPProcesso;
	}
	
	public boolean isResponsavelSessao()
	{
		if (isAnalistaPodeMovimentar()) return false;
		
		return this.isResponsavelSessao;
	}
	
	public void setIsResponsavelSessao(boolean isResponsavelSessao)
	{
		this.isResponsavelSessao = isResponsavelSessao;
	}
	
	public String getId_ServentiaResponsavel() {
		return this.id_ServentiaResponsavel;
	}

	public void setId_ServentiaResponsavel(String id_ServentiaResponsavel) {
		if(id_ServentiaResponsavel != null) this.id_ServentiaResponsavel = id_ServentiaResponsavel;
	}
	
	public String getAcordo() {
		return Acordo;
	}

	public void setAcordo(String valor) {
		if (valor!=null) {
			if (valor.equalsIgnoreCase("null")) {
				Acordo = "";			
			} else if (!valor .equalsIgnoreCase("")) {
				Acordo = valor;
			}	
		}
	}	
	
	public boolean isHouveAcordo() {
		return this.Acordo != null && this.Acordo.equalsIgnoreCase("1");
	}
	
	public boolean isNaoHouveAcordo() {
		return this.Acordo != null && this.Acordo.equalsIgnoreCase("0");
	}
	
	public String getValorAcordo() {
		if (this.ValorAcordo == null) return "";
		return ValorAcordo;
	}

	public void setValorAcordo(String valor) {
		if (valor!=null) {
			if (valor.equalsIgnoreCase("null")) {
				ValorAcordo = "";			
			} else if (!valor .equalsIgnoreCase("")) {
				ValorAcordo = valor;
			}	
		}
	}
	
	public String getId_ProcessoTipo()  {
		return Id_ProcessoTipo;
	}
	
	public void setId_ProcessoTipo(String valor ) {
		if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_ProcessoTipo = ""; ProcessoTipo = "";} else if (!valor.equalsIgnoreCase("")) Id_ProcessoTipo = valor;
	}
	
	public String getProcessoTipo()  {
		return ProcessoTipo;
	}
	
	public void setProcessoTipo(String valor ) {
		if (valor!=null) ProcessoTipo = valor;
	}
	
	public String getDescricaoPoloAtivo() {
		if (descricaoPoloAtivo == null || descricaoPoloAtivo.trim().length() == 0) return "Polo Ativo";
		return descricaoPoloAtivo;
	}

	public void setDescricaoPoloAtivo(String descricaoPoloAtivo) {
		this.descricaoPoloAtivo = descricaoPoloAtivo;
	}

	public String getDescricaoPoloPassivo() {
		if (descricaoPoloPassivo == null || descricaoPoloPassivo.trim().length() == 0) return "Polo Passivo";
		return descricaoPoloPassivo;
	}

	public void setDescricaoPoloPassivo(String descricaoPoloPassivo) {
		this.descricaoPoloPassivo = descricaoPoloPassivo;
	}
	
	public String getId_PendenciaVotoRelator() {
		return Id_PendenciaVotoRelator;
	}

	public void setId_PendenciaVotoRelator(String valor) {
		if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaVotoRelator = ""; } else if (!valor.equalsIgnoreCase("")) Id_PendenciaVotoRelator = valor;		
	}
	
	public String getId_PendenciaEmentaRelator() {
		return Id_PendenciaEmentaRelator;
	}

	public void setId_PendenciaEmentaRelator(String valor) {
		if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaEmentaRelator = ""; } else if (!valor.equalsIgnoreCase("")) Id_PendenciaEmentaRelator = valor;		
	}
	
	public String getId_PendenciaVotoRedator() {
		return Id_PendenciaVotoRedator;
	}

	public void setId_PendenciaVotoRedator(String valor) {
		if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaVotoRedator = ""; } else if (!valor.equalsIgnoreCase("")) Id_PendenciaVotoRedator = valor;		
	}
	
	public String getId_PendenciaEmentaRedator() {
		return Id_PendenciaEmentaRedator;
	}

	public void setId_PendenciaEmentaRedator(String valor) {
		if (valor!=null) if (valor.equalsIgnoreCase("null")) {Id_PendenciaEmentaRedator = ""; } else if (!valor.equalsIgnoreCase("")) Id_PendenciaEmentaRedator = valor;		
	}
	
	public String getPropriedades(){
		return "[Id_AudienciaProcesso:" + getId() + ";Id_Audiencia:" + getId_Audiencia() + ";AudienciaTipo:" + getAudienciaTipo() + ";Id_AudienciaProcessoStatus:" + getId_AudienciaProcessoStatus() + ";AudienciaProcessoStatus:" + getAudienciaProcessoStatus() + ";Id_ServentiaCargo:" + getId_ServentiaCargo() + ";ServentiaCargo:" + getServentiaCargo() + ";Id_Processo:" + getId_Processo() + ";ProcessoNumero:" + getProcessoNumero() + ";DataMovimentacao:" + getDataMovimentacao() + ";CodigoTemp:" + getCodigoTemp() + ";AudienciaTipoCodigo:" + getAudienciaTipoCodigo() + ";AudienciaProcessoStatusCodigo:" + getAudienciaProcessoStatusCodigo()+ ";AudienciaProcessoStatusAnalista:" + AudienciaProcessoStatusAnalista+ ";AudienciaProcessoStatusCodigoAnalista:" + AudienciaProcessoStatusCodigoAnalista + ";Id_ArquivoAta:" + Id_ArquivoAta + ";DataAudienciaOriginal:" + DataAudienciaOriginal + ";Id_ArquivoAtaSessaoIniciada:" + Id_ArquivoAtaSessaoIniciada + ";Id_ArquivoAtaSessaoAdiada:" + Id_ArquivoAtaSessaoAdiada + ";id_ServentiaCargoPresidente:" + id_ServentiaCargoPresidente + ";id_ServentiaMP:" + id_ServentiaMP + ";id_ServentiaCargoMP:" + id_ServentiaCargoMP + ";id_ServentiaCargoRedator:" + id_ServentiaCargoRedator  + ";acordo:" + this.getAcordo()  + ";valorAcordo:" + this.getValorAcordo()  + ";Id_ProcessoTipo:" + Id_ProcessoTipo + ";Id_PendenciaVoto:" + Id_PendenciaVotoRelator + ";Id_PendenciaEmenta:" + Id_PendenciaEmentaRelator + ";Id_PendenciaVotoRedator:" + Id_PendenciaVotoRedator + ";Id_PendenciaEmentaRedator:" + Id_PendenciaEmentaRedator + ";Id_Audi_Proc_Origem:" + Id_Audi_Proc_Origem + "]";
	}
	
	public boolean possuiVotoRelator() {
		if (getId_PendenciaVotoRelator() == null || getId_PendenciaVotoRelator().trim().length() == 0) return false;
		return true;
	}	
	
	public boolean possuiEmentaRelator() {
		if (getId_PendenciaEmentaRelator() == null || getId_PendenciaEmentaRelator().trim().length() == 0) return false;
		return true;
	}

	public boolean possuiVotoEEmentaRelator() {
		return possuiVotoRelator() && possuiEmentaRelator();
	}
	
	public boolean possuiVotoOuEmentaRelator() {
		return possuiVotoRelator() || possuiEmentaRelator();
	}
	
	public boolean possuiVotoRedator() {
		if (getId_PendenciaVotoRedator() == null || getId_PendenciaVotoRedator().trim().length() == 0) return false;
		return true;
	}	
	
	public boolean possuiEmentaRedator() {
		if (getId_PendenciaEmentaRedator() == null || getId_PendenciaEmentaRedator().trim().length() == 0) return false;
		return true;
	}

	public boolean possuiVotoEEmentaRedator() {
		return possuiVotoRedator() && possuiEmentaRedator();
	}
	
	public boolean possuiVotoOuEmentaRedator() {
		return possuiVotoRedator() || possuiEmentaRedator();
	}
	
	public boolean exibeOpcaoRetornarSessaoJulgamento() {
		return (Funcoes.StringToInt(getAudienciaProcessoStatusCodigo()) == AudienciaProcessoStatusDt.DESMARCAR_PAUTA ||
				Funcoes.StringToInt(getAudienciaProcessoStatusCodigo()) == AudienciaProcessoStatusDt.RETIRAR_PAUTA ||
			    Funcoes.StringToInt(getAudienciaProcessoStatusCodigo()) == AudienciaProcessoStatusDt.JULGAMENTO_ADIADO ||
			    Funcoes.StringToInt(getAudienciaProcessoStatusCodigo()) == AudienciaProcessoStatusDt.JULGAMENTO_INICIADO ||
			    Funcoes.StringToInt(getAudienciaProcessoStatusCodigo()) == AudienciaProcessoStatusDt.REMARCADA);
	}

	public String getAudienciaProcessoStatusCodigoTemp() {
		return AudienciaProcessoStatusCodigoTemp;
	}

	public void setAudienciaProcessoStatusCodigoTemp(String audienciaProcessoStatusCodigoTemp) {
		AudienciaProcessoStatusCodigoTemp = audienciaProcessoStatusCodigoTemp;
	}

	public boolean isVotosPrazoExpirado() {
		return votosPrazoExpirado;
	}

	public void setVotosPrazoExpirado(boolean votosPrazoExpirado) {
		this.votosPrazoExpirado = votosPrazoExpirado;
	}

	public boolean isPodeInserirExtrato() {
		return podeInserirExtrato;
	}

	public void setPodeInserirExtrato(boolean podeInserirExtrato) {
		this.podeInserirExtrato = podeInserirExtrato;
	}

	public boolean isPodeEncaminhar() {
		return podeEncaminhar;
	}

	public void setPodeEncaminhar(boolean podeEncaminhar) {
		this.podeEncaminhar = podeEncaminhar;
	}

	public boolean isPermiteSustentacaoOral() {
		return permiteSustentacaoOral;
	}

	public void setPermiteSustentacaoOral(boolean permiteSustentacaoOral) {
		this.permiteSustentacaoOral = permiteSustentacaoOral;
	}

	public String getAudienciaProcessoStatusTemp() {
		return AudienciaProcessoStatusTemp;
	}

	public void setAudienciaProcessoStatusTemp(String audienciaProcessoStatusTemp) {
		AudienciaProcessoStatusTemp = audienciaProcessoStatusTemp;
	}
	
	public boolean isPodeAdiarComPedidoSusOral() {
		return podeAdiarComPedidoSusOral;
	}

	public void setPodeAdiarComPedidoSusOral(boolean podeAdiarComPedidoSusOral) {
		this.podeAdiarComPedidoSusOral = podeAdiarComPedidoSusOral;
	}

	public boolean isPossuiPedidoDeferidoRelator() {
		return possuiPedidoDeferidoRelator;
	}

	public void setPossuiPedidoDeferidoRelator(boolean possuiPedidoDeferidoRelator) {
		this.possuiPedidoDeferidoRelator = possuiPedidoDeferidoRelator;
	}

	public boolean isPossuiPedidoIndeferidoRelator() {
		return possuiPedidoIndeferidoRelator;
	}

	public void setPossuiPedidoIndeferidoRelator(boolean possuiPedidoIndeferidoRelator) {
		this.possuiPedidoIndeferidoRelator = possuiPedidoIndeferidoRelator;
	}
	
	 // lrcampos - 11/10/2019 14:44 - Adicionar variavel
	public String getSituacaoPedidoSO() {
		return situacaoPedidoSO;
	}

	 // lrcampos - 11/10/2019 14:44 - Adicionar variavel
	public void setSituacaoPedidoSO(String situacaoPedidoSO) {
		this.situacaoPedidoSO = situacaoPedidoSO;
	}

	 // lrcampos - 11/10/2019 14:44 - Adicionar variavel
	public boolean isAdiadaPorSustentacaoOral() {
		return adiadaPorSustentacaoOral;
	}

	 // lrcampos - 11/10/2019 14:44 - Adicionar variavel
	public void setAdiadaPorSustentacaoOral(boolean adiadaPorSustentacaoOral) {
		this.adiadaPorSustentacaoOral = adiadaPorSustentacaoOral;
	}
	
	 // lrcampos - 19/03/2020 16:04 - Adicionar variavel
	public boolean isOriginadaSessaoVirtual() {
		return originadaSessaoVirtual;
	}

	 // lrcampos - 19/03/2020 16:04 - Adicionar variavel
	public void setOriginadaSessaoVirtual(boolean originadaSessaoVirtual) {
		this.originadaSessaoVirtual = originadaSessaoVirtual;
	}
	
	public boolean isPossuiDeferimentoAutomatico() {
		return possuiDeferimentoAutomatico;
	}

	public void setPossuiDeferimentoAutomatico(boolean possuiDeferimentoAutomatico) {
		this.possuiDeferimentoAutomatico = possuiDeferimentoAutomatico;
	}
	
	public boolean extratoDaAtaDeJulgamentoJaFoiInserido() {
		return (this.getId_ArquivoAta() != null && this.getId_ArquivoAta().trim().length() > 0);
	}	
	
	public boolean podeAnalisarOuPreanalisarVotoEmenta(String grupoTipoCodigo) {
		return (extratoDaAtaDeJulgamentoJaFoiInserido() && GrupoTipoDt.podeAnalisarOuPreanalisarVotoEmenta(grupoTipoCodigo));
	}

	public void setId_Audi_Proc_Origem(String  Id_Audi_Proc_Origem) {
		if ( Id_Audi_Proc_Origem!=null) this. Id_Audi_Proc_Origem =  Id_Audi_Proc_Origem;
}
	
	public String getId_Audi_Proc_Origem() {
		return  Id_Audi_Proc_Origem;
	}

}
