package br.gov.go.tj.projudi.ne;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.TarefaDt;
import br.gov.go.tj.projudi.ps.TarefaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.TJDataHora;


public class TarefaNe extends TarefaNeGen{

/**
     * 
     */
    private static final long serialVersionUID = -6438455975499189500L;

    //---------------------------------------------------------
	public  String Verificar(TarefaDt dados ) {
		String stRetorno="";
		if (dados.getTarefa().length()==0)
			stRetorno += "O Campo Tarefa é obrigatório.";
		if (dados.getProjeto().length()==0)
			stRetorno += "O Campo Projeto é obrigatório.";
		return stRetorno;
	}
	
	/**
	 * Pesquisa uma tarefa de acordo com a situação da mesma.
	 * 
	 * @param nome
	 * @param aberta
	 * @param emAndamento
	 * @param aguardaVisto
	 * @param finalizados
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public List pesquisarTarefa(String nome, String idStatusTarefa, String idPrioridadeTarefa, String idProjeto, String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
				
				//filtra as tarefas pela situação
				tempList=obPersistencia.pesquisarTarefa(nome, idStatusTarefa, idPrioridadeTarefa, idProjeto, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public String pesquisarTarefaJSON(String nome, String idStatusTarefa, String idPrioridadeTarefa, String idProjeto, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
				
				//filtra as tarefas pela situação
				stTemp=obPersistencia.pesquisarTarefaJSON(nome, idStatusTarefa, idPrioridadeTarefa, idProjeto, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}

	/**
	 * Recupera a lista de tarefas que o participante é o responsável
	 * 
	 * @param nome
	 * @param aberta
	 * @param emAndamento
	 * @param aguardaVisto
	 * @param finalizados
	 * @param idServentiaCargo
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public List pesquisarTarefaByParticipante(String nome, String idStatusTarefa, String idPrioridadeTarefa, String idProjeto, String idServentiaCargo,  String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			
			String idParticipante = this.consultarIdParticipanteByServentiaCargo(idServentiaCargo, obFabricaConexao);
			//filtra as tarefas pela situação
			tempList=obPersistencia.pesquisarTarefaByParticipante(nome, idStatusTarefa, idPrioridadeTarefa, idProjeto, idParticipante, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public String pesquisarTarefaByParticipanteJSON(String nome, String idStatusTarefa, String idPrioridadeTarefa, String idProjeto, String idServentiaCargo,  String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			
			String idParticipante = this.consultarIdParticipanteByServentiaCargo(idServentiaCargo, obFabricaConexao);
			//filtra as tarefas pela situação
			stTemp=obPersistencia.pesquisarTarefaByParticipanteJSON(nome, idStatusTarefa, idPrioridadeTarefa, idProjeto, idParticipante, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

	/**
	 * Recupera a lista de tarefas que estão aguardando visto.
	 * 
	 * @param nome
	 * @param idProjeto
	 * @param posicao
	 * @return
	 * @throws Exception
	 */
	public List pesquisarTarefaAguardandoVisto(String nome, String idProjeto, String idUsuarioCriador, String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			String idAguardandoVisto = this.consultarIdStatusAguardandoVisto(obFabricaConexao);
			//filtra as tarefas pela situação
			tempList=obPersistencia.pesquisarTarefaAguardandoVisto(nome, idAguardandoVisto, idUsuarioCriador, idProjeto, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public String consultarDescricaoJSON(String descricaoProjeto, String descricaoTarefa, String idUsuarioCriador, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
				String idAguardandoVisto = this.consultarIdStatusAguardandoVisto(obFabricaConexao);				
				stTemp = obPersistencia.consultarDescricaoJSON(descricaoProjeto, descricaoTarefa, idAguardandoVisto, idUsuarioCriador, posicao);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}
	
	public List pesquisarTarefaNaoFinalizada(String nome, String idProjeto, String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			//filtra as tarefas pela situação
			tempList=obPersistencia.pesquisarTarefaNaoFinalizada(nome, idProjeto, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public String pesquisarTarefaNaoFinalizadaJSON(String nome, String idProjeto, String posicao ) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			//filtra as tarefas pela situação
			stTemp=obPersistencia.pesquisarTarefaNaoFinalizadaJSON(nome, idProjeto, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	/**
	 * Altera a tarefa modificando o Status do Objeto ao qual a mesma está
	 * vinculada.
	 * 
	 * @param dados
	 * @param codigoStatusTarefa
	 * @throws Exception
	 */
	public void salvarAlterarStatusEmAndamento(TarefaDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			
			String idStatusEmAndamento = this.consultarIdStatusEmAndamento(obFabricaConexao);
			dados.setId_TarefaStatus(idStatusEmAndamento);
			
			obPersistencia.alterar(dados); 
			obLogDt = new LogDt("Tarefa",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Altera o status de uma tarefa segundo o novo status informado.
	 * @param tarefaDt
	 * @throws Exception
	 */
	public void alterarStatusTarefa(TarefaDt tarefaDt, String novoStatusTarefa) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			
			tarefaDt.setId_TarefaStatus(novoStatusTarefa);
			obPersistencia.alterar(tarefaDt); 
			
			obLogDt = new LogDt("Tarefa",tarefaDt.getId(), tarefaDt.getId_UsuarioLog(),tarefaDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),tarefaDt.getPropriedades());
			obDados.copiar(tarefaDt);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Altera a tarefa modificando o Status do Objeto ao qual a mesma está
	 * vinculada.
	 * 
	 * @param dados
	 * @param codigoStatusTarefa
	 * @throws Exception
	 */
	public void salvarAlterarStatusAberto(TarefaDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			
			// recupera a classe de negócio de status de tarefa
			String idTarefaStatus = this.consultarIdStatusAberto(obFabricaConexao);
			dados.setId_TarefaStatus(idTarefaStatus);
			obPersistencia.inserir(dados); 
			obLogDt = new LogDt("Tarefa",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Marca a tarefa como solucionada
	 * @param dados
	 * @throws Exception
	 */
	public void solucionarTarefa(TarefaDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			
			// recupera marca a tarefa como Aguardando Visto
			String idStatusEmAndamento = this.consultarIdStatusAguardandoVisto(obFabricaConexao);
			dados.setId_TarefaStatus(idStatusEmAndamento);
			dados.setDataFim(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
						
			obPersistencia.alterar(dados); 
			obLogDt = new LogDt("Tarefa",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Recupea o Status de Tarefa em aberto.
	 * @return String
	 * @throws Exception
	 */
	private String consultarIdStatusAberto(FabricaConexao cn) throws Exception{
		return new TarefaStatusNe().consultarIdStatusAberto(cn);
	}

	/**
	 * Atribui a tarefa mais antiga vinculada ao projeto ao 
	 * usuário serventia passado como parâmetro.
	 * 
	 * @param idUsuarioServentia
	 * @param idProjeto
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String atribuirUltimaTarefa(String idServentiaCargo, String idProjeto) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			
			//-----------------------------------
			//---- RECUPERA A ÚLTIMA TAREFA
			//-----------------------------------
			String idTarefa = obPersistencia.buscarIdPrimeiraTarefaPendenteByProjeto(idProjeto);
			if(idTarefa == null){
				return "Não foi encontrada nenhuma tarefa pendente vinculada ao projeto selecionado.";
			}
			
			//-------------------------------------------------
			//--- ALTERA O STATUS DA TAREFA PARA "EM ANDAMENTO"
			//-------------------------------------------------
			String idStatusEmAndamento = this.consultarIdStatusEmAndamento(obFabricaConexao);
			
			//-----------------------------------
			//--- RECUPERA O ID DO PROJETO PARTICIPANTE
			//-----------------------------------
			String idParticipante = this.consultarIdParticipanteByServentiaCargo(idServentiaCargo, obFabricaConexao);
			if(idParticipante == null){
				return "Não foi encontrada nenhum participante vinculado ao projeto selecionado.";
			}
			//------------------------------------
			//--- VINCULA A TAREFA AO PARTICIPANTE
			//------------------------------------
			obPersistencia.atribuirTarefaResponsavel(idTarefa, idParticipante, idStatusEmAndamento);
			obFabricaConexao.finalizarTransacao();
			return idTarefa;
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Consulta os participantes do projeto segundo as informações repassadas.
	 * @param tempNomeBusca - nome do projeto
	 * @param idProjeto - id do projeto
	 * @param PosicaoPaginaAtual - posição da página
	 * @return lista de participantes
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarParticipanteDescricaoProjeto(String tempNomeBusca, String idProjeto, String PosicaoPaginaAtual) throws Exception {
		List tempList=null;

		ProjetoParticipanteNe projetoParticipanteNe = new ProjetoParticipanteNe(); 
		tempList = projetoParticipanteNe.consultarParticipanteDescricaoProjeto(tempNomeBusca, idProjeto, PosicaoPaginaAtual);
		QuantidadePaginas = projetoParticipanteNe.getQuantidadePaginas();
		projetoParticipanteNe = null;

		return tempList;
	}

	/**
	 * Recupera o Status de Tarefa Aguardando Visto
	 * @return String
	 * @throws Exception
	 */
	private String consultarIdStatusAguardandoVisto(FabricaConexao cn) throws Exception{
		return new TarefaStatusNe().consultarIdStatusAguardandoVisto(cn);
	}
	
	/**
	 * Recupera o Status da Tarefa em andamento.
	 * @return String
	 * @throws Exception
	 */
	private String consultarIdStatusEmAndamento(FabricaConexao cn) throws Exception{
		return new TarefaStatusNe().consultarIdStatusEmAndamento(cn);
	}

	/**
	 * Recupera o Status da Tarefa em andamento.
	 * @return String
	 * @throws Exception
	 */
	private String consultarIdParticipanteByServentiaCargo(String idServentiaCargo, FabricaConexao cn) throws Exception{
		return new ProjetoParticipanteNe().consultarIdParticipanteByServentiaCargo(idServentiaCargo, cn);
	}

	/**
	 * Retorna uma lista de tarefas para emissão de relatório em tela 
	 * 
	 * @param periodoInicialUtilizado
	 * @param periodoFinalUtilizado
	 * @param idProjeto
	 * @param idServentiaCargo
	 * @author Márcio Gomes
	 * @return
	 * @throws Exception 
	 */
	public List obtenhaRelatorioListaTarefas(TJDataHora periodoInicialUtilizado, TJDataHora periodoFinalUtilizado, String idProjeto, String idServentiaCargo) throws Exception{		
		List listaRetorno = null;
		FabricaConexao obFabricaConexao = null;
		 obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			
			TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
			
			listaRetorno = obPersistencia.obtenhaRelatorioListaTarefas(periodoInicialUtilizado, periodoFinalUtilizado, idProjeto, idServentiaCargo);					
					
		} finally{
			if (obFabricaConexao != null) obFabricaConexao.fecharConexao();
		}
		
		return listaRetorno;
		
	}

	public String consultarDescricaoProjetoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		ProjetoNe Projetone = new ProjetoNe(); 
		stTemp = Projetone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		Projetone = null;
		return stTemp;
	}

	public String consultarDescricaoTarefaPrioridadeJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		TarefaPrioridadeNe TarefaPrioridadene = new TarefaPrioridadeNe(); 
		stTemp = TarefaPrioridadene.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		TarefaPrioridadene = null;
		return stTemp;
	}

	public String consultarDescricaoTarefaStatusJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		try{
			TarefaStatusNe TarefaStatusne = new TarefaStatusNe(); 
			stTemp = TarefaStatusne.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
			TarefaStatusne = null;
		}catch(Exception e){
			
			throw e;
		}
		return stTemp;
	}

	public String consultarDescricaoTarefaTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		TarefaTipoNe TarefaTipone = new TarefaTipoNe(); 
		stTemp = TarefaTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		TarefaTipone = null;
		return stTemp;
	}

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				TarefaPs obPersistencia = new TarefaPs(obFabricaConexao.getConexao());
				stTemp=obPersistencia.consultarDescricaoJSON( descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;   
	}

}
