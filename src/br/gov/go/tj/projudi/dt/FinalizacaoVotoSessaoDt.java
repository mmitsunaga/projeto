package br.gov.go.tj.projudi.dt;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import br.gov.go.tj.projudi.types.VotoTipo;
import br.gov.go.tj.projudi.sessaoVirtual.votacao.VotacaoUtils;

public class FinalizacaoVotoSessaoDt implements Serializable {
	public static final int NORMAL = 0, ADIAR = 1, RETIRAR = 2, CONVOCAR_VOTANTES = 3; // jvosantos - 14/06/2019 12:14 - Adicionar tipo de finalização
	
	private static final long serialVersionUID = 2037945699980545651L;
	private AudienciaProcessoDt audienciaProcesso;
	private PendenciaArquivoDt voto;
	private PendenciaArquivoDt ementa;
	private ArquivoTipoDt arquivoTipoVoto;	// Tipo antigo de Voto ("Relatorio do Voto")
	private ArquivoTipoDt arquivoTipoVotoNovo; // jvosantos - 18/07/2019 15:00 - Adicionar novo tipo de voto ("Voto")
	private ArquivoTipoDt arquivoTipoEmenta;
	
	private String idPendencia;
	private String conteudoArquivoVoto;
	private String conteudoArquivoEmenta;
	private String conteudoArquivoVotoOriginal; // jvosantos - 22/01/2020 18:35 - Armazenar Voto Original
	private String conteudoArquivoEmentaOriginal; // jvosantos - 22/01/2020 18:35 - Armazenar Ementa Original
	private String conteudoArquivoAdiado;
	private boolean divergente;
	private boolean julgamentoAdiado;
	private boolean retirarPauta;
	private boolean alterarVotoEmenta = false; // jvosantos - 22/01/2020 16:43 - Adicionar variavel para saber se é alterar voto ementa
	private boolean aguardandoAssinatura;
	// jvosantos - 27/09/2019 13:55 - Remover variavel podeFinalizar
	private boolean expirado;
	private boolean convocarVotantes; // jvosantos - 13/06/2019 16:25 - Adicionar variavel para saber se é convocar votantes
	private boolean verificarResultadoVotacao; // jvosantos - 11/07/2019 18:10 - Adicionar variavel para saber se veio da pendencia de verificar resultado da votação
	private boolean houvePedidoVista; // jvosantos - 27/09/2019 13:55 - Adicionar variavel para saber se houve um pedido de vista
	private boolean houveDoisTercosVotos; // jvosantos - 04/10/2019 14:06 - Adicionar variavel para saber se houve 2/3 dos votos
	private boolean acompanhaDivergencia;

	private String idVotanteConvocado1; // jvosantos - 13/06/2019 16:27 - Adicioanr variavel para armazenar o votante convocado
	private String idVotanteConvocado2; // jvosantos - 13/06/2019 16:27 - Adicioanr variavel para armazenar o votante convocado	

	// jvosantos - 02/07/2019 12:29 - Criar váriavel para verificar se veio da capa do processo 
	private String capaDoProcesso;
	
	private AudienciaProcessoStatusDt audienciaProcessoStatusOriginal; // jvosantos - 22/01/2020 18:35 - Armazenar Status da AUDI_PROC Original

	List<VotoDt> votos;

	private ResultadoVotacaoSessao resultado;
	
	public void setResultado(ResultadoVotacaoSessao resultado) {
		this.resultado = resultado;
	}
	
	public String getIdProcesso() {
		return Optional.ofNullable(audienciaProcesso).map(AudienciaProcessoDt::getId_Processo).orElse("");
	}
	
	public String getIdAudienciaProcesso() {
		return Optional.ofNullable(audienciaProcesso).map(AudienciaProcessoDt::getId).orElse("");
	}
	
	public String getProcessoNumero() {
		return Optional.ofNullable(audienciaProcesso).map(AudienciaProcessoDt::getProcessoNumero).orElse("");
	}
	
	// jvosantos - 04/06/2019 09:42 - Extrair parte do método que pega nome do votante divergente para ter acesso ao voto.
	public VotoDt getVotoDivergente() {
		Optional<VotoDt> votoDivergente = votos.stream().filter(voto -> voto.getVotoCodigoInt() == VotoTipo.DIVERGE.getValue()).findFirst();
		if(votoDivergente.isPresent()) 
			return votoDivergente.get();
		return null;
	}
	
	public String getNomeDivergente() {
		VotoDt votoDivergente = getVotoDivergente();
		if(votoDivergente != null)
			return votoDivergente.getNomeVotante();
		
		return "";
	}
	
	public String getServentiaCargoDivergente() {
		VotoDt votoDivergente = getVotoDivergente();
		if(votoDivergente != null)
			return votoDivergente.getIdServentiaCargo();
		
		return "";
	}

	public boolean temDivergencia() {
		return votos.stream().anyMatch(voto -> voto.getVotoCodigoInt() == VotoTipo.DIVERGE.getValue());
	}
	
	public PendenciaArquivoDt getVoto() {
		return voto;
	}
	public void setVoto(PendenciaArquivoDt voto) {
		this.voto = voto;
	}
	
	public PendenciaArquivoDt getEmenta() {
		return ementa;
	}
	public void setEmenta(PendenciaArquivoDt ementa) {
		this.ementa = ementa;
	}

	
	public ResultadoVotacaoSessao getResultado() {
		return resultado;
	}


	public AudienciaProcessoDt getAudienciaProcesso() {
		return audienciaProcesso;
	}


	public void setAudienciaProcesso(AudienciaProcessoDt audienciaProcesso) {
		this.audienciaProcesso = audienciaProcesso;
	}
	public List<VotoDt> getVotos() {
		return votos;
	}

	public void setVotos(List<VotoDt> votos) {
		this.votos = votos;
	}

	public String getConteudoArquivoVoto() {
		return conteudoArquivoVoto;
	}

	public void setConteudoArquivoVoto(String conteudoArquivoVoto) {
		this.conteudoArquivoVoto = conteudoArquivoVoto;
	}

	public String getConteudoArquivoVotoOriginal() {
		return conteudoArquivoVotoOriginal;
	}

	public void setConteudoArquivoVotoOriginal(String conteudoArquivoVotoOriginal) {
		this.conteudoArquivoVotoOriginal = conteudoArquivoVotoOriginal;
	}

	public String getConteudoArquivoEmenta() {
		return conteudoArquivoEmenta;
	}

	public void setConteudoArquivoEmenta(String conteudoArquivoEmenta) {
		this.conteudoArquivoEmenta = conteudoArquivoEmenta;
	}

	public String getConteudoArquivoEmentaOriginal() {
		return conteudoArquivoEmentaOriginal;
	}

	public void setConteudoArquivoEmentaOriginal(String conteudoArquivoEmentaOriginal) {
		this.conteudoArquivoEmentaOriginal = conteudoArquivoEmentaOriginal;
	}

	public String getIdPendencia() {
		return idPendencia;
	}

	public void setIdPendencia(String idPendencia) {
		this.idPendencia = idPendencia;
	}

	public String getConteudoArquivoAdiado() {
		return conteudoArquivoAdiado;
	}

	public void setConteudoArquivoAdiado(String conteudoArquivoAdiado) {
		this.conteudoArquivoAdiado = conteudoArquivoAdiado;
	}
	
	public boolean isDivergente() {
		return divergente;
	}

	public void setDivergente(boolean divergente) {
		this.divergente = divergente;
	}

	public boolean isJulgamentoAdiado() {
		return julgamentoAdiado;
	}

	public void setJulgamentoAdiado(boolean julgamentoAdiado) {
		this.julgamentoAdiado = julgamentoAdiado;
	}

	public boolean isAguardandoAssinatura() {
		return aguardandoAssinatura;
	}

	public void setAguardandoAssinatura(boolean aguardandoAssinatura) {
		this.aguardandoAssinatura = aguardandoAssinatura;
	}

	public ArquivoTipoDt getArquivoTipoEmenta() {
		return arquivoTipoEmenta;
	}

	public void setArquivoTipoEmenta(ArquivoTipoDt arquivoTipoEmenta) {
		this.arquivoTipoEmenta = arquivoTipoEmenta;
	}

	public ArquivoTipoDt getArquivoTipoVoto() {
		return arquivoTipoVoto;
	}

	public void setArquivoTipoVoto(ArquivoTipoDt arquivoTipoVoto) {
		this.arquivoTipoVoto = arquivoTipoVoto;
	}

	public int getTipoAnalise() {
		if(julgamentoAdiado) return ADIAR;
		if(retirarPauta) return RETIRAR;
		return NORMAL;
	}

	public void setTipoAnalise(int tipoAnalise) {
		julgamentoAdiado = tipoAnalise == ADIAR;
		retirarPauta = tipoAnalise == RETIRAR;
	}

	public boolean isRetirarPauta() {
		return retirarPauta;
	}
	
	public boolean isNormal() {
		return getTipoAnalise() == NORMAL;
	}

	public void setRetirarPauta(boolean retirarPauta) {
		this.retirarPauta = retirarPauta;
	}

	// jvosantos - 27/09/2019 13:55 - Remover variavel podeFinalizar
	public boolean isPodeFinalizar() {
		return !houvePedidoVista && !isExpirado();
	}
	
	public boolean isExpirado() {
		return expirado;
	}

	public void setExpirado(boolean expirado) {
		this.expirado = expirado;
	}

	// jvosantos - 02/07/2019 12:29 - Criar váriavel para verificar se veio da capa do processo 
	public String getCapaDoProcesso() {
		return capaDoProcesso;
	}
	// jvosantos - 13/06/2019 16:25 - Adicionar variavel para saber se é convocar votantes
	public boolean isConvocarVotantes() {
		return convocarVotantes;
	}

	// jvosantos - 02/07/2019 12:29 - Criar váriavel para verificar se veio da capa do processo 
	public void setCapaDoProcesso(String capaDoProcesso) {
		this.capaDoProcesso = capaDoProcesso;
	}
	
	// jvosantos - 13/06/2019 16:25 - Adicionar variavel para saber se é convocar votantes
	public void setConvocarVotantes(boolean convocarVotantes) {
		this.convocarVotantes = convocarVotantes;
	}

	// jvosantos - 13/06/2019 16:27 - Adicioanr variavel para armazenar o votante convocado
	public String getIdVotanteConvocado1() {
		return idVotanteConvocado1;
	}

	// jvosantos - 13/06/2019 16:27 - Adicioanr variavel para armazenar o votante convocado
	public void setIdVotanteConvocado1(String idVotanteConvocado1) {
		this.idVotanteConvocado1 = idVotanteConvocado1;
	}

	// jvosantos - 13/06/2019 16:27 - Adicioanr variavel para armazenar o votante convocado
	public String getIdVotanteConvocado2() {
		return idVotanteConvocado2;
	}

	// jvosantos - 13/06/2019 16:27 - Adicioanr variavel para armazenar o votante convocado
	public void setIdVotanteConvocado2(String idVotanteConvocado2) {
		this.idVotanteConvocado2 = idVotanteConvocado2;
	}

	// jvosantos - 11/07/2019 18:10 - Adicionar variavel para saber se veio da pendencia de verificar resultado da votação
	public boolean isVerificarResultadoVotacao() {
		return verificarResultadoVotacao;
	}

	// jvosantos - 11/07/2019 18:10 - Adicionar variavel para saber se veio da pendencia de verificar resultado da votação
	public void setVerificarResultadoVotacao(boolean verificarResultadoVotacao) {
		this.verificarResultadoVotacao = verificarResultadoVotacao;
	}

	// jvosantos - 18/07/2019 15:00 - Adicionar novo tipo de voto ("Voto")
	public ArquivoTipoDt getArquivoTipoVotoNovo() {
		return arquivoTipoVotoNovo;
	}

	// jvosantos - 18/07/2019 15:00 - Adicionar novo tipo de voto ("Voto")
	public void setArquivoTipoVotoNovo(ArquivoTipoDt arquivoTipoVotoNovo) {
		this.arquivoTipoVotoNovo = arquivoTipoVotoNovo;
	}

	// jvosantos - 27/09/2019 13:55 - Flag que indica se houve pedido de vista
	public boolean isHouvePedidoVista() {
		return houvePedidoVista;
	}

	// jvosantos - 27/09/2019 13:55 - Flag que indica se houve pedido de vista
	public void setHouvePedidoVista(boolean houvePedidoVista) {
		this.houvePedidoVista = houvePedidoVista;
	}

	// jvosantos - 04/10/2019 14:06 - Adicionar variavel para saber se houve 2/3 dos votos
	public boolean isHouveDoisTercosVotos() {
		return houveDoisTercosVotos;
	}

	// jvosantos - 04/10/2019 14:06 - Adicionar variavel para saber se houve 2/3 dos votos
	public void setHouveDoisTercosVotos(boolean houveDoisTercosVotos) {
		this.houveDoisTercosVotos = houveDoisTercosVotos;
	}

	public boolean isAlterarVotoEmenta() {
		return alterarVotoEmenta;
	}

	public void setAlterarVotoEmenta(boolean alterarVotoEmenta) {
		this.alterarVotoEmenta = alterarVotoEmenta;
	}

	public AudienciaProcessoStatusDt getAudienciaProcessoStatusOriginal() {
		return audienciaProcessoStatusOriginal;
	}

	public void setAudienciaProcessoStatusOriginal(AudienciaProcessoStatusDt audienciaProcessoStatusOriginal) {
		this.audienciaProcessoStatusOriginal = audienciaProcessoStatusOriginal;
	}
	
	public Collection<String> getIdsArquivoTipoVoto(){
		return Arrays.asList(getArquivoTipoVotoNovo().getId(), getArquivoTipoVoto().getId());
	}

	public boolean isAcompanhaDivergencia() {
		return acompanhaDivergencia;
	}

	public void setAcompanhaDivergencia(boolean acompanhaDivergencia) {
		this.acompanhaDivergencia = acompanhaDivergencia;
	}
	
}
