package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAusenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.ps.ProcessoParteAusenciaPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class ProcessoParteAusenciaNe extends ProcessoParteAusenciaNeGen {

	private static final long serialVersionUID = -2949392101395309878L;

	public String Verificar(ProcessoParteAusenciaDt dados) {

		String stRetorno = "";

		if (dados.getProcessoParteAusencia().length() == 0) stRetorno += "O Campo ProcessoParteAusencia � obrigat�rio.";
		if (dados.getProcessoParteAusenciaCodigo().length() == 0) stRetorno += "O Campo ProcessoParteAusenciaCodigo � obrigat�rio.";
		////System.out.println("..neProcessoParteAusenciaVerificar()");
		return stRetorno;

	}

	/**
	 * M�todo respons�vel em marcar a Aus�ncia de v�rias partes de um processo (Revelia ou Contum�cia)
	 * 
	 * @param listaPartesProcesso, partes do processo
	 * @param logDt, objeto com dados do log
	 * @param conexao, conex�o ativa
	 * @author msapaula
	 */
	public void marcarAusenciaProcessoParte(List listaPartesProcesso, LogDt logDt, FabricaConexao conexao) throws Exception {
		
		if (listaPartesProcesso != null) {
			for (int j = 0; j < listaPartesProcesso.size(); j++) {
				// Marca aus�ncia para uma ou mais partes do processo
				ProcessoParteDt parteDt = (ProcessoParteDt) listaPartesProcesso.get(j);
				this.marcarAusenciaProcessoParte(parteDt, logDt, conexao);
			}
		}

	}

	/**
	 * M�todo respons�vel em marcar a Aus�ncia de uma parte de processo (Revelia ou Contum�cia)
	 * 
	 * @param processoParteDt, parte do processo
	 * @param logDt, objeto com dados do log
	 * @param conexao, conex�o ativa
	 * @author msapaula
	 * @throws Exception 
	 */
	public void marcarAusenciaProcessoParte(ProcessoParteDt processoParteDt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		
		ProcessoParteAusenciaPs obPersistencia = new ProcessoParteAusenciaPs(obFabricaConexao.getConexao());

		String valorAtual = "[Id_ProcessoParte:" + processoParteDt.getId() + ";Id_ProcessoParteAusencia:]";
		String valorNovo = "[Id_ProcessoParte:" + processoParteDt.getId() + ";Id_ProcessoParteAusencia:" + ProcessoParteAusenciaDt.AUSENCIA_DECLARADA + "]";

		// Atualiza dados da parte
		LogDt obLogDt = new LogDt("ProcessoParte", processoParteDt.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
		obPersistencia.alterarAusenciaProcessoParte(processoParteDt.getId(), String.valueOf(ProcessoParteAusenciaDt.AUSENCIA_DECLARADA));
		obLog.salvar(obLogDt, obFabricaConexao);

	}

	/**
	 * M�todo respons�vel em Retirar a Aus�ncia de uma parte de processo (Revelia ou Contum�cia)
	 * 
	 * @param processoParteDt, parte do processo
	 * @param logDt, objeto com dados do log
	 * @param conexao, conex�o ativa
	 * @author msapaula
	 * @throws Exception 
	 */
	public void retirarAusenciaProcessoParte(ProcessoParteDt processoParteDt) throws Exception {
	    FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAusenciaPs obPersistencia = new ProcessoParteAusenciaPs(obFabricaConexao.getConexao());
			obFabricaConexao.iniciarTransacao();

			String valorAtual = "[Id_ProcessoParte:" + processoParteDt.getId() + ";Id_ProcessoParteAusencia:" + ProcessoParteAusenciaDt.AUSENCIA_DECLARADA + "]";
			String valorNovo = "[Id_ProcessoParte:" + processoParteDt.getId() + ";Id_ProcessoParteAusencia:]";

			// Atualiza dados da parte
			LogDt obLogDt = new LogDt("ProcessoParte", processoParteDt.getId(), processoParteDt.getId_UsuarioLog(), processoParteDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
			obPersistencia.alterarAusenciaProcessoParte(processoParteDt.getId(), null);
			obLog.salvar(obLogDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ProcessoParteAusenciaPs obPersistencia = new ProcessoParteAusenciaPs(obFabricaConexao.getConexao());
				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
}
