package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.ModeloDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoTipoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoEncaminhamentoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioMovimentacaoTipoDt;
import br.gov.go.tj.projudi.ne.MovimentacaoNe;
import br.gov.go.tj.projudi.ne.ProcessoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

/**
 * Servlet que controla a movimenta��o gen�rica de processos. Essa movimenta��o pode ser feita pelo
 * cart�rio ou pelo juiz a qualquer momento no processo. Suporta a movimenta��o em lote. Utilizada
 * por usu�rios internos quando desejam inserir um documento no processo ou gerar pend�ncia(s).
 * 
 * @author msapaula
 * 
 */
public class MovimentacaoCt extends MovimentacaoCtGen {

	private static final long serialVersionUID = -5370072411115726032L;

	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws MensagemException, Exception, ServletException, IOException {

		MovimentacaoProcessoDt Movimentacaodt;
		ProcessoEncaminhamentoDt processoEncaminhamentoDt;
		MovimentacaoNe Movimentacaone;

		ProcessoDt processoDt = null;
	
		String stNomeBusca1 = "";		

		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");		
				
		String Mensagem = "";
		String stId = "";
		int paginaAnterior = 0;
		int passoEditar = -1;
		String processos[] = null;
		String stAcao = "/WEB-INF/jsptjgo/MovimentacaoProcesso.jsp";

		request.setAttribute("tempPrograma", "Movimentacao");
		request.setAttribute("tempRetorno", "Movimentacao");
		request.setAttribute("TituloPagina", "Movimentar Processo");
		
		Movimentacaone = (MovimentacaoNe) request.getSession().getAttribute("Movimentacaone");
		if (Movimentacaone == null) Movimentacaone = new MovimentacaoNe();

		Movimentacaodt = (MovimentacaoProcessoDt) request.getSession().getAttribute("Movimentacaodt");
		if (Movimentacaodt == null) Movimentacaodt = new MovimentacaoProcessoDt();
	
		processoDt = (ProcessoDt) request.getSession().getAttribute("processoDt");
		
		
		//----------------------------------------------------------------------------------------------------------------------
		processoEncaminhamentoDt = (ProcessoEncaminhamentoDt) request.getSession().getAttribute("processoEncaminhamentoDt");
		if (processoEncaminhamentoDt == null) processoEncaminhamentoDt = new ProcessoEncaminhamentoDt();
		
		
		if(request.getParameter("Id_Serventia") != null && !request.getParameter("Id_Serventia").equals(""))  
			processoEncaminhamentoDt.setIdServentia(request.getParameter("Id_Serventia"));
		
		if(request.getParameter("Serventia") != null && !request.getParameter("Serventia").equals("")) 
			processoEncaminhamentoDt.setServentia(request.getParameter("Serventia"));
		
		if(request.getParameter("Id_ServentiaCargo") != null && !request.getParameter("Id_ServentiaCargo").equals(""))  
			processoEncaminhamentoDt.setIdServentiaCargo(request.getParameter("Id_ServentiaCargo"));
		
		if(request.getParameter("ServentiaCargo") != null && !request.getParameter("ServentiaCargo").equals("")) 
			processoEncaminhamentoDt.setServentiaCargo(request.getParameter("ServentiaCargo"));
		
		if(request.getParameter("Id_AreaDistribuicao") != null && !request.getParameter("Id_AreaDistribuicao").equals("")){ 
			processoEncaminhamentoDt.setIdAreaDistribuicao(request.getParameter("Id_AreaDistribuicao"));
			processoEncaminhamentoDt.setIdServentia("");
			processoEncaminhamentoDt.setServentia("");
		}
		
		if(request.getParameter("AreaDistribuicao") != null && !request.getParameter("AreaDistribuicao").equals("")) 
			processoEncaminhamentoDt.setAreaDistribuicao(request.getParameter("AreaDistribuicao"));
		//------------------------------------------------------------------------------------------------------------------------

		// Vari�veis auxiliares
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		if (request.getParameter("tempFluxo1") != null && !request.getParameter("tempFluxo1").equalsIgnoreCase("null")) passoEditar = Funcoes.StringToInt(request.getParameter("tempFluxo1"));
		if (request.getParameter("PaginaAnterior") != null) paginaAnterior = Funcoes.StringToInt(request.getParameter("PaginaAnterior"));
		
		if (request.getParameter("nomeArquivo")!= null )
			request.setAttribute("nomeArquivo", request.getParameter("nomeArquivo"));
		else
			request.setAttribute("nomeArquivo", "");
			
		
		Movimentacaodt.setId_MovimentacaoTipo(request.getParameter("Id_MovimentacaoTipo"));
		Movimentacaodt.setMovimentacaoTipo(request.getParameter("MovimentacaoTipo"));
		Movimentacaodt.setId_UsuarioRealizador(UsuarioSessao.getUsuarioDt().getId_UsuarioServentia());
		Movimentacaodt.setClassificador(request.getParameter("Classificador"));
		Movimentacaodt.setId_Classificador(request.getParameter("Id_Classificador"));
		Movimentacaodt.setClassificadorPendencia(request.getParameter("ClassificadorPendencia"));
		Movimentacaodt.setId_ClassificadorPendencia(request.getParameter("Id_ClassificadorPendencia"));
		Movimentacaodt.setId_ArquivoTipo(request.getParameter("Id_ArquivoTipo"));
		Movimentacaodt.setArquivoTipo(request.getParameter("ArquivoTipo"));
		Movimentacaodt.setId_Modelo(request.getParameter("Id_Modelo"));
		Movimentacaodt.setModelo(request.getParameter("Modelo"));
		Movimentacaodt.setTextoEditor(request.getParameter("TextoEditor"));
		Movimentacaodt.setComplemento(request.getParameter("MovimentacaoComplemento"));
		Movimentacaodt.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
		Movimentacaodt.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());

		setParametrosAuxiliares(Movimentacaodt, paginaAnterior, paginaatual, request, Movimentacaone, UsuarioSessao);

		// -----------------------------------------------------------------------------------------------------------------------//
		switch (paginaatual) {
			
			case Configuracao.Localizar:
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Movimentacao"};
					String[] lisDescricao = {"Movimentacao"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Movimentacao");
					request.setAttribute("tempBuscaDescricao","Movimentacao");
					request.setAttribute("tempBuscaPrograma","Movimentacao");
					request.setAttribute("tempRetorno","Movimentacao");
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String id_processo = request.getParameter("filtroTabela");
					String stTemp = Movimentacaone.consultarMovimentacaoProcessoJSON(stNomeBusca1, id_processo, PosicaoPaginaAtual);
					try {
						response.setContentType("text/x-json");
						response.getOutputStream().write(stTemp.getBytes());
						response.flushBuffer();
					}catch(Exception e) { }
					return;
				}
				break;

			// Inicializa movimenta��o
			case Configuracao.Novo:
				//Movimentacaodt = new MovimentacaoProcessoDt();
				
				if (request.getParameter("RedirecionaOutraServentia") != null){
					Movimentacaodt.setRedirecionaOutraServentia(request.getParameter("RedirecionaOutraServentia"));
					
					if ((Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("1"))
							|| (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("6"))){
						request.setAttribute("MensagemOk", "ATEN��O - Para trocar um respons�vel no processo, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar a troca.");						
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.TROCAR_RESPONSAVEL_PROCESSO);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setOcultaTiposMovimentacao(true);
					
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("15")) {
						request.setAttribute("MensagemOk", "ATEN��O - Para Realizar a Carga do processo, por favor fa�a uma certid�o com as Informa��es necess�rias " +
						" e clique em \"Avan�ar\" para efetuar a carga.");
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.ENTREGA_EM_CARGA_VISTA);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setOcultaTiposMovimentacao(true);
					
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("2")) {
						request.setAttribute("MensagemOk", "ATEN��O - Para redistribuir um processo, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar a redistribui��o.");
					
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("5")) {
						request.setAttribute("MensagemOk", "ATEN��O - Para redistribuir um processo, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar a redistribui��o.");
						
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("3")) {
						request.setAttribute("MensagemOk", "ATEN��O - Para redistribuir um processo na c�mara, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar a redistribui��o.");
					
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("4")){
						request.setAttribute("MensagemOk", "ATEN��O - Para trocar o Redator do processo, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar a troca.");						
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.TROCAR_REDATOR_PROCESSO);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setOcultaTiposMovimentacao(true);
					
					}  else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("7")){
						request.setAttribute("MensagemOk", "ATEN��O - Para inverter os P�los do processo/recurso, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar a invers�o.");						
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.INVERTER_POLOS);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setOcultaTiposMovimentacao(true);
					
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("8")){
						request.setAttribute("MensagemOk", "ATEN��O - Para enviar o processo para outra serventia, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar o envio.");						
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.CERTIDAO_ENCAMINHAMENTO_PROCESSO);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.CERTIDAO_ENCAMINHAMENTO_PROCESSO));
						Movimentacaodt.setOcultaTiposMovimentacao(true);					
					
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("12")){
						request.setAttribute("MensagemOk", "ATEN��O - Para Retirar Ac�rd�o/Ementa e Extrato da Ata de Julgamento, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar a retirada.");						
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.SESSAO_RETIRAR_ACORDAO_EMENTA_EXTRATO_ATA);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.SESSAO_RETIRAR_ACORDAO_EMENTA_EXTRATO_ATA));
						Movimentacaodt.setOcultaTiposMovimentacao(true);	
						Movimentacaodt.setIdRedirecionaOutraServentia(request.getParameter("Id_AudienciaProcesso"));
						processoDt = null;
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("13")){
						request.setAttribute("MensagemOk", "ATEN��O - Para Retirar Extrato da Ata de Julgamento, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar a retirada.");						
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.SESSAO_RETIRAR_EXTRATO_ATA);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.SESSAO_RETIRAR_EXTRATO_ATA));
						Movimentacaodt.setOcultaTiposMovimentacao(true);	
						Movimentacaodt.setIdRedirecionaOutraServentia(request.getParameter("Id_AudienciaProcesso"));
						processoDt = null;
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("14")){
						request.setAttribute("MensagemOk", "ATEN��O - Para Retornar o processo para a Sess�o de Julgamento, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para efetuar o retorno.");						
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.SESSAO_RETORNAR_PROCESSO_SESSAO_JULGAMENTO);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setMovimentacaoTipoCodigo(String.valueOf(MovimentacaoTipoDt.SESSAO_RETORNAR_PROCESSO_SESSAO_JULGAMENTO));
						Movimentacaodt.setOcultaTiposMovimentacao(true);	
						Movimentacaodt.setIdRedirecionaOutraServentia(request.getParameter("Id_AudienciaProcesso"));
						processoDt = null;
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("16")) {
						request.setAttribute("MensagemOk", "ATEN��O - Para Refazer a Juntada de Documento do Processo F�sico" +
						", por favor fa�a uma certid�o com as Informa��es necess�rias e clique em \"Avan�ar\" para efetuar a carga.");
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setComplemento(MovimentacaoTipoDt.COMPLEMENTO_JUNTADA_DOCUMENTO_PROCESSO_FISICO);
						Movimentacaodt.setOcultaTiposMovimentacao(true);	
						Movimentacaodt.setOcultaComplemento(true);
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("17")) {
						
						if (processoDt == null) throw new MensagemException("Processo n�o localizado, favor efetuar a consulta novamente.");
						if (!processoDt.isProcessoHibrido()) throw new MensagemException("Somente processos h�bridos poder�o ser convertidos em digital.");
						
						request.setAttribute("MensagemOk", "ATEN��O - Para Converter o Processo H�brido em Digital" +
						", fa�a o upload das pe�as assinadas do processo f�sico ou abra o assinador e selecione as pe�as para realizar a assinatura.");
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setComplemento(MovimentacaoTipoDt.COMPLEMENTO_CONVERTER_HIBRIDO_DIGITAL);
						Movimentacaodt.setOcultaTiposMovimentacao(true);	
						Movimentacaodt.setOcultaComplemento(true);
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("18")) {
						
						if (processoDt == null) throw new MensagemException("Processo n�o localizado, favor efetuar a consulta novamente.");
						if (!processoDt.isProcessoJaFoiHibrido()) throw new MensagemException("Somente processos digitais que j� foram h�bridos poder�o ser retornados para hibridos.");
						
						redireciona(response, "MovimentacaoArquivo?PaginaAtual=-2&PassoEditar=2&ProximaPaginaAtual=3");
						
						/*request.setAttribute("MensagemOk", "ATEN��O - Para Retornar o Processo Digital para H�brido, por favor fa�a uma certid�o justificando o motivo" +
						" e clique em \"Avan�ar\" para retornar o processo.");						
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setComplemento(MovimentacaoTipoDt.COMPLEMENTO_RETORNAR_PARA_HIBRIDO);
						Movimentacaodt.setOcultaTiposMovimentacao(true);	
						Movimentacaodt.setOcultaComplemento(true);*/
					} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("19")) {
						
						if (processoDt == null) throw new MensagemException("Processo n�o localizado, favor efetuar a consulta novamente.");
						if (!processoDt.isProcessoJaFoiHibrido()) throw new MensagemException("Somente processos digitais que j� foram h�bridos poder�o adicionar novas pe�as por esta op��o.");
						
						request.setAttribute("MensagemOk", "ATEN��O - Para Adicionar mais pe�as do Processo que j� foi H�brido" +
								", fa�a o upload das pe�as assinadas do processo f�sico ou abra o assinador e selecione as pe�as para realizar a assinatura.");
						MovimentacaoTipoDt movimentacaoTipoDt = Movimentacaone.consultaMovimentacaoTipoCodigo(MovimentacaoTipoDt.JUNTADA_DE_DOCUMENTO);
						Movimentacaodt.setId_MovimentacaoTipo(movimentacaoTipoDt.getId());
						Movimentacaodt.setMovimentacaoTipo(movimentacaoTipoDt.getMovimentacaoTipo());
						Movimentacaodt.setComplemento(MovimentacaoTipoDt.COMPLEMENTO_ADICIONAR_PECAS_HIBRIDO_DIGITAL);
						Movimentacaodt.setOcultaTiposMovimentacao(true);	
						Movimentacaodt.setOcultaComplemento(true);
					}			
				}
				
				
				if (request.getParameter("TodosProcessosClassificados") != null){	
					String Id_ServentiaCargo = request.getParameter("Id_ServentiaCargo");
					processos = Movimentacaone.consultarTodosProcessosClassificadoServentia(Movimentacaodt.getId_Classificador(),  UsuarioSessao.getUsuarioDt().getId_Serventia(), Id_ServentiaCargo);
					request.getSession().setAttribute("TodosProcessosClassificados", 1);
					request.getSession().setAttribute("Classificado", Movimentacaodt.getClassificador());
					request.getSession().setAttribute("processos", processos);
					// Captura os processos que ser�o movimentados, se for o caso de Movimenta��o em Lote
				}else if (request.getParameter("processos") != null){
					processos = request.getParameterValues("processos");
					request.getSession().setAttribute("processos", processos);
					
					//Processos das serventias de execu��o da penal podem ser movimentados apenas para arquivamento. BO: 2019/13635 - 2019/14873
					for (int i = 0; i < processos.length; i++) {
						ProcessoDt procTempDt = new ProcessoNe().consultarId(processos[i]);
						//Se o processo for de uma classe de execu��o penal e for de assunto diferente de Acordo de n�o persecu��o penal
						if(procTempDt.isExecucaoPenal() && !procTempDt.isAcordoNaoPersecucaoPenal()){
							ServentiaDt serventiaDt = Movimentacaone.consultarServentiaProcesso(procTempDt.getId_Serventia());
							//e for de uma serventia do subtipo execu��o penal
							if(serventiaDt != null && serventiaDt.isSubTipoExecucaoPenal()) {
								request.setAttribute("MensagemOk", "Este processo � de execu��o da penal e s� ser� permitido movimentar ele caso seja para arquivar. Qualquer outro tipo de movimenta��o ser� bloqueado ao final. Decreto judici�rio n.� 2029/2019.");
							}
						}
					}
				}else if (request.getSession().getAttribute("processos") != null){ 
					processos = (String[]) request.getSession().getAttribute("processos");
					
					//Processos das serventias de execu��o da penal podem ser movimentados apenas para arquivamento. BO: 2019/13635 - 2019/14873
					for (int i = 0; i < processos.length; i++) {
						ProcessoDt procTempDt = new ProcessoNe().consultarId(processos[i]);
						//Se o processo for de uma classe de execu��o penal e for de assunto diferente de Acordo de n�o persecu��o penal
						if(procTempDt.isExecucaoPenal() && !procTempDt.isAcordoNaoPersecucaoPenal()){
							ServentiaDt serventiaDt = Movimentacaone.consultarServentiaProcesso(procTempDt.getId_Serventia());
							//e for de uma serventia do subtipo execu��o penal
							if(serventiaDt != null && serventiaDt.isSubTipoExecucaoPenal()) {
								request.setAttribute("MensagemOk", "Este processo � de execu��o da penal e s� ser� permitido movimentar ele caso seja para arquivar. Qualquer outro tipo de movimenta��o ser� bloqueado ao final. Decreto judici�rio n.� 2029/2019.");
							}
						}
					}
				}else {
					// Recupera o processo da sess�o e adiciona ao vetor
					// o processodt ja foi recuperado na parte superior
					//Processodt = (ProcessoDt) request.getSession().getAttribute("processoDt");
					// Se processo n�o est� na sess�o, tenta capturar id do processo

					if (processoDt == null || processoDt.getId().length() == 0) {
						if (request.getParameter("Id_Processo") != null && !request.getParameter("Id_Processo").equals("")) processoDt = Movimentacaone.consultarProcessoIdCompleto(request.getParameter("Id_Processo"));

					}
					
					//Processos das serventias de execu��o da penal podem ser movimentados apenas para arquivamento. BO: 2019/13635 - 2019/14873
					//Se o processo for de uma classe de execu��o penal e for de assunto diferente de Acordo de n�o persecu��o penal
					if(processoDt.isExecucaoPenal() && !processoDt.isAcordoNaoPersecucaoPenal()){
						ServentiaDt serventiaDt = Movimentacaone.consultarServentiaProcesso(processoDt.getId_Serventia());
						//e for de uma serventia do subtipo execu��o penal
						if(serventiaDt != null && serventiaDt.isSubTipoExecucaoPenal()) {
							request.setAttribute("MensagemOk", "Este processo � de execu��o da penal e s� ser� permitido movimentar ele caso seja para arquivar. Qualquer outro tipo de movimenta��o ser� bloqueado ao final. Decreto judici�rio n.� 2029/2019.");
						}
					}
					
					// Adiciona processo �nico ao vetor para montar tela corretamente

					if (processoDt != null) processos = new String[] {processoDt.getId() };
				}

				if (processos != null && processos.length > 0) {
					
					//Tratamento para movimentacao de devolucao de carta precat�ria
					if (request.getSession().getAttribute("ProcessoPrecatoria") != null
							&& (request.getSession().getAttribute("ProcessoPrecatoria").toString().equalsIgnoreCase("DevolverProcessoPrecatoria")
							|| request.getSession().getAttribute("ProcessoPrecatoria").toString().equalsIgnoreCase("DevolverDocumentoPrecatoria")
							|| request.getSession().getAttribute("ProcessoPrecatoria").toString().equalsIgnoreCase("DevolverDocumentoOficio"))){
						
						Movimentacaodt.setDevolucaoPrecatoria(request.getSession().getAttribute("ProcessoPrecatoria").toString());
					    Movimentacaodt.setComplementoDevolucaoPrecatoria(request.getSession().getAttribute("ComplementoDevolucaoPrecatoria").toString());
					    
					    if (request.getSession().getAttribute("ProcessoPrecatoria").toString().equalsIgnoreCase("DevolverDocumentoOficio")){
					    	Movimentacaodt.setId_MovimentacaoTipo("113");
							Movimentacaodt.setMovimentacaoTipo("OF�CIO(S) EXPEDIDO(S)");
					    }
					    
					}
					
					if (request.getSession().getAttribute("AcessoOutraServentia") != null && (Funcoes.StringToInt(request.getSession().getAttribute("AcessoOutraServentia").toString()) == PendenciaTipoDt.CARTA_PRECATORIA)) {
						String processoOutraServentia = null;
						if (request.getSession().getAttribute("ProcessoOutraServentia") != null) processoOutraServentia = (String) request.getSession().getAttribute("ProcessoOutraServentia");
						//Se processo que ser� movimentado � o mesmo com acesso externo liberado, deve liberar

						if (processoOutraServentia != null && processoOutraServentia.equalsIgnoreCase(processoDt.getId())) Movimentacaodt.setAcessoOutraServentia(request.getSession().getAttribute("AcessoOutraServentia").toString());

					}
					
					//Redistribui��o em LOTE
					if (request.getSession().getAttribute("AcessoOutraServentia") != null && (Funcoes.StringToInt(request.getSession().getAttribute("AcessoOutraServentia").toString()) == 1)
							&& Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("5")) {
						Movimentacaodt.setAcessoOutraServentia(request.getSession().getAttribute("AcessoOutraServentia").toString());
					}

					// Verifica se processo(s) pode(m) ser movimentado(s)

					Mensagem = verificaMovimentacao(processos, processoDt, Movimentacaodt, Movimentacaone, UsuarioSessao);


					if (Mensagem.length() == 0) {
						montaTelaMovimentacao(processos, Movimentacaodt, Movimentacaone, UsuarioSessao, request);
					} else {

						if (processoDt != null){
							redireciona(response, "BuscaProcesso?Id_Processo=" + processoDt.getId() + "&MensagemErro=" + Mensagem);
						}else {
							redireciona(response, "BuscaProcesso?PassoBusca=2&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=" + Mensagem);
							request.getSession().removeAttribute("processos");
						}
						return;
					}
				} else {
					// Volta para tela de consulta de processos com mensagem de erro
					redireciona(response, "BuscaProcesso?PassoBusca=1&PaginaAtual=" + Configuracao.Localizar + "&MensagemErro=Nenhum Processo foi selecionado.");
					return;
				}
				break;

			// Salva movimenta��o gen�rica
			case Configuracao.SalvarResultado:
				Mensagem = Movimentacaone.verificarMovimentacaoGenerica(Movimentacaodt);
				if (Mensagem.length() == 0) {
					Mensagem = Movimentacaone.podeMovimentarProcesso(Movimentacaodt, UsuarioSessao.getUsuarioDt().getGrupoCodigo());

					if (Mensagem.length() == 0) {
						Movimentacaone.salvarMovimentacaoGenerica(Movimentacaodt, UsuarioSessao.getUsuarioDt());

						if (Movimentacaodt.isMultiplo()) redireciona(response, "BuscaProcesso?PassoBusca=2&PaginaAtual=" + Configuracao.Localizar + "&MensagemOk=Movimenta��o em Lote efetuada com sucesso.");
						else redireciona(response,"BuscaProcesso?Id_Processo=" + Movimentacaodt.getIdPrimeiroProcessoLista() + "&MensagemOk=Movimenta��o efetuada com sucesso.");

						Movimentacaodt.limpar();
						limparListas(request);
						return;
					} else request.setAttribute("MensagemErro", Mensagem);
				} else request.setAttribute("MensagemErro", Mensagem);
				break;

			case Configuracao.Salvar:
				
				//Valida pedido de arquivamento para apresentar mensagem de advert�ncia
				String mensagemAdvertencia = Movimentacaone.validaPedidoArquivamento(Movimentacaodt, processoDt);
				if( mensagemAdvertencia != null && mensagemAdvertencia.length() > 0 ) {
					request.setAttribute("MensagemAdvertencia", mensagemAdvertencia);
				}
				
				// Captura lista de arquivos e pend�ncias
				Movimentacaodt.setListaArquivos(getListaArquivos(request));
				Movimentacaodt.setListaPendenciasGerar(getListaPendencias(request));
				Movimentacaodt.setPasso2("Passo 2 OK");
				Movimentacaodt.setPasso3("Passo 3");
				stAcao = "/WEB-INF/jsptjgo/MovimentacaoProcessoConfirmacao.jsp";
				break;

			case Configuracao.Excluir:
				if (Movimentacaodt.getListaProcessos() != null) {
					if (Movimentacaodt.getListaProcessos().size() > 1) {
						String posicao = request.getParameter("posicao");
						if (posicao != null && !posicao.equals("")) {
							ProcessoDt objTemp = (ProcessoDt) Movimentacaodt.getListaProcessos().get(Funcoes.StringToInt(posicao));
							Movimentacaodt.getListaProcessos().remove(Funcoes.StringToInt(posicao));
							request.setAttribute("MensagemOk", "Processo " + Funcoes.formataNumeroProcesso(objTemp.getProcessoNumero()) + " retirado dessa movimenta��o m�ltipla.");
						}
					} else if (Movimentacaodt.getListaProcessos().size() == 1) request.setAttribute("MensagemErro", "N�o � poss�vel retirar esse processo da movimenta��o m�ltipla.");
				}
				break;

			// Validar Movimenta��es
			case Configuracao.Curinga6:
				stId = request.getParameter("Id_Movimentacao");
				if (stId != null && stId.length() > 0) {
					MovimentacaoDt movimentacaoSelecionada = (MovimentacaoDt) Movimentacaone.consultarId(stId);
					Mensagem = Movimentacaone.podeMudarStatusMovimentacao(movimentacaoSelecionada, UsuarioSessao.getUsuarioDt());
					if (Mensagem.length() == 0) {
						movimentacaoSelecionada.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
						movimentacaoSelecionada.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
						Movimentacaone.validarMovimentacao(movimentacaoSelecionada);
						Mensagem = "Movimenta��o " + movimentacaoSelecionada.getMovimentacaoTipo() + " foi Validada com Sucesso.";
						redireciona(response, "BuscaProcesso?Id_Processo=" + movimentacaoSelecionada.getId_Processo() + "&MensagemOk=" + Mensagem);
						return;
					}
				} else Mensagem = "N�o foi poss�vel Alterar a visibilidade da Movimenta��o. Selecione novamente.";
				redireciona(response, "BuscaProcesso?Id_Processo=" + Movimentacaodt.getId_Processo() + "&MensagemErro=" + Mensagem);
				return;

			// Invalidar Movimenta��es
			case Configuracao.Curinga7:
				stId = request.getParameter("Id_Movimentacao");
				String tipoBloqueio = request.getParameter("TipoBloqueio");
				
				if (tipoBloqueio != null && tipoBloqueio.length()>0){
				    int constanteAcesso=0;
				    constanteAcesso = new MovimentacaoArquivoDt().getTipoAcesso(tipoBloqueio);				    
				    
					if (stId != null && stId.length() > 0 && constanteAcesso != 0) {
						MovimentacaoDt movimentacaoSelecionada = (MovimentacaoDt) Movimentacaone.consultarId(stId);
						Mensagem = Movimentacaone.podeMudarStatusMovimentacao(movimentacaoSelecionada, UsuarioSessao.getUsuarioDt());
						if (Mensagem.length() == 0) {
							movimentacaoSelecionada.setId_UsuarioLog(UsuarioSessao.getId_Usuario());
							movimentacaoSelecionada.setIpComputadorLog(UsuarioSessao.getUsuarioDt().getIpComputadorLog());
							Movimentacaone.invalidarMovimentacao(movimentacaoSelecionada, constanteAcesso);
							Mensagem = " A visibilidade da Movimenta��o " + movimentacaoSelecionada.getMovimentacaoTipo() + " foi alterada com Sucesso.";
							redireciona(response, "BuscaProcesso?Id_Processo=" + movimentacaoSelecionada.getId_Processo() + "&MensagemOk=" + Mensagem);
							return;
						}
					} else {
						Mensagem = "N�o foi poss�vel Alterar a visibilidade da Movimenta��o. Selecione novamente.";
					}
				} else {
					Mensagem = "N�o foi poss�vel Alterar a visibilidade da Movimenta��o.";
				}
				
				redireciona(response, "BuscaProcesso?Id_Processo=" + Movimentacaodt.getId_Processo() + "&MensagemErro=" + Mensagem);
				return;

			// Consultar Tipos de Movimenta��o
			case (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"MovimentacaoTipo"};
					String[] lisDescricao = {"MovimentacaoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_MovimentacaoTipo");
					request.setAttribute("tempBuscaDescricao","MovimentacaoTipo");
					request.setAttribute("tempBuscaPrograma","MovimentacaoTipo");			
					request.setAttribute("tempRetorno","Movimentacao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PaginaAnterior", String.valueOf(MovimentacaoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Movimentacaone.consultarGrupoMovimentacaoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
				break;

			// Consultar classificadores
			case (ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				String stPermissao = String.valueOf((ClassificadorDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				if (request.getParameter("Passo")==null){
					if (request.getParameter("tempFluxo1").equalsIgnoreCase("processo")) {
						request.setAttribute("tempFluxo1", "processo");
						String[] lisNomeBusca = {"Classificador"};
						String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
						atribuirJSON(request, "Id_Classificador", "Classificador", "Classificador", "Movimentacao", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
						break;
					} else {
						String[] lisNomeBusca = {"Classificador"};
						String[] lisDescricao = {"Classificador", "Prioridade", "Serventia"};
						atribuirJSON(request, "Id_ClassificadorPendencia", "ClassificadorPendencia", "Classificador", "Movimentacao", Configuracao.Editar, stPermissao, lisNomeBusca, lisDescricao);
						break;
					}					
				} else{
					String stTemp="";
					stTemp = Movimentacaone.consultarDescricaoClassificadorJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia());
					enviarJSON(response, stTemp);
					return;								
				}				
				
			// Consultar tipos de Arquivo
			case (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ArquivoTipo"};
					String[] lisDescricao = {"ArquivoTipo"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_ArquivoTipo");
					request.setAttribute("tempBuscaDescricao","ArquivoTipo");
					request.setAttribute("tempBuscaPrograma","ArquivoTipo");			
					request.setAttribute("tempRetorno","Movimentacao");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = Movimentacaone.consultarGrupoArquivoTipoJSON(UsuarioSessao.getUsuarioDt().getGrupoCodigo(), stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
				break;

			// Consultar Modelos do Usu�rio
			case (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Modelo"};
					String[] lisDescricao = {"Modelo","Serventia","Tipo Modelo"};
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_Modelo");
					request.setAttribute("tempBuscaDescricao", "Modelo");
					request.setAttribute("tempBuscaPrograma", "Modelo");
					request.setAttribute("tempRetorno", "Movimentacao");
					request.setAttribute("tempDescricaoId", "Id");
					request.setAttribute("tempDescricaoDescricao", "Modelo");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				}else{
					String stTemp = "";
					stTemp = Movimentacaone.consultarModeloJSON(UsuarioSessao.getUsuarioDt(), Movimentacaodt.getId_ArquivoTipo(), stNomeBusca1,  PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;
				}
				break;
			
			//Consultar �reas de Distribui��o
			case (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"AreaDistribuicao"};
					String[] lisDescricao = {"AreaDistribuicao"};
					//quando for necess�rio retornar outros valos al�m do id, coloque outras colunas de descri��o
					// na localizar.jsp as descri��es geram novos input hidem para retornar ao ct
					// na funcoes.js as descricoes ser�o usadas para gerar os AlterarValue para retornar para o ct
					//String[] camposHidden = {"ForumCodigo","Id_ServentiaSubTipo"};
					//request.setAttribute("camposHidden",camposHidden);
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_AreaDistribuicao");
					request.setAttribute("tempBuscaDescricao","AreaDistribuicao");
					request.setAttribute("tempBuscaPrograma","AreaDistribuicao");			
					request.setAttribute("tempRetorno","Movimentacao?PassoEditar=1");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (AreaDistribuicaoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = new ProcessoNe().consultarDescricaoAreasDistribuicaoAtivaJSON(stNomeBusca1, PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
				break;

			//Consultar Serventias
			case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"Serventia"};
					String[] lisDescricao = {"Serventia"};
					stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId","Id_Serventia");
					request.setAttribute("tempBuscaDescricao","Serventia");
					request.setAttribute("tempBuscaPrograma","Serventia");			
					request.setAttribute("tempRetorno","Movimentacao?PassoEditar=1");		
					request.setAttribute("tempDescricaoId","Id");
					request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
				} else {
					String stTemp="";
					stTemp = new ProcessoNe().consultarServentiasAtivasAreaDistribuicaoJSON(stNomeBusca1, processoEncaminhamentoDt.getIdAreaDistribuicao(), PosicaoPaginaAtual);
					enviarJSON(response, stTemp);
					return;								
				}
				break;
			
			//Consulta de Serventia Cargo
			case (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):							

				if (request.getParameter("Passo")==null){
					String[] lisNomeBusca = {"ServentiaCargo"};
					String[] lisDescricao = {"ServentiaCargo [Serventia]", "Usu�rio", "CargoTipo"};
					String[] camposHidden = {"ServentiaCargoUsuario", "CargoTipo"};
					request.setAttribute("camposHidden",camposHidden);
					stAcao = "/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
					request.setAttribute("tempBuscaId", "Id_ServentiaCargo");
					request.setAttribute("tempBuscaDescricao", "ServentiaCargo");
					request.setAttribute("tempBuscaPrograma", "Movimentacao");
					request.setAttribute("tempRetorno", "Movimentacao");
					//Atributo novoRelatorDesabilitado ser� usado para o controle das transa��es no ct
					if(request.getSession().getAttribute("novoRelatorDesabilitado") == null) {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
					} else {
						request.setAttribute("tempPaginaAtualJSON", Configuracao.Curinga6);
					}
					request.setAttribute("PaginaAtual", (ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
					request.setAttribute("PosicaoPaginaAtual", "0");
					request.setAttribute("QuantidadePaginas", "0");
					request.setAttribute("lisNomeBusca", lisNomeBusca);
					request.setAttribute("lisDescricao", lisDescricao);
					
					request.setAttribute("tempFluxo1", "1");
				}else{
					String stTemp = "";
					stTemp = new ProcessoNe().consultarServentiaCargosJSON(stNomeBusca1, PosicaoPaginaAtual, UsuarioSessao.getId_Serventia(), UsuarioSessao.getUsuarioDt().getServentiaTipoCodigo(), UsuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo());						
					enviarJSON(response, stTemp);
					return;
				}
				break;
				
			default:
				switch (passoEditar) {

					case 0:
						if (Movimentacaodt.getRedirecionaOutraServentia() != null) {
							// Captura lista de arquivo
							Movimentacaodt.setListaArquivos(getListaArquivos(request));
							
							Mensagem = Movimentacaone.verificarMovimentacaoGenerica(Movimentacaodt);
							if (Mensagem.length() == 0) {
								request.getSession().setAttribute("Movimentacaodt", Movimentacaodt);
								
								if ((Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("1"))
										|| (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("6"))){
									redireciona(response, "ProcessoResponsavel?PaginaAtual=4");
								
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("2")) {
									if (Movimentacaodt.getListaProcessos() != null && Movimentacaodt.getListaProcessos().size() == 1) {
										processoDt = (ProcessoDt) Movimentacaodt.getListaProcessos().get(0);
										request.getSession().setAttribute("processoDt", processoDt);
									}
									redireciona(response, "Redistribuicao?PaginaAtual=4");
								
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("15")) {
									if (Movimentacaodt.getListaProcessos() != null && Movimentacaodt.getListaProcessos().size() == 1) {
										processoDt = (ProcessoDt) Movimentacaodt.getListaProcessos().get(0);
										request.getSession().setAttribute("processoDt", processoDt);
									}
									redireciona(response, "SolicitarCargaProcesso?PaginaAtual=7");
								
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("3")) {
									redireciona(response, "Redistribuicao?PaginaAtual=6");
									
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("4")) {
									redireciona(response, "ProcessoRedatorResponsavel?PaginaAtual=4");
									
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("5")) {
									redireciona(response, "RedistribuicaoLote?PaginaAtual=4");
									
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("7")) {
									redireciona(response, "InversaoPolos?PaginaAtual=4");
									
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("8")) {
									redireciona(response, "Redistribuicao?PaginaAtual=7&PassoEditar=1");
									
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("12") ||
										   Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("13") ||
										   Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("14")) {
									redireciona(response, "AudienciaProcessoMovimentacao?PaginaAtual=6");
								
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("15")) {
									redireciona(response, "MovimentacaoArquivo?PaginaAtual=10");
									
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("16")) {
									redireciona(response, "MovimentacaoArquivo?PaginaAtual=9");
									
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("17")) {
									redireciona(response, "MovimentacaoArquivo?PaginaAtual=3&PassoEditar=1");
									
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("18")) {
									redireciona(response, "MovimentacaoArquivo?PaginaAtual=3&PassoEditar=2");
									
								} else if (Movimentacaodt.getRedirecionaOutraServentia().equalsIgnoreCase("19")) {
									redireciona(response, "MovimentacaoArquivo?PaginaAtual=3&PassoEditar=3");
									
								} else {
									//Redireciona para passo 2 - Pend�ncias a Gerar
									Movimentacaodt.setPasso1("Passo 1 OK");
									Movimentacaodt.setPasso2("Passo 2");
									Movimentacaodt.setPasso3("");
									stAcao = "/WEB-INF/jsptjgo/MovimentacaoProcessoPendencias.jsp";
								}
							
							} else request.setAttribute("MensagemErro", Mensagem);
						
						} else {
							//Redireciona para passo 2 - Pend�ncias a Gerar
							Movimentacaodt.setPasso1("Passo 1 OK");
							Movimentacaodt.setPasso2("Passo 2");
							Movimentacaodt.setPasso3("");
							stAcao = "/WEB-INF/jsptjgo/MovimentacaoProcessoPendencias.jsp";
						}
						break;
						
					case 1:											
						// Usu�rio alterou apenas a op��o de sele��o do tipo de movimenta��o
						
						Movimentacaodt.setPasso1("Passo 1 OK");
						Movimentacaodt.setPasso2("Passo 2");
						Movimentacaodt.setPasso3("");
						stAcao = "/WEB-INF/jsptjgo/MovimentacaoProcessoPendencias.jsp";
						
						break;
					default:
						stId = request.getParameter("Id_Movimentacao");
						if (stId != null && !stId.isEmpty()) if (paginaatual==Configuracao.Atualizar ||  !stId.equalsIgnoreCase(Movimentacaodt.getId())) {
							Movimentacaodt.limpar();
							Movimentacaodt = (MovimentacaoProcessoDt) Movimentacaone.consultarId(stId);
						}
						Movimentacaodt.setPasso1("Passo 1");
						Movimentacaodt.setPasso2("");
						Movimentacaodt.setPasso3("");
						break;
				}
		}

		request.setAttribute("PassoEditar", passoEditar);
		request.getSession().setAttribute("Movimentacaodt", Movimentacaodt);
		request.getSession().setAttribute("Movimentacaone", Movimentacaone);
		
		//Caso o tipo de Movimenta��o escolhido n�o esteja configurado teremos que adicion�-lo na lista para aparecer na combo
		verifiqueMovimentacaoTipo(Movimentacaodt);

		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}

	/**
	 * Tratamentos necess�rios ao realizar uma movimenta��o
	 * @throws Exception 
	 */
	protected void setParametrosAuxiliares(MovimentacaoProcessoDt movimentacaoDt, int paginaAnterior, int paginaatual, HttpServletRequest request, MovimentacaoNe movimentacaoNe, UsuarioNe usuarioNe) throws Exception{

		// Quando modelo foi selecionado monta conte�do para aparecer no editor e j� carrego o tipo do arquivo
		if (!movimentacaoDt.getId_Modelo().equals("") && paginaAnterior == (ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)) {
			ModeloDt modeloDt = movimentacaoNe.consultarModeloId(movimentacaoDt.getId_Modelo(),  movimentacaoDt.getPrimeiroProcessoLista(), usuarioNe.getUsuarioDt());
			movimentacaoDt.setId_ArquivoTipo(modeloDt.getId_ArquivoTipo());
			movimentacaoDt.setArquivoTipo(modeloDt.getArquivoTipo());
			movimentacaoDt.setTextoEditor(modeloDt.getTexto());
		}

		request.setAttribute("PaginaAnterior", paginaatual);
		request.setAttribute("TextoEditor", movimentacaoDt.getTextoEditor());
		request.setAttribute("Id_ArquivoTipo", movimentacaoDt.getId_ArquivoTipo());
		request.setAttribute("ArquivoTipo", movimentacaoDt.getArquivoTipo());
		request.setAttribute("Modelo", movimentacaoDt.getModelo());
		request.setAttribute("PaginaAtual", Configuracao.Editar);

		if (request.getParameter("MensagemOk") != null) request.setAttribute("MensagemOk", request.getParameter("MensagemOk"));
		else request.setAttribute("MensagemOk", "");
		if (request.getParameter("MensagemErro") != null) request.setAttribute("MensagemErro", request.getParameter("MensagemErro"));
		else request.setAttribute("MensagemErro", "");

	}

	/**
	 * Realiza chamada ao m�todo que verifica se processos podem ser movimentados
	 * 
	 * @param processos lista de processos a serem movimentados
	 * @param movimentacaone
	 * @return
	 * @throws Exception
	 */
	protected String verificaMovimentacao(String[] processos, ProcessoDt processo, MovimentacaoProcessoDt movimentacaoDt, MovimentacaoNe movimentacaone, UsuarioNe usuarioNe) throws Exception{
		String Mensagem = "";
		List listaProcessos = new ArrayList();
		if (processos != null) {
			// Para verificar se processo(s) pode(m) ser movimentado(s) deve consultar os dados de cada um
			if (processos.length == 1) {
				if (processo == null || processo.getId().equals("")){
					processo = movimentacaone.consultarProcessoId(processos[0]);
				}
				Mensagem += movimentacaone.podeMovimentar(processo, usuarioNe.getUsuarioDt(), movimentacaoDt.getAcessoOutraServentia());
				listaProcessos.add(processo);
			} else {
				// Consulta dados b�sicos de cada processo e adiciona � lista
				for (int i = 0; i < processos.length; i++) {
					ProcessoDt obj = movimentacaone.consultarProcessoIdCompleto(processos[i]);
					Mensagem += movimentacaone.podeMovimentar(obj, usuarioNe.getUsuarioDt(), movimentacaoDt.getAcessoOutraServentia());
					if (Mensagem.length() > 0){
						break;
					}
					listaProcessos.add(obj);
				}
			}
		}

//		// Verifica se o(s) processo(s) pode(m) ser movimentado(s)
//		for (int i = 0; i < listaProcessos.size(); i++) {
//			ProcessoDt processoDt = (ProcessoDt) listaProcessos.get(i);
//			Mensagem += movimentacaone.podeMovimentar(processoDt, usuarioNe.getUsuarioDt(), movimentacaoDt.getAcessoOutraServentia());
//			if (Mensagem.length() > 0) break;
//		}
		
		if (Mensagem.length() > 0){
			Mensagem = "Movimenta��o n�o permitida para processo(s). Motivo(s): " + Mensagem;
		}else{
			movimentacaoDt.setListaProcessos(listaProcessos);
		}

		return Mensagem;
	}

	/**
	 * M�todo que faz tratamentos necess�rios ao montar a tela de movimenta��o de processos. Captura
	 * os tipos de pend�ncias que o grupo em quest�o pode gerar, e resgata poss�veis arquivos vindos
	 * de Pend�ncia.
	 * @throws Exception 
	 * 
	 */
	protected void montaTelaMovimentacao(String[] processos, MovimentacaoProcessoDt movimentacaoDt, MovimentacaoNe movimentacaone, UsuarioNe usuarioSessao, HttpServletRequest request) throws Exception{
		limparListas(request);

		if (processos.length > 1) {
			movimentacaoDt.setMultiplo(true);
			request.setAttribute("TituloPagina", "Movimentar M�ltiplos Processos");
		}

		// Seta tipos de pend�ncias que poder�o ser geradas
		movimentacaoDt.setListaPendenciaTipos(movimentacaone.consultarTiposPendenciaMovimentacao(usuarioSessao.getUsuarioDt()));

		// Captura lista de arquivos enviados da pendenciact, caso possua
		request.getSession().setAttribute("ListaArquivosDwr", request.getSession().getAttribute("arquivosTransferencia"));
		request.getSession().setAttribute("ListaArquivos", request.getSession().getAttribute("arquivosTransferenciaMap"));
		
		// Seta os tipos de movimenta��o configuradas para o usu�rio e grupo do usu�rio logado, parametrizados por ele
		movimentacaoDt.setListaTiposMovimentacaoConfigurado(movimentacaone.consultarListaMovimentacaoTipoConfiguradoUsuarioGrupo(usuarioSessao.getUsuarioDt()));		
	}

	/**
	 * Resgata lista de arquivos inseridos Converte de Map para List
	 */
	protected List getListaArquivos(HttpServletRequest request) {
		Map mapArquivos = (Map) request.getSession().getAttribute("ListaArquivos");
		List lista = Funcoes.converterMapParaList(mapArquivos);

		return lista;
	}

	/**
	 * Resgata lista de pend�ncias a serem inseridas Converte de Set para List
	 */
	protected List getListaPendencias(HttpServletRequest request) {
		Set listaPendencias = (Set) request.getSession().getAttribute("ListaPendencias");
		List lista = Funcoes.converterSetParaList(listaPendencias);
		return lista;
	}

	protected void limparListas(HttpServletRequest request) {
		// Limpa lista DWR e zera contador Arquivos
		request.getSession().removeAttribute("ListaArquivosDwr");
		request.getSession().removeAttribute("ListaArquivos");
		request.getSession().removeAttribute("Id_ListaArquivosDwr");

		// Limpa lista DWR e zera contador Pend�ncias
		request.getSession().removeAttribute("Id_ListaDadosMovimentacao");
		request.getSession().removeAttribute("ListaPendencias");
	}
	
	/**
	 * Verifica se o tipo de movimenta��o selecionado � um tipo de movimenta��o j� configurado para o usu�rio logado
	 * @param movimentacaodt
	 */
	protected void verifiqueMovimentacaoTipo(MovimentacaoProcessoDt movimentacaodt) throws Exception {
		if ((movimentacaodt.getId_MovimentacaoTipo() == null) || (movimentacaodt.getId_MovimentacaoTipo().trim().equalsIgnoreCase("")) 
		    || (movimentacaodt.getMovimentacaoTipo() == null) || (movimentacaodt.getMovimentacaoTipo().trim().equalsIgnoreCase(""))) return;
		boolean encontrouNaLista = false;	
		List listaMovimentacaoTipo = movimentacaodt.getListaTiposMovimentacaoConfigurado();
		for (int i=0;i<listaMovimentacaoTipo.size();i++){
			UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = (UsuarioMovimentacaoTipoDt)listaMovimentacaoTipo.get(i);
			if (movimentacaodt.getId_MovimentacaoTipo().equalsIgnoreCase(usuarioMovimentacaoTipoDt.getId_MovimentacaoTipo())) encontrouNaLista = true;
		}
		if (!encontrouNaLista){
			UsuarioMovimentacaoTipoDt usuarioMovimentacaoTipoDt = new UsuarioMovimentacaoTipoDt();
			usuarioMovimentacaoTipoDt.setId_MovimentacaoTipo(movimentacaodt.getId_MovimentacaoTipo());
			usuarioMovimentacaoTipoDt.setMovimentacaoTipo(movimentacaodt.getMovimentacaoTipo());
			movimentacaodt.addListaTiposMovimentacaoConfigurado(usuarioMovimentacaoTipoDt);
		}
		
	}
}
