<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="FinanceiroConsultarRepassePrefeituraDt" scope="session" class="br.gov.go.tj.projudi.dt.FinanceiroConsultarRepassePrefeituraDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<title>TJGO/Projudi - Formul�rio Financeiro de Consulta de Previs�o de Repasse da Prefeitura</title>
		<style type="text/css">
		     @import url('./css/Principal.css');
		     @import url('./css/Paginacao.css');
		     #bkg_projudi { display: none;}
		</style>
		
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarNumeroProcesso.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
		
		<style>
			.TotalizadorDireita{					
				background-color:#C8C8C8;
				font-weight:bold;
				text-align:right !important;						
			}
			.TotalizadorCentralizado{				
				background-color:#C8C8C8;
				font-weight:bold;
				text-align: center !important;		
			}
		</style>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">	  	
			<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| </h2></div>
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="FinanceiroConsultarGuias" id="FinanceiroConsultarGuias">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>" />
				<input id="PaginaEditar" name="PaginaEditar" type="hidden" value="<%=request.getAttribute("PaginaEditar")%>" />
				<input id="RelatorioPDF" name="RelatorioPDF" type="hidden" value="<%=request.getAttribute("RelatorioPDF")%>" />								
			
				<div id="divEditar" class="divEditar">
                	<fieldset id="VisualizaDados" class="VisualizaDados">
				    	<legend> Consultar Previs�o de Repasse da Prefeitura </legend>
				    	
				    	<label class="formEdicaoLabel">
							Filtrar
						</label><br>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Pela Data de Baixa (Movimento) da Guia <input type="radio" id="tipoFiltroData" name="tipoFiltroData" value="1" <%=(FinanceiroConsultarRepassePrefeituraDt.getTipoFiltroData().equals("1")?" checked ":"")%> />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Pela Data de Previs�o do Repasse ao TJGO <input type="radio" id="tipoFiltroData" name="tipoFiltroData" value="0" <%=(FinanceiroConsultarRepassePrefeituraDt.getTipoFiltroData().equals("0")?" checked ":"")%> />
				    
				    	<br />
				    	<br />
						<label class="formEdicaoLabel" for="dataInicio">
							Per�odo 
						</label><br>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input name="dataInicio" id="dataInicio" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=FinanceiroConsultarRepassePrefeituraDt.getDataInicio().getDataFormatadaddMMyyyy()%>" />
						<img id="calendarioDdataInicioPagamento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calend�rio Data Inicio"  alt="Calend�rio Data Inicio" onclick="displayCalendar(document.forms[0].dataInicio,'dd/mm/yyyy',this)" />
												
						&nbsp;�&nbsp;
						
						<input name="dataFim" id="dataFim" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" value="<%=FinanceiroConsultarRepassePrefeituraDt.getDataFim().getDataFormatadaddMMyyyy() %>" />
						<img id="calendarioDataFimPagamento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calend�rio Data Fim"  alt="Calend�rio Data Fim" onclick="displayCalendar(document.forms[0].dataFim,'dd/mm/yyyy',this)" />
												
						<br />						
						<label class="formEdicaoLabel">
							Tipo
						</label><br>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Sint�tico <input type="radio" id="tipoFiltroDetalhe" name="tipoFiltroDetalhe" value="1" <%=(FinanceiroConsultarRepassePrefeituraDt.getTipoFiltroDetalhe().equals("1")?" checked ":"")%> />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						Anal�tico <input type="radio" id="tipoFiltroDetalhe" name="tipoFiltroDetalhe" value="0" <%=(FinanceiroConsultarRepassePrefeituraDt.getTipoFiltroDetalhe().equals("0")?" checked ":"")%> />
						<br />
					</fieldset>					
					<br/>				
					<center>
						<button style="width:120px;" name="consultarGuias" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" >
							Consultar
						</button>
						<button style="width:120px;" name="ImprimirGuias" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')" >
							Imprimir CSV
						</button>
						<button style="width:120px;" name="limparFormulario" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>');" >
							Limpar
						</button>
					</center>										
				</div>
				
				<%if(FinanceiroConsultarRepassePrefeituraDt.possuiRelatorio()) {%>
					<table id="Tabela" class="Tabela">
						<tr class="TabelaLinhaboLinha1"  >
							<td class="Centralizado">
								<div id="disponibilidade30d" class="divEditar">
									<fieldset class="formEdicao"  > 
										<legend class="formEdicaoLegenda">Previs�o total para o repasse no per&iacute;odo: 
											<%=FinanceiroConsultarRepassePrefeituraDt.getRelatorio().getDataInicioRepasse().getDataFormatadaddMMyyyy()%>
											<%
												if(!FinanceiroConsultarRepassePrefeituraDt.getRelatorio().getDataInicioRepasse().getDataFormatadaddMMyyyy().equals(FinanceiroConsultarRepassePrefeituraDt.getRelatorio().getDataFimPrevisaoRepasse().getDataFormatadaddMMyyyy())) {
											%>
											 a 
											<%= FinanceiroConsultarRepassePrefeituraDt.getRelatorio().getDataFimPrevisaoRepasse().getDataFormatadaddMMyyyy() %>
											<% } %> 
									   </legend>
										<label class="formEdicaoLabel">Total Guias:&nbsp;R$&nbsp;<%=FinanceiroConsultarRepassePrefeituraDt.getRelatorio().getValorTotalDasGuias()%></label><br>
										<label class="formEdicaoLabel">Total Repasse:&nbsp;R$&nbsp;<%=FinanceiroConsultarRepassePrefeituraDt.getRelatorio().getValorTotalPrevistoParaRepasse()%></label><br>
										<label class="formEdicaoLabel">Diferen�a:&nbsp;R$&nbsp;<%=FinanceiroConsultarRepassePrefeituraDt.getRelatorio().getDiferenca()%></label><br>										
									</fieldset>						
								</div>
							</td>																	
						</tr>	
					</table>	
					<div id="divTabela" class="divTabela" > 
							<table id="Tabela" class="Tabela">
								<thead>
									<tr>
										<th class="colunaMinima"></th>
										<th>GUIA</th>
										<th>PROCESSO</th>
										<th>BAIXA (MOVIMENTO)</th>
										<th>PAGAMENTO</th>	
										<td>PREVIS�O REPASSE</td>
										<td>VALOR GUIA</td>
										<td>VALOR RECEBIMENTO/REPASSE</td>															 
									</tr>
								</thead>
								<tbody id="tabListaRepassePrefeitura">						
									<%
									  if (!FinanceiroConsultarRepassePrefeituraDt.getRelatorio().possuiPrevisaoParaRepasse()) {
															%>
										<tr class="TabelaLinha2"  >
											<td class="Centralizado" colspan="6">
												N�o existem repasses para o per�odo informado			 
											</td>																											
										</tr>	
									<% } else {
																																
											Iterator<OcorrenciaDiariaPrevisaoRepasseDt> iteratorDiario = FinanceiroConsultarRepassePrefeituraDt.getRelatorio().getIteratorOcorrenciaPrevisaoRepasseDiarias();
											Iterator<OcorrenciaPrevisaoRepasseDt> iteratorItem = null;
													
											OcorrenciaDiariaPrevisaoRepasseDt objDiarioTemp = null;														
											OcorrenciaPrevisaoRepasseDt objTemp = null;																						
																																																					
											boolean boLinha=false;									
											long qtdeLinha = 0;
											
											while (iteratorDiario.hasNext()){
												objDiarioTemp = iteratorDiario.next();
												iteratorItem = objDiarioTemp.getIteratorDePrevisaoRepasse();
												
												if (FinanceiroConsultarRepassePrefeituraDt.getTipoFiltroDetalhe().equalsIgnoreCase("0")) {
													while (iteratorItem.hasNext()){
																																
														objTemp = iteratorItem.next();
														qtdeLinha += 1; %>										
														<tr class="TabelaLinha<%=(boLinha?1:2)%>">
															<td class="Centralizado">
																 <%=qtdeLinha%>
															</td>
															<td class="Centralizado">
																<%= objTemp.getNumeroGuiaEmissao() %>
															</td>
															<td class="Centralizado">
																<%= objTemp.getNumeroProcesso() %>
															</td>
															<td class="Centralizado">
																<%=objTemp.getDataMovimento().getDataFormatadaddMMyyyy()%>
															</td>
															<td class="Centralizado">
																<%=objTemp.getDataPagamento().getDataFormatadaddMMyyyy()%>
															</td>
															<td class="Centralizado">
																<%= objTemp.getDataPrevisaoRepasse().getDataFormatadaddMMyyyy() %>
															</td>
															<td class="Centralizado">
																R$&nbsp;<%= objTemp.getValorTotalGuia() %>
															</td>
															<td class="Centralizado">
																R$&nbsp;<%= objTemp.getValorRecebimento() %>
															</td>																											
														</tr>	
																				
												<%
														boLinha = !boLinha;
													}
												}
												
												%>
												
												<tr>
													<td class="TotalizadorDireita" colspan="5">
														Data Previs�o de Repasse&nbsp;<%= objDiarioTemp.getDataPrevisaoRepasse().getDataFormatadaddMMyyyy() %>:  
													</td>
													<td class="TotalizadorCentralizado">
														Total Guias:&nbsp;R$&nbsp;<%= objDiarioTemp.getValorTotalDasGuias() %>
													</td>
													<td class="TotalizadorCentralizado">
														Total Repasse:&nbsp;R$&nbsp;<%= objDiarioTemp.getValorTotalPrevistoParaRepasse() %>
													</td>
													<td class="TotalizadorCentralizado">
														Diferen�a:&nbsp;R$&nbsp;<%= objDiarioTemp.getDiferenca() %>
													</td>
												</tr>	
												
												<%
												
												boLinha = false;
											}//Fim do while	
										
										}//Fim do if/else
																						
									%>
								</tbody>
							</table>			
						</div>			          								
				<%}%>
				<br />
			</form>   
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>