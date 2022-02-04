package br.gov.go.tj.projudi.sessaoVirtual.extratoAta;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.ExtratoAtaDt;
import br.gov.go.tj.projudi.dt.ResultadoVotacaoSessao;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.VotoDt;
import br.gov.go.tj.projudi.dt.VotoTipoDt;
import br.gov.go.tj.projudi.ne.AudienciaNe;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.utils.FabricaConexao;

public class ExtratoAtaJulgamentoIniciado extends ExtratoAta {

	public ExtratoAtaJulgamentoIniciado(AudienciaDt audienciaDt, AudienciaMovimentacaoDt audienciaMovimentacaoDt,
			UsuarioDt usuario, FabricaConexao fabrica) throws Exception {
		super(audienciaDt, audienciaMovimentacaoDt, usuario, fabrica);
	}
	
	@Override
	protected void setDecisaoTextos(ExtratoAtaDt ata, ServentiaCargoDt serventiaCargoRelator, List<VotoDt> votosSessao,
			List<String> divergem, ResultadoVotacaoSessao resultado) throws Exception {
		if(StringUtils.isNotEmpty(audienciaMovimentacaoDt.getAudienciaStatus()) && !StringUtils.contains(audienciaMovimentacaoDt.getAudienciaStatus(), "Julgamento Iniciado")) {
			ata.setDecisao(StringUtils.defaultString(audienciaMovimentacaoDt.getAudienciaStatus()));
			ata.setComplementoDecisao(", NOS TERMOS DO VOTO DO(A) " + (StringUtils.equals(serventiaCargoRelator.getId_Serventia(), ServentiaDt.ID_GABINETE_PRESIDENCIA) ? "PRESIDENTE " : "" ) + "RELATOR(A)");
		}
	}
	
	@Override
	protected void setListaNomeVotosAdicionais(ExtratoAtaDt ata, List<VotoDt> votosSessao) {
		super.setListaNomeVotosAdicionais(ata, votosSessao);
		
		// alsqueiroz - 27/10/2019 14:15 - Verificar se mostrar pedido de vista 
		List<String> pedidoVista = getListaPedidoVista(votosSessao);
		
		ata.setListaPedidoVista(pedidoVista);
	}

	protected List<String> getListaPedidoVista(List<VotoDt> votosSessao) {
		return getListaVotos(votosSessao, VotoTipoDt.PEDIDO_VISTA);
	}

}
