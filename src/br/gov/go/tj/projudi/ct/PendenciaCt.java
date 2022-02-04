package br.gov.go.tj.projudi.ct;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSSignedData;

import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.AudienciaProcessoDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.PendenciaDt;
import br.gov.go.tj.projudi.dt.PendenciaResponsavelDt;
import br.gov.go.tj.projudi.dt.PendenciaStatusDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ne.MandadoTipoNe;
import br.gov.go.tj.projudi.ne.PendenciaNe;
import br.gov.go.tj.projudi.ne.ServentiaRelacionadaNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.GerarCabecalhoProcessoPDF;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.pdf.ConcatenatePDF;
import br.gov.go.tj.utils.pdf.ConverterHtmlPdf;

/**
 * Controle de pendencias
 * 
 * @author Ronneesley Moura Teles / Leandro Bernardes 
 * @since 04/07/2008 16:50
 */
public class PendenciaCt extends PendenciaCtGen {

	private static final long serialVersionUID = 6423049831291970889L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		PendenciaNe pendenciaNe = new PendenciaNe();
		PendenciaDt pendenciaDt = new PendenciaDt();
		PendenciaTipoDt pendenciaTipoDt;
		PendenciaStatusDt pendenciaStatusDt;
		ArquivoTipoDt arquivoTipoDt;
		ModeloDt modeloDt;
		ServentiaDt serventiaDt;
		ServentiaDt serventiaExpedirDt;
		ServentiaTipoDt serventiaTipoDt;
		ServentiaCargoDt serventiaCargoDt;
		ComarcaDt comarcaDt;
		String idPendencia = null;
		String hash = null;
		int fluxo = 0;
		String stNomeBusca1 = "";
		String exibirLiberar = "true";
		String codPendenciaTipo = "-1";
		
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");

		// Pagina anterior
		int paginaAnterior = -1;
		String ultimaOperacao = "";

		if (request.getParameter("ultimaOperacao") != null) ultimaOperacao = request.getParameter("ultimaOperacao");

		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));

		String stAcao = "/WEB-INF/jsptjgo/Branco.html";

		// Fluxo
		if (request.getParameter("fluxo") == null) {
			if (request.getSession().getAttribute("fluxo") != null) fluxo = Funcoes.StringToInt(request.getSession().getAttribute("fluxo").toString());
		} else
			fluxo = Funcoes.StringToInt(request.getParameter("fluxo"));

		if (request.getParameter("nomeArquivo") != null) request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
			request.setAttribute("nomeArquivo", "");

		// Recebe objetos pela sessao
		// *******************************************************************************************
		if (request.getSession().getAttribute("PendenciaTipodt") != null) pendenciaTipoDt = (PendenciaTipoDt) request.getSession().getAttribute("PendenciaTipodt");
		else
			pendenciaTipoDt = new PendenciaTipoDt();

		if (request.getSession().getAttribute("PendenciaStatusdt") != null) pendenciaStatusDt = (PendenciaStatusDt) request.getSession().getAttribute("PendenciaStatusdt");
		else
			pendenciaStatusDt = new PendenciaStatusDt();

		if (request.getSession().getAttribute("ArquivoTipodt") != null) arquivoTipoDt = (ArquivoTipoDt) request.getSession().getAttribute("ArquivoTipodt");
		else
			arquivoTipoDt = new ArquivoTipoDt();

		if (request.getSession().getAttribute("Modelodt") != null) modeloDt = (ModeloDt) request.getSession().getAttribute("Modelodt");
		else
			modeloDt = new ModeloDt();

		if (request.getSession().getAttribute("Pendenciadt") != null) pendenciaDt = (PendenciaDt) request.getSession().getAttribute("Pendenciadt");

		if (request.getSession().getAttribute("Id_Pendencia") != null && !(request.getParameter("NovaPesquisa") != null)) {
			idPendencia = String.valueOf(request.getSession().getAttribute("Id_Pendencia"));
		} else {// OPÇÃO SOLUCIONAR PENDÊNCIA
			idPendencia = String.valueOf(request.getParameter("pendencia"));
			hash = String.valueOf(request.getParameter("CodigoPendencia"));
		}

		if (request.getSession().getAttribute("Serventiadt") != null) serventiaDt = (ServentiaDt) request.getSession().getAttribute("Serventiadt");
		else
			serventiaDt = new ServentiaDt();

		if (request.getSession().getAttribute("ServentiaExpedirdt") != null) serventiaExpedirDt = (ServentiaDt) request.getSession().getAttribute("ServentiaExpedirdt");
		else
			serventiaExpedirDt = new ServentiaDt();

		if (request.getSession().getAttribute("ServentiaCargodt") != null) serventiaCargoDt = (ServentiaCargoDt) request.getSession().getAttribute("ServentiaCargodt");
		else
			serventiaCargoDt = new ServentiaCargoDt();

		if (request.getSession().getAttribute("ServentiaTipodt") != null) serventiaTipoDt = (ServentiaTipoDt) request.getSession().getAttribute("ServentiaTipodt");
		else
			serventiaTipoDt = new ServentiaTipoDt();

		if (request.getSession().getAttribute("Comarcadt") != null) comarcaDt = (ComarcaDt) request.getSession().getAttribute("Comarcadt");
		else
			comarcaDt = new ComarcaDt();

		// ********************************************************************************************************************
		if (request.getParameter("menu") != null && request.getParameter("menu").toString().equals("1")) {
			this.limparObjtosRelacionadosPendencia(pendenciaDt, pendenciaStatusDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, serventiaCargoDt, arquivoTipoDt, pendenciaTipoDt, modeloDt);
		}
		// *********************************************************************************************************************

		if (request.getParameter("Id_PendenciaTipo") != null) if (request.getParameter("Id_PendenciaTipo").equals("null")) {
			pendenciaTipoDt.setId("");
			pendenciaTipoDt.setPendenciaTipo("");
		} else {
			pendenciaTipoDt.setId(request.getParameter("Id_PendenciaTipo"));
			pendenciaTipoDt.setPendenciaTipo(request.getParameter("PendenciaTipo"));
		}
		
		if (request.getParameter("codTipoPendencia") != null){
			codPendenciaTipo = request.getParameter("codTipoPendencia");
			request.setAttribute("codTipoPendencia", codPendenciaTipo);
		}

		if (request.getParameter("Id_PendenciaStatus") != null) if (request.getParameter("Id_PendenciaStatus").equals("null")) {
			pendenciaStatusDt.setId("");
			pendenciaStatusDt.setPendenciaStatus("");
		} else {
			pendenciaStatusDt.setId(request.getParameter("Id_PendenciaStatus"));
			pendenciaStatusDt.setPendenciaStatus(request.getParameter("PendenciaStatus"));
		}

		if (request.getParameter("Id_Modelo") != null) if (request.getParameter("Id_Modelo").equalsIgnoreCase("null")) {
			modeloDt.setId("");
			modeloDt.setModelo("");
			modeloDt.limpar();
		} else {
			modeloDt.setId(request.getParameter("Id_Modelo"));
			modeloDt.setModelo(request.getParameter("Modelo"));
			if (!request.getParameter("Id_Modelo").equalsIgnoreCase("")) 
				request.getSession().setAttribute("Id_Modelo", request.getParameter("Id_Modelo"));			  
		}
		
		if (request.getParameter("Id_Serventia") != null) if (request.getParameter("Id_Serventia").equalsIgnoreCase("null")) {
			serventiaDt.setId("");
			serventiaDt.setServentia("");
		} else {
			serventiaDt.setId(request.getParameter("Id_Serventia"));
			serventiaDt.setServentia(request.getParameter("Serventia"));
		}
		
		if (request.getParameter("Id_ServentiaExpedir") != null) if (request.getParameter("Id_ServentiaExpedir").equalsIgnoreCase("null")) {
			serventiaExpedirDt.setId("");
			serventiaExpedirDt.setServentia("");
			serventiaExpedirDt.setId_ServentiaTipo("");
		} else {
			serventiaExpedirDt.setId(request.getParameter("Id_ServentiaExpedir"));
			serventiaExpedirDt.setServentia(request.getParameter("ServentiaExpedir"));
			serventiaExpedirDt.setId_ServentiaTipo(request.getParameter("Id_ServentiaTipo"));
		}

		if (request.getParameter("Id_OficialAdhoc") != null) if (request.getParameter("Id_OficialAdhoc").equalsIgnoreCase("null")) {
			pendenciaDt.setId_OficialAdhoc("");
			pendenciaDt.setOficialAdhoc("");
		} else {
			pendenciaDt.setId_OficialAdhoc(request.getParameter("Id_OficialAdhoc"));
			pendenciaDt.setOficialAdhoc(request.getParameter("OficialAdhoc"));
		}
		
		if (request.getParameter("Id_ServentiaCargo") != null) if (request.getParameter("Id_ServentiaCargo").equalsIgnoreCase("null")) {
			serventiaCargoDt.setId("");
			serventiaCargoDt.setServentiaCargo("");
		} else {
			serventiaCargoDt.setId(request.getParameter("Id_ServentiaCargo"));
			serventiaCargoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		}

		if (request.getParameter("Id_ServentiaTipo") != null) if (request.getParameter("Id_ServentiaTipo").equalsIgnoreCase("null")) {
			serventiaTipoDt.setId("");
			serventiaTipoDt.setServentiaTipo("");
		} else {
			serventiaTipoDt.setId(request.getParameter("Id_ServentiaTipo"));
			serventiaTipoDt.setServentiaTipo(request.getParameter("ServentiaTipo"));
		}

		if (request.getParameter("Id_Comarca") != null) if (request.getParameter("Id_Comarca").equalsIgnoreCase("null")) {
			comarcaDt.setId("");
			comarcaDt.setComarca("");
		} else {
			comarcaDt.setId(request.getParameter("Id_Comarca"));
			comarcaDt.setComarca(request.getParameter("Comarca"));
		}
		
		//campos necessários para o alvará de solutra***************************************************************************************************************************************************************************
		pendenciaDt.setId_EventoTipo(request.getParameter("Id_EventoTipo"));
		pendenciaDt.setEventoTipo(request.getParameter("EventoTipo"));
		pendenciaDt.setDataEvento(request.getParameter("DataEvento"));
		//**********************************************************************************************************************************************************************************************************************

		pendenciaDt.setDataFim(request.getParameter("DataRecebimento"));
		if(request.getParameter("idMandadoTipo") != null && request.getParameter("idMandadoTipo") != "") {
			pendenciaDt.setId_MandadoTipo(request.getParameter("idMandadoTipo"));
			pendenciaDt.setMandadoTipo(pendenciaDt.getNomeTipoMandado(request.getParameter("idMandadoTipo")));
		}
				
		arquivoTipoDt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		arquivoTipoDt.setArquivoTipo(request.getParameter("ArquivoTipo"));

		// Recebe o texto do editor e armazena
		if (request.getParameter("TextoEditor") != null) {
			request.getSession().setAttribute("TextoEditor", request.getParameter("TextoEditor"));
		}
		// *********************************************************************************************************************

		if(UsuarioSessao.getUsuarioDt().getCargoTipoCodigo().equalsIgnoreCase(String.valueOf(CargoTipoDt.OFICIAL_JUSTICA))) {
			exibirLiberar = "false";
		}
		
		// Modifica atributos
		request.setAttribute("exibirLiberar", exibirLiberar);
		request.setAttribute("tempPrograma", "Modelo");
		request.setAttribute("tempBuscaId_PendenciaTipo", "Id_PendenciaTipo");
		request.setAttribute("tempBuscaPendenciaTipo", "PendenciaTipo");
		request.setAttribute("tempBuscaId_Modelo", "Id_Modelo");
		request.setAttribute("tempBuscaModelo", "Modelo");
		request.setAttribute("tempBuscaId_ArquivoTipo", "Id_ArquivoTipo");
		request.setAttribute("tempBuscaArquivoTipo", "ArquivoTipo");
		request.setAttribute("Id_ArquivoTipo", arquivoTipoDt.getId());

		request.setAttribute("PaginaAnterior", paginaatual);
		
		// Parâmetros relacionados à expedição de mandados ----------------------------------------
	        
	    if (request.getParameter("prazoMandado") != null) {
	      	pendenciaDt.setPrazoMandado(request.getParameter("prazoMandado"));
	    }
	    //request.setAttribute( "prazoMandado", request.getSession().getAttribute("prazoMandado"));        

        if (request.getParameter("codigoPrazoMandado") != null){
        	pendenciaDt.setCodigoPrazoMandado(request.getParameter("codigoPrazoMandado"));
        }
        //request.setAttribute( "codigoPrazoMandado", request.getSession().getAttribute("codigoPrazoMandado") );
                
        if(request.getParameter("Id_UsuarioServentia_2") != null) {
        	pendenciaDt.setId_UsuarioServentia2(request.getParameter("Id_UsuarioServentia_2"));
        	pendenciaDt.setNome_UsuarioServentia2(request.getParameter("UsuarioServentia_2"));
        } else {
        	pendenciaDt.setId_UsuarioServentia2("");
        	pendenciaDt.setNome_UsuarioServentia2("");
        }
		// Se for um oficial fechando um mandado, não carrega o tipo do arquivo.
		if(UsuarioSessao.isOficial() && pendenciaDt != null && pendenciaDt.isMandado()) {
			// Verifica no conteúdo do documento a quantidade de locomoção utilizada.
			if( pendenciaNe.teraOficialCompanheiroMandado(pendenciaDt.getId()) ){
				request.setAttribute("temOficialCompanheiro", "sim");
			}
		}
        //-------------------------------------------------------------------------------------------
        
		if (request.getParameter("ordenacao") != null)
			request.setAttribute("ordenacao", request.getParameter("ordenacao"));
		
		if (request.getParameter("ManterMensagemOK") != null && request.getParameter("MensagemOk") != null)
			request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else
			request.setAttribute("MensagemOk", "");
		request.setAttribute("MensagemErro", "");

		// status pendencia aguardando retorno
		pendenciaDt.setStatusPendenciaRetorno(request.getParameter("status"));

		// Modifiaca os dados para o log
		pendenciaDt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		pendenciaDt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		String stTempRetorno ="";
		String stTempPrograma="";
		String stTempId="";
		String stTempDescricao="";
		int  inTempPaginaAtualJSON;
		
		// Verifica a pagina atual
		switch (paginaatual) {
		case PendenciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar: // LOCALIZAR PENDENCIA TIPO
				
				if (request.getParameter("Passo")==null){
					
					inTempPaginaAtualJSON= Configuracao.Editar;
					
					if (paginaAnterior == Configuracao.Curinga7) {
						stTempPrograma = "Acompanhamento de pendências";
						stTempRetorno = "Pendencia?fluxo=" + fluxo;		
						inTempPaginaAtualJSON = 7;
	
						stTempId= request.getParameter("tempBuscaId").trim().equals("") ? "Id_PendenciaTipo" : request.getParameter("tempBuscaId");
						stTempDescricao= request.getParameter("tempBuscaDescricao").trim().equals("") ? "PendenciaTipo" : request.getParameter("tempBuscaDescricao");
	
					} else if (paginaAnterior == Configuracao.Curinga6) {
						stTempPrograma = "Acompanhamento de pendências";
						stTempRetorno = "Pendencia?fluxo=" + fluxo;
						inTempPaginaAtualJSON = 6;
	
						stTempId = request.getParameter("tempBuscaId").trim().equals("") ? "Id_PendenciaTipo" : request.getParameter("tempBuscaId");
						stTempDescricao= request.getParameter("tempBuscaDescricao").trim().equals("") ? "PendenciaTipo" : request.getParameter("tempBuscaDescricao");
	
					} else if (paginaAnterior == Configuracao.Curinga8) {
						stTempPrograma = "Criar pendência";
						stTempRetorno =  "Pendencia";
						inTempPaginaAtualJSON = 8;
						
						request.getSession().setAttribute("ultimaAba", request.getParameter("tempBuscaId").trim().equals("") ? 1 : 0);
	
						stTempId = "Id_PendenciaTipo";
						stTempDescricao= "PendenciaTipo";
	
					} else if (paginaAnterior == Configuracao.Curinga9) {
						stTempPrograma = "Distribuição de pendência";
						stTempRetorno =  "Pendencia?prioridade=" + (request.getParameter("prioridade") != null ? request.getParameter("prioridade") : "1");
						inTempPaginaAtualJSON = 9;
						request.getSession().setAttribute("ultimaAba", request.getParameter("tempBuscaId").trim().equals("") ? 1 : 0);
	
						stTempId = request.getParameter("tempBuscaId").trim().equals("") ? "Id_PendenciaTipo" : request.getParameter("tempBuscaId");
						stTempDescricao= request.getParameter("tempBuscaDescricao").trim().equals("") ? "PendenciaTipo" : request.getParameter("tempBuscaDescricao");
	
					} else {
						inTempPaginaAtualJSON = Configuracao.Editar;
						
						if (ultimaOperacao.equals("")) {
							stTempPrograma = "Resolver pendência";
							stTempRetorno =  "Pendencia";
	
							stTempId = "Id_PendenciaTipo";
							stTempDescricao= "PendenciaTipo";
	
							request.getSession().setAttribute("ultimaAba", 1);
						} else if (ultimaOperacao.equals("Reabrir")) {
							stTempPrograma = "Reabrir pendência";
							stTempRetorno =  "Pendencia?operacao=Reabrir";
	
							stTempId = "Id_PendenciaTipo";
							stTempDescricao= "PendenciaTipo";
	
							request.getSession().setAttribute("ultimaAba", 0);
						}
					}
							
					String[] lisNomeBusca = {"PendenciaTipo"};
					String[] lisDescricao = {"Tipo de PendenciaTipo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					
					String stPermissaoPaginaAtual = String.valueOf(PendenciaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
					
					atribuirJSON(request, stTempId, stTempDescricao, stTempPrograma, stTempRetorno, inTempPaginaAtualJSON, stPermissaoPaginaAtual, lisNomeBusca, lisDescricao);
					
					modeloDt.limpar();
					
				} else {
					String stTemp = "";
					stTemp = pendenciaNe.consultarDescricaoPendenciaTipoJSON(stNomeBusca1, PosicaoPaginaAtual);
						
						enviarJSON(response, stTemp);
						
					
					return;
				}

			break;
		case PendenciaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar:// LOCALIZAR PENDENCIA STATUS
				
			if (request.getParameter("Passo")==null){
				
				inTempPaginaAtualJSON= Configuracao.Editar;
				
				if (paginaAnterior == Configuracao.Curinga7) {
					stTempPrograma = "Acompanhamento de pendências";
					stTempRetorno = "Pendencia?fluxo=" + fluxo;
					
					inTempPaginaAtualJSON = 7;
					stTempId= request.getParameter("tempBuscaId").trim().equals("") ? "Id_PendenciaTipo" : request.getParameter("tempBuscaId");
					stTempDescricao= request.getParameter("tempBuscaDescricao").trim().equals("") ? "PendenciaTipo" : request.getParameter("tempBuscaDescricao");

				} else if (paginaAnterior == Configuracao.Curinga6) {
					stTempPrograma= "Acompanhamento de pendências";
					stTempRetorno="Pendencia?fluxo=" + fluxo;
					inTempPaginaAtualJSON = 6;

					stTempId= request.getParameter("tempBuscaId").trim().equals("") ? "Id_PendenciaTipo" : request.getParameter("tempBuscaId");
					stTempDescricao= request.getParameter("tempBuscaDescricao").trim().equals("") ? "PendenciaTipo" : request.getParameter("tempBuscaDescricao");

				} else if (paginaAnterior == Configuracao.Curinga9) {
					stTempPrograma= "Reabrir pendência";
					stTempRetorno= "Pendencia?prioridade=" + (request.getParameter("prioridade") != null ? request.getParameter("prioridade") : "1");
					inTempPaginaAtualJSON = 9;
					stTempId= "Id_PendenciaStatus";
					stTempDescricao= "PendenciaStatus";
				}
				
				String[] lisNomeBusca = {"PendenciaStatus"};
				String[] lisDescricao = {"Status de Pendencia"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				
				String stPermissaoPaginaAtual = String.valueOf(PendenciaStatusDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
				
				atribuirJSON(request, stTempId, stTempDescricao, stTempPrograma, stTempRetorno, inTempPaginaAtualJSON, stPermissaoPaginaAtual, lisNomeBusca, lisDescricao);										
			
			}else{
				
				String stTemp = "";
				stTemp = pendenciaNe.consultarDescricaoPendenciaStatusJSON(tempNomeBusca, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}
				

			break;
		case ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar:

			if (request.getParameter("Passo")==null){
				
				inTempPaginaAtualJSON= Configuracao.Editar;
				
				if (paginaAnterior == Configuracao.Curinga8) {
					if (fluxo == 1) {
						stTempPrograma= "Criar publicação";
						stTempRetorno= "Pendencia?fluxo=" + fluxo;							
					} else {
						stTempPrograma= "Criar pendência";
						stTempRetorno= "Pendencia";
						request.getSession().setAttribute("ultimaAba", 1);
					}
					inTempPaginaAtualJSON = 8;
				} else {
					if (ultimaOperacao.equals("")) {
						stTempPrograma= "Resolver pendência";
						stTempRetorno= "Pendencia";
						request.getSession().removeAttribute("ultimaAba");
						request.getSession().setAttribute("ultimaAba", 1);
					} else if (ultimaOperacao.equals("Reabrir")) {
						stTempPrograma= "Reabrir pendência";
						stTempRetorno= "Pendencia?operacao=Reabrir";

						request.getSession().setAttribute("ultimaAba", 0);
					}
				}

				// limpa o modelodt
				modeloDt.limpar();
				
				String[] lisNomeBusca = {"ArquivoTipo"};
				String[] lisDescricao = {"Tipo de Arquivo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				
				String stPermissaoPaginaAtual = String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
				
				atribuirJSON(request, "Id_ArquivoTipo", "ArquivoTipo", stTempPrograma, stTempRetorno, inTempPaginaAtualJSON, stPermissaoPaginaAtual, lisNomeBusca, lisDescricao);										
			
			}else{
				
				String stTemp = "";
				stTemp = pendenciaNe.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;
			}												

			break;
		case ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar:// LOCALIZAR SERVENTIA
			if (request.getParameter("abaEncaminhar") != null && request.getParameter("abaEncaminhar").toString().equals("encaminhar")) {
				
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Serventia" };
					String[] lisDescricao = {"Serventia", "Estado" };
					String[] lisDescricaoConsultaMandado = {"Serventia" };
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					serventiaCargoDt = new ServentiaCargoDt();
					request.getSession().setAttribute("ultimaAba", 2);
	
					request.setAttribute("tempBuscaId", "Id_Serventia");
					request.setAttribute("tempBuscaDescricao", "Serventia");
					
					if (paginaAnterior == Configuracao.Curinga8) {
						request.setAttribute("tempBuscaPrograma", "Criar pendência");
						request.setAttribute("tempRetorno", "Pendencia?PaginaAtual=8");
						request.getSession().setAttribute("ultimaAba", 0);
					} else {
						if (ultimaOperacao.equals("")) {
							request.setAttribute("tempBuscaPrograma", "Resolver pendência");
							request.setAttribute("tempRetorno", "Pendencia");
						} else if (ultimaOperacao.equals("Reabrir")) {
							request.setAttribute("tempBuscaPrograma", "Reabrir pendência");
							request.setAttribute("tempRetorno", "Pendencia?operacao=Reabrir");
							serventiaCargoDt = new ServentiaCargoDt();
						}
					}
					
					request.setAttribute("tempDescricaoId", "Id");				
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					if(Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO ){
						request.setAttribute("lisDescricao", lisDescricaoConsultaMandado);
					} else {
						request.setAttribute("lisDescricao", lisDescricao);
					}
					request.setAttribute("tempFluxo1", "2");
				}
				
			}else{
				
				if (request.getParameter("Passo") == null) {
					String[] lisNomeBusca = {"Serventia" };
					String[] lisDescricao = {"Serventia", "Estado" };
					String[] lisDescricaoConsultaMandado = {"Serventia" };
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("id_ServentiaTipo", serventiaTipoDt.getId());
					request.getSession().setAttribute("ultimaAba", 1);
					
					request.setAttribute("tempBuscaId", "Id_ServentiaExpedir");
					request.setAttribute("tempBuscaDescricao", "ServentiaExpedir");
					
					if (paginaAnterior == Configuracao.Curinga8) {
						request.setAttribute("tempBuscaPrograma", "Criar pendência");
						request.setAttribute("tempRetorno", "Pendencia");
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga8);
						request.getSession().setAttribute("ultimaAba", 0);
					} else {
						if (ultimaOperacao.equals("")) {
							request.setAttribute("tempBuscaPrograma", "Resolver pendência");
							request.setAttribute("tempRetorno", "Pendencia");
						} else if (ultimaOperacao.equals("Reabrir")) {
							request.setAttribute("tempBuscaPrograma", "Reabrir pendência");
							request.setAttribute("tempRetorno", "Pendencia?operacao=Reabrir");
							serventiaCargoDt = new ServentiaCargoDt();
						}				
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					}
					
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					if(Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO ){
						request.setAttribute("lisDescricao", lisDescricaoConsultaMandado);
					} else {
						request.setAttribute("lisDescricao", lisDescricao);
					}
					request.setAttribute("tempFluxo1", "1");
				}
				
			}
				
			if (request.getParameter("Passo") != null) {
				String stTemp = "";
				
				if(request.getParameter("tempFluxo2").equalsIgnoreCase("2")){
					stTemp = pendenciaNe.consultarDescricaoServentiaJSON(stNomeBusca1, "", UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
				}else{
					// Se a pendência for do tipo MANDADO e selecionar uma Serventia do Tipo Central de Mandados,
					// lista de maneira específica as serventias para as quais o mandado pode ser expedido. O mandado pode
					// ser expedido para a Central de Mandados vinculada com a serventia que está resolvendo a pendência ou para
					// as centrais de mandados das comarcas contíguas. O cadastro das comarcas contíguas foi feito utilizando
					// o próprio cadastro de serventias relacionadas do Projudi. A central de mandados é relacionada com as centrais
					// de mandados das comarcas contíguas para que saibamos quais devemos listar.
					if(Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO && 
							Funcoes.StringToInt(pendenciaNe.consultarCodigoServentiaTipo(serventiaTipoDt.getId())) == ServentiaTipoDt.CENTRAL_MANDADOS){
						
						ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
				        ServentiaDt centralMandadosRelacionada = serventiaRelacionadaNe.consultarCentralMandadosRelacionada(UsuarioSessao.getUsuarioDt().getId_Serventia());
				        if(centralMandadosRelacionada != null) {
				        	stTemp = pendenciaNe.consultarDescricaoCentralMandadoRelacionadasJSON(centralMandadosRelacionada.getId(), stNomeBusca1, UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
				        }
	                    if (stTemp.equalsIgnoreCase("")) {
		                    stTemp = "Não existem serventias relacionadas para expedição do mandado.";		
	                    }	
					} 
					else{
						stTemp = pendenciaNe.consultarDescricaoServentiaJSON(stNomeBusca1, serventiaTipoDt.getId(), UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
					}
				}
				enviarJSON(response, stTemp);					
				return;
			}
			break;
		
		case ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar:// LOCALIZAR COMARCA
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Comarca"};
				String[] lisDescricao = {"Comarca"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_Comarca");
				request.setAttribute("tempBuscaDescricao","Comarca");
				request.setAttribute("tempBuscaPrograma","Comarca");			
				request.setAttribute("tempRetorno","Pendencia");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ComarcaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
			} else {
				String stTemp="";
				stTemp = pendenciaNe.consultarDescricaoComarcaJSON(stNomeBusca1, PosicaoPaginaAtual);
					request.getSession().setAttribute("ultimaAba", 1);
					enviarJSON(response, stTemp);
				return;								
			}
			break;
			
		case ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar:// LOCALIZAR SERVENTIA CARGO
			
			String stMensagemErro = "";
			if (serventiaDt.getId() != null && !serventiaDt.getId().equals("")) {
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia Cargo"};
					String[] lisDescricao = {"Serventia Cargo","Tipo Cargo","Nome Usuário","Serventia"};				
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "Serventia Cargo");
					
					if (ultimaOperacao.equals("")) {
						request.getSession().setAttribute("ultimaAba", 2);
						request.setAttribute("id_Serventia", serventiaDt.getId());
						request.setAttribute("tempPrograma", "Pendencia");
						request.setAttribute("tempRetorno", "Pendencia");
					} else if (ultimaOperacao.equals("Reabrir")) {
						request.setAttribute("tempPrograma", "Reabrir pendência");
						request.setAttribute("tempRetorno", "Pendencia?operacao=Reabrir");
						request.setAttribute("id_Serventia", serventiaDt.getId());
					}
					
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual",  String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					
				}else{
					String stTemp = "";
					
					if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.ELABORACAO_VOTO) {						
						stTemp = pendenciaNe.consultarDescricaoServentiaCargoMagistradosPorServentiaJSON(stNomeBusca1, serventiaDt.getId(), PosicaoPaginaAtual);
					}else{
						stTemp = pendenciaNe.consultarDescricaoServentiaCargoPorServentiaJSON(stNomeBusca1, serventiaDt.getId(), PosicaoPaginaAtual);
					}
						
					enviarJSON(response, stTemp);					
					
					return;
				}
			}else{
				stMensagemErro = "Selecione uma serventia para selecionar o Cargo da serventia";
			}
			
			if (!stMensagemErro.equals("")) {
				request.setAttribute("MensagemErro", stMensagemErro);
	
				if (ultimaOperacao.equals("Reabrir")) {
					stAcao = "/WEB-INF/jsptjgo/PendenciaReabrir.jsp";
					ultimaOperacao = "Reabrir";
					// Se recebeu o pendencia pai
					if (request.getParameter("Id_PendenciaPai") != null) {
						pendenciaDt.setId_PendenciaPai(request.getParameter("Id_PendenciaPai"));
					}
					pendenciaDt = this.reabrir(request, false, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, modeloDt, serventiaDt, serventiaCargoDt, UsuarioSessao);
	
				} else {
					this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, false);
					request.getSession().setAttribute("ultimaAba", 2);
					// TELAS PARA RESOLVER PENDÊNCIAS
					stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
				}
			}
			
			break;
			
		case ServentiaTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar: // LOCALIZAR SERVENTIA TIPO

			inTempPaginaAtualJSON= Configuracao.Editar;
			
			if (paginaAnterior == Configuracao.Curinga8) {
				stTempPrograma = "Criar pendência";
				stTempRetorno =  "Pendencia";
				inTempPaginaAtualJSON = 8;
				serventiaExpedirDt = new ServentiaDt();
				request.getSession().setAttribute("ultimaAba", 0);
			} else {
				if (ultimaOperacao.equals("")) {
					stTempPrograma = "Resolver pendência";
					stTempRetorno= "Pendencia";
					request.getSession().setAttribute("ultimaAba", 1);
				}
			}
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"ServentiaTipo"};
				String[] lisDescricao = {"Tipo de Serventia"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				
				String permissao = String.valueOf(ServentiaTipoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar);
				
				atribuirJSON(request, "Id_ServentiaTipo", "ServentiaTipo",stTempPrograma,stTempRetorno, inTempPaginaAtualJSON, permissao, lisNomeBusca, lisDescricao);
				
			} else {
				String stTemp="";
				stTemp = pendenciaNe.consultarDescricaoServentiaTipoJSON(stNomeBusca1);
									
					enviarJSON(response, stTemp);
					
				
				return;								
			}		

			break;
		case ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar:// LOCALIZAR MODELO
			
			if (request.getParameter("Passo") == null) {
				
				stTempPrograma="";
				
				if (paginaAnterior == Configuracao.Curinga8) {
					if (fluxo == 1) {
						stTempPrograma= "Criar publicação";
						stTempRetorno = "Pendencia?PaginaAtual=8&fluxo=" + fluxo;
					} else {
						stTempPrograma = "Criar pendência";
						stTempRetorno =  "Pendencia?PaginaAtual=8";
						request.getSession().setAttribute("ultimaAba", 1);
					}
				} else {
					if (ultimaOperacao.equals("")) {
						stTempPrograma = "Resolver pendência";
						stTempRetorno =  "Pendencia";

						request.getSession().setAttribute("ultimaAba", 1);
					} else if (ultimaOperacao.equals("Reabrir")) {
						stTempPrograma = "Reabrir pendência";
						stTempRetorno =  "Pendencia?operacao=Reabrir";

						request.getSession().setAttribute("ultimaAba", 0);
					}
				}
				
				String[] lisNomeBusca = {"Modelo"};
				String[] lisDescricao = {"Modelo","Serventia","ArquivoTipo"};
				stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";				
				
				String stPemissao= String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar);
				
				atribuirJSON(request, "Id_Modelo", "Modelo", stTempPrograma, stTempRetorno, Configuracao.Editar, stPemissao, lisNomeBusca, lisDescricao);
				break;
				
			} else {
				String stTemp = "";
				stTemp = pendenciaNe.consultarDescricaoModeloTipoJSON(stNomeBusca1,  PosicaoPaginaAtual, arquivoTipoDt.getId(), UsuarioSessao.getUsuarioDt() );				
				if (stTemp!=null && stTemp.length()>0){					
					request.getSession().removeAttribute("TextoEditor");								
					enviarJSON(response, stTemp);					
					return;												
				}else{
					modeloDt.limpar();

					// Verifica a origem
					if (paginaAnterior == Configuracao.Curinga8) {
						if (fluxo == 0) {
							if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ADVOGADO) stAcao = "/WEB-INF/jsptjgo/CriarPendenciaAdvogado.jsp";
							else
								stAcao = "/WEB-INF/jsptjgo/CriarPendencia.jsp";

							pendenciaDt = this.criarPendencia(request, pendenciaNe, pendenciaDt, serventiaExpedirDt, serventiaTipoDt, pendenciaTipoDt, arquivoTipoDt, modeloDt, UsuarioSessao);
						}

						if (fluxo == 1) {
							stAcao = "/WEB-INF/jsptjgo/PendenciaPublica.jsp";
							this.publicacaoPublica(request, pendenciaNe, pendenciaDt, arquivoTipoDt, modeloDt, UsuarioSessao);
						}
						request.setAttribute("PaginaAnterior", Configuracao.Curinga8);
						break;
					}
					
				}
				
			}
								
					
			//this.lista(request, "Modelo", pendenciaNe.consultarDescricaoModeloPorTipo(tempNomeBusca, PosicaoPaginaAtual, arquivoTipoDt.getId(), UsuarioSessao.getUsuarioDt()), pendenciaNe.getQuantidadePaginas(), PosicaoPaginaAtual, "Não há modelos para este tipo de arquivo", paginaatual, Configuracao.Editar)) {			
				

			// A ausencia do break e proposital, tendo em vista o fluxo

		case Configuracao.Editar: // SELECIONA A ÚLTIMA PENDÊNCIA, PENDÊNCIA A SOLUCIONAR, PENDÊNCIA REABRIR
			
			String operacao = request.getParameter("operacao");
			boolean carregarArquivoTipo = true;
			List arq = Funcoes.converterMapParaList((HashMap) request.getSession().getAttribute("ListaArquivos"));
			if (arq != null && arq.size() > 0) carregarArquivoTipo = false;

			// Verifica se teve a ultima operacao
			if (!ultimaOperacao.equals("") && (operacao == null || operacao.equals(""))) {
				operacao = ultimaOperacao;
			}

			// Se e para pegar pelo tipo
			if (request.getParameter("operacao") != null && operacao.startsWith("PegarPorTipo")) {
				// pendencia nova limpa DT's relacionados a pendência
				this.limparObjtosRelacionadosPendencia(pendenciaDt, pendenciaStatusDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, serventiaCargoDt, arquivoTipoDt, pendenciaTipoDt, modeloDt);
				this.limparArquivosSessao(request);

				// Pegar pendencia ultima pendencia disponivel
				pendenciaDt = this.pegarPendenciaPorTipo(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt);
				request.setAttribute("PaginaAtual", Configuracao.Salvar);

				Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
				// Para a pendência do tipo Intimação via Telefone, será setado o status aguardando retorno para que ela se porte da forma correta ao longo da sua finalização.
				if (tipoPendencia == PendenciaTipoDt.INTIMACAO_VIA_TELEFONE) {
					pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO));
				}
				if (tipoPendencia == PendenciaTipoDt.INTIMACAO || tipoPendencia == PendenciaTipoDt.CARTA_CITACAO || tipoPendencia == PendenciaTipoDt.MANDADO
						|| tipoPendencia == PendenciaTipoDt.INTIMACAO_AUDIENCIA || tipoPendencia == PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA) {
					if (pendenciaDt.getId_ProcessoParte() == null || pendenciaDt.getId_ProcessoParte().length() == 0) {
						request.setAttribute("MensagemErro", "Esta pendência está inconsistente. Não foi possível identificar a parte vinculada. Entre em contato com o Suporte e informe o número da pendência e a parte que deve ser vinculada para ser possível a expedição.");
					}
				}
				stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);

			} else if (request.getParameter("operacao") != null && operacao.startsWith("PreAnalisadasServentia")) {
				// pendencia nova limpa DT's relacionados a pendência
				this.limparObjtosRelacionadosPendencia(pendenciaDt, pendenciaStatusDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, serventiaCargoDt, arquivoTipoDt, pendenciaTipoDt, modeloDt);
				this.limparArquivosSessao(request);

				// Pegar pendencia ultima pendencia disponivel
				pendenciaDt = this.pegarPendenciaPorTipo(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt);
				request.setAttribute("PaginaAtual", Configuracao.Salvar);

				Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
				// Para a pendência do tipo Intimação via Telefone, será setado o status aguardando retorno para que ela se porte da forma correta ao longo da sua finalização.
				if (tipoPendencia == PendenciaTipoDt.INTIMACAO_VIA_TELEFONE) {
					pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO));
				}
				if (tipoPendencia == PendenciaTipoDt.INTIMACAO || tipoPendencia == PendenciaTipoDt.CARTA_CITACAO || tipoPendencia == PendenciaTipoDt.MANDADO
						|| tipoPendencia == PendenciaTipoDt.INTIMACAO_AUDIENCIA || tipoPendencia == PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA) {
					if (pendenciaDt.getId_ProcessoParte() == null || pendenciaDt.getId_ProcessoParte().length() == 0) {
						request.setAttribute("MensagemErro", "Esta pendência está inconsistente. Não foi possível identificar a parte vinculada. Entre em contato com o Suporte e informe o número da pendência e a parte que deve ser vinculada para ser possível a expedição.");
					}
				}
				stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
				
			} else if (UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash()) && operacao != null && (operacao.equals("Reabrir") || operacao.equals("ReabrirInicial"))) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaReabrir.jsp";
				ultimaOperacao = "Reabrir";
				// Se recebeu o pendencia pai
				if (request.getParameter("Id_PendenciaPai") != null) {
					pendenciaDt.setId_PendenciaPai(request.getParameter("Id_PendenciaPai"));
				}

				// Chama o controle para reabrir a pendencia
				pendenciaDt = this.reabrir(request, false, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, modeloDt, serventiaExpedirDt, serventiaCargoDt, UsuarioSessao);

			} else {// consulta pendencias seleciadas em telas de consultas por exemplo: reservadas, pre-analisadas
				if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)) {
					boolean finalizada = Boolean.valueOf(request.getParameter("finalizada"));
					// pendencia nova limpa DT's relacionados a pendência
					this.limparObjtosRelacionadosPendencia(pendenciaDt, pendenciaStatusDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, serventiaCargoDt, arquivoTipoDt, pendenciaTipoDt, modeloDt);
					this.limparArquivosSessao(request);
					
					if (finalizada)
						pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(idPendencia, UsuarioSessao);
					else
						pendenciaDt = pendenciaNe.consultarPendenciaId(idPendencia, UsuarioSessao);

					Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
					
					// Para a pendência do tipo Intimação via Telefone, será setado o status Aguardando Retorno para que ela se porte da forma correta ao longo da sua finalização.
					if (tipoPendencia == PendenciaTipoDt.INTIMACAO_VIA_TELEFONE) {
						pendenciaDt.setPendenciaStatusCodigo(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO));
					}
					if (tipoPendencia == PendenciaTipoDt.INTIMACAO || tipoPendencia == PendenciaTipoDt.CARTA_CITACAO || tipoPendencia == PendenciaTipoDt.MANDADO
							|| tipoPendencia == PendenciaTipoDt.INTIMACAO_AUDIENCIA || tipoPendencia == PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA) {
						if (pendenciaDt.getId_ProcessoParte() == null || pendenciaDt.getId_ProcessoParte().length() == 0) {
							request.setAttribute("MensagemErro", "Esta pendência está inconsistente. Não foi possível identificar a parte vinculada. Entre em contato com o Suporte e informe o número da pendência e a parte que deve ser vinculada para ser possível a expedição.");
						}
					}

					if (!UsuarioSessao.isAssessor() && !UsuarioSessao.isAssessorAdvogado() && !UsuarioSessao.isAssessorMP() && pendenciaDt.isEmAndamento() ){					
						pendenciaNe.distribuir(pendenciaDt.getId(), UsuarioSessao.getUsuarioDt().getId_UsuarioServentia(), UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt());
					}
					// consulta serventia responsavel pelo processo se existir uma
					if (!pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO)) && !pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO)) && pendenciaDt.getPendenciaTipoCodigo().equals(PendenciaTipoDt.OFICIO_DELEGACIA)) {

						ServentiaCargoDt aux = pendenciaNe.consultaAutoridadePolicialResponsavelProcesso(pendenciaDt.getId_Processo());
						if (aux != null) {
							serventiaDt.setId(aux.getId_Serventia());
							serventiaDt.setServentia(aux.getServentia());
						} else
							serventiaDt = null;
					}
					
					
					// Se a pendencia esta finalizada
					// if ( pendenciaNe.verificaFinalizada(pendenciaDt) ){
					// //Modifica o fluxo para visualizar a pendencia fechada
					// stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
					// this.visualizarPendencia(request, pendenciaNe,
					// pendenciaDt, UsuarioSessao);
					//
					// } else{

					// TELAS PARA RESOLVER PENDÊNCIAS
					request.setAttribute("PaginaAtual", Configuracao.Salvar);
					stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
					
					this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, carregarArquivoTipo);
					// }
				} else if (pendenciaDt.getId() != null && !pendenciaDt.getId().equals("") && UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())) {
					// Se a pendencia esta finalizada
					// if ( pendenciaNe.verificaFinalizada(pendenciaDt) ){
					// //Modifica o fluxo para visualizar a pendencia fechada
					// stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
					// this.visualizarPendencia(request, pendenciaNe,
					// pendenciaDt, UsuarioSessao);
					//
					// } else{
					// TELAS PARA RESOLVER PENDÊNCIAS
					request.setAttribute("PaginaAtual", Configuracao.Salvar);
					stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
					this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, carregarArquivoTipo);
					// }

				} else
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
			}
			
			
			// INÍCIO --- DEFINIÇÃO DE PARÂMETROS USADOS NA RESOLUÇÃO DE MANDADOS DA CENTRAL DO PROJUDI --------------------------------------------------------------
			// Se for um oficial fechando um mandado, não carrega o tipo do arquivo.
			if(UsuarioSessao.isOficial() && pendenciaDt.isMandado()) {
				carregarArquivoTipo = false;
				
				// Verifica no conteúdo do documento a quantidade de locomoção utilizada.
				if( pendenciaNe.teraOficialCompanheiroMandado(pendenciaDt.getId()) ){
					request.setAttribute("temOficialCompanheiro", "sim");
				}
			}
			
			// Faz uma verificação prévia na pendência de mandado para alertar caso algum
			// critério obrigatório para ele ser expedido não seja cumprido. Se for o caso,
			// avisa o usuário logo quando abrir a pendência.
			if( pendenciaDt.eMandado() ){
				String mensagem = "";
				mensagem = pendenciaNe.verificarPreviaResolverPendencia(UsuarioSessao.getUsuarioDt(), pendenciaDt, UsuarioSessao.getId_Serventia());
				
				if (!mensagem.equalsIgnoreCase("")) {
					request.setAttribute("MensagemErro", mensagem);
				} 
				else if(pendenciaNe.temCentralProjudiImplantada(UsuarioSessao.getId_Serventia()) && Funcoes.StringToInt(pendenciaDt.getNumeroReservadoMandadoExpedir()) == 0 ) {
					//Reservar préviamente o número do mandado que será expedido. Isto é necessário para que se use uma
					// variável com o número do mandado na redação do documento. O TESTE ACIMA GARANTE QUE SERÁ RESERVADO
					// O NÚMERO APENAS PARA SERVENTIAS ÀS QUAIS A COMARCA TIVER CENTRAL DE MANDADOS DO PROJUDI IMPLANTADA.
					pendenciaDt.setNumeroReservadoMandadoExpedir(pendenciaNe.reservarNumeroProximoMandado());
				}
			}
			
			
			// Se a pendência é um mandado e a serventia do usuário logado possui serventia relacionada, na primeira vez que abrir a tela utiliza a serventia relacionada
			// e a partir daí utiliza as que o usuário selecionar.
			if (serventiaDt != null && serventiaDt.getId().equals("") && !pendenciaNe.isCentralMandados(UsuarioSessao.getId_Serventia(), null) && pendenciaNe.temCentralProjudiImplantada(UsuarioSessao.getId_Serventia())  &&
					pendenciaDt != null && 	pendenciaDt.isMandado() &&	(serventiaExpedirDt.getId() == null  || serventiaExpedirDt.getId() == "") ) {
				ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		        ServentiaDt centralMandadosRelacionada = serventiaRelacionadaNe.consultarCentralMandadosRelacionada(UsuarioSessao.getUsuarioDt().getId_Serventia());
		        if(centralMandadosRelacionada != null && pendenciaNe.temCentralProjudiImplantada(UsuarioSessao.getId_Serventia())) {
		        	serventiaExpedirDt.setId(centralMandadosRelacionada.getId());
		        	serventiaExpedirDt.setServentia(centralMandadosRelacionada.getServentia());
		        	serventiaExpedirDt.setServentiaTipoCodigo(centralMandadosRelacionada.getServentiaTipoCodigo());
		        	request.setAttribute("Id_ServentiaExpedir", serventiaExpedirDt.getId());
					request.setAttribute("ServentiaExpedir", serventiaExpedirDt.getServentia());					
		        }
			}
			
			
			//Se estiver expedindo um mandado para uma Central de Mandados
			if (serventiaExpedirDt.getId() != null && !serventiaExpedirDt.getId().equals("") && 
					serventiaExpedirDt.getServentiaTipoCodigo().equalsIgnoreCase(String.valueOf(ServentiaTipoDt.CENTRAL_MANDADOS))) {
				MandadoTipoNe mandadoTipoNe = new MandadoTipoNe();
				pendenciaDt.setListaTiposMandados(mandadoTipoNe.consultarListaTiposMandados());
				request.setAttribute("exibirMandadoTipo", pendenciaDt.getListaTiposMandados());
				if(request.getParameter("Id_OficialAdhoc") != null){
					request.setAttribute("temadhoc", "sim");
				}
			}						
			// FIM --- DEFINIÇÃO DE PARÂMETROS USADOS NA RESOLUÇÃO DE MANDADOS DA CENTRAL DO PROJUDI --------------------------------------------------------------
			
			break;
		case Configuracao.Novo:	// MARCAR INTIMAÇÃO E CITAÇÃO LIDO

			String opPendenciaTI = "";
			if (request.getParameter("operacao") != null && !request.getParameter("operacao").equalsIgnoreCase("Confirmar")) {
				opPendenciaTI = request.getParameter("operacao");
			} else if (request.getParameter("ultimaOperacaoPendencia") != null && !request.getParameter("ultimaOperacaoPendencia").equals("")) {
				opPendenciaTI = (String) request.getParameter("ultimaOperacaoPendencia");
			}

			if (opPendenciaTI.equalsIgnoreCase("MarcarLido")) {
				if (UsuarioSessao.VerificarCodigoHash(idPendencia, hash)) {
					pendenciaDt = pendenciaNe.marcarLido(idPendencia, pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), UsuarioSessao.getUsuarioDt(), false);
					stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
					// Consultar pendencia para atualizar e mostrar na tela com data fim
					if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
						pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
					else
						pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
					
					this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			} else if (opPendenciaTI.equalsIgnoreCase("MarcarLidoAguardandoParecer")) {
				if (UsuarioSessao.VerificarCodigoHash(idPendencia, hash)) {
					pendenciaDt = pendenciaNe.marcarLido(idPendencia, pendenciaDt.getId_UsuarioLog(), pendenciaDt.getIpComputadorLog(), UsuarioSessao.getUsuarioDt(), true);
					stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
					// Consultar pendencia para atualizar e mostrar na tela com data fim
					if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
						pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
					else
						pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
					
					this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}

			break;
		case Configuracao.Imprimir:

			if (request.getParameter("OperacaoExpedirImprimir") != null && request.getParameter("OperacaoExpedirImprimir").equalsIgnoreCase("ExpedirImprimir")) {
				request.setAttribute("OperacaoExpedirImprimir", "");

				// Verifica se possui arquivos na sessao
				List arquivos = Funcoes.converterMapParaList((HashMap) request.getSession().getAttribute("ListaArquivos"));

				if (arquivos == null || arquivos.size() == 0) return;

				// Emissão dos arquivos em PDF
				byte[] arquivoPDFCompleto = null;
				Iterator itArquivos = arquivos.iterator();

				boolean isPrimeiroArquivo = true;
				int contArquivo = 1;
				while (itArquivos.hasNext()) {

					ArquivoDt arquivoDt = (ArquivoDt) itArquivos.next();
					CMSSignedData dados = new CMSSignedData(arquivoDt.conteudoBytes());
					CMSProcessable conteudo = dados.getSignedContent();

					ByteArrayOutputStream arquivoSemAssinatura = new ByteArrayOutputStream();
					conteudo.write(arquivoSemAssinatura);

					byte arquivoByteArrayPDF[] = null;
					if (arquivoDt.isArquivoHtml()) {
						arquivoByteArrayPDF = ConverterHtmlPdf.converteHtmlPDF(arquivoSemAssinatura.toByteArray(), false);
					
					} else if (arquivoDt.isArquivoPDF()) {
						arquivoByteArrayPDF = arquivoSemAssinatura.toByteArray();
					}
					
					if (arquivoByteArrayPDF != null) {
						if (isPrimeiroArquivo) {
							arquivoPDFCompleto = GerarCabecalhoProcessoPDF.geraCabecalhoArquivoPDF(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , arquivoDt, UsuarioSessao.getUsuarioDt().getNome(), pendenciaDt.getProcessoNumero(), arquivoByteArrayPDF, contArquivo);

						} else{
							arquivoByteArrayPDF = GerarCabecalhoProcessoPDF.geraCabecalhoArquivoPDF(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , arquivoDt, UsuarioSessao.getUsuarioDt().getNome(), pendenciaDt.getProcessoNumero(), arquivoByteArrayPDF, contArquivo);
							arquivoPDFCompleto = ConcatenatePDF.concatenaPdf(arquivoPDFCompleto, arquivoByteArrayPDF);
						}
					}
					isPrimeiroArquivo = false;
					contArquivo += 1;
				}

				if (arquivoPDFCompleto.length > 0) {
					// Modifica o cabeçalho
					enviarPDF(response, arquivoPDFCompleto, "ArquivoPDF_Pendencia_");										
					
					// Limpa as listas de arquivos
					arquivos.clear();
					request.getSession().removeAttribute("ListaArquivos");					
					return;
				}
			} else {
				// REABRIR PENDÊNCIA
				String op = request.getParameter("operacao");
				// Verifica se teve a ultima operacao
				if (!ultimaOperacao.equals("") && (op == null || op.equals(""))) {
					operacao = ultimaOperacao;
				}

				if (op != null && op.equals("Confirmar")) {
					stAcao = "/WEB-INF/jsptjgo/PendenciaReabrir.jsp";
					ultimaOperacao = "Reabrir";

					// Se recebeu o pendencia pai
					if (request.getParameter("Id_PendenciaPai") != null) {
						pendenciaDt.setId_PendenciaPai(request.getParameter("Id_PendenciaPai"));
					}

					// Chama o controle para reabrir a pendencia
					pendenciaDt = this.reabrir(request, true, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, modeloDt, serventiaDt, serventiaCargoDt, UsuarioSessao);

					limparArquivosSessao(request);
					// Se depois de reabrir nao possui id da pendencia pai
					if (pendenciaDt.getId_PendenciaPai() == null || pendenciaDt.getId_PendenciaPai().trim().equals("")) {
						stAcao = "/WEB-INF/jsptjgo/PendenciaMinhas.jsp";
						this.listarMinhas(request, pendenciaNe, pendenciaTipoDt, pendenciaStatusDt, UsuarioSessao, PosicaoPaginaAtual);
					}
				}
			}

			break;
		case Configuracao.LocalizarDWR:
			// PERMITE ACESSO (MOVIMENTAR E MARCAR AUDIÊNCIA) A PROCESSO EXTERNO A SERVENTIA.

			if (acessoExterno(pendenciaDt)) {
				if (request.getParameter("operacao") != null && request.getParameter("operacao").toString().equalsIgnoreCase("MarcarAudiencia")) {
					request.getSession().setAttribute("ProcessoOutraServentia", pendenciaDt.getId_Processo());
					request.getSession().setAttribute("AcessoOutraServentia", pendenciaDt.getPendenciaTipoCodigo());
					redireciona(response, "Audiencia?PaginaAtual=" + Configuracao.Curinga9 + "&Id_Processo= " + pendenciaDt.getId_Processo());

				} else {
					request.getSession().setAttribute("AcessoOutraServentia", pendenciaDt.getPendenciaTipoCodigo());
					request.getSession().setAttribute("ProcessoOutraServentia", pendenciaDt.getId_Processo());
					redireciona(response, "BuscaProcesso?Id_Processo=" + pendenciaDt.getId_Processo());
				}

			} else {
				if (request.getParameter("operacao") != null && request.getParameter("operacao").toString().equalsIgnoreCase("MarcarAudiencia")) {
					// TELAS PARA RESOLVER PENDÊNCIAS
					this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, false);
					request.setAttribute("MensagemErro", "Acesso a processo externo a serventia negado.");
					request.setAttribute("PaginaAtual", Configuracao.Editar);
					stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);

				} else {
					redireciona(response, "BuscaProcesso?Id_Processo=" + pendenciaDt.getId_Processo());
				}
			}

			break;
		case Configuracao.Localizar:
			// MOVIMENTAR PROCESSO A PARTIR DE UMA PENDENCIA DE PROCESSO OU APARTIR DE UM PROCESSO

			// pendenciaDt.setId(request.getParameter("Id_Pendencia"));

			if (UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())) {

				if (pendenciaDt.getId() != null && !pendenciaDt.getId().equals("")) {
					// pendenciaDt = pendenciaNe.consultarId(pendenciaDt.getId());

					boolean transferirControle = false;
					boolean acessoExterno = false;

					if (request.getParameterValues("arquivos[]") != null && request.getParameterValues("arquivos[]").length > 0) {
						List arquivosTransferencia = new ArrayList();
						Map arquivosTransferenciaMap = new HashMap();
						String[] arquivosTransf = (String[]) request.getParameterValues("arquivos[]");

						for (int i = 0; i < arquivosTransf.length; i++) {
							ArquivoDt arquivoDt = pendenciaNe.consultarArquivoPendenciaId(arquivosTransf[i]);
							arquivosTransferencia.add(arquivoDt);
							arquivosTransferenciaMap.put(arquivoDt.getId(), arquivoDt);
						}
						request.getSession().setAttribute("arquivosTransferencia", Funcoes.converterListParaSet(arquivosTransferencia));
						request.getSession().setAttribute("arquivosTransferenciaMap", arquivosTransferenciaMap);
						transferirControle = true;
					}

					if (acessoExterno(pendenciaDt)) {
						acessoExterno = true;
						request.getSession().setAttribute("AcessoOutraServentia", pendenciaDt.getPendenciaTipoCodigo());
						request.getSession().setAttribute("ProcessoOutraServentia", pendenciaDt.getId_Processo());

					}

					// Se for para transferir o controle
					if (transferirControle || acessoExterno) {
						if (UsuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ADVOGADO_PARTICULAR))) redireciona(response, "Peticionamento?PaginaAtual=" + Configuracao.Novo);
						else {
							if ((pendenciaDt.getDataFim() != null && !pendenciaDt.getDataFim().equals("")) && (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().equals("")) && (!acessoExterno(pendenciaDt))) pendenciaNe.marcarVistoPendenciaPrazo(pendenciaDt, UsuarioSessao.getUsuarioDt());
							redireciona(response, "Movimentacao?PaginaAtual=" + Configuracao.Novo + (!pendenciaDt.getId_Processo().equals("") ? "&Id_Processo=" + pendenciaDt.getId_Processo() : ""));
						}

						// Quebra a logica do controle de pendencia
						return;

					} else {
						if (!acessoExterno(pendenciaDt) && request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("MovimentarProcessoExterno")) {
							this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, false);
							request.setAttribute("MensagemErro", "Acesso a processo externo a serventia negado.");
							request.setAttribute("PaginaAtual", Configuracao.Editar);
							stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);

						} else {
							if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("MovimentarProcessoPendencia")) stAcao = "/WEB-INF/jsptjgo/PendenciaDetalhada.jsp";
							else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("MovimentarProcesso")) stAcao = "/WEB-INF/jsptjgo/PendenciaServentiaDetalhada.jsp";
							else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("MovimentarProcessoPendenciaPrazoDecorrido")) stAcao = "/WEB-INF/jsptjgo/PendenciaServentiaPrazoDecorridoDetalhada.jsp";
							request.setAttribute("MensagemErro", "Selecione pelo menos um arquivo para movimentar o processo");
							this.detalharPendencia(request, idPendencia, pendenciaNe, pendenciaDt, UsuarioSessao);
						}
					}
				} else {
					throw new MensagemException("Não há pendência sendo visualizada.");
				}
			} else
				throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");

			break;
		case Configuracao.Salvar:
			if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("Reabrir")) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaReabrir.jsp";
				ultimaOperacao = "Reabrir";
				// Se recebeu o pendencia pai
				if (request.getParameter("Id_PendenciaPai") != null) {
					pendenciaDt.setId_PendenciaPai(request.getParameter("Id_PendenciaPai"));
				}

				// Chama o controle para reabrir a pendencia
				pendenciaDt = this.reabrir(request, false, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, modeloDt, serventiaDt, serventiaCargoDt, UsuarioSessao);

				request.setAttribute("PaginaAtual", Configuracao.Imprimir);// permissao imprimir esta sendo usada para reabrir pendencia
				request.getSession().setAttribute("ultimaAba", 0);
				request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
				request.setAttribute("Mensagem", "Para confirmar a operação \"" + request.getParameter("operacao") + "\" clique no botão \"Confirmar\".");

				// } else if ( pendenciaNe.verificaFinalizada(pendenciaDt)
				// ){//Se a pendencia esta finalizada
				// //Modifica o fluxo para visualizar a pendencia fechada
				// stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
				// this.visualizarPendencia(request, pendenciaNe, pendenciaDt,
				// UsuarioSessao);

			} else {
				this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, false);
				// Se tem operacao
				if (request.getParameter("operacao") != null) {
					// Se tem operacao de anexar
					if (request.getParameter("operacao").equals("Anexar")) {
						this.salvaResolucao(request, pendenciaNe, pendenciaDt, arquivoTipoDt, UsuarioSessao, comarcaDt, serventiaExpedirDt, serventiaTipoDt);
						serventiaDt.limpar();
						serventiaTipoDt.limpar();
						serventiaExpedirDt.limpar();
						serventiaCargoDt.limpar();
						
						request.setAttribute("Id_ServentiaExpedir",null);
						request.setAttribute("Id_ServentiaTipo",null);
						
						request.setAttribute("PaginaAtual", Configuracao.Salvar);
						if(UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.ESTAGIARIO))) {
							serventiaDt.setId(UsuarioSessao.getUsuarioDt().getId_Serventia());
							this.encaminhar(request, pendenciaNe, pendenciaDt, serventiaDt, serventiaCargoDt, UsuarioSessao);
							// Modifica o fluxo para visualizar a pendencia fechada
							stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
							if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
								pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
							else
								pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
							this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);
						}
					} else if (request.getParameter("operacao").equalsIgnoreCase("FinalizarElaboracaoVoto")) {
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
						request.getSession().setAttribute("ultimaAba", 1);
						request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
						request.setAttribute("Mensagem", "Para confirmar a operação \"Finalizar Elaboração de Voto NÃO Movimentando Processo\" clique no botão \"Confirmar\".");
						
					} else if (request.getParameter("operacao").equalsIgnoreCase("FinalizarElaboracaoVotoMovimentandoProcesso")) {
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
						request.getSession().setAttribute("ultimaAba", 1);
						request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
						request.setAttribute("Mensagem", "Para confirmar a operação \"Finalizar Elaboração de Voto Movimentando Processo\" clique no botão \"Confirmar\".");
						
					} else if (request.getParameter("operacao").equalsIgnoreCase("Responder") || request.getParameter("operacao").equalsIgnoreCase("Expedir") || request.getParameter("operacao").equalsIgnoreCase("ExpedirImprimir") || request.getParameter("operacao").equalsIgnoreCase("Distribuir") || request.getParameter("operacao").equalsIgnoreCase("DistribuirImprimir")) {
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
						request.getSession().setAttribute("ultimaAba", 1);
						request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
						String stSalvarOperacao = request.getParameter("operacao");
						if (stSalvarOperacao.equals("ExpedirImprimir") || stSalvarOperacao.equals("DistribuirImprimir")) stSalvarOperacao = "Expedir e Imprimir";
						request.setAttribute("Mensagem", "Para confirmar a operação clique no botão \"Confirmar\".");

					} else if (request.getParameter("operacao").equalsIgnoreCase("Concluir")) {
						if (!pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("")) {
							request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
							request.getSession().setAttribute("ultimaAba", 1);
							request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
							request.setAttribute("Mensagem", "Para confirmar a operação \"" + request.getParameter("operacao") + "\" clique no botão \"Confirmar\".");
						} else {
							request.setAttribute("PaginaAtual", Configuracao.Salvar);
							stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoPegar.jsp";
							request.getSession().setAttribute("ultimaAba", 1);
							request.setAttribute("MensagemErro", "Selecione o status da pendência");
						}

					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("FinalizarMovimentar")) {
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);

					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("Finalizar")) {
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);

					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("Encaminhar")) {
						request.getSession().setAttribute("ultimaAba", 2);

						String msgValidacao = pendenciaNe.verificarEncaminhar(pendenciaDt, serventiaDt, serventiaCargoDt);
						if (msgValidacao.length() == 0) {
							request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
							request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
							request.setAttribute("Mensagem", "Para confirmar a operação \"" + request.getParameter("operacao") + "\" clique no botão \"Confirmar\".");
						} else {
							request.setAttribute("MensagemErro", msgValidacao);
							request.setAttribute("PaginaAtual", Configuracao.Salvar);
						}
					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("Efetuar Troca")) {
						request.getSession().setAttribute("ultimaAba", 3);

						String msgValidacao = pendenciaNe.verificarAlteracaoPendenciaTipo(pendenciaDt, codPendenciaTipo);
						if (msgValidacao.length() == 0) {
							request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
							request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
							request.setAttribute("Mensagem", "Para confirmar a operação \"" + request.getParameter("operacao") + "\" clique no botão \"Confirmar\".");
						} else {
							request.setAttribute("MensagemErro", msgValidacao);
							request.setAttribute("PaginaAtual", Configuracao.Salvar);
						}

					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("NaoExpedir")) {
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
						request.getSession().setAttribute("ultimaAba", 1);
						request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
						request.setAttribute("Mensagem", "Para confirmar a operação \"Não Expedir\" \" clique no botão \"Confirmar\".");

					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("Confirmar")) {
						request.setAttribute("Mensagem", "Finalizar pendência ou Finalizar e Movimentar o processo.");
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
					
					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("ConfirmarRealizacaoCarga")) {
						if (Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) != PendenciaStatusDt.ID_AGUARDANDO_RETORNO) {
							request.setAttribute("Mensagem", "Finalizar Realizando a Carga do Processo.");
							request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
						} else {
							request.setAttribute("Mensagem", "Finalizar Realizando a Devolução dos Autos.");
							request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
						}
					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("ConfirmarMarcandoAudiencia")) {
						request.setAttribute("Mensagem", "Finalizar pendência ou Finalizar e Marcar Audiência.");
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);

					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("ConfirmarAverbacaoCustas")) {
						request.setAttribute("Mensagem", "Finalizar pendência ou Finalizar e realizar a Averbação de Custas.");
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);

					}else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("ConfirmarOficioNotificacao")) {
						request.setAttribute("Mensagem", "Finalizar pendência ou Finalizar e Gerar Ofício Comunicatório.");
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);

					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("ConfirmarRedistribuir")) {
						request.setAttribute("Mensagem", "Finalize a pendência com opção desejada.");
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("GuardarParaAssinar")) {
						request.setAttribute("Mensagem", "Para confirmar a operação \"Guardar para Assinar\" \" clique no botão \"Confirmar\".");
						request.getSession().setAttribute("ultimaAba", 1);
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
						request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
					}
				}
				
				request.setAttribute("exibirMandadoTipo", pendenciaDt.getListaTiposMandados());

				// TELAS PARA RESOLVER PENDÊNCIAS
				stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
			}

			break;
		case Configuracao.SalvarResultado:
			// RESPONDER / EXPEDIR; NÃO EXPEDIR; ENCAMINHAR; FINALIZAR; FINALIZAR MOVIMENTANDO; CONCLUIR AGUARDANDO RETORNO

			if (UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())) {
				// Verifica se possui arquivos na sessao
				List arquivos = Funcoes.converterMapParaList((HashMap) request.getSession().getAttribute("ListaArquivos"));
				String opPendencia = "";

				if (request.getParameter("operacao") != null && !request.getParameter("operacao").equalsIgnoreCase("Confirmar")) {
					opPendencia = request.getParameter("operacao");
				} else if (request.getParameter("ultimaOperacaoPendencia") != null && !request.getParameter("ultimaOperacaoPendencia").equals("")) {
					opPendencia = (String) request.getParameter("ultimaOperacaoPendencia");
				}

				if (opPendencia.equalsIgnoreCase("Concluir")) {
					if (!pendenciaDt.getStatusPendenciaRetorno().equalsIgnoreCase("")) {
						String msgValidacao = pendenciaNe.validarArquivosPendencia(pendenciaDt, arquivos);
						if (pendenciaDt.getId() == null && pendenciaDt.getId().equalsIgnoreCase("")) {
							msgValidacao += " Pendência não encontrada! \n";
						}
						
						msgValidacao +=  pendenciaNe.verificarAlvaraSoltura(pendenciaDt);
								
						if (msgValidacao.length() == 0) {
							pendenciaNe.concluirAguardandoRetorno(pendenciaDt, UsuarioSessao.getUsuarioDt(), arquivos);
							// Modifica o fluxo para visualizar a pendencia
							// fechada
							stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
							if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
								pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
							else
								pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
							this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);
						} else {
							request.setAttribute("MensagemErro", msgValidacao);
							request.setAttribute("PaginaAtual", Configuracao.Salvar);
							// TELAS PARA RESOLVER PENDÊNCIAS
							stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
							this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, false);
						}

					} else {
						request.setAttribute("PaginaAtual", Configuracao.SalvarResultado);
						stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoPegar.jsp";
						request.getSession().setAttribute("ultimaAba", 1);
						request.setAttribute("MensagemErro", "Selecione o status da pendência");
					}

				} else if (opPendencia.equalsIgnoreCase("NaoExpedir")) {
					pendenciaNe.naoExpedirComArquivos(pendenciaDt, UsuarioSessao.getUsuarioDt(), arquivos);
					// Modifica o fluxo para visualizar a pendencia fechada
					stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
					if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
						pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
					else
						pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
					this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);

				} else if (opPendencia.equals("Responder") || opPendencia.equals("Expedir") || opPendencia.equals("ExpedirImprimir")) {
					
					boolean isParaCentralMandados = false;
					// Verifica se está expedindo para uma Central de Mandados
					if( serventiaExpedirDt != null && pendenciaNe.isCentralMandados(serventiaExpedirDt.getId(), null) ){
						isParaCentralMandados = true;
					}
					
					// Se for mandado, o prazo é determinado de maneira específica. Se o prazo for Especial, o prazo
					// será especificado na tela escolhendo manualmente o número de dias. Se for qualquer outro tipo, utilizar um prazo pré-cadastrado
					// no MandadoTipoDt.
					if(pendenciaDt != null && Funcoes.StringToInt(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.MANDADO && isParaCentralMandados) {
						
						// Determina o prazo limite para o mandados da Central
						pendenciaDt.setDataLimite(pendenciaNe.retornaPrazoLimiteMandado(pendenciaDt.getCodigoPrazoMandado(), pendenciaDt.getPrazoMandado(), serventiaExpedirDt.getId()));
					}
					
					String msgValidacao = pendenciaNe.verificarExpedir(UsuarioSessao.getUsuarioDt(), serventiaExpedirDt, pendenciaDt, arquivos);
					if (msgValidacao.length() == 0) {
						pendenciaDt.setIdModelo((String) request.getSession().getAttribute("Id_Modelo"));
						pendenciaNe.responderPendencia(pendenciaDt, UsuarioSessao.getUsuarioDt(), arquivos, serventiaExpedirDt);
						request.setAttribute("MensagemOk", "Pendência respondida com sucesso");

						// Se for para Imprimir, iremos atualizar o response para fazer um submit automático para gerar os PDFs
						if (opPendencia.equals("ExpedirImprimir")) {
							request.setAttribute("PaginaAtual", Configuracao.Imprimir);
							request.setAttribute("OperacaoExpedirImprimir", "ExpedirImprimir");
						}

						// Modifica o fluxo para visualizar a pendencia fechada
						stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
						request.getSession().removeAttribute("AcessoOutraServentia");
						if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
							pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
						else
							pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
						this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);
					} else {
						request.setAttribute("MensagemErro", msgValidacao);
						request.setAttribute("PaginaAtual", Configuracao.Salvar);
						// TELAS PARA RESOLVER PENDÊNCIAS
						stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
						this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, false);
					}

				} else if (opPendencia.equals("Distribuir") || opPendencia.equals("DistribuirImprimir")) {
					String msgValidacao = pendenciaNe.verificarExpedir(UsuarioSessao.getUsuarioDt(), serventiaExpedirDt, pendenciaDt, arquivos);
					if (msgValidacao.length() == 0) {
						pendenciaNe.distribuirCartaPrecatoria(pendenciaDt, UsuarioSessao.getUsuarioDt(), arquivos, comarcaDt);
						request.setAttribute("MensagemOk", "Pendência respondida com sucesso");

						// Se for para Imprimir, iremos atualizar o response para fazer um submit automático para gerar os PDFs
						if (opPendencia.equals("DistribuirImprimir")) {
							request.setAttribute("PaginaAtual", Configuracao.Imprimir);
							request.setAttribute("OperacaoExpedirImprimir", "ExpedirImprimir");
						}

						// Modifica o fluxo para visualizar a pendencia fechada
						stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
						request.getSession().removeAttribute("AcessoOutraServentia");
						if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
							pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
						else
							pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
						this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);
					} else {
						request.setAttribute("MensagemErro", msgValidacao);
						request.setAttribute("PaginaAtual", Configuracao.Salvar);
						// TELAS PARA RESOLVER PENDÊNCIAS
						stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
						this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, false);
					}

				} else if (opPendencia.equals("FinalizarMovimentar")) {
					pendenciaNe.responder(pendenciaDt, UsuarioSessao.getUsuarioDt(), PendenciaStatusDt.ID_CUMPRIDA);
					// Modifica o fluxo para o ct de movimentacao
					redireciona(response, "Movimentacao?PaginaAtual=4&Id_Processo=" + pendenciaDt.getId_Processo() + "&MensagemOk=Pendência Finalizada com sucesso. Movimente o processo.");
					return;
				
				} else if (opPendencia.equals("FinalizarPendenciaSolicitacaoCargaProcesso")) {
					if (Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) != PendenciaStatusDt.ID_AGUARDANDO_RETORNO) {
						if (pendenciaNe.possuiPendeciaSolicitarCargaAguardandoRetornoProcesso(pendenciaDt.getId_Processo())){
							redireciona(response, "BuscaProcesso?Id_Processo=" + pendenciaDt.getId_Processo() + "&MensagemErro=Não é possível realizar a carga do processo, pois o mesmo está aguardando devolução dos Autos ");
						} else {
							pendenciaNe.realizarCargaProcesso(pendenciaDt, UsuarioSessao.getUsuarioDt());
							//redireciona(response, "BuscaProcesso?Id_Processo=" + pendenciaDt.getId_Processo() + "&MensagemOk= A carga foi realizada com sucesso para " + pendenciaDt.getNomeUsuarioCadastrador());
							stAcao = "/WEB-INF/jsptjgo/ReciboCarga.jsp";
							request.setAttribute("MensagemOk", "A carga foi realizada com sucesso para " + pendenciaDt.getNomeUsuarioCadastrador());
						}
					} else {
						pendenciaNe.realizarDevolucaoAutos(pendenciaDt, UsuarioSessao.getUsuarioDt());
						redireciona(response, "BuscaProcesso?Id_Processo=" + pendenciaDt.getId_Processo() + "&MensagemOk= A Devolução dos Autos foi realizada com sucesso!");
					}
				
				} else if (opPendencia.equals("FinalizarMarcandoAudiencia")) {
					pendenciaNe.responder(pendenciaDt, UsuarioSessao.getUsuarioDt(), PendenciaStatusDt.ID_CUMPRIDA);
					// Modifica o fluxo para o ct de audiencia
					// redireciona(response, "Movimentacao?PaginaAtual=4&Id_Processo="+pendenciaDt.getId_Processo()+"&MensagemOk=Pendência Finalizada com sucesso. Movimente o processo.");
					if ( pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC || 
					     pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT || 
						 pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC){
						request.getSession().setAttribute("ProcessoOutraServentia", pendenciaDt.getId_Processo());
						request.getSession().setAttribute("AcessoOutraServentia", pendenciaDt.getPendenciaTipoCodigo());
					}
					redireciona(response, "Audiencia?PaginaAtual=" + Configuracao.Curinga9 + "&Id_Processo= " + pendenciaDt.getId_Processo() + "&MensagemOk=Pendência Finalizada com sucesso. Marque a audiência.");
					return; // FinalizarGerandoOficioNotificacao

				} else if (opPendencia.equals("FinalizarAverbacaoCustas")) {
					//pendenciaNe.responder(pendenciaDt, UsuarioSessao.getUsuarioDt(), PendenciaStatusDt.Cumprida);
					// Modifica o fluxo para o ct de  Processo Parte Debito
					redireciona(response, "ProcessoParteDebito?PaginaAtual=" + Configuracao.Editar + "&PassoEditar=2&Id_Processo= " + pendenciaDt.getId_Processo() + "&MensagemOk=Pendência Finalizada com sucesso. Faça a Averbação de Custas.");
					return; 

				} else if (opPendencia.equals("FinalizarGerandoOficioNotificacao")) {
					pendenciaNe.responder(pendenciaDt, UsuarioSessao.getUsuarioDt(), PendenciaStatusDt.ID_CUMPRIDA);

					ProcessoDt processoDt = (ProcessoDt) pendenciaNe.consultarProcessoCompletoId(pendenciaDt.getId_Processo(), UsuarioSessao.getUsuarioDt(), false, UsuarioSessao.getNivelAcesso());
					request.getSession().setAttribute("processoDt", processoDt);
					redireciona(response, "DescartarPendenciaProcesso?PaginaAtual=" + Configuracao.Novo + "&fluxo=1&MensagemOk=Pendência Finalizada com sucesso. Gere o Ofício Comunicatório.");
					return;

				} else if (opPendencia.equals("FinalizarRedistribuir")) {
					pendenciaNe.responder(pendenciaDt, UsuarioSessao.getUsuarioDt(), PendenciaStatusDt.ID_CUMPRIDA);
					// Modifica o fluxo para o ct de
					redireciona(response, "Movimentacao?PaginaAtual=" + Configuracao.Novo + "&&RedirecionaOutraServentia=2&Id_Processo= " + pendenciaDt.getId_Processo() +"&MensagemOk=Pendência Finalizada com sucesso. Redistribua o processo.");
					return;
				
				} else if (opPendencia.equals("FinalizarPorConcluso")) {
					pendenciaNe.responder(pendenciaDt, UsuarioSessao.getUsuarioDt(), PendenciaStatusDt.ID_CUMPRIDA);
					// Modifica o fluxo para o ct de movimentacao
					redireciona(response, "Movimentacao?PaginaAtual=4&Id_Processo=" + pendenciaDt.getId_Processo() + "&MensagemOk=Pendência Finalizada com sucesso. Coloque o processo Concluso.");
					return;
				} else if (opPendencia.equals("Finalizar")) {
					pendenciaNe.finalizar(pendenciaDt, UsuarioSessao.getUsuarioDt());
					request.setAttribute("MensagemOk", "Pendência respondida com sucesso");
					// Modifica o fluxo para visualizar a pendencia fechada
					stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
					if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
						pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
					else
						pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
					this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);

				} else if (opPendencia.equals("FinalizarElaboracaoVotoMovimentandoProcesso")) {
					StringBuffer msgValidacao = new StringBuffer(""); 
					msgValidacao.append(pendenciaNe.validarArquivosPendencia(pendenciaDt, arquivos));
					
					AudienciaProcessoDt audienciaProcessoDt =  pendenciaNe.consultarAudienciaProcessoPendente(pendenciaDt.getId_Processo(), UsuarioSessao.getUsuarioDt());
					if (audienciaProcessoDt == null ||  audienciaProcessoDt.getId() == null || audienciaProcessoDt.getId().length() == 0 ){
						msgValidacao.append("\nProcesso "+pendenciaDt.getProcessoNumero()+" não consta em pauta!");
					}
					
					if (msgValidacao.length()==0)
						redireciona(response, "AudienciaProcessoMovimentacao?PaginaAtual=" + Configuracao.Novo + "&Id_AudienciaProcesso= " + audienciaProcessoDt.getId() + "&Id_Pendencia=" + pendenciaDt.getId() + "&ElaboracaoVoto=true");
					else {
						request.setAttribute("MensagemErro", msgValidacao.toString());
						request.setAttribute("PaginaAtual", Configuracao.Salvar);
						// TELAS PARA RESOLVER PENDÊNCIAS
						stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
						this.setAtributosPendencia(request, pendenciaNe, pendenciaDt, pendenciaTipoDt, arquivoTipoDt, UsuarioSessao, modeloDt, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, false);
					}

				} else if (opPendencia.equals("FinalizarElaboracaoVoto")) {
					pendenciaNe.finalizar(pendenciaDt, UsuarioSessao.getUsuarioDt());
					request.setAttribute("MensagemOk", "Pendência Finalizada com sucesso");
					// Modifica o fluxo para visualizar a pendencia fechada
					stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
					if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
						pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
					else
						pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
					this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);
					
				} else if (opPendencia.equals("Encaminhar")) {// Se for operacao de encaminhar
					// pendenciaDt.setId(idPendencia);
					this.encaminhar(request, pendenciaNe, pendenciaDt, serventiaDt, serventiaCargoDt, UsuarioSessao);
					// Modifica o fluxo para visualizar a pendencia fechada
					stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
					if (pendenciaDt.getDataVisto() == null || pendenciaDt.getDataVisto().length()==0)
						pendenciaDt = pendenciaNe.consultarPendenciaId(pendenciaDt.getId(), UsuarioSessao);
					else
						pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(pendenciaDt.getId(), UsuarioSessao);
					this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);
				} else if (opPendencia.equals("Efetuar Troca")) {// Se for operacao de Efetuar Troca
					this.alterarTipoPendencia(pendenciaNe, pendenciaDt, codPendenciaTipo);
					request.getSession().removeAttribute("ultimaAba");
					super.redireciona(response, "Pendencia?PaginaAtual=-1&pendencia="+pendenciaDt.getId()+"&fluxo=1&NovaPesquisa=true&CodigoPendencia="+pendenciaDt.getHash()+"&ManterMensagemOK=S&MensagemOk=Operação de efetuar troca do tipo de pendência realizada com sucesso.");
					
				} else if (opPendencia.equals("GuardarParaAssinar")) {					
					// Modifica o fluxo para visualizar a pendencia fechada
					if (this.salvaGuardarParaAssinar(request, pendenciaNe, pendenciaDt, UsuarioSessao, arquivos, comarcaDt,  serventiaExpedirDt, serventiaTipoDt))
					{
						request.getSession().removeAttribute("ultimaAba");
						redireciona(response, "Pendencia?PaginaAtual=" + Configuracao.Curinga7 + "&opcao=AbertasServentiaCargo&TipoPendencia=" + pendenciaDt.getPendenciaTipoCodigo() + "&ManterMensagemOK=S&MensagemOk=Operação guardar para assinar realizada com sucesso.");
						return;	
					}
					else
					{
						request.getSession().setAttribute("ultimaAba", 1);
						request.setAttribute("PaginaAtual", Configuracao.Salvar);
						request.setAttribute("ultimaOperacaoPendencia", request.getParameter("operacao"));
						// TELAS PARA RESOLVER PENDÊNCIAS
						stAcao = this.telasPendencia(request, pendenciaDt, pendenciaNe, UsuarioSessao);
					}
				}
			} else
				throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");

			break;
		case Configuracao.Curinga6:
			// ACOMPANHAMENTO DE PENDÊNCIAS DA SERVENTIA; PRAZO DECORRIDO DA SERVENTIA; DETALHAR PENDÊNCIA DA SERVENTIA; FINALIZADAS DA SERVENTIA; VISTAR PRAZO DECORRIDO E EXPEDIDAS DA SERVENTIA

			if (fluxo == 3) {
				if (UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())) {
					if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("VistarPendenciaPrazoDecorrido")) {
						// seta data visto
						pendenciaNe.visualizarPendenciaPrazoDecorrido(pendenciaDt, UsuarioSessao.getUsuarioDt());
						request.setAttribute("MensagemOk", "Pendência vista com sucesso.");
						fluxo = 2;

					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("VistarPendenciaExpedidaServentia")) {
						pendenciaNe.marcarVistoPendenciaPrazo(pendenciaDt, UsuarioSessao.getUsuarioDt());
						request.setAttribute("MensagemOk", "Pendência vista com sucesso.");
						fluxo = 1;

					}
				} else
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
			}

			if (fluxo == 1) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaExpedidas.jsp";
				this.listarExpedidasServentia(request, pendenciaNe, pendenciaTipoDt, pendenciaStatusDt, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Pendencias com prazos
			if (fluxo == 2) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaPrazoDecorrido.jsp";
				this.listarPrazosDecorridos(request, pendenciaNe, pendenciaTipoDt, UsuarioSessao, PosicaoPaginaAtual, false);
			}
			
			// Pendencias com prazos
			if (fluxo == 15) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaPrazoDecorridoDevolucaoAutos.jsp";
				this.listarPrazosDecorridosDevolucaoAutos(request, pendenciaNe, UsuarioSessao.getUsuarioDt(), PosicaoPaginaAtual);
			}

			// Pendencias com prazos
			if (fluxo == 6) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaPrazoADecorrer.jsp";
				this.listarPrazosDecorridos(request, pendenciaNe, pendenciaTipoDt, UsuarioSessao, PosicaoPaginaAtual, true);
			}

			// Pendencias para liberação de acesso a processos
			if (fluxo == 7) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaLiberarAcesso.jsp";
				request.setAttribute("ListaPendencia", pendenciaNe.consultarPendenciasLiberarAcessoComHash(UsuarioSessao));
			}

			// Detalhes da pendência de liberação de acesso
			if (fluxo == 8) {
				if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)) {
					pendenciaDt = pendenciaNe.consultarPendenciaId(idPendencia, UsuarioSessao);
					stAcao = "/WEB-INF/jsptjgo/PendenciaDetalhadaLiberacaoAcesso.jsp";
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}

			// Pendencias para liberação de acesso a processos
			if (fluxo == 12) {
				stAcao = "/WEB-INF/jsptjgo/PendenciasInformativas.jsp";
				request.setAttribute("ListaPendencia", pendenciaNe.consultarPendenciasInformativas(UsuarioSessao));
			}

			// Detalhes da pendência de liberação de acesso
			if (fluxo == 13) {
				if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)) {
					stAcao = "/WEB-INF/jsptjgo/PendenciaInformativaVisualizar.jsp";
					pendenciaDt = pendenciaNe.consultarPendenciaId(idPendencia, UsuarioSessao);
					this.visualizarPendencia(request, pendenciaNe, pendenciaDt, UsuarioSessao);
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}

			// Detalhes da pendência
			if (fluxo == 4) {
				if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)) {
					if (request.getParameter("op") != null && request.getParameter("op").toString().equalsIgnoreCase("PrazoDecorrido")) stAcao = "/WEB-INF/jsptjgo/PendenciaServentiaPrazoDecorridoDetalhada.jsp";
					else
						stAcao = "/WEB-INF/jsptjgo/PendenciaExpedidaServentiaDetalhada.jsp";

					pendenciaDt = this.detalhar(request, idPendencia, pendenciaNe, pendenciaDt, UsuarioSessao);
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}

			// Consulta pendencias finalizados da serventia
			if (fluxo == 5) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaFinalizadas.jsp";
				// fluxo = 9;//consulta pendencias finalizadas da serventia
				this.listarFinalizadas(request, fluxo, pendenciaNe, pendenciaTipoDt, pendenciaStatusDt, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Consulta pendencias respondidas da serventia
			if (fluxo == 10) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaRespondidas.jsp";
				this.listarRespondidasServentia(request, fluxo, pendenciaNe, pendenciaTipoDt, pendenciaStatusDt, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Detalhes da pendência respondidas
			if (fluxo == 11) {
				if ((idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash))) {
					stAcao = "/WEB-INF/jsptjgo/PendenciaRespondidaDetalhada.jsp";
					pendenciaDt = this.detalhar(request, idPendencia, pendenciaNe, pendenciaDt, UsuarioSessao);
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}

			request.setAttribute("PaginaAtual", paginaatual);

			break;
		case Configuracao.Curinga7:
			// ACOMPANHAMENTO DE PENDÊNCIAS DO USUARIO; PRAZO DECORRIDO DO USUARIO; RESERVADAS; PRÉ-ANALISADAS; DETALHAR PENDÊNCIA DO USUARIO; MINHAS(USUÁRIO CADASTROU); FINALIZADAS DO USUARIO; INTIMAÇÕES E CITAÇÕES LIDAS

			if (fluxo == 0 || fluxo == 1 || fluxo == 2) {

				if(UsuarioSessao.isOficial() && pendenciaNe.temCentralProjudiImplantada(UsuarioSessao.getId_Serventia())){
					//Tela específica de oficiais de justiça da Central de Mandados
					stAcao = "/WEB-INF/jsptjgo/PendenciaMandadosReservadosOficial.jsp";
				} else if (UsuarioSessao.isAdvogado() || UsuarioSessao.isAssessorAdvogado()) {
					stAcao = "/WEB-INF/jsptjgo/PendenciaPreAnalisadasAdvogado.jsp";
				} else if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU || UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.JUIZ_TURMA  || UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA || UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSESSOR_DESEMBARGADOR){ 
					if (request.getParameter("TipoPend") != null && request.getParameter("TipoPend").toString().equalsIgnoreCase("ElaboracaoVoto")){
						stAcao = "/WEB-INF/jsptjgo/PendenciaPreAnalisadasElaboracaoVoto.jsp";
						request.setAttribute("TipoPend", request.getParameter("TipoPend"));
					} else
						stAcao = "/WEB-INF/jsptjgo/PendenciaPreAnalisadas.jsp";
				} else
					stAcao = "/WEB-INF/jsptjgo/PendenciaReservadas.jsp";

				// Lista as pendencias reservadas do usuario para solucinor ou liberar
				this.listaReservadasUsuarioSolucionarLiberar(request, response, pendenciaNe, pendenciaDt, serventiaDt, UsuarioSessao, pendenciaTipoDt, request.getParameter("pendencia"), fluxo);

				if( (UsuarioSessao.getUsuarioDt().isJuizTurma()  || UsuarioSessao.getUsuarioDt().isAssessoJuizVaraTurma() ) 
						&& ( request.getParameter("TipoPend") != null )
						&& (request.getParameter("TipoPend").toString().equalsIgnoreCase("ElaboracaoVoto")) ){
					
					List listaReservas = (ArrayList<PendenciaDt>) request.getAttribute("ListaReservas");
					
					PendenciaDt pend = new PendenciaDt();
					for(int i= 0; listaReservas!=null && i < listaReservas.size(); i++ ){
						
						pend = (PendenciaDt) listaReservas.get(i);
						pend.setProcessoDt(pendenciaNe.consultarProcessoCompletoId(pend.getId_Processo(), UsuarioSessao.getUsuarioDt(), true, 2));
						
					}

					request.setAttribute("listaSessoesElaboracao", pendenciaNe.consultarDataSessoesElaboracaoDeVoto( UsuarioSessao));
					String isTurma = "true";					
					request.setAttribute("isTurma", isTurma);
				}
				
				
				
			}

			// Detalhes da pendência
			if (fluxo == 3) {
				if ((idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash))) {
					if (request.getParameter("op") != null && request.getParameter("op").toString().equalsIgnoreCase("PrazoDecorrido")) 
						stAcao = "/WEB-INF/jsptjgo/PendenciaPrazoDecorridoDetalhada.jsp";
					else
						stAcao = "/WEB-INF/jsptjgo/PendenciaDetalhada.jsp";

					pendenciaDt = this.detalhar(request, idPendencia, pendenciaNe, pendenciaDt, UsuarioSessao);
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}
			
			// Detalhes da pendência Finalizada
			if (fluxo == 17) {
				if ((idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash))) {
					
					pendenciaDt = this.detalharPendendiaFinalizada(request, idPendencia, pendenciaNe, pendenciaDt, UsuarioSessao);
					
					if (request.getParameter("op") != null && request.getParameter("op").toString().equalsIgnoreCase("PrazoDecorrido")) 
						stAcao = "/WEB-INF/jsptjgo/PendenciaPrazoDecorridoDetalhada.jsp";
					else if (pendenciaDt.getPendenciaTipoCodigo() != null && pendenciaDt.getPendenciaTipoCodigo().length()> 0 &&
								(Integer.valueOf(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_PRESIDENTE_TJGO || Integer.valueOf(pendenciaDt.getPendenciaTipoCodigo()) == PendenciaTipoDt.CONCLUSO_VICE_PRESIDENTE_TJGO) )
						stAcao = "/WEB-INF/jsptjgo/PendenciaHistoricoFinalizadaDetalhada.jsp";
					else	
						stAcao = "/WEB-INF/jsptjgo/PendenciaFinalizadaDetalhada.jsp";
					
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}


			if (fluxo == 6) {
				if (UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())) {
					if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("VistarPendenciaPrazoDecorrido")) {
						// seta data visto
						pendenciaNe.visualizarPendenciaPrazoDecorrido(pendenciaDt, UsuarioSessao.getUsuarioDt());
						request.setAttribute("MensagemOk", "Pendência vista com sucesso.");
						fluxo = 8;

					} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("VistarPendencia")) {
						pendenciaNe.marcarVistoPendenciaPrazo(pendenciaDt, UsuarioSessao.getUsuarioDt());
						request.setAttribute("MensagemOk", "Pendência vista com sucesso.");
						fluxo = 4;

					}
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}

			if (fluxo == 11) {
				if (UsuarioSessao.VerificarCodigoHash(pendenciaDt.getId(), pendenciaDt.getHash())) {
					if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("DescartarPendencia")) {
						if (pendenciaNe.verificarDescartarPendenciaNormal(pendenciaDt, UsuarioSessao.getUsuarioDt())) {
							pendenciaNe.descartarPendencia(pendenciaDt, UsuarioSessao.getUsuarioDt());
							request.setAttribute("MensagemOk", "Pendência descartada com sucesso.");
							fluxo = 4;
						} else {
							throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
						}
					}
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}

			// Pendencias que o usuario criou
			if (fluxo == 4) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaMinhas.jsp";
				this.listarMinhas(request, pendenciaNe, pendenciaTipoDt, pendenciaStatusDt, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Consulta pendencias que o usuario finalizou e visualizou
			if (fluxo == 5) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaUsuarioFinalizadas.jsp";
				this.listarFinalizadas(request, fluxo, pendenciaNe, pendenciaTipoDt, pendenciaStatusDt, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Pendencias intimacoes lidas
			if (fluxo == 7) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaFinalizadasResponsavel.jsp";
				this.listarIntimacoesLidas(request, pendenciaNe, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Pendencias com prazos minhas
			if (fluxo == 8) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaPrazoDecorridoUsuario.jsp";
				this.listarPrazosDecorridosUsuario(request, pendenciaNe, pendenciaTipoDt, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Consulta pendencias finalizados para movimentar processo
			if (fluxo == 9) {
				// ------- validação feita para os processos de cálculo de
				// liquidação de pena.
				ProcessoDt processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
				if (processoDt != null) {
					if (processoDt.getProcessoTipoCodigo().equals(String.valueOf(ProcessoTipoDt.CALCULO_DE_LIQUIDACAO_DE_PENA))) {
						String mensagem = "Não é possível realizar esta ação. Motivo: Processo físico!";
						redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + mensagem);
					}
				}
				// -------------------------------------------------------------------------
				if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ADVOGADO) {
					stAcao = "/WEB-INF/jsptjgo/PendenciaAdvogadoFinalizadas.jsp";
					// fluxo = 5;// para consutar apenas as do usuario
					// (advogado)
					this.listarFinalizadas(request, fluxo, pendenciaNe, pendenciaTipoDt, pendenciaStatusDt, UsuarioSessao, PosicaoPaginaAtual);
				} else {
					stAcao = "/WEB-INF/jsptjgo/PendenciaServentiaFinalizadas.jsp";
					this.listarFinalizadas(request, fluxo, pendenciaNe, pendenciaTipoDt, pendenciaStatusDt, UsuarioSessao, PosicaoPaginaAtual);
				}
			}

			// Detalhes da pendência serventia (movimentar proceso)
			if (fluxo == 10) {
				if (idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash)) {
					stAcao = "/WEB-INF/jsptjgo/PendenciaServentiaDetalhada.jsp";
					pendenciaDt = this.detalharPendencia(request, idPendencia, pendenciaNe, pendenciaDt, UsuarioSessao);
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}

			// Pendencias citacoes lidas
			if (fluxo == 12) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaCitacaoFinalizadasResponsavel.jsp";
				this.listarCitacoesLidas(request, pendenciaNe, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Consulta pendencias respondidas da serventia
			if (fluxo == 13) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaRespondidasUsuario.jsp";
				this.listarRespondidasUsuario(request, fluxo, pendenciaNe, pendenciaTipoDt, pendenciaStatusDt, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Detalhes da pendência respondidas
			if (fluxo == 14) {
				if ((idPendencia != null && hash != null && UsuarioSessao.VerificarCodigoHash(idPendencia, hash))) {
					stAcao = "/WEB-INF/jsptjgo/PendenciaRespondidaDetalhada.jsp";
					pendenciaDt = this.detalhar(request, idPendencia, pendenciaNe, pendenciaDt, UsuarioSessao);
				} else {
					throw new MensagemException("Sem permissão para executar operaçoes com essa pendência.");
				}
			}

			// Consultar intimações distribuidas
			if (fluxo == 15) {
				if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.COORDENADOR_PROMOTORIA) {
					stAcao = "/WEB-INF/jsptjgo/PendenciasIntimacoesDistribuidasPromotoria.jsp";
				} else if (UsuarioSessao.getUsuarioDt().isCoordenadorJuridico()) {
					stAcao = "/WEB-INF/jsptjgo/PendenciasIntimacoesDistribuidasProcuradoria.jsp";
				}
				this.listarIntimacoesDistribuidas(request, pendenciaNe, UsuarioSessao, PosicaoPaginaAtual);
			}

			// Consultar intimações lidas
			if (fluxo == 16) {
				if (Funcoes.StringToInt(UsuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.COORDENADOR_PROMOTORIA) {
					stAcao = "/WEB-INF/jsptjgo/PendenciasIntimacoesLidasDistribuicaoPromotoria.jsp";
				} else if (UsuarioSessao.getUsuarioDt().isCoordenadorJuridico()) {
					stAcao = "/WEB-INF/jsptjgo/PendenciasIntimacoesLidasDistribuicaoProcuradoria.jsp";
				}
				this.listarIntimacoesLidasDistribuicao(request, pendenciaNe, UsuarioSessao, PosicaoPaginaAtual);
			}

			request.setAttribute("PaginaAtual", paginaatual);

			break;
		case Configuracao.Curinga8:
			// CRIAR PENDÊNCIA E PUBLICAÇÃO
			request.setAttribute("PaginaAtual", paginaatual);

			if (fluxo == 0) {
				if (UsuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ADVOGADO) stAcao = "/WEB-INF/jsptjgo/CriarPendenciaAdvogado.jsp";
				else
					stAcao = "/WEB-INF/jsptjgo/CriarPendencia.jsp";

				pendenciaDt = this.criarPendencia(request, pendenciaNe, pendenciaDt, serventiaExpedirDt, serventiaTipoDt, pendenciaTipoDt, arquivoTipoDt, modeloDt, UsuarioSessao);
			}

			if (fluxo == 1) {
				stAcao = "/WEB-INF/jsptjgo/PendenciaPublica.jsp";
				this.publicacaoPublica(request, pendenciaNe, pendenciaDt, arquivoTipoDt, modeloDt, UsuarioSessao);
			}

			break;
		case Configuracao.Curinga9:
			// CONSULTAR PENDÊNCIAS: AGUARDANDO RETORNO, PARA EXPEDIR, PARA VERIFICAR;
			// TODAS ABERTAS(SEM TÁ RESERVADO OU COM USUÁRIO RESPONSÁVEL) DA SERVENTIA

			stAcao = "/WEB-INF/jsptjgo/PendenciaDistribuicao.jsp";
			this.listaAbertas(request, pendenciaNe, UsuarioSessao, pendenciaTipoDt, pendenciaStatusDt);
			request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
			request.setAttribute("PaginaAtual", paginaatual);

			break;
		
		// CONSULTA DE OFICIAL DE JUSTIÇA
		case UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar:
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Nome"};
				String[] lisDescricao = {"Nome", "Serventia"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_UsuarioServentia_2");
				request.setAttribute("tempBuscaDescricao","UsuarioServentia_2");
				request.setAttribute("tempBuscaPrograma","UsuarioServentia");			
				request.setAttribute("tempRetorno","Pendencia");		
				request.setAttribute("PaginaAtual", (UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);	
				request.getSession().setAttribute("ultimaAba", 1);
			} else {
				String stTemp="";
				stTemp = pendenciaNe.consultarOficialJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
				response.setContentType("text/x-json");
				try{
					response.getOutputStream().write(stTemp.getBytes());
					response.flushBuffer();
				} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
				return;								
			}
		break;
		
		
		// CONSULTA DE SERVENTIA CARGO ESCALA (OFICIAIS AD HOC)
		case ServentiaCargoEscalaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Curinga6:
			if (request.getParameter("Passo")==null){
				String[] lisNomeBusca = {"Nome"};
				String[] lisDescricao = {"Nome", "Escala"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_OficialAdhoc");
				request.setAttribute("tempBuscaDescricao","OficialAdhoc");
				request.setAttribute("tempBuscaPrograma","ServentiaCargoEscala");			
				request.setAttribute("tempRetorno","Pendencia");		
				request.setAttribute("PaginaAtual", (ServentiaCargoEscalaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Curinga6));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);	
				request.getSession().setAttribute("ultimaAba", 1);
			} else {
				String stTemp="";
				stTemp = pendenciaNe.consultarOficialAdhocJSON(stNomeBusca1, PosicaoPaginaAtual, serventiaExpedirDt.getId());
				response.setContentType("text/x-json");
				try{
					response.getOutputStream().write(stTemp.getBytes());
					response.flushBuffer();
				} catch(Exception e ){ response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Erro! Não foi possível fazer a consulta, entre em contato com o suporte."); }
				return;								
			}
		break;
		
		default:
			super.executar(request, response, UsuarioSessao, paginaatual, tempNomeBusca, PosicaoPaginaAtual);
			return;
		}

		request.getSession().setAttribute("ServentiaCargodt", serventiaCargoDt);
		request.getSession().setAttribute("ServentiaTipodt", serventiaTipoDt);
		request.getSession().setAttribute("Serventiadt", serventiaDt);
		request.getSession().setAttribute("ServentiaExpedirdt", serventiaExpedirDt);
		request.setAttribute("temDataVisto", pendenciaDt.temDataVisto());
		request.getSession().setAttribute("Pendenciadt", pendenciaDt);
		request.getSession().setAttribute("Pendenciane", pendenciaNe);
		request.getSession().setAttribute("PendenciaTipodt", pendenciaTipoDt);
		request.getSession().setAttribute("PendenciaStatusdt", pendenciaStatusDt);
		request.getSession().setAttribute("Modelodt", modeloDt);
		request.getSession().setAttribute("ArquivoTipodt", arquivoTipoDt);
		request.getSession().setAttribute("Comarcadt", comarcaDt);
		request.setAttribute("fluxo", fluxo);
		request.setAttribute("ultimaOperacao", ultimaOperacao);
		
		//Se o usuário for ESTAGIÁRIO não pode mostrar o botão assinar
		if(UsuarioSessao.getUsuarioDt().getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.ESTAGIARIO))){
			request.setAttribute("ocultarBotoesEstagiario", true);
		}

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Detalhamento de pendencias
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 11/12/2008 16:12
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, vo de pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio de usuario
	 * @throws Exception
	 */
	protected PendenciaDt detalhar(HttpServletRequest request, String idPendencia, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception{

		if (idPendencia != null && !idPendencia.equals("null") && !idPendencia.equals("")) 
			
			pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(idPendencia, usuarioNe);
			if(pendenciaDt == null || pendenciaDt.getId() == null || pendenciaDt.getId().equals("")){
				pendenciaDt = pendenciaNe.consultarPendenciaId(idPendencia, usuarioNe);
			}


		if (pendenciaDt.isPendenciaDeProcesso()) {
			// Consulta os dados do processo
			// pendenciaDt = pendenciaNe.visualizar(pendenciaDt,
			// usuarioNe.getUsuarioDt()); // seta data visto
			pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaDt));

			request.setAttribute("ListaArquivos", pendenciaNe.consultarArquivosPendenciaComHash(pendenciaDt, usuarioNe));
			request.setAttribute("ListaAndamentos", pendenciaNe.consultarPaiRecursiva(pendenciaDt, usuarioNe));

			request.setAttribute("podeReabrir", pendenciaNe.podeReabrir(pendenciaDt, usuarioNe.getUsuarioDt()));

			PendenciaDt filha = pendenciaNe.consultarFilha(pendenciaDt);
			request.setAttribute("IdMaisRecente", filha != null ? filha.getId() : null);
			if (filha != null && filha.getId() != null && !filha.getId().equals("")) request.setAttribute("hashMaisRecente", usuarioNe.getCodigoHash(filha.getId()));
			request.setAttribute("comOpcao", true);
			request.setAttribute("linkArquivo", "PendenciaArquivo");
			request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
			request.setAttribute("paginaArquivo", Configuracao.Curinga6);

		} else {// if (pendenciaNe.podeAcompanhar(pendenciaDt,
				// usuarioNe.getUsuarioDt())){
			// Verifica se o usuario pode acompanhar a pendencia

			// pendenciaDt = pendenciaNe.visualizar(pendenciaDt,
			// usuarioNe.getUsuarioDt()); // seta data visto
			pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaDt));

			request.setAttribute("ListaArquivos", pendenciaNe.consultarArquivosPendenciaComHash(pendenciaDt, usuarioNe));
			request.setAttribute("ListaAndamentos", pendenciaNe.consultarPaiRecursiva(pendenciaDt,usuarioNe));

			request.setAttribute("podeReabrir", pendenciaNe.podeReabrir(pendenciaDt, usuarioNe.getUsuarioDt()));

			PendenciaDt filha = pendenciaNe.consultarFilha(pendenciaDt);
			request.setAttribute("IdMaisRecente", filha != null ? filha.getId() : null);

			request.setAttribute("comOpcao", true);
			request.setAttribute("linkArquivo", "PendenciaArquivo");
			request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
			request.setAttribute("paginaArquivo", Configuracao.Curinga6);
		}// else {
			// throw new
			// Exception("O usuário não tem permissão para acompanhar esta pendência");
			// }

		return pendenciaDt;
	}
	
	/**
	 * Detalhamento de pendencias
	 * 
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, vo de pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio de usuario
	 * @throws Exception
	 */
	protected PendenciaDt detalharPendendiaFinalizada(HttpServletRequest request, String idPendencia, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception{

		if (idPendencia != null && !idPendencia.equals("null") && !idPendencia.equals("")) 
			pendenciaDt = pendenciaNe.consultarPendenciaFinalizadaId(idPendencia, usuarioNe);

		if (pendenciaDt.isPendenciaDeProcesso()) {
			// Consulta os dados do processo
			// pendenciaDt = pendenciaNe.visualizar(pendenciaDt,
			// usuarioNe.getUsuarioDt()); // seta data visto
			pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveisFinais(pendenciaDt));
			
			request.setAttribute("ListaArquivos", pendenciaNe.consultarArquivosPendenciaFinalizadaComHash(pendenciaDt, usuarioNe));
			request.setAttribute("ListaAndamentos", pendenciaNe.consultarPaiRecursiva(pendenciaDt, usuarioNe));
			request.setAttribute("ListaResponsaveisHistorico", pendenciaNe.consultarHistoricosPendenciaFinal(idPendencia));

			request.setAttribute("podeReabrir", pendenciaNe.podeReabrir(pendenciaDt, usuarioNe.getUsuarioDt()));

			PendenciaDt filha = pendenciaNe.consultarFilha(pendenciaDt);
			request.setAttribute("IdMaisRecente", filha != null ? filha.getId() : null);
			if (filha != null && filha.getId() != null && !filha.getId().equals("")) request.setAttribute("hashMaisRecente", usuarioNe.getCodigoHash(filha.getId()));
			request.setAttribute("comOpcao", true);
			request.setAttribute("linkArquivo", "PendenciaArquivo");
			request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
			request.setAttribute("paginaArquivo", Configuracao.Curinga6);

		} else {// if (pendenciaNe.podeAcompanhar(pendenciaDt,
				// usuarioNe.getUsuarioDt())){
			// Verifica se o usuario pode acompanhar a pendencia

			// pendenciaDt = pendenciaNe.visualizar(pendenciaDt,
			// usuarioNe.getUsuarioDt()); // seta data visto
			pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveisFinais(pendenciaDt));

			request.setAttribute("ListaArquivos", pendenciaNe.consultarArquivosPendenciaFinalizadaComHash(pendenciaDt, usuarioNe));
			request.setAttribute("ListaAndamentos", pendenciaNe.consultarPaiRecursiva(pendenciaDt,usuarioNe));

			request.setAttribute("podeReabrir", pendenciaNe.podeReabrir(pendenciaDt, usuarioNe.getUsuarioDt()));

			PendenciaDt filha = pendenciaNe.consultarFilha(pendenciaDt);
			request.setAttribute("IdMaisRecente", filha != null ? filha.getId() : null);

			request.setAttribute("comOpcao", true);
			request.setAttribute("linkArquivo", "PendenciaArquivo");
			request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
			request.setAttribute("paginaArquivo", Configuracao.Curinga6);
		}// else {
			// throw new
			// Exception("O usuário não tem permissão para acompanhar esta pendência");
			// }

		return pendenciaDt;
	}


	/**
	 * Detalhamento de pendencias da serventia (movimentar processo)
	 * 
	 * @author Leandro Bernardes
	 * @since 26/05/2009 16:12
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, vo de pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio de usuario
	 * @throws Exception
	 */
	protected PendenciaDt detalharPendencia(HttpServletRequest request, String idPendencia, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception{

		if (pendenciaDt.getId() == null || pendenciaDt.getId().equals("")) pendenciaDt = pendenciaNe.consultarPendenciaId(idPendencia, usuarioNe);

		// Consulta os dados do processo
		pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaDt));

		request.setAttribute("ListaArquivos", pendenciaNe.consultarArquivosAssinadosComHash(pendenciaDt, usuarioNe));
		request.setAttribute("ListaAndamentos", pendenciaNe.consultarPaiRecursiva(pendenciaDt, usuarioNe));

		request.setAttribute("podeReabrir", pendenciaNe.podeReabrir(pendenciaDt, usuarioNe.getUsuarioDt()));

		PendenciaDt filha = pendenciaNe.consultarFilha(pendenciaDt);
		request.setAttribute("IdMaisRecente", filha != null ? filha.getId() : null);

		request.setAttribute("comOpcao", true);
		request.setAttribute("linkArquivo", "PendenciaArquivo");
		request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
		request.setAttribute("paginaArquivo", Configuracao.Curinga6);

		return pendenciaDt;
	}

	/**
	 * Listar as pendencias finalizadas
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 02/12/2008 11:00
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio de usuario
	 * @param String
	 *            posicaoPaginaAtual, posicao da pagina atual
	 * @throws Exception
	 */
	protected void listarFinalizadas(HttpServletRequest request, int fluxo, PendenciaNe pendenciaNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception {

		// Procura pendencias
		// String numero_processo = null;
		boolean prioridade = true, somenteDeProcessos = false;

		// if (request.getParameter("nomeBusca") != null &&
		// request.getParameter("nomeBusca").trim().equals("") == false){
		// numero_processo = request.getParameter("nomeBusca");
		// }

		if (request.getParameter("prioridade") == null || request.getParameter("prioridade").trim().equals("1") == false) {
			prioridade = false;
		}

		if (request.getParameter("somenteDeProcessos") != null && request.getParameter("somenteDeProcessos").trim().equals("1")) {
			somenteDeProcessos = true;
		}

		List pendencias = new ArrayList();

		// if (request.getParameter("menu") != null &&
		// request.getParameter("menu").toString().equals("1")){
		// pendencias = new ArrayList();
		// } else {
		//
		// if (fluxo == 5){
		// pendencias = pendenciaNe.consultarFinalizadas(usuarioNe,
		// usuarioNe.getUsuarioDt().getId_UsuarioServentia(), null,
		// pendenciaTipoDt, pendenciaStatusDt, prioridade, null,
		// numero_processo, null, null,
		// null, null, posicaoPaginaAtual);
		// } else if (fluxo == 9){
		// pendencias = pendenciaNe.consultarFinalizadas(usuarioNe, null,
		// usuarioNe.getUsuarioDt().getId_Serventia(),
		// pendenciaTipoDt, pendenciaStatusDt, prioridade, null,
		// numero_processo, null, null,
		// null, null, posicaoPaginaAtual);
		// }
		//
		// }

		request.setAttribute("Id_PendenciaStatus", pendenciaStatusDt.getId());
		request.setAttribute("PendenciaStatus", pendenciaStatusDt.getPendenciaStatus());

		request.setAttribute("Id_PendenciaTipo", pendenciaTipoDt.getId());
		request.setAttribute("PendenciaTipo", pendenciaTipoDt.getPendenciaTipo());

		request.setAttribute("prioridade", prioridade);
		request.setAttribute("somenteDeProcessos", somenteDeProcessos);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Listar as pendencias respondidas da serventia
	 * 
	 * @author Leandro Bernardes
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio de usuario
	 * @param String
	 *            posicaoPaginaAtual, posicao da pagina atual
	 * @throws Exception
	 */
	protected void listarRespondidasServentia(HttpServletRequest request, int fluxo, PendenciaNe pendenciaNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception {

		// Procura pendencias
		// String numero_processo = null;
		boolean prioridade = true, somenteDeProcessos = false;

		// if (request.getParameter("nomeBusca") != null &&
		// request.getParameter("nomeBusca").trim().equals("") == false){
		// numero_processo = request.getParameter("nomeBusca");
		// }

		if (request.getParameter("prioridade") == null || request.getParameter("prioridade").trim().equals("1") == false) {
			prioridade = false;
		}

		if (request.getParameter("somenteDeProcessos") != null && request.getParameter("somenteDeProcessos").trim().equals("1")) {
			somenteDeProcessos = true;
		}

		List pendencias = new ArrayList();

		// pendencias =
		// pendenciaNe.consultarRespondidasServentia(usuarioNe,pendenciaTipoDt,
		// pendenciaStatusDt, prioridade, null,
		// numero_processo, null, null, null, null, posicaoPaginaAtual);

		request.setAttribute("Id_PendenciaStatus", pendenciaStatusDt.getId());
		request.setAttribute("PendenciaStatus", pendenciaStatusDt.getPendenciaStatus());

		request.setAttribute("Id_PendenciaTipo", pendenciaTipoDt.getId());
		request.setAttribute("PendenciaTipo", pendenciaTipoDt.getPendenciaTipo());

		request.setAttribute("prioridade", prioridade);
		request.setAttribute("somenteDeProcessos", somenteDeProcessos);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Listar as pendencias respondidas da serventia
	 * 
	 * @author Leandro Bernardes
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio de usuario
	 * @param String
	 *            posicaoPaginaAtual, posicao da pagina atual
	 * @throws Exception
	 */
	protected void listarRespondidasUsuario(HttpServletRequest request, int fluxo, PendenciaNe pendenciaNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception {

		boolean prioridade = true, somenteDeProcessos = false;

		// if (request.getParameter("nomeBusca") != null &&
		// request.getParameter("nomeBusca").trim().equals("") == false){
		// numero_processo = request.getParameter("nomeBusca");
		// }

		if (request.getParameter("prioridade") == null || request.getParameter("prioridade").trim().equals("1") == false) {
			prioridade = false;
		}

		if (request.getParameter("somenteDeProcessos") != null && request.getParameter("somenteDeProcessos").trim().equals("1")) {
			somenteDeProcessos = true;
		}

		List pendencias = new ArrayList();

		// pendencias = pendenciaNe.consultarRespondidasUsuario(usuarioNe,
		// pendenciaTipoDt, pendenciaStatusDt, prioridade, null,
		// numero_processo, null, null, null, null, posicaoPaginaAtual);

		request.setAttribute("Id_PendenciaStatus", pendenciaStatusDt.getId());
		request.setAttribute("PendenciaStatus", pendenciaStatusDt.getPendenciaStatus());

		request.setAttribute("Id_PendenciaTipo", pendenciaTipoDt.getId());
		request.setAttribute("PendenciaTipo", pendenciaTipoDt.getPendenciaTipo());

		request.setAttribute("prioridade", prioridade);
		request.setAttribute("somenteDeProcessos", somenteDeProcessos);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Listar as pendencias de prazo decorrido
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/01/2009 13:45
	 * @param request
	 *            objeto de request
	 * @param pendenciaNe
	 *            regras de negocio
	 * @param tipoPendencia
	 *            tipo da pendencia, 1 - intimacao, 2 - citacao, outros - ambos
	 * @param usuarioNe
	 *            objeto de negocio do usuario, usuario da sessao
	 * @param posicaoPaginaAtual
	 *            paginacao
	 * @throws Exception
	 */
	protected void listarPrazosDecorridos(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaTipoDt pendenciaTipoDt, UsuarioNe usuarioNe, String posicaoPaginaAtual, boolean aDecorrer) throws Exception{

		if (request.getParameter("operacao") != null) {
			if (request.getParameter("operacao").equals("MarcarVisto")) {
				if (request.getParameterValues("pendenciaSelecionada[]") != null) {
					String pendenciasSelecionadas[] = request.getParameterValues("pendenciaSelecionada[]");

					int qtd = 0;
					for (String valor : pendenciasSelecionadas) {
						String valores[] = valor.split(" ");
						String idPendencia = valores[0];

						if (!idPendencia.equals("0")) {
							String hash = valores[1];

							if (usuarioNe.VerificarCodigoHash(idPendencia, hash)) {
								PendenciaDt pendenciaDt = new PendenciaDt();
								pendenciaDt.setId(idPendencia);

								pendenciaNe.marcarVistoAgora(pendenciaDt, usuarioNe.getUsuarioDt());
								qtd++;
							} else {
								request.setAttribute("MensagemErro", "Não há permissão para vistar uma pendência");
							}
						}
					}

					if (qtd == 0) {
						if (request.getAttribute("MensagemErro") == null) request.setAttribute("MensagemErro", "Nenhuma pendência foi informada como vista");
					} else {
						if (qtd == 1) request.setAttribute("MensagemOk", "Foi informado como vista " + qtd + " pendência");
						else
							request.setAttribute("MensagemOk", "Foram informadas como vistas " + qtd + " pendências");
					}
				} else
					request.setAttribute("MensagemErro", "Selecione pelo menos uma pendência");
			}
		}

		String numero_processo = "";
		if (request.getParameter("numeroProcesso") != null && request.getParameter("numeroProcesso").trim().equals("") == false) {
			numero_processo = request.getParameter("numeroProcesso");
		}

		List pendencias = new ArrayList();

		 if (pendenciaTipoDt != null && !pendenciaTipoDt.getId().equals("")){
			 pendencias = pendenciaNe.consultarPrazosDecorridosADecorrerComHash(usuarioNe, Funcoes.StringToInt(pendenciaTipoDt.getId()), numero_processo, posicaoPaginaAtual, aDecorrer);
		 } else{
			 pendencias = pendenciaNe.consultarPrazosDecorridosADecorrerComHash(usuarioNe, null, numero_processo, posicaoPaginaAtual, aDecorrer);
		 }

		if (pendenciaTipoDt != null) {
			request.setAttribute("Id_PendenciaTipo", pendenciaTipoDt.getId());
			request.setAttribute("PendenciaTipo", pendenciaTipoDt.getPendenciaTipo());
		}

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}
	
	/**
	 * Listar as pendencias de prazo decorrido
	 * 
	 * @param request
	 *            objeto de request
	 * @param pendenciaNe
	 *            regras de negocio
	 * @param tipoPendencia
	 *            tipo da pendencia, 1 - intimacao, 2 - citacao, outros - ambos
	 * @param usuarioNe
	 *            objeto de negocio do usuario, usuario da sessao
	 * @param posicaoPaginaAtual
	 *            paginacao
	 * @throws Exception
	 */
	protected void listarPrazosDecorridosDevolucaoAutos(HttpServletRequest request, PendenciaNe pendenciaNe, UsuarioDt usuarioDt, String posicaoPaginaAtual) throws Exception{

		String numeroProcesso = "";
		if (request.getParameter("numeroProcesso") != null && request.getParameter("numeroProcesso").trim().equals("") == false) {
			numeroProcesso = request.getParameter("numeroProcesso");
		}

		List pendencias = new ArrayList();
		pendencias = pendenciaNe.consultarPrazosDecorridosDevolucaoAutos(usuarioDt, numeroProcesso, posicaoPaginaAtual);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Consultar prazos decorridos de um usuário
	 * 
	 * @author Leandro Bernardes
	 * @since 13/08/2009
	 * @param request
	 *            objeto de request
	 * @param pendenciaNe
	 *            regras de negocio
	 * @param tipoPendencia
	 *            tipo da pendencia, 1 - intimacao, 2 - citacao, outros - ambos
	 * @param usuarioNe
	 *            objeto de negocio do usuario, usuario da sessao
	 * @param posicaoPaginaAtual
	 *            paginacao
	 * @throws Exception
	 */
	protected void listarPrazosDecorridosUsuario(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaTipoDt pendenciaTipoDt, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception{

		if (request.getParameter("operacao") != null) {
			if (request.getParameter("operacao").equals("MarcarVisto")) {
				if (request.getParameterValues("pendenciaSelecionada[]") != null) {
					String pendenciasSelecionadas[] = request.getParameterValues("pendenciaSelecionada[]");

					int qtd = 0;
					for (String valor : pendenciasSelecionadas) {
						String valores[] = valor.split(" ");
						String idPendencia = valores[0];

						if (!idPendencia.equals("0")) {
							String hash = valores[1];

							if (usuarioNe.VerificarCodigoHash(idPendencia, hash)) {
								PendenciaDt pendenciaDt = new PendenciaDt();
								pendenciaDt.setId(idPendencia);

								pendenciaNe.marcarVistoAgora(pendenciaDt, usuarioNe.getUsuarioDt());
								qtd++;
							} else {
								request.setAttribute("MensagemErro", "Não há permissão para vistar uma pendência");
							}
						}
					}

					if (qtd == 0) {
						if (request.getAttribute("MensagemErro") == null) request.setAttribute("MensagemErro", "Nenhuma pendência foi informada como vista");
					} else {
						if (qtd == 1) request.setAttribute("MensagemOk", "Foi informado como vista " + qtd + " pendência");
						else
							request.setAttribute("MensagemOk", "Foram informadas como vistas " + qtd + " pendências");
					}
				} else
					request.setAttribute("MensagemErro", "Selecione pelo menos uma pendência");
			}
		}

		String numero_processo = "";
		if (request.getParameter("numeroProcesso") != null && request.getParameter("numeroProcesso").trim().equals("") == false) {
			numero_processo = request.getParameter("numeroProcesso");
		}

		List pendencias = new ArrayList();

		if (pendenciaTipoDt != null && !pendenciaTipoDt.getId().equals("")){
			 pendencias = pendenciaNe.consultarPrazosDecorridosUsuarioComHash(usuarioNe, Funcoes.StringToInt(pendenciaTipoDt.getId()), numero_processo, posicaoPaginaAtual);
		} else {
			 pendencias = pendenciaNe.consultarPrazosDecorridosUsuarioComHash(usuarioNe, null, numero_processo, posicaoPaginaAtual);
		}

		if (pendenciaTipoDt != null) {
			request.setAttribute("Id_PendenciaTipo", pendenciaTipoDt.getId());
			request.setAttribute("PendenciaTipo", pendenciaTipoDt.getPendenciaTipo());
		}

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Listar as pendencias finalizadas (intimações lidas)
	 * 
	 * @author Leandro Bernardes
	 * @since 01/06/2009
	 * @param request
	 *            objeto de request
	 * @param pendenciaNe
	 *            regras de negocio
	 * @param usuarioNe
	 *            objeto de negocio do usuario, usuario da sessao
	 * @param posicaoPaginaAtual
	 *            paginacao
	 * @throws Exception
	 */
	protected void listarIntimacoesLidas(HttpServletRequest request, PendenciaNe pendenciaNe, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception{

		// Procura pendencias
		String numero_processo = null;

		if (request.getParameter("nomeBusca") != null && request.getParameter("nomeBusca").trim().equals("") == false) {
			numero_processo = request.getParameter("nomeBusca");
		}

		List pendencias = pendenciaNe.consultarIntimacoesLidas(usuarioNe, numero_processo, null, null, null, null, posicaoPaginaAtual);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Listar as pendencias intimacões distribuídas
	 * 
	 * @author Leandro Bernardes
	 * @since 23/02/2011
	 * @param request
	 *            objeto de request
	 * @param pendenciaNe
	 *            regras de negocio
	 * @param usuarioNe
	 *            objeto de negocio do usuario, usuario da sessao
	 * @param posicaoPaginaAtual
	 *            paginacao
	 * @throws Exception
	 */
	protected void listarIntimacoesDistribuidas(HttpServletRequest request, PendenciaNe pendenciaNe, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception{

		// Procura pendencias
		String numero_processo = null;

		if (request.getParameter("nomeBusca") != null && request.getParameter("nomeBusca").trim().equals("") == false) {
			numero_processo = request.getParameter("nomeBusca");
		}

		List pendencias = pendenciaNe.consultarIntimacoesDistribuidas(usuarioNe, numero_processo, null, null, posicaoPaginaAtual);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Listar as pendencias finalizadas (citações lidas)
	 * 
	 * @author Leandro Bernardes
	 * @since 23/07/2009
	 * @param request
	 *            objeto de request
	 * @param pendenciaNe
	 *            regras de negocio
	 * @param usuarioNe
	 *            objeto de negocio do usuario, usuario da sessao
	 * @param posicaoPaginaAtual
	 *            paginacao
	 * @throws Exception
	 */
	protected void listarCitacoesLidas(HttpServletRequest request, PendenciaNe pendenciaNe, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception{

		// Procura pendencias
		String numero_processo = null;

		if (request.getParameter("nomeBusca") != null && request.getParameter("nomeBusca").trim().equals("") == false) {
			numero_processo = request.getParameter("nomeBusca");
		}
		PendenciaTipoDt pendenciaTipoDt = new PendenciaTipoDt();
		pendenciaTipoDt.setPendenciaTipoCodigo(String.valueOf(PendenciaTipoDt.CARTA_CITACAO));

		List pendencias = pendenciaNe.consultarFinalizadasResponsavelServentia(usuarioNe, pendenciaTipoDt, numero_processo, null, null, null, null, posicaoPaginaAtual);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Lista as pendencias do usuario
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 28/11/2008 10:28
	 * @param HttpServletRequest
	 *            request, request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio de usuario
	 * @param String
	 *            posicaoPaginaAtual, pagina atual
	 * @throws Exception
	 */
	protected void listarMinhas(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception{

		/**
		 * Seguranca Incluir o calculo do hash para poder executar
		 * 
		 * @author Ronneesley Moura Teles
		 * @since 14/01/2009 14:28
		 */
		if (request.getParameter("operacao") != null) {
			if (request.getParameter("operacao").equals("MarcarComoLidas")) {
				if (request.getParameterValues("pendenciaSelecionada[]") != null) {
					String pendenciasSelecionadas[] = request.getParameterValues("pendenciaSelecionada[]");

					int qtd = 0;
					for (String idPendencia : pendenciasSelecionadas) {
						if (!idPendencia.equals("0")) {
							PendenciaDt pendenciaDt = new PendenciaDt();
							pendenciaDt.setId(idPendencia);

							pendenciaNe.marcarVistoPendenciaPrazo(pendenciaDt, usuarioNe.getUsuarioDt());
							qtd++;
						}
					}

					if (qtd == 0) request.setAttribute("MensagemErro", "Nenhuma pendência foi informada como vista");
					else if (qtd == 1) request.setAttribute("MensagemOk", "Foi informado como vista " + qtd + " pendência");
					else
						request.setAttribute("MensagemOk", "Foram informadas como vistas " + qtd + " pendências");
				} else
					request.setAttribute("MensagemErro", "Selecione pelo menos uma pendência");
			}
		}

		boolean prioridade = true, somenteDeProcessos = false;

		// if (request.getParameter("nomeBusca") != null &&
		// request.getParameter("nomeBusca").trim().equals("") == false){
		// numero_processo = request.getParameter("nomeBusca");
		// }

		if (request.getParameter("prioridade") == null || request.getParameter("prioridade").trim().equals("1") == false) {
			prioridade = false;
		}

		if (request.getParameter("somenteDeProcessos") != null && request.getParameter("somenteDeProcessos").trim().equals("1")) {
			somenteDeProcessos = true;
		}

		List pendencias = new ArrayList();// pendenciaNe.consultarMinhas(usuarioNe,
											// pendenciaTipoDt,
											// pendenciaStatusDt, prioridade,
											// null, numero_processo, null,
											// null, posicaoPaginaAtual);

		request.setAttribute("Id_PendenciaStatus", pendenciaStatusDt.getId());
		request.setAttribute("PendenciaStatus", pendenciaStatusDt.getPendenciaStatus());

		request.setAttribute("Id_PendenciaTipo", pendenciaTipoDt.getId());
		request.setAttribute("PendenciaTipo", pendenciaTipoDt.getPendenciaTipo());

		request.setAttribute("prioridade", prioridade);
		request.setAttribute("somenteDeProcessos", somenteDeProcessos);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Consulta as pendencias expedidas para serventias (on-line)
	 * 
	 * @author Leandro Bernardes
	 * @since 13/08/2009
	 * @param HttpServletRequest
	 *            request, request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio de usuario
	 * @param String
	 *            posicaoPaginaAtual, pagina atual
	 * @throws Exception
	 */
	protected void listarExpedidasServentia(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception{

		/**
		 * Seguranca Incluir o calculo do hash para poder executar
		 * 
		 * @author Ronneesley Moura Teles
		 * @since 14/01/2009 14:28
		 */
		if (request.getParameter("operacao") != null) {
			if (request.getParameter("operacao").equals("MarcarComoLidas")) {
				if (request.getParameterValues("pendenciaSelecionada[]") != null) {
					String pendenciasSelecionadas[] = request.getParameterValues("pendenciaSelecionada[]");

					int qtd = 0;
					for (String idPendencia : pendenciasSelecionadas) {
						if (!idPendencia.equals("0")) {
							PendenciaDt pendenciaDt = new PendenciaDt();
							pendenciaDt.setId(idPendencia);

							pendenciaNe.marcarVistoPendenciaPrazo(pendenciaDt, usuarioNe.getUsuarioDt());
							qtd++;
						}
					}

					if (qtd == 0) request.setAttribute("MensagemErro", "Nenhuma pendência foi informada como vista");
					else if (qtd == 1) request.setAttribute("MensagemOk", "Foi informado como vista " + qtd + " pendência");
					else
						request.setAttribute("MensagemOk", "Foram informadas como vistas " + qtd + " pendências");
				} else
					request.setAttribute("MensagemErro", "Selecione pelo menos uma pendência");
			}
		}

		boolean prioridade = true, somenteDeProcessos = false;

		// if (request.getParameter("nomeBusca") != null &&
		// request.getParameter("nomeBusca").trim().equals("") == false){
		// numero_processo = request.getParameter("nomeBusca");
		// }

		if (request.getParameter("prioridade") == null || request.getParameter("prioridade").trim().equals("1") == false) {
			prioridade = false;
		}

		if (request.getParameter("somenteDeProcessos") != null && request.getParameter("somenteDeProcessos").trim().equals("1")) {
			somenteDeProcessos = true;
		}

		List pendencias = new ArrayList();// pendenciaNe.consultarExpedidasServentia(usuarioNe,
											// pendenciaTipoDt,
											// pendenciaStatusDt, prioridade,
											// null, numero_processo, null,
											// null, posicaoPaginaAtual);

		request.setAttribute("Id_PendenciaStatus", pendenciaStatusDt.getId());
		request.setAttribute("PendenciaStatus", pendenciaStatusDt.getPendenciaStatus());

		request.setAttribute("Id_PendenciaTipo", pendenciaTipoDt.getId());
		request.setAttribute("PendenciaTipo", pendenciaTipoDt.getPendenciaTipo());

		request.setAttribute("prioridade", prioridade);
		request.setAttribute("somenteDeProcessos", somenteDeProcessos);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}

	/**
	 * Pegar uma pendencia pelo tipo, esta pendencia e indeterminada, o sistema
	 * retorna a ultima pendencia pela ordem
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 29/10/2008 10:32
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciane, objeto de negocio de pendencia
	 * @param PendenciaDt
	 *            pendenciadt, vo de pendencia
	 * @throws Exception
	 * @return String
	 */
	protected PendenciaDt pegarPendenciaPorTipo(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, PendenciaTipoDt pendenciaTipoDt, ArquivoTipoDt arquivoTipoDt, UsuarioNe usuarioNe, ModeloDt modeloDt, ServentiaDt serventiaDt, ServentiaDt serventiaExpedirDt, ServentiaTipoDt serventiaTipoDt, ComarcaDt comarcaDt, ServentiaCargoDt serventiaCargoDt) throws Exception{

		// Verifica se a pendencia nao foi encontrada
		if (pendenciaDt == null) throw new MensagemException("Não há mais pendências abertas para este critério.");

		if (request.getParameter("tipo") != null) {
			String tipo = request.getParameter("tipo"); // Receber por parametro

			String operacao = request.getParameter("operacao");

			if (operacao.equals("PegarPorTipoServentiaCargo")) {
				pendenciaDt = pendenciaNe.reservarUltimaServentiaCargo(tipo, usuarioNe);
			} else if (operacao.equals("PegarPorTipoServentia")) {
				pendenciaDt = pendenciaNe.reservarUltimaServentia(tipo, usuarioNe);
			} else if (operacao.equals("PegarPorTipoServentiaTipo")) {
				pendenciaDt = pendenciaNe.reservarUltimaServentiaTipo(tipo, usuarioNe);
			} else if (operacao.equals("PreAnalisadasServentia")) {
				pendenciaDt = pendenciaNe.reservarUltimaPreAnalisadaServentia(tipo, usuarioNe);
			}

			// consulta serventia responsavel pelo processo (Delegacia)
			Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
			if (!pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_RETORNO)) && !pendenciaDt.getPendenciaStatusCodigo().equals(String.valueOf(PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO))) {
				if (tipoPendencia == PendenciaTipoDt.OFICIO_DELEGACIA && pendenciaDt.isPendenciaDeProcesso()) {
					ServentiaCargoDt aux = pendenciaNe.consultaAutoridadePolicialResponsavelProcesso(pendenciaDt.getId_Processo());
					if (aux != null) {
						serventiaExpedirDt.setId(aux.getId_Serventia());
						serventiaExpedirDt.setServentia(aux.getServentia());
					} else
						serventiaExpedirDt = new ServentiaDt();
				}
			}

			// Apos reservar a pendencia, distribui a para o usuario corrente
			pendenciaNe.distribuir(pendenciaDt.getId(), usuarioNe.getUsuarioDt().getId_UsuarioServentia(), usuarioNe.getUsuarioDt().getGrupoCodigoToInt());

			request.getSession().removeAttribute("PendenciaTipodt");
			// request.getSession().setAttribute("ultimaAba", 0);

			pendenciaTipoDt = (PendenciaTipoDt) request.getSession().getAttribute("PendenciaTipodt");
			if ((pendenciaTipoDt == null || pendenciaTipoDt.getId().equals("")) && (arquivoTipoDt.getId() == null || arquivoTipoDt.getId().equals(""))) {
				pendenciaTipoDt = pendenciaNe.consultarPendenciaTipoId(pendenciaDt.getId_PendenciaTipo());
				arquivoTipoDt.setId(pendenciaTipoDt.getId_ArquivoTipo());
				arquivoTipoDt.setArquivoTipo(pendenciaTipoDt.getArquivoTipo());
			}

			// Se tem modelo selecionado, logo monta o modelo
			if (modeloDt.getId() != null && modeloDt.getId().equals("") == false) modeloDt.setTexto(pendenciaNe.montaModelo(pendenciaDt, modeloDt, usuarioNe));

			// Se a pendencia é uma precatória já seleciona a serventia do usuário logado
			if (serventiaDt != null && serventiaDt.getId() != null && serventiaDt.getId().equals("") &&  pendenciaDt.eCartaPrecatoria()) {

				serventiaDt.setId(usuarioNe.getUsuarioDt().getId_Serventia());
				serventiaDt.setServentia(usuarioNe.getUsuarioDt().getServentia());
			}
			
			// Se a pendência é um mandado e a serventia do usuário logado possui serventia relacionada, a seleciona
			if (serventiaDt != null && serventiaDt.getId().equals("") && pendenciaDt.eMandado()) {
				ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		        ServentiaDt centralMandadosRelacionada = serventiaRelacionadaNe.consultarCentralMandadosRelacionada(usuarioNe.getUsuarioDt().getId_Serventia());
		        if(centralMandadosRelacionada != null && pendenciaNe.temCentralProjudiImplantada(serventiaDt.getId())) {
		        	serventiaExpedirDt.setId(centralMandadosRelacionada.getId());
		        	serventiaExpedirDt.setServentia(centralMandadosRelacionada.getServentia());
		        	serventiaExpedirDt.setServentiaTipoCodigo(centralMandadosRelacionada.getServentiaTipoCodigo());
		        }
			}

			/**
			 * Todos os tratamentos da aba de encaminhamento de pendencias
			 * 
			 * @author Ronneesley Moura Teles
			 * @since 26/09/2008
			 */
			// Atribui as descricoes das variaveis
			this.atributosEdicaoPendencia(request, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, arquivoTipoDt, pendenciaTipoDt, modeloDt);

			request.setAttribute("PassoInsercao", "");
			request.setAttribute("PassoBusca", "");

			// Auxiliar para armazenar a ultima aba
			request.getSession().setAttribute("ultimaAba", request.getSession().getAttribute("ultimaAba") == null ? 1 : request.getSession().getAttribute("ultimaAba"));

			return pendenciaDt;
		}

		return null;
	}

	/**
	 * Controla publicacao publica
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 10/10/2008 16:17
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, vo de pendencia
	 * @param UsuarioNe
	 *            usuarioSessao, bo de usuario
	 * @throws Exception
	 */
	protected void publicacaoPublica(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, ArquivoTipoDt arquivoTipoDt, ModeloDt modeloDt, UsuarioNe usuarioSessao) throws Exception{
		// Verifica se foi disparado alguma operacao
		if (request.getParameter("operacao") != null) {
			// Operacao criar
			if (request.getParameter("operacao").equals("Confirmar")) {
				// Recebe os arquivos da sessao
				Map arquivos = (HashMap) request.getSession().getAttribute("ListaArquivos");
				try{
					if (arquivos == null || arquivos.size() == 0) request.setAttribute("MensagemErro", "Lista de arquivos está vazia.");
					else {
						pendenciaNe.criarPublicacao(pendenciaDt, Funcoes.converterMapParaList(arquivos), usuarioSessao.getUsuarioDt());

						request.setAttribute("MensagemOk", "Publicação criada com sucesso");
						request.setAttribute("PaginaAtual", Configuracao.Imprimir);
						request.setAttribute("OperacaoExpedirImprimir", "ExpedirImprimir");
					}
				} catch(Exception ex) {
					super.exibaMensagemInconsistenciaErro(request, "Erro na publicação.");
				}
			} else if (request.getParameter("operacao").equals("Criar")) {
				request.setAttribute("operacaoPendencia", "Confirmar");
				request.setAttribute("Mensagem", "Para confirmar Operação \"Criar Publicação\" clique no botão \"Confirmar\".");
			}
		}

		request.setAttribute("fluxo", "1");

		// Se tem modelo selecionado, logo monta o modelo
		if (modeloDt.getId() != null && modeloDt.getId().equals("") == false) modeloDt.setTexto(pendenciaNe.montaModelo(pendenciaDt, modeloDt, usuarioSessao));

		// Atribui variaveis necessarias para edicao de arquivos
		this.atributosEdicaoPendencia(request, null, null, null, null, null, arquivoTipoDt, null, modeloDt);

	}

	/**
	 * Manipula controle para reabrir uma pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 25/11/2008 14:52
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio da pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, vo da pendencia
	 * @param PendenciaTipoDt
	 *            pendenciaTipoDt, vo para tipo de pendencia
	 * @param ArquivoTipoDt
	 *            arquivoTipoDt, vo para tipo de arquivo
	 * @param ModeloDt
	 *            modeloDt, vo para modelo
	 * @param ServentiaDt
	 *            serventiaDt, vo da serventia
	 * @param ServentiaCargoDt
	 *            serventiaCargoDt, vo do cargo da serventia
	 * @param UsuarioNe
	 *            usuarioSessao, usuario da sessao
	 * @throws Exception
	 */
	protected PendenciaDt reabrir(HttpServletRequest request, boolean reabrir, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, PendenciaTipoDt pendenciaTipoDt, ArquivoTipoDt arquivoTipoDt, ModeloDt modeloDt, ServentiaDt serventiaDt, ServentiaCargoDt serventiaCargoDt, UsuarioNe usuarioSessao) throws Exception{

		// Auxiliar para armazenar a ultima aba
		request.getSession().setAttribute("ultimaAba", request.getSession().getAttribute("ultimaAba") == null ? 0 : request.getSession().getAttribute("ultimaAba"));
		String op = request.getParameter("operacao");

		// Consulta os dados da pendencia
		if (request.getSession().getAttribute("PendenciaReabrirDt") == null || (op != null && op.equals("ReabrirInicial"))) {
			if (pendenciaDt.getId_PendenciaPai() != null && !pendenciaDt.getId_PendenciaPai().equals("")) {
				// Consulta a pendencia pai, para pegar informacoes
				String hashPendenciaPai = pendenciaDt.getHash();
				PendenciaDt pendenciaPaiDt = pendenciaNe.consultarFinalizadaId(pendenciaDt.getId_PendenciaPai());

				// Cria uma nova pendencia
				pendenciaDt = new PendenciaDt();
				pendenciaDt.copiar(pendenciaPaiDt);
				pendenciaDt.limparDados();

				// Consulta dados dos responsaveis
				pendenciaDt.setResponsaveis(pendenciaNe.consultarResponsaveis(pendenciaPaiDt));

				// Modifica o id da pendencia pai
				pendenciaDt.setId_PendenciaPai(pendenciaPaiDt.getId());

				// Se possui dados do responsavel
				PendenciaResponsavelDt responsavel = pendenciaDt.getResponsavel();

				if (responsavel != null) {
					// Atribui os padroes aos valores
					// Dados do serventia cargo
					serventiaCargoDt.setId(responsavel.getId_ServentiaCargo());
					serventiaCargoDt.setServentiaCargo(responsavel.getServentiaCargo());

					// Dados da serventia
					serventiaDt.setId(responsavel.getId_Serventia());
					serventiaDt.setServentia(responsavel.getServentia());

					// Dados padroes
					request.setAttribute("Id_ServentiaCargo", serventiaCargoDt.getId());
					request.setAttribute("ServentiaCargo", serventiaCargoDt.getServentiaCargo());

				}
				pendenciaDt.setHash(hashPendenciaPai);
				request.getSession().setAttribute("PendenciaReabrirDt", pendenciaDt);
			} else
				throw new MensagemException("Não foi informado o ID da pendência pai.");
		} else
			pendenciaDt = (PendenciaDt) request.getSession().getAttribute("PendenciaReabrirDt");

		// Se tem modelo selecionado, logo monta o modelo
		if (modeloDt.getId() != null && modeloDt.getId().equals("") == false) modeloDt.setTexto(pendenciaNe.montaModelo(pendenciaDt, modeloDt, usuarioSessao));

		// Atribui valores basicos para o funcionamento de pendencias
		this.atributosEdicaoPendencia(request, serventiaDt, null, null, null, serventiaCargoDt, arquivoTipoDt, pendenciaTipoDt, modeloDt);

		request.setAttribute("PaginaAtual", Configuracao.Editar);

		if (reabrir) {
			// Atribui valores de log
			pendenciaDt.setId_UsuarioLog(usuarioSessao.getId_Usuario());
			pendenciaDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());

			// Atribui os novos dados para os responsaveis
			PendenciaResponsavelDt responsavel = pendenciaDt.getResponsavel();

			if (responsavel != null) {
				if (!serventiaCargoDt.getId().equals("")) {
					if (responsavel.getId_ServentiaCargo().equals(serventiaCargoDt.getId())) responsavel.setId_ServentiaCargo(serventiaCargoDt.getId());
					else {
						pendenciaDt.getResponsaveis().clear();
						responsavel = new PendenciaResponsavelDt();
						responsavel.setIpComputadorLog(pendenciaDt.getIpComputadorLog());
						responsavel.setId_UsuarioLog(pendenciaDt.getId_UsuarioLog());
						responsavel.setId_ServentiaCargo(serventiaCargoDt.getId());
						pendenciaDt.addResponsavel(responsavel);
					}
				}

				if (!serventiaDt.getId().equals("") && serventiaCargoDt.getId().equals("")) {
					if (responsavel.getId_Serventia().equals(serventiaDt.getId())) responsavel.setId_Serventia(serventiaDt.getId());
					else {
						pendenciaDt.getResponsaveis().clear();
						responsavel = new PendenciaResponsavelDt();
						responsavel.setIpComputadorLog(pendenciaDt.getIpComputadorLog());
						responsavel.setId_UsuarioLog(pendenciaDt.getId_UsuarioLog());
						responsavel.setId_Serventia(serventiaDt.getId());
						pendenciaDt.addResponsavel(responsavel);
					}
				}
			}

			try{
				// Recebe os arquivos da sessao
				Map arquivos = (HashMap) request.getSession().getAttribute("ListaArquivos");
				String msgErro = pendenciaNe.verificarReabrirPendencia(pendenciaDt, arquivos, usuarioSessao.getUsuarioDt());
				if (msgErro.equals("")) {
					pendenciaNe.reabrir(pendenciaDt, arquivos, usuarioSessao.getUsuarioDt());
					request.setAttribute("MensagemOk", "Pendência reaberta com sucesso");
				} else {
					request.setAttribute("MensagemErro", msgErro);
				}
			} catch(Exception ex) {
				throw new MensagemException("Erro ao Reabrir Pendência. reabrir(): " + ex.getMessage());
			}
		}

		// Retorna a pendencia configurada
		return pendenciaDt;
	}

	/**
	 * Controla a criacao de pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 10/10/2008 15:24
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, vo de pendencia
	 * @param ServentiaDt
	 *            serventiaDt, vo de serventia
	 * @param ServentiaTipoDt
	 *            serventiaTipoDt, vo de serventiatipo
	 * @param PendenciaTipoDt
	 *            pendenciaTipoDt, vo de pendencia tipo
	 * @param UsuarioNe
	 *            usuarioSessao, bo de usuario
	 * @return boolean
	 * @throws Exception
	 */
	protected PendenciaDt criarPendencia(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, ServentiaDt serventiaExpedirDt, ServentiaTipoDt serventiaTipoDt, PendenciaTipoDt pendenciaTipoDt, ArquivoTipoDt arquivoTipoDt, ModeloDt modeloDt, UsuarioNe usuarioSessao) throws Exception{

		// Verifica as operacoes a executar
		if (request.getParameter("operacao") != null) {
			if (request.getParameter("operacao").equals("Confirmar")) {
				String msgErro = "";

				if (pendenciaDt.getResponsaveis() != null) pendenciaDt.getResponsaveis().clear();

				PendenciaResponsavelDt responsavel = new PendenciaResponsavelDt();

				if (usuarioSessao.getUsuarioDt().getGrupoTipoCodigoToInt() == GrupoTipoDt.ADVOGADO) {
					// seta o advogado como responsável pela pendencia que ele
					// cadastra
					responsavel.setId_UsuarioResponsavel(usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
					// distribui a pendência para ele
					pendenciaDt.setId_UsuarioFinalizador(usuarioSessao.getUsuarioDt().getId_UsuarioServentia());
					// seta data visto
					pendenciaDt.setDataVisto(Funcoes.DataHora(new Date()));

				} else if ((serventiaExpedirDt == null || serventiaExpedirDt.getId().equals("")) && (serventiaTipoDt != null && !serventiaTipoDt.getId().equals(""))) {

					responsavel.setId_ServentiaTipo(serventiaTipoDt.getId());

				} else if (serventiaExpedirDt != null && !serventiaExpedirDt.getId().equals("")) {
					responsavel.setId_Serventia(serventiaExpedirDt.getId());

				} else {
					msgErro = "Selecione uma Serventia ou uma Serventia Tipo.";
				}

				if (msgErro.length() == 0) {
					// Adiciona o responsavel para a pendencia
					pendenciaDt.addResponsavel(responsavel);
					pendenciaDt.setId_PendenciaTipo(pendenciaTipoDt.getId());

					try{
						// Recebe os arquivos da sessao
						Map arquivos = (HashMap) request.getSession().getAttribute("ListaArquivos");
						pendenciaNe.criar(pendenciaDt, arquivos, usuarioSessao.getUsuarioDt());
						request.setAttribute("MensagemOk", "Pendência criada com sucesso");
						request.getSession().setAttribute("ultimaAba", 0);
						this.limparArquivosSessao(request);

						pendenciaDt.limpar();
						arquivoTipoDt.limpar();
						pendenciaTipoDt.limpar();
						serventiaTipoDt.limpar();
						serventiaExpedirDt.limpar();
					} catch(Exception ex) {
						request.setAttribute("MensagemErro", ex.getMessage());
					}
				} else
					request.setAttribute("MensagemErro", msgErro);

			} else if (request.getParameter("operacao").equals("Criar")) {
				request.setAttribute("operacaoPendencia", "Confirmar");
				request.setAttribute("Mensagem", "Para confirmar a operação \"" + request.getParameter("operacao") + "\" clique no botão \"Confirmar\".");
			}
		}

		// Auxiliar para armazenar a ultima aba
		request.getSession().setAttribute("ultimaAba", request.getSession().getAttribute("ultimaAba") == null ? 0 : request.getSession().getAttribute("ultimaAba"));

		// Se tem modelo selecionado, logo monta o modelo
		if (modeloDt.getId() != null && modeloDt.getId().equals("") == false) modeloDt.setTexto(pendenciaNe.montaModelo(pendenciaDt, modeloDt, usuarioSessao));

		// Atribui variaveis necessarias para edicao de arquivos
		this.atributosEdicaoPendencia(request, null, serventiaExpedirDt, serventiaTipoDt, null, null, arquivoTipoDt, pendenciaTipoDt, modeloDt);

		return pendenciaDt;
	}

	/**
	 * Atribui no request os atributos necessarios para edicao e manipulacao de
	 * pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 10/10/2008 15:31
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param ServentiaDt
	 *            serventiaDt, vo de serventia
	 * @param ArquivoTipoDt
	 *            arquivoTipoDt, vo de arquivo tipo
	 * @param PendenciaTipoDt
	 *            pendenciaTipoDt, vo de pendencia tipo
	 * @param ModeloDt
	 *            modeloDt, vo do modelo
	 * @throws Exception
	 */
	protected void atributosEdicaoPendencia(HttpServletRequest request, ServentiaDt serventiaDt, ServentiaDt serventiaExpedirDt, ServentiaTipoDt serventiaTipoDt, ComarcaDt comarcaDt, ServentiaCargoDt serventiaCargoDt, ArquivoTipoDt arquivoTipoDt, PendenciaTipoDt pendenciaTipoDt, ModeloDt modeloDt){
		// Atribui as descricoes das variaveis

		if (serventiaDt != null) {
			request.setAttribute("Id_Serventia", serventiaDt.getId());
			request.setAttribute("Serventia", serventiaDt.getServentia());
		}

		if (serventiaExpedirDt != null) {
			request.setAttribute("Id_ServentiaExpedir", serventiaExpedirDt.getId());
			request.setAttribute("ServentiaExpedir", serventiaExpedirDt.getServentia());
		}

		if (serventiaTipoDt != null) {
			request.setAttribute("Id_ServentiaTipo", serventiaTipoDt.getId());
			request.setAttribute("ServentiaTipo", serventiaTipoDt.getServentiaTipo());
		}

		if (comarcaDt != null) {
			request.setAttribute("Id_Comarca", comarcaDt.getId());
			request.setAttribute("Comarca", comarcaDt.getComarca());
		}

		if (serventiaCargoDt != null) {
			request.setAttribute("Id_ServentiaCargo", serventiaCargoDt.getId());
			request.setAttribute("ServentiaCargo", serventiaCargoDt.getServentiaCargo());
		}

		if (arquivoTipoDt != null) {
			request.setAttribute("Id_ArquivoTipo", arquivoTipoDt.getId());
			request.setAttribute("ArquivoTipo", arquivoTipoDt.getArquivoTipo());
		}

		if (pendenciaTipoDt != null) {
			request.setAttribute("Id_PendenciaTipo", pendenciaTipoDt.getId());
			request.setAttribute("PendenciaTipo", pendenciaTipoDt.getPendenciaTipo());
		}

		if (modeloDt != null) {
			request.setAttribute("Id_Modelo", modeloDt.getId());
			request.setAttribute("Modelo", modeloDt.getModelo());
		}

		// Atributos Editor de texto
		String texto = (String) request.getSession().getAttribute("TextoEditor");

		if (texto != null && !texto.equals("")) request.setAttribute("TextoEditor", texto);
		else
			request.setAttribute("TextoEditor", modeloDt != null ? modeloDt.getTexto() : "");
	}

	/**
	 * Encaminha uma pendencia para outro destino
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 26/09/2008 16:51
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param PendenciaNe
	 *            pendenciaNe
	 * @param PendenciaDt
	 *            pendenciaDt
	 * @param UsuarioNe
	 *            usuarioSessao
	 * @throws Exception 
	 */
	protected void encaminhar(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, ServentiaDt serventiaDt, ServentiaCargoDt serventiaCargoDt, UsuarioNe usuarioSessao) throws Exception{
		
			pendenciaNe.encaminhar(pendenciaDt, serventiaDt, serventiaCargoDt, usuarioSessao);
			request.setAttribute("MensagemOk", "Pendência encaminhada com sucesso");
		
	}
	
	/**
	 * Efetuar a troca do tipo de uma pendencia
	 * 
	 * @author lsbernardes
	 
	 * @param PendenciaNe
	 *            pendenciaNe
	 * @param PendenciaDt
	 *            pendenciaDt
	 * @param novoCodPendenciaTipo
	 *            Novo tipo de Pendência
	 * @throws Exception 
	 */
	protected void alterarTipoPendencia(PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, String novoCodPendenciaTipo) throws Exception{
		pendenciaNe.alterarTipoPendencia(pendenciaDt, novoCodPendenciaTipo, null);
	}

	/**
	 * Salva Resolucao da pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 24/06/2008 14:33 Alteracoes
	 * @author Ronneesley Moura Teles
	 * @since 03/09/2008 15:15 Modificacao na funcao de insercao do arquivo,
	 *        modificacao no retorno
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio da pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, pojo de pendencia
	 * @param ArquivoTipoDt
	 *            arquivoTipo, tipo de arquivo
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio da pendencia
	 * @return boolean
	 * @throws Exception 
	 */
	protected boolean salvaResolucao(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, ArquivoTipoDt arquivoTipo, UsuarioNe usuarioNe, ComarcaDt comarcaDt, ServentiaDt serventiaExpedirDt, ServentiaTipoDt serventiaTipoExpedirDt) throws Exception{
		// Recebe os arquivos da sessao
		Map arquivos = (HashMap) request.getSession().getAttribute("ListaArquivos");

		if (arquivos != null && arquivos.size() > 0) {
			// Tenta inserir os arquivos ja verificando
			if (pendenciaNe.salvaResolucao(pendenciaDt, Funcoes.converterMapParaList(arquivos), true, usuarioNe.getUsuarioDt(), comarcaDt, serventiaExpedirDt, serventiaTipoExpedirDt)) {
				request.setAttribute("MensagemOk", "Arquivo(s) adicionado(s) com sucesso");
				limparArquivosSessao(request);
			} else {
				request.setAttribute("MensagemErro", "Houve falha(s) na adição, tente novamente");
				return false;
			}
		} else {
			request.setAttribute("MensagemErro", "Lista de arquivos vazia");
			return false;
		}

		return true;
	}

	/**
	 * Limpar arquivos da sessao
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 09/01/2008 085:31
	 * @param request
	 *            objeto de request
	 */
	protected void limparArquivosSessao(HttpServletRequest request) {
		Map arquivos = (HashMap) request.getSession().getAttribute("ListaArquivos");

		// Limpa as listas de arquivos
		if (arquivos != null) arquivos.clear();
		
	}

	/**
	 * Visualizar a pendencia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 16/12/2008 13:30
	 * @param request
	 *            dados de request
	 * @param pendenciaNe
	 *            objeto de negocio para pendencia
	 * @param pendenciaDt
	 *            vo de pendencia
	 * @param usuarioNe
	 *            objeto de negocio do usuario, usuario que esta logado
	 * @throws Exception
	 */
	protected void visualizarPendencia(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, UsuarioNe usuarioNe) throws Exception {
		// Auxiliar para armazenar a ultima aba
		request.getSession().setAttribute("ultimaAba", request.getSession().getAttribute("ultimaAba") == null ? 0 : request.getSession().getAttribute("ultimaAba"));

		request.setAttribute("linkArquivo", "PendenciaArquivo");
		request.setAttribute("campoArquivo", "Id_PendenciaArquivo");
		request.setAttribute("paginaArquivo", Configuracao.Curinga6);

		request.setAttribute("ListaArquivos", pendenciaNe.consultarArquivosAssinadosComHash(pendenciaDt, usuarioNe));
	}

	/**
	 * Lista pendencias
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/06/2008 16:51
	 * @param HttpServletRequest
	 *            request,
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio da pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, pojo da pendencia
	 * @param PendenciaTipoDt
	 *            pendenciaTipoDt, pojo do tipo de pendencia
	 * @param ArquivoTipoDt
	 *            arquivoTipoDt, pojo do tipo de arquivo
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio do usuario [ utiliza-se o usuario
	 *            da sessao ]
	 * @param ModeloDt
	 *            modeloDt, pojo do modelo
	 * @param String
	 *            idPendencia, id da pendencia
	 * @throws Exception
	 */
	/*
	 * private void pegarPendencia(HttpServletRequest request, PendenciaNe
	 * pendenciaNe, PendenciaDt pendenciaDt, PendenciaTipoDt pendenciaTipoDt,
	 * ArquivoTipoDt arquivoTipoDt, UsuarioNe usuarioNe, ModeloDt modeloDt,
	 * String idPendencia, ServentiaDt serventiaDt, ServentiaDt
	 * serventiaExpedirDt, ServentiaTipoDt serventiaTipoDt, ServentiaCargoDt
	 * serventiaCargoDt){
	 * 
	 * if (idPendencia == null || idPendencia.equals("")) {try{if
	 * (request.getParameter("tipo") != null){int tipo =
	 * Funcoes.StringToInt(request.getParameter("tipo")); //Receber por parametro
	 * //Consulta a ultima pendencia da serventia do usuario
	 * pendenciaDt.copiar(pendenciaNe.consultarUltimaPendencia(tipo,
	 * usuarioNe.getUsuarioDt())); //Apos reservar a pendencia, distribui a para
	 * o usuario corrente pendenciaNe.distribuir(pendenciaDt.getId(),
	 * usuarioNe.getUsuarioDt().getId_UsuarioServentia()); idPendencia =
	 * pendenciaDt.getId(); request.getSession().setAttribute("PendenciaTipodt",
	 * null); } else {idPendencia = null; } } catch(Exception ex){throw new
	 * Exception("<{Erro ao pegar Pendência.}> Local Exception: " +
	 * this.getClass().getName() + ".pegarPendencia(): " + ex.getMessage()); } }
	 * else {//se pendencia selecionada diferente da pendencia na sessão if
	 * (pendenciaDt.getId() == null || pendenciaDt.getId().trim().equals("") ||
	 * !pendenciaDt.getId().equals(idPendencia))
	 * pendenciaDt.copiar(pendenciaNe.consultarPendenciaId(idPendencia,
	 * usuarioNe));
	 * 
	 * }
	 * 
	 * pendenciaTipoDt = (PendenciaTipoDt)
	 * request.getSession().getAttribute("PendenciaTipodt"); if
	 * ((pendenciaTipoDt == null || pendenciaTipoDt.getId().equals("")) &&
	 * (arquivoTipoDt.getId()== null || arquivoTipoDt.getId().equals(""))){
	 * pendenciaTipoDt =
	 * pendenciaNe.consultarPendenciaTipoId(pendenciaDt.getId_PendenciaTipo());
	 * arquivoTipoDt.setId(pendenciaTipoDt.getId_ArquivoTipo());
	 * arquivoTipoDt.setArquivoTipo(pendenciaTipoDt.getArquivoTipo()); }
	 * 
	 * //Se tem modelo selecionado, logo monta o modelo if (modeloDt.getId() !=
	 * null && modeloDt.getId().equals("") == false) modeloDt.setTexto(
	 * pendenciaNe.montaModelo(pendenciaDt, modeloDt, usuarioNe) );
	 * 
	 * //Atribui as descricoes das variaveis
	 * this.atributosEdicaoPendencia(request, serventiaDt, serventiaExpedirDt,
	 * serventiaTipoDt, serventiaCargoDt, arquivoTipoDt, pendenciaTipoDt,
	 * modeloDt);
	 * 
	 * request.setAttribute("PassoInsercao", "");
	 * request.setAttribute("PassoBusca", "");
	 * 
	 * //Auxiliar para armazenar a ultima aba
	 * request.getSession().setAttribute("ultimaAba",
	 * request.getSession().getAttribute("ultimaAba") ==
	 * null?1:request.getSession().getAttribute("ultimaAba"));
	 * 
	 * }
	 */

	protected void setAtributosPendencia(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, PendenciaTipoDt pendenciaTipoDt, ArquivoTipoDt arquivoTipoDt, UsuarioNe usuarioNe, ModeloDt modeloDt, ServentiaDt serventiaDt, ServentiaDt serventiaExpedirDt, ServentiaTipoDt serventiaTipoDt, ComarcaDt comarcaDt, ServentiaCargoDt serventiaCargoDt, boolean carregarArquivoTipo) throws Exception{

		pendenciaTipoDt = (PendenciaTipoDt) request.getSession().getAttribute("PendenciaTipodt");
		if (carregarArquivoTipo && (pendenciaTipoDt == null || pendenciaTipoDt.getId().equals("")) && (arquivoTipoDt.getId() == null || arquivoTipoDt.getId().equals(""))) {
			pendenciaTipoDt = pendenciaNe.consultarPendenciaTipoId(pendenciaDt.getId_PendenciaTipo());
			arquivoTipoDt.setId(pendenciaTipoDt.getId_ArquivoTipo());
			arquivoTipoDt.setArquivoTipo(pendenciaTipoDt.getArquivoTipo());
		}
		
		//todo: analisar o impacto de tirar este trecho, para verificar se está redundante com o trecho do if algumas linhas abaixo.
		int numeroMandadoReservadoPreanalise = Funcoes.StringToInt(request.getParameter("numeroMandadoReservadoPreanalise"));
		if( pendenciaDt.getNumeroReservadoMandadoExpedir() == null){
			pendenciaDt.setNumeroReservadoMandadoExpedir(String.valueOf(numeroMandadoReservadoPreanalise));
		}

		boolean isMandadoSendoExpedidoCentral = pendenciaDt.isMandado() && !(usuarioNe.isOficial() && pendenciaDt.isAguardandoRetorno());
		
		// Se tem modelo selecionado, logo monta o modelo
		if (modeloDt.getId() != null && modeloDt.getId().equals("") == false) { 
			modeloDt.setTexto(pendenciaNe.montaModelo(pendenciaDt, modeloDt, usuarioNe));
			
			// Se for um mandado, pegar a quantidade de locomoções do modelo genérico assim que carregar ele.
			// Esta informação é obrigatória e, caso não esteja contida, irá lançar uma mensagem de exceção.
			if( (serventiaExpedirDt != null && pendenciaNe.isCentralMandados(serventiaExpedirDt.getId(), null)) && isMandadoSendoExpedidoCentral){
				//O modelo deve ser obrigatoriamente genério, ou seja, sem id de serventia.
				if(modeloDt.getId_Serventia() == null || modeloDt.getId_Serventia().isEmpty()){
					
					//A quantidade de locomoções vinculada ao modelo encontra-se na coluna qtd_locomocao da tabela modelo.
					pendenciaDt.setQtdLocomocoesMandado(Funcoes.StringToInt(pendenciaNe.consultarQtdLocomocao(modeloDt.getId())));
					
					// Substitui o número reservado para o mandado em alguma variável para este número que for encontrada no texto.
					modeloDt.setTexto(pendenciaNe.montaConteudoMandadoExpedicao(pendenciaDt.getNumeroReservadoMandadoExpedir(), modeloDt.getTexto()));
				}
				else {
					throw new MensagemException("Para mandados, é obrigatório o uso de um modelo genérico.");
				}
				
			}
		}
		else
			modeloDt.setTexto("");
		
		if(isMandadoSendoExpedidoCentral) {
			
			//Especificamente se tiver expedindo através de uma pré-análise, o parâmetro de número de locomoções virá da tela do usuário que estiver
			//assinando e expedindo ela. Isto é necessário porque o arquivo de pré-análise é montado na tela do lado do cliente, através de javascript,
			//carregando por uma função ajax. Abaixo, sempre que a quantidade de locomoção não puder ser identificada através do modelo, verificamos se
			//esta variável está chegando de uma tela de pré-análise e usamos ela se for o caso.
			if(pendenciaDt.getQtdLocomocoesMandado() == 0 ){
				pendenciaDt.setQtdLocomocoesMandado(Funcoes.StringToInt(request.getParameter("qtdLocomocoesMandado")));
			}
			
			if(pendenciaDt.getQtdLocomocoesMandado() < 1 && pendenciaDt.getQtdLocomocoesMandado() > 5 ) {
				throw new MensagemException("Não foi possível identificar a quantidade de locomoções a ser usada para este modelo.");
			}
			
			//Epecificamente se tiver abrindo uma pré-análise, deve pegar o parâmetro vindo da tela que traz o número que foi reservado para o mandado no momento
			//da criação da pré-análise. Isto é necessário porque o número provavelmente estará no teor da pré-análise e se pegar um novo número ficará inconsistente.
			if(Funcoes.StringToInt(request.getParameter("numeroMandadoReservadoPreanalise")) != 0 ) {
				pendenciaDt.setNumeroReservadoMandadoExpedir(request.getParameter("numeroMandadoReservadoPreanalise"));
			}
		}
		
		// Se a pendencia é uma precatória já seleciona a serventia do usuário logado
		if (serventiaDt != null && serventiaDt.getId().equals("") && pendenciaDt != null && pendenciaDt.getPendenciaTipoCodigo() != null 
				&& (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.CARTA_PRECATORIA || pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.ELABORACAO_VOTO)) {
			serventiaDt.setId(usuarioNe.getUsuarioDt().getId_Serventia());
			serventiaDt.setServentia(usuarioNe.getUsuarioDt().getServentia());
		}
		
		/**
		 * Todos os tratamentos da aba de encaminhamento de pendencias
		 * 
		 * @author Ronneesley Moura Teles
		 * @since 26/09/2008
		 */
		// Atribui as descricoes das variaveis
		this.atributosEdicaoPendencia(request, serventiaDt, serventiaExpedirDt, serventiaTipoDt, comarcaDt, serventiaCargoDt, arquivoTipoDt, pendenciaTipoDt, modeloDt);

		request.setAttribute("PassoInsercao", "");
		request.setAttribute("PassoBusca", "");

		// Auxiliar para armazenar a ultima aba
		request.getSession().setAttribute("ultimaAba", request.getSession().getAttribute("ultimaAba") == null ? 0 : request.getSession().getAttribute("ultimaAba"));

	}

	/**
	 * Lista de pendencias reservadas do usuario para solucionar ou liberar
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/06/2008 16:28
	 * @param HttpServletRequest
	 *            request
	 * @param HttpServletResponse
	 *            response
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio da pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, pojo da pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio do usuario
	 * @param PendenciaTipoDt
	 *            pendenciaTipoDt, pojo do tipo de pendencia
	 * @param String
	 *            tempNomeBusca, nome da busca
	 * @param String
	 *            PosicaoPaginaAtual, posicao da pagina
	 * @param String
	 *            pendencia, numero da pendencia
	 * @param int fluxo
	 * @throws Exception
	 */
	protected void listaReservadasUsuarioSolucionarLiberar(HttpServletRequest request, HttpServletResponse response, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, ServentiaDt serventiaDt, UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, String pendencia, int fluxo) throws Exception{

		switch (fluxo) {
		case 1: // Liberar uma pendencia
			if (request.getParameter("operacao") != null) {
				if (request.getParameter("operacao").equals("LiberarSelecionadas")) {
					if (request.getParameterValues("pendenciaSelecionada[]") != null) {
						String pendencias[] = request.getParameterValues("pendenciaSelecionada[]");

						int qtd = 0;
						for (String valor : pendencias) {
							String valores[] = valor.split("@#;#@");
							//primeira posição id_pendencia
							//segunda posilçao hash da pendencia
							if (valores.length==2 && !valores[0].equals("0")) {
								String idPendencia = valores[0];
								String hash = valores[1];

								if (usuarioNe.VerificarCodigoHash(idPendencia, hash)) {
									pendenciaNe.liberar(idPendencia);
									qtd++;
								} else {
									request.setAttribute("MensagemErro", "Ocorreu um erro ao liberar uma pendência");
								}
							}
						}

						if (qtd == 0) request.setAttribute("MensagemErro", "Nenhuma pendência foi liberada");
						else if (qtd == 1) request.setAttribute("MensagemOk", "Foi liberada " + qtd + " pendência");
						else
							request.setAttribute("MensagemOk", "Foram liberadas " + qtd + " pendências");
					} else {
						request.setAttribute("MensagemErro", "Selecione pelo menos uma pendência");
					}
				}
			} else {
				if (request.getParameter("pendencia") != null) {
					if (usuarioNe.VerificarCodigoHash(request.getParameter("pendencia"), request.getParameter("hash"))) {
						pendenciaNe.liberar(request.getParameter("pendencia"));
						request.setAttribute("MensagemOk", "Pendência liberada com sucesso");
					} else {
						request.setAttribute("MensagemErro", "Esta pendência não pode ser liberada");
					}
				}
			}

			// case não possui break devido a lógica

		default:
			// Consulta as pendencias reservadas
			this.listaReservadasPreAnalisadas(request, pendenciaNe, usuarioNe, false);
			break;
		}

	}

	/**
	 * Lista de pendencias abertas
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 23/06/2008 15:58
	 * @param HttpServletRequest
	 *            request, request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio de pendencia
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio de usuario
	 * @throws Exception
	 */
	protected void listaAbertas(HttpServletRequest request, PendenciaNe pendenciaNe, UsuarioNe usuarioNe, PendenciaTipoDt pendenciaTipoDt, PendenciaStatusDt pendenciaStatusDt) throws Exception{
		// Verifica primeiro as distribuicoes
		if (request.getParameter("operacao") != null) {
			if (request.getParameter("operacao").equals("Distribuir")) {
				if (request.getParameterValues("pendencia[]") != null) {
					String pendencias[] = request.getParameterValues("pendencia[]");

					int qtd = 0;
					for (String pendencia : pendencias) {
						String idUsuarioServentia = request.getParameter("pendencia[" + pendencia + "]");

						if (!idUsuarioServentia.equals("0")) {
							pendenciaNe.distribuir(pendencia, idUsuarioServentia, usuarioNe.getUsuarioDt().getGrupoCodigoToInt());
							qtd++;
						}
					}

					if (qtd == 0) request.setAttribute("MensagemErro", "Nenhuma pendência foi distribuída.");
					else if (qtd == 1) request.setAttribute("MensagemOk", "Foi distribuída uma pendência.");
					else
						request.setAttribute("MensagemOk", "Foram distribuídas " + qtd + " pendências.");
				} else {
					request.setAttribute("MensagemErro", "Deve ser selecionado ao menos um usuário para resolver a pendência.");
				}
			}
		}

		boolean prioridade = true;
		// somenteDeProcessos = false;
		int filtroTipo = 3;
		int filtroCivelCriminal = 3;
		String numero_processo = "";

		if (request.getParameter("numeroProcesso") != null && request.getParameter("numeroProcesso").trim().equals("") == false) {
			numero_processo = request.getParameter("numeroProcesso").toString();
		}

		if (request.getParameter("prioridade") == null || request.getParameter("prioridade").trim().equals("1") == false) {
			prioridade = false;
		}

		if (request.getParameter("somenteDeProcessos") != null && request.getParameter("somenteDeProcessos").trim().equals("1")) {
			// somenteDeProcessos = true;
			filtroTipo = 1;
		}

		List tempList = new ArrayList();//pendenciaNe.consultarAbertas(usuarioNe, pendenciaTipoDt, pendenciaStatusDt, prioridade, filtroTipo, filtroCivelCriminal, numero_processo, null, null, "0");
		
		List usuarios = pendenciaNe.consultarTodosUsuariosAnalisamPendencias(usuarioNe.getUsuarioDt().getId_Serventia());

		request.setAttribute("Id_PendenciaStatus", pendenciaStatusDt.getId());
		request.setAttribute("PendenciaStatus", pendenciaStatusDt.getPendenciaStatus());

		request.setAttribute("Id_PendenciaTipo", pendenciaTipoDt.getId());
		request.setAttribute("PendenciaTipo", pendenciaTipoDt.getPendenciaTipo());

		request.setAttribute("ListaPendencia", tempList);
		request.setAttribute("usuarios", usuarios);
		request.setAttribute("prioridade", prioridade);
		request.setAttribute("filtroTipo", String.valueOf(filtroTipo));
		request.setAttribute("filtroCivelCriminal", String.valueOf(filtroCivelCriminal));
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
	}

	/**
	 * Lista de pendencias reservadas, pré-analisadas ou todas
	 * 
	 * @author Leandro Bernardes
	 * @since 23/06/2008 14:24
	 * @param HttpServletRequest
	 *            request, objeto de request
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocios do usuario
	 * @throws Exception
	 */
	protected void listaReservadasPreAnalisadas(HttpServletRequest request, PendenciaNe pendenciaNe, UsuarioNe usuarioNe, boolean ehPendenteAssinatura) throws Exception{
		List listaReservas = null;
		String tipoPendencia = request.getParameter("TipoPendencia");
		String filtroResponsavel = request.getParameter("Filtro");

		if(usuarioNe.isOficial() && pendenciaNe.temCentralProjudiImplantada(usuarioNe.getId_Serventia())) {
			//Se tiver central implantada e for um oficial, direcionando todos os botões da consulta de pendências reservadas
			//para a mesma consulta feita especificamente para o oficial. Não impactará usuários fora do piloto da Central (Senador Canedo).
			//Esta alteração é temporária porque uma nova tela para consulta destas pendências está sendo desenvolvida especificamente
			//para os oficiais da Central do Projudi.
			String ordenacao = request.getParameter("ordenacao");
			listaReservas = pendenciaNe.consultarMandadosReservadosComHash(usuarioNe, String.valueOf(PendenciaTipoDt.MANDADO), "3", ordenacao);
			
		} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("ConsultarReservadas")) {
			listaReservas = pendenciaNe.consultarReservadasComHash(usuarioNe);

		} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("ConsultarPreAnalisadas")) {
			listaReservas = pendenciaNe.consultarPreAnalisadasComHash(usuarioNe);

		} else if (request.getParameter("operacao") != null && request.getParameter("operacao").equalsIgnoreCase("Consultar")) {
			listaReservas = pendenciaNe.consultarReservadasPreAnalisadasComHash(usuarioNe);

		} else if (request.getParameter("opcao") != null && request.getParameter("opcao").equalsIgnoreCase("AbertasServentiaCargo")) {
			listaReservas = pendenciaNe.consultarAbertasServentiaCargoComHash(usuarioNe, tipoPendencia, ehPendenteAssinatura);
			request.setAttribute("TipoPendencia", tipoPendencia);
			request.setAttribute("opcao", request.getParameter("opcao"));

		} else if (request.getParameter("opcao") != null && request.getParameter("opcao").equalsIgnoreCase("PreAnalisadas")) {
			listaReservas = pendenciaNe.consultarPreAnalisadasComHash(usuarioNe, tipoPendencia, filtroResponsavel);
			request.setAttribute("Filtro", filtroResponsavel);
			request.setAttribute("TipoPendencia", tipoPendencia);
			request.setAttribute("opcao", request.getParameter("opcao"));

		} else if (request.getParameter("opcao") != null && request.getParameter("opcao").equalsIgnoreCase("Reservadas")) {
			listaReservas = pendenciaNe.consultarReservadasComHash(usuarioNe, tipoPendencia, filtroResponsavel);
			request.setAttribute("Filtro", filtroResponsavel);
			request.setAttribute("TipoPendencia", tipoPendencia);
			request.setAttribute("opcao", request.getParameter("opcao"));

		} else
			listaReservas = pendenciaNe.consultarReservadasPreAnalisadasComHash(usuarioNe);

		request.setAttribute("ListaReservas", listaReservas);
	}

	/**
	 * Limpa Dt's relacionados a pendência
	 * 
	 * @author Leandro Bernardes
	 * @since 04/08/2009
	 * @param PendenciaDt
	 *            pendenciaDt, objeto da pendencia
	 * @param PendenciaTipoDt
	 *            pendenciaDt, objeto pendencia tipo
	 * @param ServentiaDt
	 *            serventiaDt, objeto serventia
	 * @param ServentiaDt
	 *            serventiaTipoDt, objeto serventia tipo
	 * @param ArquivoTipoDt
	 *            arquivoTipoDt, objeto arquivo tipo
	 * @param ModeloDt
	 *            modeloDt, objeto modelo
	 */
	protected void limparObjtosRelacionadosPendencia(PendenciaDt pendenciaDt, PendenciaStatusDt pendenciaStatusDt, ServentiaDt serventiaDt, ServentiaDt serventiaExpedirDt, ServentiaTipoDt serventiaTipoDt, ServentiaCargoDt serventiaCargoDt, ArquivoTipoDt arquivoTipoDt, PendenciaTipoDt pendenciaTipoDt, ModeloDt modeloDt) {
		pendenciaDt.limpar();
		pendenciaStatusDt.limpar();
		serventiaDt.limpar();
		serventiaExpedirDt.limpar();
		serventiaTipoDt.limpar();
		serventiaCargoDt.limpar();
		arquivoTipoDt.limpar();
		pendenciaTipoDt.limpar();
		modeloDt.limpar();
	}

	/**
	 * verifica se a pendência é do tipo "verificar"
	 * 
	 * @param pendenciaDt
	 * @return verdadeiro ou falso
	 */
	protected boolean pendenciaTipoVerificar(PendenciaDt pendenciaDt) {
		boolean retorno = false;
		Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();

		if (tipoPendencia == PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO || tipoPendencia == PendenciaTipoDt.VERIFICAR_GUIA_PENDENTE || tipoPendencia == PendenciaTipoDt.VERIFICAR_PROCESSO || tipoPendencia == PendenciaTipoDt.VERIFICAR_PETICAO 
				|| tipoPendencia == PendenciaTipoDt.VERIFICAR_PARECER || tipoPendencia == PendenciaTipoDt.SUSPENSAO_PROCESSO || tipoPendencia == PendenciaTipoDt.VERIFICAR_DOCUMENTO || tipoPendencia == PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO 
				|| tipoPendencia == PendenciaTipoDt.AGUARDANDO_PRAZO_DECADENCIAL || tipoPendencia == PendenciaTipoDt.VERIFICAR_CONEXAO || tipoPendencia == PendenciaTipoDt.CALCULAR_LIQUIDACAO_PENAS
				|| tipoPendencia == PendenciaTipoDt.REVISAO || tipoPendencia == PendenciaTipoDt.RELATORIO || tipoPendencia == PendenciaTipoDt.PEDIDO_VISTA || tipoPendencia == PendenciaTipoDt.AGUARDANDO_CUMPRIMENTO_PENA 
				|| tipoPendencia == PendenciaTipoDt.EFETIVAR_EVENTO || tipoPendencia == PendenciaTipoDt.VERIFICAR_GUIA || tipoPendencia == PendenciaTipoDt.VERIFICAR_GUIA_PAGA || tipoPendencia == PendenciaTipoDt.VERIFICAR_GUIA_VENCIDA 
				|| tipoPendencia == PendenciaTipoDt.VERIFICAR_CALCULO || tipoPendencia == PendenciaTipoDt.VERIFICAR_DEVOLUCAO_PRECATORIA || tipoPendencia == PendenciaTipoDt.VERIFICAR_NOVA_PRECATORIA 
				|| tipoPendencia == PendenciaTipoDt.VERIFICAR_PRECATORIA || tipoPendencia == PendenciaTipoDt.VERIFICAR_FATO
				|| tipoPendencia == PendenciaTipoDt.VERIFICAR_RESPOSTA_CAMARA_SAUDE || tipoPendencia == PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_LAUDO_RELATORIO ||  tipoPendencia == PendenciaTipoDt.VERIFICAR_RESPOSTA_PEDIDO_CENOPES
				|| tipoPendencia == PendenciaTipoDt.VERIFICAR_RECURSO_REPETITIVO || tipoPendencia == PendenciaTipoDt.VERIFICAR_OFICIO_COMUNICATORIO || tipoPendencia == PendenciaTipoDt.VERIFICAR_RETORNO_AR_CORREIOS
				|| tipoPendencia == PendenciaTipoDt.VERIFICAR_CLASSE_PROCESSUAL  || tipoPendencia == PendenciaTipoDt.VERIFICAR_RESPOSTA_OFICIO_DELEGACIA || tipoPendencia == PendenciaTipoDt.VERIFICAR_ENDERECO_PARTE	 		
				|| tipoPendencia == PendenciaTipoDt.AGUARDANDO_DECURSO_PRAZO_PRISAO_CIVIL || tipoPendencia == PendenciaTipoDt.CONFIRMAR_DISTRIBUICAO  || tipoPendencia == PendenciaTipoDt.VERIFICAR_NOVO_PROCESSO_PEDIDO_ASSISTENCIA
				|| tipoPendencia == PendenciaTipoDt.VERIFICAR_TEMA_TRANSITADO_JULGADO || tipoPendencia == PendenciaTipoDt.VERIFICAR_PROCESSO_HIBRIDO || tipoPendencia == PendenciaTipoDt.VERIFICAR_AUDIENCIAS_REALIZADAS_CEJUSC) 
			retorno = true;

		return retorno;
	}

	/**
	 * verifica se a pendência é do tipo "expedir para serventia on-line"
	 * 
	 * @param pendenciaDt
	 * @param usuarioDt
	 * @return verdadeiro ou falso
	 */
	protected boolean pendenciaTipoExpedir(PendenciaDt pendenciaDt) {
		boolean retorno = false;
		Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
		Integer statusPendencia = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
		if (statusPendencia != PendenciaStatusDt.ID_AGUARDANDO_RETORNO && statusPendencia != PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO) {
			if (tipoPendencia != PendenciaTipoDt.INTIMACAO && tipoPendencia != PendenciaTipoDt.CARTA_CITACAO 
					&& tipoPendencia != PendenciaTipoDt.INTIMACAO_AUDIENCIA && tipoPendencia != PendenciaTipoDt.CARTA_CITACAO_AUDIENCIA 
					&& tipoPendencia != PendenciaTipoDt.CARTA_PRECATORIA && pendenciaDt.isPendenciaDeProcesso() && tipoPendencia != PendenciaTipoDt.REQUISICAO_PEQUENO_VALOR
					&& tipoPendencia != PendenciaTipoDt.ALVARA && tipoPendencia != PendenciaTipoDt.ALVARA_SOLTURA){
				retorno = true;
			}
		}
		return retorno;
	}
	
	/**
	 * verifica se a pendência é do tipo Alvara
	 * 
	 * @param pendenciaDt
	 * @param usuarioDt
	 * @return verdadeiro ou falso
	 */
	protected boolean pendenciaTipoAlvara(PendenciaDt pendenciaDt) {
		boolean retorno = false;
		Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
		Integer statusPendencia = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
		if (statusPendencia != PendenciaStatusDt.ID_AGUARDANDO_RETORNO && statusPendencia != PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO) {
			if ((tipoPendencia == PendenciaTipoDt.ALVARA || tipoPendencia == PendenciaTipoDt.ALVARA_SOLTURA) && pendenciaDt.isPendenciaDeProcesso()) {
				retorno = true;			
			}
		}
		return retorno;
	}

	/**
	 * verifica se a pendência é do tipo "distribuir para para serventia cargo"
	 * 
	 * @param pendenciaDt
	 * @param usuarioDt
	 * @return verdadeiro ou falso
	 */
	protected boolean pendenciaTipoDistribuir(PendenciaDt pendenciaDt) {
		boolean retorno = false;
		Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
		Integer statusPendencia = Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo());
		if (statusPendencia != PendenciaStatusDt.ID_AGUARDANDO_RETORNO && statusPendencia != PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO) {
			if (tipoPendencia == PendenciaTipoDt.CARTA_PRECATORIA && pendenciaDt.isPendenciaDeProcesso()) retorno = true;
		}
		return retorno;
	}

	/**
	 * verifica se é uma pendencia carta precatoria para responder para controle
	 * de tela (tela personalizada)
	 * 
	 * @param pendenciaDt
	 * @return verdadeiro ou falso
	 */
	protected boolean pendenciaCartaPrecatoriaSolucionar(PendenciaDt pendenciaDt) {
		boolean retorno = false;
		Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
		if (tipoPendencia == PendenciaTipoDt.CARTA_PRECATORIA && (Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO || Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_CORRECAO)) retorno = true;

		return retorno;
	}

	/**
	 * verifica se é uma pendencia para responder com acesso externo a processo
	 * de outra serventia para controle de tela
	 * 
	 * @param pendenciaDt
	 * @return verdadeiro ou falso
	 */
	protected boolean pendenciaAcessoExternoSolucionar(PendenciaDt pendenciaDt) {
		boolean retorno = false;
		Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
		if ( (tipoPendencia == PendenciaTipoDt.PEDIDO_NATJUS_SEGUNDO_GRAU || tipoPendencia == PendenciaTipoDt.PEDIDO_CONTADORIA || tipoPendencia == PendenciaTipoDt.PEDIDO_LAUDO ||
				tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_LIQUIDACAO || tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_CUSTAS || tipoPendencia == PendenciaTipoDt.CONTADORIA_JUNTADA_DOCUMENTO ||
				tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_CONTA || tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_TRIBUTOS || tipoPendencia == PendenciaTipoDt.PEDIDO_CONTADORIA_CRIMINAL || 
				tipoPendencia == PendenciaTipoDt.AGENDAMENTO_PERICIA || tipoPendencia == PendenciaTipoDt.PEDIDO_CENOPES || tipoPendencia == PendenciaTipoDt.PEDIDO_JUSTICA_RESTAURATIVA 
			 ) 
			 && 
			 (Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO ||Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_CORRECAO)
		 ){ 
			retorno = true;
		}
		return retorno;
	}

	/**
	 * verifica se uma pendência permite o acesso a um processo externo a
	 * serventia
	 * 
	 * @param pendenciaDt
	 * @return verdadeiro ou falso
	 */
	protected boolean acessoExterno(PendenciaDt pendenciaDt) {
		boolean retorno = false;
		Integer tipoPendencia = pendenciaDt.getPendenciaTipoCodigoToInt();
		if ( (tipoPendencia == PendenciaTipoDt.CARTA_PRECATORIA || tipoPendencia == PendenciaTipoDt.PEDIDO_NATJUS_SEGUNDO_GRAU || tipoPendencia == PendenciaTipoDt.PEDIDO_CONTADORIA ||
				tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_LIQUIDACAO || tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_CUSTAS || tipoPendencia == PendenciaTipoDt.CONTADORIA_JUNTADA_DOCUMENTO ||
				tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_CONTA || tipoPendencia == PendenciaTipoDt.CONTADORIA_CALCULO_TRIBUTOS || tipoPendencia == PendenciaTipoDt.PEDIDO_CONTADORIA_CRIMINAL ||
				tipoPendencia == PendenciaTipoDt.PEDIDO_CENOPES || tipoPendencia == PendenciaTipoDt.PEDIDO_LAUDO || tipoPendencia == PendenciaTipoDt.PEDIDO_JUSTICA_RESTAURATIVA || 
				tipoPendencia == PendenciaTipoDt.AGENDAMENTO_PERICIA
			 )				
			 && (Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO || Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_CORRECAO)) {
			retorno = true;
		} else if (tipoPendencia == PendenciaTipoDt.LIBERACAO_ACESSO) {
			retorno = true;
		}
		return retorno;
	}

	/**
	 * verifica se uma pendencia vai ser respondida ou expedida para controle de
	 * tela
	 * 
	 * @param pendenciaDt
	 * @return verdadeiro ou falso
	 */
	protected boolean pendenciaProcessoResponder(PendenciaDt pendenciaDt) {
		boolean retorno = false;
		if (Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_AGUARDANDO_CUMPRIMENTO || Funcoes.StringToInt(pendenciaDt.getPendenciaStatusCodigo()) == PendenciaStatusDt.ID_CORRECAO) {// NAO
																																																					// EXPEDE
			retorno = true;
		}
		return retorno;
	}

	/**
	 * Controla o fluxo para as telas de pendências
	 * 
	 * @param request
	 * @param pendenciaDt
	 * @param pendenciaNe
	 * @param usuarioNe
	 * @param pgAtual
	 * @return String, a tela necessária para resolver a pendência
	 * @throws Exception
	 */
	protected String telasPendencia(HttpServletRequest request, PendenciaDt pendenciaDt, PendenciaNe pendenciaNe, UsuarioNe usuarioNe) throws Exception{
		String stAcao = "";
		// Verifica se pode finalizar
		if (pendenciaNe.podeResponder(pendenciaDt, usuarioNe)) 
			request.setAttribute("habilitarResponder", true);// inserção com assinador e sem assinador
		
		request.setAttribute("habilitarGuardarParaAssinar", pendenciaNe.podeGuardarParaAssinar(pendenciaDt, usuarioNe));
		
		if (usuarioNe.getUsuarioDt().isTurmaJulgadora() ){
			if(usuarioNe.getUsuarioDt().isMagistrado() && pendenciaNe.isResponsavelProcesso(usuarioNe.getUsuarioDt().getId_ServentiaCargo(), pendenciaDt.getId_Processo())){
				request.setAttribute("finalizarElaboracaoVoto", true); // se for da turma julgadora é possível que o usuário cadastrador da pendência elaboração de voto seja o assessor ao realizar pré-análise na sessão
			}
		}else{
			if (pendenciaDt.getId_UsuarioCadastrador().equalsIgnoreCase(usuarioNe.getUsuarioDt().getId_UsuarioServentia())){
				request.setAttribute("finalizarElaboracaoVoto", true); // só o magistrado que criou a pendência elaboração de voto poderá fecha-lá.
			}			
		}
		
		// verifica se pode encaminhar
		if (!usuarioNe.getUsuarioDt().getServentiaTipoExterna().equals("true") && (usuarioNe.getUsuarioDt().getGrupoUsuarioChefe() == null || usuarioNe.getUsuarioDt().getGrupoUsuarioChefe().length() == 0 || usuarioNe.isMagistrado()
				|| Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoUsuarioChefe()) == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL || Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoUsuarioChefe()) == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL)) 
			request.setAttribute("habilitarEncaminhar", true);// encaminhar
		
		// verifica se pode trocar o tipo de uma pendência
		if ((usuarioNe.isServentiaUpjFamilia() || usuarioNe.isServentiaUpjSucessoes() || usuarioNe.isServentiaUpjCriminal() || usuarioNe.isContador() ) && pendenciaDt.isTrocarTipo()){
			request.setAttribute("habilitarTrocarTipoPendencia", true);
		}
																																																																																								// pendencia

		if ((!usuarioNe.getUsuarioDt().getGrupoUsuarioChefe().equals("") && 
				Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoUsuarioChefe()) == GrupoDt.JUIZES_VARA ||
				Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoUsuarioChefe()) == GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU || 
				Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoUsuarioChefe()) == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL || 
				Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoUsuarioChefe()) == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL))
			request.setAttribute("habilitarPreAnalise", true);// habilitar pre-analise(precatoria)
		
		// verifica se pode acompanhar
		// if (pendenciaNe.podeAcompanhar(pendenciaDt, usuarioNe.getUsuarioDt()))
		request.setAttribute("habilitarDetalhes", true);// ver detalhes da pendencia

		if (usuarioNe.getUsuarioDt().getGrupoCodigo() != null && usuarioNe.getUsuarioDt().isGrupoCodigoDeAutoridade()) 
			request.setAttribute("habilitarExpedir", true);
		else
			request.setAttribute("habilitarExpedir", false);
		// TELAS PARA RESOLVER PENDÊNCIAS
		if (pendenciaDt.isPendenciaDeProcesso() && !pendenciaDt.estaRespondida()) {

			if (pendenciaDt.getSomenteMarcarLeitura()) request.setAttribute("AguardandoRetorno", true);// mostrar lista de status pendencia aguardando retorno

			if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MARCAR_AUDIENCIA || 
				pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC || 
			    pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MARCAR_AUDIENCIA_CONCILIACAO_CEJUSC_DPVAT ||
				pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.MARCAR_AUDIENCIA_MEDIACAO_CEJUSC) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoTipoVerificarAudiencia.jsp";
			else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.SOLICITACAO_CARGA) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoTipoSolicitacaoCarga.jsp";
			else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.AVERBACAO_CUSTAS) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoTipoAverbacaoCustas.jsp";
			else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.OFICIO_COMUNICATORIO) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoTipoVerificarOficioComunicatorio.jsp";
			else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_DISTRIBUICAO || pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_REDISTRIBUICAO) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoTipoVerificarRedistribuicao.jsp";
			else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.ELABORACAO_VOTO) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaElaboracaoVoto.jsp";
			else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.VERIFICAR_IMPEDIMENTO_VOTANTES) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaVerificarVotantes.jsp";
			else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.PEDIDO_SUSTENTACAO_ORAL_DEFERIDO) 
				stAcao = "/WEB-INF/jsp/RemarcarAudienciaProcessoVirtual.jsp";
			else if (pendenciaTipoVerificar(pendenciaDt)) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoTipoVerificar.jsp";
			else if (pendenciaCartaPrecatoriaSolucionar(pendenciaDt))				
				stAcao = "/WEB-INF/jsptjgo/PendenciaCartaPrecatoria.jsp";
			else if (pendenciaAcessoExternoSolucionar(pendenciaDt)) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaAcessoExterno.jsp";
			else if (pendenciaProcessoResponder(pendenciaDt)) // NAO EXPEDE
				stAcao = "/WEB-INF/jsptjgo/PendenciaPegar.jsp";
			else if (pendenciaTipoExpedir(pendenciaDt)) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoTipoExpedir.jsp";
			else if (pendenciaTipoAlvara(pendenciaDt))
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoTipoAlvara.jsp";
			else if (pendenciaTipoDistribuir(pendenciaDt)) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoTipoDistribuir.jsp";
			else if (pendenciaDt.getPendenciaTipoCodigoToInt() == PendenciaTipoDt.INTIMACAO_VIA_TELEFONE) 
				stAcao = "/WEB-INF/jsptjgo/PendenciaPegar.jsp";
			else
				stAcao = "/WEB-INF/jsptjgo/PendenciaProcessoPegar.jsp";

		} else if (!pendenciaDt.estaRespondida()) {
			stAcao = "/WEB-INF/jsptjgo/PendenciaPegar.jsp";

		} else {
			stAcao = "/WEB-INF/jsptjgo/PendenciaVisualizar.jsp";
			this.visualizarPendencia(request, pendenciaNe, pendenciaDt, usuarioNe);
		}
		return stAcao;
	}

	/**
	 * Listar as pendencias intimacões lidas para distribuição
	 * 
	 * @author Márcio Mendonça Gomes
	 * @since 30/08/2012
	 * @param request
	 *            objeto de request
	 * @param pendenciaNe
	 *            regras de negocio
	 * @param usuarioNe
	 *            objeto de negocio do usuario, usuario da sessao
	 * @param posicaoPaginaAtual
	 *            paginacao
	 * @throws Exception
	 */
	protected void listarIntimacoesLidasDistribuicao(HttpServletRequest request, PendenciaNe pendenciaNe, UsuarioNe usuarioNe, String posicaoPaginaAtual) throws Exception{

		// Procura pendencias
		String numero_processo = null;

		if (request.getParameter("nomeBusca") != null && request.getParameter("nomeBusca").trim().equals("") == false) {
			numero_processo = request.getParameter("nomeBusca");
		}

		List pendencias = pendenciaNe.consultarIntimacoesLidasDistribuicao(usuarioNe, numero_processo, null, null, posicaoPaginaAtual);

		request.setAttribute("ListaPendencia", pendencias);
		request.setAttribute("QuantidadePaginas", pendenciaNe.getQuantidadePaginas());
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(posicaoPaginaAtual));
	}
	
	/**
	 * Salva Resolucao da pendencia
	 * 
	 * @author Márcio Mendonça Gomes
	 * @since 16/10/2013
	 * 
	 * @param HttpServletRequest
	 *            request
	 * @param PendenciaNe
	 *            pendenciaNe, objeto de negocio da pendencia
	 * @param PendenciaDt
	 *            pendenciaDt, pojo de pendencia
	 * @param ArquivoTipoDt
	 *            arquivoTipo, tipo de arquivo
	 * @param UsuarioNe
	 *            usuarioNe, objeto de negocio da pendencia
	 * @return boolean
	 * @throws Exception 
	 */
	protected boolean salvaGuardarParaAssinar(HttpServletRequest request, PendenciaNe pendenciaNe, PendenciaDt pendenciaDt, UsuarioNe usuarioNe, List arquivos, ComarcaDt comarcaDt, ServentiaDt serventiaExpedirDt, ServentiaTipoDt serventiaTipoExpedirDt) throws Exception{
		if (arquivos != null && arquivos.size() > 0) {
			for (int i = 0; i < arquivos.size(); i++) {
				ArquivoDt arquivoDt = (ArquivoDt)arquivos.get(i);
				if(arquivoDt.getUsuarioAssinador() != null && arquivoDt.getUsuarioAssinador().length() > 0) {
					request.setAttribute("MensagemErro", "Não é possível guardar para assinar uma pendência com arquivo assinado vinculado a ela.");
					return false;
				} 	
			}
		}
		if (usuarioNe.getUsuarioDt().getId_Serventia().equalsIgnoreCase(serventiaExpedirDt.getId())) {           
            request.setAttribute("MensagemErro", "Pendência não pode ser expedida para serventia do usuário logado");
			return false;
        } else if (pendenciaDt.getId_Processo() == null || pendenciaDt.getId_Processo().trim().length() == 0){
			request.setAttribute("MensagemErro", "Não é possível guardar para assinar uma pendência não vinculada a um processo");
			return false;
		} else if (arquivos == null || arquivos.size() == 0) {
			request.setAttribute("MensagemErro", "Lista de arquivos vazia");
			return false;
		} else if (arquivos.size() > 1) {
			request.setAttribute("MensagemErro", "Deve existir apenas um arquivo na lista");
			return false;
		} else {
			pendenciaNe.salvaGuardarParaAssinar(pendenciaDt, arquivos, usuarioNe.getUsuarioDt(), comarcaDt, serventiaExpedirDt, serventiaTipoExpedirDt);			
			limparArquivosSessao(request);
			return true;
		}
	}
}