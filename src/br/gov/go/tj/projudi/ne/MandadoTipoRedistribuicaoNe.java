package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.MandadoTipoRedistribuicaoDt;
import br.gov.go.tj.projudi.ps.MandadoTipoRedistribuicaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class MandadoTipoRedistribuicaoNe {

	public List consultaMandadoTipoRedistribuicao() throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			MandadoTipoRedistribuicaoPs obPersistencia = new MandadoTipoRedistribuicaoPs(obFabricaConexao.getConexao());
			MandadoTipoRedistribuicaoDt objDt = new MandadoTipoRedistribuicaoDt();
			List listaTipo = new ArrayList();
			listaTipo = obPersistencia.consultaMandadoTipoRedistribuicao();
			return listaTipo;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
}
