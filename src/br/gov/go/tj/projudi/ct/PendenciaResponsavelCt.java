package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.ProcessoResponsavelNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla as trocas de Responsáveis por Pendencia.
 * 
 * @author lsbernardes
 * 01/09/2009
 */
public class PendenciaResponsavelCt extends Controle {
	
    private static final long serialVersionUID = 196513009685575040L;

    public int Permissao() {
		return PendenciaResponsavelDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual)
			throws Exception, ServletException, IOException {

		PendenciaResponsavelDt pendenciaResponsavelDt;
		PendenciaDt pendenciaDt;
		PendenciaResponsavelNe pendenciaResponsavelne;
		List<PendenciaDt> listaDePendenciasDt = null;
		String Id_Serventia = "";
		String Serventia = "";
		String processos[] = null;

		//-Variáveis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		//String stNomeBusca2 = "";

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//if(request.getParameter("nomeBusca2") != null) stNomeBusca2 = request.getParameter("nomeBusca2");
		
		//-fim controle de buscas ajax
		
		List tempList = null;
		String Mensagem = "";
		String idPendencia = null;
		String hash = null;
		String idEmenta = null;
		String idNovoServentiaCargo = null;
		String idProcesso = null;
		String stAcao = "/WEB-INF/jsptjgo/PendenciaResponsavel.jsp";
		boolean boDistribuicao = false;
		
		if (UsuarioSessao.isGabinetePresidenciaTjgo() || UsuarioSessao.isGabineteVicePresidenciaTjgo() || UsuarioSessao.isGabineteUpj()){		
			stAcao =  "/WEB-INF/jsptjgo/PendenciaConclusaoDistribuicao.jsp";
			boDistribuicao = true;
		}
		
		request.setAttribute("tempRetorno", "PendenciaResponsavel");
		request.setAttribute("tempPrograma", "PendenciaResponsavel");

		pendenciaResponsavelne = (PendenciaResponsavelNe) request.getSession().getAttribute("PendenciaResponsavelne");
		if (pendenciaResponsavelne == null) pendenciaResponsavelne = new PendenciaResponsavelNe();

		pendenciaResponsavelDt = (PendenciaResponsavelDt) request.getSession().getAttribute("PendenciaResponsaveldt");
		if (pendenciaResponsavelDt == null) pendenciaResponsavelDt = new PendenciaResponsavelDt();
		
		pendenciaDt = (PendenciaDt) request.getSession().getAttribute("Pendenciadt");
		if (pendenciaDt == null) 
			pendenciaDt = new PendenciaDt();
		
		listaDePendenciasDt = (List<PendenciaDt>) request.getSession().getAttribute("ListaDePendenciasDt");
		if (listaDePendenciasDt == null)
			listaDePendenciasDt = new ArrayList<PendenciaDt>();

		pendenciaResponsavelDt.setId_ServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		pendenciaResponsavelDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		pendenciaResponsavelDt.setId_UsuarioResponsavel(request.getParameter("Id_UsuarioServentia"));
		
		pendenciaResponsavelDt.setId_ServentiaGrupo(request.getParameter("Id_ServentiaGrupo"));
		pendenciaResponsavelDt.setServentiaGrupo(request.getParameter("ServentiaGrupo"));
		pendenciaResponsavelDt.setConclusoMagistrado(request.getParameter("chkSubst") != null);
		
		if (request.getParameter("semRegra") != null && request.getParameter("semRegra").length()>0){
			if (request.getParameter("semRegra").equalsIgnoreCase("true")) 
				pendenciaResponsavelDt.setSemRegra(true);
			else if (request.getParameter("semRegra").equalsIgnoreCase("false"))
				pendenciaResponsavelDt.setSemRegra(false);
		}

		pendenciaResponsavelDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		pendenciaResponsavelDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
		
		
		if (request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").trim().equalsIgnoreCase("")){
			Id_Serventia = request.getParameter("Id_Serventia");
		}else{
			if(pendenciaResponsavelDt != null) Id_Serventia = pendenciaResponsavelDt.getId_Serventia();
		}
		if (request.getParameter("Serventia") != null && !request.getParameter("Serventia").trim().equalsIgnoreCase("")) {
			Serventia = request.getParameter("Serventia");
		}else{
			if(pendenciaResponsavelDt != null)Serventia = pendenciaResponsavelDt.getServentia();
		}	
		
		pendenciaResponsavelDt.setServentia(Serventia);
		pendenciaResponsavelDt.setId_Serventia(Id_Serventia);
		
		request.setAttribute("ServentiaCargoUsuario", request.getParameter("ServentiaCargoUsuario"));
		
		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

		//Inicializa Troca de Responsável
		case Configuracao.Novo:
			request.getSession().setAttribute("TrocouResponsavel",null);
			pendenciaResponsavelDt = new PendenciaResponsavelDt();
			
			idPendencia = String.valueOf(request.getParameter("pendencia"));
			hash = String.valueOf(request.getParameter("CodigoPendencia"));
			idEmenta = String.valueOf(request.getParameter("ementa"));
			idNovoServentiaCargo = String.valueOf(request.getParameter("novoServentiaCargo"));
			
			request.getSession().setAttribute("TrocaResponsavelProcesso",null);
			if (request.getParameter("trocaResponsavelProcesso") != null && request.getParameter("trocaResponsavelProcesso").trim().equalsIgnoreCase("true")) {
				request.getSession().setAttribute("TrocaResponsavelProcesso", true);
			}
			
			if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)){
				//Consulta dados da pendencia
				pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, idPendencia);
				
				List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhado(idPendencia, UsuarioSessao.getUsuarioDt().getGrupoCodigo());
				
				if (listaResponsavelPendencia != null && listaResponsavelPendencia.size()>0){

				    PendenciaResponsavelDt pendenciaResponsavelAtualDt = (PendenciaResponsavelDt)listaResponsavelPendencia.get(0);
					pendenciaDt.addResponsavel(pendenciaResponsavelAtualDt);
					pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
					pendenciaResponsavelDt.setId_pendencia_Ementa(idEmenta);
					pendenciaResponsavelDt.setId_ServentiaCargo(idNovoServentiaCargo);
					
					if (pendenciaResponsavelAtualDt.getCargoTipoCodigo() != null && boDistribuicao == false &&
							Funcoes.StringToInt(pendenciaResponsavelAtualDt.getCargoTipoCodigo()) == CargoTipoDt.ASSISTENTE_GABINETE){
						
						if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null 
								&& Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
							request.getSession().setAttribute("TrocouResponsavel", "S");
							request.setAttribute("MensagemErro", "Pendência já foi distribuída.");
						}	
					}
				} else {
					//Consulta dados da pendencia
					List listaResponsavelPendenciaHistorico = pendenciaResponsavelne.consultarResponsaveisDetalhadoHistorico(idPendencia);
					
					if (listaResponsavelPendenciaHistorico != null && listaResponsavelPendenciaHistorico.size()>0){
						stAcao =  "/WEB-INF/jsptjgo/PendenciaConclusaoHistorico.jsp";
					    PendenciaResponsavelDt pendenciaResponsavelAtualDt = (PendenciaResponsavelDt)listaResponsavelPendenciaHistorico.get(0);
						pendenciaDt.addResponsavel(pendenciaResponsavelAtualDt);
						pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
						pendenciaResponsavelDt.setListaHistoricoPendencia(pendenciaResponsavelne.consultarHistoricosPendencia(idPendencia));
						boDistribuicao = false;
						
					} else{
						stAcao =  "/WEB-INF/jsptjgo/Erro.jsp";
						request.setAttribute("MensagemErro", "Não foi possível consultar o histórico da conclusão.");
					}
				
				} 
			}else if (request.getParameter("processos") != null)	{
				listaDePendenciasDt.clear();
				
				processos = request.getParameterValues("processos");
				
				if (processos != null && processos.length > 0)	{
					//Consulta dados básicos de cada processo e adiciona à lista
					for (int i = 0; i < processos.length; i++) 
					{
						if (processos[i] != null && processos[i].trim().length() > 0 && processos[i].contains("-")) 
						{
							String valores[] = (processos[i]).split("-");
							if (valores != null && valores.length >= 3)
							{
								idPendencia = valores[0];
								idProcesso = valores[1];
								hash = valores[2];
								
								if (idProcesso != null && 
									idPendencia != null && 
									hash != null && 
									UsuarioSessao.VerificarCodigoHash(idPendencia, hash)){
									
									//Consulta dados da pendencia
									PendenciaDt pendenciaAtual = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, idPendencia);
									
									if (pendenciaAtual != null && pendenciaAtual.getId_Processo() != null && pendenciaAtual.getId_Processo().trim().equalsIgnoreCase(idProcesso)) 
									{
										List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhado(idPendencia, UsuarioSessao.getUsuarioDt().getGrupoCodigo());
										
										if (listaResponsavelPendencia != null && listaResponsavelPendencia.size()>0){

										    PendenciaResponsavelDt pendenciaResponsavelAtualDt = (PendenciaResponsavelDt)listaResponsavelPendencia.get(0);
										    pendenciaAtual.addResponsavel(pendenciaResponsavelAtualDt);
										    pendenciaResponsavelAtualDt.setId_Pendencia(pendenciaAtual.getId());									    
											
										    if (i == 0) {
										    	pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());	
										    }										    
										    
										    listaDePendenciasDt.add(pendenciaAtual);
										}
									}
								}
							}
						}						
					}
				}else{
					throw new MensagemException("Não foi possível visualizar pendência.");	
				}
			} else{
				throw new MensagemException("Não foi possível visualizar pendência.");
			}
			
			if (boDistribuicao){
				pendenciaResponsavelDt.setListaHistoricoPendencia(pendenciaResponsavelne.consultarHistoricosPendencia(idPendencia));
				if (pendenciaResponsavelDt.getListaHistoricoPendencia() != null && pendenciaResponsavelDt.getListaHistoricoPendencia().size() > 0){
					PendenciaResponsavelHistoricoDt obTempHistorico = (PendenciaResponsavelHistoricoDt) pendenciaResponsavelDt.getListaHistoricoPendencia().get(pendenciaResponsavelDt.getListaHistoricoPendencia().size() - 1);
					ServentiaGrupoDt obTempServentiaGrupo = pendenciaResponsavelne.consultarSeventiaGrupoId(obTempHistorico.getId_ServentiaGrupo());
					pendenciaResponsavelDt.setId_ServentiaGrupo(obTempServentiaGrupo.getId_ServentiaGrupoProximo());
					pendenciaResponsavelDt.setServentiaGrupo(obTempServentiaGrupo.getServentiaGrupoProximo());
					
					if (obTempHistorico.getDataFim() != null && obTempHistorico.getDataFim().length( )== 0){
						pendenciaDt.setId_ServentiaGrupo(obTempHistorico.getId_ServentiaGrupo());
						pendenciaDt.setServentiaGrupo(obTempHistorico.getServentiaGrupo());
					}
					
					if (obTempHistorico.isEnviaMagistrado() && obTempHistorico.getDataFim() != null
							&& obTempHistorico.getDataFim().length( )== 0){
						pendenciaResponsavelDt.setConclusoMagistrado(true);
						pendenciaDt.setEnviaMagistrado(true);
					}
					
				} else {
					ServentiaGrupoDt obTempServentiaGrupo = pendenciaResponsavelne.consultarSeventiaGrupoDistribuidorId(UsuarioSessao.getId_ServentiaCargo());
					if (obTempServentiaGrupo != null && obTempServentiaGrupo.getId() != null && obTempServentiaGrupo.getId().length()>0) {
						pendenciaResponsavelDt.setId_ServentiaGrupo(obTempServentiaGrupo.getId_ServentiaGrupoProximo());
						pendenciaResponsavelDt.setServentiaGrupo(obTempServentiaGrupo.getServentiaGrupoProximo());
						if (obTempServentiaGrupo.isEnviaMagistrado()){
							pendenciaResponsavelDt.setConclusoMagistrado(false);
							pendenciaDt.setEnviaMagistrado(true);
						}
					}
				}
			}
			
			break;

		case Configuracao.Salvar:
			if (boDistribuicao == true)
				request.setAttribute("Mensagem", "Clique para Confirmar a Distribuição da Conclusão");
			else {
				request.setAttribute("Mensagem", "Clique para Confirmar a Troca de Responsável");
				if (String.valueOf(request.getParameter("ProcessoSemAssistente")).equalsIgnoreCase("true")) 
					request.setAttribute("ProcessoSemAssistente", "S");
			}
			break;

		case Configuracao.SalvarResultado:
			
			if (boDistribuicao == true) {
				
				Mensagem = pendenciaResponsavelne.verificarTrocaResponsavelConclusao(pendenciaResponsavelDt);
				
				if (Mensagem.length() == 0) {
					if ( UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())){
						
						if(pendenciaResponsavelne.isReDistribuirConclusaoUnidadeTrabalho(pendenciaDt, UsuarioSessao.getUsuarioDt())){
							
							pendenciaResponsavelne.reDistribuirConclusaoUnidadeTrabalho(pendenciaResponsavelDt,  UsuarioSessao.getUsuarioDt(), pendenciaDt, pendenciaResponsavelDt.getId_ServentiaCargo());
							pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, pendenciaDt.getId());
							pendenciaResponsavelDt.setListaHistoricoPendencia(pendenciaResponsavelne.consultarHistoricosPendencia(pendenciaDt.getId()));
							
							if (pendenciaResponsavelDt.getListaHistoricoPendencia() != null && pendenciaResponsavelDt.getListaHistoricoPendencia().size() > 0){
								PendenciaResponsavelHistoricoDt obTempHistorico = (PendenciaResponsavelHistoricoDt) pendenciaResponsavelDt.getListaHistoricoPendencia().get(pendenciaResponsavelDt.getListaHistoricoPendencia().size() - 1);
								ServentiaGrupoDt obTempServentiaGrupo = pendenciaResponsavelne.consultarSeventiaGrupoId(obTempHistorico.getId_ServentiaGrupo());
								pendenciaResponsavelDt.setId_ServentiaGrupo(obTempServentiaGrupo.getId_ServentiaGrupoProximo());
								pendenciaResponsavelDt.setServentiaGrupo(obTempServentiaGrupo.getServentiaGrupoProximo());
								
								if (obTempHistorico.getDataFim() != null && obTempHistorico.getDataFim().length( )== 0){
									pendenciaDt.setId_ServentiaGrupo(obTempHistorico.getId_ServentiaGrupo());
									pendenciaDt.setServentiaGrupo(obTempHistorico.getServentiaGrupo());
								}
							}
							
						} else{
							pendenciaResponsavelne.distribuirConclusaoUnidadeTrabalho(pendenciaResponsavelDt,  UsuarioSessao.getUsuarioDt(), pendenciaDt.getId_Processo());				
							pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, pendenciaDt.getId());
							pendenciaDt.setHash("");
							request.getSession().setAttribute("DistribuicaoEfetuada", "DE");
						}
						request.setAttribute("MensagemOk", "Distribuição Efetuada com Sucesso.");	
						
						//atualiza objeto da tela
						List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhado(pendenciaDt.getId(), UsuarioSessao.getUsuarioDt().getGrupoCodigo());
						if (listaResponsavelPendencia != null && listaResponsavelPendencia.size()>0){
							pendenciaDt.addResponsavel((PendenciaResponsavelDt)listaResponsavelPendencia.get(0));
							pendenciaResponsavelDt = new PendenciaResponsavelDt();
							pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
							pendenciaResponsavelDt.setListaHistoricoPendencia(pendenciaResponsavelne.consultarHistoricosPendencia(pendenciaDt.getId()));
						}
					} else
						throw new MensagemException("Não foi possível visualizar pendência. executar()");
				} else
					request.setAttribute("MensagemErro", Mensagem);
			} else {
				
				if (listaDePendenciasDt != null && listaDePendenciasDt.size() > 0) {	
					
					ProcessoResponsavelNe ProcessoResponsavelne = new ProcessoResponsavelNe();
					MovimentacaoNe Movimentacaone = new MovimentacaoNe();
					ProcessoNe processoNe = new ProcessoNe();
					Mensagem = "";
					String mensagemParcial = "";
					
					for (PendenciaDt pendenciaAtualDt : listaDePendenciasDt) {
						
						ProcessoDt processoDt = processoNe.consultarId(pendenciaAtualDt.getId_Processo());
						pendenciaResponsavelDt.setId_Pendencia(pendenciaAtualDt.getId());
						// Verifica se a pendencia deverá ficar sem o assistente
						boolean ehSemAssistente = false;
						
						mensagemParcial = pendenciaResponsavelne.verificarTrocaResponsavel(pendenciaResponsavelDt, UsuarioSessao.getUsuarioDt(), ehSemAssistente);
						
						if (mensagemParcial.length() == 0) {
							if ( UsuarioSessao.VerificarCodigoHash(pendenciaAtualDt.getId(), pendenciaAtualDt.getHash())){
								
								PendenciaResponsavelDt pendenciaResponsavelDtAtual = null;
								List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhado(pendenciaResponsavelDt.getId_Pendencia(), UsuarioSessao.getUsuarioDt().getGrupoCodigo(), null);
								if (listaResponsavelPendencia != null && listaDePendenciasDt.size() > 0) {
									for(int i = 0; i < listaDePendenciasDt.size(); i++) {
										PendenciaResponsavelDt temp = (PendenciaResponsavelDt) listaResponsavelPendencia.get(i);
										if (temp.getCargoTipoCodigo() != null && temp.getCargoTipoCodigo().equalsIgnoreCase(String.valueOf(CargoTipoDt.MINISTERIO_PUBLICO))) {
											pendenciaResponsavelDtAtual = temp;
											break;
										}
									}
									
									if (pendenciaResponsavelDtAtual == null) {
										pendenciaResponsavelDtAtual = (PendenciaResponsavelDt)  listaResponsavelPendencia.get(0);
									}
								}								
								
								
//								if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
//									ehSemAssistente = ((request.getParameter("ProcessoSemAssistente") != null && String.valueOf(request.getParameter("ProcessoSemAssistente")).equalsIgnoreCase("true")));							
//								}				
								pendenciaResponsavelne.AtualizarResponsavelPendencia(pendenciaResponsavelDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(),  UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo(), pendenciaAtualDt.getId_Processo(), ehSemAssistente);					
//								if (ehSemAssistente)
//									request.setAttribute("MensagemOk", "Retirada de Assistente Efetuada com Sucesso.");
//								else
//									request.setAttribute("MensagemOk", "Troca de Responsável Efetuada com Sucesso.");
								
//								if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
//									request.getSession().setAttribute("TrocouResponsavel", "S");	
//								}		
								
								if (request.getSession().getAttribute("TrocaResponsavelProcesso") != null &&
								   (boolean) request.getSession().getAttribute("TrocaResponsavelProcesso")) {
									
									List<ServentiaCargoDt> listaPromotoresResponsaveis = ProcessoResponsavelne.consultarResponsavelProcessoPromotores( processoDt.getId());
									
									ServentiaCargoDt serventiaCargoDt = null;
									if (listaPromotoresResponsaveis != null && listaPromotoresResponsaveis.size() > 0) {
										for(ServentiaCargoDt promotor : listaPromotoresResponsaveis) {
											if (pendenciaResponsavelDt.getId_ServentiaCargo().equalsIgnoreCase(promotor.getId())) {
												serventiaCargoDt = promotor;
												break;
											}
										}
										
										if (serventiaCargoDt == null && pendenciaResponsavelDtAtual != null && pendenciaResponsavelDtAtual.getId_ServentiaCargo() != null && pendenciaResponsavelDtAtual.getId_ServentiaCargo().trim().length() > 0) {
											for(ServentiaCargoDt promotor : listaPromotoresResponsaveis) {
												if (pendenciaResponsavelDtAtual.getId_ServentiaCargo().equalsIgnoreCase(promotor.getId())) {
													serventiaCargoDt = promotor;
													break;
												}
											}
										}
										
										if (serventiaCargoDt == null) {
											for(ServentiaCargoDt promotor : listaPromotoresResponsaveis) {
												if (Funcoes.StringToInt(promotor.getCodigoTemp()) == ProcessoResponsavelDt.ATIVO) {
													serventiaCargoDt = promotor;
													break;
												}
											}
										}
									}
																		
									String id_ServentiaCargoAnterior = "";
									if (serventiaCargoDt != null)
									{
										id_ServentiaCargoAnterior = serventiaCargoDt.getId();
									}
									
									if (!id_ServentiaCargoAnterior.equalsIgnoreCase(pendenciaResponsavelDt.getId_ServentiaCargo())) {
										
										List listaProcessos = new ArrayList();
										listaProcessos.add(processoDt);
										
										LogDt log = new LogDt(UsuarioSessao.getId_Usuario(), UsuarioSessao.getIpComputadorLog());
										ProcessoResponsavelne.salvarTrocaResponsavelPromotor(pendenciaResponsavelDt.getId_ServentiaCargo(), id_ServentiaCargoAnterior, pendenciaResponsavelDt.getId_UsuarioResponsavel(), UsuarioSessao.getUsuarioDt().getGrupoCodigo(), listaProcessos, String.valueOf(CargoTipoDt.MINISTERIO_PUBLICO), log);
										
										String nomeResponsavelAnterior = new UsuarioNe().consultarNomeUsuarioServentiaCargo(id_ServentiaCargoAnterior);
										if (nomeResponsavelAnterior == null) nomeResponsavelAnterior = "";
															
										MovimentacaoDt movimentacaoDt = new MovimentacaoDt();
										movimentacaoDt.setId_Processo(processoDt.getId());
										movimentacaoDt.setProcessoNumero(processoDt.getProcessoNumero());
										movimentacaoDt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.TROCAR_RESPONSAVEL_PROCESSO));
										movimentacaoDt.setMovimentacaoTipo("Troca de Responsável");
										movimentacaoDt.setComplemento("MP Responsável Anterior: "+ nomeResponsavelAnterior + " <br> "
												+ "MP Responsável Atual: "+ new UsuarioNe().consultarNomeUsuarioServentiaCargo(pendenciaResponsavelDt.getId_ServentiaCargo()));
										movimentacaoDt.setId_UsuarioRealizador(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
										movimentacaoDt.setId_UsuarioLog(log.getId_Usuario());
										movimentacaoDt.setIpComputadorLog(log.getIpComputador());
										
										Movimentacaone.salvar(movimentacaoDt);
									}
								}
							} else {
								Mensagem += " Processo: " + processoDt.getProcessoNumeroCompleto() + ". " + "Não foi possível visualizar pendência. executar()";
							}								
						} else {
							Mensagem += " Processo: " + processoDt.getProcessoNumeroCompleto() + ". " + mensagemParcial;	
						}
					}
					
					if (Mensagem.length() > 0) {
						request.setAttribute("MensagemErro", Mensagem);
					} else {
						request.getSession().setAttribute("TrocaResponsavelProcesso", null);
						super.redireciona(response, "BuscaProcesso?tipoConsultaProcesso=4&MensagemOk=Troca de Promotor Responsável Efetuada com Sucesso.");
						//request.setAttribute("MensagemOk", "Troca de Promotor Responsável Efetuada com Sucesso.");
					}
					
				} else {
					Mensagem = pendenciaResponsavelne.verificarTrocaResponsavel(pendenciaResponsavelDt, UsuarioSessao.getUsuarioDt(), (String.valueOf(request.getParameter("ProcessoSemAssistente")).equalsIgnoreCase("true")));
					
					if (Mensagem.length() == 0) {
						if ( UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())){
							// Verifica se a pendencia deverá ficar sem o assistente
							boolean ehSemAssistente = false;
							if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
								ehSemAssistente = ((request.getParameter("ProcessoSemAssistente") != null && String.valueOf(request.getParameter("ProcessoSemAssistente")).equalsIgnoreCase("true")));							
							}				
							pendenciaResponsavelne.AtualizarResponsavelPendencia(pendenciaResponsavelDt, UsuarioSessao.getUsuarioDt().getGrupoCodigo(),  UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo(),pendenciaDt.getId_Processo(), ehSemAssistente);					
							if (ehSemAssistente)
								request.setAttribute("MensagemOk", "Retirada de Assistente Efetuada com Sucesso.");
							else
								request.setAttribute("MensagemOk", "Troca de Responsável Efetuada com Sucesso.");	
							if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
								request.getSession().setAttribute("TrocouResponsavel", "S");	
							}					
						} else
							throw new MensagemException("Não foi possível visualizar pendência. executar()");
					} else 
						request.setAttribute("MensagemErro", Mensagem);
					
					//atualiza objeto da tela
					pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, pendenciaDt.getId());
					
					List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhado(pendenciaDt.getId(), UsuarioSessao.getUsuarioDt().getGrupoCodigo());
					
					if (listaResponsavelPendencia != null && listaResponsavelPendencia.size()>0){
						pendenciaDt.addResponsavel((PendenciaResponsavelDt)listaResponsavelPendencia.get(0));
						pendenciaResponsavelDt = new PendenciaResponsavelDt();
						pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
					}
				}	
			}
			
			break;			
		case (Configuracao.Curinga6):
			String id_pendencia= request.getParameter("pendencia");
			String CodigoPendencia= request.getParameter("CodigoPendencia");
			if(id_pendencia==null || !UsuarioSessao.VerificarCodigoHash(id_pendencia, hash)){
					 pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, id_pendencia);
					if(pendenciaDt==null) {
						throw new MensagemException("Não foi possível determinar o conclusão/pendência");
					}
					pendenciaResponsavelne.devolverPendenciaUltimoAssessor(UsuarioSessao, pendenciaDt, CodigoPendencia);
					redireciona(response, "PreAnalisarConclusao?PaginaAtual=" + Configuracao.Curinga6 + "&tipo=" + pendenciaDt.getId_PendenciaTipo() + "&MensagemOk=Conclusão devolvida com sucesso!" );
					return;
			}else {
				throw new MensagemException("Não foi possível identificar a pendência");
			}
						
		case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"ServentiaCargo"};
				String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
				String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
				request.setAttribute("camposHidden",camposHidden);
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
				request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
				request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
				request.setAttribute("tempRetorno", "PendenciaResponsavel");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				request.setAttribute("tempFluxo1", Id_Serventia);
				request.setAttribute("tempFluxo2", Serventia);
			}else{
				String stTemp = "";
//				request.setAttribute("ServentiaTipoCodigo", ServentiaTipoDt.PROMOTORIA);
				String serventiaTipoCodigo = "";
				String serventiaTemp = "";
				 if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.COORDENADOR_PROMOTORIA))){
					 serventiaTipoCodigo = String.valueOf(ServentiaTipoDt.PROMOTORIA);
					 serventiaTemp = request.getParameter("tempFluxo1");
				 } else{					 
					 serventiaTipoCodigo =  UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo();
					 serventiaTemp = UsuarioSessao.getUsuarioDt().getId_Serventia();
				 }
				
				stTemp = pendenciaResponsavelne.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaTemp , serventiaTipoCodigo , UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
					
				enviarJSON(response, stTemp);
					
				
				return;
			}
			break;
			
			
			// Consultar as serventias que utilizam o mesmo tipo da Serventia logada 
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serventia");
					request.setAttribute("tempBuscaPrograma","Consulta de Serventias");			
					request.setAttribute("tempRetorno","PendenciaResponsavel");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
//					ProcessoResponsaveldt.setId_ServentiaCargo("null");
//					ProcessoResponsaveldt.setServentiaCargo("");
//					request.setAttribute("serventiaTipoCodigo", String.valueOf(ServentiaTipoDt.PROMOTORIA));
					
					String serventiaTipoCodigo = "";
					if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.COORDENADOR_PROMOTORIA))){
						 serventiaTipoCodigo = String.valueOf(ServentiaTipoDt.PROMOTORIA);
					} else{					 
						 serventiaTipoCodigo =  UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo();
					}
					
					stTemp = pendenciaResponsavelne.consultarServentiasAtivasJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaTipoCodigo, "");
						
					enviarJSON(response, stTemp);
					
					return;
				}
				break;
			
			
			
		case (ServentiaGrupoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar) :
			if (request.getParameter("Passo")==null){
				//lisNomeBusca = new ArrayList();
				//lisDescricao = new ArrayList();
				String[] lisNomeBusca = {"Serventia Grupo"};
				String[] lisDescricao = {"Serventia Grupo (Unidade de Trabalho)"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_ServentiaGrupo");
				request.setAttribute("tempBuscaDescricao","ServentiaGrupo");
				request.setAttribute("tempBuscaPrograma","Serventia Grupo (Unidade de Trabalho)");
				request.setAttribute("tempRetorno", "PendenciaResponsavel");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Localizar);
				request.setAttribute("PaginaAtual", (ServentiaGrupoDt.CodigoPermissao   * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				break;
			} else {
				String stTemp = "";				
				
				if(pendenciaResponsavelne.isReDistribuirConclusaoUnidadeTrabalho(pendenciaDt, UsuarioSessao.getUsuarioDt())){
					if (pendenciaResponsavelDt.isSemRegra())
						stTemp = pendenciaResponsavelne.consultarServentiaGrupoIdCargo(pendenciaResponsavelDt.getId_ServentiaCargo(), stNomeBusca1, PosicaoPaginaAtual);
					else {
						pendenciaResponsavelDt.setId_ServentiaCargo("");
						pendenciaResponsavelDt.setServentiaCargo("");
					
						stTemp = pendenciaResponsavelne.consultarDescricaoServentiaGrupoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual,  UsuarioSessao.getUsuarioDt().getId_Serventia());
					}
				} else	{				
					stTemp = pendenciaResponsavelne.consultarDescricaoServentiaGrupoJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia(), pendenciaDt.getId_ServentiaGrupo());
				}
											
				enviarJSON(response, stTemp);				
				
				return;
			}
		
		case Configuracao.Curinga9:
			idPendencia = String.valueOf(request.getParameter("pendencia"));
			hash = String.valueOf(request.getParameter("CodigoPendencia"));
			if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)){
				//Consulta dados da pendencia
				pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, idPendencia);
				List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhadoHistorico(idPendencia);
				
				if (listaResponsavelPendencia != null && listaResponsavelPendencia.size()>0){
					stAcao =  "/WEB-INF/jsptjgo/PendenciaConclusaoHistorico.jsp";
				    PendenciaResponsavelDt pendenciaResponsavelAtualDt = (PendenciaResponsavelDt)listaResponsavelPendencia.get(0);
					pendenciaDt.addResponsavel(pendenciaResponsavelAtualDt);
					pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
					pendenciaResponsavelDt.setListaHistoricoPendencia(pendenciaResponsavelne.consultarHistoricosPendencia(idPendencia));
					
				} else{
					stAcao =  "/WEB-INF/jsptjgo/Erro.jsp";
					request.setAttribute("MensagemErro", "Não foi possível consultar o histórico da conclusão.");
				}
			}
			break;

		default:
			break;
		}
		
		if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
			request.setAttribute("EhParaExibirSemAssistente", "S");
			
		}
		
		request.setAttribute("conclusoMagistrado", String.valueOf(pendenciaResponsavelDt.isConclusoMagistrado()));
		request.setAttribute("semRegra", String.valueOf(pendenciaResponsavelDt.isSemRegra()));
		request.getSession().setAttribute("Pendenciadt", pendenciaDt);
		request.getSession().setAttribute("ListaDePendenciasDt", listaDePendenciasDt);		
		request.getSession().setAttribute("PendenciaResponsaveldt", pendenciaResponsavelDt);
		request.getSession().setAttribute("PendenciaResponsavelne", pendenciaResponsavelne);
		request.setAttribute("Id_Serventia",Id_Serventia);
		request.setAttribute("Serventia",Serventia);
		

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
