package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioCandidatoComarcaTurnoDt;
import br.gov.go.tj.projudi.ps.UsuarioCandidatoComarcaTurnoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class UsuarioCandidatoComarcaTurnoNe extends Negocio {
	
	public UsuarioCandidatoComarcaTurnoNe() {
		obLog = new LogNe(); 
	}

	public void salvar(UsuarioCandidatoComarcaTurnoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		UsuarioCandidatoComarcaTurnoPs obPersistencia = new  UsuarioCandidatoComarcaTurnoPs(obFabricaConexao.getConexao());
		if (dados.getId() == null) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("UsuarioCandidatoComarcaTurno", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		}
		else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("UsuarioCandidatoComarcaTurno", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), dados.getPropriedades(), dados.getPropriedades());
		}
		
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public List<UsuarioCandidatoComarcaTurnoDt> consultarPorId_UsuarioCandidatoComarca(String idUsuarioCandidatoComarca) throws Exception {
		
		List<UsuarioCandidatoComarcaTurnoDt> listaUsuarioCandidatoComarcaTurnoDt = null;
		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioCandidatoComarcaTurnoPs obPersistencia = new  UsuarioCandidatoComarcaTurnoPs(obFabricaConexao.getConexao());
			
			listaUsuarioCandidatoComarcaTurnoDt = obPersistencia.consultarPorId_UsuarioCandidatoComarca(idUsuarioCandidatoComarca);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return listaUsuarioCandidatoComarcaTurnoDt;
		
	}
}
