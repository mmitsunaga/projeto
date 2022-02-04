package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.projudi.ps.ProcessoEncaminhamentoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.PreparedStatementTJGO;
import br.gov.go.tj.utils.ResultSetTJGO;

//---------------------------------------------------------
public class ProcessoEncaminhamentoNe extends ProcessoEncaminhamentoNeGen{

//

/**
	 * 
	 */
	private static final long serialVersionUID = -3225371979472534198L;

	//---------------------------------------------------------
	public  String Verificar(ProcessoEncaminhamentoDt dados ) {

		String stRetorno="";

		if (dados.getServOrigem().length()==0)
			stRetorno += "O Campo ServOrigem é obrigatório.";
		if (dados.getServEncaminhamento().length()==0)
			stRetorno += "O Campo ServEncaminhamento é obrigatório.";
		if (dados.getUsuEncaminhamento().length()==0)
			stRetorno += "O Campo UsuEncaminhamento é obrigatório.";
		if (dados.getUsuRetorno().length()==0)
			stRetorno += "O Campo UsuRetorno é obrigatório.";

		return stRetorno;

	}

	public long consultarQuantidadeProcessosEncaminhados(String id_Serventia) throws Exception {
		long loQuantidade = 0;
		
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoEncaminhamentoPs obPersistencia = new ProcessoEncaminhamentoPs(obFabricaConexao.getConexao());
			loQuantidade = obPersistencia.consultarQuantidadeProcessosEncaminhados(id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return loQuantidade;
	}
	
	public void salvar(ProcessoEncaminhamentoDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;

		try {
			ProcessoEncaminhamentoPs obPersistencia = new ProcessoEncaminhamentoPs(obFabricaConexao.getConexao()); 

			/* use o id do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoEncaminhamento",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ProcessoEncaminhamento",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			
		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
		}
	}
	
	/**
	 * Consulta a tabela proc_encaminhamento. Caso haja algum encamminhamento com os parâmetros informados e sem retorno para a origem registrado, traz o id
	 * do registro na tabela. Caso não encontre nenhum encaminhamento nesta situação, retorna null. Usado para saber se deve inserir um novo registro
	 * na tabela proc_encaminhamento ou apenas complementar o retorno de um registro existente.
	 * 
	 * @param idProc
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 * @since 13/08/2017
	 */
	public String consultarEncaminhamentoSemDevolucao(String idProc) throws Exception {
		return this.consultarEncaminhamentoSemDevolucao(idProc, null);
	}
		
	public String consultarEncaminhamentoSemDevolucao(String idProc, FabricaConexao fabrica) throws Exception {
		String idProcEncaminhamento = null;
		FabricaConexao obFabricaConexao = null;

		try {
			
			if(fabrica != null) {
				obFabricaConexao = fabrica;
			}
			else {
				obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			}
			
			ProcessoEncaminhamentoPs obPersistencia = new ProcessoEncaminhamentoPs(obFabricaConexao.getConexao());
			idProcEncaminhamento = obPersistencia.consultarEncaminhamentoSemDevolucao(idProc);

		} finally {
			if(fabrica == null) {
				obFabricaConexao.fecharConexao();
			}
		}
		return idProcEncaminhamento;
	}
	
	public boolean podeEncaminhar(String id_proc) throws Exception{
		
		boolean boRetorno =false;
		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoEncaminhamentoPs obPersistencia = new ProcessoEncaminhamentoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.podeEncaminhar(id_proc);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
		
	}
	
	public boolean podeEncaminharOrigemDestino(String id_proc, String id_serv_origem, String id_serv_encaminhamento) throws Exception{
		
		boolean boRetorno =false;
		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ProcessoEncaminhamentoPs obPersistencia = new ProcessoEncaminhamentoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.podeEncaminharOrigemDestino(id_proc, id_serv_origem, id_serv_encaminhamento);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
		
	}

	/**
	 * Se houver algum encaminhamento em aberto para o idProc e idServEncaminhamento, retorna o id da serventia que encaminhou o processo.
	 * Se não houver nenhum encaminhamento em aberto para o idProc, retorna null. Utilizado para descobrir a serventia para a qual
	 * o processo deve ser devolvido.
	 * 
	 * @param idProc
	 * @param idServEncaminhamento
	 * @param conexao
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 * @since 13/08/2017
	 */
	public String retornaServentiaDevolucaoEncaminhamento(String idProc, String idServEncaminhamento, FabricaConexao conexao) throws Exception {
		String idServOrigem = null;
		FabricaConexao obFabricaConexao = null;
				
				try {
					if(conexao == null) {
						obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
					} else {
						obFabricaConexao = conexao;
					}
					ProcessoEncaminhamentoPs obPersistencia = new ProcessoEncaminhamentoPs(obFabricaConexao.getConexao());
					idServOrigem = obPersistencia.retornaServentiaDevolucaoEncaminhamento(idProc, idServEncaminhamento);
		
				} finally {
					if(conexao == null) {
						obFabricaConexao.fecharConexao();
					}
				}
				return idServOrigem;
	}

	/**
	 * Retorna o idServCargo do último magistrado responsável pelo processo na serventia especificada.
	 * Consulta a informação no ponteiro de redistribuição. 
	 * @param idProc
	 * @param idServ
	 * @return
	 * @throws Exception
	 */
	public String retornaUltimoResponsavelProcessoServentia(String idProc, String idServ, FabricaConexao fabricaConexao) throws Exception {
		
		ProcessoEncaminhamentoPs processoEncaminhamentoPs = new ProcessoEncaminhamentoPs(fabricaConexao.getConexao());
		return processoEncaminhamentoPs.retornaUltimoResponsavelProcessoServentia(idProc, idServ);
	
	}
	
}
