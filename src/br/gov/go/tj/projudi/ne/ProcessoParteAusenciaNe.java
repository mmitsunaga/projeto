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

		if (dados.getProcessoParteAusencia().length() == 0) stRetorno += "O Campo ProcessoParteAusencia é obrigatório.";
		if (dados.getProcessoParteAusenciaCodigo().length() == 0) stRetorno += "O Campo ProcessoParteAusenciaCodigo é obrigatório.";
		////System.out.println("..neProcessoParteAusenciaVerificar()");
		return stRetorno;

	}

	/**
	 * Método responsável em marcar a Ausência de várias partes de um processo (Revelia ou Contumácia)
	 * 
	 * @param listaPartesProcesso, partes do processo
	 * @param logDt, objeto com dados do log
	 * @param conexao, conexão ativa
	 * @author msapaula
	 */
	public void marcarAusenciaProcessoParte(List listaPartesProcesso, LogDt logDt, FabricaConexao conexao) throws Exception {
		
		if (listaPartesProcesso != null) {
			for (int j = 0; j < listaPartesProcesso.size(); j++) {
				// Marca ausência para uma ou mais partes do processo
				ProcessoParteDt parteDt = (ProcessoParteDt) listaPartesProcesso.get(j);
				this.marcarAusenciaProcessoParte(parteDt, logDt, conexao);
			}
		}

	}

	/**
	 * Método responsável em marcar a Ausência de uma parte de processo (Revelia ou Contumácia)
	 * 
	 * @param processoParteDt, parte do processo
	 * @param logDt, objeto com dados do log
	 * @param conexao, conexão ativa
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
	 * Método responsável em Retirar a Ausência de uma parte de processo (Revelia ou Contumácia)
	 * 
	 * @param processoParteDt, parte do processo
	 * @param logDt, objeto com dados do log
	 * @param conexao, conexão ativa
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
