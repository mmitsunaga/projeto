package br.gov.go.tj.projudi.dt;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

public class MidiaPublicadaDt implements Serializable {
	
	private static final long serialVersionUID = -6953712441106276347L;

	// CÓDIGO DE SEGURANÇA DA CLASSE
	public static final int CodigoPermissao = 347;
	
	public static final String PrefixoArquivoTemporario = "cephTemp_";
	
	private String Id_UsuarioLog;
	private String IpComputadorLog;
	private ProcessoDt processo;
	private TJDataHora dataHora;
	private String complemento;
	private List<MidiaPublicadaArquivoDt> midiasPublicadas;
	
	public MidiaPublicadaDt() {
		this.midiasPublicadas = new ArrayList<>();
		this.complemento = "";
	}
		
	public void setDataHora(TJDataHora dataHora) {
		if (dataHora != null) {
			this.dataHora = dataHora;	
		}		
	}
	
	public void LimparDataHora() {
		this.dataHora = null;		
	}

	public TJDataHora getDataHora() {
		return dataHora;
	}

	public String getProcessoNumero() {
		if (this.processo == null) return "";
		return this.processo.getProcessoNumeroCompleto();
	}

	public void setProcesso(ProcessoDt processo) {
		if (processo != null) {
			this.processo = processo;	
		}		
	}
	
	public ProcessoDt getProcesso() {
		return this.processo;
	}
	
	public void LimparMidias() {
		this.getListaMidiasPublicadas().clear();		
	}
	
	public MidiaPublicadaArquivoDt adicioneMidiaPublicada(String caminhoTemporario, String nomeArquivoOriginal, String contentType, long tamanhoArquivo) {
		MidiaPublicadaArquivoDt audienciaGravadaArquivo = new MidiaPublicadaArquivoDt();
		
		String nomeTratado = Funcoes.limparNomeArquivo(nomeArquivoOriginal);
		if (Funcoes.isStringVazia(nomeTratado)) return null;
		
		if (!caminhoTemporario.endsWith(File.separator)) caminhoTemporario += File.separator;
		audienciaGravadaArquivo.setNomeArquivoOriginal(nomeArquivoOriginal);
		audienciaGravadaArquivo.setCaminhoCompletoArquivoTemp(caminhoTemporario + MidiaPublicadaDt.PrefixoArquivoTemporario + nomeTratado);
		audienciaGravadaArquivo.setNomeArquivo(nomeTratado);
		audienciaGravadaArquivo.setContentType(contentType);
		audienciaGravadaArquivo.setTamanhoArquivo(tamanhoArquivo);
		
		this.adicioneMidiaPublicada(audienciaGravadaArquivo);
		
		return audienciaGravadaArquivo;
	}
	
	public void adicioneMidiaPublicada(MidiaPublicadaArquivoDt audienciaPublicadaArquivo) {
		this.getListaMidiasPublicadas().add(audienciaPublicadaArquivo);
	}
	
	public MidiaPublicadaArquivoDt getMidiaPublicadaArquivo(String nomeArquivo) {
		String nomeTratado = Funcoes.limparNomeArquivo(nomeArquivo);
		if (Funcoes.isStringVazia(nomeTratado)) return null;
		
		for(MidiaPublicadaArquivoDt midiaArquivo : this.getListaMidiasPublicadas()) {
			if (midiaArquivo.getNomeArquivo().equalsIgnoreCase(nomeTratado)) {
				return midiaArquivo;
			}
		}
		
		return null;
	}
	
	public synchronized List<MidiaPublicadaArquivoDt> getListaMidiasPublicadas() {
		return this.midiasPublicadas;
	}
	
	public String getPropriedades(){
		return "[ProcessoNumero:" + getProcessoNumero() + ";Data:" + dataHora.getDataFormatadaddMMyyyyHHmmss() + "]";
	}
	
	@Override
	public boolean equals(Object midiaPublicada) {		
		if (midiaPublicada == null || ! (midiaPublicada instanceof MidiaPublicadaDt)) return false; 
		
		MidiaPublicadaDt audienciaDRSDt = (MidiaPublicadaDt) midiaPublicada;
		
		return audienciaDRSDt.getPropriedades().equalsIgnoreCase(this.getPropriedades());
	}

	public String getId_UsuarioLog() {
		return Id_UsuarioLog;
	}

	public void setId_UsuarioLog(String id_UsuarioLog) {
		Id_UsuarioLog = id_UsuarioLog;
	}

	public String getIpComputadorLog() {
		return IpComputadorLog;
	}

	public void setIpComputadorLog(String ipComputadorLog) {
		IpComputadorLog = ipComputadorLog;
	}
	
	public String getComplemento() {
		if (this.complemento == null) return "";
		return this.complemento;
	}

	public void setComplemento(String complemento) {
		if (complemento != null) this.complemento = complemento;
	}
	
	public String getComplementoMovimentacao() {
		String complementoMovimentacao = String.format(MovimentacaoTipoDt.COMPLEMENTO_MIDIA_PUBLICADA, 
		                                               this.getDataHora().getDataFormatadaddMMyyyyHHmm());
		
		if (this.complemento != null && this.complemento.length() > 0) {
			complementoMovimentacao += " - ";
			complementoMovimentacao += this.complemento;
		}
		
		return complementoMovimentacao;
	}
}
