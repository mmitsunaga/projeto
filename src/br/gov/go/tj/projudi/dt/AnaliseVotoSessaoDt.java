package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class AnaliseVotoSessaoDt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7790868888865715591L;
	private PendenciaArquivoDt votoRelator;
	private PendenciaArquivoDt ementaRelator;

	private VotoDt voto;
	private String textoVoto;
	private List<VotoDt> votos;
	private String idArquivoTipo;
	private String nomeArquivo;
	private ArquivoTipoDt arquivoTipo;
	private boolean aguardandoAssinatura;
	private boolean renovarVoto; // jvosantos - 03/06/2019 17:12 - Adicionar variavel para saber se é uma renovação de voto
	private String nomeRelator;
	
	private String capaDoProcesso; // jvosantos - 02/07/2019 14:52 - Flag para saber se veio da capa do processo
	
	public AnaliseVotoSessaoDt() {
		voto = new VotoDt();
		idArquivoTipo = "";
		nomeArquivo = "";
		textoVoto = "";
		renovarVoto = false; // jvosantos - 03/06/2019 17:12 - Adicionar variavel para saber se é uma renovação de voto
	}
	
	public String getIdProcesso() {
		return Optional.ofNullable(voto).map(VotoDt::getIdProcesso).orElse("");
	}
	
	public VotoDt getVoto() {
		return voto;
	}

	public void setVoto(VotoDt voto) {
		this.voto = voto;
	}

	public PendenciaArquivoDt getVotoRelator() {
		return votoRelator;
	}

	public void setVotoRelator(PendenciaArquivoDt textoVotoRelator) {
		this.votoRelator = textoVotoRelator;
	}

	public PendenciaArquivoDt getEmentaRelator() {
		return ementaRelator;
	}

	public void setEmentaRelator(PendenciaArquivoDt textoEmentaRelator) {
		this.ementaRelator = textoEmentaRelator;
	}

	public String getIdAudienciaProcesso() {
		if(voto != null) {
			return voto.getIdAudienciaProcesso();
		}
		return "";
	}

	public ProcessoDt getProcessoDt() {
		return Optional.ofNullable(voto)
				.flatMap((voto) -> Optional.ofNullable(voto.getAudienciaProcessoDt()))
				.map(AudienciaProcessoDt::getProcessoDt).orElse(null);
	}

	public List<VotoDt> getVotos() {
		return votos;
	}

	public void setVotos(List<VotoDt> votos) {
		this.votos = votos;
	}

	public String getTextoVoto() {
		return textoVoto;
	}

	public void setTextoVoto(String textoVoto) {
		this.textoVoto = textoVoto;
	}

	public String getIdArquivoTipo() {
		return idArquivoTipo;
	}

	public void setIdArquivoTipo(String idArquivoTipo) {
		this.idArquivoTipo = idArquivoTipo;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public ArquivoTipoDt getArquivoTipo() {
		return arquivoTipo;
	}

	public void setArquivoTipo(ArquivoTipoDt arquivoTipo) {
		this.arquivoTipo = arquivoTipo;
	}

	public boolean isAguardandoAssinatura() {
		return aguardandoAssinatura;
	}

	public void setAguardandoAssinatura(boolean aguardandoAssinatura) {
		this.aguardandoAssinatura = aguardandoAssinatura;
	}

	public String getNomeRelator() {
		return nomeRelator;
	}

	public void setNomeRelator(String nomeRelator) {
		this.nomeRelator = nomeRelator;
	}

	// jvosantos - 03/06/2019 17:12 - Adicionar variavel para saber se é uma renovação de voto
	public boolean isRenovarVoto() {
		return renovarVoto;
	}

	public void setRenovarVoto(boolean renovarVoto) {
		this.renovarVoto = renovarVoto;
	}

	// jvosantos - 02/07/2019 14:52 - Flag para saber se veio da capa do processo
	public String getCapaDoProcesso() {
		return capaDoProcesso;
	}
	
	// jvosantos - 02/07/2019 14:52 - Flag para saber se veio da capa do processo
	public void setCapaDoProcesso(String capaDoProcesso) {
		this.capaDoProcesso = capaDoProcesso;
	}

}
