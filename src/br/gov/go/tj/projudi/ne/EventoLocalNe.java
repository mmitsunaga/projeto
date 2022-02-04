package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.EventoLocalDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.EventoLocalPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class EventoLocalNe extends EventoLocalNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 2118053082262837871L;

    //---------------------------------------------------------
	public  String Verificar(EventoLocalDt dados ) {

		String stRetorno="";

		if (dados.getEventoExecucao().length()==0)
			stRetorno += "O Campo EventoExecucao é obrigatório.";
		if (dados.getLocalCumprimentoPena().length()==0)
			stRetorno += "O Campo LocalCumprimentoPena é obrigatório.";
		////System.out.println("..neEventoLocalVerificar()");
		return stRetorno;

	}
	
	public void salvar(EventoLocalDt dados, FabricaConexao obFabricaConexao, LogDt logDt) throws Exception {
		dados.setId_UsuarioLog(logDt.getId_Usuario());
		dados.setIpComputadorLog(logDt.getIpComputador());
		salvar(dados, obFabricaConexao);
	}
	
	public void salvar(EventoLocalDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		////System.out.println("..neEventoLocalsalvar()");
		EventoLocalPs obPersistencia = new EventoLocalPs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().equalsIgnoreCase("" ) ) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("EventoLocal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("EventoLocal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public void excluir(EventoLocalDt dados, FabricaConexao obFabricaConexao, LogDt logDt) throws Exception {
		dados.setId_UsuarioLog(logDt.getId_Usuario());
		dados.setIpComputadorLog(logDt.getIpComputador());
			excluir(dados, obFabricaConexao);
		
	}
	
	public void excluir(EventoLocalDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		EventoLocalPs obPersistencia = new EventoLocalPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("EventoLocal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados.getId()); dados.limpar();

		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public void excluirId_ProcessoEventoExecucao(EventoLocalDt dados, FabricaConexao obFabricaConexao, LogDt logDt) throws Exception {
		dados.setId_UsuarioLog(logDt.getId_Usuario());
		dados.setIpComputadorLog(logDt.getIpComputador());
		LogDt obLogDt;
		EventoLocalPs obPersistencia = new EventoLocalPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("EventoLocal", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluirId_ProcessoEventoExecucao(dados.getId_ProcessoEventoExecucao()); 
		dados.limpar();

		obLog.salvar(obLogDt, obFabricaConexao);
	}
}
