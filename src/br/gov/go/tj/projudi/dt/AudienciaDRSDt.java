package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.keteracel.auth.ApacheAuthToken;

import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class AudienciaDRSDt extends Dados implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2861682532423984446L;
	
	// CÓDIGO DE SEGURANÇA DA CLASSE
	public static final int CodigoPermissao = 929;
	
	public static final String IP_APACHE_VIDEO_RECIBO = "drs.tjgo.jus.br";
	public static final String SENHA_APACHE_VIDEO_RECIBO = "$ZgScNs:v*}9*n6?";
	public static final String ALIAS_APACHE_VIDEO_RECIBO = "3x";
	
	private String processoNumero;
	private TJDataHora dataHoraDaAudiencia;
	private long IdAudiencia;
	private String NomeArquivo;
	private String IndicesDepoimento;
	private boolean ehDRS3;
	private int CodigoGravacao;
	private List<AudienciaAnexoDt> listaDeAnexos;
	
	public AudienciaDRSDt() {
		this.listaDeAnexos = new ArrayList<AudienciaAnexoDt>();
	}
	
	public void setDataHoraDaAudiencia(TJDataHora dataHoraDaAudiencia) {
		if (dataHoraDaAudiencia != null) {
			this.dataHoraDaAudiencia = dataHoraDaAudiencia;	
		}		
	}
	
	public void LimparDataHora() {
		this.dataHoraDaAudiencia = null;		
	}

	public TJDataHora getDataHoraDaAudiencia() {
		return dataHoraDaAudiencia;
	}

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String processoNumero) {
		if (processoNumero != null) {
			this.processoNumero = processoNumero;	
		}		
	}
	
	public String getPropriedades(){
		return "[ProcessoNumero:" + processoNumero + ";DataAudiencia:" + dataHoraDaAudiencia.getDataFormatadaddMMyyyyHHmmss() + "]";
	}
	
	@Override
	public boolean equals(Object audienciaDRSDtObj) {		
		if (audienciaDRSDtObj == null || ! (audienciaDRSDtObj instanceof AudienciaDRSDt)) return false; 
		
		AudienciaDRSDt audienciaDRSDt = (AudienciaDRSDt) audienciaDRSDtObj;
		
		return audienciaDRSDt.getPropriedades().equalsIgnoreCase(this.getPropriedades());
	}

	@Override
	public void setId(String id) {
		if (id == null) return;
		String[] dados = id.split(";");		
		if (dados == null || dados.length != 2) return;
		
		String[] dadosProcesso = dados[0].split(":");
		if (dadosProcesso == null || dadosProcesso.length != 2) return;
		
		String[] dadosDataHora = dados[1].split(":");
		if (dadosDataHora == null || dadosDataHora.length != 2) return;
		TJDataHora dataHoraAudiencia = new TJDataHora();
		try {
			dataHoraAudiencia.setDataHoraFormatadayyyyMMddHHmmss(dadosDataHora[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setProcessoNumero(dadosProcesso[1]);
		setDataHoraDaAudiencia(dataHoraAudiencia);
	}

	@Override
	public String getId() {
		return getPropriedades();		
	}
	
	public void copiar(AudienciaDRSDt objeto){
		//Id_AudienciaDRS = objeto.getId();
		processoNumero = objeto.getProcessoNumero();
		dataHoraDaAudiencia = objeto.getDataHoraDaAudiencia();
		CodigoTemp = objeto.getCodigoTemp();		
	}

	public void limpar(){
		processoNumero="";
		dataHoraDaAudiencia = new TJDataHora();
	}	
	
	public long getIdAudiencia() {
		return IdAudiencia;
	}

	public void setIdAudiencia(long idAudiencia) {
		IdAudiencia = idAudiencia;
	}
	
	public String getNomeArquivo() {
		return NomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		NomeArquivo = nomeArquivo;
	}
	
	public String getIndicesDepoimento() {
		return IndicesDepoimento;
	}

	public void setIndicesDepoimento(String indicesDepoimento) {
		IndicesDepoimento = indicesDepoimento;
	}
	
	public void setEhDRS3(boolean ehDRS3) {
		this.ehDRS3 = ehDRS3;
	}

	public boolean isEhDRS3() {
		return ehDRS3;
	}
	
	public int getCodigoGravacao() {
		return CodigoGravacao;
	}

	public void setCodigoGravacao(int codigoGravacao) {
		CodigoGravacao = codigoGravacao;
	}
	
	public void AdicioneAnexo(AudienciaAnexoDt anexo) {
		this.listaDeAnexos.add(anexo);
	}
	
	public List<AudienciaAnexoDt> getListaDeAnexos() {
		return this.listaDeAnexos;
	}
	
	public boolean possuiAnexos()
	{
		return this.listaDeAnexos != null && this.listaDeAnexos.size() > 0;
	}
	
	public String getUrlVideoCripto() throws MensagemException {
		return getUrlVideoCripto(false);
	}
	
	public String getUrlVideoDownloadCripto() throws MensagemException {
		return getUrlVideoCripto(true);
	}
	
	private String getUrlVideoCripto(boolean forcaDownload) throws MensagemException {
		String parteURL = "";
		if (ehDRS3) {
			parteURL = this.NomeArquivo.replaceAll("[\\\\]", "/").replaceAll("//", "/");
		} else {
			parteURL = this.NomeArquivo.replace("\\\\sv-drs-p00.tjgo.gov\\dados\\", "").replaceAll("[\\\\]", "/").replaceAll("//", "/");
		}
		//parteURL = new ApacheAuthToken("DRS", "drs", parteURL).toUriString();
		
		if (!parteURL.endsWith("/")) parteURL += "/";		
		if (forcaDownload) parteURL += "s/";
		else parteURL += "n/";		
		parteURL += obtenhaNomeArquivoReproducao(false);		
		
		parteURL = new ApacheAuthToken(SENHA_APACHE_VIDEO_RECIBO, ALIAS_APACHE_VIDEO_RECIBO, parteURL).toUriString();
		return "https://" + IP_APACHE_VIDEO_RECIBO + "/" + parteURL;
	}
	
	private String obtenhaNomeArquivoReproducao(boolean ehRecibo) throws MensagemException
	{
		StringBuffer linkParaAcessoAudienciaPublicada = new StringBuffer();	
		
		linkParaAcessoAudienciaPublicada.append(new NumeroProcessoDt(this.processoNumero).getNumeroCompletoProcesso());		
		linkParaAcessoAudienciaPublicada.append("_");
		linkParaAcessoAudienciaPublicada.append(this.dataHoraDaAudiencia.getDataHoraFormatadayyyyMMddHHmm());
		if (ehRecibo)
			linkParaAcessoAudienciaPublicada.append(".ReciboProjudi");
		else
			linkParaAcessoAudienciaPublicada.append(".wmv");			
		
		return linkParaAcessoAudienciaPublicada.toString();				
	}	
}
