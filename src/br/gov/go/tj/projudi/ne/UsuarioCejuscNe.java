package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.UsuarioCejuscPs;
import br.gov.go.tj.utils.FabricaConexao;

public class UsuarioCejuscNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3931035080761480882L;

	public UsuarioCejuscNe() {
		obLog = new LogNe(); 
	}
	
	public void salvar(UsuarioCejuscDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		UsuarioCejuscPs obPersistencia = new  UsuarioCejuscPs(obFabricaConexao.getConexao());
		if (dados.getId() == null) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("UsuarioCejusc", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		}
		else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("UsuarioCejusc", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), dados.getPropriedades(), dados.getPropriedades());
		}
		
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	/**
	 * Método para consultar usuário via número cpf.
	 * @param String cpf
	 * @return UsuarioDt
	 * @throws Exception
	 */
	public UsuarioDt consultaUsuarioDt(String cpf) throws Exception {
		UsuarioNe usuarioNe = new UsuarioNe();
		UsuarioDt usuarioDt = null;
		
		if( cpf != null && cpf.length() > 0 ) {
			usuarioDt = usuarioNe.consultarUsuarioCpf(cpf);
		}
		
		return usuarioDt;
	}
	
	/**
	 * Método para atualizar status do Cejusc
	 * @param UsuarioCejuscDt dados
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 */
	public void atualizarStatus(UsuarioCejuscDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		UsuarioCejuscPs obPersistencia = new  UsuarioCejuscPs(obFabricaConexao.getConexao());
		
		obPersistencia.atualizarStatus(dados);
		obLogDt = new LogDt("UsuarioCejusc", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		
		obLog.salvar(obLogDt, obFabricaConexao);
	}

	/**
	 * Método para consultar UsuarioCejuscDt pelo ID_USU
	 * @param String idUsuario
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
	public UsuarioCejuscDt consultarUsuarioCejuscDtIdUsuario(String idUsuario) throws Exception {
		UsuarioCejuscDt usuarioCejuscDt = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			if( idUsuario != null && idUsuario.length() > 0 ) {
				UsuarioCejuscPs obPersistencia = new  UsuarioCejuscPs(obFabricaConexao.getConexao());
				usuarioCejuscDt = obPersistencia.consultarUsuarioCejuscDtIdUsuario(idUsuario);
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		return usuarioCejuscDt;
	}
	
	/**
	 * Método para consultar UsuarioCejuscDt pelo ID_USU_CEJUSC
	 * @param String idUsuarioCejusc
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
	public UsuarioCejuscDt consultarUsuarioCejuscDtIdUsuarioCejusc(String idUsuarioCejusc) throws Exception {
		UsuarioCejuscDt usuarioCejuscDt = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			if( idUsuarioCejusc != null && idUsuarioCejusc.length() > 0 ) {
				UsuarioCejuscPs obPersistencia = new UsuarioCejuscPs(obFabricaConexao.getConexao());
				usuarioCejuscDt = obPersistencia.consultarUsuarioCejuscDtIdUsuarioCejusc(idUsuarioCejusc);
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		return usuarioCejuscDt;
	}
	
	/**
	 * Método para consulta lista de usuarioCejuscDt no formato JSON
	 * @param String nome
	 * @param String cpf
	 * @return String
	 * @throws Exception
	 */
	public String consultarListaUsuarioCejuscDtJSON(String nome, String cpf, String statusAtualUsuario, String idServentia, String apenasVoluntarios, String posicaopaginaatual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioCejuscPs obPersistencia = new UsuarioCejuscPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarListaUsuarioCejuscDtJSON(nome, cpf, statusAtualUsuario, idServentia, apenasVoluntarios, posicaopaginaatual);
		}
		finally {
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return stTemp;
	}
	
	/**
	 * Método para consulta lista de usuarioCejuscDt no formato JSON
	 * @param String nome
	 * @param String posicaopaginaatual
	 * @return List<UsuarioCejuscDt>
	 * @throws Exception
	 */
	public List<UsuarioCejuscDt> consultarDescricao(String nome, String posicaopaginaatual) throws Exception {
		List<UsuarioCejuscDt> retorno = null;
		FabricaConexao obFabricaConexao = null;

		try	{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioCejuscPs obPersistencia = new UsuarioCejuscPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarDescricao( nome, posicaopaginaatual);
		}
		finally	{
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;   
	}
	
	/**
	 * Método para consulta lista de usuarioCejuscDt no formato JSON
	 * @param String nome
	 * @param String posicaopaginaatual
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoJSON(String nome, String posicaopaginaatual) throws Exception {
		String retorno = null;
		FabricaConexao obFabricaConexao = null;

		try	{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioCejuscPs obPersistencia = new UsuarioCejuscPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarDescricaoJSON( nome, posicaopaginaatual);
		}
		finally	{
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;   
	}
	
	/**
	 * Método para consulta lista de usuarioCejuscDt aprovados no formato JSON
	 * @param String nome
	 * @param String posicaopaginaatual
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoAprovadosJSON(String nome, String posicaopaginaatual) throws Exception {
		String retorno = null;
		FabricaConexao obFabricaConexao = null;

		try	{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			UsuarioCejuscPs obPersistencia = new UsuarioCejuscPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarDescricaoStatusAprovadoJSON(nome, posicaopaginaatual);
		}
		finally	{
			if( obFabricaConexao != null ) {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return retorno;   
	}
	
	/**
	 * Método para consultar ID_USU pelo ID_USU_CEJUSC
	 * @param String idUsuCejusc
	 * @return IdUsu
	 * @throws Exception
	 */
	public String consultarIdUsu(String idUsuCejusc) throws Exception {
		String idUsu = null;
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			if( idUsuCejusc != null && idUsuCejusc.length() > 0 ) {
				UsuarioCejuscPs obPersistencia = new  UsuarioCejuscPs(obFabricaConexao.getConexao());
				idUsu = obPersistencia.consultarIdUsu(idUsuCejusc);
			}
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		return idUsu;
	}
}
