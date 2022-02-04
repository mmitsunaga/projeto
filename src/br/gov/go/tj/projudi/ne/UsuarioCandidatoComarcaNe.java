package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioCandidatoComarcaDt;
import br.gov.go.tj.projudi.ps.UsuarioCandidatoComarcaPs;
import br.gov.go.tj.utils.FabricaConexao;

public class UsuarioCandidatoComarcaNe extends Negocio {
	
	public UsuarioCandidatoComarcaNe() {
		obLog = new LogNe(); 
	}
	
	public void salvar(UsuarioCandidatoComarcaDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		UsuarioCandidatoComarcaPs obPersistencia = new  UsuarioCandidatoComarcaPs(obFabricaConexao.getConexao());
		if (dados.getId() == null) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("UsuarioCandidatoComarca", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		}
		else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("UsuarioCandidatoComarca", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), dados.getPropriedades(), dados.getPropriedades());
		}
		
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public List<UsuarioCandidatoComarcaDt> consultarPorId_UsuarioCandidato(String idUsuarioCandidato) throws Exception {
		
		List<UsuarioCandidatoComarcaDt> listaUsuarioCandidatoComarcaDt = null;
		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioCandidatoComarcaPs obPersistencia = new  UsuarioCandidatoComarcaPs(obFabricaConexao.getConexao());
			
			listaUsuarioCandidatoComarcaDt = obPersistencia.consultarPorId_UsuarioCandidato(idUsuarioCandidato);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return listaUsuarioCandidatoComarcaDt;
		
	}
}