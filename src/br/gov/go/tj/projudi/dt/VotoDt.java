package br.gov.go.tj.projudi.dt;

import java.util.Date;
import java.util.Optional;

import br.gov.go.tj.projudi.dt.Dados;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.utils.Funcoes;

public class VotoDt extends Dados {
	public static final int QUANTIDADE_VOTANTES = 2;

	private static final long serialVersionUID = 304268892626936752L;
	private String id;
	private String votoTexto;
	private String idVotoTipo;
	private String votoTipoCodigo;
	private String votoTipo;
	private String idServentiaCargo;
	private String nomeVotante;
	private String ordemVotante;
	private String votanteTipoCodigo;
	// jvosantos - 04/06/2019 09:46 - Adicionar id do status vencido
	private String idAudienciaProcessoStatusVencido;

	private PendenciaDt pendenciaDt;
	private ArquivoDt arquivo;
	private ArquivoDt ementa;
	private ArquivoDt configuracao;
	private AudienciaProcessoDt audienciaProcessoDt;

	private Date dataVoto; // jvosantos - 09/07/2019 13:53 - Adicionar variável de data do Voto

	private boolean votoAtivo; 	// jvosantos - 06/01/2020 17:02 - Criar variável de voto ativo
	
	public VotoDt() {
		votoTexto = "";
		idVotoTipo = "";
		votoTipoCodigo = "";
		votoTipo = "";
		nomeVotante = "";
		ordemVotante = "";
		setId("");
	}
	
	public String getTexto() {
		return votoTexto;
	}

	public void setVotoTexto(String votoTexto) {
		this.votoTexto = votoTexto;
	}

	public String getIdPendencia() {
		return Optional.ofNullable(pendenciaDt).map(PendenciaDt::getId).orElse("");
	}
	
	public String getIdProcesso() {
		return Optional.ofNullable(pendenciaDt).map(PendenciaDt::getId_Processo).orElse("");
	}


	public String getIdVotoTipo() {
		return idVotoTipo;
	}

	public void setIdVotoTipo(String idVotoTipo) {
		this.idVotoTipo = idVotoTipo;
	}
	
	public void setIdVotoTipo(int idVotoTipo) {
		this.idVotoTipo = String.valueOf(idVotoTipo);
	}

	public String getVotoTipoCodigo() {
		return votoTipoCodigo;
	}
	
	public int getVotoCodigoInt() {
		return Funcoes.StringToInt(votoTipoCodigo);
	}

	public void setVotoTipoCodigo(String votoCodigo) {
		this.votoTipoCodigo = votoCodigo;
	}

	public String getVotoTipo() {
		return votoTipo;
	}

	public void setVotoTipo(String voto) {
		this.votoTipo = voto;
	}

	public PendenciaDt getPendenciaDt() {
		return pendenciaDt;
	}

	public void setPendenciaDt(PendenciaDt pendenciaDt) {
		this.pendenciaDt = pendenciaDt;
	}

	public String getNomeVotante() {
		return nomeVotante;
	}

	public void setNomeVotante(String nomeVotante) {
		this.nomeVotante = nomeVotante;
	}


	public ArquivoDt getArquivo() {
		return arquivo;
	}


	public void setArquivo(ArquivoDt arquivo) {
		this.arquivo = arquivo;
	}


	public AudienciaProcessoDt getAudienciaProcessoDt() {
		return audienciaProcessoDt;
	}


	public void setAudienciaProcessoDt(AudienciaProcessoDt audienciaProcessoDt) {
		this.audienciaProcessoDt = audienciaProcessoDt;
	}


	public String getIdAudienciaProcesso() {
		return Optional.ofNullable(audienciaProcessoDt).map(AudienciaProcessoDt::getId).orElse("");
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	public String getOrdemVotante() {
		return ordemVotante;
	}

	public void setOrdemVotante(String ordemVotante) {
		this.ordemVotante = ordemVotante;
	}

	public String getIdServentiaCargo() {
		return idServentiaCargo;
	}

	public void setIdServentiaCargo(String idServentiaCargo) {
		this.idServentiaCargo = idServentiaCargo;
	}

	public ArquivoDt getEmenta() {
		return ementa;
	}

	public void setEmenta(ArquivoDt ementa) {
		this.ementa = ementa;
	}

	public ArquivoDt getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(ArquivoDt configuracao) {
		this.configuracao = configuracao;
	}

	public boolean isAcompanhaRelator() {
		return VotoTipoDt.acompanha(getVotoCodigoInt());
	}
	
	public boolean isDivergeRelator() {
		return VotoTipoDt.diverge(getVotoCodigoInt());
	}
	
	public boolean isExpirado() {
		return getVotoCodigoInt() == VotoTipoDt.PRAZO_EXPIRADO;
	}
	
	// jvosantos - 04/06/2019 09:46 - Adicionar id do status vencido
	public String getIdAudienciaProcessoStatusVencido() {
		return idAudienciaProcessoStatusVencido;
	}

	public void setIdAudienciaProcessoStatusVencido(String idAudienciaProcessoStatusVencido) {
		this.idAudienciaProcessoStatusVencido = idAudienciaProcessoStatusVencido;
	}

	// jvosantos - 09/07/2019 13:53 - Adicionar váriavel de data do Voto
	public void setDataVoto(Date dateTime) {
		this.dataVoto = dateTime;
	}
	
	// jvosantos - 09/07/2019 13:53 - Adicionar váriavel de data do Voto
	public Date getDataVoto() { return this.dataVoto; }
	
	public static boolean isVoto(int votoTipoCodigo) {
		// lrcampos - 29/10/2019 16:03 - Adicionando prazo expirado como um tipo de voto que não é voto 
		// jvosantos - 08/08/2019 15:08 - Adicionar novos tipos de votos que não são votos
		return votoTipoCodigo != VotoTipoDt.IMPEDIDO && votoTipoCodigo != VotoTipoDt.SUSPEICAO && votoTipoCodigo != VotoTipoDt.OBSERVACAO && votoTipoCodigo != VotoTipoDt.VERIFICAR_RESULTADO_VOTACAO && votoTipoCodigo != VotoTipoDt.PROCLAMACAO_DECISAO && votoTipoCodigo != VotoTipoDt.PRAZO_EXPIRADO && votoTipoCodigo != VotoTipoDt.ERRO_MATERIAL && votoTipoCodigo != VotoTipoDt.JULGAMENTO_REINICIADO && votoTipoCodigo != VotoTipoDt.ANALISE_ERRO_MATERIAL;
	}
	
	public boolean isVoto() {
		return isVoto(getVotoCodigoInt());
	}
	
	public static int getValorVoto(int codigo) {
		if (codigo == VotoTipoDt.ACOMPANHA_RELATOR || codigo == VotoTipoDt.ACOMPANHA_RELATOR_RESSALVA) {
			return 1;
		} else if (codigo == VotoTipoDt.ACOMPANHA_DIVERGENCIA || codigo == VotoTipoDt.DIVERGE) {
			return -1;
		}
		return 0;
	}
	
	public int getValorVoto() {
		return getValorVoto(getVotoCodigoInt());
	}

	public String getVotanteTipoCodigo() {
		return votanteTipoCodigo;
	}
	
	public int getVotanteTipoCodigoInt() {
		return Funcoes.StringToInt(votanteTipoCodigo);
	}

	public void setVotanteTipoCodigo(String votanteTipoCodigo) {
		this.votanteTipoCodigo = votanteTipoCodigo;
	}

	// jvosantos - 06/01/2020 17:02 - Criar variável de voto ativo
	public boolean isAtivo() {
		return this.votoAtivo;
	}

	// jvosantos - 06/01/2020 17:02 - Criar variável de voto ativo
	public void setAtivo(boolean ativo) {
		this.votoAtivo = ativo;
	}
}
