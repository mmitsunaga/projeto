package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscArquivoDt;
import br.gov.go.tj.projudi.ps.UsuarioCejuscArquivoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class UsuarioCejuscArquivoNe extends Negocio {
	
	public UsuarioCejuscArquivoNe() {
		obLog = new LogNe(); 
	}
	
	/**
	 * Método para salvar UsuarioCandidatoArquivoDt
	 * @param UsuarioCejuscArquivoDt usuarioCandidatoArquivoDt
	 * @param obFabricaConexao
	 * @throws Exception
	 */
	public void salvar(UsuarioCejuscArquivoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		UsuarioCejuscArquivoPs obPersistencia = new  UsuarioCejuscArquivoPs(obFabricaConexao.getConexao());
		if (dados.getId() == null) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("UsuarioCandidatoArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			
			obLog.salvar(obLogDt, obFabricaConexao);
		}
//		else {
//			obPersistencia.alterar(dados);
//			obLogDt = new LogDt("UsuarioCandidatoArquivo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), dados.getPropriedades(), dados.getPropriedades());
//		}
		
//		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	/**
	 * Método para consultar UsuarioCandidatoArquivoDt pelo ID
	 * @param String id
	 * @return UsuarioCandidatoArquivoDt
	 * @throws Exception
	 */
	public UsuarioCejuscArquivoDt consultarUsuarioCejuscArquivoDtId(String id) throws Exception {
		UsuarioCejuscArquivoDt usuarioCejuscArquivoDt = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioCejuscArquivoPs obPersistencia = new  UsuarioCejuscArquivoPs(obFabricaConexao.getConexao());
			
			usuarioCejuscArquivoDt = obPersistencia.consultarId(id);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		return usuarioCejuscArquivoDt;
	}
	
	/**
	 * Método para consultar lista de arquivos.
	 * @param String idUsuarioCandidato
	 * @return List<UsuarioCandidatoArquivoDt>
	 * @throws Exception
	 */
	public List<UsuarioCejuscArquivoDt> consultaListaUsuarioCejuscArquivoDt(String idUsuarioCandidato) throws Exception {
		List<UsuarioCejuscArquivoDt> listaUsuarioCejuscArquivoDt = null;
		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioCejuscArquivoPs obPersistencia = new  UsuarioCejuscArquivoPs(obFabricaConexao.getConexao());
			
			listaUsuarioCejuscArquivoDt = obPersistencia.consultaListaUsuarioCejuscArquivoDt(idUsuarioCandidato);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return listaUsuarioCejuscArquivoDt;
	}
}
