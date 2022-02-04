package br.gov.go.tj.projudi.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.VotoSessaoLocalizarDt;

public class PreAnaliseEmLoteDTO {

	String dataSessao = StringUtils.EMPTY;
	String numeroProcesso = StringUtils.EMPTY;
	String prazoVotacao = StringUtils.EMPTY;
	String tempoRestante = StringUtils.EMPTY;
	String votos = StringUtils.EMPTY;
	String idArquivo = StringUtils.EMPTY;
	String nomeRelator = StringUtils.EMPTY;

	public String getNomeRelator() {
		return nomeRelator;
	}

	public void setNomeRelator(String nomeRelator) {
		this.nomeRelator = nomeRelator;
	}

	public String getIdArquivo() {
		return idArquivo;
	}

	private void setIdArquivo(String idArquivo) {
		this.idArquivo = idArquivo;
	}

	public String getDataSessao() {
		return dataSessao;
	}

	private void setDataSessao(String dataSessao) {
		this.dataSessao = dataSessao;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	private void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getPrazoVotacao() {
		return prazoVotacao;
	}

	private void setPrazoVotacao(String prazoVotacao) {
		this.prazoVotacao = prazoVotacao;
	}

	public String getTempoRestante() {
		return tempoRestante;
	}

	private void setTempoRestante(String tempoRestante) {
		this.tempoRestante = tempoRestante;
	}

	public String getVotos() {
		return votos;
	}

	private void setVotos(String votos) {
		this.votos = votos;
	}
	
	private static String lineBreaks(int number) {
		String result = StringUtils.EMPTY;
		for (int i = number; i > 0; i--) {
			result += "<br>";
		}
		return result;
	}

	public static List<PreAnaliseEmLoteDTO> getDTOList(List<VotoSessaoLocalizarDt> votos) {
		Map<ArquivoDt, List<VotoSessaoLocalizarDt>> sortedVotos = votos.stream()
				.collect(Collectors.groupingBy(VotoSessaoLocalizarDt::getArquivoVoto));
		List<PreAnaliseEmLoteDTO> dtos = new ArrayList<PreAnaliseEmLoteDTO>();
		Set<Map.Entry<ArquivoDt, List<VotoSessaoLocalizarDt>>> entrySet = sortedVotos.entrySet();
		for (Map.Entry<ArquivoDt, List<VotoSessaoLocalizarDt>> entry : entrySet) {
			List<VotoSessaoLocalizarDt> votoGroup = entry.getValue();
			PreAnaliseEmLoteDTO dto = new PreAnaliseEmLoteDTO();
			for (VotoSessaoLocalizarDt voto : votoGroup) {
				int numLinhas = voto.getVotosRelacionados().size()+1;
				dto.setDataSessao(dto.getDataSessao() + voto.getDataSessao() + lineBreaks(numLinhas));
				
				String a = "<a href=\"javaScript:abrirMenu('BuscaProcesso?Id_Processo="+voto.getIdProcesso()+"');\">\n";
				String b = voto.getProcessoNumero()+"&nbsp;\n";
				String c = voto.getProcessoDt().getProcessoTipo()+"</a>";
				String numeroProcessoLink = a+b+c;
				dto.setNumeroProcesso(dto.getNumeroProcesso() + numeroProcessoLink + lineBreaks(numLinhas));
				dto.setPrazoVotacao(dto.getPrazoVotacao() + voto.getPrazoVotacao() + lineBreaks(numLinhas));
				dto.setTempoRestante(dto.getTempoRestante() + voto.getTempoRestante() + lineBreaks(numLinhas));
				dto.setVotos(dto.getVotos() + "Relator - Des.(a) " + voto.getNomeRelator() + "<br>"
						+ voto.getVotosRelacionados().stream().map(v -> v + "<br>").reduce((v1, v2) -> v1 + v2).get());
			}
			dto.setIdArquivo(entry.getKey().getId());
			dtos.add(dto);
		}
		return dtos;
	}
}