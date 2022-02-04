package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.ne.AudienciaProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Controla as trocas de Responsáveis por Sessão.
 * 
 * @author mmgomes
 * 04/12/2012
 */
public class AudienciaServentiaCargoResponsavelCt extends Controle {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5070485039070465584L;
	
	private static final int CURINGA_MAGISTRADO = 1;
	private static final int CURINGA_ASSISTENTE = 2;

	public int Permissao(){
		return PendenciaResponsavelDt.CodigoPermissao;
	}

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		AudienciaProcessoDt audienciaProcessoDt;
		ServentiaDt serventiaDt;
		ServentiaCargoDt serventiaCargoMagistradoDt;
		ServentiaCargoDt serventiaCargoAssistenteDt;
		AudienciaProcessoNe audienciaProcessone;
		ServentiaCargoDt serventiaCargoAssistenteDistribuidorAtualDt;

		List tempList = null;
		String Mensagem = "";
		String idAudienciaProcesso = null;
		int cargoTipoSelecionado = -1;
		
		String stTemp;
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		
		String stAcao = "/WEB-INF/jsptjgo/AudienciaServentiaCargoResponsavel.jsp";
		request.setAttribute("tempRetorno", "AudienciaServentiaCargoResponsavel");
		request.setAttribute("tempPrograma", "AudienciaServentiaCargoResponsavel");

		audienciaProcessone = (AudienciaProcessoNe) request.getSession().getAttribute("AudienciaProcessoNe");
		if (audienciaProcessone == null) audienciaProcessone = new AudienciaProcessoNe();		
		
		audienciaProcessoDt = (AudienciaProcessoDt) request.getSession().getAttribute("AudienciaProcessoDt");
		if (audienciaProcessoDt == null) audienciaProcessoDt = new AudienciaProcessoDt();
		
		serventiaCargoAssistenteDistribuidorAtualDt = (ServentiaCargoDt) request.getSession().getAttribute("ServentiaCargoAssistenteDistribuidorAtualDt");
		if (serventiaCargoAssistenteDistribuidorAtualDt == null) serventiaCargoAssistenteDistribuidorAtualDt = new ServentiaCargoDt();
		
		if (request.getSession().getAttribute("Serventiadt") != null)
			serventiaDt = (ServentiaDt)request.getSession().getAttribute("Serventiadt");
		else		
			serventiaDt = new ServentiaDt();
			
		if (request.getSession().getAttribute("ServentiaCargoMagistradodt") != null)
			serventiaCargoMagistradoDt = (ServentiaCargoDt)request.getSession().getAttribute("ServentiaCargoMagistradodt");
		else
			serventiaCargoMagistradoDt = new ServentiaCargoDt();
		
		if (request.getSession().getAttribute("ServentiaCargoAssistentedt") != null)
			serventiaCargoAssistenteDt = (ServentiaCargoDt)request.getSession().getAttribute("ServentiaCargoAssistentedt");
		else
			serventiaCargoAssistenteDt = new ServentiaCargoDt();
		
		if (request.getParameter("Id_Serventia") != null)
		{
			if (request.getParameter("Id_Serventia").equalsIgnoreCase("null")){
				serventiaDt.setId("");
				serventiaDt.setServentia("");
			} else {
				serventiaDt.setId(request.getParameter("Id_Serventia") );
				serventiaDt.setServentia(request.getParameter("Serventia"));
			}
		}			
		
		if (request.getParameter("Id_ServentiaCargoMagistrado") != null)
		{
			if (request.getParameter("Id_ServentiaCargoMagistrado").equalsIgnoreCase("null")){
				serventiaCargoMagistradoDt.setId( "" );
				serventiaCargoMagistradoDt.setServentiaCargo("");
			} else {
				serventiaCargoMagistradoDt.setId( request.getParameter("Id_ServentiaCargoMagistrado") );
				serventiaCargoMagistradoDt.setServentiaCargo(request.getParameter("ServentiaCargoMagistrado"));
			}
		}
		
		if (request.getParameter("Id_ServentiaCargoAssistente") != null || request.getParameter("Id_ServentiaCargoAssistenteConsulta") != null) 
		{	
			if (request.getParameter("Id_ServentiaCargoAssistente") != null && request.getParameter("Id_ServentiaCargoAssistente").trim().equalsIgnoreCase("")){
				serventiaCargoAssistenteDt.setId("");
				serventiaCargoAssistenteDt.setServentiaCargo("");
			} else {
				if (request.getParameter("Id_ServentiaCargoAssistente") != null)
				{
					serventiaCargoAssistenteDt.setId(request.getParameter("Id_ServentiaCargoAssistente"));
					serventiaCargoAssistenteDt.setServentiaCargo(request.getParameter("ServentiaCargoAssistente"));	
				} else if (request.getParameter("Id_ServentiaCargoAssistenteConsulta") != null) {
					serventiaCargoAssistenteDt.setId(request.getParameter("Id_ServentiaCargoAssistenteConsulta") );
					serventiaCargoAssistenteDt.setServentiaCargo(request.getParameter("ServentiaCargoAssistenteConsulta"));	
				}				
			}
			if (request.getParameter("Id_ServentiaCargoAssistente") != null)
				request.getSession().setAttribute("AudienciaSemAssistente", (request.getParameter("AudienciaSemAssistente") == null?"N":"S"));
		}
		
		if (request.getParameter("CargoTipoSelecionado") != null)
			cargoTipoSelecionado = Funcoes.StringToInt(String.valueOf(request.getParameter("CargoTipoSelecionado")));

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		switch (paginaatual) {

			//Inicializa Troca de Responsável
			case Configuracao.Novo:
				idAudienciaProcesso = String.valueOf(request.getParameter("Id_AudienciaProcesso"));			
				
				if (idAudienciaProcesso != null){
					//Consulta dados da sessão
					audienciaProcessoDt = audienciaProcessone.consultarIdCompleto(idAudienciaProcesso);				
					
					if (audienciaProcessoDt == null)
						throw new MensagemException("Não é possivel trocar responsável da sessão, pois a sessão não foi localizada.");	
					else 
					{
						audienciaProcessoDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						audienciaProcessoDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						
						// Obtendo o magistrado responsável atual...
						serventiaCargoMagistradoDt = audienciaProcessone.consultarServentiaCargoId(audienciaProcessoDt.getId_ServentiaCargo());
						if (serventiaCargoMagistradoDt == null) 
							serventiaCargoMagistradoDt = new ServentiaCargoDt();
						
						//Teste para verificar se o distribuidor pode trocar o responsável pela pendência
						if(UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.DISTRIBUIDOR_GABINETE){
							if (!UsuarioSessao.getUsuarioDt().getId_Serventia().equalsIgnoreCase(serventiaCargoMagistradoDt.getId_Serventia())){
								redireciona(response, "BuscaProcesso?PaginaAtual=1&&MensagemErro=Não é possivel trocar responsável da Sessão de 2º Grau, pois o Desembargador responsável não pertence ao seu gabinete.");
							}
						}
						
						// Obtendo a serventia do magistrado responsável...
						serventiaDt.setId(serventiaCargoMagistradoDt.getId_Serventia());
						serventiaDt.setServentia(serventiaCargoMagistradoDt.getServentia());
						
						// Obtendo o assistente/distribuidor responsável atual...
						PendenciaResponsavelDt pendenciaResponsavelDt = audienciaProcessone.consultarAssistenteDistribuidorResponsavelVotoProcesso(audienciaProcessoDt.getId_Processo(), audienciaProcessoDt.getId_ServentiaCargo(), audienciaProcessoDt.getId());						
						if (pendenciaResponsavelDt != null)
						{
							serventiaCargoAssistenteDistribuidorAtualDt = audienciaProcessone.consultarServentiaCargoId(pendenciaResponsavelDt.getId_ServentiaCargo());
							if (serventiaCargoAssistenteDistribuidorAtualDt != null && serventiaCargoAssistenteDistribuidorAtualDt.getCargoTipoCodigo().trim().equalsIgnoreCase(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE)))
							{								
								serventiaCargoAssistenteDt = new ServentiaCargoDt();
								serventiaCargoAssistenteDt.setId(serventiaCargoAssistenteDistribuidorAtualDt.getId());
								serventiaCargoAssistenteDt.setServentiaCargo(serventiaCargoAssistenteDistribuidorAtualDt.getServentiaCargo());
							}
						}
						if (serventiaCargoAssistenteDt == null) serventiaCargoAssistenteDt = new ServentiaCargoDt();
					}
						
				} else
					throw new MensagemException("Não foi possível visualizar sessão. executar()");
				
				break;
			
			case ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar://LOCALIZAR SERVENTIA
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia","Estado"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serventia");
					request.setAttribute("tempBuscaPrograma","Serventia");			
					request.setAttribute("tempRetorno","AudienciaServentiaCargoResponsavel");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					
					//Limpa campos da tela...
					serventiaDt.setId("");
					serventiaDt.setServentia("");
					serventiaCargoMagistradoDt.setId("");
					serventiaCargoMagistradoDt.setServentiaCargo("");
					serventiaCargoAssistenteDt.setId("");
					serventiaCargoAssistenteDt.setServentiaCargo("");
				} else {
					stTemp="";
					stTemp = audienciaProcessone.consultarDescricaoServentiaJSON(stNomeBusca1, UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual );
					serventiaCargoMagistradoDt = new ServentiaCargoDt();
					serventiaCargoAssistenteDt = new ServentiaCargoDt();
										
					enviarJSON(response, stTemp);
											
					return;								
				}
				break;
				
			case ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar://LOCALIZAR SERVENTIA CARGO
				if (serventiaDt.getId() != null && !serventiaDt.getId().equals("")){					
					int grupoTipo = -1;
					
					if (cargoTipoSelecionado == CURINGA_MAGISTRADO) grupoTipo = GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU;
					else if (cargoTipoSelecionado == CURINGA_ASSISTENTE)  grupoTipo = GrupoTipoDt.ASSISTENTE_GABINETE;
					
					if(null != request.getParameter("tempFluxo2")){
						grupoTipo = Funcoes.StringToInt(request.getParameter("tempFluxo2"));
					}
					
					if (grupoTipo > 0)
					{
						if (request.getParameter("Passo")==null){
							String[] lisNomeBusca = {"ServentiaCargo"};
							String[] lisDescricao = {"ServentiaCargo", "Usuario", "CargoTipo"};
							String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
							request.setAttribute("camposHidden",camposHidden);
							stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
							request.setAttribute("tempBuscaId", "Id_ServentiaCargo"+(grupoTipo==GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU?"Magistrado":"AssistenteConsulta"));
							request.setAttribute("tempBuscaDescricao", "ServentiaCargo"+(grupoTipo==GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU?"Magistrado":"AssistenteConsulta"));
							request.setAttribute("tempBuscaPrograma", "ServentiaCargo");
							request.setAttribute("tempRetorno", "AudienciaServentiaCargoResponsavel");
							request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
							request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
							request.setAttribute("PosicaoPaginaAtual", "0");
							request.setAttribute("QuantidadePaginas", "0");
							request.setAttribute("lisNomeBusca", lisNomeBusca);
							request.setAttribute("lisDescricao", lisDescricao);
							request.setAttribute("tempFluxo2", String.valueOf(grupoTipo));
							
							//Limpa campos da tela...
							if (grupoTipo==GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU) {
								serventiaCargoMagistradoDt.setId("");
								serventiaCargoMagistradoDt.setServentiaCargo("");
								serventiaCargoAssistenteDt.setId("");
								serventiaCargoAssistenteDt.setServentiaCargo("");	
							}							
						}else{
							stTemp = "";
							
							request.setAttribute("grupoTipoCodigo", request.getParameter("tempFluxo2"));
							stTemp = audienciaProcessone.consultarServentiaCargosGrupoJSON(serventiaDt.getId(), request.getParameter("tempFluxo2"), tempNomeBusca, PosicaoPaginaAtual);							
							enviarJSON(response, stTemp);
															
							return;
						}
					}
						
				} else
					request.setAttribute("MensagemErro","Selecione uma serventia para selecionar o Cargo da serventia");
				break;
	
			case Configuracao.Salvar:
				request.setAttribute("Mensagem", "Clique para Confirmar a Troca do Responsável");
				break;
	
			case Configuracao.SalvarResultado:
				Mensagem = audienciaProcessone.verificarTrocaResponsavelSessao(serventiaCargoMagistradoDt, audienciaProcessoDt.getId_ServentiaCargo(), serventiaCargoAssistenteDt, serventiaCargoAssistenteDistribuidorAtualDt, (request.getSession().getAttribute("AudienciaSemAssistente") != null && request.getSession().getAttribute("AudienciaSemAssistente").equals("S")));						
				if (Mensagem.length() == 0) {								
					audienciaProcessone.salvarTrocaResponsavelSessao(serventiaCargoMagistradoDt, serventiaCargoAssistenteDt, audienciaProcessoDt, UsuarioSessao.getUsuarioDt());
					
					redireciona(response, "BuscaProcesso?PaginaAtual=" + Configuracao.Imprimir + "&MensagemOk=Troca de Responsável Efetuada com Sucesso.");
						
					serventiaDt.limpar();
					serventiaCargoMagistradoDt.limpar();
				} else 
					request.setAttribute("MensagemErro", Mensagem);
			break;
			
			default:
				break;
		}
				
		request.getSession().setAttribute("AudienciaProcessoDt", audienciaProcessoDt);
		request.getSession().setAttribute("ServentiaCargoAssistenteDistribuidorAtualDt", serventiaCargoAssistenteDistribuidorAtualDt);		
		request.getSession().setAttribute("Serventiadt", serventiaDt);
		request.getSession().setAttribute("ServentiaCargoMagistradodt", serventiaCargoMagistradoDt);
		request.getSession().setAttribute("ServentiaCargoAssistentedt", serventiaCargoAssistenteDt);		
		request.getSession().setAttribute("AudienciaProcessoNe", audienciaProcessone);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

}
