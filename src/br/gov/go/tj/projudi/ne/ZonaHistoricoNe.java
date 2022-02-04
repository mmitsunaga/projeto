package br.gov.go.tj.projudi.ne;


import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.dt.ZonaHistoricoDt;
import br.gov.go.tj.projudi.ps.ZonaHistoricoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class ZonaHistoricoNe extends Negocio {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7176364268614661677L;

	public boolean salvar(ZonaDt dados, FabricaConexao obFabricaConexao) throws Exception {
		if (dados == null) return false;
		
		ZonaDt dadosAnteriores = new ZonaNe().consultarId(dados.getId(), obFabricaConexao);
		
		if (dadosAnteriores == null) return false;
		
		if (dados.equals(dadosAnteriores)) return false;
		
		ZonaHistoricoDt zonaHistorico = new ZonaHistoricoDt();
		zonaHistorico.setZonaDt(dadosAnteriores);
		zonaHistorico.setDataFim(Funcoes.DataHora(new Date()));
		
		LogDt obLogDt;
		ZonaHistoricoPs obPersistencia = new  ZonaHistoricoPs(obFabricaConexao.getConexao());
		obPersistencia.inserir(zonaHistorico);
		obLogDt = new LogDt("ZonaHistorico", zonaHistorico.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",zonaHistorico.getPropriedades());
		
		new LogNe().salvar(obLogDt, obFabricaConexao);
		
		return true;
	}
	
	public List<ZonaHistoricoDt> consultar(String id_zona) throws Exception {

		List<ZonaHistoricoDt> dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ZonaHistoricoPs obPersistencia = new  ZonaHistoricoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultar(id_zona );
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
}
