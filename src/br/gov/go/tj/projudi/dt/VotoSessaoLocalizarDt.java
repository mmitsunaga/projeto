package br.gov.go.tj.projudi.dt;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.utils.MensagemException;

public class VotoSessaoLocalizarDt {

	private String idPendencia;
	private String idProcesso;
	private String idAudienciaProcesso;
	private String processoNumero;
	private String prazoVotacao;
	private String tempoRestante;
	private String dataSessao;
	private String nomeRelator;
	private String resultado;
	private boolean podeVotar;
	private boolean existeAudiProc; // Tmp
	private String audiProcStatus; // Tmp
	private String audienciaProcessoStatusCodigo; // Tmp
	private boolean temImpedimento;
	private boolean expirado;
	private String nomeAdvPedidoSusOral;
	private String solicitanteSustentacaOral;
	private PendenciaDt pendenciaDt;
	private ProcessoDt processoDt;
	private String hashUsuario;
	private ClassificadorDt classificadorDt;
	ArquivoDt arquivoVoto;
	ArquivoDt arquivoEmenta;
	ArquivoDt arquivoJulgamento;
	private ArquivoDt arquivoVotoRelator; // jvosantos - 07/10/2019 15:05 - Arquivo para armazenar o voto do relator quando o voto do votante estive no arquivoVoto

	private List<String> votosRelacionados;
	private String classeProcesso; // mrbatista - 11/10/2019 14:44 - Adicionar variavel

	public VotoSessaoLocalizarDt() {
		idPendencia = "";
		idProcesso = "";
		idAudienciaProcesso = "";
		processoNumero = "";
		dataSessao = "";
		setPrazoVotacao("");
		tempoRestante = "";
		
	}
	
	public String getIdClassificador() {
		return (pendenciaDt != null && StringUtils.isNotEmpty(pendenciaDt.getId_Classificador()))
				? pendenciaDt.getId_Classificador()
				: ((processoDt != null && StringUtils.isNotEmpty(processoDt.getId_Classificador())) ? processoDt.getId_Classificador() : "-1");
	}
	
	public String getClassificador() {
		return (pendenciaDt != null && StringUtils.isNotEmpty(pendenciaDt.getClassificador()))
				? pendenciaDt.getClassificador()
				: ((processoDt != null && StringUtils.isNotEmpty(processoDt.getClassificador())) ? processoDt.getClassificador() : "Sem Classificador");
	}

	public String getIdPendencia() {
		return idPendencia;
	}

	public void setIdPendencia(String idPendencia) {
		this.idPendencia = idPendencia;
	}

	public String getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(String idProcesso) {
		this.idProcesso = idProcesso;
	}

	public String getIdAudienciaProcesso() {
		return idAudienciaProcesso;
	}

	public void setIdAudienciaProcesso(String idAudienciaProcesso) {
		this.idAudienciaProcesso = idAudienciaProcesso;

		if(StringUtils.isEmpty(idAudienciaProcesso)) {
			this.setExisteAudiProc(false);
			return;
		}
		
		try {
			AudienciaProcessoDt d = (new AudienciaProcessoNe()).consultarId(idAudienciaProcesso);
			
			if(d == null)
				throw new MensagemException("Não foi encontrado AudienciaProcesso com o id = \""+idAudienciaProcesso+"\".");
			
			this.setExisteAudiProc(d != null);
			this.setAudienciaProcessoStatus((d != null) ? d.getAudienciaProcessoStatus() : "");
			this.setAudienciaProcessoStatusCodigo((d != null) ? d.getAudienciaProcessoStatusCodigo() : "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = StringUtils.defaultIfEmpty(processoNumero, "");
	}

	public List<String> getVotosRelacionados() {
		return votosRelacionados;
	}

	public void setVotosRelacionados(List<String> votosRelacionados) {
		this.votosRelacionados = votosRelacionados;
	}

	public String getDataSessao() {
		return dataSessao;
	}

	public void setDataSessao(String dataAgendada) {
		this.dataSessao = dataAgendada;
	}

	public String getPrazoVotacao() {
		return prazoVotacao;
	}

	public  void setPrazoVotacao(String prazoVotacao) {
		this.prazoVotacao = StringUtils.defaultIfEmpty(prazoVotacao, "");
	}

	public ArquivoDt getArquivoVoto() {
		return arquivoVoto;
	}

	public void setArquivoVoto(ArquivoDt arquivoVoto) {
		this.arquivoVoto = arquivoVoto;
	}

	public ArquivoDt getArquivoEmenta() {
		return arquivoEmenta;
	}

	public void setArquivoEmenta(ArquivoDt arquivoEmenta) {
		this.arquivoEmenta = arquivoEmenta;
	}

	public ArquivoDt getArquivoJulgamento() {
		return arquivoJulgamento;
	}

	public void setArquivoJulgamento(ArquivoDt arquivoJulgamento) {
		this.arquivoJulgamento = arquivoJulgamento;
	}

	public String getTempoRestante() {
		return tempoRestante;
	}

	public void setTempoRestante(String tempoRestante) {
		this.tempoRestante = tempoRestante;
	}

	public String getNomeRelator() {
		return nomeRelator;
	}

	public void setNomeRelator(String nomeRelator) {
		this.nomeRelator = nomeRelator;
	}

	public boolean isPodeVotar() {
		return podeVotar;
	}

	public void setPodeVotar(boolean podeVotar) {
		this.podeVotar = podeVotar;
	}

	public String getNomeAdvPedidoSusOral() {
		return nomeAdvPedidoSusOral;
	}

	public void setNomeAdvPedidoSusOral(String nomeAdvPedidoSusOral) {
		this.nomeAdvPedidoSusOral = nomeAdvPedidoSusOral;
	}

	public boolean isTemImpedimento() {
		return temImpedimento;
	}

	public void setTemImpedimento(boolean temImpedimento) {
		this.temImpedimento = temImpedimento;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public boolean isExpirado() {
		return expirado;
	}

	public void setExpirado(boolean expirado) {
		this.expirado = expirado;
	}

	public boolean isExisteAudiProc() {
		return existeAudiProc;
	}

	public void setExisteAudiProc(boolean existeAudiProc) {
		this.existeAudiProc = existeAudiProc;
	}

	public String getAudienciaProcessoStatus() {
		return audiProcStatus;
	}

	public void setAudienciaProcessoStatus(String audiProcStatus) {
		this.audiProcStatus = audiProcStatus;
	}

	public String getAudienciaProcessoStatusCodigo() {
		return audienciaProcessoStatusCodigo;
	}

	public void setAudienciaProcessoStatusCodigo(String audienciaProcessoStatusCodigo) {
		this.audienciaProcessoStatusCodigo = audienciaProcessoStatusCodigo;
	}

	// jvosantos - 07/10/2019 15:05 - Arquivo para armazenar o voto do relator quando o voto do votante estive no arquivoVoto
	public ArquivoDt getArquivoVotoRelator() {
		return arquivoVotoRelator;
	
	}

	// jvosantos - 07/10/2019 15:05 - Arquivo para armazenar o voto do relator quando o voto do votante estive no arquivoVoto
	public void setArquivoVotoRelator(ArquivoDt arquivoVotoRelator) {
		this.arquivoVotoRelator = arquivoVotoRelator;
	}

	public PendenciaDt getPendenciaDt() {
		return pendenciaDt;
	}

	public void setPendenciaDt(PendenciaDt pendenciaDt) {
		this.pendenciaDt = pendenciaDt;
	}

	public ProcessoDt getProcessoDt() {
		return processoDt;
	}

	public void setProcessoDt(ProcessoDt processoDt) {
		this.processoDt = processoDt;
	}

	public String getHashUsuario() {
		return hashUsuario;
	}

	public void setHashUsuario(String hashUsuario) {
		this.hashUsuario = hashUsuario;
	}

	public ClassificadorDt getClassificadorDt() {
		return classificadorDt;
	}

	public void setClassificadorDt(ClassificadorDt classificadorDt) {
		this.classificadorDt = classificadorDt;
	}
	
	// mrbatista - 11/10/2019 14:44 - Adicionar variavel
	public String getClasseProcesso() {
		 if (classeProcesso == null)  return "";
		
		return  classeProcesso ;
	}

	// mrbatista - 11/10/2019 14:44 - Adicionar variavel
	public void setClasseProcesso(String classeProcesso) {
		this.classeProcesso = classeProcesso;
	}

	public String getSolicitanteSustentacaOral() {
		return solicitanteSustentacaOral;
	}

	public void setSolicitanteSustentacaOral(String solicitanteSustentacaOral) {
		this.solicitanteSustentacaOral = solicitanteSustentacaOral;
	}
	
}