package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.RecursoDt;
import br.gov.go.tj.projudi.dt.RecursoExcluirDt;
import br.gov.go.tj.projudi.dt.RecursoNovoDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.RecursoPartePs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.TJDataHora;

//---------------------------------------------------------
public class RecursoParteNe extends RecursoParteNeGen {

	//

	/**
     * 
     */
    private static final long serialVersionUID = -8944138964197025288L;

    //---------------------------------------------------------
	public String Verificar(RecursoParteDt dados) {

		String stRetorno = "";

		if (dados.getNome().length() == 0) stRetorno += "O Campo Nome é obrigatório.";
		if (dados.getProcessoParteTipo().length() == 0) stRetorno += "O Campo ProcessoParteTipo é obrigatório.";
		////System.out.println("..neRecursoParteVerificar()");
		return stRetorno;

	}

	/**
	 * Insere partes recorrentes de um recurso
	 * @param listaPartes lista de partes recorrentes
	 * @param id_Recurso identificação do recurso
	 * @param id_ProcTipo identificação do processo tipo
	 * @param obFabricaConexao fábrica de conexão
	 * 
	 * @author msapaula
	 */
	public void inserirRecorrentes(List listaPartes, String id_Recurso, String id_ProcTipo, FabricaConexao obFabricaConexao) throws Exception {
		this.inserirRecursoParte(listaPartes, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, id_Recurso, id_ProcTipo, obFabricaConexao);
	}

	/**
	 * Insere partes recorridas de um processo
	 * @param listaPartes lista de partes recorridas
	 * @param id_Recurso identificação do recurso
	 * @param id_ProcTipo identificação do processo tipo
	 * @param id_ProcTipo identificação do processo tipo
	 * @param obFabricaConexao fábrica de conexão
	 * 
	 * @author msapaula
	 */
	public void inserirRecorridas(List listaPartes, String id_Recurso, String id_ProcTipo, FabricaConexao obFabricaConexao) throws Exception {
		this.inserirRecursoParte(listaPartes, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, id_Recurso, id_ProcTipo, obFabricaConexao);
	}
	
	/**
	 * Insere partes de um recurso
	 * @param listaPartes lista de partes recorrentes
	 * @param id_Recurso identificação do recurso
	 * @param id_ProcTipo identificação do processo tipo
	 * @param obFabricaConexao fábrica de conexão
	 * 
	 * @author lsbernardes
	 */
	public void inserirPromoventes(List listaPartes, String id_Recurso, String id_ProcTipo, FabricaConexao obFabricaConexao) throws Exception {
		this.inserirRecursoPartes(listaPartes, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, id_Recurso, id_ProcTipo, obFabricaConexao);
	}

	/**
	 * Insere partes de um processo
	 * @param listaPartes lista de partes recorridas
	 * @param id_Recurso identificação do recurso
	 * @param id_ProcTipo identificação do processo tipo
	 * @param obFabricaConexao fábrica de conexão
	 * 
	 * @author lsbernardes
	 */
	public void inserirPromovidos(List listaPartes, String id_Recurso, String id_ProcTipo, FabricaConexao obFabricaConexao) throws Exception {
		this.inserirRecursoPartes(listaPartes, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, id_Recurso, id_ProcTipo, obFabricaConexao);
	}

	/**
	 * Método utilizado na autuação de recurso para inserir várias partes.
	 * @param listaPartes: lista das parte do processo
	 * @param tipoParte: tipo da parte
	 * @param id_Recurso: id_recurso para o qual serão vinculaads as partes
	 * @param id_ProcTipo identificação do processo tipo
	 * @param obFabricaConexao fábrica de conexão
	 * 
	 * @author msapaula
	 */
	public void inserirRecursoParte(List listaPartes, int tipoParte, String id_Recurso, String id_ProcTipo, FabricaConexao obFabricaConexao) throws Exception {

		if (listaPartes != null && listaPartes.size() > 0) {
			RecursoPartePs obPersistencia = new RecursoPartePs(obFabricaConexao.getConexao());
			//Para cada parte da lista deve associar com o recurso
			for (int i = 0; i < listaPartes.size(); i++) {
				RecursoParteDt recursoParteDt = (RecursoParteDt) listaPartes.get(i);
				recursoParteDt.setId_Recurso(id_Recurso);
				recursoParteDt.setProcessoParteTipoCodigo(String.valueOf(tipoParte));
				recursoParteDt.setId_ProcessoTipo(id_ProcTipo);
				//Salva parte do recurso
				obPersistencia.inserir(recursoParteDt);

			}
		}
	}
	
	/**
	 * Método utilizado na autuação de recurso para inserir várias partes.
	 * @param listaPartes: lista das parte do processo
	 * @param tipoParte: tipo da parte
	 * @param id_Recurso: id_recurso para o qual serão vinculaads as partes
	 * @param id_ProcTipo identificação do processo tipo
	 * @param obFabricaConexao fábrica de conexão
	 * 
	 * @author lsbernardes
	 */
	public void inserirRecursoPartes(List listaPartes, int tipoParte, String id_Recurso, String id_ProcTipo, FabricaConexao obFabricaConexao) throws Exception {

		if (listaPartes != null && listaPartes.size() > 0) {
			RecursoPartePs obPersistencia = new RecursoPartePs(obFabricaConexao.getConexao());
			//Para cada parte da lista deve associar com o recurso
			for (int i = 0; i < listaPartes.size(); i++) {
				ProcessoParteDt processoPartedt = (ProcessoParteDt) listaPartes.get(i);
				RecursoParteDt recursoParteDt = new RecursoParteDt();
				recursoParteDt.setId_Recurso(id_Recurso);
				recursoParteDt.setId_ProcessoParte(processoPartedt.getId_ProcessoParte());
				recursoParteDt.setId_Processo(processoPartedt.getId_Processo());
				recursoParteDt.setProcessoParteTipoCodigo(String.valueOf(tipoParte));
				recursoParteDt.setId_ProcessoTipo(id_ProcTipo);
				//Salva parte do recurso
				obPersistencia.inserir(recursoParteDt);

			}
		}
	}

	/**
	 * Método que altera as partes de um recurso inominado.
	 * Compara a lista de partes editada com a lista anterior, e então exclui ou insere novos partes
	 * 
	 * @param recursoDt, dados do processo com lista de partes atual
	 * @param listaRecorrentesAnterior, lista de partes recorrentes antes da alteração 
	 * @param listaRecorridosAnterior, lista de partes recorridas antes da alteração
	 * @param id_ProcTipo identificação do processo tipo
	 * 
	 * @author msapaula
	 */
	public void alterarPartesRecurso(RecursoDt recursoDt, List listaRecorrentesAnterior, List listaRecorridosAnterior, String id_ProcTipo) throws Exception {
		LogDt logDt = new LogDt(recursoDt.getId_UsuarioLog(), recursoDt.getIpComputadorLog());
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			//Salva partes recorrentes e recorridas
			this.salvarPartesRecurso(recursoDt, recursoDt.getListaRecorrentes(), listaRecorrentesAnterior, ProcessoParteTipoDt.POLO_ATIVO_CODIGO, id_ProcTipo, logDt, obFabricaConexao);
			this.salvarPartesRecurso(recursoDt, recursoDt.getListaRecorridos(), listaRecorridosAnterior, ProcessoParteTipoDt.POLO_PASSIVO_CODIGO, id_ProcTipo, logDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

	}

	/**
	 * Método que salva múltiplas partes de um recurso.
	 * Compara a lista de partes editada com a lista anterior, e então exclui ou insere novos partes
	 * 
	 * @param recursoDt, dados do processo com lista de partes atual
	 * @param listaAtualPartes, lista de partes depois da alteração 
	 * @param listaAnterior, lista depois da alteração
	 * @param processoParteTipo, tipo da parte a ser inserida
	 * @param logDt, objeto log
	 * @param conexao, conexão ativa
	 * 
	 * @author msapaula
	 */
	public void salvarPartesRecurso(RecursoDt recursoDt, List listaAtual, List listaAnterior, int processoParteTipo, String id_ProcTipo, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		
		RecursoPartePs obPersistencia = new RecursoPartePs(obFabricaConexao.getConexao());

		List listaExcluir = new ArrayList(); //Lista dos assuntos a serem excluídos
		List listaIncluir = new ArrayList(); //Lista dos novos assuntos a serem salvos

		//Pega a lista anterior dos assuntos e procura se algum foi excluído
		if(listaAnterior != null){
			for (int i = 0; i < listaAnterior.size(); i++) {
				RecursoParteDt parteAnterior = (RecursoParteDt) listaAnterior.get(i);
				boolean boEncontrado = false;
				//Verifica qual assunto saiu da lista
				for (int j = 0; j < listaAtual.size(); j++) {
					RecursoParteDt parteAtual = (RecursoParteDt) listaAtual.get(j);
					if (parteAnterior.getId_ProcessoParte().equals(parteAtual.getId_ProcessoParte())) {
						boEncontrado = true;
						break;
					}
				}
				//se o id do assunto não foi encontrado na lista editada coloco o objeto para ser excluido
				if (!boEncontrado) listaExcluir.add(parteAnterior);
			}
		}

		//Verifica os assuntos a serem incluídos
		for (int i = 0; i < listaAtual.size(); i++) {
			RecursoParteDt parteAtual = (RecursoParteDt) listaAtual.get(i);
			boolean boEncontrado = false;
			if(listaAnterior != null){
				for (int j = 0; j < listaAnterior.size(); j++) {
					RecursoParteDt parteAnterior = (RecursoParteDt) listaAnterior.get(j);
					if (parteAnterior.getId_ProcessoParte().equals(parteAtual.getId_ProcessoParte())) {
						boEncontrado = true;
						break;
					}
				}
			}
			if (!boEncontrado) listaIncluir.add(parteAtual);
		}

		for (int i = 0; i < listaIncluir.size(); i++) {
			RecursoParteDt obDt = (RecursoParteDt) listaIncluir.get(i);
			obDt.setId_Recurso(recursoDt.getId());
			obDt.setProcessoParteTipoCodigo(String.valueOf(processoParteTipo));
			obDt.setId_ProcessoTipo(id_ProcTipo);
			LogDt obLogDt = new LogDt("RecursoParte", obDt.getId(),logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Incluir), "", obDt.getPropriedades());
			obPersistencia.inserir(obDt);
			obLog.salvar(obLogDt, obFabricaConexao);
		}

		for (int i = 0; i < listaExcluir.size(); i++) {
			RecursoParteDt obDtTemp = (RecursoParteDt) listaExcluir.get(i);
			obDtTemp.setDataBaixa(Funcoes.DataHora(new Date()));
			obDtTemp.setProcessoParteTipoCodigo(String.valueOf(processoParteTipo));
			obDtTemp.setId_ProcessoTipo(id_ProcTipo);
			LogDt obLogDt = new LogDt("RecursoParte",obDtTemp.getId(), logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.Alterar), obDtTemp.getPropriedades(), "");
			obPersistencia.alterar(obDtTemp);
			obLog.salvar(obLogDt, obFabricaConexao);
		}


	}
	
	/**
	 * Exclui partes do recurso quer será convertido em processo
	 * @param id_Recurso: identificação do recurso
	 * 
	 * @author lsbernardades
	 */
	public void excluirRecursoPartes(String id_recurso, FabricaConexao obFabricaConexao) throws Exception {
		
		RecursoPartePs obPersistencia = new RecursoPartePs(obFabricaConexao.getConexao());
		obPersistencia.excluirRecursoPartes(id_recurso);
		
	}
	
	/**
	 * Altera os dados de um recurso parte.
	 * 
	 * @param dados
	 * @param obFabricaConexao
	 * @author mmgomes
	 * @throws Exception
	 */
	public void alterar(RecursoParteDt recursoParteDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;		
			
		RecursoPartePs obPersistencia = new  RecursoPartePs(obFabricaConexao.getConexao());
					
		obDados = obPersistencia.consultarId(recursoParteDt.getId());
		obPersistencia.alterar(recursoParteDt);
		obLogDt = new LogDt("RecursoParte", recursoParteDt.getId(), recursoParteDt.getId_UsuarioLog(), recursoParteDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), recursoParteDt.getPropriedades());			

		obDados.copiar(recursoParteDt);
		obLog.salvar(obLogDt, obFabricaConexao);
	}
	
	/**
	 * Consulta um recurso parte
	 * @param id_recursoparte
	 * @param obFabricaConexao
	 * @return
	 * @author mmgomes
	 * @throws Exception
	 */
	public RecursoParteDt consultarId(String id_recursoparte, FabricaConexao obFabricaConexao) throws Exception {
		RecursoParteDt dtRetorno=null;		 
		
		RecursoPartePs obPersistencia = new  RecursoPartePs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_recursoparte ); 
		obDados.copiar(dtRetorno);
	
		return dtRetorno;
	}
	
	/**
	 * Verifica se já existe um recurso ativo com a classe informada.
	 * @param id_recurso
	 * @param id_ProcessoTipo
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 */
	public boolean existeRecursoParteAtivoNaClasse(String id_recurso, String id_ProcessoTipo, FabricaConexao obFabricaConexao) throws Exception {	
		RecursoPartePs obPersistencia = new  RecursoPartePs(obFabricaConexao.getConexao());
		return obPersistencia.existeRecursoParteAtivoNaClasse(id_recurso, id_ProcessoTipo);
	}
	
	/**
	 * Consulta uma lista de recursos ativos com a classe informada.
	 * @param id_recurso
	 * @param id_ProcessoTipo
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 */
	public List<RecursoParteDt> consultar(String id_recurso, String id_ProcessoTipo, FabricaConexao obFabricaConexao) throws Exception {
		RecursoPartePs obPersistencia = new  RecursoPartePs(obFabricaConexao.getConexao());
		return obPersistencia.consultar(id_recurso, id_ProcessoTipo);
	}
	
	public String consultarDescricaoProcessoTipoJSON(String stNomeBusca1, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ProcessoTipoNe neObjeto = new ProcessoTipoNe();
		
		stTemp = neObjeto.consultarProcessoTipoJSON(stNomeBusca1 , posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public String verificarAdicionarRecursoSegundoGrau(RecursoDt recursoDt, String id_ProcessoTipo) throws Exception {
		String stRetorno = "";
		if (id_ProcessoTipo == null || id_ProcessoTipo.trim().length() == 0) {
			stRetorno = "Selecione a classe do novo recurso";
		}
		
		if (recursoDt.getListaRecorrentes().size() == 0 && recursoDt.getListaRecorridos().size() == 0) {
			if (stRetorno.length() > 0) stRetorno += "\n";
			stRetorno += "Selecione as partes do recurso";
		}
		
		if (stRetorno.length() == 0) {
			FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try {
				List<RecursoParteDt> partes = this.consultar(recursoDt.getId(), id_ProcessoTipo, obFabricaConexao);
				if (partes.size() > 0) {
					stRetorno += "Já existe um recurso ativo com a classe informada.";		
				}
			} finally {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return stRetorno;

	}
	
	public void adicionarRecursoSegundoGrau(RecursoDt recursoDt, RecursoNovoDt recursoNovoDt, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			RecursoPartePs obPersistencia = new RecursoPartePs(obFabricaConexao.getConexao());
			
			if (recursoDt.getListaRecorrentes() != null) {
				for (int i = 0; i < recursoDt.getListaRecorrentes().size(); i++) {
					RecursoParteDt obDt = new RecursoParteDt();
					obDt.copiar(((RecursoParteDt) recursoDt.getListaRecorrentes().get(i)));
					obDt.setId("");
					
					obDt.setId_Recurso(recursoDt.getId());
					obDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_ATIVO_CODIGO));
					obDt.setId_ProcessoTipo(recursoNovoDt.getId_ProcessoTipo());
					
					LogDt obLogDt = new LogDt("RecursoParte", obDt.getId(), recursoDt.getId_UsuarioLog(), recursoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", obDt.getPropriedades());
					
					obPersistencia.inserir(obDt);
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}	
			
			if (recursoDt.getListaRecorridos() != null) {
				for (int i = 0; i < recursoDt.getListaRecorridos().size(); i++) {
					RecursoParteDt obDt = new RecursoParteDt();
					obDt.copiar(((RecursoParteDt) recursoDt.getListaRecorridos().get(i)));	
					obDt.setId("");
					
					obDt.setId_Recurso(recursoDt.getId());
					obDt.setProcessoParteTipoCodigo(String.valueOf(ProcessoParteTipoDt.POLO_PASSIVO_CODIGO));
					obDt.setId_ProcessoTipo(recursoNovoDt.getId_ProcessoTipo());
					
					LogDt obLogDt = new LogDt("RecursoParte", obDt.getId(), recursoDt.getId_UsuarioLog(), recursoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", obDt.getPropriedades());
					
					obPersistencia.inserir(obDt);
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}
			
			LogDt logDt = new LogDt(recursoNovoDt.getId_UsuarioLog(), recursoNovoDt.getIpComputadorLog());
			
			// Gera movimentação RECURSO AUTUADO
			movimentacaoNe.gerarMovimentacaoRecursoInserido(recursoDt.getId_Processo(), usuarioDt.getId_UsuarioServentia(), "(Recurso " + recursoNovoDt.getProcessoTipo() + ")", logDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarProcessoTipoServentiaRecursoJSON(String stNomeBusca1, String id_recurso, boolean somenteAtivos, String posicaoPaginaAtual) throws Exception {
		String stTemp = "";
		ProcessoTipoNe neObjeto = new ProcessoTipoNe();
		
		stTemp = neObjeto.consultarProcessoTipoServentiaRecursoJSON(stNomeBusca1, id_recurso, somenteAtivos , posicaoPaginaAtual);
		neObjeto = null;
		return stTemp;
	}
	
	public List<ProcessoTipoDt> consultarProcessoTipoServentiaRecurso(String stNomeBusca1, String id_recurso, boolean somenteRecursosAtivos) throws Exception {
		List<ProcessoTipoDt> liTemp;
		ProcessoTipoNe neObjeto = new ProcessoTipoNe();
		liTemp = neObjeto.consultarProcessoTipoServentiaRecurso(stNomeBusca1, id_recurso, somenteRecursosAtivos);
		neObjeto = null;
		return liTemp;
	}
	
	public String verificarRemoverRecursoSegundoGrau(RecursoDt recursoDt, String id_ProcessoTipo) throws Exception {
		String stRetorno = "";
		if (id_ProcessoTipo == null || id_ProcessoTipo.trim().length() == 0) {
			stRetorno = "Selecione a classe do recurso";
		}
		
		if (stRetorno.length() == 0) {
			FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try {
				List<RecursoParteDt> partes = this.consultar(recursoDt.getId(), id_ProcessoTipo, obFabricaConexao);
				if (partes == null || partes.size() == 0) {
					stRetorno += "Não existe um recurso ativo com a classe informada.";		
				}
			} finally {
				obFabricaConexao.fecharConexao();
			}
		}
		
		return stRetorno;
	}
	
	public void removerRecursoSegundoGrau(RecursoDt recursoDt, RecursoExcluirDt RecursoExcluirdt, UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = null;
		MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
		RecursoNe recursoNe = new RecursoNe();
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			obFabricaConexao.iniciarTransacao();
			
			LogDt logDt = new LogDt(RecursoExcluirdt.getId_UsuarioLog(), RecursoExcluirdt.getIpComputadorLog());
			
			List<RecursoParteDt> recursosAtivos = this.consultar(recursoDt.getId(), obFabricaConexao);
			
			if (recursosAtivos != null) {
				TJDataHora dataHoraAtual = new TJDataHora();
				String primeiroRecursoAdicional = "";
				
				for (RecursoParteDt recursoParteDt : recursosAtivos) {
					if (recursoParteDt.getId_ProcessoTipo().equalsIgnoreCase(RecursoExcluirdt.getId_ProcessoTipo())) {
						
						recursoParteDt.setId_UsuarioLog(RecursoExcluirdt.getId_UsuarioLog());
						recursoParteDt.setIpComputadorLog(RecursoExcluirdt.getIpComputadorLog());
						recursoParteDt.setDataBaixa(dataHoraAtual.getDataFormatadaddMMyyyyHHmmssSemSeparador());
						
						this.alterar(recursoParteDt, obFabricaConexao);	
					} else if (primeiroRecursoAdicional.length() == 0) {
						primeiroRecursoAdicional = recursoParteDt.getId_ProcessoTipo();
					}
				}
				
				if (RecursoExcluirdt.getId_ProcessoTipo().equalsIgnoreCase(recursoDt.getId_ProcessoTipo()) && primeiroRecursoAdicional.length() > 0) {
					recursoDt.setId_ProcessoTipo(primeiroRecursoAdicional);
					recursoNe.alterarProcessoTipoRecurso(recursoDt, logDt, usuarioDt.getId_UsuarioServentia(), false, obFabricaConexao);
				}
			}
			
			// Gera movimentação RECURSO EXCLUÍDO
			movimentacaoNe.gerarMovimentacaoRecursoExcluido(recursoDt.getId_Processo(), usuarioDt.getId_UsuarioServentia(), "(Recurso " + RecursoExcluirdt.getProcessoTipo() + ")", logDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Consulta uma lista de recursos ativos.
	 * @param id_recurso
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 */
	public List<RecursoParteDt> consultar(String id_recurso, FabricaConexao obFabricaConexao) throws Exception {
		RecursoPartePs obPersistencia = new  RecursoPartePs(obFabricaConexao.getConexao());
		return obPersistencia.consultar(id_recurso);
	}
}
