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
			stRetorno += "Descrição é é obrigatório.";
		}
		return stRetorno;

	}

	/**
	 * Consultar objeto PendenciaTipo equivalente ao código passado
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
	 * Método responsável em montar os destinatários possíveis de uma pendência,
	 * no momento em que esta está sendo gerada, seja na movimentação do processo ou na análise de conclusões.
	 * 
	 * Os destinatários serão tratados da seguinte forma:
	 * 		Identificador 1 = todos promoventes 
	 * 		Identificador 2 = todos promovidos
	 * 		Identificador 3 = pendência vinculada a uma parte específica (deve ser passado o Id da parte) 
	 * 		Identificador 4 = pendência para Ministério Publico
	 * 
	 * @param tipoPendencia, tipo de pendência selecionada pelo usuário, de acordo com esse tipo serão retornados os destinatários possíveis
	 * @param processoDt, Processo envolvido
	 * 
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List montarListaDestinatarios(String tipoPendencia, ProcessoDt processoDt, String serventiaSubTipoCodigo, String id_ServentiaLogada) throws Exception{
		List listaDestinatarios = new ArrayList();

		//A primeira posição define se determinadas opções devem ser mostradas ou ocultadas
		//Se valor for opcoesIntimacao = determina que checkbox "Pessoal e Advogados", "Pessoal" e "Intimação em Audiência" aparecerão
		//Se valor for opcoes = determina que opções de prazo e urgência devem ser mostradas
		//Se valor for sessao = determina que opções de marcar sessão devem ser mostradas
		//Se valor for semOpcoes = determina que opções de prazo e urgência não devem ser mostradas
		switch (Funcoes.StringToInt(tipoPendencia)) {

			case PendenciaTipoDt.CARTA_NOTIFICACAO:
			case PendenciaTipoDt.MANDADO:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinatários possíveis
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				break;
			case PendenciaTipoDt.CARTA_CITACAO:
			case PendenciaTipoDt.INTIMACAO:
				listaDestinatarios.add(new String("opcoesIntimacao")); //Opções de intimação
				//Captura destinatários possíveis
				listaDestinatarios.add(new dwrCombo("Ambas as Partes", "6"));
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				listaDestinatarios.add(new dwrCombo("Ministério Público 1º Grau", "4"));				
				listaDestinatarios.add(new dwrCombo("Ministério Público 2º Grau", "8"));				
				listaDestinatarios.add(new dwrCombo("Ministério Público Turma Julgadora", "7"));					
				
				break;
			case PendenciaTipoDt.INTIMACAO_VIA_TELEFONE:
				listaDestinatarios.add(new String("opcoesIntimacaoTelefone")); //Opções de intimação
				//Captura destinatários possíveis
				listaDestinatarios.add(new dwrCombo("Ambas as Partes", "6"));
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				listaDestinatarios.add(new dwrCombo("Ministério Público 1º Grau", "4"));				
				listaDestinatarios.add(new dwrCombo("Ministério Público 2º Grau", "8"));						
				listaDestinatarios.add(new dwrCombo("Ministério Público Turma Julgadora", "7"));								
				
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
				//Nesses casos não deve mostrar as opções de prazo e urgência
				listaDestinatarios.add("semOpcoes");
				break;

			case PendenciaTipoDt.CARTA_PRECATORIA:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinatários possíveis
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				break;
				
			case PendenciaTipoDt.ALVARA:
			case PendenciaTipoDt.ALVARA_SOLTURA:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinatários possíveis
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				break;
				
			case PendenciaTipoDt.EDITAL:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinatários possíveis
				getListaDestinatariosProcesso(processoDt, listaDestinatarios);
				break;
			case PendenciaTipoDt.OFICIO:
				listaDestinatarios.add(new String("opcoes"));
				//Captura destinatários possíveis
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
				//Se é um tipo de conclusão não deve mostrar as opções de prazo e urgência
				if (Conclusao(tipoPendencia)) listaDestinatarios.add("semOpcoes");
				else listaDestinatarios.add(new String("opcoes"));
				break;
		}

		return listaDestinatarios;
	}

	/**
	 * Monta os destinatários comuns em pendências para partes de processo.
	 * Quando uma pendência puder ser direcionada para as partes do processo, serão disponibilizadas as opções:
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
			//No caso de pendências para Recursos Inominados, deve mostrar as partes Recorrentes e Recorridas
			if (processoDt.getId_Recurso() != null && processoDt.getId_Recurso().length() > 0) {
				if (processoDt.getRecursoDt() != null) {
					partes = processoDt.getRecursoDt().getPartesRecorrentesRecorridas();
					for (int i = 0; i < partes.size(); i++) {
						RecursoParteDt recursoParteDt = (RecursoParteDt) partes.get(i);
						ProcessoParteDt parteDt = recursoParteDt.getProcessoParteDt();
						listaDestinatarios.add(new dwrCombo(parteDt.getNome(), "3-" + parteDt.getId()));
					}
					//Incluindo as outras partes do PROCESSO, já que elas não são cadastradas no RECURSO, mas podem ser intimadas em determinadas situações
					partes = processoDt.getListaOutrasPartes();
					for (int i = 0; partes != null && i < partes.size(); i++) {
						ProcessoParteDt parteDt = (ProcessoParteDt) partes.get(i);
						listaDestinatarios.add(new dwrCombo(parteDt.getNome() + " - " + parteDt.getProcessoParteTipo(), "3-" + parteDt.getId()));
					}
				}
			} else {
				//No caso de ser Processo de 1º ou 2º grau mostra as partes Promoventes e Promovidas
				partes = processoDt.getPartesProcesso();
				for (int i = 0; i < partes.size(); i++) {
					ProcessoParteDt parteDt = (ProcessoParteDt) partes.get(i);
					listaDestinatarios.add(new dwrCombo(parteDt.getNome() + " - " + parteDt.getProcessoParteTipo(), "3-" + parteDt.getId()));
				}
			}
		}

	}

	/**
	 * Monta os destinatários para "Revelia".
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
	 * Monta os destinatários para "Contumácia".
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
	 * Monta os destinatários para "Pedido de Vista".
	 * Serão: Presidente, Relator, Revisor e Vogal
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
	 * Chama método para retornar tipos de pendência definidos para um grupo com limite na consulta
	 */
	public List consultarGrupoPendenciaTipo(String descricao, UsuarioDt usuarioDt, String posicao) throws Exception {
		return this.consultarGrupoPendenciaTipo(descricao, usuarioDt, posicao, true);
	}

	/**
	 * Consulta Tipos de Pendência filtrando por grupo de usuário.
	 * Se usuário é Administrador, lista todos os tipos.
	 * Se não é Administrador, chama GrupoPendenciaTipoNe e filtra os tipos de pendência de acordo com o grupo do usuário
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
	 * Consulta todos os tipos de pendências existentes
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
	 * Método que checa se o tipo de pendência passado é de um tipo de conclusão
	 * @param pendenciaTipoCodigo Código do tipo de pendência
	 * @return True se o código é referente a uma conclusão, false caso contrário
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
	 * Esse método verifica para um determinado tipo de pendência passado
	 * se existem algumas pendências relacionadas que devem ser geradas obrigatoriamente.
	 * Ex. quando uma pendência é do tipo "Marcar Sessao" sempre deve gerar intimação para todas as partes no processo
	 * 
	 * @param codPendenciaTipo tipo da pendência
	 * @author msapaula
	 * 
	 * @return lista com um vetor de String na seguinte estrutura
	 * 			[0] - pendenciaTipoCodigo 	[1] - descrição da pendência tipo
	 * 			[2] - codDestinatario		[3] - descrição do destinatário	
	 * @throws Exception 
	 */
	public List getPendenciasRelacionadas(String codPendenciaTipo, String id_Serventia) throws Exception {
		List liTemp = null;
		//Se trata de uma pendência do tipo Marcar Sessão já adiciona também intimação para todas as partes
		if (Funcoes.StringToInt(codPendenciaTipo) == PendenciaTipoDt.MARCAR_SESSAO || 
			Funcoes.StringToInt(codPendenciaTipo) == PendenciaTipoDt.REMARCAR_SESSAO) {
			
			ServentiaDt serventiaDt = new ServentiaNe().consultarId(id_Serventia);			
			if (serventiaDt != null && serventiaDt.isTurma()) {
				liTemp = new ArrayList();
				liTemp.add(new String[] {String.valueOf(PendenciaTipoDt.INTIMACAO), "Intimação", "1", "Todos Promoventes" });
				liTemp.add(new String[] {String.valueOf(PendenciaTipoDt.INTIMACAO), "Intimação", "2", "Todos Promovidos" });	
			}			
		}
		return liTemp;
	}
	
	/**
	 * Método que gera o relatório PDF ou TXT resultante da listagem de Pendência Tipo.
	 * 
	 * @param diretorioProjeto - diretório do projeto onde está o arquivo jasper compilado
	 * @param nomeBusca - nome de busca de processo tipo
	 * @param tipoArquivo - tipo de arquivo do relatório selecionado (txt ou pdf)
	 * @param usuarioResponsavelRelatorio - usuário que está solicitando o relatório
	 * @return relatório
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

			// Tipo Arquivo == 1 é PDF , TipoArquivo == 2 é TXT
			if (tipoArquivo != null && tipoArquivo.equals(1)) {

				// PARÂMETROS DO RELATÓRIO
				Map parametros = new HashMap();
				parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
				parametros.put("dataAtual", new Date());
				parametros.put("titulo", "Listagem de Tipos de Pendência");
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
