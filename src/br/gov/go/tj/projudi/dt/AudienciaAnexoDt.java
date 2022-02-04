package br.gov.go.tj.projudi.dt;

import com.keteracel.auth.ApacheAuthToken;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

public class AudienciaAnexoDt  extends Dados {

	private static final long serialVersionUID = 4351952355333726009L;
	
	private String numeroProcessoCompleto;
	private TJDataHora dataHoraDaAudiencia;
	private String CaminhoCompletoArquivo;
	private String DisplayNomeArquivo;
	private String Id;
	
	public AudienciaAnexoDt() {}
		
	public AudienciaAnexoDt(String numeroProcessoCompleto, TJDataHora dataHoraDaAudiencia)
	{
		this.numeroProcessoCompleto = numeroProcessoCompleto;
		this.dataHoraDaAudiencia = dataHoraDaAudiencia;
		this.DisplayNomeArquivo = "";
	}
	
	public String getNumeroProcessoCompletoDt() {
		return numeroProcessoCompleto;
	}

	public void setNumeroProcessoCompletoDt(String numeroProcessoCompleto) {
		this.numeroProcessoCompleto = numeroProcessoCompleto;
	}

	public void setDataHoraDaAudiencia(TJDataHora dataHoraDaAudiencia) {
		this.dataHoraDaAudiencia = dataHoraDaAudiencia;
	}

	public TJDataHora getDataHoraDaAudiencia() {
		return dataHoraDaAudiencia;
	}

	public String getNomeArquivo() {
		return CaminhoCompletoArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		CaminhoCompletoArquivo = nomeArquivo;
	}
	
	public String getDisplayNomeArquivo() {
		return DisplayNomeArquivo;
	}

	public void setDisplayNomeArquivo(String displayNomeArquivo) {
		DisplayNomeArquivo = displayNomeArquivo;
	}
	
	@Override
	public void setId(String id) {
		Id = id;	
	}

	@Override
	public String getId() {
		return Id;
	}
	
	public String getUrlAnexoDownloadCripto() throws MensagemException {
		String parteURL = this.CaminhoCompletoArquivo.replaceAll("[\\\\]", "/").replaceAll("//", "/");
		
		if (!parteURL.endsWith("/")) parteURL += "/";		
		parteURL += "s/";
		parteURL += obtenhaNomeArquivoReproducao();		
		
		parteURL = new ApacheAuthToken(AudienciaDRSDt.SENHA_APACHE_VIDEO_RECIBO, AudienciaDRSDt.ALIAS_APACHE_VIDEO_RECIBO, parteURL).toUriString();
		return "https://" + AudienciaDRSDt.IP_APACHE_VIDEO_RECIBO + "/" + parteURL;
	}
	
	private String obtenhaNomeArquivoReproducao() throws MensagemException
	{
		StringBuffer linkParaAcessoAudienciaPublicada = new StringBuffer();	
		
		linkParaAcessoAudienciaPublicada.append(new NumeroProcessoDt(this.numeroProcessoCompleto).getNumeroCompletoProcesso());		
		linkParaAcessoAudienciaPublicada.append("_");
		linkParaAcessoAudienciaPublicada.append(this.dataHoraDaAudiencia.getDataHoraFormatadayyyyMMddHHmm());
		linkParaAcessoAudienciaPublicada.append("_");		
		/*String displayNomeArquivoTratado = this.DisplayNomeArquivo;
		if (displayNomeArquivoTratado != null) {
			if (displayNomeArquivoTratado.contains("'")) {
				displayNomeArquivoTratado = displayNomeArquivoTratado.replace("'", "");
			}
			if (displayNomeArquivoTratado.contains("\"")) {
				displayNomeArquivoTratado = displayNomeArquivoTratado.replace("\"", "");
			}
		}		
		linkParaAcessoAudienciaPublicada.append(displayNomeArquivoTratado);*/
		linkParaAcessoAudienciaPublicada.append(Funcoes.limparNomeArquivo(this.DisplayNomeArquivo));
		
		return linkParaAcessoAudienciaPublicada.toString();				
	}		
}
