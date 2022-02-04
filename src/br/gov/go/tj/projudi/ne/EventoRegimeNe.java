package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.EventoRegimeDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.EventoRegimePs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class EventoRegimeNe extends EventoRegimeNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = 8786439321535954660L;

    //---------------------------------------------------------
	public  String Verificar(EventoRegimeDt dados ) {

		String stRetorno="";

		if (dados.getEventoExecucao().length()==0)
			stRetorno += "O Campo EventoExecucao é obrigatório.";
		if (dados.getRegimeExecucao().length()==0)
			stRetorno += "O Campo RegimeExecucao é obrigatório.";
		////System.out.println("..neEventoRegimeVerificar()");
		return stRetorno;

	}

	public void salvar(EventoRegimeDt dados, FabricaConexao obFabricaConexao, LogDt logDt) throws Exception {
		
		dados.setId_UsuarioLog(logDt.getId_Usuario());
		dados.setIpComputadorLog(logDt.getIpComputador());
		salvar(dados, obFabricaConexao);	
		
	}
	
	public void salvar(EventoRegimeDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		////System.out.println("..neEventoRegimesalvar()");
		
		EventoRegimePs obPersistencia = new EventoRegimePs(obFabricaConexao.getConexao());
		/* use o iu do objeto para saber se os dados ja estão ou não salvos */
		if (dados.getId().equalsIgnoreCase("" ) ) {				
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("EventoRegime", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {				
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("EventoRegime", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}
		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

	}
	
	public void excluir(EventoRegimeDt dados, FabricaConexao obFabricaConexao, LogDt logDt) throws Exception {
		dados.setId_UsuarioLog(logDt.getId_Usuario());
		dados.setIpComputadorLog(logDt.getIpComputador());
			excluir(dados, obFabricaConexao);
		
	}
	
	public void excluir(EventoRegimeDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		EventoRegimePs obPersistencia = new EventoRegimePs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("EventoRegime", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluir(dados.getId()); dados.limpar();

		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	public void excluirId_ProcessoEventoExecucao(EventoRegimeDt dados, FabricaConexao obFabricaConexao, LogDt logDt) throws Exception {
		dados.setId_UsuarioLog(logDt.getId_Usuario());
		dados.setIpComputadorLog(logDt.getIpComputador());
		LogDt obLogDt;
		EventoRegimePs obPersistencia = new EventoRegimePs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("EventoRegime", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
		obPersistencia.excluirId_ProcessoEventoExecucao(dados.getId_ProcessoEventoExecucao());
		dados.limpar();

		obLog.salvar(obLogDt, obFabricaConexao);
	}
}
