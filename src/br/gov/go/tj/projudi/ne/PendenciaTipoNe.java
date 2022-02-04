package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.RecursoParteDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.dwrCombo;
import br.gov.go.tj.projudi.ps.ArquivoTipoPs;
import br.gov.go.tj.projudi.ps.PendenciaTipoPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Relatorios;

//---------------------------------------------------------
public class PendenciaTipoNe extends PendenciaTipoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6443160847316044081L;

	public String Verificar(PendenciaTipoDt dados) {

		String stRetorno = "";
		if (dados.getPendenciaTipo().equalsIgnoreCase("")) {
			stRetorno += "Descri��o � � obrigat�rio.";
		}
		return stRetorno;

	}

	/**
	 * Consultar objeto PendenciaTipo equivalente ao c�digo passado
	 */
	public PendenciaTipoDt consultarPendenciaTipoCodigo(int pendenciaTipoCodigo) throws Exception {
		PendenciaTipoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaTipoPs obPersistencia = new PendenciaTipoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarPendenciaTipoCodigo(pendenciaTipoCodigo);
			obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * M�todo respons�vel em montar os destinat�rios poss�veis de uma pend�ncia,
	 * no momento em que esta est� sendo gerada, seja na movimenta��o do processo ou na an�lise de conclus�es.
	 * 
	 * Os destinat�rios ser�o tratados da seguinte forma:
	 * 		Identificador 1 = todos promoventes 
	 * 		Identificador 2 = todos promovidos
	 * 		Identificador 3 = pend�ncia vinculada a uma parte espec�fica (deve ser passado o Id da parte) 
	 * 		Identificador 4 = pend�ncia para Minist�rio Publico
	 * 
	 * @param tipoPendencia, tipo de pend�ncia selecionada pelo usu�rio, de acordo com esse tipo ser�o retornados os destinat�rios poss�veis
	 * @param processoDt, Processo envolvido
	 * 
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List montarListaDestinatarios(String tipoPendencia, ProcessoDt processoDt, String serventiaSubTipoCodigo, String id_ServentiaLogada) throws Exception{
		List listaDestinatarios = new ArrayList();

		//A primeira posi��o define se determinadas op��es devem ser mostradas ou ocultadas
		//Se valor for opcoesIntimacao = determina que checkbox "Pessoal e Advogados", "Pessoal" e "Intima��o em Audi�ncia" aparecer�o
		//Se valor for opcoes = determina que op��es de prazo e urg�ncia devem ser mostradas
		//Se valor for sessao = determina que op��es de marcar sess�o devem ser mostradas
		//Se valor for semOpcoes = determina que op��es de prazo e urg�ncia n�o devem ser mostradas
		switch (Funcoes.StringToInt(tipoPendencia)) {

			case PendenciaTipoDt.CARTA_NOTIFICACAO:
			case PendenciaTipoDt.MANDADO:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinat�rios poss�veis
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				break;
			case PendenciaTipoDt.CARTA_CITACAO:
			case PendenciaTipoDt.INTIMACAO:
				listaDestinatarios.add(new String("opcoesIntimacao")); //Op��es de intima��o
				//Captura destinat�rios poss�veis
				listaDestinatarios.add(new dwrCombo("Ambas as Partes", "6"));
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				listaDestinatarios.add(new dwrCombo("Minist�rio P�blico 1� Grau", "4"));				
				listaDestinatarios.add(new dwrCombo("Minist�rio P�blico 2� Grau", "8"));				
				listaDestinatarios.add(new dwrCombo("Minist�rio P�blico Turma Julgadora", "7"));					
				
				break;
			case PendenciaTipoDt.INTIMACAO_VIA_TELEFONE:
				listaDestinatarios.add(new String("opcoesIntimacaoTelefone")); //Op��es de intima��o
				//Captura destinat�rios poss�veis
				listaDestinatarios.add(new dwrCombo("Ambas as Partes", "6"));
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				listaDestinatarios.add(new dwrCombo("Minist�rio P�blico 1� Grau", "4"));				
				listaDestinatarios.add(new dwrCombo("Minist�rio P�blico 2� Grau", "8"));						
				listaDestinatarios.add(new dwrCombo("Minist�rio P�blico Turma Julgadora", "7"));								
				
				break;

			case PendenciaTipoDt.MARCAR_SESSAO:
			case PendenciaTipoDt.REMARCAR_SESSAO:
			case PendenciaTipoDt.MARCAR_SESSAO_EXTRA_PAUTA:
				if (processoDt != null && Funcoes.StringToLong(processoDt.getId_Recurso()) > 0) listaDestinatarios.add(new String("sessaoClasse"));
				else listaDestinatarios.add(new String("sessao"));
				getListaDestinatariosMagistradosSegundoGrau(processoDt, listaDestinatarios, serventiaSubTipoCodigo, id_ServentiaLogada);
				break;

			case PendenciaTipoDt.CONTUMACIA:
				listaDestinatarios.add("semOpcoes");
				getListaDestinatariosContumacia(processoDt, listaDestinatarios);
				break;
				
			case PendenciaTipoDt.REVELIA:
				listaDestinatarios.add("semOpcoes");
				getListaDestinatariosRevelia(processoDt, listaDestinatarios);
				break;
				
			case PendenciaTipoDt.PEDIDO_VISTA:
			case PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU:
				listaDestinatarios.add("magistrados");
				getListaDestinatariosMagistradosSegundoGrau(processoDt, listaDestinatarios, serventiaSubTipoCodigo, id_ServentiaLogada);
				break;
				
			case PendenciaTipoDt.ENVIAR_INSTANCIA_SUPERIOR:
				listaDestinatarios.add("semOpcoes");
				getListaDestinatariosEnvioInstanciaSuperior(processoDt, id_ServentiaLogada, listaDestinatarios);
				break;
			
			case PendenciaTipoDt.ARQUIVAMENTO:
				if (processoDt != null && processoDt.isCriminal()){
					listaDestinatarios.add("opcoesArquivamento");
				} else {
					listaDestinatarios.add("semOpcoes");
				}
				break;
				
			case PendenciaTipoDt.RETORNAR_SERVENTIA_ORIGEM:
			case PendenciaTipoDt.DESARQUIVAMENTO:
			case PendenciaTipoDt.TORNAR_SIGILOSO:
			case PendenciaTipoDt.RETIRAR_SIGILO:
			case PendenciaTipoDt.DESMARCAR_SESSAO:
			case PendenciaTipoDt.FINALIZAR_SUSPENSAO_PROCESSO:
			case PendenciaTipoDt.FINALIZAR_SUSPENSAO_PROCESSO_ACORDO_OUTROS:
			case PendenciaTipoDt.MARCAR_AUDIENCIA:
			case PendenciaTipoDt.AVERBACAO_CUSTAS:
			case PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC:
			case PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT:
			case PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC:	
			case PendenciaTipoDt.ARQUIVAMENTO_PROVISORIO:
			case PendenciaTipoDt.RELATORIO:
			case PendenciaTipoDt.ENVIAR_PROCESSO_PRESIDENTE_UNIDADE:
			case PendenciaTipoDt.RETORNAR_AUTOS_RELATOR_PROCESSO:
			case PendenciaTipoDt.ATIVAR_PROCESSO_CALCULO:
				//Nesses casos n�o deve mostrar as op��es de prazo e urg�ncia
				listaDestinatarios.add("semOpcoes");
				break;

			case PendenciaTipoDt.CARTA_PRECATORIA:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinat�rios poss�veis
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				break;
				
			case PendenciaTipoDt.ALVARA:
			case PendenciaTipoDt.ALVARA_SOLTURA:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinat�rios poss�veis
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				break;
				
			case PendenciaTipoDt.EDITAL:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinat�rios poss�veis
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				break;
			case PendenciaTipoDt.OFICIO:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinat�rios poss�veis
				listaDestinatarios.add(new dwrCombo("Outros", "999"));
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				break;
			case PendenciaTipoDt.ALTERAR_VALOR_CAUSA:
				listaDestinatarios.add("opcoesAlterarValorCausa");
				break;
			case PendenciaTipoDt.ALTERAR_CLASSE_PROCESSUAL:
				listaDestinatarios.add("opcoesAlterarClasseProcessual");
				break;
				
			case PendenciaTipoDt.ENCAMINHAR_PROCESSO:
				listaDestinatarios.add("opcoesEncaminharProcesso");
				break;
			case PendenciaTipoDt.ENCAMINHAR_PROCESSO_GABINETE:
				listaDestinatarios.add("opcoesEncaminharProcessoGabinete");
				break;
			case PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO:
			case PendenciaTipoDt.SUSPENSAO_PROCESSO:
			case PendenciaTipoDt.AGUARDANDO_PRAZO_DECADENCIAL:
				listaDestinatarios.add("opcoesAguardandoDecursoPrazo");
				break;	
			case PendenciaTipoDt.MARCAR_AUDIENCIA_AUTOMATICA:
				listaDestinatarios.add("opcoesAudiencia");
				break;
			default:
				//Se � um tipo de conclus�o n�o deve mostrar as op��es de prazo e urg�ncia
				if (Conclusao(tipoPendencia)) listaDestinatarios.add("semOpcoes");
				else listaDestinatarios.add(new String("opcoes"));
				break;
		}

		return listaDestinatarios;
	}

	/**
	 * Monta os destinat�rios comuns em pend�ncias para partes de processo.
	 * Quando uma pend�ncia puder ser direcionada para as partes do processo, ser�o disponibilizadas as op��es:
	 * 
	 * 		- TODOS PROMOVENTES
	 * 		- TODOS PROMOVIDOS
	 * 		- CADA PARTE INDIVIDUALMENTE
	 * 
	 * @param processoDt
	 * @param listaDestinatarios
	 */
	private void getListaDestinatariosProcesso(ProcessoDt processoDt, List listaDestinatarios) {
		List partes = null;

		listaDestinatarios.add(new dwrCombo("Todos Promoventes", "1"));
		listaDestinatarios.add(new dwrCombo("Todos Promovidos", "2"));
		if (processoDt != null) {
			//No caso de pend�ncias para Recursos Inominados, deve mostrar as partes Recorrentes e Recorridas
			if (processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) {
				if (processoDt.getRecursoDt() != null) {
					partes = processoDt.getRecursoDt().getPartesRecorrentesRecorridas();
					for (int i = 0; i < partes.size(); i++) {
						RecursoParteDt recursoParteDt = (RecursoParteDt) partes.get(i);
						ProcessoParteDt parteDt = recursoParteDt.getProcessoParteDt();
						listaDestinatarios.add(new dwrCombo(parteDt.getNome(), "3-" + parteDt.getId()));
					}
					//Incluindo as outras partes do PROCESSO, j� que elas n�o s�o cadastradas no RECURSO, mas podem ser intimadas em determinadas situa��es
					partes = processoDt.getListaOutrasPartes();
					for (int i = 0; partes != null && i < partes.size(); i++) {
						ProcessoParteDt parteDt = (ProcessoParteDt) partes.get(i);
						listaDestinatarios.add(new dwrCombo(parteDt.getNome() + " - " + parteDt.getProcessoParteTipo(), "3-" + parteDt.getId()));
					}
				}
			} else {
				//No caso de ser Processo de 1� ou 2� grau mostra as partes Promoventes e Promovidas
				partes = processoDt.getPartesProcesso();
				for (int i = 0; i < partes.size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt) partes.get(i);
					listaDestinatarios.add(new dwrCombo(parteDt.getNome() + " - " + parteDt.getProcessoParteTipo(), "3-" + parteDt.getId()));
				}
			}
		}

	}

	/**
	 * Monta os destinat�rios para "Revelia".
	 * 		- TODOS PROMOVIDOS
	 * 		- CADA PARTE INDIVIDUALMENTE
	 * 
	 * @param processoDt
	 * @param listaDestinatarios
	 */
	private void getListaDestinatariosRevelia(ProcessoDt processoDt, List listaDestinatarios) {
		List partes = null;

		listaDestinatarios.add(new dwrCombo("Todos Promovidos", "1"));
		if (processoDt != null) {
			partes = processoDt.getListaPolosPassivos();
			for (int i = 0; i < partes.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) partes.get(i);
				listaDestinatarios.add(new dwrCombo(parteDt.getNome(), "2-" + parteDt.getId()));
			}
		}
	}
	
	/**
	 * Monta os destinat�rios para "Contum�cia".
	 * 		- TODOS PROMOVENTES
	 * 		- CADA PARTE INDIVIDUALMENTE
	 * 
	 * @param processoDt
	 * @param listaDestinatarios
	 */
	private void getListaDestinatariosContumacia(ProcessoDt processoDt, List listaDestinatarios) {
		List partes = null;

		listaDestinatarios.add(new dwrCombo("Todos Promoventes", "1"));
		if (processoDt != null) {
			partes = processoDt.getListaPolosAtivos();
			for (int i = 0; i < partes.size(); i++) {
				ProcessoParteDt parteDt = (ProcessoParteDt) partes.get(i);
				listaDestinatarios.add(new dwrCombo(parteDt.getNome(), "2-" + parteDt.getId()));
			}
		}
	}
	
	/**
	 * Monta os destinat�rios para "Pedido de Vista".
	 * Ser�o: Presidente, Relator, Revisor e Vogal
	 * @param listaDestinatarios
	 * @throws Exception
	 */
	private void getListaDestinatariosMagistradosSegundoGrau(ProcessoDt processoDt, List listaDestinatarios, String serventiaSubTipoCodigo, String id_ServentiaLogada) throws Exception{
		ProcessoResponsavelNe responsavelNe = new ProcessoResponsavelNe();
		List listaResponsaveis = null;
		ServentiaCargoDt serventiaCargoRelator = null;
		
		if (ServentiaSubtipoDt.isSegundoGrau(serventiaSubTipoCodigo)){
			ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
			listaResponsaveis = serventiaCargoNe.consultarServentiaCargosDesembargadores("", id_ServentiaLogada);
			if (processoDt != null) {
				if (ServentiaSubtipoDt.isUPJTurmaRecursal(serventiaSubTipoCodigo)) {
					serventiaCargoRelator = responsavelNe.consultarProcessoResponsavel2Grau(processoDt.getId(), CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU, true);
				} else {
					serventiaCargoRelator = responsavelNe.consultarRelator2GrauConsideraSubstituicao(processoDt.getId(), processoDt.getId_Serventia(), null);  
				}
			}
		} else if (ServentiaSubtipoDt.isTurma(serventiaSubTipoCodigo)){
			ProcessoNe processoNe = new ProcessoNe();
			if (processoDt != null && processoDt.getId_Serventia() != null) {
				listaResponsaveis = processoNe.consultarJuizesTurma(processoDt.getId_Serventia());
				serventiaCargoRelator = responsavelNe.getRelatorResponsavelProcesso(processoDt.getId(), processoDt.getId_Serventia());
			} else {
				listaResponsaveis = processoNe.consultarJuizesTurma(id_ServentiaLogada);
			}
		}
		
		if (listaResponsaveis != null && listaDestinatarios != null) {
			for (Iterator iterator = listaResponsaveis.iterator(); iterator.hasNext();) {
				ServentiaCargoDt serventiaCargo = (ServentiaCargoDt) iterator.next();
				
				boolean selected = (serventiaCargoRelator != null && serventiaCargoRelator.getId() != null && serventiaCargoRelator.getId().equalsIgnoreCase(serventiaCargo.getId()));
				
				listaDestinatarios.add(new dwrCombo(serventiaCargo.getCargoTipo() + " - " + serventiaCargo.getNomeUsuario() + " - " + serventiaCargo.getServentiaCargo(), 
						                            "5-" + serventiaCargo.getId(),
						                            (selected ? "true" : "false")));
			}	
		}
	}
	
	private void getListaDestinatariosEnvioInstanciaSuperior(ProcessoDt processoDt, String id_ServentiaLogada, List listaDestinatarios) throws Exception{
		
		ServentiaDt serventiaDt;
		if (processoDt != null && processoDt.getId_Serventia() != null && processoDt.getId_Serventia().length()>0){
			serventiaDt = (ServentiaDt) new ServentiaNe().consultarId(processoDt.getId_Serventia());
		} else {
			serventiaDt = (ServentiaDt) new ServentiaNe().consultarId(id_ServentiaLogada);
		}
			
		if (serventiaDt != null) {
			if(serventiaDt.getAreaDistribuicao() != null && !serventiaDt.getAreaDistribuicao().equalsIgnoreCase("")){
				listaDestinatarios.add(new dwrCombo(serventiaDt.getAreaDistribuicao(), serventiaDt.getId_AreaDistribuicao()));
			}
			if(serventiaDt.getAreaDistribuicaoSecundaria() != null && !serventiaDt.getAreaDistribuicaoSecundaria().equalsIgnoreCase("")){
				listaDestinatarios.add(new dwrCombo(serventiaDt.getAreaDistribuicaoSecundaria(), serventiaDt.getId_AreaDistribuicaoSecundaria()));
			}
		}
		
	}

	/**
	 * Chama m�todo para retornar tipos de pend�ncia definidos para um grupo com limite na consulta
	 */
	public List consultarGrupoPendenciaTipo(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		return this.consultarGrupoPendenciaTipo(descricao, usuarioDt, posicao, true);
	}

	/**
	 * Consulta Tipos de Pend�ncia filtrando por grupo de usu�rio.
	 * Se usu�rio � Administrador, lista todos os tipos.
	 * Se n�o � Administrador, chama GrupoPendenciaTipoNe e filtra os tipos de pend�ncia de acordo com o grupo do usu�rio
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public List consultarGrupoPendenciaTipo(String descricao, UsuarioDt usuarioDt, String posicao, boolean limite) throws Exception{
		List tempList = null;
		
		switch (Funcoes.StringToInt(usuarioDt.getGrupoCodigo())) {
			case GrupoDt.ESTATISTICA:
			case GrupoDt.JUIZ_CORREGEDOR:
			case GrupoDt.ADMINISTRADORES:
				tempList = consultarDescricao(descricao, posicao);
				break;
			default:
				GrupoPendenciaTipoNe grupoPendenciaTipoNe = new GrupoPendenciaTipoNe();
				tempList = grupoPendenciaTipoNe.consultarGrupoPendenciaTipo(descricao, usuarioDt, posicao, limite);
				QuantidadePaginas = grupoPendenciaTipoNe.getQuantidadePaginas();
				break;
		}
		
		
		return tempList;
	}

	/**
	 * Consulta todos os tipos de pend�ncias existentes
	 * @return Lista de PendenciaTipoDt
	 */
	public List consultarTiposPendenciaMovimentacao() throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaTipoPs obPersistencia = new PendenciaTipoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarTiposPendenciaMovimentacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * M�todo que checa se o tipo de pend�ncia passado � de um tipo de conclus�o
	 * @param pendenciaTipoCodigo C�digo do tipo de pend�ncia
	 * @return True se o c�digo � referente a uma conclus�o, false caso contr�rio
	 * 
	 * @author msapaula
	 */
	public boolean Conclusao(String pendenciaTipoCodigo) {
		int codigo = Funcoes.StringToInt(pendenciaTipoCodigo);
		Iterator iterador = PendenciaTipoDt.Conclusoes.iterator();
		while (iterador.hasNext()) {
			int intTemp = ((Integer) iterador.next()).intValue();
			if (intTemp == codigo) return true;
		}
		return false;
	}

	/**
	 * Esse m�todo verifica para um determinado tipo de pend�ncia passado
	 * se existem algumas pend�ncias relacionadas que devem ser geradas obrigatoriamente.
	 * Ex. quando uma pend�ncia � do tipo "Marcar Sessao" sempre deve gerar intima��o para todas as partes no processo
	 * 
	 * @param codPendenciaTipo tipo da pend�ncia
	 * @author msapaula
	 * 
	 * @return lista com um vetor de String na seguinte estrutura
	 * 			[0] - pendenciaTipoCodigo 	[1] - descri��o da pend�ncia tipo
	 * 			[2] - codDestinatario		[3] - descri��o do destinat�rio	
	 * @throws Exception 
	 */
	public List getPendenciasRelacionadas(String codPendenciaTipo, String id_Serventia) throws Exception {
		List liTemp = null;
		//Se trata de uma pend�ncia do tipo Marcar Sess�o j� adiciona tamb�m intima��o para todas as partes
		if (Funcoes.StringToInt(codPendenciaTipo) == PendenciaTipoDt.MARCAR_SESSAO || 
			Funcoes.StringToInt(codPendenciaTipo) == PendenciaTipoDt.REMARCAR_SESSAO) {
			
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(id_Serventia);			
			if (serventiaDt != null && serventiaDt.isTurma()) {
				liTemp = new ArrayList();
				liTemp.add(new String[] {String.valueOf(PendenciaTipoDt.INTIMACAO), "Intima��o", "1", "Todos Promoventes" });
				liTemp.add(new String[] {String.valueOf(PendenciaTipoDt.INTIMACAO), "Intima��o", "2", "Todos Promovidos" });	
			}			
		}
		return liTemp;
	}
	
	/**
	 * M�todo que gera o relat�rio PDF ou TXT resultante da listagem de Pend�ncia Tipo.
	 * 
	 * @param diretorioProjeto - diret�rio do projeto onde est� o arquivo jasper compilado
	 * @param nomeBusca - nome de busca de processo tipo
	 * @param tipoArquivo - tipo de arquivo do relat�rio selecionado (txt ou pdf)
	 * @param usuarioResponsavelRelatorio - usu�rio que est� solicitando o relat�rio
	 * @return relat�rio
	 * @throws Exception
	 * @author hmgodinho
	 */
	public byte[] relPendenciaTipo(String diretorioProjeto, String nomeBusca, Integer tipoArquivo, String usuarioResponsavelRelatorio) throws Exception {
		byte[] temp = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaTipoPs obPersistencia = new PendenciaTipoPs(obFabricaConexao.getConexao());

			List listaPendenciaTipo = obPersistencia.consultarPendenciaTipoDescricao(nomeBusca);
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "listagemPendenciaTipo";

			// Tipo Arquivo == 1 � PDF , TipoArquivo == 2 � TXT
			if (tipoArquivo != null && tipoArquivo.equals(1)) {

				// PAR�METROS DO RELAT�RIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("dataAtual", new Date());
				parametros.put("titulo", "Listagem de Tipos de Pend�ncia");
				parametros.put("parametroConsulta", nomeBusca.toUpperCase());
				parametros.put("usuarioResponsavelRelatorio", usuarioResponsavelRelatorio);

					temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaPendenciaTipo);

			} else {
				String conteudoArquivo = "";
				Relatorios.getSeparadorRelatorioTxt();
				for (int i = 0; i < listaPendenciaTipo.size(); i++) {
					PendenciaTipoDt obTemp = (PendenciaTipoDt) listaPendenciaTipo.get(i);
					conteudoArquivo += obTemp.getPendenciaTipo() + "\n";
				}
				temp = conteudoArquivo.getBytes();
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		return consultarDescricaoJSON (descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PendenciaTipoPs obPersistencia = new PendenciaTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	//ArquivoTipo
	public String consultarDescricaoEstadoJSON(String descricao, String posicao) throws Exception {
        String stTemp ="";
        stTemp = (new ArquivoTipoNe()).consultarDescricaoJSON(descricao,  posicao);                       
        return stTemp;
    }
	
	public String consultarArquivoTipoDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoTipoPs obPersistencia = new ArquivoTipoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}

	public PendenciaTipoDt consultarPendenciaTipoCodigo(int pendenciaTipoCodigo, FabricaConexao conexao) throws Exception {
		PendenciaTipoPs obPersistencia = new PendenciaTipoPs(conexao.getConexao());
		return obPersistencia.consultarPendenciaTipoCodigo(pendenciaTipoCodigo);
	}
}
