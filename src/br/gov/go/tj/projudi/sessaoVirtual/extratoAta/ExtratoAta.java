package br.gov.go.tj.projudi.sessaoVirtual.extratoAta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ExtratoAtaDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoSecundarioParteDt;
import br.gov.go.tj.projudi.dt.ResultadoVotacaoSessao;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.VotanteDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.RecursoNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.VotoNe;
import br.gov.go.tj.projudi.ps.VotoPs;
import br.gov.go.tj.projudi.sessaoVirtual.votacao.VotacaoUtils;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class ExtratoAta {
	protected AudienciaDt audienciaDt;
	protected AudienciaMovimentacaoDt audienciaMovimentacaoDt;
	protected UsuarioDt usuario;
	protected VotoNe votoNe;
	protected VotoPs votoPs; 
	protected FabricaConexao fabrica;
	protected ServentiaCargoNe serventiaCargoNe;
	protected ProcessoNe processoNe;

	public ExtratoAta(AudienciaDt audienciaDt, AudienciaMovimentacaoDt audienciaMovimentacaoDt, UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		this.audienciaDt = audienciaDt;
		this.audienciaMovimentacaoDt = audienciaMovimentacaoDt;
		this.usuario = usuario;
		this.fabrica = fabrica;
		
		this.votoNe = new VotoNe();
		this.serventiaCargoNe = new ServentiaCargoNe();
		this.processoNe = new ProcessoNe();
		this.votoPs = new VotoPs(fabrica.getConexao());
		

		this.audienciaDt.getAudienciaProcessoDt().setProcessoDt(processoNe.consultarIdCompleto(this.audienciaDt.getAudienciaProcessoDt().getId_Processo()));
	}
	
	public ExtratoAtaDt make() throws Exception {
		ExtratoAtaDt ata = new ExtratoAtaDt();

		AudienciaProcessoDt audienciaProcessoDt = audienciaDt.getAudienciaProcessoDt();
		
		setDatas(ata);

		List<VotanteDt> integrantesSessao = votoPs.consultarIntegrantesSessaoPorTipo(audienciaProcessoDt.getId(), null);

		setResponsaveis(ata, audienciaProcessoDt, integrantesSessao);
		
		setRecurso(audienciaProcessoDt, audienciaProcessoDt.getProcessoDt());

		setRecursoSecundario(ata, audienciaProcessoDt, audienciaProcessoDt.getProcessoDt());

		ServentiaCargoDt serventiaCargoRelator = serventiaCargoNe.consultarId(audienciaProcessoDt.getId_ServentiaCargo(), fabrica);
		
		setTurmaJulgadora(ata, audienciaProcessoDt, audienciaProcessoDt.getProcessoDt(), serventiaCargoRelator);

		setPartes(ata, audienciaProcessoDt, audienciaProcessoDt.getProcessoDt());		
		
		setResultado(ata, audienciaProcessoDt, integrantesSessao, serventiaCargoRelator);
		
		ata.setServentia(usuario.getServentia());

		return ata;
	}

	protected void setResponsaveis(ExtratoAtaDt ata, AudienciaProcessoDt audienciaProcessoDt,
			List<VotanteDt> integrantesSessao) throws Exception {
		setMinisterioPublico(ata, integrantesSessao);
		
		setPresidenteDaSessao(ata, integrantesSessao);

		ata.setNomeRelator(audienciaProcessoDt.getNomeResponsavel());
		ata.setProcessoDt(audienciaProcessoDt.getProcessoDt());
		ata.setNomeAnalista(usuario.getNome());
		
		setRedator(ata, audienciaProcessoDt);
	}

	protected void setResultado(ExtratoAtaDt ata, AudienciaProcessoDt audienciaProcessoDt,
			List<VotanteDt> integrantesSessao, ServentiaCargoDt serventiaCargoRelator) throws Exception {
		// jvosantos - 01/11/2019 09:43 - Adicionar pessoas que não votaram na lista de não votaram
		List<VotanteDt> votantes = integrantesSessao.stream().filter(filtrarPorIntegranteTipo(VotanteTipoDt.VOTANTE)).collect(Collectors.toList());

		List<VotoDt> votosSessao = votoPs.consultarVotosSessao(audienciaProcessoDt.getId()).stream().filter(x -> x.getVotanteTipoCodigoInt() == VotanteTipoDt.VOTANTE).collect(Collectors.toList());
		List<String> acompanham = new ArrayList<>(), divergem = new ArrayList<>(), impedidos = new ArrayList<String>(),
				suspeitos = new ArrayList<String>(), naoVotaram = new ArrayList<String>();

		impedidos = getListaImpedidos(votosSessao);
		suspeitos = getListaSuspeitos(votosSessao);
		naoVotaram = getListaNaoVotaram(votosSessao);
		
		naoVotaram.addAll(getListaNomeNaoVotaram(votosSessao, naoVotaram, votantes));

		acompanham = votosSessao.stream()
				.filter(voto -> voto.isAcompanhaRelator() && !voto.isExpirado() && !StringUtils.equals(voto.getIdServentiaCargo(), audienciaProcessoDt.getId_ServentiaCargo()))
				.map(voto -> getNomeVotanteAcompanha(voto))
				.collect(Collectors.toList());
		
		divergem = votosSessao.stream()
				.filter(VotoDt::isDivergeRelator)
				.map(VotoDt::getNomeVotante)
				.collect(Collectors.toList());
		
		ResultadoVotacaoSessao resultado = VotacaoUtils.calculaResultado(votosSessao);
		
		setAudienciaProcessoStatus(audienciaDt.getAudienciaProcessoDt());
		
		setDecisaoTextos(ata, serventiaCargoRelator, votosSessao, divergem, resultado);
		setListaNomeVotos(ata, acompanham, divergem, impedidos, suspeitos, naoVotaram);
		setListaNomeVotosAdicionais(ata, votosSessao);
	}

	protected void setListaNomeVotosAdicionais(ExtratoAtaDt ata, List<VotoDt> votosSessao) {}

	protected Predicate<? super VotoDt> filtrarVotoTipo(int votoTipoCodigo) {
		return voto -> voto.getVotoCodigoInt() == votoTipoCodigo;
	}
	
	protected List<String> getListaVotos(List<VotoDt> votosSessao, int votoTipoCodigo) {
		return votosSessao.stream()
				.filter(filtrarVotoTipo(votoTipoCodigo))
				.map(VotoDt::getNomeVotante)
				.collect(Collectors.toList());
	}

	protected List<String> getListaNaoVotaram(List<VotoDt> votosSessao) {
		return getListaVotos(votosSessao, VotoTipoDt.PRAZO_EXPIRADO);
	}

	protected List<String> getListaImpedidos(List<VotoDt> votosSessao) {
		return getListaVotos(votosSessao, VotoTipoDt.IMPEDIDO);
	}
	
	protected List<String> getListaSuspeitos(List<VotoDt> votosSessao) {
		return getListaVotos(votosSessao, VotoTipoDt.SUSPEICAO);
	}

	protected List<String> getListaNomeNaoVotaram(List<VotoDt> votosReais, List<String> naoVotaram,
			List<VotanteDt> votantes) {
		return votantes.stream().map(x -> x.getNome()).filter(filtrarNomeNaoVotaram(naoVotaram, votosReais)).collect(Collectors.toList());
	}

	protected void setAudienciaProcessoStatus(AudienciaProcessoDt audienciaProcessoDt) throws Exception {
		if (audienciaProcessoDt.getAudienciaProcessoStatusCodigoAnalista().isEmpty()) {
			AudienciaProcessoStatusDt statusAudienciaTemp = new AudienciaProcessoNe()
					.consultarStatusAudienciaTemp(audienciaProcessoDt.getId());
			if (statusAudienciaTemp != null) {
				audienciaMovimentacaoDt
						.setAudienciaStatusCodigo(statusAudienciaTemp.getAudienciaProcessoStatusCodigo());
				audienciaMovimentacaoDt.setAudienciaStatus(statusAudienciaTemp.getAudienciaProcessoStatus());
			}
		} else {
			audienciaMovimentacaoDt
					.setAudienciaStatusCodigo(audienciaProcessoDt.getAudienciaProcessoStatusCodigoAnalista());
			audienciaMovimentacaoDt.setAudienciaStatus(audienciaProcessoDt.getAudienciaProcessoStatusAnalista());
		}
	}

	protected String getNomeVotanteAcompanha(VotoDt voto) {
		return voto.getNomeVotante()
				.concat((StringUtils.equals(voto.getVotoTipoCodigo(),
						String.valueOf(VotoTipoDt.ACOMPANHA_RELATOR_RESSALVA))
								? " (Com Ressalva de Entendimento)"
								: ""));
	}

	protected void setListaNomeVotos(ExtratoAtaDt ata, List<String> acompanham, List<String> divergem, List<String> impedidos, List<String> suspeitos, List<String> naoVotaram) {
		ata.setListaAcompanhaRelator(acompanham);
		ata.setListaDivergeRelator(divergem);
		ata.setImpedidos(impedidos);
		ata.setSuspeitos(suspeitos);
		ata.setListaNaoVotaram(naoVotaram);
	}

	protected void setDecisaoTextos(ExtratoAtaDt ata, ServentiaCargoDt serventiaCargoRelator, List<VotoDt> votosSessao,
			List<String> divergem, ResultadoVotacaoSessao resultado) throws Exception {
		String textoUnanimidade, textoRedator;
		
		textoUnanimidade = (resultado == ResultadoVotacaoSessao.UNANIMIDADE) ? "A UNANIMIDADE" : "POR MAIORIA";

		if (resultado == ResultadoVotacaoSessao.MAIORIA_DIVERGE) {
			textoRedator = "NOS TERMOS DO VOTO DO DES(A). " + StringUtils.defaultIfEmpty(ata.getNomeRedator(), divergem.get(0)); // jvosantos - 04/06/2019 10:22 - Coloca o nome do redator corretamente
			audienciaMovimentacaoDt.setVotoPorMaioria(true);
			audienciaMovimentacaoDt
					.setId_ServentiaCargoRedator(
							votosSessao
									.stream()
									.filter(VotoDt::isDivergeRelator)
									.findFirst()
									.get()
									.getIdServentiaCargo());
			audienciaMovimentacaoDt.setServentiaCargoRedator(divergem.get(0));
		} else {
			textoRedator = "NOS TERMOS DO VOTO DO(A) " + (StringUtils.equals(serventiaCargoRelator.getId_Serventia(), ServentiaDt.ID_GABINETE_PRESIDENCIA) ? "PRESIDENTE " : "" ) + "RELATOR(A)"; 
		}

		ata.setDecisao(StringUtils.defaultString(audienciaMovimentacaoDt.getAudienciaStatus()));
		ata.setComplementoDecisao(", " + textoUnanimidade + ", " + textoRedator);
	}

	protected void setPartes(ExtratoAtaDt ata, AudienciaProcessoDt audienciaProcessoDt, ProcessoDt processoDt)
			throws Exception {
		List<ProcessoParteDt> partesAudienciaProcesso = votoPs.consultarPartesRecursoSecundario(audienciaProcessoDt.getId());
		
		Integer maiorOrdem = calculaMaiorOrdem(partesAudienciaProcesso);
		
		// lrcampos 15/08/2019 * Caso não possuir recurso secundario, verifica se possui recurso e agrupa as partes pela ordem.
		if(partesAudienciaProcesso.isEmpty()) {
			partesAudienciaProcesso = votoPs.consultarPartesRecurso(audienciaProcessoDt.getId());
			maiorOrdem = calculaMaiorOrdem(partesAudienciaProcesso);
		}
		ata.setMaiorOrdem(maiorOrdem);
		HashMap<Integer, List<String>> poloAtivo = new HashMap<Integer, List<String>>();
		
		for(int i = 0; i < maiorOrdem; ++i) {
			final int t = i;
			List<String> poloAtivoTemp = getListaPartes(partesAudienciaProcesso, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, t);
			if(!poloAtivoTemp.isEmpty()) poloAtivo.put(i, poloAtivoTemp);
		}

		ata.setTemRecSecPoloAtivo(true);
		if (poloAtivo.isEmpty()) {
			setListaPartes(processoDt.getListaPolosAtivos(), poloAtivo);
			ata.setTemRecSecPoloAtivo(false);
		}
		ata.setPoloAtivo(poloAtivo);
		
		HashMap<Integer, List<String>> poloPassivo = new HashMap<Integer, List<String>>();
		
		for(int i = 0; i < maiorOrdem; ++i) {
			final int t = i;
			List<String> poloPassivoTemp = getListaPartes(partesAudienciaProcesso, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, t);
			if(!poloPassivoTemp.isEmpty()) poloPassivo.put(i, poloPassivoTemp);
		}
		
		ata.setTemRecSecPoloPassivo(true);
		if (poloPassivo.isEmpty()) {
			setListaPartes(processoDt.getListaPolosPassivos(), poloPassivo);
			ata.setTemRecSecPoloPassivo(false);
		}
		ata.setPoloPassivo(poloPassivo);
	}

	protected int calculaMaiorOrdem(List<ProcessoParteDt> partes) {
		return partes.stream().mapToInt(ProcessoParteDt::getOrdemParte).reduce(Integer::max).orElse(0) + 1;
	}

	protected List<String> getListaPartes(List<ProcessoParteDt> partesRecursoSecundario, int processoParteTipoCodigo , final int ordemParte) {
		return partesRecursoSecundario
				.stream()
				.filter(
						parte -> Funcoes.StringToInt(parte.getProcessoParteTipoCodigo()) == processoParteTipoCodigo && parte.getOrdemParte() == ordemParte)
				.map(ProcessoParteDt::getNome)
				.collect(Collectors.toList());
	}

	protected void setListaPartes(List<ProcessoParteDt> lista, HashMap<Integer, List<String>> polo) {
		List<ProcessoParteDt> list = Optional.ofNullable(lista).orElse(Collections.emptyList());
		polo.put(-1, list.stream().map(ProcessoParteDt::getNome).collect(Collectors.toList()));
	}

	protected void setTurmaJulgadora(ExtratoAtaDt ata, AudienciaProcessoDt audienciaProcessoDt,
			ProcessoDt processoDt, ServentiaCargoDt serventiaCargoRelator) throws Exception {		
		ata.setTurmaJulgadora(votoPs.consultarOrdemTurmaJulgadora(processoDt.getId_Serventia(),
				serventiaCargoRelator.getId_Serventia()));
	}

	protected void setRedator(ExtratoAtaDt ata, AudienciaProcessoDt audienciaProcessoDt) throws Exception {
		if (StringUtils.isNotEmpty(audienciaProcessoDt.getId_ServentiaCargoRedator())) {
			ServentiaCargoDt serventiaCargoDt = serventiaCargoNe.consultarId(audienciaProcessoDt.getId_ServentiaCargoRedator());

			if(serventiaCargoDt == null) throw new Exception("Não foi encontrado a serventia cargo do Redator");

			ata.setNomeRedator(serventiaCargoDt.getNomeUsuario());				
		}
	}

	protected void setRecursoSecundario(ExtratoAtaDt ata, AudienciaProcessoDt audienciaProcessoDt,
			ProcessoDt processoDt) throws Exception {
		RecursoSecundarioParteDt recursoSecundario = votoPs.consultarRecursoSecundarioIdProcesso(processoDt.getId());

		ata.setClasse(recursoSecundario == null ? audienciaProcessoDt.getProcessoTipo()
				: recursoSecundario.getProcessoTipoRecursoSecundario() + " - "
						+ audienciaProcessoDt.getProcessoTipo());

		if (recursoSecundario != null) {
			processoDt.setDescricaoPoloAtivo(recursoSecundario.getDescricaoPoloAtivo());
			processoDt.setDescricaoPoloPassivo(recursoSecundario.getDescricaoPoloPassivo());
		}
	}

	protected void setRecurso(AudienciaProcessoDt audienciaProcessoDt, ProcessoDt processoDt) throws Exception {
		RecursoDt recursoDt = new RecursoNe().consultarRecursoPorProcessoTipo(processoDt.getId(), audienciaProcessoDt.getId_ProcessoTipo()); // jvosantos - 26/08/2019 17:13 - Correção busca recurso
		
		if (recursoDt != null && StringUtils.isNotEmpty(processoDt.getId_Recurso())) { // jvosantos - 12/08/2019 15:23 - Adicionar verificação de ID_RECURSO para não sobrescrever a descrição dos polos
			processoDt.setDescricaoPoloAtivo(recursoDt.getDescricaoPoloAtivo());
			processoDt.setDescricaoPoloPassivo(recursoDt.getDescricaoPoloPassivo());
		}
	}

	protected void setPresidenteDaSessao(ExtratoAtaDt ata, List<VotanteDt> integrantesSessao) {
		Optional<VotanteDt> presidenteSessao = integrantesSessao.stream().filter(filtrarPorIntegranteTipo(VotanteTipoDt.PRESIDENTE_SESSAO)).findFirst();
		
		presidenteSessao.ifPresent(x -> {
			ata.setNomePresidenteSessao(x.getNome());
			audienciaMovimentacaoDt.setServentiaCargoPresidente(x.getNome());				
			audienciaMovimentacaoDt.setId_ServentiaCargoPresidente(x.getIdServentiaCargo());
		});
	}

	private Predicate<? super VotanteDt> filtrarPorIntegranteTipo(int integranteTipoCodigo) {
		return x -> x.getVotanteTipoDt().getVotanteTipoCodigoInt() == integranteTipoCodigo;
	}
	
	private Predicate<String> filtrarNomeNaoVotaram(List<String> naoVotaram, List<VotoDt> votosReais){
		return x -> {
			String xTrimed = x.trim();
			
			// Verifica se esse nome já não está na lista dos que não votaram
			for(String t : naoVotaram) {
				if(!t.trim().equalsIgnoreCase(xTrimed)) continue;
				return false;
			}
			
			// Verifica se esse nome já não está na lista de votos (Acompanha, impedimento,
			for(VotoDt v : votosReais) {
				if(!v.getNomeVotante().trim().equalsIgnoreCase(xTrimed)) continue;
				return false;
			}
			
			// Nome não estava em nenhuma lista, logo, não votou
			return true;
		};
	}

	protected void setPresidenteDaSessao(ExtratoAtaDt ata, AudienciaProcessoDt audienciaProcessoDt) throws Exception {
		if (StringUtils.isNotEmpty(audienciaProcessoDt.getId_ServentiaCargoPresidente())) {

			ServentiaCargoDt serventiaCargoDt = serventiaCargoNe
					.consultarId(audienciaProcessoDt.getId_ServentiaCargoPresidente());
			ata.setNomePresidenteSessao(serventiaCargoDt.getNomeUsuario());
		}
		audienciaMovimentacaoDt.setServentiaCargoPresidente(audienciaProcessoDt.getServentiaCargoPresidente());
	}

	protected void setMinisterioPublico(ExtratoAtaDt ata, List<VotanteDt> integrantesSessao) {
		Optional<VotanteDt> ministerioPublico = integrantesSessao.stream().filter(filtrarPorIntegranteTipo(VotanteTipoDt.MINISTERIO_PUBLICO)).findFirst();
		
		ministerioPublico.ifPresent(x -> {
			ata.setProcurador(x.getNome());
			audienciaMovimentacaoDt.setId_ServentiaCargoMp(x.getIdServentiaCargo());
		});
	}

	private void setDatas(ExtratoAtaDt ata) {
//		LocalDateTime data = Funcoes.parseData(audienciaDt.getDataAgendada());
		ata.setData(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
//		ata.setDataSessao(data.format(DateTimeFormatter.ofPattern("dd LLLL YYYY")));
//		ata.setHoraSessao(data.format(DateTimeFormatter.ofPattern("HH:mm")));

		ata.setDataSessao(audienciaDt.getDataAgendada());
	}
}
