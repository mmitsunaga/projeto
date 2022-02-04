package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
import br.gov.go.tj.projudi.ne.ServentiaCargoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla as trocas de Responsáveis por Pendencia.
 * 
 * @author lsbernardes
 * 18/03/2011
 */
public class PendenciaServentiaCargoResponsavelCt extends Controle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6612779659847261085L;

    public int Permissao(){
		return PendenciaResponsavelDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual)	throws MensagemException, Exception, ServletException, IOException {

		PendenciaResponsavelDt pendenciaResponsavelDt;
		PendenciaDt pendenciaDt;
		ServentiaDt serventiaDt;
		ServentiaCargoDt serventiaCargoDt;
		PendenciaResponsavelNe pendenciaResponsavelne;
		
		List tempList = null;		
		String Mensagem = "";		
		String id_UsuarioServentiaCargoAtual = null;
		String assistenteGabinete = null;
		String stNomeBusca1 = "";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		String stAcao = "/WEB-INF/jsptjgo/PendenciaServentiaCargoResponsavel.jsp";
		request.setAttribute("tempRetorno", "PendenciaServentiaCargoResponsavel");
		request.setAttribute("tempPrograma", "PendenciaServentiaCargoResponsavel");

		pendenciaResponsavelne = (PendenciaResponsavelNe) request.getSession().getAttribute("PendenciaResponsavelne");
		if (pendenciaResponsavelne == null) pendenciaResponsavelne = new PendenciaResponsavelNe();

		pendenciaResponsavelDt = (PendenciaResponsavelDt) request.getSession().getAttribute("PendenciaResponsaveldt");
		if (pendenciaResponsavelDt == null) pendenciaResponsavelDt = new PendenciaResponsavelDt();
		
		pendenciaDt = (PendenciaDt) request.getSession().getAttribute("Pendenciadt");
		if (pendenciaDt == null) 
			pendenciaDt = new PendenciaDt();
		
		if (request.getSession().getAttribute("Serventiadt") != null)
			serventiaDt = (ServentiaDt)request.getSession().getAttribute("Serventiadt");
		else
			serventiaDt = new ServentiaDt();
		
		if (request.getSession().getAttribute("ServentiaCargodt") != null)
			serventiaCargoDt = (ServentiaCargoDt)request.getSession().getAttribute("ServentiaCargodt");
		else
			serventiaCargoDt = new ServentiaCargoDt();
		
		//responsável que será substituido
		if (request.getParameter("id_UsuarioServentiaCargo") != null && !request.getParameter("id_UsuarioServentiaCargo").equals("")) {
			id_UsuarioServentiaCargoAtual = request.getParameter("id_UsuarioServentiaCargo");
			request.getSession().setAttribute("id_UsuarioServentiaCargoAtural", id_UsuarioServentiaCargoAtual);
			
		} else if (request.getSession().getAttribute("id_UsuarioServentiaCargoAtural") != null ){
			id_UsuarioServentiaCargoAtual = request.getSession().getAttribute("id_UsuarioServentiaCargoAtural").toString();
		}
		
		//assistente gabinete
		if (request.getParameter("assistenteGab") != null && !request.getParameter("assistenteGab").equals("")) {
			assistenteGabinete = request.getParameter("assistenteGab");
			request.getSession().setAttribute("assistenteGab", assistenteGabinete);
			
		} else if (request.getSession().getAttribute("assistenteGab") != null ){
			assistenteGabinete = request.getSession().getAttribute("assistenteGab").toString();
		}
		
		boolean permiteTrocarServentia = true;
		if (request.getSession().getAttribute("PermiteTrocarDeServentia") != null)
			permiteTrocarServentia = (boolean)request.getSession().getAttribute("PermiteTrocarDeServentia");
				
		if (request.getParameter("Id_Serventia") != null) {
			if (request.getParameter("Id_Serventia").equalsIgnoreCase("null")){
				serventiaDt.setId("");
				serventiaDt.setServentia("");
			} else {
				serventiaDt.setId( request.getParameter("Id_Serventia") );
				serventiaDt.setServentia(request.getParameter("Serventia"));
			}
		} else if (!permiteTrocarServentia && id_UsuarioServentiaCargoAtual != null && id_UsuarioServentiaCargoAtual.trim().length() > 0) {
			ServentiaCargoDt serventiaCargoDtAtual = (new ServentiaCargoNe()).consultarId(id_UsuarioServentiaCargoAtual);
			if (serventiaCargoDtAtual != null) {
				serventiaDt.setId(serventiaCargoDtAtual.getId_Serventia());
				serventiaDt.setServentia(serventiaCargoDtAtual.getServentia());
			}
		}
		
		if (request.getParameter("Id_ServentiaCargo") != null)
			if (request.getParameter("Id_ServentiaCargo").equalsIgnoreCase("null")){
				serventiaCargoDt.setId( "" );
				serventiaCargoDt.setServentiaCargo("");
			} else {
				serventiaCargoDt.setId( request.getParameter("Id_ServentiaCargo") );
				serventiaCargoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
			}
		
		// Verifica a opção que o usuário informou em substituição
		if (request.getParameter("novoResponsavel") != null) {
			pendenciaResponsavelDt.setNovoResponsavel(request.getParameter("novoResponsavel"));	
		}

		pendenciaResponsavelDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		pendenciaResponsavelDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

		//Inicializa Troca de Responsável
		case Configuracao.Novo:
			
			request.getSession().setAttribute("TrocouResponsavel",null);
			pendenciaResponsavelDt = new PendenciaResponsavelDt();
			
			String idPendencia = String.valueOf(request.getParameter("pendencia"));
			String hash = String.valueOf(request.getParameter("CodigoPendencia"));
			if (!pendenciaDt.isTemId()) {
				if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)){
					//Consulta dados da pendencia
					pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, idPendencia);
				}else {			
					throw new MensagemException("Não foi possível visualizar pendência. executar()");			
				}
			}
							
			List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhadoTrocaResponsavelConclusao(pendenciaDt.getId(), UsuarioSessao.getUsuarioDt().getGrupoCodigo());
			
			if (request.getParameter("assistenteGab") != null 	&& request.getParameter("assistenteGab").toString().equalsIgnoreCase("true")){
				serventiaDt.setId( UsuarioSessao.getUsuarioDt().getId_Serventia() );
				serventiaDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
			}		
			
			if (pendenciaDt != null && (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VOTO_SESSAO || 
										pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_EMENTA ||
										pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CONCLUSO_VOTO)) {
				permiteTrocarServentia = false;
			} else {
				permiteTrocarServentia = true;
			}				
			
			//Teste para verificar se o distribuidor pode trocar o responsável pela pendência
			if(UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.DISTRIBUIDOR_GABINETE){
				if (pendenciaResponsavelne.isPendenciaResponsavelMesmaServentia(pendenciaDt.getId(), UsuarioSessao.getUsuarioDt().getId_Serventia())){
					if (listaResponsavelPendencia != null && listaResponsavelPendencia.size() > 0){
						pendenciaDt.limparListaResponsaveis();
						for(int k=0;  k<listaResponsavelPendencia.size(); k++) { 
							PendenciaResponsavelDt responsavel = (PendenciaResponsavelDt) listaResponsavelPendencia.get(k);
							if (responsavel != null && responsavel.getId_ServentiaCargo() != null && !responsavel.getId_ServentiaCargo().equals("")){
								pendenciaDt.addResponsavel(responsavel);
								pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
							}
						}
						
						serventiaDt = new ServentiaDt();
						serventiaDt.setId(UsuarioSessao.getUsuarioDt().getId_Serventia());
						serventiaDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
					
					} else {
						request.setAttribute("MensagemErro", "Não é possivel trocar responsável da pendência.");
					}
				
				} else {
					//request.setAttribute("MensagemErro", "Não é possivel trocar responsável da conclusão, pois o Desembargador responsável não pertence ao seu gabinete.");
					// Modifica o fluxo para o ct de busca processo
					redireciona(response, "PendenciaServentiaCargoResponsavel?PaginaAtual=1&&MensagemErro=Não é possivel trocar responsável da "+pendenciaDt.getPendenciaTipo()+", pois o Desembargador responsável não pertence ao seu gabinete.");
					return;
				}
								
			} else {
				if (listaResponsavelPendencia != null && listaResponsavelPendencia.size() > 0){
					String id_serventiaCargoPrimeiroResponsavel = "";
					pendenciaDt.limparListaResponsaveis();
					for(int k=0;  k<listaResponsavelPendencia.size(); k++) { 									
						PendenciaResponsavelDt responsavel = (PendenciaResponsavelDt) listaResponsavelPendencia.get(k);
						if (responsavel != null && responsavel.getId_ServentiaCargo() != null && !responsavel.getId_ServentiaCargo().equals("")){
							pendenciaDt.addResponsavel(responsavel);
							pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
							if (id_serventiaCargoPrimeiroResponsavel.trim().length() == 0 && responsavel.getId_ServentiaCargo() != null && responsavel.getId_ServentiaCargo().trim().length() > 0) {
								id_serventiaCargoPrimeiroResponsavel = responsavel.getId_ServentiaCargo();
							}
						}
					}
					
					if(UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU) {
						serventiaDt = new ServentiaDt();
						if (id_serventiaCargoPrimeiroResponsavel != null &&
							id_serventiaCargoPrimeiroResponsavel.trim().length() > 0) {							
							ServentiaCargoDt serventiaCargoResponsavel = pendenciaResponsavelne.consultarServentiaCargo(id_serventiaCargoPrimeiroResponsavel);							
							if (serventiaCargoResponsavel != null) {
								serventiaDt.setId(serventiaCargoResponsavel.getId_Serventia());
								serventiaDt.setServentia(serventiaCargoResponsavel.getServentia());
							}
						}
					}		
				
				} else {
					request.setAttribute("MensagemErro", "Não é possivel trocar responsável da pendência.");
				}
			}	
			break;
		case Configuracao.Excluir:
			request.setAttribute("Mensagem", "Deseja realmente remover o reponsável pela pendencia?");
			String id_pendenciaresponsavel = request.getParameter("id_pend_resp");
			if(id_pendenciaresponsavel!=null && !id_pendenciaresponsavel.isEmpty()) {
				request.getSession().setAttribute("id_pend_resp_excluir", id_pendenciaresponsavel);
			}else {
				throw new MensagemException("Não foi possível defirnir o responsável a ser removido");
			}
			break;
		case Configuracao.ExcluirResultado:
			String id_pr = (String) request.getSession().getAttribute("id_pend_resp_excluir");
			if(id_pr!=null && !id_pr.isEmpty()) {
				PendenciaResponsavelDt pendResp = pendenciaResponsavelne.consultarId(id_pr);
				if (pendResp!=null) {
					pendResp.setIpComputadorLog(this.getIpCliente(request));
					pendResp.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
					pendenciaResponsavelne.excluir(pendResp);	
					pendenciaDt.limparListaResponsaveis();
				}
				 request.getSession().removeAttribute("id_pend_resp_excluir");
				redireciona(response, "PendenciaServentiaCargoResponsavel?PaginaAtual="+Configuracao.Novo+"&Mensagem=Resposável Removido da Pendência.");
				return;			
			}
			break;
		
		case ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar://LOCALIZAR SERVENTIA
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia","Estado"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId", "Id_Serventia");
				request.setAttribute("tempBuscaDescricao", "Serventia");
				request.setAttribute("tempBuscaPrograma", "Serventia");
				request.setAttribute("tempRetorno", "PendenciaServentiaCargoResponsavel");
				request.setAttribute("tempDescricaoId", "Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			}else{
				String stTemp = "";
									
				stTemp = pendenciaResponsavelne.consultarDescricaoServentiaJSON(stNomeBusca1, UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
					
				enviarJSON(response, stTemp);
				return;
				
			}
			break;
			
		case ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar://LOCALIZAR SERVENTIA CARGO
			String stMensagemErro = "";
		
			if (serventiaDt.getId() != null && !serventiaDt.getId().equals("")){
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia Cargo"};
					String[] lisDescricao = {"Serventia Cargo","Tipo Cargo","Nome Usuário","Serventia"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "Serventia Cargo");
					request.getSession().setAttribute("ultimaAba", 2);
					request.setAttribute("id_Serventia", serventiaDt.getId());
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp = "";
					stTemp = pendenciaResponsavelne.consultarDescricaoServentiaCargoPorServentiaJSON(stNomeBusca1, serventiaDt.getId(), PosicaoPaginaAtual);
					
					enviarJSON(response, stTemp);
											
					return;
				}
				
			} else {
				stMensagemErro = "Selecione uma serventia para selecionar o Cargo da serventia";
			}
			
			if (!stMensagemErro.equals("")) {
				request.setAttribute("MensagemErro", stMensagemErro);
			}
			break;

		case Configuracao.Salvar:
			request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Responsável da pendência");
			break;

		case Configuracao.SalvarResultado:
			pendenciaResponsavelDt.setId_ServentiaCargo(serventiaCargoDt.getId());
			if (id_UsuarioServentiaCargoAtual==null) {
				//se não informar o carto atual será um novo responsável
				pendenciaResponsavelDt.setNovoResponsavel(true);
			}
			Mensagem = pendenciaResponsavelne.verificarTrocaResponsavelConclusao(pendenciaResponsavelDt, pendenciaDt, assistenteGabinete, id_UsuarioServentiaCargoAtual, UsuarioSessao.getUsuarioDt());
			
			if (Mensagem.length() == 0) {
				if ( UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())){
					pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
					pendenciaResponsavelne.salvarTrocaResponsavelPendenciaConclusao(pendenciaResponsavelDt, id_UsuarioServentiaCargoAtual, pendenciaDt.getId_Processo());
					
					
					if(pendenciaResponsavelDt.isNovoResponsavel() || id_UsuarioServentiaCargoAtual==null) {
						request.setAttribute("MensagemOk", "Novo responsável inserido com Sucesso.");
					}else {
						request.setAttribute("MensagemOk", "Troca de Responsável Efetuada com Sucesso.");
					}
					
					pendenciaResponsavelDt = new PendenciaResponsavelDt();
					//atualiza objeto da tela
					pendenciaDt = pendenciaResponsavelne.consultaSimplesId(UsuarioSessao, pendenciaDt.getId());
					
					//List listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhado(pendenciaDt.getId(), UsuarioSessao.getUsuarioDt().getGrupoCodigo());
					listaResponsavelPendencia = pendenciaResponsavelne.consultarResponsaveisDetalhadoTrocaResponsavelConclusao(pendenciaDt.getId(), UsuarioSessao.getUsuarioDt().getGrupoCodigo());
					
					String id_serventiaCargoPrimeiroResponsavel = "";
					
					if (listaResponsavelPendencia != null && listaResponsavelPendencia.size() > 0){
						for (Iterator iterator = listaResponsavelPendencia.iterator(); iterator.hasNext();) {
							PendenciaResponsavelDt responsavel = (PendenciaResponsavelDt) iterator.next();
							if (responsavel != null && responsavel.getId_ServentiaCargo() != null && !responsavel.getId_ServentiaCargo().equals("")){
								pendenciaDt.addResponsavel(responsavel);
								pendenciaResponsavelDt.setId_Pendencia(pendenciaDt.getId());
								
								if (id_serventiaCargoPrimeiroResponsavel.trim().length() == 0 && responsavel.getId_ServentiaCargo() != null && responsavel.getId_ServentiaCargo().trim().length() > 0) {
									id_serventiaCargoPrimeiroResponsavel = responsavel.getId_ServentiaCargo();
								}
							}
						}
					}
					
					request.getSession().removeAttribute("id_UsuarioServentiaCargoAtural");
					serventiaCargoDt.limpar();
					
					serventiaDt = new ServentiaDt();
					
					if(UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.DISTRIBUIDOR_GABINETE){
						serventiaDt.setId(UsuarioSessao.getUsuarioDt().getId_Serventia());
						serventiaDt.setServentia(UsuarioSessao.getUsuarioDt().getServentia());
					} else if(UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU &&
							  id_serventiaCargoPrimeiroResponsavel != null &&
							  id_serventiaCargoPrimeiroResponsavel.trim().length() > 0) {
							ServentiaCargoDt serventiaCargoResponsavel = pendenciaResponsavelne.consultarServentiaCargo(id_serventiaCargoPrimeiroResponsavel);							
							if (serventiaCargoResponsavel != null) {
								serventiaDt.setId(serventiaCargoResponsavel.getId_Serventia());
								serventiaDt.setServentia(serventiaCargoResponsavel.getServentia());
							}		
					} else {
						serventiaDt.limpar();
					}
					
				} else
					throw new MensagemException("Não foi possível visualizar pendência. executar()");
			} else 
				request.setAttribute("MensagemErro", Mensagem);
			break;
		
		default:
			break;
		}
		
		if (serventiaDt != null){
			request.setAttribute("Id_Serventia", serventiaDt.getId() );
			request.setAttribute("Serventia", serventiaDt.getServentia() );
		}
		
		if (serventiaCargoDt != null){
			request.setAttribute("Id_ServentiaCargo", serventiaCargoDt.getId() );
			request.setAttribute("ServentiaCargo", serventiaCargoDt.getServentiaCargo() );
		}

		request.setAttribute("novoResponsavel", String.valueOf(pendenciaResponsavelDt.isNovoResponsavel()));
		request.getSession().setAttribute("Pendenciadt", pendenciaDt);
		request.getSession().setAttribute("PendenciaResponsaveldt", pendenciaResponsavelDt);
		request.getSession().setAttribute("Serventiadt", serventiaDt);
		request.getSession().setAttribute("PendenciaResponsavelne", pendenciaResponsavelne);		
		request.getSession().setAttribute("PermiteTrocarDeServentia", permiteTrocarServentia);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	private void iniciarTroca(HttpServletRequest request,  HttpServletResponse response, UsuarioNe UsuarioSessao, PendenciaDt pendenciaDt, ServentiaDt serventiaDt, PendenciaResponsavelNe pendenciaResponsavelne, boolean permiteTrocarServentia, PendenciaResponsavelDt pendenciaResponsavelDt ) throws Exception {

		
	}

}
