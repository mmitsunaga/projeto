package br.gov.go.tj.projudi.sessaoVirtual.votante;

import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.FabricaConexao;

public class AlterarMPTemplate extends AlterarIntegranteTemplate {

	public AlterarMPTemplate(FabricaConexao fabricaConexao, UsuarioNe usuarioNe) {
		super(fabricaConexao, usuarioNe);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getIntegranteTipoCodigo() {
		return VotanteTipoDt.MINISTERIO_PUBLICO;
	}

	@Override
	protected String getId_ServentiaCargo(AudienciaProcessoDt ap) {
		return ap.getId_ServentiaCargoMP();
	}
	
//	@Override
//	protected List<VotoDt> filtrarVotos(List<VotoDt> votos) {
//		List<VotoDt> observacoes = votos != null ? votos.stream().filter(x -> StringUtils.equals(x.getVotoTipoCodigo(), String.valueOf(VotoTipoDt.OBSERVACAO))).collect(Collectors.toList()) : null;
//		return observacoes;
//	}
	
	@Override
	protected List<VotoDt> filtrarVotos(List<VotoDt> votos) {
		return new FiltraVotoObservacao().filtrar(votos);
	}
	
	@Override
	protected void setarIntegranteAudiProc(ServentiaCargoDt novoIntegrante, AudienciaProcessoDt ap) {
		ap.setId_ServentiaCargoMP(novoIntegrante.getId());
	}
	

}
