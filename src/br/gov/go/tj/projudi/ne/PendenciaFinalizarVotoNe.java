package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.ps.VotoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class PendenciaFinalizarVotoNe {

	PendenciaNe pendenciaNe = new PendenciaNe();

	public int consultarQuantidadeFinalizarVoto(String idServentiaCargo) throws Exception {
		return pendenciaNe.consultarQuantidadeFinalizarVoto(idServentiaCargo);
	}
	
	public int consultarQuantidadeFinalizarVotoPreAnalisadas(String idServentiaCargo) throws Exception {
		return pendenciaNe.consultarQuantidadeFinalizarVotoPreAnalisadas(idServentiaCargo);
	}
	
	public int consultarQuantidadeFinalizarVotoAssinatura(String idServentiaCargo, UsuarioNe usuario) throws Exception {
		FabricaConexao obFabricaConexao = null;
		int retorno = 0;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			VotoPs votoPs = new VotoPs(obFabricaConexao.getConexao());
			List lista = votoPs.consultarVotosAguardandoAssinatura(idServentiaCargo, "", PendenciaTipoDt.PROCLAMACAO_VOTO, usuario, obFabricaConexao);
			retorno = lista.size();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
}
