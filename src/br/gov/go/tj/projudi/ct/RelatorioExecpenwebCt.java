package br.gov.go.tj.projudi.ct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt;
import br.gov.go.tj.projudi.dt.relatorios.RelatorioExecpenwebDt;
import br.gov.go.tj.projudi.dt.relatorios.SituacaoAtualExecucaoPenalDt;
import br.gov.go.tj.projudi.ne.RelatorioExecpenwebNe;
import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.Configuracao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.Relatorios;


/**
 * Servlet que controla os relat�rios do Execpenweb para corregedoria.
 * @author wcsilva
 */
public class RelatorioExecpenwebCt extends Controle{

	private static final long serialVersionUID = -9204648599505048296L;

	public int Permissao() {
		return DataProvavelDt.CodigoPermissao;
	}
	
	public void executar(HttpServletRequest request, HttpServletResponse response, UsuarioNe UsuarioSessao, int paginaatual, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception, ServletException, IOException {

		int passoEditar = -1;		
		String stAcao = "";		
		byte[] byTemp = null;
		RelatorioExecpenwebNe relNe = new RelatorioExecpenwebNe();
		
		request.setAttribute("tempPrograma", "RelatorioExecpenweb");
		request.setAttribute("tempRetorno", "RelatorioExecpenweb");
		stAcao = "/WEB-INF/jsptjgo/RelatorioExecpenweb.jsp";
		
		// Vari�veis auxiliares
		if (request.getParameter("PassoEditar") != null) passoEditar = Funcoes.StringToInt(request.getParameter("PassoEditar"));
		String idServentia = "";
		String serventia = "";
		if (request.getParameter("Id_Serventia") == null || request.getParameter("Id_Serventia").equalsIgnoreCase("null")) idServentia = "";
		else idServentia = request.getParameter("Id_Serventia");
		if (request.getParameter("Serventia") == null || request.getParameter("Serventia").equalsIgnoreCase("null")) serventia = ""; 
		else serventia = request.getParameter("Serventia");
		
		//-Vari�veis para controlar as buscas utilizando ajax
		//List lisNomeBusca = null; 
		//List lisDescricao = null; 
		String stNomeBusca1 = "";
		if(request.getParameter("nomeBusca1") != null) stNomeBusca1 = request.getParameter("nomeBusca1");
		//-fim controle de buscas ajax

		setListaRegimeRequest(relNe, request);
		
		switch (paginaatual) {
		case Configuracao.Novo:
			break;
		
		case (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar):
			if (request.getParameter("Passo")==null){
				//lisNomeBusca = new ArrayList();
				//lisDescricao = new ArrayList();
				String[] lisNomeBusca = {"Serventia"};
				String[] lisDescricao = {"Serventia","Estado"};
				stAcao="/WEB-INF/jsptjgo/Padroes/Localizar.jsp";
				request.setAttribute("tempBuscaId","Id_Serventia");
				request.setAttribute("tempBuscaDescricao","Serventia");
				request.setAttribute("tempBuscaPrograma","Serventia");			
				request.setAttribute("tempRetorno","RelatorioExecpenweb");		
				request.setAttribute("tempDescricaoId","Id");
				request.setAttribute("tempPaginaAtualJSON", Configuracao.Editar);
				request.setAttribute("PaginaAtual", (ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar));
				request.setAttribute("PosicaoPaginaAtual", "0");
				request.setAttribute("QuantidadePaginas", "0");
				request.setAttribute("lisNomeBusca", lisNomeBusca);
				request.setAttribute("lisDescricao", lisDescricao);
				break;
			} else {
				String stTemp="";
				stTemp = relNe.consultarDescricaoServentiaJSON(stNomeBusca1, PosicaoPaginaAtual);
									
					enviarJSON(response, stTemp);
					
				
				return;								
			}
			
		case Configuracao.Imprimir:
			List lista = null;
			String titulo = "";
			String nomeJasper = "";
			String observacao = "";
			
			passoEditar = 0;
			if (request.getParameter("radio") != null){
				passoEditar = Funcoes.StringToInt(request.getParameter("radio"));
			}

			//1: Total de processos com c�lculo no per�odo
			//2: Total de processos com c�lculo em atraso e sem c�lculo
			//4: total de processos por regime/modalidade
			//5: total de processos por serventia
			//6: lista de processos com c�lculo em atraso
			//7: lista de processos com prov�vel t�rmino da pena anterior � data atual
			//8: lista de processos com prov�vel prograss�o de regime em atraso
			//9: lista de processos com prov�vel livramento condicionalem atraso
			//10: lista de processos com validade do mandado de pris�o vencida e sentenciado foragido
			//11: lista de processos ativos na serventia
			//12: lista de processos eletr�nico ativos na serventia
			//13: lista da situa��o atual dos processos
			switch(passoEditar){
				//Total de processos com c�lculo no per�odo
				case 1:
					if (request.getParameter("dataInicialPeriodo") == null || request.getParameter("dataInicialPeriodo").length() == 0){
						request.setAttribute("MensagemErro", "Informe a data inicial para consulta.");
						break;
					} else {
						lista = relNe.consultarTotalProcessoComCalculoPeriodo(request.getParameter("dataInicialPeriodo"), request.getParameter("dataFinalPeriodo"), idServentia);
						if (lista == null || lista.size() == 0){
							request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
							break;
						} else {
							String tipoArquivo = request.getParameter("tipoArquivo");
							titulo = "Total de Processos ativos com C�lculo de Liquida��o de Pena realizado no per�odo " + request.getParameter("dataInicialPeriodo") + " � ";
							if (request.getParameter("dataFinalPeriodo").length() > 0) titulo += request.getParameter("dataFinalPeriodo");
							else titulo += "-";
							
							//tipo 2: PDF, tipo 1: TXT
							if (tipoArquivo.equals("2")){
								nomeJasper = "ExecpenwebProcessoServentia.jasper";							
								byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "", "");
								
							    enviarPDF(response,byTemp,"EXECPENWEBProcessoCalculoPeriodo");
							} else {
								String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
								String conteudoArquivo = titulo + "\nData do relat�rio: " + Funcoes.DataHora(new Date()) + "\nEmitido por: " + UsuarioSessao.getUsuarioDt().getNome() + "\n";
								
								conteudoArquivo += "SERVENTIA" + separadorTxt + "QUANTIDADE" + "\n";
								
								for (int i = 0; i < lista.size(); i++) {
									RelatorioExecpenwebDt obTemp = (RelatorioExecpenwebDt) lista.get(i);
									conteudoArquivo += obTemp.getServentia() + separadorTxt + obTemp.getQuantidade() + "\n";
								}
								 							 
								enviarTXT(response, conteudoArquivo.getBytes(), "EXECPENWEBProcessoCalculoPeriodo");
							}
						    byTemp = null;
							return;	
						}
					}
					
				//Total de processos com c�lculo em atraso e sem c�lculo (nunca foi feito c�lculo de liquida��o de pena), agrupado e ordenado por serventia.
				case 2:
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						titulo = "Total de Processos com c�lculo em atraso e sem c�lculo de liquida��o de pena";
						String tipoArquivo = request.getParameter("tipoArquivo");
						observacao = "Ob.: Considera-se processo com c�lculo em atraso, os processos ativos, sem c�lculo ou com c�lculo anterior � 1 ano considerando a data do relat�rio, onde o sentenciado n�o est� em Livramento Condicional.";
						String nome="";
						//tipo 2: PDF, tipo 1: TXT
						if (tipoArquivo.equals("2")){
							nomeJasper = "ExecpenwebProcessoServentia.jasper";
							byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "", observacao);
							response.setHeader("Content-Disposition", "attachment; filename=EXECPENWEBProcessoCalculoAtrasoServentia"+System.currentTimeMillis()+".pdf");
						    response.setContentType("application/pdf");	
						    enviarPDF(response,byTemp, "EXECPENWEBProcessoCalculoAtrasoServentia");
						} else {
							String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
							String conteudoArquivo = titulo + "\n" + observacao + "\nData do relat�rio: " + Funcoes.DataHora(new Date()) + "\nEmitido por: " + UsuarioSessao.getUsuarioDt().getNome() + "\n";
							
							conteudoArquivo += "SERVENTIA" + separadorTxt + "QUANTIDADE" + "\n";
							
							for (int i = 0; i < lista.size(); i++) {
								RelatorioExecpenwebDt obTemp = (RelatorioExecpenwebDt) lista.get(i);
								conteudoArquivo += obTemp.getServentia() + separadorTxt + obTemp.getQuantidade() + "\n";
							}
														
							enviarTXT(response, conteudoArquivo.getBytes(), "EXECPENWEBProcessoCalculoAtrasoServentia");
						}
											    
						return;
					}
					
				//4: total de processos por regime
				case 4: 
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						String tipoArquivo = request.getParameter("tipoArquivo");
						titulo = "Total de processos por regime/modalidade e status de cumprimento da pena";
						
						//tipo 2: PDF, tipo 1: TXT
						if (tipoArquivo.equals("2")){
							nomeJasper = "ExecpenwebProcessoRegime.jasper";
							byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "", observacao);							
						    enviarPDF(response, byTemp,"EXECPENWEBProcessoRegimeModalidade");
						}else {
							String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
							String conteudoArquivo = titulo + "\nData do relat�rio: " + Funcoes.DataHora(new Date()) + "\nEmitido por: " + UsuarioSessao.getUsuarioDt().getNome() + "\n";
							
							conteudoArquivo += "SERVENTIA" + separadorTxt + "REGIME/MODALIDADE" + separadorTxt + "STATUS" + separadorTxt + "QUANTIDADE" + "\n";
							
							for (int i = 0; i < lista.size(); i++) {
								RelatorioExecpenwebDt obTemp = (RelatorioExecpenwebDt) lista.get(i);
								conteudoArquivo += obTemp.getServentia() + separadorTxt + obTemp.getRegime() + separadorTxt + obTemp.getStatus() + separadorTxt + obTemp.getModalidade() + separadorTxt + obTemp.getQuantidade() + "\n";
							}
							
							enviarTXT(response, conteudoArquivo.getBytes(), "EXECPENWEBProcessoRegimeModalidade");
						}
					    byTemp = null;
						return;
					}
					
				//5: total de processos por serventia
				case 5:
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						String tipoArquivo = request.getParameter("tipoArquivo");
						titulo = "Total de Processos de Execu��o Penal por Serventia";
						observacao = "Ob.: Processos ativos cadastrados no Execpenweb.";
						
						//tipo 2: PDF, tipo 1: TXT
						if (tipoArquivo.equals("2")){
							nomeJasper = "ExecpenwebProcessoServentia.jasper";
							byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "", observacao);
							response.setHeader("Content-Disposition", "attachment; filename=EXECPENWEBProcessoServentia"+System.currentTimeMillis()+".pdf");
						    response.setContentType("application/pdf");	
						} else {
							String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
							String conteudoArquivo = titulo + "\n" + observacao + "\nData do relat�rio: " + Funcoes.DataHora(new Date()) + "\nEmitido por: " + UsuarioSessao.getUsuarioDt().getNome() + "\n";
							
							conteudoArquivo += "SERVENTIA" + separadorTxt + "QUANTIDADE" + "\n";
							
							for (int i = 0; i < lista.size(); i++) {
								RelatorioExecpenwebDt obTemp = (RelatorioExecpenwebDt) lista.get(i);
								conteudoArquivo += obTemp.getServentia() + separadorTxt + obTemp.getQuantidade() + "\n";
							}
							byTemp = conteudoArquivo.getBytes();
							
						}
						
					    enviarPDF(response, byTemp, "EXECPENWEBProcessoServentia");
					    byTemp = null;
						return;
					}
					
				//Lista de processos com c�lculo em atraso, agrupados por serventia e ordenado por serventia e data do c�lculo, respectivamente.
				//ob.: Considera-se c�lculo em atraso, os processos, onde o sentenciado n�o est� em Livramento condicional, com per�odo de c�lculo maior que 1 ano, � partir da data do relat�rio.
				//Dados do relat�rio: N�mero do processo, dados do �ltimo c�lculo (data do c�lculo, data da prov�vel progress�o, data do prov�vel livramento, data de homologa��o do c�lculo, data de validade do mandado de pris�o, data do t�rmino da pena) e �ltimo regime
				case 6:
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						titulo = "Processos com c�lculo em atraso e sem c�lculo";
						nomeJasper = "ExecpenwebProcessoCalculoAtraso.jasper";
						observacao = "Ob.: Considera-se processo com c�lculo em atraso, os processos ativos, sem c�lculo ou com c�lculo anterior � 1 ano considerando a data do relat�rio, onde o sentenciado n�o est� em Livramento Condicional.";
						byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "", observacao);						
					    enviarPDF(response, byTemp, "EXECPENWEBCalculoAtraso");
					    byTemp = null;
						return;
					}
					
				//Lista de processos com data de t�rmino da pena anterior � data atual, agrupados por serventia
				case 7:
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						titulo = "Processos com prov�vel T�rmino da Pena anterior � data atual";
						nomeJasper = "ExecpenwebProcessoTerminoPena.jasper";
						observacao = "Ob.: Processos ativos com sentenciado cumprindo pena e data do prov�vel t�rmino da pena anterior � data de emiss�o do relat�rio.";
						byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "", observacao);
						
					    enviarPDF(response, byTemp, "EXECPENWEBTerminoPena");
					    byTemp = null;
						return;
					}

				//8: lista de processos com prov�vel progress�o de regime em atraso
				//Lista dos processos que n�o receberam a Progress�o de Regime, agrupados por serventia.
				//Ob.: o sentenciado t�m data prov�vel de Progress�o de Regime anterior � data atual, o c�lculo est� atualizado, mas n�o recebeu a Progress�o.
				case 8:
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						titulo = "Processos com Prov�vel Progress�o de Regime anterior � data atual";
						nomeJasper = "ExecpenwebProcessoBeneficio.jasper";
						observacao = "Ob.: Processos ativos com data prov�vel da progress�o de regime anterior � data de emiss�o do relat�rio. Por�m, n�o consta o evento de Progress�o de Regime com data in�cio posterior � data prov�vel da progress�o.";
						byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "Data Prov�vel PR", observacao);						
					    enviarPDF(response, byTemp, "EXECPENWEBProvavelPRAtraso");
					    byTemp = null;
						return;
					}
					
				//9: lista de processos com prov�vel livramento condicional em atraso
				case 9:
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						titulo = "Processos com Prov�vel Livramento Condicional anterior � data atual";
						nomeJasper = "ExecpenwebProcessoBeneficio.jasper";
						observacao = "Ob.: Processos ativos com data prov�vel do Livramento Condicional anterior � data de emiss�o do relat�rio. Por�m, n�o consta o evento Concess�o do Livramento Condicional com data in�cio posterior � data prov�vel do benef�cio.";
						byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "Data Prov�vel LC", observacao);
						
					    enviarPDF(response, byTemp, "EXECPENWEBProvavelLCAtraso");
					    byTemp = null;
						return;
					}
					
				//10: lista de processos com validade do mandado de pris�o vencida e sentenciado foragido
				case 10:
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						titulo = "Processos com data de validade do Mandado de Pris�o vencido e sentenciado foragido";
						nomeJasper = "ExecpenwebProcessoBeneficio.jasper";
						observacao = "Ob.: Processo ativo com data de validade do mandado de pris�o anterior � data de emiss�o do relat�rio e sentenciado foragido.";
						byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "Validade Mandado de Pris�o", observacao);
						
					    enviarPDF(response, byTemp, "EXECPENWEBProvavelMandadoVencido");
					    byTemp = null;
						return;
					}
					
				//processos ativos na serventia
				case 11:
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
						String conteudoArquivo = "Processos ativos na serventia\nData do relat�rio: " + Funcoes.DataHora(new Date()) + "\nEmitido por: " + UsuarioSessao.getUsuarioDt().getNome() + "\n";
						conteudoArquivo += "COMARCA" + separadorTxt + "SERVENTIA" + separadorTxt + "N�MERO DO PROCESSO (CNJ)" + separadorTxt + "NOME DO SENTENCIADO" + separadorTxt 
											+ "REGIME" + separadorTxt + "STATUS" + separadorTxt + "�LTIMO C�LCULO" + separadorTxt + "PROV�VEL T�RMINO DA PENA"
											+ "PROV�VEL LIVRAMENTO" + separadorTxt + "PROV�VEL PROGRESS�O" + separadorTxt + "VALIDADE MANDADO DE PRIS�O" + separadorTxt + "HOMOLOGA��O DO C�LCULO\n";
						
						for (int i = 0; i < lista.size(); i++) {
							RelatorioExecpenwebDt obTemp = (RelatorioExecpenwebDt) lista.get(i);
							conteudoArquivo += obTemp.getComarca() + separadorTxt + obTemp.getServentia() + separadorTxt + obTemp.getProcessoNumeroCompleto()
												+ separadorTxt + obTemp.getSentenciado() + separadorTxt + obTemp.getRegime() + separadorTxt + obTemp.getStatus() 
												+ separadorTxt + obTemp.getDataCalculo() + separadorTxt + obTemp.getDataTerminoPena()
												+ separadorTxt + obTemp.getDataLivramento() + separadorTxt + obTemp.getDataProgressao()
												+ separadorTxt + obTemp.getDataValidadeMandadoPrisao() + separadorTxt + obTemp.getDataHomologacaoCalculo()
												+ "\n";
						}
												 
					    enviarTXT(response, conteudoArquivo.getBytes(), "EXECPENWEBProcessoAtivosServentia");
					    byTemp = null;
						return;
					}
					
				//processos eletr�nico ativos na serventia
				case 12:
					lista = relNe.consultarDadosRelatorio(passoEditar, idServentia);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
						String conteudoArquivo = "Processos eletr�nico ativos na serventia\nData do relat�rio: " + Funcoes.DataHora(new Date()) + "\nEmitido por: " + UsuarioSessao.getUsuarioDt().getNome() + "\n";
						conteudoArquivo += "COMARCA" + separadorTxt + "SERVENTIA" + separadorTxt + "JUIZ" + separadorTxt + "N�MERO DO PROCESSO (CNJ)" + separadorTxt + "DATA DO PROTOCOLO" + separadorTxt + "ULTIMA MOVIMENTA��O (FASE ATUAL)" + separadorTxt + "DATA DA �LTIMA MOVIMENTA��O (FASE ATUAL)" + separadorTxt + "NATUREZA" + "\n";
						
						for (int i = 0; i < lista.size(); i++) {
							RelatorioExecpenwebDt obTemp = (RelatorioExecpenwebDt) lista.get(i);
							conteudoArquivo += obTemp.getComarca() + separadorTxt + obTemp.getServentia() + separadorTxt + obTemp.getServentiaCargo() + separadorTxt + obTemp.getProcessoNumeroCompleto() + separadorTxt + obTemp.getDataProtocolo() + separadorTxt + obTemp.getFase() + separadorTxt + obTemp.getDataFase() + separadorTxt + obTemp.getNatureza() + "\n";
						}
												
					    enviarTXT(response, conteudoArquivo.getBytes(), "EXECPENWEBProcessoEletronicoAtivosServentia");
					    byTemp = null;
						return;
					}
					
				//situa��o atual dos processos
				case 13:
					String[] valoresLocal = request.getParameterValues("chkLocal[]");
					List listaIdLocal = new ArrayList();
					if (valoresLocal != null){
						for (int i=0; i<valoresLocal.length; i++){
							listaIdLocal.add(valoresLocal[i]);
						}	
					}
					
					String[] valoresRegime = request.getParameterValues("chkRegime[]");
					List listaIdRegime = new ArrayList();
					if (valoresRegime != null){
						for (int i=0; i<valoresRegime.length; i++){
							listaIdRegime.add(valoresRegime[i]);
						}	
					}
					
					String[] valoresModalidade = request.getParameterValues("chkModalidade[]");
					List listaIdModalidade = new ArrayList();
					if (valoresModalidade != null){
						for (int i=0; i<valoresModalidade.length; i++){
							listaIdModalidade.add(valoresModalidade[i]);
						}	
					}
					
					String[] valoresStatus = request.getParameterValues("chkStatus[]");
					List listaIdStatus = new ArrayList();
					if (valoresStatus != null){
						for (int i=0; i<valoresStatus.length; i++){
							listaIdStatus.add(valoresStatus[i]);
						}	
					}
					
					String[] valoresFormaCumprimento = request.getParameterValues("chkFormaCumprimento[]");
					List listaIdFormaCumprimento = new ArrayList();
					if (valoresFormaCumprimento != null){
						for (int i=0; i<valoresFormaCumprimento.length; i++){
							listaIdFormaCumprimento.add(valoresFormaCumprimento[i]);
						}	
					}
					
					boolean excluirPRD = false;
					if (request.getParameter("ExcluirPRD") != null && request.getParameter("ExcluirPRD").equalsIgnoreCase("S")){
						excluirPRD = true;
					}
					
					lista = relNe.consultarSituacaoAtual(listaIdFormaCumprimento, listaIdLocal, listaIdRegime, listaIdModalidade, listaIdStatus, idServentia, excluirPRD);
					if (lista == null || lista.size() == 0){
						request.setAttribute("MensagemErro", "Nenhum dado encontrado.");
						break;
					} else {
						
						
						String tipoArquivo = request.getParameter("tipoArquivo");
						titulo = "Situa��o atual dos processos na serventia";
						if (request.getParameter("LocalCumprimentoPena").length() > 0) 
							observacao += "Local de cumprimento da pena: " + request.getParameter("LocalCumprimentoPena") + ". ";
						if (request.getParameter("Modalidade").length() > 0)
							observacao += "Modalidade: " + request.getParameter("Modalidade") + ". ";
						if (request.getParameter("RegimeExecucao").length() > 0)
							observacao += "Regime: " + request.getParameter("RegimeExecucao") + ". ";
						if (request.getParameter("EventoExecucaoStatus").length() > 0)
							observacao += "Situa��o: " + request.getParameter("EventoExecucaoStatus") + ". ";
						if (request.getParameter("FormaCumprimento").length() > 0)
							observacao += "Forma de Cumprimento: " + request.getParameter("FormaCumprimento") + ". ";
						
						//tipo 2: PDF, tipo 1: TXT
						if (tipoArquivo.equals("2")){
							nomeJasper = "ExecpenwebSituacaoAtual.jasper";
							byTemp = relNe.imprimirRelatorio(ProjudiPropriedades.getInstance().getCaminhoAplicacao() , UsuarioSessao.getUsuarioDt().getNome(), lista, titulo, nomeJasper, "", observacao);
							
							String nome="EXECPENWEBSituacaoAtual_" + request.getParameter("Serventia").toString().replace(" - EXECPENWEB", "").replace(" - ", "_").replace(" ", "") + "_" + request.getParameter("RegimeExecucao") ;
						    	
						    enviarPDF(response, byTemp, nome);
						} else {
							String separadorTxt = Relatorios.getSeparadorRelatorioTxt();
							String conteudoArquivo = titulo + "\n" + observacao + "\n" + "Data do relat�rio: " + Funcoes.DataHora(new Date()) + "\nEmitido por: " + UsuarioSessao.getUsuarioDt().getNome() + "\n";
							conteudoArquivo += "SERVENTIA" + separadorTxt + "N�MERO DO PROCESSO (CNJ)" + separadorTxt + "SENTENCIADO" + "\n";
							
							for (int i = 0; i < lista.size(); i++) {
								SituacaoAtualExecucaoPenalDt obTemp = (SituacaoAtualExecucaoPenalDt) lista.get(i);
								conteudoArquivo += obTemp.getServentia() + separadorTxt + obTemp.getNumeroProcessoCompleto() + separadorTxt + obTemp.getInformacaoSentenciado() + "\n";
							}							
							  
							enviarTXT(response, conteudoArquivo.getBytes(), "EXECPENWEBSituacaoAtualProcesso");
						}
						
					    byTemp = null;
						return;
					}
					
				default:
					break;
			}
			break;
		}

		request.setAttribute("PaginaAtual", paginaatual);
		request.setAttribute("PassoEditar", passoEditar);
		request.setAttribute("Id_Serventia", idServentia);
		request.setAttribute("Serventia", serventia);
				
		RequestDispatcher dis = request.getRequestDispatcher(stAcao);
		dis.include(request, response);
	}
	
	private void setListaRegimeRequest(RelatorioExecpenwebNe relNe, HttpServletRequest request) throws Exception{
		 if (request.getSession().getAttribute("ListaModalidade") == null) request.getSession().setAttribute("ListaModalidade", relNe.consultarDescricaoRegimeExecucao("", String.valueOf(PenaExecucaoTipoDt.PENA_RESTRITIVA_DIREITO), "0"));
		 if (request.getSession().getAttribute("ListaLocal") == null) request.getSession().setAttribute("ListaLocal", relNe.consultarDescricaoLocalCumprimentoPena("", "0"));
		 if (request.getSession().getAttribute("ListaRegime_PPL") == null) request.getSession().setAttribute("ListaRegime_PPL", relNe.consultarDescricaoRegimeExecucao("", String.valueOf(PenaExecucaoTipoDt.PENA_PRIVATIVA_LIBERDADE), "0"));
		 if (request.getSession().getAttribute("ListaStatus") == null) request.getSession().setAttribute("ListaStatus", relNe.consultarDescricaoSituacaoProcessoExecucao("", "0"));
		 if (request.getSession().getAttribute("ListaFormaCumprimento") == null) request.getSession().setAttribute("ListaFormaCumprimento", relNe.consultarDescricaoFormaCumprimentoExecucao("", "0"));
	}
}
