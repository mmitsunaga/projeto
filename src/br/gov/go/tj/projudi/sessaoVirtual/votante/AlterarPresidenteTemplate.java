package br.gov.go.tj.projudi.sessaoVirtual.votante;

import java.util.List;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.VotanteTipoDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.FabricaConexao;

public class AlterarPresidenteTemplate extends AlterarIntegranteTemplate {

	public AlterarPresidenteTemplate(FabricaConexao fabricaConexao, UsuarioNe usuarioNe) {
		super(fabricaConexao, usuarioNe);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int getIntegranteTipoCodigo() {
		return VotanteTipoDt.PRESIDENTE_SESSAO;
	}

	@Override
	protected String getId_ServentiaCargo(AudienciaProcessoDt ap) {
		return ap.getId_ServentiaCargoPresidente();
	}
	
	@Override
	protected List<VotoDt> filtrarVotos(List<VotoDt> votos) {
		return new FiltraVotoObservacao().filtrar(votos);
	}
	
	@Override
	protected void setarIntegranteAudiProc(ServentiaCargoDt novoIntegrante, AudienciaProcessoDt ap) {
		ap.setId_ServentiaCargoPresidente(novoIntegrante.getId());
	}
}
