package br.gov.go.tj.projudi.ne;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoSPGDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ps.ProcessoParteAdvogadoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class ProcessoParteAdvogadoNe extends ProcessoParteAdvogadoNeGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6067188577364447357L;

	/**
	 * Valida dados obrigatórios
	 */
	public String Verificar(ProcessoParteAdvogadoDt dados) {

		String stRetorno = "";

		if (dados.getId_UsuarioServentiaAdvogado().equalsIgnoreCase("")) stRetorno += "Nenhum Advogado encontrado com essa OAB.";
		if (dados.getId_ProcessoParte().equalsIgnoreCase("")) stRetorno += "A Parte deve ser escolhida.";

		return stRetorno;

	}

	/**
	 * Valida dados obrigatórios do advogado no momento de uma habilitação em processo
	 * @param dados
	 * @return
	 */
	public String verificarAdvogadoHabilitacao(ProcessoParteAdvogadoDt ProcessoParteAdvogadodt ,String[] partesSelecionadas) {
		String stRetorno = "";
//		if (dados.getOabNumero().length() == 0) stRetorno += "É necessário digitar a OAB do Advogado. \n";
//		if (dados.getOabComplemento().length() == 0) stRetorno += "É necessário digitar o complemento da OAB. \n";
//		if (dados.getEstadoOabUf().length() == 0) stRetorno += "É necessário selecionar o estado da OAB. \n";
//		if (dados.getAdvogadoTipo() == null || dados.getAdvogadoTipo().equals("-1")) stRetorno += "Selecione o Tipo de Advogado. \n";
		if (ProcessoParteAdvogadodt.getId_UsuarioServentiaAdvogado() == null || ProcessoParteAdvogadodt.getId_UsuarioServentiaAdvogado().length() == 0)
			stRetorno += "Selecione o Advogado para habilitação. \n";
		if (partesSelecionadas == null || partesSelecionadas.length == 0) 
			stRetorno += "Selecione a(s) Parte(s) para habilitação.";
		return stRetorno;

	}
	
	public String verificarAdvogadoPromotorHabilitacao(int fluxo, ProcessoParteAdvogadoDt dados, String id_Serventia) {
		String stRetorno = "";
		
		if(fluxo == 1){
			if (dados.getOabNumero().length() == 0) stRetorno += "É necessário digitar a OAB do Advogado. \n";
			if (dados.getOabComplemento().length() == 0) stRetorno += "É necessário digitar o complemento da OAB. \n";
			if (dados.getEstadoOabUf().length() == 0) stRetorno += "É necessário selecionar o estado da OAB. \n";
			if (dados.getAdvogadoTipo() == null || dados.getAdvogadoTipo().equals("-1")) stRetorno += "Selecione o Tipo de Advogado. \n";
		} else if (fluxo == 2){
			if (id_Serventia.length() == 0) stRetorno += "É necessário Selecionar uma serventia. \n";
		}
		return stRetorno;

	}

	/**
	 * Método para salvar ProcessoParteAdvogado, que recebe uma conexão como parâmetro
	 */
	public void salvar(ProcessoParteAdvogadoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		try{
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ProcessoParteAdvogado", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ProcessoParteAdvogado", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}
			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch(Exception e) {
			dados.setId("");
			throw e;
		}
	}

	/**
	 * Salva habilitação de advogado(s) à parte(s) de um processo
	 * @param dados
	 * @throws Exception
	 */
	public void salvarHabilitacaoAdvogado(ProcessoParteAdvogadoDt dados, String[] partesSelecionadas) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());

			if ((partesSelecionadas != null) && (partesSelecionadas.length > 0)) {
				for (int i = 0; i < partesSelecionadas.length; i++) {
					ProcessoParteAdvogadoDt dt = new ProcessoParteAdvogadoDt();
					dt.setId_UsuarioServentiaAdvogado(dados.getId_UsuarioServentiaAdvogado());
					dt.setId_ProcessoParte(partesSelecionadas[i]);
					dt.setPrincipal(dados.getPrincipal());
					dt.setId_Processo(dados.getId_Processo());
					dt.setId_UsuarioLog(dados.getId_UsuarioLog());
					dt.setIpComputadorLog(dados.getIpComputadorLog());
					dt.setRecebeIntimacao(dados.getRecebeIntimacao());
					dt.setDativo(dados.getDativo());

					if (dados.getPrincipal().equalsIgnoreCase("true")) {
						//Se advogado que será habilitado é principal, retira o principal anterior
						this.removeAdvogadoPrincipalProcesso(dt, obFabricaConexao);
					}

					//Insere dados do advogado e gera log
					obPersistencia.inserir(dt);
					obDados.copiar(dados);
					obLogDt = new LogDt("ProcessoParteAdvogado", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dt.getPropriedades());
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}

			obFabricaConexao.finalizarTransacao();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método que verifica se a habilitação de advogado pode ser realizada para o processo passado
	 * 
	 * @param processoDt dt de processo 
	 * @param usuarioDt usuário que vai habilitar advogados
	 * 
	 * @author msapaula
	 */
	public String podeHabilitarAdvogado(ProcessoDt processoDt, UsuarioDt usuarioDt){
		String stMensagem = "";
		//Se usuário for de serventia diferente do processo, não poderá habilitar advogados
		if (!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia())) stMensagem += "Sem permissão para habilitar advogados nesse processo.";

		if (processoDt.isArquivado()){
			stMensagem += " Não é possível executar essa ação, processo está arquivado. \n";
		}else if (processoDt.isErroMigracao()){
			stMensagem += " Não é possível executar essa ação, processo está com ERRO DE MIGRAÇÃO. \n";
		}
		
		if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))) stMensagem += "Sem permissão para habilitar advogados. Motivo: Processo físico.";
		
		return stMensagem;
	}

	/**
	 * Método utilizado no cadastro de processo para salvar os advogados incluídos vinculando esses à todas as partes promoventes
	 * do processo
	 * 
	 * @param listaPartes, lista das parte promoventes do processo
	 * @param listaAdvogados, advogados inseridos no cadastro do processo
	 * @param obFabricaConexao, conexão ativa
	 * @throws Exception
	 */
	public void salvarAdvogadosPartesPromoventes(List listaPartesPromoventes, List listaAdvogados, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {

		if (listaAdvogados != null && listaAdvogados.size() > 0) {
			//Vincula cada advogado cadastrado com cada uma das partes promoventes
			for (int i = 0; i < listaAdvogados.size(); i++) {
				UsuarioServentiaOabDt usuarioServentiaOabDt = (UsuarioServentiaOabDt) listaAdvogados.get(i);

				if (listaPartesPromoventes != null && listaPartesPromoventes.size() > 0) {
					for (int j = 0; j < listaPartesPromoventes.size(); j++) {
						ProcessoParteDt parteDt = (ProcessoParteDt) listaPartesPromoventes.get(j);

						//Criando objeto ProcessoParteAdvogado
						ProcessoParteAdvogadoDt advogadoDt = new ProcessoParteAdvogadoDt();
						advogadoDt.setId_ProcessoParte(parteDt.getId_ProcessoParte());
						advogadoDt.setId_UsuarioLog(logDt.getId_Usuario());
						advogadoDt.setIpComputadorLog(logDt.getIpComputador());
						advogadoDt.setId_UsuarioServentiaAdvogado(usuarioServentiaOabDt.getId_UsuarioServentia());
						this.salvar(advogadoDt, obFabricaConexao);
					}
				}
			}
		}
	}
	
	/**
	 * Verifica se usuário passado é advogado de um processo
	 * @param id_UsuarioServentia, usuário a verificar
	 * @param id_Processo, identificação do processo
	 */
	public boolean isAdvogadoProcesso(String id_UsuarioServentia, String id_Processo) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			boRetorno = obPersistencia.isAdvogadoProcesso(id_UsuarioServentia, id_Processo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}
	
	/**
	 * Verifica se os advogados da parte são procuradores  (Municipal, Estadual, União) ou defensores públicos ou advogados dativos
	 * @param listaAdvogadosParte, lista que será verificada
	 * @param intimacaoAudiencia, Se for intimado em adiência não deve ser públicado no diário eletrônico
	 * @param pendenciaTipocodigo, Se for carta de citação não deve ser públicado no diário eletrônico
	 */
	public boolean isUtilizarDiarioEletronico(List<ProcessoParteAdvogadoDt> listaAdvogadosParte, boolean intimacaoAudiencia, int pendenciaTipocodigo) throws Exception {
		boolean boRetorno = true;
//		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
//		Date dataLimiteDE = df.parse("01/04/2019 00:00:00");  //Resolução Nº 100, de 23 de janeiro de 2019 - publicada em 31/01/2019 - Proad nº 201801000073553
//		Date hoje = Calendar.getInstance().getTime();
		
		if (intimacaoAudiencia){
			boRetorno = false;
		} else if (pendenciaTipocodigo == PendenciaTipoDt.CARTA_CITACAO){
			boRetorno = false;  
//		} else if (hoje.after(dataLimiteDE)) { //Resolução Nº 100, de 23 de janeiro de 2019 - publicada em 31/01/2019 - Proad nº 201801000073553
//			boRetorno = false;
		} else {
			for (Iterator<ProcessoParteAdvogadoDt> iterator = listaAdvogadosParte.iterator(); iterator.hasNext();) {
				ProcessoParteAdvogadoDt processoParteAdvogadoDt = (ProcessoParteAdvogadoDt) iterator.next();
				int grupoCodigo = Funcoes.StringToInt(processoParteAdvogadoDt.getGrupoCodigo());
				 if ( grupoCodigo == GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL|| 
					  grupoCodigo == GrupoDt.ADVOGADO_PUBLICO_ESTADUAL || 
					  grupoCodigo == GrupoDt.ADVOGADO_PUBLICO_UNIAO    ||
					  grupoCodigo == GrupoDt.ADVOGADO_DEFENSOR_PUBLICO ||
					  grupoCodigo == GrupoDt.ADVOGADO_PUBLICO ||
					  grupoCodigo == GrupoDt.MP_TCE || 
					  grupoCodigo == GrupoDt.MINISTERIO_PUBLICO   ){ // GrupoDt.PROMOTORES -> substituto processual		 
					 boRetorno = false;
					 break;
				 } else if (grupoCodigo == GrupoDt.ADVOGADO_PARTICULAR 
						 && processoParteAdvogadoDt.getDativo() != null && processoParteAdvogadoDt.getDativo().equalsIgnoreCase("true")) {
					 boRetorno = false;
					 break;
				 }
			}
		}
			
		return boRetorno;
	}

	/**
	 * Buscar advogados vinculados a um processo 
	 */
	public List consultarAdvogadosProcesso(String id_processo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAdvogadosProcesso(id_processo);
			if (tempList != null) {
                for (int i = 0; i < tempList.size(); i++) {
                    ProcessoParteAdvogadoDt objProcesso = (ProcessoParteAdvogadoDt) tempList.get(i);
                    String[] serventiaHabilitacao;
                    
                    serventiaHabilitacao = obPersistencia.consultarServentiaUFHabilitacaoAdvogados(objProcesso.getId_UsuarioServentiaAdvogado());
                    if (serventiaHabilitacao == null)
                    	serventiaHabilitacao = obPersistencia.consultarServentiaUFHabilitacaoPromotores(objProcesso.getId_UsuarioServentiaAdvogado());
                    
                    objProcesso.setId_ServentiaHabilitacao(serventiaHabilitacao[0]);
                    objProcesso.setServentiaHabilitacao(serventiaHabilitacao[1]);
                }
            }
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public List consultarAdvogadosProcesso(String id_processo, FabricaConexao fabrica) throws Exception {
		List tempList = null;
		ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(fabrica.getConexao());
		tempList = obPersistencia.consultarAdvogadosProcesso(id_processo);
		if (tempList != null) {
            for (int i = 0; i < tempList.size(); i++) {
                ProcessoParteAdvogadoDt objProcesso = (ProcessoParteAdvogadoDt) tempList.get(i);
                String[] serventiaHabilitacao;
                
                serventiaHabilitacao = obPersistencia.consultarServentiaUFHabilitacaoAdvogados(objProcesso.getId_UsuarioServentiaAdvogado());
                if (serventiaHabilitacao == null)
                	serventiaHabilitacao = obPersistencia.consultarServentiaUFHabilitacaoPromotores(objProcesso.getId_UsuarioServentiaAdvogado());
                
                objProcesso.setId_ServentiaHabilitacao(serventiaHabilitacao[0]);
                objProcesso.setServentiaHabilitacao(serventiaHabilitacao[1]);
            }
        }
		
		return tempList;
	}
	
	public HashMap<Long, List<ProcessoParteAdvogadoDt>> consultarAdvogadosProcessoMNI(String id_processo, FabricaConexao fabrica) throws Exception {
		ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(fabrica.getConexao());
		return obPersistencia.consultarAdvogadosProcessoMNI(id_processo);
	}
	
	/**
	 * Consulta os advogados que foram habilitados E desabilitados no processo.
	 * @param id_processo - ID do processo
	 * @return lista de advogados desabilitados
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarAdvogadosDesabilitadosProcesso(String id_processo) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAdvogadosDesabilitadosProcesso(id_processo);
			if (tempList != null) {
                for (int i = 0; i < tempList.size(); i++) {
                    ProcessoParteAdvogadoDt objProcesso = (ProcessoParteAdvogadoDt) tempList.get(i);
                    String[] serventiaHabilitacao;
                    
                    serventiaHabilitacao = obPersistencia.consultarServentiaUFHabilitacaoAdvogados(objProcesso.getId_UsuarioServentiaAdvogado());
                    if (serventiaHabilitacao == null)
                    	serventiaHabilitacao = obPersistencia.consultarServentiaUFHabilitacaoPromotores(objProcesso.getId_UsuarioServentiaAdvogado());
                    
                    objProcesso.setId_ServentiaHabilitacao(serventiaHabilitacao[0]);
                    objProcesso.setServentiaHabilitacao(serventiaHabilitacao[1]);
                }
            }
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Buscar advogados de uma determinada parte
	 */
	public List consultarAdvogadoParteProcesso(String Id_Processo, String Id_ProcessoParte, FabricaConexao obFabricaConexao) throws Exception {
		List tempList=null;
		
		ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
		tempList= obPersistencia.consultarAdvogadoParteProcesso(Id_Processo, Id_ProcessoParte ); 
		
		return tempList;
	}
	
	/**
	 * Consultar ProcessoPatesAdvogados de um determinado processo, partes vinculado a um id_Usuario_serventia
	 */
	public List consultarProcessoParteAdvogado(String Id_Processo, String Id_UsuarioServentia, FabricaConexao obFabricaConexao) throws Exception {
		List tempList=null;
		ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
		tempList= obPersistencia.consultarProcessoParteAdvogado(Id_Processo, Id_UsuarioServentia ); 
				
		return tempList;
	}
	
	/**
	 * Buscar partes de um determinado processo vinculado a um promotor
	 */
	public List consultarProcessoPartePromotorSubstitutoProcessual(String Id_Processo, String Id_UsuarioServentiaPromotor, FabricaConexao obFabricaConexao) throws Exception {
		List tempList=null;
		
		ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
		tempList= obPersistencia.consultarProcessoPartePromotorSubstitutoProcessual(Id_Processo, Id_UsuarioServentiaPromotor ); 
		
		return tempList;
	}
	
	/**
	 * Atualiza  procurador/defensor/advogado Responsável de um processo, alteração em processo parte advogado.
	 * @param String id_Processo, identificador processo
	 * @param String id_UsuarioServentiaNovoProcurador, novo procurador no processo
	 * @param String id_UsuarioServentiaAtualProcurador, procurador que será substituído no processo
	 * @param LogDt logDt, objeto de log
	 * @param FabricaConexao obFabricaConexao, fabrica de conexão
	 * @since 24/03/2011
	 * @throws Exception , exception lançada
	 * 
	 * @author lsBernardes
	 */
	public void atualizarProcessoParteAdvogado(String id_Processo, String id_UsuarioServentiaNovo, String id_UsuarioServentiaAtual, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		if (id_UsuarioServentiaNovo != null && id_UsuarioServentiaNovo.length() > 0) {
			
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			List listaProcessoParteAdvogado = this.consultarProcessoParteAdvogado(id_Processo, id_UsuarioServentiaAtual, obFabricaConexao);
			
			if (listaProcessoParteAdvogado != null && listaProcessoParteAdvogado.size() > 0){
				LogDt obLogDt;
				for (Iterator iterator = listaProcessoParteAdvogado.iterator(); iterator.hasNext();) {
					ProcessoParteAdvogadoDt object = (ProcessoParteAdvogadoDt) iterator.next();
					obPersistencia.atualizarProcessoParteAdvogado(id_Processo, id_UsuarioServentiaNovo, id_UsuarioServentiaAtual, object.getId_ProcessoParte());
					
					String valorAtual = "[id_ProcessoParte:"+object.getId_ProcessoParte()+";id_UsuarioServentiaAtual:" + id_UsuarioServentiaAtual  + "]";
			        String valorNovo = "[id_ProcessoParte:"+object.getId_ProcessoParte()+";id_UsuarioServentiaNovo:"+ id_UsuarioServentiaNovo + "]";
			        obLogDt = new LogDt("ProcessoParteAdvogado", "",  logDt.getId_UsuarioLog(),logDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
					obLog.salvar(obLogDt, obFabricaConexao);
				}
			}
		}
	}
	
	public void atualizarProcessoParteAdvogado(String id_Processo, String id_UsuarioServentiaNovo, String id_UsuarioServentiaAtual, LogDt logDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

            obFabricaConexao.iniciarTransacao();
			
            if (id_UsuarioServentiaNovo != null && id_UsuarioServentiaNovo.length() > 0) {
				ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
				List listaProcessoParteAdvogado = this.consultarProcessoParteAdvogado(id_Processo, id_UsuarioServentiaAtual, obFabricaConexao);
				
				if (listaProcessoParteAdvogado != null && listaProcessoParteAdvogado.size() > 0){
					LogDt obLogDt;
					for (Iterator iterator = listaProcessoParteAdvogado.iterator(); iterator.hasNext();) {
						ProcessoParteAdvogadoDt object = (ProcessoParteAdvogadoDt) iterator.next();
						obPersistencia.atualizarProcessoParteAdvogado(id_Processo, id_UsuarioServentiaNovo, id_UsuarioServentiaAtual, object.getId_ProcessoParte());
						
						String valorAtual = "[id_ProcessoParte:"+object.getId_ProcessoParte()+";id_UsuarioServentiaAtual:" + id_UsuarioServentiaAtual  + "]";
				        String valorNovo = "[id_ProcessoParte:"+object.getId_ProcessoParte()+";id_UsuarioServentiaNovo:"+ id_UsuarioServentiaNovo + "]";
				        obLogDt = new LogDt("ProcessoParteAdvogado", "",  logDt.getId_UsuarioLog(),logDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), valorAtual, valorNovo);
						obLog.salvar(obLogDt, obFabricaConexao);
					}
				}
				
			}
            
			obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
            throw e;
		} finally{
	            // Se a conexao foi criada dentro do metodo, entao finaliza a conexao
			obFabricaConexao.fecharConexao();
	    }
	}
	
	/**
	 * Buscar processos partes advogados Ativos vinculados a um determinado id_usu_serv
	  * 
	 * @param id_Processo, identificação do processo
	 * @param id_Serventia, identificação da serventia do usuário logado
	 * @param id_usu_serv, indentificação do usuario serventia vinculado a parte
	 * @author lsbernardes
	 */
	public List consultarProcessosPartesAdvogados(String id_Processo, String id_Serventia, String idUsuarioServentia) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarProcessosPartesAdvogados(id_Processo, id_Serventia, idUsuarioServentia);
			if (tempList != null) {
                for (int i = 0; i < tempList.size(); i++) {
                    ProcessoParteAdvogadoDt objProcesso = (ProcessoParteAdvogadoDt) tempList.get(i);
                    String[] serventia = obPersistencia.consultarServentiaUFHabilitacaoAdvogados(objProcesso.getId_UsuarioServentiaAdvogado());
                    if(serventia != null) objProcesso.setServentiaHabilitacao(serventia[1]);
                }
            }
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Buscar advogados vinculados a um processo para webservice
	 */
	public List consultarAdvogadosProcessoComHash(String id_processo, UsuarioNe UsuarioSessao) throws Exception {
		List tempList = this.consultarAdvogadosProcesso(id_processo);

		for (int i = 0; i < tempList.size(); i++) {
			ProcessoParteAdvogadoDt ProcessoParteAdvogadodt = (ProcessoParteAdvogadoDt) tempList.get(i);
			ProcessoParteAdvogadodt.setHash(UsuarioSessao.getCodigoHash(ProcessoParteAdvogadodt.getId()));
		}

		return tempList;
	}
	
	/**
	 * Buscar advogados ativos vinculados a um parte no processo.
	 * Usado para disparar intimações para o(s) advogado(s) da parte 
	 */
	public List consultarAdvogadosParte(String id_processoParte) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAdvogadosParte(id_processoParte);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Buscar advogados ativos vinculados a um parte no processo e que estão autorizados a receber intimação.
	 * Usado para disparar intimações para o(s) advogado(s) da parte 
	 */
	public List consultarAdvogadosRecebemIntimacaoParte(String id_processoParte) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = null;
		try{
		    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAdvogadosRecebemIntimacaoParte(id_processoParte);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Buscar se um determinado advogado (id_UsuarioServentia) é advogado de um 
	 * processo (id_processo), e quais os outros advogados da mesma parte
	 */
	public List consultarAdvogadosProcessoParte(String id_Processo, String id_UsuarioServentia) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarAdvogadosProcessoParte(id_Processo, id_UsuarioServentia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Desabilita um determinado advogado de um processo
	 * @param processoParteAdvogadoDt, dados do advogado
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public void desabilitaAdvogado(ProcessoParteAdvogadoDt processoParteAdvogadoDt) throws Exception{
		this.desabilitaAdvogado(processoParteAdvogadoDt, null);
	}

	/**
	 * Desabilita o advogado passado em determinado processo
	 * 
	 * @param processoParteAdvogadoDt, dados do advogado
	 * @param conexao, conexão ativa
	 * 
	 * @author msapaula
	 */
	public void desabilitaAdvogado(ProcessoParteAdvogadoDt processoParteAdvogadoDt, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else obFabricaConexao = conexao;
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());

			obLogDt = new LogDt("ProcessoParteAdvogado", processoParteAdvogadoDt.getId(), processoParteAdvogadoDt.getId_UsuarioLog(), processoParteAdvogadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), processoParteAdvogadoDt.getPropriedades(), "");
			obPersistencia.desabilitaAdvogado(processoParteAdvogadoDt.getId());

			obLog.salvar(obLogDt, obFabricaConexao);
			if (conexao == null) obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			if (conexao == null) obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Atualiza o promotor (Substituto processual) passado em determinado processo
	 * 
	 * @param String, id_ProcessoParteAdvogado
	 * @param String, id_UsuarioServentiaPromotorAtual
	 * @param String, id_UsuarioServentiaPromotorNovo
	 * @param LogDt, logDt
	 * @param FabricaConexao, conexão ativa
	 * 
	 * @author lsbernardes
	 */
	public void atualizarPromotorSubstitutoProcessual(String id_ProcessoParteAdvogado, String id_UsuarioServentiaPromotorAtual, String id_UsuarioServentiaPromotorNovo, LogDt logDt, FabricaConexao conexao) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				obFabricaConexao.iniciarTransacao();
			} else obFabricaConexao = conexao;
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			
			String valorAtual = "[Id_ProcessoParteAdvogado:" + id_ProcessoParteAdvogado + ";ID_USU_SERV:" + id_UsuarioServentiaPromotorAtual + "]";
			String valorNovo = "[Id_ProcessoParteAdvogado:" + id_ProcessoParteAdvogado + ";ID_USU_SERV:" + id_UsuarioServentiaPromotorNovo + "]";

			obLogDt = new LogDt("ProcessoParteAdvogado", id_ProcessoParteAdvogado, logDt.getId_Usuario(), logDt.getIpComputador(), String.valueOf(LogTipoDt.TrocaResponsavelProcesso), valorAtual, valorNovo);

			obPersistencia.atualizarPromotorSubstitutoProcessual(id_ProcessoParteAdvogado, id_UsuarioServentiaPromotorAtual, id_UsuarioServentiaPromotorNovo);
			obLog.salvar(obLogDt, obFabricaConexao);
			
			if (conexao == null) obFabricaConexao.finalizarTransacao();
		} catch(Exception e) {
			if (conexao == null) obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Define o advogado passado como principal em determinado processo
	 */
	public void defineAdvogadoPrincipal(ProcessoParteAdvogadoDt processoParteAdvogadoDt) throws Exception {
		LogDt obLogDt;
		
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParteAdvogado", processoParteAdvogadoDt.getId(), processoParteAdvogadoDt.getId_UsuarioLog(), processoParteAdvogadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), processoParteAdvogadoDt.getPropriedades(), "");
			obPersistencia.removeAdvogadoPrincipalProcesso(processoParteAdvogadoDt.getId_ProcessoParte());
			obPersistencia.defineAdvogadoPrincipal(processoParteAdvogadoDt);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Remove o advogado principal de determinada parte em um processo
	 */
	public void removeAdvogadoPrincipalProcesso(ProcessoParteAdvogadoDt processoParteAdvogadoDt, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
		obLogDt = new LogDt("ProcessoParteAdvogado", processoParteAdvogadoDt.getId(), processoParteAdvogadoDt.getId_UsuarioLog(), processoParteAdvogadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), processoParteAdvogadoDt.getPropriedades(), "");
		obPersistencia.removeAdvogadoPrincipalProcesso(processoParteAdvogadoDt.getId_ProcessoParte());

		obLog.salvar(obLogDt, obFabricaConexao);
		
	}

	public List consultarDescricaoEstado(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		List tempList = null;
		
		EstadoNe Estadone = new EstadoNe();
		tempList = Estadone.consultarDescricao(tempNomeBusca, posicaoPaginaAtual);
		QuantidadePaginas = Estadone.getQuantidadePaginas();
		Estadone = null;
		
		return tempList;
	}
	
	public String consultarDescricaoEstadoJSON(String descricao, String posicao) throws Exception {
        String stTemp ="";               
                                
        stTemp = (new EstadoNe()).consultarDescricaoJSON(descricao,  posicao);                       
        
        return stTemp;
    }
	
	/**
	 * Método que altera o atributo Receber Intimação do advogado
	 * @param processoParteAdvogadoDt - dados do advogado a ser alterado
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void modificarRecebimentoIntimacaoAdvogado(ProcessoParteAdvogadoDt processoParteAdvogadoDt) throws Exception {
		
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try{
		    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParteAdvogado", processoParteAdvogadoDt.getId(), processoParteAdvogadoDt.getId_UsuarioLog(), processoParteAdvogadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), processoParteAdvogadoDt.getPropriedades(), "");
			obPersistencia.modificarRecebimentoIntimacaoAdvogado(processoParteAdvogadoDt);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
	}
	
	/**
	 * Método que altera o atributo Dativo do advogado
	 * @param processoParteAdvogadoDt - dados do advogado a ser alterado
	 * @throws Exception
	 * @author lsbernardes
	 */
	public void modificarAdvogadoDativo(ProcessoParteAdvogadoDt processoParteAdvogadoDt) throws Exception {
		
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try{
		    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ProcessoParteAdvogado", processoParteAdvogadoDt.getId(), processoParteAdvogadoDt.getId_UsuarioLog(), processoParteAdvogadoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), processoParteAdvogadoDt.getPropriedades(), "");
			obPersistencia.modificarAdvogadoDativo(processoParteAdvogadoDt);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
	}
	
	/**
	 * Retorna as serventias com base na descrição, página atual, Tipo da Serventia e SubTipo da Serventia
	 * @param descricao
	 * @param posicao
	 * @param serventiaTipoCodigo
	 * @param serventiaSubTipoCodigo
	 * 
	 *  @author lsbernardes
	 *  @author asrocha 20/11/2014
	 * @throws Exception 
	 */
	public String consultarServentiasPromotoriaJSON(String descricao, String posicao) throws Exception{
		String stTemp = "";
		int[] serventiaTipoCodigo = {ServentiaTipoDt.PROMOTORIA};
		
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarServentiasJuridicasJSON(descricao, posicao, serventiaTipoCodigo);
		
		return stTemp;
	}	

	public String consultarServentiasJuridicasJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		int[] serventiaTipoCodigo = {ServentiaTipoDt.DEFENSORIA_PUBLICA, ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL, ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO, ServentiaTipoDt.PROCURADORIA_UNIAO, ServentiaTipoDt.ESCRITORIO_JURIDICO, ServentiaTipoDt.ADVOGADO_PUBLICO_AUTARQUIAS}; 
		
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarServentiasJuridicasJSON(descricao, posicao, serventiaTipoCodigo);
		return stTemp;
	}
	
	public void inserir(ProcessoParteAdvogadoSPGDt dados, FabricaConexao obFabricaConexao) throws Exception {
		ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
		obPersistencia.inserirSPG(dados);
	}
	
	public void vinculeAdvogadosPartesProcessoImportadosSPG(String id_UsuarioServentia, String oabNumero, String oabUF, String oabComplemento, String nomeAdvogado, String cpfAdvogado, FabricaConexao obFabricaConexao) throws Exception {		
		ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
		
		List<ProcessoParteAdvogadoSPGDt> listaDeAdvogadosSPG = obPersistencia.consulteSPG(oabNumero, oabUF, oabComplemento, nomeAdvogado, cpfAdvogado);		
		
		for (ProcessoParteAdvogadoSPGDt advogadoSPG : listaDeAdvogadosSPG) 
		{			
			//Criando objeto ProcessoParteAdvogado
			ProcessoParteAdvogadoDt advogadoDt = new ProcessoParteAdvogadoDt();
			advogadoDt.setId_ProcessoParte(advogadoSPG.getId_Processo_Parte());
			advogadoDt.setRecebeIntimacao(true);
			advogadoDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
			advogadoDt.setIpComputadorLog("127.0.0.1");
			advogadoDt.setId_UsuarioServentiaAdvogado(id_UsuarioServentia);
			
			this.salvar(advogadoDt, obFabricaConexao);
			
			obPersistencia.excluirSPG(advogadoSPG);
		}
	}
	
	public HashMap<String, HashMap<String, String>>  consultaAdvogadoComSustentacaoOralAbertoPJD(String idAudiProc) throws Exception {
		FabricaConexao obFabricaConexao = null;
		HashMap<String, HashMap<String, String>>  advogadosSustentacaoOralAberta = new HashMap<String, HashMap<String, String>>();
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ProcessoParteAdvogadoPs obPersistencia = new ProcessoParteAdvogadoPs(obFabricaConexao.getConexao());
			advogadosSustentacaoOralAberta = obPersistencia.consultaAdvogadoComSustentacaoOralAbertoPJD(idAudiProc);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}

		return advogadosSustentacaoOralAberta;
	}
	
	/**
	 * Verifica se os advogados da parte são defensores públicos ou advogados dativos ou promotor substituto processual
	 * @param listaAdvogadosParte, lista que será verificada
	 * @return boolean
	 */
	public boolean isAdvogadoDativoDefensorPublico(List<ProcessoParteAdvogadoDt> listaAdvogadosParte) throws Exception {
		boolean boRetorno = false;
		for (Iterator<ProcessoParteAdvogadoDt> iterator = listaAdvogadosParte.iterator(); iterator.hasNext();) {
			ProcessoParteAdvogadoDt processoParteAdvogadoDt = (ProcessoParteAdvogadoDt) iterator.next();
			int grupoCodigo = Funcoes.StringToInt(processoParteAdvogadoDt.getGrupoCodigo());
			 if ( grupoCodigo == GrupoDt.ADVOGADO_DEFENSOR_PUBLICO || grupoCodigo == GrupoDt.MINISTERIO_PUBLICO   ){ // GrupoDt.PROMOTORES -> substituto processual		 
				 boRetorno = true;
				 break;
			 } else if (grupoCodigo == GrupoDt.ADVOGADO_PARTICULAR 
					 && processoParteAdvogadoDt.getDativo() != null && processoParteAdvogadoDt.getDativo().equalsIgnoreCase("true")) {
				 boRetorno = true;
				 break;
			 }
		}
		return boRetorno;
	}
}
