package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.GerenciaUsuarios;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ResultadoConsultaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.AdministrarPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;

public class AdministrarNe extends Negocio{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4077822519982181878L;

	protected LogNe obLog;
	private FabricaConexao obFabricaConexao = null;
	
	public AdministrarNe() throws Exception{
		 obLog = new LogNe();
	}
	
	public List getUsuarios() throws Exception {
        return GerenciaUsuarios.getInstancia().getUsuarios();
    }
    
    public UsuarioDt getUsuario(String id_sessao){
        return GerenciaUsuarios.getInstancia().getUsuario(id_sessao);
    }

    public void atualizarPermissoesUsuario(String id_Sessao) {
        GerenciaUsuarios.getInstancia().atualizarPermissoesUsuario(id_Sessao);
    }

    public void InvalidaSessao(String id_Sessao) {
        GerenciaUsuarios.getInstancia().InvalidaSessao(id_Sessao);
    }

	public void atualizarPropriedades() {
		ProjudiPropriedades.getInstance();		
	}
	
	/**
	 * Método que executa e comita um comando SQL, armazenando log da alteração.
	 * @param usuarioSessao - usuário da sessão
	 * @param comando - comando sql
	 * @param motivo - motivo da alteração
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void comitarTransacao(UsuarioNe usuarioSessao, String comando, String motivo) throws Exception{
		LogDt obLogDt;
		obLog = new LogNe();
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			obFabricaConexao.iniciarTransacao();
			AdministrarPs obPersistencia = new  AdministrarPs(obFabricaConexao.getConexao());
			String sql = comando.replaceAll("\n", "");
			sql = comando.replaceAll("\t", "");
			sql = comando.replaceAll("\r", "");
			obPersistencia.executarComandos(sql);
			obLogDt = new LogDt("Execucao_SQL", "-12345", usuarioSessao.getId_Usuario(), usuarioSessao.getUsuarioDt().getIpComputadorLog(), String.valueOf(LogTipoDt.ExecucaoSQL), "", "Comando SQL: " + comando + ". Motivo: " + motivo);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que executa um comando SQL sem realizar commit. Usado para verificar se o SQL está correto e quantos registros ele altera.
	 * @param sql - comando sql
	 * @return quantidade de registros alterados.
	 * @throws Exception
	 * @author hmgodinho
	 */
	public int[] executarComando(String sql)throws Exception {
		int inRetorno[];
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			obFabricaConexao.iniciarTransacao();
			AdministrarPs obPersistencia = new  AdministrarPs(obFabricaConexao.getConexao());
			sql = sql.replaceAll("\n", " ");
			sql = sql.replaceAll("\t", " ");
			sql = sql.replaceAll("\r", " ");
			inRetorno= obPersistencia.executarComandos(sql);
			obFabricaConexao.cancelarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return inRetorno;
	}
	
	/**
	 * Método que executa um comando de consulta SQL. 
	 * 
	 * @param sql - comando sql
	 * @return registros.
	 * @throws Exception
	 * @author mmgomes
	 */
	public ResultadoConsultaDt executarConsulta(String sql)throws Exception {
		ResultadoConsultaDt retorno;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{			
			AdministrarPs obPersistencia = new  AdministrarPs(obFabricaConexao.getConexao());
			sql = sql.replaceAll("\n", " ");
			sql = sql.replaceAll("\t", " ");
			sql = sql.replaceAll("\r", " ");
			
			retorno= obPersistencia.executarConsulta(sql);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
}
