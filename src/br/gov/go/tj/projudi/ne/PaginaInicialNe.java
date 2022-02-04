package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.BuscaProcessoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.ListaConclusaoDt;
import br.gov.go.tj.projudi.dt.ListaDadosServentiaDt;
import br.gov.go.tj.projudi.dt.ListaPendenciaDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;
import br.gov.go.tj.projudi.dt.PaginaInicialDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;

/**
 * Objeto de negocios para a pagina inicial
 * 
 * @author Ronneesley Moura Teles
 * @since 02/12/2008 15:42
 */
@SuppressWarnings("all")
public class PaginaInicialNe extends Negocio {
	/**
	 * 
	 */
	private static final long serialVersionUID = -226618942284087133L;

	/**
	 * Consulta os dados para a pagina inicial, de um determinado usuario
	 * 
	 * @author Ronneesley Moura Teles
	 * @param UsuarioDt usuarioDt, vo do usuario
	 * @return PaginaInicialDt
	 * @throws Exception
	 */
	public PaginaInicialDt consultarDados(UsuarioNe usuarioNe) throws Exception{
		PaginaInicialDt paginaInicialDt = new PaginaInicialDt();
		
		// Configura os dados de processos
		configurarDadosProcessos(paginaInicialDt, usuarioNe.getUsuarioDt());

		// Configura os dados das audiencias
		configurarDadosAudiencia(paginaInicialDt, usuarioNe.getUsuarioDt());

		// Configura os dados de pendencias
		configurarDadosPendencias(paginaInicialDt, usuarioNe);
		
		// Configura os dados das sess�es
		configurarDadosSessao(paginaInicialDt, usuarioNe.getUsuarioDt());
		
		// Configura os dados do perfil financeiro
		configurarDadosFinanceiro(paginaInicialDt, usuarioNe);
		
		// Configura os dados da Central de Mandados
		configurarDadosCentralMandados(paginaInicialDt, usuarioNe.getUsuarioDt());

		// Retorna a pagina inicial configurada
		return paginaInicialDt;
	}

	/**
	 * Consulta dados de pend�ncias para montar a p�gina inicial
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @throws Exception 
	 */
	private void configurarDadosPendencias(PaginaInicialDt paginaInicialDt, UsuarioNe usuarioNe) throws Exception{
		PendenciaNe pendenciaNe = new PendenciaNe();

		// Seleciona os tipos de pendencias que estao abertas mesmo que reservadas
		switch (Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo())) {
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
			case GrupoTipoDt.JUIZ_TURMA:
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
			case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
			case GrupoTipoDt.JUIZ_LEIGO:
				this.configurarPendenciasJuizes(usuarioNe.getUsuarioDt(), paginaInicialDt, pendenciaNe);
				break;
			case GrupoTipoDt.ASSISTENTE_GABINETE:
			case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
				if (usuarioNe.isGabinetePresidenciaTjgo() || usuarioNe.isGabineteVicePresidenciaTjgo() || usuarioNe.isGabineteUpj())
					this.configurarPendenciasAssistenteGabinete(usuarioNe.getUsuarioDt(), paginaInicialDt, pendenciaNe);
				else
					this.configurarPendenciasJuizes(usuarioNe.getUsuarioDt(), paginaInicialDt, pendenciaNe);
				break;

			case GrupoTipoDt.ASSESSOR:
				this.configurarPendenciasAssistente(usuarioNe, paginaInicialDt, pendenciaNe);
				break;

			case GrupoTipoDt.ASSESSOR_ADVOGADO:
			case GrupoTipoDt.ASSESSOR_MP:
				this.configurarPendenciasAssistenteAdvogadoPromotor(usuarioNe, paginaInicialDt, pendenciaNe);
				break;

			case GrupoTipoDt.ADVOGADO:
				this.configurarPendenciasAdvogado(usuarioNe, paginaInicialDt, pendenciaNe);
				break;
				
			case GrupoTipoDt.MP:
				this.configurarPendenciasPromotor(usuarioNe, paginaInicialDt, pendenciaNe);
				break;
				
			case GrupoTipoDt.COORDENADOR_PROMOTORIA:
				this.configurarPendenciasCoordenadorPromotoria(usuarioNe, paginaInicialDt, pendenciaNe);
				break;
			//Foi adicionado o grupo de Escrit�rio Jur�dico pois o funcionamento das serventias � id�ntico
			case GrupoTipoDt.COORDENADOR_ESCRITORIO_JURIDICO:
			case GrupoTipoDt.COORDENADOR_PROCURADORIA:
			case GrupoTipoDt.COORDENADOR_DEFENSORIA_PUBLICA:
			case GrupoTipoDt.COORDENADOR_ADVOCACIA_PUBLICA:
				this.configurarPendenciasIntimacoesCoordenadorProcuradoria(usuarioNe, paginaInicialDt, pendenciaNe);
				//se o usu�rio for coordenador do escrit�rio jur�dico, faz consulta de cartas de cita��o
				this.configurarPendenciasCitacaoEscritorioJuridicoProcuradoria(usuarioNe, paginaInicialDt, pendenciaNe);
				break;

			case GrupoTipoDt.DISTRIBUIDOR_GABINETE:
				this.configurarPendenciasDistribuidorGabinete(usuarioNe, paginaInicialDt, pendenciaNe);
				if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
					//Busca pend�ncias para serventia
					this.consultarPendenciasServentia(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);
				}
				break;
			case GrupoTipoDt.DISTRIBUIDOR_CAMARA:
				if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
					//Busca pend�ncias para serventia
					this.configurarPendenciasDistribuidorCamara(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);

					// Busca as pend�ncias para ServentiaCargo
					this.consultarPendenciasServentiaCargo(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);
					
					//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
					paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));

				}
				break;
			case GrupoTipoDt.AUTORIDADE_POLICIAL:
			case GrupoTipoDt.ANALISTA_TI:
			case GrupoTipoDt.ANALISTA_VARA:
			case GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU:
			case GrupoTipoDt.ANALISTA_EXECUCAO:
			case GrupoTipoDt.TECNICO:
			case GrupoTipoDt.TECNICO_VARA:
			case GrupoTipoDt.TECNICO_TURMA_SEGUNDO_GRAU:
			case GrupoTipoDt.CONTADOR:
			case GrupoTipoDt.MALOTE_DIGITAL:
			case GrupoTipoDt.OUVIDOR:
			case GrupoTipoDt.USUARIO_INTERNO_JUDICIARIO:
			case GrupoTipoDt.ESTAGIARIO:
			case GrupoTipoDt.NUPEMEC:
				
				if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
					//Busca pend�ncias para serventia
					this.consultarPendenciasServentia(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);

					// Busca as pend�ncias para ServentiaTipo
					this.consultarPendenciasServentiaTipo(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);

					// Busca as pend�ncias para ServentiaCargo
					this.consultarPendenciasServentiaCargo(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);
					
					// Busca as pend�ncias para Serventia - Mandado de Pris�o
					this.consultarPendenciasServentiaMandadoPrisao(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);

					// Consulta a quantidade de pend�ncias com prazo decorrido
					paginaInicialDt.setQtdePrazoDecorrido(pendenciaNe.consultarQtdPrazosDecorridos(usuarioNe.getUsuarioDt()));
					
					// Consulta a quantidade de pend�ncias com prazo decorrido Devolucao Autos 
					paginaInicialDt.setQtdePrazoDecorridoDevolucaoAutos(pendenciaNe.consultarQtdPrazosDecorridosDevolucaoAutos(usuarioNe.getUsuarioDt()));

					//Consulta a quantidade de pend�ncias expedidas para serventias on-line e que estao aguardando visto
					paginaInicialDt.setQtdExpedidasAguardandoVisto(pendenciaNe.consultarQtdExpedidasAguardandoVisto(usuarioNe.getUsuarioDt()));

					//Consulta a quantidade de pend�ncias  em andamento
					paginaInicialDt.setQtdPendenciasEmAndamento(pendenciaNe.consultarQtdPendenciasAndamento(usuarioNe.getUsuarioDt()));

					List pendencias = pendenciaNe.consultarQuantidadeSessaoVirtualTomarConhecimento(null, usuarioNe.getId_Serventia(), 
							PendenciaTipoDt.PEDIDO_VISTA_SESSAO, PendenciaTipoDt.RESULTADO_UNANIME, PendenciaTipoDt.RETIRAR_PAUTA, PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA);
					paginaInicialDt.setTomarConhecimento(pendencias);
				}

				//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
				paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));
				
				//Consulta a quantidade de pend�ncias do tipo informativa
				paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));

				break;
			case GrupoTipoDt.DIVISAO_RECURSOS_CONSTITUCIONAIS:
				
				if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
					
					//Busca pend�ncias para serventia
					this.consultarPendenciasServentiaDRC(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);
					
					// Busca as pend�ncias para ServentiaCargo
					this.consultarPendenciasServentiaCargo(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);

					// Consulta a quantidade de pend�ncias com prazo decorrido
					paginaInicialDt.setQtdePrazoDecorrido(pendenciaNe.consultarQtdPrazosDecorridos(usuarioNe.getUsuarioDt()));
					
					//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
					paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));
				}

				//Consulta a quantidade de pend�ncias do tipo informativa
				paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));
				
				break;
				
//			case GrupoTipoDt.OFICIAL_JUSTICA:
//				if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
//					// Busca as pend�ncias para ServentiaCargo
//					this.consultarPendenciasMandadosReservadosOficial(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);
//				}
//				break;
			
			case GrupoTipoDt.CONCILIADOR_VARA:
				if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
					//Busca pend�ncias para serventia
					this.consultarPendenciasServentia(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);
				}
				break;

			default:
				if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
					// Busca as pend�ncias para ServentiaCargo
					this.consultarPendenciasServentiaCargo(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);

					//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
					paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));
				}

				//Consulta a quantidade de pend�ncias do tipo informativa
				paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));
				break;
		}

	}

	/**
	 * Busca as pend�ncias para o ServentiaCargo do usu�rio logado
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void consultarPendenciasServentiaCargo(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt, PendenciaNe pendenciaNe) throws Exception{
		SortedMap est = new TreeMap();
		List listaPendenciasNaoAnalisadas = pendenciaNe.consultarPendenciasCargoServentiaAbertasEmAndamento(usuarioDt);
		List listaPendenciasPreAnalisadas = pendenciaNe.consultarPendenciasCargoServentiaPreAnalisadas(usuarioDt);
		List listaPendenciasReservadas = pendenciaNe.consultarPendenciasCargoServentiaReservadas(usuarioDt);

		//n�o analisado
		for (int i = 0; i < listaPendenciasNaoAnalisadas.size(); i++) {
			String[] a = (String[]) listaPendenciasNaoAnalisadas.get(i);

			ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
			listaPendencias.setIdTipo(a[0]);
			listaPendencias.setTitulo(a[1]);
			listaPendencias.setQtdeNaoAnalisadas(Funcoes.StringToInt(a[2]));
			listaPendencias.setQtdePreAnalisadas(0);
			listaPendencias.setQtdeReservadas(0);
			//est.put(listaPendencias.getIdTipo(), listaPendencias);
			est.put(listaPendencias.getTitulo(), listaPendencias);
		}

		for (int i = 0; i < listaPendenciasPreAnalisadas.size(); i++) {
			String[] b = (String[]) listaPendenciasPreAnalisadas.get(i);
			//pegar pr�-analisado
			if (!est.containsKey(b[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(b[0]);
				listaPendencias.setTitulo(b[1]);
				listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
				listaPendencias.setQtdeNaoAnalisadas(0);
				listaPendencias.setQtdeReservadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(b[1]);
				listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
			}
		}

		for (int i = 0; i < listaPendenciasReservadas.size(); i++) {
			String[] c = (String[]) listaPendenciasReservadas.get(i);
			//pegar reservadas
			if (!est.containsKey(c[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(c[0]);
				listaPendencias.setTitulo(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
				listaPendencias.setQtdePreAnalisadas(0);
				listaPendencias.setQtdeNaoAnalisadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
			}
		}

		List lista = new ArrayList();
		lista.addAll(est.values());
		paginaInicialDt.setPendenciasServentiaCargo(lista);
	}
	
	/**
	 * Consulta o quantitativo de mandados que aparecer� na tela inicial do oficial de justi�a.
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @param pendenciaNe
	 * @throws Exception
	 */
	private void consultarPendenciasMandadosReservadosOficial(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt, PendenciaNe pendenciaNe) throws Exception{
		SortedMap est = new TreeMap();
		List listaPendenciasReservadas = pendenciaNe.consultarPendenciasMandadosReservadosOficial(usuarioDt);

		for (int i = 0; i < listaPendenciasReservadas.size(); i++) {
			String[] c = (String[]) listaPendenciasReservadas.get(i);
			//pegar reservadas
			if (!est.containsKey(c[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(c[0]);
				listaPendencias.setTitulo(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
				listaPendencias.setQtdePreAnalisadas(0);
				listaPendencias.setQtdeNaoAnalisadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
			}
		}
		
		List lista = new ArrayList();
		lista.addAll(est.values());
		paginaInicialDt.setPendenciasServentiaCargo(lista);
	}
	
	/**
	 * CONSULTAS PARA O MANDADO DE PRIS�O
	 * @throws Exception 
	*/
	private void consultarPendenciasServentiaMandadoPrisao(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt, PendenciaNe pendenciaNe) throws Exception{
		List lista = new ArrayList();
		// consulta quantidade de mandados de pris�o expedidos, aguardando impress�o
		lista.add(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO));
		int qtde = new MandadoPrisaoNe().consultarQuantidadeMandadoPrisaoServentia(usuarioDt.getId_Serventia(), lista, false);
		if (qtde > 0){
			ListaPendenciaDt listaPendenciaDt = new ListaPendenciaDt();
			listaPendenciaDt.setTitulo("Mandado de Pris�o aguardando Impress�o");
			listaPendenciaDt.setQuantidadePendencias(qtde);
			listaPendenciaDt.setUrlRetorno("MandadoPrisao?PaginaAtual="+Configuracao.Curinga6+"&tempFluxo1=2");
			paginaInicialDt.adicionarPendenciasServentiaMandadoPrisao(listaPendenciaDt);			
		}
		// consulta quantidade de mandados de pris�o impresso, aguardando cumprimento
		lista.add(String.valueOf(MandadoPrisaoStatusDt.IMPRESSO));
		qtde = new MandadoPrisaoNe().consultarQuantidadeMandadoPrisaoServentia(usuarioDt.getId_Serventia(), lista, false);
		if (qtde > 0){
			ListaPendenciaDt listaPendenciaDt = new ListaPendenciaDt();
			listaPendenciaDt.setTitulo("Mandado de Pris�o aguardando Cumprimento");
			listaPendenciaDt.setQuantidadePendencias(qtde);
			listaPendenciaDt.setUrlRetorno("MandadoPrisao?PaginaAtual="+Configuracao.Curinga6+"&tempFluxo1=3");
			paginaInicialDt.adicionarPendenciasServentiaMandadoPrisao(listaPendenciaDt);			
		}
		
	}
	
	/**
	 * Busca as pend�ncias para ServentiaTipo do usu�rio logado
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void consultarPendenciasServentiaTipo(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt, PendenciaNe pendenciaNe) throws Exception{
		Map mapPendenciasServentiaTipo = pendenciaNe.consultarTiposAtivasServentiaTipoPaginaInicial(usuarioDt);
		Iterator itPendenciasServentiaTipo = mapPendenciasServentiaTipo.keySet().iterator();

		while (itPendenciasServentiaTipo.hasNext()) {
			ListaPendenciaDt listaPendenciaDt = new ListaPendenciaDt();

			String chave = (String) itPendenciasServentiaTipo.next();
			String idTipo = ((String[]) mapPendenciasServentiaTipo.get(chave))[0];

			listaPendenciaDt.setIdTipo(idTipo);
			listaPendenciaDt.setTitulo(chave);

			// Consulta a quantidade de pendencias abertas (nao analisadas) para cada tipo
			listaPendenciaDt.setQtdeNaoAnalisadas(pendenciaNe.consultarQuantidadeServentiaTipoAbertasEmAndamento(usuarioDt, idTipo));

			// Consulta a quantidade de pendencias reservadas para cada tipo
			listaPendenciaDt.setQtdeReservadas(pendenciaNe.consultarQuantidadeServentiaTipoReservadasPorTipo(usuarioDt, idTipo));

			// Consulta a quantidade de pendencias que foram pre-analisadas para cada tipo
			listaPendenciaDt.setQtdePreAnalisadas(pendenciaNe.consultarQuantidadeServentiaTipoPreAnalisadasPorTipo(usuarioDt, idTipo));

			paginaInicialDt.adicionarPendenciasServentiaTipo(listaPendenciaDt);
		}

	}

	/**
	 * Busca as pend�ncias para a serventia do usu�rio logado
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @param pendenciaNe
	 * @throws Exception
	 */
	private void consultarPendenciasServentia(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt, PendenciaNe pendenciaNe) throws Exception{
		SortedMap est = new TreeMap();
		List listaPendenciasNaoAnalisadas = pendenciaNe.consultarPendenciasServentiaAbertasEmAndamento(usuarioDt);
		List listaPendenciasPreAnalisadas = pendenciaNe.consultarPendenciasServentiaPreAnalisadas(usuarioDt);
		List listaPendenciasReservadas = pendenciaNe.consultarPendenciasServentiaReservadas(usuarioDt);
		List listaPendenciasPreAnalisadasServentia = pendenciaNe.consultarPendenciasServentiaAbertasPreAnalisada(usuarioDt);

		//n�o analisado
		for (int i = 0; i < listaPendenciasNaoAnalisadas.size(); i++) {
			String[] a = (String[]) listaPendenciasNaoAnalisadas.get(i);

			ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
			listaPendencias.setIdTipo(a[0]);
			listaPendencias.setTitulo(a[1]);
			listaPendencias.setQtdeNaoAnalisadas(Funcoes.StringToInt(a[2]));
			listaPendencias.setQtdePreAnalisadas(0);
			listaPendencias.setQtdeReservadas(0);
			//est.put(listaPendencias.getIdTipo(), listaPendencias);
			est.put(listaPendencias.getTitulo(), listaPendencias);
		}

		for (int i = 0; i < listaPendenciasPreAnalisadas.size(); i++) {
			String[] b = (String[]) listaPendenciasPreAnalisadas.get(i);
			//pegar pr�-analisado
			if (!est.containsKey(b[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(b[0]);
				listaPendencias.setTitulo(b[1]);
				listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
				listaPendencias.setQtdeNaoAnalisadas(0);
				listaPendencias.setQtdeReservadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(b[1]);
				listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
			}
		}

		for (int i = 0; i < listaPendenciasReservadas.size(); i++) {
			String[] c = (String[]) listaPendenciasReservadas.get(i);
			//pegar reservadas
			if (!est.containsKey(c[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(c[0]);
				listaPendencias.setTitulo(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
				listaPendencias.setQtdePreAnalisadas(0);
				listaPendencias.setQtdeNaoAnalisadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
			}
		}
		
		//Pr�-analisadas da Serventia
		for (int i = 0; i < listaPendenciasPreAnalisadasServentia.size(); i++) {
			String[] d = (String[]) listaPendenciasPreAnalisadasServentia.get(i);
			//pegar pr�-analisado
			if (!est.containsKey(d[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(d[0]);
				listaPendencias.setTitulo(d[1]);
				listaPendencias.setQtdePreAnalisadasServentia(Funcoes.StringToInt(d[2]));
				listaPendencias.setQtdeNaoAnalisadas(0);
				listaPendencias.setQtdeReservadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(d[1]);
				listaPendencias.setQtdePreAnalisadasServentia(Funcoes.StringToInt(d[2]));
			}
		}

		List lista = new ArrayList();
		lista.addAll(est.values());
		paginaInicialDt.setPendenciasServentia(lista);

	}
	
	/**
	 * Busca as pend�ncias para a serventia do usu�rio logado
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @param pendenciaNe
	 * @throws Exception
	 */
	private void consultarPendenciasServentiaDRC(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt, PendenciaNe pendenciaNe) throws Exception{
		SortedMap est = new TreeMap();
		List listaPendenciasNaoAnalisadas = pendenciaNe.consultarPendenciasServentiaDRCAbertasEmAndamento(usuarioDt);
		List listaPendenciasPreAnalisadas = pendenciaNe.consultarPendenciasServentiaPreAnalisadas(usuarioDt);
		List listaPendenciasReservadas = pendenciaNe.consultarPendenciasServentiaReservadas(usuarioDt);

		//n�o analisado
		for (int i = 0; i < listaPendenciasNaoAnalisadas.size(); i++) {
			String[] a = (String[]) listaPendenciasNaoAnalisadas.get(i);

			ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
			listaPendencias.setIdTipo(a[0]);
			listaPendencias.setTitulo(a[1]);
			listaPendencias.setQtdeNaoAnalisadas(Funcoes.StringToInt(a[2]));
			listaPendencias.setQtdePreAnalisadas(0);
			listaPendencias.setQtdeReservadas(0);
			//est.put(listaPendencias.getIdTipo(), listaPendencias);
			est.put(listaPendencias.getTitulo(), listaPendencias);
		}

		for (int i = 0; i < listaPendenciasPreAnalisadas.size(); i++) {
			String[] b = (String[]) listaPendenciasPreAnalisadas.get(i);
			//pegar pr�-analisado
			if (!est.containsKey(b[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(b[0]);
				listaPendencias.setTitulo(b[1]);
				listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
				listaPendencias.setQtdeNaoAnalisadas(0);
				listaPendencias.setQtdeReservadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(b[1]);
				listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
			}
		}

		for (int i = 0; i < listaPendenciasReservadas.size(); i++) {
			String[] c = (String[]) listaPendenciasReservadas.get(i);
			//pegar reservadas
			if (!est.containsKey(c[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(c[0]);
				listaPendencias.setTitulo(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
				listaPendencias.setQtdePreAnalisadas(0);
				listaPendencias.setQtdeNaoAnalisadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
			}
		}

		List lista = new ArrayList();
		lista.addAll(est.values());
		paginaInicialDt.setPendenciasServentia(lista);
		
	}

	/**
	 * Consulta dados referentes � pend�ncias de assistentes.
	 * Traz todas as pend�ncias para o cargo do usu�rio chefe
	 * 
	 * @param usuarioDt
	 * @param idTipo
	 * @param paginaInicialDt
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void configurarPendenciasAssistente(UsuarioNe usuarioNe, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{

		SortedMap est = new TreeMap();
		List listaPendenciasPreAnalisadas = pendenciaNe.consultarPendenciasServentiaPreAnalisadas(usuarioNe.getUsuarioDt());
		List listaPendenciasReservadas = pendenciaNe.consultarPendenciasServentiaReservadas(usuarioNe.getUsuarioDt());

		List listaPendenciasServentiaCargoPreAnalisadas = pendenciaNe.consultarPendenciasCargoServentiaPreAnalisadas(usuarioNe.getUsuarioDt());
		List listaPendenciasServentiaCargoReservadas = pendenciaNe.consultarPendenciasCargoServentiaReservadas(usuarioNe.getUsuarioDt());

		//n�o analisado
		for (int i = 0; i < listaPendenciasPreAnalisadas.size(); i++) {
			String[] a = (String[]) listaPendenciasPreAnalisadas.get(i);

			ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
			listaPendencias.setIdTipo(a[0]);
			listaPendencias.setTitulo(a[1]);
			listaPendencias.setQtdeNaoAnalisadas(0);
			listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(a[2]));
			listaPendencias.setQtdeReservadas(0);
			//est.put(listaPendencias.getIdTipo(), listaPendencias);
			est.put(listaPendencias.getTitulo(), listaPendencias);
		}

		for (int i = 0; i < listaPendenciasReservadas.size(); i++) {
			String[] c = (String[]) listaPendenciasReservadas.get(i);
			//pegar reservadas
			if (!est.containsKey(c[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(c[0]);
				listaPendencias.setTitulo(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
				listaPendencias.setQtdePreAnalisadas(0);
				listaPendencias.setQtdeNaoAnalisadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(c[1]);
				if (listaPendencias.getQtdeReservadas() > 0) listaPendencias.setQtdeReservadas(listaPendencias.getQtdeReservadas() + Funcoes.StringToInt(c[2]));
				else listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
			}
		}

		for (int i = 0; i < listaPendenciasServentiaCargoPreAnalisadas.size(); i++) {
			String[] b = (String[]) listaPendenciasServentiaCargoPreAnalisadas.get(i);
			//pegar pr�-analisado
			if (!est.containsKey(b[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(b[0]);
				listaPendencias.setTitulo(b[1]);
				listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
				listaPendencias.setQtdeNaoAnalisadas(0);
				listaPendencias.setQtdeReservadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(b[1]);
				if (listaPendencias.getQtdePreAnalisadas() > 0) listaPendencias.setQtdePreAnalisadas(listaPendencias.getQtdePreAnalisadas() + Funcoes.StringToInt(b[2]));
				else listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
			}
		}

		for (int i = 0; i < listaPendenciasServentiaCargoReservadas.size(); i++) {
			String[] c = (String[]) listaPendenciasServentiaCargoReservadas.get(i);
			//pegar reservadas
			if (!est.containsKey(c[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(c[0]);
				listaPendencias.setTitulo(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
				listaPendencias.setQtdePreAnalisadas(0);
				listaPendencias.setQtdeNaoAnalisadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(c[1]);

				if (listaPendencias.getQtdeReservadas() > 0) listaPendencias.setQtdeReservadas(listaPendencias.getQtdeReservadas() + Funcoes.StringToInt(c[2]));
				else listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
			}
		}

		List lista = new ArrayList();
		lista.addAll(est.values());
		paginaInicialDt.setPendenciasServentia(lista);
	}

	/**
	 * Realiza tratamentos para Assistente de Promotor e Advogado, pois s�o casos semelhantes.
	 * @param usuarioNe
	 * @param paginaInicialDt
	 * @param pendenciaNe
	 * @throws Exception
	 */
	private void configurarPendenciasAssistenteAdvogadoPromotor(UsuarioNe usuarioNe, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{

		List intimacoes = new ArrayList();
		List intimacoesAguardandoParecer = new ArrayList();
		List intimacoesLeituraAutomaticaAguardandoParecer = new ArrayList();
		List pendenciasIntimacoesLeituraAutomaticaAguardandoParecer = null;
		int grupoChefe = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoUsuarioChefe());
		
		if (grupoChefe == GrupoDt.MINISTERIO_PUBLICO || grupoChefe == GrupoDt.MP_TCE) {
			
			//Consulta as pendencias (serventia cargo) do tipo intimacao aguardando parecer do promotor chefe do assistente
			List pendenciasIntimacoesAguardandoParecer = pendenciaNe.consultarIntimacoesPromotorAguardandoParecer(usuarioNe);
			
			if (pendenciasIntimacoesAguardandoParecer != null) {
				for (int i = 0; i < pendenciasIntimacoesAguardandoParecer.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasIntimacoesAguardandoParecer.get(i);
					intimacoesAguardandoParecer.add(dt);
				}
	
				if (intimacoesAguardandoParecer.size() > 0) {
					ListaPendenciaDt listaIntimacoesAguardandoParecer = new ListaPendenciaDt();
					listaIntimacoesAguardandoParecer.setTitulo("Intima��es do Promotor Aguardando Parecer");
					listaIntimacoesAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesAguardandoParecer.setPendenciasAndamento(intimacoesAguardandoParecer);
					paginaInicialDt.adicionarPendenciasIntimacoesAguardandoParecer(listaIntimacoesAguardandoParecer);
				}
	
			}
			
			// Consulta as pendencias (serventia cargo do chefe do assistente promotor) do tipo intimacao do promotor
			List pendencias = pendenciaNe.consultarIntimacoesPromotorChefe(usuarioNe);
			
			if (pendencias != null) {
				for (int i = 0; i < pendencias.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendencias.get(i);
					intimacoes.add(dt);
				}

				if (intimacoes.size() > 0) {
					ListaPendenciaDt listaIntimacoes = new ListaPendenciaDt();
					listaIntimacoes.setTitulo("Intima��es do Promotor Aguardando Leitura");
					listaIntimacoes.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoes.setPendenciasAndamento(intimacoes);
					paginaInicialDt.adicionarPendenciasServentiaCargo(listaIntimacoes);
				}

			}
			
			//	Consulta as pendencias (serventia cargo) do tipo intimacao lidas automaticamente aguardando parecer do promotor chefe do assistente
			pendenciasIntimacoesLeituraAutomaticaAguardandoParecer = pendenciaNe.consultarIntimacoesLidasAutomaticamentePromotorAguardandoParecer(usuarioNe);

			if (pendenciasIntimacoesLeituraAutomaticaAguardandoParecer != null) {
				for (int i = 0; i < pendenciasIntimacoesLeituraAutomaticaAguardandoParecer.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasIntimacoesLeituraAutomaticaAguardandoParecer.get(i);
					intimacoesLeituraAutomaticaAguardandoParecer.add(dt);
				}

				if (intimacoesLeituraAutomaticaAguardandoParecer.size() > 0) {
					ListaPendenciaDt listaIntimacoesLeituraAutomaticaAguardandoParecer = new ListaPendenciaDt();
					listaIntimacoesLeituraAutomaticaAguardandoParecer.setTitulo("Intima��es Lidas Automaticamente do Promotor Aguardando Parecer");
					listaIntimacoesLeituraAutomaticaAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesLeituraAutomaticaAguardandoParecer.setPendenciasAndamento(intimacoesLeituraAutomaticaAguardandoParecer);
					paginaInicialDt.adicionarPendenciasIntimacoesLeituraAutomaticaAguardandoParecer(listaIntimacoesLeituraAutomaticaAguardandoParecer);
				}

			}

			//Chama m�todo que ir� trazer outras pend�ncias para o Cargo do Promotor chefe
			this.configurarPendenciasAssistente(usuarioNe, paginaInicialDt, pendenciaNe);
		} else {
			
			//Consulta as pendencias (serventia cargo) do tipo intimacao aguardando parecer do promotor chefe do assistente
			List pendenciasIntimacoesAguardandoParecer = pendenciaNe.consultarIntimacoesAdvogadoAguardandoPeticionamento(usuarioNe);
			
			if (pendenciasIntimacoesAguardandoParecer != null) {
				for (int i = 0; i < pendenciasIntimacoesAguardandoParecer.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasIntimacoesAguardandoParecer.get(i);
					intimacoesAguardandoParecer.add(dt);
				}
	
				if (intimacoesAguardandoParecer.size() > 0) {
					ListaPendenciaDt listaIntimacoesAguardandoParecer = new ListaPendenciaDt();
					listaIntimacoesAguardandoParecer.setTitulo("Intima��es do Advogado Aguardando Peticionamento");
					listaIntimacoesAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesAguardandoParecer.setPendenciasAndamento(intimacoesAguardandoParecer);
					paginaInicialDt.adicionarPendenciasIntimacoesAguardandoParecer(listaIntimacoesAguardandoParecer);
				}
	
			}
			
			// Consulta as pendencias tipo intimacao que foram lidas automaticamente do advogado aguardando peticionamento
			pendenciasIntimacoesLeituraAutomaticaAguardandoParecer = pendenciaNe.consultarIntimacoesLidasAutomaticamenteAdvogadoAguardandoPeticionamento(usuarioNe);

			if (pendenciasIntimacoesLeituraAutomaticaAguardandoParecer != null) {
				for (int i = 0; i < pendenciasIntimacoesLeituraAutomaticaAguardandoParecer.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasIntimacoesLeituraAutomaticaAguardandoParecer.get(i);
					intimacoesLeituraAutomaticaAguardandoParecer.add(dt);
				}

				if (intimacoesLeituraAutomaticaAguardandoParecer.size() > 0) {
					ListaPendenciaDt listaIntimacoesLeituraAutomaticaAguardandoParecer = new ListaPendenciaDt();
					listaIntimacoesLeituraAutomaticaAguardandoParecer.setTitulo("Intima��es Lidas Automaticamente Aguardando Peticionamento");
					listaIntimacoesLeituraAutomaticaAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesLeituraAutomaticaAguardandoParecer.setPendenciasAndamento(intimacoesLeituraAutomaticaAguardandoParecer);
					paginaInicialDt.adicionarPendenciasIntimacoesLeituraAutomaticaAguardandoParecer(listaIntimacoesLeituraAutomaticaAguardandoParecer);
				}

			}
		}
		
		//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo do chefe
		paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));
		
		//Consulta a quantidade de pend�ncias do tipo informativa
		paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));

		//Busca pend�ncias do tipo Pedido de Manifesta��o
		this.configurarOutrasPendenciasAnalise(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);

	}

	/**
	 * Consulta dados referentes � pend�ncias de advogados Retorna intima��es e cita��es detalhadas
	 * e quantitativo de outras pend�ncias
	 * 
	 * @param usuarioDt
	 * @param idTipo
	 * @param paginaInicialDt
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void configurarPendenciasAdvogado(UsuarioNe usuarioNe, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{
		List intimacoes = new ArrayList();
		List intimacoesAguardandoParecer = new ArrayList();
		List intimacoesLeituraAutomaticaAguardandoParecer = new ArrayList();
		List intimacoePublicadasDiarioEletronicoAguardandoParecer = new ArrayList();
		List citacoes = new ArrayList();

		// Consulta as pendencias do tipo intimacao e cita��o do advogado
		List pendencias = pendenciaNe.consultarIntimacoesCitacoesAdvogado(usuarioNe);
		
		// Consulta as pendencias (serventia cargo) do tipo intimacao do promotor aguardando parecer
		List pendenciasIntimacoesAguardandoParecer = pendenciaNe.consultarIntimacoesAdvogadoAguardandoPeticionamento(usuarioNe);

		// Consulta as pendencias tipo intimacao que foram lidas automaticamente do advogado aguardando peticionamento
		List pendenciasIntimacoeLeituraAutomaticaAguardandoParecer = pendenciaNe.consultarIntimacoesLidasAutomaticamenteAdvogadoAguardandoPeticionamento(usuarioNe);
		
		// Consulta as pendencias tipo intimacao que foram publicadas no Diario Eletronico do advogado aguardando peticionamento
		List pendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer = pendenciaNe.consultarIntimacoesPublicadasDiarioEletronicoAdvogadoAguardandoPeticionamento(usuarioNe);

		//Consulta a quantidade de pend�ncias do tipo informativa
		paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));

		if (pendencias != null) {
			for (int i = 0; i < pendencias.size(); i++) {
				PendenciaDt dt = (PendenciaDt) pendencias.get(i);
				if (Funcoes.StringToInt(dt.getPendenciaTipoCodigo()) == PendenciaTipoDt.INTIMACAO) intimacoes.add(dt);
				else if (Funcoes.StringToInt(dt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CARTA_CITACAO) citacoes.add(dt);
			}

			if (intimacoes.size() > 0) {
				ListaPendenciaDt listaIntimacoes = new ListaPendenciaDt();
				listaIntimacoes.setTitulo("Intima��es do Advogado Aguardando Leitura");
				listaIntimacoes.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
				listaIntimacoes.setPendenciasAndamento(intimacoes);
				paginaInicialDt.adicionarPendenciasUsuarioServentia(listaIntimacoes);
			}

			if (citacoes.size() > 0) {
				ListaPendenciaDt listaCitacoes = new ListaPendenciaDt();
				listaCitacoes.setTitulo("Cita��es do Advogado");
				listaCitacoes.setIdTipo(String.valueOf(PendenciaTipoDt.CARTA_CITACAO));
				listaCitacoes.setPendenciasAndamento(citacoes);
				paginaInicialDt.adicionarPendenciasUsuarioServentia(listaCitacoes);
			}

		}
		
		if (pendenciasIntimacoesAguardandoParecer != null) {
			for (int i = 0; i < pendenciasIntimacoesAguardandoParecer.size(); i++) {
				PendenciaDt dt = (PendenciaDt) pendenciasIntimacoesAguardandoParecer.get(i);
				intimacoesAguardandoParecer.add(dt);
			}

			if (intimacoesAguardandoParecer.size() > 0) {
				ListaPendenciaDt listaIntimacoesAguardandoParecer = new ListaPendenciaDt();
				listaIntimacoesAguardandoParecer.setTitulo("Intima��es Lidas Aguardando Peticionamento");
				listaIntimacoesAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
				listaIntimacoesAguardandoParecer.setPendenciasAndamento(intimacoesAguardandoParecer);
				paginaInicialDt.adicionarPendenciasIntimacoesAguardandoParecer(listaIntimacoesAguardandoParecer);
			}

		}

		if (pendenciasIntimacoeLeituraAutomaticaAguardandoParecer != null) {
			for (int i = 0; i < pendenciasIntimacoeLeituraAutomaticaAguardandoParecer.size(); i++) {
				PendenciaDt dt = (PendenciaDt) pendenciasIntimacoeLeituraAutomaticaAguardandoParecer.get(i);
				intimacoesLeituraAutomaticaAguardandoParecer.add(dt);
			}

			if (intimacoesLeituraAutomaticaAguardandoParecer.size() > 0) {
				ListaPendenciaDt listaIntimacoesLeituraAutomaticaAguardandoParecer = new ListaPendenciaDt();
				listaIntimacoesLeituraAutomaticaAguardandoParecer.setTitulo("Intima��es Lidas Automaticamente");
				listaIntimacoesLeituraAutomaticaAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
				listaIntimacoesLeituraAutomaticaAguardandoParecer.setPendenciasAndamento(intimacoesLeituraAutomaticaAguardandoParecer);
				paginaInicialDt.adicionarPendenciasIntimacoesLeituraAutomaticaAguardandoParecer(listaIntimacoesLeituraAutomaticaAguardandoParecer);
			}

		}
		
		//****
		if (pendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer != null) {
			for (int i = 0; i < pendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer.size(); i++) {
				PendenciaDt dt = (PendenciaDt) pendenciasIntimacoePublicadasDiarioEletronicoAguardandoParecer.get(i);
				intimacoePublicadasDiarioEletronicoAguardandoParecer.add(dt);
			}

			if (intimacoePublicadasDiarioEletronicoAguardandoParecer.size() > 0) {
				ListaPendenciaDt listaIntimacoesPublicadasDiarioEletronicoAguardandoParecer = new ListaPendenciaDt();
				listaIntimacoesPublicadasDiarioEletronicoAguardandoParecer.setTitulo("Intima��es/Cita��es Publicadas no Di�rio Eletr�nico");
				listaIntimacoesPublicadasDiarioEletronicoAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
				listaIntimacoesPublicadasDiarioEletronicoAguardandoParecer.setPendenciasAndamento(intimacoePublicadasDiarioEletronicoAguardandoParecer);
				paginaInicialDt.adicionarPendenciasIntimacoesPublicadasDiarioEletronicoAguardandoParecer(listaIntimacoesPublicadasDiarioEletronicoAguardandoParecer);
			}

		}
		//****
		
		//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
		paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));

		//Consulta pend�ncias do tipo Pedido de Manifesta��o
		this.configurarOutrasPendenciasAnalise(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);
	}

	/**
	 * Consulta dados referentes � pend�ncias de promotores Retorna intima��es detalhadas
	 * e quantitativo de outras pend�ncias
	 * 
	 * @param usuarioDt
	 * @param idTipo
	 * @param paginaInicialDt
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void configurarPendenciasPromotor(UsuarioNe usuarioNe, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{
		List intimacoes = new ArrayList();
		List intimacoesUsuarioServentia = new ArrayList();
		List intimacoesUsuarioServentiaLidasAutomaticamente = new ArrayList();
		List intimacoesAguardandoParecer = new ArrayList();
		List intimacoesLeituraAutomaticaAguardandoParecer = new ArrayList();

		if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
			// Consulta as pendencias (serventia cargo) do tipo intimacao do promotor
			List pendencias = pendenciaNe.consultarIntimacoesPromotor(usuarioNe);
			
			// Consulta as pendencias do tipo intimacao do usuario serventia promotor
			List pendenciasUsuarioServentia = pendenciaNe.consultarIntimacoesCitacoesAdvogado(usuarioNe);
			
			// Consulta as pendencias tipo intimacao que foram lidas automaticamente do usuario serventia aguardando peticionamento
			List pendenciasIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer = pendenciaNe.consultarIntimacoesLidasAutomaticamenteAdvogadoAguardandoPeticionamento(usuarioNe);

			// Consulta as pendencias (serventia cargo) do tipo intimacao do promotor aguardando parecer
			List pendenciasIntimacoesAguardandoParecer = pendenciaNe.consultarIntimacoesPromotorAguardandoParecer(usuarioNe);

			// Consulta as pendencias tipo intimacao que foram lidas automaticamente do advogado aguardando peticionamento
			List pendenciasIntimacoeLeituraAutomaticaAguardandoParecer = pendenciaNe.consultarIntimacoesLidasAutomaticamentePromotorAguardandoParecer(usuarioNe);

			//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
			paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));

			//Consulta a quantidade de pend�ncias do tipo informativa
			//paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));

			if (pendencias != null) {
				for (int i = 0; i < pendencias.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendencias.get(i);
					intimacoes.add(dt);
				}

				if (intimacoes.size() > 0) {
					ListaPendenciaDt listaIntimacoes = new ListaPendenciaDt();
					listaIntimacoes.setTitulo("Intima��es do Promotor Aguardando Leitura");
					listaIntimacoes.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoes.setPendenciasAndamento(intimacoes);
					paginaInicialDt.adicionarPendenciasServentiaCargo(listaIntimacoes);
				}

			}
			
			//******************
			if (pendenciasUsuarioServentia != null) {
				for (int i = 0; i < pendenciasUsuarioServentia.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasUsuarioServentia.get(i);
					if (Funcoes.StringToInt(dt.getPendenciaTipoCodigo()) == PendenciaTipoDt.INTIMACAO) 
						intimacoesUsuarioServentia.add(dt);
				}

				if (pendenciasUsuarioServentia.size() > 0) {
					ListaPendenciaDt listaIntimacoesUsuarioServentia = new ListaPendenciaDt();
					listaIntimacoesUsuarioServentia.setTitulo("Intima��es do Promotor (Substituto Processual) Aguardando Leitura");
					listaIntimacoesUsuarioServentia.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesUsuarioServentia.setPendenciasAndamento(intimacoesUsuarioServentia);
					paginaInicialDt.adicionarPendenciasUsuarioServentia(listaIntimacoesUsuarioServentia);
				}

			}
			
			if (pendenciasIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer != null) {
				for (int i = 0; i < pendenciasIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer.get(i);
					intimacoesUsuarioServentiaLidasAutomaticamente.add(dt);
				}

				if (pendenciasIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer.size() > 0) {
					ListaPendenciaDt listaIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer = new ListaPendenciaDt();
					listaIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer.setTitulo("Intima��es do Promotor (Substituto Processual) Lidas Automaticamente Aguardando Parecer");
					listaIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer.setPendenciasAndamento(intimacoesUsuarioServentiaLidasAutomaticamente);
					paginaInicialDt.adicionarPendenciasUsuarioServentiaLidasAutomaticamente(listaIntimacoeUsuarioServentiaLeituraAutomaticaAguardandoParecer);
				}

			}
			
			//*******************

			if (pendenciasIntimacoesAguardandoParecer != null) {
				for (int i = 0; i < pendenciasIntimacoesAguardandoParecer.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasIntimacoesAguardandoParecer.get(i);
					intimacoesAguardandoParecer.add(dt);
				}

				if (intimacoesAguardandoParecer.size() > 0) {
					ListaPendenciaDt listaIntimacoesAguardandoParecer = new ListaPendenciaDt();
					listaIntimacoesAguardandoParecer.setTitulo("Intima��es Lidas Aguardando Parecer");
					listaIntimacoesAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesAguardandoParecer.setPendenciasAndamento(intimacoesAguardandoParecer);
					paginaInicialDt.adicionarPendenciasIntimacoesAguardandoParecer(listaIntimacoesAguardandoParecer);
				}

			}

			if (pendenciasIntimacoeLeituraAutomaticaAguardandoParecer != null) {
				for (int i = 0; i < pendenciasIntimacoeLeituraAutomaticaAguardandoParecer.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasIntimacoeLeituraAutomaticaAguardandoParecer.get(i);
					intimacoesLeituraAutomaticaAguardandoParecer.add(dt);
				}

				if (intimacoesLeituraAutomaticaAguardandoParecer.size() > 0) {
					ListaPendenciaDt listaIntimacoesLeituraAutomaticaAguardandoParecer = new ListaPendenciaDt();
					listaIntimacoesLeituraAutomaticaAguardandoParecer.setTitulo("Intima��es Lidas Automaticamente Aguardando Parecer");
					listaIntimacoesLeituraAutomaticaAguardandoParecer.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesLeituraAutomaticaAguardandoParecer.setPendenciasAndamento(intimacoesLeituraAutomaticaAguardandoParecer);
					paginaInicialDt.adicionarPendenciasIntimacoesLeituraAutomaticaAguardandoParecer(listaIntimacoesLeituraAutomaticaAguardandoParecer);
				}

			}

			SortedMap est = new TreeMap();
			List listaPendenciasNaoAnalisadas = pendenciaNe.consultarPendenciasCargoServentiaAbertasEmAndamento(usuarioNe.getUsuarioDt());
			List listaPendenciasPreAnalisadas = pendenciaNe.consultarPendenciasCargoServentiaPreAnalisadas(usuarioNe.getUsuarioDt());
			List listaPendenciasReservadas = pendenciaNe.consultarPendenciasCargoServentiaReservadas(usuarioNe.getUsuarioDt());

			//n�o analisado
			for (int i = 0; i < listaPendenciasNaoAnalisadas.size(); i++) {
				String[] a = (String[]) listaPendenciasNaoAnalisadas.get(i);

				if (!a[0].equals(PendenciaTipoDt.INTIMACAO)) {
					ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
					listaPendencias.setIdTipo(a[0]);
					listaPendencias.setTitulo(a[1]);
					listaPendencias.setQtdeNaoAnalisadas(Funcoes.StringToInt(a[2]));
					listaPendencias.setQtdePreAnalisadas(0);
					listaPendencias.setQtdeReservadas(0);
					//est.put(listaPendencias.getIdTipo(), listaPendencias);
					est.put(listaPendencias.getTitulo(), listaPendencias);
				}
			}

			for (int i = 0; i < listaPendenciasPreAnalisadas.size(); i++) {
				String[] b = (String[]) listaPendenciasPreAnalisadas.get(i);
				//pegar pr�-analisado
				if (!est.containsKey(b[1])) {
					if (!b[0].equals(PendenciaTipoDt.INTIMACAO)) {
						ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
						listaPendencias.setIdTipo(b[0]);
						listaPendencias.setTitulo(b[1]);
						listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
						listaPendencias.setQtdeNaoAnalisadas(0);
						listaPendencias.setQtdeReservadas(0);
						est.put(listaPendencias.getTitulo(), listaPendencias);
					}
				} else {
					ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(b[1]);
					listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
				}
			}

			for (int i = 0; i < listaPendenciasReservadas.size(); i++) {
				String[] c = (String[]) listaPendenciasReservadas.get(i);
				//pegar reservadas
				if (!est.containsKey(c[1])) {
					if (!c[0].equals(PendenciaTipoDt.INTIMACAO)) {
						ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
						listaPendencias.setIdTipo(c[0]);
						listaPendencias.setTitulo(c[1]);
						listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
						listaPendencias.setQtdePreAnalisadas(0);
						listaPendencias.setQtdeNaoAnalisadas(0);
						est.put(listaPendencias.getTitulo(), listaPendencias);
					}
				} else {
					ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(c[1]);
					listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
				}
			}

			List lista = new ArrayList();
			lista.addAll(est.values());
			for (int i = 0; i < lista.size(); i++) {
				paginaInicialDt.adicionarPendenciasServentiaCargo((ListaPendenciaDt) lista.get(i));
			}

			SortedMap mapServentia = new TreeMap();
			List listaPendenciasServentiaNaoAnalisadas = pendenciaNe.consultarPendenciasServentiaAbertasEmAndamento(usuarioNe.getUsuarioDt());
			List listaPendenciasServentiaPreAnalisadas = pendenciaNe.consultarPendenciasServentiaPreAnalisadas(usuarioNe.getUsuarioDt());
			List listaPendenciasServentiaReservadas = pendenciaNe.consultarPendenciasServentiaReservadas(usuarioNe.getUsuarioDt());

			//n�o analisado
			for (int i = 0; i < listaPendenciasServentiaNaoAnalisadas.size(); i++) {
				String[] a = (String[]) listaPendenciasServentiaNaoAnalisadas.get(i);

				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(a[0]);
				listaPendencias.setTitulo(a[1]);
				listaPendencias.setQtdeNaoAnalisadas(Funcoes.StringToInt(a[2]));
				listaPendencias.setQtdePreAnalisadas(0);
				listaPendencias.setQtdeReservadas(0);
				//est.put(listaPendencias.getIdTipo(), listaPendencias);
				mapServentia.put(listaPendencias.getTitulo(), listaPendencias);
			}

			for (int i = 0; i < listaPendenciasServentiaPreAnalisadas.size(); i++) {
				String[] b = (String[]) listaPendenciasServentiaPreAnalisadas.get(i);
				//pegar pr�-analisado
				if (!mapServentia.containsKey(b[1])) {
					ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
					listaPendencias.setIdTipo(b[0]);
					listaPendencias.setTitulo(b[1]);
					listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
					listaPendencias.setQtdeNaoAnalisadas(0);
					listaPendencias.setQtdeReservadas(0);
					mapServentia.put(listaPendencias.getTitulo(), listaPendencias);
				} else {
					ListaPendenciaDt listaPendencias = (ListaPendenciaDt) mapServentia.get(b[1]);
					listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
				}
			}

			for (int i = 0; i < listaPendenciasServentiaReservadas.size(); i++) {
				String[] c = (String[]) listaPendenciasServentiaReservadas.get(i);
				//pegar reservadas
				if (!mapServentia.containsKey(c[1])) {
					ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
					listaPendencias.setIdTipo(c[0]);
					listaPendencias.setTitulo(c[1]);
					listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
					listaPendencias.setQtdePreAnalisadas(0);
					listaPendencias.setQtdeNaoAnalisadas(0);
					mapServentia.put(listaPendencias.getTitulo(), listaPendencias);
				} else {
					ListaPendenciaDt listaPendencias = (ListaPendenciaDt) mapServentia.get(c[1]);
					listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
				}
			}

			List listaPendenciasServentia = new ArrayList();
			listaPendenciasServentia.addAll(mapServentia.values());
			paginaInicialDt.setPendenciasServentia(listaPendenciasServentia);

		}
		
		//Consulta a quantidade de pend�ncias do tipo informativa
		paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));

		//Procura por pend�ncias do tipo Pedido de Manifesta��o
		this.configurarOutrasPendenciasAnalise(paginaInicialDt, usuarioNe.getUsuarioDt(), pendenciaNe);
		VotoNe votoNe = new VotoNe();
		int qtde = votoNe .consultarQuantidadePendenciasConhecimento(usuarioNe.getId_ServentiaCargo());
		paginaInicialDt.setQtdeConhecimentoNaoAnalisadas(qtde);
		qtde = votoNe.consultarQuantidadePendenciasConhecimentoPreAnalisadas(usuarioNe.getId_ServentiaCargo());
		paginaInicialDt.setQtdeConhecimentoPreAnalisadas(qtde);
		qtde = votoNe.consultarQuantidadePendenciaVerificarImpedimento(usuarioNe.getId_ServentiaCargo());
		paginaInicialDt.setQtdeVerificarImpedimentoNaoAnalisadas(qtde);
		qtde = votoNe.consultarQuantidadePendenciaVerificarImpedimentoPreAnalisadas(usuarioNe.getId_ServentiaCargo());
		paginaInicialDt.setQtdeVerificarImpedimentoPreAnalisadas(qtde);

		String idServentiaCargo = (StringUtils.isEmpty(usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe()) ? usuarioNe.getUsuarioDt().getId_ServentiaCargo() : usuarioNe.getUsuarioDt().getId_ServentiaCargoUsuarioChefe());
		List pendencias = pendenciaNe.consultarQuantidadeSessaoVirtualTomarConhecimento(usuarioNe.getUsuarioDt().getId_ServentiaCargo(), null, 
							PendenciaTipoDt.PEDIDO_VISTA_SESSAO, PendenciaTipoDt.RESULTADO_UNANIME, PendenciaTipoDt.RETIRAR_PAUTA, PendenciaTipoDt.ADIADO_PELO_RELATOR, PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_INDEFERIDA, PendenciaTipoDt.RESULTADO_VOTACAO);
		paginaInicialDt.setTomarConhecimento(pendencias);

	}

	/**
	 * Consulta dados referentes � pend�ncias de coordenador de promotoria
	 * 
	 * @param usuarioDt
	 * @param paginaInicialDt
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void configurarPendenciasCoordenadorPromotoria(UsuarioNe usuarioNe, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{
		List intimacoes = new ArrayList();
		List intimacoesSubstitutoProcessual = new ArrayList();
		List intimacoesLidas = new ArrayList();
		List intimacoesLidasSubstitutoProcessual = new ArrayList();
		
		if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
			// Consulta as intima��es da serventia
			List pendencias = pendenciaNe.consultarIntimacoesPromotoria(usuarioNe);

			//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
			paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));

			if (pendencias != null) {
				for (int i = 0; i < pendencias.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendencias.get(i);
					
					if (dt.getNomeParte() != null && dt.getNomeParte().length()>0)
						intimacoesSubstitutoProcessual.add(dt);
					else						
						intimacoes.add(dt);
				}

				if (intimacoes.size() > 0) {
					ListaPendenciaDt listaIntimacoes = new ListaPendenciaDt();
					listaIntimacoes.setTitulo("Intima��es da Serventia Aguardando Leitura");
					listaIntimacoes.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoes.setPendenciasAndamento(intimacoes);
					paginaInicialDt.adicionarPendenciasServentia(listaIntimacoes);
				}
				
				if (intimacoesSubstitutoProcessual.size() > 0) {
					ListaPendenciaDt listaIntimacoesSubstitutoProcessual = new ListaPendenciaDt();
					listaIntimacoesSubstitutoProcessual.setTitulo("Intima��es da Serventia (Substituto Processual) Aguardando Leitura");
					listaIntimacoesSubstitutoProcessual.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesSubstitutoProcessual.setPendenciasAndamento(intimacoesSubstitutoProcessual);
					paginaInicialDt.adicionarPendenciasUsuarioServentia(listaIntimacoesSubstitutoProcessual);
				}
				
			}
			
			// Consulta as intima��es lidas e dentro do prazo da serventia
			List pendenciasLidas = pendenciaNe.consultarIntimacoesLidasDistribuicaoPromotoria(usuarioNe);
			
			if (pendenciasLidas != null) {
				for (int i = 0; i < pendenciasLidas.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasLidas.get(i);
					
					if (dt.getNomeParte() != null && dt.getNomeParte().length()>0)
						intimacoesLidasSubstitutoProcessual.add(dt);
					else						
						intimacoesLidas.add(dt);
				}

				if (intimacoesLidas.size() > 0) {
					ListaPendenciaDt listaIntimacoesLidas = new ListaPendenciaDt();
					listaIntimacoesLidas.setTitulo("Intima��es da Serventia Lidas e Dentro do Prazo de Cumprimento");
					listaIntimacoesLidas.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesLidas.setPendenciasAndamento(intimacoesLidas);
					paginaInicialDt.adicionarPendenciasServentia(listaIntimacoesLidas);
				}
				
				if (intimacoesLidasSubstitutoProcessual.size() > 0) {
					ListaPendenciaDt listaIntimacoesLidasSubstitutoProcessual = new ListaPendenciaDt();
					listaIntimacoesLidasSubstitutoProcessual.setTitulo("Intima��es da Serventia (Substituto Processual) Lidas e Dentro do Prazo de Cumprimento");
					listaIntimacoesLidasSubstitutoProcessual.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoesLidasSubstitutoProcessual.setPendenciasAndamento(intimacoesLidasSubstitutoProcessual);
					paginaInicialDt.adicionarPendenciasUsuarioServentia(listaIntimacoesLidasSubstitutoProcessual);
				}				
			}
		}
		
		//Consulta a quantidade de pend�ncias do tipo informativa
		paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));
	}
	
	/**
	 * Consulta dados referentes � pend�ncias de coordenador de procuradoria
	 * 
	 * @param usuarioDt
	 * @param paginaInicialDt
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void configurarPendenciasIntimacoesCoordenadorProcuradoria(UsuarioNe usuarioNe, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{
		List intimacoes = new ArrayList();
		List intimacoesLidas = new ArrayList();
		if (usuarioNe.getUsuarioDt() != null && !usuarioNe.getUsuarioDt().getId_Serventia().trim().equals("")) {
			// Consulta as intima��es da serventia
			List pendencias = pendenciaNe.consultarIntimacoesProcuradoria(usuarioNe);

			//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
			paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));

			if (pendencias != null) {
				for (int i = 0; i < pendencias.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendencias.get(i);
					intimacoes.add(dt);
				}

				if (intimacoes.size() > 0) {
					ListaPendenciaDt listaIntimacoes = new ListaPendenciaDt();
					listaIntimacoes.setTitulo("Intima��es da Serventia Aguardando Leitura");
					listaIntimacoes.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
					listaIntimacoes.setPendenciasAndamento(intimacoes);
					paginaInicialDt.adicionarPendenciasServentia(listaIntimacoes);
				}
			}
			
//			List pendenciasLidas = pendenciaNe.consultarIntimacoesLidasDistribuicaoProcuradoria(usuarioNe);
//
//			if (pendenciasLidas != null) {
//				for (int i = 0; i < pendenciasLidas.size(); i++) {
//					PendenciaDt dt = (PendenciaDt) pendenciasLidas.get(i);
//					intimacoesLidas.add(dt);
//				}
//
//				if (intimacoesLidas.size() > 0) {
//					ListaPendenciaDt listaIntimacoesLidas = new ListaPendenciaDt();
//					listaIntimacoesLidas.setTitulo("Intima��es da Serventia Lidas e Dentro do Prazo de Cumprimento");
//					listaIntimacoesLidas.setIdTipo(String.valueOf(PendenciaTipoDt.INTIMACAO));
//					listaIntimacoesLidas.setPendenciasAndamento(intimacoesLidas);
//					paginaInicialDt.adicionarPendenciasServentia(listaIntimacoesLidas);
//				}
//			}
		}
		
		//Consulta a quantidade de pend�ncias do tipo informativa
		paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));
	}
	
	/**
	 * Consulta dados referentes � pend�ncias de cartas de cita��o
	 * do Escrit�rio Jur�dico
	 * @param usuarioDt
	 * @param idTipo
	 * @param paginaInicialDt
	 * @param pendenciaNe
	 * @author hmgodinho
	 * @author jrcorrea 15/05/2015
	 * @throws Exception 
	 */
	private void configurarPendenciasCitacaoEscritorioJuridicoProcuradoria(UsuarioNe usuarioNe, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{
		List citacoes = new ArrayList();
		List citacoesLidas = new ArrayList();
		if (usuarioNe.getUsuarioDt() != null && !usuarioNe.getUsuarioDt().getId_Serventia().trim().equals("")) {
			// Consulta as cita��es da serventia
			List pendencias = pendenciaNe.consultarCitacoesEscritorioJuridicoProcuradoria(usuarioNe);

			if (pendencias != null) {
				for (int i = 0; i < pendencias.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendencias.get(i);
					citacoes.add(dt);
				}

				if (citacoes.size() > 0) {
					ListaPendenciaDt listaCitacoes = new ListaPendenciaDt();
					listaCitacoes.setTitulo("Cita��es da Serventia Aguardando Leitura");
					listaCitacoes.setIdTipo(String.valueOf(PendenciaTipoDt.CARTA_CITACAO));
					listaCitacoes.setPendenciasAndamento(citacoes);
					paginaInicialDt.adicionarPendenciasServentia(listaCitacoes);
				}
			}			
			
			List pendenciasLidas = pendenciaNe.consultarCitacoesLidasDistribuicaoEscritorioJuridicoProcuradoria(usuarioNe);

			if (pendenciasLidas != null) {
				for (int i = 0; i < pendenciasLidas.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendenciasLidas.get(i);
					citacoesLidas.add(dt);
				}

				if (citacoesLidas.size() > 0) {
					ListaPendenciaDt listaCitacoesLidas = new ListaPendenciaDt();
					listaCitacoesLidas.setTitulo("Cita��es da Serventia Lidas e Dentro do Prazo de Cumprimento");
					listaCitacoesLidas.setIdTipo(String.valueOf(PendenciaTipoDt.CARTA_CITACAO));
					listaCitacoesLidas.setPendenciasAndamento(citacoesLidas);
					paginaInicialDt.adicionarPendenciasServentia(listaCitacoesLidas);
				}
			}
		}
	}
	
	/**
	 * Consulta dados referentes � pend�ncias do distribuidor
	 * 
	 * @param usuarioDt
	 * @param idTipo
	 * @param paginaInicialDt
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void configurarPendenciasDistribuidorGabinete(UsuarioNe usuarioNe, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{
		List pendenciasDistribuidor = new ArrayList();

		if ( usuarioNe.isGabinete()) {
			int quantidadeConclusoes = pendenciaNe.consultarQuantidadeConclusoesGabinete(usuarioNe.getId_Serventia());
			paginaInicialDt.setQtdConclusoesGabinete(quantidadeConclusoes);
		}
		
		if (usuarioNe.getUsuarioDt().getId_ServentiaCargo() != null && !usuarioNe.getUsuarioDt().getId_ServentiaCargo().trim().equals("")) {
			// Consulta as pendencias (serventia cargo) do tipo pedido de vista e relatorio
			List pendencias = pendenciaNe.consultarPendenciasDistribuidorGabinete(usuarioNe);

			//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
			paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioNe.getUsuarioDt()));

			//Consulta a quantidade de pend�ncias do tipo informativa
			paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioNe.getUsuarioDt()));

			if (pendencias != null) {
				for (int i = 0; i < pendencias.size(); i++) {
					PendenciaDt dt = (PendenciaDt) pendencias.get(i);
					pendenciasDistribuidor.add(dt);
				}

				if (pendenciasDistribuidor.size() > 0) {
					ListaPendenciaDt listaPendenciasDistribuidor = new ListaPendenciaDt();
					listaPendenciasDistribuidor.setTitulo("Pedido de Vista/Relat�rio e Revis�o/Voto Aguardando Distribui��o");
					listaPendenciasDistribuidor.setPendenciasAndamento(pendenciasDistribuidor);
					paginaInicialDt.adicionarPendenciasServentiaCargo(listaPendenciasDistribuidor);
				}

			}

			//Consulta as conclus�es que est�o para o distribuidor
			List pendenciasConclusao = pendenciaNe.consultarConclusoesDistribuidorGabinete(usuarioNe);

			if (pendenciasConclusao != null) {
				if (pendenciasConclusao.size() > 0) {
					ListaConclusaoDt listaConclusoes = new ListaConclusaoDt();
					listaConclusoes.setTitulo("Conclus�es Aguardando Distribui��o");
					listaConclusoes.setConclusoesAndamento(pendenciasConclusao);
					paginaInicialDt.adicionarConclusao(listaConclusoes);
				}

			}
		}

	}
	
	/**
	 * Consulta dados referentes � pend�ncias do distribuidor C�mara
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @param pendenciaNe
	 * @throws Exception
	 */
	private void configurarPendenciasDistribuidorCamara(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt, PendenciaNe pendenciaNe) throws Exception{
		SortedMap est = new TreeMap();
		List listaPendenciasNaoAnalisadas = pendenciaNe.consultarPendenciasServentiaDistribuidorCamara(usuarioDt);
		//List listaPendenciasPreAnalisadas = pendenciaNe.consultarPendenciasServentiaPreAnalisadas(usuarioDt);
		List listaPendenciasReservadas = pendenciaNe.consultarPendenciasServentiaReservadasDistribuidorCamara(usuarioDt);

		//n�o analisado
		for (int i = 0; i < listaPendenciasNaoAnalisadas.size(); i++) {
			String[] a = (String[]) listaPendenciasNaoAnalisadas.get(i);

			ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
			listaPendencias.setIdTipo(a[0]);
			listaPendencias.setTitulo(a[1]);
			listaPendencias.setQtdeNaoAnalisadas(Funcoes.StringToInt(a[2]));
			listaPendencias.setQtdePreAnalisadas(0);
			listaPendencias.setQtdeReservadas(0);
			//est.put(listaPendencias.getIdTipo(), listaPendencias);
			est.put(listaPendencias.getTitulo(), listaPendencias);
		}

//		for (int i = 0; i < listaPendenciasPreAnalisadas.size(); i++) {
//			String[] b = (String[]) listaPendenciasPreAnalisadas.get(i);
//			//pegar pr�-analisado
//			if (!est.containsKey(b[1])) {
//				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
//				listaPendencias.setIdTipo(b[0]);
//				listaPendencias.setTitulo(b[1]);
//				listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
//				listaPendencias.setQtdeNaoAnalisadas(0);
//				listaPendencias.setQtdeReservadas(0);
//				est.put(listaPendencias.getTitulo(), listaPendencias);
//			} else {
//				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(b[1]);
//				listaPendencias.setQtdePreAnalisadas(Funcoes.StringToInt(b[2]));
//			}
//		}

		for (int i = 0; i < listaPendenciasReservadas.size(); i++) {
			String[] c = (String[]) listaPendenciasReservadas.get(i);
			//pegar reservadas
			if (!est.containsKey(c[1])) {
				ListaPendenciaDt listaPendencias = new ListaPendenciaDt();
				listaPendencias.setIdTipo(c[0]);
				listaPendencias.setTitulo(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
				listaPendencias.setQtdePreAnalisadas(0);
				listaPendencias.setQtdeNaoAnalisadas(0);
				est.put(listaPendencias.getTitulo(), listaPendencias);
			} else {
				ListaPendenciaDt listaPendencias = (ListaPendenciaDt) est.get(c[1]);
				listaPendencias.setQtdeReservadas(Funcoes.StringToInt(c[2]));
			}
		}

		List lista = new ArrayList();
		lista.addAll(est.values());
		paginaInicialDt.setPendenciasServentia(lista);

	}

	/**
	 * Consulta dados referentes as conclus�es
	 * 
	 * @param usuarioDt usu�rio que est� realizando consulta
	 * @param paginaInicialDt dt com dados a ser mostrados na pagina inicial
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void configurarPendenciasJuizes(UsuarioDt usuarioDt, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{
		//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		if ( usuarioDt.isGabinete()) {
			int quantidadeConclusoes = pendenciaNe.consultarQuantidadeConclusoesGabinete(usuarioDt.getId_Serventia());
			paginaInicialDt.setQtdConclusoesGabinete(quantidadeConclusoes);
		}
		
		if (usuarioDt.isGabineteUPJ()){
			// Consulta a quantidade de pend�ncias com prazo decorrido e a decorrer
			paginaInicialDt.setQtdePrazoDecorrido(pendenciaNe.consultarQtdPrazosDecorridos(usuarioDt));
			
			// Consulta a quantidade de pend�ncias com prazo a decorrer
			paginaInicialDt.setQtdePrazoADecorrer(pendenciaNe.consultarQtdPrazosADecorrer(usuarioDt));
			
			//Consulta a quantidade de pend�ncias expedidas para serventias on-line e que estao aguardando visto
			paginaInicialDt.setQtdExpedidasAguardandoVisto(pendenciaNe.consultarQtdExpedidasAguardandoVisto(usuarioDt));
		}
		
		List listaConclusoesNaoAnalisadas = pendenciaNe.consultarQuantidadeConclusoesNaoAnalisadas(usuarioDt, false, false);

		for (int i = 0; i < listaConclusoesNaoAnalisadas.size(); i++) {
			String[] conclusao = (String[]) listaConclusoesNaoAnalisadas.get(i);

			// Incializa o objeto para a lista de conclus�es
			ListaConclusaoDt objConclusao = new ListaConclusaoDt();
			// Id do tipo da pendencia, para poder montar os links
			objConclusao.setIdTipo(conclusao[0]);
			// Modifica o titulo da lista de conclus�es
			objConclusao.setTitulo(conclusao[1]);
			objConclusao.setQtdeNaoAnalisadas(Funcoes.StringToInt(conclusao[2]));
			// Adiciona a lista
			paginaInicialDt.adicionarConclusao(objConclusao);
		}

		List listaConclusoesPreAnalisadas = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadas(usuarioDt, false, false);

		for (int i = 0; i < listaConclusoesPreAnalisadas.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesPreAnalisadas.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoes().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoes().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				}
			}

			//Se n�o tinha uma conclus�o analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setTitulo(conclusao[1]);
				objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarConclusao(objConclusao);
			}
		}
		
		List listaConclusoesPreAnalisadasPendentesAssinatura = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadasPendentesAssinatura(usuarioDt, false);

		for (int i = 0; i < listaConclusoesPreAnalisadasPendentesAssinatura.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesPreAnalisadasPendentesAssinatura.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoes().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoes().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadasPendentesAssinatura(Funcoes.StringToInt(conclusao[2]));
				}
			}

			//Se n�o tinha uma conclus�o analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setTitulo(conclusao[1]);
				objConclusao.setQtdePreAnalisadasPendentesAssinatura(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarConclusao(objConclusao);
			}
		}

		// Consulta a quantidade de pre-analises multiplas de conclus�es
		paginaInicialDt.setQtdePreAnalisesMultiplasConclusoes(pendenciaNe.consultarQuantidadeConclusoesPreAnalisadasMultipla(usuarioDt));

		// Busca as pend�ncias para ServentiaCargo e UsuarioServentia
		Map mapPendenciasServentiaCargo = null;

		// Se � assistente deve buscar as pend�ncias para o serventia cargo do seu chefe
		if (grupoTipo == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || grupoTipo == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU
				|| grupoTipo == GrupoTipoDt.ASSESSOR_DESEMBARGADOR ) {
			mapPendenciasServentiaCargo = pendenciaNe.consultarTiposServentiaCargoJuiz(usuarioDt.getId_ServentiaCargoUsuarioChefe(), false);
		} else {
			mapPendenciasServentiaCargo = pendenciaNe.consultarTiposServentiaCargoJuiz(usuarioDt.getId_ServentiaCargo(), false);
			//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
			paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioDt));
			//Consulta a quantidade de pend�ncias do tipo informativa
			paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioDt));
		}

		// Monta lista de pend�ncias
		Iterator itPendenciasCargo = mapPendenciasServentiaCargo.keySet().iterator();
		while (itPendenciasCargo.hasNext()) {
			ListaPendenciaDt listaPendenciaDt = new ListaPendenciaDt();
			String chave = (String) itPendenciasCargo.next();
			String idTipo = ((String[]) mapPendenciasServentiaCargo.get(chave))[0];
			int qtde = Funcoes.StringToInt(((String[]) mapPendenciasServentiaCargo.get(chave))[1]);
			listaPendenciaDt.setIdTipo(idTipo);
			listaPendenciaDt.setTitulo(chave);
			listaPendenciaDt.setQuantidadePendencias(qtde);
			paginaInicialDt.adicionarPendenciasServentiaCargo(listaPendenciaDt);
		}
		
		// Busca as pend�ncias para ServentiaCargo e UsuarioServentia, pendente de assinatura
		Map mapPendenciasServentiaCargoPendenteAssinatura = null;
		
		// Se � assistente deve buscar as pend�ncias para o serventia cargo do seu chefe
		if (grupoTipo == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || grupoTipo == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU
				|| grupoTipo == GrupoTipoDt.ASSESSOR_DESEMBARGADOR ) {
			mapPendenciasServentiaCargoPendenteAssinatura = pendenciaNe.consultarTiposServentiaCargoJuiz(usuarioDt.getId_ServentiaCargoUsuarioChefe(), true);
		} else {
			mapPendenciasServentiaCargoPendenteAssinatura = pendenciaNe.consultarTiposServentiaCargoJuiz(usuarioDt.getId_ServentiaCargo(), true);			
		}
		
		// Monta lista de pend�ncias
		Iterator itPendenciasCargoPendenteAssinatura = mapPendenciasServentiaCargoPendenteAssinatura.keySet().iterator();
		while (itPendenciasCargoPendenteAssinatura.hasNext()) {
			boolean boExiste = false;			
			String chave = (String) itPendenciasCargoPendenteAssinatura.next();
			String idTipo = ((String[]) mapPendenciasServentiaCargoPendenteAssinatura.get(chave))[0];
			int qtde = Funcoes.StringToInt(((String[]) mapPendenciasServentiaCargoPendenteAssinatura.get(chave))[1]);
			ListaPendenciaDt listaPendenciaDt = null;

			for (int i = 0; i < paginaInicialDt.getPendenciasServentiaCargo().size(); i++) {
				listaPendenciaDt = (ListaPendenciaDt) paginaInicialDt.getPendenciasServentiaCargo().get(i);
				if (listaPendenciaDt.getIdTipo().equals(idTipo)) {
					boExiste = true;
					listaPendenciaDt.setQtdePreAnalisadasPendentesAssinatura(qtde);
				}
			}

			//Se n�o tinha uma conclus�o analisada antes inicializa o objeto agora
			if (!boExiste) {
				listaPendenciaDt = new ListaPendenciaDt();							
				listaPendenciaDt.setIdTipo(idTipo);
				listaPendenciaDt.setTitulo(chave);
				listaPendenciaDt.setQtdePreAnalisadasPendentesAssinatura(qtde);						
				paginaInicialDt.adicionarPendenciasServentiaCargo(listaPendenciaDt);
			}	
		}
		
		//*******************************CONSULTAS PARA O MANDADO DE PRIS�O*********************************************************//
		// consulta quantidade de mandados de pris�o emitidos, aguardando expedi��o
		int qtde = new MandadoPrisaoNe().consultarQuantidadeMandadoPrisaoEmitidoServentiaCargo(usuarioDt.getId_ServentiaCargo());
		if (qtde > 0){
			ListaPendenciaDt listaPendenciaDt = new ListaPendenciaDt();
			listaPendenciaDt.setTitulo("Mandado de Pris�o para Expedir");
			listaPendenciaDt.setQuantidadePendencias(qtde);
			listaPendenciaDt.setUrlRetorno("MandadoPrisao?PaginaAtual="+Configuracao.Curinga7+"&tempFluxo1=1");
			paginaInicialDt.adicionarPendenciasServentiaCargo(listaPendenciaDt);			
		}

		// consulta quantidade de mandados de pris�o expedidos, aguardando impress�o
		List lista = new ArrayList();
		lista.add(String.valueOf(MandadoPrisaoStatusDt.EXPEDIDO));
		qtde = new MandadoPrisaoNe().consultarQuantidadeMandadoPrisaoServentiaCargo(usuarioDt.getId_ServentiaCargo(), lista);
		if (qtde > 0){
			ListaPendenciaDt listaPendenciaDt = new ListaPendenciaDt();
			listaPendenciaDt.setTitulo("Mandado de Pris�o aguardando Impress�o");
			listaPendenciaDt.setQuantidadePendencias(qtde);
			listaPendenciaDt.setUrlRetorno("MandadoPrisao?PaginaAtual="+Configuracao.Curinga7+"&tempFluxo1=2");
			paginaInicialDt.adicionarPendenciasServentiaCargo(listaPendenciaDt);			
		}
		
		// consulta quantidade de mandados de pris�o impresso, aguardando cumprimento
		lista.add(String.valueOf(MandadoPrisaoStatusDt.IMPRESSO));
		qtde = new MandadoPrisaoNe().consultarQuantidadeMandadoPrisaoServentiaCargo(usuarioDt.getId_ServentiaCargo(), lista);
		if (qtde > 0){
			ListaPendenciaDt listaPendenciaDt = new ListaPendenciaDt();
			listaPendenciaDt.setTitulo("Mandado de Pris�o aguardando Cumprimento");
			listaPendenciaDt.setQuantidadePendencias(qtde);
			listaPendenciaDt.setUrlRetorno("MandadoPrisao?PaginaAtual="+Configuracao.Curinga7+"&tempFluxo1=3");
			paginaInicialDt.adicionarPendenciasServentiaCargo(listaPendenciaDt);			
		}
		//**************************************************************************************************************************//

		//Chama m�todo para configurar outras pend�ncias que poder�o ser analisadas e pr�-analisadas
//		if (grupo == GrupoDt.DESEMBARGADOR ||grupo == GrupoDt.JUIZES_VARA || grupo == GrupoDt.ASSISTENTES_GABINETE 
//				|| grupo == GrupoDt.ASSISTENTES_JUIZES_VARA) {
		if (grupoTipo == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU ||grupoTipo == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU ||grupoTipo == GrupoTipoDt.JUIZ_TURMA || grupoTipo == GrupoTipoDt.ASSISTENTE_GABINETE || grupoTipo == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
				|| grupoTipo == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || grupoTipo == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU || grupoTipo == GrupoTipoDt.ASSESSOR_DESEMBARGADOR) {
			this.configurarOutrasPendenciasAnalise(paginaInicialDt, usuarioDt, pendenciaNe);
		}
	}
	
	/**
	 * Consulta dados referentes as conclus�es
	 * 
	 * @param usuarioDt usu�rio que est� realizando consulta
	 * @param paginaInicialDt dt com dados a ser mostrados na pagina inicial
	 * @param pendenciaNe
	 * @throws Exception 
	 */
	private void configurarPendenciasAssistenteGabinete(UsuarioDt usuarioDt, PaginaInicialDt paginaInicialDt, PendenciaNe pendenciaNe) throws Exception{
		Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		List listaConclusoesNaoAnalisadas = pendenciaNe.consultarQuantidadeConclusoesNaoAnalisadasAssistenteGabinete(usuarioDt, false, false);

		for (int i = 0; i < listaConclusoesNaoAnalisadas.size(); i++) {
			String[] conclusao = (String[]) listaConclusoesNaoAnalisadas.get(i);

			// Incializa o objeto para a lista de conclus�es
			ListaConclusaoDt objConclusao = new ListaConclusaoDt();
			// Id do tipo da pendencia, para poder montar os links
			objConclusao.setIdTipo(conclusao[0]);
			objConclusao.setIdServentiaGrupo(conclusao[2]);
			// Modifica o titulo da lista de conclus�es
			objConclusao.setTitulo(conclusao[1]+" - "+conclusao[3] +" - "+ conclusao[5]);
			objConclusao.setQtdeNaoAnalisadas(Funcoes.StringToInt(conclusao[4]));
			// Adiciona a lista
			paginaInicialDt.adicionarConclusao(objConclusao);
		}

		List listaConclusoesPreAnalisadas = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadasAssistenteGabinete(usuarioDt, false, false);

		for (int i = 0; i < listaConclusoesPreAnalisadas.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesPreAnalisadas.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoes().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoes().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0]) && objConclusao.getIdServentiaGrupo().equals(conclusao[2])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[4]));
				}
			}

			//Se n�o tinha uma conclus�o analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setIdServentiaGrupo(conclusao[2]);
				if (conclusao[3] != null){
					objConclusao.setTitulo(conclusao[1]+" - "+conclusao[3]);
				} else {
					objConclusao.setTitulo(conclusao[1]);
				}
				objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[4]));
				paginaInicialDt.adicionarConclusao(objConclusao);
			}
		}

		// Consulta a quantidade de pre-analises multiplas de conclus�es
		paginaInicialDt.setQtdePreAnalisesMultiplasConclusoes(pendenciaNe.consultarQuantidadeConclusoesPreAnalisadasMultipla(usuarioDt));

		// Busca as pend�ncias para ServentiaCargo e UsuarioServentia
		//Map mapPendenciasServentiaCargo = null;
		//mapPendenciasServentiaCargo = pendenciaNe.consultarTiposServentiaCargoJuiz(usuarioDt.getId_ServentiaCargo());
		
		// Busca as pend�ncias para ServentiaCargo
		//this.consultarPendenciasServentiaCargo(paginaInicialDt, usuarioDt, pendenciaNe);
		
		//Consulta a quantidade de pend�ncias do tipo liberar acesso ao processo
		paginaInicialDt.setQtdePendenciaLiberaAcesso(pendenciaNe.consultarQtdPendenciaLiberaAcesso(usuarioDt));
		//Consulta a quantidade de pend�ncias do tipo informativa
		paginaInicialDt.setQtdePendenciaInformativa(pendenciaNe.consultarQtdPendenciaInformativa(usuarioDt));

		// Monta lista de pend�ncias
//		Iterator itPendenciasCargo = mapPendenciasServentiaCargo.keySet().iterator();
//		while (itPendenciasCargo.hasNext()) {
//			ListaPendenciaDt listaPendenciaDt = new ListaPendenciaDt();
//			String chave = (String) itPendenciasCargo.next();
//			String idTipo = ((String[]) mapPendenciasServentiaCargo.get(chave))[0];
//			int qtde = Funcoes.StringToInt(((String[]) mapPendenciasServentiaCargo.get(chave))[1]);
//			listaPendenciaDt.setIdTipo(idTipo);
//			listaPendenciaDt.setTitulo(chave);
//			listaPendenciaDt.setQuantidadePendencias(qtde);
//			paginaInicialDt.adicionarPendenciasServentiaCargo(listaPendenciaDt);
//		}

		//Chama m�todo para configurar outras pend�ncias que poder�o ser analisadas e pr�-analisadas
//		if (grupoTipo == GrupoTipoDt.DESEMBARGADOR ||grupoTipo == GrupoTipoDt.JUIZ_VARA ||grupoTipo == GrupoTipoDt.JUIZ_TURMA || grupoTipo == GrupoTipoDt.ASSISTENTE_GABINETE
//				|| grupoTipo == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || grupoTipo == GrupoTipoDt.ASSISTENTE_PRESIDENTE_SEGUNDO_GRAU || grupoTipo == GrupoTipoDt.ASSISTENTE_DESEMBARGADOR) {
//			this.configurarOutrasPendenciasAnalise(paginaInicialDt, usuarioDt, pendenciaNe);
//		}
		
// ******* Foi comentado, pois estava duplicado com o m�todo configurarDadosSessao *******		
//		List listaConclusoesSessaoNaoAnalisadas = pendenciaNe.consultarQuantidadeConclusoesNaoAnalisadas(usuarioDt, true, false);
//
//		for (int i = 0; i < listaConclusoesSessaoNaoAnalisadas.size(); i++) {
//			String[] conclusao = (String[]) listaConclusoesSessaoNaoAnalisadas.get(i);			
//			// Incializa o objeto para a lista de conclus�es
//			ListaConclusaoDt objConclusao = new ListaConclusaoDt();
//			// Id do tipo da pendencia, para poder montar os links
//			objConclusao.setIdTipo(conclusao[0]);
//			// Modifica o titulo da lista de conclus�es
//			objConclusao.setTitulo(conclusao[1]);
//			objConclusao.setQtdeNaoAnalisadas(Funcoes.StringToInt(conclusao[2]));
//			// Adiciona a lista
//			paginaInicialDt.adicionarConclusaoSessao(objConclusao);
//		}
//		
//		List listaConclusoesSessaoPreAnalisadas = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadas(usuarioDt, true, false);
//
//		for (int i = 0; i < listaConclusoesSessaoPreAnalisadas.size(); i++) {
//			boolean boExiste = false;
//			String[] conclusao = (String[]) listaConclusoesSessaoPreAnalisadas.get(i);
//
//			for (int j = 0; j < paginaInicialDt.getConclusoesSessao().size(); j++) {
//				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoesSessao().get(j);
//				if (objConclusao.getIdTipo().equals(conclusao[0])) {
//					boExiste = true;
//					objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
//				}
//			}
//
//			//Se n�o tinha uma conclus�o analisada antes inicializa o objeto agora
//			if (!boExiste) {
//				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
//				objConclusao.setIdTipo(conclusao[0]);
//				objConclusao.setTitulo(conclusao[1]);
//				objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
//				paginaInicialDt.adicionarConclusaoSessao(objConclusao);
//			}
//		}
		
		
//		List listaConclusoesVotoVencidoNaoAnalisadas = pendenciaNe.consultarQuantidadeConclusoesNaoAnalisadas(usuarioDt, true, true);
//
//		for (int i = 0; i < listaConclusoesVotoVencidoNaoAnalisadas.size(); i++) {
//			String[] conclusao = (String[]) listaConclusoesVotoVencidoNaoAnalisadas.get(i);			
//			// Incializa o objeto para a lista de conclus�es
//			ListaConclusaoDt objConclusao = new ListaConclusaoDt();
//			// Id do tipo da pendencia, para poder montar os links
//			objConclusao.setIdTipo(conclusao[0]);
//			// Modifica o titulo da lista de conclus�es
//			objConclusao.setTitulo("Voto");
//			objConclusao.setQtdeNaoAnalisadas(Funcoes.StringToInt(conclusao[2]));
//			// Adiciona a lista
//			paginaInicialDt.adicionarConclusaoVotoVencido(objConclusao);
//		}
//		
//		List listaConclusoesVotoVencidoPreAnalisadas = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadas(usuarioDt, true, true);
//
//		for (int i = 0; i < listaConclusoesVotoVencidoPreAnalisadas.size(); i++) {
//			boolean boExiste = false;
//			String[] conclusao = (String[]) listaConclusoesVotoVencidoPreAnalisadas.get(i);
//
//			for (int j = 0; j < paginaInicialDt.getConclusoesVotoVencido().size(); j++) {
//				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoesVotoVencido().get(j);
//				if (objConclusao.getIdTipo().equals(conclusao[0])) {
//					boExiste = true;
//					objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
//				}
//			}
//
//			//Se n�o tinha uma conclus�o analisada antes inicializa o objeto agora
//			if (!boExiste) {
//				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
//				objConclusao.setIdTipo(conclusao[0]);
//				objConclusao.setTitulo("Voto");
//				objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
//				paginaInicialDt.adicionarConclusaoVotoVencido(objConclusao);
//			}
//		}

	}

	/**
	 * M�todo que trata as pend�ncias semelhantes �s conclus�es, ou seja, aquelas que poder�o ser analisadas ou pr�-analisadas.
	 * S�o essas: Pedido de Vista, Relat�rio, Pedido de Manifesta��o Promotor e Advogado....
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @param pendenciaNe
	 * @throws Exception
	 */
	private void configurarOutrasPendenciasAnalise(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt, PendenciaNe pendenciaNe) throws Exception{
		//Consulta outras pend�ncias em aberto com mesmo tratamento de conclus�es (Pedido de Vista, Relat�rio)

		List listaPendenciasNaoAnalisadas = pendenciaNe.consultarQuantidadePendenciasNaoAnalisadas(usuarioDt);

		for (int i = 0; i < listaPendenciasNaoAnalisadas.size(); i++) {
			String[] conclusao = (String[]) listaPendenciasNaoAnalisadas.get(i);

			// Incializa o objeto para a lista de pend�ncias para an�lise
			ListaPendenciaDt objPendencia = new ListaPendenciaDt();
			// Id do tipo da pendencia, para poder montar os links
			objPendencia.setIdTipo(conclusao[0]);
			// Modifica o titulo da lista de
			objPendencia.setTitulo(conclusao[1]);
			objPendencia.setQtdeNaoAnalisadas(Funcoes.StringToInt(conclusao[2]));
			// Adiciona a lista
			paginaInicialDt.adicionarPendenciaAnalise(objPendencia);
		}

		List listaPendenciasPreAnalisadas = pendenciaNe.consultarQuantidadePendenciasPreAnalisadas(usuarioDt);

		for (int i = 0; i < listaPendenciasPreAnalisadas.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaPendenciasPreAnalisadas.get(i);

			for (int j = 0; j < paginaInicialDt.getPendenciasAnalise().size(); j++) {
				ListaPendenciaDt objPendencia = (ListaPendenciaDt) paginaInicialDt.getPendenciasAnalise().get(j);
				if (objPendencia.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objPendencia.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				}
			}

			//Se n�o tinha uma pend�ncia analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaPendenciaDt objPendencia = new ListaPendenciaDt();
				objPendencia.setIdTipo(conclusao[0]);
				objPendencia.setTitulo(conclusao[1]);
				objPendencia.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarPendenciaAnalise(objPendencia);
			}
		}
		
		List listaPendenciasPreAnalisadasPendentesAssinatura = pendenciaNe.consultarQuantidadePendenciasPreAnalisadasPendentesAssinatura(usuarioDt);

		for (int i = 0; i < listaPendenciasPreAnalisadasPendentesAssinatura.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaPendenciasPreAnalisadasPendentesAssinatura.get(i);

			for (int j = 0; j < paginaInicialDt.getPendenciasAnalise().size(); j++) {
				ListaPendenciaDt objPendencia = (ListaPendenciaDt) paginaInicialDt.getPendenciasAnalise().get(j);
				if (objPendencia.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objPendencia.setQtdePreAnalisadasPendentesAssinatura(Funcoes.StringToInt(conclusao[2]));
				}
			}

			//Se n�o tinha uma pend�ncia analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaPendenciaDt objPendencia = new ListaPendenciaDt();
				objPendencia.setIdTipo(conclusao[0]);
				objPendencia.setTitulo(conclusao[1]);
				objPendencia.setQtdePreAnalisadasPendentesAssinatura(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarPendenciaAnalise(objPendencia);
			}
		}

		// Consulta a quantidade de pre-analises multiplas de outras pend�ncias
		paginaInicialDt.setQtdePreAnalisesMultiplasPendencias(pendenciaNe.consultarQuantidadePendenciasPreAnalisadasMultipla(usuarioDt));
		
		
		
	}

	/**
	 * Prepara dados dos processos ativos e adiciona ao dt da PaginaInicial
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @throws Exception 
	 */
	private void configurarDadosProcessos(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt) throws Exception{
		ProcessoNe processoNe = new ProcessoNe();
		int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());

		List listaProcessos = processoNe.consultarQuantidadeAtivos(usuarioDt);
		// Consulta acima retorna uma lista onde cada posi��o � um vetor de String com os dados:
		// [0] Id_ServentiaSubTipo, [1] ServentiaSubTipo, [2] Quantidade
		
		List listaProcessosCalculo = null;
		if (grupoTipo ==  GrupoTipoDt.ANALISTA_EXECUCAO
				|| usuarioDt.getGrupoCodigo().equals(String.valueOf(GrupoDt.JUIZ_EXECUCAO_PENAL))
				|| usuarioDt.getGrupoCodigo().equals(String.valueOf(GrupoDt.TECNICO_EXECUCAO_PENAL))){
			listaProcessosCalculo = processoNe.consultarQuantidadeCalculo(usuarioDt.getId_Serventia());
		}
		
		
		// Para cada subTipo de serventia encontrado monta uma lista com os dados necessarios na pagina inicial
		for (int i = 0; listaProcessos != null && i < listaProcessos.size(); i++) {
			String[] dados = (String[]) listaProcessos.get(i);
			ListaDadosServentiaDt listaDadosServentiaDt = new ListaDadosServentiaDt();
			if (dados[2] != null) listaDadosServentiaDt.setQuantidade(Funcoes.StringToInt(dados[2]));
			if (dados[1] != null) listaDadosServentiaDt.setDescricao(dados[1]);
							
			// Monta link para processos
			switch (grupoTipo) {
				//case GrupoDt.ADVOGADOS:
				case GrupoTipoDt.ADVOGADO:
					listaDadosServentiaDt.setDescricao(dados[1]);
					listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&codigo=" + dados[0] + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
					break;

				//case GrupoDt.AUTORIDADES_POLICIAIS:
				case GrupoTipoDt.AUTORIDADE_POLICIAL:
					listaDadosServentiaDt.setDescricao("Processos Ativos Delegacia");
					listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.Curinga7 + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
					break;

				//case GrupoDt.PROMOTORES:
				case GrupoTipoDt.MP:
					listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + dados[0]);
					//listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_INTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
					break;
				
				case GrupoTipoDt.COORDENADOR_PROMOTORIA:
					listaDadosServentiaDt.setDescricao("Processos da Promotoria");
					listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
					break;
				case GrupoTipoDt.COORDENADOR_PROCURADORIA:
					listaDadosServentiaDt.setDescricao("Processos da Procuradoria");
					listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
					break;
				case GrupoTipoDt.COORDENADOR_DEFENSORIA_PUBLICA:
					listaDadosServentiaDt.setDescricao("Processos da Denfensoria P�blica");
					listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
					break;
				case GrupoTipoDt.COORDENADOR_ESCRITORIO_JURIDICO:
					listaDadosServentiaDt.setDescricao("Processos do Escrit�rio Jur�dico");
					listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
					break;
				case GrupoTipoDt.COORDENADOR_ADVOCACIA_PUBLICA:
					listaDadosServentiaDt.setDescricao("Processos da Advocacia P�blica");
					listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
					break;

				//case GrupoDt.ASSISTENTES_ADVOGADOS_PROMOTORES:
				case GrupoTipoDt.ASSESSOR_MP:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
						listaDadosServentiaDt.setDescricao("Processos Promotor");
						if(listaDadosServentiaDt.getQuantidade() > 0) {
							listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
						} else {
							listaDadosServentiaDt.setLink("#");
						}
					}
					break;
				case GrupoTipoDt.ASSESSOR_ADVOGADO:	
						if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
								listaDadosServentiaDt.setDescricao(dados[1]);
								listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.LocalizarDWR + "&codigo=" + dados[0] + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
						}					
					break;

				//case GrupoDt.ASSISTENTES:
				case GrupoTipoDt.ASSESSOR:
					if (usuarioDt.getGrupoUsuarioChefe() != null && usuarioDt.getGrupoUsuarioChefe().length() > 0) {
						switch (Funcoes.StringToInt(usuarioDt.getGrupoUsuarioChefe())) {
							case GrupoDt.AUTORIDADES_POLICIAIS:
								listaDadosServentiaDt.setDescricao("Processos " + dados[1] + " Delegacia");
								listaDadosServentiaDt.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=" + Configuracao.Curinga7 + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_EXTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
								break;
							default:
								listaDadosServentiaDt.setDescricao("Processos " + dados[1]);
								listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_INTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
								break;

						}
					}
					break;
				case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
					if (usuarioDt.isJuizUPJ()) {
						listaDadosServentiaDt.setDescricao( dados[1] );
						listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_INTERNO +"&ProcessoStatusCodigo=" +  dados[0] + "&Id_Serventia=" + dados[3] );
					}else{ 
						listaDadosServentiaDt.setDescricao( dados[1] );							
						listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_INTERNO +"&ProcessoStatusCodigo=" +  dados[0]);
					}
					break;				
				case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				case GrupoTipoDt.ASSISTENTE_GABINETE:				
					listaDadosServentiaDt.setDescricao( dados[1] );
					listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_INTERNO +"&ProcessoStatusCodigo=" +  dados[0] + "&Id_Serventia=" + dados[3] );
					break;				
				case GrupoTipoDt.ESTAGIARIO:
					listaDadosServentiaDt.setDescricao( dados[1] );
					listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_INTERNO +"&ProcessoStatusCodigo=" +  dados[0]);
					break;
				default:
					if (grupoTipo == GrupoTipoDt.ANALISTA_EXECUCAO){
						listaDadosServentiaDt.setDescricao( dados[1]  + " (Processo Eletr�nico)");
					}else{ 
						listaDadosServentiaDt.setDescricao( dados[1] );							
					}
					
					listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_INTERNO +"&ProcessoStatusCodigo=" +  dados[0]);
					break;
			}
			
			//coloco qual o tipo de status de processo
			listaDadosServentiaDt.setCodigo(Funcoes.StringToLong(dados[0],-1));
			paginaInicialDt.adicionarDadosServentia(listaDadosServentiaDt);
											
		}
		
		ListaDadosServentiaDt listaDadosServentiaDt = processoNe.consultarQuantidadeSigiloso(usuarioDt);
		
		if (listaDadosServentiaDt != null){	
			paginaInicialDt.adicionarDadosServentia(listaDadosServentiaDt);
		}
			
			
		long loQuantidadePrescritos = processoNe.consultarQuantidadePossiveisPrescritos(usuarioDt);
		// Consulta acima retorna uma lista onde cada posi��o � um vetor de String com os dados:
		// [0] Id_ServentiaSubTipo, [1] ServentiaSubTipo, [2] Quantidade
		if (loQuantidadePrescritos >0 ) {																		
			paginaInicialDt.setQuantidadePrescritos(loQuantidadePrescritos);					
			paginaInicialDt.setDescricaoPrescritos("Processos com Poss�vel Prescri��o");
			paginaInicialDt.setLinkPrescritos("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&tipoConsultaProcesso=" +BuscaProcessoDt.CONSULTA_PROCESSOS_PRESCRITOS +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);									
		}
		
		if (usuarioDt.isCriminal()){
			long loQuantidadQuantidadePrisaoForaPrazo = processoNe.consultarQuantidadePrisaoForaPrazo(usuarioDt.getId_Serventia());
			// Consulta acima retorna uma lista onde cada posi��o � um vetor de String com os dados:
			// [0] Id_ServentiaSubTipo, [1] ServentiaSubTipo, [2] Quantidade
			if (loQuantidadQuantidadePrisaoForaPrazo >0 ) {																		
				paginaInicialDt.setQuantidadePrisaoForaPrazo(loQuantidadQuantidadePrisaoForaPrazo);					
				paginaInicialDt.setDescricaoPrisaoForaPrazo("Processos com Poss�vel Pris�o Fora do Prazo");
				paginaInicialDt.setLinkPrisaoForaPrazo("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&tipoConsultaProcesso=" +BuscaProcessoDt.CONSULTA_PROCESSOS_PRISAO_FORA_PRAZO);									
			}
		}

		//processos encaminhados
		long loQuantidadeEncaminhados = (new ProcessoEncaminhamentoNe()).consultarQuantidadeProcessosEncaminhados(usuarioDt.getId_Serventia());
		paginaInicialDt.setQtdEncaminhados(loQuantidadeEncaminhados);
		
		//processos em recursos
		long loQuantidadeRecursos = (new RecursoNe()).consultarQuantidadeRecursos(usuarioDt.getId_Serventia());
		paginaInicialDt.setQtdRecursos(loQuantidadeRecursos);						
		
		if (listaProcessosCalculo != null && listaProcessosCalculo.size() > 0) {
			// Para cada subTipo de serventia encontrado monta uma lista com os dados necessarios na pagina inicial
			for (int i = 0; i < listaProcessosCalculo.size(); i++) {
				String[] dados = (String[]) listaProcessosCalculo.get(i);
				listaDadosServentiaDt = new ListaDadosServentiaDt();
				if (dados[2] != null) listaDadosServentiaDt.setQuantidade(Funcoes.StringToInt(dados[2]));

				// Monta link para processos
				if (grupoTipo ==  GrupoTipoDt.ANALISTA_EXECUCAO
						|| usuarioDt.getGrupoCodigo().equals(String.valueOf(GrupoDt.JUIZ_EXECUCAO_PENAL))
						|| usuarioDt.getGrupoCodigo().equals(String.valueOf(GrupoDt.TECNICO_EXECUCAO_PENAL))){
						listaDadosServentiaDt.setDescricao("Processos de C�lculo de Liquida��o de Pena na Serventia (EXECPENWEB)");
						listaDadosServentiaDt.setLink("BuscaProcesso?PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_INTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.CALCULO);
				}
				paginaInicialDt.adicionarDadosServentia(listaDadosServentiaDt);
			}
		}
		
		// Para o caso de turma recursal deve consultar o quantitativo de recursos para autuar
		switch (grupoTipo) {
			// case GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
			// case GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
			case GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU:
			case GrupoTipoDt.TECNICO_TURMA_SEGUNDO_GRAU:
				RecursoNe recursoNe = new RecursoNe();
				int qtdeRecursosAutuar = recursoNe.consultarQuantidadeRecursosAutuar(usuarioDt.getId_Serventia());
				listaDadosServentiaDt = new ListaDadosServentiaDt();
				listaDadosServentiaDt.setDescricao("Definir P�los");
				listaDadosServentiaDt.setQuantidade(qtdeRecursosAutuar);
				listaDadosServentiaDt.setLink("Recurso?PaginaAtual=" + Configuracao.Localizar);
				paginaInicialDt.adicionarDadosServentia(listaDadosServentiaDt);
				break;
		}
		
		// Para o caso de Ju�zes e Assistentes deve consultar processos espec�ficos dele
		switch (grupoTipo) {
			//case GrupoDt.JUIZES_VARA:
			//case GrupoDt.JUIZES_TURMA_RECURSAL:
			//case GrupoDt.ASSISTENTES_JUIZES_VARA:
			//case GrupoDt.ASSISTENTES_JUIZES_SEGUNDO_GRAU:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
			case GrupoTipoDt.JUIZ_TURMA:
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				int qtdeProcessos = processoNe.consultarQuantidadeAtivosJuiz(usuarioDt);
				listaDadosServentiaDt = new ListaDadosServentiaDt();
				listaDadosServentiaDt.setDescricao("Processos Ativos Magistrado");
				listaDadosServentiaDt.setQuantidade(qtdeProcessos);
				listaDadosServentiaDt.setLink("BuscaProcesso?tipoConsultaProcesso=2&PaginaAtual=" + Configuracao.Localizar + "&PassoBusca=" +BuscaProcessoDt.CONSULTA_USUARIO_INTERNO +"&ProcessoStatusCodigo=" + ProcessoStatusDt.ATIVO);
				paginaInicialDt.adicionarDadosServentia(listaDadosServentiaDt);
				break;
		}
		
		//Para o caso de GRUPO analista criminal 1� e 2� grau
		switch(grupo) {
			//analista
			case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
			//Tecnico
			case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL: {
				
				long qtdeProcessos = processoNe.consultarQuantidadeArquivadosSemMovito(usuarioDt.getId_Serventia());
				paginaInicialDt.setQtdeArquivadosSemMovito(qtdeProcessos);
				
				qtdeProcessos = processoNe.consultarQuantidadeInconsistenciaPoloPassivo(usuarioDt.getId_Serventia());
				paginaInicialDt.setQtdeInconsistenciaPoloPassivo(qtdeProcessos);
				
				qtdeProcessos = processoNe.consultarQuantidadeProcessosSemAssunto(usuarioDt.getId_Serventia());
				paginaInicialDt.setQtdeProcessosSemAssunto(qtdeProcessos);
				
				qtdeProcessos = processoNe.consultarQuantidadeProcessosComAssuntoPai(usuarioDt.getId_Serventia());
				paginaInicialDt.setQtdeProcessosComAssuntoPai(qtdeProcessos);
				
				qtdeProcessos = processoNe.consultarQuantidadeProcessosComClassePai(usuarioDt.getId_Serventia());
				paginaInicialDt.setQtdeProcessosComClassePai(qtdeProcessos);
				
				break;
			}
			
			//analista
			case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
			case GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
			case GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL:
			case GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL:
			case GrupoDt.ANALISTA_JUDICIARIO_PRESIDENCIA:
			//Tecnico
			case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
			case GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
			//JUIZES E ASSESSORES
			case GrupoDt.JUIZES_VARA:
			case GrupoDt.JUIZES_TURMA_RECURSAL:			
			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL:
			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
			case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU:
			case GrupoDt.MAGISTRADO_UPJ_SEGUNDO_GRAU:
			case GrupoDt.ASSISTENTE_GABINETE_FLUXO:
			case GrupoDt.ASSISTENTE_GABINETE:
			case GrupoDt.ASSESSOR_JUIZES_VARA:
			case GrupoDt.ASSESSOR_DESEMBARGADOR:
			case GrupoDt.ASSESSOR:
			case GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU:
			case GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				
			{
				
				long qtdeProcessos = processoNe.consultarQuantidadeProcessosSemAssunto(usuarioDt.getId_Serventia());
				paginaInicialDt.setQtdeProcessosSemAssunto(qtdeProcessos);
				
				qtdeProcessos = processoNe.consultarQuantidadeProcessosComAssuntoPai(usuarioDt.getId_Serventia());
				paginaInicialDt.setQtdeProcessosComAssuntoPai(qtdeProcessos);
				
				qtdeProcessos = processoNe.consultarQuantidadeProcessosComClassePai(usuarioDt.getId_Serventia());
				paginaInicialDt.setQtdeProcessosComClassePai(qtdeProcessos);
				
				break;
			}
			
			
		}
	}

	/**
	 * Prepara dados das Audi�ncias e adiciona ao dt da PaginaInicial
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @throws Exception 
	 * @author hrrosa
	 */
	private void configurarDadosAudiencia(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt) throws Exception{		
		AudienciaNe audienciaNe = new AudienciaNe();
		
		//int grupo = Funcoes.StringToInt(usuarioDt.getGrupoCodigo());
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		
		List audienciasParaHoje = null;

		// Consultar quantidade de Audiencias para Hoje
		switch (grupoTipo) {
			//case GrupoDt.ADVOGADOS:
			case GrupoTipoDt.ADVOGADO:
				audienciasParaHoje = audienciaNe.consultarQuantidadeAudienciasParaHojeAdvogado(usuarioDt, null);
				break;
			//case GrupoDt.CONCILIADORES_VARA:
			//case GrupoDt.JUIZES_VARA:
			case GrupoTipoDt.CONCILIADOR_VARA:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				if (usuarioDt.getId_ServentiaCargo() != null && !usuarioDt.getId_ServentiaCargo().equals("")) {
					audienciasParaHoje = audienciaNe.consultarQuantidadeAudienciasParaHoje(usuarioDt.getId_ServentiaCargo(), null);
				}
				break;
		}
		
		if (audienciasParaHoje != null) {
			for (int i = 0; i < audienciasParaHoje.size(); i++) {
				String[] dados = (String[]) audienciasParaHoje.get(i);
				ListaDadosServentiaDt dadosServentiaDt = new ListaDadosServentiaDt();
				dadosServentiaDt.setDescricao(dados[1] + " para Hoje");
				dadosServentiaDt.setLink("Audiencia?PaginaAtual=" + Configuracao.Localizar + "&AudienciaTipoCodigo=" + dados[0] + "&fluxo=1");
				dadosServentiaDt.setQuantidade(Funcoes.StringToLong(dados[2]));
				paginaInicialDt.adicionarDadosServentia(dadosServentiaDt);
			}
		}

	}
	
	/**
	 * Prepara dados das Centrais de Mandados e adiciona ao dt da PaginaInicial
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @throws Exception 
	 */
	private void configurarDadosCentralMandados(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt) throws Exception{		
		UsuarioServentiaAfastamentoNe usuServAfastamentoNe = new UsuarioServentiaAfastamentoNe();
		
		int grupoTipo = Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo());
		
		String qtdAfastamentosAbertos = null;
		String qtdMandadosReservados = null;

		// Consultar quantidade de Audiencias para Hoje
		switch (grupoTipo) {
			case GrupoTipoDt.COORDENADOR_CENTRAL_MANDADO:
				qtdAfastamentosAbertos = usuServAfastamentoNe.consultarQuantidadeAfastamentosAbertos(usuarioDt.getId_Serventia());
				paginaInicialDt.setQtdAfastamentoOficiaisAbertos(Funcoes.StringToLong(qtdAfastamentosAbertos));
				break;
			case GrupoTipoDt.OFICIAL_JUSTICA:
				paginaInicialDt.setQtdMandadosAbertosReservadosOficial( new MandadoJudicialNe().retornaQtdMandadosAbertosReservadosOficial(usuarioDt.getId_UsuarioServentia()) ); 
				break;

		}

	}
	
	/**
	 * Prepara dados das sess�es e adiciona ao dt da PaginaInicial
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @throws Exception 
	 */
	private void configurarDadosSessao(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt) throws Exception{
		PendenciaNe pendenciaNe = new PendenciaNe();		
		
		configurarDadosSessaoAguardandoAcordao(paginaInicialDt, usuarioDt);
		
		List listaConclusoesSessaoNaoAnalisadas = pendenciaNe.consultarQuantidadeConclusoesNaoAnalisadas(usuarioDt, true, false);

		for (int i = 0; i < listaConclusoesSessaoNaoAnalisadas.size(); i++) {
			String[] conclusao = (String[]) listaConclusoesSessaoNaoAnalisadas.get(i);			
			// Incializa o objeto para a lista de conclus�es
			ListaConclusaoDt objConclusao = new ListaConclusaoDt();
			// Id do tipo da pendencia, para poder montar os links
			objConclusao.setIdTipo(conclusao[0]);
			// Modifica o titulo da lista de conclus�es
			objConclusao.setTitulo(conclusao[1]);
			objConclusao.setQtdeNaoAnalisadas(Funcoes.StringToInt(conclusao[2]));
			paginaInicialDt.setQtdeTotalBoxPresencialSessaoVirtual(objConclusao.getQtdeNaoAnalisadas());
			// Adiciona a lista
			paginaInicialDt.adicionarConclusaoSessao(objConclusao);
		}

		List listaConclusoesSessaoPreAnalisadas = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadas(usuarioDt, true, false);
	
		for (int i = 0; i < listaConclusoesSessaoPreAnalisadas.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesSessaoPreAnalisadas.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoesSessao().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoesSessao().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				}
			}

			//Se n�o tinha uma conclus�o analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setTitulo(conclusao[1]);
				objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarConclusaoSessao(objConclusao);
			}
		}
		
		List listaConclusoesVotoVencidoNaoAnalisadas = pendenciaNe.consultarQuantidadeConclusoesNaoAnalisadas(usuarioDt, true, true);

		for (int i = 0; i < listaConclusoesVotoVencidoNaoAnalisadas.size(); i++) {
			String[] conclusao = (String[]) listaConclusoesVotoVencidoNaoAnalisadas.get(i);			
			// Incializa o objeto para a lista de conclus�es
			ListaConclusaoDt objConclusao = new ListaConclusaoDt();
			// Id do tipo da pendencia, para poder montar os links
			objConclusao.setIdTipo(conclusao[0]);
			// Modifica o titulo da lista de conclus�es
			objConclusao.setTitulo("Voto");
			objConclusao.setQtdeNaoAnalisadas(Funcoes.StringToInt(conclusao[2]));
			// Adiciona a lista
			paginaInicialDt.adicionarConclusaoVotoVencido(objConclusao);
		}
		
		List listaConclusoesVotoVencidoPreAnalisadas = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadas(usuarioDt, true, true);

		for (int i = 0; i < listaConclusoesVotoVencidoPreAnalisadas.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesVotoVencidoPreAnalisadas.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoesVotoVencido().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoesVotoVencido().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				}
			}

			//Se n�o tinha uma conclus�o analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setTitulo("Voto");
				objConclusao.setQtdePreAnalisadas(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarConclusaoVotoVencido(objConclusao);
			}
		}
		
		List listaConclusoesVotoVencidoAguardandoAssinatura = pendenciaNe.consultarQuantidadeConclusoesPreAnalisadasPendentesAssinatura(usuarioDt, true);
		
		for (int i = 0; i < listaConclusoesVotoVencidoAguardandoAssinatura.size(); i++) {
			boolean boExiste = false;
			String[] conclusao = (String[]) listaConclusoesVotoVencidoAguardandoAssinatura.get(i);

			for (int j = 0; j < paginaInicialDt.getConclusoesVotoVencido().size(); j++) {
				ListaConclusaoDt objConclusao = (ListaConclusaoDt) paginaInicialDt.getConclusoesVotoVencido().get(j);
				if (objConclusao.getIdTipo().equals(conclusao[0])) {
					boExiste = true;
					objConclusao.setQtdePreAnalisadasPendentesAssinatura(Funcoes.StringToInt(conclusao[2]));
				}
			}

			//Se n�o tinha uma conclus�o analisada antes inicializa o objeto agora
			if (!boExiste) {
				ListaConclusaoDt objConclusao = new ListaConclusaoDt();
				objConclusao.setIdTipo(conclusao[0]);
				objConclusao.setTitulo("Voto");
				objConclusao.setQtdePreAnalisadasPendentesAssinatura(Funcoes.StringToInt(conclusao[2]));
				paginaInicialDt.adicionarConclusaoVotoVencido(objConclusao);
			}
		}
	}
	
	private void configurarDadosSessaoAguardandoAcordao(PaginaInicialDt paginaInicialDt, UsuarioDt usuarioDt) throws Exception {
		AudienciaNe audienciaNe = new AudienciaNe();
		long qtdeSessoesAguardandoAcordaoPreAnalisados = 0;
		long qtdeSessoesAguardandoAcordaoNaoAnalisados = 0;
		long qtdeSessoesAguardandoAcordaoAguardandoAssinatura = 0;
		
		switch (Funcoes.StringToInt(usuarioDt.getGrupoTipoCodigo())) {

			case GrupoTipoDt.JUIZ_TURMA:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
			case GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA:
			case GrupoTipoDt.ASSISTENTE_GABINETE:
			case GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO:
			case GrupoTipoDt.ASSESSOR_DESEMBARGADOR:
				
				qtdeSessoesAguardandoAcordaoPreAnalisados = audienciaNe.consultarQuantidadeSessoesPendentes(usuarioDt, "", true, false, true);
				qtdeSessoesAguardandoAcordaoNaoAnalisados = audienciaNe.consultarQuantidadeSessoesPendentes(usuarioDt, "", true, false, false);
				qtdeSessoesAguardandoAcordaoAguardandoAssinatura = audienciaNe.consultarQuantidadeSessoesPendentes(usuarioDt, "", true, true, true);

				break;
				
		}

		paginaInicialDt.setQtdeSessoesAguardandoAcordaoNaoAnalisadas(qtdeSessoesAguardandoAcordaoNaoAnalisados);
		paginaInicialDt.setQtdeSessoesAguardandoAcordaoPreAnalisadas(qtdeSessoesAguardandoAcordaoPreAnalisados);
		paginaInicialDt.setQtdeSessoesAguardandoAcordaoAguardandoAssinatura(qtdeSessoesAguardandoAcordaoAguardandoAssinatura);
	}
	
	public PaginaInicialDt listarPendencias(UsuarioNe usuarioNe) throws Exception {
		PaginaInicialDt paginaInicialDt = new PaginaInicialDt();

		// Configura os dados de pendencias
		configurarDadosPendencias(paginaInicialDt, usuarioNe);
		
		// Retorna a pagina inicial configurada
		return paginaInicialDt;
	}
	
	/**
	 * Consulta dados do financeiro para montar a p�gina inicial
	 * 
	 * @param paginaInicialDt
	 * @param usuarioDt
	 * @throws Exception 
	 */
	private void configurarDadosFinanceiro(PaginaInicialDt paginaInicialDt, UsuarioNe usuarioNe) throws Exception{
		//Confirma se usu�rio do grupo financeiro
		if( usuarioNe != null && usuarioNe.getUsuarioDt() != null ) {
			int grupoTipo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo());
			
			if( grupoTipo == GrupoTipoDt.DIRETORIA_FINANCEIRA ) {
				configurarDadosCadastroDeDebitos(paginaInicialDt, usuarioNe);
			
				consultarUltimosArquivos10MinutosRecebidos(paginaInicialDt);
				
				consultarQuantidadeBoletosEmitidosHoje(paginaInicialDt);
				
				consultaLogUltimoBoletoEmitidoHoje(paginaInicialDt);
			}
		}
	}
	
	private void configurarDadosCadastroDeDebitos(PaginaInicialDt paginaInicialDt, UsuarioNe usuarioNe) throws Exception {
//		AudienciaNe audienciaNe = new AudienciaNe();
//		long qtdeDebitosStatusNovo = 0;
//		long qtdeDebitosStatusEmAnalisePeloFinanceiro = 0;
//		long qtdeSessoesAguardandoAcordaoAguardandoAssinatura = 0;
//		
//		switch (Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo())) {
//
//			case GrupoTipoDt.DIRETORIA_FINANCEIRA:				
//				qtdeSessoesAguardandoAcordaoPreAnalisados = audienciaNe.consultarQuantidadeSessoesPendentes(usuarioDt, "", true, false, true);
//				qtdeSessoesAguardandoAcordaoNaoAnalisados = audienciaNe.consultarQuantidadeSessoesPendentes(usuarioDt, "", true, false, false);
//				qtdeSessoesAguardandoAcordaoAguardandoAssinatura = audienciaNe.consultarQuantidadeSessoesPendentes(usuarioDt, "", true, true, true);
//			
//			default:
//								
//		}
//		
//		paginaInicialDt.setQtdeSessoesAguardandoAcordaoNaoAnalisadas(qtdeSessoesAguardandoAcordaoNaoAnalisados);
//		paginaInicialDt.setQtdeSessoesAguardandoAcordaoPreAnalisadas(qtdeSessoesAguardandoAcordaoPreAnalisados);
//		paginaInicialDt.setQtdeSessoesAguardandoAcordaoAguardandoAssinatura(qtdeSessoesAguardandoAcordaoAguardandoAssinatura);
	}
	
	/**
	 * M�todo que consulta as ultimas 5 ocorrencias de arquivos recebido da Caixa economina.
	 * Tamb�m conhecido como arquivos de "Rajada".
	 * 
	 * @param PaginaInicialDt paginaInicialDt
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	private void consultarUltimosArquivos10MinutosRecebidos(PaginaInicialDt paginaInicialDt) throws Exception {
		paginaInicialDt.setListaUltimos5ArquivosRecebidoCaixa10Minutos(new ArquivoBancoNe().consulta5UltimosArquivosProcessados());
	}
	
	/**
	 * M�todo que consulta a quantidade de boletos emitidos no dia de hoje.
	 * 
	 * @param PaginaInicialDt paginaInicialDt
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	private void consultarQuantidadeBoletosEmitidosHoje(PaginaInicialDt paginaInicialDt) throws Exception {
		//paginaInicialDt.setQuantidadeBoletosEmitidosHoje(new GuiaEmissaoNe().consultarQuantidadeBoletosEmitidosHoje());
	}
	
	/**
	 * M�todo que consulta o �ltimo log de boleto emitido hoje.
	 * 
	 * @param PaginaInicialDt paginaInicialDt
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	private void consultaLogUltimoBoletoEmitidoHoje(PaginaInicialDt paginaInicialDt) throws Exception {
		paginaInicialDt.setLogUltimoBoletoEmitidoHoje(new LogNe().consultaLogUltimoBoletoEmitidoHoje());
	}
}
