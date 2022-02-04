package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.TransitoJulgadoEventoDt;
import br.gov.go.tj.projudi.ps.TransitoJulgadoEventoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class TransitoJulgadoEventoNe extends Negocio{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5189614848654434643L;
	
	protected TransitoJulgadoEventoDt obDados;

	public TransitoJulgadoEventoNe() {
		obLog = new LogNe(); 
		obDados = new TransitoJulgadoEventoDt(); 
	}

//---------------------------------------------------------
	public void salvar(TransitoJulgadoEventoDt dados ) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TransitoJulgadoEventoPs obPersistencia = new TransitoJulgadoEventoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("TransitoJulgadoEvento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados);
                obLogDt = new LogDt("TransitoJulgadoEvento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());				
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void salvar(TransitoJulgadoEventoDt dados, FabricaConexao obFabricaConexao) throws Exception{

		LogDt obLogDt;
		
		TransitoJulgadoEventoPs obPersistencia = new TransitoJulgadoEventoPs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().length()==0) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("TransitoJulgadoEvento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {
			obPersistencia.alterar(dados);
            obLogDt = new LogDt("TransitoJulgadoEvento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());				
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
		
		
	}

//---------------------------------------------------------
	public void excluir(TransitoJulgadoEventoDt dados) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TransitoJulgadoEventoPs obPersistencia = new TransitoJulgadoEventoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("TransitoJulgadoEvento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); 
			dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void excluir(TransitoJulgadoEventoDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		
		TransitoJulgadoEventoPs obPersistencia = new TransitoJulgadoEventoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("TransitoJulgadoEvento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados.getId()); 
		dados.limpar();

		obLog.salvar(obLogDt, obFabricaConexao);

	}

	public void excluirId_Evento(String id_Evento, FabricaConexao obFabricaConexao, String Id_UsuarioLog, String IpComputadorLog) throws Exception {

		TransitoJulgadoEventoPs obPersistencia = new TransitoJulgadoEventoPs(obFabricaConexao.getConexao());
		obPersistencia.excluirId_Evento(id_Evento); 

	}
 //---------------------------------------------------------

	public TransitoJulgadoEventoDt consultarId(String id_TransitoJulgadoEvento ) throws Exception{

		TransitoJulgadoEventoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TransitoJulgadoEventoPs obPersistencia = new TransitoJulgadoEventoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_TransitoJulgadoEvento); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	/**
	 * Lista os TransitoJulgadoEvento através do Id_Evento (idEventoExecucao)
	 */
	public List listarTransitoJulgadoEvento(String idProcessoEventoExecucao, String idEventoExecucao, FabricaConexao obFabricaConexao) throws Exception{
		List lista = null;
		 
		TransitoJulgadoEventoPs obPersistencia = new TransitoJulgadoEventoPs(obFabricaConexao.getConexao());
		lista = obPersistencia.listarTransitoJulgadoEvento(idProcessoEventoExecucao, idEventoExecucao); 
		
		return lista;
	}
	
	/**
	 * Lista os TransitoJulgadoEvento através do Id_Evento(idEventoExecucao) das condenações não extintas
	 */
	public List listarTransitoJulgadoEventoNaoExtinto(String idProcessoEventoExecucao, String idEventoExecucao) throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TransitoJulgadoEventoPs obPersistencia = new TransitoJulgadoEventoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarTransitoJulgadoEventoNaoExtinto(idProcessoEventoExecucao, idEventoExecucao); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
	/**
	 * Lista os TransitoJulgadoEvento através do id_ProcessoExecucaoPenal, com condenações não extintas
	 */
	public List listarTodosTransitoJulgadoEventoNaoExtinto(String id_ProcessoExecucaoPenal, String id_EventoExecucao, boolean ordenarPeloEvento, boolean ordenarPeloTJ) throws Exception{
		List lista = null;
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TransitoJulgadoEventoPs obPersistencia = new TransitoJulgadoEventoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.listarTodosTransitoJulgadoEventoNaoExtinto(id_ProcessoExecucaoPenal, id_EventoExecucao, ordenarPeloEvento, ordenarPeloTJ); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lista;
	}
	
}
