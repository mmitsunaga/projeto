<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GuiaEmissaoCompletaDt"%>
<%@page import="br.gov.go.tj.projudi.ne.GuiaEmissaoNe"%>
<%@page import="br.gov.go.tj.projudi.util.enumeradoresSeguros.EnumSistemaProcessoTJGO"%>

<jsp:useBean id="guiaEmissaoConsultaDt" scope="session" class= "br.gov.go.tj.projudi.dt.GuiaEmissaoConsultaDt"/>
<jsp:useBean id="guiaEmissaoConsultaNe" scope="session" class= "br.gov.go.tj.projudi.ne.GuiaEmissaoConsultaNe"/>

<html>
	<head>
	
	<title>Consulta Emissão de Guias</title>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/menusimples.css');
		@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
	</style>
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	    <script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
	    <script type="text/javascript" src="./js/Digitacao/DigitarNumeroProcesso.js" ></script>
	    <script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
	    <script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
	    <script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
   		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
   		<script type='text/javascript' src='./js/DivFlutuante.js'></script>
		<script type='text/javascript' src='./js/Mensagens.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
		  	<div class="area"><h2>&raquo; <%=request.getAttribute("TituloPagina")%> </h2></div>			
			
			<form action="GuiaEmissaoConsulta" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>">
				
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
					    <legend class="formEdicaoLegenda">Busca de Processos </legend>
                        <label class="formEdicaoLabel" for="ProcessoNumeroCompleto">*Número do Processo</label><br> 
				    	<input class="formEdicaoInput" name="ProcessoNumeroCompleto" id="ProcessoNumeroCompleto"  type="text" size="30" maxlength="25" value="<%=guiaEmissaoConsultaDt.getNumeroProcessoCompletoDt().getNumeroCompletoProcesso()%>" onkeyup="mascara(this, '#######.##.####.#.##.####'); autoTab(this,25)" onkeypress="return DigitarNumeroProcesso(this, event)" />
				    	<em><strong> Nova Numeração</strong>:  Digite o Número do Processo completo. Ex. <strong>5000280.28.2010.8.09.0059</strong></em>
				    	<br />			    
			    		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">
							<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						</div>
					</fieldset>		
				</div>
				
				<%
									if(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt() != null) {
								%>
				
					<div id="divEditar" class="divEditar">
						<fieldset class="formEdicao">
		                	<legend class="formEdicaoLegenda">Guias do Processo</legend>
		                	<br />
		                	
		                	<fieldset id="VisualizaDados" class="VisualizaDados">
		                		<legend>Processo</legend>
		                	
		                		<div> Número </div>
		                			<%
		                				if(guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getSistemaProcessoTJGO().equals(EnumSistemaProcessoTJGO.projudi)) {
		                			%>		                			
										<span><a href="<%=request.getAttribute("tempRetornoBuscaProcesso")%>?Id_Processo=<%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getId()%>&PassoBusca=2"><%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getNumeroProcessoCompletoDt().getNumeroCompletoProcesso()%></a></span>										 
									<%
										 										} else {
										 									%>
										<span><%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getNumeroProcessoCompletoDt().getNumeroCompletoProcesso()%></span> 
									<%
 										}
 									%>
								<div> Sistema </div>
							    <span><%=guiaEmissaoConsultaDt.getProcessoGuiaEmissaoConsultaDt().getSistemaProcessoTJGO().getValor()%></span>
		                	</fieldset>
		                	
							<br/><br/>
							
							<fieldset id="VisualizaDados" class="VisualizaDados">
		                		<legend>Guias</legend>
		                		
								<table id="Tabela" class="Tabela">
									<thead>
										<tr>
											<th>Nº</th>
											<th>Número Guia</th>
											<th>Tipo Guia</th>
											<th>Data Emissão</th>
											<th>Data Vencimento</th>
											<th>Data Recebimento</th>
											<th>Data Cancelamento</th>
											<th>Situação</th>
											<%if( request.getAttribute("apresentaBotaoCancelar") != null && request.getAttribute("apresentaBotaoCancelar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA) ) { %>
												<th>Cancelar?</th>
											<%} %>
										</tr>
									</thead>
									<tbody id="tabListaEscala">
									<%
									if(guiaEmissaoConsultaDt.getListaDeGuiasEmitidas() != null && guiaEmissaoConsultaDt.getListaDeGuiasEmitidas().size() > 0 ) {
										int i = -1;
										for(GuiaEmissaoCompletaDt objTemp : guiaEmissaoConsultaDt.getListaDeGuiasEmitidas()) {
											i += 1;
										%>
											<tr>
												<td align="center"><%=(i + 1)%></td>
												
												<td align="center">
													<%
													if( !objTemp.getId_GuiaStatus().equals(GuiaStatusDt.CANCELADA) ) {
														%>
														<a href="<%=request.getAttribute("tempRetorno")%>?hash=<%=Funcoes.GeraHashMd5(objTemp.getId() + GuiaEmissaoNe.NUMERO_SERIE_GUIA)%>&PaginaAtual=<%=Configuracao.Curinga6%>&PassoEditar=<%=Configuracao.Curinga8%>&Id_GuiaEmissao=<%=objTemp.getId()%>&Id_GuiaTipo=<%=objTemp.getGuiaModeloDt().getId_GuiaTipo()%>">
														<%
													}
													%>
														<%=Funcoes.FormatarNumeroSerieGuia(objTemp.getNumeroGuiaCompleto())%>
													<%
													if( !objTemp.getId_GuiaStatus().equals(GuiaStatusDt.CANCELADA) ) {
														%>
														</a>
														<%
													}
													%>
												</td>
												
												<td>
													<%if( objTemp.getGuiaTipo() != null && objTemp.getGuiaTipo().length() > 0 ) { %>
														<%=objTemp.getGuiaTipo() %>
													<%}
													else { %>
														<%=objTemp.getGuiaModeloDt().getGuiaTipo() %>
													<%} %>
												</td>
												
												<td align="center">
													<%=Funcoes.FormatarDataHora(objTemp.getDataEmissao())%>
												</td>
												
												<td align="center">
													<%=Funcoes.TelaData(objTemp.getDataVencimento()) %>
												</td>
												
												<td align="center">
													<%=Funcoes.TelaData(objTemp.getDataRecebimento()) %>
												</td>
												
												<td align="center">
													<%=Funcoes.FormatarDataHora(objTemp.getDataCancelamento()) %>
												</td>
												
												<td align="center">
													<%=objTemp.getGuiaStatus() %>
												</td>
												
												<%if( request.getAttribute("apresentaBotaoCancelar") != null && request.getAttribute("apresentaBotaoCancelar").toString().equals(GuiaEmissaoNe.APRESENTAR_BOTAO_CANCELAR_GUIA) ) { %>
													<%if( objTemp.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.AGUARDANDO_PAGAMENTO)) || objTemp.getId_GuiaStatus().equals(String.valueOf(GuiaStatusDt.ESTORNO_BANCARIO)) ) { %>
														<td align="center">
															<center>
																&nbsp;&nbsp;&nbsp;&nbsp;
																&nbsp;&nbsp;&nbsp;&nbsp;
																<input class="FormEdicaoimgLocalizar" id="imaCancelarGuia" name="imaCancelarGuia" type="image"  src="./imagens/16x16/remove.png"  onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga9%>');AlterarValue('GuiaCancelar','<%=objTemp.getId()%>');" />
															</center>
														</td>
													<%} %>
												<%} %>
											</tr>
									<%
										}
									}
									else {
										%>
										<tr>
											<td colspan="9">
												<em> Nenhuma Guia Emitida Localizada para este Processo. </em>
											</td>
										</tr>
										<%
									}
									%>
									</tbody>
								</table>
							</fieldset>
							
		                </fieldset>
					</div>	
				<% } %>	
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>