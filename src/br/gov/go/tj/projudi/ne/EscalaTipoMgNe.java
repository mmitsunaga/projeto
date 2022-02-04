package br.gov.go.tj.projudi.ne;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import br.gov.go.tj.projudi.dt.EscalaTipoMgDt;
import br.gov.go.tj.projudi.ps.EscalaTipoMgPs;
import br.gov.go.tj.utils.FabricaConexao;

public class EscalaTipoMgNe implements Serializable {

	private static final long serialVersionUID = 5715164942905400305L;

	public EscalaTipoMgNe() {
	}

	public List consultaEscalaTipoMg() throws Exception {
		List<EscalaTipoMgDt> listaEscalaTipoMg = new ArrayList();
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			EscalaTipoMgPs obPersistencia = new EscalaTipoMgPs(obFabricaConexao.getConexao());
			listaEscalaTipoMg = obPersistencia.consultaEscalaTipoMg();
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaEscalaTipoMg;
	}
}
