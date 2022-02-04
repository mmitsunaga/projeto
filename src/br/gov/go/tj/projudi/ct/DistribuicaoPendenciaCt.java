package br.gov.go.tj.projudi.ct;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelHistoricoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaGrupoDt;
import br.gov.go.tj.projudi.ne.PendenciaResponsavelNe;
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
public class DistribuicaoPendenciaCt extends Controle {
	
    private static final long serialVersionUID = 196513009685575040L;

    public int Permissao() {
		return PendenciaResponsavelDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual)
			throws Exception, ServletException, IOException {

		PendenciaResponsavelDt pendenciaResponsavelDt;
		PendenciaResponsavelNe pendenciaResponsavelne;
		String Id_Serventia = "";
		String Serventia = "";

		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) {
			stNomeBusca1 = request.getParameter("nomeBusca1");
		}
		
		String Mensagem = "";
		String idPendencia = null;
		String hash = null;
		String stAcao =  "/WEB-INF/jsptjgo/PendenciaConclusaoDistribuicaoUPJ.jsp";
		
		request.setAttribute("tempRetorno", "DistribuicaoPendencia");
		request.setAttribute("tempPrograma", "DistribuicaoPendencia");

		pendenciaResponsavelne = (PendenciaResponsavelNe) request.getSession().getAttribute("PendenciaResponsavelne");
		if (pendenciaResponsavelne == null) pendenciaResponsavelne = new PendenciaResponsavelNe();

		pendenciaResponsavelDt = (PendenciaResponsavelDt) request.getSession().getAttribute("PendenciaResponsaveldt");
		if (pendenciaResponsavelDt == null) pendenciaResponsavelDt = new PendenciaResponsavelDt();
		
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
			
			String id_Arquivo = String.valueOf(request.getParameter("Id_Arquivo"));
			idPendencia = String.valueOf(request.getParameter("pendencia"));
			hash = String.valueOf(request.getParameter("CodigoPendencia"));

			if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)){
				//implementar lógica para distribuição pre-analise simples
				
			} else if(id_Arquivo != null && id_Arquivo.length()>0){
				//Se um arquivo de pré-análise foi passado, verificar quais pendências ele está vinculado. Busca pendências vinculadas ao arquivo
				pendenciaResponsavelDt.setlistaPendenciaDt(pendenciaResponsavelne.consultarPendencias(id_Arquivo));
				
			}  else  {
				throw new MensagemException("Não foi possível visualizar pendência.");
			}
			
			if (pendenciaResponsavelDt.getlistaPendenciaDt() != null && pendenciaResponsavelDt.getlistaPendenciaDt().size()>0){
				PendenciaDt pendenciaDt = (PendenciaDt)  pendenciaResponsavelDt.getlistaPendenciaDt().get(0);
				 
				if (pendenciaDt.getListaHistoricoPendencia() != null && pendenciaDt.getListaHistoricoPendencia().size() > 0){
					PendenciaResponsavelHistoricoDt obTempHistorico = (PendenciaResponsavelHistoricoDt) pendenciaDt.getListaHistoricoPendencia().get(pendenciaDt.getListaHistoricoPendencia().size() - 1);
					ServentiaGrupoDt obTempServentiaGrupo = pendenciaResponsavelne.consultarSeventiaGrupoId(obTempHistorico.getId_ServentiaGrupo());
					pendenciaResponsavelDt.setId_ServentiaGrupo(obTempServentiaGrupo.getId_ServentiaGrupoProximo());
					pendenciaResponsavelDt.setServentiaGrupo(obTempServentiaGrupo.getServentiaGrupoProximo());
					
					if (obTempHistorico.getDataFim() != null && obTempHistorico.getDataFim().length( )== 0){
						//pendenciaDt.setId_ServentiaGrupo(obTempHistorico.getId_ServentiaGrupo());
						//pendenciaDt.setServentiaGrupo(obTempHistorico.getServentiaGrupo());
					}
					
					if (obTempHistorico.isEnviaMagistrado() && obTempHistorico.getDataFim() != null	&& obTempHistorico.getDataFim().length( )== 0){
						pendenciaResponsavelDt.setConclusoMagistrado(true);
						//pendenciaDt.setEnviaMagistrado(true);
					}
					
				} else {
					ServentiaGrupoDt obTempServentiaGrupo = pendenciaResponsavelne.consultarSeventiaGrupoDistribuidorId(UsuarioSessao.getId_ServentiaCargo());
					if (obTempServentiaGrupo != null && obTempServentiaGrupo.getId() != null && obTempServentiaGrupo.getId().length()>0) {
						pendenciaResponsavelDt.setId_ServentiaGrupo(obTempServentiaGrupo.getId_ServentiaGrupoProximo());
						pendenciaResponsavelDt.setServentiaGrupo(obTempServentiaGrupo.getServentiaGrupoProximo());
						if (obTempServentiaGrupo.isEnviaMagistrado()){
							pendenciaResponsavelDt.setConclusoMagistrado(true);
							//pendenciaDt.setEnviaMagistrado(true);
						}
					}
				}
			}
			
			break;

		case Configuracao.Salvar:
			request.setAttribute("Mensagem", "Clique para Confirmar a Distribuição da Conclusão");
			break;

		case Configuracao.SalvarResultado:
			
				Mensagem = pendenciaResponsavelne.verificarTrocaResponsavelConclusao(pendenciaResponsavelDt);
				if (Mensagem.length() == 0) {					
					if(pendenciaResponsavelne.isDistribuirConclusaoUnidadeTrabalho(UsuarioSessao.getUsuarioDt())){
						pendenciaResponsavelne.distribuirConclusaoUnidadeTrabalhoLote(pendenciaResponsavelDt,  UsuarioSessao.getUsuarioDt());
					}
					redireciona(response, "Usuario?PaginaAtual=-10&MensagemOk=Distribuição Efetuada com Sucesso.");
					
				} else {
					redireciona(response, "Usuario?PaginaAtual=-10&MensagemErro="+Mensagem);
				}
				
				
			
			break;			
			
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
				request.setAttribute("tempRetorno", "DistribuicaoPendencia");
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
				stTemp = pendenciaResponsavelne.consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia() , UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo() , UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());
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
					request.setAttribute("tempRetorno","DistribuicaoPendencia");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = pendenciaResponsavelne.consultarServentiasAtivasJSON(stNomeBusca1, PosicaoPaginaAtual,  UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), "");
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
				request.setAttribute("tempRetorno", "DistribuicaoPendencia");
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
				if(pendenciaResponsavelDt.getlistaPendenciaDt() != null && pendenciaResponsavelDt.getlistaPendenciaDt().size()>0 &&
						pendenciaResponsavelne.isReDistribuirConclusaoUnidadeTrabalho(pendenciaResponsavelDt.getlistaPendenciaDt().get(0), UsuarioSessao.getUsuarioDt())){
					
					if (pendenciaResponsavelDt.isSemRegra()) {
						stTemp = pendenciaResponsavelne.consultarServentiaGrupoIdCargo(pendenciaResponsavelDt.getId_ServentiaCargo(), stNomeBusca1, PosicaoPaginaAtual);
						
					} else {
						pendenciaResponsavelDt.setId_ServentiaCargo("");
						pendenciaResponsavelDt.setServentiaCargo("");
						stTemp = pendenciaResponsavelne.consultarDescricaoServentiaGrupoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual,  UsuarioSessao.getUsuarioDt().getId_Serventia());
					}
					
				}
				enviarJSON(response, stTemp);				
				return;
			}

		default:
			break;
		}
		
		if (UsuarioSessao.getUsuarioDt().getGrupoCodigo() != null && Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE){
			request.setAttribute("EhParaExibirSemAssistente", "S");
			
		}
		
		request.setAttribute("conclusoMagistrado", String.valueOf(pendenciaResponsavelDt.isConclusoMagistrado()));
		request.setAttribute("semRegra", String.valueOf(pendenciaResponsavelDt.isSemRegra()));
		request.getSession().setAttribute("PendenciaResponsaveldt", pendenciaResponsavelDt);
		request.getSession().setAttribute("PendenciaResponsavelne", pendenciaResponsavelne);
		request.setAttribute("Id_Serventia",Id_Serventia);
		request.setAttribute("Serventia",Serventia);
		
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
