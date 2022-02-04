<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="FinanceiroConsultarRepassePrefeituraWebServiceDt" scope="session" class="br.gov.go.tj.projudi.dt.FinanceiroConsultarRepassePrefeituraWebServiceDt"/>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<title>TJGO/Projudi - Formulário Financeiro de Consulta de Repasse Prefeitura - Web Service</title>
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
		
		<script type="text/javascript">
			function liparDadosDivWebService() {
				$('#divTabela').empty();
				$('#divConfirmarSalvar').empty();				
			}
		</script>
		
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
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />					
			
				<div id="divEditar" class="divEditar">
                	<fieldset id="VisualizaDados" class="VisualizaDados">
				    	<legend> Consultar Repasse da Prefeitura - Web Service </legend>
				    	
				    	<label class="formEdicaoLabel" for="dataInicio">
							Data de Baixa (Movimento) Informada na Ordem de Pagamento 
						</label><br>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<input name="dataMovimento" id="dataMovimento" type="text" size="15" maxlength="10" onkeyup="mascara_data(this)" onblur="verifica_data(this)" onchange="liparDadosDivWebService();" value="<%=FinanceiroConsultarRepassePrefeituraWebServiceDt.getDataMovimento().getDataFormatadaddMMyyyy()%>" />
						<img id="calendariodataMovimento" src="./imagens/dlcalendar_2.gif" height="13" width="13" title="Calendário Data Repasse"  alt="Calendário Data Repasse" onclick="displayCalendar(document.forms[0].dataMovimento,'dd/mm/yyyy',this)" />
						
						<br />
					</fieldset>					
					<br/>				
					<center>
						<button style="width:120px;" name="consultarRepasse" value="Consultar" onclick="liparDadosDivWebService(); AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" >
							Consultar
						</button>
						<button style="width:120px;" name="ImprimirGuias" value="Consultar" onclick="liparDadosDivWebService(); AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')" >
							Imprimir CSV
						</button>
						<button style="width:120px;" name="limparFormulario" value="Limpar" onclick="liparDadosDivWebService(); AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>');" >
							Limpar
						</button>
					</center>										
				</div>
				<div id="divTabela" class="divTabela" >
					<%if(FinanceiroConsultarRepassePrefeituraWebServiceDt.possuiRelatorio()) {%>
						<%if (!FinanceiroConsultarRepassePrefeituraWebServiceDt.getRelatorio().possuiRepasse()) { %>
							<fieldset class="fieldEdicaoEscuro">
								<legend>Informações do Repasse</legend>
								Não existem repasses para a data informada.
							</fieldset>	
						<% } else {%>	
							<fieldset class="fieldEdicaoEscuro">
								<legend>Data do Repasse ao TJGO</legend>
								<%if(FinanceiroConsultarRepassePrefeituraWebServiceDt.getRelatorio().getDataRepasseConfirmado() != null) {%>
									Repasse confirmado em <%= FinanceiroConsultarRepassePrefeituraWebServiceDt.getRelatorio().getDataRepasseConfirmado().getDataFormatadaddMMyyyy() %>.
								<% } else { %>
									Repasse ainda não confirmado.
								<% } %>
							</fieldset>	
							<table id="Tabela" class="Tabela">
								<thead>
									<tr>
										<th class="colunaMinima"></th>
										<th>PROCESSO</th>
										<th>GUIA</th>
										<th>TIPO MOVIMENTO</th>
										<td>VALOR ARRECADAÇÃO</td>
										<th>DATA ARRECADAÇÃO</th>
										<td>BANCO ARRECADAÇÃO</td>
										<td>AGÊNCIA ARRECADAÇÃO</td>																									 
									</tr>
								</thead>
								<tbody id="tabListaRepassePrefeitura">						
									<%
										Iterator<OcorrenciaPrevisaoRepasseWebServiceDt> iterator = FinanceiroConsultarRepassePrefeituraWebServiceDt.getRelatorio().getIteratorOcorrenciaRepasseDiarias();
										
										OcorrenciaPrevisaoRepasseWebServiceDt objTemp = null;																						
																																																				
										boolean boLinha=false;									
										long qtdeLinha = 0;
										
										while (iterator.hasNext()){
											objTemp = iterator.next();												
											
											qtdeLinha += 1; %>										
											<tr class="TabelaLinha<%=(boLinha?1:2)%>">
												<td class="Centralizado">
													 <%=qtdeLinha%>
												</td>
												<td class="Centralizado">
													<%= objTemp.getNumeroProcesso() %>
												</td>
												<td class="Centralizado">
													<%= objTemp.getNumeroGuia() %>
												</td>
												<td class="Centralizado">
													<%= objTemp.getTextoMovimento() %>
												</td>
												<td class="Centralizado">
													R$&nbsp;<%= objTemp.getValorcustas() %>
												</td>
												<td class="Centralizado">
													<%= objTemp.getDataArrecadacao().getDataFormatadaddMMyyyy() %>
												</td>
												<td class="Centralizado">
													<%= objTemp.getNumeroBancoPagamento() %>
												</td>
												<td class="Centralizado">
													<%= objTemp.getNumeroAgenciaPagamento() %>
												</td>																											
											</tr>	
																			
											<% boLinha = !boLinha; %>
											
											<%												
											boLinha = false;
										}//Fim do while									
									%>
									<tr>
										<td colspan="3" class="TotalizadorCentralizado">
											Total de Pagamentos:&nbsp;R$&nbsp;<%= FinanceiroConsultarRepassePrefeituraWebServiceDt.getRelatorio().getValorTotalDosPagamentos() %>
										</td>
										<td colspan="2" class="TotalizadorCentralizado">
											Total de Estornos:&nbsp;R$&nbsp;<%= FinanceiroConsultarRepassePrefeituraWebServiceDt.getRelatorio().getValorTotalDosEstornos() %>
										</td>
										<td colspan="3" class="TotalizadorCentralizado">
											Total Repasse:&nbsp;R$&nbsp;<%= FinanceiroConsultarRepassePrefeituraWebServiceDt.getRelatorio().getValorRepasse() %>														
										</td>
									</tr>
								</tbody>
							</table>	
						
							<% if (FinanceiroConsultarRepassePrefeituraWebServiceDt.getRelatorio().getDataRepasseConfirmado() == null && (new TJDataHora().ehApos(FinanceiroConsultarRepassePrefeituraWebServiceDt.getDataMovimento()))) { %>
								<center>
									<button style="width:300px;" name="ConfirmarRepasse" value="Confirmar Repasse" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>'); AlterarValue('PassoEditar','1');" >
										Confirmar Repasse para TJGO
									</button>
									
									<% if (FinanceiroConsultarRepassePrefeituraWebServiceDt.getRelatorio().getDataPagamentoConfirmado() == null) { %>									
										<button style="width:300px;" name="ConfirmarPagamento" value="Confirmar Pagamento" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>'); AlterarValue('PassoEditar','2');" >
											Confirmar Pagamento da Guia à Prefeitura
										</button>									
									<% } %>
																								
								</center>						
							<%}%>
						<%}%>		          								
					<%}%>
				</div>
				<br />
				<%@ include file="Padroes/Mensagens.jspf" %>
				<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</form>			   
		</div>		
	</body>
</html>