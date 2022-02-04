package br.gov.go.tj.projudi.sessaoVirtual.votante;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoVotantesDt;
import br.gov.go.tj.projudi.dt.ImpedimentoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.VotanteDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.ne.VotoNe;
import br.gov.go.tj.projudi.sessaoVirtual.ne.AudienciaProcessoSessaoVirtualNe;
import br.gov.go.tj.utils.FabricaConexao;

public abstract class AlterarIntegranteTemplate {
	FabricaConexao fabricaConexao;
	protected UsuarioNe usuarioNe;
	protected PendenciaResponsavelNe pendenciaResponsavelNe;
	protected AudienciaProcessoNe audienciaProcessoNe;
	protected AudienciaProcessoSessaoVirtualNe audienciaProcessoSessaoVirtualNe;
	protected PendenciaNe pendenciaNe;
	protected VotoNe votoNe;
	
	public AlterarIntegranteTemplate (FabricaConexao fabricaConexao, UsuarioNe usuarioNe) {
		this.fabricaConexao = fabricaConexao;
		this.usuarioNe = usuarioNe;
		votoNe = new VotoNe();
		pendenciaNe = new PendenciaNe();
		audienciaProcessoNe = new AudienciaProcessoNe();
		audienciaProcessoSessaoVirtualNe = new AudienciaProcessoSessaoVirtualNe();
		pendenciaResponsavelNe = new PendenciaResponsavelNe();
	}

	public void alterar(AudienciaProcessoDt ap, ServentiaCargoDt novoIntegrante) throws Exception {
		if(StringUtils.equals(getId_ServentiaCargo(ap), novoIntegrante.getId())) return;
		
		boolean criarPendencia = false;
		
		List<VotoDt> votos = votoNe.consultarTodosVotosSessao(ap.getId(), fabricaConexao);
				
		votos = filtrarVotos(votos);
		
		criarPendencia |= finalizarVotos(criarPendencia, votos, ap);
		
		List<PendenciaDt> pendenciasAudienciaProcesso = pendenciaNe.consultarPendenciasProcessoPorTipo(ap.getProcessoDt().getId(), PendenciaTipoDt.SESSAO_CONHECIMENTO);
		
		criarPendencia |= finalizarPendencias(criarPendencia, pendenciasAudienciaProcesso, ap);

		List<VotanteDt> votantes = votoNe.consultarTodosVotantesSessaoCompletoDeVerdade(ap.getId(), fabricaConexao);
		Optional<VotanteDt> integranteAntigo = integranteAntigo(votantes);
		
		setarNovoIntegrante(novoIntegrante, criarPendencia, ap, integranteAntigo);

		if(criarPendencia)
			criarPendencia(novoIntegrante, ap);
	}

	protected List<VotoDt> filtrarVotos(List<VotoDt> votos) {
		return votos;
	}

	protected Optional<VotanteDt> integranteAntigo(List<VotanteDt> votantes) {
		Optional<VotanteDt> integranteAntigo = votantes != null ? votantes.stream().filter(x -> x.getVotanteTipoDt().getVotanteTipoCodigoInt() == getIntegranteTipoCodigo()).findAny() : Optional.empty();
		return integranteAntigo;
	}

	protected void setarNovoIntegrante(ServentiaCargoDt novoIntegrante, boolean criarPendencia, AudienciaProcessoDt ap,
			Optional<VotanteDt> integranteAntigo) throws Exception {
		if(integranteAntigo.isPresent()) 
			votoNe.excluirVotante(integranteAntigo.get().getId(), fabricaConexao);
		
		cadastrarIntegrante(novoIntegrante, ap);
		
		atualizarAudiProc(novoIntegrante, ap);
		
	}

	protected void atualizarAudiProc(ServentiaCargoDt novoIntegrante, AudienciaProcessoDt ap) throws Exception {
		setarIntegranteAudiProc(novoIntegrante, ap);
		
		ap.setIpComputadorLog(usuarioNe.getIpComputadorLog());
		ap.setId_UsuarioLog(usuarioNe.getId_Usuario());
		
		audienciaProcessoSessaoVirtualNe.salvar(ap, fabricaConexao);
	}

	protected void setarIntegranteAudiProc(ServentiaCargoDt novoIntegrante, AudienciaProcessoDt ap) {}

	protected void cadastrarIntegrante(ServentiaCargoDt novoIntegrante, AudienciaProcessoDt ap) throws Exception {
		AudienciaProcessoVotantesDt integranteNovo = montaObjetoIntegrante(novoIntegrante, ap);
		
		audienciaProcessoNe.cadastrarVotante(integranteNovo, fabricaConexao);
	}

	protected AudienciaProcessoVotantesDt montaObjetoIntegrante(ServentiaCargoDt novoIntegrante, AudienciaProcessoDt ap) {
		AudienciaProcessoVotantesDt integranteNovo = new AudienciaProcessoVotantesDt();
		integranteNovo.setId_AudienciaProcesso(ap.getId());
		integranteNovo.setImpedimentoTipoCodigo(String.valueOf( ImpedimentoTipoDt.NAO_IMPEDIDO));
		integranteNovo.setId_ServentiaCargo(novoIntegrante.getId());
		integranteNovo.setOrdemVotante("99");
		integranteNovo.setConvocado(false);
		integranteNovo.setRelator(false);
		integranteNovo.setVotanteTipoCodigo(String.valueOf(getIntegranteTipoCodigo()));
		return integranteNovo;
	}

	protected abstract int getIntegranteTipoCodigo();

	protected void criarPendencia(ServentiaCargoDt novoIntegrante, AudienciaProcessoDt ap) throws Exception {
		pendenciaNe.gerarPendenciaSessaoConhecimento(novoIntegrante.getId(), usuarioNe.getUsuarioDt(), ap.getId_Processo(), ap.getId(), fabricaConexao);
	}
	
	protected boolean finalizarPendencias(boolean criarPendencia, List<PendenciaDt> pendenciasAudienciaProcesso,
			AudienciaProcessoDt ap) throws Exception {
		for(PendenciaDt p : pendenciasAudienciaProcesso) {
			List<PendenciaResponsavelDt> responsaveis = pendenciaResponsavelNe.consultarResponsaveis(p.getId(), fabricaConexao);

			if(responsaveis.stream().filter(x -> StringUtils.equals(x.getId_ServentiaCargo(), getId_ServentiaCargo(ap))).findAny().isPresent()) {
				pendenciaNe.setInfoPendenciaFinalizar(p, usuarioNe.getUsuarioDt(), fabricaConexao);
				criarPendencia = true;
			}
		}
		return criarPendencia;
	}

	protected abstract String getId_ServentiaCargo(AudienciaProcessoDt ap);

	protected boolean finalizarVotos(boolean criarPendencia, List<VotoDt> votos, AudienciaProcessoDt ap)
			throws Exception {
		if(votos != null && !votos.isEmpty()) {   
			
			for(VotoDt ob : votos) {
				List<PendenciaResponsavelDt> responsaveis = pendenciaResponsavelNe.consultarResponsaveis(ob.getPendenciaDt().getId(), fabricaConexao);
				List<PendenciaResponsavelDt> responsaveisFinais = pendenciaResponsavelNe.consultarResponsaveisFinais(ob.getPendenciaDt().getId(), fabricaConexao);
				
				if(responsaveisFinais != null) {
					responsaveis.addAll(responsaveisFinais);
					responsaveisFinais.clear();
					responsaveisFinais = null;
				}
				
				if(responsaveis.stream().filter(x -> StringUtils.equals(x.getId_ServentiaCargo(), getId_ServentiaCargo(ap))).findAny().isPresent()) {
					votoNe.excluirVoto(ob.getId(), fabricaConexao);
					return true;
				}
			}
		}
		return false;
	}
}
