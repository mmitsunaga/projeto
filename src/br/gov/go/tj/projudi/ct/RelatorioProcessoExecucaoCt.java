package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.ProcessoEventoExecucaoDt;
import br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.ne.RelatorioProcessoExecucaoNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;


/**
 * Servlet que controla os relatórios do processo de execução penal.
 * 
 * @author kbsriccioppo
 */
public class RelatorioProcessoExecucaoCt extends Controle{

    private static final long serialVersionUID = 7554535923090150054L;
    
    public int Permissao() {
		return ProcessoEventoExecucaoDt.CodigoPermissao;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		int passoEditar = -1;
	
		String Mensagem = "";
		String MensagemErro = "Os dados não foram encontrados.";
		String stAcao = "";
		List tempList = null;
		byte[] byTemp = null;

		request.setAttribute("tempPrograma", "RelatorioProcessoExecucao");
		request.setAttribute("tempRetorno", "RelatorioProcessoExecucao");
		stAcao = "/WEB-INF/jsptjgo/RelatorioProcessoExecucao.jsp";
		
		// Variáveis auxiliares
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		request.setAttribute("PaginaAtual", Configuracao.Curinga7);
		
		String idServentia = UsuarioSessao.getUsuarioDt().getId_Serventia(); 
			
		if(request.getParameter("nomeBusca1") != null)
			request.getParameter("nomeBusca1");
		if(request.getParameter("nomeBusca2") != null)
			request.getParameter("nomeBusca2");
		if(request.getParameter("nomeBusca3") != null)
			request.getParameter("nomeBusca3");
		
		RelatorioProcessoExecucaoNe relNe = new RelatorioProcessoExecucaoNe();
		
		DataProvavelDt dataProvavelDt;
        SituacaoAtualExecucaoDt situacaoAtualDt = (SituacaoAtualExecucaoDt) request.getSession().getAttribute("SituacaoAtualDt");
        if (situacaoAtualDt == null) situacaoAtualDt = new SituacaoAtualExecucaoDt();
        
		List listaSentenciado;
		String titulo = "";
		this.setListaRegimeRequest(relNe, request);
		
		switch (paginaatual) {
			
			case Configuracao.Curinga7:
				if (request.getParameter("radio") != null){
					passoEditar = Funcoes.StringToInt(request.getParameter("radio"));
				} 
				
				//utilizei o "passoImprimir" para não perder o "passoEditar" utilizado na consulta da paginação
				int passoImprimir = -1;
				if (request.getParameter("PassoImprimir") != null && !request.getParameter("PassoImprimir").equals("null"))
					passoImprimir = Funcoes.StringToInt(request.getParameter("PassoImprimir"));
 
				if (passoImprimir != -1){
					switch(passoImprimir){
						case 1://Data provável - imprime (gera .pdf") do resultado total da consulta 
							dataProvavelDt = new DataProvavelDt();
							dataProvavelDt.setDataInicialPeriodo(request.getParameter("dataInicialPeriodo"));
							dataProvavelDt.setDataFinalPeriodo(request.getParameter("dataFinalPeriodo"));
							dataProvavelDt.setTipoConsulta(request.getParameter("tipoConsulta"));
							listaSentenciado = relNe.consultarPeriodoDataProvavel(dataProvavelDt, "", UsuarioSessao.getUsuarioDt().getId_Serventia());
							
							titulo = "Processos com data provável para " + dataProvavelDt.getTipoConsulta() + " no período de " + dataProvavelDt.getDataInicialPeriodo() + " a ";
							if (dataProvavelDt.getDataFinalPeriodo().length() == 0) titulo += "-";
							else titulo += dataProvavelDt.getDataFinalPeriodo();
							
							byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , listaSentenciado, UsuarioSessao.getUsuarioDt().getServentia(), titulo, "consultaDataProvavel.jasper", "", "", "", dataProvavelDt.getTipoConsulta(), "");													
						    
							enviarPDF(response, byTemp, "DataProvavel" + dataProvavelDt.getTipoConsulta());
						    byTemp = null;
							return;
							
						case 2://Processo sem cálculo - imprime (gera .pdf") do resultado total da consulta
							listaSentenciado = relNe.consultarProcessoSemCalculo("", UsuarioSessao.getUsuarioDt().getId_Serventia());
							byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , listaSentenciado, UsuarioSessao.getUsuarioDt().getServentia(), "Processos sem Cálculo de Liquidação de Pena", "consultaProcessoSemCalculo.jasper", "", "", "", "", "");
													   
							enviarPDF(response, byTemp, "ProcessoSemCalculo");
						    byTemp = null;
							return;
							
						case 3: //Processo com cálculo, processos ativos
							String nomeArquivo = "";
							listaSentenciado = new ArrayList();
							if (passoEditar == 7){//processos ativos
								nomeArquivo = "ProcessoAtivo";
								titulo = "Processos ativos na serventia";
								listaSentenciado = relNe.consultarDadosRelatorio(passoEditar, "", idServentia, UsuarioSessao.getUsuarioDt().getServentia());
								
							} else if (passoEditar == 2){//processo com cálculo
								nomeArquivo = "ProcessoComCalculo";
								dataProvavelDt = new DataProvavelDt();
								dataProvavelDt.setDataInicialPeriodo(request.getParameter("dataInicialPeriodo"));
								dataProvavelDt.setDataFinalPeriodo(request.getParameter("dataFinalPeriodo"));
								titulo = "Processos com Cálculo de Liquidação de Pena no período de " + dataProvavelDt.getDataInicialPeriodo() + " a ";
								if (dataProvavelDt.getDataFinalPeriodo().length() == 0) titulo += "-";
								else titulo += dataProvavelDt.getDataFinalPeriodo();
								listaSentenciado = relNe.consultarPeriodoCalculo(dataProvavelDt, "", UsuarioSessao.getUsuarioDt().getId_Serventia());
							
							} else if (passoEditar == 8){//processos ativos
								nomeArquivo = "ProcessoCalculoAtraso";
								titulo = "Processos com cálculo em atraso";
								listaSentenciado = relNe.consultarDadosRelatorio(passoEditar, "", idServentia, UsuarioSessao.getUsuarioDt().getServentia());
							}
							
							byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , listaSentenciado, UsuarioSessao.getUsuarioDt().getServentia(), titulo, "consultaProcessoComCalculo.jasper", "", "", "", "", "");						
						    enviarPDF(response, byTemp, nomeArquivo);
						    byTemp = null;
							return;
							
						case 4: //Situação atual - imprime (gera .pdf") do resultado total da consulta
							//captura as opções de consulta
//							situacaoAtualDt = new SituacaoAtualExecucaoDt();
//							setDadosSituacaoAtual(situacaoAtualDt, request, listaIdLocal, listaIdRegime, listaIdModalidade, listaIdStatus, listaIdFormaCumprimento);
							
							listaSentenciado = relNe.consultarSituacaoAtualProcessoExecucao(situacaoAtualDt.getListaIdFormaCumprimento(), situacaoAtualDt.getListaIdLocal(), situacaoAtualDt.getListaIdRegime(), situacaoAtualDt.getListaIdStatus(), situacaoAtualDt.getListaIdModalidade(), "", idServentia, situacaoAtualDt.isExcluirPRD());
//							listaSentenciado = relNe.consultarSituacaoAtualProcessoExecucao(situacaoAtualDt, "", UsuarioSessao.getUsuarioDt().getId_Serventia(), excluirPRD);
							
							titulo = "Situação atual dos processos";
							String parametrosConsulta = ""; 
							if (situacaoAtualDt.getLocalCumprimentoPena().length() > 0) parametrosConsulta += " LOCAL: " + situacaoAtualDt.getLocalCumprimentoPena() + ". ";
							if (situacaoAtualDt.getModalidade().length() > 0) parametrosConsulta += "MODALIDADE: " + situacaoAtualDt.getModalidade() + ". ";
							if (situacaoAtualDt.getRegime().length() > 0) parametrosConsulta += "REGIME: " + situacaoAtualDt.getRegime() + ". ";
							if (situacaoAtualDt.getEventoExecucaoStatus().length() > 0) parametrosConsulta += "SITUAÇÃO: " + situacaoAtualDt.getEventoExecucaoStatus() + ". ";
							if (situacaoAtualDt.getFormaCumprimento().length() > 0) parametrosConsulta += "FORMA DE CUMPRIMENTO: " + situacaoAtualDt.getFormaCumprimento() + ". ";
							
							byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , listaSentenciado, UsuarioSessao.getUsuarioDt().getServentia(), titulo, "consultaSituacaoAtual.jasper", "", "", "", "", parametrosConsulta);
							
						    enviarPDF(response, byTemp, "SituacaoAtual");
						    byTemp = null;
							return;
							
						case 5://Processo com término da pena em atraso
							listaSentenciado = relNe.consultarDadosRelatorio(passoEditar, "", idServentia, UsuarioSessao.getUsuarioDt().getServentia());
							String observacao = "";
							String nomeJasper = "";
							String tituloBeneficio = "";
							nomeArquivo = "";
							if (passoEditar == 9){
								titulo = "Processos com provável Término da Pena anterior à data atual";
								observacao = "Ob.: Processos ativos com sentenciado cumprindo pena e data do provável término da pena anterior à data de emissão do relatório.";
								nomeJasper = "ExecpenwebProcessoTerminoPena.jasper";
								nomeArquivo = "ProcessoTerminoPena";

							} else if (passoEditar == 10){
								titulo = "Processos com Provável Progressão de Regime anterior à data atual";
								nomeJasper = "ExecpenwebProcessoBeneficio.jasper";
								observacao = "Ob.: Processos ativos com data provável da progressão de regime anterior à data de emissão do relatório. Porém, não consta o evento de Progressão de Regime com data início posterior à data provável da progressão.";
								nomeArquivo = "ProcessoProgressaoAtraso";
								tituloBeneficio = "Data Provável Progressão";
								
							} else if (passoEditar == 11){
								titulo = "Processos com Provável Livramento Condicional anterior à data atual";
								nomeJasper = "ExecpenwebProcessoBeneficio.jasper";
								observacao = "Ob.: Processos ativos com data provável do Livramento Condicional anterior à data de emissão do relatório. Porém, não consta o evento Concessão do Livramento Condicional com data início posterior à data provável do benefício.";
								nomeArquivo = "ProcessoLivramentoAtraso";
								tituloBeneficio = "Data Provável Livramento";
								
							} else if (passoEditar == 12){
								titulo = "Processos com data de validade do Mandado de Prisão vencido e sentenciado foragido";
								nomeJasper = "ExecpenwebProcessoBeneficio.jasper";
								observacao = "Ob.: Processo ativo com data de validade do mandado de prisão anterior à data de emissão do relatório e sentenciado foragido.";
								nomeArquivo = "ProcessoMandadoPrisaoVencido";
								tituloBeneficio = "Validade Mandado de Prisão";
							}
						
							
							byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , listaSentenciado, UsuarioSessao.getUsuarioDt().getServentia(), titulo, nomeJasper, observacao, UsuarioSessao.getUsuarioDt().getNome(), tituloBeneficio, "", "");							
						    enviarPDF(response, byTemp, nomeArquivo);
						    byTemp = null;
							return;
					}
					
				} else {
					//trata as consultas do processo de execução penal
					//passoEditar 1:  data prováveis - o resultado da consulta
					//passoEditar 2:  processos com cálculo - o resultado da consulta
					//passoEditar 3:  processos sem calculo de liquidação - resultado da consulta
					//passoEditar 4:  situação da execução penal - tela inicial com os parametros para consulta
					//passoEditar 5:  situação da execução penal - o resultado da consulta
					//passoEditar 6:  situação da execução penal - o resultado da consulta
					//passoEditar 7:  processos ativos na serventia
					//passoEditar 8:  cálculo em atraso
					//passoEditar 9:  provável término da pena anterior à data atual
					//passoEditar 10:  provável PR em atraso
					//passoEditar 11:  provável LC em atraso
					//passoEditar 12:  validade do mandado de prisão vencido
					//passoEditar 13:  processos ativos na serventia para maiores de 70 anos
					boolean somenteMaioresDe70Anos = false;
					switch(passoEditar){
						case 1:
							//consulta de data provavel  
							request.setAttribute("TituloPagina", "Consulta de Data Provável");
							dataProvavelDt = new DataProvavelDt();
							listaSentenciado = new ArrayList();
							dataProvavelDt.setDataInicialPeriodo(request.getParameter("dataInicialPeriodo"));
							if (request.getParameter("radioTipoConsulta") != null){
								dataProvavelDt.setTipoConsulta(request.getParameter("radioTipoConsulta"));
							}
							Mensagem = relNe.VerificarConsultaDataProvavel(dataProvavelDt);
							if (Mensagem.length() == 0){
								dataProvavelDt.setDataFinalPeriodo(request.getParameter("dataFinalPeriodo"));
								listaSentenciado = relNe.consultarPeriodoDataProvavel(dataProvavelDt, "", UsuarioSessao.getUsuarioDt().getId_Serventia());
								if (listaSentenciado.size()==0) {
									Mensagem = "Verifique! Não foi encontrado sentenciado para os parâmetros de consulta informados.";
									request.setAttribute("MensagemErro", Mensagem);
									passoEditar = -1; //permanecer na tela inicial
								}
								else{
									request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
									request.setAttribute("DataProvavelDt", dataProvavelDt);
									request.setAttribute("listaSentenciado", listaSentenciado);
									stAcao = "/WEB-INF/jsptjgo/DataProvavelConsulta.jsp";
								}
							} else {
								request.setAttribute("MensagemErro", Mensagem);
								passoEditar = -1; //permanecer na tela inicial
							}
							break;
							
						case 2: //resultado da consulta de processo com cálculo  
							request.setAttribute("TituloPagina", "Consulta de Processos com Cálculo de Liquidação de Pena");
							dataProvavelDt = new DataProvavelDt();
							listaSentenciado = new ArrayList();
							dataProvavelDt.setDataInicialPeriodo(request.getParameter("dataInicialPeriodo"));
							Mensagem = relNe.verificarConsultaProcessoComCalculo(dataProvavelDt);
							if (Mensagem.length() == 0){
								dataProvavelDt.setDataFinalPeriodo(request.getParameter("dataFinalPeriodo"));
								listaSentenciado = relNe.consultarPeriodoCalculo(dataProvavelDt, "", UsuarioSessao.getUsuarioDt().getId_Serventia());
								if (listaSentenciado.size()==0) {
									Mensagem = "Verifique! Não foi encontrado sentenciado para os parâmetros de consulta informados.";
									request.setAttribute("MensagemErro", Mensagem);
									passoEditar = -1; //permanecer na tela inicial
								}
								else{
									request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
									request.setAttribute("DataProvavelDt", dataProvavelDt);
									request.setAttribute("listaSentenciado", listaSentenciado);
									stAcao = "/WEB-INF/jsptjgo/ConsultaProcessoComCalculo.jsp";
								}
							} else {
								request.setAttribute("MensagemErro", Mensagem);
								passoEditar = -1; //permanecer na tela inicial
							}
							
							break;
						case 3:
							//consulta de processos sem calculo de liquidação de pena 
							request.setAttribute("TituloPagina", "Consulta de Processos Sem Cálculo de Liquidação de Pena");
							dataProvavelDt = new DataProvavelDt();
							listaSentenciado = new ArrayList();
							listaSentenciado = relNe.consultarProcessoSemCalculo("", UsuarioSessao.getUsuarioDt().getId_Serventia());
							if (listaSentenciado.size()==0) {
								Mensagem = "Verifique! Não foi encontrado processo para os parâmetros de consulta informados.";
								request.setAttribute("MensagemOk", Mensagem);
							}
							else{
								request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
								request.setAttribute("DataProvavelDt", dataProvavelDt);
								request.setAttribute("listaSentenciado", listaSentenciado);
								stAcao = "/WEB-INF/jsptjgo/ConsultaProcessoSemCalculo.jsp";
							}
							break;
							
						case 4://iniciando a tela consulta de situação atual  
							this.setListaRegimeRequest(relNe, request);
//							stAcao = "/WEB-INF/jsptjgo/ConsultaSituacaoAtualExecucaoPenal.jsp";
							request.setAttribute("TituloPagina", "Consulta da Situação Atual do Processo de Execução Penal");
							situacaoAtualDt = new SituacaoAtualExecucaoDt();
							request.setAttribute("SituacaoAtualDt", situacaoAtualDt);
							break;
							
						case 5://pagina de consulta de situação atual
							break;
							
						case 6:
							//retorno da consulta de situação atual
							request.setAttribute("TituloPagina", "Consulta da Situação Atual do Processo de Execução Penal");
							listaSentenciado = new ArrayList();
							setDadosSituacaoAtual(situacaoAtualDt, request);
							request.setAttribute("SituacaoAtualDt", situacaoAtualDt);
							
							listaSentenciado = relNe.consultarSituacaoAtualProcessoExecucao(situacaoAtualDt.getListaIdFormaCumprimento(), situacaoAtualDt.getListaIdLocal(), situacaoAtualDt.getListaIdRegime(), situacaoAtualDt.getListaIdStatus(), situacaoAtualDt.getListaIdModalidade(), "", idServentia, situacaoAtualDt.isExcluirPRD());
//							listaSentenciado = relNe.consultarSituacaoAtualProcessoExecucao(situacaoAtualDt, PosicaoPaginaAtual, UsuarioSessao.getUsuarioDt().getId_Serventia(), situacaoAtualDt.isExcluirPRD());
							if (listaSentenciado.size()==0) {
								Mensagem = "Verifique! Não foi encontrado sentenciado para os parâmetros de consulta informados.";
								request.setAttribute("MensagemOk", Mensagem);
								passoEditar = 5; //permanecer na tela inicial
							}
							else{
								request.setAttribute("descricaoRegime", situacaoAtualDt.getRegime());
								request.setAttribute("descricaoLocalCumprimentoPena",  situacaoAtualDt.getLocalCumprimentoPena());
								request.setAttribute("descricaoSituacaoProcessoExecucao",  situacaoAtualDt.getEventoExecucaoStatus());
								request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
								request.setAttribute("SituacaoAtualDt", situacaoAtualDt);
								request.setAttribute("listaSentenciado", listaSentenciado);
							}
							stAcao = "/WEB-INF/jsptjgo/ConsultaSituacaoAtualExecucaoPenal.jsp";
							break;
							
						case 13:
							somenteMaioresDe70Anos = true;
						case 7: // processos ativos na serventia
							request.setAttribute("TituloPagina", "Consulta de Processos Ativos da Serventia");
							listaSentenciado = relNe.consultarDadosRelatorio(passoEditar, "", UsuarioSessao.getUsuarioDt().getId_Serventia(), "", somenteMaioresDe70Anos);
							if (listaSentenciado.size()==0) {
								Mensagem = "Verifique! Não foi encontrado sentenciado para os parâmetros de consulta informados.";
								request.setAttribute("MensagemErro", Mensagem);
								passoEditar = -1; //permanecer na tela inicial
							}
							else{
								request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
								request.setAttribute("listaSentenciado", listaSentenciado);
								passoEditar = 7;
								stAcao = "/WEB-INF/jsptjgo/ConsultaProcessoComCalculo.jsp";
							}
							break;
							
						case 8: // cálculo em atraso
							request.setAttribute("TituloPagina", "Consulta de processos com cálculo em atraso");
							listaSentenciado = relNe.consultarDadosRelatorio(passoEditar, "", UsuarioSessao.getUsuarioDt().getId_Serventia(), "");
							if (listaSentenciado.size()==0) {
								Mensagem = "Verifique! Não foi encontrado sentenciado para os parâmetros de consulta informados.";
								request.setAttribute("MensagemErro", Mensagem);
								passoEditar = -1; //permanecer na tela inicial
							}
							else{
								request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
								request.setAttribute("listaSentenciado", listaSentenciado);
								passoEditar = 8;
								stAcao = "/WEB-INF/jsptjgo/ConsultaProcessoComCalculo.jsp";
							}
							break;
							
						case 9:
							request.setAttribute("TituloPagina", "Consulta de processos com provável término da pena em atraso");
							listaSentenciado = relNe.consultarDadosRelatorio(passoEditar, "", UsuarioSessao.getUsuarioDt().getId_Serventia(), "");
							if (listaSentenciado.size()==0) {
								Mensagem = "Verifique! Não foi encontrado sentenciado para os parâmetros de consulta informados.";
								request.setAttribute("MensagemErro", Mensagem);
								passoEditar = -1; //permanecer na tela inicial
							}
							else{
								request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
								request.setAttribute("listaSentenciado", listaSentenciado);
								passoEditar = 9;
								stAcao = "/WEB-INF/jsptjgo/ConsultaProcessoTerminoPena.jsp";
							}
							break;
							
						case 10:
							request.setAttribute("TituloPagina", "Consulta de processos com provável progressão de regime em atraso");
							listaSentenciado = relNe.consultarDadosRelatorio(passoEditar, "", UsuarioSessao.getUsuarioDt().getId_Serventia(), "");
							if (listaSentenciado.size()==0) {
								Mensagem = "Verifique! Não foi encontrado sentenciado para os parâmetros de consulta informados.";
								request.setAttribute("MensagemErro", Mensagem);
								passoEditar = -1; //permanecer na tela inicial
							}
							else{
								request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
								request.setAttribute("listaSentenciado", listaSentenciado);
								request.setAttribute("beneficio", "Provável PR");
								passoEditar = 10;
								stAcao = "/WEB-INF/jsptjgo/ConsultaProcessoTerminoPena.jsp";
							}
							break;
							
						case 11:
							request.setAttribute("TituloPagina", "Consulta de processos com provável livramento condicional em atraso");
							listaSentenciado = relNe.consultarDadosRelatorio(passoEditar, "", UsuarioSessao.getUsuarioDt().getId_Serventia(), "");
							if (listaSentenciado.size()==0) {
								Mensagem = "Verifique! Não foi encontrado sentenciado para os parâmetros de consulta informados.";
								request.setAttribute("MensagemErro", Mensagem);
								passoEditar = -1; //permanecer na tela inicial
							}
							else{
								request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
								request.setAttribute("listaSentenciado", listaSentenciado);
								request.setAttribute("beneficio", "Provável LC");
								passoEditar = 11;
								stAcao = "/WEB-INF/jsptjgo/ConsultaProcessoTerminoPena.jsp";
							}
							break;
							
						case 12:
							request.setAttribute("TituloPagina", "Consulta de processos com data de validade do mandado de prisão vencido");
							listaSentenciado = relNe.consultarDadosRelatorio(passoEditar, "", UsuarioSessao.getUsuarioDt().getId_Serventia(), "");
							if (listaSentenciado.size()==0) {
								Mensagem = "Verifique! Não foi encontrado sentenciado para os parâmetros de consulta informados.";
								request.setAttribute("MensagemErro", Mensagem);
								passoEditar = -1; //permanecer na tela inicial
							}
							else{
								request.setAttribute("QuantidadePaginas", relNe.getQuantidadePaginas());
								request.setAttribute("listaSentenciado", listaSentenciado);
								request.setAttribute("beneficio", "Validade Mandado Prisão");
								passoEditar = 12;
								stAcao = "/WEB-INF/jsptjgo/ConsultaProcessoTerminoPena.jsp";
							}
							break;
					}
				}
			break;
		}

		request.getSession().setAttribute("SituacaoAtualDt", situacaoAtualDt);
		request.setAttribute("PaginaAtual", paginaatual);
		request.setAttribute("PosicaoPaginaAtual", Funcoes.StringToLong(PosicaoPaginaAtual));
		request.setAttribute("PassoEditar", passoEditar);
				
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	protected void setListaRegimeRequest(RelatorioProcessoExecucaoNe relNe, HttpServletRequest request) throws Exception{
//		 if (request.getSession().getAttribute("ListaModalidade") == null) request.getSession().setAttribute("ListaModalidade", relNe.consultarDescricaoRegimeExecucao("", String.valueOf(PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO), "0"));
//		 if (request.getSession().getAttribute("ListaLocal") == null) request.getSession().setAttribute("ListaLocal", relNe.consultarDescricaoLocalCumprimentoPena("", "0"));
//		 if (request.getSession().getAttribute("ListaRegime_PPL") == null) request.getSession().setAttribute("ListaRegime_PPL", relNe.consultarDescricaoRegimeExecucao("", String.valueOf(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE), "0"));
//		 if (request.getSession().getAttribute("ListaStatus") == null) request.getSession().setAttribute("ListaStatus", relNe.consultarDescricaoSituacaoProcessoExecucao("", "0"));
//		 if (request.getSession().getAttribute("ListaFormaCumprimento") == null) request.getSession().setAttribute("ListaFormaCumprimento", relNe.consultarDescricaoFormaCumprimentoExecucao("", "0"));
		relNe.setListaRegimeRequest(request);
   }
	
	private void setDadosSituacaoAtual(SituacaoAtualExecucaoDt situacaoAtualDt, HttpServletRequest request){
		situacaoAtualDt.setIdRegime(request.getParameter("Id_RegimeExecucao"));
		situacaoAtualDt.setRegime(request.getParameter("RegimeExecucao"));
		situacaoAtualDt.setIdEventoExecucaoStatus(request.getParameter("Id_EventoExecucaoStatus"));
		situacaoAtualDt.setEventoExecucaoStatus(request.getParameter("EventoExecucaoStatus"));
		situacaoAtualDt.setIdLocalCumprimentoPena(request.getParameter("Id_LocalCumprimentoPena"));
		situacaoAtualDt.setLocalCumprimentoPena(request.getParameter("LocalCumprimentoPena"));
		situacaoAtualDt.setIdModalidade(request.getParameter("Id_Modalidade"));
		situacaoAtualDt.setModalidade(request.getParameter("Modalidade"));
		situacaoAtualDt.setIdFormaCumprimento(request.getParameter("Id_FormaCumprimento"));
		situacaoAtualDt.setFormaCumprimento(request.getParameter("FormaCumprimento"));
		
		String[] valoresLocal = request.getParameterValues("chkLocal[]");
		List listaIdLocal = new ArrayList();
		if (valoresLocal != null){
			for (int i=0; i<valoresLocal.length; i++){
				listaIdLocal.add(valoresLocal[i]);
			}	
		}
		situacaoAtualDt.setListaIdLocal(listaIdLocal);
		
		String[] valoresRegime = request.getParameterValues("chkRegime[]");
		List listaIdRegime = new ArrayList();
		if (valoresRegime != null){
			for (int i=0; i<valoresRegime.length; i++){
				listaIdRegime.add(valoresRegime[i]);
			}	
		}
		situacaoAtualDt.setListaIdRegime(listaIdRegime);
		
		String[] valoresModalidade = request.getParameterValues("chkModalidade[]");
		List listaIdModalidade = new ArrayList();
		if (valoresModalidade != null){
			for (int i=0; i<valoresModalidade.length; i++){
				listaIdModalidade.add(valoresModalidade[i]);
			}	
		}
		situacaoAtualDt.setListaIdModalidade(listaIdModalidade);
		
		String[] valoresStatus = request.getParameterValues("chkStatus[]");
		List listaIdStatus = new ArrayList();
		if (valoresStatus != null){
			for (int i=0; i<valoresStatus.length; i++){
				listaIdStatus.add(valoresStatus[i]);
			}	
		}
		situacaoAtualDt.setListaIdStatus(listaIdStatus);
		
		String[] valoresFormaCumprimento = request.getParameterValues("chkFormaCumprimento[]");
		List listaIdFormaCumprimento = new ArrayList();
		if (valoresFormaCumprimento != null){
			for (int i=0; i<valoresFormaCumprimento.length; i++){
				listaIdFormaCumprimento.add(valoresFormaCumprimento[i]);
			}	
		}
		situacaoAtualDt.setListaIdFormaCumprimento(listaIdFormaCumprimento);
		
		boolean excluirPRD = false;
		if (request.getParameter("ExcluirPRD") != null && request.getParameter("ExcluirPRD").equalsIgnoreCase("S")){
			excluirPRD = true;
		}
		situacaoAtualDt.setExcluirPRD(excluirPRD);
	}
}
