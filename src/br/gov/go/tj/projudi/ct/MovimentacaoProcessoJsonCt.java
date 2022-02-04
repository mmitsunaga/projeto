package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.AudienciaDt;
import br.gov.go.tj.projudi.dt.AudienciaMovimentacaoDt;
import br.gov.go.tj.projudi.dt.AudienciaSegundoGrauDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.dwrCombo;
import br.gov.go.tj.projudi.dt.dwrMovimentarProcesso;
import br.gov.go.tj.projudi.ne.GuiaEmissaoNe;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaTipoRelacionadaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet responsável em controlar as chamadas Ajax do arquivo MovimentacaoProcesso.js
 * Substituindo o MovimentacaoProcesso.dwr e o controlador MovimentacaoCtDwr
 * @author mmitsunaga
 *
 */
public class MovimentacaoProcessoJsonCt extends Controle {

	private static final long serialVersionUID = -5728123985798997701L;

	private final int INSERIR_PENDENCIA = 10000;
	private final int ATUALIZAR_PENDENCIAS = 10001;
	private final int EXCLUIR_PENDENCIA = 10002;
	private final int GET_LISTA_DESTINATARIOS = 10004;
	private final int GET_LISTA_SESSOES_ABERTAS = 10005;
	private final int GET_LISTA_CLASSES_PROCESSO_RECURSO = 10006;
	private final int GET_LISTA_SESSOES_VIRTUAIS_ABERTAS = 10007;
	
	@Override
	public int Permissao() {		
		return MovimentacaoDt.CodigoPermissao;
	}

	@Override
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {
		
		switch (paginaatual) {
			case Configuracao.Curinga8:
				int operacao = Funcoes.StringToInt(request.getParameter("operacao"), 0);				
				switch (operacao){
					case GET_LISTA_DESTINATARIOS:
						String pendenciaTipoCodigo = request.getParameter("codPendenciaTipo");
						getListaDestinatariosJSON(request, response, pendenciaTipoCodigo);
						break;
						
					case GET_LISTA_SESSOES_ABERTAS:
						getListaSessoesAbertasJSON(request, response);
						break;
					
					case GET_LISTA_SESSOES_VIRTUAIS_ABERTAS:
						getListaSessoesVirtuaisAbertasJSON(request, response);
						break;
						
					case GET_LISTA_CLASSES_PROCESSO_RECURSO:
						getListaClassesProcessoRecursoJSON(request, response);
						break;
						
					case ATUALIZAR_PENDENCIAS:
						getTodosDados(request, response);
						break;
						
					case INSERIR_PENDENCIA:						
						dwrMovimentarProcesso movimentarProcesso = new dwrMovimentarProcesso();
						movimentarProcesso.setId(Integer.valueOf(request.getParameter("id")));
						movimentarProcesso.setCodPendenciaTipo(request.getParameter("codPendenciaTipo"));
						movimentarProcesso.setPendenciaTipo(request.getParameter("pendenciaTipo"));
						movimentarProcesso.setCodDestinatario(request.getParameter("codDestinatario"));
						movimentarProcesso.setDestinatario(request.getParameter("destinatario"));
						movimentarProcesso.setDestinatarioTipo(request.getParameter("destinatarioTipo"));						
						movimentarProcesso.setPrazo(request.getParameter("prazo"));
						movimentarProcesso.setDataLimite(request.getParameter("dataLimite"));
						movimentarProcesso.setUrgencia(request.getParameter("urgencia"));						
						movimentarProcesso.setOutros(request.getParameter("outros"));
						movimentarProcesso.setIdProcessoTipo(request.getParameter("idProcessoTipo"));
						movimentarProcesso.setProcessoTipo(request.getParameter("processoTipo"));
						movimentarProcesso.setOnLine(request.getParameter("onLine"));
						movimentarProcesso.setPessoalAdvogado(request.getParameter("pessoalAdvogado"));
						movimentarProcesso.setIntimacaoAudiencia(request.getParameter("intimacaoAudiencia"));						
						movimentarProcesso.setId_Sessao(request.getParameter("id_Sessao"));
						movimentarProcesso.setDataSessao(request.getParameter("dataSessao"));
						movimentarProcesso.setPessoal(request.getParameter("pessoal"));
						movimentarProcesso.setId_Classe(request.getParameter("id_Classe"));
						movimentarProcesso.setClasse(request.getParameter("classe"));
						movimentarProcesso.setIdServentiaDestino(request.getParameter("idServentiaDestino"));
						movimentarProcesso.setIdServentiaCargo(request.getParameter("idServentiaCargo"));
						movimentarProcesso.setIdAreaDistribuicaoDestino(request.getParameter("idAreaDistribuicaoDestino"));
						movimentarProcesso.setCodTipoAudiencia(request.getParameter("codTipoAudiencia"));
						movimentarProcesso.setCodTipoProcessoFase(request.getParameter("codTipoProcessoFase"));
						movimentarProcesso.setId_ProcArquivamentoTipo(request.getParameter("id_ProcArquivamentoTipo"));
						movimentarProcesso.setProcArquivamentoTipo(request.getParameter("procArquivamentoTipo"));
						movimentarProcesso.setExpedicaoAutomatica(Funcoes.StringToBoolean(request.getParameter("expedicaoAutomatica")));
						movimentarProcesso.setCodExpedicaoAutomatica(request.getParameter("codExpedicaoAutomatica"));
						movimentarProcesso.setMaoPropria(Funcoes.StringToBoolean(request.getParameter("maoPropria")));
						movimentarProcesso.setOrdemServico(Funcoes.StringToBoolean(request.getParameter("ordemServico")));						
						adicionar(request, movimentarProcesso);
						break;
						
					case EXCLUIR_PENDENCIA:
						int id = Integer.valueOf(request.getParameter("id"));
						excluir(request, id);
						break;
				}
		}
							
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private ProcessoDt obtenhaProcessoDtSessao(HttpServletRequest request) throws Exception{
		ProcessoDt processoDt = null;		
		MovimentacaoProcessoDt movimentacaoDt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
		if (movimentacaoDt != null) {
			if (movimentacaoDt.getListaProcessos() != null && movimentacaoDt.getListaProcessos().size() == 1) processoDt = movimentacaoDt.getPrimeiroProcessoLista();
		} else {
			AnaliseConclusaoDt analisePendenciaDt = (AnaliseConclusaoDt) request.getSession().getAttribute("AnalisePendenciadt");
			if (analisePendenciaDt != null) {
				if (analisePendenciaDt.getListaPendenciasFechar() != null && analisePendenciaDt.getListaPendenciasFechar().size() == 1) processoDt =  analisePendenciaDt.getPrimeiroProcessoListaFechar();
			} else {
				AudienciaMovimentacaoDt audienciaMovimentacaoDt = (AudienciaMovimentacaoDt) request.getSession().getAttribute("AudienciaMovimentacaoDt");
				if (audienciaMovimentacaoDt.getAudienciaDt() != null && audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt() != null) processoDt = (ProcessoDt) audienciaMovimentacaoDt.getAudienciaDt().getAudienciaProcessoDt().getProcessoDt();
			}
		}
		
		return processoDt;
	}
	
	/**
	 * Método responsável em devolver uma lista de destinatários possíveis de acordo com a pendência selecionada para ser gerada.
	 * @param request
	 * @param response
	 * @param tipoPendencia
	 * @throws Exception
	 */
	private void getListaDestinatariosJSON (HttpServletRequest request, HttpServletResponse response, String tipoPendencia) throws Exception {
		
		List tempLista = null;
		MovimentacaoNe obNegocio = null;
		
		//Alteração para colocar o código da pendencia na sessão para quando se escolhe o tipo de pendencia para "alterar classe processual"
		request.getSession().setAttribute("PendenciaTipoCodigo", tipoPendencia);
		
		obNegocio = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (obNegocio == null) obNegocio = new MovimentacaoNe();
		
		/**
		 * Para preencher os destinatários de Intimação precisa dos dados das partes do processo, por isso tenta resgatar o processo que está sendo
		 * movimentado, audiência movimentada ou processo analisado e devolve o primeiro processo.
		 * Nos casos em que for Movimentação Múltipla ou Análise Múltipla não será possível destinar Intimação a uma parte específica, somente para
		 * casos gerais como "Todos Promoventes", "Todos Promovidos", etc.
		 */
		ProcessoDt processoDt = obtenhaProcessoDtSessao(request);
		
		UsuarioNe UsuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");

		tempLista = obNegocio.montarListaDestinatarios(tipoPendencia, processoDt, UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo(), UsuarioSessao.getUsuarioDt().getId_Serventia());
		
		// Cria um json com o seguinte formato: 
		// {"tipo": "opcoes", "destinatarios": [{"id": 1, "texto": "Ambas as partes"}]}		
		JSONObject jsonRetorno = new JSONObject();
		JSONArray aJson = new JSONArray();		
		for (int i=0;i<tempLista.size();i++){
			if (tempLista.get(i) instanceof String){
				jsonRetorno.put("tipo", tempLista.get(i));
				jsonRetorno.put("destinatarios", aJson);
			} else {
				dwrCombo item = (dwrCombo) tempLista.get(i);
				JSONObject oJson = new JSONObject();
				oJson.put("id", item.getValue());
				oJson.put("texto", item.getOption());
				oJson.put("selected", item.getSelected());
				aJson.put(oJson);
			}
		}
				
		try {			
            response.setContentType("text/x-json");
            response.getOutputStream().write(jsonRetorno.toString().getBytes());
            response.flushBuffer();
        } catch(Exception e ){
            throw new Exception("Erro!");
        }
		
	}
	
	/**
	 * Método responsável em devolver uma lista de sessões abertas para a opção de Marcar/Remarcar Sessão
	 * durante a movimentação de processos
	 * @throws Exception
	 */
	private void getListaSessoesAbertasJSON (HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MovimentacaoNe obNegocio = null;

		obNegocio = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (obNegocio == null) obNegocio = new MovimentacaoNe();

		UsuarioNe usuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");

		//Consulta sessões abertas na serventia do usuário logado
		List tempLista = obNegocio.consultarSessoesAbertas(usuarioSessao.getUsuarioDt().getId_Serventia(), usuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), true);
		
		JSONArray jsonRetorno = new JSONArray();	
					
		if (tempLista != null) {
			for (int i = 0; i < tempLista.size(); i++) {
				AudienciaDt audienciaDt = (AudienciaDt) tempLista.get(i);
				//listaSessoes.add(new dwrCombo(audienciaDt.getDataAgendada() + (audienciaDt.isVirtual() ? " [Virtual]" : " [Presencial]"), audienciaDt.getId()));
				if (!audienciaDt.isVirtual() &&
					!audienciaDt.isSessaoIniciada()) {
					JSONObject json = new JSONObject();
					json.put("id", audienciaDt.getId());
					json.put("texto", audienciaDt.getDataAgendada() + " [Presencial]");
					jsonRetorno.put(json);					
				}
			}
		}
		
		try {			
            response.setContentType("text/x-json");
            response.getOutputStream().write(jsonRetorno.toString().getBytes());
            response.flushBuffer();
        } catch(Exception e ){
            throw new Exception("Erro!");
        }
		
	}
	
	/**
	 * Método responsável em devolver uma lista de sessões abertas, destacando as virtuais, para a opção de Marcar/Remarcar Sessão
	 * durante a movimentação de processos
	 */
	public void getListaSessoesVirtuaisAbertasJSON(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String marcarSessao = request.getParameter("marcarSessao") == null ? "false" : request.getParameter("marcarSessao");
		
		MovimentacaoNe obNegocio = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (obNegocio == null) obNegocio = new MovimentacaoNe();

		UsuarioNe usuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");

		//Consulta sessões abertas na serventia do usuário logado
		List tempLista = obNegocio.consultarSessoesVirtuaisAbertas(usuarioSessao.getUsuarioDt().getId_Serventia(), usuarioSessao.getUsuarioDt().getGrupoTipoCodigo(), true, Boolean.valueOf(marcarSessao)); 
		
		JSONArray jsonRetorno = new JSONArray();
		
		//Percorre a lista e preenche outra com objetos do tipo "dwrCombo" para mostrar no select
		if (tempLista != null) {
			for (int i = 0; i < tempLista.size(); i++) {
				AudienciaDt audienciaDt = (AudienciaDt) tempLista.get(i);
				JSONObject json = new JSONObject();
				json.put("id", audienciaDt.getId());
				if(audienciaDt instanceof AudienciaSegundoGrauDt && ((AudienciaSegundoGrauDt) audienciaDt).isVirtual()) {
					json.put("texto", audienciaDt.getDataAgendada()+" (Virtual)");
				} else {
					json.put("texto", audienciaDt.getDataAgendada());
				}
				jsonRetorno.put(json);				
			}
		}
		
		try {			
            response.setContentType("text/x-json");
            response.getOutputStream().write(jsonRetorno.toString().getBytes());
            response.flushBuffer();
        } catch(Exception e ){
            throw new Exception("Erro!");
        }

	}
	
	
	/**
	 * Método responsável em devolver uma lista de classes do processo/recurso para a opção de Marcar/Remarcar Sessão
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void getListaClassesProcessoRecursoJSON(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		MovimentacaoNe obNegocio = null;

		obNegocio = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (obNegocio == null) obNegocio = new MovimentacaoNe();

		ProcessoDt processoDt = obtenhaProcessoDtSessao(request);
		
		JSONArray jsonRetorno = new JSONArray();
		
		if (processoDt.getId_Recurso() != null && processoDt.getId_Recurso().trim().length() > 0) {
			List<ProcessoTipoDt> tempLista = obNegocio.consultarProcessoTipoServentiaRecurso(null, processoDt.getId_Recurso(), true);
			for (ProcessoTipoDt processoTipoDt : tempLista) {				
				JSONObject json = new JSONObject();
				json.put("id", processoTipoDt.getId());
				json.put("texto", processoTipoDt.getProcessoTipo());
				jsonRetorno.put(json);
			}				
		} else {			
			JSONObject json = new JSONObject();
			json.put("id", processoDt.getId_ProcessoTipo());
			json.put("texto", processoDt.getProcessoTipo());
			jsonRetorno.put(json);
		}
		
		try {			
            response.setContentType("text/x-json");
            response.getOutputStream().write(jsonRetorno.toString().getBytes());
            response.flushBuffer();
        } catch(Exception e ){
            throw new Exception("Erro!");
        }
	}
	
	/**
	 * Consulta a lista de pendências em sessão e retorna o JSON 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private void getTodosDados (HttpServletRequest request, HttpServletResponse response) throws Exception {
		Set listaPendencias = (Set) getLista(request);
		Iterator iterator = listaPendencias.iterator();
		JSONArray jsonRetorno = new JSONArray();
		while (iterator.hasNext()) {
			dwrMovimentarProcesso dado = (dwrMovimentarProcesso) iterator.next();			
			jsonRetorno.put(new JSONObject(dado.getJSON()));
		}		
		try {			
            response.setContentType("text/x-json");
            response.getOutputStream().write(jsonRetorno.toString().getBytes());
            response.flushBuffer();
        } catch(Exception e ){
            throw new Exception("Erro!");
        }
	}
	
	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	private Set getLista(HttpServletRequest request) throws Exception{
		Set listaDados = null;
		listaDados = (Set) request.getSession().getAttribute("ListaPendencias");
		if (listaDados == null) {
			listaDados = new HashSet();
			request.getSession().setAttribute("ListaPendencias", listaDados);
		}
		return listaDados;
	}
	
	/**
	 * Exclui uma pendência por ID da coleção
	 * @param request
	 * @param id
	 * @throws Exception
	 */
	public void excluir(HttpServletRequest request, int id) throws Exception{
		Set people = getLista(request);			
		people.removeIf( item -> ((dwrMovimentarProcesso)item).getId() == id);				
	}
	
	
	/**
	 * Resgata os dados e adiciona na lista de pendencias a gerar
	 * @param dado
	 * @throws Exception
	 */
	public void adicionar(HttpServletRequest request, dwrMovimentarProcesso dado) throws Exception{
		
		ProcessoDt processoDt = null;		
		String id_seventia = null;

		//Captura lista das pendências
		Set listaPendencias = getLista(request);
		
		switch (Funcoes.StringToInt(dado.getCodPendenciaTipo())) {
			case PendenciaTipoDt.ARQUIVAMENTO:				
				processoDt = obtenhaProcessoDtSessao(request);
				if (processoDt != null && processoDt.isCriminal() && (dado.getId_ProcArquivamentoTipo() == null || dado.getId_ProcArquivamentoTipo().length()==0)){
					throw new Exception("É necessário informar o tipo de Arquivamento."); 
				} 
			break;
			case PendenciaTipoDt.INTIMACAO:				
				processoDt = obtenhaProcessoDtSessao(request);				
				if ((processoDt != null && (processoDt.getId_Custa_Tipo() == null || processoDt.getId_Custa_Tipo().length() == 0) && dado.getExpedicaoAutomatica())) {
					throw new Exception("É necessário que o Tipo de Custa do processo esteja cadastrado."); 
				} else if(processoDt != null) {
					dado.setId_ProcessoCustaTipo(processoDt.getId_Custa_Tipo());
				}				
				if(dado.getExpedicaoAutomatica() && dado.getCodExpedicaoAutomatica().equalsIgnoreCase("-1")){ 
					throw new Exception("É necessário informar o tipo de Modelo para essa pendência."); 
				}
			break;
			case PendenciaTipoDt.CARTA_CITACAO:
				processoDt = obtenhaProcessoDtSessao(request);				
				if ((processoDt != null && (processoDt.getId_Custa_Tipo() == null || processoDt.getId_Custa_Tipo().length() == 0) && dado.getExpedicaoAutomatica())) {
					throw new Exception("É necessário que o Tipo de Custa do processo esteja cadastrado."); 
				} else if(processoDt != null) {
					dado.setId_ProcessoCustaTipo(processoDt.getId_Custa_Tipo());
				}				
				if(dado.getExpedicaoAutomatica() && dado.getCodExpedicaoAutomatica().equalsIgnoreCase("-1")){
					throw new Exception("É necessário informar o tipo de Modelo para essa pendência."); 
				}
			break;
			case PendenciaTipoDt.MARCAR_SESSAO:
			case PendenciaTipoDt.REMARCAR_SESSAO:
			case PendenciaTipoDt.MARCAR_SESSAO_EXTRA_PAUTA:
				//Se foi selecionada uma sessão, sua data será mostrada juntamente com a descrição da pendência
				if (dado.getId_Sessao() != null && dado.getId_Sessao().length() > 0) {
					dado.setPendenciaTipo(dado.getPendenciaTipo() + " " + dado.getDataSessao());
				} else throw new Exception("Informe a Data e Hora da Sessão");
								
				processoDt = obtenhaProcessoDtSessao(request);
				
				if (processoDt != null && Funcoes.StringToLong(processoDt.getId_Recurso()) > 0) {
					if (dado.getId_Classe() != null && dado.getId_Classe().length() > 0) {
						dado.setPendenciaTipo(dado.getPendenciaTipo() + " " + dado.getClasse());
					} else throw new Exception("Informe a Classe");
					id_seventia = processoDt.getId_Serventia();
				} else if (processoDt != null) {
					dado.setId_Classe(processoDt.getId_ProcessoTipo());
					dado.setPendenciaTipo(dado.getPendenciaTipo() + " " + processoDt.getProcessoTipo());
					id_seventia = processoDt.getId_Serventia();
				}
				if (dado.isVirtual()){
					dado.setCodDestinatario("5-"+dado.getId_relator());
					dado.setDestinatario(dado.getRelator());
					if (dado.getRelator() == null || dado.getRelator().equals("")){
						throw new Exception("Informe o Relator do Processo");
					}
					if (dado.getListaVotantes().size() < 2) {
						throw new Exception("Informe os Votantes do Processo");
					}					
				}				
				break;
			case PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO:
			case PendenciaTipoDt.AGUARDANDO_PRAZO_DECADENCIAL:
			case PendenciaTipoDt.SUSPENSAO_PROCESSO:
				//Quando for esse tipo de Suspensão é obrigatório informar o prazo em dias ou data limite
				if ((dado.getPrazo() == null || dado.getPrazo().length() == 0) && (dado.getDataLimite() == null || dado.getDataLimite().length() == 0)) {
					throw new Exception("É necessário informar o  \"Prazo em dias\"  ou a  \"Data Limite\".");
				
				} else if ((dado.getPrazo() != null && dado.getPrazo().length() > 0) && (dado.getDataLimite() != null && dado.getDataLimite().length() > 0)) {
					throw new Exception("Por favor informe apenas o  \"Prazo em dias\"  ou a  \"Data Limite\".");
				} else if(dado.getDataLimite() != null && dado.getDataLimite().length() > 0 && Funcoes.isDataMenorDataAtual(dado.getDataLimite())){
					throw new Exception("Por favor informe uma  \"Data Limite\"  maior que a  Data Atual.");
				}
				
			break;
			case PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO_PRISAO_CIVIL:
			case PendenciaTipoDt.AGUARDANDO_CUMPRIMENTO_PENA:
				//Quando for Suspensão é obrigatório informar o prazo
				if (dado.getPrazo() == null || dado.getPrazo().length() == 0) {throw new Exception("É necessário informar um Prazo."); }
				break;
			case PendenciaTipoDt.PEDIDO_VISTA:
				if (dado.getIdDestinatario() == null || dado.getIdDestinatario().trim().length() == 0) {throw new Exception("É necessário informar um Destinatário."); }
				else {
					processoDt = obtenhaProcessoDtSessao(request);
					if (processoDt != null && processoDt.getId() != null && processoDt.getId().trim().length() > 0){
						MovimentacaoNe movimentacaoNe = new MovimentacaoNe();
						List pendenciasNaoAnalisadas = movimentacaoNe.consultarPendenciasNaoAnalisadasProcesso(processoDt.getId(), dado.getCodPendenciaTipo());
						if (pendenciasNaoAnalisadas != null){
							Iterator itpendenciasNaoAnalisadas = pendenciasNaoAnalisadas.iterator();
							while (itpendenciasNaoAnalisadas.hasNext()) {
								PendenciaDt pendenciaDt = (PendenciaDt) itpendenciasNaoAnalisadas.next();
								if (pendenciaDt != null && pendenciaDt.getId_ServentiaCargo().trim().equalsIgnoreCase(dado.getIdDestinatario().trim())){
									throw new Exception("O destinatário selecionado já possui uma pendência deste tipo.");
								}
							}
						}
					}				
				}
				break;
			case PendenciaTipoDt.ENCAMINHAR_PROCESSO:
				if(dado.getIdAreaDistribuicaoDestino() == null || dado.getIdAreaDistribuicaoDestino().trim().length() == 0 ){
					throw new Exception("É necessário informar a área de distribuição."); 
				}
				if(dado.getIdServentiaDestino() == null || dado.getIdServentiaDestino().trim().length() == 0 ){
					throw new Exception("É necessário informar a serventia de destino."); 
				}
				break;
			case PendenciaTipoDt.ENCAMINHAR_PROCESSO_GABINETE:
				if(dado.getIdServentiaCargo() == null || dado.getIdServentiaCargo().trim().length() == 0 ){
					throw new Exception("É necessário informar o gabinete de destino."); 
				}
				break;
			case PendenciaTipoDt.MARCAR_AUDIENCIA_AUTOMATICA:
				if(dado.getCodTipoAudiencia() == null || dado.getCodTipoAudiencia().equalsIgnoreCase("-1") ){
					throw new Exception("É necessário informar o tipo de Audiência."); 
				}
				break;
			case PendenciaTipoDt.CONCLUSO_DECISAO:
			case PendenciaTipoDt.CONCLUSO_DESPACHO:
			case PendenciaTipoDt.CONCLUSO_SENTENCA:
			case PendenciaTipoDt.CONCLUSO_GENERICO:
			case PendenciaTipoDt.CONCLUSO_GENERICO_SEGUNDO_GRAU:
			case PendenciaTipoDt.CONCLUSO_DESPACHO_PRESIDENTE:
			case PendenciaTipoDt.CONCLUSO_DESPACHO_RELATOR:
			case PendenciaTipoDt.CONCLUSO_PRESIDENTE:
			case PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO:
			case PendenciaTipoDt.CONCLUSO_EMENTA:
			case PendenciaTipoDt.CONCLUSO_RELATOR:
			case PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO:
			case PendenciaTipoDt.CONCLUSO_VOTO:				
				processoDt = obtenhaProcessoDtSessao(request);
				if(processoDt != null) {
					GuiaEmissaoNe guiaEmissaoNe = new GuiaEmissaoNe(); 
					GuiaEmissaoDt guiaEmissaoDt = guiaEmissaoNe.consultarGuiaEmissaoInicialAguardandoDeferimento(processoDt.getId_Processo());
					if (guiaEmissaoDt != null && guiaEmissaoDt.getId() != null && guiaEmissaoDt.getId().length()>0){
						throw new Exception("O processo ("+processoDt.getProcessoNumeroCompleto()+") possui uma guia Aguardando Deferimento ("+Funcoes.FormatarNumeroSerieGuia(guiaEmissaoDt.getNumeroGuiaCompleto())+") - Por favor utilizar a pendência do tipo: Concluso com Pedido de Benefício de Assistência");
					}
				}
				break;
			case PendenciaTipoDt.CONCLUSO_DECISAO_PEDIDO_ASSISTENCIA:				
				processoDt = obtenhaProcessoDtSessao(request);
				if(processoDt != null) {
					GuiaEmissaoNe guiaEmissaoNe2 = new GuiaEmissaoNe(); 
					GuiaEmissaoDt guiaEmissaoDt2 = guiaEmissaoNe2.consultarGuiaEmissaoInicialAguardandoDeferimento(processoDt.getId_Processo());
					if (guiaEmissaoDt2 == null || guiaEmissaoDt2.getId() == null && guiaEmissaoDt2.getId().length() == 0){
						throw new Exception("O processo ("+processoDt.getProcessoNumeroCompleto()+") não possui uma guia Aguardando Deferimento - Por favor utilize outra pendência do tipo conclusão.");
					}
				}
				break;
		}

		//Verifica se existem pendências relacionadas
		List relacionadas = getPendenciasRelacionadas(request, dado.getCodPendenciaTipo(), id_seventia);
		if (relacionadas != null && relacionadas.size() > 0) listaPendencias.addAll(relacionadas);

		PendenciaTipoRelacionadaNe pendenciaTipoRelacionadaNe = new PendenciaTipoRelacionadaNe();
		boolean boPodeAdicionar = true;
		Iterator itPendencias = listaPendencias.iterator();
		
		UsuarioNe UsuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");
		
		while (itPendencias.hasNext()) {
			dwrMovimentarProcesso dt = (dwrMovimentarProcesso) itPendencias.next();			
			if (Funcoes.StringToInt(dado.getCodPendenciaTipo()) == PendenciaTipoDt.PEDIDO_VISTA) {
				if (dt.getIdDestinatario() != null && dado.getIdDestinatario().trim() != null && dt.getIdDestinatario().trim().equalsIgnoreCase(dado.getIdDestinatario().trim())){
					boPodeAdicionar = false;
					break;
				}				
			
			} else if ((Funcoes.StringToInt(dado.getCodPendenciaTipo()) == PendenciaTipoDt.MARCAR_SESSAO || 
					    Funcoes.StringToInt(dado.getCodPendenciaTipo()) == PendenciaTipoDt.REMARCAR_SESSAO || 
					    Funcoes.StringToInt(dado.getCodPendenciaTipo()) == PendenciaTipoDt.MARCAR_SESSAO_EXTRA_PAUTA)
					    && processoDt != null && dt.getId_Classe() != null && dado.getId_Classe().trim() != null && dado.getId_Classe().trim().length() > 0) {
					if (dt.getId_Classe().trim().equalsIgnoreCase(dado.getId_Classe().trim())){
						throw new Exception("Já foi adicionada uma pendência de sessão para a classe" + dado.getClasse() + ".");
					}
			
			} else if (UsuarioSessao.getUsuarioDt().getServentiaCodigo().equalsIgnoreCase("613")) { // Inicio: Alteracao devido aos B.O's 5808 e 5810 ambos de 2017
					
				if ( ((Funcoes.StringToInt(dado.getCodPendenciaTipo()) == PendenciaTipoDt.ARQUIVAMENTO || (Funcoes.StringToInt(dado.getCodPendenciaTipo()) == PendenciaTipoDt.ARQUIVAMENTO_PROVISORIO)) 
						&&  Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.INTIMACAO) || 
					 ((Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.ARQUIVAMENTO || (Funcoes.StringToInt(dt.getCodPendenciaTipo()) == PendenciaTipoDt.ARQUIVAMENTO_PROVISORIO)) 
								&&  Funcoes.StringToInt(dado.getCodPendenciaTipo()) == PendenciaTipoDt.INTIMACAO)
					){
					
					boPodeAdicionar = true;
				
				} else if (!pendenciaTipoRelacionadaNe.verificaRelacionamentoPendencia(dt.getCodPendenciaTipo(), dado.getCodPendenciaTipo())) {
					boPodeAdicionar = false;
					break;
				}
			// Fim: Alteracao devido aos B.O's 5808 e 5810 ambos de 2017	
			} else if (!pendenciaTipoRelacionadaNe.verificaRelacionamentoPendencia(dt.getCodPendenciaTipo(), dado.getCodPendenciaTipo())) {
				boPodeAdicionar = false;
				break;
			}

		}
		
		//Ao adicionar a pendência, retira da seção o seu código para não interferir nas outras telas.
		request.getSession().removeAttribute("PendenciaTipoCodigo");
		
		//Adiciona pendência a lista
		if (boPodeAdicionar) {
			if (dado.getId() == -1) dado.setId(getProximoId(request));
			listaPendencias.remove(dado);
			listaPendencias.add(dado);
		} else {
			throw new Exception("Combinação de Pendências inválida.");
		}
	}
	
	/**
	 * Realiza chamada ao Ne responsável em retornar a lista de pendências relacionadas 
	 * ao tipo de pendência passado.
	 * Deve então percorrer a lista e devolver uma lista de objetos dwrMovimentarProcesso
	 * 
	 * @param codPendenciaTipo tipo da pendência
	 * @param id_Serventia id da serventia do usuário logado
	 * @throws Exception 
	 */
	private List getPendenciasRelacionadas(HttpServletRequest request, String codPendenciaTipo, String id_Serventia) throws Exception{		
		MovimentacaoNe obNegocio = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (obNegocio == null) obNegocio = new MovimentacaoNe();
		List liTemp = new ArrayList();

		List listaPendenciasRelacionadas = obNegocio.getPendenciasRelacionadas(codPendenciaTipo, id_Serventia);

		if (listaPendenciasRelacionadas != null && listaPendenciasRelacionadas.size() > 0) {
			for (int i = 0; i < listaPendenciasRelacionadas.size(); i++) {
				String[] valor = (String[]) listaPendenciasRelacionadas.get(i);
				dwrMovimentarProcesso novaPendencia = new dwrMovimentarProcesso();
				novaPendencia.setCodPendenciaTipo(valor[0]);
				novaPendencia.setPendenciaTipo(valor[1]);
				novaPendencia.setCodDestinatario(valor[2]);
				novaPendencia.setDestinatario(valor[3]);
				novaPendencia.setId(getProximoId(request));
				liTemp.add(novaPendencia);
			}
		}
		return liTemp;
	}
	
	
	/**
	 * Retorna o proximo id de um objeto na lista
	 * @return
	 * @throws Exception
	 */
	private int getProximoId(HttpServletRequest request) throws Exception{
		Integer inId = (Integer) request.getSession().getAttribute("Id_ListaDadosMovimentacao");
		if (inId == null) {
			inId = 0;
		}
		request.getSession().setAttribute("Id_ListaDadosMovimentacao", ++inId);
		return inId;
	}
	
}
